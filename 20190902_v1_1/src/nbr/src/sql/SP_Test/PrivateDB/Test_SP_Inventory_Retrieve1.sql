SELECT '++++++++++++++++++ Test_SP_Inventory_Retrieve1.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:’˝≥£≤È—Ø -------------------------' AS 'Case1';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,5,'2017-08-06','...........................');

SET @iID = last_insert_id();

INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES ( @iID, 1, 1, 1, 0, 0);

SET @iInventoryCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Inventory_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: ”–…æ≥˝◊¥Ã¨µƒ -------------------------' AS 'Case2';
SET @sErrorMsg = '';

CALL SP_Inventory_Delete(@iErrorCode, @sErrorMsg, @iID); 

CALL SP_Inventory_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM T_InventorySheet WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 2, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'Case2 Testing Result';

DELETE FROM T_InventoryCommodity WHERE F_ID = @iInventoryCommodityID;

DELETE FROM T_InventorySheet WHERE F_ID = @iID;