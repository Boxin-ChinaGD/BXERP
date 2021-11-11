package com.bx.erp.test.warehousing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseWarehouseTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

@WebAppConfiguration
public class WarehouseActionTest extends BaseActionTest {
	public final static double TOLERANCE = 0.000001d;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	// 接口注释了
//	@Test
//	public void testIndex() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		mvc.perform(get("/warehouse.bx").session((MockHttpSession) sessionBoss)).andExpect(status().isOk());
//	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();
		// case1:正常进行创建
		System.out.println("------------------------------- case1: 正确的参数进行创建---------------------------");
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		MvcResult mrl = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/createEx.bx", MediaType.APPLICATION_JSON, w) //
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mrl);
		checkWarehouse(mrl, w);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		// case2:创建已存在name的仓库名
		System.out.println("------------------------------- case2: 创建的对象字段name为已存在的---------------------------");
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		MvcResult mrl = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/createEx.bx", MediaType.APPLICATION_JSON, w) //
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mrl);

		checkWarehouse(mrl, w);

		Warehouse w2 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam = w2.getCreateParam(BaseBO.INVALID_CASE_ID, w2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse warehouseCreate = (Warehouse) warehouseMapper.create(createParam); // ...

		w2.setIgnoreIDInComparision(true);
		if (w2.compareTo(warehouseCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(warehouseCreate != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		warehouseCreate.setName(w.getName());
		MvcResult mrl2 = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/createEx.bx", MediaType.APPLICATION_JSON, warehouseCreate) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mrl2, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		// case3：创建的参数StaffID为不存在的
		System.out.println("------------------------------- case3: 进行创建的参数StaffID不存在 ---------------------------");
		Warehouse w4 = BaseWarehouseTest.DataInput.getWarehouse();
		w4.setStaffID(Shared.BigStaffID);
		MvcResult mrl4 = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/createEx.bx", MediaType.APPLICATION_JSON, w4) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mrl4, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("------------------------------- case4:电话，地址为空的仓库 --------------------------");
		Warehouse w5 = BaseWarehouseTest.DataInput.getWarehouse();
		w5.setPhone(null);
		w5.setAddress(null);
		w5.setStaffID(3);
		MvcResult mrl5 = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/createEx.bx", MediaType.APPLICATION_JSON, w5) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mrl5);

		checkWarehouse(mrl5, w5);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("------------------------------- case5: 用不存在的staff创建---------------------------");
		Warehouse w1 = BaseWarehouseTest.DataInput.getWarehouse();
		w1.setStaffID(Shared.BigStaffID);
		MvcResult mrl1 = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/createEx.bx", MediaType.APPLICATION_JSON, w1) //
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mrl1, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("------------------------------- case6:没有权限进行操作---------------------------");
//		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
//		MvcResult mrl12 = mvc.perform(//
//				BaseWarehouseTest.DataInput.getBuilder("/warehouse/createEx.bx", MediaType.APPLICATION_JSON, w) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk()).andDo(print())//
//				.andReturn();//
//
//		Shared.checkJSONErrorCode(mrl12, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("------------------------------- case7: 用不存在的staff创建,字段检验不通过(StaffID<=0)---------------------------");
		Warehouse w8 = BaseWarehouseTest.DataInput.getWarehouse();
		w8.setStaffID(BaseAction.INVALID_ID);
		MvcResult mrl8 = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/createEx.bx", MediaType.APPLICATION_JSON, w8) //
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mrl8, EnumErrorCode.EC_WrongFormatForInputField);
	}

	public void checkWarehouse(MvcResult mrl, Warehouse warehouse) throws Exception {
		String json = mrl.getResponse().getContentAsString();
		com.alibaba.fastjson.JSONObject o = com.alibaba.fastjson.JSONObject.parseObject(json);
		int ID = JsonPath.read(o, "$.warehouse.ID");

		warehouse.setID(ID);
		Map<String, Object> retrieve1Param = warehouse.getRetrieve1Param(BaseBO.INVALID_CASE_ID, warehouse);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse retrieve1Warehouse = (Warehouse) warehouseMapper.retrieve1(retrieve1Param);

		if (warehouse.compareTo(retrieve1Warehouse) != 0) {
			Assert.assertTrue(false, "创建的对象与DB读出的不相等");
		}
	}

	@Test
	public void testRetrieve1Ex1() throws Exception {
		Shared.printTestMethodStartInfo();

		// Warehouse w = DataInput.getWarehouse();
		MvcResult mrl = mvc.perform(//
				get("/warehouse/retrieve1Ex.bx?" + Warehouse.field.getFIELD_NAME_ID() + "=1") //
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl);
	}
	
	@Test
	public void testRetrieve1Ex2() throws Exception {
		Shared.printTestMethodStartInfo();
		
		System.out.println("------------------------------- case2:没有权限进行操作---------------------------");
//		MvcResult mr2 = mvc.perform(//
//				get("/warehouse/retrieve1Ex.bx?" + Warehouse.field.getFIELD_NAME_ID() + "=1") //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}

	// 接口注释了
//	@Test
//	public void testRetrieveN() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		// Warehouse w = new Warehouse();
//		mvc.perform(//
//				get("/warehouse/retrieveN.bx")//
//						.session((MockHttpSession) sessionBoss)//
//		)//
//				.andExpect(status().isOk());
//	}

	@Test
	public void testRetrieveNEx1() throws Exception {
		Shared.printTestMethodStartInfo();
		// Warehouse w = new Warehouse();
		MvcResult mrl = mvc.perform(//
				post("/warehouse/retrieveNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mrl);
	}
	
	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		
		MvcResult mrl2 = mvc.perform(//
				post("/warehouse/retrieveNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(Warehouse.field.getFIELD_NAME_name(), "仓库")//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mrl2);
	}
	
	@Test
	public void testRetrieveNEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		
		System.out.println("------------------------------- case3:没有权限进行操作---------------------------");
//		MvcResult mr3 = mvc.perform(//
//				post("/warehouse/retrieveNEx.bx") //
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();//
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 正确的参数进行修改");
		Warehouse warehouse = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam = warehouse.getCreateParam(BaseBO.INVALID_CASE_ID, warehouse);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse createWareshouse = (Warehouse) warehouseMapper.create(createParam);

		Assert.assertTrue(createWareshouse != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		warehouse.setIgnoreIDInComparision(true);
		if (warehouse.compareTo(createWareshouse) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		w.setID(createWareshouse.getID());
		MvcResult mrl = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/updateEx.bx", MediaType.APPLICATION_JSON, w) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl);

		Map<String, Object> retrieve1Param = w.getRetrieve1Param(BaseBO.INVALID_CASE_ID, w);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse retrieve1Warehouse = (Warehouse) warehouseMapper.retrieve1(retrieve1Param);
		Assert.assertTrue(retrieve1Warehouse != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		if (retrieve1Warehouse.compareTo(w) != 0) {
			Assert.assertTrue(false, "创建的对象与DB读出的不相等");
		}
	}
	
	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("case2: 修改的参数name为已存在的");
		Warehouse warehouse = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam = warehouse.getCreateParam(BaseBO.INVALID_CASE_ID, warehouse);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse createWareshouse = (Warehouse) warehouseMapper.create(createParam);

		Assert.assertTrue(createWareshouse != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		warehouse.setIgnoreIDInComparision(true);
		if (warehouse.compareTo(createWareshouse) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		w.setID(createWareshouse.getID());
		MvcResult mrl = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/updateEx.bx", MediaType.APPLICATION_JSON, w) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl);

		Map<String, Object> retrieve1Param = w.getRetrieve1Param(BaseBO.INVALID_CASE_ID, w);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse retrieve1Warehouse = (Warehouse) warehouseMapper.retrieve1(retrieve1Param);
		Assert.assertTrue(retrieve1Warehouse != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		if (retrieve1Warehouse.compareTo(w) != 0) {
			Assert.assertTrue(false, "创建的对象与DB读出的不相等");
		}

		
		
		Warehouse warehouse2 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam2 = warehouse2.getCreateParam(BaseBO.INVALID_CASE_ID, warehouse2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse createWareshouse2 = (Warehouse) warehouseMapper.create(createParam2);

		Assert.assertTrue(createWareshouse2 != null && EnumErrorCode.values()[Integer.parseInt(createParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		warehouse2.setIgnoreIDInComparision(true);
		if (warehouse2.compareTo(createWareshouse2) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		createWareshouse2.setName(retrieve1Warehouse.getName());
		MvcResult mrl2 = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/updateEx.bx", MediaType.APPLICATION_JSON, createWareshouse2) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn(); //

		Shared.checkJSONErrorCode(mrl2, EnumErrorCode.EC_Duplicated);
	}
	
	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("case3: 修改的参数StaffID为不存在的");
		Warehouse warehouse4 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam4 = warehouse4.getCreateParam(BaseBO.INVALID_CASE_ID, warehouse4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse createWareshouse4 = (Warehouse) warehouseMapper.create(createParam4);

		Assert.assertTrue(createWareshouse4 != null && EnumErrorCode.values()[Integer.parseInt(createParam4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		warehouse4.setIgnoreIDInComparision(true);
		if (warehouse4.compareTo(createWareshouse4) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		createWareshouse4.setStaffID(Shared.BigStaffID);
		MvcResult mrl4 = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/updateEx.bx", MediaType.APPLICATION_JSON, createWareshouse4) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mrl4, EnumErrorCode.EC_NoSuchData);
	}
	
	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4: 修改仓库的联系人，电话，地址为空");
		Warehouse warehouse5 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam5 = warehouse5.getCreateParam(BaseBO.INVALID_CASE_ID, warehouse5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse createWareshouse5 = (Warehouse) warehouseMapper.create(createParam5);

		Assert.assertTrue(createWareshouse5 != null && EnumErrorCode.values()[Integer.parseInt(createParam5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		warehouse5.setIgnoreIDInComparision(true);
		if (warehouse5.compareTo(createWareshouse5) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		createWareshouse5.setAddress(null);
		createWareshouse5.setStaffID(3);
		createWareshouse5.setPhone(null);
		MvcResult mrl5 = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/updateEx.bx", MediaType.APPLICATION_JSON, createWareshouse5) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn(); //

		Shared.checkJSONErrorCode(mrl5);

		checkWarehouse(mrl5, createWareshouse5);
	}
	
	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("case5:没有权限进行操作");
		Warehouse warehouse = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam = warehouse.getCreateParam(BaseBO.INVALID_CASE_ID, warehouse);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse createWareshouse = (Warehouse) warehouseMapper.create(createParam);

		Assert.assertTrue(createWareshouse != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		warehouse.setIgnoreIDInComparision(true);
		if (warehouse.compareTo(createWareshouse) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		w.setID(createWareshouse.getID());
//		MvcResult mrl6 = mvc.perform(//
//				BaseWarehouseTest.DataInput.getBuilder("/warehouse/updateEx.bx", MediaType.APPLICATION_JSON, w) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mrl6, EnumErrorCode.EC_NoPermission);
	}
	
	@Test
	public void testUpdateEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("case6:修改的参数StaffID为不存在的(该值很大)");
		Warehouse warehouse = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam = warehouse.getCreateParam(BaseBO.INVALID_CASE_ID, warehouse);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse createWareshouse = (Warehouse) warehouseMapper.create(createParam);

		Assert.assertTrue(createWareshouse != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		warehouse.setIgnoreIDInComparision(true);
		if (warehouse.compareTo(createWareshouse) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		w.setID(createWareshouse.getID());
		w.setStaffID(BaseAction.INVALID_ID);
		MvcResult mrl7 = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/updateEx.bx", MediaType.APPLICATION_JSON, w) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl7, EnumErrorCode.EC_WrongFormatForInputField);
	}
	
	@Test
	public void testUpdateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7 默认的仓库不能修改");
		Warehouse w7 = BaseWarehouseTest.DataInput.getWarehouse();
		w7.setID(BaseBO.DEFAULT_DB_Row_ID);
		MvcResult mrl8 = mvc.perform(//
				BaseWarehouseTest.DataInput.getBuilder("/warehouse/updateEx.bx", MediaType.APPLICATION_JSON, w7) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl8, EnumErrorCode.EC_BusinessLogicNotDefined);
		com.alibaba.fastjson.JSONObject o18 = com.alibaba.fastjson.JSONObject.parseObject(mrl8.getResponse().getContentAsString());
		String msg18 = JsonPath.read(o18, "$.msg");
		Assert.assertTrue(Warehouse.ACTION_ERROR_UpdateDelete1.equals(msg18), "返回的错误消息不正确，期望的是:" + Warehouse.ACTION_ERROR_UpdateDelete1);
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常删除");
		Warehouse w2 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam = w2.getCreateParam(BaseBO.INVALID_CASE_ID, w2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse warehouseCreate = (Warehouse) warehouseMapper.create(createParam); // ...

		w2.setIgnoreIDInComparision(true);
		if (w2.compareTo(warehouseCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(warehouseCreate != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		MvcResult mrl = mvc.perform(get("/warehouse/deleteEx.bx?" + Warehouse.field.getFIELD_NAME_ID() + "=" + warehouseCreate.getID()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("case2:重复删除");
		Warehouse w2 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam = w2.getCreateParam(BaseBO.INVALID_CASE_ID, w2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse warehouseCreate = (Warehouse) warehouseMapper.create(createParam); // ...

		w2.setIgnoreIDInComparision(true);
		if (w2.compareTo(warehouseCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(warehouseCreate != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		MvcResult mrl = mvc.perform(get("/warehouse/deleteEx.bx?" + Warehouse.field.getFIELD_NAME_ID() + "=" + warehouseCreate.getID()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl);
		
		MvcResult mrl2 = mvc.perform(get("/warehouse/deleteEx.bx?" + Warehouse.field.getFIELD_NAME_ID() + "=" + warehouseCreate.getID()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("case3:没有权限进行操作");
		Warehouse w2 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam = w2.getCreateParam(BaseBO.INVALID_CASE_ID, w2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse warehouseCreate = (Warehouse) warehouseMapper.create(createParam); // ...
		w2.setIgnoreIDInComparision(true);
		if (w2.compareTo(warehouseCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(warehouseCreate != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
//		MvcResult mr3 = mvc.perform(get("/warehouse/deleteEx.bx?" + Warehouse.field.getFIELD_NAME_ID() + "=" + warehouseCreate.getID()) //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)))//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}
	
	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("case4:有依赖，无法删除");
		Warehouse w3 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> createParam3 = w3.getCreateParam(BaseBO.INVALID_CASE_ID, w3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse warehouseCreate3 = (Warehouse) warehouseMapper.create(createParam3); // ...

		w3.setIgnoreIDInComparision(true);
		if (w3.compareTo(warehouseCreate3) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(warehouseCreate3 != null && EnumErrorCode.values()[Integer.parseInt(createParam3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		// 添加入库单依赖
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		warehousing.setWarehouseID(warehouseCreate3.getID());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params1 = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		BaseModel wsCreate = warehousingMapper.create(params1);

		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(wsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(wsCreate != null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		MvcResult mrl3 = mvc.perform(get("/warehouse/deleteEx.bx?" + Warehouse.field.getFIELD_NAME_ID() + "=" + warehouseCreate3.getID()) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("CASE5 默认的仓库不能删除,返回错误码7");
		MvcResult mrl5 = mvc.perform(get("/warehouse/deleteEx.bx?" + Warehouse.field.getFIELD_NAME_ID() + "=1") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl5, EnumErrorCode.EC_BusinessLogicNotDefined);
		com.alibaba.fastjson.JSONObject o5 = com.alibaba.fastjson.JSONObject.parseObject(mrl5.getResponse().getContentAsString());
		String msg5 = JsonPath.read(o5, "$.msg");
		Assert.assertTrue(Warehouse.ACTION_ERROR_UpdateDelete1.equals(msg5), "返回的错误消息不正确，期望的是:" + Warehouse.ACTION_ERROR_UpdateDelete1);
	}
	
	@Test
	public void testRetrieveInventory1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("-------------------------case1:正常查询----------------------------------------");
		MvcResult mr1 = mvc.perform(//
				get("/warehouse/retrieveInventoryEx.bx?shopID=" + 2)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr1);
	}

	@Test
	public void testRetrieveInventory2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("-------------------------case2:查询创建单品后库存总额最高的商品及其库存总额 ----------------------------------------");
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		commTemplate.setOperatorStaffID(STAFF_ID4);
		// 创建期初商品latestPricePurchase会被设为purchasingPriceStart
		commTemplate.setNO(10000);
		commTemplate.setLatestPricePurchase(9999);
		commTemplate.setnOStart(commTemplate.getNO());
		commTemplate.setPurchasingPriceStart(commTemplate.getLatestPricePurchase());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateSingle);
		MvcResult mr2 = mvc.perform(//
				get("/warehouse/retrieveInventoryEx.bx?shopID=" + 2)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr2);
		String json = mr2.getResponse().getContentAsString();
		com.alibaba.fastjson.JSONObject o = com.alibaba.fastjson.JSONObject.parseObject(json);
		String commodityName = JsonPath.read(o, "$." + BaseAction.KEY_Object + "." + Warehouse.field.getFIELD_NAME_commodityName());
		if (!commodityName.equals(commCreate.getName())) {
			Assert.assertTrue(false, "验证查询创建单品后库存总额最高的商品及其库存总额失败！" + commodityName + " 和" + commCreate.getName() + " 不相等");
		}
		// 修改商品商品最近采购价，以免对其他测试造成影响
		commCreate.setLatestPricePurchase(0.500000d);
		commCreate.setOperatorStaffID(commTemplate.getOperatorStaffID());
		BaseCommodityTest.updateCommodityPrice(commCreate);
	}

	/** 测试思路： 1、 call action计算库存总额1 2、给系统增加商品A 3、给系统增加商品B 4、 call action计算库存总额2
	 * 5、结果验证：库存总额1+（最大的库存总额，即A的总额）+B的总额=库存总额2 */
	@Test
	public void testRetrieveInventory3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("-------------------------case3:验证库存总额----------------------------------------");
		// 1、先计算库存总额1
		MvcResult mr1 = mvc.perform(//
				get("/warehouse/retrieveInventoryEx.bx?shopID=" + 2)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		//
		Shared.checkJSONErrorCode(mr1);
		String json1 = mr1.getResponse().getContentAsString();
		com.alibaba.fastjson.JSONObject o1 = com.alibaba.fastjson.JSONObject.parseObject(json1);
		Double fTotalInventory1 = o1.getJSONObject(BaseAction.KEY_Object).getDouble(Warehouse.field.getFIELD_NAME_fTotalInventory());
		// 2、创建一个库存总额最大的商品A。它具有最大的库存总额
		Commodity commTemplateA = BaseCommodityTest.DataInput.getCommodity();
		commTemplateA.setOperatorStaffID(STAFF_ID3);
		commTemplateA.setNO(10000);
		commTemplateA.setnOStart(commTemplateA.getNO());
		commTemplateA.setLatestPricePurchase(9999);
		commTemplateA.setPurchasingPriceStart(commTemplateA.getLatestPricePurchase());
		Commodity commodityA = BaseCommodityTest.createCommodityViaMapper(commTemplateA, BaseBO.CASE_Commodity_CreateSingle);
		//
		MvcResult mr2 = mvc.perform(//
				get("/warehouse/retrieveInventoryEx.bx?shopID=" + 2)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		String json2 = mr2.getResponse().getContentAsString();
		com.alibaba.fastjson.JSONObject o2 = com.alibaba.fastjson.JSONObject.parseObject(json2);
		Double fMaxTotalInventory = o2.getJSONObject(BaseAction.KEY_Object).getDouble(Warehouse.field.getFIELD_NAME_fMaxTotalInventory());
		// 3、创建另外一个商品B。它的LatestPricePurchase和NO都是较小的数
		Commodity commTemplateB = BaseCommodityTest.DataInput.getCommodity();
		commTemplateB.setLatestPricePurchase(2.000000d);
		commTemplateB.setOperatorStaffID(STAFF_ID3);
		commTemplateB.setNO(1);
		commTemplateB.setnOStart(commTemplateB.getNO());
		commTemplateB.setPurchasingPriceStart(commTemplateB.getLatestPricePurchase());
		Commodity commodityB = BaseCommodityTest.createCommodityViaMapper(commTemplateB, BaseBO.CASE_Commodity_CreateSingle);

		// 4、call action计算库存总额2
		MvcResult mr4 = mvc.perform(//
				get("/warehouse/retrieveInventoryEx.bx?shopID=" + 2)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		//
		Shared.checkJSONErrorCode(mr4);
		String json4 = mr4.getResponse().getContentAsString();
		com.alibaba.fastjson.JSONObject aliJsonObject = com.alibaba.fastjson.JSONObject.parseObject(json4);
		Double fTotalInventory2 = aliJsonObject.getJSONObject(BaseAction.KEY_Object).getDouble(Warehouse.field.getFIELD_NAME_fTotalInventory());
		// 5、结果验证
		if (Math.abs(GeneralUtil.sub(GeneralUtil.sum(fTotalInventory1, GeneralUtil.sum(fMaxTotalInventory, GeneralUtil.mul(commodityB.getLatestPricePurchase(), commodityB.getNO()))), fTotalInventory2)) >= TOLERANCE) {
			Assert.assertTrue(false, "验证当前库存总额失败！" + GeneralUtil.sub(GeneralUtil.sum(fTotalInventory1, GeneralUtil.sum(fMaxTotalInventory, GeneralUtil.mul(commodityB.getLatestPricePurchase(), commodityB.getNO()))), fTotalInventory2));
		}
		// 修改商品商品最近采购价，以免对其他测试造成影响
		commodityA.setLatestPricePurchase(0.500000d);
		commodityA.setOperatorStaffID(commTemplateA.getOperatorStaffID());
		commodityB.setLatestPricePurchase(0.500000d);
		commodityB.setOperatorStaffID(commTemplateB.getOperatorStaffID());
		BaseCommodityTest.updateCommodityPrice(commodityA);
		BaseCommodityTest.updateCommodityPrice(commodityB);
	}

	@Test
	public void testRetrieveInventory4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("-------------------------case4:没有权限进行操作----------------------------------------");
//		MvcResult mr5 = mvc.perform(//
//				get("/warehouse/retrieveInventoryEx.bx")//
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();//
//
//		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveInventory5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("-------------------------case5:库存总额为负数 ----------------------------------------");
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		commTemplate.setOperatorStaffID(STAFF_ID4);
		// 库存总额（对应的SP会返回3个数，库存总额是其中1个）是所有未删除的单品的最近进货价*数量然后相加的结果，为了使它变为负数，这里创建一个最近进货价很大，数量为负的数，这样相加就会为负数
		commTemplate.setLatestPricePurchase(1000000);
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commTemplate);
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		//
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setCommodityID(commCreate.getID());
		retailTradeCommodity.setBarcodeID(barcodes.getID());
		retailTradeCommodity.setNO(9999);
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodity.add(retailTradeCommodity);
		rt.setListSlave1(listRetailTradeCommodity);
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
		//
		MvcResult mr = mvc.perform(//
				get("/warehouse/retrieveInventoryEx.bx?shopID=" + 2)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr);
		String json = mr.getResponse().getContentAsString();
		com.alibaba.fastjson.JSONObject o = com.alibaba.fastjson.JSONObject.parseObject(json);
		Double fTotalInventory = o.getJSONObject(BaseAction.KEY_Object).getDouble(Warehouse.field.getFIELD_NAME_fTotalInventory());
		Assert.assertTrue(fTotalInventory < 0, "数据不符合测试目的" + fTotalInventory + "不小于0");
		// 修改商品商品最近采购价，以免对其他测试造成影响
		commCreate.setLatestPricePurchase(0.500000d);
		commCreate.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.updateCommodityPrice(commCreate);
	}
}
