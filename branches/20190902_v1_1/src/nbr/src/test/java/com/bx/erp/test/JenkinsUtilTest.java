package com.bx.erp.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.publiC.CompanyCache;
import com.bx.erp.model.Vip;
import com.bx.erp.util.DataSourceContextHolder;

public class JenkinsUtilTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}
	
	@Resource
	private CompanyCache companyCache;
	
	private Vip retrieve1VipViaMapper(Vip vip) {		
		Map<String, Object> params = vip.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vip);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		return (Vip) vipMapper.retrieve1(params);
	}

	@Test
	public void refreshDB() throws IOException, InterruptedException {
		Shared.printTestMethodStartInfo();

		File fileRefreshDB = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\sql\\TableCreate\\RefreshDB.db");
		File fileDBRefreshed = new File("D:\\BXERP\\branches\\20190902_v1_1\\src\\nbr\\src\\sql\\TableCreate\\DBRefreshed.db");
		fileRefreshDB.delete();
		fileDBRefreshed.delete();

		System.out.println("正在通知小王子刷新DB......");
		fileRefreshDB.createNewFile();
		FileOutputStream fosRefreshDB = new FileOutputStream(fileRefreshDB);
		fosRefreshDB.write("RefreshDB...".getBytes());
		fosRefreshDB.close();

		System.out.println("检查DB是否已经刷新......");
		//
		int timeout = 240;
		while (timeout-- > 0) {
			if (fileDBRefreshed.exists()) {
				// 不要影响Jenkins的其它任务
				fileRefreshDB.delete();
				fileDBRefreshed.delete();

				System.out.println("DB已经成功刷新！正在验证DB已经就绪......");
				Thread.sleep(15 * 1000); //DB可能未就绪

				// 验证DB就绪，以备后面的测试跑起来
				Vip vip = new Vip();
				vip.setID(Shared.DEFAULT_VIP_ID);
				Vip vipR1 = null;
				do {
					vipR1 = retrieve1VipViaMapper(vip);
					if (vipR1 != null) {
						break;
					} else {
						System.out.println("DB未就绪！等待一下再重试......");
						Thread.sleep(15 * 1000);
					}
				} while (true);

				System.out.println("DB已经就绪！");
				
				companyCache.loadForTestNGTest();
				System.out.println("刷新DB后重新加载缓存成功！如果上次刷新时，夜间任务线程未停止，那么 本次不会再启动那些线程。");

				return;
			}
			Thread.sleep(1000);
		}

		// 不要影响Jenkins的其它任务
		fileRefreshDB.delete();
		fileDBRefreshed.delete();

		System.out.println("DB刷新超时！~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		Assert.assertTrue(false, "DB刷新超时！");
	}
}
