package com.bx.erp.selenium.staff;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Staff_UTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTU1updateInfo1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(1) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(1) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
	}

	@Test
	public void uTU2updateInfo2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("phone1", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		vars.put("phone2", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
	}

	@Test
	public void uTU3updateInfo3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("weChat")).sendKeys("a123456");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("weChat")).sendKeys("a123456");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
	}

	@Test
	public void uTU4updateInfo4() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone1", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(1) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("dd:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone1", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(1) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("dd:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
	}

	@Test
	public void uTU5updateInfo5() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone1", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(1) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("dd:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone1", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(1) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(5)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("dd:nth-child(6)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
	}

	@Test
	public void uTU6updateInfo6() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone1", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(1) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(4)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone1", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(1) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(4)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) > .layui-inline:nth-child(2) .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(3)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
	}

	@Test
	public void uTU7updateInfo7() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("当前员工信息尚未修改，请修改再保存！"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("当前员工信息尚未修改，请修改再保存！"));
		driver.close();
	}

	@Test
	public void uTU8updateInfo8() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("ICID")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("ICID")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
	}

	@Test
	public void uTU9updateInfo9() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("weChat")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("weChat")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
	}

	@Test
	public void uTU10resetPassword1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("resetNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("resetNewPassword")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmResetNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的密码不一致！"));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("resetNewPassword")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的密码不一致！ "));
	}

	@Test
	public void uTU11resetPassword2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("resetNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("resetNewPassword")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmResetNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("重置密码成功"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("resetNewPassword")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("重置密码成功"));
	}

	@Test
	public void uTU12resetPassword3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		vars.put("password1", js.executeScript("return \"               \";"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(" ");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(" ");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		vars.put("password2", js.executeScript("return \"      psw     \";"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys("密password码");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("密password码");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码不能有中文，首尾不能有空格并且格式必须是6到16位"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys("密码密码密码密码密码");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("密码密码密码密码密码");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码不能有中文，首尾不能有空格并且格式必须是6到16位"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(" ");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(" ");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys("密password码");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("密password码");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码不能有中文，首尾不能有空格并且格式必须是6到16位"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys("密码密码密码密码密码");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("密码密码密码密码密码");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码不能有中文，首尾不能有空格并且格式必须是6到16位"));
	}

	@Test
	public void uTU13updateLoginStaffStatus() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(5) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-form-selected .layui-this")).getText(), is("在职"));
		assertThat(driver.findElement(By.cssSelector(".layui-form-selected .layui-disabled:nth-child(2)")).getText(), is("离职"));
	}

	@Test
	public void uTU14updateLoginStaffRole() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("收银员")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
		driver.findElement(By.linkText("店长")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
	}

	@Test
	public void uTU15checkResetPassword1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		vars.put("password1", js.executeScript("return \"               \";"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(" ");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(" ");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		vars.put("password2", js.executeScript("return \"      psw     \";"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys("12345");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("12345");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys("pass  word");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("pass  word");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("重置密码成功"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(" ");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(" ");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys("12345");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("12345");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("resetNewPassword")).clear();
		driver.findElement(By.name("resetNewPassword")).sendKeys("pass  word");
		driver.findElement(By.name("confirmResetNewPassword")).clear();
		driver.findElement(By.name("confirmResetNewPassword")).sendKeys("pass  word");
		driver.findElement(By.cssSelector(".saveResetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("重置密码成功"));
	}

	@Test
	public void uTU16checkResetPassword2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).sendKeys("000000");
		vars.put("password1", js.executeScript("return \"               \";"));
		driver.findElement(By.name("newPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.name("confirmNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("newPassword")).sendKeys(" ");
		driver.findElement(By.name("confirmNewPassword")).sendKeys(" ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		vars.put("password2", js.executeScript("return \"      psw     \";"));
		driver.findElement(By.name("newPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.name("confirmNewPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("newPassword")).sendKeys("12345");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("12345");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
		driver.findElement(By.name("newPassword")).sendKeys("pass  word");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("pass  word");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码成功"));
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).sendKeys("pass  word");
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码成功"));
	}

	@Test
	public void uTU17updateLoginStaffPassword1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("123456");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTU18updateLoginStaffPassword2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).sendKeys(" ");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("123456");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
	}

	@Test
	public void uTU19updateLoginStaffPassword3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).sendKeys("123456");
		driver.findElement(By.name("confirmNewPassword")).sendKeys(" ");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.close();
	}

	@Test
	public void uTU20updateLoginStaffPassword4() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).sendKeys("111111");
		driver.findElement(By.name("newPassword")).sendKeys("123456");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("123456");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码失败，请重试"));
		driver.close();
	}

	@Test
	public void uTU21updateLoginStaffPassword5() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).sendKeys("654321");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("123456");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("两次输入的新密码不一致"));
		driver.close();
	}

	@Test
	public void uTU22updateLoginStaffPassword6() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).sendKeys("123456");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("123456");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码成功"));
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码成功"));
	}

	@Test
	public void uTU23presalesUpdateStaffInfo() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("服务器错误，请重试"));
		driver.close();
	}

	@Test
	public void uTU24presalesUpdatePassword() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("服务器错误，请重试"));
		driver.close();
	}

	@Test
	public void uTU25updateLoginStaffPassword7() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("html")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).sendKeys("000000");
		driver.findElement(By.name("newPassword")).sendKeys("密码密码密码密码");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("密码密码密码密码");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码失败，请重试"));
		vars.put("password1", js.executeScript("return \"          \";"));
		driver.findElement(By.name("newPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.name("confirmNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		driver.findElement(By.name("confirmNewPassword")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("newPassword")).sendKeys("密password码");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("密password码");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改密码失败，请重试"));
		vars.put("password2", js.executeScript("return \" password \";"));
		driver.findElement(By.name("newPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.name("confirmNewPassword")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
	}

	@Test
	public void uTU26updateLoginStaffPassword8() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要修改自己的密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		driver.findElement(By.name("password")).sendKeys("密码密码密码密码");
		driver.findElement(By.name("newPassword")).sendKeys("123456");
		driver.findElement(By.name("confirmNewPassword")).sendKeys("123456");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码不可输入中文字符"));
		vars.put("password1", js.executeScript("return \"          \";"));
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(vars.get("password1").toString());
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("必填项不能为空"));
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("密password码");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("密码不可输入中文字符"));
		vars.put("password2", js.executeScript("return \" password \";"));
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(vars.get("password2").toString());
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请输入6-16位的字符,首尾不能有空格"));
	}

	@Test
	public void uTU27userGuidance1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("getPhone1", driver.findElement(By.name("phone")).getAttribute("value"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("getPhone1").toString()));
		}
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.linkText("店长")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("phone").toString()));
		}
		driver.findElement(By.linkText("收银员")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("phone").toString()));
		}
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone2", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("phone"), vars.get("getPhone2"))) {
		}
		driver.findElement(By.linkText("全部在职员工")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("phone").toString()));
		}
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("phone").toString()));
		}
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone3", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("phone"), vars.get("getPhone3"))) {
		}
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("phone").toString()));
		}
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("phone").toString()));
		}
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("13888888");
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone4", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("phone"), vars.get("getPhone4"))) {
		}
		driver.findElement(By.id("staffMain")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		vars.put("getPhone5", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("phone"), vars.get("getPhone5"))) {
		}
		driver.findElement(By.linkText("1")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone6", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("phone"), vars.get("getPhone6"))) {
		}
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/span")).click();
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("phone").toString()));
		}
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone7", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("phone"), vars.get("getPhone7"))) {
		}
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector(".staffCreate")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("phone").toString()));
		}
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("phone").toString()));
		}
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone8", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] != \'\')", vars.get("getPhone8"))) {
		}
	}

	@Test
	public void uTU28userGuidance2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("店长")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("收银员")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("全部在职员工")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[4]/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-btn:nth-child(4)")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("当前员工信息尚未修改，请修改再保存！"));
		driver.findElement(By.cssSelector(".resetPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃修改密码吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
	}
}
