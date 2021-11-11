SELECT '++++++++++++++++++ Test_SP_Commodity_UpdateWarehousing.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 非多包装商品更新 -------------------------' AS 'Case1';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比xxx', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111',0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iNO = 200;
SET @iPrice = 5.0;
SET @iStaffID = 3;
SET @iShopID = 2;

CALL SP_Commodity_UpdateWarehousing(@iErrorCode, @sErrorMsg, @iCommodityID, @iNO, @iPrice, @iStaffID, @iShopID);

SELECT @sErrorMsg;
SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = @iCommodityID AND F_LatestPricePurchase = @iPrice AND F_ShopID = @iShopID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue = ((SELECT F_OldValue FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1) + @iNO)
AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;