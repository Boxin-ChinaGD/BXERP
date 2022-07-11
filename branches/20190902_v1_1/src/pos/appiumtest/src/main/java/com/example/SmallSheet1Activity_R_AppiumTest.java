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

public class SmallSheet1Activity_R_AppiumTest extends BaseAppiumTest {

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

    private void chose_smallsheet(String o) throws InterruptedException {
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/printFormatVersion").click();//点击格式

        List<AndroidElement> list = driver.findElementsById(pageName + ":id/textview");//获取所有的格式
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().equals(o)) {//点击格式o
                list.get(i).click();
                break;
            }
        }
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
        System.out.println("----------case:点击进入“前台小票”，展示最新的小票格式");
        System.out.println("观察是否显示为最新的小票格式");
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
    }

    /**
     * case 2： 与 UI 类的case20相同
     */

    @Test
    public void SmallSheet1Activity03() throws InterruptedException {
        System.out.println("---------case:存在多个小票格式且小票格式中有部分有多个页脚数据,页脚行数不同，对应的小票1跟小票2展示的页脚数行以及数据都不同");
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        //修改一个小票格式的页脚为多个
        chose_smallsheet("格式 6");
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        AndroidElement add = driver.findElementById(pageName + ":id/add_footer");
        add.click();//点击加号按钮添加页脚
        driver.findElementById(pageName + ":id/designFooter2").click();//点击第2个页脚
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/designFooter2").sendKeys("添加页脚2");// TODO: 2020/4/9 此处测试不过是因为原本的代码设计问题
        add.click();//点击加号按钮添加页脚
        driver.findElementById(pageName + ":id/design_footer3").click();//点击第3个页脚
        driver.findElementById(pageName + ":id/design_footer3").sendKeys("添加页脚3");
        add.click();//点击加号按钮添加页脚
        driver.findElementById(pageName + ":id/design_footer4").click();//点击第4个页脚
        driver.findElementById(pageName + ":id/design_footer4").sendKeys("添加页脚4");
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        //切换到页脚比较少的格式
        System.out.println("观察页脚显示是否只有一个");
        chose_smallsheet("格式 1");
        Thread.sleep(4 * 1000);
    }

    /**
     * case 4:不理解该测试用例
     */

    @Test
    public void SmallSheet1Activity05() throws InterruptedException {
        System.out.println("---------case:1.进入“前台小票”2.点击版本号进行选择");
        System.out.println("观察是否展示格式二的内容");
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        chose_smallsheet("格式 2");
    }

    /**
     * case 6 ：与case 5 相同
     * case 7-8：需要创建一个新公司，暂无法实现
     */

    @Test
    public void SmallSheet1Activity09() throws InterruptedException {
        System.out.println("----------case:收银员登录，查看小票格式，提示无权进行操作");
        //等待pos登录
        Thread.sleep(10 * 1000);
        //输入用户名和密码，点击登录，等待跳转
        driver.findElementById(pageName + ":id/username").sendKeys("15016509167");
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
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
    }

    /**
     * case 10-12：已经被摒弃
     * case 13：需联合数据库进行检查测试是否完成，这里实现可能不合适
     */

    @Test
    public void SmallSheet1Activity14() throws InterruptedException {
        System.out.println("----------case:登陆前断网，设置当前使用小票格式，弹出提示：网络不可用");
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
        Thread.sleep(2 * 1000);
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
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
    }

    @Test
    public void SmallSheet1Activity15() throws InterruptedException {
        System.out.println("-------------case:在前台小票页面断网，设置当前使用小票格式,弹出提示：网络不可用");

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
        //断网
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
    }

    @Test
    public void SmallSheet1Activity16() throws InterruptedException {
        System.out.println("-------------case:始终有网的情况下，进入打印小票格式页面，点击同步，结果为同步成功");

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
        driver.findElementById(pageName + ":id/loading_config_data").click();//点击同步
        Thread.sleep(10 * 1000);
    }

    @Test
    public void SmallSheet1Activity17() throws InterruptedException {
        System.out.println("-------------case:始终有网的情况下，进入打印小票格式页面，点击同步，提示网络故障！");

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
        //断网
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/loading_config_data").click();//点击同步
        Thread.sleep(10 * 1000);
    }

    @Test
    public void SmallSheet1Activity18() throws InterruptedException {
        System.out.println("-------------case:始终无网的情况下，进入打印小票格式页面，点击同步，提示网络故障！");

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
        Thread.sleep(2 * 1000);
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
        driver.findElementById(pageName + ":id/loading_config_data").click();//点击同步
        Thread.sleep(4 * 1000);
    }

    /**
     * case 19--30：切换app，暂无法实现
     */


}
