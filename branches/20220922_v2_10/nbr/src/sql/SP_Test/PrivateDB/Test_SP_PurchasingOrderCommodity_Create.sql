SELECT '++++++++++++++++++ Test_SP_PurchasingOrderCommodity_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ������Ӳɹ�����Ʒ -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
INSERT INTO t_purchasingorder (F_ShopID, F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (2, 0, 1, 1, '1', 1, '1', NOW(), NOW(), NOW(), NOW());
SET @iPurchasingOrderID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID = last_insert_ID();

SET @iCommodityNO = 201;
SET @fPriceSuggestion = 1.0;
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);

SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
SELECT @iErrorCode;

DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case2: ���һ�������ڵ���Ʒ�����ش�����7 -------------------------' AS 'Case2';
SET @sErrorMsg = '';
SET @iCommodityID = -1;
SET @iCommodityNO = 201;
SET @iBarcodeID = 1;
SET @fPriceSuggestion = 1.0;

CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);

SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';
SELECT @iErrorCode;

SELECT '-------------------- Case3: ���һ��������iPurchasingOrderID(-1)�����ش�����3 -------------------------' AS 'Case3';
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @iCommodityNO = 201;
SET @iBarcodeID = 1;
SET @fPriceSuggestion = 1.0;
SET @iPurchasingOrderID=-1;

CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);

SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 3, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4: ���һ�����ݱ����Ѿ�����iPurchasingOrderID(1)��iCommodityID(1)����ϣ����ش�����3 -------------------------' AS 'Case4';
SET @sErrorMsg = '';
SET @iCommodityID = 1;
SET @iCommodityNO = 203;
SET @iBarcodeID = 1;
SET @fPriceSuggestion = 1.0;
SET @iPurchasingOrderID=1;

CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

SELECT '-------------------- Case5:���Ѿ�ɾ���Ķ��װ��Ʒ�����ɹ�����Ʒ�����ش�����2 -------------------------' AS 'Case5';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (2, '��Ը����ζ��Ƭ6616', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1,
11.8, 11, 1, 1, 'url=116843434834', 
 3, 30, '2018-04-14', 20, 0, 0, '1111111', 2);

SET @sErrorMsg = '';
SET @iCommodityID = last_insert_id();
SET @iCommodityNO = 204;
SET @iBarcodeID = 1;
SET @fPriceSuggestion = 1.0;
SET @iPurchasingOrderID = 1;

CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case6:���Ѿ�ɾ������ͨ��Ʒ�����ɹ�����Ʒ�����ش�����2 -------------------------' AS 'Case6';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (2, '��Ը����ζ��Ƭ6666', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111', 0);

SET @sErrorMsg = '';
SET @iCommodityID = last_insert_id();
SET @iCommodityNO = 205;
SET @iBarcodeID = 1;
SET @fPriceSuggestion = 1.0;
SET @iPurchasingOrderID = 1;

CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case7:�������Ʒ�����ɹ�����Ʒ�����ش�����7 -------------------------' AS 'Case7';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '��Ը����ζ��Ƭ6667', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1,
11.8, 11, 1, 1, 'url=116843434834', 
 3, 30, '2018-04-14', 20, 0, 0, '1111111', 1);

SET @sErrorMsg = '';
SET @iCommodityID = last_insert_id();
SET @iCommodityNO = 206;
SET @iBarcodeID = 1;
SET @fPriceSuggestion = 1.0;
SET @iPurchasingOrderID = 1;

CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case8:����ͨ��Ʒ�����ɹ�����Ʒ�����ش�����0 -------------------------' AS 'Case8';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '��Ը����ζ��Ƭ6668', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111', 0);

SET @sErrorMsg = '';
SET @iCommodityID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID = last_insert_ID();

SET @iCommodityNO = 207;
SET @fPriceSuggestion = 1.0;
SET @iPurchasingOrderID = 1;

CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case9:�ö��װ��Ʒ�����ɹ�����Ʒ�����ش�����7 -------------------------' AS 'Case9';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '��Ը����ζ��Ƭ6669', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1,
11.8, 11, 1, 1, 'url=116843434834', 
 3, 30, '2018-04-14', 20, 0, 0, '1111111', 2);

SET @sErrorMsg = '';
SET @iCommodityID = last_insert_id();
SET @iCommodityNO = 207;
SET @iBarcodeID = 1;
SET @fPriceSuggestion = 1.0;
SET @iPurchasingOrderID = 1;

CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);

SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';

DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case10:���Ѿ�ɾ�������Ʒ�����ɹ�����Ʒ�����ش�����2 -------------------------' AS 'Case10';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (2, '��Ը����ζ��Ƭ6670', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1,
11.8, 11, 1, 1, 'url=116843434834', 
 3, 30, '2018-04-14', 20, 0, 0, '1111111', 1);
-- 
SET @sErrorMsg = '';
SET @iCommodityID = last_insert_id();
SET @iCommodityNO = 206;
SET @iBarcodeID = 1;
SET @fPriceSuggestion = 1.0;
SET @iPurchasingOrderID = 1;
-- 
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'CASE10 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case11:�÷�����Ʒ�����ɹ�����Ʒ�����ش�����7 -------------------------' AS 'Case11';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '��Ը����ζ��Ƭ6670', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1,
11.8, 11, 1, 1, 'url=116843434834', 
 3, 30, '2018-04-14', 20, 0, 0, '1111111', 3);
-- 
SET @sErrorMsg = '';
SET @iCommodityID = last_insert_id();
SET @iCommodityNO = 1;
SET @iBarcodeID = 1;
SET @fPriceSuggestion = 1.0;
SET @iPurchasingOrderID = 1;
-- 
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE11 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case12:�����������ID����Ʒʵ��������ID����Ӧ�����ش�����7 -------------------------' AS 'Case12';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
INSERT INTO t_purchasingorder (F_ShopID, F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime)
VALUES (2, 0, 1, 1, '1', 1, '1', NOW(), NOW(), NOW(), NOW());
SET @iPurchasingOrderID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeIDa = last_insert_ID();

SET @iCommodityNO = 201;
SET @fPriceSuggestion = 1.0;
SET @iBarcodeID = 1;
CALL SP_PurchasingOrderCommodity_Create(@iErrorCode, @sErrorMsg, @iPurchasingOrderID, @iCommodityID, @iCommodityNO, @iBarcodeID, @fPriceSuggestion);

SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '������ID����Ʒʵ��������ID����Ӧ', '���Գɹ�', '����ʧ��') AS 'CASE12 Testing Result';
SELECT @iErrorCode;

DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = @iPurchasingOrderID;
DELETE FROM T_PurchasingOrder WHERE F_ID = @iPurchasingOrderID;
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeIDa;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;