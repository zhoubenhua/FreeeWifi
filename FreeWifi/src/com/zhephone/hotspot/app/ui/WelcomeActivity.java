package com.zhephone.hotspot.app.ui;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.alibaba.fastjson.JSON;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.widget.AppUpdateDialog;
import com.zhephone.hotspot.app.widget.AppUpdateDialog.AppUpdateListener;
import com.zhephone.hotspoti.app.bean.ChatGroup;
import com.zhephone.hotspoti.app.bean.ChatGroupList;
import com.zhephone.hotspoti.app.bean.VersionUpdate;

public class WelcomeActivity extends BaseActivity {
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		this.setUpViews();
	}

	public String getVersionCode()
	{
	    String str = "";
	    String packageName;
	    try
	    {
	      packageName = getPackageName();
	      AppConfig.getInstance().setAppPackageName(packageName);
	      str = getPackageManager().getPackageInfo(packageName, 0).versionName+"";
	      return str;
	    } catch (Exception e) {			
	    	e.printStackTrace();
	    }
		return str;
	}

	@Override
	protected void initData() {
		context = this;
		String userDevice = AppConfig.getInstance().getDeviceToken();
		registerChat(userDevice);
		//mHandler.postDelayed(waitRunnable, 2000);
		
		
	}
	
	Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	
        }
    };
    
    /**
     * 检查是否是第一次使用
     */
    private void checkIsOpenIntroductionActivity() {
    	Intent intent = null;
		if(AppConfig.getInstance().checkIsFirstUseApp()) {
			intent = new Intent(WelcomeActivity.this,IntroductionActivity.class);
		} else {
			intent = new Intent(WelcomeActivity.this,MainActivity.class);
		}
		startActivity(intent);
		finish();
    }
    
    private AppUpdateListener appUpdateListener = new AppUpdateListener() {

		@Override
		public void cancelUpdate() {
			checkIsOpenIntroductionActivity();
		}

		@Override
		public void updateError(String error) {
			checkIsOpenIntroductionActivity();
		}
    	
    };
    
	/**
	 * 弹出对话框,确认是否要更新
	 */
	private void confirmUpdateDialog(final String downloadUrl)
	{
		// 构造对话框
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(R.string.soft_update_info);
		// 更新  
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();			
				// 显示下载对话框  
				new AppUpdateDialog(context,appUpdateListener,downloadUrl);
			}
		});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				checkIsOpenIntroductionActivity();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.setCanceledOnTouchOutside(false);
		noticeDialog.show();
	}
    
    private void checkVersionUpdateSucess(String data) {
		try {
			VersionUpdate versionUpdate = JSON.parseObject(data, VersionUpdate.class);
			if(StringUtils.isNotEmpty(versionUpdate.getDownloadUrl())) {
				confirmUpdateDialog(versionUpdate.getDownloadUrl());
			} else {
//				String userDevice = AppConfig.getInstance().getDeviceToken();
//				registerChat(userDevice);
				checkIsOpenIntroductionActivity();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		dissmissProgressDialog();
	}
	
	private void checkVersionUpdateFailed(String error) {
		dissmissProgressDialog();
		CommonUtil.sendToast(getApplicationContext(), error);
		checkIsOpenIntroductionActivity();
	}
	
	private void checkVersionUpdate(final String version,final String userDevice) {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			//showProgressDialog(null, getResources().getString(R.string.loading));
			RequestParams paramMap = new RequestParams();
			paramMap.put("v", version);
			paramMap.put("udid", userDevice);
			//paramMap.put("secret", AppConfig.getInstance().getUserInfo().getSecret());
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.APP_UPDATE,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						checkVersionUpdateSucess(data);
					}
				}

				public void onError(String error) {
					error = error+",确认重试吗?";
					openNetErrorDialog(error,new NetErrorCallback() {
						public void retry() {
							checkVersionUpdate(version,userDevice);
						}
						
					});
				}
			});
			api.execute();
		} else {
			openNetErrorDialog("网络连接失败,确认重试吗?",new NetErrorCallback() {
				public void retry() {			
					checkVersionUpdate(version,userDevice);							
				}
			});
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
			JPushInterface.setAliasAndTags(getApplicationContext(), userDevice, null, new TagAliasCallback() {

				@Override
				public void gotResult(int arg0, String arg1, Set<String> arg2) {
					CommonUtil.sendToast(context, arg1);
				}
				
			});
		}
		checkVersionUpdate(getVersionCode(),userDevice);
	}
	
	/**
	 * 获取用户所在的所有的组
	 * @param uid
	 */
	private void loadAllGroupByUserId(final String userDevice) {
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
					error = error+",确认重试吗?";
					openNetErrorDialog(error,new NetErrorCallback() {
						public void retry() {
							loadAllGroupByUserId(userDevice);
						}
						
					});
				}
			});
			api.execute();
		} else {
			openNetErrorDialog("网络连接失败,确认重试吗?",new NetErrorCallback() {
				public void retry() {			
					loadAllGroupByUserId(userDevice);									
				}
			});
		}
	}
	
	public interface NetErrorCallback {
		/**
		 * 重试
		 */
		public void retry();
	}

	
	protected void openNetErrorDialog(String message,final NetErrorCallback callback) {
		AlertDialog.Builder builder = new Builder(WelcomeActivity.this);
		builder.setMessage(message);
		builder.setTitle("提示");
		builder.setPositiveButton("重试", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			   dialog.dismiss();  
			   callback.retry();
			   //auth();	
			}
					
		});  
		builder.setNegativeButton("关闭", new android.content.DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});  
		builder.create().show();
	}
	
	/**
	 * 注册聊天
	 * @param uid
	 */
	private void registerChat(final String userDevice) {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			RequestParams paramMap = new RequestParams();
			paramMap.put("udid", userDevice);
			paramMap.put("os",2+"");
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.REGISTER_CHAT,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						loadAllGroupByUserId(userDevice);
					}
				}

				public void onError(String error) {
					error = error+",确认重试吗?";
					openNetErrorDialog(error,new NetErrorCallback() {
						public void retry() {
							registerChat(userDevice);
						}
						
					});
				}
			});
			api.execute();
		} else {
			openNetErrorDialog("网络连接失败,确认重试吗?",new NetErrorCallback() {
				public void retry() {
					registerChat(userDevice);
				}
			});
		}
	}
    
//	Runnable waitRunnable = new Runnable() {
//		public void run() {
//			mHandler.removeCallbacks(waitRunnable);
//
//			
//		}
//	};
	


}
