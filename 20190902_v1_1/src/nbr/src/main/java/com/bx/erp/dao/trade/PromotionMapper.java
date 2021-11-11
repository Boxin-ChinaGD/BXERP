package com.bx.erp.dao.trade;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;

@Component("promotionMapper")
public interface PromotionMapper extends BaseMapper {

	public void checkSN(Map<String, Object> params);

	public void checkStatus(Map<String, Object> params);

	public void checkScope(Map<String, Object> params);

	public void checkType(Map<String, Object> params);

	public void checkDatetime(Map<String, Object> params);
}
