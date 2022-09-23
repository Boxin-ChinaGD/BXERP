package com.bx.erp.test.staff;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BasePosTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class ShopMapperTest extends BaseMapperTest {
	/** 检查门店名字唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_SHOPNAME = 1;

	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	@Test
	public void retrieveNExTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: 正常查询");

		Shop s = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(s);
		List<List<BaseModel>> list = BaseShopTest.retrieveNExViaMapper(shopCreate);
		for (BaseModel bm : list.get(0)) {
			int ID = ((Shop) bm).getID();
			if (ID == shopCreate.getID()) {
				Assert.assertTrue(true);
			}
		}
	}

	@Test
	public void retrieveNExTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: 传入依据districtID进行查询 ");

		Shop s = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(s);
		shopCreate.setDistrictID(1);
		List<List<BaseModel>> list = BaseShopTest.retrieveNExViaMapper(shopCreate);
		for (BaseModel bm : list.get(0)) {
			int districtID = ((Shop) bm).getDistrictID();
			if (districtID == shopCreate.getDistrictID()) {
				Assert.assertTrue(true);
			}
		}
	}

	@Test
	public void retrieveNTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: 正常查询 ");

		Shop shop = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(shop);
		shopCreate.setDistrictID(0);
		List<BaseModel> list = BaseShopTest.retrieveNViaMapper(shopCreate);
		//
		for (BaseModel bm : list) {
			String name = ((Shop) bm).getName();
			if (name.equals(shop.getName())) {
				Assert.assertTrue(true);
			}
		}
	}

	@Test
	public void retrieveNTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: 正常查询 ");

		Shop shop = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(shop);
		shopCreate.setDistrictID(1);
		List<BaseModel> list = BaseShopTest.retrieveNViaMapper(shopCreate);
		//
		for (BaseModel bm : list) {
			int districtID = ((Shop) bm).getDistrictID();
			if (districtID == shop.getDistrictID()) {
				Assert.assertTrue(true);
			}
		}
	}

	@Test
	public void createTest_CASE1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: 正常添加");
		Shop s = BaseShopTest.DataInput.getShop();
		String err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		BaseShopTest.createViaMapper(s);
	}

	@Test
	public void createTest_CASE2() throws CloneNotSupportedException, InterruptedException {// Case3: 添加不存在的公司的门店
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: 添加重复名字的公司并且重复名字的门店");

		Shop s = BaseShopTest.DataInput.getShop();
		s.setName("博昕小卖部");
		s.setCompanyID(1);
		String err = s.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		Map<String, Object> paramsCreate = s.getCreateParam(BaseBO.INVALID_CASE_ID, s);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Shop shopCreate = (Shop) shopMapper.create(paramsCreate);
		//
		Assert.assertTrue(shopCreate == null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, //
				paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("创建对象失败： " + paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
	}

	@Test
	public void createTest_CASE3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: 添加不存在的公司的门店");

		Shop s = BaseShopTest.DataInput.getShop();
		s.setCompanyID(BaseAction.INVALID_ID);
		Map<String, Object> paramsCreate = s.getCreateParam(BaseBO.INVALID_CASE_ID, s);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Shop shopCreate = (Shop) shopMapper.create(paramsCreate);
		//
		Assert.assertTrue(shopCreate == null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
				paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("创建对象失败: " + paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
	}

	@Test
	public void createTest_CASE4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:用不存在的业务经理进行创建");

		Shop s = BaseShopTest.DataInput.getShop();
		s.setBxStaffID(BaseAction.INVALID_ID);
		Map<String, Object> paramsCreate = s.getCreateParam(BaseBO.INVALID_CASE_ID, s);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Shop shopCreate = (Shop) shopMapper.create(paramsCreate);
		Assert.assertTrue(shopCreate == null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
				paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("创建对象失败: " + paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
	}

	@Test
	public void createTest_CASE5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:用不存在的门店区域进行创建");

		Shop s = BaseShopTest.DataInput.getShop();
		s.setDistrictID(BaseAction.INVALID_ID);
		Map<String, Object> paramsCreate = s.getCreateParam(BaseBO.INVALID_CASE_ID, s);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Shop shopCreate = (Shop) shopMapper.create(paramsCreate);
		Assert.assertTrue(shopCreate == null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
				paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("创建对象失败: " + paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
	}

	@Test
	public void deleteTest_CASE1() throws Exception {//
		// case2:删除存在门店。创建一个门店，两个staff和两个pos机。可以直接删除，错误码为0
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:删除存在门店。创建一个门店，一个staff和一个pos机。可以直接删除，错误码为0");

		// shop，staff和pos分别创建一个
		Shop shop = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(shop);
		//
		Pos pos = BasePosTest.DataInput.getPOS();
		pos.setShopID(shopCreate.getID());
		Pos posCreate = BasePosTest.createPosViaMapper(pos, Shared.DBName_Test);
		//
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreate.getID());
		Staff staffCreate = BaseStaffTest.createStaffViaMapper(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError);
		//

		int posIDList1[] = new int[1];
		posIDList1[0] = posCreate.getID();
		int staffIDList1[] = new int[1];
		staffIDList1[0] = staffCreate.getID();

		// 删除一个门店并删除该门店的相关的pos机和staff
		BaseShopTest.deleteViaMapper(shopCreate, staffIDList1, posIDList1);
	}

	@Test
	public void deleteTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:删除存在门店。创建一个门店，两个staff和两个pos机。可以直接删除，错误码为0");

		// shop创建一个，staff和pos分别创建两个
		Shop shop = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(shop);
		
		int posIDList[] = new int[2];
		int staffIDList[] = new int[2];
		for(int i = 0; i < 2; i ++) {
			Pos pos = BasePosTest.DataInput.getPOS();
			pos.setShopID(shopCreate.getID());
			Pos posCreate = BasePosTest.createPosViaMapper(pos, Shared.DBName_Test);
			posIDList[i] = posCreate.getID();
			//
			Staff staff = BaseStaffTest.DataInput.getStaff();
			staff.setShopID(shopCreate.getID());
			Staff staffCreate = BaseStaffTest.createStaffViaMapper(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError);
			staffIDList[i] = staffCreate.getID();
		}

		// 删除一个门店并删除该门店的相关的pos机和staff
		Shop s = BaseShopTest.DataInput.getShop();
		s.setID(shopCreate.getID());
		s.setCompanyID(shopCreate.getCompanyID());
		//
		BaseShopTest.deleteViaMapper(shopCreate, staffIDList, posIDList);
	}

	@Test
	public void deleteTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: 不传入companyID,返回错误码7");

		Shop shop = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(shop);

		Shop shopDelete = (Shop) shopCreate.clone();
		shopDelete.setCompanyID(BaseAction.INVALID_ID);
		Map<String, Object> paramsDelete = shop.getDeleteParamEx(BaseBO.INVALID_CASE_ID, shopDelete);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = shopMapper.deleteEx(paramsDelete);
		//
		Assert.assertTrue(list.size() == 0//
				&& EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("删除门店失败： " + paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
	}

	// @Test 此测试会将所有的门店删除
	public void deleteTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:shopID<=0时，删除公司下的所有门店信息，错误码为0");

		Shop shop = new Shop();
		shop.setID(BaseAction.INVALID_ID);
		Map<String, Object> paramsDelete = shop.getDeleteParamEx(BaseBO.INVALID_CASE_ID, shop);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = shopMapper.deleteEx(paramsDelete);
		//
		Assert.assertTrue(list.size() == 0//
				&& EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		List<BaseModel> listRN = BaseShopTest.retrieveNViaMapper(shop);
		for (BaseModel bm : listRN) {
			if (((Shop) bm).getStatus() != Shop.EnumStatusShop.ESS_ArrearageLocking.getIndex()) {
				Assert.assertTrue(false, "删除门店失败");
			}
		}
		//
		System.out.println("删除门店成功");
	}

	@Test
	public void retrieve1ExTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常查询一个门店 ");
		BaseShopTest.retrieve1ExViaMapper(2, BaseShopTest.RETRIEVE1EX_CASEID1);
	}

	@Test
	public void retrieve1ExTestCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:用不存在的ID查询一个门店");
		BaseShopTest.retrieve1ExViaMapper(BaseAction.INVALID_ID, BaseShopTest.RETRIEVE1EX_NOEXIST_CASEID);
	}

	@Test
	public void retrieve1ExTestCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3: 查找不存在Shop");
		// 创建Shop
		Shop shop = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(shop);
		//
		Pos pos = BasePosTest.DataInput.getPOS();
		pos.setShopID(shopCreate.getID());
		Pos posCreate = BasePosTest.createPosViaMapper(pos, Shared.DBName_Test);
		//
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreate.getID());
		Staff staffCreate = BaseStaffTest.createStaffViaMapper(BaseBO.INVALID_CASE_ID, staff, EnumErrorCode.EC_NoError);
		//
		int posIDList1[] = new int[1];
		posIDList1[0] = posCreate.getID();
		int staffIDList1[] = new int[1];
		staffIDList1[0] = staffCreate.getID();

		// 删除一个门店并删除该门店的相关的pos机和staff
		BaseShopTest.deleteViaMapper(shopCreate, staffIDList1, posIDList1);

		// 查找shop
		BaseShopTest.retrieve1ExViaMapper(shopCreate.getID(), BaseShopTest.RETRIEVE1EX_DELETESTATUS_CASEID2);
	}

	@Test
	public void updateTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常修改一个门店");
		// 创建shop
		Shop shop = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(shop);
		// 修改shop
		shopCreate.setName("updatecase" + String.valueOf(System.currentTimeMillis()).substring(6));
		BaseShopTest.updateViaMapper(shopCreate);
	}

	@Test
	public void updateTest_CASE2() throws Exception {//
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2：修改成重复的门店名称");
		// 创建shop
		Shop shop = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(shop);
		// 修改shop
		shop.setID(shopCreate.getID());
		shop.setName("博昕小卖部");
		String err = shop.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		Map<String, Object> paramsUpdate = shop.getUpdateParam(BaseBO.INVALID_CASE_ID, shop);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Shop shopUpdate = (Shop) shopMapper.update(paramsUpdate);
		//
		Assert.assertTrue(shopUpdate == null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, //
				paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Shop shopRN = (Shop) BaseShopTest.retrieveNViaMapper(shopCreate).get(0);
		if (shopCreate.compareTo(shopRN) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		System.out.println("修改失败 :" + paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
	}

	@Test
	public void updateTest_CASE3() throws Exception {//
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3：修改成不存在的门店区域");
		// 创建shop
		Shop shop = BaseShopTest.DataInput.getShop();
		Shop shopCreate = BaseShopTest.createViaMapper(shop);

		// 修改shop
		shop.setID(shopCreate.getID());
		shop.setName("博昕的小卖部" + String.valueOf(System.currentTimeMillis()).substring(4));
		shop.setDistrictID(BaseAction.INVALID_ID);

		Map<String, Object> paramsUpdate = shop.getUpdateParam(BaseBO.INVALID_CASE_ID, shop);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Shop shopUpdate = (Shop) shopMapper.update(paramsUpdate);
		//
		Assert.assertTrue(shopUpdate == null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
				paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkUniqueFieldTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:创建门店时，传入一个不存在的门店名称，检查名称是否已存在");

		Shop shop1 = new Shop();
		shop1.setFieldToCheckUnique(CASE_CHECK_UNIQUE_SHOPNAME);
		shop1.setUniqueField("部卖小昕博");
		Map<String, Object> params1 = shop1.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, shop1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		shopMapper.checkUniqueField(params1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("检查成功，该名称：" + shop1.getUniqueField() + "没有重复");
	}

	@Test
	public void checkUniqueFieldTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:创建门店时，传入一个已存在的门店名称，检查名称是否已存在");

		Shop shop2 = new Shop();
		shop2.setFieldToCheckUnique(CASE_CHECK_UNIQUE_SHOPNAME);
		shop2.setUniqueField("博昕小卖部");
		Map<String, Object> params2 = shop2.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, shop2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		shopMapper.checkUniqueField(params2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, //
				params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("检查成功： " + params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
	}

	@Test
	public void checkUniqueFieldTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4: 更新门店时，传入一个已存在的门店名称，检查名称是否已存在");

		Shop shop3 = new Shop();
		shop3.setID(2);
		shop3.setFieldToCheckUnique(CASE_CHECK_UNIQUE_SHOPNAME);
		shop3.setUniqueField("博昕小卖部");
		Map<String, Object> params3 = shop3.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, shop3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		shopMapper.checkUniqueField(params3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("检查成功：该名称" + shop3.getUniqueField() + "没有重复");
	}

	@Test
	public void checkUniqueFieldTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4: 更新门店时，传入一个已存在的门店名称，检查名称是否已存在");
		//
		Shop shop3 = new Shop();
		shop3.setID(1);
		shop3.setFieldToCheckUnique(CASE_CHECK_UNIQUE_SHOPNAME);
		shop3.setUniqueField("博昕小卖部");
		Map<String, Object> params3 = shop3.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, shop3);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		shopMapper.checkUniqueField(params3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, //
				params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("检查成功： " + params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
	}
	
	@Test
	public void retrieveNByFieldsTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1: 正常查询");
		Shop shop = new Shop();
		shop.setQueryKeyword("");
		shop.setPageIndex(1);
		shop.setPageSize(10);
		Map<String, Object> params = shop.getRetrieveNParam(BaseBO.CASE_Shop_RetrieveNByFields, shop);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = shopMapper.retrieveNByFields(params);

		Assert.assertTrue(listBM != null && listBM.size() > 0 && //
				EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//		for (BaseModel bm : listBM) {
//			Provider p = (Provider) bm;
//			assertTrue(p.getName().contains(shop.getQueryKeyword()));
//		}
	}
	
	@Test
	public void retrieveNByFieldsTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2: 根据门店名称查询");
		Shop shop = new Shop();
		shop.setQueryKeyword("默认");
		shop.setPageIndex(1);
		shop.setPageSize(10);
		Map<String, Object> params = shop.getRetrieveNParam(BaseBO.CASE_Shop_RetrieveNByFields, shop);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = shopMapper.retrieveNByFields(params);

		Assert.assertTrue(listBM != null && listBM.size() > 0 && //
				EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		
		for (BaseModel bm : listBM) {
			Shop s = (Shop) bm;
			assertTrue(s.getName().contains(shop.getQueryKeyword()));
		}
	}
	
	@Test
	public void retrieveNByFieldsTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3: 根据地域ID查询");
		Shop shop = new Shop();
		shop.setQueryKeyword("");
		shop.setDistrictID(1);
		shop.setPageIndex(1);
		shop.setPageSize(10);
		Map<String, Object> params = shop.getRetrieveNParam(BaseBO.CASE_Shop_RetrieveNByFields, shop);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = shopMapper.retrieveNByFields(params);

		Assert.assertTrue(listBM != null && listBM.size() > 0 && //
				EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		
		for (BaseModel bm : listBM) {
			Shop s = (Shop) bm;
			assertTrue(s.getDistrictID() == shop.getDistrictID());
		}
	}

	@Test
	public void retrieveNByFieldsTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3: 根据地域ID和名称查询查询");
		Shop shop = new Shop();
		shop.setQueryKeyword("默认");
		shop.setDistrictID(1);
		shop.setPageIndex(1);
		shop.setPageSize(10);
		Map<String, Object> params = shop.getRetrieveNParam(BaseBO.CASE_Shop_RetrieveNByFields, shop);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = shopMapper.retrieveNByFields(params);

		Assert.assertTrue(listBM != null && listBM.size() > 0 && //
				EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		
		for (BaseModel bm : listBM) {
			Shop s = (Shop) bm;
			assertTrue(s.getDistrictID() == shop.getDistrictID() && s.getName().contains(shop.getQueryKeyword()));
		}
	}
}
