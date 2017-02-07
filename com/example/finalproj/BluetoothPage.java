package com.example.finalproj;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class BluetoothPage extends Activity{
	//Button okfromnbtn;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.server_client);
		//okfromnbtn=(Button)findViewById(R.id.oknamebtn);
		
		//okfromnbtn.setOnClickListener(clicked);
		
	}
	View.OnClickListener clicked=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			//case R.id.oknamebtn:
				//gonextpage=new Intent(MainActivity.this,HowDoYouPlay.class);
				//startActivity(gonextpage);
				//finish();
				//break;
			
			}
			
		}
	};
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.EndApp) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
