package com.example.finalproj;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class GalleryPage extends Activity{
	SoundPool sound_pool;
	HashMap<Integer,Integer> soundMap;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallerypage);
		initSound();
}
	private void initSound(){
		try{
			
			sound_pool=new SoundPool(2,AudioManager.STREAM_MUSIC,0);
			soundMap=new HashMap<Integer,Integer>();
			//soundMap.put(1, sound_pool.load(MainActivity.this,R.raw.bgpuzzle,1));
			soundMap.put(1, sound_pool.load(GalleryPage.this, R.raw.btn_sound,1));
			soundMap.put(2, sound_pool.load(GalleryPage.this, R.raw.gun_sound,1));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode==KeyEvent.KEYCODE_BACK){
			showDialog(0);
			return true;
		}return false;

	}
	public Dialog onCreateDialog(int id){
		switch(id){
		case 0:
			return new AlertDialog.Builder(GalleryPage.this)
					.setMessage(R.string.checker).setCancelable(false)
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int id) {
						
							//mpSong.stop();
							GalleryPage.this.finish();
						}
						})
					.setNegativeButton(R.string.no, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int id) {
							
							
						}
					}).create();
		}return null;
					
			
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
		if(id==R.id.GoBackMenu){
			Intent gobacktoMenu=new Intent(GalleryPage.this,MainActivity.class);
			sound_pool.play(1,1,1,1,0,1f);
			startActivity(gobacktoMenu);
			finish();
		}else if(id==R.id.EndApp){
			sound_pool.play(2,1,1,1,0,1f);
			MusicManager.release(MusicManager.MUSIC_MENU);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}