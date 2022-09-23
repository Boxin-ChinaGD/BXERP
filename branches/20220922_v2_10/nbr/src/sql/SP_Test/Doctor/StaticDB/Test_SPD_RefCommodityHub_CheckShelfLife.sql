SELECT '++++++++++++++++++ Test_SPD_RefCommodityHub_CheckShelfLife.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RefCommodityHub_CheckShelfLife(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:�ο���Ʒ�ı�����Ϊ0 -------------------------' AS 'Case2';
-- 
INSERT INTO staticdb.t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType,  F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale,  F_ShelfLife, F_ReturnDays)
VALUES ('3548293894789','�ɱȿ���Ƭ1', '��Ƭ', '��', 'ƿ', '��', '���¿���',' Ĭ�����', 'SP', 1,  0, 8, 8, 12, 11.8, 11, 0, 30);
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RefCommodityHub_CheckShelfLife(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iID ,'�Ĳο���Ʒ�ı����ڲ���Ϊ��Ҳ����Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM staticdb.t_refcommodityhub WHERE F_ID = @iID;


-- �ڲ���ο���Ʒ����ʱ���ֶ�F_ShelfLife����Ϊnull,����ע�͸ò�������
-- SELECT '-------------------- Case3:�ο���Ʒ�ı�����ΪNull -------------------------' AS 'Case3';
-- 
-- INSERT INTO staticdb.t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType,  F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale,  F_ShelfLife, F_ReturnDays)
-- VALUES ('3548293894789','�ɱȿ���Ƭ1', '��Ƭ', '��', 'ƿ', '��', '���¿���',' Ĭ�����', 'SP', 1,  0, 8, 8, 12, 11.8, 11, NULL, 30);
-- SET @iID = LAST_INSERT_ID();
-- 
-- SET @iErrorCode = 0;
-- SET @sErrorMsg = '';
-- 
-- CALL SPD_RefCommodityHub_CheckShelfLife(@iErrorCode, @sErrorMsg);
-- SELECT @iErrorCode, @sErrorMsg;
-- 
-- SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iID ,'�Ĳο���Ʒ�ı����ڲ���Ϊ��Ҳ����Ϊ0') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
-- DELETE FROM staticdb.t_refcommodityhub WHERE F_ID = @iID;