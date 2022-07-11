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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
public class UTR26searchByConditionsTest {
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
  public void uTR26searchByConditions() {
    LoginSucceed();
    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-cart-simple")).click();
    driver.findElement(By.linkText("库管")).click();
    driver.findElement(By.linkText("库管查询")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.linkText("默认分类")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-nav-child:nth-child(53) a")).getText(), is("默认分类"));
    driver.findElement(By.cssSelector(".layui-nav-child:nth-child(53) a")).click();
    driver.findElement(By.id("time")).click();
    assertThat(driver.findElement(By.cssSelector("#timeList > li:nth-child(2)")).getText(), is("过去一周内"));
    driver.findElement(By.cssSelector("#timeList > li:nth-child(2)")).click();
    driver.findElement(By.name("queryKeyword")).click();
    driver.findElement(By.name("queryKeyword")).sendKeys("果");
    driver.findElement(By.cssSelector(".layui-icon-search")).click();
    vars.put("counts", driver.findElement(By.cssSelector(".layui-laypage-count")).getText());
    driver.switchTo().defaultContent();
    driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
    driver.findElement(By.linkText("库管查询")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.linkText("默认分类")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-nav-child:nth-child(53) a")).getText(), is("默认分类"));
    driver.findElement(By.cssSelector(".layui-nav-child:nth-child(53) a")).click();
    {
      WebElement element = driver.findElement(By.linkText("默认分类"));
   // TODO
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    driver.findElement(By.id("time")).click();
    assertThat(driver.findElement(By.cssSelector("#timeList > li:nth-child(2)")).getText(), is("过去一周内"));
    driver.findElement(By.cssSelector("#timeList > li:nth-child(2)")).click();
    driver.findElement(By.name("queryKeyword")).click();
    driver.findElement(By.name("queryKeyword")).sendKeys("果");
    driver.findElement(By.cssSelector(".layui-icon-search")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-laypage-count")).getText(), is("vars.get(\"counts\").toString()"));
    driver.close();
  }
}
