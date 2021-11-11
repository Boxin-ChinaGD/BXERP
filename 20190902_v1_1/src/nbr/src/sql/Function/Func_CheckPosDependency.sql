-- 删除Pos时，应该检查的依赖。
-- 返回值：
-- ''，可以删除Pos。
-- 其它errorMsg，不可以删除POS
DROP FUNCTION IF EXISTS Func_CheckPosDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckPosDependency`(
	iPosID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
--	IF EXISTS (SELECT 1 FROM t_staff WHERE F_IDInPOS = iPosID AND F_Status = 0) THEN
--		SET sErrorMsg := '该POS机已被员工使用，不能删除';
	IF EXISTS(SELECT 1 FROM t_retailtrade WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '该POS机有零售单依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM T_RetailTradeAggregation WHERE F_PosID = iPosID) THEN
		SET sErrorMsg := '该POS机有收银汇总依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM T_PromotionSyncCacheDispatcher WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '该POS机有促销同步缓存调度表依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM T_CommoditySyncCacheDispatcher WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '该POS机有商品同步缓存调度表依赖，不能删除';	   
	ELSEIF EXISTS(SELECT 1 FROM T_BrandSyncCacheDispatcher WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '该POS机有品牌同步缓存调度表依赖，不能删除';
   	ELSEIF EXISTS(SELECT 1 FROM T_CategorySyncCacheDispatcher WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '该POS机有类别同步缓存调度表依赖，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM T_BarcodesSyncCacheDispatcher WHERE F_POS_ID = iPosID) THEN
		SET sErrorMsg := '该POS机有条形码同步缓存调度表依赖，不能删除'; 						
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;