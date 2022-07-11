package com.bx.erp.test.task.retailTrade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

public class BaseRetailTradeReportTest extends BaseMapperTest {

	protected SimpleDateFormat sdf;
	protected SimpleDateFormat sdf2;

	private Date datetimeStart = null; // 开始时间
	private Date datetimeEnd = null; // 结束时间
	private int IsASC; // 为0时按降序排序，为1时按升序排序
	private int OrderBy; // 根据状态进行排序 0金额，1数量，2毛利
	private Double[] totalAmount = null; // 销售总额或销售额
	private Double[] totalGross = null; // 销售毛利
	private Integer[] totalNO = null; // 销售总笔数或者销售数量
	protected RetailTradeDailyReport retailTradeDailyReport = null;
	protected List<RetailTradeDailyReportSummary> listRetailTradeDailyReportSummary = null;
	protected List<RetailTradeDailyReportByStaff> listRetailTradeDailyReportByStaff = null;
	protected List<RetailTradeDailyReportByCommodity> listRetailTradeDailyReportByCommodity = null;
	protected List<RetailTradeDailyReportByCategoryParent> listRetailTradeDailyReportByCategoryParent = null;

	@BeforeClass
	public void setup() {

		super.setUp();

		sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
		sdf2 = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	/** 根据saleDatetime 创建零售日报表 */
	protected void createRetailTradeDailyReport(Date saleDatetime) {
		RetailTradeDailyReport rtdr = new RetailTradeDailyReport();
		rtdr.setSaleDatetime(saleDatetime);
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

	/** 根据saleDatetime 创建Staff零售日报表 */
	protected void createRetailTradeDailyReportByStaff(Date saleDatetime) {
		RetailTradeDailyReportByStaff staff = new RetailTradeDailyReportByStaff();
		staff.setSaleDatetime(saleDatetime);
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

	/** 根据saleDatetime 创建销售商品大类日报表 */
	protected void createRetailTradeDailyReportByCategoryParent(Date saleDatetime) {
		RetailTradeDailyReportByCategoryParent categoryParent = new RetailTradeDailyReportByCategoryParent();
		categoryParent.setSaleDatetime(saleDatetime);
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

	/** 根据datetimeStart,datetimeEnd 查询销售日报汇总表 */
	protected void retrieve1RetailTradeDailyReportSummary(Date datetimeStart, Date datetimeEnd) {
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
		listRetailTradeDailyReportSummary = new ArrayList<RetailTradeDailyReportSummary>();
		for (List<BaseModel> list2 : list) {
			for (BaseModel bm : list2) {
				RetailTradeDailyReportSummary s = (RetailTradeDailyReportSummary) bm;
				listRetailTradeDailyReportSummary.add(s);
			}
		}
	}

	/** 根据datetimeStart,datetimeEnd,queryKeyword 查询商品零售日报表并按照IsASC,OrderBy进行排序 */
	protected void retrieveNRetailTradeDailyReportByCommodity(Date datetimeStart, Date datetimeEnd, String queryKeyword, int IsASC, int OrderBy, int commIDLength) {
		RetailTradeDailyReportByCommodity commodity = new RetailTradeDailyReportByCommodity();
		commodity.setiOrderBy(OrderBy); // 根据状态进行排序 0金额，1数量，2毛利
		commodity.setIsASC(IsASC); // 为0时降序排序，为1时升序排序
		commodity.setQueryKeyword(queryKeyword);
		commodity.setiCategoryID(BaseAction.INVALID_ID);
		commodity.setDatetimeStart(datetimeStart);
		commodity.setDatetimeEnd(datetimeEnd);
		commodity.setPageIndex(1);
		commodity.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		Map<String, Object> params = commodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = retailTradeDailyReportByCommodityMapper.retrieveN(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() > 0, sdf2.format(datetimeStart) + "这一天没有零售商品！");
		//
		listRetailTradeDailyReportByCommodity = new ArrayList<RetailTradeDailyReportByCommodity>();
		if ("".equals(queryKeyword) && commIDLength != 0) {// 当queryKeyword为""时，则是按销售金额或销售数量或销售毛利排序
			for (int i = 0; i < commIDLength; i++) {
				RetailTradeDailyReportByCommodity comm = (RetailTradeDailyReportByCommodity) list.get(i);
				listRetailTradeDailyReportByCommodity.add(comm);
			}
		} else {// 当queryKeyword不为""时，则是根据queryKeyword模糊查询
			for (BaseModel bm : list) {
				RetailTradeDailyReportByCommodity comm = (RetailTradeDailyReportByCommodity) bm;
				listRetailTradeDailyReportByCommodity.add(comm);
			}
		}
	}

	protected void createDailyReport(String dtStart, String dtEnd) throws Exception {
		datetimeStart = sdf.parse(dtStart);
		datetimeEnd = sdf.parse(dtEnd);
		createRetailTradeDailyReport(datetimeStart);
		createRetailTradeDailyReportByStaff(datetimeStart);
		createRetailTradeDailyReportByCategoryParent(datetimeStart);
	}

	/** 验证销售日报汇总表 */
	protected void verifyRetailTradeDailyReportSummary(double[] saleData) {
		retrieve1RetailTradeDailyReportSummary(datetimeStart, datetimeEnd);
		// 检查销售总额，销售毛利，销售总笔数，日均销售总额，日均销售毛利，日均销售总笔数
		if (saleData == null || saleData.length != 6) {
			Assert.assertTrue(false, "saleData数组长度为空或者saleData数组参数缺失!");
		}
		double totalAmount = saleData[0]; // 销售总额
		double totalGross = saleData[1]; // 销售毛利
		int totalNO = (int) saleData[2]; // 销售总笔数
		double dailyAmount = saleData[3]; // 日均销售总额（销售总额/2000年4月的天数）
		double dailyGross = saleData[4]; // 日均销售毛利（销售毛利/2000年4月的天数）
		double dailyNO = saleData[5]; // 日均销售总笔数（销售总笔数/2000年4月的天数）
		Assert.assertTrue(Math.abs(GeneralUtil.sub(totalAmount, listRetailTradeDailyReportSummary.get(0).getTotalAmount())) < BaseModel.TOLERANCE, //
				"返回的销售总额有问题，期望的是:" + totalAmount);
		Assert.assertTrue(Math.abs(GeneralUtil.sub(totalGross, listRetailTradeDailyReportSummary.get(0).getTotalGross())) < BaseModel.TOLERANCE, //
				"返回的销售毛利有问题，期望的是:" + totalGross);
		Assert.assertTrue(totalNO == listRetailTradeDailyReportSummary.get(0).getTotalNO(), "返回的销售总笔数有问题，期望的是:" + totalNO);
		// 获取当前月份有多少天
		Calendar c = Calendar.getInstance();
		c.set(Integer.valueOf(sdf.format(datetimeStart).substring(0, 4)), Integer.valueOf(sdf.format(datetimeStart).substring(5, 7)), 0); // yyyy-MM-dd HH:mm:ss
		int Day = c.get(Calendar.DAY_OF_MONTH);
		double amount = GeneralUtil.div(listRetailTradeDailyReportSummary.get(0).getTotalAmount(), Day, 2);
		double gross = GeneralUtil.div(listRetailTradeDailyReportSummary.get(0).getTotalGross(), Day, 2);
		double NO = GeneralUtil.div(listRetailTradeDailyReportSummary.get(0).getTotalNO(), Day, 2);
		Assert.assertTrue(Math.abs(GeneralUtil.sub(dailyAmount, amount)) < BaseModel.TOLERANCE, "返回的日均销售总额有问题,期望的是：" + dailyAmount);
		Assert.assertTrue(Math.abs(GeneralUtil.sub(dailyGross, gross)) < BaseModel.TOLERANCE, "返回的日均销售毛利有问题,期望的是：" + dailyGross);
		Assert.assertTrue(Math.abs(GeneralUtil.sub(dailyNO, NO)) < BaseModel.TOLERANCE, "返回的日均销售总笔数有问题,期望的是：" + dailyNO);
	}

	/** 验证销售商品大类日报表 */
	protected void verifyRetailTradeDailyReportByCategoryParent(String categoryParent) {
		String[] category = categoryParent.split(";");
		Integer[] categoryPanrentID = GeneralUtil.toIntArray(category[0]); // 商品大类ID
		totalAmount = GeneralUtil.toDoubleArray(category[1]); // 商品大类的销售额
		if (categoryPanrentID == null || totalAmount == null) {
			Assert.assertTrue(false, "传入的数组参数为空");
		}
		if (categoryPanrentID.length != totalAmount.length) {
			Assert.assertTrue(false, "传入的数组维度不一样");
		}
		for (int i = 0; i < categoryPanrentID.length; i++) {
			Assert.assertTrue(categoryPanrentID[i] == listRetailTradeDailyReportByCategoryParent.get(i).getCategoryParentID(), "返回的商品大类ID有问题，期望的ID是:" + categoryPanrentID[i]);
			Assert.assertTrue(Math.abs(GeneralUtil.sub(totalAmount[i], listRetailTradeDailyReportByCategoryParent.get(i).getTotalAmount())) < BaseModel.TOLERANCE, "返回的销售额有问题，期望的是:" + totalAmount[i]);
		}
	}

	/** 验证Staff零售日报表 */
	protected void verifyRetailTradeDailyReportByStaff(String saleStaff) {
		String[] staff = saleStaff.split(";");
		Integer[] staffID = GeneralUtil.toIntArray(staff[0]); // 收银员ID
		totalAmount = GeneralUtil.toDoubleArray(staff[1]); // 销售总额
		totalGross = GeneralUtil.toDoubleArray(staff[2]); // 销售毛利
		totalNO = GeneralUtil.toIntArray(staff[3]); // 销售单数
		if (staffID == null || totalAmount == null || totalGross == null || totalNO == null) {
			Assert.assertTrue(false, "传入的数组参数为空");
		}
		if (staffID.length != totalAmount.length || staffID.length != totalGross.length || staffID.length != totalNO.length) {
			Assert.assertTrue(false, "传入的数组维度不一样");
		}
		for (int i = 0; i < staffID.length; i++) {
			Assert.assertTrue(staffID[i] == listRetailTradeDailyReportByStaff.get(i).getStaffID(), "返回的员工ID有问题,期望的ID是:" + staffID[i]);
			Assert.assertTrue(Math.abs(GeneralUtil.sub(totalAmount[i], listRetailTradeDailyReportByStaff.get(i).getTotalAmount())) < BaseModel.TOLERANCE, "返回的销售总额有问题，期望的是:" + totalAmount[i]);
			Assert.assertTrue(Math.abs(GeneralUtil.sub(totalGross[i], listRetailTradeDailyReportByStaff.get(i).getGrossMargin())) < BaseModel.TOLERANCE, "返回的销售毛利有问题，期望的是:" + totalGross[i]);
			Assert.assertTrue(totalNO[i] == listRetailTradeDailyReportByStaff.get(i).getNO(), "返回的销售总笔数有问题,期望的是:" + totalNO[i]);
		}
	}

	/** 验证商品零售日报表中前N位商品的销售数据 */
	protected void verifyRetailTradeDailyReportByCommodity_RanKing(String saleCommodity) {
		String[] comm = saleCommodity.split(";");
		Integer[] commID = GeneralUtil.toIntArray(comm[0]); // 商品ID
		totalNO = GeneralUtil.toIntArray(comm[1]); // 销售数量
		IsASC = Integer.valueOf(comm[2]); // 为0时按降序排序，为1时按升序排序
		OrderBy = Integer.valueOf(comm[3]); // 根据状态进行排序 0金额，1数量，2毛利
		if (commID == null || totalNO == null) {
			Assert.assertTrue(false, "传入的数组参数为空");
		}
		if (commID.length != totalNO.length) {
			Assert.assertTrue(false, "传入的数组维度不一样");
		}
		// NO相同时，ID排序并不是固定的，会导致比较时彼此的ID不相等，因而需要处理这种特殊情况
		List<RetailTradeDailyReportByCommodity> listRetailTradeDailyReportByCommodityExpected = new ArrayList<RetailTradeDailyReportByCommodity>();
		for (int i = 0; i < commID.length; i++) {
			RetailTradeDailyReportByCommodity tmp = new RetailTradeDailyReportByCommodity();
			tmp.setNO(totalNO[i]);
			tmp.setCommodityID(commID[i]);
			listRetailTradeDailyReportByCommodityExpected.add(tmp);
		}
		//
		retrieveNRetailTradeDailyReportByCommodity(datetimeStart, datetimeEnd, "", IsASC, OrderBy, commID.length);
		// 先按照商品ID排序
		RetailTradeDailyReportByCommodity tmp = new RetailTradeDailyReportByCommodity();
		tmp.setIsASC(EnumBoolean.EB_NO.getIndex());
		Collections.sort(listRetailTradeDailyReportByCommodityExpected, tmp);

		// 先按照商品ID排序
		RetailTradeDailyReportByCommodity tmp2 = new RetailTradeDailyReportByCommodity();
		tmp2.setIsASC(EnumBoolean.EB_NO.getIndex());
		Collections.sort(listRetailTradeDailyReportByCommodity, tmp2);

		// 再一一比较
		for (int i = 0; i < commID.length; i++) {
			Assert.assertTrue(listRetailTradeDailyReportByCommodityExpected.get(i).getCommodityID() == listRetailTradeDailyReportByCommodity.get(i).getCommodityID(), //
					"返回的商品ID有问题，期望的ID是：" + listRetailTradeDailyReportByCommodityExpected.get(i).getCommodityID());
			Assert.assertTrue(listRetailTradeDailyReportByCommodityExpected.get(i).getNO() == listRetailTradeDailyReportByCommodity.get(i).getNO(), //
					"返回的销售数量有问题，期望的销售数量是：" + listRetailTradeDailyReportByCommodityExpected.get(i).getNO());
		}
	}

	/** 验证根据queryKeyword模糊查询商品零售日报表后的数据 */
	protected void verifyRetailTradeDailyReportByCommodityRetrieve(String queryKeyword) {
		String[] s = queryKeyword.split(";");
		String[] QueryKeyword = s[0].split(","); // 模糊搜索条件
		totalNO = GeneralUtil.toIntArray(s[1]); // 销售数量
		totalAmount = GeneralUtil.toDoubleArray(s[2]); // 销售额
		totalGross = GeneralUtil.toDoubleArray(s[3]); // 销售毛利
		IsASC = Integer.valueOf(s[4]); // 为0时按降序排序，为1时按升序排序
		OrderBy = Integer.valueOf(s[5]); // 根据状态进行排序 0金额，1数量，2毛利
		if (QueryKeyword == null || totalNO == null || totalAmount == null || totalGross == null) {
			Assert.assertTrue(false, "传入的数组参数为空");
		}
		if (QueryKeyword.length != totalAmount.length || QueryKeyword.length != totalNO.length || QueryKeyword.length != totalGross.length) {
			Assert.assertTrue(false, "传入的数组维度不一样");
		}
		int TotalNO = 0;
		double TotalAmount = 0D;
		for (int i = 0; i < QueryKeyword.length; i++) {
			retrieveNRetailTradeDailyReportByCommodity(datetimeStart, datetimeEnd, QueryKeyword[i], IsASC, OrderBy, 0);
			TotalNO = 0;
			TotalAmount = 0D;
			for (int j = 0; j < listRetailTradeDailyReportByCommodity.size(); j++) {
				TotalNO += listRetailTradeDailyReportByCommodity.get(j).getNO();
				TotalAmount = GeneralUtil.sum(TotalAmount, listRetailTradeDailyReportByCommodity.get(j).getTotalAmount());
				Assert.assertTrue(listRetailTradeDailyReportByCommodity.get(j).getName().contains(QueryKeyword[i]), "返回的商品名称有问题，期望的是商品名称包含有：" + QueryKeyword[i] + "字");
				if (listRetailTradeDailyReportByCommodity.size() == 1 && QueryKeyword[i].equals(listRetailTradeDailyReportByCommodity.get(0).getName())) {
					Assert.assertTrue(Math.abs(GeneralUtil.sub(totalGross[i], listRetailTradeDailyReportByCommodity.get(j).getGrossMargin())) < BaseModel.TOLERANCE, "返回的销售毛利有问题，期望的是:" + totalGross[i]);
				}
			}
			Assert.assertTrue(totalNO[i] == TotalNO, "返回的销售数量有问题，期望的销售数量是:" + totalNO[i]);
			Assert.assertTrue(Math.abs(GeneralUtil.sub(totalAmount[i], TotalAmount)) < BaseModel.TOLERANCE, "返回的销售总额有问题，期望的是:" + totalAmount[i]);
		}
	}

}
