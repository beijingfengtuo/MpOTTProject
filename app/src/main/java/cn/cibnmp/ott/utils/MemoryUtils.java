package cn.cibnmp.ott.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug.MemoryInfo;
import android.os.Process;


public class MemoryUtils {

	private static final String TAG = "MemoryUtils";

	public static void logoutMemoryInfo(Context context) {
		try {
			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			MemoryInfo[] memoryInfoArray = am
					.getProcessMemoryInfo(new int[] { Process.myPid() });
			MemoryInfo pidMemoryInfo = memoryInfoArray[0];
			Lg.d(TAG, "---------------PSS : " + pidMemoryInfo.getTotalPss()+"--------------");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
