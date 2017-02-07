package com.example.finalproj;


import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Button startbtn, gallerybtn, recordbtn, exitbtn;
	Intent gonextpage;
	SoundPool sound_pool;
	HashMap<Integer,Integer> soundMap;
	MediaPlayer mpSong;
	boolean isMainPlaying=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuscreen);
		startbtn=(Button)findViewById(R.id.strbtn);
		gallerybtn=(Button)findViewById(R.id.glrbtn);
		recordbtn=(Button)findViewById(R.id.rcdbtn);
		exitbtn=(Button)findViewById(R.id.extbtn);
		startbtn.setOnClickListener(clicked);
		gallerybtn.setOnClickListener(clicked);
		recordbtn.setOnClickListener(clicked);
		exitbtn.setOnClickListener(clicked);
		//if(isMainPlaying=false){
		isMainPlaying=true;//}
		if(isMainPlaying=true)
		MusicManager.start(this, MusicManager.MUSIC_MENU);
		initSound();
		
		//mpSong.start();
	}
	protected void onPause(){
		super.onPause();
		
	}
	protected void onRestart(){
		super.onRestart();
		MusicManager.start(this, MusicManager.MUSIC_MENU);
		//ContentValues values=new ContentValues();
		//getContentResolver().update(mUri, values, null, null);
	}
	
	private void initSound(){
		try{
			
			sound_pool=new SoundPool(2,AudioManager.STREAM_MUSIC,0);
			soundMap=new HashMap<Integer,Integer>();
			//soundMap.put(1, sound_pool.load(MainActivity.this,R.raw.bgpuzzle,1));
			soundMap.put(1, sound_pool.load(MainActivity.this, R.raw.btn_sound,1));
			soundMap.put(2, sound_pool.load(MainActivity.this, R.raw.gun_sound,1));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
/*private OnPreparedListener okStart=new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			mpSong.start();
			isMainPlaying=true;
		}
	};*/
	View.OnClickListener clicked=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.strbtn:
				gonextpage=new Intent(MainActivity.this,HowDoYouPlay.class);
				sound_pool.play(1,1,1,1,0,1f);
				startActivity(gonextpage);
				 //mpSong.stop();
				 
				
				finish();
				break;
			case R.id.extbtn:
				//mpSong.stop();
				
				sound_pool.play(2,1,1,1,0,1f);
				MusicManager.release(MusicManager.MUSIC_MENU);
				isMainPlaying=false;
				finish();
				break;
			case R.id.glrbtn:
				gonextpage=new Intent(MainActivity.this,GalleryPage.class);
				sound_pool.play(1,1,1,1,0,1f);
				startActivity(gonextpage);
				//mpSong.stop();
				finish();
				break;
			case R.id.rcdbtn:
				gonextpage=new Intent(MainActivity.this,ShowGameRecordActivity.class);
				sound_pool.play(1,1,1,1,0,1f);
				startActivity(gonextpage);
				//mpSong.stop();
				finish();
				break;
			}
			
		}
	};
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode==KeyEvent.KEYCODE_BACK){
			showDialog(0);
			return true;
		}return false;

	}
	public Dialog onCreateDialog(int id){
		switch(id){
		case 0:
			return new AlertDialog.Builder(MainActivity.this)
					.setMessage(R.string.checker).setCancelable(false)
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int id) {
						
							mpSong.stop();
							MainActivity.this.finish();
						}
						})
					.setNegativeButton(R.string.no, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int id) {
							
							
						}
					}).create();
		}return null;
					
			
		}
	
	
	
	
}
