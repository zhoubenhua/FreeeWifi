package com.zhephone.hotspoti.app.bean;

import java.util.List;

public class NearGroupList {
	private List<NearGroup> devices;
	private List<NearGroup> allchannel;
	
	public List<NearGroup> getAllchannel() {
		return allchannel;			
	}				

	public void setAllchannel(List<NearGroup> allchannel) {
		this.allchannel = allchannel;			
	}

	public List<NearGroup> getDevices() {
		return devices;
	}

	public void setDevices(List<NearGroup> devices) {
		this.devices = devices;
	}
	
}
