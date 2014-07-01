package com.zhephone.hotspot.app;
import com.zhephone.hotspot.R;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;			
import android.widget.Toast;

public class BaseActivity extends Activity {
	private ProgressDialog loadingProgressDialog;

	
	/**
	 * 隐藏键盘
	 */
	 public void keyBoardCancle() {
		 View view = getWindow().peekDecorView();
	     if (view != null) {
	    	 InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		     inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		 }
	 }	
	 
	 
	/**
	 * 左右推动跳转
	 * 
	 * @param mActivity
	 * @param intent
	 */
	 protected void startActivityLeft(Intent intent) {
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	 protected void back() {
		 finish();
		 overridePendingTransition(R.anim.back_push_left_in, R.anim.back_push_left_out);//左右
	 }
	/**
	 * 退出应用
	 */
//	public void exitApp(){
//		AppManager.getAppManager().AppExit(this.getApplicationContext());
//    }
	
	protected ProgressDialog showProgressDialog(String title, String message){
		try{
			if (isFinishing() == false){
				if(loadingProgressDialog == null){
					loadingProgressDialog = ProgressDialog.show(this, title, message);
					loadingProgressDialog.setCancelable(true);
					loadingProgressDialog.setCanceledOnTouchOutside(true);
				}else{
					loadingProgressDialog.setTitle(title);
					loadingProgressDialog.setMessage(message);
					if(loadingProgressDialog.isShowing() == false){
						loadingProgressDialog.show();
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return loadingProgressDialog;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		// 结束Activity&从堆栈中移除
//		AppManager.getAppManager().finishActivity(this);
//	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
		JPushInterface.onResume(this);
		
	}

	protected void dissmissProgressDialog() {
		try{
			if (isFinishing() == false && loadingProgressDialog != null) {
				loadingProgressDialog.dismiss();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}



//	public void finish() {
//		activityManager.removeActivity(this);
//		super.finish();
//	}

	protected void setUpViews() {
	    AppManager.getAppManager().addActivity(this);
		this.initViews();
		this.addListener();
		this.initData();
	}

	protected void initViews() {

	}
	

	protected void initData() {

	}

	protected void addListener() {

	}
	protected void showShortToast(int pResId) {
		showShortToast(getString(pResId));
	}

	protected void showLongToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
	}

	protected void showShortToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}

}
