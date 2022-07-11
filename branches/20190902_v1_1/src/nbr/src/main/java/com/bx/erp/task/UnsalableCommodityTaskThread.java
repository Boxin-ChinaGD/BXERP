package com.bx.erp.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.RetailTradeBO;
import com.bx.erp.action.bo.RetailTradeCommodityBO;
import com.bx.erp.action.bo.RoleBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.bo.message.MessageItemBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.action.bo.warehousing.WarehousingCommodityBO;
import com.bx.erp.action.wx.WxAction;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Staff;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.message.Message.EnumMessageCategory;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

@Component("unsalableCommodityTaskThread")
@Scope("prototype")
public class UnsalableCommodityTaskThread extends TaskThread {
	private Log logger = LogFactory.getLog(UnsalableCommodityTaskThread.class);

	@Resource
	private WarehousingBO warehousingBO;

	@Resource
	private WarehousingCommodityBO warehousingCommodityBO;

	@Resource
	private RetailTradeBO retailTradeBO;

	@Resource
	private RetailTradeCommodityBO retailTradeCommodityBO;

	@Resource
	private CommodityBO commodityBO;

	@Resource
	private ShopBO shopBO;

	@Resource
	private MessageBO messageBO;

	@Resource
	private RoleBO roleBO;

	@Value("${wx.templateid.unsalableCommodity}")
	private String WXMSG_TEMPLATEID_UnsalableCommodity;

	@Resource
	private MessageItemBO messageItemBO;

	/** 不包含退货型零售单 */
	public static final int EXCLUDE_Returned_RetailTrade = 1;

	/** 为了使测试代码能够在任何时间驱动夜间任务，而不必真的等待到夜间才开始夜间任务，特设置本变量。 默认值为null。 <br />
	 * 1、值为null时，生成当前日期前一天的报表。这是用于功能代码的值，功能代码不能设置其它值！ <br />
	 * 2、为其它值时，生成reportDateForTest-1天的报表 */
	public static Date reportDateForTest;

	/** 将此值设置为true，夜间任务将无条件执行。运行一次后，如果不想再运行，可以设置为false */
	public static boolean runOnce;

	public Date getReportDateForTest() {
		return reportDateForTest;
	}

	public UnsalableCommodityTaskThread() {
		reportDateForTest = null;
		name = "商品滞销检查";
	}

	@Override
	protected Date getStartDatetime(String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral cgStart = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.UsalableCommodityTaskScanStartTime, BaseBO.SYSTEM, ecOut, dbName);
		return DatetimeUtil.getNextDayWithTime(new Date(), cgStart.getValue());
	}

	@Override
	public boolean canRunOnceForTest() {
		return runOnce;
	}

	@Override
	protected void doTaskForEveryCompany(Company company) {
		String dbName = company.getDbName();
		if (DatetimeUtil.isOnSpecialDay(new Date(), 1)) { // 只在每月1号发送滞销消息，并不是天天发
			logger.info("定时任务执行时间段已到，开始运行任务：" + name + "并推送 \t本线程hashcode=" + this.hashCode() + "\t 执行的dbName=" + dbName);
			ErrorInfo ecOut = new ErrorInfo();
			ConfigGeneral cg = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.UsalableCommodityTaskFlag, BaseBO.SYSTEM, ecOut, dbName);
			// 查询滞销商品
			Commodity unsalableCommRN = new Commodity();
			Date nowDate = new Date();
			Calendar calStart = Calendar.getInstance();
			calStart.setTime(nowDate);
			calStart.add(Calendar.DATE, Integer.valueOf(cg.getValue()) * -1); // 开始时间为现在时间的前30天
			Date startDatetime = calStart.getTime();
			unsalableCommRN.setDate1(startDatetime);
			unsalableCommRN.setDate2(nowDate);
			unsalableCommRN.setMessageIsRead(0);
			unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
			unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
			unsalableCommRN.setCompanyID(company.getID());
			unsalableCommRN.setMessageSenderID(0);
			unsalableCommRN.setMessageReceiverID(1);
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> bmList = commodityBO.retrieveNObjectEx(BaseBO.SYSTEM, BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
			if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询滞销商品失败," + commodityBO.printErrorInfo(dbName, unsalableCommRN));
				return;
			}
			List<BaseModel> commList = bmList.get(0);
			Set<Commodity> setCommodity = new HashSet<Commodity>();
			for (BaseModel bm : commList) {
				Commodity comm = (Commodity) bm;
				setCommodity.add(comm);
			}
			Message unSalableCommMsg = (Message) bmList.get(1).get(0);
			if (setCommodity.size() > 0 && unSalableCommMsg != null) {
				if (!sendWxUnsalableCommodityToBoss(dbName, ecOut, setCommodity, unSalableCommMsg)) {
					return;
				}
			}
		}
	}

	private boolean sendWxUnsalableCommodityToBoss(String dbName, ErrorInfo ecOut, Set<Commodity> setCommodity, Message unSalableCommMsg) {
		logger.info("滞销的商品有：" + setCommodity);

		ErrorInfo ei = new ErrorInfo();
		List<Staff> staffList = hasAtLeast1BossBindWx(ei, dbName, name);

		if (ei.getErrorCode() != EnumErrorCode.EC_NoError) {
			 logger.info("夜间检查任务【" + name + "】创建消息，查询老板是否绑定微信号时失败！错误信息：" + ei.getErrorMessage());
			return false;
		} else if (staffList == null) {
			logger.info("夜间检查任务【" + name + "】创建消息时失败；门店老板未绑定openid!");
			return false;
		} else {
			boolean bSendWxMsgSuccess = false;
			// List<Staff> staffList = (List<Staff>) list.get(1);
			for (Staff staffBoss : staffList) {
				if (staffBoss.getOpenid() != null || !"".equals(staffBoss.getOpenid())) { // ... 没处理以后多门店的问题
					try {
						Map<String, Object> wxMap = WxAction.sendUnsalableCommodityMsg(PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL, GET_SENDTEMPLATEMSG_URL, setCommodity, unSalableCommMsg, staffBoss.getOpenid(),
								WXMSG_TEMPLATEID_UnsalableCommodity);
						if ((int) wxMap.get(BaseWxModel.WX_ERRCODE) == BaseWxModel.WX_ERRCODE_Success) {
							bSendWxMsgSuccess = true;
						} else {
							logger.error("夜间检查任务【" + name + "】发送消息失败！错误信息：" + wxMap.get(BaseWxModel.WX_ERRMSG));
							return false; // ... 这里是继续执行下一个发送消息还是return
						}
					} catch (Exception e) {
						logger.error("向门店老板发送商品滞销微信消息失败，错误信息=" + e.getMessage());
						return false;
					}
				} else {
					logger.debug("此老板未绑定微信，忽略发送给" + staffBoss);
				}
			}

			if (bSendWxMsgSuccess) {
				DataSourceContextHolder.setDbName(dbName);
				messageBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Message_UpdateStatus, unSalableCommMsg);
				if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("更新消息的状态失败：" + messageBO.printErrorInfo() + "。请OP手动更新本条消息的状态为已读。消息ID=" + unSalableCommMsg.getID());
					return false;
				}
			}
		}

		return true;
	}
}
