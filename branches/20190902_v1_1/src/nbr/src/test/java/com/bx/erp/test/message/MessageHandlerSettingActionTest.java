//package com.bx.erp.test.message;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MvcResult;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.message.MessageHandlerSetting;
//import com.bx.erp.model.message.MessageHandlerSettingField;
//import com.bx.erp.test.BaseActionTest;
//import com.bx.erp.test.Shared;
//
//@WebAppConfiguration
//public class MessageHandlerSettingActionTest extends BaseActionTest {
//
//	MessageHandlerSettingField messageHandlerSettingField = new MessageHandlerSettingField();
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
//	public void testCreateEx() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		// categoryID=1&isRead=1&Parameter=Json&senderID=1&receiverID=2
//		// MessageHandlerSetting m = new MessageHandlerSetting();
//		MvcResult mr = mvc.perform(post("/messageHandlerSetting/createEx.bx") //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
//				.param(MessageHandlerSetting.field.getFIELD_NAME_categoryID(), "1") //
//				.param(MessageHandlerSetting.field.getFIELD_NAME_template(), "消息1") //
//				.param(MessageHandlerSetting.field.getFIELD_NAME_link(), "http://www.bxit.vip/shop/order/ID/10") //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr);
//
//		// Case2：用不存在CategoryID创建
//		MvcResult mr1 = mvc.perform(post("/messageHandlerSetting/createEx.bx") //
//				.session((MockHttpSession) sessionBoss)//
//				.param(MessageHandlerSetting.field.getFIELD_NAME_categoryID(), "999999999") //
//				.param(MessageHandlerSetting.field.getFIELD_NAME_template(), "消息1") //
//				.param(MessageHandlerSetting.field.getFIELD_NAME_link(), "http://www.bxit.vip/shop/order/ID/10") //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
//
//		System.out.println("-----------------------case3:没有权限进行操作-------------------------------");
//		MvcResult mr2 = mvc.perform(post("/messageHandlerSetting/createEx.bx") //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//				.param(MessageHandlerSetting.field.getFIELD_NAME_categoryID(), "1") //
//				.param(MessageHandlerSetting.field.getFIELD_NAME_template(), "消息1") //
//				.param(MessageHandlerSetting.field.getFIELD_NAME_link(), "http://www.bxit.vip/shop/order/ID/10") //
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
//	}
//
//	@Test
//	public void testRetrieve1Ex() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		MvcResult mr = mvc.perform(get("/messageHandlerSetting/retrieve1Ex.bx?" + messageHandlerSettingField.getFIELD_NAME_ID() + "=1")//
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr);
//
//		MvcResult mr1 = mvc.perform(get("/messageHandlerSetting/retrieve1Ex.bx?" + messageHandlerSettingField.getFIELD_NAME_ID() + "=1")//
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
//	}
//
//	@Test
//	public void testRetrieveNEx() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		MvcResult mr = mvc.perform(get("/messageHandlerSetting/retrieveNEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr);
//
//		System.out.println("------------------------case2:没有权限进行操作-------------------------------");
//		MvcResult mr1 = mvc.perform(//
//				get("/messageHandlerSetting/retrieveNEx.bx")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
//	}
//
//	@Test
//	public void testUpdateEx() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		MvcResult mr = mvc
//				.perform(get("/messageHandlerSetting/updateEx.bx?" + messageHandlerSettingField.getFIELD_NAME_ID() + "=2&" + messageHandlerSettingField.getFIELD_NAME_categoryID() + "" + "=1&"
//						+ messageHandlerSettingField.getFIELD_NAME_template() + "=消息10&" + messageHandlerSettingField.getFIELD_NAME_link() + "=http://www.bxit.vip/shop/order/ID/10")
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
//								.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr);
//
//		System.out.println("------------------------case2:没有权限进行操作-------------------------------");
//		MvcResult mr1 = mvc
//				.perform(get("/messageHandlerSetting/updateEx.bx?" + messageHandlerSettingField.getFIELD_NAME_ID() + "=2&" + messageHandlerSettingField.getFIELD_NAME_categoryID() + "" + "=1&"
//						+ messageHandlerSettingField.getFIELD_NAME_template() + "=消息10&" + messageHandlerSettingField.getFIELD_NAME_link() + "=http://www.bxit.vip/shop/order/ID/10")
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//								.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
//	}
//
//	@Test
//	public void testDeleteEx() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		MvcResult mr = mvc.perform(get("/messageHandlerSetting/deleteEx.bx?" + messageHandlerSettingField.getFIELD_NAME_ID() + "=1")//
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr);
//
//		System.out.println("------------------------case2:没有权限进行操作-------------------------------");
//		MvcResult mr1 = mvc.perform(get("/messageHandlerSetting/deleteEx.bx?" + messageHandlerSettingField.getFIELD_NAME_ID() + "=1")//
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//				.contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
//	}
//}
