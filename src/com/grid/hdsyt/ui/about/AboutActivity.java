package com.grid.hdsyt.ui.about;

import com.grid.hdsyt.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AboutActivity extends Activity implements OnClickListener {

	private TextView tv_btn_back; // 返回
	private WebView webView;
	private ProgressBar pBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		// ===初始化按钮
		tv_btn_back = (TextView) findViewById(R.id.tv_btn_back);
		webView = (WebView) findViewById(R.id.webView);
		pBar = (ProgressBar) findViewById(R.id.pBar);

		tv_btn_back.setOnClickListener(this);
		
		init();
		
		
	}

	private void init() {

		// WebView加载web资源
		webView.loadUrl("file:///android_asset/html/about.html");
		// 启用支持javascript
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

		// 加载进度
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
	
	
    //重写onclick 方法
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		this.finish();
	}

}
