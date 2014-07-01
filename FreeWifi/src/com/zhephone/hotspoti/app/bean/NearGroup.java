package com.zhephone.hotspoti.app.bean;

public class NearGroup {
	private String deviceToken;
	private String longitude;
	private String latitude;
	private String oId;
	private String chatName;
	
	private String id;
	private int status;
	private String imei;
	private String name;
	
	public String getoId() {
		return oId;
	}

	public void setoId(String oId) {
		this.oId = oId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceToken() {
		return deviceToken;
	}
	
	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getOid() {
		return oId;
	}

	public void setOid(String oId) {
		this.oId = oId;
	}
	
	
}
