package com.bx.erp.test.wx;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumEnv;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Staff;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class WxSITTest extends BaseActionTest {

	private static String phone = Shared.PhoneOfBoss; // 店员3

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	protected static class DataInput {
		private static Warehousing warehousingInput = null;
		private static WarehousingCommodity warehousingCommodityInput = null;

		public static final Warehousing getWarehousing() throws CloneNotSupportedException, InterruptedException {
			warehousingInput = new Warehousing();
			warehousingInput.setWarehouseID(1);
			warehousingInput.setProviderID(1);
			warehousingInput.setStaffID(1);
			warehousingInput.setPurchasingOrderID(7);

			return (Warehousing) warehousingInput.clone();
		}

		protected static final WarehousingCommodity getWarehousingCommodity() throws CloneNotSupportedException, InterruptedException {
			warehousingCommodityInput = new WarehousingCommodity();
			warehousingCommodityInput.setCommodityID(1);
			warehousingCommodityInput.setCommodityName("可比克薯片");
			warehousingCommodityInput.setPackageUnitID(1);
			warehousingCommodityInput.setNO(10);
			warehousingCommodityInput.setBarcodeID(1);
			warehousingCommodityInput.setPrice(12.0f);
			warehousingCommodityInput.setAmount(120.0f);
			warehousingCommodityInput.setShelfLife(36);

			return (WarehousingCommodity) warehousingCommodityInput.clone();
		}

	}

	// 创建采购订单,用于入库时发生价格变动后创建消息发送给门店老板
	public PurchasingOrder createPO() throws CloneNotSupportedException, InterruptedException {

		System.out.println("--------------------------------创建采购单---------------------------------");
		PurchasingOrder po = new PurchasingOrder();
		po.setStaffID(3);
		po.setProviderID(1);
		po.setRemark("Remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		Thread.sleep(1);

		Map<String, Object> paramsForCreate = po.getCreateParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder poCreate = (PurchasingOrder) purchasingOrderMapper.create(paramsForCreate);
		//
		po.setIgnoreIDInComparision(true);
		if (po.compareTo(poCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		assertTrue(poCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		System.out.println("--------------------------------创建采购单商品---------------------------------");

		PurchasingOrderCommodity poc1 = new PurchasingOrderCommodity();
		poc1.setCommodityID(1);
		poc1.setCommodityNO(20);
		poc1.setPurchasingOrderID(poCreate.getID());
		poc1.setPriceSuggestion(1);
		poc1.setBarcodeID(1);
		Map<String, Object> params = poc1.getCreateParam(BaseBO.INVALID_CASE_ID, poc1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params);
		//
		poc1.setIgnoreIDInComparision(true);
		if (poc1.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		System.out.println("--------------------------------审核采购单---------------------------------");

		poCreate.setApproverID(1);
		Map<String, Object> params2 = poCreate.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, poCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder approvePo = (PurchasingOrder) purchasingOrderMapper.approve(params2);
		Assert.assertTrue(approvePo != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		return (PurchasingOrder) poCreate.clone();
	}

	// 向门店老板发送采购订单已生成、待审核消息
	@Test
	public void testPurchasingOrderToWxMsgSIT() throws Exception {
		Shared.printTestMethodStartInfo(); // 调用StackTraceElement类检测方法开始的运行时间;

		Shared.caseLog("case2 :发送到一个用户的openid不正确的消息:错误码为：40003");
		// PurchasingOrder p = new PurchasingOrder();

		Staff s = new Staff();
		s.setPhone(phone);
		s.setOpenid(Shared.openid);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Staff_Update_OpenidAndUnionid, s);
		assertTrue(staffBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "更新openid失败！错误码为：" + staffBO.getLastErrorCode());

		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param("commIDs", "1")//
						.param("NOs", "2") //
						.param("priceSuggestions", "11.11") //
						.param("commPurchasingUnit", "桶") //
						.param("providerID", "2") //
						.param("barcodeIDs", "2")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, phone))//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 验证返回的数据是否正确
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		if (BaseAction.ENV != EnumEnv.DEV) {
			assertTrue(o.getInt(BaseWxModel.WX_ERRCODE) == 0 && o.getString(BaseWxModel.WX_ERRMSG).equals("ok"), "发送信息失败！！！错误码为：" + o.getInt(BaseWxModel.WX_ERRCODE) + ",错误信息为：" + o.getString(BaseWxModel.WX_ERRMSG) + "(环境不是DEV，不用修)" + "\t");
		} else {
			assertTrue(o.getInt(BaseWxModel.WX_ERRCODE) == 0 && o.getString(BaseWxModel.WX_ERRMSG).equals("ok"), "发送信息失败！！！错误码为：" + o.getInt(BaseWxModel.WX_ERRCODE) + ",错误信息为：" + o.getString(BaseWxModel.WX_ERRMSG) + "\t");
		}

		// 更新该店员的openid为不合法的openid
		Staff staff = new Staff();
		staff.setPhone(phone);
		staff.setOpenid("12312312dqw312546sdf");

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Staff_Update_OpenidAndUnionid, staff);

		assertTrue(staffBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "更新openid失败！错误码为：" + staffBO.getLastErrorCode());

		// PurchasingOrder p2 = new PurchasingOrder();

		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param("commIDs", "1") //
						.param("NOs", "4") //
						.param("priceSuggestions", "11.1") //
						.param("commPurchasingUnit", "桶") //
						.param("providerID", "2") //
						.param("barcodeIDs", "2") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, phone))//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr2);
		// 验证返回的数据是否正确
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		assertFalse(o2.getInt(BaseWxModel.WX_ERRCODE) == 0 && o.getString(BaseWxModel.WX_ERRMSG).equals("ok"), "发送信息成功！！！此case应为发送失败！");

	}

	// 向门店老板发送入库价格与采购订单上的价格不符消息
	@Test
	public void testWarehousingToWxSIT() throws Exception {
		Shared.printTestMethodStartInfo();

		// 1.创建采购订单，创建采购订单商品表。
		// 2.审核采购订单。
		// 3.创建入库单，创建入库单商品表
		// 4.审核入库单。采购订单商品表中的价格和入库单商品表的价格一致的话返回的消息就是 'ok'
		Staff s = new Staff();
		s.setPhone(phone);
		s.setOpenid(Shared.openid);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Staff_Update_OpenidAndUnionid, s);
		Staff staff = com.bx.erp.test.BaseStaffTest.retrieve1StaffViaMapper(s);
		Assert.assertTrue(staff != null, "查询staff失败");
		Assert.assertTrue(staff.getOpenid().equals(s.getOpenid()), "更新openid失败！错误码为：" + staffBO.getLastErrorCode());
		// 其它测试用例已经更新过老板的openid为Shared.openid，这时再更新openid为Shared.openid为失败，可以通过查询来判断老板的openid是否已经为Shared.openid
//		assertTrue(staffBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "更新openid失败！错误码为：" + staffBO.getLastErrorCode());

		Warehousing warehousing = DataInput.getWarehousing();
		Warehousing ws = createWS(warehousing);
		// 创建入库商品
		Commodity commodity1 = new Commodity();
		commodity1.setID(1);
		Commodity commodityCache1 = BaseCommodityTest.queryCommodityCache(commodity1.getID());
		Barcodes barcodes = new Barcodes();
		barcodes.setOperatorStaffID(STAFF_ID3);
		barcodes.setBarcode("");
		barcodes.setCommodityID(commodityCache1.getID());
		Barcodes barcode1 = retrieveNBarcodes(barcodes);
		WarehousingCommodity wc = createWC(ws.getID(), commodityCache1, barcode1.getID());
		// 审核入库单前，查询商品的F_NO，检查点判断入库单审核后F_NO是否正确计算
		List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, ws);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
			Assert.assertTrue(false, "查询一个入库单失败或查询的入库单为空，错误码=" + warehousingBO.getLastErrorCode() + "，错误信息=" + warehousingBO.getLastErrorMessage());
		}
		//
		List<BaseModel> whCommBm = bmList.get(1);
		List<WarehousingCommodity> whCommList = new ArrayList<>();
		for (BaseModel bm : whCommBm) {
			WarehousingCommodity whc = (WarehousingCommodity) bm;
			whCommList.add(whc);
		}
		List<Commodity> commList = new ArrayList<>();
		// 入库单有从表
		for (WarehousingCommodity whc : whCommList) {
			Commodity commodityCache = BaseCommodityTest.queryCommodityCache(whc.getCommodityID());
			commList.add(commodityCache);
		}
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.param("commIDs", String.valueOf(wc.getCommodityID()))//
						.param("barcodeIDs", String.valueOf(wc.getBarcodeID()))//
						.param("commNOs", String.valueOf(wc.getNO()))//
						.param("commPrices", String.valueOf(wc.getPageSize()))//
						.param("amounts", String.valueOf(wc.getAmount()))//
						.param("shelfLifes", String.valueOf(wc.getShelfLife()))//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test);

		// openid更新为无效的
		Staff s1 = new Staff();
		s1.setPhone(phone);
		s1.setOpenid("123456");
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		staffBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Staff_Update_OpenidAndUnionid, s1);
		assertTrue(staffBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "更新openid失败！错误码为：" + staffBO.getLastErrorCode());
	}

	public Warehousing createWS(Warehousing warehousing) throws CloneNotSupportedException, InterruptedException {
		warehousing.setProviderID(1);
		String error = warehousing.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "入库单的checkCreate不通过");
		Map<String, Object> paramsForCreate = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wsCreate = (Warehousing) warehousingMapper.create(paramsForCreate); // ...
		//
		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(wsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		assertTrue(wsCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		return (Warehousing) wsCreate.clone();
	}

	public WarehousingCommodity createWC(int warehousingID, Commodity commodity, int barcodeID) throws CloneNotSupportedException, InterruptedException {

		WarehousingCommodity wc = new WarehousingCommodity();
		wc.setWarehousingID(warehousingID);
		wc.setCommodityID(commodity.getID());
		wc.setPackageUnitID(commodity.getPackageUnitID());
		wc.setNO(commodity.getNO());
		wc.setNoSalable(wc.getNO());
		wc.setBarcodeID(barcodeID);
		wc.setPrice(commodity.getPriceRetail());
		wc.setAmount(wc.getNO() * wc.getPrice());
		wc.setShelfLife(commodity.getShelfLife());
		Map<String, Object> paramsForCreate = wc.getCreateParam(BaseBO.INVALID_CASE_ID, wc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wcCreate = (WarehousingCommodity) warehousingCommodityMapper.create(paramsForCreate); // ...
		//
		wc.setIgnoreIDInComparision(true);
		wc.setNoSalable(wc.getNO()); // 创建入库单时，F_NoSalable是根据F_NO来赋值的
		if (wc.compareTo(wcCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		assertTrue(wcCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		return (WarehousingCommodity) wcCreate.clone();
	}

	protected Barcodes retrieveNBarcodes(Barcodes barcodes) {
		Map<String, Object> params = barcodes.getRetrieveNParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = barcodesMapper.retrieveN(params);
		//
		assertTrue(retrieveN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		for (BaseModel bm : retrieveN) {
			Barcodes b = (Barcodes) bm;
			b.setOperatorStaffID(1);
			//
			String error = b.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
			b.setOperatorStaffID(0); // 恢复值
		}
		Barcodes bc = (Barcodes) retrieveN.get(0);
		return bc;
	}
}
