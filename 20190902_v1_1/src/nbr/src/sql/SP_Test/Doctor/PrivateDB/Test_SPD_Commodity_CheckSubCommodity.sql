SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckSubCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询所有的组合商品有一个商品不存在子商品 -------------------------' AS 'Case1';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,1);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckSubCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('组合商品', @iCommodityID ,'没有对应的子商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case2:查询的所有组合商品都存在子商品 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SPD_Commodity_CheckSubCommodity(@iErrorCode, @sErrorMsg);

SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';



-- CASE3:查询的所有组合商品有一个组合商品的子商品是组合商品
SELECT '-------------------- CASE3:查询的所有组合商品有一个组合商品的子商品是组合商品 -------------------------' AS 'Case3';
-- 创建主商品A
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,1);
SET @iCommodityID1 = LAST_INSERT_ID();
-- 创建子商品B
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB12','咖啡12','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,1);
SET @iCommodityID2 = LAST_INSERT_ID();
-- 创建主商品A的子商品
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID1, @iCommodityID2, 10, 11.0, now(), now());
SET @iSubCommodityID2 = LAST_INSERT_ID();
-- 创建商品B的子商品
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID2, 1, 10, 11.0, now(), now());
SET @iSubCommodityID1 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckSubCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('组合商品', @iCommodityID1 ,'对应的子商品不是普通商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_subcommodity WHERE F_ID = @iSubCommodityID1;
DELETE FROM t_subcommodity WHERE F_ID = @iSubCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID1;

-- Case4:查询的所有组合商品有一个组合商品的子商品是多包装商品
SELECT '-------------------- Case4:查询的所有组合商品有一个组合商品的子商品是多包装商品 -------------------------' AS 'Case4';
-- 创建普通商品A
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,1);
SET @iCommodityID1 = LAST_INSERT_ID();
-- 创建多包装商品B
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB12','咖啡12','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',@iCommodityID1,3,'星巴克AB',0,2);
SET @iCommodityID2 = LAST_INSERT_ID();
-- 创建主商品A的子商品
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID1, @iCommodityID2, 10, 11.0, now(), now());
SET @iSubCommodityID1 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckSubCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('组合商品', @iCommodityID1 ,'对应的子商品不是普通商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_subcommodity WHERE F_ID = @iSubCommodityID1;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID1;

-- Case5:查询的所有组合商品有一个组合商品的子商品是服务商品
SELECT '-------------------- Case5:查询的所有组合商品有一个组合商品的子商品是服务商品 -------------------------' AS 'Case5';
-- 创建普通商品A
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,1);
SET @iCommodityID1 = LAST_INSERT_ID();
-- 创建服务商品B
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB12','咖啡12','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,3);
SET @iCommodityID2 = LAST_INSERT_ID();
-- 创建主商品A的子商品
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID1, @iCommodityID2, 10, 11.0, now(), now());
SET @iSubCommodityID1 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckSubCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('组合商品', @iCommodityID1 ,'对应的子商品不是普通商品') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_subcommodity WHERE F_ID = @iSubCommodityID1;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID1;

