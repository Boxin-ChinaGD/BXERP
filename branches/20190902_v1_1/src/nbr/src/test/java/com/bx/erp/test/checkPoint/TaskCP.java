package com.bx.erp.test.checkPoint;

import static org.testng.Assert.assertTrue;

import java.util.List;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.report.RetailTradeDailyReportByCategoryParent;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportByStaff;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.model.report.RetailTradeMonthlyReportSummary;
import com.bx.erp.util.GeneralUtil;

/** 该检查点所有的方法，暂时都是用数据库准备好的数据进行检查，所以所有数据都是确定性的，以后可能会更改为传入参数进行检查。 */
public class TaskCP {
	private final static double Monthly_TotalAmount = 3582.46d;
	private final static double Monthly_TotalGross = 749.13d;

	private final static String Staff_Name = "收银员二号";
	private final static int Staff_NO = 8;
	private final static double Staff_TotalAmount = 3582.46d;
	private final static double Staff_TotalGross = 749.13d;

	private final static int CategoryParentID_1 = 2;
	private final static double TotalAmount_CategoryParentID_1 = 1103.68d;

	private final static int CategoryParentID_2 = 3;
	private final static double TotalAmount_CategoryParentID_2 = 2478.78d;

	public static boolean verifyRetailTradeDailyReportSummaryTask(RetailTradeDailyReportSummary rtdps, RetailTradeDailyReportSummary expectDailyReportSummary) {
		assertTrue(rtdps.getTotalNO() == expectDailyReportSummary.getTotalNO(), "TotalNO销售笔数错误！日报表的TotalNO=" + rtdps.getTotalNO());
		assertTrue(Math.abs(GeneralUtil.sub(rtdps.getPricePurchase(), expectDailyReportSummary.getPricePurchase())) < BaseModel.TOLERANCE, "PricePurchase总进货价错误！日报表的PricePurchase=" + rtdps.getPricePurchase());
		assertTrue(Math.abs(GeneralUtil.sub(rtdps.getTotalAmount(), expectDailyReportSummary.getTotalAmount())) < BaseModel.TOLERANCE, "TotalAmount总销售额错误！日报表的TotalAmount=" + rtdps.getTotalAmount());
		assertTrue(Math.abs(GeneralUtil.sub(rtdps.getAverageAmountOfCustomer(), expectDailyReportSummary.getAverageAmountOfCustomer())) < BaseModel.TOLERANCE,
				"AverageAmountOfCustomer客单价错误！日报表的AverageAmountOfCustomer=" + rtdps.getAverageAmountOfCustomer());
		assertTrue(Math.abs(GeneralUtil.sub(rtdps.getTotalGross(), expectDailyReportSummary.getTotalGross())) < BaseModel.TOLERANCE, "totalGross销售毛利错误！日报表的totalGross=" + rtdps.getTotalGross());
		assertTrue(Math.abs(GeneralUtil.sub(rtdps.getRatioGrossMargin(), expectDailyReportSummary.getRatioGrossMargin())) < BaseModel.TOLERANCE, "RatioGrossMargin销售毛利率错误！日报表RatioGrossMargin=" + rtdps.getRatioGrossMargin());
		assertTrue(rtdps.getTopSaleCommodityID() == expectDailyReportSummary.getTopSaleCommodityID(), "TopSaleCommodityID销售额最高的商品错误！日报表的TopSaleCommodityID=" + rtdps.getTopSaleCommodityID());
		assertTrue(rtdps.getTopSaleCommodityNO() == expectDailyReportSummary.getTopSaleCommodityNO(), "TopSaleCommodityNO销售额最高的商品的数量错误！日报表的TopSaleCommodityNO=" + rtdps.getTopSaleCommodityNO());
		assertTrue(Math.abs(GeneralUtil.sub(rtdps.getTopSaleCommodityAmount(), expectDailyReportSummary.getTopSaleCommodityAmount())) < BaseModel.TOLERANCE,
				"TopSaleCommodityAmount销售额最高的商品的销售额错误！日报表的TopSaleCommodityAmount=" + rtdps.getTopSaleCommodityAmount());
		return true;
	}

	public static boolean verifyRetailTradeDailyReportByCommodityTask(List<RetailTradeDailyReportByCommodity> retailTradeDailyReportByCommodityList, List<RetailTradeDailyReportByCommodity> expectDailyReportByCommodityList) {

		if (retailTradeDailyReportByCommodityList.size() == expectDailyReportByCommodityList.size()) {
			for (int i = 0; i < retailTradeDailyReportByCommodityList.size(); i++) {
				RetailTradeDailyReportByCommodity reportByCommodity = retailTradeDailyReportByCommodityList.get(i);
				Boolean exist = false;
				for (int j = 0; j < expectDailyReportByCommodityList.size(); j++) {
					RetailTradeDailyReportByCommodity expectReportByCommodity = expectDailyReportByCommodityList.get(j);
					if (expectReportByCommodity.getCommodityID() == reportByCommodity.getCommodityID()) {
						assertTrue(expectReportByCommodity.getSpecification().equals(reportByCommodity.getSpecification()), "商品规格不正确！");
						assertTrue(Math.abs(GeneralUtil.sub(expectReportByCommodity.getTotalPurchasingAmount(), reportByCommodity.getTotalPurchasingAmount())) < BaseModel.TOLERANCE, "商品总进货价不正确！");
						assertTrue(expectReportByCommodity.getNO() == reportByCommodity.getNO(), "商品售出数量不正确！");
						assertTrue(Math.abs(GeneralUtil.sub(expectReportByCommodity.getTotalAmount(), reportByCommodity.getTotalAmount())) < BaseModel.TOLERANCE, "商品总销售额不正确！");
						assertTrue(Math.abs(GeneralUtil.sub(expectReportByCommodity.getGrossMargin(), reportByCommodity.getGrossMargin())) < BaseModel.TOLERANCE, "商品毛利不正确！");
						exist = true;
						break;
					}
				}
				if (!exist) {
					assertTrue(false, "查询不到期望的商品报表：" + retailTradeDailyReportByCommodityList);
				}
			}
		}
		return true;
	}

	public static boolean verifyRetailTradeMonthlyReportSummaryTask(RetailTradeMonthlyReportSummary rtmrs) {
		assertTrue(Math.abs(GeneralUtil.sub(rtmrs.getTotalAmount(), Monthly_TotalAmount)) < BaseModel.TOLERANCE, "TotalAmount总销售额错误！");
		assertTrue(Math.abs(GeneralUtil.sub(rtmrs.getTotalGross(), Monthly_TotalGross)) < BaseModel.TOLERANCE, "totalGross销售毛利错误！");
		return true;
	}

	public static boolean verifyRetailTradeMonthlyReportSummaryTask(RetailTradeMonthlyReportSummary rtmrs, RetailTradeMonthlyReportSummary expectrtMonthlyReportSummary) {
		assertTrue(Math.abs(GeneralUtil.sub(rtmrs.getTotalAmount(), expectrtMonthlyReportSummary.getTotalAmount())) < BaseModel.TOLERANCE, "TotalAmount总销售额错误！月报表生成的TotalAmount=" + rtmrs.getTotalAmount());
		assertTrue(Math.abs(GeneralUtil.sub(rtmrs.getTotalGross(), expectrtMonthlyReportSummary.getTotalGross())) < BaseModel.TOLERANCE, "totalGross销售毛利错误！月报表生成的totalGross=" + rtmrs.getTotalGross());
		return true;
	}

	public static boolean verifyRetailTradeDailyReportByStaffTask(List<RetailTradeDailyReportByStaff> retailTradeDailyReportByStaffList) {
		RetailTradeDailyReportByStaff reportByStaff = retailTradeDailyReportByStaffList.get(0);
		assertTrue(Staff_Name.equals(reportByStaff.getStaffName()), "用户名称不正确！");
		assertTrue(Staff_NO == reportByStaff.getNO(), "商品售出数量不正确！");
		assertTrue(Math.abs(GeneralUtil.sub(Staff_TotalAmount, reportByStaff.getTotalAmount())) < BaseModel.TOLERANCE, "商品总销售额不正确！");
		assertTrue(Math.abs(GeneralUtil.sub(Staff_TotalGross, reportByStaff.getGrossMargin())) < BaseModel.TOLERANCE, "商品毛利不正确！");
		return true;
	}

	public static boolean verifyRetailTradeDailyReportByCategoryParentTask(List<RetailTradeDailyReportByCategoryParent> retailTradeDailyReportByCategoryParentList) {
		for (RetailTradeDailyReportByCategoryParent rtdpByCategoryParent : retailTradeDailyReportByCategoryParentList) {
			if (rtdpByCategoryParent.getCategoryParentID() == CategoryParentID_1) {
				assertTrue(Math.abs(GeneralUtil.sub(TotalAmount_CategoryParentID_1, rtdpByCategoryParent.getTotalAmountSummary())) < BaseModel.TOLERANCE, "商品总销售额不正确！");
			} else if (rtdpByCategoryParent.getCategoryParentID() == CategoryParentID_2) {
				assertTrue(Math.abs(GeneralUtil.sub(TotalAmount_CategoryParentID_2, rtdpByCategoryParent.getTotalAmountSummary())) < BaseModel.TOLERANCE, "商品总销售额不正确！");
			}
		}

		return true;
	}
}
