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
			Integer[] staffIDs = GeneralUtil.toIntArray(staffData[0]); // ??????ID
			Double[] staffTotalAmount = GeneralUtil.toDoubleArray(staffData[1]); // ?????????????????????
			Double[] staffTotalGross = GeneralUtil.toDoubleArray(staffData[2]); // ????????????????????????
			Integer[] staffTotalNO = GeneralUtil.toIntArray(staffData[3]); // ?????????????????????
			if (staffIDs == null || staffTotalAmount == null || staffTotalGross == null || staffTotalNO == null) {
				Assert.assertTrue(false, "???????????????????????????");
			}
			if (staffIDs.length != staffTotalAmount.length || staffIDs.length != staffTotalGross.length || staffIDs.length != staffTotalNO.length) {
				Assert.assertTrue(false, "??????????????????????????????");
			}
			// ??????????????????????????????????????????
			RetailTradeDailyReportByStaff reportByStaff = new RetailTradeDailyReportByStaff();
			reportByStaff.setDatetimeStart(sdf.parse(dtStart));
			reportByStaff.setDatetimeEnd(sdf.parse(dtEnd));
			//
			Map<String, Object> params = reportByStaff.getRetrieveNParam(BaseBO.INVALID_CASE_ID, reportByStaff);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> bmList = retailTradeDailyReportByStaffMapper.retrieveN(params);
			// ??????????????????????????????????????????
			for (BaseModel bm : bmList) {
				RetailTradeDailyReportByStaff rs = (RetailTradeDailyReportByStaff) bm;
				for (int i = 0; i < staffIDs.length; i++) {
					if (rs.getStaffID() == staffIDs[i]) {
						assertTrue((rs.getTotalAmount() - staffTotalAmount[i]) < BaseModel.TOLERANCE, "?????????????????????????????????");
						assertTrue((rs.getGrossMargin() - staffTotalGross[i]) < BaseModel.TOLERANCE, "????????????????????????????????????");
						assertTrue((rs.getNO() - staffTotalNO[i]) < BaseModel.TOLERANCE, "?????????????????????????????????");
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
			Integer[] categoryPanrentIDs = GeneralUtil.toIntArray(category[0]); // ????????????ID
			Double[] categoryPanrentTotalAmount = GeneralUtil.toDoubleArray(category[1]); // ????????????????????????
			Double[] categoryPanrentTotalAmountProportion = GeneralUtil.toDoubleArray(category[2]); // ???????????????????????????
			if (categoryPanrentIDs == null || categoryPanrentTotalAmount == null || categoryPanrentTotalAmountProportion == null) {
				Assert.assertTrue(false, "???????????????????????????");
			}
			if (categoryPanrentIDs.length != categoryPanrentTotalAmount.length || categoryPanrentIDs.length != categoryPanrentTotalAmountProportion.length) {
				Assert.assertTrue(false, "??????????????????????????????");
			}
			// ????????????????????????????????????
			RetailTradeDailyReportByCategoryParent reportByCategoryParent = new RetailTradeDailyReportByCategoryParent();
			reportByCategoryParent.setDatetimeStart(sdf.parse(dtStart));
			reportByCategoryParent.setDatetimeEnd(sdf.parse(dtEnd));
			//
			Map<String, Object> params = reportByCategoryParent.getRetrieveNParam(BaseBO.INVALID_CASE_ID, reportByCategoryParent);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> bmList = retailTradeDailyReportByCategoryParentMapper.retrieveN(params);
			// ?????????????????????????????????????????????
			double reportByCategoryParentAmount = 0.000000d;
			for (BaseModel bm : bmList) {
				RetailTradeDailyReportByCategoryParent rcp = (RetailTradeDailyReportByCategoryParent) bm;
				reportByCategoryParentAmount = GeneralUtil.sum(reportByCategoryParentAmount, rcp.getTotalAmountSummary());
			}
			// ??????????????????????????????????????????
			for (BaseModel bm : bmList) {
				RetailTradeDailyReportByCategoryParent rcp = (RetailTradeDailyReportByCategoryParent) bm;
				for (int i = 0; i < categoryPanrentIDs.length; i++) {
					if (rcp.getCategoryParentID() == categoryPanrentIDs[i]) {
						assertTrue((rcp.getTotalAmountSummary() - categoryPanrentTotalAmount[i]) < BaseModel.TOLERANCE, "??????????????????????????????");
						assertTrue((GeneralUtil.div(rcp.getTotalAmountSummary(), reportByCategoryParentAmount, 4) - categoryPanrentTotalAmountProportion[i]) < 0.0001d, "??????????????????????????????");
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	protected void verifyRetailTradeMonthlyDailySummary(double[] avgDailyData) {
		// ???????????????????????????????????????????????????????????????????????????
		if (avgDailyData == null || avgDailyData.length != 4) {
			Assert.assertTrue(false, "avgDailyData????????????????????????avgDailyData??????????????????!");
		}
		int totalAmount = (int) avgDailyData[0];
		double dailyAmount = avgDailyData[1]; // ?????????????????????????????????/2000???3???????????????
		double dailyGross = avgDailyData[2]; // ?????????????????????????????????/2000???3???????????????
		int dailyNO = (int) avgDailyData[3]; // ???????????????????????????????????????/2000???3???????????????
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

		Assert.assertTrue(Math.abs(GeneralUtil.sub(dailyAmount, totalAmout)) < BaseModel.TOLERANCE, "????????????????????????????????????,???????????????" + dailyAmount);
		Assert.assertTrue(Math.abs(GeneralUtil.sub(dailyGross, totalGross)) < BaseModel.TOLERANCE, "????????????????????????????????????,???????????????" + dailyGross);
		Assert.assertTrue(dailyNO == (totalNO / days), "???????????????????????????????????????,???????????????" + dailyNO);
		Assert.assertTrue(totalAmount == totalNO, "?????????????????????????????????,???????????????" + totalAmount);
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
			Assert.assertTrue(list.size() > 0, sdf2.format(datetimeStart) + "???????????????????????????");
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
			Assert.assertTrue(bm != null, "????????????????????????");

			return (RetailTradeMonthlyReportSummary) bm;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void verifyRetailTradeMonthlySummary(RetailTradeMonthlyReportSummary rtmrs, double[] saleData) {
		// ??????????????????????????????????????????
		if (saleData == null || saleData.length != 2) {
			Assert.assertTrue(false, "saleData????????????????????????saleData??????????????????!");
		}
		double totalAmount = saleData[0]; // ????????????
		double totalGross = saleData[1]; // ????????????
		Assert.assertTrue(Math.abs(GeneralUtil.sub(totalAmount, rtmrs.getTotalAmount())) < BaseModel.TOLERANCE, //
				"?????????????????????????????????????????????:" + totalAmount);
		Assert.assertTrue(Math.abs(GeneralUtil.sub(totalGross, rtmrs.getTotalGross())) < BaseModel.TOLERANCE, //
				"?????????????????????????????????????????????:" + totalGross);
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
		Assert.assertTrue(list.size() > 0, sdf2.format(saleDatetime) + "?????????????????????????????????????????????????????????!");
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
		Assert.assertTrue(list.size() > 0, sdf2.format(saleDatetime) + "????????????????????????!");
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
		Assert.assertTrue(list.size() > 0, sdf2.format(saleDatetime) + "?????????????????????????????????!");
		//
		listRetailTradeDailyReportByCategoryParent = new ArrayList<RetailTradeDailyReportByCategoryParent>();
		for (int i = 0; i < list.size(); i++) {
			RetailTradeDailyReportByCategoryParent cp = (RetailTradeDailyReportByCategoryParent) list.get(i);
			listRetailTradeDailyReportByCategoryParent.add(cp);
		}
	}

	/** ?????????????????????????????????N???????????????????????? */
	protected void verifyRetailTradeDailyReportByCommodity_RanKing(String saleCommodity, String startDate, String endDate) {
		String[] comm = saleCommodity.split(";");
		Integer[] commID = GeneralUtil.toIntArray(comm[0]); // ??????ID
		Integer[] totalNO = GeneralUtil.toIntArray(comm[1]); // ????????????
		int IsASC = Integer.valueOf(comm[2]); // ???0????????????????????????1??????????????????
		int OrderBy = Integer.valueOf(comm[3]); // ???????????????????????? 0?????????1?????????2??????
		if (commID == null || totalNO == null) {
			Assert.assertTrue(false, "???????????????????????????");
		}
		if (commID.length != totalNO.length) {
			Assert.assertTrue(false, "??????????????????????????????");
		}
		//
		List<BaseModel> bmList = retrieveNRetailTradeDailyReportByCommodity(startDate, endDate, "", IsASC, OrderBy, commID.length);
		// ???????????????
		RetailTradeDailyReportByCommodity reportByCommodity = new RetailTradeDailyReportByCommodity();
		for (int i = 0; i < bmList.size(); i++) {
			reportByCommodity = (RetailTradeDailyReportByCommodity) bmList.get(i);
			Assert.assertTrue(commID[i] == reportByCommodity.getCommodityID(), "???????????????ID?????????????????????ID??????" + commID[i]);
			Assert.assertTrue(totalNO[i] == reportByCommodity.getNO(), "????????????????????????????????????????????????????????????" + totalNO[i]);
		}
	}

	/** ??????datetimeStart,datetimeEnd,queryKeyword ????????????????????????????????????IsASC,OrderBy???????????? */
	protected List<BaseModel> retrieveNRetailTradeDailyReportByCommodity(String datetimeStart, String datetimeEnd, String queryKeyword, int IsASC, int OrderBy, int commIDLength) {
		try {
			RetailTradeDailyReportByCommodity reportByCommodity = new RetailTradeDailyReportByCommodity();
			reportByCommodity.setiOrderBy(OrderBy); // ???????????????????????? 0?????????1?????????2??????
			reportByCommodity.setIsASC(IsASC); // ???0?????????????????????1???????????????
			reportByCommodity.setQueryKeyword(queryKeyword);
			reportByCommodity.setiCategoryID(BaseAction.INVALID_ID);
			reportByCommodity.setDatetimeStart(sdf.parse(datetimeStart));
			reportByCommodity.setDatetimeEnd(sdf.parse(datetimeEnd));
			reportByCommodity.setPageIndex(1);
			if ("".equals(queryKeyword) && commIDLength != 0) {// ???queryKeyword???""???????????????????????????????????????????????????????????????
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
			Assert.assertTrue(list.size() > 0, datetimeStart + "??????????????????????????????");
			//

			return list;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
