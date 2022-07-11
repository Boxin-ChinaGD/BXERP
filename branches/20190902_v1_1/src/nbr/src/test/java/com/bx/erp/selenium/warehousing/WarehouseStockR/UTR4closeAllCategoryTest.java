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
public class UTR4closeAllCategoryTest {
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
  public void uTR4closeAllCategory() {
    LoginSucceed();
    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-cart-simple")).click();
    driver.findElement(By.linkText("库管")).click();
    driver.findElement(By.linkText("库管查询")).click();
    driver.switchTo().frame(1);
    vars.put("categoryCounts ", js.executeScript("var categoryCounts = document.getElementById(\"categoryList\").getElementsByClassName(\"layui-nav-item\").length; return categoryCounts ;"));
    driver.findElement(By.cssSelector("span.allCategoryShow")).click();
    vars.put("categoryShowedCounts", js.executeScript("var categoryShowedCounts = document.getElementById(\"categoryList\").getElementsByClassName(\"layui-nav-itemed\").length; return categoryShowedCounts;"));
    if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("categoryCounts"),vars.get("categoryShowedCounts"))) {
      System.out.println("展开全部商品类别成功");
    } else {
    }
    driver.findElement(By.cssSelector("span.allCategoryShow")).click();
    vars.put("categoryShowedCounts", js.executeScript("var categoryShowedCounts = document.getElementById(\"categoryList\").getElementsByClassName(\"layui-nav-itemed\").length; return categoryShowedCounts;"));
    if ((Boolean) js.executeScript("return (arguments[0] == 0)", vars.get("categoryShowedCounts"))) {
      System.out.println("测试通过");
    } else {
    }
    driver.close();
  }
}
