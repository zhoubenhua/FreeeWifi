package com.zhephone.hotspot.app.adapter;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.widget.DownloadImageView;
import com.zhephone.hotspoti.app.bean.VideoInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VideoListAdapter extends BaseAdapter {
	private List<VideoInfo> videoList;
	private LayoutInflater inflater;
	private Context mContext;
	public VideoListAdapter(List<VideoInfo> videoList, Context context) {
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.videoList = videoList;		
	}
	
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();  
	}

	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView =  inflater.inflate(R.layout.video_list_item, null);  
		TextView videoNameTv = (TextView) convertView.findViewById(R.id.video_name_tv);
		TextView videoDetailTv = (TextView) convertView.findViewById(R.id.video_detail_tv);
		DownloadImageView videoPicIv = (DownloadImageView)convertView.findViewById(R.id.video_pic_iv);
		VideoInfo videoInfo  = videoList.get(position);
		if(StringUtils.isNotEmpty(videoInfo.getVideoImage())) {
			videoPicIv.loadPic(videoInfo.getVideoImage());
		}
		if(StringUtils.isNotEmpty(videoInfo.getVideoIntroduction())) {
			videoDetailTv.setText(videoInfo.getVideoIntroduction());
		}
		if(StringUtils.isNotEmpty(videoInfo.getName())) {
			videoNameTv.setText(videoInfo.getName());
		}
		return convertView;
	}
	
	public int getCount() {
		return videoList.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}
	

}
