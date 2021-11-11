SELECT '++++++++++++++++++ Test_SPD_WarehousingCommodity_CheckCommodity.sql ++++++++++++++++++++';
SELECT '------------------ 正常测试 --------------------' AS 'CASE1';
--  
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_WarehousingCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';
--  
SELECT '------------------ 入库单商品不是单品 --------------------' AS 'CASE2';
INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0, 3, 1, 1, now(), 1, now());
SET @iWarehousingID = Last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iWarehousingID, 52, 1, 1, '乐事青椒味薯片3', 56, 11.1, 11.1, now(), 36, now(), now(), now());
SET @iWarehousingCommodityID = Last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_WarehousingCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('入库单商品', @iWarehousingCommodityID, '不是单品'), '测试成功', '测试失败') AS 'Case2 Testing Result';
--  
DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
-- 
SELECT '------------------ 入库单商品入库数量小于1 --------------------' AS 'CASE3';
--  
INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0, 3, 1, 1, now(), 1, now());
SET @iWarehousingID = Last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iWarehousingID, 2, 0, 1, '乐事青椒味薯片3', 56, 11.1, 11.1, now(), 36, now(), now(), now());
SET @iWarehousingCommodityID = Last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_WarehousingCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('入库单商品', @iWarehousingCommodityID, '入库数量小于1'), '测试成功', '测试失败') AS 'Case3 Testing Result';
--  
DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
--  
SELECT '------------------ 入库单商品是服务商品不是单品 --------------------' AS 'CASE4';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'顺丰','快递','个',4,NULL,4,4,'SF',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'顺丰快递',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (0, 3, 1, 1, now(), 1, now());
SET @iWarehousingID = Last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iWarehousingID, @iCommodityID, 1, 1, '顺丰', 56, 11.1, 11.1, now(), 36, now(), now(), now());
SET @iWarehousingCommodityID = Last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
--  
CALL SPD_WarehousingCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
--  
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = CONCAT('入库单商品', @iWarehousingCommodityID, '不是单品'), '测试成功', '测试失败') AS 'Case4 Testing Result';
--  
DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 