package com.bx.erp.action;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.Staff;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class CacheActionTest extends BaseActionTest {
	@BeforeClass
	public void setup() {
		super.setUp();
		try {
			// 运行testUnit.xml sessionBoss被其它类修改成bx的了
			sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Staff).deleteAll();

		super.tearDown();
	}

	@Test
	public void testCacheRNEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("获取公司下的所有缓存！！");
		MvcResult mr = mvc.perform(//
				get("/cache/retrieveNEx.bx")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.DBName_Test).contentType(MediaType.APPLICATION_JSON)//
						.session(sessionOP)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<JSONObject> list = JsonPath.read(o, "$.objectList[*]");
		Assert.assertTrue(!CollectionUtils.isEmpty(list));
	}

	@Test
	public void testCacheRNEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("获取公司下的某个类型的所有缓存！！");
		MvcResult mr2 = mvc.perform(//
				get("/cache/retrieveNEx.bx")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.DBName_Test) //
						.param(Company.field.getFIELD_NAME_cacheType(), EnumCacheType.ECT_Barcodes.toString())//
						.param(Company.field.getFIELD_NAME_whetherRetrieveAll(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionOP)//

		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<JSONObject> list2 = JsonPath.read(o2, "$.objectList[*]");
		Assert.assertTrue(!CollectionUtils.isEmpty(list2));
	}

	@Test
	public void testCacheRNEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("获取公司下的某个类型的所有缓存是失败");
		MvcResult mr3 = mvc.perform(//
				get("/cache/retrieveNEx.bx")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.BXDBName_Test) //
						.param(Company.field.getFIELD_NAME_cacheType(), EnumCacheType.ECT_Barcodes.toString())//
						.param(Company.field.getFIELD_NAME_whetherRetrieveAll(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionOP)//

		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		JSONObject o3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		Assert.assertTrue(o3.get(BaseAction.KEY_HTMLTable_Parameter_msg) != null);
	}

	@Test
	public void testCacheRNEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("Case4:非OP账号登录，获取公司下的所有缓存");
		MvcResult mr = mvc.perform(//
				get("/cache/retrieveNEx.bx")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.DBName_Test).contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}

	// @Test
	public void testUpdateCacheEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		List<BaseModel> staffList0 = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Staff).readN(false, false);
		System.out.println("staffList0:" + staffList0.size());
		// 创建了一个Staff
		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffCreate = BaseStaffTest.createStaffViaMapper(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError);
		// 将创建的Staff加入缓存
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Staff).write1(staffCreate, Shared.DBName_Test, 3);
		List<BaseModel> staffList1 = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Staff).readN(false, false);
		System.out.println("staffList1:" + staffList1.size());
		// 修改新创建的Staff
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setID(staffCreate.getID());
		staff2.setRoleID(1);
		Staff staffUpdate = BaseStaffTest.updateStaffViaMapper(BaseBO.INVALID_CASE_ID, staff2, staffCreate, EnumErrorCode.EC_NoError);
		// 更新创建的Staff之后，因为没有写入缓存，所以从缓存获取出来的Staf应该是与修改后的Staff是不相等的
		List<BaseModel> staffList2 = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Staff).readN(false, false);
		System.out.println("staffList2:" + staffList2.size());
		for (BaseModel bm : staffList2) {
			Staff staffCache2 = (Staff) bm;
			if (staffCache2.getID() == staffCreate.getID()) {
				Assert.assertTrue(staffUpdate.compareTo(staffCache2) == -1, "更新后的Staff没有写入缓存，应该与缓存读出来的不相等");
			}
		}

		Shared.caseLog("case1:获取公司下的所有缓存！！");

		MvcResult mr = mvc.perform(//
				get("/cache/updateCacheEx.bx")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.DBName_Test).param(Company.field.getFIELD_NAME_cacheType(), EnumCacheType.ECT_Staff.toString())//
						.param(Company.field.getFIELD_NAME_cacheID(), String.valueOf(staffCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionOP)//

		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

		List<BaseModel> staffList3 = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Staff).readN(false, false);
		System.out.println("staffList3:" + staffList3.size());
		for (BaseModel bm : staffList3) {
			Staff staffCache3 = (Staff) bm;
			if (staffCache3.getID() == staffCreate.getID()) {
				Assert.assertTrue(staffUpdate.compareTo(staffCache3) == 0, "更新后的Staff没有写入缓存，但是因为Staff是重新从DB读出来的，应该与缓存读出来的相等");
			}
		}

		BaseStaffTest.deleteStaffViaMapper(staffCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testUpdateCacheEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		List<BaseModel> commList0 = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).readN(false, false);
		System.out.println("staffList0:" + commList0.size());
		// 创建了一个comm
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle); // 将创建的Commodity加入缓存
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).write1(commCreated, Shared.DBName_Test, STAFF_ID3);
		List<BaseModel> commList1 = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).readN(false, false);
		System.out.println("commList1:" + commList1.size());

		// 修改新创建的comm
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		commodity2.setName(UUID.randomUUID().toString().substring(1, 7));
		commodity2.setID(commCreated.getID());
		commodity2.setOperatorStaffID(STAFF_ID3);
		//
		String error = commodity2.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate2 = commodity2.getUpdateParam(BaseBO.INVALID_CASE_ID, commodity2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase2 = (Commodity) commodityMapper.update(paramsForUpdate2);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForUpdate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNotNull(updateCommodityCase2);

		// 更新创建的Comm之后，因为没有写入缓存，所以从缓存获取出来的Comm应该是与修改后的Comm是不相等的
		List<BaseModel> commList2 = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).readN(false, false);
		System.out.println("staffList2:" + commList2.size());
		for (BaseModel bm : commList2) {
			Commodity commCache2 = (Commodity) bm;
			if (commCache2.getID() == commCreated.getID()) {
				Assert.assertTrue(updateCommodityCase2.compareTo(commCache2) == -1, "更新后的Commodity没有写入缓存，应该与缓存读出来的不相等");
			}
		}

		Shared.caseLog("case1:获取公司下的所有缓存！！");

		MvcResult mr = mvc.perform(//
				get("/cache/updateCacheEx.bx")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.DBName_Test).param(Company.field.getFIELD_NAME_cacheType(), EnumCacheType.ECT_Commodity.toString())//
						.param(Company.field.getFIELD_NAME_cacheID(), String.valueOf(commCreated.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionOP)//

		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

		List<BaseModel> commList3 = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).readN(false, false);
		System.out.println("commList3:" + commList3.size());
		for (BaseModel bm : commList3) {
			Commodity commCache3 = (Commodity) bm;
			if (commCache3.getID() == commCreated.getID()) {
				Assert.assertTrue(updateCommodityCase2.compareTo(commCache3) == 0, "更新后的Comm没有写入缓存，但是因为Comm是重新从DB读出来的，应该与缓存读出来的相等");
			}
		}
		BaseCommodityTest.deleteCommodityViaAction(updateCommodityCase2, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@Test
	public void testUpdateCacheEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:非OP账号，BX登录，修改商品缓存！！");
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				get("/cache/updateCacheEx.bx")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.DBName_Test).param(Company.field.getFIELD_NAME_cacheType(), EnumCacheType.ECT_Commodity.toString())//
						.param(Company.field.getFIELD_NAME_cacheID(), String.valueOf(1))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}

	@Test
	public void testUpdateCacheEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:非OP账号登录，修改商品缓存！！");
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				get("/cache/updateCacheEx.bx")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.DBName_Test).param(Company.field.getFIELD_NAME_cacheType(), EnumCacheType.ECT_Commodity.toString())//
						.param(Company.field.getFIELD_NAME_cacheID(), String.valueOf(1))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "测试预期结果为空");
	}
}