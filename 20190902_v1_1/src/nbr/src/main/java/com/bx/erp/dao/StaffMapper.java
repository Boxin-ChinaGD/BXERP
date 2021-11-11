package com.bx.erp.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.model.Staff;

@Component("staffMapper")
public interface StaffMapper extends BaseMapper {
	public Staff resetPassword(Map<String, Object> params);

	public Staff updateOpenidAndUnionid(Map<String, Object> params);

	public void checkICID(Map<String, Object> params);

	public void checkUnionid(Map<String, Object> params);

	public void checkWeChat(Map<String, Object> params);

	public void checkOpenID(Map<String, Object> params);

	public void checkStatus(Map<String, Object> params);

	public void checkPhone(Map<String, Object> params);

	public void checkIsFirstTimeLogin(Map<String, Object> params);

	public void checkName(Map<String, Object> params);
	
	public Staff updateUnsubscribe(Map<String, Object> params);
}
