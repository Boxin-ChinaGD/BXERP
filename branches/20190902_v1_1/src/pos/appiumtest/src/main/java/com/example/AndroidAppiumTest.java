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
//import io.appium.java_client.NetworkConnectionSetting;
//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.android.AndroidElement;
//import io.appium.java_client.android.AndroidKeyCode;
//
//public class AndroidAppiumTest extends BaseAppiumTest {
//
//    public void setUp() throws MalformedURLException, InterruptedException {
//        super.setUp();
//
//    }
//
//    public void posLogin() throws InterruptedException {
//        //等待pos登录
//        Thread.sleep(10 * 1000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//    }
//
//    @Test
//    public void loginTest() throws InterruptedException, MalformedURLException {
//        // 重新安装apk
//        capabilities.setCapability("noReset", false);
//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//
//        //填写公司编号
//        Thread.sleep(5000);
//        driver.findElement(By.xpath("//android.widget.Button[contains(@text,'允许')]")).click();
//        Thread.sleep(3000);
//        driver.findElementById(pageName + ":id/companysn").sendKeys("668866");
//        driver.findElementById(pageName + ":id/input_companysn_submit").click();
//        Thread.sleep(5000);
//        //等待pos登录
//        posLogin();
//        //进入设置界面进行基础资料重置
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/set_up").click();
//        String loc_text = "new UiSelector().text(\"数据处理\")";
//        driver.findElementByAndroidUIAutomator(loc_text).click();
//        driver.findElementById(pageName + ":id/sync_base_data").click();
//        Thread.sleep(5 * 1000);
//        driver.findElementById(pageName + ":id/sync_base_data").click();
//        Thread.sleep(70 * 1000);
//        driver.findElementById(pageName + ":id/reset_back").click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/set_up_back").click();
//        Thread.sleep(2 * 1000);
//
//        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        // 该测试开始前把不重新安装App设为false，在这里设置回来。
//        capabilities.setCapability("noReset", true);
//
////        //进入设置界面
////        Thread.sleep(3 * 1000);
////        driver.findElementById(pageName+":id/set_up").click();
////
////        //通过文本定位并点击“打印与外设”
////        loc_text = "new UiSelector().text(\"打印与外设\")";
////        driver.findElementByAndroidUIAutomator(loc_text).click();
////        driver.findElementById(pageName+":id/small_ticket").click();
////        Thread.sleep(5 * 1000);
//    }
//
//    public void deletePayNumber() {
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest() throws Exception {
//
//        //等待POS登录
//        posLogin();
//        /**
//         *支付的UI流程：
//         * 1.在主页面中点击扫描条形码框
//         * 2.输入条形码选择需要购买的商品并点击加入
//         * 3.点击结算
//         * 4.点击支付
//         * 5.点击支付完成
//         */
//        System.out.println("----------------------case：支付UI中不选取商品进行支付");
//        driver.findElementById(pageName + ":id/balance_tv").click();
//
//        //TODO 由于上面触发点击事件之后，toast消息框消失了之后才运行下面代码，所以下面代码无法捕获toast消息，导致出错。
////        String toast_message = "商品列表为空";
////        final WebDriverWait wait = new WebDriverWait(driver, 10);
////        WebElement ele = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@text,'" + toast_message + "')]")));
////        Assert.assertTrue("界面提示消息不正确！", toast_message.equals(ele.getText()));
//    }
//
//    /**
//     * 以下支付界面的测试用例是根据元粒度测试文档PaymentActivity中D栏编写
//     * 其中用例1~10暂时无法实现(微信支付需要扫码枪，沙箱环境下无法进行微信支付，会提示支付失败)
//     */
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest11() throws Exception {
//        System.out.println("----------------------case11:点击需要购买的商品进行支付");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest12() throws Exception {
//        System.out.println("----------------------case12:第一次支付金额小于总价，第二次支付金额为待付余额");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//        driver.findElementById(pageName + ":id/number_1_pay").click();
//        driver.findElementById(pageName + ":id/pay").click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/pay").click();
//        Thread.sleep(5 * 1000);
//
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest13() throws Exception {
//        System.out.println("----------------------case13:填写支付金额大于总价");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//        for (int i = 0; i < 4; i++) {
//            driver.findElementById(pageName + ":id/number_9_pay").click();
//        }
//        driver.findElementById(pageName + ":id/pay").click();
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest14() throws Exception {
//        System.out.println("----------------------case14:将支付金额清空，点击支付");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        //把金额删掉重新输入一个0
//        deletePayNumber();
//        driver.findElementById(pageName + ":id/pay").click();
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest15() throws Exception {
//        System.out.println("----------------------case15:填写支付金额为0");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        //把金额删掉重新输入一个0
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//        driver.findElementById(pageName + ":id/number_0_pay").click();
//        driver.findElementById(pageName + ":id/pay").click();
//        driver.findElementById(pageName + ":id/pay").click();
//        Thread.sleep(5 * 1000);
//    }
//
//    /**
//     * 用例16~19暂时无法实现
//     */
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest20() throws Exception {
//        System.out.println("----------------------case20:输入付款金额时，只能输入两位小数");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        //把金额删掉重新输入一个0
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//        driver.findElementById(pageName + ":id/number_2_pay").click();
//        driver.findElementById(pageName + ":id/number_point_pay").click();
//        driver.findElementById(pageName + ":id/number_3_pay").click();
//        driver.findElementById(pageName + ":id/number_6_pay").click();
//        driver.findElementById(pageName + ":id/number_5_pay").click();
//        Thread.sleep(5 * 1000);
//    }
//
//    /**
//     * 用例21与用例13相同
//     */
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest22() throws Exception {
//        System.out.println("----------------------case22:输入付款金额时多次输入小数点");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        //把金额删掉
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//        driver.findElementById(pageName + ":id/number_2_pay").click();
//        driver.findElementById(pageName + ":id/number_point_pay").click();
//        driver.findElementById(pageName + ":id/number_point_pay").click();
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest23() throws Exception {
//        System.out.println("----------------------case23:销售进行到一半，支付了部分金额，进行取消支付");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        //把金额删掉
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//        driver.findElementById(pageName + ":id/number_1_pay").click();
//        driver.findElementById(pageName + ":id/pay").click();//支付部分金额
//        driver.findElementById(pageName + ":id/payment_back").click();//取消支付
//        Thread.sleep(3 * 1000);
//        driver.findElementById("android:id/button1").click();//确定取消
//
//        Thread.sleep(5 * 1000);
//    }
//
//    /**
//     * 用例24~27暂时无法实现
//     */
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest28() throws Exception {
//        System.out.println("----------------------case28:销售进行到一半进行取消");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/payment_back").click();//取消支付
//        Thread.sleep(3 * 1000);
//        driver.findElementById("android:id/button1").click();//确定取消
//        Thread.sleep(5 * 1000);
//    }
//
//    /* 用例29~31暂时无法实现
//     * 用例32~33已删除
//     * 用例34~35暂时无法实现
//     * 用例36与用例13相同
//     * 用例37暂时无法实现(连续点击支付)
//     * 用例38~39暂时无法实现*/
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest40() throws Exception {
//        System.out.println("----------------------case40:将(微信)支付金额清空后切换到现金支付模式");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        //把金额删掉
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest41() throws Exception {
//        System.out.println("----------------------case41:将现金支付金额清空后切换到微信支付模式");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        //把金额删掉
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//        driver.findElementById(pageName + ":id/wechat_pay").click();//微信支付
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest42() throws Exception {
//        System.out.println("----------------------case42:现金支付完成后打印小票");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("8965348932854");
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        // ...打印小票，需要肉眼验证小票格式是当前使用的小票格式
//        Thread.sleep(5 * 1000);
//    }
//
//    /**
//     * 用例43~44暂时无法实现
//     */
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest45() throws Exception {
//        System.out.println("----------------------case45:商品名称超过一行时，打印小票");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("multiPackagCommBarcodesC");
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        // ...打印小票，需要肉眼验证小票格式是当前使用的小票格式
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest46() throws Exception {
//        System.out.println("----------------------case46:零售多个商品，打印小票");
//
//        //等待POS登录
//        posLogin();
//
//        for (int i = 1; i <= 5; i++) {
//            driver.findElementById(pageName + ":id/scan_barcode_search").click();
//            driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("123456780" + i);
//            driver.findElementById(pageName + ":id/name_attr").click();
//            driver.findElementById(pageName + ":id/add_tv").click();
//        }
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        // ...打印小票，需要肉眼验证小票格式是当前使用的小票格式
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest47() throws Exception {
//        System.out.println("----------------------case47:有促销参与时，打印小票");
//
//        //等待POS登录
//        posLogin();
//
//        for (int i = 0; i < 2; i++) {
//            driver.findElementById(pageName + ":id/scan_barcode_search").click();
//            driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567805");
//            driver.findElementById(pageName + ":id/name_attr").click();
//            driver.findElementById(pageName + ":id/add_tv").click();
//        }
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        // ...打印小票，需要肉眼验证小票格式是当前使用的小票格式
//        Thread.sleep(5 * 1000);
//    }
//
//    /**
//     * 用例48暂时无法实现
//     * 用例49与用例46重复了
//     */
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest50() throws Exception {
//        System.out.println("----------------------case50:支付完成后查单");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567805");
//        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        // 查单
//        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        Thread.sleep(5 * 1000);
//        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/commodity_name").click();
//        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
//        Assert.assertTrue("商品名字不一样！", commodityName_checkList.equals(commodityName_scanBarcode));
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest51() throws Exception {
//        System.out.println("----------------------case51:支付部分金额后返回上一页面");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567805");
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        //把金额删掉
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//        driver.findElementById(pageName + ":id/number_1_pay").click();
//        driver.findElementById(pageName + ":id/pay").click();
//        //取消支付
//        driver.findElementById(pageName + ":id/cancel_pay").click();
//        Thread.sleep(2 * 1000);
//        driver.findElementById("android:id/button1").click();
//        Thread.sleep(5 * 1000);
//    }
//
//    /**
//     * 用例52~58暂时无法实现
//     */
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest59() throws Exception {
//        System.out.println("----------------------case59:支付部分金额后返回上一页面，再次支付，成功");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567805");
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        //把金额删掉
//        for (int i = 0; i < 7; i++) {
//            driver.findElementById(pageName + ":id/number_delete_pay").click();
//        }
//        driver.findElementById(pageName + ":id/number_1_pay").click();
//        driver.findElementById(pageName + ":id/pay").click();
//        //取消支付
//        driver.findElementById(pageName + ":id/cancel_pay").click();
//        Thread.sleep(2 * 1000);
//        driver.findElementById("android:id/button1").click();
//        //再次点击支付
//        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        Thread.sleep(5 * 1000);
//    }
//
//    /**
//     * 用例60~62暂时无法实现
//     */
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest63() throws Exception {
//        System.out.println("----------------------case63:现金支付后断网查单，再联网查单");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567804");
//        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        //断网
//        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));
//        //查单
//        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        Thread.sleep(2 * 1000);
//        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/commodity_name").click();
//        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
//        //商品名字理论上不一样（零售单没有上传到服务器）
//        Assert.assertFalse("商品名字居然一样！", commodityName_checkList.equals(commodityName_scanBarcode));
//        //重新联网
//        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
//        Thread.sleep(20 * 1000);
//        //再次查单
//        Thread.sleep(5 * 1000);
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        Thread.sleep(5 * 1000);
//        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/commodity_name").click();
//        commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
//        //商品名字理论上不一样（零售单没有上传到服务器）
//        Assert.assertFalse("商品名字居然一样！", commodityName_checkList.equals(commodityName_scanBarcode));
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest64() throws Exception {
//        System.out.println("----------------------case64:现金支付后断网查单，再退出登录联网查单");
//
//        //等待POS登录
//        posLogin();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567802");
//        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        //断网
//        int i = driver.getNetworkConnection().value;
//        System.out.println(i);
//        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));
//        //查单
//        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
//        Thread.sleep(2 * 1000);
//        //输入日期查单
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        Thread.sleep(2 * 1000);
//        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/commodity_name").click();
//        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
//        //商品名字理论上不一样（零售单没有上传到服务器）
//        Assert.assertTrue("商品名字居然不一样！", commodityName_checkList.equals(commodityName_scanBarcode));
//        //重新联网
//        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
//        //交班退出
//        driver.pressKeyCode(AndroidKeyCode.BACK);
//        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName + ":id/logout").click();
//        Thread.sleep(2 * 1000);
//        driver.findElementById("android:id/button1").click();
//        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName + ":id/change_shifts").click();
//        Thread.sleep(5 * 1000);
//        //重新登录
//        posLogin();
//        //再次查单
//        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        Thread.sleep(5 * 1000);
//        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/commodity_name").click();
//        commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
//        //商品名字理论上一样
//        Assert.assertTrue("商品名字居然不一样！", commodityName_checkList.equals(commodityName_scanBarcode));
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest65() throws Exception {
//        System.out.println("----------------------case65:断网后登陆并售卖商品现金支付后查单，再联网查单");
//
//        //断网
//        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));
//        //等待POS登录
//        Thread.sleep(10 * 1000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        // 提示离线模式，点确定
//        Thread.sleep(2 * 1000);
//        driver.findElementById("android:id/button1").click();
//
//        //进入到MainActivity：设置准备金
//        Thread.sleep(5 * 1000);
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567801");
//        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        //查单
//        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        Thread.sleep(2 * 1000);
//        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/commodity_name").click();
//        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
//        //商品名字理论上不一样（零售单没有上传到服务器）
//        Assert.assertTrue("商品名字居然不一样！", commodityName_checkList.equals(commodityName_scanBarcode));
//        //返回
//        driver.pressKeyCode(AndroidKeyCode.BACK);
//        //重新联网
//        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
//        //再次查单
//        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        Thread.sleep(5 * 1000);
//        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/commodity_name").click();
//        commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
//        //商品名字理论上不一样（零售单没有上传到服务器）
//        Assert.assertFalse("商品名字居然一样！", commodityName_checkList.equals(commodityName_scanBarcode));
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest66() throws Exception {
//        System.out.println("----------------------case66:断网后卖商品并现金支付后查单，再退出登录联网查单");
//
//        //断网
//        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));
//        //等待POS登录
//        Thread.sleep(10 * 1000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        // 提示离线模式，点确定
//        Thread.sleep(2 * 1000);
//        driver.findElementById("android:id/button1").click();
//
//        //进入到MainActivity：设置准备金
//        Thread.sleep(5 * 1000);
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("1234567805");
//        String commodityName_scanBarcode = driver.findElementById(pageName + ":id/name_attr").getText();
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//        //查单
//        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        Thread.sleep(2 * 1000);
//        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/commodity_name").click();
//        String commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
//        //商品名字理论上不一样（零售单没有上传到服务器）
//        Assert.assertFalse("商品名字居然一样！", commodityName_checkList.equals(commodityName_scanBarcode));
//        //重新联网
//        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
//        //交班退出
//        driver.pressKeyCode(AndroidKeyCode.BACK);
//        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName + ":id/logout").click();
//        Thread.sleep(2 * 1000);
//        driver.findElementById("android:id/button1").click();
//        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName + ":id/change_shifts").click();
//        Thread.sleep(5 * 1000);
//        //重新登录
//        posLogin();
//        //再次查单
//        driver.findElementById(pageName + ":id/btnQueryRetailTradeList").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        Thread.sleep(5 * 1000);
//        driver.findElementsById(pageName + ":id/retailtrade_sn").get(0).click();
//        Thread.sleep(1 * 1000);
//        driver.findElementById(pageName + ":id/commodity_name").click();
//        commodityName_checkList = driver.findElementById(pageName + ":id/commodity_name").getText();
//        //商品名字理论上一样
//        Assert.assertTrue("商品名字居然不一样！", commodityName_checkList.equals(commodityName_scanBarcode));
//        Thread.sleep(5 * 1000);
//    }
//
//    /**
//     * 用例67~69有微信支付，暂时无法实现
//     * 用例70与用例23一样
//     * 用例71涉及微信支付，暂时无法实现
//     */
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest72() throws Exception {
//        System.out.println("----------------------case72:售卖一个或一批包含价格为0的商品，验证能售卖成功");
//
//        //等待POS登录
//        posLogin();
//        //添加价格为0的商品
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("priceis0");
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        //添加其他商品
//        for (int i = 1; i <= 5; i++) {
//            driver.findElementById(pageName + ":id/scan_barcode_search").click();
//            driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("123456780" + i);
//            driver.findElementById(pageName + ":id/name_attr").click();
//            driver.findElementById(pageName + ":id/add_tv").click();
//        }
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void paymentActivityTest73() throws Exception {
//        System.out.println("----------------------case73:查看所有无优惠支付测试打印的小票，看看优惠是否存在-0.00的情况");
//
//        //等待POS登录
//        posLogin();
//        //添加价格为0的商品
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("priceis0");
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        //添加其他商品
//        driver.findElementById(pageName + ":id/scan_barcode_search").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("123456780");
//        driver.findElementById(pageName + ":id/name_attr").click();
//        driver.findElementById(pageName + ":id/add_tv").click();
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
//        driver.findElementById(pageName + ":id/pay").click();
//
//        Thread.sleep(5 * 1000);
//    }
//
//    //coding=utf-8
//    @Test
//    public void refundTest() throws InterruptedException {
//        //等待pos登录
//        Thread.sleep(5000);
//        /**
//         * 退货的UI流程：
//         * 1.点击查单按钮
//         * 2.输入相关条件进行查询
//         * 3.选择需要进行退款的零售单和商品信息等
//         * 4.点击退货按钮
//         * 5.点击退款按钮
//         */
//        System.out.println("----------------------- case1: 查询零售单进行退货退款");
//        driver.findElementById(pageName + ":id/check_list").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        //...选取零售单
//
//        System.out.println("----------------------- case2: 查询零售单，退货商品数量大于购买数量进行退货退款");
//        driver.findElementById(pageName + ":id/check_list").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//
//        System.out.println("----------------------- case3: 进行退货退款时退款金额小于0或大于应退金额");
//        driver.findElementById(pageName + ":id/check_list").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//        /**
//         * case :
//         *  3).查询零售单进行退货退款
//         4）查询零售单，退货商品数量大于购买数量进行退货退款
//         5) 进行退货退款时退款金额小于0或大于应退金额
//         */
//    }
//
//    @Test
//    public void smallSheetActivityTest() throws InterruptedException, MalformedURLException {
//        //等待pos登录
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("222");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        //进入设置界面
//        Thread.sleep(3 * 1000);
//        driver.findElementById(pageName + ":id/set_up").click();
//
//        //通过文本定位并点击“打印与外设”
//        String loc_text = "new UiSelector().text(\"打印与外设\")";
//        driver.findElementByAndroidUIAutomator(loc_text).click();
//        driver.findElementById(pageName + ":id/small_ticket").click();
//        Thread.sleep(5 * 1000);
//
//        //设置页眉，页脚，底部文字，添加页脚并向上滑动
//        driver.findElementById(pageName + ":id/header").sendKeys("页眉");
//        driver.findElementById(pageName + ":id/ticket_bottom").sendKeys("底部文字");
//        driver.findElementById(pageName + ":id/footer1").sendKeys("页脚1");
//        driver.findElementById(pageName + ":id/add_footer").click();
//        driver.swipe(439, 659, 439, 200, 200);
//
//        //点击打印测试
//        String loc_text1 = "new UiSelector().text(\"打印测试\")";
//        driver.findElementByAndroidUIAutomator(loc_text1).click();
//        Thread.sleep(5 * 1000);
//
//        //向下滑动并加上所有页脚
//        driver.swipe(439, 200, 439, 659, 200);
//        driver.findElementById(pageName + ":id/footer2").sendKeys("页脚2");
//        driver.findElementById(pageName + ":id/add_footer").click();
//        driver.findElementById(pageName + ":id/footer3").sendKeys("页脚3");
//        driver.findElementById(pageName + ":id/add_footer").click();
//        driver.findElementById(pageName + ":id/footer4").sendKeys("页脚4");
//        driver.swipe(439, 659, 439, 200, 200);
//        driver.findElementById(pageName + ":id/add_footer").click();
//        driver.findElementById(pageName + ":id/footer5").sendKeys("页脚5");
//        driver.findElementById(pageName + ":id/add_footer").click();
//        driver.findElementById(pageName + ":id/footer6").sendKeys("页脚6");
//        driver.findElementById(pageName + ":id/add_footer").click();
//        driver.findElementById(pageName + ":id/footer7").sendKeys("页脚7");
//        driver.findElementById(pageName + ":id/add_footer").click();
//        driver.findElementById(pageName + ":id/footer8").sendKeys("页脚8");
//
//        //向上滑动并点击打印测试
//        driver.swipe(439, 659, 439, 200, 200);
//        driver.findElementByAndroidUIAutomator(loc_text1).click();
//        Thread.sleep(5 * 1000);
//
//        //向下滑动，点击支付方式文字点击点击向右移动
//        driver.swipe(439, 200, 439, 659, 200);
//        driver.findElementById(pageName + ":id/smallsheet_create").click();
//        driver.findElementById(pageName + ":id/header").sendKeys("小票格式格式1页眉");
//        driver.findElementById(pageName + ":id/create_confirm").click();
//        Thread.sleep(5 * 1000);
//
//        driver.findElementById(pageName + ":id/smallsheet_create").click();
//        driver.findElementById(pageName + ":id/header").sendKeys("小票格式格式2页眉");
//        driver.findElementById(pageName + ":id/create_confirm").click();
//        Thread.sleep(5 * 1000);
//
//        //让合计方式和下面的所有文字的所有文字都向左移，然后往右移，然后再居中
//        //合计方式的移动
//        driver.findElementById(pageName + ":id/total_money").click();
//        driver.findElementById(pageName + ":id/keep_left").click();
//        driver.findElementById(pageName + ":id/keep_right").click();
//        driver.findElementById(pageName + ":id/keep_center").click();
//
//        //支付方式的移动
//        driver.findElementById(pageName + ":id/payment_method").click();
//        driver.findElementById(pageName + ":id/keep_left").click();
//        driver.findElementById(pageName + ":id/keep_right").click();
//        driver.findElementById(pageName + ":id/keep_center").click();
//
//        //优惠和应付的移动暂时无法使用
//
//        //微信支付的移动
//        driver.findElementById(pageName + ":id/payment_method1").click();
//        driver.findElementById(pageName + ":id/keep_left").click();
//        driver.findElementById(pageName + ":id/keep_right").click();
//        driver.findElementById(pageName + ":id/keep_center").click();
//
//        driver.swipe(439, 659, 439, 200, 200);
//        //向上滑动点击打印与测试
//        driver.findElementByAndroidUIAutomator(loc_text1).click();
//        Thread.sleep(5 * 1000);
//    }
//
//    //退出操作
//    @Test
//    public void logoutTest() throws InterruptedException, MalformedURLException {
//        //等待pos登录
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        //点击退出按钮，弹出是否退出并交班
//        driver.findElementById(pageName + ":id/logout").click();
//        //点击确定
//        Thread.sleep(2 * 1000);
//        driver.findElementById("android:id/button1").click();
//        //点击交班
//        Thread.sleep(2 * 1000);
//        driver.findElementById(pageName + ":id/change_shifts").click();
//        //等待返回登录界面
//        Thread.sleep(10 * 1000);
//    }
//
//    //支付 收银汇总操作
//    @Test
//    public void TestRetailTradeAggregationActivity() throws InterruptedException, MalformedURLException { //这个是我写的  你要连真机跑测试  然后他会自己在机器上模拟人去输入
//        //等待pos登录
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895"); //打开安装文档
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");    //基本上都是选中  点击这些操作  按照你的逻辑想顺序 就这些  你把我打的字删掉吧
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        /**
//         * 1.点击左下方输入条形码
//         * 2.将查询到的商品调价到代购列表
//         * 3.点击结算
//         * 4.结算完成之后，重复步骤1 2
//         * 5.选中一个商品，进行移除商品操作
//         * 6.点击查单，进行零售单的查询
//         * 7.进行退货操作
//         * 8.点击退出，跳转到收银汇总的Activity
//         */
//
//        //1.点击左下方输入条形码
//        driver.findElementById(pageName + ":id/bar_code").click();
//        //在弹出层输入条形码
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("3548293894545");
//        //2.将查询到的商品调价到代购列表
//        driver.findElementById(pageName + ":id/add_tv").click();
//        //选中一个商品，进行移除商品操作 因为没有唯一标示 所以根据商品序号定位
//        driver.findElementById(pageName + ":id/commodity_id").click();
//        //点击移除商品
//        driver.findElementById(pageName + ":id/remove_commodity").click();
//        //添加商品进行结算
//        driver.findElementById(pageName + ":id/bar_code").click();
//        driver.findElementById(pageName + ":id/search_commodity_et").sendKeys("3548293894545");
//        driver.findElementById(pageName + ":id/add_tv").click();
//
//        //3.点击结算
//        driver.findElementById(pageName + ":id/balance_tv").click();
//        //点击支付
//        driver.findElementById(pageName + ":id/pay").click();
//
//        //8.点击退出，跳转到收银汇总的Activity
//        driver.findElementById(pageName + ":id/logout").click();
//        //点击确定
//        driver.findElementById(pageName + ":id/button1").click();
//        //点击交班
//        driver.findElementById(pageName + ":id/change_shifts").click();
//
//    }
//
//    //查询零售单，退货操作
//    @Test
//    public void TestSalesReturn() throws InterruptedException, MalformedURLException {
//        //等待pos登录
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        /**
//         * 1.点击左下方输入条形码
//         * 2.将查询到的商品调价到代购列表
//         * 3.点击结算
//         * 4.结算完成之后，重复步骤1 2
//         * 5.选中一个商品，进行移除商品操作
//         * 6.点击查单，进行零售单的查询
//         * 7.进行退货操作
//         * 8.点击退出，跳转到收银汇总的Activity
//         */
//
//        //6.1.点击查单，进行零售单的查询
//        driver.findElementById(pageName + ":id/check_list").click();
//        //什么都不输入 点击搜索所有零售单
//        driver.findElementById(pageName + ":id/check_list_search").click();
//
//        //7.进行退货操作
//        //先选中需要退货的退货单
//        String serial_text = "new UiSelector().text(\"4\")";
//        driver.findElementByAndroidUIAutomator(serial_text).click();
//
//        //点击退货
//        driver.findElementById(pageName + ":id/return_goods").click();
//        //选中商品之后需要进行数量更改才能进行退货操作
//        String commodidyname_text = "new UiSelector().text(\"小白兔牛轧糖\")";
//        driver.findElementByAndroidUIAutomator(commodidyname_text).click();
//        //数量更改
//        driver.findElementById(pageName + ":id/reduce_number").click();
//        //点击确认退货
//        driver.findElementById(pageName + ":id/confirm_return_goods").click();
//
//        //8.点击退出，跳转到收银汇总的Activity
//        driver.findElementById(pageName + ":id/logout").click();
//        //点击确定
//        driver.findElementById(pageName + ":id/button1").click();
//        //点击交班
//        driver.findElementById(pageName + ":id/change_shifts").click();
//
//    }
//
//    @Test
//    public void TestSalesReturn2() throws InterruptedException, MalformedURLException {
//        //等待pos登录
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        /**
//         * 1.点击左下方输入条形码
//         * 2.将查询到的商品调价到代购列表
//         * 3.点击结算
//         * 4.结算完成之后，重复步骤1 2
//         * 5.选中一个商品，进行移除商品操作
//         * 6.点击查单，进行零售单的查询
//         * 7.进行退货操作
//         * 8.点击退出，跳转到收银汇总的Activity
//         */
//
//        //6.2.点击查单，进行零售单的查询
//        driver.findElementById(pageName + ":id/check_list").click();
//        //只输入单号，不输入时间
//        driver.findElementById(pageName + ":id/check_list_sn").sendKeys("SN123483218"); //sendKeys 控件设置
//        //点击搜索
//        driver.findElementById(pageName + ":id/check_list_search").click();
//
//        //7.进行退货操作
//        //先选中需要退货的退货单
//        String serial_text = "new UiSelector().text(\"4\")";
//        driver.findElementByAndroidUIAutomator(serial_text).click();
//
//        //点击退货
//        driver.findElementById(pageName + ":id/return_goods").click();//click 控件点击  千万记得找对id 或者name属性
//        //选中商品之后需要进行数量更改才能进行退货操作
//        String commodidyname_text = "new UiSelector().text(\"小白兔牛轧糖\")";
//        driver.findElementByAndroidUIAutomator(commodidyname_text).click();
//        //数量更改
//        driver.findElementById(pageName + ":id/reduce_number").click();
//        //点击确认退货
//        driver.findElementById(pageName + ":id/confirm_return_goods").click();
//
//        //8.点击退出，跳转到收银汇总的Activity
//        driver.findElementById(pageName + ":id/logout").click();
//        //点击确定
//        driver.findElementById(pageName + ":id/button1").click();
//        //点击交班
//        driver.findElementById(pageName + ":id/change_shifts").click();
//
//    }
//
//    @Test
//    public void TestSalesReturn3() throws InterruptedException, MalformedURLException {
//        //等待pos登录
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        /**
//         * 1.点击左下方输入条形码
//         * 2.将查询到的商品调价到代购列表
//         * 3.点击结算
//         * 4.结算完成之后，重复步骤1 2
//         * 5.选中一个商品，进行移除商品操作
//         * 6.点击查单，进行零售单的查询
//         * 7.进行退货操作
//         * 8.点击退出，跳转到收银汇总的Activity
//         */
//
//        //6.3.点击查单，进行零售单的查询
//        driver.findElementById(pageName + ":id/check_list").click();
//        //只输入开始时间，不输入结束时间和单号
//        driver.findElementById(pageName + ":id/check_list_startDate").click();
//        //点击选择时间 13号
//        String startdate_text = "new UiSelector().text(\"13\")";
//        driver.findElementByAndroidUIAutomator(startdate_text).click();
//        driver.findElementById("android:id/button1").click();
//
//        driver.findElementById(pageName + ":id/check_list_search").click();
//
//        //7.进行退货操作
//        //先选中需要退货的退货单
//        String serial_text = "new UiSelector().text(\"4\")";
//        driver.findElementByAndroidUIAutomator(serial_text).click();
//
//        //点击退货
//        driver.findElementById(pageName + ":id/return_goods").click();
//        //选中商品之后需要进行数量更改才能进行退货操作
//        String commodidyname_text = "new UiSelector().text(\"小白兔牛轧糖\")";
//        driver.findElementByAndroidUIAutomator(commodidyname_text).click();
//        //数量更改
//        driver.findElementById(pageName + ":id/reduce_number").click();
//        //点击确认退货
//        driver.findElementById(pageName + ":id/confirm_return_goods").click();
//
//        //8.点击退出，跳转到收银汇总的Activity
//        driver.findElementById(pageName + ":id/logout").click();
//        //点击确定
//        driver.findElementById(pageName + ":id/button1").click();
//        //点击交班
//        driver.findElementById(pageName + ":id/change_shifts").click();
//
//    }
//
//    @Test
//    public void TestSalesReturn4() throws InterruptedException, MalformedURLException {
//        //等待pos登录
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        /**
//         * 1.点击左下方输入条形码
//         * 2.将查询到的商品调价到代购列表
//         * 3.点击结算
//         * 4.结算完成之后，重复步骤1 2
//         * 5.选中一个商品，进行移除商品操作
//         * 6.点击查单，进行零售单的查询
//         * 7.进行退货操作
//         * 8.点击退出，跳转到收银汇总的Activity
//         */
//
//        //点击查单，进行零售单的查询
//        driver.findElementById(pageName + ":id/check_list").click();
//        //6.4.只输入结束时间，不输入开始时间和单号
//        driver.findElementById(pageName + ":id/check_list_endDate").click();
//        String enddate_text = "new UiSelector().text(\"13\")";
//        driver.findElementByAndroidUIAutomator(enddate_text).click();
//        driver.findElementById("android:id/button1").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//
//        //7.进行退货操作
//        //先选中需要退货的退货单
//        String serial_text = "new UiSelector().text(\"4\")";
//        driver.findElementByAndroidUIAutomator(serial_text).click();
//
//        //点击退货
//        driver.findElementById(pageName + ":id/return_goods").click();
//        //选中商品之后需要进行数量更改才能进行退货操作
//        String commodidyname_text = "new UiSelector().text(\"小白兔牛轧糖\")";
//        driver.findElementByAndroidUIAutomator(commodidyname_text).click();
//        //数量更改
//        driver.findElementById(pageName + ":id/reduce_number").click();
//        //点击确认退货
//        driver.findElementById(pageName + ":id/confirm_return_goods").click();
//
//        //8.点击退出，跳转到收银汇总的Activity
//        driver.findElementById(pageName + ":id/logout").click();
//        //点击确定
//        driver.findElementById(pageName + ":id/button1").click();
//        //点击交班
//        driver.findElementById(pageName + ":id/change_shifts").click();
//
//    }
//
//    //查询零售单，退货操作
//    @Test
//    public void TestSalesReturn5() throws InterruptedException, MalformedURLException {
//        //等待pos登录
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        /**
//         * 1.点击左下方输入条形码
//         * 2.将查询到的商品调价到代购列表
//         * 3.点击结算
//         * 4.结算完成之后，重复步骤1 2
//         * 5.选中一个商品，进行移除商品操作
//         * 6.点击查单，进行零售单的查询
//         * 7.进行退货操作
//         * 8.点击退出，跳转到收银汇总的Activity
//         */
//
//        //点击查单，进行零售单的查询
//        driver.findElementById(pageName + ":id/check_list").click();
//        //6.5只输入开始时间和结束时间，不输入单号
//        driver.findElementById(pageName + ":id/check_list_startDate").click();
//        String startdate_text = "new UiSelector().text(\"13\")";
//        driver.findElementByAndroidUIAutomator(startdate_text).click();
//        driver.findElementById("android:id/button1").click();
//        driver.findElementById(pageName + ":id/check_list_endDate").click();
//        String enddate_text = "new UiSelector().text(\"25\")";
//        driver.findElementByAndroidUIAutomator(enddate_text).click();
//        driver.findElementById("android:id/button1").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//
//        //7.进行退货操作
//        //先选中需要退货的退货单
//        String serial_text = "new UiSelector().text(\"4\")";
//        driver.findElementByAndroidUIAutomator(serial_text).click();
//
//        //点击退货
//        driver.findElementById(pageName + ":id/return_goods").click();
//        //选中商品之后需要进行数量更改才能进行退货操作
//        String commodidyname_text = "new UiSelector().text(\"小白兔牛轧糖\")";
//        driver.findElementByAndroidUIAutomator(commodidyname_text).click();
//        //数量更改
//        driver.findElementById(pageName + ":id/reduce_number").click();
//        //点击确认退货
//        driver.findElementById(pageName + ":id/confirm_return_goods").click();
//
//        //8.点击退出，跳转到收银汇总的Activity
//        driver.findElementById(pageName + ":id/logout").click();
//        //点击确定
//        driver.findElementById(pageName + ":id/button1").click();
//        //点击交班
//        driver.findElementById(pageName + ":id/change_shifts").click();
//
//    }
//
//    @Test
//    public void TestSalesReturn6() throws InterruptedException, MalformedURLException {
//        //等待pos登录
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        /**
//         * 1.点击左下方输入条形码
//         * 2.将查询到的商品调价到代购列表
//         * 3.点击结算
//         * 4.结算完成之后，重复步骤1 2
//         * 5.选中一个商品，进行移除商品操作
//         * 6.点击查单，进行零售单的查询
//         * 7.进行退货操作
//         * 8.点击退出，跳转到收银汇总的Activity
//         */
//
//        //点击查单，进行零售单的查询
//        driver.findElementById(pageName + ":id/check_list").click();
//        //6.6输入单号 开始时间和结束时间
//        driver.findElementById(pageName + ":id/check_list_sn").sendKeys("120190125935721");
//        driver.findElementById(pageName + ":id/check_list_startDate").click();
//        String startdate_text = "new UiSelector().text(\"13\")";
//        driver.findElementByAndroidUIAutomator(startdate_text).click();
//        driver.findElementById("android:id/button1").click();
//        driver.findElementById(pageName + ":id/check_list_endDate").click();
//        String enddate_text = "new UiSelector().text(\"25\")";
//        driver.findElementByAndroidUIAutomator(enddate_text).click();
//        driver.findElementById("android:id/button1").click();
//        driver.findElementById(pageName + ":id/check_list_search").click();
//
//        //7.进行退货操作
//        //先选中需要退货的退货单
//        String serial_text = "new UiSelector().text(\"4\")";
//        driver.findElementByAndroidUIAutomator(serial_text).click();
//
//        //点击退货
//        driver.findElementById(pageName + ":id/return_goods").click();
//        //选中商品之后需要进行数量更改才能进行退货操作
//        String commodidyname_text = "new UiSelector().text(\"小白兔牛轧糖\")";
//        driver.findElementByAndroidUIAutomator(commodidyname_text).click();
//        //数量更改
//        driver.findElementById(pageName + ":id/reduce_number").click();
//        //点击确认退货
//        driver.findElementById(pageName + ":id/confirm_return_goods").click();
//
//        //8.点击退出，跳转到收银汇总的Activity
//        driver.findElementById(pageName + ":id/logout").click();
//        //点击确定
//        driver.findElementById(pageName + ":id/button1").click();
//        //点击交班
//        driver.findElementById(pageName + ":id/change_shifts").click();
//
//    }
//
//    //库存查询
//    @Test
//    public void TestRetrieveCommodityInventoryActivity() throws InterruptedException, MalformedURLException {
//        //等待pos登录
//        Thread.sleep(5000);
//        driver.findElementById(pageName + ":id/username").sendKeys("15854320895");
//        driver.findElementById(pageName + ":id/password").sendKeys("000000");
//        System.out.println("currentActivity:" + driver.currentActivity());
//        driver.findElementById(pageName + ":id/login").click();
//        while (!"com.bx.erp.view.activity.MainActivity".equals(driver.currentActivity())) {
//            System.out.println("currentActivity:" + driver.currentActivity());
//            Thread.sleep(1000);
//        }
//        System.out.println("currentActivity:" + driver.currentActivity());
//        System.out.println("Staff Login Success");
//
//        //进入到MainActivity：设置准备金
//        driver.findElementById(pageName + ":id/reserve_et").sendKeys("200");
//        String reverseNumber = driver.findElementById(pageName + ":id/reserve_et").getText();
//        System.out.println("Input reverse is " + reverseNumber);
//        driver.findElementById(pageName + ":id/confirm_reserve").click();
//
//        /**
//         * 1.点击主页库存
//         * 2.在弹出层点击库存查询
//         * 3.输入商品条形码进行库存查询
//         * 4.返回主页
//         */
//        //1.点击主页库存
//        driver.findElementById(pageName + ":id/inventory").click();
//        //2.在弹出层点击库存查询
//        driver.findElementById(pageName + ":id/retrieve_inventory").click();
//        //先进行同步操作
//        driver.findElementById(pageName + ":id/sync_button").click();
//        Thread.sleep(25 * 1000);  //... 需要进行判断优化
//        //3.输入商品条形码点击搜索按钮进行库存查询
//        driver.findElementById(pageName + ":id/condition_input").sendKeys("3548293894545");
//        driver.findElementById(pageName + ":id/search").click(); //... 这里有错误  需要进行修改
//        //4.返回主页
//        driver.findElementById(pageName + ":id/retrieve_commodity_inventory_back").click();
//        //5.点击退出，跳转到收银汇总的Activity
//        driver.findElementById(pageName + ":id/logout").click();
//        //点击确定
//        driver.findElementById(pageName + ":id/button1").click();
//        //点击交班
//        driver.findElementById(pageName + ":id/change_shifts").click();
//    }
//}
