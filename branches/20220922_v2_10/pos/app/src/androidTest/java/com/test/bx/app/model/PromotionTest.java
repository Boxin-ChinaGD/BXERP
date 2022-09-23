package com.test.bx.app.model;

import com.base.BaseAndroidTestCase;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.Promotion;
import com.bx.erp.promotion.BasePromotion;
import com.bx.erp.utils.DatetimeUtil;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PromotionTest extends BaseAndroidTestCase {

    public static final String PAGE_SIZE = "10";
    public static final String PAGE_StartIndex = "1";
    public static final String PAGE_SIZE_Infinite = "100000000";

    @BeforeClass
    public void setUp() {
        Shared.printTestClassStartInfo();
    }

    @AfterClass
    public void tearDown() {
        Shared.printTestClassEndInfo();
    }

    @Test
    public void test_a_checkUpdate() throws ParseException {
        Shared.printTestMethodStartInfo();
        Promotion p = new Promotion();

        // 设除了starttime以外的正常初始值
        String end = "2039-3-16 10:10:20";
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default6);
        Date endDate = sdf.parse(end);
        p.setID(1l);
        p.setDatetimeEnd(endDate);
        p.setExcecutionThreshold(1);
        p.setExcecutionAmount(1);
        p.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
        p.setName("checkUpdate");
        p.setScope(BasePromotion.EnumPromotionScope.EPS_AllCommodities.getIndex());
        p.setStaff(1);
        String err = "";

        // 活动开始时间必须至少从明天开始！
        String start = "2009-3-14 10:10:20";
        Date startDate = sdf.parse(start);
        p.setDatetimeStart(startDate);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_startDatetime);
        //
        p.setDatetimeStart(new Date());
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_startDatetime);
        //
        p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 1));
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // 开始时间必须在结束时间之前！
        start = "3320-3-13 0:0:0";
        startDate = sdf.parse(start);
        p.setDatetimeStart(startDate);
        end = "3320-3-13 0:0:0";
        endDate = sdf.parse(end);
        p.setDatetimeEnd(endDate);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_datetimeStart_datetimeEnd);

        p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 1));
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // 阀值不能小于等于0
        p.setExcecutionThreshold(0);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);
        p.setExcecutionThreshold(-10);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);

        p.setExcecutionThreshold(1);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        p.setExcecutionThreshold(11);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // 阀值需大于满减金额！
        p.setExcecutionThreshold(11);
        p.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
        p.setExcecutionAmount(11);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //
        p.setExcecutionAmount(12);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount_excecutionThreshold);
        //
        p.setExcecutionAmount(-1);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
        //
        p.setExcecutionAmount(0);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
        //
        p.setExcecutionAmount(10);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        p.setType(Promotion.EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
        p.setExcecutionDiscount(1.01f);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
        //
        p.setExcecutionDiscount(1);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //
        p.setExcecutionDiscount(0f);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
        //
        p.setExcecutionDiscount(-0.01f);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
        //
        p.setExcecutionDiscount(-1);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
        //
        p.setExcecutionDiscount(0.99f);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //
        p.setExcecutionDiscount(0.01f);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // type必须是0或1
        p.setType(BaseSQLiteBO.INVALID_Type);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_type);
        //
        p.setType(2);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_type);
        //
        p.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // name不为null且长度不大于32
        p.setName(null);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
        //
        p.setName("阿斯顿122309阿萨德请问才杂事0说的insert4-1IKEA132出去玩313认识的罚点球2312");
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
        //
        p.setName("");
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
        //
        p.setName("name不为null");
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // scope必须是0或1
        p.setScope(-1);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_scope);
        //
        p.setScope(2);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_scope);
        //
        p.setScope(BasePromotion.EnumPromotionScope.EPS_AllCommodities.getIndex());
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // status必须是0或1
        p.setStatus(-1);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_status);
        //
        p.setStatus(2);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_status);
        //
        p.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        // ID必须大于0
        p.setID(0l);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
        p.setID(1l);
        // staffID必须大于0
        p.setStaff(0);
        err = p.checkUpdate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_staff);
        p.setStaff(1);
    }

    @Test
    public void test_b_checkCreate() throws ParseException {
        Shared.printTestMethodStartInfo();
        Promotion p = new Promotion();

        // 设除了starttime以外的正常初始值
        String end = "2039-3-16 10:10:20";
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default6);
        Date endDate = sdf.parse(end);
        p.setDatetimeEnd(endDate);
        p.setExcecutionThreshold(1);
        p.setExcecutionAmount(1);
        p.setName("case1");
        p.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
        p.setScope(BasePromotion.EnumPromotionScope.EPS_AllCommodities.getIndex());
        p.setStaff(1);
        String err = "";

        // 活动开始时间必须至少从明天开始！
        String start = "2009-3-14 10:10:20";
        Date startDate = sdf.parse(start);
        p.setDatetimeStart(startDate);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_startDatetime);
        //
        p.setDatetimeStart(new Date());
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_startDatetime);
        //
        p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 1));
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // 开始时间必须在结束时间之前！
        start = "3320-3-13 0:0:0";
        startDate = sdf.parse(start);
        p.setDatetimeStart(startDate);
        end = "3320-3-13 0:0:0";
        endDate = sdf.parse(end);
        p.setDatetimeEnd(endDate);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_datetimeStart_datetimeEnd);

        p.setDatetimeStart(DatetimeUtil.getDays(new Date(), 1));
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // 阀值不能小于等于0
        p.setExcecutionThreshold(0);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);
        p.setExcecutionThreshold(-10);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);

        p.setExcecutionThreshold(1);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        p.setExcecutionThreshold(11);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // 阀值需大于满减金额！
        p.setExcecutionThreshold(11);
        p.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
        p.setExcecutionAmount(11);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //
        p.setExcecutionAmount(12);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount_excecutionThreshold);
        //
        p.setExcecutionAmount(-1);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
        //
        p.setExcecutionAmount(0);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
        //
        p.setExcecutionAmount(10);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        p.setType(Promotion.EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
        p.setExcecutionDiscount(1.01f);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
        //
        p.setExcecutionDiscount(1);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //
        p.setExcecutionDiscount(-0.01f);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
        //
        p.setExcecutionDiscount(-1);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
        //
        p.setExcecutionDiscount(0.99f);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        //
        p.setExcecutionDiscount(0f);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionDiscount);
        //
        p.setExcecutionDiscount(0.01f);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // type必须是0或1
        p.setType(BaseSQLiteBO.INVALID_Type);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_type);
        //
        p.setType(2);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_type);
        //
        p.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // name不为null且长度不大于32
        p.setName(null);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
        //
        p.setName("阿斯顿122309阿萨德请问才杂事0说的insert4-1IKEA132出去玩313认识的罚点球2312");
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
        //
        p.setName("");
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_name);
        //
        p.setName("name不为null");
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // scope必须是0或1
        p.setScope(-1);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_scope);
        //
        p.setScope(2);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_scope);
        //
        p.setScope(BasePromotion.EnumPromotionScope.EPS_AllCommodities.getIndex());
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");

        // status必须是0或1
        p.setStatus(-1);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_status);
        //
        p.setStatus(2);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_status);
        //
        p.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        // staffID必须大于0
        p.setStaff(0);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_staff);
        p.setStaff(1);
        // 满减阀值大于满减金额 允许的
        p.setType(Promotion.EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
        p.setExcecutionThreshold(100);
        p.setExcecutionAmount(10);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        // 满减阀值等于满减金额 允许的
        p.setExcecutionThreshold(100);
        p.setExcecutionAmount(100);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, "");
        // 满减阀值小于满减金额 不允许的
        p.setExcecutionThreshold(10);
        p.setExcecutionAmount(20);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount_excecutionThreshold);
        // 满减阀值大于满减金额但是满减阀值超过最大值 不允许的
        p.setExcecutionThreshold(10001);
        p.setExcecutionAmount(10);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);
        // 满减阀值等于满减金额但是满减阀值超过最大值 不允许的
        p.setExcecutionThreshold(10001);
        p.setExcecutionAmount(10001);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);
        // 满减阀值小于满减金额但是满减金额超过最大值 不允许的
        p.setExcecutionThreshold(10);
        p.setExcecutionAmount(10001);
        err = p.checkCreate(BaseSQLiteBO.INVALID_CASE_ID);
        Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
    }

    @Test
    public void test_c_checkDelete() {
        Shared.printTestMethodStartInfo();

        // ID必须大于0
        Promotion p = new Promotion();
        p.setID(0l);
        assertEquals(p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        p.setID(-1l);
        assertEquals(p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        p.setID(1l);
        assertEquals(p.checkDelete(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_d_checkRetrieveN() {
        Promotion p = new Promotion();

        // 检查status
        p.setStatus(-2);
        assertEquals(p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_statusForRetrieveN);
        //
        p.setStatus(3);
        assertEquals(p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_statusForRetrieveN);
        //
        p.setStatus(BaseSQLiteBO.INVALID_STATUS);
        assertEquals(p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查string1
        p.setQueryKeyword("0123456789 0123456789 0123456789 0123456789");
        assertEquals(p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_nameForRetrieveN);
        //
        p.setQueryKeyword(null);
        assertEquals(p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        //
        p.setQueryKeyword(String.valueOf(System.currentTimeMillis() % 10));
        assertEquals(p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        //
        p.setInt1(Promotion.ACTIVE);
        assertEquals(p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
        // 检查page indx
        p.setPageIndex("-1");
        p.setPageSize(PAGE_SIZE);
        String err = p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID);
        assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
        //
        p.setPageIndex(PAGE_StartIndex);
        p.setPageSize("-1");
        assertEquals(p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
        //
        p.setPageIndex(PAGE_StartIndex);
        p.setPageSize(PAGE_SIZE);
        assertEquals(p.checkRetrieveN(BaseSQLiteBO.INVALID_CASE_ID), "");
    }

    @Test
    public void test_e_checkRetrieve1() {
        Shared.printTestMethodStartInfo();

        // ID必须大于0
        Promotion p = new Promotion();
        p.setID(0l);
        assertEquals(p.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        p.setID(-1l);
        assertEquals(p.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
        //
        p.setID(1l);
        assertEquals(p.checkRetrieve1(BaseSQLiteBO.INVALID_CASE_ID), "");
    }
}
