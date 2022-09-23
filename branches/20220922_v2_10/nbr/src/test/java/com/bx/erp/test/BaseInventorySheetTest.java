package com.bx.erp.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.BaseMapper;
import com.bx.erp.dao.warehousing.InventoryCommodityMapper;
import com.bx.erp.dao.warehousing.InventorySheetMapper;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.InventorySheet;
import com.bx.erp.model.warehousing.InventorySheet.EnumScopeInventorySheet;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseInventorySheetTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {

		super.setup();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkCreate();
		// Doctor_checkStaffID(); // ...不能放带有Assert的语句在非@Test的函数
		// Doctor_checkStatus();
		// Doctor_checkWarehouseID();
		// Doctor_checkInventoryCommodtiy();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkCreate();
		// Doctor_checkStaffID();
		// Doctor_checkStatus();
		// Doctor_checkWarehouseID();
		// Doctor_checkInventoryCommodtiy();
	}

	private void Doctor_checkWarehouseID(InventorySheetMapper inventorySheetMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.checkWarehouseID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的盘点单仓库ID的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的盘点单仓库ID的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkStatus(InventorySheetMapper inventorySheetMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.checkStatus(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的盘点单状态的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的盘点单状态的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkStaffID(InventorySheetMapper inventorySheetMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.checkStaffID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的盘点单经办人ID的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的盘点单经办人ID的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkInventoryCommodtiy(InventorySheetMapper inventorySheetMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.checkInventoryCommodtiy(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的盘点单对应的盘点商品的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的盘点单对应的盘点商品的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkCreate(InventorySheetMapper inventorySheetMapper, InventoryCommodityMapper inventoryCommodityMapper) {
		Shared.printTestClassEndInfo();

		InventorySheet inventorySheet = new InventorySheet();
		inventorySheet.setStatus(BaseAction.INVALID_STATUS);
		inventorySheet.setPageIndex(1);
		inventorySheet.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		String err = inventorySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		if (!err.equals("")) {
			System.out.println("对盘点单进行RN时，传入的的字段数据校验出现异常");
			System.out.println("pOrder=" + (inventorySheet == null ? null : inventorySheet));
		}
		//
		Map<String, Object> params = inventorySheet.getRetrieveNParam(BaseBO.INVALID_CASE_ID, inventorySheet);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> inventorySheetList = inventorySheetMapper.retrieveN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (inventorySheetList.size() == 0 || inventorySheetList == null) {
			System.out.println("RN读出的盘点单信息为空");
		}
		for (BaseModel bm : inventorySheetList) {
			InventorySheet is = (InventorySheet) bm;
			String error1 = is.checkCreate(BaseBO.INVALID_CASE_ID);
			if (!error1.equals("")) {
				System.out.println(is.getID() + "号采购订单数据验证出现异常");
				System.out.println("is=" + (is == null ? null : is));
			}
			InventoryCommodity inventoryCommodity = new InventoryCommodity();
			inventoryCommodity.setInventorySheetID(is.getID());
			inventorySheet.setPageIndex(1);
			inventorySheet.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			Map<String, Object> inventoryCommodityParams = inventoryCommodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, inventoryCommodity);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> inventoryCommodityList = inventoryCommodityMapper.retrieveN(inventoryCommodityParams);
			for (BaseModel ic : inventoryCommodityList) {
				InventoryCommodity iCommodity = (InventoryCommodity) ic;
				String error2 = iCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
				if (!error2.equals("")) {
					System.out.println(iCommodity.getID() + "号盘点商品数据验证出现");
					System.out.println("pComm=" + (iCommodity == null ? null : iCommodity));
				}
			}
		}
	}

	public static class DataInput {
		private static InventorySheet isInput = null;
		private static InventoryCommodity inventoryCommodityInput = null;

		public static final InventorySheet getInventorySheet() throws CloneNotSupportedException, InterruptedException {
			isInput = new InventorySheet();
			isInput.setCreateDatetime(new Date());
			isInput.setWarehouseID(1);
			isInput.setStaffID(STAFF_ID1);
			isInput.setShopID(Shared.DEFAULT_Shop_ID);
			isInput.setScope(EnumScopeInventorySheet.ESIS_WholeShop.getIndex());// .... Giggs TODO
			// isInput.setInt2(1); //...int2作用未知
			isInput.setRemark("aaaaaaaa1");

			return (InventorySheet) isInput.clone();
		}

		public static final InventoryCommodity getInventoryCommodity() throws CloneNotSupportedException, InterruptedException {

			inventoryCommodityInput = new InventoryCommodity();
			// inventoryCommodityInput.setCommodityID(new Random().nextInt(3) + 1);
			// 商品ID写死会造成后面测试几率报错，在测试里面单独创商品数据放进盘点单
			inventoryCommodityInput.setBarcodeID(1);
			inventoryCommodityInput.setNoReal(new Random().nextInt(100));
			inventoryCommodityInput.setInventorySheetID(1);
			inventoryCommodityInput.setCommodityID(new Random().nextInt(3) + 1);
			return (InventoryCommodity) inventoryCommodityInput.clone();
		}

		public static final InventorySheet getInventorySheetAndInventoryCommodity() throws CloneNotSupportedException, InterruptedException {
			isInput = new InventorySheet();
			inventoryCommodityInput = new InventoryCommodity();
			isInput.setWarehouseID(1);
			isInput.setScope(EnumScopeInventorySheet.ESIS_WholeShop.getIndex());// .... Giggs TODO
			isInput.setStaffID(1);
			isInput.setRemark("aaaaaaaa1");

			// 商品ID写死会造成后面测试几率报错，在测试里面单独创商品数据放进盘点单
			// inventoryCommodityInput.setCommodityID(new Random().nextInt(3) + 1);
			inventoryCommodityInput.setBarcodeID(1);
			inventoryCommodityInput.setNoReal(new Random().nextInt(100));

			List<InventoryCommodity> listInventoryCommodity = new ArrayList<InventoryCommodity>();
			listInventoryCommodity.add(inventoryCommodityInput);

			isInput.setListSlave1(listInventoryCommodity);

			return (InventorySheet) isInput.clone();
		}
	}

	/** 创建盘点单商品数据 */
	public static InventoryCommodity createInventoryCommodity(InventoryCommodity ic, EnumErrorCode errorCode, boolean bToCreateCommodity) throws CloneNotSupportedException, InterruptedException {
		// 判断是否需要生成一个新的商品进行盘点
		if (bToCreateCommodity) {
			// 创建商品
			Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
			commodity.setnOStart(100);
			commodity.setPurchasingPriceStart(11.1D);
			List<List<BaseModel>> list = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
			Commodity commCreate = (Commodity) list.get(0).get(0);
			Barcodes barcodes = (Barcodes) list.get(1).get(0);
			ic.setCommodityID(commCreate.getID());
			ic.setBarcodeID(barcodes.getID());
			ic.setNoReal(commCreate.getnOStart());
		}
		//
		String err = ic.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> icParams = ic.getCreateParam(BaseBO.INVALID_CASE_ID, ic);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity icCreate = (InventoryCommodity) inventoryCommodityMapper.create(icParams);
		//
		if (icCreate != null) {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(icParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errorCode, //
					icParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			err = icCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			ic.setIgnoreIDInComparision(true);
			if (ic.compareTo(icCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			//
			System.out.println(icCreate == null ? "null" : icCreate);
			//
			System.out.println("创建盘点商品成功");
		} else {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(icParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errorCode, //
					icParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("创建对象失败:" + icParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		return icCreate;
	}

	/** 创建盘点单数据 */
	public static InventorySheet createInventorySheet(InventorySheet inventorySheet) {
		// 传入参数字段验证
		String error = inventorySheet.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = inventorySheet.getCreateParam(BaseBO.INVALID_CASE_ID, inventorySheet);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet createIs = (InventorySheet) inventorySheetMapper.create(params);
		inventorySheet.setIgnoreIDInComparision(true);
		if (inventorySheet.compareTo(createIs) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createIs != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		String error1 = createIs.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		return createIs;
	}

	/** 删除盘点单并结果验证创建的数据 */
	public static void deleteInventorySheet(InventorySheet is) {
		//
		String err = is.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> isParamsDelete = is.getDeleteParam(BaseBO.INVALID_CASE_ID, is);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.delete(isParamsDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(isParamsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				isParamsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证盘点单是否删除成功，如果根据ID查询盘点单为空并且错误码为2则删除成功
		Map<String, Object> isParamsR1 = is.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, is);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> isR1List = (List<List<BaseModel>>) inventorySheetMapper.retrieve1Ex(isParamsR1);
		//
		Assert.assertTrue(isR1List.size() == 0 && EnumErrorCode.values()[Integer.parseInt(isParamsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				isParamsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("盘点单删除成功");
	}

	/** 删除盘点单从表并结果验证创建的数据 */
	public static void deleteInventoryCommodity(InventoryCommodity icCreate) {
		//
		Map<String, Object> params2 = icCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, icCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(params2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramsRetrieveN = icCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, icCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> wcList = (List<BaseModel>) inventoryCommodityMapper.retrieveN(paramsRetrieveN);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (BaseModel bm : wcList) {
			InventoryCommodity ic = (InventoryCommodity) bm;
			//
			Assert.assertFalse(icCreate.getCommodityID() == ic.getCommodityID());
		}
	}

	/** 提交盘点单 */
	public static InventorySheet submitInventorySheet(InventorySheet isCreate, EnumErrorCode errorCode) {
		//
		Map<String, Object> isParamsSubmit = isCreate.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet isSubmit = (InventorySheet) inventorySheetMapper.submit(isParamsSubmit);
		//
		if (isSubmit != null) { // 提交成功
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(isParamsSubmit.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errorCode, //
					isParamsSubmit.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			String err = isSubmit.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			isCreate.setIgnoreIDInComparision(true);
			Assert.assertTrue(isCreate.compareTo(isSubmit) != 0, "修改盘点单失败");
			//
			System.out.println("修改盘点单成功");
		} else { // 提交失败
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(isParamsSubmit.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errorCode, //
					isParamsSubmit.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			String err = isCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			Map<String, Object> isParamsR1 = isCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, isCreate);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			InventorySheet isR1 = (InventorySheet) inventorySheetMapper.retrieve1(isParamsR1);
			//
			Assert.assertTrue(isR1 != null && EnumErrorCode.values()[Integer.parseInt(isParamsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					isParamsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(isCreate.compareTo(isR1) == 0, "创建的对象的字段与DB读出的不相等");
			//
			System.out.println("修改对象失败 : " + isParamsSubmit.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return isSubmit;
	}

	/** 审核盘点单 */
	public static InventorySheet approveInventorySheet(InventorySheet isCreate, EnumErrorCode errorCode) {
		//
		Map<String, Object> isParamsApprove = isCreate.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, isCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = inventorySheetMapper.approveEx(isParamsApprove);
		InventorySheet isApprove = (InventorySheet) bmList.get(0).get(0);
		//
		if (isApprove != null) { // 审核成功
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(isParamsApprove.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errorCode, //
					isParamsApprove.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			String err = isApprove.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			isCreate.setIgnoreIDInComparision(true);
			Assert.assertTrue(isCreate.compareTo(isApprove) != 0, "修改盘点单失败");
			//
			System.out.println("修改盘点单成功");
		} else { // 审核失败
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(isParamsApprove.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errorCode, //
					isParamsApprove.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			String err = isCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			Map<String, Object> isParamsR1 = isCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, isCreate);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			InventorySheet isR1 = (InventorySheet) inventorySheetMapper.retrieve1(isParamsR1);
			//
			Assert.assertTrue(isR1 != null && EnumErrorCode.values()[Integer.parseInt(isParamsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
					isParamsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(isCreate.compareTo(isR1) == 0, "创建的对象的字段与DB读出的不相等");
			//
			System.out.println("修改对象失败: " + isParamsApprove.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return isApprove;
	}

	public static InventorySheet updateInventorySheet(InventorySheet isCreate, EnumErrorCode errorCode, Map<String, BaseMapper> mapMapper) {
		InventorySheetMapper inventorySheetMapper = (InventorySheetMapper) mapMapper.get(InventorySheetMapper.class.getSimpleName());
		//
		Map<String, Object> isParams = isCreate.getUpdateParam(BaseBO.INVALID_CASE_ID, isCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet isUpdate = (InventorySheet) inventorySheetMapper.update(isParams);
		//
		if (isUpdate != null) { // 修改成功
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(isParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errorCode, //
					isParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			String err = isUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			isCreate.setIgnoreIDInComparision(true);
			Assert.assertTrue(isCreate.compareTo(isUpdate) != 0, "修改盘点单失败");
			//
			System.out.println("修改盘点单成功");
		} else { // 修改失败
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(isParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errorCode, //
					isParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			String err = isCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			Map<String, Object> isParamsR1 = isCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, isCreate);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			InventorySheet isR1 = (InventorySheet) inventorySheetMapper.retrieve1(isParamsR1);
			//
			Assert.assertTrue(isR1 != null && EnumErrorCode.values()[Integer.parseInt(isParamsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					isParamsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(isCreate.compareTo(isR1) == 0, "创建的对象的字段与DB读出的不相等");
			//
			System.out.println("修改对象失败: " + isParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return isUpdate;
	}

	public static InventoryCommodity updateInventoryCommodityNoReal(InventoryCommodity icCreate, InventoryCommodity ic) {
		//
		Map<String, Object> icParamsNoReal = ic.getUpdateParam(BaseBO.CASE_UpdateInventoryCommodityNoReal, ic);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity icNoReal = (InventoryCommodity) inventoryCommodityMapper.updateNoReal(icParamsNoReal);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(icParamsNoReal.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				icParamsNoReal.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (icNoReal != null) { // 修改成功
			String err = icNoReal.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			icCreate.setIgnoreIDInComparision(true);
			Assert.assertTrue(icCreate.compareTo(icNoReal) != 0, "修改盘点单商品失败");
			//
			System.out.println("修改盘点单商品成功");
		} else { // 修改失败
			String err = icCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			Map<String, Object> icParamsRN = icCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, icCreate);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> icRN = (List<BaseModel>) inventoryCommodityMapper.retrieveN(icParamsRN);
			//
			Assert.assertTrue(icRN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(icParamsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					icParamsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			boolean isSame = false;
			for (BaseModel bm : icRN) {
				if (icCreate.compareTo((InventoryCommodity) bm) == 0) {
					isSame = true;
					break;
				}
			}
			Assert.assertTrue(isSame, "创建的对象的字段与DB读出的不相等");
			//
			System.out.println("修改盘点单商品失败: " + icParamsNoReal.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return icNoReal;
	}

	public static InventoryCommodity updateInventoryCommodity(InventoryCommodity icCreate, InventoryCommodity ic, int iUseCaseID, EnumErrorCode errorCode) {
		//
		Map<String, Object> icParamsUpdate = ic.getUpdateParam(iUseCaseID, ic);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity icUpdate = (InventoryCommodity) inventoryCommodityMapper.update(icParamsUpdate);
		//
		if (icUpdate != null) { // 修改成功
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(icParamsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errorCode, //
					icParamsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			String err = icUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			icUpdate.setIgnoreIDInComparision(true);
			if (icUpdate.compareTo(icCreate) == 0) {
				Assert.assertTrue(false, "修改盘点单商品失败");
			}
			//
			System.out.println("修改盘点单商品成功");
		} else { // 修改失败
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(icParamsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errorCode, //
					icParamsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			String err = icCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			Map<String, Object> icParamsRN = icCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, icCreate);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> icRN = (List<BaseModel>) inventoryCommodityMapper.retrieveN(icParamsRN);
			//
			Assert.assertTrue(icRN.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(icParamsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
					icParamsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			for (BaseModel bm : icRN) {
				Assert.assertTrue(icCreate.compareTo((InventoryCommodity) bm) == 0, "创建的对象的字段与DB读出的不相等");
			}
			//
			System.out.println("修改对象失败: " + icParamsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return icUpdate;
	}

	/** 创建盘点单以及盘点单商品 */
	public static InventorySheet createInventorySheet() throws CloneNotSupportedException, InterruptedException {
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		InventoryCommodity ic = new InventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		//
		List<BaseModel> listbm = new ArrayList<BaseModel>();
		listbm.add(icCreate);
		isCreate.setListSlave1(listbm);
		//
		return isCreate;
	}
	
	public static InventoryCommodity createInventoryCommodityViaMapper(int commodityID) throws CloneNotSupportedException, InterruptedException {
		//
		InventoryCommodity ic = DataInput.getInventoryCommodity();
		ic.setCommodityID(commodityID);
		Map<String, Object> icParams = ic.getCreateParam(BaseBO.INVALID_CASE_ID, ic);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity icCreate = (InventoryCommodity) inventoryCommodityMapper.create(icParams);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(icParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, icParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = icCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		ic.setIgnoreIDInComparision(true);
		if (ic.compareTo(icCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		return icCreate;
	}

}