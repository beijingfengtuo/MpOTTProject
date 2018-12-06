package cn.cibnmp.ott.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import cn.cibnmp.ott.utils.Lg;

/**
 * Created by wqly on 2017/6/30.
 * log打印日志保存,文件的保存以小时为单位
 */

public class LogcatHelper {

    private static final String TAG = "LogcatHelper";

    private static LogcatHelper instance = null;
    private String dirPath;//保存路径
    private int appid;//应用pid
    private Thread logThread;

    /**
     * @param mContext
     * @param path     log日志保存根目录
     * @return
     */
    public static LogcatHelper getInstance(Context mContext, String path) {
        if (instance == null) {
            instance = new LogcatHelper(mContext, path);
        }
        return instance;
    }

    private LogcatHelper(Context mContext, String path) {
        appid = android.os.Process.myPid();
        if (TextUtils.isEmpty(path)) {
            if (TextUtils.isEmpty(getUSBPath())) {
                dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + "cibn" + File.separator + mContext.getPackageName();
            } else {
                dirPath = getUSBPath()
                        + File.separator + "cibn" + File.separator + mContext.getPackageName();
            }
        } else {
            dirPath = path;
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 启动log日志保存
     */
    public void start() {
        if (!isLogcatRunning) {
            try {
                if (logThread == null) {
                    logThread = new Thread(new LogRunnable(appid, dirPath));
                }
                logThread.start();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isLogcatRunning = false;

    private static class LogRunnable implements Runnable {

        private Process mProcess;
        private FileOutputStream fos;
        private BufferedReader mReader;
        private String cmds;
        private String mPid;

        public LogRunnable(int pid, String dirPath) {
            this.mPid = "" + pid;
            try {
                File file = new File(dirPath, FormatDate.getFormatDate() + ".log");
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cmds = "logcat *:v | grep \"(" + mPid + ")\"";
        }

        @Override
        public void run() {
            try {
                mProcess = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()), 1024);
                String line;
                while ((line = mReader.readLine()) != null) {
                    if (line.length() == 0) {
                        continue;
                    }
                    if (fos != null && line.contains(mPid)) {
                        fos.write((FormatDate.getFormatTime() + " " + line + "\r\n").getBytes());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mProcess != null) {
                    mProcess.destroy();
                    mProcess = null;
                }
                try {
                    if (mReader != null) {
                        mReader.close();
                        mReader = null;
                    }
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取usb目录的方法
     *
     * @return
     */
    public static String getUSBPath() {
        try {
            String path = getExternalStorageDirectory();
            if (TextUtils.isEmpty(path)) {
                Lg.e(TAG, "USB device does't exists");
                return null;
            }
            Lg.d(TAG, "getExternalStorageDirectory : " + path);
            String[] devices = path.split("\n");
            path = "";
            for (String s : devices) {
                if (s.contains("/sda")) {
                    path = s;
                    break;
                }
            }
            if (TextUtils.isEmpty(path)) {
                Lg.e(TAG, "USB device does't exists , log can't printout.");
                return null;
            }
            Lg.d(TAG, "usb path : " + path);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取扩展存储路径，TF卡、U盘
     */
    private static String getExternalStorageDirectory() {
        String dir = new String();
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

    @SuppressLint("SimpleDateFormat")
    private static class FormatDate {

        public static String getFormatDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
            return sdf.format(System.currentTimeMillis());
        }

        public static String getFormatTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(System.currentTimeMillis());
        }
    }
}
