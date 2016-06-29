package com.grid.hdsyt.ui.setting.manulsync;

import com.grid.hdsyt.R;
import com.grid.hdsyt.ui.syncdata.SyncdataProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ManulsyncActivity extends Activity implements OnClickListener {

	private RelativeLayout rel_loading;
	private TextView sync_ok;
	private TextView tv_btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manulsync);
		
		rel_loading = (RelativeLayout) findViewById(R.id.rel_loading);
		sync_ok = (TextView) findViewById(R.id.sync_ok);
		tv_btn_back =(TextView) findViewById(R.id.tv_btn_back);
		
		tv_btn_back.setOnClickListener(this);
		
		rel_loading.setVisibility(View.VISIBLE);
		sync_ok.setVisibility(View.INVISIBLE);

		syncData();

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				rel_loading.setVisibility(View.INVISIBLE);
				sync_ok.setVisibility(View.VISIBLE);
			}
		}, 3000);
	}

	
	/**
	 * 同步数据
	 */
	private void syncData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {

				SyncdataProvider.syncTbUser(ManulsyncActivity.this);
				SyncdataProvider.syncTbStock(ManulsyncActivity.this);
				SyncdataProvider.syncTbMember(ManulsyncActivity.this);
				SyncdataProvider.postTbDetail(ManulsyncActivity.this);

			};
		}.start();
	}

	// ui线程
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	//重写onclick 方法
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		this.finish();
	}


}
