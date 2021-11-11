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
 * Created by WPNA on 2020/3/24.
 * 本测试类根据ResetBaseDataActivity手动测试文档编写case
 * 本类登录账号：15854320895
 */

public class ResetBaseData1Activity_R_AppiumTest extends BaseAppiumTest {

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

    //    @Test
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

        // 该测试开始前把不重新安装App设为false，在这里设置回来。
        capabilities.setCapability("noReset", true);
    }

    @Test
    public void ResetBaseData1Activity01() throws InterruptedException, MalformedURLException {
        System.out.println("--------case:无网络情况下点击重置按钮");
        posLogin();

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        //进入设置界面进行基础资料重置
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(2 * 1000);
        Assert.assertTrue("断网下居然能重置基础资料！", driver.findElementById("android:id/button1") != null);
        driver.findElementById("android:id/button1").click();

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网,以免影响接下来的测试
        Thread.sleep(3 * 1000);
    }

    @Test
    public void ResetBaseData1Activity02() throws InterruptedException, MalformedURLException {
        System.out.println("--------case:有网络情况下点击重置按钮");
        System.out.println("需手动查看是否下载了默认小票格式");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网,以免影响接下来的测试
        Thread.sleep(3 * 1000);
        posLogin();
        //进入设置界面进行基础资料重置
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);
    }

    /**
     * case 3:暂无法完成，需要该账号被俩台POS机登录了
     * case 4：暂无法完成，需要设置nbr端session过期时长
     */

    @Test
    public void ResetBaseData1Activity05() throws InterruptedException, MalformedURLException {
        System.out.println("------------case: 断网登录，进行重置基础资料，提示网络故障");

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
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

        //进入设置界面进行基础资料重置
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Assert.assertTrue("断网下居然能重置基础资料！", driver.findElementById("android:id/button1") != null);
        driver.findElementById("android:id/button1").click();

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网,以免影响接下来的测试
        Thread.sleep(3 * 1000);
    }

    @Test
    public void ResetBaseData1Activity06() throws InterruptedException, MalformedURLException {
        System.out.println("------------case: 断网登录，重新联网进行重置基础资料，提示网络故障");

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
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

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        //进入设置界面进行基础资料重置
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Assert.assertTrue("断网下居然能重置基础资料！", driver.findElementById("android:id/button1") != null);
        driver.findElementById("android:id/button1").click();
    }

    @Test
    public void ResetBaseData1Activity07() throws InterruptedException, MalformedURLException {
        System.out.println("------------case: 有网登录，断网进行重置基础资料，提示网络故障");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        //进入设置界面进行基础资料重置
        Thread.sleep(3 * 1000);
        posLogin();
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        //进入设置界面进行基础资料重置
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Assert.assertTrue("断网下居然能重置基础资料！", driver.findElementById("android:id/button1") != null);
        driver.findElementById("android:id/button1").click();

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网,以免影响接下来的测试
        Thread.sleep(3 * 1000);
    }

    @Test
    public void ResetBaseData1Activity08() throws InterruptedException, MalformedURLException {
        System.out.println("------------case: 有网登录，进行重置基础资料期间断网，提示网络故障");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网,以免影响接下来的测试
        Thread.sleep(3 * 1000);
        posLogin();
        //进入设置界面进行基础资料重置
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(2 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(1000);
        Assert.assertTrue("居然能重置基础资料！", driver.findElementById(pageName + ":id/sync_base_data") != null);

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网,以免影响接下来的测试
        Thread.sleep(3 * 1000);
    }

    /**
     * case 9、10为切换app，暂无法完成
     */

}
