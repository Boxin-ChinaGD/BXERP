package com.bx.erp.test.task.retailTrade;

import static org.testng.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.report.RetailTradeDailyReport;
import com.bx.erp.model.report.RetailTradeDailyReportByCategoryParent;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportByStaff;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
import com.bx.erp.model.report.RetailTradeMonthlyReportSummary;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

public class BaseRetailTradeMonthlyReportTest extends BaseActionTest {
	protected SimpleDateFormat sdf;
	protected SimpleDateFormat sdf2;
	//
	protected RetailTradeDailyReport retailTradeDailyReport = null;
	protected List<RetailTradeDailyReportSummary> listRetailTradeDailyReportSummary = null;
	protected List<RetailTradeDailyReportByStaff> listRetailTradeDailyReportByStaff = null;
	protected List<RetailTradeDailyReportByCategoryParent> listRetailTradeDailyReportByCategoryParent = null;

	@BeforeClass
	public void setup() {
		super.setUp();
		sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		sdf2 = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);

		listRetailTradeDailyReportSummary = new ArrayList<RetailTradeDailyReportSummary>();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	protected void verifyRetailTradeMonthlyReportByStaff(String monthlyReporyByStaffData, String dtStart, String dtEnd) {
		try {
			String[] staffData = monthlyReporyByStaffData.split(";");
			Integer[] staffIDs = GeneralUtil.toIntArray(staffData[0]); // 员工ID
			Double[] staffTotalAmount = GeneralUtil.toDoubleArray(staffData[1]); // 员工月销售总额
			Double[] staffTotalGross = GeneralUtil.toDoubleArray(staffData[2]); // 员工月总销售毛利
			Integer[] staffTotalNO = GeneralUtil.toIntArray(staffData[3]); // 员工月销售单数
			if (staffIDs == null || staffTotalAmount == null || staffTotalGross == null || staffTotalNO == null) {
				Assert.assertTrue(false, "传入的数组参数为空");
			}
			if (staffIDs.length != staffTotalAmount.length || staffIDs.length != staffTotalGross.length || staffIDs.length != staffTotalNO.length) {
				Assert.assertTrue(false, "传入的数组维度不一样");
			}
			// 查询到当月的所有员工报表汇总
			RetailTradeDailyReportByStaff reportByStaff = new RetailTradeDailyReportByStaff();
			reportByStaff.setDatetimeStart(sdf.parse(dtStart));
			reportByStaff.setDatetimeEnd(sdf.parse(dtEnd));
			//
			Map<String, Object> params = reportByStaff.getRetrieveNParam(BaseBO.INVALID_CASE_ID, reportByStaff);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> bmList = retailTradeDailyReportByStaffMapper.retrieveN(params);
			// 验证当月大类报表的总额和占比
			for (BaseModel bm : bmList) {
				RetailTradeDailyReportByStaff rs = (RetailTradeDailyReportByStaff) bm;
				for (int i = 0; i < staffIDs.length; i++) {
					if (rs.getStaffID() == staffIDs[i]) {
						assertTrue((rs.getTotalAmount() - staffTotalAmount[i]) < BaseModel.TOLERANCE, "员工月销售总额不正确！");
						assertTrue((rs.getGrossMargin() - staffTotalGross[i]) < BaseModel.TOLERANCE, "员工月总销售毛利不正确！");
						assertTrue((rs.getNO() - staffTotalNO[i]) < BaseModel.TOLERANCE, "员工月销售单数不正确！");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void verifyRetailTradeMonthlyReportByCategoryParent(String categoryParent, String dtStart, String dtEnd) {
		try {
			String[] category = categoryParent.split(";");
			Integer[] categoryPanrentIDs = GeneralUtil.toIntArray(category[0]); // 商品大类ID
			Double[] categoryPanrentTotalAmount = GeneralUtil.toDoubleArray(category[1]); // 商品大类的销售额
			Double[] categoryPanrentTotalAmountProportion = GeneralUtil.toDoubleArray(category[2]); // 商品大类的销售占比
			if (categoryPanrentIDs == null || categoryPanrentTotalAmount == null || categoryPanrentTotalAmountProportion == null) {
				Assert.assertTrue(false, "传入的数组参数为空");
			}
			if (categoryPanrentIDs.length != categoryPanrentTotalAmount.length || categoryPanrentIDs.length != categoryPanrentTotalAmountProportion.length) {
				Assert.assertTrue(false, "传入的数组维度不一样");
			}
			// 查询到当月的所有大类报表
			RetailTradeDailyReportByCategoryParent reportByCategoryParent = new RetailTradeDailyReportByCategoryParent();
			reportByCategoryParent.setDatetimeStart(sdf.parse(dtStart));
			reportByCategoryParent.setDatetimeEnd(sdf.parse(dtEnd));
			//
			Map<String, Object> params = reportByCategoryParent.getRetrieveNParam(BaseBO.INVALID_CASE_ID, reportByCategoryParent);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> bmList = retailTradeDailyReportByCategoryParentMapper.retrieveN(params);
			// 遍历得到当月所有大类报表的总额
			double reportByCategoryParentAmount = 0.000000d;
			for (BaseModel bm : bmList) {
				RetailTradeDailyReportByCategoryParent rcp = (RetailTradeDailyReportByCategoryParent) bm;
				reportByCategoryParentAmount = GeneralUtil.sum(reportByCategoryParentAmount, rcp.getTotalAmountSummary());
			}
			// 验证当月大类报表的总额和占比
			for (BaseModel bm : bmList) {
				RetailTradeDailyReportByCategoryParent rcp = (RetailTradeDailyReportByCategoryParent) bm;
				for (int i = 0; i < categoryPanrentIDs.length; i++) {
					if (rcp.getCategoryParentID() == categoryPanrentIDs[i]) {
						assertTrue((rcp.getTotalAmountSummary() - categoryPanrentTotalAmount[i]) < BaseModel.TOLERANCE, "商品大类总额不正确！");
						assertTrue((GeneralUtil.div(rcp.getTotalAmountSummary(), reportByCategoryParentAmount, 4) - categoryPanrentTotalAmountProportion[i]) < 0.0001d, "商品大类总额不正确！");
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	protected void verifyRetailTradeMonthlyDailySummary(double[] avgDailyData) {
		// 销售总笔数，日均销售额，日均销售毛利，日均销售笔数
		if (avgDailyData == null || avgDailyData.length != 4) {
			Assert.assertTrue(false, "avgDailyData数组长度为空或者avgDailyData数组参数缺失!");
		}
		int totalAmount = (int) avgDailyData[0];
		double dailyAmount = avgDailyData[1]; // 日均销售总额（销售总额/2000年3月的天数）
		double dailyGross = avgDailyData[2]; // 日均销售毛利（销售毛利/2000年3月的天数）
		int dailyNO = (int) avgDailyData[3]; // 日均销售总笔数（销售总笔数/2000年3月的天数）
		//
		int days = 0;
		int totalNO = 0;
		double totalAmout = 0.000000d;
		double totalGross = 0.000000d;
		for (RetailTradeDailyReportSummary rtdrs : listRetailTradeDailyReportSummary) {
			days++;
			totalNO += rtdrs.getTotalNO();
			totalAmout = GeneralUtil.sum(totalAmout, rtdrs.getTotalAmount());
			totalGross = GeneralUtil.sum(totalGross, rtdrs.getTotalGross());
		}

		Assert.assertTrue(Math.abs(GeneralUtil.sub(dailyAmount, totalAmout)) < BaseModel.TOLERANCE, "返回的日均销售总额有问题,期望的是：" + dailyAmount);
		Assert.assertTrue(Math.abs(GeneralUtil.sub(dailyGross, totalGross)) < BaseModel.TOLERANCE, "返回的日均销售毛利有问题,期望的是：" + dailyGross);
		Assert.assertTrue(dailyNO == (totalNO / days), "返回的日均销售总笔数有问题,期望的是：" + dailyNO);
		Assert.assertTrue(totalAmount == totalNO, "返回的销售总笔数有问题,期望的是：" + totalAmount);
	}

	protected RetailTradeDailyReportSummary retrieveRetailTradeDailyReport(String dtStart, String dtEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		try {
			Date datetimeStart = sdf.parse(dtStart);
			Date datetimeEnd = sdf.parse(dtEnd);

			RetailTradeDailyReportSummary summary = new RetailTradeDailyReportSummary();
			summary.setDatetimeStart(datetimeStart);
			summary.setDatetimeEnd(datetimeEnd);
			Map<String, Object> params = summary.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, summary);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<List<BaseModel>> list = retailTradeDailyReportSummaryMapper.retrieve1Ex(params);
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(list.size() > 0, sdf2.format(datetimeStart) + "这一天没有日报汇总");
			//
			for (List<BaseModel> list2 : list) {
				for (BaseModel bm : list2) {
					RetailTradeDailyReportSummary s = (RetailTradeDailyReportSummary) bm;
					listRetailTradeDailyReportSummary.add(s);
				}
			}
			return (RetailTradeDailyReportSummary) list.get(0).get(0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected RetailTradeMonthlyReportSummary createRetailTradeMonthlyReport(String endDatetime) {
		try {
			Date dtEnd = sdf.parse(endDatetime);
			RetailTradeMonthlyReportSummary rtdr = new RetailTradeMonthlyReportSummary();
			rtdr.setDatetimeEnd(dtEnd);
			Map<String, Object> params = rtdr.getCreateParam(BaseBO.INVALID_CASE_ID, rtdr);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			BaseModel bm = retailTradeMonthlyReportSummaryMapper.create(params);
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(bm != null, "月报表生成失败！");

			return (RetailTradeMonthlyReportSummary) bm;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void verifyRetailTradeMonthlySummary(RetailTradeMonthlyReportSummary rtmrs, double[] saleData) {
		// 检查月销售总额，月总销售毛利
		if (saleData == null || saleData.length != 2) {
			Assert.assertTrue(false, "saleData数组长度为空或者saleData数组参数缺失!");
		}
		double totalAmount = saleData[0]; // 销售总额
		double totalGross = saleData[1]; // 销售毛利
		Assert.assertTrue(Math.abs(GeneralUtil.sub(totalAmount, rtmrs.getTotalAmount())) < BaseModel.TOLERANCE, //
				"返回的销售总额有问题，期望的是:" + totalAmount);
		Assert.assertTrue(Math.abs(GeneralUtil.sub(totalGross, rtmrs.getTotalGross())) < BaseModel.TOLERANCE, //
				"返回的销售毛利有问题，期望的是:" + totalGross);
	}

	protected void createDailyReport(String dtStart) throws Exception {
		Date datetimeStart = sdf.parse(dtStart);
		createRetailTradeDailyReport(datetimeStart);
		createRetailTradeDailyReportByStaff(datetimeStart);
		createRetailTradeDailyReportByCategoryParent(datetimeStart);
	}

	protected void createRetailTradeDailyReport(Date saleDatetime) {
		RetailTradeDailyReport rtdr = new RetailTradeDailyReport();
		rtdr.setSaleDatetime(saleDatetime);
		rtdr.setShopID(Shared.DEFAULT_Shop_ID); // ...
		rtdr.setDeleteOldData(EnumBoolean.EB_Yes.getIndex());
		Map<String, Object> params = rtdr.getCreateParamEx(BaseBO.INVALID_CASE_ID, rtdr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportMapper.createEx(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() > 0, sdf2.format(saleDatetime) + "这一天没有零售记录，生成不了零售日报表!");
		retailTradeDailyReport = (RetailTradeDailyReport) list.get(0).get(0);
	}

	protected void createRetailTradeDailyReportByStaff(Date saleDatetime) {
		RetailTradeDailyReportByStaff staff = new RetailTradeDailyReportByStaff();
		staff.setSaleDatetime(saleDatetime);
		staff.setShopID(Shared.DEFAULT_Shop_ID);
		String error = staff.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(error), error);
		//
		Map<String, Object> params = staff.getCreateParamEx(BaseBO.INVALID_CASE_ID, staff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportByStaffMapper.createEx(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() > 0, sdf2.format(saleDatetime) + "这一天没有日报表!");
		//
		listRetailTradeDailyReportByStaff = new ArrayList<RetailTradeDailyReportByStaff>();
		for (int i = 0; i < list.size(); i++) {
			RetailTradeDailyReportByStaff s = (RetailTradeDailyReportByStaff) list.get(i);
			listRetailTradeDailyReportByStaff.add(s);
		}
	}

	protected void createRetailTradeDailyReportByCategoryParent(Date saleDatetime) {
		RetailTradeDailyReportByCategoryParent categoryParent = new RetailTradeDailyReportByCategoryParent();
		categoryParent.setSaleDatetime(saleDatetime);
		categoryParent.setShopID(Shared.DEFAULT_Shop_ID);
		String error = categoryParent.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(error), error);
		//
		Map<String, Object> params = categoryParent.getCreateParamEx(BaseBO.INVALID_CASE_ID, categoryParent);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportByCategoryParentMapper.createEx(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() > 0, sdf2.format(saleDatetime) + "这一天没有商品分类占比!");
		//
		listRetailTradeDailyReportByCategoryParent = new ArrayList<RetailTradeDailyReportByCategoryParent>();
		for (int i = 0; i < list.size(); i++) {
			RetailTradeDailyReportByCategoryParent cp = (RetailTradeDailyReportByCategoryParent) list.get(i);
			listRetailTradeDailyReportByCategoryParent.add(cp);
		}
	}

	/** 验证商品零售日报表中前N位商品的销售数据 */
	protected void verifyRetailTradeDailyReportByCommodity_RanKing(String saleCommodity, String startDate, String endDate) {
		String[] comm = saleCommodity.split(";");
		Integer[] commID = GeneralUtil.toIntArray(comm[0]); // 商品ID
		Integer[] totalNO = GeneralUtil.toIntArray(comm[1]); // 销售数量
		int IsASC = Integer.valueOf(comm[2]); // 为0时按降序排序，为1时按升序排序
		int OrderBy = Integer.valueOf(comm[3]); // 根据状态进行排序 0金额，1数量，2毛利
		if (commID == null || totalNO == null) {
			Assert.assertTrue(false, "传入的数组参数为空");
		}
		if (commID.length != totalNO.length) {
			Assert.assertTrue(false, "传入的数组维度不一样");
		}
		//
		List<BaseModel> bmList = retrieveNRetailTradeDailyReportByCommodity(startDate, endDate, "", IsASC, OrderBy, commID.length);
		// 再一一比较
		RetailTradeDailyReportByCommodity reportByCommodity = new RetailTradeDailyReportByCommodity();
		for (int i = 0; i < bmList.size(); i++) {
			reportByCommodity = (RetailTradeDailyReportByCommodity) bmList.get(i);
			Assert.assertTrue(commID[i] == reportByCommodity.getCommodityID(), "返回的商品ID有问题，期望的ID是：" + commID[i]);
			Assert.assertTrue(totalNO[i] == reportByCommodity.getNO(), "返回的销售数量有问题，期望的销售数量是：" + totalNO[i]);
		}
	}

	/** 根据datetimeStart,datetimeEnd,queryKeyword 查询商品零售日报表并按照IsASC,OrderBy进行排序 */
	protected List<BaseModel> retrieveNRetailTradeDailyReportByCommodity(String datetimeStart, String datetimeEnd, String queryKeyword, int IsASC, int OrderBy, int commIDLength) {
		try {
			RetailTradeDailyReportByCommodity reportByCommodity = new RetailTradeDailyReportByCommodity();
			reportByCommodity.setiOrderBy(OrderBy); // 根据状态进行排序 0金额，1数量，2毛利
			reportByCommodity.setIsASC(IsASC); // 为0时降序排序，为1时升序排序
			reportByCommodity.setQueryKeyword(queryKeyword);
			reportByCommodity.setiCategoryID(BaseAction.INVALID_ID);
			reportByCommodity.setDatetimeStart(sdf.parse(datetimeStart));
			reportByCommodity.setDatetimeEnd(sdf.parse(datetimeEnd));
			reportByCommodity.setPageIndex(1);
			if ("".equals(queryKeyword) && commIDLength != 0) {// 当queryKeyword为""时，则是按销售金额或销售数量或销售毛利排序
				reportByCommodity.setPageSize(commIDLength);
			} else {
				reportByCommodity.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			}
			Map<String, Object> params = reportByCommodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, reportByCommodity);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> list = retailTradeDailyReportByCommodityMapper.retrieveN(params);
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(list.size() > 0, datetimeStart + "这一天没有零售商品！");
			//

			return list;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
