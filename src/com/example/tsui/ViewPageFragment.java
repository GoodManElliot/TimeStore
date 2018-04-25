package com.example.tsui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.tsui.GooeyMenu.GooeyMenuInterface;
import com.example.tsui.baidumap.MapActivity;
import com.example.tsui.negotiate.DemoApplication;

import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.AvoidXfermode.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

class OrderPostTask extends AsyncTask<String, Void, JSONObject> {
	private Exception exception;
//	public String giveResult;
	private Context cont;
	protected JSONObject doInBackground(String... para){
		String start_point_str = para[0];
		String event_str = para[1];
		String end_point_str = para[2];
		String url_str = para[3];
		String username_str = para[4];
		Log.d("NET", start_point_str);
		Log.d("NET", event_str);
		Log.d("NET", end_point_str);
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
			jsonPara.put("start", start_point_str);
			jsonPara.put("event", event_str);
			jsonPara.put("end", end_point_str);
			jsonPara.put("username", username_str);
			jsonPara.put("type", DemoApplication.current_order_type);
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
			JSONObject resultJson = new JSONObject(result.toString());
			return resultJson;
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
	
	public void setContext(Context ccc){
		cont = ccc;
	}

	protected void onPostExecute(JSONObject result) {
		try {
			if (result.getString("result").equals("success")) {
				Intent intent = new Intent();
				intent.setClass(cont, MapActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("order_id", result.getString("id").toString());
				intent.putExtras(bundle);
				DemoApplication.current_order_id = result.getString("id").toString();
				cont.startActivity(intent); 
			} else {
				Toast.makeText(cont, "Error", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			Log.e("TIME", "Uncaught exception", e);
		}
		
		
		
	}
}
public class ViewPageFragment extends Fragment implements GooeyMenuInterface{
	private Toast mToast;
	private Button showLeft;
	private Button showRight;
	private MyAdapter mAdapter;
	private ViewPager mPager;
	private ArrayList<Fragment> pagerItemList = new ArrayList<Fragment>();
	
	GooeyMenu mGooeyMenu_busy;
	GooeyMenu mGooeyMenu_free;
	
	View popupWindow_view;
	private PopupWindow popupWindow; 
	
	public String start_text_string;
	public String event_text_string;
	public String end_text_string;

	
    @Override
    public void menuOpen(int type) {
        if (type==0) {
        	DemoApplication.busy = 0;
        	MapActivity mapActivity = new MapActivity();
    		Intent intent = new Intent(getActivity(),mapActivity.getClass());
    		startActivity(intent);
        }
        if (type==1) {
        	mGooeyMenu_free.setVisibility(View.INVISIBLE);
        }
        
    }

    @Override
    public void menuClose(int type) {
    	if (type==0) {
    		MapActivity mapActivity = new MapActivity();
    		Intent intent = new Intent(getActivity(),mapActivity.getClass());
    		startActivity(intent);
        }
    	if (type==1) {
    		
    		mGooeyMenu_free.setVisibility(View.VISIBLE);
    	}
        
    }

    @Override
    public void menuItemClicked(int menuNumber) {
        showToast( "Menu item clicked : " + menuNumber);
        if (menuNumber % 3 == 1) {
        	DemoApplication.current_order_type = "big";
        } else if (menuNumber % 3 == 2){
			DemoApplication.current_order_type = "small";
		}
        DemoApplication.busy = 1;
     // ��ȡ�Զ��岼���ļ�activity_popupwindow_left.xml����ͼ   
        // ����PopupWindowʵ��,200,LayoutParams.MATCH_PARENT�ֱ��ǿ�Ⱥ͸߶�  
        Button confirm;
        Button cancel;
        popupWindow = new PopupWindow(popupWindow_view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);  
        // ���ö���Ч��  
        popupWindow.setAnimationStyle(R.style.windowstyle);  
        // ������λ����ʾ��ʽ,����Ļ�����  
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        confirm = (Button) popupWindow_view.findViewById(R.id.confirm);
        cancel = (Button) popupWindow_view.findViewById(R.id.cancel);
        final EditText start_text = (EditText) popupWindow_view.findViewById(R.id.start_point);
        final EditText event_text = (EditText) popupWindow_view.findViewById(R.id.event);
        final EditText end_text = (EditText) popupWindow_view.findViewById(R.id.end_point);
        
        confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				start_text_string = start_text.getText().toString();
				event_text_string = event_text.getText().toString();
				end_text_string = end_text.getText().toString();  
		
				String Url_str = DemoApplication.url_base+"post_order";
				//Toast.makeText(RegisterActivity.this, username_str+" "+password_str, Toast.LENGTH_SHORT).show();
				OrderPostTask orderPostTask = new OrderPostTask();
				orderPostTask.setContext(getActivity());
				orderPostTask.execute(start_text_string,event_text_string,end_text_string,Url_str, DemoApplication.me.name);
				
				
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();  
                popupWindow = null;  
			}
		});
    }

   private void showToast(String msg){
        if(mToast!=null){
            mToast.cancel();
        }
       mToast= Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT);
       mToast.setGravity(Gravity.CENTER,0,0);
       mToast.show();
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.view_pager, null);
		popupWindow_view = inflater.inflate(R.layout.popwindow, null);
		
		// set circle menu
		mGooeyMenu_free = (GooeyMenu) mView.findViewById(R.id.gooey_menu_free);
		mGooeyMenu_busy = (GooeyMenu) mView.findViewById(R.id.gooey_menu_busy);
		mGooeyMenu_free.setOnMenuListener(this);
		mGooeyMenu_busy.setOnMenuListener(this);
		
		/*
		mGooeyMenu_free.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Activity loginActivity = new LoginActivity();
				Intent intent = new Intent(getActivity(),loginActivity.getClass());
				startActivity(intent);
			}
		});
		*/

//		showRight = (Button) mView.findViewById(R.id.showRight);
//		mPager = (ViewPager) mView.findViewById(R.id.pager);
/*		PageFragment1 page1 = new PageFragment1();
		PageFragment2 page2 = new PageFragment2();*/
/*		pagerItemList.add(page1);
		pagerItemList.add(page2);*/
/*		mAdapter = new MyAdapter(getFragmentManager());
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (myPageChangeListener != null)
					myPageChangeListener.onPageSelected(position);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				

			}

			@Override
			public void onPageScrollStateChanged(int position) {

				

			}
		});*/

		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
/*
		showLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).showLeft();
			}
		});
*/
/*
		showRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showRight();
			}
		});*/
	}

	public boolean isFirst() {
		if (mPager.getCurrentItem() == 0)
			return true;
		else
			return false;
	}

	public boolean isEnd() {
		if (mPager.getCurrentItem() == pagerItemList.size() - 1)
			return true;
		else
			return false;
	}

	public class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return pagerItemList.size();
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = null;
			if (position < pagerItemList.size())
				fragment = pagerItemList.get(position);
			else
				fragment = pagerItemList.get(0);

			return fragment;

		}
	}

	private MyPageChangeListener myPageChangeListener;

	public void setMyPageChangeListener(MyPageChangeListener l) {

		myPageChangeListener = l;

	}

	public interface MyPageChangeListener {
		public void onPageSelected(int position);
	}

}
