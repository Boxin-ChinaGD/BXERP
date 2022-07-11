package com.bx.erp.selenium.vip;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CouponManage_RTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTR1queryCommodityInPopup1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("$%^");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，支持输入的符号有：（）()_-——"));
	}

	@Test
	public void uTR2queryCommodityInPopup2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		vars.put("commodityName", driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/span")).getText());
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/span")).getText(), is("vars.get(\"commodityName\").toString()"));
	}

	@Test
	public void uTR3queryCommodityInPopup3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("barcodes")).sendKeys("123456789");
		vars.put("commodityName", js.executeScript("return \"随机商品\" + Math.floor(Math.random() * 999);"));
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
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("commodityName").toString());
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/span")).getText(), is(vars.get("commodityName").toString()));
	}

	@Test
	public void uTR4queryCommodityInPopup4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-cart-simple")).click();
		driver.findElement(By.linkText("库管查询")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-checked > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector(".layui-laypage-count")).getText());
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-count:nth-child(9)")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR14showCommodityCategoryInPopup() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		vars.put("categoryLength", js.executeScript("return document.getElementsByClassName(\"leftRegion\")[0].getElementsByClassName(\"layui-nav-item\").length;"));
		System.out.println(vars.get("categoryLength").toString());
		driver.findElement(By.cssSelector(".showAllCommCategory")).click();
		Thread.sleep(1000);
		vars.put("categoryShowLength", js.executeScript("return document.getElementsByClassName(\"leftRegion\")[0].getElementsByClassName(\"layui-nav-itemed\").length;"));
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("categoryLength"), vars.get("categoryShowLength"))) {
			System.out.println("测试通过");
		} else {
		}
	}

	@Test
	public void uTR15closeCommodityCategoryInPopup() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".showAllCommCategory")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".showAllCommCategory")).click();
		Thread.sleep(1000);
		vars.put("categoryShowLength", js.executeScript("return document.getElementsByClassName(\"leftRegion\")[0].getElementsByClassName(\"layui-nav-itemed\").length;"));
		if ((Boolean) js.executeScript("return (arguments[0] == 0)", vars.get("categoryShowLength"))) {
			System.out.println("测试通过");
		} else {
		}
	}

	@Test
	public void uTR16automaticChooseCommodityInPopup() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector("span > strong")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon-add-circle")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR17showCoupon() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\'couponMain\']/div[2]/div/div/div[2]/table/tbody/tr/td/div")));
		}
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("layui-table-page1")));
		}
	}

	@Test
	public void uTR18retrieveCoupon1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"全部商品可用\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定创建该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建优惠券成功"));
		driver.findElement(By.xpath("//div[@id=\'couponMain\']/div[2]/div/div/div[2]/table/tbody/tr[2]/td[7]/div/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'couponMain\']/div[2]/div/div/div[2]/table/tbody/tr/td[7]/div/span")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("title")).getAttribute("value");
			assertThat(value, is(vars.get("couponName").toString()));
		}
	}

	@Test
	public void uTR19retrieveCoupon2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"部分商品可用\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("commodityName", driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/span")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定创建该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建优惠券成功"));
		driver.findElement(By.xpath("//div[@id=\'couponMain\']/div[2]/div/div/div[2]/table/tbody/tr[2]/td[7]/div/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'couponMain\']/div[2]/div/div/div[2]/table/tbody/tr/td[7]/div/span")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("title")).getAttribute("value");
			assertThat(value, is(vars.get("couponName").toString()));
		}
		vars.put("commodityName", js.executeScript("return arguments[0].split(\"/\")[0];", vars.get("commodityName")));
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(3) td:nth-child(2)")).getText(), is(vars.get("commodityName").toString()));
	}
}
