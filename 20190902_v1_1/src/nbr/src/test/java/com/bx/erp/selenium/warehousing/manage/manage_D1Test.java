package com.bx.erp.selenium.warehousing.manage;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class manage_D1Test extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTD1deleteCommodity() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//a[contains(text(),\'2\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".footArea")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(4) .layui-icon-close-fill")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-icon-close-fill")).click();
	}

	@Test
	public void uTD2deleteWS() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("100");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("10");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity > .rightRegion")).click();
		Thread.sleep(1000);
		;
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.findElement(By.cssSelector(".warehousingManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除入库单成功"));
	}

	@Test
	public void uTD3deleteWS() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("100");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("10");
		Thread.sleep(1000);
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
		driver.findElement(By.cssSelector(".amountTotalAndStatus > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".warehousingCommodityList")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".amountTotalAndStatus > .layui-btn")).getText(), is("已审核"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(3)")).getText(), is("删除"));
	}

	@Test
	public void uTD6deleteWarehousingCommodity() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		Thread.sleep(1000);
		vars.put("commodityName", js.executeScript("var commodityName = \"小当家\" + Math.round(Math.random()*9999); return commodityName;"));
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
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
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector("td > .laytable-cell-28-0-0 > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".commodityManage:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认删除商品" + vars.get("commodityName").toString() + "吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("该商品有入库单依赖，不能删除"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector("#warehousingList > li")).click();
		Thread.sleep(1000);
		vars.put("warehousingSN", driver.findElement(By.cssSelector(".warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector(".amountTotalAndStatus > .layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核单号为：" + vars.get("warehousingSN").toString() + "的入库单成功 ！"));
	}

	@Test
	public void uTD7dateleWS() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		System.out.println(vars.get("num1").toString());
		driver.findElement(By.cssSelector(".warehousingManage:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除入库单成功"));
		assertThat(driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).getText(), is("所有 v"));
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] > arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}
}
