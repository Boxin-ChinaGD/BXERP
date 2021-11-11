package com.bx.erp.action.warehousing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CategoryParentBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.bo.warehousing.InventoryCommodityBO;
import com.bx.erp.action.bo.warehousing.InventorySheetBO;
import com.bx.erp.action.bo.warehousing.WarehouseBO;
import com.bx.erp.action.wx.WxAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Role;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityField;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.warehousing.InventoryCommodity;
//import com.bx.erp.model.warehousing.InventoryInfo;
import com.bx.erp.model.warehousing.InventorySheet;
import com.bx.erp.model.warehousing.InventorySheetField;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/inventorySheet")
@Scope("prototype")
public class InventorySheetAction extends BaseAction {
	private Log logger = LogFactory.getLog(InventorySheetAction.class);

	// public final static string browser_msg_deletelistex =
	// "已审核的盘点单不允许删除，其他盘点单已删除";

	@Value("${public.account.appid}")
	private String PUBLIC_ACCOUNT_APPID;

	@Value("${public.account.secret}")
	private String PUBLIC_ACCOUNT_SECRET;

	@Value("${get.accesstoken.url}")
	private String GET_ACCESSTOKEN_URL; // 获取访问微信端的token接口

	@Value("${get.sendTemplateMsg.url}")
	private String GET_SENDTEMPLATEMSG_URL; // 发送模板消息的接口

	@Resource
	private InventorySheetBO inventorySheetBO;

	@Resource
	private InventoryCommodityBO inventoryCommodityBO;

	@Resource
	private WarehouseBO warehouseBO;

	@Resource
	private CategoryBO categoryBO;

	@Resource
	private CategoryParentBO categoryParentBO;

	@Resource
	private CommodityBO commodityBO;

	@Resource
	private BarcodesBO barcodesBO;

	@Resource
	private StaffBO staffBO;

	@Resource
	private RoleBO roleBO;

	@Resource
	private MessageBO messageBO;

	@Resource
	private PackageUnitBO packageUnitBO;
	
	@Resource
	private ShopBO shopBO;

	@Value("${wx.templateid.inventorySheetDiff}")
	private String WXMSG_TEMPLATEID_inventorySheetDiff;

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * status=0 URL: http://localhost:8080/nbr/inventorySheet/retrieveNInventory.bx
	 */
	// @RequestMapping(value = "/retrieveNInventory", method = RequestMethod.POST)
	// public String retrieveNInventory(@ModelAttribute("SpringWeb") InventorySheet
	// inventorySheet, ModelMap mm, HttpSession session) {
	// logger.info("retrieveNInventory时读取多个盘点单，inventorySheet=" +
	// inventorySheet.toString());
	//
	// Company company = getCompanyFromSession(session);
	//
	// DataSourceContextHolder.setDbName(company.getDbName());
	// List<?> ls =
	// inventorySheetBO.retrieveNObject(getStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, inventorySheet);
	//
	// logger.info("RetrieveN inventory error code=" +
	// inventorySheetBO.getLastErrorCode());
	//
	// switch (inventorySheetBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("ls:" + ls.toString());
	// return INVENTORY_RetrieveN;
	// default:
	// // "", "未知错误！");
	// break;
	// }
	// return INVENTORY_ToRetrieveN;
	// }

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * status=0 URL: http://localhost:8080/nbr/inventorySheet/retrieveNEx.bx
	 */
	@SuppressWarnings("unchecked")
	// @Transactional
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") InventorySheet inventorySheet, ModelMap model, HttpSession session) throws IOException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个盘点单，inventorySheet=" + inventorySheet);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		boolean hasDBError = false;

		do {
			DataSourceContextHolder.setDbName(dbName);
			List<InventorySheet> isList = (List<InventorySheet>) inventorySheetBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, inventorySheet);
			if (inventorySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("读取多个盘点单失败：" + inventorySheetBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, inventorySheetBO.getLastErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, inventorySheetBO.getLastErrorMessage());
				break;
			}
			logger.info("读取多个盘点单成功，isList=" + isList);

			// 加载审核人和创建人
			if (!setCreatorAndApprover(isList, dbName, session)) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "服务器错误，请重试");
				break;
			}

			for (InventorySheet inventory : isList) {
				// 加载盘点单商品，商品详情，仓库
				if (loadInventoryCommodity(inventory, dbName, session) == null || !loadCommodityList(inventory, dbName, session) || !loadWarehouse(inventory, dbName, session)) {
					hasDBError = true;
					break; // for
				} else {
					logger.info("加载盘点相关数据成功");
				}
			}
			if (hasDBError) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "服务器错误，请重试");
				break;
			}

			logger.info(isList);

			params.put(KEY_HTMLTable_Parameter_TotalRecord, inventorySheetBO.getTotalRecord());
			params.put(KEY_ObjectList, isList);
			params.put(KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * sString1=薯片 URL:
	 * http://localhost:8080/nbr/inventorySheet/retrieveNByFieldsEx.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNByFieldsEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNByFieldsEx(@ModelAttribute("SpringWeb") InventorySheet inventorySheet, ModelMap mm, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("盘点单的模糊查询功能，inventorySheet= " + inventorySheet);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		boolean hasDBError = false;

		do {
			String queryKeyword = GetStringFromRequest(req, "queryKeyword", null);
			if (queryKeyword == null) {
				logger.error("参数queryKeyword缺失");
				return null;
			} else {
				logger.info("queryKeyword=" + queryKeyword);
			}

			inventorySheet.setQueryKeyword(queryKeyword);
			DataSourceContextHolder.setDbName(dbName);
			List<InventorySheet> isList = (List<InventorySheet>) inventorySheetBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_InventorySheet_RetrieveNByNFields, inventorySheet);
			if (inventorySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("模糊搜索盘点单失败：" + inventorySheetBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, inventorySheetBO.getLastErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, inventorySheetBO.getLastErrorMessage());
				break;
			}
			logger.info("模糊搜索成功，listInventory=" + isList);

			// 加载审核人和创建人
			if (!setCreatorAndApprover(isList, dbName, req.getSession())) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "服务器错误，请重试");
				break;
			}

			for (InventorySheet is : isList) {
				// 加载入库单商品，商品详情
				if (loadInventoryCommodity(is, dbName, req.getSession()) == null || !loadCommodityList(is, dbName, req.getSession()) || !loadWarehouse(is, dbName, req.getSession())) {
					hasDBError = true;
					break;
				} else {
					logger.info("加载盘点相关数据成功");
				}
			}

			if (hasDBError) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "服务器错误，请重试");
				break;
			}

			logger.info("isList=" + isList);

			params.put(KEY_HTMLTable_Parameter_TotalRecord, inventorySheetBO.getTotalRecord());
			params.put(KEY_ObjectList, isList);
			params.put(KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: ID=1
	 * URL: http://localhost:8080/nbr/inventorySheet/approveEx.bx String2：审核人的名称
	 */
	// @Transactional
	@RequestMapping(value = "/approveEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String approveEx(@ModelAttribute("SpringWeb") InventorySheet inventorySheetIn, ModelMap model, HttpServletRequest req) throws IOException, CloneNotSupportedException {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("审核一个盘点单，inventorySheetIn=" + inventorySheetIn);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = new HashMap<String, Object>();
		do {
			HttpSession session = req.getSession();
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> isList = inventorySheetBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, inventorySheetIn);
			if(inventorySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError || !(isList != null && isList.get(0).size() != 0)) {
				logger.error("查询该盘点单失败");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "审核失败：查无该盘点单");
				break;
			}
			InventorySheet isR1 = (InventorySheet) isList.get(0).get(0);
			
			logger.info("调用staffSession得到盘点单的审核人信息");
			Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
			if (staff == null) {
				logger.error("得到staffSession为空");
				return null;
			} else {
				logger.info("得到staffSession=" + staff);
			}
			
			if(staff.getShopID() != 1 && staff.getShopID() != isR1.getShopID()) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "不能跨门店审核盘点单");
				break;
			}
			// List<InventoryCommodity> listInventoryCommodity = (List<InventoryCommodity>)
			// session.getAttribute(EnumSession.SESSION_InventorySheetTempInventoryCommodityList.getName());
			// if (listInventoryCommodity == null || listInventoryCommodity.size() == 0) {
			// logger.info("盘点单商品的session缺失");
			// return "";
			// } else {
			// logger.info("listInventoryCommodity=" + listInventoryCommodity.toString());
			// }
			// Staff staff = (Staff)
			// session.getAttribute(EnumSession.SESSION_Staff.getName());
			// if (staff == null) {
			// logger.info("员工的session缺失");
			// return "";
			// } else {
			// logger.info("staff=" + staff.toString());
			// }
			//
			// InventorySheet isFromSession = (InventorySheet)
			// session.getAttribute(EnumSession.SESSION_InventorySheet.getName());
			// if (isFromSession == null) {
			// logger.info("盘点单的session缺失");
			// return "";
			// } else {
			// logger.info("isFromSession=" + isFromSession.toString());
			// }
			//
			// UpdateInventoryAndInventorySheetCommodity(inventorySheetIn, isFromSession,
			// listInventoryCommodity);
			DataSourceContextHolder.setDbName(dbName);
			inventorySheetIn = (InventorySheet) inventorySheetBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, inventorySheetIn);
			if (inventorySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("修改盘点单失败：" + inventorySheetBO.printErrorInfo());
				params.put(JSON_ERROR_KEY, inventorySheetBO.getLastErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, "修改盘点单失败，失败原因：" + inventorySheetBO.getLastErrorMessage());
				break;
			}
			logger.info("修改盘点单成功，isUpdate=" + inventorySheetIn);
			// 查询盘点单从表数据
			DataSourceContextHolder.setDbName(dbName);
			InventoryCommodity inventoryCommodity = new InventoryCommodity();
			inventoryCommodity.setInventorySheetID(inventorySheetIn.getID());
			List<?> listItc = inventoryCommodityBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, inventoryCommodity);
			//
			inventorySheetIn.setListSlave1(listItc);
			CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).write1(inventorySheetIn, dbName, getStaffFromSession(req.getSession()).getID());
			//
			inventorySheetIn.setApproverID(getStaffFromSession(req.getSession()).getID());
			//
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> bmList = inventorySheetBO.updateObjectEx(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_ApproveInventorySheet, inventorySheetIn);
			if (inventorySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("审核一个盘点单失败：" + inventorySheetBO.printErrorInfo());
				params.put(JSON_ERROR_KEY, inventorySheetBO.getLastErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, "审核盘点单失败，失败原因：" + inventorySheetBO.getLastErrorMessage());
				break;
			}
			InventorySheet bm = (InventorySheet) bmList.get(0).get(0);
			logger.info("审核一个盘点单成功，bm=" + bm);
			//
			bm.setListSlave1(listItc);
			CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).write1(bm, dbName, getStaffFromSession(req.getSession()).getID());// 审核后再次写入缓存

			// 下面的信息是返回给前端的，所以需要clone，以免修改了缓存的值
			bm = (InventorySheet) bm.clone();

			// 加载入库单商品，商品详情，加载审核人和创建人
			if (bm != null) {
				List<InventoryCommodity> itCommList = new ArrayList<>();
				for (BaseModel itBm : bmList.get(1)) {
					InventoryCommodity itComm = (InventoryCommodity) itBm;
					itCommList.add(itComm);
				}
				bm.setListSlave1(itCommList);
				for (InventoryCommodity itCommodity : itCommList) {
					if (loadBarcodesAndPackageUnit(itCommodity, dbName, req.getSession()) != EnumErrorCode.EC_NoError) {
						params.put(KEY_HTMLTable_Parameter_msg, "盘点单审核成功，但是在加载相关数据时出现异常");
					}
				}
				if (!loadCommodityList(bm, dbName, req.getSession()) || !loadWarehouse(bm, dbName, req.getSession())) {
					params.put(KEY_HTMLTable_Parameter_msg, "盘点单审核成功，但是在加载相关数据时出现异常");
				} else {
					logger.info("加载盘点相关数据成功");
				}
				// 加载审核人和创建人
				setCreatorAndApprover(bm, dbName, req.getSession());
			}

			// session.setAttribute(EnumSession.SESSION_InventorySheet.getName(),
			// isFromSession);
			// session.setAttribute(EnumSession.SESSION_InventorySheetTempInventoryCommodityList.getName(),
			// listInventoryCommodity);
			params.put(KEY_Object, bm);
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			// 向门店老板发送盘点差异消息
			if (bm.getMessageID() > 0) {
				Message message = (Message) bmList.get(2).get(0);
				logger.info("查询出来的Message：" + message);
				if (message != null && !StringUtils.isEmpty(message.getParameter())) {
					if (!sendMsgToWx(company, message, getStaffFromSession(req.getSession()), bm, dbName)) {
						params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess.toString());
						params.put(KEY_HTMLTable_Parameter_msg, "审核盘点单成功，发送微信消息失败!");
					}
				} else {
					logger.error("查询创建出来的Message的消息内容为空:Message为" + message);
					params.put(BaseWxModel.WX_ERRCODE, BaseWxModel.WXTest_ERRCODE_Failed);
					params.put(BaseWxModel.WX_ERRMSG, "查询创建出来的Message的消息内容为空:Message为" + message);
				}
			}
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@SuppressWarnings("unchecked")
	private boolean sendMsgToWx(Company company, Message msg, Staff staff, InventorySheet is, String dbName) {
		if (is == null || is.getMessageID() <= 0) {
			logger.error("wsApprove == null || wsApprove.getMessageID() <= 0");
			return false;
		}

		Role role = new Role();
		role.setID(EnumTypeRole.ETR_Boss.getIndex()); // ...查询门店老板
		DataSourceContextHolder.setDbName(dbName);
		List<?> list = roleBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_Role_RetrieveAlsoStaff, role);
		if (roleBO.getLastErrorCode() != EnumErrorCode.EC_NoError || list.get(1) == null) {
			logger.error("发送盘点单生成的消息失败；查询门店老板失败或老板未绑定微信公众号！");
			return false;
		}

		boolean bSendWxMsgSuccess = false;
		List<Staff> slist = (List<Staff>) list.get(1);
		for (Staff staffBoss : slist) {
			if (staffBoss.getOpenid() == null || "".equals(staffBoss.getOpenid()) || staff.getShopID() != staffBoss.getShopID()) {
				logger.debug("用户是在哪个门店进行审核盘点单，则发送微信消息给哪个门店的老板，不会发送给其他门店的老板，并且此老板可能未绑定微信，忽略发送给" + staffBoss);
				continue;
			}

			try {
				Map<String, Object> wxMap = WxAction.sendApproveInventorySheetMsg(PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL, GET_SENDTEMPLATEMSG_URL, staff, company, staffBoss.getOpenid(), WXMSG_TEMPLATEID_inventorySheetDiff);
				if ((int) wxMap.get(BaseWxModel.WX_ERRCODE) == BaseWxModel.WX_ERRCODE_Success) {
					bSendWxMsgSuccess = true;
				} else {
					logger.error("向" + staffBoss + "发送盘点单生成的微信消息失败!错误消息=" + wxMap.get(BaseWxModel.WX_ERRMSG));
				}
			} catch (Exception e) {
				logger.error("向门店老板发送审核盘点单微信消息失败，错误信息=" + e.getMessage());
			}
		}

		if (bSendWxMsgSuccess) {
			DataSourceContextHolder.setDbName(dbName);
			messageBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Message_UpdateStatus, msg);
			if (messageBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("更新消息的状态失败：" + messageBO.printErrorInfo() + "。请OP手动更新本条消息的状态为已读。消息ID=" + msg.getID());
			}
		}

		return true;
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/inventorySheet/submitEx.bx
	 */
	@SuppressWarnings("unchecked")
	// @Transactional
	@RequestMapping(value = "/submitEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String submitEx(@ModelAttribute("SpringWeb") InventorySheet inventorySheet, ModelMap mm, HttpServletRequest req) throws CloneNotSupportedException {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("提交一个盘点单，inventorySheet=" + inventorySheet);
		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			HttpSession session = req.getSession();
			List<InventoryCommodity> listInventoryCommodity = null;
			InventorySheet is = null;
			is = getInventorySheetParameter(inventorySheet, req, params, dbName);
			if (!params.get(BaseAction.JSON_ERROR_KEY).toString().equals(EnumErrorCode.EC_NoError.toString())) {
				break;
			}
			if (is == null || is.getStatus() != InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex()) {
				logger.error("盘点单或盘点单商品表为空！");
				return null;// ...
			}
			listInventoryCommodity = (List<InventoryCommodity>) is.getListSlave1();

			UpdateInventoryAndInventorySheetCommodity(inventorySheet, is, listInventoryCommodity, dbName, req, params);
			if (!params.get(BaseAction.JSON_ERROR_KEY).toString().equals(EnumErrorCode.EC_NoError.toString())) {
				break;
			}

			DataSourceContextHolder.setDbName(dbName);
			InventorySheet isSubmit = (InventorySheet) inventorySheetBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_SubmitInventorySheet, is);
			if (inventorySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(JSON_ERROR_KEY, inventorySheetBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, "提交盘点单失败，失败原因：" + inventorySheetBO.getLastErrorMessage());
				break;
			} else {
				logger.info("审核一个盘点单成功");
			}
			// set从表数据进去
			InventoryCommodity ic = new InventoryCommodity();
			ic.setInventorySheetID(isSubmit.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<InventoryCommodity> listIc = (List<InventoryCommodity>) inventoryCommodityBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, ic);
			isSubmit.setListSlave1(listIc);
			// 更新缓存
			CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).write1(isSubmit, dbName, getStaffFromSession(req.getSession()).getID());
			// 下面的信息是返回给前端的，所以需要clone，以免修改了缓存的值
			isSubmit = (InventorySheet) isSubmit.clone();

			// 加载入库单商品，商品详情，加载审核人和创建人
			if (isSubmit != null) {
				if (loadInventoryCommodity(isSubmit, dbName, session) == null || !loadCommodityList(isSubmit, dbName, session) || !loadWarehouse(isSubmit, dbName, session)) {
					params.put(KEY_HTMLTable_Parameter_msg, "盘点单提交成功，但是在加载相关数据时出现异常");
				} else {
					logger.info("加载盘点相关数据成功");
					params.put(KEY_HTMLTable_Parameter_msg, "盘点单提交成功");
				}
			}

			// 加载审核人和创建人
			setCreatorAndApprover(isSubmit, dbName, req.getSession());

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_Object, isSubmit);
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	protected InventorySheet getInventorySheetParameter(InventorySheet is, HttpServletRequest req, Map<String, Object> params, String dbName) {
		ErrorInfo ecOut = new ErrorInfo();

		DataSourceContextHolder.setDbName(dbName);
		if (!inventorySheetBO.checkRetrieve1ExPermission(getStaffFromSession(req.getSession()).getID(), INVALID_ID, null)) {
			logger.error("当前staff无权限进行盘点单查询");
			params.put(KEY_HTMLTable_Parameter_msg, "权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission);
			// break;
			return null;
		} else {
			is = (InventorySheet) CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).read1(is.getID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || is == null || is.getStatus() == InventorySheet.EnumStatusInventorySheet.ESIS_Approved.getIndex()) {
				logger.error("盘点单不存在或者状态为2.无法进行修改！");
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(KEY_HTMLTable_Parameter_msg, "当前盘点单不存在或不支持修改！");
				// break;
				return null;
			}
			//
			if (is.getStatus() == InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex()) {
				// is = checkParameter(req);
				// listInventoryCommodity = (List<InventoryCommodity>) is.getListSlave1();
				String sInventoryCommIDs = GetStringFromRequest(req, "inventoryCommodityIDs", String.valueOf(BaseAction.INVALID_ID)).trim();
				String sCommListID = GetStringFromRequest(req, "commListID", String.valueOf(BaseAction.INVALID_ID)).trim();
				String sCommNOReals = req.getParameter("commNOReals");
				if (sCommNOReals == null || sInventoryCommIDs.equals(String.valueOf(BaseAction.INVALID_ID)) || sCommListID.equals(String.valueOf(BaseAction.INVALID_ID))) {
					logger.error("参数缺失,黑客来访");
					return null;// ...
				}

				Integer[] iCommListID = GeneralUtil.toIntArray(sCommListID);
				Integer[] iInventoryCommIDs = GeneralUtil.toIntArray(sInventoryCommIDs);
				Integer[] iCommNOReals = GeneralUtil.toIntArray(sCommNOReals);
				if (iCommNOReals == null || iInventoryCommIDs == null || iCommListID == null || iCommNOReals.length < 0 || iInventoryCommIDs.length < 0 || iInventoryCommIDs.length != iCommNOReals.length || iCommListID.length < 0
						|| iCommListID.length != iInventoryCommIDs.length) {
					logger.error("参数的长度不一致或参数不正确,黑客来访");
					return null;// ...
				}

				List<InventoryCommodity> listInventoryCommodity = new ArrayList<InventoryCommodity>();
				for (int i = 0; i < iInventoryCommIDs.length; i++) {
					InventoryCommodity ic = new InventoryCommodity();
					ic.setNoReal(iCommNOReals[i]);
					ic.setID(iInventoryCommIDs[i]);
					ic.setCommodityID(iCommListID[i]);
					listInventoryCommodity.add(ic);
				}

				is.setListSlave1(listInventoryCommodity);
			}
		}

		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		return is;
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded Request Body in Fiddler:
	 * remark=222222222 URL: http://localhost:8080/nbr/inventorySheet/updateEx.bx
	 */
	@SuppressWarnings("unchecked")
	// @Transactional
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") InventorySheet inventorySheet, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一个盘点单，inventorySheet=" + inventorySheet);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(false);
		do {
			List<InventoryCommodity> listInventoryCommodity = null;
			InventorySheet is = null;
			HttpSession session = req.getSession();
			//
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> isList = inventorySheetBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, inventorySheet);
			if(inventorySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError || !(isList != null && isList.get(0).size() != 0)) {
				logger.error("查询该盘点单失败");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "审核失败：查无该盘点单");
				break;
			}
			InventorySheet isR1 = (InventorySheet) isList.get(0).get(0);
			//
			Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
			if(staff.getShopID() != 1 && staff.getShopID() != isR1.getShopID()) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "不能跨门店创建盘点单");
				break;
			}
			is = getInventorySheetParameter(inventorySheet, req, params, dbName);
			if (!params.get(BaseAction.JSON_ERROR_KEY).toString().equals(EnumErrorCode.EC_NoError.toString())) {
				break;
			}
			if (is == null) {
				logger.error("盘点单或盘点单商品表为空！");
				return null;
			} else if (is.getStatus() == InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex()) {
				listInventoryCommodity = (List<InventoryCommodity>) is.getListSlave1();
			}

			InventorySheet updateInventory = UpdateInventoryAndInventorySheetCommodity(inventorySheet, is, listInventoryCommodity, dbName, req, params);
			// 加载入库单商品，商品详情，加载审核人和创建人
			if (updateInventory != null) {
				if (loadInventoryCommodity(updateInventory, dbName, session) == null || !loadCommodityList(updateInventory, dbName, session) || !loadWarehouse(updateInventory, dbName, session)) {
					params.put(KEY_HTMLTable_Parameter_msg, "盘点单修改成功，但是在加载相关数据时出现异常");
				} else {
					params.put(KEY_HTMLTable_Parameter_msg, "盘点单修改成功");
					logger.info("加载盘点相关数据成功");
				}
			}

			params.put(KEY_Object, updateInventory);
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();

	}

	// @Transactional
	/**
	 * @param inventorySheetHttp
	 * @param isFromSession
	 * @param listInventoryCommodity
	 *            只有在盘点单状态为待录入状态下才能够进行修改实盘数量
	 */
	private InventorySheet UpdateInventoryAndInventorySheetCommodity(InventorySheet inventorySheetHttp, InventorySheet isFromSession, List<InventoryCommodity> listInventoryCommodity, String dbName, HttpServletRequest req,
			Map<String, Object> params) {
		logger.info("修改盘点单和盘点单商品表，isFromSession=" + isFromSession);
		isFromSession.setRemark(inventorySheetHttp.getRemark());
		DataSourceContextHolder.setDbName(dbName);
		isFromSession = (InventorySheet) inventorySheetBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, isFromSession);
		if (inventorySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("修改盘点单失败，错误码=" + inventorySheetBO.getLastErrorCode().toString());
			params.put(JSON_ERROR_KEY, inventorySheetBO.getLastErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, "修改盘点单失败，失败原因：" + inventorySheetBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("修改盘点单成功，isUpdate=" + isFromSession);
		}
		//
		ErrorInfo ecOut = new ErrorInfo();
		List<InventoryCommodity> inventoryCommodities = new ArrayList<InventoryCommodity>();
		if (isFromSession.getStatus() == InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex()) {
			boolean hasDBError = false;
			for (InventoryCommodity inventoryCommodity : listInventoryCommodity) {
				// 如果实盘数量不等于-1，既是默认为只修改实盘数量

				// 先前版本:盘点商品的实盘数量不能小于0.所以小于0时删除再创建。现盘点商品的实盘数量允许小于0.所以下面注释的else代码
				// if (inventoryCommodity.getNoReal() > -1) {
				Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个商品失败，错误码=" + ecOut.getErrorCode().toString() + ",错误信息=" + ecOut.getErrorMessage());
					params.put(KEY_HTMLTable_Parameter_msg, "查询单个商品失败，失败原因：" + ecOut.getErrorMessage());
					hasDBError = true;
					break;
				} else {
					logger.info("查询一个商品成功！comm=" + comm);
				}
				inventoryCommodity.setNoSystem(comm.getNO());
				DataSourceContextHolder.setDbName(dbName);
				InventoryCommodity bm = (InventoryCommodity) inventoryCommodityBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_UpdateInventoryCommodityNoReal, inventoryCommodity);
				if (inventoryCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bm == null) {
					logger.error("修改一个盘点单商品的实盘数量失败，错误码=" + inventoryCommodityBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, "修改盘点单商品失败，失败原因：" + inventoryCommodityBO.getLastErrorMessage());
					hasDBError = true;
					break;
				} else {
					logger.info("修改一个盘点单商品的实盘数量成功！bm=" + bm);
					inventoryCommodities.add(bm);
				}
				// } else {
				// inventoryCommodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
				// BaseBO.INVALID_CASE_ID, inventoryCommodity);
				// if (inventoryCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				// logger.info("删除盘点单失败，错误码=" +
				// throw new RuntimeException("删除盘点商品失败！即将回滚DB...");
				// } else {
				// logger.info("删除盘点单成功");
				// }
				//
				// Commodity comm = (Commodity)
				// CacheManager.getCache(dbName,EnumCacheType.ECT_Commodity).read1(inventoryCommodity.getCommodityID(),
				// BaseBO.CURRENT_STAFF.getID(), ecOut, dbName);
				// if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				// logger.info("查询一个商品失败，错误码=" + ecOut.getErrorCode().toString() + ",错误信息=" +
				// ecOut.getErrorMessage());
				// throw new RuntimeException("查询单个商品失败！即将回滚DB...");
				// } else {
				// logger.info("查询一个商品成功！comm=" + (comm == null ? "NULL" : comm.toString()));
				// }
				//
				// inventoryCommodity.setNoSystem(comm.getNO());
				// InventoryCommodity ic = (InventoryCommodity)
				// inventoryCommodityBO.createObject(BaseBO.CURRENT_STAFF.getID(),
				// BaseBO.INVALID_CASE_ID, inventoryCommodity);
				// if (inventoryCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				// logger.info("创建一个盘点单商品失败，错误码=" +
				// inventoryCommodityBO.getLastErrorCode().toString());
				// throw new RuntimeException("创建盘点商品失败！即将回滚DB...");
				// } else {
				// logger.info("创建一个盘点单商品成功！ic=" + ic.toString());
				// }
				// CacheManager.getCache(dbName,EnumCacheType.ECT_InventoryCommodity).write1(ic);
				// }
			}
			//
			if (hasDBError) {
				// 将从表数据写进主表的ListSlave1并写入缓存中
				isFromSession.setListSlave1(listInventoryCommodity);
				CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).write1(isFromSession, dbName, getStaffFromSession(req.getSession()).getID());
				//
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
				return null;
			}
		}
		// 将从表数据写进主表的ListSlave1并写入缓存中
		isFromSession.setListSlave1(listInventoryCommodity);
		CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).write1(isFromSession, dbName, getStaffFromSession(req.getSession()).getID());
		//
		params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
		return isFromSession;
	}

	/*
	 * Request Body in Fiddler: http://localhost:8080/nbr/inventorySheet.bx
	 */
	@RequestMapping(produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("SpringWeb") InventorySheet inventorySheet, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入盘点单主页时加载大类，仓库，员工，inventorySheet=" + inventorySheet);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		do {
			List<BaseModel> categoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(true, false);//

			CategoryParent cp = new CategoryParent(); // ...缓存已经加载全部。直接从缓存加载即可
			cp.setPageSize(PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(dbName);
			List<?> categoryParentList = categoryParentBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, cp);
			if (categoryParentBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询所有大类失败，错误码=" + categoryParentBO.getLastErrorCode().toString());
				mm.put(BaseAction.JSON_ERROR_KEY, categoryParentBO.getLastErrorCode().toString());
				break;
			} else {
				logger.info("查询所有大类成功，categoryParentList=" + categoryParentList);
			}
			
			List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店

			mm.put("shopList", shopList);
			mm.put("categoryList", categoryList);
			mm.put("categoryParentList", categoryParentList);

			break;
		} while (false);
		mm.put("InventorySheetField", new InventorySheetField());
		mm.put("CommodityField", new CommodityField());
		return new ModelAndView(INVENTORYSHEET_Index, "", new InventorySheet());
	}

	// @RequestMapping(value = "/toPlan", method = RequestMethod.POST)
	// public String plan() {
	// return INVENTORYSHEET_ToPlan;
	// }

//	@RequestMapping(value = "/toInput", method = RequestMethod.GET)
//	public String input(@ModelAttribute("SpringWeb") InventorySheet inventorySheet, ModelMap mm, HttpSession session) {
//		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
//			logger.debug("无权访问本Action");
//			return null;
//		}
//		
//		// // ... Retrieve Inventory commodity list
//		// // ...
//		// InventoryCommodity ic = new InventoryCommodity();
//		// ic.setInventorySheetID(is.getID());
//		// List<?> list = inventoryCommodityBO.retrieveNObject(ic);
//		// switch (inventoryCommodityBO.getLastErrorCode()) {
//		// case EC_NoError:
//		// commoditylist = (List<Commodity>) inventoryCommodityBO.retrieveNObject(ic);
//		// //put("commoditylist", commoditylist);
//		// logger.info(commoditylist);
//		//
//		// default:
//		// return ""; // ...
//		// }
//		//
//		return INVENTORYSHEET_ToInput;
//	}
//
//	public String approve() {
//		return INVENTORY_ToApprove;
//	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/x-www-form-urlencoded
	 * Fiddler:warehouseID=2&scope=1&status=1&staffID=2&remark=...........x&
	 * commListID=1,2 URL:http://localhost:8080/nbr/inventorySheet/createEx.bx
	 */
	// @Transactional
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") InventorySheet inventorySheet, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个盘点单，inventorySheet=" + inventorySheet);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = new HashMap<String, Object>();
		do {
			HttpSession session = req.getSession();
			Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
			if(staff.getShopID() != 1 && staff.getShopID() != inventorySheet.getShopID()) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "不能跨门店创建盘点单");
				break;
			}
			// 解析用户输入
			List<InventoryCommodity> listIc = parseInventoryCommodityList(inventorySheet, req, dbName, params);
			if (listIc == null) {
				logger.error("黑客行为");
				return null;
			}
			if (params.get(JSON_ERROR_KEY) == EnumErrorCode.EC_PartSuccess) { // ...
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}

			// 检查输入合法性
			if (!checkCreate(req, dbName, params, listIc)) {
				break;
			}
			//
			InventorySheet is = createMasterAndSalve(inventorySheet, dbName, staff.getID(), params, listIc);
			if (is == null) {
				break;
			}
			// 更新缓存
			CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).write1(is, dbName, staff.getID());

			params.put(BaseAction.KEY_Object, is);
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "创建盘点单成功");
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();

	}

	private List<InventoryCommodity> parseInventoryCommodityList(InventorySheet inventorySheet, HttpServletRequest req, String dbName, Map<String, Object> params) {
		String sCommIDs = GetStringFromRequest(req, "commListID", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sBarcodeIDs = GetStringFromRequest(req, "barcodeIDs", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sCommNOReals = req.getParameter("commNOReals");

		if (sCommIDs.equals(String.valueOf(BaseAction.INVALID_ID)) || sBarcodeIDs.equals(String.valueOf(BaseAction.INVALID_ID)) || sCommNOReals == null) {
			logger.error("参数异常,黑客友情访问");
			return null;
		} else {
			logger.info("sCommIDs=" + sCommIDs + ", sBarcodeIDs=" + sBarcodeIDs + ", sCommNOReals=" + sCommNOReals);
		}
		//
		Integer[] iaCommID = GeneralUtil.toIntArray(sCommIDs);
		Integer[] iaBarcodeID = GeneralUtil.toIntArray(sBarcodeIDs);
		Integer[] iaCommNOReals = GeneralUtil.toIntArray(sCommNOReals);
		if (iaCommID == null || iaCommID.length == 0 || iaBarcodeID == null || iaBarcodeID.length == 0 || iaBarcodeID.length != iaCommID.length || iaCommNOReals == null || iaCommNOReals.length == 0
				|| iaCommNOReals.length != iaCommID.length) {
			logger.error("参数缺失,黑客友情访问");
			return null;
		} else {
			logger.info("iaCommID=" + iaCommID + ", iaBarcodeID=" + iaBarcodeID + ", iaCommNOReals=" + iaCommNOReals);
		}
		//
		List<InventoryCommodity> icList = new ArrayList<InventoryCommodity>();
		ErrorInfo ecOut = new ErrorInfo();
		boolean hasDBError = false;
		for (int i = 0; i < iaCommID.length; i++) {
			Commodity comm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(iaCommID[i], getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || comm == null) {
				logger.error("查询一个商品失败，错误码=" + ecOut.getErrorCode());
				hasDBError = true;
				break;
			} else {
				logger.info("查询一个商品成功！comm=" + comm);
			}
			//
			InventoryCommodity inventoryCommodity = new InventoryCommodity();
			inventoryCommodity.setCommodityID(iaCommID[i]);
			inventoryCommodity.setInventorySheetID(inventorySheet.getID());
			inventoryCommodity.setNoReal(iaCommNOReals[i]);
			inventoryCommodity.setNoSystem(comm.getNO());
			inventoryCommodity.setBarcodeID(iaBarcodeID[i]);

			icList.add(inventoryCommodity);
		}
		if (hasDBError) {
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
			params.put(KEY_HTMLTable_Parameter_msg, "查询商品失败，失败原因：" + ecOut.getErrorMessage());
		}

		return icList;
	}

	private boolean checkCreate(HttpServletRequest req, String dbName, Map<String, Object> params, List<InventoryCommodity> listIc) {
		Commodity commRetrieved = null;
		ErrorInfo eiOut = new ErrorInfo();
		for (InventoryCommodity ic : listIc) {
			DataSourceContextHolder.setDbName(dbName);
			commRetrieved = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(ic.getCommodityID(), getStaffFromSession(req.getSession()).getID(), eiOut, dbName);
			if (commRetrieved == null || eiOut.getErrorCode() != EnumErrorCode.EC_NoError) {// 有可能该商品是存在的，只是读取时发生了DB异常，但这种情况几乎不会出现，所以认为是商品不存在
				logger.error("不能盘点一个不存在的商品：" + commRetrieved);
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(KEY_HTMLTable_Parameter_msg, "不能盘点一个不存在的商品");
				return false;
			} else {
				logger.info("查询商品成功=" + commRetrieved);
				if (commRetrieved.getStatus() != Commodity.EnumStatusCommodity.ESC_Normal.getIndex() && commRetrieved.getStatus() != Commodity.EnumStatusCommodity.ESC_ToEliminated.getIndex()) {
					logger.error("不能盘点已经删除的商品：" + commRetrieved);
					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
					params.put(KEY_HTMLTable_Parameter_msg, "不能盘点已经删除的商品（商品名称：" + commRetrieved.getName() + "）");
					return false;
				}
				if (commRetrieved.getType() != CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
					logger.error("不能盘点单品以外的商品：" + commRetrieved);
					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
					params.put(KEY_HTMLTable_Parameter_msg, "不能盘点单品以外的商品（商品" + commRetrieved.getName() + "的类型不是单品）");
					return false;
				}
			}
		}

		return true;
	}

	private InventorySheet createMasterAndSalve(InventorySheet inventorySheet, String dbName, int staffID, Map<String, Object> params, List<InventoryCommodity> listIc) {
		// 创建盘点单
		DataSourceContextHolder.setDbName(dbName);
		InventorySheet is = (InventorySheet) inventorySheetBO.createObject(staffID, BaseBO.INVALID_CASE_ID, inventorySheet);
		if (inventorySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("创建盘点单失败：" + inventorySheetBO.printErrorInfo());
			params.put(JSON_ERROR_KEY, inventorySheetBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, inventorySheetBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("创建一个盘点单成功！is=" + is);
		}

		// 创建盘点单商品
		List<InventoryCommodity> listIC = new ArrayList<InventoryCommodity>();
		InventoryCommodity ic = new InventoryCommodity();
		boolean hasDBError = false;
		for (int i = 0; i < listIc.size(); i++) {
			ic = listIc.get(i);
			ic.setInventorySheetID(is.getID());
			DataSourceContextHolder.setDbName(dbName);
			InventoryCommodity icCommodity = (InventoryCommodity) inventoryCommodityBO.createObject(staffID, BaseBO.INVALID_CASE_ID, ic);
			if (inventoryCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("创建一个盘点单商品失败：" + inventoryCommodityBO.printErrorInfo());
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
				params.put(KEY_HTMLTable_Parameter_msg, "创建盘点单商品列表失败，请重新调整盘点商品列表");
				hasDBError = true;
				break;
			} else {
				logger.info("创建一个盘点单商品成功！icCommodity=" + icCommodity);
			}

			listIC.add(icCommodity);
		}
		if (hasDBError) {
			return null;
		}

		// 建立主从表关系
		is.setListSlave1(listIC);

		return is;
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: ID=1 URL:
	 * http://localhost:8080/nbr/inventorySheet/deleteEx.bx
	 */
	@RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") InventorySheet inventorySheet, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("删除一个盘点单以及对应的盘点单的商品表,inventorySheet=" + inventorySheet.toString());

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		Map<String, Object> params = new HashMap<String, Object>();
		do {

			// ...先删除从表，后删除主表。下面2步，建议在一个SP内实现
			DataSourceContextHolder.setDbName(dbName);
			inventorySheetBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, inventorySheet);
			if (inventorySheetBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("删除一个盘点单失败：" + inventorySheetBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, inventorySheetBO.getLastErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, inventorySheetBO.getLastErrorMessage());
				break;
			}
			logger.info("删除一个盘点单成功");
			//
			CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).delete1(inventorySheet);

			InventoryCommodity ic = new InventoryCommodity();
			ic.setInventorySheetID(inventorySheet.getID());
			DataSourceContextHolder.setDbName(dbName);
			inventoryCommodityBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, ic);
			if (inventoryCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("删除多个盘点单商品表失败：" + inventoryCommodityBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, inventoryCommodityBO.getLastErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, inventoryCommodityBO.getLastErrorMessage());
				break;
			}
			logger.info("删除多个盘点单商品表成功");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "删除盘点单成功");
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	// /*
	// * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	// * application/x-www-form-urlencoded Request Body in Fiddler:
	// * inventorySheetListID=1,2 URL:
	// * http://localhost:8080/nbr/inventorySheet/deleteListEx.bx
	// */
	// @RequestMapping(value = "/deleteListEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteListEx(@ModelAttribute("SpringWeb") InventorySheet
	// inventorySheet, ModelMap mode, HttpServletRequest req) {
	// logger.info("删除多个盘点单以及对应的盘点单的商品表,inventorySheet=" +
	// inventorySheet.toString());
	//
	// Company company = getCompanyFromSession(req.getSession());
	// String dbName = company.getDbName();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// do {
	// String sISIDs = GetStringFromRequest(req, "inventorySheetListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sISIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// logger.info("参数异常");
	// return "";
	// } else {
	// logger.info("sISIDs=" + sISIDs);
	// }
	//
	// int[] iArrISID = toIntArray(sISIDs);
	// if (iArrISID == null) {
	// logger.info("参数缺失");
	// return "";
	// } else {
	// logger.info("iArrISID=" + iArrISID);
	// }
	//
	// boolean hasDBError = false;
	// StringBuilder sbErrorMsg = new StringBuilder();
	// for (int iID : iArrISID) { // 如果删除过程中出现错误，会记录出错的地方及出错信息，并继续向后删除，直到无物可删除
	//
	// // ...先删除从表，后删除主表。下面2步，建议在一个SP内实现
	//
	// inventorySheet.setID(iID);
	// DataSourceContextHolder.setDbName(dbName);
	// inventorySheetBO.deleteObject(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, inventorySheet);
	// switch (inventorySheetBO.getLastErrorCode()) {
	// case EC_BusinessLogicNotDefined:
	// sbErrorMsg.append("盘点单" + iID + "删除失败，因为它已经审核过。<br />");
	// hasDBError = true;
	// break;
	// case EC_NoError:
	// CacheManager.getCache(dbName,
	// EnumCacheType.ECT_InventorySheet).delete1(inventorySheet);
	// break;
	// case EC_NoPermission:
	// hasDBError = true;
	// logger.info("你没有权限删除盘点单");
	// sbErrorMsg.append("你没有权限删除盘点单。<br />");
	// break;
	// case EC_OtherError:
	// default:
	// sbErrorMsg.append("盘点单" + iID + "删除失败，数据库发生错误。<br />");
	// hasDBError = true;
	// break;
	// }
	// }
	//
	// params.put(KEY_HTMLTable_Parameter_msg, sbErrorMsg.toString());
	// if (hasDBError) {
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);//
	// 前端必须显示msg给用户看
	// break;
	// }
	//
	// logger.info("全部盘点单删除成功，sbErrorMsg=" + sbErrorMsg.toString());
	// logger.info(sbErrorMsg.toString());
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError); //
	// 前端如果检查到msg为空，则提示成功即可，如果msg不为空，需要显示msg给用户看
	// } while (false);
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler:
	 * URL:http://localhost:8080/nbr/inventorySheet/retrieve1.bx?ID=2 现阶段前端没有使用
	 * 2019-05-30
	 */
//	@RequestMapping(value = "/retrieve1", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
//	public String retrieve1(@ModelAttribute("SpringWeb") InventorySheet inventorySheet, ModelMap mm, HttpServletRequest req) {
//		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
//			logger.debug("无权访问本Action");
//			return null;
//		}
//		
//		logger.info("读取一个盘点单，inventorySheet=" + inventorySheet);
//
//		HttpSession session = req.getSession();
//		logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() + "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());
//
//		Company company = getCompanyFromSession(session);
//		String dbName = company.getDbName();
//
//		do {
//			ErrorInfo ecOut = new ErrorInfo();
//			InventorySheet is = (InventorySheet) CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_InventorySheet).read1(inventorySheet.getID(), getStaffFromSession(session).getID(), ecOut, dbName);// ...读取缓存失败
//			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || is == null) {
//				logger.info("读取一个盘点单失败，错误码=" + ecOut.getErrorCode().toString() + ",错误信息=" + ecOut.getErrorMessage());
//				mm.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
//				mm.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage().toString());
//				break;
//			} else {
//				logger.info("读取一个盘点单成功，is=" + is);
//			}
//
//			Warehouse warehouse = (Warehouse) CacheManager.getCache(dbName, EnumCacheType.ECT_Warehouse).read1(is.getWarehouseID(), getStaffFromSession(session).getID(), ecOut, dbName);
//			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
//				logger.info("读取一个仓库失败，错误码=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
//				mm.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
//				mm.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage().toString());
//				break;
//			} else {
//				logger.info("读取一个仓库成功，warehouse=" + warehouse);
//			}
//
//			Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(is.getStaffID(), getStaffFromSession(session).getID(), ecOut, dbName);
//			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
//				logger.info("读取一个员工失败，错误码=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
//				mm.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
//				mm.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage().toString());
//				break;
//			} else {
//				logger.info("读取一个员工成功，staff=" + staff);
//			}
//			// 加载盘点商品表（从表）
//			List<?> icList = is.getListSlave1();
//			if (icList == null || icList.size() == 0) {
//				logger.info("加载采购订单的采购商品列表（从表）失败");
//				break;
//			} else {
//				logger.info("加载采购订单的采购商品列表（从表）成功!,list" + icList);
//			}
//
//			mm.put("staff", staff);
//			mm.put("warehouse", warehouse);
//			mm.put("listInventoryCommodity", icList);
//			mm.put("inventorySheet", is);
//			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
//		} while (false);
//
//		return INVENTORYSHEET_Input;
//	}

	protected boolean setCreatorAndApprover(List<InventorySheet> isList, String dbName, HttpSession session) {
		logger.info("设置创建人和审核人，isList=" + isList);

		for (InventorySheet is : isList) {
			// 入库单的创建人
			ErrorInfo ecOut = new ErrorInfo();
			Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(is.getStaffID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个创建人失败，错误码=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
				return false;
			} else {
				logger.info("查询一个创建人成功！staff=" + staff);
				is.setCreatorName(staff == null ? "" : staff.getName());
			}

			// 入库单的审核人
			if (is.getApproverID() > 0) {
				Staff s = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(is.getApproverID(), getStaffFromSession(session).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个审核人失败，错误码=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
					return false;
				} else {
					logger.info("查询一个审核人成功！s=" + s);
					is.setApproverName(s == null ? "" : s.getName());
				}
			}
		}
		return true;
	}

	protected boolean setCreatorAndApprover(InventorySheet is, String dbName, HttpSession session) {
		logger.info("设置创建人和审核人，isList=" + is);

		// 入库单的创建人
		ErrorInfo ecOut = new ErrorInfo();
		Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(is.getStaffID(), getStaffFromSession(session).getID(), ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询一个创建人失败，错误码=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
			return false;
		} else {
			logger.info("查询一个创建人成功！staff=" + staff);
			is.setCreatorName(staff == null ? "" : staff.getName());
		}

		// 入库单的审核人
		if (is.getApproverID() > 0) {
			Staff s = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(is.getApproverID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个审核人失败，错误码=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
				return false;
			} else {
				logger.info("查询一个审核人成功！s=" + s);
				is.setApproverName(s == null ? "" : s.getName());
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	protected List<InventoryCommodity> loadInventoryCommodity(InventorySheet is, String dbName, HttpSession session) {
		logger.info("读取所有的盘点单商品，is=" + is);

		InventoryCommodity ic = new InventoryCommodity();
		ic.setInventorySheetID(is.getID());
		ic.setPageIndex(PAGE_StartIndex);
		ic.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<InventoryCommodity> listTmp = (List<InventoryCommodity>) inventoryCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, ic);
		logger.info("Retrieve N inventoryCommodity error code=" + inventoryCommodityBO.getLastErrorCode());
		if (inventoryCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("读取所有的盘点单商品失败，错误码=" + inventoryCommodityBO.getLastErrorCode().toString());
			return null;
		}
		logger.info("读取所有的盘点单商品成功，listTmp=" + listTmp);
		for (InventoryCommodity inventoryCommodity : listTmp) {
			if (loadBarcodesAndPackageUnit(inventoryCommodity, dbName, session) != EnumErrorCode.EC_NoError) {
				logger.error("读取盘点单商品相应的条形码，包装单位失败。");
				return null;
			}
		}
		is.setListSlave1(listTmp);

		return (List<InventoryCommodity>) listTmp;
	}

	// 根据盘点点商品表加载相应的条形码和包装单位
	protected EnumErrorCode loadBarcodesAndPackageUnit(InventoryCommodity ic, String dbName, HttpSession session) {
		ErrorInfo ecOut = new ErrorInfo();

		Barcodes barcodes = null;
		DataSourceContextHolder.setDbName(dbName);
		if (!barcodesBO.checkRetrieve1Permission(getStaffFromSession(session).getID(), INVALID_ID, null)) {
			logger.info("条形码查询无权限！");

			return barcodesBO.getLastErrorCode();
		} else {
			barcodes = (Barcodes) CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).read1(ic.getBarcodeID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				return ecOut.getErrorCode();
			}
			ic.setBarcodes(barcodes.getBarcode());
		}

		//
		PackageUnit packageUnit = new PackageUnit();
		packageUnit.setID(ic.getPackageUnitID());
		DataSourceContextHolder.setDbName(dbName);
		PackageUnit packageUnitR1 = (PackageUnit) packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, packageUnit);
		if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			return packageUnitBO.getLastErrorCode();
		}

		ic.setPackageUnitName(packageUnitR1.getName());

		return EnumErrorCode.EC_NoError;
	}

	@SuppressWarnings("unchecked")
	protected boolean loadCommodityList(InventorySheet inventorySheet, String dbName, HttpSession session) {
		logger.info("读取多个的商品，inventoryInfo=" + inventorySheet);
		List<InventoryCommodity> listIc = (List<InventoryCommodity>) inventorySheet.getListSlave1();
		List<Commodity> listComm = new ArrayList<Commodity>();
		for (InventoryCommodity wc : listIc) {
			ErrorInfo ecOut = new ErrorInfo();
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(wc.getCommodityID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("读取一个商品失败，错误码=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
				return false;
			} else {
				logger.info("读取一个商品成功，bm=" + bm);
			}

			Barcodes b = new Barcodes();
			b.setID(wc.getBarcodeID());
			DataSourceContextHolder.setDbName(dbName);
			Barcodes barcode = (Barcodes) barcodesBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("读取一个条形码失败，错误码=" + barcodesBO.getLastErrorCode());
				return false;
			} else {
				logger.info("读取多个条形码成功，barcode=" + barcode);
				((Commodity) bm).setBarcodes(barcode.getBarcode());
			}

			listComm.add((Commodity) bm);
		}
		inventorySheet.setListCommodity(listComm);

		logger.info("读取成功，inventoryInfo=" + inventorySheet);
		return true;
	}

	protected List<Commodity> loadCommodityList(List<InventoryCommodity> listIc, String dbName, HttpSession session) {
		logger.info("读取多个的商品数组，listIc=" + listIc);
		List<Commodity> listComm = new ArrayList<Commodity>();
		for (InventoryCommodity wc : listIc) {
			ErrorInfo ecOut = new ErrorInfo();
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(wc.getCommodityID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("Retrieve 1 commodity error code=" + ecOut.getErrorCode() + ",错误信息=" + ecOut.getErrorMessage());
				return null;
			} else {
				logger.info("读取一个商品成功，bm=" + bm);
			}

			Barcodes b = new Barcodes();
			b.setID(wc.getBarcodeID());
			DataSourceContextHolder.setDbName(dbName);
			Barcodes barcode = (Barcodes) barcodesBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("读取一个条形码失败，错误码=" + barcodesBO.getLastErrorCode());
				return null;
			} else {
				logger.info("读取多个条形码成功，barcode=" + barcode);
				((Commodity) bm).setBarcodes(barcode.getBarcode());
			}

			listComm.add((Commodity) bm);
		}

		return listComm;
	}

	protected boolean loadWarehouse(InventorySheet inventorySheet, String dbName, HttpSession session) {
		logger.info("读取多个仓库，inventorySheet=" + inventorySheet);
		if (inventorySheet != null) {
			ErrorInfo ecOut = new ErrorInfo();
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Warehouse).read1(inventorySheet.getWarehouseID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个仓库失败，错误码=" + ecOut.getErrorCode().toString() + ",错误信息=" + ecOut.getErrorMessage());
				return false;
			} else {
				logger.info("查询一个仓库成功！bm=" + bm);
			}
			Warehouse warehouse = (Warehouse) bm;
			inventorySheet.setWarehouse(warehouse);
		}
		return true;
	}
}
