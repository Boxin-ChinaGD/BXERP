package com.bx.erp.selenium.Shop;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class ShopIndex_DTest extends BaseSeleniumTest {

	@Test
	public void testCase1DeletePOS1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:last-child > .storeArea")).click();
		Thread.sleep(2000);
		js.executeScript("document.getElementsByClassName(\"deletePos\")[0].style.display='block';");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon-close-fill")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("pos机删除成功"));
		driver.findElement(By.cssSelector(".toCreatePos")).click();
		driver.findElement(By.name("pos_SN")).click();
		driver.findElement(By.name("pos_SN")).sendKeys("T202194740029");
		driver.findElement(By.name("passwordInPOS")).click();
		driver.findElement(By.name("passwordInPOS")).sendKeys("000000");
		driver.findElement(By.cssSelector(".posBtnSubmit > .layui-btn:nth-child(1)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("添加pos机成功"));
	}

	@Test
	public void testCase2DeletePOS2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:last-child > .storeArea")).click();
		Thread.sleep(2000);
		js.executeScript("document.getElementsByClassName(\"deletePos\")[4].style.display='block';");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(5) .layui-icon")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("该POS机有零售单依赖，不能删除"));
	}
}
