package com.grid.hdsyt.ui.setting.fragment;

import com.grid.hdsyt.R;
import com.grid.hdsyt.print.Const;
import com.grid.hdsyt.print.NetPrinter;
import com.grid.hdsyt.ui.clearing.ClearingActivity;
import com.grid.hdsyt.ui.setting.PrintSettingActivity;
import com.grid.hdsyt.utils.SPUtils;
import com.grid.hdsyt.utils.T;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentPrinter extends Fragment implements OnClickListener {

	private RelativeLayout tv_btn_print_test;
	private RelativeLayout tv_btn_printer;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_printer, container,
				false);

		tv_btn_print_test = (RelativeLayout) view
				.findViewById(R.id.tv_btn_print_test);
		tv_btn_printer = (RelativeLayout) view
				.findViewById(R.id.tv_btn_printer);

		tv_btn_print_test.setOnClickListener(this);
		tv_btn_printer.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_btn_printer:
			Intent intent = new Intent(getActivity(),
					PrintSettingActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_btn_print_test:

			new Thread() {
				public void run() {

					NetPrinter printer = new NetPrinter();

					String ip = (String) SPUtils.get(getActivity(), "ip", "127.0.0.1");
					String port = (String) SPUtils.get(getActivity(), "port",
							"9100");

					boolean isOpen = printer.Open(ip, Integer.parseInt(port));
					if (!isOpen) {
						T.showShort(getActivity(), "打印机连接失败！！");
						return;
					}

					printer.PrintText("聚商城.收银台", 1, 1, 0);
					printer.PrintEnter();
					printer.PrintText(
							"------------------------------------------------",
							0, 0, 0);
					printer.PrintEnter();
					printer.PrintText("订单号：000000000　　　　操作员：系统管理员", 0, 0, 0);
					printer.PrintEnter();
					printer.PrintText(
							"------------------------------------------------",
							0, 0, 0);
					printer.PrintEnter();
					printer.PrintText("商品　　　　单价　　 数量　　小计", 0, 0, 0);
					printer.PrintEnter();
					printer.PrintText(
							"------------------------------------------------",
							0, 0, 0);
					printer.PrintEnter();
					printer.PrintText("示例商品1　　　　2.00　　5　　10.0", 0, 0, 0);
					printer.PrintEnter();
					printer.PrintText("示例商品2　　　　2.00　　5　　10.0", 0, 0, 0);
					printer.PrintEnter();
					printer.PrintText("示例商品3　　　　2.00　　5　　10.0", 0, 0, 0);
					printer.PrintEnter();
					printer.PrintText(
							"------------------------------------------------",
							0, 0, 0);
					printer.PrintEnter();
					printer.PrintText("总金额　30.0", 2, 1, 0);
					printer.PrintEnter();
					printer.PrintText(
							"------------------------------------------------",
							0, 0, 0);
					printer.PrintEnter();
					printer.PrintText("2015-12-30 11:03:00", 2, 0, 0);
					printer.PrintEnter();
					printer.PrintText("谢谢惠顾", 1, 1, 0);
					printer.PrintNewLines(5);
					printer.CutPage(0);
					printer.Close();

				};
			}.start();

			break;

		default:
			break;
		}

	}

}
