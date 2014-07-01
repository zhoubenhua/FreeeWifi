package com.zhephone.hotspoti.app.bean;

import java.util.List;

public class VideoCategoryList {
	private List<VideoCategory> list;
	private List<VideoInfo> recommends;
	
	

	public List<VideoInfo> getRecommends() {
		return recommends;
	}

	public void setRecommends(List<VideoInfo> recommends) {
		this.recommends = recommends;
	}

	public List<VideoCategory> getList() {
		return list;
	}

	public void setList(List<VideoCategory> list) {
		this.list = list;
	}
	

}
