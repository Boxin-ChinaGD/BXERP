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
import com.bx.erp.action.bo.VipCardBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.VipCard;
import com.bx.erp.model.BonusRule;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/vipCard")
@Scope("prototype")
public class VipCardAction extends BaseAction {
	private Log logger = LogFactory.getLog(VipAction.class);

	@Resource
	private VipCardBO vipCardBO;

	@Resource
	private BonusRuleBO bonusRuleBO;

	/*
	 * URL:http://localhost:8080/nbr/vip/createEx.bx
	 * ID=1&title=会员卡&backgroundUrl=url=xxxxxxxxxx&bonusCleared=1月1号清零
	 * User-Agent: Fiddler Host:
	 * localhost:8080 Content-Type: application/x-www-form-urlencoded
	 */
	// @Transactional
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") VipCard vipCard, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一种会员卡，vipCard" + vipCard);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			DataSourceContextHolder.setDbName(company.getDbName());
			VipCard vipCardUpdate = (VipCard) vipCardBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, vipCard);
			if (vipCardBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("修改会员卡失败：" + vipCardBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, vipCardBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, vipCardBO.getLastErrorMessage());
				break;
			}
			// 加入到缓存中
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_VipCard).write1(vipCardUpdate, company.getDbName(), getStaffFromSession(session).getID());

			params.put(BaseAction.KEY_Object, vipCardUpdate);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * URL:http://localhost:8080/nbr/vip/createEx.bx?ID=1
	 * User-Agent: Fiddler Host:
	 * localhost:8080 Content-Type: application/x-www-form-urlencoded
	 */
	// @Transactional
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") VipCard vipCard, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex() | BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询一种会员卡，vipCard" + vipCard);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			ErrorInfo ecOut = new ErrorInfo();
			VipCard vipCardRetrieve1 = null;
			if (getVipFromSession(session) != null) {
				vipCardRetrieve1 = (VipCard) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_VipCard).read1(vipCard.getID(), BaseBO.SYSTEM, ecOut, company.getDbName());
			} else {
				vipCardRetrieve1 = (VipCard) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_VipCard).read1(vipCard.getID(), getStaffFromSession(session).getID(), ecOut, company.getDbName());
			}
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询会员卡失败：" + ecOut.getErrorMessage().toString());
				params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage().toString());
				break;
			}

			BonusRule bonusRuleR1 = null;
			if (getVipFromSession(session) == null) { //小程序不需要返回积分规则
				BonusRule bonusRule = new BonusRule();
				bonusRule.setID(BonusRule.DEFAULT_BonusRule_ID);
				//
				DataSourceContextHolder.setDbName(company.getDbName());
				bonusRuleR1 = (BonusRule) bonusRuleBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, bonusRule);
				if (bonusRuleBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询积分规则失败：" + bonusRuleBO.printErrorInfo());
					params.put(BaseAction.JSON_ERROR_KEY, bonusRuleBO.getLastErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, bonusRuleBO.getLastErrorMessage());
					break;
				}
				params.put(BaseAction.KEY_Object2, bonusRuleR1);
			}

			params.put(BaseAction.KEY_Object, vipCardRetrieve1);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
