package com.bx.erp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bx.erp.action.BaseAction;
import net.sf.json.JSONObject;

public class DatetimeUtil {
	private static Log logger = LogFactory.getLog(DatetimeUtil.class);

	private static String createDate_year;
	private static String createDate_month;
	private static String createDate_date;
	private static String createDate_hours;
	private static String createDate_minutes;
	private static String createDate_seconds;
	private static String createDate;

	public static Date toDate(String s) { // ...将这2个函数合为一个
		JSONObject json = null;
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		try {
			json = JSONObject.fromObject(s);
			createDate_year = json.getString("year");
			createDate_month = json.getString("month");
			createDate_date = json.getString("date");
			createDate_hours = json.getString("hours");
			createDate_minutes = json.getString("minutes");
			createDate_seconds = json.getString("seconds");
			createDate = (Integer.valueOf(createDate_year) + 1900) + "-" + Integer.valueOf(createDate_month) + 1 + "-" + createDate_date + " " + createDate_hours + ":" + createDate_minutes + ":" + createDate_seconds;
			return sdf.parse(createDate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date toDateOld(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);

		try {
			return sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 比较两个Date。由于JVM在不同的环境（比如eclipse和mysql）中，它的秒数可能不同，故需要特别的方法进行Date的比较。注意：Java的Date的秒数系从0到61，有闰秒
	 * 
	 * @return true，相等；false，不相等。 */
	public static boolean compareDate(Date date1, Date date2) {
		if (date1 == null && date2 == null) {
			logger.info("两个date都为null，被认为相等。");
			return true;
		}
		if (date1 == null || date2 == null) {
			return false;
		}
		if (Math.abs(date1.getTime() / 1000 - date2.getTime() / 1000) < 2) { // 正确处理闰秒
			return true;
		}

		return false;
	}

	/** 判断某天对应的星期是否包含在iWeekdayInDB中。主要用于判断促销日是否符合设定的星期
	 * 促销日举例：只选择星期日：1000000(二进制)。只选择星期一：100000(二进制)。只选择星期二：10000(二进制)。选择周三、周四、周五：1110(二进制)。
	 * 
	 * @param sbCanBeNull
	 * @param nowDate
	 * @param iWeekdayInDB
	 * @return */
	public static boolean isInWeekday(StringBuilder sbCanBeNull, Date nowDate, int iWeekdayInDB) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate);
		int weekdayInJava = cal.get(Calendar.DAY_OF_WEEK);
		int weekdayInJava10 = 1 << (Calendar.DAY_OF_WEEK - weekdayInJava);
		if (sbCanBeNull != null) {
			// sbCanBeNull.append("\t\t\t输入的星期是（1是周日2是周一）：" + weekdayInJava + "\t转换为十进制是：" +
			// weekdayInJava10 + "\t数据库里的星期是：" + iWeekdayInDB + "\r\n");
		}
		if ((iWeekdayInDB & weekdayInJava10) == (1 << (Calendar.DAY_OF_WEEK - weekdayInJava))) {
			return true;
		} else {
			return false;
		}
	}

	/** 判断一个时间是否在excuteStartDate和excuteEndDate期间。不计年月日
	 * 
	 * @param startTime
	 *            开始时间，不可以为null
	 * @param endTime
	 *            结束时间，不可以为null
	 * @param nowTime
	 *            待检测的时间，不可以为null */
	public static boolean isInTimeRange(Date startTime, Date endTime, Date nowTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		int start = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
		//
		cal.setTime(endTime);
		int end = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
		//
		cal.setTime(nowTime);
		int now = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);

		if (now < start || now > end) {
			return false;
		} else {
			return true;
		}
	}

	/** endTime - startTime的秒数。可能 > < = 0 */
	public static int getTimespan(Date startTime, Date endTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		int start = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
		//
		cal.setTime(endTime);
		int end = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);

		return end - start;
	}

	/** 判断一个日期是否在excuteStartDate和excuteEndDate期间。不计时分秒。
	 * 
	 * @param excuteStartDate
	 *            开始日期，不可以为null
	 * @param dateToCheck，现在日期，不可以为null
	 * @param excuteEndDate，结束日期，不可以为null
	 */
	public static boolean isInDateRange(Date excuteStartDate, Date dateToCheck, Date excuteEndDate) {
		try {
			// 判断日期
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);
			//
			Date dStart = (Date) excuteStartDate.clone();

			dStart = simpleDateFormat.parse(simpleDateFormat.format(dStart));
			long start = dStart.getTime();
			//
			Date dToCheck = (Date) dateToCheck.clone();
			dateToCheck = simpleDateFormat.parse(simpleDateFormat.format(dateToCheck));
			long now = dToCheck.getTime();
			//
			Date dEnd = (Date) excuteEndDate.clone();
			dEnd = simpleDateFormat.parse(simpleDateFormat.format(dEnd));
			long end = dEnd.getTime();
			//
			if (now < start || now > end) {
				return false;
			}
			return true;
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/** 判断一个日期是否在excuteStartDate和excuteEndDate期间。计时分秒。
	 * 
	 * @param excuteStartDate
	 *            开始日期，不可以为null
	 * @param dateToCheck，现在日期，不可以为null
	 * @param excuteEndDate，结束日期，不可以为null
	 */
	public static boolean isInDatetimeRange(Date excuteStartDate, Date dateToCheck, Date excuteEndDate) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			//
			Date dStart = (Date) excuteStartDate.clone();

			dStart = simpleDateFormat.parse(simpleDateFormat.format(dStart));
			long start = dStart.getTime();
			//
			Date dToCheck = (Date) dateToCheck.clone();
			dToCheck = simpleDateFormat.parse(simpleDateFormat.format(dToCheck));
			long now = dToCheck.getTime();
			//
			Date dEnd = (Date) excuteEndDate.clone();
			dEnd = simpleDateFormat.parse(simpleDateFormat.format(dEnd));
			long end = dEnd.getTime();
			//
			if (now < start || now > end) {
				return false;
			}
			return true;
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public static boolean hasDateIntersection(Date startDatetime, Date endDatetime, Date startDatetimeToCheck, Date endDatetimeToCheck) {
		List<Long> numList = new ArrayList<Long>();
		numList.add(startDatetime.getTime());
		numList.add(endDatetime.getTime());
		numList.add(startDatetimeToCheck.getTime());
		numList.add(endDatetimeToCheck.getTime());
		//
		Collections.sort(numList);
		//
		if (startDatetime.getTime() == numList.get(0) && (endDatetime.getTime() == numList.get(2) || endDatetime.getTime() == numList.get(3))) {
			return true;
		} else if (startDatetimeToCheck.getTime() == numList.get(0) && (endDatetimeToCheck.getTime() == numList.get(2) || endDatetimeToCheck.getTime() == numList.get(3))) {
			return true;
		}
		return false;
	}

	public static boolean hasTimeIntersection(Date excuteStartDate, Date excuteEndDate, Date CheckexcuteStartDate, Date CheckexcuteEndDate) {
		Calendar cal = Calendar.getInstance();
		//
		cal.setTime(excuteStartDate);
		int esd = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
		//
		cal.setTime(excuteEndDate);
		int eed = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
		//
		cal.setTime(CheckexcuteStartDate);
		int esdToCheck = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
		//
		cal.setTime(CheckexcuteEndDate);
		int eedToCheck = cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);

		List<Integer> numList = new ArrayList<Integer>();
		numList.add(esd);
		numList.add(eed);
		numList.add(esdToCheck);
		numList.add(eedToCheck);
		//
		Collections.sort(numList);
		//
		if (esd == numList.get(0) && (eed == numList.get(2) || eed == numList.get(3))) {
			return true;
		} else if (esdToCheck == numList.get(0) && (eedToCheck == numList.get(2) || eedToCheck == numList.get(3))) {
			return true;
		}
		return false;
	}

	/** 返回thisDate的第2天，时间设置为：time
	 * 
	 * @param thisDate
	 *            不能为null */
	public static Date getNextDayWithTime(Date dayFrom, String time) {
		SimpleDateFormat df = new SimpleDateFormat(BaseAction.TIME_FORMAT_ConfigGeneral);
		Date beginTime = null;
		try {
			beginTime = df.parse(time);
		} catch (ParseException e) {
			logger.error("日期格式转换异常" + e.toString());
			return null;
		}

		Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(beginTime);
		//
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dayFrom);
		calendar.set(Calendar.HOUR_OF_DAY, calendarFrom.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendarFrom.get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendarFrom.get(Calendar.SECOND));
		calendar.add(Calendar.DAY_OF_YEAR, 1);

		return calendar.getTime();
	}

	/** 返回thisDate的第2天，时间设置为：00:00:00
	 * 
	 * @param thisDate
	 *            不能为null */
	public static Date get2ndDayStart(Date thisDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(thisDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);// 24小时制
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_YEAR, 1);

		return calendar.getTime();
	}

	/** 判断当前时间nowTime是否在startDate之后（仅指天数，不计时分秒） */
	public static boolean isFutureDay(Date nowTime, Date startDate) {
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(startDate);
		calStart.set(Calendar.HOUR_OF_DAY, 0);// 24小时制
		calStart.set(Calendar.MINUTE, 0);
		calStart.set(Calendar.SECOND, 0);
		calStart.set(Calendar.MILLISECOND, 0);

		Calendar calNow = Calendar.getInstance();
		calNow.setTime(nowTime);
		calNow.set(Calendar.HOUR_OF_DAY, 0);// 24小时制
		calNow.set(Calendar.MINUTE, 0);
		calNow.set(Calendar.SECOND, 0);
		calNow.set(Calendar.MILLISECOND, 0);
		
		if (calNow.compareTo(calStart) > 0) {
			return true;
		}
		return false;
	}
	
	/** 判断当前时间nowTime是否在startDate + days之后 */
	public static boolean isAfterDate(Date nowTime, Date startDate, int days) {
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(startDate);
		calEnd.add(Calendar.DATE, days);

		Calendar calNow = Calendar.getInstance();
		calNow.setTime(nowTime);

		// logger.info("当前时间：" + calNow + " startDate + days：" + calEnd);
		if (calNow.compareTo(calEnd) > 0) {
			return true;
		}
		return false;
	}

	/** 返回某个日期的 正负 天后的日期 */
	public static Date getDays(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, n);

		return cal.getTime();
	}

	/** 返回某个日期的 正负 分后的日期 */
	public static Date addMinutes(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, n);

		return cal.getTime();
	}

	/** 返回某个日期的 正负 秒后的日期。 将来和上面的函数合并 //... */
	public static Date getDate(Date date, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, second);

		return cal.getTime();
	}

	/** 返回 正负 秒后的时分秒。 */
	public static String getTimeInString(Date date, int plusSecond) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, plusSecond);

		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.TIME_FORMAT_ConfigGeneral);
		return sdf.format(cal.getTime());
	}

	/** 判断date是不是一个月的i号 */
	public static boolean isOnSpecialDay(Date date, int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int specialDay = cal.get(Calendar.DATE);
		if (specialDay == i) {
			return true;
		}
		return false;
	}

	/** 判断现在是否为新的一周 */
	public static boolean isPastWeek(Date currentDatetime, Date startDatetime, int weekNO) {
		if (((currentDatetime.getTime() - startDatetime.getTime()) / (60 * 60 * 1000 * 24)) / 7 > weekNO) {
			logger.info("已经过了" + weekNO + "周了");
			return true;
		}
		return false;
	}

	/** 获取今天的第一秒 */
	public static Date getStartTime(Date date) {
		Calendar todayStart = Calendar.getInstance();
		todayStart.setTime(date);
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime();
	}

	/** 获取今天的最后一秒 */
	public static Date getEndTime(Date date) {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(date);
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime();
	}

	/** 获取传入日期所在月的第一天 */
	public static Date getFirstDayDateOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int first = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, first);
		return cal.getTime();
	}

	/** 获取传入日期所在月的最后一天 */
	public static Date getLastDayDateOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, last);
		return cal.getTime();
	}

	/** 获取传入日期的第N个月后的日期 */
	public static Date addMonths(Date date, int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, i);
		return cal.getTime();
	}

    /**
     * 把date的日期部分跟time的time部分，合并成一个日期时间
     */
    public static Date mergeDateAndTime(Date date, String time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        String targetTime = dateString.split(" ")[0].concat(" " + time);
        try {
            return formatter.parse(targetTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
