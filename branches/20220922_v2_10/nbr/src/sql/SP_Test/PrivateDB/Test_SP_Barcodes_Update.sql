SELECT '++++++++++++++++++ Test_SP_Barcodes_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�޸�һ�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 10;
SET @sOldBarcode = '1224599996';
SET @iStaffID = 1;
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, @sOldBarcode);
-- 
SET @iID = last_insert_id();
SET @sBarcode = '2233322233';
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 1, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case2:�޸�һ�������ڵ������� -------------------------' AS 'Case2';
-- 
SET @iCommodityID = 10;
SET @iID = -1;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case3:�޸ĵ������������۵���Ʒ�������� -------------------------' AS 'Case3';
SET @iID = 1;
SET @iCommodityID = 10;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case4:����@iCommodityID�����ڣ�����7 -------------------------' AS 'Case4';
-- 
SET @iID = 1;
SET @iCommodityID = -10;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case5:�޸ĵ��������ڲɹ�������Ʒ�������� -------------------------' AS 'Case5';
SET @iID = 101;
SET @iCommodityID = 10;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case6:�޸ĵ�����������ⵥ��Ʒ�������� -------------------------' AS 'Case6';
SET @iID = 102;
SET @iCommodityID = 10;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;

SELECT '-------------------- Case7:�޸ĵ����������̵㵥��Ʒ�������� -------------------------' AS 'Case7';
SET @iID = 104;
SET @iCommodityID = 10;
SET @sBarcode = '2233322233';
SET @iStaffID = 1;
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
-- 
SELECT 1 FROM t_commodityhistory 
WHERE F_StaffID = @iStaffID 
AND F_ID = (SELECT F_ID FROM t_commodityhistory ORDER BY F_ID DESC LIMIT 0,1)
AND F_NewValue LIKE CONCAT('%', @sBarcode, '%') 
AND F_OldValue LIKE CONCAT('%', @sOldBarcode, '%')
AND F_CommodityID = (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = last_insert_id());
SELECT IF(found_rows() = 0, '���Գɹ�', '����ʧ��') AS 'CASE7 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;


SELECT '-------------------- Case7:����������������ʱ���磺1234567 12345678 �޸�������12345678Ϊ1234567 -------------------------' AS 'Case7';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 10;
SET @sOldBarcode = '12245999961';
SET @iStaffID = 1;
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, @sOldBarcode);
-- 
SET @sOldBarcode = '1234567890';
INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, @sOldBarcode);
-- 
SET @iID = last_insert_id();
SET @sBarcode = '12245999961';
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iID, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
-- 
DELETE FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;
DELETE FROM t_barcodes WHERE F_Barcode = @sOldBarcode AND F_CommodityID = @iCommodityID;



SELECT '-------------------- Case8: �޸ĵ���������ָ��������Χ������, �޸�ʧ�� -------------------------' AS 'Case8';
--  ��������  
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
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
--  ������Ʒ 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (1,'������˹������88','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0);
SET @iCommodityID = last_insert_id();
--  ����������
SET @sErrorMsg = '';
SET @sBarcode = 'AABBCC16';
SET @iStaffID = 3;
INSERT INTO t_barcodes(F_CommodityID,F_Barcode) VALUES(@iCommodityID,@sBarcode);
SET @iBarcodeID = last_insert_id();
--  ����������Χ
INSERT INTO t_promotionscope (F_PromotionID, F_CommodityID, F_CommodityName)
VALUES (@iPromotionID, @iCommodityID, '������˹������88');
SET @iPromotionScopeID = last_insert_id();
-- 
SET @sBarcode = '12245999961';
CALL SP_Barcodes_Update(@iErrorCode, @sErrorMsg, @iBarcodeID, @iCommodityID, @sBarcode, @iStaffID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_barcodes WHERE F_Barcode = @sBarcode AND F_CommodityID = @iCommodityID;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = "�޸ĵ���������ָ��������Χ��������", '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- 

DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM t_promotionscope WHERE F_ID = @iPromotionScopeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;