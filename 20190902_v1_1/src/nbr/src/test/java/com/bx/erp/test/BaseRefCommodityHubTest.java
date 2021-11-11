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
import com.bx.erp.dao.commodity.RefCommodityHubMapper;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class BaseRefCommodityHubTest extends BaseTestNGSpringContextTest {

	@Resource
	private WebApplicationContext wac;
	private MockMvc mvc;
	protected static HttpSession session;
	
	protected RefCommodityHubMapper refCommodityHubMapper;
	
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

		refCommodityHubMapper = (RefCommodityHubMapper) applicationContext.getBean("refCommodityHubMapper");
	
		Doctor_checkName();
		Doctor_checkBarcode();
		Doctor_checkPriceRetail();
		Doctor_checkShelfLife();
		Doctor_checkSpecification();
		Doctor_checkType();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
		
		Doctor_checkName();
		Doctor_checkBarcode();
		Doctor_checkPriceRetail();
		Doctor_checkShelfLife();
		Doctor_checkSpecification();
		Doctor_checkType();
	}
	
	private void Doctor_checkName() {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		refCommodityHubMapper.checkName(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的参考商品库的商品名称不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的参考商品库的商品名称的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}
	
	private void Doctor_checkBarcode() {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		refCommodityHubMapper.checkBarcode(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的参考商品库的条形码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的参考商品库的条形码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}
	
	private void Doctor_checkPriceRetail() {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		refCommodityHubMapper.checkPriceRetail(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的参考商品库的零售价不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的参考商品库的零售价的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}
	
	private void Doctor_checkShelfLife() {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		refCommodityHubMapper.checkShelfLife(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的参考商品库的保质期不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的参考商品库的保质期的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}
	
	private void Doctor_checkSpecification() {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		refCommodityHubMapper.checkSpecification(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的参考商品库的规格不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的参考商品库的规格的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}
	
	private void Doctor_checkType() {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		refCommodityHubMapper.checkType(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的参考商品库的商品类型不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的参考商品库的商品类型的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}
}
