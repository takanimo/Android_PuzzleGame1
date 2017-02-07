package com.example.finalproj;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class IamClient extends Thread{
	 private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	    private Handler handler;
	    
	 
	    public IamClient(BluetoothSocket socket,Handler hand) {
	        mmSocket = socket;
	        handler = hand;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	            String hello = "connected to server";
	             byte [] bytess = hello.getBytes();
	            handler.obtainMessage(1,-1,-1,bytess).sendToTarget();
	            
	        } catch (IOException e) { 
	        	 
	        }
	 
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
	                handler.obtainMessage(9999, bytes, 1, buffer).sendToTarget();
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
