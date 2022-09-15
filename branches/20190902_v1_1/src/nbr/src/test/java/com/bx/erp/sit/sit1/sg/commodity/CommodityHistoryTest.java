package com.bx.erp.sit.sit1.sg.commodity;

import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.commodity.CommoditySyncAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityHistory;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.test.checkPoint.ReturnCommoditySheetCP;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

import org.testng.annotations.BeforeClass;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class CommodityHistoryTest extends BaseActionTest {
	protected AtomicLong barcode;
	protected AtomicInteger commodityOrder;
	protected String bossPhone = "";
	protected String companySN = "";
	protected String dbNameOfNewCompany = "";
	protected AtomicInteger order;
	protected Map<String, BaseModel> commodityHistoryMap;
	protected final String SIMPLE_COMMODITY = "普通商品_sit1_sg";
	protected final String CombinationCommodity = "组合商品_sit1_sg";
	protected final String MultiPackagingCommodity = "多包装商品_sit1_sg";
	private final String CommodityIDs = "commIDs";
	private final String WarehousingCommodityNOs = "commNOs";
	private final String CommodityPrices = "commPrices";
	private final String CommodityAmounts = "amounts";
	private final String CommodityBarcodeIDs = "barcodeIDs";
	private final String ReturnCommoditySheetCommodityNOs = "rcscNOs";
	private final String ReturnCommoditySheetCommoditySpecifications = "rcscSpecifications";

	private final String FieldName_PriceRetail = "零售价";
	private final String FieldName_Category = "商品类别";
	private final String FieldName_PackageUnit = "包装单位";
	private final String FieldName_Specification = "规格";
	private final String FieldName_CommodityName = "商品名称";
	private final String FieldName_Barcodes = "条形码";
	private final String FieldName_Stock = "库存";

	private MockHttpSession sessionBossOfNewCompany;

	private List<PackageUnit> lspu;
	private List<PackageUnit> lspuCreated;
	private List<PackageUnit> lspuUpdated;
	private List<PackageUnit> lspuDeleted;

	/** 存放创建公司1时返回的MvcResult 由于做company的结果验证会第一次登录老板账号并修改密码之后删除售前账号
	 * 所以存起来以便测试完售前账号之后做company结果验证 */
	private static MvcResult mvcResult_Company;

	@BeforeClass
	public void beforeClass() {
		super.setUp();

		order = new AtomicInteger();
		barcode = new AtomicLong();
		barcode.set(6821423302500L);
		commodityOrder = new AtomicInteger();
		commodityOrder.set(64);
		commodityHistoryMap = new HashMap<String, BaseModel>();

		// try {
		// MockHttpSession sessionBossOfAnotherCompany =
		// Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// 由于本测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();

		// 由于本测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@Test
	public void openCommodityHistoryPage() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "新建门店后，查看数据");

		// 创建一个新公司，查看相关数据（数据应为空）
		Company company = BaseCompanyTest.DataInput.getCompany();
		bossPhone = company.getBossPhone();
		dbNameOfNewCompany = company.getDbName();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr1 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP).session(sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		//
		Company companyCreate = (Company) Shared.parse1Object(mr1, company, BaseAction.KEY_Object);
		companySN = companyCreate.getSN();
		// 由于结果验证会登录老板账号然后导致售前账号被删除，所以把MvcResult先存起来，到下下个用例再做结果验证吧
		mvcResult_Company = mr1;
		commodityHistoryMap.put("company", company);
	}

	@Test(dependsOnMethods = "openCommodityHistoryPage")
	public void cannotUsePhoneOfPreSaleLoginAndCUDCommodityHistory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "售前人员无法进行CUD操作");

		// 售前账号登录
		Company company = (Company) commodityHistoryMap.get("company");
		JSONObject jsonObject = JSONObject.fromObject(mvcResult_Company.getResponse().getContentAsString());
		String companySN_New = JsonPath.read(jsonObject, "$.object.SN");
		sessionBossOfNewCompany = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, companySN_New);
		//
		// 创建普通商品53
		Commodity c53 = BaseCommodityTest.DataInput.getCommodity();
		c53.setName(SIMPLE_COMMODITY + commodityOrder);
		c53.setMultiPackagingInfo(barcode + ";" + c53.getPackageUnitID() + ";" + c53.getRefCommodityMultiple() + ";" + c53.getPriceRetail() + ";" + c53.getPriceVIP() + ";" + c53.getPriceWholesale() + ";" + c53.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c53, sessionBossOfNewCompany) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);

		// ...新公司没有数据，不能进行UD操作

		// 做company的结果验证
		CompanyCP.verifyCreate(mvcResult_Company, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, company);
		// 验证敏感信息是否为空
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbNameOfNewCompany, mvc, mapBO, companySN_New, company.getBossPhone(), company.getBossPassword());
		// 使用原来的BOSS账号登录SESSION
		try {
			sessionBossOfNewCompany = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = "cannotUsePhoneOfPreSaleLoginAndCUDCommodityHistory")
	public void createCommodityAndRetrieveCommodityHistoryAboutBarcode() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "创建没有期初值的普通商品46后，查看在商品修改历史中有无条形码修改数据");

		Commodity c46 = BaseCommodityTest.DataInput.getCommodity();
		c46.setName(SIMPLE_COMMODITY + commodityOrder + Shared.generateStringByTime(6));
		c46.setMultiPackagingInfo(barcode + ";" + c46.getPackageUnitID() + ";" + c46.getRefCommodityMultiple() + ";" + c46.getPriceRetail() + ";" + c46.getPriceVIP() + ";" + c46.getPriceWholesale() + ";" + c46.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c46, sessionBossOfNewCompany) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1);

		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Commodity commCreated = (Commodity) c46.parse1(jsonObject.getString(BaseAction.KEY_Object));
		CommodityCP.verifyCreate(mr1, commCreated, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, dbNameOfNewCompany);
		//
		jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		BaseModel commodity46 = c46.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityHistoryMap.put("commodity46", commodity46);
		//
		// 查看在商品修改历史中有无条形码修改数据
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity46.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Barcodes)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");

		Assert.assertTrue(CommodityHistoryList.size() >= 1, "商品修改历史中没有条形码修改数据");
	}

	@Test(dependsOnMethods = "createCommodityAndRetrieveCommodityHistoryAboutBarcode")
	public void createCommodityAndRetrieveCommodityHistoryAboutBarcodeAndStock() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "创建数量>0的普通商品47后，查看商品修改历史中有无条形码修改数据和库存改变数据");

		Commodity c47 = BaseCommodityTest.DataInput.getCommodity();
		c47.setName(SIMPLE_COMMODITY + getCommodityOrder());
		c47.setnOStart(10);
		c47.setPurchasingPriceStart(10);
		c47.setMultiPackagingInfo(getBarcode() + ";" + c47.getPackageUnitID() + ";" + c47.getRefCommodityMultiple() + ";" + c47.getPriceRetail() + ";" + c47.getPriceVIP() + ";" + c47.getPriceWholesale() + ";" + c47.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c47, sessionBossOfNewCompany) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		CommodityCP.verifyCreate(mr1, c47, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		BaseModel commodity47 = c47.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityHistoryMap.put("commodity47", commodity47);
		//
		// 查看商品修改历史中有无条形码修改数据和库存改变数据
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity47.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		CommodityHistory commodityHistory = new CommodityHistory();
		boolean flag = false;
		for (int i = 0; i < CommodityHistoryList.size(); i++) {
			commodityHistory = (CommodityHistory) commodityHistory.parse1(CommodityHistoryList.get(i).toString());
			if (FieldName_Barcodes.equals(commodityHistory.getFieldName())) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "商品修改历史中没有条形码的修改数据");
		//
		flag = false;
		for (int j = 0; j < CommodityHistoryList.size(); j++) {
			commodityHistory = (CommodityHistory) commodityHistory.parse1(CommodityHistoryList.get(j).toString());
			if (FieldName_Stock.equals(commodityHistory.getFieldName())) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "商品修改历史中没有商品库存的修改数据");
	}

	@Test(dependsOnMethods = "createCommodityAndRetrieveCommodityHistoryAboutBarcodeAndStock")
	public void deleteCommodityAndRetrieveCommodityHistoryAboutBarcode() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "删除普通商品46后，查看在商品修改历史中有无条形码修改数据");

		// 删除普通商品46
		Commodity commodity46 = (Commodity) commodityHistoryMap.get("commodity46");
		BaseCommodityTest.deleteCommodityViaAction(commodity46, dbNameOfNewCompany, mvc, sessionBossOfNewCompany, mapBO);// 方法里已有检查点结果验证
		//
		// 查看在商品修改历史中有无条形码修改数据
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity46.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Barcodes)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 2, "商品修改历史中没有条形码的修改数据");
	}

	@Test(dependsOnMethods = "deleteCommodityAndRetrieveCommodityHistoryAboutBarcode")
	public void deleteCommodityBarcodeAndRetrieveCommodityHistoryAboutBarcode() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "删除普通商品48的某个条形码后，查看在商品修改历史中有无条形码修改数据");

		// 创建普通商品48
		Commodity c48 = BaseCommodityTest.DataInput.getCommodity();
		c48.setName(SIMPLE_COMMODITY + getCommodityOrder());
		c48.setMultiPackagingInfo(getBarcode() + ";" + c48.getPackageUnitID() + ";" + c48.getRefCommodityMultiple() + ";" + c48.getPriceRetail() + ";" + c48.getPriceVIP() + ";" + c48.getPriceWholesale() + ";" + c48.getName() + ";");
		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c48, sessionBossOfNewCompany) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr);
		CommodityCP.verifyCreate(mr, c48, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel commodity48 = c48.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityHistoryMap.put("commodity48", commodity48);
		//
		// 给商品48创建多一个条形码
		Barcodes b48 = new Barcodes();
		b48.setBarcode(String.valueOf(getBarcode()));
		b48.setOperatorStaffID(1);
		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBossOfNewCompany) //
						.param(Barcodes.field.getFIELD_NAME_barcode(), b48.getBarcode())//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48.getID()))//
						.param(Barcodes.field.getFIELD_NAME_operatorStaffID(), String.valueOf(b48.getOperatorStaffID()))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		//
		// 根据商品ID查出其中一个barcode
		MvcResult mr2 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity48.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBossOfNewCompany)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> barcodesList = JsonPath.read(jsonObject, "$.barcodesList");
		Barcodes barcode48 = new Barcodes();
		boolean flag = false;
		for (int i = 0; i < barcodesList.size(); i++) {
			barcode48 = (Barcodes) b48.parse1(barcodesList.get(i).toString());
			if ((b48.getBarcode()).equals(barcode48.getBarcode())) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "创建出来的条形码与创建之前的不相等");
		//
		// 删除barcode
		MvcResult mr3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode48.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBossOfNewCompany)//
		)//
				.andExpect(status().isOk()).andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		//
		// 查看在商品修改历史中有无条形码修改数据
		MvcResult mr4 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Barcodes)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);
		//
		jsonObject = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 3, "商品历史中没有条形码修改数据");
	}

	@Test(dependsOnMethods = "deleteCommodityBarcodeAndRetrieveCommodityHistoryAboutBarcode")
	public void createCombinationCommodityAndRetrieveCommodityHistoryAboutBarcode() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "创建组合商品49(48+47)后，查看在商品修改历史中有无条形码修改数据");

		// 创建组合商品49(47+48)
		Commodity commodity47 = (Commodity) commodityHistoryMap.get("commodity47");
		Commodity commodity48 = (Commodity) commodityHistoryMap.get("commodity48");
		SubCommodity comm47 = new SubCommodity();
		comm47.setSubCommodityID(commodity47.getID());
		comm47.setSubCommodityNO(1);
		comm47.setPrice(20);
		//
		SubCommodity comm48 = new SubCommodity();
		comm48.setSubCommodityID(commodity48.getID());
		comm48.setSubCommodityNO(1);
		comm48.setPrice(20);
		//
		List<SubCommodity> ListCommodity = new ArrayList<SubCommodity>();
		ListCommodity.add(comm47);
		ListCommodity.add(comm48);
		//
		Commodity c49 = BaseCommodityTest.DataInput.getCommodity();
		c49.setShelfLife(0);
		c49.setPurchaseFlag(0);
		c49.setRuleOfPoint(0);
		c49.setListSlave1(ListCommodity);
		c49.setType(EnumCommodityType.ECT_Combination.getIndex());
		c49.setName(CombinationCommodity + getCommodityOrder());
		c49.setMultiPackagingInfo(getBarcode() + ";" + c49.getPackageUnitID() + ";" + c49.getRefCommodityMultiple() + ";" + c49.getPriceRetail() + ";" + c49.getPriceVIP() + ";" + c49.getPriceWholesale() + ";" + c49.getName() + ";");
		String json = JSONObject.fromObject(c49).toString();
		MvcResult mr2 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c49, sessionBossOfNewCompany) //
				.param(Commodity.field.getFIELD_NAME_subCommodityInfo(), json)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		CommodityCP.verifyCreate(mr2, c49, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel commodity49 = c49.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityHistoryMap.put("commodity49", commodity49);
		//
		// 查看在商品修改历史中有无条形码修改数据
		MvcResult mr3 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity49.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Barcodes)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		//
		jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 1, "商品历史中没有条形码修改数据");
	}

	@Test(dependsOnMethods = "createCombinationCommodityAndRetrieveCommodityHistoryAboutBarcode")
	public void deleteCombinationCommodityAndRetrieveCommodityHistoryAboutBarcode() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "删除组合商品49(48+47)后，查看在商品修改历史中有无条形码修改数据");

		// 删除组合商品49
		Commodity commodity49 = (Commodity) commodityHistoryMap.get("commodity49");
		BaseCommodityTest.deleteCommodityViaAction(commodity49, dbNameOfNewCompany, mvc, sessionBossOfNewCompany, mapBO);
		commodityHistoryMap.remove("commodity49");
		//
		// 查看在商品修改历史中有无条形码修改数据
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity49.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Barcodes)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 2, "商品历史中没有条形码修改数据");
	}

	@Test(dependsOnMethods = "deleteCombinationCommodityAndRetrieveCommodityHistoryAboutBarcode")
	public void createMultiPackagingCommodityAndRetrieveCommodityHistoryAboutBarcode() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "创建多包装商品50(ref=47)后，查看在商品修改历史中有无条形码修改数据");

		// 创建商品47的多包装50
		Commodity commodity47 = (Commodity) commodityHistoryMap.get("commodity47");
		Commodity c47 = BaseCommodityTest.DataInput.getCommodity();
		c47.setnOStart(10);
		c47.setPurchasingPriceStart(10);
		c47.setName(commodity47.getName());
		c47.setID(commodity47.getID());
		//
		Commodity c50 = BaseCommodityTest.DataInput.getCommodity();
		c50.setRefCommodityMultiple(2);
		c50.setPackageUnitID(2);
		c50.setName(MultiPackagingCommodity + getCommodityOrder());
		//
		c47.setMultiPackagingInfo(getBarcode() + "," + getBarcode() + ";" + c47.getPackageUnitID() + "," + c50.getPackageUnitID() + ";" + c47.getRefCommodityMultiple() + "," + c50.getRefCommodityMultiple() + ";" + c47.getPriceRetail() + ","
				+ c50.getPriceRetail() + ";" + c47.getPriceVIP() + "," + c50.getPriceVIP() + ";" + c47.getPriceWholesale() + "," + c50.getPriceWholesale() + ";" + c47.getName() + "," + c50.getName() + ";");
		//
		Commodity commodity47Update = updateCommodity(commodity47, c47, dbNameOfNewCompany);// 方法里有检查点结果验证
		commodityHistoryMap.replace("commodity47", commodity47Update);
		//
		// 查出多包装50的ID
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_name() + "=" + c50.getName())//
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> multipackagingCommodityList = JsonPath.read(jsonObject, "$.objectList[*].listMultiPackageCommodity[*]");
		Commodity commodity50 = new Commodity();
		boolean flag = false;
		for (int i = 0; i < multipackagingCommodityList.size(); i++) {
			commodity50 = (Commodity) commodity50.parse1(multipackagingCommodityList.get(i).toString());
			if ((c50.getName()).equals(commodity50.getName())) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "查询商品失败");
		commodityHistoryMap.put("commodity50", commodity50);
		//
		// 查看在商品修改历史中有无条形码修改数据
		MvcResult mr3 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity50.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Barcodes)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		//
		jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 1, "商品历史中没有条形码修改数据");
	}

	@Test(dependsOnMethods = "createMultiPackagingCommodityAndRetrieveCommodityHistoryAboutBarcode")
	public void deleteMultiPackagingCommodityAndRetrieveCommodityHistoryAboutBarcode() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "删除多包装商品50(ref=47)后，查看在商品修改历史中有无条形码修改数据");

		// 删除多包装50
		Commodity commodity50 = (Commodity) commodityHistoryMap.get("commodity50");
		BaseCommodityTest.deleteCommodityViaAction(commodity50, dbNameOfNewCompany, mvc, sessionBossOfNewCompany, mapBO);
		commodityHistoryMap.remove("commodity50");
		//
		// 查看在商品修改历史中有无条形码修改数据
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity50.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Barcodes)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 2, "商品历史中没有条形码修改数据");
	}

	@Test(dependsOnMethods = "deleteMultiPackagingCommodityAndRetrieveCommodityHistoryAboutBarcode")
	public void updateBarcodeAndRetrieveCommodityHistoryAboutBarcode() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "修改普通商品48条形码,查看商品48的修改记录");

		// 查询出商品48的条形码
		Commodity commodity48 = (Commodity) commodityHistoryMap.get("commodity48");
		MvcResult mr = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity48.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBossOfNewCompany)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<?> barcodesList = JsonPath.read(jsonObject, "$.barcodesList");
		Barcodes barcode48 = new Barcodes();
		barcode48 = (Barcodes) barcode48.parse1(barcodesList.get(0).toString());
		//
		// 修改条形码
		barcode48.setBarcode("AAAAAAAAA");
		MvcResult mr2 = mvc.perform(post("/barcodesSync/updateEx.bx")//
				.param(Barcodes.field.getFIELD_NAME_barcode(), barcode48.getBarcode())//
				.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48.getID()))//
				.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode48.getID()))//
				.param(Barcodes.field.getFIELD_NAME_returnObject(), "1")//
				.contentType(MediaType.APPLICATION_JSON)//
				.session((MockHttpSession) sessionBossOfNewCompany))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr2);
		//
		// 查看商品48的修改记录
		MvcResult mr3 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Barcodes)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		//
		jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 2, "商品历史中没有条形码修改数据");
	}

	@Test(dependsOnMethods = "updateBarcodeAndRetrieveCommodityHistoryAboutBarcode")
	public void updateCommodityNameAndRetrieveCommodityHistoryAboutName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "修改普通商品48名称,查看商品48的修改记录");

		// 修改普通商品48名称
		Commodity commodity48 = (Commodity) commodityHistoryMap.get("commodity48");
		Commodity c48Update = (Commodity) commodity48.clone();
		c48Update.setName("修改" + commodity48.getName());
		c48Update.setMultiPackagingInfo(getBarcode() + ";" + c48Update.getPackageUnitID() + ";" + c48Update.getRefCommodityMultiple() + ";" + c48Update.getPriceRetail() + ";" + c48Update.getPriceVIP() + ";" + c48Update.getPriceWholesale()
				+ ";" + c48Update.getName() + ";");
		//
		BaseModel commodity48Update = updateCommodity(commodity48, c48Update, dbNameOfNewCompany);
		commodityHistoryMap.replace("commodity48", commodity48Update);
		//
		// 查看商品48的修改记录
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_CommodityName)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 1, "商品历史中没有商品名称修改数据");
	}

	@Test(dependsOnMethods = "updateCommodityNameAndRetrieveCommodityHistoryAboutName")
	public void updateCommoditySpecificationAndRetrieveCommodityHistoryAboutSpecification() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "修改普通商品48规格,查看商品48的修改记录");

		// 修改普通商品48的规格
		Commodity commodity48 = (Commodity) commodityHistoryMap.get("commodity48");
		Commodity c48Update = (Commodity) commodity48.clone();
		c48Update.setSpecification("万克");
		c48Update.setMultiPackagingInfo(getBarcode() + ";" + c48Update.getPackageUnitID() + ";" + c48Update.getRefCommodityMultiple() + ";" + c48Update.getPriceRetail() + ";" + c48Update.getPriceVIP() + ";" + c48Update.getPriceWholesale()
				+ ";" + c48Update.getName() + ";");
		//
		BaseModel commodity48Update = updateCommodity(commodity48, c48Update, dbNameOfNewCompany);
		commodityHistoryMap.replace("commodity48", commodity48Update);
		//
		// 查看商品48的修改记录
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48Update.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Specification)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 1, "商品历史中没有商品规格修改数据");
	}

	@Test(dependsOnMethods = "updateCommoditySpecificationAndRetrieveCommodityHistoryAboutSpecification")
	public void updateCommodityPackageUnitAndRetrieveCommodityHistoryAboutPackageUnit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "修改普通商品48包装单位,查看商品48的修改记录");

		// 修改普通商品48的包装单位
		Commodity commodity48 = (Commodity) commodityHistoryMap.get("commodity48");
		Commodity c48Update = (Commodity) commodity48.clone();
		c48Update.setPackageUnitID(4);
		c48Update.setMultiPackagingInfo(getBarcode() + ";" + c48Update.getPackageUnitID() + ";" + c48Update.getRefCommodityMultiple() + ";" + c48Update.getPriceRetail() + ";" + c48Update.getPriceVIP() + ";" + c48Update.getPriceWholesale()
				+ ";" + c48Update.getName() + ";");
		//
		BaseModel commodity48Update = updateCommodity(commodity48, c48Update, dbNameOfNewCompany);
		commodityHistoryMap.replace("commodity48", commodity48Update);
		//
		// 查看商品48的修改记录
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_PackageUnit)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 1, "商品历史中没有商品包装单位修改数据");
	}

	@Test(dependsOnMethods = "updateCommodityPackageUnitAndRetrieveCommodityHistoryAboutPackageUnit")
	public void updateCommodityCategoryAndRetrieveCommodityHistoryAboutCategory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "修改普通商品48类别,查看商品48的修改记录");

		// 修改普通商品48的类别
		Commodity commodity48 = (Commodity) commodityHistoryMap.get("commodity48");
		Commodity c48Update = (Commodity) commodity48.clone();
		c48Update.setCategoryID(31);
		c48Update.setMultiPackagingInfo(getBarcode() + ";" + c48Update.getPackageUnitID() + ";" + c48Update.getRefCommodityMultiple() + ";" + c48Update.getPriceRetail() + ";" + c48Update.getPriceVIP() + ";" + c48Update.getPriceWholesale()
				+ ";" + c48Update.getName() + ";");
		//
		BaseModel commodity48Update = updateCommodity(commodity48, c48Update, dbNameOfNewCompany);
		commodityHistoryMap.replace("commodity48", commodity48Update);
		//
		// 查看商品48的修改记录
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Category)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 1, "商品历史中没有商品类别修改数据");
	}

	@Test(dependsOnMethods = "updateCommodityCategoryAndRetrieveCommodityHistoryAboutCategory")
	public void updateCommodityPriceRetailAndRetrieveCommodityHistoryAboutPriceRetail() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "修改普通商品48零售价,查看商品48的修改记录");

		// 修改普通商品48的零售价
		Commodity commodity48 = (Commodity) commodityHistoryMap.get("commodity48");
		Commodity c48Update = (Commodity) commodity48.clone();
		c48Update.setPriceRetail(26);
		c48Update.setMultiPackagingInfo(getBarcode() + ";" + c48Update.getPackageUnitID() + ";" + c48Update.getRefCommodityMultiple() + ";" + c48Update.getPriceRetail() + ";" + c48Update.getPriceVIP() + ";" + c48Update.getPriceWholesale()
				+ ";" + c48Update.getName() + ";");
		//
		BaseModel commodity48Update = updateCommodity(commodity48, c48Update, dbNameOfNewCompany);
		commodityHistoryMap.replace("commodity48", commodity48Update);
		//
		// 查看商品48的修改记录
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_PriceRetail)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 1, "商品历史中没有商品类别修改数据");
	}

	@Test(dependsOnMethods = "updateCommodityPriceRetailAndRetrieveCommodityHistoryAboutPriceRetail")
	public void CreateWarehousingCommodityAndRetrieveCommodityHistoryAboutStock() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "新建一个商品48的入库单并审核，查看商品48的库存修改记录");

		// 查询商品48的BarcodesID
		Commodity commodity48 = (Commodity) commodityHistoryMap.get("commodity48");
		MvcResult mr = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity48.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBossOfNewCompany)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<?> barcodesList = JsonPath.read(jsonObject, "$.barcodesList");
		Barcodes barcodes48 = new Barcodes();
		barcodes48 = (Barcodes) barcodes48.parse1(barcodesList.get(0).toString());
		commodityHistoryMap.put("barcodes48", barcodes48);
		//
		// 新建商品48的入库单
		Warehousing wh48 = new Warehousing();
		wh48.setProviderID(1);
		wh48.setWarehouseID(1);
		wh48.setStaffID(2);// 老板
		//
		MvcResult mr1 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(wh48.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(wh48.getWarehouseID())) //
						.param(CommodityIDs, String.valueOf(commodity48.getID())) //
						.param(WarehousingCommodityNOs, "4") //
						.param(CommodityPrices, String.valueOf(commodity48.getPriceRetail())) //
						.param(CommodityAmounts, "11.1") //
						.param(CommodityBarcodeIDs, String.valueOf(barcodes48.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBossOfNewCompany) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr1);
		WarehousingCP.verifyCreate(mr1, wh48, dbNameOfNewCompany);
		//
		jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Warehousing warehousing48 = (Warehousing) wh48.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityHistoryMap.put("warehousing48", warehousing48);
		//
		// 审核入库单
		// 审核入库单前，查询商品的F_NO，检查点判断入库单审核后F_NO是否正确计算
		DataSourceContextHolder.setDbName(dbNameOfNewCompany);
		List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing48);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
			Assert.assertTrue(false, "查询一个入库单失败或查询的入库单为空，错误码=" + warehousingBO.getLastErrorCode() + "，错误信息=" + warehousingBO.getLastErrorMessage());
		}
		List<BaseModel> whCommBm = bmList.get(1);
		List<WarehousingCommodity> whCommList = new ArrayList<>();
		StringBuffer commodityIDs = new StringBuffer();
		StringBuffer commodityBarcodeIDs = new StringBuffer();
		StringBuffer warehousingCommodityNOs = new StringBuffer();
		StringBuffer commodityPrices = new StringBuffer();
		StringBuffer commodityAmounts = new StringBuffer();
		for (BaseModel bm : whCommBm) {
			WarehousingCommodity whc = (WarehousingCommodity) bm;
			whCommList.add(whc);
			commodityIDs.append(whc.getCommodityID() + ",");
			commodityBarcodeIDs.append(whc.getBarcodeID() + ",");
			warehousingCommodityNOs.append(whc.getNO() + ",");
			commodityPrices.append(whc.getPrice() + ",");
			commodityAmounts.append(whc.getAmount() + ",");
		}
		List<Commodity> commList = new ArrayList<>();
		// 入库单有从表
		for (WarehousingCommodity whc : whCommList) {
			Commodity commodityCache = BaseCommodityTest.getCommodityCache(whc.getCommodityID(), dbNameOfNewCompany);
			commList.add(commodityCache);
		}

		MvcResult mr2 = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing48.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing48.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing48.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.param(CommodityIDs, commodityIDs.toString())//
						.param(CommodityBarcodeIDs, commodityBarcodeIDs.toString())//
						.param(WarehousingCommodityNOs, warehousingCommodityNOs.toString())//
						.param(CommodityPrices, commodityPrices.toString())//
						.param(CommodityAmounts, commodityAmounts.toString())//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBossOfNewCompany) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		WarehousingCP.verifyApprove(mr2, warehousing48, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbNameOfNewCompany, BaseRetailTradeTest.defaultShopID);
		//
		// 查看商品48的库存修改记录
		MvcResult mr3 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Stock)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		//
		jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 1, "商品历史中没有该商品库存的修改数据");
	}

	@Test(dependsOnMethods = "CreateWarehousingCommodityAndRetrieveCommodityHistoryAboutStock")
	public void CreateReturnCommodityAndRetrieveCommodityHistoryAboutStock() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "新建一个商品48的退货单并审核，查看商品48的库存修改记录");

		// 新建商品48的退货单
		Commodity commodity48 = (Commodity) commodityHistoryMap.get("commodity48");
		Barcodes barcodes48 = (Barcodes) commodityHistoryMap.get("barcodes48");
		ReturnCommoditySheet rcs48 = new ReturnCommoditySheet();
		rcs48.setProviderID(1);
		rcs48.setStaffID(2);
		//
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(rcs48.getProviderID())) //
				.param(CommodityIDs, String.valueOf(commodity48.getID())) //
				.param(ReturnCommoditySheetCommodityNOs, "4") //
				.param(CommodityPrices, "11.1") //
				.param(ReturnCommoditySheetCommoditySpecifications, commodity48.getSpecification()) //
				.param(CommodityBarcodeIDs, String.valueOf(barcodes48.getID())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBossOfNewCompany)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr);
		ReturnCommoditySheetCP.verifyCreate(mr, rcs48);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet returnCommoditySheet48 = (ReturnCommoditySheet) rcs48.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// 从缓存把要审核的退货单商品拿出来
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commA = (Commodity) CacheManager.getCache(dbNameOfNewCompany, EnumCacheType.ECT_Commodity).read1(commodity48.getID(), BaseBO.SYSTEM, ecOut, dbNameOfNewCompany);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "查找商品失败！");
		}
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		commListBeforeApprove.add(commA);
		List<Warehousing> warehousingList = getInfoBeforeApprove(commListBeforeApprove, dbNameOfNewCompany);
		// 审核退货单
		MvcResult mr2 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(returnCommoditySheet48.getID())) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(returnCommoditySheet48.getProviderID())) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "0") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr2);
		ReturnCommoditySheetCP.verifyApprove(mr2, returnCommoditySheet48, commodityMapper, commListBeforeApprove, returnCommoditySheetCommodityBO, dbNameOfNewCompany, warehousingList, warehousingMapper);
		//
		// 查看商品48的库存修改记录
		MvcResult mr3 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity48.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), FieldName_Stock)//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		//
		jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 2, "商品历史中没有该商品库存的修改数据");
	}

	@Test(dependsOnMethods = "CreateReturnCommodityAndRetrieveCommodityHistoryAboutStock")
	public void saleCommodityOnPOSAndRetrieveCommodityHistoryAboutStock() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "POS机销售商品48，查看商品48的库存修改记录");

	}

	@Test(dependsOnMethods = "saleCommodityOnPOSAndRetrieveCommodityHistoryAboutStock")
	public void returnCommodityOnPOSAndRetrieveCommodityHistoryAboutStock() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "POS机购买退货商品48，查看商品48的库存修改记录");

	}

	@Test(dependsOnMethods = "returnCommodityOnPOSAndRetrieveCommodityHistoryAboutStock")
	public void updateCommodityAndRetrieveCommodityHistory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "新建一个普通商品51，修改它的的商品条形码、商品名称、规格、包装单位、商品类别、零售价和商品库存（审核入库单），查看修改记录");

		// 新建一个普通商品51
		Commodity c51 = BaseCommodityTest.DataInput.getCommodity();
		c51.setName(SIMPLE_COMMODITY + getCommodityOrder());
		c51.setMultiPackagingInfo(getBarcode() + ";" + c51.getPackageUnitID() + ";" + c51.getRefCommodityMultiple() + ";" + c51.getPriceRetail() + ";" + c51.getPriceVIP() + ";" + c51.getPriceWholesale() + ";" + c51.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c51, sessionBossOfNewCompany) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		CommodityCP.verifyCreate(mr1, c51, posBO, purchasingOrderBO, messageBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany);
		//
		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Commodity commodity51 = (Commodity) c51.parse1(JsonPath.read(o, "$.object").toString());
		//
		// 查出商品51的条形码
		MvcResult mr = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity51.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBossOfNewCompany)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<?> barcodesList = JsonPath.read(jsonObject, "$.barcodesList");
		Barcodes barcode51 = new Barcodes();
		barcode51 = (Barcodes) barcode51.parse1(barcodesList.get(0).toString());
		//
		// 修改条形码
		barcode51.setBarcode("BBBBBBBBBB");
		MvcResult mr2 = mvc.perform(post("/barcodesSync/updateEx.bx")//
				.param(Barcodes.field.getFIELD_NAME_barcode(), barcode51.getBarcode())//
				.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(commodity51.getID()))//
				.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode51.getID()))//
				.param(Barcodes.field.getFIELD_NAME_returnObject(), "1")//
				.contentType(MediaType.APPLICATION_JSON)//
				.session((MockHttpSession) sessionBossOfNewCompany))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr2);
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Barcodes barcode51Update = (Barcodes) barcode51.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// 修改商品51的商品名称、规格、包装单位、商品类别、零售价
		Commodity c51Update = (Commodity) commodity51.clone();
		c51Update.setPackageUnitID(6);
		c51Update.setSpecification("公斤");
		c51Update.setName("修改" + commodity51.getName());
		c51Update.setCategoryID(32);
		c51Update.setPriceRetail(23.0);
		c51Update.setMultiPackagingInfo(getBarcode() + ";" + c51Update.getPackageUnitID() + ";" + c51Update.getRefCommodityMultiple() + ";" + c51Update.getPriceRetail() + ";" + c51Update.getPriceVIP() + ";" + c51Update.getPriceWholesale()
				+ ";" + c51Update.getName() + ";");
		//
		Commodity commodity51Update = updateCommodity(commodity51, c51Update, dbNameOfNewCompany);
		commodityHistoryMap.put("commodity51", commodity51Update);
		//
		// 新建入库单使商品51入库
		Warehousing wh51 = new Warehousing();
		wh51.setProviderID(1);
		wh51.setWarehouseID(1);
		wh51.setStaffID(2);
		//
		MvcResult mr4 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(wh51.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(wh51.getWarehouseID())) //
						.param(CommodityIDs, String.valueOf(commodity51Update.getID())) //
						.param(WarehousingCommodityNOs, "4") //
						.param(CommodityPrices, String.valueOf(commodity51Update.getPriceRetail())) //
						.param(CommodityAmounts, "11.1") //
						.param(CommodityBarcodeIDs, String.valueOf(barcode51Update.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBossOfNewCompany) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr4);
		WarehousingCP.verifyCreate(mr4, wh51, dbNameOfNewCompany);
		//
		jsonObject = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		Warehousing warehousing51 = (Warehousing) wh51.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// 审核入库单
		// 审核入库单前，查询商品的F_NO，检查点判断入库单审核后F_NO是否正确计算
		DataSourceContextHolder.setDbName(dbNameOfNewCompany);
		List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing51);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
			Assert.assertTrue(false, "查询一个入库单失败或查询的入库单为空，错误码=" + warehousingBO.getLastErrorCode() + "，错误信息=" + warehousingBO.getLastErrorMessage());
		}
		List<BaseModel> whCommBm = bmList.get(1);
		List<WarehousingCommodity> whCommList = new ArrayList<>();
		for (BaseModel bm : whCommBm) {
			WarehousingCommodity whc = (WarehousingCommodity) bm;
			whCommList.add(whc);
		}
		List<Commodity> commList = new ArrayList<>();
		// 入库单有从表
		for (WarehousingCommodity whc : whCommList) {
			Commodity commodityCache = BaseCommodityTest.getCommodityCache(whc.getCommodityID(), dbNameOfNewCompany);
			commList.add(commodityCache);
		}
		MvcResult mr5 = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing51.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing51.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing51.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBossOfNewCompany) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr5);
		WarehousingCP.verifyApprove(mr5, warehousing51, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbNameOfNewCompany, BaseRetailTradeTest.defaultShopID);
		//
		// 查看商品历史
		MvcResult mr6 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity51.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr6);
		//
		jsonObject = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 9, "商品历史中该商品的修改数据不足");
	}

	@Test(dependsOnMethods = "updateCommodityAndRetrieveCommodityHistory")
	public void updateCommodityAndRetrieveCommodityHistory2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "新建一个普通商品52，修改它的的商品条形码、商品名称、规格、包装单位、商品类别、零售价和商品库存（审核退货单），查看修改记录");

		// 新建一个普通商品52
		Commodity c52 = BaseCommodityTest.DataInput.getCommodity();
		c52.setName(SIMPLE_COMMODITY + getCommodityOrder());
		c52.setMultiPackagingInfo(getBarcode() + ";" + c52.getPackageUnitID() + ";" + c52.getRefCommodityMultiple() + ";" + c52.getPriceRetail() + ";" + c52.getPriceVIP() + ";" + c52.getPriceWholesale() + ";" + c52.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c52, sessionBossOfNewCompany) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		CommodityCP.verifyCreate(mr1, c52, posBO, purchasingOrderBO, messageBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbNameOfNewCompany);
		//
		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Commodity commodity52 = (Commodity) c52.parse1(JsonPath.read(o, "$.object").toString());
		//
		// 查出商品52的条形码
		MvcResult mr = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?" + Barcodes.field.getFIELD_NAME_commodityID() + "=" + commodity52.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBossOfNewCompany)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<?> barcodesList = JsonPath.read(jsonObject, "$.barcodesList");
		Barcodes barcode52 = new Barcodes();
		barcode52 = (Barcodes) barcode52.parse1(barcodesList.get(0).toString());
		//
		// 修改条形码
		barcode52.setBarcode("CCCCCCCCCCCC");
		MvcResult mr2 = mvc.perform(post("/barcodesSync/updateEx.bx")//
				.param(Barcodes.field.getFIELD_NAME_barcode(), barcode52.getBarcode())//
				.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(commodity52.getID()))//
				.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode52.getID()))//
				.param(Barcodes.field.getFIELD_NAME_returnObject(), "1")//
				.contentType(MediaType.APPLICATION_JSON)//
				.session((MockHttpSession) sessionBossOfNewCompany))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr2);
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Barcodes barcode52Update = (Barcodes) barcode52.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// 修改商品52的商品名称、规格、包装单位、商品类别、零售价
		Commodity c52Update = (Commodity) commodity52.clone();
		c52Update.setPackageUnitID(6);
		c52Update.setSpecification("公斤");
		c52Update.setName("修改" + commodity52.getName());
		c52Update.setCategoryID(32);
		c52Update.setPriceRetail(23.0);
		c52Update.setMultiPackagingInfo(getBarcode() + ";" + c52Update.getPackageUnitID() + ";" + c52Update.getRefCommodityMultiple() + ";" + c52Update.getPriceRetail() + ";" + c52Update.getPriceVIP() + ";" + c52Update.getPriceWholesale()
				+ ";" + c52Update.getName() + ";");
		//
		Commodity commodity52Update = updateCommodity(commodity52, c52Update, dbNameOfNewCompany);
		commodityHistoryMap.put("commodity52", commodity52Update);
		//

		// 新建入库单使商品52入库
		Warehousing wh52 = new Warehousing();
		wh52.setProviderID(1);
		wh52.setWarehouseID(1);
		wh52.setStaffID(2);
		//
		MvcResult mr4 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(wh52.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(wh52.getWarehouseID())) //
						.param(CommodityIDs, String.valueOf(commodity52Update.getID())) //
						.param(WarehousingCommodityNOs, "4") //
						.param(CommodityPrices, String.valueOf(commodity52Update.getPriceRetail())) //
						.param(CommodityAmounts, "11.1") //
						.param(CommodityBarcodeIDs, String.valueOf(barcode52Update.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBossOfNewCompany) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr4);
		WarehousingCP.verifyCreate(mr4, wh52, dbNameOfNewCompany);
		//
		jsonObject = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		Warehousing warehousing52 = (Warehousing) wh52.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// 审核入库单
		// 审核入库单前，查询商品的F_NO，检查点判断入库单审核后F_NO是否正确计算
		DataSourceContextHolder.setDbName(dbNameOfNewCompany);
		List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing52);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
			Assert.assertTrue(false, "查询一个入库单失败或查询的入库单为空，错误码=" + warehousingBO.getLastErrorCode() + "，错误信息=" + warehousingBO.getLastErrorMessage());
		}
		List<BaseModel> whCommBm = bmList.get(1);
		List<WarehousingCommodity> whCommList = new ArrayList<>();
		for (BaseModel bm : whCommBm) {
			WarehousingCommodity whc = (WarehousingCommodity) bm;
			whCommList.add(whc);
		}
		List<Commodity> commList = new ArrayList<>();
		// 入库单有从表
		for (WarehousingCommodity whc : whCommList) {
			Commodity commodityCache = BaseCommodityTest.getCommodityCache(whc.getCommodityID(), dbNameOfNewCompany);
			commList.add(commodityCache);
		}
		MvcResult mr5 = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing52.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing52.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing52.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBossOfNewCompany) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr5);
		WarehousingCP.verifyApprove(mr5, warehousing52, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbNameOfNewCompany, BaseRetailTradeTest.defaultShopID);

		// 新建退货单使商品52退货
		ReturnCommoditySheet rcs52 = new ReturnCommoditySheet();
		rcs52.setProviderID(1);
		rcs52.setStaffID(2);
		//
		MvcResult mr6 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(rcs52.getProviderID())) //
				.param(CommodityIDs, String.valueOf(commodity52.getID())) //
				.param(ReturnCommoditySheetCommodityNOs, "4") //
				.param(CommodityPrices, "11.1") //
				.param(ReturnCommoditySheetCommoditySpecifications, commodity52.getSpecification()) //
				.param(CommodityBarcodeIDs, String.valueOf(barcode52Update.getID())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBossOfNewCompany)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 检查点
		Shared.checkJSONErrorCode(mr6);
		ReturnCommoditySheetCP.verifyCreate(mr6, rcs52);
		//
		jsonObject = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		ReturnCommoditySheet returnCommoditySheet52 = (ReturnCommoditySheet) rcs52.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// 从缓存把要审核的退货单商品拿出来
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commA = (Commodity) CacheManager.getCache(dbNameOfNewCompany, EnumCacheType.ECT_Commodity).read1(commodity52.getID(), BaseBO.SYSTEM, ecOut, dbNameOfNewCompany);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "查找商品失败！");
		}
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		commListBeforeApprove.add(commA);
		List<Warehousing> warehousingList = getInfoBeforeApprove(commListBeforeApprove, dbNameOfNewCompany);
		// 审核退货单
		MvcResult mr7 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(returnCommoditySheet52.getID())) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(returnCommoditySheet52.getProviderID())) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "0") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 检查点0
		Shared.checkJSONErrorCode(mr7);
		ReturnCommoditySheetCP.verifyApprove(mr7, returnCommoditySheet52, commodityMapper, commListBeforeApprove, returnCommoditySheetCommodityBO, dbNameOfNewCompany, warehousingList, warehousingMapper);
		//
		// 查看商品历史
		MvcResult mr8 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), String.valueOf(commodity52.getID()))//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "-1") //
						.session((MockHttpSession) sessionBossOfNewCompany)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr8);
		//
		jsonObject = JSONObject.fromObject(mr8.getResponse().getContentAsString());
		List<?> CommodityHistoryList = JsonPath.read(jsonObject, "$.objectList");
		Assert.assertTrue(CommodityHistoryList.size() >= 9, "商品历史中该商品的修改数据不足");
	}

	@Test(dependsOnMethods = "updateCommodityAndRetrieveCommodityHistory2")
	public void updateCommodityAndRetrieveCommodityHistory3() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "修改普通商品51商品条形码、商品名称、规格、包装单位、商品类别、零售价和商品库存（POS机购买操作），查看修改记录");

		// ...POS机端操作
	}

	@Test(dependsOnMethods = "updateCommodityAndRetrieveCommodityHistory3")
	public void updateCommodityAndRetrieveCommodityHistory4() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "修改普通商品52商品条形码、商品名称、规格、包装单位、商品类别、零售价和商品库存（POS机退货操作），查看修改记录");

		// ...POS机端操作
	}

	@Test(dependsOnMethods = "updateCommodityAndRetrieveCommodityHistory4")
	public void useLocalComputerCURDCommodityHistoryThenUseOtherComputerLoginAndRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

	}

	@Test(dependsOnMethods = "useLocalComputerCURDCommodityHistoryThenUseOtherComputerLoginAndRetrieve")
	public void useTwoOrMoreComputerLoginAtTheSameTimeAndCURDCommodityHistory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");

	}

	private List<Warehousing> getInfoBeforeApprove(List<Commodity> commList, String dbName) {
		List<Warehousing> warehousingList = new ArrayList<>();
		Warehousing wsBeforeApprove = null;
		for (Commodity comm : commList) {
			if (comm.getCurrentWarehousingID() == 0) {
				Warehousing warehousingSheet = new Warehousing();
				warehousingSheet.setID(BaseAction.INVALID_ID);
				warehousingSheet.setProviderID(BaseAction.INVALID_ID);
				warehousingSheet.setWarehouseID(BaseAction.INVALID_ID);
				warehousingSheet.setStaffID(BaseAction.INVALID_ID);
				warehousingSheet.setPurchasingOrderID(BaseAction.INVALID_ID);
				warehousingSheet.setPageIndex(BaseAction.PAGE_StartIndex);
				warehousingSheet.setPageSize(BaseAction.PAGE_SIZE_Infinite);
				//
				Map<String, Object> params = warehousingSheet.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousingSheet);
				DataSourceContextHolder.setDbName(dbName);
				List<BaseModel> ls = warehousingMapper.retrieveN(params);
				Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
				// 应该拿的是有该退货商品的最小入库单，而不是最小入库单
				Warehousing WarehousingMin = new Warehousing();
				for (int i = ls.size() - 1; i >= 0; i--) {
					WarehousingMin = (Warehousing) ls.get(i);// 从最小的入库ID进行遍历，找到
					WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
					warehousingCommodity.setWarehousingID(WarehousingMin.getID());
					warehousingCommodity.setPageIndex(BaseAction.PAGE_StartIndex);
					warehousingCommodity.setPageSize(BaseAction.PAGE_SIZE_Infinite);
					//
					Map<String, Object> paramsWhComm = warehousingCommodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousingCommodity);
					DataSourceContextHolder.setDbName(dbName);
					List<BaseModel> whBmCommList = warehousingCommodityMapper.retrieveN(paramsWhComm);
					Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsWhComm.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsWhComm.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
					List<WarehousingCommodity> whCommList = new ArrayList<>();
					for (BaseModel bm : whBmCommList) {
						WarehousingCommodity whComm = (WarehousingCommodity) bm;
						whCommList.add(whComm);
					}
					WarehousingMin.setListSlave1(whCommList);
					boolean finded = false;
					for (WarehousingCommodity whComm : whCommList) {
						if (whComm.getCommodityID() == comm.getID()) {
							wsBeforeApprove = WarehousingMin;
							finded = true;
						}
					}
					if (finded) {
						break;
					}
				}
			} else {
				Warehousing warehousingSheet = new Warehousing();
				warehousingSheet.setID(comm.getCurrentWarehousingID());
				Map<String, Object> params = warehousingSheet.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, warehousingSheet);
				DataSourceContextHolder.setDbName(dbName);
				List<List<BaseModel>> bmList = (List<List<BaseModel>>) warehousingMapper.retrieve1Ex(params);
				// 入库单ID存在不连续的情况，如果有该入库单
				Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
				wsBeforeApprove = (Warehousing) bmList.get(0).get(0);
				List<BaseModel> whCommBmList = bmList.get(1);
				List<WarehousingCommodity> whCommDBList = new ArrayList<>();
				for (BaseModel bm : whCommBmList) {
					WarehousingCommodity wsComm = (WarehousingCommodity) bm;
					whCommDBList.add(wsComm);
				}
				wsBeforeApprove.setListSlave1(whCommDBList);
			}
			Assert.assertTrue(wsBeforeApprove != null, "要退货的商品找不到对应的入库单");
			warehousingList.add(wsBeforeApprove);
		}
		return warehousingList;
	}

	@SuppressWarnings("unchecked")
	private List<Commodity> getMultioleCommodityList(Commodity commodity, String dbName) {
		DataSourceContextHolder.setDbName(dbName);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodity);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "获取多包装商品 DB数据错误！");
		}
		return listMultiPackageCommodity;
	}

	private Commodity updateCommodity(Commodity commodity, Commodity cUpdate, String dbName) throws Exception, UnsupportedEncodingException {
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodity.getID(), dbName, commodityHistoryBO);
		// 获取多包装商品的修改状况
		lspu = new ArrayList<PackageUnit>();
		lspuCreated = new ArrayList<PackageUnit>();
		lspuUpdated = new ArrayList<PackageUnit>();
		lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(cUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, dbName, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		// 获取修改前的多包装信息
		List<Commodity> commList = getMultioleCommodityList(commodity, dbName);
		MvcResult mr3 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, cUpdate, sessionBossOfNewCompany) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证 ： 检查错误码
		Shared.checkJSONErrorCode(mr3);
		CommodityCP.verifyUpdate(mr3, cUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, commList, dbName, null);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		Commodity commodityUpdate = (Commodity) cUpdate.parse1(jsonObject.getString("object"));
		return commodityUpdate;
	}

	private long getBarcode() {
		return barcode.incrementAndGet();
	}

	private int getCommodityOrder() {
		return commodityOrder.incrementAndGet();
	}
}
