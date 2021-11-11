package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class BarcodesTest {

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
		// 纯英文
		Barcodes barcodes = new Barcodes();
		String error = "";
		//
		barcodes.setCommodityID(0);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_CommodityID);
		barcodes.setCommodityID(-99);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_CommodityID);
		barcodes.setCommodityID(1);
		//
		// 条形码传入其他字符
		barcodes.setBarcode("！@#￥%……&");
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		barcodes.setBarcode("1234567");
		//
		barcodes.setOperatorStaffID(0);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_StaffID);
		barcodes.setOperatorStaffID(-11);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_StaffID);
		barcodes.setOperatorStaffID(1);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// 专门检查barcodes字段
		barcodes.setID(1);
		String b = "zxcvbnm";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// 纯数字，长度为1
		b = "1234567";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// 英文和数字组合，长度为64
		b = "zxcvbnmasdfghjklqwertyuiop12345678901234567890123456789012345678";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		// 条形码传入其他字符
		b = "！@#￥%……&";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		b = "！@#￥%……&123";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		b = "！@#asd￥%……&";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		b = "！@#as爱d￥%你……123&";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		// 带有空格的组合
		b = "Asd 123";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		b = "Asd 1 23 ";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		b = "  Asd 1 23 ";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		// 长度不在7~64之间
		b = "";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		b = "zxcvbnmasdfghjklqwertyuiop12345678901234567890123456789012345678909asdfghj";
		barcodes.setBarcode(b);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		//
		barcodes.setBarcode("zxcvbnm");
		barcodes.setOperatorStaffID(1);
		error = barcodes.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		String error = "";
		Barcodes barcodes = new Barcodes();
		barcodes.setID(0);
		error = barcodes.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		barcodes.setID(-99);
		error = barcodes.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//
		barcodes.setID(1);
		error = barcodes.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();

		Barcodes barcodes = new Barcodes();
		String error = "";
		//
		barcodes.setCommodityID(-99);
		error = barcodes.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Barcodes.field.getFIELD_NAME_commodityID()));
		barcodes.setCommodityID(-1);
		//
		barcodes.setBarcode("！@#￥%……&");
		error = barcodes.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		barcodes.setBarcode("yy23423423 ffasdfa");
		error = barcodes.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		barcodes.setBarcode("");
		//
		barcodes.setPageIndex(-88);
		error = barcodes.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		barcodes.setPageIndex(0);
		error = barcodes.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		barcodes.setPageIndex(1);
		//
		barcodes.setPageSize(-88);
		error = barcodes.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		barcodes.setPageSize(0);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		barcodes.setPageSize(1);
		//
		error = barcodes.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

		Barcodes barcodes = new Barcodes();
		String error = "";
		//
		barcodes.setID(0);
		error = barcodes.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		barcodes.setID(-99);
		error = barcodes.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		barcodes.setID(1);
		//
		barcodes.setCommodityID(0);
		error = barcodes.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_CommodityID);
		barcodes.setCommodityID(-99);
		error = barcodes.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_CommodityID);
		barcodes.setCommodityID(1);
		//
		// 条形码传入其他字符
		barcodes.setBarcode("！@#￥%……&");
		error = barcodes.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_barcodes);
		barcodes.setBarcode("1234567");
		//
		barcodes.setOperatorStaffID(0);
		error = barcodes.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_StaffID);
		barcodes.setOperatorStaffID(-11);
		error = barcodes.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_StaffID);
		barcodes.setOperatorStaffID(1);
		//
		error = barcodes.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		Barcodes barcodes = new Barcodes();
		String error = "";
		barcodes.setID(0);
		error = barcodes.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		barcodes.setID(-99);
		error = barcodes.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		barcodes.setID(1);
		//
		barcodes.setOperatorStaffID(0);
		error = barcodes.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_StaffID);
		barcodes.setOperatorStaffID(-11);
		error = barcodes.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Barcodes.FIELD_ERROR_StaffID);
		barcodes.setOperatorStaffID(1);
		//
		error = barcodes.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
