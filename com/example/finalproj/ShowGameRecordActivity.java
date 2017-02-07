package com.example.finalproj;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ShowGameRecordActivity extends Activity {
	private SQLiteDatabase sldb;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.showgamerecord_layout);
	        initDatabase();
	        showRecords();
	       
	    }
	 private void initDatabase() {
			SQL db = new SQL(this);
			sldb = db.getReadableDatabase();
		/*	sldb.execSQL("DROP TABLE IF EXISTS 'record';");
			sldb.execSQL("CREATE TABLE record ("
					+ " _id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "name TEXT NOT NULL, "
					+ "split_by INTEGER NOT NULL, "
				    + "time_spend INTEGER NOT NULL, "
				    + "self TEXT NOT NULL)"
				      );*/
		}
	 private void showRecords() {
			String query = "select * from record order by time_spend";
			Log.i("Query", query);
			TableLayout table = (TableLayout)findViewById(R.id.recordlayout);
			table.removeAllViews();
			TableRow header = new TableRow(this);
			String[] hs = new String[] {"Name", "Split By", "Time(Seconds)"};
			for (String s : hs) {
				TextView tv = new TextView(this);
				tv.setText(s + "\t");
				header.addView(tv);
				tv.setBackgroundColor(Color.BLUE);
			}
			table.addView(header);
			Cursor recordset = sldb.rawQuery(query, null);
			if (recordset.getCount()>0)
			{
				recordset.moveToFirst();
				while(recordset.isAfterLast() == false) {
		            TableRow row = new TableRow(getApplicationContext());
		            for (int j = 1; j < 4; j++) {
		                TextView text = new TextView(getApplicationContext());
		                text.setText(recordset.getString(j) + "\t");
		       
		                text.setBackgroundColor(Color.BLACK);
		                row.addView(text);
		            }
		            table.addView(row);
			        recordset.moveToNext();
				}
			} else {
				Log.i("Query Result", "No records found");
			}
		}

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.recordpage, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	    	int id = item.getItemId();
			if (id == R.id.GoBackMenu) {
				Intent intent = new Intent(ShowGameRecordActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				return true;
			}else if (id == R.id.EndApp) {
				MusicManager.release(MusicManager.MUSIC_MENU);
				finish();
				return true;}
			return super.onOptionsItemSelected(item);
	    }
}