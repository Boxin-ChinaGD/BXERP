package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.commodity.PackageUnitField;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.InventorySheet;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseInventorySheetTest;
import com.bx.erp.test.BasePackageUnitTest;
import com.bx.erp.test.BasePurchasingOrderTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.PackageUnitCP;
import com.jayway.jsonpath.JsonPath;

@WebAppConfiguration
public class PackageUnitActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	public static class DataInput {
		private static PackageUnit puInput = null;

		protected static final PackageUnit getPackageUnit() throws CloneNotSupportedException, InterruptedException {
			puInput = new PackageUnit();
			puInput.setName("???" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(10);

			return (PackageUnit) puInput.clone();
		}
	}

	@Test
	public void testCreate1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:????????????????????????");
		PackageUnit packageUnit = DataInput.getPackageUnit();

		MvcResult mr = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(new PackageUnitField().getFIELD_NAME_name(), packageUnit.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		PackageUnitCP.verifyCreate(mr, packageUnit, Shared.DBName_Test);
	}

	@Test
	public void testCreate2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2:????????????????????????");
		PackageUnit packageUnit = DataInput.getPackageUnit();

		MvcResult mr1 = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier))//
						.param(new PackageUnitField().getFIELD_NAME_name(), packageUnit.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}

	// ??????Jenkins????????????,??????????????????????????????????????????,???sit???????????????CommodityRelatedTest???????????????
	// @Test
	// public void testCreate3() throws Exception {
	// Shared.printTestMethodStartInfo();
	// Shared.caseLog("case3:????????????????????????????????????,????????????????????????");
	// PackageUnit packageUnit = DataInput.getPackageUnit();
	//
	// MvcResult mr1 = mvc.perform(//
	// post("/packageUnit/createEx.bx")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale))//
	// .param(new PackageUnitField().getFIELD_NAME_name(), packageUnit.getName())//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	// }

	@Test
	public void testRetrieveNEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:??????????????????????????????");
		// ???????????????????????????
		PackageUnit packageUnit = DataInput.getPackageUnit();

		MvcResult mr = mvc.perform(//
				post("/packageUnit/createEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.param(new PackageUnitField().getFIELD_NAME_name(), packageUnit.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ?????????
		PackageUnitCP.verifyCreate(mr, packageUnit, Shared.DBName_Test);

		// ????????????????????????????????????????????????????????????
		MvcResult mr2 = mvc.perform(//
				post("/packageUnit/retrieveNEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		List<String> names = JsonPath.read(Shared.checkJSONErrorCode(mr2), "$.objectList[*].ID");
		if (!names.contains(packageUnit.getName())) {
			System.out.println(names);
			Shared.checkJSONErrorCode(mr2);
		}
	}

	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:????????????????????????");

//		MvcResult mr1 = mvc.perform(//
//				post("/packageUnit/retrieveNEx.bx")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:????????????????????????");
		// ????????????????????????
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);

		PackageUnit packageUnitUpdate = BasePackageUnitTest.DataInput.getPackageUnit();
		packageUnitUpdate.setID(packageUnitCreate.getID());

		MvcResult mr = mvc.perform(//
				post("/packageUnit/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(PackageUnit.field.getFIELD_NAME_ID(), String.valueOf(packageUnitUpdate.getID()))//
						.param(PackageUnit.field.getFIELD_NAME_name(), packageUnitUpdate.getName())//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		// ?????????
		PackageUnitCP.verifyUpdate(mr, packageUnitUpdate, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:????????????????????????");
		// ????????????????????????
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);

		PackageUnit packageUnitUpdate = BasePackageUnitTest.DataInput.getPackageUnit();
		packageUnitUpdate.setID(packageUnitCreate.getID());

		MvcResult mr1 = mvc.perform(//
				post("/packageUnit/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier))//
						.param(PackageUnit.field.getFIELD_NAME_ID(), String.valueOf(packageUnitUpdate.getID()))//
						.param(PackageUnit.field.getFIELD_NAME_name(), packageUnitUpdate.getName())//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}

	// ??????Jenkins????????????,??????????????????????????????????????????,???sit???????????????CommodityRelatedTest???????????????
	// @Test
	// public void testUpdateEx3() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case3:?????????????????????????????????????????????????????????");
	// // ????????????????????????
	// PackageUnit packageUnitCreate = createPackageUnit();
	//
	// PackageUnit packageUnit = DataInput.getPackageUnit();
	// packageUnit.setID(packageUnitCreate.getID());
	//
	// MvcResult mr3 = mvc.perform(//
	// post("/packageUnit/updateEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale))//
	// .param(PackageUnit.field.getFIELD_NAME_ID(),
	// String.valueOf(packageUnit.getID()))//
	// .param(PackageUnit.field.getFIELD_NAME_name(), packageUnit.getName())//
	// )//
	// .andExpect(status().isOk()).andDo(print()).andReturn();
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	// }

	@Test
	public void testRetrieve1() throws Exception {
		Shared.printTestMethodStartInfo();

		// ????????????????????????
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);

		MvcResult mr = mvc.perform(//
				get("/packageUnit/retrieve1Ex.bx?ID=" + packageUnitCreate.getID()) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//

		)//
				.andExpect(status().isOk()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1???????????????");
		// ??????????????????????????????
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);
		// ????????????
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnitCreate.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ????????????
		PackageUnitCP.verifyDelete(packageUnitCreate, packageUnitBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:????????????????????????");
		MvcResult mr1 = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=8")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "????????????????????????????????????????????????";
		Shared.caseLog("case3???" + message);
		// ??????????????????????????????
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);
		// ????????????????????????
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity();
		tmpCommodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// ???????????????
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnitCreate.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "????????????????????????");
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaAction(commodity, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "????????????????????????????????????????????????????????????";
		Shared.caseLog("case4???" + message);
		// ??????????????????????????????
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);
		// ????????????????????????
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity();
		tmpCommodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// ??????????????????
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder poCreate = BasePurchasingOrderTest.createPurchasingOrderViaMapper(po);
		// ????????????????????????
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodityCreate.getID());
		barcodes.setOperatorStaffID(STAFF_ID3);
		List<BaseModel> listBarcode = BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
		// ????????????????????????
		PurchasingOrderCommodity purchasingOrderCommodity = BasePurchasingOrderTest.DataInput.getPurchasingOrderCommodity();
		purchasingOrderCommodity.setPurchasingOrderID(poCreate.getID());
		purchasingOrderCommodity.setBarcodeID(listBarcode.get(0).getID());
		purchasingOrderCommodity.setCommodityID(commodityCreate.getID());
		PurchasingOrderCommodity purchasingOrderCommodityCreate = BasePurchasingOrderTest.createPurchasingOrderCommodity(purchasingOrderCommodity);
		// ???????????????
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnitCreate.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "????????????????????????");
		// ?????????????????????????????????
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(purchasingOrderCommodityCreate);
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(poCreate);
		BaseCommodityTest.deleteCommodityViaAction(commodityCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "?????????????????????????????????????????????????????????";
		Shared.caseLog("case5???" + message);
		// ??????????????????????????????
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);
		// ????????????????????????
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity();
		tmpCommodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// ???????????????
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing warehousingCreated = BaseWarehousingTest.createViaMapper(warehousing);
		// ????????????????????????
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodityCreate.getID());
		barcodes.setOperatorStaffID(STAFF_ID3);
		List<BaseModel> listBarcode = BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
		// ?????????????????????
		WarehousingCommodity wc = BaseWarehousingTest.DataInput.getWarehousingCommodity();
		wc.setWarehousingID(warehousingCreated.getID());
		wc.setCommodityID(commodityCreate.getID());
		wc.setBarcodeID(listBarcode.get(0).getID());
		WarehousingCommodity wcCreate = BaseWarehousingTest.createWarehousingCommodityViaMapper(wc);
		// ???????????????
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnitCreate.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "????????????????????????");
		// ?????????????????????????????????
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wcCreate);
		BaseWarehousingTest.deleteViaMapper(warehousingCreated);
		BaseCommodityTest.deleteCommodityViaAction(commodityCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void testDeleteEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "?????????????????????????????????????????????????????????";
		Shared.caseLog("case6???" + message);
		// ??????????????????????????????
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);
		// ????????????????????????
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity();
		tmpCommodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// ???????????????
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// ????????????????????????
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodityCreate.getID());
		barcodes.setOperatorStaffID(STAFF_ID3);
		List<BaseModel> listBarcode = BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
		// ?????????????????????
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		ic.setBarcodeID(listBarcode.get(0).getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		// ???????????????
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnitCreate.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "????????????????????????");
		// ?????????????????????????????????
		BaseInventorySheetTest.deleteInventoryCommodity(icCreate);
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		BaseCommodityTest.deleteCommodityViaAction(commodityCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}
}
