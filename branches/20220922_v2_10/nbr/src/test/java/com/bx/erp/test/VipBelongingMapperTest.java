//package com.bx.erp.test;
//
//import java.util.List;
//import java.util.Map;
//
//import org.testng.Assert;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.dao.VipBelongingMapper;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.VipBelonging;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.util.DataSourceContextHolder;
//
//public class VipBelongingMapperTest extends BaseTestNGSpringContextTest {
//	protected VipBelongingMapper mapper;
//
//	@BeforeClass
//	public void setup() {
//		Shared.printTestClassStartInfo();
//
//		mapper = (VipBelongingMapper) applicationContext.getBean("vipBelongingMapper");
//	}
//
//	@AfterClass
//	public void tearDown() {
//		Shared.printTestClassEndInfo();
//	}
//
//	@Test
//	public void retrieveNTest() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		System.out.println("\n------------------------ RetrieveN VipBelonging Test ------------------------");
//
//		VipBelonging vipBelonging = new VipBelonging();
//		Map<String, Object> params = vipBelonging.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vipBelonging);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<BaseModel> vipBelongingList = mapper.retrieveN(params);
//
//		Assert.assertTrue(vipBelongingList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询失败！");
//	}
//}
