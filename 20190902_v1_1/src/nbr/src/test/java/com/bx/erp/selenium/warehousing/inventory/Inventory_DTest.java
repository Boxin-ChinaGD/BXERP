package com.bx.erp.selenium.warehousing.inventory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Inventory_DTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTD1clearInfo() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".inventorySheetID")).getAttribute("value");
			assertThat(value, is(""));
		}
		assertThat(driver.findElement(By.cssSelector(".inventoryCommodityTable tbody tr:nth-child(1) td:nth-child(2)")).getText(), is(""));
	}

	@Test
	public void uTD2deleteInventoryComm() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("commodityBeChoosed", driver.findElement(By.cssSelector("#toChooseComm .footArea strong")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.xpath("//div[@id=\'rightSide\']/div[7]/div[2]/table/tbody/tr"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon-close-fill")).click();
		Thread.sleep(1000);
		vars.put("commodityBeChoosed", js.executeScript("return arguments[0] - 1;", vars.get("commodityBeChoosed")));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon-add-circle")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector("#toChooseComm .footArea strong")).getText(), is(vars.get("commodityBeChoosed").toString()));
	}

	@Test
	public void uTD3deleteInventorySheet1() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTD4deleteInventorySheet2() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
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
		vars.put("deleButtonStatus", js.executeScript("return document.getElementById(\'rightSide\').getElementsByTagName(\'button\')[3].getAttribute(\"disabled\");"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'disabled\')", vars.get("deleButtonStatus"))) {
			System.out.println("测试通过");
		} else {
		}
	}

	@Test
	public void uTD5deleteInventorySheet3() throws InterruptedException {
		driver.findElement(By.linkText("盘点")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
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
		vars.put("deleButtonStatus", js.executeScript("return document.getElementById(\'rightSide\').getElementsByTagName(\'button\')[3].getAttribute(\"disabled\");"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'disabled\')", vars.get("deleButtonStatus"))) {
			System.out.println("测试通过");
		} else {
		}
	}
}
