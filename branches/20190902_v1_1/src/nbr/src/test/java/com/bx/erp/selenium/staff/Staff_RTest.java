package com.bx.erp.selenium.staff;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Staff_RTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTR1queryByKeyword1() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).sendKeys("158543");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		vars.put("getPhone", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0].indexOf(\'158543\') == -1)", vars.get("getPhone"))) {
		}
		vars.put("getSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);console.log(numText); var num = parseInt(numText) + 1; return num;", vars.get("getSumText1")));
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
		driver.findElement(By.name("queryKeyword")).sendKeys("158543");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		vars.put("getSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum2"))) {
		}
	}

	@Test
	public void uTR2queryByKeyword2() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).sendKeys("32332");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("查无该员工"));
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("@123");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文、数字和英文"));
		vars.put("value1", js.executeScript("return \"      \";"));
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("value1").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文、数字和英文"));
		vars.put("value2", js.executeScript("return \"158 5432\";"));
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("value2").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文、数字和英文"));
		vars.put("value3", js.executeScript("return \"1585432 \";"));
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("value3").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("只允许中文、数字和英文"));
	}

	@Test
	public void uTR3queryByKeyword3() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);console.log(numText); var num = parseInt(numText); return num;", vars.get("getSumText1")));
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		vars.put("getSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum2"))) {
		}
	}

	@Test
	public void uTR4queryByRole() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);console.log(numText); var num = parseInt(numText); return num;", vars.get("getSumText1")));
		driver.findElement(By.linkText("全部在职员工")).click();
		Thread.sleep(1000);
		vars.put("getSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);console.log(numText); var num = parseInt(numText); return num;", vars.get("getSumText1")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum2"))) {
		}
		driver.findElement(By.linkText("店长")).click();
		Thread.sleep(1000);
		vars.put("getRoleText1", driver.findElement(By.cssSelector(".layui-select-title:nth-child(1) input")).getAttribute("value"));
		// TODO
		// System.out.println("RoleText：vars.get("getRoleText1").toString()");
		if ((Boolean) js.executeScript("return (arguments[0] != \'店长\')", vars.get("getRoleText1"))) {
		}
		vars.put("getShopownerSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getShopownerSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText) + 1; return num;", vars.get("getShopownerSumText1")));
		driver.findElement(By.linkText("收银员")).click();
		Thread.sleep(1000);
		vars.put("getRoleText2", driver.findElement(By.cssSelector(".layui-select-title:nth-child(1) input")).getAttribute("value"));
		System.out.println(vars.get("getRoleText2").toString());
		if ((Boolean) js.executeScript("return (arguments[0] != \'收银员\')", vars.get("getRoleText2"))) {
		}
		vars.put("getCashierSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getCashierSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText) + 1; return num;", vars.get("getCashierSumText1")));
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		// vars.put("getStatusText",
		// driver.findElement(By.cssSelector(".layui-select-title:nth-child(2)
		// input")).getAttribute("value"));
		// if ((Boolean) js.executeScript("return (arguments[0] != \'离职\')",
		// vars.get("getStatusText"))) {
		// }
		vars.put("getQuitSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getQuitSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText) + 1; return num;", vars.get("getQuitSumText1")));
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(1)")).click();
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
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
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
		driver.findElement(By.cssSelector(".staffCreate")).click();
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
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
		driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除员工成功"));
		driver.findElement(By.linkText("全部在职员工")).click();
		Thread.sleep(1000);
		vars.put("getSumText3", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum3", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText) - 2; return num;", vars.get("getSumText3")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum3"))) {
		}
		driver.findElement(By.linkText("店长")).click();
		Thread.sleep(1000);
		vars.put("getShopownerSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getShopownerSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getShopownerSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getShopownerSum1"), vars.get("getShopownerSum2"))) {
		}
		driver.findElement(By.linkText("收银员")).click();
		Thread.sleep(1000);
		vars.put("getCashierSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getCashierSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getCashierSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getCashierSum1"), vars.get("getCashierSum2"))) {
		}
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		vars.put("getQuitSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getQuitSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getQuitSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getQuitSum1"), vars.get("getQuitSum2"))) {
		}
	}

	@Test
	public void uTR5queryByStatus() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		vars.put("getQuitSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getQuitSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText) + 1; return num;", vars.get("getQuitSumText1")));
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone \")[0] .value = phone; return phone;"));
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
		driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除员工成功"));
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		vars.put("getQuitSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getQuitSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getQuitSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getQuitSum1"), vars.get("getQuitSum2"))) {
		}
	}

	@Test
	public void uTR6queryByRoleAndKeyword() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(1)")).click();
		Thread.sleep(1000);
		vars.put("phoneShopowner", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("phoneCashier", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
		driver.findElement(By.linkText("全部在职员工")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("phoneShopowner").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		vars.put("getShopownerSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getShopownerSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length); var num = parseInt(numText); return num;", vars.get("getShopownerSumText1")));
		if ((Boolean) js.executeScript("return (arguments[0] != 1)", vars.get("getShopownerSum1"))) {
		}
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("phoneCashier").toString());
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("店长")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("phoneShopowner").toString());
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		vars.put("getShopownerSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getShopownerSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getShopownerSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != 1)", vars.get("getShopownerSum2"))) {
		}
		driver.findElement(By.linkText("收银员")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("phoneCashier").toString());
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		vars.put("getCashierSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getCashierSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getCashierSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != 1)", vars.get("getCashierSum2"))) {
		}
	}

	@Test
	public void uTR7queryAfterPageTurning() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		vars.put("getSumText", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getSumText")));
		if ((Boolean) js.executeScript("return (arguments[0] != 1)", vars.get("getSum"))) {
		}
	}

	@Test
	public void uTR8queryWithRoleAndPageAndKeyword() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("phoneCashier1", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("phoneCashier2", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
		driver.findElement(By.cssSelector(".staffCreate")).click();
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("phoneCashier3", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
		driver.findElement(By.linkText("收银员")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		vars.put("pageSum", driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] != 2)", vars.get("pageSum"))) {
		}
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("phoneCashier1").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		vars.put("getCashierSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getCashierSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length); var num = parseInt(numText); return num;", vars.get("getCashierSumText1")));
		if ((Boolean) js.executeScript("return (arguments[0] != 1)", vars.get("getCashierSum1"))) {
		}
	}

	@Test
	public void uTR9pageTurningAfterQuery() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).sendKeys("员");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		vars.put("pageSum", driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] != 2)", vars.get("pageSum"))) {
		}
	}

	@Test
	public void uTR10tablePagenation() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		vars.put("pageSum1", driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] != 2)", vars.get("pageSum1"))) {
		}
		driver.findElement(By.cssSelector("em:nth-child(2)")).click();
		Thread.sleep(1000);
		vars.put("pageSum2", driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] != 2)", vars.get("pageSum2"))) {
		}
		driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("pageSum3", driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] != 1)", vars.get("pageSum3"))) {
		}
		driver.findElement(By.cssSelector(".layui-laypage-prev > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("pageSum4", driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] != 1)", vars.get("pageSum4"))) {
		}
		driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys(" ");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
		Thread.sleep(1000);
		vars.put("pageSum5", driver.findElement(By.cssSelector(".layui-laypage-curr em:nth-child(2)")).getText());
		if ((Boolean) js.executeScript("return (arguments[0] != \'1\')", vars.get("pageSum5"))) {
		}
		driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("@");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
		Thread.sleep(1000);
		vars.put("pageSum6", driver.findElement(By.cssSelector(".layui-laypage-curr em:nth-child(2)")).getText());
		if ((Boolean) js.executeScript("return (arguments[0] != \'1\')", vars.get("pageSum6"))) {
		}
		driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("page");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
		Thread.sleep(1000);
		vars.put("pageSum7", driver.findElement(By.cssSelector(".layui-laypage-curr em:nth-child(2)")).getText());
		if ((Boolean) js.executeScript("return (arguments[0] != \'1\')", vars.get("pageSum7"))) {
		}
		driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("2");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
		Thread.sleep(1000);
		vars.put("pageSum8", driver.findElement(By.cssSelector(".layui-laypage-curr em:nth-child(2)")).getText());
		if ((Boolean) js.executeScript("return (arguments[0] != \'2\')", vars.get("pageSum8"))) {
		}
		driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-skip > .layui-input")).sendKeys("页码1");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-btn")).click();
		Thread.sleep(1000);
		vars.put("pageSum9", driver.findElement(By.cssSelector(".layui-laypage-curr em:nth-child(2)")).getText());
		if ((Boolean) js.executeScript("return (arguments[0] != \'1\')", vars.get("pageSum9"))) {
		}
	}

	@Test
	public void uTR11presaleToView() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("服务器错误，请重试"));
		driver.close();
	}

	@Test
	public void uTR12viewStaffDetails1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("全部在职员工")).click();
		Thread.sleep(1000);
		vars.put("staffName", driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div")).getText());
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("staffName").toString()));
		}
		{
			String value = driver.findElement(By.cssSelector(".layui-form-item:nth-child(5) .layui-unselect .layui-input")).getAttribute("value");
			assertThat(value, is("在职"));
		}
		vars.put("createButtonStatus", js.executeScript("var createButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByClassName(\"staffCreate\")[0].getAttribute(\"disabled\"); return createButtonStatus;"));
		vars.put("saveButtonStatus", js.executeScript("var saveButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByClassName(\"staffUpdate\")[0].getAttribute(\"disabled\"); return saveButtonStatus;"));
		vars.put("deleteButtonStatus", js.executeScript("var deleteButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByClassName(\"deleteStaff\")[0].getAttribute(\"disabled\"); return deleteButtonStatus;"));
		vars.put("cancelButtonStatus", js.executeScript("var cancelButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByTagName(\"button\")[3].getAttribute(\"disabled\"); return cancelButtonStatus;"));
		if ((Boolean) js.executeScript("return (arguments[0] != \'disabled\' && arguments[1] != \'disabled\' && arguments[2] != \'disabled\' && arguments[3] != \'disabled\')", vars.get("createButtonStatus"), vars.get("saveButtonStatus"),
				vars.get("deleteButtonStatus"), vars.get("cancelButtonStatus"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTR13viewStaffDetails2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		vars.put("staffName", driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div")).getText());
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("staffName").toString()));
		}
		{
			String value = driver.findElement(By.cssSelector(".layui-form-item:nth-child(5) .layui-unselect .layui-input")).getAttribute("value");
			assertThat(value, is("离职"));
		}
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
	public void uTR14queryWhenNoData() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("aaaaaaa");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("查无该员工"));
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
		vars.put("保存", driver.findElement(By.cssSelector(".fixedButtonArea .disabledButton:nth-child(2)")).getText());
		assertThat(driver.findElement(By.cssSelector(".fixedButtonArea .disabledButton:nth-child(3)")).getText(), is("删除"));
		assertThat(driver.findElement(By.cssSelector(".fixedButtonArea .disabledButton:nth-child(4)")).getText(), is("取消"));
	}

	@Test
	public void uTR15viewShopownerDetails1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).sendKeys("新的店长");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form > .layui-form-item:nth-child(4)")).click();
		Thread.sleep(1000);
		vars.put("phone", js.executeScript("var phone = \"135\" + Math.floor(Math.random() * 99999999); return phone;"));
		driver.findElement(By.name("phone")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("phone")).sendKeys(vars.get("phone").toString());
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
		assertThat(driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[4]/div")).getText(), is("vars.get(\"phone\").toString()"));
		{
			String value = driver.findElement(By.name("phone")).getAttribute("value");
			assertThat(value, is(vars.get("phone").toString()));
		}
		vars.put("createButtonStatus", js.executeScript("var createButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByClassName(\"staffCreate\")[0].getAttribute(\"disabled\"); return createButtonStatus;"));
		vars.put("saveButtonStatus", js.executeScript("var saveButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByClassName(\"staffUpdate\")[0].getAttribute(\"disabled\"); return saveButtonStatus;"));
		vars.put("deleteButtonStatus", js.executeScript("var deleteButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByClassName(\"deleteStaff\")[0].getAttribute(\"disabled\"); return deleteButtonStatus;"));
		vars.put("cancelButtonStatus", js.executeScript("var cancelButtonStatus = document.getElementsByClassName(\"fixedButtonArea\")[0].getElementsByTagName(\"button\")[3].getAttribute(\"disabled\"); return cancelButtonStatus;"));
		if ((Boolean) js.executeScript("return (arguments[0] != \'disabled\' && arguments[1] != \'disabled\' && arguments[2] != \'disabled\' && arguments[3] != \'disabled\')", vars.get("createButtonStatus"), vars.get("saveButtonStatus"),
				vars.get("deleteButtonStatus"), vars.get("cancelButtonStatus"))) {
			System.out.println("测试通过");
		} else {
			System.out.println("测试失败");
		}
	}

	@Test
	public void uTR16viewShopownerDetails2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("店长")).click();
		Thread.sleep(1000);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);console.log(numText); var num = parseInt(numText) + 1; return num;", vars.get("getSumText1")));
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(1)")).click();
		Thread.sleep(1000);
		vars.put("phone", js.executeScript("var phone = \"1\"+Math.round(Math.random()*7777777777); document.getElementsByClassName(\"staffPhone\")[0] .value = phone; return phone;"));
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
		driver.findElement(By.linkText("店长")).click();
		Thread.sleep(1000);
		vars.put("getSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);console.log(numText); var num = parseInt(numText); return num;", vars.get("getSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum2"))) {
		}
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		vars.put("getSumText3", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum3", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);console.log(numText); var num = parseInt(numText) + 1; return num;", vars.get("getSumText3")));
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		vars.put("getSumText4", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum3", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2);console.log(numText); var num = parseInt(numText); return num;", vars.get("getSumText4")));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("getSum1"), vars.get("getSum2"))) {
		}
	}

	@Test
	public void uTR29queryByRoleAfterByKeyword() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.name("queryKeyword")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys("456498789796");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("查无该员工"));
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
		driver.findElement(By.linkText("店长")).click();
		Thread.sleep(1000);
		vars.put("getSumText1", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum1", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getSumText1")));
		if ((Boolean) js.executeScript("return (arguments[0] <= 0)", vars.get("getSum1"))) {
		}
		driver.findElement(By.name("queryKeyword")).sendKeys("456498789796");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("查无该员工"));
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
		driver.findElement(By.linkText("收银员")).click();
		Thread.sleep(1000);
		vars.put("getSumText2", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum2", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getSumText2")));
		if ((Boolean) js.executeScript("return (arguments[0] <= 0)", vars.get("getSum2"))) {
		}
		driver.findElement(By.name("queryKeyword")).sendKeys("456498789796");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("查无该员工"));
		assertThat(driver.findElement(By.cssSelector(".layui-none")).getText(), is("无数据"));
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		vars.put("getSumText3", driver.findElement(By.cssSelector("#staffList + div .layui-laypage-count")).getText());
		vars.put("getSum3", js.executeScript("var numText = arguments[0].substring(2,arguments[0].length-2); var num = parseInt(numText); return num;", vars.get("getSumText3")));
		if ((Boolean) js.executeScript("return (arguments[0] <= 0)", vars.get("getSum3"))) {
		}
	}

	@Test
	public void uTR30userGuidance() throws InterruptedException {
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		vars.put("staffName", js.executeScript(
				"var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue; return idvalue;"));
		driver.findElement(By.name("newPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.name("confirmNewPassword")).sendKeys("000000");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建员工成功"));
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/span")).click();
		Thread.sleep(1000);
		vars.put("getStaffName", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.name("queryKeyword")).sendKeys("1585432");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
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
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.linkText("收银员")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone1", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'\')", vars.get("getPhone1"))) {
		}
		driver.findElement(By.linkText("全部在职员工")).click();
		Thread.sleep(1000);
		vars.put("getStaffName1", driver.findElement(By.name("name")).getAttribute("value"));
		driver.findElement(By.name("queryKeyword")).sendKeys("1585432");
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.cssSelector(".layui-layer-close")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone2", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'\')", vars.get("getPhone2"))) {
		}
		driver.findElement(By.name("queryKeyword")).sendKeys("1585432");
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
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
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		vars.put("getPhone3", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'\')", vars.get("getPhone3"))) {
			System.out.println("测试失败");
		}
		driver.findElement(By.id("staffMain")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).sendKeys(" ");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("取消")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(""));
		}
		driver.findElement(By.linkText("1")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		vars.put("getPhone4", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'\')", vars.get("getPhone4"))) {
			System.out.println("测试失败");
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
		vars.put("getPhone5", driver.findElement(By.name("phone")).getAttribute("value"));
		if ((Boolean) js.executeScript("return (arguments[0] == \'\')", vars.get("getPhone5"))) {
			System.out.println("测试失败");
		}
		driver.findElement(By.name("queryKeyword")).sendKeys("1585432");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
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
