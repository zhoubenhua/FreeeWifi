package com.zhephone.hotspot.app.api;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.common.LogUtil;
import com.zhephone.hotspot.app.common.TimeUtil;
import com.zhephone.hotspot.app.http.AsyncHttpClient;
import com.zhephone.hotspot.app.http.AsyncHttpResponseHandler;
import com.zhephone.hotspot.app.http.RequestParams;


public class APIAsyncTask {
	private CallbackListener listener;
	private String protocol;
	private boolean requestType;// true 为 get false 为post
	private AsyncHttpClient http;
	
	// private String sign;
	// private static APIAsyncTask asyncTask;
	public void get(String protocol, Object obj, CallbackListener listener) {
		this.listener = listener;
		this.protocol = protocol;
		this.requestType = true;
		convert(obj);
	}

	public void post(String protocol, Object obj, CallbackListener listener) {
		this.listener = listener;
		this.protocol = protocol;
		this.requestType = false;
		convert(obj);
	}

	public void execute() {

	}

	private AsyncHttpClient getAsyncHttp() {
		if (http == null) {
			http = new AsyncHttpClient();
			http.setTimeout(1000*10);
			//http.addHeader("Host", ConfigConnect.getConnect().getApiHost());
		}
		return http;
	}

	private void convert(Object obj) {
		long time = TimeUtil.getCurrentTime();
		StringBuffer params = new StringBuffer();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("deviceToken", AppConfig.getInstance().getDeviceToken());
		map.put("clientType", "1");
		map.put("timestamp", time + "");
		int i = 0;
		for (String key : map.keySet()) {
			params.append(i == 0 ? "" : "&");
			params.append(key + "=" + map.get(key));
			i++;
		}
		try {
			params.append(copy(obj)).toString();
			AsyncHttpClient client = this.getAsyncHttp();
			String str = requestType?"GET":"POST";
			String baseUrl ;
			/**
			 * 判断请求地址是不是视频或app,如果是就连接视频服务器
			 * 如果不是就连接后台服务器
			 */
			if(protocol.contains("shmk_service")) {
				baseUrl = Connect.getInstance().getMovieUrl();
			} else {
				baseUrl = Connect.getInstance().getApiUrl();
			}
			String url = baseUrl+protocol+"?"+params.toString();
			
			LogUtil.log("[---------send-----"+str+"----]:"+url);
			if(requestType) {
				client.get(url, new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						LogUtil.log("[---------result----GET-----]:"+response);
						requestFinished(response);
					}

					public void onFinish() {
						LogUtil.log("onFinish");
					}
					
					public void onFailure(Throwable error,
							String content) {
						error.printStackTrace();
						requestFailed(error,content);
						LogUtil.log("onFailure");
					}

					

				});

			} else {
				HttpEntity entity = new StringEntity( params.toString(), HTTP.UTF_8);  
				client.post(null, baseUrl
						+ protocol, entity, "application/x-www-form-urlencoded",
						new AsyncHttpResponseHandler() {
							public void onSuccess(String response) {
								LogUtil.log("[---------result----POST-----]:"+response);
								requestFinished(response);
							}

							public void onFinish() {
								LogUtil.log("onFinish");
							}

							public void onFailure(Throwable error,
									String content) {
								error.printStackTrace();
								requestFailed(error,content);
								LogUtil.log("onFailure");

							}

						});

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 处理服务器返回的json数据
	 */
	private void requestFinished(String result) {

		if (StringUtils.isNotBlank(result)) {

			try {
				JSONObject jsons = new JSONObject(result);
				int code = jsons.getInt("code");
				switch (code) {
				case 200:
					String body = jsons.get("data").toString();
					if (body.equals("{}")) {
						this.listener.onError("请求出错");
					} else {
						this.listener.onSuccess(body);
					}
					break;
				case 10003:
					this.listener.onError("用户名密码错误");
					break;
				case 500:
					this.listener.onError("服务器错误");
					break;
				case 404:
					this.listener.onError("未找到或请求方式不正确");
					break;
				case 502:
					this.listener.onError("网关超过");
					break;
				case 40301:
					this.listener.onError("授权失败");
					break;
				case 40302:
					this.listener.onError("请求授权失败");
					break;
				case 40303:
					this.listener.onError("请求参数有误");
					break;
				case 40304:
					this.listener.onError("请求权限有误");
					break;
				case 40305:
					this.listener.onError("请求参数类型有误，数字类型");
					break;
				case 40306:
					this.listener.onError("签名出错");
					break;
				case 40307:
					this.listener.onError("签名不匹配");
					break;
				case 40308:
					this.listener.onError("未提交授权");
					break;
				case 40309:
					this.listener.onError("请求参数类型有误，字符类型");
					break;
				case 40310:
					this.listener.onError("验证字符串不正确");
					break;
				case 60001:
					this.listener.onError("注册失败");
					break;
				case 60002:
					this.listener.onError("账号无效");
					break;
				case 60003:
					this.listener.onError("密码无效");
					break;
				case 60004:
					this.listener.onError("账号锁定");
					break;
				case 60005:
					this.listener.onError("账号已存在");
					break;
				case 60006:
					this.listener.onError("邮件已存在");
					break;
				case 60007:
					this.listener.onError("验证字符串状态更新失败");
					break;
				case 60008:
					this.listener.onError("密码更新失败");
					break;
				case 60038:
					this.listener.onError("没有好友");
					break;
				case 60032:
					this.listener.onError("数据权限有误");
					break;
				case 60027:
					this.listener.onError("团购数据不存在");
					break;
				case 60021:
					this.listener.onError("数据不存在");
					break;
				case 10001:
					this.listener.onError("用户已经存在");
					break;
				default:
					String msg = jsons.get("msg").toString();
					if(StringUtils.isNotEmpty(msg)) {
						this.listener.onError(msg);
					} else {
						this.listener.onError("其他错误"); 
					}
					break;
				}

			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	/**
	 * ����ʧ��
	 */
	private void requestFailed(Throwable error,String content) {
		LogUtil.log("error:"+error.getMessage()+":"+content);
		if(error instanceof SocketTimeoutException || error instanceof ConnectTimeoutException || error instanceof ConnectException || error instanceof InterruptedIOException){
			this.listener.onError("服务器连接超时");
		}else if(error instanceof SocketException){
			this.listener.onError("服务器连接失败");
		}else if(error instanceof HttpResponseException){
			this.listener.onError("服务器错误");
		}else{
			this.listener.onError("服务器连接失败");
		}
		
	}
	
	private String copy(Object obj) throws IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Class<?> classType = obj.getClass();
		if(classType.equals(RequestParams.class)){
			
			return "&"+obj.toString();
		}else{
			
			Object objectCopy = classType.getConstructor(new Class[] {})
					.newInstance(new Object[] {});
			Field[] fields = classType.getDeclaredFields();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				if (fieldName.equals("serialVersionUID")) {
					continue;
				}
				String stringLetter = fieldName.substring(0, 1).toUpperCase();
				String getName = "get" + stringLetter + fieldName.substring(1);
				String setName = "set" + stringLetter + fieldName.substring(1);
				Method getMethod = classType.getMethod(getName, new Class[] {});
				Method setMethod = classType.getMethod(setName,
						new Class[] { field.getType() });
				Object value = getMethod.invoke(obj, new Object[] {});
				if (value == null)
					value = "";
				sb.append("&" + fieldName + "=" + value);
				setMethod.invoke(objectCopy, new Object[] { value });
			}
			return sb.toString();
		
		}
	}
	
//	private String copy(Map<String, String> map)  {
//		StringBuffer sb = new StringBuffer();
//		for (String key : map.keySet()) {
//			sb.append("&" + key + "=" + map.get(key));
//		}
//		return sb.toString();
//
//	}

//	private String copyEncode(Map<String, String> map) {
//		StringBuffer sb = new StringBuffer();
//		for (String key : map.keySet()) {
//			sb.append("&" + key + "=" + URLEncoder.encode(map.get(key)));
//		}
//		return sb.toString();
//
//	}

}
