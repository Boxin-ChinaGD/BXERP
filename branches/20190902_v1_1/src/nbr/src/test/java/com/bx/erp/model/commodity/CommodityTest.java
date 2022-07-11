package com.bx.erp.model.commodity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModelTest;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

//...本类中的用于case ID  的 -1，换成常量
@WebAppConfiguration
public class CommodityTest extends BaseModelTest {
	@Override
	protected BaseModel getMasterTableObject() throws CloneNotSupportedException, InterruptedException {
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commodity.setListSlave1(null);
		return commodity;
	}

	@Override
	protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
		master = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		master.setListSlave1(null);
		return master;
	}

	@Override
	protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException {
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		//
		SubCommodity sc1 = BaseCommodityTest.DataInput.getSubCommodity();
		SubCommodity sc2 = BaseCommodityTest.DataInput.getSubCommodity();
		sc2.setSubCommodityID(2);// 这里需要使两个从表对象的商品ID不同
		//
		bmList.add(sc1);
		bmList.add(sc2);
		return bmList;
	}

	@Override
	protected List<BaseModel> updateSlaveTableObject(List<BaseModel> slave) {
		slave.remove(0);
		return slave;
	}

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		// 由于商品的测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
//		ConfigCacheSize ccs = new ConfigCacheSize();
//		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
//		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
//		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
//		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, BaseTestNGSpringContextTest.STAFF_ID4);
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();

		// 由于商品的测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
//		ConfigCacheSize ccs = new ConfigCacheSize();
//		ccs.setValue("100");
//		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
//		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
//		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, BaseTestNGSpringContextTest.STAFF_ID4);
	}

	public Commodity getCommodity() {
		Commodity c = new Commodity();
		c.setID(1);
		c.setMnemonicCode("xbk");
		c.setTag("123456");
		c.setShortName("饮料");
		c.setnOStart(-1);
		c.setPurchasingPriceStart(-1D);
		c.setName("星巴克");
		c.setStatus(0);
		c.setSpecification("瓶");
		c.setPackageUnitID(1);
		c.setBrandID(1);
		// c.setPricingType(1);
		// c.setCanChangePrice(1);
		c.setCategoryID(1);
		c.setPriceVIP(11.8F);
		c.setPriceRetail(11);
		c.setPriceWholesale(11F);
		c.setNO(100);
		c.setShelfLife(3);
		c.setBarcodes("132354564");
		c.setCanChangePrice(1);
		// c.setRuleOfPoint(1);
		// c.setPurchaseFlag(20);
		c.setReturnDays(30);
		c.setType(EnumCommodityType.ECT_Normal.getIndex());
		c.setOperatorStaffID(1);
		c.setPropertyValue1("123456###@@@");
		c.setPropertyValue2("12345[6###@@@");
		c.setPropertyValue3("12345''6###@@@");
		c.setPropertyValue4("123;;456###@@@");
		c.setShopID(2);
		return c;
	}

	@Test
	public void checkUpdate_CASE1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkUpdate_CASE1");

		Commodity c = getCommodity();
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		c.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c.setRefCommodityID(1);
		c.setRefCommodityMultiple(2);
		c.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		String err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		c.setID(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		c.setID(1);
		//
		Shared.caseLog("测试助记码");
		c.setMnemonicCode("助记码");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("xbk");
		//
		Shared.caseLog("测试标记");
		c.setTag("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_tag);
		c.setTag("12345678");
		//
		Shared.caseLog("测试简称");
		c.setShortName("饮料..#%%");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("饮料");
		//
		Shared.caseLog("测试名称");
		c.setName("   ");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克12345678123456781234567812345678");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克12345678123456781234567812345678");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName(" uyueyr67");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("uyueyr67  ");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("#++uyueyr67&&&&&&$$$");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("*“”、$#/");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试状态码");
		c.setStatus(999);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_commodityStatus);
		c.setStatus(1);
		//
		Shared.caseLog("测试规格");
		c.setSpecification("@#^*$");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("12345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("个");
		//
		Shared.caseLog("测试包装单位");
		c.setPackageUnitID(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_packageUnitID);
		c.setPackageUnitID(1);
		//
		Shared.caseLog("测试品牌ID");
		c.setBrandID(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_brandID);
		c.setBrandID(1);
		//
		Shared.caseLog("测试分类ID");
		c.setCategoryID(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_categoryID);
		c.setCategoryID(1);
		//
		Shared.caseLog("测试零售价");
		c.setPriceRetail(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, "");
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试会员价");
		c.setPriceVIP(-1D);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
		c.setPriceVIP(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, "");
		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试批发价");
		c.setPriceWholesale(-1D);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
		c.setPriceWholesale(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, "");
		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试库存");
		c.setNO(100);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_NOofMultiPackaging);
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		//
		Shared.caseLog("测试退货天数");
		c.setReturnDays(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_returnDays);
		c.setReturnDays(1);
		//
		Shared.caseLog("多包装商品的参照商品倍数要是大于1,多包装商品的参照商品ID要是正整数");
		c.setRefCommodityMultiple(5);
		c.setRefCommodityID(-5);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_refCommodityOfMultiPackaging);
		c.setRefCommodityMultiple(-5);
		c.setRefCommodityID(5);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_refCommodityOfMultiPackaging);
		c.setRefCommodityMultiple(5);
		c.setRefCommodityID(5);
		//
		Shared.caseLog("测试propertyValue1");
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue1("1234@@%%");
		//
		Shared.caseLog("测试propertyValue2");
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue2("1234@@%%");
		//
		Shared.caseLog("测试propertyValue3");
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue3("1234@@%%");
		//
		Shared.caseLog("测试propertyValue4");
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue4("1234@@%%");

		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkUpdate_CASE2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkUpdate_CASE2");

		Commodity c = getCommodity();
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		String err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		c.setID(0);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		c.setID(1);
		//
		Shared.caseLog("测试助记码");
		c.setMnemonicCode("助记码");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("xbk");
		//
		Shared.caseLog("测试标记");
		c.setTag("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_tag);
		c.setTag("12345678");
		//
		Shared.caseLog("测试简称");
		c.setShortName("饮料..#%%");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("饮料");
		// 只允许以()（）-—_、中英数值、空格形式出现，长度1到32
		Shared.caseLog("测试名称");
		c.setName("   ");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName(" 星巴克A.#%%.");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克A.#%%. ");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克12345678123456781234567812345678");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("&^2345678$^");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("fdg( )（c）-—_啊星巴克A");
		//
		Shared.caseLog("测试状态码");
		c.setStatus(999);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_commodityStatus);
		c.setStatus(1);
		//
		Shared.caseLog("测试规格");
		c.setSpecification("@#^*$");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("12345678a");
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("个");
		//
		Shared.caseLog("测试包装单位");
		c.setPackageUnitID(-1);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_packageUnitID);
		c.setPackageUnitID(1);
		//
		Shared.caseLog("测试品牌ID");
		c.setBrandID(0);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_brandID);
		c.setBrandID(1);
		//
		Shared.caseLog("测试分类ID");
		c.setCategoryID(0);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_categoryID);
		c.setCategoryID(1);
		//
		Shared.caseLog("测试零售价");
		c.setPriceRetail(-1);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(0);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, "");
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试会员价");
		c.setPriceVIP(-1D);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
		c.setPriceVIP(0);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, "");
		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试批发价");
		c.setPriceWholesale(-1D);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
		c.setPriceWholesale(0);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, "");
		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试退货天数");
		c.setReturnDays(-1);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_returnDays);
		c.setReturnDays(1);
		//
		Shared.caseLog("测试最近进货价");
		c.setLatestPricePurchase(-100);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_latestPricePurchase);
		c.setLatestPricePurchase(-0.01);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_latestPricePurchase);
		c.setLatestPricePurchase(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_latestPricePurchase);
		c.setLatestPricePurchase(0);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, "");
		c.setLatestPricePurchase(FieldFormat.MAX_OneCommodityPrice);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, "");
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		//
		Shared.caseLog("测试int2---StaffID");
		c.setOperatorStaffID(BaseAction.INVALID_ID);
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(1);
		//
		Shared.caseLog("测试propertyValue1");
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue1("1234@@%%");
		//
		Shared.caseLog("测试propertyValue2");
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue2("1234@@%%");
		//
		Shared.caseLog("测试propertyValue3");
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue3("1234@@%%");
		//
		Shared.caseLog("测试propertyValue4");
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue4("1234@@%%");

		err = c.checkUpdate(BaseBO.CASE_Commodity_UpdatePrice);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkUpdate_CASE3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkUpdate_CASE3");

		Commodity c = getCommodity();
		c.setPurchasingUnit("个");
		String err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		c.setID(0);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		c.setID(1);
		//
		Shared.caseLog("测试助记码");
		c.setMnemonicCode("助记码");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("xbk");
		//
		Shared.caseLog("测试标记");
		c.setTag("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_tag);
		c.setTag("12345678");
		//
		Shared.caseLog("测试简称");
		c.setShortName("饮料..#%%");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("饮料");
		// 只允许以()（）-—_、中英数值、空格形式出现，长度1到32
		Shared.caseLog("测试名称");
		c.setName("   ");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName(" 星巴克A..");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克A ");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克12345678123456781234567812345678");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("*7&&45678&&");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("fdg( )（c）-—_啊星巴克A");
		//
		Shared.caseLog("测试状态码");
		c.setStatus(999);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_commodityStatus);
		c.setStatus(1);
		//
		Shared.caseLog("测试规格");
		c.setSpecification("@#^*$");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("12345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("个");
		//
		Shared.caseLog("测试包装单位");
		c.setPackageUnitID(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_packageUnitID);
		c.setPackageUnitID(1);
		//
		Shared.caseLog("测试品牌ID");
		c.setBrandID(0);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_brandID);
		c.setBrandID(1);
		//
		Shared.caseLog("测试分类ID");
		c.setCategoryID(0);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_categoryID);
		c.setCategoryID(1);
		//
		Shared.caseLog("测试零售价");
		c.setPriceRetail(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(0);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, "");
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试会员价");
		c.setPriceVIP(-1D);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
		c.setPriceVIP(0);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, "");
		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试批发价");
		c.setPriceWholesale(-1D);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
		c.setPriceWholesale(0);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, "");
		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试退货天数");
		c.setReturnDays(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_returnDays);
		c.setReturnDays(1);
		//
		Shared.caseLog("测试采购单位");
		c.setPurchasingUnit("个！@￥（￥");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchasingUnit);
		c.setPurchasingUnit("1234567812345678aaa");
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchasingUnit);
		c.setPurchasingUnit("个");
		//
		Shared.caseLog("测试propertyValue1");
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue1("1234@@%%");
		//
		Shared.caseLog("测试propertyValue2");
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue2("1234@@%%");
		//
		Shared.caseLog("测试propertyValue3");
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue3("1234@@%%");
		//
		Shared.caseLog("测试propertyValue4");
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue4("1234@@%%");

		err = c.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkUpdate_CASE4() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" checkUpdate_CASE4");

		Commodity c = getCommodity();
		c.setShelfLife(0);
		c.setPurchaseFlag(0);
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		c.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
		c.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
		String err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		c.setID(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		c.setID(1);
		//
		Shared.caseLog("测试助记码");
		c.setMnemonicCode("助记码");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("xbk");
		//
		Shared.caseLog("测试标记");
		c.setTag("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_tag);
		c.setTag("12345678");
		//
		Shared.caseLog("测试简称");
		c.setShortName("饮料..#%%");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("饮料");
		// 只允许以()（）-—_、中英数值、空格形式出现，长度1到32
		Shared.caseLog("测试名称");
		c.setName("   ");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName(" 星巴克A.#%%.");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克A.#%%. ");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克12345678123456781234567812345678");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("^^星巴克123&&");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("fdg( )（c）-—_啊星巴克A");

		//
		Shared.caseLog("测试状态码");
		c.setStatus(9999);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_commodityStatus);
		c.setStatus(0);
		//
		Shared.caseLog("测试规格");
		c.setSpecification("@#^*$");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("12345678a");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("个");
		//
		Shared.caseLog("测试包装单位");
		c.setPackageUnitID(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_packageUnitID);
		c.setPackageUnitID(1);
		//
		Shared.caseLog("测试品牌ID");
		c.setBrandID(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_brandID);
		c.setBrandID(1);
		//
		Shared.caseLog("测试分类ID");
		c.setCategoryID(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_categoryID);
		c.setCategoryID(1);
		//
		Shared.caseLog("测试零售价");
		c.setPriceRetail(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, "");
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试会员价");
		c.setPriceVIP(-1D);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
		c.setPriceVIP(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, "");
		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试批发价");
		c.setPriceWholesale(-1D);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
		c.setPriceWholesale(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, "");
		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试保质期");
		c.setShelfLife(1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shelfLifeOfService);
		c.setShelfLife(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shelfLifeOfService);
		c.setShelfLife(0);
		//
		Shared.caseLog("测试退货天数");
		c.setReturnDays(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_returnDays);
		c.setReturnDays(1);
		//
		Shared.caseLog("测试int2---staffID");
		c.setOperatorStaffID(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(BaseAction.INVALID_ID);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(1);
		//
		Shared.caseLog("测试propertyValue1");
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue1("1234@@%%");
		//
		Shared.caseLog("测试propertyValue2");
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue2("1234@@%%");
		//
		Shared.caseLog("测试propertyValue3");
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue3("1234@@%%");
		//
		Shared.caseLog("测试propertyValue4");
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue4("1234@@%%");
		//
		Shared.caseLog("测试最近采购价");
		c.setLatestPricePurchase(0);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_latestPricePurchaseOfService);
		c.setLatestPricePurchase(1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_latestPricePurchaseOfService);
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		//
		Shared.caseLog("测试采购阀值");
		c.setPurchaseFlag(1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchaseFlagOfService);
		c.setPurchaseFlag(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchaseFlagOfService);
		c.setPurchaseFlag(0);
		//
		Shared.caseLog("测试参照商品和参照商品倍数");
		c.setRefCommodityID(1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_refCommodity);
		c.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
		c.setRefCommodityMultiple(1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_refCommodity);
		c.setRefCommodityID(1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_refCommodity);
		c.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
		c.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
		//
		Shared.caseLog("测试积分规则");
		c.setRuleOfPoint(-1);
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_ruleOfPoint);
		c.setRuleOfPoint(1);
		//
		Shared.caseLog("测试StartValueRemark");
		c.setStartValueRemark("132");
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_startValueRemark_Simple);
		c.setStartValueRemark("");
		//
		err = c.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkUpdate_CASE5() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" checkUpdate_CASE5");

		Commodity c = getCommodity();
		c.setStartValueRemark("123");
		String err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		c.setID(0);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		c.setID(1);
		//
		Shared.caseLog("测试助记码");
		c.setMnemonicCode("助记码");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("xbk");
		//
		Shared.caseLog("测试标记");
		c.setTag("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_tag);
		c.setTag("12345678");
		//
		Shared.caseLog("测试简称");
		c.setShortName("饮料..#%%");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("12345678123456781234567812345678a");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("饮料");
		// 只允许以()（）-—_、中英数值、空格形式出现，长度1到32
		Shared.caseLog("测试名称");
		c.setName("   ");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName(" 星巴克A.#%%.");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克A.#%%. ");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克12345678123456781234567812345678");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("&*星巴克123^&^&");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("fdg( )（c）-—_啊星巴克A");
		//
		Shared.caseLog("测试状态码");
		c.setStatus(9999);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_commodityStatus);
		c.setStatus(0);
		//
		Shared.caseLog("测试规格");
		c.setSpecification("@#^*$");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("12345678a");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("个");
		//
		Shared.caseLog("测试包装单位");
		c.setPackageUnitID(-1);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_packageUnitID);
		c.setPackageUnitID(1);
		//
		Shared.caseLog("测试品牌ID");
		c.setBrandID(0);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_brandID);
		c.setBrandID(1);
		//
		Shared.caseLog("测试分类ID");
		c.setCategoryID(0);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_categoryID);
		c.setCategoryID(1);
		//
		Shared.caseLog("测试零售价");
		c.setPriceRetail(-1);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(0);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试会员价");
		c.setPriceVIP(-1D);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
		c.setPriceVIP(0);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试批发价");
		c.setPriceWholesale(-1D);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
		c.setPriceWholesale(0);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试保质期");
		c.setShelfLife(0);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shelfLife);
		c.setShelfLife(10);
		//
		Shared.caseLog("测试退货天数");
		c.setReturnDays(-1);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_returnDays);
		c.setReturnDays(1);
		//
		Shared.caseLog("测试商品类型");
		c.setType(BaseAction.INVALID_Type);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_type);
		c.setType(EnumCommodityType.ECT_Normal.getIndex());
		//
		Shared.caseLog("测试int2---staffID");
		c.setOperatorStaffID(BaseAction.INVALID_ID);
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(1);
		//
		Shared.caseLog("测试propertyValue1");
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue1("1234@@%%");
		//
		Shared.caseLog("测试propertyValue2");
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue2("1234@@%%");
		//
		Shared.caseLog("测试propertyValue3");
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue3("1234@@%%");
		//
		Shared.caseLog("测试propertyValue4");
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue4("1234@@%%");
		//
		Shared.caseLog("测试StartValueRemark");
		c.setStartValueRemark(".!@~#$%^&*()_+=-{}][:'|?></");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		c.setStartValueRemark("012345678901234567890123456789012345678901234567890123456789");
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_startValueRemark);
		c.setStartValueRemark("<(^_^)>");
		//
		err = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkCreate_CASE1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkCreate_CASE_1");

		Commodity c = getCommodity();
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		String err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试助记码");
		c.setMnemonicCode("助记码");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("xbk");
		//
		Shared.caseLog("测试标记");
		c.setTag("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_tag);
		c.setTag("12345678");
		//
		Shared.caseLog("测试简称");
		c.setShortName("饮料..#%%");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("饮料");
		// 只允许以()（）-—_、中英数值、空格形式出现，长度1到32
		Shared.caseLog("测试名称");
		c.setName("   ");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克12345678123456781234567812345678");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName(" dfgjlkjldfkj");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("dfgjlkjldfkj ");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("dfgsdfgf是电话费&&&&&");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("*“”、$#/");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err,  "");
		//
		Shared.caseLog("测试状态码");
		c.setStatus(9999);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_status);
		c.setStatus(0);
		//
		Shared.caseLog("测试规格");
		c.setSpecification("@#^*$");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("12345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("个");
		//
		Shared.caseLog("测试包装单位");
		c.setPackageUnitID(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_packageUnitID);
		c.setPackageUnitID(1);
		//
		Shared.caseLog("测试品牌ID");
		c.setBrandID(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_brandID);
		c.setBrandID(1);
		//
		Shared.caseLog("测试分类ID");
		c.setCategoryID(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_categoryID);
		c.setCategoryID(1);
		//
		Shared.caseLog("测试零售价");
		c.setPriceRetail(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, "");
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice);
		// 会员价、批发价暂时没有用到
//		Shared.caseLog("测试会员价");
//		c.setPriceVIP(-1D);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
//		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice + 0.01);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
//		c.setPriceVIP(0);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
//		Assert.assertEquals(err, "");
//		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice);
		//
//		Shared.caseLog("测试批发价");
//		c.setPriceWholesale(-1D);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
//		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice + 0.01);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
//		c.setPriceWholesale(0);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
//		Assert.assertEquals(err, "");
//		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试条形码");
		c.setBarcodes("条形码");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes(" ");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("^^^&*^*");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("1234567812345678123456781234567812345678123456781234567812345678aaaa");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("123456789");
		//
		Shared.caseLog("测试退货天数");
		c.setReturnDays(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_returnDays);
		c.setReturnDays(1);
		//
		Shared.caseLog("测试商品类型");
		c.setType(BaseAction.INVALID_Type);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_typeOfComposition);
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		//
		Shared.caseLog("测试int2---staffID");
		c.setOperatorStaffID(BaseAction.INVALID_ID);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		//
		Shared.caseLog("测试propertyValue1");
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(40));
		//
		Shared.caseLog("测试propertyValue2");
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(40));
		//
		Shared.caseLog("测试propertyValue3");
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(40));
		//
		Shared.caseLog("测试propertyValue4");
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(40));
		//
		Shared.caseLog("测试nOStart");
		c.setnOStart(1);
		c.setOperatorStaffID(1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_nOStartOfComposition);
		c.setnOStart(Commodity.NO_START_Default);
		//
		Shared.caseLog("测试purchasingPriceStart");
		c.setPurchasingPriceStart(10d);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchasingPriceStartOfComposition);
		c.setPurchasingPriceStart(Commodity.PurchaseFlag_START_Default);
	}

	@Test
	public void checkCreate_CASE2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" checkCreate_CASE2 ");

		Commodity c = getCommodity();
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		c.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c.setRefCommodityID(1);
		c.setRefCommodityMultiple(5);
		c.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		String err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试助记码");
		c.setMnemonicCode("助记码");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("xbk");
		//
		Shared.caseLog("测试标记");
		c.setTag("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_tag);
		c.setTag("12345678");
		//
		Shared.caseLog("测试简称");
		c.setShortName("饮料..#%%");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("饮料");
		// 只允许以()（）-—_、中英数值、空格形式出现，长度1到32
		Shared.caseLog("测试名称");
		c.setName("   ");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName(" 星巴克A.#%%.");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克A.#%%. ");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克12345678123456781234567812345678");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克123&*&*&*");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("fdg( )（c）-—_啊星巴克A");
		//
		Shared.caseLog("测试状态码");
		c.setStatus(9999);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_statusOfMultipackaging);
		c.setStatus(0);
		//
		Shared.caseLog("测试规格");
		c.setSpecification("@#^*$");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("12345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("个");
		//
		Shared.caseLog("测试包装单位");
		c.setPackageUnitID(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_packageUnitID);
		c.setPackageUnitID(1);
		//
		Shared.caseLog("测试品牌ID");
		c.setBrandID(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_brandID);
		c.setBrandID(1);
		//
		Shared.caseLog("测试分类ID");
		c.setCategoryID(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_categoryID);
		c.setCategoryID(1);
		//
		Shared.caseLog("测试零售价");
		c.setPriceRetail(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, "");
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice);
		// 会员价、批发价没有用到
//		Shared.caseLog("测试会员价");
//		c.setPriceVIP(-1D);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
//		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice + 0.01);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
//		c.setPriceVIP(0);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
//		Assert.assertEquals(err, "");
//		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice);
//		//
//		Shared.caseLog("测试批发价");
//		c.setPriceWholesale(-1D);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
//		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice + 0.01);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
//		c.setPriceWholesale(0);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
//		Assert.assertEquals(err, "");
//		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试库存");
		c.setNO(100);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_NOofMultiPackaging);
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		//
		Shared.caseLog("测试条形码");
		c.setBarcodes("条形码");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes(" ");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("^^^&*^*");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("1234567812345678123456781234567812345678123456781234567812345678aaaa");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("123456789");
		//
		Shared.caseLog("测试退货天数");
		c.setReturnDays(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_returnDays);
		c.setReturnDays(1);
		//
		Shared.caseLog("测试商品类型");
		c.setType(BaseAction.INVALID_Type);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_typeOfMultipackaging);
		c.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		//
		Shared.caseLog("多包装商品的参照商品倍数要是大于1,多包装商品的参照商品ID要是正整数");
		c.setRefCommodityMultiple(5);
		c.setRefCommodityID(-5);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_refCommodityOfMultiPackaging);
		c.setRefCommodityMultiple(-5);
		c.setRefCommodityID(5);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_refCommodityOfMultiPackaging);
		c.setRefCommodityMultiple(5);
		c.setRefCommodityID(5);
		//
		Shared.caseLog("测试int2---staffID");
		c.setOperatorStaffID(BaseAction.INVALID_ID);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(1);
		//
		Shared.caseLog("测试propertyValue1");
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(40));
		//
		Shared.caseLog("测试propertyValue2");
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(40));
		//
		Shared.caseLog("测试propertyValue3");
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(40));
		//
		Shared.caseLog("测试propertyValue4");
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(40));
		//
		Shared.caseLog("测试nOStart");
		c.setnOStart(1);
		c.setOperatorStaffID(1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_nOStartOfMultipackaging);
		c.setnOStart(Commodity.NO_START_Default);
		//
		Shared.caseLog("测试purchasingPriceStart");
		c.setPurchasingPriceStart(10d);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchasingPriceStartOfMultipackaging);
		c.setPurchasingPriceStart(Commodity.PurchaseFlag_START_Default);
	}

	@Test
	public void checkCreate_CASE3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkCreate_CASE3");

		Commodity c = getCommodity();
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setPurchasingUnit("");
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		c.setnOStart(Commodity.NO_START_Default);
		c.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		c.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
		c.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
		c.setShelfLife(0);
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		c.setPurchaseFlag(0);
		String err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试助记码");
		c.setMnemonicCode("助记码");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("xbk");
		//
		Shared.caseLog("测试标记");
		c.setTag("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_tag);
		c.setTag("12345678");
		//
		Shared.caseLog("测试简称");
		c.setShortName("饮料..#%%");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("饮料");
		//
		Shared.caseLog("测试期初值");
		c.setnOStart(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_nOStartOfService);
		c.setnOStart(1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_nOStartOfService);
		c.setnOStart(Commodity.NO_START_Default);
		//
		Shared.caseLog("测试期初采购价");
		c.setPurchasingPriceStart(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchasingPriceStartOfService);
		c.setPurchasingPriceStart(1D);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchasingPriceStartOfService);
		c.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		// 只允许以()（）-—_、中英数值、空格形式出现，长度1到32
		Shared.caseLog("测试名称");
		c.setName(" ");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName(" 星巴克A.#%%.");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克A.#%%. ");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克12345678123456781234567812345678");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克123&*&*&");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("fdg( )（c）-—_啊星巴克A");
		//
		Shared.caseLog("测试状态码");
		c.setStatus(Shared.BIG_ID);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_status);
		c.setStatus(BaseAction.INVALID_STATUS);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_status);
		c.setStatus(0);
		//
		Shared.caseLog("测试规格");
		c.setSpecification("@#^*$");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("12345678a");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("个");
		//
		Shared.caseLog("测试包装单位");
		c.setPackageUnitID(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_packageUnitID);
		c.setPackageUnitID(1);
		//
		Shared.caseLog("测试品牌ID");
		c.setBrandID(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_brandID);
		c.setBrandID(1);
		//
		Shared.caseLog("测试分类ID");
		c.setCategoryID(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_categoryID);
		c.setCategoryID(1);
		//
		Shared.caseLog("测试零售价");
		c.setPriceRetail(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, "");
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice);
		// 会员价、批发价没有用到
//		Shared.caseLog("测试会员价");
//		c.setPriceVIP(-1D);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
//		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice + 0.01);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
//		c.setPriceVIP(0);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
//		Assert.assertEquals(err, "");
//		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice);
//		//
//		Shared.caseLog("测试批发价");
//		c.setPriceWholesale(-1D);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
//		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice + 0.01);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
//		c.setPriceWholesale(0);
//		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
//		Assert.assertEquals(err, "");
//		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试保质期");
		c.setShelfLife(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shelfLifeOfService);
		c.setShelfLife(1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shelfLifeOfService);
		c.setShelfLife(0);
		//
		Shared.caseLog("测试条形码");
		c.setBarcodes("条形码");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes(" ");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("^^^&*^*");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("1234567812345678123456781234567812345678123456781234567812345678aaaa");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("123456789");
		//
		Shared.caseLog("测试退货天数");
		c.setReturnDays(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_returnDays);
		c.setReturnDays(1);
		//
		Shared.caseLog("测试商品类型");
		c.setType(BaseAction.INVALID_Type);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_typeOfService);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		//
		Shared.caseLog("测试int2---staffID");
		c.setOperatorStaffID(BaseAction.INVALID_ID);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(1);
		//
		Shared.caseLog("测试propertyValue1");
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue1("123");
		//
		Shared.caseLog("测试propertyValue2");
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue2("123");
		//
		Shared.caseLog("测试propertyValue3");
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue3("123");
		//
		Shared.caseLog("测试propertyValue4");
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue4("123");
		//
		Shared.caseLog("测试最近采购价");
		c.setLatestPricePurchase(0);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_latestPricePurchaseOfService);
		c.setLatestPricePurchase(1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_latestPricePurchaseOfService);
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		//
		Shared.caseLog("测试采购阀值");
		c.setPurchaseFlag(-1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchaseFlagOfService);
		c.setPurchaseFlag(1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchaseFlagOfService);
		c.setPurchaseFlag(0);
		//
		Shared.caseLog("测试采购单位");
		c.setPurchasingUnit("个");
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchasingUnitOfService);
		c.setPurchasingUnit(null);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, "");
		c.setPurchasingUnit("");
		//
		Shared.caseLog("测试库存");
		c.setNO(BaseAction.INVALID_NO);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FEIDL_ERROR_noOfService);
		c.setNO(1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FEIDL_ERROR_noOfService);
		c.setNO(0);
		//
		Shared.caseLog("测试参照商品和参照商品倍数");
		c.setRefCommodityID(1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_refCommodity);
		c.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
		c.setRefCommodityMultiple(1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_refCommodity);
		c.setRefCommodityID(1);
		err = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_refCommodity);
		c.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
		c.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
	}

	@Test
	public void checkCreate_CASE4() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkCreate_CASE4");

		Commodity c = getCommodity();
		c.setStartValueRemark("123");
		System.out.println(c.getSpecification().length());
		String err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试助记码");
		c.setMnemonicCode("助记码");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_mnemonicCode);
		c.setMnemonicCode("xbk");
		//
		Shared.caseLog("测试标记");
		c.setTag("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_tag);
		c.setTag("12345678");
		//
		Shared.caseLog("测试简称");
		c.setShortName("饮料..#%%");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("12345678123456781234567812345678a");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shortName);
		c.setShortName("饮料");
		//
		Shared.caseLog("测试期初值");
		c.setPurchasingPriceStart(100D);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_nOStart);
		c.setPurchasingPriceStart(-1);
		//
		Shared.caseLog("测试期初采购价");
		c.setnOStart(100);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_purchasingPriceStart);
		c.setPurchasingPriceStart(100D);
		// 只允许以()（）-—_、中英数值、空格形式出现，长度1到32
		Shared.caseLog("测试名称");
		c.setName(" ");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName(" 星巴克A.#%%.");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克A.#%%. ");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("星巴克12345678123456781234567812345678");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("￥￥￥￥**$$星巴克1234");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_name);
		c.setName("fdg( )（c）-—_啊星巴克A");

		//
		Shared.caseLog("测试状态码");
		c.setStatus(9999);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_status);
		c.setStatus(0);
		//
		Shared.caseLog("测试规格");
		c.setSpecification("@#^*$");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("12345678a");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_specification);
		c.setSpecification("个");
		//
		Shared.caseLog("测试包装单位");
		c.setPackageUnitID(-1);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_packageUnitID);
		c.setPackageUnitID(1);
		//
		Shared.caseLog("测试品牌ID");
		c.setBrandID(0);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_brandID);
		c.setBrandID(1);
		//
		Shared.caseLog("测试分类ID");
		c.setCategoryID(0);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_categoryID);
		c.setCategoryID(1);
		//
		Shared.caseLog("测试零售价");
		c.setPriceRetail(-1);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice + 0.01);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceRetail);
		c.setPriceRetail(0);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice);
		// 会员价、批发价没有用到
//		Shared.caseLog("测试会员价");
//		c.setPriceVIP(-1D);
//		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
//		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice + 0.01);
//		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceVIP);
//		c.setPriceVIP(0);
//		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, "");
//		c.setPriceVIP(FieldFormat.MAX_OneCommodityPrice);
//		//
//		Shared.caseLog("测试批发价");
//		c.setPriceWholesale(-1D);
//		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
//		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice + 0.01);
//		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, Commodity.FIELD_ERROR_priceWholesale);
//		c.setPriceWholesale(0);
//		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, "");
//		c.setPriceWholesale(FieldFormat.MAX_OneCommodityPrice);
		//
		Shared.caseLog("测试保质期");
		c.setShelfLife(0);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_shelfLife);
		c.setShelfLife(10);
		//
		Shared.caseLog("测试条形码");
		c.setBarcodes("条形码");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes(" ");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("^^^&*^*");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("1234567812345678123456781234567812345678123456781234567812345678aaaa");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_barcode);
		c.setBarcodes("123456789");
		//
		Shared.caseLog("测试退货天数");
		c.setReturnDays(-1);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_returnDays);
		c.setReturnDays(1);
		//
		Shared.caseLog("测试商品类型");
		c.setType(BaseAction.INVALID_Type);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_type);
		c.setType(EnumCommodityType.ECT_Normal.getIndex());
		//
		Shared.caseLog("测试int2---staffID");
		c.setOperatorStaffID(BaseAction.INVALID_ID);
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(1);
		//
		Shared.caseLog("测试propertyValue1");
		c.setPropertyValue1(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue1("123");
		//
		Shared.caseLog("测试propertyValue2");
		c.setPropertyValue2(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue2("123");
		//
		Shared.caseLog("测试propertyValue3");
		c.setPropertyValue3(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue3("123");
		//
		Shared.caseLog("测试propertyValue4");
		c.setPropertyValue4(RandomStringUtils.randomAlphanumeric(60));
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_propertyValue);
		c.setPropertyValue4("123");
		//
		Shared.caseLog("测试StartValueRemark");
		c.setStartValueRemark(".!@~#$%^&*()_+=-{}][:'|?></");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		c.setStartValueRemark("012345678901234567890123456789012345678901234567890123456789");
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_startValueRemark);
		c.setStartValueRemark("<(^_^)>");
		//
		err = c.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkRetrieveN_CASE1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkRetrieveN_CASE1");

		Commodity c = new Commodity();
		c.setID(1);
		c.setUniqueField("132");
		c.setFieldToCheckUnique(1);
		c.setPageIndex(1);
		c.setPageSize(10);
		c.setFieldToCheckUnique(1);
		String err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		c.setID(0);
		err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, "");
		c.setID(BaseAction.INVALID_ID);
		err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_checkUniqueFieldID);
		c.setID(1);
		//
		Shared.caseLog("测试FieldToCheckUnique");
		c.setFieldToCheckUnique(0);
		err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_checkUniqueField);
		c.setFieldToCheckUnique(1);
		//
		Shared.caseLog("测试UniqueField");
		c.setUniqueField("123456789123456789123456789123456789");
		err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_checkUniqueField);
		c.setUniqueField("123");
		//
		Shared.caseLog("测试PageIndex");
		c.setPageIndex(0);
		err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		c.setPageIndex(2);
		//
		Shared.caseLog("测试PageSize");
		c.setPageSize(0);
		err = c.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		c.setPageSize(5);
	}

	@Test
	public void checkRetrieveN_CASE2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkRetrieveN_CASE2 ");

		Commodity c = new Commodity();
		c.setID(1);
		c.setPageIndex(1);
		c.setPageSize(10);
		String err = c.checkRetrieveN(BaseBO.CASE_RetrieveNMultiPackageCommodity);
		Assert.assertEquals(err, "");
		c.setID(0);
		err = c.checkRetrieveN(BaseBO.CASE_RetrieveNMultiPackageCommodity);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		Shared.caseLog("测试PageIndex");
		c.setPageIndex(0);
		err = c.checkRetrieveN(BaseBO.CASE_RetrieveNMultiPackageCommodity);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		c.setPageIndex(2);
		//
		Shared.caseLog("测试PageSize");
		c.setPageSize(0);
		err = c.checkRetrieveN(BaseBO.CASE_RetrieveNMultiPackageCommodity);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		c.setPageSize(5);
	}

	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkRetrieve1");

		Commodity c = new Commodity();
		c.setID(1);
		c.setFieldToCheckUnique(1);
		String err = c.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		c.setID(0);
		err = c.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
	}

	@Test
	public void checkRetrieveN_CASE3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkRetrieveN_CASE3");

		Commodity c = new Commodity();
		c.setStatus(0);
		c.setNO(100);
		c.setBrandID(1);
		c.setCategoryID(1);
		c.setType(EnumCommodityType.ECT_Normal.getIndex());
		c.setQueryKeyword("123");
		c.setPageIndex(1);
		c.setPageSize(10);
		c.setFieldToCheckUnique(-1);
		String err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试状态码");
		c.setStatus(9);
		err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_commodityStatus);
		c.setStatus(0);
		//
		Shared.caseLog("测试库存");
		c.setNO(-9999);
		err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_NO);
		c.setNO(100);
		//
		Shared.caseLog("测试品牌ID");
		c.setBrandID(0);
		err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_brandID);
		c.setBrandID(1);
		//
		Shared.caseLog("测试分类ID");
		c.setCategoryID(0);
		err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_categoryID);
		c.setCategoryID(1);
		//
		Shared.caseLog("测试商品类型");
		c.setType(999999);
		err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_commodityType);
		c.setType(EnumCommodityType.ECT_Normal.getIndex());
		//
		Shared.caseLog("测试QueryKeyword");
		c.setQueryKeyword("1234567812345678123456781234567812345678123456781234567812345678aaaaa");
		err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_queryKeyword);
		c.setQueryKeyword("123");
		//
		Shared.caseLog("测试PageIndex");
		c.setPageIndex(0);
		err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		c.setPageIndex(2);
		//
		Shared.caseLog("测试PageSize");
		c.setPageSize(0);
		err = c.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		c.setPageSize(5);
	}

	@Test
	public void checkDelete_CASE1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkDelete");

		Commodity c = new Commodity();
		c.setID(0);
		c.setOperatorStaffID(3);
		String err = c.checkDelete(BaseBO.CASE_Commodity_DeleteCombination);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		Shared.caseLog("测试ID");
		c.setID(1);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteCombination);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试staffID");
		c.setOperatorStaffID(BaseAction.INVALID_ID);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteCombination);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(0);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteCombination);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		//
		c.setOperatorStaffID(3);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteCombination);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkDelete_CASE2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkDelete");

		Commodity c = new Commodity();
		c.setID(0);
		c.setOperatorStaffID(3);
		String err = c.checkDelete(BaseBO.CASE_Commodity_DeleteMultiPackaging);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		Shared.caseLog("测试ID");
		c.setID(1);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteMultiPackaging);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试StaffID");
		c.setOperatorStaffID(BaseAction.INVALID_ID);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(0);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteMultiPackaging);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		//
		c.setOperatorStaffID(3);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteMultiPackaging);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkDelete_CASE3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("checkDelete");

		Commodity c = new Commodity();
		c.setID(1);
		c.setOperatorStaffID(1);
		String err = c.checkDelete(BaseBO.CASE_Commodity_DeleteSimple);
		Assert.assertEquals(err, "");
		//
		Shared.caseLog("测试ID");
		c.setID(0);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteSimple);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		c.setID(1);
		//
		Shared.caseLog("测试StaffId--staffID");
		c.setOperatorStaffID(BaseAction.INVALID_ID);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteSimple);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(0);
		err = c.checkDelete(BaseBO.CASE_Commodity_DeleteSimple);
		Assert.assertEquals(err, Commodity.FIELD_ERROR_staff);
		c.setOperatorStaffID(3);
	}

	@Test
	public void testCompareTo1() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case1();
	}

	@Test
	public void testCompareTo2() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case2();
	}

	@Test
	public void testCompareTo3() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case3();
	}

	@Test
	public void testCompareTo4() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case4();
	}

	@Test
	public void testCompareTo5() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case5();
	}

	@Test
	public void testCompareTo6() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case6();
	}

	@Test
	public void testCompareTo7() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case7();
	}

	@Test
	public void testCompareTo8() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case8();
	}

	@Test
	public void testCompareTo9() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case9();
	}

	@Test
	public void testCompareTo10() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case10();
	}
}
