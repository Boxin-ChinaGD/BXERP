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
import java.util.List;

import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import static io.appium.java_client.android.AndroidKeyCode.BACKSPACE;
import static io.appium.java_client.android.AndroidKeyCode.KEYCODE_A;
import static io.appium.java_client.android.AndroidKeyCode.META_CTRL_MASK;

/**
 * Created by WPNA on 2020/4/1.
 */

public class SyncData1Activity_R_AppiumTest extends BaseAppiumTest {

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
        Thread.sleep(15 * 1000);
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

        // 该测试开始前把不重新安装App设为false，在这里设置回来。
        capabilities.setCapability("noReset", true);
    }

    @Test
    public void SyncData1Activity01() throws InterruptedException {
        System.out.println("-------case:登陆后跳转到同步页面，进行数据同步");
        //等待pos登录
        Thread.sleep(10 * 1000);
        //输入用户名和密码，点击登录，等待跳转
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
        Thread.sleep(2000);
        Assert.assertTrue("跳转同步页面失败了！", "com.bx.erp.view.activity.SyncData1Activity".equals(driver.currentActivity()));
    }

    @Test
    public void SyncData1Activity02() throws InterruptedException {
        System.out.println("-------case:断网情况下跳转到数据同步,提示“当前网络不可用");

        //断网
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));

        //等待POS登录
        Thread.sleep(10 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));
        driver.findElementById(pageName + ":id/password").sendKeys("000000");
        driver.findElementById(pageName + ":id/login").click();
        // 提示离线模式，点确定
        Thread.sleep(3 * 1000);
        driver.findElementById("android:id/button1").click();
        Thread.sleep(2 * 1000);
        Assert.assertTrue("登录失败了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));

    }

    @Test
    public void SyncData1Activity03() throws InterruptedException {
        System.out.println("----------case:断网情况下，在小票格式中创建一个小票格式,退出登录，重新登录,页面显示正在同步数据，" +
                "上传了本地临时的小票格式，进入到小票格式界面，已经存在。");
        //重新联网
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
        Thread.sleep(5 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 000);
        //断网
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));
        Thread.sleep(3 * 000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("页眉被新建");
        Thread.sleep(3 * 1000);

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);

        //重新联网
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 000);

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/printFormatVersion").click();//点击格式
        List<AndroidElement> list = driver.findElementsById(pageName + ":id/textview");//获取所有的格式
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                driver.findElementById(pageName + ":id/printFormatVersion").click();//点击格式
                driver.findElementsById(pageName + ":id/textview").get(i).click();
            } else
                list.get(i).click();

            if (driver.findElementById(pageName + ":id/design_header").getText().equals("页眉被新建")) {
                Assert.assertTrue(true);
                break;
            }

        }
    }

    /**
     * case 4 ：无法判断是否上传了临时零售单
     * case 5：无法判断是否同步了临时收银汇总
     * case 6：无法判断是否同步了POS需要的配置
     * case 7：需要nbr修改商品的信息，无法实现
     * case 8：需要在nbr离职员工，无法实现
     * case 9：nbr修改商品的信息，无法实现
     */

    @Test
    public void SyncData1Activity10() throws InterruptedException {
        System.out.println("-----------case:员工登陆成功，断网情况下跳转到数据同步");

        /**
         *  1.在点击登录按钮之前断开wifi连接
         2.点击登录按钮，进入到同步界面
         3.点击提示框除了“确定”按钮的地方
         4.点击提示框外的地方
         */
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(10 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));
        driver.findElementById(pageName + ":id/password").sendKeys("000000");

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        driver.findElementById(pageName + ":id/login").click();//点击登录
        //用滑动来代替点击确定按钮意外的地方
        // 提示离线模式
        Thread.sleep(2 * 1000);
        driver.swipe(1000, 800, 1000, 200, 200);
        Thread.sleep(1000);
        driver.findElementById("android:id/button1").click();
        Thread.sleep(2 * 1000);
        Assert.assertTrue("登录失败了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    /**
     * case 11：.断网进行登录，登录成功后有网进入同步界面，appium无法实现，因为它断网没那么快的
     * case 12：经过测试，appium无法开机同步，同步期间断网，因为反应太慢了
     * case 13：需要网页创建基础信息
     * case 14-15：切换app暂无法实现
     */


}
