package com.bx.erp.selenium.purchase.order;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.selenium.BaseSeleniumTest;

@WebAppConfiguration
public class PurchaseOrder_CTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTC1createPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(8000);
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC2createPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(8000);
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".closeLayerPage")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请添加商品"));
	}

	@Test
	public void uTC3createPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(8000);
		driver.findElement(By.cssSelector("li:nth-child(3) .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(4) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC4createPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(8000);
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("采购订单列表")).click();
	}

	@Test
	public void uTC5createPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(8000);
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("#layui-layer14 > .layui-layer-content")).getText(), is("审核成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.switchTo().defaultContent();
	}

	@Test
	public void uTC6createPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("#layui-layer14 > .layui-layer-content")).getText(), is("审核成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(4) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC8PreSalecreatePurchaseOrder() throws InterruptedException {
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
		{
			WebElement element = driver.findElement(By.cssSelector(".tabColor"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".tabColor")).click();
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		driver.findElement(By.cssSelector(".tabColor")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".tabColor"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTC9createPurchaseOrder() throws InterruptedException {
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
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[2]/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td[2]/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[10]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[9]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[8]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
	}

	@Test
	public void uTC10createPurchaseOrder() throws InterruptedException {
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
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) td:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("15")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[2]/ul/li[2]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
	}

	@Test
	public void uTC11createPurchaseOrder() throws InterruptedException {
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
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).sendKeys("服务");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
	}

	@Test
	public void uTC12createPurchaseOrder() throws InterruptedException {
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
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).sendKeys("multiPackagCommBarcodesB");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
	}

	@Test
	public void uTC13createPurchaseOrder() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(8000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("searchcommodity")).sendKeys("组合商品");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
	}

	@Test
	public void uTC14createPurchaseOrder() throws InterruptedException {
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.cssSelector(".layui-icon-chart-screen")).click();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(8000);
		{
			WebElement element = driver.findElement(By.linkText("商品列表"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("barcodes")).sendKeys("123465789");
		Thread.sleep(2000);
		driver.findElement(By.name("specification")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("specification")).sendKeys("毫升");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(10) > .layui-inline:nth-child(1) > .layui-input-inline")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(2000);
		vars.put("commodity", js.executeScript("return \"商品\"+Math.round(Math.random()*77777777)"));
		Thread.sleep(2000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodity").toString());
		Thread.sleep(2000);
		driver.findElement(By.name("priceRetail")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("priceRetail")).sendKeys("50.00");
		Thread.sleep(2000);
		driver.findElement(By.name("nOStart")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("nOStart")).sendKeys("20");
		Thread.sleep(2000);
		driver.findElement(By.name("purchasingPriceStart")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("purchasingPriceStart")).sendKeys("1");
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
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("NO")).sendKeys("10");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("price")).sendKeys("0.50");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'purchasingOrderMain\']/div[2]/ul/li[2]/button")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
	}

	@Test
	public void uTC15createPurchaseOrder() throws InterruptedException {
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
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) .option-status")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[2]/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("采购订单修改成功"));
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[10]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[9]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[8]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[7]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建采购订单成功"));
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("providerinput")).sendKeys("45555322");
		Thread.sleep(2000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("采购订单修改成功"));
		driver.findElement(By.cssSelector("li:nth-child(2) > .list_option")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:nth-child(1) .option-status")).click();
	}

	@Test
	public void uTC16createPurchaseOrder() throws InterruptedException {
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
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(2000);
		{
			WebElement element = driver.findElement(By.linkText("全部商品"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover > .layui-table-col-special > .layui-table-cell")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover > .layui-table-col-special > .layui-table-cell")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".search-box")).sendKeys("CG3434345345");
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
	}
	
	 @Test
	  public void uTC17createPurchaseOrder() throws InterruptedException {
	    driver.findElement(By.linkText("采购订单列表")).click();
	    Thread.sleep(8000);
	    driver.switchTo().frame(1);
	    driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".addGeneralComm")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.linkText("确定")).click();
	  }
	 
	  @Test
	  public void uTC18createPurchaseOrder() throws InterruptedException {
	    driver.findElement(By.linkText("采购订单列表")).click();
	    Thread.sleep(2000);
	    driver.switchTo().frame(1);
	    driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector("li:nth-child(4) > .layui-btn")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.linkText("确定")).click();
	  }
}
