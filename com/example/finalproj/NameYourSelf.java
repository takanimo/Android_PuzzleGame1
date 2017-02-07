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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NameYourSelf extends Activity{
	Button okfromnbtn;
	Intent nametobluetooth;
	boolean singlePlayer;
	SoundPool sound_pool;
	HashMap<Integer,Integer> soundMap;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.name_yourself);
		okfromnbtn=(Button)findViewById(R.id.oknamebtn);
		initSound();
		okfromnbtn.setOnClickListener(clicked);
		Intent intent = getIntent();
		if (intent != null && "Yes".equals(intent.getStringExtra("singlePlayer"))) {
			singlePlayer = true;
		}
		
	}
	private void initSound(){
		try{
			
			sound_pool=new SoundPool(2,AudioManager.STREAM_MUSIC,0);
			soundMap=new HashMap<Integer,Integer>();
			//soundMap.put(1, sound_pool.load(MainActivity.this,R.raw.bgpuzzle,1));
			soundMap.put(1, sound_pool.load(NameYourSelf.this, R.raw.btn_sound,1));
			soundMap.put(2, sound_pool.load(NameYourSelf.this, R.raw.gun_sound,1));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	View.OnClickListener clicked=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.oknamebtn:
				if (singlePlayer) {
					EditText et = (EditText) findViewById(R.id.usernametxt);
					Intent singlePuzzle = new Intent(NameYourSelf.this, SinglePuzzleActivity.class);
					singlePuzzle.putExtra("Name", et.getText().toString());
					sound_pool.play(1,1,1,1,0,1f);
					MusicManager.stopper(MusicManager.MUSIC_MENU);
					startActivityForResult(singlePuzzle, 0);
					finish();
				} else {
					EditText et2 = (EditText) findViewById(R.id.usernametxt);
				nametobluetooth=new Intent(NameYourSelf.this,Setup.class);
				nametobluetooth.putExtra("Name",et2.getText().toString());
				sound_pool.play(1,1,1,1,0,1f);
				MusicManager.stopper(MusicManager.MUSIC_MENU);
				startActivity(nametobluetooth);
				finish();
				}
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
			return new AlertDialog.Builder(NameYourSelf.this)
					.setMessage(R.string.checker).setCancelable(false)
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int id) {
						
							//mpSong.stop();
							NameYourSelf.this.finish();
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



