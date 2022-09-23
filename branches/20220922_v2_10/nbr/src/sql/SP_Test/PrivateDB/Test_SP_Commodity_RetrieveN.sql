SELECT '++++++++++++++++++ Test_SP_Commodity_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:����iCategoryID��Ϊ������ѯ -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = 2;
SET @iBrandID = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @dStartDatetime = NULL;
SET @dEndDatetime = NULL;
SET @String1 = '';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @String1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';
SELECT '-------------------- Case2:����iBrandID��Ϊ������ѯ -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = -1;
SET @iBrandID = 3;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @String1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() =  1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

SELECT '-------------------- Case3:����string1����name��Ϊ������ѯ -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '��Ը����ζ��Ƭ';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() =  1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';

SELECT '-------------------- Case4:����sMnemonicCode��Ϊ������ѯ -------------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @sName = '';
-- SET @sMnemonicCode = 'SP';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = 'SP';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() =  1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE4 Testing Result';

SELECT '-------------------- Case5:�������6λ��sBarcode��Ϊ������ѯ -------------------------' AS 'Case5';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '3548293';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() =  1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

SELECT '-------------------- Case6:��������ѯ -------------------------' AS 'Case6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE6 Testing Result';

SELECT '-------------------- Case7:����iNO��Ϊ������ѯ -------------------------' AS 'Case7';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = 1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';

SELECT '-------------------- Case8:����iStatus��Ϊ������ѯ -------------------------' AS 'Case8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = 0;
SET @iNO = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE10 Testing Result';

SELECT '-------------------- Case9:����iStatus��iNO��Ϊ������ѯ -------------------------' AS 'Case9';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = 0;
SET @iNO = 1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';
 
CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE11 Testing Result';

SELECT '-------------------- Case10:����iType��Ϊ������ѯ -------------------------' AS 'Case10';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';
SET @dStartDatetime = NULL;
SET @dEndDatetime = NULL;

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE12 Testing Result';

SELECT '-------------------- Case11:����iType��iStatus��Ϊ������ѯ -------------------------' AS 'Case11';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = 0;
SET @iNO = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE13 Testing Result';

SELECT '-------------------- Case12:����iType��iNO��Ϊ������ѯ -------------------------' AS 'Case12';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = 1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE14 Testing Result';

SELECT '------------------- case 13: �����������ѯ��Ʒ����Ϊ2�� ----------------------' AS 'Case13';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '25452145462876975';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE15 Testing Result';


SELECT '------------------- case 14: ���������Ʒ����һ�� ----------------------' AS 'Case14';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'55555555558','����','��',4,'֧',4,4,'SP',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0);
SET @iCommodityID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'55555555559','����','��',4,'֧',4,4,'SP',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0);
SET @iCommodityID2 = last_insert_id();

INSERT INTO T_Barcodes (F_CommodityID,F_Barcode)
VALUES (@iCommodityID2,'55555555558');
SET @iBarcodes = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '55555555558';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE14 Testing Result';

DELETE FROM t_barcodes WHERE F_ID = @iBarcodes;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '------------------- case 15: ��Ʒ���������Ʒ����һ�� ----------------------' AS 'Case15';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'55555556558','����','��',4,'֧',4,4,'ty',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0);
SET @iCommodityID = last_insert_id();

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'ty','����','��',4,'֧',4,4,'SP',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0);
SET @iCommodityID2 = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = 'ty';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iTotalRecord = 2 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE16 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID2;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '------------------- case 16: ͨ��name��ѯ���װ��Ʒ ----------------------' AS 'Case16';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'����16','kf16','��',4,'֧',4,4,'ty',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',2);
SET @iCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = -1;
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '����16';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE16 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '------------------- case 17: ͨ��name��ѯ�����Ʒ ----------------------' AS 'Case17';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'����17','kf17','��',4,'֧',4,4,'ty',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',1);
SET @iCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = -1;
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '����17';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iTotalRecord = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE17 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '------------------- case 18: ����һʱ��ν��в�ѯ�� ----------------------' AS 'Case18';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'55555558558','����17','��',4,'֧',4,4,'ty',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2017/01/14','20',0,0,'�ǰͿ�AB',1);
SET @iCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = -1;
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';
SET @dStartDatetime = '2017/1/13';
SET @dEndDatetime = '2017/1/15';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iTotalRecord = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE18 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '------------------- case 19: ��ѯĳʱ������Ʒ�� ----------------------' AS 'Case19';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'55555558558','����17','��',4,'֧',4,4,'ty',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2017/01/14','20',0,0,'�ǰͿ�AB',1);
SET @iCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = -1;
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';
SET @dStartDatetime = '2017/1/13';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iTotalRecord = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE19 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '------------------- case 20: ��ѯĳ��ʱ��ǰ����Ʒ�� ----------------------' AS 'Case20';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'55555558558','����17','��',4,'֧',4,4,'ty',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2017/01/14','20',0,0,'�ǰͿ�AB',1);
SET @iCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = -1;
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';
SET @dEndDatetime = '2017/1/15';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iTotalRecord = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE20 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '------------------- case 21: ��ʼʱ����ڽ���ʱ�䡣 ----------------------' AS 'Case21';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'55555558558','����17','��',4,'֧',4,4,'ty',1,
5.8,5.5,1,1,'url=116843434834',
3,30,'2017/01/14','20',0,0,'�ǰͿ�AB',1);
SET @iCommodityID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @iStatus = -1;
SET @iNO = -1;
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '';
SET @dStartDatetime = '2017/1/15';
SET @dEndDatetime = '2017/1/13';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE21 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case22:����С��7λ��sBarcode��Ϊ������ѯ�������ǲ�ѯ������Ʒ -------------------------' AS 'Case22';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '354829';
SET @dStartDatetime = '1970/1/1';
SET @dEndDatetime = '2019/7/2';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE22 Testing Result';

SELECT '-------------------- Case23:����string1����_�������ַ�����ģ������ -------------------------' AS 'Case23';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'������1_2��','����','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @dStartDatetime = NULL;
SET @dEndDatetime = NULL;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '_';

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO, @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0 AND @iTotalRecord > 0, '���Գɹ�', '����ʧ��') AS 'CASE23 Testing Result';

DELETE FROM t_commodity WHERE F_Name = '������1_2��';

SELECT '-------------------- Case24:������123456789 12345678 ����1234567 ���� -------------------------' AS 'Case22';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'cjs666','��������','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);

SET @iCommodityID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID, '123456789', now(), now());

INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID, '12345678', now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '1234567';
SET @dStartDatetime = '1970/1/1';
SET @dEndDatetime = now();

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE22 Testing Result';

DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case25:����64λ������ ���� -------------------------' AS 'Case25';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'cjs666','��������','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);

SET @iCommodityID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID, '1234567812345678123456781234567812345678123456781234567812345678', now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '1234567812345678123456781234567812345678123456781234567812345678';
SET @dStartDatetime = '1970/1/1';
SET @dEndDatetime = now();

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE25 Testing Result';

DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case26:�������64λ������ ���� -------------------------' AS 'Case26';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'cjs666','��������','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);

SET @iCommodityID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime)
VALUES (@iCommodityID, '1234567812345678123456781234567812345678123456781234567812345678', now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = -1;
SET @iNO = -1;
SET @iCategoryID = -1;
SET @iBrandID = -1;
SET @sName = '';
SET @sMnemonicCode = '';
SET @iType = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @string1 = '12345678123456781234567812345678123456781234567812345678123456787777';
SELECT length(@string1);
SET @dStartDatetime = '1970/1/1';
SET @dEndDatetime = now();

CALL SP_Commodity_RetrieveN(@iErrorCode, @sErrorMsg, @iStatus, @iNO,  @iCategoryID, @iBrandID, @iType, @dStartDatetime, @dEndDatetime, @string1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE26 Testing Result';

DELETE FROM t_barcodes WHERE F_CommodityID = @iCommodityID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;