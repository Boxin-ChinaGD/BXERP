//package com.example;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.remote.DesiredCapabilities;
//
//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.android.AndroidElement;
//
//public class DayEndRetailTradeAggregationTest {
//
//    //创建Capabilities
//    DesiredCapabilities capabilities = new DesiredCapabilities();
//
//    private AndroidDriver<AndroidElement> driver;
//
//    @Before
//    public void setUp() throws MalformedURLException, InterruptedException {
////        File classpathRoot = new File(System.getProperty("user.dir"));
////        //app的目录
////        File appDir = new File(classpathRoot, "/src/main/java/apps/");
//        File appDir = new File("D:/BXERP/branches/20190901_v1_0/src/pos/appiumtest/src/main/java/apps/");
//        //app的名字，对应你apps目录下的文件
//        /*File app = new File(appDir, "app-dev-debug.apk");*/
//        File app = new File(appDir, "app-dev-debug.apk");
//        //支持中文输入
//        capabilities.setCapability("unicodeKeyboard", "True");
//        //设置要调试的真机的名字
////        capabilities.setCapability("deviceName", "79ffaa78");
//        capabilities.setCapability("deviceName", "T202192H40228");
//        //设置真机的系统版本
////        capabilities.setCapability("platformVersion", "6.0");
//        capabilities.setCapability("platformVersion", "7.1"); // 兼容6.0
//        //设置app的路径
//        capabilities.setCapability("app", app.getAbsolutePath());
//        //设置app的包名
//        capabilities.setCapability("appPackage", "com.bx.erp.dev");
//        //设置app的启动activity
//        capabilities.setCapability("appActivity", "com.bx.erp.view.activity.LoginActivity");
//        //启动driver
//        capabilities.setCapability("automationName", "uiautomator2");
//        capabilities.setCapability("noReset", true);
//        capabilities.setCapability("newCommandTimeout", 600);
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        //测试完毕，关闭driver，不关闭将会导致会话还存在，下次启动就会报错
//        driver.quit();
//    }
//
//    /*
//    * 本测试不能与文件中的其它测试同时进行！！只能单独运行！！！！
//    * 测试目的：不允许某段时间内登录app（因为收银汇总正在该时间段内进行）
//    * 执行该测试的前提条件：
//    * 1. 在功能代码的Constants.java下找到一下语句：
//    * public static final String LOGIN_NotAllowed_TimeStart = "23:59:00";
//        public static final String LOGIN_NotAllowed_TimeEnd = "23:59:59";
//      2.将public static final String LOGIN_NotAllowed_TimeStart改为目前测试时间的5分钟后 与 public static final String LOGIN_NotAllowed_TimeEnd改为目前测试时间的10分钟后
//      3.重新build apk，将其改名后（app-dev-debug.apk）放入${pos_src_root_dir}\appiumtest\src\main\java\apps包下
//      4.运行appium，单独执行本测试
//    * */
//    @Test
//    public void TestNotAllowLoginAppInLimitedTime() throws MalformedURLException, InterruptedException {
//        // 重新安装apk
//        capabilities.setCapability("noReset", false);
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//        //点击权限框的允许
//        Thread.sleep(5000);
//        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();
//        //填写公司编号
//        Thread.sleep(3000);
//        driver.findElementById("com.bx.erp.dev:id/companysn").sendKeys("668866");
//        driver.findElementById("com.bx.erp.dev:id/input_companysn_submit").click();
//        Thread.sleep(5000);
//        //等待pos登录
//        Thread.sleep(5 * 1000);
//        driver.findElementById("com.bx.erp.dev:id/username").sendKeys("15854320895");
//        driver.findElementById("com.bx.erp.dev:id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById("com.bx.erp.dev:id/login").click();
//        System.out.println(driver.findElementByXPath("//*[@class='android.widget.Toast']").getText());
//        Thread.sleep(1 * 1000);
//        if ("com.bx.erp.view.activity.LoginActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(5 * 60 * 1000);//等待5mins(5mins后，一定会过了限制登录的时间)
//            driver.findElementById("com.bx.erp.dev:id/login").click();
//            if (!"com.bx.erp.view.activity.LoginActivity".equals(driver.currentActivity())) {
//                System.out.println("currentActivity:" + driver.currentActivity());
//                System.out.println("当过了禁止登陆的时间段，登录操作可以正常进行");
//            }
//        } else {
//            Assert.assertTrue("测试失败，在禁止登陆时间段内依然可以登录", false);//这里应该调用appium的出错接口什么的
//        }
//    }
//
//    /*
//    * 本测试不能与文件中的其它测试同时进行！！只能单独运行！！！！
//    * 测试目的：在接近第二天凌晨时，弹出跨天上班Dialog
//    * 执行该测试的前提条件：
//    * 1. 测试前把pos机的时间调为上一天的23:57：00
//      2.运行appium，单独执行该测试
//      3.当自动化测试成功运行到主页面时，观察在23：59：30 是否有弹出跨天上班消息框
//    * */
//    @Test
//    public void TestShowEndDayAlertDialog() throws MalformedURLException, InterruptedException {
//        // 重新安装apk
//        capabilities.setCapability("noReset", false);
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//        //点击权限框的允许
//        Thread.sleep(5000);
//        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();
//        //填写公司编号
//        Thread.sleep(5000);
//        driver.findElementById("com.bx.erp.dev:id/companysn").sendKeys("668866");
//        driver.findElementById("com.bx.erp.dev:id/input_companysn_submit").click();
//        Thread.sleep(5000);
//        //等待pos登录
//        Thread.sleep(10 * 1000);
//        driver.findElementById("com.bx.erp.dev:id/username").sendKeys("15854320895");
//        driver.findElementById("com.bx.erp.dev:id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById("com.bx.erp.dev:id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        Thread.sleep(5 * 60 * 1000);//等待5mins
//       /*测试观察跨天上班提示框是否在设定的时间内弹出*/
//    }
//}
