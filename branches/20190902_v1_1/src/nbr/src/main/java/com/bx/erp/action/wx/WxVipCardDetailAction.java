//package com.bx.erp.action.wx;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpSession;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.BaseWxBO;
//import com.bx.erp.action.bo.wx.card.WxVipCardDetailBO;
//import com.bx.erp.cache.CacheManager;
//import com.bx.erp.cache.VipBelongingCache;
//import com.bx.erp.model.ErrorInfo;
//import com.bx.erp.model.VipBelonging;
//import com.bx.erp.model.CacheType.EnumCacheType;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.wx.card.WxVipCardDetail;
//import com.bx.erp.util.DataSourceContextHolder;
//import com.bx.erp.util.JsonUtil;
//
//import net.sf.json.JSONObject;
//
//@Controller
//@RequestMapping("/wxVipCardDetail")
//public class WxVipCardDetailAction extends BaseAction {
//
//	private static final Logger logger = LoggerFactory.getLogger(WxVipCardDetailAction.class);
//
//	@Resource
//	private WxVipCardDetailBO wxVipCardDetailBO;
//
//	/** 向DB查询微信会员卡详情
//	 * URL:https://localhost:8888/wxVipCardDetail/retrieve1ByUnionIdEx.bx?unionId='' */
//	@RequestMapping(value = "retrieve1ByUnionIdEx", method = RequestMethod.GET)
//	@ResponseBody
//	public String retrieve1ByUnionIdEx(@ModelAttribute("SpringWeb") WxVipCardDetail wxVipCardDetail, HttpSession session) {
//		logger.info("查询一个WxVipCardDetail，WxVipCardDetail=" + wxVipCardDetail.toString());
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		//
//		if(wxVipCardDetail.getCard_id().isEmpty()) {
//			logger.error("会员卡card_id为空");
//			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
//			params.put(KEY_HTMLTable_Parameter_msg, "会员卡card_id为空");
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		ErrorInfo errorInfo = new ErrorInfo();
//		VipBelongingCache vipBelongingCache = (VipBelongingCache) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_VipBelonging);
//		VipBelonging vb = vipBelongingCache.read1(wxVipCardDetail.getCard_id(), errorInfo);
//		if (errorInfo.getErrorCode() != EnumErrorCode.EC_NoError || vb == null) {
//			logger.error("查询缓存异常,异常信息:" + errorInfo.getErrorCode().toString() + ",会员卡ID为" + wxVipCardDetail.getCard_id());
//			params.put(KEY_HTMLTable_Parameter_msg, "查询缓存异常！！！");
//			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		//
//		DataSourceContextHolder.setDbName(vb.getDbName());
//		WxVipCardDetail wxVipCardDetailFromDB = (WxVipCardDetail) wxVipCardDetailBO.retrieve1Object(BaseBO.SYSTEM, BaseWxBO.CASE_WxVipCardDetail_Retrieve1ByUnionId, wxVipCardDetail);
//		logger.info("Retrieve1 wxVipCardDetailBO error code and error Info=" + wxVipCardDetailBO.printErrorInfo());
//		if (wxVipCardDetailBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
//			logger.error("查询会员详情异常！！！");
//			params.put(BaseAction.JSON_ERROR_KEY, wxVipCardDetailBO.getLastErrorCode());
//			params.put(KEY_HTMLTable_Parameter_msg, wxVipCardDetailBO.getLastErrorMessage());
//			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//		}
//		params.put(BaseAction.KEY_Object, wxVipCardDetailFromDB);
//		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//		params.put(KEY_HTMLTable_Parameter_msg, wxVipCardDetailBO.getLastErrorMessage());
//		logger.info("返回的数据=" + params);
//
//		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
//	}
//}
