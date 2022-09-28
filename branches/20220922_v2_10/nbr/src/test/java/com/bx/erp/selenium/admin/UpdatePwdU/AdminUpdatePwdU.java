package com.bx.erp.selenium.admin.UpdatePwdU;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class AdminUpdatePwdU extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		bNeedAssistantManagerLogin = false;
		setOperatorType(EnumOperatorType.EOT_PhoneOfAssistManager.getIndex());
		super.setUp();
	}

	@Test
	public void uTU1firstLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).sendKeys("13144496272");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("   00000");
		driver.findElement(By.name("newPassword")).sendKeys("111111");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("111111");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("00000   ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".layui-card-body")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("00000@");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码失败，请重试"));
		Thread.sleep(3000);
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("00000密码");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码不可输入中文字符"));
		Thread.sleep(4000);
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("0 0000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码失败，请重试"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("0000000000000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码失败，请重试"));
		Thread.sleep(2000);
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("           ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTU2firstLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1633, 778));
		driver.findElement(By.name("companySN")).sendKeys("668866");
		// driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("phone")).sendKeys("13144496272");
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("111111");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("  1111");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("11110 ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("11110@");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("11110");
		// Thread.sleep(2000);
		// 点击空白的地方
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("00000密码");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn")).click();
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("000   00");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("0000000000000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTU3firstLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1633, 778));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("13144496272");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.name("pwdEncrypted")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("111111");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("  11111");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("1111   ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("111111$#");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		driver.findElement(By.name("newPassword")).click();
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("00000密码");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("111   111");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("0000000000000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		// driver.findElement(By.cssSelector("html")).click();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTU10staffLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1056));
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
		// Thread.sleep(1000);
		driver.findElement(By.linkText("员工管理")).click();
		driver.switchTo().frame(1);
		Thread.sleep(2000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("queryKeyword")).sendKeys("18915460959");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("td span")).click();
		driver.findElement(By.cssSelector(".resetPassword")).click();
		driver.findElement(By.name("resetNewPassword")).click();
		driver.findElement(By.name("resetNewPassword")).sendKeys("111111");
		driver.findElement(By.name("confirmResetNewPassword")).click();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("111111");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("111111");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("222222");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("333333");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("18915460959");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("111111");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("111111");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(2000);
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码成功"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("111111");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("222222");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("333333");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
	}

	@Test
	public void uTU11staffLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("111111");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("222222");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("222222");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码失败，请重试"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("18915460959");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("111111");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("222222");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("222222");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码失败，请重试"));
	}

	@Test
	public void uTU12staffLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("111111");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("222222");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("18915460959");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("111111");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("222222");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
	}

	@Test
	public void uTU13staffLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("111111");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("111111");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("111111");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("111111");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("18915460959");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("111111");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("111111");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("18915460959");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("111111");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("111111");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码成功"));
	}

	@Test
	public void uTU14staffLoginToUpdatePwd() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		{
			WebElement element = driver.findElement(By.cssSelector(".layui-btn"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			Thread.sleep(1000);
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新密码不能与原密码相同"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("18915460959");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("修改密码")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.name("password")).click();
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).click();
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).click();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新密码不能与原密码相同"));
	}
}