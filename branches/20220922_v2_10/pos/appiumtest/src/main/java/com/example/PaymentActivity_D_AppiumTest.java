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
import io.appium.java_client.android.AndroidKeyCode;

import static io.appium.java_client.android.AndroidKeyCode.BACKSPACE;
import static io.appium.java_client.android.AndroidKeyCode.KEYCODE_A;
import static io.appium.java_client.android.AndroidKeyCode.KEYCODE_BACK;
import static io.appium.java_client.android.AndroidKeyCode.META_CTRL_MASK;

/**
 * Created by WPNA on 2020/3/19.
 */

public class PaymentActivity_D_AppiumTest extends BaseAppiumTest {

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

    /**
     * 用户登录--选择商品--添加商品--点击结账--选择现金支付
     *
     * @throws InterruptedException
     */
    private void ChoseCommodityAndClickPay() throws InterruptedException {
        //等待POS登录
        posLogin();
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");//输入条形码
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        Assert.assertTrue("预期与测试商品数量不相等", driver.findElementById(pageName + ":id/commodity_quantity").getText().equals("商品数量：1 件"));
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
    }

    //断网查单方法
    private void DisConnect_CheckList() throws InterruptedException {
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        //查单
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//查单页面
        driver.findElementById(pageName + ":id/check_list_search").click();
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/check_list_search").click();
        Thread.sleep(2 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
        Thread.sleep(1 * 1000);
        driver.findElementById(pageName + ":id/commodity_name").click();
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
        posLogin();

        //进入设置界面进行基础资料重置
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);

//        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
//        driver.findElementById(pageName+":id/sale_linear").click();//点击销售按钮
//        driver.findElementById(pageName+":id/scan_barcode_search").click();//点击搜索框
//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567");
//        driver.findElementById(pageName+":id/bar_code").click();
//        driver.findElementById(pageName+":id/add_tv").click();
        // 该测试开始前把不重新安装App设为false，在这里设置回来。
        capabilities.setCapability("noReset", true);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest() throws Exception {

        //等待POS登录
        posLogin();
        /**
         *支付的UI流程：
         * 1.在主页面中点击扫描条形码框
         * 2.输入条形码选择需要购买的商品并点击加入
         * 3.点击结算
         * 4.点击支付
         * 5.点击支付完成
         */
        System.out.println("----------------------case：支付UI中不选取商品进行支付");
        Assert.assertTrue("预期与测试商品数量不相等", driver.findElementById(pageName + ":id/commodity_quantity").getText().equals("商品数量：1 件"));
        System.out.println("预期===弹出Toast:商品列表为空；右侧页面不显示！");
        driver.findElementById(pageName + ":id/balance_tv").click();
    }

    /**
     * 以下支付界面的测试用例是根据元粒度测试文档PaymentActivity中D栏编写
     * 其中用例1~10暂时无法实现(微信支付需要扫码枪，沙箱环境下无法进行微信支付，会提示支付失败)
     */
    //coding=utf-8
    @Test
    public void paymentActivityTest11() throws Exception {
        System.out.println("----------------------case11:点击需要购买的商品进行支付");
        ChoseCommodityAndClickPay();
        System.out.println("预期===弹出Toast:支付成功；关闭右侧页面，商品列表为空；");
        driver.findElementById(pageName + ":id/pay").click();//支付按钮

        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest12() throws Exception {
        System.out.println("----------------------case12:第一次支付金额小于总价，第二次支付金额为待付余额");
        System.out.println("测试商品的价格为：168");
        //等待POS登录
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/t1").click();//点击数字键盘的1

        Assert.assertTrue("预期与测试支付金额不相等", driver.findElementById(pageName + ":id/cash_paying_amount").getText().toString().equals("1"));
        System.out.println("预期===弹出Toast:支付成功；不关闭右侧页面");

        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(1 * 1000);
        Assert.assertTrue("预期与测试支付金额不相等", driver.findElementById(pageName + ":id/cash_paying_amount").getText().toString().equals("147.00"));
        System.out.println("预期===弹出Toast:支付成功；关闭右侧页面，商品列表为空；");
        driver.findElementById(pageName + ":id/pay").click();

        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest13() throws Exception {
        System.out.println("----------------------case13:填写支付金额大于总价");

        ChoseCommodityAndClickPay();
        AndroidElement element = driver.findElementById(pageName + ":id/t9");
        for (int i = 0; i < 4; i++) {
            element.click();//点击数字键盘的9
        }
        Assert.assertTrue("预期与测试支付金额不相等", driver.findElementById(pageName + ":id/cash_paying_amount").getText().toString().equals("9999"));
        System.out.println("预期===弹出Toast:支付成功，请使用现金退还：99987元；右侧页面关闭，商品列表清空");

        driver.findElementById(pageName + ":id/pay").click();//支付按钮
        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest15() throws Exception {
        System.out.println("----------------------case15:填写支付金额为0");

        //等待POS登录
        ChoseCommodityAndClickPay();
        driver.findElementById(pageName + ":id/t0").click();//填写支付金额为0
        Assert.assertTrue("预期与测试支付金额不相等", driver.findElementById(pageName + ":id/cash_paying_amount").getText().toString().equals("0"));
        System.out.println("预期===弹出Toast:支付成功；不关闭右侧页面");
        driver.findElementById(pageName + ":id/pay").click();//点击支付
        Thread.sleep(5 * 1000);
    }

    /**
     * 用例16~19暂时无法实现，无法实现原因：支付宝支付不存在
     */

    //coding=utf-8
    @Test
    public void paymentActivityTest20() throws Exception {
        System.out.println("----------------------case20:输入付款金额时，只能输入两位小数");

        //等待POS登录
        ChoseCommodityAndClickPay();

        driver.findElementById(pageName + ":id/t2").click(); //输入2
        driver.findElementById(pageName + ":id/t_point").click();//输入点
        driver.findElementById(pageName + ":id/t3").click();//输入3
        driver.findElementById(pageName + ":id/t6").click();//输入6
        driver.findElementById(pageName + ":id/t5").click();//输入5
        Assert.assertTrue("预期与测试支付金额不相等", driver.findElementById(pageName + ":id/cash_paying_amount").getText().toString().equals("2.36"));
        Thread.sleep(5 * 1000);
    }

    /**
     * 用例21与用例13相同
     */

    //coding=utf-8
    @Test
    public void paymentActivityTest22() throws Exception {
        System.out.println("----------------------case22:输入付款金额时多次输入小数点");
        ChoseCommodityAndClickPay();

        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t_point").click();
        driver.findElementById(pageName + ":id/t_point").click();
        Assert.assertTrue("预期与测试支付金额不相等", driver.findElementById(pageName + ":id/cash_paying_amount").getText().toString().equals("2."));
        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest23() throws Exception {
        System.out.println("----------------------case23:销售进行到一半，支付了部分金额，进行取消支付");

        ChoseCommodityAndClickPay();
        driver.findElementById(pageName + ":id/t1").click();
        Assert.assertTrue("预期与测试支付金额不相等", driver.findElementById(pageName + ":id/cash_paying_amount").getText().toString().equals("1"));
        driver.findElementById(pageName + ":id/pay").click();//支付部分金额
        System.out.println("预期===弹出对话框；点击确定后关闭右侧页面，返回商品列表；商品列表不为空；弹出Toast:请使用现金退还1元");
        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        Thread.sleep(5 * 1000);
        driver.findElementById("android:id/button1").click();//确定取消

        Thread.sleep(5 * 1000);
    }

    /**
     * 用例24~27暂时无法实现
     */

    //coding=utf-8
    @Test
    public void paymentActivityTest28() throws Exception {
        System.out.println("----------------------case28:销售进行到一半进行取消");

        ChoseCommodityAndClickPay();
        System.out.println("预期===弹出对话框；点击确定后关闭右侧页面，返回商品列表；商品列表不为空；弹出Toast:还没有进行支付金额，无需退款");
        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        Thread.sleep(3 * 1000);
        driver.findElementById("android:id/button1").click();//确定取消
        Thread.sleep(5 * 1000);
    }


    /* 用例29~31暂时无法实现
     * 用例32~33已删除
     * 用例34~35暂时无法实现
     * 用例36与用例13相同
     * 用例37暂时无法实现(连续点击支付)
     * 用例38~39暂时无法实现*/

    //coding=utf-8
    @Test
    public void paymentActivityTest40() throws Exception {
        System.out.println("----------------------case40:将(微信)支付金额置0后切换到现金支付模式");

        //等待POS登录
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("8965348932854");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/t0").click();//输入数字0
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        Assert.assertTrue("预期与测试支付金额不相等", driver.findElementById(pageName + ":id/cash_paying_amount").getText().toString().equals("12.00"));
        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest41() throws Exception {
        System.out.println("----------------------case41:将现金支付金额清空后切换到微信支付模式");
        ChoseCommodityAndClickPay();
        driver.findElementById(pageName + ":id/t0").click();//输入数字0
        driver.findElementById(pageName + ":id/wechat_pay").click();//微信支付
        Assert.assertTrue("预期与测试支付金额不相等", driver.findElementById(pageName + ":id/wechat_paying_amount").getText().toString().equals("12.00"));
        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest42() throws Exception {
        System.out.println("----------------------case42:现金支付完成后打印小票");

        ChoseCommodityAndClickPay();
        driver.findElementById(pageName + ":id/pay").click();
        // ...打印小票，需要肉眼验证小票格式是当前使用的小票格式
        Thread.sleep(5 * 1000);
    }

    /**
     * 用例43~44暂时无法实现
     */

    //coding=utf-8
    @Test
    public void paymentActivityTest45() throws Exception {
        System.out.println("----------------------case45:商品名称超过一行时，打印小票");

        //等待POS登录
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/t_m").click();
        driver.findElementById(pageName + ":id/t_u").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_t").click();
        driver.findElementById(pageName + ":id/t_i").click();
        driver.findElementById(pageName + ":id/t_p").click();
        driver.findElementById(pageName + ":id/t_a").click();
        driver.findElementById(pageName + ":id/t_c").click();
        driver.findElementById(pageName + ":id/t_k").click();
        driver.findElementById(pageName + ":id/t_a").click();
        driver.findElementById(pageName + ":id/t_g").click();
        driver.findElementById(pageName + ":id/t_c").click();
        driver.findElementById(pageName + ":id/t_o").click();
        driver.findElementById(pageName + ":id/t_m").click();
        driver.findElementById(pageName + ":id/t_m").click();
        driver.findElementById(pageName + ":id/t_b").click();
        driver.findElementById(pageName + ":id/t_a").click();
        driver.findElementById(pageName + ":id/t_r").click();
        driver.findElementById(pageName + ":id/t_c").click();
        driver.findElementById(pageName + ":id/t_o").click();
        driver.findElementById(pageName + ":id/t_d").click();
        driver.findElementById(pageName + ":id/t_e").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/t_c").click();
        driver.findElementById(pageName + ":id/en_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("multiPackagCommBarcodesC");
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        // ...打印小票，需要肉眼验证小票格式是当前使用的小票格式
        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest46() throws Exception {
        System.out.println("----------------------case46:零售多个商品，打印小票");

        //等待POS登录
        posLogin();

        for (int i = 1; i <= 5; i++) {  //循环添加5个商品
            driver.findElementById(pageName + ":id/scan_barcode_search").click();
            driver.findElementById(pageName + ":id/search_commodity_et").click();
            if (i == 1) {    //只有第一次需要切换到数字键盘
                driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
            }
            driver.findElementById(pageName + ":id/t1").click();
            driver.findElementById(pageName + ":id/t2").click();
            driver.findElementById(pageName + ":id/t3").click();
            driver.findElementById(pageName + ":id/t4").click();
            driver.findElementById(pageName + ":id/t5").click();
            driver.findElementById(pageName + ":id/t6").click();
            driver.findElementById(pageName + ":id/t7").click();
            driver.findElementById(pageName + ":id/t8").click();
            driver.findElementById(pageName + ":id/t0").click();
            driver.findElementById(pageName + ":id/t" + i).click();
            driver.findElementById(pageName + ":id/num_sure").click();//确定
//            driver.findElementById(pageName+":id/search_commodity_et").sendKeys("123456780" + i);
            driver.findElementById(pageName + ":id/name_attr").click();
            driver.findElementById(pageName + ":id/add_tv").click();
        }

        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        // ...打印小票，需要肉眼验证小票格式是当前使用的小票格式
        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest47() throws Exception {
        System.out.println("----------------------case47:有促销参与时，打印小票");

        //等待POS登录
        posLogin();

        for (int i = 0; i < 2; i++) {
            driver.findElementById(pageName + ":id/scan_barcode_search").click();
            //点击虚拟键盘
            driver.findElementById(pageName + ":id/search_commodity_et").click();
            driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
            driver.findElementById(pageName + ":id/t1").click();
            driver.findElementById(pageName + ":id/t2").click();
            driver.findElementById(pageName + ":id/t3").click();
            driver.findElementById(pageName + ":id/t4").click();
            driver.findElementById(pageName + ":id/t5").click();
            driver.findElementById(pageName + ":id/t6").click();
            driver.findElementById(pageName + ":id/t7").click();
            driver.findElementById(pageName + ":id/t8").click();
            driver.findElementById(pageName + ":id/t0").click();
            driver.findElementById(pageName + ":id/t5").click();
            driver.findElementById(pageName + ":id/num_sure").click();//确定
//            driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567805");
            driver.findElementById(pageName + ":id/name_attr").click();
            driver.findElementById(pageName + ":id/add_tv").click();
        }
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        // ...打印小票，需要肉眼验证小票格式是当前使用的小票格式
        Thread.sleep(5 * 1000);
    }

    /**
     * 用例48暂时无法实现
     * 用例49与用例46重复了
     */

    //coding=utf-8
    @Test
    public void paymentActivityTest50() throws Exception {
        System.out.println("----------------------case50:支付完成后查单");

        //等待POS登录
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567805");
        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        // 查单
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击切换到查单页面
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(5 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//点击搜索到的列表中的第一个
        Thread.sleep(1 * 1000);
        driver.findElementById(pageName + ":id/commodity_name").click();//点击右侧页面的第一个
        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
        Assert.assertTrue("商品名字不一样！", commodityName_checkList.equals(commodityName_scanBarcode));
        Thread.sleep(5 * 1000);
    }

    /**
     * 用例51与用例23重复了
     * 用例52~58暂时无法实现
     */

    @Test
    public void paymentActivityTest59() throws Exception {
        System.out.println("----------------------case59:支付部分金额后返回上一页面，再次支付，成功；查单可以查到该单");

        //等待POS登录
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567805");
        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/t1").click();
        Assert.assertTrue("预期与测试支付金额不相等", driver.findElementById(pageName + ":id/cash_paying_amount").getText().toString().equals("1"));
        driver.findElementById(pageName + ":id/pay").click();

        //取消支付
        System.out.println("预期===弹出对话框；点击确定后关闭右侧页面，返回商品列表；商品列表不为空；弹出Toast:请使用现金退还1元");
        driver.findElementById(pageName + ":id/payment_close").click();
        Thread.sleep(2 * 1000);
        driver.findElementById("android:id/button1").click();

        //再次点击支付
        Thread.sleep(2 * 1000);
        System.out.println("预期===弹出Toast:支付成功");
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(5 * 1000);

        //查单
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击切换到查单页面
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(5 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//点击搜索到的列表中的第一个
        Thread.sleep(1 * 1000);
        driver.findElementById(pageName + ":id/commodity_name").click();//点击右侧页面的第一个
        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
        Assert.assertTrue("商品名字不一样！", commodityName_checkList.equals(commodityName_scanBarcode));
        Thread.sleep(5 * 1000);
    }

    /**
     * 用例60~62暂时无法实现
     */

    //coding=utf-8
    @Test
    public void paymentActivityTest63() throws Exception {
        System.out.println("----------------------case63:现金支付后断网查单，再联网查单");
        //此测试跑之前，上一单的商品名称不能为4号商品（1234567804）；否则将影响本次测试的结果；
        //等待POS登录
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567804");
        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();

        //断网查单
        DisConnect_CheckList();
        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
        Assert.assertTrue("商品名字不一样！查单失败！", commodityName_checkList.equals(commodityName_scanBarcode));

        //重新联网
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
        Thread.sleep(20 * 1000);
        //再次查单
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/check_list_search").click();
        Thread.sleep(5 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
        Thread.sleep(1 * 1000);
        driver.findElementById(pageName + ":id/commodity_name").click();
        commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
        //商品名字理论上不一样（零售单没有上传到服务器）
        Assert.assertTrue("商品名字不一样！查单失败！", commodityName_checkList.equals(commodityName_scanBarcode));
        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest64() throws Exception {
        System.out.println("----------------------case64:现金支付后断网查单，再退出登录联网查单");
        //此测试跑之前，上一单的商品名称不能为2号商品（1234567802）；否则将影响本次测试的结果；
        //等待POS登录
        posLogin();

        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567802");
        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();

        //断网查单
        DisConnect_CheckList();
        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
        Assert.assertTrue("商品名字居然不一样！查单失败！", commodityName_checkList.equals(commodityName_scanBarcode));

        //重新联网
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);

        //重新登录
        posLogin();

        //再次查单
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
        driver.findElementById(pageName + ":id/check_list_search").click();
        Thread.sleep(5 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
        Thread.sleep(1 * 1000);
        driver.findElementById(pageName + ":id/commodity_name").click();
        commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
        //商品名字理论上一样
        Assert.assertTrue("商品名字居然不一样！", commodityName_checkList.equals(commodityName_scanBarcode));
        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest65() throws Exception {
        System.out.println("----------------------case65:断网后登陆并售卖商品现金支付后查单，再联网查单");

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
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
        System.out.println("Input reverse is " + reverseNumber);
        driver.findElementById(pageName + ":id/confirm_reserve").click();

        //添加商品，点击结账
        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567801");
        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();

        //查单
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
        driver.findElementById(pageName + ":id/check_list_search").click();//点击筛选
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//再次点击筛选
        Thread.sleep(2 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
        Thread.sleep(1 * 1000);
        driver.findElementById(pageName + ":id/commodity_name").click();
        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();

        Assert.assertTrue("商品名字居然不一样！查单失败！", commodityName_checkList.equals(commodityName_scanBarcode));

        //重新联网
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
        //再次查单
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
        driver.findElementById(pageName + ":id/check_list_search").click();
        Thread.sleep(5 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
        Thread.sleep(1 * 1000);
        driver.findElementById(pageName + ":id/commodity_name").click();
        commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
        Assert.assertTrue("商品名字居然不一样！查单失败！", commodityName_checkList.equals(commodityName_scanBarcode));
        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest66() throws Exception {
        System.out.println("----------------------case66:断网后卖商品并现金支付后查单，再退出登录联网查单");

        //断网
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));
        //等待POS登录
        Thread.sleep(10 * 1000);
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();
        clearText(driver.findElementById(pageName + ":id/password"));
        driver.findElementById(pageName + ":id/password").sendKeys("000000");
        System.out.println("currentActivity:" + driver.currentActivity());
        driver.findElementById(pageName + ":id/login").click();
        // 提示离线模式，点确定
        Thread.sleep(2 * 1000);
        driver.findElementById("android:id/button1").click();

        //进入到MainActivity：设置准备金
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
        //搜索商品，添加商品，结账
        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567805");
        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        //查单
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
        driver.findElementById(pageName + ":id/check_list_search").click();//点击筛选
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//再次点击筛选
        Thread.sleep(2 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
        Thread.sleep(1 * 1000);
        driver.findElementById(pageName + ":id/commodity_name").click();
        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
        Assert.assertTrue("商品名字不一样！查单失败", commodityName_checkList.equals(commodityName_scanBarcode));
        //重新联网
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
        //交班退出
        driver.findElementById(pageName + ":id/logout").click();
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();
        Thread.sleep(5 * 1000);
        //重新登录
        posLogin();
        //再次查单
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
        driver.findElementById(pageName + ":id/check_list_search").click();
        Thread.sleep(5 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
        Thread.sleep(1 * 1000);
        driver.findElementById(pageName + ":id/commodity_name").click();
        commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
        //商品名字理论上一样
        Assert.assertTrue("商品名字居然不一样！查单失败", commodityName_checkList.equals(commodityName_scanBarcode));
        Thread.sleep(5 * 1000);
    }

    /**
     * 用例67~69有微信支付，暂时无法实现
     * 用例70与用例23一样
     * 用例71涉及微信支付，暂时无法实现
     */

    //coding=utf-8
    @Test
    public void paymentActivityTest72() throws Exception {
        System.out.println("----------------------case72:售卖一个或一批包含价格为0的商品，验证能售卖成功");

        //等待POS登录
        posLogin();
        //添加价格为0的商品
        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/t_p").click();
        driver.findElementById(pageName + ":id/t_r").click();
        driver.findElementById(pageName + ":id/t_i").click();
        driver.findElementById(pageName + ":id/t_c").click();
        driver.findElementById(pageName + ":id/t_e").click();
        driver.findElementById(pageName + ":id/t_i").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("priceis0");
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        //添加其他商品
        for (int i = 1; i <= 5; i++) {
            driver.findElementById(pageName + ":id/scan_barcode_search").click();
            driver.findElementById(pageName + ":id/search_commodity_et").click();
            if (i == 1) {    //只有第一次需要切换到数字键盘
                driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
            }
            driver.findElementById(pageName + ":id/t1").click();
            driver.findElementById(pageName + ":id/t2").click();
            driver.findElementById(pageName + ":id/t3").click();
            driver.findElementById(pageName + ":id/t4").click();
            driver.findElementById(pageName + ":id/t5").click();
            driver.findElementById(pageName + ":id/t6").click();
            driver.findElementById(pageName + ":id/t7").click();
            driver.findElementById(pageName + ":id/t8").click();
            driver.findElementById(pageName + ":id/t0").click();
            driver.findElementById(pageName + ":id/t" + i).click();
            driver.findElementById(pageName + ":id/num_sure").click();//确定
//            driver.findElementById(pageName+":id/search_commodity_et").sendKeys("123456780" + i);
            driver.findElementById(pageName + ":id/name_attr").click();
            driver.findElementById(pageName + ":id/add_tv").click();
        }
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();

        Thread.sleep(5 * 1000);
    }

    //coding=utf-8
    @Test
    public void paymentActivityTest73() throws Exception {
        System.out.println("----------------------case73:查看所有无优惠支付测试打印的小票，看看优惠是否存在-0.00的情况");

        //等待POS登录
        posLogin();
        //添加价格为0的商品
        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/t_p").click();
        driver.findElementById(pageName + ":id/t_r").click();
        driver.findElementById(pageName + ":id/t_i").click();
        driver.findElementById(pageName + ":id/t_c").click();
        driver.findElementById(pageName + ":id/t_e").click();
        driver.findElementById(pageName + ":id/t_i").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("priceis0");
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        //添加其他商品
        driver.findElementById(pageName + ":id/scan_barcode_search").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("123456780");
        driver.findElementById(pageName + ":id/name_attr").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        driver.findElementById(pageName + ":id/balance_tv").click();
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();

        Thread.sleep(5 * 1000);
    }

}





















