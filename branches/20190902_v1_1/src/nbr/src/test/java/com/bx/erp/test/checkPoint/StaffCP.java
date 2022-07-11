package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import javax.servlet.http.HttpSession;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.StaffRoleBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Role;
import com.bx.erp.model.Staff;
import com.bx.erp.model.StaffRole;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.MD5Util;
import net.sf.json.JSONObject;

@WebAppConfiguration
public class StaffCP {
	/** 1、检查员工A的普通缓存是否创建。 2、检查数据库T_Staff，查看员工A是否正常创建。
	 * 3、检查数据库T_StaffRole，查看是否创建了员工A关联的角色数据。 */
	public static void verifyCreate(MvcResult mr, Staff staff, StaffRoleBO staffRoleBO, String dbName) throws Exception {
		Staff staffClone = (Staff) staff.clone();
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 1、检查员工A的普通缓存是否创建。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(staff2.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bm != null, "普通缓存不存在创建出来的员工");
		// 2、检查数据库T_Staff，查看员工A是否正常创建。
		staff2.setIgnoreIDInComparision(true);
		staffClone.setIsFirstTimeLogin(1); // ... 创建成功的staff应当isFirsTimeLogin为1
		Assert.assertTrue(staff2.compareTo(staffClone) == 0, "DB不存在修改的员工");
		// 3、检查数据库T_StaffRole，查看是否创建了员工A关联的角色数据。
		StaffRole sr = new StaffRole();
		sr.setStaffID(staff2.getID());
		DataSourceContextHolder.setDbName(dbName);
		StaffRole retrieve1StaffRole = (StaffRole) staffRoleBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sr);
		if (staffRoleBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询一个员工角色失败，错误码=" + staffRoleBO.getLastErrorCode() + "，错误信息=" + staffRoleBO.getLastErrorMessage());
		}
		Assert.assertTrue(retrieve1StaffRole != null && staffClone.getRoleID() == retrieve1StaffRole.getRoleID(), "DB不存在创建出来的员工");
	}

	/** 1、检查员工A的普通缓存是否修改。 2、检查数据库T_Staff，查看员工A是否正常修改。 */
	public static void verifyUpdate(MvcResult mr, Staff staff, String dbName) throws Exception {
		Staff staffClone = (Staff) staff.clone();
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 1、检查员工A的普通缓存是否修改。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(staff2.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bm != null, "普通缓存不存在修改的员工");
		// 2、检查数据库T_Staff，查看员工A是否正常修改
		staffClone.setOperator(staff2.getOperator());
		Assert.assertTrue(staffClone.compareTo(staff2) == 0, "DB不存在修改的员工");
	}

	/** 1、检查员工A的普通缓存是否更新，F_Status是否为1(已删除)。
	 * 2、检查数据库T_Staff，查看员工A的F_Status是否修改为1(已删除)。 */
	public static void verifyDelete(Staff staff, StaffBO staffBO, StaffRoleBO staffRoleBO, String dbName) throws Exception {
		Staff staffClone = (Staff) staff.clone();
		staffClone.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		
		// 1、检查员工A的普通缓存是否更新，F_Status是否为1(已删除)。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(staffClone.getID(), BaseBO.SYSTEM, ecOut, dbName);
		Assert.assertTrue(bm != null, "普通缓存不存在删除的员工");
		// 2、检查数据库T_Staff，查看员工A的F_Status是否修改为1(已删除)。
		DataSourceContextHolder.setDbName(dbName);
		Staff staff2 = (Staff) staffBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, staffClone);
		if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询一个员工失败，错误码=" + staffBO.getLastErrorCode() + "，错误信息=" + staffBO.getLastErrorMessage());
		}
		Assert.assertTrue(staff2 != null && staff2.getStatus() == Staff.EnumStatusStaff.ESS_Resigned.getIndex(), "DB中删除的员工未进行状态更改");
	}

	/** 1、检查数据库T_Staff，检查员工A的F_Salt是否正确修改。2、检查普通缓存中的Staff的F_RoleID是否大于0。 */
	public static void verifyResetMyPassword(MvcResult mr, String pwdNew, StaffBO staffBO, String dbName) throws Exception {
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		DataSourceContextHolder.setDbName(dbName);
		staff2.setReturnSalt(1); // 使其返回salt
		Staff staff3 = (Staff) staffBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, staff2);
		if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询一个员工失败，错误码=" + staffBO.getLastErrorCode() + "，错误信息=" + staffBO.getLastErrorMessage());
		}
		String md5 = MD5Util.MD5(pwdNew + BaseAction.SHADOW);
		assertTrue(staff3.getSalt().equals(md5), "DB密码未进行修改");
		// 2、检查普通缓存中的Staff的F_RoleID是否大于0
		ErrorInfo ecOut = new ErrorInfo();
		Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(staff3.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找普通缓存失败，错误码=" + ecOut.getErrorCode().toString());
		}
		Assert.assertTrue(staff.getRoleID() > 0, "修改密码后，普通缓存中的Staff的F_RoleID必须大于0!");
	}

	/** 1、检查数据库T_Staff，检查被修改的员工的F_Salt是否正确修改。 2、检查操作者角色是否为店长。3、 检查普通缓存中的Staff的F_RoleID是否大于0。*/
	public static void verifyResetOtherPassword(MvcResult mr, HttpSession session, String pwdNew, StaffBO staffBO, StaffRoleBO staffRoleBO, String dbName) throws Exception {
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 1、检查数据库T_Staff，检查被修改的员工的F_Salt是否正确修改。
		DataSourceContextHolder.setDbName(dbName);
		staff2.setReturnSalt(1); // 使其返回salt
		Staff staff3 = (Staff) staffBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, staff2);
		if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询一个员工失败，错误码=" + staffBO.getLastErrorCode() + "，错误信息=" + staffBO.getLastErrorMessage());
		}
		String md5 = MD5Util.MD5(pwdNew + BaseAction.SHADOW);
		assertTrue(staff3.getSalt().equals(md5), "DB密码未进行修改");
		// 2、检查操作者角色是否为店长。
		Object object2 = session.getAttribute(EnumSession.SESSION_Staff.getName());
		StaffRole sr = new StaffRole();
		sr.setStaffID(((Staff) object2).getID());
		DataSourceContextHolder.setDbName(dbName);
		StaffRole retrieve1StaffRole = (StaffRole) staffRoleBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sr);
		if (staffBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "查询一个员工角色失败，错误码=" + staffRoleBO.getLastErrorCode() + "，错误信息=" + staffRoleBO.getLastErrorMessage());
		}
		Assert.assertTrue(retrieve1StaffRole != null, "DB中查询员工异常");
		assertTrue(retrieve1StaffRole.getRoleID() == Role.EnumTypeRole.ETR_Boss.getIndex(), "非店长,不能进行修改他人密码");
		// 3、 检查普通缓存中的Staff的F_RoleID是否大于0
		ErrorInfo ecOut = new ErrorInfo();
		Staff staff = (Staff) CacheManager.getCache(dbName, EnumCacheType.ECT_Staff).read1(staff3.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找普通缓存失败，错误码=" + ecOut.getErrorCode().toString());
		}
		Assert.assertTrue(staff.getRoleID() > 0, "修改密码后，普通缓存中的Staff的F_RoleID必须大于0!");
	}

	/** 1、如果首次登录，检查是否要强制修改密码。 2、首次登录成功后，检查数据库T_Staff，F_IsFirstTimeLgin是否为0。 */
	// public static void verifyLogin(MvcResult mr, MvcResult ret, StaffBO staffBO)
	// throws Exception {// ... 由于功能代码未进行首次登陆修改密码，导致检查点没法编写，以后再进行编写，先发jira.
	// JSONObject object =
	// JSONObject.fromObject(mr.getResponse().getContentAsString());
	// Staff staff2 = new Staff();
	// staff2.parse1(object.getString(BaseAction.KEY_Object));
	// Assert.assertTrue(staff2 != null, "解析异常");
	// // 1、如果首次登录，检查是否要强制修改密码。
	// // if (staff2.getIsFirstTimeLogin() != 0) {
	// // 需要进行修改密码
	// //
	// // }
	// // 2、首次登录成功后，检查数据库T_Staff，F_IsFirstTimeLgin是否为0。
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Staff staff3 = (Staff) staffBO.retrieve1Object(BaseBO.SYSTEM,
	// BaseBO.INVALID_CASE_ID, staff2);
	// Assert.assertTrue(staff3 != null, "DB中查询员工异常");
	// Assert.assertTrue(staff3.getIsFirstTimeLogin() == 0, "DB中查询员工异常");
	// }
}
