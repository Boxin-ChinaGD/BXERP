package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CategoryParentBO;
import com.bx.erp.action.bo.commodity.CategorySyncCacheBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.model.commodity.CategorySyncCache;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

public class CategoryParentCP {
	// 1、检查商品大类A普通缓存是否创建。
	// 2、检查数据库T_CategoryParent是否创建了商品大类A的数据。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		CategoryParent bmOfDB = new CategoryParent();
		// bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		bmOfDB = (CategoryParent) bmOfDB.parse1(json.getString("categoryParent"));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		CategoryParent categoryParentDefalutValue = (CategoryParent) bmCreateObjet.clone();
		categoryParentDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);

		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(categoryParentDefalutValue) == 0, "创建失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法");

		// 1、检查商品大类A普通缓存是否创建。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_CategoryParent, dbName);

		return true;
	}

	// 1、检查商品大类A普通缓存是否修改。
	// 2、检查数据库T_CategoryParent是否修改了商品大类A的数据。
	public static boolean verifyUpdate(MvcResult mr, BaseModel bmCreateObjet, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		CategoryParent bmOfDB = new CategoryParent();
		// bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		bmOfDB = (CategoryParent) bmOfDB.parse1(json.getString("categoryParent"));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		CategoryParent categoryParentDefalutValue = (CategoryParent) bmCreateObjet.clone();
		categoryParentDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);

		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(categoryParentDefalutValue) == 0, "修改失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法");

		// 1、检查商品大类A普通缓存是否修改。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_CategoryParent, dbName);

		return true;
	}

	// 1、检查商品大类A普通缓存是否删除。
	// 2、检查数据库T_CategoryParent是否删除了商品大类A的数据。
	// 3、若商品大类A有关联商品小类，则要检查关联的商品小类的普通缓存和DB数据是否删除，D型同步块和同步块缓存是否创建
	public static boolean verifyDelete(BaseModel bmCreateObjet, List<BaseModel> categoryList, CategoryParentBO categoryParentBO, CategoryBO categoryBO, PosBO posBO, CategorySyncCacheBO categorySyncCacheBO, String dbName) throws Exception {
		// 检查数据库中商品大类A是否已经删除
		DataSourceContextHolder.setDbName(dbName);
		CategoryParent bmOfDB = (CategoryParent) categoryParentBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bmCreateObjet);
		if (categoryParentBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找DB中的商品大类失败，错误码=" + categoryParentBO.getLastErrorCode().toString());
		}
		assertTrue(bmOfDB == null, "数据库的数据没有正确删除");

		// 1、检查商品大类A普通缓存是否删除。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_CategoryParent).read1(bmCreateObjet.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找普通缓存失败，错误码=" + ecOut.getErrorCode().toString());
		}
		Assert.assertTrue(bm == null, "普通缓存没有正确删除");

		// 3、若商品大类A有关联商品小类，则要检查关联的商品小类的普通缓存和DB数据是否删除，D型同步块和同步块缓存是否创建
		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		for (BaseModel bmCategory : categoryList) {
			Category c = (Category) bmCategory;
			// 3-1、检查商品大类A关联的商品小类的DB数据是否有删除
			DataSourceContextHolder.setDbName(dbName);
			Category dbCategory = (Category) categoryBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, c);
			if (categoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查找DB中的商品小类失败，错误码=" + categoryBO.getLastErrorMessage().toString());
			}
			assertTrue(dbCategory == null, "数据库的数据没有正确删除");// 删除商品大类A会把关联的商品小类一起删除

			// 3-2、检查商品大类A关联的商品小类的普通缓存是否删除。
			BaseModel category = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).read1(c.getID(), BaseBO.SYSTEM, ecOut, dbName);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查找普通缓存失败，错误码=" + ecOut.getErrorCode().toString());
			}
			Assert.assertTrue(category == null, "普通缓存没有正确删除");

			if (list.size() > 1) {
				DataSourceContextHolder.setDbName(dbName);
				List<?> categorySyncCaches = categorySyncCacheBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, c);
				assertTrue(categorySyncCacheBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "查找小类DB同步块失败！");
				// 如何证明c存在于DB中，使用哨兵值
				boolean exist = false;
				for (int i = 0; i < categorySyncCaches.size(); i++) {
					CategorySyncCache categorySyncCache = (CategorySyncCache) categorySyncCaches.get(i);
					if (categorySyncCache.getSyncData_ID() == c.getID()) {
						exist = true;
						Assert.assertTrue(SyncCache.SYNC_Type_D.equals(categorySyncCache.getSyncType()), "删除小类后还存在着该小类除D型外的其他同步块");
					}
				}
				Assert.assertTrue(exist, "删除小类后并未生成D型同步块");
				//
				DataSourceContextHolder.setDbName(dbName);
				List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_CategorySyncInfo).readN(false, false);
				exist = false;
				for (int i = 0; i < bmSyncCacheList.size(); i++) {
					BaseSyncCache categorySyncCache = (BaseSyncCache) bmSyncCacheList.get(i);
					if (categorySyncCache.getSyncData_ID() == c.getID()) {
						exist = true;
						Assert.assertTrue(SyncCache.SYNC_Type_D.equals(categorySyncCache.getSyncType()), "删除小类后还存在着该小类除D型外的其他同步块缓存");
					}
				}
				Assert.assertTrue(exist, "删除小类后并未创建D型同步块缓存");
			}

		}
		return true;
	}

}
