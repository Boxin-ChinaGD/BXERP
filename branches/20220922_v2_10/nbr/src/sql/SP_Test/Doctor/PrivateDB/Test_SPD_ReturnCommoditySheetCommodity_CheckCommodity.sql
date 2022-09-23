SELECT '++++++++++++++++++ Test_SPD_ReturnCommoditySheetCommodity_CheckCommodity.sql ++++++++++++++++++++';
SELECT '------------------ CASE1:�������� --------------------' AS 'CASE1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'CASE1 RESULT';
-- 
SELECT '------------------ CASE2:�˻���Ʒ�������Ʒ --------------------' AS 'CASE2';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
VALUES (3, 3);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 45, 7, 100, '��');
SET @iReturnCommoditySheetCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('�˻�����Ʒ', @iReturnCommoditySheetCommodityID, 'ֻ����δɾ������ͨ��Ʒ'), '���Գɹ�', '����ʧ��') AS 'CASE2 RESULT';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @iReturnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
-- 
SELECT '------------------ CASE3:�˻���Ʒ�Ƕ��װ��Ʒ --------------------' AS 'CASE3';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
VALUES (3, 3);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 52, 7, 100, '��');
SET @iReturnCommoditySheetCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('�˻�����Ʒ', @iReturnCommoditySheetCommodityID, 'ֻ����δɾ������ͨ��Ʒ'), '���Գɹ�', '����ʧ��') AS 'CASE2 RESULT';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @iReturnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
-- 
SELECT '------------------ CASE4:�˻���Ʒ����ɾ����Ʒ --------------------' AS 'CASE4';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
VALUES (3, 3);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 50, 7, 100, '��');
SET @iReturnCommoditySheetCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('�˻�����Ʒ', @iReturnCommoditySheetCommodityID, 'ֻ����δɾ������ͨ��Ʒ'), '���Գɹ�', '����ʧ��') AS 'CASE4 RESULT';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @iReturnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
-- 
SELECT '------------------ CASE5:�˻���Ʒ����Ϊ0 --------------------' AS 'CASE5';
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
VALUES (3, 3);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, 5, 7, 0, '��');
SET @iReturnCommoditySheetCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('�˻�����Ʒ', @iReturnCommoditySheetCommodityID, '����Ʒ�����������0'), '���Գɹ�', '����ʧ��') AS 'CASE5 RESULT';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @iReturnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
-- 
SELECT '------------------ CASE6:�˻���Ʒ�Ƿ�����Ʒ --------------------' AS 'CASE6';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'˳��','���','��',4,NULL,4,4,'SF',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'˳����',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID)
VALUES (3, 3);
SET @iReturnCommoditySheetID = last_insert_id();
-- 
INSERT INTO t_returncommoditysheetcommodity (F_ReturnCommoditySheetID, F_CommodityID, F_BarcodeID, F_NO, F_Specification)
VALUES (@iReturnCommoditySheetID, @iCommodityID, 7, 100, '��');
SET @iReturnCommoditySheetCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_ReturnCommoditySheetCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode,@sErrorMsg;
-- 
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = concat('�˻�����Ʒ', @iReturnCommoditySheetCommodityID, 'ֻ����δɾ������ͨ��Ʒ'), '���Գɹ�', '����ʧ��') AS 'CASE6 RESULT';
-- 
DELETE FROM t_returncommoditysheetcommodity WHERE F_ID = @iReturnCommoditySheetCommodityID;
DELETE FROM t_ReturnCommoditySheet WHERE F_ID = @iReturnCommoditySheetID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;