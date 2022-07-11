package com.bx.erp.selenium.warehousingReport.SalesRecordR;
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
public class UTR13pagenationOfTableTest {
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
    driver.manage().window().setSize(new Dimension(1936, 1056));
    driver.findElement(By.name("companySN")).click();
    driver.findElement(By.name("companySN")).sendKeys("668866");
    driver.findElement(By.name("phone")).click();
    driver.findElement(By.name("phone")).sendKeys("15854320895");
    driver.findElement(By.name("pwdEncrypted")).click();
    driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
    driver.findElement(By.cssSelector(".layui-btn")).click();
  }
  @Test
  public void uTR13pagenationOfTable() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-chart-screen")).click();
    driver.findElement(By.linkText("报表")).click();
    driver.findElement(By.linkText("销售记录")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.linkText("2")).click();
    driver.findElement(By.cssSelector(".layui-input")).click();
    driver.findElement(By.cssSelector(".layui-input")).sendKeys("3");
    driver.findElement(By.cssSelector("button.layui-laypage-btn")).click();
    {
      WebElement element = driver.findElement(By.linkText("1"));
      // TODO
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.linkText("1"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.linkText("1"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).release().perform();
    }
    driver.findElement(By.linkText("1")).click();
    driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon")).click();
    driver.findElement(By.linkText("2")).click();
    driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon")).click();
    driver.findElement(By.linkText("314")).click();
    driver.findElement(By.cssSelector(".layui-laypage-next > .layui-icon")).click();
    driver.findElement(By.linkText("313")).click();
    driver.findElement(By.cssSelector(".layui-laypage-next > .layui-icon")).click();
    driver.findElement(By.id("purchasingOrderMain")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys(" ");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
    driver.findElement(By.id("purchasingOrderMain")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("76767999");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.cssSelector(".left_table")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("67");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.id("layui-laypage-11")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("314");
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.id("layui-laypage-12")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("100");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("1000");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.close();
  }
}
