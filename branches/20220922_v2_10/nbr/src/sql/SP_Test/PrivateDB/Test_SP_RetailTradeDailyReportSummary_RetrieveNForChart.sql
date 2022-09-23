SELECT '++++++++++++++++++ Test_SP_RetailTradeDailyReportSummary_RetrieveNForChart.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:��ѯһ�������ܶ������ë�������� -------------------------' AS 'Case1';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '-------------------- Case2:��ѯһ�����������ܶ������ë�������� -------------------------' AS 'Case2';
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
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '-------------------- Case3:��ѯһ�������ڵ�ʱ���ڵ������ܶ������ë�������� -------------------------' AS 'Case3';
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

SELECT '-------------------- Case4:��ѯһ�������ڵ��ŵ�ID�������ܶ������ë�� -------------------------' AS 'Case4';
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