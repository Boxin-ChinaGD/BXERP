package com.bx.erp.selenium.purchase.order;

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

import com.bx.erp.selenium.BaseSeleniumTest;
import com.bx.erp.selenium.BaseSeleniumTest.EnumOperatorType;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;

public class PurchaseOrder_UTest extends BaseSeleniumTest {
	@Before
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTU1updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
	}

	@Test
	public void uTU20updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys("5656");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU3updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-input")).sendKeys("566536");
	}

	@Test
	public void uTU4updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).sendKeys("2.3");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU5updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).sendKeys("第三帝国");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).sendKeys("ying");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).sendKeys("-1");
	}

	@Test
	public void uTU6updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).sendKeys(" ");
	}

	@Test
	public void uTU7updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .addNum")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-table-hover .reduceNum"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
	}

	@Test
	public void uTU8updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys("5656");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU9updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover > .layui-table-col-special")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys("2.3");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU10updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys("放电饭锅");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品数量"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys("dfsdaf");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品数量"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys("-1");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品数量"));
	}

	@Test
	public void uTU11updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys(" ");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品数量"));
	}

	@Test
	public void uTU12updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys("5656");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU13updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys("2.3");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU14updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("#allCommodity > .rightRegion")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys("uukghjkj");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品价格"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys("卡中科金财");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品价格"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys("-1");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品价格"));
	}

	@Test
	public void uTU15updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys(" ");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU16updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).sendKeys("5656");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".right-data")).click();
	}

	@Test
	public void uTU17updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		{
			WebElement element = driver.findElement(By.linkText("全部商品"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).sendKeys("2");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".content")).click();
	}

	@Test
	public void uTU18updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .purchase-time")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).sendKeys("1");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).sendKeys("1");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).sendKeys(" ");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU19updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).sendKeys(" ");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(10) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU21updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys("2.3");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU22updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys("dsgasdg");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys("啥华东师大");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys("-1");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU23updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys(" ");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(11) > input")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTU24updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".td_search")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("采购订单修改成功"));
	}

	@Test
	public void uTU25updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).sendKeys("1000");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".content")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU26updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[3]/div/ul/li[2]/div/div/label")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[2]/ul/li/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@name=\'price\']")).sendKeys("180.00");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//input[@name=\'price\'])[8]")).sendKeys("50");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".odd:nth-child(8) > td:nth-child(6) > input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("采购订单修改成功"));
	}

	@Test
	public void uTU27updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[2]/ul/li/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[2]/ul/li[2]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[3]/div/ul/li/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[2]/ul/li[2]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("采购订单修改成功"));
	}

	@Test
	public void uTU28updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[3]/div/ul/li/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[2]/ul/li/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr/td[2]")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//i[@onclick=\'deletecommodity(this)\'])[10]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//i[@onclick=\'deletecommodity(this)\'])[9]")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//i[@onclick=\'deletecommodity(this)\'])[8]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//i[@onclick=\'deletecommodity(this)\'])[7]")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("采购订单修改成功"));
	}

	@Test
	public void uTU29updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[2]/ul/li/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr/td[2]")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[2]/ul/li[2]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改信息后再保存"));
	}

	@Test
	public void uTU30PresaleupdataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTU31PresaleupdataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTU32updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).sendKeys("服务型");
	}

	@Test
	public void uTU33updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[2]/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("采购订单修改成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[10]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("providerinput")).sendKeys("43233");
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("采购订单修改成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .list_option")).click();
	}

	@Test
	public void uTU34updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).sendKeys("168");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("CG76");
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU36updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[10]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("#layui-laypage-4 > a:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
		driver.findElement(By.cssSelector(".odd:nth-child(2) button > .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("#layui-laypage-7 > a:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[8]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[9]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
	}

	@Test
	public void uTU37updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[10]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).sendKeys("10001");
		Thread.sleep(2000);
		{
			WebElement element = driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.cssSelector(".content"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.cssSelector(".content")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).sendKeys("10000");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
	}

	@Test
	public void uTU38updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[2]/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[10]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("purchasingOrder")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("price")).sendKeys("10001.00");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		{
			WebElement element = driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).sendKeys("10001");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) td:nth-child(5)")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).sendKeys("100");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("price")).sendKeys("1.00");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
	}

	@Test
	public void uTU39updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[10]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-form-checked > .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("16")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[2]/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
		Thread.sleep(2000);
		driver.switchTo().defaultContent();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("商品资料")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(2000);
		driver.switchTo().frame(2);
		Thread.sleep(2000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("queryKeyword")).sendKeys("可口可乐");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover span")).click();
	}

	@Test
	public void uTU40updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".td_search")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[2]/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("商品资料")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(2000);
		driver.switchTo().frame(3);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) .layui-unselect .layui-input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("商品至少需要有一个供应商\\\\n不支持删除全部的供应商"));
		driver.findElement(By.cssSelector(".layui-table-hover span")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .laytable-cell-5-0-0")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) .layui-unselect .layui-input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(10)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
	}

	@Test
	public void uTU41updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".odd:nth-child(4) button > .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("#layui-laypage-7 > a:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("1")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr[10]/td[2]/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("商品资料")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(2000);
		driver.switchTo().frame(2);
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr[9]/td/div/span")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr/td")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
	}

	@Test
	public void uTU42updataPurchaseOrder() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		{
			WebElement element = driver.findElement(By.linkText("采购"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.linkText("采购"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.linkText("采购"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		{
			WebElement element = driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn"));
			Action builder = (Action) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr[9]/td[2]/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".BX_title > span")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("商品资料")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(2000);
		driver.switchTo().frame(3);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) .layui-unselect .layui-input")).click();
	}
}
