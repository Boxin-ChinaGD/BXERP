SELECT '++++++++++++++++++ Test_SP_RetailTradeDailyReportSummary_RetrieveNForChart.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询一天销售总额和销售毛利及日期 -------------------------' AS 'Case1';
SET @dtStart ='2019/01/14 00:00:00';
SET @dtEnd = "2019/01/14 23:59:59";
SET @iShopID = 2;
SET @sErrorMsg = '';

CALL SP_RetailTradeDailyReportSummary_RetrieveNForChart
(
	@iErrorCode,
	@sErrorMsg,
	@iShopID,
	@dtStart, 
	@dtEnd
);

SELECT @iErrorCode;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:查询一个月内销售总额和销售毛利及日期 -------------------------' AS 'Case2';
SET @dtStart ='2019/01/01 00:00:00';
SET @dtEnd = "2019/01/31 23:59:59";
SET @iShopID = 2;
SET @sErrorMsg = '';

CALL SP_RetailTradeDailyReportSummary_RetrieveNForChart
(
	@iErrorCode,
	@sErrorMsg,
	@iShopID,
	@dtStart, 
	@dtEnd
);

SELECT @iErrorCode;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-------------------- Case3:查询一个不存在的时间内的销售总额和销售毛利及日期 -------------------------' AS 'Case3';
SET @dtStart ='2099/01/01 00:00:00';
SET @dtEnd = "2099/01/31 23:59:59";
SET @iShopID = 2;
SET @sErrorMsg = '';

CALL SP_RetailTradeDailyReportSummary_RetrieveNForChart
(
	@iErrorCode,
	@sErrorMsg,
	@iShopID,
	@dtStart, 
	@dtEnd
);

SELECT @iErrorCode; 

SELECT '-------------------- Case4:查询一个不存在的门店ID的销售总额和销售毛利 -------------------------' AS 'Case4';
SET @dtStart ='2019/01/01 00:00:00';
SET @dtEnd = "2019/01/31 23:59:59";
SET @iShopID = -1;
SET @sErrorMsg = '';

CALL SP_RetailTradeDailyReportSummary_RetrieveNForChart
(
	@iErrorCode,
	@sErrorMsg,
	@iShopID,
	@dtStart, 
	@dtEnd
);

SELECT @iErrorCode; 