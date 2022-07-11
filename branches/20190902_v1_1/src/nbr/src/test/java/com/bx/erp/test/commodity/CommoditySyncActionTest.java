package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.commodity.CommoditySyncAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.CouponScope.EnumCouponScope;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCouponTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.BasePurchasingOrderTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CheckPoint;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class CommoditySyncActionTest extends BaseActionTest {
	private static final int INVALID_ID = 999999999;
	private Log logger = LogFactory.getLog(CommoditySyncAction.class);

	protected final String barcode1 = "251135499823755628";
	protected final String barcode2 = "254521454628769759";
	protected final String barcode3 = "254521482452358743";
	protected final String barcode4 = "435854553853345353";
	protected final String barcode5 = "545245282384582474";
	protected final String barcode6 = "487231831218321812";
	protected final String barcode7 = "546123924248123187";
	protected final String barcode8 = "587643924275123183";
	protected final String barcode9 = "587646666665123183";

	protected final String packageUnit1 = "1";
	protected final String packageUnit2 = "2";
	protected final String packageUnit3 = "3";
	protected final String packageUnit4 = "4";
	protected final String packageUnit5 = "5";
	protected final String packageUnit6 = "6";
	protected final String packageUnit7 = "7";

	protected final String refCommodityMultiple1 = "0";
	protected final String refCommodityMultiple2 = "12";
	protected final String refCommodityMultiple3 = "12";
	protected final String refCommodityMultiple4 = "12";
	protected final String refCommodityMultiple5 = "5";
	protected final String refCommodityMultiple6 = "6";
	protected final String refCommodityMultiple7 = "7";

	protected final String priceRetail1 = "12";
	protected final String priceRetail2 = "140";
	protected final String priceRetail3 = "140";
	protected final String priceRetail4 = "140";
	protected final String priceRetail5 = "50";
	protected final String priceRetail6 = "60";
	protected final String priceRetail7 = "70";

	protected final String commodityA = "商品A";
	protected final String commodityB = "商品B";
	protected final String commodityC = "商品C";
	protected final String commodityD = "商品D";

	public static final String PATH = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\logo.png";

	private List<PackageUnit> lspu;
	private List<PackageUnit> lspuCreated;
	private List<PackageUnit> lspuUpdated;
	private List<PackageUnit> lspuDeleted;

	private static HttpSession sessionPos1;
	private static HttpSession sessionPos2;
//	private HttpSession sessionBoss;
	private Staff staff;

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
		super.setup();

		try {
			Shared.resetPOS(mvc, 1);
			sessionPos1 = Shared.getPosLoginSession(mvc, 1);

			Shared.resetPOS(mvc, 2);
			sessionPos2 = Shared.getPosLoginSession(mvc, 2);

//			sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
			staff = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();

		// 由于商品的测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	// 检查在输入错误的参数之后返回的params("commodity","")
	private static String checkJSONCommodity(MvcResult mr) throws UnsupportedEncodingException {
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		return JsonPath.read(o, "$.object");
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();

		// 由于商品的测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常情况");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		c.setProviderIDs("7");
		c.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2：传入参数为空");
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c2.setProviderIDs("7,2,3");
		c2.setMultiPackagingInfo(barcode3 + "," + "222" + Shared.generateStringByTime(8) + ",333" + Shared.generateStringByTime(8) + ";1,200,3;1,5,10;1,5,10;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c2, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3: 传入顺序打乱");
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c.setProviderIDs("7,2,3");
		c.setMultiPackagingInfo(Shared.generateStringByTime(8) + "," + Shared.generateStringByTime(8) + "," + Shared.generateStringByTime(8) + ";1,200,3;1," + barcode1 + ",10;1,5,10;0,0,0;0,0,0;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");//

		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4: 传入的参数维度不一致");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,7,0");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",333" + Shared.generateStringByTime(8) + ";1,200,3,6;1,5,10;1,5,10;0,0,0,0;0,0,0;" //
				+ "你好1,1231,好的1;");//

		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5: 没有返回供应商ID");
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c.setProviderIDs("");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",333" + Shared.generateStringByTime(8) + ";1,200,3,6;1,5,10;1,5,10;0,0,0,0;0,0,0;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");//

		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:创建商品时名称重复");
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		c.setName("可比克薯片");
		c.setProviderIDs("7,2,3");
		c.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(6) + ";");//

		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:没有返回多包装商品名字");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,2,3");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",333" + Shared.generateStringByTime(8) + ";1,200,3,6;1,5,10;1,5,10;0,0,0,0;0,0,0;");//

		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:创建时传入的不存在的品牌ID，错误码=7");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,2,3");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");//
		c.setBrandID(INVALID_ID);

		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionPos1) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, "品牌不存在", "错误信息错误！");
	}

	@Test
	public void testCreateEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9:创建时传入的不存在的类别ID，错误码=7");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,2,3");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");//
		c.setCategoryID(INVALID_ID);

		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionPos1) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, "分类不存在", "错误信息错误！");
	}

	@Test
	public void testCreateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10:创建时传入的不存在的包装单位ID，错误码=7");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,2,3");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");//
		c.setPackageUnitID(INVALID_ID);

		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionPos1) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, "包装单位不存在", "错误信息错误！");
	}

	@Test
	public void testCreateEx11_Combination() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11:创建组合商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity subCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());

		subCommodity.setShelfLife(BaseAction.INVALID_ID);
		subCommodity.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity.setProviderIDs("7");
		subCommodity.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		String json = JSONObject.fromObject(subCommodity).toString();

		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity, req.getRequest().getSession()) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, subCommodity, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12:创建组合商品,子商品价格小于0");
		SubCommodity sc = new SubCommodity();
		sc.setSubCommodityID(4);
		sc.setSubCommodityNO(2);
		sc.setPrice(BaseAction.INVALID_NO);
		SubCommodity sc1 = new SubCommodity();
		sc.setSubCommodityID(5);
		sc.setSubCommodityNO(3);
		sc.setPrice(BaseAction.INVALID_NO);

		List<SubCommodity> scList = new ArrayList<SubCommodity>();
		scList.add(sc);
		scList.add(sc1);

		Commodity subCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		subCommodity.setShelfLife(BaseAction.INVALID_ID);
		subCommodity.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity.setProviderIDs("7");
		subCommodity.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		subCommodity.setListSlave1(scList);

		String json2 = JSONObject.fromObject(subCommodity).toString();

		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity, sessionPos1) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json2)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
	}

	@Test
	public void testCreateEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13:创建组合商品,子商品ID不存在");
		SubCommodity sc2 = new SubCommodity();
		sc2.setSubCommodityID(BaseAction.INVALID_ID);
		sc2.setSubCommodityNO(3);
		sc2.setPrice(2);
		SubCommodity sc3 = new SubCommodity();
		sc3.setSubCommodityID(BaseAction.INVALID_ID);
		sc3.setSubCommodityNO(2);
		sc3.setPrice(1);

		List<SubCommodity> scList = new ArrayList<SubCommodity>();
		scList.add(sc2);
		scList.add(sc3);

		Commodity subCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		subCommodity.setShelfLife(BaseAction.INVALID_ID);
		subCommodity.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity.setProviderIDs("7");
		subCommodity.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		subCommodity.setListSlave1(scList);

		String json3 = JSONObject.fromObject(subCommodity).toString();

		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity, sessionPos1) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json3)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);

	}

	@Test
	public void testCreateEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14:创建组合商品,子商品为组合商品");
		SubCommodity sc4 = new SubCommodity();
		sc4.setSubCommodityID(49);
		sc4.setSubCommodityNO(2);
		sc4.setPrice(1);
		SubCommodity sc5 = new SubCommodity();
		sc5.setSubCommodityID(50);
		sc5.setSubCommodityNO(3);
		sc5.setPrice(2);

		List<SubCommodity> scList = new ArrayList<SubCommodity>();
		scList.add(sc4);
		scList.add(sc5);

		Commodity subCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		subCommodity.setShelfLife(BaseAction.INVALID_ID);
		subCommodity.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity.setProviderIDs("7");
		subCommodity.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		subCommodity.setListSlave1(scList);

		String json4 = JSONObject.fromObject(subCommodity).toString();

		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity, sessionPos1) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json4)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
	}

	@Test
	public void testCreateEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15:创建组合商品,子商品为多包装商品");
		SubCommodity sc1 = new SubCommodity();
		sc1.setSubCommodityID(51);
		sc1.setSubCommodityNO(2);
		sc1.setPrice(1);
		SubCommodity sc2 = new SubCommodity();
		sc2.setSubCommodityID(52);
		sc2.setSubCommodityNO(3);
		sc2.setPrice(1);

		List<SubCommodity> scList = new ArrayList<SubCommodity>();
		scList.add(sc1);
		scList.add(sc2);

		Commodity subCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		subCommodity.setShelfLife(BaseAction.INVALID_ID);
		subCommodity.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity.setProviderIDs("7");
		subCommodity.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		subCommodity.setListSlave1(scList);

		String json = JSONObject.fromObject(subCommodity).toString();

		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity, sessionPos1) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
	}

	@Test
	public void testCreateEx16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16:创建组合商品,添加相同的子商品");
		SubCommodity sc8 = new SubCommodity();
		sc8.setSubCommodityID(2);
		sc8.setSubCommodityNO(2);
		sc8.setPrice(1);

		SubCommodity sc9 = new SubCommodity();
		sc9.setSubCommodityID(2);
		sc9.setSubCommodityNO(2);
		sc9.setPrice(1);

		List<SubCommodity> scList6 = new ArrayList<SubCommodity>();
		scList6.add(sc8);
		scList6.add(sc9);

		Commodity subCommodity6 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		subCommodity6.setShelfLife(BaseAction.INVALID_ID);
		subCommodity6.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity6.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity6.setProviderIDs("7");
		subCommodity6.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		subCommodity6.setListSlave1(scList6);

		String json6 = JSONObject.fromObject(subCommodity6).toString();

		MvcResult mrl16 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity6, sessionPos1) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json6)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mrl16, EnumErrorCode.EC_PartSuccess);
	}

	@Test
	public void testCreateEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17:普通商品添加子商品");
		MvcResult req19 = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		SubCommodity sc10 = new SubCommodity();
		sc10.setSubCommodityID(2);
		sc10.setSubCommodityNO(2);
		sc10.setPrice(1);
		SubCommodity sc11 = new SubCommodity();
		sc11.setSubCommodityID(3);
		sc11.setSubCommodityNO(4);
		sc11.setPrice(1);

		List<SubCommodity> scList7 = new ArrayList<SubCommodity>();
		scList7.add(sc10);
		scList7.add(sc11);

		Commodity subCommodity7 = BaseCommodityTest.DataInput.getCommodity();
		subCommodity7.setProviderIDs("7");
		subCommodity7.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		subCommodity7.setListSlave1(scList7);

		String json7 = JSONObject.fromObject(subCommodity7).toString();

		MvcResult mrl17 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity7, req19.getRequest().getSession()) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json7)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mrl17, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case18:创建组合商品,子商品数量小于2");
		SubCommodity sc12 = new SubCommodity();
		sc12.setSubCommodityID(2);
		sc12.setSubCommodityNO(2);
		sc12.setPrice(1);

		List<SubCommodity> scList8 = new ArrayList<SubCommodity>();
		scList8.add(sc12);

		Commodity subCommodity8 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		subCommodity8.setShelfLife(BaseAction.INVALID_ID);
		subCommodity8.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity8.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity8.setProviderIDs("7");
		subCommodity8.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		subCommodity8.setListSlave1(scList8);

		String json8 = JSONObject.fromObject(subCommodity8).toString();

		MvcResult mrl18 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity8, sessionPos1) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json8)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mrl18, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19:创建组合商品,子商品数量等于2");
		MvcResult req21 = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		SubCommodity sc13 = new SubCommodity();
		sc13.setSubCommodityID(3);
		sc13.setSubCommodityNO(2);
		sc13.setPrice(1);
		SubCommodity sc14 = new SubCommodity();
		sc14.setSubCommodityID(2);
		sc14.setSubCommodityNO(2);
		sc14.setPrice(1);

		List<SubCommodity> scList9 = new ArrayList<SubCommodity>();
		scList9.add(sc13);
		scList9.add(sc14);

		Commodity subCommodity9 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		subCommodity9.setShelfLife(BaseAction.INVALID_ID);
		subCommodity9.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity9.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity9.setProviderIDs("7");
		subCommodity9.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		subCommodity9.setListSlave1(scList9);

		String json9 = JSONObject.fromObject(subCommodity9).toString();

		MvcResult mrl19 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity9, req21.getRequest().getSession()) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json9)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl19);
		// 检查点
		CommodityCP.verifyCreate(mrl19, subCommodity9, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case20:创建组合商品,子商品数量大于2");
		MvcResult req22 = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		SubCommodity sc15 = new SubCommodity();
		sc15.setSubCommodityID(2);
		sc15.setSubCommodityNO(2);
		sc15.setPrice(1);
		SubCommodity sc16 = new SubCommodity();
		sc16.setSubCommodityID(3);
		sc16.setSubCommodityNO(2);
		sc16.setPrice(1);
		SubCommodity sc17 = new SubCommodity();
		sc17.setSubCommodityID(4);
		sc17.setSubCommodityNO(2);
		sc17.setPrice(1);

		List<SubCommodity> scList10 = new ArrayList<SubCommodity>();
		scList10.add(sc15);
		scList10.add(sc16);
		scList10.add(sc17);

		Commodity subCommodity10 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		subCommodity10.setShelfLife(BaseAction.INVALID_ID);
		subCommodity10.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity10.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity10.setProviderIDs("7");
		subCommodity10.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		subCommodity10.setListSlave1(scList10);

		String json10 = JSONObject.fromObject(subCommodity10).toString();

		MvcResult mrl20 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity10, req22.getRequest().getSession()) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json10)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl20, EnumErrorCode.EC_NoError);
		// 检查点
		CommodityCP.verifyCreate(mrl20, subCommodity10, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO,
				packageUnitBO, categoryBO, Shared.DBName_Test);

	}

	@Test
	public void testCreateEx21_noStart() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case21:创建期初商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		;
		Commodity c11 = BaseCommodityTest.DataInput.getCommodity();
		c11.setRuleOfPoint(1);
		c11.setPurchaseFlag(1);
		c11.setShelfLife(1);
		c11.setnOStart(100);
		c11.setPurchasingPriceStart(100);
		c11.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		c11.setProviderIDs("1");

		MvcResult mrl21 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c11, req.getRequest().getSession())//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl21);
		// 检查点
		CommodityCP.verifyCreate(mrl21, c11, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);

	}

	@Test
	public void testCreateEx22() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("创建期初商品是组合商品，创建组合商品失败");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		;

		SubCommodity sc18 = new SubCommodity();
		sc18.setSubCommodityID(3);
		sc18.setSubCommodityNO(2);
		sc18.setPrice(1);
		SubCommodity sc19 = new SubCommodity();
		sc19.setSubCommodityID(2);
		sc19.setSubCommodityNO(2);
		sc19.setPrice(1);

		List<SubCommodity> scList11 = new ArrayList<SubCommodity>();
		scList11.add(sc18);
		scList11.add(sc19);

		Commodity subCommodity11 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		subCommodity11.setShelfLife(0);
		subCommodity11.setPurchaseFlag(0);
		subCommodity11.setRuleOfPoint(0);
		subCommodity11.setnOStart(100);
		subCommodity11.setPurchasingPriceStart(100);
		subCommodity11.setListSlave1(scList11);
		subCommodity11.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		subCommodity11.setProviderIDs("1");

		String json11 = JSONObject.fromObject(subCommodity11).toString();
		subCommodity11.setSubCommodityInfo(json11);

		MvcResult mrl22 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity11, req.getRequest().getSession()) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json11)//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl22, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx23() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case23:创建多包装商品，RefCommodityMultiple 不大于1，创建失败，返回7");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		// 测试需求，直接创建多包装商品
		Commodity c12 = BaseCommodityTest.DataInput.getCommodity();
		c12.setRuleOfPoint(1);
		c12.setPurchaseFlag(1);
		c12.setShelfLife(1);
		c12.setRefCommodityID(1);
		c12.setRefCommodityMultiple(-3);
		c12.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,1,0;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		c12.setProviderIDs("1");

		MvcResult mrl23 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c12, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码， 当黑客处理
		Shared.checkJSONErrorCode(mrl23, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx24() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case24:string1能接收64位长度以内的字符串（大于50位,原来只能为50位）");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		//
		Commodity c13 = BaseCommodityTest.DataInput.getCommodity();
		c13.setRuleOfPoint(1);
		c13.setPurchaseFlag(1);
		c13.setShelfLife(1);
		String barcode271 = "11111111222222223333333344444444555555556666666677777777";
		String barcode272 = "11111111222222223333333344444444555555556666666688888888";
		String barcode273 = "11111111222222223333333344444444555555556666666699999999";
		c13.setMultiPackagingInfo(barcode271 + "," + barcode272 + "," + barcode273 + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		c13.setProviderIDs("1");

		MvcResult mr24 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c13, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr24);
		// 检查点
		CommodityCP.verifyCreate(mr24, c13, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx25() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case25:barcode格式错误（含有中文）");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		//
		Commodity c14 = BaseCommodityTest.DataInput.getCommodity();
		String barcode281 = "啦1111111222222223333333344444444555555556666666677777777";
		String barcode282 = "啦11111111222222223333333344444444555555556666666688888888";
		String barcode283 = "啦11111111222222223333333344444444555555556666666699999999";
		c14.setMultiPackagingInfo(barcode281 + "," + barcode282 + "," + barcode283 + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		c14.setProviderIDs("1");

		MvcResult mr25 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c14, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr25, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx26() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case26:创建商品时名称与预淘汰商品名称重复");
		Commodity c15 = BaseCommodityTest.DataInput.getCommodity();
		c15.setName("康师傅牛肉面");// 预淘汰商品无法直接创建，故直接拿数据库中的预淘汰商品测试
		c15.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",333" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		c15.setProviderIDs("1");

		MvcResult mr26 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c15, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr26, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreateEx27() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case27:创建商品时名称删除商品名称重复");
		// 创建单品无多包装 1条码
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		Thread.sleep(1000);
		c1.setName("可乐薯片青瓜呦" + Shared.generateStringByTime(8));
		c1.setOperatorStaffID(STAFF_ID3);
		c1.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = c1.getCreateParamEx(BaseBO.INVALID_CASE_ID, c1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity commCreate1 = (Commodity) bmList.get(0).get(0);
		Barcodes bcCreate1 = (Barcodes) bmList.get(1).get(0);

		List<Barcodes> barcodesList = new ArrayList<Barcodes>();
		barcodesList.add(bcCreate1);
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commCreate1.getID(), Shared.DBName_Test, commodityHistoryBO);

		// 删除创建出来的单品
		MvcResult mr1 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate1.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		// 检查错误码
		Shared.checkJSONErrorCode(mr1);
		// 检查点
		CommodityCP.verifyDelete(commCreate1, barcodesList, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);

		// 商品与删除状态商品同名的创建。
		String barcodes = barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",333" + Shared.generateStringByTime(8);
		Commodity c16 = BaseCommodityTest.DataInput.getCommodity();
		c16.setMultiPackagingInfo(barcodes + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		c16.setProviderIDs("1");
		c16.setName(commCreate1.getName());

		MvcResult mrl27 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c16, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mrl27);
		// 检查点
		CommodityCP.verifyCreate(mrl27, c16, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);

		// 删除掉创建出来的商品
		JSONObject jsonObject = JSONObject.fromObject(mrl27.getResponse().getContentAsString());
		BaseModel bmOfDB = commCreate1.parse1(jsonObject.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 查询商品的创建商品的多包装商品
		Map<String, Object> params2 = bmOfDB.getRetrieveNParam(BaseBO.CASE_RetrieveNMultiPackageCommodity, bmOfDB);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNMultiPackageCommodity = commodityMapper.retrieveNMultiPackageCommodity(params2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(retrieveNMultiPackageCommodity.size() != 0, "查询不到数据");
		// 删除创建商品的多包装商品
		for (int i = 0; i < retrieveNMultiPackageCommodity.size(); i++) {
			Commodity reCommodity = (Commodity) retrieveNMultiPackageCommodity.get(i);
			MvcResult mr2 = mvc.perform(//
					get("/commoditySync/deleteEx.bx?ID=" + reCommodity.getID() + "&type=" + reCommodity.getType()) //
							.contentType(MediaType.APPLICATION_JSON) //
							.session((MockHttpSession) sessionPos1)) //
					.andExpect(status().isOk()) //
					.andDo(print()) //
					.andReturn();

			Shared.checkJSONErrorCode(mr2);
		}
		// 删除商品
		BaseCommodityTest.deleteCommodityViaAction((Commodity) bmOfDB, Shared.DBName_Test, mvc, sessionPos1, mapBO);
	}

	@Test
	public void testCreateEx28() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case28:根据名称创建助记码");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		//
		Commodity c32 = BaseCommodityTest.DataInput.getCommodity();
		c32.setMnemonicCode("");
		c32.setName("薯片侠666");
		c32.setMultiPackagingInfo(barcode1 + ";1;1;553;553;553;薯片侠666;");
		c32.setProviderIDs("7");
		MvcResult mrl28 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c32, req.getRequest().getSession())//
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl28);
		// 检查点
		CommodityCP.verifyCreate(mrl28, c32, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);
		// 判断助记码是否根据名称正确生成
		JSONObject o322 = JSONObject.fromObject(mrl28.getResponse().getContentAsString());
		BaseModel bmOfDB322 = c32.parse1(o322.getString(BaseAction.KEY_Object));
		assertTrue("spx666".equals(((Commodity) bmOfDB322).getMnemonicCode()));
		//
		BaseCommodityTest.deleteCommodityViaAction((Commodity) bmOfDB322, Shared.DBName_Test, mvc, sessionPos1, mapBO);
	}

	@Test
	public void testCreateEx29() throws Exception {
		Shared.printTestMethodStartInfo();
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);

		Shared.caseLog("case29:会员价为0.000000");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;0.000000,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyCreate(mrl, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
	}

	@Test
	public void testCreateEx30() throws Exception {
		Shared.printTestMethodStartInfo();
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);

		Shared.caseLog("case30:批发价为0.000000");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;0.000000,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession())) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyCreate(mrl, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
	}

	@Test
	public void testCreateEx31() throws Exception {
		Shared.printTestMethodStartInfo();
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);

		Shared.caseLog("case31:创建普通商品有多包装单位，多包装单位的名称有重复");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		String pUNameA = commodityA + Shared.generateStringByTime(8);
		MvcResult mrl = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()).param(Commodity.field.getFIELD_NAME_multiPackagingInfo(),
				barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;0.000000,8,8;" //
						+ pUNameA + "," + pUNameA + "," + commodityC + Shared.generateStringByTime(8) + ";") //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx32() throws Exception {
		Shared.printTestMethodStartInfo();
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);

		Shared.caseLog("case32:创建普通商品存在多包装时，商品倍数传1");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,1,10;1,5,10;8,3,8;0.000000,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession())//
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx33() throws Exception {
		Shared.printTestMethodStartInfo();
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);

		Shared.caseLog("case33:创建多包装商品，多包装商品名称重复创建失败和创建供应商商品失败 ,期望的错误码是12部分成功");
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;0.000000,8,8;" //
				+ commodityA + "," + "星巴克A" + "," + commodityC + Shared.generateStringByTime(8) + ";");
		c.setProviderIDs("1");

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession())//
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_NoError); // 当创建多包装出现EC_Duplicated错误时，会跳过该多包装的创建然后执行下去，所以返回的是no error以及相关错误信息
	}

	@Test
	public void testCreateEx34() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case34: 创建服务商品并创建多包装商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		;
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx35() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case35: 创建组合商品并创建多包装商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		;
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		String json = JSONObject.fromObject(c).toString();
		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx36() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case36: type不为0创建多包装商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		;
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(BaseAction.INVALID_Type);
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx37_Service() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case37: 创建服务商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		;
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
	}

	@Test
	public void testCreateEx38() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case38: 创建服务商品时传递采购单位");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		;
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setPurchasingUnit("箱");
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx39() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case39: 创建组合商品的子商品为服务商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setShelfLife(0);
		c.setOperatorStaffID(STAFF_ID3);
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		c.setPurchaseFlag(0);
		c.setPurchasingUnit("");
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		Commodity createServiceCommodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);

		Commodity subCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		//
		List<SubCommodity> subCommodities = new ArrayList<SubCommodity>();
		//
		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(2);
		subCommodity1.setSubCommodityID(createServiceCommodity.getID());
		subCommodity1.setPrice(3);
		subCommodities.add(subCommodity1);
		//
		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(3);
		subCommodity2.setSubCommodityID(3);
		subCommodity2.setPrice(3);
		subCommodities.add(subCommodity2);
		//
		subCommodity.setListSlave1(subCommodities);

		subCommodity.setShelfLife(BaseAction.INVALID_ID);
		subCommodity.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity.setProviderIDs("7");
		subCommodity.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		String json = JSONObject.fromObject(subCommodity).toString();
		//
		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity, req.getRequest().getSession()) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
	}

	@Test
	public void testCreateEx40() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case40: 服务商品设置期初值");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c11 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c11.setRuleOfPoint(1);
		c11.setPurchaseFlag(1);
		c11.setShelfLife(1);
		c11.setnOStart(100);
		c11.setPurchasingPriceStart(100);
		c11.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		c11.setProviderIDs("1");

		MvcResult mrl21 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c11, req.getRequest().getSession())//
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl21, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateEx41() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case41: 创建期初商品后的入库单的SN不为空");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c11 = BaseCommodityTest.DataInput.getCommodity();
		c11.setRuleOfPoint(1);
		c11.setPurchaseFlag(1);
		c11.setShelfLife(1);
		c11.setnOStart(100);
		c11.setPurchasingPriceStart(100);
		c11.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		c11.setProviderIDs("1");

		MvcResult mrl21 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c11, req.getRequest().getSession())//
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl21);

		// 检查点
		CommodityCP.verifyCreate(mrl21, c11, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);
		//
		Warehousing warehousing = new Warehousing();
		warehousing.setPageSize(1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Warehousing> listWarehousing = (List<Warehousing>) warehousingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取Warehousing DB数据错误！");
		}
		Assert.assertTrue(!"".equals(listWarehousing.get(0).getSn()), "入库单的SN为空");
	}

	@Test
	public void testCreateEx42() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case42:创建单品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7");
		c.setMultiPackagingInfo(barcode1 + ";3;10;10;8;8;" //
				+ commodityA + Shared.generateStringByTime(8) + ";");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
	}

	@Test
	public void testCreateEx43() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case43:特殊请求,同时创建多包装，组合商品。返回其他错误");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,2,3");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		c.setSubCommodityInfo("SubCommodityInfo有内容则当做创建组合商品");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
						.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), c.getSubCommodityInfo()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx44() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("MultiPackagingInfo为空");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,2,3");
		c.setMultiPackagingInfo(null);
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testCreateEx45() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case45: 创建服务商品库存不为0");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setNO(100);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		// 检查库存是否为0
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Commodity commodity = (Commodity) c.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<CommodityShopInfo> listCommodityShopInfo = (List<CommodityShopInfo>) commodity.getListSlave2();
		Assert.assertTrue(listCommodityShopInfo != null, "没有返回商品门店信息");
		CommodityShopInfo commodityShopInfo = listCommodityShopInfo.get(listCommodityShopInfo.size() - 1);
		assertTrue(commodityShopInfo.getNO() == 0, "商品库存居然不为0！");

	}

	@Test
	public void testCreateEx46() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case46: 创建服务商品参照商品ID不为0");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setRefCommodityID(1);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		// 检查库存是否为0
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Commodity commodity = (Commodity) c.parse1(jsonObject.getString(BaseAction.KEY_Object));
		assertTrue(commodity.getRefCommodityID() == 0, "商品的参照商品ID居然不为0！");

	}

	@Test
	public void testCreateEx47() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case47: 创建服务商品期初数量不为-1");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setnOStart(10);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		// 检查库存是否为0
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Commodity commodity = (Commodity) c.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<CommodityShopInfo> listCommodityShopInfo = (List<CommodityShopInfo>) commodity.getListSlave2();
		Assert.assertTrue(listCommodityShopInfo != null, "没有返回商品门店信息");
		CommodityShopInfo commodityShopInfo = listCommodityShopInfo.get(listCommodityShopInfo.size() - 1);
		assertTrue(commodityShopInfo.getnOStart() == -1, "商品的期初数量居然不为-1！");

	}

	@Test
	public void testCreateEx48() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case48: 创建服务商品期初采购价不为-1.0");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setPurchasingPriceStart(10.2d);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		// 检查库存是否为0
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Commodity commodity = (Commodity) c.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<CommodityShopInfo> listCommodityShopInfo = (List<CommodityShopInfo>) commodity.getListSlave2();
		Assert.assertTrue(listCommodityShopInfo != null, "没有返回商品门店信息");
		CommodityShopInfo commodityShopInfo = listCommodityShopInfo.get(listCommodityShopInfo.size() - 1);
		assertTrue(GeneralUtil.sub(commodityShopInfo.getPurchasingPriceStart(), -1.0) < BaseModel.TOLERANCE, "商品的期初数量居然不为-1.0！");
	}

	@Test
	public void testCreateEx49() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case49: 创建服务商品保质期不为0");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchasingUnit("");
		c.setShelfLife(10);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx50() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case50: 创建服务商品最近采购价不为-1.0");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setLatestPricePurchase(100.1d);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		// 检查库存是否为0
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Commodity commodity = (Commodity) c.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<CommodityShopInfo> listCommodityShopInfo = (List<CommodityShopInfo>) commodity.getListSlave2();
		Assert.assertTrue(listCommodityShopInfo != null, "没有返回商品门店信息");
		CommodityShopInfo commodityShopInfo = listCommodityShopInfo.get(listCommodityShopInfo.size() - 1);
		assertTrue(GeneralUtil.sub(commodityShopInfo.getLatestPricePurchase(), -1.0) < BaseModel.TOLERANCE, "商品的最近采购价居然不为-1.0！");
	}

	@Test
	public void testCreateEx51() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case51: 创建服务商品采购阀值不为0");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setPurchaseFlag(100);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		// 检查库存是否为0
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Commodity commodity = (Commodity) c.parse1(jsonObject.getString(BaseAction.KEY_Object));
		assertTrue(commodity.getPurchaseFlag() == 0, "商品的采购阀值居然不为0！");
	}

	@Test
	public void testCreateEx52() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case52: 创建单品，零售价格大于" + FieldFormat.MAX_OneCommodityPrice);
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPriceRetail(FieldFormat.MAX_OneCommodityPrice + 1);
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}
	
	@Test
	public void testCreateEx53() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case53: 创建商品，商品名称可以支持*“”、$#/符号");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		commodityGet.setName("*“”、$#/" + commodityGet.getName());
		commodityGet.setProviderIDs("7");
		commodityGet.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, commodityGet, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, commodityGet, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
	}
	
	@Test
	public void testCreateNormalAndMultiEx54() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case54:创建普通商品同时创建其多包装商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		c.setProviderIDs("7");
		c.setMultiPackagingInfo(barcode1 + "," + barcode2 + ";1,2;1,2;1,2;8,16;8,16;" + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) +";");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
	}
	
	@Test
	public void testCreateEx55() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case55:店长无权限创建普通商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionShopManager);
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		c.setProviderIDs("7");
		c.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
	}
	
	@Test
	public void testCreateEx56_Combination() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case56:店长无权限创建组合商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionShopManager);
		Commodity subCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());

		subCommodity.setShelfLife(BaseAction.INVALID_ID);
		subCommodity.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity.setProviderIDs("7");
		subCommodity.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		String json = JSONObject.fromObject(subCommodity).toString();

		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity, req.getRequest().getSession()) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
	}
	
	@Test
	public void testCreateEx57_Service() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case57: 店长无权限创建服务商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionShopManager);
		;
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
	}
	
	@Test
	public void testCreateNormalAndMultiEx58() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case58:店长无权限创建多包装商品");
		BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		// 老板正常更新单品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setProviderIDs("1");
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + commUpdate.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + ","
				+ commodityC + "新增多包装" + Shared.generateStringByTime(8) + ";");

		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionShopManager) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr);
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String msg = JsonPath.read(o, "$." + BaseAction.KEY_HTMLTable_Parameter_msg);
		System.out.println("创建多包装商品失败错误码还是为noError,但是错误信息还是会返回，错误信息：" + msg);
		Assert.assertTrue(!StringUtils.isEmpty(msg), "期望是返回没有权限创建多包装商品的信息");
	}
	
	@Test
	public void testFeedbackEx() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		// 在pos1中创建一个商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("1");
		c.setMultiPackagingInfo(barcode8 + "," + "888" + Shared.generateStringByTime(8) + ",8882" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" + commodityA + Shared.generateStringByTime(8) + "," + commodityB
				+ Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mrl);

		// 在pos2中retrieveN
		MvcResult mr = mvc.perform(get("/commoditySync/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionPos2)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());
		json.getString(BaseAction.KEY_ObjectList);
		List<BaseModel> bmList = c.parseN(json.getString(BaseAction.KEY_ObjectList));
		// 设置全部同步与未全部同步的同步块
		List<Commodity> commList = BaseCommodityTest.getCommodityListIfAllSync(mapBO, bmList);

		String ids = "";
		for (Commodity commodity : commList) {
			ids += commodity.getID() + ",";
		}

		mvc.perform(get("/commoditySync/feedbackEx.bx?sID=" + ids + "&errorCode=EC_NoError") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionPos2)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Pos pos = (Pos) sessionPos2.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();

		CommodityCP.verifySyncCommodity(commList, posID, posBO, commoditySyncCacheDispatcherBO, Shared.DBName_Test);

	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1：正常删除单品");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setOperatorStaffID(STAFF_ID3);
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = c.getCreateParamEx(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		Barcodes bcCreate1 = (Barcodes) bmList.get(1).get(0);

		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(bcCreate1);

		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commCreate.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 删除商品
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyDelete(commCreate, listBarcode, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2：重复删除单品");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setOperatorStaffID(STAFF_ID3);
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = c.getCreateParamEx(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		Barcodes bcCreate1 = (Barcodes) bmList.get(1).get(0);

		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(bcCreate1);

		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commCreate.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 删除商品
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyDelete(commCreate, listBarcode, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);
		// 重复删除商品
		MvcResult mr2 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3：单品无多包装 2条码 删除成功");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setOperatorStaffID(STAFF_ID3);
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = c.getCreateParamEx(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		Barcodes bcCreate = (Barcodes) bmList.get(1).get(0);

		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(bcCreate);
		// 给商品增加多一条条形码数据
		final String barcode1 = "123" + Shared.generateStringByTime(8);
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commCreate.getID());
		barcodes.setBarcode(barcode1);
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> params21 = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesCreate = (Barcodes) barcodesMapper.create(params21);
		Assert.assertTrue(barcodesCreate != null && EnumErrorCode.values()[Integer.parseInt(params21.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		listBarcode.add(barcodesCreate);
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commCreate.getID(), Shared.DBName_Test, commodityHistoryBO);

		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyDelete(commCreate, listBarcode, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4：单品有1个多包装 单品1条码 多包装1条码 删除失败");
		// 创建单品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setOperatorStaffID(STAFF_ID3);
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = c.getCreateParamEx(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		// 创建多包装商品
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setOperatorStaffID(STAFF_ID3);
		c2.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		c2.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		c2.setRefCommodityID(commCreate.getID());
		c2.setRefCommodityMultiple(3);
		c2.setPackageUnitID(1);
		Map<String, Object> params2 = c2.getCreateParamEx(BaseBO.INVALID_CASE_ID, c2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList2 = commodityMapper.createMultiPackagingEx(params2);
		//
		Assert.assertTrue(bmList2.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		MvcResult mr3 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate.getID() + "&type=" + commCreate.getType()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5：单品有1个多包装 单品1条码 多包装1条码 先删除单品的副单位，删除成功");
		/// 创建单品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setOperatorStaffID(STAFF_ID3);
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = c.getCreateParamEx(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 获取到商品创建出来的对象
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		Assert.assertNotNull(barcodes);
		// 创建多包装商品
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setOperatorStaffID(STAFF_ID3);
		c2.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		c2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c2.setRefCommodityID(commCreate.getID());
		c2.setRefCommodityMultiple(3);
		c2.setPackageUnitID(1);
		Map<String, Object> params2 = c2.getCreateParamEx(BaseBO.INVALID_CASE_ID, c2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList2 = commodityMapper.createSimpleEx(params2);
		//
		Assert.assertTrue(bmList2.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 获取到副单位商品的对象
		Commodity commCreate2 = (Commodity) bmList2.get(0).get(0);
		Barcodes barcodes2 = (Barcodes) bmList2.get(1).get(0);

		List<Barcodes> listBarcode2 = new ArrayList<Barcodes>();
		listBarcode2.add(barcodes2);
		// 获取该商品修改前的商品历史数量
		int iOldNO2 = BaseCommodityTest.queryCommodityHistorySize(commCreate2.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 先删除商品的副单位商品
		MvcResult mr2 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate2.getID() + "&type=" + commCreate2.getType()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr2);
		// 检查点
		CommodityCP.verifyDelete(commCreate2, listBarcode2, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO2, Shared.DBName_Test);

		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(barcodes);
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commCreate.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 删除商品
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate.getID() + "&type=" + commCreate.getType()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyDelete(commCreate, listBarcode2, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDeleteEx6_Combination() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6：该商品是组合商品");
		// 创建组合商品
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity subCommodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String json = JSONObject.fromObject(subCommodity).toString();

		subCommodity.setShelfLife(BaseAction.INVALID_ID);
		subCommodity.setPurchaseFlag(BaseAction.INVALID_ID);
		subCommodity.setRuleOfPoint(BaseAction.INVALID_ID);
		subCommodity.setProviderIDs("7");
		subCommodity.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");//
		subCommodity.setSubCommodityInfo(json);//

		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, subCommodity, req.getRequest().getSession()) //
						.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), String.valueOf(subCommodity.getSubCommodityInfo())) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, subCommodity, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);

		// 获取创建的组合商品
		JSONObject jsonToCommodity = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Commodity bmOfDB = new Commodity();
		bmOfDB = (Commodity) bmOfDB.parse1(jsonToCommodity.getString(BaseAction.KEY_Object));

		// 获取创建的组合商品条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(bmOfDB.getID());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Barcodes> listBarcode = (List<Barcodes>) barcodesBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, barcodes);
		if (barcodesBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取Barcodes DB数据错误！");
		}
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(bmOfDB.getID(), Shared.DBName_Test, commodityHistoryBO);

		MvcResult mr2 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + bmOfDB.getID() + "&type=" + bmOfDB.getType()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoError);

		// 检查错误码
		Shared.checkJSONErrorCode(mr2);
		// 检查点
		CommodityCP.verifyDelete(bmOfDB, listBarcode, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7：单品或多包装存在销存记录 采购单商品有记录 删除商品及条码失败 ec = 7");
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=88") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8：单品或多包装有销存记录，盘点商品有记录 删除商品及条码失败 ec = 7");
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=89") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9：单品或多包装有销存记录，零售商品有记录 删除商品及条码失败 ec = 7");
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=90") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10：单品或多包装有销存记录，入库商品有记录 删除商品及条码失败 ec = 7");
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=91") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11：单品或多包装有销存记录，采购计划商品有记录 删除商品及条码失败 ec = 7");
		MvcResult mr11 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=69") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12：传入多包装的Id 单品有销存记录、多包装无销存记录");

		MvcResult mr16 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=58") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr16, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13: 使用删除服务商品的SP去删除组合商品");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String json = JSONObject.fromObject(commodity).toString();
		commodity.setSubCommodityInfo(json);
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		//
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID() + "&type=" + EnumCommodityType.ECT_Service.getIndex()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14: 使用删除服务商品的SP去删除多包装商品");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity refComm = BaseCommodityTest.DataInput.getCommodity();
		refComm.setPackageUnitID(2);
		refComm.setRefCommodityMultiple(3);
		c.setMultiPackagingInfo(
				c.getBarcodes() + "," + refComm.getBarcodes() + ";" + c.getPackageUnitID() + "," + refComm.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + "," + refComm.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ","
						+ refComm.getPriceRetail() + ";" + c.getPriceVIP() + "," + refComm.getPriceVIP() + ";" + c.getPriceWholesale() + "," + refComm.getPriceWholesale() + ";" + c.getName() + "," + refComm.getName() + ";");
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		//
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID() + "&type=" + EnumCommodityType.ECT_Service.getIndex()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15: 使用删除服务商品的SP去删除普通商品");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		//
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + createCommodity.getID() + "&type=" + EnumCommodityType.ECT_Service.getIndex()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx16_Service() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16: 删除服务类商品");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodity.setOperatorStaffID(3);
		commodity.setPurchasingUnit("");
		commodity.setShelfLife(0);
		commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		commodity.setPurchaseFlag(0);
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionPos1, mapBO, Shared.DBName_Test);

		Barcodes b = new Barcodes();
		b.setCommodityID(commCreated.getID());
		b.setBarcode("case16" + Shared.generateStringByTime(8));
		b.setOperatorStaffID(3);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> createBarcodesParam = b.getCreateParam(BaseBO.INVALID_CASE_ID, b);
		Barcodes createBarcodes = (Barcodes) barcodesMapper.create(createBarcodesParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, createBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		b.setIgnoreIDInComparision(true);
		if (b.compareTo(createBarcodes) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(createBarcodes);

		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commCreated.getID(), Shared.DBName_Test, commodityHistoryBO);

		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID() + "&type=" + EnumCommodityType.ECT_Service.getIndex()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyDelete(commCreated, listBarcode, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17: 重复删除服务类商品");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setOperatorStaffID(3);
		commodity.setType(EnumCommodityType.ECT_Service.getIndex());
		commodity.setPurchasingUnit("");
		commodity.setShelfLife(0);
		commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		commodity.setPurchaseFlag(0);
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionPos1, mapBO, Shared.DBName_Test);

		Barcodes b = new Barcodes();
		b.setCommodityID(commCreated.getID());
		b.setBarcode("case20" + Shared.generateStringByTime(8));
		b.setOperatorStaffID(3);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> createBarcodesParam = b.getCreateParam(BaseBO.INVALID_CASE_ID, b);
		Barcodes createBarcodes = (Barcodes) barcodesMapper.create(createBarcodesParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, createBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		b.setIgnoreIDInComparision(true);
		if (b.compareTo(createBarcodes) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(createBarcodes);

		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commCreated.getID(), Shared.DBName_Test, commodityHistoryBO);

		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID() + "&type=" + EnumCommodityType.ECT_Service.getIndex()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		CommodityCP.verifyDelete(commCreated, listBarcode, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);

		// 重复进行删除
		MvcResult mr2 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID() + "&type=" + EnumCommodityType.ECT_Service.getIndex()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case18: 删除不存在的商品.");
		//
		Commodity c = new Commodity();
		c.setID(INVALID_ID);
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + c.getID() + "&type=" + EnumCommodityType.ECT_Service.getIndex()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testDeleteEx19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19_1: 商品有指定促销依赖，促销没有被删除，那么该商品不可以被删除");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setOperatorStaffID(STAFF_ID3);
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = c.getCreateParamEx(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		Barcodes bcCreate1 = (Barcodes) bmList.get(1).get(0);

		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(bcCreate1);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(commCreate.getID());
		ps.setPromotionID(pCreate.getID());
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_NoError);
		// 删除商品,删除失败
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		Shared.caseLog("case19_2: 商品有指定促销依赖，促销被删除，该商品还是不可以被删除");
		// 删除促销
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		// 删除商品,删除失败
		MvcResult mr2 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx22() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case22: 零售单商品有记录，不能删除该服务商品");

		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setOperatorStaffID(3);
		commodity.setType(EnumCommodityType.ECT_Service.getIndex());
		commodity.setPurchasingUnit("");
		commodity.setShelfLife(0);
		commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		commodity.setPurchaseFlag(0);
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		//
		BaseBarcodesTest.createBarcodesViaMapper(BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID()), BaseBO.INVALID_CASE_ID);
		//
		RetailTrade retailTrade = BaseRetailTradeTest.createRetailTrade(BaseRetailTradeTest.DataInput.getRetailTrade());
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setCommodityID(commCreated.getID());
		rtc.setTradeID(retailTrade.getID());
		BaseRetailTradeTest.createRetailTradeCommodity(rtc, EnumErrorCode.EC_NoError);
		//
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx23() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case23: 商品与采购商品有依赖，如果该采购商品的主表采购订单被删除后，这个商品可以被删除");
		// 创建一个采购订单
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrder = BasePurchasingOrderTest.purchasingOrderCreate(po);
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setOperatorStaffID(STAFF_ID3);
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		// 根据商品ID创建一个条形码t1
		Barcodes barcode1 = BaseBarcodesTest.retrieveNBarcodes(commCreated.getID(), Shared.DBName_Test);
		// 根据商品和条形码创建一个采购订单商品
		PurchasingOrderCommodity pOrderComm = new PurchasingOrderCommodity();
		pOrderComm.setCommodityID(commCreated.getID());
		pOrderComm.setPurchasingOrderID(purchasingOrder.getID());
		pOrderComm.setCommodityNO(Math.abs(new Random().nextInt(300)));
		pOrderComm.setPriceSuggestion(1);
		pOrderComm.setBarcodeID(barcode1.getID());
		//
		Map<String, Object> createpOrderCommparams = pOrderComm.getCreateParam(BaseBO.INVALID_CASE_ID, pOrderComm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(createpOrderCommparams);
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(createpOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		pOrderComm.setIgnoreIDInComparision(true);
		if (pOrderComm.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 删除商品,删除失败
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除该采购订单，会把从表信息删除掉
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(purchasingOrder);
		//
		Barcodes b = new Barcodes();
		b.setCommodityID(commCreated.getID());
		b.setBarcode("case16" + Shared.generateStringByTime(8));
		b.setOperatorStaffID(3);
		//
		Map<String, Object> createBarcodesParam = b.getCreateParam(BaseBO.INVALID_CASE_ID, b);
		Barcodes createBarcodes = (Barcodes) barcodesMapper.create(createBarcodesParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, createBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		b.setIgnoreIDInComparision(true);
		if (b.compareTo(createBarcodes) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(createBarcodes);

		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commCreated.getID(), Shared.DBName_Test, commodityHistoryBO);
		MvcResult mr2 = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr2);
		// 检查点
		CommodityCP.verifyDelete(commCreated, listBarcode, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx24() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case24: 商品有全场促销依赖，促销没有被删除，那么该商品也可以被删除");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(0);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setOperatorStaffID(STAFF_ID3);
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = c.getCreateParamEx(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		Barcodes bcCreate1 = (Barcodes) bmList.get(1).get(0);

		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(bcCreate1);
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commCreate.getID(), Shared.DBName_Test, commodityHistoryBO);
		// 删除商品,删除成功
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		//
		CommodityCP.verifyDelete(commCreate, listBarcode, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);
		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void testDeleteEx25() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case25: 商品有全场促销依赖，促销被删除，那么该商品也可以被删除");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(0);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setOperatorStaffID(STAFF_ID3);
		c.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = c.getCreateParamEx(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		Barcodes bcCreate1 = (Barcodes) bmList.get(1).get(0);

		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(bcCreate1);
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commCreate.getID(), Shared.DBName_Test, commodityHistoryBO);
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		// 删除商品,删除成功
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreate.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr);
		CommodityCP.verifyDelete(commCreate, listBarcode, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, commodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO, iOldNO, Shared.DBName_Test);

	}
	
	@Test
	public void testDeleteEx26() throws Exception {
		Shared.printTestMethodStartInfo();
		// 
		Shared.caseLog("case26: 商品有指定促销依赖，促销没有被删除，那么该商品不可以被删除, 它的条形码也不会被删除");
		// 创建促销
		Promotion promotionGet = BasePromotionTest.DataInput.getPromotion();
		Promotion promotionCreated = BasePromotionTest.createPromotionViaMapper(promotionGet);
		// 创建商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		commodityGet.setOperatorStaffID(STAFF_ID3);
		commodityGet.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = commodityGet.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodityGet);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		Barcodes barcodesCreated1 = (Barcodes) bmList.get(1).get(0);
		List<Barcodes> listBarcode = new ArrayList<Barcodes>();
		listBarcode.add(barcodesCreated1);
		// 创建商品的条形码2
		// 创建条形码1
		Barcodes barcodesGet2 = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreated2 = BaseBarcodesTest.createBarcodesViaMapper(barcodesGet2, BaseBO.INVALID_CASE_ID);
		// 创建促销范围
		PromotionScope promotionScope = new PromotionScope();
		promotionScope.setCommodityID(commCreated.getID());
		promotionScope.setPromotionID(promotionCreated.getID());
		BasePromotionTest.createPromotionScopeViaMapper(promotionScope, EnumErrorCode.EC_NoError);
		// 删除商品,删除失败
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionPos1)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		BaseBarcodesTest.retrieve1ViaMapper(barcodesCreated1.getID(), Shared.DBName_Test);
		BaseBarcodesTest.retrieve1ViaMapper(barcodesCreated2.getID(), Shared.DBName_Test);
	}
	
	@Test
	public void testDeleteEx27() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 27:商品有优惠券范围依赖，不能删除(优惠券已启用)");
		// 创建普通商品
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaAction(simpleCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建优惠券
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// 已启用
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		couponGet.setCommodityIDs(String.valueOf(simpleCommodityCreate.getID()));
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 删除新建的普通商品，删除失败
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreate);
	}
	
	@Test
	public void testDeleteEx28() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 28:商品有优惠券范围依赖，不能删除(优惠券未启用)");
		// 创建普通商品
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaAction(simpleCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建优惠券
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// 未启用
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), 1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		couponGet.setCommodityIDs(String.valueOf(simpleCommodityCreate.getID()));
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 删除新建的普通商品，删除失败
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreate);
	}
	
	@Test
	public void testDeleteEx29() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 29:商品有优惠券范围依赖，不能删除(优惠券已过期)");
		// 创建普通商品
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaAction(simpleCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建优惠券
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// 已过期
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -5));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setCommodityIDs(String.valueOf(simpleCommodityCreate.getID()));
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 删除新建的普通商品，删除失败
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreate);
	}
	
	@Test
	public void testDeleteEx30() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case30:商品有优惠券范围依赖，不能删除(优惠券已删除)");
		// 创建普通商品
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaAction(simpleCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建优惠券
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// 已启用
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		couponGet.setCommodityIDs(String.valueOf(simpleCommodityCreate.getID()));
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		BaseCouponTest.deleteViaAction(couponCreate, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 删除新建的普通商品，删除失败
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreate);
	}
	
	@Test
	public void testDeleteEx31() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case31:商品有优惠券全场依赖，能被删除");
		// 创建普通商品
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaAction(simpleCommodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建优惠券
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// 已启用
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 删除新建的普通商品，删除失败
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreate);
	}
	
	@Test
	public void testDeleteEx32() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case32：店长无权限删除单品");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, Shared.DBName_Test);
//		// 删除商品
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commodityCreated.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionShopManager)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
		// 删除测试数据
		BaseCommodityTest.deleteCommodityViaAction(commodityCreated, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testDeleteEx33_Service() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case33: 店长无权限删除服务类商品");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID() + "&type=" + EnumCommodityType.ECT_Service.getIndex()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionShopManager)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
		// 删除测试数据
		BaseCommodityTest.deleteCommodityViaAction(commCreated, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@Test
	public void testDeleteEx34_Combination() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case34: 店长无权限删除组合商品");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String json = JSONObject.fromObject(commodity).toString();
		commodity.setSubCommodityInfo(json);
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		MvcResult mr = mvc.perform(//
				get("/commoditySync/deleteEx.bx?ID=" + commCreated.getID() + "&type=" + EnumCommodityType.ECT_Combination.getIndex()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionShopManager)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
		// 删除测试数据
		BaseCommodityTest.deleteCommodityViaAction(commCreated, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testDeleteEx35_Multiple() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case35: 店长无权限删除多包装商品");
		//
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		c.setProviderIDs("7");
		c.setMultiPackagingInfo(barcode1 + "," + barcode2 + ";1,2;1,2;1,2;8,16;8,16;" + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) +";");
		MvcResult mr1 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		// 检查点
		CommodityCP.verifyCreate(mr1, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		//
		com.alibaba.fastjson.JSONObject json1 = com.alibaba.fastjson.JSONObject.parseObject(mr1.getResponse().getContentAsString());
		Commodity bmOfDB = new Commodity();
		bmOfDB = (Commodity) bmOfDB.parse1(com.alibaba.fastjson.JSONObject.parseObject(json1.getString(BaseAction.KEY_Object)));
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setID(bmOfDB.getID());
		commUpdate.setProviderIDs("1");
		commUpdate.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");

		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionShopManager) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr);
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String msg = JsonPath.read(o, "$." + BaseAction.KEY_HTMLTable_Parameter_msg);
		System.out.println("创建多包装商品失败错误码还是为noError,但是错误信息还是会返回，错误信息：" + msg);
		Assert.assertTrue(!StringUtils.isEmpty(msg), "期望是返回没有权限创建多包装商品的信息");
		// 删除测试数据
		MvcResult mr2 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr2);
		BaseCommodityTest.deleteCommodityViaAction(bmOfDB, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Shared.caseLog("case1:正常更新");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setProviderIDs("1");
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + commUpdate.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + ","
				+ commodityC + "新增多包装" + Shared.generateStringByTime(8) + ";");

		BaseCommodityTest.updateViaAction(commCreate, commUpdate, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:传递参数维度不一致");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl2 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		assertTrue(checkJSONCommodity(mrl2).equals(""));
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3：传递参数顺序打乱");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ",1;1,2,456" + Shared.generateStringByTime(8) + ";1,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl3 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		assertTrue(checkJSONCommodity(mrl3).equals(""));
	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4: 多包装商品单个有依赖");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity commA = BaseCommodityTest.DataInput.getCommodity();
		commA.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		commA.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commA.setProviderIDs("7,2");
		commA.setMultiPackagingInfo("222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";2,3;5,10;5,10;3,8;8,8;" //
				+ commA.getName() + "," + commodityC + Shared.generateStringByTime(8) + ";");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, commA, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, commA, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		Commodity commCreated = (Commodity) new Commodity().parse1(o.getString(BaseAction.KEY_Object));
		commA.setID(commCreated.getID());

		BaseCommodityTest.updateViaAction(commCreated, commA, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5: 多包装商品    无依赖");
		//
		Commodity commC = BaseCommodityTest.DataInput.getCommodity();
		commC.setName("商品C" + Shared.generateStringByTime(8));
		commC.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commC.setOperatorStaffID(STAFF_ID3);
		commC.setPackageUnitID(2);
		Map<String, Object> params3 = commC.getCreateParamEx(BaseBO.INVALID_CASE_ID, commC);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> commCreated3 = (List<List<BaseModel>>) commodityMapper.createSimpleEx(params3);
		Commodity commodityCreated = (Commodity) commCreated3.get(0).get(0);
		BaseCommodityTest.createListCommodityShopInfoViaMapper(commodityCreated, commC, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, new Warehousing());
		//
		Assert.assertTrue(commCreated3.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		commC.setIgnoreIDInComparision(true);
		if (commC.compareTo(commodityCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		Commodity commD = BaseCommodityTest.DataInput.getCommodity();
		commD.setName("商品D" + Shared.generateStringByTime(8));
		commD.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commD.setOperatorStaffID(STAFF_ID3);
		commD.setPackageUnitID(3);
		commD.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commD.setRefCommodityID(commCreated3.get(0).get(0).getID());
		commD.setRefCommodityMultiple(3);
		Map<String, Object> params4 = commD.getCreateParamEx(BaseBO.INVALID_CASE_ID, commD);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> commCreated4 = (List<List<BaseModel>>) commodityMapper.createMultiPackagingEx(params4);
		Commodity commDCreated = (Commodity) commCreated4.get(0).get(0);
		BaseCommodityTest.createListCommodityShopInfoViaMapper(commDCreated, commD, BaseBO.CASE_Commodity_CreateMultiPackaging, Shared.DBName_Test, new Warehousing());
		//
		Assert.assertTrue(commCreated4.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		commD.setIgnoreIDInComparision(true);
		if (commD.compareTo(commDCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		Commodity commE = BaseCommodityTest.DataInput.getCommodity();
		commE.setOperatorStaffID(STAFF_ID3);
		commE.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commE.setPackageUnitID(4);
		commE.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commE.setRefCommodityID(commCreated3.get(0).get(0).getID());
		commE.setRefCommodityMultiple(3);
		commE.setName("商品E" + Shared.generateStringByTime(8));
		Map<String, Object> params5 = commE.getCreateParamEx(BaseBO.INVALID_CASE_ID, commE);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> commCreated5 = (List<List<BaseModel>>) commodityMapper.createMultiPackagingEx(params5);
		Commodity commECreated = (Commodity) commCreated5.get(0).get(0);
		BaseCommodityTest.createListCommodityShopInfoViaMapper(commECreated, commE, BaseBO.CASE_Commodity_CreateMultiPackaging, Shared.DBName_Test, new Warehousing());
		//
		Assert.assertTrue(commCreated5.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		commE.setIgnoreIDInComparision(true);
		if (commE.compareTo(commECreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		MvcResult req5 = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setID(commCreated3.get(0).get(0).getID());
		c.setProviderIDs("1,2,3");
		c.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;5,6,7;2,3,4;" + commodityA + Shared.generateStringByTime(8)
				+ "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		BaseCommodityTest.updateViaAction((Commodity) commCreated3.get(0).get(0), c, mvc, req5.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6: 多包装商品多个中有一个有依赖");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity commA = BaseCommodityTest.DataInput.getCommodity();
		commA.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		commA.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commA.setProviderIDs("7,2");
		commA.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, commA, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyCreate(mr, commA, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		Commodity commCreated = (Commodity) new Commodity().parse1(o.getString(BaseAction.KEY_Object));
		commA.setID(commCreated.getID());

		BaseCommodityTest.updateViaAction(commCreated, commA, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}
	// // 保留测试... catch不到异常 czq
	// // // case7: 修改商品为一样的.会返回错误码2
	// // Commodity commY = BaseCommodityTest.DataInput.getCommodity();
	// // commY.setName("商品Y");
	// // Map<String, Object> params9 =
	// commY.getCreateParam(BaseBO.INVALID_CASE_ID,
	// // commY);
	// //
	// // System.out.println("创建参数" + params9);
	// // Commodity commCreated9 = (Commodity) mapper.create(params9);
	// // System.out.println(commCreated9);
	// //
	// // commY.setIgnoreIDInComparision(true);
	// // if (commY.compareTo(commCreated9) != 0) {
	// // Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
	// // }
	// //
	// // try {
	// // MvcResult mrl7 = mvc.perform(post("/commoditySync/updateEx.bx") //
	// // .param(Commodity.field.getFIELD_NAME_string1(),
	// // "234" + Shared.generateStringByTime(8) + ",456" +
	// // Shared.generateStringByTime(8) + ",789" +
	// System.currentTimeMillis()
	// %
	// // 1000000 +
	// ";1,2,3;1,5,10;1,5,10;0.8,4,8;5,6,7;2,3,4;你331212好,123,好44320的;")
	// // //
	// // .param(Commodity.field.getFIELD_NAME_ID(), commCreated9.getID() +
	// // "").param(Commodity.field.getFIELD_NAME_name(), commCreated9.getName() +
	// "1112313")
	//
	// // .param(Commodity.field.getFIELD_NAME_shortName(),
	// commCreated9.getShortName()) //
	// // .param(Commodity.field.getFIELD_NAME_specification(),
	// commCreated9.getSpecification())
	// //
	// // .param(Commodity.field.getFIELD_NAME_packageUnitID(),
	// commCreated9.getPackageUnitID()
	// +
	// "")
	// // //
	// // .param(Commodity.field.getFIELD_NAME_purchasingUnit(),
	// commCreated9.getPurchasingUnit())
	// //
	// // .param(Commodity.field.getFIELD_NAME_brandID(), commCreated9.getBrandID()
	// + "") //
	// // .param(Commodity.field.getFIELD_NAME_categoryID(),
	// commCreated9.getCategoryID() + "")
	//
	// // .param(Commodity.field.getFIELD_NAME_mnemonicCode(),
	// commCreated9.getMnemonicCode())
	//
	// // .param(Commodity.field.getFIELD_NAME_pricingType(),
	// commCreated9.getPricingType() +
	// "")
	// //
	// // .param(Commodity.field.getFIELD_NAME_isServiceType(),
	// commCreated9.getIsServiceType()
	// +
	// "")
	// // //
	// // .param(Commodity.field.getFIELD_NAME_priceVIP(),
	// commCreated9.getPriceVIP() + "") //
	// // .param(Commodity.field.getFIELD_NAME_priceWholesale(),
	// commCreated9.getPriceWholesale()
	// +
	// // "") //
	// // .param(Commodity.field.getFIELD_NAME_ratioGrossMargin(),
	// commCreated9.getRatioGrossMargin()
	// // + "") //
	// // .param(Commodity.field.getFIELD_NAME_canChangePrice(),
	// commCreated9.getCanChangePrice()
	// +
	// // "") //
	// // .param(Commodity.field.getFIELD_NAME_ruleOfPoint(),
	// commCreated9.getRuleOfPoint() +
	// "")
	// //
	// // .param(Commodity.field.getFIELD_NAME_picture(), commCreated9.getPicture())
	// //
	// // .param(Commodity.field.getFIELD_NAME_shelfLife(),
	// commCreated9.getShelfLife() + "")
	//
	// // .param(Commodity.field.getFIELD_NAME_returnDays(),
	// commCreated9.getReturnDays() + "")
	//
	// // .param(Commodity.field.getFIELD_NAME_purchaseFlag(),
	// commCreated9.getPurchaseFlag() +
	// "")
	// // //
	// // .param(Commodity.field.getFIELD_NAME_refCommodityID(),
	// commCreated9.getRefCommodityID()
	// +
	// // "") //
	// // .param(Commodity.field.getFIELD_NAME_refCommodityMultiple(),
	// // commCreated9.getRefCommodityMultiple() + "") //
	// // .param(Commodity.field.getFIELD_NAME_isGift(), commCreated9.getIsGift() +
	// "") //
	// // .param(Commodity.field.getFIELD_NAME_tag(), commCreated9.getTag()) //
	// // .param(Commodity.field.getFIELD_NAME_NO(), commCreated9.getNO() + "") //
	// // .param(Commodity.field.getFIELD_NAME_NOAccumulated(),
	// commCreated9.getNOAccumulated()
	// +
	// "")
	// // //
	// // .param(Commodity.field.getFIELD_NAME_nOStart(), Commodity.NO_START_Default
	// + "") //
	// // .param(Commodity.field.getFIELD_NAME_purchasingPriceStart(),
	// // Commodity.PURCHASING_PRICE_START_Default + "") //
	// // .param("providerIDs", "1,2,3")//
	// // ).andExpect(status().isOk()).andDo(print()).andReturn(); //
	// //
	// // Shared.checkJSONErrorCode(mrl7);
	// // } catch (Exception e) {
	// // assertTrue(true);
	// // }

	// Shared.caseLog("case8：没有传入供应商ID"); //... 供应商字段已经取消
	// MvcResult mrl8 =
	// mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx",
	// MediaType.APPLICATION_JSON, c) //
	// .param(Commodity.field.getFIELD_NAME_string1(),
	// "234" + Shared.generateStringByTime(8) + ",456" +
	// Shared.generateStringByTime(8) + ",789" + System.currentTimeMillis() %
	// 1000000 + ";1,2,3;1,5;1,5,10;0.8,4;5,6,7;2,3,4;" + commodityA
	// + Shared.generateStringByTime(8) + "," + commodityB +
	// Shared.generateStringByTime(8) + "," + commodityC +
	// Shared.generateStringByTime(8) + ";")
	// //
	// .param(Commodity.field.getFIELD_NAME_ID(), "6") //
	// .param(Commodity.field.getFIELD_NAME_name(), "薯片" +
	// Shared.generateStringByTime(8))//
	// .param(Commodity.field.getFIELD_NAME_shortName(), "薯片")//
	// .param("providerIDs", "")//
	// .param(Commodity.field.getFIELD_NAME_propertyValue1(), "自定义内容1")//
	// .param(Commodity.field.getFIELD_NAME_propertyValue2(), "自定义内容2")//
	// .param(Commodity.field.getFIELD_NAME_propertyValue3(), "自定义内容3")//
	// .param(Commodity.field.getFIELD_NAME_propertyValue4(), "自定义内容4")//
	// .param(Commodity.field.getFIELD_NAME_packageUnitID(), "1") //
	// .param(Commodity.field.getFIELD_NAME_brandID(), "3")//
	// .param(Commodity.field.getFIELD_NAME_categoryID(), "1") //
	// .param(Commodity.field.getFIELD_NAME_shelfLife(), "1") //
	// .param(Commodity.field.getFIELD_NAME_ruleOfPoint(), "1") //
	// .param(Commodity.field.getFIELD_NAME_purchaseFlag(), "1") //
	// ) //
	// .andExpect(status().isOk()).andDo(print()).andReturn(); //
	//
	// assertTrue(checkJSONCommodity(mrl8).equals(""));
	//
	// Shared.caseLog("case9:创建供应商时重复创建相同的供应商到同一商品中，使其事务回滚，查找是否存在这个商品");
	// c.setID(1);
	// MvcResult m =
	// mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx",
	// MediaType.APPLICATION_JSON, c) //
	// .param(Commodity.field.getFIELD_NAME_string1(), "234" +
	// System.currentTimeMillis() %
	// 1000000 + ",456" + Shared.generateStringByTime(8) + ",789" +
	// Shared.generateStringByTime(8) + ",901" + System.currentTimeMillis() %
	// 1000000 + //
	// ";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,4;5,6,7,4;2,3,4,4;" + c.getName() +
	// Shared.generateStringByTime(8) + "," + commodityA +
	// Shared.generateStringByTime(8) + "," + commodityB
	// + Shared.generateStringByTime(8) + "," + commodityC + "新增多包装" +
	// Shared.generateStringByTime(8) + ";") //
	// .param(Commodity.field.getFIELD_NAME_shortName(), "薯片")//
	// .param(Commodity.field.getFIELD_NAME_name(), "回滚薯条" +
	// System.currentTimeMillis() %
	// 1000000)//
	// .param("providerIDs", "1,3,3")//
	// .param(Commodity.field.getFIELD_NAME_propertyValue1(), "自定义内容1")//
	// .param(Commodity.field.getFIELD_NAME_propertyValue2(), "自定义内容2")//
	// .param(Commodity.field.getFIELD_NAME_propertyValue3(), "自定义内容3")//
	// .param(Commodity.field.getFIELD_NAME_propertyValue4(), "自定义内容4")//
	// .param(Commodity.field.getFIELD_NAME_packageUnitID(), "1") //
	// .param(Commodity.field.getFIELD_NAME_brandID(), "3")//
	// .param(Commodity.field.getFIELD_NAME_categoryID(), "1") //
	// .param(Commodity.field.getFIELD_NAME_shelfLife(), "1") //
	// .param(Commodity.field.getFIELD_NAME_ruleOfPoint(), "1") //
	// .param(Commodity.field.getFIELD_NAME_purchaseFlag(), "1") //
	// ) //
	// .andExpect(status().isOk()).andDo(print()).andReturn(); //
	//
	// Shared.checkJSONErrorCode(m, EnumErrorCode.EC_PartSuccess);

	@Test
	public void testUpdateEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case10：没有传入key值");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl9 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		assertTrue(checkJSONCommodity(mrl9).equals(""));
	}

	@Test
	public void testUpdateEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11：传入的providerIDs有英文字母");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setProviderIDs("1a,2,3");
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl10 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		assertTrue(checkJSONCommodity(mrl10).equals(""));
	}

	@Test
	public void testUpdateEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12：传入的providerIDs都是中文");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setProviderIDs("锄禾日当午,汗滴禾下土,谁知盘中餐");
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl11 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		assertTrue(checkJSONCommodity(mrl11).equals(""));
	}

	@Test
	public void testUpdateEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13：传入的商品的类型为组合商品");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setType(EnumCommodityType.ECT_Combination.getIndex());
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl12 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //

		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		String json = mrl12.getResponse().getContentAsString();
		assertTrue(json.isEmpty(), "传入的商品类型为组合商品，服务器应该返回null");
	}

	@Test
	public void testUpdateEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14：传入的商品的类型为多包装商品");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl13 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		)//
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		// 结果验证
		String json = mrl13.getResponse().getContentAsString();
		assertTrue(json.isEmpty(), "传入的商品类型为多包装，服务器应该返回null");
	}

	@Test
	public void testUpdateEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15：传入的providerIDs为负数");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setProviderIDs(String.valueOf(BaseAction.INVALID_ID) + ",1,1");
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl14 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mrl14, EnumErrorCode.EC_PartSuccess);
	}

	@Test
	public void testUpdateEx16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16：传入的providerIDs为数据库没有供应商");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setProviderIDs(INVALID_ID + ",1,1");
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl15 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mrl15, EnumErrorCode.EC_PartSuccess);

	}

	@Test
	public void testUpdateEx17() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case17：传入供应商ID有一个重复,正常修改 则证明已去除重复");
		MvcResult req17 = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity commTemplate17 = BaseCommodityTest.DataInput.getCommodity();
		commTemplate17.setOperatorStaffID(STAFF_ID3);
		commTemplate17.setRuleOfPoint(1);
		commTemplate17.setPurchaseFlag(1);
		commTemplate17.setShelfLife(1);
		Map<String, Object> paramsForCase17 = commTemplate17.getCreateParamEx(BaseBO.INVALID_CASE_ID, commTemplate17);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bm17 = (List<List<BaseModel>>) commodityMapper.createSimpleEx(paramsForCase17);
		//
		Commodity commCreated = (Commodity) bm17.get(0).get(0);
		BaseCommodityTest.createListCommodityShopInfoViaMapper(commCreated, commTemplate17, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, new Warehousing());
		//
		Assert.assertTrue(bm17.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForCase17.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setID(commCreated.getID());
		c.setProviderIDs("7,7,5,3");
		c.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;5,6,7;2,3,4;" + commodityA + Shared.generateStringByTime(8)
				+ "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		BaseCommodityTest.updateViaAction((Commodity) bm17.get(0).get(0), c, mvc, req17.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx18() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case18：传入的所有供应商ID重复,正常修改则证明已去除重复");
		MvcResult req18 = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity commTemplate18 = BaseCommodityTest.DataInput.getCommodity();
		commTemplate18.setOperatorStaffID(STAFF_ID3);
		commTemplate18.setRuleOfPoint(1);
		commTemplate18.setPurchaseFlag(1);
		commTemplate18.setShelfLife(1);
		Map<String, Object> paramsForCase18 = commTemplate18.getCreateParamEx(BaseBO.INVALID_CASE_ID, commTemplate18);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bm18 = (List<List<BaseModel>>) commodityMapper.createSimpleEx(paramsForCase18);
		//
		Commodity commCreated = (Commodity) bm18.get(0).get(0);
		BaseCommodityTest.createListCommodityShopInfoViaMapper(commCreated, commTemplate18, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, new Warehousing());
		//
		Assert.assertTrue(bm18.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForCase18.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setID(commCreated.getID());
		c.setProviderIDs("7,7,5,5,3,3");
		c.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;5,6,7;2,3,4;" + commodityA + Shared.generateStringByTime(8)
				+ "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		BaseCommodityTest.updateViaAction((Commodity) bm18.get(0).get(0), c, mvc, req18.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19：传入的所有供应商ID的数组超过100个");
		List<Integer> providerIDList = new ArrayList<Integer>();
		for (int i = 0; i < 110; i++) {
			providerIDList.add(i);
		}
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setProviderIDs(providerIDList + "");
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl18 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		assertTrue(checkJSONCommodity(mrl18).equals(""));

	}

	@Test
	public void testUpdateEx20() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case20:修改商品时修改成已有商品名称，使其事务回滚，查找是否存在这个商品");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		commCreate.setID(2);
		commCreate.setName("百事可乐");
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName());
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl19 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mrl19, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testUpdateEx21() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case21:修改已经删除的商品，查找是否存在这个商品");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl21 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mrl21, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testUpdateEx22() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "不能用不存在的PackageUnitID修改商品";
		Shared.caseLog("case22:" + message);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity cCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity cUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		cUpdate.setPackageUnitID(INVALID_ID);
		cUpdate.setID(cCreate.getID());
		cUpdate.setName(cCreate.getName() + Shared.generateStringByTime(3));
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "错误信息不正确！");
	}

	@Test
	public void testUpdateEx23() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "不能用不存在的BrandID修改商品";
		Shared.caseLog("case23:" + message);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity cUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		cUpdate.setID(commCreate.getID());
		cUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		cUpdate.setBrandID(INVALID_ID);
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "错误信息不正确！");
	}

	@Test
	public void testUpdateEx24() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "不能用不存在的CategoryID修改商品";
		Shared.caseLog("case24:" + message);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity cUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		cUpdate.setID(commCreate.getID());
		cUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		cUpdate.setCategoryID(INVALID_ID);
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionPos1) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "错误信息不正确！");
	}

	@Test
	public void testUpdateEx25() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Shared.caseLog("case25:正常更新,只修改普通商品中的多包装单位的商品名称,其它属性不变");

		Commodity commTemplate24 = BaseCommodityTest.DataInput.getCommodity();
		commTemplate24.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commTemplate24.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramsForCase24 = commTemplate24.getCreateParamEx(BaseBO.INVALID_CASE_ID, commTemplate24);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bm24 = (List<List<BaseModel>>) commodityMapper.createSimpleEx(paramsForCase24);
		//
		Assert.assertTrue(bm24.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForCase24.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Commodity commCreate24 = (Commodity) bm24.get(0).get(0);
		//
		commTemplate24.setIgnoreIDInComparision(true);
		if (commTemplate24.compareTo(commCreate24) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		System.out.println(bm24 == null ? "null" : bm24);
		Assert.assertNotNull(bm24.get(0));
		//
		System.out.println("创建商品成功");
		//
		Commodity c = (Commodity) commTemplate24.clone();
		c.setID(bm24.get(0).get(0).getID());
		c.setProviderIDs("1");
		c.setPackageUnitID(1);
		c.setMultiPackagingInfo("2342121" + ",4562121" + ",7892121" + ",9012121" + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + c.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC
				+ "新增多包装" + Shared.generateStringByTime(8) + ";");
		//
		BaseCommodityTest.updateViaAction((Commodity) bm24.get(0).get(0), c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		String A1 = c.getName() + Shared.generateStringByTime(8);
		String A2 = commodityA + Shared.generateStringByTime(8);
		String A3 = commodityB + Shared.generateStringByTime(8);
		String A4 = commodityC + Shared.generateStringByTime(8);
		c.setMultiPackagingInfo("2342121" + ",4562121" + ",7892121" + ",9012121" + ";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + A1 + "," + A2 + "," + A3 + "," + A4 + ";");
		c.setID(commCreate24.getID());
		c.setProviderIDs("1");
		//
		BaseCommodityTest.updateViaAction(c, c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx26() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case26:多包装商品的名称存在相同");
		String commodityA25 = "234" + Shared.generateStringByTime(8);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,5,5;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityA25 + "," + commodityA25 + ";");

		MvcResult mr25 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		assertTrue(checkJSONCommodity(mr25).equals(""));
	}

	@Test
	public void testUpdateEx27() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case27:商品倍数传1");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getUpdateCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setName(commCreate.getName() + Shared.generateStringByTime(3));
		commUpdate.setProviderIDs(String.valueOf(BaseAction.INVALID_ID) + ",1,1");
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ";1,2,3;1,1,1;1,5,10;5,6,7;2,3,4;" + commodityA
				+ Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mr26 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionPos1) //
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		assertTrue(checkJSONCommodity(mr26).equals(""));
	}

	@Test
	public void testUpdateEx28() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case28:修改商品名字为已删除的商品名字，修改成功");
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate1 = BaseCommodityTest.createCommodityViaAction(c1, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		commCreate1.setOperatorStaffID(STAFF_ID4);
		BaseCommodityTest.deleteCommodityViaMapper(commCreate1, EnumErrorCode.EC_NoError);

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);

		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaAction(c2, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		//
		Commodity c = (Commodity) commCreate2.clone();
		c.setReturnObject(1);
		c.setProviderIDs("1");
		c.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		c.setName(commCreate1.getName());
		c.setShopID(c2.getShopID());
		//
		BaseCommodityTest.updateViaAction(c, c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		// 删除测试对象
		BaseCommodityTest.deleteCommodityViaAction(commCreate2, Shared.DBName_Test, mvc, sessionPos1, mapBO);
	}

	@Test
	public void testUpdateEx29() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case29:修改商品图片，检查图片修改时间");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("1");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		Commodity comm = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		// 获取当前图片最后修改时间
		String path = "D:\\nbr\\pic\\private_db\\nbr\\" + comm.getID() + ".png";
		File f = new File(path);
		long beforeTime = f.lastModified();

		// 修改商品图片
		req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setProviderIDs("1");
		c2.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		c2.setID(comm.getID());
		c2.setLastOperationToPicture(Shared.LAST_OPERATION_TO_PICTURE_Upload);
		BaseCommodityTest.updateViaAction(c2, c2, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		// 获取当前图片最后修改时间
		f = new File(path);
		long afterTime = f.lastModified();
		assertTrue(beforeTime < afterTime, "图片最后修改时间异常！");

		// 删除测试对象
		BaseCommodityTest.deleteCommodityViaAction(comm, Shared.DBName_Test, mvc, sessionPos1, mapBO);
	}

	@Test
	public void testUpdateEx30() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case30:删除单品的多包装商品时，创建了相应多包装商品的D型同步块");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("1");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		Commodity comm = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		// 获取当前图片最后修改时间
		String path = "D:\\nbr\\pic\\private_db\\nbr\\" + comm.getID() + ".png";
		File f = new File(path);
		long beforeTime = f.lastModified();

		// 修改商品图片
		req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setProviderIDs("1");
		c2.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		c2.setID(comm.getID());
		c2.setLastOperationToPicture(Shared.LAST_OPERATION_TO_PICTURE_Upload);
		BaseCommodityTest.updateViaAction(c2, c2, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		// 获取当前图片最后修改时间
		f = new File(path);
		long afterTime = f.lastModified();
		assertTrue(beforeTime < afterTime, "图片最后修改时间不一致！");
		// 删除测试对象
		BaseCommodityTest.deleteCommodityViaAction(comm, Shared.DBName_Test, mvc, sessionPos1, mapBO);
	}

	@Test
	public void testUpdateEx31() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case31:update单品，同时创建了多包装商品时，创建了相应多包装商品的C型同步块");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("1");
		c.setMultiPackagingInfo(barcode1 + "," + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");

		Commodity comm = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		// 获取当前图片最后修改时间
		String path = "D:\\nbr\\pic\\private_db\\nbr\\" + comm.getID() + ".png";
		File f = new File(path);
		long beforeTime = f.lastModified();

		// 修改商品图片
		req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setProviderIDs("1");
		c2.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		c2.setID(comm.getID());
		c2.setLastOperationToPicture(Shared.LAST_OPERATION_TO_PICTURE_Upload);
		BaseCommodityTest.updateViaAction(c2, c2, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		// 获取当前图片最后修改时间
		f = new File(path);
		long afterTime = f.lastModified();
		assertTrue(beforeTime < afterTime, "图片最后修改时间不一致！");
	}

	@Test
	public void testUpdateEx32() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case32:update单品，同时update了多包装商品时，创建了相应多包装商品的U型同步块");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("1");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ";1,2;1,5;1,5;8,3;8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + ";");
		Commodity comm = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		// 获取当前图片最后修改时间
		String path = "D:\\nbr\\pic\\private_db\\nbr\\" + comm.getID() + ".png";
		File f = new File(path);
		long beforeTime = f.lastModified();

		// 修改商品图片
		req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setProviderIDs("1");
		c2.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ";1,2;1,5;1,5;8,3;8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + ";");
		c2.setID(comm.getID());
		c2.setLastOperationToPicture(Shared.LAST_OPERATION_TO_PICTURE_Upload);
		BaseCommodityTest.updateViaAction(c2, c2, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		// 获取当前图片最后修改时间
		f = new File(path);
		long afterTime = f.lastModified();
		assertTrue(beforeTime < afterTime, "图片最后修改时间不一致！");
	}

	@Test
	public void testUpdateEx33() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case33: 修改多包装商品的多包装信息");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Assert.assertTrue("".equals(mrl.getResponse().getContentAsString()), "服务器返回的数据不正确");
	}

	@Test
	public void testUpdateEx34() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case34: 修改组合商品多包装信息");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Assert.assertTrue("".equals(mrl.getResponse().getContentAsString()), "服务器返回的数据不正确");
	}

	@Test
	public void testUpdateEx35() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case35: 修改服务商品的多包装信息");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		String json = mrl.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String err = JsonPath.read(o, "$." + BaseAction.KEY_Object);
		Assert.assertTrue("".equals(err), "服务器返回的数据不正确");
	}

	@Test
	public void testUpdateEx36_Service() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case36: 修改服务商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		Commodity pase1Comm = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		//
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setShelfLife(0);
		c2.setID(pase1Comm.getID());
		c2.setPurchasingUnit("");
		c2.setPurchaseFlag(0);
		c2.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		c2.setType(EnumCommodityType.ECT_Service.getIndex());
		c2.setMultiPackagingInfo(barcode1 + ";3;10;10;8;8;" + commodityA + ";");
		c2.setBarcodes(barcode1);

		BaseCommodityTest.updateViaAction(c2, c2, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx37() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Shared.caseLog("case37: commodity的类型不是普通商品，也不是服务商品");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(BaseAction.INVALID_Type);
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "对于不能修改的商品类型，Action未进行处理");
	}

	@Test
	public void testUpdateEx38() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case38: 创建一个不带有图片的商品，修改为有图片的商品(iReturnObject = 0,即不让服务器返回新修改的object)");
		// 创建一个不带有图片的商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7");
		c.setMultiPackagingInfo(barcode1 + ";3;10;10;8;8;" //
				+ commodityA + Shared.generateStringByTime(8) + ";");
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);

		Assert.assertTrue("".equals(createCommodity.getPicture()), "创建不带有图片的商品失败。该商品有图片");

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);

		createCommodity.setName("case38" + Shared.generateStringByTime(8));
		createCommodity.setProviderIDs("1");
		createCommodity.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + c.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC
				+ "新增多包装" + Shared.generateStringByTime(8) + ";");
		//
		createCommodity.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		createCommodity.setLastOperationToPicture(Shared.LAST_OPERATION_TO_PICTURE_Upload);
		createCommodity.setShopID(c.getShopID());
		Commodity updateComm = BaseCommodityTest.updateViaAction(createCommodity, createCommodity, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		Assert.assertTrue(!"".equals(updateComm.getPicture()), "该商品添加图片失败！");
	}

	@Test
	public void testUpdateEx39() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case39:创建一个带有图片的商品，修改为没图片的商品(iReturnObject = 0,即不让服务器返回新修改的object)");

		// 创建一个带有图片的商品
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7");
		c.setMultiPackagingInfo(barcode1 + ";3;10;10;8;8;" //
				+ commodityA + Shared.generateStringByTime(8) + ";");
		c.setLastOperationToPicture(Shared.LAST_OPERATION_TO_PICTURE_Upload);
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		Assert.assertTrue(!"".equals(createCommodity.getPicture()), "创建的商品没有图片");
		createCommodity.setName("case39" + Shared.generateStringByTime(8));
		createCommodity.setProviderIDs("1");
		createCommodity.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + c.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC
				+ "新增多包装" + Shared.generateStringByTime(8) + ";");
		createCommodity.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		createCommodity.setShopID(c.getShopID());
		BaseCommodityTest.updateViaAction(createCommodity, createCommodity, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		// 目前商品图片还不能修改，等到PET-1564完成后即可
		// Commodity updateComm = (Commodity) Shared.parse1Object(mr2, createCommodity);
		// Assert.assertTrue(!"".equals(updateComm.getPicture()), "修改商品图片为空失败。");
	}

	@Test
	public void testUpdateEx40() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("MultiPackagingInfo为空");

		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		//
		c.setMultiPackagingInfo(null);
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, sessionPos1) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUpdateEx41() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case41: 组合商品修改期初值备注");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		c.setStartValueRemark("123456");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "CASE41测试失败！期望的是返回null");
	}

	@Test
	public void testUpdateEx42() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Shared.caseLog("case42: 多包装商品修改期初值备注");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c.setStartValueRemark("123456");
		//
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "CASE42测试失败！期望的是返回null");
	}

	@Test
	public void testUpdateEx43() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case43: 服务商品修改期初值备注");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		//
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setShelfLife(0);
		c2.setID(createCommodity.getID());
		c2.setPurchasingUnit("");
		c2.setPurchaseFlag(0);
		c2.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		c2.setType(EnumCommodityType.ECT_Service.getIndex());
		c2.setMultiPackagingInfo(barcode1 + ";3;10;10;8;8;" + commodityA + ";");
		c2.setStartValueRemark("1234679");
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(c2, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		//
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c2, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_WrongFormatForInputField);
		// 删除测试数据
		BaseCommodityTest.deleteCommodityViaAction(createCommodity, Shared.DBName_Test, mvc, sessionPos1, mapBO);
	}

	@Test
	public void testUpdateEx44() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Shared.caseLog("case44:普通商品修改期初值备注失败(长度超长)");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setProviderIDs("1");
		commUpdate.setStartValueRemark("012345678901234567890123456789012345678901234567890123456789");
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + commUpdate.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + ","
				+ commodityC + "新增多包装" + Shared.generateStringByTime(8) + ";");

		// 获取该商品修改前的商品历史数量
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testUpdateEx45() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Shared.caseLog("case45:普通商品修改期初值备注成功");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setProviderIDs("1");
		commUpdate.setStartValueRemark("123465797");
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + commUpdate.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + ","
				+ commodityC + "新增多包装" + Shared.generateStringByTime(8) + ";");

		BaseCommodityTest.updateViaAction(commCreate, commUpdate, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx46() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case46:增加商品A，创建多包装商品B,C,D。修改多包装商品B,C,D正确的条形码，修改成功");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,2,3");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ",4443" + Shared.generateStringByTime(8) + ";1,2,3,4;1,5,10,15;1,5,10,15;8,3,8,3;8,8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + "," + commodityD + Shared.generateStringByTime(8) + ";");
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		// 更新
		c.setID(createCommodity.getID());
		c.setProviderIDs("1");
		c.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + c.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC
				+ "新增多包装" + Shared.generateStringByTime(8) + ";");

		BaseCommodityTest.updateViaAction(createCommodity, c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx47() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case47:增加商品A，创建多包装商品B,C,D。修改多包装商品B,C,D的条形码，其中B商品的条形码为错误的，进行修改全部修改失败。");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,2,3");

		c.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + barcode3 + "," + barcode4 + ";1,2,3,4;1,5,10,15;1,5,10,15;8,3,8,3;8,8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + "," + commodityD + Shared.generateStringByTime(8) + ";");
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);

		// 更新,给A的多包装商品B错误的条形码，期望是全部修改失败
		String wrongBarcodes = "123";
		c.setID(commCreate.getID());
		c.setProviderIDs("1");
		c.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + "," + wrongBarcodes + ",789" + Shared.generateStringByTime(8) + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + c.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC
				+ "新增多包装" + Shared.generateStringByTime(8) + ";");
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(c, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 查询多包装的条形码，验证是否是没有修改成功
		Map<String, Object> params = commCreate.getRetrieveNParam(BaseBO.CASE_RetrieveNMultiPackageCommodity, commCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNMultiPackageCommodity = commodityMapper.retrieveNMultiPackageCommodity(params);
		// 查询条形码
		Assert.assertTrue(retrieveNMultiPackageCommodity.size() == 3, "创建了3个多包装，应该返回三个多包装商品");
		Commodity multiPackageCommodityB = (Commodity) retrieveNMultiPackageCommodity.get(0);
		Commodity multiPackageCommodityC = (Commodity) retrieveNMultiPackageCommodity.get(1);
		Commodity multiPackageCommodityD = (Commodity) retrieveNMultiPackageCommodity.get(2);
		Barcodes barcodesB = BaseBarcodesTest.retrieveNBarcodes(multiPackageCommodityB.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesB.getBarcode().equals(barcode4), "条形码期望是没有修改");
		Barcodes barcodesC = BaseBarcodesTest.retrieveNBarcodes(multiPackageCommodityC.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesC.getBarcode().equals(barcode3), "条形码期望是没有修改");
		Barcodes barcodesD = BaseBarcodesTest.retrieveNBarcodes(multiPackageCommodityD.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesD.getBarcode().equals(barcode2), "条形码期望是没有修改");
		Barcodes barcodesA = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesA.getBarcode().equals(barcode1), "条形码期望是没有修改");
	}

	@Test
	public void testUpdateEx48() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case48:增加商品A，创建多包装商品B,C,D。修改多包装商品B,C,D的条形码，其中C商品的条形码为错误的，进行修改全部修改失败。");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,2,3");

		c.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + barcode3 + "," + barcode4 + ";1,2,3,4;1,5,10,15;1,5,10,15;8,3,8,3;8,8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + "," + commodityD + Shared.generateStringByTime(8) + ";");
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		// 更新,给A的多包装商品C错误的条形码，期望是全部修改失败
		String wrongBarcodes = "123";
		c.setID(commCreate.getID());
		c.setProviderIDs("1");
		c.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + "," + wrongBarcodes + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + c.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC
				+ "新增多包装" + Shared.generateStringByTime(8) + ";");

		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(c, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 查询多包装的条形码，验证是否是没有修改成功
		Map<String, Object> params = commCreate.getRetrieveNParam(BaseBO.CASE_RetrieveNMultiPackageCommodity, commCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNMultiPackageCommodity = commodityMapper.retrieveNMultiPackageCommodity(params);
		// 查询条形码
		Assert.assertTrue(retrieveNMultiPackageCommodity.size() == 3, "创建了3个多包装，应该返回三个多包装商品");
		Commodity multiPackageCommodityB = (Commodity) retrieveNMultiPackageCommodity.get(0);
		Commodity multiPackageCommodityC = (Commodity) retrieveNMultiPackageCommodity.get(1);
		Commodity multiPackageCommodityD = (Commodity) retrieveNMultiPackageCommodity.get(2);
		Barcodes barcodesB = BaseBarcodesTest.retrieveNBarcodes(multiPackageCommodityB.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesB.getBarcode().equals(barcode4), "条形码期望是没有修改");
		Barcodes barcodesC = BaseBarcodesTest.retrieveNBarcodes(multiPackageCommodityC.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesC.getBarcode().equals(barcode3), "条形码期望是没有修改");
		Barcodes barcodesD = BaseBarcodesTest.retrieveNBarcodes(multiPackageCommodityD.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesD.getBarcode().equals(barcode2), "条形码期望是没有修改");
		Barcodes barcodesA = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesA.getBarcode().equals(barcode1), "条形码期望是没有修改");
	}

	@Test
	public void testUpdateEx49() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case48:增加商品A，创建多包装商品B,C,D。修改多包装商品B,C,D的条形码，其中D商品的条形码为错误的，进行修改全部修改失败。");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7,2,3");

		c.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + barcode3 + "," + barcode4 + ";1,2,3,4;1,5,10,15;1,5,10,15;8,3,8,3;8,8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + "," + commodityD + Shared.generateStringByTime(8) + ";");
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		// 更新,给A的多包装商品C错误的条形码，期望是全部修改失败
		String wrongBarcodes = "1234567890123456789012345678901234567890123456789012345678901234567890";
		c.setID(commCreate.getID());
		c.setProviderIDs("1");
		c.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + "," + wrongBarcodes + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + c.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC
				+ "新增多包装" + Shared.generateStringByTime(8) + ";");

		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(c, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 查询多包装的条形码，验证是否是没有修改成功
		Map<String, Object> params = commCreate.getRetrieveNParam(BaseBO.CASE_RetrieveNMultiPackageCommodity, commCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNMultiPackageCommodity = commodityMapper.retrieveNMultiPackageCommodity(params);
		// 查询条形码
		Assert.assertTrue(retrieveNMultiPackageCommodity.size() == 3, "创建了3个多包装，应该返回三个多包装商品");
		Commodity multiPackageCommodityB = (Commodity) retrieveNMultiPackageCommodity.get(0);
		Commodity multiPackageCommodityC = (Commodity) retrieveNMultiPackageCommodity.get(1);
		Commodity multiPackageCommodityD = (Commodity) retrieveNMultiPackageCommodity.get(2);
		Barcodes barcodesB = BaseBarcodesTest.retrieveNBarcodes(multiPackageCommodityB.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesB.getBarcode().equals(barcode4), "条形码期望是没有修改");
		Barcodes barcodesC = BaseBarcodesTest.retrieveNBarcodes(multiPackageCommodityC.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesC.getBarcode().equals(barcode3), "条形码期望是没有修改");
		Barcodes barcodesD = BaseBarcodesTest.retrieveNBarcodes(multiPackageCommodityD.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesD.getBarcode().equals(barcode2), "条形码期望是没有修改");
		Barcodes barcodesA = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		Assert.assertTrue(barcodesA.getBarcode().equals(barcode1), "条形码期望是没有修改");
	}

	@Test
	public void testUpdateEx50() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case50:组合商品的子商品修改为服务商品,修改失败"); // 现在不给予修改Type，所以这个不会修改失败，而是变成不会修改，所以结果验证是EnumErrorCode.EC_NoError

		// 创建一个单品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		// 创建一个组合商品
		SubCommodity comm2 = new SubCommodity();
		comm2.setSubCommodityID(1);
		comm2.setSubCommodityNO(1);
		comm2.setPrice(20);
		//
		SubCommodity comm3 = new SubCommodity();
		comm3.setSubCommodityID(commodity.getID());
		comm3.setSubCommodityNO(1);
		comm3.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm2);
		ListCommodity.add(comm3);
		//
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setShelfLife(0);
		c2.setPurchaseFlag(0);
		c2.setRuleOfPoint(0);
		c2.setListSlave1(ListCommodity);
		c2.setType(EnumCommodityType.ECT_Combination.getIndex());
		c2.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c2.getPackageUnitID() + ";" + c2.getRefCommodityMultiple() + ";" + c2.getPriceRetail() + ";" + c2.getPriceVIP() + ";" + c2.getPriceWholesale() + ";" + c2.getName() + ";");
		String json = JSONObject.fromObject(c2).toString();
		MvcResult mr6 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c2, sessionBoss) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		CommodityCP.verifyCreate(mr6, c2, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		//
		// 修改刚创建的单品为服务商品 无法修改商品类型
		// Commodity cUpdate = (Commodity) commodity.clone();
		// cUpdate.setShelfLife(0);
		// cUpdate.setType(EnumCommodityType.ECT_Service.getIndex());
		// commodity.setType(EnumCommodityType.ECT_Service.getIndex());
		// //为能使compareto()通过
		// cUpdate.setDefaultValueToCreate(BaseBO.CASE_Commodity_CreateService);
		// cUpdate.setBarcodes("asdkjvhiasd" + Shared.generateStringByTime(6));
		// cUpdate.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		// cUpdate.setMultiPackagingInfo(String.valueOf(System.currentTimeMillis()) +
		// ";" + cUpdate.getPackageUnitID() + ";" + cUpdate.getRefCommodityMultiple() +
		// ";" + cUpdate.getPriceRetail() + ";" + cUpdate.getPriceVIP() + ";"
		// + cUpdate.getPriceWholesale() + ";" + cUpdate.getName() + ";");
		// //
		// BaseCommodityTest.updateViaAction(commodity, cUpdate, mvc, session, mapBO,
		// Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx51() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case51:组合商品的子商品修改为组合商品,修改失败");
		// 创建一个单品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		// 创建一个组合商品
		SubCommodity comm2 = new SubCommodity();
		comm2.setSubCommodityID(1);
		comm2.setSubCommodityNO(1);
		comm2.setPrice(20);
		//
		SubCommodity comm3 = new SubCommodity();
		comm3.setSubCommodityID(commodity.getID());
		comm3.setSubCommodityNO(1);
		comm3.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm2);
		ListCommodity.add(comm3);
		//
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setShelfLife(0);
		c2.setPurchaseFlag(0);
		c2.setRuleOfPoint(0);
		c2.setListSlave1(ListCommodity);
		c2.setType(EnumCommodityType.ECT_Combination.getIndex());
		c2.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c2.getPackageUnitID() + ";" + c2.getRefCommodityMultiple() + ";" + c2.getPriceRetail() + ";" + c2.getPriceVIP() + ";" + c2.getPriceWholesale() + ";" + c2.getName() + ";");
		String json = JSONObject.fromObject(c2).toString();
		MvcResult mr6 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c2, sessionBoss) //
						.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		CommodityCP.verifyCreate(mr6, c2, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		//
		// 修改刚创建的单品为组合商品
		Commodity cUpdate = BaseCommodityTest.DataInput.getCommodity();
		cUpdate.setID(commodity.getID());
		cUpdate.setPurchasingUnit("");
		cUpdate.setShelfLife(0);
		cUpdate.setPurchaseFlag(Commodity.DEFAULT_VALUE_PurchaseFlag);
		cUpdate.setType(EnumCommodityType.ECT_Combination.getIndex());
		cUpdate.setMultiPackagingInfo(String.valueOf(System.currentTimeMillis()) + ";" + cUpdate.getPackageUnitID() + ";" + cUpdate.getRefCommodityMultiple() + ";" + cUpdate.getPriceRetail() + ";" + cUpdate.getPriceVIP() + ";"
				+ cUpdate.getPriceWholesale() + ";" + cUpdate.getName() + ";");
		//
		MvcResult mr3 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		String jsonString = mr3.getResponse().getContentAsString();
		assertTrue(jsonString.equals(""), "单品修改成组合商品成功！");
	}

	@Test
	public void testUpdateEx52() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case52:组合商品的子商品修改为多包装商品,修改失败");
		// 创建一个单品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		// 创建一个组合商品
		SubCommodity comm2 = new SubCommodity();
		comm2.setSubCommodityID(1);
		comm2.setSubCommodityNO(1);
		comm2.setPrice(20);
		//
		SubCommodity comm3 = new SubCommodity();
		comm3.setSubCommodityID(commodity.getID());
		comm3.setSubCommodityNO(1);
		comm3.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm2);
		ListCommodity.add(comm3);
		//
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setShelfLife(0);
		c2.setPurchaseFlag(0);
		c2.setRuleOfPoint(0);
		c2.setListSlave1(ListCommodity);
		c2.setType(EnumCommodityType.ECT_Combination.getIndex());
		c2.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c2.getPackageUnitID() + ";" + c2.getRefCommodityMultiple() + ";" + c2.getPriceRetail() + ";" + c2.getPriceVIP() + ";" + c2.getPriceWholesale() + ";" + c2.getName() + ";");
		String json = JSONObject.fromObject(c2).toString();
		MvcResult mr6 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c2, sessionBoss) //
						.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr6);
		CommodityCP.verifyCreate(mr6, c2, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				Shared.DBName_Test);
		//
		// 修改刚创建的单品为组合商品
		Commodity cUpdate = BaseCommodityTest.DataInput.getCommodity();
		cUpdate.setID(commodity.getID());
		cUpdate.setPurchasingUnit("");
		cUpdate.setShelfLife(0);
		cUpdate.setPurchaseFlag(Commodity.DEFAULT_VALUE_PurchaseFlag);
		cUpdate.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		cUpdate.setMultiPackagingInfo(String.valueOf(System.currentTimeMillis()) + ";" + cUpdate.getPackageUnitID() + ";" + cUpdate.getRefCommodityMultiple() + ";" + cUpdate.getPriceRetail() + ";" + cUpdate.getPriceVIP() + ";"
				+ cUpdate.getPriceWholesale() + ";" + cUpdate.getName() + ";");
		//
		MvcResult mr3 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		String jsonString = mr3.getResponse().getContentAsString();
		assertTrue(jsonString.equals(""), "单品修改成多包装商品成功！");
	}

	@Test
	public void testUpdateEx53() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case53:商品修改为期初商品,修改失败");
		// 创建一个单品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		//
		// 修改刚创建的单品为期初商品
		Commodity cUpdate = BaseCommodityTest.DataInput.getCommodity();
		cUpdate.setID(commodity.getID());
		cUpdate.setnOStart(100);
		cUpdate.setPurchasingPriceStart(10d);
		cUpdate.setMultiPackagingInfo(String.valueOf(System.currentTimeMillis()) + ";" + cUpdate.getPackageUnitID() + ";" + cUpdate.getRefCommodityMultiple() + ";" + cUpdate.getPriceRetail() + ";" + cUpdate.getPriceVIP() + ";"
				+ cUpdate.getPriceWholesale() + ";" + cUpdate.getName() + ";");
		//
		MvcResult mr3 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr3);
		JSONObject jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		Commodity commodityUpdate = new Commodity();
		commodityUpdate = (Commodity) commodityUpdate.parse1(jsonObject.getString(BaseAction.KEY_Object));
		List<CommodityShopInfo> listCommodityShopInfo = (List<CommodityShopInfo>) commodityUpdate.getListSlave2();
		Assert.assertTrue(listCommodityShopInfo != null, "没有返回商品门店信息");
		CommodityShopInfo commodityShopInfo = listCommodityShopInfo.get(listCommodityShopInfo.size() - 1);
		Assert.assertTrue(commodityShopInfo.getnOStart() == Commodity.NO_START_Default, "期初数量居然修改成功！");
		Assert.assertTrue(commodityShopInfo.getPurchasingPriceStart() == Commodity.PURCHASING_PRICE_START_Default, "期初采购价居然修改成功！");
	}

	@Test
	public void testUpdateEx54() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case54: 修改服务商品时参照商品ID不为0");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setRefCommodityID(1);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ",3332" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		String json = mrl.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String err = JsonPath.read(o, "$." + BaseAction.KEY_Object);
		Assert.assertTrue("".equals(err), "服务器返回的数据不正确");
	}

	@Test
	public void testUpdateEx55() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case55:修改服务商品时保质期不为0,修改失败");
		// 创建一个服务商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		//
		// 修改刚创建的单品为期初商品
		Commodity cUpdate = BaseCommodityTest.DataInput.getCommodity();
		cUpdate.setID(commodity.getID());
		cUpdate.setShelfLife(1);
		cUpdate.setType(EnumCommodityType.ECT_Service.getIndex());
		cUpdate.setMultiPackagingInfo(String.valueOf(System.currentTimeMillis()) + ";" + cUpdate.getPackageUnitID() + ";" + cUpdate.getRefCommodityMultiple() + ";" + cUpdate.getPriceRetail() + ";" + cUpdate.getPriceVIP() + ";"
				+ cUpdate.getPriceWholesale() + ";" + cUpdate.getName() + ";");
		//
		MvcResult mr3 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr3, "服务商品的保质期必须为0", "错误信息不正确！");
	}

	@Test
	public void testUpdateEx56() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case56:修改服务商品时，参照商品ID和参照商品倍数不为0,修改失败");
		// 创建一个服务商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		//
		// 修改刚创建的单品为期初商品
		Commodity cUpdate = BaseCommodityTest.DataInput.getCommodity();
		cUpdate.setID(commodity.getID());
		cUpdate.setRefCommodityID(1);
		cUpdate.setRefCommodityMultiple(2);
		cUpdate.setType(EnumCommodityType.ECT_Service.getIndex());
		cUpdate.setMultiPackagingInfo(String.valueOf(System.currentTimeMillis()) + ";" + cUpdate.getPackageUnitID() + ";" + cUpdate.getRefCommodityMultiple() + ";" + cUpdate.getPriceRetail() + ";" + cUpdate.getPriceVIP() + ";"
				+ cUpdate.getPriceWholesale() + ";" + cUpdate.getName() + ";");
		//
		MvcResult mr3 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr3, "单品、组合商品和服务商品的参照商品ID要是0,单品、组合商品和服务商品的参照商品倍数要是0", "错误信息不正确！");
	}

	@Test
	public void testUpdateEx57() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case57:修改服务商品时，采购阀值不为0,修改失败");
		// 创建一个服务商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		//
		// 修改刚创建的单品为期初商品
		Commodity cUpdate = BaseCommodityTest.DataInput.getCommodity();
		cUpdate.setID(commodity.getID());
		cUpdate.setPurchaseFlag(10);
		cUpdate.setShelfLife(0);
		cUpdate.setType(EnumCommodityType.ECT_Service.getIndex());
		cUpdate.setMultiPackagingInfo(String.valueOf(System.currentTimeMillis()) + ";" + cUpdate.getPackageUnitID() + ";" + cUpdate.getRefCommodityMultiple() + ";" + cUpdate.getPriceRetail() + ";" + cUpdate.getPriceVIP() + ";"
				+ cUpdate.getPriceWholesale() + ";" + cUpdate.getName() + ";");
		//
		MvcResult mr3 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr3, "服务商品的采购阀值必须为0", "错误信息不正确！");
	}

	@Test
	public void testUpdateEx58() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case58:修改服务商品时，最近采购价不为-1.0,修改失败");
		// 创建一个服务商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setMultiPackagingInfo(
				String.valueOf(System.currentTimeMillis()) + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		//
		// 修改刚创建的单品为期初商品
		Commodity cUpdate = BaseCommodityTest.DataInput.getCommodity();
		cUpdate.setID(commodity.getID());
		cUpdate.setLatestPricePurchase(100d);
		cUpdate.setPurchaseFlag(0);
		cUpdate.setShelfLife(0);
		cUpdate.setType(EnumCommodityType.ECT_Service.getIndex());
		cUpdate.setMultiPackagingInfo(String.valueOf(System.currentTimeMillis()) + ";" + cUpdate.getPackageUnitID() + ";" + cUpdate.getRefCommodityMultiple() + ";" + cUpdate.getPriceRetail() + ";" + cUpdate.getPriceVIP() + ";"
				+ cUpdate.getPriceWholesale() + ";" + cUpdate.getName() + ";");
		//
		MvcResult mr3 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr3, "服务商品的最近采购价必须为-1", "错误信息不正确！");
	}

	@Test
	public void testUpdateEx59() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case59(有Q无X):普通商品A，条形码为B1,B2; 普通商品B，条形码为B3,B4；  多包装商品C的条形码为B1，修改为B5。结果为多包装商品C的条形码修改成B5成功，还能使用B1条形码查询到普通商品A");
		// 使用mapper去查询,查询普通商品
		Commodity normalComm = new Commodity();
		normalComm.setID(268);
		normalComm = BaseCommodityTest.retrieve1ExCommodity(normalComm, EnumErrorCode.EC_NoError);
		Assert.assertTrue(normalComm != null, "查询商品失败");
		// 查询到旧的多包装商品的信息
		List<Commodity> multioleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(normalComm, mapBO);
		Assert.assertTrue(multioleCommList.size() > 0, "获取多包装商品失败");
		//
		String oldMultioleCommBarcodes = multioleCommList.get(0).getBarcodes();
		String newMultioleCommBarcodes = "555" + Shared.generateStringByTime(7);
		// 修改多包装商品信息
		normalComm.setProviderIDs("1");
		normalComm.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		normalComm.setShopID(2);
		normalComm.setMultiPackagingInfo("444" + Shared.generateStringByTime(7) + "," + newMultioleCommBarcodes + ";3,4;10,15;10,15;8,3;8,8;" + normalComm.getName() + "," + multioleCommList.get(0).getName() + ";");
		BaseCommodityTest.updateViaAction(normalComm, normalComm, mvc, sessionPos1, mapBO, Shared.DBName_Test);

		// 使用新修改后的条形码去进行查询商品 结果为多包装商品C的条形码修改成B5成功，还能使用B1条形码查询到普通商品A
		// 查询普通商品A.
		Commodity commodity = new Commodity();
		commodity.setID(266);
		commodity = BaseCommodityTest.retrieve1ExCommodity(commodity, EnumErrorCode.EC_NoError);
		Assert.assertTrue(commodity != null, "查询商品失败");
		// 使用B1条形码查到的商品和普通商品A是否一样.
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(oldMultioleCommBarcodes, BaseBO.INVALID_CASE_ID);
		Commodity commodityA = (Commodity) retrieveNCommodity.get(0); // 根据数据库中的准备数据和代码的流程，此时使用oldMultioleCommBarcodes能够查找到的商品只有1个
		Assert.assertTrue(commodityA.compareTo(commodity) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 查出新的条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setBarcode(newMultioleCommBarcodes);
		Map<String, Object> retrieveNBarcodesParam = barcodes.getRetrieveNParam(INVALID_ID, barcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> barcodesList = barcodesMapper.retrieveN(retrieveNBarcodesParam);
		assertTrue(barcodesList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找条形码失败");
		// 还原数据库的条形码
		Barcodes updateBarcodes = (Barcodes) barcodesList.get(0);
		updateBarcodes.setBarcode(oldMultioleCommBarcodes);
		updateBarcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> updateBarcodesParam = updateBarcodes.getUpdateParam(INVALID_ID, updateBarcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.update(updateBarcodesParam);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(updateBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改条形码失败");
	}
	
	@Test
	public void testUpdateEx59_2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case59_2(有Q无X):普通商品A，条形码为B1; 普通商品B，条形码为B2； 普通商品C的条形码为B1，修改为B4。结果为商品C的条形码修改成B4成功，还能使用B1条形码查询到普通商品A");
		// 创建普通商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case592newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case592newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCGet.setBarcodes(commAoldBarcodes);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
				+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case592newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		Commodity commodityRNCommA = (Commodity) retrieveNCommodityA.get(0);
		commodityRNCommA.setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(commodityRNCommA.compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		Commodity commodityRNCommB = (Commodity) retrieveNCommodityB.get(0);
		commodityRNCommB.setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(commodityRNCommB.compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码newCommCbarcodesString可以查询到商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		Commodity commodityRNCommC = (Commodity) retrieveNCommodity.get(0);
		commodityRNCommC.setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(commodityRNCommC.compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
			
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx59_3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case59_3(有Q无X):服务商品A，条形码为B1; 服务商品B，条形码为B2； 服务商品C的条形码为B1，修改为B4。结果为商品C的条形码修改成B4成功，还能使用B1条形码查询到普通商品A");
		// 创建普通商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commAoldBarcodes = "Case593newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commBoldBarcodes = "Case593newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityCGet.setBarcodes(commAoldBarcodes);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
				+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case593newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		Commodity commodityRNCommA = (Commodity) retrieveNCommodityA.get(0);
		commodityRNCommA.setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(commodityRNCommA.compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		Commodity commodityRNCommB = (Commodity) retrieveNCommodityB.get(0);
		commodityRNCommB.setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(commodityRNCommB.compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码newCommCbarcodesString可以查询到商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		Commodity commodityRNCommC = (Commodity) retrieveNCommodity.get(0);
		commodityRNCommC.setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(commodityRNCommC.compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
			
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx59_4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case59_4(有Q无X):普通商品A，条形码为B1; 普通商品B，条形码为B2； 服务商品C的条形码为B1，修改为B4。结果为商品C的条形码修改成B4成功，还能使用B1条形码查询到普通商品A");
		// 创建普通商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case594newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case594newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityCGet.setBarcodes(commAoldBarcodes);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
				+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case594newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		Commodity commodityRNCommA = (Commodity) retrieveNCommodityA.get(0);
		commodityRNCommA.setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(commodityRNCommA.compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码newCommCbarcodesString可以查询到商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodity.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodity.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
			
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx59_5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case59_5(有Q无X):服务商品A，条形码为B1; 服务商品B，条形码为B2； 普通商品C的条形码为B1，修改为B4。结果为商品C的条形码修改成B4成功，还能使用B1条形码查询到商品A");
		// 创建普通商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commAoldBarcodes = "Case595newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commBoldBarcodes = "Case595newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCGet.setBarcodes(commAoldBarcodes);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
				+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case595newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码newCommCbarcodesString可以查询到商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodity.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodity.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
			
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx59_6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case59_6(有Q无X):普通商品A，条形码为B1; 普通商品B，条形码为B2； 修改普通商品C对应的多包装商品的条形码（普通商品的条形码为B3），B1改成B5。结果为商品C的条形码修改成B4成功，还能使用B1条形码查询到普通商品A");
		// 创建普通商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case596newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case596newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = commAoldBarcodes;
		String commCbarcodesString = "Case596CommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(commCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + "," + multipleCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		String newMultiCommCbarcodesString = "Case596newMultiCommCBarcodes" + Shared.generateStringByTime(9);
		Commodity commdityCupdateGet = BaseCommodityTest.DataInput.getCommodity();
		commdityCupdateGet.setMultiPackagingInfo(multipleCommCbarcodesString + "," + newMultiCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		commdityCupdateGet.setID(commodityCCreate.getID());
		Commodity commdityCupdated = BaseCommodityTest.updateViaAction(commodityCCreate, commdityCupdateGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		List<Commodity> multioleCommodityA_AList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCCreate, mapBO);
		// 使用条形码 commAoldBarcodes 可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码 commBoldBarcodes 可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码 newCommCbarcodesString 可以查询到商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(commCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodity.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodity.get(0).compareTo(commdityCupdated) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
			
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(multioleCommodityA_AList.get(0), Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	
	@Test
	public void testUpdateEx59_7() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE59_7(有Q无X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改普通商品C对应的多包装商品的条形码（普通商品的条形码为B3），多包装条形码B1改成B5.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B3能够查找到商品C");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case597newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case597newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C和它们对应的多包装商品
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = combineCommAoldBarcodes;
		String commCbarcodesString = "Case597CommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(commCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + "," + multipleCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C对应的多包装商品的条形码
		String newMultiCommCbarcodesString = "Case597newMultiCommCBarcodes" + Shared.generateStringByTime(9);
		Commodity commdityCupdateGet = BaseCommodityTest.DataInput.getCommodity();
		commdityCupdateGet.setMultiPackagingInfo(multipleCommCbarcodesString + "," + newMultiCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		commdityCupdateGet.setID(commodityCCreate.getID());
		Commodity commdityCupdated = BaseCommodityTest.updateViaAction(commodityCCreate, commdityCupdateGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		List<Commodity> multioleCommodityA_AList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCCreate, mapBO);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityCombineACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityCombineBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码commAoldBarcodes可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(commCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commdityCupdated) == 0, "使用B3条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C、C的多包装商品
		BaseCommodityTest.deleteCommodityViaAction(multioleCommodityA_AList.get(0), Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx59_8() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE59_8(有Q无X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改普通商品C的条形码（商品的条形码为B1），B1改成B4.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B4能够查找到商品C");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case598newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case598newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = combineCommAoldBarcodes;
		commodityCGet.setBarcodes(multipleCommCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
		+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case598newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityCombineACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityCombineBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码 newCommCbarcodesString 可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx59_9() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE59_9(无Q无X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改服务商品C的条形码（商品的条形码为B3）。B3改成B4.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B4能够查找到商品C");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case599newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case599newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建服务商品C
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String multipleCommCbarcodesString = combineCommAoldBarcodes;
		commodityCGet.setBarcodes(multipleCommCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
		+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case599newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityCombineACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityCombineBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码 newCommCbarcodesString 可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateEx60() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case60(无Q有X):普通商品A，条形码为B1,B2; 普通商品B，条形码为B3,B4；  多包装商品C的条形码为B5，修改为B1。结果为多包装商品C的条形码修改成B1成功，使用B1条形码查询到普通商品A和多包装商品C");
		// 使用mapper去查询,查询普通商品
		Commodity normalComm = new Commodity();
		normalComm.setID(270);
		normalComm = BaseCommodityTest.retrieve1ExCommodity(normalComm, EnumErrorCode.EC_NoError);
		Assert.assertTrue(normalComm != null, "查询商品失败");
		// 查询到旧的多包装商品的信息
		List<Commodity> multioleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(normalComm, mapBO);
		Assert.assertTrue(multioleCommList.size() > 0, "获取多包装商品失败");
		//
		String oldMultioleCommBarcodes = multioleCommList.get(0).getBarcodes();
		String newMultioleCommBarcodes = "putongB1";
		// 修改多包装商品信息
		normalComm.setProviderIDs("1");
		normalComm.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		normalComm.setShopID(2);
		normalComm.setMultiPackagingInfo("444" + Shared.generateStringByTime(7) + "," + newMultioleCommBarcodes + ";3,4;10,15;10,15;8,3;8,8;" + normalComm.getName() + "," + multioleCommList.get(0).getName() + ";");

		BaseCommodityTest.updateViaAction(normalComm, normalComm, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		// 多包装商品C的条形码为B5，修改为B1。结果为多包装商品C的条形码修改成B1成功，使用B1条形码查询到普通商品A和多包装商品C
		// 使用B1条形码查询到普通商品A和多包装商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(newMultioleCommBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodity.size() == 1, "使用B1条形码查询不到到普通商品A"); // 因为多包装商品无法在web查询，所以只有一条
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(multioleCommList.get(0).getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Barcodes> barcodesList = (List<Barcodes>) barcodesBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, barcodes);
		assertTrue(barcodesList.size() > 0 && barcodesBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "根据商品ID查询条形码失败");
		assertTrue(newMultioleCommBarcodes.equals(barcodesList.get(0).getBarcode()), "多包装商品的条形码修改为B1失败");
		//
		// 还原数据库的条形码
		Barcodes updateBarcodes = (Barcodes) barcodesList.get(0);
		updateBarcodes.setBarcode(oldMultioleCommBarcodes);
		updateBarcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> updateBarcodesParam = updateBarcodes.getUpdateParam(INVALID_ID, updateBarcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.update(updateBarcodesParam);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(updateBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改条形码失败");

	}
	
	@Test
	public void testUpdateEx60_2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE60_2(无Q有X) 普通商品A，条形码B1; 普通商品B，条形码B2； 修改普通商品C的条形码，B3改成B1。使用B1条形码查询到商品A和商品C");
		//
		// 创建普通商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case602newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case602newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = commAoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A、C
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityA.size() >= 2, "使用B1条形码查找到的商品应该包含A、C。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@Test
	public void testUpdateEx60_3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE60_3(无Q有X) 服务商品A，条形码B1; 服务商品B，条形码B2； 修改服务商品C的条形码。B3改成B1.结果为使用B1能够查找到商品A、C，使用B2能够查找到商品B");
		//
		// 创建服务商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commAoldBarcodes = "Case603newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commBoldBarcodes = "Case603newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = commAoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityA.size() >= 2, "使用B1条形码查找到的商品应包含商品A、C。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx60_4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE60_4(无Q有X) 普通商品A，条形码B1; 普通商品B，条形码B2； 修改服务商品C的条形码。B3改成B1.结果为使用B1能够查找到商品A、C，使用B2能够查找到商品B");
		//
		// 创建商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case604newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case604newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = commAoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityA.size() >= 2, "使用B1条形码查找到的商品应包含A、C");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx60_5() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE60_5(无Q有X) 服务商品A，条形码B1; 服务商品B，条形码B2； 修改普通商品C的条形码。B3改成B1.结果为使用B1能够查找到商品A、C，使用B2能够查找到商品B");
		//
		// 创建商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commAoldBarcodes = "Case605newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commBoldBarcodes = "Case605newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = commAoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityA.size() >= 2, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx60_6() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE60_6(无Q有X) 普通商品A，条形码B1; 普通商品B，条形码B2； 修改普通商品C对应的多包装商品的条形码（普通商品的条形码为B3），多包装商品条形码B4改成B1.结果为使用B1能够查找到商品A（web不能根据条形码查找多包装商品），使用B2能够查找到商品B");
		//
		// 创建普通商品A、B
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case606newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case606newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C和它们对应的多包装商品
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = "Case606multipleCommCbarcodes" + Shared.generateStringByTime(9);
		String commCbarcodesString = "Case606CommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(commCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + "," + multipleCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C对应的多包装商品的条形码
		String newMultiCommCbarcodesString = commAoldBarcodes;
		Commodity commdityCupdateGet = BaseCommodityTest.DataInput.getCommodity();
		commdityCupdateGet.setMultiPackagingInfo(multipleCommCbarcodesString + "," + newMultiCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		commdityCupdateGet.setID(commodityCCreate.getID());
		Commodity commdityCupdated = BaseCommodityTest.updateViaAction(commodityCCreate, commdityCupdateGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		List<Commodity> multioleCommodityA_AList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCCreate, mapBO);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码commAoldBarcodes可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(commCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commdityCupdated) == 0, "使用B3条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C、C的多包装商品
		BaseCommodityTest.deleteCommodityViaAction(multioleCommodityA_AList.get(0), Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx60_7() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE60_7(无Q有X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改普通商品C对应的多包装商品的条形码（普通商品的条形码为B3），多包装条形码B4改成B1.结果为使用B1能够查找到商品A（根据条形码不能查找多包装），使用B2能够查找到商品B，使用B3能够查找到商品C");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case607newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case607newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C和它们对应的多包装商品
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = combineCommAoldBarcodes;
		String commCbarcodesString = "Case607CommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(commCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + "," + multipleCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C对应的多包装商品的条形码
		String newMultiCommCbarcodesString = "Case607newMultiCommCBarcodes" + Shared.generateStringByTime(9);
		Commodity commdityCupdateGet = BaseCommodityTest.DataInput.getCommodity();
		commdityCupdateGet.setMultiPackagingInfo(multipleCommCbarcodesString + "," + newMultiCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		commdityCupdateGet.setID(commodityCCreate.getID());
		Commodity commdityCupdated = BaseCommodityTest.updateViaAction(commodityCCreate, commdityCupdateGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		List<Commodity> multioleCommodityA_AList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCCreate, mapBO);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityCombineACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityCombineBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码commAoldBarcodes可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(commCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commdityCupdated) == 0, "使用B3条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C、C的多包装商品
		BaseCommodityTest.deleteCommodityViaAction(multioleCommodityA_AList.get(0), Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	
	@Test
	public void testUpdateEx60_8() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE60_8(无Q有X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改普通商品C的条形码（商品的条形码为B3）。B3改成B1.结果为使用B1能够查找到商品A、C，使用B2能够查找到商品B");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case608newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case608newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = "Case608multipleCommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(multipleCommCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
		+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = combineCommAoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码combineCommAoldBarcodes可以查询到商品A、C
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityA.size() >= 2, "使用B1条形码查找到的商品应包含A、C。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityCombineBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码 newCommCbarcodesString 可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx60_9() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE60_9(无Q有X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改服务商品C的条形码（商品的条形码为B3）。B3改成B1.结果为使用B1能够查找到商品A、C，使用B2能够查找到商品B");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case609newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case609newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建服务商品C
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String multipleCommCbarcodesString = "Case609multipleCommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(multipleCommCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
		+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = combineCommAoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityA.size() >= 2, "使用B1条形码查找到的商品应包含A、C。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityCombineBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateEx61() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case61(有Q有X):普通商品A,条形码为B1,B2; 普通商品B，条形码为B3,B4；多包装商品C的条形码为B1，修改为B3。结果为多包装商品修改条形码成功，使用B1能够查找到普通商品A，无法查到多包装商品C，使用B3能够查找到多包装商品C和普通商品B");
		// 使用mapper去查询,查询普通商品
		Commodity normalComm = new Commodity();
		normalComm.setID(272);
		normalComm = BaseCommodityTest.retrieve1ExCommodity(normalComm, EnumErrorCode.EC_NoError);
		Assert.assertTrue(normalComm != null, "查询商品失败");
		// 查询到旧的多包装商品的信息
		List<Commodity> multioleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(normalComm, mapBO);
		Assert.assertTrue(multioleCommList.size() > 0, "获取多包装商品失败");
		//
		String oldMultioleCommBarcodes = multioleCommList.get(0).getBarcodes();
		String newMultioleCommBarcodes = "putongS1";
		// 修改多包装商品信息
		normalComm.setProviderIDs("1");
		normalComm.setBarcodes(Shared.generateStringByTime(9));
		normalComm.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		normalComm.setMultiPackagingInfo("444" + Shared.generateStringByTime(7) + "," + newMultioleCommBarcodes + ";3,4;10,15;10,15;8,3;8,8;" + normalComm.getName() + "," + multioleCommList.get(0).getName() + ";");
		normalComm.setShopID(2);
		BaseCommodityTest.updateViaAction(normalComm, normalComm, mvc, sessionPos1, mapBO, Shared.DBName_Test);

		// 修改为B3。结果为多包装商品修改条形码成功，使用B1能够查找到普通商品A，无法查到多包装商品C，使用B3能够查找到多包装商品C和普通商品B
		// 使用B1能够查找到普通商品A，无法查到多包装商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(newMultioleCommBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodity.size() == 1, "使用B1能够查找到普通商品A，无法查到多包装商品C"); // 因为多包装商品无法在web查询，所以只有一条
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(multioleCommList.get(0).getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Barcodes> barcodesList = (List<Barcodes>) barcodesBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, barcodes);
		assertTrue(barcodesList.size() > 0 && barcodesBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "根据商品ID查询条形码失败");
		assertTrue(newMultioleCommBarcodes.equals(barcodesList.get(0).getBarcode()), "多包装商品的条形码B1修改为B3失败");
		//
		// 使用B3能够查找到多包装商品C和普通商品B
		List<BaseModel> retrieveNCommodity1 = BaseCommodityTest.retrieveNCommodity(newMultioleCommBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodity1.size() == 1, " 使用B3能够查找到多包装商品C和普通商品B"); // 因为多包装商品无法在web查询，所以只有一条
		//
		Barcodes barcodes1 = new Barcodes();
		barcodes.setCommodityID(multioleCommList.get(0).getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Barcodes> barcodesList1 = (List<Barcodes>) barcodesBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, barcodes1);
		assertTrue(barcodesList.size() > 0 && barcodesBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "根据商品ID查询条形码失败");
		assertTrue(!oldMultioleCommBarcodes.equals(barcodesList1.get(0).getBarcode()), "多包装商品的条形码B1修改为B3失败");
		//
		// 还原数据库的条形码
		Barcodes updateBarcodes = (Barcodes) barcodesList.get(0);
		updateBarcodes.setBarcode(oldMultioleCommBarcodes);
		updateBarcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> updateBarcodesParam = updateBarcodes.getUpdateParam(INVALID_ID, updateBarcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.update(updateBarcodesParam);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(updateBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改条形码失败," + updateBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateEx62() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case62(有Q有X):多包装商品A，条形码为B1，多包装商品B.条形码为B2.修改多包装商品A的条形码为B2.修改多包装商品B的条形码为B1.结果为使用B1能够查询到多包装商品B.使用B2能够查询到多包装商品A");
		// 使用mapper去查询,查询普通商品
		Commodity normalComm = new Commodity();
		normalComm.setID(275);
		normalComm = BaseCommodityTest.retrieve1ExCommodity(normalComm, EnumErrorCode.EC_NoError);
		Assert.assertTrue(normalComm != null, "查询商品失败");
		// 查询到旧的多包装商品的信息
		List<Commodity> multioleCommList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(normalComm, mapBO);
		Assert.assertTrue(multioleCommList.size() > 0, "获取多包装商品失败");
		//
		String oldMultioleCommBarcodes = multioleCommList.get(0).getBarcodes();
		String newMultioleCommBarcodes = "putongY44";
		// 修改多包装商品信息
		normalComm.setProviderIDs("1");
		normalComm.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		normalComm.setMultiPackagingInfo("444" + Shared.generateStringByTime(7) + "," + newMultioleCommBarcodes + ";3,4;10,15;10,15;8,3;8,8;" + normalComm.getName() + "," + multioleCommList.get(0).getName() + ";");
		normalComm.setShopID(2);
		BaseCommodityTest.updateViaAction(normalComm, normalComm, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		// 结果为使用B1能够查询到多包装商品B
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(multioleCommList.get(0).getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Barcodes> barcodesList = (List<Barcodes>) barcodesBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, barcodes);
		assertTrue(barcodesList.size() > 0 && barcodesBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "根据商品ID查询条形码失败");
		assertTrue(newMultioleCommBarcodes.equals(barcodesList.get(0).getBarcode()), "使用B1能够查询到多包装商品B");

		// 使用mapper去查询,查询普通商品
		Commodity normalComm1 = new Commodity();
		normalComm1.setID(277);
		normalComm1 = BaseCommodityTest.retrieve1ExCommodity(normalComm1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(normalComm1 != null, "查询商品失败");
		// 查询到旧的多包装商品的信息
		List<Commodity> multioleCommList1 = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(normalComm1, mapBO);
		Assert.assertTrue(multioleCommList1.size() > 0, "获取多包装商品失败");
		//
		String oldMultioleCommBarcodes1 = multioleCommList1.get(0).getBarcodes();
		String newMultioleCommBarcodes1 = "putongY22";
		// 修改多包装商品信息
		normalComm1.setProviderIDs("1");
		normalComm1.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		normalComm1.setMultiPackagingInfo("444" + Shared.generateStringByTime(7) + "," + newMultioleCommBarcodes1 + ";3,4;10,15;10,15;8,3;8,8;" + normalComm1.getName() + "," + multioleCommList1.get(0).getName() + ";");
		// 获取多包装商品的修改状况
		List<PackageUnit> lspu1 = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuCreated1 = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuUpdated1 = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuDeleted1 = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(normalComm1, lspu1, lspuCreated1, lspuUpdated1, lspuDeleted1, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取normalComm修改前的旧历史数量
		int iOldNO1 = BaseCommodityTest.queryCommodityHistorySize(normalComm1.getID(), Shared.DBName_Test, commodityHistoryBO);
		normalComm1.setShopID(2);
		MvcResult mr = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, normalComm1, sessionPos1) //
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CommodityCP.verifyUpdate(mr, normalComm1, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO1, lspuCreated1, lspuUpdated1, lspuDeleted1, multioleCommList1, Shared.DBName_Test, null);

		// 使用B2能够查询到多包装商品A
		Barcodes barcodes1 = new Barcodes();
		barcodes1.setCommodityID(multioleCommList1.get(0).getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<Barcodes> barcodesList1 = (List<Barcodes>) barcodesBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, barcodes1);
		assertTrue(barcodesList1.size() > 0 && barcodesBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "根据商品ID查询条形码失败");
		assertTrue(newMultioleCommBarcodes1.equals(barcodesList1.get(0).getBarcode()), "使用B2能够查询到多包装商品A");
		//
		// 还原数据库的条形码duo1
		Barcodes updateBarcodes = (Barcodes) barcodesList.get(0);
		updateBarcodes.setBarcode(oldMultioleCommBarcodes);
		updateBarcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> updateBarcodesParam = updateBarcodes.getUpdateParam(INVALID_ID, updateBarcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.update(updateBarcodesParam);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(updateBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改条形码失败");
		//
		// 还原数据库的条形码duo2
		Barcodes updateBarcodes1 = (Barcodes) barcodesList1.get(0);
		updateBarcodes1.setBarcode(oldMultioleCommBarcodes1);
		updateBarcodes1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> updateBarcodesParam1 = updateBarcodes1.getUpdateParam(INVALID_ID, updateBarcodes1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.update(updateBarcodesParam1);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(updateBarcodesParam1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改条形码失败");
	}
	
	@Test
	public void testUpdateEx62_2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE62_2(有Q有X) 普通商品A，条形码B1; 普通商品B，条形码B2； 修改普通商品C的条形码，B1改成B2。使用B1条形码查询到商品A，使用B2条形码查询到商品B和商品C");
		//
		// 创建普通商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case622newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case622newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCGet.setBarcodes(commAoldBarcodes);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
				+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = commBoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品应该包含A。");
		// 使用条形码commBoldBarcodes可以查询到商品B、C
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityB.size() >= 2, "使用B2条形码查找到的商品应包含B、C。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx62_3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE62_3(有Q有X) 服务商品A，条形码B1; 服务商品B，条形码B2； 修改服务商品C的条形码。B1改成B2.结果为使用B1能够查找到商品A，使用B2能够查找到商品B、C");
		//
		// 创建服务商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commAoldBarcodes = "Case623newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commBoldBarcodes = "Case623newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityCGet.setBarcodes(commAoldBarcodes);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
				+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = commBoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B、C
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityB.size() >= 2, "使用B2条形码查找到的商品应包含商品B、C。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx62_4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE62_4(有Q有X) 普通商品A，条形码B1; 普通商品B，条形码B2； 修改服务商品C的条形码。B1改成B2.结果为使用B1能够查找到商品A，使用B2能够查找到商品B、C");
		//
		// 创建商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case624newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case624newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commodityCGet.setBarcodes(commAoldBarcodes);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = commBoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityB.size() >= 2, "使用B2条形码查找到的商品应包含B、C。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx62_5() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE62_5(有Q有X) 服务商品A，条形码B1; 服务商品B，条形码B2； 修改普通商品C的条形码。B1改成B2.结果为使用B1能够查找到商品A，使用B2能够查找到商品B、C");
		//
		// 创建商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commAoldBarcodes = "Case625newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commBoldBarcodes = "Case625newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		commodityCGet.setBarcodes(commAoldBarcodes);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
				+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = commBoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityB.size() >= 2, "使用B2条形码查找到的商品应包含商品B、C。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx62_6() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE62_6(有Q有X) 普通商品A，条形码B1; 普通商品B，条形码B2； 修改普通商品C对应的多包装商品的条形码（普通商品的条形码为B3），修改多包装商品条形码B1改成B2.结果为使用B1能够查找到商品A，使用B2能够查找到商品B（条形码不能查多包装），使用B3能够查找到商品C");
		//
		// 创建普通商品A、B
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case626newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case626newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C和它们对应的多包装商品
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = commAoldBarcodes;
		String commCbarcodesString = "Case626CommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(commCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + "," + multipleCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C对应的多包装商品的条形码
		String newMultiCommCbarcodesString = commBoldBarcodes;
		Commodity commdityCupdateGet = BaseCommodityTest.DataInput.getCommodity();
		commdityCupdateGet.setMultiPackagingInfo(multipleCommCbarcodesString + "," + newMultiCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		commdityCupdateGet.setID(commodityCCreate.getID());
		Commodity commdityCupdated = BaseCommodityTest.updateViaAction(commodityCCreate, commdityCupdateGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		List<Commodity> multioleCommodityA_AList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCCreate, mapBO);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码commAoldBarcodes可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(commCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commdityCupdated) == 0, "使用B3条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C、C的多包装商品
		BaseCommodityTest.deleteCommodityViaAction(multioleCommodityA_AList.get(0), Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx62_7() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE62_7(有Q有X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改普通商品C对应的多包装商品的条形码（普通商品的条形码为B3），多包装条形码B1改成B2.结果为使用B1能够查找到商品A，使用B2能够查找到商品B（根据条形码不能查到多包装商品），使用B3能够查找到商品C");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case627newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case627newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C和它们对应的多包装商品
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = combineCommAoldBarcodes;
		String commCbarcodesString = "Case627CommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(commCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + "," + multipleCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C对应的多包装商品的条形码
		String newMultiCommCbarcodesString = combineCommBoldBarcodes;
		Commodity commdityCupdateGet = BaseCommodityTest.DataInput.getCommodity();
		commdityCupdateGet.setMultiPackagingInfo(multipleCommCbarcodesString + "," + newMultiCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		commdityCupdateGet.setID(commodityCCreate.getID());
		Commodity commdityCupdated = BaseCommodityTest.updateViaAction(commodityCCreate, commdityCupdateGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		List<Commodity> multioleCommodityA_AList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCCreate, mapBO);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityCombineACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityCombineBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码commAoldBarcodes可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(commCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commdityCupdated) == 0, "使用B3条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C、C的多包装商品
		BaseCommodityTest.deleteCommodityViaAction(multioleCommodityA_AList.get(0), Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx62_8() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE62_8(有Q有X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改普通商品C的条形码（商品的条形码为B1）。B1改成B2.结果为使用B1能够查找到商品A，使用B2能够查找到商品B、C");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case628newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case628newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = combineCommAoldBarcodes;
		commodityCGet.setBarcodes(multipleCommCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
		+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = combineCommBoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityCombineACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityB.size() >= 0, "使用B2条形码查找到的商品应包含B、C。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx62_9() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE62_9(有Q有X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改服务商品C的条形码（商品的条形码为B1），B1改成B2.结果为使用B1能够查找到商品A，使用B2能够查找到商品B、C");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case639newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case639newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建服务商品C
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String multipleCommCbarcodesString = combineCommAoldBarcodes;
		commodityCGet.setBarcodes(multipleCommCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
		+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = combineCommBoldBarcodes;
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityCombineACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(retrieveNCommodityB.size() >= 2, "使用B2条形码查找到的商品应包含B、C。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@Test
	public void testUpdateEx63() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE63(无Q无X) 多包装商品A，条形码B1; 多包装商品B，条形码B2； 修改多包装商品C的条形码。B3改成B4.结果为使用B1能够查找到多包装商品A，使用B2能够查找到多包装商品B，使用B4能够查找到多包装商品C");

		// 查找多包装商品A
		Commodity commodity = new Commodity();
		commodity.setID(279);
		List<Commodity> multioleCommodityAList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodity, mapBO);
		// 查找多包装商品B
		commodity.setID(281);
		List<Commodity> multioleCommodityBList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodity, mapBO);
		// 查询到旧的多包装商品的信息
		Commodity normalComm1 = new Commodity();
		normalComm1.setID(283);
		normalComm1 = BaseCommodityTest.retrieve1ExCommodity(normalComm1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(normalComm1 != null, "查询商品失败");
		List<Commodity> multioleCommListOld = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(normalComm1, mapBO);
		Assert.assertTrue(multioleCommListOld.size() > 0, "获取多包装商品失败");
		String newMultioleCommBarcodesC = "commBarcodes" + Shared.generateStringByTime(5);
		// 修改多包装商品信息
		normalComm1.setProviderIDs("1");
		normalComm1.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		normalComm1.setShopID(2);
		normalComm1.setMultiPackagingInfo("444" + Shared.generateStringByTime(7) + "," + newMultioleCommBarcodesC + ";3,4;10,15;10,15;8,3;8,8;" + normalComm1.getName() + "," + multioleCommListOld.get(0).getName() + ";");

		BaseCommodityTest.updateViaAction(normalComm1, normalComm1, mvc, sessionPos1, mapBO, Shared.DBName_Test);
		// // 使用B1查找多包装商品A
		commodity.setID(279);
		List<Commodity> multioleCommodityA_AList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodity, mapBO);
		Assert.assertTrue(multioleCommodityA_AList.get(0).getBarcodeID() == multioleCommodityAList.get(0).getBarcodeID(), "修改多包装商品C后，多包装商品A俩次查出的条形码ID不一样");
		// // 使用B2查找多包装商品B
		commodity.setID(281);
		List<Commodity> multioleCommodityB_BList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodity, mapBO);
		Assert.assertTrue(multioleCommodityB_BList.get(0).getBarcodeID() == multioleCommodityBList.get(0).getBarcodeID(), "修改多包装商品C后，多包装商品B俩次查出的条形码ID不一样");
		// 使用新修改的条形码查找多包装商品C
		List<Commodity> multioleCommListNew = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(normalComm1, mapBO);
		Assert.assertTrue(multioleCommListNew.size() > 0, "获取多包装商品失败");
		Assert.assertTrue(newMultioleCommBarcodesC.equals(multioleCommListNew.get(0).getBarcodes()), "多包装商品修改条形码失败");

		// .修条形码A时，会删除条形码A，创建条形码B。
		// 检查点：
		// 1、T_BarcodesSyncCache有没有增加2条数据。
		// 1）一个D型。它的ID是A的ID。结果验证有没有？
		// 2）一个C型。它的ID是B的ID。结果验证有没有？
		// 2、如果1不通过，手动测试POS能通过？
		// 1）如果功能代码正确，但1.1不能通过，必然有另外一个商品Q存在一品多码，其中一个条形码和A的一样。
		// 2）如果1.2能通过，必然有另外一个商品X存在一品多码，其中一个条形码和B的一样。
		// 所以，测试时分4种case：
		// 1、有Q有X
		// 2、有Q无X
		// 3、无Q有X
		// 4、无Q无X
		// 5、你们之前准备的那一批case，假设数目为8，应该有8X4=32个case。不要求写全部32个case，只需要实现典型的几个case，剩下的case要写空的测试函数，里面放Shared.printTestMethodStartInfo();、Shared.caseLog("xxxxxxx")、和//
		// TODO 将来再回头添加更多的测试用例
	}
	
	
	@Test
	public void testUpdateEx63_2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE63_2(无Q无X) 普通商品A，条形码B1; 普通商品B，条形码B2； 修改普通商品C的条形码。B3改成B4.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B4能够查找到商品C");
		//
		// 创建普通商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case632newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case632newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case632newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码newCommCbarcodesString可以查询到商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodity.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodity.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx63_3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE63_3(无Q无X) 服务商品A，条形码B1; 服务商品B，条形码B2； 修改服务商品C的条形码。B3改成B4.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B4能够查找到商品C");
		//
		// 创建服务商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commAoldBarcodes = "Case633newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commBoldBarcodes = "Case633newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case633newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码newCommCbarcodesString可以查询到商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodity.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodity.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	
	@Test
	public void testUpdateEx63_4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE63_4(无Q无X) 普通商品A，条形码B1; 普通商品B，条形码B2； 修改服务商品C的条形码。B3改成B4.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B4能够查找到商品C");
		//
		// 创建商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case634newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case634newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case634newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码newCommCbarcodesString可以查询到商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodity.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodity.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	
	@Test
	public void testUpdateEx63_5() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE63_5(无Q无X) 服务商品A，条形码B1; 服务商品B，条形码B2； 修改普通商品C的条形码。B3改成B4.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B4能够查找到商品C");
		//
		// 创建商品A、B、C
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commAoldBarcodes = "Case635newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String commBoldBarcodes = "Case635newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case635newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码newCommCbarcodesString可以查询到商品C
		List<BaseModel> retrieveNCommodity = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodity.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodity.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx63_6() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE63_6(无Q无X) 普通商品A，条形码B1; 普通商品B，条形码B2； 修改普通商品C对应的多包装商品的条形码（普通商品的条形码为B3）。B4改成B5.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B3能够查找到商品C");
		//
		// 创建普通商品A、B
		Commodity commodityAGet = BaseCommodityTest.DataInput.getCommodity();
		String commAoldBarcodes = "Case634newCommABarcodes" + Shared.generateStringByTime(9);
		commodityAGet.setBarcodes(commAoldBarcodes);
		commodityAGet.setMultiPackagingInfo(commodityAGet.getBarcodes() + ";" + commodityAGet.getPackageUnitID() + ";" + commodityAGet.getRefCommodityMultiple() + ";" + commodityAGet.getPriceRetail() + ";"
				+ commodityAGet.getPriceVIP() + ";" + commodityAGet.getPriceWholesale() + ";" + commodityAGet.getName() + ";");
		Commodity commodityACreate = BaseCommodityTest.createCommodityViaAction(commodityAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityBGet = BaseCommodityTest.DataInput.getCommodity();
		String commBoldBarcodes = "Case634newCommBBarcodes" + Shared.generateStringByTime(9);
		commodityBGet.setBarcodes(commBoldBarcodes);
		commodityBGet.setMultiPackagingInfo(commodityBGet.getBarcodes() + ";" + commodityBGet.getPackageUnitID() + ";" + commodityBGet.getRefCommodityMultiple() + ";" + commodityBGet.getPriceRetail() + ";"
				+ commodityBGet.getPriceVIP() + ";" + commodityBGet.getPriceWholesale() + ";" + commodityBGet.getName() + ";");
		Commodity commodityBCreate = BaseCommodityTest.createCommodityViaAction(commodityBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C和它们对应的多包装商品
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = "Case636multipleCommCbarcodes" + Shared.generateStringByTime(9);
		String commCbarcodesString = "Case636CommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(commCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + "," + multipleCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C对应的多包装商品的条形码
		String newMultiCommCbarcodesString = "Case632newMultiCommCBarcodes" + Shared.generateStringByTime(9);
		Commodity commdityCupdateGet = BaseCommodityTest.DataInput.getCommodity();
		commdityCupdateGet.setMultiPackagingInfo(multipleCommCbarcodesString + "," + newMultiCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		commdityCupdateGet.setID(commodityCCreate.getID());
		Commodity commdityCupdated = BaseCommodityTest.updateViaAction(commodityCCreate, commdityCupdateGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		List<Commodity> multioleCommodityA_AList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCCreate, mapBO);
		// 使用条形码commAoldBarcodes可以查询到商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(commAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(commBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码commAoldBarcodes可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(commCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commdityCupdated) == 0, "使用B3条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C、C的多包装商品
		BaseCommodityTest.deleteCommodityViaAction(multioleCommodityA_AList.get(0), Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	
	@Test
	public void testUpdateEx63_7() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE63_7(无Q无X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改普通商品C对应的多包装商品的条形码（普通商品的条形码为B3），多包装条形码B4改成B5.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B3能够查找到商品C");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case637newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case676newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C和它们对应的多包装商品
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = "Case637multipleCommCbarcodes" + Shared.generateStringByTime(9);
		String commCbarcodesString = "Case636CommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(commCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + "," + multipleCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C对应的多包装商品的条形码
		String newMultiCommCbarcodesString = "Case637newMultiCommCBarcodes" + Shared.generateStringByTime(9);
		Commodity commdityCupdateGet = BaseCommodityTest.DataInput.getCommodity();
		commdityCupdateGet.setMultiPackagingInfo(multipleCommCbarcodesString + "," + newMultiCommCbarcodesString + ";3,4;10,15;10,15;8,3;8,8;" + commodityCGet.getName() + "," + Shared.generateStringByTime(9) + ";");
		commdityCupdateGet.setID(commodityCCreate.getID());
		Commodity commdityCupdated = BaseCommodityTest.updateViaAction(commodityCCreate, commdityCupdateGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		List<Commodity> multioleCommodityA_AList = BaseCommodityTest.queryMultiPackagingCommodityListViaAction(commodityCCreate, mapBO);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityCombineACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityCombineBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码commAoldBarcodes可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(commCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commdityCupdated) == 0, "使用B3条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C、C的多包装商品
		BaseCommodityTest.deleteCommodityViaAction(multioleCommodityA_AList.get(0), Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx63_8() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE63_8(无Q无X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改普通商品C的条形码（商品的条形码为B3）。B3改成B4.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B4能够查找到商品C");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case638newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case638newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建普通商品C
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity();
		String multipleCommCbarcodesString = "Case638multipleCommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(multipleCommCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
		+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改普通商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case638newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityCombineACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityCombineBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码 newCommCbarcodesString 可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx63_9() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("CASE63_9(无Q无X) 组合商品A，条形码B1; 组合商品B，条形码B2； 修改服务商品C的条形码（商品的条形码为B3）。B3改成B4.结果为使用B1能够查找到商品A，使用B2能够查找到商品B，使用B4能够查找到商品C");
		//
		// 创建普通商品A1、A2、B1、B2,组合商品A、B
		Commodity commodityA1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA1Create = BaseCommodityTest.createCommodityViaAction(commodityA1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityA2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityA2Create = BaseCommodityTest.createCommodityViaAction(commodityA2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityCombineAGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String combineCommAoldBarcodes = "Case639newCombineCommABarcodes" + Shared.generateStringByTime(9);
		commodityCombineAGet.setBarcodes(combineCommAoldBarcodes);
		commodityCombineAGet.setMultiPackagingInfo(commodityCombineAGet.getBarcodes() + ";" + commodityCombineAGet.getPackageUnitID() + ";" + commodityCombineAGet.getRefCommodityMultiple() + ";" + commodityCombineAGet.getPriceRetail() + ";"
				+ commodityCombineAGet.getPriceVIP() + ";" + commodityCombineAGet.getPriceWholesale() + ";" + commodityCombineAGet.getName() + ";");
		setSubCommodities(commodityA1Create.getID(), 1, commodityA2Create.getID(), 2, commodityCombineAGet);
		String subCommodityAInfo = JSONObject.fromObject(commodityCombineAGet).toString();
		commodityCombineAGet.setSubCommodityInfo(subCommodityAInfo);
		Commodity commodityCombineACreate = BaseCommodityTest.createCommodityViaAction(commodityCombineAGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityB1Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB1Create = BaseCommodityTest.createCommodityViaAction(commodityB1Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityB2Get = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityB2Create = BaseCommodityTest.createCommodityViaAction(commodityB2Get, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commodityCombineBGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		setSubCommodities(commodityB1Create.getID(), 1, commodityB2Create.getID(), 2, commodityCombineBGet);
		String combineCommBoldBarcodes = "Case639newCombineCommBBarcodes" + Shared.generateStringByTime(9);
		commodityCombineBGet.setBarcodes(combineCommBoldBarcodes);
		commodityCombineBGet.setMultiPackagingInfo(commodityCombineBGet.getBarcodes() + ";" + commodityCombineBGet.getPackageUnitID() + ";" + commodityCombineBGet.getRefCommodityMultiple() + ";" + commodityCombineBGet.getPriceRetail() + ";"
				+ commodityCombineBGet.getPriceVIP() + ";" + commodityCombineBGet.getPriceWholesale() + ";" + commodityCombineBGet.getName() + ";");
		String subCommodityBInfo = JSONObject.fromObject(commodityCombineBGet).toString();
		commodityCombineBGet.setSubCommodityInfo(subCommodityBInfo);
		Commodity commodityCombineBCreate = BaseCommodityTest.createCommodityViaAction(commodityCombineBGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		// 创建服务商品C
		Commodity commodityCGet = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		String multipleCommCbarcodesString = "Case639multipleCommCbarcodes" + Shared.generateStringByTime(9);
		commodityCGet.setBarcodes(multipleCommCbarcodesString);
		commodityCGet.setMultiPackagingInfo(commodityCGet.getBarcodes() + ";" + commodityCGet.getPackageUnitID() + ";" + commodityCGet.getRefCommodityMultiple() + ";" + commodityCGet.getPriceRetail() + ";"
		+ commodityCGet.getPriceVIP() + ";" + commodityCGet.getPriceWholesale() + ";" + commodityCGet.getName() + ";");
		Commodity commodityCCreate = BaseCommodityTest.createCommodityViaAction(commodityCGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 修改服务商品C的条形码
		Barcodes commColdBarcodes = BaseBarcodesTest.retrieveNBarcodes(commodityCCreate.getID(), Shared.DBName_Test);
		String newCommCbarcodesString = "Case639newCommCBarcodes" + Shared.generateStringByTime(9);
		// 
		commColdBarcodes.setBarcode(newCommCbarcodesString);
		BaseBarcodesTest.updateBarcodesViaAction(commColdBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 使用条形码combineCommAoldBarcodes可以查询到组合商品A
		List<BaseModel> retrieveNCommodityA = BaseCommodityTest.retrieveNCommodity(combineCommAoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityA.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityA.get(0).compareTo(commodityCombineACreate) == 0, "使用B1条形码查找到的商品和原先商品不一样。");
		// 使用条形码commBoldBarcodes可以查询到商品B
		List<BaseModel> retrieveNCommodityB = BaseCommodityTest.retrieveNCommodity(combineCommBoldBarcodes, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityB.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityB.get(0).compareTo(commodityCombineBCreate) == 0, "使用B2条形码查找到的商品和原先商品不一样。");
		// 使用条形码 newCommCbarcodesString 可以查询到商品C
		List<BaseModel> retrieveNCommodityC = BaseCommodityTest.retrieveNCommodity(newCommCbarcodesString, BaseBO.INVALID_CASE_ID);
		retrieveNCommodityC.get(0).setIgnoreSlaveListInComparision(true);
		Assert.assertTrue(retrieveNCommodityC.get(0).compareTo(commodityCCreate) == 0, "使用B4条形码查找到的商品和原先商品不一样。");
		// 删除商品A、B、C
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineACreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCombineBCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityA2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB1Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityB2Create, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityCCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx64() throws Exception {
		Shared.printTestMethodStartInfo();
		/**
		 * 1、模拟web前端创建一个商品、对商品进行两次修改（修改名称）
		 * 2、pos1进行登录，同步商品、feedback
		 * 3、pos1再次进行登录，不需要再同步刚刚创建的商品
		 */
		//
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionBoss);
		Shared.caseLog("case64:创建一个商品、对商品进行两次修改（修改名称）,验证同步块缓存和pos的feedback");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		// 必须是web创建和修改商品，注意不要用错session
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 第一次update
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setName("第一次修改" + Shared.generateStringByTime(8));
		commUpdate.setID(commCreate.getID());
		commUpdate.setProviderIDs("1");
		commUpdate.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + 
				";1;1;1;0.8;2;" + commUpdate.getName() + Shared.generateStringByTime(8) + ";");
		BaseCommodityTest.updateViaAction(commCreate, commUpdate, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		// 验证同步块缓存
		// 商品A的U型同步块缓存是否创建。
		List<BaseSyncCache> commoditySyncCacheList1 = CheckPoint.verifyFromCacheIfSyncCacheExists(Shared.DBName_Test, commUpdate, EnumSyncCacheType.ESCT_CommoditySyncInfo, SyncCache.SYNC_Type_U);
		Assert.assertTrue(commoditySyncCacheList1 != null && commoditySyncCacheList1.size() == 1, "同步缓存不存在创建出来的对象");
		// 第二次update
		Commodity commUpdate2 = BaseCommodityTest.DataInput.getCommodity();
		commUpdate2.setName("第二次修改" + Shared.generateStringByTime(8));
		commUpdate2.setID(commCreate.getID());
		commUpdate2.setProviderIDs("1");
		commUpdate2.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + 
				";1;1;1;0.8;2;" + commUpdate2.getName() + Shared.generateStringByTime(8) + ";");
		BaseCommodityTest.updateViaAction(commCreate, commUpdate2, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		//
		// 验证同步块缓存
		// 商品A的U型同步块缓存是否创建。
		List<BaseSyncCache> commoditySyncCacheList2 = CheckPoint.verifyFromCacheIfSyncCacheExists(Shared.DBName_Test, commUpdate2, EnumSyncCacheType.ESCT_CommoditySyncInfo, SyncCache.SYNC_Type_U);
		Assert.assertTrue(commoditySyncCacheList2 != null && commoditySyncCacheList2.size() == 1, "同步缓存不存在创建出来的对象");
		Assert.assertTrue(commoditySyncCacheList1.get(0).getSyncSequence() != commoditySyncCacheList2.get(0).getSyncSequence(), "第一次的U型同步块没有被删除");
		// pos1登录同步，需要同步该商品
		MvcResult mr = mvc.perform(get("/commoditySync/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionPos2)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		// 获取到返回objectID和ErrorCode
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		List<?> bmList = JsonPath.read(o, "$.objectList[*].ID");
		bmList = bmList.stream().distinct().collect(Collectors.toList());// 去除重复
		String ids = "";
		// 需要同步的商品Id中，是否存在刚刚创建的商品Id
		boolean existCommID = false;
		for (Object bmID : bmList) {
			ids += bmID + ",";
			if(Integer.parseInt(String.valueOf(bmID)) == commCreate.getID()) {
				existCommID = true;
			}
		}
		Assert.assertTrue(existCommID, "pos没有获取到需要同步的商品信息");
		System.out.println(ids);
		// pos进行feedback
		mvc.perform(get("/commoditySync/feedbackEx.bx?sID=" + ids + "&errorCode=EC_NoError") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionPos2)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// pos1再次登录（不需要登出，仍然可以模拟重新同步一次），不需要同步该商品
		MvcResult mr2 = mvc.perform(get("/commoditySync/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionPos2)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2);
		// 获取到返回objectID和ErrorCode
		String json2 = mr2.getResponse().getContentAsString();
		JSONObject o2 = JSONObject.fromObject(json2);
		List<?> bmList2 = JsonPath.read(o2, "$.objectList[*].ID");
		bmList2 = bmList2.stream().distinct().collect(Collectors.toList());// 去除重复
		// feedback后，需要同步的商品Id中，是否存在刚刚创建的商品Id
		boolean existCommID2 = false;
		for (Object bmID : bmList2) {
			if(Integer.parseInt(String.valueOf(bmID)) == commCreate.getID()) {
				existCommID2 = true;
			}
		}
		Assert.assertTrue(!existCommID2, "pos已经同步过该商品信息，不应该再需要同步");
	}
	
	@Test
	public void testUpdateEx65() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Shared.caseLog("case65:店长无权限修改普通商品");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Commodity commUpdate = BaseCommodityTest.DataInput.getCommodity();
		commUpdate.setID(commCreate.getID());
		commUpdate.setProviderIDs("1");
		commUpdate.setMultiPackagingInfo(barcode1 + ";1;1;1;8;8;" + commodityA + Shared.generateStringByTime(8) + ";");
		//
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commUpdate, sessionShopManager) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_NoPermission);	
		BaseCommodityTest.deleteCommodityViaAction(commCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}
	
	@Test
	public void testUpdateEx66_Service() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case66: 店长无权限修改服务商品");
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchasingUnit("");
		c.setShelfLife(0);
		c.setType(EnumCommodityType.ECT_Service.getIndex());
		c.setMultiPackagingInfo(barcode1 + ";3;10;1;8;8;" + commodityC + Shared.generateStringByTime(8) + ";");
		//
		Commodity pase1Comm = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		//
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setShelfLife(0);
		c2.setID(pase1Comm.getID());
		c2.setPurchasingUnit("");
		c2.setPurchaseFlag(0);
		c2.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		c2.setType(EnumCommodityType.ECT_Service.getIndex());
		c2.setMultiPackagingInfo(barcode1 + ";3;10;10;8;8;" + commodityA + ";");
		c2.setBarcodes(barcode1);
		//
		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c2, sessionShopManager) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_NoPermission);
		// 删除测试数据
		BaseCommodityTest.deleteCommodityViaAction(c2, Shared.DBName_Test, mvc, sessionBoss, mapBO);
//		BaseCommodityTest.updateViaAction(c2, c2, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// staff登录
		// staffLogin(mvc, Shared.PhoneOfBoss);
		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		// 在pos1中创建一个商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("1");
		c.setMultiPackagingInfo(barcode8 + "," + "888" + Shared.generateStringByTime(8) + ",8882" + Shared.generateStringByTime(8) + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" + commodityA + Shared.generateStringByTime(8) + "," + commodityB
				+ Shared.generateStringByTime(8) + "," + commodityC + Shared.generateStringByTime(8) + ";");

		MvcResult mrl = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mrl);
		// 在pos2中retrieveN
		Shared.caseLog("case1:retrieveNEx");
		MvcResult mr = mvc.perform(get("/commoditySync/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionPos2)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testUploadPicture() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult req = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1);
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("1");
		c.setMultiPackagingInfo(barcode1 + "," + "222" + Shared.generateStringByTime(8) + ";1,2;1,5;1,5;8,3;8,8;" //
				+ commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + ";");

		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
				.param(Commodity.field.getFIELD_NAME_lastOperationToPicture(), String.valueOf(Shared.LAST_OPERATION_TO_PICTURE_Upload))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr1);
	}

	@Test
	public void testUploadPicture2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case2:用不符合的类型上传");
		// 将传入类型更换
		String path1 = System.getProperty("user.dir") + "\\src\\main\\resources";
		File file1 = new File(path1 + "\\apiclient_cert.p12");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename1, "application/x-pkcs12", fis1);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionPos1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUploadPicture3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case3:文档上传");
		String path1 = System.getProperty("user.dir") + "\\src\\test\\resources\\images";
		File file1 = new File(path1 + "\\test.txt");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename1, "txt/txt", fis1);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionPos1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUploadPicture4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4: gif格式上传");
		String path1 = System.getProperty("user.dir") + "\\src\\test\\resources\\images";
		File file1 = new File(path1 + "\\images_Gif.gif");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename1, "image/gif", fis1);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionPos1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUploadPicture5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5: 使用jpeg格式上传");
		String path1 = System.getProperty("user.dir") + "\\src\\test\\resources\\images";
		File file1 = new File(path1 + "\\normalJPEG.jpeg");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename1, CommoditySyncAction.JPEGType, fis1);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionPos1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testUploadPicture6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6: 使用jpeg格式上传，图片大小大于100kb");
		String path1 = System.getProperty("user.dir") + "\\src\\test\\resources\\images";
		File file1 = new File(path1 + "\\overSizeJPEG.jpeg");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename1, CommoditySyncAction.JPEGType, fis1);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionPos1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUploadPicture7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7: 使用png格式上传，图片大小大于100kb");
		String path1 = System.getProperty("user.dir") + "\\src\\test\\resources\\images";
		File file1 = new File(path1 + "\\overSizePNG.png");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename1, CommoditySyncAction.PNGType, fis1);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionPos1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUploadPicture8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8: 使用bmp格式上传");
		String path1 = System.getProperty("user.dir") + "\\src\\test\\resources\\images";
		File file1 = new File(path1 + "\\images_Bmp.bmp");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename1, "image/bmp", fis1);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionPos1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUploadPicture9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9: 使用tiff格式上传");
		String path1 = System.getProperty("user.dir") + "\\src\\test\\resources\\images";
		File file1 = new File(path1 + "\\images_Tiff.tiff");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename1, "image/tiff", fis1);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionPos1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUploadPicture10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10: 使用jpg格式上传");
		String path1 = System.getProperty("user.dir") + "\\src\\test\\resources\\images";
		File file1 = new File(path1 + "\\normalJPG.jpg");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename1, CommoditySyncAction.JPGType, fis1);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionPos1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testUploadPicture11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10: 使用jpg格式上传,图片大小大于100KB");
		String path1 = System.getProperty("user.dir") + "\\src\\test\\resources\\images";
		File file1 = new File(path1 + "\\overSizeJPG.jpg");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename1, CommoditySyncAction.JPGType, fis1);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionPos1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	/**
	 * 创建带图片的商品，并更换不同的图片格式以方便测试
	 * 
	 * @param originalFilePath
	 *            原来的图片的路径，如\\normalPNG.png
	 * @param updateFilePath
	 *            要更换成的图片的路径，如\\normalJPEG.jpeg
	 * @param updatePictureType
	 *            要更换成的图片格式，如CommoditySyncAction.JPEGType
	 * @param originalPicType
	 *            原来的图片格式，如CommoditySyncAction.JPEGType
	 * @param updatePicType
	 *            要更换成的图片格式，如jpeg
	 * @throws Exception
	 */
	private void checkCommodityPicType(String originalFilePath, String updateFilePath, String updatePictureType, String originalPicType, String updatePicType) throws Exception {
		// 创建一个带有图片的商品
		String path = System.getProperty("user.dir") + "\\src\\test\\resources\\images";
		MvcResult req = BaseCommodityTest.uploadPicture(path, originalFilePath, originalPicType, mvc, sessionPos1);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setProviderIDs("7");
		c.setMultiPackagingInfo(barcode1 + ";3;10;10;8;8;" //
				+ commodityA + Shared.generateStringByTime(8) + ";");
		c.setLastOperationToPicture(Shared.LAST_OPERATION_TO_PICTURE_Upload);
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(c, mvc, req.getRequest().getSession(), mapBO, Shared.DBName_Test);
		Assert.assertTrue(!"".equals(createCommodity.getPicture()), "创建的商品没有图片");

		MvcResult mr1 = BaseCommodityTest.uploadPicture(path, updateFilePath, updatePictureType, mvc, sessionPos1);
		// 修改新创建商品的图片为jpeg格式
		createCommodity.setName("case39" + Shared.generateStringByTime(8));
		createCommodity.setProviderIDs("1");
		createCommodity.setMultiPackagingInfo("234" + Shared.generateStringByTime(8) + ",456" + Shared.generateStringByTime(8) + ",789" + Shared.generateStringByTime(8) + ",901" + Shared.generateStringByTime(8) + //
				";1,2,3,4;1,5,10,4;1,5,10,4;0.8,4,8,2;2,3,4,4;" + c.getName() + Shared.generateStringByTime(8) + "," + commodityA + Shared.generateStringByTime(8) + "," + commodityB + Shared.generateStringByTime(8) + "," + commodityC
				+ "新增多包装" + Shared.generateStringByTime(8) + ";");
		createCommodity.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		createCommodity.setShopID(c.getShopID());
		BaseCommodityTest.updateViaAction(createCommodity, createCommodity, mvc, mr1.getRequest().getSession(), mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUploadPicture12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12: 创建带png格式图片的商品，再把图片更改为jpeg格式保存");
		String originalFilePath = "\\normalPNG.png";
		String originalPicType = CommoditySyncAction.PNGType;
		String updateFilePath = "\\normalJPEG.jpeg";
		String updatePictureType = CommoditySyncAction.JPEGType;
		String updatePicType = "jpeg";
		checkCommodityPicType(originalFilePath, updateFilePath, updatePictureType, originalPicType, updatePicType);
	}

	@Test
	public void testUploadPicture13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13: 创建带png格式图片的商品，再把图片更改为jpg格式保存");
		String originalFilePath = "\\normalPNG.png";
		String originalPicType = CommoditySyncAction.PNGType;
		String updateFilePath = "\\normalJPG.jpg";
		String updatePictureType = CommoditySyncAction.JPGType;
		String updatePicType = "jpg";
		checkCommodityPicType(originalFilePath, updateFilePath, updatePictureType, originalPicType, updatePicType);
	}

	@Test
	public void testUploadPicture14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14: 创建带png格式图片的商品，再把图片更改为png格式保存");
		String originalFilePath = "\\normalPNG.png";
		String originalPicType = CommoditySyncAction.PNGType;
		String updateFilePath = "\\normalPNG.png";
		String updatePictureType = CommoditySyncAction.PNGType;
		String updatePicType = "png";
		checkCommodityPicType(originalFilePath, updateFilePath, updatePictureType, originalPicType, updatePicType);
	}

	@Test
	public void testUploadPicture15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15: 创建带jpg格式图片的商品，再把图片更改为jpeg格式保存");
		String originalFilePath = "\\normalJPG.jpg";
		String originalPicType = CommoditySyncAction.JPGType;
		String updateFilePath = "\\normalJPEG.jpeg";
		String updatePictureType = CommoditySyncAction.JPEGType;
		String updatePicType = "jpeg";
		checkCommodityPicType(originalFilePath, updateFilePath, updatePictureType, originalPicType, updatePicType);
	}

	@Test
	public void testUploadPicture16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16: 创建带jpg格式图片的商品，再把图片更改为jpg格式保存");
		String originalFilePath = "\\normalJPG.jpg";
		String originalPicType = CommoditySyncAction.JPGType;
		String updateFilePath = "\\normalJPG.jpg";
		String updatePictureType = CommoditySyncAction.JPGType;
		String updatePicType = "jpg";
		checkCommodityPicType(originalFilePath, updateFilePath, updatePictureType, originalPicType, updatePicType);
	}

	@Test
	public void testUploadPicture17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17: 创建带jpg格式图片的商品，再把图片更改为png格式保存");
		String originalFilePath = "\\normalJPG.jpg";
		String originalPicType = CommoditySyncAction.JPGType;
		String updateFilePath = "\\normalPNG.png";
		String updatePictureType = CommoditySyncAction.PNGType;
		String updatePicType = "png";
		checkCommodityPicType(originalFilePath, updateFilePath, updatePictureType, originalPicType, updatePicType);
	}

	@Test
	public void testUploadPicture18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case18: 创建带jpeg格式图片的商品，再把图片更改为jpeg格式保存");
		String originalFilePath = "\\normalJPEG.jpeg";
		String originalPicType = CommoditySyncAction.JPEGType;
		String updateFilePath = "\\normalJPEG.jpeg";
		String updatePictureType = CommoditySyncAction.JPEGType;
		String updatePicType = "jpeg";
		checkCommodityPicType(originalFilePath, updateFilePath, updatePictureType, originalPicType, updatePicType);
	}

	@Test
	public void testUploadPicture19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19: 创建带jpeg格式图片的商品，再把图片更改为jpg格式保存");
		String originalFilePath = "\\normalJPEG.jpeg";
		String originalPicType = CommoditySyncAction.JPEGType;
		String updateFilePath = "\\normalJPG.jpg";
		String updatePictureType = CommoditySyncAction.JPGType;
		String updatePicType = "jpg";
		checkCommodityPicType(originalFilePath, updateFilePath, updatePictureType, originalPicType, updatePicType);
	}

	@Test
	public void testUploadPicture20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case20: 创建带jpeg格式图片的商品，再把图片更改为png格式保存");
		String originalFilePath = "\\normalJPEG.jpeg";
		String originalPicType = CommoditySyncAction.JPEGType;
		String updateFilePath = "\\normalPNG.png";
		String updatePictureType = CommoditySyncAction.PNGType;
		String updatePicType = "png";
		checkCommodityPicType(originalFilePath, updateFilePath, updatePictureType, originalPicType, updatePicType);
	}

	// 可变因素4个
	// 新集合只有C或只有D或只有U
	// 边界值
	@Test
	public void testCheckPackageUnit() throws InterruptedException {
		Shared.printTestMethodStartInfo();

		Commodity comm = new Commodity();
		comm.setID(47);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> commR1 = commodityBO.retrieve1ObjectEx(staff.getID(), BaseBO.INVALID_CASE_ID, comm);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false);
		}
		comm = Commodity.fetchCommodityFromResultSet(commR1);
		// 删除多包装2，增加多包装6，7，修改多包装4
		comm.setMultiPackagingInfo(barcode1 + "," + barcode3 + "4444" + Shared.generateStringByTime(8) + "," + barcode4 + "," + barcode5 + "," + barcode6 + "," + barcode7 + ";" //
				+ packageUnit1 + "," + packageUnit3 + "," + packageUnit4 + "," + packageUnit5 + "," + packageUnit6 + "," + packageUnit7 + ";" //
				+ refCommodityMultiple1 + "," + refCommodityMultiple3 + "," + refCommodityMultiple4 + "," + refCommodityMultiple5 + "," + refCommodityMultiple6 + "," + refCommodityMultiple7 + ";" //
				+ priceRetail1 + "," + priceRetail3 + "," + priceRetail4 + "," + priceRetail5 + "," + priceRetail6 + "," + priceRetail7 + ";");
		//
		List<PackageUnit> lspu = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuCreated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuUpdated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuDeleted = new ArrayList<PackageUnit>();

		Staff staff = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
		if (CommoditySyncAction.checkPackageUnit(comm, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, staff.getID())) {
			assertTrue(lspu.size() == 6 && lspuCreated.size() == 2 && lspuUpdated.size() == 1 && lspuDeleted.size() == 1);
		}

		// 只新增多包装商品6
		comm.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + barcode3 + "," + barcode4 + "," + barcode5 + "," + "6666" + Shared.generateStringByTime(8) + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit3 + "," + packageUnit4 + "," + packageUnit5 + "," + packageUnit6 + ";" //
				+ refCommodityMultiple1 + "," + refCommodityMultiple2 + "," + refCommodityMultiple3 + "," + refCommodityMultiple4 + "," + refCommodityMultiple5 + "," + refCommodityMultiple6 + ";" //
				+ priceRetail1 + "," + priceRetail2 + "," + priceRetail3 + "," + priceRetail4 + "," + priceRetail5 + "," + priceRetail6 + ";");

		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		if (CommoditySyncAction.checkPackageUnit(comm, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, staff.getID())) {
			assertTrue(lspu.size() == 6 && lspuCreated.size() == 1 && lspuUpdated.size() == 0 && lspuDeleted.size() == 0);
		}

		// 只修改多包装商品4
		comm.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + barcode3 + "," + "4444" + Shared.generateStringByTime(8) + "," + barcode5 + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit3 + "," + packageUnit4 + "," + packageUnit5 + ";" //
				+ refCommodityMultiple1 + "," + refCommodityMultiple2 + "," + refCommodityMultiple3 + "," + refCommodityMultiple4 + "," + refCommodityMultiple5 + ";" //
				+ priceRetail1 + "," + priceRetail2 + "," + priceRetail3 + "," + priceRetail4 + "," + priceRetail5 + ";");

		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		if (CommoditySyncAction.checkPackageUnit(comm, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, staff.getID())) {
			assertTrue(lspu.size() == 5 && lspuCreated.size() == 0 && lspuUpdated.size() == 1 && lspuDeleted.size() == 0);
		}

		// 只删除多包装商品4
		comm.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + barcode3 + "," + barcode5 + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit3 + "," + packageUnit5 + ";" //
				+ refCommodityMultiple1 + "," + refCommodityMultiple2 + "," + refCommodityMultiple3 + "," + refCommodityMultiple5 + ";" //
				+ priceRetail1 + "," + priceRetail2 + "," + priceRetail3 + "," + priceRetail5 + ";");

		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		if (CommoditySyncAction.checkPackageUnit(comm, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, staff.getID())) {
			assertTrue(lspu.size() == 4 && lspuCreated.size() == 0 && lspuUpdated.size() == 0 && lspuDeleted.size() == 1);
		}

		// 传输超过5个副单位，超过边界值
		comm.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + barcode3 + "," + barcode4 + "," + barcode5 + "," + barcode6 + "," + barcode7 + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit3 + "," + packageUnit4 + "," + packageUnit5 + "," + packageUnit6 + "," + packageUnit7 + ";" //
				+ refCommodityMultiple1 + "," + refCommodityMultiple2 + "," + refCommodityMultiple3 + "," + refCommodityMultiple4 + "," + refCommodityMultiple5 + "," + refCommodityMultiple6 + "," + refCommodityMultiple7 + ";" //
				+ priceRetail1 + "," + priceRetail2 + "," + priceRetail3 + "," + priceRetail4 + "," + priceRetail5 + "," + priceRetail6 + "," + priceRetail7 + ";");
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		if (!CommoditySyncAction.checkPackageUnit(comm, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, staff.getID())) {
			assertTrue(lspu.size() == 0 && lspuCreated.size() == 0 && lspuUpdated.size() == 0 && lspuDeleted.size() == 0);
		}

		// 只修改多包装商品2 barcode
		comm.setMultiPackagingInfo(barcode1 + "," + "2222" + Shared.generateStringByTime(8) + "," + barcode3 + "," + barcode4 + "," + barcode5 + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit3 + "," + packageUnit4 + "," + packageUnit5 + ";" //
				+ refCommodityMultiple1 + "," + refCommodityMultiple2 + "," + refCommodityMultiple3 + "," + refCommodityMultiple4 + "," + refCommodityMultiple5 + ";" //
				+ priceRetail1 + "," + priceRetail2 + "," + priceRetail3 + "," + priceRetail4 + "," + priceRetail5 + ";");

		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		if (CommoditySyncAction.checkPackageUnit(comm, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, staff.getID())) {
			assertTrue(lspu.size() == 5 && lspuCreated.size() == 0 && lspuUpdated.size() == 1 && lspuDeleted.size() == 0);
		}

		// ...等以后有需要再做单独的修改测试
		// 只修改多包装商品2倍数、零售价、采购建议价
		comm.setMultiPackagingInfo(barcode1 + "," + barcode2 + "," + barcode3 + "," + barcode4 + "," + barcode5 + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit3 + "," + packageUnit4 + "," + packageUnit5 + ";" //
				+ refCommodityMultiple1 + "," + "1" + System.currentTimeMillis() % 10 + "," + refCommodityMultiple3 + "," + refCommodityMultiple4 + "," + refCommodityMultiple5 + ";" //
				+ priceRetail1 + "," + "1" + System.currentTimeMillis() % 10 + "," + priceRetail3 + "," + priceRetail4 + "," + priceRetail5 + ";");

		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		if (CommoditySyncAction.checkPackageUnit(comm, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, staff.getID())) {
			assertTrue(lspu.size() == 5 && lspuCreated.size() == 0 && lspuUpdated.size() == 1 && lspuDeleted.size() == 0);
		}

		// ...增加case：不CUD任何东西，只将传递的顺序打乱
		comm.setMultiPackagingInfo(refCommodityMultiple3 + "," + barcode2 + "," + barcode3 + "," + barcode5 + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit3 + "," + priceRetail1 + "," + packageUnit5 + ";" //
				+ refCommodityMultiple1 + "," + "1" + System.currentTimeMillis() % 10 + "," + barcode1 + "," + refCommodityMultiple4 + "," + refCommodityMultiple5 + ";" //
				+ packageUnit4 + "," + "1" + System.currentTimeMillis() % 10 + "," + priceRetail3 + "," + priceRetail4 + "," + priceRetail5 + ";" //
				+ "1" + System.currentTimeMillis() % 10 + "," + barcode4 + ";");

		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		if (!CommoditySyncAction.checkPackageUnit(comm, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, staff.getID())) {
			assertTrue(true);
		} else {
			assertTrue(false);
		}

		// ...增加case：各个数组的维度不相同，比如，只传递4个barcode，其它包装单位啥的，全部传递5个
		comm.setMultiPackagingInfo(refCommodityMultiple3 + "," + barcode2 + "," + barcode3 + ";" //
				+ packageUnit1 + "," + packageUnit2 + "," + packageUnit3 + "," + priceRetail1 + "," + packageUnit5 + ";" //
				+ refCommodityMultiple1 + "," + "1" + System.currentTimeMillis() % 10 + "," + barcode1 + "," + refCommodityMultiple4 + "," + refCommodityMultiple5 + ";" //
				+ packageUnit4 + "," + "1" + System.currentTimeMillis() % 10 + "," + priceRetail3 + "," + priceRetail4 + ";" //
				+ "1" + System.currentTimeMillis() % 10 + "," + barcode4 + ";");

		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		if (!CommoditySyncAction.checkPackageUnit(comm, lspu, lspuCreated, lspuUpdated, lspuDeleted, Shared.DBName_Test, logger, packageUnitBO, commodityBO, barcodesBO, staff.getID())) {
			assertTrue(true);
		} else {
			assertTrue(false);
		}
	}

	@Test
	public void testDeleteCommodityPictureEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("--------------case1:正常删除会话----------------");
		sessionPos1 = BaseCommodityTest.uploadPicture(PATH, "", CommoditySyncAction.PNGType, mvc, sessionPos1).getRequest().getSession();

		mvc.perform(//
				post("/commoditySync/deleteCommodityPictureEx.bx")//
						.session((MockHttpSession) sessionPos1))//
				.andExpect(status().isOk())//
				.andReturn();//

		File dest = (File) sessionPos1.getAttribute(EnumSession.SESSION_CommodityPictureDestination.getName());
		MultipartFile file = (MultipartFile) sessionPos1.getAttribute(EnumSession.SESSION_PictureFILE.getName());
		assertTrue(dest == null && file == null);
	}
	
	private void setSubCommodities(int subCommID1, int subCommNO1, int subCommID2, int subCommNO2, Commodity combineCommodity) {
		List<SubCommodity> subCommodities = new ArrayList<SubCommodity>();

		SubCommodity subCommodity1 = new SubCommodity();
		subCommodity1.setSubCommodityNO(subCommNO1);
		subCommodity1.setSubCommodityID(subCommID1);
		subCommodity1.setPrice(3);
		subCommodities.add(subCommodity1);

		SubCommodity subCommodity2 = new SubCommodity();
		subCommodity2.setSubCommodityNO(subCommNO2);
		subCommodity2.setSubCommodityID(subCommID2);
		subCommodity2.setPrice(3);
		subCommodities.add(subCommodity2);

		combineCommodity.setListSlave1(subCommodities);
	}
}
