package com.zhephone.hotspot.app;
import java.net.URLEncoder;
import org.apache.commons.lang.StringUtils;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.common.SharedPreferencesUtil;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspoti.app.bean.NetworkConnect;
import com.zhephone.hotspoti.app.bean.UserInfo;
import com.zhephone.hotspoti.app.bean.WifiItem;
/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 */
public class AppConfig {

	private String sharedPreferencesName;
	private String wifi_dir;
	private  String log_dir;
	private String deviceToken;
	private String cacheImageDir;
	//private String mapKey;
	/**
	 * 上次的经纬度
	 */
	private String lastLat;
	private String lastLon;
//	/**
//	 * 连接热点ssid
//	 */
//	private String networkSSID;
	private WifiItem mCurrentActivateWifi;
	private static UserInfo userInfo;
	private static AppConfig  instance;
	private static NetworkConnect networkConnet;
	private String appPackageName;
	private AppConfig() {
		wifi_dir = CommonUtil.getSDCardPath()+"/wifi";
		log_dir = wifi_dir + "/log";
		sharedPreferencesName = "wifi_pref";
		cacheImageDir = wifi_dir +"/image";
		//mapKey = "D8078f63dd5d02cb3980fd4b569a73ff";
	}
	
	

	public String getAppPackageName() {
		return appPackageName;
	}



	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}



	public WifiItem getmCurrentActivateWifi() {
		return mCurrentActivateWifi;
	}



	public void setmCurrentActivateWifi(WifiItem mCurrentActivateWifi) {
		this.mCurrentActivateWifi = mCurrentActivateWifi;
	}



	public String getCacheImageDir() {
		return cacheImageDir;
	}


	public String getLastLat() {
		return lastLat;
	}


	public void setLastLat(String lastLat) {
		this.lastLat = lastLat;
	}


	public String getLastLon() {
		return lastLon;
	}


	public void setLastLon(String lastLon) {
		this.lastLon = lastLon;
	}


//	public String getMapKey() {
//		return mapKey;
//	}
//
//	public void setMapKey(String mapKey) {
//		this.mapKey = mapKey;
//	}

	public String getSharedPreferencesName() {
		return sharedPreferencesName;
	}

	public void setSharedPreferencesName(String sharedPreferencesName) {
		this.sharedPreferencesName = sharedPreferencesName;
	}

	public String getWifi_dir() {
		return wifi_dir;
	}

	public void setWifi_dir(String wifi_dir) {
		this.wifi_dir = wifi_dir;
	}

	public String getLog_dir() {
		return log_dir;
	}

	public void setLog_dir(String log_dir) {
		this.log_dir = log_dir;
	}

	public static AppConfig getInstance() {
		if(instance == null) {
			instance = new AppConfig();	
		}
		return instance;
	}
	
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public UserInfo getUserInfo() {
		if(userInfo == null){
			userInfo = new UserInfo();
			userInfo.setSecret(SharedPreferencesUtil.getSetting(AppContext.getInstance(),
					Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_SECRET));
			userInfo.setUserId(SharedPreferencesUtil.getSetting(AppContext.getInstance(),
					Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_USER_ID));
			userInfo.setUserIconUrl(SharedPreferencesUtil.getSetting(AppContext.getInstance(),
					Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_USER_HEAD));
			userInfo.setIsLogin(SharedPreferencesUtil.getSettingBoolean(AppContext.getInstance(),
					Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_LOGIN_STATU));
			userInfo.setName(SharedPreferencesUtil.getSetting(AppContext.getInstance(),
					Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_USER_NAME));
		}
		return userInfo;
	}
	
	/**
	 * 设置使用过app
	 */
	public void saveUseApp() {
		SharedPreferencesUtil.setSetting(AppContext.getInstance(),Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_FIRST,false);
	}
	
	/**
	 * 检查是否是第一次使用app
	 */
	public boolean checkIsFirstUseApp() {
		boolean flag = SharedPreferencesUtil.getSettingBoolean(AppContext.getInstance(),
				Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_FIRST,true);
		return flag;
	}

	public void setUserInfo(UserInfo user)  {
		if(user != null){
			userInfo = user;
			//String secret = userInfo.getSecret().replaceAll("=", "%3D");
			if(StringUtils.isNotEmpty(userInfo.getSecret())) {
				String secret = URLEncoder.encode(userInfo.getSecret());
				SharedPreferencesUtil.setSetting(AppContext.getInstance(), 
						Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_SECRET,secret);
				userInfo.setSecret(secret);
			}
			if(StringUtils.isNotEmpty(userInfo.getUserId())) {
				SharedPreferencesUtil.setSetting(AppContext.getInstance(), 
						Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_USER_ID,userInfo.getUserId());
			}

			if(StringUtils.isNotEmpty(user.getUserIconUrl())) {
				SharedPreferencesUtil.setSetting(AppContext.getInstance(), 
						Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_USER_HEAD,userInfo.getUserIconUrl());
			}
			SharedPreferencesUtil.setSetting(AppContext.getInstance(), 
					Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_LOGIN_STATU, userInfo.getIsLogin());
			if(StringUtils.isNotEmpty(userInfo.getName())) {
				SharedPreferencesUtil.setSetting(AppContext.getInstance(), 
						Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_USER_NAME, userInfo.getName());
			}
			if(StringUtils.isNotEmpty(userInfo.getMobile())) {
				SharedPreferencesUtil.setSetting(AppContext.getInstance(), 
						Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_MOBILE, userInfo.getMobile());
			}

		} else{
			userInfo = null;
		}
	}

	


	public boolean isLogin() {
		boolean isLogin = false;
		String uid = SharedPreferencesUtil.getSetting(AppContext.getInstance(),Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_USER_NAME);
		if(uid.equals("")) {
			isLogin = false;
		} else {
			isLogin = true;		
		}			
		return isLogin;
	}


	public NetworkConnect getNetworkConnect() {
		if(networkConnet == null){
			networkConnet = new NetworkConnect();
		}			
		return networkConnet;			
	}			

	public void setNetworkConnect(NetworkConnect networkConnect) {
		this.networkConnet = networkConnect;				
	}
	
	public void removeUserInfo(){
		SharedPreferencesUtil.removeSetting(AppContext.getInstance(),
				Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_USER_NAME);
		SharedPreferencesUtil.removeSetting(AppContext.getInstance(),
				Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_LOGIN_STATU);
		SharedPreferencesUtil.removeSetting(AppContext.getInstance(),
				Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_SECRET);
		SharedPreferencesUtil.removeSetting(AppContext.getInstance(),
				Constant.SharedPreferencesKeyDef.SHARED_PREFERENCES_USER_HEAD);
		userInfo = null;
	}
}
