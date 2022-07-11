package com.bx.erp.selenium.profile.commodity.HistoryR;
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
public class UTR5queryByStaff1Test {
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
  public void uTR5queryByStaff1() {
    LoginSucceed();
    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("修改记录")).click();
    driver.switchTo().frame(1);
    vars.put("historyCount", driver.findElement(By.cssSelector("#commodityHistoryList + div .layui-laypage-count")).getText());
    // TODO
//    System.out.println("商品修改记录条数为：vars.get("historyCount").toString()");
    driver.switchTo().defaultContent();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(2);
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.name("barcodes")).click();
    driver.findElement(By.name("barcodes")).sendKeys("12346589");
    vars.put("commodityName", js.executeScript("return \"龙战于野\" + parseInt(Math.random()*100000);"));
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
    driver.findElement(By.name("specification")).click();
    driver.findElement(By.name("specification")).sendKeys("克");
    driver.findElement(By.name("priceRetail")).click();
    driver.findElement(By.name("priceRetail")).sendKeys("66.00");
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
    vars.put("historyCount", js.executeScript("function test(text){ 		 		var num = parseInt(text.replace(/[^0-9]/ig, \"\")); 		 		num += 2; 		 		text = \"共 \" + num + \" 条\"; 		 		return text; 	 	} 	 	return test(arguments[0]);", vars.get("historyCount")));
   // TODO
//    System.out.println("此时应查到的商品修改记录条数为：vars.get("historyCount").toString()");
    driver.switchTo().defaultContent();
    driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.id("operator")).click();
    driver.findElement(By.cssSelector("#operatorList > li:nth-child(1)")).click();
    assertThat(driver.findElement(By.cssSelector("#commodityHistoryList + div .layui-laypage-count")).getText(), is("vars.get(\"historyCount\").toString()"));
    driver.close();
  }
}
