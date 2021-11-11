package com.bx.erp.test.promotion.hybrid;

import org.testng.annotations.Test;

import com.bx.erp.model.RetailTrade;
import com.bx.erp.test.Shared;

import org.testng.annotations.BeforeClass;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class PromotionL24Test extends BasePromotionCalculatingTest {

	@Test
	public void l24CalculateTest() throws FileNotFoundException {
		int[] iaPromotionID = { 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284 };

		int l24RetailTradeIDStart = 3324;
		int l24RetailTradeIDEnd = 3383;
		for (int i = l24RetailTradeIDStart; i <= l24RetailTradeIDEnd; i++) {
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
