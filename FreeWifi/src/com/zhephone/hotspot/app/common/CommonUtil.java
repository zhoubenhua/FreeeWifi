package com.zhephone.hotspot.app.common;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.zhephone.hotspot.app.AppConfig;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.widget.Toast;

public class CommonUtil {

	/**
	 * Toast
	 * 
	 * @param mContext
	 * @param text
	 */
	public static void sendToast(Context mContext, String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}		
	
	
	/**
	 * 验证手机号
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles){          		  
		return mobiles.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");       
	} 

	/**
	 * 
	 * 
	 * @param text
	 * @return
	 */
	public static SpannableString strickout(String text) {
		SpannableString ss = new SpannableString(text);
		ss.setSpan(new StrikethroughSpan(), 0, ss.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}
	
	public static String getImei(Context context, String imei) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imei;
	}

	
	/**
	 * 检查网络是否连接上
	 * @param mContext
	 * @return
	 */
	public static boolean checkNetwork(Context mContext){
		ConnectivityManager cwjManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		boolean flag = info != null && info.isAvailable() ? true : false;
		if(!flag) {	
			CommonUtil.sendToast(mContext, "请检查你的网络");
		}
		return flag;
	}
	
	/**
	 * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
     * 计算两点之间距离
     * @param longt1
     * @param lat1
     * @param longt2
     * @param lat2
     * @return
     */		
    public static int getDistance(String lng1Str, String lat1Str, String lng2Str,String lat2Str) {
    	if(!StringUtils.isNotBlank(lng2Str) || !StringUtils.isNotBlank(lat2Str)
    			|| !StringUtils.isNotBlank(lng1Str) || !StringUtils.isNotBlank(lat1Str)) {
    		return 0;
    	} else {					
        	//地球半径
        	double EARTH_RADIUS = 6378137.0; 
        	double s = 0;
    		try {  
    			Double lng2;
    			Double lat2;
    			Double lng1;
    			Double lat1;
    			lng1 = Double.parseDouble(lng1Str);
    			lat1 = Double.parseDouble(lat1Str);
    			lng2 = Double.parseDouble(lng2Str);
    			lat2 = Double.parseDouble(lat2Str);
    			double a,b;
    			double radLat1 = (lat1 * Math.PI / 180.0);
    			double radLat2 = (lat2 * Math.PI / 180.0);
    			a = radLat1 - radLat2;
    			b = (lng1 - lng2) * Math.PI / 180.0;
    			s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2), 2)+ Math.cos(radLat1) * Math.cos(radLat2)*Math.pow(Math.sin(b/2), 2)));
    			s = s * EARTH_RADIUS;  //单位为m
    			s = Math.round(s * 10000) / 10000;
    		} catch (Exception e) {
    			// TODO: handle exception
    			e.printStackTrace();
    		}
    		return (int)s;
    	}
    }
	
	/**
     * 获取外置SD卡路径
     * 
     * @return
     */
    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
            		Log.i("CommonUtil:getSDCardPath", lineStr);
                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                    Log.e("CommonUtil:getSDCardPath", "命令执行失败!");
                }
            }
            inBr.close();
            in.close();
            
        } catch (Exception e) {
            Log.e("CommonUtil:getSDCardPath", e.toString());

            return Environment.getExternalStorageDirectory().getPath();
        }

        return Environment.getExternalStorageDirectory().getPath();
    }
    
    
    /**
     *判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        String appPackageName = AppConfig.getInstance().getAppPackageName();
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if(StringUtils.isNotEmpty(appPackageName)) {
                if (!topActivity.getPackageName().equals(appPackageName)) {
                    return true;
                }
            }
        }
        return false;

    }




}
