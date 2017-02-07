package com.example.finalproj;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SimplePuzzleActivity extends Activity{

	private int puzzleImageId = R.drawable.hockey_puck; // image id for puzzle
	private int splitby = 3; // split image by splitby x splitby
	private int blankImageId = R.drawable.ic_launcher; // image id for blank piece
	private Bitmap blankBitmap;
	private Map<Integer, Bitmap> bitmap = new HashMap<Integer, Bitmap>();
	private ImageView selectedPiece;
	private int pieceWidth; // since the piece is square, piece width and height are the same
	private long startTime; // time starting the puzzle
	private long timeSpend; // time spent to finish the puzzle
	private MediaPlayer mpSong; // song to play when finish the puzzle
	private SQLiteDatabase sldb;
	private String name = "v";
	private Animation fadein, fadeout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_puzzle);
		//initSeekBar();
		initImageBitmap();
		initBlankBitmap();
		initAnimation();
		initSong();
		initDatabase();
		startGame();
	}
	
	/*private void initSeekBar() {
		SeekBar b = (SeekBar)findViewById(R.id.seekBar1);
		b.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				splitby = progress < 3 ? 2 : progress;
				startGame();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}*/
	
	private void initImageBitmap() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getRealMetrics(dm);
		Bitmap original = BitmapFactory.decodeResource(getResources(), puzzleImageId);
		Bitmap fullBitmap = Bitmap.createScaledBitmap(original, dm.widthPixels/3, dm.widthPixels/3, true);
		pieceWidth = fullBitmap.getWidth()/splitby;
		
		for (int i =1; i<=splitby*splitby; i++) {
			int tw = fullBitmap.getWidth()/splitby;
			int th = fullBitmap.getHeight()/splitby;
			Bitmap piece = Bitmap.createBitmap(fullBitmap, tw*((i-1)%splitby), ((i-1)/splitby)*th, tw, th);
			bitmap.put(i, piece);
		}
	}
	
	private void initBlankBitmap() {
		Bitmap blank = BitmapFactory.decodeResource(getResources(), blankImageId);
		blankBitmap = blank.createScaledBitmap(blank, pieceWidth, pieceWidth, true);
	}
	
	private void initAnimation() {
		fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
		fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		AnimationListener al = new AnimationListener() {
			public void onAnimationStart(Animation animation){
			}
			public void onAnimationEnd(Animation animation){
				try {
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			public void onAnimationRepeat(Animation animation) {
			}
		 };
	}
	
	private void initSong() {
		try {
			mpSong = new MediaPlayer();
			Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_music);
			mpSong.setDataSource(this, uri);
			mpSong.prepareAsync();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initDatabase() {
		SQL db = new SQL(this);
		sldb = db.getReadableDatabase();
		sldb.execSQL("DROP TABLE IF EXISTS 'record';");
		sldb.execSQL("CREATE TABLE record ("
				+ " _id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "name TEXT NOT NULL, "
				+ "split_by TEXT NOT NULL, "
			    + "time_spend TEXT NOT NULL, "
			    + "self TEXT NOT NULL)"
			      );
	}
	
	private void startGame() {
		buildFullImageLayout();
		buildRandomLayout();
		buildBlankLayout();
		startTime = System.currentTimeMillis();
	}
	
	private void buildRandomLayout() {
		TableLayout table = (TableLayout)findViewById(R.id.randomlayout);
		table.removeAllViews();
		View.OnClickListener cl = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.rowborder);
				if (isBlank((ImageView)v) && selectedPiece != null) {
					replaceBlank(v);
				} else {
					selectedPiece = (ImageView)v;
				}
			}
		};
		Set<Integer> exclude = new HashSet<Integer>();
		for (int i =0; i<splitby; i++) {
			TableRow row = new TableRow(this);
			for (int j=1;j<=splitby;j++) {
				ImageView im = new ImageView(this);
				im.setOnClickListener(cl);
				int r = randomInt(1,splitby*splitby, exclude);
				exclude.add(r);
				im.setImageBitmap(getBitmap(r));
				row.addView(im);
			}
			table.addView(row);
		}
	}
	
	private void buildFullImageLayout() {
		TableLayout table = (TableLayout)findViewById(R.id.fulllayout);
		table.removeAllViews();
		for (int i =0; i<splitby; i++) {
			TableRow row = new TableRow(this);
			for (int j=1;j<=splitby;j++) {
				ImageView im = new ImageView(this);
				im.setImageBitmap(bitmap.get((i*splitby)+j));
				row.addView(im);
			}
			table.addView(row);
		}
	}
	
	private void buildBlankLayout() {
		TableLayout table = (TableLayout)findViewById(R.id.blanklayout);
		table.removeAllViews();
		View.OnClickListener cl = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isBlank((ImageView)v) && selectedPiece != null) {
					replaceBlank(v);
					if (checkWin()) {
						timeSpend = System.currentTimeMillis()-startTime;
						showCongratulationDlg();
						insertRecord();
						mpSong.start();
					}
				} else {
					selectedPiece = (ImageView)v;
				}
			}
		};
		for (int i =0; i<splitby; i++) {
			TableRow row = new TableRow(this);
			for (int j=1;j<=splitby;j++) {
				ImageView im = new ImageView(this);
				im.setImageBitmap(blankBitmap);
				im.setBackgroundResource(R.drawable.rowborder);
				im.setOnClickListener(cl);
				row.addView(im);
			}
			table.addView(row);
		}
	}
	private void replaceBlank(View v) {
		selectedPiece.startAnimation(fadeout);
		v.startAnimation(fadein);
		Bitmap bm = ((BitmapDrawable)(selectedPiece.getDrawable())).getBitmap();
		((ImageView)v).setImageBitmap(bm);
		selectedPiece.setImageBitmap(blankBitmap);
	}
	
	public void insertRecord() {
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("split_by", splitby);
		values.put("time_spend", timeSpend);
		values.put("self", "Y");
		long id = sldb.insert("record", null, values);
		showRecords();
	}
	public void showRecords() {
		String query = "select * from record order by time_spend";
		Log.i("Query", query);
		TableLayout table = (TableLayout)findViewById(R.id.recordlayout);
		table.removeAllViews();
		Cursor recordset = sldb.rawQuery(query, null);
		if (recordset.getCount()>0)
		{
			recordset.moveToFirst();
			while(recordset.isAfterLast() == false) {
	            TableRow row = new TableRow(getApplicationContext());
	            for (int j = 0; j < 5; j++) {
	                TextView text = new TextView(getApplicationContext());
	                text.setText(recordset.getString(j) + "\t");
	       
	                text.setBackgroundColor(Color.BLACK);
	                //Log.i("Column ", recordset.getString(j));
	                row.addView(text);
	            }
	            table.addView(row);
		        recordset.moveToNext();
			}
		} else {
			Log.i("Query Result", "No records found");
		}
	}
	private boolean isBlank(ImageView v) {
		return blankBitmap.equals(((BitmapDrawable)(v.getDrawable())).getBitmap());
	}
	private void showCongratulationDlg() {
		AlertDialog dlg = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Puzzle Game");
		final TextView view = new TextView(this);
		String t = timeSpend < getMinTimeSpend() ? "Congratulation, You beat the record!!!" : "Congratulation!!!";
		view.setText(t+"\n Time: " + timeSpend + "ms\nReplay?");
		builder.setView(view);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					startGame();
					mpSong.pause();
					mpSong.seekTo(0);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}				
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dlg = builder.create();
		dlg.show();
	}
	private long getMinTimeSpend() {
		Cursor recordset = sldb.rawQuery("select min(time_spend) from record where split_by = '" + splitby+"'", null);
		if (recordset.getCount()>0)
		{
			recordset.moveToFirst();
			while(recordset.isAfterLast() == false) {
				Log.e("min", recordset.getString(0)== null ? "null": recordset.getString(0));
	            return recordset.getLong(0);
		       // recordset.moveToNext();
			}
		} else {
			Log.i("Query Result", "No records found");
		}
		return 0;
	}
	private boolean checkWin() {
		TableLayout table = (TableLayout)findViewById(R.id.blanklayout);
		for (int i = 0; i <table.getChildCount();i++) {
			TableRow row = (TableRow)table.getChildAt(i);
			for (int j=0; j<row.getChildCount();j++) {
				ImageView v = (ImageView)row.getChildAt(j);
				Bitmap bm = ((BitmapDrawable)v.getDrawable()).getBitmap();
				int id = (splitby*i)+j+1;
				if (!bm.equals(bitmap.get(id))) {
					return false;
				}
			}
		}
		return true;
	}
	
	private int randomInt(int start, int end, Set<Integer> exclude) {
		Random rnd = new Random();
		int random = start + rnd.nextInt(end - start + 1 - exclude.size());
		for (int i : exclude) {
		    if (!exclude.contains(random)) {
		        return random;
		    } 
		    random++;
	    }
	    return random;
	}
	
	private Bitmap getBitmap(int i) {
		return bitmap.get(i);
	}

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
		if (id == R.id.show_record) {
			showRecords();
			return true;
		} else if (id == R.id.replay) {
			startGame();
			return true;
		}else if(id==R.id.EndApp){
			
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

