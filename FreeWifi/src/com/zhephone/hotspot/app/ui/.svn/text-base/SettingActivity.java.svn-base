package com.zhephone.hotspot.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.ui.setting.SetAboutActivity;
import com.zhephone.hotspot.app.ui.setting.SetFeedbackActivity;
import com.zhephone.hotspot.app.ui.setting.SetVersionInfoActivity;
import com.zhephone.hotspot.app.ui.wifi.WifiListActivity;
import com.zhephone.hotspot.app.widget.Topbar;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout wifiConnectSettingRl;
	private Context context;
	private Button backBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting);
		this.setUpViews();
	}

	@Override
	protected void initViews() {
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		topbar.getRightBt().setVisibility(View.GONE);
		topbar.setToolbarCentreText(getResources().getString(R.string.setting));
		wifiConnectSettingRl = (RelativeLayout) findViewById(R.id.wifi_connect_setting_rl);

	}

	@Override
	protected void initData() {
		context = this.getApplicationContext();
	}

	private OnClickListener wifiConnectSettingListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			startActivityLeft(new Intent(context, WifiListActivity.class));
		}
	};

	private OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			back();
		}
	};

	@Override
	protected void addListener() {
		wifiConnectSettingRl.setOnClickListener(wifiConnectSettingListener);
		backBt.setOnClickListener(backListener);
		findViewById(R.id.about_app_rl).setOnClickListener(this);
		findViewById(R.id.help_feedback_rl).setOnClickListener(this);
		findViewById(R.id.version_detail_rl).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_app_rl:
			startActivityLeft(new Intent(context, SetAboutActivity.class));
			break;
		case R.id.help_feedback_rl:
			startActivityLeft(new Intent(context, SetFeedbackActivity.class));
			//SetFeedbackActivity.start(this);
			break;
		case R.id.version_detail_rl:
			startActivityLeft(new Intent(context, SetVersionInfoActivity.class));
			//SetVersionInfoActivity.start(this);
			break;
		default:
			break;
		}

	}

}
