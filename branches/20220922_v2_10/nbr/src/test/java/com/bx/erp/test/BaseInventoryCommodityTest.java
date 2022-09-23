package com.bx.erp.test;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.warehousing.InventoryCommodityMapper;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseInventoryCommodityTest extends BaseTestNGSpringContextTest {
	protected static InventoryCommodityMapper inventoryCommodityMapper;
	
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
		
		inventoryCommodityMapper = (InventoryCommodityMapper) applicationContext.getBean("inventoryCommodityMapper");
		
		Doctor_checkInventorySheetID(); // ...不能放带有Assert的语句在非@Test的函数
		Doctor_checkCommodity();
		Doctor_checkNOReal();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
		
		Doctor_checkInventorySheetID();
		Doctor_checkCommodity();
		Doctor_checkNOReal();
	}

	private static void Doctor_checkNOReal() {
		Shared.printTestClassEndInfo();
		
		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.checkNOReal(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的盘点单商品的实盘数量的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的盘点单商品的实盘数量的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private static void Doctor_checkCommodity() {
		Shared.printTestClassEndInfo();
		
		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.checkCommodity(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的盘点单商品的商品信息的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的盘点单商品的商品信息的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}

	private static void Doctor_checkInventorySheetID() {
		Shared.printTestClassEndInfo();
		
		Map<String, Object> params = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.checkInventorySheetID(params);
		if (EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] != EnumErrorCode.EC_NoError) {
			System.out.println("检查的盘点单商品的盘点单ID的错误码不正确，errorCode =" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		}
		if (!params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("")) {
			System.out.println("检查的盘点单商品的盘点单ID的错误信息与预期中的错误信息不相符，errorMsg=" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
	}
}
