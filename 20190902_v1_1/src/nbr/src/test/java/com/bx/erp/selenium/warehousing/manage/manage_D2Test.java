package com.bx.erp.selenium.warehousing.manage;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class manage_D2Test extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_PRESALE.getIndex();
	}

	@Test
	public void uTD4preSaleLogin() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

}
