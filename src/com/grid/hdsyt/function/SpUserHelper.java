package com.grid.hdsyt.function;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.grid.hdsyt.utils.SPUtils;

public class SpUserHelper {
	
	
	/**
	 * 获取登陆系统信息
	 */
	public static Map<String,Object> getUserInfo(Context context){
		String userString = (String) SPUtils.get(context, "userString", "");
		Map<String,Object> userMap = decodeUserString(userString);
		return userMap;
	}

	/**
	 * 解析sp保存的用户信息
	 */
	public static Map<String,Object> decodeUserString(String userString){
		Map<String, Object> mp = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(userString);
			mp.put("id", jsonObject.getString("id"));
			mp.put("pid", jsonObject.getString("pid"));
			mp.put("username", jsonObject.getString("username"));
			mp.put("password", jsonObject.getString("password"));
			mp.put("power", jsonObject.getString("power"));
			mp.put("cnname", jsonObject.getString("cnname"));
			mp.put("dept", jsonObject.getString("dept"));
			mp.put("role", jsonObject.getString("role"));
			mp.put("token", jsonObject.getString("token"));
			mp.put("addtime", jsonObject.getString("addtime"));
			mp.put("db_prefix", jsonObject.getString("db_prefix"));
			mp.put("db_connection", jsonObject.getString("db_connection"));
			return mp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 通用调用
	 */
	public static String getUserDbStr(Context context){
		String userString = (String) SPUtils.get(context, "userString", "");
		Map<String,Object> userMap = decodeUserString(userString);
		
		String db_prefix = (String) userMap.get("db_prefix");
		String db_connection = (String) userMap.get("db_connection");
		String dept = (String) userMap.get("dept");
		String dbStr = "db_prefix="+db_prefix+"&db_connection="+db_connection+"&dept="+dept;
		
		return dbStr;
	}
	
}
