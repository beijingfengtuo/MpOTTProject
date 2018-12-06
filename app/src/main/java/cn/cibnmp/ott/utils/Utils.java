package cn.cibnmp.ott.utils;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.config.Constant;


public class Utils {
    private final static String TAG = "Utils";

    public static boolean startActivitySafely(Context context, View v,
                                              Intent intent) {
        boolean success = false;
        if (context == null) {
            return success;
        }
        try {
            success = startActivity(context, v, intent);
        } catch (ActivityNotFoundException e) {
            Lg.e(TAG, "ActivityNotFoundException : " + e.getMessage());
        }
        return success;
    }

    private static boolean startActivity(Context context, View v, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            // Only launch using the new animation if the shortcut has not opted
            context.startActivity(intent);
            return true;
        } catch (SecurityException e) {
            Lg.e(TAG, " SecurityException : " + e.getMessage());
        }
        return false;
    }


    public static void startMarquee(TextView tv) {
        Class cl = tv.getClass().getSuperclass();
        while (!cl.equals(TextView.class)) {
            cl = cl.getSuperclass();
        }
        Method startMarquee;
        try {
            startMarquee = cl.getDeclaredMethod("startMarquee", new Class[0]);
            startMarquee.setAccessible(true);
            startMarquee.invoke(tv, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMarquee(TextView tv) {
        Class cl = tv.getClass().getSuperclass();
        while (!cl.equals(TextView.class)) {
            cl = cl.getSuperclass();
        }
        Method stopMarquee;
        try {
            stopMarquee = cl.getDeclaredMethod("stopMarquee", new Class[0]);
            stopMarquee.setAccessible(true);
            stopMarquee.invoke(tv, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String INTENT_EXTRA_IGNORE_LAUNCH_ANIMATION = "com.android.launcher" +
            ".intent.extra.shortcut.INGORE_LAUNCH_ANIMATION";


    /**
     * 过滤 “;” 方法，限制数量为5
     *
     * @param str   要过滤的字符串
     * @param token 要过滤的内容
     * @return
     */
    public static String countToken(String str, String token) {
        String name = str;
        int count = 0;
        while (str.indexOf(token) != -1) {
            count++;
            if (count > 5) {
                break;
            } else {
                str = str.substring(str.indexOf(token) + token.length());
            }
        }
        str = name.substring(0, name.length() - str.length());
        String date = str.replaceAll(";", "    ");
        return date;
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
                Log.e("获取文件大小", "文件不存在!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static void appInstall(Context mContext, String apkuri) {
        Uri local = Uri.parse("file://" + apkuri);
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setDataAndType(local,
                "application/vnd.android.package-archive");
        installIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(installIntent);
    }

    /**
     * 获取扩展存储路径，TF卡、U盘
     */
    private static String getExternalStorageDirectory() {
        String dir = "";
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;

                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        dir = dir.concat(columns[1] + "\n");
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        dir = dir.concat(columns[1] + "\n");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dir;
    }

    public static void logcatToUSB() {
        try {
            String path = getExternalStorageDirectory();
            if (TextUtils.isEmpty(path)) {
                Lg.e(TAG, "USB device does't exists");
                return;
            }
            String[] devices = path.split("\n");
            path = "";
            for (String s : devices) {
                if (s.contains("external_storage/sda")) {
                    path = s;
                    break;
                }
            }
            if (TextUtils.isEmpty(path)) {
                Lg.e(TAG, "USB device does't exists , log can't printout.");
                return;
            }
            Lg.d(TAG, "usb path : " + path);
            String cmd = "logcat -f " + path + "/log_"
                    + System.currentTimeMillis() + ".txt";
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取wifi名称
    public static String getWifiSSID(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : "";
        return wifiId.replaceAll("\"", "");
    }


    /**
     * keycode值转成Direction值
     *
     * @param keyCode
     * @return
     */
    public static int keyCode2Direction(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return View.FOCUS_DOWN;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return View.FOCUS_RIGHT;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                return View.FOCUS_LEFT;

            case KeyEvent.KEYCODE_DPAD_UP:
                return View.FOCUS_UP;

            default:
                return -1;
        }
    }


    /**
     * 获取get传参式的传参字符串 如 id=34
     *
     * @param key
     * @param val
     * @return
     */
    public static String getUrlParameter(String key, String val) {
        if (TextUtils.isEmpty(key))
            return "";
        if (val == null) val = "";
        return key + "=" + val;
    }

    //获取合法的Url方法,返回的url最后都没有'/',拼接地址时需自己把'/'加上
    public static String getValidHttpUrl(String url, String defaultUrl) {
        try {
            if (TextUtils.isEmpty(url))
                return defaultUrl;
            else {
                if (url.endsWith("/"))
                    return url.substring(0, url.length() - 1);
                else return url;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return defaultUrl;
        }
    }

    //获取合法的Url方法,返回的url最后都没有'/',拼接地址时需自己把'/'加上
    public static String getValidHttpUrl2(String url, String defaultUrl1, String defaultUrl2) {
        try {
            if (TextUtils.isEmpty(url))
                return getValidHttpUrl(defaultUrl1, defaultUrl2);
            else {
                if (url.endsWith("/"))
                    return url.substring(0, url.length() - 1);
                else return url;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getValidHttpUrl(defaultUrl1, defaultUrl2);
        }
    }


    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：cn.cibn.chanmobile.TimeService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            Lg.i("ACTIVITY_SERVICE", "...:" + mName);
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 获取错误原因string
     *
     * @return
     */
    public static String getErrorMsg(String code) {
        if (code.equals("00")) {
            return "数据异常";     //请求成功
        } else if (code.equals("01")) {
            return "网络服务中断";   //通讯失败
        } else if (code.equals("02")) {
            return "数据请求失败";   //超时无响应
        }
        return "数据异常";
    }

    /**
     * 获取错误处理方式提示
     *
     * @param code
     * @return
     */
    private static String getInnerHandleMsg(String code) {
        if (code.equals("00")) {
            return "请检查网络设置后重试";   //网络不通
        } else if (code.equals("01") || code.equals("02") || code.equals("04")) {
            return "请联系客服解决";   //01:返回为空 , 02:返回不全 , 04:返回code不合法
        } else if (code.equals("03")) {
            return "服务出现故障，请稍后重试"; //无返回,onError
        }
        return "请检查网络设置后重试";
    }

    public static String getHandleMsg(String errorCode, String handleCode) {
        return getInnerHandleMsg(handleCode) + " (错误码:" + errorCode + handleCode + ")";
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号name
     */
    public static String getVersionName() {
        String version = "";
        try {
            PackageManager manager = App.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.getInstance().getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getAppPackageName() {
        return App.getInstance().getPackageName();
    }

    public static String getAppPackageNamePath() {
        String path = App.getInstance().getPackageName();
        path = path.replace(".", "/");
        return path;
    }


    public static String getVersionCode(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 截取版本号字符串
     *
     * @param versionName
     * @return
     */
    public static String splitVersionName(String versionName) {
        String s = "";
        if (versionName.length() > 5) {
            s = versionName.substring(0, 5);
        }
        return s;
    }

    public static void saveImageToSDCard(Context context, int resID) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resID);
        String filename = Environment.getExternalStorageDirectory() + "/ic_launcher.png";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            out = null;
        }
    }

    // ---------------------------------------->>用户存储的登录标识
    public static long getUserUID() {
        return SharePrefUtils.getLong(Constant.USER_UID, 0);
    }

    public static void setUserUID(long UID) {
        SharePrefUtils.saveLong(Constant.USER_UID, UID);
    }

    //得到手机登录的ID
    public static int getUserUP() {
        return SharePrefUtils.getInt(Constant.USER_UP, 0);
    }

    public static void setUserUP(int up) {
        SharePrefUtils.saveInt(Constant.USER_UP, up);
    }

    //得到微信登录的ID
    public static String getUserOpenId() {
        return SharePrefUtils.getString(Constant.USER_OPENID, "");
    }

    public static void setUserOpenId(String openid) {
        if (openid != null) {
            SharePrefUtils.saveString(Constant.USER_OPENID, openid);
        } else {
            SharePrefUtils.saveString(Constant.USER_OPENID, "");
        }
    }

    public static String getTokenId() {
        return SharePrefUtils.getString(Constant.USER_TOKENID, "");
    }

    public static void setTokenId(String tokenid) {
        if (tokenid != null) {
            SharePrefUtils.saveString(Constant.USER_TOKENID, tokenid);
        } else {
            SharePrefUtils.saveString(Constant.USER_TOKENID, "");
        }
    }

    public static boolean setSay(String saveName) {
        boolean isUpdate = false;
        App.AppName = saveName;
        Resources resources = App.getInstance().getResources();
        Configuration config = resources.getConfiguration();
        if (saveName.equals(App.LO)) {// 老挝语lo_LA
            config.locale = new Locale("lo", "LA");
            isUpdate = true;
        } else if (saveName.equals(App.ENGLISH)) {// 英文
            if (config.locale != Locale.ENGLISH) {
                config.locale = Locale.ENGLISH;
                isUpdate = true;
            }
        } else {// 中文
            if (config.locale != Locale.ENGLISH) {
                config.locale = Locale.getDefault();
                isUpdate = true;
            }
        }
        if (isUpdate) {
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(config, dm);
        }
        return true;
    }

    //动态获取国际化文字转换
    public static String getInterString(Context context, int id) {
        return context.getResources().getString(id);
    }

}
