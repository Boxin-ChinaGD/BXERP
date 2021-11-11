package com.bx.erp.model.warehousing;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class WarehousingCommodityTest {
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
		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		warehousingCommodity.setCommodityID(1);
		warehousingCommodity.setNO(1);
		warehousingCommodity.setPrice(3.2);
		warehousingCommodity.setAmount(2);

		// 检查warehouseID
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
		//
		warehousingCommodity.setWarehousingID(BaseAction.INVALID_ID);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
		//
		warehousingCommodity.setWarehousingID(1);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		// 检查NO
		warehousingCommodity.setNO(BaseAction.INVALID_NO);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_no);
		warehousingCommodity.setNO(0);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_no);
		warehousingCommodity.setNO(FieldFormat.MAX_OneCommodityNO + 1);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_no);
		//
		warehousingCommodity.setNO(1);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		warehousingCommodity.setNO(FieldFormat.MAX_OneCommodityNO);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		// 检查price
		warehousingCommodity.setPrice(-1);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_price);
		warehousingCommodity.setPrice(FieldFormat.MAX_OneCommodityPrice + 0.01);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_price);
		//
		warehousingCommodity.setPrice(0);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		warehousingCommodity.setPrice(FieldFormat.MAX_OneCommodityNO);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		// 检查amount
		warehousingCommodity.setAmount(-1);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_amount);
		//
		warehousingCommodity.setAmount(1);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		// 检查commodityID
		warehousingCommodity.setCommodityID(BaseAction.INVALID_ID);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_commodityID);
		//
		warehousingCommodity.setCommodityID(1);
		assertEquals(warehousingCommodity.checkUpdate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkCreate() {
		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		warehousingCommodity.setBarcodeID(1);
		warehousingCommodity.setCommodityID(1);
		warehousingCommodity.setNO(1);
		warehousingCommodity.setPrice(3.2);
		warehousingCommodity.setAmount(2);
		warehousingCommodity.setShelfLife(1);
		warehousingCommodity.setCommodityName("checkCreate");

		// 检查warehouseID
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
		//
		warehousingCommodity.setWarehousingID(BaseAction.INVALID_ID);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
		//
		warehousingCommodity.setWarehousingID(1);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 检查bardcodesID
		warehousingCommodity.setBarcodeID(BaseAction.INVALID_ID);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_barcodeID);
		//
		warehousingCommodity.setBarcodeID(0);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_barcodeID);
		//
		warehousingCommodity.setBarcodeID(1);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 检查commodityID
		warehousingCommodity.setCommodityID(BaseAction.INVALID_ID);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_commodityID);
		//
		warehousingCommodity.setCommodityID(0);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_commodityID);
		//
		warehousingCommodity.setCommodityID(1);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 检查NO
		warehousingCommodity.setNO(BaseAction.INVALID_NO);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_no);
		warehousingCommodity.setNO(0);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_no);
		warehousingCommodity.setNO(FieldFormat.MAX_OneCommodityNO + 1);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_no);
		//
		warehousingCommodity.setNO(1);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		warehousingCommodity.setNO(FieldFormat.MAX_OneCommodityNO);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 检查price
		warehousingCommodity.setPrice(-1);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_price);
		warehousingCommodity.setPrice(FieldFormat.MAX_OneCommodityPrice + 0.01);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_price);
		//
		warehousingCommodity.setPrice(0);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		warehousingCommodity.setPrice(FieldFormat.MAX_OneCommodityPrice);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 检查amount
		warehousingCommodity.setAmount(-1);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_amount);
		//
		warehousingCommodity.setAmount(1);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 检查shelfLife
		warehousingCommodity.setShelfLife(0);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_shelfLife);
		//
		warehousingCommodity.setShelfLife(1);
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// //
		// warehousingCommodity.setCommodityName(null);
		// assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID),
		// WarehousingCommodity.FIELD_ERROR_commodityName);
		// //
		// warehousingCommodity.setCommodityName(RandomStringUtils.randomAlphanumeric(40));
		// assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID),
		// WarehousingCommodity.FIELD_ERROR_commodityName);
		// //
		// warehousingCommodity.setCommodityName("");
		// assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID),
		// WarehousingCommodity.FIELD_ERROR_commodityName);
		// //
		// warehousingCommodity.setCommodityName(RandomStringUtils.randomAlphanumeric(10)
		// + "!@#");
		// assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID),
		// WarehousingCommodity.FIELD_ERROR_commodityName);
		// //
		// warehousingCommodity.setCommodityName(RandomStringUtils.randomAlphanumeric(10));
		// assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");

	}

	@Test
	public void checkDelete() {
		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		assertEquals(warehousingCommodity.checkDelete(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
		//
		warehousingCommodity.setWarehousingID(BaseAction.INVALID_ID);
		assertEquals(warehousingCommodity.checkDelete(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
		//
		warehousingCommodity.setWarehousingID(1);
		assertEquals(warehousingCommodity.checkDelete(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieveN() {
		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();

		// 检查warehouseID
		assertEquals(warehousingCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
		//
		warehousingCommodity.setWarehousingID(BaseAction.INVALID_ID);
		assertEquals(warehousingCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), WarehousingCommodity.FIELD_ERROR_warehousingID);
		//
		warehousingCommodity.setWarehousingID(1);
		assertEquals(warehousingCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// 检查pageindex, pagesize
		assertEquals(warehousingCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		warehousingCommodity.setPageIndex(0);
		assertEquals(warehousingCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		warehousingCommodity.setPageIndex(1);
		warehousingCommodity.setPageSize(0);
		assertEquals(warehousingCommodity.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
	}

	@Test
	public void checkRetrieve1() {
		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		warehousingCommodity.setBarcodeID(1);
		warehousingCommodity.setWarehousingID(1);
		warehousingCommodity.setCommodityID(1);
		warehousingCommodity.setNO(1);
		warehousingCommodity.setPrice(3.2);
		warehousingCommodity.setAmount(2);
		warehousingCommodity.setShelfLife(1);
		//
		warehousingCommodity.setCommodityName("case2用多包装商品创建有一个入库单商品");
		assertEquals(warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID), "");
	}
}
