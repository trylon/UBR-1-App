package edu.hartford.ubr1app;

import java.util.regex.Pattern;

import edu.hartford.ubr1app.R;
import edu.hartford.ubr1app.R.layout;
import edu.hartford.ubr1app.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.EditText;

public class ConnectionActivity extends Activity {
	private static final Pattern PARTIAl_IP_ADDRESS = Pattern
			.compile("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])\\.){0,3}"
					+ "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])){0,1}$");
	
	public final static String IP_PORT = "edu.hartford.ubr1app.IP_PORT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		//Validation code from http://stackoverflow.com/questions/3698034/validating-ip-in-android/11545229#11545229
		EditText ipField = (EditText)findViewById(R.id.ipAddr);
		ipField.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			private String mPreviousText = "";

			@Override
			public void afterTextChanged(Editable s) {
				if (PARTIAl_IP_ADDRESS.matcher(s).matches()) {
					mPreviousText = s.toString();
				} else {
					s.replace(0, s.length(), mPreviousText);
				}
			}
		});
	}

	public void startNewActv(View view)
	{
		EditText ipField = (EditText)findViewById(R.id.ipAddr);
		EditText portField = (EditText)findViewById(R.id.port);
		String ipport = ipField.getText().toString() + ":" + portField.getText().toString();
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(IP_PORT, ipport);
        startActivity(intent);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connection, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
