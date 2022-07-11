package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.trade.PromotionSyncCacheBO;
import com.bx.erp.action.bo.trade.PromotionSyncCacheDispatcherBO;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.dao.trade.PromotionMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.Pos;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.test.checkPoint.PromotionCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

@WebAppConfiguration
public class BasePromotionTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {

		super.setup();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkSN();
		// Doctor_checkStatus();
		// Doctor_checkScope();
		// Doctor_checkType();
		// Doctor_checkDatetime();
		// Doctor_checkCreate();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkSN();
		// Doctor_checkStatus();
		// Doctor_checkScope();
		// Doctor_checkType();
		// Doctor_checkDatetime();
		// Doctor_checkCreate();
	}

	public static class DataInput {
		private static Promotion promotion = null;
		private static PromotionScope promotionInfo = null;
		private static RetailTradePromotingFlow rtpf = null;
		private static RetailTradePromoting rtp = null;
		private static Random ran = new Random();

		public static final Promotion getPromotion() throws CloneNotSupportedException {
			promotion = new Promotion();
			promotion.setName(UUID.randomUUID().toString().substring(1, 7));
			promotion.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
			promotion.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
			promotion.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2)); // 活动开始时间
			promotion.setDatetimeEnd(DatetimeUtil.getDays(new Date(), 3)); // 活动结束时间
			promotion.setExcecutionThreshold(ran.nextInt(50) + 10);
			promotion.setExcecutionAmount(ran.nextInt(10) + 1);
			promotion.setExcecutionDiscount(1 - ran.nextDouble()); // 0<=ran.nextDouble()<1
			promotion.setScope(ran.nextInt(50) % 2);
			promotion.setStaff(1);
			promotion.setPageIndex(1);
			promotion.setPageSize(10);

			return (Promotion) promotion.clone();
		}

		public static final PromotionScope getPromotioninfo() throws CloneNotSupportedException, InterruptedException {
			promotionInfo = new PromotionScope();
			promotionInfo.setPromotionID(1);
			promotionInfo.setCommodityID(1);
			// promotionInfo.setNO(new Random().nextInt(200) + 1);
			// promotionInfo.setCategoryID(1);
			// promotionInfo.setBrandID(1);
			// promotionInfo.setSpecialOffer(12.5f);
			// promotionInfo.setMaxNOForVIPDaily(10);
			// promotionInfo.setMaxNOForVIPMonthly(100);
			// promotionInfo.setMaxNOPerTrade(10);
			// promotionInfo.setMaxNOForAllShops(10);
			// promotionInfo.setMinNOPerTrade(10);
			// promotionInfo.setDiscount(0.9f);
			return (PromotionScope) promotionInfo.clone();
		}

		public static final RetailTradePromotingFlow getRetailTradePromotingFlow() throws CloneNotSupportedException {
			rtpf = new RetailTradePromotingFlow();
			rtpf.setPromotionID(ran.nextInt(4) + 1);
			rtpf.setProcessFlow(UUID.randomUUID().toString().substring(1, 7));
			return (RetailTradePromotingFlow) rtpf.clone();
		}

		public static final RetailTradePromoting getRetailTradePromoting() throws CloneNotSupportedException {
			rtp = new RetailTradePromoting();
			rtp.setTradeID(ran.nextInt(5) + 1);
			return (RetailTradePromoting) rtp.clone();
		}

		public static final Promotion getPromotionAndPromotionScope() throws CloneNotSupportedException {
			promotion = new Promotion();
			promotion.setName(UUID.randomUUID().toString().substring(1, 7));
			promotion.setStatus(0);
			promotion.setType(new Random().nextInt(100) % 2);
			promotion.setDatetimeStart(DatetimeUtil.getDays(new Date(), 2)); // 活动开始时间
			promotion.setDatetimeEnd(DatetimeUtil.getDays(new Date(), 3)); // 活动结束时间
			promotion.setExcecutionThreshold(ran.nextInt(50) + 10);
			promotion.setExcecutionAmount(ran.nextInt(10) + 1);
			promotion.setExcecutionDiscount(1 - ran.nextDouble()); // 0<=ran.nextDouble()<1
			promotion.setScope(ran.nextInt(50) % 2);
			promotion.setStaff(1);
			promotion.setPageIndex(1);
			promotion.setPageSize(10);

			promotionInfo = new PromotionScope();
			promotionInfo.setPromotionID(1);
			promotionInfo.setCommodityID(1);

			List<PromotionScope> listPromotionInfo = new ArrayList<PromotionScope>();
			listPromotionInfo.add(promotionInfo);

			promotion.setListSlave1(listPromotionInfo);
			return (Promotion) promotion.clone();
		}

		public static final Promotion getPromotionInSpecialTime() throws CloneNotSupportedException, InterruptedException {
			Calendar cal = Calendar.getInstance();
			cal.set(2007, 12 - 1, 12);
			Date startDatetime = cal.getTime();
			cal.set(9999, 12 - 1, 12);
			Date endDatetime = cal.getTime();
			promotion = new Promotion();
			promotion.setDatetimeStart(startDatetime);
			promotion.setDatetimeEnd(endDatetime);
			return (Promotion) promotion.clone();
		}

		public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Promotion p) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
			StringBuffer stringBuffer_commodityIDs = new StringBuffer();
			if (p.getListSlave1() != null) {
				for (Object object : p.getListSlave1()) {
					PromotionScope pc = (PromotionScope) object;
					stringBuffer_commodityIDs.append(pc.getCommodityID() + ",");
				}
			}

			MockHttpServletRequestBuilder builder = post(url).contentType(contentType)//
					.param(Promotion.field.getFIELD_NAME_name(), p.getName())//
					.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(p.getStatus()))//
					.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(p.getType()))//
					.param(Promotion.field.getFIELD_NAME_datetimeStart(), simpleDateFormat.format(p.getDatetimeStart()))//
					.param(Promotion.field.getFIELD_NAME_datetimeEnd(), simpleDateFormat.format(p.getDatetimeEnd()))//
					.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(p.getExcecutionThreshold()))//
					.param(Promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(p.getExcecutionAmount()))//
					.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(p.getExcecutionDiscount()))//
					.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(p.getScope()))//
					.param(Promotion.field.getFIELD_NAME_staff(), String.valueOf(p.getStaff()))//
					.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(p.getReturnObject()))//
					.param(Promotion.field.getFIELD_NAME_commodityIDs(), stringBuffer_commodityIDs.toString());//
			return builder;
		}
	}

	private void Doctor_checkSN(PromotionMapper promotionMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionMapper.checkSN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查促销编号的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查促销编号的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkStatus(PromotionMapper promotionMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionMapper.checkStatus(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查促销状态的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查促销状态的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkScope(PromotionMapper promotionMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionMapper.checkScope(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查促销参与的商品的范围的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查促销参与的商品的范围的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkType(PromotionMapper promotionMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionMapper.checkType(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查促销活动类型的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查促销活动类型的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkDatetime(PromotionMapper promotionMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionMapper.checkDatetime(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {

			System.out.println("检查促销活动开始时间与结束时间的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查促销活动开始时间与结束时间的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkCreate(PromotionMapper promotionMapper) {
		Shared.printTestClassEndInfo();

		Promotion p = new Promotion();
		p.setStatus(-1);
		p.setPageIndex(1);
		p.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		String error = p.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
		if (!error.equals("")) {
			System.out.println("对促销进行RN时，传入的的字段数据校验出现异常");
			System.out.println("p=" + (p == null ? null : p));
		}
		//
		Map<String, Object> params = p.getRetrieveNParam(BaseBO.INVALID_CASE_ID, p);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = promotionMapper.retrieveN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (list.size() == 0 || list == null) {
			System.out.println("RN读出的商品信息为空");
		}
		for (BaseModel bm : list) {
			Promotion promotion = (Promotion) bm;
			String error1 = promotion.checkCreate(BaseBO.CASE_SpecialResultVerification);
			if (!error1.equals("")) {
				System.out.println(promotion.getID() + "号促销数据验证出现异常");
				System.out.println("promotion=" + (promotion == null ? null : promotion));
			}

		}
	}

	public static Promotion createPromotionViaMapper(Promotion p) {
		String err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramsCreate = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion pCreate = (Promotion) promotionMapper.create(paramsCreate);
		//
		if (pCreate != null) { // 创建成功
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			err = pCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			p.setIgnoreIDInComparision(true);
			if (p.compareTo(pCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			//
			System.out.println("创建对象成功: " + pCreate);
		} else { // 创建失败
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
					paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			System.out.println("创建对象失败: " + paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return pCreate;
	}

	public static void deletePromotionViaMapper(Promotion pCreate) {
		Map<String, Object> paramsDelete = pCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionMapper.delete(paramsDelete);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramsR1 = pCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, pCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion pR1 = (Promotion) promotionMapper.retrieve1(paramsR1);
		//
		Assert.assertTrue(pR1 != null && EnumErrorCode.values()[Integer.parseInt(paramsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证相应的从表的数据有没有删除成功(需求不能删除从表信息，理论为删除失败)
		if (pCreate.getScope() == EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()) {
			PromotionScope ps = new PromotionScope();
			ps.setPromotionID(pCreate.getID());
			Map<String, Object> psParamsRN = ps.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ps);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> list = promotionScopeMapper.retrieveN(psParamsRN);
			// 有些测试创建从表失败
			Assert.assertTrue(list.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(psParamsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					psParamsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
		//
		System.out.println("删除促销成功");
	}

	public static List<BaseModel> retrieveNPromotionViaMapper(Promotion p) {
		Map<String, Object> paramsList = p.getRetrieveNParam(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> promotionList = promotionMapper.retrieveN(paramsList);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsList.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsList.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err;
		for (BaseModel bm : promotionList) {
			err = ((Promotion) bm).checkCreate(BaseBO.CASE_SpecialResultVerification);
			Assert.assertEquals(err, "");
			// 添加从表
			Promotion promotion = (Promotion) bm;
			if (promotion.getScope() == EnumBoolean.EB_Yes.getIndex()) {
				promotion.setListSlave1(retrieveNPromotionScopeViaMapper(promotion));
			}
		}
		//
		System.out.println("【查询促销】测试成功:" + promotionList);
		//
		return promotionList;
	}

	public static List<BaseModel> retrieveNPromotionScopeViaMapper(Promotion p) {
		PromotionScope promotionScope = new PromotionScope();
		promotionScope.setPromotionID(p.getID());
		promotionScope.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		promotionScope.setPageIndex(BaseAction.PAGE_StartIndex);
		Map<String, Object> param = promotionScope.getRetrieveNParam(BaseBO.INVALID_CASE_ID, promotionScope);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = promotionScopeMapper.retrieveN(param);
		Assert.assertTrue(list.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		return list;
	}

	public static Promotion retrieve1PromotionViaMapper(Promotion pCreate) {
		Map<String, Object> paramsR1 = pCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, pCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion pR1 = (Promotion) promotionMapper.retrieve1(paramsR1);
		//
		if (pR1 != null) {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			String err = pR1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			System.out.println("【查询促销】测试成功:" + pR1);
		} else {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, //
					paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("查询查询失败: " + paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		return pR1;
	}

	public static Promotion updatePromotionViaMapper(Promotion pCreate, Promotion pUpdate) {
		String err = pUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramUpdate = pUpdate.getUpdateParam(BaseBO.INVALID_CASE_ID, pUpdate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion pU = (Promotion) promotionMapper.update(paramUpdate);
		//
		if (pU != null) {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					paramUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			err = pU.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			pCreate.setIgnoreIDInComparision(true);
			if (pCreate.compareTo(pU) == 0) {
				Assert.assertTrue(false, "更新对象失败");
			}
			//
			System.out.println("【更新促销】测试成功:" + pU);
		} else {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
					paramUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("更新促销失败:" + paramUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return pU;
	}

	public static void createRetailTradePromotingFlowViaMapper(RetailTradePromotingFlow rtpf) {
		Map<String, Object> params2 = rtpf.getCreateParam(BaseBO.INVALID_CASE_ID, rtpf);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromotingFlow retailTradePromotingFlowCreate = (RetailTradePromotingFlow) retailTradePromotingFlowMapper.create(params2); // ...
		rtpf.setIgnoreIDInComparision(true);
		if (rtpf.compareTo(retailTradePromotingFlowCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradePromotingFlowCreate != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println(retailTradePromotingFlowCreate == null ? "retailTradePromotingFlowCreate == null" : retailTradePromotingFlowCreate);
	}

	public static RetailTradePromoting createRetailTradePromotingViaMapper() throws CloneNotSupportedException {
		RetailTradePromoting rtp = BasePromotionTest.DataInput.getRetailTradePromoting();
		Map<String, Object> params = rtp.getCreateParam(BaseBO.INVALID_CASE_ID, rtp);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingMapper.create(params); // ...
		rtp.setIgnoreIDInComparision(true);
		if (rtp.compareTo(retailTradePromoting) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradePromoting != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println(retailTradePromoting == null ? "providerCreate == null" : retailTradePromoting);
		return retailTradePromoting;
	}

	public static List<Promotion> getPromotionListIfAllSync(Map<String, BaseBO> BOsMap, List<BaseModel> bmList) {
		PosBO posBO = (PosBO) BOsMap.get(PosBO.class.getSimpleName());
		List<Pos> posList = Shared.getPosesFromDB(posBO, Shared.DBName_Test);
		List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(Shared.DBName_Test, EnumSyncCacheType.ESCT_PromotionSyncInfo).readN(false, false);
		List<Promotion> promotionList = new ArrayList<>();

		BaseSyncCache baseSyncCache = new BaseSyncCache();
		for (BaseModel bm : bmList) {
			Promotion promotion = (Promotion) bm;

			// 获取该小票格式的同步块信息
			for (BaseModel bm2 : bmSyncCacheList) {
				baseSyncCache = (BaseSyncCache) bm2;
				if (baseSyncCache.getSyncData_ID() == promotion.getID()) {
					if (baseSyncCache.getListSlave1() != null && baseSyncCache.getListSlave1().size() == (posList.size() - 1)) {
						promotion.setPosID(1); // 设int1为1，表示该商品同步块已经完全同步
					}
				}
			}
			promotionList.add(promotion);
		}
		return promotionList;
	}

	public static PromotionScope createPromotionScopeViaMapper(PromotionScope ps, EnumErrorCode eec) {
		if (eec == EnumErrorCode.EC_NoError) {
			String err = ps.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		Map<String, Object> paramsCreate = ps.getCreateParam(BaseBO.INVALID_CASE_ID, ps);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PromotionScope psCreate = (PromotionScope) promotionScopeMapper.create(paramsCreate);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == eec, //
				paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		if (eec == EnumErrorCode.EC_NoError) {
			Assert.assertTrue(psCreate != null, "返回的对象为null");
			String err = psCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			psCreate.setIgnoreIDInComparision(true);
			ps.setCommodityName(psCreate.getCommodityName());
			if (psCreate.compareTo(ps) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
		}
		//
		return psCreate;
	}

	public static Promotion createViaAction(Promotion p, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, String dbName) throws Exception {
		MvcResult mr = mvc.perform( //
				DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, p) //
						.session((MockHttpSession) session) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);
		// 验证缓存中的结果
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		PosBO posBO = (PosBO) mapBO.get(PosBO.class.getSimpleName());
		PromotionSyncCacheBO promotionSyncCacheBO = (PromotionSyncCacheBO) mapBO.get(PromotionSyncCacheBO.class.getSimpleName());
		PromotionSyncCacheDispatcherBO promotionSyncCacheDispatcherBO = (PromotionSyncCacheDispatcherBO) mapBO.get(PromotionSyncCacheDispatcherBO.class.getSimpleName());
		verifySyncCreateResult(mr, p, posBO, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, EnumCacheType.ECT_Promotion, EnumSyncCacheType.ESCT_PromotionSyncInfo, posID, dbName);
		//
		PromotionCP.verifyCreate(mr, p, dbName, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
		return (Promotion) Shared.parse1Object(mr, p, BaseAction.KEY_Object);
	}

}
