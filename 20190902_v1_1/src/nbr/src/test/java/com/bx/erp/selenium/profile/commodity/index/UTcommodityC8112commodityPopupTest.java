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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
public class UTcommodityC8112commodityPopupTest {
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
  public void uTcommodityC8112commodityPopup() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
    vars.put("commodityCategoryCount", js.executeScript("return document.getElementsByClassName(\'leftRegion\')[0].getElementsByClassName(\'layui-nav-item\').length;"));
    // TODO
//    System.out.println("商品类别的数目为：vars.get("commodityCategoryCount").toString()");
    driver.findElement(By.cssSelector(".showAllCommCategory:nth-child(3)")).click();
    assertThat(driver.findElement(By.cssSelector(".showAllCommCategory:nth-child(3)")).getText(), is("全部关闭"));
    if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'leftRegion\')[0].getElementsByClassName(\'layui-nav-itemed\').length != arguments[0])", vars.get("commodityCategoryCount"))) {
    }
    driver.findElement(By.cssSelector(".showAllCommCategory:nth-child(3)")).click();
    assertThat(driver.findElement(By.cssSelector(".showAllCommCategory:nth-child(3)")).getText(), is("全部展开"));
    if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'leftRegion\')[0].getElementsByClassName(\'layui-nav-itemed\').length != 0)")) {
    }
  }
}
