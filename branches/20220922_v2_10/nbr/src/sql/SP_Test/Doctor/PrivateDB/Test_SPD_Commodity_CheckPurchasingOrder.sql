SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckPurchasingOrder.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯ����Ʒ�вɹ���λȴû�ж�Ӧ�Ĳɹ����� -------------------------' AS 'Case1';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',-1,-0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID ,'��Ҫ�ж�Ӧ�Ĳɹ�����') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case2:��ѯ��������Ʒ�����ڹ�Ӧ�� -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);

SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:��ѯ����Ʒû�вɹ���λȴ�ж�Ӧ�Ĳɹ����� -------------------------' AS 'Case3';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4, NULL,4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',-1,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO t_purchasingordercommodity (F_PurchasingOrderID, F_CommodityID, F_CommodityNO, F_CommodityName, F_BarcodeID, F_PackageUnitID, F_PriceSuggestion)
VALUES (1, @iCommodityID, 200, '�ɱȿ���Ƭ', 1, 1, 11.1);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID ,'���ܱ��ɹ���������') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
DELETE FROM t_purchasingordercommodity WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '+++++++++++++++++++++++ case4:�����Ʒ�вɹ���λ +++++++++++++++++++++++++++++++++++++' AS 'case4';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4, '��',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',-1,1);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�����Ʒ', @iCommodityID , '�����вɹ���λ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '+++++++++++++++++++++++ case5:���װ��Ʒ�вɹ���λ +++++++++++++++++++++++++++++++++++++' AS 'case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4, '��',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',-1,2);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('���װ��Ʒ', @iCommodityID , '�����вɹ���λ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '+++++++++++++++++++++++ case6:������Ʒ�вɹ���λ +++++++++++++++++++++++++++++++++++++' AS 'case6';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'˳��','���','��',4, '��',4,4,'SF',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'˳����',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckPurchasingOrder(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('������Ʒ', @iCommodityID , '�����вɹ���λ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;