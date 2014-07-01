package com.zhephone.hotspot.app.api;

public class Protocol {

	// <!--��ȡtokenֵ-->
	public static String TOKEN = "phone/default/auth";
	/**
	 * 用户登录
	 */
	public static String LOGIN = "api/user/login";
	/**
	 * 用户注册   

	 */ 
	public static String REGISTER = "api/user/registerChatUser";
	/**
	 * 验证手机
	 */
	public static String VERIFICATION_PHONE = "api/user/sendValidateCode";
	/**
	 * 聊天列表
	 */
	public static String CHAT_LIST = "message/getChatList";	
	/**
	 *未读聊天信息列表
	 */
	public static String NO_READ_CHAT_LIST = "api/jpush/talk/getUnreadMessage";	
	/**
	 * 发送信息										
	 */
	public static String SEND_MESSAGE = "api/jpush/talk/send";	
	/**
	 * 附近热点
	 */
	public static String NEAR_GROUP = "nearby/listDeviceAll";
	/**
	 * 上传经纬度
	 */
	public static String UPLOAD_LAT_LON = "nearby/update";
	/***
	 * 视频分类列表
	 */
	public static String VIDEO_CATEGORY_LIST = "shmk_service/api/MovServlet";
	/**
	 * app列表
	 */
	public static String APP_LIST = "shmk_service/api/AppServlet";
	/**
	 * 用户信息
	 */
	public static String USER_INFO = "user/info";
	/**
	 * 退出
	 */
	public static String LOGOUT_OUT = "user/login_out";
	/**
	 * 意见反馈
	 */
	public static String FEEDBACK = "api/feedback/commit";
	/**
	 * 更新群组id
	 */
	//public static String UPDATE_GROUP_ID = "user/saveOrUpdateUserDevices";
	public static String UPDATE_GROUP_ID = "api/channel/updateuserchannel";

	/**
	 * 聊天群组列表
	 */
	public static String CHAT_GROUP_LIST = "user/listUserDevices";
	/**
	 * 版本更新
	 */
	public static String APP_UPDATE = "api/version/getversion";
	/**
	 * 注册用户通道
	 */
	public static String REGISTER_CHAT = "api/user/register";
	/**
	 * 用户所在的所有组
	 */
	public static String ALL_GROUP_BY_USERID = "api/channel/userchannels";
	/**
	 * 所有的组
	 */
	public static String ALL_GROUP = "api/channel/allchannels";
}
