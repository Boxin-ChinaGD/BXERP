SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckPurchasingOrder.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询的商品有采购单位却没有对应的采购订单 -------------------------' AS 'Case1';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',-1,-0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID ,'需要有对应的采购订单') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case2:查询的所有商品都存在供应商 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);

SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:查询的商品没有采购单位却有对应的采购订单 -------------------------' AS 'Case3';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4, NULL,4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',-1,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (1, @iCommodityID, 200, '可比克薯片', 1, 1, 11.1);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID ,'不能被采购订单引用') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '+++++++++++++++++++++++ case4:组合商品有采购单位 +++++++++++++++++++++++++++++++++++++' AS 'case4';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4, '包',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',-1,1);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('组合商品', @iCommodityID , '不能有采购单位') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '+++++++++++++++++++++++ case5:多包装商品有采购单位 +++++++++++++++++++++++++++++++++++++' AS 'case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4, '包',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',-1,2);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('多包装商品', @iCommodityID , '不能有采购单位') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '+++++++++++++++++++++++ case6:服务商品有采购单位 +++++++++++++++++++++++++++++++++++++' AS 'case6';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'顺丰','快递','个',4, '包',4,4,'SF',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'顺丰快递',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('服务商品', @iCommodityID , '不能有采购单位') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;