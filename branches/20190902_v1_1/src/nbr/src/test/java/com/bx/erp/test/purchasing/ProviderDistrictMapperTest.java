package com.bx.erp.test.purchasing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.purchasing.ProviderDistrictMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.ProviderDistrict;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class ProviderDistrictMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static ProviderDistrict pd = new ProviderDistrict();

		protected static final ProviderDistrict getProviderDistrict() throws CloneNotSupportedException, InterruptedException {
			pd = new ProviderDistrict();
			pd.setName("昆明1" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			return (ProviderDistrict) pd.clone();
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

		// 首次添加该用户
		Shared.caseLog("CASE1:正常添加");
		ProviderDistrict pd = createProviderDistrict(providerDistrictMapper);
		//
		String error = pd.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:重复添加");
		// 创建一个供应商区域
		ProviderDistrict pd = createProviderDistrict(providerDistrictMapper);
		//
		ProviderDistrict providerDistrict = new ProviderDistrict();
		providerDistrict.setName(pd.getName());
		Map<String, Object> createParam = providerDistrict.getCreateParam(BaseBO.INVALID_CASE_ID, providerDistrict);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerDistrictMapper.create(createParam);// ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_Duplicated);

		// 删除相应的供应商区域
		deleteProviderDistrict(providerDistrictMapper, pd);
	}

	@Test
	public void updateTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:修改区域名称不重复");

		ProviderDistrict pd = createProviderDistrict(providerDistrictMapper);
		//
		pd.setName("昆明" + String.valueOf(System.currentTimeMillis()).substring(6));
		Map<String, Object> paramsUpdate = pd.getUpdateParam(BaseBO.INVALID_CASE_ID, pd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderDistrict pdUpdate = (ProviderDistrict) providerDistrictMapper.update(paramsUpdate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		pd.setIgnoreIDInComparision(true);
		if (pd.compareTo(pdUpdate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		String error = pdUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");

		// 删除供应商区域
		deleteProviderDistrict(providerDistrictMapper, pd);
	}

	@Test
	public void updateTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:修改名称与自己重复");

		ProviderDistrict pd = createProviderDistrict(providerDistrictMapper);
		//
		Map<String, Object> paramsUpdate2 = pd.getUpdateParam(BaseBO.INVALID_CASE_ID, pd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderDistrict pdUpdate = (ProviderDistrict) providerDistrictMapper.update(paramsUpdate2);
		pd.setIgnoreIDInComparision(true);
		if (pd.compareTo(pdUpdate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsUpdate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		//
		String error = pdUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");

		// 删除供应商区域
		deleteProviderDistrict(providerDistrictMapper, pd);
	}

	@Test
	public void updateTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:修改区域名称与别人重复");

		// 创建一个供应商区域
		ProviderDistrict pd = createProviderDistrict(providerDistrictMapper);
		//
		pd.setName("广东");
		Map<String, Object> paramsUpdate3 = pd.getUpdateParam(BaseBO.INVALID_CASE_ID, pd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderDistrict pdUpdate = (ProviderDistrict) providerDistrictMapper.update(paramsUpdate3);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsUpdate3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_Duplicated);
		assertNull(pdUpdate, "对象与预期的不相等");

		// 删除供应商区域
		deleteProviderDistrict(providerDistrictMapper, pd);
	}

	@Test
	public void updateTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:修改ID不存在");

		ProviderDistrict pd4 = new ProviderDistrict();
		pd4.setName("广东1");
		pd4.setID(-99);
		Map<String, Object> paramsUpdate4 = pd4.getUpdateParam(BaseBO.INVALID_CASE_ID, pd4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderDistrict pdUpdate4 = (ProviderDistrict) providerDistrictMapper.update(paramsUpdate4);
		Assert.assertTrue(pdUpdate4 == null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Hack, "修改对象失败");
	}

	private void deleteProviderDistrict(ProviderDistrictMapper mapper, ProviderDistrict pd) {
		Map<String, Object> paramsDelete = pd.getDeleteParam(BaseBO.INVALID_CASE_ID, pd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		mapper.delete(paramsDelete);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieve1Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:查询");

		// 创建一个供应商区域
		ProviderDistrict providerDistrict = createProviderDistrict(providerDistrictMapper);
		//
		Map<String, Object> paramsUpdate = providerDistrict.getRetrieve1Param(BaseBO.INVALID_CASE_ID, providerDistrict);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderDistrict pdReterivev1 = (ProviderDistrict) providerDistrictMapper.retrieve1(paramsUpdate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		providerDistrict.setIgnoreIDInComparision(true);
		if (providerDistrict.compareTo(pdReterivev1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
	}

	@Test
	public void retrieve1Test2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:查询一个不存在的id");
		//
		ProviderDistrict pdCreate = new ProviderDistrict();
		pdCreate.setID(-111);
		Map<String, Object> paramsUpdate2 = pdCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, pdCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerDistrictMapper.retrieve1(paramsUpdate2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsUpdate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常查询");

		ProviderDistrict providerDistrict = new ProviderDistrict();
		Map<String, Object> paramsReterivevN = providerDistrict.getRetrieveNParam(BaseBO.INVALID_CASE_ID, providerDistrict);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = (List<BaseModel>) providerDistrictMapper.retrieveN(paramsReterivevN);
		assertTrue(listBM.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsReterivevN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:查看数据返回是否默认降序");

		ProviderDistrict providerDistrict = new ProviderDistrict();
		Map<String, Object> paramsReterivevN = providerDistrict.getRetrieveNParam(BaseBO.INVALID_CASE_ID, providerDistrict);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = (List<BaseModel>) providerDistrictMapper.retrieveN(paramsReterivevN);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsReterivevN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		assertTrue(listBM.size() > 0);
		for (int i = 1; i < listBM.size(); i++) {
			assertTrue(listBM.get(i - 1).getID() > listBM.get(i).getID(), "数据返回错误（非降序）");
		}
	}

	// 删除一个供应商区域begin --
	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:正常删除");
		//
		// 创建一个供应商区域
		ProviderDistrict pdCreate = createProviderDistrict(providerDistrictMapper);
		deleteProviderDistrict(providerDistrictMapper, pdCreate);
	}

	@Test
	public void deleteTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:删除的区域还有供应商使用");
		//
		ProviderDistrict pd = new ProviderDistrict();
		pd.setID(1);
		Map<String, Object> paramsDelete2 = pd.getDeleteParam(BaseBO.INVALID_CASE_ID, pd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerDistrictMapper.delete(paramsDelete2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void deleteTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:删除的区域不存在");
		//
		ProviderDistrict p = new ProviderDistrict();
		p.setID(-999);
		deleteProviderDistrict(providerDistrictMapper, p);
	}
	// 删除一个供应商区域end ----

	private ProviderDistrict createProviderDistrict(ProviderDistrictMapper mapper) throws CloneNotSupportedException, InterruptedException {
		// 首次添加该用户
		ProviderDistrict providerDistrict = DataInput.getProviderDistrict();
		Map<String, Object> params = providerDistrict.getCreateParam(BaseBO.INVALID_CASE_ID, providerDistrict);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderDistrict pdCreate = (ProviderDistrict) mapper.create(params);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		//
		providerDistrict.setIgnoreIDInComparision(true);
		if (providerDistrict.compareTo(pdCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		return pdCreate;
	}
}
