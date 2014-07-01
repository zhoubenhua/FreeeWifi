package com.zhephone.hotspot.app.ui;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.media.AudioManager;
import android.media.MediaPlayer; 
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.BaseActivity;


public class VideoActivity extends BaseActivity implements OnBufferingUpdateListener, 
	OnCompletionListener,MediaPlayer.OnPreparedListener, 
	SurfaceHolder.Callback {
	private MediaPlayer mediaPlayer; 
    private SurfaceView surfaceView; 
    private SurfaceHolder surfaceHolder; 
    private int videoWidth; 
    private SeekBar progressBar;
    private int videoHeight;
    private Button startPlayerBt;
    private Button pausePlayerBt;
    private Timer mTimer;  

	@Override
	protected void initViews() {
        this.surfaceView = (SurfaceView) this.findViewById(R.id.surface); 
        this.surfaceHolder = this.surfaceView.getHolder(); 
        this.surfaceHolder.addCallback(this); 
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
        this.progressBar = (SeekBar)findViewById(R.id.progress_bar);
        startPlayerBt = (Button)findViewById(R.id.play_bt);
        pausePlayerBt = (Button)findViewById(R.id.pause_bt);
	}
	
	  /******************************************************* 
     * 通过定时器和Handler来更新进度条 
     ******************************************************/  
    TimerTask mTimerTask = new TimerTask() {  
        @Override  
        public void run() {  
            if(mediaPlayer==null)  
                return;  
            if (mediaPlayer.isPlaying() && progressBar.isPressed() == false) {  
                handleProgress.sendEmptyMessage(0);  
            }  
        }  
    }; 
    
    
      
    Handler handleProgress = new Handler() {  
        public void handleMessage(Message msg) {  
            int position = mediaPlayer.getCurrentPosition();  
            int duration = mediaPlayer.getDuration();  
            if (duration > 0) {  
                long pos = progressBar.getMax() * position / duration;  
                progressBar.setProgress((int) pos);  
            }  
        };  
    }; 
    
	
	 private void playVideo() throws IllegalArgumentException, 
     	IllegalStateException, IOException { 
		 this.mediaPlayer = new MediaPlayer(); 
		 mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("http://192.168.0.182:8080/video/test001.mp4"));
//		 this.mediaPlayer 
//		         .setDataSource("/sdcard/daoxiang.3gp"); 
		 this.mediaPlayer.setDisplay(this.surfaceHolder); 
		 this.mediaPlayer.prepare(); 
		 this.mediaPlayer.setOnBufferingUpdateListener(this); 
		 this.mediaPlayer.setOnPreparedListener(this); 
		 this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
		 mTimer=new Timer();  
		 mTimer.schedule(mTimerTask, 0, 1000);  

	 }

	 @Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			this.setContentView(R.layout.video_surface);
			this.setUpViews();
		}

	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	
	private void startPlayerVideo() {
		if(!mediaPlayer.isPlaying()) {
			mediaPlayer.start();  
		}
	}
	
	private void pausePlayerVideo() {
		 mediaPlayer.pause();   
	}
	
	private OnClickListener startPlayerVideoListener = new OnClickListener() {
		public void onClick(View v) {
			startPlayerVideo();
		}
		
	};
	
	private OnClickListener pausePlayerVideoListener = new OnClickListener() {
		public void onClick(View v) {
			pausePlayerVideo(); 
		}
	};
	
	class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {  
        int progress;  
  
        @Override  
        public void onProgressChanged(SeekBar seekBar, int progress,  
                boolean fromUser) {  
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()  
            this.progress = progress * mediaPlayer.getDuration()  
                    / seekBar.getMax();  
        }  
  
        @Override  
        public void onStartTrackingTouch(SeekBar seekBar) {  
  
        }  
  
        @Override  
        public void onStopTrackingTouch(SeekBar seekBar) {  
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字  
        	mediaPlayer.seekTo(progress);  
        }  
    }  

	@Override
	protected void addListener() {
		progressBar.setOnSeekBarChangeListener(new SeekBarChangeListener());	
		startPlayerBt.setOnClickListener(startPlayerVideoListener);							
		pausePlayerBt.setOnClickListener(pausePlayerVideoListener);												
	}			

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			this.playVideo(); 
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } 
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		this.videoWidth = this.mediaPlayer.getVideoWidth(); 
        this.videoHeight = this.mediaPlayer.getVideoHeight();
        if (this.videoHeight != 0 && this.videoWidth != 0) { 
            this.surfaceHolder.setFixedSize(this.videoWidth, this.videoHeight); 
            this.mediaPlayer.start(); 
        }

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
//		try {
//			mTimer.cancel();
//			mTimer = null;
//			mediaPlayer.release();
//			mediaPlayer = null;
//			playVideo(); 
//	    } catch (Exception e) {
//	    	e.printStackTrace();
//	    } 
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (this.mediaPlayer != null) { 
			this.mediaPlayer.release(); 
	        this.mediaPlayer = null; 
	    } 
	}

}
