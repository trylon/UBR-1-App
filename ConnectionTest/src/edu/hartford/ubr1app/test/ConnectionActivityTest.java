package edu.hartford.ubr1app.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import edu.hartford.ubr1app.ConnectionActivity;
import edu.hartford.ubr1app.MainActivity;

public class ConnectionActivityTest extends
		ActivityUnitTestCase<ConnectionActivity> {

	private ConnectionActivity mActivity;
	public static final int ADAPTER_COUNT = 9;
	public static final int INITIAL_POSITION = 0;
	public static final int TEST_POSITION = 5;

	public ConnectionActivityTest() {
		super(ConnectionActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				ConnectionActivity.class);
		startActivity(intent, null, null);
		mActivity = getActivity();

	}

	@MediumTest
	public void testConnectionActivity() {
		assertNotNull("IP Field is null",
				mActivity.findViewById(edu.hartford.ubr1app.R.id.ipAddr));
		EditText ip = (EditText) mActivity
				.findViewById(edu.hartford.ubr1app.R.id.ipAddr);
		ip.setText("127.0.0.1");

		assertNotNull("IP Field is null",
				mActivity.findViewById(edu.hartford.ubr1app.R.id.port));
		EditText port = (EditText) mActivity
				.findViewById(edu.hartford.ubr1app.R.id.port);
		port.setText("5997");

		assertNotNull("Button is null",
				mActivity
						.findViewById(edu.hartford.ubr1app.R.id.btnUnitConnect));
		Button b = (Button) mActivity
				.findViewById(edu.hartford.ubr1app.R.id.btnUnitConnect);
		b.performClick();

		Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);

		final String payload = launchIntent
				.getStringExtra(MainActivity.IP_PORT);
		assertEquals("Payload is empty", ip.getText().toString() + ":"
				+ port.getText().toString(), payload);
	}

}
