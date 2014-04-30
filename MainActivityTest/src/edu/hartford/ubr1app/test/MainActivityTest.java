package edu.hartford.ubr1app.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import edu.hartford.ubr1app.MainActivity;



public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {
	
	private MainActivity mMainActivity;
	
	public MainActivityTest(){
		
		super(MainActivity.class);
		
	}
	
	@Override
	 
	protected void setUp() throws Exception {
		super.setUp();
		
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
		        MainActivity.class);
		startActivity(intent, null, null);
		
		mMainActivity = getActivity();
		
	}
	
	public void testPreconditions(){
		
		//passes test if the MainActivity was created sucessfully. 
		assertNotNull("mMainActivity is null...", mMainActivity );
			
	}
	
	public void testSocketState()
	{	
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
