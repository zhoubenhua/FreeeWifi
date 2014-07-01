package com.zhephone.hotspot.app;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.db.ChatMessageDao;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.ui.chat.GroupChatActivity;
import com.zhephone.hotspot.app.ui.chat.GroupListActivity;
import com.zhephone.hotspot.app.ui.user.LoginActivity;
import com.zhephone.hotspoti.app.bean.ChatGroupList;
import com.zhephone.hotspoti.app.bean.ChatMessage;
import com.zhephone.hotspoti.app.bean.ChatMessageList;
import com.zhephone.hotspoti.app.bean.ChatNotification;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * 接受别人发送的聊天
 */
public class PushReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		String data;
		ChatNotification chatNotification;
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
    		data = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
    		chatNotification = JSON.parseObject(data, ChatNotification.class);
			boolean isAppBackground = true;  //app是否在后台
			/**
			 * 判断程序是否在前台
			 */
			if(!CommonUtil.isApplicationBroughtToBackground(context)) {	
				//程序在前台
				isAppBackground = false;				
			}			
			if(isAppBackground) {
				
				/**
				 * 程序在后台,发送通知
				 */
				 int notiId = Integer.valueOf(chatNotification.getId());
				 Intent mIntent = new Intent(Constant.ChatIntentDef.OPEN_CHAT_NOTIFICATION_ACTION);
				 mIntent.putExtra(Constant.ChatIntentDef.RECEIVER_NOTIFICATION, data);
				 PendingIntent pendingIntent = PendingIntent.getBroadcast(context,  notiId, 
						 mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				 NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
				 builder.setContentIntent(pendingIntent);
				 builder.setContentTitle(chatNotification.getName());
				 builder.setDefaults(Notification.DEFAULT_VIBRATE);
				 builder.setSmallIcon(R.drawable.app_icon);
				 builder.setAutoCancel(true);	
				 NotificationManager notifiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				 notifiManager.notify(notiId, builder.build());
			}
			loadNoReadChatMessageList(chatNotification.getImei(),chatNotification.getSendNo(),context,isAppBackground);
        }
        else if (Constant.ChatIntentDef.OPEN_CHAT_NOTIFICATION_ACTION.equals(intent.getAction())) {
        	/**
        	 * 点击通知栏的消息打开聊天列表
        	 */
        	data = intent.getStringExtra(Constant.ChatIntentDef.RECEIVER_NOTIFICATION);
        	chatNotification = JSON.parseObject(data, ChatNotification.class);
        	Intent mIntent;
        	//if(AppConfig.getInstance().isLogin()) {
        		mIntent = new Intent(context, GroupChatActivity.class);
        		mIntent.putExtra(Constant.ChatIntentDef.GROUP_NAME, chatNotification.getName());
        		mIntent.putExtra(Constant.ChatIntentDef.GROUP_CHAT_ID, chatNotification.getImei());
        		mIntent.putExtra(Constant.ChatIntentDef.CHAT_SOURCE, Constant.ChatIntentDef.GROUP_LIST);
//        	} else {
//        		mIntent = new Intent(context, LoginActivity.class);
//        	}
        	mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mIntent);			
  			  //doOpenNotificationMessage(context,extras);
        	
        } 
	}
	
	 /**
		 * 加载未读群组聊天信息
		 */
		private void loadNoReadChatMessageList(String groupId,String sendNo,final Context context,final boolean isAppBackground) {
			if (CommonUtil.checkNetwork(context)) {
				RequestParams paramMap = new RequestParams();
				final String userDevice = AppConfig.getInstance().getDeviceToken();
				paramMap.put("udid", userDevice);
				paramMap.put("imei", groupId);
				final ChatMessageDao chatMessageDao = ChatMessageDao.getInstance(context);
				paramMap.put("sendNo", sendNo);
				final APIAsyncTask api = new APIAsyncTask();			
				api.get(Protocol.NO_READ_CHAT_LIST,paramMap, new CallbackListener() {
					public void onSuccess(String data) {
						if (data != null) {
							ChatMessageList result = JSON.parseObject(data, ChatMessageList.class);
							/**
							 * 判断有没有未读聊天信息,如果有将未读聊天数据保存到本地数据库
							 */
							ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
					        RunningTaskInfo info = manager.getRunningTasks(1).get(0);
					        String className = info.topActivity.getClassName();
					        boolean isGroupChatActivity = false; 
					        /**
					         * 检查当前是否是在群聊界面
					         */
					        if(!isAppBackground) {
					        	if(className.equals("com.zhephone.hotspot.app.ui.chat.GroupChatActivity")) {
					        		isGroupChatActivity = true;
					        	}
					        }
							if(result.getUnreadMessages() != null && result.getUnreadMessages().size() >0) {
								for(ChatMessage message:result.getUnreadMessages()) {
									message.setSendId(message.getImei());
									message.setReceiveId(userDevice);		
									if(isGroupChatActivity) {
										/**
										 * 在群聊界面 将未读状态改为已读
										 */
										message.setIsRead(1);
									}
									chatMessageDao.saveChatMessage(message);
								}	
							};
							/**
							 * 判断应用是否在前台,如果在前台就发送广播
							 */
							if(!isAppBackground) {
								
								Intent mIntent = new Intent();		
								mIntent.setAction(Constant.ChatIntentDef.RECEIVE_CHAT_ACTION);				
								mIntent.putExtra(Constant.ChatIntentDef.CHAT_JSON, data);
								context.sendBroadcast(mIntent); 
								mIntent = new Intent();		
								mIntent.setAction(Constant.ChatIntentDef.RECEIVE_NEW_MSG_ACTION);				
								context.sendBroadcast(mIntent); 
							}
 
						}
					}

					public void onError(String error) {
						
					}
				});
				api.execute();
			}
		}

}
