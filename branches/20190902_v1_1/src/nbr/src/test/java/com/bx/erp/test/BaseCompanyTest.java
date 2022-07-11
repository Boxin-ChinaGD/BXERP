package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.CompanyBO;
import com.bx.erp.action.bo.ShopBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Company;
import com.bx.erp.model.Company.EnumCompanyCreationStatus;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

public class BaseCompanyTest extends BaseMapperTest {
	public static final String DBUserName_Default = "root";
	public static final String DBUserPassword_Default = "WEF#EGEHEH$$^*DI";
	/** 大鸭湾子商户号。大鸭湾是博昕的客户 */
	public static final String nbrSubmchid = "1574431111";
	/** 老板首次登陆后的密码 */
	public static final String bossPassword_New = "66666666";
	/** 测试开始时保存的nbr数据 */
	public static Company nbrCloned = null;
	/** 测试开始时保存的nbr_bx数据 */
	public static Company nbrBxCloned = null;

	public static String DEFAULT_imagesPath = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\adding.jpg";
	public static String DEFAULT_imagesType = "image/jpeg";
	public static final String uploadLogoURL = "/company/uploadLogoEx.bx";
	public static final String uploadBusinessLicensePictureURL = "/company/uploadBusinessLicensePictureEx.bx";

	public static class DataInput {
		private static Company companyInput = new Company();

		public static final Company getCompany() throws ParseException, CloneNotSupportedException, InterruptedException {
			companyInput = new Company();
			companyInput.setBusinessLicenseSN("668" + System.currentTimeMillis() % 1000000000000L);
			Thread.sleep(1);
			companyInput.setBossPhone(Shared.getValidStaffPhone());
			companyInput.setBossPassword(Shared.PASSWORD_DEFAULT);
			companyInput.setBossWechat("cjs123456");
			//
			companyInput.setDbName(Shared.GenerateDBName());
			companyInput.setBusinessLicensePicture("/p/common_db/license/" + companyInput.getDbName() + ".jpg");
			companyInput.setLogo("/p/common_db/logo/" + companyInput.getDbName() + ".jpg");
			//
			companyInput.setKey(UUID.randomUUID().toString().replace("-", "").toUpperCase());
			companyInput.setStatus(EnumCompanyCreationStatus.ECCS_Incumbent.getIndex());
			companyInput.setName(Shared.generateCompanyName(Company.MAX_LENGTH_Name));
			companyInput.setBossName("老板");
			companyInput.setDbUserName(DBUserName_Default);
			companyInput.setDbUserPassword(DBUserPassword_Default);
			companyInput.setSubmchid(String.format("%010d", System.currentTimeMillis() % 1000000000));
			companyInput.setBrandName(String.format("%010d", System.currentTimeMillis() % 1000000000));
			return (Company) companyInput.clone();
		}
	}

	/** mapper层创建公司。传入CaseID进行创建公司 */
	public static Company createCompanyViaMapper(Company company, EnumErrorCode eec) throws Exception {
		if (eec == EnumErrorCode.EC_NoError) {
			String err = company.checkCreate(BaseBO.CASE_SpecialResultVerification);
			assertTrue("".equals(err), err);
		}

		Map<String, Object> params = company.getCreateParam(BaseBO.INVALID_CASE_ID, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		Company companyCreate = (Company) companyMapper.create(params);
		//
		if (eec == EnumErrorCode.EC_NoError) {
			assertTrue(companyCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			companyCreate.setIgnoreIDInComparision(true);
			if (companyCreate.compareTo(company) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
		} else {
			assertTrue(companyCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == eec, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
		return companyCreate;
	}

	public static void deleteViaMapper(Company company) {
		Map<String, Object> deleteParam = company.getDeleteParam(BaseBO.INVALID_CASE_ID, company);
		companyMapper.delete(deleteParam);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	public static Company updateCompanyViaMapper(Company company, EnumErrorCode eec) {
		if (eec == EnumErrorCode.EC_NoError) {
			String err = company.checkUpdate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue(err.equals(""), err);
		}
		//
		Map<String, Object> updateParam = company.getUpdateParam(BaseBO.INVALID_CASE_ID, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		Company companyUpdate = (Company) companyMapper.update(updateParam);
		//
		if (eec == EnumErrorCode.EC_NoError) {
			Assert.assertTrue(companyUpdate != null && EnumErrorCode.EC_NoError == EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
					updateParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			if (companyUpdate.compareTo(company) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等" + companyUpdate + "和" + company + "不相等");
			}
			String err1 = companyUpdate.checkUpdate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue(err1.equals(""), err1);
		} else {
			Assert.assertTrue(companyUpdate == null && eec == EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], updateParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
		return companyUpdate;
	}

	public static Company updateSubmchidCompanyViaMapper(Company company) {
		//
		String err = company.checkUpdate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> paramsUpdate = company.getUpdateParam(BaseBO.CASE_Company_UpdateSubmchid, company);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		Company companyUpdate = (Company) companyMapper.updateSubmchid(paramsUpdate);
		Assert.assertTrue(EnumErrorCode.EC_NoError == EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
				"修改公司的子商户号失败！错误信息=" + paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		if (companyUpdate.compareTo(company) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等" + company + "和" + companyUpdate + "不相等");
		}
		String err1 = companyUpdate.checkUpdate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertTrue("".equals(err1), err1);

		return companyUpdate;
	}

	public static Company createCompanyViaAction(Company company, MockMvc mvc, Map<String, BaseBO> BOsMap, MockHttpSession sessionOfOP) throws Exception, UnsupportedEncodingException {
		BaseCompanyTest.getUploadFileSession(uploadBusinessLicensePictureURL, mvc, sessionOfOP, DEFAULT_imagesPath, DEFAULT_imagesType);
		//
		BaseCompanyTest.getUploadFileSession(uploadLogoURL, mvc, sessionOfOP, DEFAULT_imagesPath, DEFAULT_imagesType);
		//
		MvcResult mr = mvc.perform(getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOfOP) //
				.session(sessionOfOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CompanyBO companyBO = (CompanyBO) BOsMap.get(CompanyBO.class.getSimpleName());
		StaffBO staffBO = (StaffBO) BOsMap.get(StaffBO.class.getSimpleName());
		ShopBO shopBO = (ShopBO) BOsMap.get(ShopBO.class.getSimpleName());
		CompanyCP.verifyCreate(mr, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mr, company);
		// 验证敏感信息是否为空
		checkSensitiveInformation(mr);
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String SN = JsonPath.read(o, "$." + BaseAction.KEY_Object + "." + Company.field.getFIELD_NAME_SN());
		// 使用售前账号进行登录操作
		Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, SN);
		// 验证老板首次登录后会修改密码，判断是否正确修改后删除该售前账号。
		ValidateIfPreSaleAccountIsDeleted(company.getDbName(), mvc, BOsMap, SN, company.getBossPhone(), company.getBossPassword());
		//
		Company company1 = new Company();
		company1 = (Company) company1.parse1(o.getJSONObject(BaseAction.KEY_Object).toString());
		//
		return company1;
	}

	public static Company updateCompanyViaAction(Company company, MockMvc mvc, MockHttpSession sessionOfOP) throws Exception, UnsupportedEncodingException {
		BaseCompanyTest.getUploadFileSession(uploadBusinessLicensePictureURL, mvc, sessionOfOP, DEFAULT_imagesPath, DEFAULT_imagesType);
		//
		MvcResult mr = mvc.perform(getBuilder("/company/updateEx.bx", MediaType.APPLICATION_JSON, company, sessionOfOP).contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CompanyCP.verifyUpdate(mr, company, Shared.BXDBName_Test);
		// 验证敏感信息是否为空
		checkSensitiveInformation(mr);
		//
		return (Company) Shared.parse1Object(mr, company, BaseAction.KEY_Object);
	}

	public static void updateSubmchidCompanyViaAction(Company company, MockMvc mvc) throws Exception, UnsupportedEncodingException {
		HttpSession session = Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP);
		MvcResult mr1 = mvc.perform(//
				post("/company/updateSubmchidEx.bx")//
						.param(Company.field.getFIELD_NAME_ID(), String.valueOf(company.getID()))//
						.param(Company.field.getFIELD_NAME_submchid(), company.getSubmchid())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);
		CompanyCP.verifyUpdate(mr1, company, Shared.BXDBName_Test);
	}

	public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Company company, HttpSession session) {
		MockHttpServletRequestBuilder builder;
		if (url.contains("/company/createEx.bx")) {
			builder = post(url).contentType(contentType).session((MockHttpSession) session)//
					.param(Company.field.getFIELD_NAME_name(), company.getName())//
					.param(Company.field.getFIELD_NAME_businessLicenseSN(), company.getBusinessLicenseSN())//
					.param(Company.field.getFIELD_NAME_bossName(), company.getBossName())//
					.param(Company.field.getFIELD_NAME_bossPhone(), company.getBossPhone())//
					.param(Company.field.getFIELD_NAME_bossWechat(), company.getBossWechat())//
					.param(Company.field.getFIELD_NAME_key(), company.getKey())//
					.param(Company.field.getFIELD_NAME_bossPassword(), company.getBossPassword())//
					.param(Company.field.getFIELD_NAME_dbName(), company.getDbName())//
					.param(Company.field.getFIELD_NAME_submchid(), company.getSubmchid())//
					.param(Company.field.getFIELD_NAME_status(), String.valueOf(company.getStatus()))//
					.param(Company.field.getFIELD_NAME_dbUserName(), company.getDbUserName())//
					.param(Company.field.getFIELD_NAME_dbUserPassword(), company.getDbUserPassword())//
					.param(Company.field.getFIELD_NAME_brandName(), company.getBrandName())//
					.param(Company.field.getFIELD_NAME_logo(), company.getLogo());
		} else {
			builder = post(url).contentType(contentType).session((MockHttpSession) session)//
					.param(Company.field.getFIELD_NAME_ID(), String.valueOf(company.getID()))//
					.param(Company.field.getFIELD_NAME_dbName(), company.getDbName())//
					.param(Company.field.getFIELD_NAME_businessLicenseSN(), company.getBusinessLicenseSN())//
					.param(Company.field.getFIELD_NAME_bossPhone(), company.getBossPhone())//
					.param(Company.field.getFIELD_NAME_bossWechat(), company.getBossWechat())//
					.param(Company.field.getFIELD_NAME_key(), company.getKey())//
					.param(Company.field.getFIELD_NAME_name(), company.getName())//
					.param(Company.field.getFIELD_NAME_bossName(), company.getBossName())//
					.param(Company.field.getFIELD_NAME_brandName(), company.getBrandName())//
					.param(Company.field.getFIELD_NAME_logo(), company.getLogo());
		}
		return builder;
	}

	// /** 营业执照图片需要知道dbName，最终在磁盘上生成xxxx/{dbName}[.jpg|.png] */
	// public static MvcResult getUploadBusinessLicensePictureSession(String dbName,
	// MockMvc mvc, MockHttpSession sessionOfOP) throws Exception {
	// String path = System.getProperty("user.dir") +
	// "\\src\\main\\webapp\\images\\adding.jpg";
	// File file = new File(path);
	// String originalFilename = file.getName();
	// FileInputStream fis = new FileInputStream(file);
	// MockMultipartFile multipartFile = new MockMultipartFile("file",
	// originalFilename, "image/jpeg", fis);
	// //
	// MvcResult mr = mvc.perform(//
	// fileUpload("/company/uploadBusinessLicensePictureEx.bx")//
	// .file(multipartFile)//
	// .contentType(MediaType.MULTIPART_FORM_DATA) //
	// .session(sessionOfOP) //
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr);
	// return mr;
	// }

	public static MvcResult getUploadFileSession(String url, MockMvc mvc, MockHttpSession sessionOfOP, String path, String type) throws Exception {
		File file = new File(path);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, type, fis);
		//
		MvcResult mr = mvc.perform(//
				fileUpload(url)//
						.file(multipartFile)//
						.contentType(MediaType.MULTIPART_FORM_DATA) //
						.session(sessionOfOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		return mr;
	}

	/** 门店老板首次登录后会强制修改密码，在操作成功后做删除售前账号操作 */
	public static void ValidateIfPreSaleAccountIsDeleted(String dbName, MockMvc mvc, Map<String, BaseBO> BOsMap, String SN, String bossPhone, String bossPassword) throws Exception {
		MvcResult result = (MvcResult) mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), bossPhone)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andReturn();
		Shared.checkJSONErrorCode(result);
		String encrypt = Shared.encrypt(result, bossPassword);

		MvcResult result1 = (MvcResult) mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), bossPhone)//
						.param(Staff.field.getFIELD_NAME_salt(), "")//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), encrypt)//
						.param(Staff.field.getFIELD_NAME_companySN(), SN)//
						.session((MockHttpSession) result.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andReturn();//
		Shared.checkJSONErrorCode(result1);
		HttpSession session = result1.getRequest().getSession();
		// ..加密密码
		final String sPasswordEncryptedOld = encrypt; // 上面加密过了
		final String sPasswordEncryptedNew = Shared.encrypt(result, bossPassword_New);
		//
		DataSourceContextHolder.setDbName(dbName);//
		MvcResult m = mvc.perform(//
				post("/staff/resetMyPasswordEx.bx") //
						.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), bossPhone) //
						.param("sPasswordEncryptedOld", sPasswordEncryptedOld) //
						.param("sPasswordEncryptedNew", sPasswordEncryptedNew) //
						.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(m);
		//
		Staff staff2 = new Staff();
		staff2.setInvolvedResigned(Staff.NOT_INVOLVE_RESIGNED);
		staff2.setPhone(BaseAction.ACCOUNT_Phone_PreSale);
		DataSourceContextHolder.setDbName(dbName);
		StaffBO staffBO = (StaffBO) BOsMap.get(StaffBO.class.getSimpleName());
		Staff s = (Staff) staffBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, staff2);
		assertEquals(staffBO.getLastErrorCode(), EnumErrorCode.EC_NoError);
		assertTrue(s == null, "售前贴在老板贴登录后，应该已经删除了，但竟然存在！");
	}

	/** 验证敏感信息是否为空 **/
	public static void checkSensitiveInformation(MvcResult mr) throws UnsupportedEncodingException {
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		Company c = new Company();
		c = (Company) c.parse1(o.getString(BaseAction.KEY_Object));
		assertTrue(c.getDbUserName().equals("") && c.getDbUserPassword().equals("") && c.getKey().equals(""));
	}

	/** 为避免污染缓存，先要保存缓存。待测试完成后，恢复缓存。 */
	public static void keepOldNbrAndNbrBx() throws CloneNotSupportedException {
		ErrorInfo ei = new ErrorInfo();
		Company nbr = (Company) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).read1(1, BaseBO.SYSTEM, ei, Shared.BXDBName_Test);
		String err = nbr.checkCreate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertTrue(err.equals(""), err + "\tobject=" + nbr);
		nbrCloned = (Company) nbr.clone(); // 保留此值，测试运行完毕之后恢复之
		err = nbrCloned.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(err.equals(""), err + "\tobject=" + nbr);
		Company nbrBx = (Company) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).read1(2, BaseBO.SYSTEM, ei, Shared.BXDBName_Test);
		err = nbrBx.checkCreate(BaseBO.CASE_SpecialResultVerification);
		Assert.assertTrue(err.equals(""), err + "\tobject=" + nbrBx);
		nbrBxCloned = (Company) nbrBx.clone(); // 保留此值，测试运行完毕之后恢复之
		err = nbrBxCloned.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(err.equals(""), err + "\tobject=" + nbrBx);
		// 敏感信息读不出，手动赋值
		nbrCloned.setKey("12345678901234567890123456789012");
		nbrCloned.setDbUserName("nbr");
		nbrCloned.setDbUserPassword("WEF#EGEHEH$$^*DI");
		nbrBxCloned.setKey("12345678901234567890123456789013");
		nbrBxCloned.setDbUserName("nbr_bx");
		nbrBxCloned.setDbUserPassword("WEF#EGEHEH$$^*DI");
	}
}
