package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.purchasing.ProviderBO;
import com.bx.erp.action.bo.purchasing.ProviderDistrictBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderDistrict;
import com.bx.erp.util.DataSourceContextHolder;
import net.sf.json.JSONObject;

public class ProviderCP {
	// 1、检查数据库T_Proveider是否创建了供应商A的数据。
	// 2、检查供应商A的普通缓存是否创建。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		Provider bmOfDB = new Provider();
		bmOfDB = (Provider) bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		Provider providerDefalutValue = (Provider) bmCreateObjet.clone();
		providerDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);

		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(providerDefalutValue) == 0, "创建失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法:" + error);

		// 1、检查供应商A普通缓存是否创建。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_Provider, dbName);

		return true;
	}

	// 1、检查数据库T_Proveider是否修改了供应商A的数据。
	// 2、检查供应商A的普通缓存是否修改。
	// 3、如果传入了providerDistrict，需要检查地域的缓存和数据库数据是否正常创建。
	public static boolean verifyUpdate(MvcResult mr, BaseModel bmCreateObjet, ProviderDistrictBO providerDistrictBO, String dbName) throws Exception {
		// 获取修改供应商时传入的地域名称
		String providerDistrict = mr.getRequest().getParameter("providerDistrict");

		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		Provider bmOfDB = new Provider();
		bmOfDB = (Provider) bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		Provider providerDefalutValue = (Provider) bmCreateObjet.clone();
		providerDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);
		// 如果修改时传入了地域名称，需要先将新创建的地域ID设置到对象中，再进行比较。
		if (!StringUtils.isEmpty(providerDistrict)) {
			providerDefalutValue.setDistrictID(bmOfDB.getDistrictID());
			// 检查传入的地域是否正常创建
			ProviderDistrict pd = new ProviderDistrict();
			pd.setID(bmOfDB.getDistrictID());
			//
			DataSourceContextHolder.setDbName(dbName);
			ProviderDistrict providerDistrictR1 = (ProviderDistrict) providerDistrictBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, pd);
			if (providerDistrictBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查找供应商地域失败，错误码=" + providerDistrictBO.getLastErrorCode().toString());
			}
			assertTrue(providerDistrictR1.getName().equals(providerDistrict), "数据库和返回的对象名称不一致");
		}
		//
		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(providerDefalutValue) == 0, "修改失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法:" + error);

		// 1、检查供应商A普通缓存是否修改。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_Provider, dbName);
		return true;
	}

	// 1、检查数据库T_Proveider是否删除了供应商A的数据。
	// 2、检查供应商A的普通缓存是否删除。
	public static boolean verifyDelete(BaseModel bmCreateObjet, ProviderBO providerBO, String dbName) throws Exception {
		// 检查数据库中供应商A是否已经删除
		DataSourceContextHolder.setDbName(dbName);
		Provider bmOfDB = (Provider) providerBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bmCreateObjet);
		if (providerBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找DB中的供应商失败，错误码=" + providerBO.getLastErrorCode().toString());
		}
		assertTrue(bmOfDB == null, "数据库的数据没有正确删除");

		// 1、检查类别A普通缓存是否删除。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Provider).read1(bmCreateObjet.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找普通缓存失败，错误码=" + ecOut.getErrorCode().toString());
		}
		Assert.assertTrue(bm == null, "普通缓存没有正确删除");

		return true;
	}
}
