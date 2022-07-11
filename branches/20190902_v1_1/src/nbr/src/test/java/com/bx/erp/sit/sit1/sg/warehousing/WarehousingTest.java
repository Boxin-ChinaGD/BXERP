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
	protected final String CommonCommodity = "普通商品";
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
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新建门店后，查看数据");

		// 创建公司3
		Company company = BaseCompanyTest.DataInput.getCompany();
		sessionBoss = (MockHttpSession) BaseCompanyTest.getUploadFileSession(BaseCompanyTest.uploadBusinessLicensePictureURL, mvc, sessionOP, BaseCompanyTest.DEFAULT_imagesPath, BaseCompanyTest.DEFAULT_imagesType).getRequest().getSession();
		//
		company = BaseCompanyTest.createCompanyViaAction(company, mvc, mapBO, sessionOP);
		//
		warehousingMap.put("company", company);
		//
		sessionBoss = Shared.getStaffLoginSession(mvc, company.getBossPhone(), BaseCompanyTest.bossPassword_New, company.getSN());
		// 查询入库单
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// 使用原来的session登录
		sessionBoss = Shared.getStaffLoginSession(mvc, bossPhone, bossPassword, companySN);
	}

	// SIT1_nbr_SG_Warehousing_2
	@Test(dependsOnMethods = "retrieveNWarehousing")
	public void createWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新增入库单后，关闭页面再打开");

		// 创建普通商品56
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName(CommonCommodity + getCommodityOrder() + Shared.generateStringByTime(6));
		c.setMultiPackagingInfo(barcode + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity56 = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcode56 = BaseBarcodesTest.retrieveNBarcodes(commodity56.getID(), dbName);
		warehousingMap.put("barcode56", barcode56);
		warehousingMap.put("commodity56", commodity56);

		// 创建普通商品57
		c.setName(CommonCommodity + getCommodityOrder() + Shared.generateStringByTime(6));
		c.setMultiPackagingInfo(getBarcode() + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity57 = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcode57 = BaseBarcodesTest.retrieveNBarcodes(commodity57.getID(), dbName);
		warehousingMap.put("barcode57", barcode57);
		warehousingMap.put("commodity57", commodity57);
		// 创建普通商品58
		c.setName(CommonCommodity + getCommodityOrder() + Shared.generateStringByTime(6));
		c.setMultiPackagingInfo(getBarcode() + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity58 = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		Barcodes barcode58 = BaseBarcodesTest.retrieveNBarcodes(commodity58.getID(), dbName);
		warehousingMap.put("barcode58", barcode58);
		warehousingMap.put("commodity58", commodity58);

		// 创建入库单1
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
		// 查询入库单
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_3
	@Test(dependsOnMethods = "createWarehousing")
	public void approveWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "审核入库单，关闭页面再打开");

		// 审核入库单1
		Warehousing warehousing1 = (Warehousing) warehousingMap.get("warehousing1");
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		approveWarehousing(warehousing1, commIDs);
		// 查询入库单
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
	}

	// SIT1_nbr_SG_Warehousing_4
	@Test(dependsOnMethods = "approveWarehousing")
	public void createWarehousingAfterApprove() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新增入库单后，进行审核");
		// 创建入库单2
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity57").getID());
		String commNOs = "35";
		String commPrices = "66";
		String amounts = "23310";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode57").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 审核入库单2
		Warehousing w1 = new Warehousing();
		w1.setWarehouseID(1);
		w1.setID(warehousing.getID());
		approveWarehousing(w1, commIDs);
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_5
	@Test(dependsOnMethods = "createWarehousingAfterApprove")
	public void createWarehousingAfterApproveAfterRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新增入库单后，进行审核，关闭页面再打开");

		// 创建入库单3
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
		// 审核入库单3
		Warehousing w1 = new Warehousing();
		w1.setWarehouseID(1);
		w1.setID(warehousing.getID());
		approveWarehousing(w1, commIDs);
		// 查询入库单
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_6
	@Test(dependsOnMethods = "createWarehousingAfterApproveAfterRetrieveN")
	public void createWarehousingAfterRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新增入库单后，查询该入库单");

		Thread.sleep(2000);
		Warehousing wRN = new Warehousing();
		wRN.setDate1(DatetimeUtil.getDate(new Date(), -1));
		// 创建入库单4
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
		// 查询入库单
		wRN.setStaffID(warehousing4.getApproverID());
		wRN.setStatus(warehousing4.getStatus());
		wRN.setProviderID(warehousing4.getProviderID());
		wRN.setDate2(DatetimeUtil.getDate(new Date(), +1));
		RNBWarehousing(wRN);
		Thread.sleep(2000);
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_7
	@Test(dependsOnMethods = "createWarehousingAfterRetrieveN")
	public void createWarehousingAfterApproveAfterRetrieveNStatus() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新增入库单后，进行审核，再根据状态查询");

		// 创建入库单5
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
		// 审核入库单5
		Warehousing warehousing1 = approveWarehousing(warehousing, commIDs);
		// 查询入库单
		Warehousing wRN = new Warehousing();
		wRN.setStatus(warehousing1.getStatus());
		RNBWarehousing(wRN);
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_8
	@Test(dependsOnMethods = "createWarehousingAfterApproveAfterRetrieveNStatus")
	public void approveWarehousingAfterRetrieveNStatus() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "审核入库单4后，根据状态查询");

		// 审核入库单4
		Warehousing w = new Warehousing();
		w.setWarehouseID(1);
		w.setID(warehousingMap.get("warehousing4").getID());
		String commIDs = String.valueOf(warehousingMap.get("commodity56").getID());
		Warehousing warehousing = approveWarehousing(w, commIDs);
		// 查询入库单
		Warehousing wRN = new Warehousing();
		wRN.setStatus(warehousing.getStatus());
		RNBWarehousing(wRN);
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_9
	@Test(dependsOnMethods = "approveWarehousingAfterRetrieveNStatus")
	public void pageUp() {// 这个已经删除，不需要实现，这里让order+1
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "对入库单列表翻页后，进行查询操作(这个已经删除，不需要实现，这里让order+1)");
	}

	// SIT1_nbr_SG_Warehousing_10
	@Test(dependsOnMethods = "pageUp")
	public void pageUpAfterCreateWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "对入库单列表翻页后，新增入库单");

		// 查询第二页的入库单
		Warehousing wRN = new Warehousing();
		wRN.setPageIndex(2);
		RNWarehousing(wRN.getPageIndex());
		// 创建入库单6
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
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_11
	@Test(dependsOnMethods = "pageUpAfterCreateWarehousing")
	public void pageUpAfterApproveWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "对入库单列表翻页后，审核入库单");

		// 查询第二页的入库单
		Warehousing wRN = new Warehousing();
		wRN.setPageIndex(2);
		RNWarehousing(wRN.getPageIndex());
		// 审核入库单6
		Warehousing w = new Warehousing();
		w.setWarehouseID(1);
		w.setID(warehousingMap.get("warehousing6").getID());
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		approveWarehousing(w, commIDs);

		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_12
	@Test(dependsOnMethods = "pageUpAfterApproveWarehousing")
	public void retrieveNAfterCreateWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "查询入库单后，新增入库单");

		// 查询入库单
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// 创建入库单7
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
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_13
	@Test(dependsOnMethods = "retrieveNAfterCreateWarehousing")
	public void retrieveNAfterPageUpAfterCreateWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "查询入库单后，翻页后，新增入库单");

		// 查询入库单
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// 查询第二页的入库单
		Warehousing wRN1 = new Warehousing();
		wRN1.setPageIndex(2);
		RNWarehousing(wRN1.getPageIndex());
		// 创建入库单8
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
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_14
	@Test(dependsOnMethods = "retrieveNAfterPageUpAfterCreateWarehousing")
	public void retrieveNAfterApproveWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "查询入库单后，审核入库单");

		// 查询入库单
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// 审核入库单7
		Warehousing w = new Warehousing();
		w.setWarehouseID(1);
		w.setID(warehousingMap.get("warehousing7").getID());
		String commIDs = String.valueOf(warehousingMap.get("commodity56").getID());
		approveWarehousing(w, commIDs);

		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_15
	@Test(dependsOnMethods = "retrieveNAfterApproveWarehousing")
	public void retrieveNAfterPageUpAfterApproveWarehousing() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "查询入库单后，翻页后，审核入库单");

		// 查询入库单
		Warehousing wRN = new Warehousing();
		RNWarehousing(wRN.getPageIndex());
		// 查询第二页的入库单
		Warehousing wRN1 = new Warehousing();
		wRN1.setPageIndex(2);
		RNWarehousing(wRN1.getPageIndex());
		// 审核入库单8
		Warehousing w = new Warehousing();
		w.setWarehouseID(1);
		w.setID(warehousingMap.get("warehousing8").getID());
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		approveWarehousing(w, commIDs);

		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_16
	@Test(dependsOnMethods = "retrieveNAfterPageUpAfterApproveWarehousing")
	public void retrieveNWarehousingAfterKeep() {// 这个后端做不到
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "查看入库单时，点击保存按钮");

		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_17
	@Test(dependsOnMethods = "retrieveNWarehousingAfterKeep")
	public void createWarehousingAfterRemove() {// 这个已经删除，不需要实现，这里让order+1
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新建入库单时，点击取消按钮(这个已经删除，不需要实现，这里让order+1)");

		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_18
	@Test(dependsOnMethods = "createWarehousingAfterRemove")
	public void newMachineRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "在本机进行CRUD操作后，在新机登录nbr系统进行查询，查看原机操作的内容是否存在");

		RNWarehousing(1);
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_19
	@Test(dependsOnMethods = "newMachineRetrieveN")
	public void twoPosMeanwhileDBOper() {// 后端做不到
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "在两台机器或者两台以上机器同时登录nbr进行CRUD操作（有可能操作了同一个商品），测试功能是否都通过");
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_20
	@Test(dependsOnMethods = "twoPosMeanwhileDBOper")
	public void preSaleOperate() {// 这个可以先不进行测试
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "售前人员无法CRUD入库单相关的");

		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_21
	@Test(dependsOnMethods = "preSaleOperate")
	public void createWarehousingAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新增入库单后删除该入库单");

		// 创建入库单9
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "11";
		String commPrices = "333";
		String amounts = "3663";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 删除入库单9
		deleteWarehousing(warehousing);
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_22
	@Test(dependsOnMethods = "createWarehousingAfterDelete")
	public void createWarehousingAfterApproveAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新增入库单后，审核该入库单再删除");

		// 创建入库单10
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "11";
		String commPrices = "333";
		String amounts = "3663";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 审核入库单10
		approveWarehousing(warehousing, commIDs);
		// 删除入库单10
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
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_23
	@Test(dependsOnMethods = "createWarehousingAfterApproveAfterDelete")
	public void createWarehousingAfterUpdateAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新增入库单后，修改该入库单再删除");

		// 创建入库单11
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "11";
		String commPrices = "333";
		String amounts = "3663";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 修改入库单11
		commIDs = String.valueOf(warehousingMap.get("commodity56").getID()) + "," + String.valueOf(warehousingMap.get("commodity57").getID()) + "," + String.valueOf(warehousingMap.get("commodity58").getID());
		barcodeIDs = String.valueOf(warehousingMap.get("barcode56").getID()) + "," + String.valueOf(warehousingMap.get("barcode57").getID()) + "," + String.valueOf(warehousingMap.get("barcode58").getID());
		commNOs = "100,100,20";
		commPrices = "1.1,1.2,1.3";
		amounts = "200,200,200";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 删除入库单11
		deleteWarehousing(warehousing);
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_24
	@Test(dependsOnMethods = "createWarehousingAfterUpdateAfterDelete")
	public void createWarehousingAfterUpdateAfterApproveAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新增入库单后，修改该入库单，再审核，再删除该入库单");

		// 创建入库单12
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "11";
		String commPrices = "333";
		String amounts = "3663";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 修改入库单12
		commIDs = String.valueOf(warehousingMap.get("commodity56").getID()) + "," + String.valueOf(warehousingMap.get("commodity57").getID()) + "," + String.valueOf(warehousingMap.get("commodity58").getID());
		barcodeIDs = String.valueOf(warehousingMap.get("barcode56").getID()) + "," + String.valueOf(warehousingMap.get("barcode57").getID()) + "," + String.valueOf(warehousingMap.get("barcode58").getID());
		commNOs = "100,100,20";
		commPrices = "1.1,1.2,1.3";
		amounts = "200,200,200";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 审核入库单12
		approveWarehousing(warehousing, commIDs);
		// 删除入库单12
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
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_25
	@Test(dependsOnMethods = "createWarehousingAfterUpdateAfterApproveAfterDelete")
	public void updateWarehousingPurchasingLinkagesAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "修改有采购订单关联的入库单后，再删除该入库单");

		// 创建采购订单1
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "66";
		String commPrices = "666";
		String commPurchasingUnit = "瓶";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		PurchasingOrder purchasingOrder1 = createPurchasingOrder(commIDs, commNOs, commPrices, commPurchasingUnit, barcodeIDs);
		// 审核采购订单1
		approvePurchasingOrder(purchasingOrder1, commIDs, commNOs, commPrices, barcodeIDs);
		// 创建入库单13
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setPurchasingOrderID(purchasingOrder1.getID());
		String amounts = "43956";
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 修改入库单13
		commNOs = "10";
		commPrices = "6666";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		deleteWarehousing(warehousing);
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_26
	@Test(dependsOnMethods = "updateWarehousingPurchasingLinkagesAfterDelete")
	public void updateWarehousingNoPurchasingLinkagesAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "修改没有采购订单关联的入库单后，再删除该入库单)");

		// 创建入库单14
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "66";
		String commPrices = "888";
		String amounts = "58608";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 修改入库单14
		commNOs = "10";
		commPrices = "8880";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		deleteWarehousing(warehousing);
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_27
	@Test(dependsOnMethods = "updateWarehousingNoPurchasingLinkagesAfterDelete")
	public void updateWarehousingPurchasingLinkagesAfterApproveAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "修改有采购订单关联的入库单后，审核该入库单，再删除该入库单");

		// 创建采购订单2
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "66";
		String commPrices = "888";
		String commPurchasingUnit = "瓶";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		PurchasingOrder purchasingOrder = createPurchasingOrder(commIDs, commNOs, commPrices, commPurchasingUnit, barcodeIDs);
		// 审核采购订单2
		approvePurchasingOrder(purchasingOrder, commIDs, commNOs, commPrices, barcodeIDs);
		// 创建入库单15
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		w.setPurchasingOrderID(purchasingOrder.getID());
		String amounts = "58608";
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 修改入库单15
		commNOs = "10";
		commPrices = "888";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 审核入库单15
		approveWarehousing(warehousing, commIDs);
		// 删除入库单15
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
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_28
	@Test(dependsOnMethods = "updateWarehousingPurchasingLinkagesAfterApproveAfterDelete")
	public void updateWarehousingNoPurchasingLinkagesAfterApproveAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "修改没有采购订单关联的入库单后，审核该入库单，再删除该入库单");

		// 创建入库单16
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "66";
		String commPrices = "888";
		String amounts = "58608";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 修改入库单16
		commNOs = "10";
		commPrices = "8880";
		updateWarehousing(warehousing, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 审核入库单16
		approveWarehousing(warehousing, commIDs);
		// 删除入库单16
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
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_29
	@Test(dependsOnMethods = "updateWarehousingNoPurchasingLinkagesAfterApproveAfterDelete")
	public void approveWarehousingAfterDelete() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "审核入库单后，再删除该入库单");

		// 创建入库单17
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = String.valueOf(warehousingMap.get("commodity56").getID()) + "," + String.valueOf(warehousingMap.get("commodity58").getID());
		String commNOs = "20,30";
		String commPrices = "50,30";
		String amounts = "1000,900";
		String barcodeIDs = String.valueOf(warehousingMap.get("barcode56").getID()) + "," + String.valueOf(warehousingMap.get("barcode58").getID());
		Warehousing warehousing = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 审核入库单17
		approveWarehousing(warehousing, commIDs);
		// 删除入库单17
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
		// 结果验证
	}

	// SIT1_nbr_SG_Warehousing_30
	@Test(dependsOnMethods = "approveWarehousingAfterDelete")
	public void cancelCreateWarehousingAfterCreateCompany() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新建公司后，进入入库页面点击新建按钮后，点击取消");

		// 使用新公司登录session
		Company company = (Company) warehousingMap.get("company");
		sessionBoss = Shared.getStaffLoginSession(mvc, company.getBossPhone(), BaseCompanyTest.bossPassword_New, company.getSN());
		dbName = company.getDbName();
		// 进入入库页面点击新建按钮后，点击取消
	}

	// SIT1_nbr_SG_Warehousing_31
	@Test(dependsOnMethods = "cancelCreateWarehousingAfterCreateCompany")
	public void saveWarehousingAfterCreateCompany() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新建公司后，进入入库页面点击保存按钮");
		// 进入入库页面；
		// 点击保存按钮；
	}

	// SIT1_nbr_SG_Warehousing_32
	@Test(dependsOnMethods = "saveWarehousingAfterCreateCompany")
	public void createCancelSaveWarehousingAfterCreateCompany() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新建公司后，进入入库页面点击新建按钮，再点击取消，再点击保存");
		// 进入入库页面；
		// 点击新建按钮，不做其他操作；
		// 点击取消按钮；
		// 点击保存按钮；
	}

	// SIT1_nbr_SG_Warehousing_33
	@Test(dependsOnMethods = "createCancelSaveWarehousingAfterCreateCompany")
	public void deleteWarehousingAfterCreateCompany() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新建公司后，进入入库页面点击删除按钮");
		// 进入入库页面；
		// 点击删除按钮；
	}

	// SIT1_nbr_SG_Warehousing_34
	@Test(dependsOnMethods = "deleteWarehousingAfterCreateCompany")
	public void approveWarehousingAfterCreateCompany() throws Exception {
		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新建公司后，进入入库页面点击审核按钮");
		// 进入入库页面；
		// 点击审核按钮；
	}

	// SIT1_nbr_SG_Warehousing_35
	@Test(dependsOnMethods = "approveWarehousingAfterCreateCompany")
	public void createComodityAndOperationInWarehousingWarehousingAfterCreateCompany() throws Exception {

		Shared.printTestMethodStartInfo("SIT1_nbr_SG_Warehousing_", order, "新建公司后，新建一个商品，进入入库页面进行相关操作");
		// 进入商品列表页面，新建一个普通商品；
		// 创建普通商品1
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setName(CommonCommodity + getCommodityOrder());
		c.setMultiPackagingInfo(getBarcode() + ";" + c.getPackageUnitID() + ";" + c.getRefCommodityMultiple() + ";" + c.getPriceRetail() + ";" + c.getPriceVIP() + ";" + c.getPriceWholesale() + ";" + c.getName() + ";");
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, dbName);
		//
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commodity.getID(), dbName);
		// 进入入库页面，新建一个金额为0的入库单；创建成功
		Warehousing w = new Warehousing();
		w.setProviderID(1);
		w.setWarehouseID(1);
		String commIDs = "" + commodity.getID();
		String commNOs = "28";
		String commPrices = "0";
		String amounts = "2772";
		String barcodeIDs = "" + barcode.getID();
		Warehousing warehousingCreate = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// 修改该入库单数据，点击保存按钮
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
		// 点击新建按钮，再点取消；
		// 删除该入库单；
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
		// 点击新建按钮，再点击取消；
		// 点击保存按钮；
		// 点击删除按钮；
		// 点击审核按钮；
		// 新建一个金额为0入库单，点击保存按钮；
		Warehousing warehousingCreate2 = createWarehousing(w, commIDs, commNOs, commPrices, amounts, barcodeIDs);
		// //修改该入库单的商品数据（金额还为0），点击审核按钮；
		// // 审核入库单前，查询商品的F_NO，检查点判断入库单审核后F_NO是否正确计算
		DataSourceContextHolder.setDbName(dbName);
		List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousingCreate2);
		if (warehousingBO.getLastErrorCode() != EnumErrorCode.EC_NoError || bmList == null || bmList.get(0).size() == 0) {
			Assert.assertTrue(false, "查询一个入库单失败或查询的入库单为空，错误码=" + warehousingBO.getLastErrorCode() + "，错误信息=" + warehousingBO.getLastErrorMessage());
		}
		List<BaseModel> whCommBm = bmList.get(1);
		List<WarehousingCommodity> whCommList = new ArrayList<>();
		for (BaseModel bm : whCommBm) {
			WarehousingCommodity whc = (WarehousingCommodity) bm;
			whCommList.add(whc);
		}
		List<Commodity> commList = new ArrayList<>();
		// 入库单有从表
		for (WarehousingCommodity whc : whCommList) {
			Commodity commodityCache = getCommodityCache(whc.getCommodityID());
			commList.add(commodityCache);
		}
		// 修改该入库单的商品数据（金额还为0），点击审核按钮；
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
		// 检查点
		WarehousingCP.verifyApprove(mr5, warehousingCreate2, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName);
	}

	protected Staff getStaffFromSession(HttpSession session) {
		Staff staff = (Staff) session.getAttribute(EnumSession.SESSION_Staff.getName());
		assert staff != null;
		logger.info("当前使用的staff=" + staff);

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
		// 创建入库单的检查点
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
			Assert.assertTrue(false, "从缓存读取商品失败，错误码=" + ecOut.getErrorCode() + "，错误信息=" + ecOut.getErrorMessage());
		}
		return commodityCache;
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
		// 审核入库单的检查点
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
		// 删除入库单的检查点
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
		// 修改入库单的检查点
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
		// 审核采购订单的检查点
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
		// 创建采购订单的检查点
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
