package com.bx.erp.selenium;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.bx.erp.model.BxStaff;
import com.bx.erp.model.Staff;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;

public class BaseSeleniumTest extends BaseActionTest{
	protected WebDriver driver;
	protected Map<String, Object> vars;
	protected JavascriptExecutor js;
	/** 0=op, 1=staff, 2=presale */
	public static final String StaffLoginUrl = "http://localhost:80/home/adminLogin.bx";
	public static final String BxStaffLoginUrl = "http://localhost:80/home/bxAdminLogin.bx";
	/** 0=op, 1=staff */
	protected int operatorType;
	
	protected boolean bNeedAssistantManagerLogin = true;

	public int getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(int operatorType) {
		this.operatorType = operatorType;
	}

	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "D:\\BXERP\\trunk\\doc\\自动化测试\\Selenium\\chromedriver.exe");
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();
	}

	@BeforeMethod
	public void beforeMethod() {
		switch (EnumOperatorType.values()[operatorType]) {
		case EOT_OP:
			bxStaffLogin();
			break;
		case EOT_BOSS:
			staffLogin(Shared.PhoneOfBoss);
			break;
		case EOT_PRESALE:
			staffLogin(Shared.PhoneOfPreSale);
			break;
		case EOT_PhoneOfAssistManager:
			if(bNeedAssistantManagerLogin) {
				staffLogin(Shared.PhoneOfPreSale);
			}
			break;
		default:
			break;
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterMethod
	public void afterMethod() {
		logout();
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

	protected void logout() {
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".layui-btn")).click();
	}

	protected void bxStaffLogin() {
		// 如果访问地址使用https的,在访问的时候chrome会告诉你这是一个私密的链接并是否让用户确认继续前往。而selenium并没有做这种处理，就会导致测试失败
		driver.get("http://localhost:80/home/bxAdminLogin.bx");
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name(BxStaff.field.getFIELD_NAME_mobile())).click();
		driver.findElement(By.name(BxStaff.field.getFIELD_NAME_mobile())).sendKeys(Shared.BxStaff_Phone);
		driver.findElement(By.name(BxStaff.field.getFIELD_NAME_salt())).click();
		driver.findElement(By.name(BxStaff.field.getFIELD_NAME_salt())).sendKeys(Shared.PASSWORD_DEFAULT);
		driver.findElement(By.cssSelector(".layui-btn")).click();
	}

	public void staffLogin(String phone) {
		driver.get("http://localhost:80/home/adminLogin.bx");
		driver.manage().window().setSize(new Dimension(1936, 1056));
		driver.findElement(By.name(Staff.field.getFIELD_NAME_companySN())).click();
		driver.findElement(By.name(Staff.field.getFIELD_NAME_companySN())).sendKeys(Shared.DB_SN_Test);
		driver.findElement(By.name(Staff.field.getFIELD_NAME_phone())).click();
		driver.findElement(By.name(Staff.field.getFIELD_NAME_phone())).sendKeys(phone);
		driver.findElement(By.name(Staff.field.getFIELD_NAME_pwdEncrypted())).click();
		driver.findElement(By.name(Staff.field.getFIELD_NAME_pwdEncrypted())).sendKeys(Shared.PASSWORD_DEFAULT);
		driver.findElement(By.cssSelector(".layui-btn")).click();
	}

	public enum EnumOperatorType {
		EOT_OP("EOT_OP", 0), EOT_BOSS("EOT_BOSS", 1), EOT_PRESALE("EOT_PRESALE", 2),
		EOT_PhoneOfAssistManager("EOT_PhoneOfAssistManage", 3);

		private String name;
		private int index;

		private EnumOperatorType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}
}
