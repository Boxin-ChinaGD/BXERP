package com.bx.erp.dao.commodity;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;

@Component("commodityMapper")
public interface CommodityMapper extends BaseMapper {
	public List<List<BaseModel>> createSimpleEx(Map<String, Object> params);

	public List<List<BaseModel>> createCombinationEx(Map<String, Object> params);

	public List<List<BaseModel>> createMultiPackagingEx(Map<String, Object> params);

	public List<List<BaseModel>> createServiceEx(Map<String, Object> params);

	public BaseModel updatePrice(Map<String, Object> params);

	public List<BaseModel> retrieveNByFields(Map<String, Object> params);

	public List<BaseModel> retrieveNMultiPackageCommodity(Map<String, Object> params);

	public void checkDependency(Map<String, Object> params);

	public BaseModel updatePurchasingUnit(Map<String, Object> params);

	public BaseModel retrieveInventory(Map<String, Object> params);

	public BaseModel deleteSimple(Map<String, Object> param);

	public BaseModel deleteCombination(Map<String, Object> param);

	public BaseModel deleteMultiPackaging(Map<String, Object> param);

	public BaseModel deleteService(Map<String, Object> param);

	public void checkProvider(Map<String, Object> params);

	public void checkSubCommodity(Map<String, Object> params);

	public void checkPurchasingOrder(Map<String, Object> params);

	public void checkBrandID(Map<String, Object> params);

	public void checkCategoryID(Map<String, Object> params);

	public void checkInventory(Map<String, Object> params);

	public void checkReturnCommoditySheet(Map<String, Object> params);

	public void checkStatus(Map<String, Object> params);

	public void checkType(Map<String, Object> params);

	public void checkWarehousing(Map<String, Object> params);

	public void checkNO(Map<String, Object> params);

	public List<List<BaseModel>> retrieveNUnsalableCommodityEx(Map<String, Object> params);
}
