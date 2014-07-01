package com.zhephone.hotspot.app.ui.chat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
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
import com.zhephone.hotspot.app.common.TimeUtil;
import com.zhephone.hotspot.app.db.ChatMessageDao;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.widget.XListView;
import com.zhephone.hotspot.app.widget.XListView.IXListViewListener;
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
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
public class SingleChatActivity extends BaseActivity{
	private Button sendChatBt;
	private EditText chatContentEt;
	private XListView chatLv;
	private ChatListAdapter chatMessageListAdapter;
	public  List<ChatMessage> chatMessageList;
	private String sendId;
	private String receId;	
	private ChatMessageDao chatMessageDao;
	private ReceiveChatReceiver receiveChatReceiver;
	private int start;
	private int cutPage;
	private int totalPage;
	private int pageSize;
	private Context context;
	private int LOAD_STATU;
	private static final int NORMAL_LOAD_CHAT_LIST = 1;  //加载聊天
	private static final int FRESH_CHAT_LIST = 2;  //刷新聊天
	private static final int LOAD_MORE_CHAT_LIST = 3;  //加载更多聊天
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.single_chat);
		this.setUpViews();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
	}
	

	IXListViewListener queryChatListListListener = new IXListViewListener() {
		public void onRefresh() {
			/**
			 * 将加载聊天线程直接加入队列
			 */
			start = 0;
	    	cutPage = 0;
	    	totalPage = 0;
	    	LOAD_STATU = FRESH_CHAT_LIST;
			mHandler.post(loadChatListRunnable);
		}

		public void onLoadMore() {
			if ((cutPage >= totalPage) && (totalPage != 0)) {
				CommonUtil.sendToast(context, "已经是最后一页");
			} else {
				/**
				 * 将加载聊天线程直接加入队列
				 */
				LOAD_STATU = LOAD_MORE_CHAT_LIST;
				mHandler.post(loadChatListRunnable);
			}
		}

	};

	@Override
	protected void initViews() {
		sendChatBt = (Button)this.findViewById(R.id.send_bt);
		chatContentEt = (EditText)this.findViewById(R.id.message_et);
		chatLv = (XListView)this.findViewById(R.id.chat_lv);
		chatLv.setPullLoadEnable(true);
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiveChatReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume(); 						
		receiveChatReceiver = new ReceiveChatReceiver();   			
		IntentFilter receiveChatFilter = new IntentFilter();																		  	
		receiveChatFilter.addAction(Constant.ChatIntentDef.RECEIVE_CHAT_ACTION);		
		registerReceiver(receiveChatReceiver, receiveChatFilter); 	
	}


	/**
	 * 接受服务器推送的有新消息广播
	 * @author Administrator
	 *
	 */
	private class ReceiveChatReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			loadNoReadChatMessageList();
		}
	}

	@Override
	protected void initData() {
		context = this.getApplicationContext();
		sendId = this.getIntent().getStringExtra(Constant.ChatIntentDef.SEND_CHAT_ID);
    	receId = this.getIntent().getStringExtra(Constant.ChatIntentDef.RECEIVE_CHAT_ID);
    	chatMessageDao = ChatMessageDao.getInstance(getApplicationContext());
    	start = 0;
    	cutPage = 0;
    	totalPage = 0;
    	pageSize = 10;
    	chatMessageList = new ArrayList<ChatMessage>();
		chatMessageListAdapter = new ChatListAdapter(SingleChatActivity.this,chatMessageList);
		chatLv.setAdapter(chatMessageListAdapter);
		LOAD_STATU = NORMAL_LOAD_CHAT_LIST;
		mHandler.post(loadChatListRunnable);
    	
	}
	
	private void loadNoReadChatMessageListSucess(String data) {
		ChatMessageList result = JSON.parseObject(data, ChatMessageList.class);
		/**
		 * 判断有没有未读聊天信息,如果有将未读聊天数据保存到本地数据库
		 */
		if(result.getMessages() != null && result.getMessages().size() >0) {
			chatMessageDao.saveChatMessageList(result.getMessages());
			LOAD_STATU = LOAD_MORE_CHAT_LIST;
			mHandler.post(loadChatListRunnable);
		}
		dissmissProgressDialog();
	}
	
	
	private void loadNoReadChatMessageListFailed(String error) {
		dissmissProgressDialog();
		CommonUtil.sendToast(getApplicationContext(), error);
	}
	
	/**
	 * 调用接口查询两个人未读的聊天信息
	 */
	private void loadNoReadChatMessageList() {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			//showProgressDialog(null, "加载中...");
			RequestParams paramMap = new RequestParams();
			paramMap.put("secret", AppConfig.getInstance().getUserInfo().getSecret());
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.NO_READ_CHAT_LIST,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						loadNoReadChatMessageListSucess(data);
					}
				}

				public void onError(String error) {
					loadNoReadChatMessageListFailed(error);
				}
			});
			api.execute();
		}
	}

	@Override
	protected void addListener() {
		sendChatBt.setOnClickListener(sendChatListener);
		chatLv.setXListViewListener(queryChatListListListener);
	}
	
	private OnClickListener sendChatListener = new OnClickListener() {
		public void onClick(View v) {
			String content = chatContentEt.getText().toString();
			if(content.equals("")) {
				CommonUtil.sendToast(getApplicationContext(), "聊天内容不能为空");
			} else {
				sendChat(content);
			}
		}
	};
		
	
	private void sendChat(String content) {
		final ChatMessage chatMessage = new ChatMessage();
		chatMessage.setSendId(AppConfig.getInstance().getDeviceToken());
		chatMessage.setReceiveId(sendId);
		chatMessage.setContent(content);
		chatMessage.setTime(TimeUtil.getCurrentDate());
		chatMessage.setIsMySend("0");								
		
		chatMessageList.add(chatMessage);
		chatMessageListAdapter.notifyDataSetChanged();
		chatContentEt.setText("");
		chatLv.setSelection(chatMessageList.size() - 1);
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			RequestParams paramMap = new RequestParams();
			paramMap.put("receiveId", chatMessage.getReceiveId());
			paramMap.put("secret", AppConfig.getInstance().getUserInfo().getSecret());
			paramMap.put("content", content);
			paramMap.put("msgType", 1+"");
			final APIAsyncTask api = new APIAsyncTask();
			api.get(Protocol.SEND_MESSAGE, paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						dissmissProgressDialog();
						chatMessageDao.saveChatMessage(chatMessage);
						CommonUtil.sendToast(getApplicationContext(), "发送成功");
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
    		start = (start + pageSize);
    		cutPage = cutPage + 1;
    		totalPage = (count % pageSize == 0) ? count / pageSize
    				: (count / pageSize + 1);
    		chatLv.setRefreshTime(TimeUtil.getData() + " " + TimeUtil.getTime());
    		chatLv.stopLoadMore();
    		chatLv.stopRefresh();
        	switch(msg.what) {
        	case NORMAL_LOAD_CHAT_LIST:
        	case FRESH_CHAT_LIST:
        		chatMessageList.clear();
        		chatMessageList.addAll(results.getMessages());
        		break;
        	case LOAD_MORE_CHAT_LIST:
        		chatMessageList.addAll(results.getMessages());
        		break;
        	}
        	if ((cutPage >= totalPage) && (totalPage != 0)) {
    			chatLv.setPullLoadEnable(false);
    			CommonUtil.sendToast(context, "已经是最后一页");
    		} else {
    			chatLv.setPullLoadEnable(true);
    		}
    		chatMessageListAdapter.notifyDataSetChanged();
        }
    };
    
    Runnable loadChatListRunnable = new Runnable() {
		public void run() {
			/**
			 * 从本地数据库查询两个人已读的聊天信息
			 */
			ChatMessageList results = chatMessageDao.queryTwoPeopleChatMessageListByPage(sendId, receId, cutPage, pageSize);
			Message msg = mHandler.obtainMessage(LOAD_STATU);
			msg.obj = results;
			mHandler.sendMessage(msg);
			/**
			 *  将加载聊天线程从线程队列中取出
			 */
			mHandler.removeCallbacks(loadChatListRunnable);
		}
	};

    
}