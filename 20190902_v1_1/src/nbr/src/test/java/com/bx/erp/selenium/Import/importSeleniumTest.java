package com.bx.erp.selenium.Import;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;


import org.openqa.selenium.By;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Vip;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.selenium.BaseSeleniumTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class importSeleniumTest extends BaseSeleniumTest {

	@BeforeClass
	public void setUp() {
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
		super.setUp();
	}

	@Test
	public void testImportEx1() throws AWTException, InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(1000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(2000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx2() throws AWTException, InterruptedException {
		Shared.caseLog("case2: 文件类型不正确(txt)");

		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case2.txt", "博销宝资料导入模板.txt");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.txt");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(1000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(2000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		renameFile("博销宝资料导入模板.txt", "import_Case2.txt");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("选择的文件中包含不支持的格式"));

	}

	@Test
	public void testImportEx3() throws AWTException, InterruptedException {
		Shared.caseLog("case3: 文件类型不正确(rar)");

		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("商品资料")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.rar");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(1000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(2000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("选择的文件中包含不支持的格式"));

	}

	@Test
	public void testImportEx4() throws AWTException, InterruptedException {
		Shared.caseLog("case4: 文件类型不正确(zip)");

		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.zip");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(1000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(2000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("选择的文件中包含不支持的格式"));

	}

	@Test
	public void testImportEx5() throws AWTException, InterruptedException {
		Shared.caseLog("case5: 创建100个会员耗时");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case7.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(1000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(2000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(30000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case7.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("88"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx6() throws AWTException, InterruptedException {
		Shared.caseLog("case6:创建100个商品耗时");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case8.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(1000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(2000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(30000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case8.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("97"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx7() throws AWTException, InterruptedException {
		Shared.caseLog("case7:创建1000个商品耗时");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case9.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(1000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(2000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(200000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case9.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("997"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx8() throws AWTException, InterruptedException {
		Shared.caseLog("case8:创建10000个商品耗时");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case10.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(1000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(2000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(3000000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case10.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx9() throws AWTException, InterruptedException {
		Shared.caseLog("case9:商品barcodes格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case11_wrongBarcodes.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case11_wrongBarcodes.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx10() throws AWTException, InterruptedException {
		Shared.caseLog("case10:商品名称格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case12_wrongCommName.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case12_wrongCommName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx11() throws AWTException, InterruptedException {
		Shared.caseLog("case11:包装单位名称格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case13_wrongCommPackageUnitName.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case13_wrongCommPackageUnitName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx12() throws AWTException, InterruptedException {
		Shared.caseLog("case12:零售价格式错误，parse1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case14_wrongCommPriceRetail.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case14_wrongCommPriceRetail.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx13() throws AWTException, InterruptedException {
		Shared.caseLog("case13:供应商名称格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case15_wrongProviderName.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case15_wrongProviderName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("1"));
	}

	@Test
	public void testImportEx14() throws AWTException, InterruptedException {
		Shared.caseLog("case14:商品规格格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case17_wrongCommSpecification.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case17_wrongCommSpecification.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx15() throws AWTException, InterruptedException {
		Shared.caseLog("case15:会员价格式错误，parse1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case20_wrongCommPriceVIP.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case20_wrongCommPriceVIP.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx16() throws AWTException, InterruptedException {
		Shared.caseLog("case16:保质期格式错误，parse1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case21_wrongCommShelfLife.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case21_wrongCommShelfLife.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx17() throws AWTException, InterruptedException {
		Shared.caseLog("case17:退货天数格式错误，parse1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case22_wrongCommReturnDays.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case22_wrongCommReturnDays.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx18() throws AWTException, InterruptedException {
		Shared.caseLog("case18:期初数量格式错误，doPase1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case24_wrongCommNOStart.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case24_wrongCommNOStart.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx19() throws AWTException, InterruptedException {
		Shared.caseLog("case19:期初采购价格式错误，doPase1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case25_wrongCommPurchasingPriceStart.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case25_wrongCommPurchasingPriceStart.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx20() throws AWTException, InterruptedException {
		Shared.caseLog("case20:供应商地址格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case27_wrongProviderAddress.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case27_wrongProviderAddress.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("1"));
	}

	@Test
	public void testImportEx21() throws AWTException, InterruptedException {
		Shared.caseLog("case21:供应商联系人格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case28_wrongProviderContactName.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		// 指定图片路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把图片路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case28_wrongProviderContactName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("1"));
	}

	@Test
	public void testImportEx22() throws AWTException, InterruptedException {
		Shared.caseLog("case22:供应商联系人电话格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case29_wrongProviderMobile.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case29_wrongProviderMobile.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("1"));
	}

	@Test
	public void testImportEx23() throws AWTException, InterruptedException {
		Shared.caseLog("case23:供应商区域格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case30_wrongProviderDisctrict.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case30_wrongProviderDisctrict.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("1"));
	}

	@Test
	public void testImportEx24() throws AWTException, InterruptedException {
		Shared.caseLog("case24:会员积分格式错误，doPase1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case31_wrongVipBonus.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case31_wrongVipBonus.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx25() throws AWTException, InterruptedException {
		Shared.caseLog("case25:会员名称格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case35_wrongVipName.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case35_wrongVipName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx26() throws AWTException, InterruptedException {
		Shared.caseLog("case26:会员性别格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case36_wrongVipSex.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case36_wrongVipSex.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx27() throws AWTException, InterruptedException {
		Shared.caseLog("case27:会员生日格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case37_wrongVipBirthday.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case37_wrongVipBirthday.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx28() throws AWTException, InterruptedException {
		Shared.caseLog("case28:会员上次消费日期时间格式错误，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case38_lastConsumeDatetime.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case38_lastConsumeDatetime.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx29() throws AWTException, InterruptedException {
		Shared.caseLog("case29:会员性别格式错误，parse1失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case36_2_wrongVipSex.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case36_2_wrongVipSex.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx30() throws AWTException, InterruptedException {
		Shared.caseLog("case30:会员性别不填写，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case36_3_wrongVipSex.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case36_3_wrongVipSex.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx31() throws AWTException, InterruptedException {
		Shared.caseLog("case31:会员手机号格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case34_2_wrongVipMobile.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case34_2_wrongVipMobile.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx32() throws AWTException, InterruptedException {
		Shared.caseLog("case32:会员积分格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case31_2_wrongVipBonus.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case31_2_wrongVipBonus.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx33() throws AWTException, InterruptedException {
		Shared.caseLog("case33:期初采购价格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case25_2_wrongCommPurchasingPriceStart.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case25_2_wrongCommPurchasingPriceStart.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx34() throws AWTException, InterruptedException {
		Shared.caseLog("case34:期初数量格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case24_2_wrongCommNOStart.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case24_2_wrongCommNOStart.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx35() throws AWTException, InterruptedException {
		Shared.caseLog("case35:退货天数格式错误，checkCreate不通过，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case22_2_wrongCommReturnDays.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case22_2_wrongCommReturnDays.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx36() throws AWTException, InterruptedException {
		Shared.caseLog("case36:保质期格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case21_2_wrongCommShelfLife.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case21_2_wrongCommShelfLife.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx37() throws AWTException, InterruptedException {
		Shared.caseLog("case37:会员价格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case20_2_wrongCommPriceVIP.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case20_2_wrongCommPriceVIP.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx38() throws AWTException, InterruptedException {
		Shared.caseLog("case38:品牌名称格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case49_wrongBrandName.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case49_wrongBrandName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx39() throws AWTException, InterruptedException {
		Shared.caseLog("case39:商品规格格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case51_wrongCommSpecification.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case51_wrongCommSpecification.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx40() throws AWTException, InterruptedException {
		Shared.caseLog("case40:商品小类名称格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case52_wrongCommCategoryName.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case52_wrongCommCategoryName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx41() throws AWTException, InterruptedException {
		Shared.caseLog("case41:没有提供供应商信息，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case15_2_wrongProviderName.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case15_2_wrongProviderName.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx42() throws AWTException, InterruptedException {
		Shared.caseLog("case42:零售价格式错误，checkCreate失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case14_2_wrongCommPriceRetail.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case14_2_wrongCommPriceRetail.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx43() throws AWTException, InterruptedException {
		Shared.caseLog("case43:文件大小不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case57_wrongFileSize.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		Thread.sleep(3000);
		driver.findElement(By.linkText("导入资料")).click();
		Thread.sleep(3000);
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		renameFile("博销宝资料导入模板.xlsm", "import_Case57_wrongFileSize.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("文件不能超过5.00MB"));
	}

	@Test
	public void testImportEx44() throws AWTException, InterruptedException {
		Shared.caseLog("case44:创建会员后，会员初始积分、会员卡号是否是excel定义的的，而不是积分规则的初始积分");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case58.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case58.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx45() throws AWTException, InterruptedException {
		Shared.caseLog("case45:创建的供应商手机号与DB中的重复，创建失败，验证目标excel文件是否加标注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case59_providerExistInDB.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case59_providerExistInDB.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("1"));
	}

	@Test
	public void testImportEx46() throws AWTException, InterruptedException {
		Shared.caseLog("case46:excel文件有多个商品文件格式有错误，验证目标excel文件是否加标注，并正确返回有多少个格式不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case60_sumCommWrongFormatNO.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case60_sumCommWrongFormatNO.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("30"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("13"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx47() throws AWTException, InterruptedException {
		Shared.caseLog("case47:excel文件有多个会员格式有错误，验证目标excel文件是否加标注，并正确返回有多少个格式不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case61_sumWrongVipFormatNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case61_sumWrongVipFormatNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("13"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("6"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx48() throws AWTException, InterruptedException {
		Shared.caseLog("case48:excel文件有多个供应商格式有错误，验证目标excel文件是否加标注，并正确返回有多少个格式不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case62_sumWrongProviderFormatNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case62_sumWrongProviderFormatNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("10"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("5"));
	}

	@Test
	public void testImportEx49() throws AWTException, InterruptedException {
		Shared.caseLog("case49:excel文件有多个供应商、商品、会员格式有错误，验证目标excel文件是否加标注，并正确返回有多少个格式不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case63_sumWrongProviderVipCommFormatNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case63_sumWrongProviderVipCommFormatNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("30"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("13"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("13"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("6"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("10"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("5"));
	}

	@Test
	public void testImportEx50() throws AWTException, InterruptedException {
		Shared.caseLog("case50:excel文件有多个商品创建失败，验证目标excel文件是否加标注，并正确返回失败个数");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case64_sumFailCreateCommNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(15000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case64_sumFailCreateCommNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("30"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("30"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx51() throws AWTException, InterruptedException {
		Shared.caseLog("case51:excel文件有多个会员创建失败，验证目标excel文件是否加标注，并正确返回失败个数");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case65_sumFailCreateVipNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case65_sumFailCreateVipNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("13"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("12"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx52() throws AWTException, InterruptedException {
		Shared.caseLog("case52:excel文件有多个供应商格式有错误(与DB中的手机号重复)，验证目标excel文件是否加标注，并正确返回有多少个格式不正确");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case66_sumFailCreateProviderNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case66_sumFailCreateProviderNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("10"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("10"));
	}

	@Test
	public void testImportEx53() throws AWTException, InterruptedException {
		Shared.caseLog("case53:excel文件有多个商品、会员创建失败，验证目标excel文件是否加标注，并正确返回失败个数");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case67_sumFailCreateCommAndVipNumber.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(15000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case67_sumFailCreateCommAndVipNumber.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("30"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("30"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("13"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("12"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx54() throws AWTException, InterruptedException {
		Shared.caseLog("case54:D:\\nbr\\file\\import目录下文件夹为空，判断是否会自动生成目录和文件(测试时需要手动清空import，然后查看是否自动生成目录和文件)");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case68.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case68.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx55() throws AWTException, InterruptedException {
		Shared.caseLog("case55:excel文件有多个会员创建成功，多个会员手机号与DB中的重复，这时候不用提示用户下载");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case69.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case69.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("3"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx56() throws AWTException, InterruptedException {
		Shared.caseLog("case56:excel文件有多个会员创建成功，多个会员手机号与DB中的重复，多个因为数据库等原因创建失败，这时候需要提示用户下载，下载后的内容只包含因为数据库等原因创建失败的数据行");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case79.xlsm", "博销宝资料导入模板.xlsm");
		//
		Vip vip = new Vip();
		vip.setPageIndex(BaseAction.PAGE_StartIndex);
		vip.setPageSize(BaseAction.PAGE_SIZE_MAX);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, vip);
		int befourVipNO = vipBO.getTotalRecord();
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case79.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("10"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, vip);
		int afterVipNO = vipBO.getTotalRecord();
		Assert.assertTrue(befourVipNO + 5 == afterVipNO, "创建的会员数量不正确"); // 十个会员只有5个是正确上传的
	}

	@Test
	public void testImportEx57() throws AWTException, InterruptedException {
		Shared.caseLog("case57:excel文件有多个商品创建成功，多个商品名称与DB中的重复，这时候不用提示用户下载");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case80.xlsm", "博销宝资料导入模板.xlsm");
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(BaseAction.PAGE_StartIndex);
		comm.setPageSize(BaseAction.PAGE_SIZE_MAX);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, comm);
		int befourCommodityNO = commodityBO.getTotalRecord();
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case80.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("10"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, comm);
		int afterCommodityNO = commodityBO.getTotalRecord();
		Assert.assertTrue(befourCommodityNO + 5 == afterCommodityNO, "创建的商品数量不正确"); // 十个商品只有5个是正确上传的
	}

	// 现在只要格式没有问题，如果是创建时出错，是不会返回错误Excel的
	// @Test
	// public void testImportEx58() throws AWTException, InterruptedException {
	// Shared.caseLog("case58:excel文件有多个商品创建成功，多个商品重复，多个商品因数据库等原因失败，上传后提示有错误，点击下载，期望是下载的文件只包含数据库等原因失败创建失败的数据行");
	// }

	@Test
	public void testImportEx59() throws AWTException, InterruptedException {
		Shared.caseLog("case59:excel文件商品为null(不填写)，格式错误，验证是否加批注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case73.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case73.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("1"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx60() throws AWTException, InterruptedException {
		Shared.caseLog("case60:excel文件会员生日和上次消费时间为null(不填写)，属于格式正常，验证是否创建了生日和消费时间为null的会员");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case74.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case74.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("4"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx61() throws AWTException, InterruptedException {
		Shared.caseLog("case61:excel文件商品名称、会员手机，供应商名称、供应商手机有重复，格式错误，验证是否添加批注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case75.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case75.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("10"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("5"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("10"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("5"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("22"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("13"));
	}

	@Test
	public void testImportEx62() throws AWTException, InterruptedException {
		Shared.caseLog("case62:excel文件供应商格式正确，正常创建供应商");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case76.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case76.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("2"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
	}

	@Test
	public void testImportEx63() throws AWTException, InterruptedException {
		Shared.caseLog("case63:excel文件有多个商品名称重复，多个会员手机号重复，多个供应商名称重复，多个供应商手机号重复，验证是否正确返回格式错误个数和添加批注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case77.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case77.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("19"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("10"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("12"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("6"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("10"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("6"));
	}

	@Test
	public void testImportEx64() throws AWTException, InterruptedException {
		Shared.caseLog("case64:excel文件商品表、会员表、供应商表中间有空白行，验证是否正确返回格式错误个数和添加批注");
		//
		renameFile("博销宝资料导入模板.xlsm", "1.xlsm");
		renameFile("import_Case78.xlsm", "博销宝资料导入模板.xlsm");
		//
		driver.findElement(By.cssSelector(".layui-icon-app")).click();
		driver.findElement(By.linkText("导入资料")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.id("test")).click();
		//
		fillInExcelFilePath();
		//
		Thread.sleep(3000);
		driver.findElement(By.id("testListAction")).click();
		Thread.sleep(10000);
		//
		renameFile("博销宝资料导入模板.xlsm", "import_Case78.xlsm");
		renameFile("1.xlsm", "博销宝资料导入模板.xlsm");
		//
		assertThat(driver.findElement(By.cssSelector(".commodityTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".commodityWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipTotalToCreate")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".vipWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerWrongFormatNumber")).getAttribute("textContent"), is("0"));
		assertThat(driver.findElement(By.cssSelector(".providerTotalToCreate")).getAttribute("textContent"), is("0"));
	}

	public void renameFile(String oldName, String newName) {
		try {
			File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\" + oldName);
			file.renameTo(new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\" + newName));
		} catch (Exception ex) {
			Assert.assertTrue(false, ex.getMessage());
		}
		File file = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\" + newName);
		if (!file.exists()) {
			Assert.assertTrue(false, "重命名失败，可能是运行测试时了打开文件，或该目录下已经有1.xlsm，请到对应的文件夹进行svn update并删除1.xlsm");
		}
	}

	public void fillInExcelFilePath() throws InterruptedException, AWTException {
		// 指定文件路径
		StringSelection selection = new StringSelection("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\test\\resources\\import\\博销宝资料导入模板.xlsm");
		// 把文件路径复制到剪切板
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		System.out.println("selection" + selection);

		Robot robot = new Robot();
		Thread.sleep(3000);
		// 按下Ctrl+V
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		// 释放Ctrl+V
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
		Thread.sleep(3000);
		// 点击回车
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}
}
