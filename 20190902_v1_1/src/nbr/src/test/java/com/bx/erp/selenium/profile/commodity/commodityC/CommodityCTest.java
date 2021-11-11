package com.bx.erp.selenium.profile.commodity.commodityC;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

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

public class CommodityCTest extends BaseSeleniumTest {

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
	public void uTC10checkPriceRetail1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "1234567");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "正确的规格");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "-123");
		driver.findElement(By.cssSelector(".commodityInfo")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("格式错误，请输入非负数字，允许有两位小数"));
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入非负数字，最多带2位小数"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) > .layui-inline:nth-child(2)")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "###");
		driver.findElement(By.cssSelector(".commodityInfo")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("格式错误，请输入非负数字，允许有两位小数"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) > .layui-inline:nth-child(2)")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "8888888888888");
		driver.findElement(By.cssSelector(".commodityPicture")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("格式错误，请输入非负数字，允许有两位小数"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) > .layui-inline:nth-child(2)")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "123.456789");
		driver.findElement(By.id("middlePart")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("格式错误，请输入非负数字，允许有两位小数"));
	}

	@Test
	public void uTC112checkTip() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(4)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#middlePart > .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("文体用品")).click();
		driver.findElement(By.linkText("办公用品")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id='middlePart']/div[2]/div/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'trChoosed\').length >= 1)")) {
			System.out.println("测试通过");
		} else {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTC11checkPriceRetail2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "1234567");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("commodityName").toString());
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "66");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is(not("请输入非负数字，最多带2位小数")));
	}

	@Test
	public void uTC12checkCategory1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "66");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-unselect .layui-input")).click();
		// click 一次下拉框没出来，需要再 click 一次
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-unselect .layui-input")).click();
		// driver.findElement(By.cssSelector(".layui-form-item:nth-child(12)
		// .layui-unselect .layui-edge")).click();
		// driver.findElement(By.xpath("xpath=//div[@id='rightSide']/div[2]/div[2]/div[4]/div/div/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected .layui-select-tips")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC13checkCategory2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(61)")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认新建商品\"" + vars.get("commodityName").toString() + "\"吗?"));
	}

	// 会员价前端已经注释了
	@Test
	public void uTC14checkPriceVIP1() throws InterruptedException {
		// LoginSucceed();
		// Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".layui-icon-app")).click();
		//// driver.findElement(By.linkText("商品资料")).click();
		// driver.findElement(By.linkText("商品列表")).click();
		// driver.switchTo().frame(1);
		// Thread.sleep(5000);
		// driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		// driver.findElement(By.name("barcodes")).click();
		// driver.findElement(By.name("barcodes")).sendKeys("1234567");
		// vars.put("commodityName", js.executeScript("return \"正确商品名称\" +
		// parseInt(Math.random()*100000);"));
		// driver.findElement(By.name("name")).click();
		// driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		// driver.findElement(By.name("specification")).click();
		// driver.findElement(By.name("specification")).sendKeys("正确的规格");
		// driver.findElement(By.name("priceRetail")).click();
		// driver.findElement(By.name("priceRetail")).sendKeys("66");
		// Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) >
		// .layui-inline:nth-child(2)")).click();
		// driver.findElement(By.name("priceVIP")).sendKeys("锄禾日当午");
		// driver.findElement(By.id("middlePart")).click();
		// Thread.sleep(1000);
		// assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(),
		// is("格式错误，请输入非负数字，允许有两位小数"));
		// driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) >
		// .layui-inline:nth-child(2)")).click();
		// driver.findElement(By.name("priceVIP")).sendKeys(Keys.chord(Keys.CONTROL,
		// "a"), "123.456789");
		// driver.findElement(By.id("middlePart")).click();
		// Thread.sleep(1000);
		// assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(),
		// is("格式错误，请输入非负数字，允许有两位小数"));
		// driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) >
		// .layui-inline:nth-child(2)")).click();
		// driver.findElement(By.name("priceVIP")).sendKeys(Keys.chord(Keys.CONTROL,
		// "a"), "12345678987654321");
		// driver.findElement(By.id("middlePart")).click();
		// Thread.sleep(1000);
		// assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(),
		// is("格式错误，请输入非负数字，允许有两位小数"));
	}

	// 会员价前端已经注释了
	@Test
	public void uTC15checkPriceVIP2() {
		// LoginSucceed();
		// driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		// driver.findElement(By.linkText("商品列表")).click();
		// driver.switchTo().frame(1);
		// driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		// driver.findElement(By.name("barcodes")).click();
		// driver.findElement(By.name("barcodes")).sendKeys("1234567");
		// vars.put("commodityName", js.executeScript("return \"正确商品名称\" +
		// parseInt(Math.random()*100000);"));
		// driver.findElement(By.name("name")).click();
		// driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		// driver.findElement(By.name("specification")).click();
		// driver.findElement(By.name("specification")).sendKeys("正确的规格");
		// driver.findElement(By.name("priceRetail")).click();
		// driver.findElement(By.name("priceRetail")).sendKeys("66");
		// driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) >
		// .layui-inline:nth-child(2)")).click();
		// driver.findElement(By.name("priceVIP")).sendKeys("666");
		// driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		// assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(),
		// is("确认新建商品\\\"vars.get(\"commodityName\").toString()\\\"吗?"));
	}

	// 批发价前端已经注释了
	@Test
	public void uTC16checkPriceWholesale1() throws InterruptedException {
		// LoginSucceed();
		// Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".layui-icon-app")).click();
		//// driver.findElement(By.linkText("商品资料")).click();
		// driver.findElement(By.linkText("商品列表")).click();
		// driver.switchTo().frame(1);
		// Thread.sleep(5000);
		// driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		// driver.findElement(By.name("barcodes")).click();
		// driver.findElement(By.name("barcodes")).sendKeys("1234567");
		// vars.put("commodityName", js.executeScript("return \"正确商品名称\" +
		// parseInt(Math.random()*100000);"));
		// driver.findElement(By.name("name")).click();
		// driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		// driver.findElement(By.name("specification")).click();
		// driver.findElement(By.name("specification")).sendKeys("正确的规格");
		// driver.findElement(By.name("priceRetail")).click();
		// driver.findElement(By.name("priceRetail")).sendKeys("66");
		// driver.findElement(By.cssSelector(".generalCommodityInfo >
		// .layui-form-item:nth-child(13) > .layui-inline:nth-child(2)")).click();
		// driver.findElement(By.name("priceWholesale")).sendKeys("^_^");
		// driver.findElement(By.cssSelector(".commodityInfo")).click();
		// driver.findElement(By.cssSelector(".generalCommodityInfo >
		// .layui-form-item:nth-child(13) > .layui-inline:nth-child(2)")).click();
		// driver.findElement(By.name("priceWholesale")).sendKeys("123.456789");
		// driver.findElement(By.id("middlePart")).click();
		// driver.findElement(By.cssSelector(".generalCommodityInfo >
		// .layui-form-item:nth-child(13)")).click();
		// driver.findElement(By.name("priceWholesale")).sendKeys("98765432123456789");
		// driver.findElement(By.id("middlePart")).click();
	}

	// 批发价前端已经注释了
	@Test
	public void uTC17checkPriceWholesale2() {
		// LoginSucceed();
		// driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		// driver.findElement(By.linkText("商品列表")).click();
		// driver.switchTo().frame(1);
		// driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		// driver.findElement(By.name("barcodes")).click();
		// driver.findElement(By.name("barcodes")).sendKeys("1234567");
		// vars.put("commodityName", js.executeScript("return \"正确商品名称\" +
		// parseInt(Math.random()*100000);"));
		// driver.findElement(By.name("name")).click();
		// driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		// driver.findElement(By.name("specification")).click();
		// driver.findElement(By.name("specification")).sendKeys("正确的规格");
		// driver.findElement(By.name("priceRetail")).click();
		// driver.findElement(By.name("priceRetail")).sendKeys("66");
		// driver.findElement(By.cssSelector(".generalCommodityInfo >
		// .layui-form-item:nth-child(13) > .layui-inline:nth-child(2)")).click();
		// driver.findElement(By.name("priceWholesale")).sendKeys("666");
		// driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		// assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(),
		// is("确认新建商品\\\"vars.get(\"commodityName\").toString()\\\"吗?"));
	}

	@Test
	public void uTC18checkShelfLife1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) > .layui-inline:nth-child(2)")).click();
		driver.findElement(By.name("shelfLife")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "to be a man");
		driver.findElement(By.cssSelector(".commodityInfo")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正整数"));
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) > .layui-inline:nth-child(2)")).click();
		driver.findElement(By.name("shelfLife")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "666.666");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正整数"));
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) > .layui-inline:nth-child(2)")).click();
		driver.findElement(By.name("shelfLife")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "88888888666666");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合要求，请修改"));
		driver.findElement(By.name("shelfLife")).click();
		driver.findElement(By.name("shelfLife")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "保质保期");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正整数"));
	}

	@Test
	public void uTC19checkShelfLife2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("shelfLife")).click();
		driver.findElement(By.name("shelfLife")).sendKeys("666");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认新建商品\"" + vars.get("commodityName").toString() + "\"吗?"));
	}

	@Test
	public void uTC22checkMnemonicCode1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("mnemonicCode")).click();
		driver.findElement(By.name("mnemonicCode")).sendKeys("哈哈哈");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("只允许数字和英文"));
		driver.findElement(By.name("mnemonicCode")).click();
		driver.findElement(By.name("mnemonicCode")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "$^123");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("只允许数字和英文"));
	}

	@Test
	public void uTC23checkMnemonicCode2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("mnemonicCode")).click();
		driver.findElement(By.name("mnemonicCode")).sendKeys("yes");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认新建商品\"" + vars.get("commodityName").toString() + "\"吗?"));
	}

	@Test
	public void uTC24checkProeprtyValue1() throws InterruptedException {
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
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.name("propertyValue1")).click();
		driver.findElement(By.name("propertyValue1")).sendKeys("012345678901234567890123456789012345678901234567m是1");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC25checkProeprtyValue2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("propertyValue1")).click();
		driver.findElement(By.name("propertyValue1")).sendKeys("01234567890123456789012345678901234567890123456789");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is(not("属性值长度[0, 50]")));
	}

	@Test
	public void uTC28checkReturnDays1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("returnDays")).click();
		driver.findElement(By.name("returnDays")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "-12");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许非负整数"));
		driver.findElement(By.name("returnDays")).click();
		driver.findElement(By.name("returnDays")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "12.3");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许非负整数"));
		driver.findElement(By.name("returnDays")).click();
		driver.findElement(By.name("returnDays")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "-1.11");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许非负整数"));
		driver.findElement(By.name("returnDays")).click();
		driver.findElement(By.name("returnDays")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "12345654321");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合要求，请修改"));
	}

	@Test
	public void uTC29checkReturnDays2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("returnDays")).click();
		driver.findElement(By.name("returnDays")).sendKeys("123");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认新建商品\"" + vars.get("commodityName").toString() + "\"吗?"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("returnDays")).click();
		driver.findElement(By.name("returnDays")).sendKeys("0");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认新建商品\"" + vars.get("commodityName").toString() + "\"吗?"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
	}

	@Test
	public void uTC2checkBarcode1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "12345");
		driver.findElement(By.cssSelector(".generalCommodityInfo > h3:nth-child(2)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "12345678$");
		driver.findElement(By.cssSelector(".commodityInfo")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("条形码格式错误，仅允许英文、数值形式，长度为[7,64]"));
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "123456 654321");
		driver.findElement(By.id("middlePart")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC30checkTag1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("tag")).click();
		driver.findElement(By.name("tag")).sendKeys("012345678901234567890123456789012");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		// 前端限制了输入长度，不能输入大于32
		// Thread.sleep(1000);
		// assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(),
		// is("商品的标注为中文或英文或数字或符号的组合"));
	}

	@Test
	public void uTC31checkTag2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("tag")).click();
		driver.findElement(By.name("tag")).sendKeys("大家好，我是备注");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is(not("商品的标注为中文或英文或数字或符号的组合")));
	}

	@Test
	public void uTC32checkNOStart1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("nOStart")).click();
		driver.findElement(By.name("nOStart")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "-123");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许非负整数"));
		driver.findElement(By.name("nOStart")).click();
		driver.findElement(By.name("nOStart")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "1.2");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许非负整数"));
		driver.findElement(By.name("nOStart")).click();
		driver.findElement(By.name("nOStart")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "1234567890123");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合要求，请修改"));
	}

	@Test
	public void uTC33checkNOStart2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("nOStart")).click();
		driver.findElement(By.name("nOStart")).sendKeys("123");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请补充完整期初信息"));
	}

	@Test
	public void uTC34checkPurchasingPriceStart1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("purchasingPriceStart")).click();
		driver.findElement(By.name("purchasingPriceStart")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "hhh");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入非负数字，最多带2位小数"));
		driver.findElement(By.name("purchasingPriceStart")).click();
		driver.findElement(By.name("purchasingPriceStart")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "-123");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入非负数字，最多带2位小数"));
		driver.findElement(By.name("purchasingPriceStart")).click();
		driver.findElement(By.name("purchasingPriceStart")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "-123.123");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入非负数字，最多带2位小数"));
		driver.findElement(By.name("purchasingPriceStart")).click();
		driver.findElement(By.name("purchasingPriceStart")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "12345654321");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合要求，请修改"));
		driver.findElement(By.name("purchasingPriceStart")).click();
		driver.findElement(By.name("purchasingPriceStart")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "哈哈哈");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入非负数字，最多带2位小数"));
	}

	@Test
	public void uTC35checkPurchasingPriceStart2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("purchasingPriceStart")).click();
		driver.findElement(By.name("purchasingPriceStart")).sendKeys("12.12");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请补充完整期初信息"));
	}

	@Test
	public void uTC36checkStartValueRemark1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("startValueRemark")).click();
		driver.findElement(By.name("startValueRemark")).sendKeys("0123456789abcdefghij哈哈哈哈哈哈哈哈哈哈！@￥%…&*（）？01234567890");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		// 前端限制了输入长度，不能大于50
		// Thread.sleep(1000);
		// assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(),
		// is("期初值备注长度[0, 50]"));
	}

	@Test
	public void uTC37checkStartValueRemark2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("startValueRemark")).click();
		driver.findElement(By.name("startValueRemark")).sendKeys("0123456789abcdefghij哈哈哈哈哈哈哈哈哈哈！@￥%…&*（）？0123456789");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC38createCommodity1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
	}

	@Test
	public void uTC39createCommodity2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("shortName")).click();
		driver.findElement(By.name("shortName")).sendKeys("你好啊");
		// 批发价注释了
		// driver.findElement(By.name("priceWholesale")).click();
		// driver.findElement(By.name("priceWholesale")).sendKeys("77");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC3checkBarcode2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		driver.findElement(By.cssSelector(".generalCommodityInfo > h3:nth-child(2)")).click();
		Thread.sleep(1000);
		{
			List<WebElement> elements = driver.findElements(By.cssSelector(".layui-layer-content"));
			Thread.sleep(1000);
			assert (elements.size() == 0);
		}
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("abcdefg123");
		driver.findElement(By.cssSelector(".commodityInfo")).click();
	}

	@Test
	public void uTC40createCommodity3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#layui-layer5 > div.layui-layer-content")).getText(), is("创建商品成功"));
	}

	@Test
	public void uTC41createCommodity4() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("shortName")).click();
		driver.findElement(By.name("shortName")).sendKeys("正确的商品简称");
		driver.findElement(By.name("propertyValue1")).click();
		driver.findElement(By.name("propertyValue1")).sendKeys("正确的商品属性");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("创建商品成功"));
	}

	@Test
	public void uTC42createCommodity5() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("nOStart")).click();
		driver.findElement(By.name("nOStart")).sendKeys("123");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请补充完整期初信息"));
	}

	@Test
	public void uTC43createCommodity6() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("propertyValue1")).click();
		driver.findElement(By.name("propertyValue1")).sendKeys("正确的商品属性");
		driver.findElement(By.name("propertyValue3")).click();
		driver.findElement(By.name("propertyValue3")).sendKeys("正确的商品属性");
		driver.findElement(By.name("purchasingPriceStart")).click();
		driver.findElement(By.name("purchasingPriceStart")).sendKeys("12.12");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请补充完整期初信息"));
	}

	@Test
	public void uTC44createCommodity7() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.name("mnemonicCode")).click();
		driver.findElement(By.name("mnemonicCode")).sendKeys("rememberMe");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(6)")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		driver.findElement(By.cssSelector(".otherProvider")).click();
		driver.findElement(By.cssSelector(".otherProvider .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(5)")).click();
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		driver.findElement(By.cssSelector(".otherProvider:nth-child(16)")).click();
		driver.findElement(By.cssSelector(".otherProvider:nth-child(16) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认新建商品\"" + vars.get("commodityName").toString() + "\"吗?"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
	}

	@Test
	public void uTC45createCommodity8() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		driver.findElement(By.cssSelector(".otherProvider .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		driver.findElement(By.cssSelector(".otherProvider:nth-child(16) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".toAddProvider")).click();
		driver.findElement(By.cssSelector(".otherProvider:nth-child(17) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
	}

	@Test
	public void uTC46createCommodity9() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("shortName")).click();
		driver.findElement(By.name("shortName")).sendKeys("简短的商品名称");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		// 批发价和会员价在前端已注释
		// Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) >
		// .layui-inline:nth-child(2) > .layui-input-inline")).click();
		// driver.findElement(By.name("priceVIP")).sendKeys("66");
		// driver.findElement(By.cssSelector(".generalCommodityInfo >
		// .layui-form-item:nth-child(13) > .layui-inline:nth-child(2) >
		// .layui-input-inline")).click();
		// driver.findElement(By.name("priceWholesale")).sendKeys("66");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		vars.put("multiPackageCommodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiPackageCommodityName1").toString());
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).sendKeys("22");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).sendKeys("1234567");
		vars.put("multiPackageCommodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).sendKeys(vars.get("multiPackageCommodityName2").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(2) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		// Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".baseBarcode")).click();
		driver.findElement(By.cssSelector(".baseBarcode")).click();
		driver.findElement(By.cssSelector(".baseBarcode")).sendKeys("1234567");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC47createCommodity10() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		vars.put("multiPackageCommodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiPackageCommodityName1").toString());
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).sendKeys("22");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).sendKeys("1234567");
		vars.put("multiPackageCommodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).sendKeys(vars.get("multiPackageCommodityName2").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(2) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("checkIfHaveSameField")).getText(), is("存在相同的包装单位，请修改"));
	}

	@Test
	public void uTC48createCommodity11() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("副单位数量已达到支持上限\n请勿再添加副单位，谢谢"));
	}

	@Test
	public void uTC49createCommodity12() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("商品倍数为大于1的整数"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(2) > .layui-inline")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("0");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
	}

	@Test
	public void uTC4checkName1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("12346589");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("小当家#￥");
		assertThat(driver.findElement(By.id("checkField")).getText(), is("商品名称格式错误，请输入中英数值、空格(只允许中间出现)，支持的符号为()（）-——_，长度为（0,32]"));
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(" ");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
	}

	@Test
	public void uTC50createCommodity13() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		vars.put("multiPackageCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiPackageCommodityName").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认新建商品\"" + vars.get("commodityName").toString() + "\"吗?"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC51createCommodity14() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC52createCommodity15() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("7654321");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("克");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("10.00");
		driver.findElement(By.id("middlePart")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("名称重复"));
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC53createCommodity16() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC54createCommodity17() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys("星巴克A");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC55createCommodity18() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("7654321");
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys("星巴克A");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC56createCommodity19() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		vars.put("multiPackageCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiPackageCommodityName").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC58createCommodity21() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("组合商品11");
		driver.findElement(By.name("name")).sendKeys("组合商品1111");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("ml");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".commodityManage:nth-child(4)"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
	}

	@Test
	public void uTC59createCommodity22() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("123");
		driver.findElement(By.name("barcodes")).sendKeys(Keys.ENTER);
	}

	@Test
	public void uTC5checkName2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		vars.put("commodityName1", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) > .layui-inline:nth-child(1) > .layui-form-label")).click();
		{
			List<WebElement> elements = driver.findElements(By.cssSelector(".layui-layer-content"));
			assert (elements.size() == 0);
		}
		vars.put("commodityName2", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("commodityName2").toString());
		// driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.id("middlePart")).click();
		{
			List<WebElement> elements = driver.findElements(By.cssSelector(".layui-layer-content"));
			assert (elements.size() == 0);
		}
	}

	@Test
	public void uTC60createCommodity23() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("组合商品11");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC61createCommodity24() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("123454321");
		vars.put("multiPackageCommodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiPackageCommodityName").toString());
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-input-inline > .layui-input")).sendKeys("22");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(4) > .layui-input")).sendKeys("543212345");
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).click();
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(4) > .layui-input")).sendKeys(vars.get("multiPackageCommodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
	}

	@Test
	public void uTC62createCommodity25() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
	}

	@Test
	public void uTC63createCommodity26() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
	}

	@Test
	public void uTC64createCommodity27() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
	}

	@Test
	public void uTC65createCommodity28() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).sendKeys("简单的商品名称");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).sendKeys("大家好我是备注");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
	}

	@Test
	public void uTC66createCommodity29() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).sendKeys("简单的商品名称");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).sendKeys("大家好我是备注");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC67createCommodity30() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).sendKeys("简单的商品名称");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).sendKeys("大家好我是备注");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
	}

	@Test
	public void uTC68createCommodity31() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("至少需要选择两个商品"));
	}

	@Test
	public void uTC69createCommodity32() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
	}

	@Test
	public void uTC6checkShortName1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("specification")).sendKeys("正确规格");
		driver.findElement(By.name("shortName")).click();
		driver.findElement(By.name("shortName")).sendKeys("！错误格式的商品简称！");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("允许以中英数值、空格形式出现，不允许使用特殊符号"));
	}

	@Test
	public void uTC70createCommodity33() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC71createCommodity34() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).sendKeys("简单的商品名称");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).sendKeys("大家好我是备注");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
	}

	@Test
	public void uTC72createCommodity35() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).sendKeys("简单的商品名称");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).sendKeys("大家好我是备注");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("至少需要选择两个商品"));
	}

	@Test
	public void uTC73createCommodity36() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).sendKeys("简单的商品名称");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(28) .layui-input")).sendKeys("大家好我是备注");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC74createCommodity37() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC75createCommodity38() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("6821423302364");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("星巴克AB");
		driver.findElement(By.name("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "星巴克AB45456");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("毫升");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
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
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC76createCommodity39() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		vars.put("commodityNO", driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).getAttribute("value"));
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).click();
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).sendKeys("number");
		driver.findElement(By.id("middlePart")).click();
		{
			String value = driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).getAttribute("value");
			assertThat(value, is(vars.get("commodityNO").toString()));
		}
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).click();
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).sendKeys("1.985");
		driver.findElement(By.id("middlePart")).click();
		{
			String value = driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).getAttribute("value");
			assertThat(value, is(vars.get("commodityNO").toString()));
		}
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).click();
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).sendKeys("-1.23");
		driver.findElement(By.id("middlePart")).click();
		{
			String value = driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).getAttribute("value");
			assertThat(value, is(vars.get("commodityNO").toString()));
		}
	}

	@Test
	public void uTC77createCommodity40() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("commodityNO", js.executeScript("return parseInt(Math.random()*100000);"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("commodityNO").toString());
		driver.findElement(By.id("middlePart")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).getAttribute("value");
			assertThat(value, is(vars.get("commodityNO").toString()));
		}
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) tr:nth-child(1) > td:nth-child(4) > input")).sendKeys("60");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC78createCommodity41() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("挂号费");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("毫升");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("1.00");
		driver.findElement(By.name("shortName")).click();
		driver.findElement(By.name("shortName")).sendKeys("咖啡");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) > .layui-inline:nth-child(2)")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".btnChoosed"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		driver.findElement(By.name("priceRetail")).sendKeys("NaN");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
	}

	@Test
	public void uTC79createCommodity42() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
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
		vars.put("commodityPrice", js.executeScript("return parseFloat(Math.random()*100000).toFixed(2);"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).click();
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("commodityPrice").toString());
		driver.findElement(By.id("middlePart")).click();
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).getAttribute("value");
			assertThat(value, is(vars.get("commodityPrice").toString()));
		}
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		driver.findElement(By.cssSelector(".layui-table:nth-child(1) > tbody:nth-child(3) > tr:nth-child(1)")).click();
		driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).sendKeys("4.00");
		driver.findElement(By.cssSelector(".combinedCommodityInfo")).click();
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC7checkShortName2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.name("specification")).sendKeys("正确规格");
		driver.findElement(By.name("shortName")).click();
		driver.findElement(By.name("shortName")).sendKeys("正确的商品简称");
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is(not("允许以中英数值、空格形式出现，不允许使用特殊符号")));
	}

	@Test
	public void uTC81_12commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		{
			String value = driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).getAttribute("value");
			assertThat(value, is("1"));
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		{
			String value = driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).getAttribute("value");
			assertThat(value, is(""));
		}
	}

	@Test
	public void uTC8110commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".leftRegion .layui-nav-item:nth-child(13) > a")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
		Thread.sleep(2000);
		vars.put("count", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText());
		System.out.println("商品条数为：" + vars.get("count").toString());
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(27) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"不一样商品名称\" + parseInt(Math.random()*100000);"));
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
		vars.put("count",
				js.executeScript("function test(text){      var num = parseInt(text.replace(/[^0-9]/ig, \"\"));      num += 1;      text = \"共 \" + num + \" 条\";      return text;    }    return test(arguments[0]);", vars.get("count")));
		System.out.println("此时查到的商品条数应为：" + vars.get("count").toString());
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTC8111commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".leftRegion .layui-nav-item:nth-child(13) > a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("01234567890123456789012345678901234567890123456789012345678901234");
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
	}

	@Test
	public void uTC8112commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		vars.put("commodityCategoryCount", js.executeScript("return document.getElementsByClassName(\'leftRegion\')[0].getElementsByClassName(\'layui-nav-item\').length;"));
		System.out.println("商品类别的数目为：" + vars.get("commodityCategoryCount").toString());
		driver.findElement(By.cssSelector(".showAllCommCategory:nth-child(3)")).click();
		assertThat(driver.findElement(By.cssSelector(".showAllCommCategory:nth-child(3)")).getText(), is("全部关闭"));
		if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'leftRegion\')[0].getElementsByClassName(\'layui-nav-itemed\').length != arguments[0])", vars.get("commodityCategoryCount"))) {
			Assert.assertTrue(false);
		}
		driver.findElement(By.cssSelector(".showAllCommCategory:nth-child(3)")).click();
		assertThat(driver.findElement(By.cssSelector(".showAllCommCategory:nth-child(3)")).getText(), is("全部展开"));
		if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'leftRegion\')[0].getElementsByClassName(\'layui-nav-itemed\').length != 0)")) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void uTC8113commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.name("layTableCheckbox")).isSelected());
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("10"));
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		assertFalse(driver.findElement(By.name("layTableCheckbox")).isSelected());
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("0"));
	}

	@Test
	public void uTC8114commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
//		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("1"));
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("2"));
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("3"));
		}
	}

	@Test
	public void uTC8115commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("3");
		// 输入数字后还要按回车键或点击其它地方，才能选中这个商品
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button")).click();
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("2"));
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button")).click();
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("1"));
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button")).click();
		assertFalse(driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is(""));
		}
	}

	@Test
	public void uTC8116commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).click();
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-table-cell .layui-input")).sendKeys("12");
		driver.findElement(By.cssSelector(".rightRegion > .topArea")).click();
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).click();
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-table-cell .layui-input")).sendKeys("0");
		driver.findElement(By.cssSelector(".rightRegion > .topArea")).click();
		assertFalse(driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/input")).isSelected());
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).click();
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-table-cell .layui-input")).sendKeys("-12");
		driver.findElement(By.cssSelector(".rightRegion > .topArea")).click();
		assertFalse(driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/input")).isSelected());
	}

	@Test
	public void uTC8117commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[2]/div[2]/div[2]/div/div/a[2]")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		vars.put("count", driver.findElement(By.cssSelector("span > strong")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		if ((Boolean) js.executeScript("return (document.getElementsByClassName(\'combinedSubCommodity\')[0].getElementsByTagName(\'tbody\')[0].getElementsByTagName(\'tr\').length != arguments[0])", vars.get("count"))) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void uTC8118commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-icon-add-circle")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		{
			WebElement element = driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		driver.findElement(By.cssSelector(".closePopupPage")).click();
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-icon-add-circle")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("2"));
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("1"));
		}
	}

	@Test
	public void uTC8119commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-icon-add-circle")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		{
			WebElement element = driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-icon-add-circle")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("2"));
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("1"));
		}
	}

	@Test
	public void uTC8120commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".leftRegion > .layui-btn")).click();
	}

	@Test
	public void uTC8121commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(6000);
		driver.findElement(By.cssSelector("#layui-laypage-2 > .layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-2 > .layui-laypage-next > .layui-icon")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector("#layui-laypage-3 > .layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#layui-laypage-5 > .layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("layui-laypage-6")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-6 .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "30");
		driver.findElement(By.cssSelector("#layui-laypage-6 .layui-laypage-btn")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("layui-laypage-7")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("middlePart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-7 .layui-laypage-btn")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#layui-laypage-8 > .layui-laypage-skip")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-8 .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "77");
		driver.findElement(By.cssSelector("#layui-laypage-8 .layui-laypage-btn")).click();
		Thread.sleep(3000);
		{
			WebElement element = driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).release().perform();
		}
		driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "2");
		driver.findElement(By.cssSelector("#layui-laypage-9 .layui-laypage-btn")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#layui-laypage-10 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-10 .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "16");
		driver.findElement(By.cssSelector("#layui-laypage-10 .layui-laypage-btn")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(10)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(10) > .layui-input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "1");
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(10) > .layui-laypage-btn")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#layui-laypage-12 > .layui-laypage-next > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-12 > .layui-laypage-next > .layui-icon")).click();
		Thread.sleep(4000);
		driver.findElement(By.linkText("4")).click();
	}

	@Test
	public void uTC813commodityPopup() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".leftRegion .layui-nav-item:nth-child(2) > a")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".leftRegion .layui-nav-item:nth-child(2) > a"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("家禽")).click();
		Thread.sleep(3000);
		// driver.findElement(By.cssSelector(".layui-table .layui-icon")).click();
		driver.findElement(By.xpath("//div[@id='toChooseGeneralComm']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addNum")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
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
		driver.findElement(By.cssSelector(".layui-form-danger")).sendKeys("12345645");
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys("有胡椒粉");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-input-inline > .layui-input")).sendKeys("咖啡");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("毫升");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(25) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
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
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("蛋品")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".leftRegion .layui-nav-item:nth-child(1) > a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".leftRegion .layui-nav-item:nth-child(1) > a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".leftRegion .layui-nav-itemed > .layui-nav-child a")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".leftRegion .layui-nav-itemed > .layui-nav-child a"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element, 0, 0).perform();
		}
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id='toChooseGeneralComm']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
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
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-layer-close"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
	}

	@Test
	public void uTC814commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("#");
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("普通……");
	}

	@Test
	public void uTC815commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("正确商品名称");
		Thread.sleep(10000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(27) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(1)")).click();
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
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("正确商品名称");
	}

	@Test
	public void uTC816commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
		Thread.sleep(2000);
		vars.put("count", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText());
		System.out.println("商品条数为：" + vars.get("count").toString());
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(27) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(1)")).click();
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
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		vars.put("count",
				js.executeScript("function test(text){      var num = parseInt(text.replace(/[^0-9]/ig, \"\"));      num += 1;      text = \"共 \" + num + \" 条\";      return text;    }    return test(arguments[0]);", vars.get("count")));
		System.out.println("此时查到的商品条数应为：" + vars.get("count").toString());
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
	}

	@Test
	public void uTC817commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("01234567890123456789012345678901234567890123456789012345678901234");
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
		{
			List<WebElement> elements = driver.findElements(By.cssSelector("#popupCommodityList + div .layui-laypage-count"));
			assert (elements.size() == 0);
		}
	}

	@Test
	public void uTC818commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".leftRegion .layui-nav-item:nth-child(13) > a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("@");
	}

	@Test
	public void uTC819commodityPopup() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".leftRegion .layui-nav-item:nth-child(13) > a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-nav-itemed:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("正确商品名称");
		Thread.sleep(10000);
		vars.put("count", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText());
		System.out.println("商品条数为：" + vars.get("count").toString());
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(27) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		vars.put("commodityName", js.executeScript("return \"不一样商品名称\" + parseInt(Math.random()*100000);"));
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
		System.out.println("此时查到的商品条数应为：" + vars.get("count").toString());
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTC82createCommodity() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("测试12");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("毫升");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("1.00");
		driver.findElement(By.name("shortName")).click();
		driver.findElement(By.name("shortName")).sendKeys("咖啡");
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
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
//		Thread.sleep(4000);
//		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
//		Thread.sleep(1000);
//		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("测试12");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("毫升");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("1.00");
		driver.findElement(By.name("shortName")).click();
		driver.findElement(By.name("shortName")).sendKeys("咖啡");
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
		//
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC83createCommodity() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
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
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
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
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("1234567");
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC84createCommodity() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
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
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建组合商品成功"));
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("1234567");
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
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
		Thread.sleep(4000);
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
	}

	@Test
	public void uTC85Presale() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("13888888888");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.name("pwdEncrypted")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector("#rightSide > .topArea")).click();
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("14667896456");
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("克");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("50.00");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("A可乐糖18");
		driver.findElement(By.name("name")).sendKeys("A可乐");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
	}

	@Test
	public void uTC86createCommodity() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC87createCommodity() throws InterruptedException {
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
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除商品成功"));
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).click();
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).sendKeys("1234567");
		driver.findElement(By.cssSelector(".serviceCommName")).click();
		driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys("正确的规格");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC8checkSpecification1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("1234567");
		driver.findElement(By.cssSelector("div.generalCommodityInfo.layui-form > h3")).click();
		Thread.sleep(1000);
		vars.put("commodityName", js.executeScript("return \"正确商品名称\" + parseInt(Math.random()*100000);"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector("div.generalCommodityInfo.layui-form > h3")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "！错误的规格！");
		driver.findElement(By.cssSelector("div.generalCommodityInfo.layui-form > h3")).click();
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("允许以中英数值、空格形式出现，不允许使用特殊符号"));
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(10) > .layui-inline:nth-child(2)")).click();
		driver.findElement(By.name("specification")).sendKeys(Keys.chord(Keys.CONTROL, "a"), " ");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC91checkBarcodeLength() throws InterruptedException {
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
		driver.findElement(By.name("barcodes")).sendKeys("123456");
		driver.findElement(By.id("middlePart")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("条形码格式错误，仅允许英文、数值形式，长度为[7,64]"));
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
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "123456");
		driver.findElement(By.id("middlePart")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("条形码格式错误，仅允许英文、数值形式，长度为[7,64]"));
		driver.findElement(By.name("barcodes")).click();
		driver.findElement(By.name("barcodes")).sendKeys("7654321");
		driver.findElement(By.cssSelector("body")).click();
	}

	@Test
	public void uTC92createCommodity() throws InterruptedException {
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
		driver.findElement(By.name("specification")).click();
		driver.findElement(By.name("specification")).sendKeys("正确的规格");
		driver.findElement(By.name("priceRetail")).click();
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).click();
		driver.findElement(By.cssSelector(".otherBarcode .layui-input")).sendKeys("1111111");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) .layui-input")).sendKeys("22222222");
		driver.findElement(By.id("middlePart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-input")).sendKeys("333333333");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		driver.findElement(By.cssSelector(".otherBarcode:nth-child(13) .layui-input")).sendKeys("4444444444");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		driver.findElement(By.cssSelector(".otherBarcode:nth-child(14) .layui-input")).sendKeys("55555555555");
		driver.findElement(By.cssSelector(".commodityInfo")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("增加条形码成功"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		driver.findElement(By.cssSelector(".otherBarcode:nth-child(15) .layui-input")).sendKeys("666666666666");
		driver.findElement(By.cssSelector("body")).click();
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
		driver.findElement(By.cssSelector(".otherBarcode:nth-child(16) .layui-input")).sendKeys("7777777777777");
		driver.findElement(By.cssSelector("body")).click();
		driver.findElement(By.cssSelector(".toAddBarcode")).click();
	}

	@Test
	public void uTC99createCommodity() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toDeleteProvider")).click();
	}

	@Test
	public void uTC9checkSpecification2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".commodityManage:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is(not("允许以中英数值、空格形式出现，不允许使用特殊符号")));
	}
}
