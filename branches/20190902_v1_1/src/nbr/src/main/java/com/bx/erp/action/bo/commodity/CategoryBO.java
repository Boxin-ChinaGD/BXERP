package com.bx.erp.action.bo.commodity;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.CategoryMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Category;

@Component("categoryBO")
@Scope("prototype")
public class CategoryBO extends BaseBO {
	protected final String SP_Category_Create = "SP_Category_Create";
	protected final String SP_Category_RetrieveN = "SP_Category_RetrieveN";
	protected final String SP_Category_Retrieve1 = "SP_Category_Retrieve1";
	protected final String SP_Category_RetrieveNByParent = "SP_Category_RetrieveNByParent";
	protected final String SP_Category_Update = "SP_Category_Update";
	protected final String SP_Category_Delete = "SP_Category_Delete";

	@Resource
	private CategoryMapper categoryMapper;

	@Override
	public void setMapper() {
		this.mapper = categoryMapper;
	}
	
	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Category_Create);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Category_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Category_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_Category_RetrieveNByParent:
			return checkStaffPermission(staffID, SP_Category_RetrieveNByParent);
		default:
			return checkStaffPermission(staffID, SP_Category_RetrieveN);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Category_Retrieve1);
		}
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Category category = (Category) s;
		switch (iUseCaseID) {
		case BaseBO.CASE_Category_RetrieveNByParent:
			Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
			List<BaseModel> ls = ((CategoryMapper) mapper).retrieveNByParent(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

			return ls;
		case CASE_CheckUniqueField:
			Map<String, Object> paramsCheckUniqueField = category.getRetrieveNParam(iUseCaseID, s);
			((CategoryMapper) mapper).checkUniqueField(paramsCheckUniqueField);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return null;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}

	}

	@Override
	protected boolean checkUpdate(int iUseCaseID, BaseModel s) {
		if (s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = Category.ACTION_ERROR_UpdateDelete1;
			return false;
		}

		return super.checkUpdate(iUseCaseID, s);
	}

	@Override
	protected boolean checkDelete(int iUseCaseID, BaseModel s) {
		if (s.getID() == DEFAULT_DB_Row_ID) {
			lastErrorCode = EnumErrorCode.EC_BusinessLogicNotDefined;
			lastErrorMessage = Category.ACTION_ERROR_UpdateDelete1;
			return false;
		}

		return super.checkDelete(iUseCaseID, s);
	}
}
