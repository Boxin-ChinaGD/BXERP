SELECT '++++++++++++++++++ Test_SP_RetailTradeMonthlyReportSummary_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @dtEnd = '2000/07/31 23:59:59';
SET @iDeleteOldData = 0;
SET @iShopID = 2;


CALL SP_RetailTradeMonthlyReportSummary_Create(@iErrorCode, @sErrorMsg, @iShopID, @dtEnd, @iDeleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_retailtrademonthlyreportsummary WHERE F_Datetime = '2000/07/1 00:00:00';
SELECT IF(@iErrorCode = 0 AND found_rows() = 1, '测试成功', '测试失败') AS 'Case1 Testing Result';

-- 现在已经支持生成任意时间的月报
--	SELECT '-------------------- Case2:添加一个不存在的时间(根据日报创建月报，当日报没有数据不会创建当月的月报) -------------------------' AS 'Case2';
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @dtEnd = '2099-01-31 23:59:59';
--	
--	CALL SP_RetailTradeMonthlyReportSummary_Create(@iErrorCode, @sErrorMsg, @dtEnd);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';


SELECT '-------------------- Case3:添加一个时间不存在销售数据（用来测试ifnull（）） -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @dtEnd = '1970-04-30 23:59:59';
SET @iDeleteOldData = 1;
SET @iShopID = 2;

CALL SP_RetailTradeMonthlyReportSummary_Create(@iErrorCode, @sErrorMsg, @iShopID, @dtEnd, @iDeleteOldData);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';
