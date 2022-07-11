package com.bx.erp.util;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.Vip;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.Coupon.EnumCouponType;
import com.bx.erp.model.Role.EnumTypeRole;
//import com.bx.erp.model.wx.coupon.CouponStatistics;
//import com.bx.erp.model.wx.coupon.WxCouponDetail.EnumCouponType;
import com.bx.erp.test.Shared;

public class FieldFormatTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	// @Test
	// public void checkIfMultiPackagingRefCommodityMultiple() {
	// Shared.printTestMethodStartInfo();
	// Assert.assertTrue(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("0"));
	// Assert.assertTrue(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("4564556165631"));
	//
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("
	// "));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("-11"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("11.1"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("fsjojsfojsfsojef"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("f93yhw2923"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("f93yhw
	// 2923"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("f93yhw
	// 2923 "));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("
	// f93yhw 2923"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("f93
	// yhw 2923"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("f93yhw中国2923"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("f93y你好hw中国2
	// 923"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple("f93y博昕科技hw中国2
	// 923"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple(",./2923"));
	// Assert.assertFalse(FieldFormat.checkIfMultiPackagingRefCommodityMultiple(null));
	//
	// }

	@Test
	public void checkBarcodeInMultiPackagingInfoTest() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkBarcodeInMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"));
		Assert.assertTrue(FieldFormat.checkBarcodeInMultiPackagingInfo(123456789 + "," + "222" + System.currentTimeMillis() % 1000000 + ",3332" + System.currentTimeMillis() % 1000000 + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				+ "commodityA" + System.currentTimeMillis() % 1000000 + "," + "commodityB" + System.currentTimeMillis() % 1000000 + "," + "commodityC" + System.currentTimeMillis() % 1000000 + ";"));

		Assert.assertFalse(FieldFormat.checkBarcodeInMultiPackagingInfo("1" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"));
		Assert.assertFalse(FieldFormat.checkBarcodeInMultiPackagingInfo(RandomStringUtils.randomAlphanumeric(65) + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"));
		Assert.assertFalse(FieldFormat.checkBarcodeInMultiPackagingInfo("1234_@#$56789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"));
	}

	@Test
	public void checkIfMultiPackagingRefCommodity() {
		Shared.printTestMethodStartInfo();
		Assert.assertTrue(FieldFormat.checkRefCommodity(2, 2, 2));
		Assert.assertTrue(FieldFormat.checkRefCommodity(2, 1000000000, 1000000000));
		Assert.assertTrue(FieldFormat.checkRefCommodity(2, 'a', 5));
		Assert.assertTrue(FieldFormat.checkRefCommodity(2, 5, 'a'));

		Assert.assertFalse(FieldFormat.checkRefCommodity(2, 0, 0));
		Assert.assertFalse(FieldFormat.checkRefCommodity(2, 0, 1));
		Assert.assertFalse(FieldFormat.checkRefCommodity(2, -5, 5));
		Assert.assertFalse(FieldFormat.checkRefCommodity(2, 5, -5));
	}

	@Test
	public void checkWarehouseName() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkWarehouseName("973456742923"));
		Assert.assertTrue(FieldFormat.checkWarehouseName("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkWarehouseName("哈哈哈哈哈哈哈哈哈"));
		Assert.assertTrue(FieldFormat.checkWarehouseName("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkWarehouseName("f93y你好hw中国2923"));
		Assert.assertTrue(FieldFormat.checkWarehouseName("f93y博昕科技hw中国2923"));

		Assert.assertFalse(FieldFormat.checkWarehouseName(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkWarehouseName(""));
		Assert.assertFalse(FieldFormat.checkWarehouseName(",./2923"));
		Assert.assertFalse(FieldFormat.checkWarehouseName(null));
	}

	@Test
	public void checkBrandName() {// 只允许中文、数字和英文，[1,20]字符
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkBrandName("12345123451234512345"));
		Assert.assertTrue(FieldFormat.checkBrandName("abcdeabcdeabcdeabcde"));
		Assert.assertTrue(FieldFormat.checkBrandName("中文中文中文中文中文"));
		Assert.assertTrue(FieldFormat.checkBrandName("1234中文中文中文abcd"));
		Assert.assertTrue(FieldFormat.checkBrandName("f93y你好hw中国2923"));
		Assert.assertTrue(FieldFormat.checkBrandName("f93y博昕科技hw中国2923"));

		Assert.assertFalse(FieldFormat.checkBrandName(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkBrandName(""));
		Assert.assertFalse(FieldFormat.checkBrandName(",./2923"));
		Assert.assertFalse(FieldFormat.checkBrandName("123451234512345123451"));
		Assert.assertFalse(FieldFormat.checkBrandName("abcdeabcdeabcdeabcdea"));
		Assert.assertFalse(FieldFormat.checkBrandName(null));
	}

	@Test
	public void checkProviderName() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkProviderName("973456742923"));
		Assert.assertTrue(FieldFormat.checkProviderName("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkProviderName("哈哈哈哈哈哈哈哈哈"));
		Assert.assertTrue(FieldFormat.checkProviderName("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkProviderName("f93y你好hw中国2923"));
		Assert.assertTrue(FieldFormat.checkProviderName("f93y博昕科技hw中国2923"));

		Assert.assertFalse(FieldFormat.checkProviderName(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkProviderName(""));
		Assert.assertFalse(FieldFormat.checkProviderName(",./2923"));
		Assert.assertFalse(FieldFormat.checkProviderName(null));
	}

	@Test
	public void checkCategoryName() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkCategoryName("973456742923"));
		Assert.assertTrue(FieldFormat.checkCategoryName("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkCategoryName("哈哈哈哈哈哈哈哈哈"));
		Assert.assertTrue(FieldFormat.checkCategoryName("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkCategoryName("f93y你好hw中国2923"));
		Assert.assertTrue(FieldFormat.checkCategoryName("f93y博昕科技hw中国2923"));

		Assert.assertFalse(FieldFormat.checkCategoryName(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkCategoryName(""));
		Assert.assertFalse(FieldFormat.checkCategoryName(",./2923"));
		Assert.assertFalse(FieldFormat.checkCategoryName(null));
	}

	@Test
	public void checkVipCategoryName() {// 只允许中文、数字和英文，[1, 30]]位
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkVipCategoryName("123456789012345678901234567890"));
		Assert.assertTrue(FieldFormat.checkVipCategoryName("ABCDEABCDEABCDEABCDEABCDEABCDE"));
		Assert.assertTrue(FieldFormat.checkVipCategoryName("哈哈哈哈哈哈哈哈哈"));
		Assert.assertTrue(FieldFormat.checkVipCategoryName("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkVipCategoryName("f93y你好hw中国2923"));
		Assert.assertTrue(FieldFormat.checkVipCategoryName("f93y博昕科技hw中国2923"));

		Assert.assertFalse(FieldFormat.checkVipCategoryName(" f93yhw 12345678"));
		Assert.assertFalse(FieldFormat.checkVipCategoryName("123456789012345678901234567890A"));
		Assert.assertFalse(FieldFormat.checkVipCategoryName("ABCDEABCDEABCDEABCDEABCDEABCDE1"));
		Assert.assertFalse(FieldFormat.checkVipCategoryName(""));
		Assert.assertFalse(FieldFormat.checkVipCategoryName(",./2923"));
		Assert.assertFalse(FieldFormat.checkVipCategoryName(null));
	}

	@Test
	public void checkMobile() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkMobile("19456742923"));
		// Assert.assertTrue(FieldFormat.checkMobile("0"));//这个目的是什么？？
		Assert.assertFalse(FieldFormat.checkMobile("159123456789"));
		Assert.assertFalse(FieldFormat.checkMobile("1347894561"));
		Assert.assertFalse(FieldFormat.checkMobile(" "));
		Assert.assertFalse(FieldFormat.checkMobile("-11"));
		Assert.assertFalse(FieldFormat.checkMobile("11.1"));
		Assert.assertFalse(FieldFormat.checkMobile("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkMobile("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkMobile("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkMobile("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkMobile(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkMobile("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkMobile("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkMobile("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkMobile("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkMobile(",./2923"));
		Assert.assertFalse(FieldFormat.checkMobile(null));
	}

	@Test
	public void checkHumanName() {// 只允许中文和英文，(0, 12]位
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkHumanName("中华人民中华人民中华人民"));
		Assert.assertTrue(FieldFormat.checkHumanName("许鹏"));
		Assert.assertTrue(FieldFormat.checkHumanName("TZQ"));
		Assert.assertTrue(FieldFormat.checkHumanName("谭志强TZQ"));
		Assert.assertTrue(FieldFormat.checkHumanName("TZQ谭志强"));
		Assert.assertTrue(FieldFormat.checkHumanName("abcdabcdabcd"));

		Assert.assertFalse(FieldFormat.checkHumanName(" "));
		Assert.assertFalse(FieldFormat.checkHumanName(" A"));
		Assert.assertFalse(FieldFormat.checkHumanName("A "));
		Assert.assertFalse(FieldFormat.checkHumanName(" 中"));
		Assert.assertFalse(FieldFormat.checkHumanName("中 "));
		Assert.assertFalse(FieldFormat.checkHumanName(" 1"));
		Assert.assertFalse(FieldFormat.checkHumanName("123456789012"));
		Assert.assertFalse(FieldFormat.checkHumanName("1234567890中国"));
		Assert.assertFalse(FieldFormat.checkHumanName("abcdabcdab12"));
		Assert.assertFalse(FieldFormat.checkHumanName("中华人民ABCD中华38"));
		Assert.assertFalse(FieldFormat.checkHumanName("中华人民中华人民中华38"));

		Assert.assertFalse(FieldFormat.checkHumanName("12345678901-"));
		Assert.assertFalse(FieldFormat.checkHumanName("12345678901_"));
		Assert.assertFalse(FieldFormat.checkHumanName("1234567890123"));
		Assert.assertFalse(FieldFormat.checkHumanName("中华人民中华人民中华人民1"));
		Assert.assertFalse(FieldFormat.checkHumanName(""));
		Assert.assertFalse(FieldFormat.checkHumanName(null));
	}

	@Test
	public void checkContactName() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkContactName("973456742923"));
		Assert.assertTrue(FieldFormat.checkContactName("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkContactName("哈哈哈哈哈哈哈哈哈"));
		Assert.assertTrue(FieldFormat.checkContactName("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkContactName("f93y你好hw中国2923"));
		Assert.assertTrue(FieldFormat.checkContactName("f93y博昕科技hw中国2923"));
		Assert.assertTrue(FieldFormat.checkContactName("f93y博昕科技hw 中国2923"));
		Assert.assertTrue(FieldFormat.checkContactName("f"));
		Assert.assertTrue(FieldFormat.checkContactName("1"));
		Assert.assertTrue(FieldFormat.checkContactName("啊"));
		Assert.assertTrue(FieldFormat.checkAddress(null));

		Assert.assertFalse(FieldFormat.checkContactName(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkContactName(" f"));
		Assert.assertFalse(FieldFormat.checkContactName(""));
		Assert.assertFalse(FieldFormat.checkContactName(RandomStringUtils.randomAlphanumeric(21)));
		Assert.assertFalse(FieldFormat.checkContactName(",./2923"));
	}

	@Test
	public void checkAddress() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkAddress("973456742923"));
		Assert.assertTrue(FieldFormat.checkAddress("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkAddress("哈哈哈哈哈哈哈哈哈"));
		Assert.assertTrue(FieldFormat.checkAddress("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkAddress("f93y你好hw中国2923"));
		Assert.assertTrue(FieldFormat.checkAddress("f93y博昕科技hw中国2923"));
		Assert.assertTrue(FieldFormat.checkAddress(null));

		Assert.assertFalse(FieldFormat.checkAddress(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkAddress(""));
		Assert.assertFalse(FieldFormat.checkAddress(",./2923"));
	}

	@Test
	public void checkShortName() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkShortName("973456742923"));
		Assert.assertTrue(FieldFormat.checkShortName("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkShortName("f93yhw2923"));
		Assert.assertTrue(FieldFormat.checkShortName("f93yhw 2923"));
		Assert.assertTrue(FieldFormat.checkShortName("f93yhw 2923 "));
		Assert.assertTrue(FieldFormat.checkShortName(" f93yhw 2923"));
		Assert.assertTrue(FieldFormat.checkShortName("f93 yhw 2923"));
		Assert.assertTrue(FieldFormat.checkShortName("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkShortName("f93y你好hw中国2 923"));
		Assert.assertTrue(FieldFormat.checkShortName("f93y博昕科技hw中国2 923"));

		Assert.assertFalse(FieldFormat.checkShortName(""));
		Assert.assertFalse(FieldFormat.checkShortName(",./2923"));
		Assert.assertFalse(FieldFormat.checkShortName(null));
	}

	@Test
	public void checkName() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkName("973456742923"));
		Assert.assertTrue(FieldFormat.checkName("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkName("f93yhw2923"));
		Assert.assertTrue(FieldFormat.checkName("f93yhw 2923"));
		Assert.assertTrue(FieldFormat.checkName("f93yhw 2923 "));
		Assert.assertTrue(FieldFormat.checkName(" f93yhw 2923"));
		Assert.assertTrue(FieldFormat.checkName("f93 yhw 2923"));
		Assert.assertTrue(FieldFormat.checkName("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkName("f93y你好hw中国2 923"));
		Assert.assertTrue(FieldFormat.checkName("f93y博昕科技hw中国2 923"));

		Assert.assertFalse(FieldFormat.checkName(""));
		Assert.assertFalse(FieldFormat.checkName(",./2923"));
		Assert.assertFalse(FieldFormat.checkName(null));
	}

	@Test
	public void checkMnemonicCode() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkMnemonicCode("973456742923"));
		Assert.assertTrue(FieldFormat.checkMnemonicCode("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkMnemonicCode("f93yhw2923"));

		Assert.assertFalse(FieldFormat.checkMnemonicCode(""));
		Assert.assertFalse(FieldFormat.checkMnemonicCode(",./2923"));
		Assert.assertFalse(FieldFormat.checkMnemonicCode("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkMnemonicCode("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkMnemonicCode(null));
	}

	@Test
	public void checkSpecification() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkSpecification("973456742923"));
		Assert.assertTrue(FieldFormat.checkSpecification("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkSpecification("f93yhw2923"));
		Assert.assertTrue(FieldFormat.checkSpecification("f93yhw 2923"));
		Assert.assertTrue(FieldFormat.checkSpecification("f93yhw 2923 "));
		Assert.assertTrue(FieldFormat.checkSpecification(" f93yhw 2923"));
		Assert.assertTrue(FieldFormat.checkSpecification("f93 yhw 2923"));
		Assert.assertTrue(FieldFormat.checkSpecification("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkSpecification("f93y你好hw中国2 923"));
		Assert.assertTrue(FieldFormat.checkSpecification("f93y博昕科技hw中国2 923"));

		Assert.assertFalse(FieldFormat.checkSpecification(""));
		Assert.assertFalse(FieldFormat.checkSpecification(",./2923"));
		Assert.assertFalse(FieldFormat.checkSpecification(null));
	}

	@Test
	public void checkPackageUnitID() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkPackageUnitID("973456742923"));
		Assert.assertTrue(FieldFormat.checkPackageUnitID("0"));

		Assert.assertFalse(FieldFormat.checkPackageUnitID(" "));
		Assert.assertFalse(FieldFormat.checkPackageUnitID("-11"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID("11.1"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkPackageUnitID(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID(",./2923"));
		Assert.assertFalse(FieldFormat.checkPackageUnitID(null));
	}

	@Test
	public void checkPurchasingUnit() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkPurchasingUnit("973456742923"));
		Assert.assertTrue(FieldFormat.checkPurchasingUnit("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93yhw2923"));
		Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93yhw 2923"));
		Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93yhw 2923 "));
		Assert.assertTrue(FieldFormat.checkPurchasingUnit(" f93yhw 2923"));
		Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93 yhw 2923"));
		Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93y你好hw中国2 923"));
		Assert.assertTrue(FieldFormat.checkPurchasingUnit("f93y博昕科技hw中国2 923"));
		Assert.assertTrue(FieldFormat.checkPurchasingUnit(null));

		Assert.assertFalse(FieldFormat.checkPurchasingUnit(""));
		Assert.assertFalse(FieldFormat.checkPurchasingUnit(",./2923"));
	}

	@Test
	public void checkProviderID() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkProviderID("973456742923"));

		Assert.assertFalse(FieldFormat.checkProviderID(" "));
		Assert.assertFalse(FieldFormat.checkProviderID("-11"));
		Assert.assertFalse(FieldFormat.checkProviderID("11.1"));
		Assert.assertFalse(FieldFormat.checkProviderID("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkProviderID("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkProviderID("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkProviderID("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkProviderID(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkProviderID("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkProviderID("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkProviderID("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkProviderID("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkProviderID(",./2923"));
		Assert.assertFalse(FieldFormat.checkProviderID(null));
	}

	@Test
	public void checkBrandID() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkBrandID("973456742923"));

		Assert.assertFalse(FieldFormat.checkBrandID(" "));
		Assert.assertFalse(FieldFormat.checkBrandID("-11"));
		Assert.assertFalse(FieldFormat.checkBrandID("11.1"));
		Assert.assertFalse(FieldFormat.checkBrandID("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkBrandID("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkBrandID("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkBrandID("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkBrandID(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkBrandID("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkBrandID("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkBrandID("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkBrandID("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkBrandID(",./2923"));
		Assert.assertFalse(FieldFormat.checkBrandID(null));
	}

	@Test
	public void checkCategoryID() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkCategoryID("973456742923"));

		Assert.assertFalse(FieldFormat.checkCategoryID("-973456742923"));
		Assert.assertFalse(FieldFormat.checkCategoryID("11.1"));
		Assert.assertFalse(FieldFormat.checkCategoryID(" "));
		Assert.assertFalse(FieldFormat.checkCategoryID("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkCategoryID("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkCategoryID("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkCategoryID("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkCategoryID(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkCategoryID("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkCategoryID("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkCategoryID("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkCategoryID("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkCategoryID(",./2923"));
		Assert.assertFalse(FieldFormat.checkCategoryID(null));
	}

	@Test
	public void checkCommodityPrice() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkCommodityPrice(0.0d));
		Assert.assertTrue(FieldFormat.checkCommodityPrice(50.00001d));
		Assert.assertTrue(FieldFormat.checkCommodityPrice(FieldFormat.MAX_OneCommodityPrice));
		
		Assert.assertFalse(FieldFormat.checkCommodityPrice(-1));
		Assert.assertFalse(FieldFormat.checkCommodityPrice(FieldFormat.MAX_OneCommodityPrice + 0.00001d));
	}
	
	@Test
	public void checkRatioGrossMargin() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkRatioGrossMargin("973456742923"));
		Assert.assertTrue(FieldFormat.checkRatioGrossMargin("11.1"));
		Assert.assertTrue(FieldFormat.checkRatioGrossMargin("0.05"));
		Assert.assertTrue(FieldFormat.checkRatioGrossMargin("11.111111"));

		Assert.assertFalse(FieldFormat.checkRatioGrossMargin("11.1111111"));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin(""));
		Assert.assertFalse(FieldFormat.checkRatioGrossMargin(",./2923"));
	}

	@Test
	public void checkPurchaseFlag() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkPurchaseFlag("0"));
		Assert.assertTrue(FieldFormat.checkPurchaseFlag("11"));
		Assert.assertTrue(FieldFormat.checkPurchaseFlag(null));

		Assert.assertFalse(FieldFormat.checkPurchaseFlag(" "));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag("-11"));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag("11.1"));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkPurchaseFlag(",./2923"));
	}

	@Test
	public void checkRefCommodityID() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkRefCommodityID("973456742923"));
		Assert.assertTrue(FieldFormat.checkRefCommodityID("0"));

		Assert.assertFalse(FieldFormat.checkRefCommodityID(" "));
		Assert.assertFalse(FieldFormat.checkRefCommodityID("-11"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID("11.1"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkRefCommodityID(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID(",./2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityID(null));
	}

	@Test
	public void checkTag() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkTag("973456742923"));
		Assert.assertTrue(FieldFormat.checkTag("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkTag("哈哈哈哈哈哈哈哈哈"));
		Assert.assertTrue(FieldFormat.checkTag("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkTag("f93y你好hw中国2923"));
		Assert.assertTrue(FieldFormat.checkTag("f93y博昕科技hw中国2923"));

		Assert.assertFalse(FieldFormat.checkTag(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkTag(""));
		Assert.assertFalse(FieldFormat.checkTag(",./2923"));
		Assert.assertFalse(FieldFormat.checkTag(null));
	}

	@Test
	public void checkNO() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkNO("973456"));
		Assert.assertTrue(FieldFormat.checkNO("0"));

		Assert.assertFalse(FieldFormat.checkNO("-973"));
		Assert.assertFalse(FieldFormat.checkNO(" "));
		Assert.assertFalse(FieldFormat.checkNO("11.1"));
		Assert.assertFalse(FieldFormat.checkNO("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkNO("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkNO("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkNO("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkNO(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkNO("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkNO("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkNO(",./2923"));
		Assert.assertFalse(FieldFormat.checkNO(null));
	}

	@Test
	public void checkCommodityNO() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkCommodityNO(FieldFormat.MAX_OneCommodityNO));
		Assert.assertTrue(FieldFormat.checkCommodityNO(1));
		
		Assert.assertFalse(FieldFormat.checkCommodityNO(FieldFormat.MAX_OneCommodityNO + 1));
		Assert.assertFalse(FieldFormat.checkCommodityNO(0));
		Assert.assertFalse(FieldFormat.checkCommodityNO(-1));
	}
	
	@Test
	public void checkSenderID() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkSenderID("973456"));
		Assert.assertTrue(FieldFormat.checkSenderID("0"));

		Assert.assertFalse(FieldFormat.checkSenderID("-973"));
		Assert.assertFalse(FieldFormat.checkSenderID(" "));
		Assert.assertFalse(FieldFormat.checkSenderID("11.1"));
		Assert.assertFalse(FieldFormat.checkSenderID("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkSenderID("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkSenderID("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkSenderID("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkSenderID(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkSenderID("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkSenderID("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkSenderID(",./2923"));
		Assert.assertFalse(FieldFormat.checkSenderID(null));
	}

	@Test
	public void checkInventoryCommodityNoReal() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkInventoryCommodityNoReal("973456"));
		Assert.assertTrue(FieldFormat.checkInventoryCommodityNoReal("0"));
		Assert.assertTrue(FieldFormat.checkInventoryCommodityNoReal("-973"));

		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal(" "));
		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("11.1"));
		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal(",./2923"));
		Assert.assertFalse(FieldFormat.checkInventoryCommodityNoReal(null));
	}

	@Test
	public void checkNOAccumulated() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkNOAccumulated("973456742923"));
		Assert.assertTrue(FieldFormat.checkNOAccumulated("0"));

		Assert.assertFalse(FieldFormat.checkNOAccumulated(" "));
		Assert.assertFalse(FieldFormat.checkNOAccumulated("-11"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated("11.1"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkNOAccumulated(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated(",./2923"));
		Assert.assertFalse(FieldFormat.checkNOAccumulated(null));
	}

	@Test
	public void checkReturnDays() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkReturnDays("973456742923"));
		Assert.assertTrue(FieldFormat.checkReturnDays("0"));

		Assert.assertFalse(FieldFormat.checkReturnDays(" "));
		Assert.assertFalse(FieldFormat.checkReturnDays("-11"));
		Assert.assertFalse(FieldFormat.checkReturnDays("11.1"));
		Assert.assertFalse(FieldFormat.checkReturnDays("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkReturnDays("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkReturnDays("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkReturnDays("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkReturnDays(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkReturnDays("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkReturnDays("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkReturnDays("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkReturnDays(",./2923"));
		Assert.assertFalse(FieldFormat.checkReturnDays(null));
	}

	@Test
	public void checkRefCommodityMultiple() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkRefCommodityMultiple("973456742923"));
		Assert.assertTrue(FieldFormat.checkRefCommodityMultiple("0"));

		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple(" "));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("-11"));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("11.1"));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple(",./2923"));
		Assert.assertFalse(FieldFormat.checkRefCommodityMultiple(null));
	}

	@Test
	public void checkShelfLife() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkShelfLife("11"));
		Assert.assertTrue(FieldFormat.checkShelfLife(null));

		Assert.assertFalse(FieldFormat.checkShelfLife("0"));
		Assert.assertFalse(FieldFormat.checkShelfLife(" "));
		Assert.assertFalse(FieldFormat.checkShelfLife("-11"));
		Assert.assertFalse(FieldFormat.checkShelfLife("11.1"));
		Assert.assertFalse(FieldFormat.checkShelfLife("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkShelfLife("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkShelfLife("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkShelfLife("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkShelfLife(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkShelfLife("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkShelfLife("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkShelfLife("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkShelfLife("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkShelfLife(",./2923"));
	}

	@Test
	public void checkRuleOfPoint() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkRuleOfPoint("11"));
		Assert.assertTrue(FieldFormat.checkRuleOfPoint(null));

		Assert.assertFalse(FieldFormat.checkRuleOfPoint(" "));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint("-11"));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint("11.1"));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkRuleOfPoint(",./2923"));
	}

	@Test
	public void checkDate() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkDate("2016-12-6 1:01:01"));
		Assert.assertTrue(FieldFormat.checkDate("2016/12/6 1:01:01"));

		Assert.assertFalse(FieldFormat.checkDate("2016/12/6"));
		Assert.assertFalse(FieldFormat.checkDate(null));
	}

	@Test
	public void checkRawPassword() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkRawPassword("ASDF12"));
		Assert.assertTrue(FieldFormat.checkRawPassword("#@%@~.+ 1234567"));
		Assert.assertTrue(FieldFormat.checkRawPassword("#@% @~.+ 12347"));
		Assert.assertTrue(FieldFormat.checkRawPassword("#dfg.+ SD34567"));
		Assert.assertTrue(FieldFormat.checkRawPassword("!@# $%+."));

		Assert.assertFalse(FieldFormat.checkRawPassword("sdf23"));
		Assert.assertFalse(FieldFormat.checkRawPassword("12354678545ds@#fd"));
		Assert.assertFalse(FieldFormat.checkRawPassword(" #@% aG~.+ 147"));
		Assert.assertFalse(FieldFormat.checkRawPassword("#@% aG~.+ 147 "));
		Assert.assertFalse(FieldFormat.checkRawPassword(null));
		Assert.assertFalse(FieldFormat.checkRawPassword(""));
		Assert.assertFalse(FieldFormat.checkRawPassword("       "));
		Assert.assertFalse(FieldFormat.checkRawPassword("!@#123中文Abc$%^.+-"));
		Assert.assertFalse(FieldFormat.checkRawPassword("中!@#123Abc$%^.+-"));
		Assert.assertFalse(FieldFormat.checkRawPassword("!@#123Abc$%^.+-文"));
		Assert.assertFalse(FieldFormat.checkRawPassword("中!@#123Abc$%^.+-文"));
		Assert.assertFalse(FieldFormat.checkRawPassword(" !@#123Abc$%^.+- "));
	}

	@Test
	public void checkSalt() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkSalt("B1AFC07474C37C5AEC4199ED28E09705"));
		Assert.assertTrue(FieldFormat.checkSalt("11111111111111111111111111111111"));
		Assert.assertTrue(FieldFormat.checkSalt("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

		Assert.assertFalse(FieldFormat.checkSalt("1AFC07474C37C5AEC4199ED28E09705"));
		Assert.assertFalse(FieldFormat.checkSalt("B1AFC07474C37C5AEC4199ED28E097051"));
		Assert.assertFalse(FieldFormat.checkSalt("a1AFC07474C37C5AEC4199ED28E09705"));
		Assert.assertFalse(FieldFormat.checkSalt(null));
	}

	@Test
	public void checkWeChat() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkWeChat("as545"));
		Assert.assertTrue(FieldFormat.checkWeChat("asd_sd32"));
		Assert.assertTrue(FieldFormat.checkWeChat("asdte-kh53"));
		Assert.assertTrue(FieldFormat.checkWeChat("asdte-11"));
		Assert.assertTrue(FieldFormat.checkWeChat("asdte_11"));
		Assert.assertTrue(FieldFormat.checkWeChat("12345678901234567890"));

		Assert.assertFalse(FieldFormat.checkWeChat("1231+11"));
		Assert.assertFalse(FieldFormat.checkWeChat("1234"));
		Assert.assertFalse(FieldFormat.checkWeChat("123456789012345678901"));
		Assert.assertFalse(FieldFormat.checkWeChat("12"));
		Assert.assertFalse(FieldFormat.checkWeChat("#ljljr"));
		Assert.assertFalse(FieldFormat.checkWeChat("sdsd"));
		Assert.assertFalse(FieldFormat.checkWeChat("as看了就开了个"));
		Assert.assertFalse(FieldFormat.checkWeChat(""));
		try {
			FieldFormat.checkWeChat(null);
			Assert.assertTrue(false);
		} catch (NullPointerException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void checkICID() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkICID("44152219940925821X"));
		Assert.assertTrue(FieldFormat.checkICID("44152219990925821x"));
		Assert.assertTrue(FieldFormat.checkICID("441522199409258216"));
		Assert.assertTrue(FieldFormat.checkICID(null));

		Assert.assertFalse(FieldFormat.checkICID("44152219940925821"));
		Assert.assertFalse(FieldFormat.checkICID("441522199409258219x"));
		Assert.assertFalse(FieldFormat.checkICID("441522199409258X1"));
		Assert.assertFalse(FieldFormat.checkICID("    "));
		Assert.assertFalse(FieldFormat.checkICID("asd123fgh456jkl789"));
		Assert.assertFalse(FieldFormat.checkICID("测试12345678909876"));
	}

	@Test
	public void checkProviderAddress() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkProviderAddress("973456742923"));
		Assert.assertTrue(FieldFormat.checkProviderAddress("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkProviderAddress("哈哈哈哈哈哈哈哈哈"));
		Assert.assertTrue(FieldFormat.checkProviderAddress("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkProviderAddress("f93y你好hw中国2923"));
		Assert.assertTrue(FieldFormat.checkProviderAddress("f93y博昕科技hw中国2923"));
		Assert.assertTrue(FieldFormat.checkProviderAddress(",./2923"));
		Assert.assertTrue(FieldFormat.checkProviderAddress("f93yh#w 2923"));
		Assert.assertTrue(FieldFormat.checkProviderAddress("f"));
		Assert.assertTrue(FieldFormat.checkProviderAddress("1"));
		Assert.assertTrue(FieldFormat.checkProviderAddress("啊"));

		Assert.assertFalse(FieldFormat.checkProviderAddress(" a"));
		Assert.assertFalse(FieldFormat.checkProviderAddress(" 441522199409258X1 "));
		Assert.assertFalse(FieldFormat.checkProviderAddress(RandomStringUtils.randomAlphanumeric(51)));
		Assert.assertFalse(FieldFormat.checkProviderAddress(" "));
		Assert.assertFalse(FieldFormat.checkProviderAddress(null));
	}

	@Test
	public void checkCommodityName() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkCommodityName("973456742923"));
		Assert.assertTrue(FieldFormat.checkCommodityName("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkCommodityName("哈哈哈哈哈哈哈哈哈"));
		Assert.assertTrue(FieldFormat.checkCommodityName("012345678901234567890123456789XX"));
		Assert.assertTrue(FieldFormat.checkCommodityName("f93yhw中国2923"));
		Assert.assertTrue(FieldFormat.checkCommodityName("f93y你好hw中国2923"));
		Assert.assertTrue(FieldFormat.checkCommodityName("f93y博昕科技hw中国2923"));
		Assert.assertTrue(FieldFormat.checkCommodityName("f93y博昕科技hw 中国2923"));
		Assert.assertTrue(FieldFormat.checkCommodityName("f"));
		Assert.assertTrue(FieldFormat.checkCommodityName("1"));
		Assert.assertTrue(FieldFormat.checkCommodityName("啊"));
		Assert.assertTrue(FieldFormat.checkCommodityName("fdg( )（c）-—_啊"));
		
		Assert.assertTrue(FieldFormat.checkCommodityName("*“”、$#/"));

		Assert.assertFalse(FieldFormat.checkCommodityName(null));
		Assert.assertFalse(FieldFormat.checkCommodityName(""));
		Assert.assertFalse(FieldFormat.checkCommodityName("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkCommodityName(",./2923"));
		Assert.assertFalse(FieldFormat.checkCommodityName(" f"));
		Assert.assertFalse(FieldFormat.checkCommodityName(" "));
		Assert.assertFalse(FieldFormat.checkCommodityName("012345678901234567890123456789XXW"));
		Assert.assertFalse(FieldFormat.checkCommodityName(RandomStringUtils.randomAlphanumeric(33)));
	}

	@Test
	public void checkShopName() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkShopName("01234567890123456789")); // 20个
		Assert.assertTrue(FieldFormat.checkShopName("博昕"));
		Assert.assertTrue(FieldFormat.checkShopName("博 昕"));
		Assert.assertTrue(FieldFormat.checkShopName("博   昕"));
		Assert.assertTrue(FieldFormat.checkShopName("中"));
		Assert.assertTrue(FieldFormat.checkShopName("2"));

		Assert.assertFalse(FieldFormat.checkShopName("012345678901234567891")); // 21个
		Assert.assertFalse(FieldFormat.checkShopName("0123 "));
		Assert.assertFalse(FieldFormat.checkShopName(" 0123"));
		Assert.assertFalse(FieldFormat.checkShopName(" 0123 "));
		Assert.assertFalse(FieldFormat.checkShopName(""));
		Assert.assertFalse(FieldFormat.checkShopName(" "));
		Assert.assertFalse(FieldFormat.checkShopName(null));
	}

	@Test
	public void checkShopAddress() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkShopAddress("012345678901234567890123456789")); // 20个
		Assert.assertTrue(FieldFormat.checkShopAddress("博昕"));
		Assert.assertTrue(FieldFormat.checkShopAddress("博 昕"));
		Assert.assertTrue(FieldFormat.checkShopAddress("博   昕"));
		Assert.assertTrue(FieldFormat.checkShopAddress("中"));
		Assert.assertTrue(FieldFormat.checkShopAddress("2"));

		Assert.assertFalse(FieldFormat.checkShopAddress("0123456789012345678901234567891")); // 21个
		Assert.assertFalse(FieldFormat.checkShopAddress("0123 "));
		Assert.assertFalse(FieldFormat.checkShopAddress(" 0123"));
		Assert.assertFalse(FieldFormat.checkShopAddress(" 0123 "));
		Assert.assertFalse(FieldFormat.checkShopAddress(""));
		Assert.assertFalse(FieldFormat.checkShopAddress(" "));
		Assert.assertFalse(FieldFormat.checkShopAddress(null));
	}

	@Test
	public void checkCompanyKey() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkCompanyKey("B1AFC07474C37C5AEC4199ED28E09705"));
		Assert.assertTrue(FieldFormat.checkCompanyKey("12345678123456781234567812345678"));

		Assert.assertFalse(FieldFormat.checkCompanyKey("123456781234567812345678123456789"));
		Assert.assertFalse(FieldFormat.checkCompanyKey("12345678123456781234567812345678E"));
		Assert.assertFalse(FieldFormat.checkCompanyKey("6789"));
		Assert.assertFalse(FieldFormat.checkCompanyKey(" "));
		Assert.assertFalse(FieldFormat.checkCompanyKey("123 456781234567812345678123456789"));
		Assert.assertFalse(FieldFormat.checkCompanyKey("中国56781234567812345678123456789"));
		Assert.assertFalse(FieldFormat.checkCompanyKey(null));
	}

	@Test
	public void checkBusinessLicenseSN() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("123456789012345"));
		Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("333456789012345"));
		Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("123456789012345123"));
		Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("333C456B789A345"));
		Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("12345ABC9012345123"));
		Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("ASDFGHJKLQWERTY"));
		Assert.assertTrue(FieldFormat.checkBusinessLicenseSN("ASDFGHJKLKJHGFDSAQ"));

		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("-333456789012345"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("-33345678901234"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("1234567891234"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("12345678912345678"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("123456789123456789123456"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN(" "));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("-11"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("11.1"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("fsjojsfojsfsojef"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93yhw2923"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93yhw 2923 "));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN(" f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93 yhw 2923"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93y你好hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN("f93y博昕科技hw中国2 923"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN(",./2923"));
		Assert.assertFalse(FieldFormat.checkBusinessLicenseSN(null));
	}

	@Test
	public void checkProviderMobile() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkProviderMobile("13660086877"));
		Assert.assertTrue(FieldFormat.checkProviderMobile("-13660AA"));
		Assert.assertTrue(FieldFormat.checkProviderMobile("111113660AA1111111@$1111"));
		Assert.assertTrue(FieldFormat.checkProviderMobile("好客山东机开机"));

		Assert.assertFalse(FieldFormat.checkProviderMobile("-3334 "));
		Assert.assertFalse(FieldFormat.checkProviderMobile(" -9012"));
		Assert.assertFalse(FieldFormat.checkProviderMobile(" "));
		Assert.assertFalse(FieldFormat.checkProviderMobile("-11"));
		Assert.assertFalse(FieldFormat.checkProviderMobile("11.1"));
		Assert.assertFalse(FieldFormat.checkProviderMobile("fjsfsojefsadijfh34875y0398y93*&*"));
		Assert.assertFalse(FieldFormat.checkProviderMobile("f93yh"));
		Assert.assertFalse(FieldFormat.checkProviderMobile("你好中国2"));
		Assert.assertFalse(FieldFormat.checkProviderMobile("博昕科技"));
		Assert.assertFalse(FieldFormat.checkProviderMobile(",./2"));
	}

	@Test
	public void checkCompanyName() {// 中英文数字，[1, 12]字符
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkCompanyName("abcdabcdabcd"));
		Assert.assertTrue(FieldFormat.checkCompanyName("abcdab中国"));
		Assert.assertTrue(FieldFormat.checkCompanyName("a"));
		Assert.assertTrue(FieldFormat.checkCompanyName("中"));

		Assert.assertFalse(FieldFormat.checkCompanyName("876543ytyt"));
		Assert.assertFalse(FieldFormat.checkCompanyName("876543"));
		Assert.assertFalse(FieldFormat.checkCompanyName("123456789012"));
		Assert.assertFalse(FieldFormat.checkCompanyName("1234567890123"));
		Assert.assertFalse(FieldFormat.checkCompanyName("abcdabcdabcd "));
		Assert.assertFalse(FieldFormat.checkCompanyName("abcdabcdabcde"));
		Assert.assertFalse(FieldFormat.checkCompanyName("_"));
		Assert.assertFalse(FieldFormat.checkCompanyName("adfs+m"));
		Assert.assertFalse(FieldFormat.checkCompanyName(""));
		Assert.assertFalse(FieldFormat.checkCompanyName(null));
	}

	@Test
	public void checkDBUserName() {// 数字、字母和下划线的组合，但首字符必须是字母，不能出现空格。[1, 20]字符
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkDBUserName("abcdabcdabcdabcdabcd"));
		Assert.assertTrue(FieldFormat.checkDBUserName("nbr_test_xxx"));
		Assert.assertTrue(FieldFormat.checkDBUserName("nbr_test_xxx9"));

		Assert.assertFalse(FieldFormat.checkDBUserName("abcdabcdabcdabcdabcd1"));
		Assert.assertFalse(FieldFormat.checkDBUserName("1bcdabcdabcdabcdabcd"));
		Assert.assertFalse(FieldFormat.checkDBUserName("nbr_test_xxx "));
		Assert.assertFalse(FieldFormat.checkDBUserName("1nbr_test_xxx"));
		Assert.assertFalse(FieldFormat.checkDBUserName("中nbr_test_xxx"));
		Assert.assertFalse(FieldFormat.checkDBUserName(""));
		Assert.assertFalse(FieldFormat.checkDBUserName(null));
		Assert.assertFalse(FieldFormat.checkDBUserName("nbr_ss w"));
	}

	@Test
	public void checkDbName() {// 数字、字母和下划线的组合，但首字符必须是字母，中间不能出现空格。(0, 20]个字符
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkDbName("abcdeabcdeabcdeabcde"));
		Assert.assertTrue(FieldFormat.checkDbName("abcdeabcdeabcdeabcd_"));
		Assert.assertTrue(FieldFormat.checkDbName("abcdeabcdeabcdeabc9_"));
		Assert.assertTrue(FieldFormat.checkDbName("abcdef1234567890"));
		Assert.assertTrue(FieldFormat.checkDbName("abcdef1234567_890"));

		Assert.assertFalse(FieldFormat.checkDbName("abcdeabcdeabcdeabcde1"));
		Assert.assertFalse(FieldFormat.checkDbName("abc de"));
		Assert.assertFalse(FieldFormat.checkDbName(""));
		Assert.assertFalse(FieldFormat.checkDbName(null));
	}

	@Test
	public void checkSubmchid() {// 子商户号是长度为10位的数字
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkSubmchid("2312465124"));
		Assert.assertTrue(FieldFormat.checkSubmchid(null));
		Assert.assertTrue(FieldFormat.checkSubmchid(""));

		Assert.assertFalse(FieldFormat.checkSubmchid("0"));
		Assert.assertFalse(FieldFormat.checkSubmchid("abc"));
		Assert.assertFalse(FieldFormat.checkSubmchid("12345678999"));
	}

	@Test
	public void checkIfCommodityStatus() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkIfCommodityStatus(0));

		Assert.assertFalse(FieldFormat.checkIfCommodityStatus(1));
		Assert.assertFalse(FieldFormat.checkIfCommodityStatus(-1));
	}

	@Test
	public void checkIfCommodityType() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkIfCommodityType(0));

		Assert.assertFalse(FieldFormat.checkIfCommodityType(-1));
		Assert.assertFalse(FieldFormat.checkIfCommodityType(1));
	}

	@Test
	public void checkPosSN() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkPosSN("12345678"));
		Assert.assertTrue(FieldFormat.checkPosSN("abcdefgh"));
		Assert.assertTrue(FieldFormat.checkPosSN("ABCDEFGH"));
		Assert.assertTrue(FieldFormat.checkPosSN("abcdefgh12345678"));
		Assert.assertTrue(FieldFormat.checkPosSN("ABCDEFGH12345678"));
		Assert.assertTrue(FieldFormat.checkPosSN("abcdefghABCDEFGH"));
		Assert.assertTrue(FieldFormat.checkPosSN("12345678123456781234567812345678"));
		Assert.assertTrue(FieldFormat.checkPosSN("abcdefghabcdefghabcdefghabcdefgh"));
		Assert.assertTrue(FieldFormat.checkPosSN("ABCDEFGHABCDEFGHABCDEFGHABCDEFGH"));

		Assert.assertFalse(FieldFormat.checkPosSN("1234567812345678123456781234567812345678"));
		Assert.assertFalse(FieldFormat.checkPosSN("abcdefghabcdefghabcdefghabcdefghabcdefgh"));
		Assert.assertFalse(FieldFormat.checkPosSN("ABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGH"));
		Assert.assertFalse(FieldFormat.checkPosSN("@!$**&^%"));
		Assert.assertFalse(FieldFormat.checkPosSN("     "));
		Assert.assertFalse(FieldFormat.checkPosSN(""));
		Assert.assertFalse(FieldFormat.checkPosSN(null));
		Assert.assertFalse(FieldFormat.checkPosSN("中文"));

	}

	@Test
	public void checkEmail() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkEmail("123@123.com"));
		Assert.assertTrue(FieldFormat.checkEmail("xxx@xx.com"));
		Assert.assertTrue(FieldFormat.checkEmail("123@xx.com"));
		Assert.assertTrue(FieldFormat.checkEmail("1234567890@qq.com"));

		Assert.assertFalse(FieldFormat.checkEmail("&$!$$$@xx.com"));
		Assert.assertFalse(FieldFormat.checkEmail("1234567890xx.com"));
		Assert.assertFalse(FieldFormat.checkEmail("1234567890@qqcom"));
		Assert.assertFalse(FieldFormat.checkEmail("1234567890@qq."));
		Assert.assertFalse(FieldFormat.checkEmail("邮箱@qq.com"));
	}

	@Test
	public void checkQQ() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkQQ("12345"));
		Assert.assertTrue(FieldFormat.checkQQ("12345678"));
		Assert.assertTrue(FieldFormat.checkQQ("1234567890"));

		Assert.assertFalse(FieldFormat.checkQQ("12345678901"));
		Assert.assertFalse(FieldFormat.checkQQ("dassafk"));
		Assert.assertFalse(FieldFormat.checkQQ(",/.@!%*%_"));
		Assert.assertFalse(FieldFormat.checkQQ("中文"));
		Assert.assertFalse(FieldFormat.checkQQ(""));
		Assert.assertFalse(FieldFormat.checkQQ(null));
		Assert.assertFalse(FieldFormat.checkQQ("     "));
	}

	@Test
	public void checkAccount() {
		Shared.printTestMethodStartInfo();

		Assert.assertTrue(FieldFormat.checkAccount("973456742923"));
		Assert.assertTrue(FieldFormat.checkAccount("fsjojsfojsfsojef"));
		Assert.assertTrue(FieldFormat.checkAccount("f93yhw2923"));

		Assert.assertFalse(FieldFormat.checkAccount(""));
		Assert.assertFalse(FieldFormat.checkAccount(",./2923"));
		Assert.assertFalse(FieldFormat.checkAccount("f93yhw 2923"));
		Assert.assertFalse(FieldFormat.checkAccount("f93yhw中国2923"));
		Assert.assertFalse(FieldFormat.checkAccount(null));
	}

	@Test
	public void checkRetailTradeQueryKeyword() {
		Shared.printTestMethodStartInfo();
		// 从app发出的请求，其queryKeyword代表SN，只根据SN来查询
		// 空串
		Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN(""));
		// 10-26位且以ls开头
		Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("ls201907130000100002"));
		// 10-26位且以LS开头
		Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("LS201907130000100002"));
		// 10-26位且不以LS开头
		Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("20190713000010000200"));
		// 26位且以LS开头，以_1结尾
		Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNBySN("LS2019071300001000021234_1"));
		// 26位且以LS开头，不以_1结尾
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("LS2019071300001000021234_2"));
		// 包含不是LS的英文字符
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("201902020101abc112348"));
		// 包含LS的英文字符但不在开头
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("201902020101LS112348"));
		// 中文
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("星巴克"));
		// 特殊字符
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("！@#￥（&（%"));
		// null
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN(null));
		// 长度小于10
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("20190"));
		// 超过26位
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN("LS20190202010101000112348888"));
		// 首尾有空格
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNBySN(" 12345678123456781 "));

		// 从web发出的请求，其queryKeyword代表商品名称或零售单的SN
		// 设置queryKeyword为空串，结果通过
		Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNByQueryKeyword(""));
		// 设置queryKeyword是0-32位，且包含中文，结果通过。
		Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNByQueryKeyword("烤面筋"));
		// 设置queryKeyword是0-32位，且包含英文字符，结果通过。
		Assert.assertTrue(FieldFormat.checkRetailTradeRetrieveNByQueryKeyword("helloisme"));
		// 设置queryKeyword的长度超过32，结果不通过。
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNByQueryKeyword("123456781234567812345678123454564564567812345678"));
		// 设置queryKeyword是0-32位，且首尾有空格，结果不通过
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNByQueryKeyword(" 12345678123456781 "));
		// 设置queryKeyword是0-32位，且包含特殊字符，结果不通过
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNByQueryKeyword("！@#￥（&（%"));
		// 设置queryKeyword为null，结果不通过
		Assert.assertFalse(FieldFormat.checkRetailTradeRetrieveNByQueryKeyword(null));
	}

	@Test
	public void checkRetailTradeSN_For_checkCreate() {
		// checkCreate
		// 设置F_SN为26位的退货单，结尾为_1，且格式正确，结果通过。
		Assert.assertTrue(FieldFormat.checkRetailTradeSN("LS2019010101010100011234_1"));
		// 设置F_SN为24位普通零售单，且格式正确，结果通过。
		Assert.assertTrue(FieldFormat.checkRetailTradeSN("LS2019010101010100011234"));

		// 设置F_SN的长度小于24，结果不通过。
		Assert.assertFalse(FieldFormat.checkRetailTradeSN("LS1234567"));
		// 设置F_SN的长度等于25，结果不通过。
		Assert.assertFalse(FieldFormat.checkRetailTradeSN("LS12345678912345678912345"));
		// 设置F_SN的长度超过26，结果不通过。
		Assert.assertFalse(FieldFormat.checkRetailTradeSN("LS1234567891234567891234567891"));
		// 设置F_SN不是LS开头，结果不通过。
		Assert.assertFalse(FieldFormat.checkRetailTradeSN("1234567890123456789012"));
		// 设置F_SN包含中文，结果不通过。
		Assert.assertFalse(FieldFormat.checkRetailTradeSN("LS123456星巴克1234567891234"));
		// 设置F_SN包含其他英文字符，结果不通过。
		Assert.assertFalse(FieldFormat.checkRetailTradeSN("LS123456abc1234567891234"));
		// 设置F_SN包含空格，结果不通过
		Assert.assertFalse(FieldFormat.checkRetailTradeSN("LS123456   1234567891234"));
		// 设置F_SN为null，结果不通过
		Assert.assertFalse(FieldFormat.checkRetailTradeSN(null));
		// 设置F_SN为空串，结果不通过
		Assert.assertFalse(FieldFormat.checkRetailTradeSN(""));
		// 设置F_SN包含特殊字符，结果不通过
		Assert.assertFalse(FieldFormat.checkRetailTradeSN("1234567891234"));
		// 设置F_SN的日期比当前时间晚，结果不通过。
		Assert.assertFalse(FieldFormat.checkRetailTradeSN("LS2021010101010100011234"));
	}

	@Test
	public void checkCountOfBlankLineAtBottom() {
		Shared.printTestMethodStartInfo();
		// 范围0-5
		Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(0));
		Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(1));
		Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(2));
		Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(3));
		Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(4));
		Assert.assertTrue(FieldFormat.checkCountOfBlankLineAtBottom(5));
		// 小于0
		Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(-1));
		Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(-22));
		Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(-333));
		Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(-4444));
		Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(-55555));
		// 大于5
		Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(6));
		Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(77));
		Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(888));
		Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(9999));
		Assert.assertFalse(FieldFormat.checkCountOfBlankLineAtBottom(10000));
	}

	@Test
	public void checkBoolean() {
		Shared.printTestMethodStartInfo();
		
		Assert.assertTrue(FieldFormat.checkBoolean(EnumBoolean.EB_NO.getIndex()));
		Assert.assertTrue(FieldFormat.checkBoolean(EnumBoolean.EB_Yes.getIndex()));
		
		Assert.assertFalse(FieldFormat.checkBoolean(2));
		Assert.assertFalse(FieldFormat.checkBoolean(-1));
		
	}
	
	@Test
	public void checkRoleIDToCreateStaff() {
		Shared.printTestMethodStartInfo();
		
		Assert.assertTrue(FieldFormat.checkRoleIDToCreateStaff(EnumTypeRole.ETR_Cashier.getIndex()));
		Assert.assertTrue(FieldFormat.checkRoleIDToCreateStaff(EnumTypeRole.ETR_Boss.getIndex()));
		Assert.assertTrue(FieldFormat.checkRoleIDToCreateStaff(EnumTypeRole.ETR_PreSale.getIndex()));
		
//		Assert.assertFalse(FieldFormat.checkRoleIDToCreateStaff(EnumTypeRole.ETR_Manager.getIndex()));
//		Assert.assertFalse(FieldFormat.checkRoleIDToCreateStaff(EnumTypeRole.ETR_Assistant.getIndex()));
//		Assert.assertFalse(FieldFormat.checkRoleIDToCreateStaff(EnumTypeRole.ETR_CommercialManager.getIndex()));
		
	}
	
	@Test
	public void checkCardType() {
		Shared.printTestMethodStartInfo();
		Assert.assertTrue(FieldFormat.checkCardType(null));
		Assert.assertTrue(FieldFormat.checkCardType(EnumCouponType.ECT_Cash.getName()));
		Assert.assertTrue(FieldFormat.checkCardType(EnumCouponType.ECT_Discount.getName()));
		
		Assert.assertFalse(FieldFormat.checkCardType(""));
		Assert.assertFalse(FieldFormat.checkCardType("1324"));
		Assert.assertFalse(FieldFormat.checkCardType("afds"));
		Assert.assertFalse(FieldFormat.checkCardType("123add"));
		Assert.assertFalse(FieldFormat.checkCardType("daf123"));
	}
	
//	@Test
//	public void checkRetrieveNFromWxServerTest() throws ParseException {
//		// 1、正常查询
//		CouponStatistics couponStatistics = new CouponStatistics();
//		couponStatistics.setBegin_date("2020-02-14");
//		couponStatistics.setEnd_date("2020-02-15");
//		String err = couponStatistics.checkRetrieveNFromWxServer();
//		Assert.assertTrue(err.equals(""));
//		// 2、日期格式错误
//		CouponStatistics couponStatistics2 = new CouponStatistics();
//		couponStatistics2.setBegin_date("2020/02/14");
//		couponStatistics2.setEnd_date("2020/02/15");
//		err = couponStatistics2.checkRetrieveNFromWxServer();
//		Assert.assertTrue(err.equals(CouponStatistics.FIELD_ERROR_GeneralDateFormat));
//		//3、结束日期要大于开始日期
//		CouponStatistics couponStatistics3 = new CouponStatistics();
//		couponStatistics3.setBegin_date("2020-02-15");
//		couponStatistics3.setEnd_date("2020-02-14");
//		err = couponStatistics3.checkRetrieveNFromWxServer();
//		Assert.assertTrue(err.equals(CouponStatistics.FIELD_ERROR_BeginDateEndDate));
//		// 4、结束日期要小于当前日期	
//		CouponStatistics couponStatistics4 = new CouponStatistics();
//		couponStatistics4.setBegin_date("2020-02-14");
//		couponStatistics4.setEnd_date("2020-03-14");
//		err = couponStatistics4.checkRetrieveNFromWxServer();
//		Assert.assertTrue(err.equals(CouponStatistics.FIELD_ERROR_EndDate));
//		// 5、查询时间区间需<=62天
//		Calendar cal = Calendar.getInstance();
//		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_Default1);
//		String beginDateStr = "2019-03-01";
//		Date beginDate = sdf.parse(beginDateStr);
//		cal.setTime(beginDate);
//		cal.add(Calendar.DATE, 63);
//		Date endDate = cal.getTime();
//		CouponStatistics couponStatistics5 = new CouponStatistics();
//		couponStatistics5.setBegin_date(beginDateStr);
//		couponStatistics5.setEnd_date(sdf.format(endDate));
//		err = couponStatistics5.checkRetrieveNFromWxServer();
//		Assert.assertTrue(err.equals(CouponStatistics.FIELD_ERROR_DateScope));
//		// 6、 传入错误的卡券来源cond_source
//		CouponStatistics couponStatistics6 = new CouponStatistics();
//		couponStatistics6.setBegin_date("2020-02-14");
//		couponStatistics6.setEnd_date("2020-02-15");
//		couponStatistics6.setCond_source(3);
//		err = couponStatistics6.checkRetrieveNFromWxServer();
//		Assert.assertTrue(err.equals(CouponStatistics.FIELD_ERROR_ConditionSource));
//	}
	
	@Test
	public void checkCouponTitle() {
		Shared.printTestMethodStartInfo();
		Assert.assertTrue(FieldFormat.checkCouponTitle("1324"));
		Assert.assertTrue(FieldFormat.checkCouponTitle("afds"));
		Assert.assertTrue(FieldFormat.checkCouponTitle("123add"));
		Assert.assertTrue(FieldFormat.checkCouponTitle("daf123"));
		//
		Assert.assertFalse(FieldFormat.checkCouponTitle(null));
		Assert.assertFalse(FieldFormat.checkCouponTitle(""));
		Assert.assertFalse(FieldFormat.checkCouponTitle(" "));
		Assert.assertFalse(FieldFormat.checkCouponTitle("1234567890"));
		Assert.assertFalse(FieldFormat.checkCouponTitle("12>"));
		Assert.assertFalse(FieldFormat.checkCouponTitle("?>"));
	}
	
	@Test
	public void testValidate2RGBColor() {
		String backgroundColor = null;
		Assert.assertFalse(FieldFormat.validate2RGBColor(backgroundColor));

		backgroundColor = "255,255;23,23";
		Assert.assertFalse(FieldFormat.validate2RGBColor(backgroundColor));

		backgroundColor = "255,255,-1;23,23,1";
		Assert.assertFalse(FieldFormat.validate2RGBColor(backgroundColor));

		backgroundColor = "255,255,1;23,23,a";
		Assert.assertFalse(FieldFormat.validate2RGBColor(backgroundColor));

		backgroundColor = "255,255,1;23,23,256";
		Assert.assertFalse(FieldFormat.validate2RGBColor(backgroundColor));

		backgroundColor = "255,255,1;23,23,256,4";
		Assert.assertFalse(FieldFormat.validate2RGBColor(backgroundColor));

		backgroundColor = "255,255,1;";
		Assert.assertFalse(FieldFormat.validate2RGBColor(backgroundColor));

		backgroundColor = "255,255,1;23,23,256;";
		Assert.assertFalse(FieldFormat.validate2RGBColor(backgroundColor));

		backgroundColor = "255,255,1,23,23,256";
		Assert.assertFalse(FieldFormat.validate2RGBColor(backgroundColor));

		backgroundColor = "255;256";
		Assert.assertFalse(FieldFormat.validate2RGBColor(backgroundColor));

		backgroundColor = "255,255,1;23,23,25";
		Assert.assertTrue(FieldFormat.validate2RGBColor(backgroundColor));
	}
	
	@Test
	public void checkVipName() {
		Shared.printTestMethodStartInfo();
		Assert.assertTrue(FieldFormat.checkVipName("1324"));
		Assert.assertTrue(FieldFormat.checkVipName("afds"));
		Assert.assertTrue(FieldFormat.checkVipName("123add"));
		Assert.assertTrue(FieldFormat.checkVipName("daf123"));
		Assert.assertTrue(FieldFormat.checkVipName("12>"));
		Assert.assertTrue(FieldFormat.checkVipName("?>"));
		//
		Assert.assertFalse(FieldFormat.checkVipName(null));
		Assert.assertFalse(FieldFormat.checkVipName(""));
		Assert.assertTrue(FieldFormat.checkVipName("1"));
		Assert.assertFalse(FieldFormat.checkVipName(String.format("%0" + (Vip.MAX_LENGTH_WxNickName + 1) + "d", System.currentTimeMillis())));
	}
	
	@Test
	public void testCheckOpenID() {
		Shared.printTestMethodStartInfo();
		Assert.assertTrue(FieldFormat.checkOpenID("12345"));
		Assert.assertTrue(FieldFormat.checkOpenID("afds"));
		Assert.assertTrue(FieldFormat.checkOpenID("123add"));
		Assert.assertTrue(FieldFormat.checkOpenID("daf123"));
		Assert.assertTrue(FieldFormat.checkOpenID("daf1-23"));
		Assert.assertTrue(FieldFormat.checkOpenID("daf1-2_3"));
		Assert.assertTrue(FieldFormat.checkOpenID("12345678901234567890123456789012345678901234567890")); // 长50
		//
		Assert.assertFalse(FieldFormat.checkOpenID(null));
		Assert.assertFalse(FieldFormat.checkOpenID(""));
		Assert.assertFalse(FieldFormat.checkOpenID("1s3fafs2>"));
		Assert.assertFalse(FieldFormat.checkOpenID("?&fsh>"));
		Assert.assertFalse(FieldFormat.checkOpenID("123456789012345678901234567890123456789012345678901")); // 长51
		Assert.assertFalse(FieldFormat.checkOpenID("12345678901234567890123456789012345678901234567890A"));  // 长51
		Assert.assertFalse(FieldFormat.checkOpenID("1234567890123456789012345 "));
		Assert.assertFalse(FieldFormat.checkOpenID("1234567890123王道789012345"));
	}

	
	@Test
	public void testCheckUnionID() {
		Shared.printTestMethodStartInfo();
		Assert.assertTrue(FieldFormat.checkUnionID("12345"));
		Assert.assertTrue(FieldFormat.checkUnionID("afds"));
		Assert.assertTrue(FieldFormat.checkUnionID("123add"));
		Assert.assertTrue(FieldFormat.checkUnionID("daf123"));
		Assert.assertTrue(FieldFormat.checkUnionID("daf1-23"));
		Assert.assertTrue(FieldFormat.checkUnionID("daf1-2_3"));
		Assert.assertTrue(FieldFormat.checkUnionID("12345678901234567890123456789012345678901234567890")); // 长50
		//
		Assert.assertFalse(FieldFormat.checkUnionID(null));
		Assert.assertFalse(FieldFormat.checkUnionID(""));
		Assert.assertFalse(FieldFormat.checkUnionID("1s3fafs2>"));
		Assert.assertFalse(FieldFormat.checkUnionID("?&fsh>"));
		Assert.assertFalse(FieldFormat.checkUnionID("123456789012345678901234567890123456789012345678901")); // 长51
		Assert.assertFalse(FieldFormat.checkUnionID("12345678901234567890123456789012345678901234567890A"));  // 长51
		Assert.assertFalse(FieldFormat.checkUnionID("1234567890123456789012345 "));
		Assert.assertFalse(FieldFormat.checkUnionID("1234567890123王道789012345"));
	}
	
	
	@Test
	public void testCheckCardCode() {
		Shared.printTestMethodStartInfo();
		Assert.assertTrue(FieldFormat.checkCardCode("12345"));
		Assert.assertTrue(FieldFormat.checkCardCode("3453245"));
		Assert.assertTrue(FieldFormat.checkCardCode("372544524245"));
		Assert.assertTrue(FieldFormat.checkCardCode("258462452"));
		Assert.assertTrue(FieldFormat.checkCardCode("587646954696"));
		Assert.assertTrue(FieldFormat.checkCardCode("98769569456"));
		Assert.assertTrue(FieldFormat.checkCardCode("1234567890123456"));
		//
		Assert.assertFalse(FieldFormat.checkCardCode(null));
		Assert.assertFalse(FieldFormat.checkCardCode(""));
		Assert.assertFalse(FieldFormat.checkCardCode("1a1"));
		Assert.assertFalse(FieldFormat.checkCardCode("1s3fafs2>"));
		Assert.assertFalse(FieldFormat.checkCardCode("?&fsh>"));
		Assert.assertFalse(FieldFormat.checkCardCode("12345678901234567890123456789012345678901234567890A"));  // 长51
		Assert.assertFalse(FieldFormat.checkCardCode("1234567890123456789012345 "));
		Assert.assertFalse(FieldFormat.checkCardCode("1234567890123王道789012345"));
	}
}
