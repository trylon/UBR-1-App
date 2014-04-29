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

public class MainActivity extends Activity {

	public final static String IP_PORT = "edu.hartford.ubr1app.IP_PORT";
	private String socketString;

	//private Button connectButton;
	//private Button driveButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		updateViews();
	}

	public void connectOnClick(View view) {
		Intent connectIntent = new Intent(this, ConnectionActivity.class);
		startActivity(connectIntent);
	}

	public void driveOnClick(View view) {
		Intent driveIntent = new Intent(this, DashboardActivity.class);
		driveIntent.putExtra(IP_PORT, socketString);
		startActivity(driveIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		
		//socketString = null;
		Intent returnIntent = getIntent();
		if (returnIntent != null) 
		{
			socketString = returnIntent.getStringExtra(ConnectionActivity.IP_PORT);
			updateViews();
		}
	}

	/**
	 * Call updateViews when something changes in the application
	 */
	private void updateViews() 
	{
		// update connect button
		Button connectButton = (Button) findViewById(R.id.connect);
		Button driveButton = (Button) findViewById(R.id.drive);
		
		if(isValidSocket()) 
		{
			connectButton.setText(R.string.manage_connection);
			driveButton.setVisibility(View.VISIBLE);
		} 
		else 
		{
			connectButton.setText(R.string.connect);
			driveButton.setVisibility(View.INVISIBLE);
		}
	}
	
	
	public boolean isValidSocket()
	{
		//return false;
		return socketString != null;
		
	}
	//for testing
	public void setSocket(String newSocket)
	{
		
		socketString = newSocket;
		
		
	}
	//for testing
	public void disconnectSocket()
	{
		
		socketString = null;
	}
	
}
