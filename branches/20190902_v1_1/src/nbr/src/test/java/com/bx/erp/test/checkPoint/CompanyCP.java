package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.test.Shared;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Company;
import com.bx.erp.util.DataSourceContextHolder;
import net.sf.json.JSONObject;

@WebAppConfiguration
public class CompanyCP {
	/** 1、检查公司A的普通缓存是否创建。 2、检查公司A的私有DB缓存是否正确加载。(所有的普通缓存)
	 * 3、检查公有DB数据库T_Company，查看公司A是否创建，数据是否正确。 4、检查公司A的私有DB是否创建。
	 * 5、检查公司A私有DB数据库T_Staff，查看售前帐号和老板帐号是否创建。 6、检查公司A私有DB数据库T_Shop，查看默认门店是否创建。 */
	@SuppressWarnings("unchecked")
	public static void verifyCreate(MvcResult mr, Company company, CompanyBO companyBO, StaffBO staffBO, ShopBO shopBO, String dbName) throws Exception {
		Company companyClone = (Company) company.clone();
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Company company2 = new Company();
		company2 = (Company) company2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(company2 != null, "解析异常");
		// 1、检查公司A的普通缓存是否创建。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(Shared.BXDBName_Test, EnumCacheType.ECT_Company).read1(company2.getID(), BaseBO.SYSTEM, ecOut, Shared.BXDBName_Test);
		Assert.assertTrue(bm != null, "普通缓存不存在创建出来的公司");
		// 2、检查公司A的私有DB缓存是否正确加载。(所有的普通缓存)
		loadCache(company2);
		// 3、检查公有DB数据库T_Company，查看公司A是否创建，数据是否正确。
		String stringTest = "";
		companyClone.setBusinessLicensePicture(stringTest);
		company2.setBusinessLicensePicture(stringTest);
		company2.setLogo(stringTest);
		companyClone.setLogo(stringTest);
		companyClone.setKey(stringTest);
		companyClone.setDbUserName(stringTest);
		companyClone.setDbUserPassword(stringTest);
		companyClone.setIgnoreIDInComparision(true);
		Assert.assertTrue(companyClone.compareTo(company2) == 0, "创建公司数据异常");
		// 4、检查公司A的私有DB是否创建。
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		Company company3 = (Company) companyBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, company2);
		if (companyBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询一个员工角色失败，错误码=" + companyBO.getLastErrorCode() + "，错误信息=" + companyBO.getLastErrorMessage());
		}
		assertTrue(company3 != null, "DB不存在创建出来的公司");
		// 5、检查公司A私有DB数据库T_Staff，查看售前帐号和老板帐号是否创建。
		Staff staff = new Staff();
		staff.setOperator(EnumBoolean.EB_Yes.getIndex());
		DataSourceContextHolder.setDbName(company2.getDbName());
		List<Staff> staffList = (List<Staff>) staffBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, staff);
		if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询一个员工失败，错误码=" + staffBO.getLastErrorCode() + "，错误信息=" + staffBO.getLastErrorMessage());
		}
		int roles = 0;
		for (Staff staff2 : staffList) {
			if (staff2.getRoleID() == Role.EnumTypeRole.ETR_PreSale.getIndex()) {
				roles++;
			} else if (staff2.getRoleID() == Role.EnumTypeRole.ETR_Boss.getIndex()) {
				roles++;
			}
		}
		assertTrue(staffList.size() == 2 && roles == 2, "员工创建异常");
		// 6、检查公司A私有DB数据库T_Shop，查看虚拟总部和默认门店是否创建。
		DataSourceContextHolder.setDbName(company2.getDbName());
		Shop shop = new Shop();
		List<?> shopList = shopBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, shop);
		if (shopBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询门店信息失败，错误码=" + shopBO.getLastErrorCode() + "，错误信息=" + shopBO.getLastErrorMessage());
		}
		assertTrue(shopList != null && shopList.size() == 2 && //ShopeName_Default
				BaseAction.ShopeName_Default.equals(((Shop) shopList.get(0)).getName()) && //
				BaseAction.ShopeName_Invented.equals(((Shop) shopList.get(1)).getName()));
	}

	/** 1、检查公司A的普通缓存是否修改。 2、检查公有DB数据库T_Company，查看公司A是否正确修改。 */
	public static void verifyUpdate(MvcResult mr, Company company, String dbName) throws Exception {
		Company companyClone = (Company) company.clone();
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Company company2 = new Company();
		company2 = (Company) company2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(company2 != null, "解析异常");
		// 1、检查公司A的普通缓存是否修改。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(Shared.BXDBName_Test, EnumCacheType.ECT_Company).read1(company2.getID(), BaseBO.SYSTEM, ecOut, Shared.BXDBName_Test);
		Assert.assertTrue(bm != null, "普通缓存不存在修改的公司");
		// 2、检查公有DB数据库T_Company，查看公司A是否正确修改。
		// 由于验证修改信息中未修改以下字段，或者返回字段为敏感信息，特设置值为空串
		String stringTest = "";
		companyClone.setBusinessLicensePicture(stringTest);
		company2.setBusinessLicensePicture(stringTest);
		companyClone.setKey(stringTest);
		companyClone.setDbUserName(stringTest);
		companyClone.setDbUserPassword(stringTest);
		companyClone.setSubmchid(stringTest);
		company2.setSubmchid(stringTest);
		companyClone.setBossPassword(stringTest);
		company2.setBossPassword(stringTest);
		companyClone.setDbName(stringTest);
		company2.setDbName(stringTest);
		companyClone.setLogo(stringTest);
		company2.setLogo(stringTest);

		Assert.assertTrue(companyClone.compareTo(company2) == 0, "修改公司数据异常");
	}

	/** 1、检查公有DB数据库T_Company，查看公司A的F_BusinessLicensePicture是否正确修改。
	 * 2、检查本地文件夹，查看公司A营业执照是否上传。 */
	public static void verifyUploadBusinessLicensePicture(MvcResult mr, Company company) throws Exception {
		// 正则匹配最后一个/ 后面的内容
		String pattern = "[^/]+(?!.*/)";
		Pattern r = Pattern.compile(pattern);
		// 1、检查公有DB数据库T_Company，查看公司A的F_BusinessLicensePicture是否正确修改。
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Company company2 = new Company();
		company2 = (Company) company2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(company2 != null, "解析异常");
		//
		String pictures = company2.getBusinessLicensePicture();
		if (!"".equals(pictures)) {
			String pictureType = pictures.substring(pictures.length() - 3, pictures.length());
			String BusinessLicensePictureName = "";
			String JPGType = "jpg";
			if (pictureType.equals(JPGType)) {
				BusinessLicensePictureName = ".jpg";
			} else {
				BusinessLicensePictureName = ".png";
			}
			String companyBusinessLicensePictureDestination = company2.getDbName() + BusinessLicensePictureName;
			String companyBusinessLicensePictureDestinationDB = "";
			Matcher m1 = r.matcher(company2.getBusinessLicensePicture());
			while (m1.find()) {
				companyBusinessLicensePictureDestinationDB = m1.group();
			}
			Assert.assertTrue(companyBusinessLicensePictureDestinationDB.equals(companyBusinessLicensePictureDestination));
			// 2、检查本地文件夹，查看公司A营业执照是否上传。
			// String string = company2.getBusinessLicensePicture();
			Matcher m2 = r.matcher(pictures);// 匹配最后一个/后面的内容
			File file = null;
			String defaultFile = "D:\\nbr\\pic\\common_db\\license\\";
			while (m2.find()) {
				file = new File(defaultFile + m2.group());
			}
			Assert.assertTrue(file.exists(), "本地文件夹不存在公司的营业执照");
		}
	}

	/** 检查缓存是否正常加载 */
	public static void loadCache(Company company) {
		String dbName = company.getDbName();
		List<BaseModel> bmConfigGeneralList = CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigGeneral).readN(false, false);
		Assert.assertTrue(bmConfigGeneralList.size() != 0, "线程配置数据异常");
		// Assert.assertTrue(bmConfigGeneralList.size() == 24);// 检查格式
		List<BaseModel> bmConfigCacheSizeList = CacheManager.getCache(dbName, EnumCacheType.ECT_ConfigCacheSize).readN(false, false);
		for (int i = 1; i < bmConfigCacheSizeList.size(); i++) {
			Assert.assertTrue(Integer.parseInt(((ConfigCacheSize) bmConfigCacheSizeList.get(i)).getValue()) > 0, "ConfigCacheSize数据异常");// 检查值都要大于0
		}
		List<BaseModel> bmBrandList = CacheManager.getCache(dbName, EnumCacheType.ECT_Brand).readN(false, false);
		Assert.assertTrue(bmBrandList != null, "品牌数据异常");
		//
		List<BaseModel> bmBrandSyncList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_BrandSyncInfo).readN(false, false);
		Assert.assertTrue(bmBrandSyncList != null, "品牌同存数据异常");
		//
		List<BaseModel> bmCategoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).readN(false, false);
		Assert.assertTrue(bmCategoryList.size() > 0, "类别数据异常");
		//
		List<BaseModel> bmCategoryParentList = CacheManager.getCache(dbName, EnumCacheType.ECT_CategoryParent).readN(false, false);
		Assert.assertTrue(bmCategoryParentList.size() > 0, "类别大类数据异常");
		//
		List<BaseModel> bmCategorySyncList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_CategorySyncInfo).readN(false, false);
		Assert.assertTrue(bmCategorySyncList != null, "类别同存数据异常");
		//
		List<BaseModel> bmCommoditySyncList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_CommoditySyncInfo).readN(false, false);
		Assert.assertTrue(bmCommoditySyncList != null, "商品同存数据异常");
		//
		List<BaseModel> bmCommodityList = CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).readN(false, false);
		Assert.assertTrue(bmCommodityList != null, "商品数据异常");
		//
		List<BaseModel> bmProviderList = CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).readN(false, false);
		Assert.assertTrue(bmProviderList != null, "供应商数据异常");
		//
		List<BaseModel> bmProviderDistrictList = CacheManager.getCache(dbName, EnumCacheType.ECT_ProviderDistrict).readN(false, false);
		Assert.assertTrue(bmProviderDistrictList != null, "区域数据异常");
		//
		List<BaseModel> bmPurchasingOrderList = CacheManager.getCache(dbName, EnumCacheType.ECT_PurchasingOrder).readN(false, false);
		Assert.assertTrue(bmPurchasingOrderList != null, "采购订单数据异常");
		//
		List<BaseModel> bmPromotionList = CacheManager.getCache(dbName, EnumCacheType.ECT_Promotion).readN(false, false);
		Assert.assertTrue(bmPromotionList != null, "促销数据异常");
		//
		List<BaseModel> bmPromotionSyncList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_PromotionSyncInfo).readN(false, false);
		Assert.assertTrue(bmPromotionSyncList != null, "促销同存数据异常");
		//
		List<BaseModel> bmVipList = CacheManager.getCache(dbName, EnumCacheType.ECT_Vip).readN(false, false);
		Assert.assertTrue(bmVipList != null, "会员数据异常");
		//
		List<BaseModel> bmVipCategoryList = CacheManager.getCache(dbName, EnumCacheType.ECT_VipCategory).readN(false, false);
		Assert.assertTrue(bmVipCategoryList.size() > 0, "会员分类数据异常");
		//
		List<BaseModel> bmWarehouseList = CacheManager.getCache(dbName, EnumCacheType.ECT_Warehouse).readN(false, false);
		Assert.assertTrue(bmWarehouseList != null, "仓库数据异常");
		//
		List<BaseModel> bmWarehousingList = CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).readN(false, false);
		Assert.assertTrue(bmWarehousingList != null, "入库订单数据异常");
		//
		List<BaseModel> bmBarcodesList = CacheManager.getCache(dbName, EnumCacheType.ECT_Barcodes).readN(false, false);
		Assert.assertTrue(bmBarcodesList != null, "条形码数据异常");
		//
		List<BaseModel> bmSyncBarcodesList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_BarcodesSyncInfo).readN(false, false);
		Assert.assertTrue(bmSyncBarcodesList != null, "条形码同存数据异常");
		//
		List<BaseModel> bmInventorySheetList = CacheManager.getCache(dbName, EnumCacheType.ECT_InventorySheet).readN(false, false);
		Assert.assertTrue(bmInventorySheetList != null, "盘点单数据异常");
		//
		List<BaseModel> bmPOSList = CacheManager.getCache(dbName, EnumCacheType.ECT_POS).readN(false, false);
		Assert.assertTrue(bmPOSList != null, "POS机数据异常");
		//
		List<BaseModel> bmSyncPOSList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_PosSyncInfo).readN(false, false);
		Assert.assertTrue(bmSyncPOSList != null, "POS机同存数据异常");
		//
		List<BaseModel> bmRetailTradePromotingList = CacheManager.getCache(dbName, EnumCacheType.ECT_RetailTradePromoting).readN(false, false);
		Assert.assertTrue(bmRetailTradePromotingList != null, "零售单促销数据异常");
		//
		List<BaseModel> bmShopList = CacheManager.getCache(dbName, EnumCacheType.ECT_Shop).readN(false, false);
		Assert.assertTrue(bmShopList != null, "门店数据异常");
		//
		List<BaseModel> bmSmallSheetList = CacheManager.getCache(dbName, EnumCacheType.ECT_SmallSheet).readN(false, false);
		Assert.assertTrue(bmSmallSheetList != null, "小票格式数据异常");
		//
		List<BaseModel> bmStaffList = CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).readN(false, false);
		Assert.assertTrue(bmStaffList != null, "员工数据异常");
		//
		List<BaseModel> bmStaffPermissionList = CacheManager.getCache(dbName, EnumCacheType.ECT_StaffPermission).readN(false, false);
		Assert.assertTrue(bmStaffPermissionList.size() > 0, "员工权限数据异常");
		//
		List<BaseModel> bmStaffBelongingList = CacheManager.getCache(dbName, EnumCacheType.ECT_StaffBelonging).readN(false, false);
		Assert.assertTrue(bmStaffBelongingList != null, "员工归属数据异常");
		//
		List<BaseModel> bmVipCardList = CacheManager.getCache(dbName, EnumCacheType.ECT_VipCard).readN(false, false);
		Assert.assertTrue(bmVipCardList != null, "Vip数据异常");
	}
}
