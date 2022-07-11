SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckWarehousing.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:商品Type是0,期初数量大于0，期初采购价为-1 -------------------------' AS 'Case2';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type,F_NOStart,F_PurchasingPriceStart)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,0,20,-1);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID, '的期初数量和期初采购价不正确，期初数量和期初采购价应该同时为-1或者同时为大于0的数') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case3:商品Type是2,存在入库单 -------------------------' AS 'Case3';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type,F_NOStart,F_PurchasingPriceStart)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,2,20,20);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO T_Warehousing (F_Status,F_ProviderID,F_WarehouseID,F_StaffID,F_CreateDatetime,F_PurchasingOrderID) VALUES (1, 1, 1, 3, now(), NULL);
SET @iWarehousingID = last_insert_id();
INSERT INTO T_WarehousingCommodity (F_WarehousingID,F_CommodityID,F_NO,F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price,F_Amount,F_ProductionDatetime,F_ShelfLife,F_ExpireDatetime,F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 20, 1, '星巴克AB123', 1, 20, 20 * 20, now(), 12, now(), 20);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID, '不是单品，不能入库') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM T_WarehousingCommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM T_Warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case4:商品Type是0,是期初商品，期初数量和对应入库单的可售数量不相等 -------------------------' AS 'Case4';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type,F_NOStart,F_PurchasingPriceStart)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,0,25,20);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO T_Warehousing (F_Status,F_ProviderID,F_WarehouseID,F_StaffID,F_CreateDatetime,F_PurchasingOrderID) VALUES (1, 1, 1, 3, now(), NULL);
SET @iWarehousingID = last_insert_id();
INSERT INTO T_WarehousingCommodity (F_WarehousingID,F_CommodityID,F_NO,F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price,F_Amount,F_ProductionDatetime,F_ShelfLife,F_ExpireDatetime,F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 20, 1, '星巴克AB123', 1, 20, 20 * 20, now(), 12, now(), 20);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID, '的期初数量和对应入库单的可售数量不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM T_WarehousingCommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM T_Warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case5:商品Type是0,是期初商品，期初采购价和对应入库单的进货价不相等 -------------------------' AS 'Case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type,F_NOStart,F_PurchasingPriceStart)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,0,20,25.1);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO T_Warehousing (F_Status,F_ProviderID,F_WarehouseID,F_StaffID,F_CreateDatetime,F_PurchasingOrderID) VALUES (1, 1, 1, 3, now(), NULL);
SET @iWarehousingID = last_insert_id();
INSERT INTO T_WarehousingCommodity (F_WarehousingID,F_CommodityID,F_NO,F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price,F_Amount,F_ProductionDatetime,F_ShelfLife,F_ExpireDatetime,F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 20, 1, '星巴克AB123', 1, 20, 20 * 20, now(), 12, now(), 20);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID, '的期初采购价和对应入库单的进货价不相等') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
DELETE FROM T_WarehousingCommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM T_Warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case6:商品Type是3,存在入库单 -------------------------' AS 'Case6';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'圆通','快递','个',4,NULL,4,4,'YT',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'圆通快递',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO T_Warehousing (F_Status,F_ProviderID,F_WarehouseID,F_StaffID,F_CreateDatetime,F_PurchasingOrderID) VALUES (1, 1, 1, 3, now(), NULL);
SET @iWarehousingID = last_insert_id();
INSERT INTO T_WarehousingCommodity (F_WarehousingID,F_CommodityID,F_NO,F_PackageUnitID,F_CommodityName,F_BarcodeID,F_Price,F_Amount,F_ProductionDatetime,F_ShelfLife,F_ExpireDatetime,F_NOSalable)
VALUES (@iWarehousingID, @iCommodityID, 20, 1, '星巴克AB123', 1, 20, 20 * 20, now(), 0, now(), 20);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckWarehousing(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID, '不是单品，不能入库') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
DELETE FROM T_WarehousingCommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM T_Warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
