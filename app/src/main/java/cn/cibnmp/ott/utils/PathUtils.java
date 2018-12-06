package cn.cibnmp.ott.utils;

import android.os.Build;
import android.os.Environment;

import cn.cibnmp.ott.App;

/**
 * Created by wanqi on 2017/3/10.
 */

public class PathUtils {

    public static String getJniCachePath() {
//        try {
//            return ((Environment.MEDIA_MOUNTED.equals(Environment
//                    .getExternalStorageState()) || !Environment
//                    .isExternalStorageRemovable()) ? App.getInstance()
//                    .getExternalCacheDir().getPath() : App.getInstance()
//                    .getCacheDir().getPath())
//                    + "/jni";
//        } catch (Exception e) {
//            e.printStackTrace();
            return App.getInstance().getCacheDir().getPath() + "/jni";
//        }
    }

    public static String getDownloadPath() {
//        try {
//            if ((Environment.MEDIA_MOUNTED.equals(Environment
//                    .getExternalStorageState()) || !Environment
//                    .isExternalStorageRemovable())) {
//                if (Build.VERSION.SDK_INT >= 23) {
//                    return App.getInstance()
//                            .getExternalCacheDir().getPath() + "/download";
//                } else {
//                    return Environment.getExternalStorageDirectory().getAbsolutePath() + "/cibn/download";
//                }
//
//            } else {
//                return App.getInstance()
//                        .getCacheDir().getPath() + "/download";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
            return App.getInstance().getCacheDir().getPath() + "/download";
//        }
    }

}
