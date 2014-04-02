package edu.hartford.ubr1app;

import com.example.myfirstapp.R;
import com.example.myfirstapp.R.layout;
import com.example.myfirstapp.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
//test comment
public class MainActivity extends Activity {

	private boolean connected;
	
	private Button connectButton;
	private Button driveButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connected = false;
        
        this.updateViews();
        
        connectButton.setOnClickListener(new View.OnClickListener() {

	        public void onClick(View view) {
	        	//Intent connectIntent = new Intent(this, going to);
	        	//startActivity(connectIntent);
	        }
	    });
        
        driveButton.setOnClickListener(new View.OnClickListener() {

	        public void onClick(View view) {
	        	//Intent driveIntent = new Intent(this, going to);
	        	//startActivity(driveIntent);
	        }
	    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * Call updateViews when something changes in the application
     */
    private void updateViews(){
    	//update connect button
    	Button connect = (Button)findViewById(R.id.connect); 
    	Button drive = (Button)findViewById(R.id.drive);
    	if(connected){
    		connect.setText(R.string.connect);
    		drive.setVisibility(View.VISIBLE);
    	}
    	else{
    		connect.setText(R.string.disconnect);
    		drive.setVisibility(View.INVISIBLE);
    	}
    }
}
