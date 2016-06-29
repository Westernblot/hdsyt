package com.grid.hdsyt.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.grid.hdsyt.utils.L;
import com.grid.hdsyt.utils.Md5Utils;

/**
 * sqlite 数据库
 * 
 * @author PC
 * 
 */
public class SqliteHelper extends AndroidTestCase {

	public static String path = "data/data/com.grid.hdsyt/files/syt.db";

	/**
	 * 写入数据
	 * 
	 * @param person
	 */
	public void insert() {
		L.i("开始写入");
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) { // 打开数据库

			// 执行操作
			db.execSQL(
					"insert into tb_user(id,username, password) values(?,?,?);",
					new Object[] { new Date().getTime(), "1", Md5Utils.md5("1") });

			db.close(); // 关闭数据库
		}
	}
	
	
	/**
	 * 清空tb_user 表
	 */
	public void deleteTbUser() {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) { // 打开数据库

			db.execSQL("delete from tb_user");

			db.close();
		}
	}

	
	/**
	 * 清空tb_user 表
	 */
	public void deleteTbDetail() {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) { // 打开数据库

			db.execSQL("delete from tb_detail");

			db.close();
		}
	}
	
	
	/**
	 * 删除数据
	 * 
	 * @param id
	 */
	public void delete(int id) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) { // 打开数据库

			db.execSQL("delete from person where _id = ?;",
					new Integer[] { id });

			db.close();
		}
	}

	/**
	 * 修改数据
	 * 
	 * @param id
	 * @param name
	 */
	public void update(int id, String name) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) {

			db.execSQL("update person set name = ? where _id = ?;",
					new Object[] { name, id });

			db.close();
		}
	}

	/**
	 * 查找tb_user表
	 */
	public void queryAll() {
		L.i("开始查询tb_user");
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) {
			// Cursor cursor = db.rawQuery(
			// "select id, username, cnname from tb_user;", null);
			Cursor cursor = db.rawQuery("select * from tb_user;", null);

			if (cursor != null && cursor.getCount() > 0) {

				List<Map> list = new ArrayList<Map>();
//				String id;
//				String username;
//				String cnname;
				while (cursor.moveToNext()) {
//					id = cursor.getString(0); // id
//					username = cursor.getString(1); // 用户名
//					cnname = cursor.getString(2); // 中文名
					Map<String, String> mp = new HashMap<String, String>();
					// mp.put("id", id);
					// mp.put("username", username);
					// mp.put("cnname", cnname);

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

					list.add(mp);
				}
				db.close();
				L.i("kkkkkkkkkk=" + list.toString());
				System.out.println("list.toString()=="+list.toString());
			}
			db.close();
		}

	}

	
	/**
	 * 查找tb_detail表
	 */
	public void queryTbDetail() {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from tb_detail;", null);

			if (cursor != null && cursor.getCount() > 0) {

				List<Map> list = new ArrayList<Map>();

				while (cursor.moveToNext()) {
					Map<String, String> mp = new HashMap<String, String>();
				
					mp.put("id",cursor.getString(cursor.getColumnIndex("id")));
					mp.put("orderno",cursor.getString(cursor.getColumnIndex("orderno")));
					mp.put("addid",cursor.getString(cursor.getColumnIndex("addid")));
					mp.put("addname",cursor.getString(cursor.getColumnIndex("addname")));
					mp.put("addtime",cursor.getString(cursor.getColumnIndex("addtime")));
					mp.put("dept",cursor.getString(cursor.getColumnIndex("dept")));
					mp.put("outdept",cursor.getString(cursor.getColumnIndex("outdept")));
					mp.put("stockcode",cursor.getString(cursor.getColumnIndex("stockcode")));
					mp.put("stockname",cursor.getString(cursor.getColumnIndex("stockname")));
					mp.put("num",cursor.getString(cursor.getColumnIndex("num")));
					mp.put("outprice",cursor.getString(cursor.getColumnIndex("outprice")));
					mp.put("money",cursor.getString(cursor.getColumnIndex("money")));
					mp.put("membername",cursor.getString(cursor.getColumnIndex("membername")));
					mp.put("memberphone",cursor.getString(cursor.getColumnIndex("memberphone")));
					mp.put("totalmoney",cursor.getString(cursor.getColumnIndex("totalmoney")));
					mp.put("discount",cursor.getString(cursor.getColumnIndex("discount")));
					mp.put("discountprice",cursor.getString(cursor.getColumnIndex("discountprice")));
					mp.put("bid",cursor.getString(cursor.getColumnIndex("bid")));
					mp.put("type",cursor.getString(cursor.getColumnIndex("type")));

					list.add(mp);
				}
				db.close();
				System.out.println("list.toString()=="+list.toString());
				
				JSONArray jsonArray = new JSONArray(list);
				System.out.println("jsonArray.toString()==="+jsonArray.toString());
			}
			db.close();
		}

	}
	
	
}
