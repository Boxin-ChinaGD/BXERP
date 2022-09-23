package com.bx.erp.selenium.Promotion;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Promotion_U2Test extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_PRESALE.getIndex();
	}

	@Test
	public void uTU2PresalesUpdateInfo() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
		driver.findElement(By.linkText("满减优惠")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("statusOfSaveButton ", js.executeScript("var statusOfSaveButton = document.getElementsByClassName(\"updatePromotion\")[0].getAttribute(\"disabled\"); return statusOfSaveButton;"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'disabled\')", vars.get("statusOfSaveButton"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}
}
