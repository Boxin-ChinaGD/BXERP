package com.bx.erp.task;

import java.lang.Thread.State;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.model.TaskType.EnumTaskType;

@Component("taskScheduler")
@Scope("prototype")
public class TaskScheduler {
	private Log logger = LogFactory.getLog(TaskScheduler.class);

	private AtomicInteger aiThreadSignal;

	@Resource
	private PurchasingTaskThread ptt;

	@Resource
	private ShelfLifeTaskThread stt;

	@Resource
	private UnsalableCommodityTaskThread uc;

	@Resource
	private RetailTradeDailyReportSummaryTaskThread dr;

	@Resource
	private RetailTradeMonthlyReportSummaryTaskThread dm;

	@Resource
	private RetailTradeDailyReportByCategoryParentTaskThread drc;

	@Resource
	private RetailTradeDailyReportByStaffTaskThread ds;

	@Resource
	private BonusTaskThread btt;

	public TaskScheduler() {
		aiThreadSignal = new AtomicInteger();
	}

	public void start() {
		TaskThread tt = TaskManager.getCache(EnumTaskType.ETT_PurchasingTimeout);
		if (tt != null && tt.isAlive() && tt.getState() != State.TERMINATED) {
			logger.info("旧线程" + tt.getName() + "仍未退出！Hashcode=" + tt.hashCode());
		} else {
			TaskManager.register(EnumTaskType.ETT_PurchasingTimeout, ptt);
			ptt.setAtomicInteger(aiThreadSignal);
			ptt.setName("采购超时定时检查线程");
			ptt.start();
			logger.info("线程" + ptt.getName() + "已经启动。Hashcode=" + ptt.hashCode());
		}

		tt = TaskManager.getCache(EnumTaskType.ETT_ShelfLifeTaskThread);
		if (tt != null && tt.isAlive() && tt.getState() != State.TERMINATED) {
			logger.info("旧线程" + tt.getName() + "仍未退出！Hashcode=" + tt.hashCode());
		} else {
			TaskManager.register(EnumTaskType.ETT_ShelfLifeTaskThread, stt);
			stt.setAtomicInteger(aiThreadSignal);
			stt.setName("商品保质期定时检查线程");
			stt.start();
			logger.info("线程" + stt.getName() + "已经启动。Hashcode=" + stt.hashCode());
		}

		tt = TaskManager.getCache(EnumTaskType.ETT_UnsalableCommodity);
		if (tt != null && tt.isAlive() && tt.getState() != State.TERMINATED) {
			logger.info("旧线程" + tt.getName() + "仍未退出！Hashcode=" + tt.hashCode());
		} else {
			TaskManager.register(EnumTaskType.ETT_UnsalableCommodity, uc);
			uc.setAtomicInteger(aiThreadSignal);
			uc.setName("商品滞销定时检查线程");
			uc.start();
			logger.info("线程" + uc.getName() + "已经启动。Hashcode=" + uc.hashCode());
		}

		tt = TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread);
		if (tt != null && tt.isAlive() && tt.getState() != State.TERMINATED) {
			logger.info("旧线程" + tt.getName() + "仍未退出！Hashcode=" + tt.hashCode());
		} else {
			TaskManager.register(EnumTaskType.ETT_RetailTradeDailyReportSummaryTaskThread, dr);
			dr.setAtomicInteger(aiThreadSignal);
			dr.setName("日报表定时推送检查线程");
			dr.start();
			logger.info("线程" + dr.getName() + "已经启动。Hashcode=" + dr.hashCode());
		}

		tt = TaskManager.getCache(EnumTaskType.ETT_RetailTradeMonthlyReportSummaryTaskThread);
		if (tt != null && tt.isAlive() && tt.getState() != State.TERMINATED) {
			logger.info("旧线程" + tt.getName() + "仍未退出！Hashcode=" + tt.hashCode());
		} else {
			TaskManager.register(EnumTaskType.ETT_RetailTradeMonthlyReportSummaryTaskThread, dm);
			dm.setAtomicInteger(aiThreadSignal);
			dm.setName("月报表定时推送检查线程");
			dm.start();
			logger.info("线程" + dm.getName() + "已经启动。Hashcode=" + dm.hashCode());
		}

		tt = TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportByCategoryParentTaskThread);
		if (tt != null && tt.isAlive() && tt.getState() != State.TERMINATED) {
			logger.info("旧线程" + tt.getName() + "仍未退出！Hashcode=" + tt.hashCode());
		} else {
			TaskManager.register(EnumTaskType.ETT_RetailTradeDailyReportByCategoryParentTaskThread, drc);
			drc.setAtomicInteger(aiThreadSignal);
			drc.setName("日大类报表定时推送检查线程");
			drc.start();
			logger.info("线程" + drc.getName() + "已经启动。Hashcode=" + drc.hashCode());
		}

		tt = TaskManager.getCache(EnumTaskType.ETT_RetailTradeDailyReportByStaffTaskThread);
		if (tt != null && tt.isAlive() && tt.getState() != State.TERMINATED) {
			logger.info("旧线程" + tt.getName() + "仍未退出！Hashcode=" + tt.hashCode());
		} else {
			TaskManager.register(EnumTaskType.ETT_RetailTradeDailyReportByStaffTaskThread, ds);
			ds.setAtomicInteger(aiThreadSignal);
			ds.setName("员工业绩报表定时推送检查线程");
			ds.start();
			logger.info("线程" + ds.getName() + "已经启动。Hashcode=" + ds.hashCode());
		}

		tt = TaskManager.getCache(EnumTaskType.ETT_BonusTaskThread);
		if (tt != null && tt.isAlive() && tt.getState() != State.TERMINATED) {
			logger.info("旧线程" + tt.getName() + "仍未退出！Hashcode=" + tt.hashCode());
		} else {
			TaskManager.register(EnumTaskType.ETT_BonusTaskThread, btt);
			btt.setAtomicInteger(aiThreadSignal);
			btt.setName("积分清零检查线程");
			btt.start();
			logger.info("线程" + btt.getName() + "已经启动。Hashcode=" + btt.hashCode());
		}

		TaskManager.setTaskScheduler(this);
	}

	/** 为TestNG测试而设的变量或函数，不能在非测试代码中调用！！！！！ */
	// public void StopAllTaskInTest() {
	// stop();
	// }
	
	/** 为TestNG测试而设的变量或函数，不能在非测试代码中调用！！！！！ */
	public void setMockingMVC(boolean isMocking) {
		ptt.setMockingMVC(isMocking);
		stt.setMockingMVC(isMocking);
		uc.setMockingMVC(isMocking);
		dr.setMockingMVC(isMocking);
		dm.setMockingMVC(isMocking);
		drc.setMockingMVC(isMocking);
		ds.setMockingMVC(isMocking);
		btt.setMockingMVC(isMocking);
	}

	public void stop() {
		aiThreadSignal.set(TaskThread.SIGNAL_ThreadExit);
		try {
			ptt.join();
			logger.info("线程" + ptt.getName() + "已经结束。Hashcode=" + ptt.hashCode());
			stt.join();
			logger.info("线程" + stt.getName() + "已经结束。Hashcode=" + stt.hashCode());
			uc.join();
			logger.info("线程" + uc.getName() + "已经结束。Hashcode=" + uc.hashCode());
			dr.join();
			logger.info("线程" + dr.getName() + "已经结束。Hashcode=" + dr.hashCode());
			dm.join();
			logger.info("线程" + dm.getName() + "已经结束。Hashcode=" + dm.hashCode());
			drc.join();
			logger.info("线程" + drc.getName() + "已经结束。Hashcode=" + drc.hashCode());
			ds.join();
			logger.info("线程" + ds.getName() + "已经结束。Hashcode=" + ds.hashCode());
			btt.join();
			logger.info("线程" + btt.getName() + "已经结束。Hashcode=" + btt.hashCode());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
