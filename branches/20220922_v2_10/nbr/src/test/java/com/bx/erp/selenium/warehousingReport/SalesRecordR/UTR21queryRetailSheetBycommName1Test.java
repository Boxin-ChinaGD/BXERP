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
public class UTR21queryRetailSheetBycommName1Test {
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
  public void uTR21queryRetailSheetBycommName1() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-chart-screen")).click();
    driver.findElement(By.linkText("报表")).click();
    driver.findElement(By.linkText("销售记录")).click();
    driver.switchTo().frame(1);
    vars.put("counts1", driver.findElement(By.cssSelector("#allSalesNoteList + div .layui-laypage-count")).getText());
    driver.findElement(By.cssSelector(".layui-form-item")).click();
    driver.findElement(By.cssSelector(".top_input")).sendKeys("可");
    driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[2]/div/div/div[2]/table/tbody/tr/td/div")).click();
    vars.put("counts2", driver.findElement(By.cssSelector(".layui-laypage-count")).getText());
    driver.findElement(By.cssSelector(".top_input")).click();
    driver.findElement(By.cssSelector(".top_input")).sendKeys(" ");
    driver.findElement(By.cssSelector(".layui-icon-search")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-laypage-count")).getText(), is("vars.get(\"counts1\").toString()"));
    driver.findElement(By.cssSelector(".top_input")).click();
    driver.findElement(By.cssSelector(".top_input")).sendKeys("可");
    driver.findElement(By.cssSelector(".layui-icon-search")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-laypage-count")).getText(), is("vars.get(\"counts2\").toString()"));
    driver.close();
  }
}
