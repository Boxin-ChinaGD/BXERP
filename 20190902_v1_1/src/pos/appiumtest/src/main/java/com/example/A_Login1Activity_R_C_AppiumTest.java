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
 * Created by WPNA on 2020/3/30.
 * 跑此测试前必须将公司的明文密码改为000000
 */

public class A_Login1Activity_R_C_AppiumTest extends BaseAppiumTest {

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
        while (!"com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity())) {
            Thread.sleep(1000);
        }
        System.out.println("Staff Login Success");

        //进入到MainActivity：设置准备金
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
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
        //    posLogin();

        // 该测试开始前把不重新安装App设为false，在这里设置回来。
        capabilities.setCapability("noReset", true);
    }

    @Test
    public void Login1Activity001() throws InterruptedException, MalformedURLException {
        System.out.println("--------------case:输入错误的公司编号进行登录");
        // 重新安装apk
        capabilities.setCapability("noReset", false);
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        //填写公司编号
        Thread.sleep(5000);
        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
        Thread.sleep(3000);
        driver.findElementById(pageName + ":id/companysn").sendKeys("8*-+)(");
        driver.findElementById(pageName + ":id/input_companysn_submit").click();
        Thread.sleep(5000);

        Assert.assertTrue("胡乱输入的公司编号居然可以进入！", driver.findElementById(pageName + ":id/companysn") != null);
    }

    @Test
    public void Login1Activity003() throws InterruptedException, MalformedURLException {
        System.out.println("------------------case:不输入公司编号，点击提交");

        // 重新安装apk
        capabilities.setCapability("noReset", false);
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        //填写公司编号
        Thread.sleep(5000);
        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
        Thread.sleep(3000);
        driver.findElementById(pageName + ":id/input_companysn_submit").click();
        Thread.sleep(5000);
        Assert.assertTrue("不输入公司编号居然可以进入！", driver.findElementById(pageName + ":id/companysn") != null);
    }

    @Test
    public void Login1Activity004() throws InterruptedException, MalformedURLException {
        System.out.println("------------------case:输入BX的编号，提示“该公司编号不存在”");

        // 重新安装apk
        capabilities.setCapability("noReset", false);
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        //填写公司编号
        Thread.sleep(5000);
        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
        Thread.sleep(3000);
        driver.findElementById(pageName + ":id/companysn").sendKeys("668867");
        driver.findElementById(pageName + ":id/input_companysn_submit").click();
        Thread.sleep(5000);
        Assert.assertTrue("输入BX的编号居然能进入！", driver.findElementById(pageName + ":id/companysn") != null);
    }

    /**
     * case5:已经无法输入空格，无法实现
     */

    @Test
    public void Login1Activity006() throws InterruptedException, MalformedURLException {
        System.out.println("------------------case:输入其他公司的公司编号");
        // 重新安装apk
        capabilities.setCapability("noReset", false);
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        //填写公司编号
        Thread.sleep(5000);
        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
        Thread.sleep(3000);
        driver.findElementById(pageName + ":id/companysn").sendKeys("668869");
        driver.findElementById(pageName + ":id/input_companysn_submit").click();
        Thread.sleep(5000);
        Assert.assertTrue("输入其他公司的公司编号居然能进入！", driver.findElementById(pageName + ":id/companysn") != null);
    }

    /**
     * 此case为02的，但是如果在测试前面输入正确的公司编号就再也不会有弹窗输入公司编号了，所以需要放在最后
     *
     * @throws InterruptedException
     * @throws MalformedURLException
     */
    @Test
    public void Login1Activity009() throws InterruptedException, MalformedURLException {
        System.out.println("--------------case:输入正确的公司编号进行登录");
        /**
         * 此测试需要手动将公司编号的密码更改为000000
         */
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
        Assert.assertTrue("输入正确的公司编号居然不能进入！", driver.findElementById(pageName + ":id/username") != null);
    }

    /**
     * case7:已经无法输入空格，无法实现
     * case8:无法实现，需要俩个pos机登录同个账号
     */

//----------------------  接下来为LoginActivity_C_appiumTest -------------------------------------------------
    /**
     * 将俩个类合并是为了不需要重新修改数据库中的POS密码
     * 下面的方法命名中，1xx中的xx为对应的测试文档的case
     */

    /**
     * 本测试类的登录账号为：18915460955
     * 测试前网络应为畅通；pos
     */

    /**
     * case:1-5 ：测试不通过为正常，因为需要手动关闭服务器；
     */
    //有网络没有连接服务器
    @Test
    public void Login1Activity101() throws InterruptedException {
        System.out.println("----------------------case：有网络没有连接服务器的情况下：显示未就绪");
        Thread.sleep(5 * 1000);
        Assert.assertTrue("状态不正常！", driver.findElementById(pageName + ":id/tv_posLogin_tip").getText().equals("未就绪"));
    }

    /**
     * case:1-5 ：测试不通过为正常，因为需要手动关闭服务器；
     */
    //有网络没有连接服务器
    @Test
    public void Login1Activity102() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：有网络没有连接服务器的情况下：输入店长账号登录，提示pos机登录失败");
        System.out.println("前提是断开服务器连接才能测试此case！");
        System.out.println("观察是否提示pos机登录失败！");

        Thread.sleep(5 * 1000);
        Assert.assertTrue("状态不正常！", driver.findElementById(pageName + ":id/tv_posLogin_tip").getText().equals("未就绪"));
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
    }

    /**
     * case:1-5 ：测试不通过为正常，因为需要手动关闭服务器；
     */
    //有网络没有连接服务器
    @Test
    public void Login1Activity103() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：有网络没有连接服务器的情况下：随意输入账号登录，提示pos机登录失败");
        System.out.println("前提是断开服务器连接才能测试此case！");
        System.out.println("观察是否提示pos机登录失败！");
        /**
         * 随意输入用户名和密码
         */
        Thread.sleep(5 * 1000);
        Assert.assertTrue("状态不正常！", driver.findElementById(pageName + ":id/tv_posLogin_tip").getText().equals("未就绪"));
        driver.findElementById(pageName + ":id/username").sendKeys("12345678912");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("123456");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
    }

    /**
     * case:1-5 ：测试不通过为正常，因为需要手动关闭服务器；
     */
    //有网络没有连接服务器
    @Test
    public void Login1Activity104() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：有网络没有连接服务器的情况下：随意输入账号登录，勾选记住面密码，提示pos机登录失败");
        System.out.println("前提是断开服务器连接才能测试此case！");
        System.out.println("观察是否提示pos机登录失败！");
        /**
         * 随意输入用户名和密码
         */
        Thread.sleep(5 * 1000);
        Assert.assertTrue("状态不正常！", driver.findElementById(pageName + ":id/tv_posLogin_tip").getText().equals("未就绪"));
        driver.findElementById(pageName + ":id/username").sendKeys("12345678912");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("123456");//输入密码
        driver.findElementById(pageName + ":id/remember_password").click();//点击记住密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
    }

    /**
     * case:1-5 ：测试不通过为正常，因为需要手动关闭服务器；
     */
    //有网络没有连接服务器
    @Test
    public void Login1Activity105() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：有网络没有连接服务器的情况下：随意输入账号登录，勾选记住面密码，提示pos机登录失败");
        System.out.println("前提是断开服务器连接才能测试此case！");
        System.out.println("观察是否提示请输入正确的手机号！");
        /**
         * 随意输入用户名和密码
         */
        Thread.sleep(5 * 1000);
        Assert.assertTrue("状态不正常！", driver.findElementById(pageName + ":id/tv_posLogin_tip").getText().equals("未就绪"));
        driver.findElementById(pageName + ":id/username").sendKeys("123456789@！12");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("123456@!");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
        Thread.sleep(1 * 1000);
        Assert.assertTrue("账号不一致！可能输入了符号", driver.findElementById(pageName + ":id/username").getText().toString().equals("12345678912"));
        //密码获取到的是空的，无法appium获取到
        //    Assert.assertTrue("密码不一致！可能无法输入符号", driver.findElementById(pageName+":id/password").getText().toString().equals("123456@!"));
    }

    @Test
    public void Login1Activity106() throws InterruptedException {
        System.out.println("----------------------case：有网络有连接服务器的情况下：显示已就绪");
        Thread.sleep(5 * 1000);
        Assert.assertTrue("状态不正常！", driver.findElementById(pageName + ":id/tv_posLogin_tip").getText().equals("已就绪"));
    }

    @Test
    public void Login1Activity107() throws InterruptedException {
        /**
         * 此测试需要手动将老板账号设置为首次登录才能通过此测试；所以注释掉；
         */

//        //等待POS登录
//        System.out.println("----------------------case：老板账号首次登录，会强制进行修改密码，修改完后再次点击登录，正常跳转");
//        System.out.println("需将老板账号设置为首次登录才能通过此测试");
//
//        Thread.sleep(5*1000);
//        driver.findElementById(pageName+":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName+":id/password").click();//点击密码框
//        clearText(driver.findElementById(pageName+":id/password"));//清除密码框的内容
//        driver.findElementById(pageName+":id/password").sendKeys("000000");//输入密码
//        driver.findElementById(pageName+":id/login").click();//点击登录
//        Thread.sleep(5*1000);
//        //输入新密码
//        driver.findElementById("android:id/button1").click();//确定按钮
//        driver.findElementById(pageName+":id/newpassword").click();
//        driver.findElementById(pageName+":id/newpassword").sendKeys("123456");
//        driver.findElementById(pageName+":id/confirmpassword").click();
//        driver.findElementById(pageName+":id/confirmpassword").sendKeys("123456");
//        driver.findElementById(pageName+":id/reset_password_submit").click();
//        Thread.sleep(5*1000);
//        //再次登录
//        driver.findElementById(pageName+":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName+":id/password").click();//点击密码框
//        clearText(driver.findElementById(pageName+":id/password"));//清除密码框的内容
//        driver.findElementById(pageName+":id/password").sendKeys("123456");//输入密码
//        driver.findElementById(pageName+":id/login").click();//点击登录
//
//        Thread.sleep(10*1000);
//        Assert.assertTrue("登录失败！","com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    @Test
    public void Login1Activity108() throws InterruptedException {
        /**
         * 此测试需要手动将员工账号设置为首次登录才能通过此测试；所以注释掉；
         */

//        //等待POS登录
//        System.out.println("----------------------case：员工账号首次登录，会强制进行修改密码，修改完后再次点击登录，正常跳转");
//        System.out.println("需将员工账号设置为首次登录才能通过此测试");
//        /**
//         * 随意输入用户名和密码
//         */
//        Thread.sleep(5*1000);
//        driver.findElementById(pageName+":id/username").sendKeys("13144496272");
//        driver.findElementById(pageName+":id/password").click();//点击密码框
//        clearText(driver.findElementById(pageName+":id/password"));//清除密码框的内容
//        driver.findElementById(pageName+":id/password").sendKeys("000000");//输入密码
//        driver.findElementById(pageName+":id/login").click();//点击登录
//        Thread.sleep(5*1000);
//        //输入新密码
//        driver.findElementById("android:id/button1").click();//确定按钮
//        driver.findElementById(pageName+":id/newpassword").click();
//        driver.findElementById(pageName+":id/newpassword").sendKeys("123456");
//        driver.findElementById(pageName+":id/confirmpassword").click();
//        driver.findElementById(pageName+":id/confirmpassword").sendKeys("123456");
//        driver.findElementById(pageName+":id/reset_password_submit").click();
//        Thread.sleep(5*1000);
//        //再次登录
//        driver.findElementById(pageName+":id/username").sendKeys("13144496272");
//        driver.findElementById(pageName+":id/password").click();//点击密码框
//        clearText(driver.findElementById(pageName+":id/password"));//清除密码框的内容
//        driver.findElementById(pageName+":id/password").sendKeys("123456");//输入密码
//        driver.findElementById(pageName+":id/login").click();//点击登录
//
//        Thread.sleep(10*1000);
//        Assert.assertTrue("登录失败！","com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    /**
     * 1.点击输入框输入账号密码
     * 2.勾选记住密码
     * 3.点击登录功能
     */
    @Test
    public void Login1Activity109() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：输入员工账号和手机号，点击记住密码，正常跳转");

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/remember_password").click();//点击记住密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(10 * 1000);
        Assert.assertTrue("登录失败！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    /**
     * case 10已被摒弃
     */

    @Test
    public void Login1Activity111() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：只输入员工手机号，不输入密码，点击登录");
        System.out.println("观察是否弹出：密码格式不正确,请输入6-16位的字符,首尾不能有空格");
        /**
         * 随意输入用户名和密码
         */
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/login").click();//点击登录
        Thread.sleep(2 * 1000);
    }

    @Test
    public void Login1Activity112() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：不输入员工手机号，只输入密码，点击登录");
        System.out.println("观察是否弹出请输入正确手机号");
        /**
         * 随意输入用户名和密码
         */
        Thread.sleep(5 * 1000);
        clearText(driver.findElementById(pageName + ":id/username"));
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
        //此处获取到的是hint的内容，所以要断言时用的是equals("用户名/手机")
        Assert.assertTrue("账号居然不为空！", driver.findElementById(pageName + ":id/username").getText().equals("用户名/手机"));
        Thread.sleep(2 * 1000);

    }

    @Test
    public void Login1Activity113() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：输入员工账号和手机号，正常跳转");

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(10 * 1000);
        Assert.assertTrue("登录失败！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    @Test
    public void Login1Activity114() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：输入错误的手机号，弹出提示：账号或密码错误");

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("11111111111");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(10 * 1000);
        Assert.assertFalse("登录居然成功了！重大BUG！！！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    /**
     * case 15与case 11相同
     * case16与case 12 相同
     * case17暂时无法实现，需要添加离职员工
     */

    @Test
    public void Login1Activity118() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：输入不正确的密码进行登录，弹出提示：账号或密码错误");

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("111111");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(10 * 1000);
        Assert.assertFalse("登录居然成功了！重大BUG！！！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    @Test
    public void Login1Activity119() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：输入不规范的密码进行登录，弹出提示：密码格式不正确,请输入6-16位的字符,首尾不能有空格");

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("00000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(10 * 1000);
        Assert.assertFalse("登录居然成功了！重大BUG！！！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    @Test
    public void Login1Activity120() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：无网络情况下：输入账号密码，进入主页面");
        System.out.println("此测试需测试13先运行成功，此测试才能成功");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(2 * 1000);
        driver.findElementById("android:id/button1").click();

        Thread.sleep(6 * 1000);
        Assert.assertTrue("登录失败！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
    }

    @Test
    public void Login1Activity121() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：无网络情况下：员工输入不存在的账号密码进行登录，弹出提示：找不到该用户，请重新输入");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("11111111111");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(6 * 1000);
        Assert.assertFalse("登录居然成功了！重大BUG！！！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
    }

    @Test
    public void Login1Activity122() throws InterruptedException {
        //等待POS登录
        System.out.println("----------------------case：无网络情况下：只输入账号，不输入密码，弹出提示：密码格式不正确,请输入6-16位的字符,首尾不能有空格");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("11111111111");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(6 * 1000);
        Assert.assertFalse("登录居然成功了！重大BUG！！！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
    }

    @Test
    public void Login1Activity123() throws InterruptedException {
        System.out.println("----------------------case：无网络情况下：员工只输入密码进行登录，弹出提示：请输入正确的手机号");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(6 * 1000);
        Assert.assertFalse("登录居然成功了！重大BUG！！！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
    }

    /**
     * case 24暂无法实现,需添加离职员工
     */

    @Test
    public void Login1Activity125() throws InterruptedException {
        System.out.println("----------------------case：无网络情况下：员工输入不正确的密码进行登录，弹出提示：账号或密码错误");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("111111");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(6 * 1000);
        Assert.assertFalse("登录居然成功了！重大BUG！！！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
    }

    @Test
    public void Login1Activity126() throws InterruptedException {
        System.out.println("----------------------case：无网络情况下：员工输入不规范的密码进行登录，弹出提示：密码格式不正确,请输入6-16位的字符,首尾不能有空格");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网

        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("00000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(6 * 1000);
        Assert.assertFalse("登录居然成功了！重大BUG！！！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    @Test
    public void Login1Activity127() throws InterruptedException {
        System.out.println("----------------------case：有网络情况下：SQLite中有当前pos信息时进行登录，观察当前状态，需要为已就绪状态");
        Thread.sleep(5 * 1000);
        Assert.assertTrue("状态不正常！", driver.findElementById(pageName + ":id/tv_posLogin_tip").getText().equals("已就绪"));
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
    }

    @Test
    public void Login1Activity128() throws InterruptedException {
        System.out.println("----------------------case：无网络情况下：登录POS，提示：pos登录失败");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        Assert.assertTrue("状态不正常！", driver.findElementById(pageName + ":id/tv_posLogin_tip").getText().equals("未就绪"));
    }

    /**
     * case 29:暂无法实现 需要pos清空SQLite表
     * case 30:暂无法实现 需要mysql中把该pos信息改为不可用状态
     */

    /**
     * 此测试为case 31；写成case 80 是因为此测试会影响其他的测试用例，所以放在最后
     *
     * @throws InterruptedException
     * @throws MalformedURLException
     */
    // @Test
    public void Login1Activity180() throws InterruptedException, MalformedURLException {
        System.out.println("--------------case:1.删除APP（清空表）2.运行APP ;有网络的情况下，服务器查找不到有关该SN的pos信息");

        // 重新安装apk
        capabilities.setCapability("noReset", false);
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        //填写公司编号
        Thread.sleep(5000);
        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
        Thread.sleep(3000);
        driver.findElementById(pageName + ":id/companysn").sendKeys("668877");
        driver.findElementById(pageName + ":id/input_companysn_submit").click();
        Thread.sleep(5000);
        Assert.assertTrue("假公司SN居然成功了！", driver.findElementById(pageName + ":id/companysn") != null);
        //以下为登录正常的公司SN，以免影响后续的测试
        clearText(driver.findElementById(pageName + ":id/companysn"));
        driver.findElementById(pageName + ":id/companysn").sendKeys("668866");
        driver.findElementById(pageName + ":id/input_companysn_submit").click();
        capabilities.setCapability("noReset", true);
    }

    /**
     * case 32,33,34已经被摒弃
     */

    @Test
    public void Login1Activity135() throws InterruptedException, MalformedURLException {
        System.out.println("----------------------case：有网登录POS，员工第一次断网登录");

        Thread.sleep(5 * 1000);//等待5秒，让POS登录成功
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);//等待5秒，让断网成功
        /**
         * 输入无登录过的员工账号，测试员工第一次断网登录
         */
        driver.findElementById(pageName + ":id/username").sendKeys("18915460959");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(6 * 1000);
        Assert.assertFalse("登录居然成功了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    @Test
    public void Login1Activity136() throws InterruptedException, MalformedURLException {
        System.out.println("----------------------case：无网登录POS，员工第一次断网登录");
        /**
         * 输入无登录过的员工账号，测试员工第一次断网登录
         */
        driver.findElementById(pageName + ":id/username").sendKeys("18915460959");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(6 * 1000);
        Assert.assertFalse("登录居然成功了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
    }

    /**
     * case 37 与 case 2重复；
     * case 38~42:售前账号登录，暂无法实现
     * case 43与case 14相同
     * case 44与case 18相同
     */

    @Test
    public void Login1Activity145() throws InterruptedException {
        System.out.println("----------------------case：格式不正确的用户名");
        System.out.println("弹出提示：请输入正确的手机号");
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("1891546095");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(10 * 1000);
        Assert.assertFalse("登录居然成功了！重大BUG！！！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    /**
     * case 46与case 19相同
     */

    @Test
    public void Login1Activity147() throws InterruptedException {
        System.out.println("----------------------case：格式不正确的用户名和密码");
        System.out.println("弹出提示：请输入正确的手机号");
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("1891546095");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("0000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录

        Thread.sleep(10 * 1000);
        Assert.assertFalse("登录居然成功了！重大BUG！！！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);//等待5秒，让断网成功
    }

    @Test
    public void Login1Activity149() throws InterruptedException {
        System.out.println("----------------------case：.断网进入到登录页面,联网点击登录");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/login").click();//点击登录
        Thread.sleep(15 * 1000);
        Assert.assertTrue("登录失败", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    /**
     * case 48无法在app启动之前断网
     * case 50与case 20相同
     */

    @Test
    public void Login1Activity151() throws InterruptedException {
        System.out.println("----------------------case：联网登录，断网交班，回到登录界面，再次联网登录");
        //登录账号
        //等待pos登录
        Thread.sleep(5 * 1000);
        //输入用户名和密码，点击登录，等待跳转
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
        System.out.println("正在等待跳转至Base1FragmentActivity");
        Thread.sleep(10 * 1000);
        Assert.assertTrue("登录失败了", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));

        //进入到MainActivity：设置准备金
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        //交班退出
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);
        //再次联网登录
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
        Thread.sleep(10 * 1000);
        Assert.assertTrue("登录失败了", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    /**
     * case 52 与case 13相同
     */

    @Test
    public void Login1Activity153() throws InterruptedException {
        System.out.println("------case:1.断开网络\n" +
                "2.运行app\n" +
                "3.输入正确账号密码\n" +
                "4.点击登录");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        //输入用户名和密码，点击登录，等待跳转
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
        Thread.sleep(3 * 1000);
        driver.findElementById("android:id/button1").click();//确定按钮
        Thread.sleep(6 * 1000);
        Assert.assertTrue("登录失败了", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    @Test
    public void Login1Activity154() throws InterruptedException {
        System.out.println("----------------------case：联网运行app输入账号密码，再断网登录");
        //再次联网登录
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        //输入用户名和密码，点击登录，等待跳转
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/login").click();//点击登录
        Thread.sleep(3 * 1000);
        driver.findElementById("android:id/button1").click();//确定按钮
        Thread.sleep(3 * 1000);
        Assert.assertTrue("登录失败了", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
    }

    @Test
    public void Login1Activity155() throws InterruptedException {
        System.out.println("-----------case:1.断开网络\n" +
                "2.运行app\n" +
                "3.输入正确账号密码\n" +
                "4.连接网络\n" +
                "5.点击登录");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        //输入用户名和密码，点击登录，等待跳转
        driver.findElementById(pageName + ":id/username").sendKeys("18915460955");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/login").click();//点击登录
        Thread.sleep(10 * 1000);
        Assert.assertTrue("登录失败了", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
    }

    /**
     * case 56~59 无法快速点击，点击了一下就响应了，再点击就找不到控件id了
     * case 60-71 app返回桌面了之后，暂无法重新打开app
     */

    @Test
    public void Login1Activity172() throws InterruptedException {
        System.out.println("----------------------case：输入过长的账号，达到最大长度后，不能输入任何字符");
        Thread.sleep(5 * 1000);

        driver.findElementById(pageName + ":id/username").sendKeys("18915460955123456789");
        Thread.sleep(1000);
        Assert.assertTrue("输入账号没有被限制为11位！", driver.findElementById(pageName + ":id/username").getText().equals("18915460955"));
    }

    @Test
    public void Login1Activity173() throws InterruptedException {
        System.out.println("----------------------case：输入过长的密码，达到最大长度后，不能输入任何字符");
        System.out.println("注意密码框的最后一个字符是否为7，是的话此测试不通过！");
        Thread.sleep(5 * 1000);

        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("12345678901234567");//输入密码
        Thread.sleep(1000);

    }

    /**
     * case 74与case 73相同
     */
    /**
     * 此方法将老板账号的密码重新修改为000000；以免影响其他测试类
     */

//    @Test
    public void setboxPassword() throws InterruptedException {
//        System.out.println("前提是将老板账号设置为首次登录");
//        Thread.sleep(5*1000);
//        driver.findElementById(pageName+":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName+":id/password").click();//点击密码框
//        clearText(driver.findElementById(pageName+":id/password"));//清除密码框的内容
//        driver.findElementById(pageName+":id/password").sendKeys("123456");//输入密码
//        driver.findElementById(pageName+":id/login").click();//点击登录
//        Thread.sleep(5*1000);
//        //输入新密码
//        driver.findElementById("android:id/button1").click();//确定按钮
//        driver.findElementById(pageName+":id/newpassword").click();
//        driver.findElementById(pageName+":id/newpassword").sendKeys("000000");
//        driver.findElementById(pageName+":id/confirmpassword").click();
//        driver.findElementById(pageName+":id/confirmpassword").sendKeys("000000");
//        driver.findElementById(pageName+":id/reset_password_submit").click();
//        Thread.sleep(5*1000);
    }
}
