SELECT '++++++++++++++++++ Test_SP_RetailTradeCommoditySource_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ȫ����ѯ -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iRetailTradeCommodityID = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;


CALL SP_RetailTradeCommoditySource_RetrieveN(@iErrorCode, @sErrorMsg, @iRetailTradeCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: ��RetailTradeCommodityID��ѯ���۵���Դ�� -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iRetailTradeCommodityID = 17;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_RetailTradeCommoditySource_RetrieveN(@iErrorCode, @sErrorMsg, @iRetailTradeCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
