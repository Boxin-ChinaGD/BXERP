package com.bx.erp.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;

@Component("barcodesMapper")	
public interface BarcodesMapper extends BaseMapper{
	BaseModel deleteBySimpleCommodityID(Map<String, Object> params);
	BaseModel deleteByCombinationCommodityID(Map<String, Object> params);
	BaseModel deleteByMultiPackagingCommodityID(Map<String, Object> params);
}
