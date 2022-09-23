package wpos.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wpos.helper.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatetimeUtil {
    private static Log log = LogFactory.getLog(DatetimeUtil.class);
    private static String createDate_year;
    private static String createDate_month;
    private static String createDate_date;
    private static String createDate_hours;
    private static String createDate_minutes;
    private static String createDate_seconds;
    private static String createDate;

    public static Date toDate(String s) throws ParseException {
        JSONObject jsonObject2 = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            jsonObject2 = JSONObject.parseObject(s);
            createDate_year = jsonObject2.getString("year");
            createDate_month = jsonObject2.getString("month");
            createDate_date = jsonObject2.getString("date");
            createDate_hours = jsonObject2.getString("hours");
            createDate_minutes = jsonObject2.getString("minutes");
            createDate_seconds = jsonObject2.getString("seconds");
            createDate = (Integer.valueOf(createDate_year) + 1900) + "-" + (Integer.valueOf(createDate_month) + 1) + "-" + createDate_date + " " + createDate_hours + ":" + createDate_minutes + ":" + createDate_seconds;
            return sdf.parse(createDate);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date toDateOld(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);    //测试数据：Thu Nov 15 03:53:44 GMT+08:00 2018

        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJson(Date dt) {
        int date = dt.getDate();
        int day = dt.getDate();
        int year = dt.getYear();
        int month = dt.getMonth();
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        int seconds = dt.getSeconds();
        int timezoneOffset = dt.getTimezoneOffset();
        long time = dt.getTime();

        String str = "{\"date\":" + date + ", \"day\":" + day + ", \"hours\":" + hours + ", \"minutes\":" + minutes + ", \"month\":" +
                month + ", \"seconds\":" + seconds + ", \"time\":" + time + ", \"timezoneOffset\":" + timezoneOffset +
                ", year:" + year + "}";
        return str;
    }

    /**
     * 比较两个Date。由于JVM在不同的环境（比如eclipse和mysql）中，它的秒数可能不同，故需要特别的方法进行Date的比较。注意：Java的Date的秒数系从0到61，有闰秒
     *
     * @return true，相等；false，不相等。
     */
    public static boolean compareDate(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            log.info("两个date都为null，被认为相等。");
            return true;
        }
        if (date1 == null || date2 == null) {
            return false;
        }
        if (Math.abs(date1.getTime() / 1000 - date2.getTime() / 1000) < 2) { //正确处理闰秒
            return true;
        }

        return false;
    }

    /**
     *  判断现在是否为新的一周
     */
    public static boolean isPastWeek(Date currentDatetime, Date startDatetime, int weekNO){
        if (((currentDatetime.getTime() - startDatetime.getTime()) / (60 * 60 * 1000 * 24)) / 7 > weekNO){
            log.info("已经过了"+ weekNO +"周了");
            return true;
        }
        return false;
    }

    /**
     *  判断现在是否为新的一天
     */
    public static boolean isPastDay(Date currentDatetime, Date startDatetime, int dayNO){
        if (((currentDatetime.getTime() - startDatetime.getTime()) / (60 * 60 * 1000 * 24)) > dayNO){
            log.info("已经过了"+ dayNO +"天了");
            return true;
        }
        return false;
    }

    /**
     * 给一个日期增加n天
     */
    public static Date addDays(Date date, int n){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);

        return cal.getTime();
    }

    /**
     * 给一个日期增加n分钟
     */
    public static Date addMinutes(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, n);

        return cal.getTime();
    }

    /**
     * 给一个日期增加n秒
     */
    public static Date addSecond(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, n);

        return cal.getTime();
    }

    /** 判断当前时间是否在excuteStartDate和excuteEndDate期间。计算到秒。
     *
     * @param excuteStartDate
     *            开始时间
     * @param dateToCheck
     *            现在时间
     * @param excuteEndDate
     *            结束时间
     * @return
     * @throws ParseException
     */
    public static boolean isInTimeRange(Date excuteStartDate, Date dateToCheck, Date excuteEndDate) {
        // sbCanBeNull.append("\t{\r\n\t\t检查本商品现在是否在促销日期中······\r\n");

        try {
            // 判断日期
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
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
                // sbCanBeNull.append("\t\t\t检查日期：本商品现在不在促销日期中。\r\n\t}\r\n");
                return false;
            }

            // sbCanBeNull.append("\t\t\t检查日期：本商品现在在促销日期中。\r\n");
            return true;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**判断当前时间nowTime是否 > startDate + days之后
     */
    public static boolean isAfterDate(Date nowTime, Date startDate, int days) {
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(startDate);
        calEnd.add(Calendar.DATE, days);

        Calendar calNow = Calendar.getInstance();
        calNow.setTime(nowTime);

//		logger.info("当前时间：" + calNow + "		startDate + days：" + calEnd);
        if(calNow.compareTo(calEnd) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 返回thisDate的第2天，时间设置为：00:00:00
     * @param thisDate 不能为null
     */
    public static Date get2ndDayStart(Date thisDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(thisDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);// 24小时制
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        return calendar.getTime();
    }

    /**
     * 返回某个日期的 正负 天后的日期
     */
    public static Date getDays(Date date, int n){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);

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

    /**
     * 判断当前时间是否在excuteStartDate和excuteEndDate期间。计算到秒。
     *
     * @param excuteStartDate 开始时间
     * @param dateToCheck     现在时间
     * @param excuteEndDate   结束时间
     * @return
     * @throws ParseException
     */
    public static boolean isInDatetimeRange(Date excuteStartDate, Date dateToCheck, Date excuteEndDate) {
        // sbCanBeNull.append("\t{\r\n\t\t检查本商品现在是否在促销日期中······\r\n");

        try {
            // 判断日期
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
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
                // sbCanBeNull.append("\t\t\t检查日期：本商品现在不在促销日期中。\r\n\t}\r\n");
                return false;
            }

            // sbCanBeNull.append("\t\t\t检查日期：本商品现在在促销日期中。\r\n");
            return true;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断某天对应的星期是否包含在iWeekdayInDB中。主要用于判断促销日是否符合设定的星期
     * 促销日举例：只选择星期日：1000000(二进制)。只选择星期一：100000(二进制)。只选择星期二：10000(二进制)。选择周三、周四、周五：1110(二进制)。
     *
     * @param sbCanBeNull
     * @param nowDate
     * @param iWeekdayInDB
     * @return
     */
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

    /**
     * 判断一个时间是否在excuteStartDate和excuteEndDate期间。不计年月日
     *
     * @param startTime 开始时间，不可以为null
     * @param endTime   结束时间，不可以为null
     * @param nowTime   待检测的时间，不可以为null
     */
    public static boolean isInTime(Date startTime, Date endTime, Date nowTime) {
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
}
