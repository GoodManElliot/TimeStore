package com.example.tsui;

import com.example.tsui.negotiate.DemoApplication;

import android.app.Activity;
import android.content.Context;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MapPopupWindowActivity extends Activity {

	private Context mContext = null;
//	Button bbb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DemoApplication.popWindow = this;
		setContentView(R.layout.activity_map_popup_window);
/*		LinearLayout v = (LinearLayout) findViewById(R.id.ll_map);//锟揭碉拷锟斤拷要锟斤拷透锟斤拷锟斤拷锟斤拷锟斤拷layout 锟斤拷id 
		v.getBackground().setAlpha(30);//0~255透锟斤拷锟斤拷值
*/
		mContext = this;
		Log.d("bbb", "bbb");

		/*bbb = (Button) findViewById(R.id.bbb);
		Log.d("daozhelema", "daozhele");
		bbb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopupWindow(view);
			}
		});*/
	}

	private void showPopupWindow(View view) {

		// 一锟斤拷锟皆讹拷锟斤拷牟锟斤拷郑锟斤拷锟轿拷锟绞撅拷锟斤拷锟斤拷锟�
		View contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_window, null);
		// 锟斤拷锟矫帮拷钮锟侥碉拷锟斤拷录锟�
		Button conf_order = (Button) contentView.findViewById(R.id.conf_order);
		conf_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(mContext, "button is
				// pressed",Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		
		Button canc_order = (Button) findViewById(R.id.canc_order);
		canc_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		

		final PopupWindow popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		// 锟斤拷锟矫讹拷锟斤拷效锟斤拷
		popupWindow.setAnimationStyle(R.style.windowstyle);
		// 锟斤拷锟斤拷锟斤拷位锟斤拷锟斤拷示锟斤拷式,锟斤拷锟斤拷幕锟斤拷锟斤拷锟�
		popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);

		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;
				// 锟斤拷锟斤拷锟斤拷锟斤拷true锟侥伙拷锟斤拷touch锟铰硷拷锟斤拷锟斤拷锟斤拷锟斤拷
				// 锟斤拷锟截猴拷 PopupWindow锟斤拷onTouchEvent锟斤拷锟斤拷锟斤拷锟矫ｏ拷锟斤拷锟斤拷锟斤拷锟解部锟斤拷锟斤拷锟睫凤拷dismiss
			}
		});

	}

}
