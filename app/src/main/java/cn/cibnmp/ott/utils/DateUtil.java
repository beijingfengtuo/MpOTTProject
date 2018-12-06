package cn.cibnmp.ott.utils;

import android.content.Context;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.cibnhp.grand.R;

public class DateUtil {

	static Calendar today = Calendar.getInstance();

	/* 获取日期 */
	public static String getDay(String date) {
		String h;
		String[] day = date.split("-");
		h = day[2];
		return h;
	}

	/* 获取月份 */
	public static String getMonth(String date) {
		String m;
		String[] day = date.split("-");
		m = day[1];
		return m;
	}

	/* 获取年份 */
	public static String getYear(String date) {
		String y;
		String[] day = date.split("-");
		y = day[0];
		return y;
	}

	/* 获取当前系统时间 */
	public static String getSysDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/* 格式化日期时间 */
	public static String formatDatetime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		return sdf.format(date);
	}

	public static String formatDatetime(String date) throws ParseException {
		DateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
		Date d = fmt.parse(date);
		return d.toString();
	}

	/**
	 * 格式化日期
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(date);
	}

	public static String formatDatetime(String date, int forid) {
		if (date == null || "".equals(date.trim())) {
			return "";
		} else {
			String str = "";
			str = date.substring(0, date.indexOf("."));
			String[] array = str.split(" ");
			String[] dates = array[0].split("-");
			switch (forid) {
			case 0: // yyyy-MM-dd HH:mm:ss
				str = date.substring(0, date.indexOf("."));
				break;
			case 1: // yyyy-MM-dd
				str = date.substring(0, date.indexOf("."));
				str = str.substring(0, str.indexOf(" "));
				break;
			case 2: // yyyy年MM月dd日 HH:mm:ss
				str = dates[0] + "年" + dates[1] + "月" + dates[2] + "日 "
						+ array[1];
				break;
			case 3: // yyyy年MM月dd日 HH:mm
				str = dates[0] + "年" + dates[1] + "月" + dates[2] + "日 "
						+ array[1].substring(0, array[1].lastIndexOf(":"));
				break;
			case 4: // yyyy年MM月dd日 HH:mm:ss
				str = dates[0] + "年" + dates[1] + "月" + dates[2] + "日 ";
				break;
			default:
				break;
			}
			return str;
		}
	}

	/* 获取当前时间的毫秒 */
	public String getSysTimeMillise() {
		long i = System.currentTimeMillis();
		return String.valueOf(i);
	}

	/* 获取星期几 */
	public static String getWeek() {
		Calendar cal = Calendar.getInstance();
		int i = cal.get(Calendar.DAY_OF_WEEK);
		switch (i) {
		case 1:
			return "星期日";
		case 2:
			return "星期一";
		case 3:
			return "星期二";
		case 4:
			return "星期三";
		case 5:
			return "星期四";
		case 6:
			return "星期五";
		case 7:
			return "星期六";
		default:
			return "";
		}
	}

	/* 获取周几 */
	public static String getWeekDay() {
		Calendar cal = Calendar.getInstance();
		int i = cal.get(Calendar.DAY_OF_WEEK);
		switch (i) {
		case 1:
			return "周日";
		case 2:
			return "周一";
		case 3:
			return "周二";
		case 4:
			return "周三";
		case 5:
			return "周四";
		case 6:
			return "周五";
		case 7:
			return "周六";
		default:
			return "";
		}
	}

	/**
	 * 获取农历日期
	 * 
	 * @return
	 */
	public static String getNlDate() {
		try {

			String date = getSysDate();
			return getNlDate(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String getNlDate(String date) {
		try {
			int y = Integer.valueOf(getYear(date));
			int m = Integer.valueOf(getMonth(date));
			int d = Integer.valueOf(getDay(date));
			int[] array = LunarCalendar.solarToLunar(y, m, d);
			String nlm = convertNlMoeth(array[1]);
			String nld = convertNlDay(array[2]);
			return nlm + "月" + nld;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取今天，明天，后天的节日
	 * 
	 * @return
	 */
	public static String getHolidays() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder holiday = new StringBuilder("");
		Calendar c = Calendar.getInstance();
		//c.add(Calendar.DAY_OF_MONTH, 111); // wanqi,test
		String today = sdf.format(c.getTime()); // 今天阳历
		String nlToday = getNlDate(today); // 今天农历
		c.add(Calendar.DAY_OF_MONTH, 1);
		String tomDay = sdf.format(c.getTime()); // 明天阳历
		String nlTomDay = getNlDate(tomDay); // 明天农历
		c.add(Calendar.DAY_OF_MONTH, 1);
		String afterDay = sdf.format(c.getTime()); // 后天阳历
		String nlAfterDay = getNlDate(afterDay); // 后天农历

		String nlHoliday = "";
		String ylHoliday = "";

		nlHoliday = getNlHoliday(nlToday);
		ylHoliday = getYlHoliday(today);
		Lg.d("DateUtil", "今天公历： " + today + " , 今天农历：" + nlToday);
		if (!TextUtils.isEmpty(nlHoliday)
				|| !TextUtils.isEmpty(getYlHoliday(ylHoliday))) {
			holiday.append("今天是" + nlHoliday + "、" + ylHoliday);
			Lg.d("DateUtil", "holiday = " + holiday.toString());
			if (holiday.toString().endsWith("、")) {
				holiday.deleteCharAt(holiday.length() - 1);
			}
			if (!TextUtils.isEmpty(holiday)) {
				holiday.append("，");
			}
		}

		nlHoliday = getNlHoliday(nlTomDay);
		ylHoliday = getYlHoliday(tomDay);
		Lg.d("DateUtil", "明天公历： " + tomDay + " , 明天农历：" + nlTomDay);
		if (!TextUtils.isEmpty(nlHoliday)
				|| !TextUtils.isEmpty(getYlHoliday(ylHoliday))) {
			holiday.append("明天是" + nlHoliday + "、" + ylHoliday);
			Lg.d("DateUtil", "holiday = " + holiday.toString());
			if (holiday.toString().endsWith("、")) {
				holiday.deleteCharAt(holiday.length() - 1);
			}
			if (!TextUtils.isEmpty(holiday)) {
				holiday.append("，");
			}
		}
		

		nlHoliday = getNlHoliday(nlAfterDay);
		ylHoliday = getYlHoliday(afterDay);
		Lg.d("DateUtil", "后天公历： " + afterDay + " , 后天农历：" + nlAfterDay);
		if (!TextUtils.isEmpty(nlHoliday)
				|| !TextUtils.isEmpty(getYlHoliday(ylHoliday))) {
			holiday.append("后天是" + nlHoliday + "、" + ylHoliday);
			Lg.d("DateUtil", "holiday = " + holiday.toString());
			if (holiday.toString().endsWith("、")) {
				holiday.deleteCharAt(holiday.length() - 1);
			}
		}

		while (holiday.toString().endsWith("，")) {
			holiday.deleteCharAt(holiday.length() - 1);
		}

		Lg.d("DateUtil", "holiday = " + holiday.toString());
		return holiday.toString();
	}

	private static String getNlHoliday(String nlday) {
		if (TextUtils.isEmpty(nlday))
			return "";
		try {
			for (String[] s : nlHolidays) {
				if (s[0].equals(nlday)) {
					return s[1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String getYlHoliday(String ylday) {
		if (TextUtils.isEmpty(ylday))
			return "";
		try {
			for (String[] s : ylHolidays) {
				if (s[0].equals(ylday)) {
					return s[1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String formatCommentTime(String str) {

		Date date = parse(str, "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	public static Date parse(String str, String pattern, Locale locale) {
		if (str == null || pattern == null) {
			return null;
		}
		try {
			return new SimpleDateFormat(pattern, locale).parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 农历月份
	static String[] nlMonth = { "正", "二", "三", "四", "五", "六", "七", "八", "九",
			"十", "十一", "十二" };

	// 农历日
	static String[] nlday = { "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八",
			"初九", "初十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九",
			"二十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十" };

	// 农历节日列表
	static ArrayList<String[]> nlHolidays = new ArrayList<String[]>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4279792466765931216L;

		{
			add(new String[] { "正月初一", "春节、弥勒佛圣诞、天腊之辰" });
			add(new String[] { "正月初五", "迎财神" });
			add(new String[] { "正月初九", "玉皇上帝圣诞" });
			add(new String[] { "正月十五", "元宵节、上元天官大帝圣诞" });
			add(new String[] { "正月十九", "长春邱真人圣诞" });
			add(new String[] { "二月初三", "文昌梓潼帝君圣诞" });
			add(new String[] { "二月初六", "东华帝君圣诞" });
			add(new String[] { "二月初八", "释迦牟尼佛出家" });
			add(new String[] { "二月十五", "释迦牟尼佛涅盘、太清太上老君圣诞" });
			add(new String[] { "二月十九", "观世音菩萨圣诞" });
			add(new String[] { "二月廿一", "普贤菩萨圣诞" });
			add(new String[] { "三月初三", "真武大帝圣诞" });
			add(new String[] { "三月十六", "准提菩萨圣诞" });
			add(new String[] { "三月廿八", "东岳大帝圣诞" });
			add(new String[] { "四月初四", "文殊菩萨圣诞" });
			add(new String[] { "四月初八", "释迦牟尼佛圣诞" });
			add(new String[] { "四月十五", "佛吉祥日" });
			add(new String[] { "五月初五", "端午节" });
			add(new String[] { "五月十八", "张天师圣诞" });
			add(new String[] { "六月十九", "观世音菩萨成道日" });
			add(new String[] { "七月十三", "大势至菩萨圣诞" });
			add(new String[] { "七月十五", "中元地官大帝圣诞" });
			add(new String[] { "七月廿四", "龙树菩萨圣诞" });
			add(new String[] { "七月三十", "地藏菩萨圣诞" });
			add(new String[] { "八月十五", "中秋节" });
			add(new String[] { "八月廿二", "燃灯佛圣诞" });
			add(new String[] { "九月初九", "重阳节" });
			add(new String[] { "九月十九", "观世音菩萨出家纪念日" });
			add(new String[] { "九月三十", "药师琉璃光如来圣诞" });
			add(new String[] { "十月初五", "达摩祖师圣诞" });
			add(new String[] { "十月十五", "下元水官大帝圣诞" });
			add(new String[] { "十一月十一", "太乙救苦天尊圣诞" });
			add(new String[] { "十一月十七", "阿弥陀佛圣诞" });
			add(new String[] { "十二月初八", "释迦牟尼佛成道日、腊八节" });
			add(new String[] { "十二月廿二", "重阳祖师圣诞" });
			add(new String[] { "十二月廿三", "祭灶节" });
			add(new String[] { "十二月廿九", "华严菩萨圣诞" });
		}
	};

	// 阳历节日列表,每年都会变
	static ArrayList<String[]> ylHolidays = new ArrayList<String[]>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8430098303564375997L;

		{
			add(new String[] { "2016/04/04", "清明节" });
			add(new String[] { "2016/06/21", "上清灵宝天尊圣诞" });
			add(new String[] { "2016/12/21", "玉清元始天尊圣诞" });
		}
	};

	// 农历天干
	String[] mten = { "null", "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };

	// 农历地支
	String[] mtwelve = { "null", "子（鼠）", "丑（牛）", "寅（虎）", "卯（兔）", "辰（龙）",
			"巳（蛇）", "午（马）", "未（羊）", "申（猴）", "酉（鸡）", "戌（狗）", "亥（猪）" };

	public static String convertNlYear(String year) {
		String maxYear = "";
		for (int i = 0; i < year.length(); i++) {
			maxYear = maxYear + minCaseMax(year.substring(i, i + 1));
		}
		return maxYear;
	}

	public static String convertNlMoeth(int month) {
		String maxMonth = "";
		maxMonth = nlMonth[month - 1];
		return maxMonth;
	}

	public static String convertNlDay(int day) {
		String maxDay = "";
		maxDay = nlday[day - 1];
		return maxDay;
	}

	public static String minCaseMax(String str) {
		switch (Integer.parseInt(str)) {
		case 0:
			return "零";
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		case 7:
			return "七";
		case 8:
			return "八";
		case 9:
			return "九";

		default:
			return "null";
		}
	}

	public static String setTimeStyle(Context context, String date){
		String[] s = (date.split(" "))[0].split("-");
		String  payTime = s[0] + context.getString(R.string.year)
				+ s[1] + context.getString(R.string.month)
				+ s[2] + context.getString(R.string.day);
		return payTime;
	}

}