package com.bx.erp.test.message;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.bx.erp.model.message.Message;
import com.bx.erp.model.message.MessageField;
import com.bx.erp.model.message.MessageItem;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class MessageActionTest extends BaseActionTest {

	private MessageField messageField = new MessageField();

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		// categoryID=1&isRead=1&Parameter=Json&senderID=1&receiverID=2
		// MessageField m = new MessageField();
		MvcResult mr = mvc.perform(post("/message/createEx.bx") //
				.session((MockHttpSession) sessionBoss).param(Message.field.getFIELD_NAME_categoryID(), "" + Message.EnumMessageCategory.EMC_PurchasingTimeout.getIndex() + "") //
				.param(Message.field.getFIELD_NAME_isRead(), "0") //
				.param(Message.field.getFIELD_NAME_parameter(), "Json") //
				.param(Message.field.getFIELD_NAME_senderID(), "1") //
				.param(Message.field.getFIELD_NAME_companyID(), "1")//
				.param(Message.field.getFIELD_NAME_receiverID(), "2") //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------case2:用不存在的MessageCategory创建Message ------------------------");
		MvcResult mr2 = mvc.perform(post("/message/createEx.bx") //
				.session((MockHttpSession) sessionBoss).param(Message.field.getFIELD_NAME_categoryID(), "99999999") //
				.param(Message.field.getFIELD_NAME_isRead(), "0") //
				.param(Message.field.getFIELD_NAME_parameter(), "Json") //
				.param(Message.field.getFIELD_NAME_senderID(), "1") //
				.param(Message.field.getFIELD_NAME_companyID(), "1")//
				.param(Message.field.getFIELD_NAME_receiverID(), "2") //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------case3:用不存在的CompanyID创建Message ------------------------");
		MvcResult mr3 = mvc.perform(post("/message/createEx.bx") //
				.session((MockHttpSession) sessionBoss).param(Message.field.getFIELD_NAME_categoryID(), "99999999") //
				.param(Message.field.getFIELD_NAME_isRead(), "0") //
				.param(Message.field.getFIELD_NAME_parameter(), "Json") //
				.param(Message.field.getFIELD_NAME_senderID(), "1") //
				.param(Message.field.getFIELD_NAME_companyID(), "999999999")//
				.param(Message.field.getFIELD_NAME_receiverID(), "2") //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------case4:没有权限进行操作 ------------------------");
		MvcResult mr4 = mvc.perform(post("/message/createEx.bx") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier))//
				.param(Message.field.getFIELD_NAME_categoryID(), "" + Message.EnumMessageCategory.EMC_PurchasingTimeout.getIndex() + "") //
				.param(Message.field.getFIELD_NAME_isRead(), "0") //
				.param(Message.field.getFIELD_NAME_parameter(), "Json") //
				.param(Message.field.getFIELD_NAME_senderID(), "1") //
				.param(Message.field.getFIELD_NAME_companyID(), "1")//
				.param(Message.field.getFIELD_NAME_receiverID(), "2") //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("正常创建某一个类别的消息，t_messageitem表关于该类别的数据项被清空");
		// categoryID=1&isRead=1&Parameter=Json&senderID=1&receiverID=2
		// MessageField m = new MessageField();
		// 先创建一个该类别的消息项
		MessageItem messageItemCreate = new MessageItem();
		messageItemCreate.setMessageID(1);
		messageItemCreate.setMessageCategoryID(Message.EnumMessageCategory.EMC_PurchasingTimeout.getIndex());
		messageItemCreate.setCommodityID(1);
		String err = messageItemCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramsMessageItemCreate = messageItemCreate.getCreateParam(BaseBO.INVALID_CASE_ID, messageItemCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		MessageItem messageItemCreated = (MessageItem) messageItemMapper.create(paramsMessageItemCreate); // ...
		Assert.assertTrue(messageItemCreated != null && EnumErrorCode.values()[Integer.parseInt(paramsMessageItemCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsMessageItemCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		err = messageItemCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		messageItemCreate.setIgnoreIDInComparision(true);
		if (messageItemCreate.compareTo(messageItemCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// RN数据项，结果应该大于或等于1
		Map<String, Object> paramsRN = messageItemCreated.getRetrieveNParam(BaseBO.INVALID_CASE_ID, messageItemCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> mRN = (List<BaseModel>) messageItemMapper.retrieveN(paramsRN); // ...
		//
		Assert.assertTrue(mRN.size() >= 1 && EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		MvcResult mr = mvc.perform(post("/message/createEx.bx") //
				.session((MockHttpSession) sessionBoss).param(Message.field.getFIELD_NAME_categoryID(), "" + Message.EnumMessageCategory.EMC_PurchasingTimeout.getIndex() + "") //
				.param(Message.field.getFIELD_NAME_isRead(), "0") //
				.param(Message.field.getFIELD_NAME_parameter(), "Json") //
				.param(Message.field.getFIELD_NAME_senderID(), "1") //
				.param(Message.field.getFIELD_NAME_companyID(), "1")//
				.param(Message.field.getFIELD_NAME_receiverID(), "2") //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		// 根据创建的消息分类查询消息数据项，查询结果条数为0
		MessageItem messageItem = new MessageItem();
		messageItem.setMessageCategoryID(Message.EnumMessageCategory.EMC_PurchasingTimeout.getIndex());
		Map<String, Object> paramsRN2 = messageItem.getRetrieveNParam(BaseBO.INVALID_CASE_ID, messageItem);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> mRN2 = (List<BaseModel>) messageItemMapper.retrieveN(paramsRN2); // ...
		//
		Assert.assertTrue(mRN2.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsRN2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void testRetrieve1Ex1() throws Exception {
		Shared.printTestMethodStartInfo();
		MvcResult mr = mvc.perform(get("/message/retrieve1Ex.bx?" + messageField.getFIELD_NAME_ID() + "=1").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)).contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

	}

	@Test
	public void testRetrieve1Ex2() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------case2:没有权限进行操作 ------------------------");
//		MvcResult mr2 = mvc.perform(get("/message/retrieve1Ex.bx?" + messageField.getFIELD_NAME_ID() + "=1").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)).contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(get("/message/retrieveNEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)).contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

	}

	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------case2:没有权限进行操作 ------------------------");
//		MvcResult mr1 = mvc.perform(get("/message/retrieveNEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)).contentType(MediaType.APPLICATION_JSON)) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testUpdateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Message m = createMessage();
		MvcResult mr = mvc
				.perform(get("/message/updateEx.bx?" + messageField.getFIELD_NAME_ID() + "=" + m.getID() + "&" + messageField.getFIELD_NAME_isRead() + "=" + m.getIsRead() + "&" + messageField.getFIELD_NAME_status() + "=" + m.getStatus())
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)).contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Message m = createMessage();
		System.out.println("\n------------------------case2:没有权限进行操作 ------------------------");
		MvcResult mr1 = mvc.perform(//
				get("/message/updateEx.bx?" + messageField.getFIELD_NAME_ID() + "=" + m.getID() + "&" + messageField.getFIELD_NAME_isRead() + "=" + m.getIsRead() + "&" + messageField.getFIELD_NAME_status() + "=" + m.getStatus())
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier))//
						.contentType(MediaType.APPLICATION_JSON)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);

	}

	public Message createMessage() throws CloneNotSupportedException, InterruptedException {

		Message message = DataInput.getMessage();
		Map<String, Object> params = message.getCreateParam(BaseBO.INVALID_CASE_ID, message);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Message messageCreate = (Message) messageMapper.create(params); // ...
		message.setIgnoreIDInComparision(true);
		if (message.compareTo(messageCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(messageCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		return messageCreate;
	}
}
