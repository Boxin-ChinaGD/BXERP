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
public class PromotionE28Test extends BasePromotionCalculatingTest{
	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();

	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void e28CalculateTest1() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1391, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest2() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1392, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest3() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1393, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest4() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1394, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest5() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1395, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest6() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1396, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest7() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1397, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest8() throws FileNotFoundException {
		int[] iaPromotionID = {};
		if (!loadDataFromDB(Shared.DBName_Test, 1398, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest9() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1399, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest10() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1400, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest11() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1401, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest12() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1402, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest13() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1403, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest14() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1404, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest15() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1405, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest16() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1406, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest17() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1407, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest18() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1408, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest19() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1409, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest20() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1410, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest21() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1411, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest22() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1412, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest23() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1413, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest24() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1414, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest25() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1415, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest26() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1416, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest27() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1417, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest28() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1418, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest29() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1419, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest30() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1420, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest31() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1421, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest32() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1422, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest33() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1423, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest34() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1424, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest35() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1425, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest36() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1426, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest37() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1427, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest38() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1428, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest39() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1429, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest40() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1430, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest41() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1431, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest42() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1432, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest43() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1433, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest44() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1434, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest45() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1435, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest46() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1436, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest47() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1437, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest48() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1438, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest49() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1439, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest50() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1440, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest51() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1441, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest52() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1442, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest53() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1443, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest54() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1444, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest55() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1445, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest56() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1446, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest57() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1447, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
	
	@Test
	public void e28CalculateTest58() throws FileNotFoundException {
		int[] iaPromotionID = {28,29,30,31,32,33,35,36};//ID为34的促销为已删除状态的，不传
		if (!loadDataFromDB(Shared.DBName_Test, 1448, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：验证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}
}
