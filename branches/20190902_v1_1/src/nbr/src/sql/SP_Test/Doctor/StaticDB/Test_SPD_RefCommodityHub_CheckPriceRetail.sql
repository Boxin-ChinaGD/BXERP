SELECT '++++++++++++++++++ Test_SPD_RefCommodityHub_CheckPriceRetail.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RefCommodityHub_CheckPriceRetail(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:参考商品的零售价为0 -------------------------' AS 'Case2';
-- 
INSERT INTO staticdb.t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType,  F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale,  F_ShelfLife, F_ReturnDays)
VALUES ('3548293894789','可比克薯片1', '薯片', '克', '瓶', '箱', '百事可乐',' 默认类别', 'SP', 1,  0, 8, 8, 0, 11.8, 11, 3, 30);
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RefCommodityHub_CheckPriceRetail(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的参考商品的零售价不能为空也不能为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM staticdb.t_refcommodityhub WHERE F_ID = @iID;


-- 插入参考商品数据时，字段F_PriceRetail不能为null，所以注释掉该测试用例
-- SELECT '-------------------- Case3:参考商品的规格为null -------------------------' AS 'Case3';
-- INSERT INTO staticdb.t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType,  F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale,  F_ShelfLife, F_ReturnDays)
-- VALUES ('3548293894789','可比克薯片1', '薯片', '克', '瓶', '箱', '百事可乐',' 默认类别', 'SP', 1,  0, 8, 8, NULL, 11.8, 11, 3, 30);
-- SET @iID = LAST_INSERT_ID();
-- 
-- SET @iErrorCode = 0;
-- SET @sErrorMsg = '';
-- 
-- CALL SPD_RefCommodityHub_CheckPriceRetail(@iErrorCode, @sErrorMsg);
-- SELECT @iErrorCode, @sErrorMsg;
-- 
-- SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的参考商品的零售价不能为空也不能为0') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
-- DELETE FROM staticdb.t_refcommodityhub WHERE F_ID = @iID;