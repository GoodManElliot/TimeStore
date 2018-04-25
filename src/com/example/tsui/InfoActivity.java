package com.example.tsui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.data.f;


import com.example.server.toolkit.PostClass;
import com.example.server.toolkit.Result;
import com.example.tsui.baidumap.MapActivity;
import com.example.tsui.negotiate.DemoApplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.R.id;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
class UserInfoTask extends AsyncTask<String, Void, JSONObject> {
	private Exception exception;
//	public String giveResult;
	private Context cont;
	protected JSONObject doInBackground(String... para){
		String username = para[0];
		String url_str = para[1];
		Log.d("NET", username);
		Log.d("NET", url_str);
		URL url = null;
		try {
			url = new URL(url_str);
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Content-Type", "application/json");
			//urlConnection.setRequestProperty("Charset", "utf-8");
			
			urlConnection.connect();
			
			DataOutputStream dop = new DataOutputStream(urlConnection.getOutputStream());
			JSONObject jsonPara = new JSONObject();
			jsonPara.put("username", username);
			dop.writeBytes(jsonPara.toString());
			dop.flush();
			dop.close();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String result = "";
			String readLine = null;
			String s = null;
			while ((readLine=bufferedReader.readLine())!=null) {
				result += readLine;
			}
			bufferedReader.close();
			urlConnection.disconnect();
			Log.d("NET", result);
			JSONObject resultJson = new JSONObject(result.toString());
			return resultJson;
		} 
		catch (MalformedURLException e) {
			Log.e("TIME", "Uncaught exception", e);
		}
		catch (IOException e) {
			Log.e("TIME", "Uncaught exception", e);
		}
		catch (JSONException e) {
			Log.e("TIME", "Uncaught exception", e);
		}
		return null;
	}
	
	public void setContext(Context ccc){
		cont = ccc;
	}

	protected void onPostExecute(JSONObject result) {
		try {
			if (result.getString("status").equals("true")) {
				DemoApplication.me.money = result.getString("money").toString();
				DemoApplication.me.gender = result.getString("gender").toString();
				DemoApplication.me.help = result.getString("help").toString();
				DemoApplication.me.helped = result.getString("helped").toString();
			}
		} catch (JSONException e) {
			Log.d("TIME", "Uncaught exception", e);
		}
		
		((InfoActivity) cont).setInfo();
		
		
	}
}

public class InfoActivity extends AppCompatActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
		TextView mTitileBar = (TextView) findViewById(R.id.titleBar);
		// set action bar
		mTitileBar.setText("Time Store");
		setSupportActionBar(actionbar);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		actionbar.setNavigationIcon(R.drawable.ic_action_back);
		String Url_str = DemoApplication.url_base+"user_info";
		String username = DemoApplication.me.name;
		
		UserInfoTask userInfoTask = new UserInfoTask();
		userInfoTask.setContext(InfoActivity.this);
		userInfoTask.execute(username,Url_str);
		
		
		actionbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startActivity(Intent(this,MainActivity.class));
				finish();
			} 
		});
	}
	
	public void setInfo() {
		TextView username = (TextView)findViewById(R.id.username);
		username.setText(DemoApplication.me.name);
		TextView money = (TextView)findViewById(R.id.money);
		money.setText(DemoApplication.me.money);
		TextView help = (TextView)findViewById(R.id.help);
		help.setText(DemoApplication.me.help);
		TextView helped = (TextView)findViewById(R.id.helped);
		helped.setText(DemoApplication.me.helped);
		
	}
}
