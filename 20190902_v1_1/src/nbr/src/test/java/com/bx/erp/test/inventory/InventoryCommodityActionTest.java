package com.bx.erp.test.inventory;

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
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.InventorySheet;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testng.Assert.assertTrue;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebAppConfiguration
public class InventoryCommodityActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	// 获取session的信息。调用创建session的方法
//	private HttpSession getLoginSession() throws Exception {
//		MvcResult result = (MvcResult) mvc.perform(//
//				get("/inventorySheet/retrieve1.bx?" + InventorySheet.field.getFIELD_NAME_ID() + "=3")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
//		)//
//				.andExpect(status().isOk())//
//				.andReturn();
//		return result.getRequest().getSession();
//	}

	private InventorySheet createInventory() throws CloneNotSupportedException, InterruptedException {
		InventorySheet is = new InventorySheet();
		is.setWarehouseID(1);
		is.setScope(11);
		is.setStatus(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex());
		is.setStaffID(1);
		is.setRemark("aaaaaaaa1");
		is.setShopID(2);
		Map<String, Object> params = is.getCreateParam(BaseBO.INVALID_CASE_ID, is);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet isCreate = (InventorySheet) inventorySheetMapper.create(params);// ...
		Assert.assertTrue(isCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		is.setIgnoreIDInComparision(true);
		if (is.compareTo(isCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		System.out.println("iIDFromCreat1：" + isCreate.getID());
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		Assert.assertTrue(isCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		return (InventorySheet) isCreate.clone();
	}

	@Test
	public void testHome() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(get("/home").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	// @Test
	// public void testUpdateCommodity() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// mvc.perform(get("/inventoryCommodity/updateCommodity.bx?inventorySheetID=1&commodityID=1&noReal=300")).andExpect(status().isOk());
	// }

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case1:正常添加盘点商品");
		InventorySheet is = createInventory();
		//
		MvcResult mr = mvc.perform(//
				get("/inventoryCommodity/createEx.bx?" + InventoryCommodity.field.getFIELD_NAME_inventorySheetID() + "=" + is.getID() + "&"//
						+ InventoryCommodity.field.getFIELD_NAME_commodityID() + "=1&"//
						+ InventoryCommodity.field.getFIELD_NAME_barcodeID() + "=1")//
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2:同一个盘点单重复添加相同商品 返回错误码1");
		// 正常添加盘点商品
		InventorySheet is = createInventory();
		MvcResult mr = mvc.perform(//
				get("/inventoryCommodity/createEx.bx?" + InventoryCommodity.field.getFIELD_NAME_inventorySheetID() + "=" + is.getID() + "&"//
						+ InventoryCommodity.field.getFIELD_NAME_commodityID() + "=1&"//
						+ InventoryCommodity.field.getFIELD_NAME_barcodeID() + "=1")//
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		// 添加重复盘点商品
		MvcResult mr1 = mvc.perform(//
				get("/inventoryCommodity/createEx.bx?" + InventoryCommodity.field.getFIELD_NAME_inventorySheetID() + "=" + is.getID() + "&"//
						+ InventoryCommodity.field.getFIELD_NAME_commodityID() + "=1&"//
						+ InventoryCommodity.field.getFIELD_NAME_barcodeID() + "=1")//
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3:没有权限进行操作");
//		InventorySheet is = createInventory();
//		MvcResult mr2 = mvc.perform(//
//				get("/inventoryCommodity/createEx.bx?" + InventoryCommodity.field.getFIELD_NAME_inventorySheetID() + "=" + is.getID() + "&"//
//						+ InventoryCommodity.field.getFIELD_NAME_commodityID() + "=1&"//
//						+ InventoryCommodity.field.getFIELD_NAME_barcodeID() + "=1")//
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4:添加组合商品到盘点单 返回7");

		InventorySheet is = createInventory();
		MvcResult mr2 = mvc.perform(//
				get("/inventoryCommodity/createEx.bx?" + InventoryCommodity.field.getFIELD_NAME_inventorySheetID() + "=" + is.getID() + "&"//
						+ InventoryCommodity.field.getFIELD_NAME_commodityID() + "=45&"//
						+ InventoryCommodity.field.getFIELD_NAME_barcodeID() + "=1")//
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:添加多包装商品到盘点单 返回7");

		InventorySheet is = createInventory();
		MvcResult mr = mvc.perform(//
				get("/inventoryCommodity/createEx.bx?" + InventoryCommodity.field.getFIELD_NAME_inventorySheetID() + "=" + is.getID() + "&"//
						+ InventoryCommodity.field.getFIELD_NAME_commodityID() + "=64&"//
						+ InventoryCommodity.field.getFIELD_NAME_barcodeID() + "=1")//
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6:添加服务商品到盘点单 返回7");

		InventorySheet is = createInventory();
		MvcResult mr = mvc.perform(//
				get("/inventoryCommodity/createEx.bx?" + InventoryCommodity.field.getFIELD_NAME_inventorySheetID() + "=" + is.getID() + "&"//
						+ InventoryCommodity.field.getFIELD_NAME_commodityID() + "=166&"//
						+ InventoryCommodity.field.getFIELD_NAME_barcodeID() + "=1")//
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	// @Test
	// public void testUpdateCommodityNOEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// // case1:正常修改盘点商品
	// MvcResult mr = mvc.perform(//
	// get("/inventoryCommodity/updateNOEx.bx?"+
	// inventoryCommodity.getFIELD_NAME_noReal() +"=200&" //
	// + inventoryCommodity.getFIELD_NAME_ID() +"=1")//
	// .session((MockHttpSession)
	// getLoginSession()).contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr);
	//
	// // case2:输入数量为-1
	// MvcResult mr1 = mvc.perform(//
	// get("/inventoryCommodity/updateNOEx.bx?"+
	// inventoryCommodity.getFIELD_NAME_noReal() +"=-1&" //
	// + inventoryCommodity.getFIELD_NAME_ID() +"=1")//
	// .session((MockHttpSession)
	// getLoginSession()).contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// String json2 = mr1.getResponse().getContentAsString();
	// assertTrue(json2.equals(""));
	//
	//
	// // case3:没有session
	// MvcResult mr3 = mvc.perform(//
	// get("/inventoryCommodity/updateNOEx.bx?"+
	// inventoryCommodity.getFIELD_NAME_noReal() +"=200&" //
	// + inventoryCommodity.getFIELD_NAME_ID() +"=1")//
	// ).andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// String json4 = mr3.getResponse().getContentAsString();
	// assertTrue(json4.equals(""));
	//
	// // case4:session中没有任何东西
	// MvcResult mr4 = mvc.perform(//
	// get("/inventoryCommodity/updateNOEx.bx?"+
	// inventoryCommodity.getFIELD_NAME_noReal() +"=200&" //
	// + inventoryCommodity.getFIELD_NAME_ID() +"=1")//
	// .session(new MockHttpSession()).contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// String json5 = mr4.getResponse().getContentAsString();
	// assertTrue(json5.equals(""));
	//
	// // case5:输入ID为-1
	// MvcResult mr5 = mvc.perform(//
	// get("/inventoryCommodity/updateNOEx.bx?"+
	// inventoryCommodity.getFIELD_NAME_noReal() +"=200&" //
	// + inventoryCommodity.getFIELD_NAME_ID() +"=1")//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// String json6 = mr5.getResponse().getContentAsString();
	// assertTrue(json6.equals(""));
	//
	// // case6:不传入ID
	// MvcResult mr6 = mvc.perform(//
	// get("/inventoryCommodity/updateNOEx.bx?realNO=200")//
	// .session((MockHttpSession)
	// getLoginSession()).contentType(MediaType.APPLICATION_JSON)//
	// ).andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// String json7 = mr6.getResponse().getContentAsString();
	// assertTrue(json7.equals(""));
	// }

	// @Test
	// public void testRetrieveNEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// MvcResult mr = mvc.perform(//
	// get("/inventoryCommodity/retrieveNEx.bx?SheetID=1")//
	// .session((MockHttpSession) getLoginSession())//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr);
	//
	// // CASE1:没有session
	// try {
	// mvc.perform(//
	// get("/inventoryCommodity/retrieveNEx.bx?SheetID=1").contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Assert.assertTrue(false, "结果不正确！");
	// } catch (Exception e) {
	// }
	// }

	// @Test //接口已废弃
	public void testAppendToCommodityEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// InventoryCommodity wc = new InventoryCommodity();

		System.out.println("---------------------------------Case1：正常添加入库商品-----------------------------------");
		MvcResult mr = mvc.perform( //
				post("/inventoryCommodity/appendToCommodityEx.bx") //
						.param(InventoryCommodity.field.getFIELD_NAME_commodityID(), "2") //
						.param(InventoryCommodity.field.getFIELD_NAME_barcodeID(), "1") //
						.param(InventoryCommodity.field.getFIELD_NAME_specification(), "1") //
						.param(InventoryCommodity.field.getFIELD_NAME_packageUnitID(), "1") //
						.param(InventoryCommodity.field.getFIELD_NAME_commodityName(), "薯片") //
						.session(sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);

		System.out.println("---------------------------------Case2：没有相应Session-----------------------------------");
		MvcResult mr2 = mvc.perform( //
				post("/inventoryCommodity/appendToCommodityEx.bx") //
						.param(InventoryCommodity.field.getFIELD_NAME_commodityID(), "2") //
						.param(InventoryCommodity.field.getFIELD_NAME_barcodeID(), "1") //
						.param(InventoryCommodity.field.getFIELD_NAME_packageUnitID(), "1") //
						.param(InventoryCommodity.field.getFIELD_NAME_packageUnitID(), "1") //
						.param(InventoryCommodity.field.getFIELD_NAME_commodityName(), "薯片") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String json2 = mr2.getResponse().getContentAsString();
		assertTrue(json2.equals(""));
	}

	// @Test //接口已废弃
	public void testAppendToCommodityListEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("----------------------------Case1：正常添加入库商品------------------------------");
		MvcResult mr = mvc.perform( //
				get("/inventoryCommodity/appendToCommodityListEx.bx?commID=2,5&barcodeIDs=1,2&specifications=1,2&packageUnitIDs=1,2&commNames=薯片,可乐") //
						.session(sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);

		System.out.println("----------------------------Case2: commID为空------------------------------");
		MvcResult mr2 = mvc.perform( //
				get("/inventoryCommodity/appendToCommodityListEx.bx?commID=&barcodeIDs=1,2&specifications=1,2&packageUnitIDs=1,2&commNames=薯片,可乐") //
						.session(sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String json2 = mr2.getResponse().getContentAsString();
		assertTrue(json2.equals(""));

		System.out.println("----------------------------Case3: commID为非法字符串------------------------------");
		MvcResult mr3 = mvc.perform( //
				get("/inventoryCommodity/appendToCommodityListEx.bx?commID=aaaaaa,aaaaaaa&barcodeIDs=1,2&specifications=1,2&packageUnitIDs=1,2&commNames=薯片,可乐") //
						.session(sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String json3 = mr3.getResponse().getContentAsString();
		assertTrue(json3.equals(""));

		System.out.println("----------------------------Case4: session没有需要的参数------------------------------");
		MvcResult mr4 = mvc.perform( //
				get("/inventoryCommodity/appendToCommodityListEx.bx?commID=2,5&barcodeIDs=1,2&specifications=1,2&packageUnitIDs=1,2&commNames=薯片,可乐") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		String json4 = mr4.getResponse().getContentAsString();
		assertTrue(json4.equals(""));
	}

	@Test
	public void testUpdateNoRealEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("----------------------------Case1：正常修改实盘数量------------------------------");
		MvcResult mr = mvc.perform( //
				get("/inventoryCommodity/updateNoRealEx.bx?ID=1&noReal=1&noSystem=1") //
						.session(sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);

		System.out.println("----------------------------Case2：修改不存在的盘点单商品的实盘数量------------------------------");
		try {
			MvcResult mr1 = mvc.perform( //
					get("/inventoryCommodity/updateNoRealEx.bx?ID=999&noReal=1&noSystem=1") //
							.session(sessionBoss) //
							.contentType(MediaType.APPLICATION_JSON) //
			) //
					.andExpect(status().isOk()) //
					.andDo(print()) //
					.andReturn(); //
			Shared.checkJSONErrorCode(mr1);
		} catch (Exception e) {
			System.out.println(e.getMessage());// 更新缓存时会出现空指针问题
			assertTrue(e.getMessage().equals("Request processing failed; nested exception is java.lang.NullPointerException"));
		}
	}
}
