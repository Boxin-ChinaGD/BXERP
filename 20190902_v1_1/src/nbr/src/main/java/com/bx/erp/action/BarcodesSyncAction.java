package com.bx.erp.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.bx.erp.action.BaseSyncAction;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BarcodesSyncCacheBO;
import com.bx.erp.action.bo.BarcodesSyncCacheDispatcherBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BarcodesSyncCache;
import com.bx.erp.model.BarcodesSyncCacheDispatcher;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/barcodesSync")
@Scope("prototype")
public class BarcodesSyncAction extends BaseSyncAction {
	private Log logger = LogFactory.getLog(BarcodesSyncAction.class);

	@Override
	protected int getSequence() {
		return aiSequenceSyncBlockForCommodityAndBarcodes.getAndIncrement();
	}

	@Resource
	protected com.bx.erp.cache.BarcodesSyncCache bsc;

	@Resource
	protected BarcodesSyncCacheBO barcodesSyncCacheBO;

	@Resource
	protected BarcodesSyncCacheDispatcherBO barcodesSyncCacheDispatcherBO;

	@Resource
	protected BarcodesBO barcodesBO;

	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	protected BaseBO getSyncCacheBO() {
		return barcodesSyncCacheBO;
	}

	protected SyncCache getSyncCache() {
		return bsc;
	}

	@Override
	protected BaseModel getModel() {
		return new Barcodes();
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return barcodesSyncCacheDispatcherBO;
	}

	@Override
	protected BaseBO getModelBO() {
		return barcodesBO;
	}

	@Override
	protected EnumCacheType getCacheType() {
		return EnumCacheType.ECT_Barcodes;
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_BarcodesSyncInfo;
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new BarcodesSyncCacheDispatcher();
	}

	@Override
	protected BaseSyncCache getSyncCacheModel() {
		return new BarcodesSyncCache();
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
	 * commodityID=1&barcode=3548293894545 post
	 * URL:http://localhost:8080/nbr/barcodesSync/createEx.bx
	 * 
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Barcodes barcodes, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建条形码的同步缓存，barcodes=" + barcodes);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			Staff staff = getStaffFromSession(req.getSession());// 避免用户传入StaffID即可进行创建的情况，需要从会话中获取登录的StaffID
			//
			barcodes.setOperatorStaffID(staff.getID());
			//
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Commodity).read1(barcodes.getCommodityID(), staff.getID(), ecOut, company.getDbName());
			if (comm == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				m = new HashMap<String, Object>();
				m.put(JSON_ERROR_KEY, ecOut.getErrorCode());
				m.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
				return JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();
			}
			//
			if (comm.getType() != EnumCommodityType.ECT_Normal.getIndex()) {
				Barcodes b = new Barcodes();
				b.setCommodityID(barcodes.getCommodityID());
				DataSourceContextHolder.setDbName(company.getDbName());
				List<?> barcodeList = barcodesBO.retrieveNObject(staff.getID(), BaseBO.INVALID_CASE_ID, b);
				if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					m = new HashMap<String, Object>();
					m.put(JSON_ERROR_KEY, ecOut.getErrorCode());
					m.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
					return JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();
				}
				//
				if (barcodeList.size() > 0) {
					logger.info("除单品外，其他商品只能够有一个条形码");
					return null;
				}
			}

			m = handleCreateEx(false, barcodes, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("createEx() 异常：" + e.getMessage());

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
	 * get URL:http://localhost:8080/nbr/barcodesSync/retrieveNEx.bx
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Barcodes barcodes, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取条形码的同步缓存，barcodes=" + barcodes);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			int posID = ((Pos) req.getSession().getAttribute(EnumSession.SESSION_POS.getName())).getID();
			m = handleRetrieveNEx(posID, barcodes, model, req, company.getDbName());
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
	 * get
	 * URL:http://localhost:8080/nbr/barcodesSync/feedbackEx.bx?sID=1,2&errorCode=
	 * EC_NoError
	 */
	@RequestMapping(value = "/feedbackEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String feedbackEx(@ModelAttribute("SpringWeb") Barcodes barcodes, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("POS机告知服务器已完成了对条形码的同步，barcodes=" + barcodes);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> param = null;
		try {
			param = handleFeedbackEx(false, barcodes, model, req, company.getDbName());
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
	 * get URL:http://localhost:8080/nbr/barcodesSync/deleteEx.bx?ID=1
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Barcodes barcodes, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("准备删除一个条形码，barcodes=" + barcodes);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			Staff staff = getStaffFromSession(req.getSession());
			barcodes.setOperatorStaffID(staff.getID());
			//
			m = handleDeleteEx(false, barcodes, model, req, company.getDbName(), BaseBO.INVALID_CASE_ID);
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

	/*
	 * Request Body in Fiddler:ID=?&commodityID=?&barcode=123456
	 * get URL:http://localhost:8080/nbr/barcodesSync/updateEx.bx?ID=1
	 * 
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Barcodes barcodes, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一个条形码的同步缓存，barcodes=" + barcodes);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			Staff staff = getStaffFromSession(req.getSession());// 避免用户传入StaffID即可进行创建的情况，需要从会话中获取登录的StaffID
			//
			barcodes.setOperatorStaffID(staff.getID());
			m = handleUpdateEx(false, BaseBO.INVALID_CASE_ID, barcodes, model, req, company.getDbName());
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

	@Override
	protected Map<String, Object> handleUpdateEx(Boolean useSYSTEM, int caseID, BaseModel baseModel, ModelMap model, HttpServletRequest req, String dbName) {
		boolean bReturnObject = (baseModel.getReturnObject() == 1);

		BaseModel bmFromDB = null;
		ErrorInfo ec = new ErrorInfo();
		do {
			logger.info("即将修改对象：" + baseModel);
			// 1、更新普通对象的DB表
			bmFromDB = updateDBObject(useSYSTEM, caseID, baseModel, model, req, ec, dbName);
			ec.setErrorCode(getModelBO().getLastErrorCode());
			ec.setErrorMessage(getModelBO().getLastErrorMessage().toString());
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				break;
			} else {
				logger.info("修改对象成功：" + bmFromDB);
				bmFromDB.setSyncDatetime(new Date());
			}

			// 2、更新普通对象的缓存(条形码的修改是先删后增，所以需要先删之前的缓存，再新增一个新的缓存)
			CacheManager.getCache(dbName, getCacheType()).delete1(baseModel);
			CacheManager.getCache(dbName, getCacheType()).write1(bmFromDB, dbName, (useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()));

			// 获得POS的数目。如果POS的数目为1，则不用维护缓存，不需要执行以下if体内的代码；否则必须执行之，因为其它POS机需要同步此同步块
			int iPosSize = getPosSize(useSYSTEM, ec, dbName, req);
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				break;
			} else {
				logger.info("获取成功！POS的数目=" + iPosSize);
			}

			int posID = getLoginPOSID(req);
			if (posID == INVALID_POS_ID || iPosSize > 1) {// 不管是不是网页端操作的，都需要更新调度表
				logger.info(posID == INVALID_POS_ID ? "网页端在操作，需要更新同步块" : "非网页端在操作，POS机数量>1，即将更新同步块...");
				// 3、更新同步对象的DB表,需要创建一个D型，一个U型
				int objSequence = getSequence();
				List<List<BaseModel>> list = updateSyncDBObject(useSYSTEM, posID, req, baseModel.getID(), objSequence, SyncCache.SYNC_Type_D, ec, dbName);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					break;
				}
				logger.info("D型同步块:" + list);
				//
				List<List<BaseModel>> list2 = updateSyncDBObject(useSYSTEM, posID, req, bmFromDB.getID(), objSequence, SyncCache.SYNC_Type_C, ec, dbName);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					break;
				}
				logger.info("C型同步块:" + list2);

				// 4、更新同步块缓存
				updateSyncCacheObjectInMemory(useSYSTEM, SyncCache.SYNC_Type_D, getSyncCacheType(), objSequence, list, dbName, req);
				updateSyncCacheObjectInMemory(useSYSTEM, SyncCache.SYNC_Type_C, getSyncCacheType(), objSequence, list2, dbName, req);
			} else {
				logger.info("pos的数量少于两个,iPosSize=" + iPosSize);
			}

			break;
		} while (false);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode().toString());
		params.put(KEY_HTMLTable_Parameter_msg, ec.getErrorMessage().toString());
		if (bReturnObject) {
			params.put(KEY_Object, bmFromDB);
		}

		return params;
	}

	@Override
	protected BaseModel createDBObjectWithDuplicatedError(BaseModel bmFromDB) {
		logger.info("重复创建条形码，bmFromDB=" + bmFromDB);
		return bmFromDB;
	}
	
	@Override
	protected void handleCreateExWithDuplictedError(BaseModel bmFromDB, ErrorInfo ec, HttpServletRequest req, String dbName) {
		logger.info("欲创建的条形码重复：" + bmFromDB);
		bmFromDB.setSyncDatetime(new Date());
	}
}
