package com.zhephone.hotspot.app.ui.user;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.widget.Topbar;


public class VerficationPhoneActivity extends BaseActivity {
	private EditText userNameEt;
	private EditText passwordEt;
	private TextView verficationTv;
	private Context context;
	private Button backBt;
	private String udid;

	@Override
	protected void initViews() {
		userNameEt = (EditText)findViewById(R.id.user_name_et);
		passwordEt = (EditText)findViewById(R.id.user_password_et);
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		verficationTv = topbar.getRightTitleTv();			
		verficationTv.setText(getResources().getString(R.string.verfication));
		topbar.getRightBt().setVisibility(View.GONE);			
		topbar.setToolbarCentreText(getResources().getString(R.string.register));
		
	}
	
	private boolean checkUserNamePasswordIsValid(String userName,String password) {
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
		}  else if(password.equals("")) {
			flag = false;
			CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.password_not_null));
		} else {
			flag = true;
		}
		return flag;
	}
	
	private void verficationPhoneSucess(String data) {
		dissmissProgressDialog();
		String userName = userNameEt.getText().toString();
		String password = passwordEt.getText().toString();
		Intent intent = new Intent(context,RegisterActivity.class);
		intent.putExtra(Constant.UserIntentDef.USER_NAME, userName);
		intent.putExtra(Constant.UserIntentDef.PASSWORD, password);
		startActivityLeft(intent);		
		//CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.register_sucess));
		back();
	}
	
	private void verficationPhoneFailed(String error) {
		dissmissProgressDialog();
		CommonUtil.sendToast(getApplicationContext(), error);
	}
	
	private void verficationPhone(String userName,String password) {
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
	
	private OnClickListener backListener = new OnClickListener() {
		public void onClick(View v) {
			back();
		}
	};

	
	private OnClickListener verficationPhoneListener = new OnClickListener() {
		public void onClick(View v) {
			String userName = userNameEt.getText().toString();
			String password = passwordEt.getText().toString();
			if(checkUserNamePasswordIsValid(userName,password)) {
				verficationPhone(userName,password);
			}
		}
	};

	
	

	@Override
	protected void initData() {
		context = this.getApplicationContext();
		udid = AppConfig.getInstance().getDeviceToken();
	}

	@Override
	protected void addListener() {
		verficationTv.setOnClickListener(verficationPhoneListener);
		backBt.setOnClickListener(backListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.verfication_phone);
		this.setUpViews();
	}

	
}
