package com.bx.erp.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.BaseModel;

@Component("companyMapper")
public interface CompanyMapper extends BaseMapper {
	public BaseModel updateSubmchid(Map<String, Object> params);
	public BaseModel updateVipSystemTip(Map<String, Object> params);
	public BaseModel updateMerchantID(Map<String, Object> params);
	public BaseModel retrieve1ByMerchantID(Map<String, Object> params);
	public List<BaseModel> retrieveNByVipMobile(Map<String, Object> params);
	public List<List<BaseModel>> matchVip(Map<String, Object> params);
}
