package com.bx.erp.selenium.warehousing.manage;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class manage_CTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTC1checkWarehousingNo() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("thead .laytable-cell-3-0-0")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("999", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[4]; td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"999\"; } test();"));
		vars.put("abcdefg", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[4]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"abcdefg\"; } test();"));
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的入库数量"));
		vars.put("2147483648", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[4]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"2147483648\"; } test();"));
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的入库数量"));
		vars.put("0.985", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[4]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"0.985\"; } test();"));
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的入库数量"));
	}

	@Test
	public void uTC2checkWarehousingPrice() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("123.456", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[5]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"123.456\"; } test();"));
		vars.put("哈里路亚", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[5]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"哈里路亚\"; } test();"));
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的入库价"));
		vars.put("2147483648", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[5]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"2147483648\"; } test();"));
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的入库价"));
		vars.put("helloWorld", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[5]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"helloWorld\"; } test();"));
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的入库价"));
	}

	@Test
	public void uTC3createWarehousing1() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".warehousingManage:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请选择入库商品"));
	}

	@Test
	public void uTC4createWarehousing2() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(2) .layui-input")).sendKeys("6");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(3) .layui-input")).sendKeys("3");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[4]/div/div/button[2]"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'layui-layer5\']/div")).getText(), is("创建入库单成功"));
	}

	@Test
	public void uTC5createWarehousing3() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[8]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("Math.round(Math.random()*9999)", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[0]; 	var td = tr.getElementsByTagName(\"td\")[4]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = Math.round(Math.random()*9999); } test();"));
		vars.put("Math.round(Math.random()*9999)", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[2]; 	var td = tr.getElementsByTagName(\"td\")[4]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = Math.round(Math.random()*9999); } test();"));
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTC6createWarehousing4() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-4 > a:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[8]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("(Math.random()*9999).toFixed(2)", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[0]; 	var td = tr.getElementsByTagName(\"td\")[5]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = (Math.random()*9999).toFixed(2); } test();"));
		vars.put("(Math.random()*9999).toFixed(2)", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[2]; 	var td = tr.getElementsByTagName(\"td\")[5]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = (Math.random()*9999).toFixed(2); } test();"));
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'layui-layer5\']/div")).getText(), is("创建入库单成功"));
	}

	@Test
	public void uTC7createWarehousing5() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("Math.round(Math.random()*9999)", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[4]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = Math.round(Math.random()*9999); } test();"));
		vars.put("(Math.random()*9999).toFixed(2)", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[5]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = (Math.random()*9999).toFixed(2); } test();"));
		vars.put("(Math.random()*9999).toFixed(2)", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[0]; 	var td = tr.getElementsByTagName(\"td\")[5]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = (Math.random()*9999).toFixed(2); } test();"));
		vars.put("Math.round(Math.random()*9999)", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[2]; 	var td = tr.getElementsByTagName(\"td\")[4]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = Math.round(Math.random()*9999); } test();"));
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'layui-layer5\']/div")).getText(), is("创建入库单成功"));
	}

	@Test
	public void uTC8createWarehousing6() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'layui-layer5\']/div")).getText(), is("创建入库单成功"));
		assertThat(driver.findElement(By.cssSelector(".providerName")).getText(), is("默认供应商"));
	}

	@Test
	public void uTC9createWarehousing7() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseProvider\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[2]/div")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.cssSelector("#popupProviderList + div table tbody tr:nth-child(5) td:nth-child(2) div")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".warehousingSheetInfo .baseInfo p:nth-child(2) span:nth-child(1) strong")).getText(), is("vars.get(\"providerName\").toString()"));
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'layui-layer6\']/div")).getText(), is("创建入库单成功"));
	}

	@Test
	public void uTC10createWarehousing8() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("(Math.random()*9999).toFixed(2)", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[0]; 	var td = tr.getElementsByTagName(\"td\")[6]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = (Math.random()*9999).toFixed(2); } test();"));
		vars.put("(Math.random()*9999).toFixed(2)", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[6]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = (Math.random()*9999).toFixed(2); } test();"));
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'layui-layer5\']/div")).getText(), is("创建入库单成功"));
	}

	@Test
	public void uTC11checkWarehousingAmount() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("666.999", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[6]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"666.999\"; } test();"));
		vars.put("hello", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[6]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"hello\"; } test();"));
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的入库金额"));
		vars.put("2147483648", js.executeScript(
				"function test(){ 	var div = document.getElementsByClassName(\"warehousingCommodityList\")[0]; 	var tr = div.getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\")[1]; 	var td = tr.getElementsByTagName(\"td\")[6]; 	td.getElementsByTagName(\"input\")[0].focus(); 	td.getElementsByTagName(\"input\")[0].value = \"2147483648\"; } test();"));
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的入库金额"));
	}

	@Test
	public void uTC1210testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(5000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText1")));
		driver.switchTo().defaultContent();
		Thread.sleep(1000);
		driver.findElement(By.linkText("库管查询")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		vars.put("getSumText2", driver.findElement(By.cssSelector("#commodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum2"))) {
		}
	}

	@Test
	public void uTC1211testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).sendKeys("11111111112222222222333333333344444444445555555555666666666677777");
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTC1212testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("3"));
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("2"));
	}

	@Test
	public void uTC1213testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("10"));
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("0"));
	}

	@Test
	public void uTC1214testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("3"));
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-table-hover .layui-input")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("1"));
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[7]/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[7]/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("1"));
		}
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("3"));
	}

	@Test
	public void uTC1215testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(4) .layui-input")).sendKeys("6");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[7]/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(7) .layui-input")).clear();
		driver.findElement(By.cssSelector("tr:nth-child(7) .layui-input")).sendKeys("5");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[7]/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/button")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[7]/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("4"));
		}
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("1"));
	}

	@Test
	public void uTC1216testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("123");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/input")).clear();
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/input")).sendKeys("66");
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/input")).clear();
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td[4]/div/div/input")).sendKeys("ddgdsdgs");
		driver.findElement(By.cssSelector("#toChooseCommodity > .rightRegion")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[8]/td[4]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[9]/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/input")).clear();
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[9]/td[4]/div/div/input")).sendKeys("-123456");
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("1"));
	}

	@Test
	public void uTC1217testCommodityPopup() throws InterruptedException {
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
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("3"));
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("trLength", js.executeScript("var list = document.getElementsByClassName(\"warehousingCommodityList\")[0]; var tr = list.getElementsByTagName(\"tr\"); return tr.length;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 3)", vars.get("trLength"))) {
		}
	}

	@Test
	public void uTC1218testCommodityPopup() throws InterruptedException {
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
		Thread.sleep(3000);
		vars.put("trLength", js.executeScript("var list = document.getElementsByClassName(\"warehousingCommodityList\")[0]; var tr = list.getElementsByTagName(\"tr\"); return tr.length;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 2)", vars.get("trLength"))) {
		}
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[3]/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-table-hover .addNum")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-table-hover .addNum"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
		driver.findElement(By.cssSelector("thead .laytable-cell-4-0-0")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".exitChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("trLength", js.executeScript("var list = document.getElementsByClassName(\"warehousingCommodityList\")[0]; var tr = list.getElementsByTagName(\"tr\"); return tr.length;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 2)", vars.get("trLength"))) {
		}
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon-add-circle")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("2"));
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("0"));
	}

	@Test
	public void uTC1219testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//a[contains(text(),\'2\')])[2]")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).sendKeys("100");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("100");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("1"));
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td[4]/div/div/input")).sendKeys("10");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td[4]/div/div/input")).sendKeys("10");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).sendKeys("10");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		vars.put("trLength", js.executeScript("var list = document.getElementsByClassName(\"warehousingCommodityList\")[0]; var tr = list.getElementsByTagName(\"tr\"); return tr.length;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 2)", vars.get("trLength"))) {
		}
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//a[contains(text(),\'2\')])[2]")).click();
		Thread.sleep(3000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("100"));
		}
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("100"));
		}
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .rightRegion .footArea strong")).getText(), is("2"));
	}

	@Test
	public void uTC121testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .showAll")).getText(), is("全部展开"));
		driver.findElement(By.cssSelector("#toChooseCommodity .showAll")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .showAll")).getText(), is("全部关闭"));
	}

	@Test
	public void uTC1220testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseCommodity > .leftRegion > .layui-btn")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.xpath("//div[@id=\'middlePart\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#commodityMain .layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.getTitle(), is("BoXin-商品列表"));
	}

	@Test
	public void uTC1221testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-next:nth-child(7) > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(8) > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(8) > .layui-input")).sendKeys("1");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(8) > .layui-laypage-btn")).click();
	}

	@Test
	public void uTC122testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .showAll")).getText(), is("全部展开"));
		driver.findElement(By.cssSelector("#toChooseCommodity .showAll")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .showAll")).getText(), is("全部关闭"));
		driver.findElement(By.cssSelector("#toChooseCommodity .showAll")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseCommodity .showAll")).getText(), is("全部展开"));
	}

	@Test
	public void uTC123testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseCommodity .layui-this > a")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(3000);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText1")));
		driver.findElement(By.linkText("休闲食品")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("糖果/巧克力")).click();
		Thread.sleep(2000);
		vars.put("getSumText2", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText2")));
		driver.switchTo().defaultContent();
		driver.findElement(By.linkText("库管查询")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(2);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		vars.put("getSumText3", driver.findElement(By.cssSelector("#commodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum3", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText3")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum3"))) {
		}
		driver.findElement(By.linkText("休闲食品")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("糖果/巧克力")).click();
		Thread.sleep(1000);
		vars.put("getSumText4", driver.findElement(By.cssSelector("#commodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum4", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText4")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum2"), vars.get("getSum4"))) {
		}
	}

	@Test
	public void uTC124testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).sendKeys("^*&*");
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，支持输入的符号有：（）()_-——"));
	}

	@Test
	public void uTC125testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).sendKeys("星");
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText1")));
		driver.switchTo().defaultContent();
		driver.findElement(By.linkText("库管查询")).click();
		Thread.sleep(3000);
		driver.switchTo().frame(2);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("星");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(2000);
		vars.put("getSumText2", driver.findElement(By.cssSelector("#commodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum2"))) {
		}
	}

	@Test
	public void uTC126testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText1")));
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("getSumText2", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum2"))) {
		}
	}

	@Test
	public void uTC127testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).sendKeys("11111111112222222222333333333344444444445555555555666666666677777");
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTC128testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).sendKeys("%^&7");
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，支持输入的符号有：（）()_-——"));
	}

	@Test
	public void uTC129testCommodityPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-input")).sendKeys("A");
		driver.findElement(By.cssSelector("#toChooseCommodity .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText1")));
		driver.switchTo().defaultContent();
		driver.findElement(By.linkText("库管查询")).click();
		Thread.sleep(3000);
		driver.switchTo().frame(2);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("A");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		vars.put("getSumText2", driver.findElement(By.cssSelector("#commodityList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum2"))) {
		}
	}

	@Test
	public void uTC1310testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#button > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseProvider\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.cssSelector("#popupProviderList + div tbody .layui-table-click td:nth-child(2) div")).getText());
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is("默认供应商"));
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseProvider\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[2]/div")).click();
		Thread.sleep(2000);
		vars.put("providerName2", driver.findElement(By.cssSelector("#popupProviderList + div tbody .layui-table-click td:nth-child(2) div")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is("vars.get(\"providerName2\").toString()"));
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#popupProviderList + div tbody .layui-table-click td:nth-child(2) div")).getText(), is(vars.get("providerName2").toString()));
		driver.findElement(By.xpath("//div[@id=\'toChooseProvider\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is(vars.get("providerName2").toString()));
	}

	@Test
	public void uTC1311testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#button > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'layui-laypage-8\']/a[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'layui-laypage-9\']/a/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'layui-laypage-10\']/span[2]/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip:nth-child(5) > .layui-input")).sendKeys("2");
		driver.findElement(By.xpath("//div[@id=\'layui-laypage-10\']/span[2]/button")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("默认区域")).click();
	}

	@Test
	public void uTC131testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseProvider .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseProvider .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseProvider .layui-input")).sendKeys(" ");
		driver.findElement(By.cssSelector("#toChooseProvider .topArea > .layui-icon")).click();
	}

	@Test
	public void uTC132testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("p:nth-child(2) > span:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#popupProviderList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText1")));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("商品相关")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("p:nth-child(12) > .district")).click();
		Thread.sleep(1000);
		vars.put("num", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\"); var a = providerPage.getElementsByTagName(\"a\"); var page = a.length - 2; var num = 0; var provider = document.getElementById(\"provider\"); var pLength = provider.getElementsByTagName(\"p\").length; if(page > 0){ num = (page - 1)*10; num += pLength; }else{ num = pLength; } if(pLength == \"1\"){ var p = provider.getElementsByTagName(\"p\")[0]; var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\"); if(value == \"没有内容，增加试试\"){ num = num - 1; } } return num;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("num"))) {
		}
	}

	@Test
	public void uTC133testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseProvider > .leftRegion > .layui-btn")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector("p:nth-child(28) > .district")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideDistrictSelect > .delete")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("默认区域不可删除"));
	}

	@Test
	public void uTC134testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseProvider .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseProvider .topArea > .layui-input")).sendKeys("默认供应商");
		driver.findElement(By.cssSelector("#toChooseProvider .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#popupProviderList + div .layui-table-page .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);  var num = parseInt(numText);  return num;", vars.get("getSumText1")));
		if ((Boolean) js.executeScript("return (arguments[0] < 1)", vars.get("getSum1"))) {
		}
	}

	@Test
	public void uTC135testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#button > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseProvider .topArea > .layui-input")).sendKeys("qqqqqqqqqqqqqqqqqqqq");
		driver.findElement(By.cssSelector("#toChooseProvider .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#popupProviderList + div .layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTC136testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#button > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		vars.put("value", js.executeScript("return \" \";"));
		driver.findElement(By.cssSelector("#toChooseProvider .topArea > .layui-input")).sendKeys(vars.get("value").toString());
		driver.findElement(By.cssSelector("#toChooseProvider .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#popupProviderList + div .layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTC137testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#button > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is("默认供应商"));
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#toChooseProvider .topArea > .layui-input")).sendKeys("q");
		driver.findElement(By.cssSelector("#toChooseProvider .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is("默认供应商"));
	}

	@Test
	public void uTC138testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#button > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseProvider\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td[2]/div")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.cssSelector("#popupProviderList + div tbody .layui-table-click td:nth-child(2) div")).getText());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is(vars.get("providerName").toString()));
	}

	@Test
	public void uTC139testProviderPopup() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#button > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseProvider\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div")).click();
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.cssSelector("#popupProviderList + div tbody .layui-table-click td:nth-child(2) div")).getText());
		driver.findElement(By.cssSelector(".exitChoosedProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is("默认供应商"));
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseProvider\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[2]/div")).click();
		Thread.sleep(1000);
		vars.put("providerName2", driver.findElement(By.cssSelector("#popupProviderList + div tbody .layui-table-click td:nth-child(2) div")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is("vars.get(\"providerName2\").toString()"));
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#popupProviderList + div tbody .layui-table-click td:nth-child(2) div")).getText(), is(vars.get("providerName2").toString()));
		driver.findElement(By.xpath("//div[@id=\'toChooseProvider\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".exitChoosedProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is(vars.get("providerName2").toString()));
	}

	@Test
	public void uTC14cancelButton() {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("库管")).click();
		driver.findElement(By.linkText("入库")).click();
		driver.switchTo().frame(1);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector("#warehousingListPage a:nth-child(2)")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vars.put("warehousingSheetSN", driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(1)")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertThat(driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText(), is("vars.get(\"warehousingSheetSN\").toString()"));
		driver.close();
	}

	@Test
	public void uTC19createWS() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("warehousingSN", js.executeScript(
				"var date=new Date(); var year=date.getFullYear(); var mon=date.getMonth()+1; var da=date.getDate(); if(mon < 10){ mon=\"0\"+mon;}; if(da< 10){ da=\"0\"+da;}; var warehousingSN= \"RK\" + year + mon + da + \"0001\"; return warehousingSN;"));
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("warehousingSN").toString());
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#warehousingList > li")).click();
	}

	@Test
	public void uTC20createButtonEasyToUseOperation() throws InterruptedException {
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
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr/td[5]/input")).sendKeys("100");
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'warehousingManageMain\']/div[4]/div[2]/div[3]/div[2]/table/tbody/tr[2]/td[5]/input")).sendKeys("10");
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		vars.put("warehousingSheetSN", driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText());
		vars.put("number", js.executeScript("var text = arguments[0].substring(arguments[0].length-4, arguments[0].length); var number = parseInt(text) + 1; return number;", vars.get("warehousingSheetSN")));
		vars.put("string", js.executeScript("var date=new Date(); var year=date.getFullYear(); var mon=date.getMonth()+1; var da=date.getDate(); if(mon < 10){ mon=\"0\"+mon;}; var string = \"RK\" + year + mon + da; return string;"));
		vars.put("is", js.executeScript("var SN = arguments[0]; var string = arguments[1]; return SN.indexOf(string);", vars.get("warehousingSheetSN"), vars.get("string")));
		if ((Boolean) js.executeScript("return (arguments[0] == -1)", vars.get("is"))) {
		}
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("td:nth-child(5) > input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("td:nth-child(5) > input")).sendKeys("100");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确认创建入库单？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建入库单成功"));
		vars.put("warehousingSheetSN2", driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText());
		vars.put("number2", js.executeScript("var text = arguments[0].substring(arguments[1].length-4, arguments[0].length); var number = parseInt(text); return number;", vars.get("warehousingSheetSN2"), vars.get("warehousingSheetSN")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("number"), vars.get("number2"))) {
		}
	}

	@Test
	public void uTC21createButtonEasyToUseOperation() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".warehousingSheetInfo .buttonArea .disabledButton:nth-child(3)")).getText(), is("删除"));
		driver.findElement(By.cssSelector("li:nth-child(1) > .provider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#warehousingList > li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(1) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStatus > li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStaffID > li:nth-child(6)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据格式不正确，入库单号或者采购订单号至少需要输入10位"));
		driver.findElement(By.cssSelector(".layui-form-label:nth-child(3) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".UlStaffID > li:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingListPage .layui-laypage-curr em:nth-child(2)")).getText(), is("1"));
	}

	@Test
	public void uTC22createButtonEasyToUseOperation() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("warehousingSheetSN", driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingListPage .layui-laypage-curr em:nth-child(2)")).getText(), is("1"));
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText(), is(vars.get("warehousingSheetSN").toString()));
	}

	@Test
	public void uTC23createButtonEasyToUseOperation() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vars.put("warehousingSheetSN", driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".warehousingSheetInfo .buttonArea .btnChoosed:nth-child(2)")).getText(), is("保存"));
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".warehousingSheetInfo .buttonArea .btnChoosed:nth-child(2)")).getText(), is("保存"));
	}

	@Test
	public void uTC24createButtonEasyToUseOperation() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vars.put("warehousingSheetSN", driver.findElement(By.cssSelector(".baseInfo .warehousingSheetSN")).getText());
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingListPage .layui-laypage-curr em:nth-child(2)")).getText(), is("1"));
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingListPage .layui-laypage-curr em:nth-child(2)")).getText(), is("1"));
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#warehousingListPage .layui-laypage-curr em:nth-child(2)")).getText(), is("2"));
	}

	@Test
	public void uTC25createEasyToUseOperation() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".addProvider")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseProvider\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedProvider")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is(not("默认供应商")));
		driver.findElement(By.cssSelector(".addWarehousingCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseCommodity\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("length", js.executeScript("var body = document.getElementsByClassName(\"warehousingCommodityList\")[0]; var tr = body.getElementsByTagName(\"tr\").length; return tr;"));
		if ((Boolean) js.executeScript("return (arguments[0] <= 1)", vars.get("length"))) {
		}
		driver.findElement(By.cssSelector(".queryWarehousingSheet")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("queryMsg")).getText(), is("数据格式不正确，入库单号或者采购订单号至少需要输入10位"));
		assertThat(driver.findElement(By.cssSelector(".baseInfo .providerName")).getText(), is("默认供应商"));
		vars.put("length2", js.executeScript("var body = document.getElementsByClassName(\"warehousingCommodityList\")[0]; var tr = body.getElementsByTagName(\"tr\").length; return tr;"));
		if ((Boolean) js.executeScript("return (arguments[0] != 1)", vars.get("length2"))) {
		}
	}

	@Test
	public void uTC26createButtonEasyToUseOperation() throws InterruptedException {
		driver.findElement(By.linkText("入库")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".buttonArea > .layui-btn:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".warehousingSheetInfo .buttonArea .btnChoosed:nth-child(2)")).getText(), is("保存"));
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".warehousingSheetInfo .buttonArea .btnChoosed:nth-child(2)")).getText(), is("保存"));
	}
}
