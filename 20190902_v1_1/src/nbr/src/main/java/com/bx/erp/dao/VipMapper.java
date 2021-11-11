
package com.bx.erp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;

@Component("vipMapper")
public interface VipMapper extends BaseMapper {
	public List<BaseModel> retrieveNByMobileOrVipCardSN(Map<String, Object> params);

	public List<List<BaseModel>> retrieveNVipConsumeHistory(Map<String, Object> params);

	public void checkStatus(Map<String, Object> params);

	public void checkPoint(Map<String, Object> params);

	public void checkName(Map<String, Object> params);

	public BaseModel updateBonus(Map<String, Object> params);
	
	public BaseModel resetBonus(Map<String, Object> params);
	
	public List<BaseModel> retrieveNByMobileOrCardCode(Map<String, Object> params);
}
