package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.Coupon.EnumCouponCardType;
import com.bx.erp.model.Coupon.EnumCouponStatus;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

import net.sf.json.JSONObject;

public class BaseCouponTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void tearDown() {
	}

	public static class DataInput {
		private static Coupon couponInput = null;

		public static final Coupon getCoupon() throws CloneNotSupportedException {
			couponInput = new Coupon();
			couponInput.setStatus(EnumCouponStatus.ECS_Normal.getIndex());
			couponInput.setType(EnumCouponCardType.ECCT_CASH.getIndex());
			couponInput.setBonus(0);
			couponInput.setLeastAmount(20.000000d);
			couponInput.setReduceAmount(1.000000d);
			couponInput.setDiscount(1.000000d);
			couponInput.setTitle("现金券满20减1");
			couponInput.setColor("xxxxxxxxxx");
			couponInput.setDescription("1、xxxxxxxxxxxxxxxxxxxx");
			couponInput.setPersonalLimit(1);
			couponInput.setWeekDayAvailable(0);
			couponInput.setBeginTime("00:00:00");
			couponInput.setEndTime("23:59:59");
			couponInput.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
			couponInput.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
			couponInput.setQuantity(1000);
			couponInput.setRemainingQuantity(1000);
			couponInput.setScope(0);

			return (Coupon) couponInput.clone();
		}

		public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Coupon coupon, HttpSession session) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType).session((MockHttpSession) session) //
					.param(Coupon.field.getFIELD_NAME_status(), String.valueOf(coupon.getStatus())) //
					.param(Coupon.field.getFIELD_NAME_type(), String.valueOf(coupon.getType())) //
					.param(Coupon.field.getFIELD_NAME_bonus(), String.valueOf(coupon.getBonus())) //
					.param(Coupon.field.getFIELD_NAME_leastAmount(), String.valueOf(coupon.getLeastAmount())) //
					.param(Coupon.field.getFIELD_NAME_reduceAmount(), String.valueOf(coupon.getReduceAmount())) //
					.param(Coupon.field.getFIELD_NAME_discount(), String.valueOf(coupon.getDiscount())) //
					.param(Coupon.field.getFIELD_NAME_title(), coupon.getTitle()) //
					.param(Coupon.field.getFIELD_NAME_color(), coupon.getColor()) //
					.param(Coupon.field.getFIELD_NAME_description(), coupon.getDescription()) //
					.param(Coupon.field.getFIELD_NAME_personalLimit(), String.valueOf(coupon.getPersonalLimit())) //
					.param(Coupon.field.getFIELD_NAME_weekDayAvailable(), String.valueOf(coupon.getWeekDayAvailable())) //
					.param(Coupon.field.getFIELD_NAME_beginTime(), coupon.getBeginTime()) //
					.param(Coupon.field.getFIELD_NAME_endTime(), coupon.getEndTime()) //
					.param(Coupon.field.getFIELD_NAME_beginDateTime(), String.valueOf(coupon.getBeginDateTime())) //
					.param(Coupon.field.getFIELD_NAME_endDateTime(), String.valueOf(coupon.getEndDateTime())) //
					.param(Coupon.field.getFIELD_NAME_quantity(), String.valueOf(coupon.getQuantity())) //
					.param(Coupon.field.getFIELD_NAME_remainingQuantity(), String.valueOf(coupon.getRemainingQuantity())) //
					.param(Coupon.field.getFIELD_NAME_scope(), String.valueOf(coupon.getScope())) //
					.param(Coupon.field.getFIELD_NAME_commodityIDs(), coupon.getCommodityIDs()) //
			;
			return builder;

		}
	}

	public static Coupon createViaMapper(Coupon coupon) {
		String error1 = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		Map<String, Object> params = coupon.getCreateParam(BaseBO.INVALID_CASE_ID, coupon);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Coupon couponCreate = (Coupon) couponMapper.create(params);
		Assert.assertTrue(couponCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败。param=" + params + "\n\r coupon=" + coupon);
		//
		coupon.setIgnoreIDInComparision(true);
		if (coupon.compareTo(couponCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		error1 = couponCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		return couponCreate;
	}

	public static void deleteViaMapper(Coupon coupon) {
		Map<String, Object> paramsDelete = coupon.getDeleteParam(BaseBO.INVALID_CASE_ID, coupon);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		couponMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		Coupon couponRetrieve1 = (Coupon) retrieve1ViaMapper(coupon);
		Assert.assertTrue(couponRetrieve1.getStatus() == EnumCouponStatus.ECS_Expired.getIndex());
	}

	public static BaseModel retrieve1ViaMapper(Coupon coupon) {
		Map<String, Object> params = coupon.getRetrieve1Param(BaseBO.INVALID_CASE_ID, coupon);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel couponRetrieve1 = couponMapper.retrieve1(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		String err = couponRetrieve1.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		return couponRetrieve1;
	}

	public static List<BaseModel> retrieveNViaMapper(Coupon coupon) {
		String err = coupon.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);

		Map<String, Object> params = coupon.getRetrieveNParam(BaseBO.INVALID_CASE_ID, coupon);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> couponRetrieveN = couponMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		return couponRetrieveN;
	}

	public static List<BaseModel> retrieveNViaAction(Coupon coupon, HttpSession session, MockMvc mvc) throws Exception {
		MvcResult mr = mvc.perform(//
				get("/coupon/retrieveNEx.bx?" + Coupon.field.getFIELD_NAME_pageIndex() + "=" + coupon.getPageIndex()  //
				+ "&" + Coupon.field.getFIELD_NAME_pageSize() + "=" + coupon.getPageSize() //
				+ "&" + Coupon.field.getFIELD_NAME_posID() + "=" + coupon.getPosID() //
				+ "&" + Coupon.field.getFIELD_NAME_bonus() + "=" + coupon.getBonus() //
				+ "&" + Coupon.field.getFIELD_NAME_type() + "=" + coupon.getType()) //
				.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr);

		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		List<BaseModel> couponList = coupon.parseN(o.getString(BaseAction.KEY_ObjectList));
		for(BaseModel bModel : couponList) {
			Coupon couponBm = (Coupon) bModel;
			// 如果是指定商品范围，判断是否返回了零售价和条形码(判断条形码)
			if(coupon.getScope() == CouponScope.EnumCouponScope.ECS_SpecifiedCommodities.getIndex()) {
				List<CouponScope> couponScopeList = (List<CouponScope>) couponBm.getListSlave1();
				for(CouponScope couponScope : couponScopeList) {
					Assert.assertTrue(couponScope.getBarcodes() != null, "没有返回条形码");
				}
			}
		}
		return coupon.parseN(o.getString(BaseAction.KEY_ObjectList));
	}

	public static Coupon createViaAction(Coupon coupon, HttpSession session, MockMvc mvc, EnumErrorCode eec) throws Exception {
		MvcResult mr = mvc.perform(//
				DataInput.getBuilder("/coupon/createEx.bx", MediaType.APPLICATION_JSON, coupon, session))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		if (eec == EnumErrorCode.EC_NoError) {
			Shared.checkJSONErrorCode(mr);
			//
			String json = mr.getResponse().getContentAsString();
			JSONObject jsonObject = JSONObject.fromObject(json);
			Coupon couponCreated = new Coupon();
			couponCreated = (Coupon) couponCreated.parse1(jsonObject.getString(BaseAction.KEY_Object));
			Assert.assertTrue(couponCreated != null, "解析异常");
			//
			if(couponCreated.getScope() == CouponScope.EnumCouponScope.ECS_SpecifiedCommodities.getIndex()) {
				List<CouponScope> couponScopeList = (List<CouponScope>) couponCreated.getListSlave1();
				for(CouponScope couponScope : couponScopeList) {
					Assert.assertTrue(couponScope.getBarcodes() != null, "没有返回条形码");
				}
			}
			return couponCreated;
		} else if (eec == EnumErrorCode.EC_Hack) {
			String o6 = mr.getResponse().getContentAsString();
			assertTrue(o6.length() == 0, "CASE6测试失败！返回的结果不是期望的");
			return null;
		} else {
			Shared.checkJSONErrorCode(mr, eec);
			return null;
		}
	}

	public static void deleteViaAction(Coupon coupon, HttpSession session, MockMvc mvc, EnumErrorCode eec) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/coupon/deleteEx.bx")//
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Coupon.field.getFIELD_NAME_ID(), String.valueOf(coupon.getID())))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr, eec);
		//
		if (eec == EnumErrorCode.EC_NoError) {
			if (coupon.getID() < Shared.BIG_ID) {
				Coupon couponRetrieve1 = (Coupon) retrieve1ViaMapper(coupon);
				Assert.assertTrue(couponRetrieve1.getStatus() == Coupon.EnumCouponStatus.ECS_Expired.getIndex(), "删除优惠券失败！");
			}
		}
	}

	public static Coupon retrieve1ViaAction(Coupon coupon, HttpSession session, MockMvc mvc, EnumErrorCode eec) throws Exception {
		MvcResult mr = mvc.perform(//
				get("/coupon/retrieve1Ex.bx")//
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Coupon.field.getFIELD_NAME_ID(), String.valueOf(coupon.getID())))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr, eec);
		//
		if (eec == EnumErrorCode.EC_NoError) {
			String json = mr.getResponse().getContentAsString();
			JSONObject jsonObject = JSONObject.fromObject(json);
			Coupon couponR1 = new Coupon();
			couponR1 = (Coupon) couponR1.parse1(jsonObject.getString(BaseAction.KEY_Object));
			Assert.assertTrue(couponR1 != null, "解析异常");
			//
			if(couponR1.getScope() == CouponScope.EnumCouponScope.ECS_SpecifiedCommodities.getIndex()) {
				List<CouponScope> couponScopeList = (List<CouponScope>) couponR1.getListSlave1();
				for(CouponScope couponScope : couponScopeList) {
					Assert.assertTrue(couponScope.getBarcodes() != null, "没有返回条形码");
				}
			}
			return couponR1;
		}
		return null;
	}
	
	public static void setCurrentWeekType(Coupon coupon) {
		int[] weekType = { 64, 32, 16, 8, 4, 2, 1 };

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		coupon.setWeekDayAvailable(weekType[cal.get(Calendar.DAY_OF_WEEK) - 1]);
	}
}
