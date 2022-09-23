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
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import static io.appium.java_client.android.AndroidKeyCode.BACKSPACE;
import static io.appium.java_client.android.AndroidKeyCode.KEYCODE_A;
import static io.appium.java_client.android.AndroidKeyCode.META_CTRL_MASK;

/**
 * Created by WPNA on 2020/4/6.
 */

public class Main1Activity_C_AppiumTest extends BaseAppiumTest {

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

    // @Test
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
    public void Main1Activity01() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:填写准备金,准备金：222,设置准备金成功");
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
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
        Thread.sleep(1000);
        Assert.assertTrue("设置准备金失败", driver.findElementById(pageName + ":id/balance_tv") != null);
    }

    @Test
    public void Main1Activity02() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:不填写准备金,提示\"准备金不允许为空\"");
        //等待pos登录
        Thread.sleep(10 * 1000);
        //输入用户名和密码，点击登录，等待跳转
        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
        driver.findElementById(pageName + ":id/password").click();//点击密码框
        clearText(driver.findElementById(pageName + ":id/password"));//清除密码框的内容
        driver.findElementById(pageName + ":id/password").sendKeys("000000");//输入密码
        driver.findElementById(pageName + ":id/login").click();//点击登录
        Thread.sleep(10 * 1000);
        Assert.assertTrue("登录失败了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        System.out.println("Staff Login Success");

        //进入到MainActivity：设置准备金
        driver.findElementById(pageName + ":id/confirm_reserve").click();
        Thread.sleep(1000);
        Assert.assertTrue("设置准备金居然成功了！", driver.findElementById(pageName + ":id/confirm_reserve") != null);
    }

    /**
     * case3:已经无法输入其他字符
     */

    @Test
    public void Main1Activity04() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:填写准备金,准备金：0,设置准备金成功");
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
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();
        Thread.sleep(1000);
        Assert.assertTrue("设置准备金失败", driver.findElementById(pageName + ":id/balance_tv") != null);
    }

    @Test
    public void Main1Activity05() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:无商品点击结账，提示商品数量为空，不跳转支付页面");

        posLogin();
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        Assert.assertTrue("支付页面可能已经被打开", driver.findElementById(pageName + ":id/balance_tv") != null);
    }

    @Test
    public void Main1Activity06() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:1.选择商品\n" +
                "2.移除商品\n" +
                "3.点击结算\n" +
                "提示商品列表为空，不跳转支付页面");
        posLogin();
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
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

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);

        //移除商品
        new TouchAction(driver).longPress(driver.findElementsById(pageName + ":id/commodity_id").get(0)).waitAction(1000).perform();//长按
        driver.findElementById(pageName + ":id/remove_commodity").click();
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        Assert.assertTrue("支付页面可能已经被打开", driver.findElementById(pageName + ":id/balance_tv") != null);
    }

    @Test
    public void Main1Activity07() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:商品列表不为空,跳转支付页面");
        posLogin();
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
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

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        Thread.sleep(2000);
        Assert.assertTrue("支付页面可能已经被打开", driver.findElementById(pageName + ":id/balance_tv") != null);
//        Assert.assertTrue("支付页面没有被打开", driver.findElementById(pageName+":id/pay")!=null);

    }

    /**
     * case 8-13:为退货的测试，在CheckList_C_appium中
     * case 14:新界面已经没有这种操作
     */

    @Test
    public void Main1Activity15() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:修改商品数量为99999；点击确定后商品数量重置为9999，并提示用户最大为9999");
        posLogin();
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
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

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
        driver.findElementsById(pageName + ":id/commodity_id").get(0).click();
        AndroidElement element = driver.findElementById(pageName + ":id/t9");//循环点击数字9
        for (int i = 0; i < 5; i++) {
            element.click();
        }
        driver.findElementById(pageName + ":id/confirm_reserve").click();//点击确定
        Thread.sleep(3 * 1000);
        Assert.assertTrue("商品数量超过9999！", driver.findElementById(pageName + ":id/number").getText().equals("9999"));
    }

    /**
     * case 16-24：为查单退货的测试，在CheckList_C_appium中
     * case 25-41：为插入扫码枪，暂无法测试
     * case 42-44：为零售单同步线程，暂无法测试
     * case 45-53：收银汇总新流程测试用例，需要联合服务器查看，暂无法完成
     * case 54-63：为跨天上班，暂无法完成；
     */

    @Test
    public void Main1Activity64() throws InterruptedException, MalformedURLException {
        System.out.println("---------case:在进入交班页面再回主页面，看看待售商品和商品数量和价格等有没有更改（目的是没有的）");
        posLogin();
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
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

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
        String commodity_id = driver.findElementById(pageName + ":id/commodity_id").getText();
        String bar_code = driver.findElementById(pageName + ":id/bar_code").getText();
        String name_attr = driver.findElementById(pageName + ":id/name_attr").getText();
        String unit = driver.findElementById(pageName + ":id/unit").getText();
        String number = driver.findElementById(pageName + ":id/number").getText();
        String unit_price = driver.findElementById(pageName + ":id/unit_price").getText();
        String discount = driver.findElementById(pageName + ":id/discount").getText();
        String after_discount = driver.findElementById(pageName + ":id/after_discount").getText();
        String total_money = driver.findElementById(pageName + ":id/total_money").getText();
        String remark = driver.findElementById(pageName + ":id/remark").getText();

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/back").click();//点击返回

        Assert.assertTrue("退出交班页面后，数据不一致！", driver.findElementById(pageName + ":id/commodity_id").getText().equals(commodity_id)
                && driver.findElementById(pageName + ":id/bar_code").getText().equals(bar_code)
                && driver.findElementById(pageName + ":id/name_attr").getText().equals(name_attr)
                && driver.findElementById(pageName + ":id/unit").getText().equals(unit)
                && driver.findElementById(pageName + ":id/number").getText().equals(number)
                && driver.findElementById(pageName + ":id/unit_price").getText().equals(unit_price)
                && driver.findElementById(pageName + ":id/discount").getText().equals(discount)
                && driver.findElementById(pageName + ":id/after_discount").getText().equals(after_discount)
                && driver.findElementById(pageName + ":id/total_money").getText().equals(total_money)
                && driver.findElementById(pageName + ":id/remark").getText().equals(remark));
    }

    @Test
    public void Main1Activity65() throws InterruptedException, MalformedURLException {
        System.out.println("---------case:在进入支付页面支付完成再回主页面，看看上一单的信息是否符合刚零售的信息");
        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
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

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
        String total_money = driver.findElementById(pageName + ":id/total_money").getText();
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(2 * 1000);
        Assert.assertTrue("前一单的金额不正确", driver.findElementById(pageName + ":id/last_amount").getText().equals(total_money));
    }

    /**
     * case 66：为session计时器测试，暂无法完成
     */


}
