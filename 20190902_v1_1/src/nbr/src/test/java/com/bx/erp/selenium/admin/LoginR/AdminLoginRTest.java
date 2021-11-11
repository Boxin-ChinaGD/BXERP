package com.bx.erp.selenium.admin.LoginR;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class AdminLoginRTest extends BaseSeleniumTest {
	
	@BeforeClass
	public void setUp() {
		super.setUp();
	}

	@Test
	public void uTloginR1checkCompanyNum() throws InterruptedException {
		System.out.println("`set speed` is a no-op in code export, use `pause` instead");
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		Thread.sleep(3000);
		driver.findElement(By.name("companySN")).sendKeys("sd  gjjjjj");
		driver.findElement(By.name("companySN")).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("sdgjj     ");
		driver.findElement(By.name("companySN")).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("你好啊");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("45456");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("46588 ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		Thread.sleep(1000);
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("465883");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("登录失败，公司编号错误"));
		// driver.close();
	}

	@Test
	public void uTloginR2checkPhone() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys("jhguhhgghhg");
		driver.findElement(By.name("phone")).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.cssSelector(".layui-btn")).click();
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys("13263456");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys("13263456hff");
		driver.findElement(By.name("phone")).sendKeys(Keys.ENTER);
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys("1585432089 ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys("15854320 89");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
	}

	@Test
	public void uTloginR3checkPassword() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).clear();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("00000    ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码错误，请重新输入"));
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).clear();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("fhdddddddddddddd");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).clear();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("0 00000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
		// driver.close();
	}

	@Test
	public void uTloginR4login1() {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1917, 1040));
		driver.findElement(By.name("companySN")).sendKeys("");
		driver.findElement(By.name("phone")).sendKeys("  ");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("  ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTloginR5login2() {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1917, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("");
		driver.findElement(By.name("pwdEncrypted")).sendKeys(" ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTloginR6login3() {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTloginR7login4() {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTloginR8login5() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工资料")).click();
	}

	@Test
	public void uTloginR9login6() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("00006587");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
	}

	@Test
	public void uTloginR10login7() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("18320427095");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("00456846456");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
		// driver.close();
	}

	@Test
	public void uTloginR11login8() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("18320437925");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
		// driver.close();
	}

	@Test
	public void uTloginR12login9() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("companySN")).click();
		// 前端限制了长度
		driver.findElement(By.name("companySN")).sendKeys("6688aa");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320hgr");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000fgfgfggf");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("668876");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys("15854326589");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("登录失败，公司编号错误"));
		// driver.close();
	}

	@Test
	public void uTloginR13login10() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.name("companySN")).click();
		// 前端限制了输入长度
		driver.findElement(By.name("companySN")).sendKeys("6688aa");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("6688655");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("登录失败，公司编号错误"));
		// driver.close();
	}

	@Test
	public void uTloginR14login11() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("00000000");
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("6688yituyuy");
		driver.findElement(By.name("companySN")).sendKeys(Keys.ENTER);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000jhjhjh");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("companySN")).clear();
		// 前端限制了长度
		driver.findElement(By.name("companySN")).sendKeys("66887");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668874");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("登录失败，公司编号错误"));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
		// driver.close();
	}

	@Test
	public void uTloginR15login12() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1040));
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.name("companySN")).click();
		// 前端限制了输入长度
		driver.findElement(By.name("companySN")).sendKeys("66866aa");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("66866");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的公司编号，只能输入6到8位的数字"));
		driver.findElement(By.name("companySN")).clear();
		driver.findElement(By.name("companySN")).sendKeys("668666");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320585");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("登录失败，公司编号错误"));
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("登录失败，公司编号错误"));
		// driver.close();
	}

	@Test
	public void uTloginR16PreSale() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1020));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("13888888888");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(0);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("权限不足"));
	}
}
