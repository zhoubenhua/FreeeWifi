
package com.zhephone.hotspot.app.common;

public final class Constant {
	
	
	public static final class SharedPreferencesKeyDef {
		public static final String SHARED_PREFERENCES_USER_NICKNAME= "pref_nickname";
		public static final String SHARED_PREFERENCES_USER_SEX = "pref_sex";
		public static final String SHARED_PREFERENCES_USER_LOGIN_PASSWORD = "pref_password";
		public static final String SHARED_PREFERENCES_USER_ID = "pref_user_id";
		public static final String SHARED_PREFERENCES_USER_HEAD = "pref_user_head";
		public static final String SHARED_PREFERENCES_SECRET = "pref_user_secret";
		public static final String SHARED_PREFERENCES_FIRST = "pref_first";
		public static final String SHARED_PREFERENCES_USER_NAME = "pref_user_name";
		public static final String SHARED_PREFERENCES_MOBILE = "pref_user_mobile";
		public static final String SHARED_PREFERENCES_LOGIN_STATU = "pref_login_statu";
	}
	
	/**
	 * 聊天传递参数
	 * @author Administrator
	 *
	 */
	public static final class ChatIntentDef {
		public static final String GROUP_NAME = "group_name";
		public static final String RECEIVER_NOTIFICATION = "receiver_notification";
		public static final String GROUP_CHAT_ID= "group_chat_id";
		public static final String SEND_CHAT_ID= "send_chat_id";
		public static final String RECEIVE_CHAT_ID = "receive_chat_id";	
		public static final String RECEIVE_CHAT_CONTENT = "receive_chat_content";
		public static final String RECEIVE_CHAT_ACTION = "receive_chat_action";	
		public static final String RECEIVE_NEW_MSG_ACTION = "receive_new_msg_action";	
		public static final String RECEIVE_INTO_GROUP_ACTION = "receive_into_group_action";	
		public static final String CHAT_SOURCE = "chat_source";
		public static final int MAP_LIST = 1001;			
		public static final int GROUP_LIST = 1002;		
		public static final String CHAT_JSON = "chat_json";				
		public static final String CHAT_NOTIFICATION = "chat_notification";
		public static final String OPEN_CHAT_NOTIFICATION_ACTION = "open_chat_notification_action";
	}
	
	/**
	 * wifi传递参数
	 * @author Administrator
	 *
	 */
	public static final class WifiIntentDef {		
		public static final String WIFI_CONNECTION_RESULT = "com.zhephone.hotspot.wifi.connection.result";
		public static final String WIFI_HOTSPOT= "wifi_hotspot";			
		public static final String EXTRA_WIFI_CONNECTION_RESULT = "wifi_connection_result";
		public static final int WIFI_CONNECT_FAILED  = 0;
		public static final int WIFI_CONNECT_SUCCESS = 1;
		public static final String WIFI_STATE_CHANGE_ACTION = "com.zhephone.hotspot.wifi.state.changed";
	}	
	
	/**
	 * 用户传递参数
	 * @author Administrator
	 *
	 */
	public static final class UserIntentDef {		
		public static final String USER_NAME = "user_name";
		public static final String PASSWORD = "password";			
	}	
	
	/**
	 * 热点传递参数
	 * @author Administrator
	 *
	 */
	public static final class HotIntentDef {
		public static final String GPS_LAT= "gps_lat";
		public static final String GPS_LON = "gps_lon";	
	}
	
	/**
	 * 网页传递参数
	 * @author Administrator
	 *
	 */
	public static final class WebIntentDef {
		public static final String WEB_URL= "web_url";
	}
	
	/**
	 * 网页传递参数
	 * @author Administrator
	 *
	 */
	public static final class VideoIntentDef {
		public static final String VIDEO_NAME = "video_name";
		public static final String VIDEO_URL= "video_url";
	}
	
	public static final String DATABASE_NAME = "wifi.db";
	public static final int DATABASE_VERSION = 1;
	public static final class ChatTableDef {
		public static final String _ID = "_id";
		public static final String TABLE_NAME= "chat_t";
		public static final String SEND_ID = "send_id";
		public static final String CONTENT = "content";
		public static final String CHAT_TIME = "chat_time";
		public static final String RECEIVE_ID = "receive_id";
		public static final String GROUP_NAME = "group_name";
		public static final String USER_ID = "user_id";
		public static final String MY_SEND = "my_send";
		public static final String USER_HEAD = "user_head";
		public static final String OID = "oid";
		public static final String READ_STATUS = "read_status";

		public static final String[] TABLE_SCHEMA = new String[] {
				_ID + " INTEGER PRIMARY KEY AUTOINCREMENT", SEND_ID + " TEXT",
				CONTENT + " TEXT", RECEIVE_ID + " TEXT",CHAT_TIME + " TEXT",
				OID + " TEXT",			
				GROUP_NAME + " TEXT",
				USER_ID + " TEXT",			
				USER_HEAD + " TEXT",						
				MY_SEND + " TEXT",
				READ_STATUS +" INTEGER"
				};			
	}

}

























