package com.zhephone.hotspot.app.adapter;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.ui.video.PlayActivity;
import com.zhephone.hotspot.app.widget.MyGridView;
import com.zhephone.hotspoti.app.bean.VideoCategory;
import com.zhephone.hotspoti.app.bean.VideoInfo;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VideoCategoryListAdapter extends BaseAdapter {
	private List<VideoCategory> videoCategoryList;
	private LayoutInflater inflater;
	private Context mContext;
	public VideoCategoryListAdapter(List<VideoCategory> videoCategoryList, Context context) {
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.videoCategoryList = videoCategoryList;		
	}
	
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();  
	}

	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView =  inflater.inflate(R.layout.video_category_list_item, null);  
		TextView vidoeCategoryNameTv = (TextView) convertView.findViewById(R.id.video_category_name_tv);
		MyGridView videoListGv = (MyGridView)convertView.findViewById(R.id.video_list_gv);
		final VideoCategory videoCategory = videoCategoryList.get(position);
		if(StringUtils.isNotEmpty(videoCategory.getName())) {
			vidoeCategoryNameTv.setText(videoCategory.getName());
		}
		if(videoCategory.getContent() != null) {
			VideoListAdapter videoListAdapter = new VideoListAdapter(videoCategory.getContent(),mContext);
			videoListGv.setAdapter(videoListAdapter);
			videoListGv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					VideoInfo videoInfo = videoCategory.getContent().get(arg2);
					Intent intent = new Intent(mContext,PlayActivity.class);
					intent.putExtra(Constant.VideoIntentDef.VIDEO_NAME, videoInfo.getName());
					intent.putExtra(Constant.VideoIntentDef.VIDEO_URL, videoInfo.getVideoUrl());
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			});
		}
		return convertView;
	}
	
	public int getCount() {
		return videoCategoryList.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}
	

}
