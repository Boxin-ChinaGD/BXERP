package com.bx.erp.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import com.bx.erp.model.Ntp;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/ntp")
@Scope("prototype")
public class NtpAction extends BaseAction {
	private Log logger = LogFactory.getLog(NtpAction.class);

	/*
	Request Body in Fiddler: pos_SN=SN989899&shopID=1&pwdEncrypted=123456
	 GET URL:http://localhost:8080/nbr/ntp/syncEx.bx?t1=1537252151745
	 */
	@RequestMapping(value = "/syncEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String syncEx(@ModelAttribute("SpringWeb") Ntp ntp, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		ntp.setT2(new Date().getTime());

		Map<String, Object> params = new HashMap<String, Object>();

		ntp.setT3(new Date().getTime());

		params.put(BaseAction.KEY_Object, ntp);
		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		params.put(KEY_HTMLTable_Parameter_msg, "同步成功！");
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

}
