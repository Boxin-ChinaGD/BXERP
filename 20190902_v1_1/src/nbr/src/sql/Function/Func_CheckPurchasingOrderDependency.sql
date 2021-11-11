-- 删除采购单时，应该检查的依赖。
-- 返回值：
-- ''，可以删除采购单。
-- 其它errorMsg，不可以删除采购单
DROP FUNCTION IF EXISTS Func_CheckPurchasingOrderDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckPurchasingOrderDependency`(
	iPurchasingOrderID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF EXISTS (SELECT 1 FROM T_Warehousing WHERE F_PurchasingOrderID = iPurchasingOrderID) THEN
		SET sErrorMsg := '该采购单已入库，不允许删除';
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;