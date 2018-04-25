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

import cn.jpush.android.api.d;

import com.example.tsui.negotiate.DemoApplication;
import com.example.tsui.negotiate.registerback;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

class UserRegisterTask extends AsyncTask<String, Void, String> {
	private Exception exception;
	public String giveResult;
	public Context cont;
	protected String doInBackground(String... para){
		String username = para[0];
		String password = para[1];
		String gender = para[2];
		String url_str = para[3];
		Log.d("NET", username);
		Log.d("NET", password);
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
			jsonPara.put("password", password);
			jsonPara.put("gender", gender);
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
	
	public void setContext(Context ccc) {
		cont = ccc;
	}
	
	protected void onPostExecute(String result) {
		Log.i("result", result);
		if(result.equals("register success"))
		{
			Intent in = new Intent(cont, LoginActivity.class);
			cont.startActivity(in);
		}
	}

	
}


public class RegisterActivity extends ActionBarActivity {
	UserRegisterTask userRegisterTask;
	public String username_str;
	public String password_str;
	public String gender_str;
	public registerback rb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		
		
		final EditText username = (EditText)findViewById(R.id.register_username);
		final EditText password = (EditText)findViewById(R.id.register_password);
//		final EditText gender = (EditText)findViewById(R.id.register_gender);
		RadioGroup rg_gender = (RadioGroup)this.findViewById(R.id.rg_gender);
		          //绑定一个匿名监听器
		          rg_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		              
		              @Override
		              public void onCheckedChanged(RadioGroup arg0, int arg1) {
		                  // TODO Auto-generated method stub
		                //获取变更后的选中项的ID
		                  int radioButtonId = arg0.getCheckedRadioButtonId();
		                  //根据ID获取RadioButton的实例
		                  RadioButton rrrr = (RadioButton)RegisterActivity.this.findViewById(radioButtonId);
		                //更新文本内容，以符合选中项
		                  Log.d("性別", rrrr.getText().toString());
		                  gender_str = rrrr.getText().toString();
		              }
		          });
		Button submit = (Button)findViewById(R.id.register_submit);
		rb = new registerback(this);
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userRegisterTask = new UserRegisterTask();
				username_str = username.getText().toString();
				password_str = password.getText().toString();
//				gender_str = rrrr.getText().toString();
				//Toast.makeText(RegisterActivity.this, username_str+" "+password_str, Toast.LENGTH_SHORT).show();
				String Url_str = DemoApplication.url_base+"register";
				userRegisterTask.setContext(RegisterActivity.this);
				if(rb.register())
					userRegisterTask.execute(username_str,password_str,gender_str,Url_str);
				
				
					
			}
		});
		
		Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
		TextView mTitileBar = (TextView) findViewById(R.id.titleBar);
		
		mTitileBar.setText("Register");
		setSupportActionBar(actionbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		actionbar.setNavigationIcon(R.drawable.ic_action_back);
		actionbar.setNavigationOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		*/
		return super.onOptionsItemSelected(item);
	}
}
