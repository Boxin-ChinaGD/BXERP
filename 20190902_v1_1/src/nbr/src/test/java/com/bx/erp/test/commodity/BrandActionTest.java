package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseBrandTest;
import com.bx.erp.test.Shared;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class BrandActionTest extends BaseActionTest {
	private static final int INVALID_ID = 999999999;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testIndex() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(//
				get("/brand.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk());
	}

	// 依赖于testCreate()
	@Test
	public void testRetrieveAllEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("------------------------ case1:有参数 ----------------------");
		String name = "默认品牌";
		MvcResult mr = mvc.perform(//
				post("/brand/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(Brand.field.getFIELD_NAME_name(), name)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		List<String> names = JsonPath.read(Shared.checkJSONErrorCode(mr), "$.brandList[*].name");
		for (String s : names) {
			assertTrue(s.contains(name));
		}

		System.out.println("------------------------ case2:无参数 ----------------------");
		String name2 = "";
		MvcResult mr2 = mvc.perform(//
				post("/brand/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(Brand.field.getFIELD_NAME_name(), name2)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);

//		System.out.println("------------------------ case3:没有权限进行操作 -------------------------------");
//		MvcResult mr3 = mvc.perform(//
//				post("/brand/retrieveNEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.param(Brand.field.getFIELD_NAME_name(), name2)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testToUpdateEx() throws Exception {
		Shared.printTestMethodStartInfo();
         
		Brand brand = BaseBrandTest.DataInput.getBrand();		
		Brand b = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		MvcResult mr = mvc.perform(//
				get("/brand/toUpdateEx.bx?" + Brand.field.getFIELD_NAME_ID() + "=" + b.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

//		System.out.println("---------------------------case2:没有权限进行操作--------------------------------------");
//		MvcResult mr1 = mvc.perform(//
//				get("/brand/toUpdateEx.bx?" + Brand.field.getFIELD_NAME_ID() + "=" + b.getID())//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieve1() throws Exception {
		Shared.printTestMethodStartInfo();
        
		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand b = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		mvc.perform(//
				get("/brand/retrieve1.bx?" + Brand.field.getFIELD_NAME_ID() + "=" + b.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//

				.andExpect(status().isOk());
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();
        
		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand b = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		MvcResult mr = mvc.perform(//
				get("/brand//retrieve1Ex.bx?" + Brand.field.getFIELD_NAME_ID() + "=" + b.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

//		System.out.println("---------------------------case2:没有权限-------------------------------");
//		MvcResult mr2 = mvc.perform(//
//				get("/brand//retrieve1Ex.bx?" + Brand.field.getFIELD_NAME_ID() + "=" + b.getID())//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);

		System.out.println("---------------------------case3:用不存在的ID进行查询-------------------------------");
		MvcResult mr3 = mvc.perform(//
				get("/brand//retrieve1Ex.bx?" + Brand.field.getFIELD_NAME_ID() + "=" + INVALID_ID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);

		JSONObject o = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		String json = JsonPath.read(o, "$.brand");
		assertTrue(json.equals(""));
	}


	// /** 除了批量删除之外没有地方用到，暂时注释掉
	// * 创建一个已有商品在使用的Brand
	// *
	// * @return
	// * @throws CloneNotSupportedException
	// * @throws InterruptedException
	// */
	// private Brand createBrand1() throws CloneNotSupportedException,
	// InterruptedException {
	// BrandMapper mapper = (BrandMapper) applicationContext.getBean("brandMapper");
	// Brand b = new Brand();
	//
	// System.out.println("\n------------------------ Case1: 添加不重复的品牌，错误码为0
	// ------------------------");
	// b.setName("士力架88" + String.valueOf(System.currentTimeMillis()).substring(6));
	// Thread.sleep(1);
	// Map<String, Object> paramsForCreate =
	// b.getCreateParam(BaseBO.INVALID_CASE_ID, b);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Brand brandCreate = (Brand) mapper.create(paramsForCreate); // ...
	// //
	// System.out.println("创建品牌对象所需要的对象：" + b);
	// System.out.println("创建出的品牌对象是：" + brandCreate);
	// //
	// b.setIgnoreIDInComparision(true);
	// if (b.compareTo(brandCreate) != 0) {
	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
	// }
	// Assert.assertNotNull(brandCreate);
	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
	// EnumErrorCode.EC_NoError);
	//
	// CommodityMapper commodityMapper = (CommodityMapper)
	// applicationContext.getBean("commodityMapper");
	//
	// Commodity commTemplate = CommodityDataInput.getCommodity();
	// commTemplate.setBrandID(brandCreate.getID());
	// Map<String, Object> params =
	// commTemplate.getCreateParamEx(BaseBO.INVALID_CASE_ID, commTemplate);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// List<List<BaseModel>> bmList = commodityMapper.createEx(params);
	//
	// Commodity bm = (Commodity) bmList.get(0).get(0);
	//
	// commTemplate.setIgnoreIDInComparision(true);
	// if (commTemplate.compareTo(bm) != 0) {
	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
	// }
	// System.out.println(bm == null ? "null" : bm);
	//
	// Assert.assertNotNull(params);
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError, "创建对象成功");
	// System.out.println("创建商品成功");
	//
	// return brandCreate;
	// }

}
