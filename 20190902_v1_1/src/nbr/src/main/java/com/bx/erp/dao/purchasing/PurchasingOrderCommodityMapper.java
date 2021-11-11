package com.bx.erp.dao.purchasing;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;

@Component("purchasingOrderCommodityMapper")
public interface PurchasingOrderCommodityMapper extends BaseMapper {
	public List<List<BaseModel>> retrieveNWarhousing(Map<String, Object> params);
	public List<BaseModel> retrieveNNoneWarhousing(Map<String, Object> params);
}
