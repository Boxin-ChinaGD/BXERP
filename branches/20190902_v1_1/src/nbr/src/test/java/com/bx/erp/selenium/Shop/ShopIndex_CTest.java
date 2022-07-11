package com.bx.erp.selenium.Shop;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class ShopIndex_CTest extends BaseSeleniumTest {

	@Test
	public void testCase1CheckCompanyName() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);

		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase1CheckCompanyName")
	public void testCase2CheckCompanyName() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("12345");
		driver.findElement(By.cssSelector(".shopDetails")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("公司名称数据格式不对，请输入中文或英文，不能为空"));
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));
		driver.findElement(By.cssSelector(".companyName")).click();
		driver.findElement(By.name("name")).sendKeys("@#");
		driver.findElement(By.cssSelector(".customerInformation > h3")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("公司名称数据格式不对，请输入中文或英文，不能为空"));
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));
	}

	@Test(dependsOnMethods = "testCase2CheckCompanyName")
	public void testCase3CheckCompanyBusinessLicenseSN() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase3CheckCompanyBusinessLicenseSN")
	public void testCase4CheckCompanyBusinessLicenseSN() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("123456789");
		driver.findElement(By.cssSelector(".customerInformation > h3")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("公司营业执照号数据格式不对，请输入15或18位的数字或大写字母或它们的组合"));
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据格式不符合要求，请输入15位或18位的大写字母或数字或大写字母与数字的组合"));
		driver.findElement(By.cssSelector("table:nth-child(8) tr:nth-child(2)")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("你好啊");
		driver.findElement(By.cssSelector(".customerInformation > h3")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("公司营业执照号数据格式不对，请输入15或18位的数字或大写字母或它们的组合"));
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("数据格式不符合要求，请输入15位或18位的大写字母或数字或大写字母与数字的组合"));
	}

	@Test(dependsOnMethods = "testCase4CheckCompanyBusinessLicenseSN")
	public void testCase5CheckCompanySubmchid() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("submchid")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("submchid")).sendKeys("123456789");
		driver.findElement(By.cssSelector(".customerInformation > h3")).click();
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("格式不正确，请输入10位数字"));
	}

	@Test(dependsOnMethods = "testCase5CheckCompanySubmchid")
	public void testCase6CheckCompanyBossName1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase6CheckCompanyBossName1")
	public void testCase7CheckCompanyBossName2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("123");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));
		driver.findElement(By.cssSelector("table:nth-child(8) tr:nth-child(5)")).click();
		driver.findElement(By.name("bossName")).sendKeys("你好@");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));
	}

	@Test(dependsOnMethods = "testCase7CheckCompanyBossName2")
	public void testCase8CheckCompanyBossPhone1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.cssSelector("table:nth-child(8) > tbody")).click();
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase8CheckCompanyBossPhone1")
	public void testCase9CheckCompanyBossPhone2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("123456789");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("123456789你好");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
	}

	@Test(dependsOnMethods = "testCase9CheckCompanyBossPhone2")
	public void testCase10CheckCompanyBossPassword1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("12345678999");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase10CheckCompanyBossPassword1")
	public void testCase11CheckCompanyBossPassword2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("13465789999");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("12345");
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("1234567哈哈");
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码不可输入中文字符"));
	}

	@Test(dependsOnMethods = "testCase11CheckCompanyBossPassword2")
	public void testCase12CheckCompanyExpireDatetime1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("12345678999");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase12CheckCompanyExpireDatetime1")
	public void testCase13CheckCompanyBossWechat1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("12345678999");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("000000");
		driver.findElement(By.id("datetime")).click();
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase13CheckCompanyBossWechat1")
	public void testCase14CheckCompanyBossWechat2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("18999999999");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("000000");
		driver.findElement(By.id("datetime")).click();
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).sendKeys("wwwww");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必须以字母开头，5位以上20位以下，由字母、数字、下划线和减号组成，不支持设置中文"));
		driver.findElement(By.cssSelector("tr:nth-child(9)")).click();
		driver.findElement(By.name("bossWechat")).sendKeys("123456");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必须以字母开头，5位以上20位以下，由字母、数字、下划线和减号组成，不支持设置中文"));
	}

	@Test(dependsOnMethods = "testCase14CheckCompanyBossWechat2")
	public void testCase15CheckCompanyDbName1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("12345678999");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("000000");
		driver.findElement(By.id("datetime")).click();
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).sendKeys("wechatacb");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase15CheckCompanyDbName1")
	public void testCase16CheckCompanyDbName2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("12346789999");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("000000");
		driver.findElement(By.id("datetime")).click();
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).sendKeys("wechatabc");
		driver.findElement(By.name("dbName")).click();
		driver.findElement(By.name("dbName")).sendKeys("12345");
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("公司dbName数据格式不对，请输入数字、字母和下划线的组合，但首字符必须是字母，中间不能出现空格"));
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首字符只能为字母，整个字段名称不能出现中文或特殊符号"));
		driver.findElement(By.cssSelector("tr:nth-child(10)")).click();
		driver.findElement(By.name("dbName")).sendKeys("你好啊");
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首字符只能为字母，整个字段名称不能出现中文或特殊符号"));
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首字符只能为字母，整个字段名称不能出现中文或特殊符号"));
	}

	@Test(dependsOnMethods = "testCase16CheckCompanyDbName2")
	public void testCase17CheckCompanyDbUserName1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("12345678999");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("000000");
		driver.findElement(By.id("datetime")).click();
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).sendKeys("wechatabc");
		driver.findElement(By.name("dbName")).click();
		driver.findElement(By.name("dbName")).sendKeys("nbr_nbr");
		driver.findElement(By.name("dbUserName")).click();
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase17CheckCompanyDbUserName1")
	public void testCase18CheckCompanyDbUserName2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("12345678999");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("000000");
		driver.findElement(By.id("datetime")).click();
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).sendKeys("wechatabc");
		driver.findElement(By.name("dbName")).click();
		driver.findElement(By.name("dbName")).sendKeys("nbr_nbr");
		driver.findElement(By.name("dbUserName")).click();
		driver.findElement(By.name("dbUserName")).sendKeys("123");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首字符只能为字母，整个字段名称不能出现中文或特殊符号"));
		driver.findElement(By.cssSelector("tr:nth-child(11)")).click();
		driver.findElement(By.name("dbUserName")).sendKeys("你好啊");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("首字符只能为字母，整个字段名称不能出现中文或特殊符号"));
	}

	@Test(dependsOnMethods = "testCase18CheckCompanyDbUserName2")
	public void testCase19CheckCompanyDbUserPassword1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("12345678999");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("000000");
		driver.findElement(By.id("datetime")).click();
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).sendKeys("wechatabc");
		driver.findElement(By.name("dbName")).click();
		driver.findElement(By.name("dbName")).sendKeys("nbr_nbr");
		driver.findElement(By.name("dbUserName")).click();
		driver.findElement(By.name("dbUserName")).sendKeys("nbr");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase19CheckCompanyDbUserPassword1")
	public void testCase20CheckCompanyDbUserPassword2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("正确的公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("789999999999999999");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("正确的联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("12345678999");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("000000");
		driver.findElement(By.id("datetime")).click();
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).sendKeys("wechatabc");
		driver.findElement(By.name("dbName")).click();
		driver.findElement(By.name("dbName")).sendKeys("nbr_nbr");
		driver.findElement(By.name("dbUserName")).click();
		driver.findElement(By.name("dbUserName")).sendKeys("nbr");
		driver.findElement(By.name("dbUserPassword")).click();
		driver.findElement(By.name("dbUserPassword")).sendKeys("12345");
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("dbUserPassword")).click();
		driver.findElement(By.name("dbUserPassword")).sendKeys("1234567哈哈");
		driver.findElement(By.cssSelector(".createCompany")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码不可输入中文字符"));
	}

	@Test(dependsOnMethods = "testCase20CheckCompanyDbUserPassword2")
	public void testCase21CheckCompanyBrandName1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).sendKeys("公司名称");
		driver.findElement(By.name("businessLicenseSN")).click();
		driver.findElement(By.name("businessLicenseSN")).sendKeys("123456789999988889");
		driver.findElement(By.name("bossName")).click();
		driver.findElement(By.name("bossName")).sendKeys("联系人");
		driver.findElement(By.name("bossPhone")).click();
		driver.findElement(By.name("bossPhone")).sendKeys("13245678988");
		driver.findElement(By.id("datetime")).click();
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		driver.findElement(By.name("bossWechat")).click();
		driver.findElement(By.name("bossWechat")).sendKeys("wechathahah");
		driver.findElement(By.name("dbName")).click();
		driver.findElement(By.name("dbName")).sendKeys("nbr_gsmc");
		driver.findElement(By.name("dbUserName")).click();
		driver.findElement(By.name("dbUserName")).sendKeys("gsmc");
		driver.findElement(By.name("dbUserPassword")).click();
		driver.findElement(By.name("dbUserPassword")).sendKeys("000000");
		driver.findElement(By.name("bossPassword")).click();
		driver.findElement(By.name("bossPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".createCompany")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test(dependsOnMethods = "testCase21CheckCompanyBrandName1")
	public void testCase22CheckCompanyBrandName2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		vars.put("maxlength", js.executeScript("var maxlength = document.getElementsByClassName(\"brandName\")[0].getElementsByTagName(\"input\")[0].getAttribute(\"maxlength\"); return maxlength;"));
		if ((Boolean) js.executeScript("return (arguments[0] == 20)", vars.get("maxlength"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test(dependsOnMethods = "testCase22CheckCompanyBrandName2")
	public void testCase23AddPos1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreatePos")).click();
		vars.put("posSN", js.executeScript("var posSN = Math.floor(Math.random() * 10000000000); return posSN;"));
		driver.findElement(By.name("pos_SN")).click();
		driver.findElement(By.name("pos_SN")).sendKeys(vars.get("posSN").toString());
		driver.findElement(By.name("passwordInPOS")).click();
		driver.findElement(By.name("passwordInPOS")).sendKeys("000000");
		driver.findElement(By.cssSelector(".posBtnSubmit > .layui-btn:nth-child(1)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("门店名称：博昕杂粮\nPOS机SN码\nPOS机密码\n确定添加 取消"));
	}

	@Test(dependsOnMethods = "testCase23AddPos1")
	public void testCase24AddPos2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".toCreatePos")).click();
		driver.findElement(By.name("passwordInPOS")).click();
		driver.findElement(By.name("passwordInPOS")).sendKeys("000000");
		driver.findElement(By.cssSelector(".posBtnSubmit > .layui-btn:nth-child(1)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("必填项不能为空"));

		Thread.sleep(5000);
		driver.findElement(By.name("pos_SN")).click();
		driver.findElement(By.name("pos_SN")).sendKeys("12345 67890");
		driver.findElement(By.cssSelector(".posBtnSubmit > .layui-btn:nth-child(1)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("只允许数字和英文"));

		Thread.sleep(5000);
		driver.findElement(By.cssSelector("#posPopup .layui-form-item:nth-child(3)")).click();
		driver.findElement(By.name("pos_SN")).sendKeys("#%$%");
		driver.findElement(By.cssSelector(".posBtnSubmit > .layui-btn:nth-child(1)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-padding")).getText(), is("只允许数字和英文"));
	}

	@Test(dependsOnMethods = "testCase24AddPos2")
	public void testCase25AddPos3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		vars.put("posSN", js.executeScript("var posSN = Math.floor(Math.random() * 10000000000); return posSN;"));
		driver.findElement(By.cssSelector(".toCreatePos")).click();
		driver.findElement(By.name("pos_SN")).click();
		driver.findElement(By.name("pos_SN")).sendKeys(vars.get("posSN").toString());
		driver.findElement(By.name("passwordInPOS")).click();
		driver.findElement(By.name("passwordInPOS")).sendKeys("000000");
		driver.findElement(By.cssSelector(".posBtnSubmit > .layui-btn:nth-child(1)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("添加pos机成功"));
		driver.findElement(By.cssSelector(".toCreatePos")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("pos_SN")).click();
		driver.findElement(By.name("pos_SN")).sendKeys(vars.get("posSN").toString());
		driver.findElement(By.name("passwordInPOS")).click();
		driver.findElement(By.name("passwordInPOS")).sendKeys("000000");
		driver.findElement(By.cssSelector(".posBtnSubmit > .layui-btn:nth-child(1)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content:nth-child(1)")).getText(), is("该POS已被公司 使用,一台pos机不能被多个公司使用"));
	}

	@Test(dependsOnMethods = "testCase25AddPos3")
	public void testCase26AaddPos4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		vars.put("posSN", driver.findElement(By.cssSelector("tr:nth-child(1) > td:nth-child(3)")).getText());
		Thread.sleep(2000);
		js.executeScript("document.getElementsByClassName(\"deletePos\")[0].style.display='block';");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("tr:nth-child(1) .layui-icon")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要删除POS机" + vars.get("posSN").toString() + "吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("pos机删除成功"));
		driver.findElement(By.cssSelector(".toCreatePos")).click();
		driver.findElement(By.name("pos_SN")).click();
		driver.findElement(By.name("pos_SN")).sendKeys(vars.get("posSN").toString());
		driver.findElement(By.name("passwordInPOS")).click();
		driver.findElement(By.name("passwordInPOS")).sendKeys("000000");
		driver.findElement(By.cssSelector(".posBtnSubmit > .layui-btn:nth-child(1)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("添加pos机成功"));
	}

	@Test(dependsOnMethods = "testCase26AaddPos4")
	public void testCase27AddPos5() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		vars.put("posSN", js.executeScript("var posSN = Math.floor(Math.random() * 10000000000); return posSN;"));
		driver.findElement(By.cssSelector(".toCreatePos")).click();
		driver.findElement(By.name("pos_SN")).click();
		driver.findElement(By.name("pos_SN")).sendKeys(vars.get("posSN").toString());
		driver.findElement(By.name("passwordInPOS")).click();
		driver.findElement(By.name("passwordInPOS")).sendKeys("000000");
		driver.findElement(By.cssSelector(".posBtnSubmit > .layui-btn:nth-child(1)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("添加pos机成功"));
		driver.findElement(By.cssSelector("li:nth-child(2) > .storeArea")).click();
		driver.findElement(By.cssSelector(".toCreatePos")).click();
		driver.findElement(By.name("pos_SN")).click();
		driver.findElement(By.name("pos_SN")).sendKeys(vars.get("posSN").toString());
		driver.findElement(By.name("passwordInPOS")).click();
		driver.findElement(By.name("passwordInPOS")).sendKeys("111111");
		driver.findElement(By.cssSelector(".posBtnSubmit > .layui-btn:nth-child(1)")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content:nth-child(1)")).getText(), is("该POS已被公司 使用,一台pos机不能被多个公司使用"));
	}

	@Test(dependsOnMethods = "testCase27AddPos5")
	public void testCas28EaseOfUseTips() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		driver.findElement(By.cssSelector("li:nth-child(1) > .storeArea")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		driver.findElement(By.cssSelector(".layui-btn-sm")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		driver.findElement(By.cssSelector(".cancel")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
	}
}
