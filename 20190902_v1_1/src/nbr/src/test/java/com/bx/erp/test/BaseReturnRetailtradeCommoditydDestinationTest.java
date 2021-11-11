package com.bx.erp.test;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.ReturnRetailtradeCommoditydDestinationMapper;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class BaseReturnRetailtradeCommoditydDestinationTest extends BaseTestNGSpringContextTest {
	@Resource
	private WebApplicationContext wac;
	private MockMvc mvc;
	protected static HttpSession session;
	
	protected ReturnRetailtradeCommoditydDestinationMapper returnRetailtradeCommoditydDestinationMapper;
	
	
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("utf-8");
		encodingFilter.setForceEncoding(true);

		mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(encodingFilter, "/*").build();
		try {
			session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		returnRetailtradeCommoditydDestinationMapper =  (ReturnRetailtradeCommoditydDestinationMapper) applicationContext.getBean("returnRetailtradeCommoditydDestinationMapper");

		Doctor_checkNO();
		Doctor_checkWarehousingID();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	
		Doctor_checkNO();
		Doctor_checkWarehousingID();
	}
   
	private void Doctor_checkNO() {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnRetailtradeCommoditydDestinationMapper.checkNO(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查退货去向表数量的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查退货去向表数量的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}
	
	private void Doctor_checkWarehousingID() {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnRetailtradeCommoditydDestinationMapper.checkWarehousingID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查退货去向表入库ID的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查退货去向表入库ID的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}
	
}
