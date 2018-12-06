package cn.cibnmp.ott.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;


/**
 * @description: 一些常用的操作方法。判断系统版本，等
 */
public final class ApolloUtils {

	/**
	 * The threshold used calculate if a color is light or dark
	 */
	private static final int BRIGHTNESS_THRESHOLD = 130;

	/* This class is never initiated */
	public ApolloUtils() {
	}

	/**
	 * Used to determine if the current device is a Google TV
	 * 
	 * @param context
	 *            The {@link Context} to use
	 * @return True if the device has Google TV, false otherwise
	 */
	public static final boolean isGoogleTV(final Context context) {
		return context.getPackageManager().hasSystemFeature(
				"com.google.android.tv");
	}

	/**
	 * Used to determine if the device is running Froyo or greater
	 * 
	 * @return True if the device is running Froyo or greater, false otherwise
	 */
	public static final boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * Used to determine if the device is running Gingerbread or greater
	 * 
	 * @return True if the device is running Gingerbread or greater, false
	 *         otherwise
	 */
	public static final boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * Used to determine if the device is running Honeycomb or greater
	 * 
	 * @return True if the device is running Honeycomb or greater, false
	 *         otherwise
	 */
	public static final boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * Used to determine if the device is running Honeycomb-MR1 or greater
	 * 
	 * @return True if the device is running Honeycomb-MR1 or greater, false
	 *         otherwise
	 */
	public static final boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/**
	 * Used to determine if the device is running ICS or greater
	 * 
	 * @return True if the device is running Ice Cream Sandwich or greater,
	 *         false otherwise
	 */
	public static final boolean hasICS() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	/**
	 * Used to determine if the device is running Jelly Bean or greater
	 * 
	 * @return True if the device is running Jelly Bean or greater, false
	 *         otherwise
	 */
	public static final boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	/**
	 * Used to determine if the device is currently in landscape mode
	 * 
	 * @param context
	 *            The {@link Context} to use.
	 * @return True if the device is in landscape mode, false otherwise.
	 */
	public static final boolean isLandscape(final Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	/**
	 * Execute an {@link AsyncTask} on a thread pool
	 * 
	 * @param forceSerial
	 *            True to force the task to run in serial order
	 * @param task
	 *            Task to execute
	 * @param args
	 *            Optional arguments to pass to
	 *            {@link AsyncTask#execute(Object[])}
	 * @param <T>
	 *            Task argument type
	 */
	@SuppressLint("NewApi")
	public static <T> void execute(final boolean forceSerial,
								   final AsyncTask<T, ?, ?> task, final T... args) {
		final WeakReference<AsyncTask<T, ?, ?>> taskReference = new WeakReference<AsyncTask<T, ?, ?>>(
				task);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.DONUT) {
			throw new UnsupportedOperationException(
					"This class can only be used on API 4 and newer.");
		}

		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
					|| forceSerial) {
				taskReference.get().execute(args);
			} else {
				taskReference.get().executeOnExecutor(
						AsyncTask.THREAD_POOL_EXECUTOR, args);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to determine if there is an active data connection and what type of
	 * connection it is if there is one
	 * 
	 * @param context
	 *            The {@link Context} to use
	 * @return True if there is an active data connection, false otherwise.
	 *         Also, if the user has checked to only download via Wi-Fi in the
	 *         settings, the mobile data and other network connections aren't
	 *         returned at all
	 */
	public static final boolean isOnline(final Context context) {
		/*
		 * This sort of handles a sudden configuration change, but I think it
		 * should be dealt with in a more professional way.
		 */
		if (context == null) {
			return false;
		}

		boolean state = false;
		final boolean onlyOnWifi = false;// PreferenceUtils.getInstace(context).onlyOnWifi();

		/* Monitor network connections */
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		/* Wi-Fi connection */
		final NetworkInfo wifiNetwork = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null) {
			state = wifiNetwork.isConnectedOrConnecting();
		}

		/* Mobile data connection */
		final NetworkInfo mbobileNetwork = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mbobileNetwork != null) {
			if (!onlyOnWifi) {
				state = mbobileNetwork.isConnectedOrConnecting();
			}
		}

		/* Other networks */
		final NetworkInfo activeNetwork = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetwork != null) {
			if (!onlyOnWifi) {
				state = activeNetwork.isConnectedOrConnecting();
			}
		}

		return state;
	}

	/**
	 * Calculate whether a color is light or dark, based on a commonly known
	 * brightness formula.
	 * 
	 * @see {@literal http://en.wikipedia.org/wiki/HSV_color_space%23Lightness}
	 */
	public static final boolean isColorDark(final int color) {
		return (30 * Color.red(color) + 59 * Color.green(color) + 11 * Color
				.blue(color)) / 100 <= BRIGHTNESS_THRESHOLD;
	}

	/**
	 * Runs a piece of code after the next layout run
	 * 
	 * @param view
	 *            The {@link View} used.
	 * @param runnable
	 *            The {@link Runnable} used after the next layout run
	 */
	@SuppressLint("NewApi")
	public static void doAfterLayout(final View view, final Runnable runnable) {
		final OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				/* Layout pass done, unregister for further events */
				if (hasJellyBean()) {
					view.getViewTreeObserver().removeOnGlobalLayoutListener(
							this);
				} else {
					view.getViewTreeObserver().removeGlobalOnLayoutListener(
							this);
				}
				runnable.run();
			}
		};
		view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
	}


	/**
	 * Used to know if Apollo was sent into the background
	 * 
	 * @param context
	 *            The {@link Context} to use
	 */
	public static final boolean isApplicationSentToBackground(
			final Context context) {
		final ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		final List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			final ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取sdcard剩余容量，单位M,若sd不存在则返回系统存储剩余容量
	 * 
	 * @return
	 */
	public static long getSDFreeSize() {
		try {
			if (!Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				Lg.e("ApolllUtils", "the device hasn't avaible sdcard . ");
				return readSystem();
			}
			// 取得SD卡文件路径
			File path = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(path.getPath());
			// 获取单个数据块的大小(Byte)
			long blockSize = sf.getBlockSize();
			// 空闲的数据块的数量
			long freeBlocks = sf.getAvailableBlocks();
			// 返回SD卡空闲大小
			// return freeBlocks * blockSize; //单位Byte
			// return (freeBlocks * blockSize)/1024; //单位KB
			return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 获取系统容量剩余大小
	 * @return
	 */
	private static long readSystem() {
		File root = Environment.getRootDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long availCount = sf.getAvailableBlocks();
		return availCount * blockSize / 1024 / 1024;
	}
}
