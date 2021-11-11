package com.bx.erp.selenium.admin.UpdatePwdU;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class AdminUpdatePwdUNoLogout extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		bNeedAssistantManagerLogin = false;
		setOperatorType(EnumOperatorType.EOT_PhoneOfAssistManager.getIndex());
		super.setUp();
	}
	
	@AfterMethod
	public void afterMethod() {
	}

	@Test
	public void uTU4firstLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("13144496272");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("0000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("0000000");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("00000000");
		driver.findElement(By.cssSelector("button.layui-btn.layui-btn-normal")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
	}

	@Test
	public void uTU5firstLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("13144496272");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("12345678");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("0000000");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("0000000");
		driver.findElement(By.cssSelector("button.layui-btn.layui-btn-normal")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码失败，请重试"));
	}

	@Test
	public void uTU6firstLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("13144496272");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("00000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("0000000");
		driver.findElement(By.cssSelector("button.layui-btn.layui-btn-normal")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
	}

	@Test
	public void uTU7firstLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("13144496272");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector("button.layui-btn.layui-btn-normal")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新密码不能与原密码相同"));
	}

	@Test
	public void uTU8firstLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("13144496272");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("123456");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("123456");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码成功"));
		driver.findElement(By.linkText("确定")).click();
	}

	@Test
	public void uTU9firstLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("13144496272");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("手机号码或密码错误"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4)")).click();
		driver.findElement(By.name("pwdEncrypted")).clear();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("123456");
		driver.findElement(By.name("pwdEncrypted")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		// 已展开
		// driver.findElement(By.linkText("员工资料")).click();
		driver.findElement(By.linkText("员工管理")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("13144496272");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("td span")).click();
		driver.findElement(By.cssSelector(".resetPassword")).click();
		driver.findElement(By.name("resetNewPassword")).click();
		driver.findElement(By.name("resetNewPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmResetNewPassword")).click();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("重置密码成功"));
	}

}
