package com.bx.erp.selenium.vip;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Manager_RTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTManagerR1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form > .layui-input")).sendKeys("14545678115");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
	}

	@Test
	public void uTManagerR2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		{
			WebElement element = driver.findElement(By.linkText("会员管理"));
			Action builder = (Action) new Actions(driver);
			((Actions) builder).moveToElement(element).release().perform();
		}
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form > .layui-input")).sendKeys("13599");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
	}

	@Test
	public void uTManagerR3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
	}

	@Test
	public void uTManagerR4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-btn")).click();
	}

	@Test
	public void uTManagerR5() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-laypage-next > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.linkText(""));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
	}
}
