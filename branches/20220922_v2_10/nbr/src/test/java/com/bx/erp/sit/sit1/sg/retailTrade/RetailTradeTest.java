package com.bx.erp.sit.sit1.sg.retailTrade;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.BasePosTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class RetailTradeTest extends BaseActionTest {
	protected AtomicInteger order;

	private static HttpSession session; // 老板账号登录
	private static HttpSession session1; // pos登录

	// 要运行创建公司时注释掉这四行，放开下面四行
	protected String bossPhone = "";
	protected String companySN = "";
	protected String dbName = "";
	// protected final String bossPhone = "15854320895";
	// protected final String bossPassword = "000000";
	// protected final String companySN = "668866";
	// protected final String dbName = "nbr";

	private static final String CommIDs = "commIDs";
	private static final String BarcodeIDs = "barcodeIDs";
	private static final String CommNOs = "commNOs";
	private static final String CommPrices = "commPrices";
	private static final String Amounts = "amounts";

	// SIT1_nbr_SG_RetailTrade_1
	@Test
	public void retrieveNRetailTrade() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_RetailTrade_", order, "新增门店");
		// 创建公司
		Company company = BaseCompanyTest.DataInput.getCompany();
		bossPhone = company.getBossPhone();
		dbName = company.getDbName();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);
		//
		companySN = ((Company) Shared.parse1Object(mr, company, BaseAction.KEY_Object)).getSN();
		//
		session = Shared.getStaffLoginSession(mvc, company.getBossPhone(), company.getBossPassword(), companySN);
		// 查询零售单
		List<RetailTrade> listRetailTrade = retrieveNExRetailTrade();
		Assert.assertTrue(listRetailTrade.size() == 0, "数据不是预想的，预想的这数据应该为0条");
		// 登录回原来的session
		session = Shared.getStaffLoginSession(mvc, bossPhone, Shared.PASSWORD_DEFAULT, companySN);
	}

	protected List<RetailTrade> retrieveNExRetailTrade() throws Exception, UnsupportedEncodingException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date date = c.getTime();
		String datetimeStart = sdf.format(new Date());
		String datetimeEnd = sdf.format(date);
		//
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), "")//
						.param(RetailTrade.field.getFIELD_NAME_datetimeStart(), datetimeStart)//
						.param(RetailTrade.field.getFIELD_NAME_datetimeEnd(), datetimeEnd)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<RetailTrade> listRetailTrade = JsonPath.read(o, "$.objectList");

		return listRetailTrade;
	}

	// SIT1_nbr_SG_RetailTrade_2
	@Test(dependsOnMethods = "retrieveNRetailTrade")
	public void createRetailTrade() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_RetailTrade_", order, "新增销售记录");

		session = Shared.getStaffLoginSession(mvc, bossPhone, Shared.PASSWORD_DEFAULT, companySN);
		//创建商品
		Commodity c = BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, session, mapBO, dbName);
		// 创建入库单1
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(c.getID());
		String commNOs = "28";
		String commPrices = "99";
		String amounts = "2772";
		String barcodeIDs = "1";
		Warehousing w1 = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 审核入库单1
		approveWarehousing(w1, commIDs);
		// 创建pos机
		Pos pos = BasePosTest.DataInput.getPOS();
		pos.setCompanySN(companySN);
		//
		Pos posCreate = BasePosTest.createPosViaSyncAction(pos, mvc, mapBO, dbName);
		Integer posID = posCreate.getID();
		session1 = Shared.getPosLoginSession(mvc, posID, bossPhone, Shared.PASSWORD_DEFAULT, companySN, posCreate.getPasswordInPOS());
		// 获取商品缓存（目的是拿到库存）
		Integer[] iaCommID = GeneralUtil.toIntArray(commIDs);
		List<Commodity> commList = new ArrayList<>();
		for (int i = 0; i < iaCommID.length; i++) {
			Commodity commodity = new Commodity();
			commodity.setID(iaCommID[i]);
			Commodity commodityCache = getCommodityCache(commodity.getID());
			commList.add(commodityCache);
		}
		// 获取零售前的商品当值入库单
		Warehousing w2 = new Warehousing();
		List<WarehousingCommodity> wscList = BaseRetailTradeTest.getWarehousingCommodityList(commList, warehousingBO, dbName);
		w2.setListSlave1(wscList);
		// 零售
		RetailTrade rt = BaseTestNGSpringContextTest.DataInput.getRetailTradeAndRetailTradeCommodity();
		rt.setPos_ID(posID);
		rt.setPaymentType(1);
		rt.setStaffID(getStaffFromSession(session).getID()); // 登录的staffID
		//
		RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(0);
		rtc.setCommodityID(1);
		rtc.setNO(1);
		rtc.setNOCanReturn(1);
		rtc.setBarcodeID(1);
		rtc.setPriceVIPOriginal(327);
		rtc.setPriceOriginal(327);
		rtc.setPriceReturn(327);
		//
		Company company = (Company) session.getAttribute(EnumSession.SESSION_Company.getName());
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, session, mapBO, rt, company.getDbName());
	}

	// SIT1_nbr_SG_RetailTrade_3
	@Test(dependsOnMethods = "createRetailTrade")
	public void NewMachineRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_RetailTrade_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

		String oldSessionId = session.getId();
		session = Shared.getStaffLoginSession(mvc, bossPhone, Shared.PASSWORD_DEFAULT, companySN);
		Assert.assertNotEquals(session.getId(), oldSessionId);

		// 查询零售单
		List<RetailTrade> listRetailTrade = retrieveNExRetailTrade();
		Assert.assertTrue(listRetailTrade.size() != 0, "数据不是预想的，预想的这数据应该大于0条");
	}

	protected Warehousing approveWarehousing(Warehousing w, String commIDs) throws Exception, UnsupportedEncodingException {
		// 获取商品缓存（目的是拿到库存）
		Integer[] iaCommID = GeneralUtil.toIntArray(commIDs);
		List<Commodity> commList = new ArrayList<>();
		for (int i = 0; i < iaCommID.length; i++) {
			Commodity commodity = new Commodity();
			commodity.setID(iaCommID[i]);
			Commodity commodityCache = getCommodityCache(commodity.getID());
			commList.add(commodityCache);
		}
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(w.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(JsonPath.read(o, "$.object").toString());
		// 审核入库单的检查点
		WarehousingCP.verifyApprove(mr, w, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName, BaseRetailTradeTest.defaultShopID);
		return warehousing1;
	}

	// SIT1_nbr_SG_RetailTrade_4
	@Test(dependsOnMethods = "NewMachineRetrieveN")
	public void TwoPosMeanwhileDBOper() {// 后台做不到
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_RetailTrade_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");
	}

	// SIT1_nbr_SG_RetailTrade_5
	@Test(dependsOnMethods = "TwoPosMeanwhileDBOper")
	public void NewSalesReturnRecord() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_RetailTrade_", order, "新增销售退货记录");

		session = Shared.getStaffLoginSession(mvc, bossPhone, Shared.PASSWORD_DEFAULT, companySN);
		// 创建入库单1
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = "1";
		String commNOs = "28";
		String commPrices = "99";
		String amounts = "2772";
		String barcodeIDs = "1";
		Warehousing w1 = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 审核入库单1
		approveWarehousing(w1, commIDs);
		// 创建pos机
		Pos pos = BasePosTest.DataInput.getPOS();
		pos.setCompanySN(companySN);
		//
		Pos posCreate = BasePosTest.createPosViaSyncAction(pos, mvc, mapBO, dbName);
		Integer posID = posCreate.getID();
		session1 = Shared.getPosLoginSession(mvc, posID, bossPhone, Shared.PASSWORD_DEFAULT, companySN, posCreate.getPasswordInPOS());
		// 获取商品缓存（目的是拿到库存）
		Integer[] iaCommID = GeneralUtil.toIntArray(commIDs);
		List<Commodity> commList = new ArrayList<>(); // 创建零售单前的商品库存
		for (int i = 0; i < iaCommID.length; i++) {
			Commodity commodity = new Commodity();
			commodity.setID(iaCommID[i]);
			Commodity commodityCache = getCommodityCache(commodity.getID());
			commList.add(commodityCache);
		}
		// 获取零售前的商品当值入库单
		Warehousing w2 = new Warehousing();
		List<WarehousingCommodity> wscList = BaseRetailTradeTest.getWarehousingCommodityList(commList, warehousingBO, dbName);
		w2.setListSlave1(wscList);
		// 创建零售单
		RetailTrade rt = BaseTestNGSpringContextTest.DataInput.getRetailTradeAndRetailTradeCommodity();
		rt.setPos_ID(posID);
		rt.setPaymentType(1);
		rt.setStaffID(getStaffFromSession(session).getID()); // 登录的staffID
		//
		int noSold = 5; // 卖出的数量
		int noReturned = 2; // 退货的数量
		//
		RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(0);
		rtc.setCommodityID(1);
		rtc.setNO(noSold);
		rtc.setNOCanReturn(noSold);
		rtc.setBarcodeID(1);
		rtc.setPriceVIPOriginal(327);
		rtc.setPriceOriginal(327);
		rtc.setPriceReturn(327);
		//
		Company company = (Company) session.getAttribute(EnumSession.SESSION_Company.getName());
		RetailTrade retailTrade = BaseRetailTradeTest.createRetailTradeViaAction(mvc, session, mapBO, rt, company.getDbName());

		List<Commodity> commListRT = new ArrayList<>(); // 创建零售单后的商品库存
		for (int i = 0; i < iaCommID.length; i++) {
			Commodity commodity = new Commodity();
			commodity.setID(iaCommID[i]);
			Commodity commodityCache = getCommodityCache(commodity.getID());
			commListRT.add(commodityCache);
		}
		// 获取零售后的商品当值入库单
		Warehousing w2RT = new Warehousing();
		List<WarehousingCommodity> wscListRT = BaseRetailTradeTest.getWarehousingCommodityList(commListRT, warehousingBO, dbName);
		w2RT.setListSlave1(wscListRT);
		// 创建零售退货单
		RetailTrade rtReturn = BaseTestNGSpringContextTest.DataInput.getRetailTradeAndRetailTradeCommodity();
		rtReturn.setPos_ID(posID);
		rtReturn.setPaymentType(1);
		rtReturn.setStaffID(getStaffFromSession(session).getID()); // 登录的staffID
		rtReturn.setSourceID(retailTrade.getID());
		//

		RetailTradeCommodity rtcReturn = (RetailTradeCommodity) rtReturn.getListSlave1().get(0);
		rtcReturn.setCommodityID(1);
		rtcReturn.setNO(noReturned);
		rtcReturn.setNOCanReturn(noSold - noReturned);
		rtcReturn.setBarcodeID(1);
		rtcReturn.setPriceVIPOriginal(327);
		rtcReturn.setPriceOriginal(327);
		rtcReturn.setPriceReturn(327);
		
		RetailTrade retailTradeReturn = BaseRetailTradeTest.createRetailTradeViaAction(mvc, session, mapBO, rtReturn, company.getDbName());
		// 在库管查询页面验证库存
		List<Commodity> commListReturn = new ArrayList<>(); // 创建零售退货单后的商品库存
		for (int i = 0; i < iaCommID.length; i++) {
			Commodity commodity = new Commodity();
			commodity.setID(iaCommID[i]);
			Commodity commodityCache = getCommodityCache(commodity.getID());
			commListReturn.add(commodityCache);
		}
		//
		Assert.assertTrue(commList.get(0).getNO() == (noSold - noReturned) + commListReturn.get(0).getNO() && commList.get(0).getNO() - noSold == commListRT.get(0).getNO() //
				&& commListReturn.get(0).getNO() - noReturned == commListRT.get(0).getNO());
		//
		// 在零售记录页面验证零售退货单
		MvcResult mr = mvc.perform(//
				post("/retailTrade/retrieveNEx.bx")//
						.session((MockHttpSession) session)//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTrade.field.getFIELD_NAME_queryKeyword(), retailTradeReturn.getSn())//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<BaseModel> listRetailTrade = new RetailTrade().parseN(o.getString(BaseAction.KEY_ObjectList));
		RetailTrade TH = (RetailTrade) listRetailTrade.get(0);
		if (TH.compareTo(retailTradeReturn) != 0) {
			Assert.assertTrue(false, "测试失败！期望的是能够查询出创建的零售退货单:" + retailTradeReturn.getSn());
		}
	}

	private Commodity getCommodityCache(int commodityID) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "从缓存读取商品失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		return commodityCache;
	}

	protected Warehousing createWarehousing(Warehousing w, String commIDs, String commNOs, String commPrices, String amounts, String barcodeIDs) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param(CommIDs, commIDs) //
						.param(CommNOs, commNOs) //
						.param(CommPrices, commPrices) //
						.param(Amounts, amounts) //
						.param(BarcodeIDs, barcodeIDs) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 创建入库单的检查点
		w.setStaffID(getStaffFromSession(session).getID());
		WarehousingCP.verifyCreate(mr, w, dbName);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(JsonPath.read(o, "$.object").toString());

		return warehousing1;
	}

	@BeforeClass
	public void beforeClass() {
		super.setUp();

		order = new AtomicInteger();
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("当前使用的staff=" + staff);

		return staff;
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}
}
