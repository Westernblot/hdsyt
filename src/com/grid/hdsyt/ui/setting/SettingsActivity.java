package com.grid.hdsyt.ui.setting;

import java.util.Map;

import com.grid.hdsyt.R;
import com.grid.hdsyt.function.SpUserHelper;
import com.grid.hdsyt.ui.setting.fragment.FragmentAbout;
import com.grid.hdsyt.ui.setting.fragment.FragmentPrinter;
import com.grid.hdsyt.ui.setting.fragment.FragmentSync;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettingsActivity extends FragmentActivity implements
		OnClickListener {

	private TextView tv_btn_back; // 返回
	private TextView tv_user_name; // 登陆用户

	// 左侧菜单栏
	private TextView tv_btn_sync; // 同步
	private TextView tv_btn_printer; // 打印与外设
	private TextView tv_btn_about; // 关于我们

	// fragment的对象
	private FragmentSync fgSync;
	private FragmentAbout fgAbout;
	private FragmentPrinter fgPrinter;

	// 定义FragmentManager对象
	FragmentManager fManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		fManager = getSupportFragmentManager();

		Map<String, Object> userMap = SpUserHelper
				.getUserInfo(SettingsActivity.this);

		// ======初始化组件========
		tv_btn_back = (TextView) findViewById(R.id.tv_btn_back);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_btn_sync = (TextView) findViewById(R.id.tv_btn_sync);
		tv_btn_printer = (TextView) findViewById(R.id.tv_btn_printer);
		tv_btn_about = (TextView) findViewById(R.id.tv_btn_about);

		tv_user_name.setText(userMap.get("cnname").toString());

		tv_btn_back.setOnClickListener(this);
		tv_btn_sync.setOnClickListener(this);
		tv_btn_printer.setOnClickListener(this);
		tv_btn_about.setOnClickListener(this);
		setChioceItem(0);

	}

	// 重写 onclick 方法
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_btn_back:  //设置返回按键
			this.finish();
			break;
		case R.id.tv_btn_sync: // 同步
			setChioceItem(0);
			break;
		case R.id.tv_btn_about: // 关于我们
			setChioceItem(1);
			break;
		case R.id.tv_btn_printer: // 打印与外设
			setChioceItem(2);
			break;
		default:
			break;
		}
	}

	// 定义一个选中一个item后的处理
	private void setChioceItem(int index) {
		// TODO Auto-generated method stub
		// 重置选项+隐藏所有Fragment
		FragmentTransaction transaction = fManager.beginTransaction();
		hideFragments(transaction);
		switch (index) {
		case 0:
			if (fgSync == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				fgSync = new FragmentSync();
				transaction.add(R.id.fg_content, fgSync);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(fgSync);
			}
			break;

		case 1:
			if (fgAbout == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				fgAbout = new FragmentAbout();
				transaction.add(R.id.fg_content, fgAbout);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(fgAbout);
			}
			break;
		case 2:
			if (fgPrinter == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				fgPrinter = new FragmentPrinter();
				transaction.add(R.id.fg_content, fgPrinter);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(fgPrinter);
			}
			break;

		}
		transaction.commit();
	}

	// 隐藏所有的Fragment,避免fragment混乱
	private void hideFragments(FragmentTransaction transaction) {
		if (fgSync != null) {
			transaction.hide(fgSync);
		}
		if (fgAbout != null) {
			transaction.hide(fgAbout);
		}
		if (fgPrinter != null) {
			transaction.hide(fgPrinter);
		}
	}

}
