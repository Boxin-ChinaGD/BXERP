package com.bx.erp.test.warehousing;

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
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;

@Transactional
@WebAppConfiguration
public class WarehousingCommodityActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	private HttpSession getLoginSession() throws Exception {
		// WarehousingCommodity wc = new WarehousingCommodity();
		MvcResult result = (MvcResult) this.mvc.perform( //
				get("/warehousing/retrieve1Ex.bx?" + WarehousingCommodity.field.getFIELD_NAME_ID() + "=1") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andReturn();
		return result.getRequest().getSession();
	}

	// @Test
	public void testCreate() throws Exception {
		Shared.printTestMethodStartInfo();
		// WarehousingCommodity w = new WarehousingCommodity();
		mvc.perform( //
				post("/warehousingCommodity/createEx.bx") //
						.param(WarehousingCommodity.field.getFIELD_NAME_warehousingID(), "5") //
						.param(WarehousingCommodity.field.getFIELD_NAME_commodityID(), "3") //
						.param(WarehousingCommodity.field.getFIELD_NAME_NO(), "100") //
						.param(WarehousingCommodity.field.getFIELD_NAME_barcodeID(), "1") //
						.param(WarehousingCommodity.field.getFIELD_NAME_price(), "10.0") //
						.param(WarehousingCommodity.field.getFIELD_NAME_amount(), "200") //
						.param(WarehousingCommodity.field.getFIELD_NAME_shelfLife(), "36") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
	}

	@Test
	public void testRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo();
		// WarehousingCommodity wc = new WarehousingCommodity();

		mvc.perform(post("/warehousingCommodity/retrieveN.bx") //
				.param(WarehousingCommodity.field.getFIELD_NAME_warehousingID(), "1") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
				.andExpect(status().isOk());
	}

	// @Test
	// public void updateExTest() throws Exception {
	// Shared.printTestMethodStartInfo();
	// WarehousingCommodity wc = new WarehousingCommodity();
	// MvcResult mr = mvc.perform(post("/warehousingCommodity/createEx.bx") //
	// .param(wc.getFIELD_NAME_warehousingID(), "5") //
	// .param(wc.getFIELD_NAME_commodityID(), "2,3") //
	// .param(wc.getFIELD_NAME_price(), "5,6") //
	// .param(wc.getFIELD_NAME_NO(), "250,301") //
	// .contentType(MediaType.APPLICATION_JSON)) //
	// .andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// Shared.checkJSONErrorCode(mr);
	// }

	// @Test //接口已废弃
	public void testAppendToCommodityListEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("----------------------------Case1：正常添加入库商品------------------------------");
		MvcResult mr = mvc.perform( //
				get("/warehousingCommodity/appendToCommodityListEx.bx?commID=2,5&barcodeIDs=1,2&NOs=10,20&packageUnitIDs=1,2&priceSuggestions=1.1,2.5&commNames=薯片,可乐") //
						.session((MockHttpSession) getLoginSession()) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);

		System.out.println("----------------------------Case2: commID为空------------------------------");
		MvcResult mr2 = mvc.perform( //
				get("/warehousingCommodity/appendToCommodityListEx.bx?commID=&barcodeIDs=1,2&NOs=10,20&packageUnitIDs=1,2&priceSuggestions=1.1,2.5&commNames=薯片,可乐") //
						.session((MockHttpSession) getLoginSession()) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String json2 = mr2.getResponse().getContentAsString();
		assertTrue(json2.equals(""));

		System.out.println("----------------------------Case3: commID为非法字符串------------------------------");
		MvcResult mr3 = mvc.perform( //
				get("/warehousingCommodity/appendToCommodityListEx.bx?commID=aaaaaa,aaaaaaa&barcodeIDs=1,2&NOs=10,20&packageUnitIDs=1,2&priceSuggestions=1.1,2.5&commNames=薯片,可乐") //
						.session((MockHttpSession) getLoginSession()) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String json3 = mr3.getResponse().getContentAsString();
		assertTrue(json3.equals(""));

		System.out.println("----------------------------Case4: session没有需要的参数------------------------------");
		MvcResult mr4 = mvc.perform( //
				get("/warehousingCommodity/appendToCommodityListEx.bx?commID=2,5&barcodeIDs=1,2&NOs=10,20&packageUnitIDs=1,2&priceSuggestions=1.1,2.5&commNames=薯片,可乐") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		String json4 = mr4.getResponse().getContentAsString();
		assertTrue(json4.equals(""));
	}

	// @Test //接口已废弃
	public void testAppendToCommodityEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// WarehousingCommodity wc = new WarehousingCommodity();

		System.out.println("---------------------------------Case1：正常添加入库商品-----------------------------------");
		MvcResult mr = mvc.perform( //
				post("/warehousingCommodity/appendToCommodityEx.bx") //
						.param(WarehousingCommodity.field.getFIELD_NAME_commodityID(), "2") //
						.param(WarehousingCommodity.field.getFIELD_NAME_barcodeID(), "1") //
						.param(WarehousingCommodity.field.getFIELD_NAME_NO(), "10") //
						.param(WarehousingCommodity.field.getFIELD_NAME_packageUnitID(), "1") //
						.param(WarehousingCommodity.field.getFIELD_NAME_price(), "1.1") //
						.session((MockHttpSession) getLoginSession()) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);

		System.out.println("---------------------------------Case2：没有相应Session-----------------------------------");
		MvcResult mr2 = mvc.perform( //
				post("/warehousingCommodity/appendToCommodityEx.bx") //
						.param(WarehousingCommodity.field.getFIELD_NAME_commodityID(), "2") //
						.param(WarehousingCommodity.field.getFIELD_NAME_barcodeID(), "1") //
						.param(WarehousingCommodity.field.getFIELD_NAME_NO(), "10") //
						.param(WarehousingCommodity.field.getFIELD_NAME_packageUnitID(), "1") //
						.param(WarehousingCommodity.field.getFIELD_NAME_price(), "1.1") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String json2 = mr2.getResponse().getContentAsString();
		assertTrue(json2.equals(""));
	}

	// @Test //接口已废弃
	public void testUpdateCommodityNOPriceUnitEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("---------------case1：正常修改数据-----------------------");
		MvcResult mr = mvc.perform( //
				get("/warehousingCommodity/updateNOPriceUnitEx.bx?commodityID=1&NO=10&price=16.6&amount=166") //
						.session((MockHttpSession) getLoginSession()) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);

		System.out.println("---------------case2：commodityID非法-----------------------");
		MvcResult mr2 = mvc.perform( //
				get("/warehousingCommodity/updateNOPriceUnitEx.bx?commodityID=-1&NO=10&price=16.6&amount=166") //
						.session((MockHttpSession) getLoginSession()) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String json2 = mr2.getResponse().getContentAsString();
		assertTrue(json2.equals(""));

		System.out.println("---------------case3：NO非法----------------------");
		MvcResult mr3 = mvc.perform( //
				get("/warehousingCommodity/updateNOPriceUnitEx.bx?commodityID=1&NO=-1&price=16.6&amount=166") //
						.session((MockHttpSession) getLoginSession()) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String json3 = mr3.getResponse().getContentAsString();
		assertTrue(json3.equals(""));

		System.out.println("---------------case4：commPrice非法------------------------");
		MvcResult mr4 = mvc.perform( //
				get("/warehousingCommodity/updateNOPriceUnitEx.bx?commodityID=1&NO=10&price=-1&amount=166") //
						.session((MockHttpSession) getLoginSession()) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String json4 = mr4.getResponse().getContentAsString();
		assertTrue(json4.equals(""));

		System.out.println("---------------case5：amount非法-------------------------");
		MvcResult mr5 = mvc.perform( //
				get("/warehousingCommodity/updateNOPriceUnitEx.bx?commodityID=1&NO=10&price=16.6&amount=-1") //
						.session((MockHttpSession) getLoginSession()) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String json5 = mr5.getResponse().getContentAsString();
		assertTrue(json5.equals(""));
	}
}
