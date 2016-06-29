package com.grid.hdsyt.ui.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.grid.hdsyt.R;
import com.grid.hdsyt.print.Const;
import com.grid.hdsyt.print.NetPrinter;
import com.grid.hdsyt.ui.main.MainActivity;
import com.grid.hdsyt.ui.syncdata.SyncdataActivity;
import com.grid.hdsyt.utils.SPUtils;
import com.grid.hdsyt.utils.T;

/**
 * 文件名：LoginActivity
 * 
 * 创建人： 王凯
 * 
 * 时期：2015-12-01 16：40：00
 * 
 * 描述：用户登陆 验证
 * 
 * 版权所有 ： 湖北网格软件开发有限公司
 * 
 * 版本号：1.0
 *
 */
public class LoginActivity extends Activity implements OnClickListener {

	protected static final int CHECK_SUCCESS = 0;

	protected static final int CHECK_SUCCESS_NET = 9;
	protected static final int CHECK_SUCCESS_LOCAL = 8;
	protected static final int CHECK_ERROR = 1;
	protected static final int NET_ERROR = 2;

	private EditText et_username;
	private EditText et_password;
	private TextView tv_btn_login;

	private Map<String, Object> uiMap = new HashMap<String, Object>();// 验证登陆结果集
	private String param; // 提交参数

	// 打印测试
	private NetPrinter printer;
	private Button btn_print;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// ===================初始化组件========================
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		tv_btn_login = (TextView) findViewById(R.id.tv_btn_login);
		btn_print = (Button) findViewById(R.id.btn_print);
		tv_btn_login.setOnClickListener(this);
		btn_print.setOnClickListener(this);
		// ===================end========================
	}

	// ui线程
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			hideProgressDialog();
			switch (msg.what) {
			case CHECK_SUCCESS_NET:
				JSONObject jsonObjectNet = new JSONObject(uiMap);// map 转json
				String userStringNet = jsonObjectNet.toString(); // json 转
																	// string
				SPUtils.put(getApplicationContext(), "userString",
						userStringNet); // 存数据到sp
				enterSyncActivity();
				break;
			case CHECK_SUCCESS_LOCAL:
				JSONObject jsonObjectLocal = new JSONObject(uiMap);// map 转json
				String userStringLocal = jsonObjectLocal.toString(); // json 转
																		// string
				SPUtils.put(getApplicationContext(), "userString",
						userStringLocal); // 存数据到sp
				enterMainActivity();
				break;
			case CHECK_ERROR:
				T.showShort(LoginActivity.this, "用户名或密码错误!");
				break;
			case NET_ERROR:
				T.showShort(LoginActivity.this, "网络错误!");
				break;
			default:
				break;
			}
		}
	};

	// 重写onclick方法
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_btn_login:
			showProgressDialog();
			new Thread() {
				public void run() {
					Message msg = handler.obtainMessage();
					uiMap = LoginProvide.checkUserLogin(et_username.getText()
							.toString().trim(), et_password.getText()
							.toString().trim());
					if (uiMap != null) {
						msg.obj = uiMap;
						if ((Boolean) uiMap.get("network")) {
							msg.what = CHECK_SUCCESS_NET; // 网络验证
						} else {
							msg.what = CHECK_SUCCESS_LOCAL; // 本地验证
						}
					} else {
						msg.what = CHECK_ERROR;
					}
					msg.sendToTarget();
				};
			}.start();
			break;
		case R.id.btn_print:

			new Thread() {
				public void run() {

					printer = new NetPrinter();

					printer.Open(Const.print_IP,
							Integer.parseInt(Const.print_Port));

					String[] aaray = { "张三", "LISI", "WANGWGWA" };
					printer.PrintText("titele xxxxx", 0, 0, 0);
					printer.PrintEnter();
					for (String s : aaray) {
						printer.PrintText(s, 0, 0, 0);
					}
					printer.PrintEnter();
					printer.PrintText("9999.00", 0, 0, 0);
					printer.PrintEnter();
					printer.PrintEnter();
					printer.PrintText("2015-12-29 16:24:00", 0, 0, 0);
					printer.PrintEnter();
					printer.PrintText("system_grid", 1, 1, 0);
					printer.PrintNewLines(5);
					printer.CutPage(0);
					printer.Close();

					System.out.println("=========打印OK？？？===========");
				};
			}.start();

			break;
		default:
			break;
		}

	}

	/**
	 * 进入同步页面
	 */
	private void enterSyncActivity() {
		Intent intent = new Intent(LoginActivity.this, SyncdataActivity.class);
		startActivity(intent);
		finish();
	};

	/**
	 * 进入主页面
	 */
	private void enterMainActivity() {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	};

	// ===================================分隔线==================================

	private ProgressDialog progressDialog;

	/**
	 * 提示加载
	 */
	public void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(LoginActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage("正在登陆......");
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
