package com.example.server.toolkit;

import org.json.JSONObject;

public class Result {
	public JSONObject result;
	public String shortResult;
	public int status;
	public Result() {
		status = 0;
	}
	public Result(JSONObject result) {
		this.result = result;
		status = 1;
	}
}
