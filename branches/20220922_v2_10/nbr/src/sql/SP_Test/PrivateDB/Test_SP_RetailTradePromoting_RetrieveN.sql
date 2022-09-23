SELECT '++++++++++++++++++ Test_SP_RetailTradePromoting_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������ѯ -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = 0;
SET @iPageIndex = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_RetailTradePromoting_RetrieveN(@iErrorCode, @sErrorMsg, @iTradeID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:����TradeID��ѯ -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = 2;
SET @iPageIndex = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_RetailTradePromoting_RetrieveN(@iErrorCode, @sErrorMsg, @iTradeID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';