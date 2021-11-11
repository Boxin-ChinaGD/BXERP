//package com.bx.erp.action.bo;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Component;
//
//import com.bx.erp.dao.VipBelongingMapper;
//import com.bx.erp.model.BaseModel;
//
//@Component("vipBelongingBO")
//public class VipBelongingBO extends BaseWxBO {
//	@Resource
//	private VipBelongingMapper vipBelongingMapper;
//
//	@Override
//	protected void setMapper() {
//		this.mapper = vipBelongingMapper;
//	}
//
//	@Override
//	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
//		// ...
//		return true;
//	}
//
//}
