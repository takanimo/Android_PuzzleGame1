package com.example.finalproj;

import java.lang.ref.WeakReference;
import java.nio.charset.Charset;

import android.os.Handler;
import android.os.Message;

public class TryHandler {
    
	public theHandler mhand;
	private Setup ss;
	boolean isClient = false;
	public TryHandler(Setup set,int who)
	{
		ss = set;
		//if (who == 0)
			//isClient = true;
		//else
			//isClient = false;
		//set.runOnUiThread(new Runnable() {
			
			//@Override
			//public void run() {
			//mhand = new theHandler(ss);
			//if (isClient)
		    //ss.finishClient();
			//else
			//ss.finishServer();
		//	}
		//});
		
	}
	public static class theHandler extends Handler 
	{
		//just reference, we get the activity later
	    public final WeakReference<Setup> mActivity;
	    private GameLogic logic;
	    public theHandler(Setup activity) 
	    {
	        mActivity = new WeakReference<Setup>(activity);
	        
	    }
	    @Override
	    public void handleMessage(Message msg)
	    {
	    	 Setup activity = mActivity.get();
	    	byte[] buffer = (byte[]) msg.obj;
	    	String mess = "";
	    	if (msg.arg2 == 0)
	    	{
	    		
	    		activity.changeOne(new String(buffer,Charset.forName("utf-8"))); 
	    	}
			String dd  = new String(buffer,Charset.forName("utf-8")); //buffer.toString();
		    activity.changeOne(dd);
			//logic.what(33);
	    }
	}
}
