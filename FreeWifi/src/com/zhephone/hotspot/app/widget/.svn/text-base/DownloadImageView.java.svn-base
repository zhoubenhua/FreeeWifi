package com.zhephone.hotspot.app.widget;

import com.zhephone.hotspot.app.AppConfig;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DownloadImageView extends ImageView{
	private Context mContext;
	private FinalBitmap finalBitmap;

	public DownloadImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
		// TODO Auto-generated constructor stub
	}
	
	private void init() {
		finalBitmap = FinalBitmap.create(mContext);
		finalBitmap.configBitmapLoadThreadSize(3);
		finalBitmap.configDiskCachePath(AppConfig.getInstance().getCacheImageDir());
		finalBitmap.configDiskCacheSize(1024 * 1024 * 10);  //设置缓存大小 
	}
	
	public void loadPic(String picUrl) {
		finalBitmap.display(this, picUrl);
	}

}
