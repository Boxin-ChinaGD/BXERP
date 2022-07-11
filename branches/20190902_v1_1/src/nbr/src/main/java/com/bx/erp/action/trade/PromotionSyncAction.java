package com.bx.erp.action.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseSyncAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.trade.PromotionBO;
import com.bx.erp.action.bo.trade.PromotionScopeBO;
import com.bx.erp.action.bo.trade.PromotionShopScopeBO;
import com.bx.erp.action.bo.trade.PromotionSyncCacheBO;
import com.bx.erp.action.bo.trade.PromotionSyncCacheDispatcherBO;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionShopScope;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Shop;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.Commodity.EnumStatusCommodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.PromotionShopScope;
import com.bx.erp.model.trade.PromotionSyncCache;
import com.bx.erp.model.trade.PromotionSyncCacheDispatcher;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/promotionSync")
@Scope("prototype")
public class PromotionSyncAction extends BaseSyncAction {
	private Log logger = LogFactory.getLog(PromotionSyncAction.class);

	@Override
	protected int getSequence() {
		return aiSequenceSyncBlock.getAndIncrement();
	}

	/** 不断自增，标记同步块在客户端同步的顺序。<br />
	 * 客户端收到N个同步块后，先排序，再逐个将这些块写入客户端的DB。<br />
	 * 多线程安全问题：本变量不需要加锁，因为每个ACTION都是互斥运行的。<br />
	 * 缺陷：理论上存在过大溢出的bug。实际上在产品的迭代过程中，不可能会有机会溢出 */
	protected static AtomicInteger aiSequenceSyncBlock = new AtomicInteger();

	@Resource
	protected com.bx.erp.cache.trade.promotion.PromotionSyncCache psc;

	@Resource
	protected PromotionSyncCacheBO promotionSyncCacheBO;

	@Resource
	protected PromotionSyncCacheDispatcherBO promotionSyncCacheDispatcherBO;

	@Resource
	protected PromotionBO promotionBO;

	@Resource
	private PromotionScopeBO promotionScopeBO;
	
	@Resource
	private PromotionShopScopeBO promotionShopScopeBO;

	@Resource
	protected CommodityBO commodityBO;

	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	protected BaseBO getSyncCacheBO() {
		return promotionSyncCacheBO;
	}

	protected SyncCache getSyncCache() {
		return psc;
	}

	@Override
	protected BaseModel getModel() {
		return new Promotion();
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return promotionSyncCacheDispatcherBO;
	}

	@Override
	protected BaseBO getModelBO() {
		return promotionBO;
	}

	@Override
	protected EnumCacheType getCacheType() {
		return EnumCacheType.ECT_Promotion;
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_PromotionSyncInfo;
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new PromotionSyncCacheDispatcher();
	}

	@Override
	protected BaseSyncCache getSyncCacheModel() {
		return new PromotionSyncCache();
	}

	@Override
	protected EnumErrorCode deleteSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		return EnumErrorCode.EC_NoError;// Promotion没有从表数据
	}

	@Override
	protected EnumErrorCode createSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		Promotion bm = (Promotion) bmMasterFromDB;
		if (bm.getScope() == 1 && bm.getCommodityIDs() != null) {
			Integer[] iArrCommID = GeneralUtil.toIntArray(bm.getCommodityIDs());
			if (iArrCommID == null) {
				logger.info("参数缺失");
				return EnumErrorCode.EC_BusinessLogicNotDefined;
			} else {
				logger.info("iArrCommID=" + iArrCommID);
			}
			// ... 在调用handleCreateEx()前就已经做了检查商品的操作了。
			// 判断商品是否全部是普通商品，如果不是，则返回EnumErrorCode.EC_BusinessLogicNotDefined，意为不支持的业务操作
			Commodity commodity = new Commodity();
			ErrorInfo errorInfo = new ErrorInfo();
			for (int i = 0; i < iArrCommID.length; i++) {
				commodity.setID(iArrCommID[i]);
				Commodity commodityRetrieve = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodity.getID(), getStaffFromSession(req.getSession()).getID(), errorInfo, dbName);
				if (errorInfo.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("Retrieve Commodity from Cache,error code = " + errorInfo.getErrorCode());
					return errorInfo.getErrorCode();
				} else {
					if (commodityRetrieve.getType() != EnumCommodityType.ECT_Normal.getIndex()) {
						logger.info("参与促销的商品只能是普通商品");
						return EnumErrorCode.EC_BusinessLogicNotDefined;
					}
				}
			}

			List<PromotionScope> prList = new ArrayList<PromotionScope>();
			// 判断是否为全场满减
			PromotionScope promotionScope = new PromotionScope();
			promotionScope.setPromotionID(bm.getID());
			for (int i = 0; i < iArrCommID.length; i++) {
				promotionScope.setCommodityID(iArrCommID[i]);

				DataSourceContextHolder.setDbName(dbName);
				PromotionScope ps = (PromotionScope) promotionScopeBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, promotionScope);
				if (promotionScopeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("Create Promotion error code = " + promotionScopeBO.getLastErrorCode());
					return promotionScopeBO.getLastErrorCode();
				} else {
					logger.info("创建促销商品成功，ps=" + ps);
					prList.add(ps);
				}
			}
			bm.setListSlave1(prList);
		} else if (bm.getScope() != 0 && bm.getScope() != 1) {
			return EnumErrorCode.EC_BusinessLogicNotDefined;
		}
		// 门店
		if (bm.getShopScope() == EnumPromotionShopScope.EPS_SpecifiedShops.getIndex() && bm.getShopIDs() != null) {
			Integer[] iArrShopID = GeneralUtil.toIntArray(bm.getShopIDs());
			if (iArrShopID == null) {
				logger.info("参数缺失");
				return EnumErrorCode.EC_BusinessLogicNotDefined;
			} else {
				logger.info("iArrShopID=" + iArrShopID);
			}
			List<PromotionShopScope> prList = new ArrayList<PromotionShopScope>();
			// 判断是否为全场满减
			PromotionShopScope promotionShopScope = new PromotionShopScope();
			promotionShopScope.setPromotionID(bm.getID());
			for (int i = 0; i < iArrShopID.length; i++) {
				promotionShopScope.setShopID(iArrShopID[i]);

				DataSourceContextHolder.setDbName(dbName);
				PromotionShopScope ps = (PromotionShopScope) promotionShopScopeBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, promotionShopScope);
				if (promotionShopScopeBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("Create PromotionShopScope error code = " + promotionShopScopeBO.getLastErrorCode());
					return promotionShopScopeBO.getLastErrorCode();
				} else {
					logger.info("创建促销门店成功，ps=" + ps);
					prList.add(ps);
				}
			}
			bm.setListSlave2(prList);
		} else if (bm.getShopScope() != EnumPromotionShopScope.EPS_AllShops.getIndex() && bm.getShopScope() != EnumPromotionShopScope.EPS_SpecifiedShops.getIndex()) {
			return EnumErrorCode.EC_BusinessLogicNotDefined;
		}

		return EnumErrorCode.EC_NoError;
	}

	// 重写createDBObject,是为了保留promotion的String2,用于创建从表时指定促销商品
	@Override
	protected BaseModel createDBObject(Boolean useSYSTEM, BaseModel baseModel, ModelMap model, HttpServletRequest req, ErrorInfo ec, String dbName) {
		logger.info("向DB中插入数据，可能需要插主表和从表");
		logger.info("baseModel=" + baseModel);

		BaseModel bmFromDB = null;
		bmFromDB = getModelBO().createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, baseModel);
		ec.setErrorCode(getModelBO().getLastErrorCode());
		ec.setErrorMessage(getModelBO().getLastErrorMessage());
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("创建普通对象失败，错误码=" + ec.getErrorCode());
			return null;
		} else {
			logger.info("创建普通对象成功，bmFromDB=" + bmFromDB);
		}

		((Promotion) bmFromDB).setCommodityIDs(((Promotion) baseModel).getCommodityIDs());
		((Promotion) bmFromDB).setShopIDs(((Promotion) baseModel).getShopIDs());
		bmFromDB.setSyncType(SyncCache.SYNC_Type_C);
		ec.setErrorCode(createSlaveDBObjects(bmFromDB, model, req, dbName));
		if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("createSlaveDBObjects()错误码=" + ec.getErrorCode());
			return null;
		}

		return bmFromDB;
	}

	@Override
	protected boolean retrieveNSlaveDBObjects(BaseModel bmTmp, String dbName, HttpServletRequest req) {
		logger.info("读取促销对象的从表，bmTmp=" + bmTmp);

		if (bmTmp == null) {
			throw new RuntimeException("主表对象为空！");
		}

		Promotion promotion = (Promotion) bmTmp;
		PromotionScope ps = new PromotionScope();
		ps.setPromotionID(promotion.getID());
		ps.setPageSize(PAGE_SIZE_Infinite);

		DataSourceContextHolder.setDbName(dbName);
		List<PromotionScope> pil = (List<PromotionScope>) promotionScopeBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, ps);
		logger.info("错误码=" + promotionScopeBO.getLastErrorCode() + ",pil=" + pil);
		if (promotionScopeBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
			((Promotion) bmTmp).setListSlave1(pil);
		}
		// 门店
		PromotionShopScope promotionShopScope = new PromotionShopScope();
		promotionShopScope.setPromotionID(promotion.getID());
		promotionShopScope.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<PromotionShopScope> listPromotionShopScope = (List<PromotionShopScope>) promotionShopScopeBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, promotionShopScope);
		logger.info("错误码=" + promotionShopScopeBO.getLastErrorCode() + ",listPromotionShopScope=" + listPromotionShopScope);
		if (promotionShopScopeBO.getLastErrorCode() == EnumErrorCode.EC_NoError) {
			((Promotion) bmTmp).setListSlave2(listPromotionShopScope);
		}

		return true;
	}

	/*
	 * URL:http://localhost:8080/nbr/promotionSync/createEx.bx
	 * IDInPOS=-1&POS_SN=1234567&iCID=3208031997070111603X&wechat=b123456&qq=
	 * 44123456&name=ban&email=123456@qq.com&account=q123456&password=123456&
	 * consumeTimes=16&consumeAmount=200&district=广州&category=1&status=0&birthday=
	 * 2016/10/10%2012:10:12&expireDatetime=2016/10/10%2012:10:12&point=42&
	 * lastConsumeDatetime=2016/10/10%2012:10:12 User-Agent: Fiddler Host:
	 * localhost:8080 Content-Type: application/x-www-form-urlencoded
	 */
	// @Transactional
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Promotion promotion, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建促销的同步缓存，promotion=" + promotion);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		// 先检查前端传递过来的商品ID是否合法的商品：1、必须是单品。2、必须未被删除。3、必须至少有一个。如果非法，直接当黑客行为处理
		if (promotion.getScope() == EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()) {
			Integer[] iArrCommID = GeneralUtil.toIntArray(promotion.getCommodityIDs());
			if (iArrCommID == null || iArrCommID.length <= 0) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("没有指定促销商品");
				}
				return null;
			} else {
				logger.info("促销商品列表：iArrCommID=" + iArrCommID);
			}
			// 一旦发现iaCommID内部有重复元素，当黑客行为处理
			if (GeneralUtil.hasDuplicatedElement(iArrCommID)) {
				logger.error("黑客传递的参数有重复！");
				return null;
			}
			//
			Commodity comm = new Commodity();
			for (int i = 0; i < iArrCommID.length; i++) {
				comm.setID(iArrCommID[i]);
				DataSourceContextHolder.setDbName(company.getDbName());
				List<List<BaseModel>> commR1 = commodityBO.retrieve1ObjectEx(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, comm);
				if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("在确定前端传递的商品是否合法商品时出错： " + commodityBO.printErrorInfo());
					Map<String, Object> params = getDefaultParamToReturn(true);
					params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				} else {
					comm = Commodity.fetchCommodityFromResultSet(commR1);
					if (comm.getType() != EnumCommodityType.ECT_Normal.getIndex() || comm.getStatus() == EnumStatusCommodity.ESC_Deleted.getIndex()) {
						logger.error("指定的促销商品不是单品或状态为已删除");
						return null;
					}
				}
			}
		}
		
		// 检查前端传递过来的门店ID是否合法
		if (promotion.getShopScope() == EnumPromotionShopScope.EPS_SpecifiedShops.getIndex()) {
			Integer[] iArrShopID = GeneralUtil.toIntArray(promotion.getShopIDs());
			if (iArrShopID == null || iArrShopID.length <= 0) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("没有指定促销门店");
				}
				return null;
			} else {
				logger.info("促销门店列表：iArrShopID=" + iArrShopID);
			}
			// 一旦发现iArrShopID内部有重复元素，当黑客行为处理
			if (GeneralUtil.hasDuplicatedElement(iArrShopID)) {
				logger.error("黑客传递的参数有重复！");
				return null;
			}
			// 虚拟总部不参与促销
			for (int i = 0; i < iArrShopID.length; i++) {
				if(iArrShopID[i] == 1) {
					Map<String, Object> params = getDefaultParamToReturn(true);
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
					params.put(KEY_HTMLTable_Parameter_msg, "促销不能指定虚拟总部！");
					return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
				}
			}
		}

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			m = handleCreateEx(false, promotion, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("createEx() 异常：" + e.getMessage());

			m = null;
			// lock.writeLock().unlock();
			//
			// throw new RuntimeException("数据库处理异常");
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
		} else {
			logger.info("返回的数据=" + m.toString());
		}

		// if
		// (!EnumErrorCode.valueOf(m.get(BaseAction.JSON_ERROR_KEY).toString()).equals(EnumErrorCode.EC_NoError))
		// {
		// throw new RuntimeException("数据库处理异常");
		// }

		ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();

		lock.writeLock().unlock();

		return ret;
	}

	/*
	 * URL:http://localhost:8080/nbr/promotionSync/updateEx.bx User-Agent: Fiddler
	 * Host: localhost:8080 Content-Type: application/x-www-form-urlencoded
	 */
	// 由于促销活动可以修改会比较麻烦，所以先屏蔽该接口
	// @RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST)
	// @ResponseBody
	// public String updateEx(@ModelAttribute("SpringWeb") Promotion promotion,
	// ModelMap model, HttpServletRequest req) {
	// logger.info("修改促销的同步缓存,promotion=" + promotion.toString());
	//
	// Company company = getCompanyFromSession(req.getSession());
	//
	// String ret;
	//
	// lock.writeLock().lock();
	//
	// Map<String, Object> m = null;
	// try {
	// m = handleUpdateEx(false, BaseBO.INVALID_CASE_ID, promotion, model, req,
	// company.getDbName());
	// } catch (Exception e) {
	// logger.info("updateEx() 异常：" + e.getMessage());
	//
	// m = null;
	// // lock.writeLock().unlock();
	// //
	// // throw new RuntimeException("数据库处理异常");
	// }
	// //
	// if (m == null) {
	// m = getDefaultParamToReturn(false);
	// } else {
	// logger.info("返回的数据=" + m.toString());
	// }
	//
	// // if
	// //
	// (!EnumErrorCode.valueOf(m.get(BaseAction.JSON_ERROR_KEY).toString()).equals(EnumErrorCode.EC_NoError))
	// // {
	// // throw new RuntimeException("数据库处理异常");
	// // }
	//
	// ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();
	//
	// lock.writeLock().unlock();
	//
	// return ret;
	// }

	// @Transactional
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Promotion promotion, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("删除一个促销的同步缓存,Promotion=" + promotion);

		String ret;

		Company company = getCompanyFromSession(req.getSession());

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			m = handleDeleteEx(false, promotion, model, req, company.getDbName(), BaseBO.INVALID_CASE_ID);
		} catch (Exception e) {
			logger.info("deleteEx() 异常：" + e.getMessage());

			m = null;
			// lock.writeLock().unlock();
			//
			// throw new RuntimeException("数据库处理异常");
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
		} else {
			logger.info("返回的数据=" + m.toString());
		}

		// if
		// (!EnumErrorCode.valueOf(m.get(BaseAction.JSON_ERROR_KEY).toString()).equals(EnumErrorCode.EC_NoError))
		// {
		// throw new RuntimeException("数据库处理异常");
		// }

		ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();

		lock.writeLock().unlock();

		return ret;
	}

	/*
	 * 1.重构相应的sp增加相应的从表信息，处理所有与之相关的问题，重写相关的syncAction
	 * 2.重构相应的syncAction，然后将从表的相关信息放入HaseMap get
	 * URL:http://localhost:8080/nbr/promotionSync/retrieveNEx.bx
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Promotion promotion, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个促销的同步缓存,promotion=" + promotion);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			int posID = ((Pos) req.getSession().getAttribute(EnumSession.SESSION_POS.getName())).getID();
			m = handleRetrieveNEx(posID, promotion, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("retrieveNEx() 异常：" + e.getMessage());

			// lock.writeLock().unlock();

			m = null;

			// throw new RuntimeException("数据库处理异常");
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
		} else {
			logger.info("返回的数据=" + m.toString());
		}

		ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();

		lock.writeLock().unlock();

		return ret;
	}

	/*
	 * URL:http://localhost:8080/nbr/promotionSync/feedbackEx.bx User-Agent: Fiddler
	 * Host: localhost:8080 Content-Type: application/x-www-form-urlencoded
	 * 1、POS吸收同步块后告诉服务器自己是否成功同步了一个块
	 * 2、更新同存的DB表（其中可能会删除这个块，因为所有POS机都已经同步了这个块）。更新普通对象的缓存？
	 * 3、更新同步块缓存。如果所有POS机都已经同步了这个块，那就从同存及同存DB表中都删除这个块的相关信息
	 */
	@RequestMapping(value = "/feedbackEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String feedbackEx(@ModelAttribute("SpringWeb") Promotion promotion, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("POS机告知服务器已完成了对促销的同步，Promotion=" + promotion);

		Company company = getCompanyFromSession(req.getSession());
		String ret;
		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			m = handleFeedbackEx(false, promotion, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("feedbackEx() 异常：" + e.getMessage());
			m = null;
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
		} else {
			logger.info("返回的数据=" + m.toString());
		}

		ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();

		lock.writeLock().unlock();

		return ret;
	}
}
