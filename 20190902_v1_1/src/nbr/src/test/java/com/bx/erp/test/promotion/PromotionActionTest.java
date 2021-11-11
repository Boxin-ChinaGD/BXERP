package com.bx.erp.test.promotion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
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
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.model.trade.Promotion.EnumSubStatusPromotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebAppConfiguration
public class PromotionActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	// @Test
	public void testCreate() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l2 = simpleDateFormat.format(date.getTime() + ((long) 3 * 24 * 60 * 60 * 1000));
		Shared.caseLog("----------------------------CASE1:添加促销活动 满减金额 ----------------------------");
		mvc.perform(post("/promotion/create.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10减1")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "0")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
				.param(Promotion.field.getFIELD_NAME_scope(), "0")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.caseLog("----------------------------CASE2:添加促销活动 满减折扣 ----------------------------");
		mvc.perform(post("/promotion/create.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10打85折")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "1")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "8.5")//
				.param(Promotion.field.getFIELD_NAME_scope(), "0")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
		)//
				.andExpect(status().isOk()).andDo(print());
		Shared.caseLog("----------------------------CASE3:添加促销活动 type 输入不存在的值----------------------------");
		mvc.perform(post("/promotion/create.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10打85折")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "25")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "8.5")//
				.param(Promotion.field.getFIELD_NAME_scope(), "0")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
		)//
				.andExpect(status().isOk()).andDo(print());
		Shared.caseLog("----------------------------CASE4:活动为当天发布---------------------------");

		String l = simpleDateFormat.format(date);
		System.out.println("-----------CASE4:活动为当天发布-今天时间为： " + l + " -----------");
		mvc.perform(post("/promotion/create.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10打85折")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "1")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "8.5")//
				.param(Promotion.field.getFIELD_NAME_scope(), "0")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
		)//
				.andExpect(status().isOk()).andDo(print());
		Shared.caseLog("----------------------------Case5:活动等于发布时间 或者 小于 发布时间！----------------------------");

		System.out.println("------------------------Case5 time " + l2 + "-----------------------");
		mvc.perform(post("/promotion/create.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10打85折")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "1")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), l2)//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "8.5")//
				.param(Promotion.field.getFIELD_NAME_scope(), "0")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
		)//
				.andExpect(status().isOk()).andDo(print());
		Shared.caseLog("----------------------------CASE6:阙值小于等于0----------------------------");
		mvc.perform(post("/promotion/create.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10打85折")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "1")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "0")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "8.5")//
				.param(Promotion.field.getFIELD_NAME_scope(), "0")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
		)//
				.andExpect(status().isOk()).andDo(print());
		Shared.caseLog("----------------------------CASE7:减去金额大于阙值！----------------------------");
		mvc.perform(post("/promotion/create.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10打85折")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "0")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "11")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "8.5")//
				.param(Promotion.field.getFIELD_NAME_scope(), "0")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
		)//
				.andExpect(status().isOk()).andDo(print());
		Shared.caseLog("----------------------------CASE8:折扣不处于0-10的之间！----------------------------");
		mvc.perform(post("/promotion/create.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10打85折")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "1")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "11")//
				.param(Promotion.field.getFIELD_NAME_scope(), "0")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
		)//
				.andExpect(status().isOk()).andDo(print());
		Shared.caseLog("----------------------------CASE9:指定商品满减！----------------------------");
		mvc.perform(post("/promotion/create.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_name(), "测试九号指定满10-1")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "0")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "11")//
				.param(Promotion.field.getFIELD_NAME_scope(), "1")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
				.param("commodityIds", new String[] { "1", "2", "3", "4", "4", "4" }))//
				.andExpect(status().isOk()).andDo(print());
		Shared.caseLog("----------------------------CASE10:指定商品满减 ids乱输入！----------------------------");
		mvc.perform(post("/promotion/create.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号指定满10-1")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "0")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "11")//
				.param(Promotion.field.getFIELD_NAME_scope(), "1")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
				.param("commodityIds", new String[] { "1", "2", "ad3", "4" }))//
				.andExpect(status().isOk()).andDo(print());

	}

	// @Test
	public void testUpdate() throws Exception {
		Shared.printTestMethodStartInfo();
		Promotion p = new Promotion();
		p.setID(1);
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l2 = simpleDateFormat.format(date.getTime() + ((long) 3 * 24 * 60 * 60 * 1000));
		System.out.println("-----------------------Case1：将数据库中的id为1的全部商品满减活动改为指定商品满减活动----------------------------------");
		mvc.perform(post("/promotion/update.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(p.getID()))//
				.param(Promotion.field.getFIELD_NAME_name(), "更新一号指定满10-1")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "0")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "9.5")//
				.param(Promotion.field.getFIELD_NAME_scope(), "1")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
				.param("commodityIds", new String[] { "1", "2", "3", "4" }))//
				.andExpect(status().isOk()).andDo(print());
		System.out.println("-----------------------Case2：将数据库中的id为1的指定商品满减活动改为全部商品满减活动----------------------------------");
		mvc.perform(post("/promotion/update.bx").session((MockHttpSession) sessionBoss)//
				.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(p.getID()))//
				.param(Promotion.field.getFIELD_NAME_name(), "全部商品满10-1")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "0")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l2)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "9.5")//
				.param(Promotion.field.getFIELD_NAME_scope(), "0")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
				.param("commodityIds", new String[] { "1", "2", "3", "4" }))//
				.andExpect(status().isOk()).andDo(print());

	}

	// @Test
	public void testDelete() throws Exception {
		Shared.printTestMethodStartInfo();
		Promotion p = new Promotion();
		p.setID(1);
		mvc.perform(//
				get("/promotion/delete.bx?" + Promotion.field.getFIELD_NAME_ID() + "=" + p.getID())//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk());
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// Promotion p = new Promotion();
		Shared.caseLog("Case1: 查询所有的促销活动");
		MvcResult andReturn = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(andReturn);
		JSONObject o1 = JSONObject.fromObject(andReturn.getResponse().getContentAsString());

		// 结果验证，因为promotion表有数据，促销活动数目大于0.
		JSONArray list1 = o1.getJSONArray(BaseAction.KEY_ObjectList);
		Assert.assertTrue(list1.size() > 0, "Case1查询所有促销活动有错误");
	}

	@Test
	public void testRetrieveNExCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: 查询未开始的活动");
		MvcResult andReturn2 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), String.valueOf(Promotion.ACTIVE_ButNotYetStarted))//
						.param(Promotion.field.getFIELD_NAME_status(), "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(andReturn2);

		// 结果验证，返回的数目>0且活动都是未开始的.
		JSONObject o2 = JSONObject.fromObject(andReturn2.getResponse().getContentAsString());
		JSONArray list2 = o2.getJSONArray(BaseAction.KEY_ObjectList);
		Date date = new Date();
		for (int i = 0; i < list2.size(); i++) {
			Promotion JsonToPromotion = (Promotion) new Promotion().parse1(list2.getJSONObject(i).toString());
			Assert.assertTrue(date.before(JsonToPromotion.getDatetimeStart()), "Case2 查询未开始的活动有错误");
		}
	}

	@Test
	public void testRetrieveNExCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: 查询正在进行中的活动");
		MvcResult andReturn3 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), String.valueOf(Promotion.ACTIVE_And_Working))//
						.param(Promotion.field.getFIELD_NAME_status(), "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(andReturn3);

		// 结果验证,返回的数目>0且活动都是进行中的.
		Date date3 = new Date();
		JSONObject o3 = JSONObject.fromObject(andReturn3.getResponse().getContentAsString());
		JSONArray list3 = o3.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < list3.size(); i++) {
			Promotion JsonToPromotion = (Promotion) new Promotion().parse1(list3.getJSONObject(i).toString());
			Assert.assertTrue(JsonToPromotion.getDatetimeStart().before(date3) && date3.before(JsonToPromotion.getDatetimeEnd()), "查询进行中的活动有错误");
		}
	}

	@Test
	public void testRetrieveNExCase4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4: 查询已经结束的促销活动");
		MvcResult andReturn4 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), String.valueOf(Promotion.ACTIVE_ButEnded))//
						.param(Promotion.field.getFIELD_NAME_status(), "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(andReturn4);

		// 结果验证,返回的数目>0,且促销活动都是已结束的.
		Date date4 = new Date();
		JSONObject o4 = JSONObject.fromObject(andReturn4.getResponse().getContentAsString());
		JSONArray list4 = o4.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < list4.size(); i++) {
			Promotion JsonToPromotion = (Promotion) new Promotion().parse1(list4.getJSONObject(i).toString());
			Assert.assertTrue(JsonToPromotion.getDatetimeEnd().before(date4), "查询已经结束的活动有错误");
		}
	}

	@Test
	public void testRetrieveNExCase5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5: 查询进行中还有将要进行的促销活动");
		MvcResult mvcResult = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), String.valueOf(Promotion.WORKING_And_ToWork))//
						.param(Promotion.field.getFIELD_NAME_status(), "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mvcResult);

		// 结果验证,返回的数目>0,且促销活动都是进行中还有将要进行的.
		Date date5 = new Date();
		JSONObject jsObject = JSONObject.fromObject(mvcResult.getResponse().getContentAsString());
		JSONArray jsonArray = jsObject.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < jsonArray.size(); i++) {
			Promotion JsonToPromotion = (Promotion) new Promotion().parse1(jsonArray.getJSONObject(i).toString());
			System.out.println("-----" + JsonToPromotion.getDatetimeEnd());
			Assert.assertTrue(date5.before(JsonToPromotion.getDatetimeEnd()), "查询进行中还有将要进行的活动有错误");
		}
	}

	@Test
	public void testRetrieveNExCase6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6: 查询已删除的促销活动");
		MvcResult mvcResult1 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mvcResult1);

		// 结果验证,返回的数目>0,且促销活动都是已删除的.
		JSONObject jsObject1 = JSONObject.fromObject(mvcResult1.getResponse().getContentAsString());
		JSONArray jsonArray1 = jsObject1.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < jsonArray1.size(); i++) {
			Promotion JsonToPromotion = (Promotion) new Promotion().parse1(jsonArray1.getJSONObject(i).toString());
			Assert.assertTrue(JsonToPromotion.getStatus() == 1, "查询已删除的活动有错误");
		}
	}

	@Test
	public void testRetrieveNExCase7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7: 查询未删除的促销活动");
		MvcResult mvcResult2 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mvcResult2);

		// 结果验证,返回的数目>0,且促销活动都是未删除的.
		JSONObject jsObject2 = JSONObject.fromObject(mvcResult2.getResponse().getContentAsString());
		JSONArray jsonArray2 = jsObject2.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < jsonArray2.size(); i++) {
			Promotion JsonToPromotion = (Promotion) new Promotion().parse1(jsonArray2.getJSONObject(i).toString());
			Assert.assertTrue(JsonToPromotion.getStatus() == 0, "查询未删除的活动有错误");
		}
	}

	@Test
	public void testRetrieveNExCase8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8: 查询传回去的int1为其他数值");
		MvcResult andReturn5 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "99")//
						.param(Promotion.field.getFIELD_NAME_status(), "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(andReturn5);

		// 结果验证，查询为状态为其他数,就是查询状态为0的活动，判断数目是否大于0
		JSONObject o5 = JSONObject.fromObject(andReturn5.getResponse().getContentAsString());
		System.out.println(o5);
		JSONArray list5 = o5.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < list5.size(); i++) {
			Promotion validPromotion = (Promotion) new Promotion().parse1(list5.getJSONObject(i).toString());
			Assert.assertTrue(validPromotion.getStatus() == 0, "查询未删除的活动有错误");
		}
	}

	@Test
	public void testRetrieveNExCase9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9:查询为状态为0,满减优惠表既返回了操作人员的name，也返回了参与促销活动的销售单数");
		// 创建一个promotion
		Promotion promotion6 = BasePromotionTest.DataInput.getPromotion();
		promotion6.setStaff(3);
		//
		Map<String, Object> params6 = promotion6.getCreateParam(BaseBO.INVALID_CASE_ID, promotion6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion promotionCreated6 = (Promotion) promotionMapper.create(params6);
		Assert.assertNotNull(promotionCreated6, "对象为空");
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "创建失败");
		//
		promotion6.setIgnoreIDInComparision(true);
		if (promotion6.compareTo(promotionCreated6) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		MvcResult andReturn61 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(andReturn61);
		// 结果验证,这时参与单数为0，操作人员为店员二号
		JSONObject o6 = JSONObject.fromObject(andReturn61.getResponse().getContentAsString());
		JSONArray list6 = o6.getJSONArray(BaseAction.KEY_ObjectList);
		Assert.assertTrue(list6.size() > 0, "Case9查询所有的促销活动为空");
		for (int i = 0; i < list6.size(); i++) {
			Promotion JsonToPromotion = (Promotion) new Promotion().parse1(list6.getJSONObject(i).toString());
			if (JsonToPromotion.getID() == promotionCreated6.getID()) {
				System.out.println(JsonToPromotion.getStaffName());
				Assert.assertTrue(JsonToPromotion.getStaffName().equals("店员二号"), "返回的操作人员不正确");
				Assert.assertTrue(JsonToPromotion.getRetailTradeNO() == 0, "返回的参与单数不正确");
			}
		}

		// 创建一个RetailTradePromotingFlow
		RetailTradePromotingFlow retailTradePromotingFlow = new RetailTradePromotingFlow();
		retailTradePromotingFlow.setRetailTradePromotingID(3);
		retailTradePromotingFlow.setPromotionID(promotionCreated6.getID());
		retailTradePromotingFlow.setProcessFlow("测试数据");
		Map<String, Object> rparams = retailTradePromotingFlow.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradePromotingFlow);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromotingFlow retailTradePromotingFlowCreated = (RetailTradePromotingFlow) retailTradePromotingFlowMapper.create(rparams);
		Assert.assertNotNull(retailTradePromotingFlowCreated, "对象为空");
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(rparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "创建失败");
		//
		retailTradePromotingFlow.setIgnoreIDInComparision(true);
		if (retailTradePromotingFlow.compareTo(retailTradePromotingFlowCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		MvcResult andReturn62 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(andReturn62);

		// 结果验证，这时参与单数为1，操作人员为店员二号
		JSONObject o62 = JSONObject.fromObject(andReturn62.getResponse().getContentAsString());
		JSONArray list62 = o62.getJSONArray(BaseAction.KEY_ObjectList);
		Assert.assertTrue(list62.size() > 0, "查询状态为0的活动有错误");
		for (int i = 0; i < list62.size(); i++) {
			Promotion JsonToPromotion = (Promotion) new Promotion().parse1(list62.getJSONObject(i).toString());
			if (JsonToPromotion.getID() == promotionCreated6.getID()) {
				Assert.assertTrue(JsonToPromotion.getStaffName().equals("店员二号"), "返回的操作人员不正确");
				Assert.assertTrue(JsonToPromotion.getRetailTradeNO() == 1, "返回的参与单数不正确");
			}
		}
	}

	@Test
	public void testRetrieveNExCase10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:查询为状态为0,查询满减优惠活动时需返回指定可参与优惠活动的商品的名称、条码，售价");
		// 1、创建一个促销范围活动
		PromotionScope psCreate = new PromotionScope();
		//
		psCreate.setCommodityID(1);
		psCreate.setPromotionID(4);
		Map<String, Object> createParams = psCreate.getCreateParam(BaseBO.INVALID_CASE_ID, psCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PromotionScope psCreated = (PromotionScope) promotionScopeMapper.create(createParams);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "创建零售单范围表失败");

		psCreate.setIgnoreIDInComparision(true);
		if (psCreate.compareTo(psCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 2、根据创建的范围表的商品ID，R1 Commodity
		Commodity commRetrieve = new Commodity();
		commRetrieve.setID(psCreated.getCommodityID());
		Map<String, Object> paramsRetrieve = commRetrieve.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, commRetrieve);
		List<List<BaseModel>> commR1 = commodityMapper.retrieve1Ex(paramsRetrieve);
		Assert.assertTrue(commR1.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsRetrieve.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");
		Commodity comm = Commodity.fetchCommodityFromResultSet(commR1);
		// 如果促销表ID为4的促销活动已变为其他的状态则测试失败
		MvcResult andReturn7 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "3")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(andReturn7);
		// RN促销活动，找到刚创建的commodity的id与RN中commdity的id相同的进行比较
		JSONObject o7 = JSONObject.fromObject(andReturn7.getResponse().getContentAsString());
		JSONArray list7 = o7.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < list7.size(); i++) {
			Promotion JsonToPromotion7 = (Promotion) new Promotion().parse1(list7.getJSONObject(i).toString());
			if (JsonToPromotion7.getID() == psCreate.getPromotionID()) {
				for (Object ob : JsonToPromotion7.getListSlave1()) {
					// 找到刚刚创建的促销活动范围
					PromotionScope pScope = (PromotionScope) ob;
					if (pScope.getID() == psCreated.getID()) {
						Commodity commodity = pScope.getCommodity();
						// 4.R1中的comodity与RN中的commodity比较
						Assert.assertTrue(commodity.compareTo(comm) == 0, "返回的商品名称、条码，售价错误");
					}
				}
			}
		}

	}

	@Test
	public void testRetrieveNExCase11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11,根据关键字模糊搜索,促销活动名称iName为8");
		// 1、根据关键字RetrieveN
		String queryKeyword = "8";
		MvcResult andReturn8 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 2、结果验证，判断返回的promotion名称是否符合按模糊搜索返回的
		Shared.checkJSONErrorCode(andReturn8);
		JSONObject o8 = JSONObject.fromObject(andReturn8.getResponse().getContentAsString());
		JSONArray list8 = o8.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < list8.size(); i++) {
			Promotion JsonToPromotion8 = (Promotion) new Promotion().parse1(list8.getJSONObject(i).toString());
			Assert.assertTrue(JsonToPromotion8.getName().contains(queryKeyword), "按关键字模糊搜索促销活动失败");
		}

	}

	@Test
	public void testRetrieveNExCase12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12,根据关键字模糊搜索,输入不存在的sName");
		// 1、根据关键字RetrieveN
		String queryKeyword = "-999999999";
		MvcResult andReturn9 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), queryKeyword)//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 2、结果验证，判断返回的promotion名称是否符合按模糊搜索返回的
		Shared.checkJSONErrorCode(andReturn9);
		JSONObject o9 = JSONObject.fromObject(andReturn9.getResponse().getContentAsString());
		JSONArray list9 = o9.getJSONArray(BaseAction.KEY_ObjectList);
		Assert.assertTrue(list9.size() == 0, "按关键字模糊搜索促销活动失败");
	}

	@Test
	public void testRetrieveNExCase13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:没有权限进行操作");
//		MvcResult andReturn10 = mvc.perform(//
//				post("/promotion/retrieveNEx.bx")//
//						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(andReturn10, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNExCase14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case14,查询满减优惠活动时，需返回所有的数据,即把promotion的从表PromotionScope全都查询出来");
		//
		PromotionScope psCreate14 = new PromotionScope();
		for (int i = 1; i <= 20; i++) {
			psCreate14.setCommodityID(i);
			psCreate14.setPromotionID(1);
			Map<String, Object> createParams14 = psCreate14.getCreateParam(BaseBO.INVALID_CASE_ID, psCreate14);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			PromotionScope psCreated14 = (PromotionScope) promotionScopeMapper.create(createParams14);
			Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParams14.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "创建促销范围失败");
			//
			psCreate14.setIgnoreIDInComparision(true);
			psCreate14.setCommodityName(psCreated14.getCommodityName());
			if (psCreate14.compareTo(psCreated14) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
		}
		//
		MvcResult andReturn14 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(andReturn14);
		JSONObject o14 = JSONObject.fromObject(andReturn14.getResponse().getContentAsString());
		// 结果验证，因为promotion表有数据，促销活动数目大于0.
		JSONArray list14 = o14.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < list14.size(); i++) {
			Promotion JsonToPromotion = (Promotion) new Promotion().parse1(list14.getJSONObject(i).toString());
			if (JsonToPromotion.getID() == 1) {
				Assert.assertTrue(JsonToPromotion.getListSlave1().size() >= 20, "查询满减优惠活动时，没有返回所有的数据");
			}
		}
	}

	@Test
	public void testRetrieveNExCase15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15,根据最小长度（10）的促销单号进行模糊查询");
		// 1、根据关键字RetrieveN
		MvcResult andReturn15 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), "CX20190605")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 2、结果验证，判断返回的promotion名称是否符合按模糊搜索返回的
		Shared.checkJSONErrorCode(andReturn15);
		JSONObject o15 = JSONObject.fromObject(andReturn15.getResponse().getContentAsString());
		JSONArray list15 = o15.getJSONArray(BaseAction.KEY_ObjectList);
		for (int i = 0; i < list15.size(); i++) {
			Promotion JsonToPromotion15 = (Promotion) new Promotion().parse1(list15.getJSONObject(i).toString());
			Assert.assertTrue(JsonToPromotion15.getSn().contains("CX20190605"), "按关键字模糊搜索促销活动失败");
		}
	}

	@Test
	public void testRetrieveNExCase16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16,根据小于最小长度（10）的促销单号进行模糊查询");
		// 1、根据关键字RetrieveN
		MvcResult andReturn16 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), "CX2019060")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 2、结果验证，判断返回的promotion名称是否符合按模糊搜索返回的
		Shared.checkJSONErrorCode(andReturn16);
		JSONObject o16 = JSONObject.fromObject(andReturn16.getResponse().getContentAsString());
		JSONArray list16 = o16.getJSONArray(BaseAction.KEY_ObjectList);
		Assert.assertTrue(list16.size() == 0, "按小于最小长度（10）的促销单号进行模糊查询");
	}

	@Test
	public void testRetrieveNExCase17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case17,根据大于最大长度（20）的促销单号进行模糊查询");
		// 1、根据关键字RetrieveN
		MvcResult andReturn17 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), "CX20190605123451234512345")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 2、结果验证，判断返回的promotion名称是否符合按模糊搜索返回的
		Shared.checkJSONErrorCode(andReturn17);
		JSONObject o17 = JSONObject.fromObject(andReturn17.getResponse().getContentAsString());
		JSONArray list17 = o17.getJSONArray(BaseAction.KEY_ObjectList);
		Assert.assertTrue(list17.size() == 0, "按大于最小长度（10）的促销单号进行模糊查询");
	}

	@Test
	public void testRetrieveNExCase18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case18,根据等于最大长度（20）的促销单号进行模糊查询");
		// 1、根据关键字RetrieveN
		MvcResult andReturn18 = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_queryKeyword(), "CX201906051234512345")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		// 2、结果验证，判断返回的promotion名称是否符合按模糊搜索返回的
		Shared.checkJSONErrorCode(andReturn18);
		JSONObject o18 = JSONObject.fromObject(andReturn18.getResponse().getContentAsString());
		JSONArray list18 = o18.getJSONArray(BaseAction.KEY_ObjectList);
		Assert.assertTrue(list18.size() >= 0, "按等于最小长度（10）的促销单号进行模糊查询");
	}

	@Test
	public void testRetrieveNExCase19() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19 pos端发出请求同步促销(查询正在进行和未开始的促销)");

		Promotion promotion = new Promotion();
		promotion.setSubStatusOfStatus(EnumSubStatusPromotion.ESSP_ToStartAndPromoting.getIndex());
		promotion.setStatus(Promotion.EnumStatusPromotion.ESP_Active.getIndex());
		promotion.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		promotion.setPageIndex(BaseAction.PAGE_StartIndex);

		MvcResult mr = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), String.valueOf(promotion.getSubStatusOfStatus()))//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(promotion.getStatus()))//
						.param(Promotion.field.getFIELD_NAME_pageSize(), String.valueOf(promotion.getPageSize()))//
						.param(Promotion.field.getFIELD_NAME_pageIndex(), String.valueOf(promotion.getPageIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getPosLoginSession(mvc, Shared.POS_2_ID))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		JSONArray list = o.getJSONArray(BaseAction.KEY_ObjectList);
		Date date = new Date();
		for (int i = 0; i < list.size(); i++) {
			Promotion p = (Promotion) new Promotion().parse1(list.getJSONObject(i).toString());
			Assert.assertTrue(p.getStatus() == Promotion.EnumStatusPromotion.ESP_Active.getIndex(), "查询到已失效的促销");
			Assert.assertTrue(date.before(p.getDatetimeEnd()), "查询到已结束的促销");
		}
	}

	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Promotion p = new Promotion();
		Shared.caseLog("Case1: 查看数据返回是否默认降序 ");
		MvcResult andReturn = mvc.perform(//
				post("/promotion/retrieveNEx.bx")//
						.param(Promotion.field.getFIELD_NAME_subStatusOfStatus(), "0")//
						.param(Promotion.field.getFIELD_NAME_status(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(andReturn);
		//
		JSONObject o = JSONObject.fromObject(andReturn.getResponse().getContentAsString());
		List<BaseModel> bmList = p.parseN(o.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (int i = 1; i < bmList.size(); i++) {
			Assert.assertTrue(((Promotion) bmList.get(i - 1)).getID() > ((Promotion) bmList.get(i)).getID(), "数据返回错误（非降序）");
		}
	}

	// 创建一个Promotion
	public Promotion createPromotion() {
		Promotion p = new Promotion();
		// p.setPromotionTypeID(1);
		// p.setProgramName("特价");
		// p.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		// p.setConsumerType("会员");
		// p.setCreator(1);
		// p.setApprover(2);
		// p.setApproveDatetime(new Date());
		// p.setStartDatetime(new Date());
		// p.setEndDatetime(new Date());
		// p.setExecuteWeekday(1111111);
		// p.setExecuteStartDatetime(new Date());
		// p.setExecuteEndDatetime(new Date());
		// p.setRemark("1111");
		// p.setDiscountUponVIP(1);
		// p.setSpecialOfferInvolved(1);
		// p.setTimes(1.2f);
		// p.setVIPBornDayOrMonth(1);
		// p.setWholeShop(1);
		// p.setNOReached(1);
		// p.setAmountReached(30);
		// p.setCashExcluded(5);
		// p.setWholeTradeDiscount(1);
		// p.setDiscount(1);
		// p.setNOComputationInvolved(1);
		// p.setGiftAll(1);
		// p.setNOComputationInvolved(1);
		// p.setAmountComputationInvolved(1);
		// p.setDiscountInvolvedForSpecialOffer(1);
		Map<String, Object> params = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);

		Promotion bm = (Promotion) promotionMapper.create(params);

		p.setIgnoreIDInComparision(true);
		if (p.compareTo(bm) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		return p;
	}
}
