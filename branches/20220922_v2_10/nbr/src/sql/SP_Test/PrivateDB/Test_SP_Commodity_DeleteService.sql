SELECT '-------------------- Case1:ɾ�����װ��Ʒ -------------------------' AS 'Case1';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'֩����1��aaa','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',2);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 7,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;
SELECT '-------------------- Case2:ɾ�������Ʒ -------------------------' AS 'Case2';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'֩����1��aaa','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',1);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 7,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;
SELECT '-------------------- Case3:ɾ����ͨ��Ʒ -------------------------' AS 'Case3';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'֩����1��aaa','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 7,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_commodity WHERE F_ID = @iID;
SELECT '-------------------- Case4:ɾ��������Ʒ -------------------------' AS 'Case4';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'֩����1��aaa','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',3);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
SELECT '-------------------- Case5:�ظ�ɾ��������Ʒ -------------------------' AS 'Case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'֩����1��aaa','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',3);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);

SELECT IF(@iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 7,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case6:ɾ��Ԥɾ���ķ�����Ʒ -------------------------' AS 'Case6';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'֩����1��aaa','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',3);
SET @iID = last_insert_id();
SET @sErrorMsg = '';
SET @sBarcode = 'DDEEFF1';
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iID,@sBarcode);
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);

SELECT IF(@iErrorCode = 0,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iID;

SELECT '-------------------- Case7:ɾ�������ڵ���Ʒ -------------------------' AS 'Case7';
SET @iID = -1;
SET @iStaffID = 3;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT IF(@iErrorCode = 2,'���Գɹ�','����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case8:������Ʒ�м�¼ ɾ����Ʒ������ʧ�� -------------------------' AS 'Case8';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������23��','������','��',3,'��',3,3,'SP',1,
130,11,1,1,'url=116843434834',
3,'15','2018-04-14','20',0,3,'1111111',3);
SET @iID = last_insert_id();
-- 
INSERT INTO t_barcodes (F_CommodityID, F_Barcode) VALUES (@iID, '987aaaf13a');
SET @ibID = last_insert_id();
-- 
INSERT INTO t_retailtradecommodity (F_TradeID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_PriceOriginal, F_NOCanReturn, F_PriceReturn, F_PriceSpecialOffer, F_PriceVIPOriginal)
VALUES (1, @iID,'������23��', @ibID, 100, 120, 100, 5, 100, NULL);
SET @irtcID = last_insert_id();
-- 
SET @iStaffID = 3;
-- 
CALL SP_Commodity_DeleteService(@iErrorCode, @sErrorMsg, @iID, @iStaffID);
SELECT @iErrorCode;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_Status = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 1,1)
AND F_CommodityID = @iID;
SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';
-- 
DELETE FROM t_retailtradecommodity WHERE F_ID = @irtcID;
DELETE FROM t_providercommodity WHERE F_CommodityID = @iID;
DELETE FROM t_barcodes WHERE F_CommodityID = @iID;
DELETE FROM t_providercommodity WHERE F_CommodityID = @ibID;
DELETE FROM t_barcodes WHERE F_CommodityID = @ibID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;
SELECT '-------------------- Case9:�ձ����м�¼ ɾ����Ʒ������ʧ�� -------------------------' AS 'Case9';
SET @sErrorMsg = '';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������23��','������','��',3,'��',3,3,'SP',1,
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
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';
-- 
DELETE FROM T_RetailTradeDailyReportByCommodity WHERE F_ID = @retailTradeDailyReportByCommodityID;
DELETE FROM t_barcodes WHERE F_ID = @ibID;
DELETE FROM t_commodity WHERE F_ID = @iID;