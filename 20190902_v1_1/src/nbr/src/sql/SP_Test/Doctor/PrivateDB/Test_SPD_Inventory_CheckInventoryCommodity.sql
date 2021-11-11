SELECT '++++++++++++++++++ Test_SPD_Inventory_CheckInventoryCommodtiy.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Inventory_CheckInventoryCommodtiy(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:盘点单不存在盘点商品 -------------------------' AS 'Case2';
-- 
INSERT INTO t_inventorysheet (F_WarehouseID, F_Scope, F_Status, F_StaffID, F_CreateDatetime, F_Remark, F_UpdateDatetime)
VALUES (1, 0, 0, 1, now(), '...', now());
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Inventory_CheckInventoryCommodtiy(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('盘点单', @iID ,'没有盘点商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_inventorysheet WHERE F_ID = @iID;


SELECT '-------------------- Case3:删除的盘点单没有盘点商品 -------------------------' AS 'Case3';
-- 
INSERT INTO t_inventorysheet (F_WarehouseID, F_Scope, F_Status, F_StaffID, F_CreateDatetime, F_Remark, F_UpdateDatetime)
VALUES (1, 0, 3, 1, now(), '...', now());
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Inventory_CheckInventoryCommodtiy(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_inventorysheet WHERE F_ID = @iID;