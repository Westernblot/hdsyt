package com.grid.hdsyt.ui.webview;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.grid.hdsyt.R;

public class WebviewActivity extends Activity {

	private WebView webView;
	private ProgressBar pBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		//初始化组件
		webView = (WebView) findViewById(R.id.webView);
		pBar = (ProgressBar) findViewById(R.id.pBar);
		
		init();
	}

	private void init() {
		
		// WebView加载web资源
		webView.loadUrl("http://cxcxt.com");
		//启用支持javascript
		webView.getSettings().setJavaScriptEnabled(true);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		
		//加载进度
		webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
            	pBar.setVisibility(View.VISIBLE);
                if (newProgress == 100) {
                    // 网页加载完成
                	pBar.setVisibility(View.INVISIBLE);
                } else {
                    // 加载中
                	pBar.setProgress(newProgress); 
                }

            }
        });
		
	}
	
	//改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
            else
            {
                //System.exit(0);//退出程序
            	finish();  //关闭 Activity
            }
        }
        return super.onKeyDown(keyCode, event);
    }
	
}
