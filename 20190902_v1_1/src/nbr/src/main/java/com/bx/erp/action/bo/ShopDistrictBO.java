package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.dao.ShopDistrictMapper;
import com.bx.erp.model.BaseModel;

@Component("shopDistrictBO")
@Scope("prototype")
public class ShopDistrictBO extends BaseBO {
	protected final String SP_ShopDistrict_Create = "SP_ShopDistrict_Create";
	protected final String SP_ShopDistrict_Retrieve1 = "SP_ShopDistrict_Retrieve1";
	protected final String SP_ShopDistrict_RetrieveN = "SP_ShopDistrict_RetrieveN";
	
	@Resource
	private ShopDistrictMapper shopDistrictMapper;
	
	@Override
	protected void setMapper() {
		// TODO 自动生成的方法存根
		this.mapper=shopDistrictMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ShopDistrict_Create);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ShopDistrict_Retrieve1);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_ShopDistrict_RetrieveN);
		}
	}
}
