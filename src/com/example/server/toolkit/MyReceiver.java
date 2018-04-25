package com.example.server.toolkit;

import java.util.List;

import org.json.JSONObject;

import com.example.tsui.ComUser;
import com.example.tsui.Deal;
import com.example.tsui.OrderActivity;
import com.example.tsui.PopOrderActivity;
import com.example.tsui.R;
import com.example.tsui.negotiate.Constant;
import com.example.tsui.negotiate.DemoApplication;
import com.example.tsui.negotiate.loginback;

import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver {

	public DemoApplication da;

	public MyReceiver(DemoApplication da) {
		this.da = da;
	}
	
	public MyReceiver() {
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("action", intent.getAction());
		Log.d("ONRECIEVE", "INIT");
		Log.i("hi1","hi1");
		if (intent.getAction()==JPushInterface.ACTION_MESSAGE_RECEIVED){
			
			Bundle bd=intent.getExtras();
			//String type="a";//bd.getString("type");
			String extras = bd.getString(JPushInterface.EXTRA_EXTRA);
			Log.i("hi","hi");
			try {
				Log.i("hi2","hi2");
				JSONObject extrasJson = new JSONObject(extras);
				String type = extrasJson.getString("head");
				Log.i("type",type);
				if (type.equals("geo")) {
					Log.i("g","I am in geo");
					if(da.contractor!=null){ 
						Log.i("g","I am in con");
						Log.i("g",extrasJson.getString("username"));
						if (extrasJson.getString("username").equals(DemoApplication.me.name.toString())) {
							Log.i("g","I am right");
							da.contractor.updatePosition(Double.parseDouble(extrasJson.getString("lot").toString()), Double.parseDouble(extrasJson.getString("lat").toString()));
						}	
					}
				}
				if (type.equals("request")) {
					if(!extrasJson.getString("helped").toString().equals(da.me.name)){
					//if(da.map!=null){
						DemoApplication.currentDeal = new Deal();
						DemoApplication.current_order_id = extrasJson.getString("id").toString();
						Log.d("me", DemoApplication.current_order_id);
						DemoApplication.currentDeal.type = extrasJson.getString("type").toString();
						Log.d("me", DemoApplication.currentDeal.type);
						DemoApplication.currentDeal.description = extrasJson.getString("event").toString();
						Log.d("me", DemoApplication.currentDeal.description);
						DemoApplication.currentDeal.money = extrasJson.getString("money").toString();
						Log.d("me", DemoApplication.currentDeal.money);
						DemoApplication.currentDeal.finish = extrasJson.getString("tag").toString();
						Log.d("me", DemoApplication.currentDeal.finish);
						DemoApplication.currentDeal.helper = extrasJson.getString("helper").toString();
						Log.i("me", DemoApplication.currentDeal.helper);
						DemoApplication.currentDeal.helped = extrasJson.getString("helped").toString();
						Log.i("me", DemoApplication.currentDeal.helped);
						da.contractor=new ComUser(extrasJson.getString("helped").toString());
						DemoApplication.popWindow.finish();
					
						PopOrderActivity orderActivity = new PopOrderActivity();
						Intent in = new Intent(DemoApplication.map ,orderActivity.getClass());
						DemoApplication.map.startActivity(in);
				
					//}
				}}
				if (type.equals("gethelp")) {
					Log.i(extrasJson.getString("helper").toString(), da.me.name);
					if(!extrasJson.getString("helper").toString().equals(da.me.name)){
						Log.i(extrasJson.getString("helper").toString(), da.me.name);
					DemoApplication.currentDeal = new Deal();
					DemoApplication.current_order_id = extrasJson.getString("id").toString();
					Log.d("me", DemoApplication.current_order_id);
					DemoApplication.currentDeal.type = extrasJson.getString("type").toString();
					Log.d("me", DemoApplication.currentDeal.type);
					DemoApplication.currentDeal.description = extrasJson.getString("event").toString();
					Log.d("me", DemoApplication.currentDeal.description);
					DemoApplication.currentDeal.money = extrasJson.getString("money").toString();
					Log.d("me", DemoApplication.currentDeal.money);
					DemoApplication.currentDeal.finish = extrasJson.getString("tag").toString();
					Log.d("me", DemoApplication.currentDeal.finish);
					DemoApplication.currentDeal.helper = extrasJson.getString("helper").toString();
					Log.d("me", DemoApplication.currentDeal.helper);
					DemoApplication.currentDeal.helped = extrasJson.getString("helped").toString();
					Log.d("me", DemoApplication.currentDeal.helped);
					da.contractor=new ComUser(extrasJson.getString("helper").toString());
					DemoApplication.popWindow.finish();}
				
					/*PopOrderActivity orderActivity = new PopOrderActivity();
					Intent in = new Intent(DemoApplication.map ,orderActivity.getClass());
					DemoApplication.map.startActivity(in);*/
				}
			} catch (Exception e) {
				Log.e("ONRECEIVE", "Unexpected",e);
			}
			
			
/*
			else if(type==Constant.REQUEST){//�㲥������
				if(da.map!=null&&da.me.state==Constant.IDLE){
					//DemoApplication.popWindow.finish();
					
					DemoApplication.orderPopWindow = new OrderActivity();
					Intent in = new Intent(DemoApplication.map ,DemoApplication.orderPopWindow.getClass());
					DemoApplication.map.startActivity(in);
					
					
					da.map.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							
						}
					});
				}
			}
			else if(type==Constant.RESPONSE){//�㲥�Ĵ�
				if(bd.getBoolean("agree")){  //�𸴵�������bool
					if(da.map!=null&&da.me.state==Constant.BUSY){
						da.map.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
							}
						});
						da.me.inbusiness=true;
						da.contractor=new ComUser(bd.getString(Constant.USERNAME));
					}
				}
				else{
					
				}
			}
			else if(type==Constant.PROGRESS){//�����Ƿ���ɣ���ɰ�һ����ť����true,����key��constant��
				if(bd.getBoolean(Constant.COMPLISH)){
					da.me.inbusiness=false;
					da.contractor=null;
					//
					//
				}
			}*/
		}
	}
}
