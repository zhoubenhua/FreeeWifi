package com.zhephone.hotspot.app.widget;
import com.zhephone.hotspot.R;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Topbar {
	private Button leftBt;
	private TextView titleTv;
	private Button rightBt;
	private TextView rightTitleTv;
	View view;
			
	public Topbar(View view) {
		this.view = view;
		initViews();
	}
	

	private void initViews() {
		leftBt = (Button)view.findViewById(R.id.topbar_left_bt);
		rightBt = (Button)view.findViewById(R.id.topbar_right_bt);
		titleTv = (TextView)view.findViewById(R.id.topbar_title_tv);
		rightTitleTv = (TextView)view.findViewById(R.id.topbar_right_title_tv);
	}

	public Button getLeftBt() {
		return leftBt;
	}
	
	public void setToolbarCentreText(String title) {
		titleTv.setText(title);
	}

	public TextView getTitleTv() {
		return titleTv;
	}

	public Button getRightBt() {
		return rightBt;
	}


	public TextView getRightTitleTv() {
		return rightTitleTv;
	}

	

}
