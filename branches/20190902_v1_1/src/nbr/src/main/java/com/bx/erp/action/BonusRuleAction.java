package com.bx.erp.action;

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
import com.bx.erp.action.bo.BonusRuleBO;
import com.bx.erp.model.Company;
import com.bx.erp.model.BonusRule;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/bonusRule")
@Scope("prototype")
public class BonusRuleAction extends BaseAction {
	private Log logger = LogFactory.getLog(VipAction.class);

	@Resource
	private BonusRuleBO bonusRuleBO;

	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") BonusRule bonusRule, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改积分规则，bonusRule" + bonusRule);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			DataSourceContextHolder.setDbName(company.getDbName());
			BonusRule BonusRuleUpdate = (BonusRule) bonusRuleBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, bonusRule);
			if (bonusRuleBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("修改积分规则失败：" + bonusRuleBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, bonusRuleBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, bonusRuleBO.getLastErrorMessage());
				break;
			}

			params.put(BaseAction.KEY_Object, BonusRuleUpdate);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
