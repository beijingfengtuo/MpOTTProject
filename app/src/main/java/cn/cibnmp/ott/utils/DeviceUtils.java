package cn.cibnmp.ott.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.ut.device.UTDevice;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DeviceUtils {


	/*
     * public static String getSerialNumber() { String SerialNumber =
	 * android.os.Build.SERIAL; return SerialNumber == null ? SerialNumber : "";
	 * }
	 */

    /**
     * getSerialNumber
     */
    public static String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial != null ? serial : "";
    }

    private static String _convertToMac(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            byte b = mac[i];
            int value = 0;
            if (b >= 0 && b < 16) {// Jerry(2013-11-6): if (b>=0&&b<=16) => if
                // (b>=0&&b<16)
                value = b;
                sb.append("0" + Integer.toHexString(value));
            } else if (b >= 16) {// Jerry(2013-11-6): else if (b>16) => else if
                // (b>=16)
                value = b;
                sb.append(Integer.toHexString(value));
            } else {
                value = 256 + b;
                sb.append(Integer.toHexString(value));
            }
            if (i != mac.length - 1) {
                sb.append(":");
            }
        }
        return sb.toString();
    }

    private static String _getLocalEthernetMacAddress() {
        String mac = null;
        try {
            Enumeration<?> localEnumeration = NetworkInterface
                    .getNetworkInterfaces();

            while (localEnumeration.hasMoreElements()) {
                NetworkInterface localNetworkInterface = (NetworkInterface) localEnumeration
                        .nextElement();
                String interfaceName = localNetworkInterface.getDisplayName();

                if (interfaceName == null) {
                    continue;
                }

                if (interfaceName.equals("eth0")) {
                    mac = _convertToMac(localNetworkInterface
                            .getHardwareAddress());
                    if (mac != null && mac.startsWith("0:")) {
                        mac = "0" + mac;
                    }
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return mac;
    }

    @SuppressLint("DefaultLocale")
    private static String _getEthMacAddress2() {
        String mac = _loadFileAsString("/sys/class/net/eth0/address");
        if (mac == null) {
            mac = "";
        } else {
            mac = mac.toUpperCase();
            if (mac.length() > 17) {
                mac = mac.substring(0, 17);
            }
        }

        return mac;
    }

    /**
     * 获取有线mac地址，若没有返回空字符串
     *
     * @return
     */
    public static String getLanMac() {
        String lanMac = "";
        if (!TextUtils.isEmpty(lanMac)) {
            return lanMac;
        }
        lanMac = _getLocalEthernetMacAddress();
        if (TextUtils.isEmpty(lanMac)) {
            lanMac = _getEthMacAddress2();
        }

        return lanMac == null ? "" : lanMac;
    }

    /**
     * 获取无线mac地址，若没有返回空字符串
     *
     * @return
     */
    public static String getWlanMac(Context context) {
        /*String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return macSerial == null ? "" : macSerial;*/
        return getWifiMac(context);
    }

    private static String _loadFileAsString(String filePath) {
        try {
            if (new File(filePath).exists()) {
                StringBuffer fileData = new StringBuffer(1000);
                BufferedReader reader = new BufferedReader(new FileReader(
                        filePath));
                char[] buf = new char[1024];
                int numRead = 0;
                while ((numRead = reader.read(buf)) != -1) {
                    String readData = String.valueOf(buf, 0, numRead);
                    fileData.append(readData);
                }
                reader.close();
                return fileData.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    // 获取CPU名字
    public static String getCpuName() {
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    private static String getWifiMacDefault(WifiManager wifiManager) {
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info == null) {
            return "";
        }
        return info.getMacAddress();
    }

    public static String getWifiMac(Context context) {

        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 24) {
            return getWlanMac2();
        } else if (Build.VERSION.SDK_INT >= 24) {
            return getWlanMac3();
        }

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String mac = getWifiMacDefault(wifiManager);
        if (!TextUtils.isEmpty(mac)) {
            return mac;
        }

        boolean isWifiOk = tryOpenMAC(wifiManager);
        for (int i = 0; i < 30; i++) {
            if (i != 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mac = getWifiMacDefault(wifiManager);
            if (!TextUtils.isEmpty(mac))
                break;
        }
        if (isWifiOk) {
            tryCloseMAC(wifiManager);
        }
        return mac;
    }

    /**
     * 获取手机的MAC地址,6.0版本以上获取无线mac的方法
     *
     * @return
     */
    public static String getWlanMac2() {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return macSerial;
    }

    /**
     * 获取手机的MAC地址,7.0版本以上获取无线mac的方法
     *
     * @return
     */
    public static String getWlanMac3() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    private static boolean tryOpenMAC(WifiManager manager) {
        boolean softOpenWifi = false;
        int state = manager.getWifiState();
        if (state != WifiManager.WIFI_STATE_ENABLED
                && state != WifiManager.WIFI_STATE_ENABLING) {
            manager.setWifiEnabled(true);
            softOpenWifi = true;
        }
        return softOpenWifi;
    }

    // 尝试关闭MAC
    private static void tryCloseMAC(WifiManager manager) {
        manager.setWifiEnabled(false);
    }

    // 获取手机型号
    public static String getDeviceName() {
        String devName = Build.MODEL;
        return devName == null ? "" : devName;
    }

    // 获取版本号
    public static String getVersionName() {
        String verName = Build.VERSION.RELEASE;
        return verName == null ? "" : verName;
    }

    // 获取HardWare
    public static String getHardWare() {
        String hardName = Build.HARDWARE;
        return hardName == null ? "" : hardName;
    }

    // 获取hostName
    public static String getHOST() {
        String hostName = Build.HOST;
        return hostName == null ? "" : hostName;
    }

    // RAM内存大小
    public static long getRamMemory() {
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
                // Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        // return Formatter.formatFileSize(context, initial_memory);//
        // Byte转换为KB或者MB，内存大小规格化
        // System.out.println("总运存--->>>"+initial_memory/(1024*1024));
        return initial_memory;
    }

    // ROM内存大小
    public static long getRomMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    // 屏幕分辨率
    public static String getScreenResolution(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        mWm.getDefaultDisplay().getMetrics(dm);
        String strOpt = dm.widthPixels + " * " + dm.heightPixels;
        return strOpt;
    }

    public static String getLocalHostIp() {
        String ipaddress = "";
//        try {
//            Enumeration<NetworkInterface> en = NetworkInterface
//                    .getNetworkInterfaces();
//            // 遍历所用的网络接口
//            while (en.hasMoreElements()) {
//                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
//                Enumeration<InetAddress> inet = nif.getInetAddresses();
//                // 遍历每一个接口绑定的所有ip
//                while (inet.hasMoreElements()) {
//                    InetAddress ip = inet.nextElement();
//                    if (!ip.isLoopbackAddress()
//                            && InetAddressUtils.isIPv4Address(ip
//                            .getHostAddress())) {
//                        return ipaddress = ip.getHostAddress();
//                    }
//                }
//
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
        return ipaddress;

    }

    public static String getV4IP() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com";

        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n");
            }
            //System.out.println(inputLine.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            String ipstr = m.group(1);
            ip = ipstr;
            //System.out.println(ipstr);
        }
        return ip;
    }

    // 解码芯片组
    public static String getSupportMediaCodecHardDecoder() {
        StringBuilder sb = new StringBuilder();
        // 读取系统配置文件/system/etc/media_codecc.xml
        File file = new File("/system/etc/media_codecs.xml");

        InputStream inFile = null;
        try {
            inFile = new FileInputStream(file);
        } catch (Exception e) {
            // TODO: handle exception
        }

        if (inFile != null) {
            XmlPullParserFactory pullFactory;
            try {
                pullFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = pullFactory.newPullParser();
                xmlPullParser.setInput(inFile, "UTF-8");
                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName = xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if ("MediaCodec".equals(tagName)) {
                                String componentName = xmlPullParser
                                        .getAttributeValue(0);
                                if (componentName.startsWith("OMX.")) {
                                    if (!componentName.startsWith("OMX.google.")) {
                                        // isHardcode = true;
                                        sb.append(componentName + ";");
                                    }
                                }
                            }
                    }
                    eventType = xmlPullParser.next();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return sb.toString();
    }

//    public static String getJniCachePath() {
//        try {
//            return ((Environment.MEDIA_MOUNTED.equals(Environment
//                    .getExternalStorageState()) || !Environment
//                    .isExternalStorageRemovable()) ? App.getInstance()
//                    .getExternalCacheDir().getPath() : App.getInstance()
//                    .getCacheDir().getPath())
//                    + "/jni";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return App.getInstance().getCacheDir().getPath() + "/jni";
//        }
//    }

//    public static String getDownloadPath() {
//        try {
//            return ((Environment.MEDIA_MOUNTED.equals(Environment
//                    .getExternalStorageState()) || !Environment
//                    .isExternalStorageRemovable()) ? App.getInstance()
//                    .getExternalCacheDir().getPath() : App.getInstance()
//                    .getCacheDir().getPath())
//                    + "/download";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return App.getInstance().getCacheDir().getPath() + "/download";
//        }
//    }

    public static int getAndroidOSVersion() {
        int osVersion;
        try {
            osVersion = Integer.valueOf(Build.VERSION.SDK);
        } catch (Exception e) {
            osVersion = 0;
        }

        return osVersion;
    }

    public static String getBluetoothAddr() {
        BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (m_BluetoothAdapter == null)
            return "";
        String m_szBTMAC = m_BluetoothAdapter.getAddress();
        return m_szBTMAC == null ? "" : m_szBTMAC;
    }

    public static String getUniqueId() {
        StringBuilder builder = new StringBuilder("epg");
        builder.append(checkString(Build.BOARD)).append(Build.HOST)
                .append(checkString(Build.MODEL)).append(Build.BRAND)
                .append(checkString(Build.DEVICE)).append(Build.MANUFACTURER)
                .append(checkString(Build.CPU_ABI)).append(Build.CPU_ABI2)
                .append(checkString(Build.PRODUCT));

        return builder.toString();
    }

    private static int checkString(String s) {
        return TextUtils.isEmpty(s) ? 0 : s.length() % 10;
    }

    public static String getDeviceId(Context context) {
        String utdid = UTDevice.getUtdid(context);
        utdid = TextUtils.isEmpty(utdid) ? "" : utdid;
        Lg.d("utdid : " + utdid);
        String lanMac = getLanMac();
        String wlanMac = getWlanMac(context);
        String blueAddr = getBluetoothAddr();

        if (TextUtils.isEmpty(lanMac) && (TextUtils.isEmpty(wlanMac) || isErrorMac(wlanMac))) {
            return utdid + getUniqueId();
        }

        return lanMac + wlanMac + getUniqueId() + blueAddr;
    }

    private static boolean isErrorMac(String mac) {
        return mac.equalsIgnoreCase(error_mac1) ||
                mac.equalsIgnoreCase(error_mac2) ||
                mac.equalsIgnoreCase(error_mac3) ||
                mac.equalsIgnoreCase(error_mac4);
    }

    private final static String error_mac1 = "02:00:00:00:00:00";
    private final static String error_mac2 = "00:00:00:00:00:00";
    private final static String error_mac3 = "02-00-00-00-00-00";
    private final static String error_mac4 = "00-00-00-00-00-00";

    public static String getDeviceModel() {
        String model = Build.MODEL;
        return model == null ? "unknown" : model;
    }

    public static String getProductName() {
        String name = Build.PRODUCT;
        return name == null ? "unknown" : name;
    }

    public static boolean isSpecialModel() {
        return models.contains(getDeviceModel());
    }

    private static ArrayList<String> models = new ArrayList<>(Arrays.asList("rtd299x_tv030"));

    //利用java 的api取mac地址
    public static String useJavaInterFace() {
        String mac = null;
        try {
            Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (interfaceEnumeration.hasMoreElements()) {
                NetworkInterface networkInterface = interfaceEnumeration.nextElement();

                byte[] addr = networkInterface.getHardwareAddress();
                if (addr == null || addr.length <= 0) {
                    continue;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (byte b : addr) {
                    stringBuilder.append(String.format("%02X:", b));
                }
                if (stringBuilder.length() > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    mac = stringBuilder.toString();
                }

            }

        } catch (Exception e) {

        }
        return mac;
    }
}
