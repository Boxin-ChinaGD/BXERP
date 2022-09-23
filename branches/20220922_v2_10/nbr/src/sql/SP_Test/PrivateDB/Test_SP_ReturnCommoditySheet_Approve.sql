SELECT '++++++++++++++++++ Test_SP_ReturnCommoditySheet_Approve.sql ++++++++++++++++++++';

SELECT '-------------------- CASE1:���˻���δ���(��Ʒ) -------------------------' AS 'Case1';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱ�xxx', '��Ƭ', '��', 1, 'ǧ��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID,@iCommodityID,'�ɱ�xxx', 7, 100, '��', 5.0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ReturnCommoditySheet
WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- ��֤�Ƿ�����ʷԭò
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = @iCommodityID AND F_CommodityName = '�ɱ�xxx';
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- CASE2:���˻���δ���(���װ��Ʒ) -------------------------' AS 'Case2';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱ�xxx', '��Ƭ', '��', 1, 'ǧ��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID, @iCommodityID,'�ɱ�xxx', 55, 10, '��', 8.0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ReturnCommoditySheet
WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- ��֤�Ƿ�����ʷԭò
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = @iCommodityID AND F_CommodityName = '�ɱ�xxx';
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- CASE3:���˻���δ���,���˻�����������Ʒ����Ʒ�Ͷ��װ��Ʒ�� -------------------------' AS 'Case3';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱ�xxx1', '��Ƭ', '��', 1, 'ǧ��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID1 = last_insert_id();
-- 
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID1, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱ�xxx2', '��Ƭ', '��', 1, 'ǧ��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID2 = last_insert_id();
-- 
INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID, F_ShopID)
VALUES (5,5,2);
SET @iID = LAST_INSERT_ID();
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID,@iCommodityID1,'�ɱ�xxx1', 7, 100, '��', 5.0);
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID, @iCommodityID2,'�ɱ�xxx2', 55, 10, '��', 8.0);

CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_ReturnCommoditySheet
WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- ��֤�Ƿ�����ʷԭò
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = @iCommodityID1 AND F_CommodityName = '�ɱ�xxx1';
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID = @iCommodityID2 AND F_CommodityName = '�ɱ�xxx2';
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;
DELETE FROM t_commodityhistory WHERE F_CommodityID IN (@iCommodityID1,@iCommodityID2);
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID IN (@iCommodityID1,@iCommodityID2);

SELECT '-------------------- CASE4:���˻�������� -------------------------' AS 'Case4';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4)
VALUES (0, '�ɱ�xxx', '��Ƭ', '��', 1, 'ǧ��', 3, 1, 'SP', 1, 
11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, NULL, '2019-12-25 10:41:25', '2019-12-25 10:47:03', NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart, F_CurrentWarehousingID)
VALUES (@iCommodityID, 2, 8, 12, 0, -1, -1, NULL);
SET @iCommShopInfoID = last_insert_id();
-- 
INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID,F_Status, F_ShopID)
VALUES (4,5,1,2);
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID,F_CommodityName, F_BarcodeID, F_NO, F_Specification, F_PurchasingPrice)
VALUES (@iID,@iCommodityID,'�ɱ�xxx1', 7, 100, '��', 5.0);
SET @sErrorMsg = '';

CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '���˻�������ˣ������ظ�����', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;
DELETE FROM t_commodityhistory WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodityshopinfo WHERE F_ID = @iCommShopInfoID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- CASE5:���˻��������� -------------------------' AS 'Case5';
SET @sErrorMsg = '';
CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SET @iID = -5;

SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTradePromoting WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '���޸òɹ��˻���', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;

SELECT '-------------------- CASE6:���û���˻���Ʒ���˻��� -------------------------' AS 'Case6';

INSERT INTO T_ReturnCommoditySheet (F_StaffID,F_ProviderID,F_Status,F_CreateDatetime,F_UpdateDatetime, F_ShopID)
VALUES (5,5,0,now(),now(),2);
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_ReturnCommoditySheet_Approve(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7 AND @sErrorMsg = '���˻���û���˻���Ʒ', '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- 
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID;