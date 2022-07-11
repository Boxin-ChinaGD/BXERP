-- 删除仓库时，应该检查的依赖。
-- 返回值：
-- ''，可以删除仓库。
-- 其它errorMsg，不可以删除仓库
DROP FUNCTION IF EXISTS Func_CheckWarehouseDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckWarehouseDependency`(
	iWarehouseID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF EXISTS (SELECT 1 FROM T_Warehousing WHERE F_WarehouseID = iWarehouseID) THEN
		SET sErrorMsg := '该仓库已被入库单使用，不能删除';
	ELSEIF EXISTS(SELECT 1 FROM T_InventorySheet WHERE F_WarehouseID = iWarehouseID) THEN
		SET sErrorMsg := '该仓库已被盘点单使用，不能删除';
	ELSE 
		SET sErrorMsg := '';
	END IF;	
	RETURN sErrorMsg;
END;