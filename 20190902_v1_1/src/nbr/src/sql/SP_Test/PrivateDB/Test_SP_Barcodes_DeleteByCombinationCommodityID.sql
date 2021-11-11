SELECT '++++++++++++++++++ Test_SP_Barcodes_DeleteByCombinationCommodityID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:µ•∆∑ -------------------------' AS 'Case1';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'∞¢∂˚±∞Àπ∞Ù∞ÙÃ«142',' Ì∆¨','øÀ',1,'œ‰',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_DeleteByCombinationCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7,'≤‚ ‘≥…π¶','≤‚ ‘ ß∞‹') AS 'Case1 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case2:∂‡∞¸◊∞ -------------------------' AS 'Case2';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'∞¢∂˚±∞Àπ∞Ù∞ÙÃ«3asdsadzxczxczx∂‡l',' Ì∆¨','øÀ',1,'œ‰',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC3';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_DeleteByCombinationCommodityID(@iErrorCode, @sErrorMsg, @imID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7,'≤‚ ‘≥…π¶','≤‚ ‘ ß∞‹') AS 'Testing Case3 Result';
-- 
DELETE FROM t_barcodes WHERE F_CommodityID=@imID;
DELETE FROM t_commodity WHERE F_ID = @imID;

SELECT '-------------------- Case3:◊È∫œ…Ã∆∑ -------------------------' AS 'Case3';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'∞¢∂˚±∞Àπ∞Ù∞ÙÃ«asdsaxcxzzd4',' Ì∆¨','øÀ',1,'œ‰',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',1);
SET @iID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode1 = 'AABBCC4A';
SET @sBarcode2 = 'AABBCC4B';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode2);
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_DeleteByCombinationCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0,'≤‚ ‘≥…π¶','≤‚ ‘ ß∞‹') AS 'Case3 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode1, '%')
AND F_OldValue LIKE CONCAT('%', @sBarcode2, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '≤‚ ‘≥…π¶', '≤‚ ‘ ß∞‹') AS 'CASE3 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID=@iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case4:◊È∫œ…Ã∆∑ -------------------------' AS 'Case3';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'∞¢∂˚±∞Àπ∞Ù∞ÙÃ«asdsaxcxzzd4',' Ì∆¨','øÀ',1,'œ‰',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',1);
SET @iID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode1 = 'AABBCC4A';
SET @sBarcode2 = 'AABBCC4B';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
SET @ibID = last_insert_id();
SET @iStaffID = 3;
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (1, @iID,'Ú˘Úœ¿ÕÊæﬂ1∫≈', @ibID, 200, 111, 100, 100, 100, null);
SET @rtcID = last_insert_id();
-- 
CALL SP_Barcodes_DeleteByCombinationCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0,'≤‚ ‘≥…π¶','≤‚ ‘ ß∞‹') AS 'Case3 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtcID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;