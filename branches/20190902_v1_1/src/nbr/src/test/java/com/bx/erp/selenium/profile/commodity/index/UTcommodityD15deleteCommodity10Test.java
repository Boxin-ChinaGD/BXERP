package com.bx.erp.selenium.profile.commodity.index;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
public class UTcommodityD15deleteCommodity10Test {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  public void LoginSucceed() {
    driver.get("https://localhost:8888/home/adminLogin.bx");
    driver.manage().window().setSize(new Dimension(1920, 1040));
    driver.findElement(By.name("companySN")).click();
    driver.findElement(By.name("companySN")).sendKeys("668866");
    driver.findElement(By.name("phone")).click();
    driver.findElement(By.name("phone")).sendKeys("15854320895");
    driver.findElement(By.name("pwdEncrypted")).click();
    driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
    driver.findElement(By.cssSelector(".layui-btn")).click();
  }
  @Test
  public void uTcommodityD15deleteCommodity10() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
    driver.findElement(By.cssSelector(".serviceCommodityBarcode")).click();
    driver.findElement(By.cssSelector(".serviceCommodityBarcode")).sendKeys("1234567");
    vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
    driver.findElement(By.cssSelector(".serviceCommName")).click();
    driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(vars.get("commodityName").toString());
    driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
    driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys("66");
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建服务类商品成功"));
    driver.switchTo().defaultContent();
    driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
    driver.findElement(By.linkText("员工资料")).click();
    driver.findElement(By.linkText("员工管理")).click();
    driver.switchTo().frame(2);
    driver.findElement(By.cssSelector(".staffCreate")).click();
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).sendKeys("aaa");
    vars.put("phone", js.executeScript("return \"1111111\" + parseInt(Math.random()*10000);"));
    driver.findElement(By.name("phone")).click();
    driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
    driver.findElement(By.name("newPassword")).click();
    driver.findElement(By.name("newPassword")).sendKeys("000000");
    driver.findElement(By.name("confirmNewPassword")).click();
    driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
    driver.findElement(By.cssSelector(".staffUpdate")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
    driver.switchTo().defaultContent();
    driver.findElement(By.cssSelector(".layui-btn")).click();
    driver.findElement(By.name("companySN")).click();
    driver.findElement(By.name("companySN")).sendKeys("668866");
    driver.findElement(By.name("phone")).click();
    driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
    driver.findElement(By.name("pwdEncrypted")).click();
    driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
    driver.findElement(By.cssSelector(".layui-btn")).click();
    driver.findElement(By.name("password")).click();
    driver.findElement(By.name("password")).sendKeys("000000");
    driver.findElement(By.name("newPassword")).click();
    driver.findElement(By.name("newPassword")).sendKeys("111111");
    driver.findElement(By.name("confirmNewPassword")).click();
    driver.findElement(By.name("confirmNewPassword")).sendKeys("111111");
    driver.findElement(By.cssSelector(".layui-btn")).click();
    driver.findElement(By.linkText("确定")).click();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
    driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
  }
}
