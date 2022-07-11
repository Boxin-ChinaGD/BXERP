SELECT '++++++++++++++++++ Test_SP_Commodity_CheckDependency.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:商品库存有改动 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 20;

CALL SP_Commodity_CheckDependency(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:商品在RetailtradeCommodity有依赖 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 创建一个商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'小公仔面','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @iID = last_insert_id();
-- 创建零售单
INSERT INTO t_retailtrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Remark,F_SourceID,F_Status,F_SyncDatetime,F_Amount,F_SmallSheetID, F_ShopID)
VALUES (now(),123456,3,'url=ashasoadigmnalskd','2017-08-06',1,1,0,'........',-1,1,now(),128.6,1, 2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = LAST_INSERT_ID();
SET @iCommodityID = @iID;
SET @iBarcodeID = 1;
SET @iNO = 0;
SET @fPrice = 222.6;

SET @iNOCanReturn = 0;
SET @iStaffID = 1;
SET @fPriceReturn = 0;
SET @fPriceSpecialOffer = 0;
SET @fPriceVIPOriginal = 0;
CALL SP_RetailTradeCommodity_UploadTrade_CreateCommodity(@iErrorCode, @sErrorMsg, @iTradeID, @iCommodityID, @iBarcodeID, @iNO, @fPrice, @iNOCanReturn, @iStaffID, @fPriceReturn, @fPriceSpecialOffer, @fPriceVIPOriginal);
SELECT @iErrorCode;
SELECT @sErrorMsg;

CALL SP_Commodity_CheckDependency(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_retailtradecommoditysource WHERE F_RetailTradeCommodityID = (SELECT F_ID FROM t_retailtradecommodity WHERE F_CommodityID = @iCommodityID AND F_TradeID = @iTradeID);
DELETE FROM t_retailtradecommodity WHERE F_TradeID = @iTradeID;
DELETE FROM t_retailtrade WHERE F_ID = @iTradeID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case3:商品未被使用 -------------------------' AS 'Case3';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'小公仔面','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',2);
SET @iID = last_insert_id();
SET @sErrorMsg = '';

CALL SP_Commodity_CheckDependency(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case4:商品在PurchasingOrderCommodity有依赖 -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 创建一个商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'小公仔面','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iID = last_insert_id();
-- 创建一个采购订单
INSERT INTO t_purchasingorder (F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (0, 1, 1, '1', 1, '1', NOW(), NOW(), NOW(), NOW());
SET @iPurchasingOrderID = last_insert_id();
SET @iCommodityID = @iID;

INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID = last_insert_ID();

SET @iCommodityNO = 0;

SET @fPriceSuggestion = 1.0;
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);
SELECT @iErrorCode;
SELECT @sErrorMsg;

CALL SP_Commodity_CheckDependency(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case5:商品在InventoryCommodity有依赖 -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 创建一个商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'小公仔面','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iID = last_insert_id();
-- 创建一个盘点单
SET @iInventorySheetID = 5;
SET @iCommodityID = @iID;
SET @iNOReal = 0;
SET @iBarcodeID = 1;

CALL SP_InventoryCommodity_Create(@iErrorCode, @sErrorMsg, @iInventorySheetID, @iCommodityID, @iNOReal, @iBarcodeID);
SET @iInventoryCommodityID = last_insert_id();

SELECT @iErrorCode;
SELECT @sErrorMsg;

CALL SP_Commodity_CheckDependency(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @iInventoryCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case6:商品在WarehousingCommodity有依赖 -------------------------' AS 'Case6';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 创建一个商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'小公仔面','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',56,3,'1111111',0);
SET @iID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iID, '123456789');
SET @iBarcodeID = last_insert_ID();
-- 创建一个入库单
SET @iWarehousingID = 1;
SET @iCommodityID = @iID;
SET @iNO = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SET @iWarehousingCommodityID = last_insert_id();

SELECT @iErrorCode;
SELECT @sErrorMsg;

CALL SP_Commodity_CheckDependency(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @iWarehousingCommodityID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM t_commodity WHERE F_ID = @iID;