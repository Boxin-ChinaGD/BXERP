package com.bx.erp.selenium.Promotion;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Promotion_D2Test extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_PRESALE.getIndex();
	}

	@Test
	public void uTD7PresalesDletetePromotion() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline li:nth-child(2)")).getText(), is("未开始"));
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toStopPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}
}
