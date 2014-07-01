package com.zhephone.hotspot.app.ui;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import com.zhephone.hotspot.app.db.ChatMessageDao;
import com.zhephone.hotspot.app.ui.appstore.AppListActivity;
import com.zhephone.hotspot.app.ui.chat.GroupListActivity;
import com.zhephone.hotspot.app.ui.chat.NearGroupActivity;
import com.zhephone.hotspot.app.ui.user.LoginActivity;
import com.zhephone.hotspot.app.ui.user.PersonCenterActivity;
import com.zhephone.hotspot.app.ui.video.VideoCategoryListActivity;
import com.zhephone.hotspot.app.widget.IconPageIndicator;
import com.zhephone.hotspot.app.widget.IconPagerAdapter;

public class MainActivity extends BaseActivity {
	//private ImageView adIv;
	private ImageView videoIv;
	private ImageView chatIv;
	private ImageView loginIv;
	private ImageView nearbyIv;
	private ImageView appIv;
	private ImageView settingIv;
	private Context context;
	private ViewPager adVp;
	private IconPageIndicator circlePageIndicator;
	private int autoScrollTime;
	private int currentItem;
	private ChatMessageDao chatMessageDao;
	private ReceiveNewMsgReceiver receiveNewMsgReceiver;
	
    private OnClickListener loginListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			/**
			 * 判断当前有没有登录,如果没有登录点击就到登录界面,如果已经登录点击就到个人中心界面
			 */
			Intent intent;
			if(AppConfig.getInstance().isLogin()) {
				intent = new Intent(context,PersonCenterActivity.class);
			} else {
				intent = new Intent(context,LoginActivity.class);
			}
			startActivityLeft(intent);
		}
    };
    
	/**
	 * 注册有新的消息广播
	 * @author Administrator
	 *
	 */
	private class ReceiveNewMsgReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			checkHasNoReadMessage();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiveNewMsgReceiver);
	}
	
	@Override
	protected void onResume() {
		super.onResume(); 	
		/**
    	 * 判断有没有登录,如果登录变显示个人中心,如果没有登录就显示登录
    	 */
		if(AppConfig.getInstance().isLogin()) {
			loginIv.setImageResource(R.drawable.main_person_selector);
		} else {
			loginIv.setImageResource(R.drawable.main_login_selector);
		}
		checkHasNoReadMessage();
		receiveNewMsgReceiver = new ReceiveNewMsgReceiver();   			
		IntentFilter intentFilter = new IntentFilter();																		  	
		intentFilter.addAction(Constant.ChatIntentDef.RECEIVE_NEW_MSG_ACTION);		
		registerReceiver(receiveNewMsgReceiver, intentFilter); 	
	}
	
	private void checkHasNoReadMessage() {
		if(chatMessageDao.queryHasNoReadMessage() ==0) {
			/**
			 * 有未读消息
			 */
			chatIv.setImageResource(R.drawable.main_chat_msg_selector);
		} else {
			/**
			 * 没有未读消息
			 */
			chatIv.setImageResource(R.drawable.main_chat_selector);
		}
	}

	private OnClickListener videoListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context,VideoCategoryListActivity.class);
			startActivityLeft(intent);
		}
    };
    
    private OnClickListener settingListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context,SettingActivity.class);
			startActivityLeft(intent);
		}
    };
    
    private OnClickListener appListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context,AppListActivity.class);
			startActivityLeft(intent);
		}
    };
    
    private OnClickListener chatListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent;
			//if(AppConfig.getInstance().isLogin()) {
				intent = new Intent(context,GroupListActivity.class);
//			} else {
//				intent = new Intent(context,LoginActivity.class);
//			}
			startActivityLeft(intent);
		}
    };
    
    private OnClickListener nearbyListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			startActivityLeft(new Intent(context,NearGroupActivity.class));
		}
    };
    
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
    
    
    @Override
	protected void initViews() {
    	adVp = (ViewPager)findViewById(R.id.ad_vp);
		videoIv = (ImageView)findViewById(R.id.video_iv);
		chatIv = (ImageView)findViewById(R.id.chat_iv);
		loginIv = (ImageView)findViewById(R.id.login_iv);
		nearbyIv = (ImageView)findViewById(R.id.nearby_iv);
		appIv = (ImageView)findViewById(R.id.app_iv);
		settingIv = (ImageView)findViewById(R.id.setting_iv);
		circlePageIndicator = (IconPageIndicator)findViewById(R.id.circle_indicator);
	}

	@Override
	protected void addListener() {
		loginIv.setOnClickListener(loginListener);
		nearbyIv.setOnClickListener(nearbyListener);
		chatIv.setOnClickListener(chatListener);
		circlePageIndicator.setOnPageChangeListener(circlePageChangeListener);
		videoIv.setOnClickListener(videoListener);
		appIv.setOnClickListener(appListener);
		settingIv.setOnClickListener(settingListener);
	}
	
	Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	
        }
    };
    
	Runnable autoScroll = new Runnable() {
		public void run() {
			if(adVp.getAdapter() != null){
				if (adVp.getCurrentItem() == adVp.getAdapter().getCount() - 1) {
					currentItem = 0;
				}else {
					currentItem = adVp.getCurrentItem() + 1;
				}
				adVp.setCurrentItem(currentItem);
			}
		}
	};
	
	/**
     * ViewPager适配器
     */
    public class AdPagerAdapter extends PagerAdapter implements IconPagerAdapter{
        public List<String> _adList;
        public AdPagerAdapter(List<String> adList) {
            this._adList = adList;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
        	// 将ImageView从ViewPager移除
        	((ViewPager) arg0).removeView((View) arg2);
        }

        public void finishUpdate(View arg0) {
        }

        public int getCount() {
            return _adList != null ? _adList.size() : 0;
        }

        @Override
        public Object instantiateItem(View container, final int position) {
        	View view = View.inflate(context, R.layout.ad_item, null);
			((ViewPager) container).addView(view);
			ImageView imageView = (ImageView) view.findViewById(R.id.ad_iv);
//			imageView.setOnClickListener(new OnClickListener() {
//				public void onClick(View v) {
//					Intent intent = new Intent(context,WebActivity.class);
//					intent.putExtra(Constant.WebIntentDef.WEB_URL, getResources().getString(R.string.web_url));
//					startActivity(intent);
//				}
//			});
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
	
	@Override
	protected void initData() {
		context = this.getApplicationContext();
		autoScrollTime = 4000;
		currentItem = 0;
		chatMessageDao = ChatMessageDao.getInstance(getApplicationContext());	
		List<String> adList = new ArrayList<String>();
		adList.add("");
		adList.add("");
		adList.add("");
		adList.add("");
		adVp.setAdapter(new AdPagerAdapter(adList));
		circlePageIndicator.setViewPager(adVp);
		mHandler.postDelayed(autoScroll, autoScrollTime);
	}

	/**
	 * 返回按钮退出
	 */
	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	Timer tExit = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				CommonUtil.sendToast(getApplicationContext(), "再按一次后退键退出应用程序");
				if (!hasTask) {
					tExit.schedule(task, 2000);
				}
			} else {
				/**
				 * 返回到桌面
				 */
				isExit = false;
				Intent home = new Intent(Intent.ACTION_MAIN);  
				home.addCategory(Intent.CATEGORY_HOME);   
				startActivity(home); 

				//exitApp();
			}
		}
		return false;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		this.setUpViews();
	}

}