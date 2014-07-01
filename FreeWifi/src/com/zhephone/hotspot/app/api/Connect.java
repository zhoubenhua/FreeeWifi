package com.zhephone.hotspot.app.api;

public class Connect {
	public static boolean isLocal=false;
	private static Connect connect;
	/**
	 * 后台服务器地址
	 */
	private String apiUrl;
	/**
	 * 视频服务器地址
	 */
	private String movieUrl;
	public static Connect getInstance(){
		if(connect==null){
			connect =new Connect();
		}  
		/**
		 * 判断是否是线下, 默认是线下
		 */
		if(isLocal){					
			/**
			 * 配置线下服务器地址
			 */
			connect.setApiUrl("http://192.168.1.152:10010/");
			connect.setMovieUrl("http://192.168.43.1:8080/");
		}else{
			/**
			 * 配置线上服务器地址
			 */
			connect.setApiUrl("http://api.shmaker.cn:8088/");
			connect.setMovieUrl("http://192.168.43.1:8080/");
		}
		return connect;
	}
	
	public String getMovieUrl() {
		return movieUrl;
	}

	public void setMovieUrl(String movieUrl) {
		this.movieUrl = movieUrl;
	}

	public String getApiUrl() {
		return apiUrl;
	}
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	
	
	

}
