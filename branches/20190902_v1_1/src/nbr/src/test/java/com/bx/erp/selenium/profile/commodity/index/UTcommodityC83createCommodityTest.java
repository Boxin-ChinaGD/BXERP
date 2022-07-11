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
public class UTcommodityC83createCommodityTest {
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
  public void uTcommodityC83createCommodity() {
    LoginSucceed();
    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.name("barcodes")).click();
    driver.findElement(By.name("barcodes")).sendKeys("1234567");
    vars.put("commodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).sendKeys(vars.get("commodityName1").toString());
    driver.findElement(By.name("specification")).click();
    driver.findElement(By.name("specification")).sendKeys("正确的规格");
    driver.findElement(By.name("priceRetail")).click();
    driver.findElement(By.name("priceRetail")).sendKeys("66.00");
    driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
    driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除商品成功"));
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.name("barcodes")).click();
    driver.findElement(By.name("barcodes")).sendKeys("1234567");
    vars.put("commodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
    driver.findElement(By.name("specification")).click();
    driver.findElement(By.name("specification")).sendKeys("正确的规格");
    driver.findElement(By.name("priceRetail")).click();
    driver.findElement(By.name("priceRetail")).sendKeys("66.00");
    driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
    driver.findElement(By.cssSelector(".toAddUnit")).click();
    driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
    driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
    driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
    driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
    driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("commodityName1").toString());
    driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
  }
}
