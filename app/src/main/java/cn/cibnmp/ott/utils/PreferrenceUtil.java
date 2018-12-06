package cn.cibnmp.ott.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 数据保存
 * 
 * @author zhuhl 
 * Create 2013-12-19
 */
public class PreferrenceUtil {

	private static final String TAG = PreferrenceUtil.class.getSimpleName();
	private static final String DBNAME = "counter.db";
	public static final String COUNTER = "counter";

	/*
	 * 保存状态信息
	 * 
	 * @param ctx
	 * 
	 * @param url The new value for the preference
	 * 
	 * @param key The name of the preference to modify
	 */
	public static boolean saveStringInfo(Context ctx, String key, String value) {
		SharedPreferences.Editor editor = ctx.getApplicationContext()
				.getSharedPreferences(DBNAME, Context.MODE_PRIVATE).edit();
		// editor.clear();
		Log.d(TAG, "saveInfo:" + value);
		return editor.putString(key, value).commit();
	}

	/*
	 * 保存状态信息
	 * 
	 * @param ctx
	 * 
	 * @param url The new value for the preference
	 * 
	 * @param key The name of the preference to modify
	 */
	public static boolean saveIntInfo(Context ctx, String key, int value) {
		SharedPreferences.Editor editor = ctx.getApplicationContext()
				.getSharedPreferences(DBNAME, Context.MODE_PRIVATE).edit();
		Log.d(TAG, "saveIntInfo:" + value);
		return editor.putInt(key, value).commit();

	}

	public static boolean saveLongInfo(Context ctx, String key, long value) {
		SharedPreferences.Editor editor = ctx.getApplicationContext()
				.getSharedPreferences(DBNAME, Context.MODE_PRIVATE).edit();
		// editor.clear();
		Log.d(TAG, "saveIntInfo:" + value);
		return editor.putLong(key, value).commit();

	}

	public static boolean saveBooleanInfo(Context ctx, String key, boolean value) {
		SharedPreferences.Editor editor = ctx.getApplicationContext()
				.getSharedPreferences(DBNAME, Context.MODE_PRIVATE).edit();
		// editor.clear();
		Log.d(TAG, "saveInfo:" + key + ":" + value);
		return editor.putBoolean(key, value).commit();

	}

	public static boolean saveFloatInfo(Context ctx, String key, float value) {
		SharedPreferences.Editor editor = ctx.getApplicationContext()
				.getSharedPreferences(DBNAME, Context.MODE_PRIVATE).edit();
		// editor.clear();
		Log.d(TAG, "saveInfo:" + key + ":" + value);
		return editor.putFloat(key, value).commit();

	}

	public static float loadFloatInfo(Context ctx, String key) {
		float info = ctx.getApplicationContext().getSharedPreferences(DBNAME, Context.MODE_PRIVATE).getFloat(key, -1);
		Log.d(TAG, "loadInfo:" + info);
		return info;
	}

	public static String loadStringInfo(Context ctx, String key) {
		String infoStr = ctx.getApplicationContext().getSharedPreferences(DBNAME, Context.MODE_PRIVATE)
				.getString(key, "");
		Log.d(TAG, "loadInfo:" + infoStr);
		return infoStr;
	}

	public static int loadIntInfo(Context ctx, String key) {
		int value = ctx.getApplicationContext().getSharedPreferences(DBNAME, Context.MODE_PRIVATE).getInt(key, 0);
		Log.d(TAG, "loadInfo:" + value);
		return value;
	}

	public static long loadLongInfo(Context ctx, String key) {
		long value = ctx.getApplicationContext().getSharedPreferences(DBNAME, Context.MODE_PRIVATE).getLong(key, 0);
		Log.d(TAG, "loadInfo:" + value);
		return value;
	}

	public static boolean loadBooleanInfo(Context ctx, String key, boolean defaultValue) {
		return ctx.getApplicationContext().getSharedPreferences(DBNAME, Context.MODE_PRIVATE)
				.getBoolean(key, defaultValue);
	}

	public static void clearSaveInfo(Context ctx, String key) {
		SharedPreferences.Editor editor = ctx.getApplicationContext()
				.getSharedPreferences(DBNAME, Context.MODE_PRIVATE).edit();
		// editor.clear();
		editor.remove(key);
		editor.commit();
	}
}
