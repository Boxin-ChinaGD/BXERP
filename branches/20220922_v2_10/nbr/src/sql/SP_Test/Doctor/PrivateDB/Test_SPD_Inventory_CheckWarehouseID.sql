SELECT '++++++++++++++++++ Test_SPD_Inventory_CheckWarehouseID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Inventory_CheckWarehouseID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

--	做不到
--	SELECT '-------------------- Case2:盘点单对应的仓库ID不正确 -------------------------' AS 'Case2';
--	INSERT INTO T_InventorySheet (F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
--	VALUES (-1,200,0,3,'2017-12-06','...........................zz');
--	SET @iInventorysheetID = LAST_INSERT_ID();
--	-- 
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	-- 
--	CALL SPD_Inventory_CheckWarehouseID(@iErrorCode, @sErrorMsg);
--	SELECT @iErrorCode, @sErrorMsg;
--	-- 
--	SELECT IF(@sErrorMsg = CONCAT('盘点单', @iInventorysheetID ,'对应的仓库ID不正确') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
--	-- 
--	DELETE FROM T_InventorySheet WHERE F_ID = @iInventorysheetID;