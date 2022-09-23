package com.bx.erp.selenium.Promotion;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Promotion_R1Test extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTR1search1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("全场");
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("全场促销活动");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("10");
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("5");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新建满减优惠活动成功"));
		vars.put("count", js.executeScript("function test(text){ var num = parseInt(text.replace(/[^0-9]/ig,\"\")); num += 1; text=\"共 \" + num + \" 条\"; console.log(text); return text;} return test (arguments[0]);", vars.get("count")));
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("全场");
	}

	@Test
	public void uTR2search2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("#@#");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("查无优惠活动"));
	}

	@Test
	public void uTR3search3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("15149");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("100");
		driver.findElement(By.name("excecutionDiscount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionDiscount")).sendKeys("8");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新建满减优惠活动成功"));
		vars.put("count", js.executeScript("function test(text){ var num = parseInt(text.replace(/[^0-9]/ig,\"\")); num += 1; text=\"共 \" + num + \" 条\"; console.log(text); return text;} return test (arguments[0]);", vars.get("count")));
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR4search4() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("count", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("count1", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(3)")).click();
		Thread.sleep(1000);
		vars.put("count2", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(4)")).click();
		Thread.sleep(1000);
		vars.put("count3", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector("b")).click();
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(5)")).click();
		Thread.sleep(1000);
		vars.put("count4", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector("label.layui-form-label")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li")).click();
		Thread.sleep(1000);
		vars.put("count5", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1] && arguments[0] == (arguments[2] + arguments[3] + arguments[4] + arguments[5]))", vars.get("count"), vars.get("count5"), vars.get("count1"), vars.get("count2"),
				vars.get("count3"), vars.get("count4"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTR5search5() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("count1", driver.findElement(By.cssSelector(".layui-laypage-count")).getText());
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline li:nth-child(3)")).getText(), is("进行中"));
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("全场");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(1000);
		vars.put("count2", driver.findElement(By.cssSelector(".layui-laypage-count")).getText());
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline li:nth-child(1)")).getText(), is("所有"));
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-count")).getText(), is(vars.get("count1").toString()));
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline li:nth-child(3)")).getText(), is("进行中"));
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("全场");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-count")).getText(), is(vars.get("count2").toString()));
	}

	@Test
	public void uTR6tablePagenation() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-curr:nth-child(3) > .layui-laypage-em")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-curr:nth-child(3) > em:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-3 > .layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-4 > .layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("5")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-disabled:nth-child(7) > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("4")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.linkText(""));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector("#layui-laypage-6 > .layui-laypage-next > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-7 > .layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-8 > .layui-laypage-skip")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-8 .layui-input")).sendKeys("1");
		driver.findElement(By.cssSelector("#layui-laypage-8 .layui-laypage-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-9 .layui-input")).sendKeys("1ttt");
		driver.findElement(By.cssSelector("#layui-laypage-9 .layui-laypage-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-10 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-10 .layui-input")).sendKeys("tttttttttt");
		driver.findElement(By.cssSelector("#layui-laypage-10 .layui-laypage-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-10 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-10 .layui-input")).sendKeys("4ttttttttt");
		driver.findElement(By.cssSelector("#layui-laypage-10 .layui-laypage-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-11 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-11 .layui-input")).sendKeys("7u");
		driver.findElement(By.cssSelector("#layui-laypage-11 .layui-laypage-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-12 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-12 .layui-input")).sendKeys("57");
		driver.findElement(By.cssSelector("#layui-laypage-12 .layui-laypage-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-13 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-13 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-13 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-13 .layui-input")).sendKeys("1");
		driver.findElement(By.cssSelector("#layui-laypage-13 .layui-laypage-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-14 .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-14 .layui-input")).click();
		Thread.sleep(1000);
		{
			WebElement element = driver.findElement(By.cssSelector("#layui-laypage-14 .layui-input"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
		driver.findElement(By.cssSelector("#layui-laypage-14 .layui-input")).sendKeys("99");
		driver.findElement(By.cssSelector("#layui-laypage-14 .layui-laypage-btn")).click();
	}

	@Test
	public void uTR7search7() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("A");
		Thread.sleep(5000);
		vars.put("count", driver.findElement(By.cssSelector(".layui-laypage-count:nth-child(5)")).getText());
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".designatedCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("默认分类")).click();
		driver.findElement(By.cssSelector(".layui-nav-item:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("A");
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-count:nth-child(5)")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR8search8() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".designatedCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("乐");
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(5000);
		vars.put("count", driver.findElement(By.cssSelector(".layui-laypage-count:nth-child(6)")).getText());
		vars.put("commodity", driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/span")).getText());
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".designatedCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("#layui-laypage-3 > .layui-laypage-next > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-curr:nth-child(3) > em:nth-child(2)")).getText(), is("2"));
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("乐");
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-count:nth-child(6)")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR9search9() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".designatedCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("片");
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector(".layui-laypage-count:nth-child(7)")).getText());
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".designatedCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-4 > a:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("片");
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-count:nth-child(7)")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTR11searchBySn1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("count1", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("CX20190722");
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(3000);
		vars.put("count2", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText(), is(vars.get("count1").toString()));
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("CX20190722");
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText(), is(vars.get("count2").toString()));
	}

	@Test
	public void uTR12searchBySn2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("CX2019072");
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("查无该活动"));
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTR13searchBySn3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("CX201907240005");
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".trChoosed .laytable-cell-7-0-0")).getText(), is("CX201907240005"));
	}

	@Test
	public void uTR14searchBySn4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("count", driver.findElement(By.cssSelector("#promotionList + div .layui-laypage-count")).getText());
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).clear();
		driver.findElement(By.name("queryKeyword")).sendKeys("CX201909060004555654645644545454");
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("查无该活动"));
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
	}

	@Test
	public void uTR17toViewPromotionDetails() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.xpath("//div[@id=\'left-Area\']/div[2]/div/div[2]/table/tbody/tr[3]/td/div")).click();
		Thread.sleep(1000);
		vars.put("promotionName", driver.findElement(By.xpath("//div[@id=\'left-Area\']/div[2]/div/div[2]/table/tbody/tr[3]/td[2]/div/span")).getText());
		vars.put("staffName", driver.findElement(By.xpath("//div[@id=\'left-Area\']/div[2]/div/div[2]/table/tbody/tr[3]/td[3]/div")).getText());
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("promotionName").toString()));
		}
		{
			String value = driver.findElement(By.name("staff")).getAttribute("value");
			assertThat(value, is(vars.get("staffName").toString()));
		}
	}
}
