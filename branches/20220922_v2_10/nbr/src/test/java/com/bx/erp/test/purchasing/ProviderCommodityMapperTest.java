package com.bx.erp.test.purchasing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.purchasing.ProviderMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;

public class ProviderCommodityMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static ProviderCommodity providerCommodityInput = null;

		protected static final ProviderCommodity getProviderCommodity(int commodityID) throws CloneNotSupportedException, InterruptedException {
			if (providerCommodityInput == null) {
				providerCommodityInput = new ProviderCommodity();
				providerCommodityInput.setCommodityID(commodityID);
				providerCommodityInput.setProviderID(6);
			}
			return (ProviderCommodity) providerCommodityInput.clone();
		}

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
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		// case1: 添加不重复的供应商
		Shared.caseLog("case1: 添加不重复的供应商");
		//
		// 创建一个供应商
		Provider p = createProvider(providerMapper);
		//
		// 创建一个供应商商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		ProviderCommodity pc1 = DataInput.getProviderCommodity(commodity.getID());
		pc1.setProviderID(p.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		String error = createPC.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");

		// 删除供应商商品表
		Map<String, Object> params1 = createPC.getDeleteParam(BaseBO.INVALID_CASE_ID, createPC);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerCommodityMapper.delete(params1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		// 删除创建出来的供应商
		Map<String, Object> paramsForDelete1 = p.getDeleteParam(BaseBO.INVALID_CASE_ID, p);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.delete(paramsForDelete1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: 添加重复的供应商");
		ProviderCommodity pc2 = DataInput.getProviderCommodity(1);
		pc2.setCommodityID(1);
		pc2.setProviderID(2);
		Map<String, Object> params2 = pc2.getCreateParam(BaseBO.INVALID_CASE_ID, pc2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity bm2 = (ProviderCommodity) providerCommodityMapper.create(params2);
		Assert.assertTrue(bm2 != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3: 添加组合商品到供应商商品中 返回错误码7");
		Provider p1 = createProvider(providerMapper);
		//
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(BaseCommodityTest.DataInput.getCommodity(), BaseBO.CASE_Commodity_CreateSingle);
		ProviderCommodity pc3 = DataInput.getProviderCommodity(commodity.getID());
		pc3.setCommodityID(45);
		pc3.setProviderID(p1.getID());
		Map<String, Object> params3 = pc3.getCreateParam(BaseBO.INVALID_CASE_ID, pc3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity bm3 = (ProviderCommodity) providerCommodityMapper.create(params3);
		//
		Assert.assertTrue(bm3 == null && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象成功");

		// 删除创建出来的供应商
		Map<String, Object> paramsForDelete1 = p1.getDeleteParam(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.delete(paramsForDelete1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4: 添加已删除商品到供应商商品表中，错误码为7");

		// 创建一个供应商
		Provider p1 = createProvider(providerMapper);
		//
		// 已删除商品到供应商商品表中，错误码为7
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(BaseCommodityTest.DataInput.getCommodity(), BaseBO.CASE_Commodity_CreateSingle);
		ProviderCommodity pc5 = DataInput.getProviderCommodity(commodity.getID());
		pc5.setCommodityID(50);
		pc5.setProviderID(p1.getID());
		Map<String, Object> params5 = pc5.getCreateParam(BaseBO.INVALID_CASE_ID, pc5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity bm5 = (ProviderCommodity) providerCommodityMapper.create(params5);
		Assert.assertTrue(bm5 == null && EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象成功");

		// 删除创建出来的供应商
		Map<String, Object> paramsForDelete1 = p1.getDeleteParam(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.delete(paramsForDelete1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5: 添加不存在的商品到供应商商品表中，错误码为7");
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(BaseCommodityTest.DataInput.getCommodity(), BaseBO.CASE_Commodity_CreateSingle);
		ProviderCommodity pc2 = DataInput.getProviderCommodity(commodity.getID());
		pc2.setCommodityID(-99);
		pc2.setProviderID(2);
		Map<String, Object> params2 = pc2.getCreateParam(BaseBO.INVALID_CASE_ID, pc2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity bm2 = (ProviderCommodity) providerCommodityMapper.create(params2);
		Assert.assertTrue(bm2 == null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象成功");
	}

	@Test
	public void createTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6: 添加不存在的供应商到供应商商品表中，错误码为7");
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(BaseCommodityTest.DataInput.getCommodity(), BaseBO.CASE_Commodity_CreateSingle);
		ProviderCommodity pc2 = DataInput.getProviderCommodity(commodity.getID());
		pc2.setCommodityID(1);
		pc2.setProviderID(-99);
		Map<String, Object> params2 = pc2.getCreateParam(BaseBO.INVALID_CASE_ID, pc2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity bm2 = (ProviderCommodity) providerCommodityMapper.create(params2);
		Assert.assertTrue(bm2 == null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象成功");
	}

	private Provider createProvider(ProviderMapper mapper) throws CloneNotSupportedException, InterruptedException {
		Provider p = new Provider();
		// if (providerInput == null) {
		p = new Provider();
		p.setName("淘宝" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		p.setDistrictID(1);
		p.setAddress("广州市天河区二十八中学");
		p.setContactName("zda");
		p.setMobile(Shared.getValidStaffPhone());
		Map<String, Object> params = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerCreate = (Provider) mapper.create(params); // ...
		p.setIgnoreIDInComparision(true);
		if (p.compareTo(providerCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(providerCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		return providerCreate;
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:传入供应商ID时删除商品对应的供应商");
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(BaseCommodityTest.DataInput.getCommodity(), BaseBO.CASE_Commodity_CreateSingle);
		ProviderCommodity pc1 = DataInput.getProviderCommodity(commodity.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity bm = (ProviderCommodity) providerCommodityMapper.create(params);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(bm) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(bm != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		Map<String, Object> params1 = bm.getDeleteParam(BaseBO.INVALID_CASE_ID, bm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerCommodityMapper.delete(params1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:没有传入供应商ID时进行删除，删除掉当前商品的所有供应商");
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(BaseCommodityTest.DataInput.getCommodity(), BaseBO.CASE_Commodity_CreateSingle);
		ProviderCommodity pc = DataInput.getProviderCommodity(commodity.getID());
		pc.setProviderID(0);
		Map<String, Object> params2 = pc.getDeleteParam(BaseBO.INVALID_CASE_ID, pc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerCommodityMapper.delete(params2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:删除一个不存在的商品ID");
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(BaseCommodityTest.DataInput.getCommodity(), BaseBO.CASE_Commodity_CreateSingle);
		ProviderCommodity pc2 = DataInput.getProviderCommodity(commodity.getID());
		pc2.setCommodityID(99999);
		pc2.setProviderID(0);
		Map<String, Object> params3 = pc2.getDeleteParam(BaseBO.INVALID_CASE_ID, pc2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerCommodityMapper.delete(params3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常查询");
		// 创建一个供应商商品
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(BaseCommodityTest.DataInput.getCommodity(), BaseBO.CASE_Commodity_CreateSingle);
		ProviderCommodity data = DataInput.getProviderCommodity(commodity.getID());
		Map<String, Object> params = data.getCreateParam(BaseBO.INVALID_CASE_ID, data);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		data.setIgnoreIDInComparision(true);
		if (data.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		Map<String, Object> params1 = data.getRetrieveNParam(BaseBO.INVALID_CASE_ID, createPC);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> is = providerCommodityMapper.retrieveN(params1);
		Assert.assertTrue(is.size() > 0);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象成功");
		//
		// 删除供应商商品表
		Map<String, Object> params2 = createPC.getDeleteParam(BaseBO.INVALID_CASE_ID, createPC);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerCommodityMapper.delete(params2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:输入一个不存在的CommodityID");
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(BaseCommodityTest.DataInput.getCommodity(), BaseBO.CASE_Commodity_CreateSingle);
		ProviderCommodity data2 = DataInput.getProviderCommodity(commodity.getID());
		data2.setCommodityID(-1);
		Map<String, Object> params2 = data2.getRetrieveNParam(BaseBO.INVALID_CASE_ID, data2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> is2 = providerCommodityMapper.retrieveN(params2);

		assertTrue(is2.size() == 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}
}
