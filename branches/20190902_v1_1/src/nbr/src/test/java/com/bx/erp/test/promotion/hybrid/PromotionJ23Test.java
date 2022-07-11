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
public class PromotionJ23Test extends BasePromotionCalculatingTest {
	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();

	}

	@AfterClass
	public void afterClass() {
		Shared.printTestClassEndInfo();
	}

	int[] iaPromotionID = { 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 };

	@Test
	public void j23CalculateTest1() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1720, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest2() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1721, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest3() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1722, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest4() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1723, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest5() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1724, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest6() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1725, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest7() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1726, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest8() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1727, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest9() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1728, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest10() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1729, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest11() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1730, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest12() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1731, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest13() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1732, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest14() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1733, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest15() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1734, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest16() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1735, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest17() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1736, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest18() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1737, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest19() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1738, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest20() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1739, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest21() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1740, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest22() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1741, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest23() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1742, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest24() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1743, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest25() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1744, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest26() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1745, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest27() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1746, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest28() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1747, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest29() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1748, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest30() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1749, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest31() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1750, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest32() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1751, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest33() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1752, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest34() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1753, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest35() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1754, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest36() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1755, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest37() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1756, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest38() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1757, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest39() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1758, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest40() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1759, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest41() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1760, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest42() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1761, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest43() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1762, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest44() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1763, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest45() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1764, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest46() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1765, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest47() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1766, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest48() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1767, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest49() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1768, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest50() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1769, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest51() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1770, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest52() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1771, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest53() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1772, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest54() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1773, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest55() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1774, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest56() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1775, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest57() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1776, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest58() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1777, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest59() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1778, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest60() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1779, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest61() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1780, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest62() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1781, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest63() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1782, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest64() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1783, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest65() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1784, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest66() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1785, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest67() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1786, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest68() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1787, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest69() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1788, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest70() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1789, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest71() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1790, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest72() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1791, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

	@Test
	public void j23CalculateTest73() throws FileNotFoundException {

		if (!loadDataFromDB(Shared.DBName_Test, 1792, iaPromotionID)) {
			Assert.assertTrue(false, "从DB中加载测试数据失败！");
		}

		RetailTrade retailTrade = calculate(Shared.DBName_Test);
		// 结果验证：印证应收款、各商品的退货价及最优的促销的ID是否和蓝图中的一样
		verifyCalculateResult(retailTrade);
	}

}
