package com.bx.erp.test.wx;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.StaffBO;
import com.bx.erp.action.bo.StaffRoleBO;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.StaffCP;

@WebAppConfiguration
public class BaseStaffTest{
	@Resource
	private WebApplicationContext wac;
	private MockMvc mvc;
	protected static HttpSession session;

	@Resource
	protected StaffBO staffBO;
	@Resource
	protected StaffRoleBO staffRoleBO;

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("utf-8");
		encodingFilter.setForceEncoding(true);
		mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(encodingFilter, "/*").build();
		try {
			Shared.resetPOS(mvc, 1);
			session = Shared.getPosLoginSession(mvc, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	public static class DataInput {
		private static Staff staffInput = null;

		public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Staff s) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType)//
					.param(Staff.field.getFIELD_NAME_ID(), String.valueOf(s.getID())) //
					.param(Staff.field.getFIELD_NAME_phone(), s.getPhone())//
					.param(Staff.field.getFIELD_NAME_name(), s.getName())//
					.param(Staff.field.getFIELD_NAME_ICID(), s.getICID())//
					.param(Staff.field.getFIELD_NAME_weChat(), s.getWeChat())//
					.param(Staff.field.getFIELD_NAME_pwdEncrypted(), s.getPwdEncrypted())//
					.param(Staff.field.getFIELD_NAME_passwordExpireDate(), String.valueOf(s.getPasswordExpireDate()))//
					.param(Staff.field.getFIELD_NAME_isFirstTimeLogin(), String.valueOf(s.getIsFirstTimeLogin()))//
					.param(Staff.field.getFIELD_NAME_shopID(), String.valueOf(s.getShopID()))//
					.param(Staff.field.getFIELD_NAME_departmentID(), String.valueOf(s.getDepartmentID()))//
					.param(Staff.field.getFIELD_NAME_roleID(), String.valueOf(s.getRoleID()))//
					.param(Staff.field.getFIELD_NAME_returnSalt(), String.valueOf(s.getReturnSalt()))//
					.param(Staff.field.getFIELD_NAME_status(), String.valueOf(s.getStatus()));//
			return builder;
		}

		public static final Staff getStaff() throws Exception {
			staffInput = new Staff();
			staffInput.setPhone(Shared.getValidStaffPhone());
			Thread.sleep(100);
			staffInput.setName("店员" + System.currentTimeMillis() % 1000000);//
			Thread.sleep(100);
			Thread.sleep(100);
			staffInput.setICID(Shared.getValidICID()); //
			Thread.sleep(100);
			staffInput.setWeChat("rr1" + System.currentTimeMillis() % 1000000);//
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default4);//
			staffInput.setPasswordExpireDate(sdf.parse("2018/02/15 12:30:45"));//
			staffInput.setSalt(Shared.getFakedSalt());
			staffInput.setIsFirstTimeLogin(EnumBoolean.EB_Yes.getIndex()); //
			staffInput.setShopID(1);//
			staffInput.setDepartmentID(1);//
			staffInput.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());//

			return (Staff) staffInput.clone();
		}
	}

	// 创建的staff密码默认为000000
	public Staff createStaff(Staff staff) throws Exception {
		MvcResult ret = getToken(session, staff.getPhone(), Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		staff.setPwdEncrypted(encrypt);
		MvcResult mr = mvc.perform(//
				DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		StaffCP.verifyCreate(mr, staff, staffRoleBO, Shared.DBName_Test);

		return (Staff) Shared.parse1Object(mr, staff, BaseAction.KEY_Object);
	}

	protected MvcResult getToken(HttpSession session, String phone, int forModifyPassword) throws Exception {
		MvcResult ret = mvc.perform(post("/staff/getTokenEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) session) //
				.param(Staff.field.getFIELD_NAME_phone(), phone)//
				.param(Staff.field.getFIELD_NAME_returnSalt(), String.valueOf(forModifyPassword))//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(ret);

		return ret;
	}

	public void deleteStaff(Staff s) throws Exception {
		MvcResult mr2 = mvc.perform(//
				get("/staff/deleteEx.bx?" + Staff.field.getFIELD_NAME_ID() + "=" + s.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		//
		Shared.checkJSONErrorCode(mr2);
		Staff staffClone = (Staff) s.clone();
		StaffCP.verifyDelete(staffClone, staffBO, staffRoleBO, Shared.DBName_Test);
	}
}
