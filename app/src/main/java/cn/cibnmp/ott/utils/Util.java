package cn.cibnmp.ott.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import cn.cibnmp.ott.config.Constant;
import cn.cibnmp.ott.App;
import cn.cibnmp.ott.bean.RequestDataError;
import cn.cibnmp.ott.jni.JNIInterface;
import cn.cibnmp.ott.utils.img.FastBlur;

/**
 * Created by wanqi on 2017/2/20.
 */

public class Util {

    //获取可用运存大小
    public static long getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        System.out.println("可用内存---->>>" + mi.availMem / (1024 * 1024) + "MB");
        return mi.availMem / (1024 * 1024);
    }

    //获取总运存大小
    public static long getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Lg.i(str2, num + "\t");
            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }
        System.out.println("总运存---->>>" + initial_memory / (1024 * 1024) + "MB");
        return initial_memory / (1024 * 1024);
    }


    /**
     * 获取sd卡的大小，不存在返回0
     *
     * @return
     */
    public static long getSDCardSize() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();

            long total = blockSize * blockCount / (1024 * 1024);
            Lg.d("getSDCardSize", "---->>>block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + total + "MB");
            return total;
        }
        return 0;
    }

    /**
     * 获取sd卡可用的大小，sd卡不存在时返回0
     *
     * @return
     */
    public static long getAvailableSDCardSize() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long availCount = sf.getAvailableBlocks();

            long available = availCount * blockSize / (1024 * 1024);
            Lg.d("getSDCardSize", "---->>>可用的block数目：:" + availCount + ",剩余空间:" + available + "MB");
            return available;
        }
        return 0;
    }


    /**
     * 获取内部存储/flash的大小
     *
     * @return
     */
    public static long getRomTotalSize() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();

        long total = blockSize * blockCount / (1024 * 1024);
        Lg.d("getRomTotalSize", "---->>>block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + total + "MB");
        return total;
    }

    /**
     * 获取内部存储/flash的可用大小
     *
     * @return
     */
    public static long getRomavAilableSize() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();

        long available = blockSize * availCount / (1024 * 1024);
        Lg.d("getRomTotalSize", "---->>>可用的block数目：:" + availCount + ",可用大小:" + available + "MB");
        return available;
    }

    //搜索相关截取字符串的非null判断
    public static String replaceStr(String str){
        if (str ==null||"".equals(str)) {
            return "";
        } else {
            return str.replace(';', '\u3000');
        }
    }

    /**
     * 高斯模糊效果
     *
     * @param activity
     * @return
     */
    public static Bitmap blur(Activity activity, int x, int y, int w, int h) {
        try {

            View v = activity.getWindow().getDecorView();
            v.setDrawingCacheEnabled(true);
            v.buildDrawingCache();
            Bitmap bitmap = v.getDrawingCache();
            Rect frame = new Rect();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                activity.getWindow().getDecorView()
                        .getWindowVisibleDisplayFrame(frame);
            }
            Bitmap b = Bitmap.createBitmap(bitmap, x, y, w, h);
            v.destroyDrawingCache();

            float scaleFactor = 2;
            float radius = 7;

            Bitmap overlay = Bitmap.createBitmap(
                    (int) (w / scaleFactor),
                    (int) (h / scaleFactor),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.translate(0, 0);
            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(b, 0, 0, paint);

            overlay = FastBlur.doBlur(overlay, (int) radius, true);
            return overlay;

        } catch (Throwable throwable) {
        }
        return null;
    }

    public static void reportRequestDataError(String url, String errorcode, String errordesc) {
        try {
            RequestDataError requestDataError = new RequestDataError(url == null ? "" : url, errorcode, errordesc);
            String json = JSON.toJSONString(requestDataError);
            JNIInterface.getInstance().reportRequestDataError(App.handleId, json, json.getBytes().length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getVersionName(long verno) {
        return String.format("%s.%s.%s.%s", (int) (verno >> 56) & 0xFF, (int) (verno >> 40) & 0xFFFF, (int) (verno >> 24) & 0xFFFF, (int) (verno >> 0) & 0xFFFFFF);
    }

    //获取文件夹路径
    private static String getFolderPath(String filePath) {
        String folderPath = filePath;
        int pos = filePath.lastIndexOf("/");
        if (pos != -1) {
            folderPath = filePath.substring(0, pos);
        }
        return folderPath;
    }

    //安装 apk
    public static boolean installApk(Context context, String fileName) {
        try {
            Log.d("test", fileName);
            try {
                String strPath = getFolderPath(fileName);
                String cmd = "chmod 777 " + strPath;
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(cmd);
            } catch (IOException e) {
                // TODO Auto-generated catch block1
                e.printStackTrace();
            }//.getContext()
            try {
                String cmd = "chmod 777 " + fileName;
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(cmd);
            } catch (IOException e) {
                // TODO Auto-generated catch block1
                e.printStackTrace();
            }//.getContext()
            File apk = new File(fileName);
            if (apk.exists()) {
                Uri uri = Uri.fromFile(apk);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                    //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                    Uri apkUri =
                            FileProvider.getUriForFile(App.getInstance(), App.getInstance().getPackageName() + ".fileProvider", apk);
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(uri,
                            "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void appInstall(Context mContext, String apkuri) {
        File apk = new File(apkuri);
        if (apk.exists()) {
            Uri uri = Uri.fromFile(apk);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri =
                        FileProvider.getUriForFile(App.getInstance(), "cn.cibnhp.grand.fileProvider", apk);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(uri,
                        "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            mContext.startActivity(intent);
        } else {
            Lg.e("-----appInstall ,  apkuri 不存在！");
        }
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * 获取epgId参数
     *
     * @param uri
     * @return
     */
    public static String getEpgId(Uri uri) {
        if (uri == null)
            return "";
        return uri.getQueryParameter(Constant.epgIdKey);
    }

    /**
     * 获取epgId参数
     *
     * @param intent
     * @return
     */
    public static String getEpgId2(Intent intent) {
        if (intent == null)
            return "";
        return intent.getStringExtra(Constant.epgIdKey);
    }


    /**
     * 获取conttentId参数
     *
     * @param uri
     * @return
     */
    public static String getContentId(Uri uri) {
        if (uri == null)
            return "";
        return uri.getQueryParameter(Constant.contentIdKey);
    }

    public static String getContentId2(Intent intent) {
        if (intent == null)
            return "";
        return intent.getStringExtra(Constant.contentIdKey);
    }

    public static String getSid2(Intent intent) {
        if (intent == null)
            return "";
        return intent.getStringExtra(Constant.sidKey);
    }

    /**
     * 获取action参数
     *
     * @param uri
     * @return
     */
    public static String getAction(Uri uri) {
        if (uri == null)
            return "";
        return uri.getQueryParameter(Constant.actionKey);
    }

    public static String getAction2(Intent intent) {
        if (intent == null)
            return "";
        return intent.getStringExtra(Constant.actionKey);
    }

    /**
     * 获取actionUrl参数
     *
     * @param uri
     * @return
     */
    public static String getActionUrl(Uri uri) {
        if (uri == null)
            return "";
        String au = uri.getQueryParameter(Constant.actionUrlKey);
        if (TextUtils.isEmpty(au))
            return "";
        try {
            return URLDecoder.decode(au, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getActionUrl2(Intent intent) {
        if (intent == null)
            return "";
        String au = intent.getStringExtra(Constant.actionUrlKey);
        if (TextUtils.isEmpty(au))
            return "";
        try {
            return URLDecoder.decode(au, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取actionParam_p1参数
     *
     * @param uri
     * @return
     */
    public static String getActionParamP1(Uri uri) {
        if (uri == null)
            return "";
        String p1 = uri.getQueryParameter(Constant.p1ParamKey);
        if (TextUtils.isEmpty(p1))
            return "";
        try {
            return URLDecoder.decode(p1, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getActionParamP1_2(Intent intent) {
        if (intent == null)
            return "";
        String p1 = intent.getStringExtra(Constant.p1ParamKey);
        if (TextUtils.isEmpty(p1))
            return "";
        try {
            return URLDecoder.decode(p1, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取actionParam_p2参数
     *
     * @param uri
     * @return
     */
    public static String getActionParamP2(Uri uri) {
        if (uri == null)
            return "";
        String p2 = uri.getQueryParameter(Constant.p2ParamKey);
        if (TextUtils.isEmpty(p2))
            return "";
        try {
            return URLDecoder.decode(p2, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getActionParamP2_2(Intent intent) {
        if (intent == null)
            return "";
        String p2 = intent.getStringExtra(Constant.p2ParamKey);
        if (TextUtils.isEmpty(p2))
            return "";
        try {
            return URLDecoder.decode(p2, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取actionParam_p3参数
     *
     * @param uri
     * @return
     */
    public static String getActionParamP3(Uri uri) {
        if (uri == null)
            return "";
        String p3 = uri.getQueryParameter(Constant.p3ParamKey);
        if (TextUtils.isEmpty(p3))
            return "";
        try {
            return URLDecoder.decode(p3, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getActionParamP3_2(Intent intent) {
        if (intent == null)
            return "";
        String p3 = intent.getStringExtra(Constant.p3ParamKey);
        if (TextUtils.isEmpty(p3))
            return "";
        try {
            return URLDecoder.decode(p3, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取actionParam_p3参数
     *
     * @param uri
     * @return
     */
    public static String getFrag(Uri uri) {
        if (uri == null)
            return "";
        return uri.getQueryParameter("frag");
    }

    public static String getFrag2(Intent intent) {
        if (intent == null)
            return "";
        return intent.getStringExtra("frag");
    }

}
