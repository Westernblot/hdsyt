package com.grid.hdsyt.ui.syncdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grid.hdsyt.db.SqliteHelper;
import com.grid.hdsyt.function.FinalValues;
import com.grid.hdsyt.utils.HttpUtils;
import com.grid.hdsyt.utils.SPUtils;

public class SyncdataProvider {

	/**
	 * 同步tb_user表,只获取本部门的数据
	 */
	public static void syncTbUser(Context context) {

		String param = getDbParam(context);
		String userArrString = HttpUtils.doPost(FinalValues.GET_DEPT_TB_USER,
				param);
		try {
			JSONArray jsonArray = new JSONArray(userArrString);

			SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
					null, SQLiteDatabase.OPEN_READWRITE);
			if (db.isOpen()) { // 打开本地sqlite数据库

				try {
					db.beginTransaction();

					db.execSQL("delete from tb_user");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						// 执行操作
						db.execSQL(
								"insert into tb_user(id,pid,username,password,power,cnname,dept,role,token,addtime,db_prefix,db_connection)"
										+ " values"
										+ "(?,?,?,?,?,?,?,?,?,?,?,?);",
								new Object[] { jsonObject.getString("id"),
										jsonObject.getString("pid"),
										jsonObject.getString("username"),
										jsonObject.getString("password"),
										jsonObject.getString("power"),
										jsonObject.getString("cnname"),
										jsonObject.getString("dept"),
										jsonObject.getString("role"),
										jsonObject.getString("token"),
										jsonObject.getString("addtime"),
										jsonObject.getString("db_prefix"),
										jsonObject.getString("db_connection") });
					}
					db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("================同步tb_user表 ok===================");

	}

	/**
	 * 同步tb_stock 库存表
	 */
	public static void syncTbStock(Context context) {

		String param = getDbParam(context);
		String userArrString = HttpUtils
				.doPost(FinalValues.GET_TB_STOCK, param);
		try {
			JSONArray jsonArray = new JSONArray(userArrString);

			SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
					null, SQLiteDatabase.OPEN_READWRITE);
			if (db.isOpen()) { // 打开本地sqlite数据库

				try {
					db.beginTransaction();

					db.execSQL("delete from tb_stock");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						// 执行操作
						db.execSQL(
								"insert into tb_stock(id,name,code,pym,description,num,lownum,addid,addname,addtime,price,dept,category)"
										+ " values"
										+ "(?,?,?,?,?,?,?,?,?,?,?,?,?);",
								new Object[] { jsonObject.getString("id"),
										jsonObject.getString("name"),
										jsonObject.getString("code"),
										jsonObject.getString("pym"),
										jsonObject.getString("description"),
										jsonObject.getString("num"),
										jsonObject.getString("lownum"),
										jsonObject.getString("addid"),
										jsonObject.getString("addname"),
										jsonObject.getString("addtime"),
										jsonObject.getString("price"),
										jsonObject.getString("dept"),
										jsonObject.getString("category") });
					}
					db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out
				.println("================同步tb_stock 表 ok===================");

	}

	/**
	 * 同步tb_member 表
	 */
	public static void syncTbMember(Context context) {

		String param = getDbParam(context);
		String userArrString = HttpUtils.doPost(FinalValues.GET_TB_MEMBER,
				param);
		try {
			JSONArray jsonArray = new JSONArray(userArrString);

			SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
					null, SQLiteDatabase.OPEN_READWRITE);
			if (db.isOpen()) { // 打开本地sqlite数据库

				try {
					db.beginTransaction();

					db.execSQL("delete from tb_member");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						// 执行操作
						db.execSQL(
								"insert into tb_member(id,pid,name,pym,sex,idcard,phone,address,account,score,gift,cost,addid,addname,addtime,dept)"
										+ " values"
										+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
								new Object[] { jsonObject.getString("id"),
										jsonObject.getString("pid"),
										jsonObject.getString("name"),
										jsonObject.getString("pym"),
										jsonObject.getString("sex"),
										jsonObject.getString("idcard"),
										jsonObject.getString("phone"),
										jsonObject.getString("address"),
										jsonObject.getString("account"),
										jsonObject.getString("score"),
										jsonObject.getString("gift"),
										jsonObject.getString("cost"),
										jsonObject.getString("addid"),
										jsonObject.getString("addname"),
										jsonObject.getString("addtime"),
										jsonObject.getString("dept") });
					}
					db.setTransactionSuccessful();

				} finally {
					db.endTransaction();
					db.close();
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out
				.println("================同步tb_member 表 ok===================");

	}

	/**
	 * 同步tb_detail 表:上传本地sqlite数据到云端，并清空自己
	 */
	public static void postTbDetail(Context context) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
				null, SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from tb_detail;", null);

			if (cursor != null && cursor.getCount() > 0) {

				List<Map> list = new ArrayList<Map>();

				while (cursor.moveToNext()) {
					Map<String, String> mp = new HashMap<String, String>();

					mp.put("id", cursor.getString(cursor.getColumnIndex("id")));
					mp.put("orderno",
							cursor.getString(cursor.getColumnIndex("orderno")));
					mp.put("addid",
							cursor.getString(cursor.getColumnIndex("addid")));
					mp.put("addname",
							cursor.getString(cursor.getColumnIndex("addname")));
					mp.put("addtime",
							cursor.getString(cursor.getColumnIndex("addtime")));
					mp.put("dept",
							cursor.getString(cursor.getColumnIndex("dept")));
					mp.put("outdept",
							cursor.getString(cursor.getColumnIndex("outdept")));
					mp.put("stockcode", cursor.getString(cursor
							.getColumnIndex("stockcode")));
					mp.put("stockname", cursor.getString(cursor
							.getColumnIndex("stockname")));
					mp.put("num",
							cursor.getString(cursor.getColumnIndex("num")));
					mp.put("outprice",
							cursor.getString(cursor.getColumnIndex("outprice")));
					mp.put("money",
							cursor.getString(cursor.getColumnIndex("money")));
					mp.put("membername", cursor.getString(cursor
							.getColumnIndex("membername")));
					mp.put("memberphone", cursor.getString(cursor
							.getColumnIndex("memberphone")));
					mp.put("totalmoney", cursor.getString(cursor
							.getColumnIndex("totalmoney")));
					mp.put("discount",
							cursor.getString(cursor.getColumnIndex("discount")));
					mp.put("discountprice", cursor.getString(cursor
							.getColumnIndex("discountprice")));
					mp.put("bid",
							cursor.getString(cursor.getColumnIndex("bid")));
					mp.put("type",
							cursor.getString(cursor.getColumnIndex("type")));

					list.add(mp);
				}
				// db.close();

				JSONArray jsonArray = new JSONArray(list);
				System.out.println("jsonArray.toString()==="
						+ jsonArray.toString());

				String params = getDbParam(context) + "&jsondata="
						+ jsonArray.toString();
				System.out.println("params----------" + params);
				try {
					HttpUtils.doPost(FinalValues.SYNC_TB_DETAIL, params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("--------------出异常了么？-----------------");
					e.printStackTrace();
				}

			}

			db.execSQL("delete from tb_detail");
			db.close();
		}

		System.out
				.println("================上传 tb_detail 数据 OK？？？===================");

	}

	// =============================================远程接口通用数据参数================================

	/**
	 * 获取必要的信息头
	 */
	private static String getDbParam(Context context) {
		String userString = (String) SPUtils.get(context, "userString", "");
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(userString);
			String dept = jsonObject.getString("dept");
			String db_prefix = jsonObject.getString("db_prefix");
			String db_connection = jsonObject.getString("db_connection");
			String param = "db_prefix=" + db_prefix + "&db_connection="
					+ db_connection + "&dept=" + dept;
			return param;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// ==================================================================================================

}
