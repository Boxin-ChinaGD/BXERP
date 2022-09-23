package com.bx.erp.selenium.warehousing.returnPurchasingCommodity;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class returnPurchasingCommodity_RTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTR1retrieveWhenFirstVisited() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".click .option-status")));
		}
	}

	@Test
	public void uTR2retrieveByStatus() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).getText(), is("未审核"));
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).getText(), is("审核"));
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(3)")).getText(), is("已审核"));
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".cost .returnCommoditySheetManage")).getText(), is("已审核"));
	}

	@Test
	public void uTR3retrieveByStaff() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnPurchaseRNHint")).getText(), is("查无数据"));
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(1000);
		vars.put("staffName", driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(8)")).getText());
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(8)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".click .creator")).getText(), is(vars.get("staffName").toString()));
	}

	@Test
	public void uTR4retrieveByProvider() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) lable")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.xpath("//li[contains(.,\'台湾雅方食品有限公司\')]")).getText());
		driver.findElement(By.xpath("//li[contains(.,\'台湾雅方食品有限公司\')]")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".click .provider-name")).getText(), is(vars.get("providerName").toString()));
	}

	@Test
	public void uTR6retrieveByCommodityAndSN1() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("commodityName", driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td[3]/div")).getText());
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(1000);
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("returnCommoditySN").toString());
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("commodityName").toString());
		assertThat(driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td[3]/div")).getText(), is(vars.get("commodityName").toString()));
	}

	@Test
	public void uTR6retrieveByCommodityAndSN2() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys("$%^");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，长度为(0,32]，支持输入的符号有：（）()_-——"));
	}

	@Test
	public void uTR8retrieveByStatusAndStaff() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) > .layui-form-label > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(8)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).getText(), is("审核"));
		assertThat(driver.findElement(By.cssSelector(".founder_warp")).getText(), is("创建人：店长"));
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(8)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".cost .returnCommoditySheetManage")).getText(), is("已审核"));
		assertThat(driver.findElement(By.cssSelector(".founder_warp")).getText(), is("创建人：店长"));
	}

	@Test
	public void uTR9retrieveByStatusAndProvider() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText());
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
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
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is(vars.get("providerName").toString()));
		assertThat(driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).getText(), is("审核"));
	}

	@Test
	public void uTR11retrieveByStatusAndCommodity() throws InterruptedException {
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
		vars.put("commodityName", driver.findElement(By.cssSelector("td:nth-child(3) > .wrap")).getText());
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
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("commodityName").toString());
		assertThat(driver.findElement(By.cssSelector("td:nth-child(3) > .wrap")).getText(), is(vars.get("commodityName").toString()));
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".cost .returnCommoditySheetManage")).getText(), is("已审核"));
	}

	@Test
	public void uTR12retrieveByStatusAndSN() throws InterruptedException {
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
		driver.findElement(By.cssSelector("li:nth-child(2) .provider-name")).click();
		Thread.sleep(1000);
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("returnCommoditySN").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
		assertThat(driver.findElement(By.cssSelector(".cost_audit_stastu > .returnCommoditySheetManage")).getText(), is("审核"));
	}

	@Test
	public void uTR13retrieveByStaffAndProvider() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText());
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
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
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建成功"));
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) > .layui-form-label > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(8)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is(vars.get("providerName").toString()));
		assertThat(driver.findElement(By.cssSelector(".founder_warp")).getText(), is("创建人：店长"));
	}

	@Test
	public void uTR15retrieveByStaffAndCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("commodityName", driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")).getText());
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
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(8)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td[3]/div")).getText(), is(vars.get("commodityName").toString()));
		assertThat(driver.findElement(By.cssSelector(".founder_warp")).getText(), is("创建人：店长"));
	}

	@Test
	public void uTR16retrieveByStaffAndSN() throws InterruptedException {
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
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(1000);
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) li:nth-child(8)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("returnCommoditySN").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
		assertThat(driver.findElement(By.cssSelector(".founder_warp")).getText(), is("创建人：店长"));
	}

	@Test
	public void uTR18retrieveByProviderAndCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText());
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("commodityName", driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")).getText());
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
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) > .layui-form-label > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is(vars.get("providerName").toString()));
		assertThat(driver.findElement(By.cssSelector("td:nth-child(3) > .wrap")).getText(), is(vars.get("commodityName").toString()));
	}

	@Test
	public void uTR19retrieveByProviderAndSN() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText());
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
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
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(1000);
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("returnCommoditySN").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is(vars.get("providerName").toString()));
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
	}

	@Test
	public void uTR22retrieveByClickLeftArea() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("commodityName", driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[2]/div")).getText());
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
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
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
		driver.findElement(By.cssSelector("li:nth-child(2) .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("td:nth-child(3) > .wrap")).getText(), is(vars.get("commodityName").toString()));
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
	}

	@Test
	public void uTR23retrieveWhenClickNextPage() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText(">")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".founder_warp")));
		}
	}

	@Test
	public void uTR24retrieveWhenClickPageAfterSearch() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys("商品");
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText(">")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".founder_warp")));
		}
		driver.findElement(By.linkText("<")).click();
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".returnCommoditySN > label")));
		}
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".founder_warp")));
		}
	}

	@Test
	public void uTR25toShowProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#chooseProviderWindow")));
		}
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#chooseProviderWindow")));
		}
	}

	@Test
	public void uTR26retrieveProviderByDistrict() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("天津")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
		driver.findElement(By.linkText("安徽")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText(), is("山东沂水食用百合研究开发中心"));
	}

	@Test
	public void uTR27jumpToCreateNewProvider() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#chooseProviderWindow > .leftRegion > .layui-btn")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		assertThat(driver.findElement(By.cssSelector(".layui-form > span:nth-child(1)")).getText(), is("供应商列表"));
	}

	@Test
	public void uTR28retrieveProviderInPopup1() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).sendKeys("默认供应商");
		assertThat(driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText(), is("默认供应商"));
	}

	@Test
	public void uTR29retrieveProviderInPopup2() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).sendKeys("abcdefg");
		driver.findElement(By.id("seek_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTR30retrieveProviderInPopup3() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("space", js.executeScript("var space = \" \"; return space;"));
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).sendKeys(vars.get("space").toString());
		driver.findElement(By.id("seek_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTR31chooseProviderInPopup1() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.cssSelector(".provider_name")).getText());
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).sendKeys("不存在的供应商");
		driver.findElement(By.id("seek_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is(vars.get("providerName").toString()));
	}

	@Test
	public void uTR32chooseProviderInPopup2() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText());
		driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirm")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is(vars.get("providerName").toString()));
	}

	@Test
	public void uTR33cancelChooseProviderInPopup() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".click .option-status")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.cssSelector(".provider_name")).getText());
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("cancel")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is(vars.get("providerName").toString()));
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".provider_name")).getText(), is(vars.get("providerName").toString()));
	}

	@Test
	public void uTR34paginationAboutProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-4 > a:nth-child(3)")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")));
		}
		driver.findElement(By.cssSelector("#layui-laypage-5 > .layui-laypage-prev > .layui-icon")).click();
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")));
		}
	}

	@Test
	public void uTR35toShowCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#allCommodity")));
		}
	}

	@Test
	public void uTR36retrieveCommodityByCategory() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("蛋肉家禽")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("家禽")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")).getText(), is("三角龙"));
		driver.findElement(By.linkText("猪牛羊肉")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTR37jumpToCreateCommodity() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#allCommodity > .leftRegion > .layui-btn")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		assertThat(driver.getTitle(), is("BoXin-商品列表"));
	}

	@Test
	public void uTR38retrieveCommodityInPopup1() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).sendKeys("长虹剑");
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")).getText(), is("长虹剑"));
	}

	@Test
	public void uTR39retrieveCommodityInPopup2() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).sendKeys("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTR40retrieveCommodityInPopup3() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("space", js.executeScript("var space = \" \"; return space;"));
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).sendKeys(vars.get("space").toString());
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，支持输入的符号有：（）()_-——"));
	}

	@Test
	public void uTR41chooseCommodityInPopup1() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("commodityName", driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")).getText());
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("confirmChooseCommodity")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td[3]/div")).getText(), is(vars.get("commodityName").toString()));
	}

	@Test
	public void uTR42chooseCommodityInPopup2() throws InterruptedException {
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
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr[10]/td[3]/div")));
		}
	}

	@Test
	public void uTR43cancelChooseCommodityInPopup() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("commodityName", driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td[3]/div")).getText());
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("td:nth-child(3) > .wrap")).getText(), is(vars.get("commodityName").toString()));
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("closeLayerPage")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("td:nth-child(3) > .wrap")).getText(), is(vars.get("commodityName").toString()));
	}

	@Test
	public void uTR44paginationAboutCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-4 > a:nth-child(3)")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")));
		}
		driver.findElement(By.cssSelector("#layui-laypage-5 > .layui-laypage-prev > .layui-icon")).click();
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")));
		}
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(8) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(8) > .layui-input")).sendKeys("3");
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(8) > .layui-laypage-btn")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")));
		}
	}

	@Test
	public void uTR45retrieveBySN1() throws InterruptedException {
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
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("returnCommoditySN").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(1) .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
	}

	@Test
	public void uTR46retrieveBySN2() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys("TH2020011");
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，退货单号至少需要输入10位"));
	}

	@Test
	public void uTR47retrieveBySN3() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("returnCommoditySN").toString() + "0000");
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnPurchaseRNHint")).getText(), is("查无数据"));
	}

	@Test
	public void uTR48retrieveBySN4() throws InterruptedException {
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("returnCommoditySN").toString() + "00000");
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnPurchaseRNHint")).getText(), is("查无数据"));
	}

	@Test
	public void uTR50retrieveByCommodity1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		vars.put("commodityName1", js.executeScript("var commodityName1 = \"我是普通商品\" + Math.floor(Math.random() * 999999); return commodityName1;"));
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
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("commodityName2", js.executeScript("var commodityName2 = \"我是普通商品\" + Math.floor(Math.random() * 999999); return commodityName2;"));
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
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnPurchaseRNHint")).getText(), is("查无数据"));
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(1) .option-status")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td[3]/div")).getText(), is("vars.get(\"commodityName2\").toString()"));
	}

	@Test
	public void uTR51retrieveByCommodity2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		vars.put("commodityName1", js.executeScript("var commodityName1 = \"我是普通商品\" + Math.floor(Math.random() * 999999); return commodityName1;"));
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
		vars.put("commodityName2", js.executeScript("var commodityName2 = \"我是普通商品\" + Math.floor(Math.random() * 999999); return commodityName2;"));
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
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("commodityName1").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'returnPurchasingCommodity\']/div[3]/div/ul/li/div/div")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//table[@id=\'CommodityTable\']/tbody/tr/td[3]/div")).getText(), is("vars.get(\"commodityName1\").toString()"));
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("commodityName2").toString());
		driver.findElement(By.cssSelector(".td_icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".returnPurchaseRNHint")).getText(), is("查无数据"));
	}

	@Test
	public void uTR52retrieveByCommodity3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		vars.put("commodityName", js.executeScript("var commodityName = \"普通_—商品\" + Math.floor(Math.random() * 999999); return commodityName;"));
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).sendKeys("克");
		driver.findElement(By.name("priceRetail")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.name("mnemonicCode")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mnemonicCode")).sendKeys("zjm");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("库管")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("button > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("searchcommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.id("searchcommodity")).sendKeys(vars.get("commodityName").toString());
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
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".td_inp")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".provider-name")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("td:nth-child(3) > .wrap")).getText(), is(vars.get("commodityName").toString()));
	}

	@Test
	public void uTR53searchByphone() throws InterruptedException {
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
		vars.put("phone", js.executeScript("var phone = \"020-82020\" + Math.floor(Math.random() * 999999); return phone;"));
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("库管")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provider_icon > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("providerinput")).sendKeys(vars.get("phone").toString());
		assertThat(driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText(), is(vars.get("providerName").toString()));
		assertThat(driver.findElement(By.xpath("//div[@id=\'chooseProviderWindow\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[5]/div")).getText(), is(vars.get("phone").toString()));
	}

	@Test
	public void uTR54retrieveByManyCondition() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(4) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(4) li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'returnPurchasingCommodity\']/div[3]/div/ul/li/div/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(4) > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).getText(), is("所有"));
		assertThat(driver.findElement(By.cssSelector(".layui-inline:nth-child(4) lable")).getText(), is("所有"));
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) lable")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(1) li:nth-child(2)")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".CommodityForm")));
		}
	}

	@Test
	public void uTR55showOrHiddenSN1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("采购退货")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("returnCommoditySN", driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText());
		driver.findElement(By.linkText("2")).click();
		driver.findElement(By.linkText("1")).click();
		assertThat(driver.findElement(By.cssSelector(".returnCommoditySN > label")).getText(), is(vars.get("returnCommoditySN").toString()));
	}
}
