package com.zhephone.hotspot.app.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.zhephone.hotspot.R;
import com.zhephone.hotspoti.app.bean.ChatGroup;



public class GroupListAdapter extends BaseAdapter {

    private List<ChatGroup> groupList;
    private LayoutInflater mInflater;
	private HashMap<Integer, View> viewMap;
    public GroupListAdapter(Context context, List<ChatGroup> groupList) {
        this.groupList = groupList;
        mInflater = LayoutInflater.from(context);
        viewMap = new HashMap<Integer, View>();
    }

    public int getCount() {
        return groupList.size();
    }

    public Object getItem(int position) {
        return  groupList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
   
	
    public View getView(int position, View convertView, ViewGroup parent) {
		convertView = this.viewMap.get(position);		
		if(convertView == null) {										
			convertView =  mInflater.inflate(R.layout.group_item, null); 
			TextView groupNameTv= (TextView)convertView.findViewById(R.id.group_name_tv);
			TextView chatMsgTv= (TextView)convertView.findViewById(R.id.chat_message_tv);
			TextView chatDateTv= (TextView)convertView.findViewById(R.id.chat_date_tv);
			ImageView hasMessageIv = (ImageView)convertView.findViewById(R.id.has_message_iv);
			ChatGroup group = groupList.get(position);
	    	if(group == null) 
	    		return null;
	    	if(StringUtils.isNotEmpty(group.getName())) {
	    		groupNameTv.setText(group.getName());
	    	}
	    	/**
	    	 * 检查这个组有没有未读消息,0是代表有,1是代表没有
	    	 */
	    	if(group.getHasNoReadMessage() == 0) {
	    		hasMessageIv.setVisibility(View.VISIBLE);
	    	} else {  
	    		hasMessageIv.setVisibility(View.GONE);
	    	}
	    	if(StringUtils.isNotEmpty(group.getContent())) {
	    		chatMsgTv.setText(group.getContent());
	    	} else {
	    		chatMsgTv.setVisibility(View.GONE);
	    	}		
	    	if(StringUtils.isNotEmpty(group.getLastTime())) {
	    		chatDateTv.setText(group.getLastTime());
	    	}
		    viewMap.put(position, convertView);		
		}
	    return convertView;
    }

}
