package com.bx.erp.test;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade.EnumStatusRetailTrade;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class RetailTradeCommodityActionTest extends BaseActionTest {

	private static final String INVALID_ID = "999999";
	
	private static final int COMMODITY_STARTNO = 50;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	public static class DataInput {
		private static RetailTrade retailTradeInput = null;

		protected static final RetailTrade getRetailTrade() throws CloneNotSupportedException, InterruptedException {
			Random ran = new Random();
			retailTradeInput = new RetailTrade();
			retailTradeInput.setVipID(1);
			retailTradeInput.setSn("");
			retailTradeInput.setLocalSN(ran.nextInt(1000));
			Thread.sleep(1);
			retailTradeInput.setPos_ID(ran.nextInt(5) + 1);
			retailTradeInput.setLogo("url:" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			retailTradeInput.setSaleDatetime(new Date());
			retailTradeInput.setStatus(EnumStatusRetailTrade.ESRT_Normal.getIndex());
			retailTradeInput.setStaffID(ran.nextInt(5) + 1);
			retailTradeInput.setPaymentType(1);
			retailTradeInput.setPaymentAccount("A" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			retailTradeInput.setRemark("remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			retailTradeInput.setSourceID(BaseAction.INVALID_ID);
			retailTradeInput.setSmallSheetID(ran.nextInt(7) + 1);
			retailTradeInput.setAmount(50);
			retailTradeInput.setAmountCash(50d);
			retailTradeInput.setAliPayOrderSN("zfb" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setWxOrderSN("wx" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setWxRefundDesc("xx" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setWxTradeNO("no" + String.valueOf(System.currentTimeMillis()).substring(6));
			retailTradeInput.setSyncDatetime(new Date());

			return (RetailTrade) retailTradeInput.clone();
		}

	}

	@Test
	public void createExTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		
		//创建商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setnOStart(COMMODITY_STARTNO);
		comm.setPurchasingPriceStart(10d);
		comm.setNO(COMMODITY_STARTNO);
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(comm, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		
		// 使用mapper创建trade.
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);		
		//
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(commCreate.getID());
		retailTradeCommodity.setNO(20);
		retailTradeCommodity.setNOCanReturn(20);
		retailTradeCommodity.setPriceReturn(20);
		//
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commA = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(retailTradeCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "查找商品失败！");
		}
		//
		MvcResult mr = mvc.perform(post("/retailTradeCommodity/createEx.bx")//
				.param(RetailTradeCommodity.field.getFIELD_NAME_tradeID(), String.valueOf(localRetailTrade.getID())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_commodityID(), String.valueOf(retailTradeCommodity.getCommodityID())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_barcodeID(), "1") //
				.param(RetailTradeCommodity.field.getFIELD_NAME_NO(), String.valueOf(retailTradeCommodity.getNO())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_price(), "222.6") //
				.param(RetailTradeCommodity.field.getFIELD_NAME_NOCanReturn(), String.valueOf(retailTradeCommodity.getNOCanReturn())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_priceReturn(), String.valueOf(retailTradeCommodity.getPriceReturn())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_priceSpecialOffer(), "0") //
				.param(RetailTradeCommodity.field.getFIELD_NAME_priceVIPOriginal(), "0") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//
		//
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		String commodityName = JsonPath.read(o, "$.object.commodityName");
		//
		Commodity commB = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(retailTradeCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commB == null) {
			Assert.assertTrue(false, "查找商品失败！");
		}
		Assert.assertTrue(commA.getName().equals(commodityName));
		// 
		CommodityShopInfo commodityShopInfoA = null;
		List<CommodityShopInfo> commodityShopInfoListA = (List<CommodityShopInfo>) commA.getListSlave2();
		for(CommodityShopInfo commodityShopInfo : commodityShopInfoListA) {
			if(localRetailTrade.getShopID() == commodityShopInfo.getShopID()) {
				commodityShopInfoA = commodityShopInfo;
			}
		}
		CommodityShopInfo commodityShopInfoB = null;
		List<CommodityShopInfo> commodityShopInfoListB = (List<CommodityShopInfo>) commB.getListSlave2();
		for(CommodityShopInfo commodityShopInfo : commodityShopInfoListB) {
			if(localRetailTrade.getShopID() == commodityShopInfo.getShopID()) {
				commodityShopInfoB = commodityShopInfo;
			}
		}
		Assert.assertTrue(commodityShopInfoA != null && commodityShopInfoB != null);
		Assert.assertTrue((commodityShopInfoA.getNO() - retailTradeCommodity.getNO()) == commodityShopInfoB.getNO(), "商品缓存更新失败" + commA.getNO() + "-" + retailTradeCommodity.getNO() + "!=" + commB.getNO());
	}

	@Test
	public void createExTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(5);
		retailTradeCommodity.setNO(20);
		retailTradeCommodity.setNOCanReturn(20);
		retailTradeCommodity.setPriceReturn(20);
		//
		Shared.caseLog("case:商品ID和赠品ID都不存在");
		MvcResult mr2 = mvc.perform(post("/retailTradeCommodity/createEx.bx")//
				.param(RetailTradeCommodity.field.getFIELD_NAME_tradeID(), "1") //
				.param(RetailTradeCommodity.field.getFIELD_NAME_commodityID(), INVALID_ID) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_barcodeID(), "7") //
				.param(RetailTradeCommodity.field.getFIELD_NAME_NO(), String.valueOf(retailTradeCommodity.getNO())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_price(), "222.6") //
				// .param(RetailTradeCommodity.field.getFIELD_NAME_isGift(), "0") //
				.param(RetailTradeCommodity.field.getFIELD_NAME_NOCanReturn(), String.valueOf(retailTradeCommodity.getNOCanReturn())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_priceReturn(), String.valueOf(retailTradeCommodity.getPriceReturn())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_priceSpecialOffer(), "0") //
				.param(RetailTradeCommodity.field.getFIELD_NAME_priceVIPOriginal(), "0") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError); // sp错误码为2
	}

	@Test
	public void createExTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(5);
		retailTradeCommodity.setNO(20);
		retailTradeCommodity.setNOCanReturn(20);
		retailTradeCommodity.setPriceReturn(20);
		//
		Shared.caseLog("case3:没有权限进行操作");
//		MvcResult mr3 = mvc.perform(post("/retailTradeCommodity/createEx.bx")//
//				.param(RetailTradeCommodity.field.getFIELD_NAME_tradeID(), "1") //
//				.param(RetailTradeCommodity.field.getFIELD_NAME_commodityID(), String.valueOf(retailTradeCommodity.getCommodityID())) //
//				.param(RetailTradeCommodity.field.getFIELD_NAME_barcodeID(), "1") //
//				.param(RetailTradeCommodity.field.getFIELD_NAME_NO(), String.valueOf(retailTradeCommodity.getNO())) //
//				.param(RetailTradeCommodity.field.getFIELD_NAME_price(), "222.6") //
//				.param(RetailTradeCommodity.field.getFIELD_NAME_NOCanReturn(), String.valueOf(retailTradeCommodity.getNOCanReturn())) //
//				.param(RetailTradeCommodity.field.getFIELD_NAME_priceReturn(), String.valueOf(retailTradeCommodity.getPriceReturn())) //
//				.param(RetailTradeCommodity.field.getFIELD_NAME_priceSpecialOffer(), "0") //
//				.param(RetailTradeCommodity.field.getFIELD_NAME_priceVIPOriginal(), "0") //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//				.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print()).andReturn();
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void createExTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:创建零售单商品，退货价priceReturn可以为0");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade localRetailTrade = BaseRetailTradeTest.createRetailTrade(rt);
		// 不要依赖数据库中的商品，防止商品在mapper层修改,商品缓存不正确
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaAction(commodityGet, mvc, sessionBoss, mapBO, Shared.DBName_Test); 
		//
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setCommodityID(commodityCreate.getID());
		retailTradeCommodity.setNO(20);
		retailTradeCommodity.setNOCanReturn(20);
		retailTradeCommodity.setPriceReturn(0);
		//
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commA = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(retailTradeCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commA == null) {
			Assert.assertTrue(false, "查找商品失败！");
		}

		//
		MvcResult mr = mvc.perform(post("/retailTradeCommodity/createEx.bx")//
				.param(RetailTradeCommodity.field.getFIELD_NAME_tradeID(), String.valueOf(localRetailTrade.getID())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_commodityID(), String.valueOf(retailTradeCommodity.getCommodityID())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_barcodeID(), "1") //
				.param(RetailTradeCommodity.field.getFIELD_NAME_NO(), String.valueOf(retailTradeCommodity.getNO())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_price(), "222.6") //
				.param(RetailTradeCommodity.field.getFIELD_NAME_NOCanReturn(), String.valueOf(retailTradeCommodity.getNOCanReturn())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_priceReturn(), String.valueOf(retailTradeCommodity.getPriceReturn())) //
				.param(RetailTradeCommodity.field.getFIELD_NAME_priceSpecialOffer(), "0") //
				.param(RetailTradeCommodity.field.getFIELD_NAME_priceVIPOriginal(), "0") //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();//
		//
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		String commodityName = JsonPath.read(o, "$.object.commodityName");
		//
		Commodity commB = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(retailTradeCommodity.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || commB == null) {
			Assert.assertTrue(false, "查找商品失败！");
		}
		Assert.assertTrue(commA.getName().equals(commodityName));
		// 
		CommodityShopInfo commodityShopInfoA = null;
		List<CommodityShopInfo> commodityShopInfoListA = (List<CommodityShopInfo>) commA.getListSlave2();
		for(CommodityShopInfo commodityShopInfo : commodityShopInfoListA) {
			if(localRetailTrade.getShopID() == commodityShopInfo.getShopID()) {
				commodityShopInfoA = commodityShopInfo;
			}
		}
		CommodityShopInfo commodityShopInfoB = null;
		List<CommodityShopInfo> commodityShopInfoListB = (List<CommodityShopInfo>) commB.getListSlave2();
		for(CommodityShopInfo commodityShopInfo : commodityShopInfoListB) {
			if(localRetailTrade.getShopID() == commodityShopInfo.getShopID()) {
				commodityShopInfoB = commodityShopInfo;
			}
		}
		Assert.assertTrue((commodityShopInfoA.getNO() - retailTradeCommodity.getNO()) == commodityShopInfoB.getNO(), "商品缓存更新失败");
	}

	@Test
	public void retrieveNExTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("------------------case1:id存在时，去查询------------------");
		// RetailTradeCommodity rc = new RetailTradeCommodity();
		MvcResult mr = mvc.perform(//
				get("/retailTradeCommodity/retrieveNEx.bx?" + RetailTradeCommodity.field.getFIELD_NAME_tradeID() + "=10")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn();//

		Shared.checkJSONErrorCode(mr);

		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<Integer> i1 = JsonPath.read(o1, "$.retailTradeCommodityList[*].tradeID");
		assertTrue(i1.get(0) == 10);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<String> commodityNameList = JsonPath.read(o, "$.retailTradeCommodityList[*].commodityName");

		boolean flag = false;
		for (String commodityName : commodityNameList) {
			if (commodityName.equals("雪晶灵肌密精华原")) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "RN出来的数据不正确");
	}

	@Test
	public void retrieveNExTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		System.out.println("------------------case2:id不存在时，去查询------------------");
		// RetailTradeCommodity rc1 = new RetailTradeCommodity();
		MvcResult mr2 = mvc.perform(//
				get("/retailTradeCommodity/retrieveNEx.bx?" + RetailTradeCommodity.field.getFIELD_NAME_tradeID() + "=-1")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn();//

		Shared.checkJSONErrorCode(mr2);

		String json = mr2.getResponse().getContentAsString();
		JSONObject o2 = JSONObject.fromObject(json);
		List<Integer> i2 = JsonPath.read(o2, "$.retailTradeCommodityList[*].ID");
		assertTrue(i2.size() == 0);
	}

	@Test
	public void retrieveNExTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		System.out.println("------------------case3：没有权限进行操作------------------");
//		MvcResult mr3 = mvc.perform(//
//				get("/retailTradeCommodity/retrieveNEx.bx?" + RetailTradeCommodity.field.getFIELD_NAME_tradeID() + "=1")//
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		) //
//				.andExpect(status().isOk()).andDo(print()).andReturn();//
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}
}
