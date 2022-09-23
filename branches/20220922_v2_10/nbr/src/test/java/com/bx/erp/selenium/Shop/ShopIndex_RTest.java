package com.bx.erp.selenium.Shop;

import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.hamcrest.CoreMatchers.is;
import org.testng.annotations.Test;
import com.bx.erp.selenium.BaseSeleniumTest;

public class ShopIndex_RTest extends BaseSeleniumTest {

	@Test
	public void testCase1CompanyR19showCompanyBrandName() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		vars.put("brandName", driver.findElement(By.name("brandName")).getAttribute("value"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(2) > .storeArea")).click();
		driver.findElement(By.cssSelector("li:nth-child(1) > .storeArea")).click();
		{
			String value = driver.findElement(By.name("brandName")).getAttribute("value");
			assertThat(value, is(vars.get("brandName").toString()));
		}
	}

	@Test
	public void testCase2ViewShopDetail() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(2) > .storeArea")).click();
		vars.put("shopName", driver.findElement(By.cssSelector(".choosed label")).getText());
		vars.put("text", js.executeScript("var text = document.getElementsByClassName(\"choosed\")[0].getElementsByClassName(\"storeArea\")[0].innerHTML.replace(/<.*?>/g,\"\"); return text;"));
		System.out.println("保存的文本：vars.get('text').toString()");
		vars.put("companyName", js.executeScript("var companyName = arguments[0].split(\"：\")[1].split(\"有效期\")[0]; return companyName;", vars.get("text")));
		System.out.println(vars.get("companyName").toString());
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("companyName").toString()));
		}
		{
			String value = driver.findElement(By.cssSelector(".shopName > input")).getAttribute("value");
			assertThat(value, is(vars.get("shopName").toString()));
		}
	}

	@Test
	public void testCase3CancelWhenCreate() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(2) > .storeArea")).click();
		vars.put("shopName", driver.findElement(By.cssSelector(".choosed label")).getText());
		{
			String value = driver.findElement(By.cssSelector(".shopName > input")).getAttribute("value");
			assertThat(value, is(vars.get("shopName").toString()));
		}
		vars.put("firstShopName", driver.findElement(By.cssSelector("li:nth-child(1) label")).getText());
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.cssSelector(".cancel")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".choosed label")).getText(), is(vars.get("firstShopName").toString()));
		{
			String value = driver.findElement(By.cssSelector(".shopName > input")).getAttribute("value");
			assertThat(value, is(vars.get("firstShopName").toString()));
		}
	}

	@Test
	public void testCase4ShowCompanyPermit() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		vars.put("companyPermit", js.executeScript("var companyPermit = document.getElementsByClassName(\"companyPermit\")[0].getElementsByTagName(\"img\")[0].getAttribute(\"src\"); return companyPermit;"));
		if ((Boolean) js.executeScript("return (arguments[0].length > 10)", vars.get("companyPermit"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void testCase5ShowShopList() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".shopList")));
		}
	}
}
