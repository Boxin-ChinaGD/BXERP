package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.VipSource;
import com.bx.erp.model.VipSource.EnumVipSourceCode;
import com.bx.erp.util.DataSourceContextHolder;

public class CompanyMapperTest extends BaseMapperTest {
	/** 检查公司名字唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_COMPANYNAME = 1;

	/** 检查公司营业执照号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_COMPANY_BUSINESSLICENSESN = 2;

	/** 检查公司营业执照照片唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_COMPANY_BusinessLicensePicture = 3;

	/** 检查公司钥匙唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_COMPANY_KEY = 4;

	/** 检查公司DB名称唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_COMPANY_DBNAME = 5;

	/** 检查公司子商户号唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_COMPANY_SUBMCHID = 6;

	@BeforeClass
	public void setup() {
		super.setUp();

		try {
			BaseCompanyTest.keepOldNbrAndNbrBx();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
		// 恢复nbr数据
		BaseCompanyTest.updateCompanyViaMapper(BaseCompanyTest.nbrCloned, EnumErrorCode.EC_NoError);
	}

	// 由于在Mapper中创建公司，并不会真的创建一个数据库，只是插入一条数据进DB里面，这会导致下次运行的时候，自动加载数据源，会找不到新添加的这个数据库。
	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1: 正常创建");
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company companyInDB = BaseCompanyTest.createCompanyViaMapper(company, EnumErrorCode.EC_NoError);
		
		BaseCompanyTest.deleteViaMapper(companyInDB);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2: 重复创建 DBName 返回错误码为7");
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company companyInDB = BaseCompanyTest.createCompanyViaMapper(company, EnumErrorCode.EC_NoError);

		Company companyB = BaseCompanyTest.DataInput.getCompany();
		companyB.setDbName(companyInDB.getDbName());
		BaseCompanyTest.createCompanyViaMapper(companyB, EnumErrorCode.EC_Duplicated);
		
		BaseCompanyTest.deleteViaMapper(companyInDB);
	}

	@Test
	public void createTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8: 重复创建营业执照  返回错误码为7");
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company companyInDB = BaseCompanyTest.createCompanyViaMapper(company, EnumErrorCode.EC_NoError);

		Company companyB = BaseCompanyTest.DataInput.getCompany();
		companyB.setBusinessLicenseSN(companyInDB.getBusinessLicenseSN());
		BaseCompanyTest.createCompanyViaMapper(companyB, EnumErrorCode.EC_Duplicated);
		
		BaseCompanyTest.deleteViaMapper(companyInDB);
	}

	@Test
	public void createTest9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9: 重复创建营业执照照片  返回错误码为7");
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company companyInDB = BaseCompanyTest.createCompanyViaMapper(company, EnumErrorCode.EC_NoError);

		Company companyB = BaseCompanyTest.DataInput.getCompany();
		companyB.setBusinessLicensePicture(companyInDB.getBusinessLicensePicture());
		BaseCompanyTest.createCompanyViaMapper(companyB, EnumErrorCode.EC_Duplicated);
		
		BaseCompanyTest.deleteViaMapper(companyInDB);
	}

	@Test
	public void createTest10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10: 重复创建公司名称  返回错误码为7");
		Company company = BaseCompanyTest.DataInput.getCompany();
		Company companyInDB = BaseCompanyTest.createCompanyViaMapper(company, EnumErrorCode.EC_NoError);

		Company companyB = BaseCompanyTest.DataInput.getCompany();
		companyB.setName(companyInDB.getName());
		BaseCompanyTest.createCompanyViaMapper(companyB, EnumErrorCode.EC_Duplicated);
		
		BaseCompanyTest.deleteViaMapper(companyInDB);
	}

	@Test
	public void createTest11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case11: 重复创建公司钥匙key  返回错误码为7");

		Company company = BaseCompanyTest.DataInput.getCompany();
		Company companyInDB = BaseCompanyTest.createCompanyViaMapper(company, EnumErrorCode.EC_NoError);

		Company companyB = BaseCompanyTest.DataInput.getCompany();
		companyB.setKey(companyInDB.getKey());
		BaseCompanyTest.createCompanyViaMapper(companyB, EnumErrorCode.EC_Duplicated);
		
		BaseCompanyTest.deleteViaMapper(companyInDB);
	}

	@Test
	public void createTest12() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case12: 创建一个子商户号为空串的公司，再新建一个字商户号为空串的公司。结果为俩个公司创建成功");
		Company companyA = BaseCompanyTest.DataInput.getCompany();
		companyA.setSubmchid("");
		Company companyInDB_A = BaseCompanyTest.createCompanyViaMapper(companyA, EnumErrorCode.EC_NoError);

		Company companyB = BaseCompanyTest.DataInput.getCompany();
		companyB.setSubmchid("");
		Company companyInDB_B = BaseCompanyTest.createCompanyViaMapper(companyB, EnumErrorCode.EC_NoError);
		
		BaseCompanyTest.deleteViaMapper(companyInDB_A);
		BaseCompanyTest.deleteViaMapper(companyInDB_B);
	}

	@Test
	public void createTest13() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case13: 重复创建公司logo  返回错误码为1");
		Company companyInDb = BaseCompanyTest.createCompanyViaMapper(BaseCompanyTest.DataInput.getCompany(), EnumErrorCode.EC_NoError);
		//
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setLogo(companyInDb.getLogo());
		Map<String, Object> params = company.getCreateParam(BaseBO.INVALID_CASE_ID, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		companyMapper.create(params);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		
		BaseCompanyTest.deleteViaMapper(companyInDb);
	}

	@Test
	public void retrieve1Test() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 正常查询");
		//
		Company company = new Company();
		company.setID(BaseCompanyTest.nbrCloned.getID());
		Map<String, Object> retrieve1Param = company.getRetrieve1Param(BaseBO.INVALID_CASE_ID, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		Company retrieve1Company = (Company) companyMapper.retrieve1(retrieve1Param);
		Assert.assertTrue(EnumErrorCode.EC_NoError == EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], retrieve1Param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(retrieve1Company != null, "查询失败，没有查到公司");
		if (retrieve1Company.compareTo(BaseCompanyTest.nbrCloned) != 0) {
			Assert.assertTrue(false, Shared.CompareToErrorMsg);
		}
		//
		String err = retrieve1Company.checkCreate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertTrue(err.equals(""), err);
	}

	@Test
	public void retrieve1Test2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: 查询 不存在的ID");
		Company companyr2 = new Company();
		companyr2.setID(Shared.BIG_ID);
		Map<String, Object> retrieve1Param2 = companyr2.getRetrieve1Param(BaseBO.INVALID_CASE_ID, companyr2);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		Company retrieve1Company2 = (Company) companyMapper.retrieve1(retrieve1Param2);
		Assert.assertNull(retrieve1Company2);
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 正常查询");
		Company companyr = new Company();
		companyr.setStatus(0);
		companyr.setPageIndex(1);
		companyr.setPageSize(10);
		Map<String, Object> retrieveNParam = companyr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, companyr);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		List<BaseModel> retrieveN = companyMapper.retrieveN(retrieveNParam);
		Assert.assertTrue(EnumErrorCode.EC_NoError == EnumErrorCode.values()[Integer.parseInt(retrieveNParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], retrieveNParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(retrieveN.size() >= 1);
		for (BaseModel bm : retrieveN) {
			Company c = (Company) bm;
			String err = c.checkCreate(BaseBO.CASE_SpecialResultVerification);
			Assert.assertTrue(err.equals(""), err);
		}
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: 查询 不存在的状态");
		Company companyr2 = new Company();
		companyr2.setStatus(3);
		Map<String, Object> retrieveNParam2 = companyr2.getRetrieveNParam(BaseBO.INVALID_CASE_ID, companyr2);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		List<BaseModel> retrieveN = companyMapper.retrieveN(retrieveNParam2);
		Assert.assertTrue(EnumErrorCode.EC_NoError == EnumErrorCode.values()[Integer.parseInt(retrieveNParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], retrieveNParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(retrieveN == null || retrieveN.size() == 0);
	}

	/* 更新已经存在的DB。不能创建一个DB因为数据库不会真的创建这个DB。只有Action层能创建真正的DB */
	@Test
	public void updateTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 正常更新");
		//
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBossWechat("aaaaaacc");
		company.setBusinessLicenseSN("111231231231231");
		//
		BaseCompanyTest.updateCompanyViaMapper(company, EnumErrorCode.EC_NoError);
		// 恢复nbr数据
		BaseCompanyTest.updateCompanyViaMapper(BaseCompanyTest.nbrCloned, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: 所更新的id 不存在");

		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setID(Shared.BIG_ID);
		//
		String err = company.checkCreate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertTrue(err.equals(""), err);
		//
		Map<String, Object> updateParam = company.getUpdateParam(BaseBO.INVALID_CASE_ID, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		BaseModel update = companyMapper.update(updateParam);
		Assert.assertNull(update);
		Assert.assertTrue(EnumErrorCode.EC_NoError == EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], updateParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void updateTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3: 更新，营业执照已有公司使用");
		Company company = (Company) BaseCompanyTest.nbrBxCloned.clone();
		company.setBusinessLicenseSN(BaseCompanyTest.nbrCloned.getBusinessLicenseSN());
		//
		BaseCompanyTest.updateCompanyViaMapper(company, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void updateTest4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4: 更新，公司名称已有公司使用");
		Company company = (Company) BaseCompanyTest.nbrBxCloned.clone();
		company.setName(BaseCompanyTest.nbrCloned.getName());
		//
		BaseCompanyTest.updateCompanyViaMapper(company, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void updateTest5() throws Exception {
		Shared.caseLog("case5: 更新，公司钥匙key已有公司使用");
		Company company = (Company) BaseCompanyTest.nbrBxCloned.clone();
		company.setKey(BaseCompanyTest.nbrCloned.getKey());
		//
		BaseCompanyTest.updateCompanyViaMapper(company, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void updateTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7: 更新，公众号取消授权");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		//
		BaseCompanyTest.updateCompanyViaMapper(company, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8: logo为重复的");
		Company company = (Company) BaseCompanyTest.nbrBxCloned.clone();
		company.setLogo(BaseCompanyTest.nbrCloned.getLogo());
		//
		BaseCompanyTest.updateCompanyViaMapper(company, EnumErrorCode.EC_Duplicated);

		// 恢复nbr数据
		BaseCompanyTest.updateCompanyViaMapper(BaseCompanyTest.nbrCloned, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("修改俩家公司的营业执照和logo为空串");
		Company companyA = BaseCompanyTest.DataInput.getCompany();
		Company companyAInDB = BaseCompanyTest.createCompanyViaMapper(companyA, EnumErrorCode.EC_NoError);
		//
		Company companyB = BaseCompanyTest.DataInput.getCompany();
		Company companyBInDB = BaseCompanyTest.createCompanyViaMapper(companyB, EnumErrorCode.EC_NoError);

		companyAInDB.setBusinessLicensePicture("");
		companyAInDB.setLogo("");
		//
		companyBInDB.setBusinessLicensePicture("");
		companyBInDB.setLogo("");

		BaseCompanyTest.updateCompanyViaMapper(companyAInDB, EnumErrorCode.EC_NoError);
		BaseCompanyTest.updateCompanyViaMapper(companyBInDB, EnumErrorCode.EC_NoError);
		
		BaseCompanyTest.deleteViaMapper(companyAInDB);
		BaseCompanyTest.deleteViaMapper(companyBInDB);
	}

	// @Test
	// public void updateTest8() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case8: 更新成重复的子商户号， 返回错误码为1");
	// //
	// Company company14 = DataInput.getCompany();
	// company14.setID(2);
	// company14.setSubmchid("1234561234");
	// Map<String, Object> paramsUpdate14 =
	// company14.getUpdateParam(BaseBO.INVALID_CASE_ID, company14);
	// mapper.update(paramsUpdate14);
	// Assert.assertTrue(EnumErrorCode.EC_Duplicated ==
	// EnumErrorCode.values()[Integer.parseInt(paramsUpdate14.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
	// "修改对象失败！错误信息=" +
	// paramsUpdate14.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// }

	@Test
	public void updateSubmchidTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 更新公司的子商户号");
		//
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setSubmchid(String.valueOf(System.currentTimeMillis()).substring(0, 10));
		BaseCompanyTest.updateSubmchidCompanyViaMapper(company);
		// 恢复数据
		BaseCompanyTest.updateSubmchidCompanyViaMapper(BaseCompanyTest.nbrCloned);
	}

	@Test
	public void updateSubmchidTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2: 修改为已有的其他公司它有的子商户号,修改失败");
		Company company = (Company) BaseCompanyTest.nbrBxCloned.clone();
		company.setSubmchid(BaseCompanyTest.nbrCloned.getSubmchid());
		//
		String err = company.checkUpdate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> paramsUpdate = company.getUpdateParam(BaseBO.CASE_Company_UpdateSubmchid, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		companyMapper.updateSubmchid(paramsUpdate);
		//
		Assert.assertTrue(EnumErrorCode.EC_Duplicated == EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void updateSubmchidTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3: 修改不存在的公司它的子商户号");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setID(Shared.BIG_ID);
		company.setSubmchid(BaseCompanyTest.nbrSubmchid);
		Map<String, Object> paramsUpdate = company.getUpdateParam(BaseBO.CASE_Company_UpdateSubmchid, company);
		companyMapper.updateSubmchid(paramsUpdate);
		Assert.assertTrue(EnumErrorCode.EC_NoSuchData == EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常删除");

		Company companyCreate = BaseCompanyTest.createCompanyViaMapper(BaseCompanyTest.DataInput.getCompany(), EnumErrorCode.EC_NoError);
		BaseCompanyTest.deleteViaMapper(companyCreate);

		Shared.caseLog("case2:删除不存在的ID");
		Company company2 = new Company();
		Map<String, Object> deleteParam2 = company2.getDeleteParam(BaseBO.INVALID_CASE_ID, company2);
		Company deleteCompany2 = (Company) companyMapper.delete(deleteParam2);
		Assert.assertNull(deleteCompany2);
	}

	@Test
	public void checkUniqueFieldTest() throws Exception {
		Shared.caseLog("Company checkUniqueField Test ");

		Shared.caseLog("case1: 查询一个不存在的公司名称");
		Company company1 = new Company();
		company1.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANYNAME);
		company1.setUniqueField("茕茕孑立");
		Map<String, Object> params1 = company1.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company1);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params1);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		Shared.caseLog("case2: 查询一个已存在的公司名称");
		Company company2 = new Company();
		company2.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANYNAME);
		company2.setUniqueField(BaseCompanyTest.nbrCloned.getName());
		Map<String, Object> params2 = company2.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company2);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

		Shared.caseLog("case3: 查询一个不存在的营业执照号");
		Company company3 = new Company();
		company3.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_BUSINESSLICENSESN);
		company3.setUniqueField("ZX9876543211234");
		Map<String, Object> params3 = company3.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company3);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params3);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		Shared.caseLog("case4: 查询一个已存在的营业执照号");
		Company company4 = new Company();
		company4.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_BUSINESSLICENSESN);
		company4.setUniqueField(BaseCompanyTest.nbrCloned.getBusinessLicenseSN());
		Map<String, Object> params4 = company4.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company4);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params4);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

		Shared.caseLog("case5: 查询一个不存在的营业执照图片");
		Company company5 = new Company();
		company5.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_BusinessLicensePicture);
		company5.setUniqueField("url=ZX454534354");
		company5.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company5);

		Shared.caseLog("case6: 查询一个已存在的营业执照图片");
		Company company6 = new Company();
		company6.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_BusinessLicensePicture);
		company6.setUniqueField(BaseCompanyTest.nbrCloned.getBusinessLicensePicture());
		Map<String, Object> params6 = company6.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company6);
		companyMapper.checkUniqueField(params6);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

		// 看到SP_Company_RetrieveN_CheckUniqueField文件里对钥匙的唯一检查给注释了，故这里将它注释
		// Shared.caseLog("case7:
		// 查询一个不存在的公司的钥匙Key");
		// Company company7 = new Company();
		// company7.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_KEY);
		// company7.setUniqueField("12345678901234567890123456789015");
		// Map<String, Object> params7 =
		// company7.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company7);
		//
		// mapper.checkUniqueField(params7);
		// assertTrue(EnumErrorCode.values()[Integer.parseInt(params7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_NoError);
		//
		// Shared.caseLog("case8:
		// 查询一个已存在的公司的钥匙Key");
		// Company company8 = new Company();
		// company8.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_KEY);
		// company8.setUniqueField("12345678901234567890123456789013");
		// Map<String, Object> params8 =
		// company8.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company8);
		//
		// // DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// mapper.checkUniqueField(params8);
		// assertTrue(EnumErrorCode.values()[Integer.parseInt(params8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_Duplicated);
		//
		// // DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// mapper.checkUniqueField(params6);
		// assertTrue(EnumErrorCode.values()[Integer.parseInt(params6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_Duplicated);

		Shared.caseLog("case9: 查询一个不存在的公司的DB名称");
		Company company9 = new Company();
		company9.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_DBNAME);
		company9.setUniqueField("zxr");
		Map<String, Object> params9 = company9.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company9);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params9);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		Shared.caseLog("case10: 查询一个已存在的公司的DB名称");
		Company company10 = new Company();
		company10.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_DBNAME);
		company10.setUniqueField(BaseCompanyTest.nbrCloned.getDbName());
		Map<String, Object> params10 = company10.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company10);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params10);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

		Shared.caseLog("case11: 查询一个已存在的公司名称,传入的ID与已存在的公司名称对应的ID相同");
		Company company11 = new Company();
		company11.setID(BaseCompanyTest.nbrCloned.getID());
		company11.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANYNAME);
		company11.setUniqueField(BaseCompanyTest.nbrCloned.getName());
		Map<String, Object> params11 = company11.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company11);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params11);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		Shared.caseLog("case12: 查询一个已存在的营业执照号，但传入的ID与已存在的营业执照号对应的ID相同");
		Company company12 = new Company();
		company12.setID(BaseCompanyTest.nbrCloned.getID());
		company12.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_BUSINESSLICENSESN);
		company12.setUniqueField(BaseCompanyTest.nbrCloned.getBusinessLicenseSN());
		Map<String, Object> params12 = company12.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company12);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params12);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params12.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		// 看到SP_Company_RetrieveN_CheckUniqueField文件里对钥匙的唯一检查给注释了，故这里将它注释
		// Shared.caseLog("case13:
		// 查询一个已存在的营业执照图片，但传入的ID与已存在的营业执照图片对应的ID相同");
		// Company company13 = new Company();
		// company13.setID(1);
		// company13.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_BusinessLicensePicture);
		// company13.setUniqueField("url=25454534354");
		// Map<String, Object> params13 =
		// company13.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company13);
		//
		// // DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// mapper.checkUniqueField(params13);
		// assertTrue(EnumErrorCode.values()[Integer.parseInt(params13.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_NoError);

		// Shared.caseLog("case14:
		// 查询一个已存在的公司钥匙Key，但传入的ID与已存在的公司钥匙Key对应的ID相同");
		// Company company14 = new Company();
		// company14.setID(1);
		// company14.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_KEY);
		// company14.setUniqueField("AS28248");
		// Map<String, Object> params14 =
		// company14.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company14);
		//
		// // DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// mapper.checkUniqueField(params14);
		// assertTrue(EnumErrorCode.values()[Integer.parseInt(params14.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_NoError);

		Shared.caseLog("case15: 查询一个已存在的公司钥匙Key，但传入的ID与已存在的公司钥匙Key对应的ID相同");
		Company company15 = new Company();
		company15.setID(BaseCompanyTest.nbrCloned.getID());
		company15.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_DBNAME);
		company15.setUniqueField(BaseCompanyTest.nbrCloned.getKey());
		Map<String, Object> params15 = company15.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company15);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params15);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params15.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		Shared.caseLog("case16: 查询一个不存在的子商户号");
		Company company16 = new Company();
		company16.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_SUBMCHID);
		company16.setUniqueField(String.valueOf(System.currentTimeMillis()).substring(0, 10));
		Map<String, Object> params16 = company16.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company16);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params16);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params16.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		Shared.caseLog("case17: 查询一个已存在的子商户号");
		Company company17 = new Company();
		company17.setFieldToCheckUnique(CASE_CHECK_UNIQUE_COMPANY_SUBMCHID);
		company17.setUniqueField(BaseCompanyTest.nbrCloned.getSubmchid());
		Map<String, Object> params17 = company17.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, company17);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		companyMapper.checkUniqueField(params17);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params17.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, params17.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNByVipMobile() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1: 正常查询所有");
		Company companyr = new Company();
		companyr.setStatus(0);
		companyr.setPageIndex(BaseAction.PAGE_StartIndex);
		companyr.setPageSize(10);
		companyr.setQueryKeyword("13545678110");
		String error = companyr.checkRetrieveN(BaseBO.CASE_Company_retrieveNByVipMobile);
		Assert.assertTrue(error.equals(""), "checkRetrieveN字段验证不通过");
		Map<String, Object> retrieveNParam = companyr.getRetrieveNParam(BaseBO.CASE_Company_retrieveNByVipMobile, companyr);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		List<BaseModel> retrieveN = companyMapper.retrieveNByVipMobile(retrieveNParam);
		Assert.assertTrue(EnumErrorCode.EC_NoError == EnumErrorCode.values()[Integer.parseInt(retrieveNParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], retrieveNParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(retrieveN.size() >= 1);
		for (BaseModel bm : retrieveN) {
			Company c = (Company) bm;
			String err = c.checkCreate(BaseBO.CASE_SpecialResultVerification);
			Assert.assertTrue(err.equals(""), err);
		}
		// 以下测试用例不能在mappertest实现，只能在actiontest实现
		Shared.caseLog("case2：创建一个公司, 改公司有手机号为13545678110的VIP，期望是返回该公司");
		Shared.caseLog("Case3:创建一个公司, 改公司没有手机号为13545678110的VIP，期望是没有返回该公司");
		Shared.caseLog("Case4:创建3个公司, 公司都有手机号为13545678110的VIP，传@iPageIndex = 1，@iPageSize = 2，期望是能正确分页,  返回两条数据，iTotalRecord应等于4(包含nbr)");
		Shared.caseLog("Case5:创建俩个公司,俩个公司都有手机号为15200702314的会员。但是有家公司已经是停业状态。查找时,返回1家");
	}
	
	@Test
	public void matchVipTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1: 正常查询所有");
		Company company = new Company();
		// 使测试独立
		// create vip
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// create vipCardCode
		VipCardCode vipCardCodeGet = BaseVipCardCodeTest.DataInput.getVipCardCode(vipCreate.getID(), 1);
		BaseVipCardCodeTest.createViaMapper(vipCardCodeGet, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// create vipSource
		VipSource vipSourceGet = BaseVipSourceTest.DataInput.getVipSource(vipCreate.getID());
		vipSourceGet.setID3(Shared.getValidStaffPhone());
		VipSource vipSourceCreate = BaseVipSourceTest.createViaMapper(vipSourceGet);
		// 
		company.setMobile(vipCreate.getMobile());
		company.setSourceCode(EnumVipSourceCode.EVSC_WX.getIndex());
		company.setOpenID(vipSourceCreate.getID3());
		company.setUnionID(vipSourceCreate.getID2());
		company.setVipName("张学友");
		//
		String error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertTrue(error.equals(""), "checkRetrieveN字段验证不通过");
		Map<String, Object> retrieveNParam = company.getRetrieveNParamEx(BaseBO.CASE_Company_matchVip, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		List<List<BaseModel>> list = companyMapper.matchVip(retrieveNParam);
		System.out.println(list);
		Assert.assertTrue(EnumErrorCode.EC_NoError == EnumErrorCode.values()[Integer.parseInt(retrieveNParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], retrieveNParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 以下测试用例不能在mappertest实现，只能在actiontest实现
		Shared.caseLog("Case2:创建一个公司, 新建一个会员信息，根据该会员查询");
		Shared.caseLog("Case3:创建一个公司, 创建一个nbr拥有的会员");
		Shared.caseLog("Case5:该会员仅在一家公司注册并且是通过nbr注册的。返回错误码0并且更新了会员的OpenID,UnionID,Name,Sex");
		Shared.caseLog("Case6:该会员在俩家公司进行注册;公司A的会员是通过nbr注册的,公司B的会员是通过小程序注册的。返回错误码0并且更新了会员的OpenID,UnionID,Name,Sex ");
		Shared.caseLog("case7:该会员在俩家公司进行注册;两家公司都是通过nbr注册的。返回错误码0并且更新了会员的OpenID,UnionID,Name,Sex");
		Shared.caseLog("case8:该会员并不是首次登录,所以并不会传递UnionID,并不会更新会员来源表中的OpenID和UnionID,Name,Sex");
	}
	
	@Test
	public void matchVipTest4() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case4:所有公司都没有查询会员信息，返回错误码2");
		Company company = new Company();
		company.setMobile("12312312312");
		company.setSourceCode(EnumVipSourceCode.EVSC_WX.getIndex());
		company.setOpenID("123123123132123");
		//
		String error = company.checkRetrieveNEx(BaseBO.CASE_Company_matchVip);
		Assert.assertTrue(error.equals(""), "checkRetrieveN字段验证不通过");
		Map<String, Object> retrieveNParam = company.getRetrieveNParamEx(BaseBO.CASE_Company_matchVip, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		companyMapper.matchVip(retrieveNParam);
		Assert.assertTrue(EnumErrorCode.EC_NoSuchData == EnumErrorCode.values()[Integer.parseInt(retrieveNParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], retrieveNParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
}
