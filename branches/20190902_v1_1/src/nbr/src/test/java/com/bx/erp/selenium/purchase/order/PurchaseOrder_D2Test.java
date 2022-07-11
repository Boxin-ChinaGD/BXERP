package com.bx.erp.selenium.purchase.order;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class PurchaseOrder_D2Test extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_PRESALE.getIndex();
	}

	@Test
	public void uTD3PresaledeletePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
		driver.close();
	}

	@Test
	public void uTD4PresaledeletePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
		driver.close();
	}

	@Test
	public void uTD5PresaledeletePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
		driver.close();
	}
}
