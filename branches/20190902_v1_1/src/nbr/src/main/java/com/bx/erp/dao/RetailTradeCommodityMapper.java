package com.bx.erp.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("retailTradeCommodityMapper")
public interface RetailTradeCommodityMapper extends BaseMapper{

	public void checkBarcodeID(Map<String, Object> params);
	
	public void checkCommodity(Map<String, Object> params);
	
}
