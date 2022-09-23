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
public class PromotionC23Test extends BasePromotionCalculatingTest {
	@Test
	public void c23CalculateTest1() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1271, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest2() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1272, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest3() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1273, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest4() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1274, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest5() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1275, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest6() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1276, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest7() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1277, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest8() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1278, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest9() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1279, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest10() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1280, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest11() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1281, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest12() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 14 };
		if (!loadDataFromDB(Shared.DBName_Test, 1282, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest13() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 14 };
		if (!loadDataFromDB(Shared.DBName_Test, 1283, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest14() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 14, 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1284, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest15() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 14, 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1285, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest16() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1286, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest17() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1287, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest18() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1288, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest19() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1289, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest20() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1290, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest21() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1291, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest22() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1292, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest23() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 14, 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1293, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest24() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1294, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest25() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1295, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest26() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1296, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest27() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1297, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest28() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1298, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest29() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1299, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest30() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1300, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest31() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1301, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest32() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 14, 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1302, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest33() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1303, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest34() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1304, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest35() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1305, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest36() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 14, 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1306, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest37() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1307, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest38() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1308, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest39() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1309, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest40() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1310, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest41() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1311, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest42() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 14, 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1312, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest43() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1313, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest44() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1314, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest45() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1315, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest46() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1316, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest47() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1317, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest48() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1318, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest49() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1319, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest50() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1320, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest51() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1321, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest52() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1322, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest53() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1323, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest54() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1324, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest55() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1325, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest56() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1326, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest57() throws FileNotFoundException {
		int[] iaPromotionID = { 13, 14, 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1327, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void c23CalculateTest58() throws FileNotFoundException {
		int[] iaPromotionID = { 15 };
		if (!loadDataFromDB(Shared.DBName_Test, 1328, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
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
