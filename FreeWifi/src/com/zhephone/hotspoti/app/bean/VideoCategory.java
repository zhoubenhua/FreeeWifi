package com.zhephone.hotspoti.app.bean;

import java.util.List;

public class VideoCategory {
	private List<VideoInfo> content;
	private String name;
	public List<VideoInfo> getContent() {
		return content;
	}
	public void setContent(List<VideoInfo> content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
