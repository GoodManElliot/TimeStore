package com.example.tsui.negotiate;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.example.tsui.LoginActivity;
import com.example.tsui.RegisterActivity;

public class registerback{
   private RegisterActivity la;
   
   public registerback(RegisterActivity la){
	   this.la=la;
   }
   
   public boolean register() {
		final String username = this.la.username_str.trim();
		final String pwd = this.la.password_str.trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this.la, "username can't be empty", Toast.LENGTH_SHORT).show();
			return false;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this.la, "password can't be empty", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
			final ProgressDialog pd = new ProgressDialog(this.la);
			pd.setMessage("registering...");
			pd.show();

			new Thread(new Runnable() {
				public void run() {
					try {
						// 调用sdk注册方法
						EMChatManager.getInstance().createAccountOnServer(username, pwd);
						registerback.this.la.runOnUiThread(new Runnable() {
							public void run() {
									pd.dismiss();
								// 保存用户名
								DemoApplication.getInstance().setUserName(username);
							}
						});
					} catch (final EaseMobException e) {
						registerback.this.la.runOnUiThread(new Runnable() {
							public void run() {
									pd.dismiss();
								int errorCode=e.getErrorCode();
								if(errorCode==EMError.NONETWORK_ERROR){
									Toast.makeText(registerback.this.la, "network error!", Toast.LENGTH_SHORT).show();
								}else if(errorCode == EMError.USER_ALREADY_EXISTS){
									Toast.makeText(registerback.this.la,"user already exist!", Toast.LENGTH_SHORT).show();
								}else if(errorCode == EMError.UNAUTHORIZED){
									Toast.makeText(registerback.this.la, "unauthorized!", Toast.LENGTH_SHORT).show();
								}else if(errorCode == EMError.ILLEGAL_USER_NAME){
								    Toast.makeText(registerback.this.la, "illegal user name!",Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(registerback.this.la, "fail:" + e.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
				}
			}).start();

		}
		return true;
	}
}
