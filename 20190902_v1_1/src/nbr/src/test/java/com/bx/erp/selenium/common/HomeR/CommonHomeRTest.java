package com.bx.erp.selenium.common.HomeR;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CommonHomeRTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		setOperatorType(EnumOperatorType.EOT_BOSS.getIndex());
		super.setUp();
	}

	@Test
	public void uTR3jumpToCommodityPage() throws InterruptedException {
		driver.switchTo().frame(0);
		assertThat(driver.findElement(By.cssSelector("li:nth-child(1) > span")).getText(), is("新建商品"));
		driver.findElement(By.cssSelector("li:nth-child(1) > span")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-商品列表"));
		// TODO 暂时未找到更好的方法判断iframe的title
		Assert.assertTrue(driver.getPageSource().contains("BoXin-商品列表"));
	}

	@Test
	public void uTR4jumpToPromotionPage() throws InterruptedException {
		driver.switchTo().frame(0);
		assertThat(driver.findElement(By.cssSelector("li:nth-child(2) > span")).getText(), is("新建促销活动"));
		driver.findElement(By.cssSelector("li:nth-child(2) > span")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("满减优惠"));
		Assert.assertTrue(driver.getPageSource().contains("满减优惠"));
	}

	@Test
	public void uTR5jumpToPurchasingOrderPage() throws InterruptedException {
		driver.switchTo().frame(0);
		assertThat(driver.findElement(By.cssSelector("li:nth-child(3) > span")).getText(), is("新建采购订单"));
		driver.findElement(By.cssSelector("li:nth-child(3) > span")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-采购订单列表"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-采购订单列表"));
	}

	@Test
	public void uTR6jumpToWarehousingPage() throws InterruptedException {
		driver.switchTo().frame(0);
		assertThat(driver.findElement(By.cssSelector("li:nth-child(4) > span")).getText(), is("新建入库单"));
		driver.findElement(By.cssSelector("li:nth-child(4) > span")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-入库单列表"));
		// TODO 暂时未找到更好的方法判断iframe的title
		Assert.assertTrue(driver.getPageSource().contains("BoXin-入库单列"));
	}

	@Test
	public void uTR7jumpToReturnCommodityPage() throws InterruptedException {
		driver.switchTo().frame(0);
		assertThat(driver.findElement(By.cssSelector("li:nth-child(5) > span")).getText(), is("新建退货单"));
		driver.findElement(By.cssSelector("li:nth-child(5) > span")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-退货"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-退货"));
	}

	@Test
	public void uTR8jumpToInventorySheetPage() throws InterruptedException {
		driver.switchTo().frame(0);
		assertThat(driver.findElement(By.cssSelector("li:nth-child(6) > span")).getText(), is("新建盘点单"));
		driver.findElement(By.cssSelector("li:nth-child(6) > span")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-盘点单列表"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-盘点单列表"));
	}
}
