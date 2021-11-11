package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BonusConsumeHistory;
import com.bx.erp.model.Vip;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@WebAppConfiguration
public class BonusConsumeHistoryActionTest extends BaseActionTest {

	@Test
	public void testRetrieveNEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1 : 正常查询积分历史");
		BonusConsumeHistory bonusConsumeHistory = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(Shared.DEFAULT_VIP_ID);
		BonusConsumeHistory bonusConsumeHistoryCreate = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistory);
		//
		MvcResult mr = mvc.perform(//
				post("/bonusConsumeHistory/retrieveNEx.bx?vipID=" + BaseAction.INVALID_ID)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);

		List<?> bonusConsumeHistoryList = Shared.parseNObject(mr, bonusConsumeHistory, BaseAction.KEY_ObjectList);
		Assert.assertTrue(bonusConsumeHistoryList.size() > 0, "查询积分历史失败");
		for (Object o : bonusConsumeHistoryList) {
			Assert.assertTrue(o instanceof BonusConsumeHistory, "查询积分历史失败");
		}
		//
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate);
	}
	
	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2 : 根据VipID查询积分历史");
		BonusConsumeHistory bonusConsumeHistory = BaseBonusConsumeHistoryTest.DataInput.getBonusConsumeHistory(Shared.DEFAULT_VIP_ID);
		BonusConsumeHistory bonusConsumeHistoryCreate = BaseBonusConsumeHistoryTest.createViaMapper(bonusConsumeHistory);
		//
		MvcResult mr = mvc.perform(//
				post("/bonusConsumeHistory/retrieveNEx.bx?vipID=" + Shared.DEFAULT_VIP_ID)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);

		List<?> bonusConsumeHistoryList = Shared.parseNObject(mr, bonusConsumeHistory, BaseAction.KEY_ObjectList);
		Assert.assertTrue(bonusConsumeHistoryList.size() > 0, "查询积分历史失败");
		for (Object o : bonusConsumeHistoryList) {
			BonusConsumeHistory bch = (BonusConsumeHistory) o;
			Assert.assertTrue(bch.getVipID() == Shared.DEFAULT_VIP_ID, "查询积分历史失败");
		}
		//
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate);
	}
	
	@Test
	public void testRetrieveNEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3 : VipID不存在");
		MvcResult mr = mvc.perform(//
				post("/bonusConsumeHistory/retrieveNEx.bx?vipID=" + Shared.BIG_ID)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("Case4 : 传vipID = -1, mobile = 会员手机号, name = '',则根据会员手机号查询");
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
		List<BonusConsumeHistory> bmList = (List<BonusConsumeHistory>) BaseBonusConsumeHistoryTest.retrieveNViaAction(bonusConsumeHistoryToRN, mvc, sessionBoss);
		Assert.assertTrue(bmList.size() == 1, "根据手机号查询，期望结果是返回一条数据");
		BonusConsumeHistory bonusConsumeHistory = (BonusConsumeHistory) bmList.get(0);
		Assert.assertTrue(bonusConsumeHistory.getVipID() == vipCreate.getID(), "根据手机号查询，期望结果返回该手机号的会员的积分历史");
		// 删除测试数据
		BaseBonusConsumeHistoryTest.deleteViaMapper(bonusConsumeHistoryCreate);
		BaseVipTest.deleteViaMapper(vipCreate);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx5() throws Exception {
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
		List<BonusConsumeHistory> bmList = (List<BonusConsumeHistory>) BaseBonusConsumeHistoryTest.retrieveNViaAction(bonusConsumeHistoryToRN, mvc, sessionBoss);
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
		//
		bmList = (List<BonusConsumeHistory>) BaseBonusConsumeHistoryTest.retrieveNViaAction(bonusConsumeHistoryToRN, mvc, sessionBoss);
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveNEx6() throws Exception {
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
		//
		List<BonusConsumeHistory> bmList = (List<BonusConsumeHistory>) BaseBonusConsumeHistoryTest.retrieveNViaAction(bonusConsumeHistoryToRN, mvc, sessionBoss);
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
}
