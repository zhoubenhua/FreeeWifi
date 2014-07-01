package com.zhephone.hotspot.app.ui.chat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang.StringUtils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cn.jpush.android.api.JPushInterface;
import com.alibaba.fastjson.JSON;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.BaseActivity;			
import com.zhephone.hotspot.app.adapter.GroupListAdapter;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.db.ChatMessageDao;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.widget.Topbar;
import com.zhephone.hotspoti.app.bean.ChatGroup;
import com.zhephone.hotspoti.app.bean.ChatGroupList;

public class GroupListActivity extends BaseActivity {

	private Button backBt;
	private ListView groupLv;
	private Context context;
	private ArrayList<ChatGroup> groupList;
	private ChatMessageDao chatMessageDao;
	private String userId;
	//private ReceiveChatReceiver receiveChatReceiver;
	private String secret;
	private RelativeLayout noChatRl;
	private ImageView lookChatIv;
	private String userDevice;
	private GroupListAdapter groupListAdapter;
	private ReceiveNewMsgReceiver receiveNewMsgReceiver;
	public static GroupListHandler groupListHandler;

	@Override
	protected void initViews() {
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		topbar.getRightBt().setVisibility(View.GONE);
		topbar.setToolbarCentreText(getResources().getString(R.string.chat));
		groupLv = (ListView)findViewById(R.id.group_lv);
		noChatRl = (RelativeLayout)findViewById(R.id.no_chat_rl);
		lookChatIv = (ImageView)findViewById(R.id.look_chat_iv);
	}
	
	private OnClickListener backListener = new OnClickListener() {
		public void onClick(View v) {
			back();
		}
	};
	
	private OnClickListener lookChatListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(context,NearGroupActivity.class);
			startActivityLeft(intent);
		}
	};

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
			noChatRl.setVisibility(View.GONE);
			groupLv.setVisibility(View.VISIBLE);
			groupList.clear();
			groupList.addAll(channels);
			groupLv.setSelection(groupList.size()-1);
			checkGroupHasNoReadMessage();
		} else {
			noChatRl.setVisibility(View.VISIBLE);
			groupLv.setVisibility(View.GONE);
		}
		dissmissProgressDialog();
	}
	
	/**
	 * 检查组有没有新的消息
	 */
	private void checkGroupHasNoReadMessage() {
		if(groupList != null && groupList.size()>0) {
			for(ChatGroup chatGroup:groupList) {
				chatGroup.setHasNoReadMessage(chatMessageDao.queryGroupHasNoReadMessage(userDevice,
						chatGroup.getImei()));
			}
			groupListAdapter = new GroupListAdapter(context,groupList); 
			groupLv.setAdapter(groupListAdapter);
		}
	}
	
	/**
	 * 注册有新的消息广播
	 * @author Administrator
	 *
	 */
	private class ReceiveNewMsgReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			checkGroupHasNoReadMessage();
		}
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiveNewMsgReceiver);
	}
	
	@Override
	protected void onResume() {
		super.onResume(); 	
		checkGroupHasNoReadMessage();
		receiveNewMsgReceiver = new ReceiveNewMsgReceiver();   			
		IntentFilter intentFilter = new IntentFilter();																		  	
		intentFilter.addAction(Constant.ChatIntentDef.RECEIVE_NEW_MSG_ACTION);		
		registerReceiver(receiveNewMsgReceiver, intentFilter); 	
	}

	private void loadAllGroupByUserIdFailed(String error) {
		CommonUtil.sendToast(context, error);
		dissmissProgressDialog();
	}
	
	/**
	 * 获取用户所在的所有的组
	 * @param uid
	 */
	public void loadAllGroupByUserId() {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			showProgressDialog(null, getResources().getString(R.string.loading));
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
	 * 群组点击事件
	 */
	private OnItemClickListener groupListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			try {
				if(groupList.size() >0) {
					ChatGroup group = groupList.get(arg2);
					Intent intent = new Intent(context,GroupChatActivity.class);
					intent.putExtra(Constant.ChatIntentDef.GROUP_NAME, group.getName());
					intent.putExtra(Constant.ChatIntentDef.GROUP_CHAT_ID, group.getImei());
		    		intent.putExtra(Constant.ChatIntentDef.CHAT_SOURCE, Constant.ChatIntentDef.GROUP_LIST);
					startActivityLeft(intent);
				}
							
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	};

	@Override
	protected void addListener() {
		groupLv.setOnItemClickListener(groupListener);
		backBt.setOnClickListener(backListener);
		lookChatIv.setOnClickListener(lookChatListener);
	}
	
	@Override
	protected void initData() {
		groupListHandler = new GroupListHandler(this);
		context = this.getApplicationContext();
		userId = AppConfig.getInstance().getUserInfo().getUserId();
		secret = AppConfig.getInstance().getUserInfo().getSecret();
		chatMessageDao = ChatMessageDao.getInstance(getApplicationContext());	
		groupList = new ArrayList<ChatGroup>(5);
		userDevice = AppConfig.getInstance().getDeviceToken();
		loadAllGroupByUserId();
		//loadNoReadChatMessageList();
		//loadChatGroupList();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.group_list);
		this.setUpViews();
	}

}
