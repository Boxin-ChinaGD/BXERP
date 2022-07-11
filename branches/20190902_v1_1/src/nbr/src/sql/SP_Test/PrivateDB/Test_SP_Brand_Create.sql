SELECT '++++++++++++++++++Test_SP_Brand_Create.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 添加不重复的品牌，错误码为0(正常添加)------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '上好佳';

CALL SP_Brand_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_brand WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_brand WHERE F_ID = last_insert_id();

SELECT '-----------------Case2: 添加重复品牌，不能添加，错误码为1------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '统一';

CALL SP_Brand_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_brand WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_brand WHERE F_ID = last_insert_id();