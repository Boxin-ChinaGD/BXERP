SELECT '----------------------------Test_SP_Role_Create.sql--------------------------------';
SELECT '----------------------case1:添加不重复的角色----------------------------';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '店长1';

CALL SP_Role_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

DELETE FROM t_role WHERE F_ID = last_insert_id();


SELECT '----------------------case2:添加重复的角色-----------------------------';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '收银员';

CALL SP_Role_Create(@iErrorCode, @sErrorMsg, @sName);

SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1, '测试成功', '测试失败') AS 'Testing Result';

DELETE FROM t_role WHERE F_ID = last_insert_id();