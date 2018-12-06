package cn.cibnmp.ott.utils;

import android.util.Log;

public class Lg {
	private static boolean debug = true;
	private final static String TAG = "cibn_log";

	public static void i(String tag, String msg) {
		if (debug) {
			if (tag == null) {
				tag = TAG;
			}
			if (msg == null) {
				msg = "";
			}
			Log.i(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (tag == null) {
			tag = TAG;
		}
		if (msg == null) {
			msg = "";
		}
		Log.e(tag, msg);
	}

	public static void e(String msg) {
		e(null, msg);
	}

	public static void v(String tag, String msg) {
		if (debug) {
			if (tag == null) {
				tag = TAG;
			}
			if (msg == null) {
				msg = "";
			}
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (debug) {
			if (tag == null) {
				tag = TAG;
			}
			if (msg == null) {
				msg = "";
			}
			Log.d(tag, msg);
		}
	}

	public static void d(String msg) {
		d(null, msg);
	}

	public static void w(String tag, String msg) {
		if (debug) {
			if (tag == null) {
				tag = TAG;
			}
			if (msg == null) {
				msg = "";
			}
			Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (debug) {
			if (tag == null) {
				tag = TAG;
			}
			if (msg == null) {
				msg = "";
			}
			Log.w(tag, msg, tr);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (tag == null) {
			tag = TAG;
		}
		if (msg == null) {
			msg = "";
		}
		Log.e(tag, msg, tr);
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (debug) {
			if (tag == null) {
				tag = TAG;
			}
			if (msg == null) {
				msg = "";
			}
			Log.d(tag, msg, tr);
		}
	}
	
	public static void logI(String tag, String content) {
		int p = 2000;
		long length = content.length();
		if (length < p || length == p)
			Log.i(tag, content);
		else {
			while (content.length() > p) {
				String logContent = content.substring(0, p);
				content = content.replace(logContent, "");
				Log.i(tag, logContent);
			}
			Log.i(tag, content);
		}
	}
}
