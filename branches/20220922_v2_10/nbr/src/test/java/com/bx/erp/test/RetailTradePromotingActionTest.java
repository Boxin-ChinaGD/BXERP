package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebAppConfiguration
public class RetailTradePromotingActionTest extends BaseActionTest {

	private static final int INVALID_ID = 999999999;

	@BeforeClass
	public void setup() {
		super.setUp();
		try {
			Shared.resetPOS(mvc, 2);
			sessionBoss = Shared.getPosLoginSession(mvc, 2);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createExTest() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case1：正常创建没有参与促销活动");
		RetailTradePromoting rp = new RetailTradePromoting();
		rp.setTradeID(1);
		String json = JSONObject.fromObject(rp).toString();
		MvcResult mr = mvc.perform(post("/retailTradePromoting/createEx.bx")//
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
				.param("json", json).contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();//
		//
		Shared.checkJSONErrorCode(mr);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		String model = o.getString(BaseAction.KEY_Object);
		RetailTradePromoting retailTradePromotingC = (RetailTradePromoting) rp.parse1(model);
		rp.setIgnoreIDInComparision(true);
		if (rp.compareTo(retailTradePromotingC) != 0) {
			Assert.assertTrue(false, "DB返回的数据和准备数据不一致");
		}

		Shared.caseLog("Case2：参与过促销活动");
		RetailTradePromoting rp2 = new RetailTradePromoting();
		rp2.setTradeID(1);
		List<RetailTradePromotingFlow> list = new ArrayList<RetailTradePromotingFlow>();
		for (int i = 0; i < 2; i++) {
			RetailTradePromotingFlow retailTradePromotingFlow1 = new RetailTradePromotingFlow();
			retailTradePromotingFlow1.setPromotionID(i + 1);
			retailTradePromotingFlow1.setProcessFlow(UUID.randomUUID().toString());
			list.add(retailTradePromotingFlow1);
		}
		rp2.setListSlave1(list);
		json = JSONObject.fromObject(rp2).toString();
		MvcResult mr2 = mvc.perform(post("/retailTradePromoting/createEx.bx")//
				.session((MockHttpSession) sessionBoss)//
				.param("json", json).contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk()).andDo(print()).andReturn();//
		//
		Shared.checkJSONErrorCode(mr2);
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		String model2 = o2.getString(BaseAction.KEY_Object);
		RetailTradePromoting retailTradePromoting2C = (RetailTradePromoting) rp.parse1(model2);
		rp2.setIgnoreIDInComparision(true);
		if (rp2.compareTo(retailTradePromoting2C) != 0) {
			Assert.assertTrue(false, "DB返回的数据和准备数据不一致");
		}

		Shared.caseLog("Case3：零售单促销计算详细信息重复创建");
		MvcResult mr3 = mvc.perform(post("/retailTradePromoting/createEx.bx")//
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
				.param("json", json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3);
		JSONObject o3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		String model3 = o3.getString(BaseAction.KEY_Object);
		RetailTradePromoting retailTradePromoting3C = (RetailTradePromoting) rp.parse1(model3);
		rp2.setIgnoreIDInComparision(true);
		if (rp2.compareTo(retailTradePromoting3C) != 0) {
			Assert.assertTrue(false, "DB返回的数据和准备数据不一致");
		}

		Shared.caseLog("case4：没有权限进行操作");
//		MvcResult mr4 = mvc.perform(post("/retailTradePromoting/createEx.bx")//
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//				.param("json", json)//
//				.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);

		Shared.caseLog("case5:不存在的零售单ID");
		RetailTradePromoting rp4 = new RetailTradePromoting();
		rp4.setTradeID(INVALID_ID);
		json = JSONObject.fromObject(rp4).toString();
		MvcResult mr5 = mvc.perform(post("/retailTradePromoting/createEx.bx")//
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
				.param("json", json)//
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();//
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoSuchData);

		Shared.caseLog("case6:不存在的零售单ID, ID <=0 数据检验失败");
		RetailTradePromoting rp6 = new RetailTradePromoting();
		rp6.setTradeID(BaseAction.INVALID_ID);
		json = JSONObject.fromObject(rp6).toString();
		MvcResult mr6 = mvc.perform(post("/retailTradePromoting/createEx.bx")//
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
				.param("json", json)//
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();//
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void retrieve1ExTest() throws Exception {
		Shared.printTestMethodStartInfo();

		RetailTradePromoting rtp = new RetailTradePromoting();
		rtp.setTradeID(1);
		String json = JSONObject.fromObject(rtp).toString();
		MvcResult mrC = mvc.perform(post("/retailTradePromoting/createEx.bx")//
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
				.param("json", json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mrC);
		JSONObject o = JSONObject.fromObject(mrC.getResponse().getContentAsString());
		String model = o.getString(BaseAction.KEY_Object);
		RetailTradePromoting retailTradePromotingC = (RetailTradePromoting) rtp.parse1(model);
		rtp.setIgnoreIDInComparision(true);
		if (rtp.compareTo(retailTradePromotingC) != 0) {
			Assert.assertTrue(false, "DB返回的数据和准备数据不一致");
		}

		Shared.caseLog("Case1：正常查询计算过程");
		MvcResult mr = mvc.perform(get("/retailTradePromoting/retrieve1Ex.bx?" + RetailTradePromoting.field.getFIELD_NAME_ID() + "=" + retailTradePromotingC.getID()) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		Shared.caseLog("Case2：查询计算过程ID不存在");
		// RetailTradePromoting rtp1 = new RetailTradePromoting();
		MvcResult mr1 = mvc.perform(get("/retailTradePromoting/retrieve1Ex.bx?" + RetailTradePromoting.field.getFIELD_NAME_ID() + "=" + INVALID_ID) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);

		Shared.caseLog("Case3：查询计算过程没有权限");
//		MvcResult mr2 = mvc.perform(get("/retailTradePromoting/retrieve1Ex.bx?" + RetailTradePromoting.field.getFIELD_NAME_ID() + "=" + retailTradePromotingC.getID()) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//				.contentType(MediaType.APPLICATION_JSON))//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}
	
	@Test
	public void testCreateNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------------case1:正常上传------------------------------------");
		List<RetailTradePromoting> retailTradePromotingList = new ArrayList<RetailTradePromoting>();
		retailTradePromotingList.add(getRetailTradePromoting());
		retailTradePromotingList.add(getRetailTradePromoting2());
		String jsonArray = JSONArray.fromObject(retailTradePromotingList).toString();

		// RetailTradePromoting rtp = new RetailTradePromoting();
		MvcResult mr = mvc.perform(//
				post("/retailTradePromoting/createNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
						.param(BaseAction.KEY_ObjectList, jsonArray)) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		try {
			JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
			JsonPath.read(o, "$.msg");
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
			assertTrue(e.getMessage().toString().equals("No results for path: $['msg']"));
		}

	}
	
	public RetailTradePromoting getRetailTradePromoting() {
		RetailTradePromoting rtp = new RetailTradePromoting();
		rtp.setTradeID(1);
		rtp.setCreateDatetime(new Date());

		RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
		rtpf.setRetailTradePromotingID(1);
		rtpf.setPromotionID(4);
		rtpf.setProcessFlow("测试1");
		rtpf.setCreateDatetime(new Date());

		RetailTradePromotingFlow rtpf2 = new RetailTradePromotingFlow();
		rtpf2.setRetailTradePromotingID(1);
		rtpf2.setPromotionID(3);
		rtpf2.setProcessFlow("测试2");
		rtpf2.setCreateDatetime(new Date());

		List<RetailTradePromotingFlow> rtpfList = new ArrayList<RetailTradePromotingFlow>();
		rtpfList.add(rtpf);
		rtpfList.add(rtpf2);

		rtp.setListSlave1(rtpfList);

		return rtp;
	}

	public RetailTradePromoting getRetailTradePromoting2() {
		RetailTradePromoting rtp = new RetailTradePromoting();
		rtp.setTradeID(1);
		rtp.setCreateDatetime(new Date());

		RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
		rtpf.setRetailTradePromotingID(2);
		rtpf.setPromotionID(4);
		rtpf.setProcessFlow("测试3");
		rtpf.setCreateDatetime(new Date());

		RetailTradePromotingFlow rtpf2 = new RetailTradePromotingFlow();
		rtpf2.setRetailTradePromotingID(2);
		rtpf2.setPromotionID(3);
		rtpf2.setProcessFlow("测试4");
		rtpf2.setCreateDatetime(new Date());

		List<RetailTradePromotingFlow> rtpfList = new ArrayList<RetailTradePromotingFlow>();
		rtpfList.add(rtpf);
		rtpfList.add(rtpf2);

		rtp.setListSlave1(rtpfList);

		return rtp;
	}
}
