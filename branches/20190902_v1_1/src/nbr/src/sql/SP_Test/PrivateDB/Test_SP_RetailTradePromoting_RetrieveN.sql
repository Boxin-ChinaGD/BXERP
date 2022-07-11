SELECT '++++++++++++++++++ Test_SP_RetailTradePromoting_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常查询 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = 0;
SET @iPageIndex = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_RetailTradePromoting_RetrieveN(@iErrorCode, @sErrorMsg, @iTradeID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:根据TradeID查询 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTradeID = 2;
SET @iPageIndex = 0;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_RetailTradePromoting_RetrieveN(@iErrorCode, @sErrorMsg, @iTradeID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';