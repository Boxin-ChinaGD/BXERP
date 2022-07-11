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
import com.bx.erp.model.VipCard;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Vip;
import com.bx.erp.util.DataSourceContextHolder;

public class VipCardCodeMapperTest extends BaseMapperTest {
	protected static final int vipID = 1;

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
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 创建一张会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(vipID, vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		//
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:会员ID不存在");
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 创建一张会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(BaseAction.INVALID_ID, vipCardCreate.getID());
		Map<String, Object> params = vipCardCode.getCreateParam(BaseBO.INVALID_CASE_ID, vipCardCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCardCode vipCardCodeCreate = (VipCardCode) vipCardCodeMapper.create(params);
		Assert.assertTrue(vipCardCodeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				"创建对象失败。param=" + params + "\n\r vipCardCode=" + vipCardCode);
		//
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case3:会员卡ID不存在");
		// 创建一张会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(vipID, BaseAction.INVALID_ID);
		Map<String, Object> params = vipCardCode.getCreateParam(BaseBO.INVALID_CASE_ID, vipCardCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCardCode vipCardCodeCreate = (VipCardCode) vipCardCodeMapper.create(params);
		Assert.assertTrue(vipCardCodeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				"创建对象失败。param=" + params + "\n\r vipCardCode=" + vipCardCode);
	}

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:查询单张会员卡");
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 创建一张会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(vipID, vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		//
		VipCardCode vipCardCodeRetrieve1 = (VipCardCode) BaseVipCardCodeTest.retrieve1ViaMapper(vipCardCodeCreate);
		Assert.assertTrue(vipCardCodeRetrieve1 != null, "查询单张会员卡失败");
		//
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:查询多张会员卡");
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 创建一张会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(vipID, vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		//
		Map<String, Object> params = vipCardCodeCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vipCardCodeCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> vipCardCodeRetrieveN = vipCardCodeMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : vipCardCodeRetrieveN) {
			VipCardCode vcc = (VipCardCode) bm;
			String sn = vcc.getSN();
			vcc.setSN(null);
			//
			err = vcc.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
			//
			vcc.setSN(sn);
		}
		//
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:根据会员查询会员卡");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 查找
		VipCardCode queryVipCardCode = new VipCardCode();
		queryVipCardCode.setVipID(createVip.getID());
		List<BaseModel> list = BaseVipCardCodeTest.retrieveNViaMapper(queryVipCardCode, Shared.DBName_Test);
		// 检查
		for (BaseModel baseModel : list) {
			VipCardCode vipCardCode2 = (VipCardCode) baseModel;
			if (vipCardCode2.getVipID() != createVip.getID()) {
				Assert.assertTrue(false, "查找到不会本会员的会员卡");
			}
		}
		// 删除数据
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
		BaseVipTest.deleteViaMapper(createVip);
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常添加");
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 创建一张会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(vipID, vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 删除一张会员卡
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		//
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}
}
