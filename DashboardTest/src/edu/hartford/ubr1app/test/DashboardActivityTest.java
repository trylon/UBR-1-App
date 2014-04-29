 package edu.hartford.ubr1app.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import edu.hartford.ubr1app.DashboardActivity;

public class DashboardActivityTest extends 
	ActivityUnitTestCase<DashboardActivity>{
	
	private DashboardActivity pActivity;
/*	public static final int ADAPTER_COUNT = 9;
	public static final int INITIAL_POSITION = 0;*/
	public static final int TEST_POSITION = 3;
	
	public DashboardActivityTest() {
		super(DashboardActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				DashboardActivity.class);
		startActivity(intent, null, null);
		pActivity = getActivity();

	}
	
	@MediumTest
	public void testDashboardActivity() {
		Button b = (Button) pActivity.findViewById(edu.hartford.ubr1app.R.id.button2);
		/*TextView textView = (TextView) pActivity.findViewById(R.id.outputTextView1);
		public static string stopCommandArray*/
		for (int i = 1; i <= TEST_POSITION; i++) {
			
			assertEquals(true,b.performClick());
		}
	}

}
