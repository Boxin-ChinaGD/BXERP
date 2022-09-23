package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.commodity.CommodityCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;

/** 测试CacheManager在多线程环境中读写缓存的 正确性、安全性。污染到缓存的地方会在测试后清除 */
@WebAppConfiguration
public class CommodityThreadSITTest extends BaseActionTest {

	private Staff staff;

	@BeforeClass
	public void setup() {
		super.setUp();

		try {
			staff = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "模拟用户登录失败！");
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();

		try {
			MvcResult mr2 = mvc.perform( //
					get("/staff/logoutEx.bx") //
							.session((MockHttpSession) sessionBoss) //
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andDo(print()).andReturn();
			Shared.checkJSONErrorCode(mr2);
			System.out.println();
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	/** 多线程环境下线程对缓存进行读写操作的正确性和安全性测试 */
	@Test
	public void runCommodityProcess() throws InterruptedException {
		Shared.printTestMethodStartInfo();

		CommodityThread ct1 = new CommodityThread();
		ct1.setName("线程1");
		ct1.setDbName(Shared.DBName_Test);
		ct1.setStaffID(staff.getID());
		//
		CommodityThread ct2 = new CommodityThread();
		ct2.setName("线程2");
		ct2.setDbName(Shared.DBName_Test);
		ct2.setStaffID(staff.getID());
		//
		CommodityThread ct3 = new CommodityThread();
		ct3.setName("线程3");
		ct3.setDbName(Shared.DBName_Test);
		ct3.setStaffID(staff.getID());
		//
		ct1.start();
		ct2.start();
		ct3.start();
		// 打乱一下顺序
		ct2.join();
		ct1.join();
		ct3.join();

		CommodityCache sc = (CommodityCache) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity);
		List<BaseModel> bmList = sc.readN(false, false);
		System.out.println("线程执行后：" + bmList);
		Assert.assertTrue(bmList.size() == 0);
	}
}
