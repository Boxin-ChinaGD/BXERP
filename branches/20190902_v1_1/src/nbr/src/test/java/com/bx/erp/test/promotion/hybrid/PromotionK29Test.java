package com.bx.erp.test.promotion.hybrid;

import org.testng.annotations.Test;

import com.bx.erp.model.RetailTrade;
import com.bx.erp.test.Shared;

import org.testng.annotations.BeforeClass;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class PromotionK29Test extends BasePromotionCalculatingTest {
	@Test
	public void k29CalculateTest() throws FileNotFoundException {
		int[] iaPromotionID = { 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273 };

		int m29RetailTradeIDStart = 3264;
		int m29RetailTradeIDEnd = 3323;
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
