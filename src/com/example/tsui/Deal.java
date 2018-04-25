package com.example.tsui;

public class Deal{
	public String helped;
	public String type;
	public String startposition;
	public String endposition;
	public String description;
	public String date;
	public String finish;
	public String tpye;
	public String money;
	public String helper;
	
	public Deal() {
		
	}
	
	public Deal(String type,String startposition,String endposition,String description){
		this.type=type;
		this.startposition=startposition;
		this.endposition=endposition;
		this.description=description;
	}
}