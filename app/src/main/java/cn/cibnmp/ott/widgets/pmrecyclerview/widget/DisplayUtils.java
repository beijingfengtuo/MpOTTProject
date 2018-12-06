package cn.cibnmp.ott.widgets.pmrecyclerview.widget;

import android.content.Context;

import cn.cibnmp.ott.App;

/**
 * Created by wanqi on 2017/2/15.
 */

public class DisplayUtils {

    public static int getAdaptValue(int value) {
        if (value == -1 || value == -2) {
            return value;
        }
        return (int) (App.ScreenScale * value / App.DesityScale + 0.5);
    }


    public static int getPxAdaptValue(int value) {
        if (value == -1 || value == -2) {
            return value;
        }
        return (int) (App.ScreenScale * value);
    }

    public static int getPxAdaptValue(int value, int size) {
        if (value == -1 || value == -2) {
            return value;
        }
        return (int) ((float) App.ScreenWidth * value / size + 0.5);
    }

    public static int getPxAdaptValueBaseOnHDpi(int value) {
        return (int) (getPxAdaptValue(value, 1920) * App.DesityScale / App.ScreenScale);
    }

    public static int getPxOnHDpi(int value) {
        return (int) getPxAdaptValue(value, 1920);
    }

    public static float getAdaptValue(float value) {
        if (value == -1 || value == -2) {
            return value;
        }
        return App.ScreenScale * value / App.DesityScale;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static int dip2px(float dpValue) {
        final float scale = App.ScreenDesity;
        return (int) (dpValue * scale + 0.5f);
    }

}
