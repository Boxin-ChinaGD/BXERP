package com.bx.erp.action.bo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.VipMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Vip;

@Component("vipBO")
@Scope("prototype")
public class VipBO extends BaseBO {
	protected final String SP_VIP_Create = "SP_VIP_Create";
	protected final String SP_VIP_Delete = "SP_VIP_Delete";
	protected final String SP_VIP_Retrieve1 = "SP_VIP_Retrieve1";
	protected final String SP_VIP_RetrieveN = "SP_VIP_RetrieveN";
	protected final String SP_VIP_RetrieveNByMobileOrVipCardSN = "SP_VIP_RetrieveNByMobileOrVipCardSN";
	protected final String SP_VIP_Update = "SP_VIP_Update";
	protected final String SP_Vip_UpdateBonus = "SP_Vip_UpdateBonus";
	protected final String SP_VIP_ResetBonus = "SP_VIP_ResetBonus";
	protected final String SP_VipConsumeHistory_RetrieveN = "SP_VipConsumeHistory_RetrieveN";
//	protected final String SP_VIP_RetrieveNByMobileOrCardCode = "SP_VIP_RetrieveNByMobileOrCardCode";

	@Resource
	protected VipMapper vipMapper;

	@Override
	protected void setMapper() {
		this.mapper = vipMapper;
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Vip vip = (Vip) s;
		Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
		switch (iUseCaseID) {
		case CASE_Vip_RetrieveNByMobileOrVipCardSN:
			List<?> ls = ((VipMapper) mapper).retrieveNByMobileOrVipCardSN(params);

			setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls;
		case CASE_Vip_RetrieveNVipConsumeHistory:
			List<?> vipReteailTrade = ((VipMapper) mapper).retrieveNVipConsumeHistory(params);

			setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return vipReteailTrade;
		case CASE_CheckUniqueField:
			Map<String, Object> paramsCheckUniqueField = vip.getRetrieveNParam(iUseCaseID, s);
			((VipMapper) mapper).checkUniqueField(paramsCheckUniqueField);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return null;
//		case CASE_Vip_RetrieveNByMobileOrCardCode:
//			List<BaseModel> bmList = ((VipMapper) mapper).retrieveNByMobileOrCardCode(params);
//
//			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
//			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
//			return bmList;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}

	@Override
	protected List<List<BaseModel>> doRetrieveNEx(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getRetrieveNParamEx(iUseCaseID, s);
		switch (iUseCaseID) {
		case CASE_Vip_RetrieveNVipConsumeHistory:
			List<List<BaseModel>> vipReteailTrade = ((VipMapper) mapper).retrieveNVipConsumeHistory(params);

			setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return vipReteailTrade;
		default:
			return super.doRetrieveNEx(iUseCaseID, s);
		}
	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getUpdateParam(iUseCaseID, s);
		BaseModel bm = null;
		switch (iUseCaseID) {
		case CASE_Vip_UpdateBonus:
			bm = ((VipMapper) mapper).updateBonus(params);
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		case CASE_Vip_ResetBonus:
			bm = ((VipMapper) mapper).resetBonus(params);
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
			return checkStaffPermission(staffID, SP_VIP_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Vip_UpdateBonus:
			return checkStaffPermission(staffID, SP_Vip_UpdateBonus);
		default:
			return checkStaffPermission(staffID, SP_VIP_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VIP_Delete);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_VIP_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Vip_RetrieveNByMobileOrVipCardSN:
			return checkStaffPermission(staffID, SP_VIP_RetrieveNByMobileOrVipCardSN);
		default:
			return checkStaffPermission(staffID, SP_VIP_RetrieveN);
		}

	}

	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Vip_RetrieveNVipConsumeHistory:
			return checkStaffPermission(staffID, SP_VipConsumeHistory_RetrieveN);
		default:
			throw new RuntimeException("Not yet implemented!");
		}

	}

}
