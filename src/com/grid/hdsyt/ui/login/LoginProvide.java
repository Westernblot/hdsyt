package com.grid.hdsyt.ui.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grid.hdsyt.db.SqliteHelper;
import com.grid.hdsyt.function.FinalValues;
import com.grid.hdsyt.utils.HttpUtils;
import com.grid.hdsyt.utils.Md5Utils;

public class LoginProvide {

	/**
	 * 验证用户登陆
	 */
	public static Map<String, Object> checkUserLogin(String username,
			String password) {
		Map<String, Object> mp = new HashMap<String, Object>();
		String param = "username=" + username + "&password="
				+ Md5Utils.md5(password);
		String result = HttpUtils
				.doPost(FinalValues.CHECK_LOGIN_ADDRESS, param);
		try {
			JSONObject jsonObject = new JSONObject(result);
			
			mp.put("id",jsonObject.getString("id"));
			mp.put("pid",jsonObject.getString("pid"));
			mp.put("username",jsonObject.getString("username"));
			mp.put("password",jsonObject.getString("password"));
			mp.put("power",jsonObject.getString("power"));
			mp.put("cnname",jsonObject.getString("cnname"));
			mp.put("dept",jsonObject.getString("dept"));
			mp.put("role",jsonObject.getString("role"));
			mp.put("token",jsonObject.getString("token"));
			mp.put("addtime",jsonObject.getString("addtime"));
			mp.put("db_prefix",jsonObject.getString("db_prefix"));
			mp.put("db_connection",jsonObject.getString("db_connection"));
			
			mp.put("network", true);   //远程取的数据

			
//			boolean status = obj.getBoolean("status");
//			mp.put("status", status);
//			if (status) {
//				String userString = obj.getString("user");
//				mp.put("user", userString);
//				System.out.println("userString=" + userString);
//			}
			return mp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out
					.println("==================出异常了,去本地数据库验证===================");

			return findUserInSQLite(username, password);

		}
		// return null;
	}

	// ============================本地数据库读写=====================================

	/**
	 * 去本地数据库验证
	 * 
	 * @return
	 */
	private static Map<String, Object> findUserInSQLite(String username,
			String password) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
				null, SQLiteDatabase.OPEN_READWRITE);
		
		if (db.isOpen()) {
			Cursor cursor = db
					.rawQuery(
							"select * from tb_user where username=? and password = ? limit 1",
							new String[] { username, Md5Utils.md5(password) });

			if (cursor != null && cursor.getCount() > 0) {
				Map<String, Object> mp = new HashMap<String, Object>();
				while (cursor.moveToNext()) {
			
					mp.put("id",cursor.getString(cursor.getColumnIndex("id")));
					mp.put("pid",cursor.getString(cursor.getColumnIndex("pid")));
					mp.put("username",cursor.getString(cursor.getColumnIndex("username")));
					mp.put("password",cursor.getString(cursor.getColumnIndex("password")));
					mp.put("power",cursor.getString(cursor.getColumnIndex("power")));
					mp.put("cnname",cursor.getString(cursor.getColumnIndex("cnname")));
					mp.put("dept",cursor.getString(cursor.getColumnIndex("dept")));
					mp.put("role",cursor.getString(cursor.getColumnIndex("role")));
					mp.put("token",cursor.getString(cursor.getColumnIndex("token")));
					mp.put("addtime",cursor.getString(cursor.getColumnIndex("addtime")));
					mp.put("db_prefix",cursor.getString(cursor.getColumnIndex("db_prefix")));
					mp.put("db_connection",cursor.getString(cursor.getColumnIndex("db_connection")));
					
					mp.put("network", false);   //本地取的数据

				}
				db.close();
				return mp;
			}
			db.close();
		}
		return null;
	}

}
