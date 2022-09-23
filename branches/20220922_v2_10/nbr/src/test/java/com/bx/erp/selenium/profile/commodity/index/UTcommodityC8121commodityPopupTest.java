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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
public class UTcommodityC8121commodityPopupTest {
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
  public void uTcommodityC8121commodityPopup() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector("#layui-laypage-2 > .layui-laypage-prev > .layui-icon")).click();
    driver.findElement(By.cssSelector("#layui-laypage-2 > .layui-laypage-next > .layui-icon")).click();
    driver.findElement(By.cssSelector("#layui-laypage-3 > .layui-laypage-prev > .layui-icon")).click();
    driver.findElement(By.linkText("27")).click();
    driver.findElement(By.cssSelector("#layui-laypage-5 > .layui-laypage-prev > .layui-icon")).click();
    driver.findElement(By.id("layui-laypage-6")).click();
    driver.findElement(By.cssSelector("#layui-laypage-6 .layui-input")).sendKeys("30");
    driver.findElement(By.cssSelector("#layui-laypage-6 .layui-laypage-btn")).click();
    driver.findElement(By.id("layui-laypage-7")).click();
    driver.findElement(By.id("middlePart")).click();
    driver.findElement(By.cssSelector("#layui-laypage-7 .layui-laypage-btn")).click();
    driver.findElement(By.cssSelector("#layui-laypage-8 > .layui-laypage-skip")).click();
    driver.findElement(By.cssSelector("#layui-laypage-8 .layui-input")).sendKeys("77");
    driver.findElement(By.cssSelector("#layui-laypage-8 .layui-laypage-btn")).click();
    {
      WebElement element = driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input"));
      // TODO 手动导包过，令其编译通过
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input")).click();
    driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input")).sendKeys("2");
    driver.findElement(By.cssSelector("#layui-laypage-9 .layui-laypage-btn")).click();
    driver.findElement(By.cssSelector("#layui-laypage-10 .layui-input")).click();
    driver.findElement(By.cssSelector("#layui-laypage-10 .layui-input")).sendKeys("16");
    driver.findElement(By.cssSelector("#layui-laypage-10 .layui-laypage-btn")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(10)")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(10) > .layui-input")).sendKeys("1");
    driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(10) > .layui-laypage-btn")).click();
    driver.findElement(By.cssSelector("#layui-laypage-12 > .layui-laypage-next > .layui-icon")).click();
    driver.findElement(By.cssSelector("#layui-laypage-12 > .layui-laypage-next > .layui-icon")).click();
    driver.findElement(By.linkText("27")).click();
  }
}
