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
 * Created by WPNA on 2020/4/1.
 */

public class SmallSheet1Activity_D_AppiumTest extends BaseAppiumTest {

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
        System.out.println("---------case:删除任意一张小票格式，都会展示最新的格式");
        posLogin();

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
        chose_smallsheet("格式 4");
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(3 * 1000);
    }

    @Test
    public void SmallSheet1Activity02() throws InterruptedException {
        System.out.println("---------case:删除默认小票格式，提示不可删除");
        posLogin();

        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
        chose_smallsheet("格式 1");
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(3 * 1000);
    }

    @Test
    public void SmallSheet1Activity03() throws InterruptedException {
        System.out.println("---------case:在断网的情况下，删除小票格式退出登录，在联网情况下登录，进入小票页面，小票格式被删除");
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
        //断网
        driver.setNetworkConnection(new NetworkConnectionSetting(true, false, false));
        Thread.sleep(3 * 1000);
        chose_smallsheet("格式 3");
        driver.findElementById(pageName + ":id/smallsheet_delete").click();//点击删除
        Thread.sleep(3 * 1000);

        //交班退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/logout").click();//退出按钮
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/sure").click();//确定退出
        Thread.sleep(2 * 1000);
        driver.findElementById(pageName + ":id/change_shifts").click();//交班按钮
        Thread.sleep(5 * 1000);

        //重新联网
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);

        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/printFormatVersion").click();//点击格式

        List<AndroidElement> list = driver.findElementsById(pageName + ":id/textview");//获取所有的格式
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                list.get(i).click();
                if (driver.findElementById(pageName + ":id/design_header").getText().equals("小票3")) {
                    Assert.assertTrue("可能还存留着，没有被删除成功", false);
                    break;
                }
            } else {
                driver.findElementById(pageName + ":id/printFormatVersion").click();//点击格式
                driver.findElementsById(pageName + ":id/textview").get(i).click();
                if (driver.findElementById(pageName + ":id/design_header").getText().equals("小票3")) {
                    Assert.assertTrue("可能还存留着，没有被删除成功", false);
                    break;
                }
            }

        }
    }

    @Test
    public void SmallSheet1Activity04() throws InterruptedException {
        System.out.println("-----------case:在联网情况下新增或者修改小票格式，使用改格式");
        //重新联网
        driver.setNetworkConnection(new NetworkConnectionSetting(false, true, false));
        Thread.sleep(3 * 1000);
        posLogin();
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(4 * 1000);
        driver.findElementById(pageName + ":id/smallsheet_create").click();//点击新建格式
        driver.findElementById(pageName + ":id/design_header").click();
        driver.findElementById(pageName + ":id/design_header").sendKeys("页眉被新建");
        driver.findElementById(pageName + ":id/create_confirm").click();//点击保存新建
        Thread.sleep(6 * 1000);
        driver.findElementById(pageName + ":id/useCurrentFormat").click();//点击使用该格式
        Thread.sleep(5 * 1000);

        driver.findElementById(pageName + ":id/sale_linear").click();//点击销售按钮
        Thread.sleep(1000);
        driver.findElementById(pageName + ":id/set_up").click(); //设置按钮
        driver.findElementById(pageName + ":id/desk_ticket").click();//前台小票按钮
        Thread.sleep(1000);
        Assert.assertTrue("展示的不是使用的格式！可能新建小票格式后使用失败", driver.findElementById(pageName + ":id/design_header").getText().equals("页眉被新建"));
    }

    /**
     * case 5 ：暂无法实现，需要创建一个新公司，不能删除默认小票格式已经在case 2 当中实现
     * case 6 ：删除小票格式，不传ID到服务器，暂无法实现
     * case 7 ：将不存在的小票ID传到服务器，暂无法实现
     * case 8 ：与SmallSheet1Activity_R_AppiumTest/case 09 相同；
     */


}
