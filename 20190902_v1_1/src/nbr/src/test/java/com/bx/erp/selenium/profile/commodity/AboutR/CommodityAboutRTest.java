package com.bx.erp.selenium.profile.commodity.AboutR;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CommodityAboutRTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

	public void LoginSucceed() {
		driver.get("https://localhost:8888/home/adminLogin.bx");
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
	}

	public void PreSaleLoginSucceed() {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("13888888888");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
	}

	@Test
	public void loginSucceed() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("15854320895");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
	}

	@Test
	public void preSaleLoginSucceed() throws InterruptedException {
		driver.get(StaffLoginUrl);
		driver.manage().window().setSize(new Dimension(1920, 1040));
		driver.findElement(By.name("companySN")).click();
		driver.findElement(By.name("companySN")).sendKeys("668866");
		driver.findElement(By.name("phone")).click();
		driver.findElement(By.name("phone")).sendKeys("13888888888");
		driver.findElement(By.name("pwdEncrypted")).click();
		driver.findElement(By.name("pwdEncrypted")).sendKeys("000000");
		driver.findElement(By.cssSelector(".layui-btn")).click();
		Thread.sleep(1000);
	}

	@Test
	public void uTR1checkSearchValueLength() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		vars.put("count1", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count1").toString());
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("012345678901234567890123456789123");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		vars.put("count2", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count2").toString());
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("count1"), vars.get("count2"))) {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTR2checkSearchValue1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		vars.put("count1", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count1").toString());
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		vars.put("count2", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count2").toString());
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("count1"), vars.get("count2"))) {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTR3checkSearchValue2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		js.executeScript("var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     if(page > 0){      a[page].click();     }");
		Thread.sleep(1000);
		vars.put("count1", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count1").toString());
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("@#");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content.layui-layer-padding")).getText(), is("允许以中英数值、空格形式出现，不允许使用特殊符号"));
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		driver.findElement(By.id("name")).click();
		vars.put("provider1", js.executeScript("return \"供应商\"+Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider1").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商成功"));
		Thread.sleep(1000);
		js.executeScript("var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     if(page > 0){      a[page].click();     }");
		Thread.sleep(1000);
		vars.put("count2", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count2").toString());
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("count1"), vars.get("count2"))) {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTR4checkSearchValue3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		vars.put("count1", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count1").toString());
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("默认供应商");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		vars.put("count2", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count2").toString());
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("count1"), vars.get("count2"))) {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTR5searchProviderByDistrict1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		vars.put("count1", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count1").toString());
		driver.findElement(By.cssSelector("input.district")).click();
		vars.put("count2", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     if(page > 0){      a[page].click();     }var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count2").toString());
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("count1"), vars.get("count2"))) {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTR6searchProviderByDistrict2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		js.executeScript("var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     if(page > 0){      a[page].click();     }");
		Thread.sleep(1000);
		vars.put("count1", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count1").toString());
		driver.findElement(By.cssSelector("p:nth-child(3) > .district")).click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vars.put("count2", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = 0;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count2").toString());
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("count1"), vars.get("count2"))) {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTR7searchProviderMessage() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rowSpace > .district")).click();
		vars.put("count1", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count1").toString());
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".rowSpace > .district")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".paddingLeft:nth-child(3) > input:nth-child(2)")).click();
		vars.put("count2", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count2").toString());
		driver.findElement(By.cssSelector(".rowSpace > .district")).click();
		vars.put("count3", js.executeScript(
				"var providerPage = document.getElementById(\"providerPage\");     var a = providerPage.getElementsByTagName(\"a\");     var page = a.length - 2;     var num = 0;     var provider = document.getElementById(\"provider\");     var pLength = provider.getElementsByTagName(\"p\").length;     if(page > 0){      num = page * 10;      num += pLength;     }else{      num = pLength;     }     if(pLength == \"1\"){      var p = provider.getElementsByTagName(\"p\")[0];      var value = p.getElementsByTagName(\"input\")[1].getAttribute(\"placeholder\");      if(value == \"没有内容，增加试试\"){       num = num - 1;      }     } return  num;"));
		System.out.println("此时记录的值为：" + vars.get("count3").toString());
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1] || arguments[0] != arguments[2] || arguments[1] != arguments[2])", vars.get("count1"), vars.get("count2"), vars.get("count3"))) {
			Assert.assertTrue(false, "测试失败");
		}
		Thread.sleep(1000);
	}

	@Test
	public void uTR8searchCategoryByParent1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		vars.put("categoryLength1", js.executeScript("var categoryLength = document.getElementById(\"category\").getElementsByTagName(\"p\").length; return categoryLength;"));
		driver.findElement(By.cssSelector("input.categoryParent")).click();
		vars.put("categoryLength2", js.executeScript("var categoryLength = document.getElementById(\"category\").getElementsByTagName(\"p\").length; return categoryLength;"));
		if ((Boolean) js.executeScript("return (arguments[0] != arguments[1])", vars.get("categoryLength1"), vars.get("categoryLength2"))) {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTR9searchCategoryByParent2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		vars.put("categoryLength1", js.executeScript("var categoryLength = document.getElementById(\"category\").getElementsByTagName(\"p\").length; return categoryLength;"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .categoryParent")).click();
		vars.put("categoryLength2", js.executeScript("var categoryLength = document.getElementById(\"category\").getElementsByTagName(\"p\").length; return categoryLength;"));
		if ((Boolean) js.executeScript("return (arguments[0] == arguments[1])", vars.get("categoryLength1"), vars.get("categoryLength2"))) {
			Assert.assertTrue(false, "测试失败");
		}
	}

	@Test
	public void uTR11providerPage1() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("em:nth-child(2)")).click();
		vars.put("providerName", driver.findElement(By.cssSelector(".provideSelect > input:nth-child(2)")).getAttribute("value"));
		driver.findElement(By.linkText("<")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText(">")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".provideSelect > input:nth-child(2)")).getAttribute("value");
			assertThat(value, is(vars.get("providerName").toString()));
		}
	}

	@Test
	public void uTR12providerPage2() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider1", js.executeScript("return \"供应商\"+Math.round(Math.random()*99999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider1").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider2", js.executeScript("return \"供应商\"+Math.round(Math.random()*99999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider2").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider3", js.executeScript("return \"供应商\"+Math.round(Math.random()*99999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider3").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider4", js.executeScript("return \"供应商\"+Math.round(Math.random()*99999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider4").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.blackIcon.add")).click();
		vars.put("provider5", js.executeScript("return \"供应商\"+Math.round(Math.random()*99999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider5").toString());
		driver.findElement(By.cssSelector("button.commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("新增供应商成功"));
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-this > .layui-icon")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@value=\'默认区域\']")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("em:nth-child(2)")).click();
		vars.put("providerName", driver.findElement(By.cssSelector(".provideSelect > input:nth-child(2)")).getAttribute("value"));
		driver.findElement(By.linkText("<")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText(">")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".provideSelect > input:nth-child(2)")).getAttribute("value");
			assertThat(value, is(vars.get("providerName").toString()));
		}
	}

	@Test
	public void uTR13providerPage3() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("供应商");
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("2")).click();
		vars.put("providerName", driver.findElement(By.cssSelector(".provideSelect > input:nth-child(2)")).getAttribute("value"));
		Thread.sleep(1000);
		driver.findElement(By.linkText("<")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText(">")).click();
		{
			String value = driver.findElement(By.cssSelector(".provideSelect > input:nth-child(2)")).getAttribute("value");
			assertThat(value, is(vars.get("providerName").toString()));
		}
	}

	@Test
	public void uTR14PreSaleProvider() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys("供应商");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
	}

	@Test
	public void uTR15PreSaleCategoryParent() throws InterruptedException {
		PreSaleLoginSucceed();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("i.layui-icon.layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("权限不足"));
		driver.findElement(By.cssSelector(".rowSpace:nth-child(2) > .categoryParent")).click();
		driver.findElement(By.xpath("//input[@value=\'蛋肉家禽\']")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("input.category")).click();
		driver.findElement(By.xpath("//input[@value=\'蔬菜水果\']")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("input.category")).click();
	}

	@Test
	public void uTR16searchByPhone() throws InterruptedException {
		LoginSucceed();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		// driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("商品相关")).click();
		driver.switchTo().frame(1);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		driver.findElement(By.id("name")).click();
		vars.put("provider", js.executeScript("return\"供应商\" + Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.name("mobile")).click();
		vars.put("phone", js.executeScript("return\"183201636\" + Math.round(Math.random()*999999)"));
		driver.findElement(By.name("mobile")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商成功"));
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		{
			String value = driver.findElement(By.cssSelector(".paddingLeft > input:nth-child(2)")).getAttribute("value");
			assertThat(value, is(vars.get("provider").toString()));
		}
		driver.findElement(By.cssSelector(".provideSelect > .add")).click();
		driver.findElement(By.id("name")).click();
		vars.put("provider", js.executeScript("return\"供应商\" + Math.round(Math.random()*999999)"));
		driver.findElement(By.id("name")).sendKeys(vars.get("provider").toString());
		driver.findElement(By.name("mobile")).click();
		vars.put("phone", js.executeScript("return\"987-147258369\" + Math.round(Math.random()*999999)"));
		driver.findElement(By.name("mobile")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector("#providerMessageButton > .commodityButton")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("新增供应商成功"));
		driver.findElement(By.name("queryKeyword")).click();
		driver.findElement(By.name("queryKeyword")).sendKeys(vars.get("phone").toString());
		driver.findElement(By.cssSelector(".layui-icon-search")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("div.layui-layer-content")).getText(), is("允许以中英数值、空格形式出现，不允许使用特殊符号"));
	}
}
