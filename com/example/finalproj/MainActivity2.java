package com.example.finalproj;

import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity2 {
	//PERMISSIONS NEEDED
		//<uses-permission android:name="android.permission.BLUETOOTH" />
		//<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
		private BluetoothServerSocket mmServerSocket;
		Thread discoverCountdown;
		boolean keepgoing = false;
		BluetoothAdapter bluetoothAdapter;
		BluetoothSocket socket;
		boolean canConnect = false;
		int toothReqCode = 100;
		int toothDiscoverCode = 60;
		final UUID _UUID = UUID.fromString("a062deec-8797-4648-8838-8c7b7898e468");//https://www.uuidgenerator.net/
		ArrayList<Devices> dlist;
		Button discoverBtn, addDevicesBtn,connectBtn,discoverOthersBtn,serverClientBtn,sendServerBtn;
		TextView one,fromClient,connectedTxt;
		ListView theList;
		ArrayList <String>justNames;
		ArrayAdapter<String>adapter;
		boolean isServer = true;
		//private ClientConnectThread forClient;
		// duration that the device is discoverable
		//private static final int DISCOVER_DURATION = 300;
	/*
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//setContentView(R.layout.activity_blue_teeth);
			setContentView(R.layout.server_client);
			dlist = new ArrayList<Devices>();
			
			
			
			IamServer = new ServerThread();
			theList = (ListView)findViewById(R.id.listView1);
			justNames = new ArrayList<String>();
			 // Define a new Adapter
	        // First parameter - Context
	        // Second parameter - Layout for the row
	        // Third parameter - ID of the TextView to which the data is written
	        // Forth - the Array of data
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1,justNames);
			theList.setAdapter(adapter);
			
			
		}
		OnClickListener listener = new OnClickListener() {
			
		@Override
		public void onClick(View v) 
		{
			switch(v.getId())
			{
		
			  case R.id.connectBtn:
				if (canConnect)
				{
				  if (!isServer)//is a client
				  {
					bluetoothAdapter.cancelDiscovery();//no more discovery
					Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
					// If there are paired devices
	        		//if (pairedDevices.size() > 0) 
	        		//{
	        			// Loop through paired devices
	        		//	for (BluetoothDevice device : pairedDevices)
	        		//	{
	        	        // Add the name and address to an array adapter to show in a ListView
	        		//		Log.e("naaaame",device.getName());
	        	    //    dlist.add(new Devices(device));
	        		//	}
	        		//}
	        		//else
	        		//{
	        			
	        		//}
	        		try {//create a socket to the device that you want
						socket = dlist.get(0).getDevice().createRfcommSocketToServiceRecord(_UUID);
						socket.connect();//is a block call , better to do it in a thread
						BlueTeeth.this.runOnUiThread(new Runnable() {
							public void run() {
								connectedTxt.setText("connected as client");
								
							}
							
						});
						runThread();
						
					} catch (IOException e) 
	        			{
						e.printStackTrace();
	        			}	
				  }
				  else//it is a server
				  {
					  Thread tt = new Thread(new Runnable() {
						
						@Override
						public void run() {
							BlueTeeth.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									connectedTxt.setText("connected as server");
								}
							});
							
							 IamServer.adapterAndHandler(bluetoothAdapter,mHandler);
						}
					});
					 tt.start();
				  }
				}
				break;
			  
			  case R.id.servBtn:
				if (isServer)
				{
					isServer = false;
					serverClientBtn.setText("isClient");
				}
				else
				{
					isServer = true;
					serverClientBtn.setText("isServer");
				}
				break;
				
			  case R.id.sendserverBtn:
				  if (!isServer)//is a client
				  {
				  String hello = "hello from me";
				  byte [] bytess = hello.getBytes();
				  forClient.write(bytess);
				  }
				  else //im server
				  {
					  String helloo = "Hello from server";
					  byte[] bytes = helloo.getBytes();
					  IamServer.callRun(bytes);
				  }
					  
				  break;
			 }
		}
		};
		public void runThread()
		{
			Thread thr = new Thread(new Runnable() {
				
				@Override
				public void run() {
					forClient = new ClientConnectThread(socket);
					forClient.run();
				}
			}); 
			thr.start();
		}
		
		
		public void changeOne(String xx)
		{
			fromClient.setText("Message received "+ xx);
		}
		void updateList()
		{
			 adapter.clear();//.getData().clear();
			 for (Devices devs: dlist)
			 adapter.addAll(devs.name);//.getData().addAll(objects);
			    // fire the event
			  adapter.notifyDataSetChanged();
		}
		//for client
		private class ClientConnectThread extends Thread {
		    private final BluetoothSocket mmSocket;
		    private final InputStream mmInStream;
		    private final OutputStream mmOutStream;
		 
		    public ClientConnectThread(BluetoothSocket socket) {
		        mmSocket = socket;
		        InputStream tmpIn = null;
		        OutputStream tmpOut = null;
		 
		        // Get the input and output streams, using temp objects because
		        // member streams are final
		        try {
		            tmpIn = socket.getInputStream();
		            tmpOut = socket.getOutputStream();
		        } catch (IOException e) { }
		 
		        mmInStream = tmpIn;
		        mmOutStream = tmpOut;
		    }
		 
		    public void run() {
		        byte[] buffer = new byte[1024];  // buffer store for the stream
		        int bytes; // bytes returned from read()
		 
		        // Keep listening to the InputStream until an exception occurs
		        while (true) {
		            try {
		                // Read from the InputStream
		                bytes = mmInStream.read(buffer);
		                // Send the obtained bytes to the UI activity
		                mHandler.obtainMessage(9999, bytes, -1, buffer).sendToTarget();
		            } catch (IOException e) {
		                break;
		            }
		        }
		    }
		 
		    // Call this from the main activity to send data to the remote device 
		    public void write(byte[] bytes) {
		        try {
		            mmOutStream.write(bytes);
		           
		            //now call run to send it??
		            
		        } catch (IOException e) {
		        	Log.e("err",e.toString());
		        }
		    }
		 
		    // Call this from the main activity to shutdown the connection 
		    public void cancel() {
		        try {
		            mmSocket.close();
		        } catch (IOException e) { }
		    }
		}

		
		//
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    // Check which request we're responding to
			if (requestCode == toothReqCode) {
		        // Make sure the request was successful
		        if (resultCode == RESULT_OK) {
		        	canConnect = true;
		        }
		    }
			else if (requestCode == toothDiscoverCode)
		    {
		    	if (resultCode == toothDiscoverCode)
		    	{
		    		canConnect = true;
		    		keepgoing = true;
		    		discoverCountdown = new Thread(new timmeee());
					discoverCountdown.start();
		    	}
		    }
		    else if (resultCode == RESULT_CANCELED)
		    {
		    	
		    		   Toast.makeText(this,"user cancelled",Toast.LENGTH_SHORT).show();

		    }
		}
		class timmeee implements Runnable{
		int time = toothDiscoverCode;
		public void timeee(){
			
		}
		@Override
		public void run() {
			while(keepgoing)
			{
				try {
					BlueTeeth.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							one.setText("Discover time left: "+ time);
						}
					});
				//
				time--;
				if (time==0){
					keepgoing = false;
						BlueTeeth.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							one.setText("discover ended");
						}
					});
				}
				Thread.sleep(1000);
				discoverCountdown = null;
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			BlueTeeth.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					one.setText("discover ended");
				}
			});
		}
			
		}
}*/
}
