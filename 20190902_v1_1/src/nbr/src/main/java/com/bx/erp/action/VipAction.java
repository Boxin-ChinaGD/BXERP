package com.bx.erp.action;

import java.util.ArrayList;
import java.util.Date;
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

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.VipBO;
import com.bx.erp.action.bo.VipCardCodeBO;
import com.bx.erp.action.bo.VipCategoryBO;
import com.bx.erp.action.bo.VipSourceBO;
//import com.bx.erp.action.bo.wx.card.WxVipCardDetailBO;
import com.bx.erp.cache.CacheManager;
//import com.bx.erp.cache.VipBelongingCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipCardField;
import com.bx.erp.model.VipCardCode;
//import com.bx.erp.model.VipBelonging;
import com.bx.erp.model.VipCategory;
import com.bx.erp.model.VipField;
import com.bx.erp.model.VipSource;
import com.bx.erp.model.VipSource.EnumVipSourceCode;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.BonusRuleField;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
//import com.bx.erp.model.wx.card.WxVipCardDetail;
import com.bx.erp.model.RetailTradeCommodity;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/vip")
@Scope("prototype")
public class VipAction extends BaseAction {
	private Log logger = LogFactory.getLog(VipAction.class);

	@Resource
	private VipBO vipBO;

	@Resource
	private VipSourceBO vipSourceBO;

	@Resource
	private VipCategoryBO vipCategoryBO;

	@Resource
	private VipCardCodeBO vipCardCodeBO;

	// @Resource
	// private WxVipCardDetailBO wxVipCardDetailBO;

	/*
	 * 会员卡管理页面跳转
	 */
	@RequestMapping(value = "/vipCardManage", method = RequestMethod.GET)
	public String manage(ModelMap mm) {
		mm.put("VipField", new VipField());
		mm.put("VipCardField", new VipCardField());
		mm.put("BonusRuleField", new BonusRuleField());
		return VIP_VipCardManage;
	}
	/*
	 * 会员管理页面跳转
	 */
	@RequestMapping(value = "/memberManagement", method = RequestMethod.GET)
	public String memberManagement(ModelMap mm) {
		mm.put("VipField", new VipField());
		mm.put("VipCardField", new VipCardField());
		mm.put("BonusRuleField", new BonusRuleField());
		return VIP_MemberManagement ;
	}

	/*
	 * 积分历史页面跳转
	 */
	@RequestMapping(value = "/bonusHistory", method = RequestMethod.GET)
	public String bonusHistory() {
		return VIP_BonusHistory;
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/vip.bx
	 */
	// @RequestMapping(method = RequestMethod.GET)
	// public ModelAndView index(@ModelAttribute("SpringWeb") Vip vip, ModelMap mm,
	// HttpSession session) {
	// if (!canCallCurrentAction(session,
	// BaseAction.EnumUserScope.STAFF.getIndex())) {
	// logger.debug("无权访问本Action");
	// return null;
	// }
	//
	// logger.info("进入页面时读取全部的会员类别，vip=" + vip);
	//
	// Company company = getCompanyFromSession(session);
	//
	// VipCategory vipCategory = new VipCategory();
	// vipCategory.setPageIndex(PAGE_StartIndex);
	// vipCategory.setPageSize(PAGE_SIZE_Infinite);
	// DataSourceContextHolder.setDbName(company.getDbName());
	// List<?> ls =
	// vipCategoryBO.retrieveNObject(getStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, vipCategory);
	//
	// logger.info("RetrieveN VIPCategory error code=" +
	// vipCategoryBO.getLastErrorCode());
	//
	// switch (vipCategoryBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("ls=" + ls);
	// mm.put("vip", new VipField());
	// mm.put("vipCategoryList", ls);
	// break;
	// default:
	// logger.info("其他错误");
	// mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// return new ModelAndView(VIP_Index, "", new Vip());
	// }

	/*
	 * URL:http://localhost:8080/nbr/vip/createEx.bx
	 * IDInPOS=-1&POS_SN=1234567&iCID=3208031997070111603X&wechat=b123456&qq=
	 * 44123456&name=ban&email=123456@qq.com&account=q123456&password=123456&
	 * consumeTimes=16&consumeAmount=200&district=广州&category=1&status=0&birthday=
	 * 2016/10/10%2012:10:12&expireDatetime=2016/10/10%2012:10:12&bonus=42&
	 * lastConsumeDatetime=2016/10/10%2012:10:12 User-Agent: Fiddler Host:
	 * localhost:8080 Content-Type: application/x-www-form-urlencoded
	 */
	// @Transactional
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Vip vip, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("创建一个会员，vip" + vip);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			int staffID = getStaffFromSession(session).getID();
			vip.setCategory(BaseAction.DEFAULT_VipCategoryID);
			vip.setCreateDatetime(new Date());
			DataSourceContextHolder.setDbName(company.getDbName());
			Vip vipCreate = null;
			if(vip.getIsImported() == EnumBoolean.EB_NO.getIndex()) {
				vipCreate = (Vip) vipBO.createObject(staffID, BaseBO.INVALID_CASE_ID, vip);
			} else {
				vipCreate = (Vip) vipBO.createObject(staffID, BaseBO.CASE_Vip_ImportFromOldSystem, vip);
			}
			if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建会员失败：" + vipBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, vipBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());
				break;
			}
			// 加入到缓存中
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Vip).write1(vipCreate, company.getDbName(), staffID);

			params.put(BaseAction.KEY_Object, vipCreate);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			// 创建会员卡来源
			VipSource vipSource = new VipSource();
			vipSource.setVipID(vipCreate.getID());
			vipSource.setSourceCode(EnumVipSourceCode.EVSC_WX.getIndex());
			vipSource.setID1(""); // openID
			vipSource.setID2(""); // unionID
			vipSource.setID3(""); // MiniProgramOpenID
			DataSourceContextHolder.setDbName(company.getDbName());
			VipSource vipSourceCreated = (VipSource) vipSourceBO.createObject(staffID, BaseBO.INVALID_CASE_ID, vipSource);
			if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError || vipSourceCreated == null) {
				logger.error("创建会员来源失败：" + vipBO.printErrorInfo()); // 插vip source如果有错，不用中途返回。
			}
			// 创建一张会员卡vipcardcode
			VipCardCode vipCardCode = new VipCardCode();
			vipCardCode.setVipID(vipCreate.getID());
			vipCardCode.setVipCardID(BaseAction.DEFAULT_VipCardID);
			vipCardCode.setCompanySN(company.getSN());
			DataSourceContextHolder.setDbName(company.getDbName());
			VipCardCode vipCardCodeCreated = (VipCardCode) vipCardCodeBO.createObject(staffID, BaseBO.INVALID_CASE_ID, vipCardCode);
			if (vipCardCodeBO.getLastErrorCode() != EnumErrorCode.EC_NoError || vipCardCodeCreated == null) {
				logger.error("创建一张会员卡失败：" + vipCardCodeBO.printErrorInfo(company.getDbName(), vipCardCode));
				params.put(BaseAction.JSON_ERROR_KEY, vipCardCodeBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, vipCardCodeBO.getLastErrorMessage());
				break;
			}
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * URL:http://localhost:8080/nbr/vip/createEx.bx
	 * ID=1&district=广州&category=1&bonus=42 User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded
	 */
	// @Transactional
	// @RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST) 小程序使用此参数时后端无法拿到ID
	@RequestMapping(value = "/updateEx", produces = "application/x-www-form-urlencoded; charset=UTF-8", method = RequestMethod.POST)
	// @RequestMapping(value = "/updateEx", produces = "application/json;
	// charset=UTF-8", method = RequestMethod.POST)
	// @RequestMapping(value = "/updateEx", method = RequestMethod.POST)
	// @RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.GET)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Vip vip, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex() | BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("修改一个会员，vip" + vip);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = getDefaultParamToReturn(true);
		Vip vipCreate;
		do {
			vip.setCategory(VipCategory.DEFAULT_VipCategory_ID);
			DataSourceContextHolder.setDbName(company.getDbName());
			Vip vipInSession = getVipFromSession(session);
			if (vipInSession != null) {
				if (vipInSession.getID() != vip.getID()) {
					if (BaseAction.ENV != BaseAction.EnumEnv.DEV) {
						logger.error("会员" + vipInSession.getID() + "试图更改会员" + vip.getID() + "的资料！");
					}
					return null;
				}
				vipCreate = (Vip) vipBO.updateObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, vip);
			} else {
				vipCreate = (Vip) vipBO.updateObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, vip);
			}
			if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("修改会员失败：" + vipBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, vipBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());
				break;
			}
			// 加入到缓存中
			if (getVipFromSession(session) != null) {
				CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Vip).write1(vipCreate, company.getDbName(), BaseBO.SYSTEM);
			} else {
				CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Vip).write1(vipCreate, company.getDbName(), getStaffFromSession(session).getID());
			}

			params.put(BaseAction.KEY_Object, vipCreate);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * URL:http://localhost:8080/nbr/vip/updateBonusEx.bx
	 * ID=1&staffID=3&bonus=500&remarkFormBonus=因为xxxxx增加500分&manuallyAdded=1
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded
	 */
	// @Transactional
	@RequestMapping(value = "/updateBonusEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateBonusEx(@ModelAttribute("SpringWeb") Vip vip, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("修改一个会员积分，vip" + vip);

		if (vip.getManuallyAdded() != EnumBoolean.EB_Yes.getIndex()) {
			logger.error("黑客行为");
			return null;
		}
		//
		Map<String, Object> params = getDefaultParamToReturn(true);
		Company company = getCompanyFromSession(session);
		vip.setStaffID(getStaffFromSession(session).getID());
		//
		do {
			DataSourceContextHolder.setDbName(company.getDbName());
			Vip vipUpdate = (Vip) vipBO.updateObject(getStaffFromSession(session).getID(), BaseBO.CASE_Vip_UpdateBonus, vip);
			if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("修改会员积分失败：" + vipBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, vipBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());
				break;
			}
			// 加入到缓存中
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Vip).write1(vipUpdate, company.getDbName(), getStaffFromSession(session).getID());

			params.put(BaseAction.KEY_Object, vipUpdate);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:http://localhost:8080/nbr/vip/retrieve1Ex.bx?ID=1
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Vip vip, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex() | BaseAction.EnumUserScope.VIP.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("读取一个会员,vip" + vip);

		Company company = getCompanyFromSession(session);

		Map<String, Object> params = new HashMap<String, Object>();
		int staffOrVipID = 0;
		if(getVipFromSession(session) != null) {
			staffOrVipID = BaseBO.SYSTEM;
		} else {
			staffOrVipID = getStaffFromSession(session).getID();
		}
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = null;
		DataSourceContextHolder.setDbName(company.getDbName());
		if (!vipBO.checkRetrieve1Permission(staffOrVipID, INVALID_ID, null)) {

			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission);

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} else {
			bm = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Vip).read1(vip.getID(), staffOrVipID, ecOut, company.getDbName());
		}

		switch (ecOut.getErrorCode()) {
		case EC_NoError:
			if (bm != null) {
				logger.info("bm=" + bm.toString());
				Vip vipBm = (Vip) bm;
				VipCardCode vipCardCode = retreveNVipCardCodeByVipID(vipBm.getID(), company.getDbName(), params, staffOrVipID);
				if (vipCardCode == null) {
					params.put(KEY_HTMLTable_Parameter_msg, "没有查询到该会员的会员卡");
					return null;
				}
				vipBm.setVipCardSN(vipCardCode.getSN());
			}

			params.put("vip", bm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
			logger.info("返回的数据=" + params);
			break;
		case EC_NoPermission:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * URL:http://localhost:8080/nbr/vip/retrieveNEx.bx?IDInPOS=-1&district=&
	 * category=-1&status=0&iCID=&wechat=&qq=&email=&account=
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Vip vip, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("读取多个的会员，vip" + vip);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<Vip> vipList = (List<Vip>) vipBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, vip);

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (vipBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！vipList=" + vipList);
			for (Vip v : vipList) {
				v.setSyncDatetime(new Date());

				VipCategory vipCategory = new VipCategory();
				vipCategory.setID(v.getCategory());
				DataSourceContextHolder.setDbName(company.getDbName());
				VipCategory retrieve1VipCategory = (VipCategory) vipCategoryBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, vipCategory);
				if (vipCategoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("读取失败，错误码=" + vipCategoryBO.getLastErrorCode().toString());

					params.put(BaseAction.JSON_ERROR_KEY, vipCategoryBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, vipCategoryBO.getLastErrorMessage().toString());
					break;
				}
				//
				v.setVipCategoryName(retrieve1VipCategory.getName());
			}

			params.put(BaseAction.KEY_ObjectList, vipList);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, vipBO.getTotalRecord());
			params.put(KEY_HTMLTable_Parameter_code, "0");
			params.put(KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage().toString());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage().toString());
			break;
		}
		logger.info("返回的值=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * IDInPOS=-1&POS_SN=1234567&iCID=3208031997070111603X&wechat=b123456&qq=
	 * 44123456&name=ban&email=123456@qq.com&account=q123456&password=123456&
	 * consumeTimes=16&consumeAmount=200&district=广州&category=1&status=0&birthday=
	 * 2016/10/10%2012:10:12&expireDatetime=2016/10/10%2012:10:12&point=42&
	 * lastConsumeDatetime=2016/10/10%2012:10:12
	 * URL:http://localhost:8080/nbr/vip/toCreate.bx
	 */
	// @RequestMapping(value = "/toCreate", method = RequestMethod.GET)
	// public ModelAndView toCreate(@ModelAttribute("SpringWeb") Vip vip, ModelMap
	// mm, HttpSession session) {
	// if (!canCallCurrentAction(session,
	// BaseAction.EnumUserScope.STAFF.getIndex())) {
	// logger.debug("无权访问本Action");
	// return null;
	// }
	//
	// logger.info("进行toCreate()时读取全部的会员类别，vip=" + vip);
	//
	// VipCategory vipCategory = new VipCategory();
	// vipCategory.setPageIndex(PAGE_StartIndex);
	// vipCategory.setPageSize(PAGE_SIZE_Infinite);
	// List<?> ls =
	// vipCategoryBO.retrieveNObject(getStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, vipCategory);
	//
	// logger.info("RetrieveN VIPCategory error code=" +
	// vipCategoryBO.getLastErrorCode());
	//
	// switch (vipCategoryBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("查询成功，ls=" + ls);
	// mm.put("vip", vip);
	// mm.put("vipCategoryList", ls);
	// break;
	// default:
	// logger.info("其他错误");
	// mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// return new ModelAndView(VIP_ToCreate, "", new Vip());
	// }

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/vip/deleteListEx.bx?vipListID=8,9
	 */
	// @Transactional
	// @RequestMapping(value = "/deleteListEx", method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteListEx(@ModelAttribute("SpringWeb") Vip vip, ModelMap
	// model, HttpServletRequest req) {
	// String sVipIDs = GetStringFromRequest(req, "vipListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sVipIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// return "";
	// }
	//
	// logger.info("sVipIDs=" + sVipIDs);//
	//
	// int[] iArrVipID = toIntArray(sVipIDs);
	// if (iArrVipID == null) {
	// return "";
	// }
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	//
	// for (int iID : iArrVipID) {
	// Vip vips = new Vip();
	// vips.setID(iID);
	// vipBO.deleteObject(BaseBO.CURRENT_STAFF.getID(), BaseBO.INVALID_CASE_ID,
	// vips);
	// logger.info("Delete vip error code=" + vipBO.getLastErrorCode());
	//
	// if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// // params.put(BaseAction.JSON_ERROR_KEY, vipBO.getLastErrorCode());
	// //
	// // return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// throw new RuntimeException("DB处理异常");
	// }
	//
	// CacheManager.getCache(EnumCacheType.ECT_Vip).delete1(vips);
	// }
	// logger.info("删除成功");
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * sString1=13545678110 URL:
	 * http://localhost:8080/nbr/vip/retrieveNByFieldsEx.bx 根据vip的以下属性进行精确搜索：手机号码
	 */
	@RequestMapping(value = "/retrieveNByMobileOrVipCardSNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNByMobileOrVipCardSNEx(@ModelAttribute("SpringWeb") Vip vip, ModelMap mm, HttpServletRequest req, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("根据手机号码或者会员卡卡号查询会员,vip=" + vip);
		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> vipList = vipBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.CASE_Vip_RetrieveNByMobileOrVipCardSN, vip);
		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (vipBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功，vipList=" + vipList);
			params.put(BaseAction.KEY_ObjectList, vipList);
			params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, vipBO.getTotalRecord());
			params.put(BaseAction.KEY_HTMLTable_Parameter_code, "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_WrongFormatForInputField:
			logger.info("字段验证不通过！");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());

		logger.info("返回的值=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/vip/retrieveNVipConsumeHistoryEx.bx?ID=1
	 * 根据vip的ID查询零售单
	 */
	@RequestMapping(value = "/retrieveNVipConsumeHistoryEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNVipConsumeHistoryEx(@ModelAttribute("SpringWeb") Vip vip, ModelMap mm, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("根据vip的ID查询零售单,vip=" + vip);
		// 返回的零售单和零售单商品信息

		Company company = getCompanyFromSession(req.getSession());

		DataSourceContextHolder.setDbName(company.getDbName());
		List<List<BaseModel>> retailTradeList = vipBO.retrieveNObjectEx(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Vip_RetrieveNVipConsumeHistory, vip);

		logger.info("retrieveNVipConsumeHistory vip error code=" + vipBO.getLastErrorCode());

		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (vipBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功，retailTradeList=" + retailTradeList);
			List<RetailTrade> rtList = new ArrayList<RetailTrade>();

			for (BaseModel bm : retailTradeList.get(0)) { // 遍历零售单
				RetailTrade rt = (RetailTrade) bm;

				List<RetailTradeCommodity> rtcList = new ArrayList<RetailTradeCommodity>();
				for (BaseModel bm2 : retailTradeList.get(1)) { // 遍历零售单商品 //TODO PET-2279
					RetailTradeCommodity rtc = (RetailTradeCommodity) bm2;
					if (rtc != null && rtc.getTradeID() == rt.getID()) {
						rtcList.add(rtc);
					} else if (rtc == null) {
						logger.info("零售单从表为null！此时主表状态应该为" + EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity + "，实际上是：" + EnumStatusRetailTrade.values()[rt.getStatus()]);
					}
				}

				rt.setListSlave1(rtcList);
				rtList.add(rt);
			}

			params.put("retailTradeList", rtList);
			params.put("count", vipBO.getTotalRecord());
			params.put("msg", "");
			params.put("code", "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoSuchData:
			logger.info("VipID不存在！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData.toString());
			break;
		case EC_NoPermission:
			logger.info("没有操作权限！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());
		logger.info("返回的对象=" + params);

		logger.info("返回的值=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * http://localhost:8080/nbr/vip/retrieveNToCheckUniqueFieldEx.bx
	 */
	@RequestMapping(value = "/retrieveNToCheckUniqueFieldEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNToCheckUniqueFieldEx(@ModelAttribute("SpringWeb") Vip vip, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		Company company = getCompanyFromSession(session);
		// 选传参数：ID，必传参数：string1和fieldToCheckUnique
		// 如果是要进行修改操作,则要传对应的ID参数，不然即使不做修改，也会报已存在该字段的错误信息
		// string1存储的是要检查的字段值，fieldToCheckUnique如下描述
		// fieldToCheckUnique=1，代表要检查手机号
		// fieldToCheckUnique=2，代表要检查身份证号
		// fieldToCheckUnique=3，代表要检查微信号
		// fieldToCheckUnique=4,代表要检查QQ号
		// fieldToCheckUnique=5,代表要检查邮箱
		// fieldToCheckUnique=6,代表要检查登录账号
		return doRetrieveNToCheckUniqueFieldEx(false, vip, company.getDbName(), session);
	}

	/**
	 * 用于微信小程序的登录 url: https://localhost:8888/vip/loginEx.bx
	 * 
	 * @throws Exception
	 */
	// @RequestMapping(value = "/loginEx", method = { RequestMethod.POST }, produces
	// = "text/plain;charset=UTF-8") //
	// @ResponseBody
	// public String loginEx(HttpSession session, @RequestBody Vip vipFromRequset)
	// throws Exception {
	// logger.debug("vipBO hash =" + vipBO.hashCode());
	//
	// // TODO 先判断会员，再判断是否带有商家编号。
	// Map<String, Object> params = new HashMap<String, Object>();
	// Vip vip = new Vip();
	// vip.setName("刘德华");
	// vip.setSn("8888 8888 8888 6666");
	// Company cpn = new Company();
	// cpn.setName("博昕虚拟门店");
	// cpn.setSN("668888");
	// params.put(BaseAction.KEY_Object, vip);
	// params.put(BaseAction.KEY_Object2, cpn);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// params.put(KEY_HTMLTable_Parameter_msg, "");
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * sString1=13545678110 URL:
	 * http://localhost:8080/nbr/vip/retrieveNByFieldsEx.bx 根据vip的以下属性进行精确搜索：手机号码
	 */
	// @RequestMapping(value = "/retrieveNByMobileOrCardCodeEx", produces =
	// "plain/text; charset=UTF-8", method = RequestMethod.POST)
	// @ResponseBody
	// public String retrieveNByMobileOrCardCodeEx(@ModelAttribute("SpringWeb") Vip
	// vip, ModelMap mm, HttpServletRequest req, HttpSession session) {
	// if (!canCallCurrentAction(session,
	// BaseAction.EnumUserScope.STAFF.getIndex())) {
	// logger.debug("无权访问本Action");
	// return null;
	// }
	//
	// logger.info("查询会员相关信息,vip=" + vip);
	// //
	// Map<String, Object> params = getDefaultParamToReturn(true);
	// Company company = getCompanyFromSession(session);
	// Staff staff = (Staff)
	// session.getAttribute(EnumSession.SESSION_Staff.getName());
	// //
	// DataSourceContextHolder.setDbName(company.getDbName());
	// List<?> bmList = vipBO.retrieveNObject(staff.getID(),
	// BaseBO.CASE_Vip_RetrieveNByMobileOrCardCode, vip);
	// if (vipBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("查询会员相关信息失败，错误码=" + vipBO.printErrorInfo());
	// params.put(BaseAction.JSON_ERROR_KEY, vipBO.getLastErrorCode().toString());
	// params.put(KEY_HTMLTable_Parameter_msg, vipBO.getLastErrorMessage());
	// } else {
	// logger.info("查询会员相关信息成功,poUpdate=" + bmList);
	// params.put(KEY_ObjectList, bmList);
	// params.put("msg", "");
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// }
	// logger.info("返回的值=" + params);
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	@Override
	protected BaseBO getBO() {
		return vipBO;
	}
	
	@SuppressWarnings("unchecked")
	private VipCardCode retreveNVipCardCodeByVipID(int vipID, String dbName, Map<String, Object> params, int staffOrVipID) {
		VipCardCode queryVipCardCode = new VipCardCode();
		queryVipCardCode.setPageIndex(BaseAction.PAGE_StartIndex);
		queryVipCardCode.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		queryVipCardCode.setVipID(vipID);

		DataSourceContextHolder.setDbName(dbName);
		List<VipCardCode> vipCardCodeList = (List<VipCardCode>) vipCardCodeBO.retrieveNObject(staffOrVipID, BaseBO.INVALID_CASE_ID, queryVipCardCode);
		if (vipCardCodeBO.getLastErrorCode() != EnumErrorCode.EC_NoError || vipCardCodeList == null) {
			logger.error("查询会员卡！错误信息：" + vipCardCodeBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, vipCardCodeBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, vipCardCodeBO.getLastErrorMessage());
			logger.info("返回的数据=" + params);
			return null;
		}
		return (vipCardCodeList.size() == 0 ? null : vipCardCodeList.get(0));
	}
}
