package com.grid.hdsyt.ui.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.grid.hdsyt.R;
import com.grid.hdsyt.ui.clearing.ClearingActivity;
import com.grid.hdsyt.ui.setting.SettingActivity;
import com.grid.hdsyt.ui.setting.SettingsActivity;
import com.grid.hdsyt.ui.webview.WebviewActivity;
import com.grid.hdsyt.utils.KeyBoardUtils;
import com.grid.hdsyt.utils.L;
import com.grid.hdsyt.utils.SPUtils;
import com.grid.hdsyt.utils.T;

public class MainActivity extends Activity implements OnClickListener {

	protected static final int NET_ERROR = 0;
	protected static final int SELECT_SUCCESS = 1;
	protected static final int SHOW_MY_CATEGORY = 2;
	protected static final int SHOW_MY_CATEGORY_GOODS_LIST = 3;
	protected static final int SHOW_MY_SUB_CATEGORY_GOODS_LIST = 4;
	protected static final int CHOOSE_GOODS_CART = 5;
	protected static final int SAME_GOODS = 6;
	protected static final int NO_FIND_GOODS = 7;
	protected static final int DIALOG_SEARCH_SUCCESS = 8;
	protected static final int DIALOG_SEARCH_ERROR = 9;

	// -----------------左边布局控件-----------------
	private Button btn_setting; // 外部设置
	private Button btn_clear_cart; // 清空结算车
	private TextView tv_total_money; // 总计钱数
	private ListView lv_cart_list;
	private ImageView img_success; // 结算成功图片...

	// ---------------右边布局控件-------------------
	private TextView tv_btn_fenlei; // 我的分类
	private TextView tv_btn_sub_fenlei; // 我的分类
	private EditText et_goods_code; // 商品查询编码输入
	private Button btn_search; // 手动搜索
	private Button btn_clearing; // 结算

	private GridView gv_stock_list;
	private MainGridAdapter gridAdapter;
	private List<Map<String, Object>> uiGridListMap = new ArrayList<Map<String, Object>>();

	// -----------------结算车数据-----------------
	private MyAdapter adapter;
	public static List<Map<String, Object>> uiListMap = new ArrayList<Map<String, Object>>();
	private String clearing_total_money = "0"; // 结算价钱

	// -----------------自定义对话框----------------------
	private Map<String, Object> dialogMap = new HashMap<String, Object>();
	private AlertDialog.Builder builder; // 自定义对话框
	AlertDialog dialog;
	View contentView;

	private EditText dialog_et_goods_code; // 对话框商品编码
	private Button dialog_btn_click_search; // 对话框点击搜索

	private TextView dialog_tv_goods_name;
	private EditText dialog_et_num;
	private EditText dialog_et_price;
	private Button dialog_btn_del;
	private Button dialog_btn_cancel;
	private Button dialog_btn_ok;
	// 数字按键
	private Button dialog_btn_7;
	private Button dialog_btn_8;
	private Button dialog_btn_9;
	private Button dialog_btn_4;
	private Button dialog_btn_5;
	private Button dialog_btn_6;
	private Button dialog_btn_1;
	private Button dialog_btn_2;
	private Button dialog_btn_3;
	private Button dialog_btn_dian;
	private Button dialog_btn_0;
	private Button dialog_btn_tuige;

	// 扫描枪一次扫描会触发2次回车事件
	long[] mHits = new long[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ===========初始化控件==========
		// ---左边布局----
		btn_setting = (Button) findViewById(R.id.btn_setting);
		btn_clear_cart = (Button) findViewById(R.id.btn_clear_cart);
		tv_total_money = (TextView) findViewById(R.id.tv_total_money);
		lv_cart_list = (ListView) findViewById(R.id.lv_cart_list);
		img_success = (ImageView) findViewById(R.id.img_success);
		btn_setting.setOnClickListener(this); // 设置
		btn_clear_cart.setOnClickListener(this);
		// ---右边布局-----
		tv_btn_fenlei = (TextView) findViewById(R.id.tv_btn_fenlei);
		tv_btn_sub_fenlei = (TextView) findViewById(R.id.tv_btn_sub_fenlei);
		et_goods_code = (EditText) findViewById(R.id.et_goods_code);
		KeyBoardUtils.hideSoftInputMethod(MainActivity.this, et_goods_code);// 屏蔽扫码软键盘
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_clearing = (Button) findViewById(R.id.btn_clearing);
		btn_search.setOnClickListener(this);
		btn_clearing.setOnClickListener(this);
		gv_stock_list = (GridView) findViewById(R.id.gv_stock_list);

		// 我的分类点击
		tv_btn_fenlei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				// T.showShort(MainActivity.this,
				// "wo de fenlei bei dian ji le !");
				// 加载我的分类
				loadMyFenlei();
			}
		});
		// 我的子分类点击
		tv_btn_sub_fenlei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// 加载我的分类
				loadMySubFenleiGoods();
			}
		});
		// =============end============

		// loadCartData();// 加载左边测试数据

		// 加载我的分类
		loadMyFenlei();

		boundListViewControl();// listview 视图item点击

		boundGridControl();// grid视图item点击

		boundAlertDialog();// 绑定弹出对话框

		// 监听 et_goods_code 查询商品
		et_goods_code.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) { // 敲击回车
					System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
					mHits[mHits.length - 1] = SystemClock.uptimeMillis();
					if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
						// Toast.makeText(this, "恭喜你，2次点击了。", 0).show();
						System.out.println("et_goods_code="
								+ et_goods_code.getText().toString().trim());
						// et_goods_code.setText("");
						System.out.println("捕捉到事件扫码事件。。。");

						// 去数据库查询商品 发送到购物车
						saomaGoods();

					}

				}
				return false;
			}
		});

	}

	/**
	 * 扫码添加商品
	 */
	private void saomaGoods() {
		if (img_success.getVisibility() == View.VISIBLE) { // 如果结算成功可见，则清空一遍结算车列表
			clearCartList();
		}
		new Thread() {
			public void run() {
				Map<String, Object> runMap = MainProvider.selectGoodsByCode(
						MainActivity.this, et_goods_code.getText().toString()
								.trim());
				Message msg = handler.obtainMessage();
				if (runMap.size() > 0) {
					if (!uiListMap.contains(runMap)) {
						msg.what = CHOOSE_GOODS_CART;
						msg.obj = runMap;
					} else {
						msg.what = SAME_GOODS;
					}
				} else {
					msg.what = NO_FIND_GOODS;
				}
				msg.sendToTarget();
			};
		}.start();
	}

	/**
	 * 加载我的分类
	 */
	private void loadMyFenlei() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				List<Map<String, Object>> runGridListMap = MainProvider
						.getMyStockFenlei(MainActivity.this);
				System.out.println("runGridListMap====" + runGridListMap);
				Message msg = handler.obtainMessage(SHOW_MY_CATEGORY,
						runGridListMap);
				msg.sendToTarget();
			};
		}.start();
	}

	/**
	 * 加载我的子分类下商品
	 */
	private void loadMySubFenleiGoods() {
		new Thread() {
			public void run() {
				String category = tv_btn_sub_fenlei.getText().toString().trim();
				List<Map<String, Object>> runGridListMap = MainProvider
						.getStockGoodsListByCategory(MainActivity.this,
								category);
				Message msg = handler.obtainMessage(
						SHOW_MY_SUB_CATEGORY_GOODS_LIST, runGridListMap);
				msg.sendToTarget();
			};
		}.start();
	}

	/**
	 * 绑定右边的 grid Item
	 */
	private void boundGridControl() {
		gv_stock_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				if (img_success.getVisibility() == View.VISIBLE) { // 如果结算成功可见，则清空一遍结算车列表
					clearCartList();
				}
				new Thread() {
					public void run() {
						String name = uiGridListMap.get(position).get("name")
								.toString().trim();
						Object price = uiGridListMap.get(position).get("price");
						Message msg = handler.obtainMessage();
						if (price != null) { // 点击商品
							if (price != null) { // 点击商品
								if (uiListMap.contains(uiGridListMap
										.get(position))) {
									msg.what = SAME_GOODS; // 已加入结算车
								} else {
									msg.what = CHOOSE_GOODS_CART;
									uiGridListMap.get(position).put("num", "1");
									msg.obj = uiGridListMap.get(position);
								}
							}
							msg.sendToTarget();

						} else { // 点击分类
							List<Map<String, Object>> categoryStockGoodsList = MainProvider
									.getStockGoodsListByCategory(
											MainActivity.this, name);
							msg.what = SHOW_MY_CATEGORY_GOODS_LIST;
							msg.obj = categoryStockGoodsList;
							msg.sendToTarget();
						}

					};
				}.start();
			}

		});

	}

	/**
	 * 绑定弹出对话框
	 */
	private void boundAlertDialog() {

		builder = new Builder(MainActivity.this);
		dialog = builder.create();
		contentView = View.inflate(MainActivity.this,
				R.layout.dialog_update_cart, null);
		// ---初始化控件---
		dialog_et_goods_code = (EditText) contentView
				.findViewById(R.id.dialog_et_goods_code);
		dialog_btn_click_search = (Button) contentView
				.findViewById(R.id.dialog_btn_click_search);
		dialog_tv_goods_name = (TextView) contentView
				.findViewById(R.id.dialog_tv_goods_name);
		dialog_et_num = (EditText) contentView.findViewById(R.id.dialog_et_num);
		dialog_et_price = (EditText) contentView
				.findViewById(R.id.dialog_et_price);
		dialog_btn_del = (Button) contentView.findViewById(R.id.dialog_btn_del);
		dialog_btn_cancel = (Button) contentView
				.findViewById(R.id.dialog_btn_cancel);
		dialog_btn_ok = (Button) contentView.findViewById(R.id.dialog_btn_ok);

		KeyBoardUtils.hideSoftInputMethod(MainActivity.this,
				dialog_et_goods_code); // 屏蔽软键盘
		KeyBoardUtils.hideSoftInputMethod(MainActivity.this, dialog_et_num); // 屏蔽软键盘
		KeyBoardUtils.hideSoftInputMethod(MainActivity.this, dialog_et_price); // 屏蔽软键盘

		// ------------- 数字按钮-自定义按钮键盘-------------------------
		dialog_btn_7 = (Button) contentView.findViewById(R.id.dialog_btn_7);
		dialog_btn_8 = (Button) contentView.findViewById(R.id.dialog_btn_8);
		dialog_btn_9 = (Button) contentView.findViewById(R.id.dialog_btn_9);
		dialog_btn_4 = (Button) contentView.findViewById(R.id.dialog_btn_4);
		dialog_btn_5 = (Button) contentView.findViewById(R.id.dialog_btn_5);
		dialog_btn_6 = (Button) contentView.findViewById(R.id.dialog_btn_6);
		dialog_btn_1 = (Button) contentView.findViewById(R.id.dialog_btn_1);
		dialog_btn_2 = (Button) contentView.findViewById(R.id.dialog_btn_2);
		dialog_btn_3 = (Button) contentView.findViewById(R.id.dialog_btn_3);
		dialog_btn_dian = (Button) contentView
				.findViewById(R.id.dialog_btn_dian);
		dialog_btn_0 = (Button) contentView.findViewById(R.id.dialog_btn_0);
		dialog_btn_tuige = (Button) contentView
				.findViewById(R.id.dialog_btn_tuige);

		// 数字按键
		boundNumKey(dialog_btn_7, "7");
		boundNumKey(dialog_btn_8, "8");
		boundNumKey(dialog_btn_9, "9");
		boundNumKey(dialog_btn_4, "4");
		boundNumKey(dialog_btn_5, "5");
		boundNumKey(dialog_btn_6, "6");
		boundNumKey(dialog_btn_1, "1");
		boundNumKey(dialog_btn_2, "2");
		boundNumKey(dialog_btn_3, "3");
		boundNumKey(dialog_btn_dian, ".");
		boundNumKey(dialog_btn_0, "0");
		// 退格
		dialog_btn_tuige.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (dialog_et_num.hasFocus()) {
					int cursor = dialog_et_num.getSelectionStart();
					if (cursor >= 1) {
						dialog_et_num.getText().delete(cursor - 1, cursor);
					}
				} else if (dialog_et_price.hasFocus()) {
					int cursor = dialog_et_price.getSelectionStart();
					if (cursor >= 1) {
						dialog_et_price.getText().delete(cursor - 1, cursor);
					}
				} else if (dialog_et_goods_code.hasFocus()) {
					int cursor = dialog_et_goods_code.getSelectionStart();
					if (cursor >= 1) {
						dialog_et_goods_code.getText().delete(cursor - 1,
								cursor);
					}
				}
			}
		});
		// ------------- 数字按钮-自定义按钮键盘 end-------------------------
		// 取消按钮事件
		dialog_btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 绑定左边结算列表点击事件
	 */
	private void boundListViewControl() {
		// 设置listview item点击事件
		lv_cart_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				// 隐藏掉查询按钮和查询框
				dialog_btn_del.setVisibility(view.VISIBLE);
				dialog_et_goods_code.setVisibility(view.INVISIBLE);
				dialog_btn_click_search.setVisibility(view.INVISIBLE);

				dialog_et_goods_code.setText("");
				// ---------------赋值---------------------
				String name = (String) uiListMap.get(position).get("name")
						.toString().trim();
				String num = (String) uiListMap.get(position).get("num")
						.toString().trim();
				String price = (String) uiListMap.get(position).get("price")
						.toString().trim();
				// T.showShort(MainActivity.this, "名称：" + goods_name + "价格："+
				// goods_price);

				dialog_tv_goods_name.setText(name);
				dialog_et_price.setText(price);
				dialog_et_price.clearFocus();
				// dialog_et_price.requestFocus();
				dialog_et_num.setText(num);
				dialog_et_num.requestFocus();
				dialog_et_num.clearFocus();

				dialog.setView(contentView);
				dialog.show();
				// -------------设置弹出框宽高-----------------
				WindowManager windowManager = getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				WindowManager.LayoutParams lp = dialog.getWindow()
						.getAttributes();
				lp.width = (int) (display.getWidth() * 0.8); // 设置宽度
				lp.height = (int) (display.getWidth() * 0.7); // 设置高度
				System.out.println("宽" + display.getWidth() * 0.8);
				System.out.println("高" + display.getHeight() * 0.8);
				dialog.getWindow().setAttributes(lp);

				// 删除按钮事件
				dialog_btn_del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						uiListMap.remove(position);
						adapter.notifyDataSetChanged();
						dialog.dismiss();
						ComCartListTotalMoney();
					}
				});

				// 确定按钮事件
				dialog_btn_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						String num = dialog_et_num.getText().toString().trim();
						String price = dialog_et_price.getText().toString()
								.trim();
						if (!TextUtils.isEmpty(num)
								&& !TextUtils.isEmpty(price)) {
							// ----------------------修改指定map----------------------------------
							uiListMap.get(position).put("num", num); // 修改数量
							uiListMap.get(position).put("price", price); // 修改价格

							System.out.println("修改后uiListMap===" + uiListMap);
							// ----------------------------------------------------------------

							adapter.notifyDataSetChanged();
							dialog.dismiss();
							ComCartListTotalMoney();
						} else {
							T.showShort(MainActivity.this, "数量或价格不能为空!");
						}
					}
				});

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
				if (dialog_et_num.hasFocus()) {
					int cursor = dialog_et_num.getSelectionStart();
					dialog_et_num.getText().insert(cursor, num);
				} else if (dialog_et_price.hasFocus()) {
					int cursor = dialog_et_price.getSelectionStart();
					dialog_et_price.getText().insert(cursor, num);
				} else if (dialog_et_goods_code.hasFocus()) {
					int cursor = dialog_et_goods_code.getSelectionStart();
					dialog_et_goods_code.getText().insert(cursor, num);
				}
			}
		});
	}

	/**
	 * 加载数据
	 */
	private void loadCartData() {
		new Thread() {
			public void run() {
				Message msg = handler.obtainMessage();
				List<Map<String, Object>> runListMap = MainProvider
						.selectAllGoods(MainActivity.this, 0, 200);
				if (runListMap != null) {
					msg.what = SELECT_SUCCESS;
					msg.obj = runListMap;
				} else {
					msg.what = NET_ERROR;
				}
				msg.sendToTarget();
			};
		}.start();
	}

	// 数据适配器
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return uiListMap.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;

			if (convertView == null) {// 等于空，说明第一次显示，则新创建
				L.i("新建getView:" + position);
				// 把一个布局文件转化成 view对象。
				view = View
						.inflate(MainActivity.this, R.layout.list_cart, null);
				// 2.减少子孩子查询的次数 内存中对象的地址。
				holder = new ViewHolder();
				holder.tv_goods_name = (TextView) view
						.findViewById(R.id.tv_goods_name);
				holder.tv_goods_num = (TextView) view
						.findViewById(R.id.tv_goods_num);
				holder.tv_goods_money = (TextView) view
						.findViewById(R.id.tv_goods_money);
				// 当孩子生出来的时候找到他们的引用，存放在记事本，放在父亲的口袋
				view.setTag(holder);

			} else { // 判断缓存对象是否为null,不为null时已经缓存了对象
				L.i("复用getView:" + position);
				view = convertView;
				holder = (ViewHolder) view.getTag();// 5%
			}

			String num = uiListMap.get(position).get("num").toString().trim();
			String price = uiListMap.get(position).get("price").toString()
					.trim();

			holder.tv_goods_name.setText(uiListMap.get(position).get("name")
					.toString().trim());
			holder.tv_goods_num.setText("×" + num);

			// ---------------计算单金额---------------------
			float new_num = Float.valueOf(num);
			float new_price = Float.valueOf(price);

			float money = new_num * new_price;
			String new_money = String.format("%.1f", money);

			holder.tv_goods_money.setText("￥" + new_money);

			// System.out.println("List 数据发生了 变化？？？？？？");

			return view;
		}

	}

	/**
	 * view对象的容器 记录孩子的内存地址。 相当于一个记事本
	 */
	static class ViewHolder {
		TextView tv_goods_name;
		TextView tv_goods_num;
		TextView tv_goods_money;
	}

	/**
	 * 计算 结算车 金额 并显示
	 */
	private void ComCartListTotalMoney() {
		float totalMoney = 0;
		// 遍历清单车
		for (int i = 0; i < uiListMap.size(); i++) {
			String num = uiListMap.get(i).get("num").toString().trim();
			String price = uiListMap.get(i).get("price").toString().trim();
			float new_num = Float.valueOf(num);
			float new_price = Float.valueOf(price);

			float money = new_num * new_price;
			String new_money = String.format("%.1f", money);

			totalMoney += Float.valueOf(new_money);
		}
		clearing_total_money = String.format("%.1f", totalMoney);
		tv_total_money.setText("总计：￥" + clearing_total_money);

	}

	/**
	 * 重写onclick 方法
	 * 
	 * @param arg0
	 */
	@Override
	public void onClick(View view) {

		Intent intent;
		switch (view.getId()) {
		case R.id.btn_clear_cart: // 清空 结算车
			clearCartList();
			break;
		case R.id.btn_setting: // 外部设置
			intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_clearing: // 结算
			if (img_success.getVisibility() == View.VISIBLE) { // 如果结算成功可见，则清空购物车
				clearCartList();
				T.showShort(MainActivity.this, "没有选择商品!");
			} else {

				if (uiListMap.size() > 0) {
					intent = new Intent(MainActivity.this,
							ClearingActivity.class);
					Bundle bundle = new Bundle();
					// bundle.putString("name", "测试商品");
					// bundle.putDouble("price", 200);
					bundle.putString("clearingTotalMoney", clearing_total_money);
					// bundle.putCharSequenceArrayList("d", uiListMap);

					intent.putExtras(bundle);
					// startActivity(intent);
					startActivityForResult(intent, 0);
				} else {
					T.showShort(MainActivity.this, "没有选择商品!");
				}
			}
			break;
		case R.id.btn_search: // 点击手动查询

			// 隐藏删除
			dialog_btn_del.setVisibility(view.INVISIBLE);
			dialog_et_goods_code.setVisibility(view.VISIBLE);
			dialog_btn_click_search.setVisibility(view.VISIBLE);

			dialog_et_goods_code.setText("");
			dialog_tv_goods_name.setText("");
			dialog_et_price.setText("");
			dialog_et_num.setText("");

			dialog.setView(contentView);
			dialog.show();
			// -------------设置弹出框宽高-----------------
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.width = (int) (display.getWidth() * 0.8); // 设置宽度
			lp.height = (int) (display.getWidth() * 0.7); // 设置高度
			System.out.println("宽" + display.getWidth() * 0.8);
			System.out.println("高" + display.getHeight() * 0.8);
			dialog.getWindow().setAttributes(lp);

			// 搜索查询
			dialog_btn_click_search.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (TextUtils.isEmpty(dialog_et_goods_code.getText()
							.toString().trim())) {
						T.showShort(MainActivity.this, "商品编码为空！");
						return;
					}
					new Thread() {
						public void run() {
							Message msg = handler.obtainMessage();
							Map<String, Object> runMap = MainProvider
									.selectGoodsByCode(MainActivity.this,
											dialog_et_goods_code.getText()
													.toString().trim());
							if (runMap.size() > 0) {
								msg.what = DIALOG_SEARCH_SUCCESS;
								msg.obj = runMap;
							} else {
								msg.what = DIALOG_SEARCH_ERROR;
							}
							msg.sendToTarget();
						};
					}.start();
				}
			});

			// 确定按钮
			dialog_btn_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (dialogMap==null || dialogMap.size()==0) {
						T.showShort(MainActivity.this, "不能添加空物品!");
						return;
					}
					if (uiListMap.contains(dialogMap)) {
						T.showShort(MainActivity.this, "该物品已添加！");
						return;
					}
					dialogMap.put("num", dialog_et_num.getText().toString().trim());
					dialogMap.put("price", dialog_et_price.getText().toString().trim());
					uiListMap.add(dialogMap);
					if (adapter == null) {
						adapter = new MyAdapter();
						lv_cart_list.setAdapter(adapter);
					} else {
						adapter.notifyDataSetChanged();
					}
					ComCartListTotalMoney();
					dialogMap = null;
					dialog.dismiss();
				}
			});
			break;
		default:
			break;
		}

	}

	/**
	 * 清空结算购物车
	 */
	private void clearCartList() {
		uiListMap.clear();
		if (adapter == null) {
			adapter = new MyAdapter();
			lv_cart_list.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		ComCartListTotalMoney();
		img_success.setVisibility(View.INVISIBLE); // 设置结算成功，可见
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}

		boolean status = data.getBooleanExtra("status", false);
		if (status) {
			img_success.setVisibility(View.VISIBLE); // 设置结算成功，可见
		} else {
			T.showLong(MainActivity.this, "结算失败！");
		}

	}

	/**
	 * 退出软件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("退出软件").setMessage("是否退出软件?")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/* 调用finish()方法,退出程序 */
						uiListMap.clear(); // 清除结算车
						MainActivity.this.finish();
						System.exit(0);// 正常退出App
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/* 若选择否则不需要填写代码 */
					}
				}).show(); /* 记得调用show()方法,否则显示不出来Dialog */
		return super.onKeyDown(keyCode, event);
	}

	// ui线程
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SELECT_SUCCESS:
				uiListMap = (List<Map<String, Object>>) msg.obj;
				if (adapter == null) {
					adapter = new MyAdapter();
					lv_cart_list.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				ComCartListTotalMoney();
				break;
			case NET_ERROR:
				T.showShort(MainActivity.this, "网络错误!");
				break;
			case SHOW_MY_CATEGORY:
				uiGridListMap = (List<Map<String, Object>>) msg.obj;
				tv_btn_sub_fenlei.setText(""); // 设置显示目前的子分类
				gridAdapter = new MainGridAdapter(MainActivity.this,
						uiGridListMap);
				gv_stock_list.setAdapter(gridAdapter);
				break;
			case SHOW_MY_CATEGORY_GOODS_LIST:
				uiGridListMap = (List<Map<String, Object>>) msg.obj;
				String name = uiGridListMap.get(0).get("category").toString()
						.trim();
				tv_btn_sub_fenlei.setText(name); // 设置显示目前的子分类
				System.out.println(uiGridListMap);
				gridAdapter = new MainGridAdapter(MainActivity.this,
						uiGridListMap);
				gv_stock_list.setAdapter(gridAdapter);
				// gridAdapter.notifyDataSetChanged();
				break;
			case CHOOSE_GOODS_CART:
				et_goods_code.setText("");
				// T.showShort(MainActivity.this, "点击我的业务逻辑还没写呢...");
				// if (img_success.getVisibility() == View.VISIBLE) { //
				// 如果结算成功可见，则清空一遍结算车列表
				// clearCartList();
				// }
				uiListMap.add((Map<String, Object>) msg.obj);
				if (adapter == null) {
					adapter = new MyAdapter();
					lv_cart_list.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				ComCartListTotalMoney();
				break;
			case SAME_GOODS:
				et_goods_code.setText("");
				T.showShort(MainActivity.this, "该商品已添加！");
				break;
			case NO_FIND_GOODS:
				et_goods_code.setText("");
				T.showShort(MainActivity.this, "系统没有找到该商品的信息!!！");
				break;
			case DIALOG_SEARCH_SUCCESS: // 对话框搜索成功
				dialogMap = (Map<String, Object>) msg.obj;
				dialog_tv_goods_name.setText(dialogMap.get("name").toString());
				dialog_et_price.setText(dialogMap.get("price").toString());
				dialog_et_num.setText("1");
				break;
			case DIALOG_SEARCH_ERROR: // 对话框搜索失败
				dialogMap = null;
				dialog_tv_goods_name.setText("");
				dialog_et_price.setText("");
				dialog_et_num.setText("");
				T.showShort(MainActivity.this, "系统没有找到该商品的信息!!！");
				break;
			default:
				break;
			}
		};
	};

}
