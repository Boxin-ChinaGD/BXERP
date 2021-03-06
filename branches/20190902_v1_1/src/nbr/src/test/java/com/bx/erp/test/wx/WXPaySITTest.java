package com.bx.erp.test.wx;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.Company.EnumCompanyCreationStatus;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.model.wx.WXPayInfo;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class WXPaySITTest extends BaseActionTest {

	private HttpSession staffSession; // ?????????staff??????
	private HttpSession posSession; // ?????????pos??????

	protected final String companyName = Shared.generateCompanyName(Company.MAX_LENGTH_Name);
	protected String companySN = null;
	protected final String businessLicenseSN = "123666666666678";
	protected final String bossName = "?????????";
	protected final String bossPhone = "13914126904";
	protected final String bossPassword = "000000";
	protected final String bossWechat = "123study1";
	protected final String dbName = "nbr_test_wx" + Shared.generateCompanyName(9);
	protected final String submchid2 = "1545789261";// ?????????????????????
	protected final String dbUserName = "two";
	protected final String dbUserPassword = "123456";

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	public static class DataInput {
		private static RetailTrade retailTrade = null;
		private static Random ran = new Random();

		protected static RetailTrade getRetailTrade(int posID, int CommodityID1, int CommodityID2) throws CloneNotSupportedException, InterruptedException {
			retailTrade = new RetailTrade();
			retailTrade.setSn(Shared.generateRetailTradeSN(5));
			retailTrade.setLocalSN(ran.nextInt(1000) + 1);
			retailTrade.setPos_ID(posID);
			retailTrade.setSaleDatetime(new Date());
			retailTrade.setLogo("11");
			retailTrade.setStaffID(ran.nextInt(4) + 1);
			retailTrade.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
			retailTrade.setPaymentType(1);
			retailTrade.setPaymentAccount("12");
			retailTrade.setRemark("11111");
			retailTrade.setSourceID(-1);
			retailTrade.setAmount(2222.2d);
			retailTrade.setAmountCash(2222.2d);
			retailTrade.setSmallSheetID(1);
			retailTrade.setSyncDatetime(new Date());
			retailTrade.setSaleDatetime(new Date());
			retailTrade.setAliPayOrderSN("zfb" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTrade.setWxRefundSubMchID(BaseCompanyTest.nbrSubmchid);
			retailTrade.setCreateDatetime(new Date());
			retailTrade.setReturnObject(1);

			RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
			retailTradeCommodity.setCommodityID(CommodityID1);
			retailTradeCommodity.setNO(20);
			retailTradeCommodity.setPriceOriginal(222.6);
			retailTradeCommodity.setBarcodeID(1);
			retailTradeCommodity.setNOCanReturn(20);
			retailTradeCommodity.setPriceReturn(100d);
			retailTradeCommodity.setPriceVIPOriginal(0);
			retailTradeCommodity.setPriceSpecialOffer(0);

			RetailTradeCommodity retailTradeCommodity2 = new RetailTradeCommodity();
			retailTradeCommodity2.setCommodityID(CommodityID2);
			retailTradeCommodity2.setNO(20);
			retailTradeCommodity2.setPriceOriginal(222.6);
			retailTradeCommodity2.setBarcodeID(1);
			retailTradeCommodity2.setNOCanReturn(20);
			retailTradeCommodity2.setPriceReturn(100d);
			retailTradeCommodity2.setPriceVIPOriginal(0);
			retailTradeCommodity2.setPriceSpecialOffer(0);

			List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
			listRetailTradeCommodity.add(retailTradeCommodity);
			listRetailTradeCommodity.add(retailTradeCommodity2);

			retailTrade.setListSlave1(listRetailTradeCommodity);

			return retailTrade;
		}

		protected static final MockHttpServletRequestBuilder getRetailTradeBuilder(String url, MediaType contentType, RetailTrade rt) throws Exception {
			// String json = JSONObject.fromObject(rt).toString();

			MockHttpServletRequestBuilder builder = post(url).contentType(contentType) //
					// .param(rt.getFIELD_NAME_pos_SN(), "111" + System.currentTimeMillis() %
					// 1000000) //
					// .param(rt.getFIELD_NAME_pos_ID(), "1") //
					// .param(rt.getFIELD_NAME_saleDatetime(), sdf.format(new Date())) //
					// .param(rt.getFIELD_NAME_logo(), "1" + System.currentTimeMillis() % 1000000)
					// //
					// .param(rt.getFIELD_NAME_staffID(), "1") //
					// .param(rt.getFIELD_NAME_paymentType(), "0") //
					// .param(rt.getFIELD_NAME_paymentAccount(), "12.5") //
					// .param(rt.getFIELD_NAME_remark(), "1" + System.currentTimeMillis() % 1000000)
					// //
					// .param(rt.getFIELD_NAME_amount(), "2000") //
					// .param(rt.getFIELD_NAME_smallSheetID(), "1") //
					// .param(rt.getFIELD_NAME_syncDatetime(), sdf.format(new Date())) //
					// .param(rt.getFIELD_NAME_sourceID(), "-1") //
					// .param(rt.getFIELD_NAME_int1(), "1") //
					// .param(rt.getFIELD_NAME_wxOrderSN(), rt.getWxOrderSN())//
					// .param(rt.getFIELD_NAME_wxTradeNO(), rt.getWxTradeNO())//
					// .param(rt.getFIELD_NAME_wxRefundSubMchID(), rt.getWxRefundSubMchID())//
					// .param(rt.getFIELD_NAME_syncDatetime(), sdf.format(new Date()))
					.param("retailTrade", Shared.toJSONString(rt)); // //
			return builder;
		}
	}

	@Test
	public void testWXPayWithoutPromotion() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:nbr?????????????????????????????????????????????");
		// 1.?????????????????????????????????
		WXPayInfo wpi = new WXPayInfo();
		wpi.setAuth_code("134526050509995238");
		wpi.setTotal_fee("1.00"); // ???api????????????2?????????
		MvcResult microPayResponse = mvc.perform(//
				post("/wxpay/microPayEx.bx")//
						.param(WXPayInfo.field.getFIELD_NAME_auth_code(), wpi.getAuth_code()) //
						.param(WXPayInfo.field.getFIELD_NAME_total_fee(), wpi.getTotal_fee()) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		JSONObject mircoPayJson = JSONObject.fromObject(microPayResponse.getResponse().getContentAsString());
		if (mircoPayJson.get(BaseWxModel.WXPAY_ERR_CODE).equals("AUTH_CODE_INVALID")) {
			Shared.checkJSONErrorCode(microPayResponse, EnumErrorCode.EC_OtherError);
			Assert.assertTrue(false, mircoPayJson.get(BaseWxModel.WXPAY_ERR_CODE_DES).toString() + "(?????????????????????)");
		}
		Shared.checkJSONErrorCode(microPayResponse);
		// 2.?????????????????????
		Commodity c = new Commodity();
		c.setID(154);
		//
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(c, EnumErrorCode.EC_NoError);

		RetailTrade rt = DataInput.getRetailTrade(5, 154, 156); // posID,commodityID,commodityID
		rt.setWxOrderSN(mircoPayJson.get("transaction_id").toString());
		rt.setWxTradeNO(mircoPayJson.get("out_trade_no").toString());
		rt.setWxRefundSubMchID(mircoPayJson.get("sub_mch_id") == null ? BaseCompanyTest.nbrSubmchid : mircoPayJson.get("sub_mch_id").toString());

		MvcResult rtResponse = mvc.perform(//
				DataInput.getRetailTradeBuilder("/retailTrade/createEx.bx", MediaType.APPLICATION_JSON, rt) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(rtResponse);
		JSONObject rtJson = JSONObject.fromObject(rtResponse.getResponse().getContentAsString());

		Map<String, Object> retrieveNParam = rt.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rt);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = retailTradeMapper.retrieveN(retrieveNParam);

		for (BaseModel bm : list) {
			RetailTrade s = (RetailTrade) bm;
			if (s.getLogo().equals(rt.getLogo())) {
				assertTrue(true);
			}
		}
		System.out.println("----------------------??????????????????????????????????????????????????????????????????????????????--------------------------");
		Commodity commR1 = BaseCommodityTest.retrieve1ExCommodity(c, EnumErrorCode.EC_NoError);
		Assert.assertTrue(commR1.getNO() == (comm.getNO() - 20));

		// 3.??????????????????????????????????????????????????????
		String wxRefundSubMchID = JsonPath.read(rtJson.get("object"), "$.wxRefundSubMchID");
		String wxOrderSN = JsonPath.read(rtJson.get("object"), "$.wxOrderSN");
		String wxTradeNO = JsonPath.read(rtJson.get("object"), "$.wxTradeNO");
		MvcResult refundResponse = mvc.perform(//
				post("/wxpay/refundEx.bx")//
						.param(RetailTrade.field.getFIELD_NAME_wxRefundSubMchID(), wxRefundSubMchID) //
						.param(RetailTrade.field.getFIELD_NAME_wxOrderSN(), wxOrderSN) //
						.param(RetailTrade.field.getFIELD_NAME_wxTradeNO(), wxTradeNO) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(refundResponse);
		JSONObject refundJson = JSONObject.fromObject(refundResponse.getResponse().getContentAsString());

		Assert.assertEquals(refundJson.get(BaseWxModel.WXPay_RESULT), "SUCCESS");
		Assert.assertEquals(refundJson.get(BaseWxModel.WXPay_RETURN), "SUCCESS");
	}

	@Test
	public void testWXPayWithoutPromotion2() throws Exception {
		Shared.printTestMethodStartInfo();
		// ???????????????????????????????????????????????????????????????????????????????????????
		Shared.caseLog("case2:??????????????????????????????????????????????????????");
		Map<String, Integer> comm = new HashMap<>();
		// ????????????
		Company company = new Company();
		company.setName(companyName);
		company.setBusinessLicenseSN("123456" + Shared.generateStringByTime(9));
		company.setBossName(bossName);
		company.setBossPhone(bossPhone);
		company.setBossPassword(bossPassword);
		company.setBossWechat(bossWechat);
		company.setDbName(dbName);
		company.setSubmchid(submchid2); // TODO submchid2????????????????????????,???????????????????????????????????????????????????
		company.setStatus(EnumCompanyCreationStatus.ECCS_Incumbent.getIndex());
		company.setDbUserName(dbUserName);
		company.setDbUserPassword(dbUserPassword);
		company.setBrandName(Shared.generateStringByTime(9));
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		//
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session(sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		companySN = JsonPath.read(o1, "$.object.SN");
		//
		staffSession = Shared.getStaffLoginSession(mvc, bossPhone, bossPassword, companySN);
		// ????????????????????????
		for (int i = 1; i <= 2; i++) {
			Commodity c = BaseCommodityTest.DataInput.getCommodity();
			c.setName("????????????" + System.currentTimeMillis());
			c.setMultiPackagingInfo("682142330239" + (3 + i) + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
			BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
			//
			comm.put("commID" + i, i);
		}
		// ??????pos???
		Integer posID = createExPosSync();
		// 1.?????????????????????????????????
		WXPayInfo wpi = new WXPayInfo();
		wpi.setAuth_code("134526050509995238");
		wpi.setTotal_fee("1.00"); // ???api????????????2?????????
		MvcResult microPayResponse = mvc.perform(//
				post("/wxpay/microPayEx.bx")//
						.param(WXPayInfo.field.getFIELD_NAME_auth_code(), wpi.getAuth_code()) //
						.param(WXPayInfo.field.getFIELD_NAME_total_fee(), wpi.getTotal_fee()) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) posSession) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		JSONObject mircoPayJson = JSONObject.fromObject(microPayResponse.getResponse().getContentAsString());
		if (mircoPayJson.get(BaseWxModel.WXPAY_ERR_CODE).equals("AUTH_CODE_INVALID")) {
			Shared.checkJSONErrorCode(microPayResponse, EnumErrorCode.EC_OtherError);
			Assert.assertTrue(false, mircoPayJson.get(BaseWxModel.WXPAY_ERR_CODE_DES).toString() + "(?????????????????????)");
		}
		Shared.checkJSONErrorCode(microPayResponse);
		// ?????????????????????????????????????????????
		String commIDs = comm.get("commID1") + "," + comm.get("commID2");
		Integer[] iaCommID = GeneralUtil.toIntArray(commIDs);
		List<Commodity> commList = new ArrayList<>();
		for (int i = 0; i < iaCommID.length; i++) {
			Commodity commodity = new Commodity();
			commodity.setID(iaCommID[i]);
			Commodity commodityCache = BaseCommodityTest.queryCommodityCache(commodity.getID());
			commList.add(commodityCache);
		}
		// ???????????????????????????????????????
		Warehousing w = new Warehousing();
		List<WarehousingCommodity> wscList = BaseRetailTradeTest.getWarehousingCommodityList(commList, warehousingBO, dbName);
		w.setListSlave1(wscList);

		RetailTrade rt = DataInput.getRetailTrade(posID, 1, 2);
		// 2.?????????????????????
		rt.setStaffID(STAFF_ID2);
		rt.setWxOrderSN(mircoPayJson.get("transaction_id").toString());
		rt.setWxTradeNO(mircoPayJson.get("out_trade_no").toString());
		rt.setWxRefundSubMchID(mircoPayJson.get("sub_mch_id") == null ? submchid2 : mircoPayJson.get("sub_mch_id").toString());
		Company company1 = (Company) sessionBoss.getAttribute(EnumSession.SESSION_Company.getName());
		// TODO ??????????????????DB????????????,?????????????????????
		RetailTrade bmOfDB = BaseRetailTradeTest.createRetailTradeViaAction(mvc, posSession, mapBO, rt, dbName);
		//
		Map<String, Object> retrieveNParam = rt.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rt);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = retailTradeMapper.retrieveN(retrieveNParam);
		//
		for (BaseModel bm : list) {
			RetailTrade s = (RetailTrade) bm;
			if (s.getLogo().equals(rt.getLogo())) {
				assertTrue(true);
			}
		}
		// 3.??????????????????????????????????????????????????????
		MvcResult refundResponse = mvc.perform(//
				post("/wxpay/refundEx.bx")//
						.param(RetailTrade.field.getFIELD_NAME_wxRefundSubMchID(), bmOfDB.getWxRefundSubMchID()) //
						.param(RetailTrade.field.getFIELD_NAME_wxOrderSN(), bmOfDB.getWxOrderSN()) //
						.param(RetailTrade.field.getFIELD_NAME_wxTradeNO(), bmOfDB.getWxTradeNO()) //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) posSession) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(refundResponse);
		JSONObject refundJson = JSONObject.fromObject(refundResponse.getResponse().getContentAsString());

		Assert.assertEquals(refundJson.get("result_code"), "SUCCESS");
		Assert.assertEquals(refundJson.get("return_code"), "SUCCESS");
	}

	protected Integer createExPosSync() throws Exception, UnsupportedEncodingException {
		Pos pos = BaseTestNGSpringContextTest.DataInput.getPOS();
		pos.setCompanySN(companySN);
		MvcResult mr = mvc.perform(//
				getPosBuilder("/posSync/createEx.bx", MediaType.APPLICATION_JSON, pos) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionOP) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		int posID = JsonPath.read(o, "$.object.ID");
		String passwordInPOS = JsonPath.read(o, "$.object.passwordInPOS");
		//
		Pos posCopy = (Pos) pos.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		// ??????pos????????????
		verifySyncCreateResult(mr, pos, posBO, posSyncCacheBO, posSyncCacheDispatcherBO, EnumCacheType.ECT_POS, EnumSyncCacheType.ESCT_PosSyncInfo, posID, dbName);// posID > 0??????pos??????????????????
		//
		pos = (Pos) posCopy.clone(); // ????????????
		//
		Shared.resetPOS(mvc, posID);
		posSession = Shared.getPosLoginSession(mvc, posID, bossPhone, bossPassword, companySN, passwordInPOS);

		return posID;
	}
}
