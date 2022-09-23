package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CommodityBO;
import com.bx.erp.action.bo.commodity.CommodityHistoryBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.dao.RetailTradeCommodityMapper;
import com.bx.erp.dao.RetailTradeCommoditySourceMapper;
import com.bx.erp.dao.RetailTradeMapper;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.checkPoint.PurchasingOrderCP;
import com.bx.erp.test.checkPoint.RetailTradeCP;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

public class BaseRetailTradeTest extends BaseMapperTest {

	private static final String KEY_COMMIDS = "commIDs";
	private static final String KEY_BARCODEIDS = "barcodeIDs";
	private static final String KEY_NOS = "NOs";
	private static final String KEY_COMMPURCHASINGUNIT = "commPurchasingUnit";
	private static final String KEY_PRICESUGGESTIONS = "priceSuggestions";
	private static final String KEY_PROVIDERID = "providerID";

	private static final String commIDs = "commIDs";
	private static final String commNOs = "commNOs";
	private static final String commPrices = "commPrices";
	private static final String amounts = "amounts";
	private static final String barcodeIDs = "barcodeIDs";
	public static final String KEY_shopID = "shopID";
	public static final int defaultShopID = 2;

	@BeforeClass
	public void setup() {

		super.setup();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkCreate();
		// Doctor_checkAmount();
		// Doctor_checkPaymentType();
		// Doctor_checkVipID();
		// Doctor_checkStatus();
		// Doctor_checkSmallSheetID();
		// Doctor_checkBarcodeID();
		// Doctor_checkCommodity();
		// Doctor_checkNO();
		// Doctor_checkRetailTradeCommodity();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkCreate();
		// Doctor_checkAmount();
		// Doctor_checkPaymentType();
		// Doctor_checkVipID();
		// Doctor_checkStatus();
		// Doctor_checkSmallSheetID();
		// Doctor_checkBarcodeID();
		// Doctor_checkCommodity();
		// Doctor_checkNO();
		// Doctor_checkRetailTradeCommodity();
	}

	public static class DataInput {
		private static RetailTrade retailTradeInput = null;
		private static RetailTradeCommodity retailTradeCommodityInput = null;
		private static RetailTradeCoupon retailTradeCouponInput = null;

		public static RetailTradeCoupon getRetailTradeCoupon(int couponCodeID, int retailTradeID) throws CloneNotSupportedException {
			retailTradeCouponInput = new RetailTradeCoupon();
			retailTradeCouponInput.setCouponCodeID(couponCodeID);
			retailTradeCouponInput.setRetailTradeID(retailTradeID);
			retailTradeCouponInput.setSyncDatetime(new Date());

			return (RetailTradeCoupon) retailTradeCouponInput.clone();
		}

		public static RetailTrade getRetailTrade() throws InterruptedException {
			Random ran = new Random();
			retailTradeInput = new RetailTrade();
			retailTradeInput.setLocalSN(Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(6)));
			Thread.sleep(1000);
			retailTradeInput.setVipID(1);
			retailTradeInput.setPos_ID(1);
			retailTradeInput.setSn(Shared.generateRetailTradeSN(retailTradeInput.getPos_ID()));
			retailTradeInput.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
			retailTradeInput.setSaleDatetime(new Date());
			retailTradeInput.setLogo("11" + ran.nextInt(1000));
			retailTradeInput.setStaffID(2);
			retailTradeInput.setPaymentType(1);
			retailTradeInput.setPaymentAccount("12");
			retailTradeInput.setRemark("11111");
			retailTradeInput.setSourceID(-1);
			retailTradeInput.setAmount(2222.200000d);
			retailTradeInput.setAmountCash(2222.200000d);
			retailTradeInput.setSmallSheetID(1);
			retailTradeInput.setSyncDatetime(new Date());
			retailTradeInput.setReturnObject(EnumBoolean.EB_Yes.getIndex());
			retailTradeInput.setWxOrderSN("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setAliPayOrderSN("zfb" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setWxRefundNO(String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setDatetimeStart(new Date());
			retailTradeInput.setDatetimeEnd(new Date());
			retailTradeInput.setOperatorStaffID(STAFF_ID3);
			retailTradeInput.setShopID(2);
			Thread.sleep(1000);

			return (RetailTrade) retailTradeInput.clone();
		}

		public static RetailTradeCommodity getRetailTradeCommodity() throws InterruptedException {
			retailTradeCommodityInput = new RetailTradeCommodity();
			retailTradeCommodityInput.setCommodityID(1);
			retailTradeCommodityInput.setNO(10);
			retailTradeCommodityInput.setPriceOriginal(20);
			retailTradeCommodityInput.setPriceReturn(20);
			retailTradeCommodityInput.setBarcodeID(1);
			retailTradeCommodityInput.setNOCanReturn(10);
			retailTradeCommodityInput.setPriceVIPOriginal(10);
			retailTradeCommodityInput.setPriceSpecialOffer(10);
			retailTradeCommodityInput.setOperatorStaffID(STAFF_ID3);

			return (RetailTradeCommodity) retailTradeCommodityInput.clone();
		}
	}

	public static RetailTrade createRetailtradeViaMapper(RetailTrade rt) {
		Map<String, Object> params = rt.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt);
		//
		String err = rt.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(params);
		//
		Assert.assertTrue(createEx.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		RetailTrade retailTradeCreate = RetailTrade.fetchRetailTradeFromResultSet(createEx);
		Assert.assertTrue(retailTradeCreate != null, "对象创建失败");
		rt.setIgnoreIDInComparision(true);
		rt.setIgnoreSlaveListInComparision(true);
		if (rt.compareTo(retailTradeCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(0);
		rtc.setTradeID(retailTradeCreate.getID());
		Map<String, Object> paramsRetailTradeCommodity = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);
		//
		String err1 = rtc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err1), err1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(paramsRetailTradeCommodity); // ...
		Assert.assertTrue(retailTradeCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsRetailTradeCommodity.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsRetailTradeCommodity.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		rtc.setIgnoreIDInComparision(true);
		if (rtc.compareTo(retailTradeCommodityCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		return retailTradeCreate;
	}

	public static RetailTradeCoupon createRetailTradeCouponViaMapper(RetailTradeCoupon retailTradeCoupon, EnumErrorCode eec) {
		if (EnumErrorCode.EC_NoError == eec) {
			String err = retailTradeCoupon.checkCreate(BaseBO.INVALID_CASE_ID);
			assertTrue("".equals(err), err);
		}
		//
		Map<String, Object> param = retailTradeCoupon.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradeCoupon);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCoupon retailTradeCouponCreate = (RetailTradeCoupon) retailTradeCouponMapper.create(param);
		//
		if (EnumErrorCode.EC_NoError == eec) {
			assertTrue(retailTradeCouponCreate != null && EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
					param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			retailTradeCoupon.setIgnoreIDInComparision(true);
			if (retailTradeCoupon.compareTo(retailTradeCouponCreate) != 0) {
				assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			//
			String err = retailTradeCoupon.checkCreate(BaseBO.INVALID_CASE_ID);
			assertTrue("".equals(err), err);
		} else {
			assertTrue(retailTradeCouponCreate == null && EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == eec, param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}

		return retailTradeCouponCreate;
	}

	private void Doctor_checkAmount(RetailTradeMapper retailTradeMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.checkAmount(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的零售单金额的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的零售单金额的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkPaymentType(RetailTradeMapper retailTradeMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.checkPaymentType(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的零售单支付方式的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的零售单支付方式的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkVipID(RetailTradeMapper retailTradeMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.checkVipID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的零售单会员ID的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的零售单会员ID的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkStatus(RetailTradeMapper retailTradeMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.checkStatus(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的零售单状态的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的零售单状态的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkSmallSheetID(RetailTradeMapper retailTradeMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.checkSmallSheetID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的零售单小票格式ID的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的零售单小票格式ID的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkBarcodeID(RetailTradeCommodityMapper retailTradeCommodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCommodityMapper.checkBarcodeID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的零售单商品条形码的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的零售单商品条形码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkCommodity(RetailTradeCommodityMapper retailTradeCommodityMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCommodityMapper.checkCommodity(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的零售单商品的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的零售单商品的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkNO(RetailTradeCommoditySourceMapper retailTradeCommoditySourceMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCommoditySourceMapper.checkNO(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的零售单商品数量的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的零售单商品数量的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private void Doctor_checkCreate(RetailTradeMapper retailTradeMapper, RetailTradeCommodityMapper retailTradeCommodityMapper) {
		Shared.printTestClassEndInfo();

		RetailTrade retailTrade = new RetailTrade();
		retailTrade.setVipID(RetailTrade.Min_ID);
		retailTrade.setQueryKeyword("");
		retailTrade.setLocalSN(RetailTrade.Min_LocalSN);
		retailTrade.setPos_ID(RetailTrade.Min_ID);
		retailTrade.setPaymentType(RetailTrade.Min_paymentType - 1);
		retailTrade.setStaffID(RetailTrade.Min_ID);
		String err = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		if (!err.equals("")) {
			System.out.println("对采购订单进行RN时，传入的的字段数据校验出现异常");
			System.out.println("pOrder=" + (retailTrade == null ? null : retailTrade));
		}
		Map<String, Object> params = retailTrade.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTrade);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retailTradeList = retailTradeMapper.retrieveN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (retailTradeList.size() == 0 || retailTradeList == null) {
			System.out.println("RN读出的零售单信息为空");
		}
		for (BaseModel bm : retailTradeList) {
			RetailTrade rTrade = (RetailTrade) bm;
			String err1 = rTrade.checkCreate(BaseBO.INVALID_CASE_ID);
			if (!err1.equals("")) {
				System.out.println(rTrade.getID() + "号零售单数据验证出现异常");
				System.out.println("rTrade=" + (rTrade == null ? null : rTrade));
			}
			RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
			retailTradeCommodity.setTradeID(rTrade.getID());
			retailTradeCommodity.setPageIndex(1);
			retailTradeCommodity.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			Map<String, Object> retailTradeCommodityParams = retailTradeCommodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTradeCommodity);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> retailTradeCommodityList = retailTradeCommodityMapper.retrieveN(retailTradeCommodityParams);
			for (BaseModel retailTradeComm : retailTradeCommodityList) {
				RetailTradeCommodity rtc = (RetailTradeCommodity) retailTradeComm;
				String err2 = rtc.checkCreate(BaseBO.INVALID_CASE_ID);
				if (!err2.equals("")) {
					System.out.println(rtc.getID() + "号零售商品数据验证出现");
					System.out.println("pComm=" + (rtc == null ? null : rtc));
				}
			}
		}
	}

	private void Doctor_checkRetailTradeCommodity(RetailTradeMapper retailTradeMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeMapper.checkRetailTradeCommodity(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的零售单商品的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的零售单商品的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public static List<WarehousingCommodity> getWarehousingCommodityList(List<Commodity> commList, WarehousingBO warehousingBO, String dbName) {
		WarehousingCommodity wsc = new WarehousingCommodity();
		List<WarehousingCommodity> wscList = new ArrayList<WarehousingCommodity>();
		for (Commodity commodity : commList) {
			if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
				wsc = getCommodityWarehousing(commodity, warehousingBO, dbName);

				wscList.add(wsc);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				Commodity normalCommodity = new Commodity();
				normalCommodity.setID(commodity.getRefCommodityID());
				normalCommodity.setCurrentWarehousingID(commodity.getCurrentWarehousingID());
				//
				wsc = getCommodityWarehousing(normalCommodity, warehousingBO, dbName);
				//
				wscList.add(wsc);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
				for (Object o : commodity.getListSlave2()) {
					Commodity normalCommodity = (Commodity) o;
					//
					wsc = getCommodityWarehousing(normalCommodity, warehousingBO, dbName);
					//
					wscList.add(wsc);
				}
			} else { //
				wscList.add(null);
			}
		}
		return wscList;
	}

	@SuppressWarnings("unchecked")
	private static WarehousingCommodity getCommodityWarehousing(Commodity commodity, WarehousingBO warehousingBO, String dbName) {
		if (commodity.getCurrentWarehousingID() == 0) {
			Warehousing warehousing = new Warehousing();
			warehousing.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			// 查询所有的入库单
			ErrorInfo ecOut = new ErrorInfo();
			DataSourceContextHolder.setDbName(dbName);
			List<Warehousing> warehousingList = (List<Warehousing>) warehousingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);
			if (warehousingList == null) {
				return null;
			}
			Warehousing tmpWarehousing = new Warehousing();
			tmpWarehousing.setIsASC(EnumBoolean.EB_NO.getIndex());
			Collections.sort(warehousingList, tmpWarehousing);

			for (Warehousing ws : warehousingList) {
				// 在缓存中获取入库单主从表信息
				Warehousing warehousingOfCache = (Warehousing) CacheManager.getCache(dbName, EnumCacheType.ECT_Warehousing).read1(ws.getID(), BaseBO.SYSTEM, ecOut, dbName);

				for (Object o : warehousingOfCache.getListSlave1()) {
					WarehousingCommodity wsc = (WarehousingCommodity) o;
					if (wsc.getCommodityID() == commodity.getID()) {
						return wsc;
					}
				}
			}
		} else {
			Warehousing warehousing = new Warehousing();
			warehousing.setID(commodity.getCurrentWarehousingID());
			DataSourceContextHolder.setDbName(dbName);
			List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);

			for (Object o : bmList.get(1)) {
				WarehousingCommodity warehousingCommodity = (WarehousingCommodity) o;
				if (warehousingCommodity.getCommodityID() == commodity.getID()) {
					return warehousingCommodity;
				}
			}
		}
		return null;
	}

	public static RetailTrade createRetailTrade(RetailTrade retailTrade) {
		String errorMsg = retailTrade.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(errorMsg), "checkCreate不通过，" + errorMsg);
		Map<String, Object> params = retailTrade.getCreateParamEx(BaseBO.INVALID_CASE_ID, retailTrade);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(params); // ...
		//
		Assert.assertTrue(createEx != null && createEx.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败," + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		RetailTrade localRetailTrade = RetailTrade.fetchRetailTradeFromResultSet(createEx);
		retailTrade.setIgnoreIDInComparision(true);
		if (retailTrade.getSourceID() > BaseAction.INVALID_ID) {
			String snBeforeCreate = retailTrade.getSn();
			retailTrade.setSn(localRetailTrade.getSn());
			if (retailTrade.compareTo(localRetailTrade) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			retailTrade.setSn(snBeforeCreate);
			System.out.println(localRetailTrade == null ? "providerCreate == null" : localRetailTrade);
			return localRetailTrade;
		}
		if (retailTrade.compareTo(localRetailTrade) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		System.out.println(localRetailTrade == null ? "providerCreate == null" : localRetailTrade);
		return localRetailTrade;
	}

	public static RetailTradeCommodity createRetailTradeCommodity(RetailTradeCommodity retailTradeComm, EnumErrorCode errCode) {
		String err = retailTradeComm.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> params = retailTradeComm.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradeComm);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(params); // ...
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == errCode, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (retailTradeCommodityCreate != null) {
			retailTradeComm.setIgnoreIDInComparision(true);
			if (retailTradeComm.compareTo(retailTradeCommodityCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
		}

		return retailTradeCommodityCreate;
	}

	public static RetailTrade createRetailTradeViaMapper(int[] retailTradeCommodityNOArr, double[] retailTradeCommodityPriceArr, double retailTrade1Amount, int[] commIdArrForCreateRetailTrade, int[] barcodeIdArrForCreateRetailTrade,
			int sourceID) throws CloneNotSupportedException, InterruptedException {
		RetailTrade retailTradeGet = DataInput.getRetailTrade();
		retailTradeGet.setAmount(retailTrade1Amount);
		retailTradeGet.setAmountCash(retailTrade1Amount);
		retailTradeGet.setStaffID(Shared.BossID);
		retailTradeGet.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		if (sourceID > 0) { // 是否创建退货单
			retailTradeGet.setSourceID(sourceID);
		}
		RetailTrade retailTradeCreated = createRetailTrade(retailTradeGet);

		for (int i = 0; i < commIdArrForCreateRetailTrade.length; i++) {
			RetailTradeCommodity retailTradeCommodityGet = new RetailTradeCommodity();
			retailTradeCommodityGet.setCommodityID(commIdArrForCreateRetailTrade[i]);
			retailTradeCommodityGet.setNO(retailTradeCommodityNOArr[i]);
			retailTradeCommodityGet.setPriceOriginal(20);
			retailTradeCommodityGet.setPriceReturn(retailTradeCommodityPriceArr[i]);
			retailTradeCommodityGet.setBarcodeID(barcodeIdArrForCreateRetailTrade[i]);
			retailTradeCommodityGet.setNOCanReturn(retailTradeCommodityNOArr[i]);
			retailTradeCommodityGet.setPriceVIPOriginal(10);
			retailTradeCommodityGet.setPriceSpecialOffer(10);
			//
			retailTradeCommodityGet.setTradeID(retailTradeCreated.getID());
			retailTradeCommodityGet.setOperatorStaffID(Shared.BossID);
			RetailTradeCommodity retailTradeCommodityCreated = createRetailTradeCommodity(retailTradeCommodityGet, EnumErrorCode.EC_NoError);
			System.out.println(retailTradeCommodityCreated);
		}

		return retailTradeCreated;
	}

	public static void createAndApproveWarehousingViaMapper(int[] warehousingCommNOArr, double[] warehousingCommPriceArr, int[] commIdArr, int[] barcodeIdArr, PurchasingOrder purchasingOrderApproved)
			throws CloneNotSupportedException, InterruptedException {
		// 入库商品
		Warehousing warehousingGet = BaseWarehousingTest.DataInput.getWarehousing();
		warehousingGet.setWarehouseID(1);
		warehousingGet.setStaffID(Shared.BossID);
		warehousingGet.setProviderID(1);
		warehousingGet.setPurchasingOrderID(purchasingOrderApproved.getID());
		Warehousing warehousingCreated = BaseWarehousingTest.createViaMapper(warehousingGet);
		for (int i = 0; i < commIdArr.length; i++) {
			WarehousingCommodity warehousingCommodityGet = new WarehousingCommodity();
			warehousingCommodityGet.setCommodityID(commIdArr[i]);
			warehousingCommodityGet.setNO(warehousingCommNOArr[i]);
			warehousingCommodityGet.setBarcodeID(barcodeIdArr[i]);
			warehousingCommodityGet.setPrice(warehousingCommPriceArr[i]);
			warehousingCommodityGet.setAmount(warehousingCommNOArr[i] * warehousingCommPriceArr[i]);
			warehousingCommodityGet.setShelfLife(22);
			warehousingCommodityGet.setWarehousingID(warehousingCreated.getID());
			// 检查字段合法性
			String errorMassage = warehousingCommodityGet.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(errorMassage, "");
			//
			WarehousingCommodity warehousingCommCreated = BaseWarehousingTest.createWarehousingCommodityViaMapper(warehousingCommodityGet);
			System.out.println(warehousingCommCreated);
		}
		//
		warehousingCreated.setApproverID(Shared.BossID);
		Warehousing warehousingApproved = BaseWarehousingTest.approveViaMapper(warehousingCreated);
		System.out.println(warehousingApproved);
	}

	public static PurchasingOrder createAndApprovePurchasingOrderViaMapper(final int purchasingOrderCommNO, final double priceSuggestion, int[] commIdArr, int[] barcodeIdArr) throws CloneNotSupportedException, InterruptedException {
		// 创建采购订单
		PurchasingOrder purchasingOrderGet = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrderCreated = BasePurchasingOrderTest.createPurchasingOrderViaMapper(purchasingOrderGet);
		// 创建一个采购订单商品
		for (int i = 0; i < commIdArr.length; i++) {
			PurchasingOrderCommodity purchasingOrderCommodityGet = new PurchasingOrderCommodity();
			purchasingOrderCommodityGet.setCommodityID(commIdArr[i]);
			purchasingOrderCommodityGet.setBarcodeID(barcodeIdArr[i]);
			purchasingOrderCommodityGet.setPurchasingOrderID(purchasingOrderCreated.getID());
			purchasingOrderCommodityGet.setCommodityNO(purchasingOrderCommNO);
			purchasingOrderCommodityGet.setPriceSuggestion(priceSuggestion);
			PurchasingOrderCommodity purchasingOrderCommodityCreated = BasePurchasingOrderTest.createPurchasingOrderCommodity(purchasingOrderCommodityGet);
			System.out.println(purchasingOrderCommodityCreated);
		}
		PurchasingOrder purchasingOrderApproved = BasePurchasingOrderTest.purchasingOrderApproverViaMapper(purchasingOrderCreated);
		return purchasingOrderApproved;
	}
	
	protected static Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
//		logger.info("当前使用的staff=" + staff);

		return staff;
	}
	
	/**
	 * 可以传入商品的参数进行创建零售单操作
	 */
	public static RetailTrade createRetailTradeViaAction(MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, int[] retailTradeCommodityNoArr, double[] retailTradeCommodityPriceArr, double retailTrade1Amount,
			int[] commodityCreatedArr, int[] barcodeCreatedArr, int sourceID) throws CloneNotSupportedException, InterruptedException, Exception, UnsupportedEncodingException {
		// 检查点
		CommodityBO commodityBO = (CommodityBO) mapBO.get(CommodityBO.class.getSimpleName());
		WarehousingBO warehousingBO = (WarehousingBO) mapBO.get(WarehousingBO.class.getSimpleName());

		RetailTrade retailTrade = DataInput.getRetailTrade();
		Staff staffSession = getStaffFromSession(session);
		retailTrade.setStaffID(staffSession.getID());
		retailTrade.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		if (sourceID > 0) { // 判断是否创建退货单
			retailTrade.setSourceID(sourceID);
		}
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		for (int i = 0; i < retailTradeCommodityNoArr.length; i++) {
			RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
			retailTradeCommodity.setCommodityID(commodityCreatedArr[i]);
			retailTradeCommodity.setNO(retailTradeCommodityNoArr[i]);
			retailTradeCommodity.setPriceOriginal(20);
			retailTradeCommodity.setPriceReturn(retailTradeCommodityPriceArr[i]);
			retailTradeCommodity.setBarcodeID(barcodeCreatedArr[i]);
			retailTradeCommodity.setNOCanReturn(10);
			retailTradeCommodity.setPriceVIPOriginal(10);
			retailTradeCommodity.setPriceSpecialOffer(10);
			listRetailTradeCommodity.add(retailTradeCommodity);
		}
		retailTrade.setListSlave1(listRetailTradeCommodity);
		retailTrade.setAmount(retailTrade1Amount);
		retailTrade.setAmountCash(retailTrade1Amount);
		//
		Map<Integer, Commodity> simpleCommodityList = null;
		boolean bIncludeDeletedCommodity = false;
		if (retailTrade.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity = true;
		}
		simpleCommodityList = getSimpleCommodityListFromRetailTrade(retailTrade, Shared.DBName_Test, bIncludeDeletedCommodity, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale = queryWarehousingBeforeSale(simpleCommodityList, Shared.DBName_Test, retailTrade, warehousingBO);

		MvcResult mr4 = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param("retailTrade", Shared.toJSONString(retailTrade)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4);
		// 检查创建后返回的对象是否和传入的对象一致
		JSONObject returnJson = JSONObject.fromObject(mr4.getResponse().getContentAsString());

		RetailTrade retailTradeCreated = new RetailTrade();
		retailTradeCreated = (RetailTrade) retailTradeCreated.parse1(returnJson.getString(BaseAction.KEY_Object));
		Assert.assertFalse(retailTradeCreated == null);

		Map<Integer, List<Warehousing>> mapAfterSale = queryWarehousingAfterSale(mapBeforeSale, Shared.DBName_Test, warehousingBO);
		if (retailTrade.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			retailTradeCreated.setIgnoreSlaveListInComparision(true);
		}
		if (retailTrade.getSourceID() > 0) {
			retailTrade.setSn(retailTradeCreated.getSn());
		}
		RetailTradeCP.verifyCreate(retailTradeCreated, retailTrade, mapBeforeSale, mapAfterSale, simpleCommodityList, mapBO, Shared.DBName_Test, true);

		return retailTradeCreated;
	}

	// TODO PurchasingOrder ??
	public static PurchasingOrder createAndApprovePurchasingOrderViaAction(MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, String purchasingOrderCommNO, String priceSuggestion, String commodityIdString, String barcodeIdString, int shopID)
			throws Exception, UnsupportedEncodingException {
		PurchasingOrderBO purchasingOrderBO = (PurchasingOrderBO) mapBO.get(PurchasingOrderBO.class.getSimpleName());
		ProviderCommodityBO providerCommodityBO = (ProviderCommodityBO) mapBO.get(ProviderCommodityBO.class.getSimpleName());
		PurchasingOrder purchasingOrderForCreate = new PurchasingOrder();
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, commodityIdString)//
						.param(KEY_NOS, purchasingOrderCommNO) //
						.param(KEY_PRICESUGGESTIONS, priceSuggestion) //
						.param(KEY_COMMPURCHASINGUNIT, "桶") //
						.param(KEY_PROVIDERID, "1") //
						.param(KEY_BARCODEIDS, barcodeIdString) //
						.param(KEY_shopID, String.valueOf(shopID)) //
						.session((MockHttpSession) session)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder purchasingOrderForApprove = new PurchasingOrder();
		purchasingOrderForApprove = (PurchasingOrder) purchasingOrderForApprove.parse1(object.getString(BaseAction.KEY_Object));
		PurchasingOrderCP.verifyCreate(mr, purchasingOrderForCreate, purchasingOrderBO, Shared.DBName_Test);
		// 审核采购订单
		String url = "/purchasingOrder/approveEx.bx";
		MvcResult mr1 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrderForApprove.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrderForApprove.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrderForApprove.getRemark())//
						.param(KEY_COMMIDS, commodityIdString)//
						.param(KEY_BARCODEIDS, barcodeIdString)//
						.param(KEY_NOS, String.valueOf(purchasingOrderCommNO))//
						.param(KEY_PRICESUGGESTIONS, String.valueOf(priceSuggestion))//
						.session((MockHttpSession) session) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		//
		PurchasingOrderCP.verifyApprove(purchasingOrderForApprove, purchasingOrderBO, providerCommodityBO, Shared.DBName_Test);
		return purchasingOrderForApprove;
	}

	// TODO Warehousing??
	public static void createAndApproveWarehousingViaAction(MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, int[] warehousingCommNO, double[] warehousingCommPrice, Commodity[] commodityCreated, Barcodes[] barcodeCreated,
			PurchasingOrder purchasingOrderForApprove, int shopID) throws Exception, UnsupportedEncodingException, ParseException {
		WarehousingBO warehousingBO = (WarehousingBO) mapBO.get(WarehousingBO.class.getSimpleName());
		PurchasingOrderBO purchasingOrderBO = (PurchasingOrderBO) mapBO.get(PurchasingOrderBO.class.getSimpleName());
		CommodityHistoryBO commodityHistoryBO = (CommodityHistoryBO) mapBO.get(CommodityHistoryBO.class.getSimpleName());
		MessageBO messageBO = (MessageBO) mapBO.get(MessageBO.class.getSimpleName());
		String warehousingCommNOStr = "";
		String warehousingCommPriceStr = "";
		String warehousingCommAmountsStr = "";
		String commodityCreatedStr = "";
		String barcodeCreatedStr = "";
		for (int i = 0; i < warehousingCommNO.length; i++) {
			warehousingCommNOStr += warehousingCommNO[i] + ",";
			warehousingCommPriceStr += warehousingCommPrice[i] + ",";
			warehousingCommAmountsStr += warehousingCommNO[i] * warehousingCommPrice[i] + ",";
			commodityCreatedStr += commodityCreated[i].getID() + ",";
			barcodeCreatedStr += barcodeCreated[i].getID() + ",";
		}
		// 创建入库单
		MvcResult result = mvc.perform( //
				post("/warehousing/retrieveNCommEx.bx") //
						.session((MockHttpSession) session) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), purchasingOrderForApprove.getID() + "") //
		) //
				.andExpect(status().isOk()) //
				.andReturn(); //
		//
		Warehousing whousing = new Warehousing();
		whousing.setPurchasingOrderID(purchasingOrderForApprove.getID());
		whousing.setProviderID(1);
		whousing.setWarehouseID(1);
		whousing.setStaffID(EnumTypeRole.ETR_Boss.getIndex());// 15854320895手机号登录
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(purchasingOrderForApprove.getID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, commodityCreatedStr) //
						.param(commNOs, warehousingCommNOStr) //
						.param(commPrices, warehousingCommPriceStr) //
						.param(amounts, warehousingCommAmountsStr) //
						.param(barcodeIDs, barcodeCreatedStr) //
						.param(KEY_shopID, String.valueOf(shopID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) result.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject o1 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Integer wsID = JsonPath.read(o1, "$.object.ID");
		//
		ErrorInfo ecOut = new ErrorInfo();
		Warehousing wsR1 = (Warehousing) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Warehousing).read1(wsID, 3, ecOut, Shared.DBName_Test);
		String wsCreateDatetime = JsonPath.read(o1, "$.object.createDatetime");
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		Date createDatetime = sdf1.parse(wsCreateDatetime);
		//
		Assert.assertTrue(createDatetime.getTime() == (wsR1.getCreateDatetime().getTime())); // ... TODO 需要增加从表验证 创建 修改 审核

		Warehousing warehousingForApprove = new Warehousing();
		warehousingForApprove = (Warehousing) warehousingForApprove.parse1(o1.getString(BaseAction.KEY_Object));
		// 审核入库单
		List<Commodity> commList = new ArrayList<>();
		for (int i = 0; i < commodityCreated.length; i++) {
			commList.add(commodityCreated[i]);
		}
		//
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousingForApprove.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousingForApprove.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousingForApprove.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// TODO dev的nbr老板的openid如果不为null，并且能正常发送微信公众号消息，或者openid为null，则错误码应为no_Error
		// 如果老板的openid不为null，又发送微信公众号不成功，则错误码为partSuccess
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_PartSuccess);
		 Shared.checkJSONErrorCode(mr3);
		WarehousingCP.verifyApprove(mr3, warehousingForApprove, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test, shopID);
	}

	public static RetailTradeCommodity createRetailtradeCommodityViaMapper(RetailTrade rt, Commodity commodity, Barcodes barcodes) {
		RetailTradeCommodity rtc = new RetailTradeCommodity();
		rtc.setTradeID(rt.getID());
		rtc.setCommodityID(commodity.getID());
		rtc.setCommodityName(commodity.getName());
		rtc.setBarcodeID(barcodes.getID());
		rtc.setNO(100);
		rtc.setNOCanReturn(100);
		rtc.setOperatorStaffID(STAFF_ID4);
		rtc.setPriceOriginal(100 * commodity.getPriceRetail());
		rtc.setPriceReturn(100 * commodity.getPriceRetail());
		String err = rtc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> params = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity rtcCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(params); // ...
		Assert.assertTrue(rtcCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		rtc.setIgnoreIDInComparision(true);
		if (rtc.compareTo(rtcCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		return rtcCreate;
	}

	public static RetailTradeCommodity createRetailTradeCommodityViaMapper(int commodityID) throws CloneNotSupportedException, InterruptedException {
		RetailTrade retailTradeGet = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(retailTradeGet);
		RetailTradeCommodity rtc = DataInput.getRetailTradeCommodity();
		rtc.setCommodityID(commodityID);
		rtc.setTradeID(retailTradeCreate.getID());
		Map<String, Object> params = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(params); // ...
		//
		rtc.setIgnoreIDInComparision(true);
		if (rtc.compareTo(retailTradeCommodityCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		// //没有checkCreate
		// String err = retailTradeCommodityCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(err, "");
		return retailTradeCommodityCreate;
	}

	public static RetailTradeCommodity createRetailTradeCommodityViaMapper(int commodityID, int commodityNO) throws CloneNotSupportedException, InterruptedException {
		RetailTrade retailTradeGet = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(retailTradeGet);
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setCommodityID(commodityID);
		rtc.setNO(commodityNO);
		rtc.setTradeID(retailTradeCreate.getID());
		Map<String, Object> params = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(params); // ...
		//
		Assert.assertTrue(retailTradeCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		rtc.setIgnoreIDInComparision(true);
		if (rtc.compareTo(retailTradeCommodityCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// //没有checkCreate
		// String err = retailTradeCommodityCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(err, "");
		return retailTradeCommodityCreate;
	}

	public static RetailTrade createRetailTradeViaMapper() throws Exception {
		RetailTrade rt = DataInput.getRetailTrade();
		Map<String, Object> rtCreateParams = rt.getCreateParamEx(BaseBO.INVALID_CASE_ID, rt);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(rtCreateParams);
		Assert.assertTrue(createEx != null && createEx.size() > 0 && EnumErrorCode.values()[Integer.parseInt(rtCreateParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		return RetailTrade.fetchRetailTradeFromResultSet(createEx);
	}

	public static RetailTrade createRetailTradeViaAction(MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, RetailTrade rt, String dbName)
			throws CloneNotSupportedException, InterruptedException, Exception, UnsupportedEncodingException {
		WarehousingBO warehousingBO = (WarehousingBO) mapBO.get(WarehousingBO.class.getSimpleName());
		CommodityBO commodityBO = (CommodityBO) mapBO.get(CommodityBO.class.getSimpleName());
		//
		Map<Integer, Commodity> simpleCommodityList = null;
		boolean bIncludeDeletedCommodity = false;
		if (rt.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			bIncludeDeletedCommodity = true;
		}
		simpleCommodityList = getSimpleCommodityListFromRetailTrade(rt, dbName, bIncludeDeletedCommodity, commodityBO);
		Map<Integer, List<Warehousing>> mapBeforeSale = queryWarehousingBeforeSale(simpleCommodityList, dbName, rt, warehousingBO);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param("retailTrade", Shared.toJSONString(rt)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		RetailTrade retailTrade = (RetailTrade) Shared.parse1ObjectByFastjson(mr, rt, BaseAction.KEY_Object);
		Assert.assertTrue(retailTrade != null, "返回数据为空，这不是预计的情况");

		Map<Integer, List<Warehousing>> mapAfterSale = queryWarehousingAfterSale(mapBeforeSale, dbName, warehousingBO);
		if (retailTrade.getStatus() == RetailTrade.EnumStatusRetailTrade.ESRT_IncludeDeletedCommodity.getIndex()) {
			retailTrade.setIgnoreSlaveListInComparision(true);
		}

		String sn = rt.getSn();
		if (retailTrade.getSourceID() > 0) {
			// 退货单并不需要比较SN
			rt.setSn(retailTrade.getSn());
		}
		// 零售单的检查点
		RetailTradeCP.verifyCreate(retailTrade, rt, mapBeforeSale, mapAfterSale, simpleCommodityList, mapBO, dbName, true);
		rt.setSn(sn);

		return retailTrade;
	}

	public static RetailTrade createRetailTradeViaMapper(int sourceID) throws CloneNotSupportedException, InterruptedException {
		// Random ran = new Random();
		RetailTrade retailTradeInput = null;

		retailTradeInput = new RetailTrade();
		retailTradeInput.setSn(Shared.generateRetailTradeSN(5));
		// retailTradeInput.setLocalSN(ran.nextInt(1000));
		retailTradeInput.setLocalSN(Integer.parseInt(String.format("%09d", System.currentTimeMillis() % 1000000000)));
		retailTradeInput.setPos_ID(1);
		retailTradeInput.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		retailTradeInput.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
		retailTradeInput.setStaffID(3);
		retailTradeInput.setPaymentType(1);
		retailTradeInput.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTradeInput.setAmount(50);
		retailTradeInput.setAmountCash(50d);
		Thread.sleep(1);
		retailTradeInput.setRemark("remarkA:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		retailTradeInput.setSourceID(sourceID);
		retailTradeInput.setSaleDatetime(new Date());
		retailTradeInput.setSyncDatetime(new Date());

		Map<String, Object> params = retailTradeInput.getCreateParamEx(BaseBO.INVALID_CASE_ID, retailTradeInput);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(params);
		Assert.assertTrue(createEx != null && createEx.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		// 如果是退货单，创建后的对象的SN会比创建前的多了个"_1"，为了让下面的assert能过，这里把sn设置成一样
		RetailTrade retailTradeCreate = RetailTrade.fetchRetailTradeFromResultSet(createEx);
		if (sourceID != BaseAction.INVALID_ID) {
			retailTradeInput.setSn(retailTradeCreate.getSn());// 为了让下面的assert能过。
		}
		retailTradeInput.setIgnoreIDInComparision(true);
		if (retailTradeInput.compareTo(retailTradeCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		return (RetailTrade) retailTradeCreate.clone();
	}

	public static RetailTrade createReturnRetailTradeFailViaMapper(int sourceID) throws CloneNotSupportedException, InterruptedException {
		Random ran = new Random();
		RetailTrade retailTradeInput = null;

		retailTradeInput = new RetailTrade();
		retailTradeInput.setLocalSN(ran.nextInt(1000));
		retailTradeInput.setPos_ID(1);
		retailTradeInput.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		retailTradeInput.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
		retailTradeInput.setStaffID(3);
		retailTradeInput.setPaymentType(1);
		retailTradeInput.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
		retailTradeInput.setAmount(50);
		retailTradeInput.setAmountCash(50d);
		Thread.sleep(1);
		retailTradeInput.setRemark("remarkA:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);
		retailTradeInput.setSourceID(sourceID);
		retailTradeInput.setSaleDatetime(new Date());
		retailTradeInput.setSyncDatetime(new Date());

		Map<String, Object> params = retailTradeInput.getCreateParamEx(BaseBO.INVALID_CASE_ID, retailTradeInput);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = retailTradeMapper.createEx(params);
		Assert.assertTrue(createEx.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		return null;
	}

	public static RetailTradeCommodity createRetailTradeCommodityViaMapper(int tradeID, int commID, int no, EnumErrorCode error) throws CloneNotSupportedException, InterruptedException {
		//
		Random ran = new Random();
		RetailTradeCommodity rtcInput = null;
		rtcInput = new RetailTradeCommodity();
		rtcInput.setTradeID(tradeID);
		rtcInput.setCommodityID(commID);
		rtcInput.setBarcodeID(1);
		rtcInput.setNO(no);
		rtcInput.setPriceOriginal(ran.nextInt(10) + 1);
		rtcInput.setNOCanReturn(rtcInput.getNO());
		rtcInput.setPriceReturn(0);
		rtcInput.setPriceVIPOriginal(0);
		rtcInput.setPriceSpecialOffer(0);
		rtcInput.setOperatorStaffID(STAFF_ID1);

		Map<String, Object> createParams = rtcInput.getCreateParam(BaseBO.INVALID_CASE_ID, rtcInput);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(createParams); // ...
		rtcInput.setIgnoreIDInComparision(true);
		if (error == EnumErrorCode.EC_NoError) {
			if (rtcInput.compareTo(retailTradeCommCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
		}

		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == error, "创建对象成功");
		if (error == EnumErrorCode.EC_NoError) {
			Assert.assertTrue(retailTradeCommCreate != null);
		} else {
			return null;
		}

		return (RetailTradeCommodity) retailTradeCommCreate.clone();
	}
}
