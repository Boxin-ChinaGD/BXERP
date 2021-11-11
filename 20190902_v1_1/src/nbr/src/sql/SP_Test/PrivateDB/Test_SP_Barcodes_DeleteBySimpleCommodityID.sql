SELECT '++++++++++++++++++ Test_SP_Barcodes_DeleteBySimpleCommodityID.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:单品无多包装 1条码 删除成功 -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖1','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC1';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case1 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case2:单品无多包装 2条码 删除成功 -------------------------' AS 'Case2';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖2','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode1 = 'AABBCC2A';
SET @sBarcode2 = 'AABBCC2B';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode2);
-- 
CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode1, '%')
AND F_OldValue LIKE CONCAT('%', @sBarcode2, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE2 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case3:多包装 1条码 删除失败 -------------------------' AS 'Case3';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖3','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖3多l','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC3';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode);
-- 
CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @imID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @imID;
SELECT IF(@iErrorCode = 7,'测试成功','测试失败') AS 'Testing Case3 Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @imID;
DELETE FROM t_commodity WHERE F_ID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID=@imID;
DELETE FROM t_commodity WHERE F_ID = @imID;

SELECT '-------------------- Case4:多包装 2条码 删除失败 -------------------------' AS 'Case4';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖4','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖4多','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode1 = 'AABBCC4A';
SET @sBarcode2 = 'AABBCC4B';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode2);
-- 
CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @imID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @imID;
SELECT IF(@iErrorCode = 7,'测试成功','测试失败') AS 'Case4 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode1, '%')
AND F_OldValue LIKE CONCAT('%', @sBarcode2, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @imID;
DELETE FROM t_commodity WHERE F_ID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID=@imID;
DELETE FROM t_commodity WHERE F_ID = @imID;

SELECT '-------------------- Case5:单品有多包装 单品1条码 多包装1条码 删除成功 -------------------------' AS 'Case5';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖5','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖5多','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode1 = 'AABBCC5A';
SET @sBarcode2 = 'AABBCC5B';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode2);
-- 
CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SET @iBarcodesNum = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @imID;
SET @mBarcodesNum = found_rows();
SELECT IF(@iBarcodesNum = 0 AND @mBarcodesNum = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case5 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode1, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE5 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @imID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case6:单品有多包装 单品2条码 多包装1条码 删除成功 -------------------------' AS 'Case6';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖6','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖6多','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode1 = 'AABBCC6A';
SET @sBarcode2 = 'AABBCC6B';
SET @sBarcode3 = 'AABBCC6C';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode2);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode3);

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SET @iBarcodesNum = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @imID;
SET @mBarcodesNum = found_rows();
SELECT IF(@iBarcodesNum = 0 AND @mBarcodesNum = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case6 Testing Result';

DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @imID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case7:单品有多包装 单品1条码 多包装2条码 删除成功 -------------------------' AS 'Case7';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖7','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖7多','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode1 = 'AABBCC6A';
SET @sBarcode2 = 'AABBCC6B';
SET @sBarcode3 = 'AABBCC6C';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode2);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode3);

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SET @iBarcodesNum = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @imID;
SET @mBarcodesNum = found_rows();
SELECT IF(@iBarcodesNum = 0 AND @mBarcodesNum = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case7 Testing Result';

DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @imID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 8: 单品有多包装 单品2条码 多包装2条码 删除成功 -------------------------' AS 'Case8';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖8','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖8多','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode1 = 'AABBCC6A';
SET @sBarcode2 = 'AABBCC6B';
SET @sBarcode3 = 'AABBCC6C';
SET @sBarcode4 = 'AABBCC6D';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode2);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode3);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode4);

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SET @iBarcodesNum = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @imID;
SET @mBarcodesNum = found_rows();
SELECT IF(@iBarcodesNum = 0 AND @mBarcodesNum = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case8 Testing Result';

DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @imID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 9: 单品有2个多包装 单品1条码 多包装各1条码 删除成功 -------------------------' AS 'Case9';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖9','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖9多A','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID1 = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖9多B','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID2 = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode1 = 'AABBCC6A';
SET @sBarcode2 = 'AABBCC6B';
SET @sBarcode3 = 'AABBCC6C';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID1,@sBarcode2);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID2,@sBarcode3);

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SET @iBarcodesNum = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @imID1;
SET @mBarcodesNum1 = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @imID2;
SET @mBarcodesNum2 = found_rows();
SELECT IF(@iBarcodesNum = 0 AND @mBarcodesNum1 = 0 AND @mBarcodesNum2 = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case9 Testing Result';

DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @imID1;
DELETE FROM t_commodity WHERE F_ID = @imID2;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 10: 单品有2个多包装 单品2条码 多包装各2条码 删除成功 -------------------------' AS 'Case10';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖10','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖10多A','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID1 = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖10多B','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID2 = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode1 = 'AABBCC6A';
SET @sBarcode2 = 'AABBCC6B';
SET @sBarcode3 = 'AABBCC6C';
SET @sBarcode4 = 'AABBCC6D';
SET @sBarcode5 = 'AABBCC6E';
SET @sBarcode6 = 'AABBCC6F';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode2);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID1,@sBarcode3);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID1,@sBarcode4);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID2,@sBarcode5);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID2,@sBarcode6);

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SET @iBarcodesNum = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @imID1;
SET @mBarcodesNum1 = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @imID2;
SET @mBarcodesNum2 = found_rows();
SELECT IF(@iBarcodesNum = 0 AND @mBarcodesNum1 = 0 AND @mBarcodesNum2 = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case10 Testing Result';

DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @imID1;
DELETE FROM t_commodity WHERE F_ID = @imID2;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 11: 商品采购单商品有记录 删除失败 -------------------------' AS 'Case11';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖11','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC11';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @ibID = last_insert_id();

INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (1, @iID, 200, '阿尔卑斯棒棒糖11', @ibID, 1, 11.1);
SET @pocID = last_insert_id();

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case11 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_ID = @pocID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 12: 商品的入库商品有记录 删除失败 -------------------------' AS 'Case12';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖12','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC11';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @ibID = last_insert_id();

INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime)
VALUES (1, @iID, 200, 1, '阿尔卑斯棒棒糖12', @ibID, 10, 2000, now(), 36, now());
SET @wcID = last_insert_id();

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case12 Testing Result';

DELETE FROM t_warehousingcommodity WHERE F_ID = @wcID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 13: 商品的零售商品有记录 删除失败 -------------------------' AS 'Case13';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖13','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC11';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @ibID = last_insert_id();

INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (1, @iID,'阿尔卑斯棒棒糖13', @ibID, 200, 111, 100, 100, 100, null);
SET @rtcID = last_insert_id();

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case13 Testing Result';

DELETE FROM t_retailtradecommodity WHERE F_ID = @rtcID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 14: 商品的盘点商品有记录 删除失败 -------------------------' AS 'Case14';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖14','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC11';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @ibID = last_insert_id();

INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal, F_NOSystem)
VALUES (1, @iID, '阿尔卑斯棒棒糖14', '克', @ibID, 1, 200, null);
SET @icID = last_insert_id();

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case14 Testing Result';

DELETE FROM t_inventorycommodity WHERE F_ID = @icID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 15: 商品的采购退货商品有记录 删除失败 -------------------------' AS 'Case14';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖14','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();

SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC11';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @ibID = last_insert_id();

INSERT INTO nbr.t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_BarcodeID, F_CommodityID, F_CommodityName, F_NO, F_Specification, F_PurchasingPrice)
VALUES (1, @ibID, @iID, '阿尔卑斯棒棒糖14', 200, '箱', 1);
SET @rcscID = last_insert_id();

CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case14 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @rcscID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;


SELECT '-------------------- Case 16: 商品在促销范围有依赖, 商品的条形码删除失败 -------------------------' AS 'Case16';
--  创建促销  
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
--  创建商品 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'阿尔卑斯棒棒糖16','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
--  创建条形码
SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC16';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iBarcodeID = last_insert_id();
--  创建促销范围
INSERT INTO t_promotionscope (F_PromotionID, F_CommodityID, F_CommodityName)
VALUES (@iPromotionID, @iID, '阿尔卑斯棒棒糖16');
SET @iPromotionScopeID = last_insert_id();
-- 
CALL SP_Barcodes_DeleteBySimpleCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_ID = @iBarcodeID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case16 Testing Result';

DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM t_promotionscope WHERE F_ID = @iPromotionScopeID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
