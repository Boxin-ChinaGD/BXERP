SELECT '++++++++++++++++++ Test_SP_RefCommodityHub_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �������������ѯ,���Ϊ�� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;
SET @sBarcode = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_RefCommodityHub_RetrieveN(@iErrorCode, @sErrorMsg, @sBarcode, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: �����������ѯ -------------------------' AS 'Case2';
SET @sBarcode = '692087750015fa8';
INSERT INTO t_refcommodityhub (F_Barcode, F_Name, F_ShortName, F_Specification, F_PackageUnitName, F_PurchasingUnit, F_BrandName, F_CategoryName, F_MnemonicCode, F_PricingType, F_Type, F_PricePurchase, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_ShelfLife, F_ReturnDays)
VALUES (@sBarcode,'ɺ���f�ĵعϸ�120��(��)',NULL,'','NULL',NULL,NULL,NULL,'',1,0,0.000000,0.000000,0.000000,0.000000,0.000000,360,7);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_RefCommodityHub_RetrieveN(@iErrorCode, @sErrorMsg, @sBarcode, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_refcommodityhub WHERE F_ID = @iID;

SELECT '-------------------- Case3: ���벻�����������ѯ,���Ϊ�� -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;
SET @sBarcode = '6920877';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_RefCommodityHub_RetrieveN(@iErrorCode, @sErrorMsg, @sBarcode, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';