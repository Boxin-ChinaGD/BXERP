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
public class UTR10queryByPayWayAndStaffTest {
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
  public void uTR10queryByPayWayAndStaff() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-chart-screen")).click();
    driver.findElement(By.linkText("报表")).click();
    driver.findElement(By.linkText("销售记录")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-inline:nth-child(1) > .layui-form-label label")).click();
    driver.findElement(By.xpath("//li[contains(.,\'店长\')]")).click();
    driver.findElement(By.cssSelector(".pattern > .layui-form-label label")).click();
    driver.findElement(By.cssSelector(".pattern li:nth-child(2)")).click();
    vars.put("counts", driver.findElement(By.cssSelector(".layui-laypage-count")).getText());
    driver.switchTo().defaultContent();
    driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
    driver.findElement(By.linkText("销售记录")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-inline:nth-child(1) > .layui-form-label label")).click();
    driver.findElement(By.xpath("//li[contains(.,\'店长\')]")).click();
    driver.findElement(By.cssSelector(".pattern > .layui-form-label label")).click();
    driver.findElement(By.cssSelector(".pattern li:nth-child(2)")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-laypage-count")).getText(), is("vars.get(\"counts\").toString()"));
    driver.close();
  }
}