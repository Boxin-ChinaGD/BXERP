-- case1:正常修改
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @sName = '清洁工';

CALL SP_Role_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

-- case2：修改的角色权限已存在，错误码为1
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 3;
SET @sName = '店长';

CALL SP_Role_Update(@iErrorCode, @sErrorMsg, @iID, @sName);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '测试成功', '测试失败') AS 'Testing Result';
