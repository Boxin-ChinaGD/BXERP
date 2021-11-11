package com.bx.erp.selenium.cache.UpdateR;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CacheUpdateRTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		setOperatorType(EnumOperatorType.EOT_OP.getIndex());
		super.setUp();
	}

	@Test
	public void uTR1defaultShowCache() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-templeate-1")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("更新缓存")).click();
		driver.switchTo().frame(1);
		vars.put("companyName", driver.findElement(By.xpath("//input[@value=\'BX二号分公司\']")).getAttribute("value"));
		vars.put("companyCache", driver.findElement(By.cssSelector("p:nth-child(3)")).getText());
		if ((Boolean) js.executeScript("return (arguments[0].indexOf(\'name=\' + arguments[1]) == -1)", vars.get("companyCache"), vars.get("companyName"))) {
			System.out.println("测试失败");
		} else {
			System.out.println("测试通过");
		}
	}

	@Test
	public void uTR2queryByCompany() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-templeate-1")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("更新缓存")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.xpath("//dd[contains(.,\'BX一号分公司\')]")).click();
		vars.put("divLength", js.executeScript("var divLength = document.getElementsByClassName(\"showInfo\")[0].getElementsByTagName(\"div\").length; return divLength;"));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if ((Boolean) js.executeScript("return (arguments[0] > 0)", vars.get("divLength"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTR3queryByCompanyAndCacheType() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-templeate-1")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("更新缓存")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(2) .layui-input")).click();
		driver.findElement(By.xpath("//dd[contains(.,\'BX一号分公司\')]")).click();
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) .layui-input")).click();
		driver.findElement(By.xpath("//dd[contains(.,\'ECT_Category\')]")).click();
		vars.put("divLength", js.executeScript("var divLength = document.getElementsByClassName(\"showInfo\")[0].getElementsByTagName(\"div\").length; return divLength;"));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if ((Boolean) js.executeScript("return (arguments[0] > 0)", vars.get("divLength"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTR4queryWhenCacheNoExist() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-templeate-1")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("更新缓存")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-inline:nth-child(3) .layui-input")).click();
		driver.findElement(By.xpath("//dd[contains(.,\'ECT_Category\')]")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("获取nbr_bx公司的缓存ECT_Category失败!!!"));
	}
}
