package com.bx.erp.sit.sit1.sg.warehousing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Staff;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCompanyTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.PurchasingOrderCP;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class WarehousingTest extends BaseActionTest {
	protected AtomicInteger order;
	protected AtomicLong barcode;
	protected AtomicInteger commodityOrder;
	//
	// protected final String bossPhone = "18814126900";
	// protected final String bossPassword = "66666666";
	// protected final String companySN = "668868";
	// protected String dbName = "nbr_bxb";
	protected final String CommonCommodity = "????????????";
	protected final String bossPhone = "15854320895";
	protected final String bossPassword = "000000";
	protected final String companySN = "668866";
	protected String dbName = "nbr";
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
	protected Map<String, BaseModel> warehousingMap;

	// SIT1_nbr_SG_Warehousing_1
	@Test
	public void retrieveNWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "??????????????????????????????");

		// ????????????3
		Company company = BaseCompanyTest.DataInput.getCompany();
		sessionBoss = (MockHttpSession) BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType).getRequest().getSession();
		//
		company = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		//
		warehousingMap.put("company", company);
		//
		sessionBoss = Shared.getStaffLoginSession(mvc, company.getBossPhone(), BaseCompanyTest.bossPassword_New, company.getSN());
		// ???????????????
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// ???????????????session??????
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, bossPassword, companySN);
	}

	// SIT1_nbr_SG_Warehousing_2
	@Test(dependsOnMethods = "retrieveNWarehousing")
	public void createWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "??????????????????????????????????????????");

		// ??????????????????56
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName(CommonCommodity + getCommodityOrder() + Shared.generateStringByTime(6));
		c.setMultiPackagingInfo(barcode + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity56 = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcode56 = BaseBarcodesTest.retrieveNBarcodes(commodity56.getID(), dbName);
		warehousingMap.put("barcode56", barcode56);
		warehousingMap.put("commodity56", commodity56);

		// ??????????????????57
		c.setName(CommonCommodity + getCommodityOrder() + Shared.generateStringByTime(6));
		c.setMultiPackagingInfo(getBarcode() + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity57 = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcode57 = BaseBarcodesTest.retrieveNBarcodes(commodity57.getID(), dbName);
		warehousingMap.put("barcode57", barcode57);
		warehousingMap.put("commodity57", commodity57);
		// ??????????????????58
		c.setName(CommonCommodity + getCommodityOrder() + Shared.generateStringByTime(6));
		c.setMultiPackagingInfo(getBarcode() + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity58 = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcode58 = BaseBarcodesTest.retrieveNBarcodes(commodity58.getID(), dbName);
		warehousingMap.put("barcode58", barcode58);
		warehousingMap.put("commodity58", commodity58);

		// ???????????????1
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(commodity58.getID());
		String commNOs = "28";
		String commPrices = "99";
		String amounts = "2772";
		String barcodeIDs = String.valueOf(barcode58.getID());
		Warehousing warehousing1 = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		warehousingMap.put("warehousing1", warehousing1);
		// ???????????????
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_3
	@Test(dependsOnMethods = "createWarehousing")
	public void approveWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "???????????????????????????????????????");

		// ???????????????1
		Warehousing warehousing1 = (Warehousing) warehousingMap.get("warehousing1");
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		approveWarehousing(warehousing1, commIDs);
		// ???????????????
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
	}

	// SIT1_nbr_SG_Warehousing_4
	@Test(dependsOnMethods = "approveWarehousing")
	public void createWarehousingAfterApprove() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "?????????????????????????????????");
		// ???????????????2
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity57").getID());
		String commNOs = "35";
		String commPrices = "66";
		String amounts = "23310";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode57").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????2
		Warehousing w1 = new Warehousing();
		w1.setWarehouseID(1);
		w1.setID(warehousing.getID());
		approveWarehousing(w1, commIDs);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_5
	@Test(dependsOnMethods = "createWarehousingAfterApprove")
	public void createWarehousingAfterApproveAfterRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "?????????????????????????????????????????????????????????");

		// ???????????????3
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		// w.setPurchasingOrderID(8);
		String commIDs = String.valueOf(warehousingMap.get("commodity56").getID());
		String commNOs = "5";
		String commPrices = "299";
		String amounts = "1495";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode56").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????3
		Warehousing w1 = new Warehousing();
		w1.setWarehouseID(1);
		w1.setID(warehousing.getID());
		approveWarehousing(w1, commIDs);
		// ???????????????
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_6
	@Test(dependsOnMethods = "createWarehousingAfterApproveAfterRetrieveN")
	public void createWarehousingAfterRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "???????????????????????????????????????");

		Thread.sleep(2000);
		Warehousing wRN = new Warehousing();
		wRN.setDate1(DatetimeUtil.getDate(new Date(), -1));
		// ???????????????4
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		// w.setPurchasingOrderID(8);
		String commIDs = String.valueOf(warehousingMap.get("commodity56").getID());
		String commNOs = "3";
		String commPrices = "233";
		String amounts = "699";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode56").getID());
		;
		Warehousing warehousing4 = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		warehousingMap.put("warehousing4", warehousing4);
		// ???????????????
		wRN.setStaffID(warehousing4.getApproverID());
		wRN.setStatus(warehousing4.getStatus());
		wRN.setProviderID(warehousing4.getProviderID());
		wRN.setDate2(DatetimeUtil.getDate(new Date(), +1));
		RNBWarehousing(wRN);
		Thread.sleep(2000);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_7
	@Test(dependsOnMethods = "createWarehousingAfterRetrieveN")
	public void createWarehousingAfterApproveAfterRetrieveNStatus() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "?????????????????????????????????????????????????????????");

		// ???????????????5
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		// w.setPurchasingOrderID(8);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "3";
		String commPrices = "211";
		String amounts = "633";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????5
		Warehousing warehousing1 = approveWarehousing(warehousing, commIDs);
		// ???????????????
		Warehousing wRN = new Warehousing();
		wRN.setStatus(warehousing1.getStatus());
		RNBWarehousing(wRN);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_8
	@Test(dependsOnMethods = "createWarehousingAfterApproveAfterRetrieveNStatus")
	public void approveWarehousingAfterRetrieveNStatus() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "???????????????4????????????????????????");

		// ???????????????4
		Warehousing w = new Warehousing();
		w.setWarehouseID(1);
		w.setID(warehousingMap.get("warehousing4").getID());
		String commIDs = String.valueOf(warehousingMap.get("commodity56").getID());
		Warehousing warehousing = approveWarehousing(w, commIDs);
		// ???????????????
		Warehousing wRN = new Warehousing();
		wRN.setStatus(warehousing.getStatus());
		RNBWarehousing(wRN);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_9
	@Test(dependsOnMethods = "approveWarehousingAfterRetrieveNStatus")
	public void pageUp() {// ????????????????????????????????????????????????order+1
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "????????????????????????????????????????????????(????????????????????????????????????????????????order+1)");
	}

	// SIT1_nbr_SG_Warehousing_10
	@Test(dependsOnMethods = "pageUp")
	public void pageUpAfterCreateWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "?????????????????????????????????????????????");

		// ???????????????????????????
		Warehousing wRN = new Warehousing();
		wRN.setPageIndex(2);
		RNWarehousing(wRN.getPageIndex());
		// ???????????????6
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "10";
		String commPrices = "325";
		String amounts = "3250";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing6 = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		warehousingMap.put("warehousing6", warehousing6);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_11
	@Test(dependsOnMethods = "pageUpAfterCreateWarehousing")
	public void pageUpAfterApproveWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "?????????????????????????????????????????????");

		// ???????????????????????????
		Warehousing wRN = new Warehousing();
		wRN.setPageIndex(2);
		RNWarehousing(wRN.getPageIndex());
		// ???????????????6
		Warehousing w = new Warehousing();
		w.setWarehouseID(1);
		w.setID(warehousingMap.get("warehousing6").getID());
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		approveWarehousing(w, commIDs);

		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_12
	@Test(dependsOnMethods = "pageUpAfterApproveWarehousing")
	public void retrieveNAfterCreateWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "????????????????????????????????????");

		// ???????????????
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// ???????????????7
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity56").getID());
		String commNOs = "6";
		String commPrices = "666";
		String amounts = "3996";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode56").getID());
		Warehousing warehousing7 = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		warehousingMap.put("warehousing7", warehousing7);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_13
	@Test(dependsOnMethods = "retrieveNAfterCreateWarehousing")
	public void retrieveNAfterPageUpAfterCreateWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "????????????????????????????????????????????????");

		// ???????????????
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// ???????????????????????????
		Warehousing wRN1 = new Warehousing();
		wRN1.setPageIndex(2);
		RNWarehousing(wRN1.getPageIndex());
		// ???????????????8
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "10";
		String commPrices = "325";
		String amounts = "3250";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing8 = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		warehousingMap.put("warehousing8", warehousing8);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_14
	@Test(dependsOnMethods = "retrieveNAfterPageUpAfterCreateWarehousing")
	public void retrieveNAfterApproveWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "????????????????????????????????????");

		// ???????????????
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// ???????????????7
		Warehousing w = new Warehousing();
		w.setWarehouseID(1);
		w.setID(warehousingMap.get("warehousing7").getID());
		String commIDs = String.valueOf(warehousingMap.get("commodity56").getID());
		approveWarehousing(w, commIDs);

		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_15
	@Test(dependsOnMethods = "retrieveNAfterApproveWarehousing")
	public void retrieveNAfterPageUpAfterApproveWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "????????????????????????????????????????????????");

		// ???????????????
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// ???????????????????????????
		Warehousing wRN1 = new Warehousing();
		wRN1.setPageIndex(2);
		RNWarehousing(wRN1.getPageIndex());
		// ???????????????8
		Warehousing w = new Warehousing();
		w.setWarehouseID(1);
		w.setID(warehousingMap.get("warehousing8").getID());
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		approveWarehousing(w, commIDs);

		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_16
	@Test(dependsOnMethods = "retrieveNAfterPageUpAfterApproveWarehousing")
	public void retrieveNWarehousingAfterKeep() {// ?????????????????????
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "???????????????????????????????????????");

		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_17
	@Test(dependsOnMethods = "retrieveNWarehousingAfterKeep")
	public void createWarehousingAfterRemove() {// ????????????????????????????????????????????????order+1
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "???????????????????????????????????????(????????????????????????????????????????????????order+1)");

		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_18
	@Test(dependsOnMethods = "createWarehousingAfterRemove")
	public void newMachineRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "???????????????CRUD???????????????????????????nbr????????????????????????????????????????????????????????????");

		RNWarehousing(1);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_19
	@Test(dependsOnMethods = "newMachineRetrieveN")
	public void twoPosMeanwhileDBOper() {// ???????????????
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "???????????????????????????????????????????????????nbr??????CRUD???????????????????????????????????????????????????????????????????????????");
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_20
	@Test(dependsOnMethods = "twoPosMeanwhileDBOper")
	public void preSaleOperate() {// ??????????????????????????????
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "??????????????????CRUD??????????????????");

		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_21
	@Test(dependsOnMethods = "preSaleOperate")
	public void createWarehousingAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "????????????????????????????????????");

		// ???????????????9
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "11";
		String commPrices = "333";
		String amounts = "3663";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????9
		deleteWarehousing(warehousing);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_22
	@Test(dependsOnMethods = "createWarehousingAfterDelete")
	public void createWarehousingAfterApproveAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "????????????????????????????????????????????????");

		// ???????????????10
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "11";
		String commPrices = "333";
		String amounts = "3663";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????10
		approveWarehousing(warehousing, commIDs);
		// ???????????????10
		MvcResult mr = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + warehousing.getID())
						//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_23
	@Test(dependsOnMethods = "createWarehousingAfterApproveAfterDelete")
	public void createWarehousingAfterUpdateAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "????????????????????????????????????????????????");

		// ???????????????11
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "11";
		String commPrices = "333";
		String amounts = "3663";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????11
		commIDs = String.valueOf(warehousingMap.get("commodity56").getID()) + "," + String.valueOf(warehousingMap.get("commodity57").getID()) + "," + String.valueOf(warehousingMap.get("commodity58").getID());
		barcodeIDs = String.valueOf(warehousingMap.get("barcode56").getID()) + "," + String.valueOf(warehousingMap.get("barcode57").getID()) + "," + String.valueOf(warehousingMap.get("barcode58").getID());
		commNOs = "100,100,20";
		commPrices = "1.1,1.2,1.3";
		amounts = "200,200,200";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????11
		deleteWarehousing(warehousing);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_24
	@Test(dependsOnMethods = "createWarehousingAfterUpdateAfterDelete")
	public void createWarehousingAfterUpdateAfterApproveAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "???????????????????????????????????????????????????????????????????????????");

		// ???????????????12
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "11";
		String commPrices = "333";
		String amounts = "3663";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????12
		commIDs = String.valueOf(warehousingMap.get("commodity56").getID()) + "," + String.valueOf(warehousingMap.get("commodity57").getID()) + "," + String.valueOf(warehousingMap.get("commodity58").getID());
		barcodeIDs = String.valueOf(warehousingMap.get("barcode56").getID()) + "," + String.valueOf(warehousingMap.get("barcode57").getID()) + "," + String.valueOf(warehousingMap.get("barcode58").getID());
		commNOs = "100,100,20";
		commPrices = "1.1,1.2,1.3";
		amounts = "200,200,200";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????12
		approveWarehousing(warehousing, commIDs);
		// ???????????????12
		MvcResult mr = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + warehousing.getID())
						//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_25
	@Test(dependsOnMethods = "createWarehousingAfterUpdateAfterApproveAfterDelete")
	public void updateWarehousingPurchasingLinkagesAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "??????????????????????????????????????????????????????????????????");

		// ??????????????????1
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "66";
		String commPrices = "666";
		String commPurchasingUnit = "???";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		PurchasingOrder purchasingOrder1 = createPurchasingOrder(commIDs, commNOs, commPrices, commPurchasingUnit, barcodeIDs);
		// ??????????????????1
		approvePurchasingOrder(purchasingOrder1, commIDs, commNOs, commPrices, barcodeIDs);
		// ???????????????13
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setPurchasingOrderID(purchasingOrder1.getID());
		String amounts = "43956";
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????13
		commNOs = "10";
		commPrices = "6666";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		deleteWarehousing(warehousing);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_26
	@Test(dependsOnMethods = "updateWarehousingPurchasingLinkagesAfterDelete")
	public void updateWarehousingNoPurchasingLinkagesAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "?????????????????????????????????????????????????????????????????????)");

		// ???????????????14
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "66";
		String commPrices = "888";
		String amounts = "58608";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????14
		commNOs = "10";
		commPrices = "8880";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		deleteWarehousing(warehousing);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_27
	@Test(dependsOnMethods = "updateWarehousingNoPurchasingLinkagesAfterDelete")
	public void updateWarehousingPurchasingLinkagesAfterApproveAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "???????????????????????????????????????????????????????????????????????????????????????");

		// ??????????????????2
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "66";
		String commPrices = "888";
		String commPurchasingUnit = "???";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		PurchasingOrder purchasingOrder = createPurchasingOrder(commIDs, commNOs, commPrices, commPurchasingUnit, barcodeIDs);
		// ??????????????????2
		approvePurchasingOrder(purchasingOrder, commIDs, commNOs, commPrices, barcodeIDs);
		// ???????????????15
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setPurchasingOrderID(purchasingOrder.getID());
		String amounts = "58608";
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????15
		commNOs = "10";
		commPrices = "888";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????15
		approveWarehousing(warehousing, commIDs);
		// ???????????????15
		MvcResult mr = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + warehousing.getID())
						//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_28
	@Test(dependsOnMethods = "updateWarehousingPurchasingLinkagesAfterApproveAfterDelete")
	public void updateWarehousingNoPurchasingLinkagesAfterApproveAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "??????????????????????????????????????????????????????????????????????????????????????????");

		// ???????????????16
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "66";
		String commPrices = "888";
		String amounts = "58608";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????16
		commNOs = "10";
		commPrices = "8880";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????16
		approveWarehousing(warehousing, commIDs);
		// ???????????????16
		MvcResult mr = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + warehousing.getID())
						//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_29
	@Test(dependsOnMethods = "updateWarehousingNoPurchasingLinkagesAfterApproveAfterDelete")
	public void approveWarehousingAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "??????????????????????????????????????????");

		// ???????????????17
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity56").getID()) + "," + String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "20,30";
		String commPrices = "50,30";
		String amounts = "1000,900";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode56").getID()) + "," + String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ???????????????17
		approveWarehousing(warehousing, commIDs);
		// ???????????????17
		MvcResult mr = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + warehousing.getID())
						//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ????????????
	}

	// SIT1_nbr_SG_Warehousing_30
	@Test(dependsOnMethods = "approveWarehousingAfterDelete")
	public void cancelCreateWarehousingAfterCreateCompany() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "????????????????????????????????????????????????????????????????????????");

		// ?????????????????????session
		Company company = (Company) warehousingMap.get("company");
		sessionBoss = Shared.getStaffLoginSession(mvc, company.getBossPhone(), BaseCompanyTest.bossPassword_New, company.getSN());
		dbName = company.getDbName();
		// ??????????????????????????????????????????????????????
	}

	// SIT1_nbr_SG_Warehousing_31
	@Test(dependsOnMethods = "cancelCreateWarehousingAfterCreateCompany")
	public void saveWarehousingAfterCreateCompany() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "??????????????????????????????????????????????????????");
		// ?????????????????????
		// ?????????????????????
	}

	// SIT1_nbr_SG_Warehousing_32
	@Test(dependsOnMethods = "saveWarehousingAfterCreateCompany")
	public void createCancelSaveWarehousingAfterCreateCompany() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "??????????????????????????????????????????????????????????????????????????????????????????");
		// ?????????????????????
		// ??????????????????????????????????????????
		// ?????????????????????
		// ?????????????????????
	}

	// SIT1_nbr_SG_Warehousing_33
	@Test(dependsOnMethods = "createCancelSaveWarehousingAfterCreateCompany")
	public void deleteWarehousingAfterCreateCompany() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "??????????????????????????????????????????????????????");
		// ?????????????????????
		// ?????????????????????
	}

	// SIT1_nbr_SG_Warehousing_34
	@Test(dependsOnMethods = "deleteWarehousingAfterCreateCompany")
	public void approveWarehousingAfterCreateCompany() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "??????????????????????????????????????????????????????");
		// ?????????????????????
		// ?????????????????????
	}

	// SIT1_nbr_SG_Warehousing_35
	@Test(dependsOnMethods = "approveWarehousingAfterCreateCompany")
	public void createComodityAndOperationInWarehousingWarehousingAfterCreateCompany() throws Exception {

		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "???????????????????????????????????????????????????????????????????????????");
		// ??????????????????????????????????????????????????????
		// ??????????????????1
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName(CommonCommodity + getCommodityOrder());
		c.setMultiPackagingInfo(getBarcode() + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		//
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commodity.getID(), dbName);
		// ??????????????????????????????????????????0???????????????????????????
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = "" + commodity.getID();
		String commNOs = "28";
		String commPrices = "0";
		String amounts = "2772";
		String barcodeIDs = "" + barcode.getID();
		Warehousing warehousingCreate = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// ?????????????????????????????????????????????
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousingCreate.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID()))//
						.param(CommIDs, String.valueOf(commodity.getID()))//
						.param(BarcodeIDs, String.valueOf(barcode.getID()))//
						.param(CommNOs, "100")//
						.param(CommPrices, "1.1")//
						.param(Amounts, "200")//
						.param("shelfLifes", "36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3);

		JSONObject o = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		int providerID = JsonPath.read(o, "$.object.providerID");
		//
		Assert.assertTrue(providerID == w.getProviderID());
		WarehousingCP.verifyUpdateEx(mr3, warehousingCreate, dbName);
		Warehousing warehousingUpdated = new Warehousing();
		warehousingUpdated = (Warehousing) warehousingUpdated.parse1(o.getString(BaseAction.KEY_Object));
		// ????????????????????????????????????
		// ?????????????????????
		MvcResult mr4 = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + warehousingUpdated.getID())
						//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr4);
		WarehousingCP.verifyDelete(warehousingUpdated, warehousingBO, dbName);
		// ???????????????????????????????????????
		// ?????????????????????
		// ?????????????????????
		// ?????????????????????
		// ?????????????????????0?????????????????????????????????
		Warehousing warehousingCreate2 = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// //????????????????????????????????????????????????0???????????????????????????
		// // ????????????????????????????????????F_NO????????????????????????????????????F_NO??????????????????
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousingCreate2);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
			Assert.assertTrue(false, "??????????????????????????????????????????????????????????????????=" + warehousingBO.getLastErrorCode() + "???????????????=" + warehousingBO.getLastErrorMessage());
		}
		List<BaseModel> whCommBm = bmList.get(1);
		List<WarehousingCommodity> whCommList = new ArrayList<>();
		for (BaseModel bm : whCommBm) {
			WarehousingCommodity whc = (WarehousingCommodity) bm;
			whCommList.add(whc);
		}
		List<Commodity> commList = new ArrayList<>();
		// ??????????????????
		for (WarehousingCommodity whc : whCommList) {
			Commodity commodityCache = getCommodityCache(whc.getCommodityID());
			commList.add(commodityCache);
		}
		// ????????????????????????????????????????????????0???????????????????????????
		MvcResult mr5 = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousingCreate2.getID())).param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(w.getPurchasingOrderID()))
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1")//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(w.getProviderID()))//
						.param(CommIDs, String.valueOf(commodity.getID()))//
						.param(BarcodeIDs, String.valueOf(barcode.getID()))//
						.param(CommNOs, "100")//
						.param(CommPrices, "0")//
						.param(Amounts, "200")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr5);
		// ?????????
		WarehousingCP.verifyApprove(mr5, warehousingCreate2, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("???????????????staff=" + staff);

		return staff;
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
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ???????????????????????????
		w.setStaffID(getStaffFromSession(sessionBoss).getID());
		WarehousingCP.verifyCreate(mr, w, dbName);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(JsonPath.read(o, "$.object").toString());

		return warehousing1;
	}

	private Commodity getCommodityCache(int commodityID) {
		ErrorInfo ecOut = new ErrorInfo();
		Commodity commodityCache = (Commodity) CacheManager.getCache(dbName, EnumCacheType.ECT_Commodity).read1(commodityID, BaseBO.SYSTEM, ecOut, dbName);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
			Assert.assertTrue(false, "???????????????????????????????????????=" + ecOut.getErrorCode() + "???????????????=" + ecOut.getErrorMessage());
		}
		return commodityCache;
	}

	protected Warehousing approveWarehousing(Warehousing w, String commIDs) throws Exception, UnsupportedEncodingException {
		// ?????????????????????????????????????????????
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
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Warehousing warehousing = new Warehousing();
		Warehousing warehousing1 = (Warehousing) warehousing.parse1(JsonPath.read(o, "$.object").toString());
		// ???????????????????????????
		WarehousingCP.verifyApprove(mr, w, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);
		return warehousing1;
	}

	protected void RNBWarehousing(Warehousing w) throws Exception {
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		MvcResult mr = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), w.getQueryKeyword()) //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(w.getStatus())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(w.getWarehouseID())) //
						.param(Warehousing.field.getFIELD_NAME_staffID(), String.valueOf(w.getStaffID())) //
						.param(Warehousing.field.getFIELD_NAME_date1(), w.getDate1() == null ? null : sdf3.format(w.getDate1())) //
						.param(Warehousing.field.getFIELD_NAME_date2(), w.getDate2() == null ? null : sdf3.format(w.getDate2())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
	}

	protected void deleteWarehousing(Warehousing warehousing) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + warehousing.getID())
						//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ???????????????????????????
		WarehousingCP.verifyDelete(warehousing, warehousingBO, dbName);
	}

	protected void updateWarehousing(Warehousing warehousing, String commIDs, String commNOs, String commPrices, String amounts, String barcodeIDs) throws Exception {
		MvcResult mr = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(warehousing.getProviderID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID()))//
						.param(CommIDs, commIDs)//
						.param(BarcodeIDs, barcodeIDs)//
						.param(CommNOs, commNOs)//
						.param(CommPrices, commPrices)//
						.param(Amounts, amounts)//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ???????????????????????????
		WarehousingCP.verifyUpdateEx(mr, warehousing, dbName);
	}

	protected void approvePurchasingOrder(PurchasingOrder purchasingOrder, String commIDs, String purchasingOrderNOs, String priceSuggestions, String barcodeIDs) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID()))//
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(CommIDs, commIDs)//
						.param(NOs, purchasingOrderNOs)//
						.param(PriceSuggestions, priceSuggestions)//
						.param(BarcodeIDs, barcodeIDs)//
						.session((MockHttpSession) sessionBoss) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);
		// ??????????????????????????????
		PurchasingOrderCP.verifyApprove(purchasingOrder, purchasingOrderBO, providerCommodityBO, dbName);
	}

	protected PurchasingOrder createPurchasingOrder(String commIDs, String commNOs, String commPrices, String commPurchasingUnit, String barcodeIDs) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), "010")//
						.param(CommIDs, commIDs)//
						.param(NOs, commNOs) //
						.param(PriceSuggestions, commPrices) //
						.param(CommPurchasingUnit, commPurchasingUnit) //
						.param(ProviderID, "1") //
						.param(BarcodeIDs, barcodeIDs) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		//
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		PurchasingOrder po = new PurchasingOrder();
		PurchasingOrder po1 = (PurchasingOrder) po.parse1(JsonPath.read(o, "$.object").toString());
		// ??????????????????????????????
		PurchasingOrder purchasingOrder = new PurchasingOrder();
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, purchasingOrderBO, dbName);

		return po1;
	}

	protected void RNWarehousing(int pageIndex) throws Exception {
		MvcResult mr = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehouse.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_pageIndex() + "=" + (pageIndex == 0 ? 1 : pageIndex) + "&" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);
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
		//
		order = new AtomicInteger();
		warehousingMap = new HashMap<String, BaseModel>();
		commodityOrder = new AtomicInteger();
		commodityOrder.set(74);
		barcode = new AtomicLong();
		barcode.set(6821423302509L);
	}

	@AfterClass
	public void afterClass() {
		super.tearDown();
	}
}
