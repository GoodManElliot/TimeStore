package com.example.server.toolkit;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DirectionClass {
	String url = "http://104.236.159.207:5000/time_store/api/direction";
	JSONObject data;
	public DirectionClass(String direction) {
		data = new JSONObject();
		try {
			data.put("direction", direction);
		}
		catch (JSONException e){
			Log.e("NET", "JSON exception", e);
		}
	}
	
	public void upload() {
		PostClass post = new PostClass(url, data);
		httpPost postTask = new httpPost();
		postTask.execute(post);
	}
}
