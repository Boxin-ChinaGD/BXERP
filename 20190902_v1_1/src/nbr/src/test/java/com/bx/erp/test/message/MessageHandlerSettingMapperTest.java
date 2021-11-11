package com.bx.erp.test.message;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.message.MessageHandlerSetting;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class MessageHandlerSettingMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static MessageHandlerSetting mhsInput = null;

		protected static final MessageHandlerSetting getMessageHandlerSetting() throws CloneNotSupportedException, InterruptedException {
			mhsInput = new MessageHandlerSetting();
			mhsInput.setCategoryID(1);
			mhsInput.setTemplate("消息" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			mhsInput.setLink("http://www.bxit.vip/shop/order/ID/" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			return (MessageHandlerSetting) mhsInput.clone();
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

	protected MessageHandlerSetting createMessageHandlerSetting(MessageHandlerSetting mhs) {
		String err = mhs.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = mhs.getCreateParam(BaseBO.INVALID_CASE_ID, mhs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		MessageHandlerSetting mhsCreate = (MessageHandlerSetting) messageHandlerSettingMapper.create(params); // ...
		//
		if (mhsCreate != null) { // 创建成功
			Assert.assertTrue(mhsCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			err = mhsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			mhs.setIgnoreIDInComparision(true);
			if (mhs.compareTo(mhsCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			//
			System.out.println("【创建消息处理配置】测试成功:" + mhsCreate);
		} else { // 创建失败
			Assert.assertTrue(mhsCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("创建对象失败: " + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return mhsCreate;
	}

	protected void deleteMessageHanlderSetting(MessageHandlerSetting mhsDelete) {
		Map<String, Object> paramsDelete = mhsDelete.getDeleteParam(BaseBO.INVALID_CASE_ID, mhsDelete);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		messageHandlerSettingMapper.delete(paramsDelete); // ...
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramsR1 = mhsDelete.getRetrieve1Param(BaseBO.INVALID_CASE_ID, mhsDelete);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		MessageHandlerSetting mhsR1 = (MessageHandlerSetting) messageHandlerSettingMapper.retrieve1(paramsR1);
		//
		Assert.assertTrue(mhsR1 == null && EnumErrorCode.values()[Integer.parseInt(paramsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("【删除消息处理配置】测试成功");
	}

	@Test
	public void createTest_CASE1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 正常创建消息处理");

		MessageHandlerSetting mhs = DataInput.getMessageHandlerSetting();
		MessageHandlerSetting mhsCreate = createMessageHandlerSetting(mhs);
		// 删除创建的数据，以免影响别的测试
		deleteMessageHanlderSetting(mhsCreate);
	}

	@Test
	public void createTest_CASE2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2：用不存在CategoryID创建");

		MessageHandlerSetting mhs = DataInput.getMessageHandlerSetting();
		mhs.setCategoryID(999999999);
		createMessageHandlerSetting(mhs);
		// 使用不合法的字段进行创建对象失败，不用删除对象
	}

	@Test
	public void updateTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		MessageHandlerSetting mhs = DataInput.getMessageHandlerSetting();
		MessageHandlerSetting mhsCreate = createMessageHandlerSetting(mhs);

		// 修改MessageHandlerSetting
		MessageHandlerSetting mhsUpdate = new MessageHandlerSetting();
		mhsUpdate.setID(mhsCreate.getID());
		mhsUpdate.setCategoryID(mhsCreate.getCategoryID());
		mhsUpdate.setTemplate("消息" + String.valueOf(System.currentTimeMillis()).substring(6));
		mhsUpdate.setLink(mhsCreate.getLink());
		//
		String err = mhsUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> mhsParamsU = mhsUpdate.getUpdateParam(BaseBO.INVALID_CASE_ID, mhsUpdate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		MessageHandlerSetting mhsU = (MessageHandlerSetting) messageHandlerSettingMapper.update(mhsParamsU); // ...
		//
		Assert.assertTrue(mhsU != null && EnumErrorCode.values()[Integer.parseInt(mhsParamsU.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, mhsParamsU.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		err = mhsU.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		mhsCreate.setIgnoreIDInComparision(true);
		if (mhsCreate.compareTo(mhsU) == 0) {
			Assert.assertTrue(false, "修改消息类别失败");
		}
		//
		System.out.println(mhsU == null ? "messageCreate == null" : mhsU);
		//
		System.out.println("【更新消息处理配置】测试成功:" + mhsU);
		// 删除创建的数据，以免影响别的测试
		deleteMessageHanlderSetting(mhsUpdate);
	}

	@Test
	public void retrieve1Test() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		MessageHandlerSetting mhs = new MessageHandlerSetting();
		mhs.setID(2);
		Map<String, Object> params = mhs.getRetrieve1Param(BaseBO.INVALID_CASE_ID, mhs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		MessageHandlerSetting mhsRetrieve1 = (MessageHandlerSetting) messageHandlerSettingMapper.retrieve1(params); // ...
		//
		Assert.assertTrue(mhsRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建消息处理成功");
		String err = mhsRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		System.out.println(mhsRetrieve1 == null ? "messageCreate == null" : mhsRetrieve1);
		//
		System.out.println("【查询单个消息处理配置】测试成功:" + mhsRetrieve1);

	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		MessageHandlerSetting mhs = new MessageHandlerSetting();
		Map<String, Object> params = mhs.getRetrieveNParam(BaseBO.INVALID_CASE_ID, mhs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> mhsRetrieveN = (List<BaseModel>) messageHandlerSettingMapper.retrieveN(params); // ...
		//
		Assert.assertTrue(mhsRetrieveN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建消息处理失败");
		for (BaseModel bm : mhsRetrieveN) {
			String err = ((MessageHandlerSetting) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		System.out.println(mhsRetrieveN.size() == 0 ? "messageCreate == null" : mhsRetrieveN);
		//
		System.out.println("【查询多个消息处理配置】测试成功:" + mhsRetrieveN);

	}

	@Test
	public void deleteTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		MessageHandlerSetting mhs = DataInput.getMessageHandlerSetting();
		MessageHandlerSetting mhsCreate = createMessageHandlerSetting(mhs);
		// 删除MessageHandlerSetting
		deleteMessageHanlderSetting(mhsCreate);
	}
}
