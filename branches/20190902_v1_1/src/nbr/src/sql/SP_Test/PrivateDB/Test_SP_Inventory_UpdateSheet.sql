SELECT '++++++++++++++++++ Test_SP_Inventory_UpdateSheet.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 状态为0时,修改盘点总结 -------------------------' AS 'Case1';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iInventorySheetID, 1, '薯片', '克', 1, 1, -1, -1, now());
SET @iInventorycommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iInventorycommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;

SELECT '-------------------- Case2: 状态为1时，只可以修改盘点总结 -------------------------' AS 'Case2';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,1,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iInventorySheetID, 1, '薯片', '克', 1, 1, -1, -1, now());
SET @iInventorycommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iInventorycommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;

SELECT '-------------------- Case3: 状态为2时，修改失败，错误码为7 -------------------------' AS 'Case3';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,2,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iInventorySheetID, 1, '薯片', '克', 1, 1, -1, -1, now());
SET @iInventorycommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '盘点单为已审核或者已删除时，修改盘点总结失败', '测试成功', '测试失败') AS 'CASE3 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iInventorycommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;

SELECT '-------------------- Case4: 状态为3时，修改失败，错误码为7 -------------------------' AS 'Case4';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,3,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iInventorySheetID, 1, '薯片', '克', 1, 1, -1, -1, now());
SET @iInventorycommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '盘点单为已审核或者已删除时，修改盘点总结失败', '测试成功', '测试失败') AS 'CASE4 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iInventorycommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;



SELECT '-------------------- Case5: 修改状态0并且没有盘点商品的盘点单 -------------------------' AS 'Case5';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '该盘点单没有盘点商品', '测试成功', '测试失败') AS 'CASE5 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;


SELECT '-------------------- Case6: 修改状态1并且没有盘点商品的盘点单 -------------------------' AS 'Case6';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,1,5,'2017-08-06','...........................');
SET @iInventorySheetID = last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '该盘点单没有盘点商品', '测试成功', '测试失败') AS 'CASE6 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;

SELECT '-------------------- Case7: 修改不存在的盘点单 -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sRemark = 'XXXX';
SET @iInventorySheetID = 999999999;

CALL SP_Inventory_UpdateSheet(@iErrorCode, @sErrorMsg, @iInventorySheetID, @sRemark);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorysheet WHERE F_ID = @iInventorySheetID AND F_Remark = @sRemark;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '盘点单不存在', '测试成功', '测试失败') AS 'CASE7 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @iInventorySheetID;