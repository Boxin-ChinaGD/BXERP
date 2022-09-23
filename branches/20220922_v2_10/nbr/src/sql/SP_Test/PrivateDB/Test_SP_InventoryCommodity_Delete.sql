SELECT '++++++++++++++++++ Test_SP_InventoryCommodity_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 当iCommodityID > 0 时 删除对应的商品 -------------------------' AS 'Case1';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'卫龙2','辣条','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @iCommodityID = last_insert_id();
SET @sErrorMsg = '';

INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,300,0,1,now(),'a...........................');
SET @iInventorySheetID = last_insert_id();

INSERT INTO t_inventorycommodity ( F_InventorySheetID, F_CommodityID, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES ( @iInventorySheetID, @iCommodityID, 1, 1, 0, 0);

CALL SP_InventoryCommodity_Delete(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID);

SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = @iInventorySheetID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_InventorySheetID = @iInventorySheetID;
DELETE FROM t_inventorysheet WHERE F_ID = @iInventorySheetID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;	

SELECT '-------------------- Case2: 当iCommodityID < 0 时 删除该盘点单所有商品 -------------------------' AS 'Case2';
INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,300,0,1,now(),'a...........................');
-- 
SET @iInventorySheetID = last_insert_id();
SET @iCommodityID = -1;
SET @sErrorMsg = '';

CALL SP_InventoryCommodity_Delete(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = @iInventorySheetID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_inventorysheet WHERE F_ID = @iInventorySheetID;