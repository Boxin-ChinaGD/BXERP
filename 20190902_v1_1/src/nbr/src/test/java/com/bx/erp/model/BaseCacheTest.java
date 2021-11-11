package com.bx.erp.model;

import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class BaseCacheTest extends BaseTestNGSpringContextTest {

	@Resource
	private WebApplicationContext wac;

	private MockMvc mvc;

	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();

		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("utf-8");
		encodingFilter.setForceEncoding(true);

		mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(encodingFilter, "/*").build();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();

	}

	@Test
	public void testCloneData() {
		Shared.printTestMethodStartInfo();

		List<BaseModel> brandListAsc = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Brand).readN(true, true);
		for (int i = 1; i < brandListAsc.size(); i++) {
			assertTrue(brandListAsc.get(i - 1).getID() < brandListAsc.get(i).getID(), "排序异常！！！");
		}

		List<BaseModel> brandListDesc = CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Brand).readN(true, false);
		for (int i = 1; i < brandListDesc.size(); i++) {
			assertTrue(brandListDesc.get(i - 1).getID() > brandListDesc.get(i).getID(), "排序异常！！！");
		}

	}
}
