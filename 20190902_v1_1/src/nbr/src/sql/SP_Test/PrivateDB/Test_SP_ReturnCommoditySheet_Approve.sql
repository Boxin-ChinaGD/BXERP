SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheet_Approve.sql ++++++++++++++++++++';

SELECT '-------------------- CASE1:该退货单未审核(单品) -------------------------' AS 'Case1';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比xxx', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID,@iCommodityID,'可比xxx', 7, 100, '箱', 5.0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ReturnCommoditySheet
WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 验证是否保留历史原貌
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = @iCommodityID AND F_CommodityName = '可比xxx';
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- CASE2:该退货单未审核(多包装商品) -------------------------' AS 'Case2';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比xxx', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID, @iCommodityID,'可比xxx', 55, 10, '箱', 8.0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ReturnCommoditySheet
WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 验证是否保留历史原貌
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = @iCommodityID AND F_CommodityName = '可比xxx';
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- CASE3:该退货单未审核,此退货单有两个商品表（单品和多包装商品） -------------------------' AS 'Case3';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比xxx1', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID1 = last_insert_id();
-- 
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID1, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比xxx2', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID2 = last_insert_id();
-- 
INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID,@iCommodityID1,'可比xxx1', 7, 100, '箱', 5.0);
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID, @iCommodityID2,'可比xxx2', 55, 10, '箱', 8.0);

CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ReturnCommoditySheet
WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 验证是否保留历史原貌
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = @iCommodityID1 AND F_CommodityName = '可比xxx1';
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';
-- 
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = @iCommodityID2 AND F_CommodityName = '可比xxx2';
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;
DELETE FROM t_commodityhistory WHERE F_CommodityID IN (@iCommodityID1,@iCommodityID2);
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID IN (@iCommodityID1,@iCommodityID2);

SELECT '-------------------- CASE4:该退货单已审核 -------------------------' AS 'Case4';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比xxx', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID,F_Status, F_ShopID)
VALUES (4,5,1,2);
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID,@iCommodityID,'可比xxx1', 7, 100, '箱', 5.0);
SET @sErrorMsg = '';

CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '该退货单已审核，请勿重复操作', '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- CASE5:该退货单不存在 -------------------------' AS 'Case5';
SET @sErrorMsg = '';
CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SET @iID = -5;

SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTradePromoting WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '查无该采购退货单', '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;

SELECT '-------------------- CASE6:审核没有退货商品的退货单 -------------------------' AS 'Case6';

INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID,F_Status,F_CreateDatetime,F_UpdateDatetime, F_ShopID)
VALUES (5,5,0,now(),now(),2);
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '该退货单没有退货商品', '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;