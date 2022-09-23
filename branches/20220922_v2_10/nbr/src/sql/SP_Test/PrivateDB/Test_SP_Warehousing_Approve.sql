SELECT '++++++++++++++++++ Test_SP_Warehousing_Approve.sql ++++++++++++++++++++';

SELECT '-------------------case1: 正常审核-------------------------' AS 'Case1';

INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '可比xxx', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, -800, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO t_warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2, 0, 3, 1, 1, now(), 1, now());
SET @iWarehousingID = Last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime, F_NOSalable)
VALUES (@iWarehousingID,@iCommodityID, 20, 1, '可比xxx', 1, 3, 10, now(), 36, now(), now(), now(), 10);
SET @iWarehousingcommodityID = Last_insert_id();


SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 3;

CALL SP_Warehousing_Approve(@iErrorCode, @sErrorMsg, @iWarehousingID, @iApproverID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_ID = @iWarehousingID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iApproverID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue = ((SELECT F_OldValue FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1) + 20)
AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingcommodityID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------case2: 审核ID不存在-------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 999;
SET @iApproverID = 1;

CALL SP_Warehousing_Approve(@iErrorCode, @sErrorMsg, @iID, @iApproverID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '审核的入库单id不存在' , '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------case3: 审核ID已审核-------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 4;
SET @iApproverID = 1;

CALL SP_Warehousing_Approve(@iErrorCode, @sErrorMsg, @iID, @iApproverID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '审核的入库单已审核', '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------case4: 审核没有入库商品的入库单-------------------------' AS 'Case4';
INSERT INTO t_warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2, 0, 3, 1, 1, now(), 1, now());
SET @iID = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iApproverID = 3;

CALL SP_Warehousing_Approve(@iErrorCode, @sErrorMsg, @iID, @iApproverID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_Warehousing WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '该入库单没有入库商品', '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_warehousing WHERE F_ID = @iID;