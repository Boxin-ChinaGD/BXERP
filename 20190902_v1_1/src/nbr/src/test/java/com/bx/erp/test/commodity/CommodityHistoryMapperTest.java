package com.bx.erp.test.commodity;

import static org.testng.Assert.assertTrue;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityHistory;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class CommodityHistoryMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE1: 根据日期查询
		Shared.caseLog("CASE1: 根据日期查询");
		CommodityHistory ch1 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch1.setQueryKeyword("");
		Map<String, Object> params = ch1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityHistoryMapper.retrieveN(params);
		//
		assertTrue(list.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE2: 根据商品ID查询
		Shared.caseLog("CASE2: 根据商品ID查询");
		CommodityHistory ch2 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch2.setCommodityID(1);
		ch2.setQueryKeyword("");
		Map<String, Object> params2 = ch2.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list2 = commodityHistoryMapper.retrieveN(params2);
		//
		assertTrue(list2.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest3() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE3: 根据员工ID查询
		Shared.caseLog("CASE3: 根据员工ID查询");
		CommodityHistory ch3 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch3.setStaffID(2);
		ch3.setQueryKeyword("");
		Map<String, Object> params3 = ch3.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list3 = commodityHistoryMapper.retrieveN(params3);
		//
		assertTrue(list3.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest4() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE4: 多个条件查询修改历史
		Shared.caseLog("CASE4: 多个条件查询修改历史");
		CommodityHistory ch4 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch4.setFieldName("零售价");
		ch4.setQueryKeyword("");
		Map<String, Object> params4 = ch4.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list4 = commodityHistoryMapper.retrieveN(params4);
		//
		assertTrue(list4.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest5() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE5: 根据修改字段查询
		Shared.caseLog("CASE5: 根据修改字段查询 ");
		CommodityHistory ch5 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch5.setQueryKeyword("可乐");
		Map<String, Object> params5 = ch5.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list5 = commodityHistoryMapper.retrieveN(params5);
		//
		assertTrue(list5.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest6() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE6: 所有条件进行查询修改历史
		Shared.caseLog("CASE6: 所有条件进行查询修改历史");
		CommodityHistory ch6 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch6.setCommodityID(1);
		ch6.setQueryKeyword("薯片");
		ch6.setFieldName("零售价");
		ch6.setStaffID(2);
		Map<String, Object> params6 = ch6.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list6 = commodityHistoryMapper.retrieveN(params6);
		//
		Assert.assertTrue(list6.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest7() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE7:根据商品名称进行名称查询
		Shared.caseLog("CASE7:根据商品名称进行名称查询");
		CommodityHistory ch7 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch7.setQueryKeyword("薯片");
		Map<String, Object> params7 = ch7.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list7 = commodityHistoryMapper.retrieveN(params7);
		//
		Assert.assertTrue(list7.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest8() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE8:根据商品条形码进行名称查询
		Shared.caseLog("CASE8:根据商品条形码进行名称查询");
		CommodityHistory ch8 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch8.setQueryKeyword("3548293");
		Map<String, Object> params8 = ch8.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch8);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list8 = commodityHistoryMapper.retrieveN(params8);
		//
		Assert.assertTrue(list8.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest9() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE9:根据商品条形码进行名称查询,QueryKeyword的长度大于32位少于64位 //...似乎没有什么文档要求必须>32位。找Giggs讨论
		Shared.caseLog("CASE9:根据商品条形码进行名称查询,QueryKeyword的长度大于32位少于64位");
		CommodityHistory ch9 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch9.setQueryKeyword("1111111122222222333333334444444455555555");
		Map<String, Object> params9 = ch9.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch9);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityHistoryMapper.retrieveN(params9);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest10() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE10:根据商品条形码进行名称查询,QueryKeyword的长度大于64位
		Shared.caseLog("CASE10:根据商品条形码进行名称查询,QueryKeyword的<=64位");
		CommodityHistory ch10 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch10.setQueryKeyword("11111111222222223333333344444444555555556666666677777777888888889");
		Map<String, Object> params10 = ch10.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch10);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		try {
			commodityHistoryMapper.retrieveN(params10); // 超长，抛出异常
			Assert.assertTrue(false, String.format(CommodityHistory.FIELD_ERROR_queryKeyword, ch10.getQueryKeyword()));
		} catch (Exception e) {
			Assert.assertTrue(true); // 抛出异常才符合期望
		}
	}

	@Test
	public void retrieveNTest11() throws CloneNotSupportedException, InterruptedException, ParseException {
		Shared.printTestMethodStartInfo();

		// CASE11:根据输入特殊字符“_”商品名称查询商品修改记录
		Shared.caseLog("CASE11:根据输入特殊字符“_”商品名称查询商品修改记录");
		// 创建商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity c = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);

		// 修改商品名称
		c.setName("小米电视剧_和" + System.currentTimeMillis());
		c.setOperatorStaffID(STAFF_ID4);
		//
		String error = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate = c.getUpdateParam(BaseBO.INVALID_CASE_ID, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase1 = (Commodity) commodityMapper.update(paramsForUpdate);// ...
		//
		Assert.assertNotNull(updateCommodityCase1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		// 更新后对比数据不一致返回-1，一致返回0
		c.setIgnoreIDInComparision(true);
		c.setIgnoreSlaveListInComparision(true);
		if (c.compareTo(updateCommodityCase1) != 0) {
			Assert.assertTrue(false, "对象的字段与DB读出的不相等");
		}
		// 模糊查询商品修改记录
		CommodityHistory ch = new CommodityHistory();
		ch.setQueryKeyword("_");
		Map<String, Object> params = ch.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> chList = commodityHistoryMapper.retrieveN(params);
		// 根据商品名称对比
		boolean flag = false;
		for (BaseModel bmHistory : chList) {
			CommodityHistory cHistory = (CommodityHistory) bmHistory;
			if (cHistory.getCommodityID() == c.getID()) {
				if (cHistory.getNewValue().contains("_")) {
					flag = true;
					break;
				}
			}
		}
		Assert.assertTrue(flag, "查询商品名称没有包含_特殊字符");
	}

	@Test
	public void retrieveNTest12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE12: 查看数据返回是否默认降序");
		CommodityHistory ch1 = BaseCommodityTest.DataInput.getCommodityHistory();
		ch1.setQueryKeyword("");
		Map<String, Object> params = ch1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityHistoryMapper.retrieveN(params);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		assertTrue(list.size() > 0);
		for (int i = 1; i < list.size(); i++) {
			assertTrue(list.get(i - 1).getID() > list.get(i).getID(), "数据返回错误（非降序）");
		}
	}
}
