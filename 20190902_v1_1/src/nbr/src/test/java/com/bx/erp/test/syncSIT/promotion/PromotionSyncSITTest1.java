package com.bx.erp.test.syncSIT.promotion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionSyncCache;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.test.Shared;
import com.bx.erp.test.syncSIT.BaseSyncSITTest1;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread1;
import com.bx.erp.util.DatetimeUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class PromotionSyncSITTest1 extends BaseSyncSITTest1 {

	protected final String SyncActionDeleteExURL = "/promotionSync/deleteEx.bx";

	@Override
	protected BaseBO getSyncCacheBO() {
		return promotionSyncCacheBO;
	}

	@Override
	protected String getSyncActionDeleteExURL() {
		return SyncActionDeleteExURL;
	}

	@Override
	protected BaseModel getModel() {
		return new Promotion();
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return promotionSyncCacheDispatcherBO;
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_PromotionSyncInfo;
	}

	@Override
	protected int getDeleteAllSyncCacheCaseID() {
		return BaseBO.CASE_X_DeleteAllPromotionSyncCache;
	}

	@Override
	protected BaseSyncCache getSyncCache() {
		return new PromotionSyncCache();
	}

	@Override
	protected BaseSyncSITTestThread1 getThread(MockMvc mvc, HttpSession session, int iPhase, int iPosNO, int iSyncBlockNO) {
		return new PromotionSyncThread1(mvc, session, iPhase, iPosNO, iSyncBlockNO);
	}

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		doSetup();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	/** 1.pos机1创建1个Promotion，pos机2创建2个Promotion，然后分别做同步更新。
	 * 2.pos3，4，5等待pos1，2更新完后，开启同步器 3.当所有pos机都已经同步完成后，删除DB和同存相关的数据 */
	@Test(timeOut = 60000)
	public void runPromotionSyncProcess() throws Exception {
		Shared.printTestMethodStartInfo();

		runSITTest1();
	}

	protected boolean createObject1() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		// Promotion promotion = new Promotion();

		MvcResult pos1CreatePromotionA = mvc.perform(post("/promotionSync/createEx.bx").contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) getLoginSession(1)) //
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号部分满10减1")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "0")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
				.param(Promotion.field.getFIELD_NAME_scope(), "1")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
				.param(Promotion.field.getFIELD_NAME_posID(), "1")//
				.param(Promotion.field.getFIELD_NAME_commodityIDs(), "1,2")//
				.param(Promotion.field.getFIELD_NAME_returnObject(), "1")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = pos1CreatePromotionA.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String err = JsonPath.read(o, "$.ERROR");
		if (err.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			return false;
		}

		JSONObject o1 = JSONObject.fromObject(pos1CreatePromotionA.getResponse().getContentAsString()); //
		int i1 = JsonPath.read(o1, "$.object.ID");

		super.objectID1 = i1;

		return true;
	}

	protected boolean createObject2AndObject3() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));
		// Promotion promotion = new Promotion();

		// 3、pos2 创建Promotion2 和 Promotion3 ，创建后添加到普存和同存
		MvcResult pos2CreatePromotionA = mvc.perform(post("/promotionSync/createEx.bx").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) getLoginSession(2)) //
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号部分满10减1")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "0")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
				.param(Promotion.field.getFIELD_NAME_scope(), "1")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
				.param(Promotion.field.getFIELD_NAME_posID(), "1")//
				.param(Promotion.field.getFIELD_NAME_commodityIDs(), "1,2")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = pos2CreatePromotionA.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String err = JsonPath.read(o, "$.ERROR");
		if (err.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			return false;
		}

		MvcResult pos2CreatePromotionB = mvc.perform(post("/promotionSync/createEx.bx").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) getLoginSession(2)) //
				.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10减1")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "0")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
				.param(Promotion.field.getFIELD_NAME_scope(), "1")//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
				.param(Promotion.field.getFIELD_NAME_posID(), "1")//
				.param(Promotion.field.getFIELD_NAME_commodityIDs(), "1,2")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		String json1 = pos2CreatePromotionB.getResponse().getContentAsString();
		JSONObject o1 = JSONObject.fromObject(json1);
		String err1 = JsonPath.read(o1, "$.ERROR");
		if (err1.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			return false;
		}

		return true;
	}
}
