package com.bx.erp.selenium.vip;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;
import org.openqa.selenium.WebElement;

public class Manager_UTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTManagerU1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("博昕");
		driver.findElement(By.id("layui-button")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
	}

	@Test
	public void uTManagerU2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("博");
		Thread.sleep(1000);
		driver.findElement(By.id("layui-button")).click();
	}

	@Test
	public void uTManagerU3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(8) > .layui-anim")).click();
		driver.findElement(By.id("layui-button")).click();
		{
			WebElement element = driver.findElement(By.id("layui-button"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".selectColor > .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("layui-button")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.id("layui-button"));
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
	public void uTManagerU4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.name("clearBonusDay")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("clearBonusDay")).clear();
		driver.findElement(By.name("clearBonusDay")).sendKeys("300");
		Thread.sleep(1000);
		driver.findElement(By.id("layui-button")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerU5() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.name("clearBonusDay")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form:nth-child(1) > .layui-form-item:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("clearBonusDay")).clear();
		driver.findElement(By.name("clearBonusDay")).sendKeys(" ");
		driver.findElement(By.id("layui-button")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.id("layui-button"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.name("clearBonusDay")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("clearBonusDay")).sendKeys("0");
		Thread.sleep(1000);
		driver.findElement(By.id("layui-button")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.id("layui-button"));
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
	public void uTManagerU6() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.name("initIncreaseBonus")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("initIncreaseBonus")).clear();
		driver.findElement(By.name("initIncreaseBonus")).sendKeys("10");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn"));
			Action builder = (Action) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Action builder = (Action) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerU7() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.name("initIncreaseBonus")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("initIncreaseBonus")).clear();
		driver.findElement(By.name("initIncreaseBonus")).sendKeys(" ");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
	}

	@Test
	public void uTManagerU8() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("amountUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("amountUnit")).clear();
		driver.findElement(By.name("amountUnit")).sendKeys("1000");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn"));
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
	public void uTManagerU9() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(6) div:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("amountUnit")).clear();
		driver.findElement(By.name("amountUnit")).sendKeys("0");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.name("amountUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("amountUnit")).clear();
		driver.findElement(By.name("amountUnit")).sendKeys(" ");
		driver.findElement(By.cssSelector(".layui-form:nth-child(1) > .layui-form-item:nth-child(8)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
	}

	@Test
	public void uTManagerU10() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.name("increaseBonus")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("increaseBonus")).clear();
		driver.findElement(By.name("increaseBonus")).sendKeys("100");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn"));
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
	public void uTManagerU11() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(6) > .layui-input-block > div:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("increaseBonus")).clear();
		driver.findElement(By.name("increaseBonus")).sendKeys(" ");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.name("increaseBonus")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("increaseBonus")).clear();
		driver.findElement(By.name("increaseBonus")).sendKeys("0");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn"));
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
	public void uTManagerU12() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.name("maxIncreaseBonus")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form:nth-child(1) > .layui-form-item:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("maxIncreaseBonus")).clear();
		driver.findElement(By.name("maxIncreaseBonus")).sendKeys("500");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.cssSelector(".layui-layer-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerU13() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form:nth-child(1) > .layui-form-item:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("maxIncreaseBonus")).clear();
		driver.findElement(By.name("maxIncreaseBonus")).sendKeys("0");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.name("maxIncreaseBonus")).clear();
		driver.findElement(By.name("maxIncreaseBonus")).sendKeys(" ");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
	}

	@Test
	public void uTManagerU14() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员卡管理")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("会员卡管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
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
	}

	@Test
	public void uTManagerU15() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		{
			WebElement element = driver.findElement(By.linkText("会员管理"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("修改积分")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-input")).sendKeys("100");
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTManagerU16() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.id("district")).clear();
		driver.findElement(By.id("district")).sendKeys("上海");
		driver.findElement(By.cssSelector(".updateVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".updateVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
	}

	@Test
	public void uTManagerU20() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(3) > td:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updateVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".updateVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		{
			WebElement element = driver.findElement(By.cssSelector(".updateVip"));
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
	public void uTManagerU21() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("birthday")).sendKeys(" ");
		driver.findElement(By.cssSelector(".updateVip")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".updateVip"));
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
	public void uTManagerU24() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("birthday")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("district")).sendKeys("广州");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updateVip")).click();
	}

	@Test
	public void uTManagerU25() throws InterruptedException {
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
		driver.findElement(By.id("district")).click();
		driver.findElement(By.id("district")).sendKeys(" ");
		driver.findElement(By.cssSelector(".updateVip")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".updateVip"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
	}
}
