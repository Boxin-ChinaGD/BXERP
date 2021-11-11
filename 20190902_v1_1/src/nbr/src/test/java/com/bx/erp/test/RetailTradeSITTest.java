package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
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
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.Commodity.EnumStatusCommodity;
import com.bx.erp.util.DataSourceContextHolder;

/** 模拟零售+退货，正确计算可退货数量和库存。零售商品的类型覆盖：单品、多包装商品、组合商品 */
public class RetailTradeSITTest extends BaseMapperTest {
	private static int befourNO1;
	private static int afterNO1;
	private static int befourNO2;
	private static int afterNO2;


	/** 获取零售或者退货前的商品库存 */
	private int getCommodityNO(int commID) {

		Commodity comm = new Commodity();
		comm.setID(commID);
		//
		Map<String, Object> paramsRetrieve = comm.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> commRetrived = commodityMapper.retrieve1Ex(paramsRetrieve);// ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		//
		comm = Commodity.fetchCommodityFromResultSet(commRetrived);

		return comm.getNO();
	}

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	// 普通商品
	@Test
	public void runRetailTradeBySimple() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建甲机零售单3个 乙机零售单3个
		System.out.println("------------------------ 创建甲机零售单3个 已机零售单3个 ------------------------");

		RetailTrade retailTradeCreateA1 = BaseRetailTradeTest.createRetailTradeViaMapper(BaseAction.INVALID_ID);
		RetailTrade retailTradeCreateA2 = BaseRetailTradeTest.createRetailTradeViaMapper(BaseAction.INVALID_ID);
		RetailTrade retailTradeCreateA3 = BaseRetailTradeTest.createRetailTradeViaMapper(BaseAction.INVALID_ID);

		RetailTrade retailTradeCreateB1 = BaseRetailTradeTest.createRetailTradeViaMapper(BaseAction.INVALID_ID);
		RetailTrade retailTradeCreateB2 = BaseRetailTradeTest.createRetailTradeViaMapper(BaseAction.INVALID_ID);
		RetailTrade retailTradeCreateB3 = BaseRetailTradeTest.createRetailTradeViaMapper(BaseAction.INVALID_ID);

		// 获取所有正常商品信息
		HashSet<Integer> commIDs = new HashSet<Integer>();
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(BaseAction.PAGE_StartIndex);
		comm.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> bmList = BaseCommodityTest.retrieveNViaMapper(comm, Shared.DBName_Test);
		Assert.assertTrue(bmList != null && bmList.size() > 0);

		while (commIDs.size() < 3) {
			Commodity commodity = (Commodity) bmList.get(new Random().nextInt(bmList.size()));

			// 判断是否是单品，并且状态为0..不重复。
			if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex() && commodity.getStatus() == EnumStatusCommodity.ESC_Normal.getIndex()) {
				commIDs.add(commodity.getID());
			}
		}

		Iterator<Integer> iterator = commIDs.iterator();

		// 是否是第一次对零售单进行退货
		int firstTimeToReturnRetailTrade = 1;
		// 往零售单里面添加商品若干
		System.out.println("--------------往零售单里面添加商品若干-----------------------");
		while (iterator.hasNext()) {
			System.out.println("\n------------------------零售单添加商品 ------------------------");
			int CommID = iterator.next();

			befourNO1 = getCommodityNO(CommID);
			RetailTradeCommodity retailTradeCommCreateA1 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(retailTradeCreateA1.getID(), CommID, 10, EnumErrorCode.EC_NoError);
			afterNO1 = getCommodityNO(CommID);
			assertTrue(befourNO1 == afterNO1 + 10, "商品库存没有正确增减！");
			System.out.println("零售单商品A1:" + retailTradeCommCreateA1);

			befourNO1 = getCommodityNO(CommID);
			RetailTradeCommodity retailTradeCommCreateA2 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(retailTradeCreateA2.getID(), CommID, 10, EnumErrorCode.EC_NoError);
			afterNO1 = getCommodityNO(CommID);
			assertTrue(befourNO1 == afterNO1 + 10, "商品库存没有正确增减！");
			System.out.println("零售单商品A2:" + retailTradeCommCreateA2);

			befourNO1 = getCommodityNO(CommID);
			RetailTradeCommodity retailTradeCommCreateA3 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(retailTradeCreateA3.getID(), CommID, 10, EnumErrorCode.EC_NoError);
			afterNO1 = getCommodityNO(CommID);
			assertTrue(befourNO1 == afterNO1 + 10, "商品库存没有正确增减！");
			System.out.println("零售单商品A3:" + retailTradeCommCreateA3);

			befourNO1 = getCommodityNO(CommID);
			RetailTradeCommodity retailTradeCommCreateB1 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(retailTradeCreateB1.getID(), CommID, 10, EnumErrorCode.EC_NoError); // ...
			afterNO1 = getCommodityNO(CommID);
			assertTrue(befourNO1 == afterNO1 + 10, "商品库存没有正确增减！");
			System.out.println("零售单商品B1:" + retailTradeCommCreateB1);

			befourNO1 = getCommodityNO(CommID);
			RetailTradeCommodity retailTradeCommCreateB2 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(retailTradeCreateB2.getID(), CommID, 10, EnumErrorCode.EC_NoError); // ...
			afterNO1 = getCommodityNO(CommID);
			assertTrue(befourNO1 == afterNO1 + 10, "商品库存没有正确增减！");
			System.out.println("零售单商品B2:" + retailTradeCommCreateB2);

			befourNO1 = getCommodityNO(CommID);
			RetailTradeCommodity retailTradeCommCreateB3 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(retailTradeCreateB3.getID(), CommID, 10, EnumErrorCode.EC_NoError); // ...
			afterNO1 = getCommodityNO(CommID);
			assertTrue(befourNO1 == afterNO1 + 10, "商品库存没有正确增减！");
			System.out.println("零售单商品B3:" + retailTradeCommCreateB3);
			// 甲机获取甲1T
			RetailTrade retrieveA1 = new RetailTrade();
			retrieveA1.setID(retailTradeCreateA1.getID());
			retrieveA1.setPos_ID(retailTradeCreateA1.getPos_ID());
			retrieveA1.setLocalSN(retailTradeCreateA1.getLocalSN());

			Map<String, Object> retrieveNA1Params = retrieveA1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retrieveA1);

			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> retrieveNA1Trade = (List<BaseModel>) retailTradeMapper.retrieveN(retrieveNA1Params);
			Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(retrieveNA1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

			System.out.println(retrieveNA1Trade.size() == 0 ? "获取甲1T失败！" : "甲1T:" + retrieveNA1Trade.toString());

			RetailTradeCommodity retrieveCommA1 = new RetailTradeCommodity();
			retrieveCommA1.setTradeID(retailTradeCreateA1.getID());

			Map<String, Object> retrieveNCimm1Params = retrieveCommA1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retrieveCommA1);

			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> retrieveNA1Comm = (List<BaseModel>) retailTradeCommodityMapper.retrieveN(retrieveNCimm1Params);

			System.out.println(retrieveNA1Comm.size() == 0 ? "获取甲1T商品失败！" : "甲1T商品:" + retrieveNA1Comm.toString());
			// 甲机创建甲4T，针对甲1T进行退货操作（退1件货）。不需要修改甲1T的POS_ID 和
			// POS_SN，也不需要修改甲1T。检查F_NOCanReturn的正确性

			if (firstTimeToReturnRetailTrade == 1) { // 创建对货单都只能退货一次

				RetailTrade returnA1 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCreateA1.getID());

				System.out.println("退货零售单:" + returnA1);

				for (BaseModel returnComm : retrieveNA1Comm) {
					RetailTradeCommodity rc = (RetailTradeCommodity) returnComm;

					befourNO1 = getCommodityNO(rc.getCommodityID());
					RetailTradeCommodity returnCommA1 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(returnA1.getID(), rc.getCommodityID(), 1, EnumErrorCode.EC_NoError);
					afterNO1 = getCommodityNO(rc.getCommodityID());
					assertTrue(befourNO1 == afterNO1 - 1, "商品库存没有正确增减！");

					System.out.println("退货单商品:" + returnCommA1);
				}

				// 甲机创建甲5T，针对甲1T进行退货操作（退1种货，即该类型的货全退）。不需要修改甲1T的POS_ID 和
				// POS_SN，也不需要修改甲1T。检查F_NOCanReturn的正确性
				RetailTrade returnRtA2 = BaseRetailTradeTest.createReturnRetailTradeFailViaMapper(retailTradeCreateA1.getID());// 现在业务不允许对零售单进行退货两次，所以这次应该是退货失败
				Assert.assertTrue(returnRtA2 == null, "第二次对零售单进行退货，退货应该失败");
				System.out.println("针对甲1T退货零售单:" + returnRtA2);

				// 创建不了退货型零售单，也应该不用创建零售商品
				// for (BaseModel returnComm : retrieveNA1Comm) {
				// RetailTradeCommodity rc = (RetailTradeCommodity) returnComm;
				//
				// befourNO1 = getCommodityNO(rc.getCommodityID());
				// RetailTradeCommodity returnCommParamsA1 =
				// BaseRetailTradeTest.getRetailTradeCommodity(returnRtA2.getID(), rc.getCommodityID(), 1,
				// EnumErrorCode.EC_NoError);
				// afterNO1 = getCommodityNO(rc.getCommodityID());
				// assertTrue(befourNO1 == afterNO1 - 1, "商品库存没有正确增减！");
				//
				// System.out.println("退货单商品:" + returnCommParamsA1);
				// break;
				// }

				// 甲机创建甲6T，针对乙1T进行退货操作（退1件货）。不需要修改源交易的POS_ID 和
				// POS_SN，也不需要修改乙1T。检查F_NOCanReturn的正确性
				RetailTrade returnRtA3 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCreateB1.getID());

				System.out.println("针对乙1T退货零售单:" + returnRtA3);

				RetailTradeCommodity retrieveCommB1 = new RetailTradeCommodity();
				retrieveCommB1.setTradeID(retailTradeCreateB1.getID());

				Map<String, Object> retrieveNCommB1Params = retrieveCommB1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retrieveCommB1);

				DataSourceContextHolder.setDbName(Shared.DBName_Test);
				List<BaseModel> retrieveNB1Comm = (List<BaseModel>) retailTradeCommodityMapper.retrieveN(retrieveNCommB1Params);

				System.out.println(retrieveNB1Comm.size() == 0 ? "获取乙1T商品失败！" : "乙1T商品:" + retrieveNB1Comm.toString());

				for (BaseModel returnComm : retrieveNB1Comm) {
					RetailTradeCommodity rc = (RetailTradeCommodity) returnComm;

					befourNO1 = getCommodityNO(rc.getCommodityID());
					RetailTradeCommodity returnCommB1 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(returnRtA3.getID(), rc.getCommodityID(), 1, EnumErrorCode.EC_NoError);
					afterNO1 = getCommodityNO(rc.getCommodityID());
					assertTrue(befourNO1 == afterNO1 - 1, "商品库存没有正确增减！");

					System.out.println("退货单商品:" + returnCommB1);
				}
				// 甲机创建甲7T，针对乙2T进行退货操作（退1种货，即该类型的货全退）。不需要修改乙2T的POS_ID 和
				// POS_SN，也不需要修改乙2T。检查F_NOCanReturn的正确性
				RetailTrade returnRtA4 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCreateB2.getID());

				System.out.println("针对乙2T退货零售单:" + returnRtA4);

				RetailTradeCommodity retrieveCommB2 = new RetailTradeCommodity();
				retrieveCommB2.setTradeID(retailTradeCreateB2.getID());

				Map<String, Object> retrieveNCommB2Params = retrieveCommB2.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retrieveCommB2);

				DataSourceContextHolder.setDbName(Shared.DBName_Test);
				List<BaseModel> retrieveNB2Comm = (List<BaseModel>) retailTradeCommodityMapper.retrieveN(retrieveNCommB2Params);

				System.out.println(retrieveNB2Comm.size() == 0 ? "获取乙2T商品失败！" : "乙2T商品:" + retrieveNB2Comm.toString());

				for (BaseModel returnComm : retrieveNB2Comm) {
					RetailTradeCommodity rc = (RetailTradeCommodity) returnComm;

					befourNO1 = getCommodityNO(rc.getCommodityID());
					RetailTradeCommodity returnCommB2 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(returnRtA4.getID(), rc.getCommodityID(), 1, EnumErrorCode.EC_NoError);
					afterNO1 = getCommodityNO(rc.getCommodityID());
					assertTrue(befourNO1 == afterNO1 - 1, "商品库存没有正确增减！");

					System.out.println("针对乙2T退货单商品:" + returnCommB2);
					break;
				}
				// 甲机创建甲8T，针对乙3T进行退货操作（退1件货，退货数量大于购买数量）。不需要修改源交易的POS_ID 和
				// POS_SN，也不需要修改乙1T。检查F_NOCanReturn是否为负数
				RetailTrade returnRtA5 = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCreateB3.getID());

				System.out.println("针对乙3T退货零售单:" + returnRtA5);

				RetailTradeCommodity retrieveCommB3 = new RetailTradeCommodity();
				retrieveCommB3.setTradeID(retailTradeCreateB3.getID());

				Map<String, Object> retrieveNCommB3Params = retrieveCommB3.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retrieveCommB3);

				DataSourceContextHolder.setDbName(Shared.DBName_Test);
				List<BaseModel> retrieveNB3Comm = (List<BaseModel>) retailTradeCommodityMapper.retrieveN(retrieveNCommB3Params);

				System.out.println(retrieveNB3Comm.size() == 0 ? "获取乙3T商品失败！" : "乙3T商品:" + retrieveNB3Comm.toString());

				for (BaseModel returnComm : retrieveNB3Comm) {
					RetailTradeCommodity rc = (RetailTradeCommodity) returnComm;

					RetailTradeCommodity returnCommB3 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(returnRtA5.getID(), rc.getCommodityID(), 999, EnumErrorCode.EC_BusinessLogicNotDefined);

					System.out.println("针对乙3T退货单商品:" + returnCommB3);
				}
			}
			firstTimeToReturnRetailTrade++;
		}
	}

	// 组合商品
	@Test
	public void runRetailTradeByCombination() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		final int subCommodityIDA = 155;
		final int subCommodityIDB = 158;

		// 创建零售单A 和 零售单B
		RetailTrade retailTradeCreateA = BaseRetailTradeTest.createRetailTradeViaMapper(BaseAction.INVALID_ID);
		RetailTrade retailTradeCreateB = BaseRetailTradeTest.createRetailTradeViaMapper(BaseAction.INVALID_ID);

		// 给零售单添加若干组合商品
		befourNO1 = getCommodityNO(subCommodityIDA);
		befourNO2 = getCommodityNO(subCommodityIDB);

		RetailTradeCommodity retailTradeCommCreateA1 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(retailTradeCreateA.getID(), 157, 10, EnumErrorCode.EC_NoError);

		afterNO1 = getCommodityNO(subCommodityIDA);
		afterNO2 = getCommodityNO(subCommodityIDB);

		assertTrue(befourNO1 == afterNO1 + 20 && befourNO2 == afterNO2 + 30, "商品库存没有正确增减！");
		System.out.println("零售单商品A1:" + retailTradeCommCreateA1);

		befourNO1 = getCommodityNO(subCommodityIDA);
		befourNO2 = getCommodityNO(subCommodityIDB);

		RetailTradeCommodity retailTradeCommCreateA2 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(retailTradeCreateB.getID(), 157, 5, EnumErrorCode.EC_NoError);

		afterNO1 = getCommodityNO(subCommodityIDA);
		afterNO2 = getCommodityNO(subCommodityIDB);

		assertTrue(befourNO1 == afterNO1 + 10 && befourNO2 == afterNO2 + 15, "商品库存没有正确增减！");
		System.out.println("零售单商品B1:" + retailTradeCommCreateA2);

		// 创建退货单A
		RetailTrade returnRetailTradeCreateA = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCreateA.getID());

		// 对零售单A商品进行全部退货
		befourNO1 = getCommodityNO(subCommodityIDA);
		befourNO2 = getCommodityNO(subCommodityIDB);

		RetailTradeCommodity returnRetailTradeCommCreateA1 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(returnRetailTradeCreateA.getID(), 157, 10, EnumErrorCode.EC_NoError);

		afterNO1 = getCommodityNO(subCommodityIDA);
		afterNO2 = getCommodityNO(subCommodityIDB);

		assertTrue(befourNO1 == afterNO1 - 20 && befourNO2 == afterNO2 - 30, "商品库存没有正确增减！");
		System.out.println("退货单零售单商品A1:" + returnRetailTradeCommCreateA1);

		// 创建退货单B
		RetailTrade returnRetailTradeCreateB = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCreateB.getID());

		// 对零售单B商品进行部分退货
		befourNO1 = getCommodityNO(subCommodityIDA);
		befourNO2 = getCommodityNO(subCommodityIDB);

		RetailTradeCommodity returnRetailTradeCommCreateB1 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(returnRetailTradeCreateB.getID(), 157, 1, EnumErrorCode.EC_NoError);

		afterNO1 = getCommodityNO(subCommodityIDA);
		afterNO2 = getCommodityNO(subCommodityIDB);

		assertTrue(befourNO1 == afterNO1 - 2 && befourNO2 == afterNO2 - 3, "商品库存没有正确增减！");
		System.out.println("退货单零售单商品B1:" + returnRetailTradeCommCreateB1);

		// 创建退货单C
		RetailTrade returnRetailTradeCreateC = BaseRetailTradeTest.createReturnRetailTradeFailViaMapper(retailTradeCreateB.getID());// 退货失败，只能对零售单退货一次
		Assert.assertTrue(returnRetailTradeCreateC == null, "只能对零售单退货一次");
		// // 对零售单B商品进行退货，超过可退货数量，返回错误码7
		// befourNO1 = getCommodityNO(subCommodityIDA);
		// befourNO2 = getCommodityNO(subCommodityIDB);
		//
		// RetailTradeCommodity returnRetailTradeCommCreateB2 =
		// BaseRetailTradeTest.getRetailTradeCommodity(returnRetailTradeCreateC.getID(), 157, 999,
		// EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		// afterNO1 = getCommodityNO(subCommodityIDA);
		// afterNO2 = getCommodityNO(subCommodityIDB);
		//
		// assertTrue(returnRetailTradeCommCreateB2 == null, "错误码返回有误！");
	}

	// 多包装商品
	@Test
	public void runRetailTradeByMultiPackaging() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		final int simpleCommodityID = 155;

		// 创建零售单A 和 零售单B
		RetailTrade retailTradeCreateA = BaseRetailTradeTest.createRetailTradeViaMapper(BaseAction.INVALID_ID);
		RetailTrade retailTradeCreateB = BaseRetailTradeTest.createRetailTradeViaMapper(BaseAction.INVALID_ID);

		// 给零售单添加若干组合商品
		befourNO1 = getCommodityNO(simpleCommodityID);
		RetailTradeCommodity retailTradeCommCreateA1 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(retailTradeCreateA.getID(), 156, 10, EnumErrorCode.EC_NoError);
		afterNO1 = getCommodityNO(simpleCommodityID);

		assertTrue(befourNO1 == afterNO1 + 30, "商品库存没有正确增减！");
		System.out.println("零售单商品A1:" + retailTradeCommCreateA1);

		befourNO1 = getCommodityNO(simpleCommodityID);
		RetailTradeCommodity retailTradeCommCreateA2 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(retailTradeCreateB.getID(), 156, 5, EnumErrorCode.EC_NoError);
		afterNO1 = getCommodityNO(simpleCommodityID);

		assertTrue(befourNO1 == afterNO1 + 15, "商品库存没有正确增减！");
		System.out.println("零售单商品B1:" + retailTradeCommCreateA2);

		// 创建退货单A
		RetailTrade returnRetailTradeCreateA = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCreateA.getID());

		// 对零售单A商品进行全部退货
		befourNO1 = getCommodityNO(simpleCommodityID);
		RetailTradeCommodity returnRetailTradeCommCreateA1 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(returnRetailTradeCreateA.getID(), 156, 10, EnumErrorCode.EC_NoError);
		afterNO1 = getCommodityNO(simpleCommodityID);

		assertTrue(befourNO1 == afterNO1 - 30, "商品库存没有正确增减！");
		System.out.println("退货单零售单商品A1:" + returnRetailTradeCommCreateA1);

		// 创建退货单B
		RetailTrade returnRetailTradeCreateB = BaseRetailTradeTest.createRetailTradeViaMapper(retailTradeCreateB.getID());

		// 对零售单B商品进行部分退货
		befourNO1 = getCommodityNO(simpleCommodityID);
		RetailTradeCommodity returnRetailTradeCommCreateB1 = BaseRetailTradeTest.createRetailTradeCommodityViaMapper(returnRetailTradeCreateB.getID(), 156, 1, EnumErrorCode.EC_NoError);
		afterNO1 = getCommodityNO(simpleCommodityID);

		assertTrue(befourNO1 == afterNO1 - 3, "商品库存没有正确增减！");
		System.out.println("退货单零售单商品B1:" + returnRetailTradeCommCreateB1);

		// 创建退货单C
		RetailTrade returnRetailTradeCreateC = BaseRetailTradeTest.createReturnRetailTradeFailViaMapper(retailTradeCreateB.getID());
		Assert.assertTrue(returnRetailTradeCreateC == null, "只能对零售单退货一次");
		// // 对零售单B商品进行退货，超过可退货数量，返回错误码7
		// befourNO1 = getCommodityNO(simpleCommodityID);
		// RetailTradeCommodity returnRetailTradeCommCreateB2 =
		// BaseRetailTradeTest.getRetailTradeCommodity(returnRetailTradeCreateC.getID(), 156, 999,
		// EnumErrorCode.EC_BusinessLogicNotDefined);
		// afterNO1 = getCommodityNO(simpleCommodityID);
		//
		// assertTrue(returnRetailTradeCommCreateB2 == null, "错误码返回有误！");
	}

}
