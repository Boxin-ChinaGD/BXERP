SELECT '++++++++++++++++++ Test_SP_RetailTradeMonthlyReportSummary_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������ѯ -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2019/01/01 00:00:00';
SET @dtEnd = '2019/12/31 23:59:59';

CALL SP_RetailTradeMonthlyReportSummary_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @dtStart, @dtEnd);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:��ѯ�����ڵ�ʱ�� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2099/01/01 00:00:00';
SET @dtEnd = '2099/12/31 23:59:59';

CALL SP_RetailTradeMonthlyReportSummary_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @dtStart, @dtEnd);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:��ѯ�����ڵ��ŵ�ID -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @dtStart = '2019/01/01 00:00:00';
SET @dtEnd = '2019/12/31 23:59:59';

CALL SP_RetailTradeMonthlyReportSummary_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @dtStart, @dtEnd);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';