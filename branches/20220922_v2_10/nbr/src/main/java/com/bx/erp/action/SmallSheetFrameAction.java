package com.bx.erp.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.SmallSheetFrameBO;
import com.bx.erp.action.bo.SmallSheetTextBO;
import com.bx.erp.cache.CacheManager;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;

@Controller
@RequestMapping("/smallSheetFrame")
@Scope("prototype")
public class SmallSheetFrameAction extends BaseAction {
	private Log logger = LogFactory.getLog(SmallSheetFrameAction.class);

	// @Override
	// protected int getSequence() {
	// return aiSequenceSyncBlock.getAndIncrement();
	// }

	/** 不断自增，标记同步块在客户端同步的顺序。<br />
	 * 客户端收到N个同步块后，先排序，再逐个将这些块写入客户端的DB。<br />
	 * 多线程安全问题：本变量不需要加锁，因为每个ACTION都是互斥运行的。<br />
	 * 缺陷：理论上存在过大溢出的bug。实际上在产品的迭代过程中，不可能会有机会溢出 */
	// protected static AtomicInteger aiSequenceSyncBlock = new AtomicInteger();

	@Resource
	private SmallSheetFrameBO smallSheetFrameBO;

	@Resource
	private SmallSheetTextBO smallSheetTextBO;

	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") SmallSheetFrame smallSheetFrame, ModelMap mm, HttpServletRequest req, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		//
		do {
			DataSourceContextHolder.setDbName(company.getDbName());
			SmallSheetFrame ssf = (SmallSheetFrame) smallSheetFrameBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, smallSheetFrame);
			if (smallSheetFrameBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建小票格式主表失败：" + smallSheetFrameBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, smallSheetFrameBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, smallSheetFrameBO.getLastErrorMessage());
				break;
			}
			//
			ErrorInfo ec = new ErrorInfo();
			ec.setErrorCode(createSmallSheetText(ssf, req, company.getDbName()));
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建小票格式从表失败：" + ec.getErrorCode());
				params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode());
				break;
			}
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_SmallSheet).write1(ssf, company.getDbName(), getStaffFromSession(session).getID());
			//
			params.put(BaseAction.KEY_Object, ssf);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") SmallSheetFrame smallSheetFrame, ModelMap mm, HttpServletRequest req, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		// 检查从表格式是否正确
		if (!checkUpdate(smallSheetFrame, req)) {
			return null;
		}
		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		//
		do {
			DataSourceContextHolder.setDbName(company.getDbName());
			SmallSheetFrame ssf = (SmallSheetFrame) smallSheetFrameBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, smallSheetFrame);
			if (smallSheetFrameBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("修改小票格式主表失败：" + smallSheetFrameBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, smallSheetFrameBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, smallSheetFrameBO.getLastErrorMessage());
				break;
			}
			// 删除旧的小票格式从表信息
			SmallSheetText sst = new SmallSheetText();
			sst.setFrameId(smallSheetFrame.getID());
			DataSourceContextHolder.setDbName(company.getDbName());
			smallSheetTextBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, sst);
			if (smallSheetFrameBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("删除小票格式从表失败：" + smallSheetTextBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, smallSheetTextBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, smallSheetTextBO.getLastErrorMessage());
				break;
			}
			// 创建新小票格式
			ErrorInfo ec = new ErrorInfo();
			ec.setErrorCode(createSmallSheetText(ssf, req, company.getDbName()));
			if (ec.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建小票格式从表失败：" + ec.getErrorCode());
				params.put(BaseAction.JSON_ERROR_KEY, ec.getErrorCode());
				break;
			}
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_SmallSheet).write1(ssf, company.getDbName(), getStaffFromSession(session).getID());
			//
			params.put(BaseAction.KEY_Object, ssf);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/smallSheetFrame/retrieveNEx.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") SmallSheetFrame smallSheetFrame, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("读取多个小票主表，smallSheetFrame" + smallSheetFrame);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<SmallSheetFrame> bm = (List<SmallSheetFrame>) smallSheetFrameBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, new SmallSheetFrame());

		logger.info("retrieveN SmallSheetFrame error code=" + smallSheetFrameBO.getLastErrorCode());
		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (smallSheetFrameBO.getLastErrorCode()) {
		case EC_NoError:
			SmallSheetText sst = new SmallSheetText();

			for (SmallSheetFrame ssf : bm) {
				ssf.setSyncDatetime(new Date());

				sst.setFrameId(ssf.getID());
				sst.setPageSize(BaseAction.PAGE_SIZE_Infinite);
				DataSourceContextHolder.setDbName(company.getDbName());
				List<SmallSheetText> retrieveNObject = (List<SmallSheetText>) smallSheetTextBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, sst);
				logger.info("retrieveN smallSheetText error code=" + smallSheetTextBO.getLastErrorCode());
				if (smallSheetTextBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("读取多个小票内容格式失败，错误码=" + smallSheetTextBO.getLastErrorCode());
					return null;
				}

				for (SmallSheetText smallSheetText : retrieveNObject) {
					smallSheetText.setSyncDatetime(new Date());
				}

				ssf.setListSlave1(retrieveNObject);
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_ObjectList, bm);
			break;
		case EC_NoPermission:
			logger.info("没有权限");

			params.put(KEY_HTMLTable_Parameter_msg, smallSheetFrameBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, smallSheetFrameBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") SmallSheetFrame smallSheetFrame, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		//
		Company company = getCompanyFromSession(session);
		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			// 判断是否为默认的小票格式
			if (smallSheetFrame.getID() == BaseAction.DEFAULT_SmallSheetID) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "无法删除默认的小票格式");
				break;
			}
			//
			DataSourceContextHolder.setDbName(company.getDbName());
			smallSheetFrameBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, smallSheetFrame);
			if (smallSheetFrameBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("删除小票格式主表失败：" + smallSheetFrameBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, smallSheetFrameBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, smallSheetFrameBO.getLastErrorMessage());
				break;
			}
			// 更新缓存
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_SmallSheet).delete1(smallSheetFrame);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/smallSheetFrame/retrieve1Ex.bx?ID=1
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") SmallSheetFrame smallSheetFrame, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("读取一个小票格式，smallSheetFrame" + smallSheetFrame);

		Company company = getCompanyFromSession(session);

		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = null;
		Map<String, Object> params = getDefaultParamToReturn(true);
		DataSourceContextHolder.setDbName(company.getDbName());
		if (!smallSheetFrameBO.checkRetrieve1Permission(getStaffFromSession(session).getID(), INVALID_ID, null)) {

			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission);

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} else {
			bm = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_SmallSheet).read1(smallSheetFrame.getID(), getStaffFromSession(session).getID(), ecOut, company.getDbName());
		}
		if(bm == null) {
			logger.error("从缓存中读取小票格式失败");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "读取小票格式失败");
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		logger.info("retrieve1 SmallSheetFrame error code=" + ecOut.getErrorCode());
		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			SmallSheetFrame ssf = (SmallSheetFrame) bm;
			SmallSheetText sst = new SmallSheetText();
			sst.setFrameId(ssf.getID());
			sst.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(company.getDbName());
			List<SmallSheetText> sstList = (List<SmallSheetText>) smallSheetTextBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, sst);

			logger.info(sstList);
			ssf.setListSlave1(sstList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put("object", ssf);// ...
			break;
		case EC_NoPermission:
			logger.info("没有权限");

			params.put(KEY_HTMLTable_Parameter_msg, smallSheetFrameBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	private EnumErrorCode createSmallSheetText(SmallSheetFrame ssf, HttpServletRequest req, String dbName) {
		logger.info("创建小票格式从表，SmallSheetFrame=" + ssf);

		//
		List<SmallSheetText> sstList = new ArrayList<SmallSheetText>();
		String str = GetStringFromRequest(req, "smallSheetTextList", String.valueOf(BaseAction.INVALID_ID)).trim();
		if (str == null || str.equals(String.valueOf(BaseAction.INVALID_ID))) {
			logger.error("->从表数据格式不正确!");
			return EnumErrorCode.EC_OtherError;
		} else {
			logger.info("->从表数据格式正确，str=" + str);
		}

		JSONArray jsonArray = JSONArray.fromObject(str);
		if(jsonArray.size() != SmallSheetFrame.NO_SmallSheetTextItem) {
			logger.error("->从表个数不正确!");
			return EnumErrorCode.EC_OtherError;
		}
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { DATETIME_FORMAT_Default2, DATETIME_FORMAT_Default3 }));
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = (JSONObject) jsonArray.get(i);
			SmallSheetText smallSheetText = (SmallSheetText) JSONObject.toBean(json, SmallSheetText.class);
			if (smallSheetText == null) {
				logger.error("-> 从表数据解析失败！");
				return EnumErrorCode.EC_OtherError;
			}
			smallSheetText.setFrameId(ssf.getID());
			//
			DataSourceContextHolder.setDbName(dbName);
			SmallSheetText sst = (SmallSheetText) smallSheetTextBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, smallSheetText);
			// logger.info(sst.getID() + " " + json);
			if (smallSheetTextBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("->创建从表失败.从表=" + sst);
				return smallSheetTextBO.getLastErrorCode();
			} else {
				logger.info("->创建从表成功.从表=" + sst);
			}
			sst.setSyncDatetime(new Date());
			logger.info("->从表信息:" + sst);
			sstList.add(sst);
		}
		ssf.setListSlave1(sstList);

		return EnumErrorCode.EC_NoError;
	}
	
	/** 检查从表的F_FrameID是否=主表的F_ID */
	private boolean checkUpdate(SmallSheetFrame smallSheetFrame, HttpServletRequest req) {
		String str = GetStringFromRequest(req, "smallSheetTextList", String.valueOf(BaseAction.INVALID_ID)).trim();
		if (str.equals(String.valueOf(BaseAction.INVALID_ID)) || str == null) {
			logger.info("->从表数据格式不正确!");
			return false;
		} else {
			logger.info("->从表数据格式正确，str=" + str);
		}
		//
		JSONArray jsonArray = JSONArray.fromObject(str);
		//			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { DATETIME_FORMAT_Default2, DATETIME_FORMAT_Default3 }));
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = (JSONObject) jsonArray.get(i);
			SmallSheetText smallSheetText = (SmallSheetText) JSONObject.toBean(json, SmallSheetText.class);
			if (smallSheetText == null) {
				logger.info("-> 从表数据解析失败！");
				return false;
			}
			if (smallSheetText.getFrameId() != smallSheetFrame.getID()) {
				return false;
			}
		}

		return true;
	}
}
