package com.grid.hdsyt.ui.clearing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grid.hdsyt.db.SqliteHelper;
import com.grid.hdsyt.function.FinalValues;
import com.grid.hdsyt.function.SpUserHelper;
import com.grid.hdsyt.utils.HttpUtils;
import com.grid.hdsyt.utils.Md5Utils;

public class ClearingProvide {

	/**
	 * 查找会员
	 */
	// public static Map<String, Object> findMemberByPhone(Context context,
	// String phone) {
	//
	// String dbStr = SpUserHelper.getUserDbStr(context);
	//
	// Map<String, Object> mp = new HashMap<String, Object>();
	// String param = "phone=" + phone + "&" + dbStr;
	// String resStr = HttpUtils.doPost(FinalValues.SEARCH_MEMBER_BY_PHONE,
	// param);
	// try {
	// JSONObject jsonObject = new JSONObject(resStr);
	// mp.put("name", jsonObject.getString("name"));
	// mp.put("phone", jsonObject.getString("phone"));
	// mp.put("score", jsonObject.getString("score"));
	// return mp;
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }

	/**
	 * 添加会员
	 */
	// public static boolean addMember(Context context, String name, String
	// phone) {
	// String dbStr = SpUserHelper.getUserDbStr(context);
	//
	// String param = "name=" + name + "&phone=" + phone + "&" + dbStr;
	// String resStr = HttpUtils.doPost(FinalValues.ADD_MEMBER, param);
	// try {
	// JSONObject jsonObject = new JSONObject(resStr);
	// boolean status = jsonObject.getBoolean("status");
	// return status;
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return false;
	// }

	/**
	 * 传递结算数据
	 * 
	 * @return
	 */
	// public static boolean postClearingData(Context context,List<Map<String,
	// Object>> mpList,
	// String totalmoney, String discount, String discountprice,
	// String bid, String membername, String memberphone) {
	//
	// String dbStr = SpUserHelper.getUserDbStr(context);
	//
	// String code = "";
	// String name = "";
	// String num = "";
	// String price = "";
	// String money = "";
	// for (int i = 0; i < mpList.size(); i++) {
	// code += mpList.get(i).get("code").toString() + ",";
	// name += mpList.get(i).get("name").toString() + ",";
	// num += mpList.get(i).get("num").toString() + ",";
	// price += mpList.get(i).get("price").toString() + ",";
	//
	// // ---------------计算单金额---------------------
	// float new_num = Float.valueOf(mpList.get(i).get("num").toString());
	// float new_price = Float.valueOf(mpList.get(i).get("price").toString());
	//
	// float new_money = new_num * new_price;
	// String str_new_money = String.format("%.1f", new_money);
	//
	// money += str_new_money + ",";
	// }
	//
	// String param = "code=" + code + "&name=" + name + "&num=" + num
	// + "&price=" + price + "&money=" + money + "&totalmoney="
	// + totalmoney + "&discount=" + discount + "&discountprice="
	// + discountprice + "&bid=" + bid + "&membername=" + membername
	// + "&memberphone=" + memberphone+"&"+dbStr;
	//
	// System.out.println("param=="+param);
	//
	// String resStr = HttpUtils.doPost(FinalValues.DO_CLEARING, param);
	//
	// System.out.println("resStr=="+resStr);
	//
	// try {
	// JSONObject jsonObject = new JSONObject(resStr);
	// boolean status = jsonObject.getBoolean("status");
	// return status;
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return false;
	// }

	// ==================================本地数据库读写===============================================

	/**
	 * 添加会员
	 */
	public static boolean addMember(Context context, String name, String phone) {

		boolean status = false;

		Map<String, Object> userMap = SpUserHelper.getUserInfo(context);
		String dept = (String) userMap.get("dept");
		SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
				null, SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) { // 打开数据库

			// 执行操作
			db.execSQL(
					"insert into tb_member(id,name, phone,dept) values(?,?,?,?);",
					new Object[] { new Date().getTime(), name, phone, dept });

			db.close(); // 关闭数据库
			status = true;
		}

		return status;
	}

	/**
	 * 查找会员
	 */
	public static Map<String, Object> findMemberByPhone(Context context,
			String phone) {

		SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
				null, SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from tb_member where phone = ? limit 1",
					new String[] { phone });

			if (cursor != null && cursor.getCount() > 0) {

				Map<String, Object> mp = new HashMap<String, Object>();
				while (cursor.moveToNext()) {
					mp.put("name",
							cursor.getString(cursor.getColumnIndex("name")));
					mp.put("phone",
							cursor.getString(cursor.getColumnIndex("phone")));
					mp.put("score",
							cursor.getString(cursor.getColumnIndex("score")));
				}
				db.close();
				return mp;
			}
			db.close();
		}
		return null;
	}

	/**
	 * 结算操作
	 */
	public static boolean postClearingData(Context context,
			List<Map<String, Object>> mpList, String totalmoney,
			String discount, String discountprice, String bid,
			String membername, String memberphone,String orderno) {

		boolean status = false;
		Map<String, Object> userMap = SpUserHelper.getUserInfo(context);
		String addid = (String) userMap.get("id");
		String addname = (String) userMap.get("username");
		String dept = (String) userMap.get("dept");
		String addtime = com.grid.hdsyt.utils.DateUtil.getTime();

		SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
				null, SQLiteDatabase.OPEN_READWRITE);
		if (db.isOpen()) { // 打开本地sqlite数据库

			try {
				db.beginTransaction();

				for (int i = 0; i < mpList.size(); i++) {

					// ---------------计算单金额---------------------
					float new_num = Float.valueOf(mpList.get(i).get("num")
							.toString());
					float new_price = Float.valueOf(mpList.get(i).get("price")
							.toString());

					float new_money = new_num * new_price;
					String str_new_money = String.format("%.1f", new_money);
					// ---------------计算单金额end---------------------

					db.execSQL(
							"insert into tb_detail(id,orderno,addid,addname,addtime,dept,outdept,stockcode"
									+ ",stockname,num,outprice,money,membername,memberphone,totalmoney,discount,discountprice,bid,type)"
									+ " values"
									+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
							new Object[] { new Date().getTime() + i,
									orderno, addid, addname,
									addtime, dept, dept,
									mpList.get(i).get("code"),
									mpList.get(i).get("name"),
									mpList.get(i).get("num"),
									mpList.get(i).get("price"), str_new_money,
									membername, memberphone, totalmoney,
									discount, discountprice, bid, "零售" });
				}
				db.setTransactionSuccessful();
				status = true;
			} finally {
				db.endTransaction();
				db.close();
			}

		}

		return status;
	}

}
