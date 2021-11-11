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
public class UTcommodityR12queryByCategoryAndKeyValue4Test {
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
  public void uTcommodityR12queryByCategoryAndKeyValue4() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    {
      WebElement element = driver.findElement(By.linkText("商品列表"));
      // TODO 手动导包过，令其编译通过
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element, 0, 0).perform();
    }
    driver.switchTo().frame(1);
    driver.findElement(By.linkText("默认分类")).click();
    driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
    driver.findElement(By.name("queryKeyword")).click();
    driver.findElement(By.name("queryKeyword")).sendKeys("5644444444444444444478977777777777777777777777777777777777788888");
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("查无商品"));
  }
}
