package com.bx.erp.action.purchasing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.RoleBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.ShopDistrictBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CategoryParentBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.action.bo.purchasing.ProviderDistrictBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderCommodityBO;
import com.bx.erp.action.wx.WxAction;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role;
import com.bx.erp.model.Shop;
import com.bx.erp.model.ShopDistrict;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Staff;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityField;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.model.purchasing.ProviderDistrict;
import com.bx.erp.model.purchasing.ProviderField;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//@Transactional
@Controller
@RequestMapping("/purchasingOrder")
@Scope("prototype")
public class PurchasingOrderAction extends BaseAction {
	private Log logger = LogFactory.getLog(PurchasingOrderAction.class);

	@Value("${public.account.appid}")
	private String PUBLIC_ACCOUNT_APPID;

	@Value("${public.account.secret}")
	private String PUBLIC_ACCOUNT_SECRET;

	@Value("${get.accesstoken.url}")
	private String GET_ACCESSTOKEN_URL; // 获取访问微信端的token接口

	@Value("${get.sendTemplateMsg.url}")
	private String GET_SENDTEMPLATEMSG_URL; // 发送模板消息的接口

	@Resource
	private PurchasingOrderBO purchasingOrderBO;

	@Resource
	private BarcodesBO barcodesBO;

	@Resource
	private CategoryBO categoryBO;

	@Resource
	private ProviderDistrictBO providerDistrictBO;

	@Resource
	private PurchasingOrderCommodityBO purchasingOrderCommodityBO;

	@Resource
	private CommodityBO commodityBO;

	@Resource
	private ProviderCommodityBO providerCommodityBO;

	@Resource
	private CategoryParentBO categoryParentBO;

	@Resource
	private PackageUnitBO packageUnitBO;

	@Resource
	private MessageBO messageBO;

	@Resource
	private ShopBO shopBO;

	@Resource
	private RoleBO roleBO;
	
	@Resource
	private ShopDistrictBO shopDistrictBO;

	@Value("${wx.templateid.successCreatePurchasingOrder}")
	private String WXMSG_TEMPLATEID_createPurchasingOrder;

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Fiddler URL:
	 * http://localhost:8080/nbr/purchasingOrder.bx
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrder, ModelMap mm, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("进入采购订单主页时加载多个采购订单的信息,purchasingOrder=" + purchasingOrder);

		Company company = getCompanyFromSession(session);

		//
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> ls = purchasingOrderBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, purchasingOrder);

		logger.info("RetrieveN purchasingorder error code=" + purchasingOrderBO.getLastErrorCode());

		switch (purchasingOrderBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("ls:" + ls);
			mm.put("providerList", ls);
			break;
		default:
			logger.info("其他错误！");
			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		return new ModelAndView(PURCHASINGORDER_Index, "", null);
	}

	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public String order(HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		return PURCHASINGORDER_Order;
	}

	@RequestMapping(value = "/region", method = RequestMethod.GET)
	public String region(HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		return PURCHASINGORDER_Region;
	}

	@RequestMapping(value = "/providerProfile", method = RequestMethod.GET)
	public String providerProfile(HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		return PURCHASINGORDER_ProviderProfile;
	}
	//
	// /*
	// * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	// * application/x-www-form-urlencoded GET
	// * :http://localhost:8080/nbr/purchasingOrder/approveListEx.bx?POListID=2,3
	// */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/approveListEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String approveListEx(@ModelAttribute("SpringWeb") PurchasingOrder
	// purchasingOrder, ModelMap mm, HttpServletRequest req) {
	// logger.info("审核多个采购订单，purchasingOrder=" + purchasingOrder.toString());
	//
	// Company company = getCompanyFromSession(req.getSession());
	// String dbName = company.getDbName();
	//
	// Map<String, Object> params = getDefaultParamToReturn(true);
	// do {
	// String sPOIDs = GetStringFromRequest(req, "POListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sPOIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// logger.info("参数异常");
	// return "";
	// } else {
	// logger.info("sPOIDs=" + sPOIDs);
	// }
	//
	// int[] iArrPOID = toIntArray(sPOIDs);
	// if (iArrPOID == null) {
	// logger.info("参数缺失");
	// return "";
	// } else {
	// logger.info("iArrPOID=" + iArrPOID);
	// }
	//
	// logger.info("调用staffSession得到相关采购订单的审核人信息");
	// HttpSession session = req.getSession();
	// Staff staff = (Staff)
	// session.getAttribute(EnumSession.SESSION_Staff.getName());
	// if (staff == null) {
	// logger.info("得到的staffSession为空");
	// return "";
	// } else {
	// logger.info("得到的staffSession=" + staff.toString());
	// }
	//
	// boolean hasDBError = false;
	// StringBuilder sbErrorMsg = new StringBuilder();
	// for (int iID : iArrPOID) { // 如果审核过程中出现错误，会记录出错的地方及出错信息，并继续向后审核，直到全部审核完成
	// PurchasingOrder po = new PurchasingOrder();
	// po.setID(iID);
	// po.setApproverID(staff.getID());
	// DataSourceContextHolder.setDbName(dbName);
	// BaseModel bm =
	// purchasingOrderBO.updateObject(getStaffFromSession(session).getID(),
	// BaseBO.CASE_ApprovePurchasingOrder, po);
	// switch (purchasingOrderBO.getLastErrorCode()) {
	// case EC_BusinessLogicNotDefined:
	// hasDBError = true;
	// sbErrorMsg.append("采购订单" + iID + "审核失败，因为它已经审核过了。<br />");
	// break;
	// case EC_NoError:
	// CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).write1(bm,
	// dbName, getStaffFromSession(session).getID());// ...rollback后，这里的普存并没有恢复
	//
	// ErrorInfo ecOut = new ErrorInfo();
	// po = (PurchasingOrder) CacheManager.getCache(dbName,
	// EnumCacheType.ECT_PurchasingOrder).read1(iID,
	// getStaffFromSession(session).getID(), ecOut, dbName);
	// if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("查询一个采购订单失败，错误码=" + ecOut.getErrorCode());
	// params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
	// params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage().toString());
	// hasDBError = true;
	// break;
	// } else {
	// logger.info("查询一个采购订单成功！bm=" + (bm == null ? "NULL" : bm.toString()));
	// }
	//
	// PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
	// poc.setPurchasingOrderID(po.getID());
	// DataSourceContextHolder.setDbName(dbName);
	// List<PurchasingOrderCommodity> listPOC = (List<PurchasingOrderCommodity>)
	// purchasingOrderCommodityBO.retrieveNObject(getStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, poc);
	// if (purchasingOrderCommodityBO.getLastErrorCode() !=
	// EnumErrorCode.EC_NoError) {
	// logger.info("retrieve N purchasingOrder error code=" +
	// purchasingOrderCommodityBO.getLastErrorCode());
	// params.put(BaseAction.JSON_ERROR_KEY,
	// purchasingOrderCommodityBO.getLastErrorCode().toString());
	// hasDBError = true;
	// break;
	// } else {
	// logger.info("查询多个采购订单商品成功，listPOC=" + listPOC.toString());
	// }
	//
	// hasDBError = createProviderCommodity(params, hasDBError, po, listPOC, dbName,
	// req);
	//
	// break;
	// case EC_NoPermission:
	// hasDBError = true;
	// logger.info("你没有权限审核采购订单");
	// sbErrorMsg.append("你没有权限审核采购订单。<br />");
	// break;
	// case EC_OtherError:
	// default:
	// sbErrorMsg.append("采购订单" + iID + "审核失败，数据库发生错误。<br />");
	// hasDBError = true;
	// break;
	// } // switch
	// } // for
	// params.put(KEY_HTMLTable_Parameter_msg, sbErrorMsg.toString());
	//
	// if (hasDBError) {
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);//
	// 前端必须显示msg给用户看
	// break;
	// }
	//
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError); //
	// 前端如果检查到msg为空，则提示成功即可，如果msg不为空，需要显示msg给用户看
	// } while (false);
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/**
	 * 前端可能更新了采购商品的供应商，这时应该作相应处理。处理办法是创建新的采购订单关联，旧的关联不删除。如果发生错误，在调用本函数后再设置error msg
	 */
	private boolean createProviderCommodity(Map<String, Object> params, int providerID, List<PurchasingOrderCommodity> listPOC, String dbName, HttpServletRequest req) {
		logger.info("更新采购商品的供应商...");

		ProviderCommodity pc = new ProviderCommodity();
		for (PurchasingOrderCommodity POC : listPOC) {
			pc.setCommodityID(POC.getCommodityID());
			pc.setProviderID(providerID);
			DataSourceContextHolder.setDbName(dbName);
			ProviderCommodity pc1 = (ProviderCommodity) providerCommodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, pc);
			if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建一个供应商商品失败：" + providerCommodityBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, providerCommodityBO.getLastErrorCode().toString());
				return false;
			} else {
				logger.info("创建一个供应商商品成功=" + pc1);
			}
		}

		return true;
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * purchasingPlanTableID=0
	 * POST:URL:http://localhost:8080/nbr/purchasingOrder/toCreate.bx
	 */
	@RequestMapping(value = "/toCreate", method = RequestMethod.GET)
	public String toCreate(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrder, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("进入采购订单读取所有的类别，所有的供应区域,purchasingOrder=" + purchasingOrder);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		Category category = new Category();
		category.setPageIndex(PAGE_StartIndex);
		category.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		//
		List<?> categoryList = categoryBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, category);

		CategoryParent cp = new CategoryParent();
		cp.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		//
		List<?> categoryParentList = categoryParentBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, cp);

		ProviderDistrict providerDistrict = new ProviderDistrict();
		providerDistrict.setPageIndex(PAGE_StartIndex);
		providerDistrict.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		//
		List<?> pdList = providerDistrictBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, providerDistrict);

		logger.info("categoryList=" + categoryList);
		logger.info("categoryParentList=" + categoryParentList);
		logger.info("pdList=" + pdList);
		mm.put("categoryList", categoryList);
		mm.put("categoryParentList", categoryParentList);
		mm.put("pdList", pdList);

		return PURCHASINGORDER_ToCreate;
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * purchasingPlanTableID=1&&commIDs=1&&commNOs=4&&commPrices=11.1&
	 * commPurchasingUnit=桶&providerID=2
	 * POST:URL:http://localhost:8080/nbr/purchasingOrder/createEx.bx
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrderIn, ModelMap model, HttpServletRequest req) throws CloneNotSupportedException {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("创建一个采购订单，purchasingOrderIn=" + purchasingOrderIn);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			// 解析用户输入
			List<PurchasingOrderCommodity> pocList = parsePurchasingOrderCommodityList(purchasingOrderIn, req, dbName);
			if (pocList == null) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("黑客行为");
				}
				return null;
			}
			// 检查输入合法性
			if (!checkCreate(req, dbName, params, pocList)) {
				break;
			}

			Staff staff = getStaffFromSession(req.getSession());
			if(staff.getShopID() != 1 && staff.getShopID() != purchasingOrderIn.getShopID()) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "不能跨门店创建采购单");
				break;
			}
			//
			// 创建采购订单和采购订单商品
			PurchasingOrder purchasingOrder = createMasterAndSalve(purchasingOrderIn, dbName, staff.getID(), params, pocList);
			if (purchasingOrder == null) {
				break;
			}

			// 更新缓存
			CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).write1(purchasingOrder, dbName, staff.getID());

			//
			purchasingOrder = (PurchasingOrder) purchasingOrder.clone(); // 下面的代码会更改purchasingOrder然后返回给用户。缓存中不需要保存provider/commodityList等信息

			// 加载采购订单的商品列表
			if (!loadCommodityList(purchasingOrder, dbName, req.getSession())) {
				logger.info("已经成功生成一张采购订单，但加载部分商品信息失败!");
				params.put(KEY_HTMLTable_Parameter_msg, params.get(KEY_HTMLTable_Parameter_msg).toString() + "\t创建采购订单成功，但加载部分商品信息失败!");
				break;
			}

			// 加载采购订单的供应商
			if (!loadProvider(purchasingOrder, dbName, req.getSession())) {
				logger.info("已经成功生成一张采购订单，但加载部分供应商信息失败!");
				params.put(KEY_HTMLTable_Parameter_msg, params.get(KEY_HTMLTable_Parameter_msg).toString() + "\t创建采购订单成功，但加载部分供应商信息失败!");
				break;
			}

			logger.info("purchasingOrder=" + purchasingOrder);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, 1);// ...
			params.put(KEY_Object, purchasingOrder);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "创建采购订单成功");
			// 发送微信公众号消息给客户
			sendMsgToWx(staff, company, purchasingOrder, params, dbName);
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	protected List<PurchasingOrderCommodity> parsePurchasingOrderCommodityList(PurchasingOrder purchasingOrder, HttpServletRequest req, String dbName) {
		String sCommIDs = GetStringFromRequest(req, PurchasingOrderCommodity.field.getFIELD_NAME_commIDs(), String.valueOf(BaseAction.INVALID_ID)).trim();
		String sNOs = GetStringFromRequest(req, PurchasingOrderCommodity.field.getFIELD_NAME_NOs(), String.valueOf(BaseAction.INVALID_ID)).trim();
		String spriceSuggestions = GetStringFromRequest(req, PurchasingOrderCommodity.field.getFIELD_NAME_priceSuggestions(), String.valueOf(BaseAction.INVALID_ID)).trim();
		String sBarcodeIDs = GetStringFromRequest(req, PurchasingOrderCommodity.field.getFIELD_NAME_barcodeIDs(), String.valueOf(BaseAction.INVALID_ID)).trim();
		//
		if (sCommIDs.equals(String.valueOf(BaseAction.INVALID_ID)) || sNOs.equals(String.valueOf(BaseAction.INVALID_ID)) || spriceSuggestions.equals(String.valueOf(BaseAction.INVALID_ID))
				|| sBarcodeIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("参数缺失，当黑客行为处理");
			}
			return null;
		} else {
			logger.info("所有参数都符合要求，sCommIDs=" + sCommIDs + "，sCommNOs=" + sNOs + "，sCommPrices=" + "，" + "，sBarcodeIDs=" + sBarcodeIDs);
		}
		//
		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral maxPurchasingOrderCommodityNO = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.MaxPurchasingOrderCommodityNO, getStaffFromSession(req.getSession()).getID(), ecOut,
				dbName);
		Integer[] iaCommID = GeneralUtil.toIntArray(sCommIDs);
		Integer[] iaCommNO = GeneralUtil.toIntArray(sNOs);
		Integer[] iaBarcodeID = GeneralUtil.toIntArray(sBarcodeIDs);
		Double[] iaCommPrices = GeneralUtil.toDoubleArray(spriceSuggestions);
		if (iaCommID == null || iaBarcodeID == null || iaCommNO == null || iaCommPrices == null || iaCommID.length != iaCommNO.length || iaCommID.length != iaBarcodeID.length || iaCommID.length != iaCommPrices.length || iaCommID.length == 0
				|| iaCommID.length > Integer.parseInt(maxPurchasingOrderCommodityNO.getValue())) {
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("参数缺失，当黑客行为处理");
			}
			return null;
		} else {
			logger.info("iaCommID=" + GeneralUtil.printArray(iaCommID) + "，iaCommNO=" + GeneralUtil.printArray(iaCommNO) + "，iaBarcodeID=" + GeneralUtil.printArray(iaBarcodeID) + "，iaCommPrices=" + GeneralUtil.printArray(iaCommPrices));
		}

		// 一旦发现iaCommID内部有重复元素，当黑客行为处理
		if (GeneralUtil.hasDuplicatedElement(iaCommID)) {
			logger.error("黑客传递的参数有重复！");
			return null;
		}

		List<PurchasingOrderCommodity> listPOC = new ArrayList<PurchasingOrderCommodity>();
		for (int i = 0; i < iaCommID.length; i++) {
			PurchasingOrderCommodity p = new PurchasingOrderCommodity();
			p.setPurchasingOrderID(purchasingOrder.getID());
			p.setCommodityID(iaCommID[i]);
			p.setCommodityNO(iaCommNO[i]);
			p.setPriceSuggestion(iaCommPrices[i]);
			p.setBarcodeID(iaBarcodeID[i]);

			listPOC.add(p);
		}
		return listPOC;
	}

	private boolean checkCreate(HttpServletRequest req, String dbName, Map<String, Object> params, List<PurchasingOrderCommodity> pocList) {
		Commodity commRetrieved = null;
		ErrorInfo eiOut = new ErrorInfo();
		for (PurchasingOrderCommodity poc : pocList) {
			String err = poc.checkCreate(BaseBO.CASE_CheckCreateForAction); // 先开始检查从表，保证主表创建成功后从表也能正常创建，保证数据不会出现有主表没从表或从表少了。
			if (!err.equals("")) {
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
				params.put(KEY_HTMLTable_Parameter_msg, err);
				return false;
			}

			DataSourceContextHolder.setDbName(dbName);
			commRetrieved = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(poc.getCommodityID(), getStaffFromSession(req.getSession()).getID(), eiOut, dbName);
			if (commRetrieved == null || eiOut.getErrorCode() != EnumErrorCode.EC_NoError) {// 有可能该商品是存在的，只是读取时发生了DB异常，但这种情况几乎不会出现，所以认为是商品不存在
				logger.error("不能采购一个不存在的商品：CommodityID=" + poc.getCommodityID());
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(KEY_HTMLTable_Parameter_msg, "不能采购一个不存在的商品");
				return false;
			} else {
				logger.info("查询商品成功=" + commRetrieved);
				if (commRetrieved.getType() != CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
					logger.error("不能采购单品以外的商品：" + commRetrieved);
					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
					params.put(KEY_HTMLTable_Parameter_msg, "不能采购单品以外的商品（商品" + commRetrieved.getName() + "的类型不是单品）");
					return false;
				}
			}

			DataSourceContextHolder.setDbName(dbName);
			Barcodes barcodesRetrieve1 = (Barcodes) CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).read1(poc.getBarcodeID(), getStaffFromSession(req.getSession()).getID(), eiOut, dbName);
			if (barcodesRetrieve1 == null || eiOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("该条形码不存在：BarcodeID=" + poc.getBarcodeID());
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(KEY_HTMLTable_Parameter_msg, "该条形码不存在");
				return false;
			}
			if (barcodesRetrieve1.getCommodityID() != poc.getCommodityID()) {
				logger.error("条形码ID与商品实际条形码ID不对应：BarcodeID=" + poc.getBarcodeID() + ",CommodityID=" + poc.getCommodityID());
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(KEY_HTMLTable_Parameter_msg, "条形码与商品实际条形码不对应");
				return false;
			}
		}

		return true;
	}

	private PurchasingOrder createMasterAndSalve(PurchasingOrder purchasingOrderIn, String dbName, int staffID, Map<String, Object> params, List<PurchasingOrderCommodity> pocList) {
		// 创建采购单
		purchasingOrderIn.setStaffID(staffID);
		DataSourceContextHolder.setDbName(dbName);
		//
		PurchasingOrder purchasingOrder = (PurchasingOrder) purchasingOrderBO.createObject(staffID, BaseBO.INVALID_CASE_ID, purchasingOrderIn);
		if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("创建采购订单失败。" + purchasingOrderBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, purchasingOrderBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("创建采购订单成功" + purchasingOrder);
		}

		// 创建采购订单商品list
		List<PurchasingOrderCommodity> listPOC = new ArrayList<PurchasingOrderCommodity>();
		PurchasingOrderCommodity p = new PurchasingOrderCommodity();
		boolean bPartSuccess = false;
		for (int i = 0; i < pocList.size(); i++) {
			p = pocList.get(i);
			p.setPurchasingOrderID(purchasingOrder.getID());
			DataSourceContextHolder.setDbName(dbName);
			//
			PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityBO.createObject(staffID, BaseBO.INVALID_CASE_ID, p);
			if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("创建失败。" + purchasingOrderCommodityBO.printErrorInfo());
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("创建部分的采购订单商品失败,请运营删除相应的采购订单,ID=" + purchasingOrder.getID());
				}
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "创建采购订单后增加采购商品时发生错误，请重新增加采购商品。");
				bPartSuccess = true;
				break;
			} else {
				logger.info("创建成功，bm=" + pocCreate);
			}
			listPOC.add(pocCreate);
		}
		if (bPartSuccess) {
			return null;
		}

		// 建立主从表关系
		purchasingOrder.setListSlave1(listPOC);

		return purchasingOrder;
	}

	@SuppressWarnings("unchecked")
	private void sendMsgToWx(Staff staff, Company company, PurchasingOrder purchasingOrder, Map<String, Object> params, String dbName) {
		if (purchasingOrder == null || purchasingOrder.getMessageID() == 0) {
			logger.error("发送采购订单生成的消息失败；获取到的消息数据有异常, 消息未创建！");
			params.put(BaseWxModel.WX_ERRCODE, BaseWxModel.WXTest_ERRCODE_Failed);
			params.put(BaseWxModel.WX_ERRMSG, "发送采购订单生成的消息失败；获取到的消息ID为空, 消息未创建！");
			params.put(KEY_HTMLTable_Parameter_msg, params.get(KEY_HTMLTable_Parameter_msg).toString() + "\t发送微信消息失败!");
			return;
		}

		Role role = new Role();
		role.setID(EnumTypeRole.ETR_Boss.getIndex()); // ...查询门店老板
		DataSourceContextHolder.setDbName(dbName);
		List<?> list = roleBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_Role_RetrieveAlsoStaff, role);
		if (roleBO.getLastErrorCode() != EnumErrorCode.EC_NoError || list.get(1) == null) {
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("发送采购订单生成的消息失败；查询门店老板失败或老板未绑定微信公众号！");
			}
			params.put(BaseWxModel.WX_ERRCODE, BaseWxModel.WXTest_ERRCODE_Failed);
			params.put(BaseWxModel.WX_ERRMSG, "发送采购订单生成的消息失败；查询门店老板失败或老板未绑定微信公众号！");
			params.put(KEY_HTMLTable_Parameter_msg, params.get(KEY_HTMLTable_Parameter_msg).toString() + "\t发送微信消息失败!");
			return;
		}

		List<Staff> slist = (List<Staff>) list.get(1);
		Message msg = null;
		boolean hasBossBindWx = false;
		for (Staff staffBoss : slist) {
			if (staffBoss.getOpenid() == null || staff.getShopID() != staffBoss.getShopID()) {
				logger.debug("用户是在哪个门店进行创建采购订单，则发送微信消息给哪个门店的老板，不会发送给其他门店的老板，并且此老板可能未绑定微信，忽略发送给" + staffBoss);
				continue;
			}

			Message msg1 = new Message();
			msg1.setID(purchasingOrder.getMessageID());
			DataSourceContextHolder.setDbName(dbName);
			msg = (Message) messageBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, msg1);
			if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError || msg == null) {
				logger.error("发送采购订单生成的消息失败；查询采购订单生成的消息失败！" + messageBO.printErrorInfo());
				params.put(BaseWxModel.WX_ERRCODE, BaseWxModel.WXTest_ERRCODE_Failed);
				params.put(BaseWxModel.WX_ERRMSG, "发送采购订单生成的消息失败。查询采购订单生成的消息失败！" + messageBO.printErrorInfo());
				params.put(KEY_HTMLTable_Parameter_msg, params.get(KEY_HTMLTable_Parameter_msg).toString() + "\t发送微信消息失败!");
				return;
			}
			logger.info("查询出来的Message：" + msg);
			if (msg.getParameter() == null || "".equals(msg.getParameter())) {
				logger.error("查询创建出来的Message的消息内容为空，Message=" + msg);
				params.put(BaseWxModel.WX_ERRCODE, BaseWxModel.WXTest_ERRCODE_Failed);
				params.put(BaseWxModel.WX_ERRMSG, "查询创建出来的Message的消息内容为空:Message为" + msg);
				params.put(KEY_HTMLTable_Parameter_msg, params.get(KEY_HTMLTable_Parameter_msg).toString() + "\t发送微信消息失败!");
				return;
			}
			logger.info("需要发送的消息： " + msg.getParameter());
			hasBossBindWx = true;
			break;// 找到一个消息即可，这个消息是发送给所有老板的
		}
		if (!hasBossBindWx) {
			logger.debug("说明该门店的老板都没有绑定微信,就不需要发送微信消息");
			return;
		}

		// 解析消息
		JSONArray jsonArray = JSONArray.fromObject(msg.getParameter());
		String purchasingOrderWxMsg = (String) jsonArray.getJSONObject(1).get("Link1_Tag");

		// 向门店老板进行发送微信消息
		boolean bSendWxMsgSuccess = false;
		for (Staff staffBoss : slist) {
			if (staffBoss.getOpenid() == null || "".equals(staffBoss.getOpenid()) || staff.getShopID() != staffBoss.getShopID()) {
				logger.debug("用户是在哪个门店进行创建采购订单，则发送微信消息给哪个门店的老板，不会发送给其他门店的老板，并且此老板可能未绑定微信，忽略发送给" + staffBoss);
				continue;
			}

			try {
				Map<String, Object> wxMap = WxAction.sendCreatePurchasingOrderMsg(PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL, GET_SENDTEMPLATEMSG_URL, staff, company, purchasingOrder, purchasingOrderWxMsg,
						staffBoss.getOpenid(), WXMSG_TEMPLATEID_createPurchasingOrder);
				params.put(BaseWxModel.WX_ERRCODE, wxMap.get(BaseWxModel.WX_ERRCODE));
				params.put(BaseWxModel.WX_ERRMSG, wxMap.get(BaseWxModel.WX_ERRMSG));
				if ((int) wxMap.get(BaseWxModel.WX_ERRCODE) == BaseWxModel.WX_ERRCODE_Success) {
					bSendWxMsgSuccess = true;
				} else {
					logger.error("向" + staffBoss + "发送生成采购订单微信消息失败，错误消息=" + wxMap.get(BaseWxModel.WX_ERRMSG));
				}
			} catch (Exception e) {
				logger.error("向门店老板发送生成采购订单微信消息失败，错误信息=" + e.getMessage());
			}
		}

		if (bSendWxMsgSuccess) {
			DataSourceContextHolder.setDbName(dbName);
			messageBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Message_UpdateStatus, msg);
			if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("更新消息的状态失败：" + messageBO.printErrorInfo() + "。请OP手动更新本条消息的状态为已读。消息ID=" + msg.getID());
			}
		}
	}

	// /*
	// * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	// * application/x-www-form-urlencoded Request Body in Fiddler: GET :URL:
	// * http://localhost:8080/nbr/purchasingOrder/deleteListEx.bx?POListID=2,5
	// */
	// @RequestMapping(value = "/deleteListEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteListEx(@ModelAttribute("SpringWeb") PurchasingOrder
	// purchasingOrder, ModelMap model, HttpServletRequest req) {
	// logger.info("删除多个采购订单，purchasingOrder=" + purchasingOrder);
	//
	// Company company = getCompanyFromSession(req.getSession());
	// String dbName = company.getDbName();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// do {
	// String sPOIDs = GetStringFromRequest(req, "POListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sPOIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// logger.info("参数sPOIDs缺失");
	// return "";
	// } else {
	// logger.info("sPOIDs=" + sPOIDs);
	// }
	//
	// int[] iArrPOID = toIntArray(sPOIDs);
	// if (iArrPOID == null) {
	// logger.info("iArrPOID为空");
	// return "";
	// } else {
	// logger.info("iArrPOID=" + iArrPOID);
	// }
	//
	// boolean hasDBError = false;
	// StringBuilder sbErrorMsg = new StringBuilder();
	// for (int iID : iArrPOID) { // 如果删除过程中出现错误，会记录出错的地方及出错信息，并继续向后删除，直到无物可删除
	// PurchasingOrder po = new PurchasingOrder();
	// po.setID(iID);
	// DataSourceContextHolder.setDbName(dbName);
	// purchasingOrderBO.deleteObject(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, po);
	// switch (purchasingOrderBO.getLastErrorCode()) {
	// case EC_BusinessLogicNotDefined:
	// hasDBError = true;
	// logger.info("采购订单" + iID + "删除失败，因为它已经审核过。");
	// sbErrorMsg.append("采购订单" + iID + "删除失败，因为它已经审核过。<br />");
	// break;
	// case EC_NoError:
	// logger.info("采购订单" + iID + "删除成功");
	// CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).delete1(po);
	// break;
	// case EC_NoPermission:
	// hasDBError = true;
	// logger.info("你没有权限删除采购订单");
	// sbErrorMsg.append("你没有权限删除采购订单。<br />");
	// break;
	// case EC_OtherError:
	// default:
	// logger.info("采购订单" + iID + "删除失败，数据库发生错误。");
	// sbErrorMsg.append("采购订单" + iID + "删除失败，数据库发生错误。<br />");
	// hasDBError = true;
	// break;
	// }
	// }
	// logger.info("msg=" + sbErrorMsg);
	// params.put("msg", sbErrorMsg.toString());
	// //
	// if (hasDBError) {
	// logger.info("部分采购订单删除成功");
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);//
	// 前端必须显示msg给用户看
	// break;
	// }
	//
	// logger.info("全部采购点单删除删除成功");
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// // 前端如果检查到msg为空，则提示成功即可，如果msg不为空，需要显示msg给用户看
	// } while (false);
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: ID=1 URL: GET
	 * :http://localhost:8080/nbr/purchasingOrder/deleteEx.bx
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrder, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("删除一个采购订单，purchasingOrder=" + purchasingOrder);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		DataSourceContextHolder.setDbName(dbName);
		purchasingOrderBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, purchasingOrder);

		logger.info("Delete inventorySheet error code=" + purchasingOrderBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (purchasingOrderBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).delete1(purchasingOrder);

			logger.info("删除成功！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_BusinessLogicNotDefined:
			logger.info("该采购单已审核，无法删除！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, purchasingOrderBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: status=0 URL: GET
	 * :http://localhost:8080/nbr/purchasingOrder/retrieveN.bx
	 */

	@RequestMapping(value = "/retrieveN", method = RequestMethod.GET)
	public ModelAndView retrieveN(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrder, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("加载全部采购订单时加载大小类，purchasingOrder=" + purchasingOrder);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		//
		DataSourceContextHolder.setDbName(dbName);
		List<?> listPurchasingOrder = purchasingOrderBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, purchasingOrder);

		logger.info("RetrieveN purchasingOrder error code=" + purchasingOrderBO.getLastErrorCode());
		switch (purchasingOrderBO.getLastErrorCode()) {
		case EC_NoError:
			List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店

			List<BaseModel> categoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(true, false);

			List<BaseModel> categoryParentList = CacheManager.getCache(dbName, EnumCacheType.ECT_CategoryParent).readN(true, false);

			List<BaseModel> providerList = CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).readN(true, false);

			List<BaseModel> providerDistrictList = CacheManager.getCache(dbName, EnumCacheType.ECT_ProviderDistrict).readN(true, false);
			
			DataSourceContextHolder.setDbName(company.getDbName());
			ShopDistrict shopDistrictRnCondition = new ShopDistrict();
			shopDistrictRnCondition.setPageIndex(BaseAction.PAGE_StartIndex);
			shopDistrictRnCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			List<?> sdList = shopDistrictBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, shopDistrictRnCondition);
			 if (shopDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("搜索门店区域失败，错误信息：" + shopDistrictBO.printErrorInfo());
			}
			 
			List<BaseModel> staffList = CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).readN(false, false);

			if (staffList != null) {
				for (int i = 0; i < staffList.size(); i++) {
					Staff staff = (Staff) staffList.get(i);
					staff.clearSensitiveInfo();
				}
			}
			
			mm.put("shopDistrictList", sdList);
			mm.put("shopList", shopList);
			mm.put("categoryList", categoryList);
			mm.put("purchasingOrderList", listPurchasingOrder);
			mm.put("providerList", providerList);
			mm.put("providerDistrictList", providerDistrictList);
			mm.put("staffList", staffList);
			mm.put("categoryParentList", categoryParentList);
			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		default:
			logger.info("其他错误！");
			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		mm.put("CommodityField", new CommodityField());
		mm.put("ProviderField", new ProviderField());
		return new ModelAndView(PURCHASINGORDER_Order, "", new PurchasingOrder());
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * purchasingOrder.status=-1 URL: GET
	 * :http://localhost:8080/nbr/purchasingOrder/retrieveNEx.bx
	 */
	// FIXME
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrder, ModelMap model, HttpSession session) throws IOException, CloneNotSupportedException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("读取多个采购订单，purchasingOrder=" + purchasingOrder);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		// 封装所有前端所需要的信息
		List<PurchasingOrder> poList = new ArrayList<PurchasingOrder>();

		Map<String, Object> params = getDefaultParamToReturn(true);
		boolean hasDBError = false;
		do {
			// 查找所有采购订单（分页）
			//
			DataSourceContextHolder.setDbName(dbName);
			List<PurchasingOrder> purchasingOrderList = (List<PurchasingOrder>) purchasingOrderBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, purchasingOrder);
			if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询失败，错误码=" + purchasingOrderBO.getLastErrorCode());
				params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderBO.getLastErrorCode().toString());
				break;
			} else if (purchasingOrderList.size() == 0) {
				logger.info("查询多个采购订单成功，purchasingOrderList=" + purchasingOrderList);
				params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderBO.getLastErrorCode().toString());
				params.put(KEY_ObjectList, poList);
				break;
			} else {
				logger.info("查询多个采购订单成功，purchasingOrderList=" + purchasingOrderList);
			}

			ErrorInfo ei = setCreatorAndApprover(poList, purchasingOrderList, dbName, session);
			if (ei.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("设置创建人或审核人出错，错误信息=" + ei);
				params.put(BaseAction.JSON_ERROR_KEY, ei.getErrorCode());
				break;
			}

			//
			for (PurchasingOrder po : poList) {
				if (!setPurchasingOrderCommodity(po, dbName, session) || !loadCommodityList(po, dbName, session) || !loadProvider(po, dbName, session)) {
					hasDBError = true;
					break;
				}
			}
			if (hasDBError) {
				logger.info("加载数据失败");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			} else {
				logger.info("加载数据成功，listPO=" + poList);
			}

			params.put("count", purchasingOrderBO.getTotalRecord());
			params.put("msg", "");
			params.put("code", "0");
			params.put(KEY_ObjectList, poList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		if (hasDBError) {
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
		}
		params.put(KEY_HTMLTable_Parameter_msg, purchasingOrderBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/**
	 * 1、更新采购订单 2、用会话中的从表信息更新采购订单的从表。更新前先删除采购订单的从表信息
	 * 
	 * @throws CloneNotSupportedException
	 */
	private PurchasingOrder doUpdate(PurchasingOrder purchasingOrder, List<PurchasingOrderCommodity> listPOCSession, Map<String, Object> params, String dbName, HttpServletRequest req) {
		logger.info("修改对应的采购单并更新采购单商品，purchasingOrder=" + purchasingOrder);
		// 检查输入合法性
		if (!checkCreate(req, dbName, params, listPOCSession)) {
			return null;
		}

		//
		DataSourceContextHolder.setDbName(dbName);
		purchasingOrder = (PurchasingOrder) purchasingOrderBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, purchasingOrder);
		if (purchasingOrder == null && purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("修改采购订单失败，错误码=" + purchasingOrderBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderBO.getLastErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, purchasingOrderBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("修改采购订单成功,poUpdate=" + purchasingOrder);
		}

		// 先删除采购单原先的商品，再添加新的商品
		PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
		poc.setPurchasingOrderID(purchasingOrder.getID());
		//
		DataSourceContextHolder.setDbName(dbName);
		purchasingOrderCommodityBO.deleteObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, poc);
		if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("删除采购订单商品失败，" + purchasingOrderCommodityBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderCommodityBO.getLastErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, purchasingOrderCommodityBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("删除采购订单商品成功");
		}

		// 先删除采购单原先的商品，再添加新的商品
		List<PurchasingOrderCommodity> listTmp = new ArrayList<PurchasingOrderCommodity>();
		boolean hasError = false;
		for (int i = 0; i < listPOCSession.size(); i++) {
			PurchasingOrderCommodity purchasingOrderCommodity = listPOCSession.get(i);
			DataSourceContextHolder.setDbName(dbName);
			BaseModel bm = purchasingOrderCommodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, purchasingOrderCommodity);
			if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) { // ....
				hasError = true;
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("商品ID=" + purchasingOrderCommodity.getCommodityID() + ":" + purchasingOrderCommodityBO.printErrorInfo());
				}
				ErrorInfo ecOut = new ErrorInfo();
				Commodity commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(purchasingOrderCommodity.getCommodityID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个商品信息失败:" + ecOut.toString());
				}
				logger.error("采购商品：" + (commodity == null ? "未知采购商品" : commodity.getName()) + "[ID=" + purchasingOrderCommodity.getCommodityID() + "]创建失败\t");
				continue;
			}
			listTmp.add((PurchasingOrderCommodity) bm);
		}
		//
		purchasingOrder.setListSlave1(listTmp);
		//
		if (hasError) {
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
			params.put(KEY_HTMLTable_Parameter_msg, "修改采购订单失败。请重试。");
		} else {
			params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderCommodityBO.getLastErrorCode());
		}

		return purchasingOrder;
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: URL: GET
	 * :http://localhost:8080/nbr/purchasingOrder/approveEx.bx?ID=2&int1=0
	 * 需要传递commIDs，barcodeIDs，NOs，priceSuggestions进行更新采购商品
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/approveEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String approveEx(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrder, ModelMap model, HttpServletRequest req) throws IOException {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("审核一个采购订单,purchasingOrder=" + purchasingOrder);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = new HashMap<String, Object>();
		logger.info("开始审核");
		do {
			HttpSession session = req.getSession();
			//
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> poList = purchasingOrderBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, purchasingOrder);
			if(purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError || !(poList != null && poList.get(0).size() != 0)) {
				logger.error("查询该采购单失败");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "审核失败：查无该采购单");
				break;
			}
			PurchasingOrder poR1 = (PurchasingOrder) poList.get(0).get(0);
			
			logger.info("调用staffSession得到入库单的审核人信息");
			Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
			if (staff == null) {
				logger.error("得到staffSession为空");
				return null;
			} else {
				logger.info("得到staffSession=" + staff);
			}
			
			if(staff.getShopID() != 1 && staff.getShopID() != poR1.getShopID()) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "不能跨门店审核采购单");
				break;
			}

			PurchasingOrder purchasingOrderApprove = null;
			if (purchasingOrder.getIsModified() == EnumBoolean.EB_NO.getIndex()) {
				logger.info("用户没修改采购订单，直接进行审核");
				purchasingOrderApprove = purchasingOrder;
			} else if (purchasingOrder.getIsModified() == EnumBoolean.EB_Yes.getIndex()) {
				logger.info("先修改后审核");

				// 检查是否有采购订单商品,没有采购商品认为是黑客行为
				List<PurchasingOrderCommodity> purchasingOrderCommodityList = parsePurchasingOrderCommodityList(purchasingOrder, req);
				if (purchasingOrderCommodityList == null) {
					if (BaseAction.ENV != EnumEnv.DEV) {
						logger.error("黑客行为：解析前端发送的采购商品列表出错！");
					}
					return null;
				}
				//
				purchasingOrderApprove = doUpdate(purchasingOrder, purchasingOrderCommodityList, params, dbName, req);
				if (params.get(BaseAction.JSON_ERROR_KEY.toString()) != EnumErrorCode.EC_NoError) {
					if (BaseAction.ENV != EnumEnv.DEV) {
						logger.error("采购订单修改失败，错误码=" + params.get(BaseAction.JSON_ERROR_KEY.toString()));
					}
					break; // 可能是部分成功也可能是错误，不再进行审核
				} else {
					logger.info("采购订单修改成功");
				}
			} else {
				logger.error("黑客行为：前端没有传递合法的参数！");
				return null;
			}
			// 审核采购订单
			purchasingOrderApprove.setApproverID(staff.getID());
			DataSourceContextHolder.setDbName(dbName);
			PurchasingOrder poApprove = (PurchasingOrder) purchasingOrderBO.updateObject(staff.getID(), BaseBO.CASE_ApprovePurchasingOrder, purchasingOrderApprove);
			if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("审核采购订单失败，错误码=" + purchasingOrderBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, purchasingOrderBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderBO.getLastErrorCode().toString());
				break;
			} else {
				logger.info("审核采购订单成功，poApprove=" + poApprove);
				poApprove.setListSlave1(purchasingOrderApprove.getListSlave1());
				CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).write1(poApprove, dbName, staff.getID());
			}

			PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
			poc.setPurchasingOrderID(poApprove.getID());
			poc.setPageSize(BaseAction.PAGE_SIZE_MAX);   //...TODO 超过50会被checkRetrieveN()拦截  要是商家一次入库超50件以上的商品时？？
			DataSourceContextHolder.setDbName(dbName);
			List<PurchasingOrderCommodity> listPOC = (List<PurchasingOrderCommodity>) purchasingOrderCommodityBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, poc);
			if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("读取多个采购订单商品失败：" + purchasingOrderCommodityBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderCommodityBO.getLastErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, purchasingOrderCommodityBO.getLastErrorMessage());
				break;
			} else {
				logger.info("读取多个采购订单商品成功,listPOC=" + listPOC.toString());
			}
			if (!createProviderCommodity(params, poApprove.getProviderID(), listPOC, dbName, req)) {
				params.put(KEY_HTMLTable_Parameter_msg, "审核采购订单成功，但是更新供应商时失败，请联系客服处理。");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			} else {
				logger.info("创建供应商商品成功");
			}

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "审核成功");
			params.put(BaseAction.KEY_Object, poApprove);
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	protected List<PurchasingOrderCommodity> parsePurchasingOrderCommodityList(PurchasingOrder purchasingOrder, HttpServletRequest req) {
		String sCommIDs = GetStringFromRequest(req, "commIDs", String.valueOf(BaseAction.INVALID_ID));
		String sBarcodeIDs = GetStringFromRequest(req, "barcodeIDs", String.valueOf(BaseAction.INVALID_ID));
		String sNOs = GetStringFromRequest(req, "NOs", String.valueOf(BaseAction.INVALID_ID));
		String sPriceSuggestions = GetStringFromRequest(req, "priceSuggestions", String.valueOf(BaseAction.INVALID_ID));

		if (sCommIDs.equals(String.valueOf(BaseAction.INVALID_ID)) || sBarcodeIDs.equals(String.valueOf(BaseAction.INVALID_ID)) || sNOs.equals(String.valueOf(BaseAction.INVALID_ID))
				|| sPriceSuggestions.equals(String.valueOf(BaseAction.INVALID_ID))) {
			logger.error("参数异常");
			return null; // ...
		} else {
			logger.info("sCommIDs=" + sCommIDs + ", sBarcodeIDs=" + sBarcodeIDs + ",sNOs=" + sNOs + ", sPriceSuggestions=" + sPriceSuggestions);
		}

		Integer[] iaCommID = GeneralUtil.toIntArray(sCommIDs);
		Integer[] iBarcodeIDs = GeneralUtil.toIntArray(sBarcodeIDs);
		Integer[] iNOs = GeneralUtil.toIntArray(sNOs);
		Double[] iPriceSuggestions = GeneralUtil.toDoubleArray(sPriceSuggestions);
		if (iaCommID == null || iBarcodeIDs == null || iNOs == null || iPriceSuggestions == null || iaCommID.length == 0 || iaCommID.length != iBarcodeIDs.length || iNOs.length != iaCommID.length
				|| iPriceSuggestions.length != iaCommID.length) {
			logger.error("参数缺失");
			return null;// ...
		}
		// 一旦发现iaCommID内部有重复元素，当黑客行为处理
		if (GeneralUtil.hasDuplicatedElement(iaCommID)) {
			logger.error("黑客传递的参数有重复！");
			return null;
		}

		List<PurchasingOrderCommodity> purchasingOrderCommList = new ArrayList<PurchasingOrderCommodity>();
		for (int i = 0; i < iaCommID.length; i++) {
			PurchasingOrderCommodity purchasingOrderCommodity = new PurchasingOrderCommodity();
			purchasingOrderCommodity.setBarcodeID(iBarcodeIDs[i]);
			purchasingOrderCommodity.setCommodityID(iaCommID[i]);
			purchasingOrderCommodity.setCommodityNO(iNOs[i]);
			purchasingOrderCommodity.setPurchasingOrderID(purchasingOrder.getID());
			purchasingOrderCommodity.setPriceSuggestion(iPriceSuggestions[i]);
			purchasingOrderCommList.add(purchasingOrderCommodity);
		}

		return purchasingOrderCommList;
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * purchasingOrder.ID=1&purchasingOrder.purchasingPlanTableID=2 URL: POST:
	 * http://localhost:8080/nbr/purchasingOrder/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrder, ModelMap model, HttpServletRequest req, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("修改一个采购订单,purchasingOrder=" + purchasingOrder);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> poList = purchasingOrderBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, purchasingOrder);
			if(purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError || !(poList != null && poList.get(0).size() != 0)) {
				logger.error("查询该采购单失败");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "审核失败：查无该采购单");
				break;
			}
			PurchasingOrder poR1 = (PurchasingOrder) poList.get(0).get(0);
			//
			Staff staff = getStaffFromSession(req.getSession());
			if(staff.getShopID() != 1 && staff.getShopID() != poR1.getShopID()) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "不能跨门店修改采购单");
				break;
			}
			//
			List<PurchasingOrderCommodity> purchasingOrderCommodityList = parsePurchasingOrderCommodityList(purchasingOrder, req);
			if (purchasingOrderCommodityList == null) {
				return null;
			}
			//
			PurchasingOrder updatePurchasingOrder = doUpdate(purchasingOrder, purchasingOrderCommodityList, params, dbName, req);
			if (params.get(BaseAction.JSON_ERROR_KEY.toString()) != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV && params.get(BaseAction.JSON_ERROR_KEY.toString()) != EnumErrorCode.EC_WrongFormatForInputField) {
					logger.error("采购订单修改失败，错误码=" + params.get(BaseAction.JSON_ERROR_KEY.toString()));
				}
				break;
			} else {
				logger.info("采购订单修改成功");
				// 修改成功后写入缓存，将缓存中的数据更新 修改中已将从表设置进入主表
				CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).write1(updatePurchasingOrder, dbName, getStaffFromSession(req.getSession()).getID());
			}

			// 加载商品表，供应商
			// if (!loadCommodityList(poi) || !loadProvider(poi)) {
			// logger.info("加载商品表或供应商失败");
			// return "";// ...没有正确处理失败
			// } else {
			// logger.info("加载商品表和供应商成功");
			// }

			logger.info("po=" + updatePurchasingOrder);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, 1);
			params.put(KEY_Object, updatePurchasingOrder);
			params.put(BaseAction.JSON_ERROR_KEY, params.get(BaseAction.JSON_ERROR_KEY.toString()));
			params.put(KEY_HTMLTable_Parameter_msg, "采购订单修改成功");
			break;
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * :http://localhost:8080/nbr/purchasingOrder/retrieve1.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieve1", method = RequestMethod.GET)
	public ModelAndView retrieve1(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrder, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("进入retrieve1界面时，读取session相关数据，purchasingOrder=" + purchasingOrder);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		// session.removeAttribute(EnumSession.SESSION_PurchasingOrder.getName());
		// session.removeAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName());
		// session.removeAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName());

		ErrorInfo ecOut = new ErrorInfo();
		PurchasingOrder po = (PurchasingOrder) CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).read1(purchasingOrder.getID(), getStaffFromSession(session).getID(), ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询一个采购订单失败，错误码=" + ecOut.getErrorCode());
			return null;
		} else {
			logger.info("查询一个采购订单成功！po=" + po);
		}
		//
		Provider p = (Provider) CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).read1(po.getProviderID(), getStaffFromSession(session).getID(), ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询一个供应商失败，错误码=" + ecOut.getErrorCode());
			return null;
		} else {
			logger.info("查询一个供应商成功！p=" + p);
		}
		po.setProviderName(p.getName());

		// 加载采购订单的创建人
		Staff s = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(po.getStaffID(), getStaffFromSession(session).getID(), ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询一个采购订单的创建人失败，错误码=" + ecOut.getErrorCode());
			return null;
		} else {
			logger.info("查询一个采购订单的创建人成功！s=" + s);
		}
		po.setStaffName(s == null ? "" : s.getName());

		// 加载采购订单的审核人
		if (po.getApproverID() > 0) {
			Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(po.getApproverID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个采购订单的审核人失败，错误码=" + ecOut.getErrorCode());
				return null;
			} else {
				logger.info("查询一个一个采购订单的审核人成功！s=" + s);
			}
			po.setStaffName(staff.getName());
		}

		PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
		poc.setPurchasingOrderID(po.getID());
		poc.setPageSize(PAGE_SIZE_Infinite);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<PurchasingOrderCommodity> listPOC = (List<PurchasingOrderCommodity>) purchasingOrderCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, poc);
		if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("Retrieve 1 purchasingOrderCommodity error code=" + purchasingOrderCommodityBO.getLastErrorCode());
			return null;
		} else {
			logger.info("查询成功！listPOC=" + listPOC);
		}

		List<Commodity> listComm = new ArrayList<Commodity>();
		for (PurchasingOrderCommodity POC : listPOC) {
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(POC.getCommodityID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个商品失败，错误码=" + ecOut.getErrorCode());
				return null;
			} else {
				logger.info("查询一个商品成功！bm=" + bm);
			}

			Barcodes b = new Barcodes();
			b.setCommodityID(bm.getID());
			//
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询多个条形码失败，错误码=" + barcodesBO.getLastErrorCode());
				return null;
			} else {
				logger.info("查询多个条形码成功！barcodeList=" + barcodeList);
			}

			String barcodes = "";
			for (Barcodes bc : barcodeList) {
				barcodes += bc.getBarcode() + " ";
			}
			((Commodity) bm).setBarcodes(barcodes);

			listComm.add((Commodity) bm);
		}

		List<BaseModel> categoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(true, false);

		List<BaseModel> categoryParentList = CacheManager.getCache(dbName, EnumCacheType.ECT_CategoryParent).readN(true, false);

		List<BaseModel> warehouseList = CacheManager.getCache(dbName, EnumCacheType.ECT_Warehouse).readN(false, false);

		ProviderDistrict providerDistrict = new ProviderDistrict();
		providerDistrict.setPageSize(PAGE_SIZE_Infinite);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<?> pdList = providerDistrictBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, providerDistrict);
		if (providerDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询一个供应商失败，错误码=" + ecOut.getErrorCode());
			return null;
		} else {
			logger.info("查询一个供应商成功！pdList=" + pdList);
		}

		// logger.info("为后面做修改采购单位等信息以及做删除功能的session");
		// session.setAttribute(EnumSession.SESSION_PurchasingOrder.getName(), po);
		// session.setAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName(),
		// listPOC);
		// session.setAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName(),
		// listComm);
		mm.put("listPD", pdList);
		mm.put("categoryParentList", categoryParentList);
		mm.put("categoryList", categoryList);
		mm.put("purchasingOrder", po);
		mm.put("listPOC", listPOC);
		mm.put("listComm", listComm);
		mm.put("warehouseList", warehouseList);
		return new ModelAndView(PURCHASINGORDER_Retrieve1, "", new PurchasingOrder());
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: URL: GET
	 * :http://localhost:8080/nbr/purchasingOrder/retrieve1Ex.bx?ID=1&string1=
	 * staffName
	 */
	// FIXME
	/**
	 * 只加载被选中的采购订单的主从表信息及返回错误码，不返回其它任何数据<br />
	 * 由于用户在编辑采购订单列表时，可能增减商品，故和临时商品列表相关的会话需要清除
	 * 
	 * @param purchasingOrder
	 * @param model
	 * @param session
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrder, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("查询一个采购订单，purchasingOrder" + purchasingOrder);

		logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() + "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		// // 清空会话，以免下面DB有错误时，无法更新会话
		// session.removeAttribute(EnumSession.SESSION_PurchasingOrder.getName());
		// session.removeAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName());
		// session.removeAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName());

		boolean hasDBError = false;
		Map<String, Object> params = new HashMap<String, Object>();
		do {
			// 加载采购订单
			//
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> poList = purchasingOrderBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, purchasingOrder);
			if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError || poList == null || poList.get(0).size() == 0) {
				logger.error("查询一个采购订单失败，错误码==" + purchasingOrderBO.getLastErrorCode() + ",错误信息=" + purchasingOrderBO.getLastErrorMessage());
				hasDBError = true;
				break;
			}
			//
			PurchasingOrder po = (PurchasingOrder) poList.get(0).get(0);
			// 加载经办人信息
			ErrorInfo ecOut = new ErrorInfo();
			Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(po.getStaffID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个商品失败，错误码=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
				hasDBError = true;
				break;
			} else {
				logger.info("查询成功！bm=" + staff);
				po.setStaffName(staff.getName());
			}
			// 加载门店信息
			Shop shop = (Shop) CacheManager.getCache(dbName, EnumCacheType.ECT_Shop).read1(po.getShopID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
				logger.error("从缓存中读取一个供应商失败：" + ecOut.toString());
				params.put(JSON_ERROR_KEY, ecOut.getErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage() + "，加载供应商失败");
				break;
			}
			po.setShop(shop);
			// 加载采购订单的采购商品列表（从表）
			List<BaseModel> listPOC = poList.get(1);
			List<PurchasingOrderCommodity> pocList = new ArrayList<PurchasingOrderCommodity>();
			if (listPOC == null || listPOC.size() == 0) {
				logger.error("加载采购订单的采购商品列表（从表）失败");
				hasDBError = true;
				break;
			} else {
				PurchasingOrderCommodity poc = null;
				for (BaseModel bm : listPOC) {
					poc = (PurchasingOrderCommodity) bm;
					pocList.add(poc);
				}
				logger.info("加载采购订单的采购商品列表（从表）成功");
			}
			// 加载采购订单的商品列表
			List<Commodity> listComm = loadCommodityList(pocList, dbName, session);
			if (listComm == null || listComm.size() == 0) {
				logger.info("查询出来的商品数组为空");
				hasDBError = true;
				break;
			} else {
				logger.info("查询成功，listComm=" + listComm);
			}
			logger.info("设置session");
			po.setListCommodity(listComm);
			po.setListSlave1(listPOC);
			// session.setAttribute(EnumSession.SESSION_PurchasingOrder.getName(), po);
			// session.setAttribute(EnumSession.SESSION_PurchasingOrderTempPurchasingOrderCommodityList.getName(),
			// listPOC);
			// session.setAttribute(EnumSession.SESSION_PurchasingOrderCommodityList.getName(),
			// listComm);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_Object, po);
			break;
		} while (false);
		if (hasDBError) {
			params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
		}
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * sValue=可乐 URL:
	 * http://localhost:8080/nbr/purchasingOrder/retrieveNByFieldsEx.bx
	 * 根据采购订单的以下属性进行模糊搜索：商品名称、供应商名称、编号
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNByFieldsEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNByFieldsEx(@ModelAttribute("SpringWeb") PurchasingOrder purchasingOrder, ModelMap mm, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("采购订单的模糊搜索，purchasingOrder=" + purchasingOrder);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		String sValue = GetStringFromRequest(req, "sValue", null);
		if (sValue == null) {
			logger.info("sValue为空");
			return "";
		} else {
			logger.info("sValue=" + sValue);
		}

		// 封装前端所需要的数据
		List<PurchasingOrder> listPO = new ArrayList<PurchasingOrder>();

		boolean hasDBError = false;
		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			// 模糊搜索采购订单，得到采购订单列表
			purchasingOrder.setQueryKeyword(sValue);
			//
			DataSourceContextHolder.setDbName(dbName);
			List<PurchasingOrder> purchasingOrderList = (List<PurchasingOrder>) purchasingOrderBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, purchasingOrder);
			if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("模糊搜索失败，错误码=" + purchasingOrderBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, purchasingOrderBO.getLastErrorCode());
				params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderBO.getLastErrorCode().toString());
				break;
			} else {
				logger.info("模糊搜索成功，purchasingOrderList=" + purchasingOrderList);
			}

			ErrorInfo ecOut = setCreatorAndApprover(listPO, purchasingOrderList, dbName, req.getSession());
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("设置创建人或审核人失败，错误信息=" + ecOut);
				params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode());
				break;
			}

			//
			for (PurchasingOrder po : listPO) {
				if (!setPurchasingOrderCommodity(po, dbName, req.getSession()) || !loadCommodityList(po, dbName, req.getSession()) || !loadProvider(po, dbName, req.getSession())) {
					logger.info("加载供应商的对应的数据失败");
					params.put(KEY_HTMLTable_Parameter_msg, "加载供应商的对应的数据失败");
					hasDBError = true;
					break;
				} else {
					logger.info("查询成功，poi=" + po);
				}
			}

			if (hasDBError) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			}

			logger.info("listPOI=" + listPO);
			params.put("count", purchasingOrderBO.getTotalRecord());
			params.put("msg", "");
			params.put("code", "0");
			params.put("purchasingOrderList", listPO);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	private ErrorInfo setCreatorAndApprover(List<PurchasingOrder> returnPoList, List<PurchasingOrder> purchasingOrderList, String dbName, HttpSession session) {
		ErrorInfo ei = new ErrorInfo();

		logger.info("设置创建人或审核人");
		if (purchasingOrderList == null || purchasingOrderList.size() == 0) {
			ei.setErrorCode(EnumErrorCode.EC_NoError);
			ei.setErrorMessage("采购订单为空，不设置创建人或审核人");
		}

		for (PurchasingOrder p : purchasingOrderList) {
			logger.info("设置采购订单的创建人");
			Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(p.getStaffID(), getStaffFromSession(session).getID(), ei, dbName);
			if (ei.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("读取创建人失败，错误码=" + ei.getErrorCode() + ",错误信息=" + ei.getErrorMessage());
				break;
			} else {
				logger.info("查询成功！staff=" + staff);
			}
			p.setStaffName(staff == null ? "" : staff.getName());

			logger.info("设置采购订单的审核人");
			if (p.getApproverID() > 0) {
				Staff s = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(p.getApproverID(), getStaffFromSession(session).getID(), ei, dbName);
				if (ei.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("读取审核人失败，错误码=" + ei.getErrorCode());
					break;
				} else {
					logger.info("查询成功！s=" + s);
				}
				p.setStaffName(s == null ? "" : s.getName());
			}

			returnPoList.add(p);
		}

		return ei;
	}

	/** 注意：本函数会修改参数po的值 */
	protected boolean loadProvider(PurchasingOrder po, String dbName, HttpSession session) {
		if (po.getListSlave1() == null) {
			logger.info("采购单中没有商品！");
			return true;
		} else {
			logger.info("采购单中至少存在一个商品");
		}
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).read1(po.getProviderID(), getStaffFromSession(session).getID(), ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("查询供应商失败，错误码=" + ecOut.getErrorCode() + ",错误信息" + ecOut.getErrorMessage());
			return false;
		} else {
			logger.info("查询成功！bm=" + bm);
		}
		Provider provider = (Provider) bm;
		po.setProvider(provider);
		return true;
	}

	@SuppressWarnings("unchecked")
	protected List<Commodity> loadCommodityList(List<PurchasingOrderCommodity> listPOC, String dbName, HttpSession session) {
		logger.info("加载采购单的商品列表，listPOC=" + listPOC);

		List<Commodity> listComm = new ArrayList<Commodity>();
		for (PurchasingOrderCommodity POC : listPOC) {
			ErrorInfo ecOut = new ErrorInfo();
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(POC.getCommodityID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("Retrieve 1 commodity error code=" + ecOut.getErrorCode() + ",error Msg=" + ecOut.getErrorMessage());
				return null;
			} else {
				logger.info("查询一个商品成功！bm=" + bm);
			}

			Barcodes b = new Barcodes();
			b.setCommodityID(bm.getID());
			//
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("读取多个条形码失败，错误码=" + barcodesBO.getLastErrorCode());
				return null;
			} else {
				logger.info("读取多个条形码成功，barcodeList=" + barcodeList);
			}

			String barcodes = "";
			for (Barcodes bc : barcodeList) {
				barcodes += bc.getBarcode() + " ";
			}
			((Commodity) bm).setBarcodes(barcodes);

			listComm.add((Commodity) bm);
		}

		return listComm;
	}

	/**
	 * 注意：本函数会修改参数po的值
	 * 
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("unchecked")
	protected boolean loadCommodityList(PurchasingOrder po, String dbName, HttpSession session) {
		logger.info("读取采购商品表的商品以及商品的其他数据");

		if (po.getListSlave1() == null) {
			logger.info("采购单中没有商品！");
			return true;
		} else {
			logger.info("采购中商品个数至少有一个");
		}

		List<PurchasingOrderCommodity> listPOC = (List<PurchasingOrderCommodity>) po.getListSlave1();
		List<Commodity> listComm = new ArrayList<Commodity>();
		for (PurchasingOrderCommodity POC : listPOC) {
			ErrorInfo ecOut = new ErrorInfo();
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(POC.getCommodityID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询一个商品失败，错误码=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
				return false;
			} else {
				logger.info("查询成功！bm=" + bm);
			}

			Barcodes b = new Barcodes();
			b.setCommodityID(bm.getID());
			//
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询失败，错误码=" + barcodesBO.getLastErrorCode());
				return false;
			} else {
				logger.info("查询成功！barcodeList=" + barcodeList);
			}

			String barcodes = "";
			for (Barcodes bc : barcodeList) {
				barcodes += bc.getBarcode() + " ";
			}
			((Commodity) bm).setBarcodes(barcodes);

			listComm.add((Commodity) bm);
		}
		po.setListCommodity(listComm);

		return true;
	}

	// protected boolean loadProvider(PurchasingOrderInfo poi) {
	// if (poi.getPurchasingOrder() == null) {
	// logger.info("采购单中没有商品！");
	// return true;
	// } else {
	// logger.info("采购单中至少存在一个商品");
	// }
	// if (poi.getPurchasingOrder() != null) {
	// ErrorCode ecOut = new ErrorCode();
	// BaseModel bm =
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Provider).read1(poi.getPurchasingOrder().getProviderID(),
	// BaseBO.CURRENT_STAFF.getID(), ecOut, dbName);
	// if (ecOut.getEec() != EnumErrorCode.EC_NoError) {
	// logger.info("查询供应商失败，错误码=" + ecOut.getEec());
	// return false;
	// } else {
	// logger.info("查询成功！bm=" + (bm == null ? "NULL" : bm.toString()));
	// }
	// Provider provider = (Provider) bm;
	// poi.setProvider(provider);
	// }
	// return true;
	// }

	@SuppressWarnings("unchecked")
	protected List<PurchasingOrderCommodity> loadPurchasingOrderCommodity(PurchasingOrder po, String dbName, HttpSession session) {
		logger.info("读取多个采购订单商品，po=" + po);

		PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
		poc.setPurchasingOrderID(po.getID());
		poc.setPageIndex(PAGE_StartIndex);
		poc.setPageSize(PAGE_SIZE_Infinite);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<?> listTmp = purchasingOrderCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, poc);
		if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("读取多个采购订单失败，错误码=" + purchasingOrderCommodityBO.getLastErrorCode());
			return null;
		} else {
			logger.info("读取多个采购订单成功，listTmp=" + listTmp);
		}

		return (List<PurchasingOrderCommodity>) listTmp;
	}

	@SuppressWarnings("unchecked")
	protected boolean setPurchasingOrderCommodity(PurchasingOrder po, String dbName, HttpSession session) {
		logger.info("加载采购订单商品表，poi=" + po);

		PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
		poc.setPurchasingOrderID(po.getID());
		poc.setPageIndex(PAGE_StartIndex);
		poc.setPageSize(PAGE_SIZE_Infinite);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<?> listTmp = purchasingOrderCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, poc);
		if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("读取多个采购订单商品失败，错误码=" + purchasingOrderCommodityBO.getLastErrorCode());
			return false;
		} else {
			logger.info("读取多个采购订单商品成功，listTmp=" + listTmp);
		}

		po.setListSlave1((List<PurchasingOrderCommodity>) listTmp);

		return true;
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: URL:GET
	 * :http://localhost:8080/nbr/purchasingOrder/retrieveNCommodityEx.bx?commIDs=1,
	 * 该方法已经没有引用，如果有需要再放开
	 */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/retrieveNCommodityEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String retrieveNCommodityEx(@ModelAttribute("SpringWeb")
	// PurchasingOrder purchasingOrder, ModelMap model, HttpServletRequest req) {
	// logger.info("读取所有的商品,purchasingOrder=" + purchasingOrder);
	//
	// Company company = getCompanyFromSession(req.getSession());
	// String dbName = company.getDbName();
	//
	// Map<String, Object> params = getDefaultParamToReturn(true);
	// do {
	// String scommIDs = GetStringFromRequest(req, "commIDs",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (scommIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// logger.info("scommIDs参数缺失");
	// return "";
	// } else {
	// logger.info("scommIDs=" + scommIDs);
	// }
	// //
	// int[] iaCommID = toIntArray(scommIDs);
	// if (iaCommID == null) {
	// logger.info("iaCommID为空");
	// return "";
	// } else {
	// logger.info("iaCommID=" + iaCommID);
	// }
	//
	// boolean hasDBError = false;
	// List<Commodity> commList = new ArrayList<Commodity>();
	// for (int i = 0; i < iaCommID.length; i++) {
	// Commodity c = new Commodity();
	// c.setID(iaCommID[i]);
	// DataSourceContextHolder.setDbName(dbName);
	// Commodity comm = (Commodity)
	// commodityBO.retrieve1Object(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, c);
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("retrieve N Commodity error code=" +
	// commodityBO.getLastErrorCode());
	// params.put(BaseAction.JSON_ERROR_KEY,
	// commodityBO.getLastErrorCode().toString());
	// hasDBError = true;
	// break;
	// } else {
	// logger.info("查询成功！comm=" + (comm == null ? "NULL" : comm.toString()));
	// }
	//
	// PackageUnit pu = new PackageUnit();
	// pu.setID(comm.getPackageUnitID());
	// DataSourceContextHolder.setDbName(dbName);
	// PackageUnit packageUnit = (PackageUnit)
	// packageUnitBO.retrieve1Object(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, pu);
	// if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("retrieve N packageUnit error code=" +
	// packageUnitBO.getLastErrorCode());
	// params.put(BaseAction.JSON_ERROR_KEY,
	// packageUnitBO.getLastErrorCode().toString());
	// hasDBError = true;
	// break;
	// } else {
	// logger.info("查询成功！packageUnit=" + (packageUnit == null ? "NULL" :
	// packageUnit.toString()));
	// }
	//
	// comm.setString2(packageUnit.getName());
	// commList.add(comm);
	// }
	// if (hasDBError) {
	// break;
	// }
	//
	// for (Commodity c : commList) {
	// Barcodes b = new Barcodes();
	// b.setCommodityID(c.getID());
	// DataSourceContextHolder.setDbName(dbName);
	// List<Barcodes> barcodeList = (List<Barcodes>)
	// barcodesBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, b);
	// if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("Retrieve N Barcodes error code=" +
	// barcodesBO.getLastErrorCode());
	// params.put(BaseAction.JSON_ERROR_KEY,
	// barcodesBO.getLastErrorCode().toString());
	// hasDBError = true;
	// break;
	// } else {
	// logger.info("查询成功！barcodeList=" + barcodeList.toString());
	// }
	//
	// String barcodes = "";
	// int barcodeID = barcodeList.get(0).getID(); // ...暂时一品多码的时候，拿以第一个条形码ID
	// for (Barcodes bc : barcodeList) {
	// barcodes += bc.getBarcode() + " ";
	// }
	// c.setString1(barcodes);
	// c.setInt1(barcodeID);
	// }
	// if (hasDBError) {
	// break;
	// }
	//
	// // ...这里是否需要返回多包装单位之类的信息？？？？？
	// params.put("commList", commList);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
	// } while (false);
	// params.put(KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
}