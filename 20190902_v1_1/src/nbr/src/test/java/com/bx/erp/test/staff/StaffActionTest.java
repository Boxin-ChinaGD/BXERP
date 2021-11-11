package com.bx.erp.test.staff;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.interceptor.LoginGuard.LoginDevice;
import com.bx.erp.action.interceptor.StaffLoginInterceptor;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.StaffPermissionCache;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.model.StaffPermission;
import com.bx.erp.model.config.BxConfigGeneral;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.StaffCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.MD5Util;
import com.bx.erp.util.RSAUtils;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class StaffActionTest extends BaseActionTest {
	private HttpSession session1;
	private Staff staff;

	/** 要返回对象 */
	public static final int RETURN_OBJECT = 1;

	/** 多处使用，变为全局变量 */
	final static String Key_NewPasword = "sPasswordEncryptedNew";
	final static String Key_OldPasword = "sPasswordEncryptedOld";
	final static String Key_isResetOtherPassword = "isResetOtherPassword";

	@BeforeClass
	public void setup() {
		super.setUp();

		try {
			session1 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		staff = (Staff) session1.getAttribute(EnumSession.SESSION_Staff.getName());
		//
		ConfigCacheSize configCacheSize = new ConfigCacheSize();
		configCacheSize.setID(EnumConfigCacheSizeCache.ECC_StaffCacheSize.getIndex());
		configCacheSize.setName(EnumConfigCacheSizeCache.ECC_StaffCacheSize.getName());
		configCacheSize.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(configCacheSize, Shared.DBName_Test, BaseBO.SYSTEM);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
		//
		ConfigCacheSize configCacheSize = new ConfigCacheSize();
		configCacheSize.setID(EnumConfigCacheSizeCache.ECC_StaffCacheSize.getIndex());
		configCacheSize.setName(EnumConfigCacheSizeCache.ECC_StaffCacheSize.getName());
		configCacheSize.setValue("50");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(configCacheSize, Shared.DBName_Test, BaseBO.SYSTEM);
		try {
			// 把删除的售前恢复
			BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常添加");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mr, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		Company company = (Company) sessionBoss.getAttribute(EnumSession.SESSION_Company.getName());
		Staff staffSession = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
		DataSourceContextHolder.setDbName(company.getDbName());
		staffBO.deleteObject(staffSession.getID(), BaseBO.INVALID_CASE_ID, staff2);
		if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("删除员工失败，错误码=" + staffBO.getLastErrorCode() + ",错误信息=" + staffBO.getLastErrorMessage());
		}
		logger.info("删除员工成功，staff=" + staff.toString());

	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "在职员工中已经存在相同电话的员工，不能再创建这样的员工";
		Shared.caseLog("case2:" + message);
		// 先新建一个员工
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		//
		staff = (Staff) staffCopy.clone(); // 恢复原样
		// 重复添加
		MvcResult rett2 = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		String encryptt2 = Shared.encrypt(rett2, Shared.PASSWORD_DEFAULT);
		//
		Staff stafft2 = BaseStaffTest.DataInput.getStaff();
		stafft2.setPhone(Staff_Phone);
		stafft2.setPwdEncrypted(encryptt2);
		stafft2.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr1 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, stafft2)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, message, "错误信息不正确！");
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3：使用status=1,以离职的员工电话去创建新的");
		final String Staff_Phone2 = Shared.getValidStaffPhone();
		MvcResult ret3 = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone2, Staff.FOR_CreateNewStaff);
		String encrypt3 = Shared.encrypt(ret3, Shared.PASSWORD_DEFAULT);

		Staff staff3 = BaseStaffTest.DataInput.getStaff();
		staff3.setPwdEncrypted(encrypt3);
		staff3.setPhone(Staff_Phone2);
		staff3.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff3.setICID(Shared.getValidICID());
		MvcResult mr3 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff3).session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoError);
		StaffCP.verifyCreate(mr3, staff3, staffRoleBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4:正常添加,设置staff的RoleID为4，代表该角色为老板，SP_Staff_Create时，如果角色是门店老板，需要设置对应的shopID为1,DepartmentID为1");
		String Staff_Phone5 = Shared.getValidStaffPhone();
		MvcResult ret5 = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone5, Staff.FOR_CreateNewStaff);
		String encrypt5 = Shared.encrypt(ret5, Shared.PASSWORD_DEFAULT);

		Staff staff5 = BaseStaffTest.DataInput.getStaff();
		staff5.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff5.setReturnSalt(Staff.RETURN_SALT);
		staff5.setPhone(Staff_Phone5);
		staff5.setPwdEncrypted(encrypt5);
		MvcResult mr5 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff5)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr5);
		StaffCP.verifyCreate(mr5, staff5, staffRoleBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:正常添加,设置staff的RoleID不为4，代表该角色不是老板，对应的shopID、DepartmentID为任意数据库已存在的值");
		String Staff_Phone6 = Shared.getValidStaffPhone();

		MvcResult ret6 = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone6, Staff.FOR_CreateNewStaff);
		String encrypt6 = Shared.encrypt(ret6, Shared.PASSWORD_DEFAULT);

		Staff staff6 = BaseStaffTest.DataInput.getStaff();
		staff6.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		// staff6.setInt2(3);// RoleID传进SP_Staff_Create的iRoldID,代表角色id，角色id不为4时该角色就不是老板
		staff6.setPhone(Staff_Phone6);
		staff6.setPwdEncrypted(encrypt6);
		MvcResult mr6 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff6)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr6);
		StaffCP.verifyCreate(mr6, staff6, staffRoleBO, Shared.DBName_Test);
		// 结果验证：判断DB是否创建正确
		JSONObject o6 = JSONObject.fromObject(mr6.getResponse().getContentAsString()); //
		Staff staffOfDB6 = (Staff) new Staff().doParse1(o6.getJSONObject(BaseAction.KEY_Object));
		Assert.assertTrue(staffOfDB6.getShopID() != 0, "对应的shopID没有被设置为NULL");
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6:新增一个staff，该staff名字与在职的staff名字相同，但微信、手机号、微信、身份证不同，也能创建成功");
		// 先创建一个新的在职staff
		String Staff_Phone71 = Shared.getValidStaffPhone();
		MvcResult ret71 = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone71, Staff.FOR_CreateNewStaff);
		//
		String encrypt71 = Shared.encrypt(ret71, Shared.PASSWORD_DEFAULT);
		Staff staff71 = BaseStaffTest.DataInput.getStaff();
		staff71.setPhone(Staff_Phone71);
		staff71.setPwdEncrypted(encrypt71);
		staff71.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr71 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff71)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr71);
		StaffCP.verifyCreate(mr71, staff71, staffRoleBO, Shared.DBName_Test);

		// 创建一个与上面创建的名字相同的staff
		Thread.sleep(500);
		String Staff_Phone72 = Shared.getValidStaffPhone();
		MvcResult ret7 = BaseStaffTest.getToken(mvc, Staff_Phone72);
		String encrypt7 = Shared.encrypt(ret7, Shared.PASSWORD_DEFAULT);

		Staff staffCreate72 = BaseStaffTest.DataInput.getStaff();
		staffCreate72.setPhone(Staff_Phone72);
		staffCreate72.setPwdEncrypted(encrypt7);
		staffCreate72.setName(staff71.getName());
		staffCreate72.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr7 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staffCreate72)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr7);
		StaffCP.verifyCreate(mr7, staffCreate72, staffRoleBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case7: isFirsTimeLogin 为0  创建成功的staff应当isFirsTimeLogin为1");
		Staff staff8 = BaseStaffTest.DataInput.getStaff();
		staff8.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult ret8 = BaseStaffTest.getToken(sessionBoss, mvc, staff8.getPhone(), Staff.FOR_CreateNewStaff);
		String encrypt8 = Shared.encrypt(ret8, Shared.PASSWORD_DEFAULT);

		staff8.setPwdEncrypted(encrypt8);
		MvcResult mr8 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff8)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr8);
		StaffCP.verifyCreate(mr8, staff8, staffRoleBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case8: isFirsTimeLogin 为任意数  创建成功的staff应当isFirsTimeLogin为1");
		Staff staff9 = BaseStaffTest.DataInput.getStaff();
		staff9.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff9.setIsFirstTimeLogin(-2);

		MvcResult ret9 = BaseStaffTest.getToken(sessionBoss, mvc, staff9.getPhone(), Staff.FOR_CreateNewStaff);
		String encrypt9 = Shared.encrypt(ret9, Shared.PASSWORD_DEFAULT);

		staff9.setPwdEncrypted(encrypt9);
		MvcResult mr9 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff9)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr9);
		StaffCP.verifyCreate(mr9, staff9, staffRoleBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case9:新增一个staff，验证StaffPermission缓存是否正确。");
		Staff staff10 = BaseStaffTest.DataInput.getStaff();

		MvcResult ret10 = BaseStaffTest.getToken(sessionBoss, mvc, staff10.getPhone(), Staff.FOR_CreateNewStaff);
		String encrypt10 = Shared.encrypt(ret10, Shared.PASSWORD_DEFAULT);

		staff10.setPwdEncrypted(encrypt10);
		staff10.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		// staff10.setInt2(3);
		MvcResult mr10 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff10)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr10);
		StaffCP.verifyCreate(mr10, staff10, staffRoleBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "在职员工中已经存在相同身份证，不能再创建这样的员工";
		Shared.caseLog("case10:" + message);
		// 先新建一个员工
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		//
		staff = (Staff) staffCopy.clone(); // 恢复原样
		// 重复添加
		String Staff_Phone2 = Shared.getValidStaffPhone();
		MvcResult rett2 = BaseStaffTest.getToken(mvc, Staff_Phone2);
		String encryptt2 = Shared.encrypt(rett2, Shared.PASSWORD_DEFAULT);
		//
		Staff stafft2 = BaseStaffTest.DataInput.getStaff();
		stafft2.setPhone(Staff_Phone2);
		stafft2.setICID(staff.getICID());
		stafft2.setPwdEncrypted(encryptt2);
		stafft2.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr1 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, stafft2)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, message, "错误信息不正确！");
	}

	@Test
	public void testCreateEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "在职员工中已经存在相同微信员工，不能再创建这样的员工";
		Shared.caseLog("case11:" + message);
		// 先新建一个员工
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		//
		staff = (Staff) staffCopy.clone(); // 恢复原样
		// 重复添加
		String Staff_Phone2 = Shared.getValidStaffPhone();
		MvcResult rett2 = BaseStaffTest.getToken(mvc, Staff_Phone2);
		String encryptt2 = Shared.encrypt(rett2, Shared.PASSWORD_DEFAULT);
		//
		Staff stafft2 = BaseStaffTest.DataInput.getStaff();
		stafft2.setPhone(Staff_Phone2);
		stafft2.setWeChat(staff.getWeChat());
		stafft2.setPwdEncrypted(encryptt2);
		stafft2.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr1 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, stafft2)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, message, "错误信息不正确！");
	}

	@Test
	public void testCreateEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12: 不能用不存在的shopID创建");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		staff.setShopID(Shared.BIG_ID);
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13:正常添加");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		staff.setDepartmentID(Shared.BIG_ID);
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14:创建员工时，员工名称带有数字，创建失败");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setPwdEncrypted(encrypt);
		staff.setPhone(Staff_Phone);
		staff.setName("收银员1号");
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Staff.FIELD_ERROR_name, "返回的结果与预期的结果不一致");
	}

	@Test
	public void testCreateEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15:创建员工时，登录密码为空串创建失败");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, "");
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setPwdEncrypted(encrypt);
		staff.setPhone(Staff_Phone);
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	// CASE16在运行Shared.encrypt(ret, null);时，报空指针错误
	// @Test
	// public void testCreateEx16() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case16:创建员工时，登录密码为null创建失败");
	// String Staff_Phone = Shared.getValidStaffPhone();
	// MvcResult ret = getToken(session, Staff_Phone, Staff.FOR_CreateNewStaff);
	// //
	// String encrypt = Shared.encrypt(ret, null);
	// Staff staff = BaseStaffTest.DataInput.getStaff();
	// staff.setPwdEncrypted(encrypt);
	// staff.setPhone(Staff_Phone);
	// staff.setRoleID(ROLE_ID1);
	// MvcResult mr = mvc.perform(//
	// BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx",
	// MediaType.APPLICATION_JSON,
	// staff)//
	// .session((MockHttpSession) session)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();//
	// // 结果验证：检查错误码
	// Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	// Shared.checkJSONMsg(mr,FieldFormat.FIELD_ERROR_Password, Msg);
	// }

	@Test
	public void testCreateEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17:创建员工时，登录密码有中文创建失败");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, "!@#123中文Abc$%^");
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setPwdEncrypted(encrypt);
		staff.setPhone(Staff_Phone);
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testCreateEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case18:创建员工时，登录密码首尾有中文创建失败");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, "中!@#123Abc$%^文");
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setPwdEncrypted(encrypt);
		staff.setPhone(Staff_Phone);
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testCreateEx19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19:创建员工时，登录密码首尾有空格创建失败");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, " !@#123Abc$%^ ");
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setPwdEncrypted(encrypt);
		staff.setPhone(Staff_Phone);
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testCreateEx20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case20:创建员工时，登录密码首尾全部是空格创建失败");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, "                ");
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setPwdEncrypted(encrypt);
		staff.setPhone(Staff_Phone);
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testCreateEx21() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case21:创建员工时，登录密码首尾小于6位创建失败");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, "12Ab!");
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setPwdEncrypted(encrypt);
		staff.setPhone(Staff_Phone);
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testCreateEx22() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case22:创建员工时，登录密码首尾大于16位创建失败");
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, "1234567890Abcd!@#$%^");
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setPwdEncrypted(encrypt);
		staff.setPhone(Staff_Phone);
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testCreateEx23() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case23:售前账号为删除状态，老板创建一个和售前账号相同号码（13888888888）有老板权限的账号，创建失败");
		//
		Staff staff = new Staff();
		staff.setID(Shared.PreSaleID);
		BaseStaffTest.deleteStaffViaAction(staff, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		String Staff_Phone = BaseAction.ACCOUNT_Phone_PreSale;
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, "000000");
		Staff staff1 = BaseStaffTest.DataInput.getStaff();
		staff1.setPwdEncrypted(encrypt);
		staff1.setPhone(Staff_Phone);
		staff1.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff1)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, "非OP帐号不能创建售前帐号", BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
		//
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
	}

	// private MvcResult getToken(String Staff_Phone) throws Exception,
	// UnsupportedEncodingException {
	// MvcResult ret = mvc.perform(//
	// post("/staff/getTokenEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(ret);
	// return ret;
	// }

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正确的进行修改");
		Staff s = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// updateEx.bx时，RoleID代表角色
		staff2.setID(s.getID());

		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff2)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mr);
		StaffCP.verifyUpdate(mr, staff2, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2:主外键关系出错（门店id）");
		Staff s = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		Staff staff8 = BaseStaffTest.DataInput.getStaff();
		staff8.setID(s.getID());
		staff8.setShopID(Shared.BIG_ID);
		staff8.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());

		MvcResult mr4 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff8)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr4, "不能修改成不存在的门店", "错误信息不正确！");
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3:主外键关系出错（部门id）");
		Staff s = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		Staff staff10 = BaseStaffTest.DataInput.getStaff();
		staff10.setID(s.getID());
		staff10.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff10.setDepartmentID(Shared.BIG_ID);

		MvcResult mr5 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff10)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr5, "不能修改成不存在的部门", "错误信息不正确！");
	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4:主外键关系出错（角色id）");
		Staff s = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		Staff staff17 = BaseStaffTest.DataInput.getStaff();
		staff17.setID(s.getID());
		staff17.setRoleID(Shared.BIG_ID);
		MvcResult mr9 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff17)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr9, Staff.FIELD_ERROR_roleID, "错误信息不正确！");
	}

	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:修改为已有的信息（ICID）");
		Staff s = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		Staff staff18 = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		staff18.setICID(s.getICID());
		staff18.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());

		MvcResult mr10 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff18)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr10, "在职员工中已经存在相同身份证的员工，不能修改", "错误信息不正确");
	}

	@Test
	public void testUpdateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:修改为已有的信息（手机号）");
		Staff s = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		Staff staff18 = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		staff18.setPhone(s.getPhone());
		staff18.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());

		MvcResult mr10 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff18)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr10, "在职员工中已经存在相同电话的员工，不能修改", "错误信息不正确");
	}

	@Test
	public void testUpdateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:修改为已有的信息（微信）");
		Staff s = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		Staff staff18 = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		staff18.setWeChat(s.getWeChat());
		staff18.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());

		MvcResult mr10 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff18)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr10, "在职员工中已经存在相同微信的员工，不能修改", "错误信息不正确");
	}

	@Test
	public void testUpdateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:修改为已有的信息（名字）");
		Staff s = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		Staff staff18 = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		staff18.setName(s.getName());
		staff18.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		//
		MvcResult mr10 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff18)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_NoError);
		StaffCP.verifyUpdate(mr10, staff18, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case9:修改员工的状态");
		Staff s = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		Staff staff24 = BaseStaffTest.DataInput.getStaff();
		staff24.setID(s.getID());
		staff24.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staff24.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr16 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff24)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();//
		String json2 = mr16.getResponse().getContentAsString();
		JSONObject o2 = JSONObject.fromObject(json2);
		Integer status = JsonPath.read(o2, "$.object.status");
		if (status != EnumStatusStaff.ESS_Incumbent.getIndex()) {
			Assert.assertTrue(false, "查询出状态不为0的对象");
		}
		StaffCP.verifyUpdate(mr16, staff24, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case10:修改员工的状态，修改shopID为空");
		Staff s = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		Staff staff24 = BaseStaffTest.DataInput.getStaff();
		staff24.setID(s.getID());
		staff24.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staff24.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff24.setShopID(BaseAction.INVALID_ID);
		MvcResult mr17 = mvc.perform(BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff24).session((MockHttpSession) sessionBoss)).andExpect(status().isOk()).andDo(print()).andReturn();//
		Shared.checkJSONErrorCode(mr17, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testUpdateEx11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case11:新增staff用离职的staff手机号、微信、身份证，再修改这个staff时，修改成功");
		// 1、创建一个离职的staff
		Staff staffCreate81 = BaseStaffTest.DataInput.getStaff();
		staffCreate81.setStatus(1);
		staffCreate81.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		Map<String, Object> createParams81 = staffCreate81.getCreateParam(BaseBO.INVALID_CASE_ID, staffCreate81);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffCreated81 = (Staff) staffMapper.create(createParams81);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParams81.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		staffCreate81.setIgnoreIDInComparision(true);
		if (staffCreate81.compareTo(staffCreated81) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不一致");
		}
		// 2、用离职的staff的手机号、微信、身份证创建一个新的在职staff
		Staff staffCreate82 = BaseStaffTest.DataInput.getStaff();
		staffCreate82.setID(staffCreated81.getID());
		staffCreate82.setPhone(staffCreated81.getPhone());
		staffCreate82.setWeChat(staffCreated81.getWeChat());
		staffCreate82.setICID(staffCreated81.getICID());
		staffCreate82.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		Map<String, Object> createParams82 = staffCreate81.getCreateParam(BaseBO.INVALID_CASE_ID, staffCreate82);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffCreated82 = (Staff) staffMapper.create(createParams82);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParams82.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		staffCreate82.setIgnoreIDInComparision(true);
		if (staffCreate82.compareTo(staffCreated82) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不一致");
		}
		// 修改这个新的staff
		staffCreated82.setName("店员" + Shared.generateCompanyName(6));
		staffCreated82.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// 要传Int2，代表角色，否则出现“不能使用不存在的外键修改”错误
		MvcResult mr8 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staffCreated82)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr8);
		StaffCP.verifyUpdate(mr8, staffCreated82, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12:登录者修改自己的角色");
		Staff staff = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// updateEx.bx时，int2代表角色
		staff2.setID(staff.getID());

		MvcResult mr12 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff2)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr12, "登录者不能修改自己的角色！", "错误信息不正确");
	}

	@Test
	public void testUpdatEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13:自己修改自己的信息。查看会话是否也修改成功");
		// 创建staff
		Staff staff = BaseStaffTest.DataInput.getStaff();
		String Staff_Phone = Shared.getValidStaffPhone();
		staff.setPhone(Staff_Phone);
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setIsFirstTimeLogin(EnumBoolean.EB_Yes.getIndex());
		staff.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT + BaseAction.SHADOW));
		Staff staff2 = BaseStaffTest.createStaffForTest(mvc, mapBO, staff, "123456");
		staff2.setRoleID(EnumTypeRole.ETR_Boss.getIndex()); // DB层没有返回RoleID字段，为了通过下面测试，需要手动赋值

		// 修改自己的信息
		HttpSession session = Shared.getStaffLoginSession(mvc, staff2.getPhone(), "123456", Shared.DB_SN_Test);
		staff2.setName("收银员" + Shared.generateCompanyName(6));
		MvcResult mrl = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff2)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mrl);
		StaffCP.verifyUpdate(mrl, staff2, Shared.DBName_Test);
		// 检查会话
		Staff sessionStaff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		Assert.assertTrue(sessionStaff.compareTo(staff2) == 0, "会话中的数据未更新");

		// 删除新创建的staff
		session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		deleteStaff(staff2, EnumErrorCode.EC_NoError);
		//
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
	}

	@Test
	public void testUpdatEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14:修改他人的信息，查看自己会话是否有被更新。");
		// 创建一个staff
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		String Staff_Phone = Shared.getValidStaffPhone();
		staff2.setPhone(Staff_Phone);
		staff2.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		Staff s = BaseStaffTest.createStaffForTest(mvc, mapBO, staff2, "123456");
		s.setRoleID(EnumTypeRole.ETR_Boss.getIndex()); // DB层没有返回RoleID字段，为了通过下面测试，需要手动赋值
		// 修改新创建的staff信息
		Staff updateBefore = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
		s.setName("收银员" + Shared.generateCompanyName(6));
		MvcResult mrl = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, s)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mrl);
		StaffCP.verifyUpdate(mrl, s, Shared.DBName_Test);
		// 检查会话
		Staff updateAfter = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
		Assert.assertTrue(updateBefore.compareTo(updateAfter) == 0, "修改他人的信息，自己会话中的数据更新！");
		// 删除新创建的staff
		deleteStaff(s, EnumErrorCode.EC_NoError);//
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15:修改员工时，员工名称带有数字，修改失败");
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());// updateEx.bx时，RoleID代表角色
		staff2.setID(1);
		staff2.setName("收银员1号");

		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff2)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证:检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, Staff.FIELD_ERROR_name, "返回的结果与预期的结果不一致");
	}

	@Test
	public void testUpdateEx16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16:售前账号为删除状态，老板修改一个账号为售前账号相同号码（13888888888），修改失败");
		//
		Staff staffProSale = new Staff();
		staffProSale.setID(Shared.PreSaleID);
		BaseStaffTest.deleteStaffViaAction(staffProSale, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		String staffPhone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, staffPhone, Staff.FOR_CreateNewStaff);
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setReturnSalt(Staff.RETURN_SALT);
		staff.setPhone(staffPhone);
		staff.setPwdEncrypted(encrypt);
		Staff staffCreate = BaseStaffTest.createStaffViaAction(staff, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setID(staffCreate.getID());
		staff2.setRoleID(EnumTypeRole.ETR_Boss.getIndex());// updateEx.bx时，RoleID代表角色
		staff2.setPhone(BaseAction.ACCOUNT_Phone_PreSale);
		MvcResult mr = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff2)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, "不能修改成售前帐号的号码", BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
		//
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------case1: 根据ID查询-------------------");
		MvcResult mr2 = mvc.perform(//
				get("/staff/retrieve1Ex.bx?" + Staff.field.getFIELD_NAME_ID() + "=2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		JSONObject o = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		Integer i = JsonPath.read(o, "$.staff.ID");
		assertTrue(i == 2);

		System.out.println("-------------------case2: 根据phone查询-------------------");
		MvcResult mr3 = mvc.perform(//
				get("/staff/retrieve1Ex.bx?" + Staff.field.getFIELD_NAME_phone() + "=15016509167")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);

		System.out.println("----------------------case3: 根据状态码，查询出1个在职的门店客户-----------------");
		MvcResult mr4 = mvc.perform(//
				get("/staff/retrieve1Ex.bx?involvedResigned=0&phone=15016509167")// ...
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);

		System.out.println("----------------------case4: 根据状态码，查询出1个离职的门店客户-----------------");
		MvcResult mr5 = mvc.perform(//
				get("/staff/retrieve1Ex.bx?" + Staff.field.getFIELD_NAME_phone() + "=13196721886&involvedResigned=1")// ...&int1=1
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr5);
		//

		System.out.println("----------------------case5：根据状态码，电话号码，id，查询出在职的门店客户-----------------");
		MvcResult mr6 = mvc.perform(//
				get("/staff/retrieve1Ex.bx?")//
						.param(Staff.field.getFIELD_NAME_ID(), "2")//
						.param(Staff.field.getFIELD_NAME_phone(), "13144496272")//
						.param("iInvolvedResigned", "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr6);

		System.out.println("----------------------case6：根据电话号码，id，查询出门店客户-----------------");
		MvcResult mr7 = mvc.perform(//
				get("/staff/retrieve1Ex.bx?")//
						.param(Staff.field.getFIELD_NAME_ID(), "2")//
						.param(Staff.field.getFIELD_NAME_phone(), "13144496272")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr7);

		System.out.println("----------------------case7:根据状态码，id，查询出在职的门店客户-----------------");
		MvcResult mr8 = mvc.perform(//
				get("/staff/retrieve1Ex.bx?")//
						.param(Staff.field.getFIELD_NAME_ID(), "2")//
						.param("iInvolvedResigned", "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr8);

		System.out.println("----------------------case8：根据电话号码，状态码，查询出在职门店客户-----------------");
		MvcResult mr9 = mvc.perform(//
				get("/staff/retrieve1Ex.bx?")//
						.param(Staff.field.getFIELD_NAME_phone(), "13144496272")//
						.param("iInvolvedResigned", "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr9);

		System.out.println("----------------------case9：没有条件，全部使用默认参数-----------------");
		MvcResult mr10 = mvc.perform(//
				get("/staff/retrieve1Ex.bx?")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// Retrieve1只能单个查询，该查询返回结果集只能为1，应当报错(可能是黑客行为)
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_OtherError);

		System.out.println("----------------------case10:根据不存在的id-----------------");
		MvcResult mr11 = mvc.perform(//
				get("/staff/retrieve1Ex.bx?")//
						.param(Staff.field.getFIELD_NAME_ID(), "999999")//
						.param("iInvolvedResigned", "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_NoSuchData);
		Shared.checkJSONMsg(mr11, "不存在该用户", BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
		String json = mr11.getResponse().getContentAsString();
		Assert.assertTrue(JSONObject.fromObject(json).get("staff") == null, "老板不能查询到售前账号");

		System.out.println("-------------------case11:没有权限进行操作-------------------");
//		MvcResult mr12 = mvc.perform(//
//				get("/staff/retrieve1Ex.bx?")//
//						.param(Staff.field.getFIELD_NAME_ID(), "2")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieve1Ex12_1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12_1:老板用售前账号的电话号码R1不到售前账号");
		MvcResult mr = mvc.perform(//
				get("/staff/retrieve1Ex.bx?" + Staff.field.getFIELD_NAME_phone() + "=" + BaseAction.ACCOUNT_Phone_PreSale)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue(JSONObject.fromObject(json).get("staff") == null, "老板不能查询到售前账号");
	}

	@Test
	public void testRetrieve1Ex12_2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12_2:老板用售前账号的ID R1不到售前账号");
		MvcResult mr = mvc.perform(//
				get("/staff/retrieve1Ex.bx?" + Staff.field.getFIELD_NAME_ID() + "=" + Shared.PreSaleID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue(JSONObject.fromObject(json).get("staff") == null, "老板不能查询到售前账号");
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("------------------------case1:查询状态为0的。---------------");
		MvcResult mr = mvc.perform(post("/staff/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON)//
				.session(sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		//
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		List<Integer> statusList = JsonPath.read(o, "$.objectList[*].status");
		for (int i = 0; i < statusList.size(); i++) {
			if (statusList.get(i) != 0) {
				Assert.assertTrue(false, "查询出状态不为0的对象");
			}
		}

		System.out.println("------------------------case2:查询状态为1（离职）的。---------------");
		MvcResult mr2 = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		String json2 = mr2.getResponse().getContentAsString();
		JSONObject o2 = JSONObject.fromObject(json2);
		List<Integer> statusList2 = JsonPath.read(o2, "$.objectList[*].status");
		for (int i = 0; i < statusList2.size(); i++) {
			if (statusList2.get(i) != 1) {
				Assert.assertTrue(false, "查询出状态不为1的对象");
			}
		}

		Staff staff8 = BaseStaffTest.DataInput.getStaff();
		staff8.setPhone(Shared.getValidStaffPhone());
		Thread.sleep(1);
		staff8.setName("用户" + Shared.generateCompanyName(6));
		Thread.sleep(1);
		staff8.setWeChat("123" + System.currentTimeMillis() % 1000000);
		Thread.sleep(1);
		staff8.setICID(Shared.getValidICID());
		Thread.sleep(1);
		staff8.setSalt("123456");
		staff8.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		// 检验字段合法性
		String errMsg = staff.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(errMsg.equals(""), errMsg);
		Map<String, Object> createParam = staff8.getCreateParam(BaseBO.INVALID_CASE_ID, staff8);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffCreate = (Staff) staffMapper.create(createParam);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		staff8.setIgnoreIDInComparision(true);
		if (staff8.compareTo(staffCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		System.out.println("------------------------case3根据name 去查询---------------");
		MvcResult mr3 = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_queryKeyword(), "xxxfxx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		String json3 = mr3.getResponse().getContentAsString();
		JSONObject o3 = JSONObject.fromObject(json3);
		List<String> statusList3 = JsonPath.read(o3, "$.objectList[*].name");
		for (int i = 0; i < statusList3.size(); i++) {
			if (!statusList3.get(i).equals("xxxfxx")) {
				Assert.assertTrue(false, "查询出状态不为1的对象");
			}
		}

		System.out.println("------------------------case4:status传-1的时候可以查到所有的员工---------------");
		MvcResult mr4 = mvc.perform//
		(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr4);

		String json4 = mr4.getResponse().getContentAsString();
		JSONObject o4 = JSONObject.fromObject(json4);
		List<Integer> statusList4 = JsonPath.read(o4, "$.objectList[*].status");
		for (int i = 0; i < statusList4.size(); i++) {
			if (statusList4.get(i) == 2) {
				Assert.assertTrue(false, "查询出所有的员工");
			}
		}

		System.out.println("------------------------case5:status传1的时候用name值查询---------------");
		MvcResult mr5 = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), "1")//
						.param(Staff.field.getFIELD_NAME_queryKeyword(), "店员")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5);

		JSONObject o5 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		List<Integer> statusList5 = JsonPath.read(o5, "$.objectList[*].status");
		for (int i = 0; i < statusList5.size(); i++) {
			if (statusList5.get(i) == 1) {
				Assert.assertTrue(true);
			}
		}

		System.out.println("------------------------case6:status传-1的时候用name值查询，可以查到所有的相应员工---------------");
		MvcResult mr6 = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), "-1")//
						.param(Staff.field.getFIELD_NAME_queryKeyword(), "店员")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr6);

		JSONObject o6 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		List<String> statusList6 = JsonPath.read(o6, "$.objectList[*].name");
		System.out.println(statusList6);
		for (int i = 0; i < statusList6.size(); i++) {
			if (statusList6.contains("店员1号")) {
				Assert.assertTrue(true);
			}
		}

		System.out.println("------------------------case7:status传0的时候用name值查询，可以查到在职的相应员工---------------");
		MvcResult mr7 = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), "0")//
						.param(Staff.field.getFIELD_NAME_queryKeyword(), "店员")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr7);
		JSONObject o7 = JSONObject.fromObject(mr7.getResponse().getContentAsString());
		List<String> statusList7 = JsonPath.read(o7, "$.objectList[*].name");
		System.out.println(statusList7);
		for (int i = 0; i < statusList7.size(); i++) {
			if (statusList7.contains("店员1号")) {
				Assert.assertTrue(true);
			}
		}

		System.out.println("------------------------case8:status传0的时候用name值查询，可以查到在职的相应员工---------------");
		MvcResult mr8 = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), "0")//
						.param(Staff.field.getFIELD_NAME_queryKeyword(), "店员999999")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr8);

		JSONObject o1 = JSONObject.fromObject(mr8.getResponse().getContentAsString()); //
		List<?> list1 = JsonPath.read(o1, "$.objectList[*]");
		Assert.assertTrue(list1.size() == 0);

		System.out.println("------------------------case9：没有权限进行操作---------------");
//		MvcResult mr9 = mvc.perform(post("/staff/retrieveNEx.bx") //
//				.contentType(MediaType.APPLICATION_JSON)//
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		/*Assert.assertTrue( mr9.getResponse().getContentAsString().equals("") , "应该返回为空!" );*/
//		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_NoPermission);

		System.out.println("------------------------case10:status传0的时候用phone值查询，可以查到在职的相应员工---------------");
		MvcResult mr10 = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), "0")//
						.param(Staff.field.getFIELD_NAME_queryKeyword(), "13144496272")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr10);
		JSONObject o10 = JSONObject.fromObject(mr10.getResponse().getContentAsString());
		List<Staff> staffList10 = JsonPath.read(o10, "$.objectList");
		assertTrue(staffList10.size() > 0, "查询错误！");

		System.out.println("------------------------case11:status传-1的时候用phone值查询，可以查到相应员工---------------");
		MvcResult mr11 = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), "-1")//
						.param(Staff.field.getFIELD_NAME_queryKeyword(), "131444")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr11);

		JSONObject o11 = JSONObject.fromObject(mr11.getResponse().getContentAsString());
		List<Staff> staffList11 = JsonPath.read(o11, "$.objectList");
		assertTrue(staffList11.size() > 0, "查询错误！");

		System.out.println("------------------------case12:status传-1的时候用不存在的phone值查询，可以查到在职的相应员工---------------");
		MvcResult mr12 = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_status(), "-1")//
						.param(Staff.field.getFIELD_NAME_queryKeyword(), "13111111111")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr12);

		JSONObject o12 = JSONObject.fromObject(mr12.getResponse().getContentAsString());
		List<Staff> staffList12 = JsonPath.read(o12, "$.objectList");
		assertTrue(staffList12.size() == 0, "查询错误！");

		Shared.caseLog("case13:查看默认返回是否降序");
		MvcResult mr13 = mvc.perform(post("/staff/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON)//
				.session(sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr13);
		//
		JSONObject o13 = JSONObject.fromObject(mr13.getResponse().getContentAsString()); //
		List<BaseModel> bmList = new Staff().parseN(o13.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (int i = 1; i < bmList.size(); i++) {
			Assert.assertTrue(((Staff) bmList.get(i - 1)).getID() > ((Staff) bmList.get(i)).getID(), "数据返回错误（非降序）");
		}
	}

	@Test
	public void testRetrieveNEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14:iStatus传-1，iOperator为0的时候，可以查到除售前账号外的所有员工");
		MvcResult mr = mvc.perform(post("/staff/retrieveNEx.bx") //
				.param(Staff.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
				.param(Staff.field.getFIELD_NAME_pageSize(), String.valueOf(Shared.PAGE_SIZE_MAX))//
				.contentType(MediaType.APPLICATION_JSON)//
				.session(sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<BaseModel> bmList = new Staff().parseN(o.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (BaseModel bm : bmList) {
			Staff s = (Staff) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(s.getPhone())), "RN Staff错误，不应该查询出售前账号。Phone=" + s.getPhone());
		}
	}

	@Test
	public void testRetrieveNEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15:售前为未删除时，iStatus传0，iOperator为0的时候，可以查到除售前账号外的所有未删除的员工");
		MvcResult mr = mvc.perform(post("/staff/retrieveNEx.bx") //
				.param(Staff.field.getFIELD_NAME_status(), String.valueOf(EnumStatusStaff.ESS_Incumbent.getIndex()))//
				.param(Staff.field.getFIELD_NAME_pageSize(), String.valueOf(Shared.PAGE_SIZE_MAX))//
				.contentType(MediaType.APPLICATION_JSON)//
				.session(sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<BaseModel> bmList = new Staff().parseN(o.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (BaseModel bm : bmList) {
			Staff s = (Staff) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(s.getPhone())), "RN Staff错误，不应该查询出售前账号。Phone=" + s.getPhone());
		}
	}

	@Test
	public void testRetrieveNEx16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16:售前为删除时，iStatus传1，iOperator为0的时候，可以查到除售前外账号的所有删除的员工");
		Staff staff = new Staff();
		staff.setID(Shared.PreSaleID);
		BaseStaffTest.deleteStaffViaAction(staff, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		MvcResult mr = mvc.perform(post("/staff/retrieveNEx.bx") //
				.param(Staff.field.getFIELD_NAME_status(), String.valueOf(EnumStatusStaff.ESS_Resigned.getIndex()))//
				.param(Staff.field.getFIELD_NAME_pageSize(), String.valueOf(Shared.PAGE_SIZE_MAX))//
				.contentType(MediaType.APPLICATION_JSON)//
				.session(sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<BaseModel> bmList = new Staff().parseN(o.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (BaseModel bm : bmList) {
			Staff s = (Staff) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(s.getPhone())), "RN Staff错误，不应该查询出售前账号。Phone=" + s.getPhone());
		}
		//
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
	}

	@Test
	public void testRetrieveNEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17:售前为未删除时，iStatus传0，iOperator为1的时候，可以查到所有未删除的员工");
		MvcResult mr = mvc.perform(post("/staff/retrieveNEx.bx") //
				.param(Staff.field.getFIELD_NAME_status(), String.valueOf(EnumStatusStaff.ESS_Incumbent.getIndex()))//
				.param(Staff.field.getFIELD_NAME_operator(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))// TODO 这字段信息不应该传进去的，而是应该在登录信息获取的
				.param(Staff.field.getFIELD_NAME_pageSize(), String.valueOf(Shared.PAGE_SIZE_MAX))//
				.contentType(MediaType.APPLICATION_JSON)//
				.session(sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<BaseModel> bmList = new Staff().parseN(o.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		boolean existPreSale = false;
		for (BaseModel bm : bmList) {
			Staff s = (Staff) bm;
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(s.getPhone())) {
				existPreSale = true;
				break;
			}
		}
		assertTrue(existPreSale, "RN Staff错误，应该查询出售前账号。");
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常删除Staff");
		//
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		Map<String, Object> createParams = staff.getCreateParam(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff s = (Staff) staffMapper.create(createParams);
		staff.setIgnoreIDInComparision(true);
		if (staff.compareTo(s) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		//
		MvcResult mr2 = mvc.perform(//
				get("/staff/deleteEx.bx?" + Staff.field.getFIELD_NAME_ID() + "=" + s.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		//
		Shared.checkJSONErrorCode(mr2);
		Staff staffClone = (Staff) s.clone();
		StaffCP.verifyDelete(staffClone, staffBO, staffRoleBO, Shared.DBName_Test);

	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2 删除staff。查看人员权限缓存是否正确");
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		MvcResult mvcResult2 = BaseStaffTest.getToken(sessionBoss, mvc, staff2.getPhone(), Staff.FOR_CreateNewStaff);
		String encrypt2 = Shared.encrypt(mvcResult2, Shared.PASSWORD_DEFAULT);

		staff2.setPwdEncrypted(encrypt2);
		staff2.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		MvcResult mvcResult3 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff2)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mvcResult3);

		checkStaffPermission(mvcResult3);

		JSONObject o2 = JSONObject.fromObject(mvcResult3.getResponse().getContentAsString());
		JSONObject jsonObject2 = o2.getJSONObject(BaseAction.KEY_Object);
		int staffID2 = jsonObject2.getInt(Staff.field.getFIELD_NAME_ID());

		MvcResult mr4 = mvc.perform(//
				get("/staff/deleteEx.bx?" + Staff.field.getFIELD_NAME_ID() + "=" + staffID2)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr4);
		Staff staffClone = staff2;
		staffClone.setID(staffID2);
		StaffCP.verifyDelete(staffClone, staffBO, staffRoleBO, Shared.DBName_Test);

		ErrorInfo ecOut = new ErrorInfo();
		StaffPermissionCache spc = (StaffPermissionCache) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_StaffPermission);
		StaffPermission sp = spc.read1(staffID2, StaffBO.SP_Staff_Retrieve1, ecOut);
		if (sp != null) {
			Assert.assertTrue(false, "门店人员的权限缓存并没有更新成功。");
		}
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3 删除售前账号，无依赖，删除成功！");
		Staff staff4 = BaseStaffTest.DataInput.getStaff();
		staff4.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		Map<String, Object> createParams4 = staff4.getCreateParam(BaseBO.INVALID_CASE_ID, staff4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff s4 = (Staff) staffMapper.create(createParams4);
		staff4.setIgnoreIDInComparision(true);
		if (staff4.compareTo(s4) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParams4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		//
		MvcResult m4 = mvc.perform(//
				get("/staff/deleteEx.bx?" + Staff.field.getFIELD_NAME_ID() + "=" + s4.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(m4);
		Staff staffClone = (Staff) s4.clone();
		StaffCP.verifyDelete(staffClone, staffBO, staffRoleBO, Shared.DBName_Test);

		ErrorInfo ecOut = new ErrorInfo();
		StaffPermissionCache spc4 = (StaffPermissionCache) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_StaffPermission);
		StaffPermission sp4 = spc4.read1(s4.getID(), StaffBO.SP_Staff_Retrieve1, ecOut);
		if (sp4 != null) {
			Assert.assertTrue(false, "门店人员的权限缓存并没有更新成功。");
		}
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4: 删除售前账号，有依赖，删除成功！");
		Staff staff5 = BaseStaffTest.DataInput.getStaff();
		staff5.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		Map<String, Object> createParams5 = staff5.getCreateParam(BaseBO.INVALID_CASE_ID, staff5);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff s5 = (Staff) staffMapper.create(createParams5);
		staff5.setIgnoreIDInComparision(true);
		if (staff5.compareTo(s5) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParams5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		//
		// 创建促销，绑定创建的员工
		Promotion p5 = BaseStaffTest.DataInput.getPromotion();
		p5.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p5.setExcecutionDiscount(-1);
		p5.setStaff(s5.getID());
		Promotion pCreate5 = createPromotion(p5);
		//
		MvcResult m5 = mvc.perform(//
				get("/staff/deleteEx.bx?" + Staff.field.getFIELD_NAME_ID() + "=" + s5.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(m5);
		Staff staffClone = (Staff) s5.clone();
		StaffCP.verifyDelete(staffClone, staffBO, staffRoleBO, Shared.DBName_Test);

		ErrorInfo ecOut = new ErrorInfo();
		StaffPermissionCache spc5 = (StaffPermissionCache) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_StaffPermission);
		StaffPermission sp5 = spc5.read1(s5.getID(), StaffBO.SP_Staff_Retrieve1, ecOut);
		if (sp5 != null) {
			Assert.assertTrue(false, "门店人员的权限缓存并没有更新成功。");
		}
		//
		deletePromotion(pCreate5);
	}

	protected void deletePromotion(Promotion pCreate) {
		Map<String, Object> paramsDelete = pCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionMapper.delete(paramsDelete);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramsR1 = pCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, pCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion pR1 = (Promotion) promotionMapper.retrieve1(paramsR1);
		//
		Assert.assertTrue(
				pR1 != null && pR1.getStatus() == Promotion.EnumStatusPromotion.ESP_Deleted.getIndex() && EnumErrorCode.values()[Integer.parseInt(paramsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证相应的从表的数据有没有删除成功
		PromotionScope ps = new PromotionScope();
		ps.setPromotionID(pCreate.getID());
		Map<String, Object> psParamsRN = ps.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ps);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = promotionScopeMapper.retrieveN(psParamsRN);
		//
		Assert.assertTrue(list.size() == 0 && EnumErrorCode.values()[Integer.parseInt(psParamsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				psParamsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("删除促销成功");
	}

	protected Promotion createPromotion(Promotion p) {
		String err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramsCreate = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion pCreate = (Promotion) promotionMapper.create(paramsCreate);
		//
		if (pCreate != null) { // 创建成功
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			err = pCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			p.setIgnoreIDInComparision(true);
			if (p.compareTo(pCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			//
			System.out.println("创建对象成功: " + pCreate);
		} else { // 创建失败
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
					paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			System.out.println("创建对象失败: " + paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return pCreate;
	}

	protected void checkStaffPermission(MvcResult mr) throws UnsupportedEncodingException {
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONObject jsonObject = o.getJSONObject(BaseAction.KEY_Object);
		int staffID = jsonObject.getInt(Staff.field.getFIELD_NAME_ID());
		StaffPermissionCache spc = (StaffPermissionCache) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_StaffPermission);
		ErrorInfo ecOut = new ErrorInfo();
		StaffPermission sp = spc.read1(staffID, StaffBO.SP_Staff_Retrieve1, ecOut);
		if (sp == null) {
			Assert.assertTrue(false, "缓存中并没有相关staff的权限");
		}
	}

	@Test
	protected void testLoginEx1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1：密码正确且密码格式正确");
		final String Staff_Phone = Shared.PhoneOfBoss;

		Staff s = new Staff();
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(mvc, Staff_Phone);

		String json = ret.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();

		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

		final String pwd = Shared.PASSWORD_DEFAULT;
		// ..加密密码
		String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
		s.setPwdEncrypted(pwdEncrypted);
		
		MvcResult mr = BaseStaffTest.login(mvc, ret.getRequest().getSession(), Shared.DB_SN_Test, Staff_Phone, pwdEncrypted, Staff.NOT_INVOLVE_RESIGNED);

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);
	}

	@Test
	protected void testLoginEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2：密码正确但密码格式不正确");
//		final String Staff_Phone2 = Shared.PhoneOfManager;
//
//		Staff s2 = new Staff();
//		// ..拿到公钥
//		MvcResult ret2 = BaseStaffTest.getToken(mvc, Staff_Phone2);
//		String json2 = ret2.getResponse().getContentAsString();
//		JSONObject o2 = JSONObject.fromObject(json2);
//		String modulus2 = JsonPath.read(o2, "$.rsa.modulus");
//		String exponent2 = JsonPath.read(o2, "$.rsa.exponent");
//		modulus2 = new BigInteger(modulus2, 16).toString();
//		exponent2 = new BigInteger(exponent2, 16).toString();
//
//		RSAPublicKey publicKey2 = RSAUtils.getPublicKey(modulus2, exponent2);
//
//		final String pwd2 = " asd4#@56";
//		// ..加密密码
//		String pwdEncrypted2 = RSAUtils.encryptByPublicKey(pwd2, publicKey2);
//		s2.setPwdEncrypted(pwdEncrypted2);
//
//		MvcResult mr2 = BaseStaffTest.login(mvc, ret2.getRequest().getSession(), Shared.DB_SN_Test, Staff_Phone2, pwdEncrypted2, Staff.NOT_INVOLVE_RESIGNED);
//
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	protected void testLoginEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3：密码不正确 ");
		final String Staff_Phone3 = Shared.PhoneOfBoss;

		Staff s3 = new Staff();
		// ..拿到公钥
		MvcResult ret3 = BaseStaffTest.getToken(mvc, Staff_Phone3);
		
		String json3 = ret3.getResponse().getContentAsString();
		JSONObject o3 = JSONObject.fromObject(json3);
		String modulus3 = JsonPath.read(o3, "$.rsa.modulus");
		String exponent3 = JsonPath.read(o3, "$.rsa.exponent");
		modulus3 = new BigInteger(modulus3, 16).toString();
		exponent3 = new BigInteger(exponent3, 16).toString();

		RSAPublicKey publicKey3 = RSAUtils.getPublicKey(modulus3, exponent3);

		final String pwd3 = "123456";
		// ..加密密码
		String pwdEncrypted3 = RSAUtils.encryptByPublicKey(pwd3, publicKey3);
		s3.setPwdEncrypted(pwdEncrypted3);

		MvcResult mr3 = BaseStaffTest.login(mvc, ret3.getRequest().getSession(), Shared.DB_SN_Test, Staff_Phone3, pwdEncrypted3, Staff.NOT_INVOLVE_RESIGNED);

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	protected void testLoginEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4：离职员工登录，返回EC_Hack");
		final String Staff_Phone4 = "13196721886";

		Staff s4 = new Staff();
		// ..拿到公钥
		MvcResult ret4 = BaseStaffTest.getToken(mvc, Staff_Phone4);
		
		String json4 = ret4.getResponse().getContentAsString();
		JSONObject o4 = JSONObject.fromObject(json4);
		String modulus4 = JsonPath.read(o4, "$.rsa.modulus");
		String exponent4 = JsonPath.read(o4, "$.rsa.exponent");
		modulus4 = new BigInteger(modulus4, 16).toString();
		exponent4 = new BigInteger(exponent4, 16).toString();

		RSAPublicKey publicKey4 = RSAUtils.getPublicKey(modulus4, exponent4);

		final String pwd4 = "123456";
		// ..加密密码
		String pwdEncrypted4 = RSAUtils.encryptByPublicKey(pwd4, publicKey4);
		s4.setPwdEncrypted(pwdEncrypted4);

		MvcResult mr4 = BaseStaffTest.login(mvc, ret4.getRequest().getSession(), Shared.DB_SN_Test, Staff_Phone4, pwdEncrypted4, Staff.INVOLVE_RESIGNED);

		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_Hack);
	}

	@Test
	protected void testLoginEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5: 员工被删除后是否还能进行登录");
		// 1.创建一个staff
		Staff staff5 = BaseStaffTest.DataInput.getStaff();
		MvcResult ret5 = BaseStaffTest.getToken(mvc, staff5.getPhone());
		String encrypt = Shared.encrypt(ret5, Shared.PASSWORD_DEFAULT);

		staff5.setPwdEncrypted(encrypt);
		staff5.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		MvcResult mr5 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff5)//
						.session((MockHttpSession) Shared.getPosLoginSession(mvc, 1))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr5);

		String json5 = mr5.getResponse().getContentAsString();
		JSONObject o5 = JSONObject.fromObject(json5);
		int id5 = JsonPath.read(o5, "$.object.ID");
		// 检查缓存，普存是否相应的增加
		ErrorInfo ecOut = new ErrorInfo();
		assertTrue(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Staff).read1(id5, staff.getID(), ecOut, Shared.DBName_Test) != null, "缓存中并没有这条数据！");

		// 2.使用该staff进行登录，首次登陆跳转修改密码页面
		String phone6 = JsonPath.read(o5, "$.object.phone");
		MvcResult ret6 = mvc.perform(post("/staff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(Staff.field.getFIELD_NAME_phone(), phone6)).andExpect(status().isOk()).andDo(print()).andReturn();

		String pwdEncrypted6 = Shared.encrypt(ret6, Shared.PASSWORD_DEFAULT);
		MvcResult mr6 = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), phone6)//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted6)//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.param(Staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.session((MockHttpSession) ret6.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_Redirect);

		// 3.修改密码
		HttpSession session = mr6.getRequest().getSession();
		MvcResult ret = BaseStaffTest.getToken(session, mvc, phone6, Staff.FOR_ModifyPassword);
		//
		final String pwdNew = "123456";
		String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew = Shared.encrypt(ret, pwdNew);
		//
		String Key_OldPasword = "sPasswordEncryptedOld";
		String Key_NewPasword = "sPasswordEncryptedNew";
		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), phone6) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_NoError);
		// 验证是否返回了盐值
		BaseStaffTest.checkIfReturnSalt(m);
		StaffCP.verifyResetMyPassword(m, pwdNew, staffBO, Shared.DBName_Test);

		// 4.使用老板账号登陆，删除该员工
		MvcResult mr7 = mvc.perform(//
				get("/staff/deleteEx.bx?" + Staff.field.getFIELD_NAME_ID() + "=" + id5).contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getPosLoginSession(mvc, 1))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())// SS
				.andReturn();//
		//
		Shared.checkJSONErrorCode(mr7);

		// 5.删除后再使用该员工进行登录
		MvcResult ret8 = mvc.perform(post("/staff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(Staff.field.getFIELD_NAME_phone(), phone6)).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		String pwdEncrypted8 = Shared.encrypt(ret8, pwdNew);
		//
		MvcResult mr8 = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.param(Staff.field.getFIELD_NAME_phone(), phone6)//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted8)//
						.session((MockHttpSession) ret8.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//
		//
		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	protected void testLoginEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6: 新员工模拟在pos端首次进行登录");
		Staff staff6 = BaseStaffTest.DataInput.getStaff();
		//
		MvcResult ret6 = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), staff6.getPhone())//
						.session((MockHttpSession) Shared.getPosLoginSession(mvc, 1))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(ret6);
		String encrypt6 = Shared.encrypt(ret6, Shared.PASSWORD_DEFAULT);

		staff6.setPwdEncrypted(encrypt6);
		staff6.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		//
		MvcResult mr6 = mvc.perform(BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff6)//
				.session((MockHttpSession) ret6.getRequest().getSession())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoError);

		JSONObject o6 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		String phone = JsonPath.read(o6, "$.object.phone");
		Assert.assertTrue(staff6.getPhone().equals(phone), "返回的手机号不正确");

		MvcResult ret7 = BaseStaffTest.getToken(mvc, staff6.getPhone());

		String pwdEncrypted7 = Shared.encrypt(ret7, Shared.PASSWORD_DEFAULT);

		MvcResult mr7 = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.param(Staff.field.getFIELD_NAME_phone(), staff6.getPhone())//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted7)//
						.param(Staff.field.getFIELD_NAME_isLoginFromPos(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.session((MockHttpSession) ret7.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//

		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_Redirect);
	}

	@Test
	protected void testLoginEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(" case7: 老员工模拟在pos端进行登录");

		String Staff_Phone = Shared.PhoneOfBoss;

		MvcResult ret7 = BaseStaffTest.getToken(mvc, Staff_Phone);
		String encrypt7 = Shared.encrypt(ret7, Shared.PASSWORD_DEFAULT);

		MvcResult mr7 = BaseStaffTest.login(mvc, ret7.getRequest().getSession(), Shared.DB_SN_Test, Staff_Phone, encrypt7, Staff.NOT_INVOLVE_RESIGNED);

		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoError);
	}
	
	@Test
	protected void testLoginEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8：用户登录超过当日限制次数");
		//
		//
		ErrorInfo ec = new ErrorInfo();
		BxConfigGeneral bxConfigGeneral = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.MaxLoginCountIn1Day, STAFF_ID4, ec, BaseAction.DBName_Public);
		String oldMaxLoginCountIn1Day = bxConfigGeneral.getValue();
		//
		int newMaxLoginCountIn1Day = 5;
		BxConfigGeneral newBxConfigGeneral = (BxConfigGeneral) bxConfigGeneral.clone();
		newBxConfigGeneral.setValue(String.valueOf(newMaxLoginCountIn1Day));
		CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).write1(newBxConfigGeneral, BaseAction.DBName_Public, STAFF_ID4);

		final String Staff_Phone = Shared.PhoneOfAnotherCashier;
		for (int i = 0; i < newMaxLoginCountIn1Day - 1; i++) {
			login(Staff_Phone);
		}

		Staff s = new Staff();
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(mvc, Staff_Phone);

		String json = ret.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();

		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

		final String pwd = Shared.PASSWORD_DEFAULT;
		// ..加密密码
		String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
		s.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = BaseStaffTest.login(mvc, ret.getRequest().getSession(), Shared.DB_SN_Test, Staff_Phone, pwdEncrypted, Staff.NOT_INVOLVE_RESIGNED);

		bxConfigGeneral.setValue(oldMaxLoginCountIn1Day);
		CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).write1(bxConfigGeneral, BaseAction.DBName_Public, STAFF_ID4);

		Assert.assertTrue(StringUtils.isEmpty(mr.getResponse().getContentAsString()), "没有正常限制用户登录次数");
	}
	
	
	@Test
	protected void testLoginEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case9：老板登录后，需要返回公司Logo的URL");
		final String Staff_Phone = Shared.PhoneOfBoss;

		Staff s = new Staff();
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(mvc, Staff_Phone);

		String json = ret.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();

		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

		final String pwd = Shared.PASSWORD_DEFAULT;
		// ..加密密码
		String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
		s.setPwdEncrypted(pwdEncrypted);
		//
		MvcResult mr = BaseStaffTest.login(mvc, ret.getRequest().getSession(), Shared.DB_SN_Test, Staff_Phone, pwdEncrypted, Staff.NOT_INVOLVE_RESIGNED);
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);
		HttpSession session = ret.getRequest().getSession();
		Company company = (Company) session.getAttribute(EnumSession.SESSION_Company.getName());
		Assert.assertTrue(company.getLogo() != null, "商家登录后，需要返回公司Logo的URL");
	}

	private void login(String phone) throws Exception {
		Staff s = new Staff();
		// ..拿到公钥
		MvcResult ret = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), phone))//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();

		String json = ret.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();

		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

		final String pwd = Shared.PASSWORD_DEFAULT;
		// ..加密密码
		String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
		s.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.param(Staff.field.getFIELD_NAME_phone(), phone)//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
						.session((MockHttpSession) ret.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);
	}


	@Test
	public void testResetMyPasswordEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case1 ：自己修改自己的密码，正确修改");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew = Shared.encrypt(ret, BaseStaffTest.PASSWORD_123456);

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_NoError);
		// 验证是否返回了F_RoleID字段
		JSONObject object = JSONObject.fromObject(m.getResponse().getContentAsString());
		Staff staff = (Staff) new Staff().parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff.getRoleID() == EnumTypeRole.ETR_ShopManager.getIndex(), BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
		// 验证是否返回了盐值
		BaseStaffTest.checkIfReturnSalt(m);
		StaffCP.verifyResetMyPassword(m, BaseStaffTest.PASSWORD_123456, staffBO, Shared.DBName_Test);
		//
		// 将该员工的密码修改回原来的.以免其他测试失败
		MvcResult result = (MvcResult) mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andReturn();

		Shared.checkJSONErrorCode(result);
		String encrypt = Shared.encrypt(result, BaseStaffTest.PASSWORD_123456);
		//
		MvcResult result1 = (MvcResult) mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager)//
						.param(Staff.field.getFIELD_NAME_salt(), "")//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), encrypt)//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.session((MockHttpSession) result.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andReturn();//
		Shared.checkJSONErrorCode(result1);
		//
		HttpSession seesion = result1.getRequest().getSession();
		MvcResult ret3 = BaseStaffTest.getToken(seesion, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld3 = Shared.encrypt(ret3, BaseStaffTest.PASSWORD_123456);
		String sPasswordEncryptedNew3 = Shared.encrypt(ret3, Shared.PASSWORD_DEFAULT);
		//
		MvcResult m3 = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) seesion).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld3) //
				.param(Key_NewPasword, sPasswordEncryptedNew3) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m3, EnumErrorCode.EC_NoError);
		JSONObject object1 = JSONObject.fromObject(m3.getResponse().getContentAsString());
		Staff staff1 = (Staff) new Staff().parse1(object1.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff1.getRoleID() == EnumTypeRole.ETR_ShopManager.getIndex(), BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
		// 验证是否返回了盐值
		BaseStaffTest.checkIfReturnSalt(m3);
		StaffCP.verifyResetMyPassword(m3, Shared.PASSWORD_DEFAULT, staffBO, Shared.DBName_Test);
	}

	@Test
	public void testResetMyPasswordEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case2: 旧密码和新密码一样");
		//
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		MvcResult ret2 = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld2 = Shared.encrypt(ret2, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew2 = Shared.encrypt(ret2, Shared.PASSWORD_DEFAULT);
		//
		MvcResult m2 = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld2) //
				.param(Key_NewPasword, sPasswordEncryptedNew2) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m2, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testResetMyPasswordEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:修改的不是自己的密码");
		//
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		MvcResult ret3 = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedOld3 = Shared.encrypt(ret3, BaseStaffTest.PASSWORD_123456);
		String sPasswordEncryptedNew3 = Shared.encrypt(ret3, Shared.PASSWORD_DEFAULT);
		//
		MvcResult m4 = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Key_OldPasword, sPasswordEncryptedOld3) //
				.param(Key_NewPasword, sPasswordEncryptedNew3) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Assert.assertTrue("".equals(m4.getResponse().getContentAsString()), "使用修改自己密码的接口，传入他人的手机号码进行修改，接口未检测到");
	}

	@Test
	public void testResetMyPasswordEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:获取原密码失败");
		//
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		MvcResult ret3 = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew3 = Shared.encrypt(ret3, Shared.PASSWORD_DEFAULT);
		//
		MvcResult m5 = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_NewPasword, sPasswordEncryptedNew3) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Assert.assertTrue("".equals(m5.getResponse().getContentAsString()), "未检测到传递旧密码");
	}

	@Test
	public void testResetMyPasswordEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:获取新密码失败");
		//
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		MvcResult ret3 = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedOld3 = Shared.encrypt(ret3, BaseStaffTest.PASSWORD_123456);
		//
		MvcResult m6 = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld3) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Assert.assertTrue("".equals(m6.getResponse().getContentAsString()), "未检测到传递新密码");
	}

	@Test
	public void testResetMyPasswordEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:解析旧密码失败");
		//
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		MvcResult ret3 = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew3 = Shared.encrypt(ret3, Shared.PASSWORD_DEFAULT);
		//
		MvcResult m7 = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_NewPasword, sPasswordEncryptedNew3) //
				.param(Key_OldPasword, BaseStaffTest.PASSWORD_123456) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(m7, EnumErrorCode.EC_Hack);
	}

	@Test
	public void testResetMyPasswordEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:解析新密码失败");
		//
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		MvcResult ret3 = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew3 = Shared.encrypt(ret3, Shared.PASSWORD_DEFAULT);
		//
		MvcResult m8 = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_NewPasword, "123456") //
				.param(Key_OldPasword, sPasswordEncryptedNew3) // 密码是000000
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(m8, EnumErrorCode.EC_Hack);
	}

	@Test
	public void testResetMyPasswordEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:原密码不正确");
		//
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		MvcResult ret9 = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld9 = Shared.encrypt(ret9, BaseStaffTest.PASSWORD_123456);
		String sPasswordEncryptedNew9 = Shared.encrypt(ret9, Shared.PASSWORD_DEFAULT);
		//
		MvcResult m9 = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_NewPasword, sPasswordEncryptedNew9) //
				.param(Key_OldPasword, sPasswordEncryptedOld9) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(m9, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testResetMyPasswordEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9:新密码的格式不正确");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		MvcResult ret10 = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld10 = Shared.encrypt(ret10, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew10 = Shared.encrypt(ret10, "0000");
		//
		MvcResult m10 = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_NewPasword, sPasswordEncryptedNew10) //
				.param(Key_OldPasword, sPasswordEncryptedOld10) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(m10, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testResetMyPasswordEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case10 ：修改自己密码时，新密码有中文,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew = Shared.encrypt(ret, "!@#123中文Abc$%^");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case11 ：修改自己密码时，新密码首尾有中文,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew = Shared.encrypt(ret, "中!@#123Abc$%^文");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case12 ：修改自己密码时，新密码首尾有空格,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew = Shared.encrypt(ret, " !@#123Abc$%^ ");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case13 ：修改自己密码时，新密码全部是空格,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew = Shared.encrypt(ret, "                ");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case14 ：修改自己密码时，新密码为空串,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew = Shared.encrypt(ret, "");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	// CASE15运行到Shared.encrypt(ret, null)语句时报空指针错误
	// @Test
	// public void testResetMyPasswordEx15() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog(" case15 ：修改自己密码时，新密码为null,修改失败");
	// HttpSession session = Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfAssistManager);
	// // ..拿到公钥
	// MvcResult ret = getToken(session, Shared.PhoneOfAssistManager,
	// Staff.FOR_ModifyPassword);
	// // ..加密密码
	// String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
	// String sPasswordEncryptedNew = Shared.encrypt(ret, null);
	//
	// MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
	// .session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(),
	// Shared.PhoneOfAssistManager) //
	// .param(Key_OldPasword, sPasswordEncryptedOld) //
	// .param(Key_NewPasword, sPasswordEncryptedNew) //
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
	// Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	// }

	@Test
	public void testResetMyPasswordEx16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case16 ：修改自己密码时，新密码小于6位,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew = Shared.encrypt(ret, "12Ab!");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case17 ：修改自己密码时，新密码大于16位,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew = Shared.encrypt(ret, "1234567890Abcd!@#$%^");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case18 ：修改自己密码时，旧密码有中文,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, "!@#123中文Abc$%^");
		String sPasswordEncryptedNew = Shared.encrypt(ret, "123456");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case19 ：修改自己密码时，旧密码首尾有中文,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, "中!@#123Abc$%^文");
		String sPasswordEncryptedNew = Shared.encrypt(ret, "123456");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case20 ：修改自己密码时，旧密码首尾有空格,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, " !@#123Abc$%^ ");
		String sPasswordEncryptedNew = Shared.encrypt(ret, "123456");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx21() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case21 ：修改自己密码时，旧密码全部是空格,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, "                ");
		String sPasswordEncryptedNew = Shared.encrypt(ret, "123456");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx22() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case22 ：修改自己密码时，旧密码为空串,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, "");
		String sPasswordEncryptedNew = Shared.encrypt(ret, "123456");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	// CASE23运行到Shared.encrypt(ret, null)语句时报空指针错误
	// @Test
	// public void testResetMyPasswordEx23() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog(" case23 ：修改自己密码时，旧密码为null,修改失败");
	// HttpSession session = Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfAssistManager);
	// // ..拿到公钥
	// MvcResult ret = getToken(session, Shared.PhoneOfAssistManager,
	// Staff.FOR_ModifyPassword);
	// // ..加密密码
	// String sPasswordEncryptedOld = Shared.encrypt(ret, null);
	// String sPasswordEncryptedNew = Shared.encrypt(ret, "123456");
	//
	// MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
	// .session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(),
	// Shared.PhoneOfAssistManager) //
	// .param(Key_OldPasword, sPasswordEncryptedOld) //
	// .param(Key_NewPasword, sPasswordEncryptedNew) //
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
	// Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	// }

	@Test
	public void testResetMyPasswordEx24() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case24 ：修改自己密码时，旧密码小于6位,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, "12Ab!");
		String sPasswordEncryptedNew = Shared.encrypt(ret, "123456");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx25() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case25 ：修改自己密码时，旧密码大于16位,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedOld = Shared.encrypt(ret, "1234567890Abcd!@#$%^");
		String sPasswordEncryptedNew = Shared.encrypt(ret, "123456");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(m, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(m, "修改密码失败，请重试", "返回的错误消息不正确!");
	}

	@Test
	public void testResetMyPasswordEx26() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case26 ：老板修改他人密码时无需旧密码，新密码有中文,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedNew = Shared.encrypt(ret, "!@#123中文Abc$%^");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_isResetOtherPassword, String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = m.getResponse().getContentAsString();
		Assert.assertEquals(json, "");
	}

	@Test
	public void testResetMyPasswordEx27() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case27 ：老板修改他人密码时无需旧密码，新密码首尾有中文,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedNew = Shared.encrypt(ret, "中!@#123Abc$%^文");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_isResetOtherPassword, String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = m.getResponse().getContentAsString();
		Assert.assertEquals(json, "");
	}

	@Test
	public void testResetMyPasswordEx28() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case28 ：老板修改他人密码时无需旧密码，新密码首尾有空格,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedNew = Shared.encrypt(ret, " !@#123Abc$%^ ");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_isResetOtherPassword, String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = m.getResponse().getContentAsString();
		Assert.assertEquals(json, "");
	}

	@Test
	public void testResetMyPasswordEx29() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case29 ：老板修改他人密码时无需旧密码，新密码全部是空格,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedNew = Shared.encrypt(ret, "                ");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_isResetOtherPassword, String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = m.getResponse().getContentAsString();
		Assert.assertEquals(json, "");
	}

	@Test
	public void testResetMyPasswordEx30() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case30 ：老板修改他人密码时无需旧密码，新密码为空串,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedNew = Shared.encrypt(ret, "");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_isResetOtherPassword, String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = m.getResponse().getContentAsString();
		Assert.assertEquals(json, "");
	}

	// CASE31运行到Shared.encrypt(ret, null)语句时报空指针错误
	// @Test
	// public void testResetMyPasswordEx31() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog(" case31 ：老板修改他人密码时无需旧密码，新密码为null,修改失败");
	// HttpSession session = Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfAssistManager);
	// // ..拿到公钥
	// MvcResult ret = getToken(session, Shared.PhoneOfAssistManager,
	// Staff.FOR_ModifyPassword);
	// // ..加密密码
	// String sPasswordEncryptedNew = Shared.encrypt(ret, null);
	//
	// MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
	// .session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(),
	// Shared.PhoneOfAssistManager) //
	// .param(Key_isResetOtherPassword,
	// String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
	// .param(Key_NewPasword, sPasswordEncryptedNew) //
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// String json = m.getResponse().getContentAsString();
	// Assert.assertEquals(json, "");
	// }

	@Test
	public void testResetMyPasswordEx32() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case32 ：老板修改他人密码时无需旧密码，新密码小于6位,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedNew = Shared.encrypt(ret, "12Ab!");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_isResetOtherPassword, String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = m.getResponse().getContentAsString();
		Assert.assertEquals(json, "");
	}

	@Test
	public void testResetMyPasswordEx33() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case33 ：老板修改他人密码时无需旧密码，新密码大于16位,修改失败");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
		// ..拿到公钥
		MvcResult ret = BaseStaffTest.getToken(session, mvc, Shared.PhoneOfAssistManager, Staff.FOR_ModifyPassword);
		// ..加密密码
		String sPasswordEncryptedNew = Shared.encrypt(ret, "1234567890Abcd!@#$%^");

		MvcResult m = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session).param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfAssistManager) //
				.param(Key_isResetOtherPassword, String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = m.getResponse().getContentAsString();
		Assert.assertEquals(json, "");
	}

	@Test
	public void testResetMyPasswordEx34_1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case34_1：老板首次登录，修改自己密码，售前账号会被删除");
		// 保证售前是未删除的
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
		//
		String staffPhone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, staffPhone, Staff.FOR_CreateNewStaff);
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		//
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setReturnSalt(Staff.RETURN_SALT);
		staff.setPhone(staffPhone);
		staff.setPwdEncrypted(encrypt);
		Staff staffCreate = BaseStaffTest.createStaffViaAction(staff, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		Staff staffResetMyPassword = BaseStaffTest.resetMyPasswordExViaAction(staffCreate, mvc, mapBO, Shared.DBName_Test, Shared.PASSWORD_DEFAULT, BaseStaffTest.PASSWORD_123456, EnumTypeRole.ETR_Boss.getIndex());
		// 验证售前账号状态是删除的
		BaseStaffTest.checkIfDeletePreSaleStaff(staffResetMyPassword, Shared.DBName_Test, staffBO, EnumStatusStaff.ESS_Resigned.getIndex());
		//
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
		//
		BaseStaffTest.deleteStaffViaAction(staffCreate, sessionBoss, mvc, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testResetMyPasswordEx34_2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case34_2：非老板首次登录，修改自己密码，售前账号会被删除");
		// 保证售前是未删除的
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
		//
		String staffPhone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, staffPhone, Staff.FOR_CreateNewStaff);
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		//
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staff.setReturnSalt(Staff.RETURN_SALT);
		staff.setPhone(staffPhone);
		staff.setPwdEncrypted(encrypt);
		Staff staffCreate = BaseStaffTest.createStaffViaAction(staff, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		Staff staffResetMyPassword = BaseStaffTest.resetMyPasswordExViaAction(staffCreate, mvc, mapBO, Shared.DBName_Test, Shared.PASSWORD_DEFAULT, BaseStaffTest.PASSWORD_123456, EnumTypeRole.ETR_Cashier.getIndex());
		// 验证售前账号状态是未删除的
		BaseStaffTest.checkIfDeletePreSaleStaff(staffResetMyPassword, Shared.DBName_Test, staffBO, EnumStatusStaff.ESS_Incumbent.getIndex());
		//
		BaseStaffTest.deleteStaffViaAction(staffCreate, sessionBoss, mvc, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testResetMyPasswordEx35() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case35：老板非首次登录，修改自己密码，售前账号不会被删除");
		// 保证售前是未删除的
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
		//
		String staffPhone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, staffPhone, Staff.FOR_CreateNewStaff);
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		//
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setReturnSalt(Staff.RETURN_SALT);
		staff.setPhone(staffPhone);
		staff.setPwdEncrypted(encrypt);
		Staff staffCreate = BaseStaffTest.createStaffViaAction(staff, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		Staff staffResetMyPassword = BaseStaffTest.resetMyPasswordExViaAction(staffCreate, mvc, mapBO, Shared.DBName_Test, Shared.PASSWORD_DEFAULT, BaseStaffTest.PASSWORD_123456, EnumTypeRole.ETR_Boss.getIndex());
		BaseStaffTest.checkIfDeletePreSaleStaff(staffResetMyPassword, Shared.DBName_Test, staffBO, EnumStatusStaff.ESS_Resigned.getIndex());
		//
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
		// 这时老板是非首次登录，修改自己密码，售前账号状态是未删除
		Staff staffResetMyPassword2 = BaseStaffTest.resetMyPasswordExViaAction(staffCreate, mvc, mapBO, Shared.DBName_Test, BaseStaffTest.PASSWORD_123456, Shared.PASSWORD_DEFAULT, EnumTypeRole.ETR_Boss.getIndex());
		// 验证售前账号状态是未删除的
		BaseStaffTest.checkIfDeletePreSaleStaff(staffResetMyPassword2, Shared.DBName_Test, staffBO, EnumStatusStaff.ESS_Incumbent.getIndex());
		//
		BaseStaffTest.deleteStaffViaAction(staffCreate, sessionBoss, mvc, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testResetMyPasswordEx36() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case36：老板首次登录，修改自己密码，售前账号已经被删除");
		String staffPhone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, staffPhone, Staff.FOR_CreateNewStaff);
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		//
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setReturnSalt(Staff.RETURN_SALT);
		staff.setPhone(staffPhone);
		staff.setPwdEncrypted(encrypt);
		Staff staffCreate = BaseStaffTest.createStaffViaAction(staff, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		Staff staffResetMyPassword = BaseStaffTest.resetMyPasswordExViaAction(staffCreate, mvc, mapBO, Shared.DBName_Test, Shared.PASSWORD_DEFAULT, BaseStaffTest.PASSWORD_123456, EnumTypeRole.ETR_Boss.getIndex());
		BaseStaffTest.checkIfDeletePreSaleStaff(staffResetMyPassword, Shared.DBName_Test, staffBO, EnumStatusStaff.ESS_Resigned.getIndex());
		//
		BaseStaffTest.deleteStaffViaAction(staffCreate, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 这时老板是首次登录，修改自己密码，售前账号状态是删除的
		String staffPhone2 = Shared.getValidStaffPhone();
		MvcResult ret2 = BaseStaffTest.getToken(sessionBoss, mvc, staffPhone2, Staff.FOR_CreateNewStaff);
		String encrypt2 = Shared.encrypt(ret2, Shared.PASSWORD_DEFAULT);
		//
		Staff staff2 = BaseStaffTest.DataInput.getStaff();
		staff2.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff2.setReturnSalt(Staff.RETURN_SALT);
		staff2.setPhone(staffPhone2);
		staff2.setPwdEncrypted(encrypt2);
		Staff staffCreate2 = BaseStaffTest.createStaffViaAction(staff2, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		Staff staffResetMyPassword2 = BaseStaffTest.resetMyPasswordExViaAction(staffCreate2, mvc, mapBO, Shared.DBName_Test, Shared.PASSWORD_DEFAULT, BaseStaffTest.PASSWORD_123456, EnumTypeRole.ETR_Boss.getIndex());
		BaseStaffTest.checkIfDeletePreSaleStaff(staffResetMyPassword2, Shared.DBName_Test, staffBO, EnumStatusStaff.ESS_Resigned.getIndex());
		//
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
		//
		BaseStaffTest.deleteStaffViaAction(staffCreate2, sessionBoss, mvc, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testResetOtherPasswordEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 老板重置他人密码");
		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult ret1 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew1 = Shared.encrypt(ret1, Shared.PASSWORD_DEFAULT);
		//
		MvcResult mrl = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session4) //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew1) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mrl);
		// 验证是否返回了F_RoleID字段
		JSONObject object = JSONObject.fromObject(mrl.getResponse().getContentAsString());
		Staff staff = (Staff) new Staff().parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff.getRoleID() == EnumTypeRole.ETR_Cashier.getIndex(), BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
		// 验证是否返回了盐值
		BaseStaffTest.checkIfReturnSalt(mrl);
		StaffCP.verifyResetOtherPassword(mrl, session4, Shared.PASSWORD_DEFAULT, staffBO, staffRoleBO, Shared.DBName_Test);
	}

	@Test
	public void testResetOtherPasswordEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2 :重置密码传递不正确的密码");
		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult ret2 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
		final String pwdNew2 = "111";
		String sPasswordEncryptedNew2 = Shared.encrypt(ret2, pwdNew2);
		//
		MvcResult mrl2 = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session4) //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), "1") //
				.param(Key_NewPasword, sPasswordEncryptedNew2) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mrl2, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testResetOtherPasswordEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3 :没有权限");
//		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
//		HttpSession session5 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager);
//
//		MvcResult ret3 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
//		final String pwd3 = "111111";
//		String encrypt3 = Shared.encrypt(ret3, pwd3);
//
//		MvcResult mrl3 = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
//				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
//				.param(Staff.field.getFIELD_NAME_involvedResigned(), "1") //
//				.param(Key_NewPasword, encrypt3) //
//				.session((MockHttpSession) session5) //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testResetOtherPasswordEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4 :session 为空");
		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult ret3 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
		final String pwd3 = "111111";
		String encrypt3 = Shared.encrypt(ret3, pwd3);
		MvcResult result = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), "1") //
				.param(Key_NewPasword, encrypt3) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Assert.assertTrue("".equals(result.getResponse().getContentAsString()), "无会话期望是返回null");
	}

	@Test
	public void testResetOtherPasswordEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:老板重置自己密码，不能重置！");
		HttpSession session9 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		MvcResult ret5 = BaseStaffTest.getToken(session9, mvc, Shared.PhoneOfBoss, Staff.FOR_ModifyPassword);
		String encryptPwd5 = Shared.encrypt(ret5, Shared.PASSWORD_DEFAULT);
		//
		MvcResult mrl9 = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfBoss) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), "1") //
				.param(Key_NewPasword, encryptPwd5) //
				.session((MockHttpSession) session9) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mrl9, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testResetOtherPasswordEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6: 别人修改离职员工密码，错误码7");// 已离职员工不能被修改密码，sp已做限制
		final String Staff_Phone6 = Shared.PhoneOfResignedStaff;
		HttpSession session12 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		MvcResult ret6 = BaseStaffTest.getToken(session12, mvc, Staff_Phone6, Staff.FOR_ModifyPassword);
		// ..加密密码
		String encryptPwd6 = Shared.encrypt(ret6, Shared.PASSWORD_DEFAULT);
		MvcResult m6 = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session12) //
				.param(Staff.field.getFIELD_NAME_phone(), Staff_Phone6) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), "1")//
				.param(Key_NewPasword, encryptPwd6) //
				.contentType(MediaType.APPLICATION_JSON)//
				.session((MockHttpSession) session12)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(m6, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 验证是否返回了盐值
		// checkIsReturnSalt(m6);
	}

	@Test
	public void testResetOtherPasswordEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case7: 未接收到sPasswordEncryptedNew");
		final String Staff_Phone6 = Shared.PhoneOfResignedStaff;
		HttpSession session12 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult m7 = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session12) //
				.param(Staff.field.getFIELD_NAME_phone(), Staff_Phone6) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), "1")//
				.contentType(MediaType.APPLICATION_JSON)//
				.session((MockHttpSession) session12)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(m7.getResponse().getContentAsString()), "未检测到没传递sPasswordEncryptedNew");
	}

	@Test
	public void testResetOtherPasswordEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case8: 创建一个和离职员工相同手机的员工，修改该员工密码，能修改成功");// 已离职员工不能被修改密码，sp已做限制
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());//
		staff.setPhone(Shared.PhoneOfResignedStaff);
		// 检验字段合法性
		String errMsg = staff.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(errMsg.equals(""), errMsg);
		//
		Map<String, Object> createParams = staff.getCreateParam(BaseBO.INVALID_CASE_ID, staff);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff s = (Staff) staffMapper.create(createParams);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError && s != null, createParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		s.setIgnoreIDInComparision(true);
		if (s.compareTo(staff) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		HttpSession session12 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		MvcResult ret6 = BaseStaffTest.getToken(session12, mvc, Shared.PhoneOfResignedStaff, Staff.FOR_ModifyPassword);
		// ..加密密码
		String encryptedNewPwd = Shared.encrypt(ret6, Shared.PASSWORD_DEFAULT);
		MvcResult m6 = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session12) //
				.param(Staff.field.getFIELD_NAME_phone(), s.getPhone()) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), "1")//
				.param(Key_NewPasword, encryptedNewPwd) //
				.contentType(MediaType.APPLICATION_JSON)//
				.session((MockHttpSession) session12)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(m6, EnumErrorCode.EC_NoError);
		// 验证是否返回了盐值
		// checkIsReturnSalt(m6);

		deleteStaff(s, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testResetOtherPasswordEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9: 老板重置他人密码,新密码有中文，修改失败");
		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult ret1 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew1 = Shared.encrypt(ret1, "!@#123中文Abc$%^");
		//
		MvcResult mrl = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session4) //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew1) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mrl, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testResetOtherPasswordEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10: 老板重置他人密码,新密码首尾有中文，修改失败");
		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult ret1 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew1 = Shared.encrypt(ret1, "中!@#123Abc$%^文");
		//
		MvcResult mrl = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session4) //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew1) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mrl, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testResetOtherPasswordEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11: 老板重置他人密码,新密码首尾有空格，修改失败");
		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult ret1 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew1 = Shared.encrypt(ret1, " !@#123Abc$%^ ");
		//
		MvcResult mrl = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session4) //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew1) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mrl, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testResetOtherPasswordEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12: 老板重置他人密码,新密码全部是空格，修改失败");
		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult ret1 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew1 = Shared.encrypt(ret1, "                ");
		//
		MvcResult mrl = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session4) //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew1) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mrl, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testResetOtherPasswordEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case13: 老板重置他人密码,新密码小于6位，修改失败");
		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult ret1 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew1 = Shared.encrypt(ret1, "12Ab!");
		//
		MvcResult mrl = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session4) //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew1) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mrl, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testResetOtherPasswordEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14: 老板重置他人密码,新密码大于16位，修改失败");
		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult ret1 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew1 = Shared.encrypt(ret1, "1234567890Abcd!@#$%^");
		//
		MvcResult mrl = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session4) //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew1) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mrl, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	@Test
	public void testResetOtherPasswordEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15: 老板重置他人密码,新密码为空串，修改失败");
		HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		MvcResult ret1 = BaseStaffTest.getToken(session4, mvc, Shared.PhoneOfCashier, Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew1 = Shared.encrypt(ret1, "1234567890Abcd!@#$%^");
		//
		MvcResult mrl = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session4) //
				.param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew1) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mrl, FieldFormat.FIELD_ERROR_Password, BaseStaffTest.TEST_RESULT_ErrorMsgNotAsExpected);
	}

	// CASE16在运行到Shared.encrypt(ret1, null);时，汇报空指针错误
	// @Test
	// public void testResetOtherPasswordEx16() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case16: 老板重置他人密码,新密码为null，修改失败");
	// HttpSession session4 = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
	// MvcResult ret1 = getToken(session4, Shared.PhoneOfCashier,
	// Staff.FOR_ModifyPassword);
	// String sPasswordEncryptedNew1 = Shared.encrypt(ret1, null);
	// //
	// MvcResult mrl = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
	// .session((MockHttpSession) session4) //
	// .param(Staff.field.getFIELD_NAME_phone(), Shared.PhoneOfCashier) //
	// .param(Staff.field.getFIELD_NAME_involvedResigned(),
	// String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
	// .param(Key_NewPasword, sPasswordEncryptedNew1) //
	// .contentType(MediaType.APPLICATION_JSON)) //
	// .andExpect(status().isOk()).andDo(print()).andReturn();
	// //
	// Shared.checkJSONErrorCode(mrl, EnumErrorCode.EC_WrongFormatForInputField);
	// Shared.checkJSONMsg(mrl, FieldFormat.FIELD_ERROR_Password, Msg);
	// }

	@Test
	public void testLogout() throws Exception {
		final String Staff_Phone = Shared.PhoneOfBoss;// staffAction登出时需要用到有权限的店员，所以把号码改为店员3的

		Staff s = new Staff();
		// ..拿到公钥
		MvcResult ret = mvc.perform(post("/staff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = ret.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();

		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

		final String pwd = Shared.PASSWORD_DEFAULT;
		// ..加密密码
		String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
		s.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
						.session((MockHttpSession) ret.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);

		MvcResult mr2 = mvc.perform(get("/staff/logoutEx.bx")//
				.session((MockHttpSession) mr.getRequest().getSession())//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);
	}

	@Test
	public void testStaffLoginSendMsgToWX() throws Exception {
		Shared.printTestMethodStartInfo();
		String Staff_Phone = "13144496272";// 店员1号,但是普通店员登录时发送消息

		Staff s = new Staff();
		// ..拿到公钥
		MvcResult ret = mvc.perform(post("/staff/getTokenEx.bx").contentType(MediaType.APPLICATION_JSON).param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = ret.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();

		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

		final String pwd = Shared.PASSWORD_DEFAULT;
		// ..加密密码
		String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
		s.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
						.param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
						.session((MockHttpSession) ret.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testPosLogoutSendMsgToWX() throws Exception {
		final String Staff_Phone = Shared.PhoneOfBoss;// staffAction登出时需要用到有权限的店员，所以把号码改为店员3的

		Staff s = new Staff();
		// ..拿到公钥
		MvcResult ret = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		String json = ret.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String modulus = JsonPath.read(o, "$.rsa.modulus");
		String exponent = JsonPath.read(o, "$.rsa.exponent");
		modulus = new BigInteger(modulus, 16).toString();
		exponent = new BigInteger(exponent, 16).toString();

		RSAPublicKey publicKey = RSAUtils.getPublicKey(modulus, exponent);

		final String pwd = Shared.PASSWORD_DEFAULT;
		// ..加密密码
		String pwdEncrypted = RSAUtils.encryptByPublicKey(pwd, publicKey);
		s.setPwdEncrypted(pwdEncrypted);

		MvcResult mr = mvc.perform(post("/staff/loginEx.bx")//
				.param(Staff.field.getFIELD_NAME_companySN(), Shared.DB_SN_Test)//
				.param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)//
				.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
				.param(Staff.field.getFIELD_NAME_isLoginFromPos(), "1")// ..模拟pos登录，赋值int3 = 1；
				.session((MockHttpSession) ret.getRequest().getSession())//
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoError);

		MvcResult mr2 = mvc.perform(//
				get("/staff/logoutEx.bx?")//
						.session((MockHttpSession) mr.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()//
				)//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Shared.caseLog("Case1:查询一个已存在的员工的手机号码");
		MvcResult mr1 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "1") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "13245678901")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		Shared.caseLog("Case2:查询一个已存在的员工的手机号码");
		MvcResult mr2 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "1") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "13144496272")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);

		Shared.caseLog("Case3:查询一个已存在的离职员工的手机号码");

		// 创建一个员工
		Staff staffCase3 = BaseStaffTest.createStaffViaActionWithoutParameter(sessionBoss, mvc, mapBO, Shared.DBName_Test);
		// 把这个员工删除了
		deleteStaff(staffCase3, EnumErrorCode.EC_NoError);
		// 查询这个员工
		MvcResult mr3 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "1")//
						.param(Staff.field.getFIELD_NAME_uniqueField(), staffCase3.getPhone())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		Shared.caseLog("Case4:查询一个不存在的员工的身份证号");
		MvcResult mr4 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "2")//
						.param(Staff.field.getFIELD_NAME_uniqueField(), "540883198412111666")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4);

		Shared.caseLog("Case4.1:身份证长度大于18位");
		MvcResult mr41 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "2") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "5408831984121116661")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr41, EnumErrorCode.EC_WrongFormatForInputField);

		Shared.caseLog("Case5: 查询一个已存在的员工的身份证号");
		MvcResult mr5 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "2") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "440883198412111666")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_Duplicated);

		Shared.caseLog("Case6:查询一个已存在的离职员工的身份证号");
		MvcResult mr6 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "2") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "341522198412111666")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr6);

		Shared.caseLog("Case7:查询一个不存在的员工的微信号");
		MvcResult mr7 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "3") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "fffff2f")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr7);

		Shared.caseLog("Case7.1:输入格式不合法，不是由英文和数字组成");
		MvcResult mr71 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "3") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "fff张ff2f")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr71, EnumErrorCode.EC_WrongFormatForInputField);

		Shared.caseLog("Case8:查询一个已存在的员工的微信号");
		MvcResult mr8 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "3") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "a326dsd")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_Duplicated);

		Shared.caseLog("Case9:查询一个已存在的离职员工的微信号");
		MvcResult mr9 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "3") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "d2sasb4")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr9);

		Shared.caseLog("Case10:查询一个已存在的员工的手机号码,但传入的ID与已存在的员工的手机号码对应的员工ID相同");
		MvcResult mr10 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_ID(), "2") //
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "1") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "13144496272")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr10);

		Shared.caseLog("Case11: 查询一个已存在的员工的身份证号,但传入的ID与已存在的员工的身份证号对应的员工ID相同");
		MvcResult mr11 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_ID(), "2") //
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "2") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "440883198412111666")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr11);

		Shared.caseLog("Case12:查询一个已存在的员工的微信号,但传入的ID与已存在的员工的微信号对应的员工ID相同");
		MvcResult mr12 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Staff.field.getFIELD_NAME_ID(), "2") //
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "3") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "a326dsd")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr12);

		Shared.caseLog("Case13:查询一个已存在的员工的微信号,传入的ID与已存在的员工的微信号对应的员工ID不相同");
		MvcResult mr13 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_ID(), "3") //
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), "3") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "a326dsd")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_Duplicated);

		Shared.caseLog("Case14:传入非法的RoleID(小于0)");
		MvcResult mr14 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_ID(), "1")//
						.param(Staff.field.getFIELD_NAME_roleID(), "-1")//
						.param(Staff.field.getFIELD_NAME_uniqueField(), "a326dsd")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr14, EnumErrorCode.EC_WrongFormatForInputField);

		Shared.caseLog("Case15:传入非法的RoleID(0)");
		MvcResult mr15 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_ID(), "1") //
						.param(Staff.field.getFIELD_NAME_roleID(), "0") //
						.param(Staff.field.getFIELD_NAME_uniqueField(), "a326dsd")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr15, EnumErrorCode.EC_WrongFormatForInputField);

		Shared.caseLog("Case16:唯一性检查时,员工的身份证号为空串");
		String ICID = "";
		MvcResult mr16 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_ID(), "3") //
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Staff.CASE_CHECKICID)) //
						.param(Staff.field.getFIELD_NAME_uniqueField(), ICID)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr16);

		Shared.caseLog("Case17:唯一性检查时,员工的身份证号为null");
		ICID = null;
		MvcResult mr17 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_ID(), "3") //
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Staff.CASE_CHECKICID)) //
						.param(Staff.field.getFIELD_NAME_uniqueField(), ICID)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr17);

		Shared.caseLog("Case18:唯一性检查时,员工的微信号为空串");
		String weChat = "";
		MvcResult mr18 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_ID(), "3") //
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Staff.CASE_CHECKWECHAT)) //
						.param(Staff.field.getFIELD_NAME_uniqueField(), weChat)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr18);

		Shared.caseLog("Case19:唯一性检查时,员工的身份证号为null");
		weChat = null;
		MvcResult mr19 = mvc.perform(//
				post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Staff.field.getFIELD_NAME_ID(), "3") //
						.param(Staff.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Staff.CASE_CHECKWECHAT)) //
						.param(Staff.field.getFIELD_NAME_uniqueField(), weChat)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr19);

		// System.out.println("----------------------------Case10:没有权限进行查询---------------------------------------");
		// MvcResult mr10 = mvc.perform(//
		// post("/staff/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// Shared.PhoneOfManager))//\
		// .param(Staff.field.getFIELD_NAME_int1(), "3")
		// .param(Staff.field.getFIELD_NAME_string1(), "d2sasb4")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveResigned() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("----------------------------Case1:查询已离职的员工------------------------------------------");
		MvcResult mr1 = mvc.perform(//
				get("/staff/retrieveResigned.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
	}

	// TODO testStaffLoginPos1 到 testStaffLoginPos6继续让它们不通过吗
	@Test
	public void testStaffLoginPos1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Staff登录两台POS(目前只支持单点登录)");
		//
		checkPOSID();
		//
		Assert.assertTrue(false, "拦截器在跑自动化测试时，不会跑进拦截器中的代码。这里必须手动测试。测试步骤为：\n"//
				+ "POS1登录Staff成功，后该Staff在POS2登录且登录成功；POS1做重置基础资料，交班，上传零售单等操作(服务器将拦截这些操作)，且操作无效");
	}

	@Test
	public void testStaffLoginPos2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("staff同时登录Web跟POS机，成功登录");
		final int POS1ID = 1;
		// Staff登录POS1
		Shared.getPosLoginSession(mvc, POS1ID); // 该函数中使用的是NBR下ID为4且是店长的手机做为默认登录，与getStaffLoginSession()中的相同，且保持一致
		// Staff登录网页
		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss); //
		Assert.assertTrue(false, "拦截器在跑自动化测试时，不会跑进拦截器中的代码。这里必须手动测试。测试步骤为：\n" //
				+ "在网页端登录Staff登录成功，后该Staff登录POS机登录成功，且都可以做CRUD等操作");
	}

	@Test
	public void testStaffLoginPos3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("staff同时在网页端重复登录");

		// Staff登录网页
		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		// Staff重复登录网页
		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Assert.assertTrue(false, "拦截器在跑自动化测试时，不会跑进拦截器中的代码。这里必须手动测试。测试步骤为：\n" //
				+ "Staff登录Web1登录成功，后打开一个新的浏览器Web2登录该Staff,登录成功;在Web1做CURD等操作，弹框提示<你已经在其它地方登录>");
	}

	@Test
	public void testStaffLoginPos4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("网页上登录Staff,POS1上登录Staff,然后再POS2上登录Staff。\n"//
				+ "POS1的satff应该被踢出且不能做重置基础资料，交班，上传零售单等操作(服务器将拦截这些操作)");
		//
		// 网页上登录Staff
		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		checkPOSID();
		//
		Assert.assertTrue(false, "拦截器在跑自动化测试时，不会跑进拦截器中的代码。这里必须手动测试。测试步骤为：\n" //
				+ "Staff登录服务器登录成功,该Staff登录POS1登录成功,\n" //
				+ "后登录POS2登录成功,POS1做重置基础资料，交班，上传零售单等操作(服务器将拦截这些操作)，且操作无效");
	}

	@Test
	public void testStaffLoginPos5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Staff成功登录POS1，在登录POS2且成功POS1做重置基础资料，上传零售单，交班操作，POS2做相应的操作");

		checkPOSID();
		//
		Assert.assertTrue(false, "拦截器在跑自动化测试时，不会跑进拦截器中的代码。这里必须手动测试。测试步骤为：\n" //
				+ "Staff成功登录POS1，在登录POS2且成功POS1做重置基础资料，上传零售单，交班操作，\n" //
				+ "POS2做相应的操作。POS1的操作都无效，POS2操作正常");
	}

	@Test
	public void testStaffLoginPos6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Staff成功登录POS1,在登录POS2且成功，在POS1做重置基础资料，上传零售单，交班操作，POS2做重置基础资料，上传零售单，交班操作，" //
				+ "然后Staff再登录POS1且登录成功，然后再POS2做重置基础资料，上传零售单，交班操作，在POS1做重置基础资料，上传零售单，交班操作");
		//
		checkPOSID();
		//
		Assert.assertTrue(false, "拦截器在跑自动化测试时，不会跑进拦截器中的代码。这里必须手动测试。测试步骤为：\n"//
				+ "Staff成功登录POS1,在登录POS2且成功，在POS1做重置基础资料，上传零售单，交班等操作(操作失败)，POS2做重置基础资料，上传零售单，交班操作(操作成功)，\n"//
				+ "然后Staff再登录POS1且登录成功，然后再POS2做重置基础资料，上传零售单，交班等操作(操作失败)，在POS1做重置基础资料，上传零售单，交班等操作(操作成功)");
	}

	private void checkPOSID() throws Exception {
		final int POS1ID = 1;
		final int POS2ID = 2;
		String StaffID = "4"; // nbr中店长的ID
		// POS1登录
		HttpSession session = Shared.getPosLoginSession(mvc, POS1ID);
		// POS2登录
		HttpSession session2 = Shared.getPosLoginSession(mvc, POS2ID);
		// 判断相应的session内容
		Assert.assertTrue(session.getId() != session2.getId(), "两台POS机的sessionID应该不相同！");
		// 获取到保存在LoginGuard中的信息
		Map<String, LoginDevice> loginMap = new HashMap<String, LoginDevice>();
		loginMap = StaffLoginInterceptor.loginGuard.getMapStaffLoginDevice();
		// 比较posID是否为POS2登录的POS2ID
		int posID = loginMap.get(StaffID).getPosIDOfPos();
		Assert.assertTrue(posID == POS2ID, "LoginDevice中保存的POSID应该为POS2的ID值");
	}

	protected void deleteStaff(Staff staff, ErrorInfo.EnumErrorCode enumErrorCode) {
		Map<String, Object> deleteParam = staff.getDeleteParam(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.delete(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == enumErrorCode, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否删除成功
		staff.setInvolvedResigned(EnumStatusStaff.ESS_Resigned.getIndex());
		Staff staffR1 = retrieve1Staff(staff);
		if (staffR1.getStatus() == EnumStatusStaff.ESS_Resigned.getIndex()) {
			System.out.println("测试成功，删除成功");
		} else {
			System.out.println("测试成功，删除失败：" + deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
	}

	protected Staff retrieve1Staff(Staff staff) {
		Map<String, Object> retrieve1Param = staff.getRetrieve1Param(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffRetrieve1 = (Staff) staffMapper.retrieve1(retrieve1Param);
		//
		if (staffRetrieve1 != null) { // 条件满足，查询到数据,检查字段的合法性
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, retrieve1Param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			staffRetrieve1.setIsFirstTimeLogin(EnumBoolean.EB_Yes.getIndex()); // 为了通过下面的checkCreate()方法
			String err = staffRetrieve1.checkCreate(BaseBO.CASE_SpecialResultVerification);
			Assert.assertEquals(err, "");
			//
			System.out.println("查询成功： " + staffRetrieve1);
		} else { // 条件不满足，没有查询到数据，不检查字段的合法性
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, retrieve1Param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("查询成功： " + staffRetrieve1);
		}
		//
		return staffRetrieve1;
	}
}
