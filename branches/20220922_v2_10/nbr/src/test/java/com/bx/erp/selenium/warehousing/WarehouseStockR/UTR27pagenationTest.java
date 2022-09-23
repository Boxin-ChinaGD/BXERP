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
public class UTR27pagenationTest {
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
  public void uTR27pagenation() {
    LoginSucceed();
    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-cart-simple")).click();
    driver.findElement(By.linkText("库管")).click();
    driver.findElement(By.linkText("库管查询")).click();
    driver.switchTo().frame(1);
    vars.put("counts", driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText());
 // TODO
//    System.out.println("库管查询条数为：vars.get("counts").toString()");
    driver.findElement(By.linkText("2")).click();
    driver.findElement(By.cssSelector("a.layui-laypage-next > i.layui-icon")).click();
    driver.findElement(By.cssSelector("a.layui-laypage-prev > i.layui-icon")).click();
    driver.findElement(By.xpath("//div[@id=\'layui-laypage-4\']/span[3]/input")).sendKeys("5");
    driver.findElement(By.cssSelector("button.layui-laypage-btn")).click();
    driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon")).click();
    driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon")).click();
    driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon"));
   // TODO
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon")).click();
    driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.linkText("20")).click();
    driver.findElement(By.cssSelector(".layui-laypage-next > .layui-icon")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-next > .layui-icon"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.linkText("18")).click();
    driver.findElement(By.id("layui-laypage-12")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector(".layui-laypage-skip")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys(" ");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("17");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.id("layui-laypage-12")).click();
    driver.findElement(By.cssSelector(".layui-table-page")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("23");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.linkText("18")).click();
    driver.findElement(By.id("layui-laypage-15")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("555");
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".layui-laypage-btn"));
      Action builder = (Action) new Actions(driver);
      ((Actions) builder).moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.cssSelector(".bottomCenter")).click();
    driver.findElement(By.cssSelector(".bottomCenter")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("hhhhhh");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("8hhhh8");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip")).click();
    driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("4t");
    driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
    driver.close();
  }
}
