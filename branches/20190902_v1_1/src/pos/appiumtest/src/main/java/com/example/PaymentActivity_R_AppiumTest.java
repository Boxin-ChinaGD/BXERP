package com.example;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import static io.appium.java_client.android.AndroidKeyCode.BACKSPACE;
import static io.appium.java_client.android.AndroidKeyCode.KEYCODE_A;
import static io.appium.java_client.android.AndroidKeyCode.META_CTRL_MASK;

/**
 * Created by WPNA on 2020/4/21.
 */

public class PaymentActivity_R_AppiumTest extends BaseAppiumTest {

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
        Thread.sleep(10 * 1000);
        //输入用户名和密码，点击登录，等待跳转
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
        System.out.println("正在等待跳转至Base1FragmentActivity");
        Thread.sleep(10*1000);

        Assert.assertTrue("登录失败了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        System.out.println("Staff Login Success");

        //进入到MainActivity：设置准备金
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
    }

    @Test
    public void paymentActivity01() throws Exception {
        System.out.println("-----------case:断网情况下现金收银，在结算页面断网");
        //等待POS登录
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(5 * 1000);
        Assert.assertTrue("支付失败了", driver.findElementById(pageName + ":id/balance_tv") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    @Test
    public void paymentActivity02() throws Exception {
        System.out.println("-----------case:断网情况下现金收银，在点击结算前断网");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        //等待POS登录
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(5 * 1000);
        Assert.assertTrue("支付失败了", driver.findElementById(pageName + ":id/balance_tv") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    @Test
    public void paymentActivity03() throws Exception {
        System.out.println("-----------case:断网情况下现金收银，在选择商品前断网");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        //等待POS登录
        posLogin();
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);

        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(5 * 1000);
        Assert.assertTrue("支付失败了", driver.findElementById(pageName + ":id/balance_tv") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    @Test
    public void paymentActivity04() throws Exception {
        System.out.println("-----------case:断网情况下现金收银，在登录前断网");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);

        //等待POS登录
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));
        driver.findElementById(pageName + ":id/password").sendKeys("000000");
        driver.findElementById(pageName + ":id/login").click();
        // 提示离线模式，点确定
        Thread.sleep(2 * 1000);
        driver.findElementById("android:id/button1").click();
        Thread.sleep(2 * 1000);
        //进入到MainActivity：设置准备金
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
        Thread.sleep(1000);

        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(5 * 1000);
        Assert.assertTrue("支付失败了", driver.findElementById(pageName + ":id/balance_tv") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    /**
     * case 5 与case 4 相同；case 5 是在APP运行前断网，现在只能做到在APP运行后断网
     * case 6-10：断网情况下微信收银，暂无法实现，需要扫码枪
     */

    @Test
    public void paymentActivity11() throws Exception {
        System.out.println("----------case:断网情况下现金支付一半取消支付，在点击取消支付前断网");
        System.out.println("观察是否提示收银员：请退还1元");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/t1").click();//点击数字键盘的1
        driver.findElementById(pageName + ":id/pay").click();

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        Thread.sleep(1000);
        driver.findElementById("android:id/button1").click();//确定取消
        Thread.sleep(3 * 1000);
        Assert.assertTrue("取消支付后返回主页面失败", driver.findElementById(pageName + ":id/balance_tv") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    @Test
    public void paymentActivity12() throws Exception {
        System.out.println("----------case:断网情况下现金支付一半取消支付，在支付金额前断网");
        System.out.println("观察是否提示收银员：请退还1元");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/t1").click();//点击数字键盘的1
        driver.findElementById(pageName + ":id/pay").click();

        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        Thread.sleep(1000);
        driver.findElementById("android:id/button1").click();//确定取消
        Thread.sleep(3 * 1000);
        Assert.assertTrue("取消支付后返回主页面失败", driver.findElementById(pageName + ":id/balance_tv") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    @Test
    public void paymentActivity13() throws Exception {
        System.out.println("----------case:断网情况下现金支付一半取消支付，在点击结算前断网");
        System.out.println("观察是否提示收银员：请退还1元");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/t1").click();//点击数字键盘的1
        driver.findElementById(pageName + ":id/pay").click();

        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        Thread.sleep(1000);
        driver.findElementById("android:id/button1").click();//确定取消
        Thread.sleep(3 * 1000);
        Assert.assertTrue("取消支付后返回主页面失败", driver.findElementById(pageName + ":id/balance_tv") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    @Test
    public void paymentActivity14() throws Exception {
        System.out.println("----------case:断网情况下现金支付一半取消支付，在登录前断网");
        System.out.println("观察是否提示收银员：请退还1元");

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        //输入用户名和密码，点击登录，等待跳转
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
        Thread.sleep(3 * 1000);
        driver.findElementById("android:id/button1").click();//确定按钮
        Thread.sleep(6 * 1000);
        Assert.assertTrue("登录失败了", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));

        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮

        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/t1").click();//点击数字键盘的1
        driver.findElementById(pageName + ":id/pay").click();

        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        Thread.sleep(1000);
        driver.findElementById("android:id/button1").click();//确定取消
        Thread.sleep(3 * 1000);
        Assert.assertTrue("取消支付后返回主页面失败", driver.findElementById(pageName + ":id/balance_tv") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    /**
     * case 15 与case 14 相同；case 15 是在APP运行前断网，现在只能做到在APP运行后断网
     * case 16-19：断网微信支付，暂无法完成
     */

    @Test
    public void paymentActivity20() throws Exception {
        System.out.println("----------case:断网情况下现金支付一半取消支付，在登录前断网");
        System.out.println("观察是否提示收银员：请退还xxx元");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮

        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/t5").click();//点击数字键盘的5
        driver.findElementById(pageName + ":id/t0").click();//点击数字键盘的0
        driver.findElementById(pageName + ":id/t0").click();//点击数字键盘的0
        driver.findElementById(pageName + ":id/pay").click();

        Assert.assertTrue("支付后返回主页面失败", driver.findElementById(pageName + ":id/balance_tv") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    /**
     * case 21:暂无法完成，需要微信支付
     * case 22：暂无法完成，需要微信支付和现金支付混合支付
     */


}
