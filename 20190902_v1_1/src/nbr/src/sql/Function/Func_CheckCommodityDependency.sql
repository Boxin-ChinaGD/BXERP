-- 检查该商品是否有销存（有依赖）等记录。
-- 返回值：
-- 1，有依赖，不能删除该商品。
-- 0，无依赖，可以删除该商品。
drop function IF EXISTS Func_CheckCommodityDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckCommodityDependency`(
	iID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	DECLARE NO INT;
	DECLARE iType INT;
	
	SELECT F_Type INTO iType FROM t_commodity WHERE F_ID = iID; 
 --	SELECT F_Type INTO iType FROM t_commodity WHERE F_ID = iID;
	
--	IF NO <> 0 
	IF EXISTS(SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = iID AND F_NO > 0) THEN
	    SET sErrorMsg := '该商品还有库存，不能删除';
    ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommoditysource WHERE F_ReducingCommodityID = iID) THEN
		SET sErrorMsg := '该商品有商品来源表依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM t_purchasingorder WHERE F_Status <> 4 AND F_ID IN(SELECT F_PurchasingOrderID FROM t_purchasingordercommodity WHERE F_CommodityID = iID)) THEN
		SET sErrorMsg := '该商品有采购单依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_CommodityID = iID) THEN
		SET sErrorMsg := '该商品有盘点单依赖，不能删除';	
	ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_CommodityID = iID) THEN
		SET sErrorMsg := '该商品有零售单依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iID) THEN
		SET sErrorMsg := '该商品有入库单依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM t_promotionscope WHERE F_CommodityID = iID) THEN
		SET sErrorMsg := '该商品有促销依赖，不能删除';
--	现在不考虑商品历史的依赖
--	ELSEIF EXISTS(SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = iID) THEN
--   		SET sErrorMsg := '该商品有商品历史依赖，不能删除';
   	ELSEIF EXISTS(SELECT 1 FROM t_retailtradedailyreportsummary WHERE F_TopSaleCommodityID = iID) THEN
   		SET sErrorMsg := '该商品有销售日报汇总表依赖，不能删除';
   	ELSEIF EXISTS(SELECT 1 FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID = iID) THEN
   		SET sErrorMsg := '该商品有商品零售报表依赖，不能删除';
--  多包装商品不可以被采购，只能采购单品
--	ELSEIF EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN	
--		SET sErrorMsg := '该单品的多包装商品有采购单依赖，不能删除';
-- 只能盘点非预淘汰状态和预淘汰的普通商品
--	ELSEIF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
--		SET sErrorMsg := '该单品的多包装商品有盘点单依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
		SET sErrorMsg := '该单品的多包装商品有零售单依赖，不能删除';
--  只能入库非预淘汰状态的普通商品
--	ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
--		SET sErrorMsg := '该单品的多包装商品有入库单依赖，不能删除';
-- 多包装不参与促销活动
-- 	ELSEIF EXISTS(SELECT 1 FROM t_promotionscope WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
--		SET sErrorMsg := '该单品的多包装商品有促销依赖，不能删除';
--	现在不考虑商品历史的依赖
--	ELSEIF EXISTS(SELECT 1 FROM t_commodityhistory WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
--		SET sErrorMsg := '该单品的多包装商品有商品历史依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM t_retailtradedailyreportsummary WHERE F_TopSaleCommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
		SET sErrorMsg := '该单品的多包装商品有销售日报汇总表依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM t_retailtradedailyreportbycommodity WHERE F_CommodityID IN (SELECT F_ID FROM t_commodity WHERE F_RefCommodityID = iID)) THEN
		SET sErrorMsg := '该单品的多包装商品有商品零售报表依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM t_commodity WHERE F_RefCommodityID = iID AND F_Status <> 2) THEN
		SET sErrorMsg := '请先删除商品的副单位';
	ELSEIF EXISTS(SELECT 1 FROM t_subcommodity ts WHERE ts.F_SubCommodityID = iID AND EXISTS (SELECT 1 FROM t_commodity c WHERE c.F_ID = ts.F_CommodityID AND c.F_Status <> 2)) THEN
		SET sErrorMsg := '要删除的商品是组合商品的一部分，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = iID) THEN
	  	SET sErrorMsg := '商品在采购退货单有依赖';
	ELSEIF EXISTS (SELECT 1 FROM t_couponscope WHERE F_CommodityID = iID) THEN
		SET sErrorMsg := '商品有优惠券范围依赖，不能删除';
	ELSE 
		SET sErrorMsg := '';
	END IF;		
	RETURN sErrorMsg;	
END;