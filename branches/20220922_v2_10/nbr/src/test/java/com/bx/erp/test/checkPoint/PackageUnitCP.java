package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.PackageUnitBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

public class PackageUnitCP {
	// 1、检查数据库T_PackageUnit是否创建了包装单位A的数据。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		PackageUnit bmOfDB = new PackageUnit();
		bmOfDB = (PackageUnit) bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		PackageUnit packageUnitDefalutValue = (PackageUnit) bmCreateObjet.clone();
		packageUnitDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法");

		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(packageUnitDefalutValue) == 0, "创建失败");

		return true;
	}

	// 1、检查数据库T_PackageUnit是否修改了包装单位A的数据。
	public static boolean verifyUpdate(MvcResult mr, BaseModel bmCreateObjet, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		PackageUnit bmOfDB = new PackageUnit();
		bmOfDB = (PackageUnit) bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		PackageUnit packageUnitDefalutValue = (PackageUnit) bmCreateObjet.clone();
		packageUnitDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法");

		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(packageUnitDefalutValue) == 0, "修改失败");

		return true;
	}

	// 1、检查数据库T_PackageUnit是否删除了包装单位A的数据。
	public static boolean verifyDelete(BaseModel bmCreateObjet, PackageUnitBO packageUnitBO, String dbName) throws Exception {
		// 检查数据库中包装单位是否已经删除
		DataSourceContextHolder.setDbName(dbName);
		PackageUnit bmOfDB = (PackageUnit) packageUnitBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bmCreateObjet);
		if (packageUnitBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找DB中的商品失败，错误码=" + packageUnitBO.getLastErrorCode().toString());
		}
		assertTrue(bmOfDB == null, "数据库的数据没有正确删除");

		return true;
	}

}
