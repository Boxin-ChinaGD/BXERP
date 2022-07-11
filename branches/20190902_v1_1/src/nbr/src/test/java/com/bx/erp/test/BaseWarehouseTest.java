package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.warehousing.WarehouseMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.model.warehousing.Warehouse.EnumStatusWarehouse;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class BaseWarehouseTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setup();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkStatus();
		// Doctor_checkCreate();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
		// TODO 数据医生暂时放在这里，以后转移
		// Doctor_checkStatus();
		// Doctor_checkCreate();
	}
	
	public static class DataInput {
		private static Random ran = new Random();
		private static Warehouse warehouseInput = null;
		
		public static final MockHttpServletRequestBuilder getBuilder(String url, MediaType contentType, Warehouse w) {
			MockHttpServletRequestBuilder builder = post(url).contentType(contentType)//
					.param(Warehouse.field.getFIELD_NAME_ID(), String.valueOf(w.getID())) //
					.param(Warehouse.field.getFIELD_NAME_address(), w.getAddress())//
					.param(Warehouse.field.getFIELD_NAME_staffID(), String.valueOf(w.getStaffID()))//
					.param(Warehouse.field.getFIELD_NAME_phone(), w.getPhone()).param(Warehouse.field.getFIELD_NAME_status(), String.valueOf(w.getStatus()))//
					.param(Warehouse.field.getFIELD_NAME_name(), w.getName());

			return builder;
		}
		
		public static final Warehouse getWarehouse() throws CloneNotSupportedException, InterruptedException {
			warehouseInput = new Warehouse();
			// warehouseInput.setBX_CustomerID(1);
			warehouseInput.setName("仓库" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			warehouseInput.setAddress("植物园");
			warehouseInput.setStatus(EnumStatusWarehouse.ESW_Normal.getIndex());
			warehouseInput.setStaffID(Shared.BossID);
			warehouseInput.setPhone(Shared.getValidStaffPhone());
			warehouseInput.setShopID(2);
			return (Warehouse) warehouseInput.clone();
		}
	}

	private void Doctor_checkStatus(WarehouseMapper warehouseMapper) {
		Shared.printTestClassEndInfo();

		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehouseMapper.checkStatus(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的仓库状态的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的仓库状态的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	public void Doctor_checkCreate(WarehouseMapper warehouseMapper) {
		Shared.printTestClassEndInfo();

		Warehouse warehouse = new Warehouse();
		warehouse.setPageIndex(1);
		warehouse.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		//
		String error = warehouse.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		if (!error.equals("")) {
			System.out.println("对仓库进行RN时，传入的的字段数据校验出现异常");
			System.out.println("warehouse=" + (warehouse == null ? null : warehouse));
		}
		//
		Map<String, Object> params = warehouse.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehouse);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = warehouseMapper.retrieveN(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (list.size() == 0 || list == null) {
			System.out.println("RN读出的仓库信息为空");
		}
		for (BaseModel bm : list) {
			Warehouse w = (Warehouse) bm;
			error = w.checkCreate(BaseBO.INVALID_CASE_ID);
			if (!error.equals("")) {
				System.out.println(w.getID() + "号仓库数据验证出现异常");
				System.out.println("w=" + (w == null ? null : w));
			}
		}
	}
	
	/** @param commTemplate
	 * @return */
	public static void deleteViaMapper(Warehouse warehouseCreate) {
		Map<String, Object> deleteParam = warehouseCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehouseMapper.delete(deleteParam);
		// 结果验证：错误码
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证：查询被删除的测试对象
		Map<String, Object> retrieve1Param = warehouseCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse retrieve2Warehouse = (Warehouse) warehouseMapper.retrieve1(retrieve1Param);
		Assert.assertTrue(retrieve2Warehouse.getStatus() == 1, "删除测试对象失败");
	}
	
	public static Warehouse createWareshouseViaMapper(Warehouse w) {
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		String errorMassage = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		Map<String, Object> params = w.getCreateParam(BaseBO.INVALID_CASE_ID, w);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse warehouseCreate = (Warehouse) warehouseMapper.create(params);
		w.setIgnoreIDInComparision(true);
		if (w.compareTo(warehouseCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(warehouseCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		errorMassage = warehouseCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		return warehouseCreate;
	}
	
	public static Warehouse updateWarehouseViaMapper(Warehouse updateWh) {
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		String errorMassage = updateWh.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		//
		Map<String, Object> params = updateWh.getUpdateParam(BaseBO.INVALID_CASE_ID, updateWh);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse bm = (Warehouse) warehouseMapper.update(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("更新成功：" + bm);
		// 结果验证
		Map<String, Object> retrieve1Param = bm.getRetrieve1Param(BaseBO.INVALID_CASE_ID, bm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse retrieve1Warehouse = (Warehouse) warehouseMapper.retrieve1(retrieve1Param);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, retrieve1Param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (retrieve1Warehouse.compareTo(bm) != 0) {
			Assert.assertTrue(false, "读出的结果和修改的不一致");
		}
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		errorMassage = retrieve1Warehouse.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		return retrieve1Warehouse;
	}
	
	public static void retrieve1WarehouseViaMapper(Warehouse warehouseCreate) {
		//
		Map<String, Object> params = warehouseCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel bm = warehouseMapper.retrieve1(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		String errorMassage = bm.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		//
		System.out.println("查询成功：" + bm);
	}
	
	public static List<BaseModel> retrieveNWarehouseViaMapper(Warehouse warehouseCreate) {
		Map<String, Object> params = warehouseCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bmList = warehouseMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		for (BaseModel baseModel : bmList) {
			String errorMassage = baseModel.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(errorMassage, "");
		}
		return bmList;
	}
}
