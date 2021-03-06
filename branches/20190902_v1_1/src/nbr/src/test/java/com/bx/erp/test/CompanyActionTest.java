package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.test.staff.BaseShopTest;
import com.bx.erp.util.FieldFormat;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class CompanyActionTest extends BaseActionTest {

	private HttpSession sessionForCheckUniqueField;

	// /** ??????????????????????????????????????????????????????????????????????????? */
	// private int companyToDelete;

	private static final String errorMsg = "??????????????????????????????????????????";

	/** ????????????????????????????????????????????? */
	public static final int CASE_CHECK_UNIQUE_SHOP_NAME = 1;

	@BeforeClass
	public void setup() {
		super.setUp();

		// companyToDelete = 0;
		try {
			BaseCompanyTest.keepOldNbrAndNbrBx();
			sessionForCheckUniqueField = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testCreate1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: ????????????");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		Company company = BaseCompanyTest.DataInput.getCompany();
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
	}

	@Test
	public void testCreate2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: ????????????(??????????????????)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setName("BX???????????????");
		// sessionOP =
		// BaseCompanyTest.getUploadBusinessLicensePictureSession(company.getDbName(),
		// mvc).getRequest().getSession();
		MvcResult mr1 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session(sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreate3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: ??????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossPhone("");
		// sessionOP =
		// BaseCompanyTest.getUploadBusinessLicensePictureSession(company.getDbName(),
		// mvc).getRequest().getSession();
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_bossPhone, errorMsg);
	}

	@Test
	public void testCreate4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4: ????????????????????????DB");
		//
		Company company = BaseCompanyTest.DataInput.getCompany();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);
		//
		Company company2 = BaseCompanyTest.DataInput.getCompany();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr2 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company2, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr2);
		//
		Company company3 = BaseCompanyTest.DataInput.getCompany();
		// sessionOP =
		// (MockHttpSession)
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr3 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company3, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr3);
		// ...????????????
	}

	@Test
	public void testCreate5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:??????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setName("");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr1 = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session(sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr1, Company.FIELD_ERROR_name, errorMsg);
	}

	@Test
	public void testCreate6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6:??????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossName("");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_HumanName, errorMsg);
	}

	@Test
	public void testCreate7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7:????????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setName("");
		company.setBossName("");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_name, errorMsg);
	}

	@Test
	public void testCreate8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8:????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossPassword("");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9: ??????????????????????????????15????????????(????????????????????????????????????????????????????????????????????????????????????????????????15??????18???)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
	}

	@Test
	public void testCreate10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case10: ???????????????????????????(????????????)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("123456789 12345");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testCreate11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case11: ???????????????????????????(??????15???)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("12345678912345");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testCreate12() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case12: ???????????????????????????(??????15????????????18???)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("1234567890123456");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testCreate13() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case13: ????????????(????????????????????????)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("222222222222222");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreate14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14: ????????????(dbName??????)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setDbName(Shared.DBName_Test);
		// ;
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreate15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15: ??????????????????????????? ??????????????????1");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setSubmchid(BaseCompanyTest.nbrSubmchid);
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
	}

	// @Test
	// public void testCreate15() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// String dbName = Shared.GenerateDBName();
	// Shared.caseLog("Case15: ????????????(key??????)");
	// session =
	// BaseCompanyTest.getUploadBusinessLicensePictureSession().getRequest().getSession();
	// //
	// MvcResult mr15 = mvc.perform(//
	// post("/company/createEx.bx")//
	// .param(Company.field.getFIELD_NAME_name(), "BX??????13" +
	// System.currentTimeMillis() % 1000000)//
	// .param(Company.field.getFIELD_NAME_businessLicenseSN(), "14" +
	// System.currentTimeMillis())//
	// .param(Company.field.getFIELD_NAME_bossName(), "??????3???")//
	// .param(Company.field.getFIELD_NAME_bossPhone(), "13324544444")//
	// .param(Company.field.getFIELD_NAME_bossPassword(), "000000")//
	// .param(Company.field.getFIELD_NAME_bossWechat(), "cjs123456")//
	// .param(Company.field.getFIELD_NAME_dbName(), dbName)//
	// .param(Company.field.getFIELD_NAME_key(),
	// "12345678901234567890123456789013")//
	// .param(Company.field.getFIELD_NAME_status(), "0")//
	// .param(Company.field.getFIELD_NAME_incumbent(), "0")//
	// .param(Company.field.getFIELD_NAME_int2(), "0")//
	// .param(Company.field.getFIELD_NAME_dbUserName(), DBUserName_Default)//
	// .param(Company.field.getFIELD_NAME_dbUserPassword(),
	// DBUserPassword_Default)//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();//
	//
	// Shared.checkJSONErrorCode(mr15, EnumErrorCode.EC_Duplicated);
	// }

	// ?????????????????????????????????null??????
	// @Test
	// public void testCreate16() throws Exception{
	// Shared.printTestMethodStartInfo();
	// //
	// Company c = new Company();
	// String dbName = Shared.GenerateDBName();
	// System.out.println("\n-------------------------Case16:
	// ????????????(????????????????????????)----------------------");
	// session =
	// BaseCompanyTest.getUploadBusinessLicensePictureSession().getRequest().getSession();
	// MvcResult mr16 = mvc.perform(//
	// post("/company/createEx.bx")//
	// .param(Company.field.getFIELD_NAME_name(), "BX??????13" +
	// System.currentTimeMillis() % 1000000)//
	// .param(Company.field.getFIELD_NAME_businessLicenseSN(),
	// "14"+System.currentTimeMillis())//
	// .param(Company.field.getFIELD_NAME_businessLicensePicture(), "")//
	// .param(Company.field.getFIELD_NAME_bossName(), "??????3???")//
	// .param(Company.field.getFIELD_NAME_bossPhone(), "13324544444")//
	// .param(Company.field.getFIELD_NAME_bossPassword(), "000000")//
	// .param(Company.field.getFIELD_NAME_bossWechat(), "cjs123456")//
	// .param(Company.field.getFIELD_NAME_dbName(), dbName)//
	// .param(Company.field.getFIELD_NAME_key(),
	// UUID.randomUUID().toString().substring(1, 6))//
	// .param(Company.field.getFIELD_NAME_status(), "0")//
	// .param(Company.field.getFIELD_NAME_incumbent(), "0")//
	// .param(Company.field.getFIELD_NAME_int2(), "0")//
	// .param(Company.field.getFIELD_NAME_dbUserName(), DBUserName_Default)//
	// .param(Company.field.getFIELD_NAME_dbUserPassword(),
	// DBUserPassword_Default)//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();//
	//
	// Shared.checkJSONErrorCode(mr16, EnumErrorCode.EC_BusinessLogicNotDefined);
	// }

	@Test
	public void testCreate17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case17: ??????????????????????????????15??????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("ASDFGHJKLQWERTY");
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
	}

	@Test
	public void testCreate18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case18: ??????????????????????????????15?????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("ASD123JKL456RTY");
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
	}

	@Test
	public void testCreate19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case19: ??????????????????????????????18?????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("ASD123JKL456RTY123");
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
	}

	@Test
	public void testCreate20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case20: ??????????????????????????????18??????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("ASDFGHJKLQWERTYTYU");
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
	}

	@Test
	public void testCreate21() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case21: ??????????????????????????????18????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("123456789123456789");
		BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
	}

	@Test
	public void testCreate22() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case22: ???????????????????????????(??????18???)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("1234567891234567891234");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testCreate23() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case23: ???????????????????????????(?????????15??????18?????????????????????????????????????????????)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("123456a89bb3456");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testCreate24() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case24: ???????????????????????????(15??????18?????????????????????????????????????????????)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("123456%89???%3456");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testCreate25() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case25: ???????????????????????????(15??????18???????????????????????????????????????)");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBusinessLicenseSN("123456???89??????3456");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testCreate26() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case26: ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");

		Company company1 = BaseCompanyTest.DataInput.getCompany();
		company1.setSubmchid("");
		BaseCompanyTest.createCompanyViaAction(company1, mvc, mapBO, sessionOP);
		//
		Company company2 = BaseCompanyTest.DataInput.getCompany();
		company2.setSubmchid("");
		BaseCompanyTest.createCompanyViaAction(company2, mvc, mapBO, sessionOP);
	}

	@Test
	public void testCreate27() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case27: ?????????????????????????????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossName("??????1???");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_HumanName, errorMsg);
	}

	@Test
	public void testCreate28() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case28: ??????????????????????????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossPassword("???@#123??????Abc$%^");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate29() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case29: ????????????????????????????????????6???");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossPassword("123ab");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate30() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case30: ????????????????????????????????????16???");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossPassword("12345123451234512345");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate32() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case32: ?????????????????????????????????null");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossPassword(null);
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate34() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case34: ???????????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossPassword("!@#123??????Abc$%^");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate35() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case35: ?????????????????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossPassword(" !@#123Abc$%^ ");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate36() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case36: ?????????????????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setBossPassword("                ");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate37() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case37: ??????????????????DB??????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setDbUserPassword("???@#123??????Abc$%^");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate38() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case38: ??????????????????DB????????????6???");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setDbUserPassword("123ab");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate39() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case39: ??????????????????DB????????????16???");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setDbUserPassword("12345123451234512345");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate40() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case40: ??????????????????DB???????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setDbUserPassword("");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate41() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case41: ??????????????????DB?????????null");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setDbUserPassword(null);
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate44() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case44: ??????????????????DB?????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setDbUserPassword("??????@#123Abc$%^???");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate45() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case45: ??????????????????DB?????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setDbUserPassword(" ???@#123Abc$%^ ");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session(sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate46() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case46: ??????????????????DB?????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setDbUserPassword("                ");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, errorMsg);
	}

	@Test
	public void testCreate47() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case47: ???OP????????????BXStaff??????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setName(BaseCompanyTest.nbrCloned.getName());
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, session1) //
				.session((MockHttpSession) session1)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testCreate48() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case48: ???OP????????????Staff??????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setName(BaseCompanyTest.nbrCloned.getName());
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, session1) //
				.session((MockHttpSession) session1)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testCreate49() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case49: ???????????????????????????brandName");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		String brandName = "?????????";
		Company company1 = BaseCompanyTest.DataInput.getCompany();
		company1.setBrandName(brandName);
		BaseCompanyTest.createCompanyViaAction(company1, mvc, mapBO, sessionOP);
		Company company2 = BaseCompanyTest.DataInput.getCompany();
		company2.setBrandName(brandName);
		BaseCompanyTest.createCompanyViaAction(company2, mvc, mapBO, sessionOP);
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: ???????????? ");
		MvcResult mr = mvc.perform(//
				get("/company/retrieve1Ex.bx?ID=1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		BaseCompanyTest.checkSensitiveInformation(mr);

		Shared.caseLog("Case2: ??????????????????ID");
		MvcResult mr2 = mvc.perform(//
				get("/company/retrieve1Ex.bx?ID=-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		JSONObject o = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Company company1 = new Company();
		company1 = (Company) company1.parse1(o.getJSONObject(BaseAction.KEY_Object).toString());
		Assert.assertTrue(company1 == null, "??????company1??????null????????????????????????");
		// bxStaff????????????????????????
		// System.out.println("\n-------------------------Case3:????????????????????????----------------------");
		// MvcResult mr3 = mvc.perform(//
		// get("/company/retrieve1Ex.bx?ID=1")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc, Staff2Phone)))//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieve3Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: ???OP????????????BXStaff??????????????????");
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				get("/company/retrieve1Ex.bx?ID=1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testRetrieve4Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4: ???OP????????????Staff??????????????????");
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				get("/company/retrieve1Ex.bx?ID=1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// Shop s = new Shop();
		Shared.caseLog("case1:???????????????????????? ");
		MvcResult mr = mvc.perform(//
				get("/company/retrieveNEx.bx")//
						.param(Shop.field.getFIELD_NAME_iPageIndex(), "1").param(Shop.field.getFIELD_NAME_iPageSize(), "5").contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<JSONObject> list = JsonPath.read(o, "$.objectList[*]");
		for (JSONObject o1 : list) {
			Shop shop = (Shop) JSONObject.toBean(o1, Shop.class);
			assertEquals(shop.getKey(), "");
		}

		Shared.caseLog("case2:??????iDistrictID??????????????????");
		MvcResult mr1 = mvc.perform(//
				get("/company/retrieveNEx.bx")//
						.param(Shop.field.getFIELD_NAME_districtID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr1);
		JSONObject o1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<Integer> list1 = JsonPath.read(o1, "$.objectList[*].districtID");
		for (int i : list1) {
			Assert.assertTrue(i == 1);
		}
		List<JSONObject> list2 = JsonPath.read(o1, "$.objectList[*]");
		for (JSONObject o2 : list2) {
			Shop shop = (Shop) JSONObject.toBean(o2, Shop.class);
			assertEquals(shop.getKey(), "");
		}

		// System.out.println("\n-------------------------Case1: ????????????
		// ----------------------");
		// MvcResult mr = mvc.perform(//
		// get("/company/retrieveNEx.bx?status=0&SN=")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) sessionForCheckUniqueField))//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// Shared.checkJSONErrorCode(mr);
		//
		// System.out.println("\n-------------------------Case2: ???????????????????????????
		// ----------------------");
		// MvcResult mr2 = mvc.perform(//
		// get("/company/retrieveNEx.bx?status=1&SN=")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) sessionForCheckUniqueField))//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// Shared.checkJSONErrorCode(mr2);
		//
		// System.out.println("\n-------------------------Case3: ???????????????????????????
		// ----------------------");
		// MvcResult mr3 = mvc.perform(//
		// get("/company/retrieveNEx.bx?status=3&SN=")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) sessionForCheckUniqueField))//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_OtherError);

		// bxStaff????????????????????????
		// System.out.println("\n-------------------------Case4???????????????????????????
		// ----------------------");
		// MvcResult mr4 = mvc.perform(//
		// get("/company/retrieveNEx.bx?status=0&SN=")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc, Staff2Phone)))//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		//
		// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);

		Shared.caseLog("Case4?????????SN?????????????????? ");
		MvcResult mr5 = mvc.perform(//
				get("/company/retrieveNEx.bx?status=0&SN=" + Shared.DB_SN_Test)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr5);
		JSONObject o2 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		List<JSONObject> list3 = JsonPath.read(o2, "$.objectList[*]");
		for (JSONObject o3 : list3) {
			Shop shop = (Shop) JSONObject.toBean(o3, Shop.class);
			assertEquals(shop.getKey(), "");
		}
	}

	@Test
	public void testRetrieveN5Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:???OP????????????BXStaff????????????????????????");
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				get("/company/retrieveNEx.bx")//
						.param(Shop.field.getFIELD_NAME_iPageIndex(), "1").param(Shop.field.getFIELD_NAME_iPageSize(), "5").contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testRetrieveN6Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:???OP????????????Staff????????????????????????");
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				get("/company/retrieveNEx.bx")//
						.param(Shop.field.getFIELD_NAME_iPageIndex(), "1").param(Shop.field.getFIELD_NAME_iPageSize(), "5").contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testUpdate1() throws Exception {
		Shared.printTestMethodStartInfo();
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		Shared.caseLog("Case1: ????????????");
		//
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setID(1);
		company.setBusinessLicenseSN("76" + System.currentTimeMillis());
		company.setBossPhone("14578451248");
		company.setBossWechat("cjs123456");
		company.setKey(Shared.getFakedSalt());
		company.setName(Shared.generateCompanyName(Company.MAX_LENGTH_Name));
		company.setBossName("????????????");
		BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
		// ??????nbr??????
		BaseCompanyTest.updateCompanyViaAction(BaseCompanyTest.nbrCloned, mvc, sessionOP);
	}

	@Test
	public void testUpdate2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case2: ???????????????null");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setID(1);
		company.setBossPhone(null);
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_bossPhone, errorMsg);
	}

	@Test
	public void testUpdate3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3?????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setID(1);
		company.setName(null);
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_name, errorMsg);
	}

	@Test
	public void testUpdate4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4????????????????????? ");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setID(1);
		company.setBossName(null);
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_HumanName, errorMsg);
	}

	@Test
	public void testUpdate5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case5????????????????????????????????????");
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setID(1);
		company.setBossName(null);
		company.setName(null);
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_name, errorMsg);
		// bxStaff????????????????????????
		// System.out.println("\n-------------------------Case7???????????????????????????
		// ----------------------");
		// session =
		// BaseCompanyTest.getUploadBusinessLicensePictureSession().getRequest().getSession();
		// MvcResult mr7 = mvc.perform(//
		// post("/company/updateEx.bx")//
		// .param(Company.field.getFIELD_NAME_ID(), "1")//
		// .param(Company.field.getFIELD_NAME_businessLicenseSN(),
		// UUID.randomUUID().toString().substring(1, 7))//
		// .param(Company.field.getFIELD_NAME_businessLicensePicture(),
		// UUID.randomUUID().toString().substring(1, 7))//
		// .param(Company.field.getFIELD_NAME_bossPhone(), "14578451248")//
		// .param(Company.field.getFIELD_NAME_bossWechat(), "cjs123456")//
		// .param(Company.field.getFIELD_NAME_dbName(), GenerateDBName())//
		// .param(Company.field.getFIELD_NAME_key(),
		// UUID.randomUUID().toString().substring(1,
		// 6))//
		// .param(Company.field.getFIELD_NAME_name(), "????????????")//
		// .param(Company.field.getFIELD_NAME_bossName(), "????????????")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc, Staff2Phone))//
		//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();//
		//
		// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdate7() throws Exception {
		Shared.printTestMethodStartInfo();

		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		Shared.caseLog("Case7: ????????????,????????????????????????(15????????????)");
		Company company1 = BaseCompanyTest.DataInput.getCompany();
		//
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN(company1.getBusinessLicenseSN());
		BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
		// ??????nbr??????
		BaseCompanyTest.updateCompanyViaAction(BaseCompanyTest.nbrCloned, mvc, sessionOP);
	}

	@Test
	public void testUpdate8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case8:????????????????????????(???????????????????????????) ");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("12345678 012345");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testUpdate9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case9:????????????????????????(???????????????????????????15???) ");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("12345678012345");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testUpdate10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case10:????????????????????????(???????????????????????????15????????????18???) ");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("1234567890123456");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testUpdate11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case11????????????businessLicenseSN ");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN(BaseCompanyTest.nbrBxCloned.getBusinessLicenseSN());
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testUpdate12() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case12????????????????????????");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setName(BaseCompanyTest.nbrBxCloned.getName());
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testUpdate13() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case13????????????key");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setKey(BaseCompanyTest.nbrBxCloned.getKey());
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testUpdate14() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case14: session???????????????????????????????????????????????????");

		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionForCheckUniqueField)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);
		BaseCompanyTest.checkSensitiveInformation(mr);
		// ??????????????????????????????????????????????????????????????????
		Company createCompany = (Company) Shared.parse1Object(mr, new Company(), BaseAction.KEY_Object);
		company.setSubmchid(createCompany.getSubmchid());
		CompanyCP.verifyUpdate(mr, company, Shared.BXDBName_Test);
		// ??????nbr??????
		BaseCompanyTest.updateCompanyViaAction(BaseCompanyTest.nbrCloned, mvc, sessionOP);
	}

	@Test
	public void testUpdate15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15: ?????????????????????????????????????????????");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
		// ??????nbr??????
		BaseCompanyTest.updateCompanyViaAction(BaseCompanyTest.nbrCloned, mvc, sessionOP);
	}

	@Test
	public void testUpdate17() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case17:????????????????????????(???????????????????????????18???) ");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("1234567890123456789");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testUpdate18() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case18:????????????????????????(?????????15??????18?????????????????????????????????????????????) ");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("1234b678a012345");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testUpdate19() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case19:????????????????????????(?????????15??????18?????????????????????????????????????????????) ");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("1234$678&012345");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testUpdate20() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case20:????????????????????????(?????????15??????18???????????????????????????????????????) ");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("1234???678???012345");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Company.FIELD_ERROR_businessLicenseSN, errorMsg);
	}

	@Test
	public void testUpdate21() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case21: ????????????,????????????????????????(15??????????????????)");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("MNBVCXZLKJHGFDS");
		BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
		// ??????nbr??????
		BaseCompanyTest.updateCompanyViaAction(BaseCompanyTest.nbrCloned, mvc, sessionOP);
	}

	@Test
	public void testUpdate22() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case22: ????????????,????????????????????????(15?????????????????????????????????)");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("ASDFG1234QWERTY");
		BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
		// ??????nbr??????
		BaseCompanyTest.updateCompanyViaAction(BaseCompanyTest.nbrCloned, mvc, sessionOP);
	}

	@Test
	public void testUpdate23() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case23: ????????????,????????????????????????(18?????????????????????????????????)");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("ASDFG1234QWERT123Y");
		BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
		// ??????nbr??????
		BaseCompanyTest.updateCompanyViaAction(BaseCompanyTest.nbrCloned, mvc, sessionOP);
	}

	@Test
	public void testUpdate24() throws Exception {
		Shared.printTestMethodStartInfo();

		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		Shared.caseLog("Case24: ????????????,????????????????????????(18????????????)");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("12345" + String.valueOf(System.currentTimeMillis()).substring(0, 13));
		BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
		// ??????nbr??????
		BaseCompanyTest.updateCompanyViaAction(BaseCompanyTest.nbrCloned, mvc, sessionOP);
	}

	@Test
	public void testUpdate25() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case25: ????????????,????????????????????????(18??????????????????)");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBusinessLicenseSN("ASDFGHJKLQWERTYUIO");
		BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
		// ??????nbr??????
		BaseCompanyTest.updateCompanyViaAction(BaseCompanyTest.nbrCloned, mvc, sessionOP);
	}

	@Test
	public void testUpdate26() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case26:???????????????????????????????????????????????????????????????");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBossName("??????33???");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_HumanName, errorMsg);
	}

	@Test
	public void testUpdate27() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case27: ???OP????????????BXStaff??????????????????????????????");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBossName("??????33???");
		//
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, session1) //
				.session((MockHttpSession) session1)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testUpdate28() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case28: ???OP????????????Staff??????????????????????????????");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setBossName("??????33???");
		//
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, session1) //
				.session((MockHttpSession) session1)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testUpdate29() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("??????????????????????????????logo????????????");
		Company companyInDB = BaseCompanyTest.createCompanyViaAction(BaseCompanyTest.DataInput.getCompany(), mvc, mapBO, sessionOP);

		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setID(companyInDB.getID());
		Company updateAfterCompany = BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
		Assert.assertTrue(updateAfterCompany.getLogo().equals(companyInDB.getLogo()), "?????????logo???????????????logo????????????");
	}

	@Test
	public void testUpdate30() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("???????????????????????????logo?????????????????????");
		Company companyInDB = BaseCompanyTest.createCompanyViaAction(BaseCompanyTest.DataInput.getCompany(), mvc, mapBO, sessionOP);
		//
		String logoFileNmae = companyInDB.getLogo().substring(18);
		File oldFile = new File(BaseAction.CompanyLogoDir + logoFileNmae);
		Assert.assertTrue(oldFile.exists(), "???????????????");
		long oldFilelength = oldFile.length();

		String path = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\day.jpg";
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadLogoURL, mvc, sessionOP, path, BaseCompanyTest.DEFAULT_imagesType);

		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setID(companyInDB.getID());
		company.setDbName(companyInDB.getDbName());
		companyInDB = BaseCompanyTest.updateCompanyViaAction(company, mvc, sessionOP);
		//
		logoFileNmae = companyInDB.getLogo().substring(18);
		File newFile = new File(BaseAction.CompanyLogoDir + logoFileNmae);
		Assert.assertTrue(newFile.exists(), "???????????????");
		 
		Assert.assertTrue(oldFilelength != newFile.length(), "?????????????????????");
	}

	@Test
	public void testUpdateSubmchidEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: ???????????????????????????????????????????????????????????????2");
		//
		Company company = BaseCompanyTest.DataInput.getCompany();
		company.setID(Shared.BIG_ID);
		MvcResult mr = mvc.perform(//
				post("/company/updateSubmchidEx.bx")//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(company.getID()))//
						.param(Company.field.getFIELD_NAME_submchid(), company.getSubmchid())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testUpdateSubmchidEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: ?????????????????????????????????");
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		company.setSubmchid(String.valueOf(System.currentTimeMillis()).substring(0, 10));
		BaseCompanyTest.updateSubmchidCompanyViaAction(company, mvc);
		// ????????????
		BaseCompanyTest.updateSubmchidCompanyViaAction(BaseCompanyTest.nbrCloned, mvc);
	}

	@Test
	public void testUpdateSubmchidEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: ????????????????????????????????????????????????????????????????????????1");
		Company company = (Company) BaseCompanyTest.nbrBxCloned.clone();
		company.setSubmchid(BaseCompanyTest.nbrCloned.getSubmchid());
		MvcResult mr = mvc.perform(//
				post("/company/updateSubmchidEx.bx")//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(company.getID()))//
						.param(Company.field.getFIELD_NAME_submchid(), company.getSubmchid())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testUpdateSubmchidEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4: ???????????????????????????????????????A????????????????????????B????????????B??????????????????????????????.?????????????????????.");
		Company company1 = BaseCompanyTest.DataInput.getCompany();
		company1.setSubmchid("");
		BaseCompanyTest.createCompanyViaAction(company1, mvc, mapBO, sessionOP);
		//
		Company company2 = BaseCompanyTest.DataInput.getCompany();
		Company companyCreate = BaseCompanyTest.createCompanyViaAction(company2, mvc, mapBO, sessionOP);
		// ????????????
		Company company = (Company) companyCreate.clone();
		company.setSubmchid("");
		//
		MvcResult mr1 = mvc.perform(//
				post("/company/updateSubmchidEx.bx")//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(company.getID()))//
						.param(Company.field.getFIELD_NAME_submchid(), company.getSubmchid())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOP)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);
		CompanyCP.verifyUpdate(mr1, company, Shared.BXDBName_Test);
	}

	@Test
	public void testUpdateSubmchidEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5: ???OP????????????BXStaff??????????????????????????????");
		//
		Company company = BaseCompanyTest.DataInput.getCompany();
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				post("/company/updateSubmchidEx.bx")//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(company.getID()))//
						.param(Company.field.getFIELD_NAME_submchid(), company.getSubmchid())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testUpdateSubmchidEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6: ???OP????????????Staff??????????????????????????????");
		//
		Company company = BaseCompanyTest.DataInput.getCompany();
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				post("/company/updateSubmchidEx.bx")//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(company.getID()))//
						.param(Company.field.getFIELD_NAME_submchid(), company.getSubmchid())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}
	// action?????????delete?????????????????????????????????????????????mapper
	// @Test
	// public void testUpdateSubmchidEx4() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case4: ????????????????????????????????????????????????????????????");
	// session = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
	// MvcResult mr = mvc.perform(//
	// get("/company/deleteEx.bx?ID=1")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();//
	//
	//
	// Assert.assertTrue(mr.getResponse().getContentAsString().equals(""));
	// Company createCompany = (Company)Shared.parse1Object(mr, new Company());
	//
	// Company company = new Company();
	// company.setID(2);
	// company.setSubmchid(createCompany.getSubmchid());
	//
	// MvcResult mr1 = mvc.perform(//
	// post("/company/updateSubmchidEx.bx")//
	// .param(Company.field.getFIELD_NAME_ID(), String.valueOf(company.getID()))//
	// .param(Company.field.getFIELD_NAME_submchid(), company.getSubmchid())//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();//
	//
	// Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_Duplicated);
	//
	// }

	// @Test
	// public void testUpdateSubmchidEx5() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case5: ??????????????????????????????????????????????????????2");
	//
	//
	// companyDelete.setSubmchid(timeStr);
	// MvcResult mr2 = mvc.perform(//
	// post("/company/updateSubmchidEx.bx")//
	// .param(Company.field.getFIELD_NAME_ID(),
	// String.valueOf(companyDelete.getID()))//
	// .param(Company.field.getFIELD_NAME_submchid(), companyDelete.getSubmchid())//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();//
	// Shared.checkJSONErrorCode(mr2);
	// CompanyCP.verifyUpdate(mr2, companyDelete, Shared.BXDBName_Test);
	// }

	// ???????????????????????????null
	// @Test
	// public void testUpdate14() throws Exception {
	// Shared.printTestMethodStartInfo();
	// Company company = new Company();
	// System.out.println("\n-------------------------Case14??????????????????????????????
	// ----------------------");
	// session =
	// BaseCompanyTest.getUploadBusinessLicensePictureSession().getRequest().getSession();
	// MvcResult mr14 = mvc.perform(//
	// post("/company/updateEx.bx")//
	// .param(Company.field.getFIELD_NAME_ID(), "1")//
	// .param(Company.field.getFIELD_NAME_businessLicenseSN(), "12" +
	// System.currentTimeMillis())//
	// .param(Company.field.getFIELD_NAME_businessLicensePicture(), "")
	// .param(Company.field.getFIELD_NAME_bossPhone(), "14578451248")//
	// .param(Company.field.getFIELD_NAME_bossWechat(), "cjs123456")//
	// .param(Company.field.getFIELD_NAME_dbName(), GenerateDBName())//
	// .param(Company.field.getFIELD_NAME_key(),
	// UUID.randomUUID().toString().substring(1, 6))//
	// .param(Company.field.getFIELD_NAME_name(),
	// UUID.randomUUID().toString().substring(1, 6))//
	// .param(Company.field.getFIELD_NAME_bossName(), "??????8???")//
	// .param(Company.field.getFIELD_NAME_funcInfo(), "7")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();//
	//
	// Shared.checkJSONErrorCode(mr14, EnumErrorCode.EC_BusinessLogicNotDefined);
	// }

	// @Test(dependsOnMethods= {"testCreate1"})

	// public void testDelete() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// session = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
	//
	// Shared.caseLog("Case1: ????????????,?????????????????????????????? ");
	// Assert.assertTrue(companyToDelete > 0, "?????????????????????????????????????????????????????????");
	// MvcResult mr = mvc.perform(//
	// get("/company/deleteEx.bx?" + Company.field.getFIELD_NAME_ID() + "=" +
	// companyToDelete + "&" + Company.field.getFIELD_NAME_shopID() + "=" + 5)//
	// ??????nbr????????????shopID???5?????????
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();//
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// Shared.caseLog("Case2: ???????????????null ");
	// MvcResult mr2 = mvc.perform(//
	// get("/company/deleteEx.bx?ID=-1")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) session)//
	//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();//
	//
	// Assert.assertTrue(mr2.getResponse().getContentAsString().equals(""));
	//
	// // System.out.println("\n-------------------------Case3: ????????????,??????????????????????????????
	// // ----------------------");
	// //
	// // // ...Company???ID???1???nbr????????????????????????????????????????????????
	// // MvcResult mr3 = mvc.perform(//
	// // get("/company/deleteEx.bx?" + Company.field.getFIELD_NAME_ID() + "=" +
	// 1)//
	// // ??????nbr???????????????????????????
	// // .contentType(MediaType.APPLICATION_JSON)//
	// // .session((MockHttpSession) session)//
	// //
	// // )//
	// // .andExpect(status().isOk())//
	// // .andDo(print())//
	// // .andReturn();//
	// //
	// // Shared.checkJSONErrorCode(mr3);
	//
	// // bxStaff????????????????????????
	// // System.out.println("\n-------------------------Case3:????????????????????????
	// // ----------------------");
	// // MvcResult mr3 = mvc.perform(//
	// // get("/company/deleteEx.bx?ID=5")//
	// // .contentType(MediaType.APPLICATION_JSON)//
	// // .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Staff2Phone))//
	// //
	// // )//
	// // .andExpect(status().isOk())//
	// // .andDo(print())//
	// // .andReturn();//
	// //
	// // Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	// }

	// /** ?????????????????? */
	// private Company createCompany() throws ParseException,
	// CloneNotSupportedException {
	// CompanyMapper mapper = (CompanyMapper)
	// applicationContext.getBean("companyMapper");
	// Company company = BaseCompanyTest.DataInput.getCompany();
	//
	// Map<String, Object> params = company.getCreateParam(BaseBO.INVALID_CASE_ID,
	// company);
	// Company companyCreate = (Company) mapper.create(params);
	// company.setIgnoreIDInComparision(true);
	// if (company.compareTo(companyCreate) != 0) {
	// Assert.assertTrue(false, "???????????????????????????DB??????????????????");
	// }
	// assertTrue(companyCreate != null &&
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError, "??????????????????");
	//
	// return companyCreate;
	// }

	@Test
	public void testUploadAPICertEx() throws Exception {
		Shared.printTestMethodStartInfo();

		//
		Shared.caseLog("case1:?????????????????????????????????");
		//
		String path = System.getProperty("user.dir") + "\\src\\main\\resources";
		File file = new File(path + "\\apiclient_cert.p12");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "application/x-pkcs12", fis);
		MvcResult mr = mvc.perform(//
				fileUpload("/company/uploadAPICertEx.bx")//
						.file(multipartFile)//
						.param(Company.field.getFIELD_NAME_dbName(), "nbr11")//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionForCheckUniqueField) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

		File file1 = new File(path + "\\ApplicationContext.xml");
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile1 = new MockMultipartFile("file", originalFilename1, "xml", fis1);
		//
		Shared.caseLog("case2??????????????????????????????????????????");
		MvcResult mr2 = mvc.perform(//
				fileUpload("/company/uploadAPICertEx.bx")//
						.file(multipartFile1)//
						.param(Company.field.getFIELD_NAME_ID(), "1")//
						.param(Company.field.getFIELD_NAME_name(), "nbr12")//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionForCheckUniqueField) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError);
		Shared.checkJSONMsg(mr2, "?????????API?????????????????????.p12??????", "????????????????????????");
	}

	@Test
	public void testUploadAPICertEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:???OP????????????BXStaff??????????????????????????????");
		//
		String path = System.getProperty("user.dir") + "\\src\\main\\resources";
		File file = new File(path + "\\apiclient_cert.p12");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "application/x-pkcs12", fis);
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				fileUpload("/company/uploadAPICertEx.bx")//
						.file(multipartFile)//
						.param(Company.field.getFIELD_NAME_dbName(), "nbr11")//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) session1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testUploadAPICertEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:???OP????????????Staff??????????????????????????????");
		//
		String path = System.getProperty("user.dir") + "\\src\\main\\resources";
		File file = new File(path + "\\apiclient_cert.p12");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "application/x-pkcs12", fis);
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				fileUpload("/company/uploadAPICertEx.bx")//
						.file(multipartFile)//
						.param(Company.field.getFIELD_NAME_dbName(), "nbr11")//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) session1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testUploadBusinessLicensePictureEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:??????jpg?????????????????????");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);

		Shared.caseLog("case2????????????????????????");
		// ?????????????????????
		String path = System.getProperty("user.dir") + "\\src\\main\\resources";
		File file = new File(path + "\\apiclient_cert.p12");
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "application/x-pkcs12", fis);
		MvcResult mr2 = mvc.perform(//
				fileUpload("/company/uploadBusinessLicensePictureEx.bx")//
						.file(multipartFile)//
						.param(Company.field.getFIELD_NAME_ID(), "1")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.DBName_Test) //
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionForCheckUniqueField) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError);

		String path1 = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\big_normal.png";
		File file1 = new File(path1);
		String originalFilename1 = file1.getName();
		FileInputStream fis1 = new FileInputStream(file1);
		MockMultipartFile multipartFile1 = new MockMultipartFile("file", originalFilename1, "image/png", fis1);
		//
		Shared.caseLog("case3:??????png??????????????????");
		MockHttpSession sMockHttpSession = (MockHttpSession) Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		MvcResult mr = mvc.perform(//
				fileUpload("/company/uploadBusinessLicensePictureEx.bx")//
						.file(multipartFile1)//
						.param(Company.field.getFIELD_NAME_ID(), "1")//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session(sMockHttpSession) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testUploadBusinessLicensePictureEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:???OP????????????BXStaff??????????????????jpg?????????????????????");
		String path = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\adding.jpg";
		File file = new File(path);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/jpeg", fis);
		//
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				fileUpload("/company/uploadBusinessLicensePictureEx.bx")//
						.file(multipartFile)//
						.param(Company.field.getFIELD_NAME_ID(), "1")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.DBName_Test)//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) session1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testUploadBusinessLicensePictureEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:???OP????????????Staff??????????????????jpg?????????????????????");
		String path = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\adding.jpg";
		File file = new File(path);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/jpeg", fis);
		//
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				fileUpload("/company/uploadBusinessLicensePictureEx.bx")//
						.file(multipartFile)//
						.param(Company.field.getFIELD_NAME_ID(), "1")//
						.param(Company.field.getFIELD_NAME_dbName(), Shared.DBName_Test)//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) session1) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testUploadLogoEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("??????jpg?????????Logo");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadLogoURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
	}

	@Test
	public void testUploadLogoEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("??????png??????Logo");
		// TODO ?????????????????????BaseCompanyTest.getUploadLogoSession(mvc, sessionOP);
		String path = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\big_normal.png";
		File file = new File(path);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/png", fis);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/company/uploadLogoEx.bx")//
						.file(multipartFile)//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session(sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testUploadLogoEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("?????????????????????");
		// TODO ?????????????????????BaseCompanyTest.getUploadLogoSession(mvc, sessionOP);
		String path = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\PersonalFolders.gif";
		File file = new File(path);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/gif", fis);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/company/uploadLogoEx.bx")//
						.file(multipartFile)//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session(sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testUploadLogoEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("???OP??????????????????jpg?????????????????????");
		// TODO ?????????????????????BaseCompanyTest.getUploadLogoSession(mvc, sessionOP);
		String path = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\big_normal.png";
		File file = new File(path);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/png", fis);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/company/uploadLogoEx.bx")//
						.file(multipartFile)//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session(sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "?????????????????????logo???");
	}

	@Test
	public void testUploadLogoEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("??????logo??????");
		// TODO ?????????????????????BaseCompanyTest.getUploadLogoSession(mvc, sessionOP);
		String path = System.getProperty("user.dir") + "\\src\\test\\resources\\images\\overSizeJPG.jpg";
		File file = new File(path);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/jpeg", fis);
		//
		MvcResult mr = mvc.perform(//
				fileUpload("/company/uploadLogoEx.bx")//
						.file(multipartFile)//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session(sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: ????????????????????????????????????");
		MvcResult mr1 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckName))//
						.param(Company.field.getFIELD_NAME_uniqueField(), "????????????")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: ????????????????????????????????????");
		MvcResult mr2 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckName))//
						.param(Company.field.getFIELD_NAME_uniqueField(), BaseCompanyTest.nbrBxCloned.getName())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3: ???????????????????????????????????????");
		MvcResult mr3 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckBusinessLicenseSN)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "201908121203000")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4: ???????????????????????????????????????");
		MvcResult mr4 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckBusinessLicenseSN)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), BaseCompanyTest.nbrBxCloned.getBusinessLicenseSN())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_Duplicated);
	}

	// @Test
	public void testRetrieveNToCheckUniqueFieldEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		// ??????????????????????????????????????????
		// Shared.caseLog("case5: ??????????????????????????????????????????");
		// MvcResult mr5 = mvc.perform(//
		// post("/company/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) sessionForCheckUniqueField)//
		// .param(Company.field.getFIELD_NAME_fieldToCheckUnique(), "3") //
		// .param(Company.field.getFIELD_NAME_uniqueField(), "url=ZX454534354")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr5);

	}

	// @Test
	public void testRetrieveNToCheckUniqueFieldEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		// ??????????????????????????????????????????null???
		// System.out.println("------------------------case6:
		// ??????????????????????????????????????????-------------------------------");
		// MvcResult mr6 = mvc.perform(//
		// post("/company/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) sessionForCheckUniqueField)// \
		// .param(Company.field.getFIELD_NAME_fieldToCheckUnique(),
		// "3").param(Company.field.getFIELD_NAME_string1(), "url=25454534354")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_Duplicated);
	}

	// @Test
	public void testRetrieveNToCheckUniqueFieldEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		// ???????????????Key ???????????????
		// Shared.caseLog("case7: ???????????????????????????????????????Key");
		// MvcResult mr7 = mvc.perform(//
		// post("/company/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) sessionForCheckUniqueField)//
		// .param(Company.field.getFIELD_NAME_fieldToCheckUnique(), "4") //
		// .param(Company.field.getFIELD_NAME_uniqueField(), "ZX28248")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr7);
	}

	// @Test
	public void testRetrieveNToCheckUniqueFieldEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		// ???????????????Key ???????????????
		// Shared.caseLog("case8: ???????????????????????????????????????Key");
		// MvcResult mr8 = mvc.perform(//
		// post("/company/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) sessionForCheckUniqueField)//
		// .param(Company.field.getFIELD_NAME_fieldToCheckUnique(), "4")//
		// .param(Company.field.getFIELD_NAME_uniqueField(),
		// "12345678901234567890123456789013")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9: ?????????????????????????????????DB??????");
		MvcResult mr9 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckDbName)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "zxr")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr9);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10: ?????????????????????????????????DB??????");
		MvcResult mr10 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckDbName)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), BaseCompanyTest.nbrCloned.getDbName())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11: ????????????????????????????????????,?????????ID????????????????????????????????????ID??????");
		MvcResult mr11 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckName)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), BaseCompanyTest.nbrCloned.getName())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr11);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12: ??????????????????????????????????????????????????????ID???????????????????????????????????????ID??????");
		MvcResult mr12 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckBusinessLicenseSN)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), BaseCompanyTest.nbrCloned.getBusinessLicenseSN())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr12);
	}

	// @Test
	public void testRetrieveNToCheckUniqueFieldEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		// ??????????????????????????????????????????
		// Shared.caseLog("case13: ?????????????????????????????????????????????????????????ID??????????????????????????????????????????ID??????");
		// MvcResult mr13 = mvc.perform(//
		// post("/company/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) sessionForCheckUniqueField)//
		// .param(Company.field.getFIELD_NAME_ID(), "1") //
		// .param(Company.field.getFIELD_NAME_fieldToCheckUnique(), "3") //
		// .param(Company.field.getFIELD_NAME_uniqueField(), "url=25454534354")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr13);
	}

	// @Test
	public void testRetrieveNToCheckUniqueFieldEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		// ???????????????Key ???????????????
		// Shared.caseLog("case14: ????????????????????????????????????Key???????????????ID???????????????????????????Key?????????ID??????");
		// MvcResult mr14 = mvc.perform(//
		// post("/company/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) sessionForCheckUniqueField)//
		// .param(Company.field.getFIELD_NAME_ID(), "1") //
		// .param(Company.field.getFIELD_NAME_fieldToCheckUnique(), "4") //
		// .param(Company.field.getFIELD_NAME_uniqueField(), "AS28248")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr14);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15: ??????????????????????????????DB?????????????????????ID?????????????????????DB???????????????ID??????");
		MvcResult mr15 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(BaseCompanyTest.nbrCloned.getID())) //
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckDbName)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), BaseCompanyTest.nbrCloned.getDbName())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr15);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16: ????????????????????????????????????");
		MvcResult mr16 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckSubmchid)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), String.valueOf(System.currentTimeMillis()).substring(0, 10))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr16);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17: ????????????????????????????????????");
		MvcResult mr17 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckSubmchid))//
						.param(Company.field.getFIELD_NAME_uniqueField(), BaseCompanyTest.nbrCloned.getSubmchid())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr17, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case18: ????????????????????????????????????????????????null");
		String companyName = null;
		MvcResult mr18 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckName))//
						.param(Company.field.getFIELD_NAME_uniqueField(), companyName)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr18, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19: ????????????????????????????????????????????????????????????");
		MvcResult mr19 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckName))//
						.param(Company.field.getFIELD_NAME_uniqueField(), "")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr19, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case20: ????????????????????????????????????????????????????????????????????????");
		MvcResult mr20 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckName))//
						.param(Company.field.getFIELD_NAME_uniqueField(), "adfs+m")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr20, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx21() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case21: ?????????????????????????????????????????????????????????12???");
		MvcResult mr21 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckName))//
						.param(Company.field.getFIELD_NAME_uniqueField(), "1234567890123")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr21, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx22() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case22: ???????????????????????????????????????????????????null");
		String businessLicenseSN = null;
		MvcResult mr22 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckBusinessLicenseSN)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), businessLicenseSN)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr22, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx23() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case23: ???????????????????????????????????????????????????????????????");
		MvcResult mr23 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckBusinessLicenseSN)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr23, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx24() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case24: ?????????????????????????????????????????????????????????????????????????????????");
		MvcResult mr24 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckBusinessLicenseSN)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "a12345678912345")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr24, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx25() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case25: ????????????????????????????????????????????????????????????15?????????18???,???15-18?????????");
		MvcResult mr25 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckBusinessLicenseSN)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "1112345678912345")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr25, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx26() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case26: ??????????????????DB?????????????????????????????????null");
		String dbName26 = null;
		MvcResult mr26 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckDbName)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), dbName26)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr26, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx27() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case27: ??????????????????DB?????????????????????????????????????????????");
		MvcResult mr27 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckDbName)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr27, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx28() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case28: ??????????????????DB??????????????????????????????????????????");
		MvcResult mr28 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckDbName)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "abc de")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr28, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx29() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case29: ??????????????????DB??????????????????????????????????????????????????????");
		MvcResult mr29 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckDbName)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "1abcde")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr29, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx30() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case30: ??????????????????DB?????????????????????????????????????????????20???");
		MvcResult mr30 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckDbName)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "a12345678912345678900")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr30, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx31() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case31: ????????????????????????????????????????????????????????????10???");
		MvcResult mr31 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckSubmchid)) // ??????Company???CASE_CheckSubmchid?????????????????????
						.param(Company.field.getFIELD_NAME_uniqueField(), "123456789")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr31, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx32() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case32: ????????????????????????????????????????????????????????????");
		MvcResult mr32 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckSubmchid)) // ??????Company???CASE_CheckSubmchid?????????????????????
						.param(Company.field.getFIELD_NAME_uniqueField(), "a123456789")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr32, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx33() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case33: ??????????????????????????????????????????????????????10???");
		MvcResult mr33 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckSubmchid)) // ??????Company???CASE_CheckSubmchid?????????????????????
						.param(Company.field.getFIELD_NAME_uniqueField(), "01234567890")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr33, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx34() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case34: ????????????????????????????????????????????????????????????15?????????18???,??????15???");
		MvcResult mr34 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckBusinessLicenseSN)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "11123456789123")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr34, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx35() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case35: ????????????????????????????????????????????????????????????15?????????18???,??????18???");
		MvcResult mr35 = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionForCheckUniqueField)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckBusinessLicenseSN)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), "1112345678912345678")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr35, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx36() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case36: ???OP????????????BXStaff????????????????????????????????????????????????");
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckSubmchid)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), String.valueOf(System.currentTimeMillis()).substring(0, 10))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx37() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case37:???OP????????????Staff????????????????????????????????????????????????");
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				post("/company/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session1)//
						.param(Company.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Company.CASE_CheckSubmchid)) //
						.param(Company.field.getFIELD_NAME_uniqueField(), String.valueOf(System.currentTimeMillis()).substring(0, 10))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testDeleteLicensePictureEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:??????????????????-");
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		mvc.perform(//
				post("/company/deleteLicensePictureEx.bx")//
						.session((MockHttpSession) sessionOP))//
				.andExpect(status().isOk())//
				.andReturn();//
		File dest = (File) sessionOP.getAttribute(EnumSession.SESSION_CompanyBusinessLicensePictureDestination.getName());
		MultipartFile file = (MultipartFile) sessionOP.getAttribute(EnumSession.SESSION_CompanyBusinessLicensePictureFILE.getName());
		assertTrue(dest == null && file == null);
	}

	@Test
	public void testDeleteLicensePictureEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:???OP????????????BXStaff????????????????????????");
		HttpSession session1 = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				post("/company/deleteLicensePictureEx.bx")//
						.session((MockHttpSession) session1))//
				.andExpect(status().isOk())//
				.andReturn();//
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testDeleteLicensePictureEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:???OP????????????Staff????????????????????????");
		HttpSession session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult mr = mvc.perform(//
				post("/company/deleteLicensePictureEx.bx")//
						.session((MockHttpSession) session1))//
				.andExpect(status().isOk())//
				.andReturn();//
		//
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue("".equals(json), "????????????????????????");
	}

	@Test
	public void testUpdateVipSystemTipEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: ????????????????????????????????????");
		//
		Company company = (Company) BaseCompanyTest.nbrCloned.clone();
		MvcResult mr = mvc.perform(//
				post("/company/updateVipSystemTipEx.bx")//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(company.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
	}
	
	@Test
	public void testCreateShop() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: ??????????????????");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		Shop shopGet = BaseShopTest.DataInput.getShop();
		BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void testCreateShop2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: ????????????????????????????????????????????????");
		Shop shopGet = BaseShopTest.DataInput.getShop();
		BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_Duplicated);
	}
	
	
	@Test
	public void testCreateShop3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: ??????????????????????????????????????????????????????");
		//
		Company companyGet = BaseCompanyTest.DataInput.getCompany();
		Company companyCreated = BaseCompanyTest.createCompanyViaAction(companyGet, mvc, mapBO, sessionOP);
		//
		Shop shopGet = BaseShopTest.DataInput.getShop();
		shopGet.setCompanyID(companyCreated.getID());
		BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void testCreateShop4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4: ?????????????????????????????????????????????????????????????????????");
		//
		Company companyGet = BaseCompanyTest.DataInput.getCompany();
		Company companyCreated = BaseCompanyTest.createCompanyViaAction(companyGet, mvc, mapBO, sessionOP);
		//
		Shop shopGet = BaseShopTest.DataInput.getShop();
		shopGet.setCompanyID(companyCreated.getID());
		BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// ????????????
		BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_Duplicated);
	}
	
	
	@Test
	public void testDeleteShop() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: ??????????????????");
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void testDeleteShop2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: ??????????????????????????????????????????????????????????????????????????????");
		//
		Company companyGet = BaseCompanyTest.DataInput.getCompany();
		Company companyCreated = BaseCompanyTest.createCompanyViaAction(companyGet, mvc, mapBO, sessionOP);
		Shop shopGet = BaseShopTest.DataInput.getShop();
		shopGet.setCompanyID(companyCreated.getID());
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void testDeleteShop3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: ????????????????????? ????????????");
		// ?????????????????????????????????????????????????????????session???????????????????????????????????????session
		Shop shopGet = BaseShopTest.DataInput.getShop();
		shopGet.setID(1);
		//
		BaseShopTest.deleteViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testDeleteShop4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4: ????????????????????????????????????????????????????????????");
		//
		Company companyGet = BaseCompanyTest.DataInput.getCompany();
		Company companyCreated = BaseCompanyTest.createCompanyViaAction(companyGet, mvc, mapBO, sessionOP);
		Shop shopGet = BaseShopTest.DataInput.getShop();
		shopGet.setCompanyID(companyCreated.getID());
		shopGet.setID(1);
		//
		BaseShopTest.deleteViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testDeleteShop5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5: ?????????????????????????????????????????????????????????????????????");
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		// ????????????????????????
		Staff staffGet = BaseStaffTest.DataInput.getStaff();
		staffGet.setShopID(shopCreated.getID());
		BaseStaffTest.createStaffViaAction(staffGet, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		Staff staffGet2 = BaseStaffTest.DataInput.getStaff();
		staffGet2.setShopID(shopCreated.getID());
		BaseStaffTest.createStaffViaAction(staffGet2, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		Staff staffRetrieveNCondition = new Staff();
		staffRetrieveNCondition.setStatus(BaseAction.INVALID_ID);
		staffRetrieveNCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		staffRetrieveNCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> bmList = BaseStaffTest.retrieveNViaAction(staffRetrieveNCondition, sessionBoss, mvc);
		Assert.assertTrue(bmList != null && bmList.size() > 0, "????????????????????????");
		for(BaseModel bm : bmList) {
			Staff staff = (Staff) bm;
			if(staff.getShopID() == shopCreated.getID()) {
				Assert.assertTrue(staff.getStatus() == Staff.EnumStatusStaff.ESS_Resigned.getIndex(), "????????????????????????");
			}
		}
	}
	
	@Test
	public void testDeleteShop6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6: ????????????????????????????????????????????????????????????????????????????????????");
		//
		Company companyGet = BaseCompanyTest.DataInput.getCompany();
		Company companyCreated = BaseCompanyTest.createCompanyViaAction(companyGet, mvc, mapBO, sessionOP);
		//
		Shop shopGet = BaseShopTest.DataInput.getShop();
		shopGet.setCompanyID(companyCreated.getID());
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		MockHttpSession sessionNewCompanyBoss = Shared.getStaffLoginSession(mvc, companyCreated.getBossPhone(), BaseCompanyTest.bossPassword_New, companyCreated.getSN());
		// ????????????????????????
		Staff staffGet = BaseStaffTest.DataInput.getStaff();
		staffGet.setShopID(shopCreated.getID());
		BaseStaffTest.createStaffViaAction(staffGet, sessionNewCompanyBoss, mvc, mapBO, companyGet.getDbName());
		//
		Staff staffGet2 = BaseStaffTest.DataInput.getStaff();
		staffGet2.setShopID(shopCreated.getID());
		BaseStaffTest.createStaffViaAction(staffGet2, sessionNewCompanyBoss, mvc, mapBO, companyGet.getDbName());
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		Staff staffRetrieveNCondition = new Staff();
		staffRetrieveNCondition.setStatus(BaseAction.INVALID_ID);
		staffRetrieveNCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		staffRetrieveNCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> bmList = BaseStaffTest.retrieveNViaAction(staffRetrieveNCondition, sessionNewCompanyBoss, mvc);
		Assert.assertTrue(bmList != null && bmList.size() > 0, "????????????????????????");
		for(BaseModel bm : bmList) {
			Staff staff = (Staff) bm;
			if(staff.getShopID() == shopCreated.getID()) {
				Assert.assertTrue(staff.getStatus() == Staff.EnumStatusStaff.ESS_Resigned.getIndex(), "????????????????????????");
			}
		}
	}
	
	@Test
	public void testDeleteShop7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7: ???????????????????????????????????????pos??????????????????????????????1");
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		// ??????????????????pos???
		Pos posGet = BasePosTest.DataInput.getPOS();
		posGet.setCompanySN(Shared.DB_SN_Test);
		posGet.setShopID(shopCreated.getID());
		BasePosTest.createPosViaSyncAction(posGet, mvc, mapBO, Shared.DBName_Test);
		//
		Pos posGet2 = BasePosTest.DataInput.getPOS();
		posGet2.setCompanySN(Shared.DB_SN_Test);
		posGet2.setShopID(shopCreated.getID());
		BasePosTest.createPosViaSyncAction(posGet2, mvc, mapBO, Shared.DBName_Test);
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		Pos posRetrieveNCondition = new Pos();
		posRetrieveNCondition.setStatus(BaseAction.INVALID_ID);
		posRetrieveNCondition.setShopID(shopCreated.getID());
		posRetrieveNCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		posRetrieveNCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<Pos> posList = BasePosTest.retrieveNViaAction(sessionBoss, mvc, posRetrieveNCondition);
		Assert.assertTrue(posList != null && posList.size() > 0, "????????????????????????");
		for(Pos pos : posList) {
			Assert.assertTrue(pos.getStatus() == Pos.EnumStatusPos.ESP_Inactive.getIndex(), "pos???????????????" + Pos.EnumStatusPos.ESP_Inactive.getIndex());
		}
	}
	
	
	@Test
	public void testDeleteShop8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8: ??????????????????????????????????????????????????????pos????????????????????????");
		//
		Company companyGet = BaseCompanyTest.DataInput.getCompany();
		Company companyCreated = BaseCompanyTest.createCompanyViaAction(companyGet, mvc, mapBO, sessionOP);
		//
		Shop shopGet = BaseShopTest.DataInput.getShop();
		shopGet.setCompanyID(companyCreated.getID());
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		MockHttpSession sessionNewCompanyBoss = Shared.getStaffLoginSession(mvc, companyCreated.getBossPhone(), BaseCompanyTest.bossPassword_New, companyCreated.getSN());
		// ??????????????????pos???
		Pos posGet = BasePosTest.DataInput.getPOS();
		posGet.setCompanySN(companyCreated.getSN());
		posGet.setShopID(shopCreated.getID());
		BasePosTest.createPosViaSyncAction(posGet, mvc, mapBO, companyGet.getDbName());
		//
		Pos posGet2 = BasePosTest.DataInput.getPOS();
		posGet2.setCompanySN(companyCreated.getSN());
		posGet2.setShopID(shopCreated.getID());
		BasePosTest.createPosViaSyncAction(posGet2, mvc, mapBO, companyGet.getDbName());
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		//
		Pos posRetrieveNCondition = new Pos();
		posRetrieveNCondition.setStatus(BaseAction.INVALID_ID);
		posRetrieveNCondition.setShopID(shopCreated.getID());
		posRetrieveNCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		posRetrieveNCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<Pos> posList = BasePosTest.retrieveNViaAction(sessionNewCompanyBoss, mvc, posRetrieveNCondition);
		Assert.assertTrue(posList != null && posList.size() > 0, "????????????????????????");
		for(Pos pos : posList) {
			Assert.assertTrue(pos.getStatus() == Pos.EnumStatusPos.ESP_Inactive.getIndex(), "pos???????????????" + Pos.EnumStatusPos.ESP_Inactive.getIndex());
		}
	}
	
	@Test
	public void testDeleteShop9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9: ????????????????????????????????????");
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testDeleteShop10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10: ???????????????????????????????????????????????????");
		//
		Company companyGet = BaseCompanyTest.DataInput.getCompany();
		Company companyCreated = BaseCompanyTest.createCompanyViaAction(companyGet, mvc, mapBO, sessionOP);
		Shop shopGet = BaseShopTest.DataInput.getShop();
		shopGet.setCompanyID(companyCreated.getID());
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testDeleteShop11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11: ?????????????????????pos???????????????????????????????????????????????????pos?????????");
		//
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		// ??????????????????pos???
		Pos posGet = BasePosTest.DataInput.getPOS();
		posGet.setCompanySN(Shared.DB_SN_Test);
		posGet.setShopID(shopCreated.getID());
		BasePosTest.createPosViaSyncAction(posGet, mvc, mapBO, Shared.DBName_Test);
		//
		Pos posGet2 = BasePosTest.DataInput.getPOS();
		posGet2.setCompanySN(Shared.DB_SN_Test);
		posGet2.setShopID(shopCreated.getID());
		BasePosTest.createPosViaSyncAction(posGet2, mvc, mapBO, Shared.DBName_Test);
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		Pos posRetrieveNCondition = new Pos();
		posRetrieveNCondition.setStatus(BaseAction.INVALID_ID);
		posRetrieveNCondition.setShopID(shopCreated.getID());
		posRetrieveNCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		posRetrieveNCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<Pos> posList = BasePosTest.retrieveNViaAction(sessionBoss, mvc, posRetrieveNCondition);
		Assert.assertTrue(posList != null && posList.size() > 0, "????????????????????????");
		ErrorInfo ecOut = new ErrorInfo();
		for(Pos pos : posList) {
			Assert.assertTrue(pos.getStatus() == Pos.EnumStatusPos.ESP_Inactive.getIndex(), "pos???????????????" + Pos.EnumStatusPos.ESP_Inactive.getIndex());
			// ??????????????????????????????????????????????????????
			Pos posCache = (Pos) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_POS).read1(pos.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			Assert.assertTrue(posCache.getStatus() == 1, "??????????????????????????????????????????pos=" + posCache);
			// ??????????????????????????????
			verifySyncDeleteSuccessResult(pos, posBO, posSyncCacheBO, posSyncCacheDispatcherBO, EnumSyncCacheType.ESCT_PosSyncInfo, BaseAction.INVALID_ID, Shared.DBName_Test);
		}
	}
	
	@Test
	public void testUpdateShop() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: ??????????????????");
		//
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		Shop shopUpdateGet = BaseShopTest.DataInput.getShop();
		shopUpdateGet.setID(shopCreated.getID());
		//
		BaseShopTest.updateViaCompanyAction(shopUpdateGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void testUpdateShop2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: ????????????????????????????????????????????????,????????????");
		//
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		Shop shopGet2 = BaseShopTest.DataInput.getShop();
		Shop shopCreated2 = BaseShopTest.createViaCompanyAction(shopGet2, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		Shop shopUpdateGet = BaseShopTest.DataInput.getShop();
		shopUpdateGet.setID(shopCreated2.getID());
		shopUpdateGet.setName(shopCreated.getName());
		//
		BaseShopTest.updateViaCompanyAction(shopUpdateGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_Duplicated);
	}
	
	@Test
	public void testUpdateShop3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: ????????????????????????????????????????????????");
		//
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		BaseShopTest.deleteViaCompanyAction(shopCreated, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		//
		Shop shopUpdateGet = BaseShopTest.DataInput.getShop();
		shopUpdateGet.setID(shopCreated.getID());
		//
		BaseShopTest.updateViaCompanyAction(shopUpdateGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
}
