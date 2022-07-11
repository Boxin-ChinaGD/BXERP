package com.bx.erp.action.bo.purchasing;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.purchasing.PurchasingOrderCommodityMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;

@Component("purchasingOrderCommodityBO")
@Scope("prototype")
public class PurchasingOrderCommodityBO extends BaseBO {
	protected final String SP_PurchasingOrderCommodity_Create = "SP_PurchasingOrderCommodity_Create";
	protected final String SP_PurchasingOrderCommodity_Delete = "SP_PurchasingOrderCommodity_Delete";
	protected final String SP_PurchasingOrderCommodity_RetrieveN = "SP_PurchasingOrderCommodity_RetrieveN";
	protected final String SP_PurchasingOrderCommodity_RetrieveNWarehousing = "SP_PurchasingOrderCommodity_RetrieveNWarehousing";

	@Resource
	private PurchasingOrderCommodityMapper pocMapper;

	@Override
	public void setMapper() {
		this.mapper = pocMapper;
	}
	
	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
		switch (iUseCaseID) {
		case CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing:
			List<BaseModel> ls2 = ((PurchasingOrderCommodityMapper) mapper).retrieveNNoneWarhousing(params);

			setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls2;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}
	@Override
	protected List<List<BaseModel>> doRetrieveNEx(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = ((PurchasingOrderCommodity) s).getRetrieveNParamEx(iUseCaseID, s);
		switch (iUseCaseID) {
		case CASE_PurchasingOrderCommodityRetrieveNWarhousing:
			List<List<BaseModel>> ls1 = ((PurchasingOrderCommodityMapper) mapper).retrieveNWarhousing(params);
			
			setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));
			
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls1;
		default:
			return super.doRetrieveNEx(iUseCaseID, s);
		}
	}
	
	

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PurchasingOrderCommodity_Create);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_PurchasingOrderCommodity_Delete);
		}
	}
	
	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing:
			return checkStaffPermission(staffID, SP_PurchasingOrderCommodity_RetrieveNWarehousing);
		default:
			return checkStaffPermission(staffID, SP_PurchasingOrderCommodity_RetrieveN);
		}
	}

	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_PurchasingOrderCommodityRetrieveNWarhousing:
			return checkStaffPermission(staffID, SP_PurchasingOrderCommodity_RetrieveNWarehousing);
		default:
			throw new RuntimeException("PurchasingOrderCommodityBO:未定义checkRetrieveNExPermission函数的Case！");
		}
	}

}
