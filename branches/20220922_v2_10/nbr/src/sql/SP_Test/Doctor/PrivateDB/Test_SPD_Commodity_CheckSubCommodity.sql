SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckSubCommodity.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯ���е������Ʒ��һ����Ʒ����������Ʒ -------------------------' AS 'Case1';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,1);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckSubCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�����Ʒ', @iCommodityID ,'û�ж�Ӧ������Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case2:��ѯ�����������Ʒ����������Ʒ -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SPD_Commodity_CheckSubCommodity(@iErrorCode, @sErrorMsg);

SELECT @iErrorCode, @sErrorMsg;
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';



-- CASE3:��ѯ�����������Ʒ��һ�������Ʒ������Ʒ�������Ʒ
SELECT '-------------------- CASE3:��ѯ�����������Ʒ��һ�������Ʒ������Ʒ�������Ʒ -------------------------' AS 'Case3';
-- ��������ƷA
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,1);
SET @iCommodityID1 = LAST_INSERT_ID();
-- ��������ƷB
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB12','����12','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,1);
SET @iCommodityID2 = LAST_INSERT_ID();
-- ��������ƷA������Ʒ
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID1, @iCommodityID2, 10, 11.0, now(), now());
SET @iSubCommodityID2 = LAST_INSERT_ID();
-- ������ƷB������Ʒ
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID2, 1, 10, 11.0, now(), now());
SET @iSubCommodityID1 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckSubCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�����Ʒ', @iCommodityID1 ,'��Ӧ������Ʒ������ͨ��Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_subcommodity WHERE F_ID = @iSubCommodityID1;
DELETE FROM t_subcommodity WHERE F_ID = @iSubCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID1;

-- Case4:��ѯ�����������Ʒ��һ�������Ʒ������Ʒ�Ƕ��װ��Ʒ
SELECT '-------------------- Case4:��ѯ�����������Ʒ��һ�������Ʒ������Ʒ�Ƕ��װ��Ʒ -------------------------' AS 'Case4';
-- ������ͨ��ƷA
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,1);
SET @iCommodityID1 = LAST_INSERT_ID();
-- �������װ��ƷB
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB12','����12','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',@iCommodityID1,3,'�ǰͿ�AB',0,2);
SET @iCommodityID2 = LAST_INSERT_ID();
-- ��������ƷA������Ʒ
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID1, @iCommodityID2, 10, 11.0, now(), now());
SET @iSubCommodityID1 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckSubCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�����Ʒ', @iCommodityID1 ,'��Ӧ������Ʒ������ͨ��Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_subcommodity WHERE F_ID = @iSubCommodityID1;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID1;

-- Case5:��ѯ�����������Ʒ��һ�������Ʒ������Ʒ�Ƿ�����Ʒ
SELECT '-------------------- Case5:��ѯ�����������Ʒ��һ�������Ʒ������Ʒ�Ƿ�����Ʒ -------------------------' AS 'Case5';
-- ������ͨ��ƷA
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,1);
SET @iCommodityID1 = LAST_INSERT_ID();
-- ����������ƷB
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB12','����12','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,3);
SET @iCommodityID2 = LAST_INSERT_ID();
-- ��������ƷA������Ʒ
INSERT INTO nbr.t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID1, @iCommodityID2, 10, 11.0, now(), now());
SET @iSubCommodityID1 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckSubCommodity(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('�����Ʒ', @iCommodityID1 ,'��Ӧ������Ʒ������ͨ��Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

DELETE FROM t_subcommodity WHERE F_ID = @iSubCommodityID1;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID1;

