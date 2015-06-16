package com.halong.aubaby.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkConnected {

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}
		if (connectivityManager.getActiveNetworkInfo() == null) {
			return false;
		}
		return connectivityManager.getActiveNetworkInfo().isAvailable();
	}
}
