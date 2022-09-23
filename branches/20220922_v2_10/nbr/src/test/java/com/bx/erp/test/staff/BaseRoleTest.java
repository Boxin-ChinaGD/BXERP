package com.bx.erp.test.staff;

import java.util.List;
import java.util.Map;

import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Role;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseRoleTest extends BaseMapperTest {
	public static class DataInput {
		private static Role roleInput = null;

		public static final Role getRole() throws Exception {
			roleInput = new Role();
			roleInput.setName("测试" + System.currentTimeMillis() % 1000000);

			return (Role) roleInput.clone();
		}
	}

	public static Role createViaMapper(Role role) throws InterruptedException {
		String err = role.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);

		Map<String, Object> paramsForCreate = role.getCreateParam(BaseBO.INVALID_CASE_ID, role);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		//
		Role roleCreate = (Role) roleMapper.create(paramsForCreate);

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		role.setIgnoreIDInComparision(true);
		if (role.compareTo(roleCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		err = roleCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);

		return roleCreate;
	}

	public static void deleteViaMapper(Role role) {
		Map<String, Object> paramsDelete = role.getDeleteParam(BaseBO.INVALID_CASE_ID, role);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		roleMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		List<BaseModel> list = retrieveNViaMapper(role);
		Assert.assertTrue(list.size() == 0);
	}

	public static List<BaseModel> retrieveNViaMapper(Role role) {
		Map<String, Object> params = role.getRetrieveNParam(BaseBO.INVALID_CASE_ID, role);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> roleRetrieveN = roleMapper.retrieveN(params);
		//
		Assert.assertTrue(roleRetrieveN.size() >= 0//
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err;
		for (BaseModel bm : roleRetrieveN) {
			err = ((Role) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		return roleRetrieveN;
	}
	
	public static Role updateViaMapper(Role role) {
		String err = role.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		
		Map<String, Object> paramsUpdate = role.getUpdateParam(BaseBO.INVALID_CASE_ID, role);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Role roleUpdate = (Role) roleMapper.update(paramsUpdate);
		
		Assert.assertTrue(roleUpdate != null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		err = roleUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		if (role.compareTo(roleUpdate) != 0) {
			Assert.assertTrue(false, "修改角色失败");
		}
		return roleUpdate;
	}
	
//	public static Role updateRoleViaAction(Role role, MockMvc mvc, HttpSession session) throws Exception {
//		MvcResult mr = mvc.perform(//
//				post("/role/updateEx.bx")//
//						.param(Role.field.getFIELD_NAME_ID(), String.valueOf(role.getID()))//
//						.param(Role.field.getFIELD_NAME_name(), role.getName())//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr);
//
//		return (Role) Shared.parse1Object(mr, role);
//	}
	
//	public static void deleteRoleViaAction(Role role, MockMvc mvc, HttpSession session) throws Exception {
//		MvcResult mr = mvc.perform(//
//				get("/role/deleteEx.bx?" + Role.field.getFIELD_NAME_ID() + "=" + role.getID() + "&" + Role.field.getFIELD_NAME_bForceDelete() + "=" + EnumBoolean.EB_Yes.getIndex())//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) session))//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr);
//	}
	
//	public static Role createRoleViaAction(Role role, MockMvc mvc, HttpSession session) throws Exception {
//		MvcResult mr = mvc.perform(//
//				post("/role/createEx.bx")//
//						.param(Role.field.getFIELD_NAME_name(), role.getName())//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr);
//		
//		return (Role) Shared.parse1Object(mr, role);
//	}
}
