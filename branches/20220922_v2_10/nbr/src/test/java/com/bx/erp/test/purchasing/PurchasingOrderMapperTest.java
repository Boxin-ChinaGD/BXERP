package com.bx.erp.test.purchasing;

import static org.testng.Assert.assertEquals;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrder.EnumStatusPurchasingOrder;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BasePurchasingOrderTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class PurchasingOrderMapperTest extends BaseMapperTest {

	private SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
	private static final int INVALID_ID = -100000;
	private static final int STATUS1_ID = 7;
	private static final int STATUS2_ID = 8;
	private static final int STATUS3_ID = 9;

	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	@Test
	public void createTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 1: 创建一个采购单，PurchasingPlanTableID > 0");
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrder = BasePurchasingOrderTest.purchasingOrderCreate(po);

		// 删除测试数据
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(purchasingOrder);
	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 2: 创建一个采购单，StaffID 为一个不存在的ID，返回错误码7");

		PurchasingOrder po4 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po4.setStaffID(INVALID_ID);
		Map<String, Object> params4 = po4.getCreateParam(BaseBO.INVALID_CASE_ID, po4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo4 = (PurchasingOrder) purchasingOrderMapper.create(params4);
		Assert.assertTrue(createPo4 == null && EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue("当前帐户不允许创建采购单".equals(params4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 3: 创建一个采购单，ProviderID 为一个不存在的ID，返回错误码3");
		PurchasingOrder po5 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po5.setProviderID(INVALID_ID);
		Map<String, Object> params5 = po5.getCreateParam(BaseBO.INVALID_CASE_ID, po5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo5 = (PurchasingOrder) purchasingOrderMapper.create(params5);
		Assert.assertTrue(createPo5 == null && EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue("该供应商不存在，请重新选择供应商".equals(params5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		/*
		 * 前端会进行校验，此段代码运行会报错，一般remark也不会这么长 System.out.
		 * println("------------------------Case 6: case6：@sRemark采购总结字段 超过数据库字符限制()" );
		 * PurchasingOrder po6 = DataInput.getPurchasingOrder(); StringBuffer sBuffer =
		 * new StringBuffer(); for(int i=1;i<200;i++) { sBuffer.append("哈哈"); }
		 * po6.setRemark(sBuffer.toString());
		 * 
		 * Map<String, Object> params6 = po1.getCreateParam(BaseBO.INVALID_CASE_ID,
		 * po6); PurchasingOrder createPo6 = (PurchasingOrder)mapper.create(params6);
		 * assertTrue(createPo6 != null &&
		 * EnumErrorCode.values()[Integer.parseInt(params6.get(BaseAction.
		 * SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建");
		 */
	}

	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4: 创建一个采购单，StaffID 为一个离职员工的ID，返回错误码7");
		// 创建员工
		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffToDelete = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		// 让员工离职
		BaseStaffTest.deleteStaff(staffToDelete, EnumErrorCode.EC_NoError, staffMapper);
		//
		PurchasingOrder po4 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po4.setStaffID(staffToDelete.getID());
		Map<String, Object> params4 = po4.getCreateParam(BaseBO.INVALID_CASE_ID, po4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo4 = (PurchasingOrder) purchasingOrderMapper.create(params4);
		Assert.assertTrue(createPo4 == null && EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue("当前帐户不允许创建采购单".equals(params4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
	
	@Test
	public void createTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5: 创建一个采购单，门店不存在，返回错误码7");
		//
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po.setShopID(BaseAction.INVALID_ID);
		Map<String, Object> params = po.getCreateParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo4 = (PurchasingOrder) purchasingOrderMapper.create(params);
		Assert.assertTrue(createPo4 == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue("该门店不存在，请重新选择门店".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieve1ExTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建一个采购订单
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);

		// 查询一个采购订单
		Shared.caseLog("Case 1: 查询一张采购单");
		Map<String, Object> retrieve1Params = createPo.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> poList = (List<List<BaseModel>>) purchasingOrderMapper.retrieve1Ex(retrieve1Params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, retrieve1Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (poList.size() == 2 && poList.get(0).size() > 0) {
			PurchasingOrder r1Po = (PurchasingOrder) poList.get(0).get(0);
			createPo.setIgnoreIDInComparision(true);
			if (po.compareTo(r1Po) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			List<BaseModel> r1Po1 = (List<BaseModel>) poList.get(1);
			if (r1Po1.size() != 0) {
				for (BaseModel bm : r1Po1) {
					PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) bm;
					Assert.assertTrue(purchasingOrderCommodity.getPurchasingOrderID() == r1Po.getID());
				}
			}
			Assert.assertTrue(r1Po.getStatus() == EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
			//
			// 删除测试数据
			BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(createPo);
		}

	}

	@Test
	public void retrieve1ExTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建一个采购订单
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		// 删除一个采购订单
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(createPo);
		//
		Shared.caseLog("Case 2: 查询一张已删除的采购单");
		Map<String, Object> retrieve1Params = createPo.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> poList = (List<List<BaseModel>>) purchasingOrderMapper.retrieve1Ex(retrieve1Params);
		List<BaseModel> r1Po = (List<BaseModel>) poList.get(0);
		Assert.assertTrue(r1Po.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieve1Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieve1ExTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 3: 查询一张不存在的采购单");
		//
		PurchasingOrder po = new PurchasingOrder();
		po.setID(INVALID_ID);
		Map<String, Object> retrieve1Params2 = po.getRetrieve1ParamEx(INVALID_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> poList = (List<List<BaseModel>>) purchasingOrderMapper.retrieve1Ex(retrieve1Params2);
		List<BaseModel> r1Po = (List<BaseModel>) poList.get(0);
		Assert.assertTrue(r1Po.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieve1Params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieve1Params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po);

		Shared.caseLog("CASE1:查询状态为0的采购单");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setStatus(EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
		//
		Map<String, Object> params = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> reteieveNPo = (List<BaseModel>) purchasingOrderMapper.retrieveN(params);
		//
		Assert.assertTrue(reteieveNPo != null && reteieveNPo.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		boolean isTrue = false;
		for (BaseModel b : reteieveNPo) {
			PurchasingOrder po1 = (PurchasingOrder) b;
			Assert.assertTrue(po1.getStatus() == EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
			//
			if (po1.getID() == purchasingOrderCreate.getID()) {
				isTrue = true;
			}
			//
			String error = po1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
		Assert.assertTrue(isTrue == true);

		// 删除测试数据
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(purchasingOrderCreate);
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(purchasingOrderCreate.getID(), 1, 100, 1, 11.1D);
		//
		BasePurchasingOrderTest.purchasingOrderApprover(purchasingOrderCreate);

		Shared.caseLog("CASE2:查询状态为1的采购单");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setStatus(EnumStatusPurchasingOrder.ESPO_Approved.getIndex());
		//
		Map<String, Object> params = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> reteieveNPo = (List<BaseModel>) purchasingOrderMapper.retrieveN(params);
		//
		Assert.assertTrue(reteieveNPo != null && reteieveNPo.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		boolean isTrue = false;
		for (BaseModel b : reteieveNPo) {
			PurchasingOrder po1 = (PurchasingOrder) b;
			Assert.assertTrue(po1.getStatus() == EnumStatusPurchasingOrder.ESPO_Approved.getIndex());
			//
			if (po1.getID() == purchasingOrderCreate.getID()) {
				isTrue = true;
			}
			//
			String error = po1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
		Assert.assertTrue(isTrue == true);
	}

	@Test
	public void retrieveNTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(purchasingOrderCreate.getID(), 1, 100, 1, 11.1D);
		//
		BasePurchasingOrderTest.purchasingOrderApprover(purchasingOrderCreate);
		//
		BasePurchasingOrderTest.purchasingOrderUpdateStatusOneToTwo(purchasingOrderCreate);

		Shared.caseLog("CASE3:查询状态为2的采购单");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setStatus(EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex());
		//
		Map<String, Object> params = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> reteieveNPo = (List<BaseModel>) purchasingOrderMapper.retrieveN(params);
		//
		Assert.assertTrue(reteieveNPo != null && reteieveNPo.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		boolean isTrue = false;
		for (BaseModel b : reteieveNPo) {
			PurchasingOrder po1 = (PurchasingOrder) b;
			Assert.assertTrue(po1.getStatus() == EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex());
			//
			if (po1.getID() == purchasingOrderCreate.getID()) {
				isTrue = true;
			}
			//
			String error = po1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
		Assert.assertTrue(isTrue == true);
	}

	@Test
	public void retrieveNTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(purchasingOrderCreate.getID(), 1, 100, 1, 11.1D);
		//
		BasePurchasingOrderTest.purchasingOrderApprover(purchasingOrderCreate);
		//
		BasePurchasingOrderTest.purchasingOrderUpdateStatusOneToTwo(purchasingOrderCreate);
		//
		BasePurchasingOrderTest.purchasingOrderUpdateStatusTwoToThree(purchasingOrderCreate);

		Shared.caseLog("CASE4:查询状态为3的采购单");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setStatus(EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex());
		//
		Map<String, Object> params = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> reteieveNPo = (List<BaseModel>) purchasingOrderMapper.retrieveN(params);
		//
		Assert.assertTrue(reteieveNPo != null && reteieveNPo.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		boolean isTrue = false;
		for (BaseModel b : reteieveNPo) {
			PurchasingOrder po1 = (PurchasingOrder) b;
			Assert.assertTrue(po1.getStatus() == EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex());
			//
			if (po1.getID() == purchasingOrderCreate.getID()) {
				isTrue = true;
			}
			//
			String error = po1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
		Assert.assertTrue(isTrue == true);
	}

	@Test
	public void retrieveNTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po);

		Shared.caseLog("CASE5:查询状态不为4的采购单，传入-1");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setStatus(BaseAction.INVALID_STATUS);
		//
		Map<String, Object> params = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> reteieveNPo = (List<BaseModel>) purchasingOrderMapper.retrieveN(params);
		//
		Assert.assertTrue(reteieveNPo != null && reteieveNPo.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		boolean isTrue = false;
		for (BaseModel b : reteieveNPo) {
			PurchasingOrder po1 = (PurchasingOrder) b;
			Assert.assertTrue(po1.getStatus() < EnumStatusPurchasingOrder.ESPO_Deleted.getIndex() && po1.getStatus() > BaseAction.INVALID_STATUS);
			//
			if (po1.getID() == purchasingOrderCreate.getID()) {
				isTrue = true;
			}
			//
			String error = po1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
		Assert.assertTrue(isTrue == true);

		// 删除测试数据
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(purchasingOrderCreate);
	}

	@Test
	public void retrieveNTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6:查询状态为4的采购单，返回错误码7");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setStatus(EnumStatusPurchasingOrder.ESPO_Deleted.getIndex());
		//
		Map<String, Object> params = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> reteieveNPo = (List<BaseModel>) purchasingOrderMapper.retrieveN(params);
		//
		Assert.assertTrue(reteieveNPo.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7:查询未定义的采购订单状态，返回错误码7");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setStatus(9999);
		//
		Map<String, Object> params = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> reteieveNPo = (List<BaseModel>) purchasingOrderMapper.retrieveN(params);
		//
		Assert.assertTrue(reteieveNPo.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8: 根据QueryKeyword传的值进行模糊查询（根据商品名称）");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setStatus(BaseAction.INVALID_STATUS);
		purchasingOrder.setQueryKeyword("可乐");
		Map<String, Object> retrieveNParams1 = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams1);
		//
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList) {
			PurchasingOrder po = (PurchasingOrder) bm;
			// 查询当前采购订单的相关采购订单商品表
			PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
			poc.setPurchasingOrderID(po.getID());
			Map<String, Object> params = poc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, poc);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> ls = purchasingOrderCommodityMapper.retrieveN(params);
			Assert.assertTrue(ls.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
			//
			boolean isTrue = false;
			for (BaseModel bm1 : ls) {
				poc = (PurchasingOrderCommodity) bm1;
				if (poc.getCommodityName().contains(purchasingOrder.getQueryKeyword())) {
					isTrue = true;
					System.out.println("搜索到的结果的字段的值" + poc.getCommodityName() + "包含字符串" + purchasingOrder.getQueryKeyword());
				}
				// 字段验证
				String error = poc.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			}
			Assert.assertTrue(isTrue == true);
			// 字段验证
			String error = po.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void retrieveNTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE9: 根据QueryKeyword传的值进行模糊查询（根据供应商名称）");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setQueryKeyword("默认");
		purchasingOrder.setStatus(BaseAction.INVALID_STATUS);
		Map<String, Object> retrieveNParams = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		//
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList) {
			PurchasingOrder po = (PurchasingOrder) bm;
			Assert.assertTrue(po.getProviderName().contains(purchasingOrder.getQueryKeyword()));
			System.out.println("搜索到的结果的字段的值" + po.getProviderName() + "包含字符串" + purchasingOrder.getQueryKeyword());
			//
			String error = po.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void retrieveNTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po);

		Shared.caseLog("CASE10: 根据QueryKeyword传的值进行模糊查询（等于最小长度的采购订单单号,等于10位）");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "CG" + sdf.format(new Date()).substring(0, 8);
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setQueryKeyword(SN);
		purchasingOrder.setStatus(BaseAction.INVALID_STATUS);
		Map<String, Object> retrieveNParams = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		//
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		boolean isTrue = false;
		for (BaseModel bm : poList) {
			PurchasingOrder po1 = (PurchasingOrder) bm;
			if (po1.getID() == purchasingOrderCreate.getID()) {
				isTrue = true;
				break;
			}
			//
			String error = po1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
		Assert.assertTrue(isTrue == true);

		// 删除测试数据
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(purchasingOrderCreate);
	}

	@Test
	public void retrieveNTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11: 根据QueryKeyword传的值进行模糊查询（等于最小长度的采购订单单号匹配或者商品名称匹配或者供应商名称匹配的可以查询到,等于10位）");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setQueryKeyword("CG20190604");
		purchasingOrder.setStatus(BaseAction.INVALID_STATUS);
		Map<String, Object> retrieveNParams = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		//
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (BaseModel bm : poList) {
			PurchasingOrder po1 = (PurchasingOrder) bm;
			String error = po1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void retrieveNTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12: 根据QueryKeyword传的值进行模糊查询（无匹配项）");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setQueryKeyword("-9999999999");
		Map<String, Object> retrieveNParams = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		//
		Assert.assertTrue(poList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13: 根据时间段进行查询（一天）");
		PurchasingOrder po = new PurchasingOrder();
		try {
			po.setDate1(sdf.parse("2016/12/06 00:00:00"));
			po.setDate2(sdf.parse("2016/12/06 23:59:59"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		po.setStatus(BaseAction.INVALID_STATUS);
		Map<String, Object> retrieveNParams = po.getRetrieveNParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		//
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList) {
			PurchasingOrder pOrder = (PurchasingOrder) bm;
			Assert.assertTrue(pOrder.getCreateDatetime().after(po.getDate1()) && pOrder.getCreateDatetime().before(po.getDate2()), "根据给定时间段查询采购订单错误");
			//
			String error = pOrder.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void retrieveNTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14: 根据时间段进行查询（一个星期）");
		PurchasingOrder po = new PurchasingOrder();
		try {
			po.setDate1(sdf.parse("2016/12/06 00:00:00"));
			po.setDate2(sdf.parse("2016/12/13 23:59:59"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		po.setStatus(BaseAction.INVALID_STATUS);
		Map<String, Object> retrieveNParams = po.getRetrieveNParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		//
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList) {
			PurchasingOrder pOrder = (PurchasingOrder) bm;
			Assert.assertTrue(pOrder.getCreateDatetime().after(po.getDate1()) && pOrder.getCreateDatetime().before(po.getDate2()), "根据给定时间段查询采购订单错误");
			//
			String error = pOrder.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void retrieveNTest15() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15: 根据时间段进行查询（无记录时间段）");
		PurchasingOrder po = new PurchasingOrder();
		try {
			po.setDate1(sdf.parse("1998/12/06 00:00:00"));
			po.setDate2(sdf.parse("1999/12/13 23:59:59"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		po.setStatus(BaseAction.INVALID_STATUS);
		Map<String, Object> retrieveNParams = po.getRetrieveNParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		//
		Assert.assertTrue(poList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest16() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case16: 根据时间段进行查询（有开始时间,无结束时间）");
		PurchasingOrder po = new PurchasingOrder();
		try {
			po.setDate1(sdf.parse("2016/12/07 00:00:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		po.setStatus(BaseAction.INVALID_STATUS);
		Map<String, Object> retrieveNParams8 = po.getRetrieveNParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams8);
		//
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList) {
			PurchasingOrder pOrder = (PurchasingOrder) bm;
			Assert.assertTrue(pOrder.getCreateDatetime().after(po.getDate1()), "Case18根据给定时间段查询采购订单错误");
			//
			String error = pOrder.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void retrieveNTest17() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case17: 根据时间段进行查询（有结束时间,无开始时间）");
		//
		PurchasingOrder po = new PurchasingOrder();
		po.setStatus(BaseAction.INVALID_STATUS);
		try {
			po.setDate2(sdf.parse("2019/12/07 00:00:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//
		Map<String, Object> retrieveNParams = po.getRetrieveNParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		//
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList) {
			PurchasingOrder pOrder = (PurchasingOrder) bm;
			Assert.assertTrue(pOrder.getCreateDatetime().before(po.getDate2()), "case19根据给定时间段查询采购订单错误");
			//
			String error = pOrder.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void retrieveNTest18() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case18:根据操作人的ID查询采购订单");
		PurchasingOrder po = new PurchasingOrder();
		po.setStaffID(STAFF_ID4);
		po.setStatus(BaseAction.INVALID_STATUS);
		Map<String, Object> retrieveNParams = po.getRetrieveNParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList) {
			PurchasingOrder pOrder = (PurchasingOrder) bm;
			Assert.assertTrue(pOrder.getStaffID() == po.getStaffID(), "case20根据操作人ID查询采购订单错误");
			//
			String error = pOrder.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void retrieveNTest19() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case19:根据不存在的操作人的ID查询采购订单");
		PurchasingOrder po = new PurchasingOrder();
		po.setStaffID(-999999);
		po.setStatus(BaseAction.INVALID_STATUS);
		Map<String, Object> retrieveNParams = po.getRetrieveNParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		Assert.assertTrue(poList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest20() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "CG" + sdf.format(new Date()).substring(0, 8);
		PurchasingOrder purchasingOrder = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		purchasingOrder.setStaffID(STAFF_ID4);
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(purchasingOrder);

		Shared.caseLog("Case20:根据全条件进行查询");
		purchasingOrder.setQueryKeyword(SN);
		Map<String, Object> retrieveNParams = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		//
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		boolean isTrue = false;
		for (BaseModel baseModel : poList) {
			PurchasingOrder pOrder = (PurchasingOrder) baseModel;
			if (pOrder.getID() == purchasingOrderCreate.getID()) {
				isTrue = true;
				break;
			}
			//
			String error = pOrder.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
		Assert.assertTrue(isTrue, "没有查询到新创建的采购订单:" + purchasingOrderCreate.toString());
	}

	@Test
	public void retrieveNTest21() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case21:根据空条件进行查询");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setStatus(BaseAction.INVALID_STATUS);
		Map<String, Object> retrieveNParams = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		//
		Assert.assertTrue(poList != null && poList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel baseModel : poList) {
			PurchasingOrder pOrder = (PurchasingOrder) baseModel;
			//
			String error = pOrder.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void retrieveNTest22() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE22: 根据QueryKeyword传的值进行模糊查询（根据小于最小长度的采购订单单号，小于10位）");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "CG" + sdf.format(new Date()).substring(0, 7);
		//
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		//
		purchasingOrder.setQueryKeyword(SN);
		purchasingOrder.setStatus(BaseAction.INVALID_STATUS);
		//
		Map<String, Object> retrieveNParams = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		// 结果验证
		Assert.assertTrue(poList.size() == 0, "CASE22测试失败，期望的是查询不到数据");
	}

	@Test
	public void retrieveNTest23() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE23: 根据QueryKeyword传的值进行模糊查询（根据大于最大长度的采购订单单号，大于20位）");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "CG" + sdf.format(new Date()).substring(0, 8) + "123451234512345";
		//
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		//
		purchasingOrder.setQueryKeyword(SN);
		purchasingOrder.setStatus(BaseAction.INVALID_STATUS);
		//
		Map<String, Object> retrieveNParams = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		// 结果验证
		Assert.assertTrue(poList.size() == 0, "CASE23测试失败，期望的是查询不到数据");
	}

	@Test
	public void retrieveNTest24() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE24: 根据QueryKeyword传的值进行模糊查询（根据等于最大长度的采购订单单号，等于20位）");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "CG" + sdf.format(new Date()).substring(0, 8) + "1234512345";
		//
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		//
		purchasingOrder.setQueryKeyword(SN);
		purchasingOrder.setStatus(BaseAction.INVALID_STATUS);
		//
		Map<String, Object> retrieveNParams = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList = purchasingOrderMapper.retrieveN(retrieveNParams);
		// 结果验证
		Assert.assertTrue(poList.size() >= 0, "CASE24测试失败");
	}

	@Test
	public void retrieveNTest25() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(purchasingOrderCreate.getID(), 1, 100, 1, 11.1D);
		//
		BasePurchasingOrderTest.purchasingOrderApprover(purchasingOrderCreate);
		//
		BasePurchasingOrderTest.purchasingOrderUpdateStatusOneToTwo(purchasingOrderCreate);
		//
		BasePurchasingOrderTest.purchasingOrderUpdateStatusTwoToThree(purchasingOrderCreate);
		//
		BasePurchasingOrderTest.purchasingOrderUpdateStatusThreeToThree(purchasingOrderCreate);

		Shared.caseLog("CASE25:查询状态为3的采购单");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setStatus(EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex());
		//
		Map<String, Object> params = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> reteieveNPo = (List<BaseModel>) purchasingOrderMapper.retrieveN(params);
		//
		Assert.assertTrue(reteieveNPo != null && reteieveNPo.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		boolean isTrue = false;
		for (BaseModel b : reteieveNPo) {
			PurchasingOrder po1 = (PurchasingOrder) b;
			Assert.assertTrue(po1.getStatus() == EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex());
			//
			if (po1.getID() == purchasingOrderCreate.getID()) {
				isTrue = true;
			}
			//
			String error = po1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
		Assert.assertTrue(isTrue == true);
	}

	@Test
	public void retrieveNTest26() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case26:根据输入_特殊字符商品名称查询采购订单");
		// 创建商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		comm1.setName("营养快_kahd" + System.currentTimeMillis());
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, comm1);
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		// 创建采购订单和采购订单从表
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrder = BasePurchasingOrderTest.purchasingOrderCreate(po);

		PurchasingOrderCommodity poc1 = BasePurchasingOrderTest.DataInput.getPurchasingOrderCommodity();
		poc1.setCommodityID(commCreated.getID());
		poc1.setBarcodeID(barcodes.getID());
		poc1.setPurchasingOrderID(purchasingOrder.getID());
		Map<String, Object> params = poc1.getCreateParam(BaseBO.INVALID_CASE_ID, poc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params);
		poc1.setIgnoreIDInComparision(true);
		if (poc1.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		String errorMsg = pocCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");

		// 模糊查询采购订单
		PurchasingOrder po1 = new PurchasingOrder();
		po1.setStaffID(4);
		po1.setQueryKeyword("_");

		Map<String, Object> retrieveNParams1 = po1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, po1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList1 = purchasingOrderMapper.retrieveN(retrieveNParams1);
		for (BaseModel bm : poList1) {
			PurchasingOrder po2 = (PurchasingOrder) bm;
			//
			// 查询当前采购订单的相关采购订单商品表
			PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
			poc.setPurchasingOrderID(po2.getID());
			Map<String, Object> param = poc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, poc);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> ls = purchasingOrderCommodityMapper.retrieveN(param);
			Assert.assertTrue(ls.size() > 0 && EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
			//
			for (BaseModel bm1 : ls) {
				poc = (PurchasingOrderCommodity) bm1;
				Assert.assertTrue(poc.getCommodityName().contains(po1.getQueryKeyword()), "查询商品名称没有包含_特殊字符");
			}

		}
		Assert.assertTrue(poList1 != null && poList1.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除采购订单商品
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(pocCreate);
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(purchasingOrder);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest27() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE27:查看默认返回是否降序");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		//
		Map<String, Object> params = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bmList = (List<BaseModel>) purchasingOrderMapper.retrieveN(params);
		//
		Assert.assertTrue(bmList != null && bmList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (int i = 1; i < bmList.size(); i++) {
			Assert.assertTrue(bmList.get(i - 1).getID() > bmList.get(i).getID(), "数据返回错误（非降序）");
		}
	}
	
	@Test
	public void retrieveNTest28() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po);

		Shared.caseLog("CASE28:根据门店ID查询采购单 ");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setShopID(Shared.DEFAULT_Shop_ID);
		//
		Map<String, Object> params = purchasingOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> reteieveNPo = (List<BaseModel>) purchasingOrderMapper.retrieveN(params);
		//
		Assert.assertTrue(reteieveNPo != null && reteieveNPo.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		boolean isTrue = false;
		for (BaseModel b : reteieveNPo) {
			PurchasingOrder po1 = (PurchasingOrder) b;
			Assert.assertTrue(po1.getShopID() == Shared.DEFAULT_Shop_ID);
			//
			if (po1.getID() == purchasingOrderCreate.getID()) {
				isTrue = true;
			}
			//
			String error = po1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
		Assert.assertTrue(isTrue == true);

		// 删除测试数据
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(purchasingOrderCreate);
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);

		Shared.caseLog("CASE1:删除状态为0的采购单");
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(createPo);
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);

		Map<String, Object> deleteParams1 = createPo.getDeleteParam(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder deletePo1 = (PurchasingOrder) purchasingOrderMapper.delete(deleteParams1);
		Assert.assertTrue(deletePo1 == null && EnumErrorCode.values()[Integer.parseInt(deleteParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				deleteParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Shared.caseLog("CASE2:删除已删除的的采购单");
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder retrieve1Po1 = (PurchasingOrder) purchasingOrderMapper.delete(deleteParams1);
		Assert.assertTrue(retrieve1Po1 == null && EnumErrorCode.values()[Integer.parseInt(deleteParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				deleteParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:删除状态为1的采购单，返回错误码7");
		PurchasingOrder po3 = new PurchasingOrder();
		po3.setID(STATUS1_ID);
		Map<String, Object> deleteParams3 = po3.getDeleteParam(BaseBO.INVALID_CASE_ID, po3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParams3);
		Assert.assertTrue(EnumErrorCode.EC_BusinessLogicNotDefined == EnumErrorCode.values()[Integer.parseInt(deleteParams3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
				deleteParams3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:删除状态为2的采购单，返回错误码7");
		PurchasingOrder po4 = new PurchasingOrder();
		po4.setID(STATUS2_ID);
		Map<String, Object> deleteParams4 = po4.getDeleteParam(BaseBO.INVALID_CASE_ID, po4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParams4);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteParams4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:删除状态为3的采购单，返回错误码7");
		PurchasingOrder po5 = new PurchasingOrder();
		po5.setID(STATUS3_ID);
		Map<String, Object> deleteParams5 = po5.getDeleteParam(BaseBO.INVALID_CASE_ID, po5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParams5);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteParams5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6:对不存在的采购订单做删除操作，错误码为7");
		PurchasingOrder po6 = new PurchasingOrder();
		po6.setID(0);
		Map<String, Object> deleteParams6 = po6.getDeleteParam(BaseBO.INVALID_CASE_ID, po6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParams6);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteParams6.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(purchasingOrderCreate.getID(), 1, 100, 1, 11.1D);

		// 审核采购单
		BasePurchasingOrderTest.purchasingOrderApprover(purchasingOrderCreate);

		// 添加入库单依赖
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		warehousing.setPurchasingOrderID(purchasingOrderCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params10 = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		BaseModel wsCreate = warehousingMapper.create(params10);
		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(wsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(wsCreate != null && EnumErrorCode.values()[Integer.parseInt(params10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params10.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Shared.caseLog("CASE7:采购单有依赖，不能删除 ");
		Map<String, Object> deleteParams9 = purchasingOrderCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, purchasingOrderCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder deletePo9 = (PurchasingOrder) purchasingOrderMapper.delete(deleteParams9);
		Assert.assertTrue(deletePo9 == null && EnumErrorCode.values()[Integer.parseInt(deleteParams9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteParams9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE8:采购订单被删除后，它的从表也被删除了 ");
		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po);
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 根据商品ID创建一个条形码t1
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 根据商品和条形码创建一个采购订单商品
		PurchasingOrderCommodity pOrderComm = new PurchasingOrderCommodity();
		pOrderComm.setCommodityID(commCreated.getID());
		pOrderComm.setPurchasingOrderID(purchasingOrderCreate.getID());
		pOrderComm.setCommodityNO(Math.abs(new Random().nextInt(300)));
		pOrderComm.setPriceSuggestion(1);
		pOrderComm.setBarcodeID(barcode1.getID());
		//
		Map<String, Object> createpOrderCommparams = pOrderComm.getCreateParam(BaseBO.INVALID_CASE_ID, pOrderComm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(createpOrderCommparams);
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(createpOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		pOrderComm.setIgnoreIDInComparision(true);
		if (pOrderComm.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 删除该采购订单，会把从表信息删除掉
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(purchasingOrderCreate);
		// 根据采购订单查询采购商品
		PurchasingOrderCommodity pOrderCommRN = BasePurchasingOrderTest.DataInput.getPurchasingOrderCommodity();
		pOrderCommRN.setPurchasingOrderID(purchasingOrderCreate.getID());
		Map<String, Object> paramsPOrderCommRN = pOrderComm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pOrderCommRN);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> pOrderCommLs = purchasingOrderCommodityMapper.retrieveN(paramsPOrderCommRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsPOrderCommRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		Assert.assertTrue(pOrderCommLs.size() == 0, "采购订单的从表没有被删除！");
		// 删除商品
		commCreated.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNByFieldsTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:根据商品名称查询商品");
		PurchasingOrder po1 = new PurchasingOrder();
		po1.setQueryKeyword("可乐");
		Map<String, Object> retrieveNParams1 = po1.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList1 = purchasingOrderMapper.retrieveNByFields(retrieveNParams1);
		for (BaseModel bm : poList1) {
			PurchasingOrder po = (PurchasingOrder) bm;
			//
			// 查询当前采购订单的相关采购订单商品表
			PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
			poc.setPurchasingOrderID(po.getID());
			Map<String, Object> params = poc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, poc);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> ls = purchasingOrderCommodityMapper.retrieveN(params);
			Assert.assertTrue(ls.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
			//
			boolean isTrue = false;
			for (BaseModel bm1 : ls) {
				poc = (PurchasingOrderCommodity) bm1;
				if (poc.getCommodityName().contains(po1.getQueryKeyword())) {
					isTrue = true;
					System.out.println("搜索到的结果的字段的值" + poc.getCommodityName() + "包含字符串" + po1.getQueryKeyword());
				}
			}
			Assert.assertTrue(isTrue == true);
		}
		Assert.assertTrue(poList1 != null && poList1.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNByFieldsTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:根据供应商名称查询商品");
		PurchasingOrder po2 = new PurchasingOrder();
		po2.setQueryKeyword("供应商");
		Map<String, Object> retrieveNParams2 = po2.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList2 = purchasingOrderMapper.retrieveNByFields(retrieveNParams2);
		//
		Assert.assertTrue(poList2 != null && poList2.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (BaseModel bm : poList2) {
			PurchasingOrder po = (PurchasingOrder) bm;
			Assert.assertTrue(po.getProviderName().contains(po2.getQueryKeyword()));
			System.out.println("搜索到的结果的字段的值" + po.getProviderName() + "包含字符串" + po2.getQueryKeyword());
		}
	}

	@Test
	public void retrieveNByFieldsTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:根据给定时间段（一个星期）查询采购订单");
		PurchasingOrder po3 = new PurchasingOrder();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		try {
			po3.setDate1(sdf.parse("2016/12/05 23:59:59"));
			po3.setDate2(sdf.parse("2016/12/12 23:59:59"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Map<String, Object> retrieveNParams3 = po3.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList3 = purchasingOrderMapper.retrieveNByFields(retrieveNParams3);
		//
		Assert.assertTrue(poList3 != null && poList3.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList3) {
			PurchasingOrder pOrder = (PurchasingOrder) bm;
			Assert.assertTrue(pOrder.getCreateDatetime().after(po3.getDate1()) && pOrder.getCreateDatetime().before(po3.getDate2()), "case3根据给定时间段查询采购订单错误");
		}
	}

	@Test
	public void retrieveNByFieldsTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:根据给定时间段（一个月）查询采购订单");
		PurchasingOrder po4 = new PurchasingOrder();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			po4.setDate1(sdf.parse("2016/12/01 00:00:00"));
			po4.setDate2(sdf.parse("2016/12/31 23:59:59"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Map<String, Object> retrieveNParams4 = po4.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList4 = purchasingOrderMapper.retrieveNByFields(retrieveNParams4);
		//
		Assert.assertTrue(poList4 != null && poList4.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList4) {
			PurchasingOrder pOrder = (PurchasingOrder) bm;
			Assert.assertTrue(pOrder.getCreateDatetime().after(po4.getDate1()) && pOrder.getCreateDatetime().before(po4.getDate2()), "case4根据给定时间段查询采购订单错误");
		}
	}

	@Test
	public void retrieveNByFieldsTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:根据给定时间段（没有采购订单创建的时间段）查询采购订单");
		PurchasingOrder po5 = new PurchasingOrder();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		try {
			po5.setDate1(sdf.parse("2000/12/06 00:00:00"));
			po5.setDate2(sdf.parse("2010/12/12 23:59:59"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//
		Map<String, Object> retrieveNParams5 = po5.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList5 = purchasingOrderMapper.retrieveNByFields(retrieveNParams5);
		// 结果验证
		Assert.assertTrue(poList5 != null && poList5.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNByFieldsTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:根据操作人的ID（iStaffID=1）查询采购订单");
		PurchasingOrder po6 = new PurchasingOrder();
		po6.setStaffID(2);
		Map<String, Object> retrieveNParams6 = po6.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList6 = purchasingOrderMapper.retrieveNByFields(retrieveNParams6);
		//
		Assert.assertTrue(poList6 != null && poList6.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams6.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList6) {
			PurchasingOrder pOrder = (PurchasingOrder) bm;
			Assert.assertTrue(pOrder.getStaffID() == po6.getStaffID(), "case6根据操作人ID查询采购订单错误");
		}
	}

	@Test
	public void retrieveNByFieldsTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:根据操作人的ID（iStaffID=-999999,ID不存在，返回0条采购订单）查询采购订单");
		PurchasingOrder po7 = new PurchasingOrder();
		po7.setStaffID(-999999);
		Map<String, Object> retrieveNParams7 = po7.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList7 = purchasingOrderMapper.retrieveNByFields(retrieveNParams7);
		// 结果验证
		Assert.assertTrue(poList7 != null && poList7.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNByFieldsTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:根据时间段进行查询采购订单.查询2019/3/13 10:35:47以后的采购订单");
		PurchasingOrder po8 = new PurchasingOrder();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			po8.setDate1(sdf.parse("2019/3/13 10:35:46"));
			po8.setDate2(null);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Map<String, Object> retrieveNParams8 = po8.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po8);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList8 = purchasingOrderMapper.retrieveNByFields(retrieveNParams8);
		//
		Assert.assertTrue(poList8.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList8) {
			PurchasingOrder pOrder = (PurchasingOrder) bm;
			Assert.assertTrue(pOrder.getCreateDatetime().after(po8.getDate1()), "case8根据给定时间段查询采购订单错误");
		}
	}

	@Test
	public void retrieveNByFieldsTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9:根据时间段进行查询采购订单.查询2019/12/6 0:00:00之前的采购订单");
		PurchasingOrder po9 = new PurchasingOrder();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		try {
			po9.setDate1(null);
			po9.setDate2(sdf.parse("2019/12/06 00:00:01"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//
		Map<String, Object> retrieveNParams9 = po9.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po9);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList9 = purchasingOrderMapper.retrieveNByFields(retrieveNParams9);
		Assert.assertTrue(poList9 != null && poList9.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		for (BaseModel bm : poList9) {
			PurchasingOrder pOrder = (PurchasingOrder) bm;
			Assert.assertTrue(pOrder.getCreateDatetime().before(po9.getDate2()), "case9根据给定时间段查询采购订单错误");
		}
	}

	@Test
	public void retrieveNByFieldsTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:根据不存在的采购订单ID进行模糊查询");
		PurchasingOrder po11 = new PurchasingOrder();
		po11.setQueryKeyword("-9999999");
		Map<String, Object> retrieveNParams11 = po11.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po11);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList11 = purchasingOrderMapper.retrieveNByFields(retrieveNParams11);
		// 结果验证
		Assert.assertTrue(poList11 != null && poList11.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams11.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNByFieldsTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:根据QueryKeyword(采购订单ID匹配或者商品名称匹配或者供应商名称匹配的可以查询到)进行模糊查询");
		PurchasingOrder po12 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreate = BasePurchasingOrderTest.purchasingOrderCreate(po12);

		po12.setQueryKeyword(String.valueOf(purchasingOrderCreate.getID()));
		Map<String, Object> retrieveNParams12 = po12.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po12);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList12 = purchasingOrderMapper.retrieveNByFields(retrieveNParams12);
		//
		Assert.assertTrue(poList12 != null && poList12.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams12.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams12.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		boolean is = false;
		for (BaseModel baseModel : poList12) {
			PurchasingOrder pOrder = (PurchasingOrder) baseModel;
			if (pOrder.getID() == purchasingOrderCreate.getID()) {
				is = true;
				break;
			}
		}
		if (!is) {
			Assert.assertTrue(false, "未查询到刚刚创建的采购订单");
		}
	}

	@Test
	public void retrieveNByFieldsTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12:根据空条件进行模糊查询");
		PurchasingOrder po13 = new PurchasingOrder();
		po13.setQueryKeyword("");
		Map<String, Object> retrieveNParams13 = po13.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po13);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList13 = purchasingOrderMapper.retrieveNByFields(retrieveNParams13);
		Assert.assertTrue(poList13 != null && poList13.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams13.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams13.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNByFieldsTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po14 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po14.setStaffID(STAFF_ID4);
		PurchasingOrder poCreate14 = BasePurchasingOrderTest.purchasingOrderCreate(po14);

		Shared.caseLog("Case14:根据全条件进行模糊查询");
		po14.setQueryKeyword(poCreate14.getID() + "");
		Map<String, Object> retrieveNParams14 = po14.getRetrieveNParam(BaseBO.CASE_PurchasingOrder_RetrieveNByNFields, po14);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList14 = purchasingOrderMapper.retrieveNByFields(retrieveNParams14);
		//
		Assert.assertTrue(poList14 != null && poList14.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams14.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams14.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		Boolean equalExistence = false;
		for (BaseModel baseModel : poList14) {
			PurchasingOrder pOrder = (PurchasingOrder) baseModel;
			if (pOrder.getID() == poCreate14.getID()) {
				equalExistence = true;
			}
		}
		Assert.assertTrue(equalExistence, "查询ID=" + poCreate14.getID() + "失败");
	}

	@Test
	public void updateTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		PurchasingOrderCommodity pocCreate = BasePurchasingOrderTest.createPurchasingOrderCommodity(createPo.getID(), 1, 100, 1, 11.1D);

		Shared.caseLog("Case1:修改未审核的采购单");
		PurchasingOrder po1 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po1.setID(createPo.getID());
		po1.setProviderID(2);
		String error = po1.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> updateParams1 = po1.getUpdateParam(BaseBO.INVALID_CASE_ID, po1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo1 = (PurchasingOrder) purchasingOrderMapper.update(updateParams1);
		po1.setIgnoreIDInComparision(true);
		if (po1.compareTo(updatePo1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(updatePo1 != null && EnumErrorCode.values()[Integer.parseInt(updateParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String error1 = updatePo1.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error1, "");
		//
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrderCommodity(pocCreate);
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(updatePo1);
	}

	@Test
	public void updateTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:修改已审核的采购单");
		PurchasingOrder po2 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po2.setID(STATUS1_ID);
		Map<String, Object> retrieveNParams2 = po2.getUpdateParam(BaseBO.INVALID_CASE_ID, po2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo2 = (PurchasingOrder) purchasingOrderMapper.update(retrieveNParams2);
		Assert.assertTrue(updatePo2 == null && EnumErrorCode.values()[Integer.parseInt(retrieveNParams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				retrieveNParams2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void updateTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:修改一个不存在的采购单");
		PurchasingOrder po3 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po3.setID(Shared.BIG_ID);
		Map<String, Object> retrieveNParams3 = po3.getUpdateParam(BaseBO.INVALID_CASE_ID, po3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo3 = (PurchasingOrder) purchasingOrderMapper.update(retrieveNParams3);
		Assert.assertTrue(updatePo3 == null && EnumErrorCode.values()[Integer.parseInt(retrieveNParams3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				retrieveNParams3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void updateTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:修改已审核但部分入库的采购单");
		PurchasingOrder po4 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po4.setID(STATUS1_ID);
		po4.setStatus(STATUS2_ID);
		Map<String, Object> retrieveNParams4 = po4.getUpdateParam(BaseBO.INVALID_CASE_ID, po4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo4 = (PurchasingOrder) purchasingOrderMapper.update(retrieveNParams4);
		Assert.assertTrue(updatePo4 == null && EnumErrorCode.values()[Integer.parseInt(retrieveNParams4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				retrieveNParams4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void updateTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:更新状态为0的 传入的iID不存在  返回错误码为7 ");
		PurchasingOrder po5 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po5.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
		po5.setID(Shared.BIG_ID);
		Map<String, Object> retrieveNParams5 = po5.getUpdateParam(BaseBO.INVALID_CASE_ID, po5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo5 = (PurchasingOrder) purchasingOrderMapper.update(retrieveNParams5);
		Assert.assertTrue(updatePo5 == null && EnumErrorCode.values()[Integer.parseInt(retrieveNParams5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				retrieveNParams5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		/*
		 * 前端会进行校验，此段代码运行会报错，一般remark也不会这么长 System.out.
		 * println("case6：更新状态为0的 传入的sReMark超过数据库限制的长度 " ); PurchasingOrder po6 =
		 * DataInput.getPurchasingOrder(); StringBuffer sBuffer = new StringBuffer();
		 * for(int i=1;i<100;i++) { sBuffer.append("哈哈"); } po6.setStatus(0);
		 * po6.setRemark(sBuffer.toString());
		 * 
		 * Map<String, Object> retrieveNParams6 =
		 * po6.getUpdateParam(BaseBO.INVALID_CASE_ID, po6); PurchasingOrder updatePo6 =
		 * (PurchasingOrder) mapper.update(retrieveNParams6);
		 * 
		 * Assert.assertTrue(updatePo6 == null &&
		 * EnumErrorCode.values()[Integer.parseInt(retrieveNParams6.get(BaseAction.
		 * SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,"修改对象失败");
		 */
	}

	@Test
	public void updateTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:更新状态为0的 传入的iproviderID不存在  返回错误码为7 ");
		PurchasingOrder po6 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po6.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
		po6.setProviderID(INVALID_ID);
		//
		Map<String, Object> retrieveNParams6 = po6.getUpdateParam(BaseBO.INVALID_CASE_ID, po6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo6 = (PurchasingOrder) purchasingOrderMapper.update(retrieveNParams6);
		//
		Assert.assertTrue(updatePo6 == null && EnumErrorCode.values()[Integer.parseInt(retrieveNParams6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				retrieveNParams6.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void updateTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		//
		Shared.caseLog("Case7:修改没有采购商品的采购单");
		PurchasingOrder po1 = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		po1.setID(createPo.getID());
		po1.setProviderID(2);
		String error = po1.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> updateParams1 = po1.getUpdateParam(BaseBO.INVALID_CASE_ID, po1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo1 = (PurchasingOrder) purchasingOrderMapper.update(updateParams1);
		Assert.assertTrue(updatePo1 == null && EnumErrorCode.values()[Integer.parseInt(updateParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(createPo);
	}

	@Test
	public void updateStatusTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:修改状态为部分入库（1 --> 2） 返回错误码0");
		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(createPo.getID(), 1, 100, 1, 11.1D);

		// 状态从 0-->1
		BasePurchasingOrderTest.purchasingOrderApprover(createPo);

		// 状态从 1-->2
		BasePurchasingOrderTest.purchasingOrderUpdateStatusOneToTwo(createPo);
	}

	@Test
	public void updateStatusTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:修改状态为部分入库（2 --> 3） 返回错误码0");
		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(createPo.getID(), 1, 100, 1, 11.1D);
		// 状态从 0-->1
		BasePurchasingOrderTest.purchasingOrderApprover(createPo);

		// 状态从 1-->2
		BasePurchasingOrderTest.purchasingOrderUpdateStatusOneToTwo(createPo);

		// 状态从 2-->3
		BasePurchasingOrderTest.purchasingOrderUpdateStatusTwoToThree(createPo);
	}

	@Test
	public void updateStatusTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3 修改全部入库状态的订单为部分入库(这是不允许的 所以返回错误码7)（3 --> 2）");
		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(createPo.getID(), 1, 100, 1, 11.1D);

		// 状态从 0-->1
		BasePurchasingOrderTest.purchasingOrderApprover(createPo);

		// 状态从 1-->2
		BasePurchasingOrderTest.purchasingOrderUpdateStatusOneToTwo(createPo);

		// 状态从 2-->3
		BasePurchasingOrderTest.purchasingOrderUpdateStatusTwoToThree(createPo);

		// 状态从 3-->2
		createPo.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex());
		Map<String, Object> updateStatusParams34 = createPo.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo34 = (PurchasingOrder) purchasingOrderMapper.updateStatus(updateStatusParams34);
		Assert.assertTrue(updatePo34 == null && EnumErrorCode.values()[Integer.parseInt(updateStatusParams34.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "修改对象失败");
	}

	@Test
	public void updateStatusTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);

		Shared.caseLog("case4:输入一个不存在的iStatus（-1）返回错误码7");
		createPo.setStatus(-1);
		Map<String, Object> updateStatusParams = createPo.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo = (PurchasingOrder) purchasingOrderMapper.updateStatus(updateStatusParams);
		Assert.assertTrue(updatePo == null && EnumErrorCode.values()[Integer.parseInt(updateStatusParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				updateStatusParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除测试数据
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(createPo);
	}

	@Test
	public void updateStatusTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5：输入一个不存在的iID返回错误码7");
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setID(Shared.BIG_ID);
		Map<String, Object> updateStatusParams5 = purchasingOrder.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo5 = (PurchasingOrder) purchasingOrderMapper.updateStatus(updateStatusParams5);
		Assert.assertTrue(updatePo5 == null && EnumErrorCode.values()[Integer.parseInt(updateStatusParams5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				updateStatusParams5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void updateStatusTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);

		Shared.caseLog("case6：修改没有采购商品的采购订单");
		Map<String, Object> updateStatusParams5 = createPo.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo5 = (PurchasingOrder) purchasingOrderMapper.updateStatus(updateStatusParams5);
		Assert.assertTrue(updatePo5 == null && EnumErrorCode.values()[Integer.parseInt(updateStatusParams5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				updateStatusParams5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除测试数据
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(createPo);
	}

	// sp层已做修改，现已不存在这种情况
	/*
	 * @Test public void updateStatusTest6() throws CloneNotSupportedException,
	 * InterruptedException { Shared.printTestMethodStartInfo();
	 * 
	 * Shared.caseLog(
	 * "CASE6:因为入库单可能没有相应的采购订单,在SP_PurchasingOrder_UpdateStatus考虑采购订单为NULL的情况,返回错误码2"
	 * ); PurchasingOrder pOrder6 = DataInput.getPurchasingOrder(); // 状态从 0-->1
	 * pOrder6.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.
	 * getIndex()); Map<String, Object> updateStatusParams71 =
	 * pOrder6.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, pOrder6);
	 * DataSourceContextHolder.setDbName(Shared.DBName_Test);
	 * purchasingOrderMapper.updateStatus(updateStatusParams71);
	 * Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(
	 * updateStatusParams71.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] ==
	 * EnumErrorCode.EC_NoSuchData,
	 * updateStatusParams71.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	 * 
	 * // 状态从 1-->2 pOrder6.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.
	 * ESPO_PartWarehousing.getIndex()); Map<String, Object> updateStatusParams72 =
	 * pOrder6.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, pOrder6);
	 * DataSourceContextHolder.setDbName(Shared.DBName_Test);
	 * purchasingOrderMapper.updateStatus(updateStatusParams72);
	 * Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(
	 * updateStatusParams72.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] ==
	 * EnumErrorCode.EC_NoSuchData,
	 * updateStatusParams72.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()); }
	 */

	@Test
	public void approveTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(createPo.getID(), 1, 100, 1, 11.1D);
		//
		Shared.caseLog("case1:正常审核");
		BasePurchasingOrderTest.purchasingOrderApprover(createPo);
	}

	@Test
	public void approveTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(createPo.getID(), 1, 100, 1, 11.1D);
		// 审核一次
		BasePurchasingOrderTest.purchasingOrderApprover(createPo);
		// 第二次审核
		Shared.caseLog("CASE2:当状态为其他时（F_Status=1）  返回错误码7");
		Map<String, Object> params = createPo.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder approvePo = (PurchasingOrder) purchasingOrderMapper.approve(params);
		Assert.assertTrue(approvePo == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void approveTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:当输入的采购订单表的@iPurchasingOrderID不存在的时候  返回错误码7");
		PurchasingOrder po = new PurchasingOrder();
		po.setID(Shared.BIG_ID);
		po.setApproverID(3);
		Map<String, Object> params = po.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder approvePo = (PurchasingOrder) purchasingOrderMapper.approve(params);
		Assert.assertTrue(approvePo == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void approveTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);

		Shared.caseLog("case4:当输入的审核人@iApproverID不存在的时候  无法调用（主外键关系限制） 返回错误码7");
		PurchasingOrder po1 = new PurchasingOrder();
		po1.setID(createPo.getID());
		po1.setApproverID(INVALID_ID);
		Map<String, Object> params = po1.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, po1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder approvePo = (PurchasingOrder) purchasingOrderMapper.approve(params);
		Assert.assertTrue(approvePo == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除测试数据
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(createPo);
	}

	@Test
	public void approveTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		//
		Shared.caseLog("case5:审核没有采购商品的采购订单");
		BasePurchasingOrderTest.purchasingOrderApprover(createPo);
		//
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(createPo);
	}

	@Test
	public void approveTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		BasePurchasingOrderTest.createPurchasingOrderCommodity(createPo.getID(), 1, 100, 1, 11.1D);
		//
		Shared.caseLog("case6:修改采购订单在审核采购订单");
		// 修改采购订单
		createPo.setProviderID(2);
		createPo.setRemark("00000000000");
		String error = createPo.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> updateParams1 = createPo.getUpdateParam(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo1 = (PurchasingOrder) purchasingOrderMapper.update(updateParams1);
		createPo.setIgnoreIDInComparision(true);
		if (createPo.compareTo(updatePo1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(updatePo1 != null && EnumErrorCode.values()[Integer.parseInt(updateParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String error1 = updatePo1.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error1, "");
		// 审核采购订单
		BasePurchasingOrderTest.purchasingOrderApprover(createPo);
	}

	@Test
	public void approveTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		PurchasingOrderCommodity pocCreate = BasePurchasingOrderTest.createPurchasingOrderCommodity(createPo.getID(), 1, 100, 1, 11.1D);
		//
		Shared.caseLog("case7:修改采购订单在审核采购订单(没有采购商品)");
		// 修改采购订单
		createPo.setProviderID(2);
		createPo.setRemark("00000000000");
		String error = createPo.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> updateParams1 = createPo.getUpdateParam(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo1 = (PurchasingOrder) purchasingOrderMapper.update(updateParams1);
		createPo.setIgnoreIDInComparision(true);
		if (createPo.compareTo(updatePo1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(updatePo1 != null && EnumErrorCode.values()[Integer.parseInt(updateParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				updateParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String error1 = updatePo1.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error1, "");
		// 手动删除修改后的采购商品
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrderCommodity(pocCreate);
		// 审核采购订单
		BasePurchasingOrderTest.purchasingOrderApprover(createPo);
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(createPo);
	}

	@Test
	public void approveTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:当输入的审核人@iApproverID为离职员工ID的时候  无法调用（主外键关系限制） 返回错误码7");
		// 创建员工
		Staff staff = BaseStaffTest.DataInput.getStaff();
		Staff staffToDelete = BaseStaffTest.createStaff(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError, staffMapper);
		// 让员工离职
		BaseStaffTest.deleteStaff(staffToDelete, EnumErrorCode.EC_NoError, staffMapper);
		// 创建测试数据
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder createPo = BasePurchasingOrderTest.purchasingOrderCreate(po);
		//
		PurchasingOrder po1 = new PurchasingOrder();
		po1.setID(createPo.getID());
		po1.setApproverID(staffToDelete.getID());
		Map<String, Object> params = po1.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, po1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder approvePo = (PurchasingOrder) purchasingOrderMapper.approve(params);
		Assert.assertTrue(approvePo == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除测试数据
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(createPo);
	}
}