package edu.hartford.ubr1app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

/**
 * Dash Board Activity 
 * 
 * Handles controlling the robot remotely by dispatching commands.
 * 
 * @author bShipman
 *
 */
public class DashboardActivity extends Activity {
	
	// Boolean flags that will be used to keep the same message from being sent multiple times.
	private boolean previousStop = false;
	private boolean previousForward = false;
	private boolean previousLeftTurn = false;
	private boolean previousRightTurn = false;
	private boolean previousLeftStep = false;
	private boolean previousRightStep = false;
		
	private boolean isStiffened = false;
	private boolean isStanding = false;

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
    	
    	// Get a handle to the text view.
    	TextView t = new TextView(this); 
    	t = (TextView)findViewById(R.id.outputTextView1);
    	
    	// Check if this is the first time the stop button is pressed.
    	if (!previousStop){
    		t.setText(getResources().getString(R.string.StopCommand));
    	}
    	// If this isn't the first press sit the robot then unstiffen.
    	else{
    		// If the robot is standing make it sit.
    		if (isStanding && isStiffened){
    			t.setText(getResources().getString(R.string.SitCommand));
    			isStanding = false;
    		}
    		
    		// If the robot is sitting unstiffen it.
    		else if (!isStanding && isStiffened){
    			t.setText(getResources().getString(R.string.UnstiffenCommand));
    			isStiffened = false;
    		}
    	}
    	
    	previousStop = true;
    	previousForward = false;
    	previousLeftTurn = false;
    	previousRightTurn = false;
    	previousLeftStep = false;
    	previousRightStep = false;
    }
    
    /**
     * Message Dispatcher that moves the robot forward while pressed.     
     * @param veiw View being passed.
     */
    public void forwardOnClick(View veiw){

    	// Get a handle to the textView
    	TextView t = new TextView(this); 
    	t = (TextView)findViewById(R.id.outputTextView1);
    	
    	// Check if the robot is standing before moving.
    	if (isStiffened && isStanding){
    		
    		// Set the text view string to the walk command.
	    	t.setText(getResources().getString(R.string.WalkCommand));
    	}
    	// If the robot is not stiffened stiffen it.
    	else if(!isStiffened){
    		t.setText(getResources().getString(R.string.StiffenCommand));
    		isStiffened = true;
    	}
    	// If the robot is not standing stand it up.
    	else if(!isStanding){
    		t.setText(getResources().getString(R.string.StandCommand));
    		isStanding = true;
    	}
    	
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
    public void turnLeftOnClick(View view){
    	TextView t = new TextView(this); 
    	t = (TextView)findViewById(R.id.outputTextView1);
    	t.setText(getResources().getString(R.string.TurnLeftCommand));
    	
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
    public void turnRightOnClick(View view){
    	TextView t = new TextView(this); 
    	t = (TextView)findViewById(R.id.outputTextView1);
    	t.setText(getResources().getString(R.string.TurnRightCommand));
    	
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
    public void stepLeftOnClick(View view){
    	TextView t = new TextView(this); 
    	t = (TextView)findViewById(R.id.outputTextView1);
    	t.setText(getResources().getString(R.string.StepLeftCommand));
    	
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
    public void stepRightOnClick(View view){
    	TextView t = new TextView(this); 
    	t = (TextView)findViewById(R.id.outputTextView1);
    	t.setText(getResources().getString(R.string.StepRightCommand));
    	
    	previousStop = false;
    	previousForward = false;
    	previousLeftTurn = false;
    	previousRightTurn = false;
    	previousLeftStep = false;
    	previousRightStep = true;
    }   
   
}