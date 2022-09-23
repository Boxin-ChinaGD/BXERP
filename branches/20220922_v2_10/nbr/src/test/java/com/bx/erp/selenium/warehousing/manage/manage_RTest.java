package com.bx.erp.selenium.warehousing.manage;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class manage_RTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTR1searchAllWarehousing() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(1)")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR2searchWarehousing() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("SN1", driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector("li:nth-child(2) > .provider")).click();
		Thread.sleep(1000);
		vars.put("SN2", driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector("li:nth-child(1) > .provider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText(), is(vars.get("SN1").toString()));
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText(), is(vars.get("SN2").toString()));
	}

	@Test
	public void uTR3checkQueryKeyword() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("123456789012345678901234567890123");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".warehousingRNHint")).getText(), is("无数据"));
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("#$%");
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，长度为(0,32]，支持输入的符号有：（）()_-——"));
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		vars.put("warehousingSheetLength ",
				js.executeScript("var warehousingList = document.getElementById(\"warehousingList\"); var warehousingSheetLength = warehousingList.getElementsByTagName(\"li\").length; return warehousingSheetLength;"));
		if ((Boolean) js.executeScript("return (arguments[0] <= 0)", vars.get("warehousingSheetLength"))) {
		}
	}

	@Test
	public void uTR4searchWarehousing1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR5searchWarehousing2() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).clear();
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).sendKeys("1号普通商品");
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		vars.put("SN1", driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText());
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("SN1").toString());
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		Thread.sleep(1000);
		vars.put("liLength", js.executeScript("var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; return liLength;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 1)", vars.get("liLength"))) {
		}
		driver.findElement(By.name("queryKeyword")).sendKeys("1号普通商品");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vars.put("liLength", js.executeScript("var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; return liLength;"));
		if ((Boolean) js.executeScript("return (arguments[0] < 1)", vars.get("liLength"))) {
		}
		driver.findElement(By.name("queryKeyword")).sendKeys("默认供应商");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vars.put("liLength", js.executeScript("var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; return liLength;"));
		if ((Boolean) js.executeScript("return (arguments[0] < 1)", vars.get("liLength"))) {
		}
	}

	@Test
	public void uTR6searchWarehousing3() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，入库单号或者采购订单号至少需要输入10位"));
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(3000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("@");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，长度为(0,32]，支持输入的符号有：（）()_-——"));
		Thread.sleep(2000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("RK 2019");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("输入的关键字首尾不能有空格，只允许中间有空格"));
	}

	@Test
	public void uTR7searchByStatus2() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(1)")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR8searchByStatus2() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
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
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num-1;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR9searchByStatus3() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(3)")).click();
		Thread.sleep(1000);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
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
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(3)")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num-1;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR10searchByStaff1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStaffID > li:nth-child(1)")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR11searchByStaff2() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
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
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num-1;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR12searchByProvider1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(5) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlProviderID > li:nth-child(1)")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR13searchByProvider2() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(5) > span")).click();
		Thread.sleep(1000);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
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
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(5) > span")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num-1;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR14searchByCreateDate1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num-1;"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlDate > li:nth-child(1)")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR15searchByCreateDate2() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlDate > li:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlDate > li:nth-child(3)")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlDate > li:nth-child(4)")).click();
		Thread.sleep(1000);
		vars.put("num3", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
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
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlDate > li:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("num1_1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num-1;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1_1"), vars.get("num1"))) {
		}
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlDate > li:nth-child(3)")).click();
		Thread.sleep(1000);
		vars.put("num2_1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num-1;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num2_1"), vars.get("num2"))) {
		}
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlDate > li:nth-child(4)")).click();
		Thread.sleep(1000);
		vars.put("num3_1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num-1;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num3_1"), vars.get("num3"))) {
		}
	}

	@Test
	public void uTR18searchWarehousing() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStaffID > li:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(5) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlProviderID > li:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
	}

	@Test
	public void uTR19page1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText(">")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("<")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
	}

	@Test
	public void uTR20page2() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li[data-keyword=\"-1\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(5) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("ul.UlProviderID > li")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText(">")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("1")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("<")).click();
	}

	@Test
	public void uTR24searchBy21RN() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("RK201910081000000001");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingList > .warehousingRNHint")).getText(), is("无数据"));
	}

	@Test
	public void uTR25searchBy10RN() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("RK20190605");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		vars.put("SN", js.executeScript("var SN = document.getElementsByClassName(\"warehousingSheetSN\")[0].innerText; return SN.indexOf(\"RK20190605\");"));
		if ((Boolean) js.executeScript("return (arguments[0] < 0)", vars.get("SN"))) {
		}
	}

	@Test
	public void uTR26searchBy9RN() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("RK2019060");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，入库单号或者采购订单号至少需要输入10位"));
	}


	@Test
	public void uTR30searchNoDatastatus() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStaffID > li[data-keyword=\"3\"]")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingList > .warehousingRNHint")).getText(), is("无数据"));
		vars.put("commodityLength", js.executeScript("var body = document.getElementsByClassName(\"warehousingCommodityList\")[0].getElementsByTagName(\"tbody\")[0]; return body.getElementsByTagName(\"tbody\").length;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 0)", vars.get("commodityLength"))) {
		}
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(2)")).getText(), is("保存"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(3)")).getText(), is("删除"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(4)")).getText(), is("取消"));
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请选择入库商品"));
	}

	@Test
	public void uTR31searchNoDatastaff() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStaffID > li[data-keyword=\"3\"]")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingList > .warehousingRNHint")).getText(), is("无数据"));
		vars.put("commodityLength", js.executeScript("var body = document.getElementsByClassName(\"warehousingCommodityList\")[0].getElementsByTagName(\"tbody\")[0]; return body.getElementsByTagName(\"tbody\").length;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 0)", vars.get("commodityLength"))) {
		}
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(2)")).getText(), is("保存"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(3)")).getText(), is("删除"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(4)")).getText(), is("取消"));
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请选择入库商品"));
	}

	@Test
	public void uTR32searchNoDataprovider() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(5) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlProviderID > li[data-keyword=\"5\"]")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingList > .warehousingRNHint")).getText(), is("无数据"));
		vars.put("commodityLength", js.executeScript("var body = document.getElementsByClassName(\"warehousingCommodityList\")[0].getElementsByTagName(\"tbody\")[0]; return body.getElementsByTagName(\"tbody\").length;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 0)", vars.get("commodityLength"))) {
		}
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(2)")).getText(), is("保存"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(3)")).getText(), is("删除"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(4)")).getText(), is("取消"));
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请选择入库商品"));
	}

	@Test
	public void uTR33searchNoDatacreateDate() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStaffID > li[data-keyword=\"3\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlDate > li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingList > .warehousingRNHint")).getText(), is("无数据"));
		vars.put("commodityLength", js.executeScript("var body = document.getElementsByClassName(\"warehousingCommodityList\")[0].getElementsByTagName(\"tbody\")[0]; return body.getElementsByTagName(\"tbody\").length;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 0)", vars.get("commodityLength"))) {
		}
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(2)")).getText(), is("保存"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(3)")).getText(), is("删除"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(4)")).getText(), is("取消"));
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请选择入库商品"));
	}

	@Test
	public void uTR34searchNoDatavalue() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("aaaa");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingList > .warehousingRNHint")).getText(), is("无数据"));
		vars.put("commodityLength", js.executeScript("var body = document.getElementsByClassName(\"warehousingCommodityList\")[0].getElementsByTagName(\"tbody\")[0]; return body.getElementsByTagName(\"tbody\").length;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 0)", vars.get("commodityLength"))) {
		}
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(2)")).getText(), is("保存"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(3)")).getText(), is("删除"));
		assertThat(driver.findElement(By.cssSelector(".buttonArea .disabledButton:nth-child(4)")).getText(), is("取消"));
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请选择入库商品"));
	}

	@Test
	public void uTR36cancelButton() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStaffID > li[data-keyword=\"3\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(5) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlProviderID > li[data-keyword=\"5\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlDate > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("aaaa");
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).getText(), is("所有 v"));
		assertThat(driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).getText(), is("所有 v"));
		assertThat(driver.findElement(By.cssSelector(".layui-form-label:nth-child(5) > span")).getText(), is("所有 v"));
		assertThat(driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).getText(), is("所有 v"));
	}

	@Test
	public void uTR37cancelButton() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("num1", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStaffID > li[data-keyword=\"4\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(5) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlProviderID > li[data-keyword=\"1\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlDate > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea button:nth-child(4)")).click();
		Thread.sleep(1000);
		vars.put("num2", js.executeScript(
				"var warehousingListPage = document.getElementById(\"warehousingListPage\"); var a = warehousingListPage.getElementsByTagName(\"a\"); var page = a.length - 1; var num = 0;  var warehousingList = document.getElementById(\"warehousingList\"); var liLength = warehousingList.getElementsByTagName(\"li\").length; if(page > 0){ num = (page - 1)*10; num += liLength; }else{num = liLength;} return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("num1"), vars.get("num2"))) {
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).getText(), is("所有 v"));
		assertThat(driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).getText(), is("所有 v"));
		assertThat(driver.findElement(By.cssSelector(".layui-form-label:nth-child(5) > span")).getText(), is("所有 v"));
		assertThat(driver.findElement(By.cssSelector(".layui-form-label:nth-child(7) > span")).getText(), is("所有 v"));
	}

	@Test
	public void uTR38cancelButton() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("warehousingSheetSN", driver.findElement(By.cssSelector(".warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".warehousingSheetSN")).getText(), is(vars.get("warehousingSheetSN").toString()));
	}

	@Test
	public void uTR39searchByphone() throws InterruptedException {
		driver.findElement(By.linkText("商品相关")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("name")).click();
		Thread.sleep(1000);
		vars.put("provider", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.name("mobile")).click();
		Thread.sleep(1000);
		vars.put("phone", js.executeScript("return \"147258369\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.name("mobile")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseProvider .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseProvider .topArea > .layui-input")).sendKeys(vars.get("phone").toString());
		assertThat(driver.findElement(By.cssSelector("#popupProviderList + div tbody tr td:nth-child(4) div")).getText(), is(vars.get("phone").toString()));
	}
}
