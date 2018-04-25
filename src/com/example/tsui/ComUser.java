package com.example.tsui;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.example.tsui.negotiate.Constant;

public class ComUser {
    public String name;
    public String pwd;
    public double lot;
    public double lat;
    public List<Deal> deallist;
    public BitmapDescriptor pic;
    public int state;
    public boolean inbusiness;
    public String gender="male";
    public String sentence;
    public String help;
    public String helped;
    public String rating;
    public String money;
    
    
    public ComUser(String name, String pwd){
    	this.name=name;
    	this.pwd=pwd;
    	this.deallist=new ArrayList<Deal>();
    	this.state=Constant.NOSTATE;
    	this.inbusiness=false;
    	pic=getDefaultPic(this.gender);
    }
    
    public ComUser(String name){
    	this(name,"");
    }
    
    public void updatePosition(double lot,double lat){
    	this.lot=lot;
    	this.lat=lat;
    }
    
    public void setPic(BitmapDescriptor pic){
    	this.pic=pic;
    }
    
    public BitmapDescriptor getDefaultPic(String sex){
    	if(sex.equals("female"))
		return BitmapDescriptorFactory
				.fromResource(R.drawable.head_women);
    	else 
    		return BitmapDescriptorFactory
    				.fromResource(R.drawable.head_women);
	}
    
    public void addDeal(Deal dl){
    	this.deallist.add(dl);
    }
    
    public void setState(int state){
    	this.state=state;
    }
}