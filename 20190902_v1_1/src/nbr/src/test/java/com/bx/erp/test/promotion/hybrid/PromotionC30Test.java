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
public class PromotionC30Test extends BasePromotionCalculatingTest {
	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}
	
	@Test
	public void c30CalculateTest() throws FileNotFoundException {
		int[] iaPromotionID = { 171, 172, 173, 174, 175, 176, 177, 178};
		
		int c30RetailTradeIDStart = 2731;
		int c30RetailTradeIDEnd = 2788;
		for (int i = c30RetailTradeIDStart; i < c30RetailTradeIDEnd + 1; i++) {
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
