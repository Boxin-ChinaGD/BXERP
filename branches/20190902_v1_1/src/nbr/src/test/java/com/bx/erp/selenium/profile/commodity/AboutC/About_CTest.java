package com.bx.erp.selenium.profile.commodity.AboutC;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

import org.openqa.selenium.By;
import com.bx.erp.selenium.BaseSeleniumTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class About_CTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void testCase1CheckDistrictLength() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("span.whiteIcon.add")).click();
		vars.put("District", js.executeScript("return \"供应商区域长度\"+Math.round(Math.random()*999999999999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).sendKeys(vars.get("District").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(5000);
//		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商区域成功"));
	}
	
	@Test
	public void testx() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-rmb")).click();
	    driver.findElement(By.linkText("促销")).click();
	    driver.findElement(By.linkText("满减优惠")).click();
	    driver.switchTo().frame(1);
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".createPromotion")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".layui-input-inline > div:nth-child(2)")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector("div:nth-child(2) > .promotionScope")).click();
	    driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
	    Thread.sleep(2000);
	    vars.put("count1", driver.findElement(By.cssSelector("#layui-laypage-3 > .layui-laypage-count")).getText());
	    System.out.println(vars.get("count1").toString());
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("商品");
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".commoditySearch")).click();
	    Thread.sleep(2000);
	    vars.put("count2", driver.findElement(By.cssSelector("#layui-laypage-6 > .layui-laypage-count")).getText());
	    System.out.println(vars.get("count2").toString());
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
//	    driver.findElement(By.cssSelector(".topArea > .layui-input")).clear();
	    driver.findElement(By.cssSelector(".commoditySearch")).click();
	    Thread.sleep(2000);
	    assertThat(driver.findElement(By.cssSelector("#layui-laypage-7 > .layui-laypage-count")).getText(), is(vars.get("count1").toString()));
	    driver.findElement(By.cssSelector(".topArea > .layui-input")).click();
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".topArea > .layui-input")).sendKeys("商品");
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".commoditySearch")).click();
	    Thread.sleep(3000);
	    assertThat(driver.findElement(By.cssSelector("#layui-laypage-11 > .layui-laypage-count")).getText(), is(vars.get("count2").toString()));
	}

	@Test(dependsOnMethods = "testCase1CheckDistrictLength")
	public void testCase2CreateDistrict1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("span.whiteIcon.add")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("commodityAboutMain")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
	}

	@Test(dependsOnMethods = "testCase2CreateDistrict1")
	public void testCase3CreateDistrict2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("p:nth-child(2) > .whiteIcon:nth-child(5)")).click();
		driver.findElement(By.cssSelector(".newDistrict")).clear();
		driver.findElement(By.cssSelector(".newDistrict")).sendKeys("        ");
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
		driver.findElement(By.cssSelector("p:nth-child(2) > .whiteIcon:nth-child(5)")).click();
		driver.findElement(By.cssSelector(".newDistrict")).clear();
		driver.findElement(By.cssSelector(".newDistrict")).sendKeys("默认区域");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("该供应商区域已存在，请重新修改"));
	}

	@Test(dependsOnMethods = "testCase3CreateDistrict2")
	public void testCase4CreateDistrict3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("span.whiteIcon.add")).click();
		vars.put("District", js.executeScript("return \"区域\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).sendKeys(vars.get("District").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商区域成功"));
	}

	@Test(dependsOnMethods = "testCase4CreateDistrict3")
	public void testCase5CreateDistrict4() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("span.whiteIcon.add")).click();
		vars.put("District", js.executeScript("return \"区域\"+Math.round(Math.random()*999999)"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).sendKeys(vars.get("District").toString());
		driver.findElement(By.cssSelector("body")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商区域成功"));
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		driver.findElement(By.cssSelector("input.layui-input.layui-unselect")).click();
		driver.findElement(By.cssSelector("dd.layui-this")).click();
		{
			Thread.sleep(2000);
			String value = driver.findElement(By.cssSelector(".layui-unselect:nth-child(0)")).getAttribute("value");
			assertThat(value, is(vars.get("District").toString()));
		}
	}

	@Test(dependsOnMethods = "testCase5CreateDistrict4")
	public void testCase6CheckProviderName() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("p.rowSpace.paddingLeft > input[type=\"text\"]")).click();
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider1", js.executeScript("return \"供应商 \"+Math.round(Math.random()*99999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider1").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("只允许中文、数字和英文"));
		vars.put("provider2", js.executeScript("return \" 供应商\"+Math.round(Math.random()*99999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider2").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("只允许中文、数字和英文"));
	}

	@Test(dependsOnMethods = "testCase6CheckProviderName")
	public void testCase7CheckContactName() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("yqgg");
		driver.findElement(By.name("contactName")).click();
		vars.put("contactName", js.executeScript("return \"   xianxian\";"));
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys(vars.get("contactName").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首尾不能有空格"));
		driver.findElement(By.name("contactName")).click();
		vars.put("contactName", js.executeScript("return \"xianxian     \";"));
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys(vars.get("contactName").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首尾不能有空格"));
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("xianxian￥#￥#");
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("允许以中英数值、空格形式出现，不允许使用特殊符号"));
	}

	@Test(dependsOnMethods = "testCase7CheckContactName")
	public void testCase9CheckAddress() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".provideSelect")).click();
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		driver.findElement(By.id("name")).click();
		vars.put("providerName", js.executeScript("return \"供应商名称\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("providerName").toString());
		driver.findElement(By.name("address")).click();
		vars.put("address", js.executeScript("return \"     地址\";"));
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys(vars.get("address").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首尾不能有空格"));
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).click();
		vars.put("address", js.executeScript("return \"地址     \";"));
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys(vars.get("address").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首尾不能有空格"));
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("地  址");
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商成功"));
	}

	@Test(dependsOnMethods = "testCase9CheckAddress")
	public void testCase10CreateProvider1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("p.rowSpace.paddingLeft > input[type=\"text\"]")).click();
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase10CreateProvider1")
	public void testCase12CreateProvider3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("p.rowSpace.paddingLeft > input[type=\"text\"]")).click();
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("56");
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("765");
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("123456");
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("56j");
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase12CreateProvider3")
	public void testCase13CreateProvider4() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		vars.put("provider1", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider1").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
	}

	@Test(dependsOnMethods = "testCase13CreateProvider4")
	public void testCase15CreateProvider6() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".paddingLeft:nth-child(10) > input:nth-child(2)")).click();
		driver.findElement(By.cssSelector("input.district")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		vars.put("provider1", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider1").toString());
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("联系人C15");
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("地址C15");
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).click();
		vars.put("mobile", js.executeScript("return \"1\"+Math.round(Math.random()*9999999999)"));
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys(vars.get("mobile").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
	}

	@Test(dependsOnMethods = "testCase15CreateProvider6")
	public void testCase16CreateProvider7() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".provideSelect")).click();
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("默认供应商");
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该供应商名称已存在"));
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该供应商已存在，请重新修改"));
	}

	@Test(dependsOnMethods = "testCase16CreateProvider7")
	public void testCase17CreateProvider8() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider1", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider1").toString());
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("联系人C17");
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider2", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider2").toString());
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("联系人C17");
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
	}

	@Test(dependsOnMethods = "testCase17CreateProvider8")
	public void testCase18CreateProvider9() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		driver.findElement(By.id("name")).click();
		vars.put("provider", js.executeScript("return\"供应商\" + Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.name("mobile")).click();
		vars.put("phone", js.executeScript("return \"135\"+Math.round(Math.random()*999999999)"));
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		vars.put("provider", js.executeScript("return \"供应商\" +Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys(vars.get("phone").toString());
		Thread.sleep(2000);
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该联系人电话已存在"));
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该联系电话已存在，请重新修改"));
	}

	@Test(dependsOnMethods = "testCase18CreateProvider9")
	public void testCase19createProvider10() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider1", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider1").toString());
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("地址C19");
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider2", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider2").toString());
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("地址C19");
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
	}

	@Test(dependsOnMethods = "testCase19createProvider10")
	public void testCase20createProvider11() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider1", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider1").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider2", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(vars.get("provider2").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
	}

	@Test(dependsOnMethods = "testCase20createProvider11")
	public void testCase21providerCancelButton() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		vars.put("providerName", driver.findElement(By.cssSelector(".provideSelect > input:nth-child(2)")).getAttribute("value"));
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("供应商C21");
		driver.findElement(By.cssSelector("input.commodityUnButton")).click();
		driver.findElement(By.linkText("确定")).click();
		{
			Thread.sleep(2000);
			String value = driver.findElement(By.id("name")).getAttribute("value");
			assertThat(value, is(vars.get("providerName").toString()));
		}
	}

	@Test(dependsOnMethods = "testCase21providerCancelButton")
	public void testCase22checkBrandLength() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.id("brandCreateButton")).click();
		vars.put("brand", js.executeScript("return \"品牌长度\"+Math.round(Math.random()*999999999999999999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).sendKeys(vars.get("brand").toString());
		driver.findElement(By.id("brandCreateButton")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("品牌名字为英文或数字的组合，长度为1到20"));
		vars.put("brand", js.executeScript("return \"品牌\"+Math.round(Math.random()*999999999999999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).sendKeys(vars.get("brand").toString());
		driver.findElement(By.id("brandCreateButton")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品品牌成功"));
	}

	@Test(dependsOnMethods = "testCase22checkBrandLength")
	public void testCase23createBrand1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.id("brandCreateButton")).click();
		driver.findElement(By.id("brandCreateButton")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
	}

	@Test(dependsOnMethods = "testCase23createBrand1")
	public void testCase24createBrand2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.id("brandCreateButton")).click();
		vars.put("brand", js.executeScript("return \"     二琛\";"));
		driver.findElement(By.cssSelector(".newBrand")).clear();
		driver.findElement(By.cssSelector(".newBrand")).sendKeys(vars.get("brand").toString());
		driver.findElement(By.id("brandButton")).click();
		driver.findElement(By.id("brandCreateButton")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".newBrand")).click();
		driver.findElement(By.cssSelector(".newBrand")).click();
		vars.put("brand", js.executeScript("return \"二琛     \";"));
		driver.findElement(By.cssSelector(".newBrand")).clear();
		driver.findElement(By.cssSelector(".newBrand")).sendKeys(vars.get("brand").toString());
		driver.findElement(By.id("brandCreateButton")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".newBrand")).click();
		driver.findElement(By.cssSelector(".newBrand")).click();
		vars.put("brand", js.executeScript("return \"二   琛\";"));
		driver.findElement(By.cssSelector(".newBrand")).clear();
		driver.findElement(By.cssSelector(".newBrand")).sendKeys(vars.get("brand").toString());
		driver.findElement(By.id("brandCreateButton")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\\\\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".newBrand")).click();
		driver.findElement(By.cssSelector(".newBrand")).click();
		driver.findElement(By.cssSelector(".newBrand")).clear();
		driver.findElement(By.cssSelector(".newBrand")).sendKeys("                  ");
		driver.findElement(By.id("brandButton")).click();
		driver.findElement(By.id("brandCreateButton")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
		driver.findElement(By.id("brandCreateButton")).click();
		driver.findElement(By.cssSelector(".newBrand")).click();
		driver.findElement(By.cssSelector(".newBrand")).clear();
		driver.findElement(By.cssSelector(".newBrand")).sendKeys("￥￥#￥%￥#");
		driver.findElement(By.id("brandCreateButton")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\\\\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector(".newBrand")).click();
		driver.findElement(By.cssSelector(".newBrand")).clear();
		driver.findElement(By.cssSelector(".newBrand")).sendKeys("默认品牌");
		driver.findElement(By.id("brandCreateButton")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("该商品品牌已存在，请重新修改"));
	}

	@Test(dependsOnMethods = "testCase24createBrand2")
	public void testCase25createBrand3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("brandCreateButton")).click();
		vars.put("brand", js.executeScript("return \"品牌\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).sendKeys(vars.get("brand").toString());
		driver.findElement(By.id("brandCreateButton")).click();
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品品牌成功"));
	}

	@Test(dependsOnMethods = "testCase25createBrand3")
	public void testCase26brandCancelButton() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		vars.put("brandName", driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).getAttribute("value"));
		driver.findElement(By.id("brandCreateButton")).click();
		vars.put("brand", js.executeScript("return \"品牌\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).sendKeys(vars.get("brand").toString());
		driver.findElement(By.id("brandExitButton")).click();
		driver.findElement(By.linkText("确定")).click();
		{
			String value = driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .brand")).getAttribute("value");
			assertThat(value, is(vars.get("brandName").toString()));
		}
	}

	@Test(dependsOnMethods = "testCase26brandCancelButton")
	public void testCase27checkCategoryParentLength() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.add")).click();
		vars.put("categoryParent", js.executeScript("return \"大类长度\"+Math.round(Math.random()*9999999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys(vars.get("categoryParent").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("输入数据的格式不正确"));
		vars.put("categoryParent", js.executeScript("return \"大类\"+Math.round(Math.random()*99999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys(vars.get("categoryParent").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品大类成功"));
	}

	@Test(dependsOnMethods = "testCase27checkCategoryParentLength")
	public void testCase28createCategoryParent1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.add")).click();
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys("    ");
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
	}

	@Test(dependsOnMethods = "testCase28createCategoryParent1")
	public void testCase29createCategoryParent2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(4) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.add")).click();
		vars.put("categoryParent1", js.executeScript("return \"      \""));
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys(vars.get("categoryParent1").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.add")).click();
		vars.put("categoryParent2", js.executeScript("return \"  大类\"+Math.round(Math.random()*99999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys(vars.get("categoryParent2").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("输入的值不符合数据格式\\\\n数据要求为：中英文数字，不能输入空格"));
		vars.put("categoryParent3", js.executeScript("return \"大类  \"+Math.round(Math.random()*99999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys(vars.get("categoryParent3").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("输入的值不符合数据格式\\\\n数据要求为：中英文数字，不能输入空格"));
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys("大类@");
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("输入的值不符合数据格式\\\\n数据要求为：中英文数字，不能输入空格"));
	}

	@Test(dependsOnMethods = "testCase29createCategoryParent2")
	public void testCase30createCategoryParent3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(3) > .categoryParent")).click();
		driver.findElement(By.cssSelector("p.rowSpace.categoryParentSelect > span.blackIcon.add")).click();
		vars.put("categoryParent", js.executeScript("return \"大类\"+Math.round(Math.random()*99999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys(vars.get("categoryParent").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品大类成功"));
	}

	@Test(dependsOnMethods = "testCase30createCategoryParent3")
	public void testCase31checkCategoryLength() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.add")).click();
		vars.put("category", js.executeScript("return \"小类长度\"+Math.round(Math.random()*9999999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("非法的值"));
		vars.put("category", js.executeScript("return \"小类\"+Math.round(Math.random()*99999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品小类成功"));
	}

	@Test(dependsOnMethods = "testCase31checkCategoryLength")
	public void testCase32createCategory1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .blackIcon:nth-child(6)")).click();
		driver.findElement(By.cssSelector(".layui-col-md6 > .title")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
	}

	@Test(dependsOnMethods = "testCase32createCategory1")
	public void testCase33createCategory2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .blackIcon:nth-child(6)")).click();
		vars.put("category", js.executeScript("return \"         \";"));
		driver.findElement(By.cssSelector(".newCategory")).clear();
		driver.findElement(By.cssSelector(".newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector(".commodityCategory")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .blackIcon:nth-child(6)")).click();
		vars.put("category", js.executeScript("return \"      二琛\";"));
		driver.findElement(By.cssSelector(".newCategory")).clear();
		driver.findElement(By.cssSelector(".newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector(".layui-container > .layui-row:nth-child(2)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\\\\n数据要求为：中英文数字，不能输入空格"));
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector(".newCategory")).click();
		vars.put("category", js.executeScript("return \"二琛     \";"));
		driver.findElement(By.cssSelector(".newCategory")).clear();
		driver.findElement(By.cssSelector(".newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector(".layui-col-md6 > .title")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\\\\n数据要求为：中英文数字，不能输入空格"));
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector(".newCategory")).click();
		vars.put("category", js.executeScript("return \"二    琛\";"));
		driver.findElement(By.cssSelector(".newCategory")).clear();
		driver.findElement(By.cssSelector(".newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector(".commodityCategory")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\\\\n数据要求为：中英文数字，不能输入空格"));
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector(".newCategory")).click();
		driver.findElement(By.cssSelector(".newCategory")).clear();
		driver.findElement(By.cssSelector(".newCategory")).sendKeys("二琛%￥#%￥%");
		driver.findElement(By.cssSelector(".layui-container > .layui-row:nth-child(2)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("输入的值不符合数据格式\\\\n数据要求为：中英文数字，不能输入空格"));
	}

	@Test(dependsOnMethods = "testCase33createCategory2")
	public void testCase34createCategory3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.add")).click();
		vars.put("category", js.executeScript("return \"小类\"+Math.round(Math.random()*9999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品小类成功"));
	}

	@Test(dependsOnMethods = "testCase34createCategory3")
	public void testCase35checkUnitLength() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("unitCreateButton")).click();
		vars.put("unit", js.executeScript("return \"单位长度\"+Math.round(Math.random()*99999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys(vars.get("unit").toString());
		driver.findElement(By.id("unitCreateButton")).click();
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("服务器错误"));
		vars.put("unit", js.executeScript("return \"单位\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys(vars.get("unit").toString());
		driver.findElement(By.id("unitCreateButton")).click();
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品包装单位成功"));
	}

	@Test(dependsOnMethods = "testCase35checkUnitLength")
	public void testCase37createUnit2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("unitCreateButton")).click();
		vars.put("unit", js.executeScript("return \"    \";"));
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys(vars.get("unit").toString());
		driver.findElement(By.id("unitCreateButton")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("数据不符合要求,不能为空且不能全为空格！"));
		driver.findElement(By.id("unitCreateButton")).click();
		vars.put("unit2", js.executeScript("return \"单位@\"+Math.round(Math.random()*9999)"));
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys(vars.get("unit2").toString());
		driver.findElement(By.id("unitCreateButton")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("输入的值不符合数据格式\\\\n数据要求为：中英文数字、空格，不能只输入空格"));
		driver.findElement(By.id("unitCreateButton")).click();
		vars.put("unit3", js.executeScript("return \"单位\"+Math.round(Math.random()*9999)"));
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys(vars.get("unit3").toString());
		driver.findElement(By.id("unitCreateButton")).click();
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品包装单位成功"));
		driver.findElement(By.id("unitCreateButton")).click();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys(vars.get("unit3").toString());
		driver.findElement(By.id("unitCreateButton")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("该商品单位已存在，请重新修改"));
	}

	@Test(dependsOnMethods = "testCase37createUnit2")
	public void testCase38createUnit3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("unitCreateButton")).click();
		vars.put("unit", js.executeScript("return \"单位\"+Math.round(Math.random()*9999)"));
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys(vars.get("unit").toString());
		driver.findElement(By.id("unitCreateButton")).click();
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品包装单位成功"));
	}

	@Test(dependsOnMethods = "testCase38createUnit3")
	public void testCase39unitCancelButton() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		vars.put("unitName", driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .unit")).getAttribute("value"));
		driver.findElement(By.id("unitCreateButton")).click();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys("单位C39");
		driver.findElement(By.id("unitExitButton")).click();
		driver.findElement(By.linkText("取消")).click();
		{
			String value = driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .unit")).getAttribute("value");
			assertThat(value, is(vars.get("unitName").toString()));
		}
	}

	@Test(dependsOnMethods = "testCase39unitCancelButton")
	public void testCase40checkUniqueProviderName() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("默认供应商");
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该供应商名称已存在"));
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该供应商已存在，请重新修改"));
	}

	@Test(dependsOnMethods = "testCase40checkUniqueProviderName")
	public void testCase41checkUniqueCategoryName() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.add")).click();
		vars.put("category", js.executeScript("return \"小类\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增商品小类成功"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .blackIcon:nth-child(6)")).click();
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).sendKeys(vars.get("category").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.id("checkUniqueFieldMsg")).getText(), is("该商品小类已存在"));
	}

	@Test(dependsOnMethods = "testCase41checkUniqueCategoryName")
	public void testCase43visitedOtherDistrictWhenCreate1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("p:nth-child(2) > .whiteIcon:nth-child(5)")).click();
		vars.put("District", js.executeScript("return \"区域\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector(".newDistrict")).clear();
		driver.findElement(By.cssSelector(".newDistrict")).sendKeys(vars.get("District").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商区域成功"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .district")).click();
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		vars.put("providerName", driver.findElement(By.cssSelector(".paddingLeft:nth-child(1) > input:nth-child(2)")).getAttribute("value"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .district")).click();
		assertThat(driver.findElement(By.cssSelector(".paddingLeft > input:nth-child(2)")).getText(), is(not("vars.get(\"providerName\").toString()")));
	}

	@Test(dependsOnMethods = "testCase43visitedOtherDistrictWhenCreate1")
	public void testCase45visitedOtherDistrictWhenCreate2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("p:nth-child(2) > .whiteIcon:nth-child(5)")).click();
		vars.put("District", js.executeScript("return \"区域\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector(".newDistrict")).clear();
		driver.findElement(By.cssSelector(".newDistrict")).sendKeys(vars.get("District").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商区域成功"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(1) > .district")).click();
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(1)")).click();
		driver.findElement(By.cssSelector("dd:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .district")).click();
		driver.findElement(By.linkText("确定")).click();
		{
			String value = driver.findElement(By.cssSelector(".layui-unselect:nth-child(1)")).getAttribute("value");
			assertThat(value, is("默认区域"));
		}
	}

	@Test(dependsOnMethods = "testCase45visitedOtherDistrictWhenCreate2")
	public void testCase48visitedOtheProviderWhenCreate4() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("供应商");
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("联系人");
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("地址");
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("13735345634");
		driver.findElement(By.cssSelector("#provider > p:nth-child(3)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
	}

	@Test(dependsOnMethods = "testCase48visitedOtheProviderWhenCreate4")
	public void testCase49visitedOtherDistrictWhenCreate4() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("供应商");
		driver.findElement(By.name("contactName")).click();
		driver.findElement(By.name("contactName")).clear();
		driver.findElement(By.name("contactName")).sendKeys("联系人");
		driver.findElement(By.name("address")).click();
		driver.findElement(By.name("address")).clear();
		driver.findElement(By.name("address")).sendKeys("地址");
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("13735345634");
		driver.findElement(By.cssSelector("p:nth-child(3) > .district")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();

		operatorType = EnumOperatorType.EOT_PRESALE.getIndex(); // 为下面测试而准备
	}

	@Test(dependsOnMethods = "testCase49visitedOtherDistrictWhenCreate4")
	public void testCase50PreSaleCreateDistrict() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		driver.findElement(By.cssSelector("p:nth-child(2) > .district")).click();
		driver.findElement(By.cssSelector("span.whiteIcon.add")).click();
		vars.put("District", js.executeScript("return \"区域\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newDistrict")).sendKeys(vars.get("District").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test(dependsOnMethods = "testCase50PreSaleCreateDistrict")
	public void testCase51PreSaleCreateProvider() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test(dependsOnMethods = "testCase51PreSaleCreateProvider")
	public void testCase52PreSaleCreateBrand() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		driver.findElement(By.id("brandCreateButton")).click();
		vars.put("brand", js.executeScript("return \"品牌\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newBrand")).sendKeys(vars.get("brand").toString());
		driver.findElement(By.id("brandCreateButton")).click();
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test(dependsOnMethods = "testCase52PreSaleCreateBrand")
	public void testCase53PreSaleCreateCategoryParent() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .categoryParent")).click();
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("CategoryParent", js.executeScript("return \"分类\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategoryParent")).sendKeys(vars.get("CategoryParent").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test(dependsOnMethods = "testCase53PreSaleCreateCategoryParent")
	public void testCase54PreSaleCreateCategory() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		driver.findElement(By.cssSelector("#category > p.rowSpace > span.blackIcon.add")).click();
		vars.put("Category", js.executeScript("return \"分类\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newCategory")).sendKeys(vars.get("Category").toString());
		driver.findElement(By.cssSelector("body")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test(dependsOnMethods = "testCase54PreSaleCreateCategory")
	public void testCase55PreSaleCreateUnit() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		driver.findElement(By.id("unitCreateButton")).click();
		vars.put("Unit", js.executeScript("return \"单位\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).clear();
		driver.findElement(By.cssSelector("input.layui-input.newUnit")).sendKeys(vars.get("Unit").toString());
		driver.findElement(By.id("unitCreateButton")).click();
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));

		operatorType = EnumOperatorType.EOT_BOSS.getIndex(); // 修改后面测试的登录者
	}

	@Test(dependsOnMethods = "testCase55PreSaleCreateUnit")
	public void testCase56createCategoryParent() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".rowSpace:nth-child(5) > .categoryParent")).click();
		driver.findElement(By.cssSelector(".categoryParentSelect > .add")).click();
		driver.findElement(By.cssSelector(".newCategoryParent")).clear();
		driver.findElement(By.cssSelector(".newCategoryParent")).sendKeys("蔬菜水果");
		driver.findElement(By.cssSelector(".commodityCategory")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("该商品大类已存在，请重新修改"));
	}
}
