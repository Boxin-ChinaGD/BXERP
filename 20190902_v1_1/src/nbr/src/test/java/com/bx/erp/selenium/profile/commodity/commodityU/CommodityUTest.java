package com.bx.erp.selenium.profile.commodity.commodityU;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CommodityUTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

	public void LoginSucceed() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
	}

	@Test
	public void uTcommodityU17deleteProvider2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherProvider .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherProvider:nth-child(16) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		vars.put("multiCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		Thread.sleep(1000);
		if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'otherProvider\').length != 0)")) {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTcommodityU18updateMultiPackageInfo1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		vars.put("multiCommodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName1").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		vars.put("multiCommodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("multiCommodityName2").toString());
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "7654321");
		driver.findElement(By.cssSelector(".multiPackagePriceRetail > td:nth-child(3) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackagePriceRetail > td:nth-child(3) > .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "555");
		Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".multiPackagePriceVIP > td:nth-child(3) >
		// .layui-input")).click();
		// driver.findElement(By.cssSelector(".multiPackagePriceVIP > td:nth-child(3) >
		// .layui-input")).sendKeys("666");
		// driver.findElement(By.cssSelector(".multiPackagePriceWholesale >
		// td:nth-child(3) > .layui-input")).click();
		// driver.findElement(By.cssSelector(".multiPackagePriceWholesale >
		// td:nth-child(3) > .layui-input")).sendKeys("777");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		{
			String value = driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).getAttribute("value");
			assertThat(value, is(vars.get("multiCommodityName2").toString()));
		}
	}

	@Test
	public void uTcommodityU20createMultiPackage1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("12345678");
		vars.put("multiCommodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName1").toString());
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).sendKeys("12");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).sendKeys("123456789");
		vars.put("multiCommodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).sendKeys(vars.get("multiCommodityName2").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
	}

	@Test
	public void uTcommodityU21createMultiPackage2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.switchTo().defaultContent();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("库管")).click();
		driver.findElement(By.linkText("盘点")).click();
		driver.switchTo().frame(2);
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建盘点单成功"));
		Thread.sleep(2000);
		driver.switchTo().defaultContent();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("12345678");
		vars.put("multiCommodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName1").toString());
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).sendKeys("12");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).sendKeys("123456789");
		vars.put("multiCommodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).sendKeys(vars.get("multiCommodityName2").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
	}

	@Test
	public void uTcommodityU22deleteMultiPackage1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("12345678");
		vars.put("multiCommodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName1").toString());
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).sendKeys("12");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).sendKeys("123456789");
		vars.put("multiCommodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).sendKeys(vars.get("multiCommodityName2").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		{
			WebElement element = driver.findElement(By.cssSelector(".multiPackage div:nth-child(2)"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(2) > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
	}

	@Test
	public void uTcommodityU26updateCommodity1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建组合商品成功"));
	}

	@Test
	public void uTcommodityU27updateCommodity2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("12345678");
		vars.put("multiCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("commodityName1").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
	}

	@Test
	public void uTcommodityU28updateCommodity3() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).click();
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).sendKeys("1234567");
		vars.put("commodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".serviceCommName")).click();
		driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys("20");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建服务类商品成功"));
		Thread.sleep(2000);
		vars.put("commodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".serviceCommodityInfo > .layui-form-item:nth-child(14) > .layui-inline:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("commodityName2").toString());
		vars.put("commoditySpecification", js.executeScript("return \"规格\" + parseInt(Math.random()*10000);"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) > .layui-input-inline")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("commoditySpecification").toString());
		vars.put("commodityPriceRetail", js.executeScript("return parseFloat(Math.random()*10000).toFixed(2);"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "20");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".btnChoosed")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改服务类商品成功"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".trChoosed .laytable-cell-5-0-0")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".trChoosed span")).getText(), is(vars.get("commodityName2").toString()));
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-5-0-2")).getText(), is(vars.get("commoditySpecification").toString()));
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-5-0-5")).getText(), is("20.00"));
	}

	@Test
	public void uTcommodityU29updateCommodity4() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("13888888888");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.name("pwdEncrypted")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		{
			WebElement element = driver.findElement(By.linkText("商品列表"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".serviceCommodityInfo")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "11");
		// Thread.sleep(5000);
		// driver.findElement(By.cssSelector("#middlePart > .layui-form >
		// .layui-table-page")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".btnChoosed"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTcommodityU34updateCommodity5() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(10)")).click();
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		driver.findElement(By.cssSelector(".otherProvider .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		driver.findElement(By.cssSelector(".otherProvider:nth-child(16) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("商品至少需要有一个供应商\n不支持删除全部的供应商"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
	}

	@Test
	public void uTcommodityU41U43updateCommodity6() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("12345678");
		vars.put("multiCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "20.00");
		driver.findElement(By.cssSelector(".commodityInfo")).click();
		{
			String value = driver.findElement(By.cssSelector(".multiPackagePriceRetail > td:nth-child(3) > .layui-input")).getAttribute("value");
			assertThat(value, is("220.00"));
		}
		// driver.findElement(By.name("priceVIP")).click();
		// driver.findElement(By.name("priceVIP")).sendKeys("30.00");
		// driver.findElement(By.cssSelector(".commodityInfo")).click();
		// {
		// String value = driver.findElement(By.cssSelector(".multiPackagePriceVIP >
		// td:nth-child(3) > .layui-input")).getAttribute("value");
		// assertThat(value, is("330.00"));
		// }
		// driver.findElement(By.cssSelector(".multiPackagePriceWholesale >
		// td:nth-child(3) > .layui-input")).click();
		// driver.findElement(By.name("priceWholesale")).sendKeys("50.00");
		// driver.findElement(By.cssSelector(".commodityInfo")).click();
		// {
		// String value = driver.findElement(By.cssSelector(".multiPackagePriceWholesale
		// > td:nth-child(3) > .layui-input")).getAttribute("value");
		// assertThat(value, is("550.00"));
		// }
	}

	@Test
	public void uTcommodityU46updateCommodity7() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.name("startValueRemark")).click();
		driver.findElement(By.name("startValueRemark")).sendKeys("123");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.name("startValueRemark")).click();
		driver.findElement(By.name("startValueRemark")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "321");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		{
			String value = driver.findElement(By.name("startValueRemark")).getAttribute("value");
			assertThat(value, is("321"));
		}
	}

	@Test
	public void uTcommodityU71checkTip() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		vars.put("commodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#middlePart > .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#leftSide .layui-this > a")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU10updateName2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		vars.put("multiCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
	}

	@Test
	public void uTU11updateName3() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		vars.put("multiCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName").toString());
		// driver.findElement(By.cssSelector(".btnChoosed")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("multiCommodityName").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU12updateProvider1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		Thread.sleep(1000);
		driver.findElement(By.name("mnemonicCode")).click();
		// driver.findElement(By.cssSelector(".layui-form-item:nth-child(14)
		// .layui-unselect .layui-input")).click();
		// driver.findElement(By.cssSelector(".layui-form-item:nth-child(14)
		// .layui-unselect .layui-edge")).click();
		// driver.findElement(By.xpath("//div[@id='rightSide']/div[2]/div[2]/div[6]/div/div/div/div/i")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) .layui-unselect .layui-input")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) .layui-unselect .layui-input")).click();
		vars.put("provider", driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).getText());
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		{
			String value = driver.findElement(By.xpath("//div[@id=\'rightSide\']/div[2]/div[2]/div[6]/div/div/div/div/input")).getAttribute("value");
			assertThat(value, is(vars.get("provider").toString()));
		}
	}

	@Test
	public void uTU13updateProvider2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.name("mnemonicCode")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		vars.put("multiCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) .layui-unselect .layui-input")).click();
		vars.put("provider", driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).getText());
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'rightSide\']/div[2]/div[2]/div[6]/div/div/div/div/input")).getAttribute("value");
			assertThat(value, is(vars.get("provider").toString()));
		}
	}

	@Test
	public void uTU14createProvider1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherProvider .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherProvider:nth-child(16) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
	}

	@Test
	public void uTU15createProvider2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		vars.put("multiCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".btnChoosed"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(10) > .layui-inline:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("汇源鲜果饮51853");
		driver.findElement(By.cssSelector(".topNav > .layui-form-item")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".btnChoosed"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU16deleteProvider1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherProvider .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherProvider:nth-child(16) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
	}

	@Test
	public void uTU1updateBarcode1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称_\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("7654321");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
		Thread.sleep(2000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "7654321");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("商品条码已存在，请重新修改"));
	}

	@Test
	public void uTU2updateBarcode2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称_\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).click();
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("7654321");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改条形码成功"));
	}

	@Test
	public void uTU3updateBarcode3() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.switchTo().defaultContent();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("库管")).click();
		driver.findElement(By.linkText("入库")).click();
		driver.switchTo().frame(2);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		Thread.sleep(2000);
		driver.switchTo().defaultContent();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改的条形码在入库单商品中有依赖"));
	}

	@Test
	public void uTU4createBarcode1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).click();
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("7654321");
		driver.findElement(By.cssSelector("body")).click();
	}

	@Test
	public void uTU5createBarcode2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).click();
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("1234567");
		driver.findElement(By.cssSelector("body")).click();
	}

	@Test
	public void uTU6createBarcode3() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		vars.put("barcode", js.executeScript("return parseInt(Math.random()*1000000000);"));
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys(vars.get("barcode").toString());
		vars.put("commodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys(vars.get("barcode").toString());
		driver.findElement(By.cssSelector("body")).click();
	}

	@Test
	public void uTU7deleteBarcode1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("commodityMain")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("12345678");
		driver.findElement(By.cssSelector("body")).click();
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) .layui-input")).sendKeys("123456789");
		driver.findElement(By.cssSelector("body")).click();
		driver.findElement(By.cssSelector(".toDeleteBarcode")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("条形码已删除"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toDeleteBarcode")).click();
	}

	@Test
	public void uTU8deleteBarcode2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("12345678");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) .layui-input")).sendKeys("123456789");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
		Thread.sleep(2000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("库管")).click();
		driver.findElement(By.linkText("盘点")).click();
		driver.switchTo().frame(2);
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.switchTo().defaultContent();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".toDeleteBarcode")).click();
	}

	@Test
	public void uTU9updateName1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
	}
}
