SELECT '++++++++++++++++++ Test_SP_Inventory_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 状态为0正常删除盘点单和盘点单商品表 -------------------------' AS 'Case1';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,3,'2017-12-06','...........................zz');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();

INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES ( @iID, 1, '薯片', 1, 1, 1, 0, 0);
SET @inventorycommodityID = last_insert_id();

CALL SP_Inventory_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_ID = @inventorycommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: 不能删除已删除的盘点单和盘点单商品表，错误码为7 -------------------------' AS 'Case2';
INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,3,3,'2017-12-06','...........................zz');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();

CALL SP_Inventory_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_inventorysheet WHERE F_ID = @iID;
SELECT '-------------------- Case3: 状态为1不能删除盘点单和盘点单商品表，错误码为7 -------------------------' AS 'Case3';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,1,3,'2017-12-06','...........................zz');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();

CALL SP_Inventory_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_inventorysheet WHERE F_ID = @iID;
SELECT '-------------------- Case4: 状态为2不能删除盘点单和盘点单商品表,错误码为7 -------------------------' AS 'Case4';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,2,3,'2017-12-06','...........................zz');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();

CALL SP_Inventory_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_inventorysheet WHERE F_ID = @iID;