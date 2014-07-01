package com.zhephone.hotspoti.app.bean;

import java.util.List;

public class ChatGroupList {
	private int total;
	private List<ChatGroup> userDevices;
	private List<ChatGroup> channels;
	

	public List<ChatGroup> getChannels() {
		return channels;
	}

	public void setChannels(List<ChatGroup> channels) {
		this.channels = channels;
	}

	public List<ChatGroup> getUserDevices() {
		return userDevices;
	}

	public void setUserDevices(List<ChatGroup> userDevices) {
		this.userDevices = userDevices;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	

}
