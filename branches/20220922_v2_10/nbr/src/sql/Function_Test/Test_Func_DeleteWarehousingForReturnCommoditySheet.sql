SELECT '++++++++++++++++++++++++++++++++++Test_Func_DeleteWarehousingForReturnCommoditySheet.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1：进行退货操作(入库单已审核，减少入库单可售数量) -------------------------' AS 'Case1';

INSERT INTO t_commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iCommodityID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();


INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime)
VALUES (1, 1, 1, 1, 1, NOW());
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @iCommodityID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, @iCommodityID, 7, 5, '箱');

SET @currentWarehousingID = @warehousingID;
SET @returnNO = 5;
SET @iShopID = 2;

SELECT Func_DeleteWarehousingForReturnCommoditySheet(@iCommodityID, @returnNO, @currentWarehousingID, @iShopID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @iCommodityID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 10; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NO = 15; -- 入库商品表入库数量不会变化
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;

SELECT '-------------------- Case2：进行退货操作，退货数量大于可售数量，可售数量变为0，循环去更新下一张入库单 -------------------------' AS 'Case2';

INSERT INTO t_commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @iCommodityID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
SET @iShopID = 2;

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (1, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @iCommodityID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (1, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID2 = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID2, @iCommodityID, 20, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 20);
SET @warehousingCommodityID2 = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, @iCommodityID, 7, 30, '箱');

SET @currentWarehousingID = @warehousingID1;
SET @returnNO = 30;


SELECT Func_DeleteWarehousingForReturnCommoditySheet(@iCommodityID, @returnNO, @currentWarehousingID, @iShopID) INTO @is;
SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID2 AND F_CommodityID = @iCommodityID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT * FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 0; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT * FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2 AND F_NOSalable = 5; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NO = 15; -- 入库商品表入库数量不会变化
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';
 
SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2 AND F_NO = 20; -- 入库商品表入库数量不会变化
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID2;

SELECT '-------------------- Case3：进行退货操作，退货数量大于可售数量，可售数量溢出，最早入库的单可售数量为负数 -------------------------' AS 'Case3';

INSERT INTO t_commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @iCommodityID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
SET @iShopID = 2;

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (1, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @iCommodityID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, @iCommodityID, 7, 30, '箱');

SET @currentWarehousingID = @warehousingID;
SET @returnNO = 30;

SELECT Func_DeleteWarehousingForReturnCommoditySheet(@iCommodityID, @returnNO, @currentWarehousingID, @iShopID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @iCommodityID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = -15; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NO = 15; -- 入库商品表入库数量不会变化
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;

SELECT '-------------------- Case4：进行退货操作(入库单未审核，不减少入库单可售数量) -------------------------' AS 'Case4';

INSERT INTO t_commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iCommodityID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
SET @iShopID = 2;

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (0, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @iCommodityID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, @iCommodityID, 7, 5, '箱');

SET @currentWarehousingID = @warehousingID;
SET @returnNO = 5;

SELECT Func_DeleteWarehousingForReturnCommoditySheet(@iCommodityID, @returnNO, @currentWarehousingID, @iShopID) INTO @is;

SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @iCommodityID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = 15; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case4 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NO = 15; -- 入库商品表入库数量不会变化
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;

SELECT '------------ Case5：一张已经审核的，一张未审核的，进行退货操作，退货数量大于第一张可售数量，可售数量变为负数，未审核入库单不变 ---------------------' AS 'Case5';

INSERT INTO t_commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'公仔面333','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @iCommodityID = LAST_INSERT_ID();
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
SET @iShopID = 2;

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (1, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID, @iCommodityID, 15, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 15);
SET @warehousingCommodityID = LAST_INSERT_ID();

INSERT INTO t_warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_ApproverID, F_CreateDatetime, F_ShopID)
VALUES (0, 1, 1, 1, 1, NOW(), @iShopID);
SET @warehousingID2 = LAST_INSERT_ID();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime, F_CreateDatetime, F_NOSalable)
VALUES (@warehousingID2, @iCommodityID, 20, 1, '公仔面333', 1, 10, 150, now(), 12, now(), NOW(), 20);
SET @warehousingCommodityID2 = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iReturnCommoditySheetID = last_insert_id();

INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, @iCommodityID, 7, 30, '箱');

SET @currentWarehousingID = @warehousingID;
SET @returnNO = 30;

SELECT Func_DeleteWarehousingForReturnCommoditySheet(@iCommodityID, @returnNO, @currentWarehousingID, @iShopID) INTO @is;
SELECT 1 FROM t_commodityshopinfo WHERE F_CurrentWarehousingID = @warehousingID AND F_CommodityID = @iCommodityID; -- 检查商品表当值入库ID是否更新
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NOSalable = -15; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2 AND F_NOSalable = 20; -- 检查入库商品表可售数量是否正确计算
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID AND F_NO = 15; -- 入库商品表入库数量不会变化
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';
 
SELECT 1 FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2 AND F_NO = 20; -- 入库商品表入库数量不会变化
SELECT IF(FOUND_ROWS() = 1, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iReturnCommoditySheetID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID;
DELETE FROM t_warehousingcommodity WHERE F_ID = @warehousingCommodityID2;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID;
DELETE FROM t_warehousing WHERE F_ID = @warehousingID2;