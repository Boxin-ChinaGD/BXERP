-- 删除供应商时，应该检查的依赖。
-- 返回值：
-- ''，可以删除供应商。
-- 其它errorMsg，不可以删除供应商
DROP FUNCTION IF EXISTS Func_CheckProviderDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckProviderDependency`(
	iProviderID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF EXISTS (SELECT 1 FROM t_providercommodity WHERE F_ProviderID = iProviderID) THEN
		SET sErrorMsg := '供应商已经存在商品，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM T_Warehousing WHERE F_ProviderID = iProviderID) THEN
		SET sErrorMsg := '供应商已被入库单引用，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM T_ReturnCommoditySheet WHERE F_ProviderID = iProviderID) THEN 
		SET sErrorMsg := '供应商已被退货单引用，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM T_PurchasingOrder WHERE F_ProviderID = iProviderID ) THEN 
		SET sErrorMsg := '供应商已被采购订单引用，不能删除';	
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;