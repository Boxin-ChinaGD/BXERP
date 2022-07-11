package com.bx.erp.model.warehousing;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModelTest;
import com.bx.erp.model.warehousing.InventorySheet.EnumScopeInventorySheet;
import com.bx.erp.model.warehousing.InventorySheet.EnumStatusInventorySheet;
import com.bx.erp.test.BaseInventorySheetTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class InventorySheetTest extends BaseModelTest {

	@Override
	protected BaseModel getMasterTableObject() throws CloneNotSupportedException, InterruptedException {
		InventorySheet inventorySheet = BaseInventorySheetTest.DataInput.getInventorySheet();
		return inventorySheet;
	}

	@Override
	protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
		master = BaseInventorySheetTest.DataInput.getInventorySheet();
		return master;
	}

	@Override
	protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException, InterruptedException {
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		//
		InventoryCommodity ic1 = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		InventoryCommodity ic2 = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		/*因为ic1,ic2具有相同主表，那么他们的commodityID必须是不一样的(一样的话也会导致compareTo通不过)*/
		ic1.setCommodityID(1);
		ic2.setCommodityID(2); // 这里需要使两个从表对象的商品ID不同
		//
		bmList.add(ic1);
		bmList.add(ic2);
		return bmList;
	}

	@Override
	protected List<BaseModel> updateSlaveTableObject(List<BaseModel> slave) {
		slave.remove(0);
		return slave;
	}

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void checkUpdate() {
		Shared.printTestMethodStartInfo();

		InventorySheet is = new InventorySheet();
		String err = "";

		System.out.println("---------------------------检查修改时盘点单备注的长度------------------------");

		// 盘点单备注长度不能大于100
		is.setRemark(RandomStringUtils.randomAlphanumeric(130));// 传入非法参数
		err = is.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, InventorySheet.FIELD_ERROR_remark);
		//
		is.setRemark("rightRemark");// 传入合法参数
		err = is.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		is.setRemark("");
		Assert.assertEquals(is.checkUpdate(BaseBO.INVALID_CASE_ID), "");

		// 正常输入
		is.setRemark(RandomStringUtils.randomAlphanumeric(100));
		err = is.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// ID必须大于0
		is.setApproverID(1);
		Assert.assertEquals(is.checkUpdate(BaseBO.CASE_ApproveInventorySheet), FieldFormat.FIELD_ERROR_ID);
		//
		is.setID(BaseAction.INVALID_ID);
		Assert.assertEquals(is.checkUpdate(BaseBO.CASE_ApproveInventorySheet), FieldFormat.FIELD_ERROR_ID);
		//
		is.setID(1);
		Assert.assertEquals(is.checkUpdate(BaseBO.CASE_ApproveInventorySheet), "");

		// approverID必须大于0
		is.setApproverID(0);
		Assert.assertEquals(is.checkUpdate(BaseBO.CASE_ApproveInventorySheet), InventorySheet.FIELD_ERROR_approverID);
		//
		is.setApproverID(BaseAction.INVALID_ID);
		Assert.assertEquals(is.checkUpdate(BaseBO.CASE_ApproveInventorySheet), InventorySheet.FIELD_ERROR_approverID);
		//
		is.setApproverID(1);
		Assert.assertEquals(is.checkUpdate(BaseBO.CASE_ApproveInventorySheet), "");
	}

	@Test
	public void checkCreate() {
		Shared.printTestMethodStartInfo();

		InventorySheet is = new InventorySheet();
		is.setStaffID(1);
		is.setWarehouseID(1);
		is.setScope(EnumScopeInventorySheet.ESIS_SpecifiedCategory.getIndex());
		is.setShopID(Shared.DEFAULT_Shop_ID);
		String err = "";

		System.out.println("---------------------------检查创建时盘点单备注的长度------------------------");

		// 盘点单备注长度不能大于100
		is.setRemark(RandomStringUtils.randomAlphanumeric(130));// 传入非法参数
		err = is.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, InventorySheet.FIELD_ERROR_remark);
		//
		is.setRemark("rightRemark");// 传入合法参数
		err = is.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		is.setRemark("");
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), "");

		// 正常输入
		is.setRemark(RandomStringUtils.randomAlphanumeric(100));
		err = is.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		// staffID必须大于0
		is.setStaffID(BaseAction.INVALID_ID);
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), InventorySheet.FIELD_ERROR_staffID);
		//
		is.setStaffID(0);
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), InventorySheet.FIELD_ERROR_staffID);
		//
		is.setStaffID(1);
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), "");

		// scope必须是0~3的正整数
		is.setScope(-1);
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), InventorySheet.FIELD_ERROR_scope);
		//
		is.setScope(4);
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), InventorySheet.FIELD_ERROR_scope);
		//
		is.setScope(EnumScopeInventorySheet.ESIS_SpecifiedCategory.getIndex());
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), "");
		
		//shopID必须大于0
		is.setShopID(BaseAction.INVALID_ID);
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), InventorySheet.FIELD_ERROR_shopID);
		//
		is.setShopID(0);
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), InventorySheet.FIELD_ERROR_shopID);
		//
		is.setShopID(1);
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), InventorySheet.FIELD_ERROR_VirtualShopID);
		//
		is.setShopID(2);
		Assert.assertEquals(is.checkCreate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkDelete() {
		InventorySheet is = new InventorySheet();
		// ID必须大于0
		assertEquals(is.checkDelete(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		is.setID(BaseAction.INVALID_ID);
		assertEquals(is.checkDelete(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		is.setID(1);
		assertEquals(is.checkDelete(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieveN() {
		InventorySheet is = new InventorySheet();
		// 只能查询状态在0~2的盘点单
		is.setStatus(-2);
		assertEquals(is.checkRetrieveN(BaseBO.INVALID_CASE_ID), InventorySheet.FIELD_ERROR_StatusForRN);
		//
		is.setStatus(EnumStatusInventorySheet.ESIS_Deleted.getIndex());
		assertEquals(is.checkRetrieveN(BaseBO.INVALID_CASE_ID), InventorySheet.FIELD_ERROR_StatusForRN);
		//
		is.setStatus(EnumStatusInventorySheet.ESIS_Approved.getIndex());
		assertEquals(is.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		is.setStatus(BaseAction.INVALID_STATUS);
		assertEquals(is.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		is.setShopID(-2);
		assertEquals(is.checkRetrieveN(BaseBO.INVALID_CASE_ID), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, InventorySheet.field.getFIELD_NAME_shopID()));
		is.setShopID(Shared.DEFAULT_Shop_ID);
		assertEquals(is.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// 检查pageindex, pagesize
		assertEquals(is.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		is.setPageIndex(0);
		assertEquals(is.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		is.setPageIndex(1);
		is.setPageSize(0);
		assertEquals(is.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		is.setPageIndex(BaseAction.PAGE_StartIndex);
		is.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(is.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// CASE_InventorySheet_RetrieveNByNFields
		is.setQueryKeyword(RandomStringUtils.randomAlphanumeric(50));
		assertEquals(is.checkRetrieveN(BaseBO.CASE_InventorySheet_RetrieveNByNFields), InventorySheet.FIELD_ERROR_queryKeyword);
		//
		is.setQueryKeyword("");
		assertEquals(is.checkRetrieveN(BaseBO.CASE_InventorySheet_RetrieveNByNFields), "");
		//
		is.setQueryKeyword(RandomStringUtils.randomAlphanumeric(10));
		assertEquals(is.checkRetrieveN(BaseBO.CASE_InventorySheet_RetrieveNByNFields), "");
	
	}

	@Test
	public void checkRetrieve1() {
		InventorySheet is = new InventorySheet();
		// ID必须大于0
		assertEquals(is.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		is.setID(BaseAction.INVALID_ID);
		assertEquals(is.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		is.setID(1);
		assertEquals(is.checkRetrieve1(BaseBO.INVALID_CASE_ID), "");
	}

	/*
	 * public void testCompareTo() throws CloneNotSupportedException,
	 * InterruptedException { caseLog("case1: 俩个对象数据一样 "); InventorySheet
	 * inventorySheetA_Case1 = DataInput.getInventorySheetAndInventoryCommodity();
	 * InventorySheet inventorySheetB_Case1 = (InventorySheet)
	 * inventorySheetA_Case1.clone();
	 * Assert.assertTrue(inventorySheetA_Case1.compareTo(inventorySheetB_Case1) ==
	 * 0, "比较失败"); // caseLog("case2: 俩个对象数据不一样 "); InventorySheet
	 * inventorySheetA_Case2 = DataInput.getInventorySheetAndInventoryCommodity();
	 * InventorySheet inventorySheetB_Case2 =
	 * DataInput.getInventorySheetAndInventoryCommodity();
	 * Assert.assertTrue(inventorySheetA_Case2.compareTo(inventorySheetB_Case2) !=
	 * 0, "比较失败"); // caseLog("case3: 俩个对象的主表信息相同，从表信息不相同"); InventorySheet
	 * inventorySheetA_Case3 = DataInput.getInventorySheetAndInventoryCommodity();
	 * InventorySheet inventorySheetB_Case3 = (InventorySheet)
	 * inventorySheetA_Case3.clone(); inventorySheetB_Case3.setListSlave1(null);
	 * Assert.assertTrue(inventorySheetA_Case3.compareTo(inventorySheetB_Case3) !=
	 * 0, "比较失败"); // caseLog("case4: 俩个对象的主表信息相同，从表信息不相同 但设置为不比较从表信息");
	 * InventorySheet inventorySheetA_Case4 =
	 * DataInput.getInventorySheetAndInventoryCommodity(); InventorySheet
	 * inventorySheetB_Case4 = (InventorySheet) inventorySheetA_Case4.clone();
	 * inventorySheetB_Case4.setListSlave1(null);
	 * inventorySheetA_Case4.setIgnoreSlaveListInComparision(true);
	 * Assert.assertTrue(inventorySheetA_Case4.compareTo(inventorySheetB_Case4) ==
	 * 0, "比较失败"); }
	 */

	@Test
	public void testCompareTo1() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case1();
	}

	@Test
	public void testCompareTo2() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case2();
	}

	@Test
	public void testCompareTo3() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case3();
	}

	@Test
	public void testCompareTo4() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case4();
	}

	@Test
	public void testCompareTo5() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case5();
	}

	@Test
	public void testCompareTo6() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case6();
	}

	@Test
	public void testCompareTo7() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case7();
	}

	@Test
	public void testCompareTo8() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case8();
	}

	@Test
	public void testCompareTo9() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case9();
	}

	@Test
	public void testCompareTo10() throws CloneNotSupportedException, InterruptedException {
		testCompareTo_Case10();
	}
}
