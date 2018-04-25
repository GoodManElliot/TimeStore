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

public class Constant {
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String CHAT_ROOM = "item_chatroom";
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
	public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
	public static final String ACCOUNT_REMOVED = "account_removed";
	public static final String CHAT_ROBOT = "item_robots";
	public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
	public static final int USER = 1;
	public static final int SYSTEM = 2;
	
	//type
	public static final String GEO="geo";
	public static final String REQUEST="request";//这个是每次帮助请求
	public static final String USER_DATA_REQUEST="user_data_request";//这个是初始化数据请求
	public static final String DEAL_STATE="deal_state";//订单是否成功的状态
	public static final String START_POSITION="sposition";
	public static final String END_POSITION="eposition";
	public static final String RESPONSE="response";
	public static final String PROGRESS="progress";
			
	//content key
	public static final String USERNAME="username";
	public static final String PWD="pwd";
	public static final String SEX="sex";
	public static final String LOT="lot";
	public static final String LAT="lat";
	public static final String TRANSACTION_TYPE="transaction_type";
	public static final String TRANSACTION="transaction";
	public static final String MONEY="money";
	public static final String IS_SUCCESS="is_success";
	public static final String ROLE="role";
	public static final String RATING="rating";
	public static final String DEAL_ID="deal_id";
	public static final String DEALLIST="deal_list"; 
	public static final String AGREE="agree";
	public static final String COMPLISH="complish";
	//public static final String PHONE="phone";
	
	public static final int BUSY=0;
	public static final int IDLE=1;
	public static final int NOSTATE=-1;
}
