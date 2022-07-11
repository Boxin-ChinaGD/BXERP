package com.bx.erp.selenium.wx.createQRcodeC;
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
public class CreateQRcodeC5Test {
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
  public void loginSuccess() {
    driver.get("https://localhost:8888/home/adminLogin.bx");
    driver.manage().window().setSize(new Dimension(1072, 1020));
    driver.findElement(By.name("companySN")).click();
    driver.findElement(By.name("companySN")).sendKeys("668866");
    driver.findElement(By.name("phone")).sendKeys("15854320895");
    driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
    driver.findElement(By.cssSelector(".layui-btn")).click();
  }
  @Test
  public void createQRcodeC5() {
    loginSuccess();
    driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
    driver.findElement(By.linkText("微信小程序二维码")).click();
    {
      WebElement element = driver.findElement(By.linkText("微信小程序二维码"));
   // TODO
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element, 0, 0).perform();
    }
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-input")).click();
    driver.findElement(By.cssSelector(".layui-input")).sendKeys("555");
    driver.findElement(By.cssSelector(".layui-btn")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.linkText("下载二维码")).click();
    driver.close();
  }
}
