package com.bx.erp.selenium.warehousing.WarehouseStockR;
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
public class UTR28presalesToViewCommodityTest {
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
  public void PresalesLoginSucceed() {
    driver.get("https://localhost:8888/home/adminLogin.bx");
    driver.manage().window().setSize(new Dimension(1936, 1056));
    driver.findElement(By.name("companySN")).click();
    driver.findElement(By.name("companySN")).sendKeys("668866");
    driver.findElement(By.name("phone")).click();
    driver.findElement(By.name("phone")).sendKeys("13888888888");
    driver.findElement(By.name("pwdEncrypted")).click();
    driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
    driver.findElement(By.cssSelector(".layui-btn")).click();
  }
  @Test
  public void uTR28presalesToViewCommodity() {
    PresalesLoginSucceed();
    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-cart-simple")).click();
    driver.findElement(By.linkText("库管")).click();
    driver.findElement(By.linkText("库管查询")).click();
    driver.switchTo().frame(1);
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
    vars.put("commodityName", driver.findElement(By.xpath("//div[@id=\'warehouseListMain\']/div[2]/div[2]/div[2]/div/div/div[2]/table/tbody/tr/td/div")).getText());
    vars.put("barcodes", driver.findElement(By.xpath("//div[@id=\'warehouseListMain\']/div[2]/div[2]/div[2]/div/div/div[2]/table/tbody/tr/td[2]/div")).getText());
    {
      String value = driver.findElement(By.name("barcodes")).getAttribute("value");
   // TODO
//      assertThat(value, is("vars.get("barcodes").toString()"));
    }
    {
      String value = driver.findElement(By.name("name")).getAttribute("value");
   // TODO
//      assertThat(value, is("vars.get("commodityName").toString()"));
    }
    driver.close();
  }
}
