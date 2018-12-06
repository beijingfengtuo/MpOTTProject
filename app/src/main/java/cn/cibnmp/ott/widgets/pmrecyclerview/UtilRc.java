package cn.cibnmp.ott.widgets.pmrecyclerview;

import android.view.View;

import cn.cibnhp.grand.R;
import cn.cibnmp.ott.utils.DisplayUtils;

/**
 * Created by wanqi on 2017/2/20.
 */

public class UtilRc {

    private static final String TAG = "Util";

    public static int getPlaceHolder(View v) {
        if (v == null)
            return R.drawable.placeholder_04;
        else return getPlaceHolderImage(v.getWidth(), v.getHeight());
    }

    public static int getPlaceHolderImage(double c, double r) {
        return getPlaceHolderImage(getValue(c), getValue(r));
    }

    public static int getPlaceHolderImage2(double c, double r) {
        return getPlaceHolderImage(getValue(c), getValue(r) - DisplayUtils.getValue(55));
    }

    public static int getValue(double v) {
        double dv = ((double) DisplayUtils.getValue(1716)) / 120 * v - DisplayUtils.getValue(24);
        return (int) dv;

    }

    public static int getPlaceHolderImage(int w, int h) {

        if (w < h) {
            return R.drawable.placeholder_260_346;
        } else if (w > h) {
            return R.drawable.placeholder_402_221;
        }
//        if (Math.abs(DisplayUtils.getValue(828) - w) < 10
//                && Math.abs(DisplayUtils.getValue(466) - h) < 10) {
//            return R.drawable.placeholder_828_466;
//        } else if (Math.abs(DisplayUtils.getValue(544) - w) < 10
//                && Math.abs(DisplayUtils.getValue(306) - h) < 10) {
//            return R.drawable.placeholder_544_306;
//        } else if (Math.abs(DisplayUtils.getValue(454) - w) < 10
//                && Math.abs(DisplayUtils.getValue(256) - h) < 10) {
//            return R.drawable.placeholder_454_256;
//        } else if (Math.abs(DisplayUtils.getValue(402) - w) < 10
//                && Math.abs(DisplayUtils.getValue(221) - h) < 10) {
//            return R.drawable.placeholder_402_221;
//        } else if (Math.abs(DisplayUtils.getValue(260) - w) < 10
//                && Math.abs(DisplayUtils.getValue(346) - h) < 10) {
//            return R.drawable.placeholder_260_346;
//        } else if (Math.abs(DisplayUtils.getValue(206) - w) < 10
//                && Math.abs(DisplayUtils.getValue(173) - h) < 10) {
//            return R.drawable.placeholder_206_173;
//        } else if (Math.abs(DisplayUtils.getValue(319) - w) < 10
//                && Math.abs(DisplayUtils.getValue(421) - h) < 10) {
//            return R.drawable.placeholder_319_421;
//        }

        return R.drawable.placeholder_454_256;
    }

}
