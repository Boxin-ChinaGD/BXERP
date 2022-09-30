package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.StaffPermissionCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.model.Staff;
import com.bx.erp.model.StaffPermission;
import com.bx.erp.util.DatetimeUtil;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class RetailTradeAggregationActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();

		try {
			sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1 :唯一键StaffWorkTiome不重复");

		RetailTradeAggregation rAggregation = getRetailTradeAggregationInstance();
		rAggregation.setAmount(5000);
		rAggregation.setCashAmount(4000);
		rAggregation.setWechatAmount(1000);
		rAggregation.setAlipayAmount(0);
		MvcResult mr = mvc.perform(post("/retailTradeAggregation/createEx.bx") //
				.param(RetailTradeAggregation.field.getFIELD_NAME_staffID(), String.valueOf(rAggregation.getStaffID())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_posID(), String.valueOf(1)) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeStart(), String.valueOf(rAggregation.getWorkTimeStart())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeEnd(), String.valueOf(rAggregation.getWorkTimeEnd())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_tradeNO(), String.valueOf(rAggregation.getTradeNO())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_amount(), String.valueOf(rAggregation.getAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_reserveAmount(), String.valueOf(rAggregation.getReserveAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_cashAmount(), String.valueOf(rAggregation.getCashAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_wechatAmount(), String.valueOf(rAggregation.getWechatAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_alipayAmount(), String.valueOf(rAggregation.getAlipayAmount())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2 :唯一键StaffWorkTiome重复时");
		// 首次创建
		RetailTradeAggregation rAggregation = getRetailTradeAggregationInstance();
		rAggregation.setAmount(5000);
		rAggregation.setCashAmount(4000);
		rAggregation.setWechatAmount(1000);
		rAggregation.setAlipayAmount(0);
		MvcResult mr = mvc.perform(post("/retailTradeAggregation/createEx.bx") //
				.param(RetailTradeAggregation.field.getFIELD_NAME_staffID(), String.valueOf(rAggregation.getStaffID())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_posID(), String.valueOf(1)) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeStart(), String.valueOf(rAggregation.getWorkTimeStart())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeEnd(), String.valueOf(rAggregation.getWorkTimeEnd())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_tradeNO(), String.valueOf(rAggregation.getTradeNO())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_amount(), String.valueOf(rAggregation.getAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_reserveAmount(), String.valueOf(rAggregation.getReserveAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_cashAmount(), String.valueOf(rAggregation.getCashAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_wechatAmount(), String.valueOf(rAggregation.getWechatAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_alipayAmount(), String.valueOf(rAggregation.getAlipayAmount())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		// 再次创建
		MvcResult mr2 = mvc.perform(post("/retailTradeAggregation/createEx.bx") //
				.param(RetailTradeAggregation.field.getFIELD_NAME_staffID(), String.valueOf(rAggregation.getStaffID())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_posID(), String.valueOf(1)) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeStart(), String.valueOf(rAggregation.getWorkTimeStart())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeEnd(), String.valueOf(rAggregation.getWorkTimeEnd())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_tradeNO(), String.valueOf(rAggregation.getTradeNO())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_amount(), String.valueOf(rAggregation.getAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_reserveAmount(), String.valueOf(rAggregation.getReserveAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_cashAmount(), String.valueOf(rAggregation.getCashAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_wechatAmount(), String.valueOf(rAggregation.getWechatAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_alipayAmount(), String.valueOf(rAggregation.getAlipayAmount())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3 :用不存在posID创建");
		RetailTradeAggregation rAggregation = getRetailTradeAggregationInstance();
		MvcResult mr = mvc.perform(post("/retailTradeAggregation/createEx.bx") //
				.param(RetailTradeAggregation.field.getFIELD_NAME_staffID(), String.valueOf(rAggregation.getStaffID())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_posID(), String.valueOf(BaseAction.INVALID_POS_ID)) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeStart(), String.valueOf(rAggregation.getWorkTimeStart())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeEnd(), String.valueOf(rAggregation.getWorkTimeEnd())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_tradeNO(), String.valueOf(rAggregation.getTradeNO())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_amount(), String.valueOf(rAggregation.getAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_reserveAmount(), String.valueOf(rAggregation.getReserveAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_cashAmount(), String.valueOf(rAggregation.getCashAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_wechatAmount(), String.valueOf(rAggregation.getWechatAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_alipayAmount(), String.valueOf(rAggregation.getAlipayAmount())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, RetailTradeAggregation.FIELD_ERROR_PosID, "错误信息与预期的不相符");
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4:没有权限进行操作");
//		RetailTradeAggregation rAggregation = getRetailTradeAggregationInstance();
//		MvcResult mr5 = mvc.perform(post("/retailTradeAggregation/createEx.bx") //
//				.param(RetailTradeAggregation.field.getFIELD_NAME_staffID(), String.valueOf(rAggregation.getStaffID())) //
//				.param(RetailTradeAggregation.field.getFIELD_NAME_posID(), String.valueOf(1)) //
//				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeStart(), String.valueOf(rAggregation.getWorkTimeStart())) //
//				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeEnd(), String.valueOf(rAggregation.getWorkTimeEnd())) //
//				.param(RetailTradeAggregation.field.getFIELD_NAME_tradeNO(), String.valueOf(rAggregation.getTradeNO())) //
//				.param(RetailTradeAggregation.field.getFIELD_NAME_amount(), String.valueOf(rAggregation.getAmount())) //
//				.param(RetailTradeAggregation.field.getFIELD_NAME_reserveAmount(), String.valueOf(rAggregation.getReserveAmount())) //
//				.param(RetailTradeAggregation.field.getFIELD_NAME_cashAmount(), String.valueOf(rAggregation.getCashAmount())) //
//				.param(RetailTradeAggregation.field.getFIELD_NAME_wechatAmount(), String.valueOf(rAggregation.getWechatAmount())) //
//				.param(RetailTradeAggregation.field.getFIELD_NAME_alipayAmount(), String.valueOf(rAggregation.getAlipayAmount())) //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		).andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:售前账号进行创建收银汇总。");
		HttpSession session1 = Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale);
		RetailTradeAggregation rAggregation = getRetailTradeAggregationInstance();
		MvcResult mr6 = mvc.perform(post("/retailTradeAggregation/createEx.bx") //
				.param(RetailTradeAggregation.field.getFIELD_NAME_posID(), String.valueOf(rAggregation.getPosID())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeStart(), String.valueOf(rAggregation.getWorkTimeStart())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeEnd(), String.valueOf(rAggregation.getWorkTimeEnd())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_tradeNO(), String.valueOf(rAggregation.getTradeNO())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_amount(), String.valueOf(rAggregation.getAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_reserveAmount(), String.valueOf(rAggregation.getReserveAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_cashAmount(), String.valueOf(rAggregation.getCashAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_wechatAmount(), String.valueOf(rAggregation.getWechatAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_alipayAmount(), String.valueOf(rAggregation.getAlipayAmount())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) session1)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoPermission);
		// 检查售前人员的权限数目是否和设定的数目一样
		List<BaseModel> staffPermissionList = new ArrayList<BaseModel>();
		StaffPermissionCache spc = (StaffPermissionCache) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_StaffPermission);
		List<BaseModel> readNCache = spc.readN(false, false);
		// 重新刷新一下
		spc.load(Shared.DBName_Test); // register后会delete掉，缓存ht为空，所以要从CacheManager获取缓存
		StaffPermissionCache spc2 = (StaffPermissionCache) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_StaffPermission);
		readNCache = spc2.readN(false, false);
		Assert.assertTrue(readNCache != null && readNCache.size() > 0, "读出的权限为空");
		Staff staff = (Staff) session1.getAttribute(EnumSession.SESSION_Staff.getName());
		Assert.assertTrue(staff != null, "会话中的staff对象为空！");
		for (BaseModel baseModel : readNCache) {
			StaffPermission s = (StaffPermission) baseModel;
			if (s.getStaffID() == staff.getID()) {
				staffPermissionList.add(s);
			}
		}
		// int preSaleStaffPermissionNum = 29;// 这个值是根据数据库的t_role_permission表中F_RoleID =
		// 6的数据个数确定的
		int preSaleStaffPermissionNum = 40; // ...感觉需要重构 应该需要加个sp查个的数量 而不是写死 不然很容易出问题.Giggs：从缓存中根据SP名称这个常量查找即可 TODO
		Assert.assertTrue(staffPermissionList.size() == preSaleStaffPermissionNum, "设置的权限和读出的权限数目不一样。" + staffPermissionList.size() + "不等于" + preSaleStaffPermissionNum);
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6 :上班时间比下班时间晚");
		RetailTradeAggregation rAggregation = getRetailTradeAggregationInstance();
		MvcResult mr = mvc.perform(post("/retailTradeAggregation/createEx.bx") //
				.param(RetailTradeAggregation.field.getFIELD_NAME_posID(), String.valueOf(1)) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeStart(), String.valueOf(rAggregation.getWorkTimeEnd())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeEnd(), String.valueOf(rAggregation.getWorkTimeStart())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_tradeNO(), String.valueOf(rAggregation.getTradeNO())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_amount(), String.valueOf(rAggregation.getAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_reserveAmount(), String.valueOf(rAggregation.getReserveAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_cashAmount(), String.valueOf(rAggregation.getCashAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_wechatAmount(), String.valueOf(rAggregation.getWechatAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_alipayAmount(), String.valueOf(rAggregation.getAlipayAmount())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, "上班时间不能比下班时间晚", "上班时间不能比下班时间晚");
	}

	@Test
	public void testRetrieve1Ex1() throws Exception {
		Shared.printTestMethodStartInfo();
		// 刷新小王子后等一分钟后运行该方法，第二次运行该方法要隔一分钟，这样就保证只返回一个对象
		RetailTradeAggregation rAggregation = getRetailTradeAggregationInstance();
		rAggregation.setAmount(5000);
		rAggregation.setCashAmount(4000);
		rAggregation.setWechatAmount(1000);
		rAggregation.setAlipayAmount(0);
		MvcResult mr = mvc.perform(post("/retailTradeAggregation/createEx.bx") //
				.param(RetailTradeAggregation.field.getFIELD_NAME_posID(), String.valueOf(1)) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeStart(), String.valueOf(rAggregation.getWorkTimeStart())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_workTimeEnd(), String.valueOf(rAggregation.getWorkTimeEnd())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_tradeNO(), String.valueOf(rAggregation.getTradeNO())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_amount(), String.valueOf(rAggregation.getAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_reserveAmount(), String.valueOf(rAggregation.getReserveAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_cashAmount(), String.valueOf(rAggregation.getCashAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_wechatAmount(), String.valueOf(rAggregation.getWechatAmount())) //
				.param(RetailTradeAggregation.field.getFIELD_NAME_alipayAmount(), String.valueOf(rAggregation.getAlipayAmount())) //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		MvcResult mr2 = mvc.perform(//
				get("/retailTradeAggregation/retrieve1Ex.bx?" + RetailTradeAggregation.field.getFIELD_NAME_staffID() + "=4")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
		retailTradeAggregation = (RetailTradeAggregation) retailTradeAggregation.parse1(o2.getString(BaseAction.KEY_Object));
		Assert.assertTrue(retailTradeAggregation != null, "没有查询出来刚创建的对象");
	}

	private RetailTradeAggregation getRetailTradeAggregationInstance() throws Exception {
		RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
		retailTradeAggregation.setPosID(1);
		retailTradeAggregation.setWorkTimeStart(new Date());
		Thread.sleep(1000);
		retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.getDate(new Date(), 1));
		retailTradeAggregation.setTradeNO(143);
		retailTradeAggregation.setAmount(5498);
		retailTradeAggregation.setReserveAmount(500);
		retailTradeAggregation.setCashAmount(1237);
		retailTradeAggregation.setWechatAmount(2874);
		retailTradeAggregation.setAlipayAmount(1854);
		return (RetailTradeAggregation) retailTradeAggregation.clone();
	}

}
