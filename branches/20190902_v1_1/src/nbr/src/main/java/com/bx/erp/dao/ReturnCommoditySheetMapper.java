package com.bx.erp.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.ReturnCommoditySheet;

@Component("returnCommoditySheetMapper")
public interface ReturnCommoditySheetMapper extends BaseMapper{
	public ReturnCommoditySheet approve(Map<String, Object> params);
	public void CheckProviderID(Map<String, Object> params);
	public void CheckStaffID(Map<String, Object> params);
	public void CheckStatus(Map<String, Object> params);
	public void CheckBarcodeID(Map<String, Object> params);
	public void CheckCommodity(Map<String, Object> params);
	public void CheckReturnCommoditySheetID(Map<String, Object> params);
	public void CheckReturnCommoditySheetCommodity(Map<String, Object> params);
}
