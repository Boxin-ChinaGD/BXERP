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

import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import static io.appium.java_client.android.AndroidKeyCode.BACKSPACE;
import static io.appium.java_client.android.AndroidKeyCode.KEYCODE_A;
import static io.appium.java_client.android.AndroidKeyCode.META_CTRL_MASK;

/**
 * Created by WPNA on 2020/3/25.
 * 参照的手动测试文档为： RetailTradeAggregation1Activity_C_AppiumTest
 */

public class RetailTradeAggregation1Activity_C_AppiumTest extends BaseAppiumTest {

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

    // @Test
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

    @Test
    public void RetailTradeAggregation1Activity01() throws InterruptedException, MalformedURLException {
        System.out.println("---------------case:准备金为0的时候,正确打印并跳转到登录页面");
        //等待pos登录
        Thread.sleep(10 * 1000);
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
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
        Thread.sleep(1000);

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);
        Assert.assertTrue("返回登录页面失败了！", "com.bx.erp.view.activity.Login1Activity".equals(driver.currentActivity()));
    }

    @Test
    public void RetailTradeAggregation1Activity02() throws InterruptedException, MalformedURLException {
        System.out.println("---------------case:准备金为100的时候,正确打印并跳转到登录页面");
        //等待pos登录
        Thread.sleep(10 * 1000);
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
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
        Thread.sleep(1000);

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);
        Assert.assertTrue("返回登录页面失败了！", "com.bx.erp.view.activity.Login1Activity".equals(driver.currentActivity()));
    }

    @Test
    public void RetailTradeAggregation1Activity03() throws InterruptedException, MalformedURLException {
        System.out.println("---------------case:准备金为10000的时候,正确打印并跳转到登录页面");
        //等待pos登录
        Thread.sleep(10 * 1000);
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
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
        Thread.sleep(1000);

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);
        Assert.assertTrue("返回登录页面失败了！", "com.bx.erp.view.activity.Login1Activity".equals(driver.currentActivity()));
    }

    /**
     * case 4:需要有微信收入，暂无法完成
     * case 5-8：需要有支付宝收入，暂无法完成
     */
    @Test
    public void RetailTradeAggregation1Activity09() throws InterruptedException, MalformedURLException {
        System.out.println("---------------case:.登陆成功，选择打印交班表数量");
        //等待pos登录
        posLogin();

        //交班退出
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/shift_schedule_number").click();//点击选择打印数量的spinner
        Thread.sleep(1000);
        driver.findElementsById(pageName + ":id/textview").get(2).click();//点击打印3张
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);
        Assert.assertTrue("返回登录页面失败了！", "com.bx.erp.view.activity.Login1Activity".equals(driver.currentActivity()));
    }

    /**
     * case 10:订单数量为负数,暂无法实现
     * case 11：订单数量跟实际的零售数量不相同，暂无法实现
     * case 12：上班时间晚于交班时间，暂无法实现
     * case 13：准备金为负数，暂无法实现
     * case 14-23：暂无法实现
     */

    @Test
    public void RetailTradeAggregation1Activity24() throws InterruptedException, MalformedURLException {
        System.out.println("---------------case:进入交班页面后点击返回键");
        //等待pos登录
        posLogin();
        //交班退出
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName+":id/back").click();//返回按钮
        driver.pressKeyCode(4);//返回键
        Thread.sleep(2 * 1000);
        Assert.assertTrue("返回销售页面失败了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));

        //断网测试
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        //交班退出
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.pressKeyCode(4);//返回键
        Thread.sleep(2 * 1000);
        Assert.assertTrue("返回销售页面失败了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    @Test
    public void RetailTradeAggregation1Activity25() throws InterruptedException, MalformedURLException {
        System.out.println("---------------case:在交班期间点击返回键");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        //等待pos登录
        posLogin();
        //交班退出
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(500);
        driver.pressKeyCode(4);//返回键
        Thread.sleep(5 * 1000);
        Assert.assertFalse("居然返回销售页面了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));

        //------------------断网测试
        //等待pos登录
        posLogin();
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        //交班退出
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(500);
        driver.pressKeyCode(4);//返回键
        Thread.sleep(5 * 1000);
        Assert.assertFalse("居然返回销售页面了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
    }

    /**
     * case 26：交班完成后关闭零售同步线程，暂无法完成
     */


}
