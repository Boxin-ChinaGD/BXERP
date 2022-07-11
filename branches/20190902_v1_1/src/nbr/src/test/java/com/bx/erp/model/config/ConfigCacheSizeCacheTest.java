package com.bx.erp.model.config;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;

public class ConfigCacheSizeCacheTest extends BaseTestNGSpringContextTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void testLoad() {
		List<BaseModel> listBM = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).readN(false, false);
		for (BaseModel baseModel : listBM) {
			ConfigCacheSize cacheSize = (ConfigCacheSize) baseModel;
			String error = cacheSize.checkUpdate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}
}
