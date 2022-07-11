SELECT '-----------------Test_SP_Role_RetrieveN.sql---------------------------------';
SELECT '----------------- case1：输入角色名称进行查询---------------------------------';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '收银员';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Role_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT '-----------------case2:不输入任何参数进行查询---------------------------------';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Role_RetrieveN(@iErrorCode, @sErrorMsg, @sName, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_role WHERE F_Name = @sName;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';