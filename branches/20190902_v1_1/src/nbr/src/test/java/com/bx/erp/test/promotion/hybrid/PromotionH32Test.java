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
public class PromotionH32Test extends BasePromotionCalculatingTest {
	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();

	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}

	int[] iaPromotionID = { 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65 };

	@Test
	public void h32CalculateTest1() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1594, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest2() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1595, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest3() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1596, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest4() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1597, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest5() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1598, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest6() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1599, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest7() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1600, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest8() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1601, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest9() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1602, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest10() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1603, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest11() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1604, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest12() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1605, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest13() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1606, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest14() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1607, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest15() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1608, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest16() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1609, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest17() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1610, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest18() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1611, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest19() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1612, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest20() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1613, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest21() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1614, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest22() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1615, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest23() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1616, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest24() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1617, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest25() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1618, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest26() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1619, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest27() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1620, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest28() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1621, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest29() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1622, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest30() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1623, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest31() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1624, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest32() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1625, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest33() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1626, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest34() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1627, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest35() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1628, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest36() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1629, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest37() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1630, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest38() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1631, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest39() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1632, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest40() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1633, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest41() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1634, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest42() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1635, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest43() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1636, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest44() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1637, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest45() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1638, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest46() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1639, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest47() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1640, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest48() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1641, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest49() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1642, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest50() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1643, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest51() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1644, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest52() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1645, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest53() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1646, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest54() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1647, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest55() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1648, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest56() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1649, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest57() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1650, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest58() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1651, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest59() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1652, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest60() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1653, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest61() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1654, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest62() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1655, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest63() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1656, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest64() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1657, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest65() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1658, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest66() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1659, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void h32CalculateTest67() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1660, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

}
