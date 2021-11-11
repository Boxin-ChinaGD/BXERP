package com.bx.erp.test.message;

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
import com.bx.erp.model.message.MessageItem;
import com.bx.erp.model.message.Message;
import com.bx.erp.model.message.Message.EnumMessageCategory;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class MessageItemMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static MessageItem msiInput = null;

		protected static final MessageItem getMessageItem() throws CloneNotSupportedException, InterruptedException {
			Random ran = new Random();
			msiInput = new MessageItem();
			msiInput.setMessageID(ran.nextInt(5) + 1);
			msiInput.setMessageCategoryID(ran.nextInt(5) + 1);
			msiInput.setCommodityID(ran.nextInt(5) + 1);
			return (MessageItem) msiInput.clone();
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

	@Test
	public void createTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case1: 正常添加");
		MessageItem messageItem = DataInput.getMessageItem();
		messageItem.setMessageID(1);
		messageItem.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		messageItem.setCommodityID(1);
		createMessageItem(messageItem);
	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2: 传入一个不存在的消息ID，创建失败");
		MessageItem messageItem = DataInput.getMessageItem();
		messageItem.setMessageID(Shared.BIG_ID);
		messageItem.setMessageCategoryID(8);
		messageItem.setCommodityID(1);
		String err = messageItem.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = messageItem.getCreateParam(BaseBO.INVALID_CASE_ID, messageItem);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		MessageItem messageItemCreate = (MessageItem) messageItemMapper.create(params); // ...
		Assert.assertTrue(messageItemCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case3: 传入一个不存在的消息分类ID，创建失败");
		MessageItem messageItem = DataInput.getMessageItem();
		messageItem.setMessageID(1);
		messageItem.setMessageCategoryID(Shared.BIG_ID);
		messageItem.setCommodityID(1);
		String err = messageItem.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = messageItem.getCreateParam(BaseBO.INVALID_CASE_ID, messageItem);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		MessageItem messageItemCreate = (MessageItem) messageItemMapper.create(params); // ...
		Assert.assertTrue(messageItemCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case4: 传入一个不存在的商品ID，创建失败");
		MessageItem messageItem = DataInput.getMessageItem();
		messageItem.setMessageID(1);
		messageItem.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		messageItem.setCommodityID(Shared.BIG_ID);
		String err = messageItem.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = messageItem.getCreateParam(BaseBO.INVALID_CASE_ID, messageItem);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		MessageItem messageItemCreate = (MessageItem) messageItemMapper.create(params); // ...
		Assert.assertTrue(messageItemCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 传入正确的消息分类ID，查询成功");

		MessageItem messageItem = new MessageItem();
		messageItem.setMessageCategoryID(Message.EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		retrieveNMessageItem(messageItem);
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: 传入不存在的消息分类ID，查询失败");

		MessageItem messageItem = new MessageItem();
		messageItem.setMessageCategoryID(Shared.BIG_ID);
		Map<String, Object> paramsRN = messageItem.getRetrieveNParam(BaseBO.INVALID_CASE_ID, messageItem);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> mRN = (List<BaseModel>) messageItemMapper.retrieveN(paramsRN); // ...
		//
		Assert.assertTrue(mRN.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	protected MessageItem createMessageItem(MessageItem messageItem) {
		String err = messageItem.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = messageItem.getCreateParam(BaseBO.INVALID_CASE_ID, messageItem);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		MessageItem messageItemCreate = (MessageItem) messageItemMapper.create(params); // ...
		Assert.assertTrue(messageItemCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		err = messageItemCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		messageItem.setIgnoreIDInComparision(true);
		if (messageItem.compareTo(messageItemCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		return messageItemCreate;
	}

	protected List<BaseModel> retrieveNMessageItem(MessageItem messageItem) {

		Map<String, Object> paramsRN = messageItem.getRetrieveNParam(BaseBO.INVALID_CASE_ID, messageItem);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> mRN = (List<BaseModel>) messageItemMapper.retrieveN(paramsRN); // ...
		//
		Assert.assertTrue(mRN.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (BaseModel bm : mRN) {
			String err = ((MessageItem) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		System.out.println("【查询消息】测试成功:" + mRN);
		//
		return mRN;
	}
}
