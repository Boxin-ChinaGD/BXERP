SELECT '++++++++++++++++++ Test_SPD_InventoryCommodity_CheckInventorySheetID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_InventoryCommodity_CheckInventorySheetID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

--	做不到
--	SELECT '-------------------- Case2:盘点单对应的盘点单ID不正确 -------------------------' AS 'Case2';
--	INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
--	VALUES (-1, 1, '薯片', 1, 1, 1, 0, 0);
--	SET @iInventoryCommodityID = LAST_INSERT_ID();
--	-- 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	-- 
--	CALL SPD_InventoryCommodity_CheckInventorySheetID(@iErrorCode, @sErrorMsg);
--	SELECT @iErrorCode, @sErrorMsg;
--	-- 
--	SELECT IF(@sErrorMsg = CONCAT('盘点单商品', @iInventoryCommodityID ,'对应的盘点单ID不正确') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
--	-- 
--	DELETE FROM t_Inventorycommodity WHERE F_ID = @iInventorysheetCommodityID;