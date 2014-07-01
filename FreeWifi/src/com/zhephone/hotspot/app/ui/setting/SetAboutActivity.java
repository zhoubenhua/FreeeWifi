package com.zhephone.hotspot.app.ui.setting;

import org.apache.commons.lang.StringUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.ui.WelcomeActivity.NetErrorCallback;
import com.zhephone.hotspot.app.widget.AppUpdateDialog;
import com.zhephone.hotspot.app.widget.Topbar;
import com.zhephone.hotspot.app.widget.AppUpdateDialog.AppUpdateListener;
import com.zhephone.hotspoti.app.bean.VersionUpdate;

public class SetAboutActivity extends BaseActivity implements OnClickListener {
	private TextView set_about_version;
	private TextView versionCodeTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_about);
		setUpViews();
	}
	
	
	@Override
	protected void initViews() {
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		topbar.setToolbarCentreText(getResources()
				.getString(R.string.set_about));
		topbar.getLeftBt().setOnClickListener(this);
		set_about_version = (TextView) findViewById(R.id.set_about_version);
		versionCodeTv = (TextView)findViewById(R.id.version_code_tv);
	}

	@Override
	protected void addListener() {
		findViewById(R.id.set_about_updata).setOnClickListener(this);
		findViewById(R.id.set_about_sina).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		versionCodeTv.setText("版本号"+getVersionCode());
		set_about_version.setText(getVersionCode());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_left_bt:
			back();
			break;
		case R.id.set_about_updata:// 版本更新处理
			// showShortToast("版本更新处理");
			String userDevice = AppConfig.getInstance().getDeviceToken();
			checkVersionUpdate(getVersionCode(), userDevice);
			break;
		case R.id.set_about_sina:// 新浪分享
			// showShortToast("新浪分享");
			break;
		default:
			break;
		}

	}
	public String getVersionCode() {
		String str = "";
		try {
			str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName
					+ "";
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 弹出对话框,确认是否要更新
	 */
	private void confirmUpdateDialog(final String downloadUrl) {
		// 构造对话框
		AlertDialog.Builder builder = new Builder(SetAboutActivity.this);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(R.string.soft_update_info);
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 显示下载对话框
						new AppUpdateDialog(SetAboutActivity.this,
								appUpdateListener, downloadUrl);
					}
				});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		Dialog noticeDialog = builder.create();
		noticeDialog.setCanceledOnTouchOutside(false);
		noticeDialog.show();
	}

	private void checkVersionUpdate(final String version,
			final String userDevice) {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			// showProgressDialog(null,
			// getResources().getString(R.string.loading));
			showProgressDialog(null,
					getResources().getString(R.string.doing));
			RequestParams paramMap = new RequestParams();
			paramMap.put("v", version);
			paramMap.put("udid", userDevice);
			// paramMap.put("secret",
			// AppConfig.getInstance().getUserInfo().getSecret());
			final APIAsyncTask api = new APIAsyncTask();
			api.get(Protocol.APP_UPDATE, paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					dissmissProgressDialog();
					if (data != null) {
						checkVersionUpdateSucess(data);
					}else {
						handler.sendEmptyMessage(msg_error);
					}
				}

				public void onError(String error) {
					dissmissProgressDialog();
					handler.sendEmptyMessage(msg_error);
				}
			});
			api.execute();
		}
	}
	private static final int msg_error=1;
	private static final int msg_updata=2;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case msg_updata:
				Toast.makeText(SetAboutActivity.this, "已是最新版本", 2000).show();
				break;
			case msg_error:
				Toast.makeText(SetAboutActivity.this, "请稍后在试", 2000).show();
				break;

			default:
				break;
			}
		
		};
	};
	private void checkVersionUpdateSucess(String data) {
		try {
			VersionUpdate versionUpdate = JSON.parseObject(data,
					VersionUpdate.class);
			if (StringUtils.isNotEmpty(versionUpdate.getDownloadUrl())) {
				confirmUpdateDialog(versionUpdate.getDownloadUrl());
			} else {
				handler.sendEmptyMessage(msg_updata); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(msg_error);
		}
	}

	private AppUpdateListener appUpdateListener = new AppUpdateListener() {

		@Override
		public void cancelUpdate() {
		}

		@Override
		public void updateError(String error) {
		}

	};

	public static void start(Context context) {
		context.startActivity(new Intent(context, SetAboutActivity.class));
	}
}
