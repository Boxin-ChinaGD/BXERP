package com.bx.erp.dao.commodity;

import java.util.Map;

import org.springframework.stereotype.Component;
import com.bx.erp.dao.BaseMapper;

@Component("refCommodityHubMapper")
public interface RefCommodityHubMapper extends BaseMapper {
	public void checkName(Map<String, Object> params);

	public void checkBarcode(Map<String, Object> params);

	public void checkPriceRetail(Map<String, Object> params);

	public void checkShelfLife(Map<String, Object> params);

	public void checkSpecification(Map<String, Object> params);

	public void checkType(Map<String, Object> params);
}
