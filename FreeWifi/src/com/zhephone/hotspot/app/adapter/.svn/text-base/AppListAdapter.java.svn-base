package com.zhephone.hotspot.app.adapter;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.widget.DownloadImageView;
import com.zhephone.hotspoti.app.bean.AppInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppListAdapter extends BaseAdapter {
	private List<AppInfo> appInfoList;
	private LayoutInflater inflater;
	private Context mContext;
	public AppListAdapter(List<AppInfo> appInfoList, Context context) {
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.appInfoList = appInfoList;		
	}
	
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();  
	}

	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView =  inflater.inflate(R.layout.app_list_item, null);  
		TextView appNameTv = (TextView) convertView.findViewById(R.id.app_name_tv);
		TextView appDetailTv = (TextView) convertView.findViewById(R.id.app_detail_tv);
		DownloadImageView appPicIv = (DownloadImageView)convertView.findViewById(R.id.app_pic_iv);
		ImageView appDownloadIv = (ImageView)convertView.findViewById(R.id.app_download_iv);
		final AppInfo appInfo  = appInfoList.get(position);
		if(StringUtils.isNotEmpty(appInfo.getAppName())) {
			appNameTv.setText(appInfo.getAppName());
		}
		if(StringUtils.isNotEmpty(appInfo.getIntroduce())) {
			appDetailTv.setText(appInfo.getIntroduce());
		}
		if(StringUtils.isNotEmpty(appInfo.getAppImage())) {
			appPicIv.loadPic(appInfo.getAppImage());
		}
		appDownloadIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(StringUtils.isNotEmpty(appInfo.getUrl())) {
					Uri uri = Uri.parse(appInfo.getUrl());
					Intent intent =new Intent(Intent.ACTION_VIEW, uri);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			}
			
		});
		return convertView;
	}
	
	public int getCount() {
		return appInfoList.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}
	

}
