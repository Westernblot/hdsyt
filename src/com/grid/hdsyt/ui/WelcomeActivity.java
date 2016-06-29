package com.grid.hdsyt.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.grid.hdsyt.R;
import com.grid.hdsyt.ui.login.LoginActivity;
import com.grid.hdsyt.utils.AppUtils;
import com.grid.hdsyt.utils.HttpUtils;
import com.grid.hdsyt.utils.SPUtils;
import com.grid.hdsyt.utils.T;
import com.grid.hdsyt.utils.FileUtils;



public class WelcomeActivity extends Activity {

	// ui状态
	protected static final int NEED_UPDATE = 0;
	protected static final int NOT_UPDATE = 1;
	protected static final int NET_ERROR = 2;

	// 检查升级
	private static final String SERVER_INFO_URL = "http://192.168.1.202/updateInfo.json";
	private Map<String, Object> uiMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		// 获取是否升级标志
		boolean isUpdate = (Boolean) SPUtils.get(this, "isUpdate", false);

		//拷贝本地数据库到 data 目录下
		FileUtils.copyDB(this);
		
		// 检查是否需要升级
		if (isUpdate) {
			checkUpdate();
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					enterHome();
				}
			}, 3000);
		}

	}

	// ui线程
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NEED_UPDATE:
				uiMap = (Map<String, Object>) msg.obj;
				showUpdateDialog();
				break;
			case NOT_UPDATE:
				enterHome();
				break;
			case NET_ERROR:
				T.showShort(WelcomeActivity.this, "网络错误!");
				enterHome();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 进入主页
	 */
	private void enterHome() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	};

	/**
	 * 显示升级对话框
	 */
	protected void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(WelcomeActivity.this);
		builder.setTitle("提示升级");
		builder.setMessage((String) uiMap.get("description"));
		builder.setPositiveButton("立即升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 下载apk检查
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// sd卡空间充足, 下载apk
					aFinalDown();

				} else {
					Toast.makeText(WelcomeActivity.this, "sd卡空间不足！", 0).show();
					return;
				}
			}
		});

		builder.setNegativeButton("下次再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				enterHome();// 进入主页面
			}
		});
		builder.show();
	}

	// ======================================分隔线=============================================

	/**
	 * 检查升级
	 */
	private void checkUpdate() {
		new Thread() {
			public void run() {
				long startTime = System.currentTimeMillis();
				Map<String, Object> runMap = getServerInfo();
				Message msg = handler.obtainMessage();
				if (runMap != null) {
					String version = (String) runMap.get("version");
					String systemVersion = AppUtils
							.getVersionName(WelcomeActivity.this);
					if (!version.equals(systemVersion)) {
						msg.what = NEED_UPDATE;
						msg.obj = runMap;
					} else {
						msg.what = NOT_UPDATE;
					}
				} else {
					msg.what = NET_ERROR;
				}
				long endTime = System.currentTimeMillis();
				// 计算检查版本之后，剩余的时间
				long dTime = endTime - startTime;
				// 3 秒停顿
				if (dTime < 3000) {
					try {
						Thread.sleep(3000 - dTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				msg.sendToTarget();
			};
		}.start();
	}

	/**
	 * 去远程服务器获取数据
	 * 
	 * @return
	 */
	private Map<String, Object> getServerInfo() {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String resStr = HttpUtils.doGet(SERVER_INFO_URL);
		try {
			JSONObject jsonObject = new JSONObject(resStr);
			resMap.put("version", jsonObject.get("version"));
			resMap.put("description", jsonObject.get("description"));
			resMap.put("apkUrl", jsonObject.get("apkUrl"));
			return resMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// ======================================分隔线=============================================

	private ProgressDialog pBar;

	/**
	 * 下载APK
	 */
	private void aFinalDown() {
		pBar = new ProgressDialog(WelcomeActivity.this); // 进度条，在下载的时候实时更新进度，提高用户友好度
		pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pBar.setTitle("正在下载");
		pBar.setMessage("请稍候...");
		pBar.setProgress(0);
		pBar.show();
		// aFinal 第三方 android 框架
		FinalHttp finalHttp = new FinalHttp();
		String apkUrl = (String) uiMap.get("apkUrl");// 获取下载apk地址
		finalHttp.download(
				apkUrl,
				Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/wgsyt"
						+ AppUtils.getVersionName(WelcomeActivity.this)
						+ ".apk", new AjaxCallBack<File>() {

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						t.printStackTrace();
						Toast.makeText(WelcomeActivity.this, "下载失败", 1).show();
						super.onFailure(t, errorNo, strMsg);
					}

					@Override
					public void onLoading(long count, long current) {
						// TODO Auto-generated method stub
						super.onLoading(count, current);
						// 当前下载进度百分比
						int progress = (int) current;
						pBar.setProgress(progress); // 这里就是关键的实时更新进度了！
					}

					@Override
					public void onSuccess(final File t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						handler.post(new Runnable() {
							public void run() {
								pBar.cancel();
								installApk(t);
							}
						});
					}

				});

	}

	/**
	 * 安装APK
	 * 
	 * @param t
	 */
	private void installApk(File t) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 安装完成后，显示 完成/打开的提示页
		intent.setDataAndType(Uri.fromFile(t),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

}
