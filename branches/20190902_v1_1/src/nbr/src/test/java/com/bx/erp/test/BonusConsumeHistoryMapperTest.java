package com.bx.erp.test;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BonusConsumeHistory;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Vip;
import com.bx.erp.util.DataSourceContextHolder;

public class BonusConsumeHistoryMapperTest extends BaseMapperTest {
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
		//
		Shared.caseLog("Case1:正常添加");
		//
		BonusConsumeHistory bonusConsumeHistory = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(Shared.DEFAULT_VIP_ID);
		BonusConsumeHistory bonusConsumeHistoryCreate = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistory);
		//
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:会员ID不存在");
		//
		BonusConsumeHistory bonusConsumeHistory = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(BaseAction.INVALID_ID); // ...
		Map<String, Object> params = bonusConsumeHistory.getCreateParam(BaseBO.INVALID_CASE_ID, bonusConsumeHistory);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BonusConsumeHistory bonusConsumeHistoryCreate = (BonusConsumeHistory) bonusConsumeHistoryMapper.create(params);
		Assert.assertTrue(bonusConsumeHistoryCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				"创建对象失败。param=" + params + "\n\r bonusConsumeHistory=" + bonusConsumeHistory);
	}

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常查询单个积分历史");
		//
		BonusConsumeHistory bonusConsumeHistory = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(Shared.DEFAULT_VIP_ID);
		BonusConsumeHistory bonusConsumeHistoryCreate = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistory);
		//
		BaseModel bonusConsumeHistoryRetrieve1 = BaseBonusConsumeHistoryTest.retrieve1ViaMapper(bonusConsumeHistoryCreate);
		Assert.assertTrue(bonusConsumeHistoryRetrieve1 != null);
		//
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate);
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:不根据条件查询所有积分历史");
		//
		BonusConsumeHistory bonusConsumeHistory = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(Shared.DEFAULT_VIP_ID);
		BonusConsumeHistory bonusConsumeHistoryCreate = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistory);
		//
		BonusConsumeHistory bonusConsumeHistoryToRN = new BonusConsumeHistory();
		bonusConsumeHistoryToRN.setVipID(BaseAction.INVALID_ID);
		List<BaseModel> bonusConsumeHistoryRetrieveN = BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		//
		Assert.assertTrue(bonusConsumeHistoryRetrieveN != null && bonusConsumeHistoryRetrieveN.size() > 0);
		//
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate);
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:根据VipID查询积分历史");
		//
		BonusConsumeHistory bonusConsumeHistory = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(Shared.DEFAULT_VIP_ID);
		BonusConsumeHistory bonusConsumeHistoryCreate = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistory);
		//
		BonusConsumeHistory bonusConsumeHistoryToRN = new BonusConsumeHistory();
		bonusConsumeHistoryToRN.setVipID(1);
		List<BaseModel> bonusConsumeHistoryRetrieveN = BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		//
		Assert.assertTrue(bonusConsumeHistoryRetrieveN != null && bonusConsumeHistoryRetrieveN.size() > 0);
		//
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate);
	}

	@Test
	public void retrieveNTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case3:会员ID不存在");
		BonusConsumeHistory bonusConsumeHistoryToRN = new BonusConsumeHistory();
		bonusConsumeHistoryToRN.setVipID(Shared.BIG_ID);
		BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, Shared.DBName_Test, EnumErrorCode.EC_NoSuchData);
	}
	
	@Test
	public void retrieveNTest4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case4:传vipID = -1, mobile = 会员手机号, name = '',则根据会员手机号查询");
		// 新建Vip
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 新建会员积分
		BonusConsumeHistory bonusConsumeHistoryGet = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(vipCreate.getID());
		BonusConsumeHistory bonusConsumeHistoryCreate = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistoryGet);
		BonusConsumeHistory bonusConsumeHistoryToRN = new BonusConsumeHistory();
		bonusConsumeHistoryToRN.setPageIndex(BaseAction.PAGE_StartIndex);
		bonusConsumeHistoryToRN.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		bonusConsumeHistoryToRN.setVipID(BaseAction.INVALID_ID);
		bonusConsumeHistoryToRN.setVipMobile(vipCreate.getMobile());
		List<BaseModel> bmList = BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		BonusConsumeHistory bonusConsumeHistory = (BonusConsumeHistory) bmList.get(0);
		Assert.assertTrue(bonusConsumeHistory.getVipID() == vipCreate.getID(), "根据手机号查询，期望结果返回该手机号的会员的积分历史");
		// 删除测试数据
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate);
		BaseVipTest.deleteViaMapper(vipCreate);
	}
	
	@Test
	public void retrieveNTest5() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case5:传vipID = -1, mobile = '',name不为空,则根据会员昵称模糊查询");
		String sName = "行者" + Shared.generateVipName(20);
		BonusConsumeHistory bonusConsumeHistoryToRN = new BonusConsumeHistory();
		bonusConsumeHistoryToRN.setPageIndex(BaseAction.PAGE_StartIndex);
		bonusConsumeHistoryToRN.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		bonusConsumeHistoryToRN.setVipID(BaseAction.INVALID_ID);
		bonusConsumeHistoryToRN.setVipMobile("");
		bonusConsumeHistoryToRN.setVipName(sName);
		List<BaseModel> bmList = BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		int oldTotalRecord = bmList.size();
		// 新建Vip1
		Vip vipGet1 = BaseVipTest.DataInput.getVip();
		vipGet1.setName("孙" + sName);
		Vip vipCreate1 = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet1, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 新建Vip2
		Vip vipGet2 = BaseVipTest.DataInput.getVip();
		vipGet2.setName(sName + "孙");
		Vip vipCreate2 = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet2, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 新建Vip3
		Vip vipGet3 = BaseVipTest.DataInput.getVip();
		vipGet3.setName("猪八戒");
		Vip vipCreate3 = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet3, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 新建会员积分1
		BonusConsumeHistory bonusConsumeHistoryGet1 = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(vipCreate1.getID());
		BonusConsumeHistory bonusConsumeHistoryCreate1 = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistoryGet1);
		// 新建会员积分2
		BonusConsumeHistory bonusConsumeHistoryGet2 = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(vipCreate2.getID());
		BonusConsumeHistory bonusConsumeHistoryCreate2 = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistoryGet2);
		// 新建会员积分3
		BonusConsumeHistory bonusConsumeHistoryGet3 = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(vipCreate3.getID());
		BonusConsumeHistory bonusConsumeHistoryCreate3 = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistoryGet3);
		bonusConsumeHistoryToRN.setVipID(BaseAction.INVALID_ID);
		bmList = BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		Assert.assertTrue(bmList.size() == oldTotalRecord + 2, "根据名字查询，期望结果是返回新创建的会员积分");
		boolean existVip1 = false;
		boolean existVip2 = false;
		boolean existVip3 = false;
		for(int i = 0; i < bmList.size(); i++) {
			BonusConsumeHistory bonusConsumeHistory = (BonusConsumeHistory) bmList.get(i);
			if(bonusConsumeHistory.getVipID() == vipCreate1.getID()) {
				existVip1 = true;
			} else if(bonusConsumeHistory.getVipID() == vipCreate2.getID()) {
				existVip2 = true;
			} else if(bonusConsumeHistory.getVipID() == vipCreate3.getID()) {
				existVip3 = true;
			}
		}
		Assert.assertTrue(existVip1 && existVip2 && !existVip3, "期望是返回新创建的vip1、vip2,不返回vip3");
		// 删除测试数据
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate1);
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate2);
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate3);
		BaseVipTest.deleteViaMapper(vipCreate1);
		BaseVipTest.deleteViaMapper(vipCreate2);
		BaseVipTest.deleteViaMapper(vipCreate3);
	}
	
	@Test
	public void retrieveNTest6() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case6:传vipID不等于-1, mobile = '',name = '',则根据会员ID查询");
		String sName = "行者" + Shared.generateVipName(20);
		BonusConsumeHistory bonusConsumeHistoryToRN = new BonusConsumeHistory();
		bonusConsumeHistoryToRN.setPageIndex(BaseAction.PAGE_StartIndex);
		bonusConsumeHistoryToRN.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		bonusConsumeHistoryToRN.setVipMobile("");
		bonusConsumeHistoryToRN.setVipName(sName);
		// 新建Vip1
		Vip vipGet1 = BaseVipTest.DataInput.getVip();
		vipGet1.setName("孙" + sName);
		Vip vipCreate1 = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet1, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 新建Vip2
		Vip vipGet2 = BaseVipTest.DataInput.getVip();
		vipGet2.setName(sName + "孙");
		Vip vipCreate2 = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet2, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 新建Vip3
		Vip vipGet3 = BaseVipTest.DataInput.getVip();
		vipGet3.setName("猪八戒");
		Vip vipCreate3 = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vipGet3, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 新建会员积分1
		BonusConsumeHistory bonusConsumeHistoryGet1 = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(vipCreate1.getID());
		BonusConsumeHistory bonusConsumeHistoryCreate1 = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistoryGet1);
		// 新建会员积分2
		BonusConsumeHistory bonusConsumeHistoryGet2 = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(vipCreate2.getID());
		BonusConsumeHistory bonusConsumeHistoryCreate2 = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistoryGet2);
		// 新建会员积分3
		BonusConsumeHistory bonusConsumeHistoryGet3 = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(vipCreate3.getID());
		BonusConsumeHistory bonusConsumeHistoryCreate3 = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistoryGet3);
		bonusConsumeHistoryToRN.setVipID(vipCreate1.getID());
		List<BaseModel> bmList = BaseBonusConsumeHistoryTest.retrieveNViaMapper(bonusConsumeHistoryToRN, Shared.DBName_Test, EnumErrorCode.EC_NoError);
		Assert.assertTrue(bmList.size() == 1, "根据vipID查询，期望结果是返回会员ID为vipID的积分历史");
		boolean existVip1 = false;
		boolean existVip2 = false;
		boolean existVip3 = false;
		for(int i = 0; i < bmList.size(); i++) {
			BonusConsumeHistory bonusConsumeHistory = (BonusConsumeHistory) bmList.get(i);
			if(bonusConsumeHistory.getVipID() == vipCreate1.getID()) {
				existVip1 = true;
			} else if(bonusConsumeHistory.getVipID() == vipCreate2.getID()) {
				existVip2 = true;
			} else if(bonusConsumeHistory.getVipID() == vipCreate3.getID()) {
				existVip3 = true;
			}
		}
		Assert.assertTrue(existVip1 && !existVip2 && !existVip3, "期望是返回新创建的vip1,不返回vip2,vip3");
		// 删除测试数据
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate1);
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate2);
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate3);
		BaseVipTest.deleteViaMapper(vipCreate1);
		BaseVipTest.deleteViaMapper(vipCreate2);
		BaseVipTest.deleteViaMapper(vipCreate3);
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常删除单个积分历史");
		//
		BonusConsumeHistory bonusConsumeHistory = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(1); // ...
		BonusConsumeHistory bonusConsumeHistoryCreate = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistory);
		//
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate);
	}
}
