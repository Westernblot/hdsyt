package com.grid.hdsyt.ui.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.grid.hdsyt.db.SqliteHelper;
import com.grid.hdsyt.function.FinalValues;
import com.grid.hdsyt.function.SpUserHelper;
import com.grid.hdsyt.ui.login.LoginProvide;
import com.grid.hdsyt.utils.HttpUtils;
import com.grid.hdsyt.utils.Md5Utils;
import com.grid.hdsyt.utils.SPUtils;

/**
 * 数据提供类
 * 
 * @author PC
 * 
 */
public class MainProvider {

	/**
	 * 查询所有的数据，分配查询
	 * 
	 * @param sRow
	 * @param eRow
	 * @return
	 */
	public static List<Map<String, Object>> selectAllGoods(Context context,
			int sRow, int eRow) {

		String dbStr = SpUserHelper.getUserDbStr(context);

		String param = "sRow=" + sRow + "&eRow=" + eRow + "&" + dbStr;
		List<Map<String, Object>> mpList = new ArrayList<Map<String, Object>>();

		String goodsListString = HttpUtils.doPost(FinalValues.GOODS_SELECT_ALL,
				param);

		try {
			JSONArray jsonArray = new JSONArray(goodsListString);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Map<String, Object> mp = new HashMap<String, Object>();
				mp.put("id", jsonObject.get("id"));
				mp.put("code", jsonObject.get("code"));
				mp.put("name", jsonObject.get("name"));
				mp.put("price", jsonObject.get("price"));
				mp.put("addtime", jsonObject.get("addtime"));
				mpList.add(mp);
			}

			return mpList;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 查询商品 by code
	 * 
	 * @param code
	 * @return
	 */
	// public static Map<String, Object> selectGoodsByCode(Context
	// context,String code) {
	//
	// String dbStr = SpUserHelper.getUserDbStr(context);
	//
	// String param = "code=" + code+"&"+dbStr;
	//
	// Map<String, Object> mp = new HashMap<String, Object>();
	//
	// String resList =
	// HttpUtils.doPost(FinalValues.GOODS_SELECT_BY_CODE,param);
	//
	// try {
	// JSONObject jsonObject = new JSONObject(resList);
	// mp.put("id", jsonObject.get("id"));
	// mp.put("code", jsonObject.get("code"));
	// mp.put("name", jsonObject.get("name"));
	// mp.put("price", jsonObject.get("price"));
	// mp.put("addtime", jsonObject.get("addtime"));
	//
	// return mp;
	//
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	// /**
	// * 获取我的分类
	// */
	// public static List<Map<String, Object>> getMyStockFenlei(Context context)
	// {
	//
	// //-----------获取分布式数据库信息-----------
	// String dbStr = SpUserHelper.getUserDbStr(context);
	// //System.out.println("我的分类接口URL==="+FinalValues.GET_STOCK_CATEGORY+"?"+dbStr);
	// //-----------------------------------------
	// List<Map<String, Object>> mpList = new ArrayList<Map<String, Object>>();
	// String resStr = HttpUtils.doPost(FinalValues.GET_STOCK_CATEGORY,dbStr);
	// try {
	// JSONArray jsonArray = new JSONArray(resStr);
	//
	// for (int i = 0; i < jsonArray.length(); i++) {
	//
	// JSONObject jsonObject = jsonArray.getJSONObject(i);
	// Map<String, Object> mp = new HashMap<String, Object>();
	// mp.put("name", jsonObject.get("category"));
	// mp.put("price",null); //分类没有价格
	// mpList.add(mp);
	// }
	//
	// return mpList;
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	/**
	 * 获取指定分类下的库存商品信息
	 */
	// public static List<Map<String, Object>>
	// getStockGoodsListByCategory(Context context,
	// String category) {
	//
	// String dbStr = SpUserHelper.getUserDbStr(context);
	//
	// String param = "category=" + category+"&"+dbStr;
	// List<Map<String, Object>> mpList = new ArrayList<Map<String, Object>>();
	//
	// String resStr = HttpUtils.doPost(FinalValues.GET_STOCK_LIST_BY_CATEGORY,
	// param);
	//
	// JSONArray jsonArray;
	// try {
	// jsonArray = new JSONArray(resStr);
	// for (int i = 0; i < jsonArray.length(); i++) {
	//
	// JSONObject jsonObject = jsonArray.getJSONObject(i);
	// Map<String, Object> mp = new HashMap<String, Object>();
	// mp.put("id", jsonObject.get("id"));
	// mp.put("name", jsonObject.get("name"));
	// mp.put("code", jsonObject.get("code"));
	// mp.put("pym", jsonObject.get("pym"));
	// mp.put("description", jsonObject.get("description"));
	// mp.put("num", "1"); //此处默认给一个到结算车
	// mp.put("lownum", jsonObject.get("lownum"));
	// mp.put("addid", jsonObject.get("addid"));
	// mp.put("addname", jsonObject.get("addname"));
	// mp.put("addtime", jsonObject.get("addtime"));
	// mp.put("price", jsonObject.get("price"));
	// mp.put("dept", jsonObject.get("dept"));
	// mp.put("category", jsonObject.get("category"));
	// mpList.add(mp);
	// }
	// return mpList;
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	// =============================================本地数据库操作===================================================

	/**
	 * 查询商品
	 * 
	 * @param context
	 * @param code
	 * @return
	 */
	public static Map<String, Object> selectGoodsByCode(Context context,
			String code) {

		Map<String, Object> mp = new HashMap<String, Object>();

		SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
				null, SQLiteDatabase.OPEN_READWRITE);

		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select *  from tb_stock where code = ? limit 1", new String[] { code });

			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {

					mp.put("id",cursor.getString(cursor.getColumnIndex("id")));
					mp.put("name",cursor.getString(cursor.getColumnIndex("name")));
					mp.put("code",cursor.getString(cursor.getColumnIndex("code")));
					mp.put("pym",cursor.getString(cursor.getColumnIndex("pym")));
					mp.put("description",cursor.getString(cursor.getColumnIndex("description")));
					mp.put("num","1");
					mp.put("lownum",cursor.getString(cursor.getColumnIndex("lownum")));
					mp.put("addid",cursor.getString(cursor.getColumnIndex("addid")));
					mp.put("addname",cursor.getString(cursor.getColumnIndex("addname")));
					mp.put("addtime",cursor.getString(cursor.getColumnIndex("addtime")));
					mp.put("price",cursor.getString(cursor.getColumnIndex("price")));
					mp.put("dept",cursor.getString(cursor.getColumnIndex("dept")));
					mp.put("category",cursor.getString(cursor.getColumnIndex("category")));
				}
			}
			db.close();
		}
		return mp;
	}

	/**
	 * 获取我的分类
	 */
	public static List<Map<String, Object>> getMyStockFenlei(Context context) {

		List<Map<String, Object>> mpList = new ArrayList<Map<String, Object>>();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
				null, SQLiteDatabase.OPEN_READWRITE);

		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select distinct category from tb_stock", null);

			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("name",
							cursor.getString(cursor.getColumnIndex("category")));
					mp.put("price", null); // 分类没有价格
					mpList.add(mp);
				}
				db.close();
				return mpList;
			}
			db.close();
		}
		return mpList;

	}

	/**
	 * 获取指定分类下的库存商品信息
	 */
	public static List<Map<String, Object>> getStockGoodsListByCategory(
			Context context, String category) {

		List<Map<String, Object>> mpList = new ArrayList<Map<String, Object>>();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(SqliteHelper.path,
				null, SQLiteDatabase.OPEN_READWRITE);

		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select * from tb_stock where category = ?",
					new String[] { category });

			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("id", cursor.getString(cursor.getColumnIndex("id")));
					mp.put("name",
							cursor.getString(cursor.getColumnIndex("name")));
					mp.put("code",
							cursor.getString(cursor.getColumnIndex("code")));
					mp.put("pym",
							cursor.getString(cursor.getColumnIndex("pym")));
					mp.put("description", cursor.getString(cursor
							.getColumnIndex("description")));
					mp.put("num", "1"); // 此处默认给一个到结算车
					mp.put("lownum",
							cursor.getString(cursor.getColumnIndex("lownum")));
					mp.put("addid",
							cursor.getString(cursor.getColumnIndex("addid")));
					mp.put("addname",
							cursor.getString(cursor.getColumnIndex("addname")));
					mp.put("addtime",
							cursor.getString(cursor.getColumnIndex("addtime")));
					mp.put("price",
							cursor.getString(cursor.getColumnIndex("price")));
					mp.put("dept",
							cursor.getString(cursor.getColumnIndex("dept")));
					mp.put("category",
							cursor.getString(cursor.getColumnIndex("category")));
					mpList.add(mp);
				}
				db.close();
				return mpList;
			}
			db.close();
		}
		return mpList;

	}

}
