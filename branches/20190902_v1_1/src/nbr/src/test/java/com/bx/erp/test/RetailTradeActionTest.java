package com.bx.erp.test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.apache.maven.model.ModelBase;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.RetailTradeAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.commodity.CommoditySyncAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.Company;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Vip;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RealTimeSalesStatisticsToday;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.scheduledTask.SalesStatistics;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.test.checkPoint.RetailTradeCP;
import com.bx.erp.test.commodity.CommoditySyncActionTest;
import com.bx.erp.test.staff.BaseShopTest;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class RetailTradeActionTest extends BaseActionTest {
	private static final int VIP_ID = 1;
	private static final int NORMAL_COMMODIY_ID1 = 1;
	private static final int NORMAL_COMMODIY_ID2 = 2;
	private static final int NORMAL_COMMODIY_ID3 = 3;
	private static final int Multi_COMMODIY_ID1 = 51;
	private static final int Multi_COMMODIY_ID2 = 52;
	private static final int Multi_COMMODIY_ID3 = 53;
	private static final int COMBINATION_COMMODIY_ID1 = 45;
	private static final int COMBINATION_COMMODIY_ID2 = 46;
	private static final int COMBINATION_COMMODIY_ID3 = 48;
	private static final int SERVICE_COMMODIY_ID1 = 166;
	private static final int SERVICE_COMMODIY_ID2 = 167;
	private static final int SERVICE_COMMODIY_ID3 = 168;
	private static final int NOT_EXIST_COMMODIY_ID = 9999999;
	private static Coupon coupon;

	private final String commIDs = "commIDs";
	private final String commNOs = "commNOs";
	private final String commPrices = "commPrices";
	private final String amounts = "amounts";
	private final String barcodeIDs = "barcodeIDs";

	private List<PackageUnit> lspu;
	private List<PackageUnit> lspuCreated;
	private List<PackageUnit> lspuUpdated;
	private List<PackageUnit> lspuDeleted;

	private Random random = new Random();
	/** 被退货的零售单的ID。值为-1或>0的整数 */
	private int iSourceRetailTradeID = -1;

	private HttpSession sessionBossOfAnotherCompany;
	private Vip vipOfAnotherCompany;
	private Company anotherCompany;

	@BeforeClass
	public void setup() {
		super.setUp();

		// 由于本测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
		ConfigCacheSize retailTradeConfigCacheSize = new ConfigCacheSize();
		retailTradeConfigCacheSize.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		retailTradeConfigCacheSize.setID(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex());
		retailTradeConfigCacheSize.setName(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(retailTradeConfigCacheSize, Shared.DBName_Test, STAFF_ID4);
		//
		ConfigCacheSize commodityConfigCacheSize = new ConfigCacheSize();
		commodityConfigCacheSize.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		commodityConfigCacheSize.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		commodityConfigCacheSize.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(commodityConfigCacheSize, Shared.DBName_Test, STAFF_ID4);
		System.out.println("----------缓存：" + CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).readN(false, false));
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();

		// 由于本测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
		ConfigCacheSize retailTradeConfigCacheSize = new ConfigCacheSize();
		retailTradeConfigCacheSize.setValue("50");
		retailTradeConfigCacheSize.setID(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex());
		retailTradeConfigCacheSize.setName(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(retailTradeConfigCacheSize, Shared.DBName_Test, STAFF_ID4);
		//
		ConfigCacheSize commodityConfigCacheSize = new ConfigCacheSize();
		commodityConfigCacheSize.setValue("100");
		commodityConfigCacheSize.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		commodityConfigCacheSize.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(commodityConfigCacheSize, Shared.DBName_Test, STAFF_ID4);
	}

	private Coupon createCoupon() {
		if (coupon == null) {
			try {
				coupon = BaseCouponTest.DataInput.getCoupon();
				// 因为很多测试需要用到该优惠券，并且都用到了id为1的VIP，所以personalLimit必须足够大
				// 运行整个类的测试的时候才不会出现超过会员领取的优惠券大于personalLimit
				coupon.setPersonalLimit(1000);
				coupon = BaseCouponTest.createViaMapper(coupon);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "getCoupon() error!");
			}
		}
		return coupon;
	}

	private RetailTrade createRetailTrade(int ID1, int ID2, int ID3) throws Exception {
		// 生成零售单
		RetailTrade rt = getRetailTrade(ID1, ID2, ID3);
		Company company = (Company) sessionBoss.getAttribute(EnumSession.SESSION_Company.getName());
		Map<Integer, Commodity> simpleCommodityList = null;
		boolean bIncludeDeletedCommodity = false;
		if (rt.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity = true;
		}
		simpleCommodityList = getSimpleCommodityListFromRetailTrade(rt, company.getDbName(), bIncludeDeletedCommodity, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale = queryWarehousingBeforeSale(simpleCommodityList, company.getDbName(), rt, warehousingBO);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());

		RetailTrade bmOfDB = new RetailTrade();
		bmOfDB = (RetailTrade) bmOfDB.parse1(returnJson.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);

		Map<Integer, List<Warehousing>> mapAfterSale = queryWarehousingAfterSale(mapBeforeSale, company.getDbName(), warehousingBO);
		if (rt.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		// 检查点
		RetailTradeCP.verifyCreate(bmOfDB, rt, mapBeforeSale, mapAfterSale, simpleCommodityList, mapBO, Shared.DBName_Test, true);

		return bmOfDB;
	}

	public RetailTrade getRetailTrade(int ID1, int ID2, int ID3) throws InterruptedException {
		Random ran = new Random();

		RetailTrade retailTrade = new RetailTrade();
		retailTrade.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
		Thread.sleep(1000);
		retailTrade.setVipID(1);
		retailTrade.setPos_ID(5);
		retailTrade.setSn(Shared.generateRetailTradeSN(5));
		retailTrade.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
		retailTrade.setSaleDatetime(new Date());
		retailTrade.setLogo("11" + ran.nextInt(1000));
		retailTrade.setStaffID(4);
		retailTrade.setPaymentType(1);
		retailTrade.setPaymentAccount("12");
		retailTrade.setRemark("11111");
		retailTrade.setSourceID(-1);
		retailTrade.setAmount(2222.200000d);
		retailTrade.setAmountCash(2222.200000d);
		retailTrade.setSmallSheetID(1);
		retailTrade.setSyncDatetime(new Date());
		retailTrade.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		retailTrade.setWxOrderSN("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTrade.setAliPayOrderSN("zfb" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTrade.setWxRefundNO(String.valueOf(System.currentTimeMillis()).substring(6));
		retailTrade.setDatetimeStart(new Date());
		retailTrade.setDatetimeEnd(new Date());
		retailTrade.setShopID(2);
		Thread.sleep(1000);

		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(ID1);
		retailTradeCommodity.setNO(10);
		retailTradeCommodity.setPriceOriginal(20);
		retailTradeCommodity.setPriceReturn(20);
		retailTradeCommodity.setBarcodeID(1);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setPriceVIPOriginal(10);
		retailTradeCommodity.setPriceSpecialOffer(10);

		RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
		retailTradeCommodity2.setCommodityID(ID2);
		retailTradeCommodity2.setNO(10);
		retailTradeCommodity2.setPriceOriginal(20);
		retailTradeCommodity2.setPriceReturn(20);
		retailTradeCommodity2.setBarcodeID(3);
		retailTradeCommodity2.setNOCanReturn(10);
		retailTradeCommodity2.setPriceVIPOriginal(10);
		retailTradeCommodity2.setPriceSpecialOffer(10);

		RetailTradeCommodity retailTradeCommodity3 = new RetailTradeCommodity();
		retailTradeCommodity3.setCommodityID(ID3);
		retailTradeCommodity3.setNO(10);
		retailTradeCommodity3.setPriceOriginal(20);
		retailTradeCommodity3.setPriceReturn(20);
		retailTradeCommodity3.setBarcodeID(5);
		retailTradeCommodity3.setNOCanReturn(10);
		retailTradeCommodity3.setPriceVIPOriginal(10);
		retailTradeCommodity3.setPriceSpecialOffer(10);

		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodity.add(retailTradeCommodity);
		listRetailTradeCommodity.add(retailTradeCommodity2);
		listRetailTradeCommodity.add(retailTradeCommodity3);

		retailTrade.setListSlave1(listRetailTradeCommodity);

		return retailTrade;
	}
	
	private RetailTrade createRetailTrade(RetailTrade rt, int ID1, int ID2, int ID3) throws Exception {
		// 生成零售单
		rt = getRetailTrade(rt, ID1, ID2, ID3);
		Company company = (Company) sessionBoss.getAttribute(EnumSession.SESSION_Company.getName());
		Map<Integer, Commodity> simpleCommodityList = null;
		boolean bIncludeDeletedCommodity = false;
		if (rt.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity = true;
		}
		simpleCommodityList = getSimpleCommodityListFromRetailTrade(rt, company.getDbName(), bIncludeDeletedCommodity, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale = queryWarehousingBeforeSale(simpleCommodityList, company.getDbName(), rt, warehousingBO);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());

		RetailTrade bmOfDB = new RetailTrade();
		bmOfDB = (RetailTrade) bmOfDB.parse1(returnJson.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);

		Map<Integer, List<Warehousing>> mapAfterSale = queryWarehousingAfterSale(mapBeforeSale, company.getDbName(), warehousingBO);
		if (rt.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		// 检查点
		RetailTradeCP.verifyCreate(bmOfDB, rt, mapBeforeSale, mapAfterSale, simpleCommodityList, mapBO, Shared.DBName_Test, true);

		return bmOfDB;
	}

	public RetailTrade getRetailTrade(RetailTrade retailTrade, int ID1, int ID2, int ID3) throws InterruptedException {
		Random ran = new Random();

//		RetailTrade retailTrade = new RetailTrade();
		retailTrade.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
		Thread.sleep(1000);
		retailTrade.setVipID(1);
		retailTrade.setPos_ID(5);
		retailTrade.setSn(Shared.generateRetailTradeSN(5));
		retailTrade.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
		retailTrade.setSaleDatetime(new Date());
		retailTrade.setLogo("11" + ran.nextInt(1000));
		retailTrade.setStaffID(4);
		retailTrade.setPaymentType(1);
		retailTrade.setPaymentAccount("12");
		retailTrade.setRemark("11111");
		retailTrade.setSourceID(-1);
		retailTrade.setAmount(2222.200000d);
		retailTrade.setAmountCash(2222.200000d);
		retailTrade.setSmallSheetID(1);
		retailTrade.setSyncDatetime(new Date());
		retailTrade.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		retailTrade.setWxOrderSN("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTrade.setAliPayOrderSN("zfb" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTrade.setWxRefundNO(String.valueOf(System.currentTimeMillis()).substring(6));
		retailTrade.setDatetimeStart(new Date());
		retailTrade.setDatetimeEnd(new Date());
//		retailTrade.setShopID(2);
		Thread.sleep(1000);

		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(ID1);
		retailTradeCommodity.setNO(10);
		retailTradeCommodity.setPriceOriginal(20);
		retailTradeCommodity.setPriceReturn(20);
		retailTradeCommodity.setBarcodeID(1);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setPriceVIPOriginal(10);
		retailTradeCommodity.setPriceSpecialOffer(10);

		RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
		retailTradeCommodity2.setCommodityID(ID2);
		retailTradeCommodity2.setNO(10);
		retailTradeCommodity2.setPriceOriginal(20);
		retailTradeCommodity2.setPriceReturn(20);
		retailTradeCommodity2.setBarcodeID(3);
		retailTradeCommodity2.setNOCanReturn(10);
		retailTradeCommodity2.setPriceVIPOriginal(10);
		retailTradeCommodity2.setPriceSpecialOffer(10);

		RetailTradeCommodity retailTradeCommodity3 = new RetailTradeCommodity();
		retailTradeCommodity3.setCommodityID(ID3);
		retailTradeCommodity3.setNO(10);
		retailTradeCommodity3.setPriceOriginal(20);
		retailTradeCommodity3.setPriceReturn(20);
		retailTradeCommodity3.setBarcodeID(5);
		retailTradeCommodity3.setNOCanReturn(10);
		retailTradeCommodity3.setPriceVIPOriginal(10);
		retailTradeCommodity3.setPriceSpecialOffer(10);

		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodity.add(retailTradeCommodity);
		listRetailTradeCommodity.add(retailTradeCommodity2);
		listRetailTradeCommodity.add(retailTradeCommodity3);

		retailTrade.setListSlave1(listRetailTradeCommodity);

		return retailTrade;
	}

	@SuppressWarnings("unchecked")
	private List<Commodity> getCommodityListBeforeSold(RetailTrade retailTrade) {
		List<Commodity> commList = new ArrayList<Commodity>();
		Commodity commodity = new Commodity();
		ErrorInfo ecOut = new ErrorInfo();
		for (Object o : retailTrade.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;

			commodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(rtc.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (commodity == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查询商品缓存失败，错误码：" + ecOut.getErrorCode());
			}

			List<Commodity> listSlave2 = new ArrayList<Commodity>();
			if (commodity.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				Commodity multiCommodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodity.getRefCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
				if (multiCommodity == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					assertTrue(false, "查询商品缓存失败，错误码：" + ecOut.getErrorCode());
				}
				// 将多包装的单品信息放到listSlave2中
				listSlave2.add(multiCommodity);
				commodity.setListSlave2(listSlave2);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
				// 获取组合商品子商品
				SubCommodity subCommodity = new SubCommodity();
				subCommodity.setCommodityID(commodity.getID());
				//
				DataSourceContextHolder.setDbName(Shared.DBName_Test);
				List<SubCommodity> listSubCommodity = (List<SubCommodity>) subCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, subCommodity);
				if (subCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					assertTrue(false, "查询子商品失败，错误码：" + retailTradeCommoditySourceBO.getLastErrorCode());
				}

				// 查询到组合商品的子商品，将单品信息放到listSlave2中
				for (SubCommodity sc : listSubCommodity) {
					Commodity subCommodityOfCache = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(sc.getSubCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
					if (subCommodityOfCache == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
						assertTrue(false, "查询商品缓存失败，错误码：" + ecOut.getErrorCode());
					}

					listSlave2.add(subCommodityOfCache);
				}
				commodity.setListSlave2(listSlave2);
			}
			commList.add(commodity);
		}
		return commList;
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常创建零售单，购买3件普通商品");

		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:正常创建零售单，购买3件多包装");

		RetailTrade rt = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:正常创建零售单，购买3件组合商品");

		RetailTrade rt = getRetailTrade(COMBINATION_COMMODIY_ID1, COMBINATION_COMMODIY_ID2, COMBINATION_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:正常创建零售单，购买2件普通，一件多包装");

		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, Multi_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:正常创建零售单，购买2件普通，一件组合");

		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, COMBINATION_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:正常创建零售单，购买2件多包装，一件组合");

		RetailTrade rt = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, COMBINATION_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:正常创建零售单，购买3件服务商品");

		RetailTrade rt = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:正常创建零售单，购买服务商品，普通商品，多包装商品");

		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, Multi_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9:正常创建零售单，购买服务商品，组合商品，多包装商品");

		RetailTrade rt = getRetailTrade(COMBINATION_COMMODIY_ID1, Multi_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10:对普通商品进行退货");

		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11:对多包装商品进行退货");

		RetailTrade rt = createRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		RetailTrade returnRetailTrade = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());

		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
		Assert.assertTrue(bmOfDB.getSn().equals(rt.getSn() + "_1"), "创建出来的退货单的单号和想退货的零售单单号不一致");
	}

	@Test
	public void testCreateEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12:对组合商品进行退货");

		RetailTrade rt = createRetailTrade(COMBINATION_COMMODIY_ID1, COMBINATION_COMMODIY_ID2, COMBINATION_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(COMBINATION_COMMODIY_ID1, COMBINATION_COMMODIY_ID2, COMBINATION_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13:对普通商品和多包装商品进行退货");

		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, Multi_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, Multi_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14:对多包装和组合商品进行退货");

		RetailTrade rt = createRetailTrade(COMBINATION_COMMODIY_ID1, COMBINATION_COMMODIY_ID2, Multi_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(COMBINATION_COMMODIY_ID1, COMBINATION_COMMODIY_ID2, Multi_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15:对普通商品和组合商品进行退货");

		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, COMBINATION_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, COMBINATION_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16:对普通商品和多包装和组合商品进行退货");

		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, Multi_COMMODIY_ID2, COMBINATION_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, Multi_COMMODIY_ID2, COMBINATION_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17:对普通商品和多包装和服务商品进行退货");

		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, Multi_COMMODIY_ID2, SERVICE_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, Multi_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case18:对组合商品和多包装和服务商品进行退货");

		RetailTrade rt = createRetailTrade(COMBINATION_COMMODIY_ID1, Multi_COMMODIY_ID2, SERVICE_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(COMBINATION_COMMODIY_ID1, Multi_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19:重复添加");

		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreateEx20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case20:使用不存在的商品创建零售单");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NOT_EXIST_COMMODIY_ID, NORMAL_COMMODIY_ID3);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "卖出不存在的商品，未能检查出来");
	}

	private void createAnotherCompany() throws UnsupportedEncodingException, Exception {
		if (anotherCompany == null) {
			// 创建公司
			System.out.println("nbr公司的缓存后，零售单数目："
					+ CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex(), BaseBO.SYSTEM, new ErrorInfo(), Shared.DBName_Test));
			Company company = BaseCompanyTest.DataInput.getCompany();
			company.setSubmchid(null);
			anotherCompany = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
			System.out.println("新公司的缓存，零售单数目：" + CacheManager.getCache(anotherCompany.getDbName(), EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex(), BaseBO.SYSTEM, new ErrorInfo(),
					anotherCompany.getDbName()));
			System.out.println("nbr公司的缓存，零售单数目："
					+ CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).read1(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex(), BaseBO.SYSTEM, new ErrorInfo(), Shared.DBName_Test));

			// 先登录Boss帐号
			sessionBossOfAnotherCompany = Shared.getStaffLoginSession(mvc, company.getBossPhone(), BaseCompanyTest.bossPassword_New, anotherCompany.getSN());

			// 后面用到1个Vip，所以这里创建1个
			Vip vip = BaseVipTest.DataInput.getVip();
			vipOfAnotherCompany = BaseVipTest.createViaAction(vip, sessionBossOfAnotherCompany, mvc, EnumErrorCode.EC_NoError);
		}
	}

	@Test
	public void testCreateEx21() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case21:没有子商户号的公司进行零售，使用的是微信支付，错误信息：支付失败，公司的子商户号未设置！不能进行微信支付");

		createAnotherCompany();

		ConfigCacheSize retailTradeConfigCacheSize = new ConfigCacheSize();
		retailTradeConfigCacheSize.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		retailTradeConfigCacheSize.setID(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex());
		retailTradeConfigCacheSize.setName(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getName());
		CacheManager.getCache(anotherCompany.getDbName(), EnumCacheType.ECT_ConfigCacheSize).write1(retailTradeConfigCacheSize, Shared.DBName_Test, STAFF_ID4);

		ConfigCacheSize commodityConfigCacheSize = new ConfigCacheSize();
		commodityConfigCacheSize.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		commodityConfigCacheSize.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		commodityConfigCacheSize.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(commodityConfigCacheSize, Shared.DBName_Test, STAFF_ID4);
		//
		// sessionBoss = Shared.getStaffLoginSession(mvc, companyCreated.getBossPhone(),
		// BaseCompanyTest.bossPassword_New, companyCreated.getSN());
		// 给新创建的公司创建商品
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(c1, mvc, sessionBossOfAnotherCompany, mapBO, anotherCompany.getDbName());
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodity2 = BaseCommodityTest.createCommodityViaAction(c2, mvc, sessionBossOfAnotherCompany, mapBO, anotherCompany.getDbName());
		Commodity c3 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodity3 = BaseCommodityTest.createCommodityViaAction(c3, mvc, sessionBossOfAnotherCompany, mapBO, anotherCompany.getDbName());
		// 生成零售单
		RetailTrade rt = getRetailTrade(commodity1.getID(), commodity2.getID(), commodity3.getID());
		rt.setPaymentType(5);
		rt.setAmount(10d);
		rt.setAmountCash(3d);
		rt.setAmountWeChat(7d);
		rt.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		//
		for (int i = 0; i < rt.getListSlave1().size(); i++) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(i);
			rtc.setBarcodeID(BaseBarcodesTest.retrieveNBarcodes(rtc.getCommodityID(), anotherCompany.getDbName()).getID());
		}

		MvcResult mr1 = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBossOfAnotherCompany) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, "支付失败，公司的子商户号未设置！不能进行微信支付", "支付失败，公司的子商户号未设置！不能进行微信支付");

		// 对创建的商品进行删除
		BaseCommodityTest.deleteCommodityViaAction(commodity1, anotherCompany.getDbName(), mvc, sessionBossOfAnotherCompany, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodity2, anotherCompany.getDbName(), mvc, sessionBossOfAnotherCompany, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodity3, anotherCompany.getDbName(), mvc, sessionBossOfAnotherCompany, mapBO);
	}

	@Test
	public void testCreateEx22() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case 22:使用删除的商品，pos未同步的商品创建零售单");

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity comm = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		BaseCommodityTest.deleteCommodityViaAction(comm, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, comm.getID(), NORMAL_COMMODIY_ID3);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);
		Shared.checkJSONMsg(mr, "", "错误信息不一致");
	}

	@Test
	public void testCreateEx23() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case23:创建一个零售单，对零售单进行两次退货，第二次退货失败");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 创建一张零售单
		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);

		// 对已经退货一次的零售单再进行退货，退货失败
		RetailTrade returnRetailTrade2 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade2.setSourceID(rt.getID());

		MvcResult mr2 = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(returnRetailTrade2)) //

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx24() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case24:交易时间超过一年的订单禁止退款");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 创建一个一年又一天前的零售单
		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -1);
		c.add(Calendar.DATE, -1);
		returnRetailTrade.setSaleDatetime(c.getTime());
		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);

		// 对刚创建的零售单进行退货
		RetailTrade returnRetailTrade2 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade2.setSourceID(bmOfDB.getID());

		MvcResult mr2 = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(returnRetailTrade2)) //

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx25() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case25:iSourceID为存在的零售单ID，支付宝退款金额大于零售时的支付宝支付金额");

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 创建一个一天前的零售单
		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setPaymentType(3);
		returnRetailTrade.setAmount(100d);
		returnRetailTrade.setAmountCash(50d);
		returnRetailTrade.setAmountAlipay(50d);
		returnRetailTrade.setSaleDatetime(DatetimeUtil.getDays(new Date(), -1));
		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);

		// 对刚创建的零售单进行退货
		RetailTrade returnRetailTrade2 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade2.setSourceID(bmOfDB.getID());
		returnRetailTrade2.setPaymentType(3);
		returnRetailTrade2.setAmount(100d);
		returnRetailTrade2.setAmountCash(40d);
		returnRetailTrade2.setAmountAlipay(60d);

		MvcResult mr2 = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(returnRetailTrade2)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr2, "退款失败，支付宝退款金额不能比零售时支付宝支付的金额多", "错误信息不正确");
	}

	@Test
	public void testCreateEx26() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case26:iSourceID为存在的零售单ID，微信退款金额大于零售时的微信金额");

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 创建一个一天前的零售单
		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setPaymentType(5);
		returnRetailTrade.setAmount(100d);
		returnRetailTrade.setAmountCash(50d);
		returnRetailTrade.setAmountWeChat(50d);
		returnRetailTrade.setSaleDatetime(DatetimeUtil.getDays(new Date(), -1));
		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);

		// 对刚创建的零售单进行退货
		RetailTrade returnRetailTrade2 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade2.setSourceID(bmOfDB.getID());
		returnRetailTrade2.setPaymentType(5);
		returnRetailTrade2.setAmount(100d);
		returnRetailTrade2.setAmountCash(40d);
		returnRetailTrade2.setAmountWeChat(60d);

		MvcResult mr2 = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(returnRetailTrade2)) //

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr2, "退款失败，微信退款金额不能比零售时微信支付的金额多", "错误信息不正确");
	}

	@Test
	public void testCreateEx27() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case27：退货数量大于可退货数量 ");

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 创建一个一天前的零售单
		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setPaymentType(5);
		returnRetailTrade.setAmount(100d);
		returnRetailTrade.setAmountCash(50d);
		returnRetailTrade.setAmountWeChat(50d);
		returnRetailTrade.setSaleDatetime(DatetimeUtil.getDays(new Date(), -1));
		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);

		// 对刚创建的零售单进行退货
		RetailTrade returnRetailTrade2 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade2.setSourceID(bmOfDB.getID());
		returnRetailTrade2.setPaymentType(5);
		returnRetailTrade2.setAmount(100d);
		returnRetailTrade2.setAmountCash(50d);
		returnRetailTrade2.setAmountWeChat(50d);
		//
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(NORMAL_COMMODIY_ID1);
		retailTradeCommodity.setNO(15);
		retailTradeCommodity.setPriceOriginal(20);
		retailTradeCommodity.setPriceReturn(20);
		retailTradeCommodity.setBarcodeID(1);
		retailTradeCommodity.setNOCanReturn(10);
		//
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodity.add(retailTradeCommodity);
		returnRetailTrade2.setListSlave1(listRetailTradeCommodity);
		//
		MvcResult mr2 = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(returnRetailTrade2)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_PartSuccess);
	}

	@Test
	public void testCreateEx28() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case28:创建零售单，SN的长度小于24，结果不通过");
		// 1号pos机登录
		// // sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("LS2009");
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx29() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case29:创建零售单，SN的长度等于25，结果不通过");
		// 1号pos机登录
		// // sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("LS20190101010101010101010");
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx30() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case30:创建零售单，SN长度超过26，结果不通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("LS2019010101010101010101045456456");
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx31() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case31:创建零售单，SN不是LS开头，结果不通过");
		// 1号pos机登录
		// // sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("201902011234567891234567");
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx32() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case32:创建零售单，SN包含中文，结果不通过");
		// 1号pos机登录
		// // sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("LS20190201123烤面筋78912345");
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx33() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case33:创建零售单，SN包含其他英文字符，结果不通过");
		// 1号pos机登录
		// // sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("LS20190201123abc78912345");
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx34() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case34:创建零售单，SN包含空格，结果不通过");
		// 1号pos机登录
		// // sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("LS2019020112312 78912345");
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx35() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case35:创建零售单，SN为null，结果不通过");
		// 1号pos机登录
		// // sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn(null);
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx36() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case36:创建零售单，SN包含特殊字符，结果不通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("LS2019020112312*78912345");
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx37() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case37:创建零售单，SN日期比当前时间晚，结果不通过。");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("LS2099020112312078912345");
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx38() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case38:创建零售单，SN为26位的退货单，结尾为_1，且格式正确，结果通过。");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("LS2019020112312078912345_1");
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx39() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case39:创建零售单，SN为24位普通零售单，且格式正确，结果通过。");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("LS2019020112312078912345");
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx40() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case40:创建退货单，SourceID为SQLite的T_RetailTrade.F_ID。能够正常的创建相应零售单的退货单");
		Date startDate = new Date();
		// 创建零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn(Shared.generateRetailTradeSN(1));
		// 获取零售前的商品信息
		List<Commodity> commList = getCommodityListBeforeSold(rt);
		// 获取零售前的商品当值入库单
		Warehousing ws = new Warehousing();
		List<WarehousingCommodity> wscList = getWarehousingCommodityList(commList);
		ws.setListSlave1(wscList);
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		// 创建退货单
		RetailTrade returnRT = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRT.setSn(rt.getSn() + "_1");
		returnRT.setSourceID(Shared.SOURCE_ID_OnlyFoundInSQLite);
		// 获取零售前的商品信息
		List<Commodity> returnCommList = getCommodityListBeforeSold(returnRT);
		// 获取零售前的商品当值入库单
		wscList = getWarehousingCommodityList(returnCommList);
		ws.setListSlave1(wscList);
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRT, Shared.DBName_Test);

		// 查找这时间段内生成的俩个零售单是否是相关联的
		RetailTrade queryRT = new RetailTrade();
		queryRT.setDatetimeStart(startDate);
		queryRT.setDatetimeEnd(new Date());
		queryRT.setQueryKeyword("");
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<?> retailTradeList = retailTradeBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, queryRT);
		Assert.assertTrue(retailTradeBO.getLastErrorCode() == EnumErrorCode.EC_NoError && retailTradeList.size() == 2, "该测试未创建俩个零售单");
		returnRT = (RetailTrade) retailTradeList.get(0);
		rt = (RetailTrade) retailTradeList.get(1);
		Assert.assertTrue(returnRT.getSourceID() == rt.getID(), "生成零售单A的退货单SourceID不是零售单A的ID");
	}

	@Test
	public void testCreateEx41() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case41:零售单带有零售单促销");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		// 生成零售单促销
		appendListSlave2(rt);

		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx42() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("42:零售单促销表解析失败");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		List<Commodity> commList = new ArrayList<Commodity>();
		commList.add(new Commodity());
		rt.setListSlave2(commList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "结果和期望的不一致");
	}

	@Test
	public void testCreateEx43() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("43:一张零售单中有俩个零售单促销表");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		// 生成零售单促销表
		List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
		List<RetailTradePromotingFlow> retailTradePromotingFlowList = new ArrayList<RetailTradePromotingFlow>();
		for (int i = 0; i < 2; i++) {
			RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
			RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
			retailTradePromotingFlow.setPromotionID(1);
			retailTradePromotingFlow.setProcessFlow("测试数据aaa" + i);
			retailTradePromotingFlowList.add(retailTradePromotingFlow);
			retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
			retailTradePromotingList.add(retailTradePromoting);
		}
		rt.setListSlave2(retailTradePromotingList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "结果和期望的不一致");
	}

	@Test
	public void testCreateEx44() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("44:零售单促销从表部分成功");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		// 生成零售单促销
		List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		List<RetailTradePromotingFlow> retailTradePromotingFlowList = new ArrayList<RetailTradePromotingFlow>();
		RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
		retailTradePromotingFlow.setPromotionID(Shared.BIG_ID);
		retailTradePromotingFlow.setProcessFlow("测试数据aaa");
		retailTradePromotingFlowList.add(retailTradePromotingFlow);
		retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
		retailTradePromotingList.add(retailTradePromoting);
		rt.setListSlave2(retailTradePromotingList);

		Map<Integer, Commodity> simpleCommodityList = null;
		boolean bIncludeDeletedCommodity = false;
		if (rt.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity = true;
		}
		simpleCommodityList = getSimpleCommodityListFromRetailTrade(rt, Shared.DBName_Test, bIncludeDeletedCommodity, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale = queryWarehousingBeforeSale(simpleCommodityList, Shared.DBName_Test, rt, warehousingBO);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());

		RetailTrade bmOfDB = new RetailTrade();
		bmOfDB = (RetailTrade) bmOfDB.parse1(returnJson.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);

		RetailTradePromoting tradePromoting = (RetailTradePromoting) bmOfDB.getListSlave2().get(0);
		Assert.assertTrue(tradePromoting.getListSlave1() == null, "零售单促销从表创建成功！");

		Map<Integer, List<Warehousing>> mapAfterSale = queryWarehousingAfterSale(mapBeforeSale, Shared.DBName_Test, warehousingBO);
		if (rt.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		RetailTradeCP.verifyCreate(bmOfDB, rt, mapBeforeSale, mapAfterSale, simpleCommodityList, mapBO, Shared.DBName_Test, false);
	}

	@Test
	public void testCreateEx45() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("45:VIP不存在");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setVipID(Shared.BIG_ID);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testCreateEx46() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("46:POS不存在");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setPos_ID(Shared.BIG_ID);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testCreateEx48() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("48:零售单商品的条形码不存在");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		for (int i = 0; i < rt.getListSlave1().size(); i++) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(i);
			rtc.setBarcodeID(Shared.BIG_ID);
		}

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "条形码不存在未能检查出来");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateEx49() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case49: 创建零售单从表部分成功");
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		Map<Integer, Commodity> simpleCommodityList = null;
		boolean bIncludeDeletedCommodity = false;
		simpleCommodityList = getSimpleCommodityListFromRetailTrade(rt, Shared.DBName_Test, bIncludeDeletedCommodity, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale = queryWarehousingBeforeSale(simpleCommodityList, Shared.DBName_Test, rt, warehousingBO);

		// 零售单从表添加一个DB不存在的商品
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		// 商品ID不能重复，否则当黑客行为
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		rtc.setCommodityID(commodityCreate.getID());
		rtc.setBarcodeID(Shared.BIG_ID);
		List<RetailTradeCommodity> rtcList = (List<RetailTradeCommodity>) rt.getListSlave1();
		rtcList.add(rtc);

		// 添加这个商品的缓存
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(Shared.BIG_ID);
		barcodes.setID(Shared.BIG_ID);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Barcodes).write1(barcodes, Shared.DBName_Test, BaseBO.SYSTEM);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
		// 将缓存删除
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Barcodes).delete1(barcodes);
		// 检查创建后返回的对象是否和传入的对象一致
		RetailTrade bmOfDB = (RetailTrade) Shared.parse1Object(mr, rt, BaseAction.KEY_Object);

		Map<Integer, List<Warehousing>> mapAfterSale = queryWarehousingAfterSale(mapBeforeSale, Shared.DBName_Test, warehousingBO);
		if (rt.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		RetailTradeCP.verifyCreate(bmOfDB, rt, mapBeforeSale, mapAfterSale, simpleCommodityList, mapBO, Shared.DBName_Test, false);
	}

	@Test
	public void testCreateEx50() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("50:零售单优惠券使用表解析失败");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<BaseModel> retailTradeCommodityList = new ArrayList<BaseModel>();
		retailTradeCommodityList.add(retailTradeCommodity);
		retailTrade.setListSlave1(retailTradeCommodityList);
		// 生成零售单促销
		appendListSlave2(retailTrade);

		// listSlave3中放入其他对象
		List<Commodity> commList = new ArrayList<Commodity>();
		Commodity comm = new Commodity();
		commList.add(comm);
		retailTrade.setListSlave3(commList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(retailTrade)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "返回结果和预期的不一样");
	}

	@Test
	public void testCreateEx47() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("47:一张零售单中存在多张零售单优惠券使用表");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<BaseModel> retailTradeCommodityList = new ArrayList<BaseModel>();
		retailTradeCommodityList.add(retailTradeCommodity);
		retailTrade.setListSlave1(retailTradeCommodityList);
		// 生成零售单促销
		appendListSlave2(retailTrade);
		// 获取优惠券
		Coupon coupon = createCoupon();
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, coupon.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 生成零售单优惠券使用表
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		for (int i = 0; i < 2; i++) {
			RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeCreate.getID(), retailTrade.getID());
			retailTradeCouponList.add(retailTradeCoupon);
		}
		retailTrade.setListSlave3(retailTradeCouponList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(retailTrade)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "返回结果和预期的不一样");
	}

	@Test
	public void testCreateEx51() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("51:零售单优惠券使用表创建失败");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<BaseModel> retailTradeCommodityList = new ArrayList<BaseModel>();
		retailTradeCommodityList.add(retailTradeCommodity);
		retailTrade.setListSlave1(retailTradeCommodityList);
		// 生成零售单促销
		appendListSlave2(retailTrade);
		// 生成零售单优惠券使用表
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(BaseAction.INVALID_ID, retailTrade.getID());
		retailTradeCouponList.add(retailTradeCoupon);
		retailTrade.setListSlave3(retailTradeCouponList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(retailTrade)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "使用不存在的优惠券去进行创建未能检查出来");
	}

	@Test
	public void testCreateEx52() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("52:零售单优惠券使用表存在,却不存在零售单促销表");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 生成零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<BaseModel> retailTradeCommodityList = new ArrayList<BaseModel>();
		retailTradeCommodityList.add(retailTradeCommodity);
		retailTrade.setListSlave1(retailTradeCommodityList);
		//
		appendListSlave3(retailTrade);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(retailTrade)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "返回的结果和预期的不一致");
	}

	@Test
	public void testCreateEx53() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("53:退货单存在零售单促销表");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 生成零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<BaseModel> retailTradeCommodityList = new ArrayList<BaseModel>();
		retailTradeCommodityList.add(retailTradeCommodity);
		retailTrade.setListSlave1(retailTradeCommodityList);
		retailTrade.setSourceID(1);
		//
		appendListSlave2(retailTrade);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(retailTrade)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "返回的结果和预期的不一致");
	}

	@Test
	public void testCreateEx54() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("54:退货单存在零售单优惠券使用表");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 生成零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<BaseModel> retailTradeCommodityList = new ArrayList<BaseModel>();
		retailTradeCommodityList.add(retailTradeCommodity);
		retailTrade.setListSlave1(retailTradeCommodityList);
		retailTrade.setSourceID(1);
		//
		appendListSlave3(retailTrade);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(retailTrade)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "返回的结果和预期的不一致");
	}

	public void appendListSlave2(RetailTrade rt) {
		// 生成零售单促销
		List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		List<RetailTradePromotingFlow> retailTradePromotingFlowList = new ArrayList<RetailTradePromotingFlow>();
		RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
		retailTradePromotingFlow.setPromotionID(1);
		retailTradePromotingFlow.setProcessFlow("测试数据aaa");
		retailTradePromotingFlowList.add(retailTradePromotingFlow);
		retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
		retailTradePromotingList.add(retailTradePromoting);
		rt.setListSlave2(retailTradePromotingList);
	}

	public void appendListSlave3(RetailTrade rt) throws Exception {
		// 获取优惠券
		Coupon coupon = createCoupon();
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, coupon.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 生成零售单优惠券使用表
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeCreate.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);
	}

	@Test
	public void testCreateEx55() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("55:会员结算了一张零售单");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		// 获取生成零售单前VIP的消费等信息
		Vip vip = new Vip();
		vip.setID(rt.getVipID());
		Vip beforeConsumeVipData = BaseVipTest.retrieve1ViaMapper(vip, Shared.DBName_Test);

		RetailTrade retailTrade = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		// 验证Vip信息是否改变
		Vip afterConsumeVipData = BaseVipTest.retrieve1ViaMapper(vip, Shared.DBName_Test);
		Assert.assertTrue(GeneralUtil.sub(GeneralUtil.sum(beforeConsumeVipData.getConsumeAmount(), retailTrade.getAmount()), afterConsumeVipData.getConsumeAmount()) <= BaseModel.TOLERANCE, "会员消费后，会员的总消费金额不正确");
		Assert.assertTrue(afterConsumeVipData.getConsumeTimes() == (beforeConsumeVipData.getConsumeTimes() + 1), "会员消费后，会员的消费次数计算不正确");

		ErrorInfo ecOut = new ErrorInfo();
		Vip vipInCache = (Vip) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Vip).read1(vip.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(vipInCache != null && vipInCache.compareTo(afterConsumeVipData) == 0, "查找会员失败或者缓存中的数据和DB数据不一致");
	}
	
	@Test
	public void testCreateEx55_2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("55:会员结算了一张零售单, 消费金额大于1000元，增加的积分为单次可获取的积分上限1000");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 创建商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建零售单商品
		List<RetailTradeCommodity> rtCommList = new ArrayList<>();
		Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCreated.getID(), Shared.DBName_Test);
		RetailTradeCommodity rtComm = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtComm.setPriceReturn(100);
		rtComm.setNO(100);
		rtComm.setNOCanReturn(100);
		rtComm.setCommodityID(commodityCreated.getID());
		rtComm.setBarcodeID(barcodes.getID());
		rtCommList.add(rtComm);
		// 生成零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setAmount(GeneralUtil.mul(rtComm.getPriceReturn(), rtComm.getNO()));
		rt.setAmountCash(rt.getAmount());
		rt.setListSlave1(rtCommList);
		// 获取生成零售单前VIP的消费等信息
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		Vip beforeConsumeVipData = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		rt.setVipID(vipCreate.getID());
		RetailTrade retailTrade = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		// 验证Vip信息是否改变
		Vip afterConsumeVipData = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(GeneralUtil.sub(GeneralUtil.sum(beforeConsumeVipData.getConsumeAmount(), retailTrade.getAmount()), afterConsumeVipData.getConsumeAmount()) <= BaseModel.TOLERANCE, "会员消费后，会员的总消费金额不正确");
		Assert.assertTrue(afterConsumeVipData.getConsumeTimes() == (beforeConsumeVipData.getConsumeTimes() + 1), "会员消费后，会员的消费次数计算不正确");

		ErrorInfo ecOut = new ErrorInfo();
		Vip vipInCache = (Vip) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Vip).read1(vipCreate.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(vipInCache != null && vipInCache.compareTo(afterConsumeVipData) == 0, "查找会员失败或者缓存中的数据和DB数据不一致");
	}
	
	@Test
	public void testCreateEx55_3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("55_3:会员结算了一张零售单, 消费金额在0-1元之间（0.5），增加的积分为5");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 创建商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建零售单商品
		List<RetailTradeCommodity> rtCommList = new ArrayList<>();
		Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCreated.getID(), Shared.DBName_Test);
		RetailTradeCommodity rtComm = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtComm.setPriceReturn(0.1);
		rtComm.setNO(5);
		rtComm.setNOCanReturn(5);
		rtComm.setCommodityID(commodityCreated.getID());
		rtComm.setBarcodeID(barcodes.getID());
		rtCommList.add(rtComm);
		// 生成零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setAmount(GeneralUtil.mul(rtComm.getPriceReturn(), rtComm.getNO()));
		rt.setAmountCash(rt.getAmount());
		rt.setListSlave1(rtCommList);
		// 获取生成零售单前VIP的消费等信息
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		Vip beforeConsumeVipData = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		rt.setVipID(vipCreate.getID());
		RetailTrade retailTrade = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		// 验证Vip信息是否改变
		Vip afterConsumeVipData = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(GeneralUtil.sub(GeneralUtil.sum(beforeConsumeVipData.getConsumeAmount(), retailTrade.getAmount()), afterConsumeVipData.getConsumeAmount()) <= BaseModel.TOLERANCE, "会员消费后，会员的总消费金额不正确");
		Assert.assertTrue(afterConsumeVipData.getConsumeTimes() == (beforeConsumeVipData.getConsumeTimes() + 1), "会员消费后，会员的消费次数计算不正确");

		ErrorInfo ecOut = new ErrorInfo();
		Vip vipInCache = (Vip) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Vip).read1(vipCreate.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(vipInCache != null && vipInCache.compareTo(afterConsumeVipData) == 0, "查找会员失败或者缓存中的数据和DB数据不一致");
	}
	
	
	@Test
	public void testCreateEx55_4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("55_4:会员结算了一张零售单, 消费金额在0-1元之间（0.05），增加的积分为1（0.5四舍五入）");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 创建商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建零售单商品
		List<RetailTradeCommodity> rtCommList = new ArrayList<>();
		Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCreated.getID(), Shared.DBName_Test);
		RetailTradeCommodity rtComm = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtComm.setPriceReturn(0.01);
		rtComm.setNO(5);
		rtComm.setNOCanReturn(5);
		rtComm.setCommodityID(commodityCreated.getID());
		rtComm.setBarcodeID(barcodes.getID());
		rtCommList.add(rtComm);
		// 生成零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setAmount(GeneralUtil.mul(rtComm.getPriceReturn(), rtComm.getNO()));
		rt.setAmountCash(rt.getAmount());
		rt.setListSlave1(rtCommList);
		// 获取生成零售单前VIP的消费等信息
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		Vip beforeConsumeVipData = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		rt.setVipID(vipCreate.getID());
		RetailTrade retailTrade = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		// 验证Vip信息是否改变
		Vip afterConsumeVipData = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(GeneralUtil.sub(GeneralUtil.sum(beforeConsumeVipData.getConsumeAmount(), retailTrade.getAmount()), afterConsumeVipData.getConsumeAmount()) <= BaseModel.TOLERANCE, "会员消费后，会员的总消费金额不正确");
		Assert.assertTrue(afterConsumeVipData.getConsumeTimes() == (beforeConsumeVipData.getConsumeTimes() + 1), "会员消费后，会员的消费次数计算不正确");

		ErrorInfo ecOut = new ErrorInfo();
		Vip vipInCache = (Vip) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Vip).read1(vipCreate.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(vipInCache != null && vipInCache.compareTo(afterConsumeVipData) == 0, "查找会员失败或者缓存中的数据和DB数据不一致");
	}
	
	
	@Test
	public void testCreateEx55_5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("55_4:会员结算了一张零售单, 消费金额在0-1元之间（0），增加的积分为0");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 创建商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建零售单商品
		List<RetailTradeCommodity> rtCommList = new ArrayList<>();
		Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCreated.getID(), Shared.DBName_Test);
		RetailTradeCommodity rtComm = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtComm.setPriceReturn(0);
		rtComm.setNO(5);
		rtComm.setNOCanReturn(5);
		rtComm.setCommodityID(commodityCreated.getID());
		rtComm.setBarcodeID(barcodes.getID());
		rtCommList.add(rtComm);
		// 生成零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setAmount(GeneralUtil.mul(rtComm.getPriceReturn(), rtComm.getNO()));
		rt.setAmountCash(rt.getAmount());
		rt.setListSlave1(rtCommList);
		// 获取生成零售单前VIP的消费等信息
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		Vip beforeConsumeVipData = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		rt.setVipID(vipCreate.getID());
		RetailTrade retailTrade = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		// 验证Vip信息是否改变
		Vip afterConsumeVipData = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(GeneralUtil.sub(GeneralUtil.sum(beforeConsumeVipData.getConsumeAmount(), retailTrade.getAmount()), afterConsumeVipData.getConsumeAmount()) <= BaseModel.TOLERANCE, "会员消费后，会员的总消费金额不正确");
		Assert.assertTrue(afterConsumeVipData.getConsumeTimes() == (beforeConsumeVipData.getConsumeTimes() + 1), "会员消费后，会员的消费次数计算不正确");

		ErrorInfo ecOut = new ErrorInfo();
		Vip vipInCache = (Vip) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Vip).read1(vipCreate.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(vipInCache != null && vipInCache.compareTo(afterConsumeVipData) == 0, "查找会员失败或者缓存中的数据和DB数据不一致");
	}

	@Test
	public void testCreateEx56() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("56:会员使用优惠券结算了一张零售单");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 创建会员
		Vip vip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);

		// 使用此会员进行消费
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setVipID(vip.getID());
		//
		List<BaseModel> listSlave1 = new ArrayList<BaseModel>();
		listSlave1.add(BaseRetailTradeTest.DataInput.getRetailTradeCommodity());
		rt.setListSlave1(listSlave1);
		//
		RetailTrade createRetailTrade = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		// 查询会员的消费信息
		Vip vipA = BaseVipTest.retrieve1ViaMapper(vip, Shared.DBName_Test);

		// 进行退货,使用其他的会员ID
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setListSlave1(listSlave1);
		// 退货前，先查询传递的vipID的vip信息
		Vip vipB = new Vip();
		vipB.setID(returnRetailTrade.getVipID());
		vipB = BaseVipTest.retrieve1ViaMapper(vipB, Shared.DBName_Test);
		//
		returnRetailTrade.setSourceID(createRetailTrade.getID());
		RetailTrade createReturnRetailTrade = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);

		// 检查是否正确的减少会员的消费
		Vip afterVipA = BaseVipTest.retrieve1ViaMapper(vip, Shared.DBName_Test);
		Assert.assertTrue(
				vipA.getConsumeTimes() == afterVipA.getConsumeTimes() + 1 && Math.abs(GeneralUtil.sub(vipA.getConsumeAmount(), GeneralUtil.sum(afterVipA.getConsumeAmount(), createReturnRetailTrade.getAmount()))) < BaseModel.TOLERANCE,
				"会员A计算消费次数不正确");
		//
		Vip afterVipB = BaseVipTest.retrieve1ViaMapper(vipB, Shared.DBName_Test);
		Assert.assertTrue(vipB.getConsumeTimes() == afterVipB.getConsumeTimes() && Math.abs(GeneralUtil.sub(vipB.getConsumeAmount(), afterVipB.getConsumeAmount())) < BaseModel.TOLERANCE, "未对会员b的单进行退货，但是会员B的消费记录却发生了改变");
	}

	@Test
	public void testCreateEx57() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("57:会员A的消费单，对其进行退货,vipID为会员B");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		appendListSlave2(rt);
		appendListSlave3(rt);
		RetailTrade retailTrade = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		CouponCode couponCode = new CouponCode();
		couponCode.setID(((RetailTradeCoupon) (retailTrade.getListSlave3().get(0))).getCouponCodeID());
		CouponCode queryCouponCode = (CouponCode) BaseCouponCodeTest.retrieve1ViaMapper(couponCode);
		Assert.assertTrue(queryCouponCode.getStatus() == CouponCode.EnumCouponCodeStatus.ECCS_Consumed.getIndex(), "优惠券未核销");
	}

	@Test
	public void testCreateEx58() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("零售单包含重复的商品");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID3);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "返回的结果和预期的不一致");
	}

	@Test
	public void testCreateEx59() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case59:iSourceID为存在的零售单ID，微信和现金混合退款，大于源单的现金和微信混合支付，退款失败");

		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 创建一个一天前的零售单
		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setPaymentType(5);
		returnRetailTrade.setAmount(100d);
		returnRetailTrade.setAmountCash(50d);
		returnRetailTrade.setAmountWeChat(50d);
		returnRetailTrade.setSaleDatetime(DatetimeUtil.getDays(new Date(), -1));
		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);

		// 对刚创建的零售单进行退货
		RetailTrade returnRetailTrade2 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade2.setSourceID(bmOfDB.getID());
		returnRetailTrade2.setPaymentType(5);
		returnRetailTrade2.setAmount(120d);
		returnRetailTrade2.setAmountCash(60d);
		returnRetailTrade2.setAmountWeChat(60d);

		MvcResult mr2 = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(returnRetailTrade2)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr2, "退款失败，退款金额不能比零售时微信支付和现金支付的总金额多", "错误信息不正确");
	}

	@Test
	public void testCreate60Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("添加商品（参与促销），搜索会员，使用优惠券进行支付，支付成功（对应的DB中的t_retailtradepromotingflow有优惠券计算过程和促销计算过程）");
		doVipConsuming(true);
	}

	@Test
	public void testCreate61Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("添加商品（不参与促销），搜索会员，使用优惠券进行支付，支付成功（对应的DB中的t_retailtradepromotingflow有优惠券计算过程）");

		doVipConsuming(false);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreate62Ex() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("62 使用nbr公司创建一个或多个零售单，验证当天的零售总额和零售笔数是否正确");
		// 获取创建零售单前的totalAmountToday和totalNOToday
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		double totalAmountToday_BeforeCreateRT = realTimeSST.getTotalAmountToday();
		double totalNOToday_BeforeCreateRT = realTimeSST.getTotalNOToday();
		// 生成零售单1
		RetailTrade rt1 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		List<RetailTradeCommodity> rtCommList = (List<RetailTradeCommodity>) rt1.getListSlave1();
		double totalRtAmount1 = 0;
		for (RetailTradeCommodity rtComm : rtCommList) {
			totalRtAmount1 += rtComm.getNO() * rtComm.getPriceReturn();
		}
		rt1.setAmount(totalRtAmount1);
		rt1.setAmountCash(totalRtAmount1);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt1, Shared.DBName_Test);
		// 生成零售单2
		RetailTrade rt2 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		List<RetailTradeCommodity> rtCommList2 = (List<RetailTradeCommodity>) rt2.getListSlave1();
		double totalRtAmount2 = 0;
		for (RetailTradeCommodity rtComm : rtCommList2) {
			totalRtAmount2 += rtComm.getNO() * rtComm.getPriceReturn();
		}
		rt2.setAmount(totalRtAmount2);
		rt2.setAmountCash(totalRtAmount2);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt2, Shared.DBName_Test);
		double totalRtAmount = GeneralUtil.sum(totalRtAmount1, totalRtAmount2);
		// 获取创建零售单后的totalAmountToday和totalNOToday
		double totalAmountToday_AfterCreateRT = realTimeSST.getTotalAmountToday();
		double totalNOToday_AfterCreateRT = realTimeSST.getTotalNOToday();
		// 结果验证
		Assert.assertTrue(totalNOToday_AfterCreateRT == totalNOToday_BeforeCreateRT + 2, "当天零售笔数有误，before:" + totalNOToday_BeforeCreateRT + ",after:" + totalNOToday_AfterCreateRT);
		double differ = GeneralUtil.sub(totalAmountToday_AfterCreateRT, GeneralUtil.sum(totalRtAmount, totalAmountToday_BeforeCreateRT));
		Assert.assertTrue(differ < BaseModel.TOLERANCE, "当天零售总额有误，before:" + totalAmountToday_BeforeCreateRT + ",after:" + totalAmountToday_AfterCreateRT);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreate63Ex() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("63 新建一个公司，创建一个或多个零售单，验证当天的零售单总额和零售笔数是否正确");
		Company companyGet = BaseCompanyTest.DataInput.getCompany();
		Company companyCreated = BaseCompanyTest.createCompanyViaAction(companyGet, mvc, mapBO, sessionOP);
		// 创建pos
		Pos posGet = BasePosTest.DataInput.getPOS();
		posGet.setCompanySN(companyCreated.getSN());
		Pos posCreated = BasePosTest.createPosViaSyncAction(posGet, mvc, mapBO, companyCreated.getDbName());
		MockHttpSession newSessionBoss = Shared.getStaffLoginSession(mvc, companyCreated.getBossPhone(), BaseCompanyTest.bossPassword_New, companyCreated.getSN());
		// 新建商品1
		Commodity commodityGet1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated1 = BaseCommodityTest.createCommodityViaAction(commodityGet1, mvc, newSessionBoss, mapBO, companyCreated.getDbName());
		// 新建商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, newSessionBoss, mapBO, companyCreated.getDbName());
		// 新建商品3
		Commodity commodityGet3 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated3 = BaseCommodityTest.createCommodityViaAction(commodityGet3, mvc, newSessionBoss, mapBO, companyCreated.getDbName());

		// 获取创建零售单前的totalAmountToday和totalNOToday
		RealTimeSalesStatisticsToday realTimeSST_newCompany = RetailTradeAction.todaysSaleCache.get(companyCreated.getDbName());
		double totalAmountToday_BeforeCreateRT_newCompany = realTimeSST_newCompany.getTotalAmountToday();
		double totalNOToday_BeforeCreateRT_newCompany = realTimeSST_newCompany.getTotalNOToday();
		// 创建vip
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreated = BaseVipTest.createViaAction(vipGet, newSessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 生成零售单1
		RetailTrade rt1_newCompany = getRetailTrade(commodityCreated1.getID(), commodityCreated2.getID(), commodityCreated3.getID());
		rt1_newCompany.setVipID(vipCreated.getID());
		rt1_newCompany.setPos_ID(posCreated.getID());
		List<RetailTradeCommodity> rtCommList_newCompany = (List<RetailTradeCommodity>) rt1_newCompany.getListSlave1();
		double totalRtAmount1_newCompany = 0;
		for (RetailTradeCommodity rtComm : rtCommList_newCompany) {
			totalRtAmount1_newCompany += rtComm.getNO() * rtComm.getPriceReturn();
			Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(rtComm.getCommodityID(), companyCreated.getDbName());
			rtComm.setBarcodeID(barcodes.getID());
		}
		rt1_newCompany.setAmount(totalRtAmount1_newCompany);
		rt1_newCompany.setAmountCash(totalRtAmount1_newCompany);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, newSessionBoss, mapBO, rt1_newCompany, companyCreated.getDbName());
		// 生成零售单2
		RetailTrade rt2_newCompany = getRetailTrade(commodityCreated1.getID(), commodityCreated2.getID(), commodityCreated3.getID());
		rt2_newCompany.setVipID(vipCreated.getID());
		rt2_newCompany.setPos_ID(posCreated.getID());
		List<RetailTradeCommodity> rtCommList2_newCompany = (List<RetailTradeCommodity>) rt2_newCompany.getListSlave1();
		double totalRtAmount2_newCompany = 0;
		for (RetailTradeCommodity rtComm : rtCommList2_newCompany) {
			totalRtAmount2_newCompany += rtComm.getNO() * rtComm.getPriceReturn();
			Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(rtComm.getCommodityID(), companyCreated.getDbName());
			rtComm.setBarcodeID(barcodes.getID());
		}
		rt2_newCompany.setAmount(totalRtAmount2_newCompany);
		rt2_newCompany.setAmountCash(totalRtAmount2_newCompany);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, newSessionBoss, mapBO, rt2_newCompany, companyCreated.getDbName());
		double totalRtAmount_newCompany = GeneralUtil.sum(totalRtAmount1_newCompany, totalRtAmount2_newCompany);
		// 获取创建零售单后的totalAmountToday和totalNOToday
		double totalAmountToday_AfterCreateRT_newCompany = realTimeSST_newCompany.getTotalAmountToday();
		double totalNOToday_AfterCreateRT_newCompany = realTimeSST_newCompany.getTotalNOToday();
		// 结果验证
		Assert.assertTrue(totalNOToday_AfterCreateRT_newCompany == totalNOToday_BeforeCreateRT_newCompany + 2, "当天零售笔数有误，before:" + totalNOToday_BeforeCreateRT_newCompany + ",after:" + totalNOToday_AfterCreateRT_newCompany);
		double differ_newCompany = GeneralUtil.sub(totalAmountToday_AfterCreateRT_newCompany, GeneralUtil.sum(totalRtAmount_newCompany, totalAmountToday_BeforeCreateRT_newCompany));
		Assert.assertTrue(differ_newCompany < BaseModel.TOLERANCE, "当天零售总额有误，before:" + totalAmountToday_BeforeCreateRT_newCompany + ",after:" + totalAmountToday_AfterCreateRT_newCompany);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreate64Ex() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case64 先用nbr创建一个或多个零售单、然后使用新公司创建一个或多个零售单，验证各个公司的零售总额和零售笔数是否正确");
		//
		// 获取创建零售单前的totalAmountToday和totalNOToday
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		double totalAmountToday_BeforeCreateRT = realTimeSST.getTotalAmountToday();
		double totalNOToday_BeforeCreateRT = realTimeSST.getTotalNOToday();
		// 生成零售单1
		RetailTrade rt1 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		List<RetailTradeCommodity> rtCommList = (List<RetailTradeCommodity>) rt1.getListSlave1();
		double totalRtAmount1 = 0;
		for (RetailTradeCommodity rtComm : rtCommList) {
			totalRtAmount1 += rtComm.getNO() * rtComm.getPriceReturn();
		}
		rt1.setAmount(totalRtAmount1);
		rt1.setAmountCash(totalRtAmount1);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt1, Shared.DBName_Test);
		// 生成零售单2
		RetailTrade rt2 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		List<RetailTradeCommodity> rtCommList2 = (List<RetailTradeCommodity>) rt2.getListSlave1();
		double totalRtAmount2 = 0;
		for (RetailTradeCommodity rtComm : rtCommList2) {
			totalRtAmount2 += rtComm.getNO() * rtComm.getPriceReturn();
		}
		rt2.setAmount(totalRtAmount2);
		rt2.setAmountCash(totalRtAmount2);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt2, Shared.DBName_Test);
		double totalRtAmount = GeneralUtil.sum(totalRtAmount1, totalRtAmount2);
		// 获取创建零售单后的totalAmountToday和totalNOToday
		double totalAmountToday_AfterCreateRT = realTimeSST.getTotalAmountToday();
		double totalNOToday_AfterCreateRT = realTimeSST.getTotalNOToday();
		//
		// 新建公司
		Company companyGet = BaseCompanyTest.DataInput.getCompany();
		Company companyCreated = BaseCompanyTest.createCompanyViaAction(companyGet, mvc, mapBO, sessionOP);
		// 创建pos
		Pos posGet = BasePosTest.DataInput.getPOS();
		posGet.setCompanySN(companyCreated.getSN());
		Pos posCreated = BasePosTest.createPosViaSyncAction(posGet, mvc, mapBO, companyCreated.getDbName());
		MockHttpSession newSessionBoss = Shared.getStaffLoginSession(mvc, companyCreated.getBossPhone(), BaseCompanyTest.bossPassword_New, companyCreated.getSN());
		// 新建商品1
		Commodity commodityGet1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated1 = BaseCommodityTest.createCommodityViaAction(commodityGet1, mvc, newSessionBoss, mapBO, companyCreated.getDbName());
		// 新建商品2
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, newSessionBoss, mapBO, companyCreated.getDbName());
		// 新建商品3
		Commodity commodityGet3 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated3 = BaseCommodityTest.createCommodityViaAction(commodityGet3, mvc, newSessionBoss, mapBO, companyCreated.getDbName());
		//
		// 获取创建零售单前的totalAmountToday和totalNOToday
		RealTimeSalesStatisticsToday realTimeSST_newCompany = RetailTradeAction.todaysSaleCache.get(companyCreated.getDbName());
		double totalAmountToday_BeforeCreateRT_newCompany = realTimeSST_newCompany.getTotalAmountToday();
		double totalNOToday_BeforeCreateRT_newCompany = realTimeSST_newCompany.getTotalNOToday();
		// 创建vip
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreated = BaseVipTest.createViaAction(vipGet, newSessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 生成零售单1
		RetailTrade rt1_newCompany = getRetailTrade(commodityCreated1.getID(), commodityCreated2.getID(), commodityCreated3.getID());
		rt1_newCompany.setVipID(vipCreated.getID());
		rt1_newCompany.setPos_ID(posCreated.getID());
		List<RetailTradeCommodity> rtCommList_newCompany = (List<RetailTradeCommodity>) rt1_newCompany.getListSlave1();
		double totalRtAmount1_newCompany = 0;
		for (RetailTradeCommodity rtComm : rtCommList_newCompany) {
			totalRtAmount1_newCompany += rtComm.getNO() * rtComm.getPriceReturn();
			Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(rtComm.getCommodityID(), companyCreated.getDbName());
			rtComm.setBarcodeID(barcodes.getID());
		}
		rt1_newCompany.setAmount(totalRtAmount1_newCompany);
		rt1_newCompany.setAmountCash(totalRtAmount1_newCompany);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, newSessionBoss, mapBO, rt1_newCompany, companyCreated.getDbName());
		// 生成零售单2
		RetailTrade rt2_newCompany = getRetailTrade(commodityCreated1.getID(), commodityCreated2.getID(), commodityCreated3.getID());
		rt2_newCompany.setVipID(vipCreated.getID());
		rt2_newCompany.setPos_ID(posCreated.getID());
		List<RetailTradeCommodity> rtCommList2_newCompany = (List<RetailTradeCommodity>) rt2_newCompany.getListSlave1();
		double totalRtAmount2_newCompany = 0;
		for (RetailTradeCommodity rtComm : rtCommList2_newCompany) {
			totalRtAmount2_newCompany += rtComm.getNO() * rtComm.getPriceReturn();
			Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(rtComm.getCommodityID(), companyCreated.getDbName());
			rtComm.setBarcodeID(barcodes.getID());
		}
		rt2_newCompany.setAmount(totalRtAmount2_newCompany);
		rt2_newCompany.setAmountCash(totalRtAmount2_newCompany);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, newSessionBoss, mapBO, rt2_newCompany, companyCreated.getDbName());
		double totalRtAmount_newCompany = GeneralUtil.sum(totalRtAmount1_newCompany, totalRtAmount2_newCompany);
		// 获取创建零售单后的totalAmountToday和totalNOToday
		double totalAmountToday_AfterCreateRT_newCompany = realTimeSST_newCompany.getTotalAmountToday();
		double totalNOToday_AfterCreateRT_newCompany = realTimeSST_newCompany.getTotalNOToday();
		// 结果验证
		Assert.assertTrue(totalNOToday_AfterCreateRT_newCompany == totalNOToday_BeforeCreateRT_newCompany + 2, "当天零售笔数有误，before:" + totalNOToday_BeforeCreateRT_newCompany + ",after:" + totalNOToday_AfterCreateRT_newCompany);
		double differ_newCompany = GeneralUtil.sub(totalAmountToday_AfterCreateRT_newCompany, GeneralUtil.sum(totalRtAmount_newCompany, totalAmountToday_BeforeCreateRT_newCompany));
		Assert.assertTrue(differ_newCompany < BaseModel.TOLERANCE, "当天零售总额有误，before:" + totalAmountToday_BeforeCreateRT_newCompany + ",after:" + totalAmountToday_AfterCreateRT_newCompany);
		//
		// 结果验证
		Assert.assertTrue(totalNOToday_AfterCreateRT == totalNOToday_BeforeCreateRT + 2, "当天零售笔数有误，before:" + totalNOToday_BeforeCreateRT + ",after:" + totalNOToday_AfterCreateRT);
		double differ = GeneralUtil.sub(totalAmountToday_AfterCreateRT, GeneralUtil.sum(totalRtAmount, totalAmountToday_BeforeCreateRT));
		Assert.assertTrue(differ < BaseModel.TOLERANCE, "当天零售总额有误，before:" + totalAmountToday_BeforeCreateRT + ",after:" + totalAmountToday_AfterCreateRT);

	}
	
	@Test
	public void testCreateEx65() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case65 门店A创建零售单A，然后门店B对零售单A进行退货，退货成功");
		// 创建一个门店A
		Shop shopGetA = BaseShopTest.DataInput.getShop();
		Shop shopGetCreateA = BaseShopTest.createViaCompanyAction(shopGetA, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 创建一个门店B
		Shop shopGetB = BaseShopTest.DataInput.getShop();
		Shop shopGetCreateB = BaseShopTest.createViaCompanyAction(shopGetB, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 门店A创建零售单A
		RetailTrade retailTradeGet = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeGet.setShopID(shopGetCreateA.getID());
		RetailTrade rt = createRetailTrade(retailTradeGet, NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		// 门店B对零售单A进行退货
		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		returnRetailTrade.setShopID(shopGetCreateB.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
	}
	
	@Test
	public void testCreateEx66() {
		Shared.printTestMethodStartInfo();
		// TODO 做完PEF-851再来实现该用例
		Shared.caseLog("case66 门店A修改商品A价格，门店B对商品A进行零售，商品价格不会改变，因为每个门店都有自己的商品定价");
	}

	private void doVipConsuming(Boolean participateInPromotion) throws Exception {
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		// 生成计算过程
		List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		List<RetailTradePromotingFlow> retailTradePromotingFlows = new ArrayList<RetailTradePromotingFlow>();
		//
		if (participateInPromotion) {
			RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
			retailTradePromotingFlow.setProcessFlow("参与了促销");
			retailTradePromotingFlows.add(retailTradePromotingFlow);
		}
		//
		RetailTradePromotingFlow retailTradeCouponFlow = new RetailTradePromotingFlow();
		retailTradeCouponFlow.setProcessFlow("使用了一张优惠券");
		retailTradePromotingFlows.add(retailTradeCouponFlow);
		//
		retailTradePromoting.setListSlave1(retailTradePromotingFlows);
		retailTradePromotingList.add(retailTradePromoting);
		rt.setListSlave2(retailTradePromotingList);

		// 生成零售单优惠券使用表
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeCreate.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);

		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testOnCreateSuccess1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("上传一张零售单(数量,金额累加)");
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		double oldTtotalAmountToday = realTimeSST.getTotalAmountToday();
		int oldTotalNOToday = realTimeSST.getTotalNOToday();

		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		double newTotalAmountToday = realTimeSST.getTotalAmountToday();
		int newTotalNOToday = realTimeSST.getTotalNOToday();

		Assert.assertTrue(GeneralUtil.sub(rt.getAmount(), GeneralUtil.sub(newTotalAmountToday, oldTtotalAmountToday)) < BaseModel.TOLERANCE, "销售一单，记录今日销售金额和预期的不一致");
		Assert.assertTrue(newTotalNOToday - oldTotalNOToday == 1, "成功销售一单，但是记录销售单数和预期的不一样");
	}

	@Test
	public void testOnCreateSuccess2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("上传一批零售单(数量,金额累加)");
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		double oldTtotalAmountToday = realTimeSST.getTotalAmountToday();
		int oldTotalNOToday = realTimeSST.getTotalNOToday();

		RetailTrade retailTradeA = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		createN(retailTradeA, retailTradeB, retailTradeC, EnumErrorCode.EC_NoError);
		// 计算本次批量上传的总金额
		double totalAmount = retailTradeA.getAmount();
		totalAmount = GeneralUtil.sum(retailTradeB.getAmount(), totalAmount);
		totalAmount = GeneralUtil.sum(retailTradeC.getAmount(), totalAmount);

		double newTotalAmountToday = realTimeSST.getTotalAmountToday();
		int newTotalNOToday = realTimeSST.getTotalNOToday();

		Assert.assertTrue(GeneralUtil.sub(totalAmount, GeneralUtil.sub(newTotalAmountToday, oldTtotalAmountToday)) < BaseModel.TOLERANCE, "销售一单，记录今日销售金额和预期的不一致");
		Assert.assertTrue(newTotalNOToday - oldTotalNOToday == 3, "成功销售一单，但是记录销售单数和预期的不一样");
	}

	@Test
	public void testOnCreateSuccess3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("上传一张退货单(数量,金额不变)");
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		double oldTtotalAmountToday = realTimeSST.getTotalAmountToday();
		int oldTotalNOToday = realTimeSST.getTotalNOToday();
		//
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		RetailTrade retailTradInDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
		//
		double newTotalAmountToday = realTimeSST.getTotalAmountToday();
		int newTotalNOToday = realTimeSST.getTotalNOToday();
		//
		Assert.assertTrue(GeneralUtil.sub(rt.getAmount(), GeneralUtil.sub(newTotalAmountToday, oldTtotalAmountToday)) < BaseModel.TOLERANCE, "销售一单，记录今日销售金额和预期的不一致");
		Assert.assertTrue(newTotalNOToday - oldTotalNOToday == 1, "成功销售一单，但是记录销售单数和预期的不一样");

		oldTtotalAmountToday = newTotalAmountToday;
		oldTotalNOToday = newTotalNOToday;
		// 创建退货单
		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setSourceID(retailTradInDB.getID());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);

		newTotalAmountToday = realTimeSST.getTotalAmountToday();
		newTotalNOToday = realTimeSST.getTotalNOToday();
		Assert.assertTrue(GeneralUtil.sub(newTotalAmountToday, oldTtotalAmountToday) < BaseModel.TOLERANCE, "上传一张退货单，记录的销售单数和销售金额发生了改变");
		Assert.assertTrue(newTotalNOToday == oldTotalNOToday, "创建了一张退货单。最新的销售单数和预期的不一样");
	}

	@Test
	public void testOnCreateSuccess4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("批量上传零售单和退货单(只累加零售单的数量和金额)");
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		RetailTrade retailTradInDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		double oldTtotalAmountToday = realTimeSST.getTotalAmountToday();
		int oldTotalNOToday = realTimeSST.getTotalNOToday();

		// 创建退货单
		RetailTrade retailTradeA = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		retailTradeA.setSourceID(retailTradInDB.getID());
		//
		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		createN(retailTradeA, retailTradeB, retailTradeC, EnumErrorCode.EC_NoError);

		// 计算本次批量上传零售单的总金额
		double totalAmount = retailTradeB.getAmount();
		totalAmount = GeneralUtil.sum(retailTradeC.getAmount(), totalAmount);
		//
		double newTotalAmountToday = realTimeSST.getTotalAmountToday();
		int newTotalNOToday = realTimeSST.getTotalNOToday();
		//
		Assert.assertTrue(GeneralUtil.sub(totalAmount, GeneralUtil.sub(newTotalAmountToday, oldTtotalAmountToday)) < BaseModel.TOLERANCE, "成功批量销售，记录今日销售金额和预期的不一致");
		Assert.assertTrue(newTotalNOToday - oldTotalNOToday == 2, "成功批量销售，但是记录销售单数和预期的不一样");
	}

	@Test
	public void testOnCreateSuccess5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("批量上传零售单时，部分零售单部分成功（数量,金额都进行累加）");
		// 无法实现此用例。此case的部分成功的情况一般都是查询从表出现失败
	}

	@Test
	public void testOnCreateSuccess6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("模拟前一天有零售单上传，到0点时调用reset方法重置零售总额)");
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
		//
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		double newTotalAmountToday = realTimeSST.getTotalAmountToday();
		int newTotalNOToday = realTimeSST.getTotalNOToday();
		Assert.assertTrue(newTotalAmountToday > 0 && newTotalNOToday > 0, "成功批量销售，记录今日销售金额和预期的不一致");
		//
		SalesStatistics salesStatistics = new SalesStatistics();
		salesStatistics.reset();
		//
		Assert.assertTrue(realTimeSST.getTotalAmountToday() == 0 && realTimeSST.getTotalNOToday() == 0, "零售总额没有正确重置清零");
	}

	// @Test
	// public void testOnCreateSuccess7() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("跨天上传一批零售单(数量为这批零售单单数，金额为这批零售单金额)");
	// RealTimeSalesStatisticsToday realTimeSST =
	// RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
	// RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2,
	// NORMAL_COMMODIY_ID3);
	// BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt,
	// Shared.DBName_Test);
	//
	// RetailTradeAction.bInTestNGMode_MokTomorrow = true;// 模拟跨天
	// RetailTrade retailTradeA = getRetailTrade(NORMAL_COMMODIY_ID1,
	// NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
	// RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1,
	// SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
	// RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1,
	// Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
	// createN(retailTradeA, retailTradeB, retailTradeC, EnumErrorCode.EC_NoError);
	// RetailTradeAction.bInTestNGMode_MokTomorrow = false;// 恢复模拟跨天
	//
	// // 计算本次批量上传的总金额
	// double totalAmount = retailTradeA.getAmount();
	// totalAmount = GeneralUtil.sum(retailTradeB.getAmount(), totalAmount);
	// totalAmount = GeneralUtil.sum(retailTradeC.getAmount(), totalAmount);
	//
	// double newTotalAmountToday = realTimeSST.getTotalAmountToday();
	// int newTotalNOToday = realTimeSST.getTotalNOToday();
	// // 跨天，上一天的不累加
	// Assert.assertTrue(GeneralUtil.sub(totalAmount, newTotalAmountToday) <
	// BaseModel.TOLERANCE, "跨天批量销售N单，记录今日销售金额和预期的不一致");
	// Assert.assertTrue(newTotalNOToday == 3, "跨天批量销售N单，但是记录销售单数和预期的不一样");
	// }

	@Test
	public void testOnCreateSuccess8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("上传一张错误的单(数量，金额不变)");
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		double oldTotalAmountToday = realTimeSST.getTotalAmountToday();
		int oldTotalNOToday = realTimeSST.getTotalNOToday();

		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSn("错误的SN");
		// 重复创建零售单rt
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBossOfAnotherCompany) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);

		double newTotalAmountToday = realTimeSST.getTotalAmountToday();
		int newTotalNOToday = realTimeSST.getTotalNOToday();
		//
		Assert.assertTrue(GeneralUtil.sub(oldTotalAmountToday, newTotalAmountToday) < BaseModel.TOLERANCE, "上传重复的单后进行更新了今日的销售额");
		Assert.assertTrue(oldTotalNOToday == newTotalNOToday, "上传重复的单后进行更新了今日的销售单数");
	}

	@Test
	public void testOnCreateSuccess9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("上传一批错误的单(数量，金额不变)");
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		double oldTtotalAmountToday = realTimeSST.getTotalAmountToday();
		int oldTotalNOToday = realTimeSST.getTotalNOToday();

		RetailTrade retailTradeA = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		retailTradeA.setSn("错误的SN");
		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		retailTradeB.setSn("错误的SN");
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTradeA);
		retailTradeList.add(retailTradeB);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);

		double newTotalAmountToday = realTimeSST.getTotalAmountToday();
		int newTotalNOToday = realTimeSST.getTotalNOToday();
		//
		Assert.assertTrue(GeneralUtil.sub(newTotalAmountToday, oldTtotalAmountToday) < BaseModel.TOLERANCE, "批量销售有问题的单，记录今日销售金额和预期的不一致");
		Assert.assertTrue(newTotalNOToday - oldTotalNOToday == 0, "批量销售有问题的单，但是记录销售单数和预期的不一样");
	}

	@Test
	public void testOnCreateSuccess10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("上传一张重复的单(数量，金额不变)");
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		RetailTrade retailTradInDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
		retailTradInDB.setReturnObject(EnumBoolean.EB_Yes.getIndex());

		double oldTotalAmountToday = realTimeSST.getTotalAmountToday();
		int oldTotalNOToday = realTimeSST.getTotalNOToday();

		// 重复创建零售单rt
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);

		double newTotalAmountToday = realTimeSST.getTotalAmountToday();
		int newTotalNOToday = realTimeSST.getTotalNOToday();
		//
		Assert.assertTrue(GeneralUtil.sub(oldTotalAmountToday, newTotalAmountToday) < BaseModel.TOLERANCE, "上传重复的单后进行更新了今日的销售额");
		Assert.assertTrue(oldTotalNOToday == newTotalNOToday, "上传重复的单后进行更新了今日的销售单数");
	}

	@Test
	public void testOnCreateSuccess11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("上传一批单，其中包含重复的.(数量，金额不会将重复的单算上)");
		RealTimeSalesStatisticsToday realTimeSST = RetailTradeAction.todaysSaleCache.get(Shared.DBName_Test);
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		RetailTrade retailTradInDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
		retailTradInDB.setReturnObject(EnumBoolean.EB_Yes.getIndex());

		double oldTtotalAmountToday = realTimeSST.getTotalAmountToday();
		int oldTotalNOToday = realTimeSST.getTotalNOToday();

		RetailTrade retailTradeA = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTradeA);
		retailTradeList.add(retailTradInDB); // 重复上传此单
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);

		// 计算本次批量上传零售单的总金额
		double totalAmount = retailTradeA.getAmount();
		//
		double newTotalAmountToday = realTimeSST.getTotalAmountToday();
		int newTotalNOToday = realTimeSST.getTotalNOToday();
		//
		Assert.assertTrue(GeneralUtil.sub(totalAmount, GeneralUtil.sub(newTotalAmountToday, oldTtotalAmountToday)) < BaseModel.TOLERANCE, "成功批量销售，记录今日销售金额和预期的不一致");
		Assert.assertTrue(newTotalNOToday - oldTotalNOToday == 1, "成功批量销售，但是记录销售单数和预期的不一样");
	}

	@Test
	public void testCheckCreateCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("零售单原始金额不满足优惠券(范围:全场)使用阀值，返回空");

		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setLeastAmount(1000000d);
		Coupon couponInDB = BaseCouponTest.createViaMapper(coupon);
		//
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponInDB.getID());
		CouponCode couponCodeInDB = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		Assert.assertTrue(rt.getAmount() < coupon.getLeastAmount(), "测试数据有错误，应让零售单原始金额<优惠券的起用金额");
		appendListSlave2(rt);
		//
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeInDB.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "未能检查出零售金额小于优惠券阀值");
	}

	@Test
	public void testCheckCreateCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("零售单原始金额不满足优惠券(范围:指定商品)使用阀值,返回空");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setLeastAmount(1000000d);
		coupon.setScope(CouponScope.EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponInDB = BaseCouponTest.createViaMapper(coupon);
		// 指定商品
		CouponScope couponScope = BaseCouponScopeTest.DataInput.getCouponScope(couponInDB.getID(), NORMAL_COMMODIY_ID1);
		BaseCouponScopeTest.createViaMapper(couponScope);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponInDB.getID());
		CouponCode couponCodeInDB = BaseCouponCodeTest.createViaMapper(couponCode);
		// 创建零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(0);
		double dTotalAmountOfNORMAL_COMMODIY_ID1 = rtc.getPriceOriginal() * rtc.getNO();
		Assert.assertTrue(dTotalAmountOfNORMAL_COMMODIY_ID1 < coupon.getLeastAmount(), "测试数据有错误，应让零售单原始金额<优惠券的起用金额");
		appendListSlave2(rt);
		//
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeInDB.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "未能检查出零售金额小于优惠券阀值");
	}

	@Test
	public void testCheckCreateCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("创建零售单时,使用的优惠券已经到了结束时间，但是此零售单是在优惠券起用时间时生成的。创建零售单成功，核销优惠券成功");

		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		// 设置优惠券已经过期
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		coupon.setEndDateTime(DatetimeUtil.getDate(new Date(), -10000));
		Coupon couponInDB = BaseCouponTest.createViaMapper(coupon);
		//
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponInDB.getID());
		CouponCode couponCodeInDB = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSaleDatetime(DatetimeUtil.getDate(coupon.getEndDateTime(), -10000)); // 设置此单的生成时间在优惠券结束前的时间
		appendListSlave2(rt);
		//
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeInDB.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);

		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCheckCreateCase4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("创建零售单时，使用的优惠券不在使用时间内，但是此零售单是在优惠券可用时间时生成的。创建零售单成功， 核销优惠券成功。");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		BaseCouponTest.setCurrentWeekType(coupon);
		coupon.setBeginTime("10:00:00");
		coupon.setEndTime("12:00:00");
		Coupon couponInDB = BaseCouponTest.createViaMapper(coupon);
		//
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponInDB.getID());
		CouponCode couponCodeInDB = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 11); // 在coupon的时间段中，10点到12点间
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		rt.setSaleDatetime(cal.getTime()); // 设置此单的生成时间在优惠券能够使用的时间
		appendListSlave2(rt);
		//
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeInDB.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);

		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCheckCreateCase5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("创建零售单时,使用的优惠券不在起用时间。返回空。");

		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		// 设置优惠券还没开始
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), 1));
		coupon.setEndDateTime(DatetimeUtil.getDays(new Date(), 2));
		Coupon couponInDB = BaseCouponTest.createViaMapper(coupon);
		//
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponInDB.getID());
		CouponCode couponCodeInDB = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSaleDatetime(DatetimeUtil.getDays(coupon.getBeginDateTime(), -1)); // 设置此单的生成时间在优惠券起用时间前
		appendListSlave2(rt);
		//
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeInDB.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "未能检查出零售使用此优惠券的时候，该优惠券还没到起用时间");
	}

	@Test
	public void testCheckCreateCase6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("创建零售单时，使用的优惠券已到结束时间。返回空。");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		// 设置优惠券已经过期
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		coupon.setEndDateTime(DatetimeUtil.getDate(new Date(), -10000));
		Coupon couponInDB = BaseCouponTest.createViaMapper(coupon);
		//
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponInDB.getID());
		CouponCode couponCodeInDB = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setSaleDatetime(new Date()); // 设置此单的生成时间在当前
		appendListSlave2(rt);
		//
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeInDB.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "未能检查出零售使用此优惠券的时候，该优惠券已经过了结束时间");
	}

	@Test
	public void testCheckCreateCase7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("当前时间是在优惠券可用时间。但是创建零售单时,使用的优惠券并不在优惠券可用时间内。返回空");

		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		BaseCouponTest.setCurrentWeekType(coupon);
		coupon.setBeginTime("10:00:00");
		coupon.setEndTime("12:00:00");
		Coupon couponInDB = BaseCouponTest.createViaMapper(coupon);
		//
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponInDB.getID());
		CouponCode couponCodeInDB = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 9);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		rt.setSaleDatetime(cal.getTime()); // 设置此单的生成时间不在优惠券能够使用的时间
		appendListSlave2(rt);
		//
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeInDB.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "未能检查出零售使用此优惠券的时候，该优惠券不在可用的时间");
	}

	@Test
	public void testCheckCreateCase8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("零售单原始金额满足优惠券(范围：全场)使用阀值。但是其参与过促销,促销后金额不满足优惠券使用阀值。返回空");
		// 看代码很难看出和其他的case有什么区别。因为对于全场的优惠券，他们计算方式都是一样的，只有指定范围的券计算方式才不一样
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setLeastAmount(600d);
		Coupon couponInDB = BaseCouponTest.createViaMapper(coupon);
		//
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponInDB.getID());
		CouponCode couponCodeInDB = BaseCouponCodeTest.createViaMapper(couponCode);
		// 零售单拥有俩条计算过程
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3); // 原始金额600d
		rt.setAmount(595d); // 假设被促销减去了5元
		rt.setAmountCash(rt.getAmount());
		appendListSlave2(rt);
		//
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeInDB.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);
		// 修改零售单商品的退货价
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "未能检查出零售单参与促销后金额不满足阀值");
	}

	@Test
	public void testCheckCreateCase9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("零售单原始金额满足优惠券(范围：指定商品)。但是参与过促销后,零售单金额就不满足优惠券使用阀值。返回空");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setLeastAmount(200d);
		coupon.setReduceAmount(3d);
		coupon.setScope(CouponScope.EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponInDB = BaseCouponTest.createViaMapper(coupon);
		// 指定商品
		CouponScope couponScope = BaseCouponScopeTest.DataInput.getCouponScope(couponInDB.getID(), NORMAL_COMMODIY_ID1);
		BaseCouponScopeTest.createViaMapper(couponScope);
		//
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponInDB.getID());
		CouponCode couponCodeInDB = BaseCouponCodeTest.createViaMapper(couponCode);

		// 零售单拥有俩条计算过程
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3); // 原始金额600d
		rt.setAmount(594d); // 原始金额600-优惠券3-促销金额3元
		rt.setAmountCash(594d);
		rt.setCouponAmount(coupon.getReduceAmount());
		// 修改退货价
		for (Object object : rt.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) object;
			rtc.setPriceReturn(GeneralUtil.sub(rtc.getPriceOriginal(), 2)); // 一共卖出三个商品，促销和优惠券免去6元.所以他们的退货价-2
		}

		// 添加计算过程
		List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		List<RetailTradePromotingFlow> retailTradePromotingFlowList = new ArrayList<RetailTradePromotingFlow>();
		// 添加参与了促销的描述
		RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
		retailTradePromotingFlow.setPromotionID(1);
		retailTradePromotingFlow.setProcessFlow("参与了减3元促销");
		retailTradePromotingFlowList.add(retailTradePromotingFlow);
		// 添加使用优惠券的描述
		RetailTradePromotingFlow retailTradePromotingFlow2 = new RetailTradePromotingFlow();
		retailTradePromotingFlow2.setPromotionID(0);
		retailTradePromotingFlow2.setProcessFlow("使用了优惠券");
		retailTradePromotingFlowList.add(retailTradePromotingFlow2);
		//
		retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
		retailTradePromotingList.add(retailTradePromoting);
		rt.setListSlave2(retailTradePromotingList);

		//
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeInDB.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "未能检查出指定商品的优惠券，在原始金额满足但参与 促销后金额不满足的情况");
	}

	@Test
	public void testCheckCreateCase10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("零售单的Amount金额不满足优惠券的阀值，那是因为使用了优惠券，而不满足的，此零售单能够通过检查");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setLeastAmount(100);
		Coupon couponInDB = BaseCouponTest.createViaMapper(coupon);
		//
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponInDB.getID());
		CouponCode couponCodeInDB = BaseCouponCodeTest.createViaMapper(couponCode);
		// 零售单拥有俩条计算过程
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		rt.setAmount(95d);
		rt.setAmountCash(rt.getAmount());
		rt.setCouponAmount(5d);
		//
		appendListSlave2(rt);
		//
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeInDB.getID(), rt.getID());
		List<BaseModel> retailTradeCouponList = new ArrayList<BaseModel>();
		retailTradeCouponList.add(retailTradeCoupon);
		rt.setListSlave3(retailTradeCouponList);
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

	}

	@Test
	public void testCheckCreateCase11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("零售单的计算过程中从表描述不存在");
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		// 添加计算过程
		List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		retailTradePromotingList.add(retailTradePromoting);
		rt.setListSlave2(retailTradePromotingList);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "未能检查出计算过程从表不存在");
	}

	@Test
	public void testCreateN1Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:批量创建A,B,C三张零售单。其中零售单A是错误的.期望结果：返回EC_PartSuccess的错误码.成功创建B,C零售单，A创建失败.");
		RetailTrade retailTradeA = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		retailTradeA.setSn("错误的SN");

		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		Map<Integer, Commodity> simpleCommodityList2 = null;
		boolean bIncludeDeletedCommodity2 = false;
		if (retailTradeB.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity2 = true;
		}
		simpleCommodityList2 = getSimpleCommodityListFromRetailTrade(retailTradeB, Shared.DBName_Test, bIncludeDeletedCommodity2, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale2 = queryWarehousingBeforeSale(simpleCommodityList2, Shared.DBName_Test, retailTradeB, warehousingBO);

		RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		Map<Integer, Commodity> simpleCommodityList3 = null;
		boolean bIncludeDeletedCommodity3 = false;
		if (retailTradeC.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity3 = true;
		}
		simpleCommodityList3 = getSimpleCommodityListFromRetailTrade(retailTradeC, Shared.DBName_Test, bIncludeDeletedCommodity3, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale3 = queryWarehousingBeforeSale(simpleCommodityList3, Shared.DBName_Test, retailTradeC, warehousingBO);

		// 将零售单ABC打包成一个集合并转为JSON格式
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTradeA);
		retailTradeList.add(retailTradeB);
		retailTradeList.add(retailTradeC);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
		//
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		RetailTrade bmOfDB = new RetailTrade();
		List<BaseModel> baseModeList = bmOfDB.parseN(returnJson.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(baseModeList.size() == 2, "批量上传，预期成功2张单，但实际结果却不是");

		Map<Integer, List<Warehousing>> mapAfterSale2 = queryWarehousingAfterSale(mapBeforeSale2, Shared.DBName_Test, warehousingBO);
		if (retailTradeB.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		Map<Integer, List<Warehousing>> mapAfterSale3 = queryWarehousingAfterSale(mapBeforeSale3, Shared.DBName_Test, warehousingBO);
		if (retailTradeC.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		// 批量检查
		for (BaseModel bm : baseModeList) {
			RetailTrade retailTrade = (RetailTrade) bm;
			if (retailTrade.getSn().equals(retailTradeB.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeB, mapBeforeSale2, mapAfterSale2, simpleCommodityList2, mapBO, Shared.DBName_Test, true);
			}
			if (retailTrade.getSn().equals(retailTradeC.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeC, mapBeforeSale3, mapAfterSale3, simpleCommodityList3, mapBO, Shared.DBName_Test, true);
			}
		}
	}

	@Test
	public void testCreateN2Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:批量创建A,B,C三张零售单。其中零售单B是错误的.期望结果：返回EC_PartSuccess的错误码.成功创建A,C零售单，B创建失败.");
		RetailTrade retailTradeA = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		Map<Integer, Commodity> simpleCommodityList1 = null;
		boolean bIncludeDeletedCommodity1 = false;
		if (retailTradeA.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity1 = true;
		}
		simpleCommodityList1 = getSimpleCommodityListFromRetailTrade(retailTradeA, Shared.DBName_Test, bIncludeDeletedCommodity1, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale1 = queryWarehousingBeforeSale(simpleCommodityList1, Shared.DBName_Test, retailTradeA, warehousingBO);

		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		retailTradeB.setSn("错误的SN");

		RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		Map<Integer, Commodity> simpleCommodityList3 = null;
		boolean bIncludeDeletedCommodity3 = false;
		if (retailTradeC.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity3 = true;
		}
		simpleCommodityList3 = getSimpleCommodityListFromRetailTrade(retailTradeC, Shared.DBName_Test, bIncludeDeletedCommodity3, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale3 = queryWarehousingBeforeSale(simpleCommodityList3, Shared.DBName_Test, retailTradeC, warehousingBO);

		// 将零售单ABC打包成一个集合并转为JSON格式
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTradeA);
		retailTradeList.add(retailTradeB);
		retailTradeList.add(retailTradeC);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
		//
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		RetailTrade bmOfDB = new RetailTrade();
		List<BaseModel> baseModeList = bmOfDB.parseN(returnJson.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(baseModeList.size() == 2, "批量上传，预期成功2张单，但实际结果却不是");

		Map<Integer, List<Warehousing>> mapAfterSale1 = queryWarehousingAfterSale(mapBeforeSale1, Shared.DBName_Test, warehousingBO);
		if (retailTradeA.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		Map<Integer, List<Warehousing>> mapAfterSale3 = queryWarehousingAfterSale(mapBeforeSale3, Shared.DBName_Test, warehousingBO);
		if (retailTradeC.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		// 批量检查
		for (BaseModel bm : baseModeList) {
			RetailTrade retailTrade = (RetailTrade) bm;
			if (retailTrade.getSn().equals(retailTradeA.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeA, mapBeforeSale1, mapAfterSale1, simpleCommodityList1, mapBO, Shared.DBName_Test, true);
			}
			if (retailTrade.getSn().equals(retailTradeC.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeC, mapBeforeSale3, mapAfterSale3, simpleCommodityList3, mapBO, Shared.DBName_Test, true);
			}
		}
	}

	@Test
	public void testCreateN3Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:批量创建A,B,C三张零售单。其中零售单C是错误的.期望结果：返回EC_PartSuccess的错误码.成功创建A,B零售单，C创建失败.");
		RetailTrade retailTradeA = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		Map<Integer, Commodity> simpleCommodityList1 = null;
		boolean bIncludeDeletedCommodity1 = false;
		if (retailTradeA.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity1 = true;
		}
		simpleCommodityList1 = getSimpleCommodityListFromRetailTrade(retailTradeA, Shared.DBName_Test, bIncludeDeletedCommodity1, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale1 = queryWarehousingBeforeSale(simpleCommodityList1, Shared.DBName_Test, retailTradeA, warehousingBO);

		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		Map<Integer, Commodity> simpleCommodityList2 = null;
		boolean bIncludeDeletedCommodity2 = false;
		if (retailTradeB.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity2 = true;
		}
		simpleCommodityList2 = getSimpleCommodityListFromRetailTrade(retailTradeB, Shared.DBName_Test, bIncludeDeletedCommodity2, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale2 = queryWarehousingBeforeSale(simpleCommodityList2, Shared.DBName_Test, retailTradeB, warehousingBO);

		RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		retailTradeC.setSn("错误的SN");
		// 将零售单ABC打包成一个集合并转为JSON格式
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTradeA);
		retailTradeList.add(retailTradeB);
		retailTradeList.add(retailTradeC);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
		//
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		RetailTrade bmOfDB = new RetailTrade();
		List<BaseModel> baseModeList = bmOfDB.parseN(returnJson.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(baseModeList.size() == 2, "批量上传，预期成功2张单，但实际结果却不是");

		Map<Integer, List<Warehousing>> mapAfterSale1 = queryWarehousingAfterSale(mapBeforeSale1, Shared.DBName_Test, warehousingBO);
		if (retailTradeA.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		Map<Integer, List<Warehousing>> mapAfterSale2 = queryWarehousingAfterSale(mapBeforeSale2, Shared.DBName_Test, warehousingBO);
		if (retailTradeB.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		// 批量检查
		for (BaseModel bm : baseModeList) {
			RetailTrade retailTrade = (RetailTrade) bm;
			if (retailTrade.getSn().equals(retailTradeA.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeA, mapBeforeSale1, mapAfterSale1, simpleCommodityList1, mapBO, Shared.DBName_Test, true);
			}
			if (retailTrade.getSn().equals(retailTradeB.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeB, mapBeforeSale2, mapAfterSale2, simpleCommodityList2, mapBO, Shared.DBName_Test, true);
			}
		}
	}

	protected void createN(RetailTrade retailTradeA, RetailTrade retailTradeC, RetailTrade retailTradeB, EnumErrorCode eec) throws Exception {
		Map<Integer, Commodity> simpleCommodityList1 = null;
		boolean bIncludeDeletedCommodity1 = false;
		if (retailTradeA.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity1 = true;
		}
		simpleCommodityList1 = getSimpleCommodityListFromRetailTrade(retailTradeA, Shared.DBName_Test, bIncludeDeletedCommodity1, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale1 = queryWarehousingBeforeSale(simpleCommodityList1, Shared.DBName_Test, retailTradeA, warehousingBO);

		Map<Integer, Commodity> simpleCommodityList2 = null;
		boolean bIncludeDeletedCommodity2 = false;
		if (retailTradeB.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity2 = true;
		}
		simpleCommodityList2 = getSimpleCommodityListFromRetailTrade(retailTradeB, Shared.DBName_Test, bIncludeDeletedCommodity2, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale2 = queryWarehousingBeforeSale(simpleCommodityList2, Shared.DBName_Test, retailTradeB, warehousingBO);

		Map<Integer, Commodity> simpleCommodityList3 = null;
		boolean bIncludeDeletedCommodity3 = false;
		if (retailTradeC.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity3 = true;
		}
		simpleCommodityList3 = getSimpleCommodityListFromRetailTrade(retailTradeC, Shared.DBName_Test, bIncludeDeletedCommodity3, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale3 = queryWarehousingBeforeSale(simpleCommodityList3, Shared.DBName_Test, retailTradeC, warehousingBO);

		// 将零售单ABC打包成一个集合并转为JSON格式
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTradeA);
		retailTradeList.add(retailTradeB);
		retailTradeList.add(retailTradeC);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, eec);
		//
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		RetailTrade bmOfDB = new RetailTrade();
		List<BaseModel> baseModeList = bmOfDB.parseN(returnJson.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(baseModeList.size() == 3, "批量上传，预期成功3张单，但实际结果却不是");

		Map<Integer, List<Warehousing>> mapAfterSale1 = queryWarehousingAfterSale(mapBeforeSale1, Shared.DBName_Test, warehousingBO);
		if (retailTradeA.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		Map<Integer, List<Warehousing>> mapAfterSale2 = queryWarehousingAfterSale(mapBeforeSale2, Shared.DBName_Test, warehousingBO);
		if (retailTradeB.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		Map<Integer, List<Warehousing>> mapAfterSale3 = queryWarehousingAfterSale(mapBeforeSale3, Shared.DBName_Test, warehousingBO);
		if (retailTradeB.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		// 批量检查
		for (BaseModel bm : baseModeList) {
			RetailTrade retailTrade = (RetailTrade) bm;
			if (retailTrade.getSn().equals(retailTradeA.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeA, mapBeforeSale1, mapAfterSale1, simpleCommodityList1, mapBO, Shared.DBName_Test, true);
			}
			if (retailTrade.getSn().equals(retailTradeB.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeB, mapBeforeSale2, mapAfterSale2, simpleCommodityList2, mapBO, Shared.DBName_Test, true);
			}
			if (retailTrade.getSn().equals(retailTradeC.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeC, mapBeforeSale3, mapAfterSale3, simpleCommodityList3, mapBO, Shared.DBName_Test, true);
			}
		}
	}

	@Test
	public void testCreateN4Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:批量创建A,B,C三张零售单。.期望结果：返回EC_NoError的错误码.成功创建A,B,C零售单");
		RetailTrade retailTradeA = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		createN(retailTradeA, retailTradeB, retailTradeC, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testCreateN5Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:批量创建A,B,C三张零售单。其中零售单A,B,C都为错误的.期望结果：返回EC_PartSuccess的错误码.没创建成功任一张单");
		RetailTrade retailTradeA = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		retailTradeA.setSn("错误的SN");

		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		retailTradeB.setSn("错误的SN");

		RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		retailTradeC.setSn("错误的SN");
		// 将零售单ABC打包成一个集合并转为JSON格式
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTradeA);
		retailTradeList.add(retailTradeB);
		retailTradeList.add(retailTradeC);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
		//
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		RetailTrade bmOfDB = new RetailTrade();
		List<BaseModel> baseModeList = bmOfDB.parseN(returnJson.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(baseModeList.size() == 0, "批量上传，预期成功0张单，但实际结果却不是");
	}

	@Test
	public void testCreateN6Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:创建零售单(有商品状态已删除)，对该零售单中零售单商品进行退货，退货成功");
		// 创建一个商品
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName("准备要删除的商品1" + Shared.generateStringByTime(3));
		c1.setMultiPackagingInfo(
				Shared.generateStringByTime(8) + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(c1, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseBarcodesTest.retrieveNBarcodes(commodity1.getID(), Shared.DBName_Test);
		// 删除该商品
		BaseCommodityTest.deleteCommodityViaAction(commodity1, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		// 创建该商品的零售单A
		RetailTrade retailTradeA = BaseRetailTradeTest.DataInput.getRetailTrade();
		//
		RetailTradeCommodity retailTradeCommodityA = new RetailTradeCommodity();
		retailTradeCommodityA.setCommodityID(commodity1.getID());
		retailTradeCommodityA.setNO(10);
		retailTradeCommodityA.setPriceOriginal(20);
		retailTradeCommodityA.setPriceReturn(20);
		retailTradeCommodityA.setBarcodeID(barcodes1.getID());
		retailTradeCommodityA.setNOCanReturn(10);

		List<RetailTradeCommodity> listRetailTradeCommodityA = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodityA.add(retailTradeCommodityA);

		retailTradeA.setListSlave1(listRetailTradeCommodityA);
		retailTradeA.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTradeA);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 解析返回回来的零售单A
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		RetailTrade retailTradeAReturn = new RetailTrade();
		List<BaseModel> baseModeList = retailTradeAReturn.parseN(returnJson.getString(BaseAction.KEY_ObjectList));
		retailTradeAReturn = (RetailTrade) baseModeList.get(0);
		Assert.assertTrue(retailTradeAReturn.getStatus() == EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex(), "零售单状态不正确：" + retailTradeAReturn.getStatus());
		// 创建零售单A的退货单B
		RetailTrade retailTradeB = BaseRetailTradeTest.DataInput.getRetailTrade();
		//
		RetailTradeCommodity retailTradeCommodityB = new RetailTradeCommodity();
		retailTradeCommodityB.setCommodityID(commodity1.getID());
		retailTradeCommodityB.setNO(10);
		retailTradeCommodityB.setPriceOriginal(20);
		retailTradeCommodityB.setPriceReturn(20);
		retailTradeCommodityB.setBarcodeID(barcodes1.getID());
		retailTradeCommodityB.setNOCanReturn(10);

		List<RetailTradeCommodity> listRetailTradeCommodityB = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodityB.add(retailTradeCommodityB);

		retailTradeB.setListSlave1(listRetailTradeCommodityB);
		retailTradeB.setSourceID(retailTradeAReturn.getID());
		retailTradeB.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		List<RetailTrade> returnRetailTradeList = new ArrayList<RetailTrade>();
		returnRetailTradeList.add(retailTradeB);
		//
		MvcResult mr1 = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(returnRetailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		// 把零售单内的ErrorInfo拿出来检查
		JSONObject returnJson1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		RetailTrade retailTradeBReturn = new RetailTrade();
		List<BaseModel> retrunRetailTradeList = retailTradeBReturn.parseN(returnJson1.getString(BaseAction.KEY_ObjectList));
		retailTradeBReturn = (RetailTrade) retrunRetailTradeList.get(0);
		Assert.assertTrue(retailTradeBReturn.getStatus() == EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex(), "零售单状态不正确：" + retailTradeBReturn.getStatus());
	}

	@Test
	public void testCreateN7Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:批量创建A,B,C三张零售单。其中零售单A为包含删除商品的.期望结果：创建零售单和其退货单均成功，返回EC_NoError的错误码.");
		// 创建一个商品
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName("准备要删除的商品1" + Shared.generateStringByTime(3));
		c1.setMultiPackagingInfo(
				Shared.generateStringByTime(8) + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(c1, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseBarcodesTest.retrieveNBarcodes(commodity1.getID(), Shared.DBName_Test);
		// 删除该商品
		BaseCommodityTest.deleteCommodityViaAction(commodity1, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		// 创建三张零售单
		RetailTrade retailTradeA = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeA.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(NORMAL_COMMODIY_ID1);
		retailTradeCommodity.setNO(10);
		retailTradeCommodity.setPriceOriginal(20);
		retailTradeCommodity.setPriceReturn(20);
		retailTradeCommodity.setBarcodeID(1);
		retailTradeCommodity.setNOCanReturn(10);
		//
		RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
		retailTradeCommodity2.setCommodityID(commodity1.getID());
		retailTradeCommodity2.setNO(10);
		retailTradeCommodity2.setPriceOriginal(20);
		retailTradeCommodity2.setPriceReturn(20);
		retailTradeCommodity2.setBarcodeID(barcodes1.getID());
		retailTradeCommodity2.setNOCanReturn(10);
		//
		RetailTradeCommodity retailTradeCommodity3 = new RetailTradeCommodity();
		retailTradeCommodity3.setCommodityID(NORMAL_COMMODIY_ID2);
		retailTradeCommodity3.setNO(10);
		retailTradeCommodity3.setPriceOriginal(20);
		retailTradeCommodity3.setPriceReturn(20);
		retailTradeCommodity3.setBarcodeID(3);
		retailTradeCommodity3.setNOCanReturn(10);
		//
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodity.add(retailTradeCommodity);
		listRetailTradeCommodity.add(retailTradeCommodity2);
		listRetailTradeCommodity.add(retailTradeCommodity3);
		//
		retailTradeA.setListSlave1(listRetailTradeCommodity);
		retailTradeA.setRemark("retailTradeA");// 备注该零售单为A
		//
		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		retailTradeB.setRemark("retailTradeB");// 备注该零售单为B
		//
		RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		retailTradeC.setRemark("retailTradeC");// 备注该零售单为C
		// 将零售单ABC打包成一个集合并转为JSON格式
		List<RetailTrade> rtList = new ArrayList<RetailTrade>();
		rtList.add(retailTradeA);
		rtList.add(retailTradeB);
		rtList.add(retailTradeC);
		// 请求批量创建零售单
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(rtList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		// 解析返回回来的json数据，看是否返回了3张零售单。
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		RetailTrade retailTrade = new RetailTrade();
		List<BaseModel> bmList = retailTrade.parseN(returnJson.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() == 3, "批量上传，预期成功3张单，但实际结果却不是");
		// 验证零售单状态，零售单A包含删除商品，状态为2
		for (BaseModel bm : bmList) {
			retailTrade = (RetailTrade) bm;
			if (retailTrade.getRemark().equals(retailTradeA.getRemark())) {
				Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex(), "零售单A的状态不正确：" + retailTrade.getStatus());
			} else {
				Assert.assertTrue(retailTrade.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex(), "零售单B或C的状态不正确：" + retailTrade.getStatus());
			}
		}
		//
		// 批量创建零售退货单
		RetailTrade retailTradeA_Return = new RetailTrade();
		RetailTrade retailTradeB_Return = new RetailTrade();
		RetailTrade retailTradeC_Return = new RetailTrade();
		for (BaseModel bm : bmList) { // 遍历返回回来的零售单列表
			RetailTrade rt = (RetailTrade) bm;
			if (rt.getRemark().equals(retailTradeA.getRemark())) { // 备注为A的单对应回备注为A的退货单
				retailTradeA_Return = BaseRetailTradeTest.DataInput.getRetailTrade();
				RetailTradeCommodity retailTradeCommodityA = new RetailTradeCommodity();
				retailTradeCommodityA.setCommodityID(commodity1.getID());
				retailTradeCommodityA.setNO(10);
				retailTradeCommodityA.setPriceOriginal(20);
				retailTradeCommodityA.setPriceReturn(20);
				retailTradeCommodityA.setBarcodeID(barcodes1.getID());
				retailTradeCommodityA.setNOCanReturn(10);
				//
				RetailTradeCommodity retailTradeCommodityB = new RetailTradeCommodity();
				retailTradeCommodityB.setCommodityID(NORMAL_COMMODIY_ID1);
				retailTradeCommodityB.setNO(10);
				retailTradeCommodityB.setPriceOriginal(20);
				retailTradeCommodityB.setPriceReturn(20);
				retailTradeCommodityB.setBarcodeID(1);
				retailTradeCommodityB.setNOCanReturn(10);
				//
				RetailTradeCommodity retailTradeCommodityC = new RetailTradeCommodity();
				retailTradeCommodityC.setCommodityID(NORMAL_COMMODIY_ID2);
				retailTradeCommodityC.setNO(10);
				retailTradeCommodityC.setPriceOriginal(20);
				retailTradeCommodityC.setPriceReturn(20);
				retailTradeCommodityC.setBarcodeID(1);
				retailTradeCommodityC.setNOCanReturn(10);

				List<RetailTradeCommodity> listRetailTradeCommodityA = new ArrayList<RetailTradeCommodity>();
				listRetailTradeCommodityA.add(retailTradeCommodityA);
				listRetailTradeCommodityA.add(retailTradeCommodityB);
				listRetailTradeCommodityA.add(retailTradeCommodityC);
				//
				retailTradeA_Return.setListSlave1(listRetailTradeCommodityA);
				retailTradeA_Return.setSourceID(rt.getID());
				retailTradeA_Return.setReturnObject(EnumBoolean.EB_Yes.getIndex());
				retailTradeA_Return.setRemark(retailTradeA.getRemark());
			} else if (rt.getRemark().equals(retailTradeB.getRemark())) { //
				// 备注为B的单对应回备注为B的退货单
				retailTradeB_Return = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
				retailTradeB_Return.setSourceID(rt.getID());
				retailTradeB_Return.setRemark(retailTradeB.getRemark());
			} else { // 备注为C的单对应回备注为C的退货单
				retailTradeC_Return = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
				retailTradeC_Return.setSourceID(rt.getID());
				retailTradeC_Return.setRemark(retailTradeC.getRemark());
			}
		}
		// 将零售单ABC打包成一个集合并转为JSON格式
		List<RetailTrade> retailTradeReturnList = new ArrayList<RetailTrade>();
		retailTradeReturnList.add(retailTradeA_Return);
		retailTradeReturnList.add(retailTradeB_Return);
		retailTradeReturnList.add(retailTradeC_Return);
		MvcResult mr2 = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeReturnList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);// 成功
		// 解析返回的退货单列表，有一张单是未创建成功而返回的，里面装着错误码和信息
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		RetailTrade returnRetailTrade = new RetailTrade();
		List<BaseModel> returnRetailTradeList = returnRetailTrade.parseN(jsonObject.getString(BaseAction.KEY_ObjectList));
		// 验证错误码和零售单状态
		for (BaseModel bm : returnRetailTradeList) {
			RetailTrade rt = (RetailTrade) bm;
			ErrorInfo errorInfo = rt.getErrorInfo();
			if (rt.getRemark().equals(retailTradeA.getRemark())) { //
				// 零售单A对应的退货单是创建成功的,但零售单商品未创建成功。
				Assert.assertTrue(errorInfo.getErrorCode() == EnumErrorCode.EC_NoError, errorInfo.getErrorMessage());
				Assert.assertTrue(rt.getStatus() == EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex(), "零售单A的状态不正确：" + rt.getStatus());
			} else { // 其他都是创建成功的
				Assert.assertTrue(errorInfo.getErrorCode() == EnumErrorCode.EC_NoError, errorInfo.getErrorMessage());
				Assert.assertTrue(rt.getStatus() == EnumStatusRetailTrade.ESRT_Normal.getIndex(), "零售单B或C的状态不正确：" + rt.getStatus());
			}
		}
	}

	@Test
	public void testCreateN8Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:创建有组合商品的零售单(组合商品状态已删除)，对该零售单中组合商品进行退货，退货成功");
		// 创建一个组合商品
		SubCommodity comm2 = new SubCommodity();
		comm2.setSubCommodityID(NORMAL_COMMODIY_ID2);
		comm2.setSubCommodityNO(1);
		comm2.setPrice(20);
		//
		SubCommodity comm3 = new SubCommodity();
		comm3.setSubCommodityID(NORMAL_COMMODIY_ID3);
		comm3.setSubCommodityNO(1);
		comm3.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm2);
		ListCommodity.add(comm3);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setShelfLife(0);
		c1.setPurchaseFlag(0);
		c1.setRuleOfPoint(0);
		c1.setListSlave1(ListCommodity);
		c1.setType(EnumCommodityType.ECT_Combination.getIndex());
		c1.setName("准备删除的组合商品1" + Shared.generateStringByTime(4));
		c1.setMultiPackagingInfo(
				Shared.generateStringByTime(8) + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		String json = JSONObject.fromObject(c1).toString();
		MvcResult mr6 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c1, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		CommodityCP.verifyCreate(mr6, c1, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		//
		JSONObject o = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		Commodity commodity1 = (Commodity) c1.parse1(o.getString("object"));
		Barcodes barcodes1 = BaseBarcodesTest.retrieveNBarcodes(commodity1.getID(), Shared.DBName_Test);
		// 删除该商品
		BaseCommodityTest.deleteCommodityViaAction(commodity1, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		// 创建该商品的零售单A
		RetailTrade retailTradeA = BaseRetailTradeTest.DataInput.getRetailTrade();
		//
		RetailTradeCommodity retailTradeCommodityA = new RetailTradeCommodity();
		retailTradeCommodityA.setCommodityID(commodity1.getID());
		retailTradeCommodityA.setNO(10);
		retailTradeCommodityA.setPriceOriginal(20);
		retailTradeCommodityA.setPriceReturn(20);
		retailTradeCommodityA.setBarcodeID(barcodes1.getID());
		retailTradeCommodityA.setNOCanReturn(10);

		List<RetailTradeCommodity> listRetailTradeCommodityA = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodityA.add(retailTradeCommodityA);

		retailTradeA.setListSlave1(listRetailTradeCommodityA);
		retailTradeA.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTradeA);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 解析返回回来的零售单A
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		RetailTrade retailTradeAReturn = new RetailTrade();
		List<BaseModel> baseModeList = retailTradeAReturn.parseN(returnJson.getString(BaseAction.KEY_ObjectList));
		retailTradeAReturn = (RetailTrade) baseModeList.get(0);
		Assert.assertTrue(retailTradeAReturn.getStatus() == EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex(), "零售单状态不正确：" + retailTradeAReturn.getStatus());
		// 创建零售单A的退货单B
		RetailTrade retailTradeB = BaseRetailTradeTest.DataInput.getRetailTrade();
		//
		RetailTradeCommodity retailTradeCommodityB = new RetailTradeCommodity();
		retailTradeCommodityB.setCommodityID(commodity1.getID());
		retailTradeCommodityB.setNO(10);
		retailTradeCommodityB.setPriceOriginal(20);
		retailTradeCommodityB.setPriceReturn(20);
		retailTradeCommodityB.setBarcodeID(barcodes1.getID());
		retailTradeCommodityB.setNOCanReturn(10);

		List<RetailTradeCommodity> listRetailTradeCommodityB = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodityB.add(retailTradeCommodityB);

		retailTradeB.setListSlave1(listRetailTradeCommodityB);
		retailTradeB.setSourceID(retailTradeAReturn.getID());
		retailTradeB.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		List<RetailTrade> returnRetailTradeList = new ArrayList<RetailTrade>();
		returnRetailTradeList.add(retailTradeB);
		//
		MvcResult mr1 = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(returnRetailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		// 把零售单内的ErrorInfo拿出来检查
		JSONObject returnJson1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		RetailTrade retailTradeBReturn = new RetailTrade();
		List<BaseModel> retrunRetailTradeList = retailTradeBReturn.parseN(returnJson1.getString(BaseAction.KEY_ObjectList));
		retailTradeBReturn = (RetailTrade) retrunRetailTradeList.get(0);
		Assert.assertTrue(retailTradeBReturn.getStatus() == EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex(), "零售单状态不正确：" + retailTradeBReturn.getStatus());
		Assert.assertTrue(retailTradeBReturn.getErrorInfo().getErrorCode() == EnumErrorCode.EC_NoError, "错误码不正确：" + retailTradeBReturn.getErrorInfo().getErrorMessage());
	}

	protected RetailTrade createRetailTrade(RetailTrade rt) throws Exception {
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 生成零售单促销
		List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
		RetailTradePromoting retailTradePromoting = new RetailTradePromoting();
		List<RetailTradePromotingFlow> retailTradePromotingFlowList = new ArrayList<RetailTradePromotingFlow>();
		RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
		retailTradePromotingFlow.setPromotionID(1);
		retailTradePromotingFlow.setProcessFlow("测试数据aaa");
		retailTradePromotingFlowList.add(retailTradePromotingFlow);
		retailTradePromoting.setListSlave1(retailTradePromotingFlowList);
		retailTradePromotingList.add(retailTradePromoting);
		rt.setListSlave2(retailTradePromotingList);

		return BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
	}

	@Test
	public void testCreateN9Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("批量上传零售单,其中零售单A为重复上传的零售单（零售单A放在上传list的首个）");

		createNExForDuplicatedRetailTrade(0);
	}

	public void createNExForDuplicatedRetailTrade(int duplicatedPosition) throws Exception, InterruptedException, CloneNotSupportedException, UnsupportedEncodingException {
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		RetailTrade retailTradeA = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3); // 重复上传的零售 单
		retailTradeA = createRetailTrade(retailTradeA); //
		// 准备数据的生成时间和实际插入数据库的时间可能有一秒钟的间隔，所以重复的单使用DB返回的
		retailTradeA.setReturnObject(EnumBoolean.EB_Yes.getIndex());

		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		Map<Integer, Commodity> simpleCommodityList2 = null;
		boolean bIncludeDeletedCommodity2 = false;
		if (retailTradeB.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity2 = true;
		}
		simpleCommodityList2 = getSimpleCommodityListFromRetailTrade(retailTradeB, Shared.DBName_Test, bIncludeDeletedCommodity2, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale2 = queryWarehousingBeforeSale(simpleCommodityList2, Shared.DBName_Test, retailTradeB, warehousingBO);

		RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		Map<Integer, Commodity> simpleCommodityList3 = null;
		boolean bIncludeDeletedCommodity3 = false;
		if (retailTradeC.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity3 = true;
		}
		simpleCommodityList3 = getSimpleCommodityListFromRetailTrade(retailTradeC, Shared.DBName_Test, bIncludeDeletedCommodity3, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale = queryWarehousingBeforeSale(simpleCommodityList3, Shared.DBName_Test, retailTradeC, warehousingBO);

		// 将零售单ABC打包成一个集合并转为JSON格式
		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		if (duplicatedPosition == 0) { // 重复上传的单放第1位
			retailTradeList.add(retailTradeA);
		}
		retailTradeList.add(retailTradeB);
		if (duplicatedPosition == 1) {// 重复上传的单放中间
			retailTradeList.add(retailTradeA);
		}
		retailTradeList.add(retailTradeC);
		if (duplicatedPosition == 2) { // 重复上传的单放末尾
			retailTradeList.add(retailTradeA);
		}
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		if (duplicatedPosition == 2) {
			Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated); // Pos那边收到之后，会当成NOError处理
		} else {
			Shared.checkJSONErrorCode(mr);
		}
		//
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		RetailTrade bmOfDB = new RetailTrade();
		List<BaseModel> baseModeList = bmOfDB.parseN(returnJson.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(baseModeList.size() == 3, "批量上传，预期成功3张单，但实际结果却不是");
		for (BaseModel bm : baseModeList) {
			RetailTrade rt = (RetailTrade) bm;
			if (rt.getLocalSN() == retailTradeA.getLocalSN()) {
				Assert.assertTrue((rt.getListSlave2() != null && rt.getListSlave2().size() > 0), "重复上传的零售单,没有返回零售单促销表");
			}
		}

		Map<Integer, List<Warehousing>> mapAfterSale2 = queryWarehousingAfterSale(mapBeforeSale2, Shared.DBName_Test, warehousingBO);
		if (retailTradeB.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		Map<Integer, List<Warehousing>> mapAfterSale3 = queryWarehousingAfterSale(mapBeforeSale, Shared.DBName_Test, warehousingBO);
		if (retailTradeC.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bmOfDB.setIgnoreSlaveListInComparision(true);
		}
		// 批量检查
		for (BaseModel bm : baseModeList) {
			RetailTrade retailTrade = (RetailTrade) bm;
			if (retailTrade.getSn().equals(retailTradeB.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeB, mapBeforeSale2, mapAfterSale2, simpleCommodityList2, mapBO, Shared.DBName_Test, true);
			}
			if (retailTrade.getSn().equals(retailTradeC.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeC, mapBeforeSale, mapAfterSale3, simpleCommodityList3, mapBO, Shared.DBName_Test, true);
			}
		}
	}

	@Test
	public void testCreateN10Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("批量上传零售单,其中零售单B为重复上传的零售单（零售单B放在上传list的中间）");

		createNExForDuplicatedRetailTrade(1);
	}

	@Test
	public void testCreateN11Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("批量上传零售单,其中零售单C为重复上传的零售单（零售单C放在上传list的末尾）");

		createNExForDuplicatedRetailTrade(2);
	}

	@Test
	public void testCreateN12Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("上传的零售单有普通零售单，参与促销的零售单，使用过优惠券的零售单");
		// 普通的零售单
		RetailTrade retailTradeA = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		Map<Integer, Commodity> simpleCommodityList1 = getSimpleCommodityListFromRetailTrade(retailTradeA, Shared.DBName_Test, false, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale1 = queryWarehousingBeforeSale(simpleCommodityList1, Shared.DBName_Test, retailTradeA, warehousingBO);

		// 参与了促销的零售单
		RetailTrade retailTradeB = getRetailTrade(SERVICE_COMMODIY_ID1, SERVICE_COMMODIY_ID2, SERVICE_COMMODIY_ID3);
		appendListSlave2(retailTradeB);
		Map<Integer, Commodity> simpleCommodityList2 = getSimpleCommodityListFromRetailTrade(retailTradeB, Shared.DBName_Test, false, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale2 = queryWarehousingBeforeSale(simpleCommodityList2, Shared.DBName_Test, retailTradeB, warehousingBO);

		// 使用过优惠券的零售单
		RetailTrade retailTradeC = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		appendListSlave2(retailTradeC);
		appendListSlave3(retailTradeC);
		Map<Integer, Commodity> simpleCommodityList3 = getSimpleCommodityListFromRetailTrade(retailTradeC, Shared.DBName_Test, false, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale3 = queryWarehousingBeforeSale(simpleCommodityList3, Shared.DBName_Test, retailTradeC, warehousingBO);

		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTradeA);
		retailTradeList.add(retailTradeB);
		retailTradeList.add(retailTradeC);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject returnJson = JSONObject.fromObject(mr.getResponse().getContentAsString());
		RetailTrade bmOfDB = new RetailTrade();
		List<BaseModel> baseModeList = bmOfDB.parseN(returnJson.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(baseModeList.size() == 3, "批量上传，预期成功3张单，但实际结果却不是");

		// 批量检查
		Map<Integer, List<Warehousing>> mapAfterSale1 = queryWarehousingAfterSale(mapBeforeSale1, Shared.DBName_Test, warehousingBO);
		Map<Integer, List<Warehousing>> mapAfterSale2 = queryWarehousingAfterSale(mapBeforeSale2, Shared.DBName_Test, warehousingBO);
		Map<Integer, List<Warehousing>> mapAfterSale3 = queryWarehousingAfterSale(mapBeforeSale3, Shared.DBName_Test, warehousingBO);
		for (BaseModel bm : baseModeList) {
			RetailTrade retailTrade = (RetailTrade) bm;
			if (retailTrade.getSn().equals(retailTradeA.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeA, mapBeforeSale1, mapAfterSale1, simpleCommodityList1, mapBO, Shared.DBName_Test, true);
			}
			if (retailTrade.getSn().equals(retailTradeB.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeB, mapBeforeSale2, mapAfterSale2, simpleCommodityList2, mapBO, Shared.DBName_Test, true);
			}
			if (retailTrade.getSn().equals(retailTradeC.getSn())) {
				RetailTradeCP.verifyCreate(retailTrade, retailTradeC, mapBeforeSale3, mapAfterSale3, simpleCommodityList3, mapBO, Shared.DBName_Test, true);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private WarehousingCommodity getCommodityWarehousing(Commodity commodity) {
		if (commodity.getCurrentWarehousingID() == 0) {
			// 由于测试需要，将PAGE_SIZE_MAX设置成很大的数
			BaseAction.PAGE_SIZE_MAX = BaseAction.PAGE_SIZE_Infinite;
			Warehousing warehousing = new Warehousing();
			warehousing.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			// 查询所有审核过的入库单
			ErrorInfo ecOut = new ErrorInfo();
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<Warehousing> warehousingList = (List<Warehousing>) warehousingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);
			// 还原PAGE_SIZE_MAX
			BaseAction.PAGE_SIZE_MAX = 50;
			if (warehousingList == null) {
				return null;
			}
			Warehousing tmpWarehousing = new Warehousing();
			tmpWarehousing.setIsASC(EnumBoolean.EB_NO.getIndex());
			Collections.sort(warehousingList, tmpWarehousing);

			for (Warehousing ws : warehousingList) {
				// 在缓存中获取入库单主从表信息
				Warehousing warehousingOfCache = (Warehousing) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Warehousing).read1(ws.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);

				for (Object o : warehousingOfCache.getListSlave1()) {
					WarehousingCommodity wsc = (WarehousingCommodity) o;
					if (wsc.getCommodityID() == commodity.getID()) {
						return wsc;
					}
				}
			}
		} else {
			Warehousing warehousing = new Warehousing();
			warehousing.setID(commodity.getCurrentWarehousingID());
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);

			for (Object o : bmList.get(1)) {
				WarehousingCommodity warehousingCommodity = (WarehousingCommodity) o;
				if (warehousingCommodity.getCommodityID() == commodity.getID()) {
					return warehousingCommodity;
				}
			}
		}
		return null;
	}

	private List<WarehousingCommodity> getWarehousingCommodityList(List<Commodity> commList) {
		WarehousingCommodity wsc = new WarehousingCommodity();
		List<WarehousingCommodity> wscList = new ArrayList<WarehousingCommodity>();
		for (Commodity commodity : commList) {
			if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
				wsc = getCommodityWarehousing(commodity);

				wscList.add(wsc);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				Commodity normalCommodity = new Commodity();
				normalCommodity.setID(commodity.getRefCommodityID());
				normalCommodity.setCurrentWarehousingID(commodity.getCurrentWarehousingID());
				//
				wsc = getCommodityWarehousing(normalCommodity);
				//
				wscList.add(wsc);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
				for (Object o : commodity.getListSlave2()) {
					Commodity normalCommodity = (Commodity) o;
					//
					wsc = getCommodityWarehousing(normalCommodity);
					//
					wscList.add(wsc);
				}
			} else { //
				wscList.add(null);
			}
		}
		return wscList;
	}

	@Test
	public void testCreateListEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case1:正常添加------------------------------------");

		RetailTrade retailTrade1 = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		// 获取零售前的商品信息
		List<Commodity> commList = getCommodityListBeforeSold(retailTrade1);
		// 获取零售前的商品当值入库单
		Warehousing ws = new Warehousing();
		List<WarehousingCommodity> wscList = getWarehousingCommodityList(commList);
		ws.setListSlave1(wscList);

		RetailTrade retailTrade2 = getRetailTrade(Multi_COMMODIY_ID1, Multi_COMMODIY_ID2, Multi_COMMODIY_ID3);
		// 获取零售前的商品信息
		List<Commodity> commList2 = getCommodityListBeforeSold(retailTrade2);
		// 获取零售前的商品当值入库单
		Warehousing ws2 = new Warehousing();
		List<WarehousingCommodity> wscList2 = getWarehousingCommodityList(commList2);
		ws2.setListSlave1(wscList2);

		List<RetailTrade> retailTradeList = new ArrayList<RetailTrade>();
		retailTradeList.add(retailTrade1);
		retailTradeList.add(retailTrade2);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, Shared.toJSONString(retailTradeList))) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

		// 检查创建后返回的对象是否和传入的对象一致
		// JSONObject returnJson =
		// JSONObject.fromObject(mr.getResponse().getContentAsString());

		// RetailTrade retailTrade = new RetailTrade();
		// List<BaseModel> bmList = (List<BaseModel>)
		// retailTrade.parseN(returnJson.getString(BaseAction.KEY_ObjectList));

		// RetailTrade rt1 = (RetailTrade) bmList.get(0);
		// RetailTradeCP.verifyCreate(rt1, retailTrade1, commList, ws, retailTradeBO,
		// retailTradeCommodityBO, posBO, retailTradeCommoditySourceBO, subCommodityBO,
		// warehousingBO, retailTradePromotingBO, retailTradePromotingFlowBO,
		// Shared.DBName_Test);

	}

	@Test
	public void testRetrieveNEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:app端，用10-26位且不以LS开头的零售单单号来查询零售单");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr1 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "1212348321")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr1);
		Shared.checkJSONErrorCode(mr1);
	}

	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE2: 用LocalSN 和 POS_ID来查询零售单");
		int LocalSN = 1;
		MvcResult mr2 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_localSN(), String.valueOf(LocalSN))//
						.param(RetailTrade.field.getFIELD_NAME_pos_ID(), "1")//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")// 防止null不通过
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr2);
		Shared.checkJSONErrorCode(mr2);

		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<Integer> i1 = JsonPath.read(o2, "$.objectList[*].localSN");
		List<Integer> i2 = JsonPath.read(o2, "$.objectList[*].pos_ID");
		assertTrue(i1.size() == i2.size());
		for (int i = 0; i < i1.size(); i++) {
			assertTrue(i1.get(i) == LocalSN);
			assertTrue(i2.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE3: 用POS_ID来查询零售单");
		MvcResult mr3 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_pos_ID(), "1")//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")// 防止null不通过
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr3);
		Shared.checkJSONErrorCode(mr3);

		JSONObject o3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<Integer> i3 = JsonPath.read(o3, "$.objectList[*].pos_ID");
		for (int i = 0; i < i3.size(); i++) {
			assertTrue(i3.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE4:web端，使用空串查询所有零售单");
		RetailTrade rt1 = createRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		MvcResult mr4 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")//

		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr4);
		Shared.checkJSONErrorCode(mr4);

		JSONObject o4 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		List<Integer> i4 = JsonPath.read(o4, "$.objectList[*].ID");
		assertTrue(rt1.getID() == i4.get(0));
	}

	@Test
	public void testRetrieveNEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE5: 用iLocalSN来查询零售单 ");
		int LocalSN = 1;
		MvcResult mr5 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_localSN(), String.valueOf(LocalSN))//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")// 防止null不通过
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr5);

		JSONObject o5 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		List<Integer> i5 = JsonPath.read(o5, "$.objectList[*].LocalSN");
		for (int i = 0; i < i5.size(); i++) {
			assertTrue(i5.get(i) == LocalSN);
		}
	}

	@Test
	public void testRetrieveNEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE6：用时间来查询零售单");
		MvcResult mr6 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")// 防止null不通过
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), "2017/8/6")//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr6);

		JSONObject o6 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		List<String> stringList = JsonPath.read(o6, "$.objectList[*].saleDatetime");
		String string6 = "2017-08-05 23:59:59 000";// 为了检查结果，故将时间推前一分钟
		String string7 = "2018-08-31 00:00:00 000";
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		Date d2 = sdf.parse(string6);
		Date d3 = sdf.parse(string7);
		for (int i = 0; i < stringList.size(); i++) {
			Date d1 = sdf.parse(stringList.get(i));
			Integer i6 = d1.compareTo(d2);
			Integer i7 = d3.compareTo(d1);
			assertTrue(i6 == 1 && i7 == -1);
		}
	}

	@Test
	public void testRetrieveNEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE7: 用VipID来查询零售单");
		MvcResult mr7 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_vipID(), "1")//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")// 防止null不通过
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr7);

		JSONObject o7 = JSONObject.fromObject(mr7.getResponse().getContentAsString());
		List<Integer> i7 = JsonPath.read(o7, "$.objectList[*].vipID");
		for (int i = 0; i < i7.size(); i++) {
			assertTrue(i7.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE8: 用paymentType来查询零售单");
		MvcResult mr8 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_paymentType(), "2")//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")// 防止null不通过
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr8);

		JSONObject o8 = JSONObject.fromObject(mr8.getResponse().getContentAsString());
		List<Integer> i8 = JsonPath.read(o8, "$.objectList[*].paymentType");
		for (int i = 0; i < i8.size(); i++) {
			assertTrue((i8.get(i) & 2) == 2);
		}
	}

	@Test
	public void testRetrieveNEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE9: 用staffID来查询零售单");
		MvcResult mr9 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_staffID(), "5")//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")// 防止null不通过
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr9);

		JSONObject o9 = JSONObject.fromObject(mr9.getResponse().getContentAsString());
		List<Integer> i9 = JsonPath.read(o9, "$.objectList[*].staffID");
		for (int i = 0; i < i9.size(); i++) {
			assertTrue(i9.get(i) == 5);
		}
	}

	@Test
	public void testRetrieveNEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE10:app端，用大于最大长度(26)的零售单单号来查询零售单");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr10 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "1234832180000111112245456452234565")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr10);
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx12() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE12:app端，用等于最大长度(26)且以LS开头的退货单单号来查询零售单");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		// 创建一张零售单
		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);

		MvcResult mr12 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), bmOfDB.getSn())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr12);
		Shared.checkJSONErrorCode(mr12);
		//
		String json1 = mr12.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json1);
		List<ModelBase> list = JsonPath.read(o, "$." + BaseAction.KEY_ObjectList);
		Assert.assertTrue(list.size() > 0, "测试失败！期望的是返回数据");
	}

	@Test
	public void testRetrieveNEx13() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE13:web端，使用零售单存在的商品名称进行模糊查询");
		String Name = "可比克薯片";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), Name)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean Same = false;
		for (String name : list13) {
			if (name.contains(Name)) {
				Same = true;
				break;
			}
		}
		Assert.assertTrue(Same, "CASE13测试失败!返回的商品名称没有包含：" + Name);
	}

	@Test
	public void testRetrieveNEx14() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE14:使用零售单不存在的商品名称进行模糊查询");
		MvcResult mr14 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "不存在的商品名称")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr14);
		Shared.checkJSONErrorCode(mr14);
		//
		String json14 = mr14.getResponse().getContentAsString();
		JSONObject o14 = JSONObject.fromObject(json14);
		List<ModelBase> list14 = JsonPath.read(o14, "$." + BaseAction.KEY_ObjectList);
		Assert.assertTrue(list14.size() == 0, "CASE14测试失败！期望的是不返回数据");
	}

	@Test
	public void testRetrieveNEx15() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE15:web端，使用空串''进行模糊查询");
		MvcResult mr15 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr15);
		Shared.checkJSONErrorCode(mr15);
		//
		String json15 = mr15.getResponse().getContentAsString();
		JSONObject o15 = JSONObject.fromObject(json15);
		List<ModelBase> list15 = JsonPath.read(o15, "$." + BaseAction.KEY_ObjectList);
		Assert.assertTrue(list15.size() > 0, "CASE15测试失败！期望的是多条返回数据");
	}

	@Test
	public void testRetrieveNEx16() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE16:web端，使用NULL进行模糊查询");
		String Name = null;
		MvcResult mr16 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), Name)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr16);
		Shared.checkJSONErrorCode(mr16, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx17() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE17:web端，使用超长字符进行模糊查询");
		MvcResult mr17 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "超长字符超长字符超长字符超长字符超长字符超长字符超长字符超长字符超长字符超长字符")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr17);
		Shared.checkJSONErrorCode(mr17, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx18() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE18:web端，使用非法字符进行模糊查询");
		MvcResult mr18 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "！@#￥%……&*（——）+《》？{}“|")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr18);
		Shared.checkJSONErrorCode(mr18, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx19() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE19:使用零售单商品名称的一部分进行模糊查询");
		String Name = "A";
		MvcResult mr19 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), Name)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr19);
		Shared.checkJSONErrorCode(mr19);
		//
		String json19 = mr19.getResponse().getContentAsString();
		JSONObject o19 = JSONObject.fromObject(json19);
		List<String> list19 = JsonPath.read(o19, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean Same = false;
		for (String name : list19) {
			if (name.contains(Name)) {
				Same = true;
				break;
			}
		}
		Assert.assertTrue(Same, "CASE19测试失败!返回的商品名称没有包含：" + Name);
	}

	@Test
	public void testRetrieveNEx20() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("根据商品名称传入特殊字符“_”模糊查询零售单");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("退货可口可乐1111_5)4（" + System.currentTimeMillis());
		String barcode = "987623" + System.currentTimeMillis();
		commodity.setMultiPackagingInfo(barcode + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";" + commodity.getPriceWholesale() + ";"
				+ commodity.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1, mvc, sessionBoss);
		// 零售
		RetailTrade rt = BaseTestNGSpringContextTest.DataInput.getRetailTradeAndRetailTradeCommodity();
		rt.setPaymentType(1);
		rt.setStaffID(4); // 登录的staffID
		rt.setSn(Shared.generateRetailTradeSN(4));
		//
		RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(0);
		rtc.setCommodityID(commodity1.getID());
		rtc.setBarcodeID(barcodes1.getID());
		// 获取零售前的商品信息
		List<Commodity> commList1 = getCommodityListBeforeSold(rt);
		// 获取零售前的商品当值入库单
		Warehousing ws = new Warehousing();
		List<WarehousingCommodity> wscList = getWarehousingCommodityList(commList1);
		ws.setListSlave1(wscList);
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
		// 模糊查询零售单
		MvcResult mr1 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "_")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr1);
		Shared.checkJSONErrorCode(mr1);
		//
		String json = mr1.getResponse().getContentAsString();
		JSONObject object = JSONObject.fromObject(json);
		// 用当前商品名称对比是否包含特殊字符"_"
		List<String> listName = JsonPath.read(object, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		for (String result : listName) {
			Assert.assertTrue(result.contains("_"), "查询商品名称没有包含_特殊字符");
		}
	}

	@Test
	public void testRetrieveNEx21() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE21:iExcludeReturned = 0，查询出的零售单包含退货单");
		// 创建一张零售单
		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
		Assert.assertTrue(bmOfDB.getSn().equals(rt.getSn() + "_1"), "创建出来的退货单的单号和想退货的零售单单号不一致");

		MvcResult mr4 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_excludeReturned(), "0")//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr4);
		Shared.checkJSONErrorCode(mr4);

		JSONObject o21 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		List<BaseModel> bmList = new RetailTrade().parseN(o21.getString(BaseAction.KEY_ObjectList));
		boolean existReturnTrade = false;
		for (BaseModel bm : bmList) {
			RetailTrade retailTrade = (RetailTrade) bm;
			if (retailTrade.getSourceID() != BaseAction.INVALID_ID) {
				existReturnTrade = true;
			}
		}
		Assert.assertTrue(existReturnTrade, "预期RN存在退货单，但实际结果没有退货单");
	}

	@Test
	public void testRetrieveNEx22() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE22:iExcludeReturned = 1，查询出的零售单不包含退货单");
		// 创建一张零售单
		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);
		Assert.assertTrue(bmOfDB.getSn().equals(rt.getSn() + "_1"), "创建出来的退货单的单号和想退货的零售单单号不一致");

		MvcResult mr4 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_excludeReturned(), "1")//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr4);
		Shared.checkJSONErrorCode(mr4);

		JSONObject o21 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		List<BaseModel> bmList = new RetailTrade().parseN(o21.getString(BaseAction.KEY_ObjectList));
		boolean notExistReturnTrade = true;
		for (BaseModel bm : bmList) {
			RetailTrade retailTrade = (RetailTrade) bm;
			if (retailTrade.getSourceID() != BaseAction.INVALID_ID) {
				notExistReturnTrade = false;
			}
		}
		Assert.assertTrue(notExistReturnTrade, "预期RN不存在退货单，但实际结果存在退货单");
	}

	@Test
	public void testRetrieveNEx23() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("CASE23:web端，0-32位，且首尾有空格，结果不通过");
		MvcResult mr23 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "LS2019454545454556456 ")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr23);
		Shared.checkJSONErrorCode(mr23, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx24() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE24:app端，用长度小于10的零售单单号来查询零售单");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "LS1212348")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx25() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE25:app端，查询零售单时查询内容包含中文");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "LS20190402烤面筋456456")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx26() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE26:app端，查询零售单时查询内容包含不是LS的英文字符");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "LS20190402abc456456")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx27() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE27:app端，查询零售单时查询内容包含LS的英文字符但不在开头");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "20190402LS456456")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx28() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE28:app端，查询零售单时查询内容包含空格，结果不通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "20190402 456456")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx29() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE29:app端，查询零售单时查询内容包含空格，结果不通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		String queryKeyword = null;
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx30() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE30:app端，查询零售单时查询内容包含特殊字符，结果不通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "LS2019081&20*002")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx31() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE31:app端，查询零售单时查询内容长度大于等于10小于等于26且以ls开头，结果通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("退货可口可乐1111_5)4（" + System.currentTimeMillis());
		String barcode = "987623" + System.currentTimeMillis();
		commodity.setMultiPackagingInfo(barcode + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";" + commodity.getPriceWholesale() + ";"
				+ commodity.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1, mvc, sessionBoss);
		// 零售
		RetailTrade rt = BaseTestNGSpringContextTest.DataInput.getRetailTradeAndRetailTradeCommodity();
		rt.setPaymentType(1);
		rt.setStaffID(4); // 登录的staffID
		rt.setSn(Shared.generateRetailTradeSN(4));
		//
		RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(0);
		rtc.setCommodityID(commodity1.getID());
		rtc.setBarcodeID(barcodes1.getID());
		// 获取零售前的商品信息
		List<Commodity> commList1 = getCommodityListBeforeSold(rt);
		// 获取零售前的商品当值入库单
		Warehousing ws = new Warehousing();
		List<WarehousingCommodity> wscList = getWarehousingCommodityList(commList1);
		ws.setListSlave1(wscList);
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "ls" + rt.getSn().substring(2, 14))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr);
		//
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		List<ModelBase> list = JsonPath.read(o, "$." + BaseAction.KEY_ObjectList);
		Assert.assertTrue(list.size() > 0, "测试失败！期望的是返回数据");

	}

	@Test
	public void testRetrieveNEx32() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE32:app端，查询零售单时查询内容长度大于等于10小于等于26且以LS开头，结果通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("退货可口可乐1111_5)4（" + System.currentTimeMillis());
		String barcode = "987623" + System.currentTimeMillis();
		commodity.setMultiPackagingInfo(barcode + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";" + commodity.getPriceWholesale() + ";"
				+ commodity.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1, mvc, sessionBoss);
		// 零售
		RetailTrade rt = BaseTestNGSpringContextTest.DataInput.getRetailTradeAndRetailTradeCommodity();
		rt.setPaymentType(1);
		rt.setStaffID(4); // 登录的staffID
		rt.setSn(Shared.generateRetailTradeSN(4));
		//
		RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(0);
		rtc.setCommodityID(commodity1.getID());
		rtc.setBarcodeID(barcodes1.getID());
		// 获取零售前的商品信息
		List<Commodity> commList1 = getCommodityListBeforeSold(rt);
		// 获取零售前的商品当值入库单
		Warehousing ws = new Warehousing();
		List<WarehousingCommodity> wscList = getWarehousingCommodityList(commList1);
		ws.setListSlave1(wscList);
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), rt.getSn().substring(0, 15))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr);
		//
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		List<ModelBase> list = JsonPath.read(o, "$." + BaseAction.KEY_ObjectList);
		Assert.assertTrue(list.size() > 0, "测试失败！期望的是返回数据");
	}

	@Test
	public void testRetrieveNEx33() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE33:app端，查询零售单时查询内容长度等于26且以LS开头，不以_1结尾，结果不通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "LS2019020201010100011234_2")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx34() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE34:app端，查询零售单时查询内容长度等于26且以ls开头，以_1结尾，结果通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		// 创建一张零售单
		RetailTrade rt = createRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);

		RetailTrade returnRetailTrade = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		returnRetailTrade.setSourceID(rt.getID());
		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, returnRetailTrade, Shared.DBName_Test);

		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "ls" + bmOfDB.getSn().substring(2))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr);
		//
		String json1 = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json1);
		List<ModelBase> list = JsonPath.read(o, "$." + BaseAction.KEY_ObjectList);
		Assert.assertTrue(list.size() > 0, "测试失败！期望的是返回数据");
	}

	@Test
	public void testRetrieveNEx35() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE35:app端，查询零售单时查询内容长度等于26且以ls开头，不以_1结尾，结果不通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "ls2019020201010100011234_2")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNEx36() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE36:app端，查询零售单时查询内容是空串，结果通过");
		// 1号pos机登录
		// sessionBoss = Shared.getPosLoginSession(mvc, 1);
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionPOS)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr);
		//
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		List<ModelBase> list = JsonPath.read(o, "$." + BaseAction.KEY_ObjectList);
		Assert.assertTrue(list.size() > 0, "测试失败！期望的是多条返回数据");

	}

	@Test
	public void testRetrieveNEx37() throws Exception {
		Shared.printTestMethodStartInfo();
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("web请求，查看默认返回的数据是否降序");
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<BaseModel> bmList = new RetailTrade().parseN(o.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (int i = 1; i < bmList.size(); i++) {
			Assert.assertTrue(((RetailTrade) bmList.get(i - 1)).getID() > ((RetailTrade) bmList.get(i)).getID(), "数据返回错误（非降序）");
		}
	}

	@Test
	public void testRetrieveNEx38() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE38：web请求，根据日期(过去一周内)查询零售单时，获得满足需求的结果");

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		Date lastWeek = DatetimeUtil.getDays(new Date(), -7);// 上周
		rt.setSaleDatetime(lastWeek);
		SimpleDateFormat formatter = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		String dateString = formatter.format(lastWeek);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
		//
		MvcResult mr38 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), dateString)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr38);
		//
		String json38 = mr38.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json38);
		List<ModelBase> list = JsonPath.read(o, "$." + BaseAction.KEY_ObjectList);
		Assert.assertTrue(list.size() > 0, "CASE38测试失败！期望的是有返回数据");
	}

	@Test
	public void testRetrieveNEx39() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE39：web请求，根据日期(过去一个月内)查询零售单时，获得满足需求的结果");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// 生成零售单
		RetailTrade rt = getRetailTrade(NORMAL_COMMODIY_ID1, NORMAL_COMMODIY_ID2, NORMAL_COMMODIY_ID3);
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MONTH, -1); // 上个月
		Date lastMonth = ca.getTime();
		rt.setSaleDatetime(lastMonth);
		SimpleDateFormat formatter = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		String dateString = formatter.format(lastMonth);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
		//
		MvcResult mr38 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")// 防止null不通过
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), dateString)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr38);
		//
		String json39 = mr38.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json39);
		List<ModelBase> list = JsonPath.read(o, "$." + BaseAction.KEY_ObjectList);
		Assert.assertTrue(list.size() > 0, "CASE39测试失败！期望的是有返回数据");
	}

	@Test
	public void testRetrieveNEx40() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE40：根据商品关键字查询零售单，只有普通商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 零售单主表1
		// 零售单商品1
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO = retailTradeCommodity1NO;
		// 零售单总额
		final double totalRetailTradeAmount = retailTrade1Amount;
		// 零售总成本
		final double totalpurchasingPrice = purchasingPrice1;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		commodityGet.setName("贝贝" + System.currentTimeMillis() % 10000);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		String commodityIdString = commodityCreated.getID() + ",";
		String barcodeIdString = barcodeCreated.getID() + ",";
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, String.valueOf(purchasingOrderCommNO), String.valueOf(priceSuggestion), commodityIdString,
				barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO };
		double[] warehousingCommPriceArr = { warehousingCommPrice };
		Commodity[] commodityArr = { commodityCreated };
		Barcodes[] barcodesArr = { barcodeCreated };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 创建零售单
		int[] retailTradeCommodityNoArr = { retailTradeCommodity1NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price };
		int[] commodityCreatedArr = { commodityCreated.getID() };
		int[] barcodeCreatedArr = { barcodeCreated.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// RN
		String queryKeyword = commodityGet.getName();
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE40测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx41() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE41：根据商品关键字查询零售单，只有多包装商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 参照商品倍数
		final int refCommodityMultiple = random.nextInt(100) + 2;
		// 零售单主表1
		// 零售单商品1
		int retailTradeCommodity1NO = random.nextInt(100) + 1;
		double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO = retailTradeCommodity1NO;
		// 零售单总额
		final double totalRetailTradeAmount = retailTrade1Amount;
		// 零售总成本
		final double totalpurchasingPrice = purchasingPrice1;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		commodityGet.setName("晶晶" + System.currentTimeMillis() % 10000);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		String commodityIdString = commodityCreated.getID() + ",";
		String barcodeIdString = barcodeCreated.getID() + ",";
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String multiPackageCommName = "晶晶多包装" + Shared.generateStringByTime(4);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + commUpdate.getName() + Shared.generateStringByTime(8) + ","
				+ multiPackageCommName + ";");

		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, String.valueOf(purchasingOrderCommNO), String.valueOf(priceSuggestion), commodityIdString,
				barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO };
		double[] warehousingCommPriceArr = { warehousingCommPrice };
		Commodity[] commodityArr = { commodityCreated };
		Barcodes[] barcodesArr = { barcodeCreated };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		int[] retailTradeCommodityNoArr = { retailTradeCommodity1NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price };
		int[] commodityCreatedArr = { multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// RN
		String queryKeyword = multiPackageCommName;
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE41测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确");
	}

	@Test
	public void testRetrieveNEx42() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE40：根据商品关键字查询零售单，只有组合商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO2) + 1; // 入库商品数量
		// 商品1、2的进货价要相差大一点
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 30) * 1000) / 1000; // 入库商品价格
		// 零售单主表1
		// 零售单商品1
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		//
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO = retailTradeCommodity1NO;
		// 零售单总额
		final double totalRetailTradeAmount = retailTrade1Amount;
		// 零售总成本
		final double totalpurchasingPrice = purchasingPrice1;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		commodityGet.setName("欢欢1" + System.currentTimeMillis() % 10000);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("欢欢2" + System.currentTimeMillis() % 10000);
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commodityIdString = commodityCreated.getID() + "," + commodityCreated2.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcodeCreated2.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("欢欢组合" + System.currentTimeMillis() % 10000);
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO2, priceSuggestion + "," + priceSuggestion2,
				commodityIdString, barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2 };
		Barcodes[] barcodesArr = { barcodeCreated, barcodeCreated2 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 创建零售单
		int[] retailTradeCommodityNoArr = { retailTradeCommodity1NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price };
		int[] commodityCreatedArr = { commodity1Combination.getID() };
		int[] barcodeCreatedArr = { barcodeCombination.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// RN
		String queryKeyword = combinationCommodityGet.getName();
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE42测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确");
	}

	@Test
	public void testRetrieveNEx43() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE43：根据商品关键字查询零售单，只有服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 零售单主表1
		// 零售单商品1
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO = retailTradeCommodity1NO;
		// 零售单总额
		final double totalRetailTradeAmount = retailTrade1Amount;
		// 零售总成本
		final double totalpurchasingPrice = 0;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个服务商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityGet.setName("迎迎" + System.currentTimeMillis() % 10000);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建零售单
		int[] retailTradeCommodityNoArr = { retailTradeCommodity1NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity1Price };
		int[] commodityCreatedArr = { commodityCreated.getID() };
		int[] barcodeCreatedArr = { barcodeCreated.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// RN
		String queryKeyword = commodityGet.getName();
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE43测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx44() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE44：根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品1
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO2) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		//
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		// 零售单总额
		final double totalRetailTradeAmount = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		// 零售总成本
		final double totalpurchasingPrice = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(4);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(5);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");

		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(4));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcode2.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(5));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(5));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO2, priceSuggestion + "," + priceSuggestion2,
				commIdString, barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2 };
		Barcodes[] barcodesArr = { barcodeCreated, barcode2 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// RN
		String queryKeyword = "多商品";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE41测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx45() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE45：(2个零售单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO2) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = 20;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = 20;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = 20;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = 20;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(4);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(5);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(4));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcode2.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(5));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(5));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO2, priceSuggestion + "," + priceSuggestion2,
				commIdString, barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2 };
		Barcodes[] barcodesArr = { barcodeCreated, barcode2 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		// 创建零售单2
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr2 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr2 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr2 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr2, retailTradeCommodityPriceArr2, retailTrade2Amount, commodityCreatedArr2, barcodeCreatedArr2, iSourceRetailTradeID);
		// 创建零售单1
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// RN
		String queryKeyword = "多商品";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE41测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确" + totalRetailTradeGross);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx46() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE46：(2个零售单,1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO2) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(4);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(5);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);
		//
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(4));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcode2.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(5));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(5));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO2, priceSuggestion + "," + priceSuggestion2,
				commIdString, barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2 };
		Barcodes[] barcodesArr = { barcodeCreated, barcode2 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		// 创建零售单2
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr2 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr2 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr2 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr2, retailTradeCommodityPriceArr2, retailTrade2Amount, commodityCreatedArr2, barcodeCreatedArr2,
				iSourceRetailTradeID);
		// 创建零售单1
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// 创建退货型零售单3
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr3 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr3 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr3 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr3, retailTradeCommodityPriceArr3, retailTrade3Amount, commodityCreatedArr3, barcodeCreatedArr3, retailTradeCreated2.getID());
		// RN
		String queryKeyword = "多商品";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE41测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确:" + totalRetailTradeGross);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx47() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE47：(2个零售单,1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利，不存在的商品关键字");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO2) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(6);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(6);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");
		//
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(6));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcode2.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(6));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(6));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO2, priceSuggestion + "," + priceSuggestion2,
				commIdString, barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2 };
		Barcodes[] barcodesArr = { barcodeCreated, barcode2 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		// 创建零售单2
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr2 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr2 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr2 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr2, retailTradeCommodityPriceArr2, retailTrade2Amount, commodityCreatedArr2, barcodeCreatedArr2,
				iSourceRetailTradeID);
		// 创建零售单1
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// 创建退货型零售单3
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr3 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr3 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr3 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr3, retailTradeCommodityPriceArr3, retailTrade3Amount, commodityCreatedArr3, barcodeCreatedArr3, retailTradeCreated2.getID());
		// RN
		String queryKeyword = "龙行龘龘";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(!bContain, "商品关键字为：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == 0, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) == 0, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) == 0, "返回的毛利不正确:");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx48() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE48：(2个零售单,1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利，不存在的staffID(操作人)");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO2) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(4);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(5);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");
		//
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(4));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcode2.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(5));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(5));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO2, priceSuggestion + "," + priceSuggestion2,
				commIdString, barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2 };
		Barcodes[] barcodesArr = { barcodeCreated, barcode2 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		// 创建零售单2
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr2 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr2 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr2 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr2, retailTradeCommodityPriceArr2, retailTrade2Amount, commodityCreatedArr2, barcodeCreatedArr2,
				iSourceRetailTradeID);
		// 创建零售单1
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// 创建退货型零售单3
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr3 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr3 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr3 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr3, retailTradeCommodityPriceArr3, retailTrade3Amount, commodityCreatedArr3, barcodeCreatedArr3, retailTradeCreated2.getID());
		// RN
		String queryKeyword = "多商品";
		String staffID = "5";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.param(RetailTrade.field.getFIELD_NAME_staffID(), staffID)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(!bContain, "商品关键字为：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == 0, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) == 0, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) == 0, "返回的毛利不正确:");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx49() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE49：(2个零售单,1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利，不存在的paymentType(付款方式)");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO2) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(6);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(6);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");
		//
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(4));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcode2.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(5));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(5));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO2, priceSuggestion + "," + priceSuggestion2,
				commIdString, barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2 };
		Barcodes[] barcodesArr = { barcodeCreated, barcode2 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		// 创建零售单2
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr2 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr2 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr2 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr2, retailTradeCommodityPriceArr2, retailTrade2Amount, commodityCreatedArr2, barcodeCreatedArr2,
				iSourceRetailTradeID);
		// 创建零售单1
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// 创建退货型零售单3
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr3 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr3 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr3 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr3, retailTradeCommodityPriceArr3, retailTrade3Amount, commodityCreatedArr3, barcodeCreatedArr3, retailTradeCreated2.getID());
		// RN
		String queryKeyword = "多商品";
		String paymentType = "4";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.param(RetailTrade.field.getFIELD_NAME_paymentType(), paymentType)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(!bContain, "商品关键字为：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == 0, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) == 0, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) == 0, "返回的毛利不正确:");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx50() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE50：(2个零售单,1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利，不存在的时间段");
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO2) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(4);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(5);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");
		//
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);
		//
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(4));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcode2.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(5));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(5));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO2, priceSuggestion + "," + priceSuggestion2,
				commIdString, barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2 };
		Barcodes[] barcodesArr = { barcodeCreated, barcode2 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		// 创建零售单2
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr2 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr2 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr2 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr2, retailTradeCommodityPriceArr2, retailTrade2Amount, commodityCreatedArr2, barcodeCreatedArr2,
				iSourceRetailTradeID);
		// 创建零售单1
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// 创建退货型零售单3
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr3 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr3 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr3 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr3, retailTradeCommodityPriceArr3, retailTrade3Amount, commodityCreatedArr3, barcodeCreatedArr3, retailTradeCreated2.getID());
		// RN
		String queryKeyword = "多商品";
		Thread.sleep(1000);
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		Date datetimeEnd = new Date();
		String datetimeEndStr = format.format(datetimeEnd);
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeEnd(), datetimeEndStr)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(!bContain, "商品关键字为：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == 0, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) == 0, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) == 0, "返回的毛利不正确:");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx51() throws Exception {
		Shared.printTestMethodStartInfo();
		/* （ps：零售单售卖商品A10件、40元，售卖商品B20件、60元，那么搜索关键字商品A，结果应该是销售数量为10、销售总额为40元） */
		Shared.caseLog("CASE51：(2个零售单,1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利, 有的零售商品符合传入的关键字，有的不符合关键字 ");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品3
		final int purchasingOrderCommNO3 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion3 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO2) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格 //xxx
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;// xcxx
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // xxx
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个不匹配关键字的商品
		Commodity commodityGet3 = BaseCommodityTest.DataInput.getCommodity();
		String c3Name = "不匹配" + Shared.generateStringByTime(4);
		commodityGet3.setName(c3Name);
		Commodity commodityCreated3 = BaseCommodityTest.createCommodityViaAction(commodityGet3, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated3 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated3, mvc, sessionBoss);
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(4);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(5);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");
		//
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);
		//
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(4));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID() + "," + commodityCreated3.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcodeCreated2.getID() + "," + barcodeCreated3.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(5));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(5));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO3 + "," + purchasingOrderCommNO2,
				priceSuggestion + "," + priceSuggestion2 + "," + priceSuggestion3, commIdString, barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2, commodityCreated3 };
		Barcodes[] barcodesArr = { barcodeCreated, barcodeCreated2, barcodeCreated3 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		// 创建零售单2
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr2 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr2 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr2 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr2, retailTradeCommodityPriceArr2, retailTrade2Amount, commodityCreatedArr2, barcodeCreatedArr2,
				iSourceRetailTradeID);
		// 创建零售单1
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// 创建退货型零售单3
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr3 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr3 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr3 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr3, retailTradeCommodityPriceArr3, retailTrade3Amount, commodityCreatedArr3, barcodeCreatedArr3, retailTradeCreated2.getID());
		// RN
		String queryKeyword = "多商品";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE41测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx52() throws Exception {
		Shared.printTestMethodStartInfo();
		/* （ps：零售单售卖商品A10件、40元，售卖商品B20件、60元，那么搜索关键字商品A，结果应该是销售数量为10、销售总额为40元） */
		Shared.caseLog("CASE52：(2个零售单,1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利, 有的零售商品符合传入的关键字，有的不符合关键字 ，零售商品价格有小数");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品3
		final int purchasingOrderCommNO3 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion3 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(100) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(100) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格 //xxx
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;// xcxx
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // xxx
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个不匹配关键字的商品
		Commodity commodityGet3 = BaseCommodityTest.DataInput.getCommodity();
		String c3Name = "不匹配" + Shared.generateStringByTime(4);
		commodityGet3.setName(c3Name);
		Commodity commodityCreated3 = BaseCommodityTest.createCommodityViaAction(commodityGet3, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated3 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated3, mvc, sessionBoss);
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(4);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(5);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");

		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(4));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID() + "," + commodityCreated3.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcodeCreated2.getID() + "," + barcodeCreated3.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());

		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(5));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(5));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO3 + "," + purchasingOrderCommNO2,
				priceSuggestion + "," + priceSuggestion2 + "," + priceSuggestion3, commIdString, barcodeIdString);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2, commodityCreated3 };
		Barcodes[] barcodesArr = { barcodeCreated, barcodeCreated2, barcodeCreated3 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		// 创建零售单2
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr2 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr2 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr2 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr2, retailTradeCommodityPriceArr2, retailTrade2Amount, commodityCreatedArr2, barcodeCreatedArr2,
				iSourceRetailTradeID);
		// 创建零售单1
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// 创建退货型零售单3
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr3 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr3 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr3 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr3, retailTradeCommodityPriceArr3, retailTrade3Amount, commodityCreatedArr3, barcodeCreatedArr3, retailTradeCreated2.getID());
		// RN
		String queryKeyword = "多商品";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE41测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx53() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE53：(2个零售单,1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利, 有的零售商品符合传入的关键字，有的不符合关键字, 跳库，创建入库单1不审核、创建入库单2并审核");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品3
		final int purchasingOrderCommNO3 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion3 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(100) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(100) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格 //xxx
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;// xcxx
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // xxx
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;

		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个不匹配关键字的商品
		Commodity commodityGet3 = BaseCommodityTest.DataInput.getCommodity();
		String c3Name = "不匹配" + Shared.generateStringByTime(4);
		commodityGet3.setName(c3Name);
		Commodity commodityCreated3 = BaseCommodityTest.createCommodityViaAction(commodityGet3, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated3 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated3, mvc, sessionBoss);
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(4);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(5);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);
		//
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(4));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID() + "," + commodityCreated3.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcodeCreated2.getID() + "," + barcodeCreated3.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(5));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(5));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO3 + "," + purchasingOrderCommNO2,
				priceSuggestion + "," + priceSuggestion2 + "," + priceSuggestion3, commIdString, barcodeIdString);
		// 创建入库单
		MvcResult result = mvc.perform( //
				post("/warehousing/retrieveNCommEx.bx") //
						.session((MockHttpSession) sessionBoss) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), purchasingOrderForApprove.getID() + "") //
		) //
				.andExpect(status().isOk()) //
				.andReturn(); //
		// 入库单2创建不审核
		Warehousing whousing2 = new Warehousing();
		whousing2.setPurchasingOrderID(purchasingOrderForApprove.getID());
		whousing2.setProviderID(1);
		whousing2.setWarehouseID(1);
		whousing2.setStaffID(EnumTypeRole.ETR_Boss.getIndex());// 15854320895手机号登录
		MvcResult mr22 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(purchasingOrderForApprove.getID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, commIdString) //
						.param(commNOs, "4,4,4") //
						.param(commPrices, "11.1,11.1,11.1") //
						.param(amounts, "44.4,44.4,44.4") //
						.param(barcodeIDs, barcodeIdString) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) result.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr22);
		//
		JSONObject o21 = JSONObject.fromObject(mr22.getResponse().getContentAsString());
		Integer wsID2 = JsonPath.read(o21, "$.object.ID");
		//
		ErrorInfo ecOut2 = new ErrorInfo();
		Warehousing wsR21 = (Warehousing) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Warehousing).read1(wsID2, 3, ecOut2, Shared.DBName_Test);
		String wsCreateDatetime2 = JsonPath.read(o21, "$.object.createDatetime");
		SimpleDateFormat sdf21 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		Date createDatetime2 = sdf21.parse(wsCreateDatetime2);
		//
		Assert.assertTrue(createDatetime2.getTime() == (wsR21.getCreateDatetime().getTime())); // ... TODO 需要增加从表验证 创建 修改 审核
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2, commodityCreated3 };
		Barcodes[] barcodesArr = { barcodeCreated, barcodeCreated2, barcodeCreated3 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		// 创建零售单2
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr2 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr2 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr2 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr2, retailTradeCommodityPriceArr2, retailTrade2Amount, commodityCreatedArr2, barcodeCreatedArr2,
				iSourceRetailTradeID);
		// 创建零售单1
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// 创建退货型零售单3
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr3 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr3 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr3 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr3, retailTradeCommodityPriceArr3, retailTrade3Amount, commodityCreatedArr3, barcodeCreatedArr3, retailTradeCreated2.getID());
		// RN
		String queryKeyword = "多商品";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE41测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx54() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE54：(2个零售单,1个退货单)根据商品关键字查询零售单，有普通、多包装、组合、服务商品的零售单时，是否返回正确的销售数量、销售总额、销售毛利, 有的零售商品符合传入的关键字，有的不符合关键字, 跨库，入库单1售卖完、继续售卖入库单2");
		// sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		final double warehousingCommPrice2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000;// 入库价2
		final int warehousingCommNO2 = 1;// 入库商品数量为1,为了跨库，不随机
		// 采购单主表1
		// 采购商品1
		final int purchasingOrderCommNO = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品2
		final int purchasingOrderCommNO2 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion2 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 采购商品3
		final int purchasingOrderCommNO3 = random.nextInt(100) + 1; // 采购数量
		final double priceSuggestion3 = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 采购价
		// 入库单主表1
		// 入库单从表1
		final int warehousingCommNO = random.nextInt(purchasingOrderCommNO - warehousingCommNO2) + 1; // 入库商品数量
		final double warehousingCommPrice = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		final int warehousingComm2NO = random.nextInt(purchasingOrderCommNO - warehousingCommNO2) + 1; // 入库商品数量
		final double warehousingComm2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000; // 入库商品价格
		// 组合商品对应的子商品数量
		final int subCommodity1NO = random.nextInt(100) + 1;
		final int subCommodity2NO = random.nextInt(100) + 1;
		final int refCommodityMultiple = random.nextInt(100) + 2; // 参照商品倍数
		// 零售单主表2
		// 零售单商品1
		// 服务
		final int retailTradeCommodity24NO = random.nextInt(100) + 1;
		final double retailTradeCommodity24Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice24 = 0;
		final double retailTrade24Amount = retailTradeCommodity24NO * retailTradeCommodity24Price;
		// 多包装，参照商品1
		final int retailTradeCommodity23NO = random.nextInt(100) + 1;
		final double retailTradeCommodity23Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice23 = retailTradeCommodity23NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade23Amount = retailTradeCommodity23NO * retailTradeCommodity23Price;
		// 单品
		final int retailTradeCommodity22NO = random.nextInt(100) + 1;
		final double retailTradeCommodity22Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice22 = retailTradeCommodity22NO * warehousingCommPrice;
		final double retailTrade22Amount = retailTradeCommodity22NO * retailTradeCommodity22Price;
		// 组合
		final int retailTradeCommodity21NO = random.nextInt(100) + 1;
		final double retailTradeCommodity21Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice21 = retailTradeCommodity21NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity21NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade21Amount = retailTradeCommodity21NO * retailTradeCommodity21Price;
		final int totalRetailTradeCommodityNO2 = retailTradeCommodity21NO + retailTradeCommodity22NO + retailTradeCommodity23NO + retailTradeCommodity24NO;
		final double totalRetailTradeAmount2 = retailTrade21Amount + retailTrade22Amount + retailTrade23Amount + retailTrade24Amount;
		final double totalpurchasingPrice2 = purchasingPrice21 + purchasingPrice22 + purchasingPrice23 + purchasingPrice24;
		// 零售单主表1
		// 零售单商品1
		// 服务
		final int retailTradeCommodity4NO = random.nextInt(100) + 1;
		final double retailTradeCommodity4Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice4 = 0;
		final double retailTrade4Amount = retailTradeCommodity4NO * retailTradeCommodity4Price;
		// 多包装，参照商品1
		final int retailTradeCommodity3NO = random.nextInt(100) + 1;
		final double retailTradeCommodity3Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice3 = retailTradeCommodity3NO * warehousingCommPrice * refCommodityMultiple;
		final double retailTrade3Amount = retailTradeCommodity3NO * retailTradeCommodity3Price;
		// 单品
		final int retailTradeCommodity2NO = random.nextInt(100) + 1;
		final double retailTradeCommodity2Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		final double purchasingPrice2 = retailTradeCommodity2NO * warehousingCommPrice;
		final double retailTrade2Amount = retailTradeCommodity2NO * retailTradeCommodity2Price;
		// 组合
		final int retailTradeCommodity1NO = random.nextInt(100) + 1;
		final double retailTradeCommodity1Price = Math.floor((random.nextDouble() + 20) * 1000) / 1000;
		//
		final double purchasingPrice1 = retailTradeCommodity1NO * warehousingCommPrice * subCommodity1NO + retailTradeCommodity1NO * warehousingComm2Price * subCommodity2NO;
		final double retailTrade1Amount = retailTradeCommodity1NO * retailTradeCommodity1Price;
		final int totalRetailTradeCommodityNO1 = retailTradeCommodity1NO + retailTradeCommodity2NO + retailTradeCommodity3NO + retailTradeCommodity4NO;
		final int totalRetailTradeCommodityNO = totalRetailTradeCommodityNO1 + totalRetailTradeCommodityNO2;
		// 零售单总额
		final double totalRetailTradeAmount1 = retailTrade1Amount + retailTrade2Amount + retailTrade3Amount + retailTrade4Amount;
		final double totalRetailTradeAmount = totalRetailTradeAmount1 + totalRetailTradeAmount2;
		// 零售总成本
		final double totalpurchasingPrice1 = purchasingPrice1 + purchasingPrice2 + purchasingPrice3 + purchasingPrice4;
		final double totalpurchasingPrice = totalpurchasingPrice1 + totalpurchasingPrice2 + warehousingCommPrice2 * warehousingCommNO2 * 2 - warehousingComm2Price - warehousingCommPrice;
		// 销售毛利 = 销售总额 - 销售成本
		final double totalRetailTradeGross = totalRetailTradeAmount - totalpurchasingPrice;
		// 创建一个不匹配关键字的商品
		Commodity commodityGet3 = BaseCommodityTest.DataInput.getCommodity();
		String c3Name = "不匹配" + Shared.generateStringByTime(4);
		commodityGet3.setName(c3Name);
		Commodity commodityCreated3 = BaseCommodityTest.createCommodityViaAction(commodityGet3, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated3 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated3, mvc, sessionBoss);
		// 创建一个普通商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		String c1Name = "多商品单1" + Shared.generateStringByTime(4);
		commodityGet.setName(c1Name);
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated, mvc, sessionBoss);
		// 创建单品的多包装商品
		MvcResult req = BaseCommodityTest.uploadPicture(CommoditySyncActionTest.PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName(c1Name);
		commUpdate.setID(commodityCreated.getID());
		commUpdate.setProviderIDs("1");
		String mutiCommName = "多商品多" + Shared.generateStringByTime(5);
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ";1,2;1," + refCommodityMultiple + ";1,5;0.8,4;2,3;" + c1Name + "," + mutiCommName + ";");
		//
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodityCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> mutipleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCreated, mapBO);
		//
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, mutipleCommList, Shared.DBName_Test, null);
		//
		Commodity commodityGet2 = BaseCommodityTest.DataInput.getCommodity();
		commodityGet2.setName("多商品单2" + Shared.generateStringByTime(4));
		Commodity commodityCreated2 = BaseCommodityTest.createCommodityViaAction(commodityGet2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCreated2 = BaseCommodityTest.retrieveNBarcodesViaAction(commodityCreated2, mvc, sessionBoss);
		String commIdString = commodityCreated.getID() + "," + commodityCreated2.getID() + "," + commodityCreated3.getID();
		String barcodeIdString = barcodeCreated.getID() + "," + barcodeCreated2.getID() + "," + barcodeCreated3.getID();
		// 组合商品
		Commodity combinationCommodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodityList = new ArrayList<SubCommodity>();
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommodity1NO);
		subCommodity1.setSubCommodityID(commodityCreated.getID());
		subCommodity1.setPrice(3);
		subCommodityList.add(subCommodity1);
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommodity2NO);
		subCommodity2.setSubCommodityID(commodityCreated2.getID());
		subCommodity2.setPrice(3);
		subCommodityList.add(subCommodity2);
		combinationCommodityGet.setListSlave1(subCommodityList);
		//
		combinationCommodityGet.setName("多商品组" + Shared.generateStringByTime(5));
		combinationCommodityGet.setSubCommodityInfo(JSONObject.fromObject(combinationCommodityGet).toString());
		Commodity commodity1Combination = BaseCommodityTest.createCommodityViaAction(combinationCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcodeCombination = BaseCommodityTest.retrieveNBarcodesViaAction(commodity1Combination, mvc, sessionBoss);
		// 服务商品
		// 创建一个服务商品
		Commodity serviceCommGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		serviceCommGet.setName("多商品服" + Shared.generateStringByTime(5));
		Commodity serviceCommCreated = BaseCommodityTest.createCommodityViaAction(serviceCommGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes serviceBarcodeCreated = BaseCommodityTest.retrieveNBarcodesViaAction(serviceCommCreated, mvc, sessionBoss);
		// 创建并审核采购订单
		PurchasingOrder purchasingOrderForApprove = BaseRetailTradeTest.createAndApprovePurchasingOrderViaAction(mvc, sessionBoss, mapBO, purchasingOrderCommNO + "," + purchasingOrderCommNO3 + "," + purchasingOrderCommNO2,
				priceSuggestion + "," + priceSuggestion2 + "," + priceSuggestion3, commIdString, barcodeIdString);
		// 创建并审核入库单2
		int[] warehousingCommNOArr2 = { warehousingCommNO2, warehousingCommNO2, warehousingCommNO2 };
		double[] warehousingCommPriceArr2 = { warehousingCommPrice2, warehousingCommPrice2, warehousingCommPrice2 };
		Commodity[] commodityArr2 = { commodityCreated, commodityCreated2, commodityCreated3 };
		Barcodes[] barcodesArr2 = { barcodeCreated, barcodeCreated2, barcodeCreated3 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr2, warehousingCommPriceArr2, commodityArr2, barcodesArr2, purchasingOrderForApprove);
		// 让检查点通过
		commodityCreated.setNO(warehousingCommNO2);
		commodityCreated2.setNO(warehousingCommNO2);
		commodityCreated3.setNO(warehousingCommNO2);
		// 创建并审核入库单
		int[] warehousingCommNOArr = { warehousingCommNO, warehousingComm2NO, warehousingComm2NO };
		double[] warehousingCommPriceArr = { warehousingCommPrice, warehousingComm2Price, warehousingComm2Price };
		Commodity[] commodityArr = { commodityCreated, commodityCreated2, commodityCreated3 };
		Barcodes[] barcodesArr = { barcodeCreated, barcodeCreated2, barcodeCreated3 };
		BaseRetailTradeTest.createAndApproveWarehousingViaAction(mvc, sessionBoss, mapBO, warehousingCommNOArr, warehousingCommPriceArr, commodityArr, barcodesArr, purchasingOrderForApprove);
		// 检查多包装商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodityCreated);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		Assert.assertTrue(listMultiPackageCommodity.size() > 0, "多包装商品没有正确删除");
		Commodity multiPackageCommodity = listMultiPackageCommodity.get(0);
		// 为商品添加条形码
		Barcodes barcodeMultiPackage = BaseCommodityTest.retrieveNBarcodesViaAction(multiPackageCommodity, mvc, sessionBoss);
		// 创建零售单
		Date datetimeStart = new Date();
		DateFormat format = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);// 日期格式
		String datetimeStartStr = format.format(datetimeStart);
		// 创建零售单2
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr2 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr2 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr2 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr2 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		RetailTrade retailTradeCreated2 = BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr2, retailTradeCommodityPriceArr2, retailTrade2Amount, commodityCreatedArr2, barcodeCreatedArr2,
				iSourceRetailTradeID);
		// 创建零售单1
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr = { retailTradeCommodity4NO, retailTradeCommodity1NO, retailTradeCommodity2NO, retailTradeCommodity3NO };
		double[] retailTradeCommodityPriceArr = { retailTradeCommodity4Price, retailTradeCommodity1Price, retailTradeCommodity2Price, retailTradeCommodity3Price };
		int[] commodityCreatedArr = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr, retailTradeCommodityPriceArr, retailTrade1Amount, commodityCreatedArr, barcodeCreatedArr, iSourceRetailTradeID);
		// 创建退货型零售单3
		// 服务商品、组合商品、单品、多包装
		int[] retailTradeCommodityNoArr3 = { retailTradeCommodity24NO, retailTradeCommodity21NO, retailTradeCommodity22NO, retailTradeCommodity23NO };
		double[] retailTradeCommodityPriceArr3 = { retailTradeCommodity24Price, retailTradeCommodity21Price, retailTradeCommodity22Price, retailTradeCommodity23Price };
		int[] commodityCreatedArr3 = { serviceCommCreated.getID(), commodity1Combination.getID(), commodityCreated.getID(), multiPackageCommodity.getID() };
		int[] barcodeCreatedArr3 = { serviceBarcodeCreated.getID(), barcodeCombination.getID(), barcodeCreated.getID(), barcodeMultiPackage.getID() };
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, retailTradeCommodityNoArr3, retailTradeCommodityPriceArr3, retailTrade3Amount, commodityCreatedArr3, barcodeCreatedArr3, retailTradeCreated2.getID());
		// RN
		String queryKeyword = "多商品";
		MvcResult mr13 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStartStr)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr13);
		Shared.checkJSONErrorCode(mr13);
		//
		String json13 = mr13.getResponse().getContentAsString();
		JSONObject o13 = JSONObject.fromObject(json13);
		List<String> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList + "[*].listSlave1[*].commodityName");
		boolean bContain = false;
		for (String name : list13) {
			if (name.contains(queryKeyword)) {
				bContain = true;
				break;
			}
		}
		Assert.assertTrue(bContain, "CASE41测试失败!返回的商品名称没有包含：" + queryKeyword);
		// 验证销售数量、销售总额、毛利
		String sumRetailTradeAmount = o13.getString("retailAmount");
		String sumCommNO = o13.getString("totalCommNO");
		String totalGross = o13.getString("totalGross");
		System.out.println("sumRetailTradeAmount:" + sumRetailTradeAmount);
		System.out.println("sumCommNO:" + sumCommNO);
		System.out.println("totalGross:" + totalGross);
		com.alibaba.fastjson.JSONObject totalGrossObject = JSON.parseObject(json13);
		totalGross = totalGrossObject.getString("totalGross");
		Assert.assertTrue(Integer.valueOf(sumCommNO) == totalRetailTradeCommodityNO, "返回的销售数量不正确");
		Assert.assertTrue(Double.valueOf(sumRetailTradeAmount) - totalRetailTradeAmount < BaseModel.TOLERANCE, "返回的销售总额不正确");
		Assert.assertTrue(Double.valueOf(totalGross) - totalRetailTradeGross < BaseModel.TOLERANCE, "返回的毛利不正确");
	}

	@Test
	public void testRetrieveNEx55() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE55: 小程序中根据VipID进行查询");
		MvcResult mr7 = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) sessionVip)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_vipID(), "1")//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")// 防止null不通过
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr7);

		JSONObject o7 = JSONObject.fromObject(mr7.getResponse().getContentAsString());
		List<Integer> i7 = JsonPath.read(o7, "$.objectList[*].vipID");
		for (int i = 0; i < i7.size(); i++) {
			assertTrue(i7.get(i) == 1);
		}
	}
}
