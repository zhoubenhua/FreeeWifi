package com.zhephone.hotspot.app;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.common.LogUtil;
import com.zhephone.hotspot.app.db.ChatMessageDao;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspoti.app.bean.ChatMessage;
import com.zhephone.hotspoti.app.bean.ChatMessageList;
import com.zhephone.hotspoti.app.bean.ChatNotification;

import android.app.IntentService;
import android.content.Intent;

/**
 * 实时将经纬度传给服务器
 * @author Administrator
 *
 */
public class UploadLatLonService extends IntentService {
	private String action;
	
    public UploadLatLonService() {
		super("UploadLatLonService");
	}
    

    @Override
    public void onCreate() {
        super.onCreate();
    }


	@Override
	protected synchronized void onHandleIntent(Intent intent) {
		action = intent.getStringExtra("action");
		if(action.equals("upload_gps")) {
			uploadGps(intent);
		}
		
	}
	
	private void uploadGps(Intent intent) {
		String lat = intent.getStringExtra(Constant.HotIntentDef.GPS_LAT);
		String lon = intent.getStringExtra(Constant.HotIntentDef.GPS_LON);
		if(StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lon)) {
			RequestParams paramMap = new RequestParams();
			paramMap.put("latitude", lat);
			paramMap.put("longitude", lon);
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.UPLOAD_LAT_LON,paramMap, new CallbackListener() {
				public void onSuccess(String data) {	}

				public void onError(String error) { }
			});
			api.execute();
		}
		
	}

}
