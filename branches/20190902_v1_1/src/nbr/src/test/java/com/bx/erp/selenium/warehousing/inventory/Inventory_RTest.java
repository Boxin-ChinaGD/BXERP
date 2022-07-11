package com.bx.erp.selenium.warehousing.inventory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Inventory_RTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTR1search1() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(1)")).click();
	}

	@Test
	public void uTR2search2() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(2)")).click();
	}

	@Test
	public void uTR3search3() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-form-label > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(3)")).click();
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector("#inventorySheetList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("count", js.executeScript(
				"function test(text){ 		 		var num = parseInt(text.replace(/[^0-9]/ig, \"\")); 		 		num += 1; 		 		text = \"共 \" + num + \" 条\"; 		 		return text; 	 	} 	 	return test(arguments[0]);",
				vars.get("count")));
		driver.findElement(By.cssSelector(".layui-form-label > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#inventorySheetList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR4search4() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(4)")).click();
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector("#inventorySheetList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("count", js.executeScript(
				"function test(text){ 		 		var num = parseInt(text.replace(/[^0-9]/ig, \"\")); 		 		num += 1; 		 		text = \"共 \" + num + \" 条\"; 		 		return text; 	 	} 	 	return test(arguments[0]);",
				vars.get("count")));
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#inventorySheetList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR5search5() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("旺旺");
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector("#inventorySheetList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).clear();
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("旺旺");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("旺旺");
	}

	@Test
	public void uTR6search6() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("#");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，支持输入的符号有：（）()_-——"));
	}

	@Test
	public void uTR7search7() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("count", driver.findElement(By.cssSelector("#inventorySheetList + div .layui-laypage-count")).getText());
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		;
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#leftSide > .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#inventorySheetList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR8search8() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.id("leftSide")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("0123456789012345678901234567890123");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#leftSide .layui-icon")).click();
	}

	@Test
	public void uTR9search9() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
	}

	@Test
	public void uTR10search10() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-anim > dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("commodityName", driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(2)")).getText());
		vars.put("scope", driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-unselect .layui-input")).getAttribute("value"));
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'leftSide\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		assertThat(driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(2)")).getText(), is(vars.get("commodityName").toString()));
		{
			String value = driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-unselect .layui-input")).getAttribute("value");
			assertThat(value, is(vars.get("scope").toString()));
		}
	}

	@Test
	public void uTR11search11() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-anim > dd:nth-child(4)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).sendKeys("盘点总结");
		Thread.sleep(1000);
		vars.put("commodityName", driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(2)")).getText());
		vars.put("scope", driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-unselect .layui-input")).getAttribute("value"));
		vars.put("remark", driver.findElement(By.cssSelector("#rightSide .layui-textarea")).getAttribute("value"));
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-anim > dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).clear();
		driver.findElement(By.name("remark")).sendKeys("!!!");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'leftSide\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(2)")).getText(), is(vars.get("commodityName").toString()));
		{
			String value = driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-unselect .layui-input")).getAttribute("value");
			assertThat(value, is(vars.get("scope").toString()));
		}
		{
			String value = driver.findElement(By.cssSelector("#rightSide .layui-textarea")).getAttribute("value");
			assertThat(value, is(vars.get("remark").toString()));
		}
	}

	@Test
	public void uTR12search12() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-anim > dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).sendKeys("我是盘点总结");
		Thread.sleep(1000);
		vars.put("commodityName", driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(2)")).getText());
		vars.put("scope", driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-unselect .layui-input")).getAttribute("value"));
		vars.put("remark", driver.findElement(By.cssSelector("#rightSide .layui-textarea")).getAttribute("value"));
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).clear();
		driver.findElement(By.name("remark")).sendKeys("???");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'leftSide\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(2)")).getText(), is("vars.get(\"commodityName\").toString()"));
		{
			String value = driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-unselect .layui-input")).getAttribute("value");
			assertThat(value, is(vars.get("scope").toString()));
		}
		{
			String value = driver.findElement(By.cssSelector("#rightSide .layui-textarea")).getAttribute("value");
			assertThat(value, is(vars.get("remark").toString()));
		}
	}

	@Test
	public void uTR13commCatrgory() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		vars.put("categoryParentLength", js.executeScript("return document.getElementsByClassName(\'leftRegion\')[0].getElementsByClassName(\'layui-nav-item\').length;"));
		driver.findElement(By.cssSelector(".showAllCommCategory")).click();
		Thread.sleep(1000);
		vars.put("categoryParentLengthBeChoosed", js.executeScript("return document.getElementsByClassName(\'leftRegion\')[0].getElementsByClassName(\'layui-nav-itemed\').length;"));
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("categoryParentLength"), vars.get("categoryParentLengthBeChoosed"))) {
			System.out.println("商品大类的总数与已选择的商品大类总数一致");
		} else {
			System.out.println("商品大类的总数与已选择的商品大类总数不一致");
		}
		assertThat(driver.findElement(By.cssSelector(".showAllCommCategory")).getText(), is("全部关闭"));
		driver.findElement(By.cssSelector(".showAllCommCategory")).click();
		Thread.sleep(1000);
		vars.put("categoryParentLengthBeChoosed", js.executeScript("return document.getElementsByClassName(\'leftRegion\')[0].getElementsByClassName(\'layui-nav-itemed\').length;"));
		if ((Boolean) js.executeScript("return (arguments[0] == 0)", vars.get("categoryParentLengthBeChoosed"))) {
			System.out.println("商品大类已全部取消选择状态");
		} else {
			System.out.println("商品大类没有全部取消选择状态");
		}
		assertThat(driver.findElement(By.cssSelector(".showAllCommCategory")).getText(), is("全部展开"));
	}

	@Test
	public void uTR14queryCommodityByCategory() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.linkText("蛋肉家禽")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("家禽")).click();
		Thread.sleep(1000);
		vars.put("commodityCount", driver.findElement(By.cssSelector("#layui-laypage-4 > .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("12346589");
		Thread.sleep(1000);
		vars.put("commodityName", js.executeScript("return \"飞龙在天\" + parseInt(Math.random()*1000);"));
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).sendKeys("克");
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		vars.put("commodityCount", js.executeScript(
				"function test(text){ 		 		var num = parseInt(text.replace(/[^0-9]/ig, \"\")); 		 		num += 1; 		 		text = \"共 \" + num + \" 条\"; 		 		return text; 	 	} 	 	return test(arguments[0]);",
				vars.get("commodityCount")));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("家禽")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#layui-laypage-6 > .layui-laypage-count")).getText(), is(vars.get("commodityCount").toString()));
	}

	@Test
	public void uTR15queryCommodityByKeyword1() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("飞龙");
		Thread.sleep(1000);
		vars.put("commodityCount", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("12346589");
		Thread.sleep(1000);
		vars.put("commodityName", js.executeScript("return \"飞龙在天\" + parseInt(Math.random()*1000);"));
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).sendKeys("克");
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		vars.put("commodityCount", js.executeScript(
				"function test(text){ 		 		var num = parseInt(text.replace(/[^0-9]/ig, \"\")); 		 		num += 1; 		 		text = \"共 \" + num + \" 条\"; 		 		return text; 	 	} 	 	return test(arguments[0]);",
				vars.get("commodityCount")));
		driver.switchTo().defaultContent();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText(), is(vars.get("commodityCount").toString()));
	}

	@Test
	public void uTR16queryCommodityByKeyword2() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(51000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseComm .rightRegion .topArea input")).sendKeys("$%?");
	}

	@Test
	public void uTR17queryCommodityByKeyword3() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseComm .rightRegion .topArea input")).clear();
		driver.findElement(By.cssSelector("#toChooseComm .rightRegion .topArea input")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("commodityCount", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
	}

	@Test
	public void uTR18queryCommodityByKeyword4() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion .layui-input")).sendKeys("012345678901234567890123456789012345678901234567890123456789健健康康的哦");
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		{
			List<WebElement> elements = driver.findElements(By.cssSelector("#popupCommodityList + div .layui-laypage-count"));
			assert (elements.size() == 0);
		}
	}

	@Test
	public void uTR19queryCommodityByKeywordAndCategory1() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.linkText("蔬菜水果")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("鲜鲜水果")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("龙");
		Thread.sleep(1000);
		vars.put("commodityCount", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-nav-item:nth-child(1) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("12346589");
		Thread.sleep(1000);
		vars.put("commodityName", js.executeScript("return \"飞龙在天\" + parseInt(Math.random()*1000);"));
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).sendKeys("克");
		driver.findElement(By.name("priceRetail")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(9)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("commodityCount", js.executeScript(
				"function test(text){ 		 		var num = parseInt(text.replace(/[^0-9]/ig, \"\")); 		 		num += 1; 		 		text = \"共 \" + num + \" 条\"; 		 		return text; 	 	} 	 	return test(arguments[0]);",
				vars.get("commodityCount")));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.linkText("鲜鲜水果")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).sendKeys("龙");
	}

	@Test
	public void uTR20queryCommodityByKeywordAndCategory2() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.linkText("蔬菜水果")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("鲜鲜水果")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseComm .rightRegion .topArea input")).sendKeys("$^^");
	}

	@Test
	public void uTR21queryCommodityByKeywordAndCategory3() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.linkText("蔬菜水果")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("鲜鲜水果")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseComm .rightRegion .topArea input")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("commodityCount", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-nav-item:nth-child(1) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("12346589");
		Thread.sleep(1000);
		vars.put("commodityName", js.executeScript("return \"飞龙在天\" + parseInt(Math.random()*1000);"));
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.name("specification")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).sendKeys("克");
		driver.findElement(By.name("priceRetail")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(9)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		vars.put("commodityCount", js.executeScript(
				"function test(text){ 		 		var num = parseInt(text.replace(/[^0-9]/ig, \"\")); 		 		num += 1; 		 		text = \"共 \" + num + \" 条\"; 		 		return text; 	 	} 	 	return test(arguments[0]);",
				vars.get("commodityCount")));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.linkText("鲜鲜水果")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#toChooseComm .rightRegion .topArea input")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText(), is(vars.get("commodityCount").toString()));
	}

	@Test
	public void uTR22queryCommodityByKeywordAndCategory4() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("日用百货"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("蔬菜水果")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("鲜鲜水果")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion .layui-input")).sendKeys("012345678901234567890123456789012345678901234567890123456789健健康康的哦");
		driver.findElement(By.cssSelector(".rightRegion > .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		{
			List<WebElement> elements = driver.findElements(By.cssSelector("#popupCommodityList + div .layui-laypage-count"));
			assert (elements.size() == 0);
		}
	}

	@Test
	public void uTR23jumpToCommodityListPage() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".leftRegion > .layui-btn")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		assertThat(driver.getTitle(), is("BoXin-商品列表"));
	}

	@Test
	public void uTR24commodityPopupToChooseCommodity() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("commodityBeChoosed", driver.findElement(By.cssSelector("#toChooseComm .footArea strong")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseComm .footArea strong")).getText(), is(vars.get("commodityBeChoosed").toString()));
	}

	@Test
	public void uTR25clickCancelButton() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("inventorySN", driver.findElement(By.cssSelector("#inventorySheetList + div .trChoosed td:nth-child(1) span")).getText());
		driver.findElement(By.xpath("//div[@id=\'leftSide\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTR26closePopup1() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		vars.put("commodityBeChoosed", driver.findElement(By.cssSelector("#toChooseComm .footArea strong")).getText());
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(5) .layui-input")).sendKeys("5");
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[7]/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[7]/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".closePopupPage")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		assertThat(driver.findElement(By.cssSelector("#toChooseComm .footArea strong")).getText(), is(vars.get("commodityBeChoosed").toString()));
	}

	@Test
	public void uTR27closePopup2() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		vars.put("commodityBeChoosed", driver.findElement(By.cssSelector("#toChooseComm .footArea strong")).getText());
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[8]/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(8) .layui-input")).sendKeys("8");
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#toChooseComm .footArea strong")).getText(), is(vars.get("commodityBeChoosed").toString()));
	}

	@Test
	public void uTR31queryCommodityByKeywordnoSuchData() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("测试查无盘点单的情况");
		driver.findElement(By.cssSelector("#leftSide .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("查无盘点单"));
		{
			WebElement element = driver.findElement(By.name("remark"));
			Boolean isEditable = element.isEnabled() && element.getAttribute("readonly") == null;
			assertFalse(isEditable);
		}
	}

	@Test
	public void uTR33queryCommodityByCommodityName() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("_");
		driver.findElement(By.id("inventoryMain")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("12346589");
		vars.put("commodityName", js.executeScript("return \"_ABC\" + parseInt(Math.random()*1000);"));
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys(vars.get("commodityName").toString());
		vars.put("specification", js.executeScript("return \"克\";"));
		driver.findElement(By.name("specification")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("specification")).sendKeys(vars.get("specification").toString());
		driver.findElement(By.name("priceRetail")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("priceRetail")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建商品成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-tab-title > li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建盘点单成功"));
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("_");
		assertThat(driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(2)")).getText(), is(vars.get("commodityName").toString()));
	}
}
