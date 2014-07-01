package com.zhephone.hotspot.app.ui;
import org.apache.commons.lang.StringUtils;
import com.zhephone.hotspot.R;
import com.zhephone.hotspot.app.BaseActivity;
import com.zhephone.hotspot.app.common.Constant;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebActivity extends BaseActivity {
	private WebView webWv;			

	@Override
	protected void initViews() {
		webWv = (WebView)findViewById(R.id.web_wv);  	
		webWv.setWebViewClient(new WebViewClient(){                    
			public boolean shouldOverrideUrlLoading(WebView view, String urlStr) {      
				showProgressDialog(null, getResources().getString(R.string.loading));
				view.loadUrl(urlStr); 	
				return true;         		            
			}     		 		          
		}); 				
		webWv.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int newProgress) {
				if (100 == newProgress) {	
					dissmissProgressDialog();
				} 
			}			
		});
		WebSettings webSetings = webWv.getSettings(); 
		webSetings.setJavaScriptEnabled(true);	
		webSetings.setSaveFormData(true);
		webSetings.setSavePassword(true);
		webSetings.setSupportZoom(true);  
		webSetings.setBuiltInZoomControls(true);
		webSetings.setDefaultTextEncodingName("UTF-8");
		webWv.requestFocus(View.FOCUS_DOWN); 
	}
	
//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && webWv.canGoBack()) {
//        	webWv.goBack();
//        	return true;
//        }
//        return super.onKeyDown(keyCode, event);  
//    }


	@Override
	protected void initData() {
		String url = this.getIntent().getStringExtra(Constant.WebIntentDef.WEB_URL);
		if(StringUtils.isNotEmpty(url)) {
			webWv.loadUrl(url);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.web);
		this.setUpViews();
	}

}