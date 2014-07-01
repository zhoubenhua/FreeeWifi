package com.zhephone.hotspot.app.ui.user;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.AppManager;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.widget.Topbar;

public class RegisterActivity extends BaseActivity {
	private EditText vercodeEt;
	private TextView sureTv;
	private Context context;
	private TextView userNameTv;
	private Button backBt;
	private String userName;
	private String password;
	private Button retrySendVercodeBt;
	private String udid;
	private boolean vercodeFlag;
	private ValidateCount validateCount;
	

	@Override
	protected void initViews() {
		vercodeEt = (EditText)findViewById(R.id.vercode_et);
		userNameTv = (TextView)findViewById(R.id.user_name_tv);
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		sureTv = topbar.getRightTitleTv();			
		retrySendVercodeBt = (Button)findViewById(R.id.retry_send_vercode_bt);
		sureTv.setText(getResources().getString(R.string.sure));
		topbar.getRightBt().setVisibility(View.GONE);			
		topbar.setToolbarCentreText(getResources().getString(R.string.register));
		
	}
	
	private void verficationPhoneSucess(String data) {
		dissmissProgressDialog();
		vercodeFlag = false;
		validateCount.cancel();
		validateCount = new ValidateCount(60000, 1000);
		validateCount.start();
	}
	
	private void verficationPhoneFailed(String error) {
		dissmissProgressDialog();
		CommonUtil.sendToast(getApplicationContext(), error);
	}
	
	private void verficationPhone() {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			showProgressDialog(null, context.getResources().getString(R.string.doing));
			RequestParams paramMap = new RequestParams();
			paramMap.put("mobile", userName);
			paramMap.put("udid", udid);
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.VERIFICATION_PHONE,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						verficationPhoneSucess(data);
					}
				}

				public void onError(String error) {
					verficationPhoneFailed(error);
				}
			});
			api.execute();
		}
	}
	
	private boolean checkVercodeIsValid(String vercode) {
		boolean flag;
		if(vercode.equals("")) {
			flag = false;
			CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.vercode_not_null));
		}  else if(vercode.length()<6) {
			flag = false;
			CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.vercode_length_error));
		} else {
			flag = true;
		}
		return flag;
	}
	
	private void userRegisterSucess(String data) {
		dissmissProgressDialog();
		CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.register_sucess));
		AppManager.getAppManager().finishActivity(VerficationPhoneActivity.class);
		back();
	}
	
	private void userRegisterFailed(String error) {
		dissmissProgressDialog();
		CommonUtil.sendToast(getApplicationContext(), error);
	}
	
	private void userRegister(String vercode) {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			showProgressDialog(null, context.getResources().getString(R.string.registering));
			RequestParams paramMap = new RequestParams();
			paramMap.put("mobile", userName);
			paramMap.put("password", password);
			paramMap.put("code", vercode);
			paramMap.put("os", "2");
			paramMap.put("udid", udid);
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.REGISTER,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						userRegisterSucess(data);
					}
				}

				public void onError(String error) {
					userRegisterFailed(error);
				}
			});
			api.execute();
		}
	}

	
	private OnClickListener registerListener = new OnClickListener() {
		public void onClick(View v) {
			String vercode = vercodeEt.getText().toString();
			if(checkVercodeIsValid(vercode)) {
				userRegister(vercode);
			}
		}
	};
	
	private OnClickListener retrySendVercodeListener = new OnClickListener() {
		public void onClick(View v) {
			if(vercodeFlag) {
				verficationPhone();
			}
			
		}
	};
	
	private OnClickListener backListener = new OnClickListener() {
		public void onClick(View v) {
			back();
		}
	};
	
	
	/*
	 * 定义一个倒计时的内部类
	 */  
	private class ValidateCount extends CountDownTimer{
	    public ValidateCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
	    
		public void onFinish() {
			vercodeFlag =true;
			retrySendVercodeBt.setText("重发");
		}

		public void onTick(long millisUntilFinished) { 
			vercodeFlag = false;
			retrySendVercodeBt.setText("再次发送验证码(" + millisUntilFinished /1000 +")");
		}
	}


	@Override
	protected void initData() {
		context = this.getApplicationContext();
		userName = this.getIntent().getStringExtra(Constant.UserIntentDef.USER_NAME);
		password = this.getIntent().getStringExtra(Constant.UserIntentDef.PASSWORD);
		udid = AppConfig.getInstance().getDeviceToken();
		String subtelephone = userName.substring(3, 7);
		String phone = userName.replaceAll(subtelephone, "xxxx");
		userNameTv.setText(phone);
		vercodeFlag = false;
		validateCount = new ValidateCount(60000, 1000);
		validateCount.start();
	}

	@Override
	protected void addListener() {
		sureTv.setOnClickListener(registerListener);
		backBt.setOnClickListener(backListener);
		retrySendVercodeBt.setOnClickListener(retrySendVercodeListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.user_register);
		this.setUpViews();
	}

	
}
