package com.example.tsui;

import com.example.tsui.ViewPageFragment.MyPageChangeListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.tsui.LeftFragment;
import com.example.tsui.SlidingMenu;
import com.example.tsui.ViewPageFragment;
import com.example.tsui.R;


public class MainActivity extends AppCompatActivity {
	
	
	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	ViewPageFragment viewPageFragment;
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
	
		Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
		TextView mTitileBar = (TextView) findViewById(R.id.titleBar);
		// set action bar
		mTitileBar.setText("Time Store");
		setSupportActionBar(actionbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		actionbar.setNavigationIcon(R.drawable.ic_action_split);
		
		actionbar.setNavigationOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showLeft();
				
			}
		});
		

		
		init();
		initListener();

	}

	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.left_frame, null));
//		mSlidingMenu.setRightView(getLayoutInflater().inflate(
//				R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		leftFragment = new LeftFragment();
		t.replace(R.id.left_frame, leftFragment);
		
/*		rightFragment = new RightFragment();
		t.replace(R.id.right_frame, rightFragment);*/

		viewPageFragment = new ViewPageFragment();
		t.replace(R.id.center_frame, viewPageFragment);

		t.commit();
	}

	private void initListener() {
		viewPageFragment.setMyPageChangeListener(new MyPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(viewPageFragment.isFirst()){
					mSlidingMenu.setCanSliding(true,false);
				}else if(viewPageFragment.isEnd()){
					mSlidingMenu.setCanSliding(false,true);
				}else{
					mSlidingMenu.setCanSliding(false,false);
				}
			}
		});
	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}
/*
	public void showRight() {
		mSlidingMenu.showRightView();
	}
*/
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Activity registerActivity = new RegisterActivity();
			//Bundle bundle = new Bundle();
			//Bundle.putString("","");
			Intent intent = new Intent(MainActivity.this, registerActivity.getClass());
			//intent.putExtras(bundle);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
}
