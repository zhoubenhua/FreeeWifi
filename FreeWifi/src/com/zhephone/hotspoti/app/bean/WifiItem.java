package com.zhephone.hotspoti.app.bean;
import java.io.Serializable;
import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;

import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.common.WifiConnectUtil;

public class WifiItem implements   Parcelable,Serializable {
	
	
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0 
	 */
	private transient static final long serialVersionUID = 1L;
	public transient static final int WIFI_RESOURCE_SCAN = 0;
	public transient static final int WIFI_RESOURCE_LIBRARY = 1;
	public transient static final int WIFI_RESOURCE_CLOUDY = 2;
	public transient static final int WIFI_RESOURCE_REMOVED = 3;
	public transient static final int WIFI_RESOURCE_CHECK_COMPLETED = 4;



	public String ssid;
	public String hotpotName;
	public String bssid;
	public String commentsLevel;
	public int mRssi  = -1;
	public String address;
	public String password;
	public int ipAddress;
	public int security = WifiConnectUtil.SECURITY_NONE;
	private int connectStatu;
	private String ssidName;
	/**
	 * 0:家庭热点
	 * 1.公司热点
	 * 2.公共热点
	 * 3.其他热点
	 */


	public transient int networkId;

	
//	public boolean bInScanned = false;
//	public int hotpotResource = WIFI_RESOURCE_SCAN;

	public WifiItem() {
		
	}
	
	
	
	public int getSecurity() {
		return security;
	}



	public void setSecurity(int security) {
		this.security = security;
	}



	public int getConnectStatu() {
		return connectStatu;
	}



	public void setConnectStatu(int connectStatu) {
		this.connectStatu = connectStatu;
	}
	
	


	public String getSsidName() {
		return ssidName;
	}



	public void setSsidName(String ssidName) {
		this.ssidName = ssidName;
	}

	

	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public WifiItem(ScanResult result) {
		ssid = WifiConnectUtil.removeDoubleQuotes(result.SSID);
		bssid = result.BSSID;
		security = WifiConnectUtil.getSecurity(result);
		networkId = -1;
		mRssi = result.level;
		commentsLevel = "3";
		connectStatu = Constant.WifiIntentDef.WIFI_CONNECT_FAILED;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * hotpotName
	 *
	 * @return the hotpotName
	 * @since 1.0.0
	 */
	public String getHotpotName() {
		return hotpotName;
	}



	/**
	 * @param hotpotName the hotpotName to set
	 */
	public void setHotpotName(String hotpotName) {
		this.hotpotName = hotpotName;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.ssid);
		dest.writeString(this.hotpotName);
		dest.writeString(this.bssid);
		dest.writeString(this.commentsLevel);
		dest.writeInt(this.mRssi);
		dest.writeString(this.address);
		dest.writeString(this.password);
		dest.writeInt(this.ipAddress);
		dest.writeInt(this.security);
		dest.writeString(this.ssidName);
		dest.writeInt(this.connectStatu);
	}

	public static final Parcelable.Creator<WifiItem> CREATOR = new Creator<WifiItem>() {

		@Override
		public WifiItem createFromParcel(Parcel source) {
			WifiItem item = new WifiItem();
			item.ssid = source.readString();
			item.hotpotName = source.readString();
			item.bssid = source.readString();
			item.commentsLevel = source.readString();
			item.mRssi = source.readInt();
			item.address = source.readString();
			item.password = source.readString();
			item.ipAddress = source.readInt();
			item.security = source.readInt();
			item.ssidName = source.readString();
			item.connectStatu = source.readInt();
			return item;
		}

		@Override
		public WifiItem[] newArray(int size) {
			return new WifiItem[size];
		}

	};
}
