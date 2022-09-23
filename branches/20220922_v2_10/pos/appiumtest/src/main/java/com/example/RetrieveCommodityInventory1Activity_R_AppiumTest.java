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
 * Created by WPNA on 2020/3/25.
 * 参照的手动测试文档为： RetrieveCommodityInventoryActivity
 */

public class RetrieveCommodityInventory1Activity_R_AppiumTest extends BaseAppiumTest {

    int startX = 1000;//手指触碰的位置的x轴
    int stratY = 1500;
    int height = 50;
    int width = 0;

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
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/data_processing").click();//数据处理按钮
        driver.findElementById(pageName + ":id/sync_base_data").click();//点击进行重置基础资料
        Thread.sleep(80 * 1000);

        // 该测试开始前把不重新安装App设为false，在这里设置回来。
        capabilities.setCapability("noReset", true);
    }

    @Test
    public void RetrieveCommodityInventory1Activity01() throws InterruptedException {
        System.out.println("-----------case:同步服务器所有的单品,.服务器返回所有的单品，pos端只显示前15条，下拉继续显示15条。直至所有的都展现出来后无法下拉。");

        posLogin();

        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(10 * 1000);
        driver.findElementById(pageName + ":id/sync_text").click();//点击同步全部数据
        Thread.sleep(60 * 1000);

        //上滑,滑动到第一页的最后一条数据被展示，判断页面中第11条数据（即第一页的第15条数据）是否有值，有值则第一页加载了15条数据
        driver.swipe(1000, 800, 1000, 200, 200);
        Thread.sleep(1000);
        Assert.assertTrue("页面不足15条数据，或者搜索不到单号！", driver.findElementsById(pageName + ":id/commodity_barcode").get(11).getText() != null);
        for (int i = 0; i < 4; i++) {   //无法得知数据是否都展示出来了，只能多做几次滑动，意思一下
            driver.swipe(1000, 800, 1000, 200, 200);//上滑
            Thread.sleep(1000);
        }
    }

    @Test
    public void RetrieveCommodityInventory1Activity02() throws InterruptedException {
        System.out.println("-----------case:进行模糊搜索，输入条形码查到相应商品并查到库存");

        posLogin();
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
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
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("896534893");//输入条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(30 * 1000);
        AndroidElement element = driver.findElementsById(pageName + ":id/commodity_barcode").get(0);//获取第一个数据
        Assert.assertTrue("搜索不到单号！", element != null);
        Assert.assertTrue("搜索到的条形码与输入的条形码不符！", element.getText().indexOf("896534893") != -1);
    }

    /**
     * case 3:暂无法完成，需要后台加入新的商品数据
     */

    @Test
    public void RetrieveCommodityInventory1Activity04() throws InterruptedException {
        System.out.println("-----------case:输入条形码真确且完整的条形码查到相应商品并查到库存");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("69656362686461");//输入条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(30 * 1000);
        AndroidElement element = driver.findElementsById(pageName + ":id/commodity_barcode").get(0);//获取第一个数据
        Assert.assertTrue("搜索不到单号！", element != null);
        Assert.assertTrue("搜索到的条形码与输入的条形码不符！", element.getText().equals("69656362686461"));

        //--------------断网测试
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/delete_all").click();//点击删除框中的条形码
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("69656362686461");//输入条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/search").click();//再次点击搜索，第一次搜索会弹出网络错误提示
        Thread.sleep(30 * 1000);
        element = driver.findElementsById(pageName + ":id/commodity_barcode").get(0);//获取第一个数据
        Assert.assertTrue("搜索不到单号！", element != null);
        Assert.assertTrue("搜索到的条形码与输入的条形码不符！", element.getText().equals("69656362686461"));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
    }

    @Test
    public void RetrieveCommodityInventory1Activity05() throws InterruptedException {
        System.out.println("-----------case:输入不存在的条形码搜索商品信息,不展示列表。\n" +
                "提示：无法找到对应的条形码的商品库存");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        AndroidElement elementById = driver.findElementById(pageName + ":id/t0");
        for (int i = 0; i < 11; i++) {
            elementById.click();
        }
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("69600000000000");//输入错误条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(30 * 1000);

        Assert.assertTrue("居然可以搜索条形码！", driver.findElementById(pageName + ":id/query_fail_view") != null);

        //--------------断网测试
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/delete_all").click();//点击删除框中的条形码
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        AndroidElement elementById2 = driver.findElementById(pageName + ":id/t0");
        for (int i = 0; i < 11; i++) {
            elementById2.click();
        }
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("69600000000000");//输入错误条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/search").click();//再次点击搜索，第一次搜索会弹出网络错误提示
        Thread.sleep(30 * 1000);
        Assert.assertTrue("居然可以搜索条形码！", driver.findElementById(pageName + ":id/query_fail_view") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
    }

    @Test
    public void RetrieveCommodityInventory1Activity06() throws InterruptedException {
        System.out.println("-----------case:输入不完整的条形码搜索商品信息,不展示列表。\n" +
                "提示：由于输入的条形码不够完整，无法查询到库存信息");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();

        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

        //  driver.findElementById(pageName+":id/condition_input").sendKeys("696563");//输入错误条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(30 * 1000);
        Assert.assertTrue("居然可以搜索条形码！", driver.findElementById(pageName + ":id/query_fail_view") != null);

        //--------------断网测试
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/delete_all").click();//点击删除框中的条形码
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("696563");//输入错误条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/search").click();//再次点击搜索，第一次搜索会弹出网络错误提示
        Thread.sleep(30 * 1000);
        Assert.assertTrue("居然可以搜索条形码！", driver.findElementById(pageName + ":id/query_fail_view") != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
    }

    @Test
    public void RetrieveCommodityInventory1Activity07() throws InterruptedException {
        System.out.println("-----------case:输入不完整的条形码搜索商品,列表中展示所有条形码前七位与输入框中的条形码一样的商品");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("6965636");//输入条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(30 * 1000);
        AndroidElement element = driver.findElementsById(pageName + ":id/commodity_barcode").get(0);//获取第一个数据
        Assert.assertTrue("搜索不到单号！", element != null);
    }

    @Test
    public void RetrieveCommodityInventory1Activity08() throws InterruptedException {
        System.out.println("-----------case:进行商品销售，并查看是否库存减少，分为联网断网情况");
        /**
         * 1.进行销售一个商品
         * 2.进入商品库存界面，点击刷新
         * 3.在列表中找到该商品，查看商品库存是否同步
         */
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();

        //先查询要销售的商品的库存是多少
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("696563624985261");//输入条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(30 * 1000);

        AndroidElement element1 = driver.findElementsById(pageName + ":id/commodity_NO").get(0);//获取第一个数据
        String commodity_no1 = element1.getText();//记录商品库存
        System.out.println("库存为：" + commodity_no1);

        //加购商品并购买
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("696563624985261");//输入条形码
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();//支付
        Thread.sleep(3 * 1000);

        //再次查询库存，看是否有变化
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        AndroidElement elementById = driver.findElementById(pageName + ":id/num_delete");
        for (int i = 0; i < 16; i++) {
            elementById.click();//点击键盘删除键
        }
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("696563624985261");//输入条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(30 * 1000);
        AndroidElement element2 = driver.findElementsById(pageName + ":id/commodity_NO").get(0);//获取第一个数据
        String commodity_no2 = element2.getText();//记录商品库存
        System.out.println("库存为：" + commodity_no2 + "预计库存为：" + (Integer.parseInt(commodity_no1) - 1));
        Assert.assertTrue("商品库存居然没变化！", String.valueOf(Integer.parseInt(commodity_no1) - 1).equals(commodity_no2));
        //---------------------断网情况
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        //加购商品并购买
        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售
        driver.findElementById(pageName + ":id/scan_barcode_search").click();//搜索框
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/search_commodity_et").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/search_commodity_et").sendKeys("696563624985261");//输入条形码
        driver.findElementById(pageName + ":id/bar_code").click();//点击商品
        driver.findElementById(pageName + ":id/add_tv").click();//添加商品按钮
        driver.findElementById(pageName + ":id/balance_tv").click();//结账按钮
        driver.findElementById(pageName + ":id/cash_pay").click();//现金支付
        driver.findElementById(pageName + ":id/pay").click();//支付
        Thread.sleep(3 * 1000);

        //再次查询库存，看是否有变化
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        elementById = driver.findElementById(pageName + ":id/num_delete");
        for (int i = 0; i < 16; i++) {
            elementById.click();//点击键盘删除键
        }
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t9").click();
        driver.findElementById(pageName + ":id/t8").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("696563624985261");//输入条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/search").click();//再次点击搜索，第一次搜索会弹出网络错误提示
        Thread.sleep(30 * 1000);
        AndroidElement element3 = driver.findElementsById(pageName + ":id/commodity_NO").get(0);//获取第一个数据
        String commodity_no3 = element3.getText();//记录商品库存
        System.out.println("库存为：" + commodity_no3 + "预计库存为：" + (Integer.parseInt(commodity_no2) - 1));
        Assert.assertTrue("商品库存居然没变化！", String.valueOf(Integer.parseInt(commodity_no2) - 1).equals(commodity_no3));
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
    }

    /**
     * case 9:与case 8相同
     */

    @Test
    public void RetrieveCommodityInventory1Activity10() throws InterruptedException {
        System.out.println("-----------case:不输入条形码搜索商品,提示：请输入商品条形码...");
        System.out.println("观察是否弹出提示：请输入商品条形码");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(2 * 1000);

        //--------------断网测试
        System.out.println("观察是否弹出提示：请输入商品条形码");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(2 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
    }

    /**
     * case 11-14 暂无法实现，需要联合nbr端查看
     */

    @Test
    public void RetrieveCommodityInventory1Activity15() throws InterruptedException {
        System.out.println("-----------case:输入库存小于0的商品条形码搜索,正常搜索");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();

        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
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

//        driver.findElementById(pageName+":id/condition_input").sendKeys("1234567801");//输入库存小于0的商品条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(2 * 1000);
        AndroidElement element1 = driver.findElementsById(pageName + ":id/commodity_NO").get(0);//获取第一个数据
        Assert.assertTrue("库存不小于0，换个条形码吧！", Integer.parseInt(element1.getText()) < 0);
    }

    @Test
    public void RetrieveCommodityInventory1Activity16() throws InterruptedException {
        System.out.println("-------------case:有网登录，断网进行全部同步");
        System.out.println("观察是否弹出网络错误的提示框，是的话测试通过");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/sync_text").click();//点击同步全部数据
        Thread.sleep(3 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
    }

    @Test
    public void RetrieveCommodityInventory1Activity17() throws InterruptedException {
        System.out.println("-------------case:有网登录，同步期间断网");
        System.out.println("观察是否弹出网络错误的提示框，是的话测试通过");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/sync_text").click();//点击同步全部数据
        Thread.sleep(2 * 1000);
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(6 * 1000);

    }

    /**
     * case 18-21为切换app测试，暂无法完成
     */

    @Test
    public void RetrieveCommodityInventory1Activity22() throws InterruptedException {
        System.out.println("-------------case:断网登录，前提重置基础资料过，断网输入条形码查询库存");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
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
        driver.findElementById(pageName + ":id/confirm_reserve").click();

        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("1234567");//输入商品条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/search").click();//再次点击搜索，第一次搜索会弹出网络错误提示
        Thread.sleep(10 * 1000);
        AndroidElement element1 = driver.findElementsById(pageName + ":id/commodity_NO").get(0);//获取第一个数据
        Assert.assertTrue("居然查不到数据！", element1 != null);
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
    }

    @Test
    public void RetrieveCommodityInventory1Activity23() throws InterruptedException {
        System.out.println("-------------case:有网登录，前提重置基础资料过，断网，输入条形码查询库存");
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);
        posLogin();

        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("1234567");//输入商品条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/search").click();//再次点击搜索，第一次搜索会弹出网络错误提示
        Thread.sleep(10 * 1000);
        AndroidElement element1 = driver.findElementsById(pageName + ":id/commodity_NO").get(0);//获取第一个数据
        Assert.assertTrue("居然查不到数据！", element1 != null);
    }

    @Test
    public void RetrieveCommodityInventory1Activity24() throws InterruptedException {
        System.out.println("-------------case:断网登录，前提重置基础资料过，断网输入条形码查询库存");
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));//断网
        Thread.sleep(3 * 1000);
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
        driver.findElementById(pageName + ":id/confirm_reserve").click();

        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));//重新联网
        Thread.sleep(3 * 1000);

        driver.findElementById(pageName + ":id/inventory_linear").click();//点击库存
        Thread.sleep(3 * 1000);
        //点击虚拟键盘
        driver.findElementById(pageName + ":id/condition_input").click();
        driver.findElementById(pageName + ":id/en_change_num").click();//切换数字键盘
        driver.findElementById(pageName + ":id/t1").click();
        driver.findElementById(pageName + ":id/t2").click();
        driver.findElementById(pageName + ":id/t3").click();
        driver.findElementById(pageName + ":id/t4").click();
        driver.findElementById(pageName + ":id/t5").click();
        driver.findElementById(pageName + ":id/t6").click();
        driver.findElementById(pageName + ":id/t7").click();
        driver.findElementById(pageName + ":id/num_sure").click();//确定

//        driver.findElementById(pageName+":id/condition_input").sendKeys("1234567");//输入商品条形码
        driver.findElementById(pageName + ":id/search").click();//点击搜索
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/search").click();//再次点击搜索，第一次搜索会弹出网络错误提示
        Thread.sleep(10 * 1000);
        AndroidElement element1 = driver.findElementsById(pageName + ":id/commodity_NO").get(0);//获取第一个数据
        Assert.assertTrue("居然查不到数据！", element1 != null);
    }

    /**
     * case 25-27：输入中文键盘数字，输入英文键盘数字，扫码输入数字，暂无法实现
     */


}
