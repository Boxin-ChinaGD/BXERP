package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.commodity.CommodityHistoryBO;
import com.bx.erp.action.bo.message.MessageBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderBO;
import com.bx.erp.action.bo.warehousing.WarehousingBO;
import com.bx.erp.dao.BaseMapper;
import com.bx.erp.dao.warehousing.WarehousingCommodityMapper;
import com.bx.erp.dao.warehousing.WarehousingMapper;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.model.warehousing.Warehousing.EnumStatusWarehousing;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseWarehousingTest extends BaseMapperTest {
	public static final int INVALID_VALUE = 999999999;
	public static final int INVALID_ID = 999999999;

	@BeforeClass
	public void setup() {
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkCreate(); // ...不能放带有Assert的语句在非@Test的函数
		// Doctor_checkStaffID();
		// Doctor_checkStatus();
		// Doctor_checkProviderID();
		// Doctor_checkWarehouseID();
		// Doctor_checkBarcodesID();
		// Doctor_checkCommodity();
		// Doctor_checkPackageUnitID();
		// Doctor_checkSalableNO();
		// Doctor_checkWarehousingID();
		// Doctor_checkWarehousingCommodity();
	}

	@AfterClass
	public void tearDown() {
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkStaffID();
		// Doctor_checkStatus();
		// Doctor_checkProviderID();
		// Doctor_checkWarehouseID();
		// Doctor_checkBarcodesID();
		// Doctor_checkCommodity();
		// Doctor_checkPackageUnitID();
		// Doctor_checkSalableNO();
		// Doctor_checkWarehousingID();
		// Doctor_checkWarehousingCommodity();
	}

	public static class DataInput {
		private static Warehousing warehousingInput = null;
		private static WarehousingCommodity warehousingCommodityInput = null;

		public static final Warehousing getWarehousing() throws CloneNotSupportedException, InterruptedException {
			warehousingInput = new Warehousing();
			warehousingInput.setWarehouseID(1);
			warehousingInput.setShopID(Shared.DEFAULT_Shop_ID);
			warehousingInput.setProviderID(1);
			warehousingInput.setStaffID(1);
			warehousingInput.setPurchasingOrderID(7);

			return (Warehousing) warehousingInput.clone();
		}

		public static final WarehousingCommodity getWarehousingCommodity() throws CloneNotSupportedException, InterruptedException {
			warehousingCommodityInput = new WarehousingCommodity();
			warehousingCommodityInput.setCommodityID(1);
			warehousingCommodityInput.setPackageUnitID(1);
			warehousingCommodityInput.setNO(new Random().nextInt(200) + 1);
			warehousingCommodityInput.setNoSalable(warehousingCommodityInput.getNO());
			warehousingCommodityInput.setBarcodeID(1);
			warehousingCommodityInput.setPrice(10.1f);
			warehousingCommodityInput.setAmount(111.1f);
			warehousingCommodityInput.setShelfLife(36);
			warehousingCommodityInput.setWarehousingID(1);

			return (WarehousingCommodity) warehousingCommodityInput.clone();
		}

		public static final Warehousing getWarehousingAndWarehousingCommodity() throws CloneNotSupportedException, InterruptedException {
			warehousingInput = new Warehousing();
			warehousingInput.setWarehouseID(1);
			warehousingInput.setProviderID(1);
			warehousingInput.setStaffID(1);

			warehousingCommodityInput = new WarehousingCommodity();
			warehousingCommodityInput.setCommodityID(1);
			warehousingCommodityInput.setPackageUnitID(1);
			warehousingCommodityInput.setNO(new Random().nextInt(200) + 1);
			warehousingCommodityInput.setBarcodeID(1);
			warehousingCommodityInput.setPrice(10.1f);
			warehousingCommodityInput.setAmount(111.1f);
			warehousingCommodityInput.setShelfLife(36);

			List<WarehousingCommodity> listWarehousingCommodity = new ArrayList<WarehousingCommodity>();
			listWarehousingCommodity.add(warehousingCommodityInput);

			warehousingInput.setListSlave1(listWarehousingCommodity);

			return (Warehousing) warehousingInput.clone();
		}
	}

	public void Doctor_checkCreate(WarehousingMapper warehousingMapper) {
		Shared.printTestClassEndInfo();

		Warehousing warehousing = new Warehousing();
		warehousing.setProviderID(-1);
		warehousing.setWarehouseID(-1);
		warehousing.setStaffID(-1);
		warehousing.setPurchasingOrderID(-1);
		warehousing.setPageIndex(1);
		warehousing.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		String error = warehousing.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
		if (!error.equals("")) {
			System.out.println("对入库单进行RN时，传入的的字段数据校验出现异常");
			System.out.println("pOrder=" + (warehousing == null ? null : warehousing));
		}
		//
		Map<String, Object> params = warehousing.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = warehousingMapper.retrieveN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (list.size() == 0 || list == null) {
			System.out.println("RN读出的入库单信息为空");
		}
		for (BaseModel bm : list) {
			Warehousing w = (Warehousing) bm;
			String error1 = w.checkCreate(BaseBO.INVALID_CASE_ID);
			if (!error1.equals("")) {
				System.out.println(w.getID() + "号入库单数据验证出现异常");
				System.out.println("p=" + (w == null ? null : w));
			}
		}
	}

	public void Doctor_checkStaffID(WarehousingMapper warehousingMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.checkStaffID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的员工ID错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的员工ID错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkStatus(WarehousingMapper warehousingMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.checkStatus(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的状态错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的状态错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkProviderID(WarehousingMapper warehousingMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.checkProviderID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的供应商ID错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的供应商ID错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkWarehouseID(WarehousingMapper warehousingMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.checkWarehouseID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的仓库ID错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的仓库ID错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkBarcodesID(WarehousingMapper warehousingMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.checkBarcodesID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的入库商品条形码ID错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的入库商品条形码ID错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkCommodity(WarehousingMapper warehousingMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.checkCommodity(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的入库商品对应的商品错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的入库商品对应的商品错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkPackageUnitID(WarehousingMapper warehousingMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.checkPackageUnitID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的入库商品包装单位ID错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的入库商品包装单位ID错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkSalableNO(WarehousingMapper warehousingMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.checkSalableNO(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的入库商品的可售数量错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的入库商品的可售数量错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkWarehousingID(WarehousingMapper warehousingMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.checkWarehousingID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的入库单ID错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的入库单ID错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkWarehousingCommodity(WarehousingMapper warehousingMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.checkWarehousingCommodity(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的入库单对应的入库商品的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的入库单对应的入库商品的错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public static Warehousing approveViaMapper(Warehousing warehousing) {
		String err = warehousing.checkUpdate(BaseBO.CASE_ApproveWarhousing);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = warehousing.getUpdateParam(BaseBO.CASE_ApproveWarhousing, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = (List<List<BaseModel>>) warehousingMapper.approveEx(params);
		Warehousing warehousingApprove = (Warehousing) list.get(0).get(0);
		assertTrue(warehousingApprove != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		err = warehousingApprove.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		if (warehousingApprove.getStatus() != EnumStatusWarehousing.ESW_Approved.getIndex()) {
			Assert.assertTrue(false, warehousing + "审核失败！");
		}
		return warehousingApprove;
	}

	/**
	 * @param warehousingMapper
	 * @param updatePo
	 * @return
	 * @throws CloneNotSupportedException
	 * @throws InterruptedException
	 */
	public static Warehousing createViaMapper(Warehousing warehousing) throws CloneNotSupportedException, InterruptedException {
		// 检验字段合法性
		String errorMessage = warehousing.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMessage, "");
		Map<String, Object> wParams = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing createW = (Warehousing) warehousingMapper.create(wParams);
		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(createW) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(wParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "创建失败！");

		return createW;
	}

	public static Warehousing createViaAction(Warehousing warehousing, String dbName, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO) throws Exception {
		StringBuffer stringBuffer_commIDs = new StringBuffer();
		StringBuffer stringBuffer_commNOs = new StringBuffer();
		StringBuffer stringBuffer_commPrices = new StringBuffer();
		StringBuffer stringBuffer_amounts = new StringBuffer();
		StringBuffer stringBuffer_barcodeIDs = new StringBuffer();
		for (Object object : warehousing.getListSlave1()) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) object;
			stringBuffer_commIDs.append(warehousingCommodity.getCommodityID() + ",");
			stringBuffer_commNOs.append(warehousingCommodity.getNO() + ",");
			stringBuffer_commPrices.append(warehousingCommodity.getPrice() + ",");
			stringBuffer_amounts.append(warehousingCommodity.getAmount() + ",");
			stringBuffer_barcodeIDs.append(warehousingCommodity.getBarcodeID() + ",");
		}

		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(warehousing.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID())) //
						.param(WarehousingCommodity.field.getFIELD_NAME_commIDs(), stringBuffer_commIDs.toString()) //
						.param(WarehousingCommodity.field.getFIELD_NAME_commNOs(), stringBuffer_commNOs.toString()) //
						.param(WarehousingCommodity.field.getFIELD_NAME_commPrices(), stringBuffer_commPrices.toString()) //
						.param(WarehousingCommodity.field.getFIELD_NAME_amounts(), stringBuffer_amounts.toString()) //
						.param(WarehousingCommodity.field.getFIELD_NAME_barcodeIDs(), stringBuffer_barcodeIDs.toString()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		WarehousingCP.verifyCreate(mr, warehousing, dbName);

		return (Warehousing) Shared.parse1Object(mr, warehousing, BaseAction.KEY_Object);
	}

	public static WarehousingCommodity createWarehousingCommodityViaMapper(WarehousingCommodity warehousingCommodity) {
		Map<String, Object> params = warehousingCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, warehousingCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wc = (WarehousingCommodity) warehousingCommodityMapper.create(params);
		Assert.assertTrue(wc != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		return wc;
	}

	public static void deleteWarehousingCommodityViaMapper(WarehousingCommodity wc) {
		//
		Map<String, Object> paramsWcDelete = wc.getDeleteParam(BaseBO.INVALID_CASE_ID, wc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityMapper.delete(paramsWcDelete);
		//
		Map<String, Object> paramsWcRetrieveN = wc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, wc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bmList = warehousingCommodityMapper.retrieveN(paramsWcRetrieveN);
		//
		for (BaseModel bm : bmList) {
			if (bm.getID() == wc.getID()) {
				Assert.assertTrue(false, "删除对象失败");
			}
		}
	}

	public static void deleteViaMapper(Warehousing ws) {
		// 删除创建出来的Warehousing测试对象
		Map<String, Object> deleteWarehousingParams = ws.getDeleteParam(BaseBO.INVALID_CASE_ID, ws);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.delete(deleteWarehousingParams);
		// 结果验证：delete Warehousing
		Map<String, Object> retrieveWarehousingParams = ws.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, ws);
		List<List<BaseModel>> bmList = (List<List<BaseModel>>) warehousingMapper.retrieve1Ex(retrieveWarehousingParams);
		Assert.assertTrue(bmList.get(0).size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieveWarehousingParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveWarehousingParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	/** 删除入库单从表并结果验证创建的数据 */
	public static void deleteWarehousingCommodityViaMapper(Map<String, BaseMapper> mappersMap, WarehousingCommodity wcCreate) {
		WarehousingCommodityMapper warehousingCommodityMapper = (WarehousingCommodityMapper) mappersMap.get(WarehousingCommodityMapper.class.getSimpleName());
		Map<String, Object> params2 = wcCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, wcCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityMapper.delete(params2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramsRetrieveN = wcCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, wcCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> wcList = (List<BaseModel>) warehousingCommodityMapper.retrieveN(paramsRetrieveN);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (BaseModel bm : wcList) {
			WarehousingCommodity wc = (WarehousingCommodity) bm;
			//
			Assert.assertFalse(wcCreate.getCommodityID() == wc.getCommodityID());
		}
	}

	public static WarehousingCommodity createWarehousingCommodityViaMapper(int warehousingID, int commodityID, int commodityNO, int barcodeID, double price, double amount, int shelfLife) {
		WarehousingCommodity wc = new WarehousingCommodity();
		wc.setWarehousingID(warehousingID);
		wc.setCommodityID(commodityID);
		wc.setNO(commodityNO);
		wc.setBarcodeID(barcodeID);
		wc.setPrice(price);
		wc.setAmount(amount);
		wc.setShelfLife(shelfLife);
		String errorMassage = wc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		//
		Map<String, Object> params = wc.getCreateParam(BaseBO.INVALID_CASE_ID, wc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wcCreate = (WarehousingCommodity) warehousingCommodityMapper.create(params);
		//
		assertTrue(wcCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		wc.setIgnoreIDInComparision(true);
		wc.setNoSalable(wc.getNO());
		if (wc.compareTo(wcCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		return wcCreate;
	}

	public static WarehousingCommodity createWarehousingCommodityViaMapper(int commodityID) throws CloneNotSupportedException, InterruptedException {
		WarehousingCommodity warehousingCommodity = DataInput.getWarehousingCommodity();
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commodityID, Shared.DBName_Test);
		warehousingCommodity.setCommodityID(commodityID);
		warehousingCommodity.setBarcodeID(barcode.getID());
		Map<String, Object> params = warehousingCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, warehousingCommodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wc = (WarehousingCommodity) warehousingCommodityMapper.create(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// //没有checkCreate
		// String err = wc.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(err, "");
		//
		warehousingCommodity.setIgnoreIDInComparision(true);
		warehousingCommodity.setNoSalable(wc.getNoSalable());
		if (warehousingCommodity.compareTo(wc) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		return wc;
	}

	/**
	 * @param warehousingMapper
	 * @param updatePo
	 * @return
	 * @throws CloneNotSupportedException
	 * @throws InterruptedException
	 */
	public static Warehousing createViaMapper(PurchasingOrder updatePo) throws CloneNotSupportedException, InterruptedException {
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		warehousing.setPurchasingOrderID(updatePo.getID());
		Map<String, Object> wParams = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing createW = (Warehousing) warehousingMapper.create(wParams);
		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(createW) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(wParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "创建失败！");

		return createW;
	}

	/** 第一个参数为入库单ID，数量为第二个参数 */
	public static void createWarehousingCommodityViaMapper(int iWarehousingID, int iNO) throws Exception {
		WarehousingCommodity warehousingCommodity1 = BaseWarehousingTest.DataInput.getWarehousingCommodity();
		warehousingCommodity1.setWarehousingID(iWarehousingID);
		warehousingCommodity1.setNO(iNO);
		warehousingCommodity1.setNoSalable(iNO);
		Map<String, Object> wcParams1 = warehousingCommodity1.getCreateParam(BaseBO.INVALID_CASE_ID, warehousingCommodity1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity createWC1 = (WarehousingCommodity) warehousingCommodityMapper.create(wcParams1);
		warehousingCommodity1.setIgnoreIDInComparision(true);
		if (warehousingCommodity1.compareTo(createWC1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(wcParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "创建失败！");
	}

	public static WarehousingCommodity createWarehousingCommodityViaMapper(Warehousing wsCreate) throws Exception {
		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		// 创建一个正常状态的普通商品
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(tmpCommodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commodity.getID(), Shared.DBName_Test);
		//
		warehousingCommodity.setWarehousingID(wsCreate.getID());
		warehousingCommodity.setCommodityID(commodity.getID());
		warehousingCommodity.setNO(100);
		warehousingCommodity.setBarcodeID(barcode.getID());
		warehousingCommodity.setPrice(10);
		warehousingCommodity.setAmount(3000);
		warehousingCommodity.setShelfLife(36);
		// 检验字段合法性
		String errorMassage = warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		Map<String, Object> paramsCreate = warehousingCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, warehousingCommodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wcCreate = (WarehousingCommodity) warehousingCommodityMapper.create(paramsCreate);
		warehousingCommodity.setIgnoreIDInComparision(true);
		warehousingCommodity.setNoSalable(warehousingCommodity.getNO());
		if (warehousingCommodity.compareTo(wcCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		assertTrue(wcCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 检验字段合法性
		errorMassage = wcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		return wcCreate;
	}

	public static WarehousingCommodity createWarehousingCommodity(int iNO, Warehousing warehousing) {
		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		warehousingCommodity.setWarehousingID(warehousing.getID());
		warehousingCommodity.setCommodityID(1);
		warehousingCommodity.setCommodityName("可比克薯片");
		warehousingCommodity.setPackageUnitID(1);
		warehousingCommodity.setNO(iNO);
		warehousingCommodity.setBarcodeID(1);
		warehousingCommodity.setPrice(10.1f);
		warehousingCommodity.setAmount(111.1f);
		warehousingCommodity.setShelfLife(36);
		Map<String, Object> wcParams = warehousingCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, warehousingCommodity);
		WarehousingCommodity createWC = (WarehousingCommodity) warehousingCommodityMapper.create(wcParams);
		warehousingCommodity.setIgnoreIDInComparision(true);
		warehousingCommodity.setNoSalable(iNO); // 创建入库商品时，DB层的可售数量是拿库存赋值的,但java层的可售数量字段是没有赋值的
		if (warehousingCommodity.compareTo(createWC) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(wcParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "创建失败！");

		return createWC;
	}

	public static void approveViaAction(Warehousing warehousing, String dbName, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO) throws Exception {
		List<Commodity> commList = new ArrayList<Commodity>();
		for(Object object : warehousing.getListSlave1()) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) object;
			Commodity commodity = new Commodity();
			commodity.setID(warehousingCommodity.getCommodityID());
			Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commodity, EnumErrorCode.EC_NoError);
			commList.add(comm);
		}
		
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_NO.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		
		Shared.checkJSONErrorCode(mr);
		WarehousingBO warehousingBO = (WarehousingBO) mapBO.get(WarehousingBO.class.getSimpleName());
		PurchasingOrderBO purchasingOrderBO = (PurchasingOrderBO) mapBO.get(PurchasingOrderBO.class.getSimpleName());
		CommodityHistoryBO commodityHistoryBO = (CommodityHistoryBO) mapBO.get(CommodityHistoryBO.class.getSimpleName());
		MessageBO messageBO = (MessageBO) mapBO.get(MessageBO.class.getSimpleName());
		WarehousingCP.verifyApprove(mr, warehousing, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, dbName, BaseRetailTradeTest.defaultShopID);
	}
}
