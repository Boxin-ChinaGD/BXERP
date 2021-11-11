package com.bx.erp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.Company;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Vip;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
public class BaseAction {
	private Log logger = LogFactory.getLog(BaseAction.class);

	/** 标明当前的运行环境，在系统启动时从配置文件中加载。加载完成后，不能更改值！ */
	public static EnumEnv ENV = EnumEnv.PROD;

	public static final String DEV_DOMAIN = "https://dev.wx.bxit.vip/";
	public static final String SIT_DOMAIN = "https://sit.wx.bxit.vip/";
	public static final String UAT_DOMAIN = "https://uat.wx.bxit.vip/";
	public static final String PROD_DOMAIN = "https://account.prosalesbox.cn/";

	public static String DOMAIN = DEV_DOMAIN;

	/** 公共DB的名字 */
	public final static String DBName_Public = "nbr_bx";
	/** 静态DB的名字 */
	public static final String DBName_StaticDB = "StaticDB";
	/** 博昕虚拟门店的编号 */
	public final static String SN_nbr = "668866";
	/** 博昕虚拟总部的ID */
	public final static int VirtualShopID = 1;

	/** 开新公司时，会默认创建个虚拟总部的特殊门店，其名字定为“虚拟总部” */
	public static final String ShopeName_Invented = "虚拟总部";
	/** 开新公司时，会默认创建个门店，其名字定为“默认门店” */
	public static final String ShopeName_Default = "默认门店";
	/** 开新公司时，会默认创建个门店，其地址定为“默认地址” */
	public static final String ShopeAddress_Default = "默认地址";
	/** 售前帐号。仅在业务经理交付POS机时有效。老板修改密码后失效 */
	public static final String ACCOUNT_Phone_PreSale = "13888888888";
	/** 售前帐号的密码。 */
	public static final String PASSWORD_PreSale = "B1AFC07474C37C5AEC4199ED28E09705"; // ...000000
	/** 售前帐号的名称。 */
	public static final String NAME_PreSale = "BX";
	/** 开新公司时，会默认创建个门店，其名字定为“默认门店ID” POS需要用到 */
	public static final String ShopeID_Default = "shopID";
	/** 开新公司时，会默认创建个，默认区域，创建默认门店时需要使用到该默认区域的ID */
	public static final Integer DistrictID_Default = 1;
	public static final int DepartmentID_Default = 1;
	/** 默认的小票格式 */
	public static final Integer DEFAULT_SmallSheetID = 1;
	/** 目前只有一种会员卡 */
	public static final int DEFAULT_VipCardID = 1;
	/** 目前不搞会员类别 */
	public static final int DEFAULT_VipCategoryID = 1;

	// 前端的html表格加载数据时，需要这3个key对应的值
	public static final String KEY_HTMLTable_Parameter_TotalRecord = "count";
	public static final String KEY_HTMLTable_Parameter_msg = "msg";
	public static final String KEY_HTMLTable_Parameter_code = "code";

	// 前端传值所需
	// protected final String SESSION_OrderCommList = "orderCommList";
	// protected final String SESSION_CommList="commList";
	// protected final String SESSION_CommodityList = "commodityList";

	private static int iEnumSessionIndex = 0;

	/** 为当前登录用户的session，不可清除该session */
	public enum EnumSession {
		/** staff登录时，为了浏览器获取token而临时存储staff phone 的会话 */
		SESSION_StaffPhone("SESSION_StaffPhone", iEnumSessionIndex++), //
		/** bxstaff登录时，为了浏览器获取token而临时存储bxstaff phone 的会话 */
		SESSION_BxStaffMobile("SESSION_BxStaffMobile", iEnumSessionIndex++), //

		SESSION_PurchasingOrderCommodityList("SESSION_PurchasingOrderCommodityList", iEnumSessionIndex++),
		/**
		 * 用户编辑采购订单的商品列表时（第一次编辑前清空会话），需要增减商品。增减后，商品列表被保存在本会话中，作为临时的商品列表。<br />
		 * 当用户保存临时的商品列表时，数据库里面的商品列表会被删除，而会话里保存的列表会被插入数据库中；如果用户不保存这个临时的商品列表，则清空这个会话。<br
		 * />
		 */
		SESSION_PurchasingOrderTempPurchasingOrderCommodityList("SESSION_PurchasingOrderTempPurchasingOrderCommodityList", iEnumSessionIndex++), //
		SESSION_PurchasingOrder("SESSION_PurchasingOrder", 2), //
		SESSION_Warehousing("SESSION_Warehousing", 3), //
		SESSION_WarehousingTempWarehousingCommodityList("SESSION_WarehousingTempWarehousingCommodityList", iEnumSessionIndex++), //
		SESSION_WarehousingCommodityList("SESSION_WarehousingCommodityList", iEnumSessionIndex++), //
		SESSION_WarehousingInfo("SESSION_WarehousingInfo", iEnumSessionIndex++), //
		SESSION_OrderCommodityList("SESSION_OrderCommodityList", iEnumSessionIndex++), //
		/** POS机成功登录后，保持POS信息的会话 */
		SESSION_POS("SESSION_POS", iEnumSessionIndex++), //
		/** POS机登录时，为了浏览器获取token而临时存储posID的会话 */
		SESSION_POS_ID("SESSION_POS_ID", iEnumSessionIndex++), //
		SESSION_CommodityPictureDestination("SESSION_CommodityPictureDestination", iEnumSessionIndex++), // CommoditySyncAction
		SESSION_PictureFILE("SESSION_PictureFILE", iEnumSessionIndex++), // CommoditySyncAction中的picture文件上传
		SESSION_OrderCommList("SESSION_OrderCommList", iEnumSessionIndex++), //
		SESSION_CommList("SESSION_CommList", iEnumSessionIndex++), //
		SESSION_CommodityList("SESSION_CommodityList", iEnumSessionIndex++), //
		/** 为当前登录的staff的session，不可清除该session */
		SESSION_Staff("SESSION_Staff", iEnumSessionIndex++), //
		SESSION_InventorySheet("SESSION_InventorySheet", iEnumSessionIndex++), //
		SESSION_InventorySheetTempInventoryCommodityList("SESSION_InventorySheetTempInventoryCommodityList", iEnumSessionIndex++), //
		SESSION_InventoryCommodityList("SESSION_InventoryCommodityList", iEnumSessionIndex++), //
		SESSION_InventorySheetWarehouse("SESSION_InventorySheetWarehouse", iEnumSessionIndex++), //
		/** 为当前登录的博昕员工的session，不可清除该session */
		SESSION_BxStaff("SESSION_BxStaff", iEnumSessionIndex++), //
		/** 保存当前用户私有DB的名字。做DB操作时，AOP需要这个名字来确定数据源是哪个私有DB */
		SESSION_Company("SESSIONCompany", iEnumSessionIndex++), //
		SESSION_ReturnCommoditySheet("SESSION_ReturnCommoditySheet", iEnumSessionIndex++), //
		SESSION_ReturnCommoditySheetTempReturnCommoditySheetCommodityList("SESSION_ReturnCommoditySheetTempReturnCommoditySheetCommodityList", iEnumSessionIndex++), //
		SESSION_CompanyBusinessLicensePictureDestination("SESSION_CompanyBusinessLicensePictureDestination", iEnumSessionIndex++), // CompanyAction
		SESSION_CompanyBusinessLicensePictureFILE("SESSION_CompanyBusinessLicensePictureFILE", iEnumSessionIndex++), // CompanyAction中的营业执照文件上传
		//
		SESSION_Vip("SESSION_Vip", iEnumSessionIndex++), //
		SESSION_CompanyLogoDestination("SESSION_CompanyLogoDestination", iEnumSessionIndex++), // CompanyAction
		SESSION_CompanyLogoFILE("SESSION_CompanyLogoFILE", iEnumSessionIndex++); // CompanyAction中的Logo文件上传

		private String name;
		private int index;

		private EnumSession(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumSession c : EnumSession.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		/** 将会话session中的key/value清空 */
		public static void clearAllSession(HttpSession session) {
			for (int i = 2; i <= EnumSession.values().length - 1; i++) { // 从2开始，避开登录必须使用的2个会话
				// System.out.println("WWW会话名称是=" + EnumSession.getName(i));
				try {
					session.removeAttribute(EnumSession.getName(i));
				} catch (Exception e) {
				}
			}
		}
	}

	protected static final String BX_Admin_ToLogin = "bx/admin/login";
	protected static final String BX_Admin_Company = "company/index";

	protected static final String HOME_Index = "common/index";
	protected static final String HOME_Header = "common/header";
	protected static final String HOME_Sidebar = "common/sidebar";
	protected static final String HOME_Home = "common/home";

	protected static final String ADMIN_ToLogin = "admin/login";
	protected static final String ADMIN_FirstLogin = "admin/UpdatePwd";
	protected static final String ADMIN_Logout = "ADMIN_Logout";
	protected static final String ADMIN_LoginSuccess = "ADMIN_LoginSuccess";
	protected static final String ADMIN_ToAdminLogin = "ADMIN_ToAdminLogin";

	public static final String STAFF_Logout = "STAFF_Logout";
	protected static final String STAFF_Login = "STAFF_Login";
	protected static final String STAFF_ToLogin = "STAFF_ToLogin";

	protected static final String BRAND_Index = "profile/brand/index";
	protected static final String BRAND_Update = "BRAND_Update";
	protected static final String BRAND_Delete = " BRAND_Delete";
	protected static final String BRAND_Create = "redirect:/brand.bx";
	protected static final String BRAND_Retrieve1 = "BRAND_Retrieve1";
	protected static final String BRAND_RetrieveN = "BRAND_RetrieveN";
	protected static final String BRAND_ToRetrieveN = "BRAND_ToRetrieveN";

	protected static final String COMPANY_Index = "company/shop/index";
	protected static final String COMPANY_CreateShop = "company/shop/createShop";
//	protected static final String CACHE_Index = "cache/index";
	protected static final String CACHE_Update = "cache/update";

	protected static final String COMMODITY_Index = "profile/commodity/index";
	protected static final String COMMODITY_About = "profile/commodity/about";
	protected static final String COMMODITY_Create = "redirect:/commodity.bx";
	protected static final String COMMODITY_ToCreate = "profile/commodity/create";
	protected static final String COMMODITY_ToUpdate = "profile/commodity/manage";
	protected static final String COMMODITY_Retrieve1 = "profile/commodity/details";
	protected static final String COMMODITY_Combination = "profile/commodity/combination";
	protected static final String COMMODITY_Detele = "COMMODITY_Detele";
	protected static final String COMMODITY_RetrieveN = "redirect:commodity.bx";
	protected static final String COMMODITY_Update = "redirect:/commodity.bx";
	protected static final String COMMODITY_RetrieveInventory = "COMMODITY_RetrieveInventory";

	protected static final String COMMODITYProperty_Index = "profile/commodity/property";

	protected static final String COMMODITYHISTORY_Index = "profile/commodity/history";

	protected static final String COMMODITY_CATEGORY_Index = "profile/category/index";

	protected static final String COUPON_Manage = "vip/couponManage";

	protected static final String PROFILE_Index = "profile/index";
	protected static final String TO_ImportData = "profile/commodity/importData";

	protected static final String PROMOTION_Index = "promotion/index";
	protected static final String PROMOTION_Coupon = "promotion/coupon";
	protected static final String PROMOTION_Sheet = "promotion/sheet";

	protected static final String CATEGORY_Create = "redirect:/category.bx";
	protected static final String CATEGORY_Delete = "CATEGORY_Delete";
	protected static final String CATEGORY_Update = "CATEGORY_Update";
	protected static final String CATEGORY_ToCreate = "CATEGORY_ToCreate";
	protected static final String CATEGORY_RetrieveN = "CATEGORY_RetrieveN";
	protected static final String CATEGORY_ToRetrieveN = "CATEGORY_ToRetrieveN";
	protected static final String CATEGORY_List = "CATEGORY_List";

	protected static final String PROVIDER_Index = "provider/index";
	protected static final String PROVIDER_ToCreate = "provider/create";
	protected static final String PROVIDER_Create = "category/index";
	protected static final String PROVIDER_ToUpdate = "provider/manage";
	protected static final String PROVIDER_Update = "provider/manage";
	protected static final String PROVIDER_RetrieveN = "provider/index";
	protected static final String PROVIDER_Retrieve1 = "PROVIDER_Retrieve1";
	protected static final String PROVIDER_ToRetrieveN = "category/index";
	protected static final String PROVIDER_ToRetrieve1 = "PROVIDER_ToRetrieve1";

	protected static final String ProviderDistrict_Create = "providerDistrict/create";

	protected static final String PURCHASINGORDER_Index = "purchase/index";
	protected static final String PURCHASINGORDER_Order = "purchase/order/index";
	protected static final String PURCHASINGORDER_Region = "purchase/region/index";
	protected static final String PURCHASINGORDER_ProviderProfile = "purchase/provider/profile";

	protected static final String PURCHASINGPLANTABLE_Create = "purchase/index";
	protected static final String PURCHASINGPLANTABLE_Retrieve1 = "purchase/index";
	protected static final String PURCHASINGPLANTABLE_RetrieveN = "purchase/index";

	protected static final String PURCHASINGPLANTABLECOMMODITY_Update = "purchase/index";

	protected static final String PURCHASINGORDER_Approve = "provider/profile";
	protected static final String PURCHASINGORDER_Create = "redirect:/retrieveN";
	protected static final String PURCHASINGORDER_ToCreate = "purchase/order/create";
	protected static final String PURCHASINGORDER_Delete = "provider/profile";
	protected static final String PURCHASINGORDER_RetrieveN = "provider/profile";
	protected static final String PURCHASINGORDER_ToRetrieveN = "provider/profile";
	protected static final String PURCHASINGORDER_Retrieve1 = "purchase/order/details";
	protected static final String PURCHASINGORDER_Update = "PURCHASINGORDER_Update";

	// protected static final String WAREHOUSING_Index = "warehousing/index";
	protected static final String WAREHOUSING_warehouseStock = "warehousing/warehouseStock";
	protected static final String WAREHOUSINGSHEET_Index = "warehousing/manage";

	// protected static final String WAREHOUSE_Create = "warehousing/index";
	// protected static final String WAREHOUSE_Retrieve1 = "warehousing/index";
	// protected static final String WAREHOUSE_RetrieveN = "warehousing/index";
	// protected static final String WAREHOUSE_Update = "warehousing/index";
	// protected static final String WAREHOUSE_Delete = "warehousing/index";

	protected static final String WAREHOUSINGSHEET_ToIndex = "WAREHOUSINGSHEET_ToIndex";
	protected static final String WAREHOUSINGCOMMODITY_ToIndex = "WAREHOUSINGCOMMODITY_ToIndex";

	protected static final String INVENTORYSHEET_Index = "warehousing/inventory/index";
	// protected static final String INVENTORYSHEET_ToPlan =
	// "warehousing/inventory/plan";
	// protected static final String INVENTORYSHEET_ToInput =
	// "warehousing/inventory/input";
	// protected static final String INVENTORY_ToApprove =
	// "warehousing/inventory/approve";
	protected static final String WAREHOUSING_ReturnPurchasingCommodity = "warehousing/returnPurchasingCommodity";

	// protected static final String INVENTORYSHEET_Create = "inventory/approve";
	// protected static final String INVENTORYSHEET_Delete = "inventory/index";
	// protected static final String INVENTORYSHEET_Input =
	// "warehousing/inventory/details";
	// protected static final String INVENTORYSHEET_Approve = "inventory/index";

	// protected static final String INVENTORY_ToRetrieveN = "inventory/approve";
	// protected static final String INVENTORY_RetrieveN = "inventory/approve";
	// protected static final String INVENTORY_Submit =
	// "warehousing/inventory/approve";
	// protected static final String INVENTORY_ToSubmit =
	// "warehousing/inventory/approve";
	// protected static final String INVENTORY_Update = "inventory/approve";
	// protected static final String INVENTORY_ToUpdate = "inventory/approve";
	// protected static final String INVENTORYCOMM_Update = "inventory/approve";
	// protected static final String INVENTORYCOMM_ToUpdate = "inventory/approve";
	// protected static final String INVENTORYCOMM_RetrieveN = "inventory/approve";

	protected static final String WAREHOUSINGREPORT_OperatingStatement = "warehousingReport/operatingStatement";
	// protected static final String WAREHOUSINGREPORT_Index =
	// "warehousingReport/sales";
	// protected static final String WAREHOUSINGREPORT_Stock =
	// "warehousingReport/stock";
	// protected static final String WAREHOUSINGREPORT_Status =
	// "warehousingReport/status";

	// protected static final String wx3rdPartyCard_cardList = "card/list";
	// protected static final String WX3RDPARTYCARD_Create = "card/create";
	// protected static final String WX3RDPARTYCARD_CouponDetail =
	// "card/couponDetail";

	// protected static final String WX3RDPARTYCARD_vipCard = "vip/vipCard";
	// protected static final String WX3RDPARTYCARD_vipCardToCreate =
	// "vip/vipCardToCreate";
	// protected static final String WX3RDPARTYCARD_vipCardDetail =
	// "vip/vipCardDetail";
	// protected static final String WX3RDPARTYCARD_vipDetail = "vip/vipDetail";

	protected static final String RETAILTRADE_Index = "warehousingReport/salesRecord";

	// protected static final String ROLE_Index = "staff/role/index";

	protected static final String STAFF_Index = "staff/index";
	// protected static final String STAFF_ToCreate = "staff/create";
	// protected static final String STAFF_ToResetPwd = "staff/resetPassword";

	// protected static final String PROMOTION_Create = "promotion/create";
	// protected static final String PROMOTION_Delete = "promotion/delete";

	// protected static final String TRADE_Index = "trade/index";
	// protected static final String TRADE_Pos = "trade/pos";

	// protected static final String PACKAGEUNIT_Index =
	// "profile/packageUnit/index";

	protected static final String VIP_VipCardManage = "vip/vipCardManage";
	protected static final String VIP_BonusHistory = "vip/bonusHistory";
	protected static final String VIP_MemberManagement = "vip/memberManagement";
	// protected static final String VIP_Index = "vip/index";
	// protected static final String VIP_Create = "vip/create";
	// protected static final String VIP_ToCreate = "vip/create";
	// protected static final String VIP_Update = "vip/update";
	// protected static final String VIP_Retrieve1Ex = "vip/retrieve1EX";
	// protected static final String VIP_RetrieveAllEx = "vip/retrieveAllEX";
	protected static final String WX_Bind = "wx/bind";
	protected static final String WX_PurchasingOrderApproval = "wx/purchasingOrderApproval";
	protected static final String WX_UnsalableCommodity = "wx/unsalableCommodity";
	protected static final String WX_Login = "wx/wx_Login";

	protected static final String MINIPROGRAM_QRCodeToCreate = "wx/mp/QRcode";

	// TotalRecord和iErrorCode
	public static final String SP_OUT_PARAM_iTotalRecord = "iTotalRecord";
	public static final String SP_OUT_PARAM_iErrorCode = "iErrorCode";
	public static final String SP_OUT_PARAM_sErrorMsg = "sErrorMsg";

	// Public var
	public static final int PAGE_SIZE = 10;
	public static final int PAGE_StartIndex = 1;
	public static final int PAGE_SIZE_Infinite = 100000000;

	// 当测试需要传一个很大的PageSize时，可以修改该变量，功能代码不能修改该值
	public static int PAGE_SIZE_MAX = 50;

	/** 上传商品图片的目录，更改此处时应相应的更改config.sql和pom.xml中的相应路径 **/
	public static final String CommodityPictureDir = "D:/nbr/pic/private_db/";

	/** 上传API证书的目录，更改此处时应相应的更改config.sql中的相应路径 **/
	public static final String CompanyAPICertDir = "D:/nbr/apicert/";

	/** 上传营业执照的目录，更改此处时应相应的更改config.sql和pom.xml中的相应路径 **/
	public static final String BusinessLicensePictureDir = "D:/nbr/pic/common_db/license/";
	
	/** 上传公司Logo的目录，更改此处时应相应的更改config.sql和pom.xml中的相应路径 **/
	public static final String CompanyLogoDir = "D:/nbr/pic/common_db/logo/";

	/** 导入旧系统的商品和会员的目录，更改此处时应相应的更改config.sql和pom.xml中的相应路径 **/
	public static final String ImportCommodityAndVipDir = "D:/nbr/file/import/";
	
	// TODO 跟商品图片放一个目录会导致商品图片rename失败
	/** 存放小程序码图片的路径。不能放在商品图片目录下！因为这里会有问题：CommoditySyncAction:line 1595 **/
	public static final String QrCodePictureDir = "D:/nbr/pic/mp/";

	/** 代表不可以退货 */
	public static final int MIN_ReturnCommodityDays = 0;

	/** 传这个特殊的providerID作为参数时，代表要删除对应商品的所有的供应商 */
	public static final int PROVIDER_ID_ToDeleteAll = 0;

	public static final String NAMEDATABING_PageCount = "PAGE_COUNT";
	public static final String NAMEDATABING_TotalRecord = "TOTAL_RECORD";

	/** 网页端创建数据时，对应的POS会话不存在，其传递给SP的ID默认为本常量。在SP中，需要对其进行正确的处理 */
	public static final int INVALID_POS_ID = -1;
	public static final int INVALID_ID = -1;
	public static final int INVALID_NO = -1;
	public static final int INVALID_Type = -1;

	public static final String DATE_FORMAT_Default1 = "yyyy-MM-dd";
	public static final String DATETIME_FORMAT_Default1 = "yyyy-MM-dd HH:mm:ss SSS";
	public static final String DATETIME_FORMAT_Default2 = "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIME_FORMAT_Default3 = "yyyy/MM/dd HH:mm:ss";
	public static final String DATETIME_FORMAT_Default4 = "yyyy/MM/dd HH:mm:ss";
	public static final String DATETIME_FORMAT_Default5 = "yyyy/MM/dd HH:mm";
	public static final String DATETIME_FORMAT_Default_Chinese = "yyyy年MM月dd日  HH:mm:ss";
	public static final String TIME_FORMAT_ConfigGeneral = "HH:mm:ss";
	public static final String DATE_FORMAT_RetailTradeSN = "yyyyMMddHHmmss"; // 零售单SN中的日期格式

	public static final String JSON_ERROR_KEY = "ERROR";
	public static final String JSON_PROCESS_KEY = "PROCESS";// 用于进程通信服务返回的json key

	public static final String SHADOW = "Kim.Lee@bxit.vip";

	public static final String KEY_ObjectList = "objectList";
	public static final String KEY_ObjectList2 = "objectList2";
	public static final String KEY_Object = "object";
	public static final String KEY_Object2 = "object2";
	public static final String KEY_Object3 = "object3";

	public static final String RESIGNED_ID = "resigned_ID";

	/** 微信支付使用沙箱环境 默认使用沙箱：true 不使用：false */
	public static boolean UseSandBox = false;

	/**
	 * 通常SP_xxxxx_RetrieveNqqqqq需要根据传进的STATUS值决定是否搜索包含这个STATUS搜索因子的对象。如果STATUS=-1，将忽略这个搜索因子
	 */
	public static final int INVALID_STATUS = -1;

	protected int GetIntFromRequest(HttpServletRequest httpReq, String name, int iDefaultValue) {
		String index = httpReq.getParameter(name);
		if (index != null) {
			try {
				return Integer.parseInt(index.trim());
			} finally {
			}
		}
		return iDefaultValue;
	}

	/**
	 * 检查前端有无传递参数name过来，如果没有，设置其默认值为sDefaultValue。 Action可以因此判断是否黑客行为，或直接采用默认值进行后续处理
	 */
	protected String GetStringFromRequest(HttpServletRequest httpReq, String name, String sDefaultValue) {
		String param = httpReq.getParameter(name);
		return (param == null ? sDefaultValue : param);
	}

	// public static int mergeIDAsSyncCacheID(int mainID, int subID) {
	// return (mainID << 16) | subID;
	// }
	//
	// public static void splitMergedSyncCacheID(int mergedID, List<Integer> listID)
	// {
	// listID.set(0, mergedID >> 16);
	// listID.set(1, mergedID & 0x00001111);
	// }

	/**
	 * @param bForHTMLTable
	 *            true，证明这个action请求是前端的html表格发出的，它需要返回几个参数：count/msg/code
	 * @return
	 */
	protected Map<String, Object> getDefaultParamToReturn(boolean bForHTMLTable) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (bForHTMLTable) {
			params.put(KEY_HTMLTable_Parameter_TotalRecord, 0);
			params.put(KEY_HTMLTable_Parameter_code, "0");
		}
		params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");// 这个字段既可以作Html表格加载时的参数，也可以是一个Action返回给前端的错误信息
		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());

		return params;
	}

	protected String doRetrieveNToCheckUniqueFieldEx(Boolean useSYSTEM, BaseModel bm, String dbName, HttpSession session) {
		logger.info("检查字段是否唯一：fieldToCheckUnique=" + bm.getFieldToCheckUnique() + "\tuniqueField=" + bm.getUniqueField());

		Map<String, Object> params = new HashMap<String, Object>();

		DataSourceContextHolder.setDbName(dbName);
		getBO().retrieveNObject((useSYSTEM ? BaseBO.SYSTEM : getStaffFromSession(session).getID()), BaseBO.CASE_CheckUniqueField, bm);

		System.out.println("检查字段是否唯一的错误码=" + getBO().getLastErrorCode().toString());
		switch (getBO().getLastErrorCode()) {
		case EC_NoError:
			logger.info("字段唯一");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_Duplicated:
			logger.info("字段不唯一");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
			break;
		case EC_NoPermission:
			logger.error("权限不足");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		case EC_WrongFormatForInputField:
			if (BaseAction.ENV != EnumEnv.DEV) {
				logger.error("非法的请求");
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
			break;
		// return null;
		default:
			logger.error("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, getBO().getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	protected BaseBO getBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("当前使用的staff=" + staff);

		return staff;
	}

	protected Vip getVipFromSession(HttpSession session) {
		Vip vip = (Vip) session.getAttribute(EnumSession.SESSION_Vip.getName());
		logger.info("当前使用的vip=" + vip);

		return vip;
	}

	protected BxStaff getBxStaffFromSession(HttpSession session) {
		BxStaff bxStaff = (BxStaff) session.getAttribute(EnumSession.SESSION_BxStaff.getName());
		assert bxStaff != null;
		logger.info("当前使用的bxStaff=" + bxStaff);

		return bxStaff;
	}

	/** 获取私有DB的名称。公共DB的名称则已经定义为常量，不需要从会话中获取 */
	protected Company getCompanyFromSession(HttpSession session) {
		Company company = (Company) session.getAttribute(EnumSession.SESSION_Company.getName());
		// assert company != null;
		logger.info("当前使用的DB名称=" + company.getDbName() + "\t\t公司信息：" + company);

		return company;
	}

	protected void setCompanyInSession(HttpSession session, Company company) {
		assert company != null;
		session.setAttribute(EnumSession.SESSION_Company.getName(), company);
		logger.info("记在会话中的公司信息=" + company);
	}

	protected Company getCompanyFromCompanyCacheBySN(String companySN) {
		List<BaseModel> companyList = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(false, false);
		for (BaseModel bm : companyList) {
			Company company = (Company) bm;

			if (company.getSN().equals(companySN)) {
				return company;
			}
		}
		logger.info("从公共DB缓存中竟然找不到公司！companySN=" + companySN);

		return null;
	}

	public enum EnumEnv {
		DEV("DEV", 0), //
		SIT("SIT", 1), //
		UAT("UAT", 2), //
		PROD("PROD", 3);

		private String name;
		private int index;

		private EnumEnv(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumEnv e : EnumEnv.values()) {
				if (e.getIndex() == index) {
					return e.name;
				}
			}
			return null;
		}
	}

	/* 用户域。用于在Action层进行权限控制 */
	public enum EnumUserScope {
		STAFF("STAFF", 1), //
		BXSTAFF("BXSTAFF", 2), //
		VIP("VIP", 4), //
		POS("Pos", 8), //
		ANYONE("ANYONE", 16); // 任何人都能访问当前Action

		private String name;
		private int index;

		private EnumUserScope(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public static String getName(int index) {
			for (EnumEnv e : EnumEnv.values()) {
				if (e.getIndex() == index) {
					return e.name;
				}
			}
			return null;
		}
	}

	/**
	 * @param whoCanCallCurrentAction
	 *            必须提供的会话的组合
	 * @return true，当前会话中有目标会话,有权限访问当前action；false，当前会话中没有目标会话,无权限访问当前action
	 */
	protected boolean canCallCurrentAction(HttpSession session, int whoCanCallCurrentAction) {
		do {
			if ((whoCanCallCurrentAction & EnumUserScope.ANYONE.getIndex()) == EnumUserScope.ANYONE.getIndex()) {
				return true;
			}
			if ((whoCanCallCurrentAction & EnumUserScope.STAFF.getIndex()) == EnumUserScope.STAFF.getIndex()) {
				if (session.getAttribute(EnumSession.SESSION_Staff.getName()) != null) {
					return true;
				}
			}
			if ((whoCanCallCurrentAction & EnumUserScope.BXSTAFF.getIndex()) == EnumUserScope.BXSTAFF.getIndex()) {
				if (session.getAttribute(EnumSession.SESSION_BxStaff.getName()) != null) {
					return true;
				}
			}
			if ((whoCanCallCurrentAction & EnumUserScope.VIP.getIndex()) == EnumUserScope.VIP.getIndex()) {
				if (session.getAttribute(EnumSession.SESSION_Vip.getName()) != null) {
					return true;
				}
			}
			if ((whoCanCallCurrentAction & EnumUserScope.POS.getIndex()) == EnumUserScope.POS.getIndex()) {
				if (session.getAttribute(EnumSession.SESSION_POS.getName()) != null) {
					return true;
				}
			}
		} while (false);
		return false;
	}
}
