package com.bx.erp.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("retailTradeCommoditySourceMapper")	
public interface RetailTradeCommoditySourceMapper extends BaseMapper{

	public void checkNO(Map<String, Object> params);
}
