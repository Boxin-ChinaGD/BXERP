package com.bx.erp.selenium.vip;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class BonusHistory_U_Test extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTC11queryBonusHistory1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("积分历史")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("keyWord")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("keyWord")).sendKeys("你");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的关键字不符合要求，需提供完整的手机号或至少2个字符的会员名称"));
	}

	@Test
	public void uTC12queryBonusHistory2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("vipName", driver.findElement(By.name("name")).getAttribute("value"));
		vars.put("originalBonus", driver.findElement(By.name("bonus")).getAttribute("value"));
		driver.findElement(By.linkText("修改积分")).click();
		Thread.sleep(1000);
		vars.put("bonus", js.executeScript("var bonus = arguments[0]; return parseInt(bonus) + 11;", vars.get("originalBonus")));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-input")).sendKeys(vars.get("bonus").toString());
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改积分成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.linkText("积分历史")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.name("keyWord")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("keyWord")).sendKeys(vars.get("vipName").toString());
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'bonusHistoryMain\']/div/div/div/div[2]/table/tbody/tr/td[2]/div")).getText(), is(vars.get("vipName").toString()));
		assertThat(driver.findElement(By.xpath("//div[@id=\'bonusHistoryMain\']/div/div/div/div[2]/table/tbody/tr/td[4]/div")).getText(), is("+11积分"));
	}

	@Test
	public void uTC13queryBonusHistory3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("vipName", driver.findElement(By.name("name")).getAttribute("value"));
		vars.put("vipMobile", driver.findElement(By.name("mobile")).getAttribute("value"));
		vars.put("originalBonus", driver.findElement(By.name("bonus")).getAttribute("value"));
		driver.findElement(By.linkText("修改积分")).click();
		Thread.sleep(1000);
		vars.put("bonus", js.executeScript("var bonus = arguments[0]; return parseInt(bonus) - 11;", vars.get("originalBonus")));
		driver.findElement(By.cssSelector(".layui-layer-input")).sendKeys(vars.get("bonus").toString());
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改积分成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.linkText("积分历史")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		driver.findElement(By.name("keyWord")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("keyWord")).sendKeys(vars.get("vipMobile").toString());
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'bonusHistoryMain\']/div/div/div/div[2]/table/tbody/tr/td[2]/div")).getText(), is(vars.get("vipName").toString()));
		assertThat(driver.findElement(By.xpath("//div[@id=\'bonusHistoryMain\']/div/div/div/div[2]/table/tbody/tr/td[4]/div")).getText(), is("-11积分"));
	}

	@Test
	public void uTC1showBonusHistory() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("会员管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		vars.put("vipName", driver.findElement(By.name("name")).getAttribute("value"));
		vars.put("originalBonus", driver.findElement(By.name("bonus")).getAttribute("value"));
		driver.findElement(By.linkText("修改积分")).click();
		Thread.sleep(1000);
		vars.put("bonus", js.executeScript("var bonus = arguments[0]; return parseInt(bonus) + 50;", vars.get("originalBonus")));
		driver.findElement(By.cssSelector(".layui-layer-input")).sendKeys(vars.get("bonus").toString());
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改积分成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.linkText("积分历史")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(2);
		assertThat(driver.findElement(By.xpath("//div[@id=\'bonusHistoryMain\']/div/div/div/div[2]/table/tbody/tr/td[2]/div")).getText(), is(vars.get("vipName").toString()));
		assertThat(driver.findElement(By.xpath("//div[@id=\'bonusHistoryMain\']/div/div/div/div[2]/table/tbody/tr/td[4]/div")).getText(), is("+50积分"));
	}
}
