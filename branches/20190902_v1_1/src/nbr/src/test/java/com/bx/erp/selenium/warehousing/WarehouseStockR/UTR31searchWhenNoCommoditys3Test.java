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
public class UTR31searchWhenNoCommoditys3Test {
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
  public void uTR31searchWhenNoCommoditys3() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
    driver.findElement(By.linkText("库管")).click();
    driver.findElement(By.linkText("库管查询")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.linkText("饮料酒水")).click();
    driver.findElement(By.linkText("饮用水")).click();
    driver.findElement(By.id("time")).click();
    assertThat(driver.findElement(By.cssSelector("#timeList > li:nth-child(3)")).getText(), is("过去一月内"));
    driver.findElement(By.cssSelector("#timeList > li:nth-child(3)")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
    driver.close();
  }
}
