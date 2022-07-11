SELECT '++++++++++++++++++ Test_SP_PackageUnit_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 添加不重复的包装单位，错误码为0 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '包1';

CALL SP_PackageUnit_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_packageunit WHERE F_ID = last_insert_id();


SELECT '-------------------- Case2: 添加重复的包装单位，不能添加，错误码为1 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '盒';

CALL SP_PackageUnit_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_packageunit WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_packageunit WHERE F_ID = last_insert_id();