package com.example.finalproj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Setup extends Activity{
/////////
TextView timer;
TableLayout fuller,randomer,blaker;
int puzzleImageId=R.drawable.hockey_puck;
int splitby=3;
String name="v";
int pieceWidth,timeSpend;
private Map<Integer, Bitmap> bitmap = new HashMap<Integer, Bitmap>();
private ImageView selectedPiece=null;
MediaPlayer mpSong;
SoundPool soundPool;
SQLiteDatabase sldb;
Animation fadein, fadeout;
long startTime;
Handler timerHandler=new Handler();
private Runnable timerRunnable = new Runnable() {

    @Override
    public void run() {
        long millis = System.currentTimeMillis() - startTime;
        timeSpend = (int) (millis / 1000);
        timer.setText("Time: " + timeSpend + " seconds");
        timerHandler.postDelayed(this, 500);
    }
};
@Override
public void onPause() {
    super.onPause();
    timerHandler.removeCallbacks(timerRunnable);
}
/////////	
Button client,server, discoverBtn,discoverMeBtn;
ListView list;
TextView players;
int toothReqCode = 100;
BluetoothAdapter bluetoothAdapter;
BluetoothSocket socket;
ArrayList<Devices> dlist;
ArrayList <String>justNames;
ArrayAdapter<String>adapter;
//Scene s_one,s_two;
ViewGroup container;
//Transition customTransition;
Thread people;
Thread startServer;
Set<BluetoothDevice> pairedDevices;

 UUID _UUID_0;//  = UUID.fromString("a062deec-8797-4648-8838-8c7b7898e468");//https://www.uuidgenerator.net/
 UUID _UUID_1;// = UUID.fromString("a062deec-b2a6-4387-8102-f9edda565d5a");
 UUID _UUID_2;// = UUID.fromString("a062deec-25be-4d21-a859-c6878d080891");
 UUID _UUID_3;// = UUID.fromString("a062deec-e796-4114-9e82-fa8ae8e75ac0");
UUID[] allUUIDS =  {_UUID_0,_UUID_1,_UUID_2,_UUID_3};
int selectedServer = -1;
boolean connectedToServer= false;
Thread connectToServer;
IamClient forClient;
IamServer forServer0;
IamServer forServer1;
IamServer forServer2;
InnerMyHandler mHandler;
//filters for discovery start finish and found 
IntentFilter discoStartFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
IntentFilter discoEndFilter =  new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//Fragment b;
View a, b;
int amtOfConnectedClients=0;
ImageView img_main,img_left,img_center,img_right;
Intent gotoGame;

//
//private Bitmap blankBitmap;
//private ImageView selectedPiece;
//Animation fadein, fadeout;
//Map<Integer, Bitmap> bitmap = new HashMap<Integer, Bitmap>();
//FragmentManager frag;
//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.server_client);
		setContentView(R.layout.game_main);
		//frag = getFragmentManager();
		a = findViewById(R.id.fg_a);
		//a.setVisibility(View.VISIBLE);
		//b = findViewById(R.id.fg_b);//(FragMent)findViewById(R.id.fg_b);
		//b.setMenuVisibility(true);
		b=findViewById(R.id.fg_b);
		b.setVisibility(View.GONE);
	timer=(TextView)b.findViewById(R.id.timer);
	Intent intent = getIntent();
	if (intent != null) {
		name = intent.getStringExtra("Name");
	}
	fuller=(TableLayout)b.findViewById(R.id.fulllayout);
	randomer=(TableLayout)b.findViewById(R.id.randomlayout);
	blaker=(TableLayout)b.findViewById(R.id.blanklayout);
		//container = (ViewGroup)findViewById(R.id.middleLayout);
		//s_one = Scene.getSceneForLayout(container,R.layout.sc1, this);
		//s_two = Scene.getSceneForLayout(container,R.layout.sc2, this);
		//s_one.enter();
		//customTransition = TransitionInflater.from(this).inflateTransition(R.transition.trans_set);
		players = (TextView)a.findViewById(R.id.textviewplayers);
		client = (Button)a.findViewById(R.id.buttonClient);
		server = (Button)a.findViewById(R.id.buttonServer);
		discoverBtn = (Button)a.findViewById(R.id.buttonDiscover);
		discoverMeBtn = (Button)a.findViewById(R.id.btn_discoverme);
		list =(ListView)a.findViewById(R.id.listView1);
		
		/*img_main = (ImageView)b.findViewById(R.id.imageViewMain);
		img_left = (ImageView)b.findViewById(R.id.imageViewLeft);
		img_center = (ImageView)b.findViewById(R.id.imageViewCenter);
		img_right = (ImageView)b.findViewById(R.id.imageViewRight);
		*/
		
		client.setOnClickListener(listenToMe);
		server.setOnClickListener(listenToMe);
		discoverBtn.setOnClickListener(listenToMe);
		discoverMeBtn.setOnClickListener(listenToMe);
		
		justNames = new ArrayList<String>();
		dlist = new ArrayList<Devices>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1,justNames);
		list.setAdapter(adapter);
		list.setOnItemClickListener(itemListener);
		
		mHandler = new InnerMyHandler(this);
		turnOnDaBlu(); //turn it on
		
		
	}
	
	private void turnOnDaBlu()
	{
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null)
		{	//
			Toast.makeText(this, "No bluetooth here...", Toast.LENGTH_SHORT).show();
			return;
		}
		//check if it is enabled
		if (!bluetoothAdapter.isEnabled()) {
			 //just ask to be on
			
			IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		    registerReceiver(rr, filter);
		    
		     Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
		     startActivityForResult(enableBtIntent, toothReqCode);
		     
		     
		     
			 // asks to be on AND discoverable
			  //  Intent discoverBt = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			   // startActivityForResult(discoverBt, toothDicoverCode);
		    
		}
		else 
		{
			//list it
			setList();
		}
	}
	
	final BroadcastReceiver rr = new BroadcastReceiver() {
					
					@Override
					public void onReceive(Context context, Intent intent) {
						String action = intent.getAction();
						
				   if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) 
				   {
				            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
				                                               BluetoothAdapter.ERROR);
				   if (state == BluetoothAdapter.STATE_TURNING_ON)
				   {
					   Toast.makeText(Setup.this, "Turning blu on...", Toast.LENGTH_SHORT).show();
				   }
				   if (state == BluetoothAdapter.STATE_ON)
					{
					   Toast.makeText(Setup.this, "Is on", Toast.LENGTH_SHORT).show();
						setList();
						 unregisterReceiver(rr);
					}
				   }
						
					else if ( action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED))
						{
						Toast.makeText(Setup.this, "discovery started", Toast.LENGTH_SHORT).show();
						}
					else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
						{
						Toast.makeText(Setup.this, "discovery finished", Toast.LENGTH_SHORT).show();
						//keepgoing = false;
						}
					else if (BluetoothDevice.ACTION_FOUND.equals(action)) 
				 	{
					 // Get the BluetoothDevice object from the Intent
					 BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					 // Add the name and address to an array adapter to show in a ListView
					 if (dlist.isEmpty()){
						 dlist.add(new Devices(device));
						 updateList();
					 }
					 else
					 	{
						 boolean isNotThere = false;//true;
						 if (device.getName()!=null)// Log.e("newname",device.getName());
						 {
							 isNotThere = true; 
							 for (Devices ff :dlist)
							 {
								 if (device.getName()!=null)
								 {
									 if (ff.name.equals(device.getName()))
										 isNotThere = false;
								 }
							 }
						 }
						 if (isNotThere){
							 dlist.add(new Devices(device));
							 updateList();
						 }
					 	}
				 	}
					}
				};
				
	private void setList()
	{
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		if (pairedDevices.size()!=0)
		{
			// Loop through paired devices
    		for (BluetoothDevice device : pairedDevices)
    		{
    	        // Add the name and address to an array adapter to show in a ListView
    			Log.e("naaaame",device.getName());
    	        dlist.add(new Devices(device));
    		}
    		updateList();
		}
	}
	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			 String main = (String)parent.getItemAtPosition(position); 
			 Toast.makeText(Setup.this, "you clicked on: "+ main, Toast.LENGTH_SHORT).show();
			 selectedServer = position;
		}
	};
	private OnClickListener listenToMe = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
			case R.id.buttonClient:
				//no more discovery
				bluetoothAdapter.cancelDiscovery();
				connectToServer();
				
				break;
			
			case R.id.buttonServer:
				//no more discovery
				bluetoothAdapter.cancelDiscovery();
				startServer = new Thread(new Runnable() {
					
					@Override
					public void run() {
					mHandler.IamServer();//<--server = true
					finishServer();
					}
				});
				startServer.start();
				
				break;
				
			case R.id.buttonDiscover:
				//IntentFilter discoFoundFilter =  new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
				registerReceiver(rr, discoStartFilter);
				registerReceiver(rr, discoEndFilter);
				registerReceiver(rr, filter);
				bluetoothAdapter.startDiscovery();
				break;
				
			case R.id.btn_discoverme:
				Intent discoverBt = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverBt.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
			    startActivityForResult(discoverBt, 888);
				break;
			}
			
			
		}
	};
	void connectToServer()
	{
		connectToServer = new Thread(new Runnable() {
		int trythisOne=0;
			@Override
			public void run() {
			//while(!connectedToServer)
			//{
				try
				{//create a socket to the device that you want
				String myAdd = bluetoothAdapter.getAddress();
				UUID mines = UUID.fromString("e0917680-d427-11e4-8830-"+myAdd.replace(":", ""));
				socket = dlist.get(selectedServer).getDevice().createRfcommSocketToServiceRecord(mines);//allUUIDS[trythisOne]);
				socket.connect();//is a block call , better to do it in a thread
				connectedToServer = true;
				//mHandler = new InnerMyHandler(Setup.this);
				mHandler.IamClient();
				finishClient();
				} 
				catch (IOException e) 
				{
				//e.printStackTrace();
				trythisOne++;
				trythisOne = trythisOne%4;
				}	
			//}	
				
			}
		});
		connectToServer.start();
		
	}
	public void finishClient()
	{
		
		forClient = new IamClient(socket,mHandler);
		forClient.run();
	}
	public void finishServer()
	{
		Thread tt00 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				forServer0 = new IamServer(2000);
				forServer0.adapterAndHandler(bluetoothAdapter,mHandler,dlist.get(0).idd);
				
			}
		});
		Thread tt01 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				forServer1 = new IamServer(3000);
				forServer1.adapterAndHandler(bluetoothAdapter,mHandler,dlist.get(1).idd);
				
			}
		});
		tt00.start();
		tt01.start();
		//forServer2 = new IamServer(2000);
	    //forServer2.adapterAndHandler(bluetoothAdapter,mHandler,_UUID_2);
	}
	private static class InnerMyHandler extends Handler 
	{
		//just reference, we get the activity later
	    private final WeakReference<Setup> mActivity;
	    private GameLogic logic;
	    private boolean isServer= false;
	    public InnerMyHandler(Setup activity) 
	    {
	        mActivity = new WeakReference<Setup>(activity);
	        
	    }
	    
	    @Override
	    public void handleMessage(Message msg)
	    {
	    	Setup activity = mActivity.get();
	    	byte[] buffer = (byte[]) msg.obj;
	    	if (msg.arg2 == 0)
	    	{
	    		String dd  = new String(buffer,Charset.forName("utf-8")); //buffer.toString();
				activity.changeOne(dd);
	    	}
	    	else if (msg.arg2 == -1)
	    		activity.sendClientName();
	    	else if (msg.arg2 == 1)
	    	{
	    	ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
	    	DataInputStream is = new DataInputStream(bis);
	    	List<String> kk = new ArrayList<String>();
	    		try 
	    		{
				kk.add(is.readUTF());
				kk.add(is.readUTF());
				if (kk.get(0).equals("name"))
				activity.seeMsg(kk.get(1));
				else if (kk.get(0).equals("start"))
					{
						activity.showGame();
					}
				} 
	    		catch (IOException e) 
	    		{
				// TODO Auto-generated catch block
				e.printStackTrace();
	    		}
	    	}
	    	/*ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
	    	ObjectInput in = null;
	    	 GameLogic o = null;
	    	  try {
				in = new ObjectInputStream(bis);
				 o =(GameLogic) in.readObject();
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	  
	    	activity.changeOne(o.getName());
	    	*/
	    	
			//String dd  = new String(buffer,Charset.forName("utf-8")); //buffer.toString();
			//activity.changeOne(dd);
			//if (msg.arg1 == 1 && msg.arg2 == 2 )//send name of client to server
			//	activity.sendClientName();
				
			//logic.what(33);
	    }
	   
	    public void IamServer()
	    {
		isServer = true;
	    }
	    public void IamClient()
	    {
	    isServer = false;
	    }
	}
	public void sendClientName()
	{
		String name = "Hello from "+bluetoothAdapter.getName();
		List<String> kk = new ArrayList<String>();
		kk.add("name");
		kk.add(name);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		for (String element : kk) {
			try {
				out.writeUTF(element);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		
			byte[] bytes = baos.toByteArray();
			forClient.write(bytes);
			//GameLogic logic = new GameLogic(name);
			//ByteArrayOutputStream bos = new ByteArrayOutputStream();
			//ObjectOutput outie = null;
			//try {
			// outie = new ObjectOutputStream(bos);   
			//outie.writeObject(logic);
			//byte[] yourBytes = bos.toByteArray();
			// forClient.write(yourBytes);
			// outie.close();
			// bos.close();
			
			
	}
	public byte[] createBytes(String one, String two)
	{
		List<String> kk = new ArrayList<String>();
		kk.add(one);
		kk.add(two);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		for (String element : kk) {
			try {
				out.writeUTF(element);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		
			byte[] bytes = baos.toByteArray();
			return bytes;
	}
	public void startGame()//tells the client to start 
	{
		byte [] xx = createBytes("start", "start");
		//forServer0.talkToClient(xx);
		forServer1.talkToClient(xx);
		showGameServer();//for server
	}
	/*public void showGameViews()
	{
		int puzzleImageId = R.drawable.hockey_puck; 
		 int blankImageId = R.drawable.ic_launcher;
		 
		
		int splitby = 3;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getRealMetrics(dm);
		dm.widthPixels = 1080;
		Bitmap original = BitmapFactory.decodeResource(getResources(), puzzleImageId);
		Bitmap fullBitmap = Bitmap.createScaledBitmap(original, dm.widthPixels/3, dm.widthPixels/3, true);
		int pieceWidth = fullBitmap.getWidth()/splitby;
		
		Bitmap blank = BitmapFactory.decodeResource(getResources(), blankImageId);
		Bitmap blankBitmap = Bitmap.createScaledBitmap(blank, pieceWidth, pieceWidth, true);
		
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
		 
		   MediaPlayer mpSong = new MediaPlayer();
			Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_music);
			try {
				mpSong.setDataSource(this, uri);
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
			mpSong.prepareAsync();
			Button n = (Button)findViewById(R.id.gogo1);
			n.setText("hello");
			TableLayout table = (TableLayout)b.getRootView().findViewById(R.id.fulllayout);//findViewById(R.id.fulllayout);
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
			
			TableLayout table1 = (TableLayout)b.getRootView().findViewById(R.id.randomlayout);
			table1.removeAllViews();
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
	private boolean isBlank(ImageView v) {
		return blankBitmap.equals(((BitmapDrawable)(v.getDrawable())).getBitmap());
	}
	private Bitmap getBitmap(int i) {
		return bitmap.get(i);
	}
	private void replaceBlank(View v) {
		selectedPiece.startAnimation(fadeout);
		v.startAnimation(fadein);
		Bitmap bm = ((BitmapDrawable)(selectedPiece.getDrawable())).getBitmap();
		((ImageView)v).setImageBitmap(bm);
		selectedPiece.setImageBitmap(blankBitmap);
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
	}*/
	public void showGame()
	{
		//a.setMenuVisibility(View.GONE);
		
		//b.setMenuVisibility(View.VISIBLE);
		a.setVisibility(View.GONE);
		b.setVisibility(View.VISIBLE);
		//showGameViews();
		//revealCards();
		puzzleGame();
		initSound();
		initDatabase();
		startPuzzleGame();
	}
	public void puzzleGame(){
		//timer.setText("aaaaaaaaaaaaaa");
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getRealMetrics(dm);
		Bitmap original =BitmapFactory.decodeResource(getResources(), puzzleImageId);
		Bitmap fullBitmap = Bitmap.createScaledBitmap(original, dm.widthPixels, dm.widthPixels, true);
		pieceWidth = fullBitmap.getWidth()/splitby;
		
		for (int i =1; i<=splitby*splitby; i++) {
			int tw = fullBitmap.getWidth()/splitby;
			int th = fullBitmap.getHeight()/splitby;
			Bitmap piece = Bitmap.createBitmap(fullBitmap, tw*((i-1)%splitby), ((i-1)/splitby)*th, tw, th);
			bitmap.put(i, piece);
		}
	}
	private void initSound() {
		try {
			//mpSong = new MediaPlayer();
			//Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_music);
			//mpSong.setDataSource(this, uri);
			//mpSong.prepareAsync();
			
			//soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC,0);
			//Map<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
			//soundMap.put(1, soundPool.load(this, R.raw.btn_sound,1));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initDatabase() {
		SQL db = new SQL(this);
		sldb = db.getReadableDatabase();
		/*sldb.execSQL("DROP TABLE IF EXISTS 'record';");
		sldb.execSQL("CREATE TABLE record ("
				+ " _id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "name TEXT NOT NULL, "
				+ "split_by INTEGER NOT NULL, "
			    + "time_spend INTEGER NOT NULL, "
			    + "self TEXT NOT NULL)"
			      );*/
	}
	private void startPuzzleGame(){
		//MusicManager.start(this, MusicManager.MUSIC_GAME,true);
		buildRandomLayout();
		startTime = System.currentTimeMillis();
		timerHandler.postDelayed(timerRunnable, 0);
	}
	private void buildRandomLayout(){
		TableLayout table = (TableLayout)findViewById(R.id.randomlayout);
		table.removeAllViews();
		View.OnClickListener cl = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (selectedPiece != null) {
					switchPiece((ImageView)v);
				} else {
					selectedPiece = (ImageView)v;
					v.setBackgroundResource(R.drawable.rowborder);
					
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
			randomer.addView(row);
		}
	}private void switchPiece(ImageView v) {
		selectedPiece.startAnimation(fadeout);
		v.startAnimation(fadein);
		Bitmap vbm = ((BitmapDrawable)v.getDrawable()).getBitmap();
		Bitmap bm = ((BitmapDrawable)(selectedPiece.getDrawable())).getBitmap();
		((ImageView)v).setImageBitmap(bm);
		
		selectedPiece.setImageBitmap(vbm);
		selectedPiece.setBackgroundResource(0);
		selectedPiece = null;
//		soundPool.play(1, 1, 1, 1, 0, 1f);
		if (checkWin()) {
			timerHandler.removeCallbacks(timerRunnable);
			//MusicManager.start(this, MusicManager.MUSIC_GAME,false);
			//MusicManager.release(MusicManager.MUSIC_GAME);
			showCongratulationDlg();
			insertRecord();
			
			
			//mpSong.start();
		}
	}
	private void insertRecord() {
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("split_by", splitby);
		values.put("time_spend", timeSpend);
		values.put("self", "Y");
		long id = sldb.insert("record", null, values);
	}
	private void showRecords() {
		Intent intent = new Intent(Setup.this, ShowGameRecordActivity.class);
		startActivity(intent);
		finish();
	}
	private void showCongratulationDlg() {
		AlertDialog dlg = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Puzzle Game");
		final TextView view = new TextView(this);
		String t = timeSpend < getMinTimeSpend() ? "Congratulation, You beat the record!!!" : "Congratulation!!!";
		view.setText(t+"\n Time: " + timeSpend + " seconds\nReplay?");
		builder.setView(view);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					startGame();
				//	mpSong.pause();
				//	mpSong.seekTo(0);
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
		Cursor recordset = sldb.rawQuery("select min(time_spend) from record where split_by = " + splitby, null);
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
		TableLayout table = (TableLayout)findViewById(R.id.randomlayout);
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
	
	public void showGameServer()
	{
		//a.setMenuVisibility(View.GONE);
		//b.setMenuVisibility(View.VISIBLE);
		a.setVisibility(View.GONE);
		b.setVisibility(View.VISIBLE);
		//revealServerCards();
	}
	public void changeOne(String x)
	{
		Toast.makeText(Setup.this, "weak ref: "+x, Toast.LENGTH_SHORT).show();
	}
	public void seeMsg(String x)
	{
		Toast.makeText(Setup.this, "msg: "+x, Toast.LENGTH_SHORT).show();
		amtOfConnectedClients++;
		if (amtOfConnectedClients == 1)
		{
			Log.e("start","game start");
			startGame();
		}
	}
	void changeScene()
	{
	//	TransitionManager.go(s_two,customTransition);
	}
	void updateList()
	{
		 adapter.clear();//.getData().clear();
		 for (Devices devs: dlist)
		 adapter.addAll(devs.name);//.getData().addAll(objects);
		    // fire the event
		  adapter.notifyDataSetChanged();
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
				Intent intent = new Intent(Setup.this, MainActivity.class);
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
