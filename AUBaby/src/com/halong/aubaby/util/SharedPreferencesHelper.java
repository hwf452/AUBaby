package com.halong.aubaby.util;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {

	public final static String SHAREPREFENCENAME = "EFBANDROID";

	public final static String USERID = "userid";
	public final static String ACCOUNT = "account";
	public final static String PASSWORD = "password";
	public final static String IMEI = "imei";

	public static Boolean setStringValue(Context context, String key,
			String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREPREFENCENAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public static String getStringValue(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREPREFENCENAME, 0);
		return sharedPreferences.getString(key, "");
	}

	public static Boolean setIntValue(Context context, String key,
			int defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREPREFENCENAME, defaultValue);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, defaultValue);
		return editor.commit();
	}

	public static int getIntValue(Context context, String key, int defaultValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREPREFENCENAME, 0);
		return sharedPreferences.getInt(key, defaultValue);
	}

	public static int getIntValue(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREPREFENCENAME, 0);
		return sharedPreferences.getInt(key, 0);
	}

	public static Boolean setLongValue(Context context, String key, Long value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREPREFENCENAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public static Long getLongValue(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREPREFENCENAME, -1);
		return sharedPreferences.getLong(key, -1);
	}

	public static void clear(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREPREFENCENAME, 0);
		sharedPreferences.edit().clear().commit();
	}

	//保存ArrayList<String> 
	public static boolean saveArray(Context context, String key,
			ArrayList<String> list) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHAREPREFENCENAME, 0);
		SharedPreferences.Editor mEdit1 = sharedPreferences.edit();
		mEdit1.putInt(key, list.size()); /* list is an array */

		for (int i = 0; i < list.size(); i++) {
			mEdit1.remove(key + key + i);
			mEdit1.putString(key + key + i, list.get(i));
		}

		return mEdit1.commit();
	}
//获取ArrayList<String> 
	public static void loadArray(Context mContext, String key,
			ArrayList<String> list) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				SHAREPREFENCENAME, 0);
		list.clear();
		int size = sharedPreferences.getInt(key, 0);

		for (int i = 0; i < size; i++) {
			list.add(sharedPreferences.getString(key + key + i, null));

		}
	}
}
