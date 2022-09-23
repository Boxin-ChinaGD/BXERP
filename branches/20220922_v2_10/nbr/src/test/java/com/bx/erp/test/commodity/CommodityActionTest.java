package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.Commodity.EnumStatusCommodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class CommodityActionTest extends BaseActionTest {

	protected final String barcode1 = "254535438237556956";
	protected final String barcode2 = "254521454628769759";
	protected final String barcode3 = "254521482452358743";
	protected final String barcode4 = "435854553853345353";
	protected final String barcode5 = "545245282384582474";
	protected final String barcode6 = "487231831218321812";
	protected final String barcode7 = "546123924248123187";

	protected final String packageUnit1 = "1";
	protected final String packageUnit2 = "2";
	protected final String packageUnit3 = "3";
	protected final String packageUnit4 = "4";
	protected final String packageUnit5 = "5";
	protected final String packageUnit6 = "6";
	protected final String packageUnit7 = "7";

	protected final String refCommodityMultiple1 = "0";
	protected final String refCommodityMultiple2 = "12";
	protected final String refCommodityMultiple3 = "12";
	protected final String refCommodityMultiple4 = "12";
	protected final String refCommodityMultiple5 = "5";
	protected final String refCommodityMultiple6 = "6";
	protected final String refCommodityMultiple7 = "7";

	protected final String priceRetail1 = "12";
	protected final String priceRetail2 = "140";
	protected final String priceRetail3 = "140";
	protected final String priceRetail4 = "140";
	protected final String priceRetail5 = "50";
	protected final String priceRetail6 = "60";
	protected final String priceRetail7 = "70";

	protected final String pricePurchase1 = "8";
	protected final String pricePurchase2 = "8";
	protected final String pricePurchase3 = "8";
	protected final String pricePurchase4 = "8";
	protected final String pricePurchase5 = "5";
	protected final String pricePurchase6 = "6";
	protected final String pricePurchase7 = "7";

	protected final String commodityA = "商品A";
	protected final String commodityB = "商品B";
	protected final String commodityC = "商品C";

	@BeforeClass
	public void setup() {

		super.setUp();

		// 由于本测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();

		// 由于本测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@Test
	public void testIndex() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(//
				get("/commodity.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk());
	}

	@Test
	public void testCommodityAbout() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(//
				get("/commodity/about.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk());
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("---------------- Case1:查询一个普通商品  -------------");
		Commodity c = new Commodity();
		MvcResult mr = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		BaseModel comm = c.parse1(o.getString(BaseAction.KEY_Object));
		assertTrue(comm.getID() == 1);

		System.out.println("---------------- Case2:查询一个多包装商品 -------------");
		MvcResult mr1 = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=51")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr1);
		//
		JSONObject o1 = JSONObject.fromObject(mr1.getResponse().getContentAsString()); //
		Commodity comm2 = (Commodity) c.parse1(o1.getString(BaseAction.KEY_Object));
		assertTrue(!comm2.getPackageUnitName().equals(""));
		assertTrue(comm2.getPackageUnitName().length() > 0);

		System.out.println("---------------- Case3:查询一个组合商品 -------------");
		MvcResult mr2 = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=45")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Commodity combination = (Commodity) c.parse1(o2.getString(BaseAction.KEY_Object));
		assertTrue(combination != null && combination.getListSlave1().size() == 2, "查询组合商品失败");

		System.out.println("---------------- Case4:查询一个不存在商品,返回为空-------------");
		MvcResult mr4 = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + Shared.BIG_ID)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		String json1 = mr4.getResponse().getContentAsString();
		assertTrue(json1.equals(""));

		System.out.println("---------------- Case5:没有权限进行查询商品，返回为空-------------");
//		MvcResult mr5 = mvc.perform(//
//				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=1")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//
//		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoPermission);

		System.out.println("---------------- Case6:查询一个已删除商品,返回为空-------------");
		MvcResult mr6 = mvc.perform(//
				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=" + 49)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoSuchData);

		System.out.println("---------------- Case7:没有权限进行查询  -------------");
//		MvcResult mr7 = mvc.perform(//
//				get("/commodity/retrieve1Ex.bx?" + Commodity.field.getFIELD_NAME_ID() + "=1")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//
//		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieve1() throws Exception {
		Shared.printTestMethodStartInfo();
		// ... 检查错误码
		System.out.println("---------------- Case1:STATUS=0|1 的ID作为条件查询  -------------");
		mvc.perform(//
				get("/commodity/retrieve1.bx?" + Commodity.field.getFIELD_NAME_ID() + "=56")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk()).andDo(print());

		System.out.println("---------------- Case2:STATUS=2（删除状态）的ID作为条件查询  -------------");
		MvcResult mr2 = mvc.perform(//
				get("/commodity/retrieve1.bx?" + Commodity.field.getFIELD_NAME_ID() + "=49")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk()).andReturn();
		String json2 = getJson(mr2);
		Assert.assertTrue(json2 == null || "".equals(json2), "查询数据不为空！");

		System.out.println("---------------- Case3:一个不存在的ID作为条件查询  -------------");
		MvcResult mr = mvc.perform(//
				get("/commodity/retrieve1.bx?" + Commodity.field.getFIELD_NAME_ID() + "=-22")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk()).andReturn();

		String json3 = getJson(mr);
		Assert.assertTrue(json3 == null || "".equals(json3), "查询数据不为空！");
	}

	private String getJson(MvcResult mr) throws UnsupportedEncodingException {
		return mr.getResponse().getContentAsString();
	}

	// @Test
	// public void testRetrieveN() throws Exception {
	// Shared.printTestMethodStartInfo();
	// System.out.println("---------------- Case1:输入iCategoryID作为条件查询
	// -------------");
	// mvc.perform(// status NO categoryID brandID name mnemonicCode barcode type
	// iPageIndex
	// get("/commodity/retrieveN.bx?" + Commodity.field.getFIELD_NAME_status() +
	// "=-1&" + Commodity.field.getFIELD_NAME_NO() + "=0&"//
	// + Commodity.field.getFIELD_NAME_categoryID() + "=2&"//
	// + Commodity.field.getFIELD_NAME_brandID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_name() + "=''&"//
	// + Commodity.field.getFIELD_NAME_mnemonicCode() + "=''&"//
	// + Commodity.field.getFIELD_NAME_string1() + "=''&"//
	// + Commodity.field.getFIELD_NAME_type() + "=-1&"//
	// + Commodity.field.getFIELD_NAME_iPageIndex() + "=1&"//
	// + Commodity.field.getFIELD_NAME_iPageSize() + "=10")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().is3xxRedirection()).andReturn();
	//
	// System.out.println("---------------- Case2:输入iBrandID作为条件查询-------------");
	// mvc.perform(// status NO categoryID brandID name mnemonicCode barcode type
	// iPageIndex
	// get("/commodity/retrieveN.bx?" + Commodity.field.getFIELD_NAME_status() +
	// "=-1&" + Commodity.field.getFIELD_NAME_NO() + "=0&"//
	// + Commodity.field.getFIELD_NAME_categoryID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_brandID() + "=3&"//
	// + Commodity.field.getFIELD_NAME_name() + "=''&"//
	// + Commodity.field.getFIELD_NAME_mnemonicCode() + "=''&"//
	// + Commodity.field.getFIELD_NAME_string1() + "=''&"//
	// + Commodity.field.getFIELD_NAME_type() + "=-1&"//
	// + Commodity.field.getFIELD_NAME_iPageIndex() + "=1&"//
	// + Commodity.field.getFIELD_NAME_iPageSize() + "=10")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().is3xxRedirection());
	// System.out.println("---------------- Case3:输入sName作为条件查询-------------");
	// mvc.perform(// status NO categoryID brandID name mnemonicCode barcode type
	// iPageIndex
	// get("/commodity/retrieveN.bx?" + Commodity.field.getFIELD_NAME_status() +
	// "=-1&" + Commodity.field.getFIELD_NAME_NO() + "=0&"//
	// + Commodity.field.getFIELD_NAME_categoryID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_brandID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_name() + "='薯愿香辣味薯片'&"//
	// + Commodity.field.getFIELD_NAME_mnemonicCode() + "=''&"//
	// + Commodity.field.getFIELD_NAME_string1() + "=''&"//
	// + Commodity.field.getFIELD_NAME_type() + "=-1&"//
	// + Commodity.field.getFIELD_NAME_iPageIndex() + "=1&"//
	// + Commodity.field.getFIELD_NAME_iPageSize() + "=10")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().is3xxRedirection());
	// System.out.println("----------------
	// Case4:输入sMnemonicCode作为条件查询-------------");
	// mvc.perform(// status NO categoryID brandID name mnemonicCode barcode type
	// iPageIndex
	// get("/commodity/retrieveN.bx?" + Commodity.field.getFIELD_NAME_status() +
	// "=-1&" + Commodity.field.getFIELD_NAME_NO() + "=0&"//
	// + Commodity.field.getFIELD_NAME_categoryID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_brandID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_name() + "=''&"//
	// + Commodity.field.getFIELD_NAME_mnemonicCode() + "='SP'&"//
	// + Commodity.field.getFIELD_NAME_string1() + "=''&"//
	// + Commodity.field.getFIELD_NAME_type() + "=-1&"//
	// + Commodity.field.getFIELD_NAME_iPageIndex() + "=1&"//
	// + Commodity.field.getFIELD_NAME_iPageSize() + "=10")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().is3xxRedirection());
	// System.out.println("---------------- Case5:输入sBarcode作为条件查询-------------");
	// mvc.perform(// status NO categoryID brandID name mnemonicCode barcode type
	// iPageIndex
	// get("/commodity/retrieveN.bx?" + Commodity.field.getFIELD_NAME_status() +
	// "=-1&" + Commodity.field.getFIELD_NAME_NO() + "=0&"//
	// + Commodity.field.getFIELD_NAME_categoryID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_brandID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_name() + "=''&"//
	// + Commodity.field.getFIELD_NAME_mnemonicCode() + "=''&"//
	// + Commodity.field.getFIELD_NAME_string1() + "='3548293894545'&"//
	// + Commodity.field.getFIELD_NAME_type() + "=-1&"//
	// + Commodity.field.getFIELD_NAME_iPageIndex() + "=1&"//
	// + Commodity.field.getFIELD_NAME_iPageSize() + "=10")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().is3xxRedirection());
	//
	// System.out.println("---------------- Case6:无条件查询-------------");
	// mvc.perform(// status NO categoryID brandID name mnemonicCode barcode type
	// iPageIndex
	// get("/commodity/retrieveN.bx?" + Commodity.field.getFIELD_NAME_status() +
	// "=-1&" + Commodity.field.getFIELD_NAME_NO() + "=0&"//
	// + Commodity.field.getFIELD_NAME_categoryID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_brandID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_name() + "=''&"//
	// + Commodity.field.getFIELD_NAME_mnemonicCode() + "=''&"//
	// + Commodity.field.getFIELD_NAME_string1() + "=''&"//
	// + Commodity.field.getFIELD_NAME_type() + "=-1&"//
	// + Commodity.field.getFIELD_NAME_iPageIndex() + "=1&"//
	// + Commodity.field.getFIELD_NAME_iPageSize() + "=10")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().is3xxRedirection());
	//
	// System.out.println("---------------- Case7:输入iNO作为条件查询-------------");
	// mvc.perform(// status NO categoryID brandID name mnemonicCode barcode type
	// iPageIndex
	// get("/commodity/retrieveN.bx?" + Commodity.field.getFIELD_NAME_status() +
	// "=-1&" + Commodity.field.getFIELD_NAME_NO() + "=1&"//
	// + Commodity.field.getFIELD_NAME_categoryID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_brandID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_name() + "=''&"//
	// + Commodity.field.getFIELD_NAME_mnemonicCode() + "=''&"//
	// + Commodity.field.getFIELD_NAME_string1() + "=''&"//
	// + Commodity.field.getFIELD_NAME_type() + "=-1&"//
	// + Commodity.field.getFIELD_NAME_iPageIndex() + "=1&"//
	// + Commodity.field.getFIELD_NAME_iPageSize() + "=10")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().is3xxRedirection());
	//
	// System.out.println("---------------- case8:根据商品状态进行查询 -------------");
	// mvc.perform(//
	// get("/commodity/retrieveN.bx?" + Commodity.field.getFIELD_NAME_status() +
	// "=0&" + Commodity.field.getFIELD_NAME_type() + "=-1")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().is3xxRedirection());
	//
	// System.out.println("---------------- case9:根据商品类型进行查询 -------------");
	// mvc.perform(//
	// get("/commodity/retrieveN.bx?" + Commodity.field.getFIELD_NAME_status() +
	// "=-1&" + Commodity.field.getFIELD_NAME_type() + "=0")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().is3xxRedirection());
	// // ... 检查错误码
	// }
	//
	// @Test
	// public void test10RetrieveN() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("----------------
	// Case10:传入string1包含_的特殊字符进行模糊搜索-------------");
	//
	// CommodityMapper commodityMapper = (CommodityMapper)
	// applicationContext.getBean("commodityMapper");
	// Commodity commodityTemplate = BaseCommodityTest.DataInput.getCommodity();
	// commodityTemplate.setInt2(Commodity.DEFINE_SET_StaffID(STAFF_ID3));
	// commodityTemplate.setName("ab_d" + System.currentTimeMillis() % 1000000);
	// createCommodity(commodityTemplate, commodityMapper);
	// mvc.perform(// status NO categoryID brandID name mnemonicCode barcode type
	// iPageIndex
	// get("/commodity/retrieveN.bx?" + Commodity.field.getFIELD_NAME_status() +
	// "=-1&" + Commodity.field.getFIELD_NAME_NO() + "=0&"//
	// + Commodity.field.getFIELD_NAME_categoryID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_brandID() + "=" + BaseAction.INVALID_ID +
	// "&"//
	// + Commodity.field.getFIELD_NAME_name() + "=''&"//
	// + Commodity.field.getFIELD_NAME_mnemonicCode() + "=''&"//
	// + Commodity.field.getFIELD_NAME_string1() + "=_d&"//
	// + Commodity.field.getFIELD_NAME_type() + "=-1&"//
	// + Commodity.field.getFIELD_NAME_iPageIndex() + "=1&"//
	// + Commodity.field.getFIELD_NAME_iPageSize() + "=10")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss))//
	// )//
	// .andExpect(status().is3xxRedirection());
	// }

	@Test
	public void testRetrieveNEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		// case1:不根据商品状态和商品类型进行查询
		System.out.println("--------------- case1:不根据商品状态和商品类型进行查询 ---------------");
		MvcResult mrl = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl);

	}

	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		// case2:根据商品状态进行查询
		System.out.println("--------------- case2:根据商品状态进行查询 ---------------");
		MvcResult mrl1 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=0&" + Commodity.field.getFIELD_NAME_type() + "=-1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mrl1);
	}

	@Test
	public void testRetrieveNEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		// case3:根据商品类型进行查询
		System.out.println("--------------- case3:根据商品类型进行查询 ---------------");
		MvcResult mrl2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=0")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl2);
	}

	@Test
	public void testRetrieveNEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case4:输入CategoryID作为条件查询---------------");
		MvcResult mvcResult = mvc.perform(//
				post("/commodity/retrieveNEx.bx?status=-1&type=-1&categoryID=2")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mvcResult);
	}

	@Test
	public void testRetrieveNEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case5:输入BrandID作为条件查询 ---------------");
		MvcResult mvcResult2 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?status=-1&type=-1&brandID=3")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mvcResult2);
	}

	@Test
	public void testRetrieveNEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case6:输入Name作为条件查询 ---------------");
		MvcResult mvcResult3 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?status=-1&type=-1&name=薯愿香辣味薯片")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mvcResult3);
	}

	@Test
	public void testRetrieveNEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case7:输入MnemonicCode作为条件查询作为条件查询---------------");
		MvcResult mvcResult4 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?status=" + BaseAction.INVALID_STATUS + "&type=-1&mnemonicCode=SP")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mvcResult4);
	}

	@Test
	public void testRetrieveNEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case8:输入大于6位Barcode作为条件查询，期望是查询到一个或者多个商品 ---------------");
		MvcResult mvcResult5 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?status=-1&type=-1&barcodes=3548293")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mvcResult5);
	}

	@Test
	public void testRetrieveNEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case9:NO作为条件查询 ---------------");
		MvcResult mvcResult6 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?status=-1&type=-1&no=1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mvcResult6);
	}

	@Test
	public void testRetrieveNEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case10:输入Status作为条件查询 ---------------");
		MvcResult mvcResult7 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?status=1&type=-1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mvcResult7);
	}

	@Test
	public void testRetrieveNEx11() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case11:输入Status和NO作为条件查询---------------");
		MvcResult mvcResult8 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=0&" + Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_NO() + "=1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mvcResult8);
	}

	@Test
	public void testRetrieveNEx12() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case12:输入iType和iNO作为条件查询 ---------------");
		MvcResult mvcResult9 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=0&" + Commodity.field.getFIELD_NAME_type() + "=0&" + Commodity.field.getFIELD_NAME_NO() + "=1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mvcResult9);
	}

	@Test
	public void testRetrieveNEx13() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case13:如果商品是组合商品，返回其子商品---------------");
		// 1、创建一个普通commodity
		Commodity commodityCreate = BaseCommodityTest.DataInput.getCommodity();
		commodityCreate.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> commParams = commodityCreate.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodityCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> commBm = commodityMapper.createSimpleEx(commParams);
		Assert.assertTrue(commBm != null && EnumErrorCode.values()[Integer.parseInt(commParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		Commodity commodityCreated = (Commodity) commBm.get(0).get(0);
		commodityCreate.setIgnoreIDInComparision(true);
		if (commodityCreate.compareTo(commodityCreated) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不相等");
		}
		// 2、创建一个组合商品
		Commodity commodityCreate2 = BaseCommodityTest.DataInput.getCommodity();
		commodityCreate2.setType(EnumCommodityType.ECT_Combination.getIndex());
		commodityCreate2.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> commParams2 = commodityCreate2.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodityCreate2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> commBm2 = commodityMapper.createCombinationEx(commParams2);
		Assert.assertTrue(commBm2 != null && EnumErrorCode.values()[Integer.parseInt(commParams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		Commodity commodityCreated2 = (Commodity) commBm2.get(0).get(0);
		commodityCreate2.setIgnoreIDInComparision(true);
		if (commodityCreate2.compareTo(commodityCreated2) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不相等");
		}
		// 3、创建关联表subcommodity
		SubCommodity subCommodityCreate = new SubCommodity();
		subCommodityCreate.setCommodityID(commodityCreated2.getID());
		subCommodityCreate.setSubCommodityID(commodityCreated.getID());
		Map<String, Object> subParams = subCommodityCreate.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodityCreate);
		SubCommodity subCommodityCreated = (SubCommodity) subCommodityMapper.create(subParams);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		subCommodityCreate.setIgnoreIDInComparision(true);
		if (subCommodityCreate.compareTo(subCommodityCreated) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不相等");
		}

		MvcResult mr13 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr13);

		// 结果验证
		// 4、判断组合商品的子商品集合是否存在commodity
		JSONObject o13 = JSONObject.fromObject(mr13.getResponse().getContentAsString()); //
		List<Integer> list13 = JsonPath.read(o13, "$.objectList[*].commodity.ID");
		List<List<Commodity>> subCommInfoDoubleList = JsonPath.read(o13, "$.objectList[*].commodity.listSlave1");
		for (List<Commodity> commList : subCommInfoDoubleList) {
			Assert.assertTrue(commList.size() > 0, "组合商品没有返回子商品信息");
		}
		System.out.println("list13:" + list13.size());
		System.out.println("list13:" + list13);

		SubCommodity subCommodity = new SubCommodity();
		for (int i = 0; i < list13.size(); i++) {
			subCommodity.setCommodityID(list13.get(i));
			Map<String, Object> params = subCommodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, subCommodity);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> list = subCommodityMapper.retrieveN(params);
			Assert.assertTrue(list.size() > 0, "ID为" + list13.get(i) + "的组合商品没有子商品");
		}
	}

	@Test
	public void testRetrieveNEx14() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case14:没有权限进行查询---------------");
//		MvcResult mrl14 = mvc.perform(//
//				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl14, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testRetrieveNEx15() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case15:根据条形码查询商品类型为2的---------------");
		Commodity commodity15 = BaseCommodityTest.DataInput.getCommodity();
		commodity15.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity15.setRefCommodityID(8);
		commodity15.setRefCommodityMultiple(3);
		commodity15.setOperatorStaffID(STAFF_ID3);
		commodity15.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity createCommodity15 = BaseCommodityTest.createCommodityViaMapper(commodity15, BaseBO.CASE_Commodity_CreateMultiPackaging);

		Barcodes createBarcodes15 = getCreateBarcodes(createCommodity15.getID(), (commodity15.getOperatorStaffID()));

		MvcResult mr15 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + createBarcodes15.getBarcode())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr15);

		JSONObject o15 = JSONObject.fromObject(mr15.getResponse().getContentAsString());
		int commNO15 = JsonPath.read(o15, "$.count");
		Assert.assertTrue(commNO15 == 0, "Action返回的商品数目不正确");
	}

	@Test
	public void testRetrieveNEx16() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case16:条形码和商品名称一样---------------");
		Commodity commNameIsBarcodes = BaseCommodityTest.DataInput.getCommodity();
		commNameIsBarcodes.setName("999999" + System.currentTimeMillis() % 10000);
		commNameIsBarcodes.setOperatorStaffID(STAFF_ID3);
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(commNameIsBarcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodityTemplate = BaseCommodityTest.DataInput.getCommodity();
		commodityTemplate.setName("999999" + System.currentTimeMillis() % 10000);
		commodityTemplate.setOperatorStaffID(STAFF_ID3);
		commodityTemplate = BaseCommodityTest.createCommodityViaAction(commodityTemplate, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setBarcode(commodityTemplate.getName());
		barcodes.setCommodityID(createCommodity.getID());
		barcodes.setOperatorStaffID(STAFF_ID3);
		//
		Map<String, Object> createBarcodesParam = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesC16 = (Barcodes) barcodesMapper.create(createBarcodesParam);
		Assert.assertTrue(barcodesC16 != null && EnumErrorCode.values()[Integer.parseInt(createBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		barcodes.setIgnoreIDInComparision(true);
		if (barcodes.compareTo(barcodesC16) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不相等");
		}

		MvcResult mr16 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_queryKeyword() + "="
						+ commodityTemplate.getName().substring(0, 6))//
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
								.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr16);

		JSONObject o16 = JSONObject.fromObject(mr16.getResponse().getContentAsString());
		int commNO16 = JsonPath.read(o16, "$.count");
		Assert.assertTrue(commNO16 == 2, "action返回的商品数目不正确");
		// 刪除測試商品
		BaseCommodityTest.deleteCommodityViaAction(createCommodity, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(commodityTemplate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@Test
	public void testRetrieveNEx17() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case17:商品助记码和商品名称一样---------------");
		Commodity commodityTemplateA = BaseCommodityTest.DataInput.getCommodity();
		commodityTemplateA.setOperatorStaffID(STAFF_ID3);
		Commodity createCommodityA = BaseCommodityTest.createCommodityViaAction(commodityTemplateA, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		Commodity commodityTemplateB = BaseCommodityTest.DataInput.getCommodity();
		commodityTemplateB.setOperatorStaffID(STAFF_ID3);
		commodityTemplateB.setShortName(createCommodityA.getName());
		Commodity createCommodityB = BaseCommodityTest.createCommodityViaAction(commodityTemplateB, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		MvcResult mr = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + createCommodityA.getName())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		int commNO = JsonPath.read(o, "$.count");
		Assert.assertTrue(commNO == 2, "action返回的商品数目不正确");
		// 刪除測試商品
		BaseCommodityTest.deleteCommodityViaAction(createCommodityA, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCommodityTest.deleteCommodityViaAction(createCommodityB, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@Test
	public void testRetrieveNEx18() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case18:通过name查询多包装商品---------------");
		Commodity commodityTemplate18 = BaseCommodityTest.DataInput.getCommodity();
		commodityTemplate18.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodityTemplate18.setRefCommodityID(8);
		commodityTemplate18.setRefCommodityMultiple(2);
		commodityTemplate18.setOperatorStaffID(STAFF_ID3);
		commodityTemplate18.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		BaseCommodityTest.createCommodityViaMapper(commodityTemplate18, BaseBO.CASE_Commodity_CreateMultiPackaging);

		MvcResult mr18 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodityTemplate18.getName())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr18);
		JSONObject o18 = JSONObject.fromObject(mr18.getResponse().getContentAsString());
		int commNO18 = JsonPath.read(o18, "$.count");
		Assert.assertTrue(commNO18 == 0, "action返回的商品数目不正确");
	}

	@Test
	public void testRetrieveNEx19() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case19:通过name查询组合商品---------------");
		Commodity commodityTemplate19 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commodityTemplate19.setOperatorStaffID(STAFF_ID3);
		String json = JSONObject.fromObject(commodityTemplate19).toString();
		commodityTemplate19.setSubCommodityInfo(json);
		BaseCommodityTest.createCommodityViaAction(commodityTemplate19, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		MvcResult mr19 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_queryKeyword() + "=" + commodityTemplate19.getName())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr19);
		JSONObject o19 = JSONObject.fromObject(mr19.getResponse().getContentAsString());
		List<String> commName = JsonPath.read(o19, "$.objectList[*].commodity.name");
		Assert.assertTrue(commName != null && commName.size() > 0, "action返回的商品数目不正确");
		if (!commodityTemplate19.getName().equals(commName.get(0))) {
			Assert.assertTrue(false, "返回的商品名称不一致");
		}
	}

	@Test
	public void testRetrieveNEx20() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case20:根据一时间段进行查询。---------------");
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

		String date1 = sdf.format(new Date());
		Commodity commodityTemplate20 = BaseCommodityTest.DataInput.getCommodity();
		commodityTemplate20.setOperatorStaffID(STAFF_ID3);
		Commodity createCommodity20 = BaseCommodityTest.createCommodityViaAction(commodityTemplate20, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		String date2 = sdf.format(new Date());

		MvcResult mr20 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_date1() + "=" + date1//
						+ "&" + createCommodity20.getDate2() + "=" + date2)//
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
								.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr20);

		JSONObject o20 = JSONObject.fromObject(mr20.getResponse().getContentAsString());
		List<String> commName20 = JsonPath.read(o20, "$.objectList[*].commodity.name");
		Assert.assertTrue(commName20 != null && commName20.size() > 0, "action返回的商品数目不正确");
		if (!createCommodity20.getName().equals(commName20.get(0))) {
			Assert.assertTrue(false, "返回的商品名称不一致");
		}

	}

	@Test
	public void testRetrieveNEx21() throws Exception {
		Shared.printTestMethodStartInfo();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);

		String date1 = sdf.format(new Date());
		System.out.println("--------------- case21:查询某时间后的商品。---------------");
		date1 = sdf.format(new Date());
		Commodity commodityTemplate21 = BaseCommodityTest.DataInput.getCommodity();
		commodityTemplate21.setOperatorStaffID(STAFF_ID3);
		Commodity createCommodity21 = BaseCommodityTest.createCommodityViaAction(commodityTemplate21, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		MvcResult mr21 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_date1() + "=" + date1)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr21);

		JSONObject o21 = JSONObject.fromObject(mr21.getResponse().getContentAsString());
		List<String> commName21 = JsonPath.read(o21, "$.objectList[*].commodity.name");
		Assert.assertTrue(commName21 != null && commName21.size() > 0, "action返回的商品数目不正确");
		if (!createCommodity21.getName().equals(commName21.get(0))) {
			Assert.assertTrue(false, "返回的商品名称不一致");
		}
	}

	@Test
	public void testRetrieveNEx22() throws Exception {
		Shared.printTestMethodStartInfo();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		System.out.println("--------------- case22:查询某段时间前的商品。---------------");
		Commodity commodityTemplate22 = BaseCommodityTest.DataInput.getCommodity();
		commodityTemplate22.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.createCommodityViaAction(commodityTemplate22, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		MvcResult mr22 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_date2() + "=" + sdf.format(new Date()))//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr22);

		JSONObject o22 = JSONObject.fromObject(mr22.getResponse().getContentAsString());
		int commNO22 = JsonPath.read(o22, "$.count");
		Assert.assertTrue(commNO22 > 0, "action返回的商品条数不正确");
	}

	@Test
	public void testRetrieveNEx23() throws Exception {
		Shared.printTestMethodStartInfo();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		String date1 = sdf.format(new Date());
		System.out.println("--------------- case23:开始时间大于结束时间。---------------");

		MvcResult mr23 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_status() + "=-1&" + Commodity.field.getFIELD_NAME_type() + "=-1&" + Commodity.field.getFIELD_NAME_date1() + "=" + sdf.format(new Date()) //
						+ "&" + Commodity.field.getFIELD_NAME_date2() + "=" + date1)//
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
								.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr23);
		JSONObject o23 = JSONObject.fromObject(mr23.getResponse().getContentAsString());
		int commNO23 = JsonPath.read(o23, "$.count");
		Assert.assertTrue(commNO23 == 0, "action返回的商品条数不正确");
	}

	@Test
	public void testRetrieveNEx24() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("------------------- case24:根据64位的条形码搜索商品-------------------");
		// 创建商品
		String string8 = "1111111122222222333333334444444411111111222222223333333344444444";
		Commodity c11 = BaseCommodityTest.DataInput.getCommodity();
		c11.setRuleOfPoint(1);
		c11.setPurchaseFlag(1);
		c11.setShelfLife(1);
		c11.setMultiPackagingInfo(string8 + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";");
		c11.setProviderIDs("1");
		c11.setReturnObject(1);
		c11.setPriceRetail(10);
		//
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
		//
		MvcResult mrl21 = mvc.perform( //
				BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c11, sessionBoss)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrl21);
		// 检查点
		CommodityCP.verifyCreate(mrl21, c11, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO,
				categoryBO, Shared.DBName_Test);
		//
		MvcResult mr8 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_type() + "=-1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), string8)//
						.param(Commodity.field.getFIELD_NAME_type(), "-1")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr8);

		JSONObject o8 = JSONObject.fromObject(mr8.getResponse().getContentAsString()); //
		List<?> list8 = JsonPath.read(o8, "$.objectList[*].listBarcodes[*].barcode");
		System.out.println("list8=" + list8);
		if (list8.size() > 0) {
			boolean flag = false;
			for (int i = 0; i < list8.size(); i++) {
				String s8 = (String) list8.get(i);
				if (s8.contains(string8)) {
					flag = true;
				}
			}
			Assert.assertTrue(flag, "case24根据条形码模糊查询，结果与预期的不一致");
		}
		//
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@Test
	public void testRetrieveNEx25() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case25:输入小于7位的Barcode作为条件查询,期望是查询不到商品 ---------------");
		MvcResult mvcResult25 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?status=-1&type=-1&queryKeyword=354829")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mvcResult25);
		// 结果验证
		String json = mvcResult25.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		List<Commodity> list = JsonPath.read(o, "$." + BaseAction.KEY_ObjectList);
		Assert.assertTrue(list.size() == 0, "case25测试失败！期望地方是查询不到商品");
	}

	@Test
	public void testRetrieveNEx26() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println("--------------- case26:输入大于64位条形码搜索商品,期望是查询不到商品 ---------------");
		String string9 = "11111111222222223333333344444444111111112222222233333333444444445555";
		MvcResult mr9 = mvc.perform(//
				post("/commodity/retrieveNEx.bx?" + Commodity.field.getFIELD_NAME_type() + "=-1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), string9)//
						.param(Commodity.field.getFIELD_NAME_type(), "-1")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_WrongFormatForInputField); // 会被Commodity的checkretrieveN方法拦截
	}

	@Test
	public void testRetrieveNEx27() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("--------------- case26:传入queryKeyword包含_的特殊字符进行模糊搜索 ---------------");
		Commodity commodityTemplate = BaseCommodityTest.DataInput.getCommodity();
		commodityTemplate.setOperatorStaffID(STAFF_ID3);
		commodityTemplate.setName("acd" + System.currentTimeMillis() % 1000000);
		Commodity createCommodity = BaseCommodityTest.createCommodityViaAction(commodityTemplate, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		MvcResult mrl = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), "_c")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl);
		// 删除测试数据
		BaseCommodityTest.deleteCommodityViaAction(createCommodity, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@Test
	public void testRetrieveNEx28() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("--------------- case27:条码有123456789 12345678 输入1234567 搜索 ---------------");
		Commodity commodityTemplate = BaseCommodityTest.DataInput.getCommodity();
		commodityTemplate.setOperatorStaffID(STAFF_ID3);
		commodityTemplate.setName("acd" + System.currentTimeMillis() % 1000000);
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(commodityTemplate, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodity.getID());
		barcodes.setBarcode("123456789");
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> barcodesparams = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesCreate = (Barcodes) barcodesMapper.create(barcodesparams);
		assertTrue(barcodesCreate != null && EnumErrorCode.values()[Integer.parseInt(barcodesparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "条形码增加失败");
		//
		Barcodes barcodes2 = new Barcodes();
		barcodes2.setCommodityID(commodity.getID());
		barcodes2.setBarcode("12345678");
		barcodes2.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> barcodesparams2 = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesCreate2 = (Barcodes) barcodesMapper.create(barcodesparams2);
		assertTrue(barcodesCreate2 != null && EnumErrorCode.values()[Integer.parseInt(barcodesparams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "条形码增加失败");
		//
		MvcResult mrl = mvc.perform(//
				post("/commodity/retrieveNEx.bx")//
						.param(Commodity.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(Commodity.field.getFIELD_NAME_type(), String.valueOf(BaseAction.INVALID_Type))//
						.param(Commodity.field.getFIELD_NAME_NO(), String.valueOf(BaseAction.INVALID_NO))//
						.param(Commodity.field.getFIELD_NAME_categoryID(), "-1")//
						.param(Commodity.field.getFIELD_NAME_brandID(), "-1")//
						.param(Commodity.field.getFIELD_NAME_queryKeyword(), "12345678")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl);
		JSONObject o8 = JSONObject.fromObject(mrl.getResponse().getContentAsString()); //
		List<?> list8 = JsonPath.read(o8, "$.objectList[*].commodity.ID");
		assertTrue(list8.contains(commodity.getID()), "查询失败");
		// barcodesCreate.setOperatorStaffID(STAFF_ID3);
		// Map<String, Object> barcodesDeleteparams =
		// barcodes.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodesCreate);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// barcodesMapper.delete(barcodesDeleteparams);
		// assertTrue(EnumErrorCode.values()[Integer.parseInt(barcodesDeleteparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_NoError, "条形码删除失败");
		// //
		// barcodesCreate2.setOperatorStaffID(STAFF_ID3);
		// Map<String, Object> barcodesDeleteparams2 =
		// barcodes.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodesCreate2);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// barcodesMapper.delete(barcodesDeleteparams2);
		// assertTrue(EnumErrorCode.values()[Integer.parseInt(barcodesDeleteparams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_NoError, "条形码删除失败");
		// //
		// commodity.setOperatorStaffID(STAFF_ID3);
		// Map<String, Object> paramForDelete =
		// commodity.getDeleteParam(BaseBO.INVALID_CASE_ID, commodity);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// commodityMapper.deleteSimple(paramForDelete);
		// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_NoError);
		// 删除测试数据
		BaseCommodityTest.deleteCommodityViaAction(commodity, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@Test
	public void testRetrieveNEx30() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case30:不传条件查询，查看商品数据返回是否默认降序");
		MvcResult mrl = mvc.perform(//
				post("/commodity/retrieveNEx.bx?")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl);
		//
		JSONObject o = JSONObject.fromObject(mrl.getResponse().getContentAsString());
		List<Integer> list = JsonPath.read(o, "$.objectList[*].commodity.ID");
		assertTrue(list.size() > 0);
		for (int i = 1; i < list.size(); i++) {
			assertTrue(list.get(i - 1) > list.get(i), "数据返回错误（非降序）");
		}

	}

	protected Barcodes getCreateBarcodes(int commodityID, int staffID) {
		Barcodes barcodes15 = new Barcodes();
		barcodes15.setBarcode("acd232" + System.currentTimeMillis() % 1000000);
		barcodes15.setCommodityID(commodityID);
		barcodes15.setOperatorStaffID(staffID);

		Map<String, Object> createParam = barcodes15.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes15);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes bm = (Barcodes) barcodesMapper.create(createParam);
		Assert.assertTrue(bm != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, createParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		barcodes15.setIgnoreIDInComparision(true);
		if (barcodes15.compareTo(bm) != 0) {
			Assert.assertTrue(false, "创建的字段与DB读出的字段不相等");
		}

		return bm;
	}

	@Test
	public void testRetrieveInventory() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("CASE1 :--------------------根据条形码查询商品库存---------------------------");
		Commodity commA = BaseCommodityTest.DataInput.getCommodity();
		commA.setNO(100);
		commA.setnOStart(100);
		commA.setPurchasingPriceStart(100D);
		commA.setLatestPricePurchase(commA.getPurchasingPriceStart()); // 期初商品latestPricePurchase要等于purchasingPriceStart
		commA.setOperatorStaffID(STAFF_ID3);
//		Map<String, Object> params = commA.getCreateParamEx(BaseBO.INVALID_CASE_ID, commA);
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);

//		Commodity commCreated = (Commodity) bmList.get(5).get(0);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commA, BaseBO.CASE_Commodity_CreateSingle);

		commA.setIgnoreIDInComparision(true);
		if (commA.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commCreated.getID());
		barcodes.setBarcode(Shared.generateStringByTime(9));
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> createBarcodesParam = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesCreate = (Barcodes) barcodesMapper.create(createBarcodesParam);
		System.out.println("barcodesCreate=" + barcodesCreate);
		barcodes.setIgnoreIDInComparision(true);
		if (barcodes.compareTo(barcodesCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		MvcResult mrl = mvc.perform(//
				get("/commodity/retrieveInventoryEx.bx?barcodes=" + barcodesCreate.getBarcode() + "&" + Commodity.field.getFIELD_NAME_shopID() + "=2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl);

		String json = mrl.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		List<Integer> nOList = JsonPath.read(o, "$.objectList[*].NO");
		if (commCreated.getNO() != nOList.get(0)) {
			Assert.assertTrue(false, "取出的数量不正确");
		}

		// ... 检查错误码
		// case2:传递已被删除的商品id
		System.out.println("CASE2 :----------------------------------------------------   根据条形码查询商品库存商品为已删除的  ---------------------------");
		Commodity commB = BaseCommodityTest.DataInput.getCommodity();
		commB.setNO(100);
		commB.setnOStart(100);
		commB.setPurchasingPriceStart(100D);
		commB.setLatestPricePurchase(commB.getPurchasingPriceStart()); // 期初商品latestPricePurchase要等于purchasingPriceStart
		commB.setOperatorStaffID(STAFF_ID3);
		commB.setStatus(EnumStatusCommodity.ESC_Deleted.getIndex());
		Map<String, Object> params2 = commB.getCreateParamEx(BaseBO.INVALID_CASE_ID, commB);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList2 = commodityMapper.createSimpleEx(params2);

		Commodity commCreated2 = (Commodity) bmList2.get(5).get(0);

		commB.setIgnoreIDInComparision(true);
		if (commB.compareTo(commCreated2) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		Barcodes barcodes2 = new Barcodes();
		barcodes2.setCommodityID(commCreated2.getID());
		barcodes2.setBarcode("12345678");
		barcodes2.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> createBarcodesParam2 = barcodes2.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesCreate2 = (Barcodes) barcodesMapper.create(createBarcodesParam2);
		barcodes2.setIgnoreIDInComparision(true);
		if (barcodes2.compareTo(barcodesCreate2) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		MvcResult mrl2 = mvc.perform(//
				get("/commodity/retrieveInventoryEx.bx?barcodes=" + barcodesCreate2.getBarcode() + "&" + Commodity.field.getFIELD_NAME_shopID() + "=2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		String json2 = mrl2.getResponse().getContentAsString();
		if (json2.equals("")) {
			Assert.assertTrue(true, "结果不为空");
		}

		System.out.println("------------------ case3:查询不存在的商品库存  -------------");
		MvcResult mrl3 = mvc.perform(//
				get("/commodity/retrieveInventoryEx.bx?id=-1" + "&" + Commodity.field.getFIELD_NAME_shopID() + "=2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		String json3 = mrl3.getResponse().getContentAsString();
		if (json3.equals("")) {
			Assert.assertTrue(true, "结果不为空");
		}

		System.out.println("------------------ case4:没有权限查询  -------------");
//		MvcResult mrl4 = mvc.perform(//
//				get("/commodity/retrieveInventoryEx.bx?barcodes=" + barcodesCreate.getBarcode() + "&" + Commodity.field.getFIELD_NAME_shopID() + "=2")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mrl4, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdatePrice() throws Exception {
		Shared.printTestMethodStartInfo();
		System.out.println();
		System.out.println("------------------------ CASE1:修改平均进货价 ----------------------");
		mvc.perform(//
				get("/commodity/updatePrice.bx?" + Commodity.field.getFIELD_NAME_ID() + "=1&" + Commodity.field.getFIELD_NAME_latestPricePurchase() + "=12.0")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk());

		// ... 检查错误码
		// System.out.println("------------------------
		// CASE2:修改进货价----------------------");
		// mvc.perform(//
		// get("/commodity/updatePrice.bx?" + Commodity.field.getFIELD_NAME_ID() + "=1&"
		// + Commodity.field.getFIELD_NAME_pricePurchase() + "=12.0")//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
		// Shared.PhoneOfBoss))//
		// )//
		// .andExpect(status().isOk());

		System.out.println("------------------------ CASE2:修改零售价 - ----------------------");
		mvc.perform(//
				get("/commodity/updatePrice.bx?" + Commodity.field.getFIELD_NAME_ID() + "=1&" + Commodity.field.getFIELD_NAME_priceRetail() + "=12.0")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk());
	}
	

	@Test
	public void testUpdatePurchasingUnit() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(//
				post("/commodity/updatePurchasingUnit.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(Commodity.field.getFIELD_NAME_ID(), "1")//
						.param(Commodity.field.getFIELD_NAME_purchasingUnit(), "盒子")//
		)//
				.andExpect(status().isOk());
	}

	@Test
	public void testRetrieveNEx29() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mrl = mvc.perform(//
				get("/commodity/retrieveN2Ex.bx?commListID=2,5")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param("sValue", "3548293")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl);

		System.out.println("-----------------------case2:没有权限进行操作---------------------------------");
//		MvcResult mrl2 = mvc.perform(//
//				get("/commodity/retrieveN2Ex.bx?commListID=2,5")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.param("sValue", "3548293")//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl2, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		System.out.println("----------------------------Case1:查询一个不存在的商品名称-------------------------------------------");
		MvcResult mrl = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Commodity.field.getFIELD_NAME_uniqueField(), "不重复的商品名称")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl);

		System.out.println("----------------------------Case2:查询一个已经存在的商品名称-------------------------------------------");
		MvcResult mrl2 = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Commodity.field.getFIELD_NAME_uniqueField(), "可比克薯片")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl2, EnumErrorCode.EC_Duplicated);

		System.out.println("----------------------------Case3:查询一个已经被删除的商品名称---------------------------------------");
		MvcResult mrl3 = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Commodity.field.getFIELD_NAME_uniqueField(), "百事青椒味薯片1")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3);

		System.out.println("----------------------------Case4:uniqueField值为null---------------------------------------");
		String string4 = null;
		MvcResult mrl4 = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Commodity.field.getFIELD_NAME_uniqueField(), string4)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl4, EnumErrorCode.EC_WrongFormatForInputField);

		System.out.println("----------------------------Case5:查询一个不存在的商品名称,传入错误的fieldToCheckUnique(小于等于0)-------------------------------------------");
		MvcResult mr5 = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), "0").param(Commodity.field.getFIELD_NAME_uniqueField(), "不重复的商品名称")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_WrongFormatForInputField);

		System.out.println("----------------------------Case6:查询一个不存在的商品名称,传入错误的fieldToCheckUnique(大于Integer.MAX_VALUE)-------------------------------------------");
		MvcResult mr6 = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(Integer.MAX_VALUE + 1)).param(Commodity.field.getFIELD_NAME_uniqueField(), "不重复的商品名称")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_WrongFormatForInputField);

		System.out.println("----------------------------Case7:查询一个不存在的商品名称,传入错误的fieldToCheckUnique(fieldToCheckUnique=999999，还没有相对应的字段检查)-------------------------------------------");
		MvcResult mr7 = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), String.valueOf(999999)).param(Commodity.field.getFIELD_NAME_uniqueField(), "不重复的商品名称")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_WrongFormatForInputField);

		System.out.println("----------------------------Case7:查询一个不存在的商品名称,格式输入不符合商品名称格式-------------------------------------------");
		MvcResult mr8 = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Commodity.field.getFIELD_NAME_uniqueField(), "_d#*&&2")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_WrongFormatForInputField);

		System.out.println("----------------------------Case9:查询一个已经存在的商品名称,但传入的ID是这个已存在的商品名称的商品ID-------------------------------------------");
		MvcResult mr9 = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Commodity.field.getFIELD_NAME_ID(), "1").param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Commodity.field.getFIELD_NAME_uniqueField(), "可比克薯片")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr9);

		System.out.println("----------------------------Case10:查询一个已经存在的商品名称,传入的ID不是这个已存在的商品名称的商品ID-------------------------------------------");
		MvcResult mr10 = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Commodity.field.getFIELD_NAME_ID(), "2").param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Commodity.field.getFIELD_NAME_uniqueField(), "可比克薯片")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_Duplicated);

		System.out.println("----------------------------Case11:查询一个已经删除的商品名称,传入的ID是这个已删除的商品名称的商品ID-------------------------------------------");
		MvcResult mr11 = mvc.perform(//
				post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(Commodity.field.getFIELD_NAME_ID(), "49").param(Commodity.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Commodity.field.getFIELD_NAME_uniqueField(), "百事青椒味薯片1")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr11);

		// System.out.println("----------------------------Case8:没有权限进行查询---------------------------------------");
		// MvcResult mrl8 = mvc.perform(//
		// post("/commodity/retrieveNToCheckUniqueFieldEx.bx")//
		// .contentType(MediaType.APPLICATION_JSON)//
		// .session((MockHttpSession) Shared.getStaffLoginSession(mvc, Staff2Phone))//
		// .param(Commodity.field.getFIELD_NAME_int1(), "1")
		// .param(Commodity.field.getFIELD_NAME_string1(), "可比克薯片")//
		// )//
		// .andExpect(status().isOk())//
		// .andDo(print())//
		// .andReturn();
		// Shared.checkJSONErrorCode(mrl8, EnumErrorCode.EC_NoPermission);
	}
}
