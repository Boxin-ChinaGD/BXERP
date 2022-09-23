package com.bx.erp.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
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

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheField;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.JsonUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/cache")
@Scope("prototype")
public class CacheAction extends BaseAction {
	private Log logger = LogFactory.getLog(CacheAction.class);

	@Resource
	protected CompanyBO companyBO;

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView update(ModelMap mm, HttpServletRequest request, HttpServletResponse response) throws CloneNotSupportedException {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		//
		List<BaseModel> bmList = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(true, false);
		if (CollectionUtils.isEmpty(bmList)) {
			mm.put(BaseAction.JSON_ERROR_KEY, "获取所有公司失败");
			return new ModelAndView(CACHE_Update);
		}
		// 通过session获取数据
		HttpSession session = request.getSession(true);
		Enumeration<String> allSession = session.getAttributeNames();
		Map<String, Object> sessionMap = new HashMap<>();
		while (allSession.hasMoreElements()) {
			String name = allSession.nextElement().toString();
			//
			if (name.equals(EnumSession.SESSION_Staff.getName())) {
				Staff s = (Staff) session.getAttribute(name);
				s = (Staff) s.clone();
				s.clearSensitiveInfo();
				sessionMap.put(EnumSession.SESSION_Staff.getName(), s);
				continue;
			}
			if (name.equals(EnumSession.SESSION_Company.getName())) {
				Company c = (Company) session.getAttribute(name);
				c = (Company) c.clone();
				c.setKey("");
				c.setDbUserPassword("");
				c.setDbUserName("");
				sessionMap.put(EnumSession.SESSION_Company.getName(), c);
				continue;
			}
			//
			if (session.getAttribute(name) instanceof BaseModel) {
				BaseModel bm = (BaseModel) session.getAttribute(name);
				sessionMap.put(name, bm.clone());
			} else {
				sessionMap.put(name, session.getAttribute(name));
			}
		}

		mm.put("enumTpye", EnumCacheType.values());
		mm.put("company", bmList);
		mm.put("session", sessionMap);
		mm.put("CacheField", new CacheField());
		return new ModelAndView(CACHE_Update);
	}

	/*
	 * URL:http://localhost:8080/cache.bx?dbName=nbr&whetherRetrieveAll=1&cacheType=ECT_Brand whetherRetrieveAll =
	 * 0：某个公司下的查询所有类型的缓存)；whetherRetrieveAll=1 查询某个公司下的所有缓存。 cacheType 传递查询的缓存的类型
	 * BX二号分公司的数据库目前为公有数据库，只有ECT_Company；ECT_BxStaff；ECT_BXConfigGeneral类型的缓存。
	 */
	@RequestMapping(value = "/retrieveNEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveNEx(@ModelAttribute("SpringWeb") Company company, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		 
		//
		logger.info("读取公司缓存：Company:" + company);
		Map<String, Object> params = new HashMap<String, Object>();
		//
		if (company.getWhetherRetrieveAll() == EnumBoolean.EB_Yes.getIndex()) {
			List<List<BaseModel>> oneCacheTypeList = new ArrayList<>();
			try {
				getOneCacheTypeList(company, oneCacheTypeList);
			} catch (Exception e) {
				logger.info("获取" + company.getDbName() + "公司的缓存" + company.getCacheType() + "失败,错误信息:" + e.getMessage());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "获取" + company.getDbName() + "公司的缓存" + company.getCacheType() + "失败!!!");
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
			params.put(BaseAction.KEY_ObjectList, oneCacheTypeList);
		} else {
			List<List<BaseModel>> allCacheTypeLists = new ArrayList<List<BaseModel>>();
			try {
				getAllCacheTypeLists(company, allCacheTypeLists);
			} catch (Exception e) {
				logger.info("获取" + company.getDbName() + "公司的所有缓存失败,错误信息:" + e.getMessage());
				params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "获取" + company.getDbName() + "公司的所有缓存失败!");
				return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
			}
			params.put(BaseAction.KEY_ObjectList, allCacheTypeLists);
		}
		logger.info("返回的数据：" + params);

		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}

	/** 获取某个公司下的某个类型的缓存 */
	private void getOneCacheTypeList(Company company, List<List<BaseModel>> oneCacheTypeList) {
		List<BaseModel> cacheList = CacheManager.getCache(company.getDbName(), Enum.valueOf(EnumCacheType.class, company.getCacheType())).readN(true, false);
		oneCacheTypeList.add(cacheList);
	}

	/** 获取某个公司下的所有类型的缓存 */
	private void getAllCacheTypeLists(Company company, List<List<BaseModel>> allCacheTypeLists) {
		if (company.getDbName().equals(BaseAction.DBName_Public)) {
			List<BaseModel> companyList = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(true, false);
			List<BaseModel> bxStaffCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_BxStaff).readN(true, false);
			List<BaseModel> bXConfigGeneralCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_BXConfigGeneral).readN(true, false);
			allCacheTypeLists.add(companyList);
			allCacheTypeLists.add(bxStaffCacheList);
			allCacheTypeLists.add(bXConfigGeneralCacheList);
			return;
		}
		//
		List<BaseModel> commodityCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Commodity).readN(true, false);
		List<BaseModel> promotionCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Promotion).readN(true, false);
		List<BaseModel> categoryCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Category).readN(true, false);
		List<BaseModel> brandCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Brand).readN(true, false);
		List<BaseModel> staffCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Staff).readN(true, false);
		List<BaseModel> vipCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Vip).readN(true, false);
		List<BaseModel> posCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_POS).readN(true, false);
		List<BaseModel> configGeneralCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_ConfigGeneral).readN(true, false);
		List<BaseModel> vipCategoryCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_VipCategory).readN(true, false);
		List<BaseModel> inventorySheet = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_InventorySheet).readN(true, false);
		List<BaseModel> providerCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Provider).readN(true, false);
		List<BaseModel> providerDistrictCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_ProviderDistrict).readN(true, false);
		List<BaseModel> purchasingOrderCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_PurchasingOrder).readN(true, false);
		List<BaseModel> warehouseCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Warehouse).readN(true, false);
		List<BaseModel> warehousingCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Warehousing).readN(true, false);
		List<BaseModel> smallSheetCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_SmallSheet).readN(true, false);
		List<BaseModel> staffPermissionCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_StaffPermission).readN(true, false);
		List<BaseModel> categoryParentCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_CategoryParent).readN(true, false);
		List<BaseModel> barcodesCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Barcodes).readN(true, false);
		List<BaseModel> retailTradePromotingCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_RetailTradePromoting).readN(true, false);
		List<BaseModel> shopCacheList = CacheManager.getCache(company.getDbName(), EnumCacheType.ECT_Shop).readN(true, false);
		//
		allCacheTypeLists.add(commodityCacheList);
		allCacheTypeLists.add(promotionCacheList);
		allCacheTypeLists.add(categoryCacheList);
		allCacheTypeLists.add(brandCacheList);
		allCacheTypeLists.add(staffCacheList);
		allCacheTypeLists.add(vipCacheList);
		allCacheTypeLists.add(posCacheList);
		allCacheTypeLists.add(configGeneralCacheList);
		allCacheTypeLists.add(vipCategoryCacheList);
		allCacheTypeLists.add(inventorySheet);
		allCacheTypeLists.add(providerCacheList);
		allCacheTypeLists.add(providerDistrictCacheList);
		allCacheTypeLists.add(purchasingOrderCacheList);
		allCacheTypeLists.add(warehouseCacheList);
		allCacheTypeLists.add(warehousingCacheList);
		allCacheTypeLists.add(smallSheetCacheList);
		allCacheTypeLists.add(staffPermissionCacheList);
		allCacheTypeLists.add(categoryParentCacheList);
		allCacheTypeLists.add(barcodesCacheList);
		allCacheTypeLists.add(retailTradePromotingCacheList);
		allCacheTypeLists.add(shopCacheList);
	}

	@RequestMapping(value = "/updateCacheEx", produces = "plain/text; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public String updateCacheEx(@ModelAttribute("SpringWeb") Company company, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		if (!canCallCurrentAction(request.getSession(), BaseAction.EnumUserScope.BXSTAFF.getIndex())) {
			logger.debug("无权访问本Action");
			return null;
		}
		
		//
		Map<String, Object> params = new HashMap<String, Object>();
		BaseModel bm = new BaseModel();
		bm.setID(company.getCacheID());
		try {
			// 1、根据缓存类型和ID删除缓存列表里的对应的某一个缓存
			CacheManager.getCache(company.getDbName(), Enum.valueOf(EnumCacheType.class, company.getCacheType())).delete1(bm);
		} catch (Exception e) {
			logger.info("删除" + company.getDbName() + "公司的" + "ID为" + company.getCacheID() + "缓存" + company.getCacheType() + "失败,错误信息:" + e.getMessage());
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "删除" + company.getDbName() + "公司的" + "ID为" + company.getCacheID() + " 缓存" + company.getCacheType() + "失败!!!");
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		// 删除后重新加载删除对象的缓存，这时会从数据库中查询对象
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bmCache = CacheManager.getCache(company.getDbName(), Enum.valueOf(EnumCacheType.class, company.getCacheType())).read1(bm.getID(), BaseBO.SYSTEM, ecOut, company.getDbName());
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			logger.info("Retrieve 1  error code=" + ecOut.getErrorCode() + ",errorMsg=" + ecOut.getErrorMessage());
			params.put(BaseAction.JSON_ERROR_KEY, ecOut.getErrorCode().toString());
			params.put(KEY_HTMLTable_Parameter_msg, ecOut.getErrorMessage().toString());
			return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
		}
		// 删除后需要重新放入缓存
		if (bmCache != null) {
			CacheManager.getCache(company.getDbName(), Enum.valueOf(EnumCacheType.class, company.getCacheType())).write1(bmCache, company.getDbName(), BaseBO.SYSTEM);
		}
		params.put(BaseAction.KEY_Object, bmCache);
		params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_NoError);
		params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "删除" + company.getDbName() + "公司的" + "ID为" + company.getCacheID() + " 缓存" + company.getCacheType() + "成功");
		return JSONObject.fromObject(params, JsonUtil.jsonConfig).toString();
	}
}
