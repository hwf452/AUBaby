package com.halong.aubaby.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

	public static String getStringForJson(JSONObject json, String key)
			throws JSONException {
		if (!json.isNull(key)) {
			return json.getString(key);
		}
		return "";
	}

	public static double getDoubleForJson(JSONObject json, String key)
			throws JSONException {
		if (!json.isNull(key)) {
			return json.getDouble(key);
		}
		return -1;
	}

	public static JSONArray getJSONArrayForJson(JSONObject json, String key)
			throws JSONException {
		if (!json.isNull(key)) {
			return json.getJSONArray(key);
		}
		return new JSONArray();
	}

	public static int getIntForJson(JSONObject json, String key)
			throws JSONException {
		if (!json.isNull(key)) {
			return json.getInt(key);
		}
		return -1;
	}
}
