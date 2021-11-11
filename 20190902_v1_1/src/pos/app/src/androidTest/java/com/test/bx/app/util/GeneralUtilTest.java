package com.test.bx.app.util;

import com.alibaba.fastjson.JSON;
import com.base.BaseAndroidTestCase;
import com.bx.erp.utils.GeneralUtil;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;


public class GeneralUtilTest {

    @Test
    public void testGeneralUtil() {

        // 格式化2位小数 四舍五入
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToShow(1.1251d), GeneralUtil.formatToShow(1.1251d).equals("1.13"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToShow(1.1351d), GeneralUtil.formatToShow(1.1351d).equals("1.14"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToShow(1.1451d), GeneralUtil.formatToShow(1.1451d).equals("1.15"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToShow(1.2250d), GeneralUtil.formatToShow(1.2250d).equals("1.23"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToShow(1.2350d), GeneralUtil.formatToShow(1.2350d).equals("1.24"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToShow(1.2450d), GeneralUtil.formatToShow(1.2450d).equals("1.25"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToShow(1.22501d), GeneralUtil.formatToShow(1.22501d).equals("1.23"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToShow(1.23505d), GeneralUtil.formatToShow(1.23505d).equals("1.24"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToShow(1.24508d), GeneralUtil.formatToShow(1.24508d).equals("1.25"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToShow(-0.000001d), GeneralUtil.formatToShow(-0.000001d).equals("0.00"));


        // 格式化6位小数 不足补0
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToCalculate(4.4984566d), GeneralUtil.formatToCalculate(4.4984566d).equals("4.498457"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToCalculate(4.498d), GeneralUtil.formatToCalculate(4.498d).equals("4.498000"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToCalculate(4.49856d), GeneralUtil.formatToCalculate(4.49856d).equals("4.498560"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToCalculate(1.1d), GeneralUtil.formatToCalculate(1.1d).equals("1.100000"));
        Assert.assertTrue("格式化错误:" + GeneralUtil.formatToCalculate(-0.00000001d), GeneralUtil.formatToCalculate(-0.00000001d).equals("0.000000"));


        // 加法
        Assert.assertTrue("计算错误", GeneralUtil.sum(1.1d, 2.2d) == 3.3d);
        Assert.assertTrue("计算错误", GeneralUtil.sum(1.000001d, 2.2d) == 3.200001d);


        // 减法
        Assert.assertTrue("计算错误", GeneralUtil.sub(3.6d, 2.2d) == 1.4d);
        Assert.assertTrue("计算错误", GeneralUtil.sub(3.300001d, 2.2d) == 1.100001d);

        // 乘法
        Assert.assertTrue("计算错误", GeneralUtil.mul(3.6d, 2.2d) == 7.92d);
        Assert.assertTrue("计算错误", GeneralUtil.mul(3.30001d, 2.2d) == 7.260022d);

        // 除法
        Assert.assertTrue("计算错误", GeneralUtil.div(6.3d, 2.1d, 6) == 3d);
        Assert.assertTrue("计算错误", GeneralUtil.div(3.6666, 2d, 6) == 1.8333d);

    }

    @Test
    public void testFastJsonParse() {
        Map<String, String> microPayData = null;

        String rsp = "";
        microPayData = (Map<String, String>) JSON.parse(rsp);
        System.out.println("microPayData=" + microPayData);

        rsp = null;
        microPayData = (Map<String, String>) JSON.parse(rsp);
        System.out.println("microPayData=" + microPayData);

        try {
            rsp = "f8f2";
            microPayData = (Map<String, String>) JSON.parse(rsp);
            System.out.println("microPayData=" + microPayData);
        } catch (Exception e) {
        }

        rsp = "{\"a\" : 3}";
        microPayData = (Map<String, String>) JSON.parse(rsp);
        System.out.println("microPayData=" + microPayData);
        System.out.println(microPayData.get("xxxxx"));
    }

    @Test
    public void testRound() {
        BaseAndroidTestCase.caseLog("case1:测试double精确到一位小数能否五入");
        System.out.println("501.05-->" + GeneralUtil.round(501.05d, 1));
        Assert.assertTrue("计算错误", GeneralUtil.round(501.05d, 1) == 501.1d);

        BaseAndroidTestCase.caseLog("case2:测试double精确到两位小数能否五入");
        System.out.println("501.005-->" + GeneralUtil.round(501.005d, 2));
        Assert.assertTrue("计算错误", GeneralUtil.round(501.005d, 2) == 501.01d);

        BaseAndroidTestCase.caseLog("case3:测试String精确到两位小数能否四舍");
        System.out.println("501.004-->" + GeneralUtil.round("501.004", 2));
        Assert.assertTrue("计算错误", GeneralUtil.round("501.004", 2) == 501.00d);

        BaseAndroidTestCase.caseLog("case4:测试String精确到两位小数能否五入");
        System.out.println("501.005-->" +GeneralUtil.round("501.005", 2));
        Assert.assertTrue("计算错误", GeneralUtil.round("501.005", 2) == 501.01d);
    }

    @Test
    public void testHasDuplicatedElement(){
        BaseAndroidTestCase.caseLog("case1:测试数组中无重复数据");
        Integer[] iID = new Integer[2];
        iID[0] = 1;
        iID[1] = 2;
        Assert.assertTrue("有重复数据", !GeneralUtil.hasDuplicatedElement(iID));

        BaseAndroidTestCase.caseLog("case2:测试数组中有重复数据");
        iID[0] = 1;
        iID[1] = 1;
        Assert.assertTrue("无重复数据", GeneralUtil.hasDuplicatedElement(iID));
    }
}
