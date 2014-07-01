package com.zhephone.hotspot.app;
import java.util.Stack;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21			
 */
public class AppManager {
	
	private static Stack<Activity> activityStack;
	private static AppManager instance;
	//private static Activity mCurActivity;
	
	private AppManager(){
		activityStack=new Stack<Activity>();
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager(){
		if(instance==null){
			instance=new AppManager();
		}
		return instance;
	}
	public boolean isExitApp() {
		return activityStack.isEmpty() ? true: false;
	}
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity){
		
		activityStack.add(activity);
	}
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				/*
				 * 你不能在对一个List进行遍历的时候将其中的元素删除掉,一旦找到就停止遍历
				 */
				finishActivity(activity);
				break;
			}
		}
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			Intent intent = new Intent(context,UploadLatLonService.class);
			context.stopService(intent);
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}