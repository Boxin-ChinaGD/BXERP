package com.bx.erp.sit.sit1.sg.purchasingOrder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.commodity.CommoditySyncAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CommodityCP;
import com.bx.erp.test.checkPoint.CompanyCP;
import com.bx.erp.test.checkPoint.PurchasingOrderCP;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class PurchasingOrderListTest extends BaseActionTest {
	protected AtomicInteger order;
	protected AtomicLong barcode;
	protected AtomicInteger commodityOrder;
	//
	protected String bossPhone = "";
	protected String companySN = "";
	protected String dbName = "";
	protected final String CommonCommodity = "????????????";
	//
	private static final String CommIDs = "commIDs";
	private static final String BarcodeIDs = "barcodeIDs";
	private static final String NOs = "NOs";
	private static final String PriceSuggestions = "priceSuggestions";
	private static final String CommNOs = "commNOs";
	private static final String CommPrices = "commPrices";
	private static final String CommPurchasingUnit = "commPurchasingUnit";
	private static final String ProviderID = "providerID";
	private static final String Amounts = "amounts";
	private Map<String, BaseModel> purchasingOrderListMap;

	/** ??????????????????1????????????MvcResult ?????????company????????????????????????????????????????????????????????????????????????????????????
	 * ???????????????????????????????????????????????????company???????????? */
	private static MvcResult mvcResult_Company;

	// SIT1_nbr_SG_PurchasingOrderList_1
	@Test
	public void retrieveNPurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????????????????????????????");
		// ????????????2
		Company company = BaseCompanyTest.DataInput.getCompany();
		bossPhone = company.getBossPhone();
		dbName = company.getDbName();
		BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType);
		MvcResult mr = mvc.perform(BaseCompanyTest.getBuilder("/company/createEx.bx", MediaType.APPLICATION_JSON, company, sessionOP) //
				.session((MockHttpSession) sessionOP)//
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr);
		//
		// ????????????????????????????????????????????????????????????????????????????????????MvcResult???????????????????????????????????????????????????????????????
		mvcResult_Company = mr;
		purchasingOrderListMap.put("company", company);
		JSONObject o1 = JSONObject.fromObject(mvcResult_Company.getResponse().getContentAsString());
		String companySN_New = JsonPath.read(o1, "$.object.SN");
		companySN = companySN_New;
		//
		sessionBoss = Shared.getStaffLoginSession(mvc, company.getBossPhone(), company.getBossPassword(), companySN_New);
		// ??????????????????
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr1);
	}

	// SIT1_nbr_SG_PurchasingOrderList_39
	@Test(dependsOnMethods = "retrieveNPurchasingOrder")
	public void preSaleOperate() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????????????????????????????CURD??????");

		Company company = (Company) purchasingOrderListMap.get("company");
		JSONObject o1 = JSONObject.fromObject(mvcResult_Company.getResponse().getContentAsString());
		String companySN_New = JsonPath.read(o1, "$.object.SN");
		// ???????????????
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, companySN_New))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
		//
		// ???company????????????
		CompanyCP.verifyCreate(mvcResult_Company, company, companyBO, staffBO, shopBO, Shared.BXDBName_Test);
		CompanyCP.verifyUploadBusinessLicensePicture(mvcResult_Company, company);
		// ??????????????????????????????
		BaseCompanyTest.checkSensitiveInformation(mvcResult_Company);
		// ????????????????????????????????????????????????????????????????????????????????????????????????
		BaseCompanyTest.ValidateIfPreSaleAccountIsDeleted(dbName, mvc, mapBO, companySN_New, company.getBossPhone(), company.getBossPassword());
		// ???????????????BOSS????????????SESSION
		try {
			sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, BaseCompanyTest.bossPassword_New, companySN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// SIT1_nbr_SG_PurchasingOrderList_2
	@Test(dependsOnMethods = "preSaleOperate")
	public void createPurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????????????????");
		// ??????????????????54
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName(CommonCommodity + commodityOrder);
		c.setMultiPackagingInfo(barcode + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ????????????????????????
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbName);
		// ??????????????????1
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr1);
		JSONObject object = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder1 = new PurchasingOrder();
		purchasingOrder1 = (PurchasingOrder) purchasingOrder1.parse1(object.getString(BaseAction.KEY_Object));
		purchasingOrderListMap.put("purchasingOrder1", purchasingOrder1);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder2 = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr1, purchasingOrder2, purchasingOrderBO, dbName);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_3
	@Test(dependsOnMethods = "createPurchasingOrder")
	public void updateCommodityNOAndPrice1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????????????????????????????????????????????????????");

		// ??????????????????2
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		PurchasingOrderCommodity poc = (PurchasingOrderCommodity) po1.getListSlave1().get(0);
		poc.setCommodityNO(20);
		poc.setPriceSuggestion(12.5);
		// ??????????????????2
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/updateEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "20")//
						.param(PriceSuggestions, "12.5")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder2 = new PurchasingOrder();
		purchasingOrder2.parse1(jsonObject.getString(BaseAction.KEY_Object));
		purchasingOrderListMap.put("purchasingOrder2", purchasingOrder2);
		// ??????????????????????????????
		PurchasingOrderCP.verifyUpdate(mr1, po1, purchasingOrderBO, dbName);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_4
	@Test(dependsOnMethods = "updateCommodityNOAndPrice1")
	public void updateCommodityNOAndPrice2() {// ?????????????????????
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????????????????????????????????????????????????????????????????");

		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_5
	@Test(dependsOnMethods = "updateCommodityNOAndPrice2")
	public void createAndDeletePurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????????????????????????????????????????????????????");

		// ??????????????????3
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		deleteExPurchasingOrder(po1);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//

		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_6
	@Test(dependsOnMethods = "createAndDeletePurchasingOrder")
	public void createAndupdateAfterApprovePurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????????????????????????????????????????????????????");

		// ??????????????????4
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ???????????????????????????4
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "20")//
						.param(PriceSuggestions, "12.5")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder4 = new PurchasingOrder();
		purchasingOrder4 = (PurchasingOrder) purchasingOrder4.parse1(jsonObject.getString(BaseAction.KEY_Object));
		purchasingOrderListMap.put("purchasingOrder4", purchasingOrder4);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_7
	@Test(dependsOnMethods = "createAndupdateAfterApprovePurchasingOrder")
	public void createAndApprovePurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????????????????????????????????????????????????????");

		// ??????????????????5
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????5
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder5 = new PurchasingOrder();
		purchasingOrder5 = (PurchasingOrder) purchasingOrder5.parse1(jsonObject.getString(BaseAction.KEY_Object));
		purchasingOrderListMap.put("purchasingOrder5", purchasingOrder5);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_8
	@Test(dependsOnMethods = "createAndApprovePurchasingOrder")
	public void approveAfterRetrievePurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????????????????????????????????????????");

		// ??????????????????6
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????6
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder6 = new PurchasingOrder();
		purchasingOrder6 = (PurchasingOrder) purchasingOrder6.parse1(jsonObject.getString(BaseAction.KEY_Object));
		purchasingOrderListMap.put("purchasingOrder6", purchasingOrder6);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ?????????????????????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_9
	@Test(dependsOnMethods = "approveAfterRetrievePurchasingOrder")
	public void createPurchasingOrderAfterApproveAfterWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????????????????????????????????????????");

		// ??????????????????7
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$." + BaseAction.KEY_Object).toString());
		Assert.assertTrue(po1 != null, "?????????????????????");
		// ??????????????????7
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder7 = new PurchasingOrder();
		purchasingOrder7 = (PurchasingOrder) purchasingOrder7.parse1(jsonObject.getString(BaseAction.KEY_Object));
		purchasingOrderListMap.put("purchasingOrder7", purchasingOrder7);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ???????????????1
		Warehousing w = new Warehousing();
		w.setPurchasingOrderID(po.getID());
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(CommIDs, "1") //
						.param(CommNOs, "30") //
						.param(CommPrices, "12") //
						.param(Amounts, "12") //
						.param(BarcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ???????????????????????????
		WarehousingCP.verifyCreate(mr2, w, dbName);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_10
	@Test(dependsOnMethods = "createPurchasingOrderAfterApproveAfterWarehousing")
	public void createPurchasingOrderAfterApproveAfterWarehousingAfterRetrieveNPurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "?????????????????????????????????????????????????????????????????????????????????");
		// ??????????????????8
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????8
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ???????????????2
		Warehousing w = new Warehousing();
		w.setPurchasingOrderID(po.getID());
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param(CommIDs, "1") //
						.param(CommNOs, "30") //
						.param(CommPrices, "12") //
						.param(Amounts, "12") //
						.param(BarcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ???????????????????????????
		WarehousingCP.verifyCreate(mr2, w, dbName);
		//
		JSONObject o1 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(o1.getString(BaseAction.KEY_Object));
		warehousing1.setStatus(1);
		// ?????????????????????????????????????????????
		Commodity commodity1 = new Commodity();
		commodity1.setID(1);
		Commodity commodityCache1 = BaseCommodityTest.getCommodityCache(commodity1.getID(), dbName);
		List<Commodity> commList = new ArrayList<>();
		commList.add(commodityCache1);
		// ???????????????2
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing1.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing1.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing1.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0").contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		// ???????????????????????????
		WarehousingCP.verifyApprove(mr3, warehousing1, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);
		// ?????????????????????????????????
		MvcResult mr4 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr4);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_11
	@Test(dependsOnMethods = "createPurchasingOrderAfterApproveAfterWarehousingAfterRetrieveNPurchasingOrder")
	public void approvePurchasingOrderAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????????????????????????????????????????");

		// ??????????????????9
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????9
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ??????????????????9
		MvcResult mr2 = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + po.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_12
	@Test(dependsOnMethods = "approvePurchasingOrderAfterDelete")
	public void approvePurchasingOrderAfterWarehousingAfterDeletePurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????????????????????????????????????????????????????????????????????????????");

		// ??????????????????10
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????10
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ???????????????3
		Warehousing w = new Warehousing();
		w.setPurchasingOrderID(po.getID());
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param(CommIDs, "1") //
						.param(CommNOs, "30") //
						.param(CommPrices, "12") //
						.param(Amounts, "12") //
						.param(BarcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ???????????????????????????
		WarehousingCP.verifyCreate(mr2, w, dbName);
		// ??????????????????9
		MvcResult mr3 = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + po.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_13
	@Test(dependsOnMethods = "approvePurchasingOrderAfterWarehousingAfterDeletePurchasingOrder")
	public void deleteAfterRetrieve1ThisPurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????????????????????????????????????????");

		// ??????????????????11
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????10
		deleteExPurchasingOrder(po1);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	protected void deleteExPurchasingOrder(PurchasingOrder po1) throws Exception, UnsupportedEncodingException {
		MvcResult mr1 = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + po1.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		// PurchasingOrderCP.verifyDelete(po1, purchasingOrderBO, dbName);
	}

	// SIT1_nbr_SG_PurchasingOrderList_14
	@Test(dependsOnMethods = "deleteAfterRetrieve1ThisPurchasingOrder")
	public void retrieve1PurchasingOrderWhenUpdateCommodity() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????????????????????????????????????????????????????????????????");

		// ??????????????????1
		PurchasingOrder purchasingOrder = (PurchasingOrder) purchasingOrderListMap.get("purchasingOrder1");
		PurchasingOrder purchasingOrder1 = R1PurchasingOrder(purchasingOrder.getID());
		purchasingOrder1.setRemark("1");
		PurchasingOrderCommodity poc = (PurchasingOrderCommodity) purchasingOrder1.getListSlave1().get(0);
		poc.setCommodityNO(10);
		poc.setPriceSuggestion(12.5);
		// ??????????????????1
		MvcResult mr = mvc.perform( //
				post("/purchasingOrder/updateEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), String.valueOf(purchasingOrder1.getRemark()))//
						.param(CommIDs, "1")//
						.param(NOs, "10")//
						.param(PriceSuggestions, "12.5")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrderCP.verifyUpdate(mr, purchasingOrder1, purchasingOrderBO, dbName);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	protected PurchasingOrder R1PurchasingOrder(int purchasingOrdeID) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform( //
				get("/purchasingOrder/retrieve1Ex.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + purchasingOrdeID) //
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());

		return po1;
	}

	// SIT1_nbr_SG_PurchasingOrderList_15
	@Test(dependsOnMethods = "retrieve1PurchasingOrderWhenUpdateCommodity")
	public void retrieve1PurchasingOrderWhenUpdateCommodityAfterRetrieveNPurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");

		// ??????????????????55
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName(CommonCommodity + getCommodityOrder());
		c.setMultiPackagingInfo(getBarcode() + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		MvcResult mr = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/createEx.bx", MediaType.APPLICATION_JSON, c, sessionBoss) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ????????????????????????
		CommodityCP.verifyCreate(mr, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				dbName);
		// ??????????????????2
		PurchasingOrder po2 = (PurchasingOrder) purchasingOrderListMap.get("purchasingOrder2");
		PurchasingOrder purchasingOrder2 = R1PurchasingOrder(po2.getID());
		PurchasingOrderCommodity poc = (PurchasingOrderCommodity) purchasingOrder2.getListSlave1().get(0);
		poc.setCommodityID(2);
		poc.setBarcodeID(2);
		poc.setCommodityNO(10);
		poc.setPriceSuggestion(12.5);
		// ??????????????????2
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/updateEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder2.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder2.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), String.valueOf(purchasingOrder2.getRemark()))//
						.param(CommIDs, "2")//
						.param(NOs, "10")//
						.param(PriceSuggestions, "12.5")//
						.param(BarcodeIDs, "2")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrderCP.verifyUpdate(mr1, purchasingOrder2, purchasingOrderBO, dbName);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), "????????????54")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_16
	@Test(dependsOnMethods = "retrieve1PurchasingOrderWhenUpdateCommodityAfterRetrieveNPurchasingOrder")
	public void deletePurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????????????????????????????????????????????????????????????????");

		PurchasingOrder purchasingOrder1 = (PurchasingOrder) purchasingOrderListMap.get("purchasingOrder1");
		// ??????????????????1
		MvcResult mr = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + purchasingOrder1.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrderCP.verifyDelete(purchasingOrder1, purchasingOrderBO, dbName);
		// ??????????????????
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_17
	@Test(dependsOnMethods = "deletePurchasingOrder")
	public void deletePurchasingOrderAfterRetrieveNThisPurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????????????????????????????????????????????????????????????????????????????????????????");

		PurchasingOrder purchasingOrder2 = (PurchasingOrder) purchasingOrderListMap.get("purchasingOrder2");
		// ??????????????????2
		MvcResult mr = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + purchasingOrder2.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrderCP.verifyDelete(purchasingOrder2, purchasingOrderBO, dbName);
		// ??????????????????2
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), "????????????2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_18
	@Test(dependsOnMethods = "deletePurchasingOrderAfterRetrieveNThisPurchasingOrder")
	public void approvePurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????????????????????????????????????????????????????");

		// ??????????????????12
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????12
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_19
	@Test(dependsOnMethods = "approvePurchasingOrder")
	public void approvePurchasingOrderAfterRetrieveNForApproveStatusPurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????????????????????????????????????????????????????????????????????????????");

		// ??????????????????13
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????13
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ??????????????????13
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_20
	@Test(dependsOnMethods = "approvePurchasingOrderAfterRetrieveNForApproveStatusPurchasingOrder")
	public void approvePurchasingOrderAfterWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");

		// ??????????????????14
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????14
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ???????????????4
		Warehousing w = new Warehousing();
		w.setPurchasingOrderID(po1.getID());
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(CommIDs, "1") //
						.param(CommNOs, "30") //
						.param(CommPrices, "12") //
						.param(Amounts, "12") //
						.param(BarcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		// ???????????????????????????
		WarehousingCP.verifyCreate(mr2, w, dbName);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_21
	@Test(dependsOnMethods = "approvePurchasingOrderAfterWarehousing")
	public void warehousingPurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????????????????4???????????????????????????????????????????????????");

		// ???????????????5
		Warehousing w = new Warehousing();
		w.setPurchasingOrderID(purchasingOrderListMap.get("purchasingOrder4").getID());
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param(CommIDs, "1") //
						.param(CommNOs, "30") //
						.param(CommPrices, "12") //
						.param(Amounts, "12") //
						.param(BarcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ???????????????????????????
		WarehousingCP.verifyCreate(mr, w, dbName);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_22
	@Test(dependsOnMethods = "warehousingPurchasingOrder")
	public void partWarehousingPurchasingOrderAfterRetrieveNForApproveStatusPurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????????????????5?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");

		// ???????????????6
		Warehousing w = new Warehousing();
		w.setPurchasingOrderID(purchasingOrderListMap.get("purchasingOrder5").getID());
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param(CommIDs, "1") //
						.param(CommNOs, "10") //
						.param(CommPrices, "12") //
						.param(Amounts, "12") //
						.param(BarcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ???????????????????????????
		WarehousingCP.verifyCreate(mr, w, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(o.getString(BaseAction.KEY_Object));
		warehousing1.setStatus(1);
		// ?????????????????????????????????????????????
		Commodity commodity = new Commodity();
		commodity.setID(1);
		Commodity commodityCache = BaseCommodityTest.getCommodityCache(commodity.getID(), dbName);
		List<Commodity> commList = new ArrayList<>();
		commList.add(commodityCache);
		// ???????????????6
		MvcResult mr1 = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr1);
		// ???????????????????????????
		WarehousingCP.verifyApprove(mr1, warehousing1, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);
		// ??????????????????6
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_23
	@Test(dependsOnMethods = "partWarehousingPurchasingOrderAfterRetrieveNForApproveStatusPurchasingOrder")
	public void warehousingPurchasingOrderAfterRetrieveNForApproveStatusPurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????????????????6???????????????????????????????????????????????????????????????????????????????????????????????????????????????");

		// ???????????????7
		Warehousing w = new Warehousing();
		w.setPurchasingOrderID(purchasingOrderListMap.get("purchasingOrder6").getID());
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param(CommIDs, "1") //
						.param(CommNOs, "30") //
						.param(CommPrices, "12") //
						.param(Amounts, "12") //
						.param(BarcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ???????????????????????????
		WarehousingCP.verifyCreate(mr, w, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(o.getString(BaseAction.KEY_Object));
		warehousing1.setStatus(1);
		// ?????????????????????????????????????????????
		Commodity commodity = new Commodity();
		commodity.setID(1);
		Commodity commodityCache = BaseCommodityTest.getCommodityCache(commodity.getID(), dbName);
		List<Commodity> commList = new ArrayList<>();
		commList.add(commodityCache);
		// ???????????????7
		MvcResult mr1 = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr1);
		// ???????????????????????????
		WarehousingCP.verifyApprove(mr1, warehousing1, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_24
	@Test(dependsOnMethods = "warehousingPurchasingOrderAfterRetrieveNForApproveStatusPurchasingOrder")
	public void updateInPartWarehousingPurchasingOrder() { // ???????????????????????????????????????
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????????????????????????????????????????????????????????????????");
	}

	// SIT1_nbr_SG_PurchasingOrderList_25
	@Test(dependsOnMethods = "updateInPartWarehousingPurchasingOrder")
	public void updateInApprovePurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????????????????7?????????????????????????????????????????????????????????????????????????????????????????????????????????");

		// ???????????????8
		Warehousing w = new Warehousing();
		w.setPurchasingOrderID(purchasingOrderListMap.get("purchasingOrder7").getID());
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param(CommIDs, "1") //
						.param(CommNOs, "20") //
						.param(CommPrices, "12") //
						.param(Amounts, "12") //
						.param(BarcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ???????????????????????????
		WarehousingCP.verifyCreate(mr, w, dbName);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_26
	@Test(dependsOnMethods = "updateInApprovePurchasingOrder")
	public void pageUp() {// ????????????????????????????????????????????????order+1
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????????????????????????????????????????(????????????????????????????????????????????????order+1)");
	}

	// SIT1_nbr_SG_PurchasingOrderList_27
	@Test(dependsOnMethods = "pageUp")
	public void pageUpAfterCreatePurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????????????????????????????15");

		// ????????????????????????
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(PurchasingOrder.field.getFIELD_NAME_iPageIndex(), "2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????15
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder15 = new PurchasingOrder();
		purchasingOrder15 = (PurchasingOrder) purchasingOrder15.parse1(jsonObject.getString(BaseAction.KEY_Object));
		purchasingOrderListMap.put("purchasingOrder15", purchasingOrder15);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr1, purchasingOrder, purchasingOrderBO, dbName);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_28
	@Test(dependsOnMethods = "pageUpAfterCreatePurchasingOrder")
	public void retrieveNAfterPageUpAfterCreatePurchasingOrder() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "?????????????????????????????????????????????????????????16");

		// ????????????????????????
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(PurchasingOrder.field.getFIELD_NAME_staffID(), "2").param(PurchasingOrder.field.getFIELD_NAME_iPageIndex(), "2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????16
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr1);
		JSONObject jsonObject = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder16 = new PurchasingOrder();
		purchasingOrder16 = (PurchasingOrder) purchasingOrder16.parse1(jsonObject.getString(BaseAction.KEY_Object));
		purchasingOrderListMap.put("purchasingOrder16", purchasingOrder16);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr1, purchasingOrder, purchasingOrderBO, dbName);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_29
	@Test(dependsOnMethods = "retrieveNAfterPageUpAfterCreatePurchasingOrder")
	public void retrieveNAfterPageUpAfterCreatePurchasingOrderAfterApprove() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "?????????????????????????????????????????????????????????17?????????");

		// ????????????????????????
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(PurchasingOrder.field.getFIELD_NAME_staffID(), "2")//
						.param(PurchasingOrder.field.getFIELD_NAME_iPageIndex(), "2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????17
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr1, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????17
		MvcResult mr2 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr2);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_30
	@Test(dependsOnMethods = "retrieveNAfterPageUpAfterCreatePurchasingOrderAfterApprove")
	public void retrieveNAfterPageUpAfterCreatePurchasingOrderAfterApproveAfterWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "?????????????????????????????????????????????????????????18????????????????????????9");

		// ????????????????????????
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(PurchasingOrder.field.getFIELD_NAME_staffID(), "2").param(PurchasingOrder.field.getFIELD_NAME_iPageIndex(), "2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????18
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr1, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????18
		MvcResult mr2 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr2);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ???????????????9
		Warehousing w = new Warehousing();
		w.setPurchasingOrderID(po1.getID());
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param(CommIDs, "1") //
						.param(CommNOs, "20") //
						.param(CommPrices, "12") //
						.param(Amounts, "12") //
						.param(BarcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		// ???????????????????????????
		WarehousingCP.verifyCreate(mr3, w, dbName);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_31
	@Test(dependsOnMethods = "retrieveNAfterPageUpAfterCreatePurchasingOrderAfterApprove")
	public void updatePurchasingOrderWhenPageUp() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????????????????????????????????????????????????????");

		// ????????????????????????
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(PurchasingOrder.field.getFIELD_NAME_iPageIndex(), "2")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_32
	@Test(dependsOnMethods = "updatePurchasingOrderWhenPageUp")
	public void updatePurchasingOrderButNotSave() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????15?????????????????????????????????????????????????????????????????????????????????????????????????????????");

		// ??????????????????15
		PurchasingOrder purchasingOrder = (PurchasingOrder) purchasingOrderListMap.get("purchasingOrder15");
		PurchasingOrder purchasingOrder15 = R1PurchasingOrder(purchasingOrder.getID());
		// ??????????????????15
		PurchasingOrderCommodity poc = (PurchasingOrderCommodity) purchasingOrder15.getListSlave1().get(0);
		poc.setCommodityID(2);
		poc.setBarcodeID(2);
		MvcResult mr = mvc.perform( //
				post("/purchasingOrder/updateEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder15.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder15.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), String.valueOf(purchasingOrder15.getRemark()))//
						.param(CommIDs, "2")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "2")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrderCP.verifyUpdate(mr, purchasingOrder15, purchasingOrderBO, dbName);
		// ??????????????????
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);

		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_33
	@Test(dependsOnMethods = "updatePurchasingOrderButNotSave")
	public void updatePurchasingOrderAfterRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????16??????????????????????????????????????????????????????");

		// ??????????????????1
		PurchasingOrder purchasingOrder = (PurchasingOrder) purchasingOrderListMap.get("purchasingOrder16");
		PurchasingOrder purchasingOrder16 = R1PurchasingOrder(purchasingOrder.getID());
		// ??????????????????16
		PurchasingOrderCommodity poc = (PurchasingOrderCommodity) purchasingOrder16.getListSlave1().get(0);
		poc.setCommodityID(2);
		poc.setBarcodeID(2);
		MvcResult mr = mvc.perform( //
				post("/purchasingOrder/updateEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder16.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder16.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), String.valueOf(purchasingOrder16.getRemark()))//
						.param(CommIDs, "2")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "2")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrderCP.verifyUpdate(mr, purchasingOrder16, purchasingOrderBO, dbName);
		// ??????????????????
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_34
	@Test(dependsOnMethods = "updatePurchasingOrderAfterRetrieveN")
	public void updatePurchasingOrderAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????????????????15????????????????????????????????????");

		// ??????????????????15
		PurchasingOrder purchasingOrder = (PurchasingOrder) purchasingOrderListMap.get("purchasingOrder15");
		PurchasingOrder purchasingOrder15 = R1PurchasingOrder(purchasingOrder.getID());
		PurchasingOrderCommodity poc = (PurchasingOrderCommodity) purchasingOrder15.getListSlave1().get(0);
		poc.setCommodityID(1);
		poc.setBarcodeID(1);
		poc.setPriceSuggestion(12.5);
		// ??????????????????15
		MvcResult mr = mvc.perform( //
				post("/purchasingOrder/updateEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder15.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder15.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), String.valueOf(purchasingOrder15.getRemark()))//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12.5")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);
		JSONObject jsonObject = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder purchasingOrder15Update = (PurchasingOrder) purchasingOrder15.parse1(jsonObject.getString(BaseAction.KEY_Object));
		// ??????????????????????????????
		PurchasingOrderCP.verifyUpdate(mr, purchasingOrder15Update, purchasingOrderBO, dbName);
		// ??????????????????15
		MvcResult mr1 = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + purchasingOrder15Update.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrderCP.verifyDelete(purchasingOrder15Update, purchasingOrderBO, dbName);
		// ??????????????????15
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), "0015")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_35
	@Test(dependsOnMethods = "updatePurchasingOrderAfterDelete")
	public void deletePurchasingOrderAfterRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "??????????????????16?????????????????????????????????????????????????????????????????????????????????");

		PurchasingOrder po = (PurchasingOrder) purchasingOrderListMap.get("purchasingOrder16");
		PurchasingOrder purchasingOrder16 = R1PurchasingOrder(po.getID());
		// ??????????????????16
		MvcResult mr = mvc.perform(//
				get("/purchasingOrder/deleteEx.bx?" + PurchasingOrder.field.getFIELD_NAME_ID() + "=" + po.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrderCP.verifyDelete(purchasingOrder16, purchasingOrderBO, dbName);
		// ??????????????????16
		MvcResult mr1 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), "0016")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_36
	@Test(dependsOnMethods = "deletePurchasingOrderAfterRetrieveN")
	public void deleteLastPage() {// ????????????????????????????????????????????????order+1
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "?????????????????????????????????????????????(????????????????????????????????????????????????order+1)");
	}

	// SIT1_nbr_SG_PurchasingOrderList_37
	@Test(dependsOnMethods = "deleteLastPage")
	public void newMachineRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????CRUD???????????????????????????nbr????????????????????????????????????????????????????????????");

		// ??????????????????
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);
		// ????????????
	}

	// SIT1_nbr_SG_PurchasingOrderList_38
	@Test(dependsOnMethods = "newMachineRetrieveN")
	public void twoPosMeanwhileDBOper() {// ???????????????
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "???????????????????????????????????????????????????nbr??????CRUD???????????????????????????????????????????????????????????????????????????");
	}

	// SIT1_nbr_SG_PurchasingOrderList_40
	@Test(dependsOnMethods = "twoPosMeanwhileDBOper")
	public void retrieveHistory1() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????1");

		// ??????????????????19
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		// ??????????????????
		Commodity commodity = new Commodity();
		commodity.setID(1);
		Commodity commodityCache = BaseCommodityTest.getCommodityCache(commodity.getID(), dbName);
		// ?????????????????????????????????????????????
		int iOldNO = BaseCommodityTest.queryCommodityHistorySize(commodity.getID(), dbName, commodityHistoryBO);
		// ?????????????????????????????????
		List<Commodity> commList = getMultioleCommodityList(commodityCache);
		// ??????????????????1
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setID(1);
		c.setProviderIDs("1");
		c.setName("???????????????1");
		c.setMultiPackagingInfo("6821423302394" + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		// ????????????????????????????????????
		List<PackageUnit> lspu = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuCreated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuUpdated = new ArrayList<PackageUnit>();
		List<PackageUnit> lspuDeleted = new ArrayList<PackageUnit>();
		CommoditySyncAction.checkPackageUnit(c, lspu, lspuCreated, lspuUpdated, lspuDeleted, dbName, logger, packageUnitBO, commodityBO, barcodesBO, BaseBO.SYSTEM);
		//
		MvcResult mr1 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, sessionBoss) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		CommodityCP.verifyUpdate(mr1, c, posBO, commoditySyncCacheBO, barcodesSyncCacheBO, barcodesBO, warehousingBO, warehousingCommodityBO, commodityBO, subCommodityBO, providerCommodityBO, commodityHistoryBO, packageUnitBO, categoryBO,
				iOldNO, lspuCreated, lspuUpdated, lspuDeleted, commList, dbName, null);
		// ??????????????????
		MvcResult mr2 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						// .param(PurchasingOrder.field.getFIELD_NAME_string1(), c.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		// ????????????
	}

	@SuppressWarnings("unchecked")
	private List<Commodity> getMultioleCommodityList(Commodity commodity) {
		DataSourceContextHolder.setDbName(dbName);
		List<Commodity> listMultiPackageCommodity = (List<Commodity>) commodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_RetrieveNMultiPackageCommodity, commodity);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			assertTrue(false, "????????????????????? DB???????????????");
		}
		return listMultiPackageCommodity;
	}

	private MvcResult uploadPicture() throws FileNotFoundException, IOException, Exception {
		String path = System.getProperty("user.dir") + "\\src\\main\\webapp\\images\\logo.png";
		File file = new File(path);
		String originalFilename = file.getName();
		FileInputStream fis = new FileInputStream(file);
		MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/png", fis);

		Shared.caseLog("???????????????????????????");
		MvcResult mr1 = mvc.perform(//
				fileUpload("/commoditySync/uploadPictureEx.bx")//
						.file(multipartFile).contentType(MediaType.MULTIPART_FORM_DATA) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr1);

		Assert.assertNotNull(sessionBoss.getAttribute(EnumSession.SESSION_CommodityPictureDestination.getName()));
		Assert.assertNotNull(sessionBoss.getAttribute(EnumSession.SESSION_PictureFILE.getName()), "????????????????????????");
		return mr1;
	}

	// SIT1_nbr_SG_PurchasingOrderList_41
	@Test(dependsOnMethods = "retrieveHistory1")
	public void retrieveHistory2() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_PurchasingOrderList_", order, "????????????????????????????????????2");

		// ??????????????????19
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, "1")//
						.param(NOs, "30") //
						.param(PriceSuggestions, "12") //
						.param(CommPurchasingUnit, "???") //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, "1") //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????19
		MvcResult mr1 = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(po1.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(po1.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), po1.getRemark())//
						.param(CommIDs, "1")//
						.param(NOs, "30")//
						.param(PriceSuggestions, "12")//
						.param(BarcodeIDs, "1")//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr1);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(po1, purchasingOrderBO, providerCommodityBO, dbName);
		// ??????????????????1
		MvcResult req = uploadPicture();
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setID(1);
		c.setProviderIDs("1");
		c.setName("??????????????????1");
		c.setMultiPackagingInfo("6821423302394" + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		MvcResult mr2 = mvc.perform(BaseCommodityTest.DataInput.getBuilder("/commoditySync/updateEx.bx", MediaType.APPLICATION_JSON, c, req.getRequest().getSession()) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr2);
		// ??????????????????????????????(??????????????????????????????)
		// ??????????????????
		MvcResult mr3 = mvc.perform(//
				post("/purchasingOrder/retrieveNEx.bx")//
						.param(PurchasingOrder.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS))//
						.param(PurchasingOrder.field.getFIELD_NAME_queryKeyword(), c.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr3);
		// ????????????
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("???????????????staff=" + staff);

		return staff;
	}

	private int getCommodityOrder() {
		return commodityOrder.incrementAndGet();
	}

	private long getBarcode() {
		return barcode.incrementAndGet();
	}

	@BeforeClass
	public void beforeClass() {
		super.setUp();

		order = new AtomicInteger();
		purchasingOrderListMap = new HashMap<String, BaseModel>();
		commodityOrder = new AtomicInteger();
		commodityOrder.set(72);
		barcode = new AtomicLong();
		barcode.set(6821423302507L);
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}
}
