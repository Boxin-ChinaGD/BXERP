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
public class UTcommodityD19deleteCommodity11Test {
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
  public void uTcommodityD19deleteCommodity11() {
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
    driver.findElement(By.name("priceRetail")).sendKeys("66.00");
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
    driver.switchTo().defaultContent();
    driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
    driver.findElement(By.linkText("促销")).click();
    driver.findElement(By.linkText("满减优惠")).click();
    driver.switchTo().frame(2);
    driver.findElement(By.cssSelector(".createPromotion")).click();
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).sendKeys("新建的促销");
    driver.findElement(By.id("datetimeStart")).click();
    driver.findElement(By.cssSelector(".laydate-next-m")).click();
    driver.findElement(By.cssSelector(".layui-laydate-content tr:nth-child(1) > td:nth-child(4)")).click();
    driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
    driver.findElement(By.id("datetimeEnd")).click();
    driver.findElement(By.cssSelector(".laydate-next-m")).click();
    driver.findElement(By.cssSelector(".laydate-next-m")).click();
    driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
    driver.findElement(By.name("excecutionThreshold")).click();
    driver.findElement(By.name("excecutionThreshold")).sendKeys("10");
    driver.findElement(By.name("excecutionAmount")).click();
    driver.findElement(By.name("excecutionAmount")).sendKeys("1");
    driver.findElement(By.cssSelector(".designatedCommodity")).click();
    driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
    driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
    driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys(vars.get("commodityName").toString());
    driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
    driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
    driver.findElement(By.cssSelector(".updatePromotion")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新建满减优惠活动成功"));
    driver.switchTo().defaultContent();
    driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("该商品有促销依赖，不能删除"));
  }
}