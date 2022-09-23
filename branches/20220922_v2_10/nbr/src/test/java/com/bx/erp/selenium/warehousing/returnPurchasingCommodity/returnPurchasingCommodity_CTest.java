package com.bx.erp.selenium.warehousing.returnPurchasingCommodity;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class returnPurchasingCommodity_CTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTC1createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.xpath("//div[@id=\'returnPurchasingCommodity\']/div[3]/div/ul/li/div/div")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
	}

	@Test
	public void uTC2createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("closeLayerPage")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请添加商品"));
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .laytable-cell-4-0-0")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
	}

	@Test
	public void uTC4presaleCreateReturnPurchasingCommodity() {
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("库管")).click();
		driver.findElement(By.linkText("采购退货")).click();
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
		driver.close();
	}

	@Test
	public void uTC7createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).sendKeys("123456789");
		vars.put("serviceCommodityName", js.executeScript("var serviceCommodityName = \"服务型商品\" + Math.floor(Math.random() * 999999); return serviceCommodityName;"));
		driver.findElement(By.cssSelector(".serviceCommName")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(vars.get("serviceCommodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys("克");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建服务类商品成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).sendKeys(vars.get("serviceCommodityName").toString());
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTC8createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		vars.put("simpleCommodityName", js.executeScript("var simpleCommodityName = \"普通商品\" + Math.floor(Math.random() * 999999); return simpleCommodityName;"));
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("simpleCommodityName").toString());
		driver.findElement(By.name("specification")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).sendKeys("克");
		driver.findElement(By.name("priceRetail")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".toChoosedMultiUnit > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toAddUnit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackageBarcode > td:nth-child(3) > .layui-input")).sendKeys("123456789");
		vars.put("multiPackageCommodityName", js.executeScript("var multiPackageCommodityName = \"多包装商品\" + Math.floor(Math.random() * 999999); return multiPackageCommodityName;"));
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".multiPackageCommName > td:nth-child(3) > .layui-input")).sendKeys(vars.get("multiPackageCommodityName").toString());
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline:nth-child(4) > .layui-input")).sendKeys("11");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommodityInfo > .layui-form-item:nth-child(23) .layui-input-inline > .layui-input")).sendKeys("123456789");
		vars.put("combinedCommodityName", js.executeScript("var combinedCommodityName = \"组合商品\" + Math.floor(Math.random() * 999999); return combinedCommodityName;"));
		driver.findElement(By.cssSelector(".combinedCommName")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".combinedCommName")).sendKeys(vars.get("combinedCommodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(24) > .layui-inline:nth-child(2) .layui-input")).sendKeys("克");
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseGeneralComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建组合商品成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).sendKeys(vars.get("multiPackageCommodityName").toString());
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).sendKeys(vars.get("combinedCommodityName").toString());
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTC9createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is("默认供应商"));
	}

	@Test
	public void uTC10createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".tabColor")).getText(), is("保存"));
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(4) > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(1) .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) li:nth-child(5)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(4) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(4) li:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
	}

	@Test
	public void uTC11createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(4) > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".tabColor")).getText(), is("保存"));
		driver.findElement(By.cssSelector("li:nth-child(4) > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".tabColor")).getText(), is("保存"));
	}

	@Test
	public void uTC12createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-curr:nth-child(3) > em:nth-child(2)")).getText(), is("2"));
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
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("returnCommoditySN").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
		assertThat(driver.findElement(By.cssSelector("#layui-laypage-9 em:nth-child(2)")).getText(), is("1"));
	}

	@Test
	public void uTC13createReturnPurchasingCommodity() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，退货单号至少需要输入10位"));
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is("默认供应商"));
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(2) td:nth-child(3)")).getText(), is(""));
	}

	@Test
	public void uTC14createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.cssSelector(".provider_name")).getText());
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		vars.put("commodityName", driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td[3]/div")).getText());
		driver.findElement(By.linkText("1")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is("默认供应商"));
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(2) td:nth-child(3)")).getText(), is(""));
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is("默认供应商"));
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(2) td:nth-child(3)")).getText(), is(""));
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is(vars.get("providerName").toString()));
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
		assertThat(driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td[3]/div")).getText(), is(vars.get("commodityName").toString()));
	}

	@Test
	public void uTC15createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品相关")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		Thread.sleep(1000);
		vars.put("providerName", js.executeScript("var providerName = \"供应商\" + Math.floor(Math.random() * 999999); return providerName;"));
		driver.findElement(By.id("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("name")).sendKeys(vars.get("providerName").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(3000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".provideSelect > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("该供应商不存在，请重新选择供应商"));
	}

	@Test
	public void uTC16createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
	}

	@Test
	public void uTC17createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
		driver.findElement(By.cssSelector("li:nth-child(4) > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
	}

	@Test
	public void uTC18createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("10001");
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数量必须是大于0，小于等于10000的整数"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("10000");
		driver.findElement(By.name("price")).click();
		Thread.sleep(5000);
		driver.findElement(By.name("price")).sendKeys("10001.00");
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("采购价必须大于等于0，小于等于10000.0"));
		driver.findElement(By.cssSelector("li:nth-child(1) .provider-name")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
	}

	@Test
	public void uTC19createReturnPurchasingCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
	}
}
