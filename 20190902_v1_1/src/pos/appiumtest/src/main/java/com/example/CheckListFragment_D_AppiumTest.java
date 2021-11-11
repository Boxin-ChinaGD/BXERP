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
//import static io.appium.java_client.android.AndroidKeyCode.BACKSPACE;
//import static io.appium.java_client.android.AndroidKeyCode.KEYCODE_A;
//import static io.appium.java_client.android.AndroidKeyCode.META_CTRL_MASK;
//
///**
// * Created by WPNA on 2020/3/30.
// * 此测试类中，根据参照的手动测试文档为：MainActivity D： 暂无任何测试可以实现
// */
//
//public class CheckListFragment_D_AppiumTest {
//
////    //创建Capabilities
////    DesiredCapabilities capabilities = new DesiredCapabilities();
////
////    private AndroidDriver<AndroidElement> driver;
////
////    @Before
////    public void setUp() throws MalformedURLException, InterruptedException {
//////        File classpathRoot = new File(System.getProperty("user.dir"));
//////        //app的目录
//////        File appDir = new File(classpathRoot, "/src/main/java/apps/");
////        File appDir = new File("D:/BXERP/branches/20190902_v1_1/src/pos/appiumtest/src/main/java/apps/");
////        //app的名字，对应你apps目录下的文件
////        /*File app = new File(appDir, "app-dev-debug.apk");*/
////        File app = new File(appDir, "app-dev-debug.apk");
////        //支持中文输入
////        capabilities.setCapability("unicodeKeyboard", "True");
////        //设置要调试的真机的名字
//////        capabilities.setCapability("deviceName", "79ffaa78");
////        capabilities.setCapability("deviceName", "T105184T00010");
////        //设置真机的系统版本
//////        capabilities.setCapability("platformVersion", "6.0");
////        capabilities.setCapability("platformVersion", "7.1"); // 兼容6.0
////        //设置app的路径
////        capabilities.setCapability("app", app.getAbsolutePath());
////        //设置app的包名
////        capabilities.setCapability("appPackage", "com.bx.erp.dev");
////        //设置app的启动activity
////        capabilities.setCapability("appActivity", "com.bx.erp.view.activity.Login1Activity");
////        //启动driver
////        capabilities.setCapability("automationName", "uiautomator2");
////        capabilities.setCapability("noReset", true);
////        capabilities.setCapability("newCommandTimeout", 600);
////        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
////    }
////
////    @After
////    public void tearDown() throws Exception {
////        //测试完毕，关闭driver，不关闭将会导致会话还存在，下次启动就会报错
////        driver.quit();
////    }
////
////    /**
////     * 逐字删除编辑框中的文字
////     * 碰到问题：无论如何都删除不了密码输入框的内容
////     * 解决方法：不管有没有内容都 都在密码框进行回退按键删除
////     * @param element
////     *            文本框架控件
////     */
////    public void clearText(AndroidElement element) {
////        String text = element.getText();
////        // 跳到最后 新版中sendKeyEvent()已经被删除，用pressKeyCode()取代
////        // driver.pressKeyCode(KEYCODE_MOVE_END);定位到文本输入框最后
////        driver.pressKeyCode(KEYCODE_A, META_CTRL_MASK);//文本内容全选
////        int size = text.length() == 0 ? 50 : text.length();
////        for (int i = 0; i < size; i++) {
////            // 循环后退删除
////            driver.pressKeyCode(BACKSPACE);//删除
////        }
////
////    }
////
////    public void posLogin() throws InterruptedException {
////        //等待pos登录
////        Thread.sleep(10 * 1000);
////        //输入用户名和密码，点击登录，等待跳转
////        driver.findElementById("com.bx.erp.dev:id/username").sendKeys("15854320895");
////        driver.findElementById("com.bx.erp.dev:id/password").click();//点击密码框
////        clearText(driver.findElementById("com.bx.erp.dev:id/password"));//清除密码框的内容
////        driver.findElementById("com.bx.erp.dev:id/password").sendKeys("000000");//输入密码
////        driver.findElementById("com.bx.erp.dev:id/login").click();//点击登录
////        System.out.println("正在等待跳转至Base1FragmentActivity");
////        Thread.sleep(10*1000);
////        Assert.assertTrue("登录失败了！","com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
////        System.out.println("Staff Login Success");
////
////        //进入到MainActivity：设置准备金
////        driver.findElementById("com.bx.erp.dev:id/t2").click();
////        driver.findElementById("com.bx.erp.dev:id/t0").click();
////        driver.findElementById("com.bx.erp.dev:id/t0").click();
////        driver.findElementById("com.bx.erp.dev:id/confirm_reserve").click();
////        Thread.sleep(1000);
////
////    }
////
////    @Test
////    public void loginTest() throws InterruptedException, MalformedURLException {
////        // 重新安装apk
////        capabilities.setCapability("noReset", false);
////        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
////
////        //填写公司编号
////        Thread.sleep(5000);
////        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
////        Thread.sleep(3000);
////        driver.findElementById("com.bx.erp.dev:id/companysn").sendKeys("668866");
////        driver.findElementById("com.bx.erp.dev:id/input_companysn_submit").click();
////        Thread.sleep(5000);
////        //等待pos登录
////        posLogin();
////
////        //进入设置界面进行基础资料重置
////        Thread.sleep(1000);
////        driver.findElementById("com.bx.erp.dev:id/set_up").click(); //设置按钮
////        driver.findElementById("com.bx.erp.dev:id/data_processing").click();//数据处理按钮
////        driver.findElementById("com.bx.erp.dev:id/sync_base_data").click();//点击进行重置基础资料
////        Thread.sleep(80 * 1000);
////
////        // 该测试开始前把不重新安装App设为false，在这里设置回来。
////        capabilities.setCapability("noReset", true);
////    }
//
//    /**
//     * case 1-4 暂不需要
//     * case 5 - 7 ：很难在数据库中找到纯微信支付的单，appium无法完成销售商品并微信支付，故无法测试；
//     * case 8：切换app的操作appium无法完成；
//     */
//
//
//
//}
