package com.zhephone.hotspot.app.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.widget.Topbar;

public class SetVersionInfoActivity extends BaseActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_versioninfo);
		setUpViews();
	}

	@Override
	protected void initViews() {
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		topbar.setToolbarCentreText(getResources()
				.getString(R.string.set_versioninfo_title));
		topbar.getLeftBt().setOnClickListener(this);
	}
	
	public static void start(Context context) {
		context.startActivity(new Intent(context, SetVersionInfoActivity.class));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_left_bt:
			finish();
			break;
		default:
			break;
		}

	}
}
