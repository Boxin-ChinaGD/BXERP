SELECT '++++++++++++++++++ Test_SP_PackageUnit_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 该包装单位已被采购订单商品使用，不能删除，错误代码为7 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '盒123';
INSERT INTO t_packageunit (F_Name)
VALUES (@sName);

SET @iPackageUnitID = LAST_INSERT_ID();

-- 创建一个商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'小公仔面','方便面','克',@iPackageUnitID,@sName,3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iCommodityID = last_insert_id();
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID = last_insert_ID();
-- 创建一个采购订单
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (0, 1, 1, '1', 1, '1', NOW(), NOW(), NOW(), NOW());
SET @iPurchasingOrderID = last_insert_id();
SET @iCommodityNO = 0;
SET @fPriceSuggestion = 1.0;
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);
SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '创建成功', '测试失败') AS '创建采购订单';

CALL SP_PackageUnit_Delete(@iErrorCode, @sErrorMsg, @iPackageUnitID);
SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iPackageUnitID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_packageunit WHERE F_ID = @iPackageUnitID;

SELECT '-------------------- Case2: 该包装单位没有商品在使用，可以直接删除，错误代码为0 -------------------------' AS 'Case2';

SET @sName = '盒12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

INSERT INTO t_packageunit (F_Name)
VALUES (@sName);

SET @iID = LAST_INSERT_ID();

CALL SP_PackageUnit_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: 该包装单位已被入库单商品使用，不能删除，错误代码为7 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '盒123';
INSERT INTO t_packageunit (F_Name)
VALUES (@sName);

SET @iPackageUnitID = LAST_INSERT_ID();

-- 创建一个商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'小公仔面','方便面','克',@iPackageUnitID,@sName,3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iCommodityID = last_insert_id();
-- 创建一个入库单
SET @iWarehousingID = 1;
SET @iNO = 1;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SET @iWarehousingCommodityID = last_insert_id();

CALL SP_PackageUnit_Delete(@iErrorCode, @sErrorMsg, @iPackageUnitID);
SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iPackageUnitID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_packageunit WHERE F_ID = @iPackageUnitID;

SELECT '-------------------- Case4: 该包装单位已被盘点单商品使用，不能删除，错误代码为7 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '盒123';
INSERT INTO t_packageunit (F_Name)
VALUES (@sName);

SET @iPackageUnitID = LAST_INSERT_ID();

-- 创建一个商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'小公仔面','方便面','克',@iPackageUnitID,@sName,3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iCommodityID = last_insert_id();
-- 创建一个盘点单
SET @iInventorySheetID = 5;
SET @iNOReal = 0;
SET @iBarcodeID = 1;

CALL SP_InventoryCommodity_Create(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID, @iNOReal, @iBarcodeID);
SET @iInventoryCommodityID = last_insert_id();

SELECT @iErrorCode;
SELECT @sErrorMsg;

CALL SP_PackageUnit_Delete(@iErrorCode, @sErrorMsg, @iPackageUnitID);
SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iPackageUnitID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
DELETE FROM t_inventorycommodity WHERE F_ID = @iInventoryCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_packageunit WHERE F_ID = @iPackageUnitID;

SELECT '-------------------- Case5: 该包装单位已被商品使用，不能删除，错误代码为7 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '盒123';
INSERT INTO t_packageunit (F_Name)
VALUES (@sName);

SET @iPackageUnitID = LAST_INSERT_ID();

-- 创建一个商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'小公仔面','方便面','克',@iPackageUnitID,@sName,3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iCommodityID = last_insert_id();

CALL SP_PackageUnit_Delete(@iErrorCode, @sErrorMsg, @iPackageUnitID);
SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_ID = @iPackageUnitID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_packageunit WHERE F_ID = @iPackageUnitID;