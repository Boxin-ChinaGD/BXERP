package com.bx.erp.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.bx.erp.action.bo.BonusConsumeHistoryBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.BonusConsumeHistory;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/bonusConsumeHistory")
@Scope("prototype")
public class BonusConsumeHistoryAction extends BaseAction {
	private Log logger = LogFactory.getLog(CouponAction.class);

	@Resource
	protected BonusConsumeHistoryBO bonusConsumeHistoryBO;

	@RequestMapping(value = "/retrieveNEx", produces = "application/x-www-form-urlencoded; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") BonusConsumeHistory bonusConsumeHistory, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex() | BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询积分历史，bonusConsumeHistory" + bonusConsumeHistory);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		
		int staffOrVipID = 0;
		if(getVipFromSession(session) != null) {
			staffOrVipID = BaseBO.SYSTEM;
		} else {
			staffOrVipID = getStaffFromSession(session).getID();
		}
		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> bonusConsumeHistoryList =  bonusConsumeHistoryBO.retrieveNObject(staffOrVipID, BaseBO.INVALID_CASE_ID, bonusConsumeHistory);
		if (bonusConsumeHistoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询积分历史失败：" + bonusConsumeHistoryBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, bonusConsumeHistoryBO.getLastErrorCode().toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, bonusConsumeHistoryBO.getLastErrorMessage());
		} else {
			params.put(BaseAction.KEY_ObjectList, bonusConsumeHistoryList);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, bonusConsumeHistoryBO.getTotalRecord());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		}

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
