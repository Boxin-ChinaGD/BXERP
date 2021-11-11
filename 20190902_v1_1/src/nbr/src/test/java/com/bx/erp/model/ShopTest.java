package com.bx.erp.model;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Shop;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class ShopTest {
	
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
		
		Shop shop = new Shop();
		shop.setName("博昕小卖部");
		shop.setAddress("华南植物园");
		shop.setKey("********************");
		shop.setRemark("紫薇小卖部");
		shop.setLongitude(100.000000);
		shop.setLatitude(100.0000000);
		shop.setStatus(0);
		shop.setCompanyID(1);
		shop.setDistrictID(1);
		shop.setBxStaffID(1);
		String error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试name
		shop.setName("123456781234567812345678博昕小卖部");
		error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_name);
		shop.setName("博昕小卖部");
		//测试address
		shop.setAddress("12345678123456781234567812345678华南植物园");
		error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_address);
		shop.setAddress("华南植物园");
		//测试key
		shop.setKey("12345678123456781234567812345678aa");
		error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_key);
		shop.setKey("********************");
		//测试Remark
		shop.setRemark("12345678123456781234567812345678123456781234567812345678123456781234567812345678"
				+ "12345678123456781234567812345678123456781234567812345678123456781234567812345678");
		error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_remark);
		shop.setRemark("紫薇小卖部");
		//测试Longitude
		shop.setLongitude(-1);
		error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_longitude);
		shop.setLongitude(100.000000);
		//测试Latitude
		shop.setLatitude(-1);
		error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_latitude);
		shop.setLatitude(100.0000000);
		//测试status
		shop.setStatus(BaseAction.INVALID_STATUS);
		error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_status);
		shop.setStatus(0);
		//测试companyID
		shop.setCompanyID(BaseAction.INVALID_ID);
		error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_companyID);
		shop.setCompanyID(1);
		//测试distrctID
		shop.setDistrictID(BaseAction.INVALID_ID);
		error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_districtID);
		shop.setDistrictID(1);
		//测试bxStaffID
		shop.setBxStaffID(BaseAction.INVALID_ID);
		error = shop.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_bxStaffID);
		shop.setBxStaffID(1);
	}
	
	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setID(1);
		String error = shop.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试ID
		shop.setID(BaseAction.INVALID_ID);
		error = shop.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
	}
	
	@Test
	public void testCheckRetrieveN_CASE1() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setID(1);
		shop.setFieldToCheckUnique(1);
		shop.setUniqueField("0123456789");
		String error = shop.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, "");
		//测试ID
		shop.setID(BaseAction.INVALID_ID);
		error = shop.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		shop.setID(1);
		//测试int1
		shop.setFieldToCheckUnique(0);
		error = shop.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, Shop.FIELD_ERROR_checkUniqueField);
		shop.setFieldToCheckUnique(1);
		//测试uniqueField
		shop.setUniqueField("01234567890123456789aa");
		error = shop.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error, Shop.FIELD_ERROR_name);
		shop.setUniqueField("0123456789");
	}
	
	@Test
	public void testCheckRetrieveN_CASE2() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setDistrictID(1);
		shop.setPageIndex(1);
		shop.setPageSize(10);
		String error = shop.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试区域ID
		shop.setDistrictID(BaseAction.INVALID_ID);
		error = shop.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		shop.setDistrictID(-222);
		error = shop.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_districtID);
		shop.setDistrictID(1);
		//测试PageIndex
		shop.setPageIndex(0);
		error = shop.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		shop.setPageIndex(2);
		//测试PageSize
		shop.setPageSize(0);
		error = shop.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_Paging);
		shop.setPageSize(5);
	}
	
	@Test
	public void testCheckRetrieveNEx() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		String error = shop.checkRetrieveNEx(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setID(1);
		shop.setName("博昕小卖部");
		shop.setAddress("华南植物园");
		shop.setKey("********************");
		shop.setRemark("紫薇小卖部");
		shop.setLongitude(100.000000);
		shop.setLatitude(100.0000000);
		shop.setDistrictID(1);
		String error = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试ID
		shop.setID(BaseAction.INVALID_ID);
		error = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		shop.setID(1);
		//测试name
		shop.setName("123456781234567812345678博昕小卖部");
		error = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_name);
		shop.setName("博昕小卖部");
		//测试address
		shop.setAddress("12345678123456781234567812345678华南植物园");
		error = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_address);
		shop.setAddress("华南植物园");
		//测试key
		shop.setKey("12345678123456781234567812345678aa");
		error = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_key);
		shop.setKey("********************");
		//测试Remark
		shop.setRemark("12345678123456781234567812345678123456781234567812345678123456781234567812345678"
				+ "12345678123456781234567812345678123456781234567812345678123456781234567812345678");
		error = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_remark);
		shop.setRemark("紫薇小卖部");
		//测试Longitude
		shop.setLongitude(-1);
		error = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_longitude);
		shop.setLongitude(100.000000);
		//测试Latitude
		shop.setLatitude(-1);
		error = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_latitude);
		shop.setLatitude(100.0000000);
		//测试distrctID
		shop.setDistrictID(BaseAction.INVALID_ID);
		error = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_districtID);
		shop.setDistrictID(1);
	}
		
	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setID(1);
		shop.setCompanyID(1);
		String error = shop.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//测试ID
//		shop.setID(BaseAction.INVALID_ID);
//		error = shop.checkDelete(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(error, FieldFormat.FIELD_ERROR_ID);
//		shop.setID(1);
		//测试int1(公司ID)
		shop.setCompanyID(BaseAction.INVALID_ID);
		error = shop.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Shop.FIELD_ERROR_companyID);
		shop.setCompanyID(1);
	}
	
	@Test
	public void testClone() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setID(123);
		shop.setName("张三");
		shop.setCompanyID(456);
		shop.setAddress("植物园");
		shop.setStatus(1);
		shop.setLongitude(1);
		shop.setLatitude(1);
		shop.setKey("1");
		shop.setBxStaffID(1);
		shop.setRemark("1");
		Shop shopClone = (Shop) shop.clone();
		Assert.assertEquals(shop.getID() == shopClone.getID(), true);
		Assert.assertEquals(shop.getName().equals(shopClone.getName()), true);
		Assert.assertEquals(shop.getCompanyID() == shopClone.getCompanyID(), true);
		Assert.assertEquals(shop.getAddress().equals(shopClone.getAddress()), true);
		Assert.assertEquals(shop.getStatus() == shopClone.getStatus(), true);
		Assert.assertEquals(shop.getLongitude() == shopClone.getLongitude(), true);
		Assert.assertEquals(shop.getLatitude() == shopClone.getLatitude(), true);
		Assert.assertEquals(shop.getKey().equals(shopClone.getKey()), true);
		Assert.assertEquals(shop.getBxStaffID() == shopClone.getBxStaffID(), true);
		Assert.assertEquals(shop.getRemark().equals(shopClone.getRemark()), true);
	}
	
	@Test
	public void testCompareTo() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setID(123);
		shop.setName("张三");
		shop.setCompanyID(456);
		shop.setAddress("植物园");
		shop.setStatus(1);
		shop.setLongitude(1);
		shop.setLatitude(1);
		shop.setKey("1");
		shop.setBxStaffID(1);
		shop.setRemark("1");
		Assert.assertEquals(shop.compareTo(shop) == 0, true);
	}
	
	@Test
	public void testGetRetrieveNParamEx() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setPageIndex(1);
		shop.setPageSize(3);
		Map<String, Object> shopRetrieveNParamEx = new HashMap<String, Object>();
		shopRetrieveNParamEx = shop.getRetrieveNParamEx(BaseBO.INVALID_CASE_ID, shop);
		Assert.assertEquals(shop.getPageIndex() == Integer.parseInt(String.valueOf(shopRetrieveNParamEx.get("iPageIndex"))), true);
		Assert.assertEquals(shop.getPageSize() == Integer.parseInt(String.valueOf(shopRetrieveNParamEx.get("iPageSize"))), true);
	}
	
	@Test
	public void testGetCreateParam() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setID(123);
		shop.setName("张三");
		shop.setCompanyID(456);
		shop.setAddress("植物园");
		shop.setStatus(1);
		shop.setLongitude(1);
		shop.setLatitude(1);
		shop.setKey("1");
		shop.setBxStaffID(1);
		shop.setRemark("1");
		Map<String, Object> shopGetCreateParam = new HashMap<String, Object>();
		shopGetCreateParam = shop.getCreateParam(BaseBO.INVALID_CASE_ID, shop);
		Assert.assertEquals(shop.getName() == shopGetCreateParam.get("name"), true);
		Assert.assertEquals(shop.getCompanyID() == Integer.parseInt(String.valueOf(shopGetCreateParam.get("companyID"))), true);
		Assert.assertEquals(shop.getAddress()== shopGetCreateParam.get("address"), true);
		Assert.assertEquals(shop.getStatus() == Integer.parseInt(String.valueOf(shopGetCreateParam.get("status"))), true);
		Assert.assertEquals(shop.getLongitude() == Double.parseDouble(String.valueOf(shopGetCreateParam.get("longitude"))), true);
		Assert.assertEquals(shop.getLatitude() == Double.parseDouble(String.valueOf(shopGetCreateParam.get("latitude"))), true);
		Assert.assertEquals(shop.getKey() == shopGetCreateParam.get("key"), true);
		Assert.assertEquals(shop.getBxStaffID()== Integer.parseInt(String.valueOf(shopGetCreateParam.get("bxStaffID"))), true);
		Assert.assertEquals(shop.getRemark() == shopGetCreateParam.get("remark"), true);
	}
	
	@Test
	public void testGetUpdateParam() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setID(123);
		shop.setName("张三");
		shop.setAddress("植物园");
		shop.setLongitude(1);
		shop.setLatitude(1);
		shop.setKey("1");
		shop.setRemark("1");
		Map<String, Object> shopGetUpdateParam = new HashMap<String, Object>();
		shopGetUpdateParam = shop.getUpdateParam (BaseBO.INVALID_CASE_ID, shop);
		Assert.assertEquals(shop.getID() == Integer.parseInt(String.valueOf(shopGetUpdateParam.get("ID"))), true);
		Assert.assertEquals(shop.getName() == shopGetUpdateParam.get("name"), true);
		Assert.assertEquals(shop.getAddress()== shopGetUpdateParam.get("address"), true);
		Assert.assertEquals(shop.getLongitude() == Double.parseDouble(String.valueOf(shopGetUpdateParam.get("longitude"))), true);
		Assert.assertEquals(shop.getLatitude() == Double.parseDouble(String.valueOf(shopGetUpdateParam.get("latitude"))), true);
		Assert.assertEquals(shop.getKey() == shopGetUpdateParam.get("key"), true);
		Assert.assertEquals(shop.getRemark() == shopGetUpdateParam.get("remark"), true);
	}
	
	@Test
	public void testGetDeleteParamEx() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setID(123);
		Map<String, Object> shopGetDeleteParamEx = new HashMap<String, Object>();
		shopGetDeleteParamEx = shop.getDeleteParamEx(BaseBO.INVALID_CASE_ID, shop);
		Assert.assertEquals(shop.getID() == Integer.parseInt(String.valueOf(shopGetDeleteParamEx.get("ID"))), true);
	}
	
	@Test
	public void testGetRetrieve1ParamEx() {
		Shared.printTestMethodStartInfo();
		
		Shop shop = new Shop();
		shop.setID(123);
		Map<String, Object> shopGetRetrieve1ParamEx = new HashMap<String, Object>();
		shopGetRetrieve1ParamEx = shop.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, shop);
		Assert.assertEquals(shop.getID() == Integer.parseInt(String.valueOf(shopGetRetrieve1ParamEx.get("ID"))), true);
	}
}
