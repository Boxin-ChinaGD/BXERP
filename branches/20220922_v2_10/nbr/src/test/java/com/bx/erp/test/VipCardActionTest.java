package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.VipCard;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BonusRule;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@WebAppConfiguration
public class VipCardActionTest extends BaseActionTest {
	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:会员卡积分清零规则天数合法，积分清零规则日期非法");
		// 创建会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 修改会员卡
		VipCard vipToUpdate = (VipCard) vipCreate.clone();
		vipToUpdate.setTitle("修改会员卡");
		vipToUpdate.setBackgroundColor("255,255,255;255,255,255");
		vipToUpdate.setClearBonusDay(100);
		vipToUpdate.setClearBonusDatetime(null);
		//
		BaseVipCardTest.updateViaAction(mvc, sessionBoss, vipToUpdate, EnumErrorCode.EC_NoError);
		//
		BaseVipCardTest.deleteViaMapper(vipCreate);
	}
	
	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:会员卡积分清零规则天数非法，积分清零规则日期合法");
		// 创建会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 修改会员卡
		VipCard vipToUpdate = (VipCard) vipCreate.clone();
		vipToUpdate.setTitle("修改会员卡");
		vipToUpdate.setBackgroundColor("255,255,255;255,255,255");
		vipToUpdate.setClearBonusDay(0);
		vipToUpdate.setClearBonusDatetime(new Date());
		//
		BaseVipCardTest.updateViaAction(mvc, sessionBoss, vipToUpdate, EnumErrorCode.EC_NoError);
		//
		BaseVipCardTest.deleteViaMapper(vipCreate);
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:没有权限");
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		vipCard.setID(Shared.DEFAULT_VIP_ID);
		//
		BaseVipCardTest.updateViaAction(mvc, sessionCashier, vipCard, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:修改会员卡积分清零规则天数和积分清零规则日期都非法");
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		vipCard.setID(Shared.DEFAULT_VIP_ID);
		vipCard.setClearBonusDay(-500);
		vipCard.setClearBonusDatetime(null);
		//
		BaseVipCardTest.updateViaAction(mvc, sessionBoss, vipCard, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieve1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("正常查询");
		VipCard vipCard = new VipCard();
		vipCard.setID(Shared.DEFAULT_VIP_ID);
		//
		MvcResult mr = mvc.perform(//
				get("/vipCard/retrieve1Ex.bx?ID=" + Shared.DEFAULT_VIP_ID)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		
		BaseModel vipCardR1 = Shared.parse1Object(mr, vipCard, BaseAction.KEY_Object);
		Assert.assertTrue(vipCardR1 != null && vipCardR1 instanceof VipCard, "查询会员卡失败");
		BaseModel bonusRuleR1 = Shared.parse1Object(mr, new BonusRule(), BaseAction.KEY_Object2);
		Assert.assertTrue(bonusRuleR1 != null && bonusRuleR1 instanceof BonusRule, "查询积分规则失败");
		
	}
}
