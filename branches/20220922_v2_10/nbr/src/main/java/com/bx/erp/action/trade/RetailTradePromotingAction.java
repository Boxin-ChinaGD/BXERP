package com.bx.erp.action.trade;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingBO;
import com.bx.erp.action.bo.trade.RetailTradePromotingFlowBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

@Controller
@RequestMapping("/retailTradePromoting")
@Scope("prototype")
public class RetailTradePromotingAction extends BaseAction {
	private Log logger = LogFactory.getLog(RetailTradePromotingAction.class);

	@Resource
	private RetailTradePromotingBO retailTradePromotingBO;

	@Resource
	private RetailTradePromotingFlowBO retailTradePromotingFlowBO;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieve1Ex", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") RetailTradePromoting rtp, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = new HashMap<String, Object>();

		DataSourceContextHolder.setDbName(company.getDbName());
		RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, rtp);

		logger.info("Create Promotion error code = " + retailTradePromotingBO.getLastErrorCode());
		switch (retailTradePromotingBO.getLastErrorCode()) {
		case EC_NoError:
			RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
			retailTradePromotingFlow.setRetailTradePromotingID(retailTradePromoting.getID());
			DataSourceContextHolder.setDbName(company.getDbName());
			List<RetailTradePromotingFlow> listRetailTradePromotingFlow = (List<RetailTradePromotingFlow>) retailTradePromotingFlowBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, retailTradePromotingFlow);
			logger.info("错误码retailTradePromotingFlowBO.getLastErrorCode()=" + retailTradePromotingFlowBO.getLastErrorCode());
			if (retailTradePromotingFlowBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(JSON_ERROR_KEY, retailTradePromotingFlowBO.getLastErrorCode().toString());
				break;
			}

			retailTradePromoting.setListSlave1(listRetailTradePromotingFlow);
			logger.info("查询成功！retailTradePromoting=" + retailTradePromoting);
			params.put(KEY_Object, retailTradePromoting);
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_BusinessLogicNotDefined:
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		case EC_NoPermission:
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, retailTradePromotingBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/createEx", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("json") String json, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("创建零售单促销详细计算信息，json=" + json);
		Company company = getCompanyFromSession(session);

		RetailTradePromoting rtp = new RetailTradePromoting();
		rtp = (RetailTradePromoting) rtp.parse1(json);
		if (rtp == null) {
			return null;
		}

		DataSourceContextHolder.setDbName(company.getDbName());
		RetailTradePromoting createObject = (RetailTradePromoting) retailTradePromotingBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, rtp);
		logger.info("Create RetailTradePromoting error code = " + retailTradePromotingBO.getLastErrorCode());
		Map<String, Object> params = new HashMap<String, Object>();
		switch (retailTradePromotingBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("创建主表成功");
			if (rtp.getListSlave1() != null && rtp.getListSlave1().size() > 0) {
				List<RetailTradePromotingFlow> list = new ArrayList<RetailTradePromotingFlow>();
				for (Object o : rtp.getListSlave1()) {
					RetailTradePromotingFlow rtpf = (RetailTradePromotingFlow) o;
					// 给从表引用ID赋值
					rtpf.setRetailTradePromotingID(createObject.getID());
					DataSourceContextHolder.setDbName(company.getDbName());
					RetailTradePromotingFlow retailTradePromotingFlow = (RetailTradePromotingFlow) retailTradePromotingFlowBO.createObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, rtpf);
					// ...判断错误码。有错就break出去，返回错误码给POS
					if (retailTradePromotingFlowBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.info("创建从表失败");
						params.put(JSON_ERROR_KEY, retailTradePromotingFlowBO.getLastErrorCode());
						break;
					}
					logger.info("Create RetailTradePromotingFlow error code = " + retailTradePromotingFlowBO.getLastErrorCode());
					list.add(retailTradePromotingFlow);
				}
				logger.info("创建从表成功");
				createObject.setListSlave1(list);
			}
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_Object, createObject);
			break;
		case EC_NoPermission:
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_NoSuchData:
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, retailTradePromotingBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/createNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createNEx(@ModelAttribute("SpringWeb") RetailTradePromoting retailTradePromoting, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		Company company = getCompanyFromSession(req.getSession());

		String str = GetStringFromRequest(req, KEY_ObjectList, String.valueOf(BaseAction.INVALID_ID));

		if (str == null || str.equals(String.valueOf(BaseAction.INVALID_ID))) {
			logger.info("->数据格式不正确!");
			return null;
		}

		Map<String, Object> params = getDefaultParamToReturn(true);
		List<RetailTradePromoting> retailTradeList = new ArrayList<RetailTradePromoting>();

		JSONArray jsonArray = JSONArray.fromObject(str);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { DATETIME_FORMAT_Default2, DATETIME_FORMAT_Default3 }));
			RetailTradePromoting rp = (RetailTradePromoting) JSONObject.toBean(json, RetailTradePromoting.class);
			if (rp.getListSlave1() == null || rp == null) {
				logger.info("->创建零售单促销计算过程或零售单促销流程为空!");
				return null;
			} else {
				logger.info("创建零售单促销计算过程成功，retailTradePromoting=" + rp);
			}
			// 获取从表信息
			List<RetailTradePromotingFlow> retailTradePromotingFlowList = new ArrayList<RetailTradePromotingFlow>();
			for (int j = 0; j < rp.getListSlave1().size(); j++) {
				json = JSONObject.fromObject(rp.getListSlave1().get(j));
				RetailTradePromotingFlow retailTradePromotingFlow = (RetailTradePromotingFlow) JSONObject.toBean(json, RetailTradePromotingFlow.class);
				if (retailTradePromotingFlow == null) {
					logger.info("->零售单促销流程为空!");
					return null;
				}
				retailTradePromotingFlowList.add(retailTradePromotingFlow);
			}
			// 创建主表
			DataSourceContextHolder.setDbName(company.getDbName());
			RetailTradePromoting retailTradePromotingCreate = (RetailTradePromoting) retailTradePromotingBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, rp);
			if (retailTradePromotingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建计算过程失败：" + retailTradePromotingBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, retailTradePromotingBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, retailTradePromotingBO.getLastErrorMessage());
				break;
			}
			// 创建从表
			RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
			retailTradePromotingFlow.setRetailTradePromotingID(retailTradePromotingCreate.getID());
			DataSourceContextHolder.setDbName(company.getDbName());
			List<?> listRetailTradePromotingFlow = retailTradePromotingFlowBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, retailTradePromotingFlow);
			logger.info("错误码retailTradePromotingFlowBO.getLastErrorCode()=" + retailTradePromotingFlowBO.getLastErrorCode());
			if (retailTradePromotingFlowBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(JSON_ERROR_KEY, retailTradePromotingFlowBO.getLastErrorCode().toString());
				break;
			}
			retailTradePromotingCreate.setListSlave1(listRetailTradePromotingFlow);
			retailTradeList.add(retailTradePromotingCreate);
		}

		params.put(KEY_Object, null);
		params.put(KEY_ObjectList, retailTradeList);
		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
