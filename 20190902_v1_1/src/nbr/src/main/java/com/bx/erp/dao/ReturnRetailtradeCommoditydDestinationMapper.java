package com.bx.erp.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("returnRetailtradeCommoditydDestinationMapper")
public interface ReturnRetailtradeCommoditydDestinationMapper extends BaseMapper {

	public void checkNO(Map<String, Object> params);

	public void checkWarehousingID(Map<String, Object> params);

}
