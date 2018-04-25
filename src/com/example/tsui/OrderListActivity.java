package com.example.tsui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.tsui.negotiate.DemoApplication;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

class OrderListTask extends AsyncTask<String, Void, JSONObject> {
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
		int num = 0;
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
		try {
			String key_type;
			String key_date;
			String key_event;
			String key_money;
			String key_tag;
			String key_helper;
			String key_helped;
			String value_type;
			String value_date;
			String value_event;
			String value_money;
			String value_tag;
			String value_helper;
			String value_helped;
			num = Integer.parseInt(result.getString("length").toString());
			Log.d("NUM",""+num);
			for (int i=0; i<num; i++) {
				key_type = "" + i + " type";
				key_date = "" + i + " date";
				key_event = "" + i + " event";
				key_money = "" + i + " money";
				key_tag = "" + i + " tag";
				key_helper = "" + i + " helper";
				key_helped = "" + i + " helped";
				value_type = result.getString(key_type).toString();
				value_date = result.getString(key_date).toString();
				value_event = result.getString(key_event).toString();
				value_money = result.getString(key_money).toString();
				value_tag = result.getString(key_tag).toString();
				value_helped = result.getString(key_helped.toString());
				value_helper = result.getString(key_helper).toString();
				Deal deal = new Deal();
				deal.type = value_type;
				deal.date = value_date;
				deal.description = value_event;
				deal.money = value_money;
				deal.finish = value_tag;
				deal.helped = value_helped;
				deal.helper = value_helper;
				
				DemoApplication.me.addDeal(deal);
				
				Log.d("NET", value_type);
				Log.d("NET", value_date);
				Log.d("NET", value_event);
				Log.d("NET", value_money);
				HashMap<String, Object> map = new HashMap<String, Object>();  
	            map.put("img", R.drawable.head_women);         
	            map.put("finish", deal.finish);  
	            map.put("date", deal.date);  
	            listItem.add(map);  
			}
		} catch (NumberFormatException e) {
			Log.e("TIME", "Uncaught exception", e);
		} catch (JSONException e) {
			Log.e("TIME", "Uncaught exception", e);
		}
		ListView lv;
		lv = (ListView) ((Activity) cont).findViewById(R.id.list);       
		
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(cont,listItem,R.layout.vlist,new String[] {"img","finish", "date"},new int[] {R.id.img,R.id.finish,R.id.date});

		lv.setAdapter(mSimpleAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						ListView listView = (ListView) arg0;
						Intent intent = new Intent();
						OrderActivity orderInformationActivity = new OrderActivity();
						intent.setClass(cont, orderInformationActivity.getClass());
						Bundle arguments = new Bundle();
						arguments.putSerializable("id", arg2);
						intent.putExtras(arguments);
						cont.startActivity(intent);
						
					}
		        });
		
		
	}
}


public class OrderListActivity extends AppCompatActivity {

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
		TextView mTitileBar = (TextView) findViewById(R.id.titleBar);
		// set action bar
		mTitileBar.setText("Time Store");
		setSupportActionBar(actionbar);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		actionbar.setNavigationIcon(R.drawable.ic_action_back);
		
		
		actionbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startActivity(Intent(this,MainActivity.class));
				finish();
			} 
		});
		
		String Url_str = DemoApplication.url_base+"get_order_list";
		String username = DemoApplication.me.name;
		
		OrderListTask OrderListTask = new OrderListTask();
		OrderListTask.setContext(OrderListActivity.this);
		OrderListTask.execute(username,Url_str);

	}
}
