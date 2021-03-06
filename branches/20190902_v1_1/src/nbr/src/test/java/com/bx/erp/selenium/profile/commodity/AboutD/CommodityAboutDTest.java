package com.bx.erp.selenium.profile.commodity.AboutD;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CommodityAboutDTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

	public void LoginSucceed() {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
	}

	public void PreSaleLoginSucceed() {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("13888888888");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
	}

	@Test
	public void loginSucceed() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
	}

	@Test
	public void preSaleLoginSucceed() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("13888888888");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
	}

	@Test
	public void uTD1deleteDistrict1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@value=\'????????????\']")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("p.provideDistrictSelect > span.whiteIcon.delete")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????????????????"));
	}

	@Test
	public void uTD2deleteDistrict2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.whiteIcon.add")).click();
		vars.put("district", js.executeScript("return \"??????\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).sendKeys(vars.get("district").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("???????????????????????????"));
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider", js.executeScript("return \"?????????\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("?????????????????????"));
		driver.findElement(By.cssSelector("p.rowSpace.provideDistrictSelect > input.district")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.whiteIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("??????????????????????????????????????????"));
	}

	@Test
	public void uTD3deleteDistrict3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.whiteIcon.add")).click();
		vars.put("district", js.executeScript("return \"??????\"+Math.round(Math.random()*99999)"));
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).sendKeys(vars.get("district").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("???????????????????????????"));
		driver.findElement(By.cssSelector("span.whiteIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("???????????????????????????"));
	}

	@Test
	public void uTD4deleteProvider1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("???????????????");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".paddingLeft > input:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".paddingLeft > .delete")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("???????????????????????????"));
	}

	@Test
	public void uTD5deleteProvider2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("??????");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".paddingLeft > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("??????????????????????????????????????????"));
	}

	@Test
	public void uTD6deleteProvider3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider", js.executeScript("return \"?????????\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("?????????????????????"));
		driver.findElement(By.cssSelector(".provideSelect > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("?????????????????????"));
	}

	@Test
	public void uTD7deleteBrand1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:last-child > .delete")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("????????????????????????"));
	}

	@Test
	public void uTD8deleteBrand2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-last-child(2) > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("?????????????????????????????????"));
	}

	@Test
	public void uTD9deleteBrand3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.id("brandCreateButton")).click();
		vars.put("brand", js.executeScript("return \"??????\"+Math.round(Math.random()*99999)"));
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).sendKeys(vars.get("brand").toString());
		driver.findElement(By.id("brandCreateButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????????????????"));
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????????????????"));
	}

	@Test
	public void uTD10deleteCategoryParent1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@value=\'????????????\']")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????????????????"));
	}

	@Test
	public void uTD11deleteCategoryParent2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@value=\'????????????\']")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("?????????????????????????????????????????????"));
	}

	@Test
	public void uTD12deleteCategoryParent3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.add")).click();
		vars.put("categoryParent", js.executeScript("return \"??????\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys(vars.get("categoryParent").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????????????????"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????????????????"));
	}

	@Test
	public void uTD13deleteCategory1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".rowSpace:last-child > .categoryParent")).click();
		Thread.sleep(2000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .delete:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("????????????????????????"));
	}

	@Test
	public void uTD14deleteCategory2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(11) > .categoryParent")).click();
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("?????????????????????????????????"));
	}

	@Test
	public void uTD15deleteCategory3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.add")).click();
		vars.put("category", js.executeScript("return \"??????\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????????????????"));
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????????????????"));
	}

	@Test
	public void uTD16deleteUnit1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#unit > .rowSpace:last-child > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????????????????????????????????????????????????????"));
	}

	@Test
	public void uTD17deleteUnit2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.id("unitCreateButton")).click();
		vars.put("unit", js.executeScript("return \"??????\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys(vars.get("unit").toString());
		driver.findElement(By.id("unitCreateButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("??????????????????????????????"));
		driver.findElement(By.cssSelector("#unit > p.rowSpace")).click();
		driver.findElement(By.cssSelector("#unit > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("??????????????????????????????"));
	}

	@Test
	public void uTD18PreSaleDeleteDistrict() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
		driver.findElement(By.cssSelector("p:nth-child(2) > .district")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.whiteIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
	}

	@Test
	public void uTD19PreSaleDeleteProvider() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
	}

	@Test
	public void uTD20PreSaleDeleteBrand() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(2) > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
	}

	@Test
	public void uTD21PreSaleDeleteCategoryParent() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(4) > .categoryParent")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
	}

	@Test
	public void uTD22PreSaleDeleteCategory() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
	}

	@Test
	public void uTD23PreSaleDeleteUnit() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("????????????")).click();
		driver.findElement(By.linkText("????????????")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#unit > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("??????")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("????????????"));
	}
}
