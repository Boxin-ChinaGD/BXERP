package com.bx.erp.action.bo.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.commodity.CommodityMapper;
import com.bx.erp.dao.commodity.CommodityShopInfoMapper;
import com.bx.erp.model.BaseModel;

@Component("commodityShopInfoBO")
@Scope("prototype")
public class CommodityShopInfoBO extends BaseBO {
	
	protected final String SP_CommodityShopInfo_Create = "SP_CommodityShopInfo_Create";
	protected final String SP_CommodityShopInfo_RetrieveN = "SP_CommodityShopInfo_RetrieveN";

	@Resource
	protected CommodityShopInfoMapper commodityShopInfoMapper;

	@Override
	public void setMapper() {
		this.mapper = commodityShopInfoMapper;
	}
	
	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		// TODO
		return true;
//		switch (iUseCaseID) {
//		case CASE_Commodity_CreateComposition:
//			return checkStaffPermission(staffID, SP_Commodity_CreateCombination);
//		case CASE_Commodity_CreateMultiPackaging:
//			return checkStaffPermission(staffID, SP_Commodity_CreateMultiPackaging);
//		case CASE_Commodity_CreateService:
//			return checkStaffPermission(staffID, SP_Commodity_CreateService);
//		default:
//			return checkStaffPermission(staffID, SP_Commodity_CreateSimple);
//		}
	}
	
	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		// TODO
		return true;
//		switch (iUseCaseID) {
//		case CASE_UnsalableCommodity_RetrieveN:
//			return checkStaffPermission(staffID, SP_UnsalableCommodity_RetrieveN);
//		default:
//			throw new RuntimeException("Not yet implemented!");
//		}
	}
}
