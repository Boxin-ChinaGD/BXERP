package com.bx.erp.selenium.staff;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Staff_CTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTC1checkName() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("@#￥");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("#staffDetail > .staffInfo")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("丁某某1号");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));
		Thread.sleep(1000);
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("111111");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("111111");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文和英文"));
	}

	@Test
	public void uTC2checkPhone() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("员工");
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).sendKeys("18915460hk");
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys("edgf45789652");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("员工手机号码数据格式不对，请输入1开头的11位数字"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys("gfhjjhhjvtd");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("员工手机号码数据格式不对，请输入1开头的11位数字"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys("1322625000$");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("员工手机号码数据格式不对，请输入1开头的11位数字"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的手机号码"));
		Thread.sleep(1000);
		vars.put("phone", js.executeScript("return \"1\"+Math.round(Math.random()*7777777777)"));
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).clear();
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.id("staffMain")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
	}

	@Test
	public void uTC3checkICID() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("员工");
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).sendKeys("13777777777");
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("ICID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("ICID")).sendKeys("44088319841211#$67");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("员工身份证号数据格式不对，请输入正确的身份证号"));
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的身份证号码"));
		driver.findElement(By.name("ICID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("ICID")).clear();
		driver.findElement(By.name("ICID")).sendKeys("4408831984121167身份");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("员工身份证号数据格式不对，请输入正确的身份证号"));
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的身份证号码"));
		driver.findElement(By.name("ICID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("ICID")).clear();
		driver.findElement(By.name("ICID")).sendKeys("身份4408831984121167");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("员工身份证号数据格式不对，请输入正确的身份证号"));
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的身份证号码"));
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入正确的身份证号码"));
		vars.put("ICID", js.executeScript("return \"\"+Math.round(Math.random()*777777)+\"19990921\"+Math.round(Math.random()*7777)"));
		driver.findElement(By.name("ICID")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("ICID")).clear();
		driver.findElement(By.name("ICID")).sendKeys(vars.get("ICID").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
	}

	@Test
	public void uTC4checkWechat() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("员工");
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).sendKeys("13777777777");
		Thread.sleep(1000);
		driver.findElement(By.name("weChat")).sendKeys("c5@sdgsd#");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("员工微信号数据格式不对，请输入5-20位的英文或数字"));
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("weChat")).clear();
		driver.findElement(By.name("weChat")).sendKeys("c54rtf微信");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("员工微信号数据格式不对，请输入5-20位的英文或数字"));
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		vars.put("weChat", js.executeScript("var weChat= \"i\"+Math.round(Math.random()*777);  document.getElementsByClassName(\"staffWeChat\")[0] .value = weChat; return weChat;"));
		driver.findElement(By.name("weChat")).clear();
		driver.findElement(By.name("weChat")).sendKeys(vars.get("weChat").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("员工微信号数据格式不对，请输入5-20位的英文或数字"));
		driver.findElement(By.name("weChat")).clear();
		driver.findElement(By.name("weChat")).sendKeys("abcde123456789123456");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
	}

	@Test
	public void uTC5password1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("员工");
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).sendKeys("13777777777");
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("123");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("newPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("123123");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("confirmNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("147258");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的密码不一致！"));
		driver.findElement(By.name("confirmNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("25874     ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-content")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("newPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("12345@");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("12345@");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
	}

	@Test
	public void uTC6password2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr=[\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue=\'员工\'; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } return idvalue;"));
		driver.findElement(By.name("name")).sendKeys(vars.get("staffName").toString());
		Thread.sleep(1000);
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
	}

	@Test
	public void uTC7password3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		vars.put("staffName", js.executeScript(
				"var arr=[\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue=\'员工\'; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } return idvalue;"));
		driver.findElement(By.name("name")).sendKeys(vars.get("staffName").toString());
		Thread.sleep(1000);
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("111111");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.cssSelector(".staffAppendPassword > .layui-inline:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("111111");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC8createStaff1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC9createStaff2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC10createStaff3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC11createStaff4() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.name("newPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC12createStaff5() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC13createStaff6() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTC14createStaff7() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
	}

	@Test
	public void uTC15createStaff8() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
	}

	@Test
	public void uTC16createStaff9() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
	}

	@Test
	public void uTC18checkRequired() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
	}

	@Test
	public void uTC19presalesToCreateStaff() throws InterruptedException {
		driver.findElement(By.cssSelector("li:nth-child(5)")).click();
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("服务器错误，请重试"));
	}

	@Test
	public void uTC20cashierLogin() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div")).click();
		vars.put("staffName", driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div")).getText());
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			Thread.sleep(1000);
			assertThat(value, is(vars.get("staffName").toString()));
		}
	}

	@Test
	public void uTC21createButton() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("createButtonStatus", js.executeScript("var createButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByClassName(\"staffCreate\")[0].getAttribute(\"disabled\"); return createButtonStatus;"));
		vars.put("saveButtonStatus", js.executeScript("var saveButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByClassName(\"staffUpdate\")[0].getAttribute(\"disabled\"); return saveButtonStatus;"));
		vars.put("deleteButtonStatus", js.executeScript("var deleteButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByClassName(\"deleteStaff\")[0].getAttribute(\"disabled\"); return deleteButtonStatus;"));
		vars.put("cancelButtonStatus", js.executeScript("var cancelButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByTagName(\"button\")[3].getAttribute(\"disabled\"); return cancelButtonStatus;"));
		if ((Boolean) js.executeScript("return (arguments[0] != \'disabled\' && arguments[1] != \'disabled\' && arguments[2] == \'disabled\' && arguments[3] != \'disabled\')", vars.get("createButtonStatus"), vars.get("saveButtonStatus"),
				vars.get("deleteButtonStatus"), vars.get("cancelButtonStatus"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTC22checkPassword() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("新密码全是中文");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("新密码全是中文");
		driver.findElement(By.name("name")).click();
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		{
			WebElement element = driver.findElement(By.cssSelector(".staffUpdate"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码不能有中文，首尾不能有空格并且格式必须是6到16位"));
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("       ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffAppendPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("       ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		{
			WebElement element = driver.findElement(By.name("newPassword"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.name("newPassword"));
			Actions builder = new Actions(driver);
			((Actions) builder).moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.name("newPassword"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.name("newPassword")).clear();
		driver.findElement(By.name("newPassword")).sendKeys("    新密码全是中文");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).click();
		{
			WebElement element = driver.findElement(By.name("confirmNewPassword"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.name("confirmNewPassword"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.name("confirmNewPassword"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.name("confirmNewPassword")).clear();
		driver.findElement(By.name("confirmNewPassword")).sendKeys("    新密码全是中文");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
	}

	@Test
	public void uTC23userGuidance() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue; return idvalue;"));
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		vars.put("getStaffName", driver.findElement(By.name("name")).getAttribute("value"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.linkText("店长")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.linkText("收银员")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone1", driver.findElement(By.name("phone")).getAttribute("value"));
		Thread.sleep(1000);
		if ((Boolean) js.executeScript("return (arguments[0] == \'\')", vars.get("getPhone1"))) {
		}
		driver.findElement(By.linkText("全部在职员工")).click();
		Thread.sleep(1000);
		vars.put("getStaffName1", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone2", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'\')", vars.get("getPhone2"))) {
		}
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("138888888");
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone3", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'\')", vars.get("getPhone3"))) {
		}
		driver.findElement(By.id("staffMain")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.linkText("1")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone4", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'\')", vars.get("getPhone4"))) {
		}
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.name("name")).getText(), is(" "));
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone5", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'\')", vars.get("getPhone5"))) {
		}
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
	}
}
