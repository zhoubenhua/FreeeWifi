package com.zhephone.hotspot.app;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.common.WifiConnectUtil;
import com.zhephone.hotspoti.app.bean.WifiItem;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiConnectionStatuReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent arg1) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        WifiItem item = AppConfig.getInstance().getmCurrentActivateWifi();
		if (item != null && info != null) {
			AppConfig.getInstance().setmCurrentActivateWifi(null);
			String statuStr = context.getResources().getString(R.string.connected_failed);
			/**
			 * 判断当前连接的热点是不是要连接的热点
			 */
			if (WifiConnectUtil.removeDoubleQuotes(info.getSSID())
					.equals(WifiConnectUtil.removeDoubleQuotes(item.ssid))) {
				/**
				 * 等于 COMPLETED说明连接成功		
				 */
				if(info.getSupplicantState() == SupplicantState.COMPLETED) {
					statuStr = context.getResources().getString(R.string.connected_sucess);
				}						
			} 
			CommonUtil.sendToast(context, statuStr);
			
		} 
		Intent intent = new Intent();
		intent.setAction(Constant.WifiIntentDef.WIFI_STATE_CHANGE_ACTION);
		context.sendBroadcast(intent);
	}

}
