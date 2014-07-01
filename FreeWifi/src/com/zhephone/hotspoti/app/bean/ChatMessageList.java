package com.zhephone.hotspoti.app.bean;

import java.util.List;

public class ChatMessageList {
	private List<ChatMessage> messages;
	private List<ChatMessage> unreadMessages;
	private int totalCount;

	public List<ChatMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ChatMessage> messages) {
		this.messages = messages;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<ChatMessage> getUnreadMessages() {
		return unreadMessages;
	}

	public void setUnreadMessages(List<ChatMessage> unreadMessages) {
		this.unreadMessages = unreadMessages;
	}
	
	
}
