package com.zhephone.hotspot.app.ui.wifi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.adapter.WifiHotListAdapter;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.widget.Topbar;
import com.zhephone.hotspoti.app.bean.WifiItem;

public class WifiListActivity extends BaseActivity {
	private ListView wifiHotspotLv;
	private WifiManager wifiManager;
	private List<WifiItem> wifiList;
	private WifiHotListAdapter wifiHotListAdapter;
	private Context context;
	private String connectHotspotSsid;
	private WifiScanResultReceiver wifiScanResultReceiver;
	private Button backBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.wifi_hotspot_list);
		this.setUpViews();
	}

	@Override
	protected void initViews() {
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		topbar.getRightBt().setVisibility(View.GONE);
		topbar.setToolbarCentreText(getResources().getString(R.string.connect_wifi));
		wifiHotspotLv = (ListView)findViewById(R.id.wifi_hotspot_lv);
	}
	
	private void handleScanResult() {
		List<ScanResult> resultList = wifiManager.getScanResults();
    	wifiList.clear();
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if(wifiInfo != null && StringUtils.isNotEmpty(wifiInfo.getSSID())) {
			if(wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
				connectHotspotSsid = wifiInfo.getSSID().replace("\"","");
			} else {
				connectHotspotSsid = null;
			}
		}
		HashMap<String,ScanResult> scanMaps = new HashMap<String,ScanResult>();
		for(ScanResult scanResult:resultList){
			scanMaps.put(scanResult.SSID, scanResult);
		}
		WifiItem wifiItem;
		ScanResult scanResult;		
		Iterator iterator = scanMaps.entrySet().iterator();     
		while(iterator.hasNext()){  
		    Map.Entry<String, ScanResult> entry =(Map.Entry<String, ScanResult>)iterator.next(); 
		    scanResult = entry.getValue();
		    wifiItem = new WifiItem(scanResult);
			wifiItem.setSsidName(scanResult.SSID);
			if(StringUtils.isNotEmpty(connectHotspotSsid) && connectHotspotSsid.equals(scanResult.SSID)) {
				wifiItem.setConnectStatu(Constant.WifiIntentDef.WIFI_CONNECT_SUCCESS);
			} 
			wifiList.add(wifiItem);
		} 
//
//
//		for(ScanResult scanResult:resultList){
//			wifiItem = new WifiItem(scanResult);
//			wifiItem.setSsidName(scanResult.SSID);
//			if(StringUtils.isNotEmpty(connectHotspotSsid) && connectHotspotSsid.equals(scanResult.SSID)) {
//				wifiItem.setConnectStatu(Constant.WifiIntentDef.WIFI_CONNECT_SUCCESS);
//			} 
//			wifiList.add(wifiItem);
//		}
		if(wifiHotListAdapter != null) {
			wifiHotListAdapter.notifyDataSetChanged();
		} else {
			wifiHotListAdapter = new WifiHotListAdapter(wifiList,context);
			wifiHotspotLv.setAdapter(wifiHotListAdapter);
		}
		dissmissProgressDialog();
	}

	class WifiScanResultReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
        	handleScanResult();
        }

	}
	
	private OnItemClickListener hotspotListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(context,WifiInputPasswordActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(Constant.WifiIntentDef.WIFI_HOTSPOT, wifiList.get(arg2));
			intent.putExtras(bundle);
			startActivityLeft(intent);
		}
	};
	
	protected void onPause() {
		unregisterReceiver(wifiScanResultReceiver);
        super.onPause();

	}
	
	private OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			back();
		}
	};

	protected void onResume() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		intentFilter.addAction(Constant.WifiIntentDef.WIFI_STATE_CHANGE_ACTION);
		registerReceiver(wifiScanResultReceiver, intentFilter);
        //
        super.onResume();
	}
	
	@Override
	protected void initData() {
		context = this.getApplicationContext();
		wifiList = new ArrayList<WifiItem>();
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiScanResultReceiver = new WifiScanResultReceiver();
        wifiManager.startScan();
        handleScanResult();
        //showProgressDialog(null, context.getResources().getString(R.string.loading));
	}

	@Override
	protected void addListener() {
		backBt.setOnClickListener(backListener);
		wifiHotspotLv.setOnItemClickListener(hotspotListener);
	}

}
