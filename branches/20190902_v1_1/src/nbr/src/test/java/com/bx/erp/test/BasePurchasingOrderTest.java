package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
import com.bx.erp.action.bo.purchasing.ProviderCommodityBO;
import com.bx.erp.action.bo.purchasing.PurchasingOrderBO;
import com.bx.erp.dao.purchasing.PurchasingOrderCommodityMapper;
import com.bx.erp.dao.purchasing.PurchasingOrderMapper;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.purchasing.PurchasingOrder.EnumStatusPurchasingOrder;
import com.bx.erp.test.checkPoint.PurchasingOrderCP;
import com.bx.erp.util.DataSourceContextHolder;

public class BasePurchasingOrderTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {

		super.setup();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkCreate(); // ...不能放带有Assert的语句在非@Test的函数
		// Doctor_checkStaffID();
		// Doctor_checkStatus();
		//
		// // 从表PurchasingOrderCommodity的数据健康检验
		// Doctor_checkBarcodesID();
		// Doctor_checkCommodity();
		// Doctor_checkPackageUnitID();
		// Doctor_checkPurchasingOrderCommodity();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkCreate(); // ...不能放带有Assert的语句在非@Test的函数
		// Doctor_checkStaffID();
		// Doctor_checkStatus();
		//
		// // 从表PurchasingOrderCommodity的数据健康检验
		// Doctor_checkBarcodesID();
		// Doctor_checkCommodity();
		// Doctor_checkPackageUnitID();
		// Doctor_checkPurchasingOrderCommodity();
	}

	public static class DataInput {
		private static PurchasingOrder poInput = null;
		private static PurchasingOrderCommodity pocInput = null;

		public static final PurchasingOrder getPurchasingOrder() throws CloneNotSupportedException, InterruptedException {

			poInput = new PurchasingOrder();
			poInput.setStatus(EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
			// poInput.setInt2(1); //...int2作用未知
			poInput.setStaffID(STAFF_ID4);
			poInput.setProviderID(1);
			poInput.setRemark("Remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			poInput.setProviderName("");
			poInput.setShopID(Shared.DEFAULT_Shop_ID);

			return (PurchasingOrder) poInput.clone();
		}

		public static final PurchasingOrderCommodity getPurchasingOrderCommodity() throws CloneNotSupportedException {
			pocInput = new PurchasingOrderCommodity();
			pocInput.setCommodityID(1);
			pocInput.setPurchasingOrderID(2);
			pocInput.setCommodityNO(Math.abs(new Random().nextInt(300)) + 1);
			pocInput.setPriceSuggestion(1);
			pocInput.setBarcodeID(1);

			return (PurchasingOrderCommodity) pocInput.clone();
		}
	}

	public void Doctor_checkCreate(PurchasingOrderMapper purchasingOrderMapper, PurchasingOrderCommodityMapper purchasingOrderCommodityMapper) {
		Shared.printTestClassEndInfo();

		PurchasingOrder pOrder = new PurchasingOrder();
		pOrder.setStatus(BaseAction.INVALID_STATUS);

		pOrder.setQueryKeyword((""));

		pOrder.setDate1(null);
		pOrder.setDate2(null);
		pOrder.setPageIndex(1);
		pOrder.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		String error = pOrder.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
		if (!error.equals("")) {
			System.out.println("对采购订单进行RN时，传入的的字段数据校验出现异常");
			System.out.println("pOrder=" + (pOrder == null ? null : pOrder));
		}
		//
		Map<String, Object> params = pOrder.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = purchasingOrderMapper.retrieveN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (list.size() == 0 || list == null) {
			System.out.println("RN读出的采购订单信息为空");
		}
		for (BaseModel bm : list) {
			PurchasingOrder p = (PurchasingOrder) bm;
			String error1 = p.checkCreate(BaseBO.INVALID_CASE_ID);
			if (!error1.equals("")) {
				System.out.println(p.getID() + "号采购订单数据验证出现异常");
				System.out.println("p=" + (p == null ? null : p));
			}
			PurchasingOrderCommodity pOrderComm = new PurchasingOrderCommodity();
			pOrderComm.setPurchasingOrderID(p.getID());
			pOrderComm.setPageIndex(1);
			pOrderComm.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			Map<String, Object> pOrderCommParams = pOrderComm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pOrderComm);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> pOrderCommList = purchasingOrderCommodityMapper.retrieveN(pOrderCommParams);
			for (BaseModel pCommbm : pOrderCommList) {
				PurchasingOrderCommodity pComm = (PurchasingOrderCommodity) pCommbm;
				String error2 = pComm.checkCreate(BaseBO.INVALID_CASE_ID);
				if (!error2.equals("")) {
					System.out.println(pComm.getID() + "号采购订单商品数据验证出现");
					System.out.println("pComm=" + (pComm == null ? null : pComm));
				}
			}
		}
	}

	public void Doctor_checkStaffID(PurchasingOrderMapper purchasingOrderMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.checkStaffID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的员工ID错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的员工ID错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkStatus(PurchasingOrderMapper purchasingOrderMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.checkStatus(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的状态错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的状态错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkBarcodesID(PurchasingOrderMapper purchasingOrderMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.checkBarcodesID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的条形码错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的条形码错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkCommodity(PurchasingOrderMapper purchasingOrderMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.checkCommodity(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的商品信息错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的商品信息错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkPackageUnitID(PurchasingOrderMapper purchasingOrderMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.checkPackageUnitID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的包装单位错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的包装单位错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkPurchasingOrderCommodity(PurchasingOrderMapper purchasingOrderMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.checkPurchasingOrderCommodity(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的采购订单对应的采购商品错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的采购订单对应的采购商品的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public static PurchasingOrder approverViaAction(PurchasingOrder purchasingOrder, String dbName, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO) throws Exception {
		MvcResult mr = mvc.perform( //
				post("/purchasingOrder/approveEx.bx") //
						.param(PurchasingOrder.field.getFIELD_NAME_ID(), String.valueOf(purchasingOrder.getID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_NO.getIndex()))//
						.session((MockHttpSession) session) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);
		PurchasingOrderBO purchasingOrderBO = (PurchasingOrderBO) mapBO.get(PurchasingOrderBO.class.getSimpleName());
		ProviderCommodityBO providerCommodityBO = (ProviderCommodityBO) mapBO.get(ProviderCommodityBO.class.getSimpleName());
		PurchasingOrderCP.verifyApprove(purchasingOrder, purchasingOrderBO, providerCommodityBO, Shared.DBName_Test);

		return (PurchasingOrder) Shared.parse1Object(mr, purchasingOrder, BaseAction.KEY_Object);
	}

	/**
	 * @param purchasingOrderMapper
	 * @param purchasingOrder
	 * 
	 *            审核采购单 状态0-->1
	 */
	public static PurchasingOrder purchasingOrderApproverViaMapper(PurchasingOrder purchasingOrder) {
		purchasingOrder.setApproverID(1);
		Map<String, Object> params = purchasingOrder.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder approvePo = (PurchasingOrder) purchasingOrderMapper.approve(params);
		if (approvePo != null) {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(approvePo.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex(), "审核失败");

			if (purchasingOrder.compareTo(approvePo) == 0) {
				Assert.assertTrue(false, "审核后的采购订单跟审核前的采购订单一样!");
			}
		} else {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			Map<String, Object> paramsR1 = purchasingOrder.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, purchasingOrder);
			List<List<BaseModel>> list = purchasingOrderMapper.retrieve1Ex(paramsR1);
			Assert.assertTrue(list.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			PurchasingOrder poR1 = (PurchasingOrder) list.get(0).get(0);
			//
			if (poR1.compareTo(purchasingOrder) != 0) {
				Assert.assertTrue(false, "审核失败的采购订单应该跟审核之前的采购订单不一样!");
			}
		}
		//
		return approvePo;
	}

	/**
	 * @param purchasingOrder
	 * 
	 *            创建采购数据
	 */
	public static PurchasingOrder createPurchasingOrderViaMapper(PurchasingOrder purchasingOrder) {
		// 传入参数字段验证
		String error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = purchasingOrder.getCreateParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo = (PurchasingOrder) purchasingOrderMapper.create(params);
		purchasingOrder.setIgnoreIDInComparision(true);
		if (purchasingOrder.compareTo(createPo) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPo != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		String error1 = createPo.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		return createPo;
	}

	public static PurchasingOrder createViaAction(PurchasingOrder purchasingOrder, String dbName, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO) throws Exception {
		StringBuffer stringBuffer_commIDs = new StringBuffer();
		StringBuffer stringBuffer_NOS = new StringBuffer();
		StringBuffer stringBuffer_pricesuggestions = new StringBuffer();
		StringBuffer stringBuffer_BarcodeIDs = new StringBuffer();
		for (int i = 0; i < purchasingOrder.getListSlave1().size(); i++) {
			PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) purchasingOrder.getListSlave1().get(i);
			stringBuffer_commIDs.append(purchasingOrderCommodity.getCommodityID() + ",");
			stringBuffer_NOS.append(purchasingOrderCommodity.getCommodityNO() + ",");
			stringBuffer_pricesuggestions.append(purchasingOrderCommodity.getPriceSuggestion() + ",");
			stringBuffer_BarcodeIDs.append(purchasingOrderCommodity.getBarcodeID() + ",");
		}
		//
		MvcResult mr = mvc.perform(//
				post("/purchasingOrder/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.param(PurchasingOrder.field.getFIELD_NAME_remark(), purchasingOrder.getRemark())//
						.param(PurchasingOrderCommodity.field.getFIELD_NAME_commIDs(), stringBuffer_commIDs.toString())//
						.param(PurchasingOrderCommodity.field.getFIELD_NAME_NOs(), stringBuffer_NOS.toString()) //
						.param(PurchasingOrderCommodity.field.getFIELD_NAME_priceSuggestions(), stringBuffer_pricesuggestions.toString()) //
						.param(PurchasingOrderCommodity.field.getFIELD_NAME_providerID(), String.valueOf(purchasingOrder.getProviderID())) //
						.param(PurchasingOrder.field.getFIELD_NAME_shopID(), String.valueOf(purchasingOrder.getShopID())) //
						.param(PurchasingOrderCommodity.field.getFIELD_NAME_barcodeIDs(), stringBuffer_BarcodeIDs.toString()) //
						.session((MockHttpSession) session)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		Shared.checkJSONErrorCode(mr);
		PurchasingOrderCP.verifyCreate(mr, purchasingOrder, (PurchasingOrderBO) mapBO.get(PurchasingOrderBO.class.getSimpleName()), dbName);
		//
		return (PurchasingOrder) Shared.parse1Object(mr, purchasingOrder, BaseAction.KEY_Object);
	}

	public static PurchasingOrderCommodity createPurchasingOrderCommodity(PurchasingOrderCommodity poc1) {
		Map<String, Object> params = poc1.getCreateParam(BaseBO.INVALID_CASE_ID, poc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params);
		poc1.setIgnoreIDInComparision(true);
		if (poc1.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		String errorMsg = pocCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");
		return pocCreate;
	}

	/**
	 * @param purchasingOrder
	 * 
	 *            创建采购数据
	 */
	public static PurchasingOrder purchasingOrderCreate(PurchasingOrder purchasingOrder) {
		// 传入参数字段验证
		String error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = purchasingOrder.getCreateParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo = (PurchasingOrder) purchasingOrderMapper.create(params);
		purchasingOrder.setIgnoreIDInComparision(true);
		if (purchasingOrder.compareTo(createPo) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPo != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		String error1 = createPo.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		return createPo;
	}

	/** 删除采购订单并结果验证创建的数据 */
	public static void deleteAndVerificationPurchasingOrder(PurchasingOrder createPo) {
		//
		Map<String, Object> deleteParams1 = createPo.getDeleteParam(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParams1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Map<String, Object> retrieve1Params = createPo.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> poList = (List<List<BaseModel>>) purchasingOrderMapper.retrieve1Ex(retrieve1Params);
		List<BaseModel> r1Po = (List<BaseModel>) poList.get(0);
		Assert.assertTrue(r1Po.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieve1Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	/**
	 * @param purchasingOrderMapper
	 * @param purchasingOrder
	 * 
	 *            审核采购单 状态0-->1
	 */
	public static PurchasingOrder purchasingOrderApprover(PurchasingOrder purchasingOrder) {
		//
		purchasingOrder.setApproverID(1);
		Map<String, Object> params = purchasingOrder.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder approvePo = (PurchasingOrder) purchasingOrderMapper.approve(params);
		if (approvePo != null) {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(approvePo.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex(), "审核失败");

			if (purchasingOrder.compareTo(approvePo) == 0) {
				Assert.assertTrue(false, "审核后的采购订单跟审核前的采购订单一样!");
			}
		} else {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			Map<String, Object> paramsR1 = purchasingOrder.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, purchasingOrder);
			List<List<BaseModel>> list = purchasingOrderMapper.retrieve1Ex(paramsR1);
			Assert.assertTrue(list.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			PurchasingOrder poR1 = (PurchasingOrder) list.get(0).get(0);
			//
			if (poR1.compareTo(purchasingOrder) != 0) {
				Assert.assertTrue(false, "审核失败的采购订单应该跟审核之前的采购订单不一样!");
			}
		}
		//
		return approvePo;
	}

	/**
	 * @param purchasingOrderCreate
	 * 
	 *            更改采购状态1-->2
	 */
	public static void purchasingOrderUpdateStatusOneToTwo(PurchasingOrder purchasingOrderCreate) {
		//
		purchasingOrderCreate.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex());
		Map<String, Object> updateStatusParams = purchasingOrderCreate.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, purchasingOrderCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo = (PurchasingOrder) purchasingOrderMapper.updateStatus(updateStatusParams);
		Assert.assertTrue(updatePo.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_PartWarehousing.getIndex()
				&& EnumErrorCode.values()[Integer.parseInt(updateStatusParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, updateStatusParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	/**
	 * @param purchasingOrderCreate
	 * 
	 *            更改采购状态2-->3
	 */
	public static void purchasingOrderUpdateStatusTwoToThree(PurchasingOrder purchasingOrderCreate) {
		//
		purchasingOrderCreate.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex());
		Map<String, Object> updateStatusParams = purchasingOrderCreate.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, purchasingOrderCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo = (PurchasingOrder) purchasingOrderMapper.updateStatus(updateStatusParams);
		Assert.assertTrue(updatePo.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex()
				&& EnumErrorCode.values()[Integer.parseInt(updateStatusParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, updateStatusParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	/**
	 * @param purchasingOrderCreate
	 * 
	 *            更改采购状态3-->3
	 */
	public static void purchasingOrderUpdateStatusThreeToThree(PurchasingOrder purchasingOrderCreate) {
		//
		purchasingOrderCreate.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex());
		Map<String, Object> updateStatusParams = purchasingOrderCreate.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, purchasingOrderCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo = (PurchasingOrder) purchasingOrderMapper.updateStatus(updateStatusParams);
		Assert.assertTrue(updatePo.getStatus() == PurchasingOrder.EnumStatusPurchasingOrder.ESPO_AllWarehousing.getIndex()
				&& EnumErrorCode.values()[Integer.parseInt(updateStatusParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, updateStatusParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	public static PurchasingOrderCommodity createPurchasingOrderCommodity(int purchasingOrderID, int commodityID, int commodityNO, int barcodeID, double priceSuggestion) {
		//
		PurchasingOrderCommodity poc = new PurchasingOrderCommodity();
		poc.setCommodityID(commodityID);
		poc.setBarcodeID(barcodeID);
		poc.setPurchasingOrderID(purchasingOrderID);
		poc.setCommodityNO(commodityNO);
		poc.setPriceSuggestion(priceSuggestion);
		String err = poc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		Map<String, Object> params = poc.getCreateParam(BaseBO.INVALID_CASE_ID, poc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params);
		poc.setIgnoreIDInComparision(true);
		if (poc.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		return poc;
	}

	public static void deleteAndVerificationPurchasingOrderCommodity(PurchasingOrderCommodity createPoc) {
		//
		Map<String, Object> deleteParams = createPoc.getDeleteParam(BaseBO.INVALID_CASE_ID, createPoc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(deleteParams);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				deleteParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> retrieveNParams = createPoc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, createPoc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> pocList = purchasingOrderCommodityMapper.retrieveN(retrieveNParams);
		Assert.assertTrue(pocList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				retrieveNParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	/** 删除采购单从表并结果验证创建的数据 */
	public static void deletePurchasingOrderCommodity(PurchasingOrderCommodity pocCreate) {
		//
		Map<String, Object> params2 = pocCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pocCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(params2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramsRetrieveN = pocCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pocCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> wcList = (List<BaseModel>) purchasingOrderCommodityMapper.retrieveN(paramsRetrieveN);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (BaseModel bm : wcList) {
			PurchasingOrderCommodity poc = (PurchasingOrderCommodity) bm;
			//
			Assert.assertFalse(pocCreate.getCommodityID() == poc.getCommodityID());
		}
	}

	public static PurchasingOrderCommodity createPurchasingOrderViaMapper(Commodity commCreated) throws CloneNotSupportedException {
		PurchasingOrderCommodity poc1 = DataInput.getPurchasingOrderCommodity();
		Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commCreated.getID(), Shared.DBName_Test);
		poc1.setBarcodeID(barcode.getID());
		poc1.setCommodityID(commCreated.getID());
		// 检验字段合法性
		String errMsg = poc1.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(errMsg.equals(""), errMsg);
		//
		Map<String, Object> params = poc1.getCreateParam(BaseBO.INVALID_CASE_ID, poc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params);
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		poc1.setIgnoreIDInComparision(true);
		if (poc1.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 检验字段合法性
		errMsg = poc1.checkCreate(BaseBO.INVALID_CASE_ID);
		assertTrue(errMsg.equals(""), errMsg);
		//
		return pocCreate;
	}

	public static PurchasingOrder approverPurcchasingOrder(PurchasingOrder p) throws CloneNotSupportedException {
		p.setApproverID(1);
		Map<String, Object> params = p.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, p);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder approvePo = (PurchasingOrder) purchasingOrderMapper.approve(params);
		Assert.assertTrue(approvePo != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		return approvePo;
	}

	public static List<BaseModel> retrieveNPurchasingOrderCommodityListByPurchasingOrderIDViaMapper(int PurchasingOrderID) {
		PurchasingOrderCommodity purchasingOrderCommodity = new PurchasingOrderCommodity();
		purchasingOrderCommodity.setPurchasingOrderID(PurchasingOrderID);
		Map<String, Object> params = purchasingOrderCommodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, purchasingOrderCommodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> ls = purchasingOrderCommodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		return ls;
	}
}
