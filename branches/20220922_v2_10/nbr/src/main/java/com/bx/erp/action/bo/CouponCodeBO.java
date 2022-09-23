package com.bx.erp.action.bo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.CouponCodeMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Scope("prototype")
@Component("couponCodeBO")
public class CouponCodeBO extends BaseBO {
	public static final String SP_CouponCode_RetrieveN = "SP_CouponCode_RetrieveN";
	public static final String SP_CouponCode_Consume = "SP_CouponCode_Consume";
	public static final String SP_CouponCode_RetrieveNByVipID = "SP_CouponCode_RetrieveNByVipID";
	public static final String SP_CouponCode_RetrieveNTotalByVipID = "SP_CouponCode_RetrieveNTotalByVipID";

	@Resource
	protected CouponCodeMapper couponCodeMapper;

	@Override
	public void setMapper() {
		this.mapper = couponCodeMapper;
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_CouponCode_retrieveNByVipID:
			return checkStaffPermission(staffID, SP_CouponCode_RetrieveNByVipID);
		case BaseBO.CASE_CouponCode_retrieveNTotalByVipID:
			return checkStaffPermission(staffID, SP_CouponCode_RetrieveNTotalByVipID);
		default:
			return checkStaffPermission(staffID, SP_CouponCode_RetrieveN);
		}
	}
	
	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		return true;
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_CouponCode_Consume:
			return checkStaffPermission(staffID, SP_CouponCode_Consume);
		default:
			return super.checkUpdatePermission(staffID, iUseCaseID, s);
		}
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		return true; // 用户去领取的优惠券，所以不需要任何权限
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
		switch (iUseCaseID) {
		case BaseBO.CASE_CouponCode_retrieveNByVipID:
			List<BaseModel> list = ((CouponCodeMapper) mapper).retrieveNByVipID(params);

			if (params.get(BaseAction.SP_OUT_PARAM_iTotalRecord) != null) {
				setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));
			} else {
				setTotalRecord(0);
			}

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return list;
		case BaseBO.CASE_CouponCode_retrieveNTotalByVipID:
			((CouponCodeMapper) mapper).retrieveNTotalByVipID(params);

			if (params.get(BaseAction.SP_OUT_PARAM_iTotalRecord) != null) {
				setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));
			} else {
				setTotalRecord(0);
			}

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return null;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_CouponCode_Consume:
			Map<String, Object> params = s.getUpdateParam(iUseCaseID, s);
			BaseModel bm = ((CouponCodeMapper) mapper).consume(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return bm;
		default:
			return super.doUpdate(iUseCaseID, s);
		}
	}
}
