package com.bx.erp.test.promotion.hybrid;

import org.testng.annotations.Test;

import com.bx.erp.model.RetailTrade;
import com.bx.erp.test.Shared;

import org.testng.annotations.BeforeClass;

import java.io.FileNotFoundException;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

@WebAppConfiguration
public class PromotionM29Test extends BasePromotionCalculatingTest {

	@Test
	public void m29CalculateTest() throws FileNotFoundException {
		int[] iaPromotionID = { 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124 };

		int m29RetailTradeIDStart = 2434;
		int m29RetailTradeIDEnd = 2491;
		for (int i = m29RetailTradeIDStart; i <= m29RetailTradeIDEnd; i++) {
			if (!loadDataFromDB(Shared.DBName_Test, i, iaPromotionID)) {
				Assert.assertTrue(false, "从DB中加载测试数据失败！retailTradeID=" + i);
			}
			//
			Shared.caseLog("当前零售单号：" + i);
			RetailTrade retailTrade = calculate(Shared.DBName_Test);
			// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
			verifyCalculateResult(retailTrade);
		}
	}

	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}

}
