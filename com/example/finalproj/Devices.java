package com.example.finalproj;

import java.util.UUID;

import android.bluetooth.BluetoothDevice;

public class Devices {
	BluetoothDevice device;
	    public String name;
	    public String address;
	    UUID idd;
	    public Devices(BluetoothDevice device){
	    	this.name = device.getName();
	    	this.address = device.getAddress();
	    	this.device = device;
	    	this.idd = UUID.fromString("e0917680-d427-11e4-8830-"+this.address.replace(":", ""));
	    }
	    public Devices(String name, String address) {
	       this.name = name;
	       this.address= address;
	    }
	    public BluetoothDevice getDevice()
	    {
	    	return device;
	    }
	
}
