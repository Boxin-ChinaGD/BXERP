package com.bx.erp.sit.sit1.sg.commodity;

import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.model.commodity.CommodityProperty;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.PackageUnitField;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderDistrict;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseBrandTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.BrandCP;
import com.bx.erp.test.checkPoint.CategoryCP;
import com.bx.erp.test.checkPoint.CategoryParentCP;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.test.checkPoint.PackageUnitCP;
import com.bx.erp.test.checkPoint.ProviderCP;
import com.bx.erp.test.checkPoint.ProviderDistrictCP;
import com.bx.erp.test.commodity.BaseCategoryParentTest;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.testng.annotations.BeforeClass;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class CommodityRelatedTest extends BaseActionTest {

	protected String bossPhone = "";
	protected String companySN = "";
	protected String dbName = "";
	protected AtomicInteger order;
	protected Map<String, BaseModel> commodityRelatedMap;
	protected final int generateStringLength = 9;

	/**
	 * ??????????????????1????????????MvcResult ?????????company????????????????????????????????????????????????????????????????????????????????????
	 * ???????????????????????????????????????????????????company????????????
	 */
	private static MvcResult mvcResult_Company;

	@BeforeClass
	public void beforeClass() {
		super.setUp();
		order = new AtomicInteger();
		commodityRelatedMap = new HashMap<String, BaseModel>();

		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????tear down???????????????
//		ConfigCacheSize providerConfigCacheSize = new ConfigCacheSize();
//		providerConfigCacheSize.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
//		providerConfigCacheSize.setID(BaseCache.Provider_CACHESIZE_ID);
//		providerConfigCacheSize.setName(EnumConfigCacheSizeCache.ECC_ProviderCacheSize.getName());
//		CacheManager.getCache(CommodityListTest.dbNameOfNewCompany1, EnumCacheType.ECT_ConfigCacheSize).write1(providerConfigCacheSize, CommodityListTest.dbNameOfNewCompany1, STAFF_ID4);
		
//		try {
//			sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
			// session = Shared.getStaffLoginSession(mvc, bossPhone, bossPassword,
			// companySN);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
		
		// ????????????????????????????????????????????????setup?????????????????????????????????????????????????????????????????????
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("20");
		ccs.setID(EnumConfigCacheSizeCache.ECC_ProviderCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_ProviderCacheSize.getName());
		CacheManager.getCache(CommodityListTest.dbNameOfNewCompany1, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, CommodityListTest.dbNameOfNewCompany1, STAFF_ID4);
	}

	@Test
	public void createProviderDistrict() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????2???????????????????????????");

		ProviderDistrict pd = new ProviderDistrict();
		pd.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ????????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict2 = pd.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("providerDistrict2", providerDistrict2);
	}

	@Test(dependsOnMethods = "createProviderDistrict")
	public void createProviderDistrictThenUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????3???????????????????????????????????????????????????");

		// ?????????????????????3
		ProviderDistrict pd = new ProviderDistrict();
		pd.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ????????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict3 = pd.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ?????????????????????3
		pd.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr2 = mvc.perform( //
				post("/providerDistrict/updateEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(providerDistrict3.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderDistrictCP.verifyUpdate(mr2, pd, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		providerDistrict3 = pd.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("providerDistrict3", providerDistrict3);
	}

	@Test(dependsOnMethods = "createProviderDistrictThenUpdate")
	public void createProviderDistrictThenDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????4?????????????????????????????????????????????????????????");

		// ?????????????????????4
		ProviderDistrict pd = new ProviderDistrict();
		pd.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ProviderDistrict providerDistrict4 = (ProviderDistrict) pd.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ????????????????????????
		MvcResult mr2 = mvc.perform( //
				get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + providerDistrict4.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderDistrictCP.verifyDelete(providerDistrict4, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createProviderDistrictThenDelete")
	public void createProviderDistrictThenUpdateAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????5??????????????????????????????????????????????????????????????????");

		// ?????????????????????5
		ProviderDistrict pd = new ProviderDistrict();
		pd.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd, CommodityListTest.dbNameOfNewCompany1);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ProviderDistrict providerDistrict5 = (ProviderDistrict) pd.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ???????????????
		pd.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr2 = mvc.perform( //
				post("/providerDistrict/updateEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(providerDistrict5.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderDistrictCP.verifyUpdate(mr2, pd, CommodityListTest.dbNameOfNewCompany1);
		//
		// ???????????????
		MvcResult mr3 = mvc.perform( //
				get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + providerDistrict5.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		ProviderDistrictCP.verifyDelete(providerDistrict5, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createProviderDistrictThenUpdateAndDelete")
	public void createProviderDistrictThenCreateProviderAndDeleteProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????6??????????????????2,?????????3?????????????????????2???????????????????????????");

		// ?????????????????????6
		ProviderDistrict pd = new ProviderDistrict();
		pd.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict6 = pd.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("providerDistrict6", providerDistrict6);
		//
		// ?????????????????????????????????2
		Provider p2 = DataInput.getProvider();
		p2.setDistrictID(providerDistrict6.getID());
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p2)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyCreate(mr2, p2, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Provider provider2 = (Provider) p2.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ?????????????????????????????????3
		Provider p3 = DataInput.getProvider();
		p3.setName(Shared.generateStringByTime(generateStringLength));
		p3.setDistrictID(providerDistrict6.getID());
		MvcResult mr3 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p3)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		ProviderCP.verifyCreate(mr3, p3, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		BaseModel provider3 = p3.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider3", provider3);
		//
		// ???????????????2
		MvcResult mr4 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider2.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr4);
		ProviderCP.verifyDelete(provider2, providerBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createProviderDistrictThenCreateProviderAndDeleteProvider")
	public void createProviderDistrictThenCreateProviderAndDeleteProviderDistrict() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????7??????????????????4????????????????????????7");

		// ?????????????????????7
		ProviderDistrict pd7 = new ProviderDistrict();
		pd7.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd7.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd7, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict7 = pd7.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("providerDistrict7", providerDistrict7);
		//
		// ?????????????????????????????????4
		Provider p4 = DataInput.getProvider();
		p4.setName(Shared.generateStringByTime(generateStringLength));
		p4.setDistrictID(providerDistrict7.getID());
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p4)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyCreate(mr2, p4, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel provider4 = p4.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider4", provider4);
		//
		// ?????????????????????7???????????????
		MvcResult mr3 = mvc.perform(get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + providerDistrict7.getID()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test(dependsOnMethods = "createProviderDistrictThenCreateProviderAndDeleteProviderDistrict")
	public void createProviderDistrictAndCreateProviderThenDeleteProviderAndProviderDistrict() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????8??????????????????5??????????????????5????????????????????????8");

		// ?????????????????????8
		ProviderDistrict pd8 = new ProviderDistrict();
		pd8.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd8.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd8, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict8 = pd8.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ?????????????????????????????????5
		Provider p5 = DataInput.getProvider();
		p5.setName(Shared.generateStringByTime(generateStringLength));
		p5.setDistrictID(providerDistrict8.getID());
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p5)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyCreate(mr2, p5, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel provider5 = p5.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ???????????????5
		MvcResult mr3 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider5.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		ProviderCP.verifyDelete(provider5, providerBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ?????????????????????8
		MvcResult mr4 = mvc.perform( //
				get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + providerDistrict8.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr4);
		ProviderDistrictCP.verifyDelete(providerDistrict8, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createProviderDistrictAndCreateProviderThenDeleteProviderAndProviderDistrict")
	public void updateProviderDistrict() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????2???????????????????????????");

		ProviderDistrict pd2 = (ProviderDistrict) commodityRelatedMap.get("providerDistrict2");
		pd2.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform( //
				post("/providerDistrict/updateEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd2.getName()) //
						.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(pd2.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyUpdate(mr, pd2, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict2 = pd2.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("providerDistrict2", providerDistrict2);
	}

	@Test(dependsOnMethods = "updateProviderDistrict")
	public void updateProviderDistrictThenDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????3????????????????????????????????????????????????");

		// ?????????????????????3
		ProviderDistrict pd3 = (ProviderDistrict) commodityRelatedMap.get("providerDistrict3");
		pd3.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform( //
				post("/providerDistrict/updateEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd3.getName()) //
						.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(pd3.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyUpdate(mr, pd3, CommodityListTest.dbNameOfNewCompany1);
		//
		// ?????????????????????3
		MvcResult mr2 = mvc.perform( //
				get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + pd3.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderDistrictCP.verifyDelete(pd3, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("providerDistrict3");
	}

	@Test(dependsOnMethods = "updateProviderDistrictThenDelete")
	public void updateProviderDistrictThenRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????????????????6???????????????????????????????????????????????????????????????");

		ProviderDistrict pd6 = (ProviderDistrict) commodityRelatedMap.get("providerDistrict6");
		pd6.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform( //
				post("/providerDistrict/updateEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd6.getName()) //
						.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(pd6.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyUpdate(mr, pd6, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict6 = pd6.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("providerDistrict6", providerDistrict6);
		//
		// ??????????????????????????????????????????
		MvcResult mr2 = mvc.perform(get("/providerDistrict/retrieveNEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + providerDistrict6.getID()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr2);
	}

	@Test(dependsOnMethods = "updateProviderDistrictThenRetrieve")
	public void updateProviderThenRetrieveProviderDistrict() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????3????????????2??????????????????2?????????");

		Provider p3 = (Provider) commodityRelatedMap.get("provider3");
		int providerDistrict2ID = commodityRelatedMap.get("providerDistrict2").getID();
		p3.setDistrictID(providerDistrict2ID);
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, p3)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyUpdate(mr, p3, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider3 = p3.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("provider3", provider3);
		//
		// ????????????2?????????
		MvcResult mr2 = mvc.perform(get("/providerDistrict/retrieveNEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + providerDistrict2ID) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr2);
	}

	@Test(dependsOnMethods = "updateProviderThenRetrieveProviderDistrict")
	public void updateProviderDistrictThenCreate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????????????????2??????????????????????????????????????????9");

		// ????????????2?????????
		ProviderDistrict pd2 = (ProviderDistrict) commodityRelatedMap.get("providerDistrict2");
		pd2.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform( //
				post("/providerDistrict/updateEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd2.getName()) //
						.param(ProviderDistrict.field.getFIELD_NAME_ID(), String.valueOf(pd2.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyUpdate(mr, pd2, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict2 = pd2.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("providerDistrict2", providerDistrict2);
		//
		// ??????????????????????????????9
		ProviderDistrict pd9 = new ProviderDistrict();
		pd9.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr2 = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd9.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderDistrictCP.verifyCreate(mr2, pd9, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel providerDistrict9 = pd9.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("providerDistrict9", providerDistrict9);
	}

	@Test(dependsOnMethods = "updateProviderDistrictThenCreate")
	public void deleteProviderDistrict() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????9???????????????????????????");

		ProviderDistrict providerDistrict9 = (ProviderDistrict) commodityRelatedMap.get("providerDistrict9");
		MvcResult mr = mvc.perform( //
				get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + providerDistrict9.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyDelete(providerDistrict9, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "deleteProviderDistrict")
	public void deleteProviderDistrictAndCreateAgain() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????????????????10????????????????????????10?????????????????????????????????11");

		// ?????????????????????10
		ProviderDistrict pd10 = new ProviderDistrict();
		pd10.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd10.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd10, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict10 = pd10.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ?????????????????????10
		MvcResult mr2 = mvc.perform( //
				get("/providerDistrict/deleteEx.bx?" + ProviderDistrict.field.getFIELD_NAME_ID() + "=" + providerDistrict10.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderDistrictCP.verifyDelete(providerDistrict10, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ?????????????????????10??????????????????????????????11
		ProviderDistrict pd11 = new ProviderDistrict();
		pd11.setName(pd10.getName());
		MvcResult mr3 = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd11.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		ProviderDistrictCP.verifyCreate(mr3, pd11, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		BaseModel providerDistrict11 = pd11.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("providerDistrict11", providerDistrict11);
	}

	@Test(dependsOnMethods = "deleteProviderDistrictAndCreateAgain")
	public void createProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????6???????????????????????????");

		Provider p6 = DataInput.getProvider();
		p6.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p6)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p6, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider6 = p6.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider6", provider6);
	}

	@Test(dependsOnMethods = "createProvider")
	public void createProviderAndRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????7??????????????????????????????");

		// ???????????????7
		Provider p7 = DataInput.getProvider();
		p7.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p7)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p7, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider7 = p7.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider7", provider7);
		//
		// ????????????????????????
		String provider7Name = ((Provider) provider7).getName();
		MvcResult mr2 = mvc.perform(post("/provider/retrieveNByFieldsEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_queryKeyword(), provider7Name)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);
	}

	@Test(dependsOnMethods = "createProviderAndRetrieveNByFields")
	public void createProviderAndRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????8???????????????????????????????????????");

		// ???????????????8
		Provider p8 = DataInput.getProvider();
		// p8.setName("????????????");
		// p8.setContactName("??????");
		// p8.setAddress("??????");
		// p8.setMobile("18814126900");
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p8)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p8, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider8 = p8.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider8", provider8);

		// ?????????????????????????????????
		MvcResult mr2 = mvc.perform(post("/provider/retrieveNEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_name(), ((Provider) provider8).getName())//
				.param(Provider.field.getFIELD_NAME_pageIndex(), "1")//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);
	}

	@Test(dependsOnMethods = "createProviderAndRetrieveN")
	public void createProviderAndUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????9?????????????????????????????????????????????????????????");

		// ???????????????9
		Provider p9 = DataInput.getProvider();
		// p9.setName("???????????????");
		// p9.setContactName("?????????");
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p9)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p9, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Provider provider9 = (Provider) p9.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ???????????????9??????
		Provider updateProvider = DataInput.getProvider();
		updateProvider.setID(provider9.getID());
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, updateProvider)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyUpdate(mr2, updateProvider, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel provider9Update = provider9.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider9", provider9Update);
	}

	@Test(dependsOnMethods = "createProviderAndUpdate")
	public void createProviderAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????10???????????????????????????????????????????????????");

		// ???????????????10
		Provider p10 = DataInput.getProvider();
		// p10.setName("???????????????");
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p10)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p10, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Provider provider10 = (Provider) p10.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ???????????????10
		MvcResult mr2 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider10.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyDelete(provider10, providerBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createProviderAndDelete")
	public void createProviderThenUpdateAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????11??????????????????????????????????????????????????????11????????????????????????");

		// ???????????????11
		Provider p11 = DataInput.getProvider();
		// p11.setName("???????????????");
		// p11.setAddress("??????????????????");
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p11)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p11, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Provider provider11 = (Provider) p11.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ???????????????11
		Provider updateProvider = DataInput.getProvider();
		updateProvider.setID(provider11.getID());
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider11)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyUpdate(mr2, provider11, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ???????????????11
		MvcResult mr3 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider11.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		ProviderCP.verifyDelete(provider11, providerBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createProviderThenUpdateAndDelete")
	public void createProviderThenUpdateAndRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????12??????????????????????????????????????????????????????");

		// ???????????????12
		Provider p12 = DataInput.getProvider();
		// p12.setName("???????????????");
		// p12.setContactName("??????");
		// p12.setMobile("18814126910");
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p12)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p12, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Provider provider12 = (Provider) p12.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ???????????????12
		provider12.setAddress(Shared.generateStringByTime(generateStringLength));
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider12)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyUpdate(mr2, provider12, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel provider12Update = p12.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider12", provider12Update);
		//
		// ????????????????????????
		String provider12Name = ((Provider) provider12).getName();
		MvcResult mr3 = mvc.perform(post("/provider/retrieveNByFieldsEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_queryKeyword(), provider12Name)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3);
	}

	@Test(dependsOnMethods = "createProviderThenUpdateAndRetrieveNByFields")
	public void createProviderThenDeleteAndRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????13??????????????????????????????????????????????????????");

		// ???????????????13
		Provider p13 = DataInput.getProvider();
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p13)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p13, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Provider provider13 = (Provider) p13.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ???????????????13
		MvcResult mr2 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider13.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyDelete(provider13, providerBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ????????????????????????
		String provider13Name = ((Provider) provider13).getName();
		MvcResult mr3 = mvc.perform(post("/provider/retrieveNByFieldsEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_queryKeyword(), provider13Name)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3);
	}

	@Test(dependsOnMethods = "createProviderThenDeleteAndRetrieveNByFields")
	public void createProviderAndDeleteThenDeleteAndRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????14?????????????????????????????????????????????14??????????????????????????????");

		// ???????????????14
		Provider p14 = DataInput.getProvider();
		// p14.setName("???????????????1");
		// p14.setContactName("??????");
		// p14.setAddress("????????????");
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p14)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p14, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Provider provider14 = (Provider) p14.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ???????????????14
		provider14.setMobile(Shared.getValidStaffPhone());
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider14)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyUpdate(mr2, provider14, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ???????????????14
		MvcResult mr3 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider14.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		ProviderCP.verifyDelete(provider14, providerBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ?????????????????????14
		String provider14Name = ((Provider) provider14).getName();
		MvcResult mr4 = mvc.perform(post("/provider/retrieveNByFieldsEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_queryKeyword(), provider14Name)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr4);
	}

	@Test(dependsOnMethods = "createProviderAndDeleteThenDeleteAndRetrieveNByFields")
	public void cancelCreateProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????????????????????????????");

	}

	@Test(dependsOnMethods = "cancelCreateProvider")
	public void updateProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????6?????????????????????????????????");

		Provider provider6 = (Provider) commodityRelatedMap.get("provider6");
		provider6.setContactName(Shared.generateStringByTime(generateStringLength));
		provider6.setAddress(Shared.generateStringByTime(generateStringLength));
		provider6.setMobile(Shared.getValidStaffPhone());
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider6)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyUpdate(mr, provider6, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider6Update = provider6.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("provider6", provider6Update);
	}

	@Test(dependsOnMethods = "updateProvider")
	public void updateProviderThenRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????7?????????????????????????????????????????????");

		// ???????????????7
		Provider provider7 = (Provider) commodityRelatedMap.get("provider7");
		provider7.setContactName(Shared.generateStringByTime(generateStringLength));
		provider7.setAddress(Shared.generateStringByTime(generateStringLength));
		provider7.setMobile(Shared.getValidStaffPhone());
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider7)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyUpdate(mr, provider7, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider7Update = provider7.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("provider7", provider7Update);
		//
		// ?????????????????????????????????
		MvcResult mr2 = mvc.perform(post("/provider/retrieveNEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_name(), ((Provider) provider7).getName())//
				.param(Provider.field.getFIELD_NAME_pageIndex(), "1")//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);
	}

	@Test(dependsOnMethods = "updateProviderThenRetrieveN")
	public void updateProviderThenRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????8?????????????????????????????????");

		// ???????????????8
		Provider provider8 = (Provider) commodityRelatedMap.get("provider8");
		provider8.setAddress(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider8)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyUpdate(mr, provider8, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider8Update = provider8.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("provider8", provider8Update);
		//
		// ????????????????????????
		String provider8Name = ((Provider) provider8).getName();
		MvcResult mr2 = mvc.perform(post("/provider/retrieveNByFieldsEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_queryKeyword(), provider8Name)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);
	}

	@Test(dependsOnMethods = "updateProviderThenRetrieveNByFields")
	public void updateProviderThenDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????9?????????????????????????????????????????????????????????");

		// ???????????????9
		Provider provider9 = (Provider) commodityRelatedMap.get("provider9");
		provider9.setContactName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider9)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyUpdate(mr, provider9, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ???????????????9
		MvcResult mr2 = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider9.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyDelete(provider9, providerBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("provider9");
	}

	@Test(dependsOnMethods = "updateProviderThenDelete")
	public void updateProviderThenCreateAnotherProviderWithSameInfo() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????12????????????????????????????????????16????????????????????????????????????????????????");

		// ???????????????12??????
		Provider provider12 = (Provider) commodityRelatedMap.get("provider12");
		provider12.setContactName("?????????");
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider12)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyUpdate(mr, provider12, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider12Update = provider12.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("provider12", provider12Update);
		//
		// ?????????????????????16??????12?????????????????????????????????
		Provider p16 = DataInput.getProvider();
		p16.setName(((Provider) provider12Update).getName());
		p16.setContactName(((Provider) provider12Update).getContactName());
		p16.setMobile(((Provider) provider12Update).getMobile());
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p16)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		//
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
	}

	@Test(dependsOnMethods = "updateProviderThenCreateAnotherProviderWithSameInfo")
	public void cancelUpdateProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????????????????????????????");

	}

	@Test(dependsOnMethods = "cancelUpdateProvider")
	public void deleteProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????6???????????????????????????");

		Provider provider6 = (Provider) commodityRelatedMap.get("provider6");
		MvcResult mr = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider6.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyDelete(provider6, providerBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("provider6");
	}

	@Test(dependsOnMethods = "deleteProvider")
	public void deleteProviderThenCreateAgain() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????16???????????????????????????17??????????????????????????????????????????");

		// ?????????16?????????updateProviderThenCreateAnotherProviderWithSameInfo???????????????????????????
	}

	@Test(dependsOnMethods = "deleteProviderThenCreateAgain")
	public void deleteProviderThenRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????7???????????????????????????");

		// ???????????????7
		Provider provider7 = (Provider) commodityRelatedMap.get("provider7");
		MvcResult mr = mvc.perform(//
				get("/provider/deleteEx.bx")//
						.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(provider7.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyDelete(provider7, providerBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("provider7");
		//
		// ????????????????????????
		String provider7Name = ((Provider) provider7).getName();
		MvcResult mr2 = mvc.perform(post("/provider/retrieveNByFieldsEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_queryKeyword(), provider7Name)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);
	}

	@Test(dependsOnMethods = "deleteProviderThenRetrieveNByFields")
	public void deleteProviderInTheLastPage() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????????????????????????????");

		// ?????????????????????????????? ??????????????????????????????index=1??????
		MvcResult mr2 = mvc.perform(post("/provider/retrieveNEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		JSONArray providerJSONArray = JsonPath.read(jsonObject, "$.objectList");
		Provider p = new Provider();
		List<?> providerList = p.parseN(providerJSONArray);
		// ??????????????????????????????
		for (int i = 0; i < providerList.size(); i++) {
			p = (Provider) providerList.get(i);
			// ????????????????????????
			if ("???????????????".equals(p.getName())) {
				continue;
			}
			MvcResult mr3 = mvc.perform(//
					get("/provider/deleteEx.bx")//
							.param(Provider.field.getFIELD_NAME_ID(), String.valueOf(p.getID()))//
							.contentType(MediaType.APPLICATION_JSON)//
							.session((MockHttpSession) sessionBoss)//
			)//
					.andExpect(status().isOk()).andDo(print()).andReturn();
			// ?????????
			String json = mr3.getResponse().getContentAsString();
			JSONObject o = JSONObject.fromObject(json);
			String err = JsonPath.read(o, "$." + BaseAction.JSON_ERROR_KEY);
			if (err.compareTo(EnumErrorCode.EC_NoError.toString()) == 0) {
				ProviderCP.verifyDelete(p, providerBO, CommodityListTest.dbNameOfNewCompany1);
			} else {
				System.out.println("?????????????????????????????????????????????????????????????????????");
			}
		}
	}

	@Test(dependsOnMethods = "deleteProviderInTheLastPage")
	public void createProviderDistrictAndProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????12??????????????????????????????18????????????????????????");

		// ?????????????????????12
		ProviderDistrict pd12 = new ProviderDistrict();
		pd12.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd12.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd12, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict12 = pd12.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("providerDistrict12", providerDistrict12);
		//
		// ???????????????18
		Provider p18 = DataInput.getProvider();
		p18.setDistrictID(providerDistrict12.getID());
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p18)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyCreate(mr2, p18, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel provider18 = p18.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider18", provider18);
	}

	@Test(dependsOnMethods = "createProviderDistrictAndProvider")
	public void createProviderDistrictAndCreateDuplicatedProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????13??????????????????????????????19?????????????????????20????????????20?????????????????????19??????");

		// ?????????????????????13
		ProviderDistrict pd13 = new ProviderDistrict();
		pd13.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd13.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd13, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict13 = pd13.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("providerDistrict13", providerDistrict13);
		//
		// ???????????????19
		Provider p19 = DataInput.getProvider();
		p19.setDistrictID(providerDistrict13.getID());
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p19)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyCreate(mr2, p19, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel provider19 = p19.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider19", provider19);
		//
		// ???????????????20(??????????????????19??????)
		Provider p20 = DataInput.getProvider();
		p20.setDistrictID(providerDistrict13.getID());
		p20.setName(p19.getName());
		MvcResult mr3 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p20)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_Duplicated);
	}

	@Test(dependsOnMethods = "createProviderDistrictAndCreateDuplicatedProvider")
	public void createProviderThenUpdateAndCreateAgainWithSameName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????14???????????????????????????21?????????????????????????????????????????????21???????????????????????????????????????22");

		// ?????????????????????14
		ProviderDistrict pd14 = new ProviderDistrict();
		pd14.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd14.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderDistrictCP.verifyCreate(mr, pd14, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel providerDistrict14 = pd14.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("providerDistrict14", providerDistrict14);
		//
		// ???????????????21
		Provider p21 = DataInput.getProvider();
		p21.setDistrictID(providerDistrict14.getID());
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p21)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyCreate(mr2, p21, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Provider provider21 = (Provider) p21.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ??????????????????21??????
		Provider updateProvider = DataInput.getProvider();
		updateProvider.setID(provider21.getID());
		MvcResult mr3 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, updateProvider)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		ProviderCP.verifyUpdate(mr3, updateProvider, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		BaseModel provider21Update = updateProvider.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider21", provider21Update);
		//
		// ???????????????22
		Provider p22 = DataInput.getProvider();
		p22.setDistrictID(providerDistrict14.getID());
		MvcResult mr4 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p22)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr4);
		ProviderCP.verifyCreate(mr4, p22, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		BaseModel provider22 = p22.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider22", provider22);
	}

	@Test(dependsOnMethods = "createProviderThenUpdateAndCreateAgainWithSameName")
	public void createProviderThenTurnThePageAndRetrieveNByFields() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????26???27???28???29????????????????????????????????????????????????????????????19");

		// ???????????????26???27???28???29
		Provider p26 = DataInput.getProvider();
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p26)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p26, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider26 = p26.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider26", provider26);
		//
		Provider p27 = DataInput.getProvider();
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p27)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyCreate(mr2, p27, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel provider27 = p27.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider27", provider27);
		//
		Provider p28 = DataInput.getProvider();
		MvcResult mr3 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p28)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		ProviderCP.verifyCreate(mr3, p28, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		BaseModel provider28 = p28.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider28", provider28);
		//
		Provider p29 = DataInput.getProvider();
		MvcResult mr4 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p29)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr4);
		ProviderCP.verifyCreate(mr4, p29, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		BaseModel provider29 = p29.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider29", provider29);
		//
		// ?????????????????????19
		Provider provider19 = (Provider) commodityRelatedMap.get("provider19");
		String provider19Name = provider19.getName();
		MvcResult mr5 = mvc.perform(post("/provider/retrieveNByFieldsEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_queryKeyword(), provider19Name)//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr5);
	}

	@Test(dependsOnMethods = "createProviderThenTurnThePageAndRetrieveNByFields")
	public void TurnThePageThenCreateProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????????????????????????????23");

		Provider p23 = DataInput.getProvider();
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p23)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p23, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider23 = p23.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider23", provider23);
	}

	@Test(dependsOnMethods = "TurnThePageThenCreateProvider")
	public void TurnThePageThenUpdateProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????????????????????????????23??????(??????)");

		Provider provider23 = (Provider) commodityRelatedMap.get("provider23");
		provider23.setContactName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider23)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyUpdate(mr, provider23, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider23Update = provider23.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("provider23", provider23Update);
	}

	@Test(dependsOnMethods = "TurnThePageThenUpdateProvider")
	public void TurnThePageThenDeleteProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????????????????????????????23(??????)");

		// ???????????????????????????23
	}

	@Test(dependsOnMethods = "TurnThePageThenDeleteProvider")
	public void clickProviderDistrictAndTurnThePageThenCreateProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????30,31,32,33,34?????????????????????1????????????????????????1????????????????????????????????????????????????24");

		// ???????????????30,31,32,33,34?????????????????????1
		Provider p30 = DataInput.getProvider();
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p30)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyCreate(mr, p30, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider30 = p30.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider30", provider30);
		//
		Provider p31 = DataInput.getProvider();
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p31)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyCreate(mr2, p31, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel provider31 = p31.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider31", provider31);
		//
		Provider p32 = DataInput.getProvider();
		MvcResult mr3 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p32)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		ProviderCP.verifyCreate(mr3, p32, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		BaseModel provider32 = p32.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider32", provider32);
		//
		Provider p33 = DataInput.getProvider();
		MvcResult mr4 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p33)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr4);
		ProviderCP.verifyCreate(mr4, p33, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		BaseModel provider33 = p33.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider33", provider33);
		//
		Provider p34 = DataInput.getProvider();
		MvcResult mr5 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p34)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr5);
		ProviderCP.verifyCreate(mr5, p34, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		BaseModel provider34 = p34.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider34", provider34);
		//
		// ?????????????????????1????????????????????????????????????????????????24??????
		Provider p24 = DataInput.getProvider();
		MvcResult mr6 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p24)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr6);
		ProviderCP.verifyCreate(mr6, p24, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		BaseModel provider24 = p24.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider24", provider24);
	}

	@Test(dependsOnMethods = "clickProviderDistrictAndTurnThePageThenCreateProvider")
	public void clickProviderDistrictAndTurnThePageThenUpdateProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????1????????????????????????????????????????????????24??????");

		Provider provider24 = (Provider) commodityRelatedMap.get("provider24");
		provider24.setContactName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider24)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyUpdate(mr, provider24, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider24Update = provider24.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("provider24", provider24Update);
	}

	@Test(dependsOnMethods = "clickProviderDistrictAndTurnThePageThenUpdateProvider")
	public void clickProviderDistrictAndTurnThePageThenDeleteProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????1????????????????????????????????????????????????24");

		// ???????????????????????????
	}

	@Test(dependsOnMethods = "clickProviderDistrictAndTurnThePageThenDeleteProvider")
	public void retrieveNByFieldsThenTurnThePageAndCreateProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????????????????????????????????????????25");

		MvcResult mr = mvc.perform(post("/provider/retrieveNByFieldsEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_queryKeyword(), "???")//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		// ?????????????????????????????????25
		Provider p25 = DataInput.getProvider();
		p25.setName("???" + Shared.generateStringByTime(generateStringLength));
		MvcResult mr6 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p25)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr6);
		ProviderCP.verifyCreate(mr6, p25, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		BaseModel provider25 = p25.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("provider25", provider25);
	}

	@Test(dependsOnMethods = "retrieveNByFieldsThenTurnThePageAndCreateProvider")
	public void retrieveNByFieldsThenTurnThePageAndUpdateProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????????????????????????????????????????25");

		Provider p25 = (Provider) commodityRelatedMap.get("provider25");
		MvcResult mr = mvc.perform(post("/provider/retrieveNByFieldsEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param(Provider.field.getFIELD_NAME_queryKeyword(), "???")//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<?> providerList = JsonPath.read(jsonObject, "$.objectList[*]");
		Provider provider25 = new Provider();
		Boolean flag = false;
		for (int i = 0; i < providerList.size(); i++) {
			provider25 = (Provider) provider25.parse1(providerList.get(i).toString());
			if ((p25.getName()).equals(provider25.getName())) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "??????????????????");
		//
		provider25.setContactName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr2 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider25)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		ProviderCP.verifyUpdate(mr2, provider25, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel provider25Update = provider25.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("provider25", provider25Update);
	}

	@Test(dependsOnMethods = "retrieveNByFieldsThenTurnThePageAndUpdateProvider")
	public void retrieveNByFieldsThenTurnThePageAndDeleteProvider() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????????????????????????????????????????25");

		// ???????????????????????????????????????????????????
	}

	@Test(dependsOnMethods = "retrieveNByFieldsThenTurnThePageAndDeleteProvider")
	public void updateProviderThenTurnThePageAndSave() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????22?????????????????????????????????");

		Provider provider22 = (Provider) commodityRelatedMap.get("provider22");
		provider22.setAddress(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/updateEx.bx", MediaType.APPLICATION_JSON, provider22)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ?????????
		Shared.checkJSONErrorCode(mr);
		ProviderCP.verifyUpdate(mr, provider22, providerDistrictBO, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel provider22Update = provider22.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("provider22", provider22Update);
	}

	@Test(dependsOnMethods = "updateProviderThenTurnThePageAndSave")
	public void createBrand() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????2???????????????????????????");

		// ????????????2
		Brand b2 = BaseBrandTest.DataInput.getBrand();
		Brand brand2 = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b2, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("brand2", brand2);
	}

	@Test(dependsOnMethods = "createBrand")
	public void createBrandAndUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????3????????????????????????3??????????????????????????????");

		// ????????????3
		Brand b3 = BaseBrandTest.DataInput.getBrand();
		Brand brand3 = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b3, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		// ????????????3
		brand3.setName(Shared.generateStringByTime(generateStringLength));
		Brand brand3Update = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand3, false, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("brand3", brand3Update);
	}

	@Test(dependsOnMethods = "createBrandAndUpdate")
	public void createBrandAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????4????????????????????????????????????????????????");

		// ????????????4
		Brand b4 = BaseBrandTest.DataInput.getBrand();
		Brand brand4 = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b4, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		// ?????? ??????4
		MvcResult mr2 = mvc.perform(//
				get("/brandSync/deleteEx.bx")//
						.param(Brand.field.getFIELD_NAME_ID(), String.valueOf(brand4.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		BrandCP.verifyDelete(brand4, posBO, brandBO, brandSyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createBrandAndDelete")
	public void createBrandThenUpdateAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????5??????????????????????????????????????????????????????????????????");

		// ????????????5
		Brand b5 = BaseBrandTest.DataInput.getBrand();
		Brand brand5 = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b5, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		// ????????????5
		brand5.setName(Shared.generateStringByTime(generateStringLength));
		BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand5, false, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ?????? ??????5
		MvcResult mr3 = mvc.perform(//
				get("/brandSync/deleteEx.bx")//
						.param(Brand.field.getFIELD_NAME_ID(), String.valueOf(brand5.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		BrandCP.verifyDelete(brand5, posBO, brandBO, brandSyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createBrandThenUpdateAndDelete")
	public void cancelCreateBrand() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????6????????????????????????");

	}

	@Test(dependsOnMethods = "cancelCreateBrand")
	public void updateBrand() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????3???????????????????????????");

		Brand brand3 = (Brand) commodityRelatedMap.get("brand3");
		brand3.setName(Shared.generateStringByTime(generateStringLength));
		Brand brand3Update = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand3, false, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.replace("brand3", brand3Update);
	}

	@Test(dependsOnMethods = "updateBrand")
	public void updateBrandAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????3??????????????????????????????????????????????????????");

		// ????????????3
		Brand brand3 = (Brand) commodityRelatedMap.get("brand3");
		brand3.setName(Shared.generateStringByTime(generateStringLength));
		BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand3, false, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ????????????3
		MvcResult mr2 = mvc.perform(//
				get("/brandSync/deleteEx.bx")//
						.param(Brand.field.getFIELD_NAME_ID(), String.valueOf(brand3.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		BrandCP.verifyDelete(brand3, posBO, brandBO, brandSyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("brand3");
	}

	@Test(dependsOnMethods = "updateBrandAndDelete")
	public void updateBrandThenCreateBrandWithSameNameBeforeUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????2???????????????????????????7??????????????????????????????????????????");

		// ????????????2
		Brand brand2 = (Brand) commodityRelatedMap.get("brand2");
		brand2.setName(Shared.generateStringByTime(generateStringLength));
		Brand brand2Update = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand2, false, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.replace("brand2", brand2Update);
		//
		// ????????????7
		Brand b7 = BaseBrandTest.DataInput.getBrand();
		Brand brand7 = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b7, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("brand7", brand7);
	}

	@Test(dependsOnMethods = "updateBrandThenCreateBrandWithSameNameBeforeUpdate")
	public void deleteBrand() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????7???????????????????????????");

		// ????????????7
		Brand brand7 = (Brand) commodityRelatedMap.get("brand7");
		MvcResult mr = mvc.perform(//
				get("/brandSync/deleteEx.bx")//
						.param(Brand.field.getFIELD_NAME_ID(), String.valueOf(brand7.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		BrandCP.verifyDelete(brand7, posBO, brandBO, brandSyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "deleteBrand")
	public void deleteBrandThenCreateBrandWithSameName() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????7???????????????????????????8????????????????????????????????????");

		// ?????????????????????7
		Brand brand7 = (Brand) commodityRelatedMap.get("brand7");
		brand7 = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand7, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.replace("brand7", (BaseModel) brand7);
		//
		// ????????????7
		MvcResult mr2 = mvc.perform(//
				get("/brandSync/deleteEx.bx")//
						.param(Brand.field.getFIELD_NAME_ID(), String.valueOf(brand7.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		BrandCP.verifyDelete(brand7, posBO, brandBO, brandSyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
		//
		commodityRelatedMap.remove("brand7");
		//
		// ?????????????????????8????????????????????????????????????
		Brand b8 = BaseBrandTest.DataInput.getBrand();
		b8.setName(brand7.getName());
		Brand brand8 = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b8, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("brand8", brand8);
	}

	@Test(dependsOnMethods = "deleteBrandThenCreateBrandWithSameName")
	public void createCategoryParent() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????13???????????????????????????");

		CategoryParent cp13 = new CategoryParent();
		cp13.setName(Shared.generateStringByTime(generateStringLength));
		//
		CategoryParent categoryParent13 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cp13, true, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("categoryParent13", categoryParent13);
	}

	@Test(dependsOnMethods = "createCategoryParent")
	public void createCategoryParentAndUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????14??????????????????????????????14???????????????????????????");

		// ??????????????????14
		CategoryParent cp14 = new CategoryParent();
		cp14.setName(Shared.generateStringByTime(generateStringLength));
		//
		CategoryParent categoryParent14 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cp14, true, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		//
		/// ??????????????????14
		categoryParent14.setName(Shared.generateStringByTime(generateStringLength));
		CategoryParent categoryParent14Update = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(categoryParent14, false, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("categoryParent14", categoryParent14Update);
	}

	@Test(dependsOnMethods = "createCategoryParentAndUpdate")
	public void createCategoryParentAndDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????15???????????????????????????15???????????????????????????");

		// ??????????????????15
		CategoryParent cp15 = new CategoryParent();
		cp15.setName(Shared.generateStringByTime(generateStringLength));
		//
		CategoryParent categoryParent15 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cp15, true, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		//
		// ??????????????????15
		MvcResult mr2 = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParent15.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		List<BaseModel> categoryList = new ArrayList<>();
		CategoryParentCP.verifyDelete(categoryParent15, categoryList, categoryParentBO, categoryBO, posBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createCategoryParentAndDelete")
	public void createCategoryParentAndUpdateThenDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????16???????????????????????????16???????????????????????????16????????????????????????");

		// ??????????????????16
		CategoryParent cp16 = new CategoryParent();
		cp16.setName(Shared.generateStringByTime(generateStringLength));
		//
		CategoryParent categoryParent16 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cp16, true, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		//
		// ??????????????????16
		categoryParent16.setName(Shared.generateStringByTime(generateStringLength));
		CategoryParent categoryParent16Update = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(categoryParent16, false, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		//
		// ??????????????????16
		MvcResult mr3 = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParent16Update.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		List<BaseModel> categoryList = new ArrayList<>();
		CategoryParentCP.verifyDelete(categoryParent16Update, categoryList, categoryParentBO, categoryBO, posBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createCategoryParentAndUpdateThenDelete")
	public void updateCategoryParent() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????14???????????????????????????");

		// ??????????????????14
		CategoryParent categoryParent14 = (CategoryParent) commodityRelatedMap.get("categoryParent14");
		categoryParent14.setName(Shared.generateStringByTime(generateStringLength));
		CategoryParent categoryParent14Update = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(categoryParent14, false, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.replace("categoryParent14", categoryParent14Update);
	}

	@Test(dependsOnMethods = "updateCategoryParent")
	public void updateCategoryParentThenDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????14??????????????????????????????14????????????????????????");

		// ??????????????????14
		CategoryParent categoryParent14 = (CategoryParent) commodityRelatedMap.get("categoryParent14");
		categoryParent14.setName(Shared.generateStringByTime(generateStringLength));
		CategoryParent categoryParent14Update = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(categoryParent14, false, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		//
		// ??????????????????14
		MvcResult mr2 = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParent14Update.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		List<BaseModel> categoryList = new ArrayList<>();
		CategoryParentCP.verifyDelete(categoryParent14Update, categoryList, categoryParentBO, categoryBO, posBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("categoryParent14");
	}

	@Test(dependsOnMethods = "updateCategoryParentThenDelete")
	public void updateCategoryParentThenCreateWithSameNameBefore() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????13?????????????????????????????????17??????????????????????????????)");

		// ??????????????????13
		CategoryParent categoryParent13 = (CategoryParent) commodityRelatedMap.get("categoryParent13");
		categoryParent13.setName(Shared.generateStringByTime(generateStringLength));
		CategoryParent categoryParent13Update = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(categoryParent13, false, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.replace("categoryParent13", categoryParent13Update);
		//
		// ????????????????????????17
		CategoryParent cp17 = new CategoryParent();
		cp17.setName(Shared.generateStringByTime(generateStringLength));
		//
		CategoryParent categoryParent17 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cp17, true, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("categoryParent17", categoryParent17);
	}

	@Test(dependsOnMethods = "updateCategoryParentThenCreateWithSameNameBefore")
	public void deleteCategoryParent() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????13???????????????????????????");

		CategoryParent categoryParent13 = (CategoryParent) commodityRelatedMap.get("categoryParent13");
		MvcResult mr = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParent13.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		List<BaseModel> categoryList = new ArrayList<>();
		CategoryParentCP.verifyDelete(categoryParent13, categoryList, categoryParentBO, categoryBO, posBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("categoryParent13");
	}

	@Test(dependsOnMethods = "deleteCategoryParent")
	public void deleteCategoryParentThenCreateWithSameNameBefore() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????17?????????????????????????????????18??????????????????????????????");

		// ??????????????????17
		CategoryParent categoryParent17 = (CategoryParent) commodityRelatedMap.get("categoryParent17");
		MvcResult mr = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParent17.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		List<BaseModel> categoryList = new ArrayList<>();
		CategoryParentCP.verifyDelete(categoryParent17, categoryList, categoryParentBO, categoryBO, posBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("categoryParent17");
		//
		// ??????????????????18??????????????????????????????
		CategoryParent cp18 = new CategoryParent();
		cp18.setName(Shared.generateStringByTime(generateStringLength));
		//
		CategoryParent categoryParent18 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cp18, true, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("categoryParent18", categoryParent18);
	}

	@Test(dependsOnMethods = "deleteCategoryParentThenCreateWithSameNameBefore")
	public void createCategory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????53(??????????????????18)???????????????????????????");

		CategoryParent categoryParent18 = (CategoryParent) commodityRelatedMap.get("categoryParent18");
		Category c53 = BaseCategoryParentTest.DataInput.getCategory();
		c53.setParentID(categoryParent18.getID());
		c53.setName(Shared.generateStringByTime(generateStringLength));
		Category category53 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c53, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("category53", category53);
	}

	@Test(dependsOnMethods = "createCategory")
	public void createCategoryAndUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????54(????????????18)??????????????????????????????54???????????????????????????");

		// ??????????????????54
		CategoryParent categoryParent18 = (CategoryParent) commodityRelatedMap.get("categoryParent18");
		Category c54 = BaseCategoryParentTest.DataInput.getCategory();
		c54.setParentID(categoryParent18.getID());
		c54.setName(Shared.generateStringByTime(generateStringLength));
		Category category54 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c54, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		// ??????????????????54
		category54.setName(Shared.generateStringByTime(generateStringLength));
		Category category54Update = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category54, false, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("category54", category54Update);
	}

	@Test(dependsOnMethods = "createCategoryAndUpdate")
	public void createCategoryThenDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????55(????????????18)???????????????????????????55???????????????????????????");

		// ??????????????????55
		CategoryParent categoryParent18 = (CategoryParent) commodityRelatedMap.get("categoryParent18");
		Category c55 = BaseCategoryParentTest.DataInput.getCategory();
		c55.setParentID(categoryParent18.getID());
		c55.setName(Shared.generateStringByTime(generateStringLength));
		Category category55 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c55, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		// ??????????????????55
		MvcResult mr2 = mvc.perform(//
				get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + category55.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		CategoryCP.verifyDelete(category55, posBO, categoryBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createCategoryThenDelete")
	public void createCategoryAndUpdateThenDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????56(????????????18)???????????????????????????56???????????????????????????56????????????????????????");

		// ??????????????????56
		CategoryParent categoryParent18 = (CategoryParent) commodityRelatedMap.get("categoryParent18");
		Category c56 = BaseCategoryParentTest.DataInput.getCategory();
		c56.setParentID(categoryParent18.getID());
		c56.setName(Shared.generateStringByTime(generateStringLength));
		Category category56 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c56, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);
		// ??????????????????56
		category56.setName(Shared.generateStringByTime(generateStringLength));
		Category category56Update = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category56, false, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);

		// ??????????????????56
		MvcResult mr3 = mvc.perform(//
				get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + category56Update.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		CategoryCP.verifyDelete(category56Update, posBO, categoryBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createCategoryAndUpdateThenDelete")
	public void updateCategory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????54???????????????????????????");

		Category category54 = (Category) commodityRelatedMap.get("category54");
		category54.setName(Shared.generateStringByTime(generateStringLength));
		Category category54Update = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category54, false, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);

		commodityRelatedMap.replace("category54", category54Update);
	}

	@Test(dependsOnMethods = "updateCategory")
	public void updateCategoryThenDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????54??????????????????????????????54????????????????????????");

		// ??????????????????54
		Category category54 = (Category) commodityRelatedMap.get("category54");
		category54.setName(Shared.generateStringByTime(generateStringLength));
		Category category54Update = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category54, false, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);

		//
		// ??????????????????54
		MvcResult mr2 = mvc.perform(//
				get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + category54Update.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		CategoryCP.verifyDelete(category54Update, posBO, categoryBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "updateCategoryThenDelete")
	public void updateCategoryThenCreateWithSameNameBefore() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????53?????????????????????????????????57????????????????????????????????????????????????????????????");

		// ??????????????????53
		Category category53 = (Category) commodityRelatedMap.get("category53");
		String category53Name = category53.getName();// ??????????????????57?????????
		category53.setName(Shared.generateStringByTime(generateStringLength));
		Category category53Update = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category53, false, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);

		commodityRelatedMap.replace("category53", category53Update);
		//
		// ????????????????????????57????????????????????????????????????????????????????????????
		Category c57 = BaseCategoryParentTest.DataInput.getCategory();
		c57.setParentID(((Category) category53Update).getParentID());
		c57.setName(category53Name);
		Category category57 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c57, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);

		commodityRelatedMap.put("category57", category57);
	}

	@Test(dependsOnMethods = "updateCategoryThenCreateWithSameNameBefore")
	public void deleteCategoryThenCreateWithSameNameBefore() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????57?????????????????????????????????58(?????????????????????58??????????????????????????????57????????????)");

		// ??????????????????57
		Category category57 = (Category) commodityRelatedMap.get("category57");
		MvcResult mr = mvc.perform(//
				get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + category57.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		CategoryCP.verifyDelete(category57, posBO, categoryBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("category57");
		//
		// ??????????????????58
		Category c58 = BaseCategoryParentTest.DataInput.getCategory();
		c58.setParentID(category57.getParentID());
		c58.setName(category57.getName());
		Category category58 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c58, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);

		commodityRelatedMap.put("category58", category58);
	}

	@Test(dependsOnMethods = "deleteCategoryThenCreateWithSameNameBefore")
	public void deleteCategory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????58???????????????????????????");

		// ??????????????????58
		Category category58 = (Category) commodityRelatedMap.get("category58");
		MvcResult mr = mvc.perform(//
				get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + category58.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr);
		CategoryCP.verifyDelete(category58, posBO, categoryBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("category58");
	}

	@Test(dependsOnMethods = "deleteCategory")
	public void createCategoryParentAndCategory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????19???????????????????????????59????????????????????????");

		// ??????????????????19
		CategoryParent cp19 = new CategoryParent();
		cp19.setName(Shared.generateStringByTime(generateStringLength));
		//
		CategoryParent categoryParent19 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cp19, true, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("categoryParent19", categoryParent19);
		//
		// ??????????????????59(????????????19)
		Category c59 = BaseCategoryParentTest.DataInput.getCategory();
		c59.setParentID(categoryParent19.getID());
		c59.setName(Shared.generateStringByTime(generateStringLength));
		Category category59 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c59, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);

		commodityRelatedMap.put("category59", category59);
	}

	@Test(dependsOnMethods = "createCategoryParentAndCategory")
	public void retrieveCategoryParentAndDeleteCategory() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????19???????????????????????????????????????59??????????????????????????????");

		// ??????????????????19
		CategoryParent categoryParent19 = (CategoryParent) commodityRelatedMap.get("categoryParent19");
		Category category59 = (Category) commodityRelatedMap.get("category59");
		MvcResult mr = mvc.perform(//
				post("/category/retrieveNByParent.bx")//
						.param(CategoryParent.field.getFIELD_NAME_ID(), String.valueOf(categoryParent19.getID()))//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<?> categoryList = JsonPath.read(jsonObject, "categoryList");
		Category categoryOfDB = new Category();
		boolean flag = false;
		for (int i = 0; i < categoryList.size(); i++) {
			categoryOfDB = (Category) category59.parse1(categoryList.get(i).toString());
			if ((categoryOfDB.getName()).equals(category59.getName())) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "????????????59(??????)?????????");
		//
		// ??????????????????59
		MvcResult mr2 = mvc.perform(//
				get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + categoryOfDB.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		CategoryCP.verifyDelete(categoryOfDB, posBO, categoryBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
		//
		commodityRelatedMap.remove("category59");
		//
		// ?????????????????????19
		MvcResult mr3 = mvc.perform(//
				post("/category/retrieveNByParent.bx")//
						.param(CategoryParent.field.getFIELD_NAME_ID(), String.valueOf(categoryParent19.getID()))//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
	}

	@Test(dependsOnMethods = "retrieveCategoryParentAndDeleteCategory")
	public void createCategoryParentAndCategoryThenDeleteCategoryAndCategoryParent() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????20?????????????????????60(????????????20)?????????????????????60?????????????????????20");

		// ??????????????????20
		CategoryParent cp20 = new CategoryParent();
		cp20.setName(Shared.generateStringByTime(generateStringLength));
		//
		CategoryParent categoryParent20 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cp20, true, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		//
		// ??????????????????60(????????????20)
		Category c60 = BaseCategoryParentTest.DataInput.getCategory();
		c60.setParentID(categoryParent20.getID());
		c60.setName(Shared.generateStringByTime(generateStringLength));
		Category category60 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c60, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);

		// ??????????????????60
		MvcResult mr3 = mvc.perform(//
				get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + category60.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		CategoryCP.verifyDelete(category60, posBO, categoryBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ??????????????????20
		MvcResult mr4 = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParent20.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr4);
		List<BaseModel> categoryList = new ArrayList<>();
		categoryList.add(category60);
		CategoryParentCP.verifyDelete(categoryParent20, categoryList, categoryParentBO, categoryBO, posBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createCategoryParentAndCategoryThenDeleteCategoryAndCategoryParent")
	public void createCategoryParentAndCategoryThenDeleteCategoryAndRetrieveCategoryParent() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????21?????????????????????61?????????????????????61?????????????????????21");

		// ??????????????????21
		CategoryParent cp21 = new CategoryParent();
		cp21.setName(Shared.generateStringByTime(generateStringLength));
		//
		CategoryParent categoryParent21 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cp21, true, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.put("categoryParent21", categoryParent21);
		//
		// ??????????????????61(????????????21)
		Category c61 = BaseCategoryParentTest.DataInput.getCategory();
		c61.setParentID(categoryParent21.getID());
		c61.setName(Shared.generateStringByTime(generateStringLength));
		Category category61 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c61, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);

		// ??????????????????61
		MvcResult mr3 = mvc.perform(//
				get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + category61.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		CategoryCP.verifyDelete(category61, posBO, categoryBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
		//
		// ??????????????????21
		MvcResult mr4 = mvc.perform(//
				post("/category/retrieveNByParent.bx")//
						.param(CategoryParent.field.getFIELD_NAME_ID(), String.valueOf(categoryParent21.getID()))//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);
	}

	@Test(dependsOnMethods = "createCategoryParentAndCategoryThenDeleteCategoryAndRetrieveCategoryParent")
	public void createCategoryParentAndCategoryThenDeleteCategoryParent() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "??????????????????22?????????????????????62?????????????????????22");

		// ??????????????????22
		CategoryParent cp22 = new CategoryParent();
		cp22.setName(Shared.generateStringByTime(generateStringLength));
		//
		CategoryParent categoryParent22 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cp22, true, mvc, sessionBoss, CommodityListTest.dbNameOfNewCompany1);
		//
		// ??????????????????62(????????????22)
		Category c62 = BaseCategoryParentTest.DataInput.getCategory();
		c62.setParentID(categoryParent22.getID());
		c62.setName(Shared.generateStringByTime(generateStringLength));
		Category category62 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c62, true, mvc, sessionBoss, mapBO, CommodityListTest.dbNameOfNewCompany1);

		//
		// ??????????????????22
		MvcResult mr3 = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParent22.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		List<BaseModel> categoryList = new ArrayList<>();
		categoryList.add(category62);
		CategoryParentCP.verifyDelete(categoryParent22, categoryList, categoryParentBO, categoryBO, posBO, categorySyncCacheBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createCategoryParentAndCategoryThenDeleteCategoryParent")
	public void createPackageUnit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????8???????????????????????????");

		PackageUnit pu8 = new PackageUnit();
		pu8.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(new PackageUnitField().getFIELD_NAME_name(), pu8.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		PackageUnitCP.verifyCreate(mr, pu8, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel packageUnit8 = pu8.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("packageUnit8", packageUnit8);
	}

	@Test(dependsOnMethods = "createPackageUnit")
	public void createPackageUnitThenUpdate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????9????????????????????????????????????9");

		// ??????????????????9
		PackageUnit pu9 = new PackageUnit();
		pu9.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(new PackageUnitField().getFIELD_NAME_name(), pu9.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		PackageUnitCP.verifyCreate(mr, pu9, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PackageUnit packageUnit9 = (PackageUnit) pu9.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ??????????????????9
		packageUnit9.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr2 = mvc.perform(//
				post("/packageUnit/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(PackageUnit.field.getFIELD_NAME_ID(), String.valueOf(packageUnit9.getID()))//
						.param(PackageUnit.field.getFIELD_NAME_name(), packageUnit9.getName())//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		PackageUnitCP.verifyUpdate(mr2, packageUnit9, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel packageUnit9Update = pu9.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("packageUnit9", packageUnit9Update);
	}

	@Test(dependsOnMethods = "createPackageUnitThenUpdate")
	public void createPackageUnitThenUpdateAndReopen() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????10???????????????s????????????10???????????????????????????");

		// ??????????????????10
		PackageUnit pu10 = new PackageUnit();
		pu10.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(new PackageUnitField().getFIELD_NAME_name(), pu10.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		PackageUnitCP.verifyCreate(mr, pu10, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PackageUnit packageUnit10 = (PackageUnit) pu10.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ??????????????????10
		packageUnit10.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr2 = mvc.perform(//
				post("/packageUnit/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(PackageUnit.field.getFIELD_NAME_ID(), String.valueOf(packageUnit10.getID()))//
						.param(PackageUnit.field.getFIELD_NAME_name(), packageUnit10.getName())//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		PackageUnitCP.verifyUpdate(mr2, packageUnit10, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel packageUnit10Update = pu10.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("packageUnit10", packageUnit10Update);
	}

	@Test(dependsOnMethods = "createPackageUnitThenUpdateAndReopen")
	public void createPackageUnitThenDeleteAndReopen() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????11??????????????????????????????????????????????????????");

		// ??????????????????11
		PackageUnit pu11 = new PackageUnit();
		pu11.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(new PackageUnitField().getFIELD_NAME_name(), pu11.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		PackageUnitCP.verifyCreate(mr, pu11, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PackageUnit packageUnit11 = (PackageUnit) pu11.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ??????????????????11
		MvcResult mr2 = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnit11.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		PackageUnitCP.verifyDelete(packageUnit11, packageUnitBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createPackageUnitThenDeleteAndReopen")
	public void createPackageUnitAndUpdateThenDeleteAndReopen() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????12???????????????????????????12???????????????????????????12????????????????????????");

		// ??????????????????12
		PackageUnit pu12 = new PackageUnit();
		pu12.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(new PackageUnitField().getFIELD_NAME_name(), pu12.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		PackageUnitCP.verifyCreate(mr, pu12, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PackageUnit packageUnit12 = (PackageUnit) pu12.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ??????????????????12
		packageUnit12.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr2 = mvc.perform(//
				post("/packageUnit/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(PackageUnit.field.getFIELD_NAME_ID(), String.valueOf(packageUnit12.getID()))//
						.param(PackageUnit.field.getFIELD_NAME_name(), packageUnit12.getName())//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		PackageUnitCP.verifyUpdate(mr2, packageUnit12, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel packageUnit12Update = pu12.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ??????????????????12
		MvcResult mr3 = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnit12Update.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr3);
		PackageUnitCP.verifyDelete(packageUnit12Update, packageUnitBO, CommodityListTest.dbNameOfNewCompany1);
	}

	@Test(dependsOnMethods = "createPackageUnitAndUpdateThenDeleteAndReopen")
	public void cancelCreatePackageUnit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????????????????????????????");

	}

	@Test(dependsOnMethods = "cancelCreatePackageUnit")
	public void updatePackageUnit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????9???????????????????????????");

		PackageUnit packageUnit9 = (PackageUnit) commodityRelatedMap.get("packageUnit9");
		packageUnit9.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr = mvc.perform(//
				post("/packageUnit/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(PackageUnit.field.getFIELD_NAME_ID(), String.valueOf(packageUnit9.getID()))//
						.param(PackageUnit.field.getFIELD_NAME_name(), packageUnit9.getName())//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		PackageUnitCP.verifyUpdate(mr, packageUnit9, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel packageUnit9Update = packageUnit9.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("packageUnit9", packageUnit9Update);
	}

	@Test(dependsOnMethods = "updatePackageUnit")
	public void updatePackageUnitThenDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????9?????????????????????????????????9????????????????????????");

		// ????????????????????????9
		PackageUnit packageUnit9 = (PackageUnit) commodityRelatedMap.get("packageUnit9");
		packageUnit9.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr = mvc.perform(//
				post("/packageUnit/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(PackageUnit.field.getFIELD_NAME_ID(), String.valueOf(packageUnit9.getID()))//
						.param(PackageUnit.field.getFIELD_NAME_name(), packageUnit9.getName())//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		PackageUnitCP.verifyUpdate(mr, packageUnit9, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel packageUnit9Update = packageUnit9.parse1(JsonPath.read(jsonObject, "$.object").toString());
		//
		// ??????????????????9
		MvcResult mr2 = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnit9Update.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		PackageUnitCP.verifyDelete(packageUnit9Update, packageUnitBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("packageUnit9");
	}

	@Test(dependsOnMethods = "updatePackageUnitThenDelete")
	public void updatePackageUnitThenCreateAnotherWithSameInfo() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????8???????????????????????????14(??????????????????????????????????????????????????????????????????????????????)");

		// ????????????????????????8
		PackageUnit packageUnit8 = (PackageUnit) commodityRelatedMap.get("packageUnit8");
		String packageUnit8Name = packageUnit8.getName();// ??????????????????14??????
		packageUnit8.setName(Shared.generateStringByTime(PackageUnit.MAX_LENGTH_Name));
		MvcResult mr = mvc.perform(//
				post("/packageUnit/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(PackageUnit.field.getFIELD_NAME_ID(), String.valueOf(packageUnit8.getID()))//
						.param(PackageUnit.field.getFIELD_NAME_name(), packageUnit8.getName())//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		PackageUnitCP.verifyUpdate(mr, packageUnit8, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel packageUnit8Update = packageUnit8.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.replace("packageUnit8", packageUnit8Update);
		//
		// ??????????????????14(??????????????????????????????????????????????????????????????????????????????)
		PackageUnit pu14 = new PackageUnit();
		pu14.setName(packageUnit8Name);
		MvcResult mr2 = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(new PackageUnitField().getFIELD_NAME_name(), pu14.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		PackageUnitCP.verifyUpdate(mr2, pu14, CommodityListTest.dbNameOfNewCompany1);
		//
		jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel packageUnit14 = pu14.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("packageUnit14", packageUnit14);
	}

	@Test(dependsOnMethods = "updatePackageUnitThenCreateAnotherWithSameInfo")
	public void deletePackageUnitThenCreateAnotherWithSameInfo() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????14?????????????????????????????????15??????????????????????????????????????????????????????????????????");

		// ??????????????????14
		PackageUnit packageUnit14 = (PackageUnit) commodityRelatedMap.get("packageUnit14");
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnit14.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		PackageUnitCP.verifyDelete(packageUnit14, packageUnitBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("packageUnit14");
		//
		// ????????????????????????15??????????????????????????????????????????????????????????????????
		PackageUnit pu15 = new PackageUnit();
		pu15.setName(packageUnit14.getName());
		MvcResult mr2 = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(new PackageUnitField().getFIELD_NAME_name(), pu15.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr2);
		PackageUnitCP.verifyCreate(mr2, pu15, CommodityListTest.dbNameOfNewCompany1);
		//
		JSONObject jsonObject = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		BaseModel packageUnit15 = pu15.parse1(JsonPath.read(jsonObject, "$.object").toString());
		commodityRelatedMap.put("packageUnit15", packageUnit15);
	}

	@Test(dependsOnMethods = "deletePackageUnitThenCreateAnotherWithSameInfo")
	public void deletePackageUnit() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "????????????????????????15???????????????????????????");

		PackageUnit packageUnit15 = (PackageUnit) commodityRelatedMap.get("packageUnit15");
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnit15.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ?????????
		Shared.checkJSONErrorCode(mr);
		PackageUnitCP.verifyDelete(packageUnit15, packageUnitBO, CommodityListTest.dbNameOfNewCompany1);
		commodityRelatedMap.remove("packageUnit15");
	}

	@Test(dependsOnMethods = "deletePackageUnit")
	public void updateCommodityProperty() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "?????????????????????????????????????????????");

		MvcResult mr = mvc.perform(//
				post("/commodityProperty/updateEx.bx")//
						.param(CommodityProperty.field.getFIELD_NAME_name1(), "AAAA")//
						.param(CommodityProperty.field.getFIELD_NAME_name2(), "BBBB")//
						.param(CommodityProperty.field.getFIELD_NAME_name3(), "CCCC")//
						.param(CommodityProperty.field.getFIELD_NAME_name4(), "DDDD")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr);
	}

	@Test(dependsOnMethods = "updateCommodityProperty")
	public void openCommodityRelatedPage() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityRelated_", order, "???????????????????????????????????????");

//		sessionBoss = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		// ???????????????????????????????????????????????????????????????
		Company company = BaseCompanyTest.DataInput.getCompany();
		bossPhone = company.getBossPhone();
		dbName = company.getDbName();
//		sessionBoss = BaseCompanyTest.getUploadBusinessLicensePictureSession(company.getDbName(), mvc).getRequest().getSession();
		//
		MvcResult mr1 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session(sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// ????????????????????????????????????????????????????????????????????????????????????MvcResult??????????????????????????????????????????????????????
		mvcResult_Company = mr1;
		commodityRelatedMap.put("company", company);
	}

	@Test(dependsOnMethods = "openCommodityRelatedPage")
	public void cannotUsePhoneOfPreSaleLoginAndCUDCommodityRelated() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "????????????????????????CUD??????");

		// ???????????????SN?????????
		Company company = (Company) commodityRelatedMap.get("company");
		JSONObject jsonObject = JSONObject.fromObject(mvcResult_Company.getResponse().getContentAsString());
		String companySN_New = JsonPath.read(jsonObject, "$.object.SN");
		sessionBoss = Shared.getStaffLoginSession(mvc, company.getBossPhone(), company.getBossPassword(), companySN_New);
		// ?????????????????????
		ProviderDistrict pd = new ProviderDistrict();
		pd.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr = (MvcResult) mvc.perform( //
				post("/providerDistrict/createEx.bx") //
						.param(ProviderDistrict.field.getFIELD_NAME_name(), pd.getName()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
		//
		// ???????????????
		Provider p = DataInput.getProvider();
		MvcResult mr4 = mvc.perform(//
				BaseCommodityTest.DataInput.getBuilder("/provider/createEx.bx", MediaType.APPLICATION_JSON, p)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
		//
		// ????????????
		Brand b = BaseBrandTest.DataInput.getBrand();
//		BaseBrandTest.createOrUpdateBrandViaActionWithExpectedError(b, true, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager), CommodityListTest.dbNameOfNewCompany1, EnumErrorCode.EC_NoPermission);
		// ????????????
		CategoryParent cp = new CategoryParent();
		cp.setName(Shared.generateStringByTime(generateStringLength));
//		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(cp, true, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager), CommodityListTest.dbNameOfNewCompany1, EnumErrorCode.EC_NoPermission);
		//
		CategoryParent categoryParent18 = (CategoryParent) commodityRelatedMap.get("categoryParent18");
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		c.setParentID(categoryParent18.getID());
		c.setName(Shared.generateStringByTime(generateStringLength));
//		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithExpectedError(c, true, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager), CommodityListTest.dbNameOfNewCompany1, EnumErrorCode.EC_NoPermission);
		// ??????????????????
		PackageUnit pu = new PackageUnit();
		pu.setName(Shared.generateStringByTime(generateStringLength));
		MvcResult mr16 = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(new PackageUnitField().getFIELD_NAME_name(), pu.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr16, EnumErrorCode.EC_NoPermission);
		//
		// ??????????????????
		MvcResult mr19 = mvc.perform(//
				post("/commodityProperty/updateEx.bx")//
						.param(CommodityProperty.field.getFIELD_NAME_name1(), "?????????1")//
						.param(CommodityProperty.field.getFIELD_NAME_name2(), "?????????2")//
						.param(CommodityProperty.field.getFIELD_NAME_name3(), "?????????3")//
						.param(CommodityProperty.field.getFIELD_NAME_name4(), "?????????4")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr19, EnumErrorCode.EC_NoPermission);
		//
		// ...??????????????????????????????????????????????????????UD??????

		// ???company????????????
		CompanyCP.verifyCreate(mvcResult_Company, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, company);
		// ??????????????????????????????
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// ????????????????????????????????????????????????????????????????????????????????????????????????
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, companySN_New, company.getBossPhone(), company.getBossPassword());
		// ???????????????BOSS????????????SESSION
		try {
			sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);

		} catch (Exception e) {
			e.printStackTrace();
		}
		

		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????tear down???????????????
		ConfigCacheSize providerConfigCacheSize = new ConfigCacheSize();
		providerConfigCacheSize.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		providerConfigCacheSize.setID(EnumConfigCacheSizeCache.ECC_PromotionCacheSize.getIndex());
		providerConfigCacheSize.setName(EnumConfigCacheSizeCache.ECC_ProviderCacheSize.getName());
		CacheManager.getCache(CommodityListTest.dbNameOfNewCompany1, EnumCacheType.ECT_ConfigCacheSize).write1(providerConfigCacheSize, CommodityListTest.dbNameOfNewCompany1, STAFF_ID4);
	}

	@Test(dependsOnMethods = "cannotUsePhoneOfPreSaleLoginAndCUDCommodityRelated")
	public void useLocalComputerCURDCommodityRelatedThenUseOtherComputerLoginAndRetrieve() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "???????????????CRUD???????????????????????????nbr????????????????????????????????????????????????????????????");

	}

	@Test(dependsOnMethods = "useLocalComputerCURDCommodityRelatedThenUseOtherComputerLoginAndRetrieve")
	public void useTwoOrMoreComputerLoginAtTheSameTimeAndCURDCommodityRelated() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_CommodityHistory_", order, "???????????????????????????????????????????????????nbr??????CRUD???????????????????????????????????????????????????????????????????????????");

	}
}
