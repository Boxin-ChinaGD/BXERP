SELECT '++++++++++++++++++ Test_SP_BxStaff_RetrieveN.sql ++++++++++++++++++++';

SELECT '+++++++++++++++++++++++ case1:正常查询 ++++++++++++++++++++++++++++++++++++' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_BxStaff_RetrieveN(@iErrorCode, @sErrorMsg, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';