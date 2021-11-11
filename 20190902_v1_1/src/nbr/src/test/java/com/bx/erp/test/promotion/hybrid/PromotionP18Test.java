package com.bx.erp.test.promotion.hybrid;

import java.io.FileNotFoundException;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.RetailTrade;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class PromotionP18Test extends BasePromotionCalculatingTest {
	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}
	
	@Test
	public void p18CalculateTest() throws FileNotFoundException {
		int[] iaPromotionID = { 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163};

		int p18RetailTradeIDStart = 2608;
		int p18RetailTradeIDEnd = 2668;
		for (int i = p18RetailTradeIDStart; i < p18RetailTradeIDEnd + 1; i++) {
			if (!loadDataFromDB(Shared.DBName_Test, i, iaPromotionID)) {
				Assert.assertTrue(false, "从DB中加载测试数据失败！retailTradeID=" + i);
			}
			//
			RetailTrade retailTrade = calculate(Shared.DBName_Test);
			// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
			verifyCalculateResult(retailTrade);
		}
	}
}
