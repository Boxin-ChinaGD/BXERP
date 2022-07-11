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
public class UTcommodityR14viewSomeoneHadManyBarcodeTest {
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
  public void uTcommodityR14viewSomeoneHadManyBarcode() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.name("barcodes")).click();
    driver.findElement(By.name("barcodes")).sendKeys("1234567");
    vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
    vars.put("commoditySpecification", js.executeScript("return \"规格\" + parseInt(Math.random()*1000000);"));
    driver.findElement(By.name("specification")).click();
    driver.findElement(By.name("specification")).sendKeys(vars.get("commoditySpecification").toString());
    vars.put("commodityPriceRetail", js.executeScript("return parseFloat(Math.random()*10000).toFixed(2);"));
    driver.findElement(By.name("priceRetail")).click();
    driver.findElement(By.name("priceRetail")).sendKeys(vars.get("commodityPriceRetail").toString());
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    driver.findElement(By.linkText("确定")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
    driver.findElement(By.cssSelector(".toAddBarcode")).click();
    driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("7654321");
    driver.findElement(By.cssSelector("body")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
    driver.findElement(By.cssSelector(".toAddBarcode")).click();
    driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) .layui-input")).sendKeys("1111111");
    driver.findElement(By.cssSelector("body")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
    driver.findElement(By.cssSelector(".trChoosed span")).click();
    assertThat(driver.findElement(By.cssSelector(".trChoosed span")).getText(), is("vars.get(\"commodityName\").toString()"));
    assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-4-0-2")).getText(), is("vars.get(\"commoditySpecification\").toString()"));
    assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-4-0-5")).getText(), is("vars.get(\"commodityPriceRetail\").toString()"));
    {
      String value = driver.findElement(By.name("name")).getAttribute("value");
      // TODO
//      assertThat(value, is("vars.get("commodityName").toString()"));
    }
    {
      String value = driver.findElement(By.name("specification")).getAttribute("value");
      // TODO
//      assertThat(value, is("vars.get("commoditySpecification").toString()"));
    }
    {
      String value = driver.findElement(By.name("priceRetail")).getAttribute("value");
      // TODO
//      assertThat(value, is("vars.get("commodityPriceRetail").toString()"));
    }
    if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'otherBarcode\').length != 2)")) {
    }
  }
}
