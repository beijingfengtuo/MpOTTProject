package cn.cibnmp.ott.config;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.cibnmp.ott.App;
import cn.cibnmp.ott.jni.HttpRequest;
import cn.cibnmp.ott.jni.HttpResponseListener;
import cn.cibnmp.ott.utils.Lg;


public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = CrashHandler.class.getSimpleName();
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // 用于格式化日期,作为日志文件名的一部分
    private List<String> saveList = new ArrayList<String>();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        if (saveList.size() > 0) {
            saveList.clear();
        }
        collectDeviceInfo(ctx);
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                saveList.add(versionName);
                saveList.add(versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "收集应用信息时出现错误", e);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.out.println("uncaughtException,app will exit !");
        handleException(ex);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Log.e(TAG, "error : ", e);
        }
        App.getAppManager().crashAppExit(mContext);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        String msgY = saveCatchInfoFile(ex);
        //替换空格回车等
        final String msg = replaceBlank(msgY);

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                int shu = 100;// 测试用，不影响程序
                setError(shu, msg);
                Looper.loop();
            }
        }.start();

        Lg.d(TAG, ex.getMessage());
        return true;
    }

    // 上传错误日志
    private void setError(int shu, String name) {
        String json = "{\"appError\":\"" + name + "\"}";
        Lg.d(TAG, json);
        HttpRequest.getInstance().excute("ReportErrorLog", new Object[]{json, new HttpResponseListener() {
            @Override
            public void onSuccess(String response) {
                Lg.d("ReportErrorLog", "错误日志上传成功：" + response);
            }

            @Override
            public void onError(String error) {
                Lg.d("ReportErrorLog", "错误日志：上传失败");
            }
        }});
        Lg.d(TAG + "1111", shu + "执行完毕");
    }

    /**
     * @param ex
     * @return 返回循环遍历的错误信息
     */
    private String saveCatchInfoFile(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < saveList.size(); i++) {
            switch (i) {
                case 0:
                    sb.append("versionName:" + saveList.get(i) + "\n");
                    break;
                case 1:
                    sb.append("versionCode:" + saveList.get(i) + "\n");
                    break;
                default:
                    break;
            }
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        return sb.toString();
    }

    //将错误日志写到本地文件（此方法暂未使用）
    private void setLog(String name) {
        try {
            String fileName = "crash-ott.log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory() + "/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File saveFile = new File(path + fileName);
                if (saveFile.exists()) {
                    saveFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(name.getBytes());
                fos.close();
                // 发送给开发人员
                // sendCrashLog(saveFile);
            }
        } catch (Exception e) {
            Log.e(TAG, "写入文件时发生错误...", e);
        }
    }

    // 上传文件到服务器（此方法暂未使用）
    public void upload() {
        HttpURLConnection connection = null;
        DataOutputStream dos = null;
        FileInputStream fin = null;
        String boundary = "---------------------------375001916915724";
        String urlPath = "http://192.168.1.100:8080/TestWeb/command=UpdatePicture";
        String lineEnd = "\r\n";
        String pathOfPicture = Environment.getExternalStorageDirectory() + "/crash/crash-ott.log";
        int bytesAvailable, bufferSize, bytesRead;
        int maxBufferSize = 1 * 1024 * 512;
        byte[] buffer = null;
        try {
            Log.d(TAG, "try");
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            // 允许向url流中读写数据
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(true);

            // 启动post方法
            connection.setRequestMethod("POST");
            // 设置请求头内容
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "text/plain");
            // 伪造请求头
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // 开始伪造POST Data里面的数据
            dos = new DataOutputStream(connection.getOutputStream());
            fin = new FileInputStream(pathOfPicture);

            Log.d(TAG, "开始上传.....");
            // --------------------开始上传文件的信息-----------------------------------
            String fileMeta = "--" + boundary + lineEnd + "Content-Disposition: form-data; name=\"uploadedPicture\"; filename=\"" + pathOfPicture + "\"" + lineEnd + "Content-Type: image/jpeg" + lineEnd + lineEnd;
            // 向流中写入fileMeta
            dos.write(fileMeta.getBytes());

            // 取得本地文件的字节流，向url流中写入文件的字节流
            bytesAvailable = fin.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fin.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fin.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fin.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd + lineEnd);
            // --------------------文件信息结束-----------------------------------
            Log.d(TAG, "结束上传");
            // POST Data结束
            dos.writeBytes("--" + boundary + "--");
            // Server端返回的信息
            System.out.println("" + connection.getResponseCode());
            System.out.println("" + connection.getResponseMessage());

            if (dos != null) {
                dos.flush();
                dos.close();
            }
            Log.d(TAG, "success-----------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "upload fail");
        }
    }

    public String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

}