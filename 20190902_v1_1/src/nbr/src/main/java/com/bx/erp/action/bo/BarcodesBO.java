package com.bx.erp.action.bo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.bx.erp.dao.BarcodesMapper;
import com.bx.erp.model.BaseModel;

@Component("barcodesBO")
@Scope("prototype")
public class BarcodesBO extends BaseBO {
	protected final String SP_Barcodes_RetrieveN = "SP_Barcodes_RetrieveN";
	protected final String SP_Barcodes_Create = "SP_Barcodes_Create";
	protected final String SP_Barcodes_Delete = "SP_Barcodes_Delete";
	protected final String SP_Barcodes_Retrieve1 = "SP_Barcodes_Retrieve1";
	protected final String SP_Barcodes_Update = "SP_Barcodes_Update";
	
	@Resource
	private BarcodesMapper barcodesMapper;

	@Override
	protected void setMapper() {
		this.mapper = barcodesMapper;
	}

	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Barcodes_Create);
		}
	}

	@Override
	protected boolean checkDeletePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Barcodes_Delete);
		}
	}

	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Barcodes_RetrieveN);
		}
	}

	@Override
	public boolean checkRetrieve1Permission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Barcodes_Retrieve1);
		}
	}

	@Override
	public boolean checkUpdatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_Barcodes_Update);
		}
	}

}
