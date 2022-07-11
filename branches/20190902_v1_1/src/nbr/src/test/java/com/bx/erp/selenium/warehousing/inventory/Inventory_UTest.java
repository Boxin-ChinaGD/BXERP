package com.bx.erp.selenium.warehousing.inventory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Inventory_UTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTU1updateInfo1() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".textInput")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".textInput")).clear();
		driver.findElement(By.cssSelector(".textInput")).sendKeys("1");
		Thread.sleep(1000);
		driver.findElement(By.id("inventoryMain")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".textInput")).getAttribute("value");
			assertThat(value, is("11"));
		}
		driver.findElement(By.cssSelector(".textInput")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".textInput")).clear();
		driver.findElement(By.cssSelector(".textInput")).sendKeys("1");
		Thread.sleep(1000);
		driver.findElement(By.id("inventoryMain")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".textInput")).getAttribute("value");
			assertThat(value, is("111"));
		}
	}

	@Test
	public void uTU2updateInfo2() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".textInput")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".textInput")).sendKeys("3.33");
		Thread.sleep(1000);
		driver.findElement(By.id("inventoryMain")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".textInput")).getAttribute("value");
			assertThat(value, is("3"));
		}
		driver.findElement(By.cssSelector(".textInput")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".textInput")).sendKeys("6.66");
		Thread.sleep(1000);
		driver.findElement(By.id("inventoryMain")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".textInput")).getAttribute("value");
			assertThat(value, is("6"));
		}
	}

	@Test
	public void uTU3updateInfo3() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".textInput")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".textInput")).sendKeys("666");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".inventoryCommodityTable .textInput")).getAttribute("value");
			assertThat(value, is("6661"));
		}
	}

	@Test
	public void uTU4updateInfo4() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建盘点单成功"));
		driver.findElement(By.cssSelector(".textInput")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".textInput")).clear();
		driver.findElement(By.cssSelector(".textInput")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.id("inventoryMain")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("盘点单修改成功"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".inventoryCommodityTable .textInput")).getAttribute("value");
			assertThat(value, is("1"));
		}
	}

	@Test
	public void uTU5updateInventorySheet1() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建盘点单成功"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".textInput")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".textInput")).sendKeys("123");
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).sendKeys("我是盘点总结");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("盘点单修改成功"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".inventoryCommodityTable .textInput")).getAttribute("value");
			assertThat(value, is("1231"));
		}
		{
			String value = driver.findElement(By.cssSelector("#rightSide .layui-textarea")).getAttribute("value");
			assertThat(value, is("我是盘点总结"));
		}
	}

	@Test
	public void uTU6updateInventorySheet2() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).sendKeys("我是已提交的盘点单的盘点总结");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector("#rightSide .layui-textarea")).getAttribute("value");
			assertThat(value, is("我是已提交的盘点单的盘点总结"));
		}
		{
			WebElement element = driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(5) input"));
			Boolean isEditable = element.isEnabled() && element.getAttribute("readonly") == null;
			assertFalse(isEditable);
		}
	}

	@Test
	public void uTU7updateInventorySheet3() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".trChoosed span")).click();
		Thread.sleep(1000);
		vars.put("saveButtonStatus", js.executeScript("return document.getElementById(\'rightSide\').getElementsByTagName(\'button\')[1].getAttribute(\"disabled\");"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'disabled\')", vars.get("saveButtonStatus"))) {
			{
				WebElement element = driver.findElement(By.cssSelector("#rightSide .layui-textarea"));
				Boolean isEditable = element.isEnabled() && element.getAttribute("readonly") == null;
				assertFalse(isEditable);
			}
			{
				WebElement element = driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(5) input"));
				Boolean isEditable = element.isEnabled() && element.getAttribute("readonly") == null;
				assertFalse(isEditable);
			}
			System.out.println("测试通过");
		} else {
		}
	}

	@Test
	public void uTU8submitInventorySheet() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建盘点单成功"));
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("盘点单提交成功"));
	}

	@Test
	public void uTU9aproveInventorySheet() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建盘点单成功"));
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("盘点单提交成功"));
		driver.findElement(By.cssSelector(".changeInSheetStatus")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("审核盘点单成功"));
	}

	@Test
	public void uTU10popupToChooseCommodity1() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("1"));
		}
		driver.findElement(By.cssSelector(".layui-form-checked > .layui-icon")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is(""));
		}
	}

	@Test
	public void uTU11popupToChooseCommodity2() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("commodityBeChoosed", js.executeScript("return document.getElementById(\'toChooseComm\').getElementsByClassName(\'layui-form-checked\').length - 1;"));
		assertThat(driver.findElement(By.cssSelector(".rightRegion .footArea strong")).getText(), is(vars.get("commodityBeChoosed").toString()));
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div/table/thead/tr/th/div/div/i")).click();
		vars.put("commodityBeChoosed", js.executeScript("return document.getElementById(\'toChooseComm\').getElementsByClassName(\'layui-form-checked\').length;"));
		assertThat(driver.findElement(By.cssSelector(".rightRegion .footArea strong")).getText(), is(vars.get("commodityBeChoosed").toString()));
	}

	@Test
	public void uTU12popupToChooseCommodity3() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("1"));
		}
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/button[2]")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("3"));
		}
	}

	@Test
	public void uTU13popupToChooseCommodity4() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/input")).isSelected());
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("1"));
		}
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/button")).click();
		Thread.sleep(1000);
		assertFalse(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/input")).isSelected());
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is(""));
		}
	}

	@Test
	public void uTU14popupToChooseCommodity5() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-input")).sendKeys("6");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rightRegion > .topArea")).click();
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		{
			String value = driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div/div/input")).getAttribute("value");
			assertThat(value, is("61"));
		}
	}

	@Test
	public void uTU15popupToChooseCommodity6() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("commodityInfo", driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/span")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		assertThat(driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(2)")).getText(), is(vars.get("commodityInfo").toString()));
	}

	@Test
	public void uTU18tipsOfUpdateButton() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建盘点单成功"));
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("remark")).sendKeys("我修改了");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(5)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#leftSide > .topArea > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("inventorySN", driver.findElement(By.cssSelector(".trChoosed span")).getText());
		{
			String value = driver.findElement(By.name("sn")).getAttribute("value");
			assertThat(value, is(vars.get("inventorySN").toString()));
		}
	}
}
