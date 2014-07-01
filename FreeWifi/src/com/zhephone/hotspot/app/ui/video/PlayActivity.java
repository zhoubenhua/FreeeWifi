package com.zhephone.hotspot.app.ui.video;
import io.vov.utils.StringUtils;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import io.vov.vitamio.widget.VideoView;
import java.sql.Timestamp;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.AppManager;
import com.zhephone.hotspot.app.common.CommonUtil;
import com.zhephone.hotspot.app.common.Constant;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;




public class PlayActivity extends Activity implements OnClickListener,
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
		OnVideoSizeChangedListener, SurfaceHolder.Callback, OnErrorListener,
		OnInfoListener, OnSeekCompleteListener {
	private String path;
	//private ImageView imageView;
	private TextView nametTextView;
	private TextView timeTextView;
	private Button chiceButton;
	private Button fontbButton, playbButton, nextButton;
	private VideoView videoView;
	private FrameLayout fLayout;
	private LinearLayout layout;
	private boolean isPlay;
	private SeekBar seekBar;
	private upDateSeekBar update;
	private boolean isLock;
	private String timeString;
	private TextView play_tiem, endTime;
	private String name;
	private ImageView mOperationVolumeIv;
	private ImageView mOperationVolumePercentIv;
	private View mVolumeBrightnessLayout;
	private AudioManager mAudioManager;
	/** �?��声音 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	/** 当前亮度 */
	private float mBrightness = -1f;
	private GestureDetector mGestureDetector;
	private View bar;
	private TextView textViewBF;
	private TextView sudu;
	private TextView sudu2;
	private PopupWindow pWindow;
	private Button backBt;
	private Button qingxiButton, liuchangButton, gaoqingButton;
    
	private boolean isFinish;
	private long pauseSize;
	private long size;
	private String url;
	private boolean firstScroll = false;// 每次触摸屏幕后，第一次scroll的标志
	private int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量
	private static final int GESTURE_MODIFY_PROGRESS = 1;
	private static final int GESTURE_MODIFY_VOLUME = 2;
//	private ImageView mOperationProgressIv;
//	private ImageView mOperationProgressPercentIv;
//	private View mProgressBrightnessLayout;// 进度图标
	private static final float STEP_PROGRESS = 2f;// 设定进度滑动时的步长，避免每次滑动都改变，导致改变过快
	private Context context;
	private boolean showFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.play);
		initView();
		initData();
		nextButton.setEnabled(false);
		fontbButton.setEnabled(false);
		play();
		context = this.getApplicationContext();
		 AppManager.getAppManager().addActivity(this);
	}
	

	private void initData() {
		//path = getIntent().getStringExtra("path");
		name = getIntent().getStringExtra(Constant.VideoIntentDef.VIDEO_NAME);
		url = getIntent().getStringExtra(Constant.VideoIntentDef.VIDEO_URL);
		if(!StringUtils.isBlank(name)) {
			nametTextView.setText(name);
		}
		showFlag = false;

	}
	
	private void initView() {
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		fLayout = (FrameLayout) findViewById(R.id.title_fl);
		fLayout.setVisibility(View.GONE);
		layout = (LinearLayout) findViewById(R.id.buttom_lin);
		layout.setVisibility(View.GONE);
		videoView = (VideoView) findViewById(R.id.surface_view);
		sudu = (TextView) findViewById(R.id.sudu);
		sudu2 = (TextView) findViewById(R.id.sudu2);
		bar = findViewById(R.id.pb);
		textViewBF = (TextView) bar.findViewById(R.id.buff);
		play_tiem = (TextView) findViewById(R.id.play_time);
		endTime = (TextView) findViewById(R.id.play_end_time);
		nametTextView = (TextView) findViewById(R.id.movie_name);
		nametTextView.setOnClickListener(this);
		timeTextView = (TextView) findViewById(R.id.movie_time);
		timeString = (new Timestamp(System.currentTimeMillis())).toString()
				.substring(11, 16);
		timeTextView.setText(timeString);
		chiceButton = (Button) findViewById(R.id.zhiliang);
		chiceButton.setOnClickListener(this);
		fontbButton = (Button) findViewById(R.id.font);
		fontbButton.setOnClickListener(this);
		playbButton = (Button) findViewById(R.id.play);
		playbButton.setOnClickListener(this);
		nextButton = (Button) findViewById(R.id.next);
		nextButton.setOnClickListener(this);
		backBt = (Button)findViewById(R.id.back_bt);
		backBt.setOnClickListener(this);
		mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
		mOperationVolumeIv = (ImageView) findViewById(R.id.operation_volume_iv);
		mOperationVolumePercentIv = (ImageView) findViewById(R.id.operation_volume_percent_iv);
		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setListener();
		update = new upDateSeekBar();
		new Thread(update).start();
	}

	private class MyGestureListener extends SimpleOnGestureListener {
		
		@Override
		public boolean onDown(MotionEvent e) {
			firstScroll = true;// 设定是触摸屏幕后第一次scroll的标志
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			float mOldY = e1.getY();
			int y = (int) e2.getRawY();
//			int x= (int)e2.getRawX();
			Display disp = getWindowManager().getDefaultDisplay();
			int windowHeight = disp.getHeight();
//			/**
//			 * 右滑动
//			 */
//			if (mOldX > windowWidth * 4.0 / 5)  {
//				onVolumeSlide((mOldY - y) / windowHeight);
//			} else if (mOldX < windowWidth / 5.0) {
//				onBrightnessSlide((mOldY - y) / windowHeight);
//			}
//
//		
//			return super.onScroll(e1, e2, distanceX, distanceY);
			
			if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
				// 横向的距离变化大则调整进度，纵向的变化大则调整音量
				if (Math.abs(distanceX) >= Math.abs(distanceY)) {
					mVolumeBrightnessLayout.setVisibility(View.INVISIBLE);
					//mProgressBrightnessLayout.setVisibility(View.VISIBLE);
					GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
				} else {
					mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
					//mProgressBrightnessLayout.setVisibility(View.INVISIBLE);
					GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
				}
			}
			// 如果每次触摸屏幕后第一次scroll是调节进度，那之后的scroll事件都处理音量进度，直到离开屏幕执行下一次操作
			if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
				if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
					if (distanceX >= CommonUtil.dip2px(context, STEP_PROGRESS)) {// 快退，用步长控制改变速度，可微调
						//mOperationProgressIv.setImageResource(R.drawable.player_prev_highlight);
						int percent = seekBar.getProgress()-3;
						if(percent <=0) {
							percent = 0;
						} 
						onProgressSlide(percent);
					} else if (distanceX <= -CommonUtil.dip2px(context, STEP_PROGRESS)) {// 快进
						//mOperationProgressIv.setImageResource(R.drawable.test_player_next_highlight);
						int percent = seekBar.getProgress()+3;
						if(percent >=seekBar.getMax()) {
							percent = seekBar.getMax();
						} 
						onProgressSlide(percent);
					}
				}

			}
			// 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
			else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
				onVolumeSlide((mOldY - y) / windowHeight);
			}

			firstScroll = false;// 第一次scroll执行完成，修改标志
			return false;
			
		}
	}
	
	/**
	 * 滑动改变进度大小
	 */
	private void onProgressSlide(float percent){
		isPlay = false;
		videoView.pause();
		seekBar.setProgress((int) percent);
		int value = (int) (percent
				* videoView.getDuration() / seekBar.getMax());
		videoView.seekTo(value);
		videoView.start();
		isPlay = true;
	}

	/**
	 * 滑动改变声音大小
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			// 显示
			mOperationVolumeIv.setImageResource(R.drawable.video_volumn_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		ViewGroup.LayoutParams lp = mOperationVolumePercentIv.getLayoutParams();
		lp.width = findViewById(R.id.operation_volume_full_iv).getLayoutParams().width
				* index / mMaxVolume;
		mOperationVolumePercentIv.setLayoutParams(lp);
	}

//	 /**
//     * 滑动改变亮度
//     * 
//     * @param percent
//     */
//	private void onBrightnessSlide(float percent) {
//		if (mBrightness < 0) {
//			mBrightness = getWindow().getAttributes().screenBrightness;
//			if (mBrightness <= 0.00f)
//				mBrightness = 0.50f;
//			if (mBrightness < 0.01f)
//				mBrightness = 0.01f;
//
//			// 显示
//			mOperationVolumeIv.setImageResource(R.drawable.video_brightness_bg);
//			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
//		}
//		WindowManager.LayoutParams lpa = getWindow().getAttributes();
//		lpa.screenBrightness = mBrightness + percent;
//		if (lpa.screenBrightness > 1.0f)
//			lpa.screenBrightness = 1.0f;
//		else if (lpa.screenBrightness < 0.01f)
//			lpa.screenBrightness = 0.01f;
//		getWindow().setAttributes(lpa);
//
//		ViewGroup.LayoutParams lp =  mOperationVolumePercentIv.getLayoutParams();
//		lp.width = (int) (findViewById(R.id.operation_volume_full_iv).getLayoutParams().width * lpa.screenBrightness);
//		mOperationVolumePercentIv.setLayoutParams(lp);
//	}

	private void play() {
		isPlay = true;
		try {
			videoView.setVideoURI(Uri.parse(url));
			videoView.setOnCompletionListener(this);
			videoView.setOnBufferingUpdateListener(this);
			videoView.setOnErrorListener(this);
			videoView.setOnInfoListener(this);
		   videoView.setOnPreparedListener(this);
			
		} catch (Exception e) {
			Log.i("hck", "PlayActivity " + e.toString());
		}
	}
	class upDateSeekBar implements Runnable {

		@Override
		public void run() {
			if (!isFinish) {
				mHandler.sendMessage(Message.obtain());
				mHandler.postDelayed(update, 500);
			}

		}
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (videoView == null) {
				return;
			}
			timeString = (new Timestamp(System.currentTimeMillis())).toString()
					.substring(11, 16);
			timeTextView.setText(timeString);
			play_tiem.setText(StringUtils.generateTime(videoView
					.getCurrentPosition()));
			if (videoView != null) {
				seekBar(videoView.getCurrentPosition());
			}

		};
	};

	private void seekBar(long size) {
		if (videoView.isPlaying()) {
			long mMax = videoView.getDuration();
			int sMax = seekBar.getMax();
			int percent = (int)(videoView.getCurrentPosition() * seekBar.getMax() / videoView.getDuration());
			seekBar.setProgress(percent);

		}
	}

	private void setListener() {
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				int value = (int) (seekBar.getProgress()
						* videoView.getDuration() / seekBar.getMax());
				videoView.seekTo(value);
				videoView.start();
				isPlay = true;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				isPlay = false;
				videoView.pause();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event))
			return true;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			showFlag = !showFlag;
			if (showFlag) {
				fLayout.setVisibility(View.VISIBLE);
				layout.setVisibility(View.VISIBLE);
			} else {
				fLayout.setVisibility(View.GONE);
				layout.setVisibility(View.GONE);
			}
			//imageView.setVisibility(View.VISIBLE);
			break;
		case MotionEvent.ACTION_UP:
			endGesture();
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;

		// 隐藏
		disHandler.removeMessages(0);
		disHandler.sendEmptyMessageDelayed(0, 1000);
		disHandler.removeMessages(1);
		disHandler.sendEmptyMessageDelayed(1, 5000);
	}

	private Handler disHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				mVolumeBrightnessLayout.setVisibility(View.GONE);
				//mProgressBrightnessLayout.setVisibility(View.GONE);
			} else {
				fLayout.setVisibility(View.GONE);
				layout.setVisibility(View.GONE);
				//imageView.setVisibility(View.GONE);
				if (pWindow != null && pWindow.isShowing()) {
					pWindow.dismiss();
				}
			}

		}
	};

	@SuppressLint("HandlerLeak")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play:
			if (isPlay) {
				videoView.pause();
				playbButton
						.setBackgroundResource(R.drawable.player_play_highlight);
				isPlay = false;
				seekBar.setEnabled(false);
			} else {
				playbButton
						.setBackgroundResource(R.drawable.player_pause_highlight);
				videoView.start();
				isPlay = true;
				seekBar.setEnabled(true);
			}
			break;
		case R.id.image_lock:
			if (isLock) {
				isLock = false;
				//imageView.setBackgroundResource(R.drawable.lock_off);
			} else {
				isLock = true;
				//imageView.setBackgroundResource(R.drawable.lock_on);
			}
			break;
		case R.id.back_bt:
			finish();				
			break;
		case R.id.next:
			videoView.pause();
			if (videoView!=null) {
				size=videoView.getCurrentPosition()+videoView.getDuration()/10;
				videoView.seekTo(size);
				videoView.start();
			}
			break;
		case R.id.font:
			videoView.pause();
			if (videoView!=null) {
				size=videoView.getCurrentPosition()-videoView.getDuration()/10;
				videoView.seekTo(size);
				videoView.start();
			}
			break;
		case R.id.zhiliang:
			Log.i("hck", "zhiliang");
			showPop(v);
			break;
		
			
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Log.i("hck", "surfaceCreated");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

	}

	@Override
	public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
	}
	@Override
	public void onPrepared(MediaPlayer arg0) {
		nextButton.setEnabled(true);
		fontbButton.setEnabled(true);
		bar.setVisibility(View.GONE);
		if (url.startsWith("http")) {
			videoView.setBufferSize(1024 * 1024);
		} else {
			videoView.setBufferSize(0);
		}
		endTime.setText(StringUtils.generateTime(videoView.getDuration()));
		bar.setVisibility(View.GONE);
		if (pauseSize > 0) {
			videoView.seekTo(pauseSize);
		}
		pauseSize = 0;
	}
	@Override
	public void onCompletion(MediaPlayer arg0) {
		videoView.seekTo(0);
		videoView.start();
		isPlay = true;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		textViewBF.setTextColor(Color.CYAN);
		textViewBF.setText(videoView.getBufferPercentage() + "%");

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		videoView.setVideoLayout(3, 0);
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		Log.i("hck", "error :" + arg1 + arg2);
		return false;
	}

	@Override
	public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
		switch (arg1) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (isPlay) {
				bar.setVisibility(View.VISIBLE);
				videoView.pause();
				isPlay = false;

			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			if (!isPlay) {
				bar.setVisibility(View.GONE);
				videoView.start();
				isPlay = true;

			}
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			sudu.setTextColor(Color.CYAN);
			sudu.setText(arg2 + "kb/s");
			sudu2.setText(arg2 + "kb/s");
			break;
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		videoView.pause();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isPlay) {
			videoView.start();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (videoView != null) {
			videoView.stopPlayback();
			videoView = null;
		}
		if (pWindow != null && pWindow.isShowing()) {
			pWindow.dismiss();
		}
		isPlay = false;
		isFinish = true;
		System.gc();

	}
	

	@Override
	public void onSeekComplete(MediaPlayer arg0) {

	}

	private void showPop(View view2) {

//		View view = LayoutInflater.from(this).inflate(R.layout.pop, null);
//		qingxiButton = (Button) view.findViewById(R.id.qingxi);
//		gaoqingButton = (Button) view.findViewById(R.id.gaoqing);
//		liuchangButton = (Button) view.findViewById(R.id.liuchang);
//		pWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//		pWindow.setOutsideTouchable(true);
//		pWindow.showAtLocation(view2, Gravity.LEFT, Gravity.LEFT, 1);
//		setListener2();

	}

	private void setListener2() {
		qingxiButton.setOnClickListener(this);
		gaoqingButton.setOnClickListener(this);
		liuchangButton.setOnClickListener(this);
	}


}
