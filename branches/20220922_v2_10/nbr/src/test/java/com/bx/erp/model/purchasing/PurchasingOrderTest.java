package com.bx.erp.model.purchasing;

import static org.testng.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModelTest;
import com.bx.erp.test.BasePurchasingOrderTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class PurchasingOrderTest extends BaseModelTest {

	@Override
	protected BaseModel getMasterTableObject() throws CloneNotSupportedException, InterruptedException {
		PurchasingOrder purchasingOrder = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		return purchasingOrder;
	}

	@Override
	protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
		master = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		return master;
	}

	@Override
	protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException {
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		//
		PurchasingOrderCommodity poc1 = BasePurchasingOrderTest.DataInput.getPurchasingOrderCommodity();
		PurchasingOrderCommodity poc2 = BasePurchasingOrderTest.DataInput.getPurchasingOrderCommodity();
		poc2.setCommodityID(2); // 这里需要使两个从表对象的商品ID不同
		//
		bmList.add(poc1);
		bmList.add(poc2);
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

		PurchasingOrder purchasingOrder = new PurchasingOrder();
		String error = "";

		// CASE_ApprovePurchasingOrder:
		// CheckID
		purchasingOrder.setID(-99);
		error = purchasingOrder.checkUpdate(BaseBO.CASE_ApprovePurchasingOrder);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		purchasingOrder.setID(0);
		error = purchasingOrder.checkUpdate(BaseBO.CASE_ApprovePurchasingOrder);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		purchasingOrder.setID(1);
		//
		// CheckApproverID
		purchasingOrder.setApproverID(-99);
		error = purchasingOrder.checkUpdate(BaseBO.CASE_ApprovePurchasingOrder);
		assertEquals(error, PurchasingOrder.FIELD_ERROR_ApproveID);
		purchasingOrder.setApproverID(0);
		error = purchasingOrder.checkUpdate(BaseBO.CASE_ApprovePurchasingOrder);
		assertEquals(error, PurchasingOrder.FIELD_ERROR_ApproveID);
		purchasingOrder.setApproverID(1);
		error = purchasingOrder.checkUpdate(BaseBO.CASE_ApprovePurchasingOrder);
		Assert.assertEquals(error, "");

		// CASE_UpdatePurchasingOrderStatus:
		// CheckID
		purchasingOrder.setID(-99);
		error = purchasingOrder.checkUpdate(BaseBO.CASE_UpdatePurchasingOrderStatus);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		purchasingOrder.setID(0);
		error = purchasingOrder.checkUpdate(BaseBO.CASE_UpdatePurchasingOrderStatus);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		purchasingOrder.setID(1);
		//
		// checkStatus
		purchasingOrder.setStatus(5);
		error = purchasingOrder.checkUpdate(BaseBO.CASE_UpdatePurchasingOrderStatus);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_Status);
		purchasingOrder.setStatus(-99);
		error = purchasingOrder.checkUpdate(BaseBO.CASE_UpdatePurchasingOrderStatus);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_Status);
		//
		purchasingOrder.setStatus(1);
		error = purchasingOrder.checkUpdate(BaseBO.CASE_UpdatePurchasingOrderStatus);
		Assert.assertEquals(error, "");

		// CASE_Update
		// CheckID
		purchasingOrder.setID(-99);
		error = purchasingOrder.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		purchasingOrder.setID(0);
		error = purchasingOrder.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		purchasingOrder.setID(1);
		//
		// CheckProviderID
		purchasingOrder.setProviderID(-1);
		error = purchasingOrder.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_ProviderID);
		purchasingOrder.setProviderID(0);
		error = purchasingOrder.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_ProviderID);
		purchasingOrder.setProviderID(1);
		//
		// checkRemark
		StringBuilder sb = new StringBuilder();
		String s = "1234567890";
		for (int i = 0; i < 13; i++) {
			sb.append(s);
		}
		purchasingOrder.setRemark(sb.toString());
		error = purchasingOrder.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_Remark);
		purchasingOrder.setRemark("哈哈哈");
		//
		error = purchasingOrder.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkRetrieveN() {
		Shared.printTestMethodStartInfo();

		PurchasingOrder purchasingOrder = new PurchasingOrder();
		String error = "";
		//
		// checkStatus
		purchasingOrder.setStatus(5);
		error = purchasingOrder.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_Status);
		purchasingOrder.setStatus(-99);
		error = purchasingOrder.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_Status);
		purchasingOrder.setStatus(BaseAction.INVALID_STATUS);
		//
		// checkQueryKeyword
		purchasingOrder.setQueryKeyword("4165465465465465654645645165465645456645645465645135465");
		error = purchasingOrder.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_QueryKeyword);
		purchasingOrder.setQueryKeyword("");
		//
		// checkStaffID
		purchasingOrder.setStaffID(-2);
		error = purchasingOrder.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, PurchasingOrder.field.getFIELD_NAME_staffID()));
		purchasingOrder.setStaffID(0);
		//
		// checkDate1AndDate2
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.TIME_FORMAT_ConfigGeneral);
		String date1 = "11:11:11";
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		purchasingOrder.setDate1(date);
		purchasingOrder.setDate2(date);
		//
		// checkPageIndex
		purchasingOrder.setPageIndex(-88);
		error = purchasingOrder.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		purchasingOrder.setPageIndex(0);
		error = purchasingOrder.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		purchasingOrder.setPageIndex(1);
		//
		// checkPageSize
		purchasingOrder.setPageSize(-88);
		error = purchasingOrder.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		purchasingOrder.setPageSize(0);
		error = purchasingOrder.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		purchasingOrder.setPageSize(1);
		//
		error = purchasingOrder.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkCreate() {
		Shared.printTestMethodStartInfo();

		PurchasingOrder purchasingOrder = new PurchasingOrder();
		String error = "";
		//
		// checkStaffID
		purchasingOrder.setStaffID(-1);
		error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_StaffID);
		purchasingOrder.setStaffID(0);
		error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_StaffID);
		purchasingOrder.setStaffID(1);
		//
		// checkProviderID
		purchasingOrder.setProviderID(-1);
		error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_ProviderID);
		purchasingOrder.setProviderID(0);
		error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_ProviderID);
		purchasingOrder.setProviderID(1);
		//
		// checkShopID
		purchasingOrder.setShopID(-1);
		error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_ShopID);
		purchasingOrder.setShopID(0);
		error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_ShopID);
		purchasingOrder.setShopID(1);
		error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_VirtualShopID);
		purchasingOrder.setShopID(2);
		error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// checkRemark
		StringBuilder sb = new StringBuilder();
		String s = "1234567890";
		for (int i = 0; i < 13; i++) {
			sb.append(s);
		}
		purchasingOrder.setRemark(sb.toString());
		error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, PurchasingOrder.FIELD_ERROR_Remark);
		purchasingOrder.setRemark("哈哈哈");
		//
		error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void checkRetrieve1() {
		Shared.printTestMethodStartInfo();
		PurchasingOrder p = new PurchasingOrder();

		String err = "";
		p.setID(-99);
		err = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		p.setID(0);
		err = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		err = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkDelete() {
		Shared.printTestMethodStartInfo();

		PurchasingOrder p = new PurchasingOrder();
		String err = "";
		//
		p.setID(-99);
		err = p.checkDelete(BaseBO.INVALID_CASE_ID);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		p.setID(0);
		err = p.checkDelete(BaseBO.INVALID_CASE_ID);
		assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		err = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
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
