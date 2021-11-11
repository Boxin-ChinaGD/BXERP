package com.bx.erp.selenium.Shop;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import com.bx.erp.selenium.BaseSeleniumTest;
import org.testng.annotations.Test;

public class ShopIndex_UTest extends BaseSeleniumTest {
	@Test
	public void testCase1UpdateCompanyName3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		vars.put("companyName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; var companyName = \"\"; for(var i=0;i<8;i++){ companyName+=arr[Math.floor(Math.random()*26)]; }; return companyName;"));
		driver.findElement(By.cssSelector(".companyName")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys(vars.get("companyName").toString());
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定修改公司\"" + vars.get("companyName").toString() + "\"吗？"));
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));
	}

	@Test(dependsOnMethods = "testCase1UpdateCompanyName3")
	public void testCase2UpdateCompanyName4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:last-child > .storeArea")).click();
		vars.put("companyName", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector("li:nth-child(1) > .storeArea")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys(vars.get("companyName").toString());
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定修改公司\"" + vars.get("companyName").toString() + "\"吗？"));
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));
	}

	@Test(dependsOnMethods = "testCase2UpdateCompanyName4")
	public void testCase3UpdateCompanyBusinessLicenseSN1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).clear();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("ab1111111111111111");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".customerInformation > h3")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("公司营业执照号数据格式不对，请输入15或18位的数字或大写字母或它们的组合"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据格式不符合要求，请输入15位或18位的大写字母或数字或大写字母与数字的组合"));
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).clear();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("你好1111111111111111");
		driver.findElement(By.cssSelector(".customerInformation > h3")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("公司营业执照号数据格式不对，请输入15或18位的数字或大写字母或它们的组合"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据格式不符合要求，请输入15位或18位的大写字母或数字或大写字母与数字的组合"));
	}

	@Test(dependsOnMethods = "testCase3UpdateCompanyBusinessLicenseSN1")
	public void testCase4UpdateCompanyBusinessLicenseSN3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		vars.put("companyName", driver.findElement(By.name("name")).getAttribute("value"));
		vars.put("businessLicenseSN", js.executeScript("var businessLicenseSN = Math.floor(Math.random() * 1000000000000000000); return businessLicenseSN;"));
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).clear();
		driver.findElement(By.name("businessLicenseSN")).sendKeys(vars.get("businessLicenseSN").toString());
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定修改公司\"" + vars.get("companyName").toString() + "\"吗？"));
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));
	}

	// 此测试需要联合手动创建公司.
	// @Test(dependsOnMethods = "testCase4UpdateCompanyBusinessLicenseSN3")
	public void testCase5UpdateCompanyBusinessLicenseSN4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:last-child > .storeArea")).click();
		vars.put("businessLicenseSN", driver.findElement(By.name("businessLicenseSN")).getAttribute("value"));
		driver.findElement(By.cssSelector("li:nth-child(1) > .storeArea")).click();
		vars.put("companyName", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).clear();
		driver.findElement(By.name("businessLicenseSN")).sendKeys(vars.get("businessLicenseSN").toString());
		driver.findElement(By.cssSelector(".customerInformation > h3")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("该营业执照号已存在"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定修改公司\"" + vars.get("companyName").toString() + "\"吗？"));
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("其它公司已经存在相同的营业执照"));

	}

	@Test(dependsOnMethods = "testCase4UpdateCompanyBusinessLicenseSN3")
	public void testCase6UpdateCompanyBossName1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).clear();
		driver.findElement(By.name("bossName")).sendKeys("老板1号");
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).clear();
		driver.findElement(By.name("bossName")).sendKeys("老板@号");
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));

	}

	@Test(dependsOnMethods = "testCase6UpdateCompanyBossName1")
	public void testCase7UpdateCompanyBossName3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		vars.put("companyName", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).clear();
		driver.findElement(By.name("bossName")).sendKeys("正确的老板名字");
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定修改公司\"" + vars.get("companyName").toString() + "\"吗？"));
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));

	}

	@Test(dependsOnMethods = "testCase7UpdateCompanyBossName3")
	public void testCase8UpdateCompanyBossName4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:last-child > .storeArea")).click();
		vars.put("bossName", driver.findElement(By.name("bossName")).getAttribute("value"));
		driver.findElement(By.cssSelector("li:nth-child(1) > .storeArea")).click();
		vars.put("companyName", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).clear();
		driver.findElement(By.name("bossName")).sendKeys(vars.get("bossName").toString());
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定修改公司\"" + vars.get("companyName").toString() + "\"吗？"));
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));

	}

	@Test(dependsOnMethods = "testCase8UpdateCompanyBossName4")
	public void testCase9UpdateCompanyBossPhone1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).clear();
		driver.findElement(By.name("bossPhone")).sendKeys("131b3615881");
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		Thread.sleep(2000);
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).clear();
		driver.findElement(By.name("bossPhone")).sendKeys("131啊3615881");
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));

	}

	@Test(dependsOnMethods = "testCase9UpdateCompanyBossPhone1")
	public void testCase10UpdateCompanyBossPhone3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		vars.put("companyName", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).clear();
		driver.findElement(By.name("bossPhone")).sendKeys("15200702355");
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定修改公司\"" + vars.get("companyName").toString() + "\"吗？"));
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));
	}

	@Test(dependsOnMethods = "testCase10UpdateCompanyBossPhone3")
	public void testCase11UpdateCompanyBossPhone4() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:last-child > .storeArea")).click();
		vars.put("bossPhone", driver.findElement(By.name("bossPhone")).getAttribute("value"));
		driver.findElement(By.cssSelector("li:nth-child(1) > .storeArea")).click();
		vars.put("companyName", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).clear();
		driver.findElement(By.name("bossPhone")).sendKeys(vars.get("bossPhone").toString());
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定修改公司\"" + vars.get("companyName").toString() + "\"吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));

	}

	@Test(dependsOnMethods = "testCase11UpdateCompanyBossPhone4")
	public void testCase12UpdateCompanyBossWechat1() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).clear();
		driver.findElement(By.name("bossWechat")).sendKeys("wechataaa你好");
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必须以字母开头，5位以上20位以下，由字母、数字、下划线和减号组成，不支持设置中文"));
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).clear();
		driver.findElement(By.name("bossWechat")).sendKeys("123wechataaa");
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必须以字母开头，5位以上20位以下，由字母、数字、下划线和减号组成，不支持设置中文"));

	}

	@Test(dependsOnMethods = "testCase12UpdateCompanyBossWechat1")
	public void testCase13UpdateCompanyBossWechat3() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		vars.put("companyName", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		vars.put("bossWechat", js.executeScript("var bossWechat = \"wechat\" + Math.floor(Math.random() * 1000000); return bossWechat;"));
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).clear();
		driver.findElement(By.name("bossWechat")).sendKeys(vars.get("bossWechat").toString());
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定修改公司\"" + vars.get("companyName").toString() + "\"吗？"));
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));

	}

	@Test(dependsOnMethods = "testCase13UpdateCompanyBossWechat3")
	public void testCase14UpdateCompanyBossWechat4() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:last-child > .storeArea")).click();
		vars.put("bossWechat", driver.findElement(By.name("bossWechat")).getAttribute("value"));
		driver.findElement(By.cssSelector("li:nth-child(1) > .storeArea")).click();
		vars.put("companyName", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).sendKeys(vars.get("bossWechat").toString());
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定修改公司\"" + vars.get("companyName").toString() + "\"吗？"));
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));

	}

	@Test(dependsOnMethods = "testCase14UpdateCompanyBossWechat4")
	public void testCase15UpdateCompanyBrandName2() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		vars.put("maxlength", js.executeScript("var maxlength = document.getElementsByClassName(\"brandName\")[0].getElementsByTagName(\"input\")[0].getAttribute(\"maxlength\"); return maxlength;"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'20\')", vars.get("maxlength"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}

	}

	@Test(dependsOnMethods = "testCase15UpdateCompanyBrandName2")
	public void testCase16UpdateCompanyBrandName3() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		vars.put("brandName", js.executeScript("return \"新的门店招牌\" + Math.floor(Math.random() * 999);"));
		System.out.println(vars.get("brandName").toString());
		driver.findElement(By.name("brandName")).click();
		driver.findElement(By.name("brandName")).clear();
		driver.findElement(By.name("brandName")).sendKeys(vars.get("brandName").toString());
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));
		{
			String value = driver.findElement(By.name("brandName")).getAttribute("value");
			assertThat(value, is(vars.get("brandName").toString()));
		}

	}

	@Test(dependsOnMethods = "testCase16UpdateCompanyBrandName3")
	public void testCase16UpdateCompanyBrandName4() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		vars.put("brandName", js.executeScript("return \"新的门店招牌\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("brandName")).click();
		driver.findElement(By.name("brandName")).clear();
		driver.findElement(By.name("brandName")).sendKeys(vars.get("brandName").toString());
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));
		driver.findElement(By.cssSelector("li:last-child > .storeArea")).click();
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("brandName")).click();
		driver.findElement(By.name("brandName")).clear();
		driver.findElement(By.name("brandName")).sendKeys(vars.get("brandName").toString());
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改公司信息成功"));

	}

	@Test(dependsOnMethods = "testCase16UpdateCompanyBrandName4")
	public void testCase17CheckCompanySubid1() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(5000);
		driver.findElement(By.cssSelector("li:nth-child(1) > .storeArea")).click();
		driver.findElement(By.cssSelector(".layui-btn-xs")).click();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).clear();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).sendKeys("@##$$#^^%%");
		driver.findElement(By.cssSelector(".layui-input-block > .layui-btn:nth-child(2)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("只允许自然数"));
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).clear();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).sendKeys("werrr44455");
		driver.findElement(By.cssSelector(".layui-input-block > .layui-btn:nth-child(2)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("只允许自然数"));
		Thread.sleep(5000);
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).clear();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).sendKeys("werrghyguu");
		driver.findElement(By.cssSelector(".layui-input-block > .layui-btn:nth-child(2)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("只允许自然数"));
	}

	@Test(dependsOnMethods = "testCase17CheckCompanySubid1")
	public void testCase18UpdateCompanySubid1() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		vars.put("submchid", driver.findElement(By.name("submchid")).getAttribute("value"));
		driver.findElement(By.cssSelector(".layui-btn-xs")).click();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).clear();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).sendKeys(vars.get("submchid").toString());
		driver.findElement(By.cssSelector(".layui-input-block > .layui-btn:nth-child(2)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改子商户号成功"));

	}

	@Test(dependsOnMethods = "testCase18UpdateCompanySubid1")
	public void testCase19UpdateCompanySubid4() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("li:last-child > .storeArea")).click();
		vars.put("submchid", driver.findElement(By.name("submchid")).getAttribute("value"));
		driver.findElement(By.cssSelector("li:nth-child(1) > .storeArea")).click();
		driver.findElement(By.cssSelector(".layui-btn-xs")).click();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).clear();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).sendKeys(vars.get("submchid").toString());
		driver.findElement(By.cssSelector(".layui-input-block > .layui-btn:nth-child(2)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content:nth-child(1)")).getText(), is(""));
	}

	@Test(dependsOnMethods = "testCase19UpdateCompanySubid4")
	public void testCase20UpdateCompanySubid5() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-xs")).click();
		vars.put("submchid", js.executeScript("var submchid = Math.floor(Math.random() * 10000000000); return submchid;"));
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).clear();
		driver.findElement(By.cssSelector(".layui-input-block > .layui-input")).sendKeys(vars.get("submchid").toString());
		driver.findElement(By.cssSelector(".layui-input-block > .layui-btn:nth-child(2)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改子商户号成功"));

	}

	@Test(dependsOnMethods = "testCase20UpdateCompanySubid5")
	public void testCase21EaseOfUseTips() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.cssSelector("li:nth-child(2) > .storeArea")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		driver.findElement(By.cssSelector(".cancel")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));

	}

	@Test(dependsOnMethods = "testCase21EaseOfUseTips")
	public void testCase22UpdateCompanyThenCancel() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		vars.put("shopName", driver.findElement(By.cssSelector(".choosed label")).getText());
		{
			String value = driver.findElement(By.cssSelector(".shopName > input")).getAttribute("value");
			assertThat(value, is(vars.get("shopName").toString()));
		}
		vars.put("companyName", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".cancel")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(2000);
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector(".choosed label")).getText(), is(vars.get("shopName").toString()));
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("companyName").toString()));
		}

	}

	@Test(dependsOnMethods = "testCase22UpdateCompanyThenCancel")
	public void testCase23UpdateCompanyName1() throws InterruptedException {

		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("存在数字1");
		driver.findElement(By.cssSelector(".customerInformation > h3")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("公司名称数据格式不对，请输入中文或英文，不能为空"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("存在特殊符号@");
		driver.findElement(By.cssSelector(".customerInformation > h3")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("公司名称数据格式不对，请输入中文或英文，不能为空"));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.cssSelector(".toUpdateCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));
	}
}
