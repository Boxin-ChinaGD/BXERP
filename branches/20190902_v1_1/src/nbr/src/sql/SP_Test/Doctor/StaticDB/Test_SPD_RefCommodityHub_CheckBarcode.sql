SELECT '++++++++++++++++++ Test_SPD_RefCommodityHub_CheckBarcode.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常测试 -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RefCommodityHub_CheckBarcode(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:条形码长度小于7位 -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
INSERT INTO staticdb.t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType,  F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale,  F_ShelfLife, F_ReturnDays)
VALUES ('123456','参考商品1', '薯片', '克', '瓶', '箱', '百事可乐',' 默认类别', 'SP', 1,  0, 8, 8, 12, 11.8, 11, 3, 30);
SET @iID = LAST_INSERT_ID();
-- 
CALL SPD_RefCommodityHub_CheckBarcode(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的参考商品的条形码长度必须是[7~20]') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM staticdb.t_refcommodityhub WHERE F_ID = @iID;


-- 由于字段F_Barcode的类型是VarChar(20)的，插入的数据不可能大于20位的，所以注释掉该测试用例
-- SELECT '-------------------- Case3:条形码长度大于20位 -------------------------' AS 'Case3';
-- 
-- SET @iErrorCode = 0;
-- SET @sErrorMsg = '';
-- 
-- INSERT INTO nbr_bx.t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType,  F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale,  F_ShelfLife, F_ReturnDays)
-- VALUES ('123456123456123456123456','参考商品1', '薯片', '克', '瓶', '箱', '百事可乐',' 默认类别', 'SP', 1,  0, 8, 8, 12, 11.8, 11, 3, 30);
-- SET @iID = LAST_INSERT_ID();
-- 
-- CALL SPD_RefCommodityHub_CheckBarcode(@iErrorCode, @sErrorMsg);
-- SELECT @iErrorCode, @sErrorMsg;
-- 
-- SELECT IF(@sErrorMsg = CONCAT('ID为', @iID ,'的参考商品的条形码长度必须是[7~20]') AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
-- DELETE FROM nbr_bx.t_refcommodityhub WHERE F_ID = @iID;