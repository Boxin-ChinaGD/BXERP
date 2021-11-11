package com.bx.erp.test.purchasing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrder.EnumStatusPurchasingOrder;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

public class ProviderMapperTest extends BaseMapperTest {
	private static final int INVALID_DISTRICTID = 999999999;


	/** 检查供应商名字唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_PROVIDERNAME = 1;
	/** 检查供应商电话唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_PROVIDERPHONE = 2;

	public static class DataInput {
		private static Provider providerInput = null;

		protected static final Provider getProvider() throws CloneNotSupportedException, InterruptedException {
			providerInput = new Provider();
			providerInput.setName(Shared.getLongestProviderName("淘宝"));
			Thread.sleep(1);
			providerInput.setDistrictID(1);
			providerInput.setAddress("广州市天河区二十八中学");
			providerInput.setContactName("zda");
			providerInput.setMobile(Shared.getValidStaffPhone());

			return (Provider) providerInput.clone();
		}

	}

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常创建");
		// 创建一个供应商
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);

		// 删除创建出来的供应商
		deleteProvider(providerCreate);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 2: 供应商名字已存在，错误码为1");
		// 创建一个供应商
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);

		// 使用创建出来的供应商名称进行创建
		Provider p1 = DataInput.getProvider();
		p1.setName(providerCreate.getName());
		// 传入参数验证
		checkCreateFunction(p1);
		//
		Map<String, Object> params1 = p1.getCreateParam(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerCreate2 = (Provider) providerMapper.create(params1); // ...
		assertNull(providerCreate2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除创建出来的供应商
		deleteProvider(providerCreate);
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case 3:供应商的电话号码已存在，错误码为1 ");
		//
		Provider p = DataInput.getProvider();
		p.setMobile("13129355441");
		// 传入参数验证
		checkCreateFunction(p);
		//
		Map<String, Object> params2 = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider provider = (Provider) providerMapper.create(params2); // ...
		Assert.assertTrue(provider == null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case 4:只输入名字进行创建");
		//
		Provider p = DataInput.getProvider();
		p.setAddress("");
		p.setContactName("");
		// 由于Mobile是唯一键,过compareTo会报空指针，把moblie设成不为空
		// 传入参数验证
		checkCreateFunction(p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider provider = createProvider(p); // ...
		//
		checkCreateFunction(provider);

		// 删除创建出来的供应商
		deleteProvider(provider);
	}

	@Test
	public void createTest5() throws Exception {
		Shared.caseLog("Case 5:用不存在的供应商区域的ID创建");
		Provider p = DataInput.getProvider();
		p.setDistrictID(INVALID_DISTRICTID);
		//
		checkCreateFunction(p);
		//
		Map<String, Object> params4 = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerCreate2 = (Provider) providerMapper.create(params4); // ...
		Assert.assertTrue(providerCreate2 == null && EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象成功");
	}

	@Test
	public void createTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case 6:没有传入电话号码正常创建");
		//
		Provider p = DataInput.getProvider();
		p.setMobile("");
		//
		checkCreateFunction(p);
		//
		Map<String, Object> params = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider provider = (Provider) providerMapper.create(params);
		p.setIgnoreIDInComparision(true);
		if (p.compareTo(provider) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		checkCreateFunction(provider);

		// 删除创建出来的供应商
		deleteProvider(provider);
	}

	@Test
	public void createTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case 7:传入手机号码大于" + Provider.Max_LengthMoile);
		//
		Provider p = DataInput.getProvider();
		p.setMobile("278344444448&*&*4444444444444444444444444444444");
		Assert.assertTrue(p.getMobile().length() > Provider.Max_LengthMoile);
		//
		try {
			Map<String, Object> params = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider provider = (Provider) providerMapper.create(params);
			if (provider != null) {
				Assert.assertTrue(false, "创建对象不应该成功！");
			}
		} catch (Exception e) {
			if (e.getCause() instanceof com.mysql.cj.jdbc.exceptions.MysqlDataTruncation) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false, "异常不是期望的类型！");
			}
		}
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:商品表中已有商品的供应商。删除不了，错误代码为7 ");
		// 创建一个供应商
		Provider p = DataInput.getProvider();
		Provider providerCreate = createProvider(p);
		//
		// 创建一个供应商商品
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setCommodityID(1);
		pc1.setProviderID(providerCreate.getID());
		Map<String, Object> params1 = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity bm = (ProviderCommodity) providerCommodityMapper.create(params1);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(bm) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(bm != null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		// 商品表中已有商品的供应商。删除不了，错误代码为7
		Provider p1 = DataInput.getProvider();
		p1.setID(providerCreate.getID());
		Map<String, Object> paramsForDelete = p1.getDeleteParam(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.delete(paramsForDelete); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		// Retrieve1确认该供应商没有被删除
		Map<String, Object> paramsForRetrieve1 = providerCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel bmForRetrieve1 = providerMapper.retrieve1(paramsForRetrieve1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		assertNotNull(bmForRetrieve1);

		// 删除供应商商品
		ProviderCommodity pc2 = new ProviderCommodity();
		pc2.setProviderID(p1.getID());
		pc2.setCommodityID(1);
		Map<String, Object> paramsForDeletePC = bm.getDeleteParam(BaseBO.INVALID_CASE_ID, pc2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerCommodityMapper.delete(paramsForDeletePC);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDeletePC.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);

		// 删除与供应商商品表的关联后正常删除
		deleteProvider(p1);
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:商品表中没有这个供应商的商品。可以直接删除，错误代码为0");
		// 创建一个供应商
		Provider p = DataInput.getProvider();
		Provider providerCreate = createProvider(p);

		// 删除一个供应商
		deleteProvider(providerCreate);
	}

	@Test
	public void deleteTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:供应商已被已删除的采购订单使用。删除成功");
		// 创建一个供应商
		Provider p = DataInput.getProvider();
		Provider providerCreate = createProvider(p);

		// 创建一个采购订单
		PurchasingOrder po1 = new PurchasingOrder();
		po1.setStatus(EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
		po1.setStaffID(1);
		po1.setProviderID(providerCreate.getID());
		po1.setRemark("Remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		Map<String, Object> params1 = po1.getCreateParam(BaseBO.INVALID_CASE_ID, po1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo1 = (PurchasingOrder) purchasingOrderMapper.create(params1);
		po1.setIgnoreIDInComparision(true);
		if (po1.compareTo(createPo1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPo1 != null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除采购订单
		Map<String, Object> deleteParams1 = createPo1.getDeleteParam(BaseBO.INVALID_CASE_ID, createPo1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParams1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Map<String, Object> retrieve1Params = createPo1.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, createPo1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> poList = (List<List<BaseModel>>) purchasingOrderMapper.retrieve1Ex(retrieve1Params);
		List<BaseModel> r1Po = (List<BaseModel>) poList.get(0);
		Assert.assertTrue(r1Po.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieve1Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除供应商
		Provider p1 = new Provider();
		p1.setID(providerCreate.getID());
		deleteProvider(p1);
	}

	@Test
	public void deleteTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:供应商已被退货单引用。删除不了，错误代码为7");
		// 创建一个供应商
		Provider p = DataInput.getProvider();
		Provider providerCreate = createProvider(p);

		// 创建一张退货单
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setStaffID(3);
		rcs.setProviderID(providerCreate.getID());
		Map<String, Object> params = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params); // ...
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		// 删除供应商
		Provider p1 = new Provider();
		p1.setID(providerCreate.getID());
		Map<String, Object> paramsForDelete3 = p1.getDeleteParam(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.delete(paramsForDelete3);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);

		// Retrieve1确认该供应商没有被删除
		Map<String, Object> paramsForRetrieve1 = providerCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel bmForRetrieve1 = providerMapper.retrieve1(paramsForRetrieve1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		assertNotNull(bmForRetrieve1);
	}

	@Test
	public void deleteTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:删除一个不存在的供应商");
		// 设置一个不存在的ID
		Provider provider = new Provider();
		provider.setID(INVALID_DISTRICTID);
		//
		// 删除一个不存在的供应商
		deleteProvider(provider);
	}

	@Test
	public void deleteTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:供应商已被入库单引用。删除不了，错误代码为7");
		// 创建一个供应商
		Provider p = DataInput.getProvider();
		Provider providerCreate1 = createProvider(p);
		//
		// 创建一个入库单
		Warehousing warehousing = new Warehousing();
		warehousing.setStaffID(1);
		warehousing.setProviderID(providerCreate1.getID());
		warehousing.setWarehouseID(1);
		warehousing.setPurchasingOrderID(1);
		Map<String, Object> params2 = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wsCreate = (Warehousing) warehousingMapper.create(params2);
		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(wsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(wsCreate != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		// 删除供应商
		Provider p1 = new Provider();
		p1.setID(providerCreate1.getID());
		Map<String, Object> paramsForDelete3 = p1.getDeleteParam(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.delete(paramsForDelete3);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);

		// Retrieve1确认该供应商没有被删除
		Map<String, Object> paramsForRetrieve1 = providerCreate1.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel bmForRetrieve1 = providerMapper.retrieve1(paramsForRetrieve1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		assertNotNull(bmForRetrieve1);
	}

	@Test
	public void deleteTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:供应商已被未删除的采购订单使用。删除失败");
		// 创建一个供应商
		Provider p = DataInput.getProvider();
		Provider providerCreate = createProvider(p);
		// 创建一个采购订单
		PurchasingOrder po1 = new PurchasingOrder();
		po1.setStatus(EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
		po1.setStaffID(1);
		po1.setProviderID(providerCreate.getID());
		po1.setRemark("Remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		Map<String, Object> params1 = po1.getCreateParam(BaseBO.INVALID_CASE_ID, po1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo1 = (PurchasingOrder) purchasingOrderMapper.create(params1);
		po1.setIgnoreIDInComparision(true);
		if (po1.compareTo(createPo1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPo1 != null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除供应商
		Provider p1 = new Provider();
		p1.setID(providerCreate.getID());
		Map<String, Object> paramsForDelete3 = p1.getDeleteParam(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.delete(paramsForDelete3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForDelete3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// Retrieve1确认该供应商未被删除
		Map<String, Object> paramsForRetrieve1 = providerCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider bmForRetrieve1 = (Provider) providerMapper.retrieve1(paramsForRetrieve1);
		Assert.assertTrue(bmForRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 测试完成后删除测试对象
		// 删除采购订单
		Map<String, Object> deleteParams1 = createPo1.getDeleteParam(BaseBO.INVALID_CASE_ID, createPo1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParams1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> retrieve1Params = createPo1.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, createPo1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> poList = (List<List<BaseModel>>) purchasingOrderMapper.retrieve1Ex(retrieve1Params);
		List<BaseModel> r1Po = (List<BaseModel>) poList.get(0);
		Assert.assertTrue(r1Po.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieve1Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		// 删除供应商
		deleteProvider(bmForRetrieve1);
	}

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:输入sName作为条件查询 ");
		// 创建一个供应商
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);
		//
		Provider providerToName = new Provider();
		providerToName.setName(providerCreate.getName());
		providerToName.setDistrictID(-1);
		//
		String error = providerToName.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForName = providerCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, providerToName);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = providerMapper.retrieveN(paramsForName); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForName.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// 检查RN出来的结果集是否与预期相同
		for (BaseModel bm : listBM) {
			Provider p = (Provider) bm;
			assertEquals(p.getName(), providerCreate.getName());
		}
		//
		// 验证之后删除供应商
		deleteProvider(providerCreate);
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);
		//
		Shared.caseLog("case2:输入iDistrictID作为条件查询 ");
		Provider providerToDistrictID = new Provider();
		providerToDistrictID.setDistrictID(providerCreate.getDistrictID());
		//
		String error = providerToDistrictID.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForDistrictID = providerCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, providerToDistrictID);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = providerMapper.retrieveN(paramsForDistrictID); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDistrictID.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// 检查RN出来的结果集是否与预期相同
		for (BaseModel bm : listBM) {
			Provider p = (Provider) bm;
			assertTrue(p.getDistrictID() == providerCreate.getDistrictID());
		}
		//
		// 验证之后删除供应商
		deleteProvider(providerCreate);
	}

	@Test
	public void retrieveNTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:输入sAddress作为条件查询");
		Provider provider = DataInput.getProvider();
		provider.setAddress("广东省广州市天河区华南植物园科学家之家");  // 必须设置一个惟一的特别的地址
		Provider providerCreate = createProvider(provider);
		//
		Provider providerToAddress = new Provider();
		providerToAddress.setDistrictID(-1);
		providerToAddress.setAddress(providerCreate.getAddress());
		//
		String error = providerToAddress.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForAddress = providerCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, providerToAddress);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = providerMapper.retrieveN(paramsForAddress); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForAddress.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// 检查RN出来的结果集是否与预期相同
		for (BaseModel bm : listBM) {
			Provider p = (Provider) bm;
			assertEquals(p.getAddress(), providerCreate.getAddress());
		}

		// 验证之后删除供应商
		deleteProvider(providerCreate);
	}

	@Test
	public void retrieveNTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:输入sContactName作为条件查询");
		Provider provider = DataInput.getProvider();
		provider.setContactName("苏斯克查"); //必须设置一个惟一的名字
		Provider providerCreate = createProvider(provider);
		//
		Provider providerToContactName = new Provider();
		providerToContactName.setDistrictID(-1);
		providerToContactName.setContactName(providerCreate.getContactName());
		//
		String error = providerToContactName.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForContactName = providerCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, providerToContactName);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = providerMapper.retrieveN(paramsForContactName); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForContactName.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// 检查RN出来的结果集是否与预期相同
		for (BaseModel bm : listBM) {
			Provider p = (Provider) bm;
			assertEquals(p.getContactName(), providerCreate.getContactName());
		}
		//
		// 验证之后删除供应商
		deleteProvider(providerCreate);
	}

	@Test
	public void retrieveNTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:输入sMobile作为条件查询");
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);

		Provider providerToMobile = new Provider();
		providerToMobile.setDistrictID(-1);
		providerToMobile.setMobile(providerCreate.getMobile());
		//
		String error = providerToMobile.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForMobile = providerCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, providerToMobile);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = providerMapper.retrieveN(paramsForMobile); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForMobile.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// 检查RN出来的结果集是否与预期相同
		for (BaseModel bm : listBM) {
			Provider p = (Provider) bm;
			assertEquals(p.getMobile(), providerCreate.getMobile());
		}

		// 验证之后删除供应商
		deleteProvider(providerCreate);
	}

	@Test
	public void retrieveNTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:不传任何值的情况");
		Provider providerToALL = new Provider();
		providerToALL.setDistrictID(-1);
		//
		String error = providerToALL.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForALL = providerToALL.getRetrieveNParam(BaseBO.INVALID_CASE_ID, providerToALL);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = providerMapper.retrieveN(paramsForALL); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForALL.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// 检查RN出来的结果集是否与预期相同
		for (int i = 0; i < listBM.size(); i++) {
			assertTrue(listBM.get(0).getID() >= listBM.get(i).getID());
		}
	}

	@Test
	public void retrieve1Test1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1 正常查询 ");
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);

		Map<String, Object> params = providerCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, providerCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel bm = providerMapper.retrieve1(params);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		assertNotNull(bm);

	}

	@Test
	public void retrieve1Test2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2 查询id为不存在的数据");
		Provider p2 = new Provider();
		p2.setID(-1);
		Map<String, Object> params2 = p2.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel bm2 = providerMapper.retrieve1(params2);
		//
		assertNull(bm2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:添加不重复的供应商名称和电话号码");
		// 先创建一个供应商
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);

		Provider p1 = DataInput.getProvider();
		p1.setID(providerCreate.getID());
		//
		String error = p1.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate = p1.getUpdateParam(BaseBO.INVALID_CASE_ID, p1);
		Provider providerUpdate = (Provider) providerMapper.update(paramsForUpdate); // ...
		Assert.assertNotNull(providerUpdate);
		if (p1.compareTo(providerUpdate) != 0) {
			Assert.assertTrue(false, "修改的对象的字段与读出的对象不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		//
		String error1 = providerUpdate.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error1, "");

		// 处理完操作之后，删除相应的数据
		deleteProvider(providerUpdate);
	}

	@Test
	public void updateTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:修改成重复的供应商名称");
		// 先创建一个供应商
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);

		Provider p2 = DataInput.getProvider();
		p2.setID(providerCreate.getID());
		p2.setName("华中供应商");
		//
		String error = p2.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate1 = p2.getUpdateParam(BaseBO.INVALID_CASE_ID, p2);
		Provider providerUpdate1 = (Provider) providerMapper.update(paramsForUpdate1); // ...
		Assert.assertTrue(providerUpdate1 == null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

		// 处理完操作之后，删除相应的数据
		deleteProvider(providerCreate);
	}

	@Test
	public void updateTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:修改成重复的供应商电话号码");
		// 先创建一个供应商
		Provider p = DataInput.getProvider();
		Provider providerCreate = createProvider(p);

		Provider p3 = DataInput.getProvider();
		p3.setID(providerCreate.getID());
		p3.setMobile("13129355442");
		//
		String error = p3.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate2 = p3.getUpdateParam(BaseBO.INVALID_CASE_ID, p3);
		Provider providerUpdate2 = (Provider) providerMapper.update(paramsForUpdate2); // ...
		Assert.assertTrue(providerUpdate2 == null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

		// 处理完操作之后，删除相应的数据
		deleteProvider(providerCreate);
	}

	@Test
	public void updateTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:修改时区域ID为0时进行修改");
		// 先创建一个供应商
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);

		// 修改供应商
		Provider p4 = DataInput.getProvider();
		p4.setID(providerCreate.getID());
		p4.setDistrictID(0);
		Map<String, Object> paramsForUpdate3 = p4.getUpdateParam(BaseBO.INVALID_CASE_ID, p4);
		Provider providerUpdate3 = (Provider) providerMapper.update(paramsForUpdate3); // ...
		assertNotNull(providerUpdate3);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// 处理完操作之后，删除相应的数据
		deleteProvider(providerUpdate3);
	}

	@Test
	public void updateTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:只传入供应商的名称，错误码为0");// ...这是什么case？
		// 先创建一个供应商
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);

		Provider p5 = DataInput.getProvider();
		p5.setID(providerCreate.getID());
		p5.setAddress("");
		p5.setContactName("");
		p5.setMobile("");
		//
		String error = p5.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate4 = p5.getUpdateParam(BaseBO.INVALID_CASE_ID, p5);
		Provider providerUpdate4 = (Provider) providerMapper.update(paramsForUpdate4); // ...
		Assert.assertTrue(providerUpdate4 != null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 进行字段检查
		checkCreateFunction(providerUpdate4);

		// 处理完操作之后，删除相应的数据
		deleteProvider(providerUpdate4);
	}

	@Test
	public void updateTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6：用不存在的区域");
		// 先创建一个供应商
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);

		// 修改供应商
		Provider p7 = DataInput.getProvider();
		p7.setDistrictID(INVALID_DISTRICTID);
		p7.setID(providerCreate.getID());
		//
		String error = p7.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate6 = p7.getUpdateParam(BaseBO.INVALID_CASE_ID, p7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerUpdate6 = (Provider) providerMapper.update(paramsForUpdate6); // ...
		Assert.assertNull(providerUpdate6);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);

		// 处理完操作之后，删除相应的数据
		deleteProvider(providerCreate);
	}

	@Test
	public void updateTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case 7:修改传入手机号码大于" + Provider.Max_LengthMoile);
		// 先创建一个供应商
		Provider provider = DataInput.getProvider();
		Provider providerCreate = createProvider(provider);
		// 修改供应商
		Provider p = DataInput.getProvider();
		p.setID(providerCreate.getID());
		p.setMobile("23457896578934657938456738496437896");
		//
		Assert.assertTrue(p.getMobile().length() > Provider.Max_LengthMoile);
		//
		try {
			Map<String, Object> paramsForUpdate7 = p.getUpdateParam(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider providerUpdate7 = (Provider) providerMapper.update(paramsForUpdate7);
			if (providerUpdate7 != null) {
				Assert.assertTrue(false, "update对象不应该成功！");
			}
		} catch (Exception e) {
			if (e.getCause() instanceof com.mysql.cj.jdbc.exceptions.MysqlDataTruncation) {
				Assert.assertTrue(true);
			} else {
				Assert.assertTrue(false, "异常不是期望的类型！");
			}
		}
		// 处理完操作之后，删除相应的数据
		deleteProvider(providerCreate);
	}

	@Test
	public void retrieveNByFieldsTest1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CAS1: 输入供应商名称模糊搜索供应商");
		Provider provider = new Provider();
		provider.setQueryKeyword("华");
		provider.setPageIndex(1);
		provider.setPageSize(10);
		Map<String, Object> params = provider.getRetrieveNParam(BaseBO.CASE_Provider_RetrieveNByFields, provider);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = providerMapper.retrieveNByFields(params);

		for (BaseModel bm : listBM) {
			Provider p = (Provider) bm;
			assertTrue(p.getName().contains(provider.getQueryKeyword()));
		}
	}

	@Test
	public void retrieveNByFieldsTest2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:输入联系人模糊搜索供应商");
		Provider provider = new Provider();
		provider.setQueryKeyword("T");
		provider.setPageIndex(1);
		provider.setPageSize(10);
		Map<String, Object> params = provider.getRetrieveNParam(BaseBO.CASE_Provider_RetrieveNByFields, provider);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = providerMapper.retrieveNByFields(params);

		for (BaseModel bm : listBM) {
			Provider p = (Provider) bm;
			assertTrue(p.getContactName().contains(provider.getQueryKeyword()));
		}
	}

	@Test
	public void retrieveNByFieldsTest3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:输入大于四位的供应商电话模糊搜索供应商");
		Provider provider = new Provider();
		provider.setQueryKeyword("1312");
		provider.setPageIndex(1);
		provider.setPageSize(10);
		Map<String, Object> params = provider.getRetrieveNParam(BaseBO.CASE_Provider_RetrieveNByFields, provider);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = providerMapper.retrieveNByFields(params);

		for (BaseModel bm : listBM) {
			Provider p = (Provider) bm;
			assertTrue(p.getMobile().contains(provider.getQueryKeyword()));
		}
	}

	@Test
	public void retrieveNByFieldsTest4() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:没有输入模糊搜索供应商");
		Provider provider = new Provider();
		provider.setPageIndex(1);
		provider.setPageSize(10);
		Map<String, Object> params = provider.getRetrieveNParam(BaseBO.CASE_Provider_RetrieveNByFields, provider);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listBM = providerMapper.retrieveNByFields(params);

		for (int i = 0; i < listBM.size(); i++) {
			assertTrue(listBM.get(0).getID() >= listBM.get(i).getID());
		}
	}

	@Test
	public void checkUniqueFieldTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:查询一个不存在的供应商名称");
		Provider provider1 = new Provider();
		provider1.setFieldToCheckUnique(CASE_CHECK_UNIQUE_PROVIDERNAME);
		provider1.setUniqueField("阿萨德佛该会否");
		Map<String, Object> params1 = provider1.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, provider1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.checkUniqueField(params1);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkUniqueFieldTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:查询一个已经存在的供应商名称");
		Provider provider2 = new Provider();
		provider2.setFieldToCheckUnique(CASE_CHECK_UNIQUE_PROVIDERNAME);
		provider2.setUniqueField("默认供应商");
		Map<String, Object> params2 = provider2.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, provider2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.checkUniqueField(params2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void checkUniqueFieldTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:查询一个已经存在的供应商名称,但传入的ID与已存在的供应商名称的供应商ID相同");
		Provider provider3 = new Provider();
		provider3.setID(2);
		provider3.setFieldToCheckUnique(CASE_CHECK_UNIQUE_PROVIDERNAME);
		provider3.setUniqueField("华南供应商");
		Map<String, Object> params3 = provider3.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, provider3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.checkUniqueField(params3);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkUniqueFieldTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:查询一个不存在的联系人电话");
		Provider provider4 = new Provider();
		provider4.setFieldToCheckUnique(CASE_CHECK_UNIQUE_PROVIDERPHONE);
		provider4.setUniqueField("93129355441");
		Map<String, Object> params4 = provider4.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, provider4);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.checkUniqueField(params4);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkUniqueFieldTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:查询一个已经存在的联系人电话");
		Provider provider5 = new Provider();
		provider5.setFieldToCheckUnique(CASE_CHECK_UNIQUE_PROVIDERPHONE);
		provider5.setUniqueField("13129355441");
		Map<String, Object> params5 = provider5.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, provider5);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.checkUniqueField(params5);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void checkUniqueFieldTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:查询一个已经存在的联系人电话,但传入的ID与已存在的联系人电话的供应商ID相同");
		Provider provider6 = new Provider();
		provider6.setID(2);
		provider6.setFieldToCheckUnique(CASE_CHECK_UNIQUE_PROVIDERPHONE);
		provider6.setUniqueField(("华南供应商"));
		Map<String, Object> params6 = provider6.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, provider6);

		providerMapper.checkUniqueField(params6);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	protected void deleteProvider(Provider provider) {
		// 没有供应商商品存在后删除成功
		Map<String, Object> paramsForDelete1 = provider.getDeleteParam(BaseBO.INVALID_CASE_ID, provider);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		providerMapper.delete(paramsForDelete1); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		//
		// Retrieve1确认该供应商已经被删除
		Map<String, Object> paramsForRetrieve1Provider = provider.getRetrieve1Param(BaseBO.INVALID_CASE_ID, provider);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel bmForRetrieve1Provider = providerMapper.retrieve1(paramsForRetrieve1Provider);
		Assert.assertTrue(bmForRetrieve1Provider == null && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve1Provider.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	protected Provider createProvider(Provider provider) throws CloneNotSupportedException, InterruptedException {

		// 传入参数字段验证
		checkCreateFunction(provider);

		// 创建一个供应商
		Map<String, Object> params = provider.getCreateParam(BaseBO.INVALID_CASE_ID, provider);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerCreate = (Provider) providerMapper.create(params); // ...
		provider.setIgnoreIDInComparision(true);
		if (provider.compareTo(providerCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(providerCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		// 字段验证
		checkCreateFunction(providerCreate);

		return providerCreate;
	}

	/** @param provider
	 */
	protected void checkCreateFunction(Provider provider) {
		String error = provider.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

}
