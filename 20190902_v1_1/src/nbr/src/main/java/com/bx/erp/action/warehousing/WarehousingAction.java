package com.bx.erp.action.warehousing;

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
import com.bx.erp.action.bo.ShopDistrictBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.bo.purchasing.ProviderBO;
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderCommodityBO;
import com.bx.erp.action.bo.warehousing.WarehousingCommodityBO;
import com.bx.erp.action.wx.WxAction;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.Company;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Role;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Shop;
import com.bx.erp.model.ShopDistrict;
import com.bx.erp.model.ShopField;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityField;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.model.purchasing.ProviderField;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrder.EnumStatusPurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.model.warehousing.WarehousingField;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.Warehousing.EnumStatusWarehousing;

import net.sf.json.JSONObject;

//@Transactional
@Controller
@RequestMapping("/warehousing")
@Scope("prototype")
public class WarehousingAction extends BaseAction {
	private Log logger = LogFactory.getLog(WarehousingAction.class);

	@Value("${public.account.appid}")
	private String PUBLIC_ACCOUNT_APPID;

	@Value("${public.account.secret}")
	private String PUBLIC_ACCOUNT_SECRET;

	@Value("${get.accesstoken.url}")
	private String GET_ACCESSTOKEN_URL; // 获取访问微信端的token接口

	@Value("${get.sendTemplateMsg.url}")
	private String GET_SENDTEMPLATEMSG_URL; // 发送模板消息的接口

	@Resource
	private PackageUnitBO packageUnitBO;

	@Resource
	private WarehousingBO warehousingBO;

	@Resource
	private PurchasingOrderCommodityBO purchasingOrderCommodityBO;

	@Resource
	private ProviderCommodityBO providerCommodityBO;

	@Resource
	private ProviderBO providerBO;

	@Resource
	private CommodityBO commodityBO;

	@Resource
	private PurchasingOrderBO purchasingOrderBO;

	@Resource
	private WarehousingCommodityBO warehousingCommodityBO;

	@Resource
	private BarcodesBO barcodesBO;

	@Resource
	private MessageBO messageBO;

	@Resource
	private ShopBO shopBO;

	@Resource
	private RoleBO roleBO;

	@Resource
	private StaffBO staffBO;
	
	@Resource
	private ShopDistrictBO shopDistrictBO;

	@Value("${wx.templateid.warehousingDiff}")
	private String WXMSG_TEMPLATEID_warehousingDiff;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(Warehousing warehousing, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入页面时加载所有的入库单,Warehousing " + warehousing.toString());

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		DataSourceContextHolder.setDbName(dbName);
		List<?> ls = warehousingBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehousing);

		logger.info("RetrieveN warehousing error code=" + warehousingBO.getLastErrorCode());

		switch (warehousingBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("ls=" + ls);
			/*
			 * List<Staff> staffList = (List<Staff>)
			 * staffBO.retrieveNObject(getStaffFromSession(session).getID(),
			 * BaseBO.INVALID_CASE_ID, staff);
			 */
			List<BaseModel> staffList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Staff).readN(false, false); // 从缓存中拿到所有员工

			if (staffList != null) {
				for (int i = 0; i < staffList.size(); i++) {
					Staff staff1 = (Staff) staffList.get(i);
					staff1.clearSensitiveInfo();
				}
			}

			logger.info("读取多个Staff成功，staffList=" + staffList);
			
			List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有员工

			List<BaseModel> providerList = CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).readN(true, false);

			List<BaseModel> providerDistrictList = CacheManager.getCache(dbName, EnumCacheType.ECT_ProviderDistrict).readN(true, false);

			List<BaseModel> categoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(true, false);

			List<BaseModel> categoryParentList = CacheManager.getCache(dbName, EnumCacheType.ECT_CategoryParent).readN(true, false);
			
			DataSourceContextHolder.setDbName(company.getDbName());
			List<?> sdList = shopDistrictBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, new ShopDistrict());
			 if (shopDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("搜索门店区域失败，错误信息：" + shopDistrictBO.printErrorInfo());
			}
			 
			mm.put("shopDistrictList", sdList);
			mm.put("warehousingList", ls);
			mm.put("shopList", shopList);
			mm.put("staffList", staffList);
			mm.put("providerList", providerList);
			mm.put("providerDistrictList", providerDistrictList);
			mm.put("categoryList", categoryList);
			mm.put("categoryParentList", categoryParentList);
			break;
		default:
			logger.info("其他错误");
			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		mm.put("WarehousingField", new WarehousingField());
		mm.put(KEY_HTMLTable_Parameter_msg, warehousingBO.getLastErrorMessage());
		return new ModelAndView(WAREHOUSINGSHEET_Index, "", null);
	}

	@RequestMapping(value = "/returnPurchasingCommodity", method = RequestMethod.GET)
	public ModelAndView returnPurchasingCommodity(ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		List<BaseModel> staffList = CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).readN(false, false);

		List<BaseModel> providerDistrictList = CacheManager.getCache(dbName, EnumCacheType.ECT_ProviderDistrict).readN(true, false);
		ShopDistrict shopDistrictRnCondition = new ShopDistrict();
		shopDistrictRnCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		shopDistrictRnCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(company.getDbName());
		List<?> shopDistrictList = shopDistrictBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, shopDistrictRnCondition);
		 if (shopDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("搜索门店区域失败，错误信息：" + shopDistrictBO.printErrorInfo());
		}
		 
		List<BaseModel> providerList = CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).readN(true, false);

		List<BaseModel> categoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(true, false);

		List<BaseModel> categoryParentList = CacheManager.getCache(dbName, EnumCacheType.ECT_CategoryParent).readN(true, false);
		
		List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店
		
		// logger.info(name);
		logger.info("header");
		mm.put("shopList", shopList);
		mm.put("CommodityField", new CommodityField());
		mm.put("ProviderField", new ProviderField());
		mm.put("ShopField", new ShopField());
		mm.put("staffList", staffList);
		mm.put("providerDistrictList", providerDistrictList);
		mm.put("shopDistrictList", shopDistrictList);
		mm.put("providerList", providerList);
		mm.put("categoryList", categoryList);
		mm.put("categoryParentList", categoryParentList);
		return new ModelAndView(WAREHOUSING_ReturnPurchasingCommodity, "", "");
	}

	/*
	 * Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/warehousing/retrieveNEx.bx?ID=1&pageIndex=1
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Warehousing warehousing, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个入库单,warehousing=" + warehousing);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		boolean hasDBError = false;

		do {
			DataSourceContextHolder.setDbName(company.getDbName());
			List<Warehousing> warehousingList = (List<Warehousing>) warehousingBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, warehousing);
			if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("查询多个入库单失败：" + warehousingBO.printErrorInfo());
				}
				params.put(BaseAction.JSON_ERROR_KEY, warehousingBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, warehousingBO.getLastErrorMessage());
				break;
			} else {
				logger.info("查询多个入库单成功,warehousingList=" + warehousingList);
			}

			if (!setCreatorAndApprover(warehousingList, dbName, req)) {
				hasDBError = true;
				break;
			}

			for (Warehousing ws : warehousingList) {
				// 加载入库单商品，商品详情，仓库，供应商
				if (!loadWarehouse(ws, dbName, req) || !loadProvider(ws, dbName, req)) {
					hasDBError = true;
					break;
				}
			}

			if (hasDBError) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			}

			logger.info("查询成功:" + warehousingList);
			params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, warehousingBO.getTotalRecord());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.KEY_HTMLTable_Parameter_code, "0");
			params.put("warehousingList", warehousingList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	protected boolean setCreatorAndApprover(List<Warehousing> warehousingList, String dbName, HttpServletRequest req) {
		logger.info("设置创建人和审核人，warehousingList=" + warehousingList);
		for (Warehousing w : warehousingList) {
			// 入库单的创建人
			ErrorInfo ecOut = new ErrorInfo();
			Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(w.getStaffID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个创建人失败：" + ecOut.toString());
				return false;
			} else {
				logger.info("查询一个创建人成功！staff=" + staff);
				w.setCreatorName(staff == null ? "" : staff.getName());
			}

			// 入库单的审核人
			if (w.getApproverID() > 0) {
				Staff s = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(w.getApproverID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个审核人失败:" + ecOut.toString());
					return false;
				} else {
					logger.info("查询一个审核人成功！staff=" + staff);
					w.setApproverName(s == null ? "" : s.getName());
				}
			}
		}
		return true;
	}
	// 因为warehousing/retrieveNEx.bx、warehousing/retrieveNByFieldsEx.bx不需要从表数据给前端，所以这个方法暂时用不上
	// @SuppressWarnings("unchecked")
	// protected boolean loadWarehousingCommodity(Warehousing ws, String dbName,
	// HttpServletRequest req) {
	// logger.info("读取多个入库单商品，ws=" + ws.toString());
	//
	// WarehousingCommodity wc = new WarehousingCommodity();
	// wc.setWarehousingID(ws.getID());
	// wc.setPageIndex(PAGE_StartIndex);
	// wc.setPageSize(PAGE_SIZE_Infinite);
	// DataSourceContextHolder.setDbName(dbName);
	// List<WarehousingCommodity> listWC = (List<WarehousingCommodity>)
	// warehousingCommodityBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, wc);
	// if (warehousingCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("读取多个入库单商品失败，错误码=" + warehousingCommodityBO.getLastErrorCode());
	// return false;
	// } else {
	// logger.info("查询多个入库单商品成功，listWC=" + listWC.toString());
	// }
	//
	// PackageUnit pu = new PackageUnit();
	// Barcodes b = new Barcodes();
	// for (WarehousingCommodity warehousingCommodity : listWC) {
	// pu.setID(warehousingCommodity.getPackageUnitID());
	// DataSourceContextHolder.setDbName(dbName);
	// PackageUnit packageUnit = (PackageUnit)
	// packageUnitBO.retrieve1Object(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, pu);
	// if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("查询一个包装单位失败，错误码=" + packageUnitBO.getLastErrorCode());
	// return false;
	// }
	// warehousingCommodity.setPackageUnitName(packageUnit.getName());
	//
	// b.setID(warehousingCommodity.getBarcodeID());
	// DataSourceContextHolder.setDbName(dbName);
	// Barcodes barcode = (Barcodes)
	// barcodesBO.retrieve1Object(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, b);
	// if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("查询一个包装单位失败，错误码=" + packageUnitBO.getLastErrorCode());
	// return false;
	// }
	// warehousingCommodity.setBarcode(barcode.getBarcode());
	// }
	//
	// ws.setListSlave1(listWC);
	//
	// return true;
	// }

	@SuppressWarnings("unchecked")
	protected boolean loadCommodityList(Warehousing ws, String dbName, HttpServletRequest req) {
		logger.info("读取多个商品，ws=" + ws);

		List<WarehousingCommodity> listWC = (List<WarehousingCommodity>) ws.getListSlave1();
		List<Commodity> listComm = new ArrayList<Commodity>();
		for (WarehousingCommodity wc : listWC) {
			ErrorInfo ecOut = new ErrorInfo();
			Commodity bm = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(wc.getCommodityID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询一个商品失败，错误码=" + ecOut.getErrorCode() + "错误信息=" + ecOut.getErrorMessage());
				return false;
			} else {
				logger.info("查询一个商品成功！bm=" + bm);
				listComm.add(bm);
			}
		}
		ws.setListCommodity(listComm);

		logger.info("读取多个商品成功，ws=" + ws);
		return true;
	}

	@SuppressWarnings("unchecked")
	protected List<Commodity> loadCommodityList(List<WarehousingCommodity> listWc, String dbName, HttpServletRequest req) {
		logger.info("读取多个商品数组，listWc=" + listWc);

		List<Commodity> listComm = new ArrayList<Commodity>();
		for (WarehousingCommodity wc : listWc) {
			ErrorInfo ecOut = new ErrorInfo();
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(wc.getCommodityID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
			logger.info("Retrieve 1 commodity error code=" + ecOut.getErrorCode());
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询一个商品失败，错误码=" + ecOut.getErrorCode() + "错误信息=" + ecOut.getErrorMessage());
				return null;
			} else {
				logger.info("查询一个商品成功！bm=" + bm);
			}

			Barcodes b = new Barcodes();
			b.setCommodityID(bm.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, b);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询多个条形码失败，错误码=" + barcodesBO.getLastErrorCode());
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
		logger.info("读取多个商品成功，listComm=" + listComm);
		return listComm;
	}

	protected boolean loadProvider(Warehousing ws, String dbName, HttpServletRequest req) {
		logger.info("读取入库单对应的供应商，ws=" + ws);

		// 需求改为直接用warehousing的字段ProviderID查询供应商信息
		if (ws.getProviderID() == 0) {
			return true;
		}

		// PurchasingOrder po = new PurchasingOrder();
		// po.setID(ws.getPurchasingOrderID());
		// po = (PurchasingOrder)
		// purchasingOrderBO.retrieve1Object(BaseBO.CURRENT_STAFF.getID(),
		// BaseBO.INVALID_CASE_ID, po);
		// if (po == null || purchasingOrderBO.getLastErrorCode() !=
		// EnumErrorCode.EC_NoError) {
		// logger.info("查询一个采购订单失败，错误码=" + purchasingOrderBO.getLastErrorCode());
		// return false;
		// } else {
		// logger.info("查询一个采购订单成功，po=" + po.toString());
		// }

		if (ws != null) {
			ErrorInfo ecOut = new ErrorInfo();
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).read1(ws.getProviderID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询一个供应商失败，错误码=" + ecOut.getErrorCode() + "错误信息=" + ecOut.getErrorMessage());
				return false;
			} else {
				logger.info("查询一个供应商成功！bm=" + bm);
			}
			Provider provider = (Provider) bm;
			ws.setProvider(provider);

			logger.info("设置入库单的供应商成功，ws=" + ws);
		}
		return true;
	}

	protected boolean loadWarehouse(Warehousing ws, String dbName, HttpServletRequest req) {
		logger.info("读取入库单对应的仓库，ws=" + ws);

		if (ws != null) {
			ErrorInfo ecOut = new ErrorInfo();
			BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Warehouse).read1(ws.getWarehouseID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("查询一个仓库失败，错误码=" + ecOut.getErrorCode() + "错误信息=" + ecOut.getErrorMessage());
				return false;
			} else {
				logger.info("查询一个仓库成功！bm=" + bm);
			}
			Warehouse warehouse = (Warehouse) bm;
			ws.setWarehouse(warehouse);

			logger.info("设置入库单的仓库成功，ws=" + ws);
		}
		return true;
	}

	/*
	 * Request Body in Fiddler: sheetID=1 URL: warehousing/retrieve1Ex.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Warehousing warehousing, ModelMap model, HttpServletRequest req, HttpSession session) throws CloneNotSupportedException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取一个入库单，warehousing=" + warehousing);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() + "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());

		Map<String, Object> params = new HashMap<String, Object>();

		do {
			boolean hasDBError = false;
			// 加载入库单
			ErrorInfo ecOut = new ErrorInfo();
			Warehousing ws = null;

			if (warehousing.getStatus() == EnumStatusWarehousing.ESW_ToApprove.getIndex()) {// 入库单未审核时，查询数据库，读取入库单商品当前数据库的商品名称
				DataSourceContextHolder.setDbName(dbName);
				List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehousing);
				if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
					if (BaseAction.ENV != EnumEnv.DEV) {
						logger.error("查询一个入库单失败或查询的入库单为空：" + warehousingBO.printErrorInfo());
					}
					params.put(BaseAction.JSON_ERROR_KEY, warehousingBO.getLastErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, warehousingBO.getLastErrorMessage());
					break;
				}
				//
				List<BaseModel> masterList = bmList.get(0);
				List<BaseModel> slaveList = (List<BaseModel>) bmList.get(1);
				masterList.get(0).setListSlave1(slaveList);
				//
				ws = (Warehousing) masterList.get(0);
				//
				CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).write1(ws, dbName, getStaffFromSession(session).getID());
			} else {
				ws = (Warehousing) CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).read1(warehousing.getID(), getStaffFromSession(session).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || ws == null) {
					if (BaseAction.ENV != EnumEnv.DEV) {
						logger.error("查询一个入库单失败或查询的入库单为空：" + ecOut.toString());
					}
					params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
					break;
				} else {
					logger.info("查询一个 库单成功！ws=" + ws);
				}
			}

			// 避免污染缓存
			ws = (Warehousing) ws.clone();

			// 加载经办人信息
			Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(ws.getStaffID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个商品失败：" + ecOut.toString());
				params.put(JSON_ERROR_KEY, ecOut.getErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
				break;
			} else {
				logger.info("查询成功！staff=" + staff);
				ws.setStaffName(staff.getName());
			}

			if (ws.getProviderID() != 0) {
				Provider provider = (Provider) CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).read1(ws.getProviderID(), getStaffFromSession(session).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
					logger.error("从缓存中读取一个供应商失败：" + ecOut.toString());
					params.put(JSON_ERROR_KEY, ecOut.getErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage() + "，加载供应商失败");
					break;
				}
				ws.setProvider(provider);
			}
			
			// 加载门店信息
			Shop shop = (Shop) CacheManager.getCache(dbName, EnumCacheType.ECT_Shop).read1(ws.getShopID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
				logger.error("从缓存中读取一个供应商失败：" + ecOut.toString());
				params.put(JSON_ERROR_KEY, ecOut.getErrorCode().toString());
				params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage() + "，加载供应商失败");
				break;
			}
			ws.setShop(shop);
			List<WarehousingCommodity> listWc = (List<WarehousingCommodity>) ws.getListSlave1();

			if (listWc == null || listWc.size() == 0) {
				logger.error("加载入库单的入库商品失败");
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError);
				// params.put(KEY_HTMLTable_Parameter_msg,
				// "加载入库单商品失败：List<WarehousingCommodity>=");
				params.put(KEY_HTMLTable_Parameter_msg, "所查看的入库单无入库商品");
				params.put(KEY_Object, ws); // ... BA说如果从表查询失败，把主表返回给前端显示
				break;
			} else {
				logger.info("加载入库单的入库商品列表成功，listWc=" + listWc);
			}

			for (WarehousingCommodity warehousingCommodity : listWc) {
				// 加载对应的入库单商品的条形码
				Barcodes b = new Barcodes();
				b.setID(warehousingCommodity.getBarcodeID());
				Barcodes barcodes = (Barcodes) CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).read1(warehousingCommodity.getBarcodeID(), getStaffFromSession(session).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询商品条形码失败：" + ecOut.toString());
					params.put(JSON_ERROR_KEY, ecOut.getErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage() + "，加载商品条形码失败");
					hasDBError = true;
					break;
				}
				warehousingCommodity.setBarcode(barcodes.getBarcode());

				// 加载对应的入库单商品的包装名
				PackageUnit p = new PackageUnit();
				p.setID(warehousingCommodity.getPackageUnitID());
				DataSourceContextHolder.setDbName(dbName);
				PackageUnit packageUnit = (PackageUnit) packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, p);
				if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("读取对应的入库单商品的包装名：" + packageUnitBO.printErrorInfo());
					params.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, packageUnitBO.getLastErrorMessage() + "，加载包装单位失败");
					hasDBError = true;
					break;
				}
				warehousingCommodity.setPackageUnitName(packageUnit.getName());
			}

			if (hasDBError) {
				break;
			}

			logger.info("设置session");
			params.put(KEY_Object, ws);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());

			break;
		} while (false);

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/warehousing/createEx.bx?ID=1&orderID=7&staffID=2&
	 * commNO=50,60&commMoney=100,200
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Warehousing warehousing, ModelMap model, HttpServletRequest req) throws CloneNotSupportedException {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个入库单，warehousing=" + warehousing);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			HttpSession session = req.getSession();
			Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
			//
			if(staff.getShopID() != 1 && staff.getShopID() != warehousing.getShopID()) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "不能跨门店创建入库单");
				break;
			}
			// 解析用户输入
			List<WarehousingCommodity> listWc = parseWarehousingCommodityList(warehousing, req, dbName);
			if (listWc == null) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("黑客行为");
				}
				return null;
			}
			// 检查输入合法性
			if (!checkCreate(req, dbName, params, listWc)) {
				break;
			}

			int staffID = getStaffFromSession(req.getSession()).getID();

			Warehousing ws = createMasterAndSalve(warehousing, dbName, staffID, params, listWc);
			if (ws == null) {
				break;
			}

			// 更新缓存
			CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).write1(ws, dbName, staffID);

			// 避免修改了缓存，因为下面的loadCommodityList(ws, dbName, req)修改了ws
			ws = (Warehousing) ws.clone();

			// 加载入库单的商品详情
			if (!loadCommodityList(ws, dbName, req)) {
				logger.error("加载采购订单的商品列表失败");
				params.put(KEY_HTMLTable_Parameter_msg, "入库单已创建成功，但显示商品失败，请刷新入库单列表页面查看！");
			}
			params.put(KEY_Object, ws);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "");
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	public Warehousing createMasterAndSalve(Warehousing warehousing, String dbName, int staffID, Map<String, Object> params, List<WarehousingCommodity> listWc) {
		// 创建入库单
		warehousing.setStaffID(staffID);
		DataSourceContextHolder.setDbName(dbName);
		Warehousing ws = (Warehousing) warehousingBO.createObject(staffID, BaseBO.INVALID_CASE_ID, warehousing);
		logger.info("Create warehousing error code=" + warehousingBO.getLastErrorCode());
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("创建入库单失败，" + warehousingBO.printErrorInfo());
			}
			params.put(BaseAction.JSON_ERROR_KEY, warehousingBO.getLastErrorCode().toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, warehousingBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("创建入库单成功，ws=" + ws);
		}

		// 创建入库单从表
		List<WarehousingCommodity> listWC = new ArrayList<WarehousingCommodity>();
		WarehousingCommodity wc = new WarehousingCommodity();
		boolean hasDBError = false;
		for (int i = 0; i < listWc.size(); i++) {
			wc = listWc.get(i);
			wc.setWarehousingID(ws.getID());
			wc.setShelfLife(Commodity.DEFAULT_VALUE_ShelfLife); // ..给定一个默认的保质期为一年。
			DataSourceContextHolder.setDbName(dbName);
			WarehousingCommodity wcC = (WarehousingCommodity) warehousingCommodityBO.createObject(staffID, BaseBO.INVALID_CASE_ID, wc);
			if (warehousingCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("创建入库单商品表失败，失败原因：" + warehousingCommodityBO.printErrorInfo() + "。请运营删除其主表(warehousing)ID = " + ws.getID());
				}
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "创建部分的入库单商品失败，失败原因：服务器错误。请重新尝试！");
				hasDBError = true;
				break;
			} else {
				logger.info("创建入库单商品表成功，bm=" + wcC);
			}

			listWC.add(wcC);
		}
		if (hasDBError) {
			return null;
		}

		// 建立主从表关系
		ws.setListSlave1(listWC);

		return ws;
	}

	private boolean checkCreate(HttpServletRequest req, String dbName, Map<String, Object> params, List<WarehousingCommodity> listWc) {
		Commodity commRetrieved = null;
		ErrorInfo eiOut = new ErrorInfo();
		for (WarehousingCommodity wc : listWc) {
			wc.setShelfLife(Commodity.DEFAULT_VALUE_ShelfLife); // ..给定一个默认的保质期为一年。
			String err = wc.checkCreate(BaseBO.CASE_CheckCreateForAction); // 在先开始检查从表，保证主表创建成功后从表也能正常创建，保证数据不会出现有主表没从表或从表少了。
			if (!err.equals("")) {
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField);
				params.put(KEY_HTMLTable_Parameter_msg, err);
				return false;
			}

			DataSourceContextHolder.setDbName(dbName);
			commRetrieved = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(wc.getCommodityID(), getStaffFromSession(req.getSession()).getID(), eiOut, dbName);
			if (commRetrieved == null || eiOut.getErrorCode() != EnumErrorCode.EC_NoError) {// 有可能该商品是存在的，只是读取时发生了DB异常，但这种情况几乎不会出现，所以认为是商品不存在
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("不能入库一个不存在的商品：CommodityID = " + wc.getCommodityID());
				}
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(KEY_HTMLTable_Parameter_msg, "不能入库一个不存在的商品");
				return false;
			} else {
				logger.info("查询商品成功=" + commRetrieved);
				if (commRetrieved.getType() != CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
					if (BaseAction.ENV != EnumEnv.DEV) {
						logger.error("不能入库单品以外的商品：" + commRetrieved);
					}
					params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
					params.put(KEY_HTMLTable_Parameter_msg, "不能入库单品以外的商品（商品" + commRetrieved.getName() + "的类型不是单品）");
					return false;
				}
			}

			DataSourceContextHolder.setDbName(dbName);
			Barcodes barcodesRetrieve1 = (Barcodes) CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).read1(wc.getBarcodeID(), getStaffFromSession(req.getSession()).getID(), eiOut, dbName);
			if (barcodesRetrieve1 == null || eiOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("该条形码不存在：BarcodeID=" + wc.getBarcodeID());
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(KEY_HTMLTable_Parameter_msg, "该条形码不存在");
				return false;
			}
			if (barcodesRetrieve1.getCommodityID() != wc.getCommodityID()) {
				logger.error("条形码ID与商品实际条形码ID不对应：BarcodeID=" + wc.getBarcodeID() + ",CommodityID=" + wc.getCommodityID());
				params.put(JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(KEY_HTMLTable_Parameter_msg, "条形码与商品实际条形码不对应");
				return false;
			}
		}

		return true;
	}

	/*
	 * Request Body in Fiddler: ID=1 URL:
	 * http://localhost:8080/nbr/warehousing/retrieveN.bx
	 */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/retrieveN", method = RequestMethod.GET)
	// public ModelAndView retrieveN(@ModelAttribute("SpringWeb") Warehousing
	// warehousing, ModelMap mm, HttpServletRequest req) throws IOException {
	// logger.info("读取多个入库单，warehousing=" + warehousing.toString());
	//
	// List<Warehousing> ls = (List<Warehousing>)
	// warehousingBO.retrieveNObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.CASE_RetrieveNWarhousingByOrderID, warehousing);
	//
	// logger.info("RetrieveN Warehousing error code = " +
	// warehousingBO.getLastErrorCode());
	//
	// switch (warehousingBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("ls=" + ls);
	// mm.put("warehousingSheetList", ls);
	// return new ModelAndView(WAREHOUSINGSHEET_Index, "warehousing", new
	// Warehousing());
	// default:
	// // "", "未知错误");
	// break;
	// }
	// return new ModelAndView(WAREHOUSINGSHEET_Index, "warehousing", new
	// Warehousing());
	// }

	/*
	 * Request Body in Fiddler: purchasingOrderID=7 URL:
	 * http://localhost:8080/nbr/warehousing/retrieveNCommEx.bx
	 * 测试第一步（6*100+6.3375*80）/180=6.15
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNCommEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNCommEx(@ModelAttribute("SpringWeb") Warehousing warehousing, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取采购订单和多个商品，warehousing=" + warehousing);

		HttpSession session = req.getSession();
		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() + "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());

		PurchasingOrderCommodity orderComm = new PurchasingOrderCommodity();
		orderComm.setPurchasingOrderID(warehousing.getPurchasingOrderID());
		DataSourceContextHolder.setDbName(dbName);
		List<PurchasingOrderCommodity> ls = (List<PurchasingOrderCommodity>) purchasingOrderCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, orderComm);
		logger.info("RetrieveN PurchasingOrderCommodity error code = " + purchasingOrderCommodityBO.getLastErrorCode());

		List<Commodity> commList = new ArrayList<Commodity>();
		Commodity comm = new Commodity();
		Provider p = new Provider();
		ProviderCommodity pc = new ProviderCommodity();
		Map<String, Object> params = new HashMap<String, Object>();
		switch (purchasingOrderCommodityBO.getLastErrorCode()) {
		case EC_NoError:
			if (ls.size() != 0) {
				for (PurchasingOrderCommodity purchasingOrderCommodity : ls) {
					comm.setID(purchasingOrderCommodity.getCommodityID());
					DataSourceContextHolder.setDbName(dbName);
					List<List<BaseModel>> commR1 = commodityBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, comm);
					if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.info("查询一个商品失败，错误码 = " + commodityBO.getLastErrorCode());
						params.put(JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
						break;
					} else {
						comm = Commodity.fetchCommodityFromResultSet(commR1);
						logger.info("查询一个商品成功！bm=" + comm);
						commList.add(comm);
					}
				}

				int commodityID = commList.get(0).getID();
				pc.setCommodityID(commodityID);
				DataSourceContextHolder.setDbName(dbName);
				List<ProviderCommodity> providerCommodityList = (List<ProviderCommodity>) providerCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pc);
				if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("查询多个供应商商品失败，错误码=" + providerCommodityBO.getLastErrorCode());
					params.put(JSON_ERROR_KEY, providerCommodityBO.getLastErrorCode().toString());
					break;
				} else {
					logger.info("查询多个供应商商品成功！providerCommodityList=" + providerCommodityList);
				}

				for (ProviderCommodity purchasingOrderCommodity : providerCommodityList) {
					p.setID(purchasingOrderCommodity.getProviderID());
					DataSourceContextHolder.setDbName(dbName);
					Provider provider = (Provider) providerBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, p);
					if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.info("读取一个供应商失败，错误码= " + providerBO.getLastErrorCode());
						params.put(JSON_ERROR_KEY, providerBO.getLastErrorCode().toString());
						break;
					} else {
						logger.info("读取一个供应商成功！provider=" + provider);
						params.put("provider", provider);
					}
				}
			}
			session.setAttribute(EnumSession.SESSION_OrderCommList.getName(), ls);
			session.setAttribute(EnumSession.SESSION_CommList.getName(), commList);
			params.put("commList", commList);
			params.put("orderCommList", ls);
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		default:
			logger.info("未知错误");
			params.put(JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * http://localhost:8080/nbr/warehousing/clearEx.bx
	 */
	@RequestMapping(value = "/clearEx", method = RequestMethod.GET)
	@ResponseBody
	public void clearEx(HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return;
		}
		
		HttpSession session = req.getSession();
		session.removeAttribute(EnumSession.SESSION_CommList.getName());
		session.removeAttribute(EnumSession.SESSION_OrderCommList.getName());
		session.removeAttribute(EnumSession.SESSION_WarehousingTempWarehousingCommodityList.getName());
		session.removeAttribute(EnumSession.SESSION_OrderCommodityList.getName());
	}

	// /*
	// * Request Body in Fiddler: URL:
	// * http://localhost:8080/nbr/warehousing/prepareCommodity.bx?commID=2,3
	// */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/prepareCommodity", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String prepareCommodityListEx(@ModelAttribute("SpringWeb") Warehousing
	// warehousing, ModelMap model, HttpServletRequest req) {
	// HttpSession session = req.getSession();
	//
	// logger.info("当前的方法名称：" + new
	// Exception().getStackTrace()[0].getMethodName() +
	// "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());
	// String commIDList = GetStringFromRequest(req, "commID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (commIDList.compareTo(String.valueOf(BaseAction.INVALID_ID)) == 0) {
	// return ""; // ...
	// }
	//
	// String[] arrCommID = commIDList.split(",");
	// int[] iArrCommID = new int[arrCommID.length];
	// try {
	// for (int i = 0; i < arrCommID.length; i++) {
	// iArrCommID[i] = Integer.parseInt(arrCommID[i]);
	// }
	// } catch (Exception e) {
	// return "";// ...
	// }
	//
	// List<WarehousingCommodity> warehousingCommList = (List<WarehousingCommodity>)
	// session.getAttribute(EnumSession.SESSION_WarehousingTempWarehousingCommodityList.getName());
	// List<PurchasingOrderCommodity> purchasingOrderCommoList =
	// (List<PurchasingOrderCommodity>)
	// session.getAttribute(EnumSession.SESSION_OrderCommodityList.getName());
	// List<Commodity> listExistingCommodity = (List<Commodity>)
	// session.getAttribute(EnumSession.SESSION_CommList.getName());
	// logger.info("commList in session:" + listExistingCommodity);
	// List<PurchasingOrderCommodity> orderCommList =
	// (List<PurchasingOrderCommodity>)
	// session.getAttribute(EnumSession.SESSION_OrderCommList.getName());
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// List<Commodity> ls = new ArrayList<Commodity>();
	//
	// if (warehousingCommList == null) {
	// if (listExistingCommodity != null) {
	// for (Commodity commodity : listExistingCommodity) {
	// ls.add(commodity);
	// }
	// }
	// for (int ID : iArrCommID) {
	// appendCommodity(ID, ls);
	// }
	// } else {
	// for (WarehousingCommodity wComm : warehousingCommList) {
	// Commodity comm = new Commodity();
	// comm.setID(wComm.getCommodityID());
	// Commodity bm = (Commodity)
	// commodityBO.retrieve1Object(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, comm);
	// ls.add(bm);
	// }
	// for (int ID : iArrCommID) {
	// appendCommodity(ID, ls);
	// }
	// }
	//
	// session.setAttribute(EnumSession.SESSION_CommList.getName(), ls);
	// session.setAttribute(EnumSession.SESSION_OrderCommList.getName(),
	// orderCommList);
	//
	// params.put("commList", ls);
	// params.put("orderCommList", orderCommList);
	// params.put("orderCommodityList", purchasingOrderCommoList);
	// params.put("warehousingCommList", warehousingCommList);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	// /*
	// * Request Body in Fiddler: URL:
	// * http://localhost:8080/nbr/warehousing/reduceCommodityEx.bx?commID=1
	// */
	// @SuppressWarnings("unchecked")
	// @RequestMapping(value = "/reduceEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.GET)
	// @ResponseBody
	// public String reduceCommodityEx(@ModelAttribute("SpringWeb") Warehousing
	// warehousing, ModelMap model, HttpServletRequest req) {
	// HttpSession session = req.getSession();
	//
	// String commID = GetStringFromRequest(req, "commID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (commID.compareTo(String.valueOf(BaseAction.INVALID_ID)) == 0) {
	// return ""; // ...
	// }
	// int ID = Integer.parseInt(commID);
	// Map<String, Object> params = new HashMap<String, Object>();
	// List<Commodity> listExistingCommodity = (List<Commodity>)
	// session.getAttribute(EnumSession.SESSION_WarehousingCommodityList.getName());
	// for (int i = 0; i < listExistingCommodity.size(); i++) {
	// if (listExistingCommodity.get(i).getID() == ID) {
	// logger.info("Delete CommodityID is:" +
	// listExistingCommodity.get(i).getID());
	// listExistingCommodity.remove(i);
	// }
	// }
	//
	// List<PurchasingOrderCommodity> orderCommList =
	// (List<PurchasingOrderCommodity>) session.getAttribute("orderCommList");
	// if (orderCommList != null) {
	// for (int i = 0; i < orderCommList.size(); i++) {
	// if (orderCommList.get(i).getCommodityID() == ID) {
	// logger.info("Delete PurchasingOrderCommodity.commodityID is:" +
	// orderCommList.get(i).getCommodityID());
	// orderCommList.remove(i);
	// }
	// }
	// params.put("orderCommList", orderCommList);
	// }
	//
	// session.setAttribute(EnumSession.SESSION_CommList.getName(),
	// listExistingCommodity);
	//
	// params.put("commList", listExistingCommodity);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * URL: http://localhost:8080/nbr/warehousing/approveEx.bx?ID=1
	 */
	@RequestMapping(value = "/approveEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String approveEx(@ModelAttribute("SpringWeb") Warehousing warehousing, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("审核一个入库单，warehousing=" + warehousing);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = new HashMap<String, Object>();
		do {
			HttpSession session = req.getSession();
			//
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> whList = warehousingBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehousing);
			if(warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || !(whList != null && whList.get(0).size() != 0)) {
				logger.error("查询该入库单失败");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "审核失败：查无该入库单");
				break;
			}
			Warehousing whR1 = (Warehousing) whList.get(0).get(0);
			//
			logger.info("调用staffSession得到入库单的审核人信息");
			Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
			if (staff == null) {
				logger.error("得到staffSession为空");
				return null;
			} else {
				logger.info("得到staffSession=" + staff);
			}
			//
			if(staff.getShopID() != 1 && staff.getShopID() != whR1.getShopID()) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "不能跨门店审核入库单");
				break;
			}
			//
			Warehousing wsApprove = null;
			if (warehousing.getIsModified() == EnumBoolean.EB_NO.getIndex()) {
				logger.info("用户没修改入库单，直接进行审核");
				wsApprove = warehousing;
			} else if (warehousing.getIsModified() == EnumBoolean.EB_Yes.getIndex()) {
				logger.info("先修改后审核");

				List<WarehousingCommodity> listWc = parseWarehousingCommodityList(warehousing, req, dbName);
				if (listWc == null) {
					if (BaseAction.ENV != EnumEnv.DEV) {
						logger.error("黑客行为：解析前端发送的入库商品列表出错！");
					}
					return null;
				}

				wsApprove = doUpdate(warehousing, listWc, params, dbName, req);
				if (params.get(BaseAction.JSON_ERROR_KEY.toString()) != EnumErrorCode.EC_NoError) {
					if (BaseAction.ENV != EnumEnv.DEV) {
						logger.error("入库单修改失败：" + params.get(BaseAction.JSON_ERROR_KEY.toString()));
					}
					break; // 可能是部分成功也可能是错误，不再进行审核
				} else {
					logger.info("入库单修改成功");
				}

			} else {
				logger.error("黑客行为：前端没有传递合法的参数！");
				return null;
			}

			wsApprove.setApproverID(staff.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> bmList = (List<List<BaseModel>>) warehousingBO.updateObjectEx(getStaffFromSession(session).getID(), BaseBO.CASE_ApproveWarhousing, wsApprove);
			if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("审核一个入库单失败：" + warehousingBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, warehousingBO.getLastErrorCode().toString());
				break;
			} else {
				params.put(KEY_HTMLTable_Parameter_msg, "审核成功");
				logger.info("审核入库单成功，wsApprove=" + wsApprove);
			}

			wsApprove = (Warehousing) bmList.get(0).get(0);
			List<WarehousingCommodity> whCommList = new ArrayList<>();
			for (BaseModel bm : bmList.get(1)) {
				WarehousingCommodity whComm = (WarehousingCommodity) bm;
				whCommList.add(whComm);
			}
			wsApprove.setListSlave1(whCommList);
			if (wsApprove.getPurchasingOrderID() != 0) {
				if (!updatePurchasingOrderStatus(wsApprove, params, dbName, req, purchasingOrderCommodityBO, purchasingOrderBO)) {
					logger.error("审核入库单成功，但是更新它对应的采购订单的状态失败！需要OP修改采购订单的状态：如果全部入库，就修改状态为全部入库，否则修改为部分入库。");
					params.put(KEY_HTMLTable_Parameter_msg, "审核入库单成功，但是更新它对应的采购订单的状态失败！请联系客服。");
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess.toString());
					params.put(BaseAction.KEY_Object, wsApprove);
					break;
				}
			} else {
				logger.info("计划外的入库其采购订单ID=0，所以不需要更新采购订单的状态");
				params.put(KEY_HTMLTable_Parameter_msg, "审核单号为：" + wsApprove.getSn() + "的入库单成功 ！");
			}

			// 更新缓存
			CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).write1(wsApprove, dbName, getStaffFromSession(session).getID());

			if (whCommList != null && whCommList.size() > 0) {
				Commodity comm = new Commodity();
				for (int i = 0; i < whCommList.size(); i++) {
					WarehousingCommodity wc = whCommList.get(i);
					comm.setID(wc.getCommodityID());
					CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).delete1(comm);
				}
			}

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_Object, wsApprove);

			// 如果是计划内的采购订单并且T_Message被插入了数据（int1 > 0），则需要考虑发送入库价格与采购订单上的价格不符消息到微信公众号给老板
			if (wsApprove.getPurchasingOrderID() > 0 && wsApprove.getMessageID() > 0) {
				Message message = (Message) bmList.get(2).get(0);
				if (message != null && !StringUtils.isEmpty(message.getParameter())) {
					if (!sendMsgToWx(company, message, staff, wsApprove, dbName, roleBO, messageBO)) {
						params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess.toString());
						params.put(KEY_HTMLTable_Parameter_msg, "发送微信消息失败!此消息用于提醒入库商品的进货价格与采购订单的价格不一致");
					}
				} else {
					logger.error("查询创建出来的Message的消息内容为空:Message为" + message);
					params.put(BaseWxModel.WX_ERRCODE, BaseWxModel.WXTest_ERRCODE_Failed);
					params.put(BaseWxModel.WX_ERRMSG, "查询创建出来的Message的消息内容为空:Message为" + message);
				}
			}
		} while (false);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	protected Warehousing getWarehousingCommodity(Warehousing warehousing, Map<String, Object> params, String dbName, int staffID, WarehousingCommodityBO warehousingCommodityBOCloned) {
		WarehousingCommodity wc = new WarehousingCommodity();
		wc.setWarehousingID(warehousing.getID());
		wc.setPageIndex(PAGE_StartIndex);
		wc.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<?> listTmp = warehousingCommodityBOCloned.retrieveNObject(staffID, BaseBO.INVALID_CASE_ID, wc);
		if (warehousingCommodityBOCloned.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("读取多个入库单商品失败，错误码=" + warehousingCommodityBOCloned.getLastErrorCode().toString());
			params.put(JSON_ERROR_KEY, warehousingCommodityBOCloned.getLastErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, warehousingCommodityBOCloned.getLastErrorMessage());
			return null;
		} else {
			logger.info("查询多个入库单商品成功！listTmp=" + listTmp);
		}
		warehousing.setListSlave1(listTmp);

		return warehousing;
	}

	@SuppressWarnings("unchecked")
	private boolean sendMsgToWx(Company company, Message msg, Staff staff, Warehousing wsApprove, String dbName, RoleBO roleBOCloned, MessageBO messageBOCloned) {
		if (wsApprove == null || wsApprove.getMessageID() <= 0) {
			logger.error("wsApprove == null || wsApprove.getMessageID() <= 0");
			return false;
		}

		Role role = new Role();
		role.setID(EnumTypeRole.ETR_Boss.getIndex()); // ...查询门店老板
		DataSourceContextHolder.setDbName(dbName);
		List<?> list = roleBOCloned.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_Role_RetrieveAlsoStaff, role);
		if (roleBOCloned.getLastErrorCode() != EnumErrorCode.EC_NoError || list.get(1) == null) {
			logger.error("发送入库价格与采购订单上的价格不符消息失败；查询门店老板失败！");
			return false;
		}

		boolean bSendWxMsgSuccess = false;
		boolean hasBossBindWx = false;
		List<Staff> stafflist = (List<Staff>) list.get(1);
		for (Staff staffBoss : stafflist) {
			if (staffBoss.getOpenid() == null || "".equals(staffBoss.getOpenid()) || staff.getShopID() != staffBoss.getShopID()) {
				logger.debug("用户是在哪个门店进行审核入库单，则发送微信消息给哪个门店的老板，不会发送给其他门店的老板，并且此老板可能未绑定微信，忽略发送给" + staffBoss);
				continue;
			}

			hasBossBindWx = true;
			try {
				Map<String, Object> wxMap = WxAction.sendApproveWarehousingMsg(PUBLIC_ACCOUNT_APPID, PUBLIC_ACCOUNT_SECRET, GET_ACCESSTOKEN_URL, GET_SENDTEMPLATEMSG_URL, staff, company, staffBoss.getOpenid(), WXMSG_TEMPLATEID_warehousingDiff);
				if ((int) wxMap.get(BaseWxModel.WX_ERRCODE) == BaseWxModel.WX_ERRCODE_Success) {
					bSendWxMsgSuccess = true;
				} else {
					logger.error("向" + staffBoss + "发送入库价格与采购订单上的价格不符微信消息失败!错误消息=" + wxMap.get(BaseWxModel.WX_ERRMSG));
				}
			} catch (Exception e) {
				logger.error("向门店老板发送审核入库单微信消息失败，错误信息=" + e.getMessage());
			}
		}

		if (hasBossBindWx) {
			if (bSendWxMsgSuccess) {
				logger.debug("向门店老板发送审核入库单微信消息成功！");
				DataSourceContextHolder.setDbName(dbName);
				messageBOCloned.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Message_UpdateStatus, msg);
				if (messageBOCloned.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("更新消息的状态失败：" + messageBOCloned.printErrorInfo() + "。请OP手动更新本条消息的状态为已读。消息ID=" + msg.getID());
					return false;
				}
			} else {
				logger.debug("向门店老板发送审核入库单微信消息失败！");
				return false;
			}
		} else {
			logger.debug("该门店的老板没有绑定微信,不需要发送微信消息");
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean updatePurchasingOrderStatus(Warehousing warehousing, Map<String, Object> params, String dbName, HttpServletRequest req, //
			PurchasingOrderCommodityBO purchasingOrderCommodityBO, PurchasingOrderBO purchasingOrderBO) {
		logger.info("修改一个入库单状态，warehousing=" + warehousing);

		PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
		poc.setPurchasingOrderID(warehousing.getPurchasingOrderID());

		// 判断是否还有未入库商品
		DataSourceContextHolder.setDbName(dbName);
		List<PurchasingOrderCommodity> pcList = (List<PurchasingOrderCommodity>) purchasingOrderCommodityBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing, poc);
		if (purchasingOrderCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("读取未入库商品失败，错误码=" + purchasingOrderCommodityBO.getLastErrorCode());
			params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderCommodityBO.getLastErrorCode().toString());
			return false;
		} else {
			logger.info("读取未入库商品成功，pcList=" + pcList);
		}

		EnumStatusPurchasingOrder status = EnumStatusPurchasingOrder.ESPO_PartWarehousing; // 由于前面已经入库了一次，所以默认为部分入库
		if (pcList.size() == 0) {
			status = EnumStatusPurchasingOrder.ESPO_AllWarehousing;
		}

		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setID(warehousing.getPurchasingOrderID());
		purchasingOrder.setStatus(status.getIndex());

		DataSourceContextHolder.setDbName(dbName);
		PurchasingOrder updateStatus = (PurchasingOrder) purchasingOrderBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_UpdatePurchasingOrderStatus, purchasingOrder);
		if (purchasingOrderBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("修改采购订单的状态失败，错误码=" + purchasingOrderBO.getLastErrorCode());
			// throw new RuntimeException("修改采购订单的状态失败，即将回滚");
			params.put(BaseAction.JSON_ERROR_KEY, purchasingOrderBO.getLastErrorCode().toString());
			return false;
		} else {
			params.put(KEY_HTMLTable_Parameter_msg, "审核单号为：" + warehousing.getSn() + "的入库单成功 ！");
		}
		//
		if (updateStatus != null) {
			CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).write1(updateStatus, dbName, getStaffFromSession(req.getSession()).getID());
		} else {
			logger.info("采购订单的状态修改为" + updateStatus);
		}

		return true;
	}

	/**
	 * return true 更新商品缓存成功, false 更新商品缓存失败
	 * 
	 * @throws CloneNotSupportedException
	 */
	// @SuppressWarnings("unchecked")
	// protected boolean updateCommodityCache(final Warehousing warehousing,
	// Map<String, Object> params, String dbName, HttpServletRequest req) {
	// WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
	// warehousingCommodity.setWarehousingID(warehousing.getID());
	// DataSourceContextHolder.setDbName(dbName);
	// List<WarehousingCommodity> warehousingCommodities =
	// (List<WarehousingCommodity>)
	// warehousingCommodityBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, warehousingCommodity);
	// if (warehousingCommodityBO.getLastErrorCode() !=
	// ErrorInfo.EnumErrorCode.EC_NoError) {
	// params.put(JSON_ERROR_KEY, warehousingCommodityBO.getLastErrorCode());
	// params.put(KEY_HTMLTable_Parameter_msg,
	// warehousingCommodityBO.getLastErrorMessage());
	// return false;
	// }
	// if (warehousingCommodities != null && warehousingCommodities.size() > 0) {
	// Commodity comm = new Commodity();
	// for (int i = 0; i < warehousingCommodities.size(); i++) {
	// WarehousingCommodity wc = warehousingCommodities.get(i);
	// comm.setID(wc.getCommodityID());
	// CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).delete1(comm);
	// }
	// }
	// return true;
	// }

	// /*
	// * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	// * application/x-www-form-urlencoded GET
	// * :http://localhost:8080/nbr/warehousing/approveListEx.bx?WSListIDID=2,3
	// * 目前前端未使用到该接口
	// */
	// @RequestMapping(value = "/approveListEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String approveListEx(@ModelAttribute("SpringWeb") Warehousing
	// warehousing, ModelMap mm, HttpServletRequest req) {
	// logger.info("审核多个入库单，warehousing=" + warehousing.toString());
	//
	// Company company = getCompanyFromSession(req.getSession());
	// String dbName = company.getDbName();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// do {
	// String sWSIDs = GetStringFromRequest(req, "WSListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sWSIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// logger.info("参数异常");
	// return "";
	// } else {
	// logger.info("sWSIDs=" + sWSIDs);
	// }
	//
	// int[] iArrWSID = toIntArray(sWSIDs);
	// if (iArrWSID == null) {
	// logger.info("参数缺失");
	// return "";
	// } else {
	// logger.info("iArrWSID=" + iArrWSID);
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
	// Warehousing bm = null;
	// StringBuilder sbErrorMsg = new StringBuilder();
	// for (int iWSID : iArrWSID) { // 如果审核过程中出现错误，会记录出错的地方及出错信息，并继续向后审核直到全部审核完
	// warehousing.setID(iWSID);
	// DataSourceContextHolder.setDbName(dbName);
	// Warehousing ws = (Warehousing)
	// warehousingBO.retrieve1Object(getStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, warehousing);
	// if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.info("查询一个入库单失败，错误码=" + warehousingBO.getLastErrorCode());
	// hasDBError = true;
	// break;
	// }
	//
	// ws.setApproverID(staff.getID());
	// DataSourceContextHolder.setDbName(dbName);
	// bm = (Warehousing)
	// warehousingBO.updateObject(getStaffFromSession(session).getID(),
	// BaseBO.CASE_ApproveWarhousing, ws);
	// switch (warehousingBO.getLastErrorCode()) {
	// case EC_BusinessLogicNotDefined:
	// sbErrorMsg.append("入库单" + iWSID + "审核失败，因为它已经审核过了。<br />");
	// hasDBError = true;
	// break;
	// case EC_NoError:
	// CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).write1(bm,
	// dbName, getStaffFromSession(req.getSession()).getID());//
	// ...rollback后，这里的普存并没有恢复
	//
	// if (!updatePurchasingOrderStatus(ws, params, dbName, req)) {
	// // return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString(); //
	// ...
	// sbErrorMsg.append("审核部分入库单成功，但修改相应的采购订单状态时出现问题，请重新尝试！");
	// hasDBError = true;
	// break;
	// }
	//
	// sbErrorMsg.append(params.get(KEY_HTMLTable_Parameter_msg) + "<br />");
	// break;
	// case EC_NoPermission:
	// logger.info("你没有权限审核入库单");
	// sbErrorMsg.append("你没有权限审核入库单。<br />");
	// hasDBError = true;
	// break;
	// case EC_OtherError:
	// default:
	// sbErrorMsg.append("入库单" + iWSID + "审核失败，数据库发生错误。<br />");
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
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError); //
	// 前端如果检查到msg为空，则提示成功即可，如果msg不为空，需要显示msg给用户看
	//
	// // 发送入库价格与采购订单上的价格不符消息
	// sendMsgToWx(staff, bm, params, dbName);
	// } while (false);
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * URL: http://localhost:8080/nbr/warehousing/retrieveNByFieldsEx.bx?string1=1
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNByFieldsEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String RetrieveNByFieldsEx(@ModelAttribute("SpringWeb") Warehousing warehousing, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("入库单的模糊搜索，warehousing=" + warehousing);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		boolean hasDBError = false;

		do {
			DataSourceContextHolder.setDbName(dbName);
			List<Warehousing> warehousingList = (List<Warehousing>) warehousingBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_RetrieveNWarhousingByFields, warehousing);
			if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("查询多个入库单失败：" + warehousingBO.printErrorInfo());
				}
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, warehousingBO.getLastErrorMessage());
				break;
			} else {
				logger.info("查询多个入库单成功，warehousingList=" + warehousingList);
			}

			if (!setCreatorAndApprover(warehousingList, dbName, req)) {
				hasDBError = true;
				break;
			}

			for (Warehousing ws : warehousingList) {
				if (!loadWarehouse(ws, dbName, req) || !loadProvider(ws, dbName, req)) {
					hasDBError = true;
					break;
				}
			}

			if (hasDBError) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			}

			logger.info("查询成功=" + warehousingList);
			params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, warehousingBO.getTotalRecord());
			params.put("warehousingList", warehousingList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: URL: GET
	 * :http://localhost:8080/nbr/warehousing/deleteEx.bx?ID=1
	 */
	@RequestMapping(value = "/deleteEx", method = RequestMethod.GET)
	@ResponseBody
	public String deleteEx(@ModelAttribute("SpringWeb") Warehousing warehousing, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("删除一个入库单，warehousing=" + warehousing);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		warehousingBO.deleteObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehousing);

		logger.info("Delete Warehousing error code=" + warehousingBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (warehousingBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Warehousing).delete1(warehousing);

			logger.info("删除成功！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		case EC_BusinessLogicNotDefined:
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("该入库单已审核，无法删除！");
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "该入库单已审核，无法删除！");
			break;
		case EC_NoPermission:
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("没有删除权限，无法删除！");
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足！");
			break;
		case EC_WrongFormatForInputField:
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("参数格式错误");
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "参数格式错误！");
			break;
		default:
			logger.error("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误！");
			break;
		}
		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	// /*
	// * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	// * application/x-www-form-urlencoded Request Body in Fiddler: GET :URL:
	// * http://localhost:8080/nbr/warehousing/deleteListEx.bx?WSListID=2,5
	// */
	// @RequestMapping(value = "/deleteListEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteListEx(@ModelAttribute("SpringWeb") Warehousing
	// warehousing, ModelMap model, HttpServletRequest req) {
	// logger.info("删除多个入库单，warehousing=" + warehousing.toString());
	//
	// Company company = getCompanyFromSession(req.getSession());
	// String dbName = company.getDbName();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// do {
	// String sWSIDs = GetStringFromRequest(req, "WSListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sWSIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// logger.info("参数异常");
	// return "";
	// } else {
	// logger.info("sWSIDs=" + sWSIDs);
	// }
	//
	// int[] iArrWSID = toIntArray(sWSIDs);
	// if (iArrWSID == null) {
	// logger.info("参数缺失");
	// return "";
	// } else {
	// logger.info("iArrWSID" + iArrWSID);
	// }
	//
	// boolean hasDBError = false;
	// StringBuilder sbErrorMsg = new StringBuilder();
	// for (int iID : iArrWSID) { // 如果删除过程中出现错误，会记录出错的地方及出错信息，并继续向后删除，直到无物可删
	// Warehousing ws = new Warehousing();
	// ws.setID(iID);
	// DataSourceContextHolder.setDbName(dbName);
	// warehousingBO.deleteObject(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.INVALID_CASE_ID, ws);
	// switch (warehousingBO.getLastErrorCode()) {
	// case EC_BusinessLogicNotDefined:
	// sbErrorMsg.append("入库单" + iID + "删除失败，因为它已经审核过。<br />");
	// hasDBError = true;
	// break;
	// case EC_NoError:
	// CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).delete1(ws);
	// break;
	// case EC_NoPermission:
	// hasDBError = true;
	// logger.info("你没有权限删除入库单");
	// sbErrorMsg.append("你没有权限删除入库单。<br />");
	// break;
	// case EC_OtherError:
	// default:
	// sbErrorMsg.append("入库单" + iID + "删除失败，数据库发生错误。<br />");
	// hasDBError = true;
	// break;
	// }
	// }
	// params.put("msg", sbErrorMsg.toString());
	//
	// if (hasDBError) {
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);//
	// 前端必须显示msg给用户看
	// break;
	// }
	//
	// logger.info("全部入库单删除删除成功，sbErrorMsg=" + sbErrorMsg.toString());
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// // 前端如果检查到msg为空，则提示成功即可，如果msg不为空，需要显示msg给用户看
	// } while (false);
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/**
	 * 1、更新入库单 <br />
	 * 2、用会话中的从表信息更新入库单的从表。更新前先删除入库单的从表信息<br />
	 * 3、由调用者更新缓存<br />
	 * 
	 * @throws CloneNotSupportedException
	 */
	private Warehousing doUpdate(Warehousing warehousing, List<WarehousingCommodity> listWSSession, Map<String, Object> params, String dbName, HttpServletRequest req) {
		logger.info("修改入库单，warehousing=" + warehousing);

		// 检查输入合法性
		if (!checkCreate(req, dbName, params, listWSSession)) {
			return null;
		}

		// 修改入库单
		DataSourceContextHolder.setDbName(dbName);
		Warehousing wsUpdate = (Warehousing) warehousingBO.updateObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, warehousing);
		if (wsUpdate == null && warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("修改一个入库单失败：" + warehousingBO.printErrorInfo());
			}
			params.put(BaseAction.JSON_ERROR_KEY, warehousingBO.getLastErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, "修改入库单失败，失败原因：" + warehousingBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("修改一个入库单成功，wsUpdate=" + wsUpdate);
		}

		// 先删除入库单原先的商品，再添加新的商品
		WarehousingCommodity wc = new WarehousingCommodity();
		wc.setWarehousingID(wsUpdate.getID());
		//
		DataSourceContextHolder.setDbName(dbName);
		warehousingCommodityBO.deleteObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, wc);
		if (warehousingCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("删除多个入库单商品表失败：" + warehousingCommodityBO.printErrorInfo());
			params.put(BaseAction.JSON_ERROR_KEY, warehousingCommodityBO.getLastErrorCode());
			params.put(KEY_HTMLTable_Parameter_msg, "删除入库单商品失败，失败原因：" + warehousingCommodityBO.getLastErrorMessage());
			return null;
		} else {
			logger.info("删除多个入库单商品表成功");
		}

		// 添加入库单商品
		List<WarehousingCommodity> listTmp = new ArrayList<WarehousingCommodity>();
		boolean hasError = false;
		for (int i = 0; i < listWSSession.size(); i++) {
			WarehousingCommodity warehousingCommodity = listWSSession.get(i);
			warehousingCommodity.setShelfLife(Commodity.DEFAULT_VALUE_ShelfLife); // ..给定一个默认的保质期为一年。
			//
			DataSourceContextHolder.setDbName(dbName);
			BaseModel bm = warehousingCommodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, warehousingCommodity);
			if (warehousingCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) { // ....
				hasError = true;
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("商品ID=" + warehousingCommodity.getCommodityID() + ":" + warehousingCommodityBO.printErrorInfo());
				}
				ErrorInfo ecOut = new ErrorInfo();
				Commodity commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(warehousingCommodity.getCommodityID(), getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
				if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询一个商品信息失败:" + ecOut.toString());
				}
				logger.error("入库商品：" + (commodity == null ? "未知入库商品" : commodity.getName()) + "[ID=" + warehousingCommodity.getCommodityID() + "]创建失败\t");
				continue;
			}

			listTmp.add((WarehousingCommodity) bm);
		}

		//
		wsUpdate.setListSlave1(listTmp);
		//
		if (hasError) {
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess);
			params.put(KEY_HTMLTable_Parameter_msg, "修改入库单失败。请重试。");
		} else {
			params.put(BaseAction.JSON_ERROR_KEY, warehousingCommodityBO.getLastErrorCode());
		}

		return wsUpdate;
	}

	/*
	 * Request Body in Fiddler: User-Agent: Fiddler Host: localhost:8080
	 * Content-Type: application/json; charset=utf-8 Request Body in Fiddler:
	 * purchasingOrder.ID=1&purchasingOrder.purchasingPlanTableID=2 URL: POST:
	 * http://localhost:8080/nbr/warehousing/updateEx.bx
	 */
	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Warehousing warehousing, ModelMap model, HttpServletRequest req, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改一个入库单，warehousing=" + warehousing);

		Company company = getCompanyFromSession(session);
		logger.info("当前的方法名称：" + new Exception().getStackTrace()[0].getMethodName() + "\t\tFiddler的http头的cookie字符串为：\nCookie: JSESSIONID=" + session.getId());

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {

			// // 加载商品表，供应商
			// if (!loadCommodityList(ws, company.getDbName(), req) || !loadProvider(ws,
			// company.getDbName(), req)) {
			// params.put(KEY_HTMLTable_Parameter_msg, "入库单已正确的修改成功，但加载商品或供应商时失败！");
			// // return "";// ...没有正确处理失败
			// }
			DataSourceContextHolder.setDbName(company.getDbName());
			List<List<BaseModel>> whList = warehousingBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, warehousing);
			if(warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || !(whList != null && whList.get(0).size() != 0)) {
				logger.error("查询该入库单失败");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoSuchData);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "审核失败：查无该入库单");
				break;
			}
			Warehousing whR1 = (Warehousing) whList.get(0).get(0);
			//
			Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
			if(staff.getShopID() != 1 && staff.getShopID() != whR1.getShopID()) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "不能跨门店修改入库单");
				break;
			}
			//
			List<WarehousingCommodity> listWc = parseWarehousingCommodityList(warehousing, req, company.getDbName());
			if (listWc == null) { // || listWc.size() == 0
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("黑客行为");
				}
				return null;
			}

			Warehousing warehousingUpdate = doUpdate(warehousing, listWc, params, company.getDbName(), req);
			if (params.get(BaseAction.JSON_ERROR_KEY.toString()) != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV && params.get(BaseAction.JSON_ERROR_KEY.toString()) != EnumErrorCode.EC_WrongFormatForInputField) {
					logger.error("入库单修改失败：" + params.get(BaseAction.JSON_ERROR_KEY.toString()));
				}
				break;
			} else {
				logger.info("入库单修改成功");
				CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Warehousing).write1(warehousingUpdate, company.getDbName(), getStaffFromSession(req.getSession()).getID());
			}

			logger.info("ws=" + warehousingUpdate);
			params.put(KEY_HTMLTable_Parameter_TotalRecord, 1);
			params.put(KEY_Object, warehousingUpdate);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			break;
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/** @return 返回null，参数格式错误或商品个数为0 */
	protected List<WarehousingCommodity> parseWarehousingCommodityList(Warehousing warehousing, HttpServletRequest req, String dbName) {
		String sCommIDs = GetStringFromRequest(req, "commIDs", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sCommNOs = GetStringFromRequest(req, "commNOs", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sCommPrices = GetStringFromRequest(req, "commPrices", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sAmounts = GetStringFromRequest(req, "amounts", String.valueOf(BaseAction.INVALID_ID)).trim();
		String sBarcodeIDs = GetStringFromRequest(req, "barcodeIDs", String.valueOf(BaseAction.INVALID_ID)).trim();
		if (sCommIDs.equals(String.valueOf(BaseAction.INVALID_ID)) || sCommNOs.equals(String.valueOf(BaseAction.INVALID_ID)) || sCommPrices.equals(String.valueOf(BaseAction.INVALID_ID))
				|| sAmounts.equals(String.valueOf(BaseAction.INVALID_ID)) || sBarcodeIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("参数异常");
			}
			return null;
		} else {
			logger.info("sCommIDs=" + sCommIDs + ",sCommNOs=" + sCommNOs + ",sCommPrices=" + sCommPrices + ",sAmounts=" + sAmounts + ",sBarcodeIDs=" + sBarcodeIDs);
		}

		ErrorInfo ecOut = new ErrorInfo();
		ConfigGeneral maxWarehousingCommodityNO = (ConfigGeneral) CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.MaxWarehousingCommodityNO, getStaffFromSession(req.getSession()).getID(), ecOut, dbName);
		Integer[] iaCommID = GeneralUtil.toIntArray(sCommIDs);
		Integer[] iaCommNO = GeneralUtil.toIntArray(sCommNOs);
		Integer[] iaBarcodeID = GeneralUtil.toIntArray(sBarcodeIDs);
		Double[] iaCommPrices = GeneralUtil.toDoubleArray(sCommPrices);
		Double[] iaAmount = GeneralUtil.toDoubleArray(sAmounts);

		if (iaCommID == null || iaBarcodeID == null || iaCommNO == null || iaCommPrices == null || iaCommID.length != iaCommNO.length || iaCommID.length != iaBarcodeID.length || iaCommID.length != iaCommPrices.length
				|| iaCommID.length != iaAmount.length || iaCommID.length == 0 || iaCommID.length > Integer.parseInt(maxWarehousingCommodityNO.getValue())) {
			logger.info("参数长度不符合或没有传入参数");
			return null;
		} else {
			logger.info("iaCommID=" + GeneralUtil.printArray(iaCommID) + ",iaCommNO=" + GeneralUtil.printArray(iaCommNO) + ",iaBarcodeID=" + GeneralUtil.printArray(iaBarcodeID) + ",iaCommPrices=" + GeneralUtil.printArray(iaCommPrices)
					+ ",iaAmount=" + GeneralUtil.printArray(iaAmount));
		}
		// 一旦发现iaCommID内部有重复元素，当黑客行为处理
		if (GeneralUtil.hasDuplicatedElement(iaCommID)) {
			logger.error("黑客传递的参数有重复！");
			return null;
		}
		//
		List<WarehousingCommodity> warehousingCommodityList = new ArrayList<WarehousingCommodity>();
		for (int i = 0; i < iaCommID.length; i++) {
			WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
			warehousingCommodity.setBarcodeID(iaBarcodeID[i]);
			warehousingCommodity.setCommodityID(iaCommID[i]);
			warehousingCommodity.setNO(iaCommNO[i]);
			warehousingCommodity.setWarehousingID(warehousing.getID());
			warehousingCommodity.setPrice(iaCommPrices[i]);
			warehousingCommodity.setAmount(iaAmount[i]);

			warehousingCommodityList.add(warehousingCommodity);
		}

		return warehousingCommodityList;
	}

}
