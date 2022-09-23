INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType,
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '薯片sgdg', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-24 12:05:11', '2019-12-24 12:11:08', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2, 0, 3, 1, 1, now(), 1, now());
-- Case1:根据入库单和商品ID删除
SET @iWarehousingID = Last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iWarehousingID, @iCommodityID, 200, 1, '薯片sgdg', 1, 11.1, 11.1, now(), 36, now(), now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_WarehousingCommodity_Delete(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID);

SELECT @sErrorMsg;
SELECT 1 FROM t_WarehousingCommodity WHERE F_CommodityID = @iCommodityID AND F_WarehousingID = @iWarehousingID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

-- Case2:根据入库单删除该入库单所有商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '薯片sgdg1', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-24 12:05:11', '2019-12-24 12:11:08', NULL, NULL, NULL, NULL);
SET @iCommodityID1 = last_insert_id();
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '薯片sgdg2', '薯片', '克', 1, '千克', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-24 12:05:11', '2019-12-24 12:11:08', NULL, NULL, NULL, NULL);
SET @iCommodityID2 = last_insert_id();
-- 
INSERT INTO t_warehousing (F_ShopID, F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime, F_PurchasingOrderID, F_UpdateDatetime)
VALUES (2, 0, 3, 1, 1, now(), 1, now());
SET @iWarehousingID = last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iWarehousingID, @iCommodityID1, 200, 1, '薯片sgdg1', 1, 11.1, 11.1, now(), 36, now(), now(), now());

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iWarehousingID,@iCommodityID2, 200, 1, '薯片sgdg2', 1, 11.1, 11.1, now(), 36, now(), now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;

CALL SP_WarehousingCommodity_Delete(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID);

SELECT @sErrorMsg;
SELECT 1 FROM t_WarehousingCommodity WHERE F_WarehousingID = @iWarehousingID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_WarehousingID  = @iWarehousingID;
DELETE FROM t_warehousing WHERE F_ID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID IN (@iCommodityID1,@iCommodityID2) ;