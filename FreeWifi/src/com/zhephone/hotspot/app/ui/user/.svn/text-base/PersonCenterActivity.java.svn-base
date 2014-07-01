package com.zhephone.hotspot.app.ui.user;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.ui.MainActivity;
import com.zhephone.hotspot.app.widget.CustomDialog;
import com.zhephone.hotspot.app.widget.DownloadImageView;
import com.zhephone.hotspot.app.widget.Topbar;
import com.zhephone.hotspot.app.widget.AbstractCustomDialog.SureOnClickListener;
import com.zhephone.hotspoti.app.bean.UserInfo;

public class PersonCenterActivity extends BaseActivity {
	private Button backBt;
	private TextView userIdTv;
	private TextView userNameTv;
	private ImageView exitLoginIv;
	private Context context;
	private DownloadImageView userHeadIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.person_center);
		this.setUpViews();
	}

	@Override
	protected void initViews() {
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		topbar.getRightBt().setVisibility(View.GONE);
		topbar.setToolbarCentreText(getResources().getString(R.string.person_center));
		userIdTv = (TextView)findViewById(R.id.user_id_tv);
		userNameTv = (TextView)findViewById(R.id.user_name_tv);
		userHeadIv = (DownloadImageView)findViewById(R.id.user_head_iv);
		exitLoginIv = (ImageView)findViewById(R.id.exit_login_iv);
	}
	
	private OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			back();
		}
	};
	
//	private void loadUserInfoSucess(String data) {
//		UserInfo userInfo = JSON.parseObject(data, UserInfo.class);
//		if(StringUtils.isNotEmpty(userInfo.getUserName())) {
//			userNameTv.setText(userInfo.getUserName());
//		}
//		if(StringUtils.isNotEmpty(userInfo.getUserId())) {
//			userIdTv.setText(userInfo.getUserId());
//		}
//		if(StringUtils.isNotEmpty(userInfo.getUserIconUrl())) {
//			userHeadIv.loadPic(userInfo.getUserIconUrl());
//		}
//		dissmissProgressDialog();
//	}
//	
//	private void loadUserInfoFailed(String error) {
//		dissmissProgressDialog();
//		CommonUtil.sendToast(getApplicationContext(), error);
//	}
	
	private void loadUserInfo() {
		try {
			UserInfo userInfo = AppConfig.getInstance().getUserInfo();
			if(StringUtils.isNotEmpty(userInfo.getMobile())) {
				userNameTv.setText(userInfo.getMobile());
			}
			if(StringUtils.isNotEmpty(userInfo.getUserId())) {
				userIdTv.setText(userInfo.getUserId());
			}
			if(StringUtils.isNotEmpty(userInfo.getUserIconUrl())) {
				userHeadIv.loadPic(userInfo.getUserIconUrl());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		if (CommonUtil.checkNetwork(getApplicationContext())) {
//			showProgressDialog(null,context.getResources().getString(R.string.loading));
//			RequestParams paramMap = new RequestParams();
//			paramMap.put("secret", AppConfig.getInstance().getUserInfo().getSecret());
//			final APIAsyncTask api = new APIAsyncTask();			
//			api.get(Protocol.USER_INFO,paramMap, new CallbackListener() {
//				public void onSuccess(String data) {
//					if (data != null) {
//						loadUserInfoSucess(data);
//					}
//				}
//
//				public void onError(String error) {
//					loadUserInfoFailed(error);
//				}
//			});
//			api.execute();
//		}
	}
	
	private void exit() {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			showProgressDialog(null, getResources().getString(R.string.loading));
			RequestParams paramMap = new RequestParams();
			paramMap.put("secret", AppConfig.getInstance().getUserInfo().getSecret());
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.LOGOUT_OUT,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						AppConfig.getInstance().removeUserInfo();	
						back();
					}
				}

				public void onError(String error) {
					
				}
			});
			api.execute();
		} 
		
	}
	
	private void confirmExit() {
		SureOnClickListener  exitListener= new SureOnClickListener() {
			public void onClick() {
				//exit();
				AppConfig.getInstance().removeUserInfo();	
				back();
			}
		};
		new CustomDialog(PersonCenterActivity.this, "提示", "确认要退出", exitListener, null, true, true).show();
	}
	
	private OnClickListener exitLoginListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			confirmExit();
		}
	};

	@Override
	protected void initData() {
		context = this.getApplicationContext();
		loadUserInfo();
	}

	@Override
	protected void addListener() {
		backBt.setOnClickListener(backListener);
		exitLoginIv.setOnClickListener(exitLoginListener);
	}
	

}
