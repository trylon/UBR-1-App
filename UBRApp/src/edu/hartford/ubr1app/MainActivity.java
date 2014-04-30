package edu.hartford.ubr1app;

import edu.hartford.ubr1app.R;
import edu.hartford.ubr1app.R.layout;
import edu.hartford.ubr1app.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Main Activity
 * 
 * Handles transitioning between activities and updating the main interface 
 * according to the current connection status.
 * 
 * @authors TLawless, ZGuan, JHenricks, MStjarre
 */
public class MainActivity extends Activity {
	
	public final static String IP_PORT = "edu.hartford.ubr1app.IP_PORT";
	private String socketString;

	/**
	 * Initializes new activity or re-initializes previous activity
	 * 
	 * @param savedInstanceState null if new activity, or saved 
	 * 		bundle from prior activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		updateViews();
	}


	/**
	 * Creates new connection intent and start activity
	 * 
	 * @param view **unused**
	 */
	public void connectOnClick(View view) {
		Intent connectIntent = new Intent(this, ConnectionActivity.class);
		startActivity(connectIntent);
	}

	/**
	 * Creates new drive intent and start activity
	 * 
	 * @param view **unused**
	 */
	public void driveOnClick(View view) {
		Intent driveIntent = new Intent(this, DashboardActivity.class);
		driveIntent.putExtra(IP_PORT, socketString);
		startActivity(driveIntent);
	}

	/**
	 * Overrides and implements custom options menu when present
	 * 
	 * @param menu XML menu to be implemented
	 * @return boolean on success
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Retrieves intent and updates buttons and socketString accordingly
	 */
	@Override
	public void onResume() {
		super.onResume();

		Intent returnIntent = getIntent();
		if (returnIntent != null) 
		{
			socketString = returnIntent.getStringExtra(ConnectionActivity.IP_PORT);
			updateViews();
		}
	}

	/**
	 * Called when connection changes in the application. Updates buttons
	 */
	private void updateViews() {
		Button connectButton = (Button) findViewById(R.id.connect);
		Button driveButton = (Button) findViewById(R.id.drive);

		if (isValidSocket()) {
			connectButton.setText(R.string.manage_connection);
			driveButton.setVisibility(View.VISIBLE);
		} else {
			connectButton.setText(R.string.connect);
			driveButton.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Checks for socket validity
	 * 
	 * @return boolean true if socket is not null
	 */
	public boolean isValidSocket() {
		return socketString != null;		
	}

	/**
	 * Mutator for current socket string
	 * 
	 * @param newSocket the new socket to mutate current
	 */
	public void setSocket(String newSocket) {		
		socketString = newSocket;		
	}

	/**
	 * Mutator for dumping current socket
	 */
	public void disconnectSocket() {		
		socketString = null;
	}

}
