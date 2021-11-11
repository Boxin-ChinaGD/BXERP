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

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import static io.appium.java_client.android.AndroidKeyCode.BACKSPACE;
import static io.appium.java_client.android.AndroidKeyCode.KEYCODE_A;
import static io.appium.java_client.android.AndroidKeyCode.META_CTRL_MASK;

/**
 * Created by WPNA on 2020/3/30.
 * 参照的手动测试文档为：MainActivity R : 19~67;
 */

public class CheckListFragment_R_AppiumTest extends BaseAppiumTest {

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
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);

        // 该测试开始前把不重新安装App设为false，在这里设置回来。
        capabilities.setCapability("noReset", true);
    }

    @Test
    public void CheckListFragment19() throws InterruptedException, MalformedURLException {
        System.out.println("--------------case:只输入订单号查询订单,点击订单后展示所有的订单中的商品");
        //登录POS
        posLogin();

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS20000329");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        Thread.sleep(10 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item

        Assert.assertTrue("居然没有展示商品列表！", driver.findElementById(pageName + ":id/Return_goods_page") != null);
    }

    @Test
    public void CheckListFragment20() throws InterruptedException, MalformedURLException {
        System.out.println("-------------case:只输入起始时间和结束时间查询订单，点击订单后展示所有的订单中的商品");
        posLogin();

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/check_list_startDate").click();//选择开始时间
        AndroidElement e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 20; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        driver.findElementById(pageName + ":id/check_list_endDate").click();//选择结束时间
        driver.findElementById("android:id/button1").click();//日历的确定按钮

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
    }

    @Test
    public void CheckListFragment21() throws InterruptedException, MalformedURLException {
        System.out.println("-------------------case:只输入订单号查询订单，提示“没有符合条件的零售单”");
        //登录POS
        posLogin();

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("0000000000");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        Thread.sleep(10 * 1000);
        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        Assert.assertTrue("搜索到单号！此处不应该搜索到单号", Integer.parseInt(amount) == 0);
    }

    @Test
    public void CheckListFragment22() throws InterruptedException, MalformedURLException {
        System.out.println("-------------------case:通过日期查询订单（日期没有生成零售单）,提示“没有符合条件的零售单”");
        posLogin();
        /**
         *  选择的日期为2019/03/13--2019/04/17，随着时间的推移，此测试选择的日期会越来越往后，
         *  可能导致选择的时间段中存在零售单，导致测试不通过
         */
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/check_list_startDate").click();//选择开始时间
        AndroidElement e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 20; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        driver.findElementById(pageName + ":id/check_list_endDate").click();//选择结束时间
        e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 19; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);
        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        Assert.assertTrue("搜索到单号！此处不应该搜索到单号", Integer.parseInt(amount) == 0);
    }

    @Test
    public void CheckListFragment23() throws InterruptedException, MalformedURLException {
        System.out.println("--------------case:查询订单号加日期，查询到同时满足两个条件的零售单，点击订单后展示所有的订单中的商品");
        /**
         *  选择的日期为2019/06/12--2019/08/14，随着时间的推移，此测试选择的日期会越来越往后，
         *  可能导致选择的时间段中存在零售单，导致测试不通过
         */
        posLogin();

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS20190725");//输入单号
        driver.findElementById(pageName + ":id/check_list_startDate").click();//选择开始时间
        AndroidElement e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 9; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        driver.findElementById(pageName + ":id/check_list_endDate").click();//选择结束时间
        e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 7; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);

        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Assert.assertTrue("居然没有展示商品列表！", driver.findElementById(pageName + ":id/Return_goods_page") != null);
    }

    @Test
    public void CheckListFragment24() throws InterruptedException, MalformedURLException {
        System.out.println("--------------case:查询订单号加日期，输入的单号不存在,提示“没有符合条件的零售单”");
        posLogin();

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS000000000");//输入单号
        driver.findElementById(pageName + ":id/check_list_startDate").click();//选择开始时间
        AndroidElement e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 9; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        driver.findElementById(pageName + ":id/check_list_endDate").click();//选择结束时间
        e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 8; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);

        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        Assert.assertTrue("搜索到单号！此处不应该搜索到单号", Integer.parseInt(amount) == 0);
    }

    @Test
    public void CheckListFragment25() throws InterruptedException, MalformedURLException {
        System.out.println("--------------case:查询订单号加日期，查询的时间没有零售单，提示“没有符合条件的零售单”");
        /**
         *  选择的日期为2019/08/15--2019/09/15，随着时间的推移，此测试选择的日期会越来越往后，
         *  可能导致选择的时间段中存在零售单，导致测试不通过
         */
        posLogin();

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS20190725");//输入单号
        driver.findElementById(pageName + ":id/check_list_startDate").click();//选择开始时间
        AndroidElement e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 8; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        driver.findElementById(pageName + ":id/check_list_endDate").click();//选择结束时间
        e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 7; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);

        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        Assert.assertTrue("搜索到单号！此处不应该搜索到单号", Integer.parseInt(amount) == 0);
    }

    @Test
    public void CheckListFragment26() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:不输入条件直接点击查单，返回所有的零售单");

        posLogin();
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);

        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
    }

    @Test
    public void CheckListFragment27() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:不输入条件直接点击查单，返回所有的零售单，零售单大于10条，分页");

        posLogin();
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);

        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        Assert.assertTrue("搜索到单号！此处不应该搜索到单号", Integer.parseInt(amount) > 0);
    }

    /**
     * case 28:暂无法实现：查单时不传日期到服务器
     */

    @Test
    public void CheckListFragment29() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:输入不完整单号，单号小于10位，提示“单据流水号长度不能大于26，且不能小于10个字符");
        posLogin();
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("222");//输入单号
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);
        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        Assert.assertTrue("搜索到单号！此处不应该搜索到单号", Integer.parseInt(amount) == 0);
    }

    @Test
    public void CheckListFragment30() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:输入不完整单号，单号大于26位，提示“单据流水号长度不能大于26，且不能小于10个字符");
        posLogin();
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        AndroidElement elementById = driver.findElementById(pageName + ":id/t2");
        for (int i = 0; i < 33; i++) {
            elementById.click();
        }
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("222222222222222222222222222222222");//输入单号
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);
        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        Assert.assertTrue("搜索到单号！此处不应该搜索到单号", Integer.parseInt(amount) == 0);
    }

    @Test
    public void CheckListFragment31() throws InterruptedException, MalformedURLException {
        System.out.println("--------------case:输入长度大于10小于26的单号,可以查询到零售单");

        posLogin();
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS201909031055");//输入单号
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);
        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        System.out.println("获取搜索后的总页数" + amount);
        Assert.assertTrue("搜索不到单号！", Integer.parseInt(amount) > 0);
    }

    /**
     * case 32与case 29相同
     * case 33与case 30相同
     */

    @Test
    public void CheckListFragment34() throws InterruptedException, MalformedURLException {
        System.out.println("-------------------case:只输入订单号查询订单，输入长度大于10小于20的单号，可以查询到零售单");
        //登录POS
        posLogin();

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS2000040318");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        Thread.sleep(10 * 1000);
        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        Assert.assertTrue("搜索不到单号！", Integer.parseInt(amount) > 0);
    }

    @Test
    public void CheckListFragment35() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:输入长度小于10，或大于20的单号,提示“输入的订单号长度最少为10位，最多为20位”;" +
                "界面数据清空，无数据显示；页码，总页数都显示为0；点击上一页无反应，点击下一页提示“没有符合条件的零售单”");
        System.out.println("此处需观察是否点击上一页无反应");
        //登录POS
        posLogin();

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS123");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        Thread.sleep(10 * 1000);
        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        Assert.assertTrue("搜索到单号！此处不应该搜索到单号", Integer.parseInt(amount) == 0);

        driver.findElementById(pageName + ":id/last_page").click();//点击上一页
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/next_page").click();//点击下一页
        Thread.sleep(3 * 1000);
    }

    /**
     * case 36 暂无法实现：需在服务器端创建一个价格为0的商品，并在pos机同步后单独售卖该商品，实现现金支付，生成一张零售单
     * case 37暂无法实现：需在服务器端创建一个价格为0的商品，并在pos机同步后售卖多个商品其中包含该价格为0的商品，生成一张零售单
     */

    @Test
    public void CheckListFragment38() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:1.成功登录\n" +
                "2.点击“查单”\n" +
                "3.输入长度小于10，或大于26的单号\n" +
                "4.点击上一页，下一页的按钮");
        System.out.println("此处需观察是否点击上一页无反应");
        //登录POS
        posLogin();

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        Thread.sleep(10 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS123");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        Thread.sleep(10 * 1000);

        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        Assert.assertTrue("搜索到单号！此处不应该搜索到单号", Integer.parseInt(amount) == 0);

        driver.findElementById(pageName + ":id/last_page").click();//点击上一页
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/next_page").click();//点击下一页
        Thread.sleep(3 * 1000);

    }

    /**
     * case 39~43 已经被摒弃
     */

    @Test
    public void CheckListFragment44() throws InterruptedException, MalformedURLException {
        System.out.println("查询超过一年的零售单，点击退货");
        posLogin();

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS2000032207225500012213");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        Thread.sleep(10 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号
        Thread.sleep(1000);

        Assert.assertTrue("这张零售单不应该可以退货！", driver.findElementById(pageName + ":id/reprint_SmallSheet") != null);
    }

    /**
     * case:45-67 已经被摒弃
     */

}
