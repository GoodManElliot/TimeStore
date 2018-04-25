/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tsui.negotiate;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.example.server.toolkit.MyReceiver;
import com.example.tsui.ComUser;
import com.example.tsui.Deal;
import com.example.tsui.MapPopupWindowActivity;
import com.example.tsui.OrderActivity;
import com.example.tsui.baidumap.MapActivity;

public class DemoApplication extends Application {

	public int num=0;
	public static Context applicationContext;
	private static DemoApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";
	
	public static String current_order_type;
	//public static String url_base = "http://104.236.159.207:5000/time_store/api/";


	//public static String url_base = "http://192.168.1.238:5000/time_store/api/";
	//public static String url_base = "http://10.12.12.34:5000/time_store/api/";
	//public static String url_base = "http://192.168.1.238:5000/time_store/api/";
	public static String url_base = "http://127.0.0.1:5000/time_store/api/";

	//public static String url_base = "http://10.12.12.34:5000/time_store/api/";
	//public static String url_base = "http://10.180.73.77:5000/time_store/api/";


	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();
	public static ComUser me=null;
	public static ComUser contractor=null;
	public static ComUser userlist=null;
	public static MyReceiver myreceiver;
	public static MapPopupWindowActivity popWindow;
	public static MapActivity map=null;
	public static int busy = 0;
	public static Deal currentDeal=null;
	public static OrderActivity orderPopWindow;

	public static String current_order_id = "-1";
	@Override  
	public void onCreate() {
		super.onCreate();
	//	this.myreceiver=new MyReceiver(this);
        applicationContext = this;
        SDKInitializer.initialize(applicationContext);
        hxSDKHelper.onInit(applicationContext);
        instance = this;
        

        /**
         * this function will initialize the HuanXin SDK
         * 
         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
         * 
         * 环信初始化SDK帮助函数
         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         * 
         * for example:
         * 例子：
         * 
         * public class DemoHXSDKHelper extends HXSDKHelper
         * 
         * HXHelper = new DemoHXSDKHelper();
         * if(HXHelper.onInit(context)){
         *     // do HuanXin related work
         * }
         */
        
        /*getInstance().init(applicationContext);
        EMChat.getInstance().init(applicationContext);*/
	}

	public static DemoApplication getInstance() {
		return instance;
	}
 

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param user
	 */
	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final boolean isGCM,final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(isGCM,emCallBack);
	}
}
