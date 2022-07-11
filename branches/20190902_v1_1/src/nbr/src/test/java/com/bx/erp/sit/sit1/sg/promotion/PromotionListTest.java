package com.bx.erp.sit.sit1.sg.promotion;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.action.trade.promotion.BasePromotionTest;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.test.checkPoint.PromotionCP;
import com.bx.erp.util.DatetimeUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class PromotionListTest extends BaseActionTest {
	protected AtomicInteger order;
	protected AtomicInteger commodityOrder;
	protected AtomicLong barcode;
	protected final String CommonCommodity = "活动商品";
	protected SimpleDateFormat simpleDateFormat;
	private Map<String, BaseModel> promotionListMap;
	public final static int ReturnObject = 1;
	public final static int staffPreSale = 1;

	protected String bossPhone = "";
	protected String companySN = "";
	protected String dbName = "";
	// protected final String bossPhone = "15854320895";
	// protected final String bossPassword = "000000";
	// protected final String companySN = "668866";
	// protected final String dbName = "nbr";

	private EnumSyncCacheType esct = EnumSyncCacheType.ESCT_PromotionSyncInfo;

	@BeforeClass
	public void beforeClass() {
		super.setUp();

		order = new AtomicInteger();
		commodityOrder = new AtomicInteger();
		commodityOrder.set(1);
		barcode = new AtomicLong();
		barcode.set(6821423302828L);
		promotionListMap = new HashMap<String, BaseModel>();
		simpleDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

	}

	// SIT1_nbr_SG_PromotionList_1
	@Test
	public void openPromotionListPage() throws Exception { // 该测试主要是页面操作,并没有调用接口
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新店开张，打开查询满减优惠页面");

		// 创建新公司并查看数据（无数据）
		Company company = BaseCompanyTest.DataInput.getCompany();
		bossPhone = company.getBossPhone();
		dbName = company.getDbName();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		//
		MvcResult mr1 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// 结果验证
		CompanyCP.verifyCreate(mr1, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mr1, company);
		// 验证敏感信息是否为空
		BaseCompanyTest.checkSensitiveInformation(mr1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		Company companyCreate = new Company();
		companyCreate = (Company) companyCreate.parse1(jsonObject.getString(BaseAction.KEY_Object));
		company.setSN(companyCreate.getSN());
		companySN = companyCreate.getSN();
		promotionListMap.put("company", company);
		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, company.getSN(), company.getBossPhone(), company.getBossPassword());
		// 登录回原来的session
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
	}

	// SIT1_nbr_SG_PromotionList_2
	@Test(dependsOnMethods = "openPromotionListPage")
	public void createPromotionTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠活动后，关闭页面再打开");
		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 创建满减优惠活动1
		Promotion p = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "走过路过别错过", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr1 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoError);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr1, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
	}

	// SIT1_nbr_SG_PromotionList_3
	@Test(dependsOnMethods = "createPromotionTest")
	public void createPromotionAndCommodityTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠活动后，指定商品，关闭页面再打开");

		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 创建指定商品1
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName(CommonCommodity + commodityOrder);
		c1.setMultiPackagingInfo(barcode + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c1, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		// 检查点
		Company company = (Company) sessionBoss.getAttribute(EnumSession.SESSION_Company.getName());
		CommodityCP.verifyCreate(mr1, c1, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				company.getDbName());
		//
		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		int commID1 = JsonPath.read(o, "$." + BaseAction.KEY_Object + ".ID");
		// 创建指定商品的满减优惠活动2
		Promotion p = getPromotion(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), String.valueOf(commID1), "有钱出钱", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr2 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr2);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr2, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
	}

	// SIT1_nbr_SG_PromotionList_4
	@Test(dependsOnMethods = "createPromotionAndCommodityTest")
	public void createPromotionAndDeleteTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠活动，终止满减优惠活动，关闭页面再打开");

		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 创建满折优惠活动3
		Promotion p = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "有力出力", -1, 0.8, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DiscountOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr1 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr1, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
		//
		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		int promotionID = JsonPath.read(o, "$." + BaseAction.KEY_Object + ".ID"); // 使用新建的活动ID终止该活动
		Promotion promotionCreate = new Promotion();
		promotionCreate = (Promotion) promotionCreate.parse1(o.getString(BaseAction.KEY_Object));
		// 终止新建的满折优惠活动3
		MvcResult mr2 = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(promotionID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr2);
		//
		PromotionCP.verifyDelete(promotionCreate, dbName, promotionMapper, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID);
	}

	// SIT1_nbr_SG_PromotionList_5
	@Test(dependsOnMethods = "createPromotionAndDeleteTest")
	public void createPromotionAndRetrieveTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠活动后，根据状态来查询活动");

		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 新建满减优惠活动4
		Promotion p = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "大酬宾", 8, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
		// 根据状态查询所有的活动
		MvcResult mr1 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), String.valueOf(Promotion.ACTIVE))//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr1);
		// 根据状态查询未开始的活动
		MvcResult mr2 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), String.valueOf(Promotion.ACTIVE_ButNotYetStarted))//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoError);
		// 根据状态查询进行中的活动
		MvcResult mr3 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), String.valueOf(Promotion.ACTIVE_And_Working))//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3);
		// 根据状态查询已结束的活动
		MvcResult mr4 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), String.valueOf(Promotion.ACTIVE_ButEnded))//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr4);
		// 根据状态查询已删除的活动
		MvcResult mr5 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Deleted.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr5);
	}

	// SIT1_nbr_SG_PromotionList_6
	@Test(dependsOnMethods = "createPromotionAndRetrieveTest")
	public void deletePromotionAndRetrieveTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "终止满减优惠活动后，根据活动状态查询");

		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 新建满减优惠活动5
		Promotion p = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "任重而道远", 8, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		int promotionID = JsonPath.read(o, "$." + BaseAction.KEY_Object + ".ID"); // 使用新建的活动ID终止该活动
		Promotion promotionCreate = new Promotion();
		promotionCreate = (Promotion) promotionCreate.parse1(o.getString(BaseAction.KEY_Object));
		// 终止满减优惠活动5
		MvcResult mr1 = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(promotionID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		//
		PromotionCP.verifyDelete(promotionCreate, dbName, promotionMapper, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID);
		// 根据状态查询已删除的活动
		MvcResult mr2 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Deleted.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);
	}

	// SIT1_nbr_SG_PromotionList_7
	@Test(dependsOnMethods = "deletePromotionAndRetrieveTest")
	public void createPromotionAndRetrieveNByFieldsTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠活动后，根据活动名称/单号进行模糊搜索");

		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 新建满减优惠活动6
		Promotion p = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "优惠大酬宾", 16, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Promotion promotion = (Promotion) new Promotion().parse1(o.getString(BaseAction.KEY_Object));
		// 根据活动名称进行模糊查询
		MvcResult mr1 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), promotion.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<BaseModel> bmList1 = new Promotion().parseN(jsonObject1.getString(BaseAction.KEY_ObjectList));
		for (BaseModel bm : bmList1) {
			Promotion promotion1 = (Promotion) bm;
			if (promotion.compareTo(promotion1) != 0) {
				Assert.assertTrue(false, "测试失败！查询的促销跟创建的促销不相同");
			}
		}
		// 根据活动单号进行模糊查询
		MvcResult mr2 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), promotion.getSn())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr2);
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<BaseModel> bmList2 = new Promotion().parseN(jsonObject2.getString(BaseAction.KEY_ObjectList));
		for (BaseModel bm : bmList2) {
			Promotion promotion2 = (Promotion) bm;
			if (promotion.compareTo(promotion2) != 0) {
				Assert.assertTrue(false, "测试失败！查询的促销跟创建的促销不相同");
			}
		}
	}

	// SIT1_nbr_SG_PromotionList_8
	@Test(dependsOnMethods = "createPromotionAndRetrieveNByFieldsTest")
	public void delatePromotionAndRetrieveNByFieldsTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "终止满减优惠活动后，根据活动名称/单号进行模糊搜索查询");

		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 新建满减优惠活动7
		Promotion p = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "庆五一优惠活动", 20, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Promotion promotionCreate = (Promotion) new Promotion().parse1(o.getString(BaseAction.KEY_Object));
		// 终止满减优惠活动7
		MvcResult mr1 = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(promotionCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		PromotionCP.verifyDelete(promotionCreate, dbName, promotionMapper, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID);
		promotionCreate.setStatus(EnumStatusPromotion.ESP_Deleted.getIndex()); // 该促销已经终止，为了通过下面的断言需手动设置为终止状态
		// 根据活动名称进行模糊查询
		MvcResult mr2 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), promotionCreate.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr2);
		JSONObject jsonObject2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<BaseModel> bmList1 = new Promotion().parseN(jsonObject2.getString(BaseAction.KEY_ObjectList));
		for (BaseModel bm : bmList1) {
			Promotion promotion1 = (Promotion) bm;
			if (promotionCreate.compareTo(promotion1) != 0) {
				Assert.assertTrue(false, "测试失败！查询的促销跟创建的促销不相同");
			}
		}
		// 根据活动SN进行模糊查询
		MvcResult mr3 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), promotionCreate.getSn())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr3);
		JSONObject jsonObject3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<BaseModel> bmList2 = new Promotion().parseN(jsonObject3.getString(BaseAction.KEY_ObjectList));
		for (BaseModel bm : bmList2) {
			Promotion promotion2 = (Promotion) bm;
			if (promotionCreate.compareTo(promotion2) != 0) {
				Assert.assertTrue(false, "测试失败！查询的促销跟创建的促销不相同");
			}
		}
	}

	// SIT1_nbr_SG_PromotionList_9
	@Test(dependsOnMethods = "delatePromotionAndRetrieveNByFieldsTest")
	public void openCommodityListPage() { // 该测试主要是页面操作,并没有调用接口
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠活动时，点击商品弹窗里的新建商品按钮跳转到商品列表页");

		// ...ActionTest

		// ...结果验证
	}

	// SIT1_nbr_SG_PromotionList_10
	@Test(dependsOnMethods = "openCommodityListPage")
	public void nextPageAndRetrieveNByFieldsTest() throws Exception { // 该测试需要大于10条的数据，但是运行到当前测试方法只有7条数据，则增加4条数据(测试优惠活动1、测试优惠活动2、测试优惠活动3、测试优惠活动4)
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "进行翻页后，再进行模糊搜索");

		// 由于数据只有7条不能进行翻页操作，所以增加4条数据(测试优惠活动1、测试优惠活动2、测试优惠活动3、测试优惠活动4)
		// 新建满减优惠活动8(测试优惠活动1)
		Date date1 = new Date();
		String datetimeStart1 = simpleDateFormat.format(DatetimeUtil.getDays(date1, +2));
		String datetimeEnd1 = simpleDateFormat.format(DatetimeUtil.getDays(date1, +7));
		//
		Promotion p1 = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "测试优惠活动1", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart1), simpleDateFormat.parse(datetimeEnd1));
		//
		MvcResult mr1 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p1, datetimeStart1, datetimeEnd1, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr1, p1, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);

		// 新建满减优惠活动9(测试优惠活动2)
		Date date2 = new Date();
		String datetimeStart2 = simpleDateFormat.format(DatetimeUtil.getDays(date2, +2));
		String datetimeEnd2 = simpleDateFormat.format(DatetimeUtil.getDays(date2, +7));
		//
		Promotion p2 = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "测试优惠活动2", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart2), simpleDateFormat.parse(datetimeEnd2));
		//
		MvcResult mr2 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p2, datetimeStart2, datetimeEnd2, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr2);
		//
		Pos pos2 = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID2 = pos2.getID();
		// 检查点
		PromotionCP.verifyCreate(mr2, p2, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID2, promotionScopeMapper);

		// 新建满减优惠活动10(测试优惠活动3)
		Date date3 = new Date();
		String datetimeStart3 = simpleDateFormat.format(DatetimeUtil.getDays(date3, +2));
		String datetimeEnd3 = simpleDateFormat.format(DatetimeUtil.getDays(date3, +7));
		//
		Promotion p3 = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "测试优惠活动3", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart3), simpleDateFormat.parse(datetimeEnd3));
		//
		MvcResult mr3 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p3, datetimeStart3, datetimeEnd3, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr3);
		//
		Pos pos3 = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID3 = pos3.getID();
		// 检查点
		PromotionCP.verifyCreate(mr3, p3, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID3, promotionScopeMapper);

		// 新建满减优惠活动11(测试优惠活动4)
		Date date4 = new Date();
		String datetimeStart4 = simpleDateFormat.format(DatetimeUtil.getDays(date4, +2));
		String datetimeEnd4 = simpleDateFormat.format(DatetimeUtil.getDays(date4, +7));
		//
		Promotion p4 = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "测试优惠活动4", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart4), simpleDateFormat.parse(datetimeEnd4));
		//
		MvcResult mr4 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p4, datetimeStart4, datetimeEnd4, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr4);
		//
		Pos pos4 = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID4 = pos4.getID();
		// 检查点
		PromotionCP.verifyCreate(mr4, p4, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID4, promotionScopeMapper);
		JSONObject o4 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		String promotionName4 = JsonPath.read(o4, "$." + BaseAction.KEY_Object + ".name"); // 使用新建的活动名称进行模糊搜搜

		// 翻页操作
		MvcResult mr5 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_pageIndex(), "2")//
						.param(Promotion.field.getFIELD_NAME_pageSize(), "10")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr5);

		// 根据活动名称进行模糊查询
		MvcResult mr6 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), promotionName4)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr6);
	}

	// SIT1_nbr_SG_PromotionList_11
	@Test(dependsOnMethods = "nextPageAndRetrieveNByFieldsTest")
	public void addCommodityAndDeleteCommodityTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠活动时，添加指定商品，再删除指定商品");

		// 新建测试优惠活动12
		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		//
		Promotion p = getPromotion(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "庆六一优惠活动", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		// Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined); //
		// 指定了促销范围没有指定促销商品
		String o = mr.getResponse().getContentAsString();
		assertTrue(o.length() == 0, "测试失败！返回的结果不是期望的");
		// 创建促销失败，无需检查
	}

	// SIT1_nbr_SG_PromotionList_12
	@Test(dependsOnMethods = "addCommodityAndDeleteCommodityTest")
	public void createPromotionAndUpdateTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠活动后，修改活动的信息");

		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 新建满减优惠活动13
		Promotion p = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), "", "庆七一优惠活动", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr2 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr2);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr2, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
		// ...结果验证
		// 由于页面是修改不了满减优惠活动的信息的，所以并没有调用修改的接口
	}

	// SIT1_nbr_SG_PromotionList_13
	@Test(dependsOnMethods = "createPromotionAndUpdateTest")
	public void createPromotionAndDeleteCommodityTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠（添加指定商品）后，再删除指定商品");

		// 创建指定商品2
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setName(CommonCommodity + getCommodityOrder());
		c2.setMultiPackagingInfo(getBarcode() + ";" + c2.getPackageUnitID() + ";" + c2.getRefCommodityMultiple() + ";" + c2.getPriceRetail() + ";" + c2.getPriceVIP() + ";" + c2.getPriceWholesale() + ";" + c2.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c2, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		// 检查点
		Company company = (Company) sessionBoss.getAttribute(EnumSession.SESSION_Company.getName());
		CommodityCP.verifyCreate(mr1, c2, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				company.getDbName());
		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		int commID2 = JsonPath.read(o, "$." + BaseAction.KEY_Object + ".ID");
		//
		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 新建满减优惠活动14
		Promotion p = getPromotion(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), String.valueOf(commID2), "庆八一优惠活动", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr2 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr2);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr2, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);

		// 由于页面是删除指定商品时页面操作，并没有调用接口
	}

	// SIT1_nbr_SG_PromotionList_14
	@Test(dependsOnMethods = "createPromotionAndDeleteCommodityTest")
	public void RetrieveNByFieldsCommodityTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠，添加指定商品，在指定商品弹窗中模糊搜索商品进行添加");

		// 创建指定商品3
		Commodity c3 = BaseCommodityTest.DataInput.getCommodity();
		c3.setName(CommonCommodity + getCommodityOrder());
		c3.setMultiPackagingInfo(getBarcode() + ";" + c3.getPackageUnitID() + ";" + c3.getRefCommodityMultiple() + ";" + c3.getPriceRetail() + ";" + c3.getPriceVIP() + ";" + c3.getPriceWholesale() + ";" + c3.getName() + ";");
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c3, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证
		Shared.checkJSONErrorCode(mr1);
		// 检查点
		Company company = (Company) sessionBoss.getAttribute(EnumSession.SESSION_Company.getName());
		CommodityCP.verifyCreate(mr1, c3, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				company.getDbName());
		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		String commName3 = JsonPath.read(o, "$." + BaseAction.KEY_Object + ".name");
		// 模糊查询指定商品3
		MvcResult mr2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" //
						+ Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commName3)//
								.session((MockHttpSession) sessionBoss)//
								.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<Integer> commID4 = JsonPath.read(o2, "$." + BaseAction.KEY_ObjectList + "[*].commodity.ID");
		//
		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 新建满减优惠活动15
		Promotion p = getPromotion(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), String.valueOf(commID4.get(0)), "庆九一优惠活动", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr3 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr3);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr3, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
	}

	// SIT1_nbr_SG_PromotionList_15
	@Test(dependsOnMethods = "RetrieveNByFieldsCommodityTest")
	public void addAllCommodityTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "新建满减优惠，添加指定商品，在指定商品弹窗中点击全选商品，然后点击下一页全选商品");

		// 创建17条商品数据(指定商品4~指定商品20)
		Company company = (Company) sessionBoss.getAttribute(EnumSession.SESSION_Company.getName());
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		for (int i = 0; i < 17; i++) {
			c.setName(CommonCommodity + getCommodityOrder());
			c.setMultiPackagingInfo(getBarcode() + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
			MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionBoss) //
			).andExpect(status().isOk()).andDo(print()).andReturn(); //
			// 结果验证
			Shared.checkJSONErrorCode(mr1);
			// 检查点
			CommodityCP.verifyCreate(mr1, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
					categoryBO, company.getDbName());
		}

		// 模拟页面全选第一页商品
		MvcResult mr111 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?")//
						.param(Commodity.field.getFIELD_NAME_status(), "-1")//
						.param(Commodity.field.getFIELD_NAME_type(), "-1")//
						.param(Commodity.field.getFIELD_NAME_pageIndex(), "1")//
						.param(Commodity.field.getFIELD_NAME_pageSize(), "10")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr111);
		JSONObject o111 = JSONObject.fromObject(mr111.getResponse().getContentAsString());
		List<Integer> list111 = JsonPath.read(o111, "$." + BaseAction.KEY_ObjectList + "[*].commodity.ID");
		String commodityIDs = "";
		for (int id : list111) {
			commodityIDs += id + ",";
		}

		// 模拟页面全选第二页商品
		MvcResult mr222 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?")//
						.param(Commodity.field.getFIELD_NAME_status(), "-1")//
						.param(Commodity.field.getFIELD_NAME_type(), "-1")//
						.param(Commodity.field.getFIELD_NAME_pageIndex(), "2")//
						.param(Commodity.field.getFIELD_NAME_pageSize(), "10")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr222);
		JSONObject o222 = JSONObject.fromObject(mr222.getResponse().getContentAsString());
		List<Integer> list222 = JsonPath.read(o222, "$." + BaseAction.KEY_ObjectList + "[*].commodity.ID");
		for (int id : list222) {
			commodityIDs += id + ",";
		}

		// 新建满减优惠活动16
		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		//
		Promotion p = getPromotion(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex(), getStaffFromSession(sessionBoss).getID(), commodityIDs, "庆十一优惠活动", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr3 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr3);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		// 检查点
		PromotionCP.verifyCreate(mr3, p, dbName, posBO, esct, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
	}

	// SIT1_nbr_SG_PromotionList_16
	@Test(dependsOnMethods = "addAllCommodityTest")
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

		// ...ActionTest

		// ...结果验证
	}

	// SIT1_nbr_SG_PromotionList_17
	@Test(dependsOnMethods = "retrieveNTest")
	public void curdTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");

		// ...ActionTest

		// ...结果验证
	}

	// 老板已经登录过了，无法再使用售前账号
	// SIT1_nbr_SG_PromotionList_18
	@Test(dependsOnMethods = "curdTest")
	public void bxStaffTest() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PromotionList_", order, "售前人员无法对促销进行任何的CUD操作");

		Date date = new Date();
		String datetimeStart = simpleDateFormat.format(DatetimeUtil.getDays(date, 2));
		String datetimeEnd = simpleDateFormat.format(DatetimeUtil.getDays(date, +7));
		// 创建满减优惠活动17
		Promotion p = getPromotion(EnumPromotionScope.EPS_AllCommodities.getIndex(), staffPreSale, "1", "创建不了的满减优惠活动", 10, -1, 100, //
				EnumStatusPromotion.ESP_Active.getIndex(), EnumTypePromotion.ETP_DecreaseOnAmount.getIndex(), simpleDateFormat.parse(datetimeStart), simpleDateFormat.parse(datetimeEnd));
		//
		MvcResult mr1 = mvc.perform(BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p, datetimeStart, datetimeEnd, sessionBoss) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale))//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
		// 终止新建的满减优惠活动17
		MvcResult mr2 = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);

		// ...暂时未提供修改满减优惠活动的接口

		// 查询满减优惠活动17
		MvcResult mr3 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), "走过路过别错过")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr3);
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("当前使用的staff=" + staff);

		return staff;
	}

	private Promotion getPromotion(int scope, int staffID, String commodityID, String name, double excecutionAmount, double excecutionDiscount, double excecutionThreshold, int status, int type, Date datetimeStart, Date datetimeEnd) {
		Promotion p = new Promotion();
		p.setReturnObject(ReturnObject);
		p.setScope(scope);
		p.setStaff(staffID);
		p.setCommodityIDs(commodityID);
		p.setName(name);
		p.setExcecutionAmount(excecutionAmount);
		p.setExcecutionDiscount(excecutionDiscount);
		p.setExcecutionThreshold(excecutionThreshold);
		p.setStatus(status);
		p.setType(type);
		p.setDatetimeStart(datetimeStart);
		p.setDatetimeEnd(datetimeEnd);
		return p;
	}

	private long getBarcode() {
		return barcode.incrementAndGet();
	}

	private int getCommodityOrder() {
		return commodityOrder.incrementAndGet();
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

}
