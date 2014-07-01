package com.zhephone.hotspot.app.ui.setting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.StringUtil;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.widget.Topbar;

public class SetFeedbackActivity extends BaseActivity implements
		OnClickListener {
	EditText set_feedback_phone, set_feedback_content;
	TextView set_feedback_tip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_feedback);
		setUpViews();

	}

	@Override
	protected void initViews() {
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		topbar.setToolbarCentreText(getResources().getString(
				R.string.set_feedback));
		topbar.getRightTitleTv().setText(
				getResources().getString(R.string.set_feedback_send));
		topbar.getRightTitleTv().setOnClickListener(this);
		topbar.getLeftBt().setOnClickListener(this);
		set_feedback_phone = (EditText) findViewById(R.id.set_feedback_phone);
		set_feedback_content = (EditText) findViewById(R.id.set_feedback_content);
		set_feedback_tip = (TextView) findViewById(R.id.set_feedback_tip);
	}

	@Override
	protected void initData() {
		set_feedback_content.addTextChangedListener(textWatcher);
	}

	private TextWatcher textWatcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			set_feedback_tip.setText("还可以输入" + (200 - temp.length()) + "字");
		}
	};

	public static void start(Context context) {
		context.startActivity(new Intent(context, SetFeedbackActivity.class));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_left_bt:
			back();
			break;
		case R.id.topbar_right_title_tv:
			if (getState()) {
				sendInfo();
			}
			break;
		default:
			break;
		}

	}

	public void sendInfo() {
		if (!CommonUtil.checkNetwork(getApplicationContext())) {
			return;
		}
		showProgressDialog(null,
				getResources().getString(R.string.set_feedback_sending));
		String content = set_feedback_content.getText().toString().trim();
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern pat = Pattern.compile(regEx);//正则过滤掉所有特殊符号提交
		Matcher mat = pat.matcher(content);
		String result = mat.replaceAll("").trim();
		String phone = set_feedback_phone.getText().toString().trim();
		RequestParams paramMap = new RequestParams();
		paramMap.put("content", result);
		paramMap.put("mobile", phone);
		final APIAsyncTask api = new APIAsyncTask();
		api.post(Protocol.FEEDBACK, paramMap, new CallbackListener() {
			public void onSuccess(String data) {
				if (data != null) {
					sucess(data);
				} else {
					failed("网络错误，请重试");
				}
			}

			public void onError(String error) {
				failed(error);
			}

		});
		api.execute();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			dissmissProgressDialog();
		};
	};

	private void sucess(String data) {
		handler.sendEmptyMessageDelayed(0, 500);
		CommonUtil.sendToast(getApplicationContext(), "提交成功！");
		set_feedback_content.setText("");
		set_feedback_phone.setText("");
	}

	private void failed(String error) {
		handler.sendEmptyMessageDelayed(0, 500);
		CommonUtil.sendToast(getApplicationContext(), error);
	}

	public boolean getState() {
		String content = set_feedback_content.getText().toString();
		String phone = set_feedback_phone.getText().toString();
		if (StringUtil.isNull(content)) {
			showLongToast("输入内容不能为空");
			return false;
		}
		if (content.trim().length() < 10) {
			showLongToast("您输入内容太少了");
			return false;
		}
		if (!StringUtil.isPhone(phone)) {
			showLongToast("手机号输入不规范");
			return false;
		}
		return true;
	}

}
