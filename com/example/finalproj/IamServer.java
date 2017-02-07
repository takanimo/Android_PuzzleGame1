package com.example.finalproj;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class IamServer extends Thread{
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothServerSocket mmServerSocket;
	UUID _UUID_0;
	String NAME = "Blue Connects";
	private Handler mhand;
	TheBlueSocket blueSocket;
	int wait;
	
	public IamServer(int xxx)
	{
		wait = xxx;
	}
    public void adapterAndHandler (BluetoothAdapter blue,Handler mm,UUID clientUUID) {
		mBluetoothAdapter = blue;
		mhand = mm;
		_UUID_0 = clientUUID;
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, _UUID_0);
        } catch (IOException e) { }
        mmServerSocket = tmp;
        this.run();
    }
 
    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) 
        {
            try {	
            	Thread.sleep(wait);
            	Log.e("connecting", "trying to connect to "+_UUID_0);
                socket = mmServerSocket.accept();//<----block call waits until the connection is set
                Log.e("connected", "connected to "+ _UUID_0);
                
                
               // String nn = _UUID_0.toString()+"connected";
               // byte [] bytess = nn.getBytes();
               // mhand.obtainMessage(999, 0, 0,bytess ).sendToTarget();
            } catch (IOException e) {
            	Log.e("sorry", "no connection "+ e.toString());
                break;
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
            	blueSocket = new TheBlueSocket(socket, mhand);
            	blueSocket.run();
              //  manageConnectedSocket(socket);
                try {
					mmServerSocket.close();//<-- close the bluetooth server socket but NOT the bluetooth socket
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                break;
            }
        }
    }
    public void talkToClient(byte[] bytes)
    {
    	blueSocket.write(bytes);
    }
 
    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }


}
