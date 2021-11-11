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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
public class UTR24pagenationTest {
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
  public void uTR24pagenation() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("修改记录")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-laypage-next > .layui-icon")).click();
    driver.findElement(By.cssSelector(".layui-laypage-next > .layui-icon")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-next > .layui-icon"));
      // TODO 手动导包过，令其编译通过
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon")).click();
    driver.findElement(By.cssSelector(".layui-laypage-next > .layui-icon")).click();
    driver.findElement(By.cssSelector(".layui-laypage-next > .layui-icon")).click();
    driver.findElement(By.cssSelector(".commodityHistoryBottomLeft")).click();
    driver.findElement(By.linkText("1")).click();
    driver.findElement(By.linkText("")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("3");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    driver.findElement(By.cssSelector(".layui-laypage-skip")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("6");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    driver.findElement(By.id("layui-laypage-10")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("46");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
  }
}
