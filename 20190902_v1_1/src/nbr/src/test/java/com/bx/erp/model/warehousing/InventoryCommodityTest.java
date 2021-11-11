package com.bx.erp.model.warehousing;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class InventoryCommodityTest {
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
		InventoryCommodity inventoryCommodity = new InventoryCommodity();

		// ID必须大于0
		inventoryCommodity.setID(BaseAction.INVALID_ID);
		assertEquals(inventoryCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		inventoryCommodity.setID(0);
		assertEquals(inventoryCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		inventoryCommodity.setID(1);
		assertEquals(inventoryCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), "");

		// 实盘数量必须为正负整数
		inventoryCommodity.setNoReal(0);
		assertEquals(inventoryCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		inventoryCommodity.setNoReal(-1);
		assertEquals(inventoryCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkCreate() {
		Shared.printTestMethodStartInfo();

		InventoryCommodity inventoryCommodity = new InventoryCommodity();
		inventoryCommodity.setInventorySheetID(1);
		// commodityID必须大于0
		inventoryCommodity.setCommodityID(BaseAction.INVALID_ID);
		assertEquals(inventoryCommodity.checkCreate(BaseBO.INVALID_CASE_ID), InventoryCommodity.FIELD_ERROR_commodityID);
		//
		inventoryCommodity.setCommodityID(0);
		assertEquals(inventoryCommodity.checkCreate(BaseBO.INVALID_CASE_ID), InventoryCommodity.FIELD_ERROR_commodityID);
		//
		inventoryCommodity.setCommodityID(1);
		assertEquals(inventoryCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// InventorySheetID必须大于0
		inventoryCommodity.setInventorySheetID(BaseAction.INVALID_ID);
		assertEquals(inventoryCommodity.checkCreate(BaseBO.INVALID_CASE_ID), InventoryCommodity.FIELD_ERROR_inventorySheetID);
		//
		inventoryCommodity.setInventorySheetID(0);
		assertEquals(inventoryCommodity.checkCreate(BaseBO.INVALID_CASE_ID), InventoryCommodity.FIELD_ERROR_inventorySheetID);
		//
		inventoryCommodity.setInventorySheetID(1);
		assertEquals(inventoryCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 实盘数量必须为正负整数
		inventoryCommodity.setNoReal(0);
		assertEquals(inventoryCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		inventoryCommodity.setNoReal(-1);
		assertEquals(inventoryCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// inventoryCommodity.setNoReal((int)1.21);
		// assertEquals(inventoryCommodity.checkCreate(BaseBO.INVALID_CASE_ID),
		// InventoryCommodity.FIELD_ERROR_noReal);
	}

	@Test
	public void checkDelete() {
		InventoryCommodity inventoryCommodity = new InventoryCommodity();
		// commodity <= 0
		assertEquals(inventoryCommodity.checkDelete(BaseBO.INVALID_CASE_ID), InventoryCommodity.FIELD_ERROR_inventorySheetID);
		//
		inventoryCommodity.setInventorySheetID(BaseAction.INVALID_ID);
		assertEquals(inventoryCommodity.checkDelete(BaseBO.INVALID_CASE_ID), InventoryCommodity.FIELD_ERROR_inventorySheetID);
		//
		inventoryCommodity.setInventorySheetID(1);
		assertEquals(inventoryCommodity.checkDelete(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieveN() {
		InventoryCommodity inventoryCommodity = new InventoryCommodity();
		
		// InventorySheetID必须大于0
		inventoryCommodity.setInventorySheetID(BaseAction.INVALID_ID);
		assertEquals(inventoryCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), InventoryCommodity.FIELD_ERROR_inventorySheetID);
		//
		inventoryCommodity.setInventorySheetID(0);
		assertEquals(inventoryCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), InventoryCommodity.FIELD_ERROR_inventorySheetID);
		//
		inventoryCommodity.setInventorySheetID(1);
		assertEquals(inventoryCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// 检查pageindex, pagesize
		assertEquals(inventoryCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		inventoryCommodity.setPageIndex(0);
		assertEquals(inventoryCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		inventoryCommodity.setPageIndex(1);
		inventoryCommodity.setPageSize(0);
		assertEquals(inventoryCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
	}

	@Test
	public void checkRetrieve1() {

	}

}
