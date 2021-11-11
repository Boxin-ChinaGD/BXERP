SELECT '++++++++++++++++++ Test_SP_Warehousing_Retrieve1.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询未审核的入库单 -------------------------' AS 'Case1';
INSERT INTO T_Warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (2, 0, 3, 1, 5, now());
SET @iID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_Warehousing_Retrieve1(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT 1 FROM t_Warehousing WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM t_warehousing WHERE F_ID = @iID;

SELECT '-------------------- Case2:查询一个不存在的数据 -------------------------' AS 'Case2';
SET @iID = 9999;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
CALL SP_Warehousing_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:查询已审核的入库单 -------------------------' AS 'Case3';
INSERT INTO t_warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2, 1, 3, 1, 1, now(), 1, now());
SET @iID = Last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iID, 4, 20, 1, '222', 6, 3, 10, now(), 36, now(), now(), now(), 10);
SET @iWarehousingcommodityID1 = Last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iID, 5, 20, 1, '222', 7, 3, 10, now(), 36, now(), now(), now(), 10);
SET @iWarehousingcommodityID2 = Last_insert_id();

SET @sErrorMsg = '';
SET @iErrorCode = 0;
CALL SP_Warehousing_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT IF(found_rows() = 2 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingcommodityID1;
DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingcommodityID2;
DELETE FROM t_warehousing WHERE F_ID = @iID;