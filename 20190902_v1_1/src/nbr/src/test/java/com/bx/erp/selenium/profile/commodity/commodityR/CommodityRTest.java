package com.bx.erp.selenium.profile.commodity.commodityR;

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

public class CommodityRTest extends BaseSeleniumTest {

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
	public void uTR10queryByCategoryAndKeyValue2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		//
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(3000);
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
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		//
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(3000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("正确商品名称");
		Thread.sleep(14000);
		vars.put("count", driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText());
		System.out.println("商品条数为：" + vars.get("count").toString());
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
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		vars.put("count",
				js.executeScript("function test(text){      var num = parseInt(text.replace(/[^0-9]/ig, \"\"));      num += 1;      text = \"共 \" + num + \" 条\";      return text;    }    return test(arguments[0]);", vars.get("count")));
		System.out.println("此时查到的商品条数应为：" + vars.get("count").toString());
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("正确商品名称");
		Thread.sleep(14000);
		assertThat(driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR11queryByCategoryAndKeyValue3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#middlePart > .topArea > .layui-icon")).click();
		Thread.sleep(2000);
		vars.put("count", driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText());
		System.out.println("商品条数为：" + vars.get("count").toString());
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
		vars.put("count",
				js.executeScript("function test(text){      var num = parseInt(text.replace(/[^0-9]/ig, \"\"));      num += 1;      text = \"共 \" + num + \" 条\";      return text;    }    return test(arguments[0]);", vars.get("count")));
		System.out.println("此时查到的商品条数应为：" + vars.get("count").toString());
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#middlePart > .topArea > .layui-icon")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR12queryByCategoryAndKeyValue4() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		Thread.sleep(5000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("5644444444444444444478977777777777777777777777777777777777788888");
	}

	@Test
	public void uTR13viewSomeoneDetails() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		vars.put("commoditySpecification", js.executeScript("return \"规格\" + parseInt(Math.random()*1000000);"));
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys(vars.get("commoditySpecification").toString());
		vars.put("commodityPriceRetail", js.executeScript("return parseFloat(Math.random()*10000).toFixed(2);"));
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(vars.get("commodityPriceRetail").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".trChoosed span")).getText(), is(vars.get("commodityName").toString()));
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-4-0-2")).getText(), is(vars.get("commoditySpecification").toString()));
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-4-0-5")).getText(), is(vars.get("commodityPriceRetail").toString()));
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("commodityName").toString()));
		}
		{
			String value = driver.findElement(By.name("specification")).getAttribute("value");
			assertThat(value, is(vars.get("commoditySpecification").toString()));
		}
		{
			String value = driver.findElement(By.name("priceRetail")).getAttribute("value");
			assertThat(value, is(vars.get("commodityPriceRetail").toString()));
		}
	}

	@Test
	public void uTR14viewSomeoneHadManyBarcode() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		vars.put("commoditySpecification", js.executeScript("return \"规格\" + parseInt(Math.random()*1000000);"));
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys(vars.get("commoditySpecification").toString());
		vars.put("commodityPriceRetail", js.executeScript("return parseFloat(Math.random()*10000).toFixed(2);"));
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(vars.get("commodityPriceRetail").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("7654321");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) .layui-input")).sendKeys("1111111");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".trChoosed span")).getText(), is(vars.get("commodityName").toString()));
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-6-0-2")).getText(), is(vars.get("commoditySpecification").toString()));
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-6-0-5")).getText(), is(vars.get("commodityPriceRetail").toString()));
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("commodityName").toString()));
		}
		{
			String value = driver.findElement(By.name("specification")).getAttribute("value");
			assertThat(value, is(vars.get("commoditySpecification").toString()));
		}
		{
			String value = driver.findElement(By.name("priceRetail")).getAttribute("value");
			assertThat(value, is(vars.get("commodityPriceRetail").toString()));
		}
		if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'otherBarcode\').length != 2)")) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void uTR15viewSomeoneHadManyProvider() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		vars.put("commoditySpecification", js.executeScript("return \"规格\" + parseInt(Math.random()*1000000);"));
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys(vars.get("commoditySpecification").toString());
		vars.put("commodityPriceRetail", js.executeScript("return parseFloat(Math.random()*10000).toFixed(2);"));
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(vars.get("commodityPriceRetail").toString());
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherProvider .layui-input")).click();
		Thread.sleep(1000);
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
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		assertThat(driver.findElement(By.cssSelector(".trChoosed span")).getText(), is(vars.get("commodityName").toString()));
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-4-0-2")).getText(), is(vars.get("commoditySpecification").toString()));
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-4-0-5")).getText(), is(vars.get("commodityPriceRetail").toString()));
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("commodityName").toString()));
		}
		{
			String value = driver.findElement(By.name("specification")).getAttribute("value");
			assertThat(value, is(vars.get("commoditySpecification").toString()));
		}
		{
			String value = driver.findElement(By.name("priceRetail")).getAttribute("value");
			assertThat(value, is(vars.get("commodityPriceRetail").toString()));
		}
		if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'otherProvider\').length != 2)")) {
		}
	}

	@Test
	public void uTR16viewSomeoneHadMultiPackage() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(8000);
		driver.findElement(By.cssSelector("#layui-laypage-2 > .layui-laypage-next > .layui-icon")).click();
		{
			WebElement element = driver.findElement(By.cssSelector("#layui-laypage-2 > .layui-laypage-next > .layui-icon"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#layui-laypage-3 > .layui-laypage-next > .layui-icon")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-laypage-next:nth-child(8) > .layui-icon")).click();
		Thread.sleep(3000);
		// driver.findElement(By.cssSelector(".layui-table-hover
		// .laytable-cell-1-0-1")).click();
		driver.findElement(By.cssSelector(".trChoosed .laytable-cell-1-0-1")).click();
	}

	@Test
	public void uTR17viewSomeoneIsCompose() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		vars.put("commoditySpecification", js.executeScript("return \"规格\" + parseInt(Math.random()*1000000);"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys(vars.get("commoditySpecification").toString());
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		vars.put("commodityName1", driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/span")).getText());
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		vars.put("commodityName2", driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[2]/div/span")).getText());
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("tbody:nth-child(3) > tr:nth-child(1) > td:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) > tbody:nth-child(3) > tr:nth-child(1) > td:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("组合商品暂时不允许修改"));
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div/div[2]/table/tbody/tr[2]/td[2]/div")).click();
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div/div[2]/table/tbody/tr[5]/td[2]/div")).click();
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div/div[2]/table/tbody/tr[7]/td[3]/div")).click();
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div/div[2]/table/tbody/tr[9]/td[3]")).click();
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div/div[2]/table/tbody/tr[10]/td[3]/div")).click();
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div/div[2]/table/tbody/tr/td[2]/div")).click();
	}

	@Test
	public void uTR1viewPageDetails() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		assertThat(driver.findElement(By.cssSelector(".trChoosed span")).getText(), is(vars.get("commodityName").toString()));
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-1-0-5")).getText(), is("66.00"));
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("commodityName").toString()));
		}
		{
			String value = driver.findElement(By.name("specification")).getAttribute("value");
			assertThat(value, is("正确的规格"));
		}
		{
			String value = driver.findElement(By.name("priceRetail")).getAttribute("value");
			assertThat(value, is("66.00"));
		}
	}

	@Test
	public void uTR21queryByStatus() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		vars.put("count1", driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(5000);
		vars.put("count2", driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(3)")).click();
		Thread.sleep(3000);
		vars.put("count3", driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText());
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
		Thread.sleep(1000);
		vars.put("count1",
				js.executeScript("function test(text){      var num = parseInt(text.replace(/[^0-9]/ig, \"\"));      num += 1;      text = \"共 \" + num + \" 条\";      return text;    }    return test(arguments[0]);", vars.get("count1")));
		System.out.println("此时查到的商品条数count1应为：" + vars.get("count1").toString());
		vars.put("count2",
				js.executeScript("function test(text){      var num = parseInt(text.replace(/[^0-9]/ig, \"\"));      num += 1;      text = \"共 \" + num + \" 条\";      return text;    }    return test(arguments[0]);", vars.get("count2")));
		System.out.println("此时查到的商品条数count2应为：" + vars.get("count2").toString());
		assertThat(driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText(), is(vars.get("count1").toString()));
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(4000);
		assertThat(driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText(), is(vars.get("count2").toString()));
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(3)")).click();
	}

	@Test
	public void uTR23showAndCloseCategory() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		vars.put("categoryLength", js.executeScript("return document.getElementById(\'leftSide\').getElementsByClassName(\'layui-nav-item\').length;"));
		driver.findElement(By.cssSelector(".showAllCommCategory:nth-child(2)")).click();
		if ((Boolean) js.executeScript("return (document.getElementById(\'leftSide\').getElementsByClassName(\'layui-nav-itemed\').length != arguments[0])", vars.get("categoryLength"))) {
		}
		driver.findElement(By.cssSelector(".showAllCommCategory:nth-child(2)")).click();
		if ((Boolean) js.executeScript("return (document.getElementById(\'leftSide\').getElementsByClassName(\'layui-nav-itemed\').length != 0)")) {
			Assert.assertTrue(false, "测试不通过");
		}
	}

	@Test
	public void uTR25queryCommodityHadSimilarBarcode() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		vars.put("commoditySpecification", js.executeScript("return \"规格\" + parseInt(Math.random()*1000000);"));
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys(vars.get("commoditySpecification").toString());
		vars.put("commodityPriceRetail", js.executeScript("return parseFloat(Math.random()*10000).toFixed(2);"));
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(vars.get("commodityPriceRetail").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("12345678");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
		Thread.sleep(2000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("12345678");
		// driver.findElement(By.name("queryKeyword")).sendKeys(Keys.chord(Keys.CONTROL,
		// "a"), "12345678");
		Thread.sleep(7000);
		assertThat(driver.findElement(By.cssSelector(".trChoosed span")).getText(), is(vars.get("commodityName").toString()));
		Thread.sleep(4000);
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-13-0-2")).getText(), is(vars.get("commoditySpecification").toString()));
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-13-0-5")).getText(), is(vars.get("commodityPriceRetail").toString()));
		Thread.sleep(3000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("commodityName").toString()));
		}
		{
			String value = driver.findElement(By.name("specification")).getAttribute("value");
			assertThat(value, is(vars.get("commoditySpecification").toString()));
		}
		{
			String value = driver.findElement(By.name("priceRetail")).getAttribute("value");
			assertThat(value, is(vars.get("commodityPriceRetail").toString()));
		}
	}

	@Test
	public void uTR30viewDifferentCommodity1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).click();
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).sendKeys("1234567");
		vars.put("serviceCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".serviceCommName")).click();
		driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(vars.get("serviceCommodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建服务类商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		vars.put("combinedCommName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("combinedCommName").toString());
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
		Thread.sleep(2000);
		{
			String value = driver.findElement(By.cssSelector(".combinedCommName")).getAttribute("value");
			assertThat(value, is(vars.get("combinedCommName").toString()));
		}
		if ((Boolean) js.executeScript("return (window.getComputedStyle(document.getElementsByClassName(\'combinedSubCommodity\')[0])[\'display\'] != \'block\')")) {
			Assert.assertTrue(false, "测试失败");
		}
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		{
			String value = driver.findElement(By.cssSelector(".serviceCommName")).getAttribute("value");
			assertThat(value, is(vars.get("serviceCommodityName").toString()));
		}
		if ((Boolean) js.executeScript("return (window.getComputedStyle(document.getElementsByClassName(\'combinedSubCommodity\')[0])[\'display\'] != \'none\')")) {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTR31viewDifferentCommodity2() throws InterruptedException {
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
		vars.put("serviceCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".serviceCommName")).click();
		driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(vars.get("serviceCommodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建服务类商品成功"));
		Thread.sleep(2000);
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
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("4437873");
		vars.put("multiCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("commodityName").toString()));
		}
		{
			String value = driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).getAttribute("value");
			assertThat(value, is(vars.get("multiCommodityName").toString()));
		}
	}

	@Test
	public void uTR32cancelButton1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(4)")).click();
		Thread.sleep(5000);
		vars.put("commodityName", driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div/div[2]/table/tbody/tr/td/div/span")).getText());
	}

	@Test
	public void uTR34queryCommodity() throws InterruptedException {
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
		vars.put("commodityName", js.executeScript("return \"正确商品名称_\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		vars.put("commoditySpecification", js.executeScript("return \"规格\" + parseInt(Math.random()*1000000);"));
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys(vars.get("commoditySpecification").toString());
		vars.put("commodityPriceRetail", js.executeScript("return parseFloat(Math.random()*10000).toFixed(2);"));
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(vars.get("commodityPriceRetail").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("_");
		Thread.sleep(4000);
		assertThat(driver.findElement(By.cssSelector("td > .laytable-cell-6-0-0 > span")).getText(), is(vars.get("commodityName").toString()));
		assertThat(driver.findElement(By.cssSelector("td > .laytable-cell-6-0-2")).getText(), is(vars.get("commoditySpecification").toString()));
		assertThat(driver.findElement(By.cssSelector("td > .laytable-cell-6-0-5")).getText(), is(vars.get("commodityPriceRetail").toString()));
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("commodityName").toString()));
		}
		{
			String value = driver.findElement(By.name("specification")).getAttribute("value");
			assertThat(value, is(vars.get("commoditySpecification").toString()));
		}
		{
			String value = driver.findElement(By.name("priceRetail")).getAttribute("value");
			assertThat(value, is(vars.get("commodityPriceRetail").toString()));
		}
	}

	@Test
	public void uTR35queryCommodity() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.name("pwdEncrypted")).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("ol")).click();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("14725896321");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("克");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("10.00");
		driver.findElement(By.name("name")).click();
		vars.put("commodity", js.executeScript("return \"商品_1\"+Math.round(Math.random()*77777777)"));
		driver.findElement(By.name("name")).sendKeys(vars.get("commodity").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("_");
	}

	@Test
	public void uTR4queryByCategory() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(4000);
		vars.put("count", driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		System.out.println("商品条数为：" + vars.get("count").toString());
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
		vars.put("count",
				js.executeScript("function test(text){      var num = parseInt(text.replace(/[^0-9]/ig, \"\"));      num += 1;      text = \"共 \" + num + \" 条\";      return text;    }    return test(arguments[0]);", vars.get("count")));
		System.out.println("此时查到的商品条数应为：" + vars.get("count").toString());
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(4000);
		assertThat(driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR5queryByKeyValue1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("#");
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("！你好");
	}

	@Test
	public void uTR6queryByKeyValue2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("正确商品名称");
		Thread.sleep(14000);
		vars.put("count", driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText());
		System.out.println("商品条数为：" + vars.get("count").toString());
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
		Thread.sleep(1000);
		vars.put("count",
				js.executeScript("function test(text){      var num = parseInt(text.replace(/[^0-9]/ig, \"\"));      num += 1;      text = \"共 \" + num + \" 条\";      return text;    }    return test(arguments[0]);", vars.get("count")));
		System.out.println("此时查到的商品条数应为：" + vars.get("count").toString());
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("正确商品名称");
	}

	@Test
	public void uTR7queryByKeyValue3() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		Thread.sleep(5000);
		vars.put("count", driver.findElement(By.cssSelector("#commodityList + div .layui-laypage-count")).getText());
		System.out.println("商品条数为：" + vars.get("count").toString());
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
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
	}

	@Test
	public void uTR8queryByKeyValue4() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("3234654897879879879789798797978979878979777777777777744444444444");
	}

	@Test
	public void uTR9queryByCategoryAndKeyValue1() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(4000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("￥");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，支持输入的符号有：（）()_-——"));
	}
}
