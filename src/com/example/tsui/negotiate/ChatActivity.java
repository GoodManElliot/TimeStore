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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
/*import com.easemob.chatuidemo.activity.ForwardMessageActivity;*/
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.example.tsui.R;

/**
 * 閼卞﹤銇夋い鐢告桨
 * 
 */
public class ChatActivity extends BaseActivity implements OnClickListener, EMEventListener{
	private static final String TAG = "ChatActivity";
	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int SYSTEM = 2;
	public static final int SERVICE = 3;

	private View more;
	public static final String COPY_IMAGE = "EASEMOBIMG";
	private ListView listView;
	private PasteEditText mEditTextContent;
	private View buttonSetModeVoice;
	private View buttonSend;
	// private ViewPager expressionViewpager;
	private LinearLayout emojiIconContainer;
	private LinearLayout btnContainer;
	private ImageView locationImgview;
	private int position;
	private ClipboardManager clipboard;
	private ViewPager expressionViewpager;
	private InputMethodManager manager;
	private List<String> reslist;
	private int chatType;
	private EMConversation conversation;
	public static ChatActivity activityInstance = null;
	private String toChatUsername;
	private VoiceRecorder voiceRecorder;
	private MessageAdapter adapter;
	private File cameraFile;
	private ImageView photo;
	static int resendPos;
	private LinearLayout bottomcontainer;
	

	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private ProgressBar loadmorePB;
	private boolean isloading;
	private final int pagesize = 20;
	private boolean haveMoreData = true;
	private Button btnMore;
	public String playMsgId;
	

	private SwipeRefreshLayout swipeRefreshLayout;

	public EMGroup group;
	public EMChatRoom room;
	public boolean isRobot;
	private DemoApplication da;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		da=DemoApplication.getInstance();
		activityInstance = this;
		initView();
		setUpView();
	}

	/**
	 * initView
	 */
	protected void initView() {
		photo = (ImageView)findViewById(R.id.photo);
		more = findViewById(R.id.more);
		listView = (ListView) findViewById(R.id.list);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
/*		buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);*/
		buttonSend = findViewById(R.id.btn_send);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
		bottomcontainer = (LinearLayout) findViewById(R.id.rl_bottom);
		
		reslist = getExpressionRes(35);

		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		views.add(gv1);
		views.add(gv2);
		expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		edittext_layout.requestFocus();
		mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
				} else {
					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
				}

			}
		});
		mEditTextContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
		});

		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					photo.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					photo.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		 swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);

		 swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
		                 android.R.color.holo_orange_light, android.R.color.holo_red_light);

		 swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

		         @Override
		         public void onRefresh() {
		                 new Handler().postDelayed(new Runnable() {

		                         @Override
		                         public void run() {
		                        	 if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                                         List<EMMessage> messages;
                                         try {
                                                 messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
                                             /*}
                                             else{
                                                 messages = conversation.loadMoreGroupMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
                                             }*/
                                         } catch (Exception e1) {
                                             swipeRefreshLayout.setRefreshing(false);
                                             return;
                                         }
                                         
                                         if (messages.size() > 0) {
                                             adapter.notifyDataSetChanged();
                                             adapter.refreshSeekTo(messages.size() - 1);
                                             if (messages.size() != pagesize){
                                                 haveMoreData = false;
                                             }
                                         } else {
                                             haveMoreData = false;
                                         }
                                         
                                         isloading = false;

                                 }
		                                     Toast.makeText(ChatActivity.this, getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
		                               
		                                 swipeRefreshLayout.setRefreshing(false);
		                         }
		                 }, 1000);
		         }
		 });
	}

	private void setUpView() {
		iv_emoticons_normal.setOnClickListener(this);
		iv_emoticons_checked.setOnClickListener(this);
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
				PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
		chatType = CHATTYPE_SINGLE;

			toChatUsername =  da.contractor.name;
			String tname;
			tname=toChatUsername;
			((TextView) findViewById(R.id.name)).setText(tname);
        
		// for chatroom type, we only init conversation and create view adapter on success
		if(true){
			onConversationInit();
			
	        onListViewCreation();
	        
	        // show forward message if the message is not null
	        String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
	        if (forward_msg_id != null) {
	            forwardMessage(forward_msg_id);
	        }
		}
	}

	protected void onConversationInit(){
	    /*if(chatType == CHATTYPE_SINGLE){*/
	        conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.Chat);    
        conversation.markAllMessagesAsRead();

        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
/*            if (chatType == CHATTYPE_SINGLE) {*/
                conversation.loadMoreMsgFromDB(msgId, pagesize);
            /*} else {
                conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
            }*/
        }
        
        EMChatManager.getInstance().addChatRoomChangeListener(new EMChatRoomChangeListener(){

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if(roomId.equals(toChatUsername)){
                    finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {                
            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                    String participant) {
                
            }

            @Override
            public void onMemberKicked(String roomId, String roomName,
                    String participant) {
                if(roomId.equals(toChatUsername)){
                    String curUser = EMChatManager.getInstance().getCurrentUser();
                    if(curUser.equals(participant)){
                        EMChatManager.getInstance().leaveChatRoom(toChatUsername);
                        finish();
                    }
                }
            }
            
        });
	}
	
	protected void onListViewCreation(){
        adapter = new MessageAdapter(ChatActivity.this, toChatUsername, chatType);
        listView.setAdapter(adapter);
        
        listView.setOnScrollListener(new ListScrollListener());
        adapter.refreshSelectLast();

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
			case RESULT_CODE_COPY: // 婢跺秴鍩楀☉鍫熶紖
				EMMessage copyMsg = ((EMMessage) adapter.getItem(data.getIntExtra("position", -1)));
				// clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
				// ((TextMessageBody) copyMsg.getBody()).getMessage()));
				clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
				break;
			case RESULT_CODE_DELETE: // 閸掔娀娅庡☉鍫熶紖
				EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
				conversation.removeMessage(deleteMsg.getMsgId());
				adapter.refreshSeekTo(data.getIntExtra("position", adapter.getCount()) - 1);
				break;

			/*case RESULT_CODE_FORWARD: // 鏉烆剙褰傚☉鍫熶紖
				EMMessage forwardMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", 0));
				Intent intent = new Intent(this, ForwardMessageActivity.class);
				intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
				startActivity(intent);
				
				break;*/

			default:
				break;
			}
		}
		if (resultCode == RESULT_OK) { 
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				EMChatManager.getInstance().clearConversation(toChatUsername);
				adapter.refresh();
			}   else if (requestCode == REQUEST_CODE_LOCAL) { 
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						sendPicByUri(selectedImage);
					}
				}
			}   else if (requestCode == REQUEST_CODE_TEXT || requestCode == REQUEST_CODE_VOICE
					|| requestCode == REQUEST_CODE_PICTURE || requestCode == REQUEST_CODE_LOCATION
					|| requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_FILE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
				if (!TextUtils.isEmpty(clipboard.getText())) {
					String pasteText = clipboard.getText().toString();
					if (pasteText.startsWith(COPY_IMAGE)) {
						sendPicture(pasteText.replace(COPY_IMAGE, ""));
					}

				}
			} else if (conversation.getMsgCount() > 0) {
				adapter.refresh();
				setResult(RESULT_OK);
			} else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
				adapter.refresh();
			}
		}
	}

	/**
	 * 濞戝牊浼呴崶鐐垼閻愮懓鍤禍瀣╂
	 * 
	 * @param view
	 */
	@Override
	public void onClick(View view) {
		String st1 = getResources().getString(R.string.not_connect_to_server);
		int id = view.getId();
		if (id == R.id.btn_send) {
			String s = mEditTextContent.getText().toString();
			sendText(s);
		} else if (id == R.id.photo) {
			selectPicFromLocal();
		} else if (id == R.id.iv_emoticons_normal) {
			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.GONE);
			emojiIconContainer.setVisibility(View.VISIBLE);
			more.setVisibility(View.VISIBLE);
			hideKeyboard();
		} else if (id == R.id.iv_emoticons_checked) { 
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
			more.setVisibility(View.GONE);
		}  
		} 

	/**
	 * 娴滃娆㈤惄鎴濇儔
	 * 
	 * see {@link EMNotifierEvent}
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
        case EventNewMessage:
        {
   
            EMMessage message = (EMMessage) event.getData();
            
            String username = null;
        
/*            if(message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom){
                username = message.getTo();
            }
            else{*/
        
                username = message.getFrom();

            if(username.equals(getToChatUsername())){
                refreshUIWithNewMessage();
                HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(message);
            }else{

                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
            }

            break;
        }
        case EventDeliveryAck:
        {
            EMMessage message = (EMMessage) event.getData();
            refreshUI();
            break;
        }
        case EventReadAck:
        {
            EMMessage message = (EMMessage) event.getData();
            refreshUI();
            break;
        }
        case EventOfflineMessage:
        {
            //a list of offline messages 
            //List<EMMessage> offlineMessages = (List<EMMessage>) event.getData();
            refreshUI();
            break;
        }
        default:
            break;
        }
        
    }
	
	
	private void refreshUIWithNewMessage(){
	    if(adapter == null){
	        return;
	    }
	    
	    runOnUiThread(new Runnable() {
            public void run() {
                adapter.refreshSelectLast();
            }
        });
	}

	private void refreshUI() {
	    if(adapter == null){
            return;
        }
	    
		runOnUiThread(new Runnable() {
			public void run() {
				adapter.refresh();
			}
		});
	}


	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}


	public void sendText(String content) {

		if (content.length() > 0) {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			// 婵″倹鐏夐弰顖滃參閼卞绱濈拋鍓х枂chattype,姒涙顓婚弰顖氬礋閼憋拷
			TextMessageBody txtBody = new TextMessageBody(content);
			// 鐠佸墽鐤嗗☉鍫熶紖body
			message.addBody(txtBody);
			// 鐠佸墽鐤嗙憰浣稿絺缂佹瑨鐨�,閻€劍鍩泆sername閹存牞锟藉懐鍏㈤懕濂紃oupid
			message.setReceipt(toChatUsername);
			// 閹跺essgage閸旂姴鍩宑onversation娑擄拷
			conversation.addMessage(message);
			// 闁氨鐓dapter閺堝绉烽幁顖氬綁閸旑煉绱漚dapter娴兼碍鐗撮幑顔煎閸忋儳娈戞潻娆愭蒋message閺勫墽銇氬☉鍫熶紖閸滃矁鐨熼悽鈺痙k閻ㄥ嫬褰傞柅浣规煙濞夛拷
			adapter.refreshSelectLast();
			mEditTextContent.setText("");

			setResult(RESULT_OK);

		}
	}


	/*private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
			if (chatType == CHATTYPE_GROUP){
				message.setChatType(ChatType.GroupChat);
				}else if(chatType == CHATTYPE_CHATROOM){
				    message.setChatType(ChatType.ChatRoom);
				}
			message.setReceipt(toChatUsername);
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
			message.addBody(body);
			if(isRobot){
				message.setAttribute("em_robot_message", true);
			}
			conversation.addMessage(message);
			adapter.refreshSelectLast();
			setResult(RESULT_OK);
			// send file
			// sendVoiceSub(filePath, fileName, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/


	private void sendPicture(final String filePath) {
		String to = toChatUsername;
		// create and add image message in view
		final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
		message.setReceipt(to);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		message.addBody(body);
		
		conversation.addMessage(message);

		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		setResult(RESULT_OK);
		// more(more);
	}

	private void sendPicByUri(Uri selectedImage) {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		String st8 = getResources().getString(R.string.cant_find_pictures);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			sendPicture(picturePath);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;

			}
			sendPicture(file.getAbsolutePath());
		}

	}

	/**
	 * 閸欐垿锟戒椒缍呯純顔讳繆閹拷
	 * 
	 * @param latitude
	 * @param longitude
	 * @param imagePath
	 * @param locationAddress
	 */


	/**
	 * 閸欐垿锟戒焦鏋冩禒锟�
	 * 
	 * @param uri
	 */

	private void resendMessage() {
		EMMessage msg = null;
		msg = conversation.getMessage(resendPos);
		// msg.setBackSend(true);
		msg.status = EMMessage.Status.CREATE;

		adapter.refreshSeekTo(resendPos);
	}

/*	public void emptyHistory(View view) {
		String st5 = getResources().getString(R.string.Whether_to_empty_all_chats);
		startActivityForResult(new Intent(this, AlertDialog.class).putExtra("titleIsCancel", true).putExtra("msg", st5)
				.putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
	}*/

	/**
	 * 閻愮懓鍤弬鍥х摟鏉堟挸鍙嗗锟�
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
	}

	private PowerManager.WakeLock wakeLock;

	/**
	 * 閹稿缍囩拠纾嬬樈listener
	 * 
	 */

	private View getGridChildView(int i) {
		View view = View.inflate(this, R.layout.expression_gridview, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
						if (filename != "delete_expression") { 
							Class clz = Class.forName("com.example.tsui.negotiate.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(ChatActivity.this,
									(String) field.get(null)));
						} else { 
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent.getSelectionStart();
								if (selectionStart > 0) {
									String body = mEditTextContent.getText().toString();
									String tempStr = body.substring(0, selectionStart);
									int i = tempStr.lastIndexOf("[");
									if (i != -1) {
										CharSequence cs = tempStr.substring(i, selectionStart);
										if (SmileUtils.containsKey(cs.toString()))
											mEditTextContent.getEditableText().delete(i, selectionStart);
										else
											mEditTextContent.getEditableText().delete(selectionStart - 1,
													selectionStart);
									} else {
										mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
									}
								}
							}

						}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
		return reslist;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		 if(adapter != null){
		     adapter.refresh();
	     }

		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
		sdkHelper.pushActivity(this);
		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage,EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck, EMNotifierEvent.Event.EventReadAck });
	}

	@Override
	protected void onStop() {
		// unregister this event listener when this activity enters the
		// background
		EMChatManager.getInstance().unregisterEventListener(this);

		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();

		sdkHelper.popActivity(this);
		
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (wakeLock.isHeld())
			wakeLock.release();
	}

	
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}


	public void back(View view) {
		EMChatManager.getInstance().unregisterEventListener(this);
		finish();
	}


	@Override
	public void onBackPressed() {
			super.onBackPressed();
	}


	private class ListScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				/*if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData && conversation.getAllMessages().size() != 0) {
					isloading = true;
					loadmorePB.setVisibility(View.VISIBLE);
					List<EMMessage> messages;
					EMMessage firstMsg = conversation.getAllMessages().get(0);
					try {
						if (chatType == CHATTYPE_SINGLE)
							messages = conversation.loadMoreMsgFromDB(firstMsg.getMsgId(), pagesize);
						else
							messages = conversation.loadMoreGroupMsgFromDB(firstMsg.getMsgId(), pagesize);
					} catch (Exception e1) {
						loadmorePB.setVisibility(View.GONE);
						return;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (messages.size() != 0) {
						if (messages.size() > 0) {
							adapter.refreshSeekTo(messages.size() - 1);
						}
						
						if (messages.size() != pagesize)
							haveMoreData = false;
					} else {
						haveMoreData = false;
					}
					loadmorePB.setVisibility(View.GONE);
					isloading = false;

				}*/
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		String username = da.contractor.name;
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	/**
	 * 鏉烆剙褰傚☉鍫熶紖
	 * 
	 * @param forward_msg_id
	 */
	protected void forwardMessage(String forward_msg_id) {
		final EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
		EMMessage.Type type = forward_msg.getType();
		switch (type) {
		case TXT:
			String content = ((TextMessageBody) forward_msg.getBody()).getMessage();
			sendText(content);
			break;
		case IMAGE:
			String filePath = ((ImageMessageBody) forward_msg.getBody()).getLocalUrl();
			if (filePath != null) {
				File file = new File(filePath);
				if (!file.exists()) {
					filePath = ImageUtils.getThumbnailImagePath(filePath);
				}
				sendPicture(filePath);
			}
			break;
		default:
			break;
		}
	}
	

	public String getToChatUsername() {
		return toChatUsername;
	}

	public ListView getListView() {
		return listView;
	}

}
