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
 * Created by WPNA on 2020/4/7.
 */

public class Main1Activity_U_AppiumTest extends BaseAppiumTest {

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
     * case 2-3:已经无效；
     * case 1 在case 4 中，与case 4 一起测试，节约时间
     */

    @Test
    public void MainActivity04() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case 1:点击移除商品后，商品移除。首页显示的商品数量相应减少;case4:移除待购列表中的商品");
        System.out.println("需查看是否都移除成功");
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
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
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
        Thread.sleep(3000);


        //移除商品
        new TouchAction(driver).longPress(driver.findElementsById(pageName + ":id/commodity_id").get(0)).waitAction(1000).perform();//长按
        driver.findElementById(pageName + ":id/remove_commodity").click();
        Thread.sleep(3000);
        System.out.println(driver.findElementById(pageName + ":id/commodity_quantity").getText());
        Assert.assertTrue("商品数量没有减少！", driver.findElementById(pageName + ":id/commodity_quantity").getText().equals("商品数量：1 件"));
        new TouchAction(driver).longPress(driver.findElementsById(pageName + ":id/commodity_id").get(0)).waitAction(1000).perform();//长按
        driver.findElementById(pageName + ":id/remove_commodity").click();
        Thread.sleep(3000);
        System.out.println(driver.findElementById(pageName + ":id/commodity_quantity").getText());
        Assert.assertTrue("商品数量没有减少！", driver.findElementById(pageName + ":id/commodity_quantity").getText().equals("商品数量：0 件"));
    }


    /**
     * case 5-6：UI界面已经更改，已不需要该用例；
     */

    @Test
    public void MainActivity07() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:将商品数量修改为0，提示商品数量必须大于0");
        System.out.println("需查看是否提示商品数量必须大于0");
        posLogin();
        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
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
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("123456");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
        driver.findElementsById(pageName + ":id/commodity_id").get(0).click();
        driver.findElementById(pageName + ":id/t0").click();//点击数字0
        driver.findElementById(pageName + ":id/confirm_reserve").click();//点击确定
        Thread.sleep(3 * 1000);
    }

    /**
     * case 8：已经无法输入无效的字符，只能输入数字
     * case 9：与Main_C_appium case 15 相同;
     * case 10：UI界面更改，已经无法完成；
     * case 11-13：无法测试使用键盘的测试用例
     */

    @Test
    public void MainActivity14() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:选中待购商品后添加待购商品（两商品不同）");
        System.out.println("需查看是否第一个为选择状态");
        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
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
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567801");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
        driver.findElementsById(pageName + ":id/commodity_id").get(0).click();
        driver.findElementById(pageName + ":id/cancel").click();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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

        System.out.println("需查看是否第一个为选择状态");
        Thread.sleep(6 * 1000);
    }

    @Test
    public void MainActivity15() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:选中待购商品后添加待购商品（两商品不同）");
        System.out.println("需查看是否第一个为选择状态");
        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
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
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567801");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
        driver.findElementsById(pageName + ":id/commodity_id").get(0).click();//点击商品
        driver.findElementById(pageName + ":id/cancel").click();//对弹出的数量修改框点击取消

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
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
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567801");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();

        System.out.println("需查看是否第一个为选择状态");
        Thread.sleep(6 * 1000);
    }

    @Test
    public void MainActivity16() throws InterruptedException, MalformedURLException {
        System.out.println("----------case:选中待购商品后在交班页面返回");

        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
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
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567801");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
        driver.findElementsById(pageName + ":id/commodity_id").get(0).click();//点击商品
        driver.findElementById(pageName + ":id/cancel").click();//对弹出的数量修改框点击取消

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/back").click();//点击返回

        System.out.println("需查看是否第一个为选择状态");
        Thread.sleep(3 * 1000);
    }

    @Test
    public void MainActivity17() throws InterruptedException, MalformedURLException {
        System.out.println("-----------case:点击想要修改数量的商品,修改商品数量为0.");
        System.out.println("需观察是否弹出请输入正整数");
        posLogin();

        //点击打开“按条形码查询”,输入条形码,将商品添加到代购列表
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
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
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("1234567801");
        driver.findElementById(pageName + ":id/bar_code").click();
        driver.findElementById(pageName + ":id/add_tv").click();
        Thread.sleep(3000);
        driver.findElementsById(pageName + ":id/commodity_id").get(0).click();//点击商品
        driver.findElementById(pageName + ":id/t0").click();
        driver.findElementById(pageName + ":id/t_point").click();
        driver.findElementById(pageName + ":id/confirm_reserve").click();//点击确定
        Thread.sleep(3 * 1000);
        Assert.assertTrue("商品数量不正常！", driver.findElementById(pageName + ":id/number").getText().equals("1"));
    }


}
