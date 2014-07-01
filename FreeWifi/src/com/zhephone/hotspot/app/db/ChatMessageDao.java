package com.zhephone.hotspot.app.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.AppContext;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.common.LogUtil;
import com.zhephone.hotspoti.app.bean.ChatGroup;
import com.zhephone.hotspoti.app.bean.ChatGroupList;
import com.zhephone.hotspoti.app.bean.ChatMessage;
import com.zhephone.hotspoti.app.bean.ChatMessageList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * 
 * 类名称：ChatMessageDao 类描述：用于对聊天表做增删查改 
 * 
 */
public class ChatMessageDao {

	// private final static String TAG = "ChatMessageDao";
	private WifiDatabseAccess wifiDb = null;
	public static ChatMessageDao instance;

	private ChatMessageDao() {
		if (wifiDb == null) {
			wifiDb = AppContext.getInstance().getDbAccess();
		}
	};

	public static ChatMessageDao getInstance(Context context) {
		if (instance == null) {
			instance = new ChatMessageDao();
		}
		return instance;
	}

	/**
	 * 保存聊天信息列表事务  
	 * 
	 * @param removeList
	 */
	public void saveChatMessageList(List<ChatMessage> chatMessageList) {
		// 开启事务
		wifiDb.beginTransaction();
		try {
			for (ChatMessage chatMessage : chatMessageList) {
				saveChatMessage(chatMessage);
			}
			// 设置事务标志为成功，当结束事务时就会提交事务
			wifiDb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 结束事务
			wifiDb.endTransaction();
		}
	}
	
	/**
	 *分页查询每个组聊天的最后一条记录
	 */
	public List<ChatGroup> queryGroupChatLastMessageList(final String userId) {
		List<ChatGroup> chatGroupList = new ArrayList<ChatGroup>();
		try {
			String sql = "select send_id,receive_id from " + Constant.ChatTableDef.TABLE_NAME+ ";";
			final List<String> userIdList = new ArrayList<String>();
			userIdList.add(userId);
			wifiDb.queryList(sql, new WifiDatabseAccess.QueryResultHandler<String>(){
				@Override
				public String handle(Cursor cursor, int numOfCols) {
					String sendId = cursor.getString(cursor.getColumnIndex(Constant.ChatTableDef.SEND_ID));
					String receId = cursor.getString(cursor.getColumnIndex(Constant.ChatTableDef.RECEIVE_ID));
					if(StringUtils.isNotEmpty(sendId) && StringUtils.isNotEmpty(receId)) {
						if (userIdList.contains(sendId)) {
							if (!userIdList.contains(receId)) {
								userIdList.add(receId);
							} 
						} else {
							userIdList.add(sendId);
						}
					} else {
						if(StringUtils.isNotEmpty(sendId)) {
							if (!userIdList.contains(sendId)) {
								userIdList.add(sendId);
							} 
						} else if(StringUtils.isNotEmpty(receId)) {
							if (!userIdList.contains(receId)) {
								userIdList.add(receId);
							} 
						}
					}
					return null;
				}
				
			}, new String[]{}); 
			Cursor cursor = null;
			String groudId;
			String groupName;
			String chatTime;
			String message;
			
			for (int i = 0; i < userIdList.size(); i++) {
				groudId = userIdList.get(i);
				sql = "select  receive_id,send_id,content, chat_time, group_name from chat_t where (send_id = ?  and receive_id = ? " +
						"or send_id = ?  and receive_id = ? ) ORDER BY _ID DESC LIMIT 1 ;";
				cursor = wifiDb.getReadableDatabase().rawQuery(sql, new String[]{userId,groudId,groudId,userId});
				ChatGroup chatGroup;
				if (cursor.moveToFirst()) {
					chatGroup = new ChatGroup();
					message = cursor.getString(2);
					if (message != null)
						chatGroup.setContent(message);
					chatTime = cursor.getString(3);
					if (chatTime != null)
						chatGroup.setLastTime(chatTime);
					groupName = cursor.getString(4);
					if (groupName != null)
						chatGroup.setChatName(groupName);
					//if(!groudId.equals(userId))
					chatGroup.setDeviceToken(groudId);
					chatGroupList.add(chatGroup);
				}
			}
			cursor.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chatGroupList;
	}


	/**
	 * 保存聊天信息
	 * 
	 * @param chatMessage
	 */
	public void saveChatMessage(ChatMessage chatMessage) {
		try {
			ContentValues cv = new ContentValues();
			cv.put(Constant.ChatTableDef.SEND_ID, chatMessage.getSendId());
			cv.put(Constant.ChatTableDef.RECEIVE_ID, chatMessage.getReceiveId());
			cv.put(Constant.ChatTableDef.CONTENT, chatMessage.getContent());
			cv.put(Constant.ChatTableDef.CHAT_TIME, chatMessage.getTime());
			cv.put(Constant.ChatTableDef.GROUP_NAME, chatMessage.getName());	
			cv.put(Constant.ChatTableDef.USER_ID, chatMessage.getUserId());
			cv.put(Constant.ChatTableDef.MY_SEND, chatMessage.getIsMySend());
			cv.put(Constant.ChatTableDef.READ_STATUS, chatMessage.getIsRead());
			if(StringUtils.isNotEmpty(chatMessage.getUserIconUrl())) {
				cv.put(Constant.ChatTableDef.USER_HEAD, chatMessage.getUserIconUrl());
			}
			wifiDb.insert(Constant.ChatTableDef.TABLE_NAME, cv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新组的读状态,
	 * @param groupId 组的id
	 * @param readStatus 读的状态 0是未读,1是已读
	 */
	public void updateGroupReadStatu(String sendId,String receiveId,int readStatus) {
		try {
			ContentValues cv = new ContentValues();
			cv.put(Constant.ChatTableDef.READ_STATUS, readStatus);
			String whereSql = "(send_id = ? " + " and receive_id = ? "
				+ " or send_id = ? " + " and receive_id = ? " + ") ";
			wifiDb.update(Constant.ChatTableDef.TABLE_NAME, cv, whereSql, new String[] { sendId, receiveId, receiveId, sendId });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	/**
	 * 检查组有没有未读的消息
	 * @param sendId
	 * @param receiveId  
	 */
	public int queryGroupHasNoReadMessage(String sendId,String receiveId) {
		int readStatus = 1;
		try {
			String whereSql = " where (send_id = ? " + " and receive_id = ? "
					+ " or send_id = ? " + " and receive_id = ? " + ") and read_status = ? ";
			String sql = "select send_id,receive_id,read_status from " + Constant.ChatTableDef.TABLE_NAME +
					whereSql + ";";
			String[] results = wifiDb.queryStrings(sql,  new String[] {sendId,receiveId,receiveId,sendId,"0"});
			
			if(results != null && results.length >0) {
				/**
				 * 有未读消息
				 */
				readStatus = 0;
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return readStatus;

	}
	
	/**
	 * 检查有没有未读的消息
	 * @param sendId
	 * @param receiveId  
	 */
	public int queryHasNoReadMessage() {
		int readStatus = 1;
		try {
			String whereSql = " where read_status = ? ";
			String sql = "select send_id,receive_id,read_status from " + Constant.ChatTableDef.TABLE_NAME +
					whereSql + ";";
			String[] results = wifiDb.queryStrings(sql,  new String[] {"0"});
			
			if(results != null && results.length >0) {
				/**
				 * 有未读消息
				 */
				readStatus = 0;
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return readStatus;

	}

	/**
	 * 查询两个人聊天的所有信息
	 * 
	 * @return
	 */
	public List<ChatMessage> queryTwoPeopleChatMessageList(final String sendId,
			String receiveId) {
		String sql = "select * from " + Constant.ChatTableDef.TABLE_NAME
				+ " where " + "(send_id = ? " + " and receive_id = ? "
				+ " or send_id = ? " + " and receive_id = ? " + ") ORDER BY _ID DESC;";
		List<ChatMessage> chatMessages = wifiDb.queryList(sql,
				new WifiDatabseAccess.QueryResultHandler<ChatMessage>() {
					public ChatMessage handle(Cursor cursor, int numOfCols) {
						ChatMessage chatMessage = new ChatMessage();
						/**
						 * 判断是不是自己发送的聊天
						 */
						String sendDeviceId = cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.SEND_ID));
						String mySend = cursor.getString(cursor.getColumnIndex(Constant.ChatTableDef.MY_SEND));
						/**
						 * 如果为0说明是自己发送的
						 * 否则是其他人发送的
						 */
						chatMessage.setIsMySend(mySend);
						chatMessage.setContent(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.CONTENT)));
						chatMessage.setSendId(sendDeviceId);
						chatMessage.setReceiveId(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.RECEIVE_ID)));
						chatMessage.setTime(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.CHAT_TIME)));
						return chatMessage;
					}

				}, new String[] { sendId, receiveId, receiveId, sendId });
		ChatMessageList chatMessageList = new ChatMessageList();
		Cursor c = wifiDb.getReadableDatabase().rawQuery(
				"select count(*) from " + Constant.ChatTableDef.TABLE_NAME,
				null);
		if (c.moveToFirst()) {
			int count = (int) c.getLong(0);
			chatMessageList.setTotalCount(count);
		}
		if (c != null) {
			c.close();
			c = null;
		}
		chatMessageList.setMessages(chatMessages);
		return chatMessages;
	}

	/**
	 * 分页查询两个人聊天的所有信息
	 * 
	 * @return
	 */
	public ChatMessageList queryTwoPeopleChatMessageListByPage(
			final String sendId, String receiveId, int curPage, int pageSize) {
		int start = curPage * pageSize;
		/**
		 * offset代表从第几条记录“之后“开始查询，limit表明查询多少条结果
		 */
		String sql = "select * from " + Constant.ChatTableDef.TABLE_NAME
				+ " where " + "(send_id = ? " + " and receive_id = ? "
				+ " or send_id = ? " + " and receive_id = ? " + ") "
				+ " ORDER BY _ID DESC  limit ? offset ? " + ";";
//		String sql = "select * from " + Constant.ChatTableDef.TABLE_NAME
//				+ " where " + "(send_id =\'"+ sendId+ "\'"+" and receive_id =\'"+receiveId+"\'"
//				+ " or send_id =\'" + receiveId+"\'"+" and receive_id =\'"+sendId+"\'" + ")"
//				+ " limit "+ pageSize+" offset " + firstResult+ ";";
		final String userId = AppConfig.getInstance().getUserInfo().getUserId();
		List<ChatMessage> chatMessages = wifiDb.queryList(sql,
				new WifiDatabseAccess.QueryResultHandler<ChatMessage>() {
					public ChatMessage handle(Cursor cursor, int numOfCols) {
						ChatMessage chatMessage = new ChatMessage();
						/**
						 * 判断是不是自己发送的聊天
						 */
						String sendDeviceId = cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.SEND_ID));
						
						String isMySend = cursor.getString(cursor.getColumnIndex(Constant.ChatTableDef.MY_SEND));
						/**
						 * 如果为0说明是自己发送的
						 * 否则是其他人发送的
						 */
						chatMessage.setIsMySend(isMySend);
						chatMessage.setContent(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.CONTENT)));
						chatMessage.setSendId(sendDeviceId);
						chatMessage.setReceiveId(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.RECEIVE_ID)));
						chatMessage.setName(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.GROUP_NAME)));
						chatMessage.setTime(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.CHAT_TIME)));
						String userHead = cursor.getString(cursor.getColumnIndex(Constant.ChatTableDef.USER_HEAD));
						if(StringUtils.isNotEmpty(userHead))  {
							chatMessage.setUserIconUrl(userHead);
						}
						return chatMessage;
					}

				}, new String[] {sendId,receiveId,receiveId,sendId,pageSize+"",start+""});
		ChatMessageList chatMessageList = new ChatMessageList();
		chatMessageList.setTotalCount(getTwoPeopleCount(sendId,receiveId));
		/**
		 * 对聊天列表进行倒序排序
		 */
		Collections.reverse(chatMessages);
		chatMessageList.setMessages(chatMessages);
		return chatMessageList;
	}
	
	/**
	 * 查询组的个数
	 * 
	 * @return
	 */
	public int getGroupCount(String userId) {
		String sql = "select count(*) from " + Constant.ChatTableDef.TABLE_NAME
				+ " GROUP BY group_name HAVING " + "(send_id = ? or receive_id = ? );";
		Cursor c = wifiDb.getReadableDatabase().rawQuery(sql,new String[]{userId,userId});
		int count=0;
		if (c.moveToFirst()) {
			count = (int) c.getLong(0);
		}
		c.close();
		return count;
	}
	
	/**
	 * 查询双方聊天总记录个数
	 * 
	 * @return
	 */
	public int getTwoPeopleCount(String sendId, String receiveId) {
		String sql = "select count(*) from " + Constant.ChatTableDef.TABLE_NAME
				+ " where " + "(send_id =\'"+ sendId+ "\'"+" and receive_id =\'"+receiveId+"\'"
				+ " or send_id =\'" + receiveId+"\'"+" and receive_id =\'"+sendId+"\'" + ")"
				+";";
		Cursor c = wifiDb.getReadableDatabase().rawQuery(sql,null);
		int count=0;
		if (c.moveToFirst()) {
			count = (int) c.getLong(0);
		}
		c.close();
		return count;
	}

	/**
	 * 查询所有人聊天的信息
	 * 
	 * @return
	 */
	public List<ChatMessage> getChatMessageList() {
		String sql = "select * from " + Constant.ChatTableDef.TABLE_NAME;
		List<ChatMessage> appShortcutIconList = wifiDb.queryList(sql,
				new WifiDatabseAccess.QueryResultHandler<ChatMessage>() {
					public ChatMessage handle(Cursor cursor, int numOfCols) {
						ChatMessage chatMessage = new ChatMessage();
						chatMessage.setContent(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.CONTENT)));
						chatMessage.setSendId(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.SEND_ID)));
						chatMessage.setReceiveId(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.RECEIVE_ID)));
						chatMessage.setTime(cursor.getString(cursor
								.getColumnIndex(Constant.ChatTableDef.CHAT_TIME)));
						return chatMessage;
					}

				}, new String[] {});
		return appShortcutIconList;
	}
}
