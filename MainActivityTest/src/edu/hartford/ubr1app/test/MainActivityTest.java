package edu.hartford.ubr1app.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
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
		
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
		        MainActivity.class);
		startActivity(intent, null, null);
		
		mMainActivity = getActivity();		
	}
	
	/**
	 * Test for checking creation of MainActivity
	 */
	public void testPreconditions() {		 
		assertNotNull("mMainActivity is null...", mMainActivity );			
	}
	
	/**
	 * Test for checking sockets
	 */
	public void testSocketState() {	
		 assertTrue(!mMainActivity.isValidSocket());
		 mMainActivity.setSocket("localhost:4096");
		 assertTrue(mMainActivity.isValidSocket());	
	}
	
	/*public void testButtonState()
	{
		mMainActivity.setSocket(null);
		//when connection is null		
	}
	
	public void testIntentData()
	{		
	}*/
}
