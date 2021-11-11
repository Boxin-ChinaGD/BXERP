package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.purchasing.ProviderDistrictBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.ProviderDistrict;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

public class ProviderDistrictCP {
	// 1、检查供应商区域A的普通缓存是否创建。
	// 1、检查数据库T_ProviderDistrict是否创建了供应商区域A的数据。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		ProviderDistrict bmOfDB = new ProviderDistrict();
		bmOfDB = (ProviderDistrict) bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		ProviderDistrict providerDistrictDefalutValue = (ProviderDistrict) bmCreateObjet.clone();
		providerDistrictDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);

		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(providerDistrictDefalutValue) == 0, "创建失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法:" + error);

		// 1、检查供应商A区域普通缓存是否创建。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_ProviderDistrict, dbName);

		return true;
	}

	// 1、检查供应商区域A的普通缓存是否修改。
	// 1、检查数据库T_ProviderDistrict是否修改了供应商区域A的数据。
	public static boolean verifyUpdate(MvcResult mr, BaseModel bmCreateObjet, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		ProviderDistrict bmOfDB = new ProviderDistrict();
		bmOfDB = (ProviderDistrict) bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		ProviderDistrict providerDistrictDefalutValue = (ProviderDistrict) bmCreateObjet.clone();
		providerDistrictDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);
		//
		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(providerDistrictDefalutValue) == 0, "修改失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法:" + error);

		// 1、检查供应商区域A普通缓存是否修改。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_ProviderDistrict, dbName);
		return true;
	}

	// 1、检查供应商区域A的普通缓存是否删除。
	// 1、检查数据库T_ProviderDistrict是否删除了供应商区域A的数据。
	public static boolean verifyDelete(BaseModel bmCreateObjet, ProviderDistrictBO providerDistrictBO, String dbName) throws Exception {
		// 检查数据库中供应商区域A是否已经删除
		DataSourceContextHolder.setDbName(dbName);
		ProviderDistrict bmOfDB = (ProviderDistrict) providerDistrictBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bmCreateObjet);
		if (providerDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找DB中的供应商区域失败，错误码=" + providerDistrictBO.getLastErrorCode().toString());
		}
		assertTrue(bmOfDB == null, "数据库的数据没有正确删除");

		// 1、检查类别A普通缓存是否删除。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_ProviderDistrict).read1(bmCreateObjet.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找普通缓存失败，错误码=" + ecOut.getErrorCode().toString());
		}
		Assert.assertTrue(bm == null, "普通缓存没有正确删除");

		return true;
	}
}
