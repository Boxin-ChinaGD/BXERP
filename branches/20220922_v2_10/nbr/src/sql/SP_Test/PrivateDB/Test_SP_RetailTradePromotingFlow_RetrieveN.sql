SELECT '++++++++++++++++++ Test_SP_RetailTradePromotingFlow_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ��ѯ����������̼�¼ -------------------------' AS 'Case1';
SET @iID = 1;
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_RetailTradePromotingFlow_RetrieveN(@iErrorCode, @sErrorMsg, @iID,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @sErrorMsg;
SELECT IF(@iTotalRecord >= 2 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';