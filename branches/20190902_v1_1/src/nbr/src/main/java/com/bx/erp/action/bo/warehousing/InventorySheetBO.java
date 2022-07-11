package com.bx.erp.action.bo.warehousing;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.warehousing.InventorySheetMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.warehousing.InventorySheet;

@Component("inventorySheetBO")
@Scope("prototype")
public class InventorySheetBO extends BaseBO {
	protected final String SP_Inventory_Create = "SP_Inventory_Create";
	protected final String SP_Inventory_Approve = "SP_Inventory_Approve";
	protected final String SP_Inventory_Retrieve1 = "SP_Inventory_Retrieve1";
	protected final String SP_Inventory_RetrieveN = "SP_Inventory_RetrieveN";
	protected final String SP_Inventory_Submit = "SP_Inventory_Submit";
	protected final String SP_Inventory_UpdateSheet = "SP_Inventory_UpdateSheet";
	protected final String SP_Inventory_RetrieveNByFields = "SP_Inventory_RetrieveNByFields";
	protected final String SP_Inventory_Delete = "SP_Inventory_Delete";
	
	@Resource
	private InventorySheetMapper inventorySheetMapper;

	@Override
	public void setMapper() {
		this.mapper = inventorySheetMapper;
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_InventorySheet_RetrieveNByNFields:
			Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
			List<?> ls = ((InventorySheetMapper) mapper).retrieveNByFields(params);

			setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];

			return ls;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getUpdateParam(iUseCaseID, s);
		switch (iUseCaseID) {
//		case CASE_ApproveInventorySheet:
//			InventorySheet approve = inventorySheetMapper.approve(params);
//			setLastErrorCode(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
//
//			return approve;
		case CASE_SubmitInventorySheet:
			InventorySheet submitInventory = ((InventorySheetMapper) mapper).submit(params);
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];

			return submitInventory;
		default:
			return super.doUpdate(iUseCaseID, s);
		}
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Inventory_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_ApproveInventorySheet:
			return checkStaffPermission(staffID, SP_Inventory_Approve);
		case CASE_SubmitInventorySheet:
			return checkStaffPermission(staffID, SP_Inventory_Submit);
		default:
			return checkStaffPermission(staffID, SP_Inventory_UpdateSheet);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Inventory_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1ExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Inventory_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_InventorySheet_RetrieveNByNFields:
			return checkStaffPermission(staffID, SP_Inventory_RetrieveNByFields);
		default:
			return checkStaffPermission(staffID, SP_Inventory_RetrieveN);
		}
	}

	@Override
	protected List<List<BaseModel>> doUpdateEx(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_ApproveInventorySheet:
			Map<String, Object> params = s.getUpdateParam(iUseCaseID, s);
			List<List<BaseModel>> bmList = (List<List<BaseModel>>) ((InventorySheetMapper) mapper).approveEx(params);
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bmList;
		default:
			return super.doUpdateEx(iUseCaseID, s);
		}
	}
	@Override
	
	protected boolean checkUpdateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_ApproveInventorySheet:
			return checkStaffPermission(staffID, SP_Inventory_Approve);
		default:
			return super.checkStaffPermission(staffID, SP_Inventory_UpdateSheet);
		}
	}
}
