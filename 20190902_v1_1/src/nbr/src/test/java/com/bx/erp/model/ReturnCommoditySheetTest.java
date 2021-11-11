package com.bx.erp.model;

import static org.testng.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ReturnCommoditySheet.EnumStatusReturnCommoditySheet;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseReturnCommoditySheetTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class ReturnCommoditySheetTest extends BaseModelTest {
    
	@Override
	protected BaseModel getMasterTableObject() throws CloneNotSupportedException, InterruptedException {
		ReturnCommoditySheet returnCommoditySheet = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		return returnCommoditySheet;
	}

	@Override
	protected BaseModel updateMasterTableObject(BaseModel master) throws CloneNotSupportedException, InterruptedException {
		master = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		return master;
	}

	@Override
	protected List<BaseModel> getSlaveTableObject() throws CloneNotSupportedException, InterruptedException {
		List<BaseModel> bmList = new ArrayList<BaseModel>();
		//
		ReturnCommoditySheetCommodity rcsc1 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		ReturnCommoditySheetCommodity rcsc2 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc2.setCommodityID(2); // 这里需要使两个从表对象的商品ID不同
		//
		bmList.add(rcsc1);
		bmList.add(rcsc2);
		return bmList;
	}

	@Override
	protected List<BaseModel> updateSlaveTableObject(List<BaseModel> slave) {
		slave.remove(0);
		return slave;
	}

	
	
	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void testCheckCreate() {
		Shared.printTestMethodStartInfo();

		ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
		returnCommoditySheet.setShopID(2);
		returnCommoditySheet.setProviderID(1);
		returnCommoditySheet.setStaffID(1);

		// 检查ProviderID
		returnCommoditySheet.setProviderID(0);
		assertEquals(returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_providerID);
		//
		returnCommoditySheet.setProviderID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_providerID);
		//
		returnCommoditySheet.setProviderID(1);
		assertEquals(returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID), "");

		// 检查StaffID
		returnCommoditySheet.setStaffID(0);
		assertEquals(returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_staffID);
		//
		returnCommoditySheet.setStaffID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_staffID);
		//
		returnCommoditySheet.setStaffID(1);
		assertEquals(returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID), "");
		// 检查shopID
		returnCommoditySheet.setShopID(-1);
		assertEquals(returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_shopID);
		returnCommoditySheet.setShopID(0);
		assertEquals(returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_shopID);
		returnCommoditySheet.setShopID(1);
		assertEquals(returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_shopID);
		returnCommoditySheet.setShopID(2);
		assertEquals(returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
		assertEquals(returnCommoditySheet.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		returnCommoditySheet.setID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheet.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		returnCommoditySheet.setID(1);
		assertEquals(returnCommoditySheet.checkRetrieve1(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void testCheckRetrieveN() throws ParseException {
		Shared.printTestMethodStartInfo();

		ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();

		// yyyy/MM/dd HH:mm:ss
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		returnCommoditySheet.setDate1(simpleDateFormat.parse("2019/05/22 10:26:30"));
		returnCommoditySheet.setDate2(simpleDateFormat.parse("2019/05/22 10:26:30"));
		returnCommoditySheet.setStatus(EnumStatusReturnCommoditySheet.ESRCS_Approved.getIndex());

		// 检验QueryKeyword
		returnCommoditySheet.setQueryKeyword(RandomStringUtils.randomAlphanumeric(40));
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_queryKeyword);
		//
		returnCommoditySheet.setQueryKeyword(null);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		returnCommoditySheet.setQueryKeyword("213123");
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");

		// 检查staffID
		returnCommoditySheet.setStaffID(-2);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Warehousing.field.getFIELD_NAME_staffID()));
		//
		returnCommoditySheet.setStaffID(0);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		returnCommoditySheet.setStaffID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		returnCommoditySheet.setStaffID(1);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");

		// 检查status
		returnCommoditySheet.setStatus(2);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_status);
		//
		returnCommoditySheet.setStatus(BaseAction.INVALID_STATUS);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		returnCommoditySheet.setStatus(EnumStatusReturnCommoditySheet.ESRCS_Approved.getIndex());
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");

		// 检查date1 and date2
		returnCommoditySheet.setDate1(null);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		returnCommoditySheet.setDate2(null);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");

		// 检查ProviderID
		returnCommoditySheet.setProviderID(-2);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Warehousing.field.getFIELD_NAME_providerID()));
		//
		returnCommoditySheet.setProviderID(0);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		returnCommoditySheet.setProviderID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		returnCommoditySheet.setProviderID(1);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");

		// 检查pageIndex、pageSize
		returnCommoditySheet.setPageIndex(-1);
		returnCommoditySheet.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		returnCommoditySheet.setPageIndex(BaseAction.PAGE_StartIndex);
		returnCommoditySheet.setPageSize(-1);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		returnCommoditySheet.setPageIndex(BaseAction.PAGE_StartIndex);
		returnCommoditySheet.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

		ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
		returnCommoditySheet.setID(1);
		returnCommoditySheet.setProviderID(1);

		// BaseBO.CASE_ApproveReturnCommoditySheet
		// 检查ID
		returnCommoditySheet.setID(0);
		assertEquals(returnCommoditySheet.checkUpdate(BaseBO.CASE_ApproveReturnCommoditySheet), FieldFormat.FIELD_ERROR_ID);
		//
		returnCommoditySheet.setID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheet.checkUpdate(BaseBO.CASE_ApproveReturnCommoditySheet), FieldFormat.FIELD_ERROR_ID);
		//
		returnCommoditySheet.setID(1);
		assertEquals(returnCommoditySheet.checkUpdate(BaseBO.CASE_ApproveReturnCommoditySheet), "");

		// BaseBO.INVALID_CASE_ID
		// 检查ID
		returnCommoditySheet.setID(0);
		assertEquals(returnCommoditySheet.checkUpdate(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		returnCommoditySheet.setID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheet.checkUpdate(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		returnCommoditySheet.setID(1);
		assertEquals(returnCommoditySheet.checkUpdate(BaseBO.INVALID_CASE_ID), "");

		// 检查ProviderID
		returnCommoditySheet.setProviderID(0);
		assertEquals(returnCommoditySheet.checkUpdate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_providerID);
		//
		returnCommoditySheet.setProviderID(BaseAction.INVALID_ID);
		assertEquals(returnCommoditySheet.checkUpdate(BaseBO.INVALID_CASE_ID), ReturnCommoditySheet.FIELD_ERROR_providerID);
		//
		returnCommoditySheet.setProviderID(1);
		assertEquals(returnCommoditySheet.checkUpdate(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
		String error = "";
		error = returnCommoditySheet.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
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
