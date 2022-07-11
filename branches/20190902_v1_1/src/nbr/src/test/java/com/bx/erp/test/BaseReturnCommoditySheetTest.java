package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bx.erp.dao.BaseMapper;
import com.bx.erp.dao.ReturnCommoditySheetCommodityMapper;
import com.bx.erp.dao.ReturnCommoditySheetMapper;
import com.bx.erp.dao.warehousing.WarehousingCommodityMapper;
import com.bx.erp.dao.warehousing.WarehousingMapper;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.checkPoint.ReturnCommoditySheetCP;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

public class BaseReturnCommoditySheetTest extends BaseMapperTest {
	
//	static ReturnCommoditySheetMapper returnCommoditySheetMapper;

	@BeforeClass
	public void setup() {

		super.setup();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkCreate(); // ...不能放带有Assert的语句在非@Test的函数
		// Doctor_checkStaffID();
		// Doctor_checkStatus();
		// Doctor_checkProviderID();
		// Doctor_checkBarcodesID();
		// Doctor_checkCommodity();
		// Doctor_checkReturnCommoditySheetID();
		// Doctor_checkReturnCommoditySheetCommodity();
		
//		returnCommoditySheetMapper = (ReturnCommoditySheetMapper) mapMapper.get(ReturnCommoditySheetMapper.class.getSimpleName());
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkStaffID();
		// Doctor_checkStatus();
		// Doctor_checkProviderID();
		// Doctor_checkBarcodesID();
		// Doctor_checkCommodity();
		// Doctor_checkReturnCommoditySheetID();
		// Doctor_checkReturnCommoditySheetCommodity();
	}

	public static class DataInput {
		private static ReturnCommoditySheet rcsInput = null;
		private static ReturnCommoditySheetCommodity rcscInput = null;

		public static final ReturnCommoditySheet getReturnCommoditySheet() throws CloneNotSupportedException, InterruptedException {
			rcsInput = new ReturnCommoditySheet();
			rcsInput.setStaffID(5);
			rcsInput.setProviderID(5);
			rcsInput.setShopID(2);

			return (ReturnCommoditySheet) rcsInput.clone();
		}

		public static final ReturnCommoditySheetCommodity getReturnCommoditySheetCommodity() throws CloneNotSupportedException, InterruptedException {
			rcscInput = new ReturnCommoditySheetCommodity();
			rcscInput.setCommodityID(5);
			rcscInput.setBarcodeID(7);
			rcscInput.setNO(50);
			rcscInput.setSpecification("箱");
			rcscInput.setPurchasingPrice(5.5d);

			return (ReturnCommoditySheetCommodity) rcscInput.clone();
		}
	}

	public void Doctor_checkCreate(ReturnCommoditySheetMapper returnCommoditySheetMapper) {
		Shared.printTestClassEndInfo();

		ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
		returnCommoditySheet.setProviderID(-1);
		returnCommoditySheet.setStaffID(-1);
		returnCommoditySheet.setStatus(-1);
		returnCommoditySheet.setQueryKeyword("");
		returnCommoditySheet.setPageIndex(1);
		returnCommoditySheet.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		String error = returnCommoditySheet.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
		if (!error.equals("")) {
			System.out.println("对退货单进行RN时，传入的的字段数据校验出现异常");
			System.out.println("pOrder=" + (returnCommoditySheet == null ? null : returnCommoditySheet));
		}
		//
		Map<String, Object> params = returnCommoditySheet.getRetrieveNParam(BaseBO.INVALID_CASE_ID, returnCommoditySheet);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = returnCommoditySheetMapper.retrieveN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (list.size() == 0 || list == null) {
			System.out.println("RN读出的退货单信息为空");
		}
		for (BaseModel bm : list) {
			ReturnCommoditySheet rcs = (ReturnCommoditySheet) bm;
			String error1 = rcs.checkCreate(BaseBO.INVALID_CASE_ID);
			if (!error1.equals("")) {
				System.out.println(rcs.getID() + "号退货单数据验证出现异常");
				System.out.println("p=" + (rcs == null ? null : rcs));
			}
		}
	}

	public void Doctor_checkStaffID(ReturnCommoditySheetMapper returnCommoditySheetMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetMapper.CheckStaffID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的员工ID错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的员工ID错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkStatus(ReturnCommoditySheetMapper returnCommoditySheetMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetMapper.CheckStatus(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的状态错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的状态错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkProviderID(ReturnCommoditySheetMapper returnCommoditySheetMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetMapper.CheckProviderID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的供应商ID错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的供应商ID错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkBarcodesID(ReturnCommoditySheetMapper returnCommoditySheetMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetMapper.CheckBarcodeID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的退货商品条形码ID错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的退货商品条形码ID错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkCommodity(ReturnCommoditySheetMapper returnCommoditySheetMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetMapper.CheckCommodity(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的退货商品对应的商品错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的退货商品对应的商品错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkReturnCommoditySheetID(ReturnCommoditySheetMapper returnCommoditySheetMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetMapper.CheckReturnCommoditySheetID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的退货商品对应的商品错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的退货商品对应的商品错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkReturnCommoditySheetCommodity(ReturnCommoditySheetMapper returnCommoditySheetMapper) {
		Shared.printTestClassStartInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetMapper.CheckReturnCommoditySheetCommodity(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的退货单对应的退货商品的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的退货单对应的退货商品的错误码的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public static List<Warehousing> getInfoBeforeApprove(List<Commodity> commList, WarehousingMapper warehousingMapper, WarehousingCommodityMapper warehousingCommodityMapper, String dbName) {
		List<Warehousing> warehousingList = new ArrayList<>();
		Warehousing wsBeforeApprove = null;
		for (Commodity comm : commList) {
			if (comm.getCurrentWarehousingID() == 0) {
				Warehousing warehousingSheet = new Warehousing();
				warehousingSheet.setPageIndex(BaseAction.PAGE_StartIndex);
				warehousingSheet.setPageSize(BaseAction.PAGE_SIZE_Infinite);
				// 查询所有的入库单
				Map<String, Object> params = warehousingSheet.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousingSheet);
				DataSourceContextHolder.setDbName(dbName);
				List<BaseModel> ls = warehousingMapper.retrieveN(params);
				Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
				// 应该拿的是有该退货商品的最小入库单，而不是最小入库单
				Warehousing WarehousingMin = new Warehousing();
				for (int i = ls.size() - 1; i >= 0; i--) {
					WarehousingMin = (Warehousing) ls.get(i);// 从最小的入库ID进行遍历，找到
					WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
					warehousingCommodity.setWarehousingID(WarehousingMin.getID());
					warehousingCommodity.setPageIndex(BaseAction.PAGE_StartIndex);
					warehousingCommodity.setPageSize(BaseAction.PAGE_SIZE_Infinite);
					//
					Map<String, Object> paramsWhComm = warehousingCommodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousingCommodity);
					DataSourceContextHolder.setDbName(dbName);
					List<BaseModel> whBmCommList = warehousingCommodityMapper.retrieveN(paramsWhComm);
					Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsWhComm.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsWhComm.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
					List<WarehousingCommodity> whCommList = new ArrayList<>();
					for (BaseModel bm : whBmCommList) {
						WarehousingCommodity whComm = (WarehousingCommodity) bm;
						whCommList.add(whComm);
					}
					WarehousingMin.setListSlave1(whCommList);
					boolean finded = false;
					for (WarehousingCommodity whComm : whCommList) {
						if (whComm.getCommodityID() == comm.getID()) {
							wsBeforeApprove = WarehousingMin;
							finded = true;
						}
					}
					if (finded) {
						break;
					}
				}
			} else {
				Warehousing warehousingSheet = new Warehousing();
				warehousingSheet.setID(comm.getCurrentWarehousingID());
				Map<String, Object> params = warehousingSheet.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, warehousingSheet);
				DataSourceContextHolder.setDbName(dbName);
				List<List<BaseModel>> bmList = (List<List<BaseModel>>) warehousingMapper.retrieve1Ex(params);
				// 入库单ID存在不连续的情况，如果有该入库单
				Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
				wsBeforeApprove = (Warehousing) bmList.get(0).get(0);
				// 如果入库单未审核，那么就当成退货时没有这入库单
				// if (wsBeforeApprove.getStatus() !=
				// EnumStatusReturnCommoditySheet.ESRCS_Approved.getIndex()) {
				// wsBeforeApprove = null;
				// } else {
				List<BaseModel> whCommBmList = bmList.get(1);
				List<WarehousingCommodity> whCommDBList = new ArrayList<>();
				for (BaseModel bm : whCommBmList) {
					WarehousingCommodity wsComm = (WarehousingCommodity) bm;
					whCommDBList.add(wsComm);
				}
				wsBeforeApprove.setListSlave1(whCommDBList);
				// }
			}
			// Assert.assertTrue(wsBeforeApprove != null, "要退货的商品找不到对应的入库单");
			// wsBeforeApprove为null时，代表该商品没有对应的入库单，业务上是允许的
			warehousingList.add(wsBeforeApprove);
		}
		return warehousingList;
	}

	public static ReturnCommoditySheet createExReturnCommoditySheetViaAction(HttpSession sessionOfBoss, Commodity createCommodity, Barcodes barcodes1, String rcscNOs, String commPrices, MockMvc mvc) throws Exception, UnsupportedEncodingException {
		MvcResult mr = mvc.perform(//
				post("/returnCommoditySheet/createEx.bx") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_providerID(), "1") //
						.param(ReturnCommoditySheet.field.getFIELD_NAME_shopID(), "2") //
						.param("commIDs", String.valueOf(createCommodity.getID())) //
						.param("barcodeIDs", String.valueOf(barcodes1.getID())) //
						.param("rcscNOs", String.valueOf(rcscNOs)) //
						.param("commPrices", String.valueOf(commPrices)) //
						.param("rcscSpecifications", "箱") //
						.session((MockHttpSession) sessionOfBoss)//
						.contentType(MediaType.APPLICATION_JSON) //
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);
		// 检查点
		ReturnCommoditySheet rtcsCreate = new ReturnCommoditySheet();
		rtcsCreate.setProviderID(1);
		rtcsCreate.setStaffID(Shared.BossID);
		rtcsCreate.setShopID(2);
		ReturnCommoditySheetCP.verifyCreate(mr, rtcsCreate);
		//
		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		ReturnCommoditySheet returnCommoditySheet = new ReturnCommoditySheet();
		returnCommoditySheet = (ReturnCommoditySheet) returnCommoditySheet.parse1(o1.getString(BaseAction.KEY_Object));
		return returnCommoditySheet;
	}

	public static ReturnCommoditySheetCommodity createReturnCommoditySheetCommodityViaMapper(ReturnCommoditySheetCommodity returnCommoditySheetCommodity, Map<String, BaseMapper> mapMapper) throws CloneNotSupportedException, InterruptedException {
		ReturnCommoditySheetCommodityMapper returnCommoditySheetCommodityMapper = (ReturnCommoditySheetCommodityMapper) mapMapper.get(ReturnCommoditySheetCommodityMapper.class.getSimpleName());
		//
		Map<String, Object> params = returnCommoditySheetCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, returnCommoditySheetCommodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheetCommodity rcsc = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		returnCommoditySheetCommodity.setIgnoreIDInComparision(true);
		if (returnCommoditySheetCommodity.compareTo(rcsc) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		return rcsc;
	}
	
	/** mapper层删除采购退货单商品测试数据并进行验证 */
	public static void deleteReturnCommoditySheetCommodity(ReturnCommoditySheetCommodity returnCommoditySheetCommodity) {
		// 删除添加的测试数据
		Map<String, Object> paramsForDelete = returnCommoditySheetCommodity.getDeleteParam(BaseBO.INVALID_CASE_ID, returnCommoditySheetCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetCommodityMapper.delete(paramsForDelete);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}
	
	
	public static ReturnCommoditySheet createReturnCommoditySheet() throws CloneNotSupportedException, InterruptedException {
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		Map<String, Object> params = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params); // ...
		//
		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		return rcsCreate;
	}
	
	/** @param purchasingOrder
	 * 
	 *            创建退货数据 */
	public static ReturnCommoditySheet returnCommoditySheetCreate(ReturnCommoditySheet returnCommoditySheet) {
//		ReturnCommoditySheetMapper returnCommoditySheetMapper = (ReturnCommoditySheetMapper) mapMapper.get(ReturnCommoditySheetMapper.class.getSimpleName());
		// 传入参数字段验证
		String error = returnCommoditySheet.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = returnCommoditySheet.getCreateParam(BaseBO.INVALID_CASE_ID, returnCommoditySheet);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet createRcs = (ReturnCommoditySheet) returnCommoditySheetMapper.create(params);
		returnCommoditySheet.setIgnoreIDInComparision(true);
		if (returnCommoditySheet.compareTo(createRcs) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createRcs != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		String error1 = createRcs.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		return createRcs;
	}
	
	public static void approveReturnCommoditySheet(ReturnCommoditySheet rcsCreate) {
		Map<String, Object> paramsUpdate = rcsCreate.getUpdateParam(BaseBO.CASE_ApproveReturnCommoditySheet, rcsCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheet rcsUpdate = (ReturnCommoditySheet) returnCommoditySheetMapper.approve(paramsUpdate); // ...
		rcsCreate.setIgnoreIDInComparision(true);
		if (rcsCreate.compareTo(rcsUpdate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcsUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
	
	public static void createReturnCommoditySheetCommodity(int returnCommoditySheetID, int commodityID, int barcodeID, int NO, String specification) throws CloneNotSupportedException, InterruptedException {
		ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(returnCommoditySheetID);
		rcsc.setCommodityID(commodityID);
		rcsc.setBarcodeID(barcodeID);
		rcsc.setNO(NO);
		rcsc.setSpecification(specification);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> rcscparams = rcsc.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc);

		ReturnCommoditySheetCommodity rcscCreate = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(rcscparams);
		rcsc.setIgnoreIDInComparision(true);
		if (rcsc.compareTo(rcscCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		System.out.println(rcscCreate == null ? "null" : rcscCreate);

		Assert.assertTrue(rcscCreate != null && EnumErrorCode.values()[Integer.parseInt(rcscparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建退货单成功");
	}
	
	public static void deleteReturnCommoditySheet(ReturnCommoditySheet rcsCreate) {
		ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(rcsCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = rcsc.getDeleteParam(BaseBO.INVALID_CASE_ID, rcsc);
		//
		returnCommoditySheetCommodityMapper.delete(params); // ...
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		rcsc.setReturnCommoditySheetID(rcsCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsForRetrieveN = rcsc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcsc);

		List<BaseModel> rcscRN = returnCommoditySheetCommodityMapper.retrieveN(paramsForRetrieveN);
		Assert.assertTrue(rcscRN.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

}
