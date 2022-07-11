package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BarcodesBO;
import com.bx.erp.action.bo.BarcodesSyncCacheBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.CommodityHistoryBO;
import com.bx.erp.action.bo.commodity.CommoditySyncCacheBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.action.bo.commodity.SubCommodityBO;
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.action.bo.warehousing.WarehousingCommodityBO;
import com.bx.erp.action.commodity.CommoditySyncAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.dao.commodity.CommodityHistoryMapper;
import com.bx.erp.dao.commodity.CommodityMapper;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Shop;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityHistory;
import com.bx.erp.model.commodity.CommodityProperty;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.commodity.CommoditySyncCache;
import com.bx.erp.model.commodity.CommoditySyncCacheDispatcher;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.commodity.Commodity.EnumStatusCommodity;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BaseCommodityTest extends BaseMapperTest {

	public static final int RETURN_OBJECT = 1;

	public static final int STAFF_ID1 = 1;
	public static final int STAFF_ID2 = 2;
	public static final int STAFF_ID3 = 3;
	public static final int STAFF_ID4 = 4;

	@BeforeClass
	public void setup() {
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkCreate(); // ...不能放带有Assert的语句在非@Test的函数
		// Doctor_checkProvider(); // ...不能放带有Assert的语句在非@Test的函数
		// Doctor_checkSubCommodity();
		// Doctor_checkPurchasingOrder();// ... mapper层传不了null，医生目前有问题
		// Doctor_checkBrandID();
		// Doctor_checkCategoryID();
		// Doctor_checkInventory();
		// Doctor_checkReturnCommoditySheet();
		// Doctor_checkStatus();
		// Doctor_checkType();
		// Doctor_checkWarehousing();
		// Doctor_checkNO();
	}

	@AfterClass
	public void tearDown() {
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkProvider(); // ...不能放带有Assert的语句在非@Test的函数
		// Doctor_checkCreate(); // ...不能放带有Assert的语句在非@Test的函数
		// Doctor_checkSubCommodity();
		// Doctor_checkPurchasingOrder();// ... mapper层传不了null，医生目前有问题
		// Doctor_checkBrandID();
		// Doctor_checkCategoryID();
		// Doctor_checkInventory();
		// Doctor_checkReturnCommoditySheet();
		// Doctor_checkStatus();
		// Doctor_checkType();
		// Doctor_checkWarehousing();
		// Doctor_checkNO();
	}

	public static class DataInput {
		private static Commodity commodityInput = null;
		private static SubCommodity subCommodityInput = null;

		public static final Commodity getCommodity() throws CloneNotSupportedException, InterruptedException {
			commodityInput = new Commodity();
			commodityInput.setStatus(EnumStatusCommodity.ESC_Normal.getIndex());
			commodityInput.setName("薯片" + Shared.generateStringByTime(9));
			Thread.sleep(100);
			commodityInput.setShortName("商品");
			commodityInput.setSpecification("克");
			commodityInput.setPackageUnitID(1);
			commodityInput.setPurchasingUnit("瓶");
			commodityInput.setBrandID(1);
			commodityInput.setCategoryID(1);
			commodityInput.setMnemonicCode("SP");
			commodityInput.setPricingType(1);
			commodityInput.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
			commodityInput.setPriceRetail(10); // 零售价
			commodityInput.setPriceVIP(10); // 会员价
			commodityInput.setPriceWholesale(10); // 批发价
			// commodityInput.setRatioGrossMargin(Math.abs(new Random().nextFloat()));
			commodityInput.setCanChangePrice(Math.abs(new Random().nextInt(1)));
			commodityInput.setRuleOfPoint(1);
			commodityInput.setPicture("url=116843435555");
			commodityInput.setShelfLife(Math.abs(new Random().nextInt(18)) + 1);
			commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
			commodityInput.setPurchaseFlag(Math.abs(new Random().nextInt(1)) + 1);//
			commodityInput.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
			commodityInput.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
			// commodityInput.setIsGift(Math.abs(new Random().nextInt(1)));
			commodityInput.setTag("111");
			commodityInput.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
			commodityInput.setOperatorStaffID(STAFF_ID3);
			// commodityInput.setNOAccumulated(Math.abs(new Random().nextInt(18000)));
			commodityInput.setnOStart(Commodity.NO_START_Default); // ...常量
			commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default); // ...常量
			commodityInput.setType(EnumCommodityType.ECT_Normal.getIndex());
			commodityInput.setStartValueRemark("");
			commodityInput.setBarcodes("asdkjvhiasd" + Shared.generateStringByTime(6));
			// Thread.sleep(500);
			commodityInput.setPropertyValue1("自定义属性1");
			commodityInput.setPropertyValue2("自定义属性2");
			commodityInput.setPropertyValue3("自定义属性3");
			commodityInput.setPropertyValue4("自定义属性4");
			commodityInput.setProviderIDs("1");
			commodityInput.setRuleOfPoint(1);
			commodityInput.setPurchaseFlag(1);
			commodityInput.setShelfLife(1);
			commodityInput.setReturnObject(RETURN_OBJECT); // 返回对象的值
			commodityInput.setMultiPackagingInfo(commodityInput.getBarcodes() + ";" + commodityInput.getPackageUnitID() + ";" + commodityInput.getRefCommodityMultiple() + ";" + commodityInput.getPriceRetail() + ";"
					+ commodityInput.getPriceVIP() + ";" + commodityInput.getPriceWholesale() + ";" + commodityInput.getName() + ";");
			commodityInput.setShopID(2);
			
			return (Commodity) commodityInput.clone();
		}
		
		public static final CommodityShopInfo getCommodityShopInfo() throws CloneNotSupportedException {
			CommodityShopInfo commodityShopInfo = new CommodityShopInfo();
			commodityShopInfo.setCommodityID(1);
			commodityShopInfo.setShopID(2);
			commodityShopInfo.setPriceRetail(12);
			commodityShopInfo.setnOStart(Commodity.NO_START_Default); // ...常量
			commodityShopInfo.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default); // ...常量
			return (CommodityShopInfo) commodityShopInfo.clone();
		}

		public static final Commodity getCommodity(int type) throws CloneNotSupportedException, InterruptedException {
			commodityInput = new Commodity();
			commodityInput.setStatus(EnumStatusCommodity.ESC_Normal.getIndex());
			commodityInput.setName("薯片" + System.currentTimeMillis() % 1000000);
			Thread.sleep(1000);
			commodityInput.setShortName("薯片");
			commodityInput.setSpecification("克");
			commodityInput.setPackageUnitID(1);
			commodityInput.setBrandID(1);
			commodityInput.setCategoryID(1);
			commodityInput.setMnemonicCode("SP");
			commodityInput.setPricingType(1);
			commodityInput.setPriceRetail(10);
			commodityInput.setPriceVIP(Math.abs(new Random().nextDouble()));
			commodityInput.setPriceWholesale(Math.abs(new Random().nextDouble()));
			commodityInput.setCanChangePrice(Math.abs(new Random().nextInt(1)));
			commodityInput.setRuleOfPoint(1);
			commodityInput.setShelfLife(Math.abs(new Random().nextInt(18)) + 1);
			commodityInput.setPurchaseFlag(Math.abs(new Random().nextInt(1)) + 1);//
			commodityInput.setPicture("url=116843435555");
			commodityInput.setReturnDays(Math.abs(new Random().nextInt(18)) + 1);
			commodityInput.setType(type);
			commodityInput.setTag("111");
			commodityInput.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
			commodityInput.setOperatorStaffID(STAFF_ID4);
			commodityInput.setnOStart(Commodity.NO_START_Default); // ...常量
			commodityInput.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default); // ...常量
			commodityInput.setStartValueRemark("");
			commodityInput.setBarcodes("asdkjvhiasd" + System.currentTimeMillis() % 1000000);
			Thread.sleep(1000);
			commodityInput.setPropertyValue1("自定义属性1");
			commodityInput.setPropertyValue2("自定义属性2");
			commodityInput.setPropertyValue3("自定义属性3");
			commodityInput.setPropertyValue4("自定义属性4");
			commodityInput.setPurchasingUnit("箱");
			commodityInput.setProviderIDs("1");
			commodityInput.setRefCommodityID(Commodity.DEFAULT_VALUE_RefCommodityID);
			commodityInput.setRefCommodityMultiple(Commodity.DEFAULT_VALUE_RefCommodityMultiple);
			commodityInput.setLatestPricePurchase(1000000);
			commodityInput.setReturnObject(BaseModel.EnumBoolean.EB_Yes.getIndex());
			commodityInput.setMultiPackagingInfo(commodityInput.getBarcodes() + ";" + commodityInput.getPackageUnitID() + ";" + commodityInput.getRefCommodityMultiple() + ";" + commodityInput.getPriceRetail() + ";"
					+ commodityInput.getPriceVIP() + ";" + commodityInput.getPriceWholesale() + ";" + commodityInput.getName() + ";");
			//
			switch (EnumCommodityType.values()[type]) {
			case ECT_MultiPackaging:
				commodityInput.setRefCommodityID(1); // ...
				commodityInput.setRefCommodityMultiple(2);
				break;
			case ECT_Service:
				commodityInput.setShelfLife(0);
				commodityInput.setPurchasingUnit("");
				commodityInput.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				commodityInput.setPurchaseFlag(0);
				break;
			case ECT_Combination:
				commodityInput.setRuleOfPoint(0);
				commodityInput.setShelfLife(0);
				commodityInput.setPurchaseFlag(0);//
				//
				List<SubCommodity> subCommodities = new ArrayList<SubCommodity>();

				SubCommodity subCommodity1 = new SubCommodity();
				subCommodity1.setSubCommodityNO(2);
				subCommodity1.setSubCommodityID(2);
				subCommodity1.setPrice(3);
				subCommodities.add(subCommodity1);

				SubCommodity subCommodity2 = new SubCommodity();
				subCommodity2.setSubCommodityNO(3);
				subCommodity2.setSubCommodityID(3);
				subCommodity2.setPrice(3);
				subCommodities.add(subCommodity2);

				commodityInput.setListSlave1(subCommodities);
				break;
			default:
				break;
			}

			return (Commodity) commodityInput.clone();
		}

		public static final SubCommodity getSubCommodity() throws CloneNotSupportedException {

			subCommodityInput = new SubCommodity();
			subCommodityInput.setSubCommodityID(1);
			subCommodityInput.setSubCommodityNO(1);
			subCommodityInput.setPrice(8);

			return (SubCommodity) subCommodityInput.clone();
		}

		// 用于修改商品的测试(需自己设ID、Name，string1和string2等等)
		public static final Commodity getUpdateCommodity() throws CloneNotSupportedException, InterruptedException {
			commodityInput = new Commodity();
			commodityInput.setShortName("修改商品");
			commodityInput.setSpecification("千克");
			commodityInput.setPackageUnitID(2);
			commodityInput.setPurchasingUnit("瓶");
			commodityInput.setBrandID(1);
			commodityInput.setCategoryID(2);
			commodityInput.setMnemonicCode("XGSP");
			commodityInput.setPricingType(1);// 暂时写死
			commodityInput.setPriceRetail(20);
			commodityInput.setPriceVIP(21);
			commodityInput.setPriceWholesale(19);
			commodityInput.setCanChangePrice(0);// 暂时写死
			commodityInput.setRuleOfPoint(1);// 暂时写死
			commodityInput.setShelfLife(366);
			commodityInput.setReturnDays(30);
			commodityInput.setTag("走葱");
			commodityInput.setNO(0);
			commodityInput.setnOStart(-1);
			commodityInput.setPurchasingPriceStart(-1.0);
			commodityInput.setPurchaseFlag(1);// 暂时写死
			commodityInput.setPropertyValue1("修改自定义内容1");
			commodityInput.setPropertyValue2("修改自定义内容2");
			commodityInput.setPropertyValue3("修改自定义内容3");
			commodityInput.setPropertyValue4("修改自定义内容4");
			commodityInput.setReturnObject(RETURN_OBJECT);
			commodityInput.setProviderIDs("1");
			commodityInput.setMultiPackagingInfo("123456789" + ";" + commodityInput.getPackageUnitID() + ";" + commodityInput.getRefCommodityMultiple() + ";" + commodityInput.getPriceRetail() + ";" + commodityInput.getPriceVIP() + ";"
					+ commodityInput.getPriceWholesale() + ";" + commodityInput.getName() + ";");
			commodityInput.setShopID(2);
			return (Commodity) commodityInput.clone();
		}

		private static Provider providerInput = null;

		public static final Provider getProvider() throws CloneNotSupportedException, InterruptedException {
			providerInput = new Provider();
			providerInput.setName(Shared.getLongestProviderName("淘宝"));
			Thread.sleep(1);
			providerInput.setDistrictID(1);
			providerInput.setAddress("广州市天河区二十八中学");
			providerInput.setContactName("zda");
			providerInput.setMobile(Shared.getValidStaffPhone());

			return (Provider) providerInput.clone();
		}

		public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Provider p) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType)//
					.param(Provider.field.getFIELD_NAME_ID(), "" + p.getID()) //
					.param(Provider.field.getFIELD_NAME_name(), p.getName() + "")//
					.param(Provider.field.getFIELD_NAME_districtID(), p.getDistrictID() + "")//
					.param(Provider.field.getFIELD_NAME_address(), p.getAddress())//
					.param(Provider.field.getFIELD_NAME_contactName(), p.getContactName())//
					.param(Provider.field.getFIELD_NAME_mobile(), p.getMobile());//
			return builder;
		}

		private static CommodityHistory commodityHistoryInput = null;

		public static final CommodityHistory getCommodityHistory() throws CloneNotSupportedException, InterruptedException, ParseException {
			DateFormat df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
			String dt1 = "2018-01-01 00:00:00";
			commodityHistoryInput = new CommodityHistory();
			commodityHistoryInput.setFieldName("");
			commodityHistoryInput.setCommodityID(0);
			commodityHistoryInput.setNewValue("");
			commodityHistoryInput.setOldValue("");
			commodityHistoryInput.setDate1(df.parse(dt1));
			commodityHistoryInput.setDate2(new Date());

			return (CommodityHistory) commodityHistoryInput.clone();
		}

		private static CommodityProperty commodityPropertyInput = null;

		public static final CommodityProperty getCommodityProperty() throws CloneNotSupportedException, InterruptedException {
			commodityPropertyInput = new CommodityProperty();
			commodityPropertyInput.setID(1);
			commodityPropertyInput.setName1("");
			commodityPropertyInput.setName2("");
			commodityPropertyInput.setName3("");
			commodityPropertyInput.setName4("");

			return commodityPropertyInput;
		}

		private static CommoditySyncCacheDispatcher ccd = new CommoditySyncCacheDispatcher();

		public static final CommoditySyncCacheDispatcher getCommoditySyncCacheDispatcher() throws CloneNotSupportedException, InterruptedException {
			ccd = new CommoditySyncCacheDispatcher();
			ccd.setSyncCacheID(2);
			ccd.setPos_ID(new Random().nextInt(5) + 1);
			return (CommoditySyncCacheDispatcher) ccd.clone();
		}

		private static CommoditySyncCache csc = new CommoditySyncCache();

		public static final CommoditySyncCache getCommoditySyncCache(String syncType) throws CloneNotSupportedException {
			csc = new CommoditySyncCache();
			csc.setSyncData_ID(2);
			csc.setSyncSequence(1);// 本字段在这里无关紧要
			csc.setSyncType(syncType);
			csc.setPosID(1);

			return (CommoditySyncCache) csc.clone();
		}

		// public static final MockHttpServletRequestBuilder getBuilder(String url,
		// MediaType contentType, Commodity c) { // TODO ...
		// MockHttpServletRequestBuilder builder =
		// post(url).contentType(contentType).session((MockHttpSession) session)//
		// .param(Commodity.field.getFIELD_NAME_ID(), "" + c.getID()) //
		// .param(Commodity.field.getFIELD_NAME_status(), String.valueOf(c.getStatus()))
		// //
		// .param(Commodity.field.getFIELD_NAME_name(), c.getName())//
		// .param(Commodity.field.getFIELD_NAME_shortName(), c.getShortName())//
		// .param(Commodity.field.getFIELD_NAME_specification(), c.getSpecification())
		// //
		// .param(Commodity.field.getFIELD_NAME_packageUnitID(),
		// String.valueOf(c.getPackageUnitID())) //
		// .param(Commodity.field.getFIELD_NAME_purchasingUnit(), c.getPurchasingUnit())
		// //
		// .param(Commodity.field.getFIELD_NAME_brandID(),
		// String.valueOf(c.getBrandID()))//
		// .param(Commodity.field.getFIELD_NAME_categoryID(),
		// String.valueOf(c.getCategoryID())) //
		// .param(Commodity.field.getFIELD_NAME_mnemonicCode(), c.getMnemonicCode()) //
		// .param(Commodity.field.getFIELD_NAME_pricingType(),
		// String.valueOf(c.getPricingType())) //
		// .param(Commodity.field.getFIELD_NAME_latestPricePurchase(),
		// String.valueOf(c.getLatestPricePurchase())) //
		// .param(Commodity.field.getFIELD_NAME_priceRetail(),
		// String.valueOf(c.getPriceRetail())) //
		// .param(Commodity.field.getFIELD_NAME_priceVIP(),
		// String.valueOf(c.getPriceVIP())) //
		// .param(Commodity.field.getFIELD_NAME_priceWholesale(),
		// String.valueOf(c.getPriceWholesale())) //
		// .param(Commodity.field.getFIELD_NAME_canChangePrice(),
		// String.valueOf(c.getCanChangePrice())) //
		// .param(Commodity.field.getFIELD_NAME_ruleOfPoint(),
		// String.valueOf(c.getRuleOfPoint())) //
		// .param(Commodity.field.getFIELD_NAME_picture(), c.getPicture()) //
		// .param(Commodity.field.getFIELD_NAME_shelfLife(),
		// String.valueOf(c.getShelfLife())) //
		// .param(Commodity.field.getFIELD_NAME_returnDays(),
		// String.valueOf(c.getReturnDays())) //
		// .param(Commodity.field.getFIELD_NAME_purchaseFlag(),
		// String.valueOf(c.getPurchaseFlag())) //
		// .param(Commodity.field.getFIELD_NAME_refCommodityID(),
		// String.valueOf(c.getRefCommodityID())) //
		// .param(Commodity.field.getFIELD_NAME_refCommodityMultiple(),
		// String.valueOf(c.getRefCommodityMultiple())) //
		// .param(Commodity.field.getFIELD_NAME_tag(), c.getTag()) //
		// .param(Commodity.field.getFIELD_NAME_NO(), String.valueOf(c.getNO())) //
		// .param(Commodity.field.getFIELD_NAME_type(), String.valueOf(c.getType())) //
		// .param(Commodity.field.getFIELD_NAME_nOStart(), c.getnOStart() == 0 ?
		// Commodity.NO_START_Default + "" : c.getnOStart() + "") //
		// ...使用Commodity.NO_START_Default。搜索其它有期初值的地方看看是否需要修改
		// .param(Commodity.field.getFIELD_NAME_purchasingPriceStart(),
		// Math.abs(GeneralUtil.sub(c.getPurchasingPriceStart(), 0)) <
		// BaseModel.TOLERANCE ? Commodity.PURCHASING_PRICE_START_Default + "" :
		// c.getPurchasingPriceStart() + "") //
		// ...使用Commodity.PURCHASING_PRICE_START_Default。搜索其它有期初值的地方看看是否需要修改
		// .param(Commodity.field.getFIELD_NAME_startValueRemark(),
		// c.getStartValueRemark()) //
		// .param(Commodity.field.getFIELD_NAME_propertyValue1(),
		// c.getPropertyValue1())//
		// .param(Commodity.field.getFIELD_NAME_propertyValue2(),
		// c.getPropertyValue2())//
		// .param(Commodity.field.getFIELD_NAME_propertyValue3(),
		// c.getPropertyValue3())//
		// .param(Commodity.field.getFIELD_NAME_propertyValue4(),
		// c.getPropertyValue4())//
		// .param(Commodity.field.getFIELD_NAME_multiPackagingInfo(),
		// c.getMultiPackagingInfo()) //
		// .param(Commodity.field.getFIELD_NAME_providerIDs(), c.getProviderIDs()) //
		// .param(Commodity.field.getFIELD_NAME_returnObject(),
		// String.valueOf(c.getReturnObject())) //
		// .param(Commodity.field.getFIELD_NAME_barcodes(), c.getBarcodes()) //
		// ;
		//
		// return builder;
		// }

		public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Commodity c, HttpSession session) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType).session((MockHttpSession) session)//
					.param(Commodity.field.getFIELD_NAME_ID(), "" + c.getID()) //
					.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(c.getStatus())) //
					.param(Commodity.field.getFIELD_NAME_name(), c.getName())//
					.param(Commodity.field.getFIELD_NAME_shortName(), c.getShortName())//
					.param(Commodity.field.getFIELD_NAME_specification(), c.getSpecification()) //
					.param(Commodity.field.getFIELD_NAME_packageUnitID(), String.valueOf(c.getPackageUnitID())) //
					.param(Commodity.field.getFIELD_NAME_purchasingUnit(), c.getPurchasingUnit()) //
					.param(Commodity.field.getFIELD_NAME_brandID(), String.valueOf(c.getBrandID()))//
					.param(Commodity.field.getFIELD_NAME_categoryID(), String.valueOf(c.getCategoryID())) //
					.param(Commodity.field.getFIELD_NAME_mnemonicCode(), c.getMnemonicCode()) //
					.param(Commodity.field.getFIELD_NAME_pricingType(), String.valueOf(c.getPricingType())) //
					.param(Commodity.field.getFIELD_NAME_latestPricePurchase(), String.valueOf(c.getLatestPricePurchase())) //
					.param(Commodity.field.getFIELD_NAME_priceRetail(), String.valueOf(c.getPriceRetail())) //
					.param(Commodity.field.getFIELD_NAME_priceVIP(), String.valueOf(c.getPriceVIP())) //
					.param(Commodity.field.getFIELD_NAME_priceWholesale(), String.valueOf(c.getPriceWholesale())) //
					.param(Commodity.field.getFIELD_NAME_canChangePrice(), String.valueOf(c.getCanChangePrice())) //
					.param(Commodity.field.getFIELD_NAME_ruleOfPoint(), String.valueOf(c.getRuleOfPoint())) //
					.param(Commodity.field.getFIELD_NAME_picture(), c.getPicture()) //
					.param(Commodity.field.getFIELD_NAME_shelfLife(), String.valueOf(c.getShelfLife())) //
					.param(Commodity.field.getFIELD_NAME_returnDays(), String.valueOf(c.getReturnDays())) //
					.param(Commodity.field.getFIELD_NAME_purchaseFlag(), String.valueOf(c.getPurchaseFlag())) //
					.param(Commodity.field.getFIELD_NAME_refCommodityID(), String.valueOf(c.getRefCommodityID())) //
					.param(Commodity.field.getFIELD_NAME_refCommodityMultiple(), String.valueOf(c.getRefCommodityMultiple())) //
					.param(Commodity.field.getFIELD_NAME_tag(), c.getTag()) //
					.param(Commodity.field.getFIELD_NAME_NO(), String.valueOf(c.getNO())) //
					.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(c.getType())) //
					.param(Commodity.field.getFIELD_NAME_nOStart(), c.getnOStart() == 0 ? Commodity.NO_START_Default + "" : c.getnOStart() + "") // ...使用Commodity.NO_START_Default。搜索其它有期初值的地方看看是否需要修改
					.param(Commodity.field.getFIELD_NAME_purchasingPriceStart(),
							Math.abs(GeneralUtil.sub(c.getPurchasingPriceStart(), 0)) < BaseModel.TOLERANCE ? Commodity.PURCHASING_PRICE_START_Default + "" : c.getPurchasingPriceStart() + "") // ...使用Commodity.PURCHASING_PRICE_START_Default。搜索其它有期初值的地方看看是否需要修改
					.param(Commodity.field.getFIELD_NAME_startValueRemark(), c.getStartValueRemark()) //
					.param(Commodity.field.getFIELD_NAME_propertyValue1(), c.getPropertyValue1())//
					.param(Commodity.field.getFIELD_NAME_propertyValue2(), c.getPropertyValue2())//
					.param(Commodity.field.getFIELD_NAME_propertyValue3(), c.getPropertyValue3())//
					.param(Commodity.field.getFIELD_NAME_propertyValue4(), c.getPropertyValue4())//
					.param(Commodity.field.getFIELD_NAME_multiPackagingInfo(), c.getMultiPackagingInfo()) //
					.param(Commodity.field.getFIELD_NAME_providerIDs(), c.getProviderIDs()) //
					.param(Commodity.field.getFIELD_NAME_returnObject(), String.valueOf(c.getReturnObject())) //
					.param(Commodity.field.getFIELD_NAME_barcodes(), c.getBarcodes()) //
					.param(Commodity.field.getFIELD_NAME_lastOperationToPicture(), String.valueOf(c.getLastOperationToPicture()))//
					.param(Commodity.field.getFIELD_NAME_shopID(), String.valueOf(c.getShopID()))//
			;

			return builder;
		}

	}

	public static Commodity createCommodityViaAction(Commodity c, MockMvc mvc, HttpSession session, Map<String, BaseBO> BOsMap, String dbName) throws Exception, UnsupportedEncodingException {
		MvcResult mr1 = null;
		if (StringUtils.isEmpty(c.getSubCommodityInfo())) { // 创建单品，多包装，服务商品
			mr1 = mvc.perform(DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, session) //
					.session((MockHttpSession) session)//
			).andExpect(status().isOk()).andDo(print()).andReturn(); //
			// 检查错误码
			Shared.checkJSONErrorCode(mr1);

		} else { // 创建组合商品
			mr1 = mvc.perform(DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, session) //
					.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), c.getSubCommodityInfo())// 该字段不为空则是创建组合商品
					.session((MockHttpSession) session)//
			).andExpect(status().isOk()).andDo(print()).andReturn(); //
			// 检查错误码
			Shared.checkJSONErrorCode(mr1);
		}
		//
		// 检查点
		PosBO posBO = (PosBO) BOsMap.get(PosBO.class.getSimpleName());
		CommoditySyncCacheBO commoditySyncCacheBO = (CommoditySyncCacheBO) BOsMap.get(CommoditySyncCacheBO.class.getSimpleName());
		BarcodesSyncCacheBO barcodesSyncCacheBO = (BarcodesSyncCacheBO) BOsMap.get(BarcodesSyncCacheBO.class.getSimpleName());
		BarcodesBO barcodesBO = (BarcodesBO) BOsMap.get(BarcodesBO.class.getSimpleName());
		WarehousingBO warehousingBO = (WarehousingBO) BOsMap.get(WarehousingBO.class.getSimpleName());
		WarehousingCommodityBO warehousingCommodityBO = (WarehousingCommodityBO) BOsMap.get(WarehousingCommodityBO.class.getSimpleName());
		CommodityBO commodityBO = (CommodityBO) BOsMap.get(CommodityBO.class.getSimpleName());
		SubCommodityBO subCommodityBO = (SubCommodityBO) BOsMap.get(SubCommodityBO.class.getSimpleName());
		ProviderCommodityBO providerCommodityBO = (ProviderCommodityBO) BOsMap.get(ProviderCommodityBO.class.getSimpleName());
		CommodityHistoryBO commodityHistoryBO = (CommodityHistoryBO) BOsMap.get(CommodityHistoryBO.class.getSimpleName());
		PackageUnitBO packageUnitBO = (PackageUnitBO) BOsMap.get(PackageUnitBO.class.getSimpleName());
		CategoryBO categoryBO = (CategoryBO) BOsMap.get(CategoryBO.class.getSimpleName());
		//
		CommodityCP.verifyCreate(mr1, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbName);
		// 把Commodity解析出来
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Commodity commodity = (Commodity) new Commodity().parse1(jsonObject.getString(BaseAction.KEY_Object));
		return commodity;
	}

	public static List<List<BaseModel>> createCommodityViaMapper(int iUseCaseID, Commodity comm) {
		String error = comm.checkCreate(iUseCaseID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramForComm = comm.getCreateParamEx(iUseCaseID, comm);
		List<List<BaseModel>> bmList = null;
		switch (iUseCaseID) {
		case BaseBO.CASE_Commodity_CreateSingle:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createSimpleEx(paramForComm);
			break;
		case BaseBO.CASE_Commodity_CreateComposition:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createCombinationEx(paramForComm);
			break;
		case BaseBO.CASE_Commodity_CreateService:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createServiceEx(paramForComm);
			break;
		case BaseBO.CASE_Commodity_CreateMultiPackaging:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createMultiPackagingEx(paramForComm);
			break;
		default:
			Assert.assertTrue(false, "不存在的创建UseCase");
		}
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramForComm.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramForComm.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Commodity commCreated = null;
		Warehousing warehousing = null;
		if (comm.getnOStart() >= 0 && comm.getPurchasingPriceStart() >= 0) {
			comm.setNO(comm.getnOStart()); // 为了通过下面的comm.compareTo(commCreated)，后面不还原
			commCreated = (Commodity) bmList.get(5).get(0);
			comm.setLatestPricePurchase(commCreated.getLatestPricePurchase()); // 为了通过下面的comm.compareTo(commCreated)，后面不还原
			warehousing = (Warehousing) bmList.get(3).get(0);
		} else {
			commCreated = (Commodity) bmList.get(0).get(0);
		}
		//
		int commCreatedStaffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID()); // 为了能通过下面的断言
		comm.setIgnoreIDInComparision(true);
		comm.setIgnoreIDInComparision(true);
		comm.setIgnoreSlaveListInComparision(true);
		commCreated.setnOStart(comm.getnOStart());
		commCreated.setPurchasingPriceStart(comm.getPurchasingPriceStart());
		commCreated.setNO(comm.getNO());
		commCreated.setLatestPricePurchase(comm.getLatestPricePurchase()); // 为了通过下面的comm.compareTo(commCreated)，后面不还原
		setCommodityShopInfo(commCreated, comm, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, warehousing);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等，" + comm + "和" + commCreated + "不相等");
		}
		commCreated.setOperatorStaffID(commCreatedStaffID); // 还原之前的StaffID的值
		//
		int staffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID()); // ... int2 = StaffID
		String barcodes = ((Barcodes) bmList.get(1).get(0)).getBarcode();
		commCreated.setBarcodes(barcodes);
		String error1 = commCreated.checkCreate(iUseCaseID);
		Assert.assertEquals(error1, "");
		commCreated.setOperatorStaffID(staffID);
		commCreated.setBarcodes(null);
		//
		if (commCreated.getType() != EnumCommodityType.ECT_Combination.getIndex() && commCreated.getType() != EnumCommodityType.ECT_Service.getIndex()) { // 服务商品没有供应商，所以不需要创建供应商商品
			// 创建一个供应商商品
			ProviderCommodity pc1 = new ProviderCommodity();
			pc1.setProviderID(1);
			pc1.setCommodityID(commCreated.getID());
			Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
			assertNotNull(createPC);
			pc1.setIgnoreIDInComparision(true);
			if (pc1.compareTo(createPC) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		} else if (commCreated.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
			SubCommodity subCommodity = new SubCommodity();
			subCommodity.setCommodityID(commCreated.getID());
			subCommodity.setSubCommodityID(9);
			subCommodity.setSubCommodityNO(1);
			Map<String, Object> subParams = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(subParams);
			//
			subCommodity.setIgnoreIDInComparision(true);
			if (subCommodity.compareTo(subCommodityCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			System.out.println(subCommodity == null ? "null" : subCommodity);
			Assert.assertTrue(subCommodity != null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
					subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			// 字段验证
			String error2 = subCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error2, "");
		}
		List<CommodityShopInfo> commodityShopInfoList = createListCommodityShopInfoViaMapper(commCreated, comm, iUseCaseID, Shared.DBName_Test, warehousing);
		commCreated.setListSlave2(commodityShopInfoList);
		commCreated.setListSlave1(comm.getListSlave1()); // 不比较listSlave1
		comm.setIgnoreSlaveListInComparision(false);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不一致！");
		}
		retrieveNCommodityHistory(commCreated.getID(), comm.getOperatorStaffID(), commodityHistoryMapper);

		return bmList;
	}
	
	// TODO 合并重载的版本
	public static List<List<BaseModel>> createCommodityViaMapper(int iUseCaseID, Commodity comm, String dbName) {
		String error = comm.checkCreate(iUseCaseID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramForComm = comm.getCreateParamEx(iUseCaseID, comm);
		List<List<BaseModel>> bmList = null;
		switch (iUseCaseID) {
		case BaseBO.CASE_Commodity_CreateSingle:
			DataSourceContextHolder.setDbName(dbName);
			bmList = commodityMapper.createSimpleEx(paramForComm);
			break;
		case BaseBO.CASE_Commodity_CreateComposition:
			DataSourceContextHolder.setDbName(dbName);
			bmList = commodityMapper.createCombinationEx(paramForComm);
			break;
		case BaseBO.CASE_Commodity_CreateService:
			DataSourceContextHolder.setDbName(dbName);
			bmList = commodityMapper.createServiceEx(paramForComm);
			break;
		case BaseBO.CASE_Commodity_CreateMultiPackaging:
			DataSourceContextHolder.setDbName(dbName);
			bmList = commodityMapper.createMultiPackagingEx(paramForComm);
			break;
		default:
			Assert.assertTrue(false, "不存在的创建UseCase");
		}
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramForComm.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramForComm.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Commodity commCreated = null;
		if (comm.getnOStart() >= 0 && comm.getPurchasingPriceStart() >= 0) {
			comm.setNO(comm.getnOStart());
			commCreated = (Commodity) bmList.get(5).get(0);
		} else {
			commCreated = (Commodity) bmList.get(0).get(0);
		}
		//
		int commCreatedStaffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID()); // 为了能通过下面的断言
		comm.setIgnoreIDInComparision(true);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等，" + comm + "和" + commCreated + "不相等");
		}
		commCreated.setOperatorStaffID(commCreatedStaffID); // 还原之前的StaffID的值
		//
		int staffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID()); // ... int2 = StaffID
		String barcodes = ((Barcodes) bmList.get(1).get(0)).getBarcode();
		commCreated.setBarcodes(barcodes);
		String error1 = commCreated.checkCreate(iUseCaseID);
		Assert.assertEquals(error1, "");
		commCreated.setOperatorStaffID(staffID);
		commCreated.setBarcodes(null);
		//
		if (commCreated.getType() != EnumCommodityType.ECT_Combination.getIndex() && commCreated.getType() != EnumCommodityType.ECT_Service.getIndex()) { // 服务商品没有供应商，所以不需要创建供应商商品
			// 创建一个供应商商品
			ProviderCommodity pc1 = new ProviderCommodity();
			pc1.setProviderID(1);
			pc1.setCommodityID(commCreated.getID());
			Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
			DataSourceContextHolder.setDbName(dbName);
			ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
			assertNotNull(createPC);
			pc1.setIgnoreIDInComparision(true);
			if (pc1.compareTo(createPC) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		} else if (commCreated.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
			SubCommodity subCommodity = new SubCommodity();
			subCommodity.setCommodityID(commCreated.getID());
			subCommodity.setSubCommodityID(9);
			subCommodity.setSubCommodityNO(1);
			Map<String, Object> subParams = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
			DataSourceContextHolder.setDbName(dbName);
			SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(subParams);
			//
			subCommodity.setIgnoreIDInComparision(true);
			if (subCommodity.compareTo(subCommodityCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			System.out.println(subCommodity == null ? "null" : subCommodity);
			Assert.assertTrue(subCommodity != null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
					subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			// 字段验证
			String error2 = subCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error2, "");
		}
		retrieveNCommodityHistory(commCreated.getID(), comm.getOperatorStaffID(), commodityHistoryMapper);

		return bmList;
	}

	public static Commodity createCommodityViaMapper(Commodity comm, int iUseCaseID) {
		String err = comm.checkCreate(iUseCaseID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> commodityParams = comm.getCreateParamEx(iUseCaseID, comm);
		List<List<BaseModel>> bmList = null;
		// 
		switch (iUseCaseID) {
		case BaseBO.CASE_Commodity_CreateSingle:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createSimpleEx(commodityParams);
			break;
		case BaseBO.CASE_Commodity_CreateComposition:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createCombinationEx(commodityParams);
			break;
		case BaseBO.CASE_Commodity_CreateService:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createServiceEx(commodityParams);
			break;
		case BaseBO.CASE_Commodity_CreateMultiPackaging:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createMultiPackagingEx(commodityParams);
			break;
		default:
			Assert.assertTrue(false, "不存在的创建UseCase");
		}
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(commodityParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				commodityParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Commodity commCreated = null;
		Warehousing warehousing = null;
		if (comm.getnOStart() >= 0 && comm.getPurchasingPriceStart() >= 0) {
			comm.setNO(comm.getnOStart());
			commCreated = (Commodity) bmList.get(5).get(0);
			warehousing = (Warehousing) bmList.get(3).get(0);
		} else {
			commCreated = (Commodity) bmList.get(0).get(0);
		}
		comm.setIgnoreIDInComparision(true);
		comm.setIgnoreSlaveListInComparision(true);
		commCreated.setnOStart(comm.getnOStart());
		commCreated.setPurchasingPriceStart(comm.getPurchasingPriceStart());
		commCreated.setNO(comm.getNO());
		commCreated.setLatestPricePurchase(comm.getLatestPricePurchase()); // 为了通过下面的comm.compareTo(commCreated)，后面不还原
		setCommodityShopInfo(commCreated, comm, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, warehousing);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不一致！");
		}
		//
		int staffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodes = ((Barcodes) bmList.get(1).get(0)).getBarcode();
		commCreated.setBarcodes(barcodes);
		commCreated.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() + ";");
		String error2 = commCreated.checkCreate(iUseCaseID);
		Assert.assertEquals(error2, "");
		commCreated.setOperatorStaffID(staffID);

		if (commCreated.getType() != EnumCommodityType.ECT_Combination.getIndex() && commCreated.getType() != EnumCommodityType.ECT_Service.getIndex()) { // 服务商品没有供应商，所以不需要创建供应商商品
			// 创建一个供应商商品
			ProviderCommodity pc1 = new ProviderCommodity();
			pc1.setProviderID(1);
			pc1.setCommodityID(commCreated.getID());
			Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
			Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			pc1.setIgnoreIDInComparision(true);
			if (pc1.compareTo(createPC) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
		} else if (commCreated.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
			SubCommodity subCommodity = new SubCommodity();
			subCommodity.setCommodityID(commCreated.getID());
			subCommodity.setSubCommodityID(9);
			subCommodity.setSubCommodityNO(1);
			Map<String, Object> subParams = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(subParams);
			Assert.assertTrue(subCommodity != null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
					subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			subCommodity.setIgnoreIDInComparision(true);
			if (subCommodity.compareTo(subCommodityCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			// 字段验证
			String error = subCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
		//
		List<CommodityShopInfo> commodityShopInfoList = createListCommodityShopInfoViaMapper(commCreated, comm, iUseCaseID, Shared.DBName_Test, warehousing);
		commCreated.setListSlave2(commodityShopInfoList);
		commCreated.setListSlave1(comm.getListSlave1()); // 不比较listSlave1
		comm.setIgnoreSlaveListInComparision(false);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不一致！");
		}
		//
		retrieveNCommodityHistory(commCreated.getID(), comm.getOperatorStaffID(), commodityHistoryMapper);
		return commCreated;
	}
	
	public static List<CommodityShopInfo> createListCommodityShopInfoViaMapper(Commodity commCreated, Commodity comm, int iUseCaseID, String dbName, Warehousing warehousing) {
		CommodityShopInfo commodityShopInfo = new CommodityShopInfo();
		commodityShopInfo.setCommodityID(commCreated.getID());
		List<BaseModel> shopList = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店
		Assert.assertTrue(shopList != null && shopList.size() > 0);
		List<CommodityShopInfo> commodityShopInfoList = new ArrayList<>();
		for(BaseModel bm : shopList) {
			Shop shop = (Shop) bm;
			if(shop.getID() != 1) {
				commodityShopInfo.setShopID(shop.getID());
				commodityShopInfo.setLatestPricePurchase(comm.getLatestPricePurchase());
				commodityShopInfo.setPriceRetail(comm.getPriceRetail());
				commodityShopInfo.setnOStart(comm.getnOStart());
				commodityShopInfo.setPurchasingPriceStart(comm.getPurchasingPriceStart());
				commodityShopInfo.setOperatorStaffID(comm.getOperatorStaffID());
				if (comm.getnOStart() >= 0 && comm.getPurchasingPriceStart() >= 0) {
					commodityShopInfo.setNO(comm.getnOStart());
//					Warehousing warehousing = (Warehousing) bmList.get(3).get(0);
					commodityShopInfo.setCurrentWarehousingID(warehousing.getID());
				} 
				String err = commodityShopInfo.checkCreate(iUseCaseID);
				Assert.assertTrue("".equals(err), err);
				Map<String, Object> commodityShopInfoParams = commodityShopInfo.getCreateParam(iUseCaseID, commodityShopInfo);
				DataSourceContextHolder.setDbName(dbName);
				CommodityShopInfo commodityShopInfoCreated = (CommodityShopInfo) commodityShopInfoMapper.create(commodityShopInfoParams);
				if(commodityShopInfoCreated == null) {
					return null;
				}
				commodityShopInfoList.add(commodityShopInfoCreated);
			}
		}
		return commodityShopInfoList;
	}
	
	public static void setCommodityShopInfo(Commodity commCreated, Commodity comm, int iUseCaseID, String dbName, Warehousing warehousing) {
		CommodityShopInfo commodityShopInfo = new CommodityShopInfo();
		commodityShopInfo.setCommodityID(commCreated.getID());
		List<BaseModel> shopList = CacheManager.getCache(dbName, EnumCacheType.ECT_Shop).readN(true, false); // 从缓存中拿到所有门店
		Assert.assertTrue(shopList != null && shopList.size() > 0);
		List<CommodityShopInfo> commodityShopInfoList = new ArrayList<>();
		for(BaseModel bm : shopList) {
			Shop shop = (Shop) bm;
			if(shop.getID() != 1) {
				commodityShopInfo.setShopID(shop.getID());
				commodityShopInfo.setLatestPricePurchase(comm.getLatestPricePurchase());
				commodityShopInfo.setPriceRetail(comm.getPriceRetail());
				commodityShopInfo.setnOStart(comm.getnOStart());
				commodityShopInfo.setPurchasingPriceStart(comm.getPurchasingPriceStart());
				commodityShopInfo.setOperatorStaffID(comm.getOperatorStaffID());
				if (comm.getnOStart() >= 0 && comm.getPurchasingPriceStart() >= 0) {
					commodityShopInfo.setNO(comm.getnOStart());
//					Warehousing warehousing = (Warehousing) bmList.get(3).get(0);
					commodityShopInfo.setCurrentWarehousingID(warehousing.getID());
					commodityShopInfo.setLatestPricePurchase(comm.getPurchasingPriceStart());
				} 
				String err = commodityShopInfo.checkCreate(iUseCaseID);
				Assert.assertTrue("".equals(err), err);
//				Map<String, Object> commodityShopInfoParams = commodityShopInfo.getCreateParam(iUseCaseID, commodityShopInfo);
//				DataSourceContextHolder.setDbName(Shared.DBName_Test);
//				CommodityShopInfo commodityShopInfoCreated = (CommodityShopInfo) commodityShopInfoMapper.create(commodityShopInfoParams);
				commodityShopInfoList.add(commodityShopInfo);
			}
		}
		comm.setListSlave2(commodityShopInfoList);
	}

	public static SubCommodity createSubCommodityViaMapper(SubCommodity data) {
		//
		Map<String, Object> subParams = data.getCreateParam(BaseBO.INVALID_CASE_ID, data);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodity = (SubCommodity) subCommodityMapper.create(subParams);
		//
		System.out.println(subCommodity == null ? "null" : subCommodity);
		Assert.assertTrue(subCommodity != null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		data.setIgnoreIDInComparision(true);
		if (data.compareTo(subCommodity) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 字段验证
		String error = subCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		return subCommodity;
	}

	/** Mapper层创建商品的同步缓存调度数据 */
	public static CommoditySyncCacheDispatcher createCommoditySyncCacheDispatcherViaMapper(CommoditySyncCache csc) throws CloneNotSupportedException, InterruptedException {
		// 添加商品同步缓存调度表数据
		CommoditySyncCacheDispatcher ccd = BaseCommodityTest.DataInput.getCommoditySyncCacheDispatcher();
		ccd.setSyncCacheID(csc.getID());
		Map<String, Object> paramsForCreate = ccd.getCreateParam(BaseBO.INVALID_CASE_ID, ccd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CommoditySyncCacheDispatcher scdCreate = (CommoditySyncCacheDispatcher) commoditySyncCacheDispatcherMapper.create(paramsForCreate); // ...
		//
		ccd.setIgnoreIDInComparision(true);
		if (ccd.compareTo(scdCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(scdCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		String error1 = scdCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		return scdCreate;
	}

	/** Mapper层创建商品的同步缓存 */
	public static List<List<BaseModel>> createCommoditySyncCacheViaMapper(CommoditySyncCache commoditySyncCache) {
		//
		Map<String, Object> paramsForCreate = commoditySyncCache.getCreateParam(BaseBO.INVALID_CASE_ID, commoditySyncCache);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = commoditySyncCacheMapper.createEx(paramsForCreate); // ...
		//
		Assert.assertTrue(list.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		if ((int) paramsForCreate.get(CommoditySyncCache.field.getFIELD_NAME_posID()) > 0) {
			assertTrue(list.size() == 2);
		} else {
			assertTrue(list.size() == 1);
		}
		//
		System.out.println("创建数据成功 ： " + list);

		return list;
	}

	public static Commodity updateViaAction(Commodity commodity, Commodity commUpdate, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, String dbName) throws Exception {
		CommodityHistoryBO commodityHistoryBO = (CommodityHistoryBO) mapBO.get(CommodityHistoryBO.class.getSimpleName());
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodity.getID(), dbName, commodityHistoryBO);
		// 获取多包装商品的修改状况
		List<PackageUnit> lspu = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuCreated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuUpdated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuDeleted = new ArrayList<PackageUnit>();

		PackageUnitBO packageUnitBO = (PackageUnitBO) mapBO.get(PackageUnitBO.class.getSimpleName());
		CommodityBO commodityBO = (CommodityBO) mapBO.get(CommodityBO.class.getSimpleName());
		BarcodesBO barcodesBO = (BarcodesBO) mapBO.get(BarcodesBO.class.getSimpleName());
		Assert.assertTrue(CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, dbName, LogFactory.getLog(BaseCommodityTest.class), packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM),
				"修改的商品包装单位有问题");
		// 获取修改前的多包装信息
		List<Commodity> commList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodity, mapBO);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, session) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);

		PosBO posBO = (PosBO) mapBO.get(PosBO.class.getSimpleName());
		CommoditySyncCacheBO commoditySyncCacheBO = (CommoditySyncCacheBO) mapBO.get(CommoditySyncCacheBO.class.getSimpleName());
		BarcodesSyncCacheBO barcodesSyncCacheBO = (BarcodesSyncCacheBO) mapBO.get(BarcodesSyncCacheBO.class.getSimpleName());
		WarehousingBO warehousingBO = (WarehousingBO) mapBO.get(WarehousingBO.class.getSimpleName());
		CategoryBO categoryBO = (CategoryBO) mapBO.get(CategoryBO.class.getSimpleName());
		WarehousingCommodityBO warehousingCommodityBO = (WarehousingCommodityBO) mapBO.get(WarehousingCommodityBO.class.getSimpleName());
		SubCommodityBO subCommodityBO = (SubCommodityBO) mapBO.get(SubCommodityBO.class.getSimpleName());
		ProviderCommodityBO providerCommodityBO = (ProviderCommodityBO) mapBO.get(ProviderCommodityBO.class.getSimpleName());
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, commList, dbName, null);

		return (Commodity) Shared.parse1Object(mrl, commUpdate, BaseAction.KEY_Object);
	}

	/** Mapper层修改商品 */
	public static Commodity updateCommodityViaMapper(Commodity c) {
		//
		String error = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate = c.getUpdateParam(BaseBO.INVALID_CASE_ID, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase = (Commodity) commodityMapper.update(paramsForUpdate);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNotNull(updateCommodityCase);
		//
		c.setIgnoreIDInComparision(true);
		// 更新后对比数据不一致返回-1，一致返回0
		c.setIgnoreSlaveListInComparision(true);
		if (c.compareTo(updateCommodityCase) != 0) {
			Assert.assertTrue(false, "对象的字段与DB读出的不相等");
		}
		return updateCommodityCase;
	}

	/** 修改商品价格的方法 */
	public static void updateCommodityPrice(Commodity commodity) {
		//
		Map<String, Object> param = commodity.getUpdateParam(BaseBO.CASE_Commodity_UpdatePrice, commodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updatePrice = (Commodity) commodityMapper.updatePrice(param);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNotNull(updatePrice);
		//
		if (!(commodity.getID() == updatePrice.getID() && Math.abs(GeneralUtil.sub(commodity.getPriceRetail(), updatePrice.getPriceRetail())) < BaseModel.TOLERANCE)) {
			Assert.assertTrue(false, "商品价格修改失败！！！");
		}
	}

	/** 从Action层删除商品，并运行检查点 */
	public static void deleteCommodityViaAction(Commodity commodity, String dbName, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO) throws Exception, UnsupportedEncodingException {
		// 把BO从Map里拿出来
		CommodityHistoryBO commodityHistoryBO = (CommodityHistoryBO) mapBO.get(CommodityHistoryBO.class.getSimpleName());
		PosBO posBO = (PosBO) mapBO.get(PosBO.class.getSimpleName());
		CommoditySyncCacheBO commoditySyncCacheBO = (CommoditySyncCacheBO) mapBO.get(CommoditySyncCacheBO.class.getSimpleName());
		BarcodesSyncCacheBO barcodesSyncCacheBO = (BarcodesSyncCacheBO) mapBO.get(BarcodesSyncCacheBO.class.getSimpleName());
		BarcodesBO barcodesBO = (BarcodesBO) mapBO.get(BarcodesBO.class.getSimpleName());
		CommodityBO commodityBO = (CommodityBO) mapBO.get(CommodityBO.class.getSimpleName());
		ProviderCommodityBO providerCommodityBO = (ProviderCommodityBO) mapBO.get(ProviderCommodityBO.class.getSimpleName());
		PackageUnitBO packageUnitBO = (PackageUnitBO) mapBO.get(PackageUnitBO.class.getSimpleName());
		CategoryBO categoryBO = (CategoryBO) mapBO.get(CategoryBO.class.getSimpleName());
		// 把commodity的barcode查出来做结果验证
		List<Barcodes> listBarcodes = queryBarcodesList(commodity, mvc, session);
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodity.getID(), dbName, commodityHistoryBO);
		//
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + commodity.getID() + "&" + Commodity.field.getFIELD_NAME_type() + "=" + commodity.getType()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		CommodityCP.verifyDelete(commodity, listBarcodes, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, dbName);

	}

	/** Mapper层删除组合商品的子商品，由于组合型商品没有R1，故没有做删除后的验证 */
	public static void deleteSubCommodityViaMapper(SubCommodity data) {
		//
		Map<String, Object> params = data.getDeleteParam(BaseBO.INVALID_CASE_ID, data);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		subCommodityMapper.delete(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	/** 从Mapper层删除商品，并进行结果验证 */
	public static void deleteCommodityViaMapper(Commodity comm, EnumErrorCode eec) {
		System.out.println("需要删除的商品为：" + comm);
		comm.setOperatorStaffID(comm.getOperatorStaffID() != 0 ? comm.getOperatorStaffID() : STAFF_ID3);
		Map<String, Object> paramsForDelete = comm.getDeleteParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		if (comm.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
			// 删除组合商品
			commodityMapper.deleteCombination(paramsForDelete);
		} else if (comm.getType() == EnumCommodityType.ECT_MultiPackaging.getIndex()) {
			// 删除多包装商品
			commodityMapper.deleteMultiPackaging(paramsForDelete);
		} else if (comm.getType() == EnumCommodityType.ECT_Service.getIndex()) {
			// 删除服务商品
			commodityMapper.deleteService(paramsForDelete);
		} else {
			// 删除单品
			commodityMapper.deleteSimple(paramsForDelete);
		}
		// 结果验证：判断错误码
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == eec, paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		if(eec == EnumErrorCode.EC_NoError) {
			// 结果验证：查询被删除的商品
			retrieve1ExCommodity(comm, EnumErrorCode.EC_NoSuchData);
			// 查询商品历史
			CommodityHistory ch = new CommodityHistory();
			ch.setCommodityID(comm.getID());
			Map<String, Object> params = ch.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> list2 = commodityHistoryMapper.retrieveN(params);
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == eec, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			assertTrue(list2.size() > 0);
			for (BaseModel bm : list2) {
				CommodityHistory commodityHistory = (CommodityHistory) bm;
				if ("".equals(commodityHistory.getNewValue())) {
					Assert.assertTrue(commodityHistory.getCommodityID() == ch.getCommodityID());
					Assert.assertTrue(commodityHistory.getStaffID() == comm.getOperatorStaffID());
				}
			}
			// 查询供应商商品表是否有对应删除该商品
			ProviderCommodity pc = new ProviderCommodity();
			pc.setCommodityID(comm.getID());
			Map<String, Object> params2 = pc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pc);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> pcRetrieveN = providerCommodityMapper.retrieveN(params2);
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(pcRetrieveN.size() == 0);
		}
	}

	/** 删除全部商品同步缓存数据 */
	public static void deleteAllCommoditySyncCache() {
		//
		Map<String, Object> map = new HashMap<>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commoditySyncCacheMapper.deleteAll(map);
	}

	public static Commodity retrieve1ExCommodity(Commodity comm, EnumErrorCode errCode) {
		//
		Map<String, Object> paramsRetrieve1Ex = comm.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> commRetrivedEx = commodityMapper.retrieve1Ex(paramsRetrieve1Ex);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve1Ex.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errCode, paramsRetrieve1Ex.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (errCode != EnumErrorCode.EC_NoError) {
			return null;
		}
		return Commodity.fetchCommodityFromResultSet(commRetrivedEx);
	}

	public static List<BaseModel> retrieveNCommodity(String queryKeyword, int iUseCaseID) {
		//
		Commodity queryConditionCommodity = new Commodity();
		queryConditionCommodity.setType(BaseAction.INVALID_ID); // -1查找全部类型的商品
		queryConditionCommodity.setQueryKeyword(queryKeyword);
		//
		Map<String, Object> retrieveNParam = queryConditionCommodity.getRetrieveNParam(iUseCaseID, queryConditionCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> commList = commodityMapper.retrieveN(retrieveNParam);
		assertTrue(commList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "根据条形码搜索商品失败");

		return commList;
	}

	public static List<BaseModel> retrieveNViaMapper(Commodity comm, String dbName) {
		Map<String, Object> param = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> commList = commodityMapper.retrieveN(param);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "搜索商品失败");
		return commList;
	}

	public static void retrieveNCommodityHistory(int commodityID, int staffID, CommodityHistoryMapper commodityHistoryMapper) {
		// 查询商品历史
		CommodityHistory ch = new CommodityHistory();
		ch.setCommodityID(commodityID);
		Map<String, Object> params2 = ch.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list2 = commodityHistoryMapper.retrieveN(params2);
		//
		assertTrue(list2.size() > 0);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (BaseModel bm : list2) {
			CommodityHistory commodityHistory = (CommodityHistory) bm;
			Assert.assertTrue(commodityHistory.getCommodityID() == ch.getCommodityID());
			// 这里查出来的10条商品历史有可能有其他staff操作过该商品的历史，这里循环判断有可能会出错。
			// Assert.assertTrue(commodityHistory.getStaffID() == staffID);
			Assert.assertTrue(commodityHistory.getStaffID() != 0);
		}
	}

	@SuppressWarnings("unchecked")
	public static int queryCommodityHistorySize(int CommodityID, String dbName, CommodityHistoryBO commodityHistoryBO) {
		// 获取该商品修改前的商品历史数量
		CommodityHistory commodityHistory = new CommodityHistory();
		commodityHistory.setCommodityID(CommodityID);
		commodityHistory.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		DataSourceContextHolder.setDbName(dbName);

		List<CommodityHistory> listCommodityHistory = (List<CommodityHistory>) commodityHistoryBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, commodityHistory);
		if (commodityHistoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取CommodityHistory DB数据错误！");
		}

		return listCommodityHistory.size();
	}

	public static Commodity queryCommodityCache(int commodityID) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityCache = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取商品失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		return commodityCache;
	}

	@SuppressWarnings("unchecked")
	public static List<Commodity> queryMultiPackagingCommodityListViaAction(Commodity simpleCommodity, Map<String, BaseBO> mapBO) {
		// 从Map里拿出BO
		CommodityBO commodityBO = (CommodityBO) mapBO.get(CommodityBO.class.getSimpleName());
		BarcodesBO barcodesBO = (BarcodesBO) mapBO.get(BarcodesBO.class.getSimpleName());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, simpleCommodity);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		// 查找多包装商品的条形码
		for (Commodity comm : listMultiPackageCommodity) {
			Barcodes barcodes = new Barcodes();
			barcodes.setCommodityID(comm.getID());
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<?> retrieveNObject = barcodesBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, barcodes);
			if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "获取多包装商品的条形码DB数据错误！");
			}
			assertTrue(retrieveNObject.size() == 1, "没查找到多包装商品的条形码数据或者该多包装商品有多个条形码。多包装商品为" + comm);
			barcodes = (Barcodes) retrieveNObject.get(0);
			comm.setBarcodeID(barcodes.getID());
			comm.setBarcodes(barcodes.getBarcode());
		}

		return listMultiPackageCommodity;
	}

	public static List<BaseModel> queryMultiPackagingCommodityListViaMapper(Commodity simpleCommodity) throws Exception {
		Map<String, Object> params = simpleCommodity.getRetrieveNParam(BaseBO.CASE_RetrieveNMultiPackageCommodity, simpleCommodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNMultiPackageCommodity = commodityMapper.retrieveNMultiPackageCommodity(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(retrieveNMultiPackageCommodity.size() != 0, "查询不到数据");

		return retrieveNMultiPackageCommodity;
	}

	public static List<Barcodes> queryBarcodesList(Commodity commodity, MockMvc mvc, HttpSession session) throws Exception, UnsupportedEncodingException {
		MvcResult mr12 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr12);
		JSONObject jsonObject = JSONObject.fromObject(mr12.getResponse().getContentAsString());
		List<?> Barcodes = JsonPath.read(jsonObject, "$.barcodesList[*]");
		List<Barcodes> listBarcodes = new ArrayList<Barcodes>();
		Barcodes barcodes = new Barcodes();
		for (int i = 0; i < Barcodes.size(); i++) {
			barcodes = (Barcodes) barcodes.parse1(Barcodes.get(i).toString());
			listBarcodes.add(barcodes);
		}
		return listBarcodes;
	}

	private static void Doctor_checkWarehousing(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkWarehousing(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的商品的入库单商品的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的商品的入库单商品的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private static void Doctor_checkType(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();
		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkNO(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查商品库存数量的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查商品库存数量的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private static void Doctor_checkNO(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkType(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的商品类型的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的商品类型的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private static void Doctor_checkStatus(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkStatus(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的商品状态的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的商品状态的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private static void Doctor_checkReturnCommoditySheet(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkReturnCommoditySheet(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的退货单商品的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的退货单商品的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private static void Doctor_checkInventory(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkInventory(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的盘点单商品的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的盘点单商品的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private static void Doctor_checkCategoryID(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkCategoryID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的类别ID的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的类别ID的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private static void Doctor_checkBrandID(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkBrandID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的品牌ID的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的品牌ID的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public static void Doctor_checkPurchasingOrder(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkPurchasingOrder(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的采购单位的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的采购单位的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public static void Doctor_checkSubCommodity(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkSubCommodity(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查组合商品的子商品的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查组合商品的子商品的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public static void Doctor_checkProvider(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkProvider(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查供应商的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查供应商的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public static void Doctor_checkCreate(CommodityMapper commodityMapper) {
		Shared.printTestClassEndInfo();

		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_ID);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(-1);
		comm.setBrandID(-1);
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		String error = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
		if (!error.equals("")) {
			System.out.println("对商品进行RN时，传入的的字段数据校验出现异常");
			System.out.println("comm=" + (comm == null ? null : comm));
		}
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (list.size() == 0 || list == null) {
			System.out.println("RN读出的商品信息为空");
		}
		for (BaseModel bm : list) {
			Commodity c = (Commodity) bm;

			String error1 = "";
			switch (c.getType()) {
			case 1:
				error1 = c.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				break;
			case 2:
				error1 = c.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				break;
			case 3:
				error1 = c.checkCreate(BaseBO.CASE_Commodity_CreateService);
				break;
			default:
				error1 = c.checkCreate(BaseBO.INVALID_CASE_ID);
				break;
			}
			if (!error1.equals("")) {
				System.out.println(c.getID() + "号商品数据验证出现异常");
				System.out.println("c=" + (c == null ? null : c));
			}
		}
	}

	public static Commodity getCommodityCache(int commodityID, String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取商品失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		return commodityCache;
	}

	public static Barcodes retrieveNBarcodesViaMapper(Commodity comm) {
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(comm.getID());
		barcodes.setBarcode(comm.getBarcodes());
		barcodes.setOperatorStaffID(STAFF_ID3);
		String err = barcodes.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> params = barcodes.getRetrieveNParam(BaseBO.INVALID_CASE_ID, barcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = barcodesMapper.retrieveN(params);
		//
		Assert.assertTrue(list.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (BaseModel bm : list) {
			Barcodes barcodes2 = (Barcodes) bm;
			barcodes2.setOperatorStaffID(barcodes.getOperatorStaffID()); // 为了通过下面断言
			err = barcodes2.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		return (Barcodes) list.get(0);
	}

	public static Commodity createCommodityViaMapperWithoutCreateSubCommodity(Commodity comm, int iUseCaseID) {
		String err = comm.checkCreate(iUseCaseID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> commodityParams = comm.getCreateParamEx(iUseCaseID, comm);
		List<List<BaseModel>> bmList = null;
		switch (iUseCaseID) {
		case BaseBO.CASE_Commodity_CreateSingle:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createSimpleEx(commodityParams);
			break;
		case BaseBO.CASE_Commodity_CreateComposition:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createCombinationEx(commodityParams);
			break;
		case BaseBO.CASE_Commodity_CreateService:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createServiceEx(commodityParams);
			break;
		case BaseBO.CASE_Commodity_CreateMultiPackaging:
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			bmList = commodityMapper.createMultiPackagingEx(commodityParams);
			break;
		default:
			Assert.assertTrue(false, "不存在的创建UseCase");
		}
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(commodityParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				commodityParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Commodity commCreated = null;
		Warehousing warehousing = null;
		if (comm.getnOStart() >= 0 && comm.getPurchasingPriceStart() >= 0) {
			comm.setNO(comm.getnOStart());
			commCreated = (Commodity) bmList.get(5).get(0);
			warehousing = (Warehousing) bmList.get(3).get(0);
		} else {
			commCreated = (Commodity) bmList.get(0).get(0);
		}
		comm.setIgnoreIDInComparision(true);
		comm.setIgnoreSlaveListInComparision(true);
		commCreated.setnOStart(comm.getnOStart());
		commCreated.setPurchasingPriceStart(comm.getPurchasingPriceStart());
		commCreated.setNO(comm.getNO());
		commCreated.setLatestPricePurchase(comm.getLatestPricePurchase()); // 为了通过下面的comm.compareTo(commCreated)，后面不还原
		setCommodityShopInfo(commCreated, comm, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, warehousing);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不一致！");
		}
		//
		int staffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodes = ((Barcodes) bmList.get(1).get(0)).getBarcode();
		commCreated.setBarcodes(barcodes);
		commCreated.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() + ";");
		String error2 = commCreated.checkCreate(iUseCaseID);
		Assert.assertEquals(error2, "");
		commCreated.setOperatorStaffID(staffID);

		if (commCreated.getType() != EnumCommodityType.ECT_Combination.getIndex() && commCreated.getType() != EnumCommodityType.ECT_Service.getIndex()) { // 服务商品没有供应商，所以不需要创建供应商商品
			// 创建一个供应商商品
			ProviderCommodity pc1 = new ProviderCommodity();
			pc1.setProviderID(1);
			pc1.setCommodityID(commCreated.getID());
			Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
			Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			pc1.setIgnoreIDInComparision(true);
			if (pc1.compareTo(createPC) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
		} else if (commCreated.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
		}
		List<CommodityShopInfo> commodityShopInfoList = createListCommodityShopInfoViaMapper(commCreated, comm, iUseCaseID, Shared.DBName_Test, warehousing);
		commCreated.setListSlave2(commodityShopInfoList);
		commCreated.setListSlave1(comm.getListSlave1()); // 不比较listSlave1
		comm.setIgnoreSlaveListInComparision(false);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不一致！");
		}
		retrieveNCommodityHistory(commCreated.getID(), comm.getOperatorStaffID(), commodityHistoryMapper);

		return commCreated;
	}

	@SuppressWarnings("unchecked")
	public static Barcodes retrieveNBarcodesViaAction(Commodity commCreate, MockMvc mvc, HttpSession session) throws Exception, UnsupportedEncodingException {
		MvcResult mrl2 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?commodityID=" + commCreate.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl2);

		JSONObject o2 = JSONObject.fromObject(mrl2.getResponse().getContentAsString());
		JSONArray barJSONArray = o2.getJSONArray("barcodesList");
		Barcodes barcodes = new Barcodes();
		List<Barcodes> list = (List<Barcodes>) barcodes.parseN(barJSONArray);
		for (Barcodes b : list) {
			assertTrue(b.getCommodityID() == commCreate.getID());
		}

		return list.get(0);
	}

	public static MvcResult uploadPicture(String path, String picturePath, String pictureType, MockMvc mvc, HttpSession session) throws FileNotFoundException, IOException, Exception {
		File file = new File(path + picturePath);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, pictureType, fis);

		Shared.caseLog("商品创建时上传图片");
		MvcResult mr1 = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) session) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr1);

		Assert.assertNotNull(session.getAttribute(EnumSession.SESSION_CommodityPictureDestination.getName()));
		Assert.assertNotNull(session.getAttribute(EnumSession.SESSION_PictureFILE.getName()), "图片文件不存在！");
		return mr1;
	}

	/**
	 * @param bmList
	 *            RN返回的需要同步的商品List
	 */
	public static List<Commodity> getCommodityListIfAllSync(Map<String, BaseBO> mapBO, List<BaseModel> bmList) { // ..
		PosBO posBO = (PosBO) mapBO.get(PosBO.class.getSimpleName());
		List<Pos> posList = Shared.getPosesFromDB(posBO, Shared.DBName_Test);
		List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(Shared.DBName_Test, EnumSyncCacheType.ESCT_CommoditySyncInfo).readN(false, false);
		List<Commodity> commList = new ArrayList<Commodity>();

		BaseSyncCache baseSyncCache = new BaseSyncCache();
		for (BaseModel bm : bmList) {
			Commodity commodity = (Commodity) bm;

			// 获取该商品的同步块信息
			for (BaseModel bm2 : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bm2;
				if (baseSyncCache.getSyncData_ID() == commodity.getID()) {
					if (baseSyncCache.getListSlave1() != null && baseSyncCache.getListSlave1().size() == (posList.size() - 1)) {
						commodity.setIsSync(1); // 表示该商品同步块已经完全同步
					}
				}
			}
			commList.add(commodity);
		}

		return commList;
	}
	
	public static List<BaseModel> getListCommodityShopInfoByCommID (Commodity commCreated) throws CloneNotSupportedException {
		CommodityShopInfo commodityShopInfo = BaseCommodityTest.DataInput.getCommodityShopInfo();
		commodityShopInfo.setCommodityID(commCreated.getID());
		commodityShopInfo.setShopID(commCreated.getShopID());
		Map<String, Object> paramsCommShopInfoRN = commodityShopInfo.getRetrieveNParam(BaseBO.INVALID_CASE_ID, commodityShopInfo);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listCommodityShopInfo = commodityShopInfoMapper.retrieveN(paramsCommShopInfoRN);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		return listCommodityShopInfo;
	}
	
	public static List<BaseModel> getListCommodityShopInfoByCommID (Commodity commCreated, String dbName, int shopID) throws CloneNotSupportedException {
		CommodityShopInfo commodityShopInfo = BaseCommodityTest.DataInput.getCommodityShopInfo();
		commodityShopInfo.setCommodityID(commCreated.getID());
		commodityShopInfo.setShopID(shopID);
		Map<String, Object> paramsCommShopInfoRN = commodityShopInfo.getRetrieveNParam(BaseBO.INVALID_CASE_ID, commodityShopInfo);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> listCommodityShopInfo = commodityShopInfoMapper.retrieveN(paramsCommShopInfoRN);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		return listCommodityShopInfo;
	}
}
