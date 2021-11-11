package com.bx.erp.test;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.sm.StateMachine;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.action.bo.BaseBO;

public class StateMachineMapperTest extends BaseMapperTest {
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

		System.out.println("\n------------------------ Retrieve StateMachine Test ----------------------");

		StateMachine sm = new StateMachine();
		Map<String, Object> p = sm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = stateMachineMapper.retrieveN(p); // ...
		Assert.assertNotNull(sm, "无法获取状态机信息！");
		System.out.println("当前状态机为：" + list);
	}
}
