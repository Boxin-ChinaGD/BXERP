package com.test.bx.app.util;

import com.base.BaseAndroidTestCase;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.Promotion;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DatetimeUtilTest extends BaseAndroidTestCase {

    @BeforeClass
    public static void beforeClass() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public static void afterClass() {
        Shared.printTestClassEndInfo();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void test_isInDatetimeRange() throws Exception {
        Shared.printTestMethodStartInfo();
        //
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
        Promotion promotion = new Promotion();
        promotion.setDatetimeEnd(new Date());
        String nowDate = "2019/03/11 02:02:02";//现在时间
        boolean falg;
        //
        promotion.setDatetimeStart(simpleDateFormat.parse("2019/03/12 02:02:02"));
        falg = DatetimeUtil.isInDatetimeRange(promotion.getDatetimeStart(), simpleDateFormat.parse(nowDate), promotion.getDatetimeEnd());
        Assert.assertTrue("促销已开始，促销的开始时间比现在时间晚一天或一天以上", !falg);
        //
        promotion.setDatetimeStart(simpleDateFormat.parse("2019/03/11 03:02:02"));
        falg = DatetimeUtil.isInDatetimeRange(promotion.getDatetimeStart(), simpleDateFormat.parse(nowDate), promotion.getDatetimeEnd());
        Assert.assertTrue("促销已开始，促销的开始时间比现在时间晚1h或1h以上", !falg);
        //
        promotion.setDatetimeStart(simpleDateFormat.parse("2019/03/11 02:03:02"));
        falg = DatetimeUtil.isInDatetimeRange(promotion.getDatetimeStart(), simpleDateFormat.parse(nowDate), promotion.getDatetimeEnd());
        Assert.assertTrue("促销已开始，促销的开始时间比现在时间晚1m或1m以上", !falg);
        //
        promotion.setDatetimeStart(simpleDateFormat.parse("2019/03/11 02:02:03"));
        falg = DatetimeUtil.isInDatetimeRange(promotion.getDatetimeStart(), simpleDateFormat.parse(nowDate), promotion.getDatetimeEnd());
        Assert.assertTrue("促销已开始，促销的开始时间比现在时间晚1s或1s以上", !falg);
        //
        promotion.setDatetimeStart(simpleDateFormat.parse("2019/03/10 02:02:02"));
        falg = DatetimeUtil.isInDatetimeRange(promotion.getDatetimeStart(), simpleDateFormat.parse(nowDate), promotion.getDatetimeEnd());
        Assert.assertTrue("促销未开始，促销的开始时间比现在时间早1天或1天以上，且在结束时间以下", falg);
        //
        promotion.setDatetimeStart(simpleDateFormat.parse("2019/03/11 01:02:02"));
        falg = DatetimeUtil.isInDatetimeRange(promotion.getDatetimeStart(), simpleDateFormat.parse(nowDate), promotion.getDatetimeEnd());
        Assert.assertTrue("促销未开始，促销的开始时间比现在时间早1h或1h以上，且在结束时间以下", falg);
        //
        promotion.setDatetimeStart(simpleDateFormat.parse("2019/03/11 02:01:02"));
        falg = DatetimeUtil.isInDatetimeRange(promotion.getDatetimeStart(), simpleDateFormat.parse(nowDate), promotion.getDatetimeEnd());
        Assert.assertTrue("促销未开始，促销的开始时间比现在时间早1m或1m以上,且在结束时间以下", falg);
        //
        promotion.setDatetimeStart(simpleDateFormat.parse("2019/03/11 02:02:01"));
        falg = DatetimeUtil.isInDatetimeRange(promotion.getDatetimeStart(), simpleDateFormat.parse(nowDate), promotion.getDatetimeEnd());
        Assert.assertTrue("促销未开始，促销开始时间比现在时间早1s或1s以上,且在结束时间以下", falg);
        //
        promotion.setDatetimeStart(simpleDateFormat.parse("2019/03/11 02:02:02"));
        falg = DatetimeUtil.isInDatetimeRange(promotion.getDatetimeStart(), simpleDateFormat.parse(nowDate), promotion.getDatetimeEnd());
        Assert.assertTrue("促销未开始，促销开始时间等于现在时间，且在小于结束时间1天或以上", falg);
    }

    @Test
    public void test_mergeDateAndTime() throws ParseException {
        Shared.printTestMethodStartInfo();

        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
        Date oneDay = formatter.parse("2013/11/12 11:10:05");
        String time = "11:11:11";
        Date dateMerged = DatetimeUtil.mergeDateAndTime(oneDay, time, Constants.DATE_FORMAT_Default);
        Date anotherDay = formatter.parse("2013/11/12 " + time);
        Assert.assertTrue(DatetimeUtil.compareDate(dateMerged, anotherDay));

        formatter = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
        oneDay = formatter.parse("3100/10/12 08:18:55");
        time = "18:13:21";
        dateMerged = DatetimeUtil.mergeDateAndTime(oneDay, time, Constants.DATE_FORMAT_Default);
        anotherDay = formatter.parse("3100/10/12 " + time);
        Assert.assertTrue(DatetimeUtil.compareDate(dateMerged, anotherDay));
    }

    // 促销日举例：只选择星期日：1000000(二进制)。只选择星期一：100000(二进制)。只选择星期二：10000(二进制)。选择周三、周四、周五：1110(二进制)。
    @Test
    public void testIsInWeekday() throws ParseException {
        // 20180701是星期日

        // 日 一 二 三 四 五 六
        // 64 32 16 8 4 2 1
        // 1 1 1 1 1 1 1
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_Default);
        Date oneDay = formatter.parse("2018/07/01 11:10:05");
        Assert.assertTrue("20180701是周日，数据库里促销日包括周日", DatetimeUtil.isInWeekday(null, oneDay, 64 + 1));
        Assert.assertTrue("20180701是周日，数据库里促销日包括周日周一", DatetimeUtil.isInWeekday(null, oneDay, 64 + 1));
        Assert.assertFalse("20180701是周日，数据库里促销日包括周四周五周六", DatetimeUtil.isInWeekday(null, oneDay, 7));
    }
}
