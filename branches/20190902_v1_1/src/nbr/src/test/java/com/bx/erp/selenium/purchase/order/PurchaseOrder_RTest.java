package com.bx.erp.selenium.purchase.order;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class PurchaseOrder_RTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTR1retrievePurchaseOrder() {
		driver.findElement(By.linkText("采购订单列表")).click();
	}

	@Test
	public void uTR2retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div/label/span/lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(4)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(5)")).click();
	}

	@Test
	public void uTR3retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div[2]/ul/li[2]")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(4)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(1)")).click();
	}

	@Test
	public void uTR4retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) > .layui-form-label")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) li:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) li:nth-child(1)")).click();
	}

	@Test
	public void uTR5retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(4) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(4) li:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(4) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(4) li:nth-child(3)")).click();
	}

	@Test
	public void uTR6retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(5) lable")).click();
	}

	@Test
	public void uTR7retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("可乐");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("默认供应商");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("默认供应商");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("默认供应商");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".td_search")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("1");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTR8retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTR9retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys(" ");
	}

	@Test
	public void uTR10retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div/ul/li[2]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div[2]/label/span/lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div[2]/ul/li[3]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div[2]/label/span/lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div[2]/ul/li[4]")).click();
	}

	@Test
	public void uTR11retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".top > .layui-form-item")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) > .layui-form-label > span")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) li:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(11)")).click();
	}

	@Test
	public void uTR12retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(4) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(4) li:nth-child(2)")).click();
	}

	@Test
	public void uTR13retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) span")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div/ul/li[2]")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("1");
		Thread.sleep(2000);
		driver.findElement(By.id("purchasingOrderMain")).click();
	}

	@Test
	public void uTR14retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("可乐");
	}

	@Test
	public void uTR15retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div/ul/li[2]")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("默认供应商");
	}

	@Test
	public void uTR16retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(6) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(4) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(8) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
	}

	@Test
	public void uTR17retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("2")).click();
	}

	@Test
	public void uTR18retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div/div/div/ul/li[3]")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("2")).click();
	}

	@Test
	public void uTR19retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("默认供应商");
		Thread.sleep(2000);
		driver.findElement(By.linkText("2")).click();
	}

	@Test
	public void uTR20retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("#chooseProviderWindow tbody")).click();
	}

	@Test
	public void uTR21retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(3) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("四川")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("安徽")).click();
	}

	@Test
	public void uTR22retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("#chooseProviderWindow > .leftRegion > .layui-btn")).click();
	}

	@Test
	public void uTR23retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("providerinput")).sendKeys("默认供应商");
	}

	@Test
	public void uTR24retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("providerinput")).sendKeys("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
	}

	@Test
	public void uTR25retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("providerinput")).sendKeys(" ");
	}

	@Test
	public void uTR26retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
	}

	@Test
	public void uTR27retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(3) .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
	}

	@Test
	public void uTR28retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[3]/div/ul/li[3]/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[3]/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("cancel")).click();
	}

	@Test
	public void uTR29retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
	}

	@Test
	public void uTR30retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//a[contains(text(),\'2\')])[3]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
	}

	@Test
	public void uTR31retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
	}

	@Test
	public void uTR32retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
	}

	@Test
	public void uTR33retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) td:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("#allCommodity > .leftRegion > .layui-btn")).click();
	}

	@Test
	public void uTR34retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).sendKeys("可乐");
	}

	@Test
	public void uTR35retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).sendKeys("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
	}

	@Test
	public void uTR36retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) td:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).sendKeys(" ");
	}

	@Test
	public void uTR37retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
	}

	@Test
	public void uTR38retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
	}

	@Test
	public void uTR39retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".closeLayerPage")).click();
	}

	@Test
	public void uTR40retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
	}

	@Test
	public void uTR41retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".click > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(8) > .layui-input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(8) > .layui-input")).sendKeys("3");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(8) > .layui-laypage-btn")).click();
	}

	@Test
	public void uTR42retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		{
			WebElement element = driver.findElement(By.linkText("采购订单列表"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("CG20200116");
	}

	@Test
	public void uTR43retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		{
			WebElement element = driver.findElement(By.linkText("采购订单列表"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("CG2020011");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，采购订单号至少需要输入10位"));
	}

	@Test
	public void uTR44retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		{
			WebElement element = driver.findElement(By.linkText("采购订单列表"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("CG202001160022");
	}

	@Test
	public void uTR45retrievePurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("CG2020011600");
	}
	
	  @Test
	  public void uTR46PresaleretrievePurchaseOrder() throws InterruptedException {
//	    PresaleLoginSucceed();
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	    driver.switchTo().defaultContent();
	    driver.findElement(By.linkText("采购订单列表")).click();
	    Thread.sleep(8000);
	    driver.switchTo().frame(1);
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	    driver.close();
	  }
	  
	  @Test
	  public void uTR47retrievePurchaseOrder() throws InterruptedException {
	    driver.findElement(By.cssSelector("ol > li:nth-child(1)")).click();
	    driver.findElement(By.cssSelector(".layui-icon-app")).click();
	    driver.findElement(By.linkText("商品列表")).click();
	    Thread.sleep(8000);
	    driver.switchTo().frame(1);
	    driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.name("barcodes")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.name("barcodes")).sendKeys("789456155266");
	    Thread.sleep(2000);
	    driver.findElement(By.name("specification")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.name("specification")).sendKeys("克");
	    Thread.sleep(2000);
	    driver.findElement(By.name("priceRetail")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.name("priceRetail")).sendKeys("10.00");
	    Thread.sleep(2000);
	    driver.findElement(By.name("name")).click();
	    Thread.sleep(2000);
	    vars.put("commodity", js.executeScript("return \"商品1_1\"+Math.round(Math.random()*77777777)"));
	    Thread.sleep(2000);
	    driver.findElement(By.name("name")).sendKeys(vars.get("commodity").toString());
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".btnChoosed")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.linkText("确定")).click();
	    Thread.sleep(2000);
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
	    Thread.sleep(2000);
	    driver.switchTo().defaultContent();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.linkText("采购")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.linkText("采购订单列表")).click();
	    Thread.sleep(2000);
	    driver.switchTo().frame(2);
	    driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".addGeneralComm")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.linkText("确定")).click();
	    Thread.sleep(2000);
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector("html")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".search-box")).sendKeys("_");
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".td_icon")).click();
	  }
	  
	  @Test
	  public void uTR48searchByPhone() throws InterruptedException {
	    driver.findElement(By.linkText("商品相关")).click();
	    Thread.sleep(8000);
	    driver.switchTo().frame(1);
	    driver.findElement(By.cssSelector(".provideSelect > .add")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.id("name")).click();
	    Thread.sleep(2000);
	    vars.put("provider", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
	    Thread.sleep(2000);
	    driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
	    Thread.sleep(2000);
	    driver.findElement(By.name("mobile")).click();
	    Thread.sleep(2000);
	    vars.put("phone", js.executeScript("return \"258258\"+Math.round(Math.random()*999999)"));
	    Thread.sleep(2000);
	    driver.findElement(By.name("mobile")).sendKeys(vars.get("phone").toString());
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.linkText("确定")).click();
	    Thread.sleep(2000);
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商成功"));
	    Thread.sleep(2000);
	    driver.switchTo().defaultContent();
	    driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.linkText("库管")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.linkText("采购")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.linkText("采购订单列表")).click();
	    Thread.sleep(2000);
	    driver.switchTo().frame(1);
	    driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".addProvider")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.id("providerinput")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.id("providerinput")).sendKeys("258258");
	    Thread.sleep(2000);
	    driver.findElement(By.id("providerinput")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.id("providerinput")).sendKeys("13129355446");
	  }
	  
	  @Test
	  public void uTR49retrievePurchaseOrder() throws InterruptedException {
	    driver.findElement(By.linkText("采购订单列表")).click();
	    Thread.sleep(8000);
	    driver.switchTo().frame(1);
	    driver.findElement(By.linkText("2")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".click > .list_option")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector("li:nth-child(2) .provider-name")).click();
	  }
}
