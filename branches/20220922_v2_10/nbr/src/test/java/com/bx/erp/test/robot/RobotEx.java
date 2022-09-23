package com.bx.erp.test.robot;

import java.util.Random;

import com.bx.erp.test.Shared;
import com.bx.erp.test.robot.program.CreateCommodityEx;
import com.bx.erp.test.robot.program.CreatePurchasingOrderEx;
import com.bx.erp.test.robot.program.CreateWarehousingEx;
import com.bx.erp.test.robot.program.ProgramEx.EnumProgramType;
import com.bx.erp.test.robot.server.RobotServerEx;
import com.bx.erp.test.robot.server.ServerHandlerEx;

public class RobotEx {

	/** 如果nbr活动有bug，为了单独测试nbr的活动不测试pos的活动, 不用运行pos端的测试 ，免去通信的时间花费 */
	public static boolean bRunNbrOnly = false;
	
	private final static int nbrProgram = 0;
	private final static int posProgram = 1;

	protected StringBuilder errorInfo;

	/** 实现随机CUD商品函数:随机时间、随机种类 */
	CreateCommodityEx createCommodityEx = new CreateCommodityEx();

	/** 实现随机创建采购订单，审核采购订单功能函数：随机时间、随机种类。
	 * 或从数据库中查找库存比较少的商品进行采购、或找到最近售卖的比较多的商品进行采购 */
	CreatePurchasingOrderEx createPurchasingOrder = new CreatePurchasingOrderEx();

	/** 实现随机创建入库单、审核入库单:全部入库、部分入库、不入库、跳库 */
	CreateWarehousingEx createWarehousingEx = new CreateWarehousingEx();

	/** 总活动数量 */
	protected int programNumber = 5800;
	
	protected int programNumberForOneDay = 20;
	
	protected int totolProgramNumber = 5800;

	public StringBuilder getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(StringBuilder errorInfo) {
		this.errorInfo = errorInfo;
	}

	//TODO 模拟多天上下班（先实现），还有夜间任务运行（后面再实现） 
	public boolean run() throws Exception {
		// 登录
		Shared.getHttpStaffLoginSession();

		Shared.caseLog("启动nbr机器人,等待客户端连接");
		RobotServerEx robotServerEx = new RobotServerEx();
		robotServerEx.startServer();
		if (!bRunNbrOnly) {
			Shared.caseLog("等待pos机器人连接");
			while (ConfigEx.TOTAL_CLIENT_Count > robotServerEx.getClientCount()) {
				Thread.sleep(500);
			}
		}
		while (programNumber > 0) {
			
			if(totolProgramNumber != programNumber && (totolProgramNumber - programNumber) % programNumberForOneDay == 0) {
				Shared.caseLog("通知pos机器人下班");
				sendMessageToPosForLogout(robotServerEx);
			}
			
			// 随机执行nbr或pos活动
			switch (new Random().nextInt(2)) {
			case nbrProgram:
				nbrProgram();
				break;
			case posProgram:
				posProgram(robotServerEx);
				break;
			default:
				break;
			}
		}
		// 通知pos关闭连接
		robotServerEx.sendMessageToPosForStop();
		
		if (!bRunNbrOnly) {
			Shared.caseLog("等待所有pos机器人断开连接");
			while (robotServerEx.getClientCount() > 0) {
				Thread.sleep(500);
			}
		}
		Shared.caseLog("nbr机器人关闭连接");
		return true;
	}


	private void nbrProgram() throws Exception {
		Shared.caseLog("nbr活动" + ",剩余活动数量" + --programNumber);
		// 随机商品、采购单、入库单活动
		switch (EnumProgramType.values()[new Random().nextInt(3)]) {
		case EPT_CreateCommodity:
			createCommodityEx.run();
			break;
		case EPT_CreatePurchasingOrder:
			createPurchasingOrder.run();
			break;
		case EPT_CreateWarehousing:
			createWarehousingEx.run();
			break;
		default:
			break;
		}
	}

	private void posProgram(RobotServerEx robotServerEx) throws InterruptedException {
		Shared.caseLog("pos活动" + ",剩余活动数量" + --programNumber);
		if (!bRunNbrOnly) {
			robotServerEx.sendMessageToPosForRun();
			while (!ServerHandlerEx.posDoneRun) {
				Thread.sleep(300);
			}
			ServerHandlerEx.posDoneRun = false;
		}
	}
	
	private void sendMessageToPosForLogout(RobotServerEx robotServerEx) throws InterruptedException {
		// TODO 自动生成的方法存根
		Shared.caseLog("pos活动" + ",剩余活动数量" + --programNumber);
		if (!bRunNbrOnly) {
			robotServerEx.sendMessageToPosForLogout();
			while (!ServerHandlerEx.posDoneRun) {
				Thread.sleep(300);
			}
			ServerHandlerEx.posDoneRun = false;
		}
	}
}
