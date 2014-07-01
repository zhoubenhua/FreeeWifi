package com.zhephone.hotspot.app.ui.wifi;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.common.WifiConnectManager;
import com.zhephone.hotspot.app.widget.Topbar;
import com.zhephone.hotspoti.app.bean.WifiItem;

public class WifiInputPasswordActivity extends BaseActivity {
	private TextView wifiSsidNameTv;
	private EditText wifiPasswordEt;
//	private Button connectWifiBt;
//	private Button disconnectWifiBt;
	private WifiItem wifiItem;
	private WifiManager wifiManager;
	private Context context;
	private Button backBt;
	private Topbar topbar;
	private int connectStatu;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.connect_wifi);
		this.setUpViews();
	}

	@Override
	protected void initViews() {
		wifiSsidNameTv = (TextView)findViewById(R.id.wifi_ssid_name_tv);
		wifiPasswordEt = (EditText)findViewById(R.id.wifi_password_et);
//		connectWifiBt = (Button)findViewById(R.id.connect_wifi_bt);
//		disconnectWifiBt = (Button)findViewById(R.id.disconnect_wifi_bt);
		topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		topbar.setToolbarCentreText(getResources().getString(R.string.wifi_connect_setting));
	}
	
	private OnClickListener connectWifiListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(connectStatu == Constant.WifiIntentDef.WIFI_CONNECT_SUCCESS) {
				/**
				 * 如果是已经连接wifi,就断开wifi			
				 */
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				//wifiManager.removeNetwork(wifiInfo.getNetworkId());			
				wifiManager.disableNetwork(wifiInfo.getNetworkId());
				//wifiManager.disconnect();			
				back();						
			} else {			
				/**
				 * 连接指定wifi
				 */
				String wifiPassword = wifiPasswordEt.getText().toString();
//				if(wifiPassword.equals("")) {
//					CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.password_not_null));
//				} else {
					wifiItem.setPassword(wifiPassword);
					CommonUtil.sendToast(getApplicationContext(), context.getResources().getString(R.string.connecting));
					WifiConnectManager.getInstance().connect(wifiItem, wifiManager, getApplicationContext());
					//back();
				//}
			}
			
		}
	};
	

	@Override
	protected void initData() {
		context = this.getApplicationContext();
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiItem = (WifiItem) getIntent().getSerializableExtra(Constant.WifiIntentDef.WIFI_HOTSPOT);
		wifiSsidNameTv.setText(wifiItem.getSsidName());
	    connectStatu = wifiItem.getConnectStatu();
		/**
		 * 判断当前wifi有没有连接,如果没有连接就让用户连接,如果已经连接,就让用户断开连接
		 */
		if(connectStatu == Constant.WifiIntentDef.WIFI_CONNECT_SUCCESS) {
			topbar.getRightTitleTv().setText(R.string.dis_connect_wifi);
			wifiPasswordEt.setVisibility(View.GONE);
		} else {
			wifiPasswordEt.setVisibility(View.VISIBLE);
			topbar.getRightTitleTv().setText(R.string.connect_wifi);
		}
		
	}
	
	private OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			back();
		}
	};

	@Override
	protected void addListener() {
		backBt.setOnClickListener(backListener);
		topbar.getRightTitleTv().setOnClickListener(connectWifiListener);
	}

}
