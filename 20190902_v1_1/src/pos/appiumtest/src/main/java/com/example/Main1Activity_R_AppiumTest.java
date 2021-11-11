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
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import static io.appium.java_client.android.AndroidKeyCode.BACKSPACE;
import static io.appium.java_client.android.AndroidKeyCode.KEYCODE_A;
import static io.appium.java_client.android.AndroidKeyCode.META_CTRL_MASK;

/**
 * Created by WPNA on 2020/4/7.
 */

public class Main1Activity_R_AppiumTest extends BaseAppiumTest {

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

    /**
     * case 1-4：暂无法完成，需要扫码枪
     */
    @Test
    public void MainActivity005() throws InterruptedException, MalformedURLException {
        System.out.println("---------case:输入完整的正确条形码,点击“加入”,首页中显示正确的商品数量。总计金额也是相应改变");
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567801");
        driver.findElementById(pageName + ":id/bar_code").click();
        String retail_price = driver.findElementById(pageName + ":id/retail_price").getText();//获取商品列表中的待购商品的价格
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        Assert.assertTrue("添加商品后，销售页面数据不一致", driver.findElementById(pageName + ":id/number").getText().equals("1")
                && driver.findElementById(pageName + ":id/total_money").getText().equals(retail_price));//与总计金额对比

    }

    @Test
    public void MainActivity006() throws InterruptedException, MalformedURLException {
        System.out.println("---------case:输入不完整的小于7位的条形码,点击“加入”,商品列表中没有出现任何商品");
        System.out.println("需查看是否商品列表是否显示为空，是的话则通过");
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
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("123456");
        driver.findElementById(pageName + ":id/add_tv").click();

        Thread.sleep(3 * 1000);
    }

    @Test
    public void MainActivity007() throws InterruptedException, MalformedURLException {
        System.out.println("-------------case:输入不完整的大于等于7位的条形码");
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
        Thread.sleep(3 * 1000);
        String after_discount = driver.findElementById(pageName + ":id/after_discount").getText();//获取商品列表中的待购商品的折后单价
//        System.out.println("number"+driver.findElementById(pageName+":id/number").getText()+"价格"+driver.findElementById(pageName+":id/total_money").getText());
        Assert.assertTrue("添加商品后，销售页面数据不一致", driver.findElementById(pageName + ":id/number").getText().equals("1")
                && driver.findElementById(pageName + ":id/total_money").getText().equals(after_discount));//与总计金额对比
    }

    /**
     * case 8-9 :需要用到扫码枪，暂无法实现；
     */

    @Test
    public void MainActivity010() throws InterruptedException, MalformedURLException {
        System.out.println("-------------case:输入错误的条形码，商品列表中没有出现任何商品");
        System.out.println("需要观察是否显示的列表为空，是则测试通过");
        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        AndroidElement elementById = driver.findElementById(pageName + ":id/t0");
        for (int i = 0; i < 11; i++) {
            elementById.click();
        }
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("00000000000");
        // driver.findElementById(pageName+":id/bar_code").click();
        Thread.sleep(3 * 1000);
    }

    @Test
    public void MainActivity011() throws InterruptedException, MalformedURLException {
        System.out.println("--------------case:选择已经存在商品列表中的商品.再次添加，商品列表中已经存在，在原来的数量上加1");
        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567802");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        //再次添加相同商品
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567802");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        String after_discount = String.valueOf(Double.valueOf(driver.findElementById(pageName + ":id/after_discount").getText()) * 2);//获取商品列表中的待购商品的折后单价*2
        Assert.assertTrue("添加商品后，销售页面数据不一致", driver.findElementById(pageName + ":id/number").getText().equals("2")
                && driver.findElementById(pageName + ":id/total_money").getText().equals(after_discount));//与总计金额对比
    }

    /**
     * case 12-13:需要扫码枪，暂无法完成
     */

    // TODO: 2020/4/8 case 14-18
    @Test
    public void MainActivity141() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:POS端还未查找会员，点击展示优惠券的控件。显示无可用优惠券");
        posLogin();
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567802");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("居然搜索到了优惠券！", driver.findElementsById(pageName + ":id/textview").get(0).getText().equals("无可用的优惠券"));

    }

    @Test
    public void MainActivity142() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:输入有效的会员卡号查找会员，且此会员有可使用的优惠券，点击展示优惠券的控件，能显示可用的优惠券");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("6688661234567891");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567802");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("优惠券不对！", driver.findElementsById(pageName + ":id/textview").get(0).getText().equals("有可用的优惠券"));
    }

    @Test
    public void MainActivity143() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:输入不存在的会员卡卡号查找会员，点击展示优惠券的控件。显示无可用优惠券");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("6688661234567892");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567802");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("居然搜索到了优惠券！", driver.findElementsById(pageName + ":id/textview").get(0).getText().equals("无可用的优惠券"));
    }

    @Test
    public void MainActivity144() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:POS端输入不存在的手机号码查找会员，点击展示优惠券的控件，显示无可用优惠券");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13250551510");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567802");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("居然搜索到了优惠券！", driver.findElementsById(pageName + ":id/textview").get(0).getText().equals("无可用的优惠券"));
    }

    @Test
    public void MainActivity145() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:结算前选择一张可用优惠券，支付金额为使用优惠券后的金额");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567802");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        String wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();

        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(1).click();//点击优惠券
        Assert.assertFalse("优惠券使用无效！", driver.findElementById(pageName + ":id/wechat_paying_amount").getText().equals(wechat_pay_amount));
    }

    /**
     * case 146:暂无法实现，在进行结算时，优惠券的结束时间已到；
     *
     * @throws InterruptedException
     * @throws MalformedURLException
     */

    @Test
    public void MainActivity147() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:POS端查找有效会员，会员有一张优惠券 ，但是并没有达到优惠券的起用金额，无法显示该优惠券");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567801");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("优惠券使用无效！", driver.findElementsById(pageName + ":id/textview").get(1).getText().equals("已起用的优惠券"));
        Thread.sleep(2 * 1000);
    }

    @Test
    public void MainActivity148() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:POS端，添加商品，查找有效会员，并且选择了一张可用优惠券，此时去掉部分商品，再次选择这张优惠券后发现优惠券不在优惠券列表中。");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567801");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("优惠券不存在", driver.findElementsById(pageName + ":id/textview").get(1).getText().equals("九折优惠券"));
        driver.findElementsById(pageName + ":id/textview").get(0).click();
        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        driver.findElementById("android:id/button1").click();//确定取消
        Thread.sleep(5 * 1000);

        //移除商品
        new TouchAction(driver).longPress(driver.findElementsById(pageName + ":id/commodity_id").get(0)).waitAction(1000).perform();//长按
        driver.findElementById(pageName + ":id/remove_commodity").click();
        Thread.sleep(5 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("优惠券不应该存在", driver.findElementsById(pageName + ":id/textview").get(1).getText().equals("已起用的优惠券"));
        Thread.sleep(1000);
    }

    /**
     * case 149:暂没有会员账号可以完成此测试
     */

    @Test
    public void MainActivity150() throws InterruptedException, MalformedURLException {
        System.out.println("---------case:优惠的金额的变化始终都是先算促销，后算优惠券");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        //单价为168，折后单价为138；
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(1).click();//9折优惠券

        //使用完9折优惠券后，价格应为138*0.9 = 133.20
        String wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("优惠金额不对！", wechat_pay_amount.equals("133.20"));
    }

    /**
     * case 151：暂时没有促销后金额不满足优惠券的起用条件的优惠券
     */

    // TODO: 2020/4/8 case:152 不理解
    @Test
    public void MainActivity153() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:添加商品，查找有效会员，顾客不使用优惠券。结算金额为原价");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        String wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("金额应为原价！", wechat_pay_amount.equals("148.00"));
    }

    @Test
    public void MainActivity154() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:添加商品，进行结算后，查找有效会员。并选择/不选择 优惠券进行支付（金额为选择/不选择优惠券后的优惠金额）");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        String wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("金额应为原价！", wechat_pay_amount.equals("148.00"));

        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(1).click();//9折优惠券
        wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("金额应为打折后的价格！", wechat_pay_amount.equals("133.20"));
    }

    /**
     *  case 155-156：暂无法完成，需要临时在小程序领取优惠券
     *  case 157：暂无法完成，需要老板新增优惠券
     *  case 158：暂无法完成，需要临时在小程序领取优惠券
     */

    // TODO: 2020/4/8 case 159:暂时没有会员拥有需要固定使用范围的优惠券

    /**
     * case 160: 在此优惠券的结束时间的前5s进行使用,暂无法完成
     */

    @Test
    public void MainActivity161() throws InterruptedException, MalformedURLException {
        System.out.println("---------case:添加商品，查找有效会员，并选择一张券后，再次选择一张券。之前选择的券就会消失。无法叠加");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(1).click();//9折优惠券
        String wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("金额应为打折后的价格！", wechat_pay_amount.equals("133.20"));

        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(2).click();//已启用的优惠券
        wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("金额应为打折后的价格！", wechat_pay_amount.equals("144.00"));
    }

    /**
     * case 162：暂无法完成 ：POS机登录，同步到俩种优惠券，查找有效会员，会员有三种优惠券。但是POS机只会显示同步到的俩种优惠券。会员的另一种优惠券不显示
     * case 163：需要老板新建一张优惠券，暂无法完成
     * case 164：此优惠券还有 >1m 左右就到了周一的限制使用时间。
     * case 165-167：今天周一。添加商品，查找会员。会员只有一张券，并且该券周一全天不可使用。显示会员优惠券列表中无数据，暂无法完成
     * case 168:与case 161相同；
     */

    @Test
    public void MainActivity169() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:POS端消费,商品参与了促销。选择一张优惠券A,结算金额为（原价-促销-优惠券A）X元。" +
                "再次选择另一张券B，结算金额为（原价-促销-优惠券B）xx元");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);

        driver.findElementsById(pageName + ":id/commodity_id").get(0).click();
        driver.findElementById(pageName + ":id/t5").click();//点击数字5
        driver.findElementById(pageName + ":id/confirm_reserve").click();//点击确定
        Thread.sleep(3 * 1000);
        /**
         * 单价为168，促销后单价为108.00；
         * 总金额为540.00
         */

        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(1).click();//9折优惠券
        String wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("金额应为打折后的价格！", wechat_pay_amount.equals("486.00"));

        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(2).click();//已启用的优惠券
        wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("金额应为打折后的价格！", wechat_pay_amount.equals("536.00"));
    }

    @Test
    public void MainActivity170() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:使用会员A结算完成后。下一张完成的零售单的消费对象不是会员A");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(2).click();//使用“已启用的优惠券”
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();
        Thread.sleep(5 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("可能保留了上一个会员的信息！", driver.findElementsById(pageName + ":id/textview").get(0).getText().equals("无可用的优惠券"));
    }

    @Test
    public void MainActivity171() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:使用会员A，并选择一张优惠券后，查找会员B。优惠券列表中的优惠券是属于会员B的。");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("该会员居然没有了优惠券！", driver.findElementsById(pageName + ":id/textview").get(0).getText().equals("有可用的优惠券"));
        driver.findElementsById(pageName + ":id/textview").get(0).click();
        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        driver.findElementById("android:id/button1").click();//确定取消
        Thread.sleep(5 * 1000);
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        AndroidElement elementById = driver.findElementById(pageName + ":id/num_delete");
        for (int i = 0; i < 11; i++) {
            elementById.click();//点击删除键
        }
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678114");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("该会员居然有了优惠券！", driver.findElementsById(pageName + ":id/textview").get(0).getText().equals("无可用的优惠券"));
    }

    /**
     * case 172:暂无法测试，无法得知优惠券是否被核销
     */

    // TODO: 2020/4/8 case 173:未找到满10-10的优惠券。暂无法测试
    @Test
    public void MainActivity174() throws InterruptedException, MalformedURLException {
        System.out.println("---------case:购买商品，选择了一张优惠券后，金额减少。此时又选择不使用优惠券。进行支付。金额为原价");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(1).click();//点击“起用的优惠券但是有”
        String wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        System.out.println("   " + wechat_pay_amount);
        Assert.assertTrue("优惠金额不对！", wechat_pay_amount.equals("133.20"));    //如果此处出错，可能是优惠券跟预想的不一样
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(0).click();//点击不使用优惠券
        wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("金额应为原价！", wechat_pay_amount.equals("148.00"));
    }

    /**
     * case 175：暂无法完成，无法切换app
     */

    @Test
    public void MainActivity176() throws InterruptedException, MalformedURLException {

    }

    @Test
    public void MainActivity177() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:1.查找会员，不选择优惠券\n" +
                "2、支付部分金额\n" +
                "3、点击选择优惠券,只有取消支付后才可以再选择优惠券");

        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮

        //选择的商品折后单价为134.40；
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/t5").click();//输入金额5
        driver.findElementById(pageName + ":id/pay").click();

        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(1).click();//点击9折的优惠券
        String cash_paying_amount = driver.findElementById(pageName + ":id/cash_paying_amount").getText();
        System.out.println(cash_paying_amount);
        Assert.assertTrue("居然支付部分金额后可以选择优惠券！", !cash_paying_amount.equals("133.20"));
    }

    @Test
    public void MainActivity178() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:待售列表添加商品，查找会员，选择一张优惠券,金额变成 原价-优惠券减免的金额。\n" +
                "此时往待售商品列表中添加商品，满足促销..重新选择优惠券，金额变成:促销后的价格-优惠券减免的金额");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(1).click();//点击9折优惠券
        String wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("优惠券使用失败了！计算价格错误", wechat_pay_amount.equals("133.20"));

        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        driver.findElementById("android:id/button1").click();//确定取消
        Thread.sleep(5 * 1000);
        driver.findElementsById(pageName + ":id/commodity_id").get(0).click();//点击待购商品
        driver.findElementById(pageName + ":id/t5").click();//点击数字
        driver.findElementById(pageName + ":id/confirm_reserve").click();//点击确定
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(1).click();//点击9折优惠券
        wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        Assert.assertTrue("优惠券使用失败了！计算价格错误", wechat_pay_amount.equals("486.00"));
    }

    @Test
    public void MainActivity179() throws InterruptedException, MalformedURLException {
        System.out.println("--------case:待售列表添加商品，进行支付。取消支付，查找会员。并选择一张优惠券进行支付");
        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        driver.findElementById("android:id/button1").click();//确定取消
        Thread.sleep(5 * 1000);
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        driver.findElementsById(pageName + ":id/textview").get(1).click();//点击9折优惠券
        String wechat_pay_amount = driver.findElementById(pageName + ":id/wechat_paying_amount").getText();
        System.out.println(wechat_pay_amount);
        Assert.assertTrue("优惠券使用失败了！计算价格错误", wechat_pay_amount.equals("133.20"));
    }

    @Test
    public void MainActivity180() throws InterruptedException, MalformedURLException {
        System.out.println("顾客购买商品A 5元，商品B 10元。查找会员，顾客有优惠券,此优惠券的作用范围为商品A。起用金额为10元.点击支付，在优惠券列表中无法查找到此优惠券。");
        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("3548294694545");//商品A（可口可乐）
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);

        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        List<AndroidElement> list = driver.findElementsById(pageName + ":id/textview");//获取所有的优惠券
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().equals("指定可口系列满减券")) {
                Assert.assertTrue("居然能查到启用金额为10元的券", false);
                break;
            }
        }
    }

    // TODO: 2020/4/9 case 181:暂无法完成，没有找到指定商品A满10做促销后金额为8元的促销

    @Test
    public void MainActivity182() throws InterruptedException, MalformedURLException {
        System.out.println("顾客购买商品A15元，商品B10元.查找会员，顾客有优惠券,此优惠券的作用范围为商品A。起用金额为10元.点击支付，在优惠券列表中能查找到此优惠券。");
        posLogin();
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("3548294694545");//商品A（可口可乐）
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);
        driver.findElementsById(pageName + ":id/commodity_id").get(0).click();//点击待购商品
        driver.findElementById(pageName + ":id/t5").click();//点击数字
        driver.findElementById(pageName + ":id/confirm_reserve").click();//点击确定
        Thread.sleep(3 * 1000);

        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);


        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        boolean istrue = false;
        List<AndroidElement> list = driver.findElementsById(pageName + ":id/textview");//获取所有的优惠券
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().equals("指定可口系列满减券")) {
                istrue = true;
                break;
            }
        }
        Assert.assertTrue("居然找不到指定范围的优惠券！", istrue);
    }

    /**
     * case 183：无法完成，需使用扫码机
     * case 184：无法完成，有一个优惠券还有N分钟后到结束时间了。关闭支付页面，N分钟后重新打开支付页面查看优惠券列表后该券不存在了
     * case 185：无法完成，该会员有张优惠券是当日12点后才能使用的。11:59 查找会员，
     * case 186：该会员有张优惠券是当日12点后才能使用的。11:59 查找会员，点击支付，查看优惠券列表。
     */

    @Test
    public void MainActivity187() throws InterruptedException, MalformedURLException {
        System.out.println("--------case:优惠券列表只会显示查找出的会员的");
        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);

        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        String text = driver.findElementsById(pageName + ":id/textview").get(1).getText();

        driver.findElementsById(pageName + ":id/textview").get(0).click();
        driver.findElementById(pageName + ":id/payment_close").click();//取消支付
        driver.findElementById("android:id/button1").click();//确定取消
        Thread.sleep(5 * 1000);

        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("优惠券列表可能相同！", driver.findElementsById(pageName + ":id/textview").get(1).getText().equals(text));
    }

    /**
     * case 188:与case 177相同
     * case 189：无法判断有网登录时是否上传了零售单
     */


    /**
     * case 191：暂无法完成，无法判断有网登录时是否上传了零售单
     * case 192-193：暂无法完成，无法得知优惠券是否被核销
     */

    @Test
    public void MainActivity194() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:查找会员，会员有一张正常的优惠券，但是已经过了结束时间。优惠券列表中无此优惠券");

        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);

        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678110");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        List<AndroidElement> list = driver.findElementsById(pageName + ":id/textview");
        for (AndroidElement element : list) {
            Assert.assertFalse("居然显示了已经结束的优惠券！", element.getText().equals("已经结束的优惠券"));
        }
    }

    @Test
    public void MainActivity195() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:添加商品，查找会员。该会员有一张优惠券,该优惠券的种类(Copon)状态为删除。" +
                "会员的这张优惠券在可用时间。优惠券列表中会显示这张优惠券");
        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);

        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678111");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("没有找到已删除但未过期的券", driver.findElementsById(pageName + ":id/textview").get(1).getText().equals("已删除的九折券"));
    }

    /**
     * case 196：暂无法完成，没有优惠券状态为删除且不在可用时间内
     */

    @Test
    public void MainActivity197() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:什么都不传，提示不能缺少手机号或者卡号进行查询");
        System.out.println("观察是否提示 不能缺少手机号或者卡号进行查询");
        posLogin();
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
    }

    @Test
    public void MainActivity198() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:传递存在的手机号码，查找此手机号码的会员");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678111");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
    }

    @Test
    public void MainActivity199() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:传递不存在的手机号码，查找数据为空");
        System.out.println("观察是否提示：查找不到相关会员");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13250551510");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
    }

    @Test
    public void MainActivity200() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:传递存在的会员卡号号码，查找此卡号号码的会员");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("6688661234567891");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
    }

    @Test
    public void MainActivity201() throws InterruptedException, MalformedURLException {
        System.out.println("---------case:传递不存在的会员卡卡号号码，查找数据为空");
        System.out.println("观察是否提示：查找不到相关会员");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("6688661234567777");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
    }

    /**
     * case 202-205:需要扫码，暂无法完成
     */

    @Test
    public void MainActivity206() throws InterruptedException, MalformedURLException {
        System.out.println("---------case:输入大于或小于11位的手机号查询。提示信息");
        System.out.println("观察是否提示：请输入正确的手机号或者会员卡号");
        posLogin();
        //输入小于11位的手机号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("135");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);

        //输入大于11位的手机号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("132505515101");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
    }

    @Test
    public void MainActivity207() throws InterruptedException, MalformedURLException {
        System.out.println("---------case:输入大于或小于16位的会员卡卡号查询。提示信息");
        System.out.println("观察是否提示：请输入正确的手机号或者会员卡号");
        posLogin();
        //输入小于16位的会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("668866456123456");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);

        //输入大于16位的会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("12345678912345678");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
    }

    @Test
    public void MainActivity208() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:输入非数字外的字符进行查询. 无法输入");
        posLogin();
        Thread.sleep(2 * 1000);
        //输入非数字外的字符进行查询
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        AndroidElement elementById = driver.findElementById(pageName + ":id/num_point");
        for (int i = 0; i < 11; i++) {      //现在自定义键盘就只有“.”可以点击了，其他已做了限制
            elementById.click();
        }
        driver.findElementById(pageName + ":id/num_sure").click();//确定

        Assert.assertTrue("居然可以输入字符！", driver.findElementById(pageName + ":id/show_client_phone").getText().equals(""));
        Thread.sleep(3 * 1000);
    }

    /**
     * case 209：暂无法实现复制粘贴
     * case 210：如果此测试不通过，则上面很多测试都过不了，所以此测试可以不用
     * case 211：暂无法实现，需要切换APP
     */

    @Test
    public void MainActivity212() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:查到会员后,进行交班。重新登录至主页面后，会员信息不存在");
        posLogin();

        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("13545678111");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);

        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//点击搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/en_change_num").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("12345678");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/spinner").click();//点击spinner
        Assert.assertTrue("居然还存在交班前输入的会员数据", !driver.findElementsById(pageName + ":id/textview").get(0).getText().equals("有可用的优惠券"));
    }

    @Test
    public void MainActivity213() throws InterruptedException, MalformedURLException {
        System.out.println("------------case:输入16位的会员卡卡号查询。但是前6位并不是公司编号,提示信息");
        System.out.println("观察是否提示：查找不到相关会员");
        posLogin();
        //输入会员卡号
        driver.findElementById(pageName + ":id/show_client_phone").click();

        //点击虚拟键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/show_client_phone").sendKeys("1234561234567890");
        driver.findElementById(pageName + ":id/search_vip").click();//点击搜索
        Thread.sleep(3 * 1000);
    }

}
