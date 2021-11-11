package com.bx.erp.selenium.vip;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.selenium.BaseSeleniumTest;

public class CouponManage_DTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTD2terminationCoupon1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
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
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定创建该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建优惠券成功"));
		driver.findElement(By.cssSelector(".terminateCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定终止该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("终止优惠券成功"));
	}

	@Test
	public void uTD4terminationCoupon3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("22");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("beginDateTime")).sendKeys("2020/03/23 00:00:00");
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).sendKeys("2020/03/26 00:00:00");
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定创建该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建优惠券成功"));
		driver.findElement(By.cssSelector(".terminateCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定终止该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("终止优惠券成功"));
	}
}
