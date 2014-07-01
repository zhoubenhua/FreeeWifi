package com.zhephone.hotspot.app.ui.chat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import cn.jpush.android.api.JPushInterface;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.AppContext;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.adapter.ChatListAdapter;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.SharedPreferencesUtil;
import com.zhephone.hotspot.app.common.TimeUtil;
import com.zhephone.hotspot.app.db.ChatMessageDao;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.widget.ChatListView;
import com.zhephone.hotspot.app.widget.ChatListView.ChatListViewListener;
import com.zhephone.hotspot.app.widget.Topbar;
import com.zhephone.hotspoti.app.bean.ChatGroup;
import com.zhephone.hotspoti.app.bean.ChatGroupList;
import com.zhephone.hotspoti.app.bean.ChatMessage;
import com.zhephone.hotspoti.app.bean.ChatMessageList;
import com.alibaba.fastjson.JSON;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
/**
 * 群组聊天
 * @author Administrator
 *
 */
public class GroupChatActivity extends BaseActivity{
	private EditText chatContentEt;
	private ChatListView messageLv;
	private ChatListAdapter chatMessageListAdapter;
	public  ArrayList<ChatMessage> chatMessageList;
	private ChatMessageDao chatMessageDao;
	private ReceiveChatReceiver receiveChatReceiver;
	private int start;
	private int cutPage;
	private int totalPage;
	private int pageSize;
	private Context context;
	//private String userId;
	private String userHeadUrl;
	private Button backBt;
	private String receiveGroupId;
	private String groupName;
	private String secret;
	private TextView titleTv;
	private int LOAD_STATU;
	private int chatSource;
	private static final int NORMAL_LOAD_CHAT_LIST = 1;  //加载聊天
	private static final int LOAD_MORE_CHAT_LIST = 3;  //加载更多聊天
	private InputMethodManager imm;
	private String userDevice;
	private String userName;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.group_chat);
		this.setUpViews();
	}
	

	ChatListViewListener queryChatListListListener = new ChatListViewListener() {
		public void onLoadMore() {
			if ((cutPage >= totalPage) && (totalPage != 0)) {
				//CommonUtil.sendToast(context, "已经是最后一页");
	    		messageLv.stopRefresh();
			} else {
				/**
				 * 将加载群组聊天线程直接加入队列
				 */
				LOAD_STATU = LOAD_MORE_CHAT_LIST;
				mHandler.post(loadChatListRunnable);
			}
		}

	};

	@Override
	protected void initViews() {
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		topbar.getRightBt().setVisibility(View.GONE);
		titleTv = topbar.getTitleTv();
		chatContentEt = (EditText)this.findViewById(R.id.message_et);
		messageLv = (ChatListView)this.findViewById(R.id.message_lv);
		//keyboardRl = (KeyboardRelativeLayout)findViewById(R.id.keyboard_rl);
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiveChatReceiver);
	}
	
	
	OnEditorActionListener sendChatListener = new OnEditorActionListener() {
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEND) {
				String content = chatContentEt.getText().toString();
				if(content.equals("")) {
					CommonUtil.sendToast(getApplicationContext(), "聊天内容不能为空");
				} else {
					sendChat(content);
				}
		        return true;
		    }
		    return false;
		}
	};

	@Override
	protected void onResume() {
		super.onResume(); 						
		receiveChatReceiver = new ReceiveChatReceiver();   			
		IntentFilter receiveChatFilter = new IntentFilter();																		  	
		receiveChatFilter.addAction(Constant.ChatIntentDef.RECEIVE_CHAT_ACTION);		
		registerReceiver(receiveChatReceiver, receiveChatFilter); 	
	}



	private class ReceiveChatReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			/**
			 * 如果有新的消息,就去请求服务器加载未读群组信息
			 */		
			String data = intent.getStringExtra(Constant.ChatIntentDef.CHAT_JSON);
			receiveNoReadMessageList(data);
			//ChatNotification chatNotification = (ChatNotification) intent.getSerializableExtra(Constant.ChatIntentDef.CHAT_NOTIFICATION);
//			if(chatNotification != null) {
//				/**
//				 * 判断是不是自己发送的,如果是自己发送的就不去请求服务器获取聊天信息
//				 */
//				//if(!chatNotification.getSendUdid().equals(userDevice)) {
//					//loadNoReadChatMessageList(chatNotification.getImei(),chatNotification.getSendNo());
//				//}
//			}
		}
	}
	
	private OnTouchListener showKeyboardListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			imm.showSoftInput(chatContentEt, 0);
			return false;
		}
	};
	
	private OnTouchListener closeKeyboardListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			imm.hideSoftInputFromWindow(chatContentEt.getWindowToken(), 0);
			//messageLv.scrollTo(0, scrollY);  

			return false;
		}
	};


	@Override
	protected void initData() {
		context = this.getApplicationContext();
		userHeadUrl = AppConfig.getInstance().getUserInfo().getUserIconUrl();
		userDevice = AppConfig.getInstance().getDeviceToken();
		secret = AppConfig.getInstance().getUserInfo().getSecret();
		chatSource = this.getIntent().getIntExtra(Constant.ChatIntentDef.CHAT_SOURCE,Constant.ChatIntentDef.MAP_LIST);
		receiveGroupId = this.getIntent().getStringExtra(Constant.ChatIntentDef.GROUP_CHAT_ID);
    	chatMessageDao = ChatMessageDao.getInstance(getApplicationContext());
    	groupName = this.getIntent().getStringExtra(Constant.ChatIntentDef.GROUP_NAME);
    	titleTv.setText(groupName);
    	start = 0;
    	cutPage = 0;
    	totalPage = 0;
    	pageSize = 10;
    	chatMessageList = new ArrayList<ChatMessage>(5);
    	userName = SharedPreferencesUtil.getSetting(context, Constant.ChatIntentDef.GROUP_NAME);
		chatMessageListAdapter = new ChatListAdapter(GroupChatActivity.this,chatMessageList);
    	if(StringUtils.isNotEmpty(userName)) {
			chatMessageListAdapter.setUserName(userName);
		}
		messageLv.setAdapter(chatMessageListAdapter);
		chatMessageDao.updateGroupReadStatu(userDevice, receiveGroupId, 1);
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		updateGroupId();
    	LOAD_STATU = NORMAL_LOAD_CHAT_LIST;
		mHandler.post(loadChatListRunnable);
		if (GroupListActivity.groupListHandler != null) {
			Message message = Message.obtain(GroupListActivity.groupListHandler,
					GroupListHandler.FRESH_GROUP_LIST);
			message.sendToTarget();
		}	
	}
	
	private void loadAllGroupByUserIdSucess(String data,String userDevice) {
		ChatGroupList chatGroupList = JSON.parseObject(data, ChatGroupList.class);
		List<ChatGroup> channels = chatGroupList.getChannels();
		if(channels != null && channels.size()>0) {
//			Set<String>  groupDevices = new TreeSet<String>();
//			for(ChatGroup chatGroup : channels) {
//				if(StringUtils.isNotEmpty(chatGroup.getImei())) {
//					groupDevices.add(chatGroup.getImei());
//				}
//			}
			JPushInterface.setAliasAndTags(getApplicationContext(), userDevice, null, null);
		} 
		dissmissProgressDialog();
	}
	
	private void loadAllGroupByUserIdFailed(String error) {
		CommonUtil.sendToast(context, error);
		dissmissProgressDialog();
	}
	
	/**
	 * 获取用户所在的所有的组
	 * @param uid
	 */
	private void loadAllGroupByUserId() {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			RequestParams paramMap = new RequestParams();
			paramMap.put("udid", userDevice);
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.ALL_GROUP_BY_USERID,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						loadAllGroupByUserIdSucess(data,userDevice);
					}
				}

				public void onError(String error) {
					loadAllGroupByUserIdFailed(error);
				}
			});
			api.execute();
		}
	}
	
	/**
	 * 请求服务器更新群组id
	 */
	private void updateGroupId() {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			RequestParams paramMap = new RequestParams();
			paramMap.put("imei", receiveGroupId);
			paramMap.put("udid", userDevice);
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.UPDATE_GROUP_ID,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if(StringUtils.isEmpty(userName)) {
						try {
							JSONObject json = new JSONObject(data);
							if(json.has("name")) {
								userName = (String) json.get("name");
								chatMessageListAdapter.setUserName(userName);
								SharedPreferencesUtil.setSetting(AppContext.getInstance(), 
										Constant.ChatIntentDef.GROUP_NAME,userName);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					loadAllGroupByUserId();
				}

				public void onError(String error) {
					loadAllGroupByUserId();
					
				}
			});
			api.execute();
		}
	}				
	
	private void receiveNoReadMessageList(String data) {
		ChatMessageList result = JSON.parseObject(data, ChatMessageList.class);
		/**
		 * 判断有没有未读聊天信息,如果有将未读聊天数据保存到本地数据库
		 */
		List<ChatMessage> messageList = new ArrayList<ChatMessage>();
		if(result.getUnreadMessages() != null && result.getUnreadMessages().size() >0) {
			for(ChatMessage message:result.getUnreadMessages()) {
				message.setSendId(message.getImei());
				message.setReceiveId(userDevice);			
				/**
				 * 判断这个消息是不是这个群组
				 */
				if(message.getImei().equals(receiveGroupId)) {
					messageList.add(message);
				}
			}	
			if(chatSource == Constant.ChatIntentDef.MAP_LIST) {
				int count = chatMessageDao.getTwoPeopleCount(userDevice, receiveGroupId);
	    		start = (start + pageSize);
	    		cutPage = cutPage + 1;
	    		totalPage = (count % pageSize == 0) ? count / pageSize
	    				: (count / pageSize + 1);
			}
			chatMessageList.addAll(messageList);
    		messageLv.stopRefresh();
    		chatMessageListAdapter.notifyDataSetChanged();
    		if(chatMessageList.size()>0) {
        		messageLv.setSelection(chatMessageList.size()-1);
    		}
    		
		};
	}
	
	
//	private void loadNoReadChatMessageListFailed(String error) {
//		dissmissProgressDialog();
//		CommonUtil.sendToast(getApplicationContext(), error);
//	}
//	
//	/**
//	 * 加载未读群组聊天信息
//	 */
//	private void loadNoReadChatMessageList(String groupId,String sendNo) {
//		if (CommonUtil.checkNetwork(getApplicationContext())) {
//			RequestParams paramMap = new RequestParams();
//			//paramMap.put("token", receiveGroupId);
//			paramMap.put("udid", userDevice);
//			paramMap.put("imei", groupId);
//			paramMap.put("sendNo", sendNo);
//			final APIAsyncTask api = new APIAsyncTask();			
//			api.get(Protocol.NO_READ_CHAT_LIST,paramMap, new CallbackListener() {
//				public void onSuccess(String data) {
//					if (data != null) {
//						loadNoReadChatMessageListSucess(data);
//					}
//				}
//
//				public void onError(String error) {
//					loadNoReadChatMessageListFailed(error);
//				}
//			});
//			api.execute();
//		}
//	}
	

	@Override
	protected void addListener() {
		chatContentEt.setOnTouchListener(showKeyboardListener);
		messageLv.setOnTouchListener(closeKeyboardListener);
		chatContentEt.setOnEditorActionListener(sendChatListener);
		messageLv.setXListViewListener(queryChatListListListener);
		backBt.setOnClickListener(backListener);
	}
	
	private OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			back();
		}
	};
	
	
	private void sendChat(String content) {
		ChatMessage chatMessage;
		chatContentEt.setText("");
		chatMessage = new ChatMessage();
		chatMessage.setSendId(userDevice);
		chatMessage.setReceiveId(receiveGroupId);
		chatMessage.setContent(content);
		chatMessage.setTime(TimeUtil.getCurrentTime()+"");
		chatMessage.setIsMySend("0");									
		chatMessage.setUserId(userDevice);
		chatMessage.setName(userName);
		chatMessage.setIsRead(1);
		if(StringUtils.isNotEmpty(userHeadUrl)) {
			chatMessage.setUserIconUrl(userHeadUrl);
		}
		chatMessageList.add(chatMessage);
		chatMessageDao.saveChatMessage(chatMessage);
		/**
		 * 判断是不是第一次群聊,如果是就要判断分页,避免下拉出现重复
		 */
		if(chatMessageList.size() == 1) {
			int count = 1;
			start = (start + pageSize);
    		cutPage = cutPage + 1;
    		totalPage = (count % pageSize == 0) ? count / pageSize
    				: (count / pageSize + 1);
		}
		chatMessageListAdapter.notifyDataSetChanged();
		messageLv.setSelection(chatMessageList.size()-1);
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			RequestParams paramMap = new RequestParams();
			paramMap.put("imei", receiveGroupId);
			paramMap.put("udid", userDevice);	
			paramMap.put("content",  content);
			final APIAsyncTask api = new APIAsyncTask();
			api.get(Protocol.SEND_MESSAGE, paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						dissmissProgressDialog();
					}		
				}

				public void onError(String error) {
					dissmissProgressDialog();
					CommonUtil.sendToast(getApplicationContext(), error);
				}
			});
			api.execute();
		}
	}
	
	Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	ChatMessageList results = (ChatMessageList) msg.obj;
        	int count = results.getTotalCount();
        	if(results.getMessages() != null && results.getMessages().size()>0) {
        		start = (start + pageSize);
        		cutPage = cutPage + 1;
        		totalPage = (count % pageSize == 0) ? count / pageSize
        				: (count / pageSize + 1);
        	}
    		messageLv.stopRefresh();
        	switch(msg.what) {
        	case NORMAL_LOAD_CHAT_LIST:
        		chatMessageList.clear();
        		chatMessageList.addAll(results.getMessages());
        		if(results.getMessages().size() >1)
        			messageLv.setSelection(chatMessageList.size()-1);
        		break;
        	case LOAD_MORE_CHAT_LIST:
        		chatMessageList.addAll(0,results.getMessages());
        		messageLv.setSelection(results.getMessages().size());
        		break;
        	}
        	if ((cutPage >= totalPage) && (totalPage != 0)) {
    			//CommonUtil.sendToast(context, "已经是最后一页");
    			messageLv.loadFinished();
    		}
    		chatMessageListAdapter.notifyDataSetInvalidated();
//    		if(chatMessageList.size()>0) {
//        		messageLv.setSelection(chatMessageList.size()-1);
//    		}
        }
    };
    
    
    Runnable loadChatListRunnable = new Runnable() {
		public void run() {
			/**
			 * 查询本地数据读,读取群组已读聊天信息
			 */
			ChatMessageList results = chatMessageDao.queryTwoPeopleChatMessageListByPage(userDevice, receiveGroupId,cutPage,pageSize);
			Message msg = mHandler.obtainMessage(LOAD_STATU);
			msg.obj = results;
			mHandler.sendMessage(msg);
			/**
			 *  将刷新聊天线程从线程队列中取出
			 */
			mHandler.removeCallbacks(loadChatListRunnable);
		}
	};
    
}