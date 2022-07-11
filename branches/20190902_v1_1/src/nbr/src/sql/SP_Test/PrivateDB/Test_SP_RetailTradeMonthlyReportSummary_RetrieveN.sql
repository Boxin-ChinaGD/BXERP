SELECT '++++++++++++++++++ Test_SP_RetailTradeMonthlyReportSummary_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常查询 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2019/01/01 00:00:00';
SET @dtEnd = '2019/12/31 23:59:59';

CALL SP_RetailTradeMonthlyReportSummary_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @dtStart, @dtEnd);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:查询不存在的时间 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2099/01/01 00:00:00';
SET @dtEnd = '2099/12/31 23:59:59';

CALL SP_RetailTradeMonthlyReportSummary_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @dtStart, @dtEnd);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:查询不存在的门店ID -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @dtStart = '2019/01/01 00:00:00';
SET @dtEnd = '2019/12/31 23:59:59';

CALL SP_RetailTradeMonthlyReportSummary_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @dtStart, @dtEnd);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';