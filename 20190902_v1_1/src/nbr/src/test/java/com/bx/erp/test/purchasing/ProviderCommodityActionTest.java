package com.bx.erp.test.purchasing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class ProviderCommodityActionTest extends BaseActionTest {
	private static final int INVALID_ID = 999999999;

	public ProviderCommodity createProviderCommodity() throws CloneNotSupportedException {
		ProviderCommodity pc = new ProviderCommodity();
		pc.setCommodityID(56);
		pc.setProviderID(3);
		Map<String, Object> deleteParam = pc.getDeleteParam(BaseBO.INVALID_CASE_ID, pc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerCommodityMapper.delete(deleteParam);
		Map<String, Object> createParam = pc.getCreateParam(BaseBO.INVALID_CASE_ID, pc);
		ProviderCommodity bm = (ProviderCommodity) providerCommodityMapper.create(createParam);
		pc.setIgnoreIDInComparision(true);
		if (pc.compareTo(bm) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		return pc;
	}

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testDeleteEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// Case1: 根据商品ID删除
		ProviderCommodity pc = createProviderCommodity();

		MvcResult mr = mvc.perform(get("/providerCommodity/deleteEx.bx?" + ProviderCommodity.field.getFIELD_NAME_commodityID() + "=" + pc.getCommodityID()) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);

		// Case2: 根据供应商ID和商品ID删除
		ProviderCommodity pc2 = createProviderCommodity();

		MvcResult mr2 = mvc
				.perform(get("/providerCommodity/deleteEx.bx?" + ProviderCommodity.field.getFIELD_NAME_providerID() + "=" + pc2.getProviderID() + "&" + ProviderCommodity.field.getFIELD_NAME_commodityID() + "=" + pc2.getCommodityID()) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr2);

		// Case3: 删除一个不存在的商品ID
		MvcResult mr4 = mvc.perform(get("/providerCommodity/deleteEx.bx?" + ProviderCommodity.field.getFIELD_NAME_commodityID() + "=99999") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr4);

		// case4:没有权限进行操作
//		MvcResult mr5 = mvc.perform(get("/providerCommodity/deleteEx.bx?" + ProviderCommodity.field.getFIELD_NAME_commodityID() + "=" + pc.getCommodityID()) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//
//		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// ProviderCommodity pc = new ProviderCommodity();

		Provider p = createProvider();

		// case1:正常添加
		MvcResult mr = mvc.perform(post("/providerCommodity/createEx.bx") //
				.param(ProviderCommodity.field.getFIELD_NAME_commodityID(), "56") //
				.param(ProviderCommodity.field.getFIELD_NAME_providerID(), String.valueOf(p.getID())) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);

		// 因为不是使用事务回滚，所以为了防止重复添加，需要在添加后删除该供应商商品
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		int commID = JsonPath.read(o, "$.providerCommodity.commodityID");

		MvcResult mr3 = mvc.perform(get("/providerCommodity/deleteEx.bx?" + ProviderCommodity.field.getFIELD_NAME_commodityID() + "=" + commID) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr3);

		// case2:重复添加
		MvcResult mr1 = mvc.perform(post("/providerCommodity/createEx.bx") //
				.param(ProviderCommodity.field.getFIELD_NAME_commodityID(), "2") //
				.param(ProviderCommodity.field.getFIELD_NAME_providerID(), "1") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr1);

		Provider p1 = createProvider();
		// case3:添加组合商品
		MvcResult mr2 = mvc.perform(post("/providerCommodity/createEx.bx") //
				.param(ProviderCommodity.field.getFIELD_NAME_commodityID(), "45") //
				.param(ProviderCommodity.field.getFIELD_NAME_providerID(), String.valueOf(p1.getID())) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);

		// case4:添加已删除商品到供应商商品表中，错误码为7
		MvcResult mr5 = mvc.perform(post("/providerCommodity/createEx.bx") //
				.param(ProviderCommodity.field.getFIELD_NAME_commodityID(), "50") //
				.param(ProviderCommodity.field.getFIELD_NAME_providerID(), "2") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);

		// case5.添加一个不存在的商品到供应商商品表中，错误码为7
		MvcResult mr6 = mvc.perform(post("/providerCommodity/createEx.bx") //
				.param(ProviderCommodity.field.getFIELD_NAME_commodityID(), String.valueOf(INVALID_ID)) //
				.param(ProviderCommodity.field.getFIELD_NAME_providerID(), "2") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_BusinessLogicNotDefined);

		// case6.添加一个不存在的供应商到供应商商品表中，错误码为7
		MvcResult mr7 = mvc.perform(post("/providerCommodity/createEx.bx") //
				.param(ProviderCommodity.field.getFIELD_NAME_commodityID(), "20") //
				.param(ProviderCommodity.field.getFIELD_NAME_providerID(), String.valueOf(INVALID_ID)) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_BusinessLogicNotDefined);

		// case7:没有权限进行操作
//		MvcResult mr8 = mvc.perform(post("/providerCommodity/createEx.bx") //
//				.param(ProviderCommodity.field.getFIELD_NAME_commodityID(), "56") //
//				.param(ProviderCommodity.field.getFIELD_NAME_providerID(), "2") //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_NoPermission);
	}

	private Provider createProvider() throws CloneNotSupportedException, InterruptedException {

		Provider p = DataInput.getProvider();
		Map<String, Object> params = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);

		System.out.println("\n------------------------ Case 1: Create a Provider first ------------------------");

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerCreate = (Provider) providerMapper.create(params); // ...
		p.setIgnoreIDInComparision(true);
		if (p.compareTo(providerCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(providerCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		System.out.println(providerCreate == null ? "providerCreate == null" : providerCreate);
		return providerCreate;
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// ProviderCommodity pc = createProviderCommodity();
		MvcResult mr = mvc.perform(get("/providerCommodity/retrieveNEx.bx?" + ProviderCommodity.field.getFIELD_NAME_commodityID() + "=2") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);

		// case2:没有权限进行操作
//		MvcResult mr2 = mvc.perform(get("/providerCommodity/retrieveNEx.bx?" + ProviderCommodity.field.getFIELD_NAME_commodityID() + "=2") //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}
}
