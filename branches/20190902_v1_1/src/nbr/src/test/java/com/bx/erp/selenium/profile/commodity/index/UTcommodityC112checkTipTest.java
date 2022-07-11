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
public class UTcommodityC112checkTipTest {
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
  public void uTcommodityC112checkTip() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".commodityManage:nth-child(4)")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
    driver.findElement(By.linkText("取消")).click();
    driver.findElement(By.cssSelector("#middlePart > .topArea > .layui-icon")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
    driver.findElement(By.cssSelector(".layui-layer-close")).click();
    driver.findElement(By.linkText("文体用品")).click();
    driver.findElement(By.linkText("办公用品")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
    driver.findElement(By.linkText("取消")).click();
    driver.findElement(By.linkText("2")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
    driver.findElement(By.cssSelector(".layui-layer-close")).click();
    driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
    driver.findElement(By.linkText("确定")).click();
    if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'trChoosed\').length >= 1)")) {
      System.out.println("测试通过");
    } else {
    }
  }
}
