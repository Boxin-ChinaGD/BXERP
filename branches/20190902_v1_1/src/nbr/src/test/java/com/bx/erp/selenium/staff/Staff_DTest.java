package com.bx.erp.selenium.staff;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class Staff_DTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTD1deleteNewStaff() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
		driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).click();
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
		driver.findElement(By.cssSelector(".trChoosed .laytable-cell-2-0-2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除员工成功"));
		driver.findElement(By.linkText("离职员工")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".layui-form-item:nth-child(5) .layui-unselect .layui-input")).getAttribute("value");
			assertThat(value, is("离职"));
		}
		{
			String value = driver.findElement(By.name("name")).getAttribute("value");
			assertThat(value, is(vars.get("staffName").toString()));
		}
	}
	
	  @Test
	  public void uTD2deleteOwn() throws InterruptedException {
	    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
	    driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
	    driver.switchTo().frame(1);
	    driver.findElement(By.name("queryKeyword")).sendKeys("15854320895");
		Thread.sleep(1000);
	    driver.findElement(By.cssSelector(".layui-icon")).click();
		Thread.sleep(1000);
	    driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("不允许删除当前登录人员"));
	  }
	  
	  @Test
	  public void uTD3deleteOldStaff() throws InterruptedException {
	    driver.findElement(By.cssSelector(".layui-icon-set-fill")).click();
	    driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
	    driver.switchTo().frame(1);
	    driver.findElement(By.xpath("//div[@id=\'staffMain\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[3]/div")).click();
		Thread.sleep(1000);
	    driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
	    driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除员工成功"));
	  }
	  
	  @Test
	  public void uTD4deleteSameStaffRepetitive() throws InterruptedException {
	    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
	    driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
	    driver.switchTo().frame(1);
	    driver.findElement(By.cssSelector(".staffCreate")).click();
		Thread.sleep(1000);
	    vars.put("staffName", js.executeScript("var arr = [\'A\',\'B\',\'C\',\'D\',\'E\',\'F\',\'G\',\'H\',\'I\',\'J\',\'K\',\'L\',\'M\',\'N\',\'O\',\'P\',\'Q\',\'R\',\'S\',\'T\',\'U\',\'V\',\'W\',\'X\',\'Y\',\'Z\']; var idvalue = \"员工\"; for(var i=0;i<8;i++){ idvalue+=arr[Math.floor(Math.random()*26)]; } document.getElementsByClassName(\"staffName\")[0].value = idvalue;"));
	    driver.findElement(By.cssSelector(".layui-form-item:nth-child(4) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
	    driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(2)")).click();
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
		Thread.sleep(1000);
	    driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
	    driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除员工成功"));
		Thread.sleep(1000);
	    driver.findElement(By.linkText("离职员工")).click();
	    {
	      String value = driver.findElement(By.name("phone")).getAttribute("value");
	      assertThat(value, is(vars.get("phone").toString()));
	    }
	    driver.findElement(By.cssSelector(".layui-form-item:nth-child(5) .layui-unselect .layui-input")).click();
		Thread.sleep(1000);
	    driver.findElement(By.cssSelector(".layui-form-selected dd:nth-child(1)")).click();
		Thread.sleep(1000);
	    driver.findElement(By.cssSelector(".staffUpdate")).click();
		Thread.sleep(1000);
	    driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("修改员工信息成功"));
	    driver.findElement(By.linkText("全部在职员工")).click();
		Thread.sleep(1000);
	    {
	      String value = driver.findElement(By.name("phone")).getAttribute("value");
	      assertThat(value, is(vars.get("phone").toString()));
	    }
	    driver.findElement(By.cssSelector(".deleteStaff")).click();
		Thread.sleep(1000);
	    driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("删除员工成功"));
	  }
	  
	  @Test
	  public void uTD5presalesToDeleteStaff() throws InterruptedException {
	    driver.findElement(By.cssSelector("i.layui-icon.layui-icon-set-fill")).click();
	    driver.findElement(By.linkText("员工管理")).click();
		Thread.sleep(1000);
	    driver.switchTo().frame(1);
	    assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("服务器错误，请重试"));
	  }
}
