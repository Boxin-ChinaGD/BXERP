package com.bx.erp.selenium.vip;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.selenium.BaseSeleniumTest;

public class CouponManage_CTest extends BaseSeleniumTest {
	@BeforeClass
	public void setUp() {
		super.setUp();
		operatorType = EnumOperatorType.EOT_BOSS.getIndex();
	}

	@Test
	public void uTC1chooseCommodityScope1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
	}

	@Test
	public void uTC2chooseCommodityScope2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
	}

	@Test
	public void uTC3openCommodityPopup() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(1000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
	}

	@Test
	public void uTC4statisticsCommodityInPopup1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("3"));
	}

	@Test
	public void uTC5statisticsCommodityInPopup2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[7]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[3]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("2"));
	}

	@Test
	public void uTC6chooseCommodityInPopup1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("commodityName1", driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td[2]/div/span")).getText());
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		vars.put("commodityName2", driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td[2]/div/span")).getText());
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		vars.put("commodityName1", js.executeScript("return arguments[0].split(\'/\')[0]", vars.get("commodityName1")));
		vars.put("commodityName2", js.executeScript("return arguments[0].split(\'/\')[0]", vars.get("commodityName2")));
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(3) > tr:nth-child(1) > td:nth-child(2)")).getText(), is("vars.get(\"commodityName1\").toString()"));
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(3) > tr:nth-child(2) > td:nth-child(2)")).getText(), is("vars.get(\"commodityName2\").toString()"));
	}

	@Test
	public void uTC7chooseCommodityInPopup2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("0"));
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		assertThat(driver.findElement(By.cssSelector("tbody:nth-child(3) td:nth-child(2)")).getText(), is(" "));
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\'couponMain\']/div[3]/form/div[15]/div/table/tbody/tr/td[2]/i")));
		}
	}

	@Test
	public void uTC8closeCommodityPopup1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\'toChooseComm\']")));
		}
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id=\'toChooseComm\']")));
		}
	}

	@Test
	public void uTC9closeCommodityPopup2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id=\'toChooseComm\']")));
		}
		driver.findElement(By.cssSelector(".confirmExitComm")).click();
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id=\'toChooseComm\']")));
		}
	}

	@Test
	public void uTC10notRecordBehaviorInPopup1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		vars.put("originalNum", driver.findElement(By.cssSelector("span > strong")).getText());
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-layer-ico")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("vars.get(\"originalNum\").toString()"));
	}

	@Test
	public void uTC11notRecordBehaviorInPopup2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		vars.put("originalNum", driver.findElement(By.cssSelector("span > strong")).getText());
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("thead .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[5]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[6]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmExitComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector("span > strong")).getText(), is("vars.get(\"originalNum\").toString()"));
	}

	@Test
	public void uTC12jumpToCommodityPageInPopup() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(500);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".leftRegion > .layui-btn")).click();
		Thread.sleep(1000);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		assertThat(driver.getTitle(), is("BoXin-商品列表"));
	}

	@Test
	public void uTC13createCoupon1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(2) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(2) .form_msg")).getText(), is("优惠券名称只能输入中文、英文或数字"));
	}

	@Test
	public void uTC14createCoupon2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys("壹贰叁肆伍上山打老虎");
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(2) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(2) .form_msg")).getText(), is("优惠券名称只能输入中文、英文或数字"));
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
	}

	@Test
	public void uTC15createCoupon3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定创建该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建优惠券成功"));
		{
			String value = driver.findElement(By.cssSelector(".layui-form-item:nth-child(3) > .layui-input-block input")).getAttribute("value");
			assertThat(value, is("#07C160"));
		}
	}

	@Test
	public void uTC16createCoupon4() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(6) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(6) .form_msg")).getText(), is("优惠券起用金额需大于等于0，只允许有2位小数"));
	}

	@Test
	public void uTC17createCoupon5() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12.345");
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(6) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(6) .form_msg")).getText(), is("优惠券起用金额需大于等于0，只允许有2位小数"));
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
	}

	@Test
	public void uTC18createCoupon6() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(7) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) .form_msg")).getText(), is("优惠券减免金额需大于0，并小于起用金额，只允许有2位小数"));
	}

	@Test
	public void uTC19createCoupon7() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("13");
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(7) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(7) .form_msg")).getText(), is("优惠券减免金额需大于0，并小于起用金额，只允许有2位小数"));
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
	}

	@Test
	public void uTC20createCoupon8() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.cssSelector(".layui-select-title > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-anim > dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("11");
		driver.findElement(By.name("discount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("discount")).sendKeys(" ");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(8) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) .form_msg")).getText(), is("优惠券打折额度的输入需大于0，小于10，只允许有2位小数"));
	}

	@Test
	public void uTC21createCoupon9() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.cssSelector(".layui-select-title > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-anim > dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("10");
		driver.findElement(By.name("discount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("discount")).sendKeys("11");
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(8) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(8) .form_msg")).getText(), is("优惠券打折额度的输入需大于0，小于10，只允许有2位小数"));
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
	}

	@Test
	public void uTC22createCoupon10() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form_msg:nth-child(4)")));
		}
		assertThat(driver.findElement(By.cssSelector(".form_msg:nth-child(4)")).getText(), is("有效期范围不正确，结束时间应晚于开始时间"));
	}

	@Test
	public void uTC23createCoupon11() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".form_msg:nth-child(4)")));
		}
		assertThat(driver.findElement(By.cssSelector(".form_msg:nth-child(4)")).getText(), is("有效期范围不正确，结束时间应晚于开始时间"));
		driver.findElement(By.cssSelector(".createCoupon")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
	}

	@Test
	public void uTC24createCoupon12() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("bonus")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("bonus")).sendKeys(" ");
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(11) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) .form_msg")).getText(), is("输入的积分需为大于等于0的整数"));
	}

	@Test
	public void uTC25createCoupon13() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("bonus")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("bonus")).sendKeys("1.1");
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(11) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(11) .form_msg")).getText(), is("输入的积分需为大于等于0的整数"));
		driver.findElement(By.cssSelector(".createCoupon")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
	}

	@Test
	public void uTC26createCoupon14() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定创建该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建优惠券成功"));
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.cssSelector(".layui-select-title > .layui-input")).click();
		driver.findElement(By.cssSelector(".layui-anim > dd:nth-child(2)")).click();
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("discount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("discount")).sendKeys("8");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定创建该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建优惠券成功"));
	}

	@Test
	public void uTC27createCoupon15() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("description")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("description")).sendKeys(
				"0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎abcdfg0123456789上山打老虎￥……*&……（）……（）12345678！*");
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(15) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(15) .form_msg")).getText(), is("优惠券使用的字数长度需小于等于1024"));
		driver.findElement(By.cssSelector(".createCoupon")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
	}

	@Test
	public void uTC28createCoupon16() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("personalLimit")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("personalLimit")).sendKeys(" ");
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(12) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .form_msg")).getText(), is("设置的优惠券每人可领券数量需为大于等于1的整数"));
	}

	@Test
	public void uTC29createCoupon17() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("personalLimit")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("personalLimit")).sendKeys("0");
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(12) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(12) .form_msg")).getText(), is("设置的优惠券每人可领券数量需为大于等于1的整数"));
		driver.findElement(By.cssSelector(".createCoupon")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
	}

	@Test
	public void uTC30createCoupon18() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("quantity")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("quantity")).sendKeys(" ");
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(13) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(13) .form_msg")).getText(), is("设置的优惠券库存数量需为大于等于1的整数"));
	}

	@Test
	public void uTC31createCoupon19() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("quantity")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("quantity")).sendKeys("0");
		{
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".layui-form-item:nth-child(13) .form_msg")));
		}
		assertThat(driver.findElement(By.cssSelector(".layui-form-item:nth-child(13) .form_msg")).getText(), is("设置的优惠券库存数量需为大于等于1的整数"));
		driver.findElement(By.cssSelector(".createCoupon")).click();
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("优惠券数据格式不符合要求，请检查修改后重试"));
	}

	@Test
	public void uTC32createCoupon20() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("请选择可使用该优惠券的商品"));
	}

	@Test
	public void uTC34createCoupon22() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.cssSelector(".layui-select-title > .layui-input")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-anim > dd:nth-child(2)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(8)")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("discount")).sendKeys("1");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(9) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(2) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(14) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("description")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("description")).sendKeys("我是使用说明");
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定创建该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建优惠券成功"));
	}

	@Test
	public void uTC35createCoupon23() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".layui-btn > .layui-icon")).click();
		Thread.sleep(1000);
		vars.put("couponName", js.executeScript("return \"随机优惠券\" + Math.floor(Math.random() * 999);"));
		driver.findElement(By.name("title")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("title")).sendKeys(vars.get("couponName").toString());
		driver.findElement(By.name("leastAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("leastAmount")).sendKeys("12");
		driver.findElement(By.name("reduceAmount")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("reduceAmount")).sendKeys("11");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(9) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(10) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-unselect:nth-child(12) > span")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("beginDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(1)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("endDateTime")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-next-m")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("tbody:nth-child(2) > tr:nth-child(4) > td:nth-child(7)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".laydate-btns-confirm")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("description")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("description")).sendKeys("我是使用说明");
		driver.findElement(By.name("bonus")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("bonus")).sendKeys("10");
		driver.findElement(By.cssSelector(".layui-form-item:nth-child(16) .layui-unselect:nth-child(4) > .layui-anim")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-icon-add-circle")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'toChooseComm\']/div[2]/div[2]/div/div[2]/table/tbody/tr[2]/td/div/div/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".confirmChoosedComm")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".createCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定创建该优惠券？"));
		driver.findElement(By.linkText("确定")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("创建优惠券成功"));
	}

	@Test
	public void uTC36operationTips1() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
	}

	@Test
	public void uTC37operationTips2() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id=\'couponMain\']/div[2]/div/div/div[2]/table/tbody/tr/td[7]/div/span")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
	}

	@Test
	public void uTC38operationTips3() throws InterruptedException {
		driver.findElement(By.cssSelector(".layui-icon-diamond")).click();
		driver.findElement(By.linkText("优惠券管理")).click();
		Thread.sleep(5000);
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".toCreateCoupon")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".layui-laypage-next:nth-child(5) > .layui-icon")).click();
		Thread.sleep(1000);
		assertThat(driver.findElement(By.cssSelector(".layui-layer-content")).getText(), is("确定要放弃之前的操作吗？"));
	}
}
