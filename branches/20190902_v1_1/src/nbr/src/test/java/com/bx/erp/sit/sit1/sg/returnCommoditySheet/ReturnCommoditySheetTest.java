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

	private int commodityID = 1; // ...?????????????????????ID
	private int rcscNO100 = 100; // ...???????????????????????????
	private Map<String, BaseModel> returnCommoditySheetMap;
	protected String bossPhone = "";
	protected String companySN = "";
	protected String dbName = "";
	protected final String CommonCommodity = "????????????";

	public static final String PATH = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\logo.png";

	/**
	 * ??????????????????1????????????MvcResult ?????????company????????????????????????????????????????????????????????????????????????????????????
	 * ???????????????????????????????????????????????????company????????????
	 */
	private static MvcResult mvcResult_Company;

	/** ??????staff???????????????????????????????????? */
	public static final int CASE_CHECK_UNIQUE_STAFF_PHONE = 1;

	/** ??????????????????????????????????????? */
	public static final int NotModify = 0;// ????????????
	public static final int Modified = 1;// ???????????????

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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "??????????????????????????????");

		// ?????????????????????????????????????????????
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
		// ????????????????????????????????????????????????????????????????????????????????????MvcResult??????????????????????????????????????????????????????
		mvcResult_Company = mr1;
		//
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, Shared.PASSWORD_DEFAULT, companySN);

		// ????????????
		retrieveNReturnCommoditySheet(0, BaseAction.INVALID_STATUS, 0);
		// ...????????????
	}

	@Test(dependsOnMethods = "createCompanyAndRetrieveNData")
	public void preSale() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "??????????????????CRUD???????????????");

		Company company = (Company) returnCommoditySheetMap.get("company");
		sessionBoss = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, company.getSN());
		// ???????????????????????????????????????
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setName(CommonCommodity + commodityOrder);
		c1.setMultiPackagingInfo("qwertyuiop" + ";" + c1.getPackageUnitID() + ";" + c1.getRefCommodityMultiple() + ";" + c1.getPriceRetail() + ";" + c1.getPriceVIP() + ";" + c1.getPriceWholesale() + ";" + c1.getName() + ";");
		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c1, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
		// ...??????????????????????????????UD??????
		// ???????????????????????????
		CompanyCP.verifyCreate(mvcResult_Company, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, company);
		// ??????????????????????????????
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// ????????????????????????????????????????????????????????????????????????????????????????????????
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, company.getSN(), company.getBossPhone(), company.getBossPassword());
		// ?????????????????????session
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);

	}

	@Test(dependsOnMethods = "preSale")
	public void createReturnCommoditySheetAndApprove() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "???????????????1?????????????????????????????????????????????");

		// ????????????
		commodityID = BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, sessionBoss, mapBO, dbName).getID();
		BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, sessionBoss, mapBO, dbName);
		BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, sessionBoss, mapBO, dbName);

		// ... ???????????????
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "???";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// ???????????????
		approveReturnCommoditySheet(rcs1, commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, dbName);
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAndApprove")
	public void createReturnCommoditySheetAndApproveAndRetrive1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "???????????????2?????????????????????????????????????????????????????????");

		// ???????????????
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "???";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// ???????????????
		ReturnCommoditySheet rcs2 = approveReturnCommoditySheet(rcs1, commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, dbName);
		// ???????????????
		List<ReturnCommoditySheet> listRcs = retrieveNReturnCommoditySheet(rcs1.getProviderID(), EnumStatusReturnCommoditySheet.ESRCS_Approved.getIndex(), 0);
		for (ReturnCommoditySheet returnCommoditySheet : listRcs) {
			Assert.assertTrue(rcs2.getStatus() == returnCommoditySheet.getStatus());
		}
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAndApproveAndRetrive1")
	public void createReturnCommoditySheetAndUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "???????????????3???????????????????????????");

		// ???????????????
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "???";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// ???????????????
		commIDs = commodityID + ",3";
		rcscNOs = rcscNO100 + ",100";
		commPrices = "0.0" + ",0.0";
		rcscSpecifications = "??????,??????";
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
		// ???????????????????????????
		ReturnCommoditySheetCP.verifyUpdate(mr2, rcs1);
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAndUpdate")
	public void createReturnCommoditySheetAnApprove() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "???????????????4????????????????????????(?????????????????????)????????????????????????");

		// ???????????????
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "???";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		rcs1.setbReturnCommodityListIsModified(NotModify);
		// ???????????????
		commIDs = commodityID + ",3";
		rcscNOs = rcscNO100 + ",100";
		commPrices = "0.0" + ",0.0";
		rcscSpecifications = "??????,??????";
		barcodeIDs = "1,5";
		approveReturnCommoditySheet(rcs1, commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, dbName);
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAnApprove")
	public void createReturnCommoditySheetAndUpdate_Approve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "???????????????5???????????????????????????????????????????????????");

		// ???????????????
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "???";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		rcs1.setbReturnCommodityListIsModified(1);
		// ???????????????
		commIDs = commodityID + ",3";
		rcscNOs = rcscNO100 + ",100";
		commPrices = "0.0" + ",0.0";
		rcscSpecifications = "??????,??????";
		barcodeIDs = "1,3";
		updateReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs1);
		// ???????????????
		approveReturnCommoditySheet(rcs1, commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, dbName);
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAndUpdate_Approve")
	public void createReturnCommoditySheetAndRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "???????????????6???????????????????????????");

		// ???????????????
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "???";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// ????????????????????????
		List<ReturnCommoditySheet> listRcs = retrieveNReturnCommoditySheet(rcs1.getProviderID(), EnumStatusReturnCommoditySheet.ESRCS_ToApprove.getIndex(), rcs1.getStaffID());
		Assert.assertTrue(listRcs != null && listRcs.size() != 0, "??????????????????????????????");
		for (ReturnCommoditySheet returnCommoditySheet : listRcs) {
			Assert.assertTrue(rcs1.getStatus() == returnCommoditySheet.getStatus());
			Assert.assertTrue(rcs1.getStaffID() == returnCommoditySheet.getStaffID());
			Assert.assertTrue(rcs1.getProviderID() == returnCommoditySheet.getProviderID());
		}
	}

	@Test(dependsOnMethods = "createReturnCommoditySheetAndRetrieve")
	public void flipAndRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "?????????????????????????????????");

		// ???????????????
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "???";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// ??????
		RNPageIndex();
		// ????????????????????????
		List<ReturnCommoditySheet> listRcs = retrieveNReturnCommoditySheet(rcs1.getProviderID(), EnumStatusReturnCommoditySheet.ESRCS_ToApprove.getIndex(), rcs1.getStaffID());
		Assert.assertTrue(listRcs != null && listRcs.size() != 0, "??????????????????????????????");
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "??????????????????????????????7");

		// ... ??????
		RNPageIndex();
		// ???????????????
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "???";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
	}

	@Test(dependsOnMethods = "flipAndCreateReturnCommoditySheet")
	public void flipAndUpdateReturnCommoditySheet() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "??????????????????????????????7");

		// ???????????????
		String commIDs = String.valueOf(commodityID);
		String rcscNOs = String.valueOf(rcscNO100);
		String commPrices = "0.0";
		String rcscSpecifications = "???";
		String barcodeIDs = "1";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// ... ??????
		RNPageIndex();
		// ???????????????
		commIDs = commodityID + ",3";
		rcscNOs = rcscNO100 + ",100";
		commPrices = "0.0" + ",0.0";
		rcscSpecifications = "??????,??????";
		barcodeIDs = "1,3";
		updateReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs1);
	}

	@Test(dependsOnMethods = "flipAndUpdateReturnCommoditySheet")
	public void onClickCreateAndPreserve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "????????????(??????????????????????????????)????????????????????????");

		// ???????????????
		MvcResult mr = null;
		try {
			mr = mvc.perform(//
					post("/returnCommoditySheet/createEx.bx") //
							// .param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "0") //
							// .param("commIDs", String.valueOf(commodityID)) //
							.param("rcscNOs", String.valueOf(rcscNO100)) //
							.param("commPrices", "0.0") //
							.param("rcscSpecifications", "???") //
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "???????????????????????????????????????????????????");

		// ?????????????????????????????????nbr?????????
	}

	@Test(dependsOnMethods = "updateAndStatus")
	public void CRUDInTowMachineToDo() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "???????????????CRUD???????????????????????????nbr????????????????????????????????????????????????????????????");

		// ???????????????????????????????????????????????????nbr??????CRUD??????
	}

	@Test(dependsOnMethods = "updateAndStatus")
	public void CRUDInTowMachineToDo2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "???????????????????????????????????????????????????nbr??????CRUD???????????????????????????????????????????????????????????????????????????");
		// ???????????????????????????????????????????????????nbr??????CRUD???????????????????????????????????????????????????????????????????????????
	}

	// SIT1_nbr_SG_ReturnCommoditySheet_18
	@Test(dependsOnMethods = "CRUDInTowMachineToDo2")
	public void RetrieveN1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "????????????????????????(????????????)");

		// ????????????
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName(CommonCommodity + commodityOrder);
		c.setMultiPackagingInfo(barcode + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcodes = retrieveNBarcodes(commCreate);
		returnCommoditySheetMap.put("commodity56", commCreate);
		returnCommoditySheetMap.put("barcode56", barcodes);
		// ???????????????
		String commIDs = String.valueOf(commCreate.getID());
		String rcscNOs = "4";
		String commPrices = "11.1";
		String rcscSpecifications = "???";
		String barcodeIDs = String.valueOf(barcodes.getID());
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet returnCommoditySheet = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// ???????????????????????????
		Commodity updateComm = BaseCommodityTest.DataInput.getCommodity();
		updateComm.setLatestPricePurchase(0.0f);
		updateComm.setID(commCreate.getID());
		updateComm.setName("????????????" + commodityOrder);
		updateComm.setProviderIDs("1");
		updateComm.setMultiPackagingInfo(barcode + ";1;1;1;8;8;" + updateComm.getName() + System.currentTimeMillis() % 1000000 + ";");
		updateCommodity(commCreate, updateComm, dbName);
		//
		List<ReturnCommoditySheet> listRcs = retrieveNReturnCommoditySheet(updateComm);
		Assert.assertTrue(listRcs != null, "??????????????????null");
		boolean flag = false;
		for (int i = 0; i < listRcs.size(); i++) {
			if (listRcs.get(i).getID() == returnCommoditySheet.getID()) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "???????????????");
		//
		List<ReturnCommoditySheet> listRcs2 = retrieveNReturnCommoditySheet(commCreate);
		Assert.assertTrue(listRcs2 != null, "??????????????????null");
		flag = false;
		for (int i = 0; i < listRcs2.size(); i++) {
			if (listRcs2.get(i).getID() == returnCommoditySheet.getID()) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(!flag, "?????????????????????");
	}

	// SIT1_nbr_SG_ReturnCommoditySheet_19
	@Test(dependsOnMethods = "RetrieveN1")
	public void RetrieveN2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "????????????????????????(????????????)");

		// ????????????
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName(CommonCommodity + getCommodityOrder());
		c.setMultiPackagingInfo(getBarcode() + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcodes = retrieveNBarcodes(commCreate);
		returnCommoditySheetMap.put("commodity57", commCreate);
		returnCommoditySheetMap.put("barcode57", barcodes);
		// ???????????????
		String commIDs = String.valueOf(commCreate.getID());
		String barcodeIDs = String.valueOf(barcodes.getID());
		String rcscNOs = "4";
		String commPrices = "11.1";
		String rcscSpecifications = "???";
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setProviderID(1);
		rcs.setStaffID(getStaffFromSession(sessionBoss).getID());
		ReturnCommoditySheet rcs1 = createReturnCommoditySheet(commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, rcs);
		// ???????????????
		approveReturnCommoditySheet(rcs1, commIDs, rcscNOs, commPrices, rcscSpecifications, barcodeIDs, dbName);
		// ???????????????????????????
		Commodity updateComm = BaseCommodityTest.DataInput.getCommodity();
		updateComm.setLatestPricePurchase(0.0f);
		updateComm.setID(commCreate.getID());
		updateComm.setName("????????????" + commodityOrder);
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
		Assert.assertTrue(flag, "???????????????");
		//
		List<ReturnCommoditySheet> listRcs2 = retrieveNReturnCommoditySheet(updateComm);
		flag = false;
		for (int i = 0; i < listRcs2.size(); i++) {
			if (listRcs2.get(i).getID() == rcs1.getID()) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(!flag, "?????????????????????");
	}

	// SIT1_nbr_SG_ReturnCommoditySheet_20
	@Test(dependsOnMethods = "RetrieveN2")
	public void RetrieveN3() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_ReturnCommoditySheet_", order, "???????????????????????????????????????????????????????????????");
		// 1???????????????????????????
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
		// 2??????????????????????????????????????????????????????????????????????????????
		// 3????????????????????????????????????????????????????????????
		// 4??????????????????????????????????????????
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
		// ???????????????????????????
		WarehousingCP.verifyCreate(mr, w, dbName);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(JsonPath.read(o, "$.object").toString());

		return warehousing1;
	}

	protected Warehousing approveWarehousing(Warehousing w, String commIDs, String dbName) throws Exception, UnsupportedEncodingException {
		// ?????????????????????????????????????????????
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
		// ???????????????????????????
		WarehousingCP.verifyApprove(mr, w, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);
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
		// ???????????????????????????
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
		// ??????????????????
		Commodity commodityCache = getCommodityCache(commodity.getID(), dbName);
		// ?????????????????????????????????????????????
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodity.getID(), dbName, commodityHistoryBO);
		// ?????????????????????????????????
		List<Commodity> commList = getMultioleCommodityList(commodityCache, dbName);
		// ????????????????????????????????????
		List<PackageUnit> lspu = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuCreated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuUpdated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(commodityUpdate, lspu, lspuCreated, lspuUpdated, lspuDeleted, dbName, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		MvcResult mrl = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, commodityUpdate, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrl);
		// ?????????
		CommodityCP.verifyUpdate(mrl, commodityUpdate, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, iOldNO, lspuCreated, lspuUpdated, lspuDeleted, commList, dbName, null);
	}

	@SuppressWarnings("unchecked")
	private List<Commodity> getMultioleCommodityList(Commodity commodity, String dbName) {
		DataSourceContextHolder.setDbName(dbName);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodity);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "????????????????????? DB???????????????");
		}
		return listMultiPackageCommodity;
	}

	private Commodity getCommodityCache(int commodityID, String dbName) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "???????????????????????????????????????=" + ecOut.getErrorCode() + "???????????????=" + ecOut.getErrorMessage());
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
		// ???????????????????????????
		ReturnCommoditySheetCP.verifyCreate(mr, rcs);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet rcs1 = new ReturnCommoditySheet();
		ReturnCommoditySheet rcs2 = (ReturnCommoditySheet) rcs1.parse1(jsonObject.getJSONObject(BaseAction.KEY_Object).toString());
		return rcs2;
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("???????????????staff=" + staff);

		return staff;
	}

	private int getCommodityOrder() {
		return commodityOrder.incrementAndGet();
	}

	private long getBarcode() {
		return barcode.incrementAndGet();
	}
}
