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
		return EnumErrorCode.EC_NoError;// Brand??????????????????
	}

	@Override
	protected EnumErrorCode createSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		return EnumErrorCode.EC_NoError;// Brand????????????
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????????????????barcodes=" + barcodes);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			Staff staff = getStaffFromSession(req.getSession());// ??????????????????StaffID???????????????????????????????????????????????????????????????StaffID
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
					logger.info("??????????????????????????????????????????????????????");
					return null;
				}
			}

			m = handleCreateEx(false, barcodes, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("createEx() ?????????" + e.getMessage());

			m = null;
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
		} else {
			logger.info("???????????????=" + m.toString());
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("?????????????????????????????????barcodes=" + barcodes);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			int posID = ((Pos) req.getSession().getAttribute(EnumSession.SESSION_POS.getName())).getID();
			m = handleRetrieveNEx(posID, barcodes, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("retrieveNEx() ?????????" + e.getMessage());

			// lock.writeLock().unlock();

			m = null;

			// throw new RuntimeException("?????????????????????");
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
		} else {
			logger.info("???????????????=" + m.toString());
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("POS??????????????????????????????????????????????????????barcodes=" + barcodes);

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
			logger.info("???????????????=" + param.toString());
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????????????????barcodes=" + barcodes);

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
			logger.info("???????????????=" + m.toString());
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
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("???????????????????????????????????????barcodes=" + barcodes);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			Staff staff = getStaffFromSession(req.getSession());// ??????????????????StaffID???????????????????????????????????????????????????????????????StaffID
			//
			barcodes.setOperatorStaffID(staff.getID());
			m = handleUpdateEx(false, BaseBO.INVALID_CASE_ID, barcodes, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("updateEx() ?????????" + e.getMessage());

			// lock.writeLock().unlock();

			m = null;

			// throw new RuntimeException("?????????????????????");
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
		} else {
			logger.info("???????????????=" + m.toString());
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
			logger.info("?????????????????????" + baseModel);
			// 1????????????????????????DB???
			bmFromDB = updateDBObject(useSYSTEM, caseID, baseModel, model, req, ec, dbName);
			ec.setErrorCode(getModelBO().getLastErrorCode());
			ec.setErrorMessage(getModelBO().getLastErrorMessage().toString());
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				break;
			} else {
				logger.info("?????????????????????" + bmFromDB);
				bmFromDB.setSyncDatetime(new Date());
			}

			// 2??????????????????????????????(???????????????????????????????????????????????????????????????????????????????????????????????????)
			CacheManager.getCache(dbName, getCacheType()).delete1(baseModel);
			CacheManager.getCache(dbName, getCacheType()).write1(bmFromDB, dbName, (useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(req.getSession()).getID()));

			// ??????POS??????????????????POS????????????1????????????????????????????????????????????????if??????????????????????????????????????????????????????POS???????????????????????????
			int iPosSize = getPosSize(useSYSTEM, ec, dbName, req);
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				break;
			} else {
				logger.info("???????????????POS?????????=" + iPosSize);
			}

			int posID = getLoginPOSID(req);
			if (posID == INVALID_POS_ID || iPosSize > 1) {// ????????????????????????????????????????????????????????????
				logger.info(posID == INVALID_POS_ID ? "??????????????????????????????????????????" : "????????????????????????POS?????????>1????????????????????????...");
				// 3????????????????????????DB???,??????????????????D????????????U???
				int objSequence = getSequence();
				List<List<BaseModel>> list = updateSyncDBObject(useSYSTEM, posID, req, baseModel.getID(), objSequence, SyncCache.SYNC_Type_D, ec, dbName);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					break;
				}
				logger.info("D????????????:" + list);
				//
				List<List<BaseModel>> list2 = updateSyncDBObject(useSYSTEM, posID, req, bmFromDB.getID(), objSequence, SyncCache.SYNC_Type_C, ec, dbName);
				if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
					break;
				}
				logger.info("C????????????:" + list2);

				// 4????????????????????????
				updateSyncCacheObjectInMemory(useSYSTEM, SyncCache.SYNC_Type_D, getSyncCacheType(), objSequence, list, dbName, req);
				updateSyncCacheObjectInMemory(useSYSTEM, SyncCache.SYNC_Type_C, getSyncCacheType(), objSequence, list2, dbName, req);
			} else {
				logger.info("pos?????????????????????,iPosSize=" + iPosSize);
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
		logger.info("????????????????????????bmFromDB=" + bmFromDB);
		return bmFromDB;
	}
	
	@Override
	protected void handleCreateExWithDuplictedError(BaseModel bmFromDB, ErrorInfo ec, HttpServletRequest req, String dbName) {
		logger.info("??????????????????????????????" + bmFromDB);
		bmFromDB.setSyncDatetime(new Date());
	}
}
