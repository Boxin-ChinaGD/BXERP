package com.bx.erp.action.bo.warehousing;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.warehousing.WarehousingMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("warehousingBO")
@Scope("prototype")
public class WarehousingBO extends BaseBO {
	protected final String SP_Warehousing_Create = "SP_Warehousing_Create";
	protected final String SP_Warehousing_Retrieve1 = "SP_Warehousing_Retrieve1";
	protected final String SP_Warehousing_RetrieveN = "SP_Warehousing_RetrieveN";
	protected final String SP_Warehousing_Retrieve1OrderID = "SP_Warehousing_Retrieve1OrderID";
	protected final String SP_Warehousing_Approve = "SP_Warehousing_Approve";
	protected final String SP_Warehousing_RetrieveNByFields = "SP_Warehousing_RetrieveNByFields";
	protected final String SP_Warehousing_Delete = "SP_Warehousing_Delete";
	protected final String SP_Warehousing_Update = "SP_Warehousing_Update";

	@Resource
	private WarehousingMapper warehousingMapper;

	@Override
	public void setMapper() {
		this.mapper = warehousingMapper;
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_RetrieveNWarhousingByFields:
			Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
			List<BaseModel> ls = ((WarehousingMapper) mapper).retrieveNByFields(params);
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];

			if (params.get(BaseAction.SP_OUT_PARAM_iTotalRecord) != null) {
				setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));
			} else {
				setTotalRecord(0);
			}

			return ls;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}

	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return super.doUpdate(iUseCaseID, s);
		}
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Warehousing_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_ApproveWarhousing:
			return checkStaffPermission(staffID, SP_Warehousing_Approve);
		default:
			return checkStaffPermission(staffID, SP_Warehousing_Update);
		}
	}
	@Override
	protected boolean checkUpdateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_ApproveWarhousing:
			return checkStaffPermission(staffID, SP_Warehousing_Approve);
		default:
			return checkStaffPermission(staffID, SP_Warehousing_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Warehousing_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1ExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Warehousing_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_RetrieveNWarhousingByOrderID:
			return checkStaffPermission(staffID, SP_Warehousing_Retrieve1OrderID);
		case CASE_RetrieveNWarhousingByFields:
			return checkStaffPermission(staffID, SP_Warehousing_RetrieveNByFields);
		default:
			return checkStaffPermission(staffID, SP_Warehousing_RetrieveN);
		}
	}
	
	@Override
	protected List<List<BaseModel>> doUpdateEx(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_ApproveWarhousing:
			Map<String, Object> params = s.getUpdateParam(iUseCaseID, s);
			List<List<BaseModel>> bmList = (List<List<BaseModel>>) ((WarehousingMapper) mapper).approveEx(params);
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bmList;
		default:
			return super.doUpdateEx(iUseCaseID, s);
		}
	}

}
