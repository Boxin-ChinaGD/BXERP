package com.bx.erp.test.robot;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;

public class ShopRobotTestEx extends BaseTestNGSpringContextTest {
	
	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}
	
	@Test(timeOut = 20000)
	public void testShopRobotEx() throws Exception {
		
		RobotEx robotEx = new RobotEx();
		if (!robotEx.run()) {
			System.out.println(robotEx.getErrorInfo());
			Assert.assertFalse(false, "随机机器人运行出错！");
		}
	}
}
