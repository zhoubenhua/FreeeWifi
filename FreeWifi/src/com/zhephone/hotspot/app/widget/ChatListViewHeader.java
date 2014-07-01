package com.zhephone.hotspot.app.widget;
import com.zhephone.hotspot.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;




public class ChatListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ProgressBar mProgressBar;
	private int mState = STATE_NORMAL;


	

	
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;

	public ChatListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ChatListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.chatlistview_head, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mProgressBar = (ProgressBar)findViewById(R.id.chatlistview_head_progressbar);
		
	}
	
	public void hideProgressBar() {
		mProgressBar.setVisibility(View.INVISIBLE);
	}

//	public void setState(int state) {
//		if (state == mState) return ;
//		
//		if (state == STATE_REFRESHING) {	// 显示进度
//			mProgressBar.setVisibility(View.VISIBLE);
//		} else {	// 显示箭头图片
//			mProgressBar.setVisibility(View.INVISIBLE);
//		}
//		mState = state;
//	}
	
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	
	public void setFullVisiable(){
		mContainer.getLayoutParams().height = LinearLayout.LayoutParams.FILL_PARENT;
	}
	
	
	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}
