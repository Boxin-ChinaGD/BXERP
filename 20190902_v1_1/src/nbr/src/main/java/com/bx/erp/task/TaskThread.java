package com.bx.erp.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.RoleBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Role;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Company.EnumCompanyCreationStatus;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

@Component("taskThread")
@Scope("prototype")
public class TaskThread extends Thread {
	private Log logger = LogFactory.getLog(TaskThread.class);

	public final static int SIGNAL_ThreadExit = 1;
	
	@Value("${public.account.appid}")
	protected String PUBLIC_ACCOUNT_APPID;

	@Value("${public.account.secret}")
	protected String PUBLIC_ACCOUNT_SECRET;

	@Value("${get.accesstoken.url}")
	protected String GET_ACCESSTOKEN_URL; // 获取访问微信端的token接口

	@Value("${get.sendTemplateMsg.url}")
	protected String GET_SENDTEMPLATEMSG_URL; // 发送模板消息的接口

	@Resource
	private RoleBO roleBO;

	/** 当前夜间任务的名称 */
	protected String name;
	/** 下一次跑本任务的时间。Key=公司名称，Value=下一次跑任务的时间 */
	protected Map<String, Date> mapDatetimeForNextRun;
	/** 在生成报表前，设置是否删除日期为reportDateForTest-1的报表 */
	public static int deleteReportOfDateRun = EnumBoolean.EB_NO.getIndex();

	/** 为测试而设的变量。<br />
	 * 当到达任务设定的时间时，本线程会执行既定任务，然后将本变量加1，再sleep一段时间，等待下一次执行。 <br />
	 * 在这段时间内，自动化测试需要知道任务的执行状态以便判断执行的结果，同时可以在线程的空闲时间内执行某些检查。 */
	private AtomicInteger taskStatusForTest;

	public AtomicInteger getTaskStatusForTest() {
		return taskStatusForTest;
	}

	public Date getReportDateForTest() {
		throw new RuntimeException("Not yet implemented!");
	}

	public void setTaskStatusForTest(AtomicInteger taskStatusForTest) {
		this.taskStatusForTest = taskStatusForTest;
	}

	/** 线程退出的标志/Signal */
	private AtomicInteger atomicInteger;

	/** 有的测试代码，需要让夜间任务执行一次。执行一次后，不再执行。可以重写本函数 */
	public boolean canRunOnceForTest() {
		return false;
	}

	public void setAtomicInteger(AtomicInteger atomicInteger) {
		this.atomicInteger = atomicInteger;
	}

	public TaskThread() {
		taskStatusForTest = new AtomicInteger();
		mapDatetimeForNextRun = new HashMap<String, Date>();
	}

	/** 当本变量设置为true时，表明当前环境是假的MVC环境，即TestNG测试环境 */
	protected boolean isMockingMVC = false;
	
	public void setMockingMVC(boolean isMockingMVC) {
		this.isMockingMVC = isMockingMVC;
	}
	
	public int timeSpanToCheckTaskStartTime = 10000; 

	public int getTimeSpanToCheckTaskStartTime() {
		return timeSpanToCheckTaskStartTime;
	}

	@Override
	public void run() {
		while (atomicInteger.get() != SIGNAL_ThreadExit) {
			doTask();
			try {
				if (isMockingMVC) {
					timeSpanToCheckTaskStartTime = 50; // 在测试环境下，检查频繁一点，以缩短报表测试的运行时间
				} else {
					timeSpanToCheckTaskStartTime = 10000; // 检查疏一点，以免占用太多CPU时间
				}
				Thread.sleep(timeSpanToCheckTaskStartTime);  
			} catch (InterruptedException e) {
				logger.error("夜间检查任务【" + name + "】定时任务线程异常" + e.getMessage());
				return;
			}
		}
	}

	/** 获取任务开始的时间。此时间是T_ConfigGeneral中配置的时间（如10:10:10）加上明天的日期部分 <br />
	 * （如2019/9/21 10:10:10） 。当当前时间超过这个时间时，就跑当前的夜间任务 */
	protected Date getStartDatetime(String dbName) {
		throw new RuntimeException("Not yet implemented!");
	}

	protected void doTask() {
		List<BaseModel> list = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(false, false);
		for (BaseModel bm : list) {
			Company company = (Company) bm;
			if (!company.getDbName().equals(BaseAction.DBName_Public)) {// 公共DB没有夜间任务
				if (company.getIncumbent() == EnumCompanyCreationStatus.ECCS_Incumbent.getIndex() && company.getStatus() == EnumCompanyCreationStatus.ECCS_Incumbent.getIndex()) {// 表示创建公司时是否已经创建完成
					Date dateToRunReport = (getReportDateForTest() == null ? new Date() : getReportDateForTest());
					if (mapDatetimeForNextRun.get(company.getDbName()) == null) {
						mapDatetimeForNextRun.put(company.getDbName(), getStartDatetime(company.getDbName()));// 有的公司是后来创建的
					}
					// 如果时间已经超过本夜间任务设定的运行时间，则设置此时间为24小时后再跑一次，然后才跑夜间任务。子类中，可以自定义夜间任务跑或不跑、如何跑。
					if (DatetimeUtil.isAfterDate(dateToRunReport, mapDatetimeForNextRun.get(company.getDbName()), 0) || canRunOnceForTest()) {
						logger.info("定时任务执行时间段已到，开始运行任务, \t本线程hashcode=" + this.hashCode());
						Date dateToRunNextTime = mapDatetimeForNextRun.get(company.getDbName());
						dateToRunNextTime = DatetimeUtil.getDate(dateToRunNextTime, 24 * 3600);
						mapDatetimeForNextRun.put(company.getDbName(), dateToRunNextTime);
						doTaskForEveryCompany(company);
						taskStatusForTest.incrementAndGet();
					}
				}
			}
		}
	}

	protected void doTaskForEveryCompany(Company bm) {
		throw new RuntimeException("尚未实现的函数！");
	}

	/** 检查是否至少有1个老板绑定了微信。如果有，后面发送微信公众号消息给他 */
	protected List<Staff> hasAtLeast1BossBindWx(ErrorInfo ei, String dbName, String taskName) {
		ei.setErrorCode(EnumErrorCode.EC_NoError);

		Role role = new Role();
		role.setID(EnumTypeRole.ETR_Boss.getIndex()); // 查询门店老板
		DataSourceContextHolder.setDbName(dbName);
		List<?> list = roleBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_Role_RetrieveAlsoStaff, role);
		if (roleBO.getLastErrorCode() != EnumErrorCode.EC_NoError || list.get(1) == null) {
			logger.error("夜间检查任务【" + taskName + "】创建消息时失败；查询门店老板失败或老板未绑定微信公众号！");
			ei.setErrorCode(EnumErrorCode.EC_OtherError);
			ei.setErrorMessage(roleBO.getLastErrorMessage());
			return null;
		}

		List<Staff> staffList = (List<Staff>) list.get(1);
		for (Staff staffBoss : staffList) {
			if (staffBoss.getOpenid() != null) { // ... 没处理以后多门店的问题
				return staffList;
			} else {
				logger.debug("此老板未绑定微信，忽略发送给" + staffBoss);
			}
		}

		return null;
	}

}
