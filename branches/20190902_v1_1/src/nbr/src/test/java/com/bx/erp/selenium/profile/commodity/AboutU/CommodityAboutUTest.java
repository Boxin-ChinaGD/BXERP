package com.bx.erp.selenium.profile.commodity.AboutU;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CommodityAboutUTest extends BaseSeleniumTest {

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
	public void uTU10updateProvider1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("");
		// driver.findElement(By.cssSelector(".layui-col-space")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("");
		// driver.findElement(By.cssSelector(".layui-col-space")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("");
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTU11updateProvider2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("");
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("联系人U11");
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("地址U11");
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("");
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTU12updateProvider3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("");
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("联系人U12");
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("地址U12");
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("13129355311");
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTU13updateProvider4() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.id("name")).click();
		vars.put("provider", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("");
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("");
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("");
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
	}

	@Test
	public void uTU14updateProvider5() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		vars.put("provider", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("联系人U14");
		// driver.findElement(By.cssSelector(".layui-col-space")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("");
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
	}

	@Test
	public void uTU15updateProvider6() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.id("name")).click();
		vars.put("provider", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("联系人U15");
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("地址U15");
		driver.findElement(By.name("mobile")).click();
		vars.put("phone", js.executeScript("return \"135678\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
	}

	@Test
	public void uTU16updateProvider7() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("默认供应商");
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该供应商名称已存在"));
	}

	@Test
	public void uTU17updateProvider8() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		driver.findElement(By.id("name")).click();
		vars.put("provide", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provide").toString());
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("sss");
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商成功"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.name("contactName")).click();
		// driver.findElement(By.cssSelector(".layui-col-space")).click();
		Thread.sleep(1000);
		// {
		// WebElement element = driver.findElement(By.cssSelector(".layui-col-space"));
		// Actions builder = new Actions(driver);
		// builder.doubleClick(element).perform();
		// }
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("sss");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
	}

	@Test
	public void uTU18updateProvider9() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).sendKeys("13129355441");
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该联系人电话已存在"));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该联系电话已存在，请重新修改"));
	}

	@Test
	public void uTU19updateProvider10() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("广州市天河区二十八中学");
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
	}

	@Test
	public void uTU1checkDistrictLength() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-edit")).click();
		vars.put("District", js.executeScript("return \"区域长度\"+Math.round(Math.random()*99999999999999999999)"));
		System.out.println(vars.get("District").toString());
		// 输入不了太长的
		// driver.findElement(By.cssSelector("p.provideDistrictSelect >
		// input.district")).sendKeys(vars.get("District").toString());
		// Thread.sleep(1000);
		// assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(),
		// is("数据不符合要求,不能为空且不能全为空格！"));
		// driver.findElement(By.id("commodityAboutMain")).click();
		// Thread.sleep(1000);
		// driver.findElement(By.cssSelector("i.layui-icon.layui-icon-edit")).click();
		vars.put("District", js.executeScript("return \"区域\"+Math.round(Math.random()*999999999999999)"));
		System.out.println("vars:" + vars.get("District").toString());
		driver.findElement(By.cssSelector("p.provideDistrictSelect > input.district")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("District").toString());
		// driver.findElement(By.cssSelector("p.provideDistrictSelect >
		// input.district")).sendKeys(vars.get("District").toString());
		Thread.sleep(1000);
		driver.findElement(By.id("commodityAboutMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("修改供应商区域成功"));
	}

	@Test
	public void uTU20updateProvider11() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("document.getElementsByClassName(\"paddingLeft\")[3].click()");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("contactName")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		Thread.sleep(1000);
		// driver.findElement(By.xpath("(//input[@value=\'默认区域\'])[2]")).click();
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(1)")).click();
		driver.findElement(By.cssSelector("dd:nth-child(7)")).click();
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("dd:nth-child(7)")).click();
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
	}

	@Test
	public void uTU21providerReturnButton() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		vars.put("providerName", driver.findElement(By.cssSelector(".provideSelect > input:nth-child(2)")).getAttribute("value"));
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.id("name")).sendKeys("供应商U21");
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).sendKeys("联系人U21");
		driver.findElement(By.cssSelector("input.layui-input.layui-unselect")).click();
		driver.findElement(By.cssSelector("dd")).click();
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).sendKeys("地址U21");
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).sendKeys("15766403321");
		driver.findElement(By.cssSelector("input.commodityUnButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		{
			String value = driver.findElement(By.id("name")).getAttribute("value");
			assertThat(value, is(vars.get("providerName").toString()));
		}
	}

	@Test
	public void uTU22checkBrandLength() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(2) .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .brand")).click();
		// System.out.println("fsdfdsfds:" +
		// driver.findElement(By.cssSelector(".rowSpace:nth-child(2) >
		// .brand")).getAttribute("value"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .brand")).sendKeys("徐福记55545643rtrrtrtrt123456789");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		// assertThat(driver.findElement(By.cssSelector(".rowSpace:nth-child(2) >
		// .brand")).getAttribute("value"), is("徐福记55545643rtrrtrtrt"));
		// sendKeys无法输入比限制长度更长的内容
		// assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(),
		// is("品牌名字为英文或数字的组合，长度为1到20"));
	}

	@Test
	public void uTU23updateBrand1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(1) .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).clear();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).sendKeys("");
		driver.findElement(By.cssSelector(".commodityBrand")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
	}

	@Test
	public void uTU24updateBrand2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(1) .layui-icon")).click();
		vars.put("brand", js.executeScript("return \"  维他奶\";"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("brand").toString());
		driver.findElement(By.cssSelector(".commodityBrand")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).click();
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(1) .layui-icon")).click();
		vars.put("brand", js.executeScript("return \"维他奶   \";"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("brand").toString());
		driver.findElement(By.cssSelector(".commodityBrand")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(1) .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "维他  奶");
		driver.findElement(By.cssSelector("#providerMessage > .layui-form-item:nth-child(1) > .layui-input-block")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(1) .layui-icon")).click();
		vars.put("brand", js.executeScript("return \"                    \";"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("brand").toString());
		driver.findElement(By.cssSelector(".commodityBrand")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(1) .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "康师傅");
		driver.findElement(By.cssSelector(".commodityBrand")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("该商品品牌已存在，请重新修改"));
	}

	@Test
	public void uTU25updateBrand3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(2) .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .brand")).click();
		vars.put("brand", js.executeScript("return \"品牌\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .brand")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("brand").toString());
		driver.findElement(By.id("brandButton")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品品牌成功"));
	}

	@Test
	public void uTU26checkCategoryParentLength() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(3) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > input.categoryParent")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "UUUUUUUUUUU");
		{
			WebElement element = driver.findElement(By.cssSelector("input.categoryParent"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		driver.findElement(By.id("commodityAboutMain")).click();
		// 前端做了限制，不能输入比限制的长度长
		// Thread.sleep(1000);
		// assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(),
		// is("输入的数据格式不正确"));
	}

	@Test
	public void uTU27updateCategoryParent1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(3) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > input.categoryParent")).sendKeys(Keys.chord(Keys.CONTROL, "a"), " ");
		driver.findElement(By.id("commodityAboutMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
	}

	@Test
	public void uTU28updateCategoryParent2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".rowSpace:nth-child(4) > .categoryParent")).click();
		driver.findElement(By.cssSelector(".categoryParentSelect .layui-icon")).click();
		driver.findElement(By.cssSelector(".categoryParentSelect > .categoryParent")).click();
		vars.put("CategoryParent", js.executeScript("return \"         \";"));
		driver.findElement(By.cssSelector(".categoryParentSelect > .categoryParent")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("CategoryParent").toString());
		driver.findElement(By.cssSelector(".layui-col-md6 > .title")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".categoryParentSelect .layui-icon")).click();
		vars.put("CategoryParent", js.executeScript("return \"日用百货    \";"));
		driver.findElement(By.cssSelector(".categoryParentSelect > .categoryParent")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("CategoryParent").toString());
		driver.findElement(By.cssSelector(".layui-container > .layui-row:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".categoryParentSelect .layui-icon")).click();
		driver.findElement(By.cssSelector(".categoryParentSelect > .categoryParent")).click();
		vars.put("CategoryParent", js.executeScript("return \"    日用百货\";"));
		driver.findElement(By.cssSelector(".categoryParentSelect > .categoryParent")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("CategoryParent").toString());
		driver.findElement(By.cssSelector(".layui-col-space")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".categoryParentSelect .layui-icon")).click();
		driver.findElement(By.cssSelector(".categoryParentSelect > .categoryParent")).click();
		vars.put("CategoryParent", js.executeScript("return \"日用   百货\";"));
		driver.findElement(By.cssSelector(".categoryParentSelect > .categoryParent")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("CategoryParent").toString());
		driver.findElement(By.cssSelector(".layui-col-md6 > .title")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".categoryParentSelect .layui-icon")).click();
		vars.put("CategoryParent", js.executeScript("return \"日用百货#@@\";"));
		driver.findElement(By.cssSelector(".categoryParentSelect > .categoryParent")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("CategoryParent").toString());
		driver.findElement(By.cssSelector(".layui-container > .layui-row:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
	}

	@Test
	public void uTU29updateCategoryParent3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(3) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		vars.put("category", js.executeScript("return \"大类\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > input.categoryParent")).sendKeys(vars.get("category").toString());
		driver.findElement(By.id("commodityAboutMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品大类成功"));
	}

	@Test
	public void uTU2updateDistrict1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.cssSelector("p.provideDistrictSelect > input.district")).sendKeys(Keys.chord(Keys.CONTROL, "a"), " ");
		driver.findElement(By.id("commodityAboutMain")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
	}

	@Test
	public void uTU30checkCategoryLength() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.cssSelector("input.category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "UUUUUUUUUUU");
		driver.findElement(By.cssSelector("div.layui-col-md6.commodityCategory > div.title")).click();
		// 前端限制了输入长度
		// Thread.sleep(1000);
		// assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(),
		// is("非法的值"));
	}

	@Test
	public void uTU31updateCategory1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.cssSelector("input.category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), " ");
		{
			WebElement element = driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.add"));
			// TODO 进行了手动导包，令其编译通过
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector("div.layui-col-md6.commodityCategory > div.title")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
	}

	@Test
	public void uTU32updateCategory2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .blackIcon:nth-child(5) > .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .category")).click();
		vars.put("category", js.executeScript("return \"        \";"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("category").toString());
		driver.findElement(By.cssSelector(".layui-col-space")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .blackIcon:nth-child(5) > .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .category")).click();
		vars.put("category", js.executeScript("return \"    猪牛羊肉\";"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("category").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .blackIcon:nth-child(5) > .layui-icon")).click();
		vars.put("category", js.executeScript("return \"猪牛羊肉      \";"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("category").toString());
		driver.findElement(By.cssSelector(".commodityUnit")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .blackIcon:nth-child(5) > .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .category")).click();
		vars.put("category", js.executeScript("return \"猪牛   羊肉\";"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("category").toString());
		driver.findElement(By.cssSelector(".layui-container > .layui-row:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .blackIcon:nth-child(5) > .layui-icon")).click();
		vars.put("category", js.executeScript("return \"猪牛羊肉$#$%\";"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("category").toString());
		driver.findElement(By.cssSelector(".layui-container > .layui-row:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .blackIcon:nth-child(5) > .layui-icon")).click();
		vars.put("category", js.executeScript("return \"海鲜\";"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("category").toString());
		driver.findElement(By.cssSelector(".layui-container > .layui-row:nth-child(2)")).click();
		// Thread.sleep(1000);
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该商品小类已存在"));
	}

	@Test
	public void uTU33updateCategory3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		vars.put("category", js.executeScript("return \"大类\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("category").toString());
		driver.findElement(By.cssSelector("div.layui-col-md6.commodityCategory")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品小类成功"));
	}

	@Test
	public void uTU34checkUnitLength() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#unit > p.rowSpace > span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.cssSelector("input.unit")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "UUUUUUUUU");
		driver.findElement(By.cssSelector("div.layui-col-md3.commodityUnit > div.title")).click();
		// 前端限制了输入长度
		// Thread.sleep(1000);
		// assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(),
		// is("服务器错误"));
	}

	@Test
	public void uTU35updateUnit1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#unit > p.rowSpace > span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.cssSelector("input.unit")).sendKeys(Keys.chord(Keys.CONTROL, "a"), " ");
		driver.findElement(By.cssSelector("div.layui-col-md3.commodityUnit > div.title")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
	}

	@Test
	public void uTU36updateUnit2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#unit > .rowSpace:nth-child(1) .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .unit")).click();
		vars.put("unit", js.executeScript("return \"        \";"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .unit")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("unit").toString());
		driver.findElement(By.cssSelector(".commodityUnit")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
		driver.findElement(By.cssSelector("#unit > .rowSpace:nth-child(1) .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .unit")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "箱%￥%￥%￥");
		driver.findElement(By.cssSelector(".commodityUnit")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字、空格，不能只输入空格"));
		driver.findElement(By.cssSelector("#unit > .rowSpace:nth-child(1) .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .unit")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "根");
		driver.findElement(By.cssSelector(".layui-container > .layui-row:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("该商品单位已存在，请重新修改"));
	}

	@Test
	public void uTU37updateUnit3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#unit > p.rowSpace > span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		vars.put("unit", js.executeScript("return \"包装单位\"+Math.round(Math.random()*999)"));
		driver.findElement(By.cssSelector("input.unit")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("unit").toString());
		driver.findElement(By.id("unitButton")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品包装单位成功"));
	}

	@Test
	public void uTU38checkProeprtyLength() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("p.layui-input-block > span.blackIcon > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.name("name1")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "UUUUUUUUUUUUUUUU");
		driver.findElement(By.cssSelector("div.layui-col-md3.commodityProeprty > div.title")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品属性成功"));
	}

	@Test
	public void uTU39updateProeprty1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(2) .layui-icon")).click();
		driver.findElement(By.name("name2")).click();
		driver.findElement(By.name("name2")).sendKeys(Keys.chord(Keys.CONTROL, "a"), " ");
		driver.findElement(By.cssSelector(".layui-container")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品属性成功"));
	}

	@Test
	public void uTU3updateDistrict2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-edit")).click();
		vars.put("District", js.executeScript("return \"       \";"));
		driver.findElement(By.cssSelector("p.provideDistrictSelect > input.district")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("District").toString());
		driver.findElement(By.id("commodityAboutMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.cssSelector("p.provideDistrictSelect > input.district")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "默认区域");
		Thread.sleep(1000);
		driver.findElement(By.id("commodityAboutMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("该供应商区域已存在，请重新修改"));
	}

	@Test
	public void uTU41updateProeprty3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("p.layui-input-block > span.blackIcon > i.layui-icon.layui-icon-edit")).click();
		vars.put("property", js.executeScript("return \"属性\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.name("name1")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("property").toString());
		driver.findElement(By.cssSelector("div.layui-col-md3.commodityProeprty")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改商品属性成功"));
	}

	@Test
	public void uTU42updateDefaultDistrict() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("p:last-child > .whiteIcon > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("默认区域不可修改"));
	}

	@Test
	public void uTU43updateDefaultProvider() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "默认供应商");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".paddingLeft .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("默认供应商不可修改"));
	}

	@Test
	public void uTU44updateDefaultBrand() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:last-child .layui-icon")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("默认品牌不可修改"));
	}

	@Test
	public void uTU45updateDefaultCategoryParent() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".rowSpace:last-child > .categoryParent")).click();
		driver.findElement(By.cssSelector(".categoryParentSelect .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("默认分类不可修改"));
	}

	@Test
	public void uTU46updateDefaultCategory() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#category .rowSpace:last-child .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("默认分类不可修改"));
	}

	@Test
	public void uTU47checkUniqueProviderName() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		// Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".layui-col-space")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "默认供应商");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该供应商名称已存在"));
	}

	@Test
	public void uTU48checkUniqueCategoryName() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .blackIcon:nth-child(5) > .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .category")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "默认分类");
		driver.findElement(By.cssSelector(".layui-col-md6 > .title")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该商品小类已存在"));
	}

	@Test
	public void uTU49visitedOtherProviderWhenUpdate1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		vars.put("providerName", driver.findElement(By.cssSelector(".paddingLeft:nth-child(2) > input:nth-child(2)")).getAttribute("value"));
		driver.findElement(By.cssSelector(".paddingLeft:nth-child(2)")).click();
		{
			String value = driver.findElement(By.id("name")).getAttribute("value");
			assertThat(value, is(vars.get("providerName").toString()));
		}
	}

	@Test
	public void uTU4updateDistrict3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-edit")).click();
		vars.put("District", js.executeScript("return \"区域\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("p.provideDistrictSelect > input.district")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("District").toString());
		driver.findElement(By.id("commodityAboutMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("修改供应商区域成功"));
	}

	@Test
	public void uTU50visitedOtherDistrictWhenUpdate1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		// 安徽供应商对应的区域会被其它测试用例修改
		vars.put("District", driver.findElement(By.xpath("//input[@value=\'默认区域\']")).getAttribute("value"));
		driver.findElement(By.xpath("//input[@value=\'默认区域\']")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-this")).getText(), is(vars.get("District").toString()));
	}

	@Test
	public void uTU51visitedOtherProviderWhenUpdate2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.cssSelector("input.layui-input.layui-unselect")).click();
		driver.findElement(By.cssSelector("dd")).click();
		driver.findElement(By.cssSelector("#provider > p:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		driver.findElement(By.cssSelector("#provider > p:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU52visitedOtherDistrictWhenUpdate2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("input.layui-input.layui-unselect")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("dd")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("p:nth-child(3) > .district")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		driver.findElement(By.cssSelector("p:nth-child(3) > .district")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU53visitedOtherProviderWhenUpdate3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.id("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "华南供应");
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "Adas");
		driver.findElement(By.cssSelector("input.layui-input.layui-unselect")).click();
		driver.findElement(By.cssSelector("dd:nth-child(2)")).click();
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "广州市 天河区 二十八中学");
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "13756564564");
		driver.findElement(By.cssSelector("#provider > p:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#provider > p:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU54visitedOtherDistrictWhenUpdate3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.id("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "华南供应");
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "Adas");
		driver.findElement(By.cssSelector("input.layui-input.layui-unselect")).click();
		driver.findElement(By.cssSelector("dd:nth-child(2)")).click();
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "广州市 天河区 二十八中学");
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "13756564564");
		driver.findElement(By.cssSelector("p:nth-child(3) > .district")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("p:nth-child(3) > .district")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU55clickOtherAreaWhenClick() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.id("name")).click();
		vars.put("name", js.executeScript("return document.getElementById(\"name\").value;"));
		driver.findElement(By.id("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "华南");
		driver.findElement(By.cssSelector("#provider > p:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("p:nth-child(2) > .district")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("name").toString());
		driver.findElement(By.cssSelector("#provider > p:nth-child(3)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		driver.findElement(By.cssSelector("p:nth-child(2) > .district")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU56PreSaleUpdateDistrict() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("p:nth-child(4) > .whiteIcon > .layui-icon")).click();
		driver.findElement(By.cssSelector(".provideDistrictSelect > .district")).click();
		driver.findElement(By.cssSelector(".provideDistrictSelect > .district")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "江苏555");
		{
			WebElement element = driver.findElement(By.cssSelector(".paddingLeft > input:nth-child(2)"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".layui-col-md9")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTU57PreSaleUpdateProvider() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
		driver.findElement(By.cssSelector(".provide")).click();
		driver.findElement(By.id("provider")).click();
		driver.findElement(By.cssSelector("p:nth-child(4) > .district")).click();
		driver.findElement(By.cssSelector("p:nth-child(2) > .district")).click();
	}

	@Test
	public void uTU58PreSaleUpdateCategoryParent() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(3) > .categoryParent")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".categoryParentSelect .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".categoryParentSelect > .categoryParent")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".categoryParentSelect > .categoryParent")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "商品大类U29555");
		{
			WebElement element = driver.findElement(By.cssSelector("#categoryParent > .rowSpace:nth-child(2)"));
			Actions builder = (Actions) new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".layui-container > .layui-row:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTU59PreSaleUpdateCategory() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .blackIcon:nth-child(5) > .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .category")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "666商品小类U33");
		driver.findElement(By.cssSelector(".layui-container > .layui-row:nth-child(2)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTU5updateDistrict4() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-edit")).click();
		vars.put("District", js.executeScript("return \"区域\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("p.provideDistrictSelect > input.district")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("District").toString());
		driver.findElement(By.id("commodityAboutMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("修改供应商区域成功"));
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(1)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.xpath("//div[@id=\'selectDistrict\']/div/div/dl/dd")).getText(), is(vars.get("District").toString()));
	}

	@Test
	public void uTU60PreSaleUpdateBrand() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#brand > .rowSpace:nth-child(1) .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "维他奶777");
		driver.findElement(By.cssSelector(".commodityBrand")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTU61PreSaleUpdateUnit() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector("#unit > .rowSpace:nth-child(1) .layui-icon")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .unit")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .unit")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "单位U37666");
		driver.findElement(By.cssSelector(".commodityUnit > .title")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTU62PreSaleUpdateProeprty() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("for(i=0;i<document.getElementsByClassName(\"blackIcon\").length;i++){document.getElementsByClassName(\"blackIcon\")[i].style.display=\"block\";}");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(1) .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name1")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name1")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "属性U418888");
		Thread.sleep(1000);
		driver.findElement(By.id("commodityAboutMain")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTU6checkProviderName() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.update > i.layui-icon.layui-icon-edit")).click();
		driver.findElement(By.id("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), " ");
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		vars.put("provider", js.executeScript("return \"    \";"));
		driver.findElement(By.id("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("provider").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		vars.put("provider", js.executeScript("return \"供应商 \"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文、数字和英文"));
		vars.put("provider", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("provider").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
	}

	@Test
	public void uTU7checkContactName() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".paddingLeft:nth-child(2) > input:nth-child(2)")).click();
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).click();
		Thread.sleep(1000);
		// 这里不能用keys.chord(keys.control,"a");,否则当做没做修改，不会弹出窗口
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("发过火 fghfh");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".paddingLeft:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).click();
		vars.put("contactName", js.executeScript("return \"  发过火fghf   \";"));
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("contactName").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首尾不能有空格"));
		driver.findElement(By.name("contactName")).click();
		vars.put("contactName", js.executeScript("return \"         发过火fghfh   \";"));
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("contactName").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首尾不能有空格"));
	}

	@Test
	public void uTU8checkAddress() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "retret    rtrtrtrt");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).click();
		vars.put("address", js.executeScript("return \"     retretrtrtrtrt\";"));
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("address").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首尾不能有空格"));
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).click();
		vars.put("address", js.executeScript("return \"     retretrtrtrtrt     \";"));
		driver.findElement(By.name("address")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("address").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首尾不能有空格"));
	}

	@Test
	public void uTU9checkMobile() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).click();
		// Thread.sleep(1000);
		// driver.findElement(By.cssSelector(".layui-col-space")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("mobile")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "144");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的字符不能少于7位"));
		vars.put("phone", js.executeScript("return \"1355\"+Math.round(Math.random()*9999999999)"));
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("phone").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
		driver.findElement(By.cssSelector(".provideSelect .layui-icon")).click();
		driver.findElement(By.cssSelector(".layui-row:nth-child(1)")).click();
		vars.put("phone", js.executeScript("return \"1566\"+Math.round(Math.random()*9999999999)"));
		driver.findElement(By.name("mobile")).sendKeys(Keys.chord(Keys.CONTROL, "a"), vars.get("phone").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改供应商成功"));
	}
}
