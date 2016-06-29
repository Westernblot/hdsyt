package com.grid.hdsyt.ui.clearing;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.grid.hdsyt.R;
import com.grid.hdsyt.function.SpUserHelper;
import com.grid.hdsyt.print.Const;
import com.grid.hdsyt.print.NetPrinter;
import com.grid.hdsyt.ui.main.MainActivity;
import com.grid.hdsyt.ui.setting.SettingsActivity;
import com.grid.hdsyt.utils.DateUtil;
import com.grid.hdsyt.utils.KeyBoardUtils;
import com.grid.hdsyt.utils.SPUtils;
import com.grid.hdsyt.utils.T;

public class ClearingActivity extends Activity implements OnClickListener {

	protected static final int CLEARING_SUCCESS = 0;
	protected static final int CLEARING_ERROR = 1;
	protected static final int SEARCH_MEMBER_RES = 2;
	protected static final int ADD_MEMBER_SUCCESS = 3;
	protected static final int ADD_MEMBER_ERROR = 4;

	private TextView tv_clearing_total_money; // 结算价格
	private TextView tv_btn_close; // 关闭
	private EditText et_discount; // 折扣优惠
	private EditText et_discount_money; // 折扣应收
	private EditText et_bid_money; // 成交价格
	private EditText et_shishou; // 实收
	private EditText et_zhaoling; // 找零
	private Button btn_complete; // 完成
	private CheckBox cb_printer; // 打印小票

	// -----------------会员部分--------------------------
	private EditText et_search_memberphone;
	private Button btn_search_member; // 查询会员
	private Button btn_add_member; // 添加会员
	private TextView tv_membername;
	private TextView tv_memberphone;
	private Map<String, Object> memberMap = new HashMap<String, Object>(); // 会员mp

	// ---------添加会员对话框----------
	private AlertDialog.Builder builder;
	AlertDialog dialog;
	private EditText dialog_et_membername;
	private EditText dialog_et_memberphone;
	private Button dialog_btn_ok;
	private Button dialog_btn_cancel;

	// ----------------------------------------------

	private String orderno;
	private String clearingTotalMoney = "0"; // 结算金额
	private List<Map<String, Object>> uiMapList;

	// 数组按钮
	private Button btn_7;
	private Button btn_8;
	private Button btn_9;
	private Button btn_100;
	private Button btn_4;
	private Button btn_5;
	private Button btn_6;
	private Button btn_50;
	private Button btn_1;
	private Button btn_2;
	private Button btn_3;
	private Button btn_20;
	private Button btn_dian;
	private Button btn_0;
	private Button btn_tuige;
	private Button btn_10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clearing);

		Bundle bundle = this.getIntent().getExtras();
		// String name = bundle.getString("name");
		// Double price = bundle.getDouble("price");
		orderno = new Date().getTime()+"";
		clearingTotalMoney = bundle.getString("clearingTotalMoney");
		uiMapList = MainActivity.uiListMap;
		// T.showShort(ClearingActivity.this,
		// "clearingTotalMoney="+clearingTotalMoney);//接收的结算价格
		System.out.println("clearingTotalMoney=" + clearingTotalMoney);
		System.out.println(MainActivity.uiListMap); // 购买的商品列表

		// ----------------------初始化组件----------------------------------
		tv_btn_close = (TextView) findViewById(R.id.tv_btn_close);
		tv_clearing_total_money = (TextView) findViewById(R.id.tv_clearing_total_money);
		et_discount = (EditText) findViewById(R.id.et_discount);
		et_discount_money = (EditText) findViewById(R.id.et_discount_money);
		et_bid_money = (EditText) findViewById(R.id.et_bid_money);
		et_shishou = (EditText) findViewById(R.id.et_shishou);
		et_zhaoling = (EditText) findViewById(R.id.et_zhaoling);
		btn_complete = (Button) findViewById(R.id.btn_complete);
		cb_printer = (CheckBox) findViewById(R.id.cb_printer);

		// 会员部分
		et_search_memberphone = (EditText) findViewById(R.id.et_search_memberphone);
		btn_search_member = (Button) findViewById(R.id.btn_search_member); // 查询会员
		btn_add_member = (Button) findViewById(R.id.btn_add_member); // 添加会员
		tv_membername = (TextView) findViewById(R.id.tv_membername);
		tv_memberphone = (TextView) findViewById(R.id.tv_memberphone);
		KeyBoardUtils.hideSoftInputMethod(ClearingActivity.this,
				et_search_memberphone); // 屏蔽软键盘
		btn_search_member.setOnClickListener(this);
		btn_add_member.setOnClickListener(this);
		// ---------

		btn_7 = (Button) findViewById(R.id.btn_7);
		btn_8 = (Button) findViewById(R.id.btn_8);
		btn_9 = (Button) findViewById(R.id.btn_9);
		btn_100 = (Button) findViewById(R.id.btn_100);
		btn_4 = (Button) findViewById(R.id.btn_4);
		btn_5 = (Button) findViewById(R.id.btn_5);
		btn_6 = (Button) findViewById(R.id.btn_6);
		btn_50 = (Button) findViewById(R.id.btn_50);
		btn_1 = (Button) findViewById(R.id.btn_1);
		btn_2 = (Button) findViewById(R.id.btn_2);
		btn_3 = (Button) findViewById(R.id.btn_3);
		btn_20 = (Button) findViewById(R.id.btn_20);
		btn_dian = (Button) findViewById(R.id.btn_dian);
		btn_0 = (Button) findViewById(R.id.btn_0);
		btn_tuige = (Button) findViewById(R.id.btn_tuige);
		btn_10 = (Button) findViewById(R.id.btn_10);

		// ------------------------------ 屏蔽键盘，数据赋值 ---------------------------
		boundKeyBoard();

		tv_btn_close.setOnClickListener(this);
		btn_complete.setOnClickListener(this);
		tv_clearing_total_money.setText("￥" + clearingTotalMoney);
		et_discount.setText("100");
		et_discount_money.setText(clearingTotalMoney);
		et_bid_money.setText(clearingTotalMoney);

		// -----------------------------计算监听------------------------------------

		changeListener(et_discount);
		changeListener(et_bid_money);
		changeListener(et_shishou);

		// ------------------------------ end ------------------------------

	}

	/**
	 * editText 数值改变监听
	 * 
	 * @param btn
	 */
	private void changeListener(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable et) {
				// TODO Auto-generated method stub
				System.out.println(editText);
				comBackLittleMoney(editText);
			}
		});
	}

	// 绑定键盘操作
	private void boundKeyBoard() {
		KeyBoardUtils.hideSoftInputMethod(ClearingActivity.this, et_discount); // 屏蔽软键盘
		KeyBoardUtils.hideSoftInputMethod(ClearingActivity.this,
				et_discount_money); // 屏蔽软键盘
		KeyBoardUtils.hideSoftInputMethod(ClearingActivity.this, et_bid_money); // 屏蔽软键盘
		KeyBoardUtils.hideSoftInputMethod(ClearingActivity.this, et_shishou); // 屏蔽软键盘
		KeyBoardUtils.hideSoftInputMethod(ClearingActivity.this, et_zhaoling); // 屏蔽软键盘
		et_shishou.requestFocus(); // 给实收金额焦点

		boundNumKey(btn_7, "7");
		boundNumKey(btn_8, "8");
		boundNumKey(btn_9, "9");
		boundNumKey(btn_100, "100");
		boundNumKey(btn_4, "4");
		boundNumKey(btn_5, "5");
		boundNumKey(btn_6, "6");
		boundNumKey(btn_50, "50");
		boundNumKey(btn_1, "1");
		boundNumKey(btn_2, "2");
		boundNumKey(btn_3, "3");
		boundNumKey(btn_20, "20");
		boundNumKey(btn_dian, ".");
		boundNumKey(btn_0, "0");
		boundNumKey(btn_10, "10");
		// 退格
		btn_tuige.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (et_discount.hasFocus()) {
					int cursor = et_discount.getSelectionStart();
					if (cursor >= 1) {
						et_discount.getText().delete(cursor - 1, cursor);
					}
				} else if (et_discount_money.hasFocus()) {
					int cursor = et_discount_money.getSelectionStart();
					if (cursor >= 1) {
						et_discount_money.getText().delete(cursor - 1, cursor);
					}

				} else if (et_bid_money.hasFocus()) {
					int cursor = et_bid_money.getSelectionStart();
					if (cursor >= 1) {
						et_bid_money.getText().delete(cursor - 1, cursor);
					}
				} else if (et_shishou.hasFocus()) {
					int cursor = et_shishou.getSelectionStart();
					if (cursor >= 1) {
						et_shishou.getText().delete(cursor - 1, cursor);
					}
				} else if (et_zhaoling.hasFocus()) {
					int cursor = et_zhaoling.getSelectionStart();
					if (cursor >= 1) {
						et_zhaoling.getText().delete(cursor - 1, cursor);
					}
				} else if (et_search_memberphone.hasFocus()) {
					int cursor = et_search_memberphone.getSelectionStart();
					if (cursor >= 1) {
						et_search_memberphone.getText().delete(cursor - 1,
								cursor);
					}
				}
			}
		});

	}

	/**
	 * 通用数字键盘操作
	 * 
	 * @param button
	 * @param num
	 */
	private void boundNumKey(Button button, final String num) {
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (et_discount.hasFocus()) {
					int cursor = et_discount.getSelectionStart();
					et_discount.getText().insert(cursor, num);
				} else if (et_discount_money.hasFocus()) {
					int cursor = et_discount_money.getSelectionStart();
					et_discount_money.getText().insert(cursor, num);
				} else if (et_bid_money.hasFocus()) {
					int cursor = et_bid_money.getSelectionStart();
					et_bid_money.getText().insert(cursor, num);
				} else if (et_shishou.hasFocus()) {
					int cursor = et_shishou.getSelectionStart();
					et_shishou.getText().insert(cursor, num);
				} else if (et_zhaoling.hasFocus()) {
					int cursor = et_zhaoling.getSelectionStart();
					et_zhaoling.getText().insert(cursor, num);
				} else if (et_search_memberphone.hasFocus()) {
					int cursor = et_search_memberphone.getSelectionStart();
					et_search_memberphone.getText().insert(cursor, num);
				}
			}
		});
	}

	/**
	 * 计算找零
	 */
	private void comBackLittleMoney(EditText editText) {

		String discount = et_discount.getText().toString().trim();
		String bid_money = et_bid_money.getText().toString().trim();
		String shishou = et_shishou.getText().toString().trim();

		if (TextUtils.isEmpty(discount)) {
			discount = "0";
		}
		if (TextUtils.isEmpty(bid_money)) {
			bid_money = "0";
		}
		if (TextUtils.isEmpty(shishou)) {
			shishou = "0";
		}

		System.out.println("========正在计算=========");
		if (editText.getId() == R.id.et_discount) { // 计算折后应收

			float com_clearingTotalMoney = Float.valueOf(clearingTotalMoney);
			float com_discount = Float.valueOf(discount);
			float com_discount_money = (com_clearingTotalMoney * com_discount) / 100;
			String give_com_discount_money = String.format("%.1f",
					com_discount_money);
			et_discount_money.setText(give_com_discount_money); // 折后价格
			et_bid_money.setText(give_com_discount_money);

		} else {
			float com_bid_money = Float.valueOf(bid_money);
			float com_shishou = Float.valueOf(shishou);

			String give_com_zhaoling = String.format("%.1f",
					(com_shishou - com_bid_money));
			et_zhaoling.setText(give_com_zhaoling); // 找零
		}

	}

	// 重写 onclick 方法
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_btn_close: // 关闭按钮
			finish();
			break;
		case R.id.btn_complete: // 结算完成按钮
			// String shishou = et_shishou.getText().toString().trim();
			// String zhaoling = et_zhaoling.getText().toString().trim();
			// System.out.println("shishou==" + shishou);
			// System.out.println("zhaoling==" + zhaoling);
			// if (TextUtils.isEmpty(shishou) || TextUtils.isEmpty(zhaoling)
			// || zhaoling.contains("-")) {
			// T.showShort(ClearingActivity.this, "数据有误，无法结算！");
			// } else {
			cpClearing();// 结算操作
			// }

			if (cb_printer.isChecked()) { // 如果选择打印小票，则打印
				printXP();
			}

			break;
		case R.id.btn_search_member: // 查询会员按钮
			if (TextUtils.isEmpty(et_search_memberphone.getText().toString()
					.trim())) {
				T.showShort(ClearingActivity.this, "手机号码不能为空!");
			} else {
				searchMember();
			}
			break;
		case R.id.btn_add_member: // 添加会员
			builder = new Builder(ClearingActivity.this);
			dialog = builder.create();
			View contentView = View.inflate(ClearingActivity.this,
					R.layout.dialog_member, null);

			// 数据操作
			dialog_et_membername = (EditText) contentView
					.findViewById(R.id.dialog_et_membername);
			dialog_et_memberphone = (EditText) contentView
					.findViewById(R.id.dialog_et_memberphone);
			dialog_btn_ok = (Button) contentView
					.findViewById(R.id.dialog_btn_ok);
			dialog_btn_cancel = (Button) contentView
					.findViewById(R.id.dialog_btn_cancel);
			// 取消按钮点击
			dialog_btn_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			// 确定按钮点击
			dialog_btn_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String name = dialog_et_membername.getText().toString()
							.trim();
					String phone = dialog_et_memberphone.getText().toString()
							.trim();
					if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)
							&& phone.length() == 11) {
						addMember();
					} else {
						T.showShort(ClearingActivity.this, "姓名或者手机输入有误！");
					}
				}
			});

			dialog.setView(contentView);
			dialog.show();
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.width = (int) (display.getWidth() * 0.8); // 设置宽度
			lp.height = (int) (display.getWidth() * 0.7); // 设置高度
			System.out.println("宽" + display.getWidth() * 0.8);
			System.out.println("高" + display.getHeight() * 0.8);
			dialog.getWindow().setAttributes(lp);
			break;
		default:
			break;
		}
	}

	/**
	 * 添加会员
	 */
	private void addMember() {
		showProgressDialog("正在添加...");
		new Thread() {
			public void run() {
				String name = dialog_et_membername.getText().toString().trim();
				String phone = dialog_et_memberphone.getText().toString()
						.trim();
				boolean status = ClearingProvide.addMember(
						ClearingActivity.this, name, phone);
				Message msg = handler.obtainMessage();
				if (status) {
					msg.what = ADD_MEMBER_SUCCESS;
					Map<String, String> mp = new HashMap<String, String>();
					mp.put("name", name);
					mp.put("phone", phone);
					msg.obj = mp;
				} else {
					msg.what = ADD_MEMBER_ERROR;
				}
				msg.sendToTarget();
			};
		}.start();
	}

	/**
	 * 查找会员
	 */
	private void searchMember() {
		showProgressDialog("正在查询...");
		new Thread() {
			public void run() {
				String phone = et_search_memberphone.getText().toString()
						.trim();
				Map<String, Object> mp = ClearingProvide.findMemberByPhone(
						ClearingActivity.this, phone);
				Message msg = handler.obtainMessage(SEARCH_MEMBER_RES, mp);
				msg.sendToTarget();
			};
		}.start();
	}

	/**
	 * 结算操作
	 */
	private void cpClearing() {
		// showProgressDialog("正在结算...");
		new Thread() {
			public void run() {
				String discount = et_discount.getText().toString().trim();
				String discountprice = et_discount_money.getText().toString()
						.trim();
				String bid = et_bid_money.getText().toString().trim();
				String membername = "";
				String memberphone = "";
				if (!TextUtils.isEmpty(tv_memberphone.getText().toString()
						.trim())) {
					membername = tv_membername.getText().toString().trim();
					memberphone = tv_memberphone.getText().toString().trim();
				}
				boolean status = ClearingProvide.postClearingData(
						ClearingActivity.this, uiMapList, clearingTotalMoney,
						discount, discountprice, bid, membername, memberphone,orderno);
				Message msg = handler.obtainMessage();
				if (status) {
					msg.what = CLEARING_SUCCESS;
				} else {
					msg.what = CLEARING_ERROR;
				}
				msg.sendToTarget();
			};
		}.start();
	}

	/**
	 * 打印小票操作
	 */
	private void printXP() {

		new Thread() {
			public void run() {

				Map<String, Object> userMap = SpUserHelper
						.getUserInfo(ClearingActivity.this);
				String cnname=userMap.get("cnname").toString();
				
				NetPrinter printer = new NetPrinter();
				
				String ip = (String) SPUtils.get(ClearingActivity.this, "ip", "127.0.0.1");
				String port = (String) SPUtils.get(ClearingActivity.this, "port", "9100");
				
				boolean isOpen = printer.Open(ip, Integer.parseInt(port));
				if(!isOpen){
					T.showShort(ClearingActivity.this, "打印机连接失败！！");
					return;
				}
				
				printer.PrintText("聚商城.收银台", 1, 1, 0);
				printer.PrintEnter();				
				printer.PrintText("------------------------------------------------", 0, 0, 0);
				printer.PrintEnter();
				printer.PrintText("订单号："+orderno+"　　　　操作员："+cnname, 0, 0, 0);
				printer.PrintEnter();
				printer.PrintText("------------------------------------------------", 0, 0, 0);
				printer.PrintEnter();
				printer.PrintText("商品　　　　　　　　　　单价　　数量　　小计", 0, 0, 0);
				printer.PrintEnter();
				printer.PrintText("------------------------------------------------", 0, 0, 0);
				printer.PrintEnter();
				for(int i = 0 ;i<uiMapList.size();i++){
					
					String name = uiMapList.get(i).get("name").toString().trim();
					String num = uiMapList.get(i).get("num").toString().trim();
					String price = uiMapList.get(i).get("price").toString().trim();
					float new_num = Float.valueOf(num);
					float new_price = Float.valueOf(price);

					float money = new_num * new_price;
					String new_money = String.format("%.1f", money);
					
					printer.PrintText(fillStr(name,12,"ZH")+fillStr(price,8,"EN")+fillStr(num,8,"EN")+fillStr(new_money,8,"EN"), 0, 0, 0);
					printer.PrintEnter();
				}
//				printer.PrintText("示例商品1　　　　2.00　　5　　10.0", 0, 0, 0);
//				printer.PrintEnter();
//				printer.PrintText("示例商品2　　　　2.00　　5　　10.0", 0, 0, 0);
//				printer.PrintEnter();
//				printer.PrintText("示例商品3　　　　2.00　　5　　10.0", 0, 0, 0);
//				printer.PrintEnter();
				printer.PrintText("------------------------------------------------", 0, 0, 0);
				printer.PrintEnter();
				printer.PrintText("总金额　"+clearingTotalMoney, 2, 1, 0);
				printer.PrintEnter();
				printer.PrintText("------------------------------------------------", 0, 0, 0);
				printer.PrintEnter();
				printer.PrintText(DateUtil.getTime(), 2, 0, 0);
				printer.PrintEnter();
				printer.PrintText("谢谢惠顾", 1, 1, 0);
				printer.PrintNewLines(5);
				printer.CutPage(0);
				printer.Close();
				

				System.out.println("=========打印OK？？？===========");
			};
		}.start();

	}
	
	
	/**
	 * 打印补全
	 */
	private String fillStr(String str,int len,String mode){
		int strLen = str.length();
		String fill=" ";
		if(mode.equals("ZH")){
			fill="　";
		}
		if(strLen<=len){
			for(int i=0;i<(len-strLen);i++){
				str+=fill;
			}
		}else{
			str=str.substring(0, len)+".";
		}
		return str;
	}
	

	// ui线程
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			hideProgressDialog();
			switch (msg.what) {
			case CLEARING_SUCCESS:
				Intent data = new Intent();
				data.putExtra("status", true);
				setResult(0, data);
				// 当前页面关闭掉
				finish();
				break;
			case CLEARING_ERROR:
				T.showShort(ClearingActivity.this, "结算失败！");
				break;
			case SEARCH_MEMBER_RES:
				memberMap = (Map<String, Object>) msg.obj;
				if (memberMap != null) {
					String name = memberMap.get("name").toString();
					String phone = memberMap.get("phone").toString();
					tv_membername.setText(name);
					tv_memberphone.setText(phone);
				} else {
					tv_membername.setText("无相关数据");
					tv_memberphone.setText("");
				}
				break;
			case ADD_MEMBER_SUCCESS:
				dialog.dismiss();
				Map<String, String> resAddMemberMap = (Map<String, String>) msg.obj;
				String name = resAddMemberMap.get("name").toString();
				String phone = resAddMemberMap.get("phone").toString();
				tv_membername.setText(name);
				tv_memberphone.setText(phone);
				break;
			case ADD_MEMBER_ERROR:
				T.showShort(ClearingActivity.this, "添加失败！");
				break;
			default:
				break;
			}
		};
	};

	// ===================================分隔线==================================

	private ProgressDialog progressDialog;

	/**
	 * 提示加载
	 */
	public void showProgressDialog(String msg) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(ClearingActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(msg);
		}
		progressDialog.show();

	}

	/**
	 * 隐藏提示加载
	 */
	public void hideProgressDialog() {

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}

}
