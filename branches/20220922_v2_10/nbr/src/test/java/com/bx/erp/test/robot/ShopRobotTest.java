package com.bx.erp.test.robot;

import java.util.Calendar;
import java.util.Random;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class ShopRobotTest extends BaseTestNGSpringContextTest {
	public static boolean bStopServer = false;

//	public static volatile boolean bClientConnected = false;
//	public static IoSession clientSession;
//	public static volatile boolean bTimeToStopServer = false;

	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}

	// 早上8:00 boss上班
	// 开始做活动（采购，入库）
	// 晚上8:00 boss下班
	@Test(timeOut = 20000) // 不设置超时的话，Jenkins跑此测试时会永远无法退出！
	public void testShopRobot() throws Exception {
		// TODO 将来要重构这里，因为这里是非分布式机器人的代码
		int YEAR = 2018;
		int MONTH = new Random().nextInt(12) + 1;
		int DAY = new Random().nextInt(30) + 1;
		int StartHOUR = 8;
		int EndHOUR = 20;
		int MINUTE = 00;
		int SECOND = 00;
		boolean bRunInRandomMode = false; // 工作在特定模式下
		Calendar calStart = Calendar.getInstance();
		calStart.set(YEAR, MONTH - 1, DAY, StartHOUR, MINUTE, SECOND);
		Calendar calEnd = Calendar.getInstance();
		calEnd.set(YEAR, MONTH - 1, DAY + 5, EndHOUR, MINUTE, SECOND);
		//
		System.out.println("开店时间：" + YEAR + "-" + MONTH + "-" + DAY);
		Robot r = new Robot(calStart.getTime(), calEnd.getTime(), bRunInRandomMode);
		if (!r.run()) {
			System.out.println(r.getErrorInfo());
			Assert.assertFalse(false, "机器人运行出错！");
		}
	}
}
