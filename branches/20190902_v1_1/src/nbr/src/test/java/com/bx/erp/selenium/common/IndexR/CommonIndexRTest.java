package com.bx.erp.selenium.common.IndexR;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CommonIndexRTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTR1logout() throws InterruptedException {
		assertThat(driver.findElement(By.cssSelector(".layui-btn")).getText(), is("退出"));
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-btn")).getText(), is("退出"));
	}

	@Test
	public void uTR2jumpToPurchasingOrderPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("采购")).click();
		assertThat(driver.findElement(By.linkText("采购订单列表")).getText(), is("采购订单列表"));
		driver.findElement(By.linkText("采购订单列表")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-采购订单列表"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-采购订单列表"));
	}

	@Test
	public void uTR3jumpToWarehouseStockPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("库管")).click();
		assertThat(driver.findElement(By.linkText("库管查询")).getText(), is("库管查询"));
		driver.findElement(By.linkText("库管查询")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-库存查询"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-库存查询"));
	}

	@Test
	public void uTR4jumpToWarehousingPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("库管")).click();
		assertThat(driver.findElement(By.linkText("入库")).getText(), is("入库"));
		driver.findElement(By.linkText("入库")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-入库单列表"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-入库单列表"));
	}

	@Test
	public void uTR5jumpToReturnPurchaseCommodityPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("库管")).click();
		assertThat(driver.findElement(By.linkText("采购退货")).getText(), is("采购退货"));
		driver.findElement(By.linkText("采购退货")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-退货"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-退货"));
	}

	@Test
	public void uTR6jumpToInventorySheetPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("库管")).click();
		assertThat(driver.findElement(By.linkText("盘点")).getText(), is("盘点"));
		driver.findElement(By.linkText("盘点")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-盘点单列表"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-盘点单列表"));
	}

	@Test
	public void uTR7jumpToPromotionPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		// driver.findElement(By.linkText("促销")).click();
		assertThat(driver.findElement(By.linkText("满减优惠")).getText(), is("满减优惠"));
		driver.findElement(By.linkText("满减优惠")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("满减优惠"));
		Assert.assertTrue(driver.getPageSource().contains("满减优惠"));
	}

	@Test
	public void uTR8jumpToManagementConditionPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-chart-screen")).click();
		// driver.findElement(By.linkText("报表")).click();
		assertThat(driver.findElement(By.linkText("经营状况")).getText(), is("经营状况"));
		driver.findElement(By.linkText("经营状况")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-经营状况报表"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-经营状况报表"));
	}

	@Test
	public void uTR9jumpToSaleRecordPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-chart-screen")).click();
		// driver.findElement(By.linkText("报表")).click();
		assertThat(driver.findElement(By.linkText("销售记录")).getText(), is("销售记录"));
		driver.findElement(By.linkText("销售记录")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("销售记录"));
		Assert.assertTrue(driver.getPageSource().contains("销售记录"));

	}

	@Test
	public void uTR10jumpToCommodityListPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		assertThat(driver.findElement(By.linkText("商品列表")).getText(), is("商品列表"));
		driver.findElement(By.linkText("商品列表")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-商品列表"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-商品列表"));
	}

	@Test
	public void uTR11jumpToCommodityAboutPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		assertThat(driver.findElement(By.linkText("商品相关")).getText(), is("商品相关"));
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-商品相关"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-商品相关"));

	}

	@Test
	public void uTR12jumpToCommodityHistoryPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		assertThat(driver.findElement(By.linkText("修改记录")).getText(), is("修改记录"));
		driver.findElement(By.linkText("修改记录")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-商品修改记录"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-商品修改记录"));
	}

	@Test
	public void uTR13jumpToVipManagePage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		// driver.findElement(By.linkText("会员与积分")).click();
		assertThat(driver.findElement(By.linkText("会员管理")).getText(), is("会员管理"));
		driver.findElement(By.linkText("会员管理")).click();
		assertThat(driver.findElement(By.linkText("会员卡管理")).getText(), is("会员卡管理"));
		driver.findElement(By.linkText("会员卡管理")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-会员卡"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-会员卡"));

	}

	@Test
	public void uTR14jumpToBonusHistoryPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		// driver.findElement(By.linkText("会员与积分")).click();
		assertThat(driver.findElement(By.linkText("积分历史")).getText(), is("积分历史"));
		driver.findElement(By.linkText("积分历史")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-积分历史"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-积分历史"));
	}

	@Test
	public void uTR15jumpToCouponManagePage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		// driver.findElement(By.linkText("优惠券")).click();
		assertThat(driver.findElement(By.linkText("优惠券管理")).getText(), is("优惠券管理"));
		driver.findElement(By.linkText("优惠券管理")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-优惠券管理"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-优惠券管理"));
	}

	@Test
	public void uTR16jumpToStaffManagePage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		// driver.findElement(By.linkText("员工资料")).click();
		assertThat(driver.findElement(By.linkText("员工管理")).getText(), is("员工管理"));
		driver.findElement(By.linkText("员工管理")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-员工列表"));
		Assert.assertTrue(driver.getPageSource().contains("BoXin-员工列表"));

	}

	@Test
	public void uTR17jumpToUpdatePasswordPage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		assertThat(driver.findElement(By.linkText("修改密码")).getText(), is("修改密码"));
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("BoXin-修改密码"));
		Thread.sleep(5000);
		Assert.assertTrue(driver.getPageSource().contains("修改密码"));
	}

	@Test
	public void uTR18jumpToMiniprogramQRCodePage() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		assertThat(driver.findElement(By.linkText("微信小程序二维码")).getText(), is("微信小程序二维码"));
		driver.findElement(By.linkText("微信小程序二维码")).click();
		driver.switchTo().frame(1);
		// assertThat(driver.getTitle(), is("微信二维码"));
		Assert.assertTrue(driver.getPageSource().contains("微信二维码"));
	}

	@Test
	public void uTR19shrinkSideNav() throws InterruptedException {
		// 已经注释
		// {
		// WebDriverWait wait = new WebDriverWait(driver, 1);
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".BX_title")));
		// }
		// {
		// WebElement element = driver.findElement(By.cssSelector(".shrinkNavDiv"));
		// Actions builder = (Actions) new Actions(driver); // TODO 编译不通过，修改过导入包
		// ((Actions) builder).moveToElement(element).perform(); // TODO 编译不通过，修改过导入包
		// }
		// driver.findElement(By.cssSelector(".layui-icon-shrink-right")).click();
		// {
		// WebDriverWait wait = new WebDriverWait(driver, 1);
		// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".BX_title")));
		// }
	}

	@Test
	public void uTR20showSideNav() {
		// 已注释
		// {
		// WebDriverWait wait = new WebDriverWait(driver, 1);
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".BX_title")));
		// }
		// {
		// WebElement element = driver.findElement(By.cssSelector(".shrinkNavDiv"));
		// Action builder = (Action) new Actions(driver); // TODO 编译不通过，修改过导入包
		// ((Actions) builder).moveToElement(element).perform(); // TODO 编译不通过，修改过导入包
		// }
		// driver.findElement(By.cssSelector(".layui-icon-shrink-right")).click();
		// {
		// WebDriverWait wait = new WebDriverWait(driver, 1);
		// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".BX_title")));
		// }
		// driver.findElement(By.cssSelector(".layui-icon-spread-left")).click();
		// {
		// WebDriverWait wait = new WebDriverWait(driver, 1);
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".BX_title")));
		// }
		// driver.close();
	}
}
