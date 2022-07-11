package com.bx.erp.selenium.warehousing.returnPurchasingCommodity;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class returnPurchasingCommodity_DTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTD1deleteCommodityFromSheet() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		{
			WebElement element = driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector("tr:nth-child(1) > td > .layui-icon")).click();
		{
			WebElement element = driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".layui-icon-close-fill")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请添加商品"));
	}
}
