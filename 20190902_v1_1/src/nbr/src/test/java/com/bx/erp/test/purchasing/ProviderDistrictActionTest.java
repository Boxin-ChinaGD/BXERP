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
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.ProviderDistrict;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.ProviderDistrictCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class ProviderDistrictActionTest extends BaseActionTest {

	private static final int INVALID_ID = -1;

	public ProviderDistrict pdCreate() {

		ProviderDistrict providerDistrict = new ProviderDistrict();
		providerDistrict.setName("昆明" + String.valueOf(System.currentTimeMillis()).substring(6));

		Map<String, Object> params = providerDistrict.getCreateParam(BaseBO.INVALID_CASE_ID, providerDistrict);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderDistrict pdCreate = (ProviderDistrict) providerDistrictMapper.create(params);
		//
		providerDistrict.setIgnoreIDInComparision(true);
		if (providerDistrict.compareTo(pdCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		return pdCreate;
	}

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	public static class DataInput {
		private static ProviderDistrict pd = new ProviderDistrict();

		protected static final ProviderDistrict getProviderDistrict() throws CloneNotSupportedException, InterruptedException {
			pd = new ProviderDistrict();
			pd.setName("昆明1" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			return (ProviderDistrict) pd.clone();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常创建");
		ProviderDistrict pd = DataInput.getProviderDistrict();
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 检查点
		ProviderDistrictCP.verifyCreate(mr, pd, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:重复添加该用户（业务上不允许的");
		ProviderDistrict providerDistrictCreate = pdCreate();

		ProviderDistrict pd = new ProviderDistrict();
		pd.setName(providerDistrictCreate.getName());

		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:没有权限");
//		MvcResult mr3 = (MvcResult) mvc.perform(//
//				post("/providerDistrict/createEx.bx") //
//						.param(ProviderDistrict.field.getFIELD_NAME_name(), "三亚" + System.currentTimeMillis() % 1000000) //
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print()) //
//				.andReturn(); //
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	// 运行Jenkins会跑不过,因为售前账号会被设为离职状态,在sit自动化测试CommodityRelatedTest里进行测试
	// @Test
	// public void testCreateEx4() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case4:售前人员进行创建供应商，没有权限");
	// MvcResult mr4 = (MvcResult) mvc.perform(//
	// post("/providerDistrict/createEx.bx") //
	// .param(ProviderDistrict.field.getFIELD_NAME_name(), "三亚" +
	// System.currentTimeMillis() % 1000000) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	// }

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		// ProviderDistrict pd = new ProviderDistrict();
		MvcResult mr = mvc.perform(get("/providerDistrict/retrieve1Ex.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=1") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);

		System.out.println("------------------------case2:没有权限进行操作-----------------------------");
//		MvcResult mr2 = mvc.perform(get("/providerDistrict/retrieve1Ex.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=1") //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();
		// ProviderDistrict pd = new ProviderDistrict();
		MvcResult mr = mvc.perform(get("/providerDistrict/retrieveNEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=1") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);

		System.out.println("---------------------case2:没有权限---------------------------------");
//		MvcResult mr2 = mvc.perform(get("/providerDistrict/retrieveNEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=1") //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);

		Shared.caseLog("case3:查看数据返回是否默认降序");
		MvcResult mr3 = mvc.perform(get("/providerDistrict/retrieveNEx.bx?") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr3);
		JSONObject o = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<BaseModel> bmList = new ProviderDistrict().parseN(o.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (int i = 1; i < bmList.size(); i++) {
			Assert.assertTrue(((ProviderDistrict) bmList.get(i - 1)).getID() > ((ProviderDistrict) bmList.get(i)).getID(), "数据返回错误（非降序）");
		}
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1 正常删除");
		ProviderDistrict providerDistrictCreate = pdCreate();

		ProviderDistrict pd = new ProviderDistrict();
		pd.setID(providerDistrictCreate.getID());
		MvcResult mr = mvc.perform( //
				get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + pd.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 检查点
		ProviderDistrictCP.verifyDelete(pd, providerDistrictBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2 删除的区域还有供应商使用");
		MvcResult mr2 = mvc.perform(get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=1") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		ProviderDistrict providerDistrictCreate = pdCreate();

		ProviderDistrict pd = new ProviderDistrict();
		pd.setID(providerDistrictCreate.getID());
		Shared.caseLog("CASE3 没有权限");
//		MvcResult mr3 = mvc.perform(get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + pd.getID()) //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4 不能删除默认供应商区域，返回错误码7");
		MvcResult mr4 = mvc.perform(get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=1") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
		JSONObject o5 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		String msg = JsonPath.read(o5, "$.msg");
		Assert.assertTrue(ProviderDistrict.ACTION_ERROR_UpdateDelete1.equals(msg), "返回的错误消息不正确，期望的是:" + ProviderDistrict.ACTION_ERROR_UpdateDelete1);
	}

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		ProviderDistrict providerDistrictCreate = pdCreate();

		ProviderDistrict pd = DataInput.getProviderDistrict();
		pd.setID(providerDistrictCreate.getID());

		Shared.caseLog("Case1:正常修改供应商区域");
		MvcResult mr = mvc.perform( //
				post("/providerDistrict/updateEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(pd.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 检查点
		ProviderDistrictCP.verifyUpdate(mr, pd, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		ProviderDistrict providerDistrictCreate = pdCreate();

		ProviderDistrict pd = DataInput.getProviderDistrict();
		pd.setID(providerDistrictCreate.getID());
		pd.setName("广东");

		Shared.caseLog("Case2:修改区域名称重复");
		MvcResult mr2 = mvc.perform(post("/providerDistrict/updateEx.bx") //
				.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
				.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(pd.getID())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		ProviderDistrict pd = DataInput.getProviderDistrict();
		pd.setID(INVALID_ID);

		Shared.caseLog("Case3:修改ID不存在");
		MvcResult mr3 = mvc.perform(post("/providerDistrict/updateEx.bx") //
				.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
				.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(pd.getID())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr3, Shared.WrongFormatForInputFieldMsgForTest, "错误信息与预期的不相符");
	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		ProviderDistrict providerDistrictCreate = pdCreate();

		ProviderDistrict pd = DataInput.getProviderDistrict();
		pd.setID(providerDistrictCreate.getID());

		Shared.caseLog("Case4:没有权限进行操作");
//		MvcResult mr4 = mvc.perform(post("/providerDistrict/updateEx.bx") //
//				.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
//				.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(pd.getID())) //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print()) //
//				.andReturn(); //
//
//		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:默认供应商区域不能修改,返回错误码7");
		ProviderDistrict pd = DataInput.getProviderDistrict();
		pd.setID(1);

		MvcResult mr5 = mvc.perform(post("/providerDistrict/updateEx.bx") //
				.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
				.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(pd.getID())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);
		JSONObject o5 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		String msg = JsonPath.read(o5, "$.msg");
		Assert.assertTrue(ProviderDistrict.ACTION_ERROR_UpdateDelete1.equals(msg), "返回的错误消息不正确，期望的是:" + ProviderDistrict.ACTION_ERROR_UpdateDelete1);
	}

	// 运行Jenkins会跑不过,因为售前账号会被设为离职状态,在sit自动化测试CommodityRelatedTest里进行测试
	// @Test
	// public void testUpdateEx6() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// ProviderDistrict providerDistrictCreate = pdCreate();
	//
	// ProviderDistrict pd = DataInput.getProviderDistrict();
	// pd.setID(providerDistrictCreate.getID());
	//
	// Shared.caseLog("Case6:售前人员修改供应商区域，没有权限进行操作");
	// MvcResult mr4 = mvc.perform(post("/providerDistrict/updateEx.bx") //
	// .param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
	// .param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(pd.getID()))
	// //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	//
	// }
}
