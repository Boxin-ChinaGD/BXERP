package com.bx.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.PosSyncCacheBO;
import com.bx.erp.action.bo.PosSyncCacheDispatcherBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.Pos;
import com.bx.erp.model.PosSyncCache;
import com.bx.erp.model.PosSyncCacheDispatcher;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.util.MD5Util;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/posSync")
@Scope("prototype")
public class PosSyncAction extends BaseSyncAction {
	private Log logger = LogFactory.getLog(PosSyncAction.class);

	@Override
	protected int getSequence() {
		return aiSequenceSyncBlock.getAndIncrement();
	}

	/** ????????????????????????????????????????????????????????????<br />
	 * ???????????????N?????????????????????????????????????????????????????????????????????DB???<br />
	 * ???????????????????????????????????????????????????????????????ACTION????????????????????????<br />
	 * ???????????????????????????????????????bug????????????????????????????????????????????????????????????????????? */
	protected static AtomicInteger aiSequenceSyncBlock = new AtomicInteger();

	@Resource
	protected com.bx.erp.cache.PosSyncCache psc;

	@Resource
	protected PosSyncCacheBO posSyncCacheBO;

	@Resource
	protected PosSyncCacheDispatcherBO posSyncCacheDispatcherBO;

	@Resource
	protected PosBO posBO;

	@Resource
	protected CompanyBO companyBO;

	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	protected BaseBO getSyncCacheBO() {
		return posSyncCacheBO;
	}

	protected SyncCache getSyncCache() {
		return psc;
	}

	@Override
	protected BaseModel getModel() {
		return new Pos();
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return posSyncCacheDispatcherBO;
	}

	@Override
	protected BaseBO getModelBO() {
		return posBO;
	}

	@Override
	protected EnumCacheType getCacheType() {
		return EnumCacheType.ECT_POS;
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_PosSyncInfo;
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new PosSyncCacheDispatcher();
	}

	@Override
	protected BaseSyncCache getSyncCacheModel() {
		return new PosSyncCache();
	}

	@Override
	protected EnumErrorCode deleteSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		return EnumErrorCode.EC_NoError;// Pos??????????????????
	}

	@Override
	protected EnumErrorCode createSlaveDBObjects(BaseModel bmMasterFromDB, ModelMap model, HttpServletRequest req, String dbName) {
		return EnumErrorCode.EC_NoError;// Pos????????????
	}

	/*
	 * post URL:http://localhost:8080/nbr/posSync/createEx.bx <br />
	 * pos.string1????????????SN
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpSession session, HttpServletRequest req) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????POS????????????pos=" + pos);
		//
		if (session.getAttribute(EnumSession.SESSION_BxStaff.getName()) == null) {
			logger.debug("???OP??????????????????pos???");
			return null;
		}
		//
		Company company = getCompanyFromCompanyCacheBySN(pos.getCompanySN());
		if (company == null) {
			return null; // ????????????
		}
		String ret;

		lock.writeLock().lock();

		Map<String, Object> params = new HashMap<String, Object>();
		do {
			try {
				List<BaseModel> bmList = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(false, false);
				if (CollectionUtils.isEmpty(bmList)) {
					logger.info("??????POS??????????????????????????????????????????");
					break;
				}
				boolean isExist = false;
				for (BaseModel bm : bmList) {
					Company c = (Company) bm;
					if (BaseAction.DBName_Public.equals(c.getDbName())) {
						continue;
					}
					DataSourceContextHolder.setDbName(c.getDbName());
					Pos posR1 = (Pos) posBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.CASE_Pos_Retrieve1BySN, pos);
					if (posBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						isExist = true;
						logger.error("??????POS????????????" + posBO.printErrorInfo());
						params.put(BaseAction.JSON_ERROR_KEY, posBO.getLastErrorCode());
						params.put(KEY_HTMLTable_Parameter_msg, posBO.getLastErrorMessage());
						break;
					}
					if (posR1 != null) {
						isExist = true;
						params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated);
						params.put(KEY_HTMLTable_Parameter_msg, "???POS????????????  <" + c.getName() + "> ??????,??????pos??????????????????????????????");
						break;
					}
				}
				if (isExist) {
					break;
				}
				pos.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17)); // ??????16??????????????????

				String md5 = MD5Util.MD5(pos.getPasswordInPOS() + BaseAction.SHADOW);
				pos.setSalt(md5);

				params = handleCreateEx(true, pos, model, req, company.getDbName());
				//TODO ?????????????????????????????????
			} catch (Exception e) {
				logger.info("createEx() ?????????" + e.getMessage());

				params = null;
			}

			break;
		} while (false);
		//
		if (params == null) {
			params = getDefaultParamToReturn(false);
		} else {
			logger.info("???????????????=" + params.toString());
		}

		ret = JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();

		lock.writeLock().unlock();

		return ret;
	}

	/*
	 * post URL:http://localhost:8080/nbr/posSync/updateEx.bx
	 */
	// @RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST)
	// @ResponseBody
	// public String updateEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model,
	// HttpSession session, HttpServletRequest req) {
	// logger.info("??????POS???pos=" + pos);
	//
	// if (session.getAttribute(EnumSession.SESSION_BxStaff.getName()) == null) {
	// logger.debug("???OP??????????????????update pos???");
	// return null;
	// }
	//
	// String ret;
	//
	// lock.writeLock().lock();
	//
	// Map<String, Object> m = null;
	// try {
	// m = handleUpdateEx(true, BaseBO.INVALID_CASE_ID, pos, model, req,
	// company.getDbName());
	// } catch (Exception e) {
	// logger.info("updateEx() ?????????" + e.getMessage());
	//
	// m = null;
	// }
	// //
	// if (m == null) {
	// m = getDefaultParamToReturn(false);
	// } else {
	// logger.info("???????????????=" + m.toString());
	// }
	//
	// ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();
	//
	// lock.writeLock().unlock();
	//
	// return ret;
	// }

	/*
	 * get URL:http://localhost:8080/nbr/posSync/retrieveNEx.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("????????????POS??????????????????pos=" + pos);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			m = handleRetrieveNEx(((Pos) req.getSession().getAttribute(EnumSession.SESSION_POS.getName())).getID(), pos, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("retrieveNEx() ?????????" + e.getMessage());

			m = null;
		}
		//
		if (m == null) {
			m = getDefaultParamToReturn(false);
		} else {
			List<BaseModel> bmList = (List<BaseModel>) m.get(KEY_ObjectList);
			for (BaseModel bm : bmList) {
				Pos posTmp = (Pos) bm;
				posTmp.setSalt("");
			}

			m.put(KEY_ObjectList, bmList);
		}

		logger.info("???????????????=" + m);

		ret = JSONObject.fromObject(m, JsonUtil.jsonConfig).toString();

		lock.writeLock().unlock();

		return ret;
	}

	/*
	 * get URL:http://localhost:8080/nbr/posSync/feedbackEx.bx?sID=1,2&errorCode=
	 * EC_NoError
	 */
	@RequestMapping(value = "/feedbackEx", produces = "plain/text;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String feedbackEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("POS?????????????????????????????????POS????????????pos=" + pos);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> param = null;
		try {
			param = handleFeedbackEx(false, pos, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("PosSynAction feedbackEx() ?????????" + e.getMessage());
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

		logger.info("???????????????=" + param);

		ret = JSONObject.fromObject(param).toString();

		return ret;
	}

	/*
	 * get URL:http://localhost:8080/nbr/posSync/deleteEx.bx?ID=1&string1=668866
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("??????????????????POS??????pos=" + pos);
		//
		Company company = getCompanyFromCompanyCacheBySN(pos.getCompanySN());
		if (company == null) {
			return null; // ????????????
		}
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			m = handleDeleteEx(true, pos, model, req, company.getDbName(), BaseBO.INVALID_CASE_ID);
		} catch (Exception e) {
			logger.info("PosSynAction deleteEx() ?????????" + e.getMessage());
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
	 * Request Body in Fiddler: POST
	 * URL:http://localhost:8080/nbr/posSync/resetEx.bx?ID=1
	 */
	@RequestMapping(value = "/resetEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String resetEx(@ModelAttribute("SpringWeb") Pos pos, ModelMap model, HttpSession session, HttpServletRequest req) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.POS.getIndex())) {
			logger.debug("???????????????Action");
			return null;
		}
		
		logger.info("????????????POS???,pos=" + pos);

		Company company = getCompanyFromSession(req.getSession());
		String ret;

		lock.writeLock().lock();

		Map<String, Object> m = null;
		try {
			m = handleUpdateEx(true, BaseBO.CASE_POS_Reset, pos, model, req, company.getDbName());
		} catch (Exception e) {
			logger.info("updateEx() ?????????" + e.getMessage());

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
}
