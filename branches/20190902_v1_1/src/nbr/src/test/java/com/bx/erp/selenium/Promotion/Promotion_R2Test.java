package com.bx.erp.selenium.Promotion;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Promotion_R2Test extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_PRESALE.getIndex();
	}

	@Test
	public void uTR10presalesSearchPromotion() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("count", driver.findElement(By.cssSelector("#layui-laypage-3 > .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-form-label > span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline li:nth-child(1)")).getText(), is("所有"));
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#layui-laypage-3 > .layui-laypage-count")).getText(), is("vars.get(\"count\").toString()"));
	}

}
