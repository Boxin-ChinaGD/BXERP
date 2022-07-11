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
public class UTcommodityC18checkShelfLife1Test {
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
  public void uTcommodityC18checkShelfLife1() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.name("barcodes")).click();
    driver.findElement(By.name("barcodes")).sendKeys("1234567");
    vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
    driver.findElement(By.name("specification")).click();
    driver.findElement(By.name("specification")).sendKeys("正确的规格");
    driver.findElement(By.name("priceRetail")).click();
    driver.findElement(By.name("priceRetail")).sendKeys("66");
    driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) > .layui-inline:nth-child(2)")).click();
    driver.findElement(By.name("shelfLife")).sendKeys("to be a man");
    driver.findElement(By.cssSelector(".commodityInfo")).click();
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正整数"));
    driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) > .layui-inline:nth-child(2)")).click();
    driver.findElement(By.name("shelfLife")).sendKeys("666.666");
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正整数"));
    driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) > .layui-inline:nth-child(2)")).click();
    driver.findElement(By.name("shelfLife")).sendKeys("88888888666666");
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合要求，请修改"));
    driver.findElement(By.name("shelfLife")).click();
    driver.findElement(By.name("shelfLife")).sendKeys("保质保期");
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正整数"));
  }
}
