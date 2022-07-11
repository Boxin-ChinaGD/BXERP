package com.bx.erp.action.bo.commodity;

import java.util.List;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.dao.commodity.CommodityMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Company;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;

@Component("commodityBO")
@Scope("prototype")
public class CommodityBO extends BaseBO {
	// protected final String SP_Commodity_RetrieveNByFields =
	// "SP_Commodity_RetrieveNByFields";
	protected final String SP_Commodity_DeleteSimple = "SP_Commodity_DeleteSimple";
	protected final String SP_Commodity_DeleteMultiPackaging = "SP_Commodity_DeleteMultiPackaging";
	protected final String SP_Commodity_Retrieve1 = "SP_Commodity_Retrieve1";
	protected final String SP_Commodity_RetrieveN = "SP_Commodity_RetrieveN";
	protected final String SP_Commodity_Update = "SP_Commodity_Update";
	protected final String SP_Commodity_UpdatePrice = "SP_Commodity_UpdatePrice";
	protected final String SP_Commodity_RetrieveNMultiPackageCommodity = "SP_Commodity_RetrieveNMultiPackageCommodity";
	protected final String SP_Commodity_CheckDependency = "SP_Commodity_CheckDependency";
	protected final String SP_Commodity_UpdatePurchasingUnit = "SP_Commodity_UpdatePurchasingUnit";
	protected final String SP_Commodity_RetrieveInventory = "SP_Commodity_RetrieveInventory";
	protected final String SP_Commodity_DeleteCombination = "SP_Commodity_DeleteCombination";
	protected final String SP_Commodity_RetrieveN_CheckUniqueField = "SP_Commodity_RetrieveN_CheckUniqueField";
	protected final String SP_Commodity_DeleteService = "SP_Commodity_DeleteService";
	protected final String SP_Commodity_CreateCombination = "SP_Commodity_CreateCombination";
	protected final String SP_Commodity_CreateMultiPackaging = "SP_Commodity_CreateMultiPackaging";
	protected final String SP_Commodity_CreateService = "SP_Commodity_CreateService";
	protected final String SP_Commodity_CreateSimple = "SP_Commodity_CreateSimple";
	protected final String SP_UnsalableCommodity_RetrieveN = "SP_UnsalableCommodity_RetrieveN";

	@Resource
	protected CommodityMapper commodityMapper;

	@Override
	public void setMapper() {
		this.mapper = commodityMapper;
	}

	protected boolean checkUpdateEx(int iUseCaseID, BaseModel s) {
		return doCheckUpdate(iUseCaseID, s);
	}

	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		Commodity comm = (Commodity) s;

		switch (iUseCaseID) {
		case CASE_RetrieveNMultiPackageCommodity:
			Map<String, Object> paramsMulti = comm.getRetrieveNParam(iUseCaseID, s);
			List<?> multils = ((CommodityMapper) mapper).retrieveNMultiPackageCommodity(paramsMulti);

			setTotalRecord(Integer.parseInt(paramsMulti.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsMulti.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsMulti.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

			return multils;
		case CASE_CheckDeleteDependency:
			Map<String, Object> paramsCheckDependency = comm.getRetrieveNParam(iUseCaseID, s);
			((CommodityMapper) mapper).checkDependency(paramsCheckDependency);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsCheckDependency.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsCheckDependency.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

			return null;
		case CASE_CheckUniqueField:
			Map<String, Object> paramsCheckUniqueField = comm.getRetrieveNParam(iUseCaseID, s);
			((CommodityMapper) mapper).checkUniqueField(paramsCheckUniqueField);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = paramsCheckUniqueField.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return null;
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}

	@Override
	protected BaseModel doUpdate(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Commodity_UpdatePrice:
			Map<String, Object> params = ((Commodity) s).getUpdateParam(iUseCaseID, s);
			BaseModel comm = ((CommodityMapper) mapper).updatePrice(params);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

			return comm;
		case CASE_UpdatePurchasingUnit:
			Map<String, Object> params1 = ((Commodity) s).getUpdateParam(iUseCaseID, s);
			BaseModel commodity = ((CommodityMapper) mapper).updatePurchasingUnit(params1);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

			return commodity;
		default:
			return super.doUpdate(iUseCaseID, s);
		}
	}

	@Override
	protected BaseModel doRetrieve1(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Commodity_RetrieveInventory:
			Map<String, Object> retrieve1Param = s.getRetrieve1Param(iUseCaseID, s);
			BaseModel retrieveInventory = ((CommodityMapper) mapper).retrieveInventory(retrieve1Param);

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = retrieve1Param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

			return retrieveInventory;
		default:
			return super.doRetrieve1(iUseCaseID, s);
		}

	}

	@Override
	protected List<List<BaseModel>> doUpdateEx(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return super.doUpdateEx(iUseCaseID, s);
		}
	}

	@Override
	protected List<List<BaseModel>> doCreateEx(int iUseCaseID, BaseModel s) {
		List<List<BaseModel>> lsls = null;

		Map<String, Object> params = s.getCreateParamEx(iUseCaseID, s);
		switch (iUseCaseID) {
		case CASE_Commodity_CreateSingle:
			lsls = ((CommodityMapper) mapper).createSimpleEx(params);
			break;
		case CASE_Commodity_CreateComposition:
			lsls = ((CommodityMapper) mapper).createCombinationEx(params);
			break;
		case CASE_Commodity_CreateService:
			lsls = ((CommodityMapper) mapper).createServiceEx(params);
			break;
		case CASE_Commodity_CreateMultiPackaging:
			lsls = ((CommodityMapper) mapper).createMultiPackagingEx(params);
			break;
		default:
			super.doCreateEx(iUseCaseID, s);
		}
		//
		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

		return lsls;
	}

	@Override
	protected BaseModel doDelete(int iUseCaseID, BaseModel s) {
		BaseModel baseModel = null;

		Map<String, Object> param = s.getDeleteParam(BaseBO.INVALID_CASE_ID, s);
		switch (iUseCaseID) {
		case CASE_Commodity_DeleteSimple:
			baseModel = ((CommodityMapper) mapper).deleteSimple(param);
			break;
		case CASE_Commodity_DeleteCombination:
			baseModel = ((CommodityMapper) mapper).deleteCombination(param);
			break;
		case CASE_Commodity_DeleteMultiPackaging:
			baseModel = ((CommodityMapper) mapper).deleteMultiPackaging(param);
			break;
		case CASE_Commodity_DeleteService:
			baseModel = ((CommodityMapper) mapper).deleteService(param);
			break;
		default:
			baseModel = ((CommodityMapper) mapper).deleteSimple(param);
			break;
		}
		//
		lastErrorCode = EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
		lastErrorMessage = param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();

		return baseModel;
	}

	@Override
	protected boolean checkCreateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Commodity_CreateComposition:
			return checkStaffPermission(staffID, SP_Commodity_CreateCombination);
		case CASE_Commodity_CreateMultiPackaging:
			return checkStaffPermission(staffID, SP_Commodity_CreateMultiPackaging);
		case CASE_Commodity_CreateService:
			return checkStaffPermission(staffID, SP_Commodity_CreateService);
		default:
			return checkStaffPermission(staffID, SP_Commodity_CreateSimple);
		}
	}

	@Override
	protected boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Commodity_UpdatePrice:
			return checkStaffPermission(staffID, SP_Commodity_UpdatePrice);
		case CASE_UpdatePurchasingUnit:
			return checkStaffPermission(staffID, SP_Commodity_UpdatePurchasingUnit);
		default:
			return checkStaffPermission(staffID, SP_Commodity_Update);
		}
	}

	@Override
	protected boolean checkUpdateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Commodity_Update);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_Commodity_DeleteSimple:
			return checkStaffPermission(staffID, SP_Commodity_DeleteSimple);
		case BaseBO.CASE_Commodity_DeleteMultiPackaging:
			return checkStaffPermission(staffID, SP_Commodity_DeleteMultiPackaging);
		case BaseBO.CASE_Commodity_DeleteCombination:
			return checkStaffPermission(staffID, SP_Commodity_DeleteCombination);
		case BaseBO.CASE_Commodity_DeleteService:
			return checkStaffPermission(staffID, SP_Commodity_DeleteService);
		default:
			return checkStaffPermission(staffID, SP_Commodity_DeleteSimple);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_Commodity_RetrieveInventory:
			return checkStaffPermission(staffID, SP_Commodity_RetrieveInventory);
		default:
			return super.checkRetrieve1Permission(staffID, iUseCaseID, s);
		}
	}

	@Override
	public boolean checkRetrieve1ExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Commodity_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_RetrieveNMultiPackageCommodity:
			return checkStaffPermission(staffID, SP_Commodity_RetrieveNMultiPackageCommodity);
		case CASE_CheckDeleteDependency:
			return checkStaffPermission(staffID, SP_Commodity_CheckDependency);
		case CASE_CheckUniqueField:
			return checkStaffPermission(staffID, SP_Commodity_RetrieveN_CheckUniqueField);
		default:
			return checkStaffPermission(staffID, SP_Commodity_RetrieveN);
		}
	}

	@Override
	protected List<List<BaseModel>> doRetrieveNEx(int iUseCaseID, BaseModel s) {
		Map<String, Object> params = ((Commodity) s).getRetrieveNParamEx(iUseCaseID, s);
		switch (iUseCaseID) {
		case CASE_UnsalableCommodity_RetrieveN:
			List<List<BaseModel>> ls1 = ((CommodityMapper) mapper).retrieveNUnsalableCommodityEx(params);

			// setTotalRecord(Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iTotalRecord).toString()));

			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			return ls1;
		default:
			return super.doRetrieveNEx(iUseCaseID, s);
		}
	}

	@Override
	protected boolean checkRetrieveNExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case CASE_UnsalableCommodity_RetrieveN:
			return checkStaffPermission(staffID, SP_UnsalableCommodity_RetrieveN);
		default:
			throw new RuntimeException("Not yet implemented!");
		}
	}

	@Override
	protected boolean doCheckRetrieveNEx(int iUseCaseID, BaseModel s) {
		// 检查CompanyID是否当前DB对应的Company的ID。如果不是，返回EC_HACK
		switch (iUseCaseID) {
		case BaseBO.CASE_UnsalableCommodity_RetrieveN:
			Commodity comm = (Commodity) s;
			boolean isCurrentCompanyID = false;
			List<BaseModel> companyList = CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_Company).readN(false, false);
			for (BaseModel bm : companyList) {
				Company company = (Company) bm;
				if (company.getID() == (comm.getCompanyID())) {
					isCurrentCompanyID = true;
					break;
				}
			}
			if (!isCurrentCompanyID) {
				lastErrorCode = EnumErrorCode.EC_Hack;
				return false;
			}
			break;
		default:
			return true;
		}
		return true;
	}
}
