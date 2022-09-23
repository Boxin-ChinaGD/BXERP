SELECT '++++++++++++++++++ Test_SPD_RefCommodityHub_CheckBarcode.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_RefCommodityHub_CheckBarcode(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:�����볤��С��7λ -------------------------' AS 'Case2';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
INSERT INTO staticdb.t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType,  F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale,  F_ShelfLife, F_ReturnDays)
VALUES ('123456','�ο���Ʒ1', '��Ƭ', '��', 'ƿ', '��', '���¿���',' Ĭ�����', 'SP', 1,  0, 8, 8, 12, 11.8, 11, 3, 30);
SET @iID = LAST_INSERT_ID();
-- 
CALL SPD_RefCommodityHub_CheckBarcode(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iID ,'�Ĳο���Ʒ�������볤�ȱ�����[7~20]') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM staticdb.t_refcommodityhub WHERE F_ID = @iID;


-- �����ֶ�F_Barcode��������VarChar(20)�ģ���������ݲ����ܴ���20λ�ģ�����ע�͵��ò�������
-- SELECT '-------------------- Case3:�����볤�ȴ���20λ -------------------------' AS 'Case3';
-- 
-- SET @iErrorCode = 0;
-- SET @sErrorMsg = '';
-- 
-- INSERT INTO nbr_bx.t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType,  F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale,  F_ShelfLife, F_ReturnDays)
-- VALUES ('123456123456123456123456','�ο���Ʒ1', '��Ƭ', '��', 'ƿ', '��', '���¿���',' Ĭ�����', 'SP', 1,  0, 8, 8, 12, 11.8, 11, 3, 30);
-- SET @iID = LAST_INSERT_ID();
-- 
-- CALL SPD_RefCommodityHub_CheckBarcode(@iErrorCode, @sErrorMsg);
-- SELECT @iErrorCode, @sErrorMsg;
-- 
-- SELECT IF(@sErrorMsg = CONCAT('IDΪ', @iID ,'�Ĳο���Ʒ�������볤�ȱ�����[7~20]') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
-- DELETE FROM nbr_bx.t_refcommodityhub WHERE F_ID = @iID;