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
public class UTcommodityC8checkSpecification1Test {
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
  public void uTcommodityC8checkSpecification1() {
    LoginSucceed();
    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.name("barcodes")).click();
    driver.findElement(By.name("barcodes")).sendKeys("1234567");
    driver.findElement(By.cssSelector("div.generalCommodityInfo.layui-form > h3")).click();
    vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
    driver.findElement(By.cssSelector("div.generalCommodityInfo.layui-form > h3")).click();
    driver.findElement(By.name("specification")).click();
    driver.findElement(By.name("specification")).sendKeys("！错误的规格！");
    driver.findElement(By.cssSelector("div.generalCommodityInfo.layui-form > h3")).click();
    driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
    assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("允许以中英数值、空格形式出现，不允许使用特殊符号"));
    driver.findElement(By.cssSelector(".layui-form-item:nth-child(10) > .layui-inline:nth-child(2)")).click();
    driver.findElement(By.name("specification")).sendKeys(" ");
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
  }
}
