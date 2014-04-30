package edu.hartford.ubr1app.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.widget.Button;
import edu.hartford.ubr1app.ConnectionActivity;
import edu.hartford.ubr1app.DashboardActivity;
import edu.hartford.ubr1app.MainActivity;


/**
 * Main Activity Test
 * 
 * Handles testing of socket, activity, and intent
 * 
 * @authors TLawless, ZGuan, JHenricks, MStjarre
 */
public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {
	
	private MainActivity mMainActivity;
	private int connectionButtonId;
	private int driveButtonId;
	private Button connectButton;
	private Button driveButton;
	
	private String testSocketString = "localhost:4096";
	
	/**
	 * Super-constructor for MainActivity
	 */
	public MainActivityTest() {		
		super(MainActivity.class);		
	}
	
	/**
	 * Prepare intent for testing and create activity variable
	 * 
	 * @throws Exception
	 */
	@Override	 
	protected void setUp() throws Exception {
		super.setUp();
		
		startActivity(new Intent(), null, null);
		
		mMainActivity = getActivity();	
		
		connectionButtonId = edu.hartford.ubr1app.R.id.connect;
		driveButtonId = edu.hartford.ubr1app.R.id.drive;
		
		connectButton = (Button) mMainActivity.findViewById(connectionButtonId);
		driveButton = (Button) mMainActivity.findViewById(driveButtonId);
	}
	
	/**
	 * Test for checking creation of MainActivity
	 */
	
	public void testPreconditions() {		 
		// check that Activity and Buttons were created OK
		assertNotNull("mMainActivity is null...", mMainActivity );	
		assertNotNull("connectButton view is null..", connectButton);
		assertNotNull("driveButton view is null..", driveButton);
	}
	
	/**
	 * Test teardown
	 */
	
	@Override 
	protected void tearDown() throws Exception {
		
		super.tearDown();
	}
	
	public void reset()
	{
		//reset socketString to null if needed
		if(mMainActivity.isValidSocket())
		{
			mMainActivity.setSocket(null);
		}
	}
	
	
	/**
	 * Test for checking sockets
	 */
	public void testSocketState() {
		
		//reset socketString to null if needed
		reset();
		//passes if socketString starts null
		 assertTrue(!mMainActivity.isValidSocket());
		 //set socketString to a non null value
		 mMainActivity.setSocket(testSocketString);
		 //passes if the socket is not null
		 assertTrue(mMainActivity.isValidSocket());	
	}
	
	/**
	 * Test for checking buttons
	 */
	
	public void testButtonState()
	{
	
		
		//reset socketString to starting null state
		reset();
		
		//check labels at start
		assertEquals("Connect button should read \"Connect\"","Connect", connectButton.getText());
		assertEquals("Drive button should read \"Drive\"","Drive", driveButton.getText());
		
		//check that Connect is visible and Drive is invisible
		assertEquals("At start (null socket), Connect button should be visible", View.VISIBLE, connectButton.getVisibility());
		assertEquals("At start (null socket) Drive button should be invisble", View.INVISIBLE, driveButton.getVisibility());
		
		//switch socket state
		 mMainActivity.setSocket(testSocketString);
		 mMainActivity.updateViews();
		 
		//check that Connect is visible and Drive is now also Visible
		 assertEquals("On valid socket, Connect button should be visible", View.VISIBLE, connectButton.getVisibility());
		 assertEquals("On valid socket, Drive button should be visible", View.VISIBLE, driveButton.getVisibility());
			
		 //check label changes Connect --> Manage Connection
		 assertEquals("Connect button should read \"Manage Connection\"","Manage Connection", connectButton.getText());
		 assertEquals("Drive button should read \"Drive\"","Drive", driveButton.getText());
		
	}
	
	/**
	 * Test for checking sent Intents
	 */
	public void testIntentData()
	{	
		reset();
		connectButton.performClick();
		
		Intent connectionIntent = getStartedActivityIntent();
		assertNotNull("Connection Intent was null", connectionIntent);
		String data = connectionIntent.getStringExtra(ConnectionActivity.IP_PORT);
		assertNull("Outgoing Connection intent contained extra data", data);
		
		 mMainActivity.setSocket(testSocketString);
		 mMainActivity.updateViews();
		
		 driveButton.performClick();
		 Intent driveIntent = getStartedActivityIntent();
		 assertNotNull("Drive Intent was null", driveIntent);
		 String sentSocketData = driveIntent.getStringExtra(MainActivity.IP_PORT);
		 assertEquals("Intent sent incorrect socket data", testSocketString, sentSocketData );
			
	}
}
