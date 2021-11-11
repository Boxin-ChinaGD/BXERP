package com.bx.erp.test.purchasing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class PurchasingOrderCommodityActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	// 获取session的信息。调用创建session的方法
	private HttpSession getLoginSession() throws Exception {
		// PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
		MvcResult result = (MvcResult) this.mvc.perform(//
				get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrderCommodity.field.getFIELD_NAME_ID() + "=5") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
				.andExpect(status().isOk())//
				.andReturn(); //

		return result.getRequest().getSession();
	}

	// @Test ...fix jenkins:没有找到retrieveN()方法 czq
	// public void testIndex() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// mvc.perform(get("/purchasingOrderCommodity/retrieveN.bx?ID=2")).andExpect(status().isOk());
	// }

	// @SuppressWarnings("static-access")
	// @Test
	// public void testCreate() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// PurchasingOrderCommodity poc = new PurchasingOrderCommodity(); //
	// mvc.perform(post("/purchasingOrderCommodity/create.bx") //
	// .param(poc.getFIELD_NAME_commodityID(), "2") //
	// .param(poc.getFIELD_NAME_purchasingOrderID(), "2") //
	// .param(poc.getFIELD_NAME_commodityNO(), "800") //
	// .param(poc.getFIELD_NAME_priceSuggestion(), "1") //
	// .session((MockHttpSession) new
	// BaseTestNGSpringContextTest().getStaffLoginSession(mvc, Shared.PhoneOfBoss)))
	// //
	// .andExpect(status().isOk()); //
	// }

	// @Test
	// public void testDelete() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// mvc.perform(get("/purchasingOrderCommodity/delete.bx?ID=3")).andExpect(status().isOk());
	// }

	// @Test
	// public void testAppendToCommodityListEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("---------------case1：正常添加采购商品---------------------------");
	// MvcResult mr = mvc.perform(//
	// get("/purchasingOrderCommodity/appendToCommodityListEx.bx?commID=2,5&barcodeIDs=1,2&NOs=10,20&packageUnitIDs=1,2&priceSuggestions=1.1,2.5&commNames=薯片,可乐")
	// //
	// .session((MockHttpSession) getLoginSession()) //
	// .contentType(MediaType.APPLICATION_JSON)//
	// ) //
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("---------------case2:
	// commID为空------------------------------");
	// MvcResult mr2 = mvc.perform(//
	// get("/purchasingOrderCommodity/appendToCommodityListEx.bx?commID=&barcodeIDs=1,2&NOs=10,20&packageUnitIDs=1,2&priceSuggestions=1.1,2.5&commNames=薯片,可乐")
	// //
	// .session((MockHttpSession) getLoginSession()) //
	// .contentType(MediaType.APPLICATION_JSON)//
	// ) //
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn(); //
	//
	// String json2 = mr2.getResponse().getContentAsString();
	// assertTrue(json2.equals(""));
	//
	// System.out.println("---------------case3:
	// commID为非法字符串-------------------------");
	// MvcResult mr3 = mvc.perform(//
	// get("/purchasingOrderCommodity/appendToCommodityListEx.bx?commID=aaaaaa,aaaaaaa&barcodeIDs=1,2&NOs=10,20&packageUnitIDs=1,2&priceSuggestions=1.1,2.5&commNames=薯片,可乐")
	// //
	// .session((MockHttpSession) getLoginSession()) //
	// .contentType(MediaType.APPLICATION_JSON)//
	// ) //
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn(); //
	//
	// String json3 = mr3.getResponse().getContentAsString();
	// assertTrue(json3.equals(""));
	//
	// System.out.println("---------------case4:
	// session没有需要的参数-----------------------");
	// MvcResult mr4 = mvc.perform(//
	// get("/purchasingOrderCommodity/appendToCommodityListEx.bx?commID=2,5&barcodeIDs=1,2&NOs=10,20&packageUnitIDs=1,2&priceSuggestions=1.1,2.5&commNames=薯片,可乐")
	// //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// .contentType(MediaType.APPLICATION_JSON)//
	// ) //
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn(); //
	//
	// String json4 = mr4.getResponse().getContentAsString();
	// assertTrue(json4.equals(""));
	// }
	//
	// @Test
	// public void testReduceCommodityEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("---------------case1：正常删除List对应的商品和采购单商品-------------------------");
	// MvcResult mr = mvc.perform(//
	// get("/purchasingOrderCommodity/reduceEx.bx?commID=2") //
	// .session((MockHttpSession) getLoginSession()) //
	// .contentType(MediaType.APPLICATION_JSON)//
	// ) //
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("---------------case2：没有传入正确的session-------------------");
	// MvcResult mr2 = mvc.perform(//
	// get("/purchasingOrderCommodity/reduceEx.bx?commID=2") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// .contentType(MediaType.APPLICATION_JSON)//
	// ) //
	// .andExpect(status().isOk()).andDo(print()).andReturn(); //
	//
	// String json2 = mr2.getResponse().getContentAsString();
	// assertTrue(json2.equals(""));
	//
	// System.out.println("---------------case3：传入空的session-------------------");
	// MvcResult mr3 = mvc.perform(//
	// get("/purchasingOrderCommodity/reduceEx.bx?commID=2") //
	// .session(new MockHttpSession()) //
	// .contentType(MediaType.APPLICATION_JSON)//
	// ) //
	// .andExpect(status().isOk()).andDo(print()).andReturn(); //
	//
	// String json3 = mr3.getResponse().getContentAsString();
	// assertTrue(json3.equals(""));
	// }

	// @Test
	// public void testUpdateCommodityNOPriceUnitEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("---------------case1：正常修改数据-----------------------");
	// MvcResult mr =
	// mvc.perform(post("/purchasingOrderCommodity/updateNOPriceUnitEx.bx?commID=2&commNO=60&commPrice=29.9&commPurchasingUnit=桶")
	// //
	// .session((MockHttpSession) getLoginSession()) //
	// .contentType(MediaType.APPLICATION_JSON)) //
	// .andExpect(status().isOk()).andDo(print()).andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("---------------case2：commID非法-----------------------");
	// MvcResult mr2 =
	// mvc.perform(post("/purchasingOrderCommodity/updateNOPriceUnitEx.bx?commID=aaaa&commNO=60&commPrice=29.9&commPurchasingUnit=桶")
	// //
	// .session((MockHttpSession) getLoginSession()) //
	// .contentType(MediaType.APPLICATION_JSON)) //
	// .andExpect(status().isOk()).andDo(print()).andReturn(); //
	//
	// String json2 = mr2.getResponse().getContentAsString();
	// assertTrue(json2.equals(""));
	//
	// System.out.println("---------------case3：commNO非法----------------------");
	// MvcResult mr3 =
	// mvc.perform(post("/purchasingOrderCommodity/updateNOPriceUnitEx.bx?commID=2&commNO=aaaaa&commPrice=29.9&commPurchasingUnit=桶")
	// //
	// .session((MockHttpSession) getLoginSession()) //
	// .contentType(MediaType.APPLICATION_JSON)) //
	// .andExpect(status().isOk()).andDo(print()).andReturn(); //
	//
	// String json3 = mr3.getResponse().getContentAsString();
	// assertTrue(json3.equals(""));
	//
	// System.out.println("---------------case4：commPrice非法------------------------");
	// MvcResult mr4 =
	// mvc.perform(post("/purchasingOrderCommodity/updateNOPriceUnitEx.bx?commID=2&commNO=60&commPrice=aaaaaaa&commPurchasingUnit=桶")
	// //
	// .session((MockHttpSession) getLoginSession()) //
	// .contentType(MediaType.APPLICATION_JSON)) //
	// .andExpect(status().isOk()).andDo(print()).andReturn(); //
	//
	// String json4 = mr4.getResponse().getContentAsString();
	// assertTrue(json4.equals(""));
	//
	// System.out.println("---------------case5：commID不存在-------------------------");
	// MvcResult mr5 =
	// mvc.perform(post("/purchasingOrderCommodity/updateNOPriceUnitEx.bx?commID=99999&commNO=60&commPrice=29.9&commPurchasingUnit=桶")
	// //
	// .session((MockHttpSession) getLoginSession()) //
	// .contentType(MediaType.APPLICATION_JSON)) //
	// .andExpect(status().isOk()).andDo(print()).andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr5);
	// }

	// @Test //接口已废弃
	public void testAppendToCommodityEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
		System.out.println("---------------case1：正常添加采购商品--------------------------");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrderCommodity/appendToCommodityEx.bx?" + PurchasingOrderCommodity.field.getFIELD_NAME_commodityID() + "=2&" + PurchasingOrderCommodity.field.getFIELD_NAME_barcodeID() + "=1&"//
						+ PurchasingOrderCommodity.field.getFIELD_NAME_commodityNO() + "=10&" + PurchasingOrderCommodity.field.getFIELD_NAME_packageUnitID() + "=1&" + PurchasingOrderCommodity.field.getFIELD_NAME_priceSuggestion() + "=1.1&"
						+ PurchasingOrderCommodity.field.getFIELD_NAME_commodityName() + "=薯片") //
								.session((MockHttpSession) getLoginSession()) //
								.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);

		System.out.println("---------------case2：没有相应Session---------------------------");
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrderCommodity/appendToCommodityEx.bx?" + PurchasingOrderCommodity.field.getFIELD_NAME_commodityID()//
						+ "=2&" + PurchasingOrderCommodity.field.getFIELD_NAME_barcodeID() + "=1&" + PurchasingOrderCommodity.field.getFIELD_NAME_commodityNO() + "=10&" + PurchasingOrderCommodity.field.getFIELD_NAME_packageUnitID() + "=1&"
						+ PurchasingOrderCommodity.field.getFIELD_NAME_priceSuggestion() + "=1.1&" + PurchasingOrderCommodity.field.getFIELD_NAME_commodityName() + "=薯片") //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)).contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		String json2 = mr2.getResponse().getContentAsString();
		assertTrue(json2.equals(""));
	}

	@Test
	public void testRetrieveNWarehousingEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
		System.out.println("---------------case1：查找未入库的采购订单商品------------------------");
		MvcResult mr = mvc.perform( //
				get("/purchasingOrderCommodity/retrieveNWarehousingEx.bx?" + PurchasingOrderCommodity.field.getFIELD_NAME_purchasingOrderID() + "=10&" + PurchasingOrderCommodity.field.getFIELD_NAME_purchasingOrderCommodityIsInWarehouse()
						+ "=0") //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);

		System.out.println("------------------case2：查找已入库的采购订单商品---------------------");
		MvcResult mr1 = mvc.perform( //
				get("/purchasingOrderCommodity/retrieveNWarehousingEx.bx?" + PurchasingOrderCommodity.field.getFIELD_NAME_purchasingOrderID() + "=10&" + PurchasingOrderCommodity.field.getFIELD_NAME_purchasingOrderCommodityIsInWarehouse()
						+ "=1") //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		System.out.println("------------------case3：查找不存在的采购订单商品---------------------");
		MvcResult mr2 = mvc.perform( //
				get("/purchasingOrderCommodity/retrieveNWarehousingEx.bx?" + PurchasingOrderCommodity.field.getFIELD_NAME_purchasingOrderID() + "=100&" + PurchasingOrderCommodity.field.getFIELD_NAME_purchasingOrderCommodityIsInWarehouse()
						+ "=1") //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
	}
}
