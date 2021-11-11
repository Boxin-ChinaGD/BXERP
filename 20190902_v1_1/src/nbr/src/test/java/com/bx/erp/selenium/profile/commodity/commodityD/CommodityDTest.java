package com.bx.erp.selenium.profile.commodity.commodityD;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CommodityDTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

	@Test
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
		Thread.sleep(1000);
	}

	@Test
	public void uTD10deleteCommodityBarcode2() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
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
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("1111111");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) .layui-input")).sendKeys("22222222");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-input")).sendKeys("333333333");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
		driver.findElement(By.cssSelector(".toDeleteBarcode")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("条形码已删除"));
		driver.findElement(By.cssSelector(".toDeleteBarcode")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toDeleteBarcode")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toDeleteBarcode")).click();
	}

	@Test
	public void uTD12deleteCommodity9() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".serviceCommName")).click();
		driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建服务类商品成功"));
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTD13D14() throws InterruptedException {
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
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(2000);
		// driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		// Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".toDeleteBarcode")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".toDeleteBarcode"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("123456");
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item > .layui-icon"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".otherBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).click();
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("1234567");
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-form-item > .layui-icon"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.cssSelector(".otherBarcode"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).clickAndHold().perform();
		}
		// Thread.sleep(3000);
		// {
		// WebElement element = driver.findElement(By.id("layui-layer-shade12"));
		// Actions builder = (Actions) new Actions(driver);
		// ((Actions) builder).moveToElement(element).release().perform();
		// }
		driver.findElement(By.cssSelector("body")).click();
	}

	@Test
	public void uTD15deleteCommodity10() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".serviceCommName")).click();
		driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys("66");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.switchTo().defaultContent();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		Thread.sleep(1000);
		// driver.findElement(By.linkText("员工资料")).click();
		// Thread.sleep(1000);
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("aaa");
		vars.put("phone", js.executeScript("return \"1111111\" + parseInt(Math.random()*10000);"));
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.switchTo().defaultContent();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(4000);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(3000);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("111111");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("111111");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
	}

	@Test
	public void uTD16deleteCommodityProvider1() throws InterruptedException {
		LoginSucceed();
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
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("商品至少需要有一个供应商\n不支持删除全部的供应商"));
	}

	@Test
	public void uTD17deleteCommodityProvider2() throws InterruptedException {
		LoginSucceed();
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
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(1000);
		if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'otherProvider\').length != 1)")) {
		}
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'otherProvider\').length != 0)")) {
		}
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("商品至少需要有一个供应商\n不支持删除全部的供应商"));
	}

	@Test
	public void uTD19deleteCommodity11() throws InterruptedException {
		LoginSucceed();
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
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		Thread.sleep(1000);
		// driver.findElement(By.linkText("促销")).click();
		// Thread.sleep(1000);
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.linkText("满减优惠"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(2);
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("满减商品");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-y")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-prev-y")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(5) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		driver.findElement(By.name("excecutionThreshold")).sendKeys("100");
		driver.findElement(By.name("excecutionAmount")).click();
		driver.findElement(By.name("excecutionAmount")).sendKeys("10");
		driver.findElement(By.cssSelector(".designatedCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		// Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".confirmChoosedComm"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.switchTo().defaultContent();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".commodityManage:nth-child(3)"));
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
	public void uTD1deleteCommodity1() throws InterruptedException {
		LoginSucceed();
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
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTD22deleteCommodity() throws InterruptedException {
		LoginSucceed();
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
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		Thread.sleep(1000);
		// driver.findElement(By.linkText("促销")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		driver.switchTo().frame(2);
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("其味无穷");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(5) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		driver.findElement(By.name("excecutionThreshold")).sendKeys("111");
		driver.findElement(By.name("excecutionAmount")).click();
		driver.findElement(By.name("excecutionAmount")).sendKeys("11");
		driver.findElement(By.cssSelector(".designatedCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".confirmChoosedComm"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".updatePromotion"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTD2deleteCommodity2() throws InterruptedException {
		LoginSucceed();
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
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.switchTo().defaultContent();
		Thread.sleep(3000);
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
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys(vars.get("commodityName").toString());
		Thread.sleep(8000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTD3deleteCommodity3() throws InterruptedException {
		LoginSucceed();
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
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("克");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTD4deleteCommodity4() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
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
		driver.findElement(By.name("nOStart")).click();
		driver.findElement(By.name("nOStart")).sendKeys("1");
		driver.findElement(By.name("purchasingPriceStart")).click();
		driver.findElement(By.name("purchasingPriceStart")).sendKeys("1");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTD6deleteCommodity6() throws InterruptedException {
		LoginSucceed();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
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
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTD8deleteCommodity8() throws InterruptedException {
		LoginSucceed();
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
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiCommodityName").toString());
		// driver.findElement(By.cssSelector(".btnChoosed")).click();
		// Thread.sleep(3000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".baseBarcode")).sendKeys("12354645");
		{
			WebElement element = driver.findElement(By.cssSelector(".btnChoosed"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTD9deleteCommodityBarcode1() throws InterruptedException {
		LoginSucceed();
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
		driver.findElement(By.cssSelector(".toDeleteBarcode")).click();
	}
}
