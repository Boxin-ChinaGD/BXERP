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
public class UTcommodityC4checkName1Test {
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
  public void uTcommodityC4checkName1() {
    LoginSucceed();
    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.name("barcodes")).click();
    driver.findElement(By.name("barcodes")).sendKeys("12346589");
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).sendKeys("小当家#￥");
    assertThat(driver.findElement(By.id("checkField")).getText(), is("商品名称格式错误，请输入中英数值、空格(只允许中间出现)，支持的符号为()（）-——_，长度为（0,32]"));
    driver.findElement(By.name("name")).sendKeys(" ");
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
  }
}
