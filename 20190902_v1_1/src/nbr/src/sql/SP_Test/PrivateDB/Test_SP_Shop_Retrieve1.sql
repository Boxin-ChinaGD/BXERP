SELECT '++++++++++++++++++ Test_SP_Shop_Retrieve1.sql ++++++++++++++++++++';

SELECT '++++++++++++++++++CASE1:正常查询++++++++++++++++++++' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;

CALL SP_Shop_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_shop WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '++++++++++++++++++CASE2:用不存在的ID查询++++++++++++++++++++' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;

CALL SP_Shop_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT 1 FROM t_shop WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case2 Testing Result';