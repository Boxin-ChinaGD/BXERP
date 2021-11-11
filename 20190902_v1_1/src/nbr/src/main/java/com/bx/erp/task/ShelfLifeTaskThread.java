package com.bx.erp.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumEnv;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.RoleBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.bo.message.MessageItemBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.action.bo.warehousing.WarehousingCommodityBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.message.MessageItem;
import com.bx.erp.model.message.Message.EnumMessageCategory;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

@Component("shelfLifeTaskThread")
@Scope("prototype")
public class ShelfLifeTaskThread extends TaskThread {
	private Log logger = LogFactory.getLog(ShelfLifeTaskThread.class);

	@Resource
	private WarehousingBO warehousingBO;

	@Resource
	private ShopBO shopBO;

	@Resource
	private WarehousingCommodityBO warehousingCommodityBO;

	@Resource
	private CommodityBO commodityBO;
	@Resource
	private MessageBO messageBO;
	@Resource
	private RoleBO roleBO;

	@Resource
	private MessageItemBO messageItemBO;

	/** 为了使测试代码能够在任何时间驱动夜间任务，而不必真的等待到夜间才开始夜间任务，特设置本变量。 默认值为null。 <br />
	 * 1、值为null时，生成当前日期前一天的报表。这是用于功能代码的值，功能代码不能设置其它值！ <br />
	 * 2、为其它值时，生成reportDateForTest-1天的报表 */
	public static Date reportDateForTest;

	/** 将此值设置为true，夜间任务将无条件执行。运行一次后，如果不想再运行，可以设置为false */
	public static boolean runOnce;

	public Date getReportDateForTest() {
		return reportDateForTest;
	}

	public ShelfLifeTaskThread() {
		reportDateForTest = null;
		name = "商品保质期检查";
	}

	@Override
	protected Date getStartDatetime(String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral cgStart = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.ShelfLifeTaskScanStartTime, BaseBO.SYSTEM, ecOut, dbName);
		return DatetimeUtil.getNextDayWithTime(new Date(), cgStart.getValue());
	}

	@Override
	public boolean canRunOnceForTest() {
		return runOnce;
	}

	/** 1、如果是服务型商品，不用保质期 2、库存>0。 3、组合型商品不用检查。 4、商品可能已经被删除。 */
	@Override
	protected void doTaskForEveryCompany(Company company) {
		String dbName = company.getDbName();
		logger.info("定时任务执行时间段已到，开始运行任务：查询当天" + name + "并推送 \t本线程hashcode=" + this.hashCode() + "\t 执行的dbName=" + dbName);
		boolean bTimeout = false;
		List<Commodity> commList = new ArrayList<Commodity>();
		Warehousing warehousing = new Warehousing();
		warehousing.setPurchasingOrderID(BaseAction.INVALID_ID);
		DataSourceContextHolder.setDbName(dbName);
		List<?> wsList = warehousingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || wsList == null) {
			logger.error("夜间检查任务【" + name + "】入库单查询异常，错误码=" + warehousingBO.getLastErrorCode() + "错误信息=" + warehousingBO.getLastErrorMessage());
			return;
		}
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral cg = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.ShelfLifeTaskFlag, BaseBO.SYSTEM, ecOut, dbName);

		// ...以后优化成根据某段时间查询，因为已经查过的时间段，可能不需要再查
		for (Object obj : wsList) { // 遍历所有入库单，拿到每个入库单的所有商品
			Warehousing w = (Warehousing) obj;
			WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
			warehousingCommodity.setWarehousingID(w.getID());
			//
			DataSourceContextHolder.setDbName(dbName);
			List<?> wcList = warehousingCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousingCommodity);
			if (warehousingCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError || wcList == null) {
				logger.error("夜间检查任务【" + name + "】入库商品查询异常 错误码=" + warehousingCommodityBO.getLastErrorCode() + "错误信息=" + warehousingCommodityBO.getLastErrorMessage());
				return;
			}

			for (Object obj2 : wcList) { // 遍历所有入库单商品，检查商品库存和是否过期
				WarehousingCommodity wc = (WarehousingCommodity) obj2;

				// ... TODO 从缓存中拿商品，不仅扰乱了原来的缓存，还会多此一举。将来重构成直接用BO向DB中拿。
				// Commodity c = (Commodity)
				// CacheManager.getCache(dbName,EnumCacheType.ECT_Commodity).read1(wc.getCommodityID(),
				// ecOut);

				Commodity comm = new Commodity();
				comm.setID(wc.getCommodityID());
				DataSourceContextHolder.setDbName(dbName);
				List<List<BaseModel>> commR1 = commodityBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, comm);
				if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("夜间检查任务,入库商品查询单个商品异常。" + commodityBO.printErrorInfo(dbName, comm));
					continue;
				}
				comm = Commodity.fetchCommodityFromResultSet(commR1);
				if (comm == null || comm.getNO() <= 0) {
					logger.info("夜间检查任务，入库商品查询单个商品异常。商品为空或者商品数量<=0. comm=" + comm);
					continue;
				}
				//
				if (!DatetimeUtil.isAfterDate(new Date(), wc.getExpireDatetime(), Integer.valueOf(cg.getValue()))) {
					commList.add(comm);
					bTimeout = true;
				}
			}
		}

		// 商品保质期提醒消息不用发送
		if (bTimeout) {
			if (!handleTimeout(dbName, ecOut, commList)) {
				return;
			}
		}

	}

	private boolean handleTimeout(String dbName, ErrorInfo ecOut, List<Commodity> commList) {
		logger.info("商品过期：" + commList);

		ErrorInfo ei = new ErrorInfo();
		List<Staff> staffList = hasAtLeast1BossBindWx(ei, dbName, name);

		if (ei.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("夜间检查任务【" + name + "】创建消息，查询商家老板是否绑定微信失败！错误信息：" + ei.getErrorMessage());
			return false;
		} else if (staffList == null) {
			logger.info("夜间检查任务【" + name + "】创建消息时失败；门店老板未绑定openid!");
			return false;
		} else {
			if (staffList.get(0) == null || staffList.get(0).getOpenid() == null) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.info("夜间检查任务【" + name + "】创建消息时失败,用户未绑定openid或未查询到门店老板的Staff!");
				}
				return false;
			}
			logger.info("查询到的门店老板Staff:" + staffList.get(0));

			Shop shop = (Shop) CacheManager.getCache(dbName, EnumCacheType.ECT_Shop).read1(staffList.get(0).getShopID(), BaseBO.SYSTEM, ecOut, dbName);
			if (shop == null) {
				logger.error("夜间检查任务【" + name + "】创建消息时失败:从缓存中读取shop,失败！查询的shopID为：" + staffList.get(0).getShopID());
				return false;
			}
			// ...将来要将过期的商品列表给用户看。另外再处理重复消息的问题。
			Message m = new Message();
			m.setCategoryID(EnumMessageCategory.EMC_ApproachingShelfLife.getIndex());
			m.setIsRead(0);
			m.setParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"有商品已过保质期:\"}]");
			m.setSenderID(0);
			m.setReceiverID(1);
			m.setCompanyID(shop.getCompanyID());
			DataSourceContextHolder.setDbName(dbName);
			Message msg = (Message) messageBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, m);
			if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("夜间检查任务【" + name + "】创建消息异常 错误码=" + messageBO.getLastErrorCode() + messageBO.getLastErrorMessage());
				return false;
			}

			if (msg.getParameter() == null || msg.getParameter().equals("")) {
				logger.error("夜间检查任务【" + name + "】创建消息内容异常,创建的消息内容为空！！");
				return false;
			}

			logger.info("需要发送的消息：" + msg.getParameter());
			// 解析消息
			// JSONArray jsonArray = JSONArray.fromObject(msg.getParameter());
			// String purchasingTaskThreadMsg = (String)
			// jsonArray.getJSONObject(1).get("Link1_Tag");

			// 将保质商品保存到消息数据项表t_messageItem中
			MessageItem messageItem = new MessageItem();
			for (Commodity commodity : commList) {
				messageItem.setMessageID(msg.getID());
				messageItem.setMessageCategoryID(msg.getCategoryID());
				messageItem.setCommodityID(commodity.getID());
				DataSourceContextHolder.setDbName(dbName);
				messageItemBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, messageItem);
				if (messageItemBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("夜间检查任务【" + name + "】创建消息项异常," + messageItemBO.printErrorInfo(dbName, messageItem));
					return false;
				}
			}

			// try {
			// Map<String, Object> wxMap = wxAction.sendTemplateMsg(purchasingTaskThreadMsg,
			// staffList.get(0).getOpenid(), FOLLOWSUCCESS);//
			// ...先写死一个帐号。这里应该是这个公司的老板的openid
			// if ((int) wxMap.get(BaseWX.WX_ERRCODE) == 0) {
			// DataSourceContextHolder.setDbName(dbName);
			// messageBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Message_UpdateStatus, msg);
			// if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			// logger.error("更新Message的staus失败! 该Message的ID为:" + msg.getID());
			// return false;
			// }
			// } else {
			// logger.error("夜间检查任务【采购单超时检查】发送消息失败！错误信息：" + wxMap.get(BaseWX.WX_ERRMSG));
			// return false;
			// }
			// } catch (ClientProtocolException e) {
			// logger.error(e.getMessage());
			// return false;
			// } catch (IOException e) {
			// logger.error(e.getMessage());
			// return false;
			// }
		}

		return true;
	}
}
