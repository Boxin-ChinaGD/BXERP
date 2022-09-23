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
public class PromotionF29Test extends BasePromotionCalculatingTest {
	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();

	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void f29CalculateTest1() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1449, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest2() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1450, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest3() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1451, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest4() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38 };

		if (!loadDataFromDB(Shared.DBName_Test, 1452, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest5() throws FileNotFoundException {
		int[] iaPromotionID = { 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1453, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest6() throws FileNotFoundException {
		int[] iaPromotionID = { 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1454, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest7() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1455, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest8() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1456, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest9() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38 };

		if (!loadDataFromDB(Shared.DBName_Test, 1457, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest10() throws FileNotFoundException {
		int[] iaPromotionID = { 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1458, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest11() throws FileNotFoundException {
		int[] iaPromotionID = { 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1459, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest12() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1460, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest13() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38 };

		if (!loadDataFromDB(Shared.DBName_Test, 1461, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest14() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1462, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest15() throws FileNotFoundException {
		int[] iaPromotionID = { 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1463, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest16() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38 };

		if (!loadDataFromDB(Shared.DBName_Test, 1464, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest17() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1465, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest18() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1466, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest19() throws FileNotFoundException {
		int[] iaPromotionID = { 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1467, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest20() throws FileNotFoundException {
		int[] iaPromotionID = { 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1468, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest21() throws FileNotFoundException {
		int[] iaPromotionID = { 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1469, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest22() throws FileNotFoundException {
		int[] iaPromotionID = { 41, 42 };

		if (!loadDataFromDB(Shared.DBName_Test, 1470, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest23() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 41 };

		if (!loadDataFromDB(Shared.DBName_Test, 1471, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest24() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1472, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest25() throws FileNotFoundException {
		int[] iaPromotionID = { 39, 41 };

		if (!loadDataFromDB(Shared.DBName_Test, 1473, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest26() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38 };

		if (!loadDataFromDB(Shared.DBName_Test, 1474, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest27() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1475, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest28() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1476, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest29() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1477, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest30() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1478, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest31() throws FileNotFoundException {
		int[] iaPromotionID = { 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1479, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest32() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 41, 42 };

		if (!loadDataFromDB(Shared.DBName_Test, 1480, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest33() throws FileNotFoundException {
		int[] iaPromotionID = { 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1481, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest34() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1482, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest35() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1483, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest36() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1484, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest37() throws FileNotFoundException {
		int[] iaPromotionID = { 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1485, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest38() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1486, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest39() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38 };

		if (!loadDataFromDB(Shared.DBName_Test, 1487, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest40() throws FileNotFoundException {
		int[] iaPromotionID = { 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1488, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest41() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1489, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest42() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1490, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest43() throws FileNotFoundException {
		int[] iaPromotionID = { 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1491, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest44() throws FileNotFoundException {
		int[] iaPromotionID = { 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1492, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest45() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1493, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest46() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1494, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest47() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1495, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest48() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1496, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest49() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38 };

		if (!loadDataFromDB(Shared.DBName_Test, 1497, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest50() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1498, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest51() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1499, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest52() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 41, 42 };

		if (!loadDataFromDB(Shared.DBName_Test, 1500, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest53() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1501, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest54() throws FileNotFoundException {
		int[] iaPromotionID = { 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1502, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest55() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1503, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest56() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1504, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest57() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39 };

		if (!loadDataFromDB(Shared.DBName_Test, 1505, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void f29CalculateTest58() throws FileNotFoundException {
		int[] iaPromotionID = { 37, 38, 39, 40 };

		if (!loadDataFromDB(Shared.DBName_Test, 1506, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

}
