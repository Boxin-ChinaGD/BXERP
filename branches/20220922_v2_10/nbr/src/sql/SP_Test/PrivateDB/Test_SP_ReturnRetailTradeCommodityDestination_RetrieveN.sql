SELECT '++++++++++++++++++ Test_SP_ReturnRetailTradeCommodityDestination_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ʹ��RetailTradeCommodityID��ѯ���۵�ȥ��� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iRetailTradeCommodityID = 5320;
SET @iTotalRecord = 0;

CALL SP_ReturnRetailTradeCommodityDestination_RetrieveN(@iErrorCode, @sErrorMsg, @iRetailTradeCommodityID, @iTotalRecord);
SELECT @sErrorMsg;
SELECT @iTotalRecord;
SELECT IF(@sErrorMsg = '' AND @iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';