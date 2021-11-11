SELECT '-------------------- Case1:删除多包装商品 -------------------------' AS 'Case1';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',2);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 7,'测试成功','测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;
SELECT '-------------------- Case2:删除组合商品 -------------------------' AS 'Case2';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',1);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 7,'测试成功','测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;
SELECT '-------------------- Case3:删除普通商品 -------------------------' AS 'Case3';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 7,'测试成功','测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;
SELECT '-------------------- Case4:删除服务商品 -------------------------' AS 'Case4';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',3);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 0,'测试成功','测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
SELECT '-------------------- Case5:重复删除服务商品 -------------------------' AS 'Case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',3);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);

SELECT IF(@iErrorCode = 0,'测试成功','测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 7,'测试成功','测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case6:删除预删除的服务商品 -------------------------' AS 'Case6';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'蜘蛛侠1号aaa','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',3);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);

SELECT IF(@iErrorCode = 0,'测试成功','测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;

SELECT '-------------------- Case7:删除不存在的商品 -------------------------' AS 'Case7';
SET @iID = -1;
SET @iStaffID = 3;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 2,'测试成功','测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case8:零售商品有记录 删除商品及条码失败 -------------------------' AS 'Case8';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼23号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',3);
SET @iID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf13a');
SET @ibID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (1, @iID,'奥特曼23号', @ibID, 100, 120, 100, 5, 100, NULL);
SET @irtcID = last_insert_id();
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case8 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE8 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @irtcID;
DELETE FROM t_providercommodity WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_providercommodity WHERE F_CommodityID = @ibID;
DELETE FROM t_barcodes WHERE F_CommodityID = @ibID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;
SELECT '-------------------- Case9:日报表有记录 删除商品及条码失败 -------------------------' AS 'Case9';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼23号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',3);
SET @iID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf13a');
SET @ibID = last_insert_id();
-- 
INSERT INTO T_RetailTradeDailyReportByCommodity (F_Datetime, F_CommodityID, F_NO, F_TotalPurchasingAmount, F_TotalAmount, F_GrossMargin, F_CreateDatetime, F_UpdateDatetime, F_ShopID)
VALUES (now(), @iID, 40, 400, 600, 200, now(), now(), 2);
SET @retailTradeDailyReportByCommodityID = last_insert_id();
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case9 Testing Result';
-- 
DELETE FROM T_RetailTradeDailyReportByCommodity WHERE F_ID = @retailTradeDailyReportByCommodityID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;