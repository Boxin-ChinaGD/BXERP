package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Vip;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;

@WebAppConfiguration
public class VipSITTest extends BaseMapperTest {
	private Vip vip;

	@BeforeClass
	public void setup() {

		super.setUp();
		// try {
		// session = Shared.getVipLoginSession(mvc, Shared.PhoneOfBoss);
		// vip = (Vip) session.getAttribute(EnumSession.SESSION_Vip.getName());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Vip).deleteAll();

	}

	// @Test TODO  以后统一编写UT
	public void runVipTest() throws InterruptedException {
		Shared.printTestMethodStartInfo();

		VipThread vt1 = new VipThread();
		VipThread vt2 = new VipThread();
		vt1.setName("线程1");
		vt2.setName("线程2");
		vt1.setDbName(Shared.DBName_Test);
		vt2.setDbName(Shared.DBName_Test);
		vt1.setVipID(vip.getID());
		vt2.setVipID(vip.getID());
		vt1.start();
		vt2.start();
		vt1.join();
		vt2.join();
	}

	// @Test 以后统一编写UT
	public void commCacheSize() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		// 1）清空会员缓存。
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Vip).deleteAll();

		// 2）读取N个不同的Vip对象，N大于配置中的个数Q。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setName(EnumConfigCacheSizeCache.ECC_VipCacheSize.getName());
		ccs.setValue("3");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, vip.getID());

		System.out.println(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).readN(false, false));

		for (int i = 1; i <= 5; i++) {
			ErrorInfo ecOut = new ErrorInfo();
			Vip vip = (Vip) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Vip).read1(i, BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false);
			}
			System.out.println(vip);
		}
		System.out.println("----------------" + CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Vip).readN(false, false));
		// 因为设置缓存上限为10，所以顺序插入20个Vip的时候，前10个ID会被删除掉，则list里面ID从11开始，size为10.
		assertTrue(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Vip).readN(false, false).size() == 3, "没有正确删除");

		// 恢复缓存大小
		ccs.setID(EnumConfigCacheSizeCache.ECC_VipCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_VipCacheSize.getName());
		ccs.setValue("50");
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, vip.getID());
	}
}
