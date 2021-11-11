package com.bx.erp.selenium;

import org.testng.annotations.Test;
import com.bx.erp.selenium.BaseSeleniumTest;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CompanyIndex_RTest extends BaseSeleniumTest {
	@Test
	public void testCase1JumpToShopPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		assertThat(driver.getTitle(), is("BoXin-CMS"));
	}

	@Test
	public void testCase2JumpToCachePage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("更新缓存")).click();
		driver.switchTo().frame(1);
		assertThat(driver.getTitle(), is("BoXin-CMS"));
	}

	@Test
	public void testCase3ShrinkSideNav() throws InterruptedException {
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".BX_title")));
		}
		{
			WebElement element = driver.findElement(By.cssSelector(".shrinkNavDiv"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".layui-icon-shrink-right")).click();
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".BX_title")));
		}
	}

	@Test
	public void testCase4ShowSideNav() throws InterruptedException {
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".BX_title")));
		}
		{
			WebElement element = driver.findElement(By.cssSelector(".shrinkNavDiv"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
			Thread.sleep(2000);
		}
		driver.findElement(By.cssSelector(".layui-icon-shrink-right")).click();
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".BX_title")));
		}
		driver.findElement(By.cssSelector(".layui-icon-spread-left")).click();
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".BX_title")));
		}
	}

	@Test
	public void testCase5LogoutAndLoginAgain() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);

		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		
		Thread.sleep(2000);
		logout();
		
		Thread.sleep(5000);
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).sendKeys("13185246281");
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		{
			Thread.sleep(2000);
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".BX_title")));
		}
	}
}
