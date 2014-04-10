package edu.hartford.ubr1app;

<<<<<<< HEAD:UBRApp/src/edu/hartford/ubr1app/MainActivity.java
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
=======
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

//test comment
public class DashboardActivity extends Activity {
>>>>>>> 5cebaabb82a412fc40070c12b9540399aadaaeac:UBRApp/src/edu/hartford/ubr1app/DashboardActivity.java

public class MainActivity extends Activity {
	
	public final static String IP_PORT = "edu.hartford.ubr1app.IP_PORT";
	private String socketString;
	private boolean connected;
	
	private Button connectButton;
	private Button driveButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD:UBRApp/src/edu/hartford/ubr1app/MainActivity.java
        setContentView(R.layout.activity_main);
        connected = false;
        updateViews();
        
        connectButton.setOnClickListener(new View.OnClickListener() {
=======
        setContentView(R.layout.activity_dashboard);
    }
>>>>>>> 5cebaabb82a412fc40070c12b9540399aadaaeac:UBRApp/src/edu/hartford/ubr1app/DashboardActivity.java

	        public void onClick(View view) {
	        	//Intent connectIntent = new Intent(this, ConnectionActivity.class);
	            //startActivity(connectIntent); 
	        }
	    });
        
        driveButton.setOnClickListener(new View.OnClickListener() {

	        public void onClick(View view) {
	        	/*Intent driveIntent = new Intent(this, DashboardActivity.class);
	        	driveIntent.putExtra(IP_PORT, socketString);
	        	startActivity(driveIntent);*/
	        }
	    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	
    	/*Intent returnIntent = getIntent();
    
    	socketString = returnIntent.getStringExtra(ConnectionActivity.IP_PORT);
    	//Have Connection activity send us this specific test ip for now...
    	if(socketString.equals("127.0.0.1:4096"))
    	{
    		connected = true;
    		updateViews();
    	}*/
    	
    	
    }
    
    /**
     * Call updateViews when something changes in the application
     */
    private void updateViews(){
    	//update connect button
    	connectButton = (Button)findViewById(R.id.connect); 
    	driveButton = (Button)findViewById(R.id.drive);
    	if(connected){
    		connectButton.setText(R.string.disconnect);
    		driveButton.setVisibility(View.VISIBLE);
    	}
    	else{
    		connectButton.setText(R.string.connect);
    		driveButton.setVisibility(View.INVISIBLE);
    	}
    }
}
