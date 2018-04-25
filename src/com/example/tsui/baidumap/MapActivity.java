package com.example.tsui.baidumap;
import cn.jpush.android.api.JPushInterface;

import com.example.server.toolkit.MyReceiver;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.example.clusterutil.clustering.ClusterItem;
import com.example.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.Marker;
import com.example.overlayutil.DrivingRouteOverlay;
import com.example.overlayutil.OverlayManager;
import com.example.overlayutil.TransitRouteOverlay;
import com.example.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.server.toolkit.MyReceiver;
import com.example.tsui.ComUser;
import com.example.tsui.LoginActivity;
import com.example.tsui.MainActivity;
import com.example.tsui.MapPopupWindowActivity;
import com.example.tsui.R;
import com.example.tsui.baidumap.MyOrientationListener;
import com.example.tsui.negotiate.ChatActivity;
import com.example.tsui.negotiate.Constant;
import com.example.tsui.negotiate.DemoApplication;
class UpdateCoorTask extends AsyncTask<String, Void, String> {
	private Exception exception;
//	public String giveResult;
	private Context cont;
	public String pref_username;
	protected String doInBackground(String... para){
		String username = para[0];
		String order_id = para[1];
		String lat = para[2];
		String lot = para[3];
		String url_str = para[4];
		pref_username = username;
		Log.d("NET", username);
		Log.d("NET", url_str);
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
			jsonPara.put("username", username);
			jsonPara.put("order_id", DemoApplication.current_order_id);
			jsonPara.put("lat", lat);
			jsonPara.put("lon", lot);
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
			return result;
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

	protected void onPostExecute(String result) {
		
	}
}
class NeedHelpTask extends AsyncTask<String, Void, String> {
	private Exception exception;
//	public String giveResult;
	private Context cont;
	public String pref_username;
	protected String doInBackground(String... para){
		String username = para[0];
		String order_id = para[1];
		String url_str = para[2];
		pref_username = username;
		Log.d("NET", username);
		Log.d("NET", url_str);
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
			jsonPara.put("username", username);
			jsonPara.put("order_id", order_id);
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
			return result;
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

	protected void onPostExecute(String result) {
		
	}
}

class WaitOfferHelpTask extends AsyncTask<String, Void, String> {
	private Exception exception;
//	public String giveResult;
	private Context cont;
	public String pref_username;
	protected String doInBackground(String... para){
		String username = para[0];
		String url_str = para[1];
		pref_username = username;
		Log.d("NET", username);
		Log.d("NET", url_str);
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
			jsonPara.put("username", username);
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
			return result;
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

	protected void onPostExecute(String result) {
		
	}
}

public class MapActivity extends Activity implements BaiduMap.OnMapClickListener,
OnGetRoutePlanResultListener
{
	/**
	 * 锟斤拷图锟截硷拷
	 */
	MapView mMapView;
	/**
	 * 锟斤拷图实锟斤拷
	 */
	BaiduMap mBaiduMap;
	/**
	 * 锟斤拷位锟侥客伙拷锟斤拷
	 */
	public LocationClient mLocationClient;
	/**
	 * 锟斤拷位锟侥硷拷锟斤拷锟斤拷
	 */
	public MyLocationListener mMyLocationListener;
	/**
	 * 锟斤拷前锟斤拷位锟斤拷模式
	 */
	private LocationMode mCurrentMode = LocationMode.NORMAL;
	/***
	 * 锟角凤拷锟角碉拷一锟轿讹拷位
	 */
	private volatile boolean isFristLocation = true;

	/**
	 * 锟斤拷锟斤拷一锟轿的撅拷纬锟斤拷
	 */
	private double mCurrentLantitude;
	private double mCurrentLongitude;
	/**
	 * 锟斤拷前锟侥撅拷锟斤拷
	 */
	private float mCurrentAccracy;
	/**
	 * 锟斤拷锟津传革拷锟斤拷锟侥硷拷锟斤拷锟斤拷
	 */
	private MyOrientationListener myOrientationListener;
	/**
	 * 锟斤拷锟津传革拷锟斤拷X锟斤拷锟斤拷锟街�
	 */
	private int mXDirection;

	/**
	 * 锟斤拷图锟斤拷位锟斤拷模式
	 */
	private String[] mStyles = new String[] { "锟斤拷图模式锟斤拷锟斤拷", "锟斤拷图模式锟斤拷锟斤拷锟芥】",
			"锟斤拷图模式锟斤拷锟斤拷锟教★拷" };
	private int mCurrentStyle = 0;
	
	public overlayUpdater ou;
	public List<Marker> lm;
	private InfoWindow mInfoWindow;
	private Message mes;
	//private ClusterManager<userPosition> mClusterManager;
	MapStatus ms;
	
	Button mBtnPre = null;//锟斤拷一锟斤拷锟节碉拷
    Button mBtnNext = null;//锟斤拷一锟斤拷锟节碉拷
    int nodeIndex = -1;//锟节碉拷锟斤拷锟斤拷,锟斤拷锟斤拷锟斤拷诘锟绞笔癸拷锟�
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    private TextView popupText = null;
    RoutePlanSearch mSearch = null;    // 锟斤拷锟斤拷模锟介，也锟斤拷去锟斤拷锟斤拷图模锟斤拷锟斤拷锟绞癸拷锟�
    boolean isconnected;
    int role;
    boolean inSearching;
    private void init(){
		Log.d("PUSH", "INIT");
		JPushInterface.init(getApplicationContext());
	}

	private MyReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.tsui.MESSAGE_RECEIVED_ACTION";
	public void registerMessageReceiver() {
		mMessageReceiver = new MyReceiver();//((DemoApplication)getApplication()).myreceiver;
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		inSearching=false;
		super.onCreate(savedInstanceState);
		init();
		registerMessageReceiver();
		role=getIntent().getIntExtra("role", Constant.BUSY);
		Log.d("role", ""+role);
		if (DemoApplication.busy==1) {
			
			NeedHelpTask needHelpTask = new NeedHelpTask();
			needHelpTask.setContext(MapActivity.this);
			String Url_str = DemoApplication.url_base+"need_help";
			needHelpTask.execute(DemoApplication.me.name.toString(),DemoApplication.current_order_id,Url_str);
			
		} else if (DemoApplication.busy==0) {
			WaitOfferHelpTask waitOfferHelpTask = new WaitOfferHelpTask();
			waitOfferHelpTask.setContext(MapActivity.this);
			String Url_str = DemoApplication.url_base+"Ioffer";
			waitOfferHelpTask.execute(DemoApplication.me.name.toString(), Url_str);
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map);
		// 注锟斤拷梅锟斤拷锟揭拷锟絪etContentView锟斤拷锟斤拷之前实锟斤拷
		
		
	    Intent in = new Intent(this, MapPopupWindowActivity.class);
	    startActivity(in);
		
		// 锟斤拷一锟轿讹拷位
		isFristLocation = true;
		isconnected=false;
		// 锟斤拷取锟斤拷图锟截硷拷锟斤拷锟斤拷
		mMapView = (MapView) findViewById(R.id.map);
		mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        
		// 锟斤拷玫锟酵硷拷锟绞碉拷锟�
		
		mBaiduMap = mMapView.getMap();
		// 锟斤拷锟斤拷锟桔合癸拷锟斤拷锟斤拷ClusterManager
		//mBaiduMap.setOnMapLoadedCallback(this);
        //mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
		// 锟斤拷始锟斤拷锟斤拷位
		init_cluster();
		initMyLocation();
//		// 锟斤拷始锟斤拷锟斤拷锟斤拷锟斤拷
		initOritationListener();
//		this.registerMessageListener(new Message(this));
		this.registerLocDetector(new LocDetector(this));
		
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				OnInfoWindowClickListener listener = null;
				
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
					button.setText("锟斤拷锟揭革拷锟斤？");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							mBaiduMap.hideInfoWindow();
						}
					};
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
					mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
		
		//锟斤拷图锟斤拷锟斤拷录锟斤拷锟斤拷锟�
        mBaiduMap.setOnMapClickListener(this);
        // 锟斤拷始锟斤拷锟斤拷锟斤拷模锟介，注锟斤拷锟铰硷拷锟斤拷锟斤拷
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        
        ((DemoApplication)getApplication()).map=this;
	}

    private void registerLocDetector(LocDetector ld){
    	ld.start();
    }
	
	private void registerMessageListener(Message mes){
		mes.start();
	}
	/**
	 * 锟斤拷始锟斤拷锟斤拷锟津传革拷锟斤拷
	 */
	private void initOritationListener()
	{
		myOrientationListener = new MyOrientationListener(
				getApplicationContext());
		myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener()
				{
					@Override
					public void onOrientationChanged(float x)
					{
						mXDirection = (int) x;

						MyLocationData locData = new MyLocationData.Builder()
								.accuracy(mCurrentAccracy)
								.direction(mXDirection)
								.latitude(mCurrentLantitude)
								.longitude(mCurrentLongitude).build();
						mBaiduMap.setMyLocationData(locData);
						BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
								.fromResource(R.drawable.navi_map_gps_locked);
						MyLocationConfiguration config = new MyLocationConfiguration(
								mCurrentMode, true, mCurrentMarker);
						mBaiduMap.setMyLocationConfigeration(config);

					}
				});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		finish();
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 锟斤拷始锟斤拷锟斤拷位锟斤拷卮锟斤拷锟�
	 */
	private void initMyLocation()
	{
		// 锟斤拷位锟斤拷始锟斤拷
		mLocationClient = new LocationClient(this);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		// 锟斤拷锟矫讹拷位锟斤拷锟斤拷锟斤拷锟斤拷锟�
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 锟斤拷gps
		option.setCoorType("bd09ll"); // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		option.setScanSpan(200);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	/**
	 * 实锟斤拷实位锟截碉拷锟斤拷锟斤拷
	 */
	public class MyLocationListener implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{

			// map view 锟斤拷俸锟斤拷诖锟斤拷锟斤拷陆锟斤拷盏锟轿伙拷锟�
			if (location == null || mMapView == null)
				return;
			// 锟斤拷锟届定位锟斤拷锟�
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 锟剿达拷锟斤拷锟矫匡拷锟斤拷锟竭伙拷取锟斤拷锟侥凤拷锟斤拷锟斤拷息锟斤拷顺时锟斤拷0-360
					.direction(mXDirection).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mCurrentAccracy = location.getRadius();		// 锟斤拷锟矫讹拷位锟斤拷锟�
			mBaiduMap.setMyLocationData(locData);
			mCurrentLantitude = location.getLatitude();
			mCurrentLongitude = location.getLongitude();
			// 锟斤拷锟斤拷锟皆讹拷锟斤拷图锟斤拷
			BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
					.fromResource(R.drawable.navi_map_gps_locked);
			MyLocationConfiguration config = new MyLocationConfiguration(
					mCurrentMode, true, mCurrentMarker);
			mBaiduMap.setMyLocationConfigeration(config);
			// 锟斤拷一锟轿讹拷位时锟斤拷锟斤拷锟斤拷图位锟斤拷锟狡讹拷锟斤拷锟斤拷前位锟斤拷
			if (isFristLocation)
			{
				isFristLocation = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
     * 锟斤拷锟斤拷路锟竭规划锟斤拷锟斤拷示锟斤拷
     *
     * @param v
     */
    public void SearchButtonProcess(View v) {
        //锟斤拷锟斤拷锟斤拷锟斤拷诘锟斤拷路锟斤拷锟斤拷锟�
    	inSearching=true;
    	String city=mLocationClient.getLastKnownLocation().getCity();
    	Log.i("city",city);
    	double lat=mLocationClient.getLastKnownLocation().getLatitude();
    	Log.i("lat",String.valueOf(lat));
        route = null;
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mBaiduMap.clear();
        // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷钮锟斤拷应
        EditText editSt = (EditText) findViewById(R.id.start);
        EditText editEn = (EditText) findViewById(R.id.end);
        //锟斤拷锟斤拷锟斤拷锟秸碉拷锟斤拷息锟斤拷锟斤拷锟斤拷tranist search 锟斤拷说锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(city, editSt.getText().toString());
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, editEn.getText().toString());

        // 实锟斤拷使锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟秸碉拷锟斤拷薪锟斤拷锟斤拷锟饺凤拷锟斤拷瓒�
        if (v.getId() == R.id.drive) {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        } else if (v.getId() == R.id.transit) {
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode)
                    .city(city)
                    .to(enNode));
        } else if (v.getId() == R.id.walk) {
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        }
    }

    public void gotoNego(View v){
    	Intent intent = new Intent();
    	intent.setClass(MapActivity.this, ChatActivity.class);
    	startActivity(intent);
    }
    
    /**
     * 锟节碉拷锟斤拷锟绞撅拷锟�
     *
     * @param v
     */
    public void nodeClick(View v) {
        if (route == null ||
                route.getAllStep() == null) {
            return;
        }
        if (nodeIndex == -1 && v.getId() == R.id.pre) {
        	return;
        }
        //锟斤拷锟矫节碉拷锟斤拷锟斤拷
        if (v.getId() == R.id.next) {
            if (nodeIndex < route.getAllStep().size() - 1) {
            	nodeIndex++;
            } else {
            	return;
            }
        } else if (v.getId() == R.id.pre) {
        	if (nodeIndex > 0) {
        		nodeIndex--;
        	} else {
            	return;
            }
        }
        //锟斤拷取锟节斤拷锟斤拷锟较�
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = route.getAllStep().get(nodeIndex);
        if (step instanceof DrivingRouteLine.DrivingStep) {
            nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrance().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
        } else if (step instanceof WalkingRouteLine.WalkingStep) {
            nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrance().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
        } else if (step instanceof TransitRouteLine.TransitStep) {
            nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }
        //锟狡讹拷锟节碉拷锟斤拷锟斤拷锟斤拷
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
        popupText = new TextView(MapActivity.this);
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaiduMap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapActivity.this, "锟斤拷歉锟斤拷未锟揭碉拷锟斤拷锟�", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //锟斤拷锟秸碉拷锟酵撅拷锟斤拷锟斤拷址锟斤拷锟斤拷澹拷锟斤拷锟斤拷陆涌诨锟饺★拷锟斤拷锟斤拷询锟斤拷息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapActivity.this, "锟斤拷歉锟斤拷未锟揭碉拷锟斤拷锟�", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //锟斤拷锟秸碉拷锟酵撅拷锟斤拷锟斤拷址锟斤拷锟斤拷澹拷锟斤拷锟斤拷陆涌诨锟饺★拷锟斤拷锟斤拷询锟斤拷息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapActivity.this, "锟斤拷歉锟斤拷未锟揭碉拷锟斤拷锟�", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //锟斤拷锟秸碉拷锟酵撅拷锟斤拷锟斤拷址锟斤拷锟斤拷澹拷锟斤拷锟斤拷陆涌诨锟饺★拷锟斤拷锟斤拷询锟斤拷息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    //锟斤拷锟斤拷RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return null;
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return null;
        }
    }

    @Override
    public void onMapClick(LatLng point) {
    	inSearching=false;
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
    	return false;
    }
	/**
	 * 锟斤拷图锟狡讹拷锟斤拷锟揭碉拷位锟斤拷,锟剿达拷锟斤拷锟斤拷锟斤拷锟铰凤拷锟斤拷位锟斤拷锟斤拷然锟斤拷位锟斤拷 
	 * 直锟斤拷锟斤拷锟斤拷锟揭伙拷尉锟轿筹拷龋锟斤拷锟斤拷时锟斤拷没锟叫讹拷位锟缴癸拷锟斤拷锟斤拷锟杰伙拷锟斤拷示效锟斤拷锟�
	 */
	private void center2myLoc()
	{
		LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.animateMapStatus(u);
	}
	
	public BitmapDescriptor getDefaultPic(){
			return BitmapDescriptorFactory
					.fromResource(R.drawable.icon_marka);
	}

	public void init_cluster(){
		//mClusterManager = new ClusterManager(this, mBaiduMap);
        // 锟斤拷锟組arker锟斤拷
		//mClusterManager.addItems(items);
		ou=new overlayUpdater(this);
		lm=new ArrayList<Marker>();
        // 锟斤拷锟矫碉拷图锟斤拷锟斤拷锟酵甲刺拷锟斤拷锟侥憋拷时锟斤拷锟斤拷锟叫碉拷酆锟斤拷锟斤拷锟�
        //mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
	} 
	
	@Override
	protected void onStart()
	{
		// 锟斤拷锟斤拷图锟姐定位
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted())
		{
			mLocationClient.start();
		}
		// 锟斤拷锟斤拷锟斤拷锟津传革拷锟斤拷
		myOrientationListener.start();
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		// 锟截憋拷图锟姐定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();

		// 锟截闭凤拷锟津传革拷锟斤拷
		myOrientationListener.stop();
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		DemoApplication.me.inbusiness=false;
		DemoApplication.contractor=null;
		DemoApplication.me.state=Constant.NOSTATE;
		unregisterReceiver(mMessageReceiver);
		((DemoApplication)getApplication()).me.setState(Constant.NOSTATE);
		mMapView.onDestroy();
		super.onDestroy();
		// 锟斤拷activity执锟斤拷onDestroy时执锟斤拷mMapView.onDestroy()锟斤拷实锟街碉拷图锟斤拷锟斤拷锟斤拷锟节癸拷锟斤拷
		mMapView = null;
		Iterator<userPosition> iter=this.ou.getIter();
		while(iter.hasNext()){
			userPosition up=iter.next();
			up.pic.recycle();
		}
	}

	@Override
	protected void onResume()
	{
		mMapView.onResume();
		super.onResume();
		// 锟斤拷activity执锟斤拷onResume时执锟斤拷mMapView. onResume ()锟斤拷实锟街碉拷图锟斤拷锟斤拷锟斤拷锟节癸拷锟斤拷
	}

	@Override
	protected void onPause()
	{
		mMapView.onPause();
		super.onPause();
		// 锟斤拷activity执锟斤拷onPause时执锟斤拷mMapView. onPause ()锟斤拷实锟街碉拷图锟斤拷锟斤拷锟斤拷锟节癸拷锟斤拷
	}

	void update(){
		mBaiduMap.clear();
		Iterator<userPosition> iter=this.ou.getIter();
		//double xmin=1000,ymin=1000,xmax=-1000,ymax=-1000;
		while(iter.hasNext()){
			userPosition up=iter.next();
			MarkerOptions oo = new MarkerOptions().position(up.ll).icon(up.pic)
					.zIndex(9).draggable(true);
			lm.add((Marker) (mBaiduMap.addOverlay(oo)));
			/*if(xmin>up.ll.latitude) xmin=up.ll.latitude;
			if(ymin>up.ll.longitude) xmin=up.ll.longitude;
			if(xmax<up.ll.latitude) xmin=up.ll.latitude;
			if(ymax<up.ll.longitude) xmin=up.ll.longitude;*/
		}
		/*mClusterManager.clearItems();
		mClusterManager.addItems(ou.users);*/
	}
	
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}
	
    public void resetOverlay(View view) {
        clearOverlay(null);
        update();
    }
    
    /*@Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub
    	ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }*/
    
   
}
class overlayUpdater{
	public List<userPosition> users;
	public MapActivity mapactivity;
	
	public overlayUpdater(MapActivity mapactivity) {
		// TODO Auto-generated constructor stub
	    this.mapactivity=mapactivity;
	    users=new ArrayList<userPosition>();
	}
	
	public void notifyChange(){
		this.mapactivity.update();
	}
	
	public void addItem(userPosition u){
		this.users.add(u);
	}
	public Iterator<userPosition> getIter(){
		return  users.iterator();
	}
}

class userPosition implements ClusterItem{
	public BitmapDescriptor pic;
	public LatLng ll;
	
	public userPosition(BitmapDescriptor pic,LatLng ll){
		this.pic=pic;
		this.ll=ll;
	}
	
    @Override
    public LatLng getPosition() {
        return ll;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
    	return pic;
    }
    
    public void updatePosition(LatLng newll){
    	this.ll=newll;
    }
    
}

class LocDetector extends Thread{
	MapActivity ma;
	DemoApplication da;
	
	public LocDetector(MapActivity ma){
		this.ma=ma;
		this.da=DemoApplication.getInstance();
		Log.d("Have", "INIT");
	}
	@Override
	public void run(){
		Log.d("can", "you run?");
		boolean isfirst=true;
		userPosition up=null;
			while (true) {
				BDLocation bdl=ma.mLocationClient.getLastKnownLocation();
				if(bdl!=null)
				da.me.updatePosition(bdl.getLongitude(),
						bdl.getLatitude());
				Log.d("update","haha");
				UpdateCoorTask updateCoorTask = new UpdateCoorTask();
				String Url_str = DemoApplication.url_base+"post_coor";
				updateCoorTask.execute(DemoApplication.me.name,DemoApplication.current_order_id,""+DemoApplication.me.lat,""+DemoApplication.me.lot,Url_str);
				try{
					sleep(1000, 0);
				}
				catch(Exception e){
					
				}
				if(da.contractor!=null&&!ma.inSearching){
					if(isfirst){				
						up=new userPosition(ma.getDefaultPic(), new LatLng(da.contractor.lat, da.contractor.lot));
						ma.ou.addItem(up);
						ma.ou.notifyChange();
					    isfirst=false;
					}
					else{
						ma.ou.users.get(0).updatePosition(new LatLng(da.contractor.lat, da.contractor.lot));
						ma.ou.notifyChange();
						Log.i(String.valueOf(da.contractor.lat), String.valueOf((da.contractor.lot)));
					}
				}
			}
			
			
			/*
			da.me.updatePosition(ma.mLocationClient.getLastKnownLocation().getLongitude(),
					ma.mLocationClient.getLastKnownLocation().getLatitude());
			*/
			/*
			UpdateCoorTask updateCoorTask = new UpdateCoorTask();
			String Url_str = DemoApplication.url_base+"post_coor";
			updateCoorTask.execute(DemoApplication.me.name,DemoApplication.current_order_id,""+DemoApplication.me.lat,""+DemoApplication.me.lot,Url_str);
			sleep(100, 0);
			*/
	}
	
	
}


