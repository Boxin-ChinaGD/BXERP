package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ReturnCommoditySheet.EnumStatusReturnCommoditySheet;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.checkPoint.ReturnCommoditySheetCP;
import com.bx.erp.test.checkPoint.StaffCP;
import com.bx.erp.test.staff.BaseShopTest;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebAppConfiguration
public class ReturnCommoditySheetActionTest extends BaseActionTest {
	private Staff boss;

	private String date1_testRetrieveNEx = "2017/1/8 01:01:00";
	private String date2_testRetrieveNEx = "2020/01/01 00:00:00";
	private SimpleDateFormat sdf2_testRetrieveNEx = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
	private SimpleDateFormat sdf3_testRetrieveNEx = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
	
	@BeforeClass
	public void setup() {
		super.setUp();

		boss = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:???????????????????????????????????????");
		ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();
		returnCommoditySheetCommodity.setCommodityID(2);
		returnCommoditySheetCommodity.setNO(4);
		returnCommoditySheetCommodity.setPurchasingPrice(11.1D);
		returnCommoditySheetCommodity.setSpecification("???");
		returnCommoditySheetCommodity.setBarcodeID(3);
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", String.valueOf(returnCommoditySheetCommodity.getCommodityID())) //
				.param("rcscNOs", String.valueOf(returnCommoditySheetCommodity.getNO())) //
				.param("commPrices", String.valueOf(returnCommoditySheetCommodity.getPurchasingPrice())) //
				.param("rcscSpecifications", String.valueOf(returnCommoditySheetCommodity.getSpecification())) //
				.param("barcodeIDs", String.valueOf(returnCommoditySheetCommodity.getBarcodeID())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr); // ... TODO ????????????????????????????????????????????????
		// ????????????
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet rcs = (ReturnCommoditySheet) new ReturnCommoditySheet().parse1(o.getString(BaseAction.KEY_Object));
		Assert.assertTrue(rcs.getProviderID() == 1 && rcs.getStatus() == EnumStatusReturnCommoditySheet.ESRCS_ToApprove.getIndex() && rcs.getStaffID() == 4, "CASE1?????????????????????????????????????????????????????????");
		// ????????????
		ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) rcs.getListSlave1().get(0);
		returnCommoditySheetCommodity.setIgnoreIDInComparision(true);
		returnCommoditySheetCommodity.setReturnCommoditySheetID(rcs.getID());
		if (returnCommoditySheetCommodity.compareTo(rcsc) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(4);// phoneOfBoss
		rtcsCreate.setShopID(2);
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr, rtcsCreate);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:?????????????????????????????????????????????");
		// ReturnCommoditySheet rcs1 = new ReturnCommoditySheet();
		MvcResult mr1 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", "3,10") //
				.param("rcscNOs", "10,15") //
				.param("commPrices", "11.1,11.2") //
				.param("rcscSpecifications", "???,???") //
				.param("barcodeIDs", "5,14") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr1);
		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(2);
		rtcsCreate.setStaffID(4);// phoneOfBoss
		rtcsCreate.setShopID(2);
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr1, rtcsCreate);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:?????????????????? ????????????????????? providerID");
		// ReturnCommoditySheet rcs2 = new ReturnCommoditySheet();
		MvcResult mr2 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), String.valueOf(Shared.BIG_ID)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", "2") //
				.param("barcodeIDs", "3") //
				.param("rcscNOs", "4") //
				.param("commPrices", "11.1") //
				.param("rcscSpecifications", "???") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:????????????????????????");
//		// ReturnCommoditySheet rcs3 = new ReturnCommoditySheet();
//		MvcResult mr3 = null;
//
//		mr3 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
//				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
//				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
//				.param("commIDs", "2") //
//				.param("barcodeIDs", "3") //
//				.param("rcscNOs", "4") //
//				.param("commPrices", "11.1") //
//				.param("rcscSpecifications", "???") //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		).andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5?????????commIDs????????????????????????commIDs?????????-1???????????????");
		// ReturnCommoditySheet rcs4 = new ReturnCommoditySheet();
		MvcResult mr4 = null;
		try {
			mr4 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
					.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
					.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
					.param("rcscNOs", "4") //
					.param("commPrices", "11.1") //
					.param("rcscSpecifications", "???") //
					.param("barcodeIDs", "1") //
					.contentType(MediaType.APPLICATION_JSON) //
					.session((MockHttpSession) sessionBoss)//
			).andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			assertNull(mr4);
		}
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6???????????????commIDs??????????????????????????????int??????????????????????????????");
		// ReturnCommoditySheet rcs5 = new ReturnCommoditySheet();
		MvcResult mr5 = null;
		try {
			mr5 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
					.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
					.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
					.param("commIDs", "??????")//
					.param("rcscNOs", "4") //
					.param("commPrices", "11.1") //
					.param("rcscSpecifications", "???") //
					.param("barcodeIDs", "1") //
					.contentType(MediaType.APPLICATION_JSON) //
					.session((MockHttpSession) sessionBoss)//
			).andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			assertNull(mr5);
		}
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7????????????commIDs????????????-1?????????");
		// ReturnCommoditySheet rcs6 = new ReturnCommoditySheet();
		MvcResult mr6 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", "-2")//
				.param("rcscNOs", "4") //
				.param("commPrices", "11.1") //
				.param("rcscSpecifications", "???") //
				.param("barcodeIDs", "1") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8?????????????????????barcodeIDs");
		// ReturnCommoditySheet rcs7 = new ReturnCommoditySheet();
		MvcResult mr7 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", "2")//
				.param("rcscNOs", "4") //
				.param("commPrices", "11.1") //
				.param("rcscSpecifications", "???") //
				.param("barcodeIDs", "-2") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9???????????????????????????ID???????????????????????????????????????");
		// ReturnCommoditySheet rcs8 = new ReturnCommoditySheet();
		MvcResult mr8 = null;
		mr8 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", Shared.BIG_ID + ",10,2,3")//
				.param("rcscNOs", "4,10,4,10") //
				.param("commPrices", "11.1,10.1,11.1,10.1") //
				.param("rcscSpecifications", "???,???,???,???") //
				.param("barcodeIDs", "1,10,3,5") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testCreateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10????????????????????????????????????????????????");
		// ReturnCommoditySheet rcs9 = new ReturnCommoditySheet();
		MvcResult mr9 = null;
		mr9 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", "2")//
				.param("rcscNOs", "10") //
				.param("commPrices", "-10.1") //
				.param("rcscSpecifications", "???") //
				.param("barcodeIDs", "10") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_WrongFormatForInputField);
		// ?????????????????????????????????????????????????????????
	}

	@Test
	public void testCreateEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11??? ???????????????????????????????????????????????????????????????7 ");
		//
		MvcResult mr11 = null;
		mr11 = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", String.valueOf("166"))//
				.param("rcscNOs", "10") //
				.param("commPrices", "10.1") //
				.param("rcscSpecifications", "???") //
				.param("barcodeIDs", "10") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_BusinessLogicNotDefined);// ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
		// ?????????????????????????????????????????????????????????
	}

	@Test
	public void testCreateEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE12:????????????????????????");
		// ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", "52") //
				.param("rcscNOs", "4") //
				.param("commPrices", "11.1") //
				.param("rcscSpecifications", "???") //
				.param("barcodeIDs", "56") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	@Test
	public void testCreateEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE13:????????????????????????????????????");
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue(json.length() == 0, "CASE13?????????????????????????????????null");
	}

	@Test
	public void testCreateEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE14:??????????????????????????????????????????" + FieldFormat.MAX_OneCommodityPrice + "???????????????");
		// ReturnCommoditySheet rcs1 = new ReturnCommoditySheet();
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", "3,10") //
				.param("barcodeIDs", "5,14") //
				.param("rcscNOs", "10,15") //
				.param("commPrices", "11.1," + (FieldFormat.MAX_OneCommodityPrice + 0.01)) //
				.param("rcscSpecifications", "???,???") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE15:??????????????????????????????????????????" + FieldFormat.MAX_OneCommodityNO + "???????????????");
		// ReturnCommoditySheet rcs1 = new ReturnCommoditySheet();
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", "3,10") //
				.param("barcodeIDs", "5,14") //
				.param("rcscNOs", "10," + (FieldFormat.MAX_OneCommodityNO + 1)) //
				.param("commPrices", "11.1,0.02") //
				.param("rcscSpecifications", "???,???") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE15:???????????????????????????????????????????????????");
		// ReturnCommoditySheet rcs1 = new ReturnCommoditySheet();
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", "3,3") //
				.param("barcodeIDs", "5,14") //
				.param("rcscNOs", "10,50") //
				.param("commPrices", "11.1,0.02") //
				.param("rcscSpecifications", "???,???") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action??????????????????????????????");
	}
	
	@Test
	public void testCreateEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE17:???????????????????????????????????????????????????????????????");
		ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();
		returnCommoditySheetCommodity.setCommodityID(2);
		returnCommoditySheetCommodity.setNO(4);
		returnCommoditySheetCommodity.setPurchasingPrice(11.1D);
		returnCommoditySheetCommodity.setSpecification("???");
		returnCommoditySheetCommodity.setBarcodeID(3);
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "1") //
				.param("commIDs", String.valueOf(returnCommoditySheetCommodity.getCommodityID())) //
				.param("rcscNOs", String.valueOf(returnCommoditySheetCommodity.getNO())) //
				.param("commPrices", String.valueOf(returnCommoditySheetCommodity.getPurchasingPrice())) //
				.param("rcscSpecifications", String.valueOf(returnCommoditySheetCommodity.getSpecification())) //
				.param("barcodeIDs", String.valueOf(returnCommoditySheetCommodity.getBarcodeID())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}
	
	@Test
	public void testCreateEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE18:??????A??????????????????????????????????????????????????????B???????????????");
		//
		// ????????????B???????????????
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		//
		ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();
		returnCommoditySheetCommodity.setCommodityID(2);
		returnCommoditySheetCommodity.setNO(4);
		returnCommoditySheetCommodity.setPurchasingPrice(11.1D);
		returnCommoditySheetCommodity.setSpecification("???");
		returnCommoditySheetCommodity.setBarcodeID(3);
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "3") //
				.param("commIDs", String.valueOf(returnCommoditySheetCommodity.getCommodityID())) //
				.param("rcscNOs", String.valueOf(returnCommoditySheetCommodity.getNO())) //
				.param("commPrices", String.valueOf(returnCommoditySheetCommodity.getPurchasingPrice())) //
				.param("rcscSpecifications", String.valueOf(returnCommoditySheetCommodity.getSpecification())) //
				.param("barcodeIDs", String.valueOf(returnCommoditySheetCommodity.getBarcodeID())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionNewShopBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();
		// ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		MvcResult mr = mvc.perform(get("/returnCommoditySheet/retrieve1Ex.bx?" + ReturnCommoditySheet.field.getFIELD_NAME_ID() + "=1") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.returnCommoditySheet.ID");
		assertTrue(i1 == 1);
		// ??????????????????
		List<Integer> listslave = JsonPath.read(o1, "$.returnCommoditySheet.listSlave1[*].returnCommoditySheetID");
		for (Integer i : listslave) {
			assertTrue(i == i1);
		}
		//
		List<String> i2 = JsonPath.read(o1, "$.returnCommoditySheet.listSlave1[*].commodityName");
		for (String i : i2) {
			assertTrue(!i.equals(""));
		}

		System.out.println("\n------------------------ CASE2:???????????????????????? ------------------------");
//		MvcResult mr2 = mvc.perform(get("/returnCommoditySheet/retrieve1Ex.bx?" + ReturnCommoditySheet.field.getFIELD_NAME_ID() + "=1") //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}
	
	@Test
	public void testRetrieveNEx_1() throws Exception {
		// ???returnCommoditySheet/retrieveNEx.bx???????????????????????????????????????????????????
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ CASE1:?????????????????????????????????????????? ------------------------");
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<Integer> list1 = JsonPath.read(o1, "$.objectList[*].ID");
		Assert.assertTrue(list1.size() >= 0);
	}

	@Test
	public void testRetrieveNEx_2() throws Exception {
		System.out.println("\n------------------------ CASE2:???????????????????????? ------------------------");
//		MvcResult mr2 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
//				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		).andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
//		String vcJson2 = mr2.getResponse().getContentAsString();
//		JSONObject vcObject2 = JSONObject.fromObject(vcJson2);
//		Object object2 = vcObject2.get("object");
//		Assert.assertNull(object2);
	}
	
	@Test
	public void testRetrieveNEx_3() throws Exception {
		System.out.println("\n------------------------ CASE3:??????????????????(10)??????????????????????????????????????? ------------------------");
		MvcResult mr3 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), "TH20190605") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoError);

		JSONObject o3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		// ????????????
		List<String> list3 = JsonPath.read(o3, "$.objectList[*].sn");
		for (int i = 0; i < list3.size(); i++) {
			String SN = list3.get(i);
			Assert.assertTrue(SN.contains("TH20190605"), "??????????????????:" + SN + "???????????????");
		}
	}
	
	@Test
	public void testRetrieveNEx_4() throws Exception {
		System.out.println("\n------------------------ CASE4:??????????????????(10)??????????????????????????????ID????????????????????? ------------------------");
		MvcResult mr4 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), "TH20190605") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_staffID(), String.valueOf(Shared.BossID)) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoError);

		JSONObject o4 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		// ????????????
		List<String> list4 = JsonPath.read(o4, "$.objectList[*].sn");
		for (int i = 0; i < list4.size(); i++) {
			String SN = list4.get(i);
			Assert.assertTrue(SN.contains("TH20190605"), "??????????????????:" + SN + "???????????????");
		}
		// ???????????????
		List<Integer> staffList4 = JsonPath.read(o4, "$.objectList[*].staffID");
		boolean isSame = false;
		for (int i = 0; i < staffList4.size(); i++) {
			if (staffList4.get(i) == 4) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "?????????ID??????");
	}
	
	@Test
	public void testRetrieveNEx_5() throws Exception {
		System.out.println("\n------------------------ CASE5:??????????????????(10)????????????????????????????????????????????????????????? ------------------------");
		MvcResult mr5 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(EnumStatusReturnCommoditySheet.ESRCS_Approved.getIndex())) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), "TH20190605") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoError);

		JSONObject o5 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		// ????????????
		List<String> list5 = JsonPath.read(o5, "$.objectList[*].sn");
		for (int i = 0; i < list5.size(); i++) {
			String SN = list5.get(i);
			Assert.assertTrue(SN.contains("TH20190605"), "??????????????????:" + SN + "???????????????");
		}
		// ?????????????????????
		List<Integer> statusList5 = JsonPath.read(o5, "$.objectList[*].status");
		boolean isSame = false;
		for (int i = 0; i < statusList5.size(); i++) {
			if (statusList5.get(i) == 1) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "????????????????????????");

	}
	
	@Test
	public void testRetrieveNEx_6() throws Exception {
		System.out.println("\n------------------------ CASE6:??????????????????(10)??????????????????????????????ID????????????????????? ------------------------");
		MvcResult mr6 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), "TH20190605") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "6") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6);

		JSONObject o6 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		// ????????????
		List<String> list6 = JsonPath.read(o6, "$.objectList[*].sn");
		for (int i = 0; i < list6.size(); i++) {
			String SN = list6.get(i);
			Assert.assertTrue(SN.contains("TH20190605"), "??????????????????:" + SN + "???????????????");
		}
		// ???????????????
		List<Integer> providerList6 = JsonPath.read(o6, "$.objectList[*].providerID");
		boolean isSame = false;
		for (int i = 0; i < providerList6.size(); i++) {
			if (providerList6.get(i) == 6) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "?????????ID??????");
	}
	
	@Test
	public void testRetrieveNEx_7() throws Exception {
		System.out.println("\n------------------------ CASE7:??????????????????(10)??????????????????????????????????????????????????????????????? ------------------------");
		MvcResult mr7 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), "TH20190605") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date1(), date1_testRetrieveNEx) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date2(), date2_testRetrieveNEx) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr7);

		JSONObject o7 = JSONObject.fromObject(mr7.getResponse().getContentAsString());
		// ????????????
		List<String> list7 = JsonPath.read(o7, "$.objectList[*].sn");
		for (int i = 0; i < list7.size(); i++) {
			String SN = list7.get(i);
			Assert.assertTrue(SN.contains("TH20190605"), "??????????????????:" + SN + "???????????????");
		}
		// ??????????????????
		List<Integer> createDatetimeList7 = JsonPath.read(o7, "$.objectList[*].createDatetime");
		boolean isSame = false;
		for (int i = 0; i < createDatetimeList7.size(); i++) {
			Date s1 = sdf2_testRetrieveNEx.parse(String.valueOf(createDatetimeList7.get(i)));
			if (s1.after(sdf3_testRetrieveNEx.parse(date1_testRetrieveNEx)) && s1.before(sdf3_testRetrieveNEx.parse(date2_testRetrieveNEx))) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "????????????");

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx_8() throws Exception {
		System.out.println("\n------------------------ CASE8:??????????????????????????????????????? ------------------------");
		// ???????????????
		// ????????????
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodeCreate = BaseBarcodesTest.retrieveNBarcodes(commodityCreate.getID(), Shared.DBName_Test);
		Random random = new Random();
		int rcscNOs = random.nextInt(100) + 1;
		double commPrices = Double.valueOf(GeneralUtil.formatToShow(random.nextDouble())) + 100; 
		// ???????????????
		BaseReturnCommoditySheetTest.createExReturnCommoditySheetViaAction(sessionBoss, commodityCreate, barcodeCreate, String.valueOf(rcscNOs), String.valueOf(commPrices), mvc);
		String name = commodityCreate.getName();
		MvcResult mr8 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), name) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr8);
		//
		JSONObject o8 = JSONObject.fromObject(mr8.getResponse().getContentAsString());
		// ??????????????????
		boolean isSame = false;
		List<Integer> list8 = JsonPath.read(o8, "$.objectList[*].ID");
		for (int i = 0; i < list8.size(); i++) {
			ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
			rcsc.setReturnCommoditySheetID(list8.get(i));
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<ReturnCommoditySheetCommodity> rcscList8 = (List<ReturnCommoditySheetCommodity>) returnCommoditySheetCommodityBO.retrieveNObject(boss.getID(), BaseBO.INVALID_CASE_ID, rcsc);
			for (ReturnCommoditySheetCommodity rcsc8 : rcscList8) {
				ErrorInfo ecOut = new ErrorInfo();
				Commodity commodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(rcsc8.getCommodityID(), boss.getID(), ecOut, Shared.DBName_Test);
				if (commodity.getName().equals(name)) {
					isSame = true;
					break;
				}
			}
		}
		Assert.assertTrue(isSame, "??????????????????");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx_9() throws Exception {
		System.out.println("\n------------------------ CASE9:??????????????????????????????ID????????????????????? ------------------------");
		String name = "????????????";
		MvcResult mr9 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), name) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_staffID(), "4") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr9);

		JSONObject o9 = JSONObject.fromObject(mr9.getResponse().getContentAsString());
		// ??????????????????
		boolean isSame = false;
		List<Integer> list9 = JsonPath.read(o9, "$.objectList[*].ID");
		for (int i = 0; i < list9.size(); i++) {
			ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
			rcsc.setReturnCommoditySheetID(list9.get(i));
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<ReturnCommoditySheetCommodity> rcscList9 = (List<ReturnCommoditySheetCommodity>) returnCommoditySheetCommodityBO.retrieveNObject(boss.getID(), BaseBO.INVALID_CASE_ID, rcsc);
			for (ReturnCommoditySheetCommodity rcsc9 : rcscList9) {
				ErrorInfo ecOut = new ErrorInfo();
				Commodity commodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(rcsc9.getCommodityID(), boss.getID(), ecOut, Shared.DBName_Test);
				if (commodity.getName().equals(name)) {
					isSame = true;
					break;
				}
			}
		}
		Assert.assertTrue(isSame, "??????????????????");
		// ???????????????
		List<Integer> staffList9 = JsonPath.read(o9, "$.objectList[*].staffID");
		isSame = false;
		for (int i = 0; i < staffList9.size(); i++) {
			if (staffList9.get(i) == 4) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "???????????????");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx_10() throws Exception {
		System.out.println("\n------------------------ CASE10:????????????????????????????????????????????????????????? ------------------------");
		String name = "??????????????????";
		MvcResult mr10 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(EnumStatusReturnCommoditySheet.ESRCS_ToApprove.getIndex())) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), name) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr10);

		JSONObject o10 = JSONObject.fromObject(mr10.getResponse().getContentAsString());
		// ??????????????????
		boolean isSame = false;
		List<Integer> list10 = JsonPath.read(o10, "$.objectList[*].ID");
		for (int i = 0; i < list10.size(); i++) {
			ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
			rcsc.setReturnCommoditySheetID(list10.get(i));
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<ReturnCommoditySheetCommodity> rcscList10 = (List<ReturnCommoditySheetCommodity>) returnCommoditySheetCommodityBO.retrieveNObject(boss.getID(), BaseBO.INVALID_CASE_ID, rcsc);
			for (ReturnCommoditySheetCommodity rcsc10 : rcscList10) {
				ErrorInfo ecOut = new ErrorInfo();
				Commodity commodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(rcsc10.getCommodityID(), boss.getID(), ecOut, Shared.DBName_Test);
				Assert.assertTrue(commodity.getName().equals(name));
				if (commodity.getName().equals(name)) {
					isSame = true;
					break;
				}
			}
		}
		Assert.assertTrue(isSame, "??????????????????");
		// ?????????????????????
		List<Integer> statusList10 = JsonPath.read(o10, "$.objectList[*].status");
		isSame = false;
		for (int i = 0; i < statusList10.size(); i++) {
			if (statusList10.get(i) == 0) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "?????????????????????");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx_11() throws Exception {
		System.out.println("\n------------------------ CASE11:??????????????????????????????ID????????????????????? ------------------------");
		String name = "????????????????????????";
		MvcResult mr11 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), name) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "6") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr11);

		JSONObject o11 = JSONObject.fromObject(mr11.getResponse().getContentAsString());
		// ??????????????????
		boolean isSame = false;
		List<Integer> list11 = JsonPath.read(o11, "$.objectList[*].ID");
		for (int i = 0; i < list11.size(); i++) {
			ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
			rcsc.setReturnCommoditySheetID(list11.get(i));
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<ReturnCommoditySheetCommodity> rcscList11 = (List<ReturnCommoditySheetCommodity>) returnCommoditySheetCommodityBO.retrieveNObject(boss.getID(), BaseBO.INVALID_CASE_ID, rcsc);
			for (ReturnCommoditySheetCommodity rcsc11 : rcscList11) {
				ErrorInfo ecOut = new ErrorInfo();
				Commodity commodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(rcsc11.getCommodityID(), boss.getID(), ecOut, Shared.DBName_Test);
				if (commodity.getName().equals(name)) {
					isSame = true;
					break;
				}
			}
		}
		Assert.assertTrue(isSame, "??????????????????");
		// ???????????????
		List<Integer> providerList11 = JsonPath.read(o11, "$.objectList[*].providerID");
		isSame = false;
		for (int i = 0; i < providerList11.size(); i++) {
			if (providerList11.get(i) == 6) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "?????????ID??????");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx_12() throws Exception {
		System.out.println("\n------------------------ CASE12:??????????????????????????????????????????????????????????????? ------------------------");
		String name = "?????????????????????";
		MvcResult mr12 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), name) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date1(), date1_testRetrieveNEx) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date2(), date2_testRetrieveNEx) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr12);

		JSONObject o12 = JSONObject.fromObject(mr12.getResponse().getContentAsString());
		// ??????????????????
		boolean isSame = false;
		List<Integer> list12 = JsonPath.read(o12, "$.objectList[*].ID");
		for (int i = 0; i < list12.size(); i++) {
			ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
			rcsc.setReturnCommoditySheetID(list12.get(i));
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<ReturnCommoditySheetCommodity> rcscList12 = (List<ReturnCommoditySheetCommodity>) returnCommoditySheetCommodityBO.retrieveNObject(boss.getID(), BaseBO.INVALID_CASE_ID, rcsc);
			for (ReturnCommoditySheetCommodity rcsc12 : rcscList12) {
				ErrorInfo ecOut = new ErrorInfo();
				Commodity commodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(rcsc12.getCommodityID(), boss.getID(), ecOut, Shared.DBName_Test);
				if (commodity.getName().equals(name)) {
					isSame = true;
					break;
				}
			}
		}
		Assert.assertTrue(isSame, "??????????????????");
		// ????????????
		List<Integer> createDatetimeList12 = JsonPath.read(o12, "$.objectList[*].createDatetime");
		isSame = false;
		for (int i = 0; i < createDatetimeList12.size(); i++) {
			Date s1 = sdf2_testRetrieveNEx.parse(String.valueOf(createDatetimeList12.get(i)));
			if (s1.after(sdf3_testRetrieveNEx.parse(date1_testRetrieveNEx)) && s1.before(sdf3_testRetrieveNEx.parse(date2_testRetrieveNEx))) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "????????????");

	}
	
	@Test
	public void testRetrieveNEx_13() throws Exception {
		System.out.println("\n------------------------ CASE13:???????????????ID????????????????????? ------------------------");
		MvcResult mr13 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_staffID(), "6") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr13);

		JSONObject o13 = JSONObject.fromObject(mr13.getResponse().getContentAsString());
		// ????????????????????????
		List<Integer> list13 = JsonPath.read(o13, "$.objectList[*].staffID");
		boolean isSame = false;
		for (int i = 0; i < list13.size(); i++) {
			if (list13.get(i) == 6) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "???????????????");

	}
	
	@Test
	public void testRetrieveNEx_14() throws Exception {
		System.out.println("\n------------------------ CASE14:?????????????????????????????????????????? ------------------------");
		MvcResult mr14 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), "1") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr14);

		JSONObject o14 = JSONObject.fromObject(mr14.getResponse().getContentAsString());
		// ?????????????????????
		List<Integer> list14 = JsonPath.read(o14, "$.objectList[*].status");
		boolean isSame = false;
		for (int i = 0; i < list14.size(); i++) {
			if (list14.get(i) == 1) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "????????????????????????");
	}
	
	@Test
	public void testRetrieveNEx_15() throws Exception {
		System.out.println("\n------------------------ CASE15:???????????????ID????????????????????? ------------------------");
		MvcResult mr15 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "3") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr15);

		JSONObject o15 = JSONObject.fromObject(mr15.getResponse().getContentAsString());
		// ???????????????
		List<Integer> list15 = JsonPath.read(o15, "$.objectList[*].providerID");
		boolean isSame = false;
		for (int i = 0; i < list15.size(); i++) {
			if (list15.get(i) == 3) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "?????????ID??????");
	}
	
	@Test
	public void testRetrieveNEx_16() throws Exception {
		System.out.println("\n------------------------ CASE16:???????????????????????????????????????????????? ------------------------");
		MvcResult mr16 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date1(), date1_testRetrieveNEx) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date2(), date2_testRetrieveNEx) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr16);

		JSONObject o16 = JSONObject.fromObject(mr16.getResponse().getContentAsString());
		// ????????????
		List<Integer> list16 = JsonPath.read(o16, "$.objectList[*].createDatetime");
		boolean isSame = false;
		for (int i = 0; i < list16.size(); i++) {
			Date s1 = sdf2_testRetrieveNEx.parse(String.valueOf(list16.get(i)));
			if (s1.after(sdf3_testRetrieveNEx.parse(date1_testRetrieveNEx)) && s1.before(sdf3_testRetrieveNEx.parse(date2_testRetrieveNEx))) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "????????????");
	}
	
	@Test
	public void testRetrieveNEx_17() throws Exception {
		System.out.println("\n------------------------ CASE17:??????????????????(string??????????????????)????????????????????? ------------------------");
		MvcResult mr17 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), "1") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), "TH20190605") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_staffID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date1(), date1_testRetrieveNEx) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date2(), date2_testRetrieveNEx) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr17);

		JSONObject o17 = JSONObject.fromObject(mr17.getResponse().getContentAsString());
		// ????????????
		List<String> list17 = JsonPath.read(o17, "$.objectList[*].sn");
		for (int i = 0; i < list17.size(); i++) {
			String SN = list17.get(i);
			Assert.assertTrue(SN.contains("TH20190605"), "??????????????????:" + SN + "???????????????");
		}
		// ?????????????????????
		List<Integer> statusList17 = JsonPath.read(o17, "$.objectList[*].status");
		boolean isSame = false;
		for (int i = 0; i < statusList17.size(); i++) {
			if (statusList17.get(i) == 1) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "????????????????????????");
		// ???????????????ID
		List<Integer> staffLIst17 = JsonPath.read(o17, "$.objectList[*].staffID");
		isSame = false;
		for (int i = 0; i < staffLIst17.size(); i++) {
			if (staffLIst17.get(i) == 2) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "?????????ID??????");
		// ???????????????ID
		List<Integer> providerList17 = JsonPath.read(o17, "$.objectList[*].providerID");
		isSame = false;
		for (int i = 0; i < providerList17.size(); i++) {
			if (providerList17.get(i) == 1) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "?????????ID??????");
		// ????????????
		List<Integer> createDatetimeList17 = JsonPath.read(o17, "$.objectList[*].createDatetime");
		isSame = false;
		for (int i = 0; i < createDatetimeList17.size(); i++) {
			Date s1 = sdf2_testRetrieveNEx.parse(String.valueOf(createDatetimeList17.get(i)));
			if (s1.after(sdf3_testRetrieveNEx.parse(date1_testRetrieveNEx)) && s1.before(sdf3_testRetrieveNEx.parse(date2_testRetrieveNEx))) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "????????????");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx_18() throws Exception {
		System.out.println("\n------------------------ CASE18:??????????????????(string???????????????)????????????????????? ------------------------");
		String name = "?????????????????????";
		MvcResult mr18 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), name) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_staffID(), "2") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date1(), date1_testRetrieveNEx) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_date2(), date2_testRetrieveNEx) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr18);

		JSONObject o18 = JSONObject.fromObject(mr18.getResponse().getContentAsString());
		// ??????????????????
		boolean isSame = false;
		List<Integer> list18 = JsonPath.read(o18, "$.objectList[*].ID");
		for (int i = 0; i < list18.size(); i++) {
			ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
			rcsc.setReturnCommoditySheetID(list18.get(i));
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<ReturnCommoditySheetCommodity> rcscList18 = (List<ReturnCommoditySheetCommodity>) returnCommoditySheetCommodityBO.retrieveNObject(boss.getID(), BaseBO.INVALID_CASE_ID, rcsc);
			Assert.assertTrue(returnCommoditySheetCommodityBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "??????????????????????????????" + returnCommoditySheetCommodityBO.printErrorInfo());
			//
			Assert.assertTrue(rcscList18 != null && rcscList18.size() != 0);
			for (ReturnCommoditySheetCommodity rcsc18 : rcscList18) {
				if (rcsc18.getCommodityName().equals(name)) {
					isSame = true;
					break;
				}
			}
		}
		Assert.assertTrue(isSame, "??????????????????");
		// ?????????????????????
		List<Integer> statusList18 = JsonPath.read(o18, "$.objectList[*].status");
		isSame = false;
		for (int i = 0; i < statusList18.size(); i++) {
			if (statusList18.get(i) == 1) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "??????????????????");
		// ???????????????ID
		List<Integer> staffList18 = JsonPath.read(o18, "$.objectList[*].staffID");
		isSame = false;
		for (int i = 0; i < staffList18.size(); i++) {
			if (staffList18.get(i) == 2) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "?????????ID??????");
		// ???????????????ID
		List<Integer> providerList18 = JsonPath.read(o18, "$.objectList[*].providerID");
		isSame = false;
		for (int i = 0; i < providerList18.size(); i++) {
			if (providerList18.get(i) == 1) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "?????????ID??????");
		// ????????????
		List<Integer> createDatetimeList18 = JsonPath.read(o18, "$.objectList[*].createDatetime");
		isSame = false;
		for (int i = 0; i < createDatetimeList18.size(); i++) {
			Date s1 = sdf2_testRetrieveNEx.parse(String.valueOf(createDatetimeList18.get(i)));
			if (s1.after(sdf3_testRetrieveNEx.parse(date1_testRetrieveNEx)) && s1.before(sdf3_testRetrieveNEx.parse(date2_testRetrieveNEx))) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame, "????????????");
	}
	
	@Test
	public void testRetrieveNEx_19() throws Exception {
		System.out.println("\n------------------------ CASE19:???????????????????????????????????????????????? ------------------------");
		String name = "????????????????????????";
		MvcResult mr19 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), name) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr19, EnumErrorCode.EC_NoError);
		//
		String vcJson19 = mr19.getResponse().getContentAsString();
		JSONObject vcObject19 = JSONObject.fromObject(vcJson19);
		//
		Object object19 = vcObject19.get("object");
		Assert.assertNull(object19);
	}
	
	@Test
	public void testRetrieveNEx_20() throws Exception {
		System.out.println("\n------------------------ CASE20:??????????????????(10)???????????????????????????????????????------------------------");
		MvcResult mr20 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), "TH2019060") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr20, EnumErrorCode.EC_NoError);
		//
		String vcJson20 = mr20.getResponse().getContentAsString();
		JSONObject vcObject20 = JSONObject.fromObject(vcJson20);
		//
		Object object20 = vcObject20.get("object");
		Assert.assertNull(object20);
	}
	
	@Test
	public void testRetrieveNEx_21() throws Exception {
		System.out.println("\n------------------------ CASE21:??????????????????(20)???????????????????????????????????????------------------------");
		MvcResult mr21 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), "TH20190605123451234512345") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr21, EnumErrorCode.EC_NoError);
		//
		String vcJson21 = mr21.getResponse().getContentAsString();
		JSONObject vcObject21 = JSONObject.fromObject(vcJson21);
		//
		Object object21 = vcObject21.get("object");
		Assert.assertNull(object21);
	}
	
	@Test
	public void testRetrieveNEx_22() throws Exception {
		System.out.println("\n------------------------ CASE22:??????????????????(20)???????????????????????????????????????------------------------");
		MvcResult mr22 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), "TH201906051234512345") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr22, EnumErrorCode.EC_NoError);
		//
		String vcJson22 = mr22.getResponse().getContentAsString();
		JSONObject vcObject22 = JSONObject.fromObject(vcJson22);
		//
		Object object22 = vcObject22.get("object");
		Assert.assertNull(object22);
	}

	@Test
	public void test22RetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE22:????????????????????????(????????????)");
		// ????????????
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, c);
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		// ???????????????
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.createReturnCommoditySheet();
		// ?????????????????????
		ReturnCommoditySheetCommodity rcsc = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc.setCommodityID(commCreate.getID());
		rcsc.setCommodityName(commCreate.getName());
		BaseReturnCommoditySheetTest.createReturnCommoditySheetCommodity(rcs.getID(), rcsc.getCommodityID(), bmList.get(1).get(0).getID(), rcsc.getNO(), rcsc.getSpecification());
		// ???????????????????????????
		commCreate.setName("????????????1" + System.currentTimeMillis() % 1000000);
		commCreate.setOperatorStaffID(STAFF_ID3);
		Thread.sleep(1000);
		BaseCommodityTest.updateCommodityViaMapper(commCreate);
		//
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), commCreate.getName()).contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray rcsJSONArray = JsonPath.read(o, "$.objectList");
		ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
		List<?> listRcs = returnCommoditySheet.parseN(rcsJSONArray);
		Assert.assertTrue(listRcs != null, "??????????????????null");
		Assert.assertTrue(listRcs.size() == 1, "??????????????????????????????");
	}

	@Test
	public void test23RetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE23:????????????????????????(????????????)");
		// ????????????
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, c);
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		// ???????????????
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.createReturnCommoditySheet();
		// ?????????????????????
		ReturnCommoditySheetCommodity rcsc = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc.setCommodityID(commCreate.getID());
		rcsc.setCommodityName(commCreate.getName());
		BaseReturnCommoditySheetTest.createReturnCommoditySheetCommodity(rcs.getID(), rcsc.getCommodityID(), bmList.get(1).get(0).getID(), rcsc.getNO(), rcsc.getSpecification());
		// ???????????????
		BaseReturnCommoditySheetTest.approveReturnCommoditySheet(rcs);
		// ???????????????????????????
		commCreate.setName("????????????1" + System.currentTimeMillis() % 1000000);
		commCreate.setOperatorStaffID(STAFF_ID3);
		commCreate.setNO(-50);
		Thread.sleep(1000);
		BaseCommodityTest.updateCommodityViaMapper(commCreate);
		//
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), rcsc.getCommodityName())//
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray rcsJSONArray = JsonPath.read(o, "$.objectList");
		ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
		List<?> listRcs = returnCommoditySheet.parseN(rcsJSONArray);
		Assert.assertTrue(listRcs != null, "??????????????????null");
		Assert.assertTrue(listRcs.size() == 1, "??????????????????????????????");
	}

	@Test
	public void test24RetrieveNEx() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("----------------Case24?????????queryKeyword??????_????????????????????????????????? -----------");
		// ????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("??????????????????_" + System.currentTimeMillis());
		String barcode = "987623" + System.currentTimeMillis();
		commodity.setMultiPackagingInfo(barcode + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";" + commodity.getPriceWholesale() + ";"
				+ commodity.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodes1 = BaseBarcodesTest.retrieveNBarcodes(commodity1.getID(), Shared.DBName_Test);
		// ?????????????????????????????????
		MvcResult mr = mvc.perform(post("/returnCommoditySheet/createEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "5") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
				.param("commIDs", String.valueOf(commodity1.getID())) //
				.param("rcscNOs", "4") //
				.param("commPrices", "11.1") //
				.param("rcscSpecifications", "???") //
				.param("barcodeIDs", String.valueOf(barcodes1.getID())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		// ??????????????????
		MvcResult mr1 = mvc.perform(post("/returnCommoditySheet/retrieveNEx.bx") //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
				.param(ReturnCommoditySheet.field.getFIELD_NAME_queryKeyword(), "_") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		// ????????????
		Shared.checkJSONErrorCode(mr1);

		// ?????????????????????ID
		JSONObject object = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<Integer> rcsID = JsonPath.read(object, "$.objectList[*].ID");
		for (int id : rcsID) {
			// ???????????????ID?????????????????????
			MvcResult mr2 = mvc.perform( //
					get("/returnCommoditySheet/retrieve1Ex.bx?" //
							+ ReturnCommoditySheet.field.getFIELD_NAME_ID() + "=" + id + "&" //
					) //
							.contentType(MediaType.APPLICATION_JSON) //
							.session((MockHttpSession) sessionBoss) //
			) //
					.andExpect(status().isOk()) //
					.andDo(print()) //
					.andReturn();
			Shared.checkJSONErrorCode(mr2);

			JSONObject o = JSONObject.fromObject(mr2.getResponse().getContentAsString());
			System.out.println();
			// ???????????????????????????name
			List<String> resultName = JsonPath.read(o, "$.returnCommoditySheet.listSlave1[*].commodity.name");
			System.out.println();

			if (id == commodity1.getID()) {
				// ????????????????????????name??????????????????_??????
				for (String result : resultName) {
					Assert.assertTrue(result.contains(commodity1.getName()), "??????????????????????????????_?????????");
				}
			}

		}
	}

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		// ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");

		System.out.println("\n------------------------ CASE1:???????????? ------------------------");

		// ReturnCommoditySheet rcs1 = new ReturnCommoditySheet();
		MvcResult mr2 = mvc.perform(//
				post("/returnCommoditySheet/updateEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i1 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "5") //
						.param("commIDs", "5") //
						.param("rcscNOs", "10") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "7") //
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		// ?????????
		ReturnCommoditySheet rtcsUpdate = new ReturnCommoditySheet();
		rtcsUpdate.setProviderID(5);
		rtcsUpdate.setStaffID(4);
		rtcsUpdate.setShopID(2);
		ReturnCommoditySheetCP.verifyUpdate(mr2, rtcsUpdate);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");

		System.out.println("\n------------------------ CASE2:???????????????????????????ID ------------------------");
		MvcResult mr3 = null;
		try {
			mr3 = mvc.perform(//
					post("/returnCommoditySheet/updateEx.bx") //
							.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i1 + "") //
							.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "-5") //
							.param("commIDs", "2") //
							.param("rcscNOs", "4") //
							.param("commPrices", "11.1") //
							.param("rcscSpecifications", "???") //
							.param("barcodeIDs", "1") //
							.session((MockHttpSession) sessionBoss) //
							.contentType(MediaType.APPLICATION_JSON) //
			)//
					.andExpect(status().isOk())//
					.andDo(print())//
					.andReturn();
		} catch (Exception e) {
			assertNull(mr3);
		}
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");

		System.out.println("\n------------------------ CASE3:??????????????????????????? ------------------------");
		// ???????????????
		MvcResult mr4 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i1 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "5") //
						.param("commIDs", "5") //
						.param("rcscNOs", "10") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "7") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "1") //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoError);

		MvcResult mr5 = mvc.perform(//
				post("/returnCommoditySheet/updateEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i1 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "5") //
						.param("commIDs", "5") //
						.param("rcscNOs", "10") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "7") //
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ CASE4:???????????????????????? ------------------------");
		// ???????????????
		// ReturnCommoditySheet rcs2 = new ReturnCommoditySheet();
		MvcResult mr6 = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("barcodeIDs", "3") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6);

		JSONObject o2 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		Integer i2 = JsonPath.read(o2, "$.object.ID");

//		MvcResult mr7 = mvc.perform(//
//				post("/returnCommoditySheet/updateEx.bx") //
//						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i2 + "") //
//						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
//						.param("commIDs", "2") //
//						.param("barcodeIDs", "3") //
//						.param("rcscNOs", "4") //
//						.param("commPrices", "11.1") //
//						.param("rcscSpecifications", "???") //
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		// ???????????????
		// ReturnCommoditySheet rcs2 = new ReturnCommoditySheet();
		MvcResult mr6 = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6);

		JSONObject o2 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		Integer i2 = JsonPath.read(o2, "$.object.ID");

		System.out.println("---------------Case5?????????commIDs????????????????????????commIDs?????????-1???????????????---------------");
		// ReturnCommoditySheet rcs3 = new ReturnCommoditySheet();
		MvcResult mr8 = mvc.perform(//
				post("/returnCommoditySheet/updateEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i2 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		String json = mr8.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testUpdateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		// ???????????????
		// ReturnCommoditySheet rcs2 = new ReturnCommoditySheet();
		MvcResult mr6 = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6);

		JSONObject o2 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		Integer i2 = JsonPath.read(o2, "$.object.ID");

		System.out.println("---------------Case6???????????????commIDs??????????????????????????????int??????????????????????????????---------------");
		// ReturnCommoditySheet rcs4 = new ReturnCommoditySheet();
		MvcResult mr9 = null;
		try {
			mr9 = mvc.perform(//
					post("/returnCommoditySheet/updateEx.bx") //
							.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i2 + "") //
							.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
							.param("commIDs", "??????")//
							.param("rcscNOs", "4") //
							.param("commPrices", "11.1") //
							.param("rcscSpecifications", "???") //
							.param("barcodeIDs", "1") //
							.contentType(MediaType.APPLICATION_JSON) //
							.session((MockHttpSession) sessionBoss)//
			).andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			assertNull(mr9);
		}
		//
	}

	@Test
	public void testUpdateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		// ???????????????
		// ReturnCommoditySheet rcs2 = new ReturnCommoditySheet();
		MvcResult mr6 = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6);

		JSONObject o2 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		Integer i2 = JsonPath.read(o2, "$.object.ID");

		System.out.println("---------------Case7????????????commIDs????????????-1?????????---------------");
		// ReturnCommoditySheet rcs5 = new ReturnCommoditySheet();
		MvcResult mr10 = mvc.perform(//
				post("/returnCommoditySheet/updateEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i2 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
						.param("commIDs", "-2")//
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testUpdateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		// ???????????????
		// ReturnCommoditySheet rcs2 = new ReturnCommoditySheet();
		MvcResult mr6 = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6);

		JSONObject o2 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		Integer i2 = JsonPath.read(o2, "$.object.ID");

		System.out.println("---------------Case8??????????????????ID?????????ID???????????????????????????????????????---------------");
		// ReturnCommoditySheet rcs6 = new ReturnCommoditySheet();
		MvcResult mr11 = mvc.perform(//
				post("/returnCommoditySheet/updateEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i2 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
						.param("commIDs", "2,10,3,1")//
						.param("barcodeIDs", "1," + Shared.BIG_ID + ",5,1") //
						.param("rcscNOs", "4,10,4,10") //
						.param("commPrices", "11.1,10.1,11.1,10.1") //
						.param("rcscSpecifications", "???,???,???,???") //
						.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr11, "??????????????????????????????????????????", "?????????????????????");
	}

	@Test
	public void testUpdateEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("---------------Case9????????????????????????????????????????????????---------------");
		// ???????????????
		// ReturnCommoditySheet rcs7 = new ReturnCommoditySheet();
		MvcResult mr12 = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr12);

		JSONObject o3 = JSONObject.fromObject(mr12.getResponse().getContentAsString());
		Integer i3 = JsonPath.read(o3, "$.object.ID");

		// ReturnCommoditySheet rcs8 = new ReturnCommoditySheet();
		MvcResult mr13 = mvc.perform(//
				post("/returnCommoditySheet/updateEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i3 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
						.param("commIDs", "10")//
						.param("rcscNOs", "10") //
						.param("commPrices", "-11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "10") //
						.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testUpdateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("---------------Case10?????????????????????????????????????????????????????????????????????---------------");
		// ???????????????
		// ReturnCommoditySheet rcs7 = new ReturnCommoditySheet();
		MvcResult mr12 = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr12);

		JSONObject o3 = JSONObject.fromObject(mr12.getResponse().getContentAsString());
		Integer i3 = JsonPath.read(o3, "$.object.ID");

		// ReturnCommoditySheet rcs8 = new ReturnCommoditySheet();
		MvcResult mr13 = mvc.perform(//
				post("/returnCommoditySheet/updateEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i3 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "2") //
						.param("commIDs", "166")//
						.param("rcscNOs", "10") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "10") //
						.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUpdateEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE11:?????????????????????????????????????????? ");
		// ?????????????????????????????????
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");
		// ???????????????
		MvcResult mr2 = mvc.perform(//
				post("/returnCommoditySheet/updateEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(i1)) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "5") //
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr2.getResponse().getContentAsString();
		Assert.assertTrue(json.length() == 0, "CASE11?????????????????????????????????null");
	}

	@Test
	public void testUpdateEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");

		System.out.println("\n------------------------ CASE12:?????????????????????????????????????????????????????? ------------------------");
		// ???????????????
		MvcResult mr4 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i1 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "5") //
						.param("commIDs", "5") //
						.param("rcscNOs", "10") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "7") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "1") //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoError);

		MvcResult mr5 = mvc.perform(//
				post("/returnCommoditySheet/updateEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i1 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "5") //
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		String json = mr5.getResponse().getContentAsString();
		Assert.assertTrue(json.length() == 0, "CASE12?????????????????????????????????null");

	}

	@Test
	public void testApproveEx1() throws Exception { // ...????????????????????????????????????????????????????????????????????????assert?????????????????????
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1: ?????????????????????");
		// ???????????????
		int commodityID = 2;
		int rcscNOs = 85;
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", String.valueOf(commodityID)) //
						.param("rcscNOs", String.valueOf(rcscNOs)) //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");
		ErrorInfo ecOut = new ErrorInfo();
		//
		Commodity commA = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(4);// phoneOfBoss
		rtcsCreate.setShopID(2);
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr, rtcsCreate);
		ReturnCommoditySheet returnCommoditySheetDB = new ReturnCommoditySheet();
		returnCommoditySheetDB = (ReturnCommoditySheet) returnCommoditySheetDB.parse1(o1.getString(BaseAction.KEY_Object));
		//
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		commListBeforeApprove.add(commA);
		List<Warehousing> warehousingList = BaseReturnCommoditySheetTest.getInfoBeforeApprove(commListBeforeApprove, warehousingMapper, warehousingCommodityMapper, Shared.DBName_Test);
		// ???????????????
		MvcResult mr1 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(i1)) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "0") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
		//
		Commodity commB = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commB == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		//
		Assert.assertTrue(commB.getNO() == (commA.getNO() - rcscNOs), "??????????????????????????????");

		// ?????????
		ReturnCommoditySheetCP.verifyApprove(mr1, returnCommoditySheetDB, commodityMapper, commListBeforeApprove, returnCommoditySheetCommodityBO, Shared.DBName_Test, warehousingList, warehousingMapper);
	}

	@Test
	public void testApproveEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2: ???????????????????????????");
		// ???????????????
		int commodityID = 2;
		int rcscNOs = 4;
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", String.valueOf(commodityID)) //
						.param("rcscNOs", String.valueOf(rcscNOs)) //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(4);// phoneOfBoss
		rtcsCreate.setShopID(2);
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr, rtcsCreate);
		//
		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commA = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		//
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		commListBeforeApprove.add(commA);
		List<Warehousing> warehousingList = BaseReturnCommoditySheetTest.getInfoBeforeApprove(commListBeforeApprove, warehousingMapper, warehousingCommodityMapper, Shared.DBName_Test);
		//
		ReturnCommoditySheet returnCommoditySheetDB = new ReturnCommoditySheet();
		returnCommoditySheetDB = (ReturnCommoditySheet) returnCommoditySheetDB.parse1(o1.getString(BaseAction.KEY_Object));
		// ???????????????
		MvcResult mr1 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i1 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "0") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
		//
		Commodity commB = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commB == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		//
		Assert.assertTrue(commB.getNO() == (commA.getNO() - rcscNOs), "??????????????????????????????");
		// ?????????
		ReturnCommoditySheet rtcsApprove = new ReturnCommoditySheet();
		rtcsApprove.setProviderID(1);
		rtcsApprove.setStaffID(4);
		rtcsApprove.setShopID(2);
		rtcsApprove.setbReturnCommodityListIsModified(0);
		rtcsApprove.setListSlave1(returnCommoditySheetDB.getListSlave1());
		ReturnCommoditySheetCP.verifyApprove(mr1, rtcsApprove, commodityMapper, commListBeforeApprove, returnCommoditySheetCommodityBO, Shared.DBName_Test, warehousingList, warehousingMapper);
		// ????????????
		MvcResult mr2 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i1 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param("commIDs", "3") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "5") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "0") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr2, "?????????????????????????????????????????????????????????", "????????????????????????");
	}

	@Test
	public void testApproveEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3: ???????????????????????????");
		MvcResult mr3 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID)) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "0") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoSuchData);
		Shared.checkJSONMsg(mr3, "???????????????????????????????????????", "????????????????????????");
	}

	@Test
	public void testApproveEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4: ????????????????????????");
		// ?????????????????????????????????
		// ReturnCommoditySheet rcs1 = new ReturnCommoditySheet();
		int commodityID = 5;
		MvcResult mr4 = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", String.valueOf(commodityID)) //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "7") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr4);
		//
		JSONObject o2 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		Integer i2 = JsonPath.read(o2, "$.object.ID");

		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(4);// phoneOfBoss
		rtcsCreate.setShopID(2);
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr4, rtcsCreate);
		// ????????????????????????????????????
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commA = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		//
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		commListBeforeApprove.add(commA);
		List<Warehousing> warehousingList = BaseReturnCommoditySheetTest.getInfoBeforeApprove(commListBeforeApprove, warehousingMapper, warehousingCommodityMapper, Shared.DBName_Test);

		MvcResult mr5 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i2 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "7") //
						.param("commIDs", String.valueOf(commodityID)) //
						// .param("rcscNOs", "30") //
						.param("rcscNOs", "300") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "7") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "1") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5);
		// ?????????
		ReturnCommoditySheet rtcsApprove = new ReturnCommoditySheet();
		rtcsApprove.setProviderID(7);
		rtcsApprove.setStaffID(4);
		rtcsApprove.setShopID(2);
		rtcsApprove.setbReturnCommodityListIsModified(1);
		//
		ReturnCommoditySheetCP.verifyApprove(mr5, rtcsApprove, commodityMapper, commListBeforeApprove, returnCommoditySheetCommodityBO, Shared.DBName_Test, warehousingList, warehousingMapper);
		//
		Commodity commB = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commB == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		//
		Assert.assertTrue(commB.getNO() == (commA.getNO() - 300), "CASE4?????????????????????????????????????????????");
	}

	@Test
	public void testApproveEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5: ???????????????????????????????????????????????????????????????");
		// ?????????????????????????????????
		// ReturnCommoditySheet rcs3 = new ReturnCommoditySheet();
		MvcResult mr6 = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", "2") //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6);

		JSONObject o3 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		Integer i3 = JsonPath.read(o3, "$.object.ID");
		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(4);// phoneOfBoss
		rtcsCreate.setShopID(2);
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr6, rtcsCreate);

		// ????????????????????????
		MvcResult mr7 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i3 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "7") //
						.param("commIDs", "5,6") //
						.param("barcodeIDs", "7," + Shared.BIG_ID) //
						.param("rcscNOs", "30,40") //
						.param("commPrices", "11.1,11.2") //
						.param("rcscSpecifications", "???,???") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "1") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoSuchData);
		Shared.checkJSONMsg(mr7, "?????????????????????", "?????????????????????");
	}

	@Test
	public void testApproveEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6: ????????????????????????????????????????????????????????????????????????");
		// ?????????????????????????????????
		int commodityID = 2;
		MvcResult mr8 = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", String.valueOf(commodityID)) //
						.param("rcscNOs", "4") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr8);

		JSONObject o4 = JSONObject.fromObject(mr8.getResponse().getContentAsString());
		Integer i4 = JsonPath.read(o4, "$.object.ID");
		//
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commA = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(4);// phoneOfBoss
		rtcsCreate.setShopID(2);
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr8, rtcsCreate);
		//
		ReturnCommoditySheet returnCommoditySheetDB = new ReturnCommoditySheet();
		returnCommoditySheetDB.setbReturnCommodityListIsModified(EnumBoolean.EB_Yes.getIndex());
		returnCommoditySheetDB = (ReturnCommoditySheet) returnCommoditySheetDB.parse1(o4.getString(BaseAction.KEY_Object));
		//
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		commListBeforeApprove.add(commA);
		List<Warehousing> warehousingList = BaseReturnCommoditySheetTest.getInfoBeforeApprove(commListBeforeApprove, warehousingMapper, warehousingCommodityMapper, Shared.DBName_Test);
		// ????????????????????????
		MvcResult mr9 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i4 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param("commIDs", "5,6") //
						.param("barcodeIDs", "7,8") //
						.param("rcscNOs", "30,40") //
						.param("commPrices", "11.1,11.2") //
						.param("rcscSpecifications", "???,???") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "1") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr9);
		// ?????????
		ReturnCommoditySheetCP.verifyApprove(mr9, returnCommoditySheetDB, commodityMapper, commListBeforeApprove, returnCommoditySheetCommodityBO, Shared.DBName_Test, warehousingList, warehousingMapper);
	}

	@Test
	public void testApproveEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7:????????????????????????");
		// ReturnCommoditySheet rcs5 = new ReturnCommoditySheet();
		MvcResult mr10 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), "3") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "7") //
						.param("commIDs", "2") //
						.param("barcodeIDs", "3") //
						.param("rcscNOs", "30") //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testApproveEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8: ???????????????????????????,?????????????????????");
		// ???????????????
		int commodityID = 2;
		int rcscNOs = 85;
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", String.valueOf(commodityID)) //
						.param("rcscNOs", String.valueOf(rcscNOs)) //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");
		ErrorInfo ecOut = new ErrorInfo();
		//
		Commodity commA = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(4);// phoneOfBoss
		rtcsCreate.setShopID(2);
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr, rtcsCreate);
		ReturnCommoditySheet returnCommoditySheetDB = new ReturnCommoditySheet();
		returnCommoditySheetDB = (ReturnCommoditySheet) returnCommoditySheetDB.parse1(o1.getString(BaseAction.KEY_Object));
		//
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		commListBeforeApprove.add(commA);
		BaseReturnCommoditySheetTest.getInfoBeforeApprove(commListBeforeApprove, warehousingMapper, warehousingCommodityMapper, Shared.DBName_Test);
		// ???????????????
		MvcResult mr1 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(i1)) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "1") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		String json = mr1.getResponse().getContentAsString();
		Assert.assertTrue(json.length() == 0, "CASE8????????????!??????????????????null");
	}

	@Test
	public void testApproveEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE9: ????????????????????????????????????");
		// ???????????????
		int commodityID = 2;
		int rcscNOs = 4;
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", String.valueOf(commodityID)) //
						.param("rcscNOs", String.valueOf(rcscNOs)) //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(4);// phoneOfBoss
		rtcsCreate.setShopID(2);
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr, rtcsCreate);
		//
		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commA = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		//
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		commListBeforeApprove.add(commA);
		BaseReturnCommoditySheetTest.getInfoBeforeApprove(commListBeforeApprove, warehousingMapper, warehousingCommodityMapper, Shared.DBName_Test);
		//
		ReturnCommoditySheet returnCommoditySheetDB = new ReturnCommoditySheet();
		returnCommoditySheetDB = (ReturnCommoditySheet) returnCommoditySheetDB.parse1(o1.getString(BaseAction.KEY_Object));
		// ????????????????????????
		ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(i1);
		Map<String, Object> params = rcsc.getDeleteParam(BaseBO.INVALID_CASE_ID, rcsc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetCommodityMapper.delete(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ???????????????
		MvcResult mr1 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), i1 + "") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "0") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ???????????????????????????????????????????????????
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setType(EnumCommodityType.ECT_Normal.getIndex());
		comm.setShelfLife(0);
		comm.setnOStart(Commodity.NO_START_Default);
		comm.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		comm.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		comm.setPurchaseFlag(0);
		comm.setOperatorStaffID(4);
		//
		Map<String, Object> createCommodityparams = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(createCommodityparams);
		//
		Commodity commodity = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		rcsc.setCommodityID(commodity.getID());
		rcsc.setCommodityName(commodity.getName());
		rcsc.setBarcodeID(barcodes.getID());
		rcsc.setNO(1);
		rcsc.setSpecification(commodity.getSpecification());
		rcsc.setPurchasingPrice(commodity.getPriceRetail());
		Map<String, Object> params2 = rcsc.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheetCommodity returnCommoditySheetCommodity = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params2);
		Assert.assertTrue(returnCommoditySheetCommodity != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void testApproveEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE10: ?????????????????????????????????????????????????????????" + FieldFormat.MAX_OneCommodityPrice + "???????????????");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseBarcodesTest.retrieveNBarcodes(createCommodity.getID(), Shared.DBName_Test);
		// ???????????????
		String rcscNOs = "100";
		String commPrices = "11";
		ReturnCommoditySheet returnCommoditySheet = BaseReturnCommoditySheetTest.createExReturnCommoditySheetViaAction(sessionBoss, createCommodity, barcodes1, rcscNOs, commPrices, mvc);
		// ???????????????
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(returnCommoditySheet.getID())) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
						.param("commIDs", String.valueOf(createCommodity.getID())) //
						.param("barcodeIDs", String.valueOf(barcodes1.getID())) //
						.param("rcscNOs", String.valueOf(rcscNOs)) //
						.param("commPrices", String.valueOf(FieldFormat.MAX_OneCommodityPrice + 0.01)) //
						.param("rcscSpecifications", "???") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testApproveEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE11: ?????????????????????????????????????????????????????????" + FieldFormat.MAX_OneCommodityNO + "???????????????");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseBarcodesTest.retrieveNBarcodes(createCommodity.getID(), Shared.DBName_Test);
		// ???????????????
		String rcscNOs = "100";
		String commPrices = "11";
		ReturnCommoditySheet returnCommoditySheet = BaseReturnCommoditySheetTest.createExReturnCommoditySheetViaAction(sessionBoss, createCommodity, barcodes1, rcscNOs, commPrices, mvc);
		// ???????????????
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(returnCommoditySheet.getID())) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
						.param("commIDs", String.valueOf(createCommodity.getID())) //
						.param("barcodeIDs", String.valueOf(barcodes1.getID())) //
						.param("rcscNOs", String.valueOf(FieldFormat.MAX_OneCommodityNO + 1)) //
						.param("commPrices", String.valueOf(commPrices)) //
						.param("rcscSpecifications", "???") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testApproveEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE12: ???????????????????????????????????????????????????????????????????????????");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseBarcodesTest.retrieveNBarcodes(createCommodity.getID(), Shared.DBName_Test);
		// ???????????????
		String rcscNOs = "100";
		String commPrices = "11";
		ReturnCommoditySheet returnCommoditySheet = BaseReturnCommoditySheetTest.createExReturnCommoditySheetViaAction(sessionBoss, createCommodity, barcodes1, rcscNOs, commPrices, mvc);
		// ???????????????
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(returnCommoditySheet.getID())) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
						.param("commIDs", createCommodity.getID() + "," + createCommodity.getID()) //
						.param("barcodeIDs", barcodes1.getID() + "," + barcodes1.getID()) //
						.param("rcscNOs", rcscNOs + "," + rcscNOs) //
						.param("commPrices", commPrices + "," + commPrices) //
						.param("rcscSpecifications", "???,???") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action??????????????????????????????");
	}

	@Test
	public void testApproveEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE13: ????????????????????????????????????????????????????????????????????????????????????");
		// ???????????????????????????checkCreate?????????????????????????????????????????????????????????EC_PartSuccess????????????????????????????????????????????????DB?????????????????????
	}
	
	@Test
	public void testApproveEx14() throws Exception { 
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE14: ??????A?????????????????????????????????B?????????????????????????????????????????????????????????");
		// ???????????????
		int commodityID = 2;
		int rcscNOs = 85;
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", String.valueOf(commodityID)) //
						.param("rcscNOs", String.valueOf(rcscNOs)) //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");
		ErrorInfo ecOut = new ErrorInfo();
		//
		Commodity commA = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(4);// phoneOfBoss
		rtcsCreate.setShopID(2);
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr, rtcsCreate);
		ReturnCommoditySheet returnCommoditySheetDB = new ReturnCommoditySheet();
		returnCommoditySheetDB = (ReturnCommoditySheet) returnCommoditySheetDB.parse1(o1.getString(BaseAction.KEY_Object));
		//
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		commListBeforeApprove.add(commA);
		List<Warehousing> warehousingList = BaseReturnCommoditySheetTest.getInfoBeforeApprove(commListBeforeApprove, warehousingMapper, warehousingCommodityMapper, Shared.DBName_Test);
		// ???????????????
		// ????????????B???????????????
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);

		MvcResult mr1 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(i1)) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "0") //
						.session((MockHttpSession) sessionNewShopBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testApproveEx15() throws Exception { 
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE15: ??????A?????????????????????????????????B(????????????)??????????????????????????????????????????????????????????????????????????????????????????");
		// ???????????????
		int commodityID = 2;
		int rcscNOs = 85;
		//
		// ????????????B???????????????
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		//
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), String.valueOf(shopCreated.getID())) //
						.param("commIDs", String.valueOf(commodityID)) //
						.param("rcscNOs", String.valueOf(rcscNOs)) //
						.param("commPrices", "11.1") //
						.param("rcscSpecifications", "???") //
						.param("barcodeIDs", "3") //
						.session((MockHttpSession) sessionNewShopBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.object.ID");
		ErrorInfo ecOut = new ErrorInfo();
		//
		Commodity commA = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		// ?????????
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(staff2.getID());// phoneOfBoss
		rtcsCreate.setShopID(shopCreated.getID());
		String error = rtcsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????,???????????????" + error);
		ReturnCommoditySheetCP.verifyCreate(mr, rtcsCreate);
		ReturnCommoditySheet returnCommoditySheetDB = new ReturnCommoditySheet();
		returnCommoditySheetDB = (ReturnCommoditySheet) returnCommoditySheetDB.parse1(o1.getString(BaseAction.KEY_Object));
		//
		List<Commodity> commListBeforeApprove = new ArrayList<>();
		commListBeforeApprove.add(commA);
		List<Warehousing> warehousingList = BaseReturnCommoditySheetTest.getInfoBeforeApprove(commListBeforeApprove, warehousingMapper, warehousingCommodityMapper, Shared.DBName_Test);
		// ???????????????

		MvcResult mr1 = mvc.perform(//
				post("/returnCommoditySheet/approveEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_ID(), String.valueOf(i1)) //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_bReturnCommodityListIsModified(), "0") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
		//
		Commodity commB = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commB == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		//
		Assert.assertTrue(commB.getNO() == (commA.getNO() - rcscNOs), "??????????????????????????????");

		// ?????????
		ReturnCommoditySheetCP.verifyApprove(mr1, returnCommoditySheetDB, commodityMapper, commListBeforeApprove, returnCommoditySheetCommodityBO, Shared.DBName_Test, warehousingList, warehousingMapper);
	}

}
