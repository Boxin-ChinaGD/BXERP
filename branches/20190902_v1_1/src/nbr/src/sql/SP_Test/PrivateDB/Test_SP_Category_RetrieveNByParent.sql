SELECT '++++++++++++++++++Test_SP_Category_RetrieveNByParent.sql+++++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 2; -- 传入大类id 得到相应的小类id与名称


CALL SP_Category_RetrieveNByParent(@iErrorCode, @sErrorMsg, @iID);
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';