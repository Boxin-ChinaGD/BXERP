package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.StaffRoleBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.dao.BaseMapper;
import com.bx.erp.dao.StaffMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.model.StaffBelonging;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.test.checkPoint.StaffCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class BaseStaffTest extends BaseMapperTest{
	public final static String PASSWORD_123456 = "123456";
	public final static String Key_NewPasword = "sPasswordEncryptedNew";
	public final static String Key_OldPasword = "sPasswordEncryptedOld";
	public final static String TEST_RESULT_ErrorMsgNotAsExpected = "返回的错误信息不是预期的";

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		// Doctor_checkICID();
		// Doctor_checkIsFirstTimeLogin();
		// Doctor_checkName();
		// Doctor_checkOpenID();
		// Doctor_checkPhone();
		// Doctor_checkStatus();
		// Doctor_checkUnionid();
		// Doctor_checkWeChat();
		// Doctor_checkCreate();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();

		// Doctor_checkICID();
		// Doctor_checkIsFirstTimeLogin();
		// Doctor_checkName();
		// Doctor_checkOpenID();
		// Doctor_checkPhone();
		// Doctor_checkStatus();
		// Doctor_checkUnionid();
		// Doctor_checkWeChat();
		// Doctor_checkCreate();
	}

	public static class DataInput {
		private static Staff staffInput = new Staff();
		private static Promotion promotionInput = null;
		private static Random r = new Random();
		private static BxStaff bxStaffInput;

		public static final Staff getStaff() throws Exception {
			staffInput.setPhone(Shared.getValidStaffPhone());
			Thread.sleep(1);
			staffInput.setName("店员" + Shared.generateCompanyName(6));
			Thread.sleep(1);
			Thread.sleep(1);
			staffInput.setICID(Shared.getValidICID()); //
			Thread.sleep(1);
			staffInput.setWeChat("rr1" + Shared.generateStringByTime(6));//
			Thread.sleep(1);
			// staffInput.setSalt(MD5Util.MD5("123456" + BaseAction.SHADOW));
			staffInput.setSalt(Shared.getFakedSalt());
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);//
			staffInput.setPasswordExpireDate(sdf.parse("2018/12/22 13:11:00")); // 该功能未启用
			staffInput.setIsFirstTimeLogin(EnumBoolean.EB_Yes.getIndex());
			staffInput.setShopID(1);
			staffInput.setDepartmentID(1);
			staffInput.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
			staffInput.setReturnSalt(EnumBoolean.EB_Yes.getIndex());
			staffInput.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
			staffInput.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
			staffInput.setOperator(EnumBoolean.EB_NO.getIndex());

			return (Staff) staffInput.clone();
		}

		public static final Promotion getPromotion() throws CloneNotSupportedException {
			promotionInput = new Promotion();
			promotionInput.setName(UUID.randomUUID().toString().substring(1, 7));
			promotionInput.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
			promotionInput.setType(new Random().nextInt(100) % 2);
			promotionInput.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2)); // 活动开始时间
			promotionInput.setDatetimeEnd(DatetimeUtil.getDays(new Date(), 3)); // 活动结束时间
			promotionInput.setExcecutionThreshold(r.nextInt(50) + 10);
			promotionInput.setExcecutionAmount(r.nextInt(10) + 1);
			promotionInput.setExcecutionDiscount(1 - r.nextDouble()); // 0<=ran.nextDouble()<1
			promotionInput.setScope(r.nextInt(50) % 2);
			promotionInput.setStaff(1);
			promotionInput.setPageIndex(1);
			promotionInput.setPageSize(10);
			return (Promotion) promotionInput.clone();
		}

		public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Staff s) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType)//
					.param(Staff.field.getFIELD_NAME_ID(), "" + s.getID()) //
					.param(Staff.field.getFIELD_NAME_phone(), s.getPhone())//
					.param(Staff.field.getFIELD_NAME_name(), s.getName())//
					.param(Staff.field.getFIELD_NAME_ICID(), s.getICID())//
					.param(Staff.field.getFIELD_NAME_weChat(), s.getWeChat())//
					.param(Staff.field.getFIELD_NAME_pwdEncrypted(), s.getPwdEncrypted())//
					.param(Staff.field.getFIELD_NAME_passwordExpireDate(), s.getPasswordExpireDate() + "")//
					.param(Staff.field.getFIELD_NAME_isFirstTimeLogin(), s.getIsFirstTimeLogin() + "")//
					.param(Staff.field.getFIELD_NAME_shopID(), s.getShopID() + "")//
					.param(Staff.field.getFIELD_NAME_departmentID(), s.getDepartmentID() + "")//
					.param(Staff.field.getFIELD_NAME_roleID(), s.getRoleID() + "")//
					.param(Staff.field.getFIELD_NAME_returnSalt(), s.getReturnSalt() + "")//
					.param(Staff.field.getFIELD_NAME_status(), s.getStatus() + "");//
			return builder;
		}
		

		public static final BxStaff getBxStaff() throws ParseException, InterruptedException {
			bxStaffInput = new BxStaff();
			bxStaffInput.setName("内部人员" + System.currentTimeMillis() % 1000000);//
			bxStaffInput.setICID(Shared.getValidICID()); //
			bxStaffInput.setSalt("123456");
			bxStaffInput.setDepartmentID(1);//
			// bxStaffInput.setInt1(1);

			return bxStaffInput;
		}
	}

	// private void Doctor_checkICID() {
	// Shared.printTestClassEndInfo();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// staffMapper.checkICID(params);
	// if
	// (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// != EnumErrorCode.EC_NoError) {
	// System.out.println("检查的员工的身份证的错误码不正确，errorCode =" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	// }
	// if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
	// System.out.println("检查的员工的身份证的错误信息与预期中的错误信息不相符，errorMsg=" +
	// params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// }
	// }
	//
	// private void Doctor_checkWeChat() {
	// Shared.printTestClassEndInfo();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// staffMapper.checkWeChat(params);
	// if
	// (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// != EnumErrorCode.EC_NoError) {
	// System.out.println("检查的员工的微信号的错误码不正确，errorCode =" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	// }
	// if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
	// System.out.println("检查的员工的微信号的错误信息与预期中的错误信息不相符，errorMsg=" +
	// params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// }
	// }
	//
	// private void Doctor_checkOpenID() {
	// Shared.printTestClassEndInfo();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// staffMapper.checkOpenID(params);
	// if
	// (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// != EnumErrorCode.EC_NoError) {
	// System.out.println("检查的员工的OpenID的错误码不正确，errorCode =" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	// }
	// if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
	// System.out.println("检查的员工的OpenID的错误信息与预期中的错误信息不相符，errorMsg=" +
	// params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// }
	// }
	//
	// private void Doctor_checkUnionid() {
	// Shared.printTestClassEndInfo();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// staffMapper.checkUnionid(params);
	// if
	// (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// != EnumErrorCode.EC_NoError) {
	// System.out.println("检查的员工的Unionid的错误码不正确，errorCode =" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	// }
	// if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
	// System.out.println("检查的员工的Unionid的错误信息与预期中的错误信息不相符，errorMsg=" +
	// params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// }
	// }
	//
	// private void Doctor_checkIsFirstTimeLogin() {
	// Shared.printTestClassEndInfo();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// staffMapper.checkIsFirstTimeLogin(params);
	// if
	// (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// != EnumErrorCode.EC_NoError) {
	// System.out.println("检查的员工的IsFirstTimeLogin的错误码不正确，errorCode =" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	// }
	// if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
	// System.out.println("检查的员工的IsFirstTimeLogin的错误信息与预期中的错误信息不相符，errorMsg=" +
	// params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// }
	// }
	//
	// private void Doctor_checkPhone() {
	// Shared.printTestClassEndInfo();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// staffMapper.checkPhone(params);
	// if
	// (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// != EnumErrorCode.EC_NoError) {
	// System.out.println("检查的员工的手机号的错误码不正确，errorCode =" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	// }
	// if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
	// System.out.println("检查的员工的手机号的错误信息与预期中的错误信息不相符，errorMsg=" +
	// params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// }
	// }
	//
	// private void Doctor_checkName() {
	// Shared.printTestClassEndInfo();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// staffMapper.checkName(params);
	// if
	// (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// != EnumErrorCode.EC_NoError) {
	// System.out.println("检查的员工的名称的错误码不正确，errorCode =" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	// }
	// if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
	// System.out.println("检查的员工的名称的错误信息与预期中的错误信息不相符，errorMsg=" +
	// params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// }
	// }
	//
	// private void Doctor_checkStatus() {
	// Shared.printTestClassEndInfo();
	//
	// Map<String, Object> params = new HashMap<String, Object>();
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// staffMapper.checkStatus(params);
	// if
	// (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// != EnumErrorCode.EC_NoError) {
	// System.out.println("检查的员工的状态的错误码不正确，errorCode =" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	// }
	// if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
	// System.out.println("检查的员工的状态的错误信息与预期中的错误信息不相符，errorMsg=" +
	// params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// }
	// }
	//
	// private void Doctor_checkCreate() {
	// Shared.printTestClassEndInfo();
	//
	// Staff staff = new Staff();
	// staff.setStatus(BaseAction.INVALID_STATUS);
	// staff.setPageIndex(1);
	// staff.setPageSize(BaseAction.PAGE_SIZE_Infinite);
	// //
	// String error = staff.checkRetrieveN(BaseBO.INVALID_CASE_ID);
	// if (!error.equals("")) {
	// System.out.println("对员工进行RN时，传入的的字段数据校验出现异常");
	// System.out.println("staff=" + staff.toString());
	// }
	// //
	// Map<String, Object> params = staff.getRetrieveNParam(BaseBO.INVALID_CASE_ID,
	// staff);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// List<BaseModel> list = staffMapper.retrieveN(params);
	// if
	// (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// != EnumErrorCode.EC_NoError) {
	// System.out.println("错误码不正确，errorCode =" +
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
	// }
	// if (list.size() == 0 || list == null) {
	// System.out.println("RN读出的员工信息为空");
	// }
	// for (BaseModel bm : list) {
	// Staff s = (Staff) bm;
	// error = s.checkCreate(BaseBO.INVALID_CASE_ID);
	// if (!error.equals("")) {
	// System.out.println(s.getID() + "号员工数据验证出现异常");
	// System.out.println("s=" + s.toString());
	// }
	// }
	// }

	/** 以下子程序仅适用于MapperTest */
	public static Staff createStaff(int iUseCaseID, Staff staff, ErrorInfo.EnumErrorCode enumErrorCode, StaffMapper staffMapper) {
		String err = staff.checkCreate(iUseCaseID);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getCreateParam(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffCreate = (Staff) staffMapper.create(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == enumErrorCode, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (staffCreate != null) { // 条件合法，创建成功
			staffCreate.setSalt(staff.getSalt());
			staffCreate.setRoleID(staff.getRoleID());
			err = staffCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			staff.setIgnoreIDInComparision(true);
			if (staff.compareTo(staffCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			//
			System.out.println("测试成功，创建店员成功： " + staffCreate);
		} else { // 条件不合法，创建失败
			System.out.println("测试成功，创建失败:" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return staffCreate;
	}

	public static Staff updateStaff(int iUseCaseID, Staff staff, ErrorInfo.EnumErrorCode enumErrorCode, StaffMapper staffMapper) {
		String err = staff.checkUpdate(iUseCaseID);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getUpdateParam(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffUpdate = (Staff) staffMapper.update(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == enumErrorCode, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (staffUpdate != null) { // 条件合法，修改成功
			staffUpdate.setSalt(staff.getSalt());
			staffUpdate.setRoleID(staff.getRoleID());
			err = staffUpdate.checkUpdate(iUseCaseID);
			Assert.assertEquals(err, "");
			//
			if (staffUpdate.compareTo(staff) != 0) {
				Assert.assertTrue(false, "字段验证不相同,修改失败");
			}
			//
			System.out.println("测试成功，修改店员成功： " + staffUpdate);
		} else { // 条件不合法，修改失败
			staff.setPhone("");
			Staff staffR1 = retrieve1Staff(staff, staffMapper);
			if (staffR1.compareTo(staff) == 0) {
				Assert.assertTrue(false, "字段验证全部相同,修改失败");
			}
			//
			System.out.println("测试成功，修改失败:" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return staffUpdate;
	}

	public static Staff retrieve1Staff(Staff staff, StaffMapper staffMapper) {
		Map<String, Object> retrieve1Param = staff.getRetrieve1Param(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffRetrieve1 = (Staff) staffMapper.retrieve1(retrieve1Param);
		//
		if (staffRetrieve1 != null) { // 条件满足，查询到数据,检查字段的合法性
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, retrieve1Param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
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

	public static List<BaseModel> retrieveNStaff(Staff staff, StaffMapper staffMapper) {
		Map<String, Object> retrieveNParam = staff.getRetrieveNParam(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> staffList = staffMapper.retrieveN(retrieveNParam);
		//
		Assert.assertTrue(staffList.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err;
		for (BaseModel bm : staffList) {
			if (((Staff) bm).getShopID() == 0) {
				((Staff) bm).setShopID(BaseAction.INVALID_ID);
			}
			err = ((Staff) bm).checkCreate(BaseBO.CASE_SpecialResultVerification);
			Assert.assertEquals(err, "");
			((Staff) bm).setShopID(0);
		}
		//
		System.out.println("测试成功： " + staffList);
		//
		return staffList;
	}

	public static void deleteStaff(Staff staff, ErrorInfo.EnumErrorCode enumErrorCode, StaffMapper staffMapper) {
		Map<String, Object> deleteParam = staff.getDeleteParam(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.delete(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == enumErrorCode, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否删除成功
		staff.setInvolvedResigned(EnumStatusStaff.ESS_Resigned.getIndex());
		Staff staffR1 = retrieve1Staff(staff, staffMapper);
		if (staffR1.getStatus() == EnumStatusStaff.ESS_Resigned.getIndex()) {
			System.out.println("测试成功，删除成功");
		} else {
			System.out.println("测试成功，删除失败：" + deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
	}

	public static Staff createStaffViaAction(Staff staff, HttpSession session, MockMvc mvc, Map<String, BaseBO> BOsMap, String dbName) throws Exception {
		MvcResult mr = mvc.perform(//
				DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		StaffRoleBO staffRoleBO = (StaffRoleBO) BOsMap.get(StaffRoleBO.class.getSimpleName());
		StaffCP.verifyCreate(mr, staff, staffRoleBO, dbName);
		//
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));

		return staff2;
	}

	public static void deleteStaffViaAction(Staff staff, HttpSession session, MockMvc mvc, Map<String, BaseBO> BOsMap, String dbName) throws Exception {
		MvcResult mr2 = mvc.perform(//
				get("/staff/deleteEx.bx?" + Staff.field.getFIELD_NAME_ID() + "=" + staff.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		//
		Shared.checkJSONErrorCode(mr2);
		//
		StaffBO staffBO = (StaffBO) BOsMap.get(StaffBO.class.getSimpleName());
		StaffRoleBO staffRoleBO = (StaffRoleBO) BOsMap.get(StaffRoleBO.class.getSimpleName());
		//
		Staff staffClone = (Staff) staff.clone();
		StaffCP.verifyDelete(staffClone, staffBO, staffRoleBO, dbName);
	}
	
	public static Staff updateStaffViaAction(Staff staff, HttpSession session, MockMvc mvc, Map<String, BaseBO> BOsMap, String dbName) throws Exception {
		MvcResult mr = mvc.perform(//
				DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		StaffCP.verifyUpdate(mr, staff, dbName);
		return (Staff) Shared.parse1Object(mr, staff, BaseAction.KEY_Object);
	}

	/** 修改售前账号为在职状态 */
	public static void updateStaffViaMapper(HttpSession session, MockMvc mvc, Map<String, BaseMapper> mappersMap, String dbName) throws Exception {
		StaffMapper staffMapper = (StaffMapper) mappersMap.get(StaffMapper.class.getSimpleName());
		Staff staff = new Staff();
		staff.setID(1);
		staff.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		Staff staffRetrieve1 = retrieve1Staff(staff, staffMapper);
		staffRetrieve1.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staffRetrieve1.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		staffRetrieve1.setIsFirstTimeLogin(EnumBoolean.EB_NO.getIndex());
		staffRetrieve1.setOperator(EnumBoolean.EB_Yes.getIndex());
		//
		MvcResult mr = mvc.perform(//
				DataInput.getBuilder("/staff/updateEx.bx", MediaType.APPLICATION_JSON, staffRetrieve1)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		StaffCP.verifyUpdate(mr, staffRetrieve1, dbName);
	}
	
	public static Staff updateOpenidAndUnionid(String openID, String phone, StaffBO staffBO) {
		Staff staff = new Staff();
		staff.setOpenid(openID);
		staff.setPhone(phone);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staff = (Staff) staffBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Staff_Update_OpenidAndUnionid, staff);
		Assert.assertTrue(staffBO.getLastErrorCode() == EnumErrorCode.EC_NoError, staffBO.getLastErrorMessage());
		return staff;
	}

	public static Staff resetMyPasswordExViaAction(Staff staff, MockMvc mvc, Map<String, BaseBO> BOsMap, String dbName, String oldPassword, String newPassword, int roleIDExpected) throws Exception {
		HttpSession session = Shared.getStaffLoginSession(mvc, staff.getPhone(), oldPassword, Shared.DB_SN_Test);
		MvcResult ret = getToken(session, mvc, staff.getPhone(), Staff.FOR_ModifyPassword);
		String sPasswordEncryptedOld = Shared.encrypt(ret, oldPassword);
		String sPasswordEncryptedNew = Shared.encrypt(ret, newPassword);
		//
		MvcResult mr = mvc.perform(post("/staff/resetMyPasswordEx.bx") //
				.session((MockHttpSession) session) //
				.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone()) //
				.param(Key_OldPasword, sPasswordEncryptedOld) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		// 验证是否返回了F_RoleID字段
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Staff staff2 = (Staff) new Staff().parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2.getRoleID() == roleIDExpected, TEST_RESULT_ErrorMsgNotAsExpected);
		// 验证是否返回了盐值
		checkIfReturnSalt(mr);
		//
		StaffBO staffBO = (StaffBO) BOsMap.get(StaffBO.class.getSimpleName());
		//
		StaffCP.verifyResetMyPassword(mr, newPassword, staffBO, dbName);

		return staff2;
	}
	
	public static Staff resetOtherPasswordExViaAction(Staff staff, HttpSession session, MockMvc mvc, Map<String, BaseBO> BOsMap, String dbName, String newPassword) throws Exception {
		MvcResult ret1 = BaseStaffTest.getToken(session, mvc, staff.getPhone(), Staff.FOR_ModifyPassword);
		String sPasswordEncryptedNew = Shared.encrypt(ret1, newPassword);
		//
		MvcResult mrl = mvc.perform(post("/staff/resetOtherPasswordEx.bx") //
				.session((MockHttpSession) session) //
				.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone()) //
				.param(Staff.field.getFIELD_NAME_involvedResigned(), String.valueOf(EnumBoolean.EB_Yes.getIndex())) //
				.param(Key_NewPasword, sPasswordEncryptedNew) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mrl);
		// 验证是否返回了盐值
		checkIfReturnSalt(mrl);
		//
		StaffBO staffBO = (StaffBO) BOsMap.get(StaffBO.class.getSimpleName());
		StaffRoleBO staffRoleBO = (StaffRoleBO) BOsMap.get(StaffRoleBO.class.getSimpleName());
		StaffCP.verifyResetOtherPassword(mrl, session, newPassword, staffBO, staffRoleBO, dbName);
		//
		JSONObject jsonObject = JSONObject.fromObject(mrl.getResponse().getContentAsString());
		return (Staff) staff.parse1(jsonObject.getString(BaseAction.KEY_Object));
	}

	public static void checkIfDeletePreSaleStaff(Staff staff, String dbName, StaffBO staffBO, int preSaleStatusExpected) {
		Staff preSaleStaff = new Staff();
		preSaleStaff.setPhone(BaseAction.ACCOUNT_Phone_PreSale);// 该值为默认手机号码
		preSaleStaff.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		DataSourceContextHolder.setDbName(dbName);
		Staff retrieveStaff = (Staff) staffBO.retrieve1Object(staff.getID(), BaseBO.INVALID_CASE_ID, preSaleStaff);
		Assert.assertTrue(retrieveStaff != null, "查询不到售前账号");
		Assert.assertTrue(staffBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "查询售前账号出现错误。" + staffBO.printErrorInfo());
		Assert.assertTrue(retrieveStaff.getStatus() == preSaleStatusExpected, "售前账号删除失败");
	}

	public static void checkIfReturnSalt(MvcResult mr) throws Exception {
		String mJson = mr.getResponse().getContentAsString();
		JSONObject jsonObject = JSONObject.fromObject(mJson);
		String salt = JsonPath.read(jsonObject, "$.object.salt");
		String newMD5 = JsonPath.read(jsonObject, "$.object.newMD5");
		String oldMD5 = JsonPath.read(jsonObject, "$.object.oldMD5");
		if (!(StringUtils.isEmpty(salt) && StringUtils.isEmpty(newMD5) && StringUtils.isEmpty(oldMD5))) {
			Assert.fail("返回给浏览器的盐值不为空!");
		}
	}

	public static MvcResult getToken(HttpSession session, MockMvc mvc, String phone, int forModifyPassword) throws Exception {
		MvcResult ret = mvc.perform(post("/staff/getTokenEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) session) //
				.param(Staff.field.getFIELD_NAME_phone(), phone)//
				.param(Staff.field.getFIELD_NAME_forModifyPassword(), String.valueOf(forModifyPassword))//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(ret);

		return ret;
	}
	
	public static MvcResult getToken(MockMvc mvc, String Staff_Phone) throws Exception, UnsupportedEncodingException {
		MvcResult ret = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(ret);
		return ret;
	}
	
	public static Staff createStaffForTest(MockMvc mvc, Map<String, BaseBO> BOsMap, Staff staff, String newPwd) throws Exception {
		StaffBO staffBO = (StaffBO) BOsMap.get(StaffBO.class.getSimpleName());
		// 1.创建一个staff
		MvcResult ret5 = mvc.perform(//
				post("/staff/getTokenEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Staff.field.getFIELD_NAME_phone(), staff.getPhone())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(ret5);
		String encrypt = Shared.encrypt(ret5, Shared.PASSWORD_DEFAULT);

		staff.setPwdEncrypted(encrypt);
		MvcResult mr5 = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
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
						.session((MockHttpSession) ret6.getRequest().getSession())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr6);// , EnumErrorCode.EC_Redirect);

		// 3.修改密码
		HttpSession session = mr6.getRequest().getSession();
		MvcResult ret = BaseStaffTest.getToken(session, mvc, phone6, Staff.FOR_ModifyPassword);
		//
		String sPasswordEncryptedOld = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		String sPasswordEncryptedNew = Shared.encrypt(ret, newPwd);
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
		StaffCP.verifyResetMyPassword(m, newPwd, staffBO, Shared.DBName_Test);
		String json3 = m.getResponse().getContentAsString();
		JSONObject o3 = JSONObject.fromObject(json3);
		Staff staff2 = (Staff) new Staff().parse1(o3.getString(BaseAction.KEY_Object));
		//
		return staff2;
	}
	
	public static Staff createStaffViaActionWithoutParameter(HttpSession session, MockMvc mvc, Map<String, BaseBO> BOsMap, String dbName) throws Exception {
		Staff staff = BaseStaffTest.DataInput.getStaff();
		MvcResult mr = mvc.perform(//
				DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		StaffRoleBO staffRoleBO = (StaffRoleBO) BOsMap.get(StaffRoleBO.class.getSimpleName());
		StaffCP.verifyCreate(mr, staff, staffRoleBO, dbName);
		//
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));

		return staff2;
	}
	
	public static Staff createStaffViaMapper(int iUseCaseID, Staff staff, ErrorInfo.EnumErrorCode enumErrorCode) {
		String err = staff.checkCreate(iUseCaseID);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getCreateParam(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffCreate = (Staff) staffMapper.create(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == enumErrorCode, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (staffCreate != null) { // 条件合法，创建成功
			staffCreate.setSalt(staff.getSalt());
			staffCreate.setRoleID(staff.getRoleID());
			err = staffCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			staff.setIgnoreIDInComparision(true);
			if (staff.compareTo(staffCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			//
			System.out.println("测试成功，创建店员成功： " + staffCreate);
		} else { // 条件不合法，创建失败
			System.out.println("测试成功，创建失败:" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return staffCreate;
	}
	
	public static Staff updateStaffViaMapper(int iUseCaseID, Staff staff, Staff staffCreate, ErrorInfo.EnumErrorCode enumErrorCode) {
		String err = staff.checkCreate(iUseCaseID);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getUpdateParam(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffUpdate = (Staff) staffMapper.update(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == enumErrorCode, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (staffUpdate != null) { // 条件合法，修改成功
			staffUpdate.setSalt(staff.getSalt());
			staffUpdate.setRoleID(staff.getRoleID());
			err = staffUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			if (staffUpdate.compareTo(staffCreate) == 0) {
				Assert.assertTrue(false, "字段验证全部相同,修改失败");
			}
			//
			System.out.println("测试成功，修改店员成功： " + staffUpdate);
		} else { // 条件不合法，修改失败
			Staff staffR1 = retrieve1StaffViaMapper(staffCreate);
			if (staffR1.compareTo(staffCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			//
			System.out.println("测试成功，修改失败:" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return staffUpdate;
	}
	
	public static Staff retrieve1StaffViaMapper(Staff staff) {
		Map<String, Object> retrieve1Param = staff.getRetrieve1Param(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Staff staffRetrieve1 = (Staff) staffMapper.retrieve1(retrieve1Param);
		//
		if (staffRetrieve1 != null) { // 条件满足，查询到数据,检查字段的合法性
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, retrieve1Param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
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
	
	public static void deleteStaffViaMapper(Staff staff, ErrorInfo.EnumErrorCode enumErrorCode) {
		Map<String, Object> deleteParam = staff.getDeleteParam(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffMapper.delete(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == enumErrorCode, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否删除成功
		Staff staffR1 = retrieve1StaffViaMapper(staff);
		if (staffR1.getStatus() == EnumStatusStaff.ESS_Resigned.getIndex()) {
			System.out.println("测试成功，删除成功");
		} else {
			System.out.println("测试成功，删除失败：" + deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
	}
	
	public static BxStaff retrieve1Bxstaff(BxStaff bs) {
		Map<String, Object> params = bs.getRetrieve1Param(BaseBO.INVALID_CASE_ID, bs);
		//
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		BxStaff bxStaff = (BxStaff) bxStaffMapper.retrieve1(params);
		//
		if (bxStaff != null) {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			String err = bxStaff.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		} else {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
		//
		System.out.println("查询对象成功： " + bxStaff);
		//
		return bxStaff;
	}
	
	/** 修改售前账号为在职状态 */
	public static void updatePreSaleStatusOfIncumbentViaMapper(String dbName) throws Exception {
		Staff staff = new Staff();
		staff.setID(1);
		staff.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		Staff staffRetrieve1 = retrieve1Staff(staff, staffMapper);
		staffRetrieve1.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staffRetrieve1.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		staffRetrieve1.setIsFirstTimeLogin(EnumBoolean.EB_NO.getIndex());
		staffRetrieve1.setOperator(EnumBoolean.EB_Yes.getIndex());
		//
		updateStaffViaMapper(dbName, BaseBO.INVALID_CASE_ID, staffRetrieve1, ErrorInfo.EnumErrorCode.EC_NoError, staffMapper);
		
	}
	
	public static Staff updateStaffViaMapper(String dbName, int iUseCaseID, Staff staff, ErrorInfo.EnumErrorCode enumErrorCode, StaffMapper staffMapper) {
		String err = staff.checkUpdate(iUseCaseID);
		Assert.assertEquals(err, "");
		Map<String, Object> params = staff.getUpdateParam(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(dbName);
		Staff staffUpdate = (Staff) staffMapper.update(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == enumErrorCode, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (staffUpdate != null) { // 条件合法，修改成功
			staffUpdate.setSalt(staff.getSalt());
			staffUpdate.setRoleID(staff.getRoleID());
			err = staffUpdate.checkUpdate(iUseCaseID);
			Assert.assertEquals(err, "");
			//
			staffUpdate.setOperator(staff.getOperator());
			if (staffUpdate.compareTo(staff) != 0) {
				Assert.assertTrue(false, "字段验证不相同,修改失败");
			}
			//
			System.out.println("测试成功，修改店员成功： " + staffUpdate);
		} else { // 条件不合法，修改失败
			staff.setPhone("");
			Staff staffR1 = retrieve1Staff(staff, staffMapper);
			if (staffR1.compareTo(staff) == 0) {
				Assert.assertTrue(false, "字段验证全部相同,修改失败");
			}
			//
			System.out.println("测试成功，修改失败:" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return staffUpdate;
	}
	
	public static List<BaseModel> retrieveNStaffBelonging(StaffBelonging staffBelong) {
		Map<String, Object> retrieveNParam = staffBelong.getRetrieveNParam(BaseBO.INVALID_CASE_ID, staffBelong);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> staffBelongingList = staffBelongingMapper.retrieveN(retrieveNParam);
		//
		Assert.assertTrue(staffBelongingList.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String errorMsg;
		for (BaseModel bm : staffBelongingList) {
			errorMsg = ((StaffBelonging) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(errorMsg, "");
		}
		//
		System.out.println("测试成功： " + staffBelongingList);
		//
		return staffBelongingList;
	}
	
	public static List<BaseModel> retrieveNViaAction(Staff staff, HttpSession session, MockMvc mvc) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/staff/retrieveNEx.bx")//
						.param(Staff.field.getFIELD_NAME_queryKeyword(), staff.getQueryKeyword())//
						.param(Staff.field.getFIELD_NAME_status(), String.valueOf(staff.getStatus()))
						.param(Staff.field.getFIELD_NAME_operator(), String.valueOf(staff.getOperator()))
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String object = o.getString(BaseAction.KEY_ObjectList);
		return staff.parseN(object);
	}
	
	public static MvcResult login(MockMvc mvc, HttpSession session, String companySN, String Staff_Phone, String pwdEncrypted, int involveResigned) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/staff/loginEx.bx")//
						.param(Staff.field.getFIELD_NAME_involvedResigned(), String.valueOf(involveResigned))//
						.param(Staff.field.getFIELD_NAME_companySN(), companySN)//
						.param(Staff.field.getFIELD_NAME_phone(), Staff_Phone)//
						.param(Staff.field.getFIELD_NAME_pwdEncrypted(), pwdEncrypted)//
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		return mr;
	}
}
