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
 * Created by WPNA on 2020/3/31.
 */

public class SmallSheet1Activity_C_AppiumTest extends BaseAppiumTest {

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

    @Test
    public void SmallSheet1Activity01() throws InterruptedException {
        System.out.println("-----------case:新建小票格式");

        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("新建格式");
        driver.findElementById(pageName + ":id/design_footer1").click();//点击页脚1
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("新建页脚");
        driver.findElementById(pageName + ":id/design_ticket_bottom").click();//点击底部
        driver.findElementById(pageName + ":id/design_ticket_bottom").sendKeys("新建底部");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        Assert.assertTrue("输入的跟保存的不一样！", driver.findElementById(pageName + ":id/design_header").getText().equals("新建格式") &&
                driver.findElementById(pageName + ":id/design_footer1").equals("新建页脚") &&
                driver.findElementById(pageName + ":id/design_ticket_bottom").equals("新建底部"));
    }

    @Test
    public void SmallSheet1Activity02() throws InterruptedException {
        System.out.println("---------case:新建小票格式，不填写任何数据");
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        //没输入任何数据，但是获取会获取到hint的内容
        Assert.assertTrue("未输入任何数据就保存，应该为空", driver.findElementById(pageName + ":id/design_header").getText().equals("小票内容")
                && driver.findElementById(pageName + ":id/design_footer1").getText().equals("此处编辑页脚")
                && driver.findElementById(pageName + ":id/design_ticket_bottom").getText().equals("此处编辑底部文字"));
    }

    /**
     * case 3为添加图片，appium暂无法完成；
     */

    @Test
    public void SmallSheet1Activity04() throws InterruptedException {
        System.out.println("-------------case:超过可存在的小票格式数量");
        /**
         * 按照顺序测试下来，此时应该有8张小票，需再新建2张
         */
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);

        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);

        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);

        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        Assert.assertTrue("居然可以超过10张新建！", driver.findElementById(pageName + ":id/smallsheet_to_update") != null);
    }

    @Test
    public void SmallSheet1Activity05() throws InterruptedException {
        System.out.println("-------------case:打印一张小票");
        System.out.println("观察是否打印成功");
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);

        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);
    }

    /**
     * case 6 与case 5 有什么不同？
     */

    @Test
    public void SmallSheet1Activity07() throws InterruptedException {
        System.out.println("-------------case:.底部空行设置为5，打印测试");
        System.out.println("观察是否打印成功");
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);

        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        AndroidElement e = driver.findElementById(pageName + ":id/add_design_bottom_blank_line_number");
        for (int i = 0; i < 5; i++) {
            e.click();  //点击5次底部空行的加号
        }
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);
    }

    @Test
    public void SmallSheet1Activity08() throws InterruptedException {
        System.out.println("-------------case:.将合计，支付方式，微信支付，支付宝支付，现金支付等字都居中，打印测试");
        System.out.println("观察是否打印成功");
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式

        driver.findElementById(pageName + ":id/total_money").click();//点击合计
        driver.findElementById(pageName + ":id/control_keep_center").click();//点击居中
        driver.findElementById(pageName + ":id/payment_method").click();//点击支付方式
        driver.findElementById(pageName + ":id/control_keep_center").click();//点击居中
        driver.findElementById(pageName + ":id/template_discont").click();//点击优惠
        driver.findElementById(pageName + ":id/control_keep_center").click();//点击居中
        driver.findElementById(pageName + ":id/template_payable").click();//点击应付
        driver.findElementById(pageName + ":id/control_keep_center").click();//点击居中
        driver.findElementById(pageName + ":id/template_payment_method1").click();//点击微信支付
        driver.findElementById(pageName + ":id/control_keep_center").click();//点击居中
        driver.findElementById(pageName + ":id/template_payment_method3").click();//点击现金支付
        driver.findElementById(pageName + ":id/control_keep_center").click();//点击居中

        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);
    }

    @Test
    public void SmallSheet1Activity09() throws InterruptedException {
        System.out.println("-------------case:.将合计，支付方式，微信支付，支付宝支付，现金支付等字都居中，打印测试");
        System.out.println("观察是否打印成功");
        posLogin();
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式

        driver.findElementById(pageName + ":id/total_money").click();//点击合计
        driver.findElementById(pageName + ":id/left_shift").click();//点击左移一格
        driver.findElementById(pageName + ":id/payment_method").click();//点击支付方式
        driver.findElementById(pageName + ":id/left_shift").click();//点击左移一格
        driver.findElementById(pageName + ":id/template_discont").click();//点击优惠
        driver.findElementById(pageName + ":id/left_shift").click();//点击左移一格
        driver.findElementById(pageName + ":id/template_payable").click();//点击应付
        driver.findElementById(pageName + ":id/left_shift").click();//点击左移一格
        driver.findElementById(pageName + ":id/template_payment_method1").click();//点击微信支付
        driver.findElementById(pageName + ":id/left_shift").click();//点击左移一格
        driver.findElementById(pageName + ":id/template_payment_method3").click();//点击现金支付
        driver.findElementById(pageName + ":id/left_shift").click();//点击左移一格

        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);
    }

    /**
     * case 10--12：暂无法实现，需创建一个公司
     * case 13：已被摒弃
     * case 14：暂无法实现，通过代码创建小票格式
     * case 15：暂无法实现，无法添加logo
     */
    @Test
    public void SmallSheet1Activity16() throws InterruptedException {
        System.out.println("----------case:收银员登录，进行创建小票格式，提示权限不足");

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
        Thread.sleep(1000);
        Assert.assertTrue("员工账号居然可以修改小票格式！", driver.findElementById(pageName + ":id/desk_ticket") != null);
    }

    @Test
    public void SmallSheet1Activity17() throws InterruptedException {
        System.out.println("--------case:登陆前断网，新建小票格式");
        System.out.println("需观察spinner是否显示新建的小票格式");
        /**
         * 按照测试下来，此时已经是有10张小票格式了，无法新建了，所以必须先删除几张再新建
         */
        //等待POS登录
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));
        driver.findElementById(pageName + ":id/password").sendKeys("000000");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
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

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);

        chose_smallsheet("格式 10");
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(3 * 1000);
        chose_smallsheet("格式 9");
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(3 * 1000);
        chose_smallsheet("格式 8");
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_footer1").click();
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被新建");
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("页眉被新建");

        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        Assert.assertTrue("新建失败了！", driver.findElementById(pageName + ":id/design_header").getText().equals("页眉被新建"));
    }

    @Test
    public void SmallSheet1Activity18() throws InterruptedException {
        System.out.println("--------case:在前台小票页面断网，新建小票格式");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_footer1").click();
        driver.findElementById(pageName + ":id/design_footer1").sendKeys("页脚1被新建");
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("页眉被18新建");

        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        Assert.assertTrue("新建失败了！", driver.findElementById(pageName + ":id/design_header").getText().equals("页眉被18新建"));
    }

    @Test
    public void SmallSheet1Activity19() throws InterruptedException {
        System.out.println("-------------此处为case19--23，集中测试，提高运行测试时的效率");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        System.out.println("----------case19:设小票分隔符为虚线“-”时，点击打印测试");
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").click();//点击分割线
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").sendKeys("-");//设小票分隔符为虚线“-”
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);

        System.out.println("----------case20:设小票分隔符为星星“*”时，点击打印测试");
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").click();//点击分割线
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").sendKeys("*");//设小票分隔符为星星“*”时
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);

        System.out.println("----------case21:设小票分隔符为空串“”时，点击打印测试");
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").click();//点击分割线
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").sendKeys("");//设小票分隔符为“”时
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);

        System.out.println("----------case22:设小票分隔符为空格“ ”时，点击打印测试");
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").click();//点击分割线
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").sendKeys(" ");//设小票分隔符为“ ”时
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);

        System.out.println("----------case23:设小票分隔符为数字2时，点击打印测试");
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").click();//点击分割线
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").sendKeys("2");//设小票分隔符为“2”时
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);

        System.out.println("----------case24:设小票分隔符为→（键盘是Alt+41466，UI键盘有这个图案）时，点击打印测试");
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").click();//点击分割线
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").sendKeys("→");//设小票分隔符为“→”时
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);

        System.out.println("----------case29:设小票分隔符为中文双引号（单个）时，点击打印测试");
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").click();//点击分割线
        driver.findElementById(pageName + ":id/design_delimiterToRepeat").sendKeys("“");//设小票分隔符为““”时
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);
    }

    /**
     * case 25：暂无法实现，需要输入爱心图案，暂时输入不了
     * case 26：为设置logo图片，暂无法实现
     * case 27：暂无法实现
     * case 28：暂无法实现，appium无法输入表情包
     */

    /**
     * 上次测试失败的原因：后台输出未料到的情况，APP崩溃，猜测为小票没有从表数据导致
     */
    @Test
    public void SmallSheet1Activity30() throws InterruptedException {
        System.out.println("-----------------case30:将小票格式底部空行加到最大，打印测试");
        System.out.println("观察空行数是否是5行");
        posLogin();

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        Thread.sleep(1000);
        AndroidElement e = driver.findElementById(pageName + ":id/add_design_bottom_blank_line_number");
        for (int i = 0; i < 6; i++) {
            e.click();  //点击6次底部空行的加号
        }
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);

        System.out.println("-----------------case31:将小票格式底部空行减到最小，打印测试");
        System.out.println("观察空行数是否是0行");
        AndroidElement e1 = driver.findElementById(pageName + ":id/reduce_design_bottom_blank_line_number");//点击底部空行的减号
        for (int i = 0; i < 6; i++) {
            e.click();  //点击6次底部空行的减号
        }
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);

        System.out.println("-----------------case32:随机加减小票格式，保存修改，打印测试");
        System.out.println("观察空行数是否是1行");
        /**
         * 此测试打印出来应该是1行空行
         */
        e.click();//底部空行的加号
        e.click();//底部空行的加号
        e1.click();//底部空行的减号
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);

        System.out.println("-----------------case34:随机加减小票格式，使用该格式，打印测试");
        System.out.println("观察空行数是否是2行");
        driver.findElementById(pageName + ":id/smallsheet_to_update").click();//点击修改格式
        Thread.sleep(1000);
        /**
         * 此测试打印出来应该是2行空行
         */
        e.click();//底部空行的加号
        e.click();//底部空行的加号
        e1.click();//底部空行的减号
        driver.findElementById(pageName + ":id/smallsheet_update").click();//保存修改
        Thread.sleep(6 * 1000);
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式 此处出错可能为：未预料到的情况！event=com.bx.erp.event.UI.SmallSheetSQLiteEvent@139b1b8
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/print_test").click();//打印测试
        Thread.sleep(1000);
    }

    /**
     *case 34已经在case33中，现在应该是不支持没有保存修改就使用该格式
     */


}
