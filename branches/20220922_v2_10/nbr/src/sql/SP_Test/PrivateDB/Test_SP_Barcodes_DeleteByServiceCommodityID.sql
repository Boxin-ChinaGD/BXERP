SELECT '-------------------- Case 1: ɾ�������Ʒ�������� -------------------------' AS 'Case1';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'���������1��','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',1);
SET @iID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_DeleteByServiceCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 2: ɾ�����װ��Ʒ�������� -------------------------' AS 'Case2';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'���������1��','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',2);
SET @iID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_DeleteByServiceCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 3: ɾ����Ʒ�������� -------------------------' AS 'Case3';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'���������1��','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_DeleteByServiceCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 4: ɾ��������Ʒ�������� -------------------------' AS 'Case4';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'���������1��','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',3);
SET @iID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Barcodes_DeleteByServiceCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;

SELECT '-------------------- Case 5: ��Ʒ�����ۼ�¼��ɾ��ʧ�� -------------------------' AS 'Case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'���������1��','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',3);
SET @iID = last_insert_id();
-- 
SET @sErrorMsg = '';
SET @iStaffID = 3;
SET @sBarcode = 'AABBCC1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @ibID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (1, @iID,'���������1��', @ibID, 200, 111, 100, 100, 100, null);
SET @rtcID = last_insert_id();
--
CALL SP_Barcodes_DeleteByServiceCommodityID(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT 1 FROM t_barcodes WHERE F_CommodityID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case5 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @rtcID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;