package com.bx.erp.test.warehousing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class WarehousingMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@DataProvider(name = "getCreateData")
	public Object[][] getCreateData() {
		return new Object[][] { //
				// pPurchasingPlanTableID,status,staffID,providerID,warehouseID,message,ec,caseID
				{ 2, 5, 1, Shared.BossID, 1, 1, "case1: 采购订单状态为已审核。", EnumErrorCode.EC_NoError, 0 }, //
				{ 2, 5, 2, Shared.BossID, 1, 1, "case2: 采购订单状态为部分入库。", EnumErrorCode.EC_NoError, 0 }, //
				{ 2, 5, 0, Shared.BossID, 1, 1, "case3: 采购订单状态为未审核的。", EnumErrorCode.EC_BusinessLogicNotDefined, 0 }, //
				{ 2, 5, 3, Shared.BossID, 1, 1, "case4: 采购订单状态为全部入库的。。", EnumErrorCode.EC_BusinessLogicNotDefined, 0 }, //
				{ 2, 0, 3, Shared.BossID, 1, 1, "case5: 并不引用采购订单。", EnumErrorCode.EC_NoError, 0 }, //
				{ 2, 5, 1, BaseWarehousingTest.INVALID_VALUE, 1, 1, "case6: 用不存在的staff创建。", EnumErrorCode.EC_BusinessLogicNotDefined, 1 }, //
				{ 2, 5, 1, Shared.BossID, 1, BaseWarehousingTest.INVALID_VALUE, "case7: 用不存在的warehouseID创建。", EnumErrorCode.EC_BusinessLogicNotDefined, 1 }, //
				{ 2, 5, 1, Shared.BossID, BaseWarehousingTest.INVALID_VALUE, 1, "case8: 用不存在的providerID创建。", EnumErrorCode.EC_BusinessLogicNotDefined, 1 } //
		};
	}

	public EnumErrorCode create(int shopID, int pPurchasingPlanTableID, int status, int staffID, int providerID, int warehouseID, int caseID) {
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		//
		if (caseID == 0) {
			if (pPurchasingPlanTableID != 0) {
				PurchasingOrder po = new PurchasingOrder();
				// po.setStatus(status);
				po.setStaffID(staffID);
				po.setProviderID(providerID);
				po.setRemark("");
				po.setShopID(2);
				// 检验字段的合法性
				String errorMessage = po.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(errorMessage, "");
				//
				Map<String, Object> params = po.getCreateParam(BaseBO.INVALID_CASE_ID, po);
				DataSourceContextHolder.setDbName(Shared.DBName_Test);
				purchasingOrder = (PurchasingOrder) purchasingOrderMapper.create(params);
				Assert.assertTrue(purchasingOrder != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
						params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
				//
				po.setIgnoreIDInComparision(true);
				if (po.compareTo(purchasingOrder) != 0) {
					Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
				}
				// 检验字段的合法性
				errorMessage = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(errorMessage, "");
				// 创建采购订单商品
				PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
				poc.setCommodityID(1);
				poc.setBarcodeID(1);
				poc.setPurchasingOrderID(purchasingOrder.getID());
				poc.setCommodityNO(100);
				poc.setPriceSuggestion(11.1D);
				String err = poc.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(err, "");
				Map<String, Object> pocparams = poc.getCreateParam(BaseBO.INVALID_CASE_ID, poc);
				//
				DataSourceContextHolder.setDbName(Shared.DBName_Test);
				PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(pocparams);
				Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(pocparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
				//
				poc.setIgnoreIDInComparision(true);
				if (poc.compareTo(pocCreate) != 0) {
					Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
				}
				//
				purchasingOrder.setApproverID(3);
				purchasingOrder.setStatus(1);
				// 检验字段的合法性
				errorMessage = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(errorMessage, "");
				//
				Map<String, Object> params2 = purchasingOrder.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, purchasingOrder);
				DataSourceContextHolder.setDbName(Shared.DBName_Test);
				PurchasingOrder poApprove = purchasingOrderMapper.approve(params2);
				//
				Assert.assertTrue(poApprove != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
						params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
				//
				purchasingOrder.setIgnoreIDInComparision(true);
				if (purchasingOrder.compareTo(poApprove) != 0) {
					Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
				}
				// 检验字段的合法性
				errorMessage = poApprove.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(errorMessage, "");
				//
				if (status != 1) { // 当需要修改采购单状态为1时，则跳过这一步，因为审核之后状态就是1
					poApprove.setStatus(status);
					// 检验字段的合法性
					errorMessage = poApprove.checkCreate(BaseBO.INVALID_CASE_ID);
					Assert.assertEquals(errorMessage, "");
					//
					Map<String, Object> params3 = purchasingOrder.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, poApprove);
					DataSourceContextHolder.setDbName(Shared.DBName_Test);
					PurchasingOrder poUpdateStatus = purchasingOrderMapper.updateStatus(params3);
					Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
					//
					poUpdateStatus.setIgnoreIDInComparision(true);
					if (poApprove.compareTo(poUpdateStatus) != 0) {
						Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
					}
					// 检验字段的合法性
					errorMessage = poUpdateStatus.checkCreate(BaseBO.INVALID_CASE_ID);
					Assert.assertEquals(errorMessage, "");
				}
			}

		}
		//
		Warehousing warehousing = new Warehousing();
		warehousing.setStaffID(staffID);
		warehousing.setShopID(shopID);
		warehousing.setProviderID(providerID);
		warehousing.setWarehouseID(warehouseID);
		warehousing.setPurchasingOrderID(purchasingOrder.getID());
		// 检验字段的合法性
		String errorMessage = warehousing.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMessage, "");
		//
		Map<String, Object> params2 = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel wsCreate = warehousingMapper.create(params2);
		if (wsCreate != null) {// 创建入库商品
			BaseWarehousingTest.createWarehousingCommodityViaMapper(wsCreate.getID(), 1, 100, 1, 11.1D, 1110.0D, 365);
		}
		if (caseID == 0) {
			if (purchasingOrder.getID() == 0 || status == 1 || status == 2) {
				warehousing.setIgnoreIDInComparision(true);
				if (warehousing.compareTo(wsCreate) != 0) {
					Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
				}
				Assert.assertTrue(wsCreate != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
						params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
				// 检验字段的合法性
				errorMessage = wsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(errorMessage, "");
				// 删除创建出来的warehousing测试对象
				BaseWarehousingTest.deleteViaMapper((Warehousing) wsCreate);
			}
		} else {

		}
		return EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Test(dataProvider = "getCreateData")
	public void createTest(int shopID, int pPurchasingPlanTableID, int status, int staffID, int providerID, int warehouseID, String message, EnumErrorCode ec, int caseID) {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(message);
		Assert.assertEquals(create(shopID, pPurchasingPlanTableID, status, staffID, providerID, warehouseID, caseID), ec);
	}

	@DataProvider(name = "getRetrieveNData")
	public Object[][] getRetrieveNData() {
		return new Object[][] { //
				// EnumStatusWarehousing.ESW_ToApprove= 0
				// EnumStatusWarehousing.ESW_Approved= 1
				// BaseAction.INVALID_STATUS = BaseAction.INVALID_ID

				// iID,iProviderID,iwarehouseID,istaffID,ipurchasingOrderID,iPageIndex,iPageSize,message,ec
				{ BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, 10, "CASE1;没有参数查询所有", EnumErrorCode.EC_NoError }, //
				{ 1, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, 10, "CASE2:根据ID查询：", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, 10, "case3:根据仓库ID查：", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, BaseAction.INVALID_ID, 1, 10, "case4：根据业务员ID查：", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, 1, 10, "CASE5:根据采购订单查", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, BaseAction.INVALID_ID, 1, 1, 10, "case6: 根据采购订单ID和仓库ID查", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, 1, BaseAction.INVALID_ID, 1, 10, "case7: 根据业务员跟仓库ID查", EnumErrorCode.EC_NoError }, //
				{ 5, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 5, 1, 10, "case8：根据采购订单跟ID查", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_STATUS, BaseAction.INVALID_ID, 1, 1, 1, 1, 10, "case9:根据采购订单ID，仓库ID，业务员ID查;", EnumErrorCode.EC_NoError }, //
				{ 1, 3, 1, 1, 1, 1, 10, "case10:根据所有条件查询", EnumErrorCode.EC_NoError }, //
				{ BaseWarehousingTest.INVALID_VALUE, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, 10, "CASE11:根据不存在的ID查询：", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseWarehousingTest.INVALID_VALUE, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, 10, "case12:根据不存在的仓库ID查：", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseWarehousingTest.INVALID_VALUE, BaseAction.INVALID_ID, 1, 10, "case13：根据不存在的业务员ID查：", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseWarehousingTest.INVALID_VALUE, 1, 10, "CASE14:根据不存在的采购订单查", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, 1, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, 10, "CASE15:根据供应商ID查", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, 1, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 2, 1, 10, "CASE16:根据供应商ID和采购订单查 ", EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, BaseWarehousingTest.INVALID_VALUE, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 1, 10, "CASE17:根据不存在的供应商ID查", EnumErrorCode.EC_NoError }, //
		};
	}

	public EnumErrorCode retrieveN(int iID, int iProviderID, int iwarehouseID, int istaffID, int ipurchasingOrderID, int iPageIndex, int iPageSize) {
		Warehousing warehousingSheet = new Warehousing();
		warehousingSheet.setID(iID);
		warehousingSheet.setProviderID(iProviderID);
		warehousingSheet.setWarehouseID(iwarehouseID);
		warehousingSheet.setStaffID(istaffID);
		warehousingSheet.setPurchasingOrderID(ipurchasingOrderID);
		warehousingSheet.setPageIndex(iPageIndex);
		warehousingSheet.setPageSize(iPageSize);
		//
		Map<String, Object> params = warehousingSheet.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousingSheet);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> ls = warehousingMapper.retrieveN(params);
		//
		if (ls.size() > 0) {
			for (BaseModel baseModel : ls) {
				System.out.println(baseModel);
				Warehousing w = (Warehousing) baseModel;

				if (iID != -1) {
					Assert.assertTrue(w.getID() == iID);
				} else if (iProviderID != -1) {
					Assert.assertTrue(w.getProviderID() == iProviderID);
				} else if (iwarehouseID != -1) {
					Assert.assertTrue(w.getWarehouseID() == iwarehouseID);
				} else if (istaffID != -1) {
					Assert.assertTrue(w.getStaffID() == istaffID);
				} else if (ipurchasingOrderID != -1) {
					Assert.assertTrue(w.getPurchasingOrderID() == ipurchasingOrderID);
				}
			}
		}

		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Test(dataProvider = "getRetrieveNData")
	public void retrieveNTest(int iID, int iProviderID, int iwarehouseID, int istaffID, int ipurchasingOrderID, int iPageIndex, int iPageSize, String message, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(message);
		Assert.assertEquals(retrieveN(iID, iProviderID, iwarehouseID, istaffID, ipurchasingOrderID, iPageIndex, iPageSize), ec);
	}

	@DataProvider(name = "getOneData")
	public Object[][] getOneData() {
		return new Object[][] { { 1, EnumErrorCode.EC_NoError }, { 2, EnumErrorCode.EC_NoError }, { BaseWarehousingTest.INVALID_VALUE, EnumErrorCode.EC_NoError }, { 11, EnumErrorCode.EC_NoError } };
	}

	public EnumErrorCode retrieve1Ex(int id) {
		Warehousing warehousingSheet = new Warehousing();
		warehousingSheet.setID(id);
		//
		Map<String, Object> params = warehousingSheet.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, warehousingSheet);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = (List<List<BaseModel>>) warehousingMapper.retrieve1Ex(params);
		// 结果验证：验证从表数据
		if (bmList.get(0).size() > 0 && bmList != null) {
			List<BaseModel> whcList = bmList.get(1);
			if (whcList.size() != 0) {
				for (BaseModel bm : whcList) {
					WarehousingCommodity whc = (WarehousingCommodity) bm;
					assertTrue(whc.getWarehousingID() == warehousingSheet.getID());
				}
			}
		}
		//
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Test(dataProvider = "getOneData")
	public void retrieve1ExTest(int id, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();

		Assert.assertEquals(retrieve1Ex(id), ec);
	}

	@Test
	public void retrieve1ExTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 1: R1入库单时，如果有采购订单与之相关联，则应返回采购订单的单号");
		// 创建一个入库单
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		PurchasingOrder pOrder = new PurchasingOrder();
		pOrder.setID(1);// 已审核的采购订单
		warehousing.setPurchasingOrderID(pOrder.getID());
		// 检验字段的合法性
		String error = warehousing.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), "创建入库单时，字段不合法");
		//
		Map<String, Object> params2 = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wsCreate = (Warehousing) warehousingMapper.create(params2);
		Assert.assertTrue(wsCreate != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(wsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 检验字段的合法性
		error = wsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error.equals(""), "创建采购订单时，DB字段不合法");
		Map<String, Object> paramsR1 = wsCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, wsCreate);
		List<List<BaseModel>> bmList = warehousingMapper.retrieve1Ex(paramsR1);
		Warehousing WarehousingR1 = (Warehousing) bmList.get(0).get(0);
		// 判断R1入库单时，是否返回采购订单的单号
		Map<String, Object> paramsPOrderR1 = pOrder.getRetrieve1Param(BaseBO.INVALID_CASE_ID, pOrder);
		List<List<BaseModel>> bmPOrderList = purchasingOrderMapper.retrieve1Ex(paramsPOrderR1);
		PurchasingOrder purchasingOrderR1 = (PurchasingOrder) bmPOrderList.get(0).get(0);
		Assert.assertTrue(WarehousingR1.getPurchasingOrderSN().equals(purchasingOrderR1.getSn()), "R1入库单时，没有返回采购订单的单号");
		// 删除创建出来的warehousing和purchasingOrder测试对象
		BaseWarehousingTest.deleteViaMapper(wsCreate);
	}

	@DataProvider(name = "getOneDataByOrderID")
	public Object[][] getOneDataByOrderID() {
		return new Object[][] { //
				{ 1, "CASE 1", EnumErrorCode.EC_NoError }, //
				// PurchasingOrderID为-1则查询全部
				{ -1, "CASE 2", EnumErrorCode.EC_NoError }, //
				{ 9999, "CASE 3", EnumErrorCode.EC_NoError }//
		};

	}

	public EnumErrorCode retrieve1OrderID(int purchasingOrderID) {
		Warehousing warehousingSheet = new Warehousing();
		warehousingSheet.setPurchasingOrderID(purchasingOrderID);
		//
		Map<String, Object> params = warehousingSheet.getRetrieveNParam(BaseBO.CASE_RetrieveNWarhousingByOrderID, warehousingSheet);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing bm = (Warehousing) warehousingMapper.retrieve1OrderID(params);
		//
		System.out.println(bm);
		//
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Test(dataProvider = "getOneDataByOrderID")
	public void retrieve1OrderIDTest(int purchasingOrderID, String message, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(message);
		Assert.assertEquals(retrieve1Ex(purchasingOrderID), ec);
	}

	public void approveTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case1:正常审核");

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing warehousingCreate = BaseWarehousingTest.createViaMapper(warehousing);
		BaseWarehousingTest.createWarehousingCommodityViaMapper(warehousing.getID(), 1, 100, 1, 11.1D, 1110.0D, 365);
		// 审核入库单
		warehousingCreate.setApproverID(STAFF_ID1);
		BaseWarehousingTest.approveViaMapper(warehousingCreate);
	}

	public void approveTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case2:审核ID不存在");

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		warehousing.setID(Shared.BIG_ID);
		// 审核入库单
		BaseWarehousingTest.approveViaMapper(warehousing);
	}

	public void approveTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case3:审核ID已审核");

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing warehousingCreate = BaseWarehousingTest.createViaMapper(warehousing);
		BaseWarehousingTest.createWarehousingCommodityViaMapper(warehousing.getID(), 1, 100, 1, 11.1D, 1110.0D, 365);
		// 审核入库单
		warehousingCreate.setApproverID(STAFF_ID1);
		BaseWarehousingTest.approveViaMapper(warehousingCreate);
		// 再次审核
		Map<String, Object> params = warehousing.getUpdateParam(BaseBO.CASE_ApproveWarhousing, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing warehousingApprove = (Warehousing) warehousingMapper.approveEx(params);
		assertTrue(warehousingApprove == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	public void approveTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case4:审核没有入库商品的入库单");

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing warehousingCreate = BaseWarehousingTest.createViaMapper(warehousing);
		// 审核入库单
		warehousingCreate.setApproverID(STAFF_ID1);
		Map<String, Object> params = warehousingCreate.getUpdateParam(BaseBO.CASE_ApproveWarhousing, warehousingCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing warehousingApprove = (Warehousing) warehousingMapper.approveEx(params);
		assertTrue(warehousingApprove == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 给没有入库商品的入库单关联入库商品
		BaseWarehousingTest.createWarehousingCommodityViaMapper(warehousing.getID(), 1, 100, 1, 11.1D, 1110.0D, 365);
	}

	@Test
	public void approveTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("审核入库单，返回关联的采购单号，放在purchasingOrderSN");
		// 创建测试数据
		Warehousing ws = BaseWarehousingTest.DataInput.getWarehousing();
		ws.setPurchasingOrderID(1);
		Warehousing warehousing = BaseWarehousingTest.createViaMapper(ws);
		BaseWarehousingTest.createWarehousingCommodityViaMapper(warehousing.getID(), 1, 100, 1, 11.1D, 111.0D, 365);
		warehousing.setApproverID(STAFF_ID3);
		Warehousing WarehousingApprove = BaseWarehousingTest.approveViaMapper(warehousing);
		//
		PurchasingOrder pOrder = new PurchasingOrder();
		pOrder.setID(WarehousingApprove.getPurchasingOrderID());
		Map<String, Object> paramsPOrderR1 = pOrder.getRetrieve1Param(BaseBO.INVALID_CASE_ID, pOrder);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmPOrderList = purchasingOrderMapper.retrieve1Ex(paramsPOrderR1);
		Assert.assertTrue(bmPOrderList != null && EnumErrorCode.values()[Integer.parseInt(paramsPOrderR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsPOrderR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		PurchasingOrder purchasingOrderR1 = (PurchasingOrder) bmPOrderList.get(0).get(0);
		Assert.assertTrue(purchasingOrderR1 != null, "返回的对象为null");
		Assert.assertTrue(WarehousingApprove.getPurchasingOrderSN().equals(purchasingOrderR1.getSn()), "审核入库单时，没有返回采购订单的单号");
	}

	@DataProvider(name = "getRetrieveNByFieldsData")
	public Object[][] getRetrieveNByFieldsData() {
		return new Object[][] { //
				// queryKeyword,shopID,staffID,status,providerID,date1,date2
				{ "RK20190605", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE1:根据最小长度(10)的入库单单号模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "A可乐18", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE2:根据商品名称模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "默认", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE3:根据供应商名称模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "默认", BaseAction.INVALID_ID, BaseAction.INVALID_ID, Warehousing.EnumStatusWarehousing.ESW_Approved.getIndex(), BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE4:在限定状态下根据供应商名称模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "默认", BaseAction.INVALID_ID, 4, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE5:在限定经办人ID下根据供应商名称模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "RK20190605", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 2, "2010/01/01 00:00:00", "2020/01/01 00:00:00", " CASE6:在限定供应商ID下根据入库单单号模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "安徽", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2017/10/8 1:01:01", "2020/01/01 00:00:00", "CASE7:在限定创建时间段下根据供应商名称查模糊询入库单", EnumErrorCode.EC_NoError }, //
				{ "", BaseAction.INVALID_ID, BaseAction.INVALID_ID, Warehousing.EnumStatusWarehousing.ESW_Approved.getIndex(), BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", " CASE8:根据状态的进行搜索（不传入queryKeyword）", EnumErrorCode.EC_NoError }, //
				{ "", BaseAction.INVALID_ID, 4, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE9:根据经办人ID的进行搜索（不传入queryKeyword)", EnumErrorCode.EC_NoError }, //
				{ "", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, 10, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE10:根据供应商ID的进行搜索（不传入queryKeyword）", EnumErrorCode.EC_NoError }, //
				{ "", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2019/1/8 1:01:00", "2020/01/01 00:00:00", "CASE11:根据传入创建时间段的进行搜索（不传入queryKeyword）", EnumErrorCode.EC_NoError }, //
				{ "", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE612:传值为空则查询全部", EnumErrorCode.EC_NoError }, //
				{ "RK2019060", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE13:根据小于10位的入库单单号模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "RK20190605123451234512345", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE14:根据大于20位的入库单单号模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "RK201906051234512345", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE15:根据20位的入库单单号模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "CG2019", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE16:根据小于10位的入库单单号模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "CG201906040007", BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE17:根据正常的入库单单号模糊查询入库单", EnumErrorCode.EC_NoError }, //
				{ "", Shared.DEFAULT_Shop_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, BaseAction.INVALID_ID, "2010/01/01 00:00:00", "2020/01/01 00:00:00", "CASE18:根据门店ID模糊查询入库单", EnumErrorCode.EC_NoError } //

		};

	}

	public EnumErrorCode retrieveNByFields(String queryKeyword, int shopID, int staffID, int status, int providerID, String date1, String date2, String message) throws Exception {
		DateFormat df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default4);
		Warehousing warehousing = new Warehousing();
		warehousing.setQueryKeyword(queryKeyword);
		warehousing.setShopID(shopID);
		warehousing.setStaffID(staffID);
		warehousing.setStatus(status);
		warehousing.setProviderID(providerID);
		warehousing.setDate1(df.parse(date1));
		warehousing.setDate2(df.parse(date2));
		//
		Map<String, Object> params = warehousing.getRetrieveNParam(BaseBO.CASE_RetrieveNWarhousingByFields, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> ls = warehousingMapper.retrieveNByFields(params);
		//
		if (ls.size() > 0) {
			for (BaseModel baseModel : ls) {
				System.out.println(baseModel);
			}
			//
			assertTrue(ls != null && ls.size() > 0, "入库单RetrieveNByFields失败！");
		} else {
			assertTrue(ls.size() == 0, "入库单RetrieveNByFields失败！");
		}

		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Test(dataProvider = "getRetrieveNByFieldsData")
	public void retrieveNByFieldsTest(String queryKeyword, int shopID, int staffID, int status, int providerID, String date1, String date2, String message, EnumErrorCode ec) throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(message);
		Assert.assertEquals(retrieveNByFields(queryKeyword, shopID, staffID, status, providerID, date1, date2, message), ec);
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case1:正常删除入库单");
		// 创建入库单
		Warehousing ws = BaseWarehousingTest.DataInput.getWarehousing();
		// 检查字段合法性
		String errorMessage = ws.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMessage, "");
		//
		Map<String, Object> paramsForCreate = ws.getCreateParam(BaseBO.INVALID_CASE_ID, ws);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wcCreate = (Warehousing) warehousingMapper.create(paramsForCreate); // ...
		//
		ws.setIgnoreIDInComparision(true);
		if (ws.compareTo(wcCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		assertTrue(wcCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 检查字段合法性
		errorMessage = wcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMessage, "");
		//
		Map<String, Object> paramsForDelete = ws.getDeleteParam(BaseBO.INVALID_CASE_ID, wcCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.delete(paramsForDelete);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		Map<String, Object> paramsForRetrieve1 = ws.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, wcCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = (List<List<BaseModel>>) warehousingMapper.retrieve1Ex(paramsForRetrieve1);
		assertTrue(bmList.get(0).size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case2:入库单已审核，不能删除");
		Warehousing ws2 = new Warehousing();
		ws2.setID(4);
		Map<String, Object> paramsForDelete2 = ws2.getDeleteParam(BaseBO.INVALID_CASE_ID, ws2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.delete(paramsForDelete2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForDelete2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		Map<String, Object> paramsForRetrieve2 = ws2.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, ws2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = (List<List<BaseModel>>) warehousingMapper.retrieve1Ex(paramsForRetrieve2);
		assertTrue(bmList.get(0).size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieve2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case3:入库单不存在,删除失败！");
		Warehousing ws3 = new Warehousing();
		ws3.setID(99999);
		Map<String, Object> paramsForDelete3 = ws3.getDeleteParam(BaseBO.INVALID_CASE_ID, ws3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.delete(paramsForDelete3);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForDelete3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println(paramsForDelete3);
		// 结果验证
		Map<String, Object> paramsForRetrieve3 = ws3.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, ws3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = (List<List<BaseModel>>) warehousingMapper.retrieve1Ex(paramsForRetrieve3);
		assertTrue(bmList.get(0).size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieve3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void updateTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建入库单
		Shared.caseLog("Case1:修改未审核入库单");
		Warehousing ws = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing wcCreate = BaseWarehousingTest.createViaMapper(ws);
		BaseWarehousingTest.createWarehousingCommodityViaMapper(wcCreate.getID(), 1, 100, 1, 11.1D, 1110.0D, 365);
		//
		Provider provider = createProvider();
		//
		wcCreate.setProviderID(provider.getID());
		wcCreate.setWarehouseID(1);
		// 检验字段合法性
		String errorMessage = wcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMessage, "");
		//
		Map<String, Object> paramsForUpdate = ws.getUpdateParam(BaseBO.INVALID_CASE_ID, wcCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wcUpdate = (Warehousing) warehousingMapper.update(paramsForUpdate);
		// 结果验证
		ws.setIgnoreIDInComparision(true);
		if (wcCreate.compareTo(wcUpdate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(wcUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 检验字段合法性
		errorMessage = wcUpdate.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMessage, "");

		// 删除创建出来的测试对象
		BaseWarehousingTest.deleteViaMapper(wcUpdate);
		deleteProvider(provider);
	}

	@Test
	public void updateTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:修改已审核入库单");
		Provider provider = createProvider();
		//
		Warehousing wc2 = new Warehousing();
		wc2.setID(4);
		wc2.setWarehouseID(1);
		wc2.setProviderID(provider.getID());
		Map<String, Object> paramsForUpdate2 = wc2.getUpdateParam(BaseBO.INVALID_CASE_ID, wc2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wcUpdate2 = (Warehousing) warehousingMapper.update(paramsForUpdate2);
		//
		assertTrue(wcUpdate2 == null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForUpdate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除创建出来的测试对象
		deleteProvider(provider);
	}

	@Test
	public void updateTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:修改一个仓库不存在的入库单");
		// 创建入库单
		Warehousing ws = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing wcCreate = BaseWarehousingTest.createViaMapper(ws);
		//
		Provider provider = createProvider();
		//
		Warehousing wc3 = new Warehousing();
		wc3.setID(wcCreate.getID());
		wc3.setWarehouseID(BaseAction.INVALID_ID);
		wc3.setProviderID(provider.getID());
		Map<String, Object> paramsForUpdate4 = wc3.getUpdateParam(BaseBO.INVALID_CASE_ID, wc3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wcUpdate4 = (Warehousing) warehousingMapper.update(paramsForUpdate4);
		//
		assertTrue(wcUpdate4 == null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除创建出来的测试对象
		BaseWarehousingTest.deleteViaMapper(wcCreate);
		deleteProvider(provider);
	}

	@Test
	public void updateTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:修改一个供应商不存在的入库单");
		// 创建入库单
		Warehousing ws = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing wcCreate = BaseWarehousingTest.createViaMapper(ws);
		//
		Warehousing wc3 = new Warehousing();
		wc3.setID(wcCreate.getID());
		wc3.setWarehouseID(1);
		wc3.setProviderID(BaseAction.INVALID_ID);
		Map<String, Object> paramsForUpdate4 = wc3.getUpdateParam(BaseBO.INVALID_CASE_ID, wc3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wcUpdate4 = (Warehousing) warehousingMapper.update(paramsForUpdate4);
		//
		assertTrue(wcUpdate4 == null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除创建出来的测试对象
		BaseWarehousingTest.deleteViaMapper(wcCreate);
	}

	@Test
	public void updateTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:修改一个入库单ID不存在的入库单");
		//
		Provider provider = createProvider();
		//
		Warehousing wc3 = new Warehousing();
		wc3.setID(BaseAction.INVALID_ID);
		wc3.setWarehouseID(1);
		wc3.setProviderID(provider.getID());
		Map<String, Object> paramsForUpdate4 = wc3.getUpdateParam(BaseBO.INVALID_CASE_ID, wc3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wcUpdate4 = (Warehousing) warehousingMapper.update(paramsForUpdate4);
		//
		assertTrue(wcUpdate4 == null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除创建出来的测试对象
		deleteProvider(provider);
	}

	@Test
	public void updateTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建入库单
		Shared.caseLog("Case6:修改没有入库商品的入库单");
		Warehousing ws = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing wcCreate = BaseWarehousingTest.createViaMapper(ws);
		//
		Provider provider = createProvider();
		//
		wcCreate.setProviderID(provider.getID());
		wcCreate.setWarehouseID(1);
		// 检验字段合法性
		String errorMessage = wcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMessage, "");
		//
		Map<String, Object> paramsForUpdate = ws.getUpdateParam(BaseBO.INVALID_CASE_ID, wcCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wcUpdate = (Warehousing) warehousingMapper.update(paramsForUpdate);
		// 结果验证
		Assert.assertTrue(wcUpdate == null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除创建出来的测试对象
		BaseWarehousingTest.deleteViaMapper(wcCreate);
		deleteProvider(provider);
	}

	private Provider createProvider() throws CloneNotSupportedException, InterruptedException {
		// 创建一个供应商
		Provider p = BaseTestNGSpringContextTest.DataInput.getProvider();
		Map<String, Object> params = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerCreate = (Provider) providerMapper.create(params); // ...
		p.setIgnoreIDInComparision(true);
		if (p.compareTo(providerCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(providerCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		return providerCreate;
	}

	private void deleteProvider(Provider p) {
		// 没有供应商商品存在后删除成功
		Map<String, Object> paramsForDelete1 = p.getDeleteParam(BaseBO.INVALID_CASE_ID, p);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.delete(paramsForDelete1); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		//
		// Retrieve1确认该供应商已经被删除
		Map<String, Object> paramsForRetrieve1Provider = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel bmForRetrieve1Provider = providerMapper.retrieve1(paramsForRetrieve1Provider);
		Assert.assertTrue(bmForRetrieve1Provider == null && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve1Provider.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:根据输入_特殊字符商品名称查询入库单");
		// 创建商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		comm1.setName("入库营养快_kahd" + System.currentTimeMillis());
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, comm1);
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		// 创建入库单和入库单从表
		Warehousing warehousing1 = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing warehousing2 = BaseWarehousingTest.createViaMapper(warehousing1);

		WarehousingCommodity wc1 = DataInput.getWarehousingCommodity();
		wc1.setCommodityID(commCreated.getID());
		wc1.setBarcodeID(barcodes.getID());
		wc1.setWarehousingID(warehousing2.getID());
		wc1.setCommodityName(commCreated.getName());
		wc1.setNoSalable(wc1.getNO());
		Map<String, Object> params = wc1.getCreateParam(BaseBO.INVALID_CASE_ID, wc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wcCreate = (WarehousingCommodity) warehousingCommodityMapper.create(params);
		Assert.assertTrue(wcCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		wc1.setIgnoreIDInComparision(true);
		if (wc1.compareTo(wcCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		String errorMsg = wcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");

		// 模糊查询入库订单
		Warehousing warehousing3 = new Warehousing();
		// warehousing3.setStaffID(1);
		warehousing3.setQueryKeyword("_");

		Map<String, Object> retrieveNParams1 = warehousing3.getRetrieveNParam(BaseBO.CASE_RetrieveNWarhousingByFields, warehousing3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList1 = warehousingMapper.retrieveNByFields(retrieveNParams1);
		for (BaseModel bm : poList1) {
			Warehousing warehousing4 = (Warehousing) bm;
			//
			// 查询当前入库单的相关入库单商品表
			WarehousingCommodity wc2 = new WarehousingCommodity();
			wc2.setWarehousingID(warehousing4.getID());
			Map<String, Object> param = wc2.getRetrieveNParam(BaseBO.INVALID_CASE_ID, wc2);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> ls = warehousingCommodityMapper.retrieveN(param);
			Assert.assertTrue(ls.size() > 0 && EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
			//
			boolean isTrue = false;
			for (BaseModel bm1 : ls) {
				wc2 = (WarehousingCommodity) bm1;
				if (wc2.getCommodityName().contains(warehousing3.getQueryKeyword())) {
					isTrue = true;
					System.out.println("搜索到的结果的字段的值" + wc2.getCommodityName() + "包含字符串" + warehousing3.getQueryKeyword());
				}
			}
			Assert.assertTrue(isTrue == true);
		}
		Assert.assertTrue(poList1 != null && poList1.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除入库单商品
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wcCreate);
		BaseWarehousingTest.deleteViaMapper(warehousing2);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}
}
