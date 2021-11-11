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
public class UTR7queryRetailSheetBySN2Test {
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
  public void uTR7queryRetailSheetBySN2() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-chart-screen")).click();
    driver.findElement(By.linkText("报表")).click();
    driver.findElement(By.linkText("销售记录")).click();
    driver.switchTo().frame(1);
    vars.put("counts", driver.findElement(By.cssSelector("#allSalesNoteList + div .layui-laypage-count")).getText());
    // TODO
//    System.out.println("销售记录的条数为：vars.get("counts").toString()");
    driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div[4]/input")).click();
    driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div[4]/input")).sendKeys("123456789");
    driver.findElement(By.cssSelector(".layui-icon")).click();
    assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，零售单号至少需要输入10位"));
    driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div[4]/input")).sendKeys("A直升飞机AA");
    driver.findElement(By.cssSelector(".layui-icon-search")).click();
    vars.put("value", js.executeScript("return \" 直升飞机\";"));
    driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div[4]/input")).sendKeys(vars.get("value").toString());
    driver.findElement(By.cssSelector(".layui-icon")).click();
    assertThat(driver.findElement(By.id("queryMsg")).getText(), is("输入的关键字首尾不能有空格，只允许中间有空格"));
    driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div[4]/input")).sendKeys("@直升飞机");
    driver.findElement(By.cssSelector(".layui-icon")).click();
    assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，长度为(0,32]，支持输入的符号有：（）()_-——"));
    driver.close();
  }
}
