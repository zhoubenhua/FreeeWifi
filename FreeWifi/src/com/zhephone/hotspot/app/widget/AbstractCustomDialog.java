
package com.zhephone.hotspot.app.widget;

import com.zhephone.hotspot.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
public abstract class AbstractCustomDialog extends Dialog {

	protected Context context;
	protected String title;
	protected String message;
	
	protected TextView titleTextView;
	protected TextView messageTextView;
	protected Button sureButton;
	protected Button cancelButton;
	
	private boolean is_ok_btn_visible = true;
	private boolean is_cancel_btn_visible = true;

	public AbstractCustomDialog(Context context, String title,
			String message, SureOnClickListener listener,
			CancelOnClickListener cancelListener,
			boolean is_ok_btn_visible, boolean is_cancel_btn_visible) {
		super(context, R.style.dialog);
		this.context = context;
		this.title = title;
		this.message = message;			
		this.listener = listener;
		this.cancelListener = cancelListener;
		this.is_ok_btn_visible = is_ok_btn_visible;
		this.is_cancel_btn_visible = is_cancel_btn_visible;
	}

	public AbstractCustomDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
	}
	

	protected abstract int getDialogContentId();
	protected abstract int initComponent();
	
	 public void setSureText(String sureTxt)
	 {
		 sureButton.setText(sureTxt);
	 }
	
	 public void setCancelText(String sureTxt)
	 {
		 cancelButton.setText(sureTxt);
	 }
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.5f;
		getWindow().setAttributes(lp);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		//这句必须加上，要不然dialog上会有一段空白（标题栏）
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(getDialogContentId());

		titleTextView = (TextView) findViewById(R.id.title);
		messageTextView = (TextView) findViewById(R.id.message);
		sureButton = (Button) findViewById(R.id.yes);
		cancelButton = (Button) findViewById(R.id.no);
		if (!is_ok_btn_visible) {
			sureButton.setVisibility(View.GONE);
		}
		if (!is_cancel_btn_visible) {
			cancelButton.setVisibility(View.GONE);
		}

		if(titleTextView != null){
			titleTextView.setText(title);
		}
		if(messageTextView != null){
			messageTextView.setText(message);
		}

		sureButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onClick();
				}
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
				if (cancelListener != null) {
					cancelListener.onClick();
				}
			}
		});
		this.initComponent();
	}

	private SureOnClickListener listener;
	private CancelOnClickListener cancelListener;

	public interface SureOnClickListener {
		public void onClick();
	}

	public interface CancelOnClickListener {
		public void onClick();
	}

	public void setSureOnClickListener(SureOnClickListener l) {
		listener = l;
	}

	public void setCancelOnClickListener(CancelOnClickListener l) {
		cancelListener = l;
	}

}
