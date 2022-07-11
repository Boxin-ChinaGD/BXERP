SELECT '++++++++++++++++++Test_SP_RetailTradeDailyReportByStaff_RetrieveN.sql+++++++++++++++++++++++';

SELECT '-----------------Case1:查询一天的员工数据 ------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2019-1-10 0:00:00';
SET @dtEnd = '2019-1-10 0:00:00';

CALL SP_RetailTradeDailyReportByStaff_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @dtStart, @dtEnd);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-----------------Case2:查询存在的时间段的员工数据 ------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2019-1-10 0:00:00';
SET @dtEnd = '2019-1-15 0:00:00';

CALL SP_RetailTradeDailyReportByStaff_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @dtStart, @dtEnd);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';


SELECT '-----------------Case3:查询不存在的时间段的员工数据  ------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = 2;
SET @dtStart = '2099-1-15 0:00:00';
SET @dtEnd = '2099-1-15 0:00:00';

CALL SP_RetailTradeDailyReportByStaff_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @dtStart, @dtEnd);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';


SELECT '-----------------Case4:查询不存在门店ID的员工数据 ------------------' AS 'Case4';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iShopID = -1;
SET @dtStart = '2019-1-10 0:00:00';
SET @dtEnd = '2019-1-10 0:00:00';

CALL SP_RetailTradeDailyReportByStaff_RetrieveN(@iErrorCode, @sErrorMsg, @iShopID, @dtStart, @dtEnd);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';
