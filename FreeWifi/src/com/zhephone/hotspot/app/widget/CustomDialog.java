package com.zhephone.hotspot.app.widget;
import com.zhephone.hotspot.R;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
public class CustomDialog extends AbstractCustomDialog {


	public CustomDialog(Context context, String title,
			String message, SureOnClickListener listener,
			CancelOnClickListener cancelListener,
			boolean is_ok_btn_visible, boolean is_cancel_btn_visible) {
		super(context,title,message,listener,cancelListener,is_ok_btn_visible,is_cancel_btn_visible);
		
	}

	public CustomDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

    public void setTitle(String title)
    {
    	this.title = title;
    	if(this.titleTextView!=null)
    	{
    		titleTextView.setText(title);
    	}
    }
    
    public void setMessage(String message)
    {
    	this.message = message;
    	if(this.messageTextView!=null)
    	{
    		messageTextView.setText(message);
    	}
    }
    
   
	@Override
	protected int getDialogContentId() {
		return R.layout.custom_dialog;
	}

	@Override
	protected int initComponent() {
		messageTextView = (TextView) findViewById(R.id.message);
		messageTextView.setText(message);
		return 0;
	}

}
