package com.example;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * Created by WPNA on 2020/5/14.
 */

public class BaseAppiumTest {
    //创建Capabilities
    protected DesiredCapabilities capabilities = new DesiredCapabilities();

    protected AndroidDriver<AndroidElement> driver;

    /**
     * 在不同的场中跑测试时，如果打包的apk包名有更改，需要在此处修改
     */
    protected String pageName = "com.bx.erp.sitaa.dev";

    @Before
    public void setUp() throws MalformedURLException, InterruptedException {
//        File classpathRoot = new File(System.getProperty("user.dir"));
//        //app的目录
//        File appDir = new File(classpathRoot, "/src/main/java/apps/");
        File appDir = new File("D:/BXERP/branches/20190902_v1_1/src/pos/appiumtest/src/main/java/apps/");
        //app的名字，对应你apps目录下的文件
        /*File app = new File(appDir, "app-dev-debug.apk");*/
        File app = new File(appDir, "app-dev-debug.apk");
        //支持中文输入
        capabilities.setCapability("unicodeKeyboard", "True");
        //设置要调试的真机的名字
//        capabilities.setCapability("deviceName", "79ffaa78");
        capabilities.setCapability("deviceName", "T105184T00010");
        //设置真机的系统版本
//        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("platformVersion", "7.1"); // 兼容6.0
        //设置app的路径
        capabilities.setCapability("app", app.getAbsolutePath());
        //设置app的包名
        capabilities.setCapability("appPackage", pageName);
        //设置app的启动activity
        capabilities.setCapability("appActivity", "com.bx.erp.view.activity.Login1Activity");
        //启动driver
        capabilities.setCapability("automationName", "uiautomator2");
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("newCommandTimeout", 600);
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws Exception {
        //测试完毕，关闭driver，不关闭将会导致会话还存在，下次启动就会报错
        String currentActivity = driver.currentActivity();
        if (currentActivity.equals("com.bx.erp.view.activity.Base1FragmentActivity")) {
            try {
                if (driver.findElementById(pageName + ":id/confirm_reserve") != null) { //如果当前处于准备金页面
                    driver.findElementById(pageName + ":id/t2").click();
                    driver.findElementById(pageName + ":id/confirm_reserve").click();
                    logOut();
                    driver.quit();
                    return;
                }
            } catch (NoSuchElementException e) {
                //如果找不到id则表明当前不在准备金页面中,接下来检测是否是在支付页面
                try {
                    if (driver.findElementById(pageName + ":id/pay") != null) {  //如果当前是在支付页面，则需要点击取消支付
                        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
                        Thread.sleep(5 * 1000);
                        driver.findElementById("android:id/button1").click();//确定取消
                        Thread.sleep(2 * 1000);
                        logOut();
                        driver.quit();
                        return;
                    }
                }catch (NoSuchElementException e2) {
                    System.out.println("准备退出，检测到当前不处于准备金页面，也不处于支付页面");
                    logOut();
                    driver.quit();
                    return;
                }
            }
            logOut();
        } else if (currentActivity.equals("com.bx.erp.view.activity.RetailTradeAggregation1Activity")) {
            //防止当前点击了spinner而没关闭，先点击一下屏幕
//            driver.tap(1, 10, 10, 500);
            new TouchAction(driver).tap(10, 10).perform();
            //交班按钮
            driver.findElementById(pageName + ":id/change_shifts").click();
            Thread.sleep(6 * 1000);
        }else if (currentActivity.equals("com.bx.erp.view.activity.SyncData1Activity")){
            //如果当前处于同步页面，那比较大可能是同步失败卡在这里了，现在只能强行退出了
        }else if (currentActivity.equals("com.bx.erp.view.activity.Login1Activity")){

        }
        driver.quit();
    }

    public void logOut() throws InterruptedException {
        //在交班退出前为了以防当前处于弹窗等形式的页面中导致退出登录失败，先点击一下屏幕
        driver.tap(1, 10, 10, 500);
        new TouchAction(driver).tap(10, 10).perform();
        //交班退出
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(6 * 1000);
    }


}
