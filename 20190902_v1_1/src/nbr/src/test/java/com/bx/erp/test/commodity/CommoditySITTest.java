package com.bx.erp.test.commodity;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseBrandTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class CommoditySITTest extends BaseActionTest {
	private Staff staff;

	public static class DataInput {

		private static Provider providerInput = null;

		// 获取供应商数据
		protected static final Provider getProvider() throws CloneNotSupportedException, InterruptedException {
			if (providerInput == null) {
				providerInput = new Provider();
				providerInput.setName(Shared.getLongestProviderName("阿里"));
				providerInput.setDistrictID(5);
				providerInput.setAddress("山东");
				providerInput.setContactName("king");
				providerInput.setMobile(Shared.getValidStaffPhone());
			} else {
				providerInput.setName("京东" + String.valueOf(System.currentTimeMillis()).substring(5));
				providerInput.setMobile(Shared.getValidStaffPhone());
			}

			return (Provider) providerInput.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();

		try {
			staff = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createAndDeleteCommodity() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建一个新商品类别CategoryA
		Category categoryAData = BaseCategoryParentTest.DataInput.getCategory();
		Map<String, Object> categoryParamsA = categoryAData.getCreateParam(BaseBO.INVALID_CASE_ID, categoryAData);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category categoryA = (Category) categoryMapper.create(categoryParamsA);
		//
		categoryA.setIgnoreIDInComparision(true);
		Assert.assertNotNull(categoryA, "商品类别创建失败");
		if (categoryA.compareTo(categoryAData) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		Assert.assertTrue(categoryA != null && EnumErrorCode.values()[Integer.parseInt(categoryParamsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "商品类别对象创建成功");
		System.out.println("创建的商品类别是：" + categoryA);
		System.out.println("------------------------------- 商品类别A创建成功----------------------------");

		// 创建一个新商品品牌BrandA
		Brand brandAData = BaseBrandTest.DataInput.getBrand();
		Map<String, Object> brandParam = brandAData.getCreateParam(BaseBO.INVALID_CASE_ID, brandAData);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Brand brandA = (Brand) brandMapper.create(brandParam);
		//
		brandA.setIgnoreIDInComparision(true);
		Assert.assertNotNull(brandA, "商品品牌创建失败");
		//
		if (brandA.compareTo(brandAData) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		Assert.assertTrue(brandA != null && EnumErrorCode.values()[Integer.parseInt(brandParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "商品品牌对象创建成功");
		System.out.println("创建的商品品牌为" + brandA);
		System.out.println("-------------------------- 商品品牌A创建成功 ------------------------------");

		// 创建一个新商品供应商ProviderA
		Provider providerAData = DataInput.getProvider();
		Map<String, Object> providerParam = providerAData.getCreateParam(BaseBO.INVALID_CASE_ID, providerAData);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerA = (Provider) providerMapper.create(providerParam);
		//
		providerA.setIgnoreIDInComparision(true);
		Assert.assertNotNull(providerA, "供应商创建失败");
		//
		if (providerA.compareTo(providerAData) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		Assert.assertTrue(providerA != null && EnumErrorCode.values()[Integer.parseInt(brandParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "商品品牌对象创建成功");
		System.out.println("创建的供应商是：" + providerA);
		System.out.println("------------------------- 供应商A创建成功 ------------------");

		// 用以上信息创建一个新商品CommodityA
		Commodity commodityData = BaseCommodityTest.DataInput.getCommodity();
		commodityData.setCategoryID(categoryA.getID());
		commodityData.setBrandID(brandA.getID());
		commodityData.setProviderIDs(String.valueOf(providerA.getID()));
		commodityData.setnOStart(commodityData.getNO());// 当nOStart不等于-1时，把nOStart赋值给NO的(NO=nOStart),如果nOStart等于-1，NO是为0的
		commodityData.setPurchasingPriceStart(100D);
		//
		Map<String, Object> commodityParam = commodityData.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodityData);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(commodityParam);
		//
		Commodity commodity = (Commodity) bmList.get(5).get(0);
		commodity.setIgnoreIDInComparision(true);
		Assert.assertNotNull(commodity, "商品创建失败");
		commodity.setLatestPricePurchase(commodityData.getLatestPricePurchase()); // 为了让compareTo()通过
		if (commodity.compareTo(commodityData) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		Assert.assertTrue(commodity != null && EnumErrorCode.values()[Integer.parseInt(commodityParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "商品创建成功");
		System.out.println("创建的商品是:" + commodity);
		System.out.println("-------------------- 商品A创建成功 ------------------");

		// 创建一个新商品类别CategoryB
		Category categoryData = BaseCategoryParentTest.DataInput.getCategory();
		Map<String, Object> categoryParamsB = categoryData.getCreateParam(BaseBO.INVALID_CASE_ID, categoryData);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category categoryB = (Category) categoryMapper.create(categoryParamsB);
		//
		categoryB.setIgnoreIDInComparision(true);
		Assert.assertNotNull(categoryB, "商品类别创建失败");
		//
		if (categoryB.compareTo(categoryData) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		Assert.assertTrue(categoryB != null && EnumErrorCode.values()[Integer.parseInt(categoryParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "商品类别B对象创建成功");
		System.out.println("新创建的商品类别B的ID是：" + categoryB.getID());
		System.out.println("---------------------- 商品类别B创建成功 ---------------------");

		// 更改CommodityA的类别为CategoryB
		Commodity commodityBData = BaseCommodityTest.DataInput.getCommodity();
		commodityBData.setCategoryID(categoryB.getID());
		commodityBData.setBrandID(brandA.getID());
		commodityBData.setProviderIDs(String.valueOf(providerA.getID()));
		commodityBData.setOperatorStaffID(STAFF_ID3);
		commodityBData.setID(commodity.getID());
		commodityBData.setName("西瓜薯片" + System.currentTimeMillis() % 1000000);
		commodityBData.setnOStart(commodityData.getNO());// 当nOStart不等于-1时，把nOStart赋值给NO的(NO=nOStart),如果nOStart等于-1，NO是为0的
		commodityBData.setPurchasingPriceStart(commodityData.getPurchasingPriceStart());
		//
		Map<String, Object> updateParamsA = commodityBData.getUpdateParam(BaseBO.INVALID_CASE_ID, commodityBData);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityA = (Commodity) commodityMapper.update(updateParamsA);
		//
		commodityBData.setIgnoreIDInComparision(true);
		Assert.assertNotNull(updateCommodityA, "商品更新失败");
		//
		updateCommodityA.setLatestPricePurchase(commodityBData.getLatestPricePurchase()); // 为了让compareTo()通过
		if (commodityBData.compareTo(updateCommodityA) != 0) {
			Assert.assertTrue(false, "更新的对象的字段与DB读出的不相等");
		}
		//
		Assert.assertTrue(updateCommodityA != null && EnumErrorCode.values()[Integer.parseInt(updateParamsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "商品更新类别成功");
		System.out.println("更新成功后的商品是：" + updateCommodityA);
		System.out.println("-------------------- 商品更新商品类别B成功 -----------------------");

		// 创建一个新商品品牌BrandB
		Brand brandBData = BaseBrandTest.DataInput.getBrand();
		Map<String, Object> brandParamB = brandBData.getCreateParam(BaseBO.INVALID_CASE_ID, brandBData);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Brand brandB = (Brand) brandMapper.create(brandParamB);
		//
		brandB.setIgnoreIDInComparision(true);
		Assert.assertNotNull(brandB, "商品品牌B创建失败");
		//
		if (brandB.compareTo(brandBData) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		Assert.assertTrue(brandB != null && EnumErrorCode.values()[Integer.parseInt(brandParamB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "商品品牌B创建成功");
		System.out.println("新创建的商品品牌B的ID是：" + brandB.getID());
		System.out.println("------------------ 商品品牌B创建成功 ------------------------");

		// 更改CommodityA的品牌为BrandB
		Commodity commodityCData = BaseCommodityTest.DataInput.getCommodity();
		commodityCData.setCategoryID(categoryB.getID());
		commodityCData.setBrandID(brandB.getID());
		commodityCData.setProviderIDs(String.valueOf(providerA.getID()));
		commodityCData.setOperatorStaffID(STAFF_ID3);
		commodityCData.setID(commodity.getID());
		commodityCData.setnOStart(commodityData.getNO());// 当nOStart不等于-1时，把nOStart赋值给NO的(NO=nOStart),如果nOStart等于-1，NO是为0的
		commodityCData.setPurchasingPriceStart(100D);
		//
		Map<String, Object> updateParamsB = commodityCData.getUpdateParam(BaseBO.INVALID_CASE_ID, commodityCData);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityB = (Commodity) commodityMapper.update(updateParamsB);
		//
		commodityCData.setIgnoreIDInComparision(true);
		Assert.assertNotNull(updateCommodityB, "更新商品失败");
		//
		updateCommodityB.setLatestPricePurchase(commodityBData.getLatestPricePurchase()); // 为了让compareTo()通过
		if (commodityCData.compareTo(updateCommodityB) != 0) {
			Assert.assertTrue(false, "更新的对象的字段与DB读出的不相等");
		}
		//
		Assert.assertTrue(updateCommodityB != null && EnumErrorCode.values()[Integer.parseInt(updateParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "商品品牌B创建成功");
		System.out.println("更改品牌成功的商品:" + updateCommodityB);
		System.out.println("--------------- 更改商品品牌B成功 --------------------");

		// 创建一个新供应商ProviderB
		Provider providerBData = DataInput.getProvider();
		Map<String, Object> providerParamB = providerBData.getCreateParam(BaseBO.INVALID_CASE_ID, providerBData);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerB = (Provider) providerMapper.create(providerParamB);
		//
		providerBData.setIgnoreIDInComparision(true);
		Assert.assertNotNull(providerB, "创建新供应商B失败");
		//
		if (providerBData.compareTo(providerB) != 0) {
			Assert.assertTrue(false, "创建供应商B失败");
		}
		//
		Assert.assertTrue(providerB != null && EnumErrorCode.values()[Integer.parseInt(providerParamB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建供应商B成功！");
		System.out.println("新供应商B为：" + providerB);
		System.out.println("-------------------- 创建新供应商B成功 ---------------------");

		// 更改CommodityA的供应商为ProviderB
		Commodity commodityDData = BaseCommodityTest.DataInput.getCommodity();
		commodityDData.setCategoryID(categoryB.getID());
		commodityDData.setBrandID(brandB.getID());
		commodityDData.setProviderIDs(String.valueOf(providerB.getID()));
		commodityDData.setOperatorStaffID(STAFF_ID3);
		commodityDData.setID(commodity.getID());
		commodityDData.setnOStart(commodityData.getNO());// 当nOStart不等于-1时，把nOStart赋值给NO的(NO=nOStart),如果nOStart等于-1，NO是为0的
		commodityDData.setPurchasingPriceStart(100D);
		//
		Map<String, Object> updateParamsC = commodityDData.getUpdateParam(BaseBO.INVALID_CASE_ID, commodityDData);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityC = (Commodity) commodityMapper.update(updateParamsC);
		//
		commodityDData.setIgnoreIDInComparision(true);
		Assert.assertNotNull(updateCommodityC, "更新商品失败");
		//
		updateCommodityC.setLatestPricePurchase(commodityBData.getLatestPricePurchase()); // 为了让compareTo()通过
		if (commodityDData.compareTo(updateCommodityC) != 0) {
			Assert.assertTrue(false, "商品更新供应商失败");
		}
		//
		Assert.assertTrue(updateCommodityC != null && EnumErrorCode.values()[Integer.parseInt(updateParamsC.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "更改新供应商成功");
		System.out.println("更改供应商成功的商品：" + updateCommodityC);
		System.out.println("--------------------- 更改商品供应商成功 --------------------");

		// 删除CommodityA
		commodityParam.put("F_ID", commodity.getID());
		Commodity cd = new Commodity();
		cd.setID(commodity.getID());
		//
		Map<String, Object> paramForDelete = cd.getDeleteParam(BaseBO.INVALID_CASE_ID, cd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity delete = (Commodity) commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(delete == null && EnumErrorCode.values()[Integer.parseInt(commodityParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除商品成功！");
		System.out.println("商品删除成功：" + delete);
		System.out.println("------------------------ 删除商品成功 -----------------------");

		// 运行本测试至少2次（不能修改任何代码！），看看能否成功创建商品。如不能，修改代码令其能
		// ...
	}

	/**
	 * 测试目的：测试缓存的对象个数限制是否起作用
	 */
	@Test
	public void testCacheSize() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 1）清空商品缓存。
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).deleteAll();

		// 2）读取N个不同的商品对象，N大于配置中的个数Q。
		System.out.println("-----------:" + CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).readN(false, false).size());
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		ccs.setValue("10");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, staff.getID());

		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).readN(false, false));

		for (int i = 1; i <= 20; i++) {
			ErrorInfo ecOut = new ErrorInfo();
			Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(i, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false);
			}
			System.out.println(comm.getID());
		}

		System.out.println("-----------:" + CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).readN(false, false).size());
		// 因为设置缓存上限为10，所以顺序插入20个商品的时候，前10个ID会被删除掉，则list里面ID从11开始，size为10.
		List<BaseModel> commList = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).readN(false, false);
		assertTrue(commList.size() == 10, "没有正确删除");

		// 恢复缓存的值
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		ccs.setValue("100");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, staff.getID());
	}
}
