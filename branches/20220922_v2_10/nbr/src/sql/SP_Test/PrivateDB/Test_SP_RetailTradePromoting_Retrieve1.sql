SELECT '++++++++++++++++++ Test_SP_RetailTradePromoting_Retrieve1.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;

CALL SP_RetailTradePromoting_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_RetailTradePromoting WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:ID������ -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;

CALL SP_RetailTradePromoting_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_RetailTradePromoting WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';