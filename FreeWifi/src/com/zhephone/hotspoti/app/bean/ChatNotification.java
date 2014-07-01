package com.zhephone.hotspoti.app.bean;

import java.io.Serializable;

public class ChatNotification implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5188532702760018394L;
	//{"sendUdid":"a977b2ba3ff68f94","sendNo":1073409420,"imei":"re1342re3e6r6365df1"}
	//private String sendUdid;
	private String sendNo;
	private String imei;		
	private String name;
	private String id;
//	public String getSendUdid() {
//		return sendUdid;
//	}
//	public void setSendUdid(String sendUdid) {
//		this.sendUdid = sendUdid;
//	}	
	
	public String getSendNo() {
		return sendNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSendNo(String sendNo) {
		this.sendNo = sendNo;				
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	

}
