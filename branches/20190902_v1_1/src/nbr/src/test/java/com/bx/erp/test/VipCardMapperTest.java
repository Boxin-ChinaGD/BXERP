package com.bx.erp.test;

import java.util.Date;
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
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

public class VipCardMapperTest extends BaseMapperTest {
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
		// 创建会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		//
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void updateTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:会员卡积分清零规则天数合法，积分清零规则日期非法");
		// 创建会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 修改会员卡
		VipCard vipCardToUpdate = new VipCard();
		vipCardToUpdate.setID(vipCardCreate.getID());
		vipCardToUpdate.setTitle("会员卡修改");
		vipCardToUpdate.setBackgroundColor("255,255,255;255,255,255");
		vipCardToUpdate.setClearBonusDay(3650);
		vipCardToUpdate.setClearBonusDatetime(null);
		VipCard vipCardUpdate = BaseVipCardTest.updateViaMapper(vipCardToUpdate);
		//
		BaseVipCardTest.deleteViaMapper(vipCardUpdate);
	}
	
	@Test
	public void updateTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:会员卡积分清零规则天数非法，积分清零规则日期合法");
		// 创建会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 修改会员卡
		VipCard vipCardToUpdate = new VipCard();
		vipCardToUpdate.setID(vipCardCreate.getID());
		vipCardToUpdate.setTitle("会员卡修改");
		vipCardToUpdate.setBackgroundColor("255,255,255;255,255,255");
		vipCardToUpdate.setClearBonusDay(0);
		vipCardToUpdate.setClearBonusDatetime(new Date());
		VipCard vipCardUpdate = BaseVipCardTest.updateViaMapper(vipCardToUpdate);
		//
		BaseVipCardTest.deleteViaMapper(vipCardUpdate);
	}

	@Test
	public void updateTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case3:修改会员卡不存在");
		// 修改会员卡
		VipCard vipCardToUpdate = new VipCard();
		vipCardToUpdate.setID(BaseAction.INVALID_ID);
		vipCardToUpdate.setTitle("会员卡修改");
		vipCardToUpdate.setBackgroundColor("255,255,255;255,255,255");
		vipCardToUpdate.setClearBonusDay(3650);
		vipCardToUpdate.setClearBonusDatetime(null);
		Map<String, Object> params = vipCardToUpdate.getUpdateParam(BaseBO.INVALID_CASE_ID, vipCardToUpdate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCardMapper.update(params);
		Assert.assertTrue(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("会员卡不存在") && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				"修改对象失败。param=" + params + "\n\r vipCard=" + vipCardToUpdate);
	}

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:查询单种会员卡");
		// 创建会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		//
		VipCard vipCardRetrieve1 = (VipCard) BaseVipCardTest.retrieve1ViaMapper(vipCardCreate);
		Assert.assertTrue(vipCardRetrieve1 != null, "查询会员卡失败");
		//
		BaseVipCardTest.deleteViaMapper(vipCardRetrieve1);
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:查询多种会员卡");
		// 创建会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		//
		Map<String, Object> params = vipCard.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vipCard);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> vipCardRetrieveN = vipCardMapper.retrieveN(params);
		//
		Assert.assertTrue(vipCardRetrieveN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : vipCardRetrieveN) {
			VipCard vc = (VipCard) bm;
			err = vc.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常删除");
		// 创建会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 删除会员卡
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}
}
