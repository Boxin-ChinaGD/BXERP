package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
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
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class CommodityHistoryActionTest extends BaseActionTest {
	private MockHttpSession session;

	@BeforeClass
	public void setup() {
		super.setUp();
		
		try {
//			session = (MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfAssistManager);
			session = sessionBoss;
		} catch (Exception e) {
			System.out.println("登录失败：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testIndex() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(//
				get("/commodityHistory.bx")//
						.session(session)//
		)//
				.andExpect(status().isOk());
	}

	@Test
	public void testRetriveNEx_1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" CASE1: 查询具体时间段的修改历史 ");
		MvcResult mr = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx") //
						.param(CommodityHistory.field.getFIELD_NAME_date1(), "2018/01/01") //
						.param(CommodityHistory.field.getFIELD_NAME_date2(), "2020/01/01")//
						.param(CommodityHistory.field.getFIELD_NAME_queryKeyword(), "可乐") //
						.session(session) //
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
	}

	@Test
	private void testRetriveNEx_2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" CASE2:查询某个员工的修改历史 ");
		MvcResult mr2 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "2")//
						.session(session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
	}

	@Test
	private void testRetriveNEx_3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" CASE3:查询某个商品的修改历史 ");
		MvcResult mr3 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.session(session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
	}

	@Test
	private void testRetriveNEx_4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" CASE4: 查询某个字段的修改历史");
		MvcResult mr4 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), "零售价")//
						.session(session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);
	}

	@Test
	private void testRetriveNEx_5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:所有条件进行查询修改历史");
		MvcResult mr5 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_queryKeyword(), "薯片")//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), "零售价")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), "1")//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "2")//
						.session(session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr5);
	}

	@Test
	private void testRetriveNEx_6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6:多个条件查询修改历史  ");
		MvcResult mr6 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_fieldName(), "零售价")//
						.param(CommodityHistory.field.getFIELD_NAME_commodityID(), "1")//
						.param(CommodityHistory.field.getFIELD_NAME_staffID(), "2")//
						.session(session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr6);
	}

	@Test
	private void testRetriveNEx_7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7:根据商品名称进行名称查询 ");
		MvcResult mr7 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_queryKeyword(), "薯片")//
						.session(session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr7);
	}

	@Test
	private void testRetriveNEx_8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8:根据商品条形码进行名称查询");
		MvcResult mr8 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_queryKeyword(), "3548293894545")//
						.session(session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr8);
	}

	@Test
	public void testRetriveNEx_9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" CASE9:没有权限进行查询 ");

		MvcResult mr9 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx") //
						.param(CommodityHistory.field.getFIELD_NAME_date1(), "2018/01/01") //
						.param(CommodityHistory.field.getFIELD_NAME_date2(), "2020/01/02")//
						.param(CommodityHistory.field.getFIELD_NAME_queryKeyword(), "可乐") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier)) //
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_NoPermission);
	}

	@Test
	private void testRetriveNEx_10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE10:根据商品条形码进行名称查询，长度大于32位少于64位");
		MvcResult mr10 = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_queryKeyword(), "1111111122222222333333334444444455555555")//
						.session(session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr10);
	}

	@Test
	private void testRetriveNEx_11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE11:根据输入特殊字符“_”商品名称查询商品修改记录");
		// 创建商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity c = BaseCommodityTest.createCommodityViaAction(comm1, mvc, session, mapBO, Shared.DBName_Test);

		// 修改商品名称
		c.setName("小米action电视剧_和" + System.currentTimeMillis());
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

		// 根据商品名称输入“_”模糊查询商品修改记录
		MvcResult mr = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx")//
						.param(CommodityHistory.field.getFIELD_NAME_queryKeyword(), "_")//
						.session(session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

		// JSON解析商品名称
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<String> listName = JsonPath.read(o, "$.objectList[*].commodityName");
		// 用商品名称包含“_”特殊字符对比
		for (String result : listName) {
			Assert.assertTrue(result.contains("_"), "查询商品名称没有包含_特殊字符");
		}

	}

	@Test
	public void testRetriveNEx_12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE12: 查看数据返回是否默认降序");
		// 创建商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity c = BaseCommodityTest.createCommodityViaAction(comm1, mvc, session, mapBO, Shared.DBName_Test);
		// 修改商品
		c.setName("薯片" + Shared.generateStringByTime(6));
		c.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.updateCommodityViaMapper(c);
		// 再次修改商品
		c.setName("薯片2" + Shared.generateStringByTime(6));
		BaseCommodityTest.updateCommodityViaMapper(c);

		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3); //这里较为奇怪的是不能用yyyy-MM-dd的格式，或许和特定的计算机有关
		Date dtStart = new Date();
		//
		Calendar cal = Calendar.getInstance();
		cal.setTime(dtStart);
		cal.add(Calendar.DATE, 1);
		Date dtEnd = cal.getTime();

		MvcResult mr = mvc.perform(//
				post("/commodityHistory/retrieveNEx.bx") //
						.param(CommodityHistory.field.getFIELD_NAME_date1(), sdf.format(dtStart)) //
						.param(CommodityHistory.field.getFIELD_NAME_date2(), sdf.format(dtEnd))//
						.session(session) //
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<BaseModel> bmList = new CommodityHistory().parseN(o.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (int i = 1; i < bmList.size(); i++) {
			Assert.assertTrue(((CommodityHistory) bmList.get(i - 1)).getID() > ((CommodityHistory) bmList.get(i)).getID(), "数据返回错误（非降序）");
		}
	}
}
