SELECT '++++++++++++++++++ Test_SP_PurchasingOrderCommodity_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 正常查询 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPurchasingOrderID = 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT * FROM t_PurchasingOrderCommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: F_PurchasingOrderID输入一个不存在的数字(-1) -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPurchasingOrderID = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iErrorCode;
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT * FROM t_PurchasingOrderCommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3: 创建一个采购订单，查询对应的入库数量应当为null -------------------------' AS 'Case3';
INSERT INTO T_PurchasingOrder(F_Status, F_StaffID,F_ProviderID,F_ProviderName,F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(0,5,1,'默认供应商',now(),now(),	now());
SET @iPurchasingOrderID = last_insert_id();
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (@iPurchasingOrderID,1,50,'可比克薯片',1,1,1); 


SET @iPurchasingordercommodityID=last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT * FROM t_PurchasingOrderCommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';
DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;


SELECT '-------------------- Case4: 创建一个入库单，入库数量为0，查询对应的入库数量应当为0 -------------------------' AS 'Case4';
INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID,F_ProviderID,F_ProviderName,F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2,0,5,1,'默认供应商',now(),now(),	now());
SET @iPurchasingOrderID = last_insert_id();
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (@iPurchasingOrderID,1,50,'可比克薯片',1,1,1);
-- 
INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID)
VALUES (1, 3, 1, 1, now(), @iPurchasingOrderID);
SET @iWarehousingID=last_insert_id();
--
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime,F_NOSalable) 
VALUES (@iwarehousingID, 1, 0/*F_NO*/, 1,'可比克薯片',1, 10, 2000, now(),36, now(),0);

SET @iWarehousingcommodityID=last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT * FROM t_PurchasingOrderCommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';
DELETE FROM t_warehousingcommodity WHERE F_WarehousingID = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;


SELECT '-------------------- Case5: 入库第二次，入库数量为10，查询对应的入库数量应当为10 -------------------------' AS 'Case5';
INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID,F_ProviderID,F_ProviderName,F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2,0,5,1,'默认供应商',now(),now(),	now());
SET @iPurchasingOrderID = last_insert_id();
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (@iPurchasingOrderID,1,50,'可比克薯片',1,1,1);
-- 
INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID)
VALUES (1, 3, 1, 1, now(), @iPurchasingOrderID);
SET @iWarehousingID=last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime,F_NOSalable) 
VALUES (@iwarehousingID, 1, 10, 1,'可比克薯片',1, 10, 2000, now(),36, now(),0);

SET @iWarehousingcommodityID=last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT * FROM t_PurchasingOrderCommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingcommodityID;
DELETE FROM T_Warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;

SELECT '-------------------- Case6:  将所有采购数量入库完成，查询对应的入库数量应当为所有的采购数量 -------------------------' AS 'Case6';
INSERT INTO T_PurchasingOrder(F_ShopID, F_Status, F_StaffID,F_ProviderID,F_ProviderName,F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(2,0,5,1,'默认供应商',now(),now(),	now());
SET @iPurchasingOrderID = last_insert_id();
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (@iPurchasingOrderID,1,50,'可比克薯片',1,1,1);
INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID)
VALUES (1, 3, 1, 1, now(), @iPurchasingOrderID);
-- 
SET @iWarehousingID=last_insert_id();
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime,F_NOSalable) 
VALUES (@iwarehousingID, 1, 50, 1,'可比克薯片',1, 10, 2000, now(),36, now(),0);

SET @iWarehousingcommodityID=last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_PurchasingOrderCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT * FROM t_PurchasingOrderCommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingcommodityID;
DELETE FROM T_Warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM t_purchasingorder WHERE F_ID = @iPurchasingOrderID;