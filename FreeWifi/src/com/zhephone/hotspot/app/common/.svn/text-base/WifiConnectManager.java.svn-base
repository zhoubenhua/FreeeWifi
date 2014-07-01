package com.zhephone.hotspot.app.common;

import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.wificonnect.ConfigurationSecurities;
import com.zhephone.hotspot.app.wificonnect.ReenableAllApsWhenNetworkStateChanged;
import com.zhephone.hotspoti.app.bean.WifiItem;

public class WifiConnectManager {
	
	private final int mNumOpenNetworksKept = 10;
	private static final String TAG = "Wifi Connecter";
	public static WifiConnectManager instance;
	private WifiConnectManager() {
		
	}
	public static WifiConnectManager getInstance() {
		if(instance == null) {
			instance = new WifiConnectManager();
		}
		return instance;
	}
	
	public static final ConfigurationSecurities ConfigSec = ConfigurationSecurities.newInstance();
	
	/**
	 * Ensure no more than numOpenNetworksKept open networks in configuration list.
	 * @param wifiMgr
	 * @param numOpenNetworksKept
	 * @return Operation succeed or not.
	 */
	private boolean checkForExcessOpenNetworkAndSave(final WifiManager wifiMgr, final int numOpenNetworksKept) {
		final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
		sortByPriority(configurations);
		
		boolean modified = false;
		int tempCount = 0;
		for(int i = configurations.size() - 1; i >= 0; i--) {
			final WifiConfiguration config = configurations.get(i);
			if(ConfigSec.isOpenNetwork(ConfigSec.getWifiConfigurationSecurity(config))) {
				tempCount++;
				if(tempCount >= numOpenNetworksKept) {
					modified = true;
					wifiMgr.removeNetwork(config.networkId);
				}
			}
		}
		if(modified) {
			return wifiMgr.saveConfiguration();
		}
		
		return true;
	}
	
	private  void sortByPriority(final List<WifiConfiguration> configurations) {
		java.util.Collections.sort(configurations, new Comparator<WifiConfiguration>() {

			@Override
			public int compare(WifiConfiguration object1,
					WifiConfiguration object2) {
				return object1.priority - object2.priority;
			}
		});
	}
	
	
	
	private static final int MAX_PRIORITY = 99999;
	
	private int shiftPriorityAndSave(final WifiManager wifiMgr) {
		final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
		sortByPriority(configurations);
		final int size = configurations.size();
		for(int i = 0; i < size; i++) {
			final WifiConfiguration config = configurations.get(i);
			config.priority = i;
			wifiMgr.updateNetwork(config);
		}
		wifiMgr.saveConfiguration();
		return size;
	}

	private int getMaxPriority(final WifiManager wifiManager) {
		final List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
		int pri = 0;
		for(final WifiConfiguration config : configurations) {
			if(config.priority > pri) {
				pri = config.priority;
			}
		}
		return pri;
	}

	public WifiConfiguration getWifiConfiguration(final WifiManager wifiMgr, final WifiItem wifiItem) {
		final String ssid = convertToQuotedString(wifiItem.ssid);;
		if(ssid.length() == 0) {
			return null;
		}
		final String bssid = wifiItem.bssid;
		if(bssid == null) {
			return null;
		}
		
		final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
		if(configurations == null) {
			return null;
		}

		for (final WifiConfiguration config : configurations) {
			if (config.SSID == null || !ssid.equals(config.SSID)) {
				continue;
			}
			final String configSecurity = ConfigSec.getWifiConfigurationSecurity(config);
			if (String.valueOf(wifiItem.security).equals(configSecurity)) {
				return config;
			}
		}
		return null;
	}
	/**
	 * 
	 *getWifiConfiguration(这里用一句话描述这个方法的作用）
	 *（从系统保存的信息中获取WifiConfiguration）
	 * @param wifiMgr
	 * @param wifiItem
	 * @param hotspotSecurity
	 * @return
	 *WifiConfiguration
	 * @exception
	 * @since 1.0.0
	 */
	public WifiConfiguration getWifiConfiguration(final WifiManager wifiMgr, final WifiItem wifiItem, String hotspotSecurity) {
		final String ssid = convertToQuotedString(wifiItem.ssid);
		if(ssid.length() == 0) {
			return null;
		}
		
		final String bssid = wifiItem.bssid;
		if(bssid == null) {
			return null;
		}
		
		if(hotspotSecurity == null) {
			hotspotSecurity = String.valueOf(wifiItem.security);
		}
		
		final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
		if(configurations == null) {
			return null;
		}

		for(final WifiConfiguration config : configurations) {
			if(config.SSID == null || !ssid.equals(config.SSID)) {
				continue;
			}
			if(config.BSSID == null || bssid.equals(config.BSSID)) {
				final String configSecurity = ConfigSec.getWifiConfigurationSecurity(config);
				if(hotspotSecurity.equals(configSecurity)) {
					return config;
				}
			}
		}
		return null;
	}
	
	public WifiConfiguration getWifiConfiguration(final WifiManager wifiMgr, final ScanResult hotsopt, String hotspotSecurity) {
		final String ssid = convertToQuotedString(hotsopt.SSID);
		if(ssid.length() == 0) {
			return null;
		}
		
		final String bssid = hotsopt.BSSID;
		if(bssid == null) {
			return null;
		}
		
		if(hotspotSecurity == null) {
			hotspotSecurity = ConfigSec.getScanResultSecurity(hotsopt);
		}
		
		final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
		if(configurations == null) {
			return null;
		}

		for(final WifiConfiguration config : configurations) {
			if(config.SSID == null || !ssid.equals(config.SSID)) {
				continue;
			}
			if(config.BSSID == null || bssid.equals(config.BSSID)) {
				final String configSecurity = ConfigSec.getWifiConfigurationSecurity(config);
				if(hotspotSecurity.equals(configSecurity)) {
					return config;
				}
			}
		}
		return null;
	}
	
	public  WifiConfiguration getWifiConfiguration(final WifiManager wifiMgr, final WifiConfiguration configToFind, String security) {
		final String ssid = configToFind.SSID;
		if(ssid.length() == 0) {
			return null;
		}
		
		final String bssid = configToFind.BSSID;

		
		if(security == null) {
			security = ConfigSec.getWifiConfigurationSecurity(configToFind);
		}
		
		final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();

		for(final WifiConfiguration config : configurations) {
			if(config.SSID == null || !ssid.equals(config.SSID)) {
				continue;
			}
			if(config.BSSID == null || bssid == null || bssid.equals(config.BSSID)) {
				final String configSecurity = ConfigSec.getWifiConfigurationSecurity(config);
				if(security.equals(configSecurity)) {
					return config;
				}
			}
		}
		return null;
	}
	
	public String convertToQuotedString(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        
        final int lastPos = string.length() - 1;
        if(lastPos > 0 && (string.charAt(0) == '"' && string.charAt(lastPos) == '"')) {
            return string;
        }
        
        return "\"" + string + "\"";
    }
   
	
	/**
	 * Change the password of an existing configured network and connect to it
	 * @param wifiMgr
	 * @param config
	 * @param newPassword
	 * @return
	 */
	public int changePasswordAndConnect(final Context ctx, final WifiManager wifiMgr, final WifiConfiguration config, final String newPassword, final int numOpenNetworksKept) {
		ConfigSec.setupSecurity(config, ConfigSec.getWifiConfigurationSecurity(config), newPassword);
		final int networkId = wifiMgr.updateNetwork(config);
		if(networkId == -1) {
			// Update failed.
			return -1;
		}
		// Force the change to apply.
		wifiMgr.disconnect();
		return connectToConfiguredNetwork(ctx, wifiMgr, config, true);
	}
	
	/**
	 * Configure a network, and connect to it.
	 * @param wifiMgr
	 * @param wifiItem
	 * @param password Password for secure network or is ignored.
	 * @return 返回networkid
	 */
	public int connectToNewNetwork(final Context ctx, final WifiManager wifiMgr, final WifiItem wifiItem, final String password, final int numOpenNetworksKept) {
		
		final String security = String.valueOf(wifiItem.security);
		
		if(ConfigSec.isOpenNetwork(security)) {
			checkForExcessOpenNetworkAndSave(wifiMgr, numOpenNetworksKept);
		}
		
		WifiConfiguration config = new WifiConfiguration();
		config.SSID  = WifiConnectUtil.convertToQuotedString(wifiItem.ssid);
		config.BSSID = wifiItem.bssid;
		ConfigSec.setupSecurity(config, security, password);
		
		
		int id = -1;
		try {
			id = wifiMgr.addNetwork(config);
		} catch(NullPointerException e) {
			Log.e("test", "Weird!! Really!! What's wrong??", e);
			// Weird!! Really!!
			// This exception is reported by user to Android Developer Console(https://market.android.com/publish/Home)
		}
		if(id == -1) {
			return id;
		}
		
		if(!wifiMgr.saveConfiguration()) {
			return -1;
		}
		
		config = getWifiConfiguration(wifiMgr, config, security);
		if(config == null) {
			return -1;
		}
		
		return connectToConfiguredNetwork(ctx, wifiMgr, config, true);
	}
	
	/**
	 * Configure a network, and connect to it.
	 * @param wifiMgr
	 * @param scanResult
	 * @param password Password for secure network or is ignored.
	 * @return
	 */
	public int connectToNewNetwork(final Context ctx, final WifiManager wifiMgr, final ScanResult scanResult, final String password, final int numOpenNetworksKept) {
		final String security = ConfigSec.getScanResultSecurity(scanResult);
		
		if(ConfigSec.isOpenNetwork(security)) {
			checkForExcessOpenNetworkAndSave(wifiMgr, numOpenNetworksKept);
		}
		
		WifiConfiguration config = new WifiConfiguration();
		config.SSID = convertToQuotedString(scanResult.SSID);
		config.BSSID = scanResult.BSSID;
		ConfigSec.setupSecurity(config, security, password);
		
		int id = -1;
		try {
			id = wifiMgr.addNetwork(config);
		} catch(NullPointerException e) {
			Log.e(TAG, "Weird!! Really!! What's wrong??", e);
			// Weird!! Really!!
			// This exception is reported by user to Android Developer Console(https://market.android.com/publish/Home)
		}
		if(id == -1) {
			return -1;
		}
		
		if(!wifiMgr.saveConfiguration()) {
			return -1;
		}
		
		config = getWifiConfiguration(wifiMgr, config, security);
		if(config == null) {
			return -1;
		}
		
		return connectToConfiguredNetwork(ctx, wifiMgr, config, true);
	}
	
	/**
	 * Connect to a configured network.
	 * @param wifiManager
	 * @param config
	 * @param numOpenNetworksKept Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT
	 * @return
	 */
	public int connectToConfiguredNetwork(final Context ctx, final WifiManager wifiMgr, WifiConfiguration config, boolean reassociate) {
		final String security = ConfigSec.getWifiConfigurationSecurity(config);
		int nResult = -1;
		int oldPri = config.priority;
		// Make it the highest priority.
		int newPri = getMaxPriority(wifiMgr) + 1;
		if(newPri > MAX_PRIORITY) {
			newPri = shiftPriorityAndSave(wifiMgr);
			config = getWifiConfiguration(wifiMgr, config, security);
			if(config == null) {
				return nResult;
			}
		}
		
		// Set highest priority to this configured network
		config.priority = newPri;
		int networkId = wifiMgr.updateNetwork(config);
		if(networkId == -1) {
			return nResult;
		}
		
		// Do not disable others
		if(!wifiMgr.enableNetwork(networkId, false)) {
			config.priority = oldPri;
			return nResult;
		}
		
		if(!wifiMgr.saveConfiguration()) {
			config.priority = oldPri;
			return nResult;
		}
		
		// We have to retrieve the WifiConfiguration after save.
		config = getWifiConfiguration(wifiMgr, config, security);
		if(config == null) {
			return nResult;
		}
		
		ReenableAllApsWhenNetworkStateChanged.schedule(ctx);
		
		// Disable others, but do not save.
		// Just to force the WifiManager to connect to it.
		if(!wifiMgr.enableNetwork(config.networkId, true)) {
			return nResult;
		}
		
		final boolean connect = reassociate ? wifiMgr.reassociate() : wifiMgr.reconnect();
		if(!connect) {
			return nResult;
		}
		
		return networkId;
	}
	
	/**
	 * 
	 *connNewNetwork(这里用一句话描述这个方法的作用）
	 *（连接到指定的wifi上面）
	 * @param context
	 * @param wifiItem
	 *void
	 * @exception
	 * @since 1.0.0
	 */
	private int connNewNetwork(Context context, WifiItem wifiItem,WifiManager wifiManager) {
		int connResult = -1;
		boolean mIsOpenNetwork = false;
		//判断是否为开放网络
		if (ConfigSec.isOpenNetwork(String.valueOf(wifiItem.security))) {
			mIsOpenNetwork = true;
		}				
		if (mIsOpenNetwork) {
			connResult = connectToNewNetwork(context, wifiManager, wifiItem,
					null, mNumOpenNetworksKept );
		} else {
			connResult = connectToNewNetwork(context, wifiManager, wifiItem,
					wifiItem.password, mNumOpenNetworksKept);
		}			

		if (-1 == connResult) {
			CommonUtil.sendToast(context, context.getResources().getString(R.string.toastWifiConnFailed));
		}
		return connResult;
	}
	
	public void connect(WifiItem item,WifiManager wifiManager,Context context) {
		Log.i("test", "item:" + item.security);
		Log.i("test", "item:" + item);
		AppConfig.getInstance().setmCurrentActivateWifi(item);
		//检查一下是否系统有保存连接信息
		WifiConfiguration itemConfig = getWifiConfiguration(wifiManager,
				item, String.valueOf(item.security));
		int connResult = -1;
		if (itemConfig == null) {
			connResult = connNewNetwork(context, item,wifiManager);
		} else {
			//首先判断目前连接的是否是将要连接的网络
			final boolean isCurrentNetwork_ConfigurationStatus = itemConfig.status == WifiConfiguration.Status.CURRENT;
			final WifiInfo info = wifiManager.getConnectionInfo();
			final boolean isCurrentNetwork_WifiInfo = info != null
					&& android.text.TextUtils.equals(info.getSSID(), item.ssid)
					&& android.text.TextUtils.equals(info.getBSSID(),
							item.bssid);
			if (isCurrentNetwork_ConfigurationStatus || isCurrentNetwork_WifiInfo) {
				// TODO disconnect
			} else {//准备连接新的网络
//				int nConnectId = connConfigNetwork(WifiStateService.getInstance(), item);
				int nConnectId = changePasswordAndConnect(context, wifiManager, itemConfig, item.password, mNumOpenNetworksKept);
			}
		}
	}

}
