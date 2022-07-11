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
			puInput.setName("箱" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(10);

			return (PackageUnit) puInput.clone();
		}
	}

	@Test
	public void testCreate1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常创建包装单位");
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
		Shared.caseLog("case2:没有权限进行操作");
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

	// 运行Jenkins会跑不过,因为售前账号会被设为离职状态,在sit自动化测试CommodityRelatedTest里进行测试
	// @Test
	// public void testCreate3() throws Exception {
	// Shared.printTestMethodStartInfo();
	// Shared.caseLog("case3:售前人员进行创建商品单位,没有权限进行操作");
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

		Shared.caseLog("case1:正常查询所有包装单位");
		// 先创建一个包装单位
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
		// 检查点
		PackageUnitCP.verifyCreate(mr, packageUnit, Shared.DBName_Test);

		// 查询所有包装单位，检查刚刚创建的是否存在
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

		Shared.caseLog("case2:没有权限进行操作");

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

		Shared.caseLog("case1:正常修改包装单位");
		// 创建一个包装单位
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
		// 检查点
		PackageUnitCP.verifyUpdate(mr, packageUnitUpdate, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:没有权限进行操作");
		// 创建一个包装单位
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

	// 运行Jenkins会跑不过,因为售前账号会被设为离职状态,在sit自动化测试CommodityRelatedTest里进行测试
	// @Test
	// public void testUpdateEx3() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case3:售前人员修改商品单位，没有权限进行操作");
	// // 创建一个包装单位
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

		// 创建一个包装单位
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
		Shared.caseLog("case1：正常删除");
		// 创建包装单位测试对象
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);
		// 正常删除
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnitCreate.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// 结果验证
		PackageUnitCP.verifyDelete(packageUnitCreate, packageUnitBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:没有权限进行操作");
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

		String message = "该包装单位已被商品使用，不能删除";
		Shared.caseLog("case3：" + message);
		// 创建包装单位测试对象
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);
		// 创建商品测试对象
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity();
		tmpCommodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 做删除操作
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnitCreate.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "错误信息不正确！");
		// 删除测试对象并结果验证
		BaseCommodityTest.deleteCommodityViaAction(commodity, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "该包装单位已被采购订单商品使用，不能删除";
		Shared.caseLog("case4：" + message);
		// 创建包装单位测试对象
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);
		// 创建商品测试对象
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity();
		tmpCommodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建采购订单
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder poCreate = BasePurchasingOrderTest.createPurchasingOrderViaMapper(po);
		// 查询商品的条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodityCreate.getID());
		barcodes.setOperatorStaffID(STAFF_ID3);
		List<BaseModel> listBarcode = BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
		// 创建采购订单商品
		PurchasingOrderCommodity purchasingOrderCommodity = BasePurchasingOrderTest.DataInput.getPurchasingOrderCommodity();
		purchasingOrderCommodity.setPurchasingOrderID(poCreate.getID());
		purchasingOrderCommodity.setBarcodeID(listBarcode.get(0).getID());
		purchasingOrderCommodity.setCommodityID(commodityCreate.getID());
		PurchasingOrderCommodity purchasingOrderCommodityCreate = BasePurchasingOrderTest.createPurchasingOrderCommodity(purchasingOrderCommodity);
		// 做删除操作
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnitCreate.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "错误信息不正确！");
		// 删除测试对象并结果验证
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(purchasingOrderCommodityCreate);
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(poCreate);
		BaseCommodityTest.deleteCommodityViaAction(commodityCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "该包装单位已被入库单商品使用，不能删除";
		Shared.caseLog("case5：" + message);
		// 创建包装单位测试对象
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);
		// 创建商品测试对象
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity();
		tmpCommodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建入库单
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing warehousingCreated = BaseWarehousingTest.createViaMapper(warehousing);
		// 查询商品的条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodityCreate.getID());
		barcodes.setOperatorStaffID(STAFF_ID3);
		List<BaseModel> listBarcode = BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
		// 创建入库单商品
		WarehousingCommodity wc = BaseWarehousingTest.DataInput.getWarehousingCommodity();
		wc.setWarehousingID(warehousingCreated.getID());
		wc.setCommodityID(commodityCreate.getID());
		wc.setBarcodeID(listBarcode.get(0).getID());
		WarehousingCommodity wcCreate = BaseWarehousingTest.createWarehousingCommodityViaMapper(wc);
		// 做删除操作
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnitCreate.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "错误信息不正确！");
		// 删除测试对象并结果验证
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wcCreate);
		BaseWarehousingTest.deleteViaMapper(warehousingCreated);
		BaseCommodityTest.deleteCommodityViaAction(commodityCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void testDeleteEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "该包装单位已被盘点单商品使用，不能删除";
		Shared.caseLog("case6：" + message);
		// 创建包装单位测试对象
		PackageUnit packageUnit = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(packageUnit);
		// 创建商品测试对象
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity();
		tmpCommodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 查询商品的条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodityCreate.getID());
		barcodes.setOperatorStaffID(STAFF_ID3);
		List<BaseModel> listBarcode = BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		ic.setBarcodeID(listBarcode.get(0).getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		// 做删除操作
		MvcResult mr = mvc.perform(//
				get("/packageUnit/deleteEx.bx?ID=" + packageUnitCreate.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, message, "错误信息不正确！");
		// 删除测试对象并结果验证
		BaseInventorySheetTest.deleteInventoryCommodity(icCreate);
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		BaseCommodityTest.deleteCommodityViaAction(commodityCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}
}
