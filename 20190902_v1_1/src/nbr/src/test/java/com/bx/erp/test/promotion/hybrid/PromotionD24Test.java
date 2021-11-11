package com.bx.erp.test.promotion.hybrid;

import java.io.FileNotFoundException;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.bx.erp.model.RetailTrade;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class PromotionD24Test extends BasePromotionCalculatingTest {
	@Test
	public void d24CalculateTest1() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1333, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest2() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1334, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest3() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1335, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest4() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1336, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest5() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1337, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest6() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1338, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest7() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1339, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest8() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1340, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest9() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1341, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest10() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1342, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest11() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1343, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest12() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1344, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest13() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1345, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest14() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1346, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest15() throws FileNotFoundException {
		int[] iaPromotionID = { 19 };
		if (!loadDataFromDB(Shared.DBName_Test, 1347, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest16() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1348, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest17() throws FileNotFoundException {
		int[] iaPromotionID = { 19 };
		if (!loadDataFromDB(Shared.DBName_Test, 1349, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest18() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1350, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest19() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1351, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest20() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1352, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest21() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1353, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest22() throws FileNotFoundException {
		int[] iaPromotionID = { 19 };
		if (!loadDataFromDB(Shared.DBName_Test, 1354, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest23() throws FileNotFoundException {
		int[] iaPromotionID = { 19 };
		if (!loadDataFromDB(Shared.DBName_Test, 1355, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest24() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1356, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest25() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1357, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest26() throws FileNotFoundException {
		int[] iaPromotionID = { 19 };
		if (!loadDataFromDB(Shared.DBName_Test, 1358, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest27() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1359, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest28() throws FileNotFoundException {
		int[] iaPromotionID = { 19 };
		if (!loadDataFromDB(Shared.DBName_Test, 1360, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest29() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1361, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest30() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1362, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest31() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1363, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest32() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1364, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest33() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1365, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest34() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1366, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest35() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1367, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest36() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1368, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest37() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1369, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest38() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1370, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest39() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1371, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest40() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1372, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest41() throws FileNotFoundException {
		int[] iaPromotionID = { 22 };
		if (!loadDataFromDB(Shared.DBName_Test, 1373, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest42() throws FileNotFoundException {
		int[] iaPromotionID = { 19 };
		if (!loadDataFromDB(Shared.DBName_Test, 1374, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest43() throws FileNotFoundException {
		int[] iaPromotionID = { 20 };
		if (!loadDataFromDB(Shared.DBName_Test, 1375, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest44() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1376, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest45() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1377, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest46() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1378, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest47() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1379, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest48() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1380, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest49() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1381, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest50() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1382, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest51() throws FileNotFoundException {
		int[] iaPromotionID = { 22 };
		if (!loadDataFromDB(Shared.DBName_Test, 1383, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest52() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1384, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest53() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1385, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest54() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1386, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest55() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1387, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest56() throws FileNotFoundException {
		int[] iaPromotionID = { 22 };
		if (!loadDataFromDB(Shared.DBName_Test, 1388, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest57() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1389, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void d24CalculateTest58() throws FileNotFoundException {
		int[] iaPromotionID = { 21 };
		if (!loadDataFromDB(Shared.DBName_Test, 1390, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
}
