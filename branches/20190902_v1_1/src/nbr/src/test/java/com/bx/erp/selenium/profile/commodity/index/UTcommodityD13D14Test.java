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
public class UTcommodityD13D14Test {
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
  @Test
  public void uTcommodityD13D14() {
    driver.get("https://localhost:8888/home/adminLogin.bx");
    driver.findElement(By.name("companySN")).click();
    driver.findElement(By.name("companySN")).sendKeys("668866");
    driver.findElement(By.name("phone")).click();
    driver.findElement(By.name("phone")).click();
    driver.findElement(By.name("phone")).sendKeys("13888888888");
    driver.findElement(By.name("pwdEncrypted")).click();
    driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
    driver.findElement(By.name("pwdEncrypted")).sendKeys(Keys.ENTER);
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
    driver.findElement(By.cssSelector(".toDeleteBarcode")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".toDeleteBarcode"));
      // TODO 手动导包过，令其编译通过
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      // TODO 手动导包过，令其编译通过
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.cssSelector(".toAddBarcode")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".toDeleteBarcode"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("12345678");
    driver.findElement(By.cssSelector("body")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
  }
}
