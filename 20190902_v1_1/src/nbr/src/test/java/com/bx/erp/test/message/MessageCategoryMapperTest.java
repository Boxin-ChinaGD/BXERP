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
import com.bx.erp.model.message.MessageCategory;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class MessageCategoryMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void retrieveNTest() {
		Shared.printTestMethodStartInfo();

		MessageCategory mc = new MessageCategory();
		Map<String, Object> params = mc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, mc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> mcRetrieveN = (List<BaseModel>) messageCategoryMapper.retrieveN(params); // ...
		//
		Assert.assertTrue(mcRetrieveN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (BaseModel bm : mcRetrieveN) {
			String err = ((MessageCategory) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		System.out.println(mcRetrieveN.size() == 0 ? "messageCreate == null" : mcRetrieveN);
		//
		System.out.println("【查询多个消息处理配置】测试成功:" + mcRetrieveN);

	}
}
