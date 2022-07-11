package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.Pos;
import com.bx.erp.model.BaseModel;

import net.sf.json.JSONObject;

public class PosCP {
	// pos的其它检查点都在BaseTestNGSpringContextTest.java
	// 1、检查checkCreate
	// 2、compareTo
	public static boolean verifyRecycleApp(MvcResult mr, BaseModel oldPos, String dbName) throws Exception {
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Pos pos = new Pos();
		pos = (Pos) pos.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertTrue(pos != null, "没有得到返回结果");
		Assert.assertTrue("".equals(pos.getSalt()));
		Assert.assertTrue("".equals(pos.getPasswordInPOS()));
		//
		pos.setIgnoreIDInComparision(true);
		if (pos.compareTo(oldPos) != 0) {
			assertTrue(false, "查找的对象的字段与DB读出的不相等");
		}
		// 因为在PosAction中返回时，把Salt和PasswordInPOS设为了""，所以不能用checkCreate方法比较
		// String err = pos.checkCreate(BaseBO.INVALID_CASE_ID);
		// assertTrue(err.equals(""), err);
		return true;
	}
}
