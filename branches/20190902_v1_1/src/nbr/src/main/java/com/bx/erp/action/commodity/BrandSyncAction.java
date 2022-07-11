package com.bx.erp.action.commodity;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bx.erp.action.BaseSyncAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.BrandBO;
import com.bx.erp.action.bo.commodity.BrandSyncCacheBO;
import com.bx.erp.action.bo.commodity.BrandSyncCacheDispatcherBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.Pos;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.BrandSyncCache;
import com.bx.erp.model.commodity.BrandSyncCacheDispatcher;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/brandSync")
@Scope("prototype")
public class BrandSyncAction extends BaseSyncAction {
	private Log logger = LogFactory.getLog(BrandSyncAction.class);

	@Override
	protected int getSequence() {
		return aiSequenceSyncBlock.getAndIncrement();
	}

	/**
	 * 不断自增，标记同步块在客户端同步的顺序。<br />
	 * 客户端收到N个同步块后，先排序，再逐个将这些块写入客户端的DB。<br />
	 * 多线程安全问题：本变量不需要加锁，因为每个ACTION都是互斥运行的。<br />
	 * 缺陷：理论上存在过大溢出的bug。实际上在产品的迭代过程中，不可能会有机会溢出
	 */
	protected static AtomicInteger aiSequenceSyncBlock = new AtomicInteger();

	@Resource
	protected com.bx.erp.cache.commodity.BrandSyncCache bsc;

	@Resource
	protected BrandSyncCacheBO brandSyncCacheBO;

	@Resource
	protected BrandSyncCacheDispatcherBO brandSyncCacheDispatcherBO;

	@Resource
	protected BrandBO brandBO;

	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	protected BaseBO getSyncCacheBO() {
		return brandSyncCacheBO;
	}

	protected SyncCache getSyncCache() {
		return bsc;
	}

	@Override
	protected BaseModel getModel() {
		return new Brand();
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return brandSyncCacheDispatcherBO;
	}

	@Override
	protected BaseBO getModelBO() {
		return brandBO;
	}

	@Override
	protected EnumCacheType getCacheType() {
		return EnumCacheType.ECT_Brand;
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_BrandSyncInfo;
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new BrandSyncCacheDispatcher();
	}

	@Override
	protected BaseSyncCache getSyncCacheModel() {
		return new BrandSyncCache();
	}

	@Override
	protected EnumErrorCode deleteSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		return EnumErrorCode.EC_NoError;// Brand没有从表数据
	}

	@Override
	protected EnumErrorCode createSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		return EnumErrorCode.EC_NoError;// Brand没有从表
	}

	/*
	 * name=测试 post URL:http://localhost:8080/nbr/brandSync/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Brand brand, ModelMap model, HttpSession session, HttpServletRequest req) {
		if (!canCallCurrentAction(session, EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个品牌的同步缓存,brand=" + brand);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			m = handleCreateEx(false, brand, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("createEx() 异常：" + e.getMessage());

			// lock.writeLock().unlock();
			//
			// throw new RuntimeException("数据库处理异常");

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

	/*
	 * ID=1&name=测试 post URL:http://localhost:8080/nbr/brandSync/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Brand brand, ModelMap model, HttpSession session, HttpServletRequest req) {
		if (!canCallCurrentAction(session, EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一个品牌的同步缓存,brand=" + brand);

		Map<String, Object> m = null;
		
		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		try {
			m = handleUpdateEx(false, BaseBO.INVALID_CASE_ID, brand, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("updateEx() 异常：" + e.getMessage());

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
	 * get URL:http://localhost:8080/nbr/brandSync/retrieveNEx.bx
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Brand brand, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查找多个品牌的同步缓存,brand=" + brand);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			int posID = ((Pos) req.getSession().getAttribute(EnumSession.SESSION_POS.getName())).getID();
			m = handleRetrieveNEx(posID, brand, model, req, company.getDbName());
		} catch (Exception e) {
			logger.error("retrieveNEx() 异常：" + e.getMessage());

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
	 * get URL:http://localhost:8080/nbr/brandSync/feedbackEx.bx?sID=1,2&errorCode=
	 * EC_NoError
	 */
	@RequestMapping(value = "/feedbackEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String feedbackEx(@ModelAttribute("SpringWeb") Brand brand, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("POS告知服务器品牌已经同步完成,brand=" + brand);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> param = null;
		try {
			param = handleFeedbackEx(false, brand, model, req, company.getDbName());
		} catch (Exception e) {
			e.printStackTrace();
			param = null;
		}
		//
		if (param == null) {
			param = getDefaultParamToReturn(false);
		} else {
			logger.info("返回的数据=" + param.toString());
		}

		lock.writeLock().unlock();

		ret = JSONObject.fromObject(param, JsonUtil.jsonConfig).toString();

		return ret;
	}

	/*
	 * get URL:http://localhost:8080/nbr/brandSync/deleteEx.bx?ID=1
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Brand brand, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("准备删除一个品牌,brand=" + brand);

		GeneralUtil.printCurrentFunction(null, null);

		Map<String, Object> m = null;
		
		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		try {
			m = handleDeleteEx(false, brand, model, req, company.getDbName(), BaseBO.INVALID_CASE_ID);
		} catch (Exception e) {
			e.printStackTrace();
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
	
	@Override
	protected BaseModel createDBObjectWithDuplicatedError(BaseModel bmFromDB) {
		logger.info("重复创建品牌，bmFromDB=" + bmFromDB);
		return bmFromDB;
	}
	
	@Override
	protected void handleCreateExWithDuplictedError(BaseModel bmFromDB, ErrorInfo ec, HttpServletRequest req, String dbName) {
		logger.info("欲创建的品牌重复：" + bmFromDB);
		bmFromDB.setSyncDatetime(new Date());
	}

}
