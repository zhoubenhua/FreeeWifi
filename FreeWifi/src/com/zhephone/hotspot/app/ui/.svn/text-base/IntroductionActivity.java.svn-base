package com.zhephone.hotspot.app.ui;
import java.util.ArrayList;
import java.util.List;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppConfig;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.widget.IconPageIndicator;
import com.zhephone.hotspot.app.widget.IconPagerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;


public class IntroductionActivity extends BaseActivity {
	private ViewPager introductionPager;
	private View introductionOneView;
	private View introductionTwoView;
	private View introductionThreeView;
	private View introductionFourView;
	private ArrayList<View> introductionViews;
	private Button startBt;
	private Context context;
	private IconPageIndicator circlePageIndicator;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.introduction);  
        this.setUpViews(); 	
	}
	
	@Override
	protected void initViews() {
		introductionPager = (ViewPager)this.findViewById(R.id.introduction_vp);		
		LayoutInflater inflater = getLayoutInflater();  
		introductionViews = new ArrayList<View>();  
		introductionOneView = inflater.inflate(R.layout.introduction_one, null);
		introductionTwoView = inflater.inflate(R.layout.introduction_two, null);
		introductionThreeView = inflater.inflate(R.layout.introduction_three, null);
		introductionFourView = inflater.inflate(R.layout.introduction_four, null);
	    startBt = (Button)introductionFourView.findViewById(R.id.start_bt);
	    circlePageIndicator = (IconPageIndicator)findViewById(R.id.circle_indicator);
	}
	
	@Override
	protected void initData() {
		context = this.getApplicationContext();
		introductionViews.add(introductionOneView);
	    introductionViews.add(introductionTwoView);			
	    introductionViews.add(introductionThreeView);
	    introductionViews.add(introductionFourView);
	    IntroductionPagerAdapter guidePagerAdapter = new IntroductionPagerAdapter(introductionViews);
	    introductionPager.setAdapter(guidePagerAdapter);
		circlePageIndicator.setViewPager(introductionPager);
	}


	/**
     * ViewPager适配器
     */
    public class IntroductionPagerAdapter extends PagerAdapter implements IconPagerAdapter{
        public List<View> views;
        public IntroductionPagerAdapter(List<View> views) {
            this.views = views;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
        	// 将ImageView从ViewPager移除
        	((ViewPager) arg0).removeView((View) arg2);
        }

        public void finishUpdate(View arg0) {
        }

        public int getCount() {
            return views != null ? views.size() : 0;
        }

        @Override
        public Object instantiateItem(View arg0, final int arg1) {
        	((ViewPager) arg0).addView(views.get(arg1));  
            return views.get(arg1);  
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
	protected void addListener() {
		startBt.setOnClickListener(startListener);
	}
	
	private OnClickListener startListener = new OnClickListener() {
		public void onClick(View v) {
			AppConfig.getInstance().saveUseApp();	
			Intent intent = new Intent(IntroductionActivity.this,MainActivity.class);
			startActivity(intent);	
		}
	};
	

}
