package com.bx.erp.selenium.warehousing.manage;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class manage_UTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTU1reviewWarehousiong1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".amountTotalAndStatus .layui-btn-disabled")).getText(), is("已审核"));
		assertThat(driver.findElement(By.cssSelector(".amountTotalAndStatus .hadApproveButton")).getText(), is("已审核"));
		driver.close();
	}

	@Test
	public void uTU2reviewWarehousiong2() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("warehousingSheetSN", driver.findElement(By.cssSelector(".warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector(".amountTotalAndStatus > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核单号为：" + vars.get("warehousingSheetSN").toString() + "的入库单成功 ！"));
		assertThat(driver.findElement(By.cssSelector("#warehousingList li:nth-child(1) span:nth-child(2)")).getText(), is("已审核"));
		assertThat(driver.findElement(By.cssSelector(".amountTotalAndStatus .layui-btn-disabled")).getText(), is("已审核"));
		assertThat(driver.findElement(By.cssSelector(".amountTotalAndStatus .hadApproveButton")).getText(), is("已审核"));
	}

	@Test
	public void uTU4updateCommodityNo2() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("90");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("80");
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).getAttribute("value");
			assertThat(value, is("90"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).getAttribute("value");
			assertThat(value, is("80"));
		}
	}

	@Test
	public void uTU5updateCommodityNo3() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).sendKeys("19");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).sendKeys("123");
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon-add-circle")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("123"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("19"));
		}
	}

	@Test
	public void uTU6updateCommodityNo4() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("100");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("10");
		driver.findElement(By.cssSelector("#toChooseCommodity > .rightRegion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id=\'toChooseProvider\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[10]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[3]/td[5]/input")).sendKeys("101");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[3]/td[6]/input")).sendKeys("4.00");
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[3]/td[7]/input")).getAttribute("value");
			assertThat(value, is("404.00"));
		}
		{
			WebElement element = driver.findElement(By.cssSelector(".warehousingCommodityList table tbody tr:nth-child(2)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU7updateCommodity1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-icon-close-fill")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-icon-close-fill")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".btnChoosed"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU8updateCommodityNO1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("100");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("10");
		driver.findElement(By.cssSelector("#toChooseCommodity > .rightRegion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("2.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).sendKeys("4.00");
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).sendKeys("140");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).sendKeys("60");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).getAttribute("value");
			assertThat(value, is("280.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).getAttribute("value");
			assertThat(value, is("240.00"));
		}
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改入库单成功"));
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).getAttribute("value");
			assertThat(value, is("140"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).getAttribute("value");
			assertThat(value, is("60"));
		}
	}

	@Test
	public void uTU9updateCommodityPrice1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("100");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("10");
		driver.findElement(By.cssSelector("#toChooseCommodity > .rightRegion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("2.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).sendKeys("4.00");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).getAttribute("value");
			assertThat(value, is("200.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).getAttribute("value");
			assertThat(value, is("40.00"));
		}
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改入库单成功"));
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).getAttribute("value");
			assertThat(value, is("200.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).getAttribute("value");
			assertThat(value, is("40.00"));
		}
	}

	@Test
	public void uTU10updateCommodityAmount1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("100");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("10");
		driver.findElement(By.cssSelector("#toChooseCommodity > .rightRegion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("2.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).sendKeys("4.00");
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).sendKeys("300.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).sendKeys("80.00");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).getAttribute("value");
			assertThat(value, is("3.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).getAttribute("value");
			assertThat(value, is("8.00"));
		}
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改入库单成功"));
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).getAttribute("value");
			assertThat(value, is("300.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).getAttribute("value");
			assertThat(value, is("80.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).getAttribute("value");
			assertThat(value, is("3.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).getAttribute("value");
			assertThat(value, is("8.00"));
		}
	}

	@Test
	public void uTU11updateCommodityNO2() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr/td[5]/input")).sendKeys("1000");
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr/td[6]/input")).sendKeys("5.00");
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr[2]/td[5]/input")).sendKeys("1200");
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr[2]/td[6]/input")).sendKeys("3.00");
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个采购订单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("SN", driver.findElement(By.cssSelector(".odd-numbers > label")).getText());
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个入库单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		assertThat(driver.findElement(By.cssSelector(".purchasingOrderSN")).getText(), is(vars.get("SN").toString()));
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).sendKeys("1100");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).sendKeys("200");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).getAttribute("value");
			assertThat(value, is("5500.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).getAttribute("value");
			assertThat(value, is("600.00"));
		}
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改入库单成功"));
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).getAttribute("value");
			assertThat(value, is("1100"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).getAttribute("value");
			assertThat(value, is("200"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).getAttribute("value");
			assertThat(value, is("5500.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).getAttribute("value");
			assertThat(value, is("600.00"));
		}
	}

	@Test
	public void uTU12updateCommodityPrice2() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr/td[5]/input")).sendKeys("1000");
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr/td[6]/input")).sendKeys("5.00");
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr[2]/td[5]/input")).sendKeys("1200");
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr[2]/td[6]/input")).sendKeys("3.00");
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个采购订单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("SN", driver.findElement(By.cssSelector(".odd-numbers > label")).getText());
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个入库单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		assertThat(driver.findElement(By.cssSelector(".purchasingOrderSN")).getText(), is("vars.get(\"SN\").toString()"));
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("4.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).sendKeys("4.00");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).getAttribute("value");
			assertThat(value, is("4000.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).getAttribute("value");
			assertThat(value, is("4800.00"));
		}
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改入库单成功"));
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).getAttribute("value");
			assertThat(value, is("4.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).getAttribute("value");
			assertThat(value, is("4.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).getAttribute("value");
			assertThat(value, is("4000.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).getAttribute("value");
			assertThat(value, is("4800.00"));
		}
	}

	@Test
	public void uTU13updateCommodityAmount2() throws InterruptedException {
		driver.findElement(By.linkText("采购订单列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("li:nth-child(1) > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addGeneralComm")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'allCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChooseCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr/td[5]/input")).sendKeys("1000");
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr/td[6]/input")).sendKeys("5.00");
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr[2]/td[5]/input")).sendKeys("1200");
		driver.findElement(By.xpath("//table[@id=\'purchasingOrder\']/tbody/tr[2]/td[6]/input")).sendKeys("3.00");
		driver.findElement(By.cssSelector(".tabColor:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个采购订单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("SN", driver.findElement(By.cssSelector(".odd-numbers > label")).getText());
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核成功"));
		driver.findElement(By.cssSelector(".cost_audit_stastu > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".tabColor > .layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要创建这个入库单吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		assertThat(driver.findElement(By.cssSelector(".purchasingOrderSN")).getText(), is(vars.get("SN").toString()));
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).sendKeys("6500.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).sendKeys("2800.00");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).getAttribute("value");
			assertThat(value, is("6.50"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).getAttribute("value");
			assertThat(value, is("2.33"));
		}
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改入库单成功"));
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).getAttribute("value");
			assertThat(value, is("6.50"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).getAttribute("value");
			assertThat(value, is("2.33"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]/input")).getAttribute("value");
			assertThat(value, is("6500.00"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]/input")).getAttribute("value");
			assertThat(value, is("2800.00"));
		}
	}

	@Test
	public void uTU14updateByApprove() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("100");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("10");
		driver.findElement(By.cssSelector("#toChooseCommodity > .rightRegion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("2.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).sendKeys("4.00");
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		vars.put("warehousingSheetSN", driver.findElement(By.cssSelector(".warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector(".amountTotalAndStatus > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核单号为：" + vars.get("warehousingSheetSN").toString() + "的入库单成功 ！"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]")).getText(), is("100"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]")).getText(), is("2.00"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]")).getText(), is("200.00"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]")).getText(), is("10"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]")).getText(), is("4.00"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[7]")).getText(), is("40.00"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[4]/button")).getText(), is("已审核"));
	}

	@Test
	public void uTU18noUpdateWarehousing() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改信息后再保存"));
		driver.findElement(By.cssSelector(".warehousingManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText(">")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("warehousingManageMain")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("商品");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
	}

	@Test
	public void uTU19updateWarehousing() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).sendKeys("1034");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li:nth-child(2) > .creatorName")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStaffID > li:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-curr:nth-child(2) > em:nth-child(2)")).getText(), is("1"));
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-curr:nth-child(2) > em:nth-child(2)")).getText(), is("1"));
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-curr:nth-child(3) > em:nth-child(2)")).getText(), is("2"));
	}

	@Test
	public void uTU20closeButtonOfPromptBox() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("5.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).sendKeys("6.00");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".buttonArea .btnChoosed")).getText(), is("保存"));
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".buttonArea .btnChoosed")).getText(), is("保存"));
	}

	@Test
	public void uTU21saveButton() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改信息后再保存"));
	}

	@Test
	public void uTU22updateNextSearch() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).sendKeys("190");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).sendKeys("180");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("123.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).sendKeys("321.00");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).sendKeys("144");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).sendKeys("145");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("4.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).sendKeys("5.00");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("RK");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，入库单号或者采购订单号至少需要输入10位"));
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).getAttribute("value");
			assertThat(value, is("190"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(5) > input")).getAttribute("value");
			assertThat(value, is("180"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).getAttribute("value");
			assertThat(value, is("123.00"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(6) > input")).getAttribute("value");
			assertThat(value, is("321.00"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(7) > input")).getAttribute("value");
			assertThat(value, is("23370.00"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(7) > input")).getAttribute("value");
			assertThat(value, is("57780.00"));
		}
	}

	@Test
	public void uTU23updateNextApprove() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).sendKeys("101");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("11.00");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		vars.put("warehousingSheetSN", driver.findElement(By.cssSelector(".warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector(".amountTotalAndStatus > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核单号为：" + vars.get("warehousingSheetSN").toString() + "的入库单成功 ！"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]")).getText(), is("101"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]")).getText(), is("11.00"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[7]")).getText(), is("1111.00"));
		assertThat(driver.findElement(By.cssSelector("li:nth-child(2) > .statusText")).getText(), is("已审核"));
		assertThat(driver.findElement(By.cssSelector("#warehousingList > li:nth-child(2) .circle_hadApproved")).getText(), is(""));
		assertThat(driver.findElement(By.cssSelector(".btnChoosed")).getText(), is("已审核"));
	}

	@Test
	public void uTU24updateNextSearch() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).sendKeys("190");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).sendKeys("180");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("123.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).sendKeys("321.00");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).sendKeys("144");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).sendKeys("145");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[6]/input")).sendKeys("4.00");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[6]/input")).sendKeys("5.00");
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("RK2019");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，入库单号或者采购订单号至少需要输入10位"));
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(5) > input")).getAttribute("value");
			assertThat(value, is("190"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(5) > input")).getAttribute("value");
			assertThat(value, is("180"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(6) > input")).getAttribute("value");
			assertThat(value, is("123.00"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(6) > input")).getAttribute("value");
			assertThat(value, is("321.00"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(7) > input")).getAttribute("value");
			assertThat(value, is("23370.00"));
		}
		{
			String value = driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(7) > input")).getAttribute("value");
			assertThat(value, is("57780.00"));
		}
	}
}
