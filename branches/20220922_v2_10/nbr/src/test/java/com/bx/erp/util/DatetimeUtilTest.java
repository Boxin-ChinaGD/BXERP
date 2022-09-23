package com.bx.erp.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.test.Shared;

public class DatetimeUtilTest {
	// 20180701是星期日
	//
	private final int YEAR = 2018;
	private final int MONTH = 6; // Not 7
	private final int DAY = 15;
	private final int HOUR = 12;
	private final int MINUTE = 30;
	private final int SECOND = 30;

	private SimpleDateFormat df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);// 设置日期格式

	@Test
	public void toDateOldTest() {
		String s = "Sep 10, 2018 8:11:00 AM";

		Date date = DatetimeUtil.toDateOld(s);

		Assert.assertTrue(date != null);
	}

	@Test
	public void testDateEqual() {
		String s = "Sep 10, 2018 8:11:00 AM";

		Date d1 = DatetimeUtil.toDateOld(s);
		Date d2 = DatetimeUtil.toDateOld(s);

		Assert.assertTrue(d1 != d2);
		Assert.assertTrue(d1.equals(d2));
		Assert.assertTrue(d1.getTime() == d2.getTime());
	}

	// 促销日举例：只选择星期日：1000000(二进制)。只选择星期一：100000(二进制)。只选择星期二：10000(二进制)。选择周三、周四、周五：1110(二进制)。
	@Test
	public void testIsInWeekday() {
		// 20180701是星期日

		// 日 一 二 三 四 五 六
		// 64 32 16 8 4 2 1
		// 1 1 1 1 1 1 1
		Calendar cal = Calendar.getInstance();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		Assert.assertTrue(DatetimeUtil.isInWeekday(null, cal.getTime(), 64 + 1), "20180701是周日，数据库里促销日包括周日");
		Assert.assertTrue(DatetimeUtil.isInWeekday(null, cal.getTime(), 64 + 1), "20180701是周日，数据库里促销日包括周日周一");
		Assert.assertFalse(DatetimeUtil.isInWeekday(null, cal.getTime(), 7), "20180701是周日，数据库里促销日包括周四周五周六");
	}

	@Test
	public void testHasDateIntersection() {
		Shared.printTestMethodStartInfo();

		Calendar cal = Calendar.getInstance();
		//
		cal.set(YEAR, MONTH - 3, DAY, HOUR, MINUTE, SECOND);
		Date startDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		Date endDatetime = cal.getTime();
		cal.set(YEAR, MONTH + 1, DAY, HOUR, MINUTE, SECOND);
		Date startDatetimeCheck = cal.getTime();
		cal.set(YEAR, MONTH + 5, DAY, HOUR, MINUTE, SECOND);
		Date endDatetimeCheck = cal.getTime();

		Assert.assertFalse(DatetimeUtil.hasDateIntersection(startDatetime, endDatetime, startDatetimeCheck, endDatetimeCheck)); // 日期不重合

		cal.set(YEAR, MONTH - 1, DAY, HOUR, MINUTE, SECOND);
		startDatetimeCheck = cal.getTime();
		cal.set(YEAR, MONTH + 5, DAY, HOUR, MINUTE, SECOND);
		endDatetimeCheck = cal.getTime();
		Assert.assertTrue(DatetimeUtil.hasDateIntersection(startDatetime, endDatetime, startDatetimeCheck, endDatetimeCheck)); // 日期重合

		cal.set(YEAR, MONTH - 2, DAY, HOUR, MINUTE, SECOND);
		startDatetimeCheck = cal.getTime();
		cal.set(YEAR, MONTH - 1, DAY, HOUR, MINUTE, SECOND);
		endDatetimeCheck = cal.getTime();
		Assert.assertTrue(DatetimeUtil.hasDateIntersection(startDatetime, endDatetime, startDatetimeCheck, endDatetimeCheck)); // 日期重合

		cal.set(YEAR, MONTH - 4, DAY, HOUR, MINUTE, SECOND);
		startDatetimeCheck = cal.getTime();
		cal.set(YEAR, MONTH + 1, DAY, HOUR, MINUTE, SECOND);
		endDatetimeCheck = cal.getTime();
		Assert.assertTrue(DatetimeUtil.hasDateIntersection(startDatetime, endDatetime, startDatetimeCheck, endDatetimeCheck)); // 日期重合
	}

	@Test
	public void testHasTimeIntersection() {
		Shared.printTestMethodStartInfo();

		Calendar cal = Calendar.getInstance();
		//
		cal.set(YEAR, MONTH, DAY, HOUR - 5, MINUTE, SECOND);
		Date startDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
		Date endDatetime = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 1, MINUTE, SECOND);
		Date startDatetimeCheck = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 2, MINUTE, SECOND);
		Date endDatetimeCheck = cal.getTime();

		Assert.assertFalse(DatetimeUtil.hasTimeIntersection(startDatetime, endDatetime, startDatetimeCheck, endDatetimeCheck)); // 时段不重合

		cal.set(YEAR, MONTH, DAY, HOUR - 3, MINUTE, SECOND);
		startDatetimeCheck = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR + 2, MINUTE, SECOND);
		endDatetimeCheck = cal.getTime();
		Assert.assertTrue(DatetimeUtil.hasTimeIntersection(startDatetime, endDatetime, startDatetimeCheck, endDatetimeCheck)); // 时段重合

		cal.set(YEAR, MONTH, DAY, HOUR - 7, MINUTE, SECOND);
		startDatetimeCheck = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR - 2, MINUTE, SECOND);
		endDatetimeCheck = cal.getTime();
		Assert.assertTrue(DatetimeUtil.hasTimeIntersection(startDatetime, endDatetime, startDatetimeCheck, endDatetimeCheck)); // 时段重合

		cal.set(YEAR, MONTH, DAY, HOUR - 3, MINUTE, SECOND);
		startDatetimeCheck = cal.getTime();
		cal.set(YEAR, MONTH, DAY, HOUR - 2, MINUTE, SECOND);
		endDatetimeCheck = cal.getTime();
		Assert.assertTrue(DatetimeUtil.hasTimeIntersection(startDatetime, endDatetime, startDatetimeCheck, endDatetimeCheck)); // 时段重合
	}

	@Test
	public void testGetNextDayWithTime() {
		try {
			final int year = 1999;
			final int month = 9;
			final int day = 2;

			System.out.println("--------------------------------------------------------Case 1: 将普通日期加1天--------------------------------------------------------");
			Date d1 = df.parse(year + "-" + month + "-" + day + " 15:10:10");
			System.out.println("计算前：" + d1.toString());
			Date d2 = DatetimeUtil.getNextDayWithTime(d1, "15:10:10");
			System.out.println("计算后：" + d2.toString());
			Calendar cal = Calendar.getInstance();
			cal.setTime(d2);
			df.format(d2);
			Assert.assertEquals(cal.get(Calendar.YEAR), year);// Calendar的年从1算起
			Assert.assertEquals(cal.get(Calendar.MONTH), month - 1);// Calendar的月份从0算起
			Assert.assertEquals(cal.get(Calendar.DAY_OF_MONTH), day + 1);// Calendar的日从1算起，明天=今天+1
			Assert.assertEquals(cal.get(Calendar.HOUR_OF_DAY), 15);
			Assert.assertEquals(cal.get(Calendar.MINUTE), 10);
			Assert.assertEquals(cal.get(Calendar.SECOND), 10);

			System.out.println("--------------------------------------------------------Case 2: 将月尾加1天--------------------------------------------------------");
			final int day2 = 30; // 9月最后一天是30号
			d1 = df.parse(year + "-" + month + "-" + day2 + " 10:10:10");
			System.out.println("计算前：" + d1.toString());
			d2 = DatetimeUtil.getNextDayWithTime(d1, "10:10:10");
			System.out.println("计算后：" + d2.toString());
			cal = Calendar.getInstance();
			cal.setTime(d2);
			df.format(d2);
			Assert.assertEquals(cal.get(Calendar.YEAR), year);// Calendar的年从1算起
			Assert.assertEquals(cal.get(Calendar.MONTH), 9);// Calendar的月份从0算起。此时d2已经是10月1号
			Assert.assertEquals(cal.get(Calendar.DAY_OF_MONTH), 1);// Calendar的日从1算起，明天=今天+1。此时d2已经是10月1号
			Assert.assertEquals(cal.get(Calendar.HOUR_OF_DAY), 10);
			Assert.assertEquals(cal.get(Calendar.MINUTE), 10);
			Assert.assertEquals(cal.get(Calendar.SECOND), 10);
		} catch (ParseException e) {
			Assert.assertTrue(false, e.getMessage());
		}
	}

	@Test
	public void testGet2ndDayStart() {
		try {
			final int year = 1999;
			final int month = 9;
			final int day = 2;

			System.out.println("--------------------------------------------------------Case 1: 将普通日期加1天--------------------------------------------------------");
			Date d1 = df.parse(year + "-" + month + "-" + day + " 10:10:10");
			System.out.println("计算前：" + d1.toString());
			Date d2 = DatetimeUtil.get2ndDayStart(d1);
			System.out.println("计算后：" + d2.toString());
			Calendar cal = Calendar.getInstance();
			cal.setTime(d2);
			Assert.assertEquals(cal.get(Calendar.YEAR), year);// Calendar的年从1算起
			Assert.assertEquals(cal.get(Calendar.MONTH), month - 1);// Calendar的月份从0算起
			Assert.assertEquals(cal.get(Calendar.DAY_OF_MONTH), day + 1);// Calendar的日从1算起，明天=今天+1
			Assert.assertEquals(cal.get(Calendar.HOUR_OF_DAY), 0);
			Assert.assertEquals(cal.get(Calendar.MINUTE), 0);
			Assert.assertEquals(cal.get(Calendar.SECOND), 0);

			System.out.println("--------------------------------------------------------Case 2: 将月尾加1天--------------------------------------------------------");
			final int day2 = 30; // 9月最后一天是30号
			d1 = df.parse(year + "-" + month + "-" + day2 + " 15:10:10");
			System.out.println("计算前：" + d1.toString());
			d2 = DatetimeUtil.get2ndDayStart(d1);
			System.out.println("计算后：" + d2.toString());
			cal = Calendar.getInstance();
			cal.setTime(d2);
			Assert.assertEquals(cal.get(Calendar.YEAR), year);// Calendar的年从1算起
			Assert.assertEquals(cal.get(Calendar.MONTH), 9);// Calendar的月份从0算起。此时d2已经是10月1号
			Assert.assertEquals(cal.get(Calendar.DAY_OF_MONTH), 1);// Calendar的日从1算起，明天=今天+1。此时d2已经是10月1号
			Assert.assertEquals(cal.get(Calendar.HOUR_OF_DAY), 0);
			Assert.assertEquals(cal.get(Calendar.MINUTE), 0);
			Assert.assertEquals(cal.get(Calendar.SECOND), 0);

		} catch (ParseException e) {
			Assert.assertTrue(false, e.getMessage());
		}
	}

	@Test
	public void isInTimeRangeTest() throws Exception {
		Shared.printTestMethodStartInfo();

		SimpleDateFormat df = new SimpleDateFormat(BaseAction.TIME_FORMAT_ConfigGeneral);
		Date now = null;
		Date beginTime = null;
		Date endTime = null;
		try {
			beginTime = df.parse("01:00:00");
			endTime = df.parse("01:00:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		now = df.parse("01:00:00"); // 在设定时间内
		assertTrue(DatetimeUtil.isInTimeRange(beginTime, endTime, now) == true);

		now = df.parse("01:00:30"); // 在设定时间内
		assertTrue(DatetimeUtil.isInTimeRange(beginTime, endTime, now) == true);

		now = df.parse("02:00:30"); // 不在设定时间内
		assertTrue(DatetimeUtil.isInTimeRange(beginTime, endTime, now) == false);

		now = df.parse("00:00:30"); // 不在设定时间内
		assertTrue(DatetimeUtil.isInTimeRange(beginTime, endTime, now) == false);
	}

	@Test
	public void getTimespan() throws Exception {
		Shared.printTestMethodStartInfo();

		SimpleDateFormat df = new SimpleDateFormat(BaseAction.TIME_FORMAT_ConfigGeneral);

		Date beginTime = null;
		Date endTime = null;
		beginTime = df.parse("01:00:00");
		endTime = df.parse("01:00:59");
		assertTrue(DatetimeUtil.getTimespan(beginTime, endTime) == 59);

		beginTime = df.parse("01:00:00");
		endTime = df.parse("01:01:09");
		assertTrue(DatetimeUtil.getTimespan(beginTime, endTime) == 69);

		endTime = df.parse("01:00:00");
		beginTime = df.parse("01:01:09");
		assertTrue(DatetimeUtil.getTimespan(beginTime, endTime) == -69);
	}

	@Test
	public void isInDateRangeTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Date dateStart = df.parse("2019-01-09 10:10:10");
		Date dateToCheck = df.parse("2019-01-10 10:10:10");
		Date dateEnd = df.parse("2019-01-11 10:10:10");
		Assert.assertTrue(DatetimeUtil.isInDateRange(dateStart, dateToCheck, dateEnd));

		dateStart = df.parse("2019-01-09 10:10:10");
		dateToCheck = df.parse("2019-01-08 10:10:10");
		dateEnd = df.parse("2020-01-11 10:10:10");
		Assert.assertTrue(!DatetimeUtil.isInDateRange(dateStart, dateToCheck, dateEnd));

		dateStart = df.parse("2019-01-09 10:10:10");
		dateToCheck = df.parse("2022-01-12 10:10:10");
		dateEnd = df.parse("2022-01-11 10:10:10");
		Assert.assertTrue(!DatetimeUtil.isInDateRange(dateStart, dateToCheck, dateEnd));
	}
	
	@Test
	public void isInDatetimeRangeTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("在开始，结算时间区间");
		Date dateStart = df.parse("2019-01-09 10:10:10");
		Date dateToCheck = df.parse("2019-01-10 10:10:10");
		Date dateEnd = df.parse("2019-01-11 10:10:10");
		Assert.assertTrue(DatetimeUtil.isInDatetimeRange(dateStart, dateToCheck, dateEnd));

		Shared.caseLog("当前时间比起用时间早N天");
		dateStart = df.parse("2019-01-09 10:10:10");
		dateToCheck = df.parse("2019-01-08 10:10:10");
		dateEnd = df.parse("2020-01-11 10:10:10");
		Assert.assertTrue(!DatetimeUtil.isInDatetimeRange(dateStart, dateToCheck, dateEnd));

		Shared.caseLog("当前时间已过结算时间N天");
		dateStart = df.parse("2019-01-09 10:10:10");
		dateToCheck = df.parse("2022-01-12 10:10:10");
		dateEnd = df.parse("2022-01-11 10:10:10");
		Assert.assertTrue(!DatetimeUtil.isInDatetimeRange(dateStart, dateToCheck, dateEnd));
		
		Shared.caseLog("当前时间已到起用日期，但是还没到具体的时间");
		dateStart = df.parse("2019-01-09 10:10:10");
		dateToCheck = df.parse("2019-01-09 10:10:09");
		dateEnd = df.parse("2022-01-11 10:10:10");
		Assert.assertTrue(!DatetimeUtil.isInDatetimeRange(dateStart, dateToCheck, dateEnd));
		
		Shared.caseLog("当前时间已到结算日期，但是还没到具体的时间");
		dateStart = df.parse("2019-01-09 10:10:10");
		dateToCheck = df.parse("2022-01-11 10:10:09");
		dateEnd = df.parse("2022-01-11 10:10:10");
		Assert.assertTrue(DatetimeUtil.isInDatetimeRange(dateStart, dateToCheck, dateEnd));
		
		Shared.caseLog("当前时间已到结算日期，超过具体时间N秒");
		dateStart = df.parse("2019-01-09 10:10:10");
		dateToCheck = df.parse("2022-01-11 10:10:11");
		dateEnd = df.parse("2022-01-11 10:10:10");
		Assert.assertTrue(!DatetimeUtil.isInDatetimeRange(dateStart, dateToCheck, dateEnd));
	}

	@Test
	public void isAfterDateTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		date.add(Calendar.DATE, -8); // 现在时间8天前，未到采购订单期限
		Date dateStart1 = date.getTime();
		assertTrue(DatetimeUtil.isAfterDate(new Date(), dateStart1, 7) == true);

		date.setTime(new Date());
		date.add(Calendar.DATE, -5); // 现在时间5天前，超过采购订单期限
		Date dateStart2 = date.getTime();
		assertTrue(DatetimeUtil.isAfterDate(new Date(), dateStart2, 7) == false);

		date.setTime(new Date());
		date.add(Calendar.DATE, 1); // 现在时间1天后，超过采购订单期限
		Date dateStart3 = date.getTime();
		assertTrue(DatetimeUtil.isAfterDate(new Date(), dateStart3, 7) == false);

		date.set(2019, 8, 6, 14, 41, 36);
		Date dateStart4 = date.getTime();
		date.set(2019, 8, 6, 14, 41, 48);
		Date dateStart5 = date.getTime();
		assertTrue(DatetimeUtil.isAfterDate(dateStart5, dateStart4, 0));
	}

	@Test
	public void getDaysTest() throws ParseException {
		Date d1 = DatetimeUtil.getDays(df.parse("2018-01-01 00:00:00"), -1);
		Date d2 = DatetimeUtil.getDays(df.parse("2018-01-01 00:00:00"), 1);
		Date d3 = DatetimeUtil.getDays(df.parse("2018-01-01 00:00:00"), 0);

		// System.out.println(df.format(df.parse("2017-12-31 00:00:00")));
		// System.out.println(df.format(d1));
		assertEquals(df.format(df.parse("2017-12-31 00:00:00")), df.format(d1));
		assertEquals(df.parse("2018-01-02 00:00:00"), d2);
		assertEquals(df.parse("2018-01-01 00:00:00"), d3);
	}

	@Test
	public void getDateTest() throws ParseException {
		Date d1 = DatetimeUtil.getDate(df.parse("2018-01-01 00:00:01"), -1); // 减少一秒
		Date d2 = DatetimeUtil.getDate(df.parse("2018-01-01 00:00:00"), 1); // 增加一秒
		Date d3 = DatetimeUtil.getDate(df.parse("2018-01-01 00:00:00"), 0); // 不改变

		assertEquals(df.format(df.parse("2018-01-01 00:00:00")), df.format(d1));
		assertEquals(df.parse("2018-01-01 00:00:01"), d2);
		assertEquals(df.parse("2018-01-01 00:00:00"), d3);
	}

	@Test
	public void getFutureTimeInString() throws ParseException {
		Date date = df.parse("2018-01-01 00:00:00");
		String str = DatetimeUtil.getTimeInString(date, 18);
		assertEquals(str, "00:00:18");

		date = df.parse("2018-01-01 13:18:00");
		str = DatetimeUtil.getTimeInString(date, 16);
		assertEquals(str, "13:18:16");
	}

	@Test
	public void isOnSpecialDayTest() throws ParseException {
		// 是1号
		Date date = df.parse("2018-01-01 00:00:00");
		boolean isOnSpecialDay = DatetimeUtil.isOnSpecialDay(date, 1);
		assertTrue(isOnSpecialDay, "日期不对");
		// 不是1号
		date = df.parse("2018-01-30 00:00:00");
		isOnSpecialDay = DatetimeUtil.isOnSpecialDay(date, 1);
		assertTrue(isOnSpecialDay == false, "居然通过了！");

		// 是30号
		isOnSpecialDay = DatetimeUtil.isOnSpecialDay(date, 30);
		assertTrue(isOnSpecialDay, "居然没有通过！");
		// 不是30号
		date = df.parse("2018-01-31 00:00:00");
		isOnSpecialDay = DatetimeUtil.isOnSpecialDay(date, 30);
		assertTrue(isOnSpecialDay == false, "居然通过了！");

		// 是31号
		isOnSpecialDay = DatetimeUtil.isOnSpecialDay(date, 31);
		assertTrue(isOnSpecialDay, "居然没有通过！");
		// 不是31号
		date = df.parse("2019-08-06 00:00:00");
		isOnSpecialDay = DatetimeUtil.isOnSpecialDay(date, 31);
		assertTrue(isOnSpecialDay == false, "居然通过了！");
	}

	@Test
	public void getFirstDayDateOfMonthTest() throws ParseException {
		// 传月份的第一天，查看是否返回相同日期
		Date firstDay = df.parse("2019-01-01 00:00:00");
		assertTrue(Math.abs(DatetimeUtil.getFirstDayDateOfMonth(firstDay).getTime() - firstDay.getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 传月份的中间日子，查看是否返回当月的第一天
		assertTrue(Math.abs(DatetimeUtil.getFirstDayDateOfMonth(df.parse("2019-01-15 00:00:00")).getTime() - firstDay.getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 传月份的最后一天日子，查看是否返回当月的第一天
		assertTrue(Math.abs(DatetimeUtil.getFirstDayDateOfMonth(df.parse("2019-01-31 00:00:00")).getTime() - firstDay.getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");
	}

	@Test
	public void getLastDayDateOfMonthTest() throws ParseException {
		// 平年：
		Date lastDay = df.parse("1900-02-28 00:00:00");
		// 传月份的第一天，查看是否返回当月的最后一天
		assertTrue(Math.abs(DatetimeUtil.getLastDayDateOfMonth(df.parse("1900-02-01 00:00:00")).getTime() - lastDay.getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 传月份的中间日子，查看是否返回当月的最后一天
		assertTrue(Math.abs(DatetimeUtil.getLastDayDateOfMonth(df.parse("1900-02-15 00:00:00")).getTime() - lastDay.getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 传中月份的最后一天日子，查看是否返回当月的最后一天
		assertTrue(Math.abs(DatetimeUtil.getLastDayDateOfMonth(lastDay).getTime() - lastDay.getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 闰年：
		lastDay = df.parse("2000-02-29 00:00:00");
		// 传月份的第一天，查看是否返回当月的最后一天
		assertTrue(Math.abs(DatetimeUtil.getLastDayDateOfMonth(df.parse("2000-02-01 00:00:00")).getTime() - lastDay.getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 传月份的中间日子，查看是否返回当月的最后一天
		assertTrue(Math.abs(DatetimeUtil.getLastDayDateOfMonth(df.parse("2000-02-15 00:00:00")).getTime() - lastDay.getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 传中月份的最后一天日子，查看是否返回当月的最后一天
		assertTrue(Math.abs(DatetimeUtil.getLastDayDateOfMonth(lastDay).getTime() - lastDay.getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");
	}

	@Test
	public void addMonthsTest() throws ParseException {
		Date months = df.parse("2019-01-01 00:00:00");
		// 传入一个月份,添加N个月份，验证返回的日期是否是N个月后的月份
		assertTrue(Math.abs(DatetimeUtil.addMonths(months, 1).getTime() - df.parse("2019-02-01 00:00:00").getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 跨年的情况，传入一个年底的月份,添加N个月份。验证返回的日期是否是N个月后的月份，并且是第二年的
		assertTrue(Math.abs(DatetimeUtil.addMonths(months, 12).getTime() - df.parse("2020-01-01 00:00:00").getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 跨多个年的情况
		assertTrue(Math.abs(DatetimeUtil.addMonths(months, 24).getTime() - df.parse("2021-01-01 00:00:00").getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 传入负数的情况
		assertTrue(Math.abs(DatetimeUtil.addMonths(months, -1).getTime() - df.parse("2018-12-01 00:00:00").getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");

		// 传入负数,跨年的情况
		assertTrue(Math.abs(DatetimeUtil.addMonths(months, -12).getTime() - df.parse("2018-01-01 00:00:00").getTime()) < BaseModel.TOLERANCE, "返回的日期和预期的不一致！");
	}
	

    @Test
    public void test_mergeDateAndTime() throws ParseException {
        Shared.printTestMethodStartInfo();

        SimpleDateFormat formatter = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
        Date oneDay = formatter.parse("2013/11/12 11:10:05");
        String time = "11:11:11";
        Date dateMerged = DatetimeUtil.mergeDateAndTime(oneDay, time, BaseAction.DATETIME_FORMAT_Default3);
        Date anotherDay = formatter.parse("2013/11/12 " + time);
        Assert.assertTrue(DatetimeUtil.compareDate(dateMerged, anotherDay));

        formatter = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
        oneDay = formatter.parse("3100/10/12 08:18:55");
        time = "18:13:21";
        dateMerged = DatetimeUtil.mergeDateAndTime(oneDay, time, BaseAction.DATETIME_FORMAT_Default3);
        anotherDay = formatter.parse("3100/10/12 " + time);
        Assert.assertTrue(DatetimeUtil.compareDate(dateMerged, anotherDay));
    }
    
    @Test
    public void test_isFutureDay() throws ParseException {
    	Shared.printTestMethodStartInfo();
    	
    	// nowTime在startDate晚几天
    	SimpleDateFormat formatter = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
    	Date nowTime = formatter.parse("2013-11-13 12:10:05 003");
    	Date startDate = formatter.parse("2013-11-12 11:10:05 004");
    	Assert.assertTrue(DatetimeUtil.isFutureDay(nowTime, startDate), "nowTime不比startDate晚");
    	
    	// nowTime在startDate早几天
    	nowTime = formatter.parse("2013-11-10 11:10:05 999");
    	startDate = formatter.parse("2013-11-12 11:10:05 888");
    	Assert.assertTrue(!DatetimeUtil.isFutureDay(nowTime, startDate), "nowTime比startDate晚");
    	
    	// nowTime和startDate同一天
    	nowTime = formatter.parse("2013-11-12 11:10:05 999");
    	startDate = formatter.parse("2013-11-12 11:10:05 8");
    	Assert.assertTrue(!DatetimeUtil.isFutureDay(nowTime, startDate), "未能检查出nowTime和startDate是同一天");
    	
    	// nowTime比startDate晚一个月
    	nowTime = formatter.parse("2013-12-12 11:10:05 168");
    	startDate = formatter.parse("2013-11-12 11:10:05 668");
    	Assert.assertTrue(DatetimeUtil.isFutureDay(nowTime, startDate), "nowTime不比startDate晚");
    	
    	// nowTime比startDate晚一年
    	nowTime = formatter.parse("2014-11-10 11:10:05 998");
    	startDate = formatter.parse("2013-11-12 11:10:05 108");
    	Assert.assertTrue(DatetimeUtil.isFutureDay(nowTime, startDate), "nowTime不比startDate晚");

    	nowTime = formatter.parse("2014-11-10 11:10:05 998");
    	startDate = formatter.parse("2014-11-10 11:10:05 108");
    	Assert.assertFalse(DatetimeUtil.isFutureDay(nowTime, startDate), "nowTime比startDate晚");

    	nowTime = formatter.parse("2014-11-10 11:10:05 108");
    	startDate = formatter.parse("2014-11-10 11:10:05 108");
    	Assert.assertFalse(DatetimeUtil.isFutureDay(nowTime, startDate), "nowTime比startDate晚");
    }
}
