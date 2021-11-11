package com.bx.erp.action.bo.warehousing;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.warehousing.InventoryCommodityMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.warehousing.InventoryCommodity;

@Component("inventoryCommodityBO")
@Scope("prototype")
public class InventoryCommodityBO extends BaseBO {
	protected final String SP_Inventory_UpdateCommodity = "SP_Inventory_UpdateCommodity";
	protected final String SP_InventoryCommodity_Create = "SP_InventoryCommodity_Create";
	protected final String SP_InventoryCommodity_RetrieveN = "SP_InventoryCommodity_RetrieveN";
	protected final String SP_InventoryCommodity_Delete = "SP_InventoryCommodity_Delete";
	protected final String SP_InventoryCommodity_UpdateNoReal = "SP_InventoryCommodity_UpdateNoReal";

	@Resource
	private InventoryCommodityMapper inventoryCommodityMapper;

	@Override
	public void setMapper() {
		this.mapper = inventoryCommodityMapper;
	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_UpdateInventoryCommodityNoReal:
			Map<String, Object> params = ((InventoryCommodity) s).getUpdateParam(iUseCaseID, s);
			BaseModel bm = ((InventoryCommodityMapper) mapper).updateNoReal(params);
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			
			return bm;
		default:
			return super.doUpdate(iUseCaseID, s);
		}
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_InventoryCommodity_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_UpdateInventoryCommodityNoReal:
			return checkStaffPermission(staffID, SP_InventoryCommodity_UpdateNoReal);
		default:
			return checkStaffPermission(staffID, SP_Inventory_UpdateCommodity);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_InventoryCommodity_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_InventoryCommodity_RetrieveN);
		}
	}

}
