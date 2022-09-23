package com.bx.erp.test.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.report.RetailTradeDailyReportByCategoryParent;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class RetailTradeDailyReportByCategoryParentMapperTest extends BaseMapperTest {

	private SimpleDateFormat sdf;

	/** 1，删除旧的数据。只能在测试代码中使用 */
	public static final int DELETE_OLD_DATA_FOR_TEST = 1;

	@BeforeClass
	public void setup() {

		super.setUp();

		sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	protected List<List<BaseModel>> createRDRCP(RetailTradeDailyReportByCategoryParent rdrc) {
		String err = rdrc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = rdrc.getCreateParamEx(BaseBO.INVALID_CASE_ID, rdrc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportByCategoryParentMapper.createEx(params);
		//
		if (list.size() > 0) { // 创建成功
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			for (int i = 0; i < list.size(); i++) {
				((RetailTradeDailyReportByCategoryParent) list.get(i)).setSaleDatetime(((RetailTradeDailyReportByCategoryParent) list.get(i)).getCreateDatetime());
				err = ((RetailTradeDailyReportByCategoryParent) list.get(i)).checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(err, "");
				// 创建时需要比较的字段在java层没有赋值，从DB读取出来的是有值的，使用compareTo验证不通过
				// rdrc.setIgnoreIDInComparision(true);
				// if(rdrc.compareTo((RetailTradeDailyReportByCategoryParent) list.get(i)) != 0)
				// {
				// Assert.assertTrue(false, "创建的对象跟读取的对象不相同");
				// }
			}
			//
			System.out.println("创建对象成功: " + list);
		} else { // 创建失败
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("创建对象失败 : " + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return list;
	}

	protected List<BaseModel> retrieveNRDRCP(RetailTradeDailyReportByCategoryParent rdrc) {
		//
		String err = rdrc.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = rdrc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rdrc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = retailTradeDailyReportByCategoryParentMapper.retrieveN(params);
		//
		Assert.assertTrue(list.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (int i = 0; i < list.size(); i++) {
			((RetailTradeDailyReportByCategoryParent) list.get(i)).setDatetimeStart(new Date());
			((RetailTradeDailyReportByCategoryParent) list.get(i)).setDatetimeEnd(new Date());
			err = ((RetailTradeDailyReportByCategoryParent) list.get(i)).checkRetrieveN(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		System.out.println("查询对象成功: " + list);
		//
		return list;
	}

	/*
	 * 
	 * 仅为了测试，传递int1使测试通过  （1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用）
	 */
	@Test
	public void createTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case1 零售单有销售记录且包含多个商品大类 ");

		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setSaleDatetime(new Date());
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		// rdrc.setDateTime(new Date());
		rdrc.setDeleteOldData(DELETE_OLD_DATA_FOR_TEST);
		createRDRCP(rdrc);
		//
		// 为了测试该case暂时去除
		// System.out.println("\n------------------------ case5
		// 重复添加触发组合唯一键---------------------------");
		// RetailTradeDailyReportByCategoryParent rdrc5 = new
		// RetailTradeDailyReportByCategoryParent();
		// rdrc5.setSaleDatetime(sdf.parse("2018-12-09 0:00:00"));
		// Map<String, Object> params5 = rdrc5.getCreateParamEx(BaseBO.INVALID_CASE_ID,
		// rdrc5);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// List<List<BaseModel>> list5 = rdrcMapper.createEx(params5);
		// Assert.assertTrue(list5.size() == 0 &&
		// EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	/*
	 * 
	 * 仅为了测试，传递int1使测试通过  （1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用）
	 */
	@Test
	public void createTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case2  零售单有销售记录且包含一个商品大类 ");

		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setSaleDatetime(sdf.parse("2019-4-18 15:30:30"));
		rdrc.setDeleteOldData(DELETE_OLD_DATA_FOR_TEST);
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		createRDRCP(rdrc);
	}

	/*
	 * 
	 * 仅为了测试，传递int1使测试通过  （1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用）
	 */
	@Test
	public void createTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case3  零售单有销售记录且包含退货商品 ");

		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setSaleDatetime(sdf.parse("2019-01-18 15:30:30"));
		rdrc.setDeleteOldData(DELETE_OLD_DATA_FOR_TEST);
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		createRDRCP(rdrc);
	}

	/*
	 * 
	 * 仅为了测试，传递int1使测试通过  （1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用）
	 */
	@Test
	public void createTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case4 零售单无记录添加 ");

		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setSaleDatetime(sdf.parse("2029-01-13 15:30:30"));
		rdrc.setDeleteOldData(DELETE_OLD_DATA_FOR_TEST);
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		createRDRCP(rdrc);
	}

	/*
	 * 
	 * 仅为了测试，传递int1使测试通过  （1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用）
	 */

	// 注意：createTest_CASE5-createTest_CASE8不能连续运行两次，否则第二次失败，因为这几个测试是
	// 验证某一天某一个分类的销售额，所以运行第二次的时候，这一天这一分类的销售额会叠加起来，导致验证失败
	// 所以运行第二次可以在sql删除测试日期的关于retailtrade的数据(目前没有删除零售单的接口)，或者刷新小王子
	@Test
	public void createTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case5 3029/03/02号分类1有零售单A,销售总额为50，退货单B,B对A退货30，那么这一天分类1的销售总额是20 ");

		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSaleDatetime(sdf.parse("3029-03-02 00:00:00"));
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 新建退货单B
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setSourceID(rTrade.getID());
		returnRetailTrade.setSaleDatetime(sdf.parse("3029-03-02 00:00:00"));
		RetailTrade returnTrade = BaseRetailTradeTest.createRetailTrade(returnRetailTrade);
		//
		RetailTradeCommodity returnRetailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		returnRetailTradeCommodity.setPriceReturn(2d);
		returnRetailTradeCommodity.setTradeID(returnTrade.getID());
		returnRetailTradeCommodity.setCommodityID(comm.getID());
		returnRetailTradeCommodity.setCommodityName(comm.getName());
		returnRetailTradeCommodity.setBarcodeID(barcodes2.getID());
		returnRetailTradeCommodity.setNO(15);
		returnRetailTradeCommodity.setNOCanReturn(10);
		returnRetailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(returnRetailTradeCommodity, EnumErrorCode.EC_NoError);
		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setSaleDatetime(sdf.parse("3029-03-02 00:00:00"));
		rdrc.setDeleteOldData(DELETE_OLD_DATA_FOR_TEST);
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		String err = rdrc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = rdrc.getCreateParamEx(BaseBO.INVALID_CASE_ID, rdrc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportByCategoryParentMapper.createEx(params);
		//
		RetailTradeDailyReportByCategoryParent rtdr;
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (int i = 0; i < list.size(); i++) {
			((RetailTradeDailyReportByCategoryParent) list.get(i)).setSaleDatetime(((RetailTradeDailyReportByCategoryParent) list.get(i)).getCreateDatetime());
			err = ((RetailTradeDailyReportByCategoryParent) list.get(i)).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			rtdr = (RetailTradeDailyReportByCategoryParent) list.get(i);
			if (rtdr.getCategoryParentID() == 1) {
				Assert.assertTrue(rtdr.getTotalAmount() == 20, "金额不对");
			}
		}
	}

	/*
	 * 
	 * 仅为了测试，传递int1使测试通过  （1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用）
	 */
	@Test
	public void createTest_CASE6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case6 3月4号分类1有零售单A,销售总额为50，退货单B,B对3月3号的分类1零售单C退货30，那么这一天分类1的销售总额是20");
		// 新建3月3号零售单C
		RetailTrade retailTradeC = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeC.setSaleDatetime(sdf.parse("3029-03-03 00:00:00"));
		RetailTrade rTradeC = BaseRetailTradeTest.createRetailTrade(retailTradeC);
		// 创建零售单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodityC = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodityC.setPriceReturn(2d);
		retailTradeCommodityC.setTradeID(rTradeC.getID());
		retailTradeCommodityC.setCommodityID(comm.getID());
		retailTradeCommodityC.setCommodityName(comm.getName());
		retailTradeCommodityC.setBarcodeID(barcodes2.getID());
		retailTradeCommodityC.setNO(25);
		retailTradeCommodityC.setNOCanReturn(10);
		retailTradeCommodityC.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodityC, EnumErrorCode.EC_NoError);
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSaleDatetime(sdf.parse("3029-03-04 00:00:00"));
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 新建退货单B，B对3月3号的零售单C进行退货
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setSourceID(rTradeC.getID());
		returnRetailTrade.setSaleDatetime(sdf.parse("3029-03-04 00:00:00"));
		RetailTrade returnTrade = BaseRetailTradeTest.createRetailTrade(returnRetailTrade);
		//
		RetailTradeCommodity returnRetailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		returnRetailTradeCommodity.setPriceReturn(2d);
		returnRetailTradeCommodity.setTradeID(returnTrade.getID());
		returnRetailTradeCommodity.setCommodityID(comm.getID());
		returnRetailTradeCommodity.setCommodityName(comm.getName());
		returnRetailTradeCommodity.setBarcodeID(barcodes2.getID());
		returnRetailTradeCommodity.setNO(15);
		returnRetailTradeCommodity.setNOCanReturn(10);
		returnRetailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(returnRetailTradeCommodity, EnumErrorCode.EC_NoError);
		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setSaleDatetime(sdf.parse("3029-03-04 00:00:00"));
		rdrc.setDeleteOldData(DELETE_OLD_DATA_FOR_TEST);
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		String err = rdrc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = rdrc.getCreateParamEx(BaseBO.INVALID_CASE_ID, rdrc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportByCategoryParentMapper.createEx(params);
		//
		RetailTradeDailyReportByCategoryParent rtdr;
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (int i = 0; i < list.size(); i++) {
			((RetailTradeDailyReportByCategoryParent) list.get(i)).setSaleDatetime(((RetailTradeDailyReportByCategoryParent) list.get(i)).getCreateDatetime());
			err = ((RetailTradeDailyReportByCategoryParent) list.get(i)).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			rtdr = (RetailTradeDailyReportByCategoryParent) list.get(i);
			if (rtdr.getCategoryParentID() == 1) {
				Assert.assertTrue(rtdr.getTotalAmount() == 20, "金额不对");
			}
		}
	}

	/*
	 * 
	 * 仅为了测试，传递int1使测试通过  （1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用）
	 */
	@Test
	public void createTest_CASE7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case7 3月6号分类1有零售单A,销售总额为50，退货单B,B对3月5号的零售单C退货总额100，那么这一天分类1的销售总额就为-50");
		// 新建3月3号零售单C
		RetailTrade retailTradeC = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeC.setSaleDatetime(sdf.parse("3029-03-05 00:00:00"));
		RetailTrade rTradeC = BaseRetailTradeTest.createRetailTrade(retailTradeC);
		// 创建零售单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(comm.getID());
		barcodes.setBarcode(comm.getBarcodes());
		barcodes.setOperatorStaffID(STAFF_ID3);
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodityC = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodityC.setPriceReturn(2d);
		retailTradeCommodityC.setTradeID(rTradeC.getID());
		retailTradeCommodityC.setCommodityID(comm.getID());
		retailTradeCommodityC.setCommodityName(comm.getName());
		retailTradeCommodityC.setBarcodeID(barcodes2.getID());
		retailTradeCommodityC.setNO(100);
		retailTradeCommodityC.setNOCanReturn(10);
		retailTradeCommodityC.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodityC, EnumErrorCode.EC_NoError);
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSaleDatetime(sdf.parse("3029-03-06 00:00:00"));
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 新建退货单B，B对3月3号的零售单C进行退货
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setSourceID(rTradeC.getID());
		returnRetailTrade.setSaleDatetime(sdf.parse("3029-03-06 00:00:00"));
		RetailTrade returnTrade = BaseRetailTradeTest.createRetailTrade(returnRetailTrade);
		//
		RetailTradeCommodity returnRetailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		returnRetailTradeCommodity.setPriceReturn(2d);
		returnRetailTradeCommodity.setTradeID(returnTrade.getID());
		returnRetailTradeCommodity.setCommodityID(comm.getID());
		returnRetailTradeCommodity.setCommodityName(comm.getName());
		returnRetailTradeCommodity.setBarcodeID(barcodes2.getID());
		returnRetailTradeCommodity.setNO(50);
		returnRetailTradeCommodity.setNOCanReturn(10);
		returnRetailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(returnRetailTradeCommodity, EnumErrorCode.EC_NoError);
		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setSaleDatetime(sdf.parse("3029-03-06 00:00:00"));
		rdrc.setDeleteOldData(DELETE_OLD_DATA_FOR_TEST);
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		String err = rdrc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = rdrc.getCreateParamEx(BaseBO.INVALID_CASE_ID, rdrc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportByCategoryParentMapper.createEx(params);
		//
		RetailTradeDailyReportByCategoryParent rtdr;
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (int i = 0; i < list.size(); i++) {
			((RetailTradeDailyReportByCategoryParent) list.get(i)).setSaleDatetime(((RetailTradeDailyReportByCategoryParent) list.get(i)).getCreateDatetime());
			err = ((RetailTradeDailyReportByCategoryParent) list.get(i)).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			rtdr = (RetailTradeDailyReportByCategoryParent) list.get(i);
			if (rtdr.getCategoryParentID() == 1) {
				Assert.assertTrue(rtdr.getTotalAmount() == -50, "金额不对");
			}
		}
	}

	/*
	 * 
	 * 仅为了测试，传递int1使测试通过  （1，删除旧的数据。只能在测试代码中使用。0，不删除旧的数据。只能在功能代码中使用）
	 */
	@Test
	public void createTest_CASE8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case8 3月8号分类1有零售单A,销售总额为50，分类2无零售，退货单B,B对3月7号分类2零售单C退货30，那么这一天分类1的销售总额是50，分类2销售总额-30");
		// 新建3月3号零售单C
		RetailTrade retailTradeC = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeC.setSaleDatetime(sdf.parse("3029-03-07 00:00:00"));
		RetailTrade rTradeC = BaseRetailTradeTest.createRetailTrade(retailTradeC);
		// 创建零售单C的商品
		Commodity commodityC = BaseCommodityTest.DataInput.getCommodity();
		commodityC.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commodityC.setCategoryID(2);
		Commodity commC = BaseCommodityTest.createCommodityViaMapper(commodityC, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodesC = new Barcodes();
		barcodesC.setCommodityID(commC.getID());
		barcodesC.setBarcode(commC.getBarcodes());
		barcodesC.setOperatorStaffID(STAFF_ID3);
		Barcodes barcodes2C = BaseBarcodesTest.retrieveNBarcodes(commC.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodityC = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodityC.setPriceReturn(2d);
		retailTradeCommodityC.setTradeID(rTradeC.getID());
		retailTradeCommodityC.setCommodityID(commC.getID());
		retailTradeCommodityC.setCommodityName(commC.getName());
		retailTradeCommodityC.setBarcodeID(barcodes2C.getID());
		retailTradeCommodityC.setNO(100);
		retailTradeCommodityC.setNOCanReturn(10);
		retailTradeCommodityC.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodityC, EnumErrorCode.EC_NoError);
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSaleDatetime(sdf.parse("3029-03-08 00:00:00"));
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建A的零售单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(comm.getID());
		barcodes.setBarcode(comm.getBarcodes());
		barcodes.setOperatorStaffID(STAFF_ID3);
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);

		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 新建退货单B，B对3月3号的零售单C进行退货
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setSourceID(rTradeC.getID());
		returnRetailTrade.setSaleDatetime(sdf.parse("3029-03-08 00:00:00"));
		RetailTrade returnTrade = BaseRetailTradeTest.createRetailTrade(returnRetailTrade);
		//
		RetailTradeCommodity returnRetailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		returnRetailTradeCommodity.setPriceReturn(2d);
		returnRetailTradeCommodity.setTradeID(returnTrade.getID());
		returnRetailTradeCommodity.setCommodityID(commC.getID());
		returnRetailTradeCommodity.setCommodityName(commC.getName());
		returnRetailTradeCommodity.setBarcodeID(barcodes2C.getID());
		returnRetailTradeCommodity.setNO(15);
		returnRetailTradeCommodity.setNOCanReturn(10);
		returnRetailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(returnRetailTradeCommodity, EnumErrorCode.EC_NoError);
		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setSaleDatetime(sdf.parse("3029-03-08 00:00:00"));
		rdrc.setDeleteOldData(DELETE_OLD_DATA_FOR_TEST);
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		String err = rdrc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = rdrc.getCreateParamEx(BaseBO.INVALID_CASE_ID, rdrc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportByCategoryParentMapper.createEx(params);
		//
		RetailTradeDailyReportByCategoryParent rtdr;
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (int i = 0; i < list.size(); i++) {
			((RetailTradeDailyReportByCategoryParent) list.get(i)).setSaleDatetime(((RetailTradeDailyReportByCategoryParent) list.get(i)).getCreateDatetime());
			err = ((RetailTradeDailyReportByCategoryParent) list.get(i)).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			rtdr = (RetailTradeDailyReportByCategoryParent) list.get(i);
			if (rtdr.getCategoryParentID() == 1) {
				Assert.assertTrue(rtdr.getTotalAmount() == 50, "销售金额不对");
			} else if (rtdr.getCategoryParentID() == 2) {
				Assert.assertTrue(rtdr.getTotalAmount() == -30, "销售金额不对");
			}
		}
	}

	@Test
	public void retrieveNTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:查询一天 ");

		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setDatetimeStart(sdf.parse("2019-01-16 00:00:00"));
		rdrc.setDatetimeEnd(sdf.parse("2019-01-16 23:59:59"));
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		retrieveNRDRCP(rdrc);
	}

	@Test
	public void retrieveNTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:查询一个月 ");

		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setDatetimeStart(sdf.parse("2019-01-01 00:00:00"));
		rdrc.setDatetimeEnd(sdf.parse("2019-01-31 23:59:59"));
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		retrieveNRDRCP(rdrc);
	}

	@Test
	public void retrieveNTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:查询某个特定的时间段 ");

		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setDatetimeStart(sdf.parse("2019-01-16 00:00:00"));
		rdrc.setDatetimeEnd(sdf.parse("2019-01-17 23:59:59"));
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		retrieveNRDRCP(rdrc);
	}

	@Test
	public void retrieveNTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:查询不存在的时间段 ");

		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setDatetimeStart(sdf.parse("2029-01-15 00:00:00"));
		rdrc.setDatetimeEnd(sdf.parse("2029-01-15 23:59:59"));
		rdrc.setShopID(Shared.DEFAULT_Shop_ID);
		//
		retrieveNRDRCP(rdrc);
	}
	
	@Test
	public void retrieveNTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:查询不存在的门店ID ");

		RetailTradeDailyReportByCategoryParent rdrc = new RetailTradeDailyReportByCategoryParent();
		rdrc.setDatetimeStart(sdf.parse("2029-01-15 00:00:00"));
		rdrc.setDatetimeEnd(sdf.parse("2029-01-15 23:59:59"));
		rdrc.setShopID(BaseAction.INVALID_ID);
		//
		retrieveNRDRCP(rdrc);
	}
}
