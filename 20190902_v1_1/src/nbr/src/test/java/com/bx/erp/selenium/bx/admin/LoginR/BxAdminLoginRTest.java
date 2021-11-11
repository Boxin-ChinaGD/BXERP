package com.bx.erp.selenium.bx.admin.LoginR;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class BxAdminLoginRTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
	}

	@Test
	public void uTR1checkMobile() throws InterruptedException {
		driver.get(BxStaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys("000000");
		driver.findElement(By.name("mobile")).sendKeys("1318524628@");
		driver.findElement(By.name("mobile")).sendKeys(Keys.ENTER);
		driver.findElement(By.cssSelector(".layui-btn")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		// Thread.sleep(2000);
		// nth-child(n)选取属于其父元素的第n个子元素的每个 layui-form-item 元素
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) > .layui-input-block")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("1318524手机号");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) > .layui-input-block")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("53185246281");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4)")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("1576640324");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) > .layui-input-block")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("1 318524628");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) > .layui-input-block")).click();
		driver.findElement(By.name("mobile")).clear();
		driver.findElement(By.name("mobile")).sendKeys("13185246281");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-templeate-1")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
	}

	@Test
	public void uTR2checkPassword() throws InterruptedException {
		driver.get(BxStaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("mobile")).sendKeys("13185246281");
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys(" 000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码错误，请重新输入"));
		driver.findElement(By.name("salt")).clear();
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys("000000 ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码错误，请重新输入"));
		driver.findElement(By.name("salt")).clear();
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys("00000!");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
		driver.findElement(By.name("salt")).clear();
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys("00000密码");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
		driver.findElement(By.name("salt")).clear();
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys("0 0000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
		driver.findElement(By.name("salt")).clear();
		driver.findElement(By.name("salt")).click();
		// 前端限制了输入长度
		driver.findElement(By.name("salt")).sendKeys("000000000000000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
	}

	@Test
	public void uTR3login1() {
		driver.get(BxStaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("mobile")).sendKeys("");
		driver.findElement(By.name("salt")).sendKeys("");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项，不能为空"));
	}

	@Test
	public void uTR4login2() {
		driver.get(BxStaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("mobile")).sendKeys("");
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys("0000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项，不能为空"));
	}

	@Test
	public void uTR5login3() {
		driver.get(BxStaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("salt")).sendKeys("");
		driver.findElement(By.name("mobile")).sendKeys("13185246281");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项，不能为空"));
	}

	@Test
	public void uTR6login4() throws InterruptedException {
		driver.get(BxStaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).sendKeys("13185246280");
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys("00000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
	}

	@Test
	public void uTR7login5() throws InterruptedException {
		driver.get(BxStaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys("00000000");
		driver.findElement(By.name("mobile")).sendKeys("13185246281");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
	}

	@Test
	public void uTR8login6() throws InterruptedException {
		driver.get(BxStaffLoginUrl);
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).sendKeys("13185246280");
		driver.findElement(By.name("salt")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("手机号码或密码错误"));
	}

	@Test
	public void uTR9login7() throws InterruptedException {
		driver.get(BxStaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).click();
		driver.findElement(By.name("mobile")).sendKeys("13185246281");
		driver.findElement(By.name("salt")).click();
		driver.findElement(By.name("salt")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-templeate-1")).click();
		driver.findElement(By.linkText("门店管理")).click();
		driver.findElement(By.linkText("门店列表")).click();
	}
}
