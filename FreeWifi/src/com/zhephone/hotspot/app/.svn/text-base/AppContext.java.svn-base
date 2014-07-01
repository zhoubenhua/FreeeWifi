package com.zhephone.hotspot.app;
import org.apache.commons.lang.StringUtils;

import cn.jpush.android.api.JPushInterface;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.LogUtil;
import com.zhephone.hotspot.app.db.WifiDatabseAccess;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;




public class AppContext extends Application{
	public static AppContext instance;
	private WifiDatabseAccess dbAccess = null;
	public BMapManager mBMapManager;
	public void onCreate() {
		super.onCreate();	
		instance = this;
		String device = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		//String device = CommonUtil.getImei(getApplicationContext(), "");
		AppConfig.getInstance().setDeviceToken(device);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        JPushInterface.setAliasAndTags(getApplicationContext(), device, null, null);
        initEngineManager(this);
    	IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(new WifiConnectionStatuReceiver(), intentFilter);
		AppException handler = AppException.getInstance(this);  
		Thread.setDefaultUncaughtExceptionHandler(handler);  
		//((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true);
	} 
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
            	CommonUtil.sendToast(AppContext.getInstance().getApplicationContext(),
            			"您的网络出错啦！");
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
            	CommonUtil.sendToast(AppContext.getInstance().getApplicationContext(),
            			"输入正确的检索条件！");
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
        	//非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
            	LogUtil.log("请输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError);
            }
            else{
            	CommonUtil.sendToast(AppContext.getInstance().getApplicationContext(),
            			"key认证成功");
            }
        }
    }
    
    /**
	 * 监听位置变化事件
	 */
	private class MyLocationListenner implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			String lat = location.getLatitude()+"";
			String lng = location.getLongitude()+"";
			int distance;
			if(StringUtils.isEmpty(AppConfig.getInstance().getLastLat()) && StringUtils.isEmpty(AppConfig.getInstance().getLastLon())) {
				distance = CommonUtil.getDistance("0", "0", lng, lat);
			} else {
				distance = CommonUtil.getDistance(AppConfig.getInstance().getLastLon(), AppConfig.getInstance().getLastLat(), lng, lat);
			}
			/**
			 * 判断所有界面有没有finish,如果全部finish就不向服务器发送经纬度,如果没有还要判断距离有没有超过100米
			 */
			if(!AppManager.getAppManager().isExitApp() && distance >=100) {
				AppConfig.getInstance().setLastLat(lat);
				AppConfig.getInstance().setLastLon(lng);
				/**
				 * 判断经纬度是否为4.9E-324,如果是说明定位出现错误,就不上传到服务器
				 */
				if(!lat.equals("4.9E-324") && !lng.equals("4.9E-324")) {
			        Intent intent = new Intent(AppContext.getInstance().getApplicationContext(),UploadLatLonService.class);
			        intent.putExtra(Constant.HotIntentDef.GPS_LAT, location.getLatitude()+"");
			        intent.putExtra(Constant.HotIntentDef.GPS_LON, location.getLongitude()+"");
			        startService(intent);	
				}
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}
    
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        try {
			if (!mBMapManager.init(new MyGeneralListener())) {
				CommonUtil.sendToast(getApplicationContext(), "BMapManager  初始化错");
			} 
//			else {
//				LocationClient mLocClient = new LocationClient(this);
//	    		mLocClient.registerLocationListener(new MyLocationListenner());
//	    		LocationClientOption option = new LocationClientOption();
//	    		option.setOpenGps(true);// 打开gps
//	    		option.setCoorType("bd09ll"); // 设置坐标类型
//	    		option.setLocationNotify(true);
//	    		option.setScanSpan(1000);
//	    		mLocClient.setLocOption(option);
//	    		mLocClient.start();
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static AppContext getInstance() {
		return (AppContext) instance;
	}
	
	public WifiDatabseAccess getDbAccess() {
		if (this.dbAccess == null) {
			this.dbAccess = new WifiDatabseAccess(this);
		}
		return this.dbAccess;
	}


}
