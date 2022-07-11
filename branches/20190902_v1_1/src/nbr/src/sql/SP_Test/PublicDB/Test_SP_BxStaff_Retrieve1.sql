SELECT '+++++++++++++++++++++++++++ Test_SP_BxStaff_Retrieve1.sql +++++++++++++++++++++++++++++++++++++++++++++';

SELECT '+++++++++++++++++++++++++++ case1:正常查询 +++++++++++++++++++++++++++++++++++++++' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sMobile = '';

CALL SP_BxStaff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sMobile);

SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '','测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '+++++++++++++++++++++++++++ case2:用不存在的ID查询 +++++++++++++++++++++++++++++++++++++++' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;
SET @sMobile = '';

CALL SP_BxStaff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sMobile);

SELECT IF(found_rows() = 0 AND @iErrorCode = 0 AND @sErrorMsg = '','测试成功', '测试失败') AS 'case2 Testing Result';

SELECT '+++++++++++++++++++++++++++ case3:用moblie正常查询 +++++++++++++++++++++++++++++++++++++++' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sMobile = '13462346281';

CALL SP_BxStaff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sMobile);

SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '','测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '+++++++++++++++++++++++++++ case4:用不存在的moblie查询 +++++++++++++++++++++++++++++++++++++++' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @sMobile = '-9999';

CALL SP_BxStaff_Retrieve1(@iErrorCode, @sErrorMsg, @iID, @sMobile);

SELECT IF(found_rows() = 0 AND @iErrorCode = 0 AND @sErrorMsg = '','测试成功', '测试失败') AS 'Case4 Testing Result';
