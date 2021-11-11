SELECT '++++++++++++++++++ Test_SP_Commodity_DeleteSimple  .sql ++++++++++++++++++++';

SELECT '-------------------- Case1:单品无多包装 1条码 删除成功 -------------------------' AS 'Case1';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼1号','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
-- 
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SET @commodity = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SET @barcodes = found_rows();
SELECT IF(@commodity = 0 AND @barcodes = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case1 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE1 Testing Result';
-- 
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case2:单品无多包装 2条码 删除成功 -------------------------' AS 'Case2';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼2号','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
-- 
SET @sBarcode1 = 'DDEEFF2A';
SET @sBarcode2 = 'DDEEFF2B';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode2);
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SET @commodity = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SET @barcodes = found_rows();
SELECT IF(@commodity = 0 AND @barcodes = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode1, '%')
AND F_OldValue LIKE CONCAT('%', @sBarcode2, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE2 Testing Result';
-- 
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE2 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case3:单品有1个多包装 单品1条码 多包装1条码 删除失败 -------------------------' AS 'Case3';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼3号','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼4号','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',@iID,0,'.................',2);
SET @imID = last_insert_id();
-- 
SET @sBarcode1 = 'DDEEFF3A';
SET @sBarcode2 = 'DDEEFF3B';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode1);
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@imID,@sBarcode2);
-- 
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
-- 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SET @commodity = found_rows();
SELECT IF(@commodity = 1 AND @iErrorCode = 7,'测试成功','测试失败') AS 'Case3 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode1, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE3 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID IN (@iID,@imID) ;
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@imID) ;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@imID);

SELECT '-------------------- Case4:该商品是组合商品 -------------------------' AS 'Case4';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼17号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,1,'1111111',1);
SET @iID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼18号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',@iID,2,'1111111',0);
SET @commID1 = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼19号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',@iID,2,'1111111',0);
SET @commID2 = LAST_INSERT_ID();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID1, '987ccc9a'); -- 组合A商品单品的条码
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@commID2, '987ccc9b'); -- 组合B商品单品的条码
-- 
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID1, 10);
INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO) VALUES (@iID, @commID2, 5);
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case4 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE4 Testing Result';
-- 
DELETE FROM t_subcommodity WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID IN (@iID,@commID1,@commID2) ;
DELETE FROM t_commodity WHERE F_ID IN (@iID,@commID1,@commID2) ;

SELECT '-------------------- Case5:单品或多包装存在销存记录 采购单商品有记录 删除商品及条码失败 ec = 7 -------------------------' AS 'Case5';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼20号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',0);
SET @iID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf11a');
SET @ibID = last_insert_id();
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (1, @iID, 100, '港式优品11a', @ibID, 1, 11.1);
SET @pocID = last_insert_id();
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE5 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @pocID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case6:单品或多包装有销存记录，盘点商品有记录 删除商品及条码失败 ec = 7 -------------------------' AS 'Case6';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼21号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',0);
SET @iID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf12a');
SET @ibID = last_insert_id();
-- 
INSERT INTO t_inventorycommodity (F_InventorySheetID, F_CommodityID, F_CommodityName, F_Specification, F_BarcodeID, F_PackageUnitID, F_NOReal)
VALUES (2, @iID, '奥特曼22号', '克', @ibID, 1, 200);
SET @iicID = last_insert_id();
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case6 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE6 Testing Result';
-- 
DELETE FROM t_inventorycommodity WHERE F_ID = @iicID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case7:单品或多包装有销存记录，零售商品有记录 删除商品及条码失败 ec = 7 -------------------------' AS 'Case7';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼23号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',0);
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
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case7 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE7 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @irtcID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case8:单品或多包装有销存记录，入库商品有记录 删除商品及条码失败 ec = 7 -------------------------' AS 'Case8';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼24号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',0);
SET @iID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf14a');
SET @ibID = last_insert_id();
-- 
INSERT INTO t_warehousingcommodity (F_WarehousingID, F_CommodityID, F_NO, F_PackageUnitID, F_CommodityName, F_BarcodeID, F_Price, F_Amount, F_ProductionDatetime, F_ShelfLife, F_ExpireDatetime)
VALUES (1, @iID, 122, 1, '港式优品14a', @ibID, 10, 1000, now(), 23, now());
SET @iwcID = last_insert_id();
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
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
DELETE FROM t_warehousingcommodity WHERE F_ID = @iwcID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- case9:传入多包装的Id 单品有销存记录、多包装无销存记录 ec=7 -------------------------' AS 'Case9';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼25号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',0);
SET @cId=last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼26号','方便面','克',3,'箱',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',@cId,3,'1111111',2);
SET @iID=last_insert_id();
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Testing Case9 Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE9 Testing Result';
DELETE FROM t_commodity WHERE F_ID IN (@iID,@cId);

SELECT '-------------------- Case10:重复删除商品 -------------------------' AS 'Case10';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (2,'奥特曼1号','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
-- 
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '该商品已删除，不能重复删除','测试成功','测试失败') AS 'Case10 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE10 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case11:删除不存在的商品 -------------------------' AS 'Case11';
SET @iID = -1;
SET @iStaffID = 3;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 2,'测试成功','测试失败') AS 'Case11 Testing Result';


SELECT '-------------------- Case12:商品有促销依赖，促销没有被删除，那么该商品不可以被删除 -------------------------' AS 'Case12';
-- 
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
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼1号','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iCommodityID = last_insert_id();
-- 
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iCommodityID,@sBarcode);
-- 
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
SET @promotionScopeID = last_insert_id();
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case12_1 Testing Result';
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iCommodityID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iCommodityID AND F_Status = 0;
SET @commodity = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
SET @barcodes = found_rows();
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '该商品有促销依赖，不能删除','测试成功','测试失败') AS 'Case12_2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0, '测试成功', '测试失败') AS 'CASE12_3 Testing Result';
-- 
DELETE FROM t_promotionscope WHERE F_ID = @promotionScopeID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '-------------------- Case13:商品有促销依赖，但是促销已经被删除，商品又无其他依赖，那么该商品可以被删除 -------------------------' AS 'Case13';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '满10-1';
SET @status = 1;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iPromotionID = last_insert_id();
-- 
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'奥特曼1号','薯片','克',1,'箱',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iCommodityID = last_insert_id();
-- 
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iCommodityID,@sBarcode);
-- 
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
SET @promotionScopeID = last_insert_id(); 
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = @promotionScopeID;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case13_1 Testing Result';
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @iCommodityID, @iStaffID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iCommodityID AND F_Status = 0;
SET @commodity = found_rows();
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
SET @barcodes = found_rows();
SELECT IF(@commodity = 0 AND @barcodes = 0 AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case13_2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_OldValue LIKE CONCAT('%', @sBarcode, '%')
AND F_NewValue = "" 
AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1, '测试成功', '测试失败') AS 'CASE13_3 Testing Result';
-- 
DELETE FROM t_promotionscope WHERE F_ID = @promotionScopeID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;



SELECT '-------------------- Case14：商品有优惠券范围依赖，不能删除(优惠券已启用) -------------------------' AS 'Case14';
-- 
-- 新建普通商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '优惠券普通商品', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2020-04-20 08:57:14', '2020-04-20 08:57:14', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 
-- 创建优惠券
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, '未起用的优惠券', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '1933-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- 创建优惠券范围
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @commodityID, '优惠券普通商品');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @commodityID, @iStaffID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '商品有优惠券范围依赖，不能删除', '测试成功', '测试失败') AS 'Case14 Testing Result';
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;


SELECT '-------------------- Case15：商品有优惠券范围依赖，不能删除(优惠券已删除) -------------------------' AS 'Case15';
-- 
-- 新建普通商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (1, '优惠券普通商品', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2020-04-20 08:57:14', '2020-04-20 08:57:14', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 
-- 创建优惠券
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (1, 0, 0, 1, 1, 1, '未起用的优惠券', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '1933-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- 创建优惠券范围
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @commodityID, '优惠券普通商品');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @commodityID, @iStaffID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '商品有优惠券范围依赖，不能删除', '测试成功', '测试失败') AS 'Case15 Testing Result';
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case16：商品有优惠券范围依赖，不能删除(优惠券未启用) -------------------------' AS 'Case16';
-- 
-- 新建普通商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '优惠券普通商品', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2020-04-20 08:57:14', '2020-04-20 08:57:14', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 
-- 创建优惠券
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, '未起用的优惠券', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- 创建优惠券范围
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @commodityID, '优惠券普通商品');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @commodityID, @iStaffID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '商品有优惠券范围依赖，不能删除', '测试成功', '测试失败') AS 'Case16 Testing Result';
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;



SELECT '-------------------- Case17：商品有优惠券范围依赖，不能删除(优惠券已过期) -------------------------' AS 'Case17';
-- 
-- 新建普通商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '优惠券普通商品', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2020-04-20 08:57:14', '2020-04-20 08:57:14', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 
-- 创建优惠券
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, '未起用的优惠券', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '1933-03-03 15:15:15', '1999-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- 创建优惠券范围
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @commodityID, '优惠券普通商品');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @commodityID, @iStaffID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '商品有优惠券范围依赖，不能删除', '测试成功', '测试失败') AS 'Case17 Testing Result';
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @commodityID;




SELECT '-------------------- Case18：商品有优惠券全场依赖，能被删除 -------------------------' AS 'Case18';
-- 
-- 新建普通商品
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '优惠券普通商品', '薯片', '克', 1, '箱', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2020-04-20 08:57:14', '2020-04-20 08:57:14', NULL, NULL, NULL, NULL);
SET @commodityID = last_insert_id();
-- 
-- 创建优惠券
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, '未起用的优惠券', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteSimple(@iErrorCode, @sErrorMsg, @commodityID, @iStaffID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case18 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = @couponID;