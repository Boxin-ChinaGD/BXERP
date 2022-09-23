SELECT '++++++++++++++++++ Test_SP_CommodityShopInfo_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片aaa', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2021-03-24 10:47:43', '2021-03-24 10:47:43', NULL, NULL, NULL, NULL);


SET @iCommodityID = last_insert_id();
SET @iShopID = 2;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dLatestPricePurchase = 8;
SET @dPriceRetail = 12;
SET @iNOStart = -1;
SET @dPurchasingPriceStart = -1;
SET @iStaffID = 4;
SET @iCurrentWarehousingID = NULL;

-- 
CALL SP_CommodityShopInfo_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @dLatestPricePurchase, @dPriceRetail, @iNOStart, @dPurchasingPriceStart, @iStaffID, @iCurrentWarehousingID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_ShopID = @iShopID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 


SELECT '-------------------- Case2:重复添加 -------------------------' AS 'Case2';
-- 
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片aaa', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2021-03-24 10:47:43', '2021-03-24 10:47:43', NULL, NULL, NULL, NULL);

SET @iCommodityID = last_insert_id();
SET @iShopID = 2;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dLatestPricePurchase = 8;
SET @dPriceRetail = 12;
SET @iNOStart = -1;
SET @dPurchasingPriceStart = -1;
SET @iStaffID = 4;
SET @iCurrentWarehousingID = NULL;
-- 
CALL SP_CommodityShopInfo_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @dLatestPricePurchase, @dPriceRetail, @iNOStart, @dPurchasingPriceStart, @iStaffID, @iCurrentWarehousingID);
-- 
CALL SP_CommodityShopInfo_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @dLatestPricePurchase, @dPriceRetail, @iNOStart, @dPurchasingPriceStart, @iStaffID, @iCurrentWarehousingID);
-- 
SELECT IF(@iErrorCode = 1 AND @sErrorMsg = '该商品门店信息已存在', '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
-- 

SELECT '-------------------- Case3:用不存在的商品创建商品门店信息表 -------------------------' AS 'Case3';
-- 
SET @iCommodityID = -999;
SET @iShopID = 2;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dLatestPricePurchase = 8;
SET @dPriceRetail = 12;
SET @iNOStart = -1;
SET @dPurchasingPriceStart = -1;
SET @iStaffID = 4;
SET @iCurrentWarehousingID = NULL;
-- 
CALL SP_CommodityShopInfo_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @dLatestPricePurchase, @dPriceRetail, @iNOStart, @dPurchasingPriceStart, @iStaffID, @iCurrentWarehousingID);
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '不能使用不存在的商品创建商品门店信息', '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 


SELECT '-------------------- Case4:用不存在的门店创建商品门店信息表 -------------------------' AS 'Case4';
-- 
SET @iCommodityID = 1;
SET @iShopID = -999;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dLatestPricePurchase = 8;
SET @dPriceRetail = 12;
SET @iNOStart = -1;
SET @dPurchasingPriceStart = -1;
SET @iStaffID = 4;
SET @iCurrentWarehousingID = NULL;
-- 
CALL SP_CommodityShopInfo_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @dLatestPricePurchase, @dPriceRetail, @iNOStart, @dPurchasingPriceStart, @iStaffID, @iCurrentWarehousingID);
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '不能使用不存在的门店创建商品门店信息', '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 



SELECT '-------------------- Case5:商品为期初商品，F_NO与F_NOStart相等,F_LatestPricePurchase为传入的值 -------------------------' AS 'Case5';

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比克薯片aaa', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2021-03-24 10:47:43', '2021-03-24 10:47:43', NULL, NULL, NULL, NULL);

SET @iCommodityID = last_insert_id();
SET @iShopID = 2;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dLatestPricePurchase = 8;
SET @dPriceRetail = 12;
SET @iNOStart = 10;
SET @dPurchasingPriceStart = 2;
SET @iStaffID = 4;
SET @iCurrentWarehousingID = NULL;
-- 
CALL SP_CommodityShopInfo_Create(@iErrorCode, @sErrorMsg, @iCommodityID, @iShopID, @dLatestPricePurchase, @dPriceRetail, @iNOStart, @dPurchasingPriceStart, @iStaffID, @iCurrentWarehousingID);
-- 
SELECT @sErrorMsg;
SET @iNO = 0;
SELECT F_NO INTO @iNO FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_ShopID = @iShopID;
-- 
SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_ShopID = @iShopID AND F_NOStart = @iNOStart AND F_LatestPricePurchase = @dPurchasingPriceStart;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @iNO = @iNOStart, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
DELETE FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;