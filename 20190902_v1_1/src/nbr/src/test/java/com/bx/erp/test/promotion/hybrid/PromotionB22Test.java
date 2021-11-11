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
public class PromotionB22Test extends BasePromotionCalculatingTest {
	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();

	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void b22CalculateTest1() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 9 };
		if (!loadDataFromDB(Shared.DBName_Test, 1207, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest2() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1208, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest3() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1209, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest4() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1210, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest5() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1211, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest6() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1212, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest7() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1213, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest8() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 7, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1214, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest9() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 7, 8, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1215, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest10() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1216, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest11() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1217, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest12() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1218, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest13() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1219, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest14() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1220, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest15() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1221, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest16() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1222, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest17() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1223, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest18() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1224, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest19() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1225, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest20() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1226, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest21() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1227, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest22() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 7, 8, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1228, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest23() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 7, 8, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1229, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest24() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 7, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1230, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest25() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1231, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest26() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1232, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest27() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1233, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest28() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1234, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest29() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1235, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest30() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 7, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1236, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest31() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1237, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest32() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1238, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest33() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1239, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest34() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1240, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest35() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1241, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest36() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1242, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest37() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1243, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest38() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1244, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest39() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1245, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest40() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1246, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest41() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1247, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest42() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1248, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest43() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1249, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest44() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1250, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest45() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1251, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest46() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1252, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest47() throws FileNotFoundException {
		int[] iaPromotionID = {};

		if (!loadDataFromDB(Shared.DBName_Test, 1253, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest48() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1254, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest49() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1255, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest50() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1256, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest51() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1257, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest52() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1258, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest53() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 7, 8, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1259, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest54() throws FileNotFoundException {
		int[] iaPromotionID = { 6, 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1260, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest55() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1261, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest56() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1262, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest57() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1263, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void b22CalculateTest58() throws FileNotFoundException {
		int[] iaPromotionID = { 9 };

		if (!loadDataFromDB(Shared.DBName_Test, 1264, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

}
