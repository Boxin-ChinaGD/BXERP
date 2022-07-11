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
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@value=\'默认区域\']")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("p.provideDistrictSelect > span.whiteIcon.delete")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("默认区域不可删除"));
	}

	@Test
	public void uTD2deleteDistrict2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.whiteIcon.add")).click();
		vars.put("district", js.executeScript("return \"区域\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).sendKeys(vars.get("district").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商区域成功"));
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.findElement(By.cssSelector("p.rowSpace.provideDistrictSelect > input.district")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.whiteIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("不能删除还有供应商使用的区域"));
	}

	@Test
	public void uTD3deleteDistrict3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.whiteIcon.add")).click();
		vars.put("district", js.executeScript("return \"区域\"+Math.round(Math.random()*99999)"));
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).sendKeys(vars.get("district").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商区域成功"));
		driver.findElement(By.cssSelector("span.whiteIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("删除供应商区域成功"));
	}

	@Test
	public void uTD4deleteProvider1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("默认供应商");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".paddingLeft > input:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".paddingLeft > .delete")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("默认供应商不可删除"));
	}

	@Test
	public void uTD5deleteProvider2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("天猫");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".paddingLeft > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("供应商已经存在商品，不能删除"));
	}

	@Test
	public void uTD6deleteProvider3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.findElement(By.cssSelector(".provideSelect > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("删除供应商成功"));
	}

	@Test
	public void uTD7deleteBrand1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:last-child > .delete")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("默认品牌不可删除"));
	}

	@Test
	public void uTD8deleteBrand2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-last-child(2) > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("品牌表中已有商品的品牌"));
	}

	@Test
	public void uTD9deleteBrand3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.id("brandCreateButton")).click();
		vars.put("brand", js.executeScript("return \"品牌\"+Math.round(Math.random()*99999)"));
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).sendKeys(vars.get("brand").toString());
		driver.findElement(By.id("brandCreateButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品品牌成功"));
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("删除商品品牌成功"));
	}

	@Test
	public void uTD10deleteCategoryParent1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@value=\'默认分类\']")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("默认分类不可删除"));
	}

	@Test
	public void uTD11deleteCategoryParent2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@value=\'饮料酒水\']")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("删除的大类当中的小类有关联商品"));
	}

	@Test
	public void uTD12deleteCategoryParent3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.add")).click();
		vars.put("categoryParent", js.executeScript("return \"分类\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys(vars.get("categoryParent").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品大类成功"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("删除商品大类成功"));
	}

	@Test
	public void uTD13deleteCategory1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".rowSpace:last-child > .categoryParent")).click();
		Thread.sleep(2000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .delete:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("默认分类不可删除"));
	}

	@Test
	public void uTD14deleteCategory2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(11) > .categoryParent")).click();
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("商品表中已有商品的类别"));
	}

	@Test
	public void uTD15deleteCategory3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.add")).click();
		vars.put("category", js.executeScript("return \"分类\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品小类成功"));
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("删除商品小类成功"));
	}

	@Test
	public void uTD16deleteUnit1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#unit > .rowSpace:last-child > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("该包装单位已被采购订单商品使用，不能删除"));
	}

	@Test
	public void uTD17deleteUnit2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.id("unitCreateButton")).click();
		vars.put("unit", js.executeScript("return \"分类\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys(vars.get("unit").toString());
		driver.findElement(By.id("unitCreateButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品包装单位成功"));
		driver.findElement(By.cssSelector("#unit > p.rowSpace")).click();
		driver.findElement(By.cssSelector("#unit > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("删除商品包装单位成功"));
	}

	@Test
	public void uTD18PreSaleDeleteDistrict() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		driver.findElement(By.cssSelector("p:nth-child(2) > .district")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.whiteIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTD19PreSaleDeleteProvider() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTD20PreSaleDeleteBrand() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(2) > .delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTD21PreSaleDeleteCategoryParent() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(4) > .categoryParent")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTD22PreSaleDeleteCategory() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTD23PreSaleDeleteUnit() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#unit > p.rowSpace > span.blackIcon.delete")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}
}
