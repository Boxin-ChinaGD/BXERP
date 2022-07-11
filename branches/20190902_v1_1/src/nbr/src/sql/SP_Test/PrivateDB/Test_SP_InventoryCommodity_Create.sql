SELECT '++++++++++++++++++ Test_SP_InventoryCommodity_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 添加不重复的商品到盘点单中，错误码为0 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInventorySheetID = 5;
SET @iCommodityID = 1;
SET @iNOReal = 20;
SET @iBarcodeID = 1;

CALL SP_InventoryCommodity_Create(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID, @iNOReal, @iBarcodeID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = @iInventorySheetID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2: 添加相同的商品到同一个盘点单中，错误码为1 -------------------------' AS 'Case2';
INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,5,'2017-08-06','...........................');
SET @iID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem, F_CreateDatetime)
VALUES (@iID, 1, '薯片', '克', 1, 1, -1, -1, now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInventorySheetID = @iID;
SET @iCommodityID = 1;
SET @iBarcodeID = 1;

CALL SP_InventoryCommodity_Create(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID, @iNOReal, @iBarcodeID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = @iInventorySheetID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = last_insert_id();
DELETE FROM t_inventorysheet WHERE F_ID = @iID;

SELECT '-------------------- Case3: 添加组合商品到盘点单中，错误码为7 -------------------------' AS 'Case3';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '薯愿香辣味薯片6667', '薯片', '克', 1, '箱', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 
20, 0, 0, '1111111', 1);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInventorySheetID = 1;
SET @iCommodityID = last_insert_id();
SET @iBarcodeID = 1;

CALL SP_InventoryCommodity_Create(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID, @iNOReal, @iBarcodeID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE3 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = last_insert_id();
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case4: 添加多包装商品到盘点单中，错误码为7 -------------------------' AS 'Case4';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale,  F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '薯愿香辣味6675', '薯片', '克', 1, '箱', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 1, 2, '1111111', 2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInventorySheetID = 1;
SET @iCommodityID = last_insert_id();
SET @iBarcodeID = 1;

CALL SP_InventoryCommodity_Create(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID, @iNOReal, @iBarcodeID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE4 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = last_insert_id();
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case5: 添加服务商品到盘点单中，错误码为7 -------------------------' AS 'Case5';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale,  F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '薯愿香辣味6675', '薯片', '克', 1, '', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
0, 30, '2018-04-14', 20, 0, 0, '1111111', 3);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInventorySheetID = 1;
SET @iCommodityID = last_insert_id();
SET @iBarcodeID = 1;

CALL SP_InventoryCommodity_Create(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID, @iNOReal, @iBarcodeID);

SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '只能创建普通商品的盘点单', '测试成功', '测试失败') AS 'CASE5 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = last_insert_id();
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;