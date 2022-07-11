package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.commodity.CategoryBO;
import com.bx.erp.action.bo.commodity.CategorySyncCacheBO;
import com.bx.erp.action.bo.commodity.CategorySyncCacheDispatcherBO;
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
import com.bx.erp.model.commodity.CategorySyncCacheDispatcher;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

public class CategoryCP {
	// 1、检查商品类别A普通缓存是否创建。
	// 2、检查商品类别A是否创建了C型同步块缓存。
	// 3、检查数据库T_Category是否创建了商品类别A的数据。
	// 4、检查数据库T_CategorySyncCache是否创建了商品类别A的C型同步块。
	public static boolean verifyCreate(MvcResult mr, BaseModel bmCreateObjet, PosBO posBO, CategorySyncCacheBO categorySyncCacheBO, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		Category bmOfDB = new Category();
		bmOfDB = (Category) bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		Category categoryDefalutValue = (Category) bmCreateObjet.clone();
		categoryDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);

		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(categoryDefalutValue) == 0, "创建失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法");

		// 1、检查类别A普通缓存是否创建。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_Category, dbName);

		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 2、检查类别A是否创建了C型同步块缓存。
			BaseSyncCache categorySyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(bmOfDB, EnumSyncCacheType.ESCT_CategorySyncInfo, SyncCache.SYNC_Type_C, dbName);
			Assert.assertTrue(categorySyncCache != null, "同步缓存不存在创建出来的对象");
			// 4、检查数据库T_CategorySyncCache是否创建了类别A的C型同步块。
			BaseSyncCache categorySyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(categorySyncCacheBO, bmOfDB, categorySyncCache, SyncCache.SYNC_Type_C, dbName);
			Assert.assertTrue(categorySyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
		}

		return true;
	}

	// 1、检查商品类别A普通缓存是否修改。
	// 2、检查商品类别A是否创建了U型同步块缓存。
	// 3、检查数据库T_Category是否修改了商品类别A的数据。
	// 4、检查数据库T_CategorySyncCache是否创建了商品类别A的U型同步块。
	public static boolean verifyUpdate(MvcResult mr, BaseModel bmCreateObjet, PosBO posBO, CategorySyncCacheBO categorySyncCacheBO, String dbName) throws Exception {
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject json = JSONObject.fromObject(mr.getResponse().getContentAsString());

		Category bmOfDB = new Category();
		bmOfDB.parse1(json.getString(BaseAction.KEY_Object));
		Assert.assertFalse(bmOfDB == null);
		// 设置对象默认值后进行比较
		Category categoryDefalutValue = (Category) bmCreateObjet.clone();
		categoryDefalutValue.setDefaultValueToCreate(BaseBO.INVALID_CASE_ID);

		bmOfDB.setIgnoreIDInComparision(true);
		Assert.assertTrue(bmOfDB.compareTo(categoryDefalutValue) == 0, "修改失败");

		// 检查返回字段是否合法
		String error = bmOfDB.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(error.equals(""), "数据库中的数据不合法");

		// 1、检查类别A普通缓存是否修改。
		CheckPoint.verifyFromCacheIfCacheExists(true, bmOfDB, EnumCacheType.ECT_Category, dbName);

		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 2、检查类别A是否创建了U型同步块缓存。
			BaseSyncCache categorySyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(bmOfDB, EnumSyncCacheType.ESCT_CategorySyncInfo, SyncCache.SYNC_Type_U, dbName);
			Assert.assertTrue(categorySyncCache != null, "同步缓存不存在创建出来的对象");
			// 4、检查数据库T_CategorySyncCache是否创建了类别A的U型同步块。
			BaseSyncCache categorySyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(categorySyncCacheBO, bmOfDB, categorySyncCache, SyncCache.SYNC_Type_U, dbName);
			Assert.assertTrue(categorySyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
		}

		return true;
	}

	// 1、检查商品类别A普通缓存是否删除。
	// 2、检查商品类别A是否创建了D型同步块缓存。
	// 3、检查数据库T_Category是否删除了商品类别A的数据。
	// 4、检查数据库T_CategorySyncCache是否创建了商品类别A的D型同步块。
	public static boolean verifyDelete(BaseModel bmCreateObjet, PosBO posBO, CategoryBO categoryBO, CategorySyncCacheBO categorySyncCacheBO, String dbName) throws Exception {
		// 检查数据库中类别A是否已经删除
		DataSourceContextHolder.setDbName(dbName);
		Category bmOfDB = (Category) categoryBO.retrieve1Object(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, bmCreateObjet);
		if (categoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找DB中的商品失败，错误码=" + categoryBO.getLastErrorCode().toString());
		}
		assertTrue(bmOfDB == null, "数据库的数据没有正确删除");

		// 1、检查类别A普通缓存是否删除。
		ErrorInfo ecOut = new ErrorInfo();
		BaseModel bm = CacheManager.getCache(dbName, EnumCacheType.ECT_Category).read1(bmCreateObjet.getID(), BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "查找普通缓存失败，错误码=" + ecOut.getErrorCode().toString());
		}
		Assert.assertTrue(bm == null, "普通缓存没有正确删除");

		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		if (list.size() > 1) {
			// 2、检查类别A是否创建了D型同步块缓存。
			BaseSyncCache categorySyncCache = CheckPoint.verifyFromCacheIfSyncCacheExists(bmCreateObjet, EnumSyncCacheType.ESCT_CategorySyncInfo, SyncCache.SYNC_Type_D, dbName);
			Assert.assertTrue(categorySyncCache != null, "同步缓存不存在创建出来的对象");
			// 4、检查数据库T_CategorySyncCache是否创建了类别A的D型同步块。
			BaseSyncCache categorySyncCacheDB = CheckPoint.verifyFromDBIfSyncCacheExists(categorySyncCacheBO, bmCreateObjet, categorySyncCache, SyncCache.SYNC_Type_D, dbName);
			Assert.assertTrue(categorySyncCacheDB != null, "同步缓存DB不存在创建出来的对象");
		}

		return true;
	}

	// 1、检查商品类别A的同步块缓存，查看ListSlave1()中是否存在POS1的同步记录。
	// 2、检查数据库T_CategorySyncCacheDispatcher是否创建了商品类别A的POS1同步记录。
	@SuppressWarnings("unchecked")
	public static boolean verifySyncCategory(List<Category> categoryList, int posID, PosBO posBO, CategorySyncCacheDispatcherBO cacheDispatcherBO, String dbName) throws Exception {
		// 检查有效的pos机
		List<Pos> list = Shared.getPosesFromDB(posBO, dbName);
		assert list != null;
		// 1、检查类别A的同步块缓存，查看ListSlave1()中是否存在POS1的同步记录。
		if (list.size() > 1) {
			CategorySyncCacheDispatcher categorySyncCacheDispatcher = new CategorySyncCacheDispatcher();
			// 获取数据库中同步块数据
			DataSourceContextHolder.setDbName(dbName);
			List<CategorySyncCacheDispatcher> cscdList = (List<CategorySyncCacheDispatcher>) cacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, categorySyncCacheDispatcher);
			if (cacheDispatcherBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查找同步调度表失败，错误码=" + cacheDispatcherBO.getLastErrorCode().toString());
			}

			boolean posIDInListSlave1;
			boolean posIDInSyncCacheDispatcherDB;
			List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_CategorySyncInfo).readN(false, false);
			for (Category c : categoryList) {
				posIDInListSlave1 = false;
				posIDInSyncCacheDispatcherDB = false;

				BaseSyncCache baseSyncCache = null;
				// 如果类别的int1为1，代表这个类别已经完全同步，同步块主表和从表都已被删除
				if (c.getIsSync() == 1) {
					for (BaseModel bm : bmSyncCacheList) {
						baseSyncCache = (BaseSyncCache) bm;
						if (baseSyncCache.getSyncData_ID() == c.getID()) {
							assertTrue(false, "该同步块已经完全同步，缓存中不应该还有该同步块！");
						}
					}
				} else {
					for (BaseModel bm : bmSyncCacheList) {
						baseSyncCache = (BaseSyncCache) bm;
						if (baseSyncCache.getSyncData_ID() == c.getID()) {
							break;
						}
					}
					assertTrue(baseSyncCache.getSyncData_ID() == c.getID(), "同步块不存在");

					// 检查该同步块的从表信息是否有POS机同步的数据
					CategorySyncCacheDispatcher cscdOfCache = new CategorySyncCacheDispatcher();
					for (Object o : baseSyncCache.getListSlave1()) {
						cscdOfCache = (CategorySyncCacheDispatcher) o;
						if (cscdOfCache.getPos_ID() == posID) {
							posIDInListSlave1 = true;
							break;
						}
					}
					if (!posIDInListSlave1) {
						assertTrue(false, "同步块从表没有正确插入。");
					}
					// 检查数据库的数据是否和缓存中的数据相等
					for (CategorySyncCacheDispatcher bscdOfDB : cscdList) {
						if (cscdOfCache.getID() == bscdOfDB.getID() && bscdOfDB.compareTo(cscdOfCache) == 0) {
							posIDInSyncCacheDispatcherDB = true;
							break;
						}
					}
					if (!posIDInSyncCacheDispatcherDB) {
						assertTrue(false, "同步块从表没有正确插入数据库。");
					}
				}
			}
		}
		return true;
	}
}
