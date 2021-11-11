package com.example;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import static io.appium.java_client.android.AndroidKeyCode.BACKSPACE;
import static io.appium.java_client.android.AndroidKeyCode.KEYCODE_A;
import static io.appium.java_client.android.AndroidKeyCode.META_CTRL_MASK;

/**
 * Created by WPNA on 2020/4/1.
 */

public class RetailTradeAggregation1Activity_R_AppiumTest extends BaseAppiumTest {

    /**
     * 逐字删除编辑框中的文字
     * 碰到问题：无论如何都删除不了密码输入框的内容
     * 解决方法：不管有没有内容都 都在密码框进行回退按键删除
     *
     * @param element 文本框架控件
     */
    public void clearText(AndroidElement element) {
        String text = element.getText();
        // 跳到最后 新版中sendKeyEvent()已经被删除，用pressKeyCode()取代
        // driver.pressKeyCode(KEYCODE_MOVE_END);定位到文本输入框最后
        driver.pressKeyCode(KEYCODE_A, META_CTRL_MASK);//文本内容全选
        int size = text.length() == 0 ? 50 : text.length();
        for (int i = 0; i < size; i++) {
            // 循环后退删除
            driver.pressKeyCode(BACKSPACE);//删除
        }

    }

    public void posLogin() throws InterruptedException {
        //等待pos登录
        Thread.sleep(15 * 1000);
        //输入用户名和密码，点击登录，等待跳转
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
        System.out.println("正在等待跳转至Base1FragmentActivity");
        Thread.sleep(10 * 1000);
        Assert.assertTrue("登录失败了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        System.out.println("Staff Login Success");

        //进入到MainActivity：设置准备金
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
        Thread.sleep(1000);

    }

    //@Test
    public void loginTest() throws InterruptedException, MalformedURLException {
        // 重新安装apk
        capabilities.setCapability("noReset", false);
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        //填写公司编号
        Thread.sleep(5000);
        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
        Thread.sleep(3000);
        driver.findElementById(pageName + ":id/companysn").sendKeys("668866");
        driver.findElementById(pageName + ":id/input_companysn_submit").click();
        Thread.sleep(5000);
        //等待pos登录
        posLogin();

        //进入设置界面进行基础资料重置
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);

        // 该测试开始前把不重新安装App设为false，在这里设置回来。
        capabilities.setCapability("noReset", true);
    }

    /**
     * case 1:需要微信支付，暂无法完成；
     * case 2:需要支付宝支付，暂无法完成
     */

    @Test
    public void RetailTradeAggregation1Activity03() throws InterruptedException, MalformedURLException {
        System.out.println("--------case:" +
                "1.成功登录，填写准备金\n" +
                "2.结算，只进行微信支付\n" +
                "3.退出\n" +
                "4.点击交班");
        /**
         *  交易单数显示结算的次数
         营业额显示所有的订单的总价的和
         准备金显示登陆成功后输入的金额
         现金收入的值等于营业额
         并且打印出交班表
         */
        posLogin();
        //添加商品，点击结账
        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567801");
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(4 * 1000);

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);

        Assert.assertTrue("交易单数不正确！", driver.findElementById(pageName + ":id/showTradeNo").getText().equals("1"));
        Assert.assertTrue("营业额显示不正确！", driver.findElementById(pageName + ":id/showAmount").getText().equals("5.68"));
        Assert.assertTrue("准备金显示不正确！", driver.findElementById(pageName + ":id/showReserveAmount").getText().equals("200.0"));
        Assert.assertTrue("现金收入的值不等于营业额", driver.findElementById(pageName + ":id/showCashAmount").getText().equals("5.68"));

        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);
    }

    /**
     * case 4:需要支付宝支付，暂无法完成
     * case 5 :需要微信支付，暂无法完成；
     * case 6-8 ：需要支付宝支付，暂无法完成
     */

    @Test
    public void RetailTradeAggregation1Activity09() throws InterruptedException, MalformedURLException {
        System.out.println("-------case:不进行收银，交班");
        posLogin();

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        Assert.assertTrue("交易单数不正确！", driver.findElementById(pageName + ":id/showTradeNo").getText().equals("0"));
        Assert.assertTrue("营业额显示不正确！", driver.findElementById(pageName + ":id/showAmount").getText().equals("0.00"));
        Assert.assertTrue("准备金显示不正确！", driver.findElementById(pageName + ":id/showReserveAmount").getText().equals("200.0"));
        Assert.assertTrue("现金收入的值不等于营业额", driver.findElementById(pageName + ":id/showCashAmount").getText().equals("0.00"));
        Assert.assertTrue("微信收入显示不正确！", driver.findElementById(pageName + ":id/showWechatAmount").getText().equals("0.00"));
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);
    }


    @Test
    public void RetailTradeAggregation1Activity10() throws InterruptedException, MalformedURLException {
        System.out.println("-------case:当销售3张零售单，无退货单，收银汇总时，查看销售笔数和收银次数是否相同");
        posLogin();

        for (int i = 0; i < 3; i++) {
            //添加商品，点击结账
            driver.findElementById(pageName + ":id/scan_barcode_search").click();
            driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567801");
            driver.findElementById(pageName + ":id/name_attr").click();
            driver.findElementById(pageName + ":id/add_tv").click();
            driver.findElementById(pageName + ":id/balance_tv").click();
            driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
            driver.findElementById(pageName + ":id/pay").click();
            Thread.sleep(4 * 1000);
        }

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        Assert.assertTrue("交易单数不正确！", driver.findElementById(pageName + ":id/showTradeNo").getText().equals("3"));

    }

    @Test
    public void RetailTradeAggregation1Activity11() throws InterruptedException, MalformedURLException {
        System.out.println("-------case:当销售3张零售单，1张退货单，收银汇总时，查看销售笔数和收银次数是否相同");
        posLogin();

        for (int i = 0; i < 3; i++) {
            //添加商品，点击结账
            driver.findElementById(pageName + ":id/scan_barcode_search").click();
            driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567801");
            driver.findElementById(pageName + ":id/name_attr").click();
            driver.findElementById(pageName + ":id/add_tv").click();
            driver.findElementById(pageName + ":id/balance_tv").click();
            driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
            driver.findElementById(pageName + ":id/pay").click();
            Thread.sleep(4 * 1000);
        }

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(2).click();//获取第一个结账的单
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号
        driver.findElementById(pageName + ":id/confirm_return_goods").click();//点击退货
        Thread.sleep(2 * 1000);

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        Assert.assertTrue("交易单数不正确！", driver.findElementById(pageName + ":id/showTradeNo").getText().equals("3"));
    }

    /**
     * case 12：不理解，无销售，怎么会有退货单
     * case 13：无法生成一张昨天的销售单进行退货，暂无法完成
     */

    @Test
    public void RetailTradeAggregation1Activity14() throws InterruptedException, MalformedURLException {
        System.out.println("-------case:当今天销售3单（A,B,C），对A进行部分退货。查看收银汇总是否正常。");
        posLogin();

        posLogin();

        for (int i = 0; i < 2; i++) {
            //添加商品，点击结账
            driver.findElementById(pageName + ":id/scan_barcode_search").click();
            driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567801");
            driver.findElementById(pageName + ":id/name_attr").click();
            driver.findElementById(pageName + ":id/add_tv").click();
            driver.findElementById(pageName + ":id/balance_tv").click();
            driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
            driver.findElementById(pageName + ":id/pay").click();
            Thread.sleep(4 * 1000);
        }

        //添加商品
        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567801");
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        //添加商品
        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567802");
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        //点击结账
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(4 * 1000);

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个结账的单
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号
        driver.findElementById(pageName + ":id/confirm_return_goods").click();//点击退货
        Thread.sleep(2 * 1000);

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        Assert.assertTrue("交易单数不正确！", driver.findElementById(pageName + ":id/showTradeNo").getText().equals("3"));
    }

    /**
     * case 15与case 11相同，不知是否是我理解有误
     * case 16-17：切换app，暂无法完成
     */


}
