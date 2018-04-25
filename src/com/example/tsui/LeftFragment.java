package com.example.tsui;


import java.util.ArrayList;

import com.example.tsui.negotiate.DemoApplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class LeftFragment extends Fragment {

	TextView user_info;
	TextView user_order;
	TextView user_name;
	RelativeLayout rl_left;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);
		user_info = (TextView) view.findViewById(R.id.user_info);
		user_order = (TextView)view.findViewById(R.id.user_order);
		user_name = (TextView)view.findViewById(R.id.name_textview);
		rl_left = (RelativeLayout) view.findViewById(R.id.rl_left);
		user_name.setText(DemoApplication.me.name);
		user_order.setOnClickListener(new fonclick());
		user_info.setOnClickListener(new sonclick());
		rl_left.setOnClickListener(new sonclick());
		
		return view;
	}
	
	private final class fonclick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent in = new Intent(getActivity(),OrderListActivity.class);
			startActivity(in);
			
		}
		
	}
	private final class sonclick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(),InfoActivity.class);
			startActivity(intent);
		}
		
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}