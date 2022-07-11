SELECT '++++++++++++++++++ Test_SP_Shop_RetrieveN.sql ++++++++++++++++++++';

SELECT '+++++++++++++++++++++++ case1:正常查询 ++++++++++++++++++++++++++++++++++++' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iDistrictID= -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iDistrictID, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '+++++++++++++++++++++++ case2:输入iDistrictID作为条件查询 ++++++++++++++++++++++++++++++++++++' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iDistrictID= 1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Shop_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iDistrictID, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';