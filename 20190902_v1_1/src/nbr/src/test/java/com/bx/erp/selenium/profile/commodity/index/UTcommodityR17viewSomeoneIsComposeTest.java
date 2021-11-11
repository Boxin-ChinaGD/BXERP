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
public class UTcommodityR17viewSomeoneIsComposeTest {
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
  public void uTcommodityR17viewSomeoneIsCompose() {
    LoginSucceed();
    driver.findElement(By.cssSelector(".layui-icon-app")).click();
    driver.findElement(By.linkText("商品资料")).click();
    driver.findElement(By.linkText("商品列表")).click();
    driver.switchTo().frame(1);
    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
    driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
    vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
    driver.findElement(By.cssSelector(".combinedCommName")).click();
    driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
    vars.put("commoditySpecification", js.executeScript("return \"规格\" + parseInt(Math.random()*1000000);"));
    driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
    driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys(vars.get("commoditySpecification").toString());
    driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
    vars.put("commodityName1", driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/span")).getText());
    driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
    vars.put("commodityName2", driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[2]/div/span")).getText());
    driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
    driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    driver.findElement(By.linkText("确定")).click();
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    driver.findElement(By.cssSelector("tbody:nth-child(3) > tr:nth-child(1) > td:nth-child(6)")).click();
    driver.findElement(By.cssSelector(".layui-table:nth-child(1) > tbody:nth-child(3) > tr:nth-child(1) > td:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".btnChoosed")).click();
    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("组合商品暂时不允许修改"));
    driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr[2]/td[2]/div")).click();
    driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr[5]/td[2]/div")).click();
    driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr[7]/td[3]/div")).click();
    driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr[9]/td[3]")).click();
    driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr[10]/td[3]/div")).click();
    driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr/td[2]/div")).click();
  }
}
