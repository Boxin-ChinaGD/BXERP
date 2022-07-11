package com.bx.erp.test.inventory;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import org.springframework.test.annotation.Rollback;
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
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.InventorySheet;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseInventorySheetTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

// 待修改：创建和修改时没有比较前后的对象的一致性
// ...
@WebAppConfiguration
public class InventorySheetMapperTest extends BaseMapperTest {
	private static final int INVALID_ID = 999999999;
	private static final int APPROVER_ID1 = 1;

	private static InventorySheet inventorySheet1;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@DataProvider
	public Object[][] create1() {
		return new Object[][] { { 2, 1, 12, 3, EnumErrorCode.EC_NoError, 1 }, { 2, INVALID_ID, 12, 3, EnumErrorCode.EC_BusinessLogicNotDefined, 0 }, { 2, 1, 12, INVALID_ID, EnumErrorCode.EC_BusinessLogicNotDefined, 0 }, { INVALID_ID, 1, 12, 3, EnumErrorCode.EC_BusinessLogicNotDefined, 0 } };
	}

	public EnumErrorCode createOne(int shopID, int iWarehouseID, int iScope, int iStaffID, int i) {
		InventorySheet inventorySheet = new InventorySheet();
		inventorySheet.setStatus(InventorySheet.EnumStatusInventorySheet.ESIS_ToInput.getIndex());
		inventorySheet.setRemark("aaaaaaaa1");
		inventorySheet.setWarehouseID(iWarehouseID);
		inventorySheet.setScope(iScope);
		inventorySheet.setStaffID(iStaffID);
		inventorySheet.setShopID(shopID);
		//
		Map<String, Object> params = inventorySheet.getCreateParam(BaseBO.INVALID_CASE_ID, inventorySheet);

		//

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheet1 = (InventorySheet) inventorySheetMapper.create(params);// ...

		if (i == 1) {
			// Assert.assertEquals(inventorySheet1.compareTo(inventorySheet), 0);
			inventorySheet.setIgnoreIDInComparision(true);
			if (inventorySheet.compareTo(inventorySheet1) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			System.out.println("iIDFromCreat1：" + inventorySheet1.getID());
		}
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Rollback(false)
	@Test(dataProvider = "create1")
	public void createTest1(int shopID, int iWarehouseID, int iScope, int iStaffID, EnumErrorCode ec, int i) {
		Shared.printTestMethodStartInfo();

		System.out.println("Test Jenkins+++++++++++++++++++-------------------------------");
		Assert.assertEquals(createOne(shopID, iWarehouseID, iScope, iStaffID, i), ec);

	}

	@DataProvider
	public Object[][] approve1() throws Exception {
		// 判断状态如果为1，则iErrorCode返回 EC_NoError，否则iErrorCode反回 EC_BusinessLogicNotDefined
		// iStatus：0=待录入、1=已提交、2=已审核
		// CASE1:审核待录入的盘点单
		InventorySheet is1 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate1 = BaseInventorySheetTest.createInventorySheet(is1);
		InventoryCommodity ic = new InventoryCommodity();
		ic.setInventorySheetID(isCreate1.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// CASE2:审核已提交的盘点单
		InventorySheet is2 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate2 = BaseInventorySheetTest.createInventorySheet(is2);
		ic.setInventorySheetID(isCreate2.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 提交盘点单
		Map<String, Object> submitParams2 = isCreate2.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS2 = inventorySheetMapper.submit(submitParams2);
		// CASE3:审核已审核的盘点单
		InventorySheet is3 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate3 = BaseInventorySheetTest.createInventorySheet(is3);
		ic.setInventorySheetID(isCreate3.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 提交盘点单
		Map<String, Object> submitParams3 = isCreate3.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS3 = inventorySheetMapper.submit(submitParams3);
		// 审核盘点单
		submitIS3.setApproverID(1);
		Map<String, Object> approveParams3 = submitIS3.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submitIS3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list3 = inventorySheetMapper.approveEx(approveParams3);
		InventorySheet approveIS3 = (InventorySheet) list3.get(0).get(0);
		// CASE4:审核待录入的盘点单并且没有盘点商品
		InventorySheet is4 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate4 = BaseInventorySheetTest.createInventorySheet(is4);
		// CASE5:审核已提交的盘点单并且没有盘点商品
		InventorySheet is5 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate5 = BaseInventorySheetTest.createInventorySheet(is5);
		ic.setInventorySheetID(isCreate5.getID());
		InventoryCommodity ic5 = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		//
		Map<String, Object> submitParams5 = isCreate5.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS5 = inventorySheetMapper.submit(submitParams5);
		// 手动删除已提交盘点单对应的盘点商品
		Map<String, Object> daleteParams5 = ic5.getDeleteParam(BaseBO.INVALID_CASE_ID, ic5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(daleteParams5);
		// CASE6:审核已审核的盘点单并且没有盘点商品
		InventorySheet is6 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate6 = BaseInventorySheetTest.createInventorySheet(is6);
		ic.setInventorySheetID(isCreate6.getID());
		InventoryCommodity ic6 = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 提交盘点单
		Map<String, Object> submitParams6 = isCreate6.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS6 = inventorySheetMapper.submit(submitParams6);
		// 审核盘点单
		submitIS6.setApproverID(1);
		Map<String, Object> approveParams6 = submitIS6.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submitIS6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list6 = inventorySheetMapper.approveEx(approveParams6);
		InventorySheet approveIS6 = (InventorySheet) list6.get(0).get(0);
		// 手动删除已审核盘点单对应的盘点商品
		Map<String, Object> daleteParams6 = ic6.getDeleteParam(BaseBO.INVALID_CASE_ID, ic6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(daleteParams6);
		// CASE7:审核已删除的盘点单
		InventorySheet is7 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate7 = BaseInventorySheetTest.createInventorySheet(is7);
		ic.setInventorySheetID(isCreate7.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 删除盘点单
		Map<String, Object> deleteParams7 = isCreate7.getDeleteParam(BaseBO.INVALID_CASE_ID, isCreate7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.delete(deleteParams7);
		// CASE8:审核已删除的盘点单并且没有商品
		InventorySheet is8 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate8 = BaseInventorySheetTest.createInventorySheet(is8);
		ic.setInventorySheetID(isCreate8.getID());
		InventoryCommodity ic8 = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 删除盘点单
		Map<String, Object> deleteParams8 = isCreate7.getDeleteParam(BaseBO.INVALID_CASE_ID, isCreate8);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.delete(deleteParams8);
		// 手动删除已删除盘点单对应的盘点商品
		Map<String, Object> daleteParams9 = ic6.getDeleteParam(BaseBO.INVALID_CASE_ID, ic8);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(daleteParams9);
		//
		return new Object[][] { { isCreate1.getID(), 1, EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ submitIS2.getID(), 1, EnumErrorCode.EC_NoError }, //
				{ approveIS3.getID(), 1, EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ isCreate4.getID(), 1, EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ submitIS5.getID(), 1, EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ approveIS6.getID(), 1, EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ isCreate7.getID(), 1, EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ isCreate8.getID(), 1, EnumErrorCode.EC_BusinessLogicNotDefined } };
	}

	public EnumErrorCode approve(int id, int iApproverID) {
		InventorySheet is = new InventorySheet();
		is.setID(id);
		is.setApproverID(iApproverID);
		Map<String, Object> params = is.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, is);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.approveEx(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		if (!params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString().equals("0")) {
			Map<String, Object> params2 = is.getDeleteParam(BaseBO.INVALID_CASE_ID, is);
			inventorySheetMapper.delete(params2);
		}
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Rollback(false)
	@Test(dataProvider = "approve1")
	public void approveTest(int id, int iApproverID, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();

		Assert.assertEquals(approve(id, iApproverID), ec);
	}

	@DataProvider
	public Object[][] retrieveOne1() {
		return new Object[][] { { 1, EnumErrorCode.EC_NoError }, { 2, EnumErrorCode.EC_NoError } };
	}

	public EnumErrorCode retrieveOne(int id) {
		InventorySheet is = new InventorySheet();
		is.setID(id);
		Map<String, Object> params = is.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, is);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.retrieve1Ex(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get("iErrorCode").toString())]);
		return EnumErrorCode.values()[Integer.parseInt(params.get("iErrorCode").toString())];
	}

	@Rollback(false)
	@Test(dataProvider = "retrieveOne1")
	public void retrieveOneTest(int id, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();

		Assert.assertEquals(retrieveOne(id), ec);
	}

	@DataProvider
	public Object[][] retrieveN1() {
		return new Object[][] { //
				{ BaseAction.INVALID_ID, InventorySheet.EnumStatusInventorySheet.ESIS_ToInput, BaseAction.PAGE_StartIndex, 5, EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, InventorySheet.EnumStatusInventorySheet.ESIS_Submitted, BaseAction.PAGE_StartIndex, 5, EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, InventorySheet.EnumStatusInventorySheet.ESIS_Approved, BaseAction.PAGE_StartIndex, 5, EnumErrorCode.EC_NoError }, //
				{ Shared.DEFAULT_Shop_ID, InventorySheet.EnumStatusInventorySheet.ESIS_ToInput, BaseAction.PAGE_StartIndex, 5, EnumErrorCode.EC_NoError }, //
				{ BaseAction.INVALID_ID, InventorySheet.EnumStatusInventorySheet.ESIS_Deleted, BaseAction.PAGE_StartIndex, 5, EnumErrorCode.EC_WrongFormatForInputField }//
		};
	}

	public EnumErrorCode retrieveN(int shopID, InventorySheet.EnumStatusInventorySheet status, int iPageIndex, int iPageSize) {
		InventorySheet is = new InventorySheet();
		is.setStatus(status.getIndex());
		is.setShopID(shopID);
		is.setPageIndex(iPageIndex);
		is.setPageSize(iPageSize);
		if (!is.checkRetrieveN(BaseBO.INVALID_CASE_ID).equals("")) {
			return EnumErrorCode.EC_WrongFormatForInputField;
		}
		//
		Map<String, Object> params = is.getRetrieveNParam(BaseBO.INVALID_CASE_ID, is);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.retrieveN(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Rollback(false)
	@Test(dataProvider = "retrieveN1")
	public void retrieveNTest(int shopID, InventorySheet.EnumStatusInventorySheet status, int iPageIndex, int iPageSize, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();

		Assert.assertEquals(retrieveN(shopID, status, iPageIndex, iPageSize), ec);
	}

	@DataProvider
	public Object[][] submit1() throws Exception {
		// CASE1:提交待录入的盘点单
		InventorySheet is1 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate1 = BaseInventorySheetTest.createInventorySheet(is1);
		// 创建盘点商品
		InventoryCommodity ic = new InventoryCommodity();
		ic.setInventorySheetID(isCreate1.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// CASE2:提交已提交的盘点单
		InventorySheet is2 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate2 = BaseInventorySheetTest.createInventorySheet(is2);
		// 创建盘点商品
		ic.setInventorySheetID(isCreate2.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 提交盘点单
		Map<String, Object> submitParams2 = isCreate2.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS2 = inventorySheetMapper.submit(submitParams2);
		// CASE3： 提交已审核的盘点单
		InventorySheet is3 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate3 = BaseInventorySheetTest.createInventorySheet(is3);
		ic.setInventorySheetID(isCreate3.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 提交盘点单
		Map<String, Object> submitParams3 = isCreate3.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS3 = inventorySheetMapper.submit(submitParams3);
		// 审核盘点单
		submitIS3.setApproverID(1);
		Map<String, Object> approveParams3 = submitIS3.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submitIS3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list3 = inventorySheetMapper.approveEx(approveParams3);
		InventorySheet approveIS3 = (InventorySheet) list3.get(0).get(0);
		// CASE4：提交已删除的盘点单
		InventorySheet is4 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate4 = BaseInventorySheetTest.createInventorySheet(is4);
		ic.setInventorySheetID(isCreate4.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 删除盘点单
		Map<String, Object> deleteParams4 = isCreate4.getDeleteParam(BaseBO.INVALID_CASE_ID, isCreate4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.delete(deleteParams4);
		// CASE5:提交待录入的盘点单并且没有盘点商品
		InventorySheet is5 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate5 = BaseInventorySheetTest.createInventorySheet(is5);
		// CASE6:提交已提交的盘点单并且没有盘点商品
		InventorySheet is6 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate6 = BaseInventorySheetTest.createInventorySheet(is6);
		ic.setInventorySheetID(isCreate6.getID());
		InventoryCommodity ic6 = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		//
		Map<String, Object> submitParams6 = isCreate6.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS6 = inventorySheetMapper.submit(submitParams6);
		// 手动删除已提交盘点单对应的盘点商品
		Map<String, Object> daleteParams6 = ic6.getDeleteParam(BaseBO.INVALID_CASE_ID, ic6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(daleteParams6);
		// CASE7： 提交已审核的盘点单并且没有盘点商品
		InventorySheet is7 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate7 = BaseInventorySheetTest.createInventorySheet(is7);
		ic.setInventorySheetID(isCreate7.getID());
		InventoryCommodity ic7 = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 提交盘点单
		Map<String, Object> submitParams7 = isCreate7.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS7 = inventorySheetMapper.submit(submitParams7);
		// 审核盘点单
		submitIS7.setApproverID(1);
		Map<String, Object> approveParams6 = submitIS7.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submitIS7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list7 = inventorySheetMapper.approveEx(approveParams6);
		InventorySheet approveIS7 = (InventorySheet) list7.get(0).get(0);
		// 手动删除已审核盘点单对应的盘点商品
		Map<String, Object> daleteParams7 = ic7.getDeleteParam(BaseBO.INVALID_CASE_ID, ic7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(daleteParams7);
		// CASE8：提交已删除的盘点单并且没有盘点商品
		InventorySheet is8 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate8 = BaseInventorySheetTest.createInventorySheet(is8);
		ic.setInventorySheetID(isCreate8.getID());
		InventoryCommodity ic8 = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 删除盘点单
		Map<String, Object> deleteParams8 = isCreate7.getDeleteParam(BaseBO.INVALID_CASE_ID, isCreate8);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.delete(deleteParams8);
		// 手动删除已删除盘点单对应的盘点商品
		Map<String, Object> daleteParams9 = ic6.getDeleteParam(BaseBO.INVALID_CASE_ID, ic8);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(daleteParams9);
		//
		return new Object[][] { { isCreate1.getID(), EnumErrorCode.EC_NoError }, { submitIS2.getID(), EnumErrorCode.EC_BusinessLogicNotDefined }, { approveIS3.getID(), EnumErrorCode.EC_BusinessLogicNotDefined },
				{ isCreate4.getID(), EnumErrorCode.EC_BusinessLogicNotDefined }, { isCreate5.getID(), EnumErrorCode.EC_BusinessLogicNotDefined }, { submitIS6.getID(), EnumErrorCode.EC_BusinessLogicNotDefined },
				{ approveIS7.getID(), EnumErrorCode.EC_BusinessLogicNotDefined }, { isCreate8.getID(), EnumErrorCode.EC_BusinessLogicNotDefined } };
	}

	public EnumErrorCode submit(int id) {
		InventorySheet is = new InventorySheet();
		is.setID(id);
		//
		Map<String, Object> params = is.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, is);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.submit(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Rollback(false)
	@Test(dataProvider = "submit1")
	public void submitTest(int id, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();

		Assert.assertEquals(submit(id), ec);
	}

	@DataProvider
	public Object[][] updateSheet1() throws CloneNotSupportedException, InterruptedException {
		// CASE1:提交待录入的盘点单
		InventorySheet is1 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate1 = BaseInventorySheetTest.createInventorySheet(is1);
		// 创建盘点商品
		InventoryCommodity ic = new InventoryCommodity();
		ic.setInventorySheetID(isCreate1.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// CASE2:提交已提交的盘点单
		InventorySheet is2 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate2 = BaseInventorySheetTest.createInventorySheet(is2);
		// 创建盘点商品
		ic.setInventorySheetID(isCreate2.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 提交盘点单
		Map<String, Object> submitParams2 = isCreate2.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS2 = inventorySheetMapper.submit(submitParams2);
		// CASE3： 提交已审核的盘点单
		InventorySheet is3 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate3 = BaseInventorySheetTest.createInventorySheet(is3);
		ic.setInventorySheetID(isCreate3.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 提交盘点单
		Map<String, Object> submitParams3 = isCreate3.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS3 = inventorySheetMapper.submit(submitParams3);
		// 审核盘点单
		submitIS3.setApproverID(1);
		Map<String, Object> approveParams3 = submitIS3.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submitIS3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list3 = inventorySheetMapper.approveEx(approveParams3);
		InventorySheet approveIS3 = (InventorySheet) list3.get(0).get(0);
		// CASE4：提交已删除的盘点单
		InventorySheet is4 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate4 = BaseInventorySheetTest.createInventorySheet(is4);
		ic.setInventorySheetID(isCreate4.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 删除盘点单
		Map<String, Object> deleteParams4 = isCreate4.getDeleteParam(BaseBO.INVALID_CASE_ID, isCreate4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.delete(deleteParams4);
		// CASE5:提交待录入的盘点单并且没有盘点商品
		InventorySheet is5 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate5 = BaseInventorySheetTest.createInventorySheet(is5);
		// CASE6:提交已提交的盘点单并且没有盘点商品
		InventorySheet is6 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate6 = BaseInventorySheetTest.createInventorySheet(is6);
		ic.setInventorySheetID(isCreate6.getID());
		InventoryCommodity ic6 = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		//
		Map<String, Object> submitParams6 = isCreate6.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS6 = inventorySheetMapper.submit(submitParams6);
		// 手动删除已提交盘点单对应的盘点商品
		Map<String, Object> daleteParams6 = ic6.getDeleteParam(BaseBO.INVALID_CASE_ID, ic6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(daleteParams6);
		// CASE7： 提交已审核的盘点单并且没有盘点商品
		InventorySheet is7 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate7 = BaseInventorySheetTest.createInventorySheet(is7);
		ic.setInventorySheetID(isCreate7.getID());
		InventoryCommodity ic7 = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 提交盘点单
		Map<String, Object> submitParams7 = isCreate7.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, isCreate7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitIS7 = inventorySheetMapper.submit(submitParams7);
		// 审核盘点单
		submitIS7.setApproverID(1);
		Map<String, Object> approveParams6 = submitIS7.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, submitIS7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list7 = inventorySheetMapper.approveEx(approveParams6);
		InventorySheet approveIS7 = (InventorySheet) list7.get(0).get(0);
		// 手动删除已审核盘点单对应的盘点商品
		Map<String, Object> daleteParams7 = ic7.getDeleteParam(BaseBO.INVALID_CASE_ID, ic7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(daleteParams7);
		// CASE8：提交已删除的盘点单并且没有盘点商品
		InventorySheet is8 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate8 = BaseInventorySheetTest.createInventorySheet(is8);
		ic.setInventorySheetID(isCreate8.getID());
		InventoryCommodity ic8 = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, true);
		// 删除盘点单
		Map<String, Object> deleteParams8 = isCreate7.getDeleteParam(BaseBO.INVALID_CASE_ID, isCreate8);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.delete(deleteParams8);
		// 手动删除已删除盘点单对应的盘点商品
		Map<String, Object> daleteParams9 = ic6.getDeleteParam(BaseBO.INVALID_CASE_ID, ic8);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(daleteParams9);
		//
		return new Object[][] { { isCreate1.getID(), "bbbbbbbbbbbbbbbb1", EnumErrorCode.EC_NoError }, // 状态为0
				{ submitIS2.getID(), "bbbbbbbbbbbbbbbb2", EnumErrorCode.EC_NoError }, // 状态为2
				{ approveIS3.getID(), "bbbbbbbbbbbbbbbb3", EnumErrorCode.EC_BusinessLogicNotDefined }, // 状态为1
				{ isCreate4.getID(), "bbbbbbbbbbbbbbbb4", EnumErrorCode.EC_BusinessLogicNotDefined }, // 状态为3
				{ isCreate5.getID(), "bbbbbbbbbbbbbbbb5", EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ submitIS6.getID(), "bbbbbbbbbbbbbbbb6", EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ approveIS7.getID(), "bbbbbbbbbbbbbbbb7", EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ isCreate8.getID(), "bbbbbbbbbbbbbbbb8", EnumErrorCode.EC_BusinessLogicNotDefined } };
	}

	public EnumErrorCode updateSheet(int id, String remark) {
		InventorySheet is = new InventorySheet();
		is.setID(id);
		is.setRemark(remark);
		//
		Map<String, Object> params = is.getUpdateParam(BaseBO.INVALID_CASE_ID, is);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.update(params);// ...
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Rollback(false)
	@Test(dataProvider = "updateSheet1")
	public void updateSheetTest(int id, String remark, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();

		Assert.assertEquals(updateSheet(id, remark), ec);
	}

	@DataProvider
	public Object[][] delete1() throws CloneNotSupportedException, InterruptedException {
		// 创建一条数据用于测试正常删除
		InventorySheet is1 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet inventorySheet1 = BaseInventorySheetTest.createInventorySheet(is1);
		InventoryCommodity ic1 = new InventoryCommodity();
		ic1.setInventorySheetID(inventorySheet1.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic1, EnumErrorCode.EC_NoError, true);
		// 创建一条数据用于测试提交后删除
		InventorySheet is2 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet inventorySheet2 = BaseInventorySheetTest.createInventorySheet(is2);
		InventoryCommodity ic2 = new InventoryCommodity();
		ic2.setInventorySheetID(inventorySheet2.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic2, EnumErrorCode.EC_NoError, true);
		// 进行提交
		Map<String, Object> submitParams = inventorySheet2.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, inventorySheet2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitInventorySheet = inventorySheetMapper.submit(submitParams);
		Assert.assertTrue(submitInventorySheet != null && EnumErrorCode.values()[Integer.parseInt(submitParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				submitParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 创建一条数据用于测试审核后删除
		InventorySheet is3 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet inventorySheet3 = BaseInventorySheetTest.createInventorySheet(is3);
		InventoryCommodity ic3 = new InventoryCommodity();
		ic3.setInventorySheetID(inventorySheet3.getID());
		BaseInventorySheetTest.createInventoryCommodity(ic3, EnumErrorCode.EC_NoError, true);
		// 进行提交
		Map<String, Object> submitParams1 = inventorySheet3.getUpdateParam(BaseBO.CASE_SubmitInventorySheet, inventorySheet3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitInventorySheet1 = inventorySheetMapper.submit(submitParams1);
		Assert.assertTrue(submitInventorySheet1 != null && EnumErrorCode.values()[Integer.parseInt(submitParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				submitParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 进行审核
		inventorySheet3.setApproverID(APPROVER_ID1);
		Map<String, Object> paramsApprover = inventorySheet3.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, inventorySheet3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = inventorySheetMapper.approveEx(paramsApprover);
		InventorySheet inventorySheetApperover = (InventorySheet) bmList.get(0).get(0);
		Assert.assertTrue(inventorySheetApperover != null && EnumErrorCode.values()[Integer.parseInt(paramsApprover.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsApprover.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 创建一条数据用于测试删除已删除的数据
		InventorySheet is4 = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet inventorySheet4 = BaseInventorySheetTest.createInventorySheet(is4);
		// 进行删除
		Map<String, Object> paramsDelete = inventorySheet4.getUpdateParam(BaseBO.CASE_ApproveInventorySheet, inventorySheet4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.delete(paramsDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		return new Object[][] { { inventorySheet1.getID(), EnumErrorCode.EC_NoError }, //
				{ submitInventorySheet.getID(), EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ inventorySheetApperover.getID(), EnumErrorCode.EC_BusinessLogicNotDefined }, //
				{ inventorySheet4.getID(), EnumErrorCode.EC_BusinessLogicNotDefined } };
	}

	public EnumErrorCode delete(int id) {

		Map<String, Object> params = new InventorySheet().getDeleteParam(BaseBO.INVALID_CASE_ID, id);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.delete(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Rollback(false)
	@Test(dataProvider = "delete1")
	public void deleteTest(int id, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------盘点单的ID是" + id + "-----------------");
		Assert.assertEquals(delete(id), ec);
	}

	@DataProvider
	public Object[][] retrieveNByFields() {
		return new Object[][] { //
				{ "薯片", 1, 10, EnumErrorCode.EC_NoError, 1 }, //
				{ "", 1, 10, EnumErrorCode.EC_NoError, 0 }, //
				{ "PD20190605", 1, 10, EnumErrorCode.EC_NoError, 0 }, //
				{ "PD2019060", 1, 10, EnumErrorCode.EC_NoError, 0 }, //
				{ "PD20190605123451234512345", 1, 10, EnumErrorCode.EC_NoError, 0 }, //
				{ "PD201906051234512345", 1, 10, EnumErrorCode.EC_NoError, 0 } //
		};
	}

	public EnumErrorCode retrieveNByFields(String queryKeyword, int iPageIndex, int iPageSize, int a) {
		InventorySheet is = new InventorySheet();
		is.setQueryKeyword(queryKeyword);
		is.setPageIndex(iPageIndex);
		is.setPageSize(iPageSize);
		//
		Map<String, Object> params = is.getRetrieveNParam(BaseBO.CASE_InventorySheet_RetrieveNByNFields, is);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = (List<BaseModel>) inventorySheetMapper.retrieveNByFields(params);

		if (queryKeyword != null) {
			// 名称，简称，助记码，6位以上的条形码
			for (BaseModel bm : listBM) {
				InventorySheet inventorySheet = (InventorySheet) bm;
				switch (a) {
				case 1:
					InventoryCommodity ic = new InventoryCommodity();
					ic.setInventorySheetID(inventorySheet.getID());
					Map<String, Object> params1 = ic.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ic);
					DataSourceContextHolder.setDbName(Shared.DBName_Test);
					List<BaseModel> ls = inventoryCommodityMapper.retrieveN(params1);
					//
					boolean isTrue = false;
					for (BaseModel bm1 : ls) {
						ic = (InventoryCommodity) bm1;
						if (ic.getCommodityName().contains(is.getQueryKeyword())) {
							isTrue = true;
							System.out.println("搜索到的结果的字段的值" + ic.getCommodityName() + "包含字符串" + is.getQueryKeyword());
						}
					}
					Assert.assertTrue(isTrue == true);
					break;
				}
			}
		}
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Rollback(false)
	@Test(dataProvider = "retrieveNByFields")
	public void retrieveNByFieldsTest(String queryKeyword, int iPageIndex, int iPageSize, EnumErrorCode ec, int a) {
		Shared.printTestMethodStartInfo();

		Assert.assertEquals(retrieveNByFields(queryKeyword, iPageIndex, iPageSize, a), ec);
	}

	@Test
	public void retrieveNTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:根据输入_特殊字符商品名称查询盘点单");
		// 创建商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		comm1.setName("小辣椒_ka(hd" + System.currentTimeMillis());
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, comm1);
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		// 创建盘点单和盘点单从表
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet inventorySheet = BaseInventorySheetTest.createInventorySheet(is);

		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setCommodityID(commCreated.getID());
		ic.setBarcodeID(barcodes.getID());
		ic.setInventorySheetID(inventorySheet.getID());
		Map<String, Object> params = ic.getCreateParam(BaseBO.INVALID_CASE_ID, ic);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity icCreate = (InventoryCommodity) inventoryCommodityMapper.create(params);
		ic.setIgnoreIDInComparision(true);
		if (ic.compareTo(icCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(icCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String errorMsg = icCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");

		// 模糊查询盘点单
		InventorySheet is1 = new InventorySheet();
		is1.setStaffID(1);
		is1.setQueryKeyword("_ka(h");

		Map<String, Object> retrieveNParams1 = is1.getRetrieveNParam(BaseBO.CASE_InventorySheet_RetrieveNByNFields, is1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> poList1 = inventorySheetMapper.retrieveNByFields(retrieveNParams1);
		for (BaseModel bm : poList1) {
			InventorySheet is2 = (InventorySheet) bm;
			//
			// 查询当前盘点单的相关盘点单商品表
			InventoryCommodity ic1 = new InventoryCommodity();
			ic1.setInventorySheetID(is2.getID());
			Map<String, Object> param = ic1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ic1);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> ls = inventoryCommodityMapper.retrieveN(param);
			Assert.assertTrue(ls.size() > 0 && EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
			//
			boolean isTrue = false;
			for (BaseModel bm1 : ls) {
				ic1 = (InventoryCommodity) bm1;
				if (ic1.getInventorySheetID() == is2.getID()) {
					isTrue = true;
				}
			}
			Assert.assertTrue(isTrue == true);
		}
		Assert.assertTrue(poList1 != null && poList1.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveNParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除盘点单商品
		BaseInventorySheetTest.deleteInventoryCommodity(icCreate);
		BaseInventorySheetTest.deleteInventorySheet(inventorySheet);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);

	}
}
