/*
 * Wifi Connecter
 * 
 * Copyright (c) 20101 Kevin Yuan (farproc@gmail.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 **/ 
package com.zhephone.hotspot.app.common;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.text.TextUtils;



public class WifiConnectUtil {

	private static final int SIGNAL_LEVELS = 4;
	
    public static final int SECURITY_NONE = 0;
    public static final int SECURITY_WEP = 1;
    public static final int SECURITY_PSK = 2;
    public static final int SECURITY_EAP = 3;
    
    
    public static final int OPERATION_TYPE_CLOUDY_HOTPOT_ADD = 0;
    public static final int OPERATION_TYPE_CLOUDY_HOTPOT_MODIFY = 1;
    public static final int OPERATION_TYPE_CLOUDY_HOTPOT_DEL = 2;
	
//	public static void connectToNetwork(WifiHotspot wifiHotspot,WifiManager wifiManager) {
//		//Log.i(TAG, "* connectToAP");
//	    WifiConfiguration wifiConfiguration = new WifiConfiguration();
//	    String networkSSID = wifiHotspot.getWifiSsidName();
//	    String networkPass = wifiHotspot.getWifiPassword();
//	    String securityMode = wifiHotspot.getSecurityMode();
//	    if (securityMode.equalsIgnoreCase("OPEN")) {
//            wifiConfiguration.SSID = "\"" + networkSSID + "\"";
//            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            int res = wifiManager.addNetwork(wifiConfiguration);
//           // Log.d(TAG, "# add Network returned " + res);
//            boolean b = wifiManager.enableNetwork(res, true);
//            //Log.d(TAG, "# enableNetwork returned " + b);
//            wifiManager.setWifiEnabled(true);
//        } else if (securityMode.equalsIgnoreCase("WEP")) {
//            wifiConfiguration.SSID = "\"" + networkSSID + "\"";
//            wifiConfiguration.wepKeys[0] = "\"" + networkPass + "\"";
//            wifiConfiguration.wepTxKeyIndex = 0;
//            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//            int res = wifiManager.addNetwork(wifiConfiguration);
//            //Log.d(TAG, "### 1 ### add Network returned " + res);
//            boolean b = wifiManager.enableNetwork(res, true);
//           // Log.d(TAG, "# enableNetwork returned " + b);
//            wifiManager.setWifiEnabled(true);
//        }
//        wifiConfiguration.SSID = "\"" + networkSSID + "\"";
//        wifiConfiguration.preSharedKey = "\"" + networkPass + "\"";
//        wifiConfiguration.hiddenSSID = true;
//        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
//        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//
//        int res = wifiManager.addNetwork(wifiConfiguration);
//        //Log.d(TAG, "### 2 ### add Network returned " + res);
//
//        wifiManager.enableNetwork(res, true);
//        boolean changeHappen = wifiManager.saveConfiguration();
////    	if(res != -1 && changeHappen){
////            Log.d(TAG, "### Change happen");
////        } else{
////            Log.d(TAG, "*** Change NOT happen");
////        }
//        wifiManager.setWifiEnabled(true);
//	}
	
	public static String getScanResultSecurity(ScanResult scanResult) {
	    //Log.i(TAG, "* getScanResultSecurity");
	    final String cap = scanResult.capabilities;
	    final String[] securityModes = { "WEP", "PSK", "EAP" };
	    for (int i = securityModes.length - 1; i >= 0; i--) {
	        if (cap.contains(securityModes[i])) {
	            return securityModes[i];
	        }
	    }
	    return "OPEN";
	}
	

	public static int getLevel(int rssi) {
		if (rssi == Integer.MAX_VALUE) {
			return -1;
		}
		return WifiManager.calculateSignalLevel(rssi, SIGNAL_LEVELS);
	}
	
	public static int getSecurity(WifiConfiguration config){
		if(config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)){
			return SECURITY_PSK;
		}
		if(config.allowedKeyManagement.get(KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)){
			return SECURITY_EAP;
		}
		return (config.wepKeys[0] != null)?SECURITY_WEP : SECURITY_NONE;
	}
	
	public static int getSecurity(ScanResult result){
		if(result.capabilities.contains("WEP")){
			return SECURITY_WEP;
		}else if(result.capabilities.contains("PSK")){
			return SECURITY_PSK;
		}else if (result.capabilities.contains("EAP")){
			return SECURITY_EAP;
		}
		return SECURITY_NONE;
	}
	

	public static String removeDoubleQuotes(String ssid){
		if(TextUtils.isEmpty(ssid) == false){
			int length = ssid.length();
			if((length > 1) && (ssid.charAt(0) == '"') &&(ssid.charAt(length - 1) == '"')){
				return ssid.substring(1, length -1);
			}
		}
		return ssid;
	}
	
	public static String convertToQuotedString(String ssid){
		return "\"" + ssid + "\"";
	}

}
