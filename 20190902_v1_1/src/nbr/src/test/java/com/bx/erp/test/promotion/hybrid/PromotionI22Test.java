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
public class PromotionI22Test extends BasePromotionCalculatingTest {
	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();

	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}

	int[] iaPromotionID = { 66, 67, 68, 69, 70, 71, 72, 73, 74, 75 };

	@Test
	public void i22CalculateTest1() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1661, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest2() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1662, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest3() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1663, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest4() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1664, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest5() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1665, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest6() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1666, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest7() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1667, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest8() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1668, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest9() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1669, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest10() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1670, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest11() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1671, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest12() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1672, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest13() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1673, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest14() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1674, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest15() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1675, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest16() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1676, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest17() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1677, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest18() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1678, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest19() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1679, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest20() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1680, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest21() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1681, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest22() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1682, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest23() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1683, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest24() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1684, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest25() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1685, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest26() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1686, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest27() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1687, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest28() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1688, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest29() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1689, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest30() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1690, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest31() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1691, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest32() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1692, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest33() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1693, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest34() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1694, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest35() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1695, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest36() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1696, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest37() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1697, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest38() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1698, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest39() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1699, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest40() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1700, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest41() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1701, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest42() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1702, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest43() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1703, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest44() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1704, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest45() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1705, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest46() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1706, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest47() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1707, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest48() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1708, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest49() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1709, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest50() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1710, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest51() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1711, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest52() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1712, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest53() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1713, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest54() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1714, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest55() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1715, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest56() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1716, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest57() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1717, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest58() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1718, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void i22CalculateTest59() throws FileNotFoundException {
		if (!loadDataFromDB(Shared.DBName_Test, 1719, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}
		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

}
