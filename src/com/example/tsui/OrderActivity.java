package com.example.tsui;

import com.example.tsui.negotiate.DemoApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.example.tsui.R;

public class OrderActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
		TextView mTitileBar = (TextView) findViewById(R.id.titleBar);
		// set action bar
		mTitileBar.setText("Time Store");
		setSupportActionBar(actionbar);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		actionbar.setNavigationIcon(R.drawable.ic_action_back);
		Intent intent = getIntent();
		int id = (Integer) intent.getSerializableExtra("id");
		
		Log.d("ID",""+id);
		
		TextView helper_name = (TextView)findViewById(R.id.username1);
		
		TextView helped_name = (TextView)findViewById(R.id.username2);
		TextView type_view = (TextView)findViewById(R.id.type);
		TextView event_view = (TextView)findViewById(R.id.case_content);
		TextView money_view = (TextView)findViewById(R.id.case_money);
		//String haha = DemoApplication.me.deallist.get(id).helper.toString();
		int length = DemoApplication.me.deallist.size();
		Log.d("Length", ""+length);
		if (DemoApplication.me.deallist.get(id) !=null) {
			Log.d("NULL", "NOT");
		} else {
			Log.d("NULL", "YES");
		}
		
		helper_name.setText(DemoApplication.me.deallist.get(id).helper.toString());
		helped_name.setText(DemoApplication.me.deallist.get(id).helped.toString());
		type_view.setText(DemoApplication.me.deallist.get(id).type.toString());
		event_view.setText(DemoApplication.me.deallist.get(id).description.toString());
		money_view.setText(DemoApplication.me.deallist.get(id).money.toString());
		
		Button finish_order = (Button) findViewById(R.id.finish_order); //完成订单按钮
		finish_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		actionbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startActivity(Intent(this,MainActivity.class));
				finish();
			} 
		});

	}
}
