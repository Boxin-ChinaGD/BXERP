package com.bx.erp.selenium.cache.UpdateU;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CacheUpdateUTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		setOperatorType(EnumOperatorType.EOT_OP.getIndex());
		super.setUp();
	}

	@Test
	public void uTU1updateCache1() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-templeate-1")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("更新缓存")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.xpath("//dd[contains(.,\'BX一号分公司\')]")).click();
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) .layui-input")).click();
		driver.findElement(By.xpath("//dd[contains(.,\'ECT_Brand\')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除nbr公司的ID为12 缓存ECT_Brand成功"));
	}

	@Test
	public void uTU2updateCache2() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-templeate-1")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("更新缓存")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.xpath("//dd[contains(.,\'BX一号分公司\')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除nbr公司的ID为52 缓存null失败!!!"));
	}
}
