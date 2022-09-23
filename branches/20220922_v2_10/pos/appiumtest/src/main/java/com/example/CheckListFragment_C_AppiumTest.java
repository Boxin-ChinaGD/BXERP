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
 * Created by WPNA on 2020/3/23.
 * 参照的手动测试文档为：MainActivity C 8-13；16-24
 * 测试账号为：15854320895
 */

public class CheckListFragment_C_AppiumTest extends BaseAppiumTest {

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

    @Test
    public void loginTest() throws InterruptedException, MalformedURLException {
//        // 重新安装apk
//        capabilities.setCapability("noReset", false);
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//
//        //填写公司编号
//        Thread.sleep(5000);
//        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();//允许权限
//        Thread.sleep(3000);
//        driver.findElementById(pageName+":id/companysn").sendKeys("668866");
//        driver.findElementById(pageName+":id/input_companysn_submit").click();
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
    public void CheckListFragment08() throws InterruptedException {
        System.out.println("----------------case:当有网进行退货，支付方式为现金或者微信或者混合支付都能退货成功");
        /**
         * 跑此测试时需要：LS2019123100010100100006（微信支付），（现金单立即销售生成），LS2019123100010100100001（现金微信混合支付）未退货过
         *
         */
        //登录POS
        posLogin();

        //--------------------------查微信支付单
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
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

        //driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS2019072933333300013333");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索

        //退货
        Thread.sleep(3 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号
        driver.findElementById(pageName + ":id/confirm_return_goods").click();//点击退货
        Thread.sleep(2 * 1000);

        Assert.assertTrue("这张单有问题，不能退货！", driver.findElementById("android:id/button1") != null);
        driver.findElementById("android:id/button1").click();//确定按钮

        //--------------------------------查现金单
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(90 * 1000);

        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        AndroidElement elementById = driver.findElementById(pageName + ":id/num_delete");
        for (int i = 0; i < 24; i++) {
            elementById.click();//点击键盘删除键
        }
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        //退货
        Thread.sleep(2 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号

        Assert.assertTrue("这张单有问题，不能退货！", driver.findElementById(pageName + ":id/confirm_return_goods") != null);
        driver.findElementById(pageName + ":id/confirm_return_goods").click();//点击退货
        Thread.sleep(2 * 1000);
        driver.findElementById("android:id/button1").click();//确定按钮

        //-----------------------------------查微信现金混合支付单
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/num_change_en").click();
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
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS2019011501010100021248");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索

        //退货
        Thread.sleep(2 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号

        Assert.assertTrue("这张单有问题，不能退货！", driver.findElementById(pageName + ":id/confirm_return_goods") != null);
        driver.findElementById(pageName + ":id/confirm_return_goods").click();//点击退货
        Thread.sleep(2 * 1000);
        driver.findElementById("android:id/button1").click();//确定按钮

    }

    @Test
    public void CheckListFragment09() throws InterruptedException {
        System.out.println("----------------case:当有网进行退货，点击确认退货时，断网，支付方式为微信");
        System.out.println("弹出提示：“网络故障，请稍后再试！”");
        //此测试需 LS2019123100010100100007（微信支付单），LS2019123100010100100002（微信现金支付单）未退货过;
        //登录POS
        posLogin();

        //--------------------------查微信支付单
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
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索

        //退货
        Thread.sleep(2 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号
        Thread.sleep(2 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/confirm_return_goods").click();//点击退货
        Thread.sleep(5 * 1000);

        //-------------------查微信现金混合支付单
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();  //如果执行到这里失败了，那上面可能退货成功了；如果当前状态为未断网，则为未断网引起的退货成功；
        driver.findElementById(pageName + ":id/num_change_en").click();
        AndroidElement elementById = driver.findElementById(pageName + ":id/english_delete");
        for (int i = 0; i < 24; i++) {
            elementById.click();//点击键盘删除键
        }
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
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS2019011501010100021251");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();        //点击搜索
        //退货
        Thread.sleep(2 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/confirm_return_goods").click();//点击退货
        Thread.sleep(6 * 1000);

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);

    }

    /**
     * case 10:始终无网的情况下，无法搜索单号，所以也无法点击退货；
     * 如果是先搜单号，再断网去退货，则与case 9相同；
     * case 11:暂时无法实现：需要俩台pos机对一张零售单进行退货
     */

    @Test
    public void CheckListFragment12() throws InterruptedException {
        /**
         * 1. 结算商品前断网
         * 2. 结算商品
         * 3. 查单，对该零售单退货某些商品
         * 4. 退货后交班
         * 5. 交班后联网重新登录
         */
        System.out.println("----------------case:结算商品前断网，查单，对该零售单退货某些商品，交班后联网重新登录");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);
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

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567");//输入条形码
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        //2. 结算商品
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/pay").click();//支付

        //3. 查单，对该零售单退货某些商品
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(2 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Thread.sleep(110 * 1000);//新结算的零售单需要120秒后才能退货
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号

        Assert.assertTrue("这张单有问题，不能退货！", driver.findElementById(pageName + ":id/confirm_return_goods") != null);
        driver.findElementById(pageName + ":id/confirm_return_goods").click();//点击退货
        Thread.sleep(2 * 1000);
        driver.findElementById("android:id/button1").click();//确定按钮

        //4. 退货后交班
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);

        //5.交班后联网重新登录
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/login").click();//点击登录
        System.out.println("正在等待跳转至Base1FragmentActivity");
        Thread.sleep(20 * 1000);
        Assert.assertTrue("登录失败了！", "com.bx.erp.view.activity.Base1FragmentActivity".equals(driver.currentActivity()));
        System.out.println("Staff Login Success");
    }

    /**
     * 联网结算后对该零售单进行退货，然后交班，登录。结果：登录成功
     */
    @Test
    public void CheckListFragment13() throws InterruptedException {
        /**
         * 暂不理解这个测试是如何测的
         */
    }

    /**
     * case 16:疯狂点击上下页按钮无法用appium实现；
     */

    @Test
    public void CheckListFragment17() throws InterruptedException {
        System.out.println("--------case：查出来的零售单创建时间应该在选择好的开始和结束日期之间");
        /**
         * 选择好日期后，点击搜索按钮，再点击上/下一页按钮
         * 断网 联网俩种情况
         */

        //----------------------联网测试
        posLogin();
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/check_list_startDate").click();//选择开始时间
        AndroidElement e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 10; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        driver.findElementById(pageName + ":id/check_list_endDate").click();//选择结束时间
        driver.findElementById("android:id/button1").click();//日历的确定按钮

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(6) != null);

        //----------------------断网测试
        System.out.println("断网情况下，可能存在本地零售单，所以可能有数据，可能没有");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(3 * 1000);//断网之后第一次搜索是搜不到内容的，得再一次搜索
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(5 * 1000);
        //如果是按照本测试类的流程测试下来，应该有单号
        Assert.assertTrue("搜索不到本地零售单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网,以免影响其他测试
        Thread.sleep(3 * 1000);
    }

    @Test
    public void CheckListFragment18() throws InterruptedException {
        System.out.println("-----------case:在查单页面，如果输入了单据号(符合查询单据号的格式),点击搜索按钮，这时候搜出总页数是N页，\n" +
                " 且都是按单据号模糊搜索出来的，那么点击上/下一页按钮的时候，总页数也是N页");
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS20190810");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);
        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        driver.findElementById(pageName + ":id/next_page").click();//点击下一页
        Thread.sleep(6 * 1000);
        Assert.assertTrue("跟之前的总页数不一致！", driver.findElementById(pageName + ":id/total_page").getText().equals(amount));
        driver.findElementById(pageName + ":id/last_page").click();//点击上一页
        Thread.sleep(6 * 1000);
        Assert.assertTrue("跟之前的总页数不一致！", driver.findElementById(pageName + ":id/total_page").getText().equals(amount));
    }

    @Test
    public void CheckListFragment19() throws InterruptedException {
        System.out.println("--------case：查出来的零售单创建时间应该在选择好的开始和结束日期之间");

        /**
         * 选择好日期后，点击搜索按钮，再点击上/下一页按钮
         * 修改时间条件或单据，没有点击搜索按钮，直接点击上/下一个按钮，那么还是按原来的时间或单据条件，不按最新输入的时间条件查询
         * 断网 联网俩种情况
         */

        //----------------------联网测试
        posLogin();
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/check_list_startDate").click();//选择开始时间
        AndroidElement e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 10; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        driver.findElementById(pageName + ":id/check_list_endDate").click();//选择结束时间
        driver.findElementById("android:id/button1").click();//日历的确定按钮

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(5 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(6) != null);

        //获取第二页的第一条数据
        driver.findElementById(pageName + ":id/next_page").click();//点击下一页
        Thread.sleep(6 * 1000);
        String retailtrade_sn = driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).getText();//获取到第二页第一条的单据号
        System.out.println("单据号：" + retailtrade_sn);
        //返回上一页
        driver.findElementById(pageName + ":id/last_page").click();//点击上一页
        Thread.sleep(6 * 1000);

        //修改时间条件
        driver.findElementById(pageName + ":id/check_list_startDate").click();//选择开始时间
        e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 2; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        //不点击搜索，点击下一页
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/next_page").click();//点击下一页
        Thread.sleep(6 * 1000);
        Assert.assertTrue("数据不一致！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).getText().equals(retailtrade_sn));

        //----------------------断网测试
        /**
         * 不进行断网测试，可能本地的数据不足，无法点击下一页，会造成此测试无法通过
         */
    }

    @Test
    public void CheckListFragment20() throws InterruptedException {

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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS20190810");//输入单号
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);
        String amount = driver.findElementById(pageName + ":id/total_page").getText();//获取搜索后的总页数
        driver.findElementById(pageName + ":id/next_page").click();//点击下一页
        Thread.sleep(6 * 1000);
        Assert.assertTrue("跟之前的总页数不一致！", driver.findElementById(pageName + ":id/total_page").getText().equals(amount));
        driver.findElementById(pageName + ":id/last_page").click();//点击上一页
        Thread.sleep(6 * 1000);
        Assert.assertTrue("跟之前的总页数不一致！", driver.findElementById(pageName + ":id/total_page").getText().equals(amount));

        //修改输入的单据号不点击搜索
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/num_change_en").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS2000011907423700011455");//输入单号
        driver.findElementById(pageName + ":id/next_page").click();//点击下一页
        Thread.sleep(6 * 1000);
        System.out.println("观察页面数据是否不止一条数据，是的话，测试通过");
        Thread.sleep(3000);
    }

    @Test
    public void CheckListFragment21() throws InterruptedException {
        System.out.println("-----------------case:在查单页面，选好了时间条件和输入了单据号条件，点击查询按钮，查询出来的零售单\n" +
                " 都是以时间条件和单据号条件查询出来的。然后修改时间条件或单据号条件，不点击\n" +
                " 搜索按钮，那么点击上/下一页按钮的时候，还是以上一次搜索按钮给出的条件查询");

        //1.在查单页面，选好了时间条件和输入了单据号条件，点击查询按钮
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

        //driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS20190810");//输入单号
        driver.findElementById(pageName + ":id/check_list_startDate").click();//选择开始时间
        AndroidElement e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 10; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        driver.findElementById(pageName + ":id/check_list_endDate").click();//选择结束时间
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(6) != null);

        //获取第二页的第一条数据
        driver.findElementById(pageName + ":id/next_page").click();//点击下一页
        Thread.sleep(6 * 1000);
        String retailtrade_sn = driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).getText();//获取到第二页第一条的单据号
        System.out.println("单据号：" + retailtrade_sn);

        //返回上一页
        driver.findElementById(pageName + ":id/last_page").click();//点击上一页
        Thread.sleep(6 * 1000);

        //修改时间条件
        driver.findElementById(pageName + ":id/check_list_startDate").click();//选择开始时间
        e = driver.findElementById("android:id/prev");
        for (int i = 0; i < 2; i++) {
            e.click();//点击上一个月
        }
        driver.findElementsById("android:id/month_view").get(0).click();//点击日期
        driver.findElementById("android:id/button1").click();//日历的确定按钮
        //修改单据号
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/check_list_sn").click();
        driver.findElementById(pageName + ":id/num_change_en").click();
        driver.findElementById(pageName + ":id/t_l").click();
        driver.findElementById(pageName + ":id/t_s").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/check_list_sn").sendKeys("LS2000011917261200011457");//输入单号
        //不点击搜索，点击下一页
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/next_page").click();//点击下一页
        Thread.sleep(6 * 1000);
        Assert.assertTrue("数据不一致！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).getText().equals(retailtrade_sn));

    }

    @Test
    public void CheckListFragment22() throws InterruptedException {
        System.out.println("-----------case:创建零售单后立即去查单退货，结果应该是退货不成功");
        /**
         * 1.创建零售单，立马去点击查单
         * 2.选中零售单，点击退货
         */
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567801");//输入条形码
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();//支付
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(5 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号
        Thread.sleep(500);
        Assert.assertTrue("居然可以退货！可能刚结算的商品查单没查出,或者appium运行太慢了，导致超过120s",
                driver.findElementById(pageName + ":id/confirm_return_goods") == null);

    }

    @Test
    public void CheckListFragment23() throws InterruptedException {
        System.out.println("--------------case:创建零售单后立即去查单，然后断网退货，结果应该是退货不成功");
        /**
         * 1.创建零售单，立马去点击查单
         * 2.查出单后，点击断网
         * 3.选中零售单，点击退货
         */
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567805");//输入条形码
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();//支付
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(10 * 1000);

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号
        Thread.sleep(500);
        Assert.assertTrue("居然可以退货！可能刚结算的商品查单没查出,或者appium运行太慢了，导致超过120s",
                driver.findElementById(pageName + ":id/confirm_return_goods") == null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
    }

    @Test
    public void CheckListFragment24() throws InterruptedException {
        System.out.println("-------------case: 选中超过120s的零售单，点击退货，正常退货成功");
        /**
         * 超过120s的零售单不好找，自己生成了一个零售单并进行结算
         */
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(5 * 1000);

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

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567");//输入条形码
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();//支付

        //3. 查单，对该零售单退货某些商品
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();//点击查单
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/check_list_search").click();//点击搜索
        Thread.sleep(5 * 1000);
        Assert.assertTrue("搜索不到单号！", driver.findElementsById(pageName + ":id/retailtrade_sn").get(0) != null);
        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();//获取第一个的item
        Thread.sleep(110 * 1000);//新结算的零售单需要120秒后才能退货
        driver.findElementById(pageName + ":id/reduce_number").click();//点击减号

        Assert.assertTrue("这张单有问题，不能退货！", driver.findElementById(pageName + ":id/confirm_return_goods") != null);
        driver.findElementById(pageName + ":id/confirm_return_goods").click();//点击退货
        Thread.sleep(2 * 1000);
        driver.findElementById("android:id/button1").click();//确定按钮
    }

}
