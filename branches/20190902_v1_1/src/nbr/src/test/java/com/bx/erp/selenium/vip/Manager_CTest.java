package com.bx.erp.selenium.vip;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Manager_CTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTManagerC1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("星巴克AB");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199607016031");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803196707016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13545678113");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13567678113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("birthday")).sendKeys("2020/03/25");
		driver.findElement(By.cssSelector(".vipInfoForm > .layui-form-item:nth-child(7) > .layui-form-label")).click();
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wang11dachui@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("上海");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerC2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199607016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13545678113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("birthday")).sendKeys("2020/03/25");
		driver.findElement(By.cssSelector(".layui-this")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("66666@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("广东");
		driver.findElement(By.cssSelector(".createVip")).click();
	}

	@Test
	public void uTManagerC3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		{
			WebElement element = driver.findElement(By.linkText("会员管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("星巴克A");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199607016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13599888113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("birthday")).sendKeys("2020/03/25");
		driver.findElement(By.cssSelector(".layui-this")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wan12gdachui@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("广州");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerC4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("M82A1");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199607016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13545678113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("birthday")).sendKeys("2020/03/25");
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(4)")).click();
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("66666@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("广州");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerC5() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("默认供应商");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199607016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13545678113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("birthday")).sendKeys("2020/03/24");
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(4)")).click();
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wangdachui@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("上海");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerC6() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("星巴克AB");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199607016031");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("birthday")).sendKeys("2020/03/25");
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wangdachui@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("广州");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".createVip")).click();
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.close();
	}

	@Test
	public void uTManagerC7() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("新会员");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13222222222");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-day-next:nth-child(4)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wangdachui@bx.vip");
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wang111dachui@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("上海");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerC8() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("M82A1");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199607016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13545678113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-day-next:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wangdachui@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("广东");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerC9() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("新会员");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199607016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13545678123");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(6) > .laydate-day-next:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("广州");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerC10() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("新会员");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199907016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13599978113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(6) > .laydate-day-next:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wangd123achui@bx.vip");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerC11() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("星巴克AB");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("32080319907016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13545678113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("birthday")).sendKeys("2020/03/25");
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("66666@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("上海");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
	}

	@Test
	public void uTManagerC12() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("星巴克AB");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199607016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("1354568113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("birthday")).sendKeys("2020/03/25");
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wangdachui@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("广州");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
	}

	@Test
	public void uTManagerC13() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("星");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) > .layui-form-label")).click();
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803199607016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13545678113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("birthday")).sendKeys("2020/03/25");
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wangdachui@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("上海");
		driver.findElement(By.cssSelector(".createVip")).click();
	}

	@Test
	public void uTManagerC14() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("新会员");
		driver.findElement(By.name("iCID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("iCID")).sendKeys("320803198607016031");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13545869113");
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".vipInfoForm > .layui-form-item:nth-child(6) > .layui-form-label")).click();
		driver.findElement(By.name("email")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("email")).sendKeys("wan454gdachui@bx.vip");
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("上海");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".createVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerU15() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("M82A1");
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys("13455895756");
		driver.findElement(By.cssSelector(".createVip")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}
}
