package com.bx.erp.action.bo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.ShopMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Shop;

@Component("shopBO")
@Scope("prototype")
public class ShopBO extends BaseBO {
	protected final String SP_Shop_Create = "SP_Shop_Create";
	protected final String SP_Shop_RetrieveN = "SP_Shop_RetrieveN";
	protected final String SP_Shop_Delete = "SP_Shop_Delete";
	protected final String SP_Shop_Update = "SP_Shop_Update";
	protected final String SP_Shop_Retrieve1 = "SP_Shop_Retrieve1";
	protected final String SP_Shop_RetrieveN_CheckUniqueField = "SP_Shop_RetrieveN_CheckUniqueField";
	protected final String SP_Shop_RetrieveNByFields = "SP_Shop_RetrieveNByFields";

	@Resource
	private ShopMapper shopMapper;

	@Override
	protected void setMapper() {
		this.mapper = shopMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_BxStaffCreateShopAndPreSaleStaffAndBossAccount:
			return true;
		default:
			return checkStaffPermission(staffID, SP_Shop_Create);
		}
	}

	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Shop_RetrieveN);
		}
	}

	@Override
	public boolean checkRetrieve1ExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Shop_Retrieve1);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Shop_Update);
		}
	}

	@Override
	protected boolean checkDeleteExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Shop_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		// case CASE_CheckUniqueField:
		// return checkStaffPermission(staffID, SP_Shop_RetrieveN_CheckUniqueField);
		default:
			return checkStaffPermission(staffID, SP_Shop_RetrieveN);
		}
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Shop shop = (Shop) s;
		switch (iUseCaseID) {
		case CASE_CheckUniqueField:
			Map<String, Object> paramsCheckUniqueField = shop.getRetrieveNParam(iUseCaseID, s);
			((ShopMapper) mapper).checkUniqueField(paramsCheckUniqueField);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return null;
		case CASE_Shop_RetrieveNByFields:
			Map<String, Object> params = ((Shop) s).getRetrieveNParam(iUseCaseID, s);
			List<?> ls = ((ShopMapper) mapper).retrieveNByFields(params);

			setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}
}
