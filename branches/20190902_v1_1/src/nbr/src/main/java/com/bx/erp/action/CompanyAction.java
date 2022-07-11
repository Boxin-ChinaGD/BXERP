package com.bx.erp.action;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.BxStaffBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.PosSyncCacheBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.commodity.CommodityShopInfoBO;
import com.bx.erp.action.bo.warehousing.WarehouseBO;
import com.bx.erp.cache.BarcodesCache;
import com.bx.erp.cache.BarcodesSyncCache;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.InventorySheetCache;
import com.bx.erp.cache.POSCache;
import com.bx.erp.cache.PosSyncCache;
import com.bx.erp.cache.RetailTradePromotingCache;
import com.bx.erp.cache.ShopCache;
import com.bx.erp.cache.SmallSheetCache;
import com.bx.erp.cache.StaffBelongingCache;
import com.bx.erp.cache.StaffCache;
import com.bx.erp.cache.StaffPermissionCache;
import com.bx.erp.cache.VipCardCache;
import com.bx.erp.cache.commodity.BrandCache;
import com.bx.erp.cache.commodity.BrandSyncCache;
import com.bx.erp.cache.commodity.CategoryCache;
import com.bx.erp.cache.commodity.CategoryParentCache;
import com.bx.erp.cache.commodity.CategorySyncCache;
import com.bx.erp.cache.commodity.CommodityCache;
import com.bx.erp.cache.commodity.CommoditySyncCache;
import com.bx.erp.cache.config.ConfigCacheSizeCache;
import com.bx.erp.cache.config.ConfigGeneralCache;
import com.bx.erp.cache.purchasing.ProviderCache;
import com.bx.erp.cache.purchasing.ProviderDistrictCache;
import com.bx.erp.cache.purchasing.PurchasingOrderCache;
import com.bx.erp.cache.trade.promotion.PromotionCache;
import com.bx.erp.cache.trade.promotion.PromotionSyncCache;
import com.bx.erp.cache.vip.VipCache;
import com.bx.erp.cache.vip.VipCategoryCache;
import com.bx.erp.cache.warehousing.WarehouseCache;
import com.bx.erp.cache.warehousing.WarehousingCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.Company;
import com.bx.erp.model.Company.EnumCompanyCreationStatus;
import com.bx.erp.model.CompanyField;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.PosField;
import com.bx.erp.model.RealTimeSalesStatisticsToday;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Shop.EnumStatusShop;
import com.bx.erp.model.ShopField;
import com.bx.erp.model.Staff;
import com.bx.erp.model.StaffRole;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.config.BxConfigGeneral;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.JsonUtil;
import com.bx.erp.util.MD5Util;
import com.bx.erp.util.ProcessUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/company")
@Scope("prototype")
public class CompanyAction extends BaseAction {
	private Log logger = LogFactory.getLog(CompanyAction.class);

	public static final String ERROR_MSG_CompanyNotExist = "公司不存在";

	/** API证书类型 */
	private static final String APICertContentType = "application/x-pkcs12";

	/** 营业执照类型 */
	private static final String JPEGType = "image/jpeg";
	private static final String PNGType = "image/png";

	/** 临时的API名称 */
	private static final String APIName = "API" + System.currentTimeMillis() % 1000000 + ".p12";

	/** 临时的磁盘路径 */
	private static final String Disk = "D:/";

	@Resource
	protected CompanyBO companyBO;

	@Resource
	protected StaffBO staffBO;

	@Resource
	private BxStaffBO bxStaffBO;

	@Resource
	protected ShopBO shopBO;
	
	@Resource
	protected WarehouseBO warehouseBO;
	
	@Resource
	protected CommodityShopInfoBO commodityShopInfoBO;

	@Resource
	private PosBO posBO;

	@Resource
	private BrandCache brandCache;

	@Resource
	private BrandSyncCache brandSyncCache;

	@Resource
	private CategoryCache categoryCache;

	@Resource
	private CategoryParentCache categoryParentCache;

	@Resource
	private CategorySyncCache categorySyncCache;

	@Resource
	private CommodityCache commodityCache;

	@Resource
	private CommoditySyncCache commoditySyncCachel;

	@Resource
	private ConfigGeneralCache configGeneralCache;

	@Resource
	private ConfigCacheSizeCache configCacheSizeCache;

	@Resource
	private ProviderCache providerCache;

	@Resource
	private ProviderDistrictCache providerDistrictCache;

	@Resource
	private PurchasingOrderCache purchasingOrderCache;

	@Resource
	private PromotionCache promotionCache;

	@Resource
	private PromotionSyncCache promotionSyncCache;

	@Resource
	private VipCache vipCache;

	@Resource
	private VipCategoryCache vipCategoryCache;

	@Resource
	private WarehouseCache warehouseCache;

	@Resource
	private WarehousingCache warehousingCache;

	@Resource
	private BarcodesCache barcodesCache;

	@Resource
	private BarcodesSyncCache barcodesSyncCache;

	// @Resource
	// private InventoryCommodityCache inventoryCommodityCache;

	@Resource
	private InventorySheetCache inventorySheetCache;

	@Resource
	private POSCache posCache;

	@Resource
	private PosSyncCache posSyncCache;

	@Resource
	private RetailTradePromotingCache retailTradePromotingCache;

	@Resource
	private SmallSheetCache smallSheetCache;

	@Resource
	private StaffCache staffCache;

	@Resource
	private StaffPermissionCache staffPermissionCache;

	@Resource
	private StaffBelongingCache staffBelongingCache;

	@Resource
	private VipCardCache vipCardCache;

	@Resource
	private ShopCache shopCache;
	
	@Resource
	protected com.bx.erp.cache.PosSyncCache psc;

	@SuppressWarnings("unchecked")
	@RequestMapping(produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
		// ... 由于是业务经理使用该功能，临时处理方案为设成最高权限
		List<BxStaff> bxStaffList = (List<BxStaff>) bxStaffBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shop);

		// ShopDistrict shopDistrict=new ShopDistrict();
		// shopDistrict.setPageIndex(PAGE_StartIndex);
		// shopDistrict.setPageSize(PAGE_SIZE_Infinite);
		// String dbName = getDBNameFromSession(session);
		// DataSourceContextHolder.setDbName(dbName);
		// //... 由于是业务经理使用该功能，临时处理方案为设成最高权限
		// List<?> shopDistrictList = shopDistrictBO.retrieveNObject(BaseBO.SYSTEM,
		// BaseBO.INVALID_CASE_ID, shopDistrict);
		// logger.info("retrieveN shopDistrict error code=" +
		// shopDistrictBO.getLastErrorCode());

		// switch (shopDistrictBO.getLastErrorCode()) {
		// case EC_NoError:
		// logger.info("查询成功,bxStaffList=" + bxStaffList.toString());
		// mm.put("shopDistrictList", shopDistrictList);
		// mm.put("shopDistrict", shopDistrict);
		mm.put("bxStaffList", bxStaffList);
		mm.put("shop", new ShopField());
		mm.put("company", new CompanyField());
		mm.put("pos", new PosField());
		// break;
		// default:
		// logger.info("其他错误");
		// mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
		// break;
		// }
		return new ModelAndView(COMPANY_Index, "", "");
	}

	/*
	 * URL: http://localhost:8080/nbr/companyAction/create.bx
	 * 创建公司时需要传入int1和int2,int1是用来创建公司数据库暂未完成，设置暂不扫描夜间任务，
	 */
	@RequestMapping(value = "/createEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createEx(@ModelAttribute("SpringWeb") Company company, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("创建一个公司,company=" + company.toString());
		//
		if (session.getAttribute(EnumSession.SESSION_BxStaff.getName()) == null) {
			logger.debug("非OP帐号不能创建公司！");
			return null;
		}
		//
		Lock lock = new ReentrantLock();

		lock.lock();

		Map<String, Object> params = getDefaultParamToReturn(true);
		try {
			setLogoAndBusinessLicensePictureAttribute(company, session);

			DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
			Company companyCreated = (Company) companyBO.createObject(getBxStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, company);
			logger.info("Create company error code = " + companyBO.getLastErrorCode() + "\r\nLicensePicture=" + company.getBusinessLicensePicture() + "\r\nLicenseSN=" + company.getBusinessLicenseSN());

			switch (companyBO.getLastErrorCode()) {
			case EC_NoError:
				logger.info("创建公司成功，创建的对象为：" + (companyCreated == null ? "NULL" : companyCreated));
				if (createPrivateDB(companyCreated.getDbName())) {
					BxStaff bxStaff = (BxStaff) session.getAttribute(BaseAction.EnumSession.SESSION_BxStaff.getName());

					companyCreated.setIncumbent(EnumCompanyCreationStatus.ECCS_NotIncumbent.getIndex());// 创建公司数据库暂未完成，设置暂不扫描夜间任务
					CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(companyCreated, BaseAction.DBName_Public, bxStaff.getID());

					logger.info("创建私有DB：" + companyCreated.getDbName() + " 成功！！加载私有DB缓存");
					loadCacheFromPrivateDB(companyCreated);
					
					Shop InventedShopCreated = createDefaultShop(bxStaff, companyCreated, BaseAction.ShopeName_Invented);
					if (InventedShopCreated == null) {
						params.put(BaseAction.JSON_ERROR_KEY, shopBO.getLastErrorCode());
						params.put(KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
						logger.error("创建公司：" + companyCreated.getName() + "时，创建虚拟总部失败！错误信息:" + shopBO.getLastErrorMessage() + "！！！ 请删除DB：" + companyCreated.getDbName());

						lock.unlock();

						return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
					}
					
					Shop defaultShopCreated = createDefaultShop(bxStaff, companyCreated, BaseAction.ShopeName_Default);
					if (defaultShopCreated == null) {
						params.put(BaseAction.JSON_ERROR_KEY, shopBO.getLastErrorCode());
						params.put(KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
						logger.error("创建公司：" + companyCreated.getName() + "时，创建默认门店失败！错误信息:" + shopBO.getLastErrorMessage() + "！！！ 请删除DB：" + companyCreated.getDbName());

						lock.unlock();

						return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
					}
					params.put(BaseAction.ShopeID_Default, defaultShopCreated.getID());// POS机需要用到create Company之后返回ShopID

					CacheManager.getCache(companyCreated.getDbName(), EnumCacheType.ECT_Shop).write1(defaultShopCreated, companyCreated.getDbName(), bxStaff.getID());

					Staff preSaleStaff = createPreSaleAccount(bxStaff, InventedShopCreated, companyCreated);
					if (preSaleStaff == null) {
						params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode());
						params.put(KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
						logger.error("创建公司：" + companyCreated.getName() + "时，创建售前账号失败！错误信息:" + staffBO.getLastErrorMessage() + "！！！ 请删除DB：" + companyCreated.getDbName());

						lock.unlock();

						return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
					}
					CacheManager.getCache(companyCreated.getDbName(), EnumCacheType.ECT_Staff).write1(preSaleStaff, companyCreated.getDbName(), bxStaff.getID());

					Staff bossStaff = createBossAccount(bxStaff, InventedShopCreated, companyCreated);
					if (bossStaff == null) {
						params.put(BaseAction.JSON_ERROR_KEY, staffBO.getLastErrorCode());
						params.put(KEY_HTMLTable_Parameter_msg, staffBO.getLastErrorMessage());
						logger.error("创建公司：" + companyCreated.getName() + "时，创建老板账号失败！错误信息:" + staffBO.getLastErrorMessage() + "！！！ 请删除DB：" + companyCreated.getDbName());

						lock.unlock();

						return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
					}
					CacheManager.getCache(companyCreated.getDbName(), EnumCacheType.ECT_Staff).write1(bossStaff, companyCreated.getDbName(), bxStaff.getID());

					// 加载老板账号的权限到缓存...
					// staffPermissionCache.load(companyCreated.getDbName());
					// 刷新权限 TODO
					loadCacheFromPrivateDB(companyCreated);

					Company com = (Company) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).read1(companyCreated.getID(), bxStaff.getID(), new ErrorInfo(), companyCreated.getDbName());
					assert com != null;
					com.setIncumbent(EnumCompanyCreationStatus.ECCS_Incumbent.getIndex());// 创建公司数据库完成，设置扫描夜间任务
					// 设置扫描夜间任务状态后重新更新缓存
					CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(com, BaseAction.DBName_Public, bxStaff.getID());

					// 将敏感信息设置为空串
					Company companyCreated1 = (Company) companyCreated.clone();
					companyCreated1.setKey("");
					companyCreated1.setDbUserPassword("");
					companyCreated1.setDbUserName("");
					//
					params.put(KEY_Object, companyCreated1);
					params.put(KEY_HTMLTable_Parameter_msg, "");
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
				} else {
					logger.error("创建公司数据库失败！！！");
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "创建公司数据库失败");
					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				}
				break;
			case EC_Duplicated:
				logger.info("创建失败，错误码为：" + EnumErrorCode.EC_Duplicated);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage().toString());
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
				break;
			case EC_NoPermission:
				logger.error("没有权限");
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
				break;
			case EC_WrongFormatForInputField:
				logger.error("格式错误！");
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
				break;
			default:
				logger.error("其他错误！");
				params.put(KEY_HTMLTable_Parameter_msg, "创建公司异常，请重试");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			}
			logger.info("返回的数据=" + params);

		} catch (Exception e) {
			logger.error("创建公司异常：" + e.getMessage());
			params.put(KEY_HTMLTable_Parameter_msg, "创建公司异常，请重试");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());

			lock.unlock();

			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}

		lock.unlock();

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	private void loadCacheFromPrivateDB(final Company com) {
		logger.info("正在加载公司的缓存：" + com);

		int failCount = 0;
		final int RETRY_COUNT = 200; // 加载私有DB的缓存可能出现异常，需要重试。这里定义重试的次数
		do {
			try {
				Thread.sleep(5 * 1000); // 由于无法测试mysql中私有DB的就绪状态，先sleep一下，等待其就绪
				Staff staff = new Staff();
				staff.setID(1); // 即使查询不到数据也无所谓，不影响目的
				DataSourceContextHolder.setDbName(com.getDbName());
				staffBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, staff);
				if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.info("私有DB未就绪，重试" + failCount + "次...信息：" + staffBO.printErrorInfo());
					failCount++;
					if (failCount > RETRY_COUNT) {
						break;
					}
					continue; // 证明DB未就绪
				} else {
					break;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} while (true);

		try {
			// 每一个公司的缓存，都是私有的，不能和其它公司共享，所以下面的代码，每一块缓存内部都会clone一块然后绑定到com中，不会干扰其它com
			// 如果以下代码有变，则CompanyCache.load()也需要跟着变
			configGeneralCache.load(com.getDbName());// 配置需要首先加载
			configCacheSizeCache.load(com.getDbName());// 配置需要首先加载
			configCacheSizeCache.checkIfLoadOK(com.getDbName());
			brandCache.load(com.getDbName());
			brandSyncCache.load(com.getDbName());
			categoryCache.load(com.getDbName());
			categoryParentCache.load(com.getDbName());
			categorySyncCache.load(com.getDbName());
			commodityCache.load(com.getDbName());
			commoditySyncCachel.load(com.getDbName());
			providerCache.load(com.getDbName());
			providerDistrictCache.load(com.getDbName());
			purchasingOrderCache.load(com.getDbName());
			promotionCache.load(com.getDbName());
			promotionSyncCache.load(com.getDbName());
			vipCache.load(com.getDbName());
			vipCategoryCache.load(com.getDbName());
			warehouseCache.load(com.getDbName());
			warehousingCache.load(com.getDbName());
			barcodesCache.load(com.getDbName());
			barcodesSyncCache.load(com.getDbName());
			inventorySheetCache.load(com.getDbName());
			posCache.load(com.getDbName());
			posSyncCache.load(com.getDbName());
			retailTradePromotingCache.load(com.getDbName());
			shopCache.load(com.getDbName());
			smallSheetCache.load(com.getDbName());
			staffCache.load(com.getDbName());
			staffPermissionCache.load(com.getDbName());
			staffBelongingCache.load(com.getDbName());
			vipCardCache.load(com.getDbName());
			// 在这里添加缓存后需要在CompanyCache.load()也添加
			//
			// 记录每个公司当天的销售总额和零售笔数
			RealTimeSalesStatisticsToday realTimeSST = new RealTimeSalesStatisticsToday(0.000000d, 0, new Date());
			RetailTradeAction.todaysSaleCache.put(com.getDbName(), realTimeSST);
			logger.info("加载私有DB的缓存成功");
		} catch (Exception ex) {
			logger.error("加载私有DB的缓存失败，请重启Tomcat！信息：" + ex.getMessage());
		}
	}

	private boolean createPrivateDB(String dbName) {
		logger.info("正在创建公司的私有DB：" + dbName);
		boolean isCreated = false;
		try {
			final String sharedMemoryPath = "D:\\QueenService\\ProcessMessage\\";// 进程间通信文件所在目录

			@SuppressWarnings("resource")
			FileChannel fcClient = new RandomAccessFile(sharedMemoryPath + "client.mm", "rw").getChannel();
			@SuppressWarnings("resource")
			FileChannel fcServer = new RandomAccessFile(sharedMemoryPath + "server.mm", "rw").getChannel();

			ProcessUtil.writeSharedMemory(fcClient, dbName);

			int timeOut = 20;
			String processMsg = "";
			while (timeOut-- > 0) {
				processMsg = ProcessUtil.readJSON(ProcessUtil.readSharedMemory(fcServer), BaseAction.JSON_PROCESS_KEY);
				if ("SUCCESS".equals(processMsg)) {// ... hardcode
					logger.info("数据库创建完成!!!");
					isCreated = true;
					break;
				} else if ("FAIL".equals(processMsg)) {
					logger.info("数据库创建失败，请查看QueenService日志查找原因!!!");
					break; // ...是否使用事务？
				}

				Thread.sleep(2000 * 2);// 设置创建数据库超时时间...
			}

			if ("".equals(processMsg) || processMsg == null) {
				logger.info("数据库创建超时...");
				// ...是否需要将上面创建的门店删除？
			}
		} catch (Exception e) {
			logger.info("创建公司数据库异常：" + e.getMessage());
		}
		return isCreated;
	}

	private Shop createDefaultShop(BxStaff bxStaff, final Company company, String name) {
		Shop shop = new Shop();
		shop.setName(name);
		shop.setBxStaffID(bxStaff.getID());
		shop.setCompanyID(company.getID());
		shop.setDistrictID(BaseAction.DistrictID_Default);
		shop.setStatus(EnumStatusShop.ESS_Online.getIndex());
		shop.setKey(company.getKey());
		shop.setLatitude(0.0f);// ... hardcode
		shop.setLongitude(0.0f);
		shop.setAddress(BaseAction.ShopeAddress_Default);

		DataSourceContextHolder.setDbName(company.getDbName());
		Shop shopCreated = (Shop) shopBO.createObject(bxStaff.getID(), BaseBO.CASE_BxStaffCreateShopAndPreSaleStaffAndBossAccount, shop);
		if (shopBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			return null;
		}

		logger.info("创建" + name +"成功！");
		return shopCreated;
	}
	
	private Staff createPreSaleAccount(BxStaff bxStaff, final Shop shop, final Company company) {
		Staff staff = new Staff();
		staff.setName(BaseAction.NAME_PreSale);
		staff.setPhone(BaseAction.ACCOUNT_Phone_PreSale);
		staff.setSalt(BaseAction.PASSWORD_PreSale);
		staff.setShopID(shop.getID());
		staff.setDepartmentID(BaseAction.DepartmentID_Default);
		staff.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staff.setRoleID(Role.EnumTypeRole.ETR_PreSale.getIndex()); // ...角色ID
		staff.setIsFirstTimeLogin(EnumBoolean.EB_NO.getIndex());// 1代表未登录状态
		DataSourceContextHolder.setDbName(company.getDbName());
		Staff preSaleStaff = (Staff) staffBO.createObject(bxStaff.getID(), BaseBO.CASE_BxStaffCreateShopAndPreSaleStaffAndBossAccount, staff);
		if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			return null;
		}
		logger.info("创建博昕售前账号成功！");
		return preSaleStaff;
	}

	private Staff createBossAccount(BxStaff bxStaff, final Shop shop, final Company company) {
		Staff boss = new Staff();
		boss.setName(company.getBossName());
		boss.setPhone(company.getBossPhone());
		boss.setDepartmentID(BaseAction.DepartmentID_Default);
		boss.setWeChat(company.getBossWechat());
		boss.setSalt(MD5Util.MD5(company.getBossPassword() + BaseAction.SHADOW));
		boss.setShopID(shop.getID());
		boss.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		boss.setRoleID(Role.EnumTypeRole.ETR_Boss.getIndex()); // ...角色ID
		boss.setIsFirstTimeLogin(EnumBoolean.EB_Yes.getIndex());// 1代表未登录状态
		DataSourceContextHolder.setDbName(company.getDbName());
		Staff staff_boss = (Staff) staffBO.createObject(bxStaff.getID(), BaseBO.CASE_BxStaffCreateShopAndPreSaleStaffAndBossAccount, boss);
		if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			return null;
		}
		logger.info("创建博昕门店老板账号成功！");
		return staff_boss;
	}

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/companyAction/retrieve1Ex.bx
	 */

	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Company company, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);

		logger.info("查询一个公司,company=" + company.toString());

		Company c = (Company) companyBO.retrieve1Object(getBxStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, company);

		logger.info("retrieve1 company error code=" + companyBO.getLastErrorCode());
		Map<String, Object> params = getDefaultParamToReturn(true);
		switch (companyBO.getLastErrorCode()) {
		case EC_NoError:
			logger.info("查询成功！c=" + (c == null ? "" : c.toString()));

			// 将敏感信息设置为空串
			if (c != null) {
				c.setKey("");
				c.setDbUserPassword("");
				c.setDbUserName("");
			}
			//
			params.put(KEY_Object, c);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			break;
		case EC_NoPermission:
			logger.info("没有权限");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/companyAction/retrieveNEx.bx
	 */

	// @RequestMapping(value = "/retrieveNEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String retrieveNEx(@ModelAttribute("SpringWeb") Company company,
	// ModelMap mm, HttpSession session) {
	// DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
	//
	// logger.info("查询多个公司,company=" + company.toString());
	//
	// List<?> listBm =
	// companyBO.retrieveNObject(getBxStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, company);
	// logger.info("retrieve brand error code=" + companyBO.getLastErrorCode());
	// Map<String, Object> params = getDefaultParamToReturn(true);
	// switch (companyBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("查询成功！listBm=" + listBm.toString());
	//
	// params.put(KEY_ObjectList, listBm);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// case EC_NoPermission:
	// logger.info("没有权限");
	//
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_NoPermission.toString());
	// break;
	// default:
	// logger.info("其他错误");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: URL: GET:
	 * http://localhost:8080/nbr/companyAction/delete.bx int1 为shopID
	 */

	// @RequestMapping(value = "/deleteEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteEx(@ModelAttribute("SpringWeb") Company company, ModelMap
	// mm, HttpSession session) {
	// logger.info("删除一个公司,company=" + company.toString());
	//
	// // 从缓存中读取公司的信息
	// ErrorInfo ecOut = new ErrorInfo();
	// Company c = (Company) CacheManager.getCache(BaseAction.DBName_Public,
	// EnumCacheType.ECT_Company).read1(company.getID(), BaseBO.SYSTEM, ecOut,
	// BaseAction.DBName_Public);
	// Map<String, Object> params = getDefaultParamToReturn(true);
	// if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || c == null) {
	// return null;
	// }
	// // 删除公司
	// DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
	// companyBO.deleteObject(getBxStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, company);
	// logger.info("Delete company error code=" + companyBO.getLastErrorCode());
	//
	// switch (companyBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("删除公司成功！");
	// // ...在一个公司有多个门店的情况下,删除单个门店时不应该删除该公司的缓存,以免下次删除该公司其他门店
	// CacheManager.getCache(BaseAction.DBName_Public,
	// EnumCacheType.ECT_Company).delete1(c);
	//
	// // 删除shop信息
	// Shop shop = new Shop();
	// shop.setID(company.getInt1());
	// shop.setInt1(company.getID());
	// DataSourceContextHolder.setDbName(c.getDbName());
	// List<List<BaseModel>> shList = shopBO.deleteObjectEx(BaseBO.SYSTEM,
	// BaseBO.INVALID_CASE_ID, shop);
	// if (shopBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// logger.error("删除公司" + c.getDbName() + "下的门店" + company.getInt1() +
	// "失败！OP要将此门店状态设置为删除状态");
	//
	// params.put(BaseAction.JSON_ERROR_KEY, shopBO.getLastErrorCode());
	// params.put(BaseAction.KEY_HTMLTable_Parameter_msg,
	// shopBO.getLastErrorMessage());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }
	// logger.info("删除公司" + c.getDbName() + "下的门店成功！" +
	// (CollectionUtils.isEmpty(shList) ? "删除所有门店的信息成功!" : "删除一个门店的信息成功" + shList));
	//
	// CacheManager.getCache(BaseAction.DBName_Public,
	// EnumCacheType.ECT_Company).delete1(company);
	//
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// case EC_NoPermission:
	// logger.info("没有权限");
	//
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_NoPermission.toString());
	// break;
	// default:
	// logger.info("未知错误");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	private void setLogoAndBusinessLicensePictureAttribute(Company company, HttpSession session) throws Exception {
		String BusinessLicensePictureType = uploadFile(company, session, BaseAction.BusinessLicensePictureDir, EnumSession.SESSION_CompanyBusinessLicensePictureDestination.getName(),
				EnumSession.SESSION_CompanyBusinessLicensePictureFILE.getName());
		if (BusinessLicensePictureType != null) {
			String businessLicensePictureDir = BaseAction.BusinessLicensePictureDir.replaceAll("D:/nbr/pic", "/p");
			company.setBusinessLicensePicture(businessLicensePictureDir + company.getDbName() + BusinessLicensePictureType);
		} else {
			company.setBusinessLicensePicture("");
		}
		//
		String logoType = uploadFile(company, session, BaseAction.CompanyLogoDir, EnumSession.SESSION_CompanyLogoDestination.getName(), EnumSession.SESSION_CompanyLogoFILE.getName());
		if (logoType != null) {
			String logoDir = BaseAction.CompanyLogoDir.replaceAll("D:/nbr/pic", "/p");
			company.setLogo(logoDir + company.getDbName() + logoType);
		} else {
			company.setLogo("");
		}
	}

	@RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateEx(@ModelAttribute("SpringWeb") Company company, ModelMap mm, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		//
		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);

		logger.info("更新一个公司,company=" + company);

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			setLogoAndBusinessLicensePictureAttribute(company, session);
			//
			if ("".equals(company.getBusinessLicensePicture()) || "".equals(company.getLogo())) {
				ErrorInfo ecOut = new ErrorInfo();
				Company c = (Company) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).read1(company.getID(), BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);

				if ("".equals(company.getBusinessLicensePicture())) {
					company.setBusinessLicensePicture(c.getBusinessLicensePicture());
				}
				if ("".equals(company.getLogo())) {
					company.setLogo(c.getLogo());
				}
			}

			Company companyUpdate = (Company) companyBO.updateObject(getBxStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, company);
			if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("修改公司失败，错误码=" + companyBO.getLastErrorCode() + ",错误信息 =" + companyBO.getLastErrorMessage());

				params.put(BaseAction.JSON_ERROR_KEY, companyBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
				break;
			}

			logger.info("更新成功！companyUpdate=" + companyUpdate);
			BxStaff bxStaff = (BxStaff) session.getAttribute(BaseAction.EnumSession.SESSION_BxStaff.getName());
			CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(companyUpdate, BaseAction.DBName_Public, bxStaff.getID()); // TODO 这里使用的是bxstaff的ID。

			// 将敏感信息设置为空串
			companyUpdate = (Company) companyUpdate.clone();
			companyUpdate.setKey("");
			companyUpdate.setDbUserPassword("");
			companyUpdate.setDbUserName("");
			//
			params.put(KEY_Object, companyUpdate);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/updateSubmchidEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateSubmchidEx(@ModelAttribute("SpringWeb") Company company, ModelMap mm, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		//
		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);

		logger.info("更新一个公司的子商户号,company=" + company + "子商户号=" + company.getSubmchid());

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			Company companyUpdate = (Company) companyBO.updateObject(getBxStaffFromSession(session).getID(), BaseBO.CASE_Company_UpdateSubmchid, company);
			if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("修改公司子商户号失败，" + companyBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, companyBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
				break;
			}
			logger.info("更新成功！companyUpdate=" + companyUpdate);
			BxStaff bxStaff = (BxStaff) session.getAttribute(BaseAction.EnumSession.SESSION_BxStaff.getName());
			CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(companyUpdate, BaseAction.DBName_Public, bxStaff.getID()); // TODO 这里使用的是bxstaff的ID。
			// 将敏感信息设置为空串
			companyUpdate = (Company) companyUpdate.clone();
			companyUpdate.setKey("");
			companyUpdate.setDbUserPassword("");
			companyUpdate.setDbUserName("");
			//
			params.put(KEY_Object, companyUpdate);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@RequestMapping(value = "/updateVipSystemTipEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateVipSystemTipEx(@ModelAttribute("SpringWeb") Company company, ModelMap mm, HttpSession session) throws Exception {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		DataSourceContextHolder.setDbName(BaseAction.DBName_Public);

		logger.info("更新一个公司是否需要会员提示,company=" + company);

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			Company companyUpdate = (Company) companyBO.updateObject(getStaffFromSession(session).getID(), BaseBO.CASE_Company_updateVipSystemTip, company);
			if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("修改公司是否需要会员提示失败，" + companyBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, companyBO.getLastErrorCode());
				params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
				break;
			}
			logger.info("更新成功！companyUpdate=" + companyUpdate);
			Staff staff = (Staff) session.getAttribute(BaseAction.EnumSession.SESSION_Staff.getName());
			CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).write1(companyUpdate, BaseAction.DBName_Public, staff.getID());

			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	private String uploadFile(Company company, HttpSession session, String dir, String destination, String fileName) throws Exception {
		// 从session中需要上传的文件和上传的路径
		File dest = (File) session.getAttribute(destination);
		MultipartFile file = (MultipartFile) session.getAttribute(fileName);

		// 创建时没有传入
		if (dest == null && file == null) {
			return null;
		}

		// 现在传入的文件是否已经存在，如果有删除该文件
		do {
			File file5 = new File(dir + company.getDbName() + ".jpg");
			if (file5.exists()) {
				file5.delete();
				break;
			}
			file5 = new File(dir + company.getDbName() + ".jpeg");
			if (file5.exists()) {
				file5.delete();
				break;
			}
			file5 = new File(dir + company.getDbName() + ".png");
			if (file5.exists()) {
				file5.delete();
				break;
			}
		} while (false);

		// 上传文件
		if (dest != null && file != null) {
			file.transferTo(dest);
			logger.info("上传文件成功：" + dest);
		}

		// 截取路径用来判断类型
		String type = ".jpg";
		File dest1 = (File) new File(dir);
		if (dest1.list().length > 0) {
			String sFilePath = dest1.list()[0];
			int index = sFilePath.lastIndexOf(".");
			if (index != -1) {
				type = sFilePath.substring(index);
				System.out.println("解析出的图片类型=" + type);
			} else {
				logger.error("图片无后缀！" + sFilePath);
			}
		}
		//
		File destination1 = new File(dir + "/" + company.getDbName() + type);
		dest.renameTo(destination1);

		// 清空session以免影响下一次的功能
		session.removeAttribute(destination);
		session.removeAttribute(fileName);
		return type;
	}

	@RequestMapping(value = "/uploadAPICertEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String uploadAPICertEx(@ModelAttribute("SpringWeb") Company company, ModelMap model, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("公司上传证书，company=" + company);
		//
		if (session.getAttribute(EnumSession.SESSION_BxStaff.getName()) == null) {
			logger.debug("非OP帐号不能上传公司证书！");
			return null;
		}
		//
		Map<String, Object> params = new HashMap<>();
		do {
			if (!checkDisk(params)) {
				break;
			}

			if (!file.isEmpty()) {
				if (!file.getContentType().contains(APICertContentType)) {
					logger.error("公司的API证书类型不符合要求,当前的证书类型为:" + file.getContentType());

					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
					params.put(KEY_HTMLTable_Parameter_msg, "公司的API证书类型必须为.p12格式");
					break;
				}

				Long size = file.getSize();
				ErrorInfo ecOut = new ErrorInfo();
				BxConfigGeneral companyAPICertVolumeMax = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.CompanyAPICertVolumeMax, getBxStaffFromSession(session).getID(),
						ecOut, BaseAction.DBName_Public);
				if (size <= 0 || size > Long.parseLong(companyAPICertVolumeMax.getValue())) {
					logger.error("公司API证书大小不符合要求，size=" + size);

					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
					params.put(KEY_HTMLTable_Parameter_msg, "公司API证书大小不符合要求");
					break;
				}

				BxConfigGeneral companyAPICertDir = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.CompanyAPICertDir, getBxStaffFromSession(session).getID(), ecOut,
						BaseAction.DBName_Public);
				if (!BaseAction.CompanyAPICertDir.equals(companyAPICertDir.getValue())) {
					logger.error("不存在此目录，路径为:" + BaseAction.CompanyAPICertDir);

					params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
					params.put(KEY_HTMLTable_Parameter_msg, "服务器错误，文件上传失败");
					break;
				}

				// 判断文件夹是否存在，如果不存在，创建相应的文件夹
				File companyAPICertDIR = new File(BaseAction.CompanyAPICertDir);
				if (!companyAPICertDIR.exists()) {
					companyAPICertDIR.mkdirs();
				}

				// 检查文件夹是否存在文件，如果有删除所有文件
				File file1 = new File(BaseAction.CompanyAPICertDir);
				String[] list = file1.list();
				if (list.length > 0) {
					for (String string : list) {
						File file2 = new File(BaseAction.CompanyAPICertDir + "/" + string);
						file2.delete();
					}
				}

				// 找到对应的文件夹上传相应的api证书
				File companyAPIDestination = new File(BaseAction.CompanyAPICertDir + "/" + APIName);
				file.transferTo(companyAPIDestination);
				logger.info("上传API证书成功：" + companyAPIDestination);

				File companyAPIDestination1 = new File(BaseAction.CompanyAPICertDir + "/" + company.getDbName() + ".p12");
				companyAPIDestination.renameTo(companyAPIDestination1);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			}
		} while (false);
		logger.info("返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * http://localhost:8080/nbr/company/retrieveNToCheckUniqueFieldEx.bx
	 */
	@RequestMapping(value = "/retrieveNToCheckUniqueFieldEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNToCheckUniqueFieldEx(@ModelAttribute("SpringWeb") Company company, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		String dbName = getCompanyFromSession(session).getDbName();
		// 选传参数：ID，必传参数：string1和int1
		// 如果是要进行修改操作,则要传对应的ID参数，不然即使不做修改，也会报已存在该字段的错误信息
		// string1存储的是要检查的字段值，int1如下描述
		// int1=1，代表要检查公司名称
		// int1=2，代表要检查营业执照
		// int1=5，代表要检查DB名称

		return doRetrieveNToCheckUniqueFieldEx(true, company, dbName, session);
	}

	@Override
	protected BaseBO getBO() {
		return companyBO;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteLicensePictureEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	public void deleteLicensePictureEx(HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return;
		}

		logger.info("清空营业执照的session");

		session.removeAttribute(EnumSession.SESSION_CompanyBusinessLicensePictureDestination.getName());
		session.removeAttribute(EnumSession.SESSION_CompanyBusinessLicensePictureFILE.getName());
	}

	private boolean uploadImg(Map<String, Object> params, HttpSession session, MultipartFile file, int bxConfigGeneralVolumeMaxID, String dir, int bxConfigGeneralDirID, String fileFirstName, String DestinationName, String fileName) {
		session.removeAttribute(DestinationName);
		session.removeAttribute(fileName);

		if (!checkDisk(params)) {
			return false;
		}

		if (!file.isEmpty()) {
			if (!file.getContentType().equals(JPEGType) && !file.getContentType().equals(PNGType)) {
				logger.info("图片类型不符合,当前的图片类型为:" + file.getContentType());

				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "图片类型必须是.jpg/.jpeg/.png格式");
				return false;
			}

			Long size = file.getSize();
			ErrorInfo ecOut = new ErrorInfo();
			BxConfigGeneral volumeMax = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral)//
					.read1(bxConfigGeneralVolumeMaxID, getBxStaffFromSession(session).getID(), ecOut, BaseAction.DBName_Public);
			if (size <= 0 || size > Long.parseLong(volumeMax.getValue())) {
				logger.info("图片大小不符合要求，size=" + size);

				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "图片大小不能超过100KB");
				return false;
			}

			// 判断文件夹是否存在，如果不存在，创建相应的文件夹
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			BxConfigGeneral bxConfigGeneralDir = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral)//
					.read1(bxConfigGeneralDirID, getBxStaffFromSession(session).getID(), ecOut, BaseAction.DBName_Public);
			if (!dir.equals(bxConfigGeneralDir.getValue())) {
				logger.error("不存在此目录，path=" + dir);

				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				params.put(KEY_HTMLTable_Parameter_msg, "不存在此目录，path=" + dir);
				return false;
			}

			// 判断是jpg还是png类型，上传名称
			String name = "";
			if (file.getContentType().contains(JPEGType)) {
				name = fileFirstName + System.currentTimeMillis() % 1000000 + ".jpg";
			} else {
				name = fileFirstName + System.currentTimeMillis() % 1000000 + ".png";
			}

			// 找到对应的文件夹放到session里面
			File fileDestination = new File(dir + name);

			session.setAttribute(DestinationName, fileDestination);
			session.setAttribute(fileName, file);
		}
		return true;
	}

	@RequestMapping(value = "/uploadBusinessLicensePictureEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String uploadBusinessLicensePictureEx(@ModelAttribute("SpringWeb") Company company, ModelMap model, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("公司上传营业执照，company=" + company);
		//
		Map<String, Object> params = new HashMap<>();
		String fileFirstName = "BLP";

		if (uploadImg(params, session, file, BaseCache.CompanyBusinessLicensePictureVolumeMax, BaseAction.BusinessLicensePictureDir, BaseCache.CompanyBusinessLicensePictureDir, //
				fileFirstName, EnumSession.SESSION_CompanyBusinessLicensePictureDestination.getName(), EnumSession.SESSION_CompanyBusinessLicensePictureFILE.getName())) //
		{
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.KEY_Object, company);
		}
		// do {
		// if (!checkDisk(params)) {
		// break;
		// }
		//
		// if (!file.isEmpty()) {
		// if (!file.getContentType().equals(JPEGType) &&
		// !file.getContentType().equals(PNGType)) {
		// logger.info("公司的营业执照不符合,当前的图片类型为:" + file.getContentType());
		//
		// params.put(BaseAction.JSON_ERROR_KEY,
		// EnumErrorCode.EC_OtherError.toString());
		// params.put(KEY_HTMLTable_Parameter_msg, "公司的营业执照类型必须是.jpg/.jpeg/.png格式");
		// break;
		// }
		//
		// Long size = file.getSize();
		// ErrorInfo ecOut = new ErrorInfo();
		// BxConfigGeneral businessLicensePictureVolumeMax = (BxConfigGeneral)
		// CacheManager.getCache(BaseAction.DBName_Public,
		// EnumCacheType.ECT_BXConfigGeneral)//
		// .read1(BaseCache.CompanyBusinessLicensePictureVolumeMax,
		// getBxStaffFromSession(session).getID(), ecOut, BaseAction.DBName_Public);
		// if (size <= 0 || size >
		// Long.parseLong(businessLicensePictureVolumeMax.getValue())) {
		// logger.info("公司营业执照大小不符合要求，size=" + size);
		//
		// params.put(BaseAction.JSON_ERROR_KEY,
		// EnumErrorCode.EC_OtherError.toString());
		// params.put(KEY_HTMLTable_Parameter_msg, "公司营业执照大小大小不能超过100KB");
		// break;
		// }
		//
		// // 判断文件夹是否存在，如果不存在，创建相应的文件夹
		// File businessLicensePictureDirDIR = new
		// File(BaseAction.BusinessLicensePictureDir);
		// if (!businessLicensePictureDirDIR.exists()) {
		// businessLicensePictureDirDIR.mkdirs();
		// }
		//
		// BxConfigGeneral businessLicensePictureDir = (BxConfigGeneral)
		// CacheManager.getCache(BaseAction.DBName_Public,
		// EnumCacheType.ECT_BXConfigGeneral)//
		// .read1(BaseCache.CompanyBusinessLicensePictureDir,
		// getBxStaffFromSession(session).getID(), ecOut, BaseAction.DBName_Public);
		// if
		// (!BaseAction.BusinessLicensePictureDir.equals(businessLicensePictureDir.getValue()))
		// {
		// logger.error("不存在此目录，path=" + BaseAction.BusinessLicensePictureDir);
		//
		// params.put(BaseAction.JSON_ERROR_KEY,
		// EnumErrorCode.EC_OtherError.toString());
		// params.put(KEY_HTMLTable_Parameter_msg, "不存在此目录，path=" +
		// BaseAction.BusinessLicensePictureDir);
		// break;
		// }
		//
		// // 判断是jpg还是png类型，上传名称
		// String BusinessLicensePictureName = "";
		// if (file.getContentType().contains(JPEGType)) {
		// BusinessLicensePictureName = "BLP" + System.currentTimeMillis() % 1000000 +
		// ".jpg";
		// } else {
		// BusinessLicensePictureName = "BLP" + System.currentTimeMillis() % 1000000 +
		// ".png";
		// }
		//
		// // 找到对应的文件夹放到session里面
		// File businessLicensePictureDestination = new
		// File(BaseAction.BusinessLicensePictureDir + BusinessLicensePictureName);
		//
		// session.setAttribute(EnumSession.SESSION_CompanyBusinessLicensePictureDestination.getName(),
		// businessLicensePictureDestination);
		// session.setAttribute(EnumSession.SESSION_CompanyBusinessLicensePictureFILE.getName(),
		// file);

		// }
		// } while (false);

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	private boolean checkDisk(Map<String, Object> params) {
		// 检查当前磁盘是否存在，如果存在，检查当前磁盘的可用空间
		File diskPath = new File(Disk);
		if (!diskPath.exists()) {
			logger.error("该路径不存在:" + diskPath);

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
			return false;
		}
		//
		ErrorInfo ecOut = new ErrorInfo();
		BxConfigGeneral extraDiskSpaceSize = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.EXTRA_DISK_SPACE_SIZE, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);

		long minUsablePatitionSpace = 1;
		logger.info("磁盘可用空间为:" + diskPath.getUsableSpace() / (1024 * 1024 * 1024) + "GB");
		if (diskPath.getUsableSpace() - Integer.valueOf(extraDiskSpaceSize.getValue()) < minUsablePatitionSpace) {
			logger.error("磁盘空间不足!");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "服务器错误");
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/uploadLogoEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String uploadLogoEx(@ModelAttribute("SpringWeb") Company company, ModelMap model, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("公司上传Logo，company=" + company);
		//
		session.removeAttribute(EnumSession.SESSION_CompanyLogoDestination.getName());
		session.removeAttribute(EnumSession.SESSION_CompanyLogoFILE.getName());

		Map<String, Object> params = new HashMap<>();
		String fileFirstName = "Logo";

		if (uploadImg(params, session, file, BaseCache.CompanyLogoVolumeMax, BaseAction.CompanyLogoDir, BaseCache.CompanyLogoDir, //
				fileFirstName, EnumSession.SESSION_CompanyLogoDestination.getName(), EnumSession.SESSION_CompanyLogoFILE.getName())) //
		{
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.KEY_Object, company);
		}

		// do {
		// if (!checkDisk(params)) {
		// break;
		// }
		//
		// if (!file.isEmpty()) {
		// if (!JPEGType.equals(file.getContentType()) &&
		// !PNGType.equals(file.getContentType())) {
		// logger.info("公司的Logo不符合,当前的图片类型为:" + file.getContentType());
		//
		// params.put(BaseAction.JSON_ERROR_KEY,
		// EnumErrorCode.EC_OtherError.toString());
		// params.put(KEY_HTMLTable_Parameter_msg, "公司的Logo类型必须是.jpg/.jpeg/.png格式");
		// break;
		// }
		//
		// Long size = file.getSize();
		// ErrorInfo ecOut = new ErrorInfo();
		// BxConfigGeneral logoVolumeMax = (BxConfigGeneral)
		// CacheManager.getCache(BaseAction.DBName_Public,
		// EnumCacheType.ECT_BXConfigGeneral)//
		// .read1(BaseCache.CompanyLogoVolumeMax,
		// getBxStaffFromSession(session).getID(), ecOut, BaseAction.DBName_Public);
		// if (size <= 0 || size > Long.parseLong(logoVolumeMax.getValue())) {
		// logger.info("公司Logo大小不符合要求，size=" + size);
		//
		// params.put(BaseAction.JSON_ERROR_KEY,
		// EnumErrorCode.EC_OtherError.toString());
		// params.put(KEY_HTMLTable_Parameter_msg, "公司Logo大小不能超过100KB");
		// break;
		// }
		//
		// // 判断文件夹是否存在，如果不存在，创建相应的文件夹
		// File companyLogoDir = new File(BaseAction.CompanyLogoDir);
		// if (!companyLogoDir.exists()) {
		// companyLogoDir.mkdirs();
		// }
		//
		// BxConfigGeneral logoDir = (BxConfigGeneral)
		// CacheManager.getCache(BaseAction.DBName_Public,
		// EnumCacheType.ECT_BXConfigGeneral)//
		// .read1(BaseCache.CompanyLogoDir, getBxStaffFromSession(session).getID(),
		// ecOut, BaseAction.DBName_Public);
		// if (!BaseAction.CompanyLogoDir.equals(logoDir.getValue())) {
		// logger.error("不存在此目录，path=" + BaseAction.CompanyLogoDir);
		//
		// params.put(BaseAction.JSON_ERROR_KEY,
		// EnumErrorCode.EC_OtherError.toString());
		// params.put(KEY_HTMLTable_Parameter_msg, "不存在此目录，path=" +
		// BaseAction.CompanyLogoDir);
		// break;
		// }
		//
		// // 判断是jpg还是png类型，上传名称
		// String logoName = "";
		// if (file.getContentType().contains(JPEGType)) {
		// logoName = "logo" + System.currentTimeMillis() % 1000000 + ".jpg";
		// } else {
		// logoName = "logo" + System.currentTimeMillis() % 1000000 + ".png";
		// }
		//
		// // 找到对应的文件夹放到session里面
		// File logoDestination = new File(BaseAction.CompanyLogoDir + logoName);
		//
		// session.setAttribute(EnumSession.SESSION_CompanyLogoDestination.getName(),
		// logoDestination);
		// session.setAttribute(EnumSession.SESSION_CompanyLogoFILE.getName(), file);
		// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		// params.put(KEY_HTMLTable_Parameter_msg, "");
		// params.put(BaseAction.KEY_Object, company);
		// }
		// } while (false);

		logger.info("返回的数据=" + params);
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: GET http://localhost:8080/nbr/company/retrieveNEx.bx
	 * 该接口用于查询所有公司的门店,返回所有门店的信息:包括company信息，pos信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("查询多个门店，shop=" + shop);
		//
		if (session.getAttribute(EnumSession.SESSION_BxStaff.getName()) == null) {
			logger.debug("非OP帐号不能查询门店！");
			return null;
		}
		//
		Map<String, Object> params = new HashMap<String, Object>();
		//
		Company company = getCompanyFromSession(session);
		// 1、查询所有公司
		DataSourceContextHolder.setDbName(company.getDbName());
		// ... 由于是业务经理使用该功能，临时处理方案为设成最高权限
		Company company1 = new Company();
		company1.setPageIndex(BaseAction.PAGE_StartIndex);
		company1.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<Company> listComp = (List<Company>) companyBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, company1);
		if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询所有公司失败！错误信息：" + companyBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, companyBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());

			logger.info("返回的数据=" + params);
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}

		// 2、获取到所有的shop
		List<BaseModel> listShops = new ArrayList<>();
		for (Company com : listComp) {
			if (com.getDbName().equals(BaseAction.DBName_Public)) {
				continue;
			}
			Shop shopRN = new Shop();
			shopRN.setPageIndex(BaseAction.PAGE_StartIndex);
			shopRN.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			shopRN.setDistrictID(shop.getDistrictID());
			List<List<BaseModel>> listBM = null;
			try {
				DataSourceContextHolder.setDbName(com.getDbName());
				listBM = shopBO.retrieveNObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shopRN);
			} catch (Exception e) {
				logger.error("加载门店异常，异常DB为：" + com.getDbName() + "\r\n，异常信息为：" + e.getMessage());
				continue;
			}
			if (shopBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询shop失败！当前查询的company为" + com + ";\r\n" + shopBO.printErrorInfo());
				continue;
			}
			// RN Shop
			List<BaseModel> listShop = listBM.get(0);

			List<BaseModel> bxStaffList = listBM.get(2);

			for (BaseModel bm : listShop) {
				Shop s = (Shop) bm;
				s.setCompany(com);
				// 将shop的敏感信息隐藏
				s.setKey("");

				Pos pos = new Pos();
				pos.setShopID(s.getID());
				DataSourceContextHolder.setDbName(com.getDbName());
				List<Pos> posList = (List<Pos>) posBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, pos);
				if (posBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("读取相应的门店下的所有POS机失败，错误码=" + posBO.getLastErrorCode().toString());

					params.put(KEY_HTMLTable_Parameter_msg, posBO.getLastErrorMessage());
					params.put(BaseAction.JSON_ERROR_KEY, posBO.getLastErrorCode().toString());
					break;
				}
				s.setListPos(posList);

				for (BaseModel bxstaff : bxStaffList) {
					BxStaff bxStaff = (BxStaff) bxstaff;

					if (s.getBxStaffID() == bm.getID()) {
						s.setBxStaffName(bxStaff.getName());
					}
				}
				listShops.add(s);
			}

		}

		// 将获取到的list进行分页处理
		List<BaseModel> shList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(listShops)) {
			int currIdx = (shop.getPageIndex() > 1 ? (shop.getPageIndex() - 1) * shop.getPageSize() : 0);
			for (int i = 0; i < shop.getPageSize() && i < listShops.size() - currIdx; i++) {
				BaseModel bm = listShops.get(currIdx + i);
				shList.add(bm);
			}
		}

		params.put(BaseAction.JSON_ERROR_KEY, companyBO.getLastErrorCode().toString());
		params.put(KEY_HTMLTable_Parameter_msg, "");
		params.put(KEY_ObjectList, shList);
		params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, listShops.size());
		logger.info("返回的数据=" + params.toString());

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * // * http://localhost:8080/nbr/company/retrieveNToCheckUniqueFieldEx.bx //
	 */
	// @RequestMapping(value = "/retrieveNToCheckUniqueFieldEx", produces =
	// "plain/text; charset=UTF-8", method = RequestMethod.POST)
	// @ResponseBody
	// public String retrieveNToCheckUniqueFieldEx(@ModelAttribute("SpringWeb")
	// Company company, ModelMap model, HttpSession session) {
	// String dbName = getDBNameFromSession(session);
	// // 选传参数：ID，必传参数：string1和int1
	// // 如果是要进行修改操作,则要传对应的ID参数，不然即使不做修改，也会报已存在该字段的错误信息
	// // string1存储的是要检查的字段值，int1如下描述
	// // int1=1，代表要检查商品名称
	//
	// return doRetrieveNToCheckUniqueFieldEx(company, dbName);
	// }
	//
	// @Override
	// protected BaseBO getBO() {
	// return companyBO;
	// }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "createShop", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView createShop(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}

		logger.info("查询多个门店，shop=" + shop);
		//
		if (session.getAttribute(EnumSession.SESSION_BxStaff.getName()) == null) {
			logger.debug("非OP帐号不能查询门店！");
			return null;
		}
		//
		Map<String, Object> params = new HashMap<String, Object>();
		//
		Company company = getCompanyFromSession(session);
		// 1、查询所有公司
		DataSourceContextHolder.setDbName(company.getDbName());
		// ... 由于是业务经理使用该功能，临时处理方案为设成最高权限
		Company company1 = new Company();
		company1.setPageIndex(BaseAction.PAGE_StartIndex);
		company1.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<Company> listComp = (List<Company>) companyBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, company1);
		if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.error("查询所有公司失败！错误信息：" + companyBO.getLastErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, companyBO.getLastErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, companyBO.getLastErrorMessage());

			logger.info("返回的数据=" + params);
			return new ModelAndView(COMPANY_CreateShop, "", "");
		}
		Iterator<Company> it = listComp.iterator();
		while (it.hasNext())
		{
			Company companyIterator = (Company) it.next();
			if(companyIterator.getDbName().equals(BaseAction.DBName_Public)) {
				listComp.remove(companyIterator);
			}
		}
		mm.put("shop", new ShopField());
		mm.put("companyList", listComp);
		// break;
		// default:
		// logger.info("其他错误");
		// mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
		// break;
		// }
		return new ModelAndView(COMPANY_CreateShop, "", "");
	}
	
	@RequestMapping(value = "/createShopEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String createShopEx(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("创建一个公司门店,shop=" + shop.toString());
		BxStaff bxStaffFromSession = (BxStaff) session.getAttribute(EnumSession.SESSION_BxStaff.getName());
		//
		if (bxStaffFromSession == null) {
			logger.debug("非OP帐号不能创建公司门店！");
			return null;
		}
		
		//
		Lock lock = new ReentrantLock();

		lock.lock();

		Map<String, Object> params = getDefaultParamToReturn(true);
		try {
			
			DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
			
			Company companyR1Condition = new Company();
			companyR1Condition.setID(shop.getCompanyID());

			logger.info("查询一个公司,companyR1=" + companyR1Condition.toString());

			Company companyR1 = (Company) companyBO.retrieve1Object(getBxStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, companyR1Condition);
			if(companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个公司失败，" + companyBO.printErrorInfo());
			}
			shop.setKey(companyR1.getKey()); // TODO
			shop.setDistrictID(1);
			shop.setStatus(Shop.EnumStatusShop.ESS_Online.getIndex());
			shop.setBxStaffID(bxStaffFromSession.getID());

			DataSourceContextHolder.setDbName(companyR1.getDbName());
			Shop shopCreated = (Shop) shopBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shop);
			logger.info("Create shop error code = " + shopBO.printErrorInfo());

			switch (shopBO.getLastErrorCode()) {
			case EC_NoError:
				logger.info("创建公司门店成功，创建的对象为：" + (shopCreated == null ? "NULL" : shopCreated));
				// 创建门店对应的仓库, 仓库信息先默认
				Warehouse wsCreateCondition = new Warehouse();
				wsCreateCondition.setName(shopCreated.getName() + "的仓库");
				wsCreateCondition.setAddress("未知地址");
				wsCreateCondition.setStaffID(2);
				DataSourceContextHolder.setDbName(companyR1.getDbName());
				Warehouse warehouseCreated = (Warehouse) warehouseBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, wsCreateCondition);
				if(warehouseBO.getLastErrorCode() != EnumErrorCode.EC_NoError || warehouseCreated == null) {
					logger.error("创建门店对应的仓库失败," + warehouseBO.printErrorInfo());
				}
				logger.info("Create warehouse error code = " + warehouseBO.printErrorInfo());
				// 创建该门店对应的商品门店信息
				CommodityShopInfo commodityShopInfoRnCondition = new CommodityShopInfo();
				commodityShopInfoRnCondition.setShopID(2); // TODO 先使用默认门店的商品信息作为模版
				commodityShopInfoRnCondition.setCommodityID(BaseAction.INVALID_ID);
				DataSourceContextHolder.setDbName(companyR1.getDbName());
				List<CommodityShopInfo> commodityShopInfoList = (List<CommodityShopInfo>) commodityShopInfoBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityShopInfoRnCondition);
				if(commodityShopInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询商品门店信息失败," + commodityShopInfoBO.printErrorInfo());
				} else {
					for(CommodityShopInfo commodityShopInfo : commodityShopInfoList) {
						commodityShopInfo.setShopID(shopCreated.getID());
						commodityShopInfo.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
						commodityShopInfo.setnOStart(Commodity.NO_START_Default);
						commodityShopInfo.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
						commodityShopInfo.setCurrentWarehousingID(0);
						commodityShopInfo.setOperatorStaffID(4); // 老板
						DataSourceContextHolder.setDbName(companyR1.getDbName());
						CommodityShopInfo commodityShopInfoCreated = (CommodityShopInfo) commodityShopInfoBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityShopInfo);
						if(commodityShopInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError || commodityShopInfoCreated == null) {
							logger.error("查询商品门店信息失败," + commodityShopInfoBO.printErrorInfo());
						}
					}
				}
				params.put(KEY_Object, shopCreated);
				params.put(KEY_Object2, warehouseCreated);
				params.put(KEY_HTMLTable_Parameter_msg, "");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
				break;
			case EC_Duplicated:
				logger.info("创建失败，错误码为：" + EnumErrorCode.EC_Duplicated);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage().toString());
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
				break;
			case EC_NoPermission:
				logger.error("没有权限");
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
				break;
			case EC_WrongFormatForInputField:
				logger.error("格式错误！");
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
				break;
			default:
				logger.error("其他错误！");
				params.put(KEY_HTMLTable_Parameter_msg, "创建公司门店异常，请重试");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			}
			logger.info("返回的数据=" + params);

		} catch (Exception e) {
			logger.error("创建公司门店异常：" + e.getMessage());
			params.put(KEY_HTMLTable_Parameter_msg, "创建公司门店异常，请重试");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} finally {
			lock.unlock();
		}

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
	
	
	
	
	@RequestMapping(value = "/updateShopEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String updateShopEx(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("更新一个公司门店,shop=" + shop.toString());
		BxStaff bxStaffFromSession = (BxStaff) session.getAttribute(EnumSession.SESSION_BxStaff.getName());
		//
		if (bxStaffFromSession == null) {
			logger.debug("非OP帐号不能更新公司门店！");
			return null;
		}
		
//		shop.setKey("123456"); // TODO
//		shop.setDistrictID(1);
		shop.setBxStaffID(bxStaffFromSession.getID());
		//
		Lock lock = new ReentrantLock();

		lock.lock();

		Map<String, Object> params = getDefaultParamToReturn(true);
		try {
			
			DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
			
			Company companyR1Condition = new Company();
			companyR1Condition.setID(shop.getCompanyID());

			logger.info("查询一个公司,companyR1=" + companyR1Condition.toString());

			Company companyR1 = (Company) companyBO.retrieve1Object(getBxStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, companyR1Condition);
			if(companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个公司失败，" + companyBO.printErrorInfo());
			}

			DataSourceContextHolder.setDbName(companyR1.getDbName());
			Shop shopCreated = (Shop) shopBO.updateObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shop);
			logger.info("Create shop error code = " + shopBO.printErrorInfo());

			switch (shopBO.getLastErrorCode()) {
			case EC_NoError:
				logger.info("更新公司门店成功，更新的对象为：" + (shopCreated == null ? "NULL" : shopCreated));
//				//
				params.put(KEY_Object, shopCreated);
				params.put(KEY_HTMLTable_Parameter_msg, "");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
				break;
			case EC_Duplicated:
				logger.info("创建失败，错误码为：" + EnumErrorCode.EC_Duplicated);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage().toString());
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
				break;
			case EC_NoPermission:
				logger.error("没有权限");
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
				break;
			case EC_WrongFormatForInputField:
				logger.error("格式错误！");
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
				break;
			case EC_BusinessLogicNotDefined:
				logger.error("业务逻辑错误！");
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
				break;
			default:
				logger.error("其他错误！");
				params.put(KEY_HTMLTable_Parameter_msg, "更新公司门店异常，请重试");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			}
			logger.info("返回的数据=" + params);

		} catch (Exception e) {
			logger.error("更新公司门店异常：" + e.getMessage());
			params.put(KEY_HTMLTable_Parameter_msg, "更新公司门店异常，请重试");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} finally {
			lock.unlock();
		}

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
	
	@RequestMapping(value = "/deleteShopEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String deleteShopEx(@ModelAttribute("SpringWeb") Shop shop, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("冻结一个公司门店,shop=" + shop.toString());
		BxStaff bxStaffFromSession = (BxStaff) session.getAttribute(EnumSession.SESSION_BxStaff.getName());
		//
		if (bxStaffFromSession == null) {
			logger.debug("非OP帐号不能冻结公司门店！");
			return null;
		}
		
		shop.setBxStaffID(bxStaffFromSession.getID());
		//
		Lock lock = new ReentrantLock();

		lock.lock();

		Map<String, Object> params = getDefaultParamToReturn(true);
		try {
			
			DataSourceContextHolder.setDbName(BaseAction.DBName_Public);
			
			Company companyR1Condition = new Company();
			companyR1Condition.setID(shop.getCompanyID());

			logger.info("查询一个公司,companyR1=" + companyR1Condition.toString());

			Company companyR1 = (Company) companyBO.retrieve1Object(getBxStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, companyR1Condition);
			if(companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询一个公司失败，" + companyBO.printErrorInfo());
			}

			DataSourceContextHolder.setDbName(companyR1.getDbName());
			shopBO.deleteObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shop);
			logger.info("Create shop error code = " + shopBO.printErrorInfo());

			switch (shopBO.getLastErrorCode()) {
			case EC_NoError:
				logger.info("冻结公司门店成功");
				// 更新pos的普通缓存
				CacheManager.getCache(companyR1.getDbName(), EnumCacheType.ECT_POS).deleteAll();
				// 更新pos的同步缓存
				psc.load(companyR1.getDbName());
				params.put(KEY_HTMLTable_Parameter_msg, "");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
				break;
			case EC_Duplicated:
				logger.info("创建失败，错误码为：" + EnumErrorCode.EC_Duplicated);
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage().toString());
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_Duplicated.toString());
				break;
			case EC_NoPermission:
				logger.error("没有权限");
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "权限不足");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
				break;
			case EC_WrongFormatForInputField:
				logger.error("格式错误！");
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_WrongFormatForInputField.toString());
				break;
			case EC_BusinessLogicNotDefined:
				logger.error("业务逻辑错误！");
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, shopBO.getLastErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_BusinessLogicNotDefined.toString());
				break;
			default:
				logger.error("其他错误！");
				params.put(KEY_HTMLTable_Parameter_msg, "创建公司门店异常，请重试");
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
				break;
			}
			logger.info("返回的数据=" + params);

		} catch (Exception e) {
			logger.error("冻结公司门店异常：" + e.getMessage());
			params.put(KEY_HTMLTable_Parameter_msg, "冻结公司门店异常，请重试");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		} finally {
			lock.unlock();
		}

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
