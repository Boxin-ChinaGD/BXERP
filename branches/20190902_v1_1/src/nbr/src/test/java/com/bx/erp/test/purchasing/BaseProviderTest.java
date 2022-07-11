package com.bx.erp.test.purchasing;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseProviderTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void tearDown() {
	}

	public static List<BaseModel> queryViaMapper(Provider provider, String dbName) {
		Map<String, Object> param = provider.getRetrieveNParam(BaseBO.INVALID_CASE_ID, provider);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> list = providerMapper.retrieveN(param);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象失败");
		return list;
	}
}
