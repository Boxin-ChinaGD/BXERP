package com.bx.erp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;

@Component("couponCodeMapper")
public interface CouponCodeMapper extends BaseMapper {
	public BaseModel consume(Map<String, Object> params);

	public void retrieveNTotalByVipID(Map<String, Object> params);

	public List<BaseModel> retrieveNByVipID(Map<String, Object> params);
}
