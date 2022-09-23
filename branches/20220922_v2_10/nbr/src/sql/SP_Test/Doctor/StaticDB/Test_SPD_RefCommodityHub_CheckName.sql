SELECT '++++++++++++++++++ Test_SPD_RefCommodityHub_CheckName.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RefCommodityHub_CheckName(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:��Ʒ����Ϊ'' -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
INSERT INTO staticdb.t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType,  F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale,  F_ShelfLife, F_ReturnDays)
VALUES ('12345678','', '��Ƭ', '��', 'ƿ', '��', '���¿���',' Ĭ�����', 'SP', 1,  0, 8, 8, 12, 11.8, 11, 3, 30);
SET @iID = LAST_INSERT_ID();
-- 
CALL SPD_RefCommodityHub_CheckName(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iID ,'�Ĳο���Ʒ����Ʒ���Ʋ���Ϊ��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM staticdb.t_refcommodityhub WHERE F_ID = @iID;


-- �ڲ���ο���Ʒ����ʱ���ֶ�F_Name����Ϊnull,����ע�͵��ò�������
-- SELECT '-------------------- Case3:��Ʒ����Ϊnull -------------------------' AS 'Case3';
-- 
-- SET @iErrorCode = 0;
-- SET @sErrorMsg = '';
-- 
-- INSERT INTO staticdb.t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType,  F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale,  F_ShelfLife, F_ReturnDays)
-- VALUES ('12345678',NULL, '��Ƭ', '��', 'ƿ', '��', '���¿���',' Ĭ�����', 'SP', 1,  0, 8, 8, 12, 11.8, 11, 3, 30);
-- SET @iID = LAST_INSERT_ID();
-- 
-- CALL SPD_RefCommodityHub_CheckName(@iErrorCode, @sErrorMsg);
-- SELECT @iErrorCode, @sErrorMsg;
-- 
-- SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iID ,'�Ĳο���Ʒ����Ʒ���Ʋ���Ϊ��') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
-- DELETE FROM staticdb.t_refcommodityhub WHERE F_ID = @iID;