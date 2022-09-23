package com.bx.erp.selenium.profile.commodity.HistoryR;
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
public class UTR26queryHistorynoData1Test {
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
  public void uTR26queryHistorynoData1() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("修改记录")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.name("queryKeyword")).click();
    driver.findElement(By.name("queryKeyword")).sendKeys("测试查无商品修改记录的情况");
    {
      List<WebElement> elements = driver.findElements(By.cssSelector("#commodityHistoryList + div .layui-laypage-count"));
      assert(elements.size() == 0);
    }
    {
      String value = driver.findElement(By.name("barcodes")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("brandName")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("name")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("specification")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("shortName")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("shelfLife")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("categoryName")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("priceRetail")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("packageUnit")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("priceVIP")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("providerName")).getAttribute("value");
      assertThat(value, is(" "));
    }
    {
      String value = driver.findElement(By.name("NO")).getAttribute("value");
      assertThat(value, is(" "));
    }
    if ((Boolean) js.executeScript("return (document.getElementById(\'units\').getAttribute(\'disabled\') == \'disabled\')")) {
      System.out.println("测试通过");
    } else {
    }
    if ((Boolean) js.executeScript("return (window.getComputedStyle(document.getElementById(\'packageUnits\'))[\'display\'] == \'none\')")) {
      System.out.println("测试通过");
    } else {
    }
  }
}
