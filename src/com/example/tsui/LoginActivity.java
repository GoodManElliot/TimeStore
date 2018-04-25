package com.example.tsui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;

import org.bitlet.weupnp.Main;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.tsui.negotiate.DemoApplication;
import com.example.tsui.negotiate.loginback;

import android.R.menu;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

class UserLoginTask extends AsyncTask<String, Void, String> {
	private Exception exception;
	// public String giveResult;
	private Context cont;
	public String pref_username;

	protected String doInBackground(String... para) {
		String username = para[0];
		String password = para[1];
		String url_str = para[2];
		pref_username = username;
		Log.d("NET", username);
		Log.d("NET", password);
		Log.d("NET", url_str);
		URL url = null;
		try {
			url = new URL(url_str);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Content-Type", "application/json");
			// urlConnection.setRequestProperty("Charset", "utf-8");

			urlConnection.connect();

			DataOutputStream dop = new DataOutputStream(urlConnection.getOutputStream());
			JSONObject jsonPara = new JSONObject();
			jsonPara.put("username", username);
			jsonPara.put("password", password);
			dop.writeBytes(jsonPara.toString());
			dop.flush();
			dop.close();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String result = "";
			String readLine = null;
			String s = null;
			while ((readLine = bufferedReader.readLine()) != null) {
				result += readLine;
			}
			bufferedReader.close();
			urlConnection.disconnect();
			Log.d("NET", result);
			return result;
		} catch (MalformedURLException e) {
			Log.e("TIME", "Uncaught exception", e);
		} catch (IOException e) {
			Log.e("TIME", "Uncaught exception", e);
		} catch (JSONException e) {
			Log.e("TIME", "Uncaught exception", e);
		}
		return null;
	}

	public void setContext(Context ccc) {
		cont = ccc;
	}

	protected void onPostExecute(String result) {
		if (result.equals("login success")) {
			DemoApplication.me = new ComUser(pref_username);
			Intent in = new Intent(cont, MainActivity.class);
			cont.startActivity(in);
		}
		if (result.equals("wrong user")) {
			Toast.makeText(cont, "User not exist", Toast.LENGTH_SHORT).show();
		}
		if (result.equals("wrong password")) {
			Toast.makeText(cont, "Wrong password", Toast.LENGTH_SHORT).show();
		}
	}
}

public class LoginActivity extends ActionBarActivity {
	UserLoginTask userLoginTask;
	public String username_str;
	public String password_str;
	public loginback lb;
	public String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
		TextView mTitileBar = (TextView) findViewById(R.id.titleBar);

		lb = new loginback(this);
		Button register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(in);
			}
		});

		mTitileBar.setText("TimeStore");
		setSupportActionBar(actionbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		/*
		 * actionbar.setNavigationIcon(R.drawable.ic_action_back);
		 * actionbar.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { finish();
		 * 
		 * } });
		 */

		final EditText username = (EditText) findViewById(R.id.login_username);
		final EditText password = (EditText) findViewById(R.id.login_password);
		Button submit = (Button) findViewById(R.id.login_submit);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userLoginTask = new UserLoginTask();
				username_str = username.getText().toString();
				password_str = password.getText().toString();
				// Toast.makeText(RegisterActivity.this, username_str+"
				// "+password_str, Toast.LENGTH_SHORT).show();
				String Url_str = DemoApplication.url_base + "login";
				username_str = username.getText().toString();
				password_str = password.getText().toString();
				// Toast.makeText(RegisterActivity.this, username_str+"
				// "+password_str, Toast.LENGTH_SHORT).show();

				userLoginTask.setContext(LoginActivity.this);

				if (lb.login()) {
					Log.e("e", "fuck");
					userLoginTask.execute(username_str, password_str, Url_str);
					Log.e("asdfadsf", "fuck");
				}
				/*
				 * if(result.equals("true") ){ Intent in = new Intent();
				 * in.setClass(LoginActivity.this, MainActivity.class);
				 * startActivity(in); } else{ Log.e("login", "error");
				 * Toast.makeText(LoginActivity.this, "error",
				 * Toast.LENGTH_SHORT).show(); }
				 */

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*
		 * int id = item.getItemId(); if (id == R.id.action_settings) { return
		 * true; }
		 */
		return super.onOptionsItemSelected(item);
	}
}
