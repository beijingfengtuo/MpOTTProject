package cn.cibnmp.ott.utils;

import android.content.Context;

public class ResourceUtil {

	/**
	 * 
	 * @param resId
	 *            R.string.name
	 * @return
	 */
	public static String getResource(Context context, int resId) {
		return context.getResources().getString(resId);
	}
}
