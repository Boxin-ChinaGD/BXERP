package com.bx.erp.action.bo;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.BaseAction;
import com.bx.erp.dao.RetailTradeMapper;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

@Component("retailTradeBO")
@Scope("prototype")
public class RetailTradeBO extends BaseBO {
	protected final String SP_RetailTrade_UploadTrade = "SP_RetailTrade_UploadTrade";
	protected final String SP_RetailTrade_RetrieveOldTrade = "SP_RetailTrade_RetrieveOldTrade";
	protected final String SP_RetailTrade_Retrieve1 = "SP_RetailTrade_Retrieve1";
	protected final String SP_RetailTrade_RetrieveNByCommodityNameInTimeRange = "SP_RetailTrade_RetrieveNByCommodityNameInTimeRange";
	@Resource
	private RetailTradeMapper retailTradeMapper;
	
	/*符合查询条件的商品销售数量*/
	protected int totalCommNO;
	
	/*符合查询条件的商品销售总额*/
	protected double retailAmount;
	
	/*符合查询条件的商品销售毛利*/
	protected double totalGross;
	
	
	public int getTotalCommNO() {
		return totalCommNO;
	}

	public double getRetailAmount() {
		return retailAmount;
	}

	public double getTotalGross() {
		return totalGross;
	}

	@Override
	protected void setMapper() {
		this.mapper = retailTradeMapper;
	}

	@Override
	protected boolean checkCreateExPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTrade_UploadTrade);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb:
			return checkStaffPermission(staffID, SP_RetailTrade_RetrieveNByCommodityNameInTimeRange);
		default:
			return checkStaffPermission(staffID, SP_RetailTrade_RetrieveOldTrade);
		}
	}
	
	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_RetailTrade_Retrieve1);
		}
	}
	
	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		case BaseBO.CASE_RetailTrade_RetrieveNByCommodityNameInTimeRangeFromWeb:
			Map<String, Object> params = s.getRetrieveNParam(iUseCaseID, s);
			List<BaseModel> ls = ((RetailTradeMapper) mapper).retrieveNByCommodityNameInTimeRange(params);
			lastErrorCode = EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
			lastErrorMessage = params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString();
			//
			retailAmount = Double.parseDouble(params.get("dRetailAmount").toString());
			totalGross = Double.parseDouble(params.get("dTotalGross").toString());
			totalCommNO = Integer.parseInt(params.get("dTotalCommNO").toString());
			//
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
}
