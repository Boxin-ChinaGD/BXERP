SELECT '++++++++++++++++++ Test_SPD_PurchasingOrderCommodity_CheckCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

-- t_purchasingordercommodity��F_commodityID��t_commodity����������ܲ��벻���ڵ���ƷID


SELECT '-------------------- Case2:�ɹ�������Ʒ��Ӧ����Ʒ����Ϊ��ɾ����Ʒ -------------------------' AS 'Case2';
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (12, 49, 300, '�����ཷζ��Ƭ1', 101, 1, 11.1, '2019/5/30 9:17:01', '2019/5/30 9:17:01');
-- 
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�ɹ�������Ʒ', @iPurchasingOrderCommodityID, '��Ӧ����Ʒ����Ϊ��ɾ����Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;



SELECT '-------------------- Case3:�ɹ�������Ʒ��Ӧ����Ʒ����Ϊ���װ��Ʒ -------------------------' AS 'Case3';
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (12, 51, 300, '����ţ��ζ��Ƭ2', 101, 1, 11.1, '2019/5/30 9:17:01', '2019/5/30 9:17:01');
-- 
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�ɹ�������Ʒ', @iPurchasingOrderCommodityID, '��Ӧ����Ʒ����Ϊ���װ��Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;


SELECT '-------------------- Case4:�ɹ�������Ʒ��Ӧ����Ʒ����Ϊ�����Ʒ -------------------------' AS 'Case3';
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (12, 45, 300, '��Ը����ζ��Ƭ', 101, 1, 11.1, '2019/5/30 9:17:01', '2019/5/30 9:17:01');
-- 
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�ɹ�������Ʒ', @iPurchasingOrderCommodityID, '��Ӧ����Ʒ����Ϊ�������Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;


SELECT '-------------------- Case5:�ɹ�������Ʒ�е���Ʒ�����������0 -------------------------' AS 'Case3';
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (12, 2, 0, '�ɿڿ���', 101, 1, 11.1, '2019/5/30 9:17:01', '2019/5/30 9:17:01');
-- 
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�ɹ�������Ʒ', @iPurchasingOrderCommodityID, '����Ʒ�����������0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;

SELECT '-------------------- Case6:�ɹ�������Ʒ��Ӧ����Ʒ����Ϊ������Ʒ -------------------------' AS 'Case6';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'˳��','���','��',4,NULL,4,4,'SF',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'˳����',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion, F_CreateDatetime, F_UpdateDatetime)
VALUES (12, @iCommodityID, 300, '˳��', 101, 1, 11.1, '2019/5/30 9:17:01', '2019/5/30 9:17:01');
-- 
SET @iPurchasingOrderCommodityID = LAST_INSERT_ID();
SET @iErrorCode := 0;
SET @sErrorMsg := '';
-- 
CALL SPD_PurchasingOrderCommodity_CheckCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�ɹ�������Ʒ', @iPurchasingOrderCommodityID, '��Ӧ����Ʒ����Ϊ��������Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_ID = @iPurchasingOrderCommodityID;
