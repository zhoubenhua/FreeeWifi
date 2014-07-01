package com.zhephone.hotspot.app.ui.video;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import android.content.Context;
import android.content.Intent;
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
import com.zhephone.hotspot.app.adapter.VideoCategoryListAdapter;
import com.zhephone.hotspot.app.api.APIAsyncTask;
import com.zhephone.hotspot.app.api.CallbackListener;
import com.zhephone.hotspot.app.api.Protocol;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.http.RequestParams;
import com.zhephone.hotspot.app.widget.DownloadImageView;
import com.zhephone.hotspot.app.widget.IconPageIndicator;
import com.zhephone.hotspot.app.widget.IconPagerAdapter;
import com.zhephone.hotspot.app.widget.Topbar;
import com.zhephone.hotspoti.app.bean.VideoCategoryList;
import com.zhephone.hotspoti.app.bean.VideoInfo;
/**
 * 视频列表
 * @author Administrator
 *
 */
public class VideoCategoryListActivity extends BaseActivity {
	private ViewPager recommendVideoVp;
	private Button backBt;
	private ListView videoCategoryListLv;
	private IconPageIndicator circlePageIndicator;
	private int autoScrollTime;
	private int currentItem;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.video_category_list);
		this.setUpViews();
	}

	@Override
	protected void initViews() {
		View recommendVideoView = LayoutInflater.from(this).inflate(R.layout.recommend_video_view, null);
		recommendVideoVp = (ViewPager)recommendVideoView.findViewById(R.id.recommend_video_vp);
		circlePageIndicator = (IconPageIndicator)recommendVideoView.findViewById(R.id.circle_indicator);
		videoCategoryListLv = (ListView)findViewById(R.id.video_category_list_lv);	
		videoCategoryListLv.addHeaderView(recommendVideoView);
		Topbar topbar = new Topbar(findViewById(R.id.hotspot_topbar));
		backBt = topbar.getLeftBt();
		topbar.getRightBt().setVisibility(View.GONE);
		topbar.setToolbarCentreText(getResources().getString(R.string.video));
	}
	
	Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	
        }
    };
    
	Runnable autoScroll = new Runnable() {
		public void run() {
			if(recommendVideoVp.getAdapter() != null){
				if (recommendVideoVp.getCurrentItem() == recommendVideoVp.getAdapter().getCount() - 1) {
					currentItem = 0;
				}else {
					currentItem = recommendVideoVp.getCurrentItem() + 1;
				}
				recommendVideoVp.setCurrentItem(currentItem);
			}
		}
	};
	
	private void loadVideoCategoryListSucess(String data) {
		try {
			VideoCategoryList videoCategoryList = JSON.parseObject(data, VideoCategoryList.class);
			if(videoCategoryList.getList() != null) {
				VideoCategoryListAdapter videoCategoryListAdapter = new VideoCategoryListAdapter(videoCategoryList.getList(),context);
				videoCategoryListLv.setAdapter(videoCategoryListAdapter);
			}
			if(videoCategoryList.getRecommends() != null) {
				recommendVideoVp.setAdapter(new RecommendVideoPagerAdapter(videoCategoryList.getRecommends()));
				circlePageIndicator.setViewPager(recommendVideoVp);
				mHandler.postDelayed(autoScroll, autoScrollTime);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		dissmissProgressDialog();
	}
	
	private void loadVideoCategoryListFailed(String error) {
		dissmissProgressDialog();
		CommonUtil.sendToast(getApplicationContext(), error);
	}
	
	private void loadVideoCategoryList() {
		if (CommonUtil.checkNetwork(getApplicationContext())) {
			showProgressDialog(null, getResources().getString(R.string.loading));
			RequestParams paramMap = new RequestParams();
			//paramMap.put("secret", AppConfig.getInstance().getUserInfo().getSecret());
			final APIAsyncTask api = new APIAsyncTask();			
			api.get(Protocol.VIDEO_CATEGORY_LIST,paramMap, new CallbackListener() {
				public void onSuccess(String data) {
					if (data != null) {
						loadVideoCategoryListSucess(data);
					}
				}

				public void onError(String error) {
					loadVideoCategoryListFailed(error);
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
		loadVideoCategoryList();
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
    public class RecommendVideoPagerAdapter extends PagerAdapter implements IconPagerAdapter{
        public List<VideoInfo> recommendVideoInfoList;
        public RecommendVideoPagerAdapter(List<VideoInfo> recommendVideoInfoList) {
            this.recommendVideoInfoList = recommendVideoInfoList;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
        	// 将ImageView从ViewPager移除
        	((ViewPager) arg0).removeView((View) arg2);
        }

        public void finishUpdate(View arg0) {
        }

        public int getCount() {
            return recommendVideoInfoList != null ? recommendVideoInfoList.size() : 0;
        }

        @Override
        public Object instantiateItem(View container, final int position) {
        	View view = View.inflate(context, R.layout.recommend_video_item, null);
			((ViewPager) container).addView(view);
			DownloadImageView recommendVideoPicIv = (DownloadImageView) view.findViewById(R.id.recommend_video_pic_iv);
			final VideoInfo videoInfo = recommendVideoInfoList.get(position);
			if(StringUtils.isNotEmpty(videoInfo.getVideoImage())) {
				recommendVideoPicIv.loadPic(videoInfo.getVideoImage());
			}
			recommendVideoPicIv.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(context,PlayActivity.class);
					intent.putExtra(Constant.VideoIntentDef.VIDEO_NAME, videoInfo.getName());
					intent.putExtra(Constant.VideoIntentDef.VIDEO_URL, videoInfo.getVideoUrl());
					startActivityLeft(intent);
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
