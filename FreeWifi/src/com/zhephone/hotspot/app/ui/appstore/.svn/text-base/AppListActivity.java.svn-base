package com.zhephone.hotspot.app.ui.appstore;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.adapter.AppListAdapter;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.widget.DownloadImageView;
import com.zhephone.hotspot.app.widget.IconPageIndicator;
import com.zhephone.hotspot.app.widget.IconPagerAdapter;
import com.zhephone.hotspot.app.widget.Topbar;
import com.zhephone.hotspoti.app.bean.AppInfo;
import com.zhephone.hotspoti.app.bean.AppList;

public class AppListActivity extends BaseActivity {
	private ViewPager recommendAppVp;
	private Button backBt;
	private ListView appListLv;
	private IconPageIndicator circlePageIndicator;
	private int autoScrollTime;
	private int currentItem;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_list);
		this.setUpViews();
	}

	@Override
	protected void initViews() {
		View recommendAppView = LayoutInflater.from(this).inflate(R.layout.recommend_app_view, null);
		recommendAppVp = (ViewPager)recommendAppView.findViewById(R.id.recommend_app_vp);
		circlePageIndicator = (IconPageIndicator)recommendAppView.findViewById(R.id.circle_indicator);
		appListLv = (ListView)findViewById(R.id.app_list_lv);
		appListLv.addHeaderView(recommendAppView);
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		topbar.getRightBt().setVisibility(View.GONE);
		topbar.setToolbarCentreText(getResources().getString(R.string.app_store));
	}
	
	Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	
        }
    };
    
	Runnable autoScroll = new Runnable() {
		public void run() {
			if(recommendAppVp.getAdapter() != null){
				if (recommendAppVp.getCurrentItem() == recommendAppVp.getAdapter().getCount() - 1) {
					currentItem = 0;
				}else {
					currentItem = recommendAppVp.getCurrentItem() + 1;
				}
				recommendAppVp.setCurrentItem(currentItem);
			}
		}
	};
	
	private void loadAppListSucess(String data) {
		try {
//			data =new String(data.getBytes("UTF-8"),"ISO-8859-1");
//			String iso = new String(data.getBytes("UTF-8"),"ISO-8859-1");
//			data = new String(iso.getBytes("ISO-8859-1"),"UTF-8");  

		
			AppList appList = JSON.parseObject(data, AppList.class);
			if(appList.getList() != null) {
				List<AppInfo> recommendAppList = new ArrayList<AppInfo>();
				for(AppInfo appInfo: appList.getList()) {
					/**
					 * 判断是否为推荐应用,1为推荐应用,0不是
					 */
					if(appInfo.getIsRecommend() ==1) {
						recommendAppList.add(appInfo);
					}
				}
				if(recommendAppList.size() >0) {
					recommendAppVp.setAdapter(new RecommendAppPagerAdapter(recommendAppList));
					circlePageIndicator.setViewPager(recommendAppVp);
					mHandler.postDelayed(autoScroll, autoScrollTime);
				}
				AppListAdapter appListAdapter = new AppListAdapter(appList.getList(),context);
				appListLv.setAdapter(appListAdapter);		
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		dissmissProgressDialog();
	}
	
	private void loadAppListFailed(String error) {
		dissmissProgressDialog();
		CommonUtil.sendToast(getApplicationContext(), error);
	}
	
	private void loadAppList() {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			showProgressDialog(null, getResources().getString(R.string.loading));
			RequestParams paramMap = new RequestParams();
			//paramMap.put("secret", AppConfig.getInstance().getUserInfo().getSecret());
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.APP_LIST,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						loadAppListSucess(data);
					}
				}

				public void onError(String error) {
					loadAppListFailed(error);
				}
			});
			api.execute();
		}
	}

	@Override
	protected void initData() {
		context = this.getApplicationContext();
		autoScrollTime = 2000;
		currentItem = 0;
		loadAppList();
	}
	
	private OnPageChangeListener circlePageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			mHandler.removeCallbacks(autoScroll);
			mHandler.postDelayed(autoScroll, 2000);
		}
			
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
		@Override
		public void onPageScrollStateChanged(int arg0) {}
	};
	
	private OnClickListener backListener = new OnClickListener() {
		public void onClick(View v) {
			back();
		}
	};

	@Override
	protected void addListener() {
		circlePageIndicator.setOnPageChangeListener(circlePageChangeListener);
		backBt.setOnClickListener(backListener);
	}
	
	/**
     * ViewPager适配器
     */
    public class RecommendAppPagerAdapter extends PagerAdapter implements IconPagerAdapter{
        public List<AppInfo> recommendAppInfoList;
        public RecommendAppPagerAdapter(List<AppInfo> recommendAppInfoList) {
            this.recommendAppInfoList = recommendAppInfoList;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
        	// 将ImageView从ViewPager移除
        	((ViewPager) arg0).removeView((View) arg2);
        }

        public void finishUpdate(View arg0) {
        }

        public int getCount() {
            return recommendAppInfoList != null ? recommendAppInfoList.size() : 0;
        }

        @Override
        public Object instantiateItem(View container, final int position) {
        	View view = View.inflate(context, R.layout.recommend_app_item, null);
			((ViewPager) container).addView(view);
			DownloadImageView recommendAppPicIv = (DownloadImageView) view.findViewById(R.id.recommend_app_pic_iv);
			final AppInfo appInfo = recommendAppInfoList.get(position);
			if(StringUtils.isNotEmpty(appInfo.getAdImage())) {
				recommendAppPicIv.loadPic(appInfo.getAdImage());
			}
			recommendAppPicIv.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					if(StringUtils.isNotEmpty(appInfo.getUrl())) {
						Uri uri = Uri.parse(appInfo.getUrl());
						Intent intent =new Intent(Intent.ACTION_VIEW, uri);
						startActivityLeft(intent);
					}
				}
				
			});
			return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

		@Override
		public int getIconResId(int index) {
			return R.drawable.circle_indicator;
		}
    }


}
