package com.bx.erp.test.purchasing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.model.purchasing.PurchasingOrder.EnumStatusPurchasingOrder;
import com.bx.erp.model.wx.BaseWxModel;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BasePurchasingOrderTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.PurchasingOrderCP;
import com.bx.erp.test.checkPoint.StaffCP;
import com.bx.erp.test.staff.BaseShopTest;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class PurchasingOrderActionTest extends BaseActionTest {
	private static final int PO_STATUS1_ID = 7;
	private static final int PO_STATUS2_ID = 8;
	private static final int PO_STATUS3_ID = 9;
	private static final int PO_STATUS4_ID = 11;
	private static final int PO_STATUS0_ID1 = 5;

	private static final int INVALID_ID2 = -1;

	private static final String KEY_COMMIDS = "commIDs";
	private static final String KEY_BARCODEIDS = "barcodeIDs";
	private static final String KEY_NOS = "NOs";
	private static final String KEY_PRICESUGGESTIONS = "priceSuggestions";
	private static final String KEY_PROVIDERID = "providerID";
	private static final String KEY_SHOPID = "shopID";

	private Staff staff;

	public static class DataInput {
		private static PurchasingOrder purchasingOrderInput = null;

		protected static final PurchasingOrder getPurchasingOrder() throws CloneNotSupportedException {
			purchasingOrderInput = new PurchasingOrder();
			purchasingOrderInput.setProviderID(1);
			purchasingOrderInput.setStaffID(3);
			purchasingOrderInput.setRemark("红红火火恍恍惚惚");
			purchasingOrderInput.setShopID(Shared.DEFAULT_Shop_ID);

			return (PurchasingOrder) purchasingOrderInput.clone();
		}
	}

	@BeforeClass
	public void setup() {

		super.setUp();

		try {
			staff = (Staff) sessionBoss.getAttribute(EnumSession.SESSION_Staff.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 由于商品的测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);

	}

	@AfterClass
	public void tearDown() {

		super.tearDown();

		// 由于商品的测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	public PurchasingOrder createPO() throws Exception {

		PurchasingOrder po = new PurchasingOrder();
		po.setProviderID(2);
		po.setStaffID(staff.getID());
		po.setRemark("123");
		po.setShopID(Shared.DEFAULT_Shop_ID);
		
		Map<String, Object> createParam = po.getCreateParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder bm = (PurchasingOrder) purchasingOrderMapper.create(createParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		po.setIgnoreIDInComparision(true);
		if (po.compareTo(bm) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		List<PurchasingOrderCommodity> purchasingOrderCommList = new ArrayList<PurchasingOrderCommodity>();
		for (int i = 1; i < 4; i++) {
			PurchasingOrderCommodity purchasingOrderCommodity = new PurchasingOrderCommodity();
			// 创建一个正常状态的普通商品
			Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
			Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
			Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
			//
			purchasingOrderCommodity.setBarcodeID(barcode.getID());
			purchasingOrderCommodity.setCommodityID(commCreate.getID());
			purchasingOrderCommodity.setCommodityNO(i);
			purchasingOrderCommodity.setPriceSuggestion(i);
			purchasingOrderCommodity.setPurchasingOrderID(bm.getID());

			Map<String, Object> createPOCParam = purchasingOrderCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, purchasingOrderCommodity);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			PurchasingOrderCommodity purchasingOrderCommodityC = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(createPOCParam);
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createPOCParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
			purchasingOrderCommodity.setIgnoreIDInComparision(true);
			if (purchasingOrderCommodity.compareTo(purchasingOrderCommodityC) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}

			purchasingOrderCommList.add(purchasingOrderCommodityC);
		}
		bm.setListSlave1(purchasingOrderCommList);

		return (PurchasingOrder) bm.clone();
	}


	// 创建一个状态值为0的采购订单，将状态值为0的采购订单放入session中
	// private MvcResult getLoginSession() throws Exception {
	// PurchasingOrder p = new PurchasingOrder();
	// PurchasingOrder bm = createPO();
	// PurchasingOrderCommodity poc = createPOC(bm);
	//
	// MvcResult result = (MvcResult) this.mvc.perform( //
	// get("/purchasingOrder/retrieve1Ex.bx?" + p.getFIELD_NAME_ID() + "=" +
	// poc.getPurchasingOrderID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))
	// //
	// ) //
	// .andExpect(status().isOk()) //
	// .andReturn(); //
	// return result;// ...
	// }

	// private PurchasingOrderCommodity createPOC(PurchasingOrder bm) {
	// PurchasingOrderCommodity poc1 = new PurchasingOrderCommodity();
	// poc1.setCommodityID(6);
	// poc1.setPurchasingOrderID(bm.getID());
	// poc1.setCommodityNO(Math.abs(new Random().nextInt(300)));
	// poc1.setPriceSuggestion(1);
	// poc1.setBarcodeID(1);
	//
	// PurchasingOrderCommodityMapper mapper = (PurchasingOrderCommodityMapper)
	// applicationContext.getBean("purchasingOrderCommodityMapper");
	//
	// Map<String, Object> params = poc1.getCreateParam(BaseBO.INVALID_CASE_ID,
	// poc1);
	// //
	// PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity)
	// mapper.create(params);
	// //
	// poc1.setIgnoreIDInComparision(true);
	// if (poc1.compareTo(pocCreate) != 0) {
	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
	// }
	// Assert.assertTrue(pocCreate != null &&
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError, "创建对象失败");
	// System.out.println("采购单商品创建成功：" + pocCreate);
	// return pocCreate;
	// }

	// 创建一个状态值为1的采购订单，将状态值为1的采购订单放入session中
	// private MvcResult getLoginSession1() throws Exception {
	// PurchasingOrder p = new PurchasingOrder();
	// PurchasingOrder bm = approverPurcchasingOrder(p);
	// PurchasingOrderCommodity poc = createPOC(bm);
	//
	// MvcResult result = (MvcResult) this.mvc.perform( //
	// get("/purchasingOrder/retrieve1Ex.bx?" + p.getFIELD_NAME_ID() + "=" +
	// poc.getPurchasingOrderID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))
	// //
	// ) //
	// .andExpect(status().isOk()) //
	// .andReturn(); //
	// return result;// ...
	// }

	// 获取一个状态值为2的采购订单，将状态值为2的采购订单放入session中
	// private HttpSession getLoginSession2() throws Exception {
	// PurchasingOrder p = new PurchasingOrder();
	//
	// MvcResult result = (MvcResult) this.mvc.perform( //
	// get("/purchasingOrder/retrieve1Ex.bx?" + p.getFIELD_NAME_ID() + "=" +
	// PO_STATUS2_ID) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))
	// //
	// ) //
	// .andExpect(status().isOk()) //
	// .andReturn(); //
	// return result.getRequest().getSession();// ...
	// }

	// 获取一个状态值为3的采购订单，将状态值为3的采购订单放入session中
	// private HttpSession getLoginSession3() throws Exception {
	// PurchasingOrder p = new PurchasingOrder();
	//
	// MvcResult result = (MvcResult) this.mvc.perform( //
	// get("/purchasingOrder/retrieve1Ex.bx?" + p.getFIELD_NAME_ID() + "=" +
	// PO_STATUS3_ID) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))
	// //
	// ) //
	// .andExpect(status().isOk()) //
	// .andReturn(); //
	// return result.getRequest().getSession();
	// }

	@Test
	public void testIndex() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(get("/purchasingOrder.bx").session(sessionBoss)).andExpect(status().isOk());
	}

	@Test
	public void testOrder() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(get("/purchasingOrder/order.bx")).andExpect(status().isOk());
	}

	@Test
	public void testRegion() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(get("/purchasingOrder/region.bx")).andExpect(status().isOk());
	}

	@Test
	public void testProviderProfile() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(get("/purchasingOrder/providerProfile.bx")).andExpect(status().isOk());
	}

	@Test
	public void testRetrieveNByFieldsEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------case1：根据商品名字搜索----------------------");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "不二家棒棒糖")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr);
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<?> list = JsonPath.read(o, "$.purchasingOrderList[*].listCommodity[*].name");
		//
		String string = "不二家棒棒糖";
		Boolean isSame = false;
		for (int i = 0; i < list.size(); i++) {
			String s1 = (String) list.get(i);
			if (s1.contains(string)) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame == true);

		System.out.println("-------------------case2：根据供应商名字搜索----------------------");
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx")//
						.param("sValue", "华北供应商")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		assertNotNull(mr1);
		Shared.checkJSONErrorCode(mr1);

		JSONObject o1 = JSONObject.fromObject(mr1.getResponse().getContentAsString()); //
		List<?> list1 = JsonPath.read(o1, "$.purchasingOrderList[*].providerName");

		string = "华北供应商";
		System.out.println("1=" + list1);
		for (int i = 0; i < list1.size(); i++) {
			String s1 = (String) list1.get(i);
			Assert.assertTrue(s1.contains(string));
		}

		System.out.println("-------------------case3：输入不存在的供应商名字搜索----------------------");
		MvcResult mr3 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx")//
						.param("sValue", "华北供应商" + Shared.BIG_ID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoError);

		System.out.println("-------------------case4：输入不存在的商品名称搜索----------------------");
		MvcResult mr4 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx")//
						.param("sValue", "不二家棒棒糖" + Shared.BIG_ID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoError);

		System.out.println("-------------------case5：没有输入sValue值----------------------");
		MvcResult mr5 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		String json1 = mr5.getResponse().getContentAsString();
		assertTrue(json1.equals(""));

		System.out.println("-------------------case6:员工没有权限----------------------");
//		MvcResult mr6 = mvc.perform(//
//				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
//						.param("sValue", "不二家棒棒糖")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//		).andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoPermission);

		System.out.println("-------------------case7:根据给定时间段（一个星期）查询采购订单--------------");
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		String date1 = "2016/12/05 23:59:59";
		String date2 = "2016/12/12 23:59:59";
		// PurchasingOrder purchasingOrder = new PurchasingOrder();
		MvcResult mr7 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "")//
						.param(PurchasingOrder.field.getFIELD_NAME_date1(), date1)//
						.param(PurchasingOrder.field.getFIELD_NAME_date2(), date2)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss) //
		).andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr7);

		// //结果验证
		PurchasingOrder pOrder7 = new PurchasingOrder();
		pOrder7.setDate1(sdf3.parse(date1));
		pOrder7.setDate2(sdf3.parse(date2));
		JSONObject o7 = JSONObject.fromObject(mr7.getResponse().getContentAsString()); //
		List<?> list7 = JsonPath.read(o7, "$.purchasingOrderList[*].createDatetime");
		for (int i = 0; i < list7.size(); i++) {
			Date createDatetime = sdf1.parse(String.valueOf(list7.get(i)));
			Assert.assertTrue(pOrder7.getDate1().before(createDatetime) && pOrder7.getDate2().after(createDatetime), "case7测试不通过");
		}

		System.out.println("-------------------case8:根据给定时间段（一个月）查询采购订单--------------");
		date1 = "2016/12/01 00:00:00";
		date2 = "2016/12/31 23:59:59";
		MvcResult mr8 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "")//
						.param(PurchasingOrder.field.getFIELD_NAME_date1(), date1)//
						.param(PurchasingOrder.field.getFIELD_NAME_date2(), date2)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss) //
		).andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr8);

		// //结果验证
		PurchasingOrder pOrder8 = new PurchasingOrder();
		pOrder8.setDate1(sdf3.parse(date1));
		pOrder8.setDate2(sdf3.parse(date2));

		JSONObject o8 = JSONObject.fromObject(mr8.getResponse().getContentAsString()); //
		List<?> list8 = JsonPath.read(o8, "$.purchasingOrderList[*].createDatetime");
		for (int i = 0; i < list8.size(); i++) {
			Date createDatetime = sdf1.parse(String.valueOf(list8.get(i)));
			Assert.assertTrue(pOrder8.getDate1().before(createDatetime) && pOrder8.getDate2().after(createDatetime), "case8测试不通过");
		}

		System.out.println("-------------------case9:根据给定时间段（没有采购订单创建的时间段）查询采购订单--------------");
		date1 = "2000/12/01 00:00:00";
		date2 = "2010/12/31 23:59:59";
		MvcResult mr9 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "")//
						.param(PurchasingOrder.field.getFIELD_NAME_date1(), date1)//
						.param(PurchasingOrder.field.getFIELD_NAME_date2(), date2)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss) //
		).andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_NoError);// 结果为空，otherError

		System.out.println("-------------------case10:根据操作人的ID（iStaffID=1）查询采购订单--------------");
		PurchasingOrder pOrder10 = new PurchasingOrder();
		pOrder10.setStaffID(1);
		MvcResult mr10 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "")//
						.param(PurchasingOrder.field.getFIELD_NAME_staffID(), String.valueOf(pOrder10.getStaffID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss) //
		).andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr10);

		JSONObject o10 = JSONObject.fromObject(mr10.getResponse().getContentAsString()); //
		List<Integer> list10 = JsonPath.read(o10, "$.purchasingOrderList[*].staffID");
		for (int i : list10) {
			Assert.assertTrue(pOrder10.getStaffID() == i, "case10测试不通过");
		}

		System.out.println("-------------------case11:根据操作人的ID（iStaffID=-999999,ID不存在，返回0条采购订单）查询采购订单--------------");
		PurchasingOrder pOrder11 = new PurchasingOrder();
		pOrder11.setStaffID(-999999);
		MvcResult mr11 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "")//
						.param(PurchasingOrder.field.getFIELD_NAME_staffID(), String.valueOf(pOrder11.getStaffID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss) //
		).andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_NoError);// 结果为空，otherError

		System.out.println("-------------------case12:根据时间段进行查询采购订单.查询2016/12/6 0:00:00以后的采购订单--------------");
		date1 = "2016/12/05 23:59:59";
		date2 = null;
		MvcResult mr12 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "")//
						.param(PurchasingOrder.field.getFIELD_NAME_date1(), date1)//
						.param(PurchasingOrder.field.getFIELD_NAME_date2(), date2)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss) //
		).andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr12);

		// 结果验证
		PurchasingOrder pOrder12 = new PurchasingOrder();
		pOrder12.setDate1(sdf3.parse(date1));
		JSONObject o12 = JSONObject.fromObject(mr12.getResponse().getContentAsString()); //
		List<?> list12 = JsonPath.read(o12, "$.purchasingOrderList[*].createDatetime");
		for (int i = 0; i < list12.size(); i++) {
			Date createDatetime = sdf1.parse(String.valueOf(list12.get(i)));
			Assert.assertTrue(pOrder12.getDate1().before(createDatetime), "case12测试不通过");
		}

		System.out.println("-------------------case13:根据时间段进行查询采购订单.查询2019/3/13 0:00:00之前的采购订单--------------");
		date1 = null;
		date2 = "2019/3/13 00:00:01";
		MvcResult mr13 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "")//
						.param(PurchasingOrder.field.getFIELD_NAME_date1(), date1)//
						.param(PurchasingOrder.field.getFIELD_NAME_date2(), date2)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss) //
		).andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr13);

		// 结果验证
		PurchasingOrder pOrder13 = new PurchasingOrder();
		pOrder13.setDate2(sdf3.parse(date2));
		JSONObject o13 = JSONObject.fromObject(mr13.getResponse().getContentAsString()); //
		List<?> list13 = JsonPath.read(o13, "$.purchasingOrderList[*].createDatetime");
		for (int i = 0; i < list13.size(); i++) {
			Date createDatetime = sdf1.parse(String.valueOf(list13.get(i)));
			Assert.assertTrue(pOrder13.getDate2().after(createDatetime), "case13测试不通过");
		}

		System.out.println("-------------------case14：根据不存在的采购订单ID进行模糊查询----------------------");
		MvcResult mr14 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "-999999")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr14);
		Shared.checkJSONErrorCode(mr14);

		JSONObject o14 = JSONObject.fromObject(mr14.getResponse().getContentAsString()); //
		List<Integer> list14 = JsonPath.read(o14, "$.purchasingOrderList[*]");
		// 结果验证
		Assert.assertTrue(list14.size() == 0);

		System.out.println("-------------------case15：根据采购订单ID进行模糊查询----------------------");

		PurchasingOrder po15 = DataInput.getPurchasingOrder();

		Map<String, Object> createParams15 = po15.getCreateParam(BaseBO.INVALID_CASE_ID, po15);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder poCreate15 = (PurchasingOrder) purchasingOrderMapper.create(createParams15);

		poCreate15.setIgnoreIDInComparision(true);
		if (poCreate15.compareTo(po15) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		po15.setID(poCreate15.getID());

		MvcResult mr15 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", po15.getID() + "")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr15);
		Shared.checkJSONErrorCode(mr15);

		JSONObject o15 = JSONObject.fromObject(mr15.getResponse().getContentAsString()); //
		List<Integer> List15 = JsonPath.read(o15, "$.purchasingOrderList[*].ID");
		// 结果验证
		boolean flag = false;
		for (int i = 0; i < List15.size(); i++) {
			if (List15.get(i) == poCreate15.getID()) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "查询失败");

		System.out.println("-------------------case16：根据空条件进行模糊查询----------------------");
		MvcResult mr16 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", "")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr16);
		Shared.checkJSONErrorCode(mr16);

		JSONObject o16 = JSONObject.fromObject(mr16.getResponse().getContentAsString()); //
		List<PurchasingOrder> list16 = JsonPath.read(o16, "$.purchasingOrderList[*]");
		// 结果验证
		Assert.assertTrue(list16.size() >= 0);

		System.out.println("-------------------case17：根据全条件进行模糊查询----------------------");

		PurchasingOrder po17 = DataInput.getPurchasingOrder();

		Map<String, Object> createParams17 = po17.getCreateParam(BaseBO.INVALID_CASE_ID, po17);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder poCreate17 = (PurchasingOrder) purchasingOrderMapper.create(createParams17);

		poCreate17.setIgnoreIDInComparision(true);
		if (poCreate17.compareTo(po17) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		po17.setID(poCreate17.getID());

		MvcResult mr17 = mvc.perform(//
				post("/purchasingOrder/retrieveNByFieldsEx.bx") //
						.param("sValue", po17.getID() + "")//
						.param("staffID", po17.getStaffID() + "")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		assertNotNull(mr17);
		Shared.checkJSONErrorCode(mr17);

		JSONObject o17 = JSONObject.fromObject(mr17.getResponse().getContentAsString()); //
		List<Integer> List17 = JsonPath.read(o17, "$.purchasingOrderList[*].ID");
		// 结果验证
		flag = false;
		for (int i = 0; i < List17.size(); i++) {
			if (List17.get(i) == poCreate17.getID()) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag, "查询失败");
	}

	// @Test
	// public void testApproveListEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("------------------------------Case1:审核1个的采购订单------------------------");
	// PurchasingOrder bm = createPO();
	//
	// MvcResult mr = (MvcResult) mvc.perform( //
	// get("/purchasingOrder/approveListEx.bx?POListID=" + bm.getID()) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("-----------------------------------Case2:审核多个的采购订单------------------------------");
	// PurchasingOrder bm1 = createPO();
	// PurchasingOrder bm2 = createPO();
	//
	// MvcResult mr2 = (MvcResult) mvc.perform( //
	// get("/purchasingOrder/approveListEx.bx?POListID=" + bm1.getID() + "," +
	// bm2.getID()) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	// Shared.checkJSONErrorCode(mr2);
	//
	// System.out.println("-----------------------------------Case3:审核一个不可审核的采购订单------------------------------");
	// String msg1 = "采购订单" + 7 + "审核失败，因为它已经审核过了。<br />";
	//
	// MvcResult mr3 = mvc.perform( //
	// get("/purchasingOrder/approveListEx.bx?POListID=7") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr3, msg1, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case4:审核多个不可审核的采购订单------------------------------");
	// String msg2 = "采购订单" + 8 + "审核失败，因为它已经审核过了。<br />";
	//
	// MvcResult mr4 = mvc.perform( //
	// get("/purchasingOrder/approveListEx.bx?POListID=7,8") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr4, msg1 + msg2, "返回的结果不一致");
	//
	// System.out.println("------------------------------Case5:审核0个采购订单-----------------------");
	// MvcResult mr5 = mvc.perform( //
	// get("/purchasingOrder/approveListEx.bx") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// String json3 = mr5.getResponse().getContentAsString();
	// assertTrue(json3.equals(""));
	//
	// System.out.println("-----------------------case6：审核一个不存在的采购单---------------");
	// String msg = "采购订单" + INVALID_ID + "审核失败，因为它已经审核过了。<br />";
	//
	// MvcResult mr6 = (MvcResult) mvc.perform( //
	// get("/purchasingOrder/approveListEx.bx?POListID=" + INVALID_ID) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr6, msg, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case7:审核多个采购订单，其中一个不可审核------------------------------");
	// PurchasingOrder bm4 = createPO();
	// MvcResult mr7 = mvc.perform( //
	// get("/purchasingOrder/approveListEx.bx?POListID=7" + "," + bm4.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONMsg(mr7, msg1, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:没有权限进行审核------------------------------");
	// PurchasingOrder bm5 = createPO();
	// String msg3 = "你没有权限审核采购订单。<br />";
	//
	// MvcResult mr8 = mvc.perform( //
	// get("/purchasingOrder/approveListEx.bx?POListID=" + bm5.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfManager)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONMsg(mr8, msg3, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_PartSuccess);
	// }

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo(); // 调用StackTraceElement类检测方法开始的运行时间;

		Shared.caseLog("case1:正常创建");
		PurchasingOrder p = new PurchasingOrder();
		// 创建正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreateA = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commCreateB = BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, sessionBoss, mapBO, Shared.DBName_Test);

		// 为商品添加条形码
		Barcodes barcodeA = BaseBarcodesTest.retrieveNBarcodes(commCreateA.getID(), Shared.DBName_Test);
		Barcodes barcodeB = BaseBarcodesTest.retrieveNBarcodes(commCreateB.getID(), Shared.DBName_Test);

		String commIDs = commCreateA.getID() + "," + commCreateB.getID();
		String barcodeIDs = barcodeA.getID() + "," + barcodeB.getID();
		
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_NOS, "4,5") //
						.param(KEY_PRICESUGGESTIONS, "11.1,1.1") //
//						.param(KEY_COMMPURCHASINGUNIT, "桶") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		PurchasingOrderCP.verifyCreate(mr, p, purchasingOrderBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2：因缺少commIDs，所以服务器会把commIDs处理为-1，数据为空");
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "001")//
						.param(KEY_NOS, "4")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Assert.assertTrue("".equals(mr2.getResponse().getContentAsString()), "action返回的结果和预期的不一样");
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3：因传入参数commIDs不为数字，为汉字，转int类型时出错。数据为空");
		MvcResult mr3 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "002")//
						.param(KEY_COMMIDS, "回滚")//
						.param(KEY_NOS, "4")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Assert.assertTrue("".equals(mr3.getResponse().getContentAsString()), "action返回的结果和预期的不一样");
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:传入的商品ID为不存在");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "003")//
						.param(KEY_COMMIDS, "-5")//
						.param(KEY_NOS, "4")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:没有传入商品的数量，数据为空");
		MvcResult mr5 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "004")//
						.param(KEY_COMMIDS, "10")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Assert.assertTrue("".equals(mr5.getResponse().getContentAsString()), "action返回的结果和预期的不一样");
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:传入的商品数量为汉字，数据为空");
		MvcResult mr6 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "005")//
						.param(KEY_COMMIDS, "10")//
						.param(KEY_NOS, "啊哈")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Assert.assertTrue("".equals(mr6.getResponse().getContentAsString()), "action返回的结果和预期的不一样");
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:传入的商品数量为-1，数据为空");
		MvcResult mr7 = mvc.perform( //
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "006") //
						.param(KEY_COMMIDS, "10") //
						.param(KEY_NOS, "-1") //
						.param(KEY_PRICESUGGESTIONS, "11.1") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2") //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Assert.assertTrue("".equals(mr7.getResponse().getContentAsString()), "action返回的结果和预期的不一样");
	}

	@Test
	public void testCreateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8:不传入的商品价格，数据为空");
		MvcResult mr8 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "007")//
						.param(KEY_COMMIDS, "10")//
						.param(KEY_NOS, "1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Assert.assertTrue("".equals(mr8.getResponse().getContentAsString()), "action返回的结果和预期的不一样");
	}

	@Test
	public void testCreateEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9:传入的商品ID个数超出限定数，数据为空");
		List<Integer> MumList = new ArrayList<Integer>();
		for (int i = 0; i < 110; i++) {
			MumList.add(i);
		}

		MvcResult mr9 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "008")//
						.param(KEY_COMMIDS, MumList + "")//
						.param(KEY_NOS, "1")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Assert.assertTrue("".equals(mr9.getResponse().getContentAsString()), "action返回的结果和预期的不一样");
	}

	@Test
	public void testCreateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:传入的商品数量个数超出限定数，数据为空");
		List<Integer> MumList = new ArrayList<Integer>();
		for (int i = 0; i < 110; i++) {
			MumList.add(i);
		}
		MvcResult mr10 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "009")//
						.param(KEY_COMMIDS, "13")//
						.param(KEY_NOS, MumList + "")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Assert.assertTrue("".equals(mr10.getResponse().getContentAsString()), "action返回的结果和预期的不一样");
	}

	@Test
	public void testCreateEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:传入的供应商ID为不存在的");
		MvcResult mr11 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, "10")//
						.param(KEY_NOS, "11")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, String.valueOf(Shared.BIG_ID))//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "14")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr11, "该供应商不存在，请重新选择供应商", "错误信息不正确！");
	}

	@Test
	public void testCreateEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12:传入商品ID为组合商品的ID,数据为空");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "011")//
						.param(KEY_COMMIDS, "45")//
						.param(KEY_NOS, "4")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:传入商品ID为多包装商品的ID,数据为空");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "011")//
						.param(KEY_COMMIDS, "51")//
						.param(KEY_NOS, "4")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		PurchasingOrder p = new PurchasingOrder();
		Shared.caseLog("Case14:传入商品ID为普通商品的ID");
		MvcResult mr14 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "011")//
						.param(KEY_COMMIDS, "1")//
						.param(KEY_NOS, "4")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr14);
		PurchasingOrderCP.verifyCreate(mr14, p, purchasingOrderBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15:传入的商品ID为已删除的普通商品ID,数据为空");
		// 创建一个普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 删除一个普通商品
		BaseCommodityTest.deleteCommodityViaAction(commCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);

		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "011")//
						.param(KEY_COMMIDS, String.valueOf(commCreate.getID()))//
						.param(KEY_NOS, "4")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testCreateEx16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case16:传入的商品ID为已删除的组合商品ID，数据为空");
		// 创建一个组合商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		String subCommodityInfo = JSONObject.fromObject(commodity).toString();
		commodity.setSubCommodityInfo(subCommodityInfo);
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 删除一个组合商品
		BaseCommodityTest.deleteCommodityViaAction(commCreate2, Shared.DBName_Test, mvc, sessionBoss, mapBO);

		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "011")//
						.param(KEY_COMMIDS, String.valueOf(commCreate2.getID()))//
						.param(KEY_NOS, "4")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testCreateEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case17:传入的商品ID为已删除的多包装商品ID，数据为空");
		// 创建一个多包装商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setDefaultValueToCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		commodity.setRefCommodityID(1);
		commodity.setRefCommodityMultiple(2);
		commodity.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity.setMultiPackagingInfo("123456789" + "," + "222" + System.currentTimeMillis() % 1000000 + ",333" + System.currentTimeMillis() % 1000000 + ";1,200,3,6;1,5,10;1,5,10;0,0,0,0;0,0,0;");//
		Commodity commCreate3 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 删除一个多包装商品
		BaseCommodityTest.deleteCommodityViaAction(commCreate3, Shared.DBName_Test, mvc, sessionBoss, mapBO);

		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "011")//
						.param(KEY_COMMIDS, String.valueOf(commCreate3.getID()))//
						.param(KEY_NOS, "4")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testCreateEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case18:传入的商品ID为服务商品");
		// 创建一个多包装商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		commodity.setDefaultValueToCreate(BaseBO.CASE_Commodity_CreateService);
		commodity.setPurchasingUnit("");
		commodity.setShelfLife(0);
		Commodity commCreate18 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "011")//
						.param(KEY_COMMIDS, String.valueOf(commCreate18.getID()))//
						.param(KEY_NOS, "4")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx19() throws Exception {
		Shared.printTestMethodStartInfo(); // 调用StackTraceElement类检测方法开始的运行时间;

		Shared.caseLog("case19:正常创建采购订单，但是发送消息给老板失败");
		PurchasingOrder p = new PurchasingOrder();
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		String commIDs = commCreate.getID() + ",";
		String barcodeIDs = barcode.getID() + ",";
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_NOS, "4") //
						.param(KEY_PRICESUGGESTIONS, "11.1") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		PurchasingOrderCP.verifyCreate(mr, p, purchasingOrderBO, Shared.DBName_Test);
		JSONObject object = JSONObject.fromObject(mr.getResponse().getContentAsString());
		String msg = (String) object.get(BaseAction.KEY_HTMLTable_Parameter_msg);
		// 发送微信消息失败
		// Assert.assertTrue(msg.contains("发送微信消息失败!"), "预期发送微信消息失败，但是发送成功");
		// 发送微信消息成功，需要设置白名单和更改DB中店长的openid，才能测试发送微信消息成功
		Assert.assertTrue(!msg.contains("发送微信消息失败!"), "预期发送微信消息成功，但是发送失败");
		System.out.println(msg);
	}

	@Test
	public void testCreateEx20() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case20:一张采购单商品中有两个相同的商品");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "003")//
						.param(KEY_COMMIDS, "2,2")//
						.param(KEY_NOS, "4,4")//
						.param(KEY_PRICESUGGESTIONS, "11.1,11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, "2,2")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action返回的错误信息不正确");
	}

	@Test
	public void testCreateEx21() throws Exception {
		Shared.printTestMethodStartInfo(); // 调用StackTraceElement类检测方法开始的运行时间;

		Shared.caseLog("case21:创建没有采购商品的采购订单");
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue(json.length() == 0, "CASE21测试失败！期望的是返回null");
	}

	@Test
	public void testCreateEx22() throws Exception {
		Shared.printTestMethodStartInfo(); // 调用StackTraceElement类检测方法开始的运行时间;

		Shared.caseLog("case22:创建的采购订单商品单价大于" + FieldFormat.MAX_OneCommodityPrice + "，创建失败");
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		// 为商品添加条形码
		String commIDs = commCreate.getID() + ",";
		String barcodeIDs = barcodes.getID() + ",";
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.param(KEY_NOS, "4") //
						.param(KEY_PRICESUGGESTIONS, String.valueOf(FieldFormat.MAX_OneCommodityPrice + 0.01)) //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx23() throws Exception {
		Shared.printTestMethodStartInfo(); // 调用StackTraceElement类检测方法开始的运行时间;

		Shared.caseLog("case23:创建的采购订单商品数量大于" + FieldFormat.MAX_OneCommodityNO + "，创建失败");
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		// 为商品添加条形码
		String commIDs = commCreate.getID() + ",";
		String barcodeIDs = barcodes.getID() + ",";
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.param(KEY_NOS, String.valueOf(FieldFormat.MAX_OneCommodityNO + 1)) //
						.param(KEY_PRICESUGGESTIONS, "10.1") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}
	
	@Test
	public void testCreateEx24() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case24:传入的门店ID为不存在的");
		MvcResult mr11 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, "10")//
						.param(KEY_NOS, "11")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(Shared.BIG_ID)) //
						.param(KEY_BARCODEIDS, "14")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr11, "该门店不存在，请重新选择门店", "错误信息不正确！");
	}
	
	@Test
	public void testCreateEx25() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case25:传入的门店ID为虚拟总部");
		MvcResult mr11 = mvc.perform(//
				post("/purchasingOrder/createEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, "10")//
						.param(KEY_NOS, "11")//
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.param(KEY_PROVIDERID, "2")//
						.param(KEY_SHOPID, String.valueOf(BaseAction.VirtualShopID)) //
						.param(KEY_BARCODEIDS, "14")//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr11, PurchasingOrder.FIELD_ERROR_VirtualShopID, "错误信息不正确！");
	}
	
	@Test
	public void testCreateEx26() throws Exception {
		Shared.printTestMethodStartInfo(); // 调用StackTraceElement类检测方法开始的运行时间;

		Shared.caseLog("case26:门店B的店长创建门店A的采购，创建失败，不能跨店创建");
		// 创建正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreateA = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commCreateB = BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, sessionBoss, mapBO, Shared.DBName_Test);

		// 为商品添加条形码
		Barcodes barcodeA = BaseBarcodesTest.retrieveNBarcodes(commCreateA.getID(), Shared.DBName_Test);
		Barcodes barcodeB = BaseBarcodesTest.retrieveNBarcodes(commCreateB.getID(), Shared.DBName_Test);

		String commIDs = commCreateA.getID() + "," + commCreateB.getID();
		String barcodeIDs = barcodeA.getID() + "," + barcodeB.getID();
		// 创建门店B的店长账号
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("店长" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_NOS, "4,5") //
						.param(KEY_PRICESUGGESTIONS, "11.1,1.1") //
//						.param(KEY_COMMPURCHASINGUNIT, "桶") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.session(sessionNewShopBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testCreateEx27() throws Exception {
		Shared.printTestMethodStartInfo(); // 调用StackTraceElement类检测方法开始的运行时间;

		Shared.caseLog("case27:门店B(虚拟总部)的店长创建门店A的采购，创建成功");
		PurchasingOrder p = new PurchasingOrder();
		// 创建正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreateA = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commCreateB = BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, sessionBoss, mapBO, Shared.DBName_Test);

		// 为商品添加条形码
		Barcodes barcodeA = BaseBarcodesTest.retrieveNBarcodes(commCreateA.getID(), Shared.DBName_Test);
		Barcodes barcodeB = BaseBarcodesTest.retrieveNBarcodes(commCreateB.getID(), Shared.DBName_Test);

		String commIDs = commCreateA.getID() + "," + commCreateB.getID();
		String barcodeIDs = barcodeA.getID() + "," + barcodeB.getID();
		// 为虚拟总部新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("店长" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_NOS, "4,5") //
						.param(KEY_PRICESUGGESTIONS, "11.1,1.1") //
//						.param(KEY_COMMPURCHASINGUNIT, "桶") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.session(sessionNewShopBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		PurchasingOrderCP.verifyCreate(mr, p, purchasingOrderBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: 正常删除");
		// sessionBoss = sessionBoss;
		PurchasingOrder po = createPO();

		MvcResult mr = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + po.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr);
		PurchasingOrderCP.verifyDelete(po, purchasingOrderBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: 删除已删除的采购单");
		PurchasingOrder po = createPO();
		MvcResult mr2 = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + po.getID())//
						.session(sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr2);
		PurchasingOrderCP.verifyDelete(po, purchasingOrderBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: 删除已审核的采购单");
		MvcResult mr3 = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS1_ID)//
						.session(sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case4: 删除部分采购订的采购单");
		MvcResult mr4 = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS2_ID)//
						.session(sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case5: 删除全部采购订的采购单");
		MvcResult mr5 = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS3_ID)//
						.session(sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case6: 删除不存在的采购单");
		MvcResult mr6 = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + Shared.BIG_ID)//
						.session(sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case7: 没有权限");
//		PurchasingOrder bm1 = createPO();
//		MvcResult mr7 = mvc.perform(//
//				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + bm1.getID())//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();//
//
//		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testDeleteEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case8: 有依赖，不能删除");

		PurchasingOrder bm2 = createPO();

		bm2.setApproverID(1);
		Map<String, Object> params2 = bm2.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, bm2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder approvePo = (PurchasingOrder) purchasingOrderMapper.approve(params2);
		Assert.assertTrue(approvePo != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		MvcResult mr8 = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + bm2.getID())//
						.session(sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("采购订单被删除后，它的从表也被删除了");
		// sessionBoss = sessionBoss;
		PurchasingOrder po = createPO();
		// 根据商品和条形码创建一个采购订单商品
		PurchasingOrderCommodity pOrderComm = new PurchasingOrderCommodity();
		pOrderComm.setCommodityID(1);
		pOrderComm.setPurchasingOrderID(po.getID());
		pOrderComm.setCommodityNO(Math.abs(new Random().nextInt(300)));
		pOrderComm.setPriceSuggestion(1);
		pOrderComm.setBarcodeID(1);
		//
		Map<String, Object> createpOrderCommparams = pOrderComm.getCreateParam(BaseBO.INVALID_CASE_ID, pOrderComm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(createpOrderCommparams);
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(createpOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		pOrderComm.setIgnoreIDInComparision(true);
		if (pOrderComm.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		MvcResult mr = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + po.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr);
		PurchasingOrderCP.verifyDelete(po, purchasingOrderBO, Shared.DBName_Test);
		// 根据采购订单查询采购商品
		PurchasingOrderCommodity pOrderCommRN = new PurchasingOrderCommodity();
		pOrderCommRN.setPurchasingOrderID(po.getID());
		Map<String, Object> paramsPOrderCommRN = pOrderComm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pOrderCommRN);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> pOrderCommLs = purchasingOrderCommodityMapper.retrieveN(paramsPOrderCommRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsPOrderCommRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		Assert.assertTrue(pOrderCommLs.size() == 0, "采购订单的从表没有被删除！");
	}

	@Test
	public void testRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(//
				get("/purchasingOrder/retrieveN.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk());
	}

	@Test
	public void testRetrieveNEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1：查询状态为0的采购单");
		// PurchasingOrder p = new PurchasingOrder();
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<Integer> list = JsonPath.read(o, "$.objectList[*].status");
		for (int i : list) {
			Assert.assertTrue(i == EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
		}
	}

	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------------------Case2：查询状态为1的采购单-------------------------------");
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);

		JSONObject o1 = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		List<Integer> list1 = JsonPath.read(o1, "$.objectList[*].status");
		for (int i : list1) {
			Assert.assertTrue(i == EnumStatusPurchasingOrder.ESPO_Approved.getIndex());
		}
	}

	@Test
	public void testRetrieveNEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case3：查询状态不为4的采购单-------------------------------");
		MvcResult mr3 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr3);
		//
		JSONObject o2 = JSONObject.fromObject(mr3.getResponse().getContentAsString()); //
		List<Integer> list2 = JsonPath.read(o2, "$.objectList[*].status");
		for (int i : list2) {
			Assert.assertTrue(i < EnumStatusPurchasingOrder.ESPO_Deleted.getIndex() && i > -1);
		}
	}

	@Test
	public void testRetrieveNEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case4：查询状态未定义-------------------------------");
		MvcResult mr4 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(Shared.BIG_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_WrongFormatForInputField);
		String json = mr4.getResponse().getContentAsString();
		JSONObject jsonObject = JSONObject.fromObject(json);
		String err = JsonPath.read(jsonObject, "$.msg");
		Assert.assertTrue("采购订单的状态只能在0到4之间".equals(err));
	}

	@Test
	public void testRetrieveNEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case5：查询状态为2的采购单-------------------------------");
		MvcResult mr9 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr9);
		//
		JSONObject o4 = JSONObject.fromObject(mr9.getResponse().getContentAsString()); //
		List<Integer> list5 = JsonPath.read(o4, "$.objectList[*].status");
		for (int i : list5) {
			Assert.assertTrue(i == EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex());
		}
	}

	@Test
	public void testRetrieveNEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case6：查询状态为3的采购单-------------------------------");
		MvcResult mr10 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr10);
		//
		JSONObject o5 = JSONObject.fromObject(mr10.getResponse().getContentAsString()); //
		List<Integer> list6 = JsonPath.read(o5, "$.objectList[*].status");
		for (int i : list6) {
			Assert.assertTrue(i == EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex());
		}
	}

	@Test
	public void testRetrieveNEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case7：查询状态为4的采购单-------------------------------");
		MvcResult mr11 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Deleted.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testRetrieveNEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case8：根据商品名称进行模糊查询-------------------------------");
		MvcResult mr12 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), "不二家棒棒糖")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr12);

		JSONObject o6 = JSONObject.fromObject(mr12.getResponse().getContentAsString()); //
		List<?> list7 = JsonPath.read(o6, "$.objectList[*].listCommodity[*].name");
		String string = "不二家棒棒糖";
		Boolean isSame = false;
		for (int i = 0; i < list7.size(); i++) {
			String s1 = (String) list7.get(i);
			if (s1.contains(string)) {
				isSame = true;
				break;
			}
		}
		Assert.assertTrue(isSame == true);
	}

	@Test
	public void testRetrieveNEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case9： 根据供应商名称进行模糊查询------------------------------");
		MvcResult mr13 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), "默认供应商")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr13);

		JSONObject o7 = JSONObject.fromObject(mr13.getResponse().getContentAsString()); //
		List<?> list8 = JsonPath.read(o7, "$.objectList[*].providerName");
		String string = "默认供应商";
		for (int i = 0; i < list8.size(); i++) {
			String s1 = (String) list8.get(i);
			Assert.assertTrue(s1.contains(string));
		}
	}

	@Test
	public void testRetrieveNEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case10： 根据10位采购订单单号进行模糊查询）-------------------------------");
		PurchasingOrder purchasingOrder = createPO();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "CG" + sdf.format(new Date()).substring(0, 8);

		MvcResult mr14 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), SN)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr14);

		JSONObject o8 = JSONObject.fromObject(mr14.getResponse().getContentAsString()); //
		List<Integer> list9 = JsonPath.read(o8, "$.objectList[*].ID");
		boolean isTrue = false;
		for (int i = 0; i < list9.size(); i++) {
			int id = (int) list9.get(i);
			if (id == purchasingOrder.getID()) {
				isTrue = true;
			}
		}
		Assert.assertTrue(isTrue == true);
	}

	@Test
	public void testRetrieveNEx11() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case11： 根据采购订单单号传的值进行模糊查询-------------------------------");
		MvcResult mr15 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), "CG20190604")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr15);

		JSONObject o9 = JSONObject.fromObject(mr15.getResponse().getContentAsString()); //
		List<?> list10 = JsonPath.read(o9, "$.objectList[*]");
		Assert.assertTrue(list10.size() > 0);
	}

	@Test
	public void testRetrieveNEx12() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case12： 根据不存在的值进行模糊查询-------------------------------");
		MvcResult mr16 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), String.valueOf(Shared.BIG_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr16);

		JSONObject o10 = JSONObject.fromObject(mr16.getResponse().getContentAsString()); //
		List<?> list11 = JsonPath.read(o10, "$.objectList[*]");
		Assert.assertTrue(list11.size() == 0);
	}

	@Test
	public void testRetrieveNEx13() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case13： 根据时间段进行查询（一天）-------------------------------");
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		String date1 = "2016/12/06 00:00:00";
		String date2 = "2016/12/06 23:59:59";

		MvcResult mr17 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_date1(), date1)//
						.param(PurchasingOrder.field.getFIELD_NAME_date2(), date2)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr17);

		// 结果验证
		PurchasingOrder pOrder = new PurchasingOrder();
		pOrder.setDate1(sdf3.parse(date1));
		pOrder.setDate2(sdf3.parse(date2));
		JSONObject o11 = JSONObject.fromObject(mr17.getResponse().getContentAsString()); //
		List<?> list12 = JsonPath.read(o11, "$.objectList[*].createDatetime");
		for (int i = 0; i < list12.size(); i++) {
			Date createDatetime = sdf1.parse(String.valueOf(list12.get(i)));
			Assert.assertTrue(pOrder.getDate1().before(createDatetime) && pOrder.getDate2().after(createDatetime), "case17测试不通过");
		}
	}

	@Test
	public void testRetrieveNEx14() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case14： 根据时间段进行查询（一星期）-------------------------------");
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		String date1 = "2016/12/06 00:00:00";
		String date2 = "2016/12/13 23:59:59";

		MvcResult mr18 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_date1(), date1)//
						.param(PurchasingOrder.field.getFIELD_NAME_date2(), date2)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr18);

		// 结果验证
		PurchasingOrder pOrder8 = new PurchasingOrder();
		pOrder8.setDate1(sdf3.parse(date1));
		pOrder8.setDate2(sdf3.parse(date2));
		JSONObject o12 = JSONObject.fromObject(mr18.getResponse().getContentAsString()); //
		List<?> list13 = JsonPath.read(o12, "$.objectList[*].createDatetime");
		for (int i = 0; i < list13.size(); i++) {
			Date createDatetime = sdf1.parse(String.valueOf(list13.get(i)));
			Assert.assertTrue(pOrder8.getDate1().before(createDatetime) && pOrder8.getDate2().after(createDatetime), "case18测试不通过");
		}
	}

	@Test
	public void testRetrieveNEx15() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case15： 根据时间段进行查询（无记录时间段）-------------------------------");
		String date1 = "1998/12/06 00:00:00";
		String date2 = "1999/12/13 23:59:59";

		MvcResult mr19 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_date1(), date1)//
						.param(PurchasingOrder.field.getFIELD_NAME_date2(), date2)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr19);

		JSONObject o13 = JSONObject.fromObject(mr19.getResponse().getContentAsString()); //
		List<?> list14 = JsonPath.read(o13, "$.objectList[*]");
		Assert.assertTrue(list14.size() == 0);
	}

	@Test
	public void testRetrieveNEx16() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case16： 根据时间段进行查询（有开始时间，无结束时间）-------------------------------");
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		String date1 = "2016/12/07 00:00:00";
		MvcResult mr20 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_date1(), date1)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr20);

		// 结果验证
		PurchasingOrder pOrder12 = new PurchasingOrder();
		pOrder12.setDate1(sdf3.parse(date1));
		JSONObject o14 = JSONObject.fromObject(mr20.getResponse().getContentAsString()); //
		List<?> list15 = JsonPath.read(o14, "$.objectList[*].createDatetime");
		for (int i = 0; i < list15.size(); i++) {
			Date createDatetime = sdf1.parse(String.valueOf(list15.get(i)));
			Assert.assertTrue(pOrder12.getDate1().before(createDatetime), "case20测试不通过");
		}
	}

	@Test
	public void testRetrieveNEx17() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------------------Case17： 根据时间段进行查询（有结束时间，无开始时间）-------------------------------");
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		String date2 = "2016/12/07 00:00:00";
		MvcResult mr21 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_date2(), date2)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr21);

		// 结果验证
		PurchasingOrder pOrder13 = new PurchasingOrder();
		pOrder13.setDate2(sdf3.parse(date2));
		JSONObject o15 = JSONObject.fromObject(mr21.getResponse().getContentAsString()); //
		List<?> list16 = JsonPath.read(o15, "$.objectList[*].createDatetime");
		for (int i = 0; i < list16.size(); i++) {
			Date createDatetime = sdf1.parse(String.valueOf(list16.get(i)));
			Assert.assertTrue(pOrder13.getDate2().after(createDatetime), "case21测试不通过");
		}
	}

	@Test
	public void testRetrieveNEx18() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case18： 根据操作人的ID查询采购订单-------------------------------");
		MvcResult mr22 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_staffID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr22);

		JSONObject o16 = JSONObject.fromObject(mr22.getResponse().getContentAsString()); //
		List<Integer> list17 = JsonPath.read(o16, "$.objectList[*].staffID");
		for (int i : list17) {
			Assert.assertTrue(i == 1, "case22测试不通过");
		}
	}

	@Test
	public void testRetrieveNEx19() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case19： 根据不存在的操作人的ID查询采购订单-------------------------------");
		MvcResult mr23 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_staffID(), String.valueOf(Shared.BIG_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr23);

		JSONObject o17 = JSONObject.fromObject(mr23.getResponse().getContentAsString()); //
		List<?> list18 = JsonPath.read(o17, "$.objectList[*]");
		Assert.assertTrue(list18.size() == 0);
	}

	@Test
	public void testRetrieveNEx20() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case20： 根据空条件进行查询-------------------------------");
		MvcResult mr24 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr24);

		JSONObject o18 = JSONObject.fromObject(mr24.getResponse().getContentAsString()); //
		List<?> list19 = JsonPath.read(o18, "$.objectList[*]");
		Assert.assertTrue(list19.size() > 0);
	}

	@Test
	public void testRetrieveNEx21() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case21： 根据全条件进行查询-------------------------------");
		MvcResult mr25 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2)).param(PurchasingOrder.field.getFIELD_NAME_staffID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), "供应商")//
						.param(PurchasingOrder.field.getFIELD_NAME_date1(), "1998/08/18 00:00:00")//
						.param(PurchasingOrder.field.getFIELD_NAME_date2(), "2019/05/13 00:00:00")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr25);

		JSONObject o19 = JSONObject.fromObject(mr25.getResponse().getContentAsString()); //
		List<Integer> list20 = JsonPath.read(o19, "$.objectList[*].ID");
		for (int i : list20) {
			Assert.assertTrue(i == 1, "case25测试不通过");
		}
	}

	@Test
	public void testRetrieveNEx22() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case22： 根据小于10位的采购订单单号进行模糊查询-------------------------------");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "CG" + sdf.format(new Date()).substring(0, 7);

		MvcResult mr14 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), SN)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr14);

		JSONObject o8 = JSONObject.fromObject(mr14.getResponse().getContentAsString()); //
		List<Integer> list9 = JsonPath.read(o8, "$.objectList[*].ID");
		Assert.assertTrue(list9.size() == 0, "CASE22测试失败，期望的是查询不到数据");
	}

	@Test
	public void testRetrieveNEx23() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case23： 根据大于20位的采购订单单号进行模糊查询-------------------------------");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "CG" + sdf.format(new Date()).substring(0, 8) + "123451234512345";

		MvcResult mr14 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), SN)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr14);

		JSONObject o8 = JSONObject.fromObject(mr14.getResponse().getContentAsString()); //
		List<Integer> list9 = JsonPath.read(o8, "$.objectList[*].ID");
		Assert.assertTrue(list9.size() == 0, "CASE23测试失败，期望的是查询不到数据");
	}

	@Test
	public void testRetrieveNEx24() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------------------Case24： 根据20位的采购订单单号进行模糊查询-------------------------------");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "CG" + sdf.format(new Date()).substring(0, 8) + "1234512345";

		MvcResult mr14 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), SN)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr14);

		JSONObject o8 = JSONObject.fromObject(mr14.getResponse().getContentAsString()); //
		List<Integer> list9 = JsonPath.read(o8, "$.objectList[*].ID");
		Assert.assertTrue(list9.size() >= 0, "CASE24测试失败");
	}

	@Test
	public void testRetrieveNEx25() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("------------Case25： ：传入queryKeyword包含_的特殊字符进行模糊搜索 ------------");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("退货可口可乐1111_5)4（" + System.currentTimeMillis());
		String barcode = "987623" + System.currentTimeMillis();
		commodity.setMultiPackagingInfo(barcode + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";" + commodity.getPriceWholesale() + ";"
				+ commodity.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseBarcodesTest.retrieveNBarcodes(commodity1.getID(), Shared.DBName_Test);
		// 创建采购单和采购单从表
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, String.valueOf(commodity1.getID()))//
						.param(KEY_NOS, "4") //
						.param(KEY_PRICESUGGESTIONS, "11.1") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, String.valueOf(barcodes1.getID())) //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验正
		Shared.checkJSONErrorCode(mr);

		// 商品名称模糊查询
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(INVALID_ID2))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), "_")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验正
		Shared.checkJSONErrorCode(mr1);

		// 解析出商品名称作对比
		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<String> listName = JsonPath.read(o, "$.objectList[*].listCommodity[*].name");
		for (String result : listName) {
			Assert.assertTrue(result.contains("_"), "查询商品名称没有包含_特殊字符");
		}

	}

	@Test
	public void testRetrieveNEx26() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case26：查询状态为0的采购单");
		PurchasingOrder p = new PurchasingOrder();
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<BaseModel> bmList = p.parseN(o.getString(BaseAction.KEY_ObjectList));
		Assert.assertTrue(bmList.size() > 0, "数据异常！");
		for (int i = 1; i < bmList.size(); i++) {
			Assert.assertTrue(((PurchasingOrder) bmList.get(i - 1)).getID() > ((PurchasingOrder) bmList.get(i)).getID(), "数据返回错误（非降序）");
		}

	}

	@Test
	public void testApproveEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = sessionBoss;

		String url = "/purchasingOrder/approveEx.bx";
		check1(url);
		check2(url);
		check3(url);
		check4(url);
		check5(url);
		check6(url);
		check7(url);
		check8(url);
		check9(url);
		check10(url);
		check11(url);
		check12(url);
		check13(url);
		check14(url);
		check15(url);
		check16(url);
		check17(url);
		check18(url);
		check19(url);
		check20(url);
		check21(url);
	}

	@Test
	public void testApproveEx22() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = sessionBoss;

		Shared.caseLog("Case22:审核时修改，把入库单商品单价修改成大于" + FieldFormat.MAX_OneCommodityPrice + "，审核失败。");
		PurchasingOrder purchasingOrder = createPO();
		//
		String commIDs = "";
		String barcodeIDs = "";
		for (int i = 0; i < purchasingOrder.getListSlave1().size(); i++) {
			PurchasingOrderCommodity pOrderComm = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(i);
			commIDs = commIDs + pOrderComm.getCommodityID() + ",";
			barcodeIDs = barcodeIDs + pOrderComm.getBarcodeID() + ",";
		}
		MvcResult mr = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, "20,30,40")//
						.param(KEY_PRICESUGGESTIONS, "20," + (FieldFormat.MAX_OneCommodityPrice + 0.01) + ",30")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testApproveEx23() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = sessionBoss;

		Shared.caseLog("Case23:审核时修改，把入库单商品数量修改成大于" + FieldFormat.MAX_OneCommodityNO + "，审核失败。");
		PurchasingOrder purchasingOrder = createPO();
		//
		PurchasingOrderCommodity pOrderComm = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(0);
		String commIDs = pOrderComm.getCommodityID() + ",";
		String barcodeIDs = pOrderComm.getBarcodeID() + ",";
		MvcResult mr = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, String.valueOf(FieldFormat.MAX_OneCommodityNO + 1))//
						.param(KEY_PRICESUGGESTIONS, "30")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testApproveEx24() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = sessionBoss;

		Shared.caseLog("Case24:不修改直接审核");
		PurchasingOrder purchasingOrder = createPO();
		//
		MvcResult mr = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_NO.getIndex()))//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testApproveEx25() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = sessionBoss;

		String url = "/purchasingOrder/approveEx.bx";
		check22(url);
	}

	@Test
	public void testApproveEx26() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE26: 审核时修改，把入库单商品传能部分创建成功的商品，审核失败");
		// 目前在修改主表前会checkCreate检查从表，这样理论上不会出现部分成功（EC_PartSuccess）的情况，自动化测试测试不出（要DB错误才有可能）
	}

	@Test
	public void testApproveEx27() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case27:修改采购订单的供应商后审核");
		// 创建两个个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaAction(commodity2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcode2 = BaseBarcodesTest.retrieveNBarcodes(commCreate2.getID(), Shared.DBName_Test);
		//
		String commIDs = commCreate.getID() + "," + commCreate2.getID();
		String barcodeIDs = barcode.getID() + "," + barcode2.getID();
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setRemark("010");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(KEY_PROVIDERID, "1") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.param(KEY_NOS, "4,5") //
						.param(KEY_PRICESUGGESTIONS, "11.1,11.2") //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 检查点
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, Shared.DBName_Test);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = (PurchasingOrder) new PurchasingOrder().parse1(o.getString(BaseAction.KEY_Object));
		po.setIsModified(EnumBoolean.EB_Yes.getIndex());
		po.setProviderID(2);
		//
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(po.getIsModified()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, "8,10")//
						.param(KEY_PRICESUGGESTIONS, "11.1,12")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// 检查点
		PurchasingOrderCP.verifyApprove(po, purchasingOrderBO, providerCommodityBO, Shared.DBName_Test);
	}

	@Test
	public void testApproveEx28() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case28:修改采购订单的商品后审核");
		// 创建两个个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		//
		String commIDs = commCreate.getID() + ",";
		String barcodeIDs = barcode.getID() + ",";
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setRemark("010");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(KEY_PROVIDERID, "1") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.param(KEY_NOS, "4") //
						.param(KEY_PRICESUGGESTIONS, "11.1") //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 检查点
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, Shared.DBName_Test);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = (PurchasingOrder) new PurchasingOrder().parse1(o.getString(BaseAction.KEY_Object));
		po.setIsModified(EnumBoolean.EB_Yes.getIndex());
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaAction(commodity2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcode2 = BaseBarcodesTest.retrieveNBarcodes(commCreate2.getID(), Shared.DBName_Test);
		//
		commIDs = commCreate2.getID() + ",";
		barcodeIDs = barcode2.getID() + ",";
		//
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(po.getIsModified()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, "10")//
						.param(KEY_PRICESUGGESTIONS, "12")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// 检查点
		PurchasingOrderCP.verifyApprove(po, purchasingOrderBO, providerCommodityBO, Shared.DBName_Test);
	}

	@Test
	public void testApproveEx29() throws Exception { 
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE29: 门店A的店长创建采购，门店B的店长进行审核，审核失败，不能跨店审核");
		// 创建两个个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		//
		String commIDs = commCreate.getID() + ",";
		String barcodeIDs = barcode.getID() + ",";
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setRemark("010");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(KEY_PROVIDERID, "1") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.param(KEY_NOS, "4") //
						.param(KEY_PRICESUGGESTIONS, "11.1") //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 检查点
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, Shared.DBName_Test);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = (PurchasingOrder) new PurchasingOrder().parse1(o.getString(BaseAction.KEY_Object));
		po.setIsModified(EnumBoolean.EB_Yes.getIndex());
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaAction(commodity2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcode2 = BaseBarcodesTest.retrieveNBarcodes(commCreate2.getID(), Shared.DBName_Test);
		//
		commIDs = commCreate2.getID() + ",";
		barcodeIDs = barcode2.getID() + ",";		
		// 审核采购单
		// 创建门店B的店长账号
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("店长" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);

		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(po.getIsModified()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, "10")//
						.param(KEY_PRICESUGGESTIONS, "12")//
						.session((MockHttpSession) sessionNewShopBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testApproveEx30() throws Exception { 
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE30: 门店A的店长创建采购，门店B(虚拟总部)的店长进行审核，审核成功，虚拟总部店长可以作为多个门店的店长");
		// 创建两个个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		//
		String commIDs = commCreate.getID() + ",";
		String barcodeIDs = barcode.getID() + ",";
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setRemark("010");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(KEY_PROVIDERID, "1") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.param(KEY_NOS, "4") //
						.param(KEY_PRICESUGGESTIONS, "11.1") //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 检查点
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, Shared.DBName_Test);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = (PurchasingOrder) new PurchasingOrder().parse1(o.getString(BaseAction.KEY_Object));
		po.setIsModified(EnumBoolean.EB_Yes.getIndex());
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaAction(commodity2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcode2 = BaseBarcodesTest.retrieveNBarcodes(commCreate2.getID(), Shared.DBName_Test);
		//
		commIDs = commCreate2.getID() + ",";
		barcodeIDs = barcode2.getID() + ",";		
		// 审核采购单
		// 为虚拟总部新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("店长" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);

		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(po.getIsModified()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, "10")//
						.param(KEY_PRICESUGGESTIONS, "12")//
						.session((MockHttpSession) sessionNewShopBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
	}
	
	@Test
	public void testRetrieve1Ex_1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		System.out.println("-------------------case1：根据ID搜索----------------------");
		// PurchasingOrder p = new PurchasingOrder();
		MvcResult mr = mvc.perform( //
				get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS0_ID1) //
						.session(sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		PurchasingOrder po = new PurchasingOrder();
		// PurchasingOrder po = (PurchasingOrder)
		// mr.getRequest().getSession().getAttribute(EnumSession.SESSION_PurchasingOrder.getName());
		// assertTrue(po.getID() == PO_STATUS0_ID1);
		// 结果验证：检验从表数据
		JSONObject o11 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder poOrder = (PurchasingOrder) po.parse1(o11.getString(BaseAction.KEY_Object));
		List<?> pocList = poOrder.getListSlave1();
		PurchasingOrderCommodity poc = null;
		for (Object object : pocList) {
			poc = (PurchasingOrderCommodity) object;
			assertTrue(poOrder.getID() == poc.getPurchasingOrderID());
		}
	}

	@Test
	public void testRetrieve1Ex_2() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------case2：根据不存在的ID搜索----------------------");
		try {
			mvc.perform( //
					get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + Shared.BIG_ID) //
							.session(sessionBoss) //
							.contentType(MediaType.APPLICATION_JSON) //
			) //
					.andExpect(status().isOk()) //
					.andDo(print()) //
					.andReturn();//
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("Request processing failed; nested exception is java.lang.NullPointerException"));
		}
	}

	@Test
	public void testRetrieve1Ex_3() throws Exception {

		System.out.println("-------------------case3：根据负数的ID搜索----------------------");
		try {
			mvc.perform( //
					get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + INVALID_ID2) //
							.session(sessionBoss) //
							.contentType(MediaType.APPLICATION_JSON) //
			) //
					.andExpect(status().isOk()) //
					.andDo(print()) //
					.andReturn();//
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("Request processing failed; nested exception is java.lang.NullPointerException"));
		}
	}

	@Test
	public void testRetrieve1Ex_4() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------case4：根据已删除的采购订单的ID搜索----------------------");
		try {
			mvc.perform( //
					get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS4_ID) //
							.session(sessionBoss) //
							.contentType(MediaType.APPLICATION_JSON) //
			) //
					.andExpect(status().isOk()) //
					.andDo(print()) //
					.andReturn();//
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().equals("Request processing failed; nested exception is java.lang.NullPointerException"));
		}
	}

	@Test
	public void testRetrieve1Ex_5() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------case5：没有权限的员工进行搜索----------------------");
//		MvcResult mr3 = mvc.perform( //
//				get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS1_ID) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//						.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print()) //
//				.andReturn();
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testRetrieve1Ex_6() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------case6：刚创建的采购订单id搜索，入库数量为null----------------------");
		MvcResult mr4 = mvc.perform( //
				get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS0_ID1) //
						.session(sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		JSONObject o2 = JSONObject.fromObject(mr4.getResponse().getContentAsString()); //
		List<Integer> warehousingNOList2 = JsonPath.read(o2, "$.object.listSlave1[*].warehousingNO");
		for (int i : warehousingNOList2) {
			Assert.assertTrue(i == 0);
		}
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoError);
		// 结果验证：检验从表数据
		JSONObject o14 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		PurchasingOrder poOrder4 = (PurchasingOrder) new PurchasingOrder().parse1(o14.getString(BaseAction.KEY_Object));
		List<?> pocList4 = poOrder4.getListSlave1();
		PurchasingOrderCommodity poc4 = null;
		for (Object object : pocList4) {
			poc4 = (PurchasingOrderCommodity) object;
			assertTrue(poOrder4.getID() == poc4.getPurchasingOrderID());
		}
	}

	@Test
	public void testRetrieve1Ex_7() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------case7：刚创建的入库单入库数量为0,对应的采购订单id搜索，入库数量为0----------------------");
		MvcResult mr5 = mvc.perform( //
				get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS2_ID) //
						.session(sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		JSONObject o1 = JSONObject.fromObject(mr5.getResponse().getContentAsString()); //
		List<Integer> warehousingNOList1 = JsonPath.read(o1, "$.object.listSlave1[*].warehousingNO");
		for (int i : warehousingNOList1) {
			Assert.assertTrue(i == 0);
		}
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoError);
		// 结果验证：检验从表数据
		JSONObject o15 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		PurchasingOrder poOrder5 = (PurchasingOrder) new PurchasingOrder().parse1(o15.getString(BaseAction.KEY_Object));
		List<?> pocList5 = poOrder5.getListSlave1();
		PurchasingOrderCommodity poc5 = null;
		for (Object object : pocList5) {
			poc5 = (PurchasingOrderCommodity) object;
			assertTrue(poOrder5.getID() == poc5.getPurchasingOrderID());
		}
	}

	@Test
	public void testRetrieve1Ex_8() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------case8：第二次入库数量为10，入库数量为10----------------------");
		Warehousing warehousingGet8 = new Warehousing();
		warehousingGet8.setWarehouseID(1);
		warehousingGet8.setStaffID(1);
		warehousingGet8.setProviderID(1);
		warehousingGet8.setShopID(Shared.DEFAULT_Shop_ID);
		warehousingGet8.setPurchasingOrderID(12);
		Warehousing warehousing = BaseWarehousingTest.createViaMapper(warehousingGet8);
		// 创建一个入库单商品表，入库数量为10
		WarehousingCommodity createWarehousingCommodity = BaseWarehousingTest.createWarehousingCommodity(10, warehousing);
		warehousing.setApproverID(1);
		BaseWarehousingTest.approveViaMapper(warehousing);

		MvcResult mr6 = mvc.perform( //
				get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + warehousing.getPurchasingOrderID()) //
						.session(sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		JSONObject o = JSONObject.fromObject(mr6.getResponse().getContentAsString()); //
		List<Integer> warehousingNOList = JsonPath.read(o, "$.object.listSlave1[*].warehousingNO");
		for (int i : warehousingNOList) {
			Assert.assertTrue(i == 10);
		}
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoError);
		// 结果验证：检验从表数据
		JSONObject o16 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		PurchasingOrder poOrder6 = (PurchasingOrder) new PurchasingOrder().parse1(o16.getString(BaseAction.KEY_Object));
		List<?> pocList6 = poOrder6.getListSlave1();
		PurchasingOrderCommodity poc6 = null;
		for (Object object : pocList6) {
			poc6 = (PurchasingOrderCommodity) object;
			assertTrue(poOrder6.getID() == poc6.getPurchasingOrderID());
		}
		
		// 删除测试数据避免测试出错
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(createWarehousingCommodity);
	}

	@Test
	public void testRetrieve1Ex_9() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("-------------------case9： 将所有采购数量入库完成，查询对应的入库数量应当为所有的采购数量----------------------");
		Warehousing warehousingGet9 = new Warehousing();
		warehousingGet9.setWarehouseID(1);
		warehousingGet9.setStaffID(1);
		warehousingGet9.setProviderID(1);
		warehousingGet9.setShopID(Shared.DEFAULT_Shop_ID);
		warehousingGet9.setPurchasingOrderID(12);
		Warehousing warehousing1 = BaseWarehousingTest.createViaMapper(warehousingGet9);
		// 创建一个入库单商品表，入库数量为290
		WarehousingCommodity createWarehousingCommodity1 = BaseWarehousingTest.createWarehousingCommodity(290, warehousing1);
		warehousing1.setApproverID(1);
		BaseWarehousingTest.approveViaMapper(warehousing1);

		MvcResult mr7 = mvc.perform( //
				get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + warehousing1.getPurchasingOrderID()) //
						.session(sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		JSONObject o4 = JSONObject.fromObject(mr7.getResponse().getContentAsString()); //
		List<Integer> warehousingNOList4 = JsonPath.read(o4, "$.object.listSlave1[*].warehousingNO");
		System.out.println("warehousingNOList4=" + warehousingNOList4);
		for (int i : warehousingNOList4) {
			Assert.assertTrue(i % 290 == 0); // 多次运行后数目是290的倍数
		}
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoError);
		// 结果验证：检验从表数据
		JSONObject o17 = JSONObject.fromObject(mr7.getResponse().getContentAsString());
		PurchasingOrder poOrder7 = (PurchasingOrder) new PurchasingOrder().parse1(o17.getString(BaseAction.KEY_Object));
		List<?> pocList7 = poOrder7.getListSlave1();
		PurchasingOrderCommodity poc7 = null;
		for (Object object : pocList7) {
			poc7 = (PurchasingOrderCommodity) object;
			assertTrue(poOrder7.getID() == poc7.getPurchasingOrderID());
		}

		// 删除测试数据避免测试出错
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(createWarehousingCommodity1);
	}

	@Test
	public void testUpdateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// sessionBoss = sessionBoss;

		String url = "/purchasingOrder/updateEx.bx";
		check1(url);
		check2(url);
		check3(url);
		check4(url);
		check5(url);
		check6(url);
		check7(url);
		check8(url);
		check9(url);
		check10(url);
		check11(url);
		check12(url);
		check13(url);
		check14(url);
		check15(url);
		check16(url);
		check17(url);
		check18(url);
		check21(url);
	}

	@Test
	public void testUpdateEx22() throws Exception {
		Shared.printTestMethodStartInfo();

//		// sessionBoss = sessionBoss;

		String url = "/purchasingOrder/updateEx.bx";
		check22(url);
	}
	
	@Test
	public void testUpdateEx23() throws Exception {
		Shared.printTestMethodStartInfo();

//		// sessionBoss = sessionBoss;

		String url = "/purchasingOrder/updateEx.bx";
		check23(url);
	}
	
	@Test
	public void testUpdateEx24() throws Exception {
		Shared.printTestMethodStartInfo();

//		// sessionBoss = sessionBoss;

		String url = "/purchasingOrder/updateEx.bx";
		check24(url);
	}

	protected void check1(String url) throws Exception {
		// PurchasingOrder bm = new PurchasingOrder();
		Shared.caseLog("Case1:传递状态为0的采购订单进行修改。");
		PurchasingOrder purchasingOrder = createPO();
		PurchasingOrderCommodity pc = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(0);
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		//
		pc.setCommodityID(commCreate.getID());
		pc.setCommodityName(commCreate.getName());
		pc.setBarcodeID(barcode.getID());
		String commIDs = "";
		String barcodeIDs = "";
		for (int i = 0; i < purchasingOrder.getListSlave1().size(); i++) {
			PurchasingOrderCommodity pOrderComm = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(i);
			commIDs = commIDs + pOrderComm.getCommodityID() + ",";
			barcodeIDs = barcodeIDs + pOrderComm.getBarcodeID() + ",";
		}
		PurchasingOrderCommodity poc = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(0);
		poc.setCommodityNO(20);
		poc.setPriceSuggestion(12.1);
		PurchasingOrderCommodity poc1 = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(1);
		poc1.setCommodityNO(30);
		poc1.setPriceSuggestion(14.5);
		PurchasingOrderCommodity poc2 = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(2);
		poc2.setCommodityNO(40);
		poc2.setPriceSuggestion(16.7);
		MvcResult mr1 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, "20,30,40")//
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		//
		String request = "/purchasingOrder/updateEx.bx";
		if (url.equals(request)) {
			PurchasingOrderCP.verifyUpdate(mr1, purchasingOrder, purchasingOrderBO, Shared.DBName_Test);
		} else {
			PurchasingOrderCP.verifyApprove(purchasingOrder, purchasingOrderBO, providerCommodityBO, Shared.DBName_Test);
		}
	}

	protected void check2(String url) throws Exception {
		Shared.caseLog("Case2:传递状态为1的采购订单进行修改。");
		PurchasingOrder purchasingOrder2 = createPO();
		BasePurchasingOrderTest.approverPurcchasingOrder(purchasingOrder2);

		MvcResult mr2 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder2.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder2.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder2.getRemark())//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2,3")//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.param(KEY_NOS, "20,30,40")//
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	protected void check3(String url) throws Exception {
		Shared.caseLog("Case3:传递状态为2的采购订单进行修改。");
		PurchasingOrder purchasingOrder2 = createPO();
		BasePurchasingOrderTest.approverPurcchasingOrder(purchasingOrder2);
		//
		purchasingOrder2.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex());
		Map<String, Object> updataStatusParams3 = purchasingOrder2.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, purchasingOrder2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo3 = (PurchasingOrder) purchasingOrderMapper.updateStatus(updataStatusParams3);

		Assert.assertTrue(updatePo3.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex()
				&& EnumErrorCode.values()[Integer.parseInt(updataStatusParams3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改对象失败");

		MvcResult mr3 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(updatePo3.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(updatePo3.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), updatePo3.getRemark())//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2,3")//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.param(KEY_NOS, "20,30,40")//
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	protected void check4(String url) throws Exception {
		Shared.caseLog("Case4:传递状态为3的采购订单进行修改。");
		PurchasingOrder purchasingOrder2 = createPO();
		BasePurchasingOrderTest.approverPurcchasingOrder(purchasingOrder2);
		//
		purchasingOrder2.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex());
		Map<String, Object> updataStatusParams4 = purchasingOrder2.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, purchasingOrder2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo4 = (PurchasingOrder) purchasingOrderMapper.updateStatus(updataStatusParams4);

		Assert.assertTrue(updatePo4.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex()
				&& EnumErrorCode.values()[Integer.parseInt(updataStatusParams4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改对象失败");

		MvcResult mr4 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(updatePo4.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(updatePo4.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), updatePo4.getRemark())//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2,3")//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.param(KEY_NOS, "20,30,40")//
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	protected void check5(String url) throws Exception {
		Shared.caseLog("Case5:传递状态为4的采购订单进行修改。");
		PurchasingOrder purchasingOrder5 = createPO();
		Map<String, Object> deleteParam = purchasingOrder5.getDeleteParam(BaseBO.INVALID_CASE_ID, purchasingOrder5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败");

		MvcResult mr5 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder5.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder5.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder5.getRemark())//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2,3")//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.param(KEY_NOS, "20,30,40")//
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	protected void check6(String url) throws Exception {
		Shared.caseLog("Case6:没相关权限的人员进行审核。");
//		PurchasingOrder purchasingOrder6 = createPO();
//		MvcResult mr6 = mvc.perform( //
//				post(url) //
//						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder6.getID())) //
//						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "1")//
//						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
//						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
//						.param(KEY_COMMIDS, "1,2,3")//
//						.param(KEY_BARCODEIDS, "1,3,5")//
//						.param(KEY_NOS, "20,30,40")//
//						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//						.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print()) //
//				.andReturn(); //
//		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoPermission);
	}

	protected void check7(String url) throws Exception {
		Shared.caseLog("Case7:.传递的商品ID不存在进行采购订单修改");
		// sessionBoss = sessionBoss;
		PurchasingOrder purchasingOrder7 = createPO();
		MvcResult mr7 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder7.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2," + Shared.BIG_ID)//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.param(KEY_NOS, "20,30,40")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoSuchData);
	}

	protected void check8(String url) throws Exception {
		Shared.caseLog("Case8:.传递的条形码ID不存在进行采购订单修改");
		PurchasingOrder purchasingOrder8 = createPO();
		MvcResult mr8 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder8.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2,3")//
						.param(KEY_BARCODEIDS, "1," + String.valueOf(Shared.BIG_ID) + ",3")//
						.param(KEY_NOS, "20,30,40")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_NoSuchData);
		Shared.checkJSONMsg(mr8, "该条形码不存在", "错误信息不正确");
	}

	protected void check9(String url) throws Exception {
		Shared.caseLog("Case9:.传递不存在的采购订单ID进行审核。");
		MvcResult mr9 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID)) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2,3")//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.param(KEY_NOS, "20,30,40")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	protected void check10(String url) throws Exception {
		Shared.caseLog("Case10:.传入的参数格式维度不一致");
		PurchasingOrder purchasingOrder10 = createPO();
		MvcResult mr10 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder10.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2,3")//
						.param(KEY_BARCODEIDS, "1,2,3,4")//
						.param(KEY_NOS, "20,30,40")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Assert.assertTrue("".equals(mr10.getResponse().getContentAsString()), "action返回的错误码不正确");
	}

	protected void check11(String url) throws Exception {
		Shared.caseLog("Case11.传入的参数格式带有中文");
		PurchasingOrder purchasingOrder10 = createPO();
		MvcResult mr11 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder10.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2,3")//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.param(KEY_NOS, "20,30,测试")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Assert.assertTrue("".equals(mr11.getResponse().getContentAsString()), "action返回的错误码不正确");
	}

	protected void check12(String url) throws Exception {
		Shared.caseLog("Case12.传入的参数格式为空.");
		PurchasingOrder purchasingOrder10 = createPO();
		MvcResult mr12 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder10.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2,3")//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Assert.assertTrue("".equals(mr12.getResponse().getContentAsString()), "action返回的错误码不正确");
	}

	protected void check13(String url) throws Exception {
		Shared.caseLog("Case13.要修改的商品ID为多包装商品");
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commoditySimple = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setRefCommodityID(commoditySimple.getID());
		commodity.setRefCommodityMultiple(2);
		commodity.setType(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity.setMultiPackagingInfo("123456789" + "," + "222" + System.currentTimeMillis() % 1000000 + ",333" + System.currentTimeMillis() % 1000000 + ";1,200,3,6;1,5,10;1,5,10;0,0,0,0;0,0,0;");//
		Commodity commodityMultiPackaging = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateMultiPackaging);
		PurchasingOrder purchasingOrder13 = createPO();
		//
		MvcResult mr13 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder13.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2," + String.valueOf(commodityMultiPackaging.getID()))//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.param(KEY_NOS, "20,30,1")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr13, "不能采购单品以外的商品（商品" + commodityMultiPackaging.getName() + "的类型不是单品）", "错误信息不正确");

		BaseCommodityTest.deleteCommodityViaAction(commodityMultiPackaging, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commoditySimple, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	protected void check14(String url) throws Exception {
		Shared.caseLog("Case14.要修改的商品ID为组合商品");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		String subCommodityInfo = JSONObject.fromObject(commodity).toString();
		commodity.setSubCommodityInfo(subCommodityInfo);
		Commodity commodityCombination = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		PurchasingOrder purchasingOrder14 = createPO();
		//
		MvcResult mr = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder14.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2," + String.valueOf(commodityCombination.getID()))//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.param(KEY_NOS, "20,30,1")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, "不能采购单品以外的商品（商品" + commodityCombination.getName() + "的类型不是单品）", "错误信息不正确");

		BaseCommodityTest.deleteCommodityViaAction(commodityCombination, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	protected void check15(String url) throws Exception {
		Shared.caseLog("Case15.要修改的商品ID为已删除的");
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		BaseCommodityTest.deleteCommodityViaAction(commodity, Shared.DBName_Test, mvc, sessionBoss, mapBO);

		PurchasingOrder purchasingOrder15 = createPO();
		//
		MvcResult mr15 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder15.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "1")//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, String.valueOf(commodity.getID()) + ",1,2")//
						.param(KEY_BARCODEIDS, "3,2,1")//
						.param(KEY_NOS, "20,30,1")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr15, EnumErrorCode.EC_NoSuchData);
		Shared.checkJSONMsg(mr15, "不能采购一个不存在的商品", "错误信息不正确");
	}

	protected void check16(String url) throws Exception {
		Shared.caseLog("Case16.修改的供应商ID不存在。");
		PurchasingOrder purchasingOrder16 = createPO();
		//
		MvcResult mr16 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder16.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(Shared.BIG_ID))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1,2,3")//
						.param(KEY_BARCODEIDS, "1,3,5")//
						.param(KEY_NOS, "20,30,1")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr16, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr16, "供应商不存在，请重新选择供应商", "错误信息不正确");
	}

	protected void check17(String url) throws Exception {
		Shared.caseLog("Case17.采购单中有两个重复的商品。");
		PurchasingOrder purchasingOrder17 = createPO();
		//
		String commIDs = "";
		String barcodeIDs = "";
		int j;
		for (int i = 0; i < purchasingOrder17.getListSlave1().size(); i++) {
			j = i;
			if (j == 2) {
				j = 1;
			}
			PurchasingOrderCommodity pOrderComm = (PurchasingOrderCommodity) purchasingOrder17.getListSlave1().get(j);
			commIDs = commIDs + pOrderComm.getCommodityID() + ",";
			barcodeIDs = barcodeIDs + pOrderComm.getBarcodeID() + ",";
		}
		MvcResult mr = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder17.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder17.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, "20,30,1")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action返回的结果和预期的不一样");
	}

	protected void check18(String url) throws Exception {
		Shared.caseLog("Case18:采购订单没有采购商品");
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		String commIDs = commCreate.getID() + ",";
		String barcodeIDs = barcode.getID() + ",";
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_NOS, "4") //
						.param(KEY_PRICESUGGESTIONS, "11.1") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = (PurchasingOrder) new PurchasingOrder().parse1(o.getString(BaseAction.KEY_Object));
		//
		MvcResult mr1 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "")//
						.param(KEY_BARCODEIDS, "")//
						.param(KEY_NOS, "")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Assert.assertTrue("".equals(mr1.getResponse().getContentAsString()), "action返回的结果和预期的不一样");
	}

	protected void check19(String url) throws Exception {
		Shared.caseLog("Case19:修改采购订单后审核");
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		String commIDs = commCreate.getID() + ",";
		String barcodeIDs = barcode.getID() + ",";
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setRemark("010");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_NOS, "4") //
						.param(KEY_PRICESUGGESTIONS, "11.1") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 检查点
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, Shared.DBName_Test);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = (PurchasingOrder) new PurchasingOrder().parse1(o.getString(BaseAction.KEY_Object));
		//
		MvcResult mr1 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, "8")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// 检查点
		PurchasingOrderCP.verifyApprove(po, purchasingOrderBO, providerCommodityBO, Shared.DBName_Test);
	}

	protected void check20(String url) throws Exception {
		Shared.caseLog("Case20:修改采购订单后审核,没有传采购商品，审核失败");
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		String commIDs = commCreate.getID() + ",";
		String barcodeIDs = barcode.getID() + ",";
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setRemark("010");
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_NOS, "4") //
						.param(KEY_PRICESUGGESTIONS, "11.1") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 检查点
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, Shared.DBName_Test);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = (PurchasingOrder) new PurchasingOrder().parse1(o.getString(BaseAction.KEY_Object));
		//
		MvcResult mr1 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "")//
						.param(KEY_BARCODEIDS, "")//
						.param(KEY_NOS, "")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		String o20 = mr1.getResponse().getContentAsString();
		assertTrue(o20.length() == 0, "CASE20测试失败！返回的结果不是期望的");
	}

	protected void check21(String url) throws Exception {
		Shared.caseLog("Case21:不存在的采购订单");
		//
		MvcResult mr1 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID)) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), "2")//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, "1")//
						.param(KEY_BARCODEIDS, "1")//
						.param(KEY_NOS, "10")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	protected void check22(String url) throws Exception {
		Shared.caseLog("Case22:修改采购订单,传的商品ID和条形码ID不对应");
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		String commIDs = commCreate.getID() + ",";
		String barcodeIDs = barcode.getID() + ",";
		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		purchasingOrder.setRemark("010");
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs) //
						.param(KEY_NOS, "4") //
						.param(KEY_PRICESUGGESTIONS, "11.1") //
						.param(KEY_PROVIDERID, "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 检查点
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, Shared.DBName_Test);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = (PurchasingOrder) new PurchasingOrder().parse1(o.getString(BaseAction.KEY_Object));
		//
		MvcResult mr1 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "备注")//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, "1")//
						.param(KEY_NOS, "8")//
						.session((MockHttpSession) sessionBoss) //
						.param(KEY_PRICESUGGESTIONS, "11.1")//
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, "条形码与商品实际条形码不对应", "错误信息不正确");
	}
	
	protected void check23(String url) throws Exception {
		// PurchasingOrder bm = new PurchasingOrder();
		Shared.caseLog("Case23:门店A的店长创建采购，门店B的店长进行修改，修改失败，不能跨店修改。");
		PurchasingOrder purchasingOrder = createPO();
		PurchasingOrderCommodity pc = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(0);
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		//
		pc.setCommodityID(commCreate.getID());
		pc.setCommodityName(commCreate.getName());
		pc.setBarcodeID(barcode.getID());
		String commIDs = "";
		String barcodeIDs = "";
		for (int i = 0; i < purchasingOrder.getListSlave1().size(); i++) {
			PurchasingOrderCommodity pOrderComm = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(i);
			commIDs = commIDs + pOrderComm.getCommodityID() + ",";
			barcodeIDs = barcodeIDs + pOrderComm.getBarcodeID() + ",";
		}
		PurchasingOrderCommodity poc = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(0);
		poc.setCommodityNO(20);
		poc.setPriceSuggestion(12.1);
		PurchasingOrderCommodity poc1 = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(1);
		poc1.setCommodityNO(30);
		poc1.setPriceSuggestion(14.5);
		PurchasingOrderCommodity poc2 = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(2);
		poc2.setCommodityNO(40);
		poc2.setPriceSuggestion(16.7);
		//
		// 创建门店B的店长账号
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("店长" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);

		MvcResult mr1 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, "20,30,40")//
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.session((MockHttpSession) sessionNewShopBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	protected void check24(String url) throws Exception {
		// PurchasingOrder bm = new PurchasingOrder();
		Shared.caseLog("Case24:门店A的店长创建采购，门店B(虚拟总部)的店长进行修改，修改成功。");
		PurchasingOrder purchasingOrder = createPO();
		PurchasingOrderCommodity pc = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(0);
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 为商品添加条形码
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreate.getID(), Shared.DBName_Test);
		//
		pc.setCommodityID(commCreate.getID());
		pc.setCommodityName(commCreate.getName());
		pc.setBarcodeID(barcode.getID());
		String commIDs = "";
		String barcodeIDs = "";
		for (int i = 0; i < purchasingOrder.getListSlave1().size(); i++) {
			PurchasingOrderCommodity pOrderComm = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(i);
			commIDs = commIDs + pOrderComm.getCommodityID() + ",";
			barcodeIDs = barcodeIDs + pOrderComm.getBarcodeID() + ",";
		}
		PurchasingOrderCommodity poc = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(0);
		poc.setCommodityNO(20);
		poc.setPriceSuggestion(12.1);
		PurchasingOrderCommodity poc1 = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(1);
		poc1.setCommodityNO(30);
		poc1.setPriceSuggestion(14.5);
		PurchasingOrderCommodity poc2 = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(2);
		poc2.setCommodityNO(40);
		poc2.setPriceSuggestion(16.7);
		//
		// 为该虚拟总部新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("店长" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);

		MvcResult mr1 = mvc.perform( //
				post(url) //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(KEY_COMMIDS, commIDs)//
						.param(KEY_BARCODEIDS, barcodeIDs)//
						.param(KEY_NOS, "20,30,40")//
						.param(KEY_PRICESUGGESTIONS, "12.1,14.5,16.7")//
						.session((MockHttpSession) sessionNewShopBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
	}
	// @Test
	// public void testDeleteListEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("------------------------------Case1:删除1个采购订单------------------------");
	// PurchasingOrder bm = createPO();
	//
	// MvcResult mr = mvc.perform( //
	// get("/purchasingOrder/deleteListEx.bx?POListID=" + bm.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("-----------------------------------Case2:删除多个采购订单------------------------------");
	// PurchasingOrder bm2 = createPO();
	// PurchasingOrder bm3 = createPO();
	//
	// MvcResult mr2 = mvc.perform( //
	// get("/purchasingOrder/deleteListEx.bx?POListID=" + bm2.getID() + "," +
	// bm3.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr2);
	//
	// System.out.println("-----------------------------------Case3:删除一个不可删除的采购订单------------------------------");
	// String msg1 = "采购订单" + 7 + "删除失败，因为它已经审核过。<br />";
	//
	// MvcResult mr3 = mvc.perform( //
	// get("/purchasingOrder/deleteListEx.bx?POListID=7") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr3, msg1, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case4:删除多个不可删除的采购订单------------------------------");
	// String msg2 = "采购订单" + 8 + "删除失败，因为它已经审核过。<br />";
	//
	// MvcResult mr4 = mvc.perform( //
	// get("/purchasingOrder/deleteListEx.bx?POListID=7,8") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr4, msg1 + msg2, "返回的结果不一致");
	//
	// System.out.println("------------------------------Case5:删除0个采购订单-----------------------");
	// MvcResult mr5 = mvc.perform( //
	// get("/purchasingOrder/deleteListEx.bx") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// String json3 = mr5.getResponse().getContentAsString();
	// assertTrue(json3.equals(""));
	//
	// System.out.println("-----------------------case6：POListID的ID在采购订单中不存在。---------------");
	// String msg = "采购订单" + Shared.BIG_ID + "删除失败，因为它已经审核过。<br />";
	//
	// MvcResult mr6 = mvc.perform( //
	// get("/purchasingOrder/deleteListEx.bx?POListID=" + INVALID_ID) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	// Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr6, msg, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case7:删除多个采购订单，其中一个不可删除------------------------------");
	// PurchasingOrder bm4 = createPO();
	// MvcResult mr7 = mvc.perform( //
	// get("/purchasingOrder/deleteListEx.bx?POListID=7" + "," + bm4.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONMsg(mr7, msg1, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:没有权限进行删除------------------------------");
	// PurchasingOrder bm5 = createPO();
	// String msg3 = "你没有权限删除采购订单。<br />";
	//
	// MvcResult mr8 = mvc.perform( //
	// get("/purchasingOrder/deleteListEx.bx?POListID=" + bm5.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfManager)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONMsg(mr8, msg3, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_PartSuccess);
	// }

	// @Test
	public void testRetrieveNCommodityEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------case1:commIDs为1，2------------------------------");
		MvcResult mr = mvc.perform(//
				get("/purchasingOrder/retrieveNCommodityEx.bx?commIDs=1,2")//
						.session(sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("-----------------------case2:没有传入commIDs------------------------------");
		MvcResult mr1 = mvc.perform(//
				get("/purchasingOrder/retrieveNCommodityEx.bx?commIDs=")//
						.session(sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		String json = mr1.getResponse().getContentAsString();
		assertTrue(json.equals(""));

		System.out.println("-----------------------case3:传入commIDs为英文------------------------------");
		MvcResult mr2 = mvc.perform(//
				get("/purchasingOrder/retrieveNCommodityEx.bx?commIDs=aaaaaa")//
						.session(sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		String json1 = mr2.getResponse().getContentAsString();
		assertTrue(json1.equals(""));

		System.out.println("-----------------------case1:commIDs为1，2------------------------------");
//		MvcResult mr3 = mvc.perform(//
//				get("/purchasingOrder/retrieveNCommodityEx.bx?commIDs=1,2")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieve1() throws Exception {
		Shared.printTestMethodStartInfo();

		// PurchasingOrder p = new PurchasingOrder();
		System.out.println("---------------------------case1：查询一个状态为0的采购订单---------------------------------");
		mvc.perform(//
				get("/purchasingOrder/retrieve1.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=1")//
						.session(sessionBoss)//
		).andExpect(status().isOk());
		System.out.println("---------------------------case2：查询一个状态为1的采购订单---------------------------------");
		mvc.perform(//
				get("/purchasingOrder/retrieve1.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS1_ID)//
						.session(sessionBoss)//
		).andExpect(status().isOk());

		System.out.println("---------------------------case3：查询一个状态为2的采购订单---------------------------------");
		mvc.perform(//
				get("/purchasingOrder/retrieve1.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS2_ID)//
						.session(sessionBoss)//
		).andExpect(status().isOk());

		System.out.println("---------------------------case4：查询一个状态为3的采购订单---------------------------------");
		mvc.perform(//
				get("/purchasingOrder/retrieve1.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + PO_STATUS3_ID)//
						.session(sessionBoss)//
		).andExpect(status().isOk());

	}

	// @Test
	public void testPurchasingOrderToWxMsg() throws Exception {
		Shared.printTestMethodStartInfo(); // 调用StackTraceElement类检测方法开始的运行时间;

		System.out.println("----------------case1正确发送到一个用户的openid正确的消息---------------");
		// PurchasingOrder p = new PurchasingOrder();

		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param("commIDs", "2").param("commNOs", "4") //
						.param("commPrices", "11.1") //
						.param("commPurchasingUnit", "桶") //
						.param("providerID", "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param("barcodeIDs", "1").session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		// 验证返回的数据是否正确
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		assertTrue(o.getInt(BaseWxModel.WX_ERRCODE) == 0, "发送信息失败！！！错误码为：" + o.getInt(BaseWxModel.WX_ERRCODE) + ",错误信息为：" + o.getString(BaseWxModel.WX_ERRMSG) + "\t");

		System.out.println("----------------case2 发送到一个用户的openid不正确的消息:错误码为：40003---------------");
		Staff staff = new Staff();
		staff.setPhone(Shared.PhoneOfBoss);
		staff.setOpenid("12312312dqw312546sdf");
		staff.setName("店员3");
		staff.setICID("431522198412111666");
		staff.setWeChat("c5sdgsd");

		staffBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Staff_Update_OpenidAndUnionid, staff);

		assertTrue(staffBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "更新openid失败！错误码为：" + staffBO.getLastErrorCode());

		// PurchasingOrder p2 = new PurchasingOrder();

		// 按照目前做法，不需要传入采购单位，前端说后面迭代将进行处理
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param("commIDs", "2").param("commNOs", "4") //
						.param("commPrices", "11.1") //
						.param("commPurchasingUnit", "桶") //
						.param("providerID", "2") //
						.param(KEY_SHOPID, String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param("barcodeIDs", "1").session(sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr2);
		// 验证返回的数据是否正确
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		assertFalse(o2.getInt(BaseWxModel.WX_ERRCODE) == 0, "发送信息成功！！！此case应为发送失败！");

		Staff staff2 = new Staff();
		staff2.setPhone(Shared.PhoneOfBoss);
		staff2.setOpenid(Shared.openid);
		staff2.setName("店员3");
		staff2.setICID("431522198412111666");
		staff2.setWeChat("c5sdgsd");
		staffBO.updateObject(BaseBO.SYSTEM, BaseBO.CASE_Staff_Update_OpenidAndUnionid, staff2);

		assertTrue(staffBO.getLastErrorCode() == EnumErrorCode.EC_NoError, "更新openid失败！错误码为：" + staffBO.getLastErrorCode());
	}

}
