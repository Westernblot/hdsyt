package com.grid.hdsyt.ui.setting.fragment;

import com.grid.hdsyt.R;
import com.grid.hdsyt.ui.setting.manulsync.ManulsyncActivity;
import com.grid.hdsyt.utils.SPUtils;
import com.grid.hdsyt.utils.T;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FragmentSync extends Fragment implements OnClickListener {

	private TextView tv_btn_click_sync; //点击手动更新
	private Switch switch_sync; // 自动更新开关
	private RelativeLayout rl_timer_interval;// 自动更新间隔点击
	private TextView tv_timer_interval;// 自动更新间隔时间显示
	private String[] intervalArr = new String[] { "1小时", "2小时", "3小时", "4小时",
			"5小时" };

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_sync, container, false);
		int timerInterval = (Integer) SPUtils.get(getActivity(),
				"timerInterval", 0);
		// ---------初始化组件----
		tv_btn_click_sync = (TextView) view.findViewById(R.id.tv_btn_click_sync);
		switch_sync = (Switch) view.findViewById(R.id.switch_sync);
		rl_timer_interval = (RelativeLayout) view
				.findViewById(R.id.rl_timer_interval);
		tv_timer_interval = (TextView) view
				.findViewById(R.id.tv_timer_interval);
		tv_timer_interval.setText(timerInterval + 1 + "小时"); // 回显sp保存的自动更新间隔时间
		tv_btn_click_sync.setOnClickListener(this);
		rl_timer_interval.setOnClickListener(this);

		// ---------------switch 开关----------------------
		boolean timerStatus = (Boolean) SPUtils.get(getActivity(),
				"timerStatus", true);
		switch_sync.setChecked(timerStatus);
		if (timerStatus) {
			rl_timer_interval.setVisibility(View.VISIBLE);
		} else {
			rl_timer_interval.setVisibility(View.INVISIBLE);
		}

		switch_sync.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean status) {
				// TODO Auto-generated method stub
				SPUtils.put(getActivity(), "timerStatus", status); // 保存自动更新状态到sp中
				if (status) {
					rl_timer_interval.setVisibility(View.VISIBLE);
				} else {
					rl_timer_interval.setVisibility(View.INVISIBLE);
				}
			}
		});
		return view;
	}

	// 重写onclick 方法
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.rl_timer_interval: // 自动同步间隔

			int timerInterval = (Integer) SPUtils.get(getActivity(),
					"timerInterval", 0);

			new AlertDialog.Builder(getActivity())
					.setTitle("自动同步间隔")
					.setSingleChoiceItems(intervalArr, timerInterval,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									SPUtils.put(getActivity(), "timerInterval",
											which); // 保存自动更新状态到sp中
									tv_timer_interval
											.setText(intervalArr[which]);
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).show();

			break;
		case R.id.tv_btn_click_sync:
			Intent intent = new Intent(getActivity(),ManulsyncActivity.class);
			startActivity(intent);
			//T.showShort(getActivity(), "手动更新的逻辑还没写呢！！");
			break;
		default:
			break;
		}
	}

}
