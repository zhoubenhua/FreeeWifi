package com.zhephone.hotspot.app.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.common.TimeUtil;
import com.zhephone.hotspot.app.widget.DownloadImageView;
import com.zhephone.hotspoti.app.bean.ChatMessage;


public class ChatListAdapter extends BaseAdapter {
	
	public static interface IMsgViewType
	{
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}
    private List<ChatMessage> chatMessageList;
    private LayoutInflater mInflater;
    private String userHeadUrl;
    private String userName;
    
    public ChatListAdapter(Context context, List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
        mInflater = LayoutInflater.from(context);
        userHeadUrl = AppConfig.getInstance().getUserInfo().getUserIconUrl();
    }
    
    

    public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public int getCount() {
        return chatMessageList.size();
    }

    public Object getItem(int position) {
        return  chatMessageList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
   
	
    public View getView(int position, View convertView, ViewGroup parent) {	
    	if(convertView == null) {										
			convertView =  mInflater.inflate(R.layout.chat_item, null); 		 			
    	}
    	LinearLayout leftDialogLl = (LinearLayout)convertView.findViewById(R.id.chat_item_left_ll);
		LinearLayout rightDialogLl = (LinearLayout)convertView.findViewById(R.id.chat_item_right_ll);
		//DownloadImageView friendHeadIv = (DownloadImageView)convertView.findViewById(R.id.iv_userhead_left);
		TextView tvSendTimeLeft = (TextView) convertView.findViewById(R.id.tv_sendtime_left);
		TextView tvUserNameLeft = (TextView) convertView.findViewById(R.id.tv_username_left);
		Button btContentLeft = (Button) convertView.findViewById(R.id.bt_chatcontent_left);
		TextView tvSendTimeRight= (TextView) convertView.findViewById(R.id.tv_sendtime_right);
		TextView tvUserNameRight = (TextView) convertView.findViewById(R.id.tv_username_right);
		Button btContentRight = (Button) convertView.findViewById(R.id.bt_chatcontent_right);	
		//DownloadImageView userHeadIv = (DownloadImageView)convertView.findViewById(R.id.iv_userhead_right);
		ChatMessage chatMessage = chatMessageList.get(position);
    	if(chatMessage == null) 
    		return null;
		if (chatMessage.getIsMySend().equals("0")) {		
			rightDialogLl.setVisibility(View.VISIBLE);
			leftDialogLl.setVisibility(View.GONE);	
			if(StringUtils.isNotEmpty(userName)) {
				tvUserNameRight.setText(userName);
			}
			btContentRight.setText(chatMessage.getContent());
			if(!StringUtils.isEmpty(chatMessage.getTime())) {
				tvSendTimeRight.setText(TimeUtil.parseChatDateTime(chatMessage.getTime()));
			}										
		} else {			
			rightDialogLl.setVisibility(View.GONE);		
			leftDialogLl.setVisibility(View.VISIBLE);
			btContentLeft.setText(chatMessage.getContent());
			tvUserNameLeft.setText(chatMessage.getName());
			if(!StringUtils.isEmpty(chatMessage.getTime())) {
				tvSendTimeLeft.setText(TimeUtil.parseChatDateTime(chatMessage.getTime()));
			}
		}
	    return convertView;
    }

}
