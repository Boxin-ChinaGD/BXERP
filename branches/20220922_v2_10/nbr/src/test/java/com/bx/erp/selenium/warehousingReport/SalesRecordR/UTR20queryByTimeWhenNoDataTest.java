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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
public class UTR20queryByTimeWhenNoDataTest {
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
  public void uTR20queryByTimeWhenNoData() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-chart-screen")).click();
    driver.findElement(By.linkText("报表")).click();
    driver.findElement(By.linkText("销售记录")).click();
    driver.switchTo().frame(1);
    vars.put("counts", driver.findElement(By.cssSelector("#allSalesNoteList + div .layui-laypage-count")).getText());
    driver.findElement(By.cssSelector(".sell > .layui-form-label label")).click();
    driver.findElement(By.cssSelector(".sell li:nth-child(4)")).click();
    driver.findElement(By.id("timeQuantum")).click();
    driver.findElement(By.cssSelector(".laydate-main-list-0 .laydate-prev-y")).click();
    driver.findElement(By.cssSelector(".laydate-main-list-0 .laydate-prev-y")).click();
    driver.findElement(By.cssSelector(".laydate-main-list-0 .laydate-prev-y")).click();
    driver.findElement(By.cssSelector(".laydate-main-list-0 tr:nth-child(3) > td:nth-child(3)")).click();
    driver.findElement(By.cssSelector(".laydate-main-list-1 tr:nth-child(4) > td:nth-child(5)")).click();
    driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
    assertThat(driver.findElement(By.cssSelector(".retailTradeSN span")).getText(), is(" "));
    driver.close();
  }
}
