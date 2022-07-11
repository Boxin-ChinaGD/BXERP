SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckType.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCategoryID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:商品没有对应的类别 -------------------------' AS 'Case2';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'星巴克AB123','咖啡123','个',4,'支',4,NULL,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCategoryID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('商品', @iCommodityID ,'没有对应的类别ID') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case3:删除商品还存在类别 -------------------------' AS 'Case3';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (2,'星巴克AB123','咖啡123','个',4,'支',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'星巴克AB',0,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCategoryID(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('删除商品', @iCommodityID ,'没有正确更改对应的类别ID，它应该是null') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;