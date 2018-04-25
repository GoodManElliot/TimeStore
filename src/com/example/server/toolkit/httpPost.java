package com.example.server.toolkit;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.os.AsyncTask;
import android.util.Log;

public class httpPost extends AsyncTask<PostClass, integer, Result> {
private Exception exception;
	
	protected Result doInBackground(PostClass... para){
		PostClass postClass = para[0];
		
		Log.d("NET", postClass.url);
		
		URL url = null;
		try {
			url = new URL(postClass.url);
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Content-Type", "application/json");
			//urlConnection.setRequestProperty("Charset", "utf-8");
			
			urlConnection.connect();
			
			DataOutputStream dop = new DataOutputStream(urlConnection.getOutputStream());
			JSONObject jsonPara = postClass.data;
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
			Result resultclass = new Result();
			try {
				resultclass = new Result(new JSONObject(result.toString()));
			} catch (JSONException e) {
				Log.e("TIME", "Uncaught exception", e);
			}
			return resultclass;
		} 
		catch (MalformedURLException e) {
			Log.e("TIME", "Uncaught exception", e);
		}
		catch (IOException e) {
			Log.e("TIME", "Uncaught exception", e);
		}
		return null;
	}
	

	protected void onPostExecute(Result result) {
		
	}

}
