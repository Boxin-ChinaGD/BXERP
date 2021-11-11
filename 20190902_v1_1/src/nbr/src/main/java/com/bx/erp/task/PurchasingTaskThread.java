package com.bx.erp.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderCommodityBO;
import com.bx.erp.action.wx.WxAction;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Staff;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.message.Message.EnumMessageCategory;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

import net.sf.json.JSONArray;

@Component("purchasingTaskThread")
@Scope("prototype")
public class PurchasingTaskThread extends TaskThread {
	private Log logger = LogFactory.getLog(PurchasingTaskThread.class);

	@Resource
	private PurchasingOrderCommodityBO purchasingOrderCommodityBO;

	@Resource
	private PurchasingOrderBO purchasingOrderBO;

	@Resource
	private ShopBO shopBO;

	@Resource
	private MessageBO messageBO;

	@Value("${wx.templateid.purchasingOrderOverTime}")
	private String WXMSG_TEMPLATEID_purchasingOrderOverTime;

	/** 为了使测试代码能够在任何时间驱动夜间任务，而不必真的等待到夜间才开始夜间任务，特设置本变量。 默认值为null。 <br />
	 * 1、值为null时，生成当前日期前一天的报表。这是用于功能代码的值，功能代码不能设置其它值！ <br />
	 * 2、为其它值时，生成reportDateForTest-1天的报表 */
	public static Date reportDateForTest;

	/** 将此值设置为true，夜间任务将无条件执行。运行一次后，如果不想再运行，可以设置为false */
	public static boolean runOnce;

	public Date getReportDateForTest() {
		return reportDateForTest;
	}

	public PurchasingTaskThread() {
		reportDateForTest = null;
		name = "采购单超时检查";
	}

	@Override
	protected Date getStartDatetime(String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral cgStart = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.PurchasingTimeoutTaskScanStartTime, BaseBO.SYSTEM, ecOut, dbName);
		return DatetimeUtil.getNextDayWithTime(new Date(), cgStart.getValue());
	}

	@Override
	public boolean canRunOnceForTest() {
		return runOnce;
	}

	@Override
	protected void doTaskForEveryCompany(Company company) {
		// logger.info(this.getName() + "--正在检查是否到达定时任务执行时期段（DB名称=" + dbName + "）...");

		String dbName = company.getDbName();
		logger.info("定时任务执行时间段已到，开始运行任务：查询当天" + name + "并推送 \t本线程hashcode=" + this.hashCode() + "\t 执行的dbName=" + dbName);
		// 找出已经审批的采购订单
		PurchasingOrder po = new PurchasingOrder();
		po.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex());
		DataSourceContextHolder.setDbName(dbName);
		List<?> listApproved = purchasingOrderBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, po);
		if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("夜间检查任务【"+ name + "】采购单查询异常 错误码=" + purchasingOrderBO.printErrorInfo(dbName, po));
			return;
		}
		// 找出部分入库的采购订单
		po.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex());
		DataSourceContextHolder.setDbName(dbName);
		List<?> listPartWarehousing = purchasingOrderBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, po);
		if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError || listPartWarehousing == null) {
			logger.error("夜间检查任务【"+ name + "】采购单查询异常 错误码=" + purchasingOrderBO.printErrorInfo(dbName, po));
			return;
		}
		// 判断采购单是否超时
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral cg = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.PurchasingTimeoutTaskFlag, BaseBO.SYSTEM, ecOut, dbName);
		for (Object obj : listApproved) {
			PurchasingOrder p = (PurchasingOrder) obj;
			if (p.getApproveDatetime() != null && DatetimeUtil.isAfterDate(new Date(), p.getApproveDatetime(), Integer.valueOf(cg.getValue()))) {
				// 采购超时时间是10天的倍数才会发消息，例如10、20、30
				int diffDays = (int) ((new Date().getTime() - p.getApproveDatetime().getTime()) / (1000 * 3600 * 24));
				if (diffDays % 10 == 0 && diffDays / 10 > 0) {
					if (!generateAndSendMessage(company, p, diffDays)) {
						return;// TODO 这里发送消息失败就不继续发送了，也可能继续发送下一条采购超时消息，以后再讨论
					}
				}
			}
		}
		for (Object obj : listPartWarehousing) {
			PurchasingOrder p = (PurchasingOrder) obj;
			if (p.getApproveDatetime() != null && DatetimeUtil.isAfterDate(new Date(), p.getApproveDatetime(), Integer.valueOf(cg.getValue()))) {
				// 采购超时时间是10天的倍数才会发消息，例如10、20、30
				int diffDays = (int) ((new Date().getTime() - p.getApproveDatetime().getTime()) / (1000 * 3600 * 24));
				if (diffDays % 10 == 0 && diffDays / 10 > 0) {
					if (!generateAndSendMessage(company, p, diffDays)) {
						return; // TODO 这里发送消息失败就不继续发送了，也可能继续发送下一条采购超时消息，以后再讨论
					}
				}
			}
		}
	}

	/** 生成消息在DB中，并发送消息给微信公众号 */
	private boolean generateAndSendMessage(Company company, PurchasingOrder purchasingOrder, int days) {
		ErrorInfo ei = new ErrorInfo();
		List<Staff> staffList = hasAtLeast1BossBindWx(ei, company.getDbName(), name);

		if (ei.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("夜间检查任务【" + name + "】创建消息，查询商家老板是否绑定微信时失败！错误信息：" + ei.getErrorMessage());
			return false;
		} else if (staffList == null) {
			logger.info("夜间检查任务【" + name + "】创建消息时失败；门店老板未绑定openid!");
			return false;
		} else {
			// ... 提供准确信息：哪个采购订单入货超时。同时处理重复消息的问题。
			Message m = new Message();
			m.setCategoryID(EnumMessageCategory.EMC_PurchasingTimeout.getIndex());
			m.setIsRead(0);
			m.setParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"采购超时\"}]");
			m.setSenderID(0);
			m.setReceiverID(1);
			m.setCompanyID(company.getID());
			DataSourceContextHolder.setDbName(company.getDbName());
			Message msg = (Message) messageBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, m);
			if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("夜间检查任务【" + name + "】创建消息异常" + messageBO.printErrorInfo(company.getDbName(), m));
				return false;// ...
			}
//			if (StringUtils.isEmpty(msg.getParameter())) {
//				logger.error("夜间检查任务【采购单超时检查】创建消息异常 ,创建的消息内容为空！！");
//				return false;
//			}
			logger.info("需要发送的消息： " + msg.getParameter());

			// 解析消息
			JSONArray jsonArray = JSONArray.fromObject(msg.getParameter());
			String purchasingTaskThreadMsg = (String) jsonArray.getJSONObject(1).get("Link1_Tag");

			boolean bSendWxMsgSuccess = false;
			for (Staff staffBoss : staffList) {
				if (staffBoss.getOpenid() != null && !"".equals(staffBoss.getOpenid())) {
					try {
						Map<String, Object> wxMap = WxAction.sendPurchasingOrderOverTimeMsg(PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL, GET_SENDTEMPLATEMSG_URL, purchasingOrderCommodityBO, days, company,
								purchasingOrder, purchasingTaskThreadMsg, staffBoss.getOpenid(), WXMSG_TEMPLATEID_purchasingOrderOverTime);
						if ((int) wxMap.get(BaseWxModel.WX_ERRCODE) == BaseWxModel.WX_ERRCODE_Success) {
							bSendWxMsgSuccess = true;
						} else {
							logger.error("夜间检查任务【" + name + "】向" + staffBoss + "发送消息失败！错误信息：" + wxMap.get(BaseWxModel.WX_ERRMSG));
						}
					} catch (Exception e) {
						logger.error("向门店老板发送采购订单入库超时的微信消息失败，错误信息=" + e.getMessage());
						return false;
					}
				}
			}

			if (bSendWxMsgSuccess) {
				DataSourceContextHolder.setDbName(company.getDbName());
				messageBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Message_UpdateStatus, msg);
				if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("更新消息的状态失败：" + messageBO.printErrorInfo(company.getDbName(), msg) + "。请OP手动更新本条消息的状态为已读");
					return false;
				}
			}
		}

		return true;
	}
}
