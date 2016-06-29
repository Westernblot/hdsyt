package com.grid.hdsyt.ui.setting;

import java.util.Map;

import com.grid.hdsyt.R;
import com.grid.hdsyt.function.SpUserHelper;
import com.grid.hdsyt.ui.about.AboutActivity;
import com.grid.hdsyt.utils.SPUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener {

	private TextView tv_btn_back; // 返回
	private TextView tv_btn_sync; // 同步
	private TextView tv_btn_about; // 关于我们
	private TextView tv_user_name; // 登陆用户

	private Switch switch_sync; // 自动更新开关
	private RelativeLayout rl_timer_interval;// 自动更新间隔点击
	private TextView tv_timer_interval;//自动更新间隔时间显示
	private String[] intervalArr = new String[] { "1小时", "2小时","3小时","4小时","5小时" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		Map<String, Object> userMap = SpUserHelper
				.getUserInfo(SettingActivity.this);
		int timerInterval = (Integer) SPUtils.get(SettingActivity.this, "timerInterval", 0);

		// 初始化组件
		tv_btn_back = (TextView) findViewById(R.id.tv_btn_back);
		tv_btn_about = (TextView) findViewById(R.id.tv_btn_about);
		tv_btn_sync = (TextView) findViewById(R.id.tv_btn_sync);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		switch_sync = (Switch) findViewById(R.id.switch_sync);
		rl_timer_interval = (RelativeLayout) findViewById(R.id.rl_timer_interval);
		tv_timer_interval = (TextView) findViewById(R.id.tv_timer_interval);		
		tv_timer_interval.setText(timerInterval+1+"小时");   //回显sp保存的自动更新间隔时间

		tv_user_name.setText(userMap.get("cnname").toString());
		tv_btn_back.setOnClickListener(this);
		tv_btn_about.setOnClickListener(this);
		tv_btn_sync.setOnClickListener(this);
		rl_timer_interval.setOnClickListener(this);
		switch_sync.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean status) {
				// TODO Auto-generated method stub
				SPUtils.put(SettingActivity.this, "timerStatus", status); // 保存自动更新状态到sp中
				if (status) {
					rl_timer_interval.setVisibility(View.VISIBLE);
				} else {
					rl_timer_interval.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	// 重写 onclick 方法
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_btn_back: // 返回
			this.finish();
			break;
		case R.id.tv_btn_sync: // 同步

			break;
		case R.id.tv_btn_about: // 关于
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_timer_interval: // 自动同步间隔
			
			int timerInterval = (Integer) SPUtils.get(SettingActivity.this, "timerInterval", 0);
			
			new AlertDialog.Builder(this)
					.setTitle("自动同步间隔")
					.setSingleChoiceItems(intervalArr, timerInterval,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									SPUtils.put(SettingActivity.this, "timerInterval", which); // 保存自动更新状态到sp中
									tv_timer_interval.setText(intervalArr[which]);
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).show();

			break;
		default:
			break;
		}
	}
}
