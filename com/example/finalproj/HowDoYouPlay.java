package com.example.finalproj;

import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HowDoYouPlay extends Activity{
	Button singlebtn, multiplebtn,exitbtn;
	Intent gonameurselfpage;
	boolean isMainPlaying=false;
	SoundPool sound_pool;
	HashMap<Integer,Integer> soundMap;
	private MediaPlayer m;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.how_to_play);
		singlebtn=(Button)findViewById(R.id.sglbtn);
		multiplebtn=(Button)findViewById(R.id.mtlbtn);
		exitbtn=(Button)findViewById(R.id.extbtn);
		singlebtn.setOnClickListener(clicked);
		multiplebtn.setOnClickListener(clicked);
		exitbtn.setOnClickListener(clicked);
		m=new MediaPlayer();
		isMainPlaying=true;
		if(isMainPlaying=true)
		MusicManager.start(this, MusicManager.MUSIC_MENU);
		//Uri uri = Uri.parse("android.resource://" + HowDoYouPlay.this.getPackageName() + "/" + R.raw.bgpuzzle);
		/*try {
			m.setDataSource(HowDoYouPlay.this, uri);
			m.prepareAsync();
			m.setOnPreparedListener(okStart);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//m = MediaPlayer.create(this, R.raw.backgroundhowto);
		//m.prepareAsync();
		//m.start();
		//m.setLooping(true);		*/
		initSound();
	}
	
	private void initSound(){
		try{
			
			sound_pool=new SoundPool(2,AudioManager.STREAM_MUSIC,0);
			soundMap=new HashMap<Integer,Integer>();
			//soundMap.put(1, sound_pool.load(MainActivity.this,R.raw.bgpuzzle,1));
			soundMap.put(1, sound_pool.load(HowDoYouPlay.this, R.raw.btn_sound,1));
			soundMap.put(2, sound_pool.load(HowDoYouPlay.this, R.raw.gun_sound,1));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/*private OnPreparedListener okStart=new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			m.start();
			isMainPlaying=true;
		}
	};*/
	View.OnClickListener clicked=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.mtlbtn:
				sound_pool.play(1,1,1,1,0,1f);
				//gonextpage=new Intent(MainActivity.this,HowDoYouPlay.class);
				//startActivity(gonextpage);
				gonameurselfpage=new Intent(HowDoYouPlay.this,NameYourSelf.class);
				
				startActivity(gonameurselfpage);
				//m.stop();
				finish();
				break;
			case R.id.sglbtn:
				sound_pool.play(1,1,1,1,0,1f);
				gonameurselfpage = new Intent(HowDoYouPlay.this, NameYourSelf.class);
				gonameurselfpage.putExtra("singlePlayer", "Yes");
				startActivityForResult(gonameurselfpage, 0);
				//m.stop();
				finish();
				break;
			case R.id.extbtn:
				gonameurselfpage=new Intent(HowDoYouPlay.this,MainActivity.class);
				sound_pool.play(2,1,1,1,0,1f);
				startActivity(gonameurselfpage);
				//m.stop();
				finish();
				break;
			
			}
			
		}
	};

	
	
	
	
}


