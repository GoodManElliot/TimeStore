package com.example.tsui.baidumap;

import android.util.Log;

public class Message extends Thread{
	MapActivity m;
    public Message(MapActivity m){
	    this.m=m;
    }
    public void run(){
    	if(m.isconnected){
    		Log.i("mes","start");
    		m.isconnected=false;
    	}
    }
}
