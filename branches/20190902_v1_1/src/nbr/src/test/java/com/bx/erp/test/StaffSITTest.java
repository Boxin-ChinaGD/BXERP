package com.bx.erp.test;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.CacheType.EnumCacheType;

@WebAppConfiguration
public class StaffSITTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Staff).deleteAll();

	}

	@Test
	public void runStaffProcess() throws InterruptedException {
		Shared.printTestMethodStartInfo();

		StaffThread st1 = new StaffThread();
		st1.setName("线程1");
		st1.setDBName(Shared.DBName_Test);
		StaffThread st2 = new StaffThread();
		st2.setName("线程2");
		st2.setDBName(Shared.DBName_Test);

		st1.start();
		st2.start();
		st1.join();
		st2.join();
	}
}
