package com.bx.erp.selenium.Promotion;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;
import java.util.*;

public class Promotion_D1Test extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTD1dalete1() throws InterruptedException {
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
		driver.findElement(By.name("excecutionThreshold")).sendKeys("10");
		driver.findElement(By.name("excecutionAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("excecutionAmount")).sendKeys("2");
		driver.findElement(By.cssSelector(".updatePromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新建满减优惠活动成功"));
		driver.findElement(By.cssSelector(".toStopPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("终止促销活动成功"));
	}

	@Test
	public void uTD2dalete2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline li:nth-child(3)")).getText(), is("进行中"));
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'left-Area\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toStopPromotion")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("终止促销活动成功"));
	}

	@Test
	public void uTD3dalete3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline li:nth-child(4)")).getText(), is("已结束"));
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(4)")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'left-Area\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div")).click();
		Thread.sleep(1000);
		vars.put("statusOfButton", js.executeScript("var statusOfButton = document.getElementsByClassName(\"toStopPromotion\")[0].getAttribute(\"disabled\"); return statusOfButton;"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'disabled\')", vars.get("statusOfButton"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTD4dalete4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("b")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-inline li:nth-child(5)")).getText(), is("已删除"));
		driver.findElement(By.cssSelector(".layui-inline li:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'left-Area\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div")).click();
		Thread.sleep(1000);
		vars.put("statusOfButton", js.executeScript("var statusOfButton = document.getElementsByClassName(\"toStopPromotion\")[0].getAttribute(\"disabled\"); return statusOfButton;"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'disabled\')", vars.get("statusOfButton"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTD6deleteComm2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.xpath("//div[@id=\'left-Area\']/div[2]/div/div[2]/table/tbody/tr[2]/td/div")).click();
		Thread.sleep(1000);
		{
			List<WebElement> elements = driver.findElements(By.cssSelector(".deleteGeneralComm"));
			assert (elements.size() == 0);
		}
	}

}
