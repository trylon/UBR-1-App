package edu.hartford.ubr1app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

//test comment
public class DashboardActivity extends Activity {
	
	private boolean previousStop = false;
	private boolean previousForward = false;
	private boolean previousLeftTurn = false;
	private boolean previousRightTurn = false;
	private boolean previousLeftStep = false;
	private boolean previousRightStep = false;
		

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * Emergency Stop Message Dispatched that stops, sits and 
     * un-stiffens the robot.
     */
    public void stopOnClick(View view){
    	
    }
    
    /**
     * Message Dispatcher that moves the robot forward while pressed.
     */
    public void forwardOnClick(View veiw){
    	
    }
    
    /**
     * Message dispatcher that turns the robot left while pressed.
     */
    public void turnLeftOnClick(View view){
    	
    }
    
    /**
     * Message dispatcher that turns the robot right while pressed.
     */
    public void turnRightOnClick(View view){
    	
    }
    
    /**
     * Message dispatcher that has the robot step left while pressed.
     */
    public void stepLeftOnClick(View view){
    	
    }
    
    /**
     * Message dispatcher that has the robot step right while pressed.
     */
    public void stepRightOnClick(View view){
    	
    }   
   
}