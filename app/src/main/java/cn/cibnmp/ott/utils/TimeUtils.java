package cn.cibnmp.ott.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * TimeUtils
 */
public class TimeUtils {

    private static TimeZone timezone = TimeZone.getTimeZone("GMT+0:00");
    private static TimeZone timezone2 = TimeZone.getTimeZone("GMT+08:00");

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd " +
            "HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_DATE_POS = new SimpleDateFormat("yyyy.MM.dd");

    public static final SimpleDateFormat HOME_TIME_FORMAT = new SimpleDateFormat("HH:mm");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * string time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(String timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(Long.parseLong(timeInMillis)));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static String getHomeTimeInString() {
        return getTime(getCurrentTimeInLong(), HOME_TIME_FORMAT);
    }

    public static long timeString2Long(String timeInMillis) {
        return Long.parseLong(timeInMillis);
    }

    // 显示时间 HH:mm:ss
    public static String getCurTime(long systime) {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        sf.setTimeZone(timezone);
        Date curDate = new Date(systime);
        String time = sf.format(curDate);

        return time;
    }

    // 显示时间 yyyy-MM-dd
    public static String getLiveCurTime(long systime) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        sf.setTimeZone(timezone2);
        Date curDate = new Date(systime);
        String time = sf.format(curDate);
        return time;
    }

    // 自定义显示时间格式
    public static String getLiveCurTime2(SimpleDateFormat sf, long systime) {
        sf.setTimeZone(timezone2);
        Date curDate = new Date(systime);
        String time = sf.format(curDate);
        return time;
    }

    /**
     * 显示时间 yyyy.MM.dd
     *
     * @param systime
     * @return
     */
    public static String getLiveCurTime3(long systime) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);
        sf.setTimeZone(timezone2);
        Date curDate = new Date(systime);
        String time = sf.format(curDate);
        return time;
    }

    /**
     * TODO zxy 自定义
     * 显示时间 19:20 2月3日
     *
     * @param systime
     * @return
     */
    public static String getLiveCurTime4(long systime) {
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日_HH:mm", Locale.ENGLISH);
        sf.setTimeZone(timezone2);
        Date curDate = new Date(systime);
        String time = sf.format(curDate);
        return time;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间  //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String getLiveWeek(String pTime, SimpleDateFormat format) {
        String Week = "";
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                Week = "周天";
                break;
            case 2:
                Week = "周一";
                break;
            case 3:
                Week = "周二";
                break;
            case 4:
                Week = "周三";
                break;
            case 5:
                Week = "周四";
                break;
            case 6:
                Week = "周五";
                break;
            case 7:
                Week = "周六";
                break;
        }
        return Week;
    }

    // 显示时间 HH:mm:ss
    public static String getCurTime2(long systime) {
        SimpleDateFormat sf = new SimpleDateFormat("HH小时mm分", Locale.ENGLISH);
        sf.setTimeZone(timezone);
        Date curDate = new Date(systime);
        String time = sf.format(curDate);

        return time;
    }

    // wanqi,首页使用
    public static String getCurTime3(long systime) {
        SimpleDateFormat sf;
        if (systime < 60 * 1000) {
            sf = new SimpleDateFormat("ss秒");
        } else if (systime < 60 * 60 * 1000) {
            sf = new SimpleDateFormat("mm分ss秒");
        } else {
            sf = new SimpleDateFormat("HH小时mm分ss秒");
        }
        sf.setTimeZone(timezone);
        Date curDate = new Date(systime);
        String time = sf.format(curDate);
        return time;
    }

    // TODO: 18/2/8  首页样式四时长转换
    public static String getCurTime4(long systime) {
        SimpleDateFormat sf;
        if (systime < 60 * 1000) {
            sf = new SimpleDateFormat("ss:");
        } else if (systime < 60 * 60 * 1000) {
            sf = new SimpleDateFormat("mm:ss:");
        } else {
            sf = new SimpleDateFormat("HH:mm:ss:");
        }
        sf.setTimeZone(timezone);
        Date curDate = new Date(systime);
        String time = sf.format(curDate);
        return time;
    }

    public static String getTimeM(long time) {
        String str = "";
        int m = (int) (time / 60);
        str = m + "分钟";
        return str;
    }

    //yyyy-MM-dd HH:mm:ss->yyyy.MM.dd
    public static String formatTime(String time) {
        try {
            if(TextUtils.isEmpty(time))
                return "";
            return DATE_FORMAT_DATE_POS.format(DEFAULT_DATE_FORMAT.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
