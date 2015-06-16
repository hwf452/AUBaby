package com.halong.aubaby.util;

import java.lang.reflect.Field;

import android.content.Context;
/**
 * 手机屏目工具
 * @author hwf452_pc
 *
 */

public class ScreenUtil {
	
	/**
	 * 像素转DP
	 * @param context
	 * @param px
	 * @return
	 */
	
	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}
	/**
	 * dp转像素
	 * @param context
	 * @param dp
	 * @return
	 */

	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	
	/**
	 * 获取手机状态栏高度
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return statusBarHeight;
	}

}
