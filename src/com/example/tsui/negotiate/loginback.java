package com.example.tsui.negotiate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.tsui.LoginActivity;;

public class loginback {
	private LoginActivity la;
	private boolean progressShow;
	public boolean state;
	public int next=0;
	
	public loginback(LoginActivity la){
		this.la=la;
		state=false;
	}
	
	public boolean checklogin(){
		if (DemoHXSDKHelper.getInstance().isLogined()) {
			return true;
		}
		else return false;
	}
	
	public boolean login() {
		if (!CommonUtils.isNetWorkConnected(la)) {
			Toast.makeText(this.la, "network not available", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(this.la.username_str)) {
			Toast.makeText(this.la, "username empty", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(this.la.password_str)) {
			Toast.makeText(this.la, "password empty", Toast.LENGTH_SHORT).show();
			return false;
		}

		progressShow = true;
		final ProgressDialog pd = new ProgressDialog(this.la);
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				progressShow = false;
			}
		});
		pd.setMessage("loading...");
		pd.show();

		final long start = System.currentTimeMillis();
		// 调用sdk登陆方法登陆聊天服务�?
		EMChatManager.getInstance().login(this.la.username_str, this.la.password_str, new EMCallBack() {

			@Override
			public void onSuccess() {
				if (!progressShow) {
					return;
				}
				loginback.this.state=true;
				next=1;
				// 登陆成功，保存用户名密码
				DemoApplication.getInstance().setUserName(loginback.this.la.username_str);
				DemoApplication.getInstance().setPassword(loginback.this.la.username_str);

				/*try {
					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回�?
					// ** manually load all local groups and
				    EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					// 处理好友和群�?
					initializeContacts();
				} catch (Exception e) {
					e.printStackTrace();
					// 取好友或者群聊失败，不让进入主页�?
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							DemoHXSDKHelper.getInstance().logout(true,null);
							Toast.makeText(getApplicationContext(), R.string.login_failure_failed, 1).show();
						}
					});
					return;
				}*/
				// 更新当前用户的nickname 此方法的作用是在ios离线推�?�时能够显示用户nick
				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
						DemoApplication.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}
				if (!loginback.this.la.isFinishing() && pd.isShowing()) {
					pd.dismiss();
				}
				// 进入主页�?
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				if (!progressShow) {
					next=-1;
					return;
				}
				loginback.this.state=false;
				next=-1;
						pd.dismiss();
						Toast.makeText(loginback.this.la, "failed!\t" + message,
								Toast.LENGTH_SHORT).show();
			}
		});
		while(next==0){
			try{
				Thread.sleep(10,0);
			}
			catch(Exception e){
				Log.e("sleep error", e.toString());
			}
		}
		return state;
	}
}
