package com.zhephone.hotspot.app.ui.user;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.zhephone.hotspot.app.widget.Topbar;
import com.zhephone.hotspoti.app.bean.UserInfo;

public class LoginActivity extends BaseActivity {
	private EditText userNameEt;
	private EditText passwordEt;
	private TextView loginTv;
	private TextView registerTv;
	private Context context;
	private Button backBt;
	private String userName;
	private String udid;

	@Override
	protected void initViews() {
		userNameEt = (EditText)findViewById(R.id.user_name_et);
		passwordEt = (EditText)findViewById(R.id.user_password_et);
		registerTv = (TextView)findViewById(R.id.register_tv);
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		loginTv = topbar.getRightTitleTv();
		loginTv.setText(getResources().getString(R.string.sure));
		topbar.getRightBt().setVisibility(View.GONE);
		topbar.setToolbarCentreText(getResources().getString(R.string.login));
		
	}
	
	private boolean validateLogin(String userName,String password) {
		boolean flag;
		if(userName.equals("")) {
			flag = false;
			CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.username_not_null));
		} else if(userName.length() <11) {
			flag = false;
			CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.phone_length_error));
		} else if(!CommonUtil.isMobileNO(userName)) {
			flag = false;
			CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.phone_format_error));
		} else if(password.equals("")) {
			flag = false;
			CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.password_not_null));
		} else {
			flag = true;
		}
		return flag;
	}
	
	private void loginSucess(String data) {
		UserInfo userInfo = JSON.parseObject(data, UserInfo.class);
		userInfo.setMobile(userName);
		AppConfig.getInstance().setUserInfo(userInfo);		
		dissmissProgressDialog();
		back();
	}
	
	private void loginFailed(String error) {
		dissmissProgressDialog();
		CommonUtil.sendToast(getApplicationContext(), error);
	}
	
	private void userLogin(String userName,String password) {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			showProgressDialog(null,context.getResources().getString(R.string.logining));
			RequestParams paramMap = new RequestParams();
			paramMap.put("mobile", userName);
			paramMap.put("password", password);
			paramMap.put("udid",udid);
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.LOGIN,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						loginSucess(data);
					}
				}

				public void onError(String error) {
					loginFailed(error);
				}
			});
			api.execute();
		}
	}

	
	private OnClickListener loginListener = new OnClickListener() {
		public void onClick(View v) {
			userName = userNameEt.getText().toString();
			String password = passwordEt.getText().toString();
			if(validateLogin(userName,password)) {
				userLogin(userName,password);
			}
		}
	};
	
	private OnClickListener registerListener = new OnClickListener() {
		public void onClick(View v) {
			startActivityLeft(new Intent(LoginActivity.this,VerficationPhoneActivity.class));
		}
	};
	
	private OnClickListener backListener = new OnClickListener() {
		public void onClick(View v) {
			back();
		}
	};
	
	@Override
	protected void initData() {
		context = this.getApplicationContext();
		udid = AppConfig.getInstance().getDeviceToken();
	}

	@Override
	protected void addListener() {
		loginTv.setOnClickListener(loginListener);
		registerTv.setOnClickListener(registerListener);
		backBt.setOnClickListener(backListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.user_login);
		this.setUpViews();
	}


}
