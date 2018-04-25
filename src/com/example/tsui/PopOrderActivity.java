package com.example.tsui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.tsui.negotiate.DemoApplication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


class OfferHelpTask extends AsyncTask<String, Void, String> {
	private Exception exception;
//	public String giveResult;
	private PopOrderActivity cont;
	public String pref_username;
	protected String doInBackground(String... para){
		String username = para[0];
		String order_id = para[1];
		String url_str = para[2];
		pref_username = username;
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
			jsonPara.put("order_id", DemoApplication.current_order_id);
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
			return result;
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
	
	public void setContext(PopOrderActivity ccc){
		cont = ccc;
	}

	protected void onPostExecute(String result) {
		cont.finish();
	}
}
public class PopOrderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_order);
		
		TextView helper_name = (TextView)findViewById(R.id.username1);
		TextView helped_name = (TextView)findViewById(R.id.username2);
		TextView type_view = (TextView)findViewById(R.id.type);
		TextView event_view = (TextView)findViewById(R.id.case_content);
		TextView money_view = (TextView)findViewById(R.id.case_money);
		
		helper_name.setText(DemoApplication.me.name);
		helped_name.setText(DemoApplication.currentDeal.helped.toString());
		type_view.setText(DemoApplication.currentDeal.type.toString());
		event_view.setText(DemoApplication.currentDeal.description.toString());
		money_view.setText(DemoApplication.currentDeal.money.toString());
		
		Button sure = (Button)findViewById(R.id.sure);
		Button cancel = (Button)findViewById(R.id.cancel);
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String Url_str = DemoApplication.url_base+"offer_help";
				OfferHelpTask offerHelpTask = new OfferHelpTask();
				offerHelpTask.setContext(PopOrderActivity.this);
				offerHelpTask.execute(DemoApplication.me.name, DemoApplication.current_order_id, Url_str);
				
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pop_order, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
