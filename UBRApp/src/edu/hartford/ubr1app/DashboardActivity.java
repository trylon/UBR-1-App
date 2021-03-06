package edu.hartford.ubr1app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Dash Board Activity
 * 
 * Handles controlling the robot remotely by dispatching commands.
 * 
 * @author bShipman
 * @author roberthilton - Socket Connections
 */
public class DashboardActivity extends Activity {

	// Boolean flags that will be used to keep the same message from being sent
	// multiple times.
	private boolean previousStop = false;
	private boolean previousForward = false;
	private boolean previousLeftTurn = false;
	private boolean previousRightTurn = false;
	private boolean previousLeftStep = false;
	private boolean previousRightStep = false;

	private boolean isStiffened = false;
	private boolean isStanding = false;

	private boolean isVideoStarted = false;

	protected String ip;
	protected int port;
	protected Timer videoTimer;
	protected VideoTimerTask vTimerTask;
	protected ASyncVideo av;

	protected int numFrames = 0;
	protected int numFailFrames = 0;
	protected long startTime;
	protected long endTime;

	Socket socket;
	PrintWriter outputSocketWriter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		Intent returnIntent = getIntent();
		final TextView speedText = (TextView) findViewById(R.id.txtSpeed);
		speedText.setText(getResources().getString(R.string.walkSpeed) + ": 0");
		SeekBar s = (SeekBar) findViewById(R.id.motionSpeed);
		s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				float f = (float) ((seekBar.getProgress() - 10)/10.0);
				speedText.setText(getResources().getString(R.string.walkSpeed) + ": " + f);
			}
		});
		if (returnIntent != null) {
			/*String[] ipPort = returnIntent.getStringExtra(MainActivity.IP_PORT)
					.split(":");*/
			String[] ipPort = {"127.0.0.1", "5997"};
			ip = ipPort[0];
			port = Integer.parseInt(ipPort[1]);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		videoTimer.cancel();
		isVideoStarted = false;
		eStopOnClick(null);
		try {
			socket.close();
		} catch (Exception ex) {

		}
	}

	@Override
	public void onStop() {
		super.onStop();
		videoTimer.cancel();
		isVideoStarted = false;
		eStopOnClick(null);
		try {
			socket.close();
		} catch (Exception ex) {

		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Helper method to connect the socket.
	 */
	private void tryConnectSocket() {
		if (ip != null && port != 0) {
			try {
				socket = new Socket(ip, port);
				socket.setKeepAlive(true);
				outputSocketWriter = new PrintWriter(socket.getOutputStream(),
						true);
			} catch (Exception ex) {

			}
		}
	}

	/**
	 * Helper method to disconnect the socket
	 */
	private void tryDisconnectSocket() {
		try {
			outputSocketWriter.close();
			socket.close();
		} catch (Exception ex) {

		}
	}

	public void sayMessage(View view) {
		EditText textMessage = (EditText) findViewById(R.id.textMessage);
		String msg = textMessage.getText().toString();
		tryConnectSocket();
		outputSocketWriter.println(msg);
		tryDisconnectSocket();
	}

	/**
	 * Emergency Stop Message Dispatched that stops, sits and un-stiffens the
	 * robot.
	 */
	public void stopOnClick(View view) {

		// Get a handle to the text view.
		TextView t = new TextView(this);
		t = (TextView) findViewById(R.id.outputTextView1);
		tryConnectSocket();
		if (outputSocketWriter == null)
			return;
		// Check if this is the first time the stop button is pressed.
		if (!previousStop) {
			t.setText(getResources().getString(R.string.StopCommand));
			outputSocketWriter.println(getResources().getString(
					R.string.StopCommand));
		}
		// If this isn't the first press sit the robot then unstiffen.
		else {
			// If the robot is standing make it sit.
			if (isStanding && isStiffened) {
				t.setText(getResources().getString(R.string.SitCommand));
				outputSocketWriter.println(getResources().getString(
						R.string.SitCommand));
				isStanding = false;
			}

			// If the robot is sitting unstiffen it.
			else if (!isStanding && isStiffened) {
				t.setText(getResources().getString(R.string.UnstiffenCommand));
				outputSocketWriter.println(getResources().getString(
						R.string.UnstiffenCommand));
				isStiffened = false;
			}
		}
		tryDisconnectSocket();
		previousStop = true;
		previousForward = false;
		previousLeftTurn = false;
		previousRightTurn = false;
		previousLeftStep = false;
		previousRightStep = false;
	}

	public void updateVideo(View view) {

		if (!isVideoStarted) {
			startTime = System.currentTimeMillis();
			vTimerTask = new VideoTimerTask();
			videoTimer = new Timer();
			videoTimer.schedule(vTimerTask, 0, 52);
			isVideoStarted = true;
		} else {
			endTime = System.currentTimeMillis();
			videoTimer.cancel();
			isVideoStarted = false;
			Log.d("VIDEO_FRAMES", "Average FPS: "
					+ (numFrames / ((endTime - startTime) / 1000.0)));
			Log.d("VIDEO_FRAMES", "Total Event Overlaps: " + numFailFrames);
			numFrames = 0;
			numFailFrames = 0;
		}
	}

	protected class VideoTimerTask extends TimerTask {

		@Override
		public void run() {
			// Code will not start another task until the last is complete
			if (av != null) {
				if (av.isDone()) {
					av = new ASyncVideo();
					av.execute();
				} else {
					numFailFrames++;
					Log.d("VIDEO_FRAMES",
							"Event triggered before prior process finished!");
				}
			} else {
				av = new ASyncVideo();
				av.execute();
			}

			// Code will immediately start another task - floods images
			// (improves framerate, but delays disabling video)
			// new ASyncVideo().execute();
		}

	}

	protected class ASyncVideo extends AsyncTask<Void, Void, Bitmap> {

		private boolean done = false;

		@Override
		protected Bitmap doInBackground(Void... params) {

			try {
				Socket sImage = new Socket(ip, port);
				PrintWriter p = new PrintWriter(sImage.getOutputStream(), true);
				DataInputStream streamInput = new DataInputStream(
						sImage.getInputStream());
				p.println(getResources().getString(R.string.VideoCommand));
				Bitmap b = BitmapFactory.decodeStream(streamInput);
				streamInput.close();
				p.close();
				sImage.close();
				return b;
			} catch (Exception ex) {
				return null;
			}
		}

		protected void onPostExecute(Bitmap bMap) {
			if (bMap != null) {
				ImageView robotView = (ImageView) findViewById(R.id.imageView1);
				robotView.setImageBitmap(bMap);
				numFrames++;
			}
			done = true;
		}

		protected boolean isDone() {
			return done;
		}

	}

	/**
	 * Emergency Stop
	 */
	public void eStopOnClick(View view) {
		TextView t = new TextView(this);
		t = (TextView) findViewById(R.id.outputTextView1);
		t.setText(getResources().getString(R.string.EStop));
		tryConnectSocket();
		outputSocketWriter.println(getResources().getString(R.string.EStop));
		tryDisconnectSocket();
		previousStop = true;
		isStiffened = false;
		isStanding = false;
		previousForward = false;
		previousLeftTurn = false;
		previousRightTurn = false;
		previousLeftStep = false;
		previousRightStep = false;
	}

	/**
	 * Message Dispatcher that moves the robot forward while pressed.
	 * 
	 * @param veiw
	 *            View being passed.
	 */
	public void forwardOnClick(View veiw) {

		// Get a handle to the textView
		TextView t = new TextView(this);
		t = (TextView) findViewById(R.id.outputTextView1);

		tryConnectSocket();
		if (outputSocketWriter == null)
			return;
		// Check if the robot is standing before moving.
		if (isStiffened && isStanding) {

			// Set the text view string to the walk command.
			t.setText(getResources().getString(R.string.WalkCommand));
			outputSocketWriter.println(getResources().getString(
					R.string.WalkCommand));
			SeekBar s = (SeekBar) findViewById(R.id.motionSpeed);
			int val = s.getProgress() - 10;
			float f = (float) (val / 10.0);
			outputSocketWriter.println(f);
		}
		// If the robot is not stiffened stiffen it.
		else if (!isStiffened) {
			t.setText(getResources().getString(R.string.StiffenCommand));
			outputSocketWriter.println(getResources().getString(
					R.string.StiffenCommand));
			isStiffened = true;
		}
		// If the robot is not standing stand it up.
		else if (!isStanding) {
			t.setText(getResources().getString(R.string.StandCommand));
			outputSocketWriter.println(getResources().getString(
					R.string.StandCommand));
			isStanding = true;
		}
		tryDisconnectSocket();
		previousStop = false;
		previousForward = true;
		previousLeftTurn = false;
		previousRightTurn = false;
		previousLeftStep = false;
		previousRightStep = false;
	}

	/**
	 * Message dispatcher that turns the robot left while pressed.
	 */
	public void turnLeftOnClick(View view) {
		TextView t = new TextView(this);
		t = (TextView) findViewById(R.id.outputTextView1);
		t.setText(getResources().getString(R.string.TurnLeftCommand));

		tryConnectSocket();
		if (outputSocketWriter == null)
			return;

		outputSocketWriter.println(getResources().getString(
				R.string.TurnLeftCommand));
		tryDisconnectSocket();
		previousStop = false;
		previousForward = false;
		previousLeftTurn = true;
		previousRightTurn = false;
		previousLeftStep = false;
		previousRightStep = false;
	}

	/**
	 * Message dispatcher that turns the robot right while pressed.
	 */
	public void turnRightOnClick(View view) {
		TextView t = new TextView(this);
		t = (TextView) findViewById(R.id.outputTextView1);
		t.setText(getResources().getString(R.string.TurnRightCommand));

		tryConnectSocket();
		if (outputSocketWriter == null)
			return;
		outputSocketWriter.println(getResources().getString(
				R.string.TurnRightCommand));
		tryDisconnectSocket();
		previousStop = false;
		previousForward = false;
		previousLeftTurn = false;
		previousRightTurn = true;
		previousLeftStep = false;
		previousRightStep = false;

	}

	/**
	 * Message dispatcher that has the robot step left while pressed.
	 */
	public void stepLeftOnClick(View view) {
		TextView t = new TextView(this);
		t = (TextView) findViewById(R.id.outputTextView1);
		t.setText(getResources().getString(R.string.StepLeftCommand));

		tryConnectSocket();
		if (outputSocketWriter == null)
			return;
		outputSocketWriter.println(getResources().getString(
				R.string.StepLeftCommand));
		tryDisconnectSocket();
		previousStop = false;
		previousForward = false;
		previousLeftTurn = false;
		previousRightTurn = false;
		previousLeftStep = true;
		previousRightStep = false;
	}

	/**
	 * Message dispatcher that has the robot step right while pressed.
	 */
	public void stepRightOnClick(View view) {
		TextView t = new TextView(this);
		t = (TextView) findViewById(R.id.outputTextView1);
		t.setText(getResources().getString(R.string.StepRightCommand));

		tryConnectSocket();
		if (outputSocketWriter == null)
			return;
		outputSocketWriter.println(getResources().getString(
				R.string.StepRightCommand));
		tryDisconnectSocket();
		previousStop = false;
		previousForward = false;
		previousLeftTurn = false;
		previousRightTurn = false;
		previousLeftStep = false;
		previousRightStep = true;
	}

}
