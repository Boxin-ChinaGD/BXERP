package com.bx.erp.sit.sit1.sg.cms.cmsLogin;

import org.testng.annotations.Test;

import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;

import org.testng.annotations.BeforeClass;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class BxAdminLoginTest extends BaseTestNGSpringContextTest {

	protected AtomicInteger order;

	@Test
	public void createBXAccountAndLogin() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_bxAdminLogin_", order, "创建一个bx账号，再登录");

		// ...目前无CMS员工管理页面与接口
	}

	@Test(dependsOnMethods = "createBXAccountAndLogin")
	public void updateBXAccountAndLogin() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_bxAdminLogin_", order, "修改一个bx账号，再登录");
		
		// ...目前无CMS员工管理页面与接口
	}

	@Test(dependsOnMethods = "updateBXAccountAndLogin")
	public void deleteBXAccountAndLogin() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_bxAdminLogin_", order, "删除一个bx账号，再登录");

		// ...目前无CMS员工管理页面与接口
	}

	@Test(dependsOnMethods = "deleteBXAccountAndLogin")
	public void createBXAccountAndUpdateThenLogin() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_bxAdminLogin_", order, "创建一个bx账号，修改该账号，再登录");

		// ...目前无CMS员工管理页面与接口
	}

	@Test(dependsOnMethods = "createBXAccountAndUpdateThenLogin")
	public void createBXAccountAndDeleteThenLogin() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_bxAdminLogin_", order, "创建一个bx账号，删除该账号，再登录");

		// ...目前无CMS员工管理页面与接口
	}
	
	@Test(dependsOnMethods = "createBXAccountAndDeleteThenLogin")
	public void updateBXAccountAndDeleteThenLogin() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_bxAdminLogin_", order, "修改一个bx账号，删除该账号，再登录");

		// ...目前无CMS员工管理页面与接口
	}
	
	@Test(dependsOnMethods = "updateBXAccountAndDeleteThenLogin")
	public void createBXAccountAndUpdateThenDeleteAndLogin() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_bxAdminLogin_", order, "创建一个bx账号，修改该账号，再删除该账号，再登录");

		// ...目前无CMS员工管理页面与接口
	}
	
	@Test(dependsOnMethods = "createBXAccountAndUpdateThenDeleteAndLogin")
	public void updateLeftBXAccountThenLogin() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_bxAdminLogin_", order, "将一个离职的bx账号修改为在职，再登录");

		// ...目前无CMS员工管理页面与接口
	}
	
	@Test(dependsOnMethods = "updateLeftBXAccountThenLogin")
	public void useLocalComputerCURDbxAdminLoginThenUseOtherComputerLoginAndRetrieve() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_bxAdminLogin_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

		// ...目前无CMS员工管理页面与接口
	}
	
	@Test(dependsOnMethods = "useLocalComputerCURDbxAdminLoginThenUseOtherComputerLoginAndRetrieve")
	public void useTwoOrMoreComputerLoginAtTheSameTimeAndCURDbxAdminLogin() {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_bxAdminLogin_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

		// ...目前无CMS员工管理页面与接口
	}

	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();

		order = new AtomicInteger();
	}

	@AfterClass
	public void afterClass() {
	}

}
