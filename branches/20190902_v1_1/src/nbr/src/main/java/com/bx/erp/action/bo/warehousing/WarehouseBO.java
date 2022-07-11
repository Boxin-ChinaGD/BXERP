package com.bx.erp.action.bo.warehousing;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.warehousing.WarehouseMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.warehousing.Warehouse;

@Component("warehouseBO")
@Scope("prototype")
public class WarehouseBO extends BaseBO {
	protected final String SP_Warehouse_Create = "SP_Warehouse_Create";
	// protected final String SP_Warehouse_RetrieveNByFields =
	// "SP_Warehouse_RetrieveNByFields";
	protected final String SP_Warehouse_Retrieve1 = "SP_Warehouse_Retrieve1";
	protected final String SP_Warehouse_RetrieveN = "SP_Warehouse_RetrieveN";
	protected final String SP_Warehouse_Update = "SP_Warehouse_Update";
	protected final String SP_Warehouse_Delete = "SP_Warehouse_Delete";
	protected final String SP_Warehouse_RetrieveInventory = "SP_Warehouse_RetrieveInventory";
	
	@Resource
	private WarehouseMapper warehouseMapper;

	@Override
	public void setMapper() {
		this.mapper = warehouseMapper;

	}

	@Override
	protected BaseModel doRetrieve1(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Warehouse_RetrieveInventory:
			Map<String, Object> params = ((Warehouse) s).getRetrieve1Param(iUseCaseID, s);
			BaseModel warehouse=((WarehouseMapper) mapper).retrieveInventory(params);
			//
//			Warehouse wh = new Warehouse();
//			wh.setfTotalInventory(Float.parseFloat(params.get("fTotalInventory").toString()));
//			wh.setfMaxTotalInventory(Float.parseFloat(params.get("fMaxTotalInventory").toString()));
//			wh.setString1(params.get("sName").toString());
			//
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

			return warehouse;
		default:
			return super.doRetrieve1(iUseCaseID, s);
		}
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		// case CASE_RetrieveCommodityByNFields:
		// Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
		// List<?> ls = ((WarehouseMapper) mapper).retrieveN(params);
		//
		// setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));
		//
		// setLastErrorCode(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		//
		// return ls;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Warehouse_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Warehouse_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Warehouse_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Warehouse_RetrieveInventory:
			return checkStaffPermission(staffID, SP_Warehouse_RetrieveInventory);
		default:
			return checkStaffPermission(staffID, SP_Warehouse_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		// case CASE_RetrieveCommodityByNFields:
		// return checkStaffPermission(staffID, SP_Warehouse_RetrieveNByFields);
		default:
			return checkStaffPermission(staffID, SP_Warehouse_RetrieveN);
		}
	}

	
	@Override
	protected boolean checkDelete(int iUseCaseID, BaseModel s) {
		if(s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = Warehouse.ACTION_ERROR_UpdateDelete1;
			return false;
		}
		
		return super.checkDelete(iUseCaseID, s);
	}

	
	@Override
	protected boolean checkUpdate(int iUseCaseID, BaseModel s) {
		if(s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = Warehouse.ACTION_ERROR_UpdateDelete1;
			return false;
		}
		
		return super.checkUpdate(iUseCaseID, s);
	}

	
}
