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
 * Created by WPNA on 2020/3/25.
 */

public class SmallSheet1Activity_UI_AppiumTest extends BaseAppiumTest {

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

    private void ClickSmallSheet(String o) throws InterruptedException {
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        chose_smallsheet(o);
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
        System.out.println("----------------case:开始进入，显示的是当前小票格式");
        System.out.println("观察小票格式是否是1，是的话测试通过");
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(2000);
        Assert.assertTrue("显示的居然不是小票格式1的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("小票"));
    }

    @Test
    public void SmallSheet1Activity02() throws InterruptedException {
        System.out.println("----------------case:使用该格式后，显示使用该格式那张");
        ClickSmallSheet("格式 4");

        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(3 * 1000);
        System.out.println("观察spinner是否显示格式4");
        Assert.assertTrue("显示的居然不是小票格式4的内容！" + driver.findElementById(pageName + ":id/printFormatVersion").getText(),
                driver.findElementById(pageName + ":id/design_header").getText().equals("小票4"));
    }

    @Test
    public void SmallSheet1Activity03() throws InterruptedException {
        System.out.println("----------------case:修改后，显示修改那张");
        ClickSmallSheet("格式 3");

        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        driver.findElementById(pageName + ":id/design_footer1").click();//点击页脚1
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被修改");
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示格式3");
        Assert.assertTrue("显示的居然不是小票格式3的内容！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("页脚1被修改"));
    }

    @Test
    public void SmallSheet1Activity04() throws InterruptedException {
        System.out.println("----------------case:创建后，显示最后一张（即创建那张）");
        System.out.println("观察spinner是否显示新建的格式");
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_footer1").click();
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被新建");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        Assert.assertTrue("显示的居然不是新建格式的内容！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("页脚1被新建"));
    }

    @Test
    public void SmallSheet1Activity05() throws InterruptedException {

        System.out.println("----------------case:完成CU后，重新进入，显示的是当前小票格式");

        ClickSmallSheet("格式 4");
        //-----------------1.使用格式
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示格式4");
        Assert.assertTrue("显示的居然不是小票格式4的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("小票4"));
        //----------------2.修改格式
        chose_smallsheet("格式 3");
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        driver.findElementById(pageName + ":id/design_footer1").click();//点击页脚1
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被修改");
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示格式3");
        Assert.assertTrue("显示的居然不是小票格式3的内容！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("页脚1被修改"));
        //----------------3.新建格式
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_footer1").click();
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被新建");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        //--------------4.点击销售页，再返回
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        System.out.println("观察spinner是否显示格式4");
        Assert.assertTrue("显示的居然不是小票格式4的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("小票4"));
    }

    @Test
    public void SmallSheet1Activity06() throws InterruptedException {
        System.out.println("----------------case:删除后，显示当前小票格式");
        ClickSmallSheet("格式 4");
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(6 * 1000);
        chose_smallsheet("格式 6");
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(6 * 1000);

        System.out.println("观察spinner是否显示格式4");
        Assert.assertTrue("显示的居然不是小票格式4的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("小票4"));
    }

    //不通过原因：功能代码有问题
    @Test
    public void SmallSheet1Activity07() throws InterruptedException {
        System.out.println("----------------case:使用该格式后，删除这张小票，显示默认小票（ID=1那张）");
        ClickSmallSheet("格式 2");

        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(6 * 1000);
        Assert.assertTrue("显示的居然不是小票格式1的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("小票"));
    }

    @Test
    public void SmallSheet1Activity08() throws InterruptedException {
        System.out.println("----------------case:点击同步按钮，能正常同步，同步后显示最大的");
        System.out.println("观察是否显示最大的小票格式");
        posLogin();
        Thread.sleep(1000);

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/loading_config_data").click();//点击同步
        Thread.sleep(10 * 1000);
    }


    //// TODO: 2020/3/27 这个测试有问题，进入小票页面显示的是最小的小票格式，原先的代码也是如此
    @Test
    public void SmallSheet1Activity09() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:未同步小票格式，断网,进入小票页面，显示的是最大那个");
        System.out.println("观察是否显示最大的小票格式");
        posLogin();
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
    }

    @Test
    public void SmallSheet1Activity10() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:未同步小票格式，断网,进入小票页面，修改后，显示修改那张");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮

        chose_smallsheet("格式 3");
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        driver.findElementById(pageName + ":id/design_footer1").click();//点击页脚1
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被断网修改");
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示格式3");
        Assert.assertTrue("显示的居然不是小票格式3的内容！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("页脚1被断网修改"));
    }

    //不通过的原因：需要未同步小票格式，新建格式，才能显示新建的格式，所以需要重新安装app，需要重新设置明文密码；
    @Test
    public void SmallSheet1Activity11() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:未同步小票格式，断网,进入小票页面，创建后，显示最后一张（即创建那张）");
        System.out.println("观察spinner是否显示新建小票格式");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_footer1").click();
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被断网新建");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        Assert.assertTrue("显示的居然不是新建小票格式的内容！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("页脚1被断网新建"));
    }

    @Test
    public void SmallSheet1Activity12() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:未同步小票格式，断网，完成CU后，重新进入，显示的是当前小票格式");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        Thread.sleep(1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(200);

        //----------------2.修改格式
        chose_smallsheet("格式 3");
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        driver.findElementById(pageName + ":id/design_footer1").click();//点击页脚1
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被断网修改");
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示格式3");
        Assert.assertTrue("显示的居然不是小票格式3的内容！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("页脚1被断网修改"));
        //----------------3.新建格式
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_footer1").click();
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被新建");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        //--------------4.点击销售页，再返回
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        System.out.println("观察spinner是否显示格式1");//断网情况下无法使用格式，只会显示格式1
        Assert.assertTrue("显示的居然不是小票格式1的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("小票"));
    }

    // TODO: 2020/3/27 断网，进入小票页面，删除后，显示最大那个
    @Test
    public void SmallSheet1Activity13() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:未同步小票格式，断网，进入小票页面，删除后，显示最大那个");
        System.out.println("观察是否显示最大的那个，是则测试通过");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        Thread.sleep(1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(200);
        chose_smallsheet("格式 5");
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(6 * 1000);
    }

    // TODO: 2020/3/27 同步小票格式后断网，开始进入，显示的是最大那个
    @Test
    public void SmallSheet1Activity14() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:同步小票格式后断网，开始进入，显示的是最大那个");
        System.out.println("观察spinner是否显示最大的那个");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        //进入设置界面进行基础资料重置
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
    }

    @Test
    public void SmallSheet1Activity15() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:同步小票格式后断网，修改后，显示修改那张");
        System.out.println("观察修改后，是否显示修改那张，是则通过");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        //进入设置界面进行基础资料重置
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮

        chose_smallsheet("格式 3");
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        driver.findElementById(pageName + ":id/design_footer1").click();//点击页脚1
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1在同步后断网修改");
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示格式3");
        Assert.assertTrue("显示的居然不是小票格式3的内容！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("页脚1在同步后断网修改"));
    }

    //不通过原因：新建格式后，显示的不是新建格式的内容；
    @Test
    public void SmallSheet1Activity16() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:同步小票格式后断网，创建后，显示最后一张（即创建那张）");
        System.out.println("观察创建后，是否显示最后一张（即创建那张），是则通过");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        //进入设置界面进行基础资料重置
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮

        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_footer1").click();
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被同步后断网新建");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示新建格式");
        Assert.assertTrue("显示的居然不是新建格式的内容！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("页脚1被同步后断网新建"));
    }

    @Test
    public void SmallSheet1Activity17() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:同步小票格式后，断网，完成CU后，重新进入，显示的是当前小票格式");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        posLogin();
        Thread.sleep(1000);
        //进入设置界面进行基础资料重置
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(200);
        //----------------2.修改格式
        chose_smallsheet("格式 3");
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        driver.findElementById(pageName + ":id/design_footer1").click();//点击页脚1
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被同步后断网修改");
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示格式3");
        Assert.assertTrue("显示的居然不是小票格式3的内容！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("页脚1被同步后断网修改"));
        //----------------3.新建格式
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_footer1").click();
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被新建");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        //--------------4.点击销售页，再返回
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        System.out.println("观察spinner是否显示格式4");
        Assert.assertTrue("显示的居然不是小票格式1的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("小票"));
    }

    @Test
    public void SmallSheet1Activity18() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:同步小票格式后，断网，删除后，显示的是当前小票格式");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        Thread.sleep(1000);
        //进入设置界面进行基础资料重置
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(200);
        chose_smallsheet("格式 4");
        //-----------------1.使用格式
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(6 * 1000);
        chose_smallsheet("格式 7");//这里出错的话，就是前面的case没有运行，没有产生格式 7
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(6 * 1000);
        //虽然选择了格式 4；但是没有联网选择格式是无效的，所以应该是格式1
        System.out.println("观察spinner是否显示格式1");
        Assert.assertTrue("显示的居然不是小票格式1的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("小票"));
    }

    @Test
    public void SmallSheet1Activity19() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:同步小票格式后，断网，使用该格式后，删除这张小票，显示默认小票（ID=1那张）");
        System.out.println("观察是否显示格式1，是的话则测试通过");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        Thread.sleep(1000);
        //进入设置界面进行基础资料重置
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(200);
        chose_smallsheet("格式 3");
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(6 * 1000);
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(6 * 1000);
        Assert.assertTrue("显示的居然不是小票格式1的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("小票"));
    }

    /**
     * 以下为未同步小票格式后的测试
     * case 20必须在case 21-27之前先运行
     *
     * @throws InterruptedException
     * @throws MalformedURLException
     */

    //不通过的原因：需要未同步小票格式，所以重新安装了app，数据库没有重新设置明文密码，所以测试失败；
    @Test
    public void SmallSheet1Activity20() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:未同步小票格式后，开始进入，显示空白");
        System.out.println("观察是否显示空白。是的话测试通过");
        /**
         * 为了清除所有的小票格式还原回未同步小票格式，这里进行重新安装app
         */
        // 重新安装apk
        capabilities.setCapability("noReset", false);
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);

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

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(3 * 1000);
    }

    //case 21-27： 因为上面的case填写公司编号失败，所以这些登录都失败了；
    @Test
    public void SmallSheet1Activity21() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:未同步小票格式后，使点击创建后点击取消，显示空白");
        System.out.println("此测试需要在case 20先运行的情况下才能测试");
        System.out.println("观察是否显示空白。是的话测试通过");
        posLogin();

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/create_cancle").click();//点击取消新建
        Thread.sleep(3 * 1000);
    }

    //case 21-27： 因为上面的case填写公司编号失败，所以这些登录都失败了；
    @Test
    public void SmallSheet1Activity22() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:未同步小票格式后，创建后，显示最后一张（即创建那张）");
        System.out.println("此测试需要在case 20先运行的情况下才能测试");
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("格式1");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        Assert.assertTrue("显示的居然不是新建格式的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("格式1"));
    }

    //case 21-27： 因为上面的case填写公司编号失败，所以这些登录都失败了；
    @Test
    public void SmallSheet1Activity23() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:未同步小票格式后，修改后，显示修改那张");
        System.out.println("此测试需要在case 20先运行的情况下才能测试");
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        driver.findElementById(pageName + ":id/design_footer1").click();//点击页脚1
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1未同步后被修改");
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示格式1");
        Assert.assertTrue("显示的居然不是小票格式1的内容！", driver.findElementById(pageName + ":id/design_footer1").getText().equals("页脚1未同步后被修改"));
    }

    //case 21-27： 因为上面的case填写公司编号失败，所以这些登录都失败了；
    @Test
    public void SmallSheet1Activity24() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:未同步小票格式后，使用该格式后，显示使用该格式那张");
        System.out.println("此测试需要在case 22先运行的情况下才能测试");
        posLogin();
        ClickSmallSheet("格式 1");
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(3 * 1000);
        System.out.println("观察spinner是否显示格式1");
        Assert.assertTrue("居然不是显示使用该格式那张！", driver.findElementById(pageName + ":id/design_header").getText().equals("格式1"));
    }

    //case 21-27： 因为上面的case填写公司编号失败，所以这些登录都失败了；
    @Test
    public void SmallSheet1Activity25() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:未同步小票格式后，重新进入，显示的是使用该格式那张");
        /**
         * 此处按照上面的流程测试下来，一共只有一个格式，不具备测试价值，所以再新建一个格式
         */
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("格式2");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        Assert.assertTrue("显示的居然不是新建格式的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("格式2"));

        chose_smallsheet("格式 2");
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        System.out.println("观察spinner是否显示格式2");
        Assert.assertTrue("显示的居然不是小票格式2的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("格式2"));
    }

    //case 21-27： 因为上面的case填写公司编号失败，所以这些登录都失败了；
    @Test
    public void SmallSheet1Activity26() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:未同步小票格式后，删除后，显示当前小票格式");
        /**
         * 此处按照上面的流程测试下来，一共只有俩个格式，不具备测试价值，所以再新建一个格式
         */
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("格式3");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        Assert.assertTrue("显示的居然不是新建格式的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("格式3"));

        chose_smallsheet("格式 1");
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(3 * 1000);

        chose_smallsheet("格式 3");
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示格式1");
        Assert.assertTrue("显示的居然不是小票格式1的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("格式1"));
    }

    //case 21-27： 因为上面的case填写公司编号失败，所以这些登录都失败了；
    @Test
    public void SmallSheet1Activity27() throws InterruptedException, MalformedURLException {
        System.out.println("----------------case:未同步小票格式后，使用该格式后，删除这张小票，显示最大那个");
        /**
         * 此处按照上面的流程测试下来，一共只有俩个格式，不具备测试价值，所以再新建一个格式
         */
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("格式3");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        Assert.assertTrue("显示的居然不是新建格式的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("格式3"));

        chose_smallsheet("格式 1");
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(6 * 1000);
        System.out.println("观察spinner是否显示格式3");
        Assert.assertTrue("显示的居然不是小票格式1的内容！", driver.findElementById(pageName + ":id/design_header").getText().equals("格式3"));
    }


}
