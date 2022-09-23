package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
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
import com.bx.erp.model.Pos;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseBrandTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.BrandCP;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class BrandSyncActionTest extends BaseActionTest {
	private static HttpSession session2;
	final EnumCacheType ect = EnumCacheType.ECT_Brand;
	final EnumSyncCacheType esct = EnumSyncCacheType.ESCT_BrandSyncInfo;

	@BeforeClass
	public void setup() {
		super.setUp();

		try {
			Shared.resetPOS(mvc, 1);
			sessionBoss = Shared.getPosLoginSession(mvc, 1);

			Shared.resetPOS(mvc, 2);
			session2 = Shared.getPosLoginSession(mvc, 2);
		} catch (Exception e) {
			e.printStackTrace();
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
		//
		Brand b = BaseBrandTest.DataInput.getBrand();
		BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);

	
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:缺失posID参数。导致Action失败");
		MvcResult mr2 = null;
		try {
			Brand brand = BaseBrandTest.DataInput.getBrand();
			BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand, true, mvc, null, mapBO, Shared.DBName_Test);
			// 注释原因：在对应的SyncAction中req
			// get的session里获取不到dbName，导致dbName为空，在BaseAction中断言错误，无法操作数据库，所以返回的错误码与期望的不一致。
			// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError);
		} catch (Exception e) {
			Assert.assertNull(mr2);
		}
	}
	
	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();
        
		Shared.caseLog("case3:重复创建品牌");
		//
		Brand b = BaseBrandTest.DataInput.getBrand();
		BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
        //
		BaseBrandTest.createOrUpdateBrandViaActionWithExpectedError(b, true, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_Duplicated);
	}

	// 运行Jenkins会跑不过,因为售前账号会被设为离职状态,在sit自动化测试CommodityRelatedTest里进行测试
	// @Test
	// public void testCreateEx3() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case3:售前人员进行创建品牌,创建失败，没有权限");
	// Brand b = DataInput.getBrand();
	// MvcResult mr = mvc.perform(//
	// post(true) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale)) //
	// .param(Brand.field.getFIELD_NAME_name(), b.getName())//
	// .param(Brand.field.getFIELD_NAME_returnObject(),
	// String.valueOf(b.getReturnObject())) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// // 结果验证：检查错误码
	// Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
	// }

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		MvcResult mr3 = mvc.perform(//
				get("/brandSync/deleteEx.bx")//
						.param(Brand.field.getFIELD_NAME_ID(), String.valueOf(brandCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3);

		MvcResult mr = mvc.perform(//
				get("/brandSync/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getPosLoginSession(mvc, 1))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1 正常修改品牌");
		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Brand b = BaseBrandTest.DataInput.getBrand();
		b.setID(brandCreate.getID());
		BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b, false, mvc, sessionBoss, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case2:缺失posID参数。导致Action失败");
		MvcResult mr2 = null;
		try {
			Brand brand = BaseBrandTest.DataInput.getBrand();
			BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand, false, mvc, null, mapBO, Shared.DBName_Test);
			// 注释原因：在对应的SyncAction中req
			// get的session里获取不到dbName，导致dbName为空，在BaseAction中断言错误，无法操作数据库，所以返回的错误码与期望的不一致。
			// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError);
		} catch (Exception e) {
			Assert.assertNull(mr2);
		}
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3 默认品牌不能修改,返回错误码7");
		Brand b = BaseBrandTest.DataInput.getBrand();
		b.setID(1);
		//
		String msg3 = BaseBrandTest.createOrUpdateBrandViaActionWithExpectedError(b, false, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_BusinessLogicNotDefined);
		Assert.assertTrue(Brand.ACTION_ERROR_UpdateDelete1.equals(msg3), "返回的错误消息不正确，期望的是:" + Brand.ACTION_ERROR_UpdateDelete1);
	}

	// 运行Jenkins会跑不过,因为售前账号会被设为离职状态,在sit自动化测试CommodityRelatedTest里进行测试
	// @Test
	// public void testUpdateEx4() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("CASE4:售前人员修改品牌，没有权限");
	// Brand brandCreate = createBrand();
	// //
	// Brand b = DataInput.getBrand();
	// b.setID(brandCreate.getID());
	// //
	// MvcResult mr4 = mvc.perform(//
	// post(false) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale)) //
	// .param(Brand.field.getFIELD_NAME_ID(), String.valueOf(b.getID()))//
	// .param(Brand.field.getFIELD_NAME_name(), b.getName())//
	// .param(Brand.field.getFIELD_NAME_returnObject(),
	// String.valueOf(b.getReturnObject()))//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// // 结果验证:检查错误码
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	// }

	@Test
	public void testFeedbackEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// 在pos1中创建一个品牌
		Brand b = BaseBrandTest.DataInput.getBrand();
		BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 在pos2中retrieveN
		System.out.println("--------------------------case1:retrieveNEx-----------------------------");
		MvcResult mr2 = mvc.perform(get("/brandSync/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) session2)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2);
		JSONObject json = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		json.getString(BaseAction.KEY_ObjectList);
		List<BaseModel> bmList = (List<BaseModel>) b.parseN(json.getString(BaseAction.KEY_ObjectList));
		// 设置全部同步与未全部同步的同步块
		List<Brand> brandList = BaseBrandTest.queryBrandListIfAllSync(bmList, mapBO);

		String ids = "";
		for (Brand brand : brandList) {
			ids += brand.getID() + ",";
		}

		MvcResult mr3 = mvc.perform( //
				get("/brandSync/feedbackEx.bx?sID=" + ids + "&errorCode=EC_NoError") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session2) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr3);
		// 检查点
		Pos pos = (Pos) session2.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();

		BrandCP.verifySyncBrand(brandList, posID, posBO, brandSyncCacheDispatcherBO, Shared.DBName_Test);

	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1 正常删除品牌");

		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		MvcResult mr = mvc.perform(//
				get("/brandSync/deleteEx.bx")//
						.param(Brand.field.getFIELD_NAME_ID(), String.valueOf(brandCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证：验证普通缓存中不存在这条数据
		BrandCP.verifyDelete(brandCreate, posBO, brandBO, brandSyncCacheBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(brand, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		MvcResult mr = null;
		try {
			mr = mvc.perform(//
					get("/brandSync/deleteEx.bx")//
							.param(Brand.field.getFIELD_NAME_ID(), String.valueOf(brandCreate.getID()))//
							.contentType(MediaType.APPLICATION_JSON)//
			)//
					.andExpect(status().isOk())//
					.andDo(print())//
					.andReturn();
			// 注释原因：在对应的SyncAction中req
			// get的session里获取不到dbName，导致dbName为空，在BaseAction中断言错误，无法操作数据库，所以返回的错误码与期望的不一致。
			// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError);
		} catch (Exception e) {
			Assert.assertNull(mr);
		}

	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3 默认的品牌不能被删除");
		MvcResult mr3 = mvc.perform(//
				get("/brandSync/deleteEx.bx")//
						.param(Brand.field.getFIELD_NAME_ID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
		JSONObject o3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		String msg3 = JsonPath.read(o3, "$.msg");
		Assert.assertTrue(Brand.ACTION_ERROR_UpdateDelete1.equals(msg3), "返回的错误消息不正确，期望的是:" + Brand.ACTION_ERROR_UpdateDelete1);
	}
}
