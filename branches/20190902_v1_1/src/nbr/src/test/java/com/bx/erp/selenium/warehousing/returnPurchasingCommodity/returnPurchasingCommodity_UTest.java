package com.bx.erp.selenium.warehousing.returnPurchasingCommodity;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class returnPurchasingCommodity_UTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTU1approveSheet1() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要审核这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核退货单成功"));
		assertThat(driver.findElement(By.cssSelector(".cost .returnCommoditySheetManage")).getText(), is("已审核"));
	}

	@Test
	public void uTU2approveSheet2() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector("tr:nth-child(1) button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要审核这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核退货单成功"));
		assertThat(driver.findElement(By.cssSelector(".cost .returnCommoditySheetManage")).getText(), is("已审核"));
	}

	@Test
	public void uTU3approveSheet3() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		{
			WebElement element = driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td/i")).click();
		{
			WebElement element = driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(1) button > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("8"));
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要审核这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核退货单成功"));
	}

	@Test
	public void uTU4approveSheet4() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("9");
		driver.findElement(By.name("NO")).sendKeys(Keys.ENTER);
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).sendKeys("99");
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).sendKeys(Keys.ENTER);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要审核这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核退货单成功"));
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(5)")).getText(), is("9"));
		assertThat(driver.findElement(By.cssSelector(".odd > td:nth-child(5)")).getText(), is("99"));
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(3) > td:nth-child(5)")).getText(), is("1"));
	}

	@Test
	public void uTU5approveSheet5() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("td > button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys("99.99");
		driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).sendKeys("123.00");
		driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要审核这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核退货单成功"));
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(6)")).getText(), is("99.99"));
		assertThat(driver.findElement(By.cssSelector(".odd > td:nth-child(6)")).getText(), is("123.00"));
	}

	@Test
	public void uTU6approveSheet6() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText());
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要审核这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核退货单成功"));
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is("vars.get(\"providerName\").toString()"));
	}

	@Test
	public void uTU7updateSheet1() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("2"));
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector("tr:nth-child(1) button > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("2"));
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("退货单修改成功"));
		driver.findElement(By.cssSelector("tr:nth-child(1) button > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("4"));
	}

	@Test
	public void uTU8updateSheet2() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("10"));
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector("tr:nth-child(1) button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("0"));
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请添加商品"));
	}

	@Test
	public void uTU9updateSheet3() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("10"));
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		{
			WebElement element = driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td/i")).click();
		{
			WebElement element = driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("退货单修改成功"));
		driver.findElement(By.cssSelector("tr:nth-child(7) button > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("8"));
	}

	@Test
	public void uTU10updateSheet4() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("9");
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).sendKeys("99");
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("退货单修改成功"));
		driver.findElement(By.cssSelector("li:nth-child(2) .provider-name")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(1) .option-status")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is("9"));
		}
		{
			String value = driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).getAttribute("value");
			assertThat(value, is("99"));
		}
	}

	@Test
	public void uTU11updateSheet5() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改信息后再保存"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("999");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("1");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改信息后再保存"));
	}

	@Test
	public void uTU12updateSheet6() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys("9.99");
		driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).sendKeys("123.00");
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("退货单修改成功"));
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(1) .option-status")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("price")).getAttribute("value");
			assertThat(value, is("9.99"));
		}
		{
			String value = driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).getAttribute("value");
			assertThat(value, is("123.00"));
		}
	}

	@Test
	public void uTU13updateSheet7() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改信息后再保存"));
		vars.put("price", driver.findElement(By.name("price")).getAttribute("value"));
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys("1.23");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys(vars.get("price").toString());
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改信息后再保存"));
	}

	@Test
	public void uTU14updateSheet8() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText());
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("退货单修改成功"));
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(1) .provider-name")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is(vars.get("providerName").toString()));
	}

	@Test
	public void uTU15updateCommodityNoInPopup1() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("555");
		driver.findElement(By.cssSelector("#allCommodity .footArea")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("1"));
	}

	@Test
	public void uTU16updateCommodityNoInPopup2() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).click();
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-input")).sendKeys("2.3");
		driver.findElement(By.cssSelector("#allCommodity .footArea")).click();
		{
			String value = driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("2"));
		}
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("1"));
		driver.close();
	}

	@Test
	public void uTU17updateCommodityNoInPopup3() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("数量");
		driver.findElement(By.cssSelector("#allCommodity .footArea")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is(""));
		}
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("0"));
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("abc");
		driver.findElement(By.cssSelector("#allCommodity .footArea")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is(""));
		}
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("0"));
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("-1");
		driver.findElement(By.cssSelector("#allCommodity .footArea")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is(""));
		}
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("0"));
	}

	@Test
	public void uTU18updateCommodityNoInPopup4() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("space", js.executeScript("var space = \" \"; return space;"));
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys(vars.get("space").toString());
		driver.findElement(By.cssSelector("#allCommodity .footArea")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is(""));
		}
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("0"));
	}

	@Test
	public void uTU19updateCommodityNoInPopup5() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("2"));
		}
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("1"));
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("1"));
		}
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("1"));
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is(""));
		}
		assertThat(driver.findElement(By.cssSelector(".commodityKinds")).getText(), is("0"));
	}

	@Test
	public void uTU20updateCommodityNoInPage1() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .provider-name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("123");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is("123"));
		}
	}

	@Test
	public void uTU21updateCommodityNoInPage2() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("2.3");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品数量"));
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is("2"));
		}
	}

	@Test
	public void uTU22updateCommodityNoInPage3() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .provider-name")).click();
		Thread.sleep(1000);
		vars.put("NO", driver.findElement(By.name("NO")).getAttribute("value"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("数量");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品数量"));
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is(vars.get("NO").toString()));
		}
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("abc");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品数量"));
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is(vars.get("NO").toString()));
		}
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("-1");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品数量"));
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is(vars.get("NO").toString()));
		}
	}

	@Test
	public void uTU23updateCommodityNoInPage4() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		vars.put("NO", driver.findElement(By.name("NO")).getAttribute("value"));
		vars.put("space", js.executeScript("var space = \" \"; return space;"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys(vars.get("space").toString());
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品数量"));
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is(vars.get("NO").toString()));
		}
	}

	@Test
	public void uTU24updateCommodityPriceInPage1() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys("3.21");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("price")).getAttribute("value");
			assertThat(value, is("3.21"));
		}
	}

	@Test
	public void uTU25updateCommodityPriceInPage2() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys("2.356");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("price")).getAttribute("value");
			assertThat(value, is("2.36"));
		}
	}

	@Test
	public void uTU26updateCommodityPriceInPage3() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		vars.put("price", driver.findElement(By.name("price")).getAttribute("value"));
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys("价格");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品价格"));
		{
			String value = driver.findElement(By.name("price")).getAttribute("value");
			assertThat(value, is(vars.get("price").toString()));
		}
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys("abc");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品价格"));
		{
			String value = driver.findElement(By.name("price")).getAttribute("value");
			assertThat(value, is(vars.get("price").toString()));
		}
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys("-11");
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品价格"));
		{
			String value = driver.findElement(By.name("price")).getAttribute("value");
			assertThat(value, is(vars.get("price").toString()));
		}
	}

	@Test
	public void uTU27updateCommodityPriceInPage4() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .provider-name")).click();
		Thread.sleep(1000);
		vars.put("price", driver.findElement(By.name("price")).getAttribute("value"));
		vars.put("space", js.executeScript("var space = \" \"; return space;"));
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys(vars.get("space").toString());
		driver.findElement(By.cssSelector("tbody:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的商品价格"));
		{
			String value = driver.findElement(By.name("price")).getAttribute("value");
			assertThat(value, is(vars.get("price").toString()));
		}
	}

	@Test
	public void uTU31keepHistory1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		vars.put("commodityName1", js.executeScript("var commodityName1 = \"我叫普通商品\" + Math.floor(Math.random() * 999999); return commodityName1;"));
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.name("specification")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).sendKeys("克");
		driver.findElement(By.name("priceRetail")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
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
		driver.findElement(By.id("searchcommodity")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("commodityName2", js.executeScript("var commodityName2 = \"我叫普通商品\" + Math.floor(Math.random() * 999999); return commodityName2;"));
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("returnCommoditySN").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("td:nth-child(3) > .wrap")).getText(), is(vars.get("commodityName2").toString()));
	}

	@Test
	public void uTU32keepHistory2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		vars.put("commodityName1", js.executeScript("var commodityName1 = \"我叫普通商品\" + Math.floor(Math.random() * 999999); return commodityName1;"));
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.name("specification")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).sendKeys("克");
		driver.findElement(By.name("priceRetail")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
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
		driver.findElement(By.id("searchcommodity")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要审核这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核退货单成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("commodityName2", js.executeScript("var commodityName2 = \"我叫普通商品\" + Math.floor(Math.random() * 999999); return commodityName2;"));
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(3)")).click();
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("returnCommoditySN").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		assertThat(driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td[3]/div")).getText(), is(vars.get("commodityName1").toString()));
	}

	@Test
	public void uTU33updateSheet9() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品相关")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		Thread.sleep(1000);
		vars.put("providerName", js.executeScript("var providerName = \"供应商\" + Math.floor(Math.random() * 999999); return providerName;"));
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
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).sendKeys(vars.get("providerName").toString());
		driver.findElement(By.id("seek_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".provideSelect > .delete")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要删除" + vars.get("providerName").toString() + "吗"));
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("该供应商不存在，请重新选择供应商"));
	}

	@Test
	public void uTU34updateSheet10() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).sendKeys("2");
		driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(5) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(5) > input")).sendKeys("3");
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("退货单修改成功"));
		{
			String value = driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr[2]/td[5]/input")).getAttribute("value");
			assertThat(value, is("2"));
		}
		{
			String value = driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr[3]/td[5]/input")).getAttribute("value");
			assertThat(value, is("3"));
		}
	}

	@Test
	public void uTU35updateSheet11() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).sendKeys("2.22");
		driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(6) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(6) > input")).sendKeys("3.33");
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("退货单修改成功"));
		{
			String value = driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr[2]/td[6]/input")).getAttribute("value");
			assertThat(value, is("2.22"));
		}
		{
			String value = driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr[3]/td[6]/input")).getAttribute("value");
			assertThat(value, is("3.33"));
		}
	}

	@Test
	public void uTU36updateSheet12() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).sendKeys("9");
		driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).sendKeys("5.55");
		driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(5) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(5) > input")).sendKeys("6");
		driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(6) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(6) > input")).sendKeys("6.66");
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("退货单修改成功"));
		{
			String value = driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).getAttribute("value");
			assertThat(value, is("9"));
		}
		{
			String value = driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).getAttribute("value");
			assertThat(value, is("5.55"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(5) > input")).getAttribute("value");
			assertThat(value, is("6"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(6) > input")).getAttribute("value");
			assertThat(value, is("6.66"));
		}
	}

	@Test
	public void uTU37updateSheet13() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(2) .provider-name")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(3) .option-status")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("987");
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(3) .option-status")).click();
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
	public void uTU38updateSheet14() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		vars.put("NO", driver.findElement(By.name("NO")).getAttribute("value"));
		vars.put("price", driver.findElement(By.name("price")).getAttribute("value"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("123");
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys("321.00");
		driver.findElement(By.cssSelector("li:nth-child(2) .provider-name")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(1) .option-status")).click();
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is(vars.get("NO").toString()));
		}
		{
			String value = driver.findElement(By.name("price")).getAttribute("value");
			assertThat(value, is(vars.get("price").toString()));
		}
	}

	@Test
	public void uTU39updateSheet15() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(" ");
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		vars.put("NO", driver.findElement(By.name("NO")).getAttribute("value"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys("12");
		vars.put("price", js.executeScript("var price = Math.floor(Math.random() * 999); return price;"));
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys(vars.get("price").toString());
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys(vars.get("NO").toString());
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("退货单修改成功"));
	}

	@Test
	public void uTU40updateSheet16() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		vars.put("NO", driver.findElement(By.name("NO")).getAttribute("value"));
		vars.put("price", driver.findElement(By.name("price")).getAttribute("value"));
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(" ");
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("1")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		vars.put("NO1", js.executeScript("var NO1 = Math.floor(Math.random() * 999); return NO1;"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys(vars.get("NO1").toString());
		vars.put("price1", js.executeScript("var price1 = Math.floor(Math.random() * 999); return price1;"));
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys(vars.get("price1").toString());
		vars.put("NO2", js.executeScript("var NO2 = Math.floor(Math.random() * 999); return NO2;"));
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(5) > input")).sendKeys(vars.get("NO2").toString());
		vars.put("price2", js.executeScript("var price2 = Math.floor(Math.random() * 999); return price2;"));
		driver.findElement(By.cssSelector(".odd > td:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".odd > td:nth-child(6) > input")).sendKeys(vars.get("price2").toString());
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys(vars.get("NO").toString());
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys(vars.get("price").toString());
		driver.findElement(By.cssSelector(".tabColor")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改这个退货单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("退货单修改成功"));
	}

	@Test
	public void uTU41updateSheet17() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		vars.put("NO", js.executeScript("var NO = Math.floor(Math.random() * 999); return NO;"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys(vars.get("NO").toString());
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is(vars.get("NO").toString()));
		}
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is(vars.get("NO").toString()));
		}
	}

	@Test
	public void uTU42updateSheet18() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .provider-name")).click();
		Thread.sleep(1000);
		vars.put("InitialNO", driver.findElement(By.name("NO")).getAttribute("value"));
		vars.put("InitialPrice", driver.findElement(By.name("price")).getAttribute("value"));
		vars.put("NO", js.executeScript("var NO = Math.floor(Math.random() * 999); return NO;"));
		driver.findElement(By.name("NO")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("NO")).sendKeys(vars.get("NO").toString());
		vars.put("price", js.executeScript("var price = Math.floor(Math.random() * 999); return price;"));
		driver.findElement(By.name("price")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("price")).sendKeys(vars.get("price").toString());
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(" ");
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("NO")).getAttribute("value");
			assertThat(value, is(vars.get("InitialNO").toString()));
		}
		{
			String value = driver.findElement(By.name("price")).getAttribute("value");
			assertThat(value, is(vars.get("InitialPrice").toString()));
		}
	}
}
