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
// * 跑此测试前必须将公司的明文密码改为000000
// */
//
//public class Login1Activity_R_AppiumTest {
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
//        File appDir = new File("D:/BXERP/branches/20190902_v1_1/src/pos/appiumtest/src/main/java/apps/");
//        //app的名字，对应你apps目录下的文件
//        /*File app = new File(appDir, "app-dev-debug.apk");*/
//        File app = new File(appDir, "app-dev-debug.apk");
//        //支持中文输入
//        capabilities.setCapability("unicodeKeyboard", "True");
//        //设置要调试的真机的名字
////        capabilities.setCapability("deviceName", "79ffaa78");
//        capabilities.setCapability("deviceName", "T105184T00010");
//        //设置真机的系统版本
////        capabilities.setCapability("platformVersion", "6.0");
//        capabilities.setCapability("platformVersion", "7.1"); // 兼容6.0
//        //设置app的路径
//        capabilities.setCapability("app", app.getAbsolutePath());
//        //设置app的包名
//        capabilities.setCapability("appPackage", "com.bx.erp.dev");
//        //设置app的启动activity
//        capabilities.setCapability("appActivity", "com.bx.erp.view.activity.Login1Activity");
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
//    @Test
//    public void Login1Activity01() throws InterruptedException, MalformedURLException {
//        System.out.println("--------------case:输入错误的公司编号进行登录");
//        // 重新安装apk
//        capabilities.setCapability("noReset", false);
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//
//        //填写公司编号
//        Thread.sleep(5000);
//        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
//        Thread.sleep(3000);
//        driver.findElementById("com.bx.erp.dev:id/companysn").sendKeys("8*-+)(");
//        driver.findElementById("com.bx.erp.dev:id/input_companysn_submit").click();
//        Thread.sleep(5000);
//
//        Assert.assertTrue("胡乱输入的公司编号居然可以进入！",driver.findElementById("com.bx.erp.dev:id/companysn") != null);
//    }
//
//    @Test
//    public void Login1Activity03() throws InterruptedException, MalformedURLException {
//        System.out.println("------------------case:不输入公司编号，点击提交");
//
//        // 重新安装apk
//        capabilities.setCapability("noReset", false);
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//
//        //填写公司编号
//        Thread.sleep(5000);
//        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
//        Thread.sleep(3000);
//        driver.findElementById("com.bx.erp.dev:id/input_companysn_submit").click();
//        Thread.sleep(5000);
//        Assert.assertTrue("不输入公司编号居然可以进入！",driver.findElementById("com.bx.erp.dev:id/companysn") != null);
//    }
//
//    @Test
//    public void Login1Activity04() throws InterruptedException, MalformedURLException {
//        System.out.println("------------------case:输入BX的编号，提示“该公司编号不存在”");
//
//        // 重新安装apk
//        capabilities.setCapability("noReset", false);
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//
//        //填写公司编号
//        Thread.sleep(5000);
//        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
//        Thread.sleep(3000);
//        driver.findElementById("com.bx.erp.dev:id/companysn").sendKeys("668867");
//        driver.findElementById("com.bx.erp.dev:id/input_companysn_submit").click();
//        Thread.sleep(5000);
//        Assert.assertTrue("输入BX的编号居然能进入！",driver.findElementById("com.bx.erp.dev:id/companysn") != null);
//    }
//
//    /**
//     * case5:已经无法输入空格，无法实现
//     */
//
//    @Test
//    public void Login1Activity06() throws InterruptedException, MalformedURLException {
//        System.out.println("------------------case:输入其他公司的公司编号");
//        // 重新安装apk
//        capabilities.setCapability("noReset", false);
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//
//        //填写公司编号
//        Thread.sleep(5000);
//        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
//        Thread.sleep(3000);
//        driver.findElementById("com.bx.erp.dev:id/companysn").sendKeys("668869");
//        driver.findElementById("com.bx.erp.dev:id/input_companysn_submit").click();
//        Thread.sleep(5000);
//        Assert.assertTrue("输入其他公司的公司编号居然能进入！",driver.findElementById("com.bx.erp.dev:id/companysn") != null);
//    }
//
//    /**
//     * 此case为02的，但是如果在测试前面输入正确的公司编号就再也不会有弹窗输入公司编号了，所以需要放在最后
//     * @throws InterruptedException
//     * @throws MalformedURLException
//     */
//    @Test
//    public void Login1Activity09() throws InterruptedException, MalformedURLException {
//        System.out.println("--------------case:输入正确的公司编号进行登录");
//        /**
//         * 此测试需要手动将公司编号的密码更改为000000
//         */
//        // 重新安装apk
//        capabilities.setCapability("noReset", false);
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//
//        //填写公司编号
//        Thread.sleep(5000);
//        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
//        Thread.sleep(3000);
//        driver.findElementById("com.bx.erp.dev:id/companysn").sendKeys("668866");
//        driver.findElementById("com.bx.erp.dev:id/input_companysn_submit").click();
//        Thread.sleep(5000);
//        Assert.assertTrue("输入正确的公司编号居然不能进入！",driver.findElementById("com.bx.erp.dev:id/username") != null);
//    }
//
//    /**
//     * case7:已经无法输入空格，无法实现
//     * case8:无法实现，需要俩个pos机登录同个账号
//     */
//
//
//
//}
