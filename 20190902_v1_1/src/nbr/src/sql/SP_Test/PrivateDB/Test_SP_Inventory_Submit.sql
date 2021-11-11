SELECT '++++++++++++++++++ Test_SP_Inventory_Submit.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 盘点单的状态还是为0时 -------------------------' AS 'Case1';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,1,'2017-04-06','...........................');
SET @ID = last_insert_id();  

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@ID, 1, '薯片', '克', 1, 1, -1, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @ID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = @ID;
DELETE FROM T_InventorySheet WHERE F_ID = @ID;

SELECT '-------------------- Case2: 盘点单状态为1时 -------------------------' AS 'Case2';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,1,1,'2017-04-06','...........................');
SET @InventorySheetID = last_insert_id();     

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@InventorySheetID, 1, '薯片', '克', 1, 1, -1, -1, now());
SET @InventorySheetCommodityID = last_insert_id();  

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @InventorySheetID);
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @InventorySheetID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '盘点单状态不为0，不能进行提交', '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @InventorySheetCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @InventorySheetID;

SELECT '-------------------- Case3: 盘点单状态为2时 -------------------------' AS 'Case3';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,2,1,'2017-04-06','...........................');
SET @InventorySheetID = last_insert_id();     

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@InventorySheetID, 1, '薯片', '克', 1, 1, -1, -1, now());
SET @InventorySheetCommodityID = last_insert_id();  

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @InventorySheetID);
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @InventorySheetID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '盘点单状态不为0，不能进行提交', '测试成功', '测试失败') AS 'CASE3 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @InventorySheetCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @InventorySheetID;

SELECT '-------------------- Case4: 盘点单状态为其他值时 -------------------------' AS 'Case4';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,-1,1,'2017-04-06','...........................');
SET @InventorySheetID = last_insert_id();     

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@InventorySheetID, 1, '薯片', '克', 1, 1, -1, -1, now());
SET @InventorySheetCommodityID = last_insert_id();  

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @InventorySheetID);
SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @InventorySheetID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '盘点单状态不为0，不能进行提交', '测试成功', '测试失败') AS 'CASE4 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @InventorySheetCommodityID;
DELETE FROM T_InventorySheet WHERE F_ID = @InventorySheetID;


SELECT '-------------------- Case5: 提交没有盘点商品的盘点单 -------------------------' AS 'Case5';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,1,'2017-04-06','...........................');
SET @ID = last_insert_id();  


SET @iErrorCode = 0; 
SET @sErrorMsg = '';

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '该盘点单没有盘点商品', '测试成功', '测试失败') AS 'CASE5 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @ID;


SELECT '-------------------- Case6: 提交不存在的盘点单 -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @ID = 99999999999;

CALL SP_Inventory_Submit(@iErrorCode, @sErrorMsg, @ID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @ID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '盘点单不存在', '测试成功', '测试失败') AS 'CASE6 Testing Result';
