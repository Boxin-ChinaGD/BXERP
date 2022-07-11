-- case1: 查询一天
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2019/01/11 00:00:00';
SET @dtEnd = '2019/01/11 23:59:59';

CALL SP_RetailTradeDailyReportSummary_Retrieve1
	(
		@iErrorCode,
		@sErrorMsg,
		@iShopID,
		@dtStart,
		@dtEnd
	);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF (found_rows() = 1 AND @iErrorCode = 0, '测试成功','测试失败') AS 'Test Case1 Result';

-- case2: 查询一个月
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2019/01/01 00:00:00';
SET @dtEnd = '2019/01/31 23:59:59';

CALL SP_RetailTradeDailyReportSummary_Retrieve1
	(
		@iErrorCode,
		@sErrorMsg,
		@iShopID,
		@dtStart,
		@dtEnd
	);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF (found_rows() = 1 AND @iErrorCode = 0, '测试成功','测试失败') AS 'Test Case2 Result';


-- case3: 查询某个特定时间段
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2019/01/11 00:00:00';
SET @dtEnd = '2019/01/14 23:59:59';

CALL SP_RetailTradeDailyReportSummary_Retrieve1
	(
		@iErrorCode,
		@sErrorMsg,
		@iShopID,
		@dtStart,
		@dtEnd
	);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF (found_rows() = 1 AND @iErrorCode = 0, '测试成功','测试失败') AS 'Test Case3 Result';


-- case4:查询不存在的时间
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2020/01/01 00:00:00';
SET @dtEnd = '2020/01/31 23:59:59';

CALL SP_RetailTradeDailyReportSummary_Retrieve1
	(
		@iErrorCode,
		@sErrorMsg,
		@iShopID,
		@dtStart,
		@dtEnd
	);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF (@iErrorCode = 2, '测试成功','测试失败') AS 'Test Case4 Result';

-- case5:查询不存在的门店ID
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @dtStart = '2019/01/11 00:00:00';
SET @dtEnd = '2019/01/14 23:59:59';

CALL SP_RetailTradeDailyReportSummary_Retrieve1
	(
		@iErrorCode,
		@sErrorMsg,
		@iShopID,
		@dtStart,
		@dtEnd
	);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF (@iErrorCode = 2, '测试成功','测试失败') AS 'Test Case5 Result';