package com.bx.erp.test.purchasing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrder.EnumStatusPurchasingOrder;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.ProviderCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;
import net.sf.json.JSONObject;

@WebAppConfiguration
public class ProviderActionTest extends BaseActionTest {
	// private static final int INVALID_ID = 999999999;

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	public static class DataInput {
		private static Provider providerInput = null;

		protected static final Provider getProvider() throws CloneNotSupportedException, InterruptedException {
			providerInput = new Provider();
			providerInput.setName(Shared.getLongestProviderName("??????"));
			Thread.sleep(1);
			providerInput.setDistrictID(1);
			providerInput.setAddress("?????????????????????????????????");
			providerInput.setContactName("zda");
			providerInput.setMobile(Shared.getValidStaffPhone());

			return (Provider) providerInput.clone();
		}

		protected static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Provider p) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType)//
					.param(Provider.field.getFIELD_NAME_ID(), "" + p.getID()) //
					.param(Provider.field.getFIELD_NAME_name(), p.getName() + "")//
					.param(Provider.field.getFIELD_NAME_districtID(), p.getDistrictID() + "")//
					.param(Provider.field.getFIELD_NAME_address(), p.getAddress())//
					.param(Provider.field.getFIELD_NAME_contactName(), p.getContactName())//
					.param(Provider.field.getFIELD_NAME_mobile(), p.getMobile());//
			return builder;
		}
	}

	private Provider createProvider() throws CloneNotSupportedException, InterruptedException {
		Provider p = DataInput.getProvider();
		Map<String, Object> params = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerCreate = (Provider) providerMapper.create(params); // ...
		p.setIgnoreIDInComparision(true);
		if (p.compareTo(providerCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(providerCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		System.out.println(providerCreate == null ? "providerCreate == null" : providerCreate);
		return (Provider) providerCreate.clone();
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1???????????????");
		Provider p = createProvider();
		//
		MvcResult mr = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), p.getID() + "")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		//
		ProviderCP.verifyDelete(p, providerBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2????????????????????????????????????");
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		MvcResult mr1 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), "2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, "??????????????????????????????????????????", "????????????????????????????????????");
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("case3??????????????????????????????????????????");
		// ?????????????????????
		Provider providerCreate = createProvider();
		//
		// ????????????????????????
		PurchasingOrder po1 = new PurchasingOrder();
		po1.setStatus(EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
		po1.setStaffID(1);
		po1.setProviderID(providerCreate.getID());
		po1.setRemark("Remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Map<String, Object> params1 = po1.getCreateParam(BaseBO.INVALID_CASE_ID, po1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo1 = (PurchasingOrder) purchasingOrderMapper.create(params1);
		po1.setIgnoreIDInComparision(true);
		if (po1.compareTo(createPo1) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(createPo1 != null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		//
		MvcResult mr2 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(providerCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr2, "????????????????????????????????????????????????", "????????????????????????????????????");
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("case4??????????????????????????????????????????");
		// ?????????????????????
		Provider providerCreate1 = createProvider();
		//
		// ?????????????????????
		Warehousing warehousing = new Warehousing();
		warehousing.setStaffID(1);
		warehousing.setProviderID(providerCreate1.getID());
		warehousing.setWarehouseID(1);
		warehousing.setPurchasingOrderID(1);
		Map<String, Object> params2 = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wsCreate = (Warehousing) warehousingMapper.create(params2);
		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(wsCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(wsCreate != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		//
		MvcResult mr3 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(providerCreate1.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr3, "?????????????????????????????????????????????", "????????????????????????????????????");
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("case5:?????????????????????????????????????????????");
		// ?????????????????????
		Provider providerCreate2 = createProvider();
		//
		// ?????????????????????
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setStaffID(3);
		rcs.setProviderID(providerCreate2.getID());
		Map<String, Object> params = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params); // ...
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		//
		MvcResult mr4 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(providerCreate2.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr4, "?????????????????????????????????????????????", "????????????????????????????????????");
	}

	@Test
	public void testDeleteEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("case6???????????????????????????ID");
		MvcResult mr5 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), "999999999")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr5);
	}

	@Test
	public void testDeleteEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7???????????????????????????");
//		// ????????????????????????
//		Provider p = createProvider();
//		MvcResult mr6 = mvc.perform(//
//				get("/provider/deleteEx.bx")//
//						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(p.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//
//		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testDeleteEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("case8??????????????????????????????????????????,????????????");
		// ?????????????????????
		Provider providerCreate = createProvider();
		//
		// ????????????????????????
		PurchasingOrder po1 = new PurchasingOrder();
		po1.setStatus(EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
		po1.setStaffID(1);
		po1.setProviderID(providerCreate.getID());
		po1.setRemark("Remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Map<String, Object> params1 = po1.getCreateParam(BaseBO.INVALID_CASE_ID, po1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo1 = (PurchasingOrder) purchasingOrderMapper.create(params1);
		po1.setIgnoreIDInComparision(true);
		if (po1.compareTo(createPo1) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(createPo1 != null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		//
		// ????????????????????????
		Map<String, Object> deleteParams1 = createPo1.getDeleteParam(BaseBO.INVALID_CASE_ID, createPo1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParams1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Map<String, Object> retrieve1Params = createPo1.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, createPo1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> poList = (List<List<BaseModel>>) purchasingOrderMapper.retrieve1Ex(retrieve1Params);
		List<BaseModel> r1Po = (List<BaseModel>) poList.get(0);
		Assert.assertTrue(r1Po.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieve1Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		MvcResult mr2 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(providerCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		ProviderCP.verifyDelete(providerCreate, providerBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("case9???????????????????????????????????????????????????7");

		MvcResult mr2 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		String msg2 = JsonPath.read(o2, "$.msg");
		Assert.assertTrue(Provider.ACTION_ERROR_UpdateDelete1.equals(msg2), "?????????????????????????????????????????????:" + Provider.ACTION_ERROR_UpdateDelete1);
	}

	// @Test
	// public void testDeleteListEx() throws Exception {
	// session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
	//
	// System.out.println("-----------------------case1:????????????????????????---------------------------");
	// Provider providerCreate = createProvider();
	// //
	// MvcResult mr = mvc.perform(//
	// get("/provider/deleteListEx.bx?providerListID=" + providerCreate.getID())//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("-----------------------case2:???????????????????????????---------------------------");
	// Provider providerCreate2 = createProvider();
	// Provider providerCreate3 = createProvider();
	// //
	// MvcResult mr2 = mvc.perform(//
	// get("/provider/deleteListEx.bx?providerListID=" + providerCreate2.getID() +
	// "," + providerCreate3.getID())//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr2);
	//
	// System.out.println("-----------------------case3:???????????????????????????---------------------------");
	// String msg = "?????????" + 1 + "??????????????????????????????????????????<br />";
	//
	// MvcResult mr3 = mvc.perform(//
	// get("/provider/deleteListEx.bx?providerListID=1")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().isOk()).andDo(print()).andReturn();
	// Shared.checkJSONMsg(mr3, msg, "????????????????????????");
	//
	// System.out.println("-----------------------case4:???????????????????????????---------------------------");
	// String msg1 = "?????????" + 4 + "??????????????????????????????????????????<br />";
	// MvcResult mr4 = mvc.perform(//
	// get("/provider/deleteListEx.bx?providerListID=1,4")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONMsg(mr4, msg + msg1, "????????????????????????");
	//
	// System.out.println("------------------------------Case5:??????0????????????-----------------------");
	// MvcResult mr5 = mvc.perform( //
	// get("/provider/deleteListEx.bx") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// String json3 = mr5.getResponse().getContentAsString();
	// assertTrue(json3.equals(""));
	//
	// System.out.println("-----------------------case6???providerListID???ID???????????????????????????---------------");
	// MvcResult mr6 = mvc.perform( //
	// get("/provider/deleteListEx.bx?providerListID=" + INVALID_ID) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	// Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoError);
	//
	// System.out.println("-----------------------------------Case7:????????????????????????????????????????????????------------------------------");
	// Provider providerCreate4 = createProvider();
	// MvcResult mr7 = mvc.perform( //
	// get("/provider/deleteListEx.bx?providerListID=4" + "," +
	// providerCreate4.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONMsg(mr7, msg1, "????????????????????????");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:????????????????????????------------------------------");
	// Provider providerCreate5 = createProvider();
	// String msg3 = "???????????????????????????????????????<br />";
	//
	// MvcResult mr8 = mvc.perform( //
	// get("/provider/deleteListEx.bx?providerListID=" + providerCreate5.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfManager)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONMsg(mr8, msg3, "????????????????????????");
	// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_PartSuccess);
	//
	// }

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:?????????????????????");
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Provider p1 = DataInput.getProvider();
		MvcResult mr1 = mvc.perform(//
				post("/provider/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_name(), p1.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), p1.getDistrictID() + "")//
						.param(Provider.field.getFIELD_NAME_address(), p1.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), p1.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), p1.getMobile())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr1);
		// ?????????
		ProviderCP.verifyCreate(mr1, p1, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:?????????????????????ID???????????????");
		Provider p2 = DataInput.getProvider();
		p2.setDistrictID(999999);
		MvcResult mr2 = mvc.perform(//
				post("/provider/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_name(), p2.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), String.valueOf(p2.getDistrictID()))//
						.param(Provider.field.getFIELD_NAME_address(), p2.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), p2.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), p2.getMobile())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:?????????????????????????????????????????????");
		Provider p3 = DataInput.getProvider();
		p3.setMobile("13129355410");
		MvcResult mr3 = mvc.perform(//
				post("/provider/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_name(), p3.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), p3.getDistrictID() + "")//
						.param(Provider.field.getFIELD_NAME_address(), p3.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), p3.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), p3.getMobile())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:????????????????????????????????????????????????");
		//
		Provider p4 = DataInput.getProvider();
		p4.setAddress("???  ??? ????????? ??????????????????" + System.currentTimeMillis() % 1000000);
		p4.setContactName("z d a");
		//
		MvcResult mr4 = mvc.perform(//
				DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p4)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr4);
		// ?????????
		ProviderCP.verifyCreate(mr4, p4, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("---------------------------case5:???????????????????????????????????????????????????--------------------------------------");
		Provider p5 = DataInput.getProvider();
		p5.setName(" ??????" + String.valueOf(System.currentTimeMillis()).substring(6) + " ");
		p5.setAddress(" ?????? ????????? ??????????????????" + System.currentTimeMillis() % 1000000 + " ");
		p5.setContactName(" zda ");
		MvcResult mr5 = mvc.perform(//
				DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p5)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:????????????????????????");
//		Provider p6 = DataInput.getProvider();
//		MvcResult mr6 = mvc.perform(//
//				post("/provider/createEx.bx")//
//						.session((MockHttpSession) sessionBoss)//
//						.param(Provider.field.getFIELD_NAME_name(), p6.getName())//
//						.param(Provider.field.getFIELD_NAME_districtID(), p6.getDistrictID() + "")//
//						.param(Provider.field.getFIELD_NAME_address(), p6.getAddress())//
//						.param(Provider.field.getFIELD_NAME_contactName(), p6.getContactName())//
//						.param(Provider.field.getFIELD_NAME_mobile(), p6.getMobile())//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)))//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:?????????????????????,????????????null");
		Provider p7 = DataInput.getProvider();
		p7.setMobile(null);
		MvcResult mr7 = mvc.perform(//
				post("/provider/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_name(), p7.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), p7.getDistrictID() + "")//
						.param(Provider.field.getFIELD_NAME_address(), p7.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), p7.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), p7.getMobile())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr7);
		// ???????????????????????????null???????????????????????????????????????""??????????????????compareTo????????????
		p7.setMobile("");
		// ?????????
		ProviderCP.verifyCreate(mr7, p7, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:?????????????????????(??????11???)");
		Provider p8 = DataInput.getProvider();
		p8.setMobile(String.valueOf(System.currentTimeMillis() % 1000000));
		MvcResult mr8 = mvc.perform(//
				post("/provider/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_name(), p8.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), p8.getDistrictID() + "")//
						.param(Provider.field.getFIELD_NAME_address(), p8.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), p8.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), p8.getMobile())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9:?????????????????????(??????" + Provider.Max_LengthMoile + "???)");
		Provider p9 = DataInput.getProvider();
		p9.setMobile("11325647111111111111" + String.valueOf(System.currentTimeMillis() % 1000000));
		MvcResult mr9 = mvc.perform(//
				post("/provider/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_name(), p9.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), p9.getDistrictID() + "")//
						.param(Provider.field.getFIELD_NAME_address(), p9.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), p9.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), p9.getMobile())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10:????????????????????????????????????<" + Provider.Min_LengthMoile);
		Provider p10 = DataInput.getProvider();
		p10.setMobile("a13660");
		MvcResult mr10 = mvc.perform(//
				post("/provider/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_name(), p10.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), p10.getDistrictID() + "")//
						.param(Provider.field.getFIELD_NAME_address(), p10.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), p10.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), p10.getMobile())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11:???????????????????????????(????????????)");
		Provider p11 = DataInput.getProvider();
		p11.setName("??? ???" + System.currentTimeMillis() % 1000000);
		MvcResult mr11 = mvc.perform(//
				post("/provider/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_name(), p11.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), p11.getDistrictID() + "")//
						.param(Provider.field.getFIELD_NAME_address(), p11.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), p11.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), p11.getMobile())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12:???????????????????????????(??????32???)");
		Provider p12 = DataInput.getProvider();
		p12.setName(Shared.getLongestProviderName("??????") + "111");
		MvcResult mr12 = mvc.perform(//
				post("/provider/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_name(), p12.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), p12.getDistrictID() + "")//
						.param(Provider.field.getFIELD_NAME_address(), p12.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), p12.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), p12.getMobile())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_WrongFormatForInputField);
	}

	// ??????Jenkins????????????,??????????????????????????????????????????,???sit???????????????CommodityRelatedTest???????????????
	// @Test
	// public void testCreateEx13() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case13:??????????????????????????????????????????????????????");
	// Provider p6 = DataInput.getProvider();
	// MvcResult mr13 = mvc.perform(//
	// post("/provider/createEx.bx")//
	// .session((MockHttpSession) session)//
	// .param(Provider.field.getFIELD_NAME_name(), p6.getName())//
	// .param(Provider.field.getFIELD_NAME_districtID(), p6.getDistrictID() + "")//
	// .param(Provider.field.getFIELD_NAME_address(), p6.getAddress())//
	// .param(Provider.field.getFIELD_NAME_contactName(), p6.getContactName())//
	// .param(Provider.field.getFIELD_NAME_mobile(), p6.getMobile())//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale)))//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_NoPermission);
	// }

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Provider providerCreate = createProvider();

		Provider provider = DataInput.getProvider();
		provider.setID(providerCreate.getID());

		Shared.caseLog("CASE1 ?????????????????????");
		MvcResult mr = mvc.perform//
		(//
				post("/provider/updateEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider.getID()))//
						.param(Provider.field.getFIELD_NAME_name(), provider.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), String.valueOf(provider.getDistrictID()))//
						.param(Provider.field.getFIELD_NAME_address(), provider.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), provider.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), provider.getMobile())//
						.param("providerDistrict", "??????" + System.currentTimeMillis() % 1000000)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		// ?????????
		ProviderCP.verifyUpdate(mr, provider, providerDistrictBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Provider providerCreate = createProvider();

		Provider provider = DataInput.getProvider();
		provider.setID(providerCreate.getID());
		provider.setContactName("k k k" + System.currentTimeMillis() % 1000000);
		provider.setAddress("??? ???????????? ???????????? ??????" + System.currentTimeMillis() % 1000000);

		Shared.caseLog("case2:????????????????????????????????????????????????");
		MvcResult mr = mvc.perform//
		(//
				post("/provider/updateEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider.getID()))//
						.param(Provider.field.getFIELD_NAME_name(), provider.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), String.valueOf(provider.getDistrictID()))//
						.param(Provider.field.getFIELD_NAME_address(), provider.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), provider.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), provider.getMobile())//
						.param("providerDistrict", "??????" + System.currentTimeMillis() % 1000000)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		// ?????????
		ProviderCP.verifyUpdate(mr, provider, providerDistrictBO, Shared.DBName_Test);

	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Provider providerCreate = createProvider();

		Provider provider = DataInput.getProvider();
		provider.setID(providerCreate.getID());
		provider.setContactName("k");
		provider.setAddress("???");

		Shared.caseLog("case3:?????????????????????????????????????????????1");
		MvcResult mr = mvc.perform//
		(//
				post("/provider/updateEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider.getID()))//
						.param(Provider.field.getFIELD_NAME_name(), provider.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), String.valueOf(provider.getDistrictID()))//
						.param(Provider.field.getFIELD_NAME_address(), provider.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), provider.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), provider.getMobile())//
						.param("providerDistrict", "??????" + System.currentTimeMillis() % 1000000)//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		// ?????????
		ProviderCP.verifyUpdate(mr, provider, providerDistrictBO, Shared.DBName_Test);

	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:????????????????????????");
//		MvcResult mr = mvc.perform//
//		(//
//				post("/provider/updateEx.bx")//
//						.param(Provider.field.getFIELD_NAME_ID(), "7")//
//						.param(Provider.field.getFIELD_NAME_name(), "??????" + System.currentTimeMillis() % 1000000)//
//						.param(Provider.field.getFIELD_NAME_districtID(), "1")//
//						.param(Provider.field.getFIELD_NAME_address(), "?????????????????????????????????" + System.currentTimeMillis() % 1000000)//
//						.param(Provider.field.getFIELD_NAME_contactName(), "kkk" + System.currentTimeMillis() % 1000000)//
//						.param(Provider.field.getFIELD_NAME_mobile(), Shared.getValidStaffPhone())//
//						.param("providerDistrict", "13125" + System.currentTimeMillis() % 1000000)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Provider providerCreate = createProvider();

		Provider provider = DataInput.getProvider();
		provider.setID(providerCreate.getID());
		provider.setMobile(null);

		Shared.caseLog("case5:????????????????????????????????????null");
		MvcResult mr = mvc.perform//
		(//
				post("/provider/updateEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider.getID()))//
						.param(Provider.field.getFIELD_NAME_name(), provider.getName())//
						.param(Provider.field.getFIELD_NAME_districtID(), String.valueOf(provider.getDistrictID()))//
						.param(Provider.field.getFIELD_NAME_address(), provider.getAddress())//
						.param(Provider.field.getFIELD_NAME_contactName(), provider.getContactName())//
						.param(Provider.field.getFIELD_NAME_mobile(), provider.getMobile())//
						.param("providerDistrict", "??????" + System.currentTimeMillis() % 1000000)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		// ?????????
		provider.setMobile("");
		ProviderCP.verifyUpdate(mr, provider, providerDistrictBO, Shared.DBName_Test);

	}

	@Test
	public void testUpdateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:??????????????????????????????????????????" + Provider.Min_LengthMoile + "??????");
		MvcResult mr = mvc.perform//
		(//
				post("/provider/updateEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), "7")//
						.param(Provider.field.getFIELD_NAME_name(), "??????" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_districtID(), "1")//
						.param(Provider.field.getFIELD_NAME_address(), "?????????????????????????????????" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_contactName(), "kkk" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_mobile(), "1234AA")//
						.param("providerDistrict", "13125" + System.currentTimeMillis() % 1000000)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Provider.FIELD_ERROR_mobile, "??????????????????????????????");
	}

	@Test
	public void testUpdateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:??????????????????????????????????????????" + Provider.Max_LengthMoile + "??????");
		MvcResult mr = mvc.perform//
		(//
				post("/provider/updateEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), "7")//
						.param(Provider.field.getFIELD_NAME_name(), "??????" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_districtID(), "1")//
						.param(Provider.field.getFIELD_NAME_address(), "?????????????????????????????????" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_contactName(), "kkk" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_mobile(), "12345678901111111111111aaa")//
						.param("providerDistrict", "13125" + System.currentTimeMillis() % 1000000)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Provider.FIELD_ERROR_mobile, "??????????????????????????????");
	}

	@Test
	public void testUpdateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:??????????????????????????????????????????");
		MvcResult mr = mvc.perform//
		(//
				post("/provider/updateEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), "7")//
						.param(Provider.field.getFIELD_NAME_name(), "??? ???" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_districtID(), "1")//
						.param(Provider.field.getFIELD_NAME_address(), "?????????????????????????????????" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_contactName(), "kkk" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_mobile(), "12345678901")//
						.param("providerDistrict", "13125" + System.currentTimeMillis() % 1000000)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Provider.FIELD_ERROR_name, "??????????????????????????????");
	}

	@Test
	public void testUpdateEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9:????????????????????????????????????32???");
		MvcResult mr = mvc.perform//
		(//
				post("/provider/updateEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), "7")//
						.param(Provider.field.getFIELD_NAME_name(), Shared.getLongestProviderName("??????") + "111")//
						.param(Provider.field.getFIELD_NAME_districtID(), "1")//
						.param(Provider.field.getFIELD_NAME_address(), "?????????????????????????????????" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_contactName(), "kkk" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_mobile(), "12345678901")//
						.param("providerDistrict", "13125" + System.currentTimeMillis() % 1000000)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Provider.FIELD_ERROR_name, "??????????????????????????????");
	}

	@Test
	public void testUpdateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE10 ???????????????????????????????????????????????????7");
		MvcResult mr = mvc.perform//
		(//
				post("/provider/updateEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), "1")//
						.param(Provider.field.getFIELD_NAME_name(), "??????" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_districtID(), "1")//
						.param(Provider.field.getFIELD_NAME_address(), "?????????????????????????????????" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_contactName(), "kkk" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_mobile(), Shared.getValidStaffPhone())//
						.param("providerDistrict", "13125" + System.currentTimeMillis() % 1000000)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		JSONObject o13 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		String msg13 = JsonPath.read(o13, "$.msg");
		Assert.assertTrue(Provider.ACTION_ERROR_UpdateDelete1.equals(msg13), "?????????????????????????????????????????????:" + Provider.ACTION_ERROR_UpdateDelete1);
	}

	// ??????Jenkins????????????,??????????????????????????????????????????,???sit???????????????CommodityRelatedTest???????????????
	// @Test
	// public void testUpdateEx11() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case11:??????????????????????????????????????????????????????");
	// MvcResult mr = mvc.perform//
	// (//
	// post("/provider/updateEx.bx")//
	// .param(Provider.field.getFIELD_NAME_ID(), "7")//
	// .param(Provider.field.getFIELD_NAME_name(), "??????" + System.currentTimeMillis()
	// % 1000000)//
	// .param(Provider.field.getFIELD_NAME_districtID(), "1")//
	// .param(Provider.field.getFIELD_NAME_address(), "?????????????????????????????????" +
	// System.currentTimeMillis() % 1000000)//
	// .param(Provider.field.getFIELD_NAME_contactName(), "kkk" +
	// System.currentTimeMillis() % 1000000)//
	// .param(Provider.field.getFIELD_NAME_mobile(), Shared.getValidStaffPhone())//
	// .param("providerDistrict", "13125" + System.currentTimeMillis() % 1000000)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale))//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
	//
	// }

	@Test
	public void testRetrieveN() throws Exception {
		mvc.perform(get("/provider/retrieveN.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))).andExpect(status().isOk());
		Shared.printTestMethodStartInfo();
	}

	@Test
	public void testRetrieve1() throws Exception {
		MvcResult mr = mvc.perform(get("/provider/retrieve1Ex.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)).param(Provider.field.getFIELD_NAME_ID(), "2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		String providerName = Shared.getLongestProviderName("??????");
		MvcResult mr6 = mvc.perform(//
				post("/provider/createEx.bx")//
						.param(Provider.field.getFIELD_NAME_name(), providerName)//
						.param(Provider.field.getFIELD_NAME_districtID(), "1")//
						.param(Provider.field.getFIELD_NAME_address(), "?????????" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_contactName(), "ccc" + System.currentTimeMillis() % 1000000)//
						.param(Provider.field.getFIELD_NAME_mobile(), Shared.getValidStaffPhone())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr6);

		// case1:??????sName??????????????????
		MvcResult mr = mvc.perform(post("/provider/retrieveNEx.bx").session((MockHttpSession) sessionBoss).param(Provider.field.getFIELD_NAME_name(), "??????").param("pageIndex", "1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		// case2:??????iDistrictID??????????????????
		MvcResult mr2 = mvc.perform(post("/provider/retrieveNEx.bx").session((MockHttpSession) sessionBoss).param(Provider.field.getFIELD_NAME_districtID(), "1").param("pageIndex", "1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);

		// case3:??????sAddress??????????????????
		MvcResult mr3 = mvc.perform(post("/provider/retrieveNEx.bx").session((MockHttpSession) sessionBoss).param(Provider.field.getFIELD_NAME_address(), "?????????????????????????????????").param("pageIndex", "1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3);

		// case4:??????sContactName??????????????????
		MvcResult mr4 = mvc.perform(post("/provider/retrieveNEx.bx").session((MockHttpSession) sessionBoss).param(Provider.field.getFIELD_NAME_contactName(), "Ada").param("pageIndex", "1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr4);

		// case5:??????sMobile??????????????????
		MvcResult mr5 = mvc.perform(post("/provider/retrieveNEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)).param(Provider.field.getFIELD_NAME_name(), "13129355446").param("pageIndex", "1")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr5);

		// case6:????????????????????????
//		MvcResult mr7 = mvc.perform(//
//				post("/provider/createEx.bx")//
//						.param(Provider.field.getFIELD_NAME_name(), providerName)//
//						.param(Provider.field.getFIELD_NAME_districtID(), "1")//
//						.param(Provider.field.getFIELD_NAME_address(), "?????????" + System.currentTimeMillis() % 1000000)//
//						.param(Provider.field.getFIELD_NAME_contactName(), "ccc" + System.currentTimeMillis() % 1000000)//
//						.param(Provider.field.getFIELD_NAME_mobile(), Shared.getValidStaffPhone())//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//						.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNByFieldsEx() throws Exception {
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// case1.????????????????????????????????????
		MvcResult mr = mvc.perform(
				post("/provider/retrieveNByFieldsEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)).param(Provider.field.getFIELD_NAME_queryKeyword(), "???").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<?> list = JsonPath.read(o, "$.objectList[*].name");
		//
		String string = "???";
		for (int i = 0; i < list.size(); i++) {
			String s1 = (String) list.get(i);
			Assert.assertTrue(s1.contains(string));
		}

		// case2.??????????????????????????????
		MvcResult mr1 = mvc.perform(post("/provider/retrieveNByFieldsEx.bx").session((MockHttpSession) sessionBoss).param(Provider.field.getFIELD_NAME_queryKeyword(), "Giggs").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr1);

		JSONObject o1 = JSONObject.fromObject(mr1.getResponse().getContentAsString()); //
		List<?> list1 = JsonPath.read(o1, "$.objectList[*].contactName");
		System.out.println("1=" + list1);
		//
		string = "Giggs";
		for (int i = 0; i < list1.size(); i++) {
			String s1 = (String) list1.get(i);
			Assert.assertTrue(s1.contains(string));
		}

		// case3.????????????????????????????????????
		MvcResult mr2 = mvc.perform(post("/provider/retrieveNByFieldsEx.bx").session((MockHttpSession) sessionBoss).param(Provider.field.getFIELD_NAME_queryKeyword(), "1312").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);

		JSONObject o2 = JSONObject.fromObject(mr1.getResponse().getContentAsString()); //
		List<?> list2 = JsonPath.read(o2, "$.objectList[*].mobile");
		System.out.println("1=" + list2);
		//
		string = "1312";
		for (int i = 0; i < list2.size(); i++) {
			String s1 = (String) list2.get(i);
			Assert.assertTrue(s1.contains(string));
		}

		// case4.????????????????????????????????????
		MvcResult mr3 = mvc.perform(post("/provider/retrieveNByFieldsEx.bx").session((MockHttpSession) sessionBoss).param(Provider.field.getFIELD_NAME_queryKeyword(), "").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3);

		// case5:????????????????????????
//		MvcResult mr4 = mvc.perform(
//				post("/provider/retrieveNByFieldsEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)).param(Provider.field.getFIELD_NAME_queryKeyword(), "???").contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx() throws Exception {
		Shared.printTestMethodStartInfo();
		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// Provider provider = new Provider();

		System.out.println("----------------------------Case1:???????????????????????????????????????------------------------------------------");
		MvcResult mr1 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckName)) //
						.param(Provider.field.getFIELD_NAME_uniqueField(), "?????????????????????")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		// System.out.println("----------------------------Case7:????????????????????????---------------------------------------");
		// MvcResult mr7 = mvc.perform(//
		// post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc, Staff2Phone))//\
		// .param(provider.getFIELD_NAME_fieldToCheckUnique(), "2")
		// .param(provider.getFIELD_NAME_string1(), "93129355441")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("----------------------------Case2:??????????????????????????????????????????------------------------------------------");
		MvcResult mr2 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckName))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), "???????????????")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------Case3:??????????????????????????????????????????,????????????ID??????????????????????????????????????????ID??????--------------------");
		MvcResult mr3 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Provider.field.getFIELD_NAME_ID(), "2")//
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckName))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), "?????????????????????")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("----------------------------Case4:???????????????????????????????????????------------------------------------------");
		MvcResult mr4 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckMoblie))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), "18129355441")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("----------------------------Case5:??????????????????????????????????????????------------------------------------------");
		MvcResult mr5 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckMoblie))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), "13129355441")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------Case6:??????????????????????????????????????????,????????????ID??????????????????????????????????????????ID??????------------------");
		MvcResult mr6 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Provider.field.getFIELD_NAME_ID(), "2")//
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckMoblie))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), "13129355441")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr6);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------Case7:??????????????????????????????????????????????????????------------------");
		MvcResult mr7 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckName))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), " ??????")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------Case8:????????????????????????????????????(??????" + Provider.Max_LengthMoile + "???)------------------");
		MvcResult mr8 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckMoblie))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), "89012235234re63674576??????????????")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_WrongFormatForInputField);
	}
	// ????????????????????????????????????????????????????????????
	// @Test
	// public void testRetrieveNToCheckUniqueFieldEx9() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("-------------Case9:????????????????????????????????????(???????????????)------------------");
	// MvcResult mr9 = mvc.perform(//
	// post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)// \
	// .param(Provider.field.getFIELD_NAME_fieldToCheckUnique(),
	// String.valueOf(Provider.CASE_CheckMoblie))//
	// .param(Provider.field.getFIELD_NAME_uniqueField(), "1324567890a")//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_WrongFormatForInputField);
	// }

	@Test
	public void testRetrieveNToCheckUniqueFieldEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------Case10:????????????????????????????????????(??????" + Provider.Min_LengthMoile + "???)------------------");
		MvcResult mr10 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckMoblie))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), "13245")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------Case11:???????????????????????????????????????null???------------------");
		String providerName11 = null;
		MvcResult mr11 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckName))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), providerName11)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx12() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------Case12:???????????????????????????????????????????????????32???------------------");
		MvcResult mr12 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckName))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), Shared.getLongestProviderName("??????") + "111")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx13() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------Case13:????????????????????????????????????,??????????????????------------------");
		MvcResult mr13 = mvc.perform(//
				post("/provider/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Provider.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Provider.CASE_CheckName))//
						.param(Provider.field.getFIELD_NAME_uniqueField(), "")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_WrongFormatForInputField);
	}
}
