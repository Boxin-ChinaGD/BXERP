package com.bx.erp.test.syncSIT.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommoditySyncCache;
import com.bx.erp.test.Shared;
import com.bx.erp.test.syncSIT.BaseSyncSITTest1;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread1;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class CommoditySyncSITTest1 extends BaseSyncSITTest1 {
	protected final String barcode1 = "251135499823755628";

	protected final String commodityA = "商品A";
	protected final String commodityB = "商品B";
	protected final String commodityC = "商品C";
	
	protected final String SyncActionDeleteExURL = "/commoditySync/deleteEx.bx";

	@Override
	protected BaseBO getSyncCacheBO() {
		return commoditySyncCacheBO;
	}

	@Override
	protected String getSyncActionDeleteExURL() {
		return SyncActionDeleteExURL;
	}

	@Override
	protected BaseModel getModel() {
		return new Commodity();
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return commoditySyncCacheDispatcherBO;
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_CommoditySyncInfo;
	}

	@Override
	protected int getDeleteAllSyncCacheCaseID() {
		return BaseBO.CASE_X_DeleteAllCommoditySyncCache;
	}

	@Override
	protected BaseSyncCache getSyncCache() {
		return new CommoditySyncCache();
	}

	@Override
	protected BaseSyncSITTestThread1 getThread(MockMvc mvc, HttpSession session, int iPhase, int iPosNO, int iSyncBlockNO) {
		return new CommoditySyncThread1(mvc, session, iPhase, iPosNO, iSyncBlockNO);
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

	/** 1.pos机1创建1个Commodity，pos机2创建2个Commodity，然后分别做同步更新。
	 * 2.pos3，4，5等待pos1，2更新完后，开启同步器 3.当所有pos机都已经同步完成后，删除DB和同存相关的数据 */
	@Test(timeOut = 60000)
	public void runCommoditySyncProcess() throws Exception {
		Shared.printTestMethodStartInfo();

		runSITTest1();
	}

	protected boolean createObject1() throws Exception {
		Commodity c = new Commodity();
		MvcResult pos1CreateCommodityA = mvc.perform(post("/commoditySync/createEx.bx")//
				.contentType(MediaType.APPLICATION_JSON)//
				.session((MockHttpSession) getLoginSession(1))//
				.param(Commodity.field.getFIELD_NAME_multiPackagingInfo(), barcode1 + ";1;1;1;8;8;" + commodityA + System.currentTimeMillis() % 1000000 + ";") //
				// .param(Commodity.field.getFIELD_NAME_string1(), barcode1 + "," + "222" +
				// System.currentTimeMillis() % 1000000 + ",3332" + System.currentTimeMillis() %
				// 1000000 + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				// + commodityA + System.currentTimeMillis() % 1000000 + "," + commodityB +
				// System.currentTimeMillis() % 1000000 + "," + commodityC +
				// System.currentTimeMillis() % 1000000 + ";") //
				.param(Commodity.field.getFIELD_NAME_name(), "薯片" + System.currentTimeMillis() % 1000000)//
				.param(Commodity.field.getFIELD_NAME_ID(), "" + c.getID()) //
				.param(Commodity.field.getFIELD_NAME_status(), "0") //
				// .param(Commodity.field.getFIELD_NAME_name(), "焦糖瓜子" +
				// String.valueOf(System.currentTimeMillis()).substring(6)) //
				// .param(Commodity.field.getFIELD_NAME_shortName(), "瓜子") //
				.param(Commodity.field.getFIELD_NAME_specification(), "克") //
				.param(Commodity.field.getFIELD_NAME_purchasingUnit(), "箱") //
				.param(Commodity.field.getFIELD_NAME_mnemonicCode(), "SP") //
				.param(Commodity.field.getFIELD_NAME_pricingType(), "1") //
				.param(Commodity.field.getFIELD_NAME_priceVIP(), "0.8") //
				.param(Commodity.field.getFIELD_NAME_priceWholesale(), "0.8") //
				.param(Commodity.field.getFIELD_NAME_priceRetail(), "10.0f")//
				//.param(Commodity.field.getFIELD_NAME_ratioGrossMargin(), "0.8") //
				.param(Commodity.field.getFIELD_NAME_canChangePrice(), "1") //
				.param(Commodity.field.getFIELD_NAME_ruleOfPoint(), "1") //
				.param(Commodity.field.getFIELD_NAME_picture(), "url=116843435555") //
				.param(Commodity.field.getFIELD_NAME_shelfLife(), "1") //
				.param(Commodity.field.getFIELD_NAME_returnDays(), "1") //
				.param(Commodity.field.getFIELD_NAME_purchaseFlag(), "1") //
				.param(Commodity.field.getFIELD_NAME_refCommodityID(), "0") //
				.param(Commodity.field.getFIELD_NAME_refCommodityMultiple(), "0") //
				//.param(Commodity.field.getFIELD_NAME_isGift(), "0") //
				.param(Commodity.field.getFIELD_NAME_tag(), "1") //
				.param(Commodity.field.getFIELD_NAME_NO(), "2421") //
				//.param(Commodity.field.getFIELD_NAME_NOAccumulated(), "1") //
				.param(Commodity.field.getFIELD_NAME_nOStart(), Commodity.NO_START_Default + "") // ...使用Commodity.NO_START_Default。搜索其它有期初值的地方看看是否需要修改
				.param(Commodity.field.getFIELD_NAME_purchasingPriceStart(), Commodity.PURCHASING_PRICE_START_Default + "") // ...使用Commodity.PURCHASING_PRICE_START_Default。搜索其它有期初值的地方看看是否需要修改
				.param(Commodity.field.getFIELD_NAME_type(), "0")//
				.param(Commodity.field.getFIELD_NAME_shortName(), "12131").param("providerIDs", "7,2,3") //
				.param(Commodity.field.getFIELD_NAME_propertyValue1(), "自定义内容1")//
				.param(Commodity.field.getFIELD_NAME_propertyValue2(), "自定义内容2")//
				.param(Commodity.field.getFIELD_NAME_propertyValue3(), "自定义内容3")//
				.param(Commodity.field.getFIELD_NAME_propertyValue4(), "自定义内容4")//
				.param(Commodity.field.getFIELD_NAME_packageUnitID(), "1") //
				.param(Commodity.field.getFIELD_NAME_brandID(), "3")//
				.param(Commodity.field.getFIELD_NAME_categoryID(), "1") //
				.param(Commodity.field.getFIELD_NAME_returnObject(), "1")//
				.param(Commodity.field.getFIELD_NAME_operatorStaffID(), "3")).andExpect(status().isOk()).andDo(print()).andReturn(); //

		String json = pos1CreateCommodityA.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String err = JsonPath.read(o, "$.ERROR");
		if (err.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			return false;
		}

		JSONObject o1 = JSONObject.fromObject(pos1CreateCommodityA.getResponse().getContentAsString()); //
		int i1 = JsonPath.read(o1, "$.object.ID");

		super.objectID1 = i1;

		return true;
	}

	protected boolean createObject2AndObject3() throws Exception {
		Commodity c = new Commodity();

		// 3、pos2 创建Commodity2 和Commodity3 ，创建后添加到普存和同存
		MvcResult pos2CreateCommodityA = mvc.perform(post("/commoditySync/createEx.bx").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) getLoginSession(2)) //
				.param(Commodity.field.getFIELD_NAME_multiPackagingInfo(), barcode1 + ";1;1;1;8;8;" + commodityA + System.currentTimeMillis() % 1000000 + ";") //
				// .param(Commodity.field.getFIELD_NAME_string1(), barcode1 + "," + "222" +
				// System.currentTimeMillis() % 1000000 + ",3332" + System.currentTimeMillis() %
				// 1000000 + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				// + commodityA + System.currentTimeMillis() % 1000000 + "," + commodityB +
				// System.currentTimeMillis() % 1000000 + "," + commodityC +
				// System.currentTimeMillis() % 1000000 + ";") //
				.param(Commodity.field.getFIELD_NAME_name(), "薯片" + System.currentTimeMillis() % 1000000)//
				.param(Commodity.field.getFIELD_NAME_ID(), "" + c.getID()) //
				.param(Commodity.field.getFIELD_NAME_status(), "0") //
				// .param(Commodity.field.getFIELD_NAME_name(), "焦糖瓜子" +
				// String.valueOf(System.currentTimeMillis()).substring(6)) //
				// .param(Commodity.field.getFIELD_NAME_shortName(), "瓜子") //
				.param(Commodity.field.getFIELD_NAME_specification(), "克") //
				.param(Commodity.field.getFIELD_NAME_purchasingUnit(), "箱") //
				.param(Commodity.field.getFIELD_NAME_mnemonicCode(), "SP") //
				.param(Commodity.field.getFIELD_NAME_pricingType(), "1") //
				.param(Commodity.field.getFIELD_NAME_priceVIP(), "0.8") //
				.param(Commodity.field.getFIELD_NAME_priceRetail(), "10.0f") //
				.param(Commodity.field.getFIELD_NAME_priceWholesale(), "0.8") //
				//.param(Commodity.field.getFIELD_NAME_ratioGrossMargin(), "0.8") //
				.param(Commodity.field.getFIELD_NAME_canChangePrice(), "1") //
				.param(Commodity.field.getFIELD_NAME_ruleOfPoint(), "1") //
				.param(Commodity.field.getFIELD_NAME_picture(), "url=116843435555") //
				.param(Commodity.field.getFIELD_NAME_shelfLife(), "1") //
				.param(Commodity.field.getFIELD_NAME_returnDays(), "1") //
				.param(Commodity.field.getFIELD_NAME_purchaseFlag(), "1") //
				.param(Commodity.field.getFIELD_NAME_refCommodityID(), "0") //
				.param(Commodity.field.getFIELD_NAME_refCommodityMultiple(), "0") //
				//.param(Commodity.field.getFIELD_NAME_isGift(), "0") //
				.param(Commodity.field.getFIELD_NAME_tag(), "1") //
				.param(Commodity.field.getFIELD_NAME_NO(), "2421") //
				//.param(Commodity.field.getFIELD_NAME_NOAccumulated(), "1") //
				.param(Commodity.field.getFIELD_NAME_nOStart(), Commodity.NO_START_Default + "") // ...使用Commodity.NO_START_Default。搜索其它有期初值的地方看看是否需要修改
				.param(Commodity.field.getFIELD_NAME_purchasingPriceStart(), Commodity.PURCHASING_PRICE_START_Default + "") // ...使用Commodity.PURCHASING_PRICE_START_Default。搜索其它有期初值的地方看看是否需要修改
				.param(Commodity.field.getFIELD_NAME_type(), "0")//
				.param(Commodity.field.getFIELD_NAME_shortName(), "12131").param("providerIDs", "7,2,3") //
				.param(Commodity.field.getFIELD_NAME_propertyValue1(), "自定义内容1")//
				.param(Commodity.field.getFIELD_NAME_propertyValue2(), "自定义内容2")//
				.param(Commodity.field.getFIELD_NAME_propertyValue3(), "自定义内容3")//
				.param(Commodity.field.getFIELD_NAME_propertyValue4(), "自定义内容4")//
				.param(Commodity.field.getFIELD_NAME_packageUnitID(), "1") //
				.param(Commodity.field.getFIELD_NAME_brandID(), "3")//
				.param(Commodity.field.getFIELD_NAME_categoryID(), "1") //
				.param(Commodity.field.getFIELD_NAME_returnObject(), "1")//
				.param(Commodity.field.getFIELD_NAME_operatorStaffID(), "3")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		String json = pos2CreateCommodityA.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String err = JsonPath.read(o, "$.ERROR");
		if (err.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			return false;
		}

		MvcResult pos2CreateCommodityB = mvc.perform(post("/commoditySync/createEx.bx").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) getLoginSession(2)) //
				.param(Commodity.field.getFIELD_NAME_multiPackagingInfo(), barcode1 + ";1;1;1;8;8;" + commodityA + System.currentTimeMillis() % 1000000 + ";") //
				// .param(Commodity.field.getFIELD_NAME_string1(), barcode1 + "," + "222" +
				// System.currentTimeMillis() % 1000000 + ",3332" + System.currentTimeMillis() %
				// 1000000 + ";1,2,3;1,5,10;1,5,10;8,3,8;8,8,8;" //
				// + commodityA + System.currentTimeMillis() % 1000000 + "," + commodityB +
				// System.currentTimeMillis() % 1000000 + "," + commodityC +
				// System.currentTimeMillis() % 1000000 + ";") //
				.param(Commodity.field.getFIELD_NAME_name(), "薯片" + System.currentTimeMillis() % 1000000)//
				.param(Commodity.field.getFIELD_NAME_ID(), "" + c.getID()) //
				.param(Commodity.field.getFIELD_NAME_status(), "0") //
				// .param(Commodity.field.getFIELD_NAME_name(), "焦糖瓜子" +
				// String.valueOf(System.currentTimeMillis()).substring(6)) //
				// .param(Commodity.field.getFIELD_NAME_shortName(), "瓜子") //
				.param(Commodity.field.getFIELD_NAME_specification(), "克") //
				.param(Commodity.field.getFIELD_NAME_purchasingUnit(), "箱") //
				.param(Commodity.field.getFIELD_NAME_mnemonicCode(), "SP") //
				.param(Commodity.field.getFIELD_NAME_pricingType(), "1") //
				.param(Commodity.field.getFIELD_NAME_priceVIP(), "0.8") //
				.param(Commodity.field.getFIELD_NAME_priceRetail(), "10.0f") //
				.param(Commodity.field.getFIELD_NAME_priceWholesale(), "0.8") //
				//.param(Commodity.field.getFIELD_NAME_ratioGrossMargin(), "0.8") //
				.param(Commodity.field.getFIELD_NAME_canChangePrice(), "1") //
				.param(Commodity.field.getFIELD_NAME_ruleOfPoint(), "1") //
				.param(Commodity.field.getFIELD_NAME_picture(), "url=116843435555") //
				.param(Commodity.field.getFIELD_NAME_shelfLife(), "1") //
				.param(Commodity.field.getFIELD_NAME_returnDays(), "1") //
				.param(Commodity.field.getFIELD_NAME_purchaseFlag(), "1") //
				.param(Commodity.field.getFIELD_NAME_refCommodityID(), "0") //
				.param(Commodity.field.getFIELD_NAME_refCommodityMultiple(), "0") //
				//.param(Commodity.field.getFIELD_NAME_isGift(), "0") //
				.param(Commodity.field.getFIELD_NAME_tag(), "1") //
				.param(Commodity.field.getFIELD_NAME_NO(), "2421") //
				//.param(Commodity.field.getFIELD_NAME_NOAccumulated(), "1") //
				.param(Commodity.field.getFIELD_NAME_nOStart(), Commodity.NO_START_Default + "") // ...使用Commodity.NO_START_Default。搜索其它有期初值的地方看看是否需要修改
				.param(Commodity.field.getFIELD_NAME_purchasingPriceStart(), Commodity.PURCHASING_PRICE_START_Default + "") // ...使用Commodity.PURCHASING_PRICE_START_Default。搜索其它有期初值的地方看看是否需要修改
				.param(Commodity.field.getFIELD_NAME_type(), "0")//
				.param(Commodity.field.getFIELD_NAME_shortName(), "12131").param("providerIDs", "7,2,3") //
				.param(Commodity.field.getFIELD_NAME_propertyValue1(), "自定义内容1")//
				.param(Commodity.field.getFIELD_NAME_propertyValue2(), "自定义内容2")//
				.param(Commodity.field.getFIELD_NAME_propertyValue3(), "自定义内容3")//
				.param(Commodity.field.getFIELD_NAME_propertyValue4(), "自定义内容4")//
				.param(Commodity.field.getFIELD_NAME_packageUnitID(), "1") //
				.param(Commodity.field.getFIELD_NAME_brandID(), "3")//
				.param(Commodity.field.getFIELD_NAME_categoryID(), "1") //
				.param(Commodity.field.getFIELD_NAME_operatorStaffID(), "1")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		String json1 = pos2CreateCommodityB.getResponse().getContentAsString();
		JSONObject o1 = JSONObject.fromObject(json1);
		String err1 = JsonPath.read(o1, "$.ERROR");
		if (err1.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			return false;
		}

		return true;
	}

}
