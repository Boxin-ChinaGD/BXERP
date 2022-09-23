//package com.bx.erp.test.message;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MvcResult;
//import org.testng.Assert;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.bx.erp.action.BaseAction;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.message.MessageItem;
//import com.bx.erp.model.message.Message.EnumMessageCategory;
//import com.bx.erp.test.BaseActionTest;
//import com.bx.erp.test.Shared;
//
//import net.sf.json.JSONObject;
//
//@WebAppConfiguration
//public class MessageItemActionTest extends BaseActionTest {
//
//	@BeforeClass
//	public void setup() {
//		super.setUp();
//	}
//
//	@AfterClass
//	public void tearDown() {
//		super.tearDown();
//	}
//
//	@Test
//	public void testCreateEx1() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("case1: 传入正确的消息ID、消息分类ID、商品ID，创建成功");
//
//		MessageItem messageItem = new MessageItem();
//		messageItem.setMessageID(1);
//		messageItem.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
//		messageItem.setCommodityID(1);
//
//		MvcResult mr = mvc.perform(post("/messageItem/createEx.bx") //
//				.session((MockHttpSession) sessionBoss) //
//				.param(MessageItem.field.getFIELD_NAME_messageID(), String.valueOf(messageItem.getMessageID())) //
//				.param(MessageItem.field.getFIELD_NAME_messageCategoryID(), String.valueOf(messageItem.getMessageCategoryID())) //
//				.param(MessageItem.field.getFIELD_NAME_commodityID(), String.valueOf(messageItem.getCommodityID())) //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr);
//
//		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());
//
//		MessageItem messageItemParse1 = (MessageItem) new MessageItem().parse1(json.getString(BaseAction.KEY_Object));
//
//		messageItem.setIgnoreIDInComparision(true);
//		Assert.assertTrue(messageItem.compareTo(messageItemParse1) == 0, "创建的对象与DB中读出的不一致");
//	}
//
//	@Test
//	public void testCreateEx2() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("case2: 传入一个不存在的消息ID，创建失败");
//
//		MessageItem messageItem = new MessageItem();
//		messageItem.setMessageID(Shared.BIG_ID);
//		messageItem.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
//		messageItem.setCommodityID(1);
//
//		MvcResult mr = mvc.perform(post("/messageItem/createEx.bx") //
//				.session((MockHttpSession) sessionBoss) //
//				.param(MessageItem.field.getFIELD_NAME_messageID(), String.valueOf(messageItem.getMessageID())) //
//				.param(MessageItem.field.getFIELD_NAME_messageCategoryID(), String.valueOf(messageItem.getMessageCategoryID())) //
//				.param(MessageItem.field.getFIELD_NAME_commodityID(), String.valueOf(messageItem.getCommodityID())) //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
//	}
//
//	@Test
//	public void testCreateEx3() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("case3: 传入一个不存在的消息分类ID，创建失败");
//
//		MessageItem messageItem = new MessageItem();
//		messageItem.setMessageID(1);
//		messageItem.setMessageCategoryID(Shared.BIG_ID);
//		messageItem.setCommodityID(1);
//
//		MvcResult mr = mvc.perform(post("/messageItem/createEx.bx") //
//				.session((MockHttpSession) sessionBoss) //
//				.param(MessageItem.field.getFIELD_NAME_messageID(), String.valueOf(messageItem.getMessageID())) //
//				.param(MessageItem.field.getFIELD_NAME_messageCategoryID(), String.valueOf(messageItem.getMessageCategoryID())) //
//				.param(MessageItem.field.getFIELD_NAME_commodityID(), String.valueOf(messageItem.getCommodityID())) //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
//	}
//
//	@Test
//	public void testCreateEx4() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("case4: 传入一个不存在的商品ID，创建失败");
//
//		MessageItem messageItem = new MessageItem();
//		messageItem.setMessageID(1);
//		messageItem.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
//		messageItem.setCommodityID(Shared.BIG_ID);
//
//		MvcResult mr = mvc.perform(post("/messageItem/createEx.bx") //
//				.session((MockHttpSession) sessionBoss) //
//				.param(MessageItem.field.getFIELD_NAME_messageID(), String.valueOf(messageItem.getMessageID())) //
//				.param(MessageItem.field.getFIELD_NAME_messageCategoryID(), String.valueOf(messageItem.getMessageCategoryID())) //
//				.param(MessageItem.field.getFIELD_NAME_commodityID(), String.valueOf(messageItem.getCommodityID())) //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
//	}
//
//	@Test
//	public void testCreateEx5() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("case5: 没有权限创建(售前账号)");
//
//		MessageItem messageItem = new MessageItem();
//		messageItem.setMessageID(1);
//		messageItem.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
//		messageItem.setCommodityID(1);
//
//		MvcResult mr = mvc.perform(post("/messageItem/createEx.bx") //
//				.param(MessageItem.field.getFIELD_NAME_messageID(), String.valueOf(messageItem.getMessageID())) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//				.param(MessageItem.field.getFIELD_NAME_messageCategoryID(), String.valueOf(messageItem.getMessageCategoryID())) //
//				.param(MessageItem.field.getFIELD_NAME_commodityID(), String.valueOf(messageItem.getCommodityID())) //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
//	}
//
//	@Test
//	public void testRetrieveNEx1() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("case1: 根据消息类别查询");
//		MessageItem messageItem = new MessageItem();
//		messageItem.setMessageID(1);
//		messageItem.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
//		messageItem.setCommodityID(1);
//
//		MvcResult mr = mvc.perform(post("/messageItem/createEx.bx") //
//				.session((MockHttpSession) sessionBoss) //
//				.param(MessageItem.field.getFIELD_NAME_messageID(), String.valueOf(messageItem.getMessageID())) //
//				.param(MessageItem.field.getFIELD_NAME_messageCategoryID(), String.valueOf(messageItem.getMessageCategoryID())) //
//				.param(MessageItem.field.getFIELD_NAME_commodityID(), String.valueOf(messageItem.getCommodityID())) //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr);
//
//		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());
//
//		MessageItem messageItemParse1 = (MessageItem) new MessageItem().parse1(json.getString(BaseAction.KEY_Object));
//
//		messageItem.setIgnoreIDInComparision(true);
//		Assert.assertTrue(messageItem.compareTo(messageItemParse1) == 0, "创建的对象与DB中读出的不一致");
//
//		messageItem.setPageIndex(BaseAction.PAGE_StartIndex);
//		messageItem.setPageSize(BaseAction.PAGE_SIZE_MAX);
//		MvcResult mr2 = mvc.perform(post("/messageItem/retrieveNEx.bx") //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//				.param(MessageItem.field.getFIELD_NAME_messageCategoryID(), String.valueOf(messageItem.getMessageCategoryID())) //
//				.param(MessageItem.field.getFIELD_NAME_pageIndex(), String.valueOf(messageItem.getPageIndex())) //
//				.param(MessageItem.field.getFIELD_NAME_pageSize(), String.valueOf(messageItem.getPageSize())).contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//		Shared.checkJSONErrorCode(mr2);
//
//		JSONObject json2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
//
//		List<BaseModel> bmList = new MessageItem().parseN(json2.getString(BaseAction.KEY_ObjectList));
//		boolean exist = false;
//		for (BaseModel bm : bmList) {
//			if (bm.getID() == messageItemParse1.getID()) {
//				exist = true;
//			}
//		}
//		Assert.assertTrue(exist, "查询失败");
//	}
//
//	@Test
//	public void testRetrieveNEx2() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("case2: 传入不存在的消息分类ID，查询失败");
//		MessageItem messageItem = new MessageItem();
//		messageItem.setMessageID(1);
//		messageItem.setMessageCategoryID(Shared.BIG_ID);
//		messageItem.setCommodityID(1);
//		//
//		MvcResult mr = mvc.perform(post("/messageItem/retrieveNEx.bx") //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//				.param(MessageItem.field.getFIELD_NAME_messageCategoryID(), String.valueOf(messageItem.getMessageCategoryID())) //
//				.param(MessageItem.field.getFIELD_NAME_pageIndex(), String.valueOf(messageItem.getPageIndex())) //
//				.param(MessageItem.field.getFIELD_NAME_pageSize(), String.valueOf(messageItem.getPageSize())).contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
//	}
//
//	@Test
//	public void testRetrieveNEx3() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("case3: 没有权限，查询失败");
//		MessageItem messageItem = new MessageItem();
//		messageItem.setMessageID(1);
//		messageItem.setMessageCategoryID(Shared.BIG_ID);
//		messageItem.setCommodityID(1);
//		//
//		MvcResult mr = mvc.perform(post("/messageItem/retrieveNEx.bx") //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//				.param(MessageItem.field.getFIELD_NAME_messageCategoryID(), String.valueOf(messageItem.getMessageCategoryID())) //
//				.param(MessageItem.field.getFIELD_NAME_pageIndex(), String.valueOf(messageItem.getPageIndex())) //
//				.param(MessageItem.field.getFIELD_NAME_pageSize(), String.valueOf(messageItem.getPageSize())).contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
//	}
//}
