package com.bx.erp.model.warehousing;

import static org.testng.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModelTest;
import com.bx.erp.model.warehousing.Warehousing.EnumStatusWarehousing;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class WarehousingTest extends BaseModelTest {

	@Override
	protected BaseModel getMasterTableObject() throws CloneNotSupportedException, InterruptedException {
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		return warehousing;
	}

	@Override
	protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
		master = BaseWarehousingTest.DataInput.getWarehousing();
		return master;
	}

	@Override
	protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException, InterruptedException {
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		//
		WarehousingCommodity wc1 = BaseWarehousingTest.DataInput.getWarehousingCommodity();
		WarehousingCommodity wc2 = BaseWarehousingTest.DataInput.getWarehousingCommodity();
		wc2.setCommodityID(2); // 这里需要使两个从表对象的商品ID不同
		//
		bmList.add(wc1);
		bmList.add(wc2);
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
		Warehousing w = new Warehousing();
		w.setID(1);
		w.setApproverID(1);
		w.setWarehouseID(1);
		w.setProviderID(1);

		// 检验ID
		w.setID(0);
		assertEquals(w.checkUpdate(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(BaseAction.INVALID_ID);
		assertEquals(w.checkUpdate(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(1);
		assertEquals(w.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		// 检查warehouseID
		w.setWarehouseID(0);
		assertEquals(w.checkUpdate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_warehouseID);
		//
		w.setWarehouseID(BaseAction.INVALID_ID);
		assertEquals(w.checkUpdate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_warehouseID);
		//
		w.setWarehouseID(1);
		assertEquals(w.checkUpdate(BaseBO.INVALID_CASE_ID), "");

		// BaseBO.CASE_ApproveWarhousing
		w.setID(0);
		assertEquals(w.checkUpdate(BaseBO.CASE_ApproveWarhousing), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(BaseAction.INVALID_ID);
		assertEquals(w.checkUpdate(BaseBO.CASE_ApproveWarhousing), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(1);
		assertEquals(w.checkUpdate(BaseBO.CASE_ApproveWarhousing), "");
		// ApproverID检验
		w.setApproverID(0);
		assertEquals(w.checkUpdate(BaseBO.CASE_ApproveWarhousing), Warehousing.FIELD_ERROR_approverID);
		//
		w.setApproverID(BaseAction.INVALID_ID);
		assertEquals(w.checkUpdate(BaseBO.CASE_ApproveWarhousing), Warehousing.FIELD_ERROR_approverID);
		//
		w.setApproverID(1);
		assertEquals(w.checkUpdate(BaseBO.CASE_ApproveWarhousing), "");
		// ProviderID检查
		w.setProviderID(0);
		assertEquals(w.checkUpdate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_providerID);
		//
		w.setProviderID(BaseAction.INVALID_ID);
		assertEquals(w.checkUpdate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_providerID);
		//
		w.setProviderID(1);
		assertEquals(w.checkUpdate(BaseBO.INVALID_CASE_ID), "");
		// bToApproveStartValue的字段检验
		// w.setInt1(BaseAction.INVALID_ID);
		// assertEquals(w.checkUpdate(BaseBO.CASE_ApproveWarhousing),
		// Warehousing.FIELD_ERROR_bToApproveStartValue);
		// //
		// w.setInt1(2);
		// assertEquals(w.checkUpdate(BaseBO.CASE_ApproveWarhousing),
		// Warehousing.FIELD_ERROR_bToApproveStartValue);
		// //
		// w.setInt1(0);
		// assertEquals(w.checkUpdate(BaseBO.CASE_ApproveWarhousing), ""); //此参数已去除，无需验证
	}

	@Test
	public void checkCreate() {
		Warehousing w = new Warehousing();
		w.setStaffID(1);
		w.setProviderID(1);
		w.setPurchasingOrderID(1);
		w.setWarehouseID(1);
		w.setShopID(Shared.DEFAULT_Shop_ID);
		//ShopID检查
		w.setShopID(BaseAction.INVALID_ID);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_shopID);
		w.setShopID(0);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_shopID);
		w.setShopID(1);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_VirtualShopID);
		w.setShopID(2);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// ProviderID检查
		w.setProviderID(0);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_providerID);
		//
		w.setProviderID(BaseAction.INVALID_ID);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_providerID);
		//
		w.setProviderID(1);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// StaffID检查
		w.setStaffID(0);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_staffID);
		//
		w.setStaffID(BaseAction.INVALID_ID);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_staffID);
		//
		w.setStaffID(1);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// WarehouseID检查
		w.setWarehouseID(0);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_warehouseID);
		//
		w.setWarehouseID(BaseAction.INVALID_ID);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), Warehousing.FIELD_ERROR_warehouseID);
		//
		w.setWarehouseID(1);
		assertEquals(w.checkCreate(BaseBO.INVALID_CASE_ID), "");

	}

	@Test
	public void checkDelete() {
		Warehousing w = new Warehousing();
		assertEquals(w.checkDelete(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(BaseAction.INVALID_ID);
		assertEquals(w.checkDelete(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(1);
		assertEquals(w.checkDelete(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieveN() throws ParseException {
		Warehousing warehousing = new Warehousing();
		//
		warehousing.setPurchasingOrderID(-2);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByOrderID), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Warehousing.field.getFIELD_NAME_purchasingOrderID()));
		//
		warehousing.setPurchasingOrderID(0);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByOrderID), "");

		// BaseBO.CASE_RetrieveNWarhousingByFields yyyy/MM/dd HH:mm:ss
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		warehousing.setDate1(simpleDateFormat.parse("2019/05/22 10:26:30"));
		warehousing.setDate2(simpleDateFormat.parse("2019/05/22 10:26:30"));
		warehousing.setStatus(EnumStatusWarehousing.ESW_Approved.getIndex());
		// 检验queryKeyword
		warehousing.setQueryKeyword(RandomStringUtils.randomAlphanumeric(40));
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), Warehousing.FIELD_ERROR_queryKeyword);
		//
		warehousing.setQueryKeyword("");
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), "");
		//
		warehousing.setQueryKeyword("213123");
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), "");
		// 检查shopID
		warehousing.setShopID(-2);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Warehousing.field.getFIELD_NAME_shopID()));
		//
		warehousing.setShopID(BaseAction.INVALID_ID);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), "");
		// 检查staffID
		warehousing.setStaffID(-2);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Warehousing.field.getFIELD_NAME_staffID()));
		//
		warehousing.setStaffID(BaseAction.INVALID_ID);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), "");
		// 检查page indx
		warehousing.setPageIndex(-1);
		warehousing.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), FieldFormat.FIELD_ERROR_Paging);
		//
		warehousing.setPageIndex(BaseAction.PAGE_StartIndex);
		warehousing.setPageSize(-1);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), FieldFormat.FIELD_ERROR_Paging);
		//
		warehousing.setPageIndex(BaseAction.PAGE_StartIndex);
		warehousing.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), "");
		// 检查status
		warehousing.setStatus(2);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), Warehousing.FIELD_ERROR_status);
		//
		warehousing.setStatus(-1);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), "");
		//
		warehousing.setStatus(EnumStatusWarehousing.ESW_Approved.getIndex());
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), "");
		// 检查date1 and date2
		warehousing.setDate1(null);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), "");
		//
		warehousing.setDate2(null);
		assertEquals(warehousing.checkRetrieveN(BaseBO.CASE_RetrieveNWarhousingByFields), "");
		// BaseBO.INVALID_CASE_ID
		warehousing.setProviderID(-2);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Warehousing.field.getFIELD_NAME_providerID()));
		//
		warehousing.setProviderID(1);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// warehouseID
		warehousing.setWarehouseID(-2);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Warehousing.field.getFIELD_NAME_warehouseID()));
		//
		warehousing.setWarehouseID(1);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// staffID
		warehousing.setStaffID(-2);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Warehousing.field.getFIELD_NAME_staffID()));
		//
		warehousing.setStaffID(1);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// purchasingID
		warehousing.setPurchasingOrderID(-2);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Warehousing.field.getFIELD_NAME_purchasingOrderID()));
		//
		warehousing.setPurchasingOrderID(1);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// 检查page indx
		warehousing.setPageIndex(-1);
		warehousing.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		warehousing.setPageIndex(BaseAction.PAGE_StartIndex);
		warehousing.setPageSize(-1);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		warehousing.setPageIndex(BaseAction.PAGE_StartIndex);
		warehousing.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");

	}

	@Test
	public void checkRetrieve1() {
		Warehousing w = new Warehousing();
		assertEquals(w.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(BaseAction.INVALID_ID);
		assertEquals(w.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(1);
		assertEquals(w.checkRetrieve1(BaseBO.INVALID_CASE_ID), "");
	}

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
