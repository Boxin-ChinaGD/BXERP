package com.test.bx.app;

import com.base.BaseAndroidTestCase;
import com.bx.erp.model.CommodityType;
import com.bx.erp.utils.FieldFormat;
import com.bx.erp.utils.Shared;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FieldFormatTest extends BaseAndroidTestCase {

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
    public void test_checkIntegerID() {
        Shared.printTestMethodStartInfo();
        // 格式正确
        Assert.assertTrue(FieldFormat.checkID(1));
        Assert.assertTrue(FieldFormat.checkID(100));
        Assert.assertTrue(FieldFormat.checkID(1000));
        Assert.assertTrue(FieldFormat.checkID(10000));
        // 格式错误
        Assert.assertFalse(FieldFormat.checkID(0));
        Assert.assertFalse(FieldFormat.checkID(-1));
        Assert.assertFalse(FieldFormat.checkID(-155));
    }

    @Test
    public void test_checkLongID() {
        Shared.printTestMethodStartInfo();
        // 格式正确
        Assert.assertTrue(FieldFormat.checkID(Long.valueOf(1)));
        Assert.assertTrue(FieldFormat.checkID(Long.valueOf(100)));
        Assert.assertTrue(FieldFormat.checkID(Long.valueOf(1000)));
        Assert.assertTrue(FieldFormat.checkID(Long.valueOf(10000)));
        // 格式错误
        Long id = null;
        Assert.assertFalse(FieldFormat.checkID(id));
        Assert.assertFalse(FieldFormat.checkID(Long.valueOf(0)));
        Assert.assertFalse(FieldFormat.checkID(Long.valueOf(-1)));
        Assert.assertFalse(FieldFormat.checkID(Long.valueOf(-155)));
    }

    @Test
    public void test_checkRetailTradeSN_For_checkCreate() {
        Shared.printTestMethodStartInfo();
        // checkCreate
        // 设置F_SN为26位的退货单，结尾为_1，且格式正确，结果通过。
        Assert.assertTrue(FieldFormat.checkRetailTradeSN(false,"LS2019010101010100011234_1"));
        // 设置F_SN为24位普通零售单，且格式正确，结果通过。
        Assert.assertTrue(FieldFormat.checkRetailTradeSN(false,"LS2019010101010100011234"));

        // 设置F_SN的长度小于24，结果不通过。
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,"LS1234567"));
        // 设置F_SN的长度等于25，结果不通过。
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,"LS12345678912345678912345"));
        // 设置F_SN的长度超过26，结果不通过。
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,"LS1234567891234567891234567891"));
        // 设置F_SN不是LS开头，结果不通过。
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,"1234567890123456789012"));
        // 设置F_SN包含中文，结果不通过。
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,"LS123456星巴克1234567891234"));
        // 设置F_SN包含其他英文字符，结果不通过。
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,"LS123456abc1234567891234"));
        // 设置F_SN包含空格，结果不通过
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,"LS123456   1234567891234"));
        // 设置F_SN为null，结果不通过
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,null));
        // 设置F_SN为空串，结果不通过
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,""));
        // 设置F_SN包含特殊字符，结果不通过
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,"1234567891234"));
        // 设置F_SN的日期比当前时间晚，结果不通过。
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(false,"LS2021010101010100011234"));
    }


    @Test
    public void test_checkRetailTradeQueryKeyword() {
        Shared.printTestMethodStartInfo();
        // 从app发出的请求，其queryKeyword代表SN，只根据SN来查询
        // 空串
        Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN(""));
        // 10-26位且以ls开头
        Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("ls201907130000100002"));
        // 10-26位且以LS开头
        Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("LS201907130000100002"));
        // 10-26位且不以LS开头
        Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("20190713000010000200"));
        // 26位且以LS开头，以_1结尾
        Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("LS2019071300001000021234_1"));
        // 26位且以LS开头，不以_1结尾
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("LS2019071300001000021234_2"));
        // 包含不是LS的英文字符
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("201902020101abc112348"));
        // 包含LS的英文字符但不在开头
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("201902020101LS112348"));
        // 中文
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("星巴克"));
        // 特殊字符
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("！@#￥（&（%"));
        // null
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN(null));
        // 长度小于10
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("20190"));
        // 超过26位
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("LS20190202010101000112348888"));
        // 首尾有空格
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN(" 12345678123456781 "));
        //
        // 空串
        Assert.assertTrue(FieldFormat.checkRetailTradeSN(true,""));
        // 10-26位且以ls开头
        Assert.assertTrue(FieldFormat.checkRetailTradeSN(true,"ls201907130000100002"));
        // 10-26位且以LS开头
        Assert.assertTrue(FieldFormat.checkRetailTradeSN(true,"LS201907130000100002"));
        // 10-26位且不以LS开头
        Assert.assertTrue(FieldFormat.checkRetailTradeSN(true,"20190713000010000200"));
        // 26位且以LS开头，以_1结尾
        Assert.assertTrue(FieldFormat.checkRetailTradeSN(true,"LS2019071300001000021234_1"));
        // 26位且以LS开头，不以_1结尾
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(true,"LS2019071300001000021234_2"));
        // 包含不是LS的英文字符
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(true,"201902020101abc112348"));
        // 包含LS的英文字符但不在开头
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(true,"201902020101LS112348"));
        // 中文
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(true,"星巴克"));
        // 特殊字符
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(true,"！@#￥（&（%"));
        // null
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(true,null));
        // 长度小于10
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(true,"20190"));
        // 超过26位
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(true,"LS20190202010101000112348888"));
        // 首尾有空格
        Assert.assertFalse(FieldFormat.checkRetailTradeSN(true," 12345678123456781 "));
    }

    @Test
    public void test_checkPaging() {
        Assert.assertTrue(FieldFormat.checkPaging(1,1));
        Assert.assertTrue(FieldFormat.checkPaging(3,100));
        Assert.assertTrue(FieldFormat.checkPaging(200,5));
        Assert.assertTrue(FieldFormat.checkPaging(1000,300));
        //
        Assert.assertFalse(FieldFormat.checkPaging(0,0));
        Assert.assertFalse(FieldFormat.checkPaging(3,0));
        Assert.assertFalse(FieldFormat.checkPaging(0,5));
        Assert.assertFalse(FieldFormat.checkPaging(-1,0));
        Assert.assertFalse(FieldFormat.checkPaging(3,-1));
        Assert.assertFalse(FieldFormat.checkPaging(-1,5));
        Assert.assertFalse(FieldFormat.checkPaging(-1000,-56465));
    }

    @Test
    public void test_checkDate() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkDate("2016-12-6 1:01:01"));
        Assert.assertTrue(FieldFormat.checkDate("2016/12/6 1:01:01"));
        //
        Assert.assertFalse(FieldFormat.checkDate("2016/12/6"));
        Assert.assertFalse(FieldFormat.checkDate("2016.12.6"));
        Assert.assertFalse(FieldFormat.checkDate("2016-12-6"));
        Assert.assertFalse(FieldFormat.checkDate("2016.12.6 1:01:01"));
        Assert.assertFalse(FieldFormat.checkDate(null));
        Assert.assertFalse(FieldFormat.checkDate(""));
        Assert.assertFalse(FieldFormat.checkDate("1"));
        Assert.assertFalse(FieldFormat.checkDate("1213"));
        Assert.assertFalse(FieldFormat.checkDate("a"));
        Assert.assertFalse(FieldFormat.checkDate("agfdsg"));
        Assert.assertFalse(FieldFormat.checkDate("12ag32fdsg23"));
    }

    @Test
    public void test_checkIfMultiPackagingRefCommodity() {
        Shared.printTestMethodStartInfo();
        // 多包装
        Assert.assertTrue(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex(), 2, 2));
        Assert.assertTrue(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex(), 1000000000, 1000000000));
        Assert.assertTrue(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex(), 'a', 5));
        Assert.assertTrue(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex(), 5, 'a'));
        //
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex(), 0, 0));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex(), 0, 1));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex(), -5, 5));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex(), 5, -5));
        // 单品
        Assert.assertTrue(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex(), 0, 0));
        //
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex(), 2, 2));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex(), 1000000000, 1000000000));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex(), 'a', 5));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex(), 5, 'a'));
        // 组合
        Assert.assertTrue(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex(), 0, 0));
        //
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex(), 2, 2));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex(), 1000000000, 1000000000));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex(), 'a', 5));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex(), 5, 'a'));
        // 服务
        Assert.assertTrue(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex(), 0, 0));
        //
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex(), 2, 2));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex(), 1000000000, 1000000000));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex(), 'a', 5));
        Assert.assertFalse(FieldFormat.checkRefCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex(), 5, 'a'));
    }

    @Test
    public void test_checkProviderAddress() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkProviderAddress("973456742923"));
        Assert.assertTrue(FieldFormat.checkProviderAddress("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkProviderAddress("哈哈哈哈哈哈哈哈哈"));
        Assert.assertTrue(FieldFormat.checkProviderAddress("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkProviderAddress("f93y你好hw中国2923"));
        Assert.assertTrue(FieldFormat.checkProviderAddress("f93y博昕科技hw中国2923"));
        Assert.assertTrue(FieldFormat.checkProviderAddress(",./2923"));
        Assert.assertTrue(FieldFormat.checkProviderAddress("f93yh#w 2923"));
        Assert.assertTrue(FieldFormat.checkProviderAddress("f"));
        Assert.assertTrue(FieldFormat.checkProviderAddress("1"));
        Assert.assertTrue(FieldFormat.checkProviderAddress("啊"));

        Assert.assertFalse(FieldFormat.checkProviderAddress(" a"));
        Assert.assertFalse(FieldFormat.checkProviderAddress(" 441522199409258X1 "));
        Assert.assertFalse(FieldFormat.checkProviderAddress(" "));
        Assert.assertFalse(FieldFormat.checkProviderAddress(null));
    }

    @Test
    public void test_checkCommodityName() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkCommodityName("973456742923"));
        Assert.assertTrue(FieldFormat.checkCommodityName("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkCommodityName("哈哈哈哈哈哈哈哈哈"));
        Assert.assertTrue(FieldFormat.checkCommodityName("012345678901234567890123456789XX"));
        Assert.assertTrue(FieldFormat.checkCommodityName("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkCommodityName("f93y你好hw中国2923"));
        Assert.assertTrue(FieldFormat.checkCommodityName("f93y博昕科技hw中国2923"));
        Assert.assertTrue(FieldFormat.checkCommodityName("f93y博昕科技hw 中国2923"));
        Assert.assertTrue(FieldFormat.checkCommodityName("f"));
        Assert.assertTrue(FieldFormat.checkCommodityName("1"));
        Assert.assertTrue(FieldFormat.checkCommodityName("啊"));
        Assert.assertTrue(FieldFormat.checkCommodityName("fdg( )（c）-—_啊"));

        Assert.assertTrue(FieldFormat.checkCommodityName("*“”、$#/"));

        Assert.assertFalse(FieldFormat.checkCommodityName(null));
        Assert.assertFalse(FieldFormat.checkCommodityName(""));
        Assert.assertFalse(FieldFormat.checkCommodityName("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkCommodityName(",./2923"));
        Assert.assertFalse(FieldFormat.checkCommodityName(" f"));
        Assert.assertFalse(FieldFormat.checkCommodityName(" "));
        Assert.assertFalse(FieldFormat.checkCommodityName("012345678901234567890123456789XXW"));
    }

    @Test
    public void test_checkInventorySheetRemark() {
        Assert.assertTrue(FieldFormat.checkInventorySheetRemark("你好"));
        Assert.assertTrue(FieldFormat.checkInventorySheetRemark("hello"));
        Assert.assertTrue(FieldFormat.checkInventorySheetRemark("123456"));
        Assert.assertTrue(FieldFormat.checkInventorySheetRemark("你好hello123456"));
        //
        Assert.assertFalse(FieldFormat.checkInventorySheetRemark(""));
        Assert.assertFalse(FieldFormat.checkInventorySheetRemark("_"));
        Assert.assertFalse(FieldFormat.checkInventorySheetRemark("-"));
        Assert.assertFalse(FieldFormat.checkInventorySheetRemark(null));
        Assert.assertFalse(FieldFormat.checkInventorySheetRemark("@"));
        Assert.assertFalse(FieldFormat.checkInventorySheetRemark("@$%#%$^$&&"));
        Assert.assertFalse(FieldFormat.checkInventorySheetRemark("%123"));
        Assert.assertFalse(FieldFormat.checkInventorySheetRemark("%hello"));
        Assert.assertFalse(FieldFormat.checkInventorySheetRemark("%你好"));
        Assert.assertFalse(FieldFormat.checkInventorySheetRemark("%你好hello123456"));
    }

    @Test
    public void test_checkHumanName() {// 只允许中文和英文，(0, 12]位
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkHumanName("中华人民中华人民中华人民"));
        Assert.assertTrue(FieldFormat.checkHumanName("许鹏"));
        Assert.assertTrue(FieldFormat.checkHumanName("TZQ"));
        Assert.assertTrue(FieldFormat.checkHumanName("谭志强TZQ"));
        Assert.assertTrue(FieldFormat.checkHumanName("TZQ谭志强"));
        Assert.assertTrue(FieldFormat.checkHumanName("abcdabcdabcd"));

        Assert.assertFalse(FieldFormat.checkHumanName(" "));
        Assert.assertFalse(FieldFormat.checkHumanName(" A"));
        Assert.assertFalse(FieldFormat.checkHumanName("A "));
        Assert.assertFalse(FieldFormat.checkHumanName(" 中"));
        Assert.assertFalse(FieldFormat.checkHumanName("中 "));
        Assert.assertFalse(FieldFormat.checkHumanName(" 1"));
        Assert.assertFalse(FieldFormat.checkHumanName("123456789012"));
        Assert.assertFalse(FieldFormat.checkHumanName("1234567890中国"));
        Assert.assertFalse(FieldFormat.checkHumanName("abcdabcdab12"));
        Assert.assertFalse(FieldFormat.checkHumanName("中华人民ABCD中华38"));
        Assert.assertFalse(FieldFormat.checkHumanName("中华人民中华人民中华38"));

        Assert.assertFalse(FieldFormat.checkHumanName("12345678901-"));
        Assert.assertFalse(FieldFormat.checkHumanName("12345678901_"));
        Assert.assertFalse(FieldFormat.checkHumanName("1234567890123"));
        Assert.assertFalse(FieldFormat.checkHumanName("中华人民中华人民中华人民1"));
        Assert.assertFalse(FieldFormat.checkHumanName(""));
        Assert.assertFalse(FieldFormat.checkHumanName(null));
    }

    @Test
    public void test_checkRoleName() {
        Assert.assertTrue(FieldFormat.checkRoleName("你好"));
        Assert.assertTrue(FieldFormat.checkRoleName("hello"));
        Assert.assertTrue(FieldFormat.checkRoleName("123456"));
        Assert.assertTrue(FieldFormat.checkRoleName("你好hello123456"));
        //
        Assert.assertFalse(FieldFormat.checkRoleName(""));
        Assert.assertFalse(FieldFormat.checkRoleName("_"));
        Assert.assertFalse(FieldFormat.checkRoleName("-"));
        Assert.assertFalse(FieldFormat.checkRoleName(null));
        Assert.assertFalse(FieldFormat.checkRoleName("@"));
        Assert.assertFalse(FieldFormat.checkRoleName("@$%#%$^$&&"));
        Assert.assertFalse(FieldFormat.checkRoleName("%123"));
        Assert.assertFalse(FieldFormat.checkRoleName("%hello"));
        Assert.assertFalse(FieldFormat.checkRoleName("%你好"));
        Assert.assertFalse(FieldFormat.checkRoleName("%你好hello123456"));
    }

    @Test
    public void test_checkWarehouseName() {
        Assert.assertTrue(FieldFormat.checkWarehouseName("你好"));
        Assert.assertTrue(FieldFormat.checkWarehouseName("hello"));
        Assert.assertTrue(FieldFormat.checkWarehouseName("123456"));
        Assert.assertTrue(FieldFormat.checkWarehouseName("你好hello123456"));
        //
        Assert.assertFalse(FieldFormat.checkWarehouseName(""));
        Assert.assertFalse(FieldFormat.checkWarehouseName("_"));
        Assert.assertFalse(FieldFormat.checkWarehouseName("-"));
        Assert.assertFalse(FieldFormat.checkWarehouseName(null));
        Assert.assertFalse(FieldFormat.checkWarehouseName("@"));
        Assert.assertFalse(FieldFormat.checkWarehouseName("@$%#%$^$&&"));
        Assert.assertFalse(FieldFormat.checkWarehouseName("%123"));
        Assert.assertFalse(FieldFormat.checkWarehouseName("%hello"));
        Assert.assertFalse(FieldFormat.checkWarehouseName("%你好"));
        Assert.assertFalse(FieldFormat.checkWarehouseName("%你好hello123456"));
    }

    @Test
    public void test_checkBrandName() {// 只允许中文、数字和英文，[1,20]字符
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkBrandName("12345123451234512345"));
        Assert.assertTrue(FieldFormat.checkBrandName("abcdeabcdeabcdeabcde"));
        Assert.assertTrue(FieldFormat.checkBrandName("中文中文中文中文中文"));
        Assert.assertTrue(FieldFormat.checkBrandName("1234中文中文中文abcd"));
        Assert.assertTrue(FieldFormat.checkBrandName("f93y你好hw中国2923"));
        Assert.assertTrue(FieldFormat.checkBrandName("f93y博昕科技hw中国2923"));

        Assert.assertFalse(FieldFormat.checkBrandName(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkBrandName(""));
        Assert.assertFalse(FieldFormat.checkBrandName(",./2923"));
        Assert.assertFalse(FieldFormat.checkBrandName("123451234512345123451"));
        Assert.assertFalse(FieldFormat.checkBrandName("abcdeabcdeabcdeabcdea"));
        Assert.assertFalse(FieldFormat.checkBrandName(null));
    }

    @Test
    public void test_checkProviderName() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkProviderName("973456742923"));
        Assert.assertTrue(FieldFormat.checkProviderName("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkProviderName("哈哈哈哈哈哈哈哈哈"));
        Assert.assertTrue(FieldFormat.checkProviderName("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkProviderName("f93y你好hw中国2923"));
        Assert.assertTrue(FieldFormat.checkProviderName("f93y博昕科技hw中国2923"));

        Assert.assertFalse(FieldFormat.checkProviderName(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkProviderName(""));
        Assert.assertFalse(FieldFormat.checkProviderName(",./2923"));
        Assert.assertFalse(FieldFormat.checkProviderName(null));
    }

    @Test
    public void test_checkCategoryName() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkCategoryName("973456742923"));
        Assert.assertTrue(FieldFormat.checkCategoryName("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkCategoryName("哈哈哈哈哈哈哈哈哈"));
        Assert.assertTrue(FieldFormat.checkCategoryName("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkCategoryName("f93y你好hw中国2923"));
        Assert.assertTrue(FieldFormat.checkCategoryName("f93y博昕科技hw中国2923"));

        Assert.assertFalse(FieldFormat.checkCategoryName(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkCategoryName(""));
        Assert.assertFalse(FieldFormat.checkCategoryName(",./2923"));
        Assert.assertFalse(FieldFormat.checkCategoryName(null));
    }

    @Test
    public void test_checkVipCategoryName() {// 只允许中文、数字和英文，[1, 30]]位
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkVipCategoryName("123456789012345678901234567890"));
        Assert.assertTrue(FieldFormat.checkVipCategoryName("ABCDEABCDEABCDEABCDEABCDEABCDE"));
        Assert.assertTrue(FieldFormat.checkVipCategoryName("哈哈哈哈哈哈哈哈哈"));
        Assert.assertTrue(FieldFormat.checkVipCategoryName("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkVipCategoryName("f93y你好hw中国2923"));
        Assert.assertTrue(FieldFormat.checkVipCategoryName("f93y博昕科技hw中国2923"));

        Assert.assertFalse(FieldFormat.checkVipCategoryName(" f93yhw 12345678"));
        Assert.assertFalse(FieldFormat.checkVipCategoryName("123456789012345678901234567890A"));
        Assert.assertFalse(FieldFormat.checkVipCategoryName("ABCDEABCDEABCDEABCDEABCDEABCDE1"));
        Assert.assertFalse(FieldFormat.checkVipCategoryName(""));
        Assert.assertFalse(FieldFormat.checkVipCategoryName(",./2923"));
        Assert.assertFalse(FieldFormat.checkVipCategoryName(null));
    }

    @Test
    public void test_checkNOStart() {
        Shared.printTestMethodStartInfo();
        Assert.assertTrue(FieldFormat.checkNOStart("1"));
        Assert.assertTrue(FieldFormat.checkNOStart("1542"));
        Assert.assertTrue(FieldFormat.checkNOStart("354"));
        Assert.assertTrue(FieldFormat.checkNOStart("43554"));
        Assert.assertTrue(FieldFormat.checkNOStart("3543542"));
        Assert.assertTrue(FieldFormat.checkNOStart("54254254"));
        //
        Assert.assertFalse(FieldFormat.checkNOStart(""));
        Assert.assertFalse(FieldFormat.checkNOStart(null));
        Assert.assertFalse(FieldFormat.checkNOStart("0"));
        Assert.assertFalse(FieldFormat.checkNOStart("-1"));
        Assert.assertFalse(FieldFormat.checkNOStart("-1542"));
        Assert.assertFalse(FieldFormat.checkNOStart("-354"));
        Assert.assertFalse(FieldFormat.checkNOStart("-43554"));
        Assert.assertFalse(FieldFormat.checkNOStart("-3543542"));
    }

    @Test
    public void test_checkMobile() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkMobile("19456742923"));
        // Assert.assertTrue(FieldFormat.checkMobile("0"));//这个目的是什么？？
        Assert.assertFalse(FieldFormat.checkMobile("159123456789"));
        Assert.assertFalse(FieldFormat.checkMobile("1347894561"));
        Assert.assertFalse(FieldFormat.checkMobile(" "));
        Assert.assertFalse(FieldFormat.checkMobile("-11"));
        Assert.assertFalse(FieldFormat.checkMobile("11.1"));
        Assert.assertFalse(FieldFormat.checkMobile("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkMobile("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkMobile("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkMobile("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkMobile(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkMobile("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkMobile("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkMobile("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkMobile("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkMobile(",./2923"));
        Assert.assertFalse(FieldFormat.checkMobile(null));
    }

    @Test
    public void test_checkContactName() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkContactName("973456742923"));
        Assert.assertTrue(FieldFormat.checkContactName("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkContactName("哈哈哈哈哈哈哈哈哈"));
        Assert.assertTrue(FieldFormat.checkContactName("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkContactName("f93y你好hw中国2923"));
        Assert.assertTrue(FieldFormat.checkContactName("f93y博昕科技hw中国2923"));
        Assert.assertTrue(FieldFormat.checkContactName("f93y博昕科技hw 中国2923"));
        Assert.assertTrue(FieldFormat.checkContactName("f"));
        Assert.assertTrue(FieldFormat.checkContactName("1"));
        Assert.assertTrue(FieldFormat.checkContactName("啊"));
        Assert.assertTrue(FieldFormat.checkContactName(null));

        Assert.assertFalse(FieldFormat.checkContactName(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkContactName(" f"));
        Assert.assertFalse(FieldFormat.checkContactName(""));
        Assert.assertFalse(FieldFormat.checkContactName(",./2923"));
    }

    @Test
    public void test_checkAddress() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkAddress("973456742923"));
        Assert.assertTrue(FieldFormat.checkAddress("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkAddress("哈哈哈哈哈哈哈哈哈"));
        Assert.assertTrue(FieldFormat.checkAddress("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkAddress("f93y你好hw中国2923"));
        Assert.assertTrue(FieldFormat.checkAddress("f93y博昕科技hw中国2923"));
        Assert.assertTrue(FieldFormat.checkAddress(null));

        Assert.assertFalse(FieldFormat.checkAddress(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkAddress(""));
        Assert.assertFalse(FieldFormat.checkAddress(",./2923"));
    }

    @Test
    public void test_checkName() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkName("973456742923"));
        Assert.assertTrue(FieldFormat.checkName("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkName("f93yhw2923"));
        Assert.assertTrue(FieldFormat.checkName("f93yhw 2923"));
        Assert.assertTrue(FieldFormat.checkName("f93yhw 2923 "));
        Assert.assertTrue(FieldFormat.checkName(" f93yhw 2923"));
        Assert.assertTrue(FieldFormat.checkName("f93 yhw 2923"));
        Assert.assertTrue(FieldFormat.checkName("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkName("f93y你好hw中国2 923"));
        Assert.assertTrue(FieldFormat.checkName("f93y博昕科技hw中国2 923"));

        Assert.assertFalse(FieldFormat.checkName(""));
        Assert.assertFalse(FieldFormat.checkName(",./2923"));
        Assert.assertFalse(FieldFormat.checkName(null));
    }

    @Test
    public void test_checkShortName() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkShortName("973456742923"));
        Assert.assertTrue(FieldFormat.checkShortName("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkShortName("f93yhw2923"));
        Assert.assertTrue(FieldFormat.checkShortName("f93yhw 2923"));
        Assert.assertTrue(FieldFormat.checkShortName("f93yhw 2923 "));
        Assert.assertTrue(FieldFormat.checkShortName(" f93yhw 2923"));
        Assert.assertTrue(FieldFormat.checkShortName("f93 yhw 2923"));
        Assert.assertTrue(FieldFormat.checkShortName("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkShortName("f93y你好hw中国2 923"));
        Assert.assertTrue(FieldFormat.checkShortName("f93y博昕科技hw中国2 923"));

        Assert.assertFalse(FieldFormat.checkShortName(""));
        Assert.assertFalse(FieldFormat.checkShortName(",./2923"));
        Assert.assertFalse(FieldFormat.checkShortName(null));
    }

    @Test
    public void test_checkShopName() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkShopName("01234567890123456789")); // 20个
        Assert.assertTrue(FieldFormat.checkShopName("博昕"));
        Assert.assertTrue(FieldFormat.checkShopName("博 昕"));
        Assert.assertTrue(FieldFormat.checkShopName("博   昕"));
        Assert.assertTrue(FieldFormat.checkShopName("中"));
        Assert.assertTrue(FieldFormat.checkShopName("2"));

        Assert.assertFalse(FieldFormat.checkShopName("012345678901234567891")); // 21个
        Assert.assertFalse(FieldFormat.checkShopName("0123 "));
        Assert.assertFalse(FieldFormat.checkShopName(" 0123"));
        Assert.assertFalse(FieldFormat.checkShopName(" 0123 "));
        Assert.assertFalse(FieldFormat.checkShopName(""));
        Assert.assertFalse(FieldFormat.checkShopName(" "));
        Assert.assertFalse(FieldFormat.checkShopName(null));
    }

    @Test
    public void test_checkShopAddress() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkShopAddress("012345678901234567890123456789")); // 20个
        Assert.assertTrue(FieldFormat.checkShopAddress("博昕"));
        Assert.assertTrue(FieldFormat.checkShopAddress("博 昕"));
        Assert.assertTrue(FieldFormat.checkShopAddress("博   昕"));
        Assert.assertTrue(FieldFormat.checkShopAddress("中"));
        Assert.assertTrue(FieldFormat.checkShopAddress("2"));

        Assert.assertFalse(FieldFormat.checkShopAddress("0123456789012345678901234567891")); // 21个
        Assert.assertFalse(FieldFormat.checkShopAddress("0123 "));
        Assert.assertFalse(FieldFormat.checkShopAddress(" 0123"));
        Assert.assertFalse(FieldFormat.checkShopAddress(" 0123 "));
        Assert.assertFalse(FieldFormat.checkShopAddress(""));
        Assert.assertFalse(FieldFormat.checkShopAddress(" "));
        Assert.assertFalse(FieldFormat.checkShopAddress(null));
    }

    @Test
    public void test_checkBarcode() {
        Shared.printTestMethodStartInfo();
        Assert.assertTrue(FieldFormat.checkBarcode("012345678901234567890123456789"));
        Assert.assertTrue(FieldFormat.checkBarcode("fdsafdsafdasfds"));
        //
        Assert.assertFalse(FieldFormat.checkBarcode(""));
        Assert.assertFalse(FieldFormat.checkBarcode(null));
        Assert.assertFalse(FieldFormat.checkBarcode("你好"));
        Assert.assertFalse(FieldFormat.checkBarcode("!#$@"));
        Assert.assertFalse(FieldFormat.checkBarcode("."));
        Assert.assertFalse(FieldFormat.checkBarcode("-"));
        Assert.assertFalse(FieldFormat.checkBarcode("_"));
        Assert.assertFalse(FieldFormat.checkBarcode("="));
    }

    @Test
    public void test_checkSpecification() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkSpecification("973456742923"));
        Assert.assertTrue(FieldFormat.checkSpecification("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkSpecification("f93yhw2923"));
        Assert.assertTrue(FieldFormat.checkSpecification("f93yhw 2923"));
        Assert.assertTrue(FieldFormat.checkSpecification("f93yhw 2923 "));
        Assert.assertTrue(FieldFormat.checkSpecification(" f93yhw 2923"));
        Assert.assertTrue(FieldFormat.checkSpecification("f93 yhw 2923"));
        Assert.assertTrue(FieldFormat.checkSpecification("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkSpecification("f93y你好hw中国2 923"));
        Assert.assertTrue(FieldFormat.checkSpecification("f93y博昕科技hw中国2 923"));

        Assert.assertFalse(FieldFormat.checkSpecification(""));
        Assert.assertFalse(FieldFormat.checkSpecification(",./2923"));
        Assert.assertFalse(FieldFormat.checkSpecification(null));
    }

    @Test
    public void test_checkPackageUnitID() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkPackageUnitID("973456742923"));
        Assert.assertTrue(FieldFormat.checkPackageUnitID("0"));

        Assert.assertFalse(FieldFormat.checkPackageUnitID(" "));
        Assert.assertFalse(FieldFormat.checkPackageUnitID("-11"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID("11.1"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkPackageUnitID(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID(",./2923"));
        Assert.assertFalse(FieldFormat.checkPackageUnitID(null));
    }

    @Test
    public void test_checkPurchasingUnit() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkPurchasingUnit("973456742923"));
        Assert.assertTrue(FieldFormat.checkPurchasingUnit("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93yhw2923"));
        Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93yhw 2923"));
        Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93yhw 2923 "));
        Assert.assertTrue(FieldFormat.checkPurchasingUnit(" f93yhw 2923"));
        Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93 yhw 2923"));
        Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93y你好hw中国2 923"));
        Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93y博昕科技hw中国2 923"));
        Assert.assertTrue(FieldFormat.checkPurchasingUnit(null));

        Assert.assertFalse(FieldFormat.checkPurchasingUnit(""));
        Assert.assertFalse(FieldFormat.checkPurchasingUnit(",./2923"));
    }

    @Test
    public void test_checkProviderID() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkProviderID("973456742923"));

        Assert.assertFalse(FieldFormat.checkProviderID(" "));
        Assert.assertFalse(FieldFormat.checkProviderID("-11"));
        Assert.assertFalse(FieldFormat.checkProviderID("11.1"));
        Assert.assertFalse(FieldFormat.checkProviderID("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkProviderID("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkProviderID("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkProviderID("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkProviderID(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkProviderID("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkProviderID("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkProviderID("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkProviderID("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkProviderID(",./2923"));
        Assert.assertFalse(FieldFormat.checkProviderID(null));
    }

    @Test
    public void test_checkBrandID() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkBrandID("973456742923"));

        Assert.assertFalse(FieldFormat.checkBrandID(" "));
        Assert.assertFalse(FieldFormat.checkBrandID("-11"));
        Assert.assertFalse(FieldFormat.checkBrandID("11.1"));
        Assert.assertFalse(FieldFormat.checkBrandID("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkBrandID("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkBrandID("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkBrandID("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkBrandID(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkBrandID("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkBrandID("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkBrandID("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkBrandID("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkBrandID(",./2923"));
        Assert.assertFalse(FieldFormat.checkBrandID(null));
    }

    @Test
    public void test_checkCategoryID() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkCategoryID("973456742923"));

        Assert.assertFalse(FieldFormat.checkCategoryID("-973456742923"));
        Assert.assertFalse(FieldFormat.checkCategoryID("11.1"));
        Assert.assertFalse(FieldFormat.checkCategoryID(" "));
        Assert.assertFalse(FieldFormat.checkCategoryID("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkCategoryID("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkCategoryID("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkCategoryID("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkCategoryID(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkCategoryID("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkCategoryID("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkCategoryID("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkCategoryID("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkCategoryID(",./2923"));
        Assert.assertFalse(FieldFormat.checkCategoryID(null));
    }

    @Test
    public void test_checkMnemonicCode() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkMnemonicCode("973456742923"));
        Assert.assertTrue(FieldFormat.checkMnemonicCode("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkMnemonicCode("f93yhw2923"));

        Assert.assertFalse(FieldFormat.checkMnemonicCode(""));
        Assert.assertFalse(FieldFormat.checkMnemonicCode(",./2923"));
        Assert.assertFalse(FieldFormat.checkMnemonicCode("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkMnemonicCode("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkMnemonicCode(null));
    }

    @Test
    public void test_checkPrice() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkPrice(0));
        Assert.assertTrue(FieldFormat.checkPrice(1));
        Assert.assertTrue(FieldFormat.checkPrice(115));
        Assert.assertTrue(FieldFormat.checkPrice(78748481));
        Assert.assertTrue(FieldFormat.checkPrice(0.55));
        Assert.assertTrue(FieldFormat.checkPrice(1.55));
        Assert.assertTrue(FieldFormat.checkPrice(115.55));
        Assert.assertTrue(FieldFormat.checkPrice(78748481.55));
        //
        Assert.assertFalse(FieldFormat.checkPrice(-1));
        Assert.assertFalse(FieldFormat.checkPrice(-115));
        Assert.assertFalse(FieldFormat.checkPrice(-78748481));
        Assert.assertFalse(FieldFormat.checkPrice(-0.55));
        Assert.assertFalse(FieldFormat.checkPrice(-1.55));
        Assert.assertFalse(FieldFormat.checkPrice(-115.55));
        Assert.assertFalse(FieldFormat.checkPrice(-78748481.55));
    }

    @Test
    public void test_checkPriceVIP() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkPriceVIP(0));
        Assert.assertTrue(FieldFormat.checkPriceVIP(1));
        Assert.assertTrue(FieldFormat.checkPriceVIP(115));
        Assert.assertTrue(FieldFormat.checkPriceVIP(78748481));
        Assert.assertTrue(FieldFormat.checkPriceVIP(0.55));
        Assert.assertTrue(FieldFormat.checkPriceVIP(1.55));
        Assert.assertTrue(FieldFormat.checkPriceVIP(115.55));
        Assert.assertTrue(FieldFormat.checkPriceVIP(78748481.55));
        //
        Assert.assertFalse(FieldFormat.checkPriceVIP(-1));
        Assert.assertFalse(FieldFormat.checkPriceVIP(-115));
        Assert.assertFalse(FieldFormat.checkPriceVIP(-78748481));
        Assert.assertFalse(FieldFormat.checkPriceVIP(-0.55));
        Assert.assertFalse(FieldFormat.checkPriceVIP(-1.55));
        Assert.assertFalse(FieldFormat.checkPriceVIP(-115.55));
        Assert.assertFalse(FieldFormat.checkPriceVIP(-78748481.55));
    }

    @Test
    public void test_checkPriceWholesale() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkPriceWholesale(0));
        Assert.assertTrue(FieldFormat.checkPriceWholesale(1));
        Assert.assertTrue(FieldFormat.checkPriceWholesale(115));
        Assert.assertTrue(FieldFormat.checkPriceWholesale(78748481));
        Assert.assertTrue(FieldFormat.checkPriceWholesale(0.55));
        Assert.assertTrue(FieldFormat.checkPriceWholesale(1.55));
        Assert.assertTrue(FieldFormat.checkPriceWholesale(115.55));
        Assert.assertTrue(FieldFormat.checkPriceWholesale(78748481.55));
        //
        Assert.assertFalse(FieldFormat.checkPriceWholesale(-1));
        Assert.assertFalse(FieldFormat.checkPriceWholesale(-115));
        Assert.assertFalse(FieldFormat.checkPriceWholesale(-78748481));
        Assert.assertFalse(FieldFormat.checkPriceWholesale(-0.55));
        Assert.assertFalse(FieldFormat.checkPriceWholesale(-1.55));
        Assert.assertFalse(FieldFormat.checkPriceWholesale(-115.55));
        Assert.assertFalse(FieldFormat.checkPriceWholesale(-78748481.55));
    }

    @Test
    public void test_checkRatioGrossMargin() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkRatioGrossMargin("973456742923"));
        Assert.assertTrue(FieldFormat.checkRatioGrossMargin("11.1"));
        Assert.assertTrue(FieldFormat.checkRatioGrossMargin("0.05"));
        Assert.assertTrue(FieldFormat.checkRatioGrossMargin("11.111111"));

        Assert.assertFalse(FieldFormat.checkRatioGrossMargin("11.1111111"));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin(""));
        Assert.assertFalse(FieldFormat.checkRatioGrossMargin(",./2923"));
    }

    @Test
    public void test_checkPurchaseFlag() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkPurchaseFlag("0"));
        Assert.assertTrue(FieldFormat.checkPurchaseFlag("11"));
        Assert.assertTrue(FieldFormat.checkPurchaseFlag(null));

        Assert.assertFalse(FieldFormat.checkPurchaseFlag(" "));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag("-11"));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag("11.1"));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkPurchaseFlag(",./2923"));
    }

    @Test
    public void test_checkRuleOfPoint() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkRuleOfPoint("11"));
        Assert.assertTrue(FieldFormat.checkRuleOfPoint(null));

        Assert.assertFalse(FieldFormat.checkRuleOfPoint(" "));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint("-11"));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint("11.1"));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkRuleOfPoint(",./2923"));
    }

    @Test
    public void test_checkRefCommodityID() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkRefCommodityID("973456742923"));
        Assert.assertTrue(FieldFormat.checkRefCommodityID("0"));

        Assert.assertFalse(FieldFormat.checkRefCommodityID(" "));
        Assert.assertFalse(FieldFormat.checkRefCommodityID("-11"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID("11.1"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkRefCommodityID(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID(",./2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityID(null));
    }

    @Test
    public void test_checkRefCommodityMultiple() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkRefCommodityMultiple("973456742923"));
        Assert.assertTrue(FieldFormat.checkRefCommodityMultiple("0"));

        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple(" "));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("-11"));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("11.1"));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple(",./2923"));
        Assert.assertFalse(FieldFormat.checkRefCommodityMultiple(null));
    }

    @Test
    public void test_checkTag() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkTag("973456742923"));
        Assert.assertTrue(FieldFormat.checkTag("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkTag("哈哈哈哈哈哈哈哈哈"));
        Assert.assertTrue(FieldFormat.checkTag("f93yhw中国2923"));
        Assert.assertTrue(FieldFormat.checkTag("f93y你好hw中国2923"));
        Assert.assertTrue(FieldFormat.checkTag("f93y博昕科技hw中国2923"));

        Assert.assertFalse(FieldFormat.checkTag(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkTag(""));
        Assert.assertFalse(FieldFormat.checkTag(",./2923"));
        Assert.assertFalse(FieldFormat.checkTag(null));
    }

    @Test
    public void test_checkSenderID() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkSenderID("973456"));
        Assert.assertTrue(FieldFormat.checkSenderID("0"));

        Assert.assertFalse(FieldFormat.checkSenderID("-973"));
        Assert.assertFalse(FieldFormat.checkSenderID(" "));
        Assert.assertFalse(FieldFormat.checkSenderID("11.1"));
        Assert.assertFalse(FieldFormat.checkSenderID("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkSenderID("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkSenderID("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkSenderID("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkSenderID(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkSenderID("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkSenderID("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkSenderID(",./2923"));
        Assert.assertFalse(FieldFormat.checkSenderID(null));
    }

    @Test
    public void test_checkNO() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkNO("973456"));
        Assert.assertTrue(FieldFormat.checkNO("0"));

        Assert.assertFalse(FieldFormat.checkNO("-973"));
        Assert.assertFalse(FieldFormat.checkNO(" "));
        Assert.assertFalse(FieldFormat.checkNO("11.1"));
        Assert.assertFalse(FieldFormat.checkNO("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkNO("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkNO("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkNO("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkNO(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkNO("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkNO("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkNO(",./2923"));
        Assert.assertFalse(FieldFormat.checkNO(null));
    }

    @Test
    public void test_checkInventoryCommodityNoReal() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkInventoryCommodityNoReal("973456"));
        Assert.assertTrue(FieldFormat.checkInventoryCommodityNoReal("0"));
        Assert.assertTrue(FieldFormat.checkInventoryCommodityNoReal("-973"));

        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal(" "));
        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("11.1"));
        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal(",./2923"));
        Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal(null));
    }

    @Test
    public void test_checkNOAccumulated() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkNOAccumulated("973456742923"));
        Assert.assertTrue(FieldFormat.checkNOAccumulated("0"));

        Assert.assertFalse(FieldFormat.checkNOAccumulated(" "));
        Assert.assertFalse(FieldFormat.checkNOAccumulated("-11"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated("11.1"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkNOAccumulated(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated(",./2923"));
        Assert.assertFalse(FieldFormat.checkNOAccumulated(null));
    }

    @Test
    public void test_checkReturnDays() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkReturnDays("973456742923"));
        Assert.assertTrue(FieldFormat.checkReturnDays("0"));

        Assert.assertFalse(FieldFormat.checkReturnDays(" "));
        Assert.assertFalse(FieldFormat.checkReturnDays("-11"));
        Assert.assertFalse(FieldFormat.checkReturnDays("11.1"));
        Assert.assertFalse(FieldFormat.checkReturnDays("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkReturnDays("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkReturnDays("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkReturnDays("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkReturnDays(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkReturnDays("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkReturnDays("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkReturnDays("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkReturnDays(",./2923"));
        Assert.assertFalse(FieldFormat.checkReturnDays(null));
    }

    @Test
    public void test_checkShelfLife() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkShelfLife("11"));
        Assert.assertTrue(FieldFormat.checkShelfLife(null));

        Assert.assertFalse(FieldFormat.checkShelfLife("0"));
        Assert.assertFalse(FieldFormat.checkShelfLife(" "));
        Assert.assertFalse(FieldFormat.checkShelfLife("-11"));
        Assert.assertFalse(FieldFormat.checkShelfLife("11.1"));
        Assert.assertFalse(FieldFormat.checkShelfLife("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkShelfLife("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkShelfLife("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkShelfLife("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkShelfLife(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkShelfLife("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkShelfLife("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkShelfLife("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkShelfLife("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkShelfLife(",./2923"));
    }

    @Test
    public void test_checkPhone() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkPhone("12345678901"));
        //
        Assert.assertFalse(FieldFormat.checkPhone("02345678901"));
        Assert.assertFalse(FieldFormat.checkPhone(" "));
        Assert.assertFalse(FieldFormat.checkPhone("-11"));
        Assert.assertFalse(FieldFormat.checkPhone("11.1"));
        Assert.assertFalse(FieldFormat.checkPhone("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkPhone("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkPhone("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkPhone("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkPhone(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkPhone("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkPhone("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkPhone("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkPhone(",./2923"));
        Assert.assertFalse(FieldFormat.checkPhone(null));
    }

    @Test
    public void test_checkRawPassword() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkRawPassword("ASDF12"));
        Assert.assertTrue(FieldFormat.checkRawPassword("#@%@~.+ 1234567"));
        Assert.assertTrue(FieldFormat.checkRawPassword("#@% @~.+ 12347"));
        Assert.assertTrue(FieldFormat.checkRawPassword("#dfg.+ SD34567"));
        Assert.assertTrue(FieldFormat.checkRawPassword("!@# $%+."));

        Assert.assertFalse(FieldFormat.checkRawPassword("sdf23"));
        Assert.assertFalse(FieldFormat.checkRawPassword("12354678545ds@#fd"));
        Assert.assertFalse(FieldFormat.checkRawPassword(" #@% aG~.+ 147"));
        Assert.assertFalse(FieldFormat.checkRawPassword("#@% aG~.+ 147 "));
        Assert.assertFalse(FieldFormat.checkRawPassword(null));
        Assert.assertFalse(FieldFormat.checkRawPassword(""));
        Assert.assertFalse(FieldFormat.checkRawPassword("       "));
        Assert.assertFalse(FieldFormat.checkRawPassword("!@#123中文Abc$%^.+-"));
        Assert.assertFalse(FieldFormat.checkRawPassword("中!@#123Abc$%^.+-"));
        Assert.assertFalse(FieldFormat.checkRawPassword("!@#123Abc$%^.+-文"));
        Assert.assertFalse(FieldFormat.checkRawPassword("中!@#123Abc$%^.+-文"));
        Assert.assertFalse(FieldFormat.checkRawPassword(" !@#123Abc$%^.+- "));
    }

    @Test
    public void test_checkWeChat() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkWeChat("as545"));
        Assert.assertTrue(FieldFormat.checkWeChat("asd_sd32"));
        Assert.assertTrue(FieldFormat.checkWeChat("asdte-kh53"));
        Assert.assertTrue(FieldFormat.checkWeChat("asdte-11"));
        Assert.assertTrue(FieldFormat.checkWeChat("asdte_11"));
        Assert.assertTrue(FieldFormat.checkWeChat("12345678901234567890"));

        Assert.assertFalse(FieldFormat.checkWeChat("1231+11"));
        Assert.assertFalse(FieldFormat.checkWeChat("1234"));
        Assert.assertFalse(FieldFormat.checkWeChat("123456789012345678901"));
        Assert.assertFalse(FieldFormat.checkWeChat("12"));
        Assert.assertFalse(FieldFormat.checkWeChat("#ljljr"));
        Assert.assertFalse(FieldFormat.checkWeChat("sdsd"));
        Assert.assertFalse(FieldFormat.checkWeChat("as看了就开了个"));
        Assert.assertFalse(FieldFormat.checkWeChat(""));
        try {
            FieldFormat.checkWeChat(null);
            Assert.assertTrue(false);
        } catch (NullPointerException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void test_checkDbName() {// 数字、字母和下划线的组合，但首字符必须是字母，中间不能出现空格。(0, 20]个字符
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkDbName("abcdeabcdeabcdeabcde"));
        Assert.assertTrue(FieldFormat.checkDbName("abcdeabcdeabcdeabcd_"));
        Assert.assertTrue(FieldFormat.checkDbName("abcdeabcdeabcdeabc9_"));
        Assert.assertTrue(FieldFormat.checkDbName("abcdef1234567890"));
        Assert.assertTrue(FieldFormat.checkDbName("abcdef1234567_890"));

        Assert.assertFalse(FieldFormat.checkDbName("abcdeabcdeabcdeabcde1"));
        Assert.assertFalse(FieldFormat.checkDbName("abc de"));
        Assert.assertFalse(FieldFormat.checkDbName(""));
        Assert.assertFalse(FieldFormat.checkDbName(null));
    }

    @Test
    public void test_checkSalt() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkSalt("B1AFC07474C37C5AEC4199ED28E09705"));
        Assert.assertTrue(FieldFormat.checkSalt("11111111111111111111111111111111"));
        Assert.assertTrue(FieldFormat.checkSalt("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

        Assert.assertFalse(FieldFormat.checkSalt("1AFC07474C37C5AEC4199ED28E09705"));
        Assert.assertFalse(FieldFormat.checkSalt("B1AFC07474C37C5AEC4199ED28E097051"));
        Assert.assertFalse(FieldFormat.checkSalt("a1AFC07474C37C5AEC4199ED28E09705"));
        Assert.assertFalse(FieldFormat.checkSalt(null));
    }

    @Test
    public void test_checkICID() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkICID("44152219940925821X"));
        Assert.assertTrue(FieldFormat.checkICID("44152219990925821x"));
        Assert.assertTrue(FieldFormat.checkICID("441522199409258216"));
        Assert.assertTrue(FieldFormat.checkICID(null));

        Assert.assertFalse(FieldFormat.checkICID("44152219940925821"));
        Assert.assertFalse(FieldFormat.checkICID("441522199409258219x"));
        Assert.assertFalse(FieldFormat.checkICID("441522199409258X1"));
        Assert.assertFalse(FieldFormat.checkICID("    "));
        Assert.assertFalse(FieldFormat.checkICID("asd123fgh456jkl789"));
        Assert.assertFalse(FieldFormat.checkICID("测试12345678909876"));
    }

    @Test
    public void test_checkIfMultiPackagingType() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()));
        //
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(CommodityType.EnumCommodityType.ECT_Normal.getIndex()));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(CommodityType.EnumCommodityType.ECT_Combination.getIndex()));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(CommodityType.EnumCommodityType.ECT_Service.getIndex()));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(10));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(100));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(1000));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(10000));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(-10));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(-100));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(-1000));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingType(-10000));
    }

    @Test
    public void test_checkIfServiceType() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfServiceType(CommodityType.EnumCommodityType.ECT_Service.getIndex()));
        //
        Assert.assertFalse(FieldFormat.checkIfServiceType(CommodityType.EnumCommodityType.ECT_Normal.getIndex()));
        Assert.assertFalse(FieldFormat.checkIfServiceType(CommodityType.EnumCommodityType.ECT_Combination.getIndex()));
        Assert.assertFalse(FieldFormat.checkIfServiceType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()));
        Assert.assertFalse(FieldFormat.checkIfServiceType(10));
        Assert.assertFalse(FieldFormat.checkIfServiceType(100));
        Assert.assertFalse(FieldFormat.checkIfServiceType(1000));
        Assert.assertFalse(FieldFormat.checkIfServiceType(10000));
        Assert.assertFalse(FieldFormat.checkIfServiceType(-10));
        Assert.assertFalse(FieldFormat.checkIfServiceType(-100));
        Assert.assertFalse(FieldFormat.checkIfServiceType(-1000));
        Assert.assertFalse(FieldFormat.checkIfServiceType(-10000));
    }

    @Test
    public void test_checkIfMultiPackagingStatus() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingStatus(0));
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingStatus(1));
        //
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingStatus(10));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingStatus(100));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingStatus(1000));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingStatus(10000));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingStatus(-10));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingStatus(-100));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingStatus(-1000));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingStatus(-10000));
    }

    @Test
    public void test_checkIfCommodityStatus() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfCommodityStatus(0));
        //
        Assert.assertFalse(FieldFormat.checkIfCommodityStatus(1));
        Assert.assertFalse(FieldFormat.checkIfCommodityStatus(-1));
        Assert.assertFalse(FieldFormat.checkIfCommodityStatus(10));
        Assert.assertFalse(FieldFormat.checkIfCommodityStatus(100));
        Assert.assertFalse(FieldFormat.checkIfCommodityStatus(1000));
        Assert.assertFalse(FieldFormat.checkIfCommodityStatus(10000));
        Assert.assertFalse(FieldFormat.checkIfCommodityStatus(-10));
        Assert.assertFalse(FieldFormat.checkIfCommodityStatus(-100));
        Assert.assertFalse(FieldFormat.checkIfCommodityStatus(-1000));
        Assert.assertFalse(FieldFormat.checkIfCommodityStatus(-10000));
    }

    @Test
    public void test_checkIfCommodityType() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkIfCommodityType(0));

        Assert.assertFalse(FieldFormat.checkIfCommodityType(-1));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(1));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(1));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(-1));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(10));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(100));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(1000));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(10000));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(-10));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(-100));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(-1000));
        Assert.assertFalse(FieldFormat.checkIfCommodityType(-10000));
    }

    @Test
    public void test_checkIfMultiPackagingBarcodes() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingBarcodes("1234567"));
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingBarcodes("abcdefg"));
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingBarcodes("1234567abcdefg"));
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingBarcodes("1234567890123456789012345678901234567890123456789012345678901234"));
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingBarcodes("abcdefhijkabcdefhijkabcdefhijkabcdefhijkabcdefhijkabcdefhijkabcd"));
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingBarcodes("abcdefhijkabcdefhijkabcdefhijk1234567890123456789012345678901234"));
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingBarcodes("1234567890123456789012345678901234abcdefhijkabcdefhijkabcdefhijk"));
        //
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingBarcodes(""));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingBarcodes(null));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingBarcodes("123456"));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingBarcodes("abcdef"));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingBarcodes("12345678901234567890123456789012345678901234567890123456789012345"));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingBarcodes("abcdefhijkabcdefhijkabcdefhijkabcdefhijkabcdefhijkabcdefhijkabcde"));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingBarcodes("abcdefhijkabcdefhijkabcdefhijk12345678901234567890123456789012345"));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingBarcodes("1234567890123456789012345678901234abcdefhijkabcdefhijkabcdefhijka"));
    }

    @Test
    public void test_checkIfMultiPackagingNO() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfMultiPackagingNO(0));
        //
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(-1));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(1));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(1));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(-1));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(10));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(100));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(1000));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(10000));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(-10));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(-100));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(-1000));
        Assert.assertFalse(FieldFormat.checkIfMultiPackagingNO(-10000));
    }

    @Test
    public void test_checkIfMultiiaPriceRetail() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfMultiiaPriceRetail(0));
        Assert.assertTrue(FieldFormat.checkIfMultiiaPriceRetail(1));
        Assert.assertTrue(FieldFormat.checkIfMultiiaPriceRetail(10));
        Assert.assertTrue(FieldFormat.checkIfMultiiaPriceRetail(100));
        Assert.assertTrue(FieldFormat.checkIfMultiiaPriceRetail(1000));
        Assert.assertTrue(FieldFormat.checkIfMultiiaPriceRetail(10000));
        //
        Assert.assertFalse(FieldFormat.checkIfMultiiaPriceRetail(-1));
        Assert.assertFalse(FieldFormat.checkIfMultiiaPriceRetail(-10));
        Assert.assertFalse(FieldFormat.checkIfMultiiaPriceRetail(-100));
        Assert.assertFalse(FieldFormat.checkIfMultiiaPriceRetail(-1000));
        Assert.assertFalse(FieldFormat.checkIfMultiiaPriceRetail(-10000));
    }

    @Test
    public void test_checkVIPPrice() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkVIPPrice(0));
        Assert.assertTrue(FieldFormat.checkVIPPrice(1));
        Assert.assertTrue(FieldFormat.checkVIPPrice(10));
        Assert.assertTrue(FieldFormat.checkVIPPrice(100));
        Assert.assertTrue(FieldFormat.checkVIPPrice(1000));
        Assert.assertTrue(FieldFormat.checkVIPPrice(10000));
        //
        Assert.assertFalse(FieldFormat.checkVIPPrice(-1));
        Assert.assertFalse(FieldFormat.checkVIPPrice(-10));
        Assert.assertFalse(FieldFormat.checkVIPPrice(-100));
        Assert.assertFalse(FieldFormat.checkVIPPrice(-1000));
        Assert.assertFalse(FieldFormat.checkVIPPrice(-10000));
    }

    @Test
    public void test_checkNoReal() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkNoReal(0));
        Assert.assertTrue(FieldFormat.checkNoReal(1));
        Assert.assertTrue(FieldFormat.checkNoReal(10));
        Assert.assertTrue(FieldFormat.checkNoReal(100));
        Assert.assertTrue(FieldFormat.checkNoReal(1000));
        Assert.assertTrue(FieldFormat.checkNoReal(10000));
        //
        Assert.assertFalse(FieldFormat.checkNoReal(-1));
        Assert.assertFalse(FieldFormat.checkNoReal(-10));
        Assert.assertFalse(FieldFormat.checkNoReal(-100));
        Assert.assertFalse(FieldFormat.checkNoReal(-1000));
        Assert.assertFalse(FieldFormat.checkNoReal(-10000));
    }

    @Test
    public void test_checkIfCompositionPurchaseFlag() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfCompositionPurchaseFlag(-1));
        //
        Assert.assertFalse(FieldFormat.checkIfCompositionPurchaseFlag(0));
        Assert.assertFalse(FieldFormat.checkIfCompositionPurchaseFlag(1));
        Assert.assertFalse(FieldFormat.checkIfCompositionPurchaseFlag(10));
        Assert.assertFalse(FieldFormat.checkIfCompositionPurchaseFlag(100));
        Assert.assertFalse(FieldFormat.checkIfCompositionPurchaseFlag(1000));
        Assert.assertFalse(FieldFormat.checkIfCompositionPurchaseFlag(10000));
        Assert.assertFalse(FieldFormat.checkIfCompositionPurchaseFlag(-10));
        Assert.assertFalse(FieldFormat.checkIfCompositionPurchaseFlag(-100));
        Assert.assertFalse(FieldFormat.checkIfCompositionPurchaseFlag(-1000));
        Assert.assertFalse(FieldFormat.checkIfCompositionPurchaseFlag(-10000));
    }

    @Test
    public void test_checkIfCompositionShelfLife() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfCompositionShelfLife(-1));
        //
        Assert.assertFalse(FieldFormat.checkIfCompositionShelfLife(0));
        Assert.assertFalse(FieldFormat.checkIfCompositionShelfLife(1));
        Assert.assertFalse(FieldFormat.checkIfCompositionShelfLife(10));
        Assert.assertFalse(FieldFormat.checkIfCompositionShelfLife(100));
        Assert.assertFalse(FieldFormat.checkIfCompositionShelfLife(1000));
        Assert.assertFalse(FieldFormat.checkIfCompositionShelfLife(10000));
        Assert.assertFalse(FieldFormat.checkIfCompositionShelfLife(-10));
        Assert.assertFalse(FieldFormat.checkIfCompositionShelfLife(-100));
        Assert.assertFalse(FieldFormat.checkIfCompositionShelfLife(-1000));
        Assert.assertFalse(FieldFormat.checkIfCompositionShelfLife(-10000));
    }

    @Test
    public void test_checkIfCompositionRuleOfPoint() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfCompositionRuleOfPoint(-1));
        //
        Assert.assertFalse(FieldFormat.checkIfCompositionRuleOfPoint(0));
        Assert.assertFalse(FieldFormat.checkIfCompositionRuleOfPoint(1));
        Assert.assertFalse(FieldFormat.checkIfCompositionRuleOfPoint(10));
        Assert.assertFalse(FieldFormat.checkIfCompositionRuleOfPoint(100));
        Assert.assertFalse(FieldFormat.checkIfCompositionRuleOfPoint(1000));
        Assert.assertFalse(FieldFormat.checkIfCompositionRuleOfPoint(10000));
        Assert.assertFalse(FieldFormat.checkIfCompositionRuleOfPoint(-10));
        Assert.assertFalse(FieldFormat.checkIfCompositionRuleOfPoint(-100));
        Assert.assertFalse(FieldFormat.checkIfCompositionRuleOfPoint(-1000));
        Assert.assertFalse(FieldFormat.checkIfCompositionRuleOfPoint(-10000));
    }

    @Test
    public void test_checkIfCompositionType() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkIfCompositionType(1));
        //
        Assert.assertFalse(FieldFormat.checkIfCompositionType(0));
        Assert.assertFalse(FieldFormat.checkIfCompositionType(-1));
        Assert.assertFalse(FieldFormat.checkIfCompositionType(10));
        Assert.assertFalse(FieldFormat.checkIfCompositionType(100));
        Assert.assertFalse(FieldFormat.checkIfCompositionType(1000));
        Assert.assertFalse(FieldFormat.checkIfCompositionType(10000));
        Assert.assertFalse(FieldFormat.checkIfCompositionType(-10));
        Assert.assertFalse(FieldFormat.checkIfCompositionType(-100));
        Assert.assertFalse(FieldFormat.checkIfCompositionType(-1000));
        Assert.assertFalse(FieldFormat.checkIfCompositionType(-10000));
    }

    @Test
    public void test_checkCompanyKey() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkCompanyKey("B1AFC07474C37C5AEC4199ED28E09705"));
        Assert.assertTrue(FieldFormat.checkCompanyKey("12345678123456781234567812345678"));

        Assert.assertFalse(FieldFormat.checkCompanyKey("123456781234567812345678123456789"));
        Assert.assertFalse(FieldFormat.checkCompanyKey("12345678123456781234567812345678E"));
        Assert.assertFalse(FieldFormat.checkCompanyKey("6789"));
        Assert.assertFalse(FieldFormat.checkCompanyKey(" "));
        Assert.assertFalse(FieldFormat.checkCompanyKey("123 456781234567812345678123456789"));
        Assert.assertFalse(FieldFormat.checkCompanyKey("中国56781234567812345678123456789"));
        Assert.assertFalse(FieldFormat.checkCompanyKey(null));
    }

    @Test
    public void test_checkSubmchid() {// 子商户号是长度为10位的数字
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkSubmchid("2312465124"));
        Assert.assertTrue(FieldFormat.checkSubmchid(null));
        Assert.assertTrue(FieldFormat.checkSubmchid(""));

        Assert.assertFalse(FieldFormat.checkSubmchid("0"));
        Assert.assertFalse(FieldFormat.checkSubmchid("abc"));
        Assert.assertFalse(FieldFormat.checkSubmchid("12345678999"));
    }

    @Test
    public void test_checkBusinessLicenseSN() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("123456789012345"));
        Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("333456789012345"));
        Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("123456789012345123"));
        Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("333C456B789A345"));
        Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("12345ABC9012345123"));
        Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("ASDFGHJKLQWERTY"));
        Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("ASDFGHJKLKJHGFDSAQ"));

        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("-333456789012345"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("-33345678901234"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("1234567891234"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("12345678912345678"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("123456789123456789123456"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN(" "));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("-11"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("11.1"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("fsjojsfojsfsojef"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93yhw2923"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93yhw 2923 "));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN(" f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93 yhw 2923"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93y你好hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93y博昕科技hw中国2 923"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN(",./2923"));
        Assert.assertFalse(FieldFormat.checkBusinessLicenseSN(null));
    }

    @Test
    public void test_checkProviderMobile() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkProviderMobile("13660086877"));

        Assert.assertFalse(FieldFormat.checkProviderMobile("-3334 "));
        Assert.assertFalse(FieldFormat.checkProviderMobile(" -9012"));
        Assert.assertFalse(FieldFormat.checkProviderMobile(" "));
        Assert.assertFalse(FieldFormat.checkProviderMobile("-11"));
        Assert.assertFalse(FieldFormat.checkProviderMobile("11.1"));
        Assert.assertFalse(FieldFormat.checkProviderMobile("fjsfsojefsadijfh34875y0398y93*&*"));
        Assert.assertFalse(FieldFormat.checkProviderMobile("f93yh"));
        Assert.assertFalse(FieldFormat.checkProviderMobile("你好中国2"));
        Assert.assertFalse(FieldFormat.checkProviderMobile("博昕科技"));
        Assert.assertFalse(FieldFormat.checkProviderMobile(",./2"));
    }

    @Test
    public void test_checkCompanyAppField() {
        Shared.printTestMethodStartInfo();
        Assert.assertTrue(FieldFormat.checkCompanyAppField("12213","3213"));
        Assert.assertTrue(FieldFormat.checkCompanyAppField("dsafds","fdsafdsa"));
        Assert.assertTrue(FieldFormat.checkCompanyAppField("%$^#^$#","^$^#%$#%"));
        Assert.assertTrue(FieldFormat.checkCompanyAppField("12213dsafds","3213dsafds"));
        Assert.assertTrue(FieldFormat.checkCompanyAppField("12213%$^#^$#","3213%$^#^$#"));
        Assert.assertTrue(FieldFormat.checkCompanyAppField("12213dsafds%$^#^$#","3213%$^#^$#fdsafdsa"));
        Assert.assertTrue(FieldFormat.checkCompanyAppField("12212546333333333333333453","32546877777777777783"));
        Assert.assertTrue(FieldFormat.checkCompanyAppField("122255555555555555555555555555548888888888613","3248888888888888888888888888613"));
        Assert.assertTrue(FieldFormat.checkCompanyAppField("",""));
        Assert.assertTrue(FieldFormat.checkCompanyAppField("",null));
        Assert.assertTrue(FieldFormat.checkCompanyAppField(null,""));
        Assert.assertTrue(FieldFormat.checkCompanyAppField(null,null));
        //
        Assert.assertFalse(FieldFormat.checkCompanyAppField("","3213"));
        Assert.assertFalse(FieldFormat.checkCompanyAppField("","fdsafdsa"));
        Assert.assertFalse(FieldFormat.checkCompanyAppField(null,"^$^#%$#%"));
        Assert.assertFalse(FieldFormat.checkCompanyAppField(null,"3213dsafds"));
    }

    @Test
    public void test_checkCompanyName() {// 中英文数字，[1, 12]字符
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkCompanyName("abcdabcdabcd"));
        Assert.assertTrue(FieldFormat.checkCompanyName("abcdab中国"));
        Assert.assertTrue(FieldFormat.checkCompanyName("a"));
        Assert.assertTrue(FieldFormat.checkCompanyName("中"));
        Assert.assertTrue(FieldFormat.checkCompanyName("876543ytyt"));
        Assert.assertTrue(FieldFormat.checkCompanyName("876543"));
        Assert.assertTrue(FieldFormat.checkCompanyName("123456789012"));
        //
        Assert.assertFalse(FieldFormat.checkCompanyName("abcdabcdabcde"));
        Assert.assertFalse(FieldFormat.checkCompanyName("abcdabcdabcd "));
        Assert.assertFalse(FieldFormat.checkCompanyName("1234567890123"));
        Assert.assertFalse(FieldFormat.checkCompanyName("_"));
        Assert.assertFalse(FieldFormat.checkCompanyName("adfs+m"));
        Assert.assertFalse(FieldFormat.checkCompanyName(""));
        Assert.assertFalse(FieldFormat.checkCompanyName(null));
    }

    @Test
    public void test_checkDBUserName() {// 数字、字母和下划线的组合，但首字符必须是字母，不能出现空格。[1, 20]字符
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkDBUserName("abcdabcdabcdabcdabcd"));
        Assert.assertTrue(FieldFormat.checkDBUserName("nbr_test_xxx"));
        Assert.assertTrue(FieldFormat.checkDBUserName("nbr_test_xxx9"));

        Assert.assertFalse(FieldFormat.checkDBUserName("abcdabcdabcdabcdabcd1"));
        Assert.assertFalse(FieldFormat.checkDBUserName("1bcdabcdabcdabcdabcd"));
        Assert.assertFalse(FieldFormat.checkDBUserName("nbr_test_xxx "));
        Assert.assertFalse(FieldFormat.checkDBUserName("1nbr_test_xxx"));
        Assert.assertFalse(FieldFormat.checkDBUserName("中nbr_test_xxx"));
        Assert.assertFalse(FieldFormat.checkDBUserName(""));
        Assert.assertFalse(FieldFormat.checkDBUserName(null));
        Assert.assertFalse(FieldFormat.checkDBUserName("nbr_ss w"));
    }

    @Test
    public void test_checkPassword() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkPassword("123456"));
        Assert.assertTrue(FieldFormat.checkPassword("12345678"));
        Assert.assertTrue(FieldFormat.checkPassword("abcedefds"));
        Assert.assertTrue(FieldFormat.checkPassword("你你你你你你"));
        Assert.assertTrue(FieldFormat.checkPassword("1234567890123456"));
        //
        Assert.assertFalse(FieldFormat.checkPassword(null));
        Assert.assertFalse(FieldFormat.checkPassword(""));
        Assert.assertFalse(FieldFormat.checkPassword("1"));
        Assert.assertFalse(FieldFormat.checkPassword("12"));
        Assert.assertFalse(FieldFormat.checkPassword("123"));
        Assert.assertFalse(FieldFormat.checkPassword("1234"));
        Assert.assertFalse(FieldFormat.checkPassword("12345"));
        Assert.assertFalse(FieldFormat.checkPassword("abcde"));
        Assert.assertFalse(FieldFormat.checkPassword("你你你你你"));
    }

    @Test
    public void test_checkShopID() {
        Shared.printTestMethodStartInfo();
        //
        Assert.assertTrue(FieldFormat.checkShopID(-1));
        Assert.assertTrue(FieldFormat.checkShopID(1));
        Assert.assertTrue(FieldFormat.checkShopID(10));
        Assert.assertTrue(FieldFormat.checkShopID(100));
        Assert.assertTrue(FieldFormat.checkShopID(1000));
        Assert.assertTrue(FieldFormat.checkShopID(10000));
        //
        Assert.assertFalse(FieldFormat.checkShopID(0));
        Assert.assertFalse(FieldFormat.checkShopID(-10));
        Assert.assertFalse(FieldFormat.checkShopID(-100));
        Assert.assertFalse(FieldFormat.checkShopID(-1000));
        Assert.assertFalse(FieldFormat.checkShopID(-10000));
    }

    @Test
    public void test_checkEmail() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkEmail("123@123.com"));
        Assert.assertTrue(FieldFormat.checkEmail("xxx@xx.com"));
        Assert.assertTrue(FieldFormat.checkEmail("123@xx.com"));
        Assert.assertTrue(FieldFormat.checkEmail("1234567890@qq.com"));

        Assert.assertFalse(FieldFormat.checkEmail("&$!$$$@xx.com"));
        Assert.assertFalse(FieldFormat.checkEmail("1234567890xx.com"));
        Assert.assertFalse(FieldFormat.checkEmail("1234567890@qqcom"));
        Assert.assertFalse(FieldFormat.checkEmail("1234567890@qq."));
        Assert.assertFalse(FieldFormat.checkEmail("邮箱@qq.com"));
    }

    @Test
    public void test_checkQQ() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkQQ("12345"));
        Assert.assertTrue(FieldFormat.checkQQ("12345678"));
        Assert.assertTrue(FieldFormat.checkQQ("1234567890"));

        Assert.assertFalse(FieldFormat.checkQQ("12345678901"));
        Assert.assertFalse(FieldFormat.checkQQ("dassafk"));
        Assert.assertFalse(FieldFormat.checkQQ(",/.@!%*%_"));
        Assert.assertFalse(FieldFormat.checkQQ("中文"));
        Assert.assertFalse(FieldFormat.checkQQ(""));
        Assert.assertFalse(FieldFormat.checkQQ(null));
        Assert.assertFalse(FieldFormat.checkQQ("     "));
    }

    @Test
    public void test_checkAccount() {
        Shared.printTestMethodStartInfo();

        Assert.assertTrue(FieldFormat.checkAccount("973456742923"));
        Assert.assertTrue(FieldFormat.checkAccount("fsjojsfojsfsojef"));
        Assert.assertTrue(FieldFormat.checkAccount("f93yhw2923"));

        Assert.assertFalse(FieldFormat.checkAccount(""));
        Assert.assertFalse(FieldFormat.checkAccount(",./2923"));
        Assert.assertFalse(FieldFormat.checkAccount("f93yhw 2923"));
        Assert.assertFalse(FieldFormat.checkAccount("f93yhw中国2923"));
        Assert.assertFalse(FieldFormat.checkAccount(null));
    }

    @Test
    public void test_checkCountOfBlankLineAtBottom() {
        Shared.printTestMethodStartInfo();
        // 范围0-5
        Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(0));
        Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(1));
        Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(2));
        Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(3));
        Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(4));
        Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(5));
        // 小于0
        Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(-1));
        Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(-22));
        Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(-333));
        Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(-4444));
        Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(-55555));
        // 大于5
        Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(6));
        Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(77));
        Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(888));
        Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(9999));
        Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(10000));
    }

    @Test
    public void test_checkRetailTradeRetrieveNBySN() {
        Shared.printTestMethodStartInfo();
        // 从app发出的请求，其queryKeyword代表SN，只根据SN来查询
        // 空串
        Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN(""));
        // 10-26位且以ls开头
        Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("ls201907130000100002"));
        // 10-26位且以LS开头
        Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("LS201907130000100002"));
        // 10-26位且不以LS开头
        Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("20190713000010000200"));
        // 26位且以LS开头，以_1结尾
        Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("LS2019071300001000021234_1"));
        // 26位且以LS开头，不以_1结尾
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("LS2019071300001000021234_2"));
        // 包含不是LS的英文字符
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("201902020101abc112348"));
        // 包含LS的英文字符但不在开头
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("201902020101LS112348"));
        // 中文
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("星巴克"));
        // 特殊字符
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("！@#￥（&（%"));
        // null
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN(null));
        // 长度小于10
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("20190"));
        // 超过26位
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("LS20190202010101000112348888"));
        // 首尾有空格
        Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN(" 12345678123456781 "));
    }
}
