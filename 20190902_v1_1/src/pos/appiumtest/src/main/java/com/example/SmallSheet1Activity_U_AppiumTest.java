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
 * Created by WPNA on 2020/4/1.
 */

public class SmallSheet1Activity_U_AppiumTest extends BaseAppiumTest {

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

    @Test
    public void SmallSheet1Activity01() throws InterruptedException {
        System.out.println("----------case:1.修改页眉 2.点击保存修改按钮");
        posLogin();

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);

        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("修改页眉");
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        Assert.assertTrue("修改页眉失败了！", driver.findElementById(pageName + ":id/design_header").getText().equals("修改页眉"));
    }

    @Test
    public void SmallSheet1Activity02() throws InterruptedException {
        System.out.println("----------case:1.将页眉设置为空值 2.点击保存修改按钮");
        posLogin();

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);

        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("");
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        Assert.assertTrue("修改页眉失败了！", driver.findElementById(pageName + ":id/design_header").getText().equals("小票内容"));
    }

    @Test
    public void SmallSheet1Activity03() throws InterruptedException {
        System.out.println("----------case:设置页脚1,设置页脚2 点击保存修改按钮");
        posLogin();

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);

        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        AndroidElement add = driver.findElementById(pageName + ":id/add_footer");
        add.click();//点击加号按钮添加页脚
        driver.findElementById(pageName + ":id/designFooter2").click();//点击第2个页脚
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/designFooter2").sendKeys("添加页脚2");// TODO: 2020/4/9 此处测试不过是因为原本的代码问题,没有字符时隐藏了
        driver.findElementById(pageName + ":id/design_footer1").click();//点击第21个页脚
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("修改页脚1");

        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        Assert.assertTrue("修改页脚失败了！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("修改页脚1"));

        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        Assert.assertTrue("没有展示页脚2或者跟之前的设置不符", driver.findElementById(pageName + ":id/design_footer2").getText().equals("添加页脚2"));
    }

    /**
     * case 4-5：增加logo，暂无法实现
     */

    @Test
    public void SmallSheet1Activity06_0() throws InterruptedException {
        System.out.println("--------case:在断网的情况下，修改小票格式");
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

        //进入到MainActivity：设置准备金
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
        System.out.println("Input reverse is " + reverseNumber);
        driver.findElementById(pageName + ":id/confirm_reserve").click();

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        driver.findElementById(pageName + ":id/design_header").click();//点击页眉
        driver.findElementById(pageName + ":id/design_header").sendKeys("修改页眉");//点击修改页眉
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        Assert.assertTrue("修改页眉失败了！", driver.findElementById(pageName + ":id/design_header").getText().equals("修改页眉"));
    }

    @Test
    public void SmallSheet1Activity06_1() throws InterruptedException {
        System.out.println("--------case:在联网情况下登录，进入小票格式，存在已修改的小票格式");
        /**
         * 此测试必须在06_0之后执行，这俩个是同个测试，但是因为需要重新登录，所以这样做
         */
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
    }

    /**
     * case 7 ：将不存在的小票id传到服务器，暂无法实现
     * case 8 ：上传到服务器的小票格式对象的主从表不匹配，暂无法实现
     * case 9 ：修改小票格式A的logo，修改小票格式B的logo为空，暂无法实现
     * case 10: 与SmallSheet1Activity_R_AppiumTest 09 相同；
     * case 11-13：存在小票格式logo，暂无法实现
     */


}
