package com.bx.erp.selenium.purchase.order;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class PurchaseOrder_DTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTD1deletePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabControl li:nth-child(3) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除采购单成功"));
	}

	@Test
	public void uTD2deletePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".odd:nth-child(10) > td > .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(9) > td > .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".odd:nth-child(8) > td > .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(7) > td > .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-close-fill")).click();
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
	}
}
