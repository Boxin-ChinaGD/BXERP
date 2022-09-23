SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckType.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:Type����0��1��2��3 -------------------------' AS 'Case2';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,4);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '����Ʒ����Type����ȷ����ֻ��Ϊ0��1��2��3') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case3:TypeΪ0��������ƷID��Ϊ0 -------------------------' AS 'Case3';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',2,0,'�ǰͿ�AB',0,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '�Ĳ�����ƷID����ȷ����Ʒ�������Ʒ�Ĳ�����ƷIDΪ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case4:TypeΪ0��������Ʒ������Ϊ0 -------------------------' AS 'Case4';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,5,'�ǰͿ�AB',0,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '�Ĳ�����Ʒ��������ȷ����Ʒ�������Ʒ�Ĳ�����Ʒ����Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case5:TypeΪ2��������ƷID������ -------------------------' AS 'Case5';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',9999,3,'�ǰͿ�AB',0,2);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '�Ĳ�����ƷID����ȷ�����װ��Ʒ�Ĳ�����ƷIDΪ���ڵĵ�Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case6:TypeΪ2��������ƷIDΪ���װ��Ʒ -------------------------' AS 'Case6';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',1,3,'�ǰͿ�AB',0,2);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',@iCommodityID,3,'�ǰͿ�AB',0,2);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID + 1, '�Ĳ�����ƷID����ȷ�����װ��Ʒ�Ĳ�����ƷIDΪ���ڵĵ�Ʒ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_RefCommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case7:TypeΪ2��������Ʒ����С��2 -------------------------' AS 'Case7';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',@iCommodityID,1,'�ǰͿ�AB',0,2);
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID + 1, '�Ĳ�����Ʒ��������ȷ�����װ��Ʒ�Ĳ�����Ʒ��������1') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_RefCommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case8:TypeΪ3��������ƷID��Ϊ0 -------------------------' AS 'Case8';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'��ͨ','���','��',4,NULL,4,4,'ST',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',2,0,'��ͨ���',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '�Ĳ�����ƷID����ȷ����Ʒ�������Ʒ�Ĳ�����ƷIDΪ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case9:TypeΪ3��������Ʒ������Ϊ0 -------------------------' AS 'Case9';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'��ͨ','���','��',4,NULL,4,4,'ZT',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,5,'��ͨ���',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckType(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID, '�Ĳ�����Ʒ��������ȷ����Ʒ�������Ʒ�Ĳ�����Ʒ����Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;