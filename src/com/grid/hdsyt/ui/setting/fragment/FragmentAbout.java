package com.grid.hdsyt.ui.setting.fragment;

import com.grid.hdsyt.R;
import com.grid.hdsyt.R.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class FragmentAbout extends Fragment {

	private WebView webView;
	private ProgressBar pBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_about, container,false);
		
		webView = (WebView) view.findViewById(R.id.webView);
		pBar = (ProgressBar) view.findViewById(R.id.pBar);
		init();
		
		return view;
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
	
}
