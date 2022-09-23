package com.bx.erp.model;

import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;
import com.bx.erp.util.MD5Util;

public class PosTest {

	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	public static class DataInput {
		private static Pos getPos() {
			Pos p = new Pos();
			p.setSalt(MD5Util.MD5(Shared.PASSWORD_DEFAULT));
			p.setPos_SN("SN" + String.valueOf(System.currentTimeMillis()).substring(6));
			p.setShopID(1);
			p.setStatus(Pos.EnumStatusPos.ESP_Active.getIndex());
			p.setPasswordInPOS(UUID.randomUUID().toString().substring(1, 17));

			return p;
		}
	}

	@Test
	public void testCheckCreate() {
		Shared.printTestMethodStartInfo();

		Pos p = new Pos();
		String error = "";
		//
		p.setPos_SN(null);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_PosSN);
		p.setPos_SN("1234567890123456789012345678901234");
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_PosSN);
		p.setPos_SN("123544561");
		//
		p.setShopID(0);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_ShopID);
		p.setShopID(-99);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_ShopID);
		p.setShopID(1);
		//
		p.setSalt(null);
		error = p.checkCreate(BaseBO.CASE_Pos_Retrieve1BySN);
		Assert.assertEquals(error, Pos.FIELD_ERROR_Salt);
		p.setSalt("1234567890123456789012345678901234");
		error = p.checkCreate(BaseBO.CASE_Pos_Retrieve1BySN);
		Assert.assertEquals(error, Pos.FIELD_ERROR_Salt);
		p.setSalt("123544561");
		//
		p.setStatus(2);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_Status);
		p.setStatus(-99);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_Status);
		p.setStatus(1);
		//
		p.setPasswordInPOS(null);
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_PassWordInPos);
		p.setPasswordInPOS("1234567890123456789012345678901234");
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_PassWordInPos);
		p.setPasswordInPOS("132465401");
		//
		error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		String error = "";
		Pos p = new Pos();
		//
		// case default:
		p.setID(0);
		error = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(-99);
		error = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		error = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		p.setID(-1);
		error = p.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);

		// case CASE_Pos_Retrieve1BySN
		p.setPos_SN(null);
		error = p.checkRetrieve1(BaseBO.CASE_Pos_Retrieve1BySN);
		Assert.assertEquals(error, Pos.FIELD_ERROR_PosSN);
		p.setPos_SN("1234567890123456789012345678901234");
		error = p.checkRetrieve1(BaseBO.CASE_Pos_Retrieve1BySN);
		Assert.assertEquals(error, Pos.FIELD_ERROR_PosSN);
		//
		p.setPos_SN("13245633");
		error = p.checkRetrieve1(BaseBO.CASE_Pos_Retrieve1BySN);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();

		Pos p = DataInput.getPos();
		String error = "";
		//
		System.out.println("检查SN");
		p.setPos_SN("1234567890123456789012345678901234");
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_PosSN);
		p.setPos_SN(null);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		p.setPos_SN("12345678");
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		//
		System.out.println("检查ShopID");
		p.setShopID(-99);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Pos.FIELD_ERROR_ShopID_RN, error);
		p.setShopID(0);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Pos.FIELD_ERROR_ShopID_RN, error);
		p.setShopID(-1);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		p.setShopID(1);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		//
		p.setStatus(-99);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Pos.FIELD_ERROR_Status_RN, error);
		p.setStatus(2);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Pos.FIELD_ERROR_Status_RN, error);
		p.setStatus(-1);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		p.setStatus(1);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		p.setStatus(0);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		//
		p.setPageIndex(-666);
		p.setPageSize(10);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		p.setPageIndex(0);
		p.setPageSize(10);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		p.setPageIndex(1);
		p.setPageSize(10);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		//
		p.setPageSize(-666);
		p.setPageIndex(10);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		p.setPageSize(0);
		p.setPageIndex(10);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		p.setPageSize(1);
		p.setPageIndex(10);
		error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

		Pos p = new Pos();
		p.setPasswordInPOS("132465401");
		p.setSalt("123544561");
		String error = "";
		//
		// CASE default
		p.setID(0);
		error = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(-99);
		error = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(1);
		//
		p.setShopID(0);
		error = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_ShopID);
		p.setShopID(-99);
		error = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Pos.FIELD_ERROR_ShopID);
		p.setShopID(1);
		error = p.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// CASE CASE_POS_RecycleApp
		p.setID(0);
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(-99);
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(1);
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, "");
		//
		p.setPasswordInPOS(null);
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, Pos.FIELD_ERROR_PassWordInPos);
		p.setPasswordInPOS("");
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, Pos.FIELD_ERROR_PassWordInPos);
		p.setPasswordInPOS("1234567890123456789012345678901234");
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, Pos.FIELD_ERROR_PassWordInPos);
		p.setPasswordInPOS("132465401");
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, "");
		//
		p.setSalt(null);
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, Pos.FIELD_ERROR_Salt);
		p.setSalt("");
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, Pos.FIELD_ERROR_Salt);
		p.setSalt("1234567890123456789012345678901234");
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, Pos.FIELD_ERROR_Salt);
		p.setSalt("123544561");
		error = p.checkUpdate(BaseBO.CASE_POS_RecycleApp);
		Assert.assertEquals(error, "");
		// CASE CASE_POS_Reset
		p.setID(0);
		error = p.checkUpdate(BaseBO.CASE_POS_Reset);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(-99);
		error = p.checkUpdate(BaseBO.CASE_POS_Reset);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(1);
		error = p.checkUpdate(BaseBO.CASE_POS_Reset);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		Pos p = new Pos();
		String error = "";
		//
		p.setID(0);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		p.setID(-99);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//
		p.setID(1);
		error = p.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
