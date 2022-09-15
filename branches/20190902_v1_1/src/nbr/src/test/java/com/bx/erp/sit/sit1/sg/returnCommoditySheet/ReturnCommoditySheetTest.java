package com.bx.erp.sit.sit1.sg.returnCommoditySheet;

import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.commodity.CommoditySyncAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ReturnCommoditySheet.EnumStatusReturnCommoditySheet;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseReturnCommoditySheetTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.test.checkPoint.ReturnCommoditySheetCP;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebAppConfiguration
public class ReturnCommoditySheetTest extends BaseActionTest {

	private AtomicInteger order;
	protected AtomicLong barcode;
	private AtomicInteger commodityOrder;

	private int commodityID = 1; // ...临时写死的商品ID
	private int rcscNO100 = 100; // ...临时写死的退货数量
	private Map<String, BaseModel> returnCommoditySheetMap;
	protected String bossPhone = "";
	protected String companySN = "";
	protected String dbName = "";
	protected final String CommonCommodity = "普通商品";

	public static final String PATH = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\logo.png";

	/**
	 * 存放创建公司1时返回的MvcResult 由于做company的结果验证会第一次登录老板账号并修改密码之后删除售前账号
	 * 所以存起来以便测试完售前账号之后做company结果验证
	 */
	private static MvcResult mvcResult_Company;

	/** 检查staff手机号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_STAFF_PHONE = 1;

	/** 审核退货单时有没有修改操作 */
	public static final int NotModify = 0;// 直接审核
	public static final int Modified = 1;// 修改后审核

	@BeforeClass
	public void beforeClass() {
		super.setUp();

		order = new AtomicInteger();
		commodityOrder = new AtomicInteger();
		commodityOrder.set(78);
		barcode = new AtomicLong();
		barcode.set(6821423302513L);
		returnCommoditySheetMap = new HashMap<String, BaseModel>();

	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}

	@Test
	public void createCompanyAndRetrieveNData() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "新建门店后，查看数据");

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
		Company companyCreate = (Company) Shared.parse1Object(mr1, company, BaseAction.KEY_Object);
		company.setSN(companyCreate.getSN());
		companySN = companyCreate.getSN();
		returnCommoditySheetMap.put("company", company);
		// 由于结果验证会登录老板账号然后导致售前账号被删除，所以把MvcResult先存起来，到下下个用例再做结果验证吧
		mvcResult_Company = mr1;
		//
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, Shared.PASSWORD_DEFAULT, companySN);

		// 查看数据
		retrieveNReturnCommoditySheet(0, BaseAction.INVALID_STATUS, 0);
		// ...结果验证
	}

	@Test(dependsOnMethods = "createCompanyAndRetrieveNData")
	public void preSale() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "售前人员无法CRUD盘点相关的");

		Company company = (Company) returnCommoditySheetMap.get("company");
		sessionBoss = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, company.getSN());
		// 新公司无数据先创建一个商品
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName(CommonCommodity + commodityOrder);
		c1.setMultiPackagingInfo("qwertyuiop" + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c1, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
		// ...新公司无数据无法进行UD操作
		// 创建公司的结果验证
		CompanyCP.verifyCreate(mvcResult_Company, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, company);
		// 验证敏感信息是否为空
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, company.getSN(), company.getBossPhone(), company.getBossPassword());
		// 用老板账号登录session
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);

	}

	@Test(dependsOnMethods = "preSale")
	public void createReturnCommoditySheetAndApprove() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "新增退货单1，再审核该订单，关闭页面再打开");

		// 创建商品
		commodityID = BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, sessionBoss, mapBO, dbName).getID();
		BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, sessionBoss, mapBO, dbName);
		BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, sessionBoss, mapBO, dbName);

		// ... 创建退货单
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "箱";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// 审核退货单
		approveReturnCommoditySheet(rcs1, commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, dbName);
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAndApprove")
	public void createReturnCommoditySheetAndApproveAndRetrive1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "新增退货单2，再审核该订单，再根据状态查询该退货单");

		// 创建退货单
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "箱";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// 审核退货单
		ReturnCommoditySheet rcs2 = approveReturnCommoditySheet(rcs1, commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, dbName);
		// 查询退货单
		List<ReturnCommoditySheet> listRcs = retrieveNReturnCommoditySheet(rcs1.getProviderID(), EnumStatusReturnCommoditySheet.ESRCS_Approved.getIndex(), 0);
		for (ReturnCommoditySheet returnCommoditySheet : listRcs) {
			Assert.assertTrue(rcs2.getStatus() == returnCommoditySheet.getStatus());
		}
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAndApproveAndRetrive1")
	public void createReturnCommoditySheetAndUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "新增退货单3后，再修改该退货单");

		// 创建退货单
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "箱";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// 修改退货单
		commIDs = commodityID + ",3";
		rcscNOs = rcscNO100 + ",100";
		commPrices = "0.0" + ",0.0";
		rcscSpecifications = "毫升,毫升";
		barcodeIDs = "1,3";
		updateReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs1);
	}

	protected void updateReturnCommoditySheet(String commIDs, String rcscNOs, String commPrices, String rcscSpecifications, String barcodeIDs, ReturnCommoditySheet rcs1) throws Exception, UnsupportedEncodingException {
		MvcResult mr2 = mvc.perform(//
				post("/returnCommoditySheet/updateEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(rcs1.getID())) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(rcs1.getProviderID())) //
						.param("commIDs", commIDs) //
						.param("rcscNOs", rcscNOs) //
						.param("commPrices", commPrices) //
						.param("rcscSpecifications", rcscSpecifications) //
						.param("barcodeIDs", barcodeIDs) //
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// 修改退货单的检查点
		ReturnCommoditySheetCP.verifyUpdate(mr2, rcs1);
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAndUpdate")
	public void createReturnCommoditySheetAnApprove() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "新增退货单4成功后，进行修改(未点击保存按钮)，直接点审核按钮");

		// 创建退货单
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "箱";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		rcs1.setbReturnCommodityListIsModified(NotModify);
		// 审核退货单
		commIDs = commodityID + ",3";
		rcscNOs = rcscNO100 + ",100";
		commPrices = "0.0" + ",0.0";
		rcscSpecifications = "毫升,毫升";
		barcodeIDs = "1,5";
		approveReturnCommoditySheet(rcs1, commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, dbName);
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAnApprove")
	public void createReturnCommoditySheetAndUpdate_Approve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "新增退货单5成功后，进行修改退货单。再进行审核");

		// 创建退货单
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "箱";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		rcs1.setbReturnCommodityListIsModified(1);
		// 修改退货单
		commIDs = commodityID + ",3";
		rcscNOs = rcscNO100 + ",100";
		commPrices = "0.0" + ",0.0";
		rcscSpecifications = "毫升,毫升";
		barcodeIDs = "1,3";
		updateReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs1);
		// 审核退货单
		approveReturnCommoditySheet(rcs1, commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, dbName);
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAndUpdate_Approve")
	public void createReturnCommoditySheetAndRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "新增退货单6，根据条件进行查询");

		// 创建退货单
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "箱";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// 根据状态进行查询
		List<ReturnCommoditySheet> listRcs = retrieveNReturnCommoditySheet(rcs1.getProviderID(), EnumStatusReturnCommoditySheet.ESRCS_ToApprove.getIndex(), rcs1.getStaffID());
		Assert.assertTrue(listRcs != null && listRcs.size() != 0, "查询出来的数据不正确");
		for (ReturnCommoditySheet returnCommoditySheet : listRcs) {
			Assert.assertTrue(rcs1.getStatus() == returnCommoditySheet.getStatus());
			Assert.assertTrue(rcs1.getStaffID() == returnCommoditySheet.getStaffID());
			Assert.assertTrue(rcs1.getProviderID() == returnCommoditySheet.getProviderID());
		}
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAndRetrieve")
	public void flipAndRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "翻页后，再进行条件查询");

		// 创建退货单
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "箱";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// 翻页
		RNPageIndex();
		// 根据状态进行查询
		List<ReturnCommoditySheet> listRcs = retrieveNReturnCommoditySheet(rcs1.getProviderID(), EnumStatusReturnCommoditySheet.ESRCS_ToApprove.getIndex(), rcs1.getStaffID());
		Assert.assertTrue(listRcs != null && listRcs.size() != 0, "查询出来的数据不正确");
		for (ReturnCommoditySheet returnCommoditySheet : listRcs) {
			Assert.assertTrue(rcs1.getStatus() == returnCommoditySheet.getStatus());
			Assert.assertTrue(rcs1.getStaffID() == returnCommoditySheet.getStaffID());
			Assert.assertTrue(rcs1.getProviderID() == returnCommoditySheet.getProviderID());
		}
	}

	protected void RNPageIndex() throws Exception {
		MvcResult mr = mvc.perform( //
				post("/returnCommoditySheet/retrieveNEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_pageIndex(), "2") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);
	}

	@Test(dependsOnMethods = "flipAndRetrieve")
	public void flipAndCreateReturnCommoditySheet() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "翻页后，再新增退货单7");

		// ... 翻页
		RNPageIndex();
		// 创建退货单
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "箱";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
	}

	@Test(dependsOnMethods = "flipAndCreateReturnCommoditySheet")
	public void flipAndUpdateReturnCommoditySheet() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "翻页后，再修改退货单7");

		// 创建退货单
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "箱";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// ... 翻页
		RNPageIndex();
		// 修改退货单
		commIDs = commodityID + ",3";
		rcscNOs = rcscNO100 + ",100";
		commPrices = "0.0" + ",0.0";
		rcscSpecifications = "毫升,毫升";
		barcodeIDs = "1,3";
		updateReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs1);
	}

	@Test(dependsOnMethods = "flipAndUpdateReturnCommoditySheet")
	public void onClickCreateAndPreserve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "点击新建(不传入商品以及供应商)，再点击保存按钮");

		// 创建退货单
		MvcResult mr = null;
		try {
			mr = mvc.perform(//
					post("/returnCommoditySheet/createEx.bx") //
							// .param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "0") //
							// .param("commIDs", String.valueOf(commodityID)) //
							.param("rcscNOs", String.valueOf(rcscNO100)) //
							.param("commPrices", "0.0") //
							.param("rcscSpecifications", "箱") //
							.param("barcodeIDs", "1") //
							.session((MockHttpSession) sessionBoss)//
							.contentType(MediaType.APPLICATION_JSON) //
			)//
					.andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			assertNull(mr);
		}
	}

	@Test(dependsOnMethods = "onClickCreateAndPreserve")
	public void updateAndStatus() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "修改未审核的退货单时，点击取消按钮");

		// 在页面的操作，未调用到nbr的接口
	}

	@Test(dependsOnMethods = "updateAndStatus")
	public void CRUDInTowMachineToDo() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

		// 在两台机器或者两台以上机器同时登录nbr进行CRUD操作
	}

	@Test(dependsOnMethods = "updateAndStatus")
	public void CRUDInTowMachineToDo2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");
		// 在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过
	}

	// SIT1_nbr_SG_ReturnCommoditySheet_18
	@Test(dependsOnMethods = "CRUDInTowMachineToDo2")
	public void RetrieveN1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "根据商品名称查询(未审核的)");

		// 创建单品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName(CommonCommodity + commodityOrder);
		c.setMultiPackagingInfo(barcode + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcodes = retrieveNBarcodes(commCreate);
		returnCommoditySheetMap.put("commodity56", commCreate);
		returnCommoditySheetMap.put("barcode56", barcodes);
		// 创建退货单
		String commIDs = String.valueOf(commCreate.getID());
		String rcscNOs = "4";
		String commPrices = "11.1";
		String rcscSpecifications = "箱";
		String barcodeIDs = String.valueOf(barcodes.getID());
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet returnCommoditySheet = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// 更改商品表商品名称
		Commodity updateComm = BaseCommodityTest.DataInput.getCommodity();
		updateComm.setLatestPricePurchase(0.0f);
		updateComm.setID(commCreate.getID());
		updateComm.setName("修改商品" + commodityOrder);
		updateComm.setProviderIDs("1");
		updateComm.setMultiPackagingInfo(barcode + ";1;1;1;8;8;" + updateComm.getName() + System.currentTimeMillis() % 1000000 + ";");
		updateCommodity(commCreate, updateComm, dbName);
		//
		List<ReturnCommoditySheet> listRcs = retrieveNReturnCommoditySheet(updateComm);
		Assert.assertTrue(listRcs != null, "获取的数据为null");
		boolean flag = false;
		for (int i = 0; i < listRcs.size(); i++) {
			if (listRcs.get(i).getID() == returnCommoditySheet.getID()) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "查询失败！");
		//
		List<ReturnCommoditySheet> listRcs2 = retrieveNReturnCommoditySheet(commCreate);
		Assert.assertTrue(listRcs2 != null, "获取的数据为null");
		flag = false;
		for (int i = 0; i < listRcs2.size(); i++) {
			if (listRcs2.get(i).getID() == returnCommoditySheet.getID()) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(!flag, "居然查询成功！");
	}

	// SIT1_nbr_SG_ReturnCommoditySheet_19
	@Test(dependsOnMethods = "RetrieveN1")
	public void RetrieveN2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "根据商品名称查询(已审核的)");

		// 创建单品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName(CommonCommodity + getCommodityOrder());
		c.setMultiPackagingInfo(getBarcode() + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcodes = retrieveNBarcodes(commCreate);
		returnCommoditySheetMap.put("commodity57", commCreate);
		returnCommoditySheetMap.put("barcode57", barcodes);
		// 创建退货单
		String commIDs = String.valueOf(commCreate.getID());
		String barcodeIDs = String.valueOf(barcodes.getID());
		String rcscNOs = "4";
		String commPrices = "11.1";
		String rcscSpecifications = "箱";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// 审核退货单
		approveReturnCommoditySheet(rcs1, commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, dbName);
		// 更改商品表商品名称
		Commodity updateComm = BaseCommodityTest.DataInput.getCommodity();
		updateComm.setLatestPricePurchase(0.0f);
		updateComm.setID(commCreate.getID());
		updateComm.setName("修改商品" + commodityOrder);
		updateComm.setProviderIDs("1");
		updateComm.setMultiPackagingInfo(barcode + ";1;1;1;8;8;" + updateComm.getName() + System.currentTimeMillis() % 1000000 + ";");
		updateCommodity(commCreate, updateComm, dbName);
		//
		List<ReturnCommoditySheet> listRcs = retrieveNReturnCommoditySheet(commCreate);
		boolean flag = false;
		for (int i = 0; i < listRcs.size(); i++) {
			if (listRcs.get(i).getID() == rcs1.getID()) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "查询失败！");
		//
		List<ReturnCommoditySheet> listRcs2 = retrieveNReturnCommoditySheet(updateComm);
		flag = false;
		for (int i = 0; i < listRcs2.size(); i++) {
			if (listRcs2.get(i).getID() == rcs1.getID()) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(!flag, "居然查询成功！");
	}

	// SIT1_nbr_SG_ReturnCommoditySheet_20
	@Test(dependsOnMethods = "RetrieveN2")
	public void RetrieveN3() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "修改未审核的退货单时，翻页后，点击取消按钮");
		// 1、进入采购退货页面
		MvcResult mr = mvc.perform( //
				post("/returnCommoditySheet/retrieveNEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_pageIndex(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_pageSize(), "10") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);
		// 2、点击未审核的退货单，修改该退货单，（不点击保存）；
		// 3、进行翻页，提示是否放弃操作，点击取消；
		// 4、在点击列表上方的取消按钮；
	}

	protected Warehousing createWarehousing(Warehousing w, String commIDs, String commNOs, String commPrices, String amounts, String barcodeIDs, String dbName) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param("commIDs", commIDs) //
						.param("commNOs", commNOs) //
						.param("commPrices", commPrices) //
						.param("barcodeIDs", barcodeIDs) //
						.param("amounts", amounts) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 创建入库单的检查点
		WarehousingCP.verifyCreate(mr, w, dbName);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(JsonPath.read(o, "$.object").toString());

		return warehousing1;
	}

	protected Warehousing approveWarehousing(Warehousing w, String commIDs, String dbName) throws Exception, UnsupportedEncodingException {
		// 获取商品缓存（目的是拿到库存）
		Integer[] iaCommID = GeneralUtil.toIntArray(commIDs);
		List<Commodity> commList = new ArrayList<>();
		for (int i = 0; i < iaCommID.length; i++) {
			Commodity commodity = new Commodity();
			commodity.setID(iaCommID[i]);
			Commodity commodityCache = getCommodityCache(commodity.getID(), dbName);
			commList.add(commodityCache);
		}
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(w.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(JsonPath.read(o, "$.object").toString());
		// 审核入库单的检查点
		WarehousingCP.verifyApprove(mr, w, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName, BaseRetailTradeTest.defaultShopID);
		return warehousing1;
	}

	protected ReturnCommoditySheet approveReturnCommoditySheet(ReturnCommoditySheet rcs1, String commIDs, String rcscNOs, String commPrices, String rcscSpecifications, String barcodeIDs, String dbName)
			throws Exception, UnsupportedEncodingException {
		Integer[] commID = GeneralUtil.toIntArray(commIDs);
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		List<Warehousing> warehousingList = null;
		for (int i = 0; i < commID.length; i++) {
			Commodity comm = getCommodityCache(commID[i], dbName);
			commListBeforeApprove.add(comm);
		}
		warehousingList = BaseReturnCommoditySheetTest.getInfoBeforeApprove(commListBeforeApprove, warehousingMapper, warehousingCommodityMapper, dbName);
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), rcs1.getID() + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), rcs1.getProviderID() + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), rcs1.getbReturnCommodityListIsModified() + "") //
						.param("commIDs", commIDs) //
						.param("rcscNOs", rcscNOs) //
						.param("commPrices", commPrices) //
						.param("rcscSpecifications", rcscSpecifications) //
						.param("barcodeIDs", barcodeIDs) //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 审核退货单的检查点
		ReturnCommoditySheetCP.verifyApprove(mr, rcs1, commodityMapper, commListBeforeApprove, returnCommoditySheetCommodityBO, dbName, warehousingList, warehousingMapper);

		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet rcs2 = new ReturnCommoditySheet();
		ReturnCommoditySheet rcs3 = (ReturnCommoditySheet) rcs2.parse1(jsonObject.getJSONObject(BaseAction.KEY_Object).toString());

		return rcs3;
	}

	@SuppressWarnings("unchecked")
	protected Barcodes retrieveNBarcodes(Commodity commCreate) throws Exception, UnsupportedEncodingException {
		MvcResult mrl2 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?commodityID=" + commCreate.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
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

	protected List<ReturnCommoditySheet> retrieveNReturnCommoditySheet(Commodity commCreate) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), commCreate.getName())//
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<?> rcsJSONArray = JsonPath.read(o, "$.objectList");
		List<ReturnCommoditySheet> listRcs = new ArrayList<ReturnCommoditySheet>();
		for (int i = 0; i < rcsJSONArray.size(); i++) {
			ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
			returnCommoditySheet = (ReturnCommoditySheet) returnCommoditySheet.parse1(rcsJSONArray.get(i).toString());
			listRcs.add(returnCommoditySheet);
		}
		return listRcs;
	}

	private void updateCommodity(Commodity commodity, Commodity commodityUpdate, String dbName) throws Exception, UnsupportedEncodingException {
		// 获取商品缓存
		Commodity commodityCache = getCommodityCache(commodity.getID(), dbName);
		// 获取该商品修改前的商品历史数量
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodity.getID(), dbName, commodityHistoryBO);
		// 获取修改前的多包装信息
		List<Commodity> commList = getMultioleCommodityList(commodityCache, dbName);
		// 获取多包装商品的修改状况
		List<PackageUnit> lspu = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuCreated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuUpdated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commodityUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, dbName, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		MvcResult mrl = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commodityUpdate, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl);
		// 检查点
		CommodityCP.verifyUpdate(mrl, commodityUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, commList, dbName, null);
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

	private Commodity getCommodityCache(int commodityID, String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取商品失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		return commodityCache;
	}

	@SuppressWarnings("unchecked")
	protected List<ReturnCommoditySheet> retrieveNReturnCommoditySheet(int providerID, int status, int staffID) throws Exception, UnsupportedEncodingException {
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		String date1 = sdf3.format(DatetimeUtil.getDate(new Date(), -5));
		Thread.sleep(2000);
		String date2 = sdf3.format(DatetimeUtil.getDate(new Date(), +3));
		//
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(providerID)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(status)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_staffID(), String.valueOf(staffID)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date1(), date1) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date2(), date2) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		List<ReturnCommoditySheet> listRcs = (List<ReturnCommoditySheet>) rcs.parseN(jsonObject.getJSONArray(BaseAction.KEY_ObjectList));

		return listRcs;

	}

	protected ReturnCommoditySheet createReturnCommoditySheet(String commIDs, String rcscNOs, String commPrices, String rcscSpecifications, String barcodeIDs, ReturnCommoditySheet rcs) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(rcs.getProviderID())) //
						.param("commIDs", commIDs) //
						.param("rcscNOs", rcscNOs) //
						.param("commPrices", commPrices) //
						.param("rcscSpecifications", rcscSpecifications) //
						.param("barcodeIDs", barcodeIDs) //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		// 创建退货单的检查点
		ReturnCommoditySheetCP.verifyCreate(mr, rcs);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet rcs1 = new ReturnCommoditySheet();
		ReturnCommoditySheet rcs2 = (ReturnCommoditySheet) rcs1.parse1(jsonObject.getJSONObject(BaseAction.KEY_Object).toString());
		return rcs2;
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("当前使用的staff=" + staff);

		return staff;
	}

	private int getCommodityOrder() {
		return commodityOrder.incrementAndGet();
	}

	private long getBarcode() {
		return barcode.incrementAndGet();
	}
}
