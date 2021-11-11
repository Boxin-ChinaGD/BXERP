package com.bx.erp.selenium.Promotion;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.is;
import com.bx.erp.selenium.BaseSeleniumTest;
import static org.junit.Assert.*;

public class Promotion_C1Test extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTC1checkName() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		vars.put("maxlength", js.executeScript("var maxlength = document.getElementsByClassName(\"promotionName\")[0].getAttribute(\"maxlength\"); return maxlength;"));
		if ((Boolean) js.executeScript("return (arguments[0] == 32)", vars.get("maxlength"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTC2checkExcecutionThreshold() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("促销活动");
		Thread.sleep(1000);
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
		driver.findElement(By.name("excecutionThreshold")).sendKeys("300000");
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("200");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("200.123");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("promotionMain")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正数，小数点后最多只能带两位小数"));
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("-2005");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("promotionMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正数，小数点后最多只能带两位小数"));
	}

	@Test
	public void uTC3checkExcecutionAmount() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
		Thread.sleep(1000);
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
		driver.findElement(By.name("excecutionThreshold")).sendKeys("1000");
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("20000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("200.1236");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("promotionMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正数，小数点后最多只能带两位小数"));
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("-200.16");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("promotionMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正数，小数点后最多只能带两位小数"));
	}

	@Test
	public void uTC4checkExcecutionDiscount() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
		Thread.sleep(1000);
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
		driver.findElement(By.name("excecutionThreshold")).sendKeys("1000");
		driver.findElement(By.name("excecutionDiscount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionDiscount")).sendKeys("100");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("折扣需处于0-10的之间！"));
		driver.findElement(By.name("excecutionDiscount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionDiscount")).sendKeys("10");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("折扣需处于0-10的之间！"));
		driver.findElement(By.name("excecutionDiscount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionDiscount")).sendKeys("-1");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正数，小数点后最多只能带两位小数"));
		driver.findElement(By.name("excecutionDiscount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionDiscount")).sendKeys("8.562");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("promotionMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许正数，小数点后最多只能带两位小数"));
	}

	@Test
	public void uTC5checkDatetime() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		driver.findElement(By.id("datetimeEnd")).click();
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
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("活动开始时间必须至少从明天开始！"));
	}

	@Test
	public void uTC6checkInput() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("right-Area")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("444");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("right-Area")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(12)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("promotionMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("200");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("满减和满折不能都为空！"));
	}

	@Test
	public void uTC7create1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("464455");
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("10");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC8create2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.layui-input-inline > input[name=\"name\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.layui-input-inline > input[name=\"name\"]")).sendKeys("2");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tr:nth-child(6) > .laydate-day-next:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("22");
		driver.findElement(By.cssSelector(".promotionManage:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC9create3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
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
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("满减和满折不能都为空！"));
	}

	@Test
	public void uTC10create4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
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
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("10");
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新建满减优惠活动成功"));
	}

	@Test
	public void uTC11create5() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.promotionInfo")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("55");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC12create6() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.layui-input-inline > input[name=\"name\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.layui-input-inline > input[name=\"name\"]")).sendKeys("满10-2");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.laydate-btns-confirm")).click();
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("10");
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("2");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("活动开始时间必须至少从明天开始！"));
	}

	@Test
	public void uTC13create7() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("100");
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("50");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("结束时间不能小于开始时间！"));
	}

	@Test
	public void uTC14create8() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("8687");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-prev-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("22");
		driver.findElement(By.name("excecutionDiscount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionDiscount")).sendKeys("8");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("活动开始时间必须至少从明天开始！"));
	}

	@Test
	public void uTC15create9() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-prev-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-prev-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("20");
		driver.findElement(By.name("excecutionDiscount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionDiscount")).sendKeys("8");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("活动开始时间必须至少从明天开始！"));
	}

	@Test
	public void uTC16create10() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.layui-input-inline > input[name=\"name\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.layui-input-inline > input[name=\"name\"]")).sendKeys("411");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("100");
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("0");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("满减金额不能等于0！"));
	}

	@Test
	public void uTC17create11() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
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
		driver.findElement(By.name("excecutionDiscount")).sendKeys("0");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("折扣需处于0-10的之间！"));
	}

	@Test
	public void uTC18create12() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("862");
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
		driver.findElement(By.name("excecutionDiscount")).sendKeys("10");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("折扣需处于0-10的之间！"));
		driver.findElement(By.name("excecutionDiscount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionDiscount")).sendKeys("11");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("折扣需处于0-10的之间！"));
		driver.findElement(By.name("excecutionDiscount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionDiscount")).sendKeys("10.12");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("折扣需处于0-10的之间！"));
	}

	@Test
	public void uTC19create13() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.layui-input-inline > input[name=\"name\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div.layui-input-inline > input[name=\"name\"]")).sendKeys("促销活动");
		driver.findElement(By.id("datetimeStart")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("datetimeEnd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionThreshold")).sendKeys("100");
		driver.findElement(By.name("excecutionDiscount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionDiscount")).sendKeys("8");
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("请选择指定商品！"));
	}

	@Test
	public void uTC20create18() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
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
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("100");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新建满减优惠活动成功"));
	}

	@Test
	public void uTC21create19() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("82872");
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
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("88");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新建满减优惠活动成功"));
	}

	@Test
	public void uTC22create20() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
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
		driver.findElement(By.name("excecutionThreshold")).sendKeys("230000");
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("220000");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠门槛不能大于10000！"));
	}

	@Test
	public void uTC23create21() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
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
		driver.findElement(By.name("excecutionThreshold")).sendKeys("20");
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("22");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠门槛需大于满减金额！"));
	}

	@Test
	public void uTC24addComm1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("commodity1", driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/span")).getText());
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("commodity2", driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[2]/div/span")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(2)")).getText(), is("已有过入库的商品3"));
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(2)")).getText(), is("已有过入库的商品2"));
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon-add-circle")).click();
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/input")).isSelected());
	}

	@Test
	public void uTC25addComm2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("2"));
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".promotionManage:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		assertFalse(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		assertFalse(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/input")).isSelected());
	}

	@Test
	public void uTC26CommPopup1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline > div:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		vars.put("categoryCount1", js.executeScript("var categoryCount1 = document.getElementsByClassName(\"layui-nav-tree\")[0].getElementsByClassName(\"layui-nav-item\").length; return categoryCount1;"));
		driver.findElement(By.cssSelector(".showAllCommCategory")).click();
		Thread.sleep(1000);
		vars.put("categoryCount2", js.executeScript("var categoryCount2 = document.getElementsByClassName(\"layui-nav-tree\")[0].getElementsByClassName(\"layui-nav-itemed\").length; return categoryCount2;"));
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("categoryCount1"), vars.get("categoryCount2"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTC26CommPopup2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".showAllCommCategory")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".showAllCommCategory")).getText(), is("全部关闭"));
		driver.findElement(By.cssSelector(".showAllCommCategory")).click();
		Thread.sleep(1000);
		vars.put("categoryCount", js.executeScript("var categoryCount = document.getElementsByClassName(\"layui-nav-tree\")[0].getElementsByClassName(\"layui-nav-itemed\").length; return categoryCount;"));
		if ((Boolean) js.executeScript("return (arguments[0] == 0)", vars.get("categoryCount"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTC26CommPopup3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("默认分类")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTC26CommPopup4() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//input[@name=\'queryKeyword\'])[2]")).sendKeys("#");
		driver.findElement(By.cssSelector(".rightRegion > .topArea")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，支持输入的符号有：（）()_-——"));
	}

	@Test
	public void uTC26CommPopup5() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline > div:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		vars.put("count1", driver.findElement(By.cssSelector("#layui-laypage-3 > .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("商品");
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		vars.put("count2", driver.findElement(By.cssSelector("#layui-laypage-6 > .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).clear();
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#layui-laypage-7 > .layui-laypage-count")).getText(), is(vars.get("count1").toString()));
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("商品");
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#layui-laypage-10 > .layui-laypage-count")).getText(), is(vars.get("count2").toString()));
	}

	@Test
	public void uTC26CommPopup6() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText());
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#popupCommodityList + div .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTC26CommPopup7() throws InterruptedException {
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
		vars.put("maxlength", js.executeScript("var maxlength = document.getElementsByClassName(\"rightRegion\")[0].getElementsByClassName(\"topArea\")[0].getElementsByTagName(\"input\")[0].getAttribute(\"maxlength\"); return maxlength;"));
		if ((Boolean) js.executeScript("return (arguments[0] == 64)", vars.get("maxlength"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTC26CommPopup8() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".layui-nav-item:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div/input")).sendKeys("%￥##￥%……");
		driver.findElement(By.cssSelector(".footArea")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("数据格式不正确，请输入中英文、数字，只允许中间有空格，支持输入的符号有：（）()_-——"));
	}

	@Test
	public void uTC26CommPopup9() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-input-inline > div:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("默认分类")).click();
		driver.findElement(By.cssSelector(".layui-nav-item:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("商品");
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector(".layui-laypage-count:nth-child(6)")).getText());
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
		driver.findElement(By.cssSelector(".layui-nav-item:nth-child(13) > .layui-nav-child a")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("商品");
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-laypage-count:nth-child(6)")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTC26CommPopup10() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		vars.put("count", driver.findElement(By.cssSelector("#layui-laypage-5 > .layui-laypage-count")).getText());
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
		driver.findElement(By.xpath("(//a[contains(text(),\'默认分类\')])[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("#layui-laypage-5 > .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTC26CommPopup11() throws InterruptedException {
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
		vars.put("maxlength", js.executeScript("var maxlength = document.getElementsByClassName(\"rightRegion\")[0].getElementsByClassName(\"topArea\")[0].getElementsByTagName(\"input\")[0].getAttribute(\"maxlength\"); return maxlength;"));
		if ((Boolean) js.executeScript("return (arguments[0] == 64)", vars.get("maxlength"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
		vars.put("count", driver.findElement(By.cssSelector("#layui-laypage-4 > .layui-laypage-count")).getText());
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
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		vars.put("maxlength", js.executeScript("var maxlength = document.getElementsByClassName(\"rightRegion\")[0].getElementsByClassName(\"topArea\")[0].getElementsByTagName(\"input\")[0].getAttribute(\"maxlength\"); return maxlength;"));
		if ((Boolean) js.executeScript("return (arguments[0] == 64)", vars.get("maxlength"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
		assertThat(driver.findElement(By.cssSelector("#layui-laypage-5 > .layui-laypage-count")).getText(), is(vars.get("count").toString()));
	}

	@Test
	public void uTC26CommPopup12() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		{
			WebElement element = driver.findElement(By.linkText("满减优惠"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".designatedCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		assertFalse(driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/input")).isSelected());
	}

	@Test
	public void uTC26CommPopup13() throws InterruptedException {
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
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		assertTrue(driver.findElement(By.name("layTableCheckbox")).isSelected());
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("10"));
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		assertFalse(driver.findElement(By.name("layTableCheckbox")).isSelected());
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("0"));
	}

	@Test
	public void uTC26CommPopup17() throws InterruptedException {
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
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("barcode1", driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).getText());
		vars.put("barcode1", js.executeScript("return arguments[0] + \" \";", vars.get("barcode1")));
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("barcode2", driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[3]/div")).getText());
		vars.put("barcode2", js.executeScript("return arguments[0] + \" \";", vars.get("barcode2")));
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.xpath("//table[@id=\'commodityList\']/tbody/tr/td[3]/input")).getAttribute("value");
			assertThat(value, is(vars.get("barcode1").toString()));
		}
		{
			String value = driver.findElement(By.xpath("//table[@id=\'commodityList\']/tbody/tr[2]/td[3]/input")).getAttribute("value");
			assertThat(value, is(vars.get("barcode2").toString()));
		}
	}

	@Test
	public void uTC26CommPopup18() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("促销")).click();
		Thread.sleep(5000);
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".designatedCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("2"));
		driver.findElement(By.cssSelector(".confirmExitComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("0"));
	}

	@Test
	public void uTC26CommPopup19() throws InterruptedException {
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
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("2"));
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("0"));
	}

	@Test
	public void uTC26CommPopup20() throws InterruptedException {
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
		driver.findElement(By.cssSelector(".leftRegion > .layui-btn")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		assertThat(driver.getTitle(), is("博销宝"));
	}

	@Test
	public void uTC26CommPopup21() throws InterruptedException {
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
		driver.findElement(By.cssSelector("#layui-laypage-3 > a:nth-child(4)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-curr:nth-child(4) > em:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-4 > .layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-5 > .layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-6 > .layui-laypage-prev > .layui-icon")).click();
		{
			WebElement element = driver.findElement(By.cssSelector("#layui-laypage-6 > .layui-laypage-prev > .layui-icon"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
		driver.findElement(By.cssSelector("#layui-laypage-6 > .layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-6 > .layui-laypage-next > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("18")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-disabled:nth-child(7) > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-8 .layui-laypage-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#layui-laypage-8 .layui-input")).click();
	}

	@Test
	public void uTC27PreSaleCreatePromotion() throws InterruptedException {
		operatorType = EnumOperatorType.EOT_PRESALE.getIndex();
		//
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("7575");
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
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("20");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
		//
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTC28UserOperationGuide1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".updatePromotion")).getText(), is("保存"));
		assertThat(driver.findElement(By.cssSelector(".createPromotion")).getText(), is("新建"));
		assertThat(driver.findElement(By.cssSelector(".promotionManage:nth-child(3)")).getText(), is("取消"));
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".toStopPromotion")));
		}
	}

	@Test
	public void uTC29UserOperationGuide2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".promotionSearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".promotionManage:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
	}

	@Test
	public void uTC30UserOperationGuide3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("updateButtonStatus", js.executeScript("var updateButtonStatus = document.getElementsByClassName(\"updatePromotion\")[0].getAttribute(\"disabled\"); return updateButtonStatus;"));
		vars.put("cancelButtonStatus", js.executeScript("var cancelButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByClassName(\"layui-btn\")[2].getAttribute(\"disabled\"); return cancelButtonStatus;"));
		vars.put("createButtonStatus", js.executeScript("var createButtonStatus = document.getElementsByClassName(\"createPromotion\")[0].getAttribute(\"disabled\"); return createButtonStatus;"));
		if ((Boolean) js.executeScript("return (arguments[0] != \'disabled\' && arguments[1] == \'disabled\' && arguments[2] == \'disabled\')", vars.get("createButtonStatus"), vars.get("updateButtonStatus"),
				vars.get("cancelButtonStatus"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTC31UserOperationGuide4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline li:nth-child(2)")).getText(), is("未开始"));
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("name", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector(".toStopPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("终止促销活动成功"));
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("name").toString()));
		}
	}

	@Test
	public void uTC32Useroperationguide5() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toStopPromotion")).click();
		Thread.sleep(5000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("终止促销活动成功"));
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("促销活动");
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
		driver.findElement(By.name("excecutionAmount")).sendKeys("1");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新建满减优惠活动成功"));
	}

	@Test
	public void uTC33chooseServerCommodity() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品列表")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".generalCommodityInfo > .layui-form-item:nth-child(16) .layui-unselect .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommodityBarcode")).sendKeys("123456789");
		vars.put("serverCommodityName", js.executeScript("var serverCommodityName = \"服务商品\" + Math.floor(Math.random() * 999); return serverCommodityName;"));
		driver.findElement(By.cssSelector(".serviceCommName")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".serviceCommName")).sendKeys(vars.get("serverCommodityName").toString());
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(14) > .layui-inline:nth-child(2) .layui-input")).sendKeys("克");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) > .layui-inline:nth-child(2) .layui-input")).sendKeys("66.00");
		driver.findElement(By.cssSelector(".btnChoosed")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建服务类商品成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.cssSelector(".createPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".designatedCommodity")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys(vars.get("serverCommodityName").toString());
		driver.findElement(By.cssSelector(".commoditySearch")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
	}
}
