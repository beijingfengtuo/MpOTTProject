package cn.cibnmp.ott.utils;

import android.content.Context;

import cn.cibnmp.ott.App;

public class DisplayUtils {

    public static int getAdaptValue(int value) {
        if (value == -1 || App.ScreenWidth == 720) {
            return value;
        }
        return (int) (App.ScreenScale * value / App.DesityScale);
    }

    public static int getValue(int value) {
        if (value == -1 || App.ScreenWidth == 720) {
            return value;
        }
        return (int) (App.ScreenScale * value);
    }

    public static int getScaleValue(int value) {
        if (value == -1 || App.ScreenWidth == 720) {
            return value;
        }
        return (int) (App.ScreenScale * value / App.DesityScale);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 此方法使 source 关键字变色
     *
     * @param source  原string
     * @param keyWord 关键字
     * @return 结果string
     */
    public static String changeTextColor(String source, String keyWord) {
        if (source == null || keyWord == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char[] sourch = source.toCharArray();
        char[] keyWordch = keyWord.toCharArray();
        for (int i = 0; i < sourch.length; i++) {
            if (isContain(sourch[i], keyWordch)) {
                String s = "<font color='#e50009'>" + sourch[i] + "</font>";
                sb.append(s);
            } else {
                sb.append(sourch[i]);
            }
        }
        return sb.toString();
    }

    /**
     * char数组中是否包含某char
     *
     * @param c
     * @param key
     * @return
     */
    public static boolean isContain(char c, char[] key) {
        for (char ch : key) {
            if (ch == c) {
                return true;
            }
        }
        return false;
    }

//    //00000000000000000000000000000000000
//    public static int getPxOnHDpi(int value) {
//        return (int) getPxAdaptValue(value, 720);
//    }
//
//    public static int getPxAdaptValue(int value, int size) {
//        if (value == -1 || value == -2) {
//            return value;
//        }
//        return (int) ((float) App.ScreenWidth * value / size + 0.5);
//    }

}
