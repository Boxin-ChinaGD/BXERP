package com.bx.erp.test.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.report.RetailTradeDailyReportByStaff;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

public class RetailTradeDailyReportByStaffMapperTest extends BaseMapperTest {

	private SimpleDateFormat sdf;

	@BeforeClass
	public void setup() {
		super.setUp();

		sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	public static class DataInput {
		private static RetailTradeAggregation rtaInput = null;

		protected static final RetailTradeAggregation getRetailTradeAggregation(Date workTimeStart, Date workTimeEnd) {
			Random ran = new Random();
			rtaInput = new RetailTradeAggregation();
			rtaInput.setStaffID(3);
			rtaInput.setPosID(ran.nextInt(5) + 1);
			rtaInput.setWorkTimeStart(workTimeStart);
			rtaInput.setWorkTimeEnd(workTimeEnd);
			rtaInput.setTradeNO(ran.nextInt(500));
			rtaInput.setCashAmount(ran.nextInt(2000));
			rtaInput.setWechatAmount(ran.nextInt(2000));
			rtaInput.setAlipayAmount(ran.nextInt(2000));
			rtaInput.setAmount(rtaInput.getCashAmount() + rtaInput.getWechatAmount() + rtaInput.getAlipayAmount());
			rtaInput.setReserveAmount(500);
			try {
				return (RetailTradeAggregation) rtaInput.clone();
			} catch (CloneNotSupportedException e) {
				Assert.assertTrue(false, "RetailTradeAggregation clone失败");
				return null;
			}
		}

	}

	protected List<List<BaseModel>> createExRetailTradeDailyReportByStaff(RetailTradeDailyReportByStaff retailTradeDailyReportByStaff) {
		String err = retailTradeDailyReportByStaff.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = retailTradeDailyReportByStaff.getCreateParamEx(BaseBO.INVALID_CASE_ID, retailTradeDailyReportByStaff);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportByStaffMapper.createEx(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		if (list != null) { // ... 有些特殊的case 可以测试报表不能生成数据，如生成员工业绩报表时，收银汇总没有该员工今天的上班记录
			// ... 假如一天多个员工上班
			for (int i = 0; i < list.size(); i++) {
				RetailTradeDailyReportByStaff reportByStaff = (RetailTradeDailyReportByStaff) list.get(i);
				Assert.assertTrue(DatetimeUtil.compareDate(retailTradeDailyReportByStaff.getSaleDatetime(), reportByStaff.getDateTime()), "创建的对象的字段与DB读出的不相等");
			}
		}
		System.out.println("创建对象成功： " + list);
		return list;
	}

	private List<BaseModel> retrieveNRetailTradeDailyReportByStaff(RetailTradeDailyReportByStaff retailTradeDailyReportByStaff) {
		Map<String, Object> params = retailTradeDailyReportByStaff.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTradeDailyReportByStaff);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = retailTradeDailyReportByStaffMapper.retrieveN(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		if (list != null) {
			for (BaseModel bm : list) {
				RetailTradeDailyReportByStaff reportByStaff = (RetailTradeDailyReportByStaff) bm;
				Assert.assertTrue(DatetimeUtil.compareDate(retailTradeDailyReportByStaff.getSaleDatetime(), reportByStaff.getDateTime()), "查询的对象的字段与DB读出的不相等");
			}
		}
		System.out.println("创建对象成功： " + list);
		return list;
	}

	private void createRetailTradeAggregation(Date workTimeStart, Date workTimeEnd) {
		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation(workTimeStart, workTimeEnd);
		String err = rta.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = rta.getCreateParam(BaseBO.INVALID_CASE_ID, rta);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) retailTradeAggregationMapper.create(params); // ...
		//
		Assert.assertTrue(retailTradeAggregation != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		rta.setIgnoreIDInComparision(true);
		if (rta.compareTo(retailTradeAggregation) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
	}

	@Test
	public void createExTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 零售单有数据，进行统计创建");
		createRetailTradeAggregation(sdf.parse("2019-8-12 06:00:00"), sdf.parse("2019-8-12 21:00:00"));

		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		retailTradeDailyReportByStaff.setSaleDatetime(sdf.parse("2019-8-12 00:00:00"));
		retailTradeDailyReportByStaff.setDeleteOldData(EnumBoolean.EB_Yes.getIndex());
		retailTradeDailyReportByStaff.setShopID(Shared.DEFAULT_Shop_ID);
		List<List<BaseModel>> createExRetailTradeDailyReportByStaff = createExRetailTradeDailyReportByStaff(retailTradeDailyReportByStaff);
		Assert.assertTrue(createExRetailTradeDailyReportByStaff != null && createExRetailTradeDailyReportByStaff.size() > 0, "零售单有数据，进行统计创建失败！");
	}

	@Test
	public void createExTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: 零售单没有数据，进行统计创建");

		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		retailTradeDailyReportByStaff.setSaleDatetime(sdf.parse("2099-1-15 00:00:00"));
		retailTradeDailyReportByStaff.setDeleteOldData(EnumBoolean.EB_Yes.getIndex());
		retailTradeDailyReportByStaff.setShopID(Shared.DEFAULT_Shop_ID);
		List<List<BaseModel>> createExRetailTradeDailyReportByStaff = createExRetailTradeDailyReportByStaff(retailTradeDailyReportByStaff);
		Assert.assertTrue(createExRetailTradeDailyReportByStaff != null && createExRetailTradeDailyReportByStaff.size() == 0, "无数据进行统计创建成功！");
	}

	@Test
	public void createExTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3: 零售单商品未入库，进行统计创建，进货价为0，毛利等于销售金额");
		createRetailTradeAggregation(sdf.parse("2019-5-19 06:00:00"), sdf.parse("2019-5-19 21:00:00"));

		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		retailTradeDailyReportByStaff.setSaleDatetime(sdf.parse("2019-5-19 00:00:00"));
		retailTradeDailyReportByStaff.setDeleteOldData(EnumBoolean.EB_Yes.getIndex());
		retailTradeDailyReportByStaff.setShopID(Shared.DEFAULT_Shop_ID);
		List<List<BaseModel>> list = createExRetailTradeDailyReportByStaff(retailTradeDailyReportByStaff);
		for (int i = 0; i < list.size(); i++) {
			RetailTradeDailyReportByStaff reportByStaff = (RetailTradeDailyReportByStaff) list.get(i);
			Assert.assertTrue(Math.abs(reportByStaff.getTotalAmount() - reportByStaff.getGrossMargin()) < BaseModel.TOLERANCE);
		}
	}

	@Test
	public void createExTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4: deleteOldData=0时，不删除旧数据");
		createRetailTradeAggregation(sdf.parse("2019-8-10 06:00:00"), sdf.parse("2019-8-10 21:00:00"));

		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		retailTradeDailyReportByStaff.setSaleDatetime(sdf.parse("2019-8-10 00:00:00"));
		retailTradeDailyReportByStaff.setShopID(Shared.DEFAULT_Shop_ID);
		createExRetailTradeDailyReportByStaff(retailTradeDailyReportByStaff);
		// 重复进行生成员工业绩报表
		Map<String, Object> params = retailTradeDailyReportByStaff.getCreateParamEx(BaseBO.INVALID_CASE_ID, retailTradeDailyReportByStaff);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeDailyReportByStaffMapper.createEx(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_OtherError, //
				"不删除旧数据，重复生成同一天的报表成功！");
	}

	@Test
	public void createExTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5: deleteOldData=1时，删除旧数据");
		createRetailTradeAggregation(sdf.parse("2019-8-2 06:00:00"), sdf.parse("2019-8-2 21:00:00"));

		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		retailTradeDailyReportByStaff.setSaleDatetime(sdf.parse("2019-8-2 00:00:00"));
		retailTradeDailyReportByStaff.setDeleteOldData(EnumBoolean.EB_Yes.getIndex());
		retailTradeDailyReportByStaff.setShopID(Shared.DEFAULT_Shop_ID);
		createExRetailTradeDailyReportByStaff(retailTradeDailyReportByStaff);
		// 重复进行生成员工业绩报表
		createExRetailTradeDailyReportByStaff(retailTradeDailyReportByStaff);
	}

	@Test
	public void retrieveNTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:查询一天的员工数据 ");

		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		retailTradeDailyReportByStaff.setDatetimeStart(sdf.parse("2019-1-10 00:00:00"));
		retailTradeDailyReportByStaff.setDatetimeEnd(sdf.parse("2019-1-10 00:00:00"));
		retailTradeDailyReportByStaff.setShopID(Shared.DEFAULT_Shop_ID);
		//
		retrieveNRetailTradeDailyReportByStaff(retailTradeDailyReportByStaff);
	}

	@Test
	public void retrieveNTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:查询存在的时间段的员工数据");

		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		retailTradeDailyReportByStaff.setDatetimeStart(sdf.parse("2019-1-10 00:00:00"));
		retailTradeDailyReportByStaff.setDatetimeEnd(sdf.parse("2019-1-15 00:00:00"));
		retailTradeDailyReportByStaff.setShopID(Shared.DEFAULT_Shop_ID);
		//
		retrieveNRetailTradeDailyReportByStaff(retailTradeDailyReportByStaff);
	}

	@Test
	public void retrieveNTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println(" Case3:查询不存在的时间段");
		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		retailTradeDailyReportByStaff.setDatetimeStart(sdf.parse("2099-1-10 00:00:00"));
		retailTradeDailyReportByStaff.setDatetimeEnd(sdf.parse("2099-1-10 00:00:00"));
		retailTradeDailyReportByStaff.setShopID(Shared.DEFAULT_Shop_ID);
		//
		retrieveNRetailTradeDailyReportByStaff(retailTradeDailyReportByStaff);
	}
	
	@Test
	public void retrieveNTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println(" Case4:查询不存在的门店IDs");
		RetailTradeDailyReportByStaff retailTradeDailyReportByStaff = new RetailTradeDailyReportByStaff();
		retailTradeDailyReportByStaff.setDatetimeStart(sdf.parse("2099-1-10 00:00:00"));
		retailTradeDailyReportByStaff.setDatetimeEnd(sdf.parse("2099-1-10 00:00:00"));
		retailTradeDailyReportByStaff.setShopID(BaseAction.INVALID_ID);
		//
		retrieveNRetailTradeDailyReportByStaff(retailTradeDailyReportByStaff);
	}
}
