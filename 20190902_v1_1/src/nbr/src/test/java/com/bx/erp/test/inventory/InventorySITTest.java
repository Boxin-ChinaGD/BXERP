package com.bx.erp.test.inventory;

import static org.testng.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.warehousing.InventorySheet;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class InventorySITTest extends BaseMapperTest {

	// private static InventoryCommodity inventoryCommodity = new
	// InventoryCommodity();
	private static InventorySheet inventorySheetA = null;
	private static InventorySheet inventorySheetB = null;
	private static Random ran = new Random();

	public static class DataInput {
		protected static final InventorySheet getInventoryA() throws CloneNotSupportedException, InterruptedException {
			if (inventorySheetA == null) {
				inventorySheetA = new InventorySheet();
				inventorySheetA.setWarehouseID(1);
				inventorySheetA.setScope(ran.nextInt(500) + 1);
				inventorySheetA.setStaffID(1);
				inventorySheetA.setRemark("备注A：" + String.valueOf(System.currentTimeMillis()).substring(6));
				inventorySheetA.setCreateDatetime(new Date());
				Thread.sleep(1);
			}
			return (InventorySheet) inventorySheetA.clone();
		}

		protected static final InventorySheet getInventoryB() throws CloneNotSupportedException, InterruptedException {
			Random ran = new Random();
			if (inventorySheetB == null) {
				inventorySheetB = new InventorySheet();
				inventorySheetB.setWarehouseID(1);
				inventorySheetB.setScope(ran.nextInt(500) + 1);
				inventorySheetB.setStaffID(2);
				inventorySheetB.setRemark("备注B：" + String.valueOf(System.currentTimeMillis()).substring(6));
				inventorySheetB.setCreateDatetime(new Date());
				Thread.sleep(1);
			}
			return (InventorySheet) inventorySheetB.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@DataProvider
	public Object[][] runInventoryProcessData() {
		return new Object[][] {}; // ...
	}

	@Test
	public void runInventoryProcess() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建2个盘点单A和B
		System.out.println("\n------------------------ 创建2个盘点单A和B --------------------");

		InventorySheet inventorySheetA = DataInput.getInventoryA();
		InventorySheet inventorySheetB = DataInput.getInventoryB();

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsA = inventorySheetA.getCreateParam(BaseBO.INVALID_CASE_ID, inventorySheetA);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsB = inventorySheetB.getCreateParam(BaseBO.INVALID_CASE_ID, inventorySheetB);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet createInventoryA = (InventorySheet) inventorySheetMapper.create(paramsA);
		assertNotNull(createInventoryA, "盘点单A为空");
		inventorySheetA.setIgnoreIDInComparision(true);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet createInventoryB = (InventorySheet) inventorySheetMapper.create(paramsB);
		assertNotNull(createInventoryB, "盘点单B为空");
		inventorySheetB.setIgnoreIDInComparision(true);

		if (inventorySheetA.compareTo(createInventoryA) != 0 || inventorySheetB.compareTo(createInventoryB) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createInventoryA != null && EnumErrorCode.values()[Integer.parseInt(paramsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError && createInventoryB != null
				&& EnumErrorCode.values()[Integer.parseInt(paramsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		System.out.println(createInventoryA == null ? "createInventoryA == null" : createInventoryA);
		System.out.println(createInventoryB == null ? "createInventoryB == null" : createInventoryB);

		// 向盘点单A和B中增加商品A（可采用一个已经在InsertTables.sql中的商品，下同。将来再重构成：调用HYJ正在写的CommoditySITTest.CreateCommodity()来创建）
		System.out.println("\n------------------------  向盘点单A和B中增加商品A --------------------");

		Map<String, Object> cParamsA1 = new HashMap<String, Object>();
		cParamsA1.put(InventoryCommodity.field.getFIELD_NAME_inventorySheetID(), createInventoryA.getID());
		cParamsA1.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 1);
		cParamsA1.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);
		cParamsA1.put(InventoryCommodity.field.getFIELD_NAME_barcodeID(), 1);

		Map<String, Object> cParamsA2 = new HashMap<String, Object>();
		cParamsA2.put(InventoryCommodity.field.getFIELD_NAME_inventorySheetID(), createInventoryB.getID());
		cParamsA2.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 1);
		cParamsA2.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);
		cParamsA2.put(InventoryCommodity.field.getFIELD_NAME_barcodeID(), 1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity icA1 = (InventoryCommodity) inventoryCommodityMapper.create(cParamsA1);
		assertNotNull(icA1, "盘点商品A1为空");

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity icA2 = (InventoryCommodity) inventoryCommodityMapper.create(cParamsA2);
		assertNotNull(icA2, "盘点商品A2为空");

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(cParamsA1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(cParamsA2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("更新的商品A1：" + icA1);
		System.out.println("更新的商品A2：" + icA2);

		// 向盘点单A和B中增加商品B和数量（随机自然数）
		System.out.println("\n------------------------ 向盘点单A和B中增加商品B和数量（随机自然数） --------------------");

		Map<String, Object> cParamsB1 = new HashMap<String, Object>();
		cParamsB1.put(InventoryCommodity.field.getFIELD_NAME_ID(), icA1.getID());
		cParamsB1.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 2);
		cParamsB1.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);

		Map<String, Object> cParamsB2 = new HashMap<String, Object>();
		cParamsB2.put(InventoryCommodity.field.getFIELD_NAME_ID(), icA2.getID());
		cParamsB2.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 2);
		cParamsB2.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity icB1 = (InventoryCommodity) inventoryCommodityMapper.update(cParamsB1);
		assertNotNull(icB1, "盘点商品B1为空");

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity icB2 = (InventoryCommodity) inventoryCommodityMapper.update(cParamsB2);
		assertNotNull(icB2, "盘点商品B2为空");

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(cParamsB1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(cParamsB2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("更新的商品B1：" + icB1);
		System.out.println("更新的商品B2：" + icB2);

		// 向盘点单A和B中增加重复的商品A和数量（随机自然数）
		System.out.println("\n------------------------ 向盘点单A和B中增加重复的商品A和数量（随机自然数） --------------------");

		Map<String, Object> repeatParamsA1 = new HashMap<String, Object>();
		repeatParamsA1.put(InventoryCommodity.field.getFIELD_NAME_ID(), icA1.getID());
		repeatParamsA1.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 1);
		repeatParamsA1.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);

		Map<String, Object> repeatParamsA2 = new HashMap<String, Object>();
		repeatParamsA2.put(InventoryCommodity.field.getFIELD_NAME_ID(), icA2.getID());
		repeatParamsA2.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 1);
		repeatParamsA2.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity repeatICA1 = (InventoryCommodity) inventoryCommodityMapper.update(repeatParamsA1);
		assertNotNull(repeatICA1, "盘点商品A1为空");

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity repeatICA2 = (InventoryCommodity) inventoryCommodityMapper.update(repeatParamsA2);
		assertNotNull(repeatICA2, "盘点商品A2为空");

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(repeatParamsA1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(repeatParamsA2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("重复添加商品A1：" + repeatICA1);
		System.out.println("重复添加商品A2：" + repeatICA2);

		// 向盘点单A中增加商品C和数量（随机自然数）
		System.out.println("\n------------------------ 向盘点单A中增加商品C和数量（随机自然数 --------------------");

		Map<String, Object> ParamsC1 = new HashMap<String, Object>();
		ParamsC1.put(InventoryCommodity.field.getFIELD_NAME_ID(), icA1.getID());
		ParamsC1.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 3);
		ParamsC1.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity ICC1 = (InventoryCommodity) inventoryCommodityMapper.update(ParamsC1);
		assertNotNull(ICC1, "盘点商品C为空");

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(ParamsC1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("添加商品C：" + ICC1);
		// 向盘点单A中除去商品D。由于商品D不存在，所以本操作有可能引发意外。
		System.out.println("\n------------------------ 向盘点单A中除去商品D。由于商品D不存在，所以会返回EC_NoSuchData --------------------");

		Map<String, Object> ParamsD = new HashMap<String, Object>();
		ParamsD.put(InventoryCommodity.field.getFIELD_NAME_ID(), icA1.getID());
		ParamsD.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 4);
		ParamsD.put(InventoryCommodity.field.getFIELD_NAME_noReal(), 0);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.update(ParamsD);

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(ParamsD.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		System.out.println("iErrorCode:" + ParamsD.get("iErrorCode").toString());
		// 向盘点单B中增加商品D和数量（随机自然数）
		System.out.println("\n------------------------ 向盘点单B中增加商品D和数量（随机自然数） --------------------");

		Map<String, Object> crearteParamsD = new HashMap<String, Object>();
		crearteParamsD.put(InventoryCommodity.field.getFIELD_NAME_ID(), icA2.getID());
		crearteParamsD.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 4);
		crearteParamsD.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity ICD = (InventoryCommodity) inventoryCommodityMapper.update(crearteParamsD);
		assertNotNull(ICD, "盘点商品D为空");

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(crearteParamsD.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		System.out.println("添加商品D：" + ICD);
		// 修改盘点单A中的商品B的数量为另一个随机自然数
		System.out.println("\n------------------------ 修改盘点单A中的商品B的数量为另一个随机自然数 --------------------");

		Map<String, Object> updateParamsB = new HashMap<String, Object>();
		updateParamsB.put(InventoryCommodity.field.getFIELD_NAME_ID(), icA1.getID());
		updateParamsB.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 2);
		updateParamsB.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity updateICB = (InventoryCommodity) inventoryCommodityMapper.update(updateParamsB);
		assertNotNull(updateICB, "盘点商品B为空");

		System.out.println("修改后的盘点单A里的商品B：" + updateICB);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(updateParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 再次向盘点单A中增加商品A和数量（随机自然数）
		System.out.println("\n------------------------ 再次向盘点单A中增加商品A和数量（随机自然数） --------------------");

		Map<String, Object> updateParamsA = new HashMap<String, Object>();
		updateParamsA.put(InventoryCommodity.field.getFIELD_NAME_ID(), icA1.getID());
		updateParamsA.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 1);
		updateParamsA.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity updateICA = (InventoryCommodity) inventoryCommodityMapper.update(updateParamsA);
		assertNotNull(updateICA, "盘点商品B为空");

		System.out.println("修改后的盘点单A里的商品A：" + updateICA);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(updateParamsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// 提交盘点单A。
		System.out.println("\n------------------------ 提交盘点单A --------------------");

		Map<String, Object> submitParamsA = new HashMap<String, Object>();
		submitParamsA.put(InventoryCommodity.field.getFIELD_NAME_ID(), createInventoryA.getID());

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventorySheet submitInventory = (InventorySheet) inventorySheetMapper.submit(submitParamsA);
		assertNotNull(submitInventory, "盘点单A为空");

		System.out.println("提交后的盘点单A：" + submitInventory);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(submitParamsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 向盘点单A中增加商品B。这个操作应该是失败的，因为已经提交。
		System.out.println("\n------------------------ 向盘点单A中增加商品B。这个操作应该是失败的，因为已经提交 --------------------");

		Map<String, Object> repeatParamsB = new HashMap<String, Object>();
		repeatParamsB.put(InventoryCommodity.field.getFIELD_NAME_ID(), icA1.getID());
		repeatParamsB.put(InventoryCommodity.field.getFIELD_NAME_commodityID(), 2);
		repeatParamsB.put(InventoryCommodity.field.getFIELD_NAME_noReal(), ran.nextInt(500) + 1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.update(repeatParamsB);

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(repeatParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);
		System.out.println("iErrorCode:" + repeatParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString());
		// 审批盘点单B。这个操作应该失败，因为它未被提交。参考盘点单的状态图。
		System.out.println("\n------------------------ 审核盘点单B(因为未提交，所以会返回EC_BusinessLogicNotDefined) --------------------");

		Map<String, Object> approveParamsB = new HashMap<String, Object>();
		approveParamsB.put(InventoryCommodity.field.getFIELD_NAME_ID(), createInventoryB.getID());

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventorySheetMapper.approveEx(approveParamsB);

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(approveParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);
		System.out.println("iErrorCode:" + approveParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString());
		// 审批盘点单A。
		System.out.println("\n------------------------ 审核盘点单A --------------------");

		Map<String, Object> approveParamsA = new HashMap<String, Object>();
		approveParamsA.put(InventoryCommodity.field.getFIELD_NAME_ID(), createInventoryA.getID());

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = inventorySheetMapper.approveEx(approveParamsA);
		InventorySheet approveInventoryA = (InventorySheet) bmList.get(0).get(0);
		assertNotNull(approveInventoryA, "盘点单A为空");

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(approveParamsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		System.out.println("审核后盘点单A：" + approveInventoryA);

		// 此时查询数据库的所有盘点单，至少应该有一个盘点单的状态为已录入，至少有一个状态为已审批。
		// ...

		// 将处于已审批状态的所有盘点单生成差异报告。重构SP_Inventory_UpdateCommodity.sql，增加一个参数：IN iNOSystem
		// INT。其业务逻辑为：当且仅当盘点单状态为2时，能且仅能修改商品的F_NOSystem为iNOSystem
		// ...这需要重构与SP_Inventory_UpdateCommodity相应的所有代码：SP/SPTest、Mapper/MapperTest、BO（如果需要）、Action（增加一个新Action，名字：CreateReport，专门用于生成差异报告）

		// 运行本测试至少2次（不能修改任何代码！），看看能否成功执行整个流程。如不能，修改代码令其能
		// ...

	}

}
