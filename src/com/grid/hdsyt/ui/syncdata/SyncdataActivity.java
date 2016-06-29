package com.grid.hdsyt.ui.syncdata;

import java.util.Timer;
import java.util.TimerTask;

import com.grid.hdsyt.R;
import com.grid.hdsyt.ui.main.MainActivity;
import com.grid.hdsyt.utils.SPUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SyncdataActivity extends Activity {

	private RelativeLayout rel_loading;
	private TextView sync_ok;

	// -------定时任务---------
	public static final Timer timer = new Timer();
	public static TimerTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_syncdata);

		rel_loading = (RelativeLayout) findViewById(R.id.rel_loading);
		sync_ok = (TextView) findViewById(R.id.sync_ok);

		// ---------------定时器任务代码开始----------------
		// 同步数据
		task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 定时器做的事情
				SyncdataProvider.syncTbUser(SyncdataActivity.this);
				SyncdataProvider.syncTbStock(SyncdataActivity.this);
				SyncdataProvider.syncTbMember(SyncdataActivity.this);
				SyncdataProvider.postTbDetail(SyncdataActivity.this);
				System.out
						.println("===============我是一个定时器，我被执行了！！===============");
			}
		};

		boolean timerStatus = (Boolean) SPUtils.get(this, "timerStatus", true);
		int timerInterval = (Integer) SPUtils.get(this, "timerInterval", 0);
		if (timerStatus) {
			timer.schedule(task, 0, (timerInterval + 1) * 60 * 60 * 1000);
		} else {
			syncData();
			System.out
					.println("==================定时器没有启动=======================");
		}
		// ---------------定时器任务代码结束----------------

		// -------------------同步数据------------------------
		rel_loading.setVisibility(View.VISIBLE);
		sync_ok.setVisibility(View.INVISIBLE);

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				rel_loading.setVisibility(View.INVISIBLE);
				sync_ok.setVisibility(View.VISIBLE);
				enterMain();
			}
		}, 5000);

	}

	/**
	 * 同步数据
	 */
	private void syncData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {

				SyncdataProvider.syncTbUser(SyncdataActivity.this);
				SyncdataProvider.syncTbStock(SyncdataActivity.this);
				SyncdataProvider.syncTbMember(SyncdataActivity.this);
				SyncdataProvider.postTbDetail(SyncdataActivity.this);

			};
		}.start();
	}

	// ui线程
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	/**
	 * 进入主页面
	 */
	private void enterMain() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SyncdataActivity.this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}
}
