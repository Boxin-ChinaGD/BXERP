package com.bx.erp.test;

import java.util.HashMap;
import java.util.List;
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
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.PosMapper;
import com.bx.erp.dao.ShopMapper;
import com.bx.erp.dao.StaffMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Shop;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;


@WebAppConfiguration
public class BaseShopTest extends BaseTestNGSpringContextTest {

	@Resource
	private WebApplicationContext wac;
	private MockMvc mvc;
	protected static HttpSession session;
	
	protected ShopMapper shopMapper;
	protected PosMapper posMapper;
	protected StaffMapper staffMapper;
	
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

		shopMapper = (ShopMapper) applicationContext.getBean("shopMapper");
		posMapper = (PosMapper) applicationContext.getBean("posMapper");
		staffMapper = (StaffMapper) applicationContext.getBean("staffMapper");
		
		Doctor_checkStatus();
		Doctor_checkCreate();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();

		Doctor_checkStatus();
		Doctor_checkCreate();
	}
	
	private void Doctor_checkStatus() {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		shopMapper.checkStatus(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的门店状态的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的门店状态的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}
	
	private void Doctor_checkCreate() {
		Shared.printTestClassEndInfo();
		
		Shop shop = new Shop();
		shop.setPageIndex(1);
		shop.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		shop.setDistrictID(BaseAction.INVALID_ID);
		
		String error = shop.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
		if (!error.equals("")) {
			System.out.println("对门店进行RN时，传入的的字段数据校验出现异常");
			System.out.println("shop=" + shop.toString());
		}
		//
		Map<String, Object> params = shop.getRetrieveNParam(BaseBO.INVALID_CASE_ID, shop);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = shopMapper.retrieveN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (list.size() == 0 || list == null) {
			System.out.println("RN读出的门店信息为空");
		}
		for (BaseModel bm : list) {
			Shop s = (Shop) bm;
			error  = s.checkCreate(BaseBO.INVALID_CASE_ID);
			if (!error.equals("")) {
				System.out.println(s.getID() + "号门店数据验证出现异常");
				System.out.println("s=" + s.toString());
			}
		}
	}
}
