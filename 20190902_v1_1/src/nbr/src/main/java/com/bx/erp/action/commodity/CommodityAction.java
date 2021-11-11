package com.bx.erp.action.commodity;

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
import org.springframework.web.servlet.ModelAndView;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.purchasing.ProviderBO;
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.action.bo.warehousing.InventoryCommodityBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.action.bo.warehousing.WarehousingCommodityBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.BrandBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CategoryParentBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.CommodityPropertyBO;
import com.bx.erp.action.bo.commodity.CommodityShopInfoBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.commodity.SubCommodityBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import net.sf.json.JSONObject;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityField;
import com.bx.erp.model.commodity.CommodityInfo;
import com.bx.erp.model.commodity.CommodityProperty;
import com.bx.erp.model.commodity.CommodityPropertyField;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.model.purchasing.ProviderField;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.bx.erp.util.JsonUtil;

@Controller
@RequestMapping("/commodity")
@Scope("prototype")
public class CommodityAction extends BaseAction {
	private Log logger = LogFactory.getLog(CommodityAction.class);

	@Resource
	private CommodityPropertyBO commodityPropertyBO;

	@Resource
	private CommodityBO commodityBO;
	
	@Resource
	private CommodityShopInfoBO commodityShopInfoBO;

	@Resource
	private CategoryBO categoryBO;

	@Resource
	private ProviderBO providerBO;

	@Resource
	private BrandBO brandBO;

	@Resource
	private SubCommodityBO subCommodityBO;

	@Resource
	private InventoryCommodityBO inventoryCommodityBO;

	@Resource
	private BarcodesBO barcodesBO;

	@Resource
	private PackageUnitBO packageUnitBO;

	@Resource
	private WarehousingBO warehousingBO;

	@Resource
	private WarehousingCommodityBO warehousingCommodityBO;

	@Resource
	private CategoryParentBO categoryParentBO;

	@Resource
	private ProviderCommodityBO providerCommodityBO;

	// private static final int MULTI_PACKAGING_ELEMENT_COUNT = 8;

	// Fiddler URL: http://localhost:8080/nbr/commodity.bx
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(@ModelAttribute("SpringWeb") Commodity comm, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		// ...将来重构为使用缓存。次次都从DB中读取，代价太大
		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		logger.info("进入商品页面时加载所有的商品相关数据，comm=" + comm);
		//
		PackageUnit pu = new PackageUnit();
		pu.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<?> puList = packageUnitBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pu);
		List<BaseModel> categoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(true, false);
		List<BaseModel> brandList = CacheManager.getCache(dbName, EnumCacheType.ECT_Brand).readN(true, false); // ...这里需要取全部数据？
		List<BaseModel> providerList = CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).readN(true, false); // ...这里需要取全部数据？
		List<BaseModel> categoryParentList = CacheManager.getCache(dbName, EnumCacheType.ECT_CategoryParent).readN(true, false);
		List<BaseModel> shopList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有员工
		// 去除虚拟总部
		for(int i = 0; i < shopList.size(); i++) {
			if(shopList.get(i).getID() == 1) {
				shopList.remove(i);
			}
		}
		//
		CommodityProperty cp = new CommodityProperty();
		cp.setID(1);
		DataSourceContextHolder.setDbName(dbName);
		CommodityProperty commodityProperty = (CommodityProperty) commodityPropertyBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, cp);
		if (commodityPropertyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("查询单个商品属性失败，错误码=" + commodityPropertyBO.getLastErrorCode().toString());
		} else {
			logger.info("查询成功！commodityProperty=" + commodityProperty == null ? "null" : commodityProperty);
		}

		mm.put("commodityProperty", commodityProperty);
		mm.put("PackageUnitList", puList);
		mm.put("brandList", brandList);
		mm.put("providerList", providerList);
		mm.put("categoryParentList", categoryParentList);
		mm.put("categoryList", categoryList);
		mm.put("CommodityField", new CommodityField());
		mm.put("shopList", shopList);

		return new ModelAndView(COMMODITY_Index, "", null);
	}

	//导入商品资料跳转页面
	@RequestMapping(value = "/toImportData", method = RequestMethod.GET)
	public String toImportData(ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		return TO_ImportData;
	}
	// Fiddler URL: http://localhost:8080/nbr/commodity/about.bx
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String commodityAbout(@ModelAttribute("SpringWeb") Commodity comm, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		//
		PackageUnit pu = new PackageUnit();
		pu.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<?> puList = packageUnitBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pu);
		if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("查询所有包装单位失败，错误码=" + packageUnitBO.getLastErrorCode().toString());
		} else {
			logger.info("查询成功！puList=" + puList);
		}
		//
		Provider p = new Provider();
		p.setPageSize(10);
		DataSourceContextHolder.setDbName(dbName);
		List<?> providerList = providerBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, p);
		if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("查询多个供应商失败，错误码=" + providerBO.getLastErrorCode().toString());
		} else {
			logger.info("查询成功！providerList=" + providerList);
		}
		List<BaseModel> providerDistrictList = CacheManager.getCache(dbName, EnumCacheType.ECT_ProviderDistrict).readN(true, false);
		List<BaseModel> brandList = CacheManager.getCache(dbName, EnumCacheType.ECT_Brand).readN(true, false);
		List<BaseModel> categoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(true, false);
		List<BaseModel> categoryParentList = CacheManager.getCache(dbName, EnumCacheType.ECT_CategoryParent).readN(true, false);
		//
		CommodityProperty cp = new CommodityProperty();
		cp.setID(1);
		DataSourceContextHolder.setDbName(dbName);
		CommodityProperty commodityProperty = (CommodityProperty) commodityPropertyBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, cp);
		if (commodityPropertyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("查询单个商品属性失败，错误码=" + commodityPropertyBO.getLastErrorCode().toString());
		} else {
			logger.info("查询成功！commodityProperty=" + commodityProperty == null ? "null" : commodityProperty);
		}
		//
		mm.put("PackageUnitList", puList);
		mm.put("commodityProperty", commodityProperty);
		mm.put("categoryParentList", categoryParentList);
		mm.put("categoryList", categoryList);
		mm.put("brandList", brandList);
		mm.put("providerDistrictList", providerDistrictList);
		mm.put("providerList", providerList);
		mm.put("ProviderField", new ProviderField());
		mm.put("CommodityPropertyField", new CommodityPropertyField());
		return COMMODITY_About;
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * pricePurchase=0.04&ID=1 URL:
	 * http://localhost:8080/nbr/commodity/combination.bx
	 */
	@RequestMapping(value = "/combination", method = RequestMethod.GET)
	public String combinationCommodity(@ModelAttribute("SpringWeb") Commodity comm, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入组合新增商品界面时加载所有包装单位，大小类，comm=" + comm);
		// ...将来重构为使用缓存。次次都从DB中读取，代价太大
		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		//
		PackageUnit pu = new PackageUnit();
		pu.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<?> puList = packageUnitBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pu);
		if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("查询失败！错误码=" + packageUnitBO.getLastErrorCode().toString());
			mm.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode().toString());
			return "";
		} else {
			logger.info("查询成功！puList=" + puList);
		}
		List<BaseModel> categoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(true, false);
		List<BaseModel> categoryParentList = CacheManager.getCache(dbName, EnumCacheType.ECT_CategoryParent).readN(true, false);
		//
		mm.put("puList", puList);
		mm.put("categoryParentList", categoryParentList);
		mm.put("categoryList", categoryList);
		return COMMODITY_Combination;
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler:
	 * pricePurchase=0.04&ID=1 URL: http://localhost:8080/nbr/commodity/toCreate.bx
	 */
	@RequestMapping(value = "/toCreate", method = RequestMethod.GET)
	public String toCreate(@ModelAttribute("SpringWeb") Commodity comm, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入新增商品界面时加载商品的所有的数据,comm=" + comm);
		// ...从缓存中获取数据，否则代价太大。找Giggs重构：缓存第1页的数据
		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		// 类别 品牌 供应商
		ErrorInfo ecOut = new ErrorInfo();
		do {
			PackageUnit pu = new PackageUnit();
			pu.setPageSize(PAGE_SIZE_Infinite);
			DataSourceContextHolder.setDbName(dbName);
			List<?> puList = packageUnitBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pu);
			if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				ecOut.setErrorCode(packageUnitBO.getLastErrorCode());
				break;
			} else {
				logger.info("查询成功！puList=" + puList);
			}
			List<BaseModel> categoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(true, false);
			List<BaseModel> brandList = CacheManager.getCache(dbName, EnumCacheType.ECT_Brand).readN(true, false);
			List<BaseModel> providerList = CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).readN(true, false); // ...这里需要取全部数据？
			List<BaseModel> categoryParentList = CacheManager.getCache(dbName, EnumCacheType.ECT_CategoryParent).readN(true, false);
			//
			CommodityProperty cp = new CommodityProperty();
			cp.setID(1);
			DataSourceContextHolder.setDbName(dbName);
			CommodityProperty commodityProperty = (CommodityProperty) commodityPropertyBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, cp);
			if (commodityPropertyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				ecOut.setErrorCode(commodityPropertyBO.getLastErrorCode());
				break;
			} else {
				logger.info("查询成功！commodityProperty=" + commodityProperty);
			}
			mm.put("commodityProperty", commodityProperty);
			mm.put("puList", puList);
			mm.put("comm", comm);
			mm.put("categoryList", categoryList);
			mm.put("brandList", brandList);
			mm.put("providerList", providerList);
			mm.put("categoryParentList", categoryParentList);
			mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		// ...use do while, check error after bo.xxx()
		mm.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode());

		return COMMODITY_ToCreate;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean setProviderAndBarcodesAndCommodityProperty(Map<String, Object> params, List<CommodityInfo> listCI, List<Commodity> commList, ProviderCommodity pc, Barcodes barcodes, CommodityProperty cp, boolean bHasDBError,
			String dbName, HttpSession session, Commodity commodityArgs) {
		logger.info("设置供应商，条形码和大类，传入的数据=" + params);

		for (Commodity comm : commList) {
			CommodityInfo commodityInfo = new CommodityInfo();
			// 单品供应商
			pc.setCommodityID(comm.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<ProviderCommodity> pcList = (List<ProviderCommodity>) providerCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pc);
			logger.info("Retrieve N providerCommodity error code=" + providerCommodityBO.getLastErrorCode());
			if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(BaseAction.JSON_ERROR_KEY, providerCommodityBO.getLastErrorCode().toString());
				break;

			}

			barcodes.setCommodityID(comm.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> barcodesList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, barcodes);
			logger.info("Retrieve N barcodes error code=" + barcodesBO.getLastErrorCode());
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(BaseAction.JSON_ERROR_KEY, barcodesBO.getLastErrorCode().toString());
				break;
			}

			Provider porvider = new Provider();
			List<Provider> providerList = new ArrayList();
			for (int i = 0; i < pcList.size(); i++) {
				porvider.setID(pcList.get(i).getProviderID());
				DataSourceContextHolder.setDbName(dbName);
				Provider provider = (Provider) providerBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, porvider);
				logger.info("Retrieve 1 provider error code=" + providerBO.getLastErrorCode());
				if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					params.put(BaseAction.JSON_ERROR_KEY, providerBO.getLastErrorCode().toString());
					bHasDBError = true;
					break;
				}
				providerList.add(provider);
			}

			cp.setID(1);
			DataSourceContextHolder.setDbName(dbName);
			CommodityProperty commodityProperty = (CommodityProperty) commodityPropertyBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, cp);
			logger.info("Retrieve 1 CommodityProperty error code=" + commodityPropertyBO.getLastErrorCode());
			if (commodityPropertyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(BaseAction.JSON_ERROR_KEY, commodityPropertyBO.getLastErrorCode().toString());
				bHasDBError = true;
				break;
			}

			// 多包装商品
			DataSourceContextHolder.setDbName(dbName);
			List<Commodity> listMPCommodity = (List<Commodity>) commodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.CASE_RetrieveNMultiPackageCommodity, (Commodity) comm);
			logger.info("Retrieve N MultiPackageCommodity error code=" + commodityBO.getLastErrorCode());
			if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
				break;
			}
			logger.info("listMPCommodity=" + listMPCommodity);

			if (listMPCommodity != null && listMPCommodity.size() > 0) {
				bHasDBError = setBarcodeAndPackageUnitToCommodityList(params, listMPCommodity, dbName, session);
				if (bHasDBError) {
					break;
				}
				// 商品门店信息
				for(Commodity commodityRnFromDB : listMPCommodity) {
					CommodityShopInfo commodityShopInfoRnCondition = new CommodityShopInfo();
					commodityShopInfoRnCondition.setCommodityID(commodityRnFromDB.getID());
					commodityShopInfoRnCondition.setShopID(commodityArgs.getShopID());
					DataSourceContextHolder.setDbName(dbName);
					List<CommodityShopInfo> listCommodityShopInfo = (List<CommodityShopInfo>) commodityShopInfoBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, commodityShopInfoRnCondition);
					if (commodityShopInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						logger.error("查询商品门店信息对象失败，错误码=" + commodityShopInfoBO.getLastErrorCode());
						params.put(BaseAction.JSON_ERROR_KEY, commodityShopInfoBO.getLastErrorCode().toString());
						params.put(KEY_HTMLTable_Parameter_msg, commodityShopInfoBO.getLastErrorMessage());
						break;
					}
					commodityRnFromDB.setListSlave2(listCommodityShopInfo);
				}
			}
			// 如果该商品是组合商品，则返回该商品的子商品的信息
			if (comm.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				// 子商品表
				SubCommodity subCommodity = new SubCommodity();
				subCommodity.setCommodityID(comm.getID());
				DataSourceContextHolder.setDbName(dbName);
				List<SubCommodity> listSubCommodity = (List<SubCommodity>) subCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, subCommodity);
				// 子商品
				List<Commodity> commListSlave = new ArrayList<>();
				for (SubCommodity subComm : listSubCommodity) {
					Commodity commodity = new Commodity();
					commodity.setID(subComm.getSubCommodityID());
					DataSourceContextHolder.setDbName(dbName);
					List<List<BaseModel>> commRetrieved = commodityBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, commodity);
					if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
						params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
						break;
					}
					Commodity commR1 = Commodity.fetchCommodityFromResultSet(commRetrieved);
					commR1.setSubCommodityNO(subComm.getSubCommodityNO());//
					commR1.setSubCommodityPrice(String.valueOf(subComm.getPrice()));//
					commListSlave.add(commR1);
				}
				// 子商品条码、包装单位
				bHasDBError = setBarcodeAndPackageUnitToCommodityList(params, commListSlave, dbName, session);
				if (bHasDBError) {
					break;
				}
				comm.setListSlave1(commListSlave);
			}

			//
			commodityInfo.setListProvider(providerList);
			comm.setSyncDatetime(new Date());
			commodityInfo.setListBarcodes(barcodesList);
			commodityInfo.setCommodityProperty(commodityProperty);
			commodityInfo.setListMultiPackageCommodity(listMPCommodity);
			commodityInfo.setCommodity((Commodity) comm);
			listCI.add(commodityInfo);
		}
		return bHasDBError;
	}

	/*
	 * 点击编辑按钮，获取商品的model绑定到编辑商品的页面，然后跳转到该页面 Request Body in Fiddler:
	 * pricePurchase=0.04&ID=1 URL: http://localhost:8080/nbr/commodity/toUpdate.bx
	 */
	@RequestMapping(value = "/toUpdate", method = RequestMethod.GET)
	public String toUpdate(@ModelAttribute("SpringWeb") Commodity comm, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("进入修改页面之前带入数据，comm=" + comm);

		Company company = getCompanyFromSession(session);

		DataSourceContextHolder.setDbName(company.getDbName());
		List<List<BaseModel>> commR1 = commodityBO.retrieve1ObjectEx(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, comm);

		switch (commodityBO.getLastErrorCode()) {
		case EC_NoError:
			Commodity bm = Commodity.fetchCommodityFromResultSet(commR1);
			logger.info("commodity=" + bm);
			mm.put("comm", bm);
			return COMMODITY_ToUpdate;
		default:
			return COMMODITY_ToUpdate;
		}
	}

	/*
	 * Request Body in Fiddler: pricePurchase=0.04&ID=1 URL:
	 * http://localhost:8080/nbr/commodity/retrieve1.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieve1", method = RequestMethod.GET)
	public String retrieve1(@ModelAttribute("SpringWeb") Commodity comm, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		//
		List<BaseModel> warehouseList = CacheManager.getCache(dbName, EnumCacheType.ECT_Warehouse).readN(false, false);
		List<BaseModel> categoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(true, false);
		List<BaseModel> brandList = CacheManager.getCache(dbName, EnumCacheType.ECT_Brand).readN(true, false);
		List<BaseModel> providerList = CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).readN(true, false);
		//
		CategoryParent cp = new CategoryParent();
		cp.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<?> categoryParentList = categoryParentBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, cp);
		if (categoryParentBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("Retrieve N categoryParent error code=" + categoryParentBO.getLastErrorCode());
			mm.put(BaseAction.JSON_ERROR_KEY, categoryParentBO.getLastErrorCode().toString());
			return "";
		}

		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(comm.getID(), getStaffFromSession(session).getID(), ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			mm.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode());
			mm.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage());
			return "";
		}
		if (commodity == null) {
			return "";
		}
		//
		Barcodes b = new Barcodes();
		b.setCommodityID(comm.getID());
		DataSourceContextHolder.setDbName(dbName);
		List<Barcodes> barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
		if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("Retrieve N Barcodes error code=" + barcodesBO.getLastErrorCode());
			mm.put(BaseAction.JSON_ERROR_KEY, barcodesBO.getLastErrorCode().toString());
			return "";
		}
		//
		String barcodes = "";
		for (Barcodes bc : barcodeList) {
			barcodes += bc.getBarcode() + " ";
		}
		commodity.setBarcodes(barcodes);
		//
		PackageUnit p = new PackageUnit();
		p.setID(commodity.getPackageUnitID());
		DataSourceContextHolder.setDbName(dbName);
		PackageUnit packageUnit = (PackageUnit) packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, p);
		if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("Retrieve 1 packageUnit error code=" + packageUnitBO.getLastErrorCode());
			mm.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode().toString());
			return "";
		}
		//
		commodity.setPackageUnitName(packageUnit.getName());

		p.setPageSize(PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);
		List<?> puList = packageUnitBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, p);
		if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("Retrieve N packageUnit error code=" + packageUnitBO.getLastErrorCode());
			mm.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode().toString());
			return "";
		}
		//
		DataSourceContextHolder.setDbName(dbName);
		List<Commodity> MPCommodityList = (List<Commodity>) commodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.CASE_RetrieveNMultiPackageCommodity, comm);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("Retrieve N MultiPackageCommodity error code=" + commodityBO.getLastErrorCode());
			mm.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
			return "";
		}
		//
		List<List<Barcodes>> MPBarcodesList = new ArrayList<List<Barcodes>>();
		List<PackageUnit> MPPackageUnitList = new ArrayList<PackageUnit>();
		for (int i = 0; i < MPCommodityList.size(); i++) {
			b.setCommodityID(MPCommodityList.get(i).getID());
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> MPCommodityBarcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("Retrieve N Barcodes error code=" + barcodesBO.getLastErrorCode());
				mm.put(BaseAction.JSON_ERROR_KEY, barcodesBO.getLastErrorCode().toString());
				return "";
			}
			MPBarcodesList.add(MPCommodityBarcodeList);
			//
			p.setID(MPCommodityList.get(i).getPackageUnitID());
			DataSourceContextHolder.setDbName(dbName);
			PackageUnit MPPackageUnit = (PackageUnit) packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, p);
			if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.info("Retrieve 1 MPPackageUnit error code=" + packageUnitBO.getLastErrorCode());
				mm.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode().toString());
				return "";
			}
			MPPackageUnitList.add(MPPackageUnit);
		}
		mm.put("warehouseList", warehouseList);
		mm.put("categoryParentList", categoryParentList);
		mm.put("categoryList", categoryList);
		mm.put("MultiPackagePackageUnitList", MPPackageUnitList);
		mm.put("MultiPackageCommodityBarcodeList", MPBarcodesList);
		mm.put("MultiPackageCommodityList", MPCommodityList);
		mm.put("brandList", brandList);
		mm.put("providerList", providerList);
		mm.put("puList", puList);
		mm.put("comm", commodity);
		return COMMODITY_Retrieve1;
	}

	/*
	 * Request Body in Fiddler: pricePurchase=0.04&ID=1 URL:
	 * http://localhost:8080/nbr/commodity/retrieve1Ex.bx
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/retrieve1Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieve1Ex(@ModelAttribute("SpringWeb") Commodity comm, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询一个商品,comm=" + comm);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		do {
			ErrorInfo ecOut = new ErrorInfo();
			Commodity commodity = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(comm.getID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				if (BaseAction.ENV != EnumEnv.DEV) {
					logger.error("Retrieve 1 commodity error code=" + ecOut.getErrorCode() + ",erroeMsg=" + ecOut.getErrorMessage());
				}
				params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage().toString());
				break;
			}

			if (commodity == null) {
				return null;
			}
			//
			commodity.setCreateDatetime(new Date());
			commodity.setUpdateDatetime(new Date());// 从缓存中获取得不到这俩个参数.但pos解析时需要
			commodity.setSyncDatetime(new Date());
			//
			Category category = (Category) CacheManager.getCache(dbName, EnumCacheType.ECT_Category).read1(commodity.getCategoryID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("Retrieve 1 category error code=" + ecOut.getErrorCode() + ",errorMsg=" + ecOut.getErrorMessage());
				params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage().toString());
				break;
			}

			Brand brand = (Brand) CacheManager.getCache(dbName, EnumCacheType.ECT_Brand).read1(commodity.getBrandID(), getStaffFromSession(session).getID(), ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("Retrieve 1 brand error code=" + ecOut.getErrorCode());
				params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage().toString());
				break;
			}

			if (category != null && brand != null) {
				commodity.setBrandName(brand.getName());
				commodity.setCategoryName(category.getName());
			}

			// 加载商品对应的条形码
			Barcodes b = new Barcodes();
			b.setCommodityID(commodity.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询商品对应的多个条形码失败：" + barcodesBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, barcodesBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
				break;
			} else {
				logger.info("查询商品对应的多个条形码成功，barcodeList=" + barcodeList);

				String barcodes = "";
				for (Barcodes bc : barcodeList) {
					barcodes += bc.getBarcode() + " ";
				}
				commodity.setBarcodes(barcodes);
			}

			// 加载商品对应的多包装单位信息,如果不是多包装商品，拿到商品的包装单位去查询
			PackageUnit pu = new PackageUnit();
			pu.setID(commodity.getPackageUnitID());
			DataSourceContextHolder.setDbName(dbName);
			PackageUnit puR1 = (PackageUnit) packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pu);
			if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询包装单位失败：" + packageUnitBO.printErrorInfo());

				params.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, packageUnitBO.getLastErrorMessage());
				break;
			}
			commodity.setPackageUnitName(puR1.getName());

			// 加载商品对应的供应商ID和供应商名称
			ProviderCommodity providerCommodity = new ProviderCommodity();
			providerCommodity.setCommodityID(commodity.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<ProviderCommodity> pcList = (List<ProviderCommodity>) providerCommodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, providerCommodity);
			if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询对应供应商失败：" + providerCommodityBO.printErrorInfo());

				params.put(BaseAction.JSON_ERROR_KEY, providerCommodityBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, providerCommodityBO.getLastErrorMessage());
				break;
			} else {
				logger.info("返回对应供应商成功，pcList=" + pcList);

				String providerIDs = "";
				String providerNames = "";
				boolean hasDBError = false;
				for (ProviderCommodity pc : pcList) {
					providerIDs += pc.getProviderID() + " ";

					Provider p = (Provider) CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).read1(pc.getProviderID(), getStaffFromSession(session).getID(), ecOut, dbName);
					if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
						logger.error("读取一个供应商失败，错误码=" + ecOut.getErrorCode());
						params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
						params.put(BaseAction.KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage().toString());
						hasDBError = true;
						break;
					} else {
						logger.info("读取一个供应商成功，p=" + p.getName() == null ? "" : p.getName());
						providerNames += p.getName() == null ? "" : p.getName() + " ";
					}
				}
				if (hasDBError) {
					break;
				}
				//
				commodity.setProviderIDs(providerIDs);
				commodity.setProviderName(providerNames);
			}

			DataSourceContextHolder.setDbName(dbName);
			List<Commodity> commodityList = (List<Commodity>) commodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.CASE_RetrieveNMultiPackageCommodity, comm);
			if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询多个多包装商品失败：" + commodityBO.printErrorInfo());

				params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, commodityBO.getLastErrorMessage());
				break;
			}
			//
			for (Commodity c : commodityList) {
				// 条形码
				b.setCommodityID(c.getID());
				DataSourceContextHolder.setDbName(dbName);
				List<Barcodes> barcodesList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
				if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询多个条形码失败：" + barcodesBO.printErrorInfo());
					params.put(BaseAction.JSON_ERROR_KEY, barcodesBO.getLastErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
					break;
				}
				String barcodes = "";
				for (Barcodes bc : barcodesList) {
					barcodes += bc.getBarcode() + " ";
				}
				c.setBarcodes(barcodes);
				// 包装单位
				pu.setID(c.getPackageUnitID());
				DataSourceContextHolder.setDbName(dbName);
				PackageUnit packageUnit = (PackageUnit) packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pu);
				if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询包装单位失败：" + packageUnitBO.printErrorInfo());
					params.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode().toString());
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, packageUnitBO.getLastErrorMessage());
					break;
				}
				c.setPackageUnitName(packageUnit.getName());
			}
			params.put(BaseAction.KEY_Object, commodity);
			params.put(BaseAction.KEY_ObjectList, commodityList);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("CommodityAction.retrieve1Ex()返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * Request Body in Fiddler: URL:
	 * http://localhost:8080/nbr/commodity/updatePrice.bx?pricePurchase=0.05&ID=1&
	 * priceRetail=12
	 */
	@RequestMapping(value = "/updatePrice", method = RequestMethod.GET)
	public String updatePrice(@ModelAttribute("SpringWeb") Commodity comm, ModelMap mm, HttpSession session, CommodityBO commodityBO) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改商品价格，comm=" + comm);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		DataSourceContextHolder.setDbName(dbName);
		BaseModel bm = commodityBO.updateObject(getStaffFromSession(session).getID(), BaseBO.CASE_Commodity_UpdatePrice, comm);

		logger.info("Update commodityPrice error code=" + commodityBO.getLastErrorCode());
		switch (commodityBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).delete1(bm);

			return COMMODITY_Index;
		default:
			logger.info("其他错误");

			return COMMODITY_Index;
		}
	}

	/*
	 * Request Body in Fiddler: URL:purchasingUnit=盒子&ID=3
	 * http://localhost:8080/nbr/commodity/updatePurchasingUnit.bx
	 */
	@RequestMapping(value = "/updatePurchasingUnit", method = RequestMethod.POST)
	public String updatePurchasingUnit(@ModelAttribute("SpringWeb") Commodity comm, ModelMap mm, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("修改商品的采购单位，comm=" + comm);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();
		//
		DataSourceContextHolder.setDbName(dbName);
		BaseModel bm = commodityBO.updateObject(getStaffFromSession(session).getID(), BaseBO.CASE_UpdatePurchasingUnit, comm);

		logger.info("Update PurchasingUnit error code=" + commodityBO.getLastErrorCode());
		switch (commodityBO.getLastErrorCode()) {
		case EC_NoError:
			CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).delete1(bm);

			return COMMODITY_Index;
		default:
			logger.info("其他错误");

			return COMMODITY_Index;
		}
	}

	/*
	 * Request Body in Fiddler:
	 * string1=234%2C456%2C789%3B1%2C2%2C3%3B1%2C5%2C10%3B1%2C5%2C10%3B0.8%2C4%2C8%
	 * 3B&name=手表&status=0&shortName=薯片&specification=克&packageUnitID=1&
	 * purchasingUnit=箱&providerID=1&brandID=3&categoryID=1&mnemonicCode=SP&
	 * pricingType=1&
	 * isServiceType=1&priceRetail=12&priceVIP=11&priceWholesale=11&ratioGrossMargin
	 * =0&canChangePrice=1&ruleOfPoint=1&picture=116843434834&shelfLife=3&returnDays
	 * =15&purchaseFlag=20&refCommodityID=0&refCommodityMultiple=0&isGift=0&tag=232x
	 * &NO=11&NOAccumulated=1&type=0&nOStart=-1&purchasingPriceStart=-1&
	 * latestPricePurchase=8&pricePurchase=8 type=0 POST
	 * URL:http://localhost:8080/nbr/commodity/createEx.bx
	 * 
	 * 前端传来的参数顺序：条形码，包装单位，商品倍数，零售价，参考进货价，会员价以及批发价以X,X,X; X,X,X; X,X,X;的格式传到后端
	 */
	// @Transactional
	// @RequestMapping(value = "/createEx", method = RequestMethod.POST)
	// @ResponseBody
	// public String createEx(@ModelAttribute("SpringWeb") Commodity commodity,
	// ModelMap model, HttpServletRequest req) {
	// // 获取条形码，包装单位，商品倍数，零售价，参考进货价，会员价和批发价，拆分成一个个数组
	// String[] sarr = commodity.getString1().split(";");
	// if (sarr.length != MULTI_PACKAGING_ELEMENT_COUNT) {
	// return "";
	// }
	//
	// String[] iaBarcodes = sarr[0].split(",");
	// int[] iaPuID = toIntArray(sarr[1]);
	// int[] iaCommMultiples = toIntArray(sarr[2]);
	// float[] iaPriceRetail = tofloatArray(sarr[3]);
	// float[] iaPricePurchase = tofloatArray(sarr[4]);
	// float[] iaPriceVIP = tofloatArray(sarr[5]);
	// float[] iaPriceWholesale = tofloatArray(sarr[6]);
	// String[] sNames = sarr[7].split(",");
	// if (iaPuID == null || iaBarcodes == null || iaCommMultiples == null ||
	// iaPriceRetail == null || iaPricePurchase == null || iaPriceVIP == null ||
	// iaPriceWholesale == null || sNames == null) {
	// return "";
	// }
	// if (iaBarcodes.length != iaPuID.length || iaCommMultiples.length !=
	// iaBarcodes.length || iaPriceRetail.length != iaBarcodes.length ||
	// iaPricePurchase.length != iaBarcodes.length || iaPriceVIP.length !=
	// iaBarcodes.length
	// || iaPriceWholesale.length != iaBarcodes.length || sNames.length !=
	// iaBarcodes.length) {
	// return "";
	// }
	//
	// ErrorInfo ecOut = new ErrorInfo();
	// ConfigGeneral maxProviderNO = (ConfigGeneral)
	// CacheManager.getCache(dbName,EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.MaxProviderNOPerCommodity,
	// ecOut);
	//
	// String sProviderIDs = GetStringFromRequest(req, "providerIDs",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sProviderIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// return "";
	// }
	// //
	// int[] iArrProviderID = toIntArray(sProviderIDs);
	// if (iArrProviderID == null || iArrProviderID.length >
	// Integer.parseInt(maxProviderNO.getValue()) || iArrProviderID.length < 0) {
	// return "";
	// }
	//
	// // 该action是做“保存”和“保存并新增”两个功能的
	// ChineseToEN cte = new ChineseToEN();// 用于转换字符串为拼音首字母
	// commodity.setMnemonicCode(cte.getAllFirstLetter(commodity.getName()));
	//
	// BaseModel bmCreated = commodityBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, commodity);
	//
	// logger.info("Create commodity error code=" +
	// commodityBO.getLastErrorCode());
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (commodityBO.getLastErrorCode()) {
	// case EC_WrongFormatForInputField:
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_WrongFormatForInputField.toString());
	// break;
	// case EC_Duplicated:
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_Duplicated.toString());
	// break;
	// case EC_NoError:
	// Commodity commCreated = (Commodity) bmCreated;
	// ProviderCommodity pc = new ProviderCommodity();
	// pc.setCommodityID(commCreated.getID());
	// pc.setProviderID(BaseAction.PROVIDER_ID_ToDeleteAll);
	// providerCommodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, pc);
	// if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("ProviderCommodity Delete error");
	// }
	//
	// List<Integer> iArrProviderIDList =
	// GeneralUtil.sortAndDeleteDuplicated(iArrProviderID);
	// for (int iID : iArrProviderIDList) {
	// pc.setCommodityID(commCreated.getID());
	// pc.setProviderID(iID);
	// providerCommodityBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, pc);
	// if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("添加供应商失败！即将回滚DB...");
	// }
	// }
	//
	// ConfigGeneral configGeneralNO = (ConfigGeneral)
	// CacheManager.getCache(dbName,EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.CommodityNOStart,
	// ecOut);
	// ConfigGeneral configGeneralPrice = (ConfigGeneral)
	// CacheManager.getCache(dbName,EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.CommodityPurchasingPriceStart,
	// ecOut);
	// if (commodity.getnOStart() > Integer.parseInt(configGeneralNO.getValue()) &&
	// commodity.getPurchasingPriceStart() >
	// Float.parseFloat(configGeneralPrice.getValue())) {
	// commCreated.setnOStart(commodity.getnOStart());
	// commCreated.setPurchasingPriceStart(commodity.getPurchasingPriceStart());
	//
	// commodityBO.updateObjectEx(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.CASE_UpdateStartValue, commCreated);
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("对期初值入库失败！即将回滚DB...");
	// }
	// }
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Commodity).write1(commCreated);
	//
	// Barcodes b = new Barcodes();
	// b.setCommodityID(commCreated.getID());
	// b.setBarcode(iaBarcodes[0]);
	// barcodesBO.createObject(BaseBO.CURRENT_STAFF.getID(), BaseBO.INVALID_CASE_ID,
	// b);
	//
	// if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("获取条形码失败！即将回滚DB...");
	// }
	//
	// if (iaBarcodes.length > 1) {
	// for (int i = 1; i < iaBarcodes.length; i++) { //
	// 由于前端传来的多包装数组是包括商品本身的，所以从数组的第二个开始。
	// commodity.setName(sNames[i]);
	// commodity.setPackageUnitID(iaPuID[i]);
	// commodity.setType(2);
	// commodity.setRefCommodityID(commCreated.getID());
	// commodity.setPriceRetail(iaPriceRetail[i]);
	// commodity.setPricePurchase(iaPricePurchase[i]);
	// commodity.setRefCommodityMultiple(iaCommMultiples[i]);
	// commodity.setPriceVIP(iaPriceVIP[i]);
	// commodity.setPriceWholesale(iaPriceWholesale[i]);
	// Commodity commodityCreate = (Commodity)
	// commodityBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.CASE_CreateCommodityOfMultiPackaging, commodity);
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("多包装商品创建失败。即将回滚DB...");
	// }
	//
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Commodity).write1(commodity);
	//
	// b.setCommodityID(commodityCreate.getID());
	// b.setBarcode(iaBarcodes[i]);
	// barcodesBO.createObject(BaseBO.CURRENT_STAFF.getID(), BaseBO.INVALID_CASE_ID,
	// b);
	//
	// if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("创建多包装商品的条形码失败！即将回滚DB...");
	// }
	// }
	// }
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// default:
	// logger.info("未知错误！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler:commodity.ID=1(商品状态为2时不能删除) URL:
	 * http://localhost:8080/nbr/commodity/deleteEx.bx
	 */
	// @RequestMapping(value = "/deleteEx", method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteEx(@ModelAttribute("SpringWeb") Commodity commodity,
	// ModelMap model) {
	// commodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, commodity);//
	// ...重构：在删除的SP里，checkDependency(commodity);和做删除单品、多包装商品的操作。checkDependency()函数是否删除有待讨论
	//
	// logger.info("delete commodity error code=" +
	// commodityBO.getLastErrorCode());
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// switch (commodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("删除成功！");
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Commodity).delete1(commodity);
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// case EC_NoSuchData:
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_NoSuchData.toString());
	// break;
	// case EC_BusinessLogicNotDefined:
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// break;
	// default:
	// logger.info("其他错误！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	// /*
	// * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	// * application/x-www-form-urlencoded Request Body in Fiddler: GET :URL:
	// * http://localhost:8080/nbr/commodity/deleteListEx.bx?commListID=2,5
	// */
	// @RequestMapping(value = "/deleteListEx", produces = "plain/text;
	// charset=UTF-8", method = RequestMethod.GET)
	// @ResponseBody
	// public String deleteListEx(@ModelAttribute("SpringWeb") Commodity commodity,
	// ModelMap model, HttpServletRequest req) {
	// logger.info("删除多个商品，commodity=" + commodity.toString());
	//
	// Company company = getCompanyFromSession(req.getSession());
	// String dbName = company.getDbName();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// do {
	// String sCommIDs = GetStringFromRequest(req, "commListID",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// logger.info("sCommIDs=" + sCommIDs);
	// if (sCommIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// return "";
	// }
	//
	// int[] iArrCommID = toIntArray(sCommIDs);
	// if (iArrCommID == null) {
	// return "";
	// }
	// logger.info("iArrCommID=" + iArrCommID);
	//
	// Commodity comm = new Commodity();
	// Boolean hasDBError = false;
	// for (int iID : iArrCommID) {
	// comm.setID(iID);
	// //
	// DataSourceContextHolder.setDbName(dbName);
	// commodityBO.deleteObject(getStaffFromSession(req.getSession()).getID(),
	// BaseBO.CASE_Commodity_DeleteSimple, comm);//
	// ...删除部分成功，仍然可以提示用户已经删除部分成功，不需要rollback。
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// params.put("msg", "选中的商品只有部分删除成功，请重试");
	// hasDBError = true;
	// continue;
	// }
	// }
	//
	// if (hasDBError) {
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_PartSuccess.toString());
	// break;
	// }
	//
	// logger.info("删除成功！");
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// } while (false);
	// logger.info("返回的数据=" + params);
	//
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * URL:http://localhost:8080/nbr/commodity/retrieveN.bx
	 */
	// @RequestMapping(value = "/retrieveN", method = RequestMethod.GET)
	// public String retrieveN(@ModelAttribute("SpringWeb") Commodity commodity,
	// ModelMap mm, HttpSession session) throws IOException {
	// logger.info("读取多个商品,commodity=" + commodity.toString());
	//
	// Company company = getCompanyFromSession(session);
	// String dbName = company.getDbName();
	//
	// commodity.setType(-1); // ...
	// commodity.setStatus(BaseAction.INVALID_STATUS); // ...
	// DataSourceContextHolder.setDbName(dbName);
	// List<?> list =
	// commodityBO.retrieveNObject(getStaffFromSession(session).getID(),
	// BaseBO.INVALID_CASE_ID, commodity);// ...这里不返回多包装信息有没有问题？
	// logger.info("RetrieveN commodity error code=" +
	// commodityBO.getLastErrorCode());
	//
	// switch (commodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// logger.info("list=" + list);
	// mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// return COMMODITY_RetrieveN;
	// default:
	// logger.info("其他错误！");
	// mm.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// return "";// ...
	// }

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded
	 * 
	 * Request URL:
	 * 根据商品类别查询商品：http://localhost:8080/nbr/commodity/retrieveNEx.bx?categoryID=2&
	 * pageIndex=1
	 * 
	 * commodity.providerID=2&commodity.categoryID=0&pageIndex=1&pageSize=1
	 * commodity.providerID=2&commodity.categoryID=0&commodity.name=薯片
	 */
	// FIXME:
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("读取多个商品，commodity=" + commodity);

		Company company = getCompanyFromSession(session);
		String dbName = company.getDbName();

		Map<String, Object> params = getDefaultParamToReturn(true);
		List<CommodityInfo> listCI = new ArrayList<CommodityInfo>();
		do {
			DataSourceContextHolder.setDbName(dbName);
			List<Commodity> commList = (List<Commodity>) commodityBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, commodity);
			logger.info("Retrieve N Commodity error code=" + commodityBO.getLastErrorCode());
			if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				logger.error("查询多个商品失败：" + commodityBO.printErrorInfo());
				params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, commodityBO.getLastErrorMessage());
				break;
			}
			int commodityNO = commodityBO.getTotalRecord();

			ProviderCommodity pc = new ProviderCommodity();
			Barcodes barcodes = new Barcodes();
			CommodityProperty cp = new CommodityProperty();
			boolean bHasDBError = false;
			//
			bHasDBError = setProviderAndBarcodesAndCommodityProperty(params, listCI, commList, pc, barcodes, cp, bHasDBError, dbName, session, commodity);

			if (bHasDBError) {
				break;
			}

			// 单品 //...放for (Commodity comm : commList) {的前面？？？？？？？？？？
			if (commList != null && commList.size() > 0) {

				bHasDBError = setBarcodeAndPackageUnitToCommodityList(params, commList, dbName, session);
				if (bHasDBError) {
					break;
				}
			}
			// 商品门店信息
			for(Commodity commodityRnFromDB : commList) {
				CommodityShopInfo commodityShopInfoRnCondition = new CommodityShopInfo();
				commodityShopInfoRnCondition.setCommodityID(commodityRnFromDB.getID());
				commodityShopInfoRnCondition.setShopID(commodity.getShopID());
				DataSourceContextHolder.setDbName(dbName);
				List<CommodityShopInfo> listCommodityShopInfo = (List<CommodityShopInfo>) commodityShopInfoBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, commodityShopInfoRnCondition);
				if (commodityShopInfoBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("查询商品门店信息对象失败，错误码=" + commodityShopInfoBO.getLastErrorCode());
					params.put(BaseAction.JSON_ERROR_KEY, commodityShopInfoBO.getLastErrorCode().toString());
					params.put(KEY_HTMLTable_Parameter_msg, commodityShopInfoBO.getLastErrorMessage());
					break;
				}
				commodityRnFromDB.setListSlave2(listCommodityShopInfo);
			}
			logger.info(listCI);
			params.put(BaseAction.KEY_ObjectList, listCI);
			params.put(BaseAction.KEY_HTMLTable_Parameter_TotalRecord, commodityNO);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "");
			params.put(BaseAction.KEY_HTMLTable_Parameter_code, "0");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
		} while (false);
		logger.info("CommodityAction.retrieveNEx()返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	@SuppressWarnings("unchecked")
	protected boolean setBarcodeAndPackageUnitToCommodityList(Map<String, Object> params, List<Commodity> commList, String dbName, HttpSession session) {
		logger.info("给商品设置对应的条形码和包装单位");

		Barcodes b = new Barcodes();
		PackageUnit pu = new PackageUnit();
		boolean bHasDBError = false;
		for (Commodity c : commList) {
			// 给商品设置包装单位
			pu.setID(c.getPackageUnitID());
			DataSourceContextHolder.setDbName(dbName);
			PackageUnit packageUnit = (PackageUnit) packageUnitBO.retrieve1Object(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, pu);
			logger.info("Retrieve N Barcodes error code=" + packageUnitBO.getLastErrorCode());
			if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(BaseAction.JSON_ERROR_KEY, packageUnitBO.getLastErrorCode().toString());
				bHasDBError = true;
				break;
			}
			c.setPackageUnitName(packageUnit.getName());
			// 给商品设置条形码
			b.setCommodityID(c.getID());
			DataSourceContextHolder.setDbName(dbName);
			List<Barcodes> barcodeList = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(session).getID(), BaseBO.INVALID_CASE_ID, b);
			logger.info("Retrieve N Barcodes error code=" + barcodesBO.getLastErrorCode());
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(BaseAction.JSON_ERROR_KEY, barcodesBO.getLastErrorCode().toString());
				bHasDBError = true;
				break;
			}
			if (barcodeList.size() == 0) {
				continue;
			}
			//
			String barcodes = "";
			int barcodeID = barcodeList.get(0).getID();
			for (Barcodes bc : barcodeList) {
				barcodes += bc.getBarcode() + " ";
			}
			c.setBarcodes(barcodes);
			c.setBarcodeID(barcodeID);
			c.setSyncDatetime(new Date());
		}
		return bHasDBError;
	}

	/*
	 * Request Body in Fiddler:ID=1&name=手表&status=0& shortName=薯片
	 * &specification=克&packageUnit=盒&purchasingUnit=箱 &providerID=1&brandID=3
	 * &categoryID=1&mnemonicCode=SP&pricingType=1&isServiceType=1
	 * &priceVIP=11.8&priceWholesale=11&RatioGrossMargin=0.05
	 * &canChangePrice=1&ruleOfPoint=1 &picture=url=116843434834&shelfLife=3&
	 * returnDays=15&purchaseFlag=20.1&refCommodityID=0
	 * &refCommodityMultiple=0&isGift=0&Tag=.......... ...x&NO=11&providerIDs=1,2
	 * 
	 * URL:http://localhost:8080/nbr/commodity/updateEx.bx
	 */
	// @SuppressWarnings("unchecked")
	// @Transactional
	// @RequestMapping(value = "/updateEx", produces = "plain/text; charset=UTF-8",
	// method = RequestMethod.POST)
	// @ResponseBody
	// public String updateEx(@ModelAttribute("SpringWeb") Commodity commodity,
	// ModelMap model, HttpServletRequest req) {
	// String[] sarr = commodity.getString1().split(";");
	// if (sarr.length != MULTI_PACKAGING_ELEMENT_COUNT) {// ...7可以考虑定义一个常量在本类内
	// return "";
	// }
	//
	// String[] iaBarcodes = sarr[0].split(",");
	// int[] iaPuID = toIntArray(sarr[1]);
	// int[] iaCommMultiples = toIntArray(sarr[2]);
	// float[] iaPriceRetail = tofloatArray(sarr[3]);
	// float[] iaPricePurchase = tofloatArray(sarr[4]);
	// float[] iaPriceVIP = tofloatArray(sarr[5]);
	// float[] iaPriceWholesale = tofloatArray(sarr[6]);
	// String[] sNames = sarr[7].split(",");
	// if (iaPuID == null || iaBarcodes == null || iaCommMultiples == null ||
	// iaPriceRetail == null || iaPricePurchase == null || iaPriceVIP == null ||
	// iaPriceWholesale == null || sNames == null) {// ...未检查它们的维度是否一致
	// return "";
	// }
	//
	// if (iaBarcodes.length != iaPuID.length || iaCommMultiples.length !=
	// iaBarcodes.length || iaPriceRetail.length != iaBarcodes.length ||
	// iaPricePurchase.length != iaBarcodes.length || iaPriceVIP.length !=
	// iaBarcodes.length
	// || iaPriceWholesale.length != iaBarcodes.length || sNames.length !=
	// iaBarcodes.length) {
	// return "";
	// }
	//
	// if (commodity.getType() == EnumCommodityType.ECT_MultiPackaging.getIndex()) {
	// return "";
	// }
	//
	// ErrorInfo ecOut = new ErrorInfo();
	// ConfigGeneral maxProviderNO = (ConfigGeneral)
	// CacheManager.getCache(dbName,EnumCacheType.ECT_ConfigGeneral).read1(BaseCache.MaxProviderNOPerCommodity,
	// ecOut);
	//
	// String sProviderIDs = GetStringFromRequest(req, "providerIDs",
	// String.valueOf(BaseAction.INVALID_ID)).trim();
	// if (sProviderIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
	// return "";
	// }
	// //
	// int[] iArrProviderID = toIntArray(sProviderIDs);
	// if (iArrProviderID == null || iArrProviderID.length >
	// Integer.parseInt(maxProviderNO.getValue()) || iArrProviderID.length < 0) {
	// return "";
	// }
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// // 判断要修改商品是否为单品
	// Commodity commR1 = (Commodity)
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Commodity).read1(commodity.getID(),
	// ecOut);
	// if (ecOut.getEec() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("Commodity R1 error");
	// }
	// //
	// if (commR1.getType() !=
	// CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
	// return "";
	// }
	//
	// BaseModel bm = commodityBO.updateObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, commodity);
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("Commodity Update error");// ...
	// 对数据库进行错误的操作，这句话需要修改为有意义一些的，而且不能和下面的相同。
	// }
	//
	// commodityBO.updateObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.CASE_UpdateCommodityPrice, commodity);
	// logger.info("Update commodity error code=" +
	// commodityBO.getLastErrorCode());
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("CommodityPrice Update error");// ...
	// 对数据库进行错误的操作，这句话需要修改为有意义一些的，而且不能和下面的相同。
	// }
	//
	// ProviderCommodity pc = new ProviderCommodity();
	// pc.setCommodityID(bm.getID());
	// providerCommodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, pc);
	// if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("ProviderCommodity Delete error");
	// }
	//
	// // 去除iArrProviderID的重复值
	// List<Integer> iArrProviderIDList =
	// GeneralUtil.sortAndDeleteDuplicated(iArrProviderID);
	// for (int iID : iArrProviderIDList) {
	// pc.setCommodityID(bm.getID());
	// pc.setProviderID(iID);
	// providerCommodityBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, pc);
	// if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("添加供应商失败！即将回滚DB...");
	// }
	// }
	//
	// // ...本函数太长，需要简化。
	//
	// // 获取条形码，包装单位，商品倍数，零售价，参考进货价，会员价和批发价,拆分成一个个数组
	// List<PackageUnit> lspu = new ArrayList<PackageUnit>();
	// List<PackageUnit> lspuCreated = new ArrayList<PackageUnit>();
	// List<PackageUnit> lspuUpdated = new ArrayList<PackageUnit>();
	// List<PackageUnit> lspuDeleted = new ArrayList<PackageUnit>();
	// if (isPackageUnitModified(commodity, lspu, lspuCreated, lspuUpdated,
	// lspuDeleted)) {// ...如果返回false，要怎样处理？返回false可能是DB错误，并不一定代表包装单位没有修改过
	// List<Commodity> listMPCommodity = (List<Commodity>)
	// commodityBO.retrieveNObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.CASE_RetrieveNMultiPackageCommodity, commodity);
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// return "";
	// }
	//
	// for (Commodity comm : listMPCommodity) {
	// pc.setCommodityID(comm.getID());
	// pc.setProviderID(BaseAction.PROVIDER_ID_ToDeleteAll);
	// providerCommodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, pc);
	// if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("ProviderCommodity Delete error");
	// }
	//
	// for (int iID : iArrProviderIDList) {
	// pc.setCommodityID(comm.getID());
	// pc.setProviderID(iID);
	// providerCommodityBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, pc);
	// if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("添加供应商失败！即将回滚DB...");
	// }
	// }
	// }
	//
	// // 检查 lspu内部有无重复的副单位。如果有，当黑客行为处理。
	// if (lspu.size() != new HashSet<PackageUnit>(lspu).size()) {
	// return "";
	// }
	//
	// // 检查lspuDeleted中对应的多包装商品有无在其它地方使用，如果有，返回7。
	// for (Object obj : listMPCommodity) {
	// Commodity c = (Commodity) obj;
	// commodityBO.retrieveNObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.CASE_CheckDependency, c);
	//
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError &&
	// commodityBO.getLastErrorCode() == EnumErrorCode.EC_BusinessLogicNotDefined) {
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_BusinessLogicNotDefined.toString());
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();//
	// ...这个时候，并未恢复之前更新过的商品。需要讨论怎样做
	// }
	//
	// // 删除相应多包装商品
	// for (PackageUnit pu : lspuDeleted) {
	// if (c.getPackageUnitID() != pu.getID()) {
	// continue;
	// }
	//
	// commodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, c);
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("Delete MultipackCommodity error!!!");
	// }
	//
	// pc.setCommodityID(c.getID());
	// pc.setProviderID(BaseAction.PROVIDER_ID_ToDeleteAll);
	// providerCommodityBO.deleteObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, pc);
	// if (providerCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("Delete MultipackCommodity provider error!!!");
	// }
	// break;
	// }
	//
	// // 更新对应的多包装商品的相关字段：倍数、零售价、采购价、会员价、批发价。
	// for (PackageUnit pu : lspuUpdated) {
	// if (c.getPackageUnitID() != pu.getID()) {
	// continue;
	// }
	// for (int i = 1; i < iaBarcodes.length; i++) {
	// if (pu.getID() == iaPuID[i]) {
	// c.setName(sNames[i]);
	// c.setRefCommodityMultiple(iaCommMultiples[i]);
	// c.setPriceRetail(iaPriceRetail[i]);
	// c.setPricePurchase(iaPricePurchase[i]);
	// c.setPriceVIP(iaPriceVIP[i]);
	// c.setPriceWholesale(iaPricePurchase[i]);
	//
	// commodityBO.updateObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.CASE_UpdateCommodityOfMultiPackaging, c);
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("Update MultipackCommodity error!!!");
	// }
	//
	// commodityBO.updateObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.CASE_UpdateCommodityPrice, c); // ...
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("Update CommodityPrice error!!!");
	// }
	//
	// Barcodes barcodes = new Barcodes();
	// barcodes.setCommodityID(c.getID());
	//
	// List<?> barcodesList =
	// barcodesBO.retrieveNObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.INVALID_CASE_ID, barcodes);
	// if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("RetrieveN Barcodes error!!!");
	// }
	//
	// for (Object o : barcodesList) {
	// Barcodes b = (Barcodes) o;
	//
	// barcodesBO.deleteObject(BaseBO.CURRENT_STAFF.getID(), BaseBO.INVALID_CASE_ID,
	// b);
	// if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("Delete Barcodes error!!!");
	// }
	// }
	//
	// barcodes.setBarcode(iaBarcodes[i]);
	// barcodes.setCommodityID(c.getID());
	//
	// barcodesBO.createObject(BaseBO.CURRENT_STAFF.getID(), BaseBO.INVALID_CASE_ID,
	// barcodes);
	// if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("Create Barcodes error!!!");
	// }
	//
	// break;
	// }
	// }
	// }
	// }
	//
	// // 创建新增的多包装商品
	// for (PackageUnit pu : lspuCreated) {
	// for (int i = 1; i < iaBarcodes.length; i++) {
	// if (pu.getID() == iaPuID[i]) {
	// commodity.setName(sNames[i]);
	// commodity.setPackageUnitID(iaPuID[i]);
	// commodity.setRefCommodityMultiple(iaCommMultiples[i]);
	// commodity.setPriceRetail(iaPriceRetail[i]);
	// commodity.setPricePurchase(iaPricePurchase[i]);
	// commodity.setPriceVIP(iaPriceVIP[i]);
	// commodity.setPriceWholesale(iaPriceWholesale[i]);
	// commodity.setType(2);
	// commodity.setNO(0);
	//
	// Commodity c = (Commodity)
	// commodityBO.createObject(BaseBO.CURRENT_STAFF.getID(),
	// BaseBO.CASE_CreateCommodityOfMultiPackaging, commodity);
	// if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("Create MultipackCommodity error!!!");
	// }
	//
	// Barcodes b = new Barcodes();
	// b.setBarcode(iaBarcodes[i]);
	// b.setCommodityID(c.getID());
	// barcodesBO.createObject(BaseBO.CURRENT_STAFF.getID(), BaseBO.INVALID_CASE_ID,
	// b);
	// if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
	// throw new RuntimeException("Barcodes create error!!!");
	// }
	// break;
	// }
	// }
	// }
	// } else {
	// throw new RuntimeException(" isPackageUnitModified error!!!");
	// }
	//
	// switch (commodityBO.getLastErrorCode()) {
	// case EC_NoError:
	// //
	// CacheManager.getCache(dbName,EnumCacheType.ECT_Commodity).write1(comm);
	// //
	// // logger.info("更新过后的商品：" + comm); //..
	// params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
	// break;
	// case EC_WrongFormatForInputField:
	// // "", "输入的格式不正确");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_WrongFormatForInputField.toString());
	// break;
	// case EC_NoSuchData:
	// // "", "更新失败，该商品并不存在");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_NoSuchData.toString());
	// break;
	// default:
	// logger.info("其他错误！");
	// params.put(BaseAction.JSON_ERROR_KEY,
	// EnumErrorCode.EC_OtherError.toString());
	// break;
	// }
	// return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	// }

	/*
	 * Request Body in Fiddler: commodity.ID=1
	 * URL:http://localhost:8080/nbr/commodity/retrieveInventory.bx
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/retrieveInventoryEx", method = RequestMethod.GET)
	public String retrieveInventoryEx(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap mm, HttpServletRequest req) {
		logger.info("查询商品的库存，commodity=" + commodity);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		String barcodes = GetStringFromRequest(req, "barcodes", String.valueOf(BaseAction.INVALID_ID)).trim();
		if (barcodes.equals(String.valueOf(BaseAction.INVALID_ID))) {
			return "";// ...
		}

		Barcodes queryConditionBarcode = new Barcodes();
		queryConditionBarcode.setBarcode(barcodes);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<Barcodes> retrieveNObject = (List<Barcodes>) barcodesBO.retrieveNObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, queryConditionBarcode);

		logger.info("Retrieve commodity_NO error code=" + barcodesBO.getLastErrorCode());

		Map<String, Object> params = new HashMap<String, Object>();
		switch (barcodesBO.getLastErrorCode()) {
		case EC_NoError:
			List<BaseModel> list = new ArrayList<BaseModel>();

			boolean dbError = false;
			for (Barcodes barcodes2 : retrieveNObject) {
				commodity.setID(barcodes2.getCommodityID());
				DataSourceContextHolder.setDbName(dbName);
				Commodity commodity2 = (Commodity) commodityBO.retrieve1Object(getStaffFromSession(req.getSession()).getID(), BaseBO.CASE_Commodity_RetrieveInventory, commodity);
				if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError || commodity2 == null) {
					logger.error(commodityBO.printErrorInfo());
					dbError = true;
					break;
				}
				commodity2.setID(barcodes2.getCommodityID());
				commodity2.setBarcodes(barcodes2.getBarcode());// ...
				list.add(commodity2);
			}

			if (dbError) {
				params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode().toString());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
				logger.info("CommodityAction.retrieveInventoryEx()返回的数据=" + params);

				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			} else {
				params.put(KEY_ObjectList, list);
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			}
			break;
		case EC_NoPermission:
			logger.info("没有权限进行操作");

			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoPermission.toString());
			break;
		default:
			logger.info("其他错误！");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			break;
		}
		params.put(BaseAction.KEY_HTMLTable_Parameter_msg, barcodesBO.getLastErrorMessage());
		logger.info("CommodityAction.retrieveInventoryEx()返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();// ...
	}

	/*
	 * Request Body in Fiddler:
	 * name=手表&status=0&shortName=薯片&specification=克&packageUnitID=1&purchasingUnit=
	 * 箱&providerID=1&brandID=3&categoryID=1&mnemonicCode=SP&pricingType=1&
	 * isServiceType=1&latestPricePurchase=8&priceRetail=12&priceVIP=11.8&
	 * priceWholesale=11&RatioGrossMargin=0.05&canChangePrice=1&ruleOfPoint=1&
	 * picture=116843434834&shelfLife=3&
	 * returnDays=15&purchaseFlag=20&refCommodityID=0&refCommodityMultiple=0&isGift=
	 * 0&Tag=121x&NO=11&type=1&nOStart=-1&purchasingPriceStart=-1&commID=1,2
	 * RequestBody in Fiddler:
	 * URL:http://localhost:8080/nbr/commodity/createCombinationCommodityEx.bx
	 */
	// @Transactional
	@RequestMapping(value = "/createCombinationCommodityEx", method = RequestMethod.POST)
	@ResponseBody
	public String createCombinationCommodityEx(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("组合新增商品，commodity=" + commodity);

		String scommIDs = GetStringFromRequest(req, "commID", String.valueOf(BaseAction.INVALID_ID)).trim();
		if (scommIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
			return null;// 黑客行为
		}
		//
		Integer[] iaCommID = GeneralUtil.toIntArray(scommIDs);
		if (iaCommID == null) {
			return null;// 黑客行为
		}

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = new HashMap<String, Object>();
		do {
			// 创建组合商品 组合商品的创建目前根据前端传回来的固定值创建
			DataSourceContextHolder.setDbName(dbName);
			BaseModel bm = commodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, commodity);
			//
			logger.info("Create commodity error code=" + commodityBO.getLastErrorCode());
			if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, commodityBO.getLastErrorMessage());
				break;
			}

			// 创建组合商品的从表
			SubCommodity s = new SubCommodity();
			Boolean hasDBError = false;
			for (int i = 0; i < iaCommID.length; i++) {
				s.setCommodityID(bm.getID());
				s.setSubCommodityID(iaCommID[i]);
				DataSourceContextHolder.setDbName(dbName);
				BaseModel sc = subCommodityBO.createObject(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, s);
				logger.info("Create SubCommodity error code=" + subCommodityBO.getLastErrorCode());
				if (subCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					logger.error("创建组合商品的子商品(子商品ID=" + iaCommID[i] + ")失败，请运营彻底删除（不是修改状态）组合商品(ID=" + bm.getID() + ")");
					params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "创建子商品失败，请修改组合商品名称后重试");
					hasDBError = true;
					break;
				}
				logger.info("子商品创建成功，对象为" + sc);
			}

			if (hasDBError) {
				params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_PartSuccess.toString());
				break;
			}
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_Object, bm);
		} while (false);
		logger.info("CommodityAction.createCombinationCommodityEx()返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/commodity/retrieveN2Ex.bx?commListID=2,5
	 */
	@RequestMapping(value = "/retrieveN2Ex", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveN2Ex(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap model, HttpServletRequest req) {
		if (!canCallCurrentAction(req.getSession(), BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		logger.info("查询多个商品，commodity=" + commodity);

		Company company = getCompanyFromSession(req.getSession());
		String dbName = company.getDbName();

		Map<String, Object> params = new HashMap<String, Object>();
		do {
			String sCommIDs = GetStringFromRequest(req, "commListID", String.valueOf(BaseAction.INVALID_ID)).trim();
			logger.info("sCommIDs=" + sCommIDs);
			if (sCommIDs.equals(String.valueOf(BaseAction.INVALID_ID))) {
				return "";
			}

			Integer[] iArrCommID = GeneralUtil.toIntArray(sCommIDs);
			if (iArrCommID == null) {
				return "";
			}

			boolean hasDBError = false;
			List<Commodity> commList = new ArrayList<Commodity>();
			for (int iID : iArrCommID) {
				Commodity comm = new Commodity();
				comm.setID(iID);
				DataSourceContextHolder.setDbName(dbName);
				List<List<BaseModel>> commodityR1 = commodityBO.retrieve1ObjectEx(getStaffFromSession(req.getSession()).getID(), BaseBO.INVALID_CASE_ID, comm);
				if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					params.put(BaseAction.JSON_ERROR_KEY, commodityBO.getLastErrorCode());
					params.put(KEY_HTMLTable_Parameter_msg, commodityBO.getLastErrorMessage());
					hasDBError = true;
					break;
				}
				comm = Commodity.fetchCommodityFromResultSet(commodityR1);
				commList.add(comm);
			}
			if (hasDBError) {
				break;
			}
			setBarcodeAndPackageUnitToCommodityList(params, commList, dbName, req.getSession());
			if (hasDBError) {
				break;
			}

			params.put("commList", commList);
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError.toString());
			params.put(KEY_HTMLTable_Parameter_msg, commodityBO.getLastErrorMessage());
		} while (false);
		logger.info("CommodityAction.retrieveN2Ex()返回的数据=" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/*
	 * User-Agent: Fiddler Host: localhost:8080 Content-Type:
	 * application/x-www-form-urlencoded Request Body in Fiddler: GET :URL:
	 * http://localhost:8080/nbr/commodity/retrieveNToCheckUniqueFieldEx.bx
	 * name=不重复的商品名称
	 */
	@RequestMapping(value = "/retrieveNToCheckUniqueFieldEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String retrieveNToCheckUniqueFieldEx(@ModelAttribute("SpringWeb") Commodity commodity, ModelMap model, HttpSession session) {
		if (!canCallCurrentAction(session, BaseAction.EnumUserScope.STAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		Company company = getCompanyFromSession(session);
		// 选传参数：ID，必传参数：string1和int1
		// 如果是要进行修改操作,则要传对应的ID参数，不然即使不做修改，也会报已存在该字段的错误信息
		// string1存储的是要检查的字段值，int1如下描述
		// int1=1，代表要检查商品名称

		return doRetrieveNToCheckUniqueFieldEx(false, commodity, company.getDbName(), session);
	}

	@Override
	protected BaseBO getBO() {
		return commodityBO;
	}
}
