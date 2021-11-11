package com.bx.erp.test.message;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.message.MessageItem;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class MessageMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static Message msInput = null;

		protected static final Message getMessage() throws CloneNotSupportedException, InterruptedException {
			Random ran = new Random();
			msInput = new Message();
			msInput.setCategoryID(ran.nextInt(5) + 1);
			msInput.setCompanyID(1);
			msInput.setIsRead(0);
			msInput.setParameter("Json");
			msInput.setSenderID(ran.nextInt(5) + 1);
			msInput.setReceiverID(ran.nextInt(5) + 1);

			return (Message) msInput.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	protected Message createMessage(Message message) {
		String err = message.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = message.getCreateParam(BaseBO.INVALID_CASE_ID, message);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Message messageCreate = (Message) messageMapper.create(params); // ...
		if (messageCreate != null) { // 正常创建消息
			//
			Assert.assertTrue(messageCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			err = messageCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			message.setIgnoreIDInComparision(true);
			if (message.compareTo(messageCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			//
			System.out.println("【创建消息】测试成功:" + messageCreate);
		} else { // 使用不合法的字段创建消息
			Assert.assertTrue(messageCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("创建对象失败  :" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		return messageCreate;
	}

	protected List<BaseModel> rnMessages(Message message) {

		Map<String, Object> paramsRN = message.getRetrieveNParam(BaseBO.INVALID_CASE_ID, message);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> mRN = (List<BaseModel>) messageMapper.retrieveN(paramsRN); // ...
		//
		Assert.assertTrue(mRN.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (BaseModel bm : mRN) {
			String err = ((Message) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		System.out.println("【查询消息】测试成功:" + mRN);
		//
		return mRN;
	}

	protected List<BaseModel> retrieveNForWx(Message message) {
		Map<String, Object> params = message.getRetrieveNParam(BaseBO.CASE_Message_RetrieveNForWx, message);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> mRNorWX = (List<BaseModel>) messageMapper.messageRetrieveNForWx(params); // ...
		//
		Assert.assertTrue(mRNorWX.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (BaseModel bm : mRNorWX) {
			String err = ((Message) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			assertEquals(err, "");
		}
		//
		System.out.println("【查询消息】测试成功:" + mRNorWX);
		return mRNorWX;
	}

	@Test
	public void createTest_CASE1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1：正常创建消息 ");

		Message message = DataInput.getMessage();
		createMessage(message);
	}

	@Test
	public void createTest_CASE2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:用不存在的MessageCategory创建Message");

		Message message = DataInput.getMessage();
		message.setCategoryID(99999999);
		createMessage(message);
	}

	@Test
	public void createTest_CASE3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:用不存在的CompanyID创建Message");

		Message message = DataInput.getMessage();
		message.setCompanyID(999999999);
		createMessage(message);
	}

	@Test
	public void createTest_CASE4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4：正常创建某一个类别的消息，t_messageitem表关于该类别的数据项被清空");

		Message message = DataInput.getMessage();
		String err = message.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		// 先创建一个该类别的消息项
		MessageItem messageItemCreate = new MessageItem();
		messageItemCreate.setMessageID(1);
		messageItemCreate.setMessageCategoryID(message.getCategoryID());
		messageItemCreate.setCommodityID(1);
		err = messageItemCreate.checkCreate(BaseBO.INVALID_CASE_ID);
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
		//
		Map<String, Object> params = message.getCreateParam(BaseBO.INVALID_CASE_ID, message);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Message messageCreate = (Message) messageMapper.create(params); // ...
		//
		Assert.assertTrue(messageCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		err = messageCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		message.setIgnoreIDInComparision(true);
		if (message.compareTo(messageCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 根据创建的消息分类查询消息数据项，查询结果条数为0
		MessageItem messageItem = new MessageItem();
		messageItem.setMessageCategoryID(message.getCategoryID());
		Map<String, Object> paramsRN2 = messageItem.getRetrieveNParam(BaseBO.INVALID_CASE_ID, messageItem);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> mRN2 = (List<BaseModel>) messageItemMapper.retrieveN(paramsRN2); // ...
		//
		Assert.assertTrue(mRN2.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsRN2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void updateTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Update Message Test ");

		// 创建Message
		Message message = DataInput.getMessage();
		Message messageCreate = createMessage(message);

		// 修改Message
		Message messageU = new Message();
		messageU.setID(messageCreate.getID());
		messageU.setIsRead(1);
		Map<String, Object> paramsUpdate = messageU.getUpdateParam(BaseBO.INVALID_CASE_ID, messageU);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Message messageUpdate = (Message) messageMapper.update(paramsUpdate); // ...
		//
		Assert.assertTrue(messageUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err = messageUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		messageCreate.setIgnoreIDInComparision(true);
		if (messageCreate.compareTo(messageUpdate) == 0) {
			Assert.assertTrue(false, "修改对象失败");
		}
		//
		System.out.println(messageUpdate == null ? "messageUpdate == null" : messageUpdate);
		//
		System.out.println("【修改消息】测试成功:" + messageUpdate);
	}

	@Test
	public void retrieve1Test() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Retrieve1 Message Test ");

		Message message = new Message();
		message.setID(1);
		Map<String, Object> params = message.getRetrieve1Param(BaseBO.INVALID_CASE_ID, message);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Message messageRetrieve1 = (Message) messageMapper.retrieve1(params); // ...
		//
		Assert.assertTrue(messageRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		String err = messageRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		System.out.println("【查询单个消息】测试成功:" + messageRetrieve1);

	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" RetrieveN Message Test ");

		Message message = new Message();
		rnMessages(message);
	}

	@Test
	public void retrieveNForWxTest_CASE1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:根据status查询 ");

		Message message = new Message();
		message.setStatus(0);
		message.setCompanyID(-1);
		retrieveNForWx(message);
	}

	@Test
	public void retrieveNForWxTest_CASE2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Case2:根据companyID查询 ");

		Message message = new Message();
		message.setStatus(-1);
		message.setCompanyID(1);
		retrieveNForWx(message);
	}

	@Test
	public void retrieveNForWxTest_CASE3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Case3:查询全部 ");

		Message message = new Message();
		message.setStatus(-1);
		message.setCompanyID(-1);
		retrieveNForWx(message);
	}

	@Test
	public void retrieveNForWxTest_CASE4() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Case4:根据companyID和Status查询 ");

		Message message = new Message();
		message.setStatus(0);
		message.setCompanyID(1);
		retrieveNForWx(message);
	}

	@Test
	public void updateStatusTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Update Message Test ");

		// 创建Message
		Message message = DataInput.getMessage();
		Message messageCreate = createMessage(message);

		// 修改Message
		Message messageU = new Message();
		messageU.setID(messageCreate.getID());
		Map<String, Object> paramsUpdateStatus = messageU.getUpdateParam(BaseBO.CASE_Message_UpdateStatus, messageU);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Message messageUpdateStatus = (Message) messageMapper.updateStatus(paramsUpdateStatus);
		//
		Assert.assertTrue(messageUpdateStatus != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdateStatus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsUpdateStatus.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err = messageUpdateStatus.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		messageCreate.setIgnoreIDInComparision(true);
		if (messageCreate.compareTo(messageUpdateStatus) == 0) {
			Assert.assertTrue(false, "修改对象失败");
		}
		//
		System.out.println("【修改消息】测试成功:" + messageUpdateStatus);
	}
}
